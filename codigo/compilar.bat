@echo off
rem Compila o projeto. Requer JDK 11 ou superior (javac no PATH).
cd /d "%~dp0"
if not exist bin mkdir bin
javac -encoding UTF-8 -d bin src\*.java
if errorlevel 1 exit /b 1
echo Compilacao concluida. Classes geradas em bin\
