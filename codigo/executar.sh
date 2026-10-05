#!/bin/sh
# Executa o programa. Sem argumentos abre o menu interativo.
#   ./executar.sh ABCBDAB BDCABA
#   ./executar.sh --arquivo testes/caso_dna_livro.txt
#   ./executar.sh --testes
#   ./executar.sh --testes --completo
cd "$(dirname "$0")" || exit 1
if [ ! -f bin/Main.class ]; then
    sh ./compilar.sh || exit 1
fi
java -Xmx2g -cp bin Main "$@"
