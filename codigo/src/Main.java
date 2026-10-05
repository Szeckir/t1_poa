import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class Main {

    // A força bruta só roda se m + n não passar desse limite
    // e se não for fazer mais do que MAX_CHAMADAS_FB chamadas (alguns minutos).
    static final int LIMITE_FB_PADRAO = 32;
    static final int LIMITE_FB_COMPLETO = 60;
    static final long MAX_CHAMADAS_FB = 100_000_000_000L;

    static final String FORMATO = " %-26s %6s %18s  %-20s %12s%n";
    static final Locale BR = Locale.forLanguageTag("pt-BR");

    static final class Linha {
        int m, n, lcs;
        boolean fbExecutada;
        long fbIteracoes, pdIteracoes;
        double fbTempoNs, pdTempoNs;
    }

    public static void main(String[] args) {
        boolean completo = false;
        List<String> resto = new ArrayList<>();
        for (String a : args) {
            if (a.equals("--completo")) {
                completo = true;
            } else {
                resto.add(a);
            }
        }
        int limite = completo ? LIMITE_FB_COMPLETO : LIMITE_FB_PADRAO;

        try {
            if (resto.isEmpty()) {
                menu(limite);
            } else if (resto.get(0).equals("--testes")) {
                BateriaTestes.executar(limite);
            } else if (resto.get(0).equals("--arquivo") && resto.size() == 2) {
                String[] par = lerArquivo(resto.get(1));
                analisar("LCS - arquivo " + resto.get(1), par[0], par[1], limite);
            } else if (resto.size() == 2) {
                analisar("LCS entre S1 e S2", resto.get(0), resto.get(1), limite);
            } else {
                System.out.println("Uso: Main [S1 S2 | --arquivo caminho | --testes] [--completo]");
            }
        } catch (IOException e) {
            System.out.println("Erro: " + e.getMessage());
            System.exit(1);
        }
    }

    static Linha analisar(String titulo, String s1, String s2, int limiteFB) {
        Linha r = new Linha();
        r.m = s1.length();
        r.n = s2.length();

        System.out.println(repetir('=', 78));
        System.out.println(" " + titulo);
        System.out.println(repetir('=', 78));
        System.out.println(" S1 = " + resumir(s1) + "  (m = " + r.m + ")");
        System.out.println(" S2 = " + resumir(s2) + "  (n = " + r.n + ")");
        System.out.println();

        LCSProgramacaoDinamica pd = new LCSProgramacaoDinamica();
        Medidor.Medicao mpd = Medidor.medir(() -> pd.calcular(s1, s2));
        r.lcs = mpd.lcs;
        r.pdIteracoes = pd.iteracoes();
        r.pdTempoNs = mpd.tempoNs;

        LCSForcaBruta fb = new LCSForcaBruta();
        Medidor.Medicao mfb = null;
        String motivo = null;
        if (r.m + r.n > limiteFB) {
            motivo = "(inviável: m+n > " + limiteFB + ")";
        } else if (LCSForcaBruta.contarChamadas(s1, s2) > MAX_CHAMADAS_FB) {
            motivo = "(> 10^11 chamadas)";
        } else {
            mfb = Medidor.medir(() -> fb.calcular(s1, s2));
            r.fbExecutada = true;
            r.fbIteracoes = fb.iteracoes();
            r.fbTempoNs = mfb.tempoNs;
        }

        System.out.printf(FORMATO, "Algoritmo", "LCS", "Iterações", "(unidade)", "Tempo médio");
        System.out.printf(FORMATO, repetir('-', 26), repetir('-', 6), repetir('-', 18),
                repetir('-', 20), repetir('-', 12));
        if (mfb != null) {
            System.out.printf(FORMATO, "Força bruta (recursivo)", inteiro(mfb.lcs),
                    inteiro(r.fbIteracoes), "chamadas recursivas", tempo(r.fbTempoNs));
        } else {
            System.out.printf(FORMATO, "Força bruta (recursivo)", "-", "não executada", motivo, "-");
        }
        System.out.printf(FORMATO, "Programação Dinâmica", inteiro(mpd.lcs),
                inteiro(r.pdIteracoes), "células calculadas", tempo(r.pdTempoNs));
        System.out.println();

        if (mfb != null) {
            System.out.println(" Verificação: força bruta e PD encontraram o mesmo valor? "
                    + (mfb.lcs == mpd.lcs ? "sim [OK]" : "NÃO [ERRO]"));
        }
        System.out.println(" Uma LCS reconstruída a partir da tabela: \"" + resumir(pd.reconstruir()) + "\"");
        if (r.m > 0 && r.n > 0 && r.m <= 12 && r.n <= 12) {
            System.out.println();
            System.out.println(" Tabela C[i][j] da Programação Dinâmica:");
            pd.imprimirTabela();
        }
        System.out.println();
        return r;
    }

    private static void menu(int limite) {
        Scanner in = new Scanner(System.in, StandardCharsets.UTF_8.name());
        while (true) {
            System.out.println("==== LCS - Maior Subsequência Comum ====");
            System.out.println(" 1) Digitar duas strings");
            System.out.println(" 2) Ler strings de um arquivo");
            System.out.println(" 3) Executar bateria de testes");
            System.out.println(" 0) Sair");
            System.out.print("Opção: ");
            if (!in.hasNextLine()) {
                return;
            }
            String op = in.nextLine().trim();
            try {
                switch (op) {
                    case "1":
                        System.out.print("S1: ");
                        String s1 = in.nextLine();
                        System.out.print("S2: ");
                        String s2 = in.nextLine();
                        analisar("LCS entre S1 e S2", s1, s2, limite);
                        break;
                    case "2":
                        System.out.print("Caminho do arquivo: ");
                        String caminho = tirarAspas(in.nextLine().trim());
                        String[] par = lerArquivo(caminho);
                        analisar("LCS - arquivo " + caminho, par[0], par[1], limite);
                        break;
                    case "3":
                        BateriaTestes.executar(limite);
                        break;
                    case "0":
                        return;
                    default:
                        System.out.println("Opção inválida.");
                }
            } catch (IOException e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }
    }

    static String[] lerArquivo(String caminho) throws IOException {
        Path arquivo;
        try {
            arquivo = Paths.get(caminho);
        } catch (InvalidPathException e) {
            throw new IOException("caminho inválido: " + caminho);
        }
        if (!Files.isRegularFile(arquivo)) {
            throw new IOException("arquivo não encontrado: " + arquivo.toAbsolutePath());
        }
        List<String> linhas = new ArrayList<>();
        for (String l : decodificar(Files.readAllBytes(arquivo)).split("\r\n|\r|\n")) {
            if (!l.startsWith("#")) {
                linhas.add(l);
            }
        }
        String s1 = linhas.size() > 0 ? linhas.get(0) : "";
        String s2 = linhas.size() > 1 ? linhas.get(1) : "";
        return new String[] {s1, s2};
    }

    // Aceita UTF-8 (com ou sem BOM), UTF-16 com BOM (gerado por "echo ... > arquivo"
    // no PowerShell do Windows) e ANSI/Latin-1 (Bloco de Notas antigo).
    static String decodificar(byte[] b) {
        if (b.length >= 2 && ((b[0] == (byte) 0xFF && b[1] == (byte) 0xFE)
                || (b[0] == (byte) 0xFE && b[1] == (byte) 0xFF))) {
            return new String(b, StandardCharsets.UTF_16);
        }
        try {
            String s = StandardCharsets.UTF_8.newDecoder().decode(ByteBuffer.wrap(b)).toString();
            return !s.isEmpty() && s.charAt(0) == 0xFEFF ? s.substring(1) : s; // tira o BOM
        } catch (CharacterCodingException e) {
            return new String(b, StandardCharsets.ISO_8859_1);
        }
    }

    // O "Copiar como caminho" do Windows cola o caminho entre aspas.
    static String tirarAspas(String s) {
        if (s.length() >= 2 && (s.charAt(0) == '"' || s.charAt(0) == '\'')
                && s.charAt(s.length() - 1) == s.charAt(0)) {
            return s.substring(1, s.length() - 1);
        }
        return s;
    }

    static String inteiro(long v) {
        return String.format(BR, "%,d", v);
    }

    static String tempo(double ns) {
        if (ns < 1e3) {
            return String.format(BR, "%.1f ns", ns);
        } else if (ns < 1e6) {
            return String.format(BR, "%.2f µs", ns / 1e3);
        } else if (ns < 1e9) {
            return String.format(BR, "%.2f ms", ns / 1e6);
        }
        return String.format(BR, "%.2f s", ns / 1e9);
    }

    static String resumir(String s) {
        return s.length() <= 40 ? s : s.substring(0, 20) + "..." + s.substring(s.length() - 10)
                + " (" + inteiro(s.length()) + " caracteres)";
    }

    static String repetir(char c, int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append(c);
        }
        return sb.toString();
    }
}
