#!/bin/bash

export JAVA_HOME=$(/usr/libexec/java_home)

JAVAFX_PATH="/Users/davidkozahmetov/java/javafx-sdk-23.0.1"

# Директория для артефактов компиляции
OUT_DIR="./out"

# Создание директории для артефактов, если она не существует
mkdir -p "$OUT_DIR"

# Компиляция с указанием папки для выходных файлов
javac --module-path "$JAVAFX_PATH/lib" \
      --add-modules=javafx.controls,javafx.fxml \
      -d "$OUT_DIR" \
      StudentManagementSystem.java

# Запуск программы из папки out
java --module-path "$JAVAFX_PATH/lib" \
     --add-modules=javafx.controls,javafx.fxml \
     -cp "$OUT_DIR" \
     StudentManagementSystem
