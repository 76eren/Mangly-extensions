# Mangly-Extension

Mangly-Extension is the official companion app for [Mangly](https://github.com/76eren/Mangly), the open-source manga and comic reader. It provides the tools needed to develop and package extensions, which act as content sources for the main Mangly app.

These extensions are imported into Mangly to fetch and display manga/comic content from supported websites.

---

## Status

This project is currently in active development. Expect frequent updates, breaking changes, and incomplete features as we iterate rapidly.

---

## How to build
```bash
.\gradlew build
```

This will generate a .mangly file, which then can be imported into the main App. 
The .mangly file is essentially a zip file containing the extension's code and resources.

---

## Disclaimer

This project does **not** host or distribute any manga, comics, or copyrighted material.

The Mangly Extension Builder exists solely as a **development tool**. It contains **no actual content sources**, only **sample/test extensions** for demonstration and testing purposes.

**We do not support or condone piracy.** Developers are responsible for ensuring their extensions comply with the terms of service and copyright laws of the content providers they target.

---