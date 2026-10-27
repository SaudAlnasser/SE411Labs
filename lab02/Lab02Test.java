import java.util.Arrays;
import java.util.Collections;

public class Lab02Test {
    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    public static void main(String[] args) {
        NumberBox<Integer> integers = new NumberBox<>();
        integers.setItem(7);
        check(integers.getItem().equals(7), "Integer storage");
        check(integers.add(2.5) == 9.5, "Mixed addition");
        integers.setItem(-3);
        check(integers.getItem().equals(-3), "Replacement");
        NumberBox<Double> doubles = new NumberBox<>();
        doubles.setItem(2.5);
        check(doubles.getItem().equals(2.5), "Double storage");
        check(doubles.add(1.5) == 4.0, "Double addition");
        check(Main.sumNumbers(Arrays.asList(1, 2, 3)) == 6, "Integer sum");
        check(Main.sumNumbers(Arrays.asList(1.5, 2.5)) == 4, "Double sum");
        check(NumberBox.sumNumbers(Arrays.<Number>asList(1, 2.5, 3L)) == 6.5, "Mixed sum");
        check(Main.sumNumbers(Collections.<Number>emptyList()) == 0, "Empty sum");

        PipeLine<String, String> identity = PipeLine.start();
        PipeLine<String, String> trimmed = identity.add(String::trim);
        PipeLine<String, Integer> length = trimmed.add(String::length);
        PipeLine<String, Integer> doubled = length.add(n -> n * 2);
        check(identity.execute(" hi ").equals(" hi "), "Identity preserved");
        check(trimmed.execute(" hi ").equals("hi"), "Earlier pipeline preserved");
        check(length.execute(" hi ") == 2, "Type change and step order");
        check(doubled.execute(" hi ") == 4, "Same-type step");
        check(doubled.execute(" java ") == 8, "Pipeline reuse");
        check(trimmed.add(String::toUpperCase).execute(" hi ").equals("HI"), "Branching");
        PipeLine<StringBuilder, StringBuilder> inPlace = PipeLine.<StringBuilder>start()
                .add(value -> value.append("!"));
        StringBuilder value = new StringBuilder("Java");
        check(inPlace.execute(value) == value && value.toString().equals("Java!"), "In-place transformation");
        System.out.println("All Lab 02 checks passed.");
    }
}
