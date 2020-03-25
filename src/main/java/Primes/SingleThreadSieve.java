package Primes;

public class SingleThreadSieve {

    /**
     * The main single threaded method to generate primes up to a certain value.
     *
     * @param num The value to generate primes up to.
     */
    public static void run(int num) {
        int[] numbers = new int[num - 2];
        for (int i = 0; i < numbers.length; i++)
            numbers[i] = i + 2;
        for (int i = 0; i < numbers.length; i++) {
            int prime = numbers[i];
            if (prime == -1)
                continue;
            System.out.print(prime + " ");
            for (int j = i + 1; j < numbers.length; j++) {
                if (numbers[j] % prime == 0)
                    numbers[j] = -1;
            }
        }
        System.out.println();
    }
}
