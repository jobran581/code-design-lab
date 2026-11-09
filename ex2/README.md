# Exercise 2 — Builder (MyStringBuilder)

**Submitted file(s):** `MyStringBuilder.java`, in package `test`.

## The problem
Imitate `StringBuilder` on top of an `ArrayList<Character>`, with `append`, `reverse`, `apply` and `toString`, all in a **fluent** style. Using `StringBuilder` or `StringBuffer` is forbidden.

## Design
* **Fluent Builder:** `append`, `reverse` and `apply` change the buffer and `return this`, so calls chain in any order. `toString()` builds the final product.
* **`reverse`:** swaps characters from both ends towards the middle (in place, O(n)).
* **`apply(UnaryOperator<Character>)`:** replaces every character with `f(c)`. This uses the `UnaryOperator` import that came with the file.
* **`toString`:** copies the characters into a `char[]` and calls `new String(chars)`. This is O(n), and no `StringBuilder` is used.

## Alternatives considered
| Option | Why not chosen |
|---|---|
| `String s = ""; s += c` in a loop | O(n²), because each `+=` copies the whole string. |
| `Collections.reverse(buff)` | Allowed and fine. The manual swap just shows the idea; both are O(n). |
| `Function<Character, Character>` for `apply` | Works too, but `UnaryOperator` (same input and output type) states the intent and was already imported. |

## Requirements review (gaps and decisions)
* **`append(null)`:** ignored. The real `StringBuilder` appends "null"; we chose to ignore it.
* **Empty builder, empty append, reverse of an empty or one-letter buffer:** all work.
* **A function given to `apply` must not return `null`:** a `null` character would break `toString`. This is documented as a requirement on the caller.
* The booklet says the grading tests pass **other functions** to `apply`. The edge tests mix two different functions with `append`.

## Comparison with last year's solution
* Same Builder structure: `return this`, and `Collections.reverse`.
* Their `toString` used `String +=` in a loop, which is O(n²). Ours is O(n).

## How to run
* **Training test:** run `test/MainTrain.java` (Eclipse: Run As → Java Application). It prints only `FEDCBA and done`.
* **Edge cases:** compile `edge-tests/Edge2.java` together with the `test` package and run `Edge2`. It prints `OK` or `FAIL` for each case. Current result: **ALL EDGE CASES PASSED**.

  Command line, from this folder:

  ```
  javac -d out test/*.java edge-tests/Edge2.java
  java -cp out Edge2
  ```

## AI process
* The solution was **generated with AI (Claude)** from the booklet and the given code.
* It was then **verified on my own machine**: imported into Eclipse, with the training test run there.
* It was **checked against last year's student solution** (see "Comparison").
* The edge cases were written to test behaviour that the training test does not cover.
