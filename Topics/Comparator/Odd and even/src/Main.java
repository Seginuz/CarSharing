import java.util.List;

class Utils {

    public static List<Integer> sortOddEven(List<Integer> numbers) {
        numbers.sort((n1, n2) ->
                n1 % 2 == 0
                        ? n2 % 2 == 0 ? -Integer.compare(n1, n2) : 1
                        : n2 % 2 != 0 ? Integer.compare(n1, n2) : -1
        );
        return numbers;
    }
}