import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String text = scanner.nextLine();

        Arrays.stream(text.split("\\W+"))
                .map(String::toLowerCase)
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        Collectors.counting())
                )
                .entrySet()
                .stream()
                .sorted(Map.Entry.<String, Long>comparingByValue()
                                .reversed()
                                .thenComparing(Map.Entry.comparingByKey()))
                .limit(10)
                .forEach(entry -> System.out.println(entry.getKey()));
    }
}