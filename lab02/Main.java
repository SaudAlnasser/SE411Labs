import java.util.Arrays;
import java.util.List;

public class Main {
    public static void printList(List<?> items) {
        for (Object item : items) {
            System.out.println(item);
        }
    }

    public static double sumNumbers(List<? extends Number> numbers) {
        return NumberBox.sumNumbers(numbers);
    }

    public static void main(String[] args) {
        System.out.println("Exercise 1: PrintableList<String>");
        new PrintableList<>(new String[]{"Java", "Generics", "SE411"}).printItems();

        System.out.println("Exercise 2: NumberBox");
        NumberBox<Integer> integers = new NumberBox<>();
        integers.setItem(10);
        NumberBox<Double> doubles = new NumberBox<>();
        doubles.setItem(2.5);
        System.out.println("Integer item: " + integers.getItem());
        System.out.println("Double item: " + doubles.getItem());
        System.out.println("Integer + 5: " + integers.add(5));
        System.out.println("Double + 1.5: " + doubles.add(1.5));
        System.out.println("Mixed number sum: " + NumberBox.sumNumbers(Arrays.<Number>asList(1, 2.5, 3L)));

        System.out.println("Exercise 3: Reusable pipeline");
        PipeLine<String, String> cleaned = PipeLine.<String>start()
                .add(String::trim)
                .add(String::toUpperCase);
        PipeLine<String, Integer> lengths = cleaned.add(String::length);
        PipeLine<String, String> descriptions = lengths.add(length -> "Length: " + length);
        System.out.println(cleaned.execute("  generics  "));
        System.out.println(descriptions.execute("  generics  "));
        System.out.println(descriptions.execute("  java  "));

        System.out.println("Exercise 4: Wildcards");
        printList(Arrays.asList("Hello", "Wildcards"));
        printList(Arrays.asList(1, 2, 3));
        System.out.println("Integer sum: " + sumNumbers(Arrays.asList(1, 2, 3)));
        System.out.println("Double sum: " + sumNumbers(Arrays.asList(1.5, 2.5)));
    }
}
