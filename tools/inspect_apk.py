"""Inventory an APK without extracting or launching it. Python 3.10+."""
import argparse
from collections import Counter
import hashlib
import json
from pathlib import Path
import re
import struct
from zipfile import ZipFile

RULES = {
    "private-key": rb"-----BEGIN [A-Z0-9 ]{0,40}PRIVATE KEY-----",
    "github": rb"\b(?:gh[pousr]_[A-Za-z0-9]{30,}|github_pat_[A-Za-z0-9_]{30,})\b",
    "aws-access-id": rb"\b(?:AKIA|ASIA)[A-Z0-9]{16}\b",
    "google-api-key": rb"\bAIza[A-Za-z0-9_-]{35}\b",
    "jwt": rb"\beyJ[A-Za-z0-9_-]{7,}\.eyJ[A-Za-z0-9_-]{7,}\.[A-Za-z0-9_-]{16,}\b",
    "slack-token": rb"\bxox[baprs]-[A-Za-z0-9-]{15,}\b",
    "mapbox-token": rb"\b(?:pk|sk)\.eyJ[A-Za-z0-9_-]{8,}\.[A-Za-z0-9_-]{10,}\b",
    "stripe-secret": rb"\b(?:sk|rk)_(?:live|test)_[A-Za-z0-9]{12,}\b",
    "service-secret": rb"\b(?:sk-(?:proj-|ant-)?|npm_)[A-Za-z0-9_-]{25,}\b",
    "credential-url": rb"https?://[A-Za-z0-9._%+-]{1,100}:[^/\s\x00\"'<>@]{3,140}@[A-Za-z0-9.-]+",
    "url-secret": rb"[?&](?:api[_-]?key|access[_-]?token|client[_-]?secret|secret|signature)=[A-Za-z0-9._%+-]{8,}",
    "assigned-secret": rb"(?i)[\"']?(?:password|passwd|api[_-]?key|client[_-]?secret|access[_-]?token|refresh[_-]?token|authorization)[\"']?\s*[:=]\s*[\"'][A-Za-z0-9_./+%=-]{12,}[\"']",
}
PATTERNS = {name: re.compile(rule) for name, rule in RULES.items()}
MAX_ENTRY = 128 * 1024 * 1024
MAX_SCAN = 512 * 1024 * 1024
HERMES_MAGIC = 0x1F1903C103BC1FC6


def file_hash(path):
    with path.open("rb") as handle:
        return hashlib.file_digest(handle, "sha256").hexdigest()


def scan(data):
    results = Counter()
    # Android resources and native blobs can contain UTF-16LE ASCII runs.
    for candidate in (data, re.sub(rb"((?:[\x20-\x7e]\x00){12,})", lambda m: m[0][::2], data)):
        for name, pattern in PATTERNS.items():
            results[name] = max(results[name], sum(1 for _ in pattern.finditer(candidate)))
    return {name: count for name, count in results.items() if count}


def inspect_apk(path):
    result = {"sha256": file_hash(path), "size_bytes": path.stat().st_size}
    groups, features, hits, entries = Counter(), {}, [], []
    scanned_bytes = 0
    with ZipFile(path) as apk:
        for index, item in enumerate(apk.infolist(), 1):
            name = item.filename
            groups[name.split("/")[0]] += 1
            entries.append({"path": name, "bytes": item.file_size, "compressed_bytes": item.compress_size})
            if item.file_size > MAX_ENTRY or scanned_bytes + item.file_size > MAX_SCAN:
                raise ValueError("APK exceeds the configured decompression limits")
            data = apk.read(item)
            scanned_bytes += len(data)
            matched = scan(data)
            if matched:
                hits.append({"entry_index": index, "rules": matched})
            if re.fullmatch(r"classes(?:\d+)?\.dex", name) and data.startswith(b"dex\n") and len(data) >= 112:
                features.setdefault("dex", {})[name] = {
                    "format": data[4:7].decode("ascii", "replace"),
                    "strings": struct.unpack_from("<I", data, 56)[0],
                    "class_definitions": struct.unpack_from("<I", data, 96)[0],
                }
            if name == "assets/index.android.bundle" and len(data) >= 12:
                magic, version = struct.unpack_from("<QI", data)
                features["bundle"] = {"type": "Hermes" if magic == HERMES_MAGIC else "unknown"}
                if magic == HERMES_MAGIC:
                    features["bundle"]["bytecode_version"] = version
            if name == "assets/app.config":
                config = json.loads(data)
                features["expo"] = {key: config[key] for key in ("slug", "version", "sdkVersion") if key in config}
            if re.fullmatch(r"lib/[^/]+/[^/]+\.so", name):
                features.setdefault("native_abis", {}).setdefault(name.split("/")[1], []).append(name.split("/")[2])
    result.update(entry_count=len(entries), groups=groups, features=features, entries=entries, secret_indicators=hits)
    return result


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("path", type=Path)
    parser.add_argument("--tree", action="store_true", help="Scan files in a directory, excluding .git")
    parser.add_argument("--output", type=Path, help="Write JSON to this file instead of stdout")
    args = parser.parse_args()
    if args.tree:
        result = {"secret_indicators": []}
        for file in sorted(args.path.rglob("*")):
            rel = file.relative_to(args.path)
            if ".git" in rel.parts or not file.is_file():
                continue
            if file.stat().st_size > MAX_ENTRY:
                raise ValueError(f"File exceeds scan limit: {rel}")
            matches = scan(file.read_bytes())
            if matches:
                result["secret_indicators"].append({"path": rel.as_posix(), "rules": matches})
    else:
        result = inspect_apk(args.path)
    content = json.dumps(result, ensure_ascii=True, indent=2) + "\n"
    if args.output:
        args.output.write_text(content, encoding="utf-8")
    else:
        print(content, end="")


if __name__ == "__main__":
    main()
