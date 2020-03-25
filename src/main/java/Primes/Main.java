package Primes;

public class Main {

    public static void main(String[] args) {
        System.out.println("\n\tSift the Two's and Sift the Three's,\n\t\t" +
                "The Sieve of Eratosthenes.\n\t\t" +
                "When the multiples sublime,\n\t\t" +
                "The numbers that remain are Prime.\n");
        singleThread();
        actors();
    }

    private static void singleThread() {
        long time = System.currentTimeMillis();
        SingleThreadSieve.run(1_000);
        System.out.println("Ran with single thread, took " + (System.currentTimeMillis() - time) + "ms");
    }

    private static void actors() {
        long time = System.currentTimeMillis();
        PrimeActor.run(1_000);
        System.out.println("Ran with actors, took " + (System.currentTimeMillis() - time) + "ms");
    }
}
