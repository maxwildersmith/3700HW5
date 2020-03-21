package Primes;

public class Main {
    public static void main(String[] args) {
        System.out.println("Sift the Two's and Sift the Three's,\n" +
                "The Sieve of Eratosthenes.\n" +
                "When the multiples sublime,\n" +
                "The numbers that remain are Prime.\n");
        long time = System.currentTimeMillis();
        SingleThreadSieve.run(100);
        System.out.println("Ran with single thread, took "+(System.currentTimeMillis()-time)+"ms");
    }
}
