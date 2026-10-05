# Trabalho 1 — Maior Subsequência Comum (LCS)

PUCRS — Projeto e Otimização de Algoritmos — Prof. Michael da Costa Móra
Dhara Hadi e Thomaz Gomes Szeckir

Implementação em Java do cálculo do comprimento da LCS entre duas strings:

- `LCSProgramacaoDinamica.java` — **Programação Dinâmica bottom-up** (implementação principal); também reconstrói uma LCS;
- `LCSForcaBruta.java` — algoritmo recursivo de força bruta, para comparação.

Cada algoritmo contabiliza suas iterações e o programa mede o tempo de execução de cada teste.

## Requisitos

JDK 11 ou superior (`javac` e `java` no PATH). Nenhuma biblioteca externa.

## Compilar

- Linux/macOS: `./compilar.sh`
- Windows: `compilar.bat`
- Manualmente: `javac -encoding UTF-8 -d bin src/*.java`

Se o Linux/macOS responder `Permission denied` (o .zip pode não guardar a permissão de execução dos scripts), use `sh compilar.sh` e `sh executar.sh ...`, ou rode `chmod +x *.sh` uma vez.

## Executar

```
./executar.sh                                  # menu interativo
./executar.sh ABCBDAB BDCABA                   # LCS entre duas strings
./executar.sh --arquivo testes/caso_classico.txt
./executar.sh --testes                         # bateria de testes (~10 s)
./executar.sh --testes --completo              # executa a força bruta também nos casos 08 e 09 (~3-4 min)
```

No Windows, use `executar.bat` com os mesmos argumentos.
Strings com espaços devem ir entre aspas: `./executar.sh "PROGRAMACAO DINAMICA" "PROJETO DE ALGORITMOS"`.

A força bruta só é executada quando m + n ≤ 32 (ou m + n ≤ 60 com `--completo`) e quando faria no máximo 10^11 chamadas recursivas. O programa conta essas chamadas antes de rodar, em O(m·n), para não travar com entradas como 30 × 30 sem nenhum caractere em comum, que levariam anos.

## Arquivos de entrada (`testes/`)

Linhas iniciadas com `#` são comentários. A primeira linha restante é S1 e a segunda é S2 (qualquer uma pode ser vazia).
O arquivo pode estar em UTF-8 (com ou sem BOM), UTF-16 com BOM ou ANSI (Latin-1). No menu, o caminho pode ser colado com ou sem aspas.

## Saída

A bateria de testes grava `resultados/bateria_testes.csv` com iterações e tempos de cada caso.
