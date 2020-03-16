package Primes;

public class Main {
    public static void main(String[] args) {
        System.out.println("Sift the Two's and Sift the Three's,\n" +
                "The Sieve of Eratosthenes.\n" +
                "When the multiples sublime,\n" +
                "The numbers that remain are Prime.\n\n");
        SingleThreadSieve.run(100);
    }
}
