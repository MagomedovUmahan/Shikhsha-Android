# Повторить разбор локально

Потребуется исходный APK и Python 3.10+. Запускайте из корня репозитория. Замените путь `path/to/app.apk` на свой.

```sh
python tools/inspect_apk.py "path/to/app.apk"
python tools/inspect_apk.py . --tree
```

Первый вызов проверит читаемость ZIP-записей и выдаст JSON с SHA-256, DEX, архитектурами и всеми файлами. Второй проверит содержимое папки на типовые секреты и напечатает только имена файлов, правила и числа совпадений. Номер записи APK в предупреждениях соответствует порядку в массиве `entries`, начиная с 1.

Для анализа Android-кода используйте [JADX 1.5.6](https://github.com/skylot/jadx/releases/tag/v1.5.6). Для слоя React Native используйте [hermes-dec](https://github.com/P1sec/hermes-dec) с поддержкой байткода 99. Вывод пишите в игнорируемый каталог `local/`.

```sh
mkdir local
jadx -d local/android "path/to/app.apk"
python -c "from pathlib import Path; from zipfile import ZipFile; p=ZipFile('path/to/app.apk'); Path('local/index.android.bundle').write_bytes(p.read('assets/index.android.bundle'))"
hbc-decompiler local/index.android.bundle local/hermes-pseudocode.js
python tools/inspect_apk.py local --tree
```

Не переносите автоматически локальный вывод в коммит. APK и полные дампы содержат сторонний код и могут содержать строки, которые надо проверить вручную. Если обнаружен рабочий секрет, его нужно отозвать и выпустить заново: он извлекаем из уже выданного APK.
