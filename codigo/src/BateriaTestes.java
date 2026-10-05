import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

public final class BateriaTestes {

    private BateriaTestes() {
    }

    // Cada caso: {id, descrição, S1, S2}
    static String[][] casos() {
        return new String[][] {
            {"01", "Exemplo clássico (Cormen)", "ABCBDAB", "BDCABA"},
            {"02", "Caracteres intercalados", "AGGTAB", "GXTXAYB"},
            {"03", "Strings iguais", "ALGORITMO", "ALGORITMO"},
            {"04", "Nada em comum", "ABCDEF", "GHIJKL"},
            {"05", "String vazia", "", "PUCRS"},
            {"06", "Palavras parecidas", "ALGORITMO", "LOGARITMO"},
            {"07", "Pior caso da força bruta", Main.repetir('A', 15), Main.repetir('B', 15)},
            {"08", "Frases com espaços", "PROGRAMACAO DINAMICA", "PROJETO DE ALGORITMOS"},
            {"09", "DNA do livro (Cormen)", "ACCGGTCGAGTGCGCGGAAGCCGGCCGAA", "GTCGTTCGGAATGCCGTTGCTCTGTAAA"},
            {"10", "DNA aleatório", aleatoria(1_000, "ACGT", 1), aleatoria(1_000, "ACGT", 2)},
            {"11", "Letras aleatórias", aleatoria(5_000, "ABCDEFGHIJKLMNOPQRSTUVWXYZ", 3),
                    aleatoria(5_000, "ABCDEFGHIJKLMNOPQRSTUVWXYZ", 4)},
        };
    }

    public static void executar(int limiteFB) throws IOException {
        String[][] casos = casos();
        Main.Linha[] linhas = new Main.Linha[casos.length];
        for (int k = 0; k < casos.length; k++) {
            String[] c = casos[k];
            linhas[k] = Main.analisar("Caso " + c[0] + " - " + c[1], c[2], c[3], limiteFB);
        }

        System.out.println(Main.repetir('=', 96));
        System.out.println(" RESUMO DA BATERIA DE TESTES (iterações e tempo médio de execução)");
        System.out.println(Main.repetir('=', 96));
        System.out.printf(" %-4s %-26s %13s %6s | %17s %11s | %12s %11s%n",
                "Caso", "Descrição", "m x n", "LCS", "FB iterações", "FB tempo", "PD iterações", "PD tempo");
        System.out.println(" " + Main.repetir('-', 95));
        for (int k = 0; k < casos.length; k++) {
            Main.Linha r = linhas[k];
            System.out.printf(" %-4s %-26s %13s %6s | %17s %11s | %12s %11s%n",
                    casos[k][0], casos[k][1], Main.inteiro(r.m) + " x " + Main.inteiro(r.n), Main.inteiro(r.lcs),
                    r.fbExecutada ? Main.inteiro(r.fbIteracoes) : "não executada",
                    r.fbExecutada ? Main.tempo(r.fbTempoNs) : "-",
                    Main.inteiro(r.pdIteracoes), Main.tempo(r.pdTempoNs));
        }
        System.out.println(" FB = força bruta (iterações = chamadas recursivas); PD = programação dinâmica (iterações = células).");

        Path pasta = Paths.get("resultados");
        Files.createDirectories(pasta);
        Path arquivo = pasta.resolve("bateria_testes.csv");
        try (PrintWriter out = new PrintWriter(Files.newBufferedWriter(arquivo, StandardCharsets.UTF_8))) {
            out.println("caso;descricao;m;n;lcs;fb_iteracoes;fb_tempo_ns;pd_iteracoes;pd_tempo_ns");
            for (int k = 0; k < casos.length; k++) {
                Main.Linha r = linhas[k];
                out.printf(java.util.Locale.ROOT, "%s;%s;%d;%d;%d;%s;%s;%d;%.1f%n", casos[k][0], casos[k][1], r.m, r.n, r.lcs,
                        r.fbExecutada ? String.valueOf(r.fbIteracoes) : "",
                        r.fbExecutada ? String.format(java.util.Locale.ROOT, "%.1f", r.fbTempoNs) : "",
                        r.pdIteracoes, r.pdTempoNs);
            }
        }
        System.out.println(" Resultados gravados em " + arquivo);
    }

    static String aleatoria(int tamanho, String alfabeto, long semente) {
        Random r = new Random(semente);
        StringBuilder sb = new StringBuilder(tamanho);
        for (int i = 0; i < tamanho; i++) {
            sb.append(alfabeto.charAt(r.nextInt(alfabeto.length())));
        }
        return sb.toString();
    }
}
