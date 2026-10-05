import java.util.function.IntSupplier;

public final class Medidor {

    private static final long AQUECIMENTO_NS = 100_000_000L;
    private static final long MEDICAO_NS = 200_000_000L;
    private static final long EXECUCAO_UNICA_NS = 500_000_000L;

    // Impede que a JVM descarte as execuções repetidas.
    public static volatile long sumidouro;

    public static final class Medicao {
        public final int lcs;
        public final double tempoNs;

        Medicao(int lcs, double tempoNs) {
            this.lcs = lcs;
            this.tempoNs = tempoNs;
        }
    }

    private Medidor() {
    }

    public static Medicao medir(IntSupplier execucao) {
        long inicio = System.nanoTime();
        int lcs = execucao.getAsInt();
        long primeira = System.nanoTime() - inicio;
        if (primeira >= EXECUCAO_UNICA_NS) {
            return new Medicao(lcs, primeira);
        }

        long acumulador = 0;
        long t0 = System.nanoTime();
        do {
            acumulador += execucao.getAsInt();
        } while (System.nanoTime() - t0 < AQUECIMENTO_NS);

        long execucoes = 0;
        long s = System.nanoTime();
        do {
            acumulador += execucao.getAsInt();
            execucoes++;
        } while (System.nanoTime() - s < MEDICAO_NS || execucoes < 3);
        long total = System.nanoTime() - s;

        sumidouro = acumulador;
        return new Medicao(lcs, total / (double) execucoes);
    }
}
