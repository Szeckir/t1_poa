@echo off
rem Executa o programa. Sem argumentos abre o menu interativo.
rem   executar.bat ABCBDAB BDCABA
rem   executar.bat --arquivo testes\caso_dna_livro.txt
rem   executar.bat --testes
rem   executar.bat --testes --completo
cd /d "%~dp0"
chcp 65001 >nul
if not exist bin\Main.class (
    call .\compilar.bat
    if errorlevel 1 exit /b 1
)
java -Xmx2g -Dfile.encoding=UTF-8 -Dstdout.encoding=UTF-8 -cp bin Main %*
