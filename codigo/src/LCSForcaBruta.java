public class LCSForcaBruta {

    private char[] x;
    private char[] y;
    private long iteracoes;

    public int calcular(String s1, String s2) {
        x = s1.toCharArray();
        y = s2.toCharArray();
        iteracoes = 0;
        return lcs(x.length, y.length);
    }

    private int lcs(int i, int j) {
        iteracoes++;
        if (i == 0 || j == 0) {
            return 0;
        }
        if (x[i - 1] == y[j - 1]) {
            return lcs(i - 1, j - 1) + 1;
        }
        return Math.max(lcs(i - 1, j), lcs(i, j - 1));
    }

    public long iteracoes() {
        return iteracoes;
    }

    // Número exato de chamadas que calcular(s1, s2) faria, obtido em O(m*n) com a
    // mesma recorrência, sem executar a recursão. Cabe em long para m + n <= 65.
    public static long contarChamadas(String s1, String s2) {
        int m = s1.length();
        int n = s2.length();
        long[][] t = new long[m + 1][n + 1];
        for (int i = 0; i <= m; i++) {
            for (int j = 0; j <= n; j++) {
                if (i == 0 || j == 0) {
                    t[i][j] = 1;
                } else if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    t[i][j] = 1 + t[i - 1][j - 1];
                } else {
                    t[i][j] = 1 + t[i - 1][j] + t[i][j - 1];
                }
            }
        }
        return t[m][n];
    }
}
