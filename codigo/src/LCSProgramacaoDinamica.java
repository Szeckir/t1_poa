public class LCSProgramacaoDinamica {

    private char[] x;
    private char[] y;
    private int[][] c;
    private long iteracoes;

    public int calcular(String s1, String s2) {
        x = s1.toCharArray();
        y = s2.toCharArray();
        int m = x.length;
        int n = y.length;

        c = new int[m + 1][n + 1];
        iteracoes = 0;

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                iteracoes++;
                if (x[i - 1] == y[j - 1]) {
                    c[i][j] = c[i - 1][j - 1] + 1;
                } else {
                    c[i][j] = Math.max(c[i - 1][j], c[i][j - 1]);
                }
            }
        }
        return c[m][n];
    }

    public long iteracoes() {
        return iteracoes;
    }

    public String reconstruir() {
        StringBuilder sb = new StringBuilder();
        int i = x.length;
        int j = y.length;
        while (i > 0 && j > 0) {
            if (x[i - 1] == y[j - 1]) {
                sb.append(x[i - 1]);
                i--;
                j--;
            } else if (c[i - 1][j] >= c[i][j - 1]) {
                i--;
            } else {
                j--;
            }
        }
        return sb.reverse().toString();
    }

    public void imprimirTabela() {
        StringBuilder sb = new StringBuilder("        -");
        for (char cy : y) {
            sb.append(String.format("%4s", cy));
        }
        System.out.println(sb);
        for (int i = 0; i <= x.length; i++) {
            sb = new StringBuilder(String.format("    %s", i == 0 ? "-" : String.valueOf(x[i - 1])));
            for (int j = 0; j <= y.length; j++) {
                sb.append(String.format("%4d", c[i][j]));
            }
            System.out.println(sb);
        }
    }
}
