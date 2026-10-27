# SE411 - Lab 02: Introduction to Java Generics

Fall 2026-2027. Requires JDK 8 or newer; no external dependencies.

From this folder:

```sh
javac -Xlint:all -Werror -d out *.java
java -cp out Main
java -cp out Lab02Test
```

- Exercise 1: `PrintableList<T>` copies an array into an internal `List<T>` and prints its items. `Main` demonstrates strings.
- Exercise 2: `NumberBox<T extends Number>` stores and retrieves integers or doubles, adds a number to the stored value, and sums numeric lists. Arithmetic returns `double`, so mixed number types are supported (subject to normal floating-point precision).
- Exercise 3: `Transformer<T, R>` and `PipeLine<T, R>` provide typed, ordered composition. `start()` creates an identity pipeline; `add()` returns a new pipeline with the same initial input type and a new output type. Same-type steps and type-changing steps can be combined without raw types or casts. `Main` demonstrates trimming, uppercasing, length calculation, branching, and reuse. Following the handout's explicit two-parameter class signature resolves its later inconsistent reference to `Pipeline<T>`. Same-type transformations may also mutate an input object if the supplied transformer does so; the pipeline structure itself is immutable.
- Exercise 4: `Main.printList(List<?>)` and `Main.sumNumbers(List<? extends Number>)` demonstrate wildcards.

`Lab02Test` checks wrapper arithmetic, wildcard sums, empty lists, transformation order, identity, reuse, and preservation of earlier pipelines.
