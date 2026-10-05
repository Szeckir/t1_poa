#!/bin/sh
# Compila o projeto. Requer JDK 11 ou superior (javac no PATH).
cd "$(dirname "$0")" || exit 1
mkdir -p bin
javac -encoding UTF-8 -d bin src/*.java || exit 1
echo "Compilação concluída. Classes geradas em bin/"
