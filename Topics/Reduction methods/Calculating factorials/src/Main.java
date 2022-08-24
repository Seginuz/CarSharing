import java.util.OptionalLong;
import java.util.Scanner;
import java.util.stream.LongStream;

class Main {

    /**
     * Calculates the factorial of the given number n
     *
     * @param n >= 0
     *
     * @return factorial value
     */
    public static long factorial(long n) {
        OptionalLong f = LongStream.rangeClosed(1, n).reduce((product, item) -> product * item);
        return f.isPresent() ? f.getAsLong() : 1;
    }

    // Don't change the code below
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        long n = Integer.parseInt(scanner.nextLine().trim());

        System.out.println(factorial(n));
    }
}