import java.util.Scanner;
import java.util.stream.*;

class PrimeNumbers {

    /**
     * Checking if a number is prime
     *
     * @param number to test >= 2
     * @return true if number is prime else false
     */
    private static boolean isPrime(long number) {
        return LongStream.rangeClosed(2L, number - 1) // Create a LongStream with a range from 2 to the number minus one
                .filter(n -> number % n == 0) // Filter the stream for items that are divisors of the number
                .noneMatch(n -> true); // If the stream doesn't contain anything at this point, it's a prime number
    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        String line = scanner.nextLine().trim();

        int n = Integer.parseInt(line);

        System.out.println(isPrime(n) ? "True" : "False");
    }
}