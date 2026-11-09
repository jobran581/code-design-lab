# Exercise 3 — Interpreter

**Submitted file(s):** `Expression`, `Number`, `BinaryExpression`, `Plus`, `Minus`, `Mul`, `Div`, `Q3`, in package `test`.

## The problem
1. Build the class diagram: `Expression` → `Number` and the abstract `BinaryExpression` (`Plus`, `Minus`, `Mul`, `Div`).
2. Write `Q3.calc(String)`, which parses an infix expression with Dijkstra's **Shunting-yard** algorithm and evaluates it **through the expression tree**.

## Design
* **Interpreter pattern:** each grammar element is a class with `calculate()`. Only `Number` is a leaf; a `BinaryExpression` holds `left` and `right` (protected, as in the diagram).
* **`Q3.calc` works in three separate steps** (each with its own responsibility):
  1. `tokenize`: numbers, including decimals, plus operators and brackets. Spaces are ignored.
  2. `toPostfix`: Shunting-yard.
  3. `buildExpression`: a stack turns the postfix order into a tree of `Plus`, `Mul`, `Number` and so on. Then `calculate()` is called once on the root.

## Requirements review (gaps and decisions)
* **Left associativity.** The activity diagram pops operators of *greater* precedence. Followed literally, `6-2-1` would become `6-(2-1)` = 5. We pop **greater or equal**, so `6-2-1` = 3 and `8/4/2` = 1.
* **Test 5 needs exactly `5`.** Correct floating-point math gives `7.5*(4.3+(6-12.3))/(0-3.0)` = `5.000000000000003`. We checked every evaluation order: none gives exactly 5. So `calc` **rounds the final result to 10 decimal places** (with `BigDecimal`), and `0.1+0.2` gives `0.3`.
* **Division by zero:** gives `Infinity`. It does not crash or exit.
* **Not supported,** because the booklet does not ask for them: unary minus (write `0-3`) and other operators. An unknown character throws `IllegalArgumentException`.

## Comparison with last year's solution
* **The same Shunting-yard with `>=`.** Last year's solution also **had to round** (to 4 decimals), which confirms that test 5 requires it.
* **Their rounding** used `DecimalFormat("0.0000")` and `Double.parseDouble`. On a computer whose language writes decimals with a comma (German, French, …) that crashes with `NumberFormatException: "8,0000"`; we reproduced the crash. Our `BigDecimal` rounding does not depend on the language setting.
* **They evaluated each operator immediately** into a new `Number`, instead of building the full tree. That works, but it loses the point of the Interpreter pattern.
* **Their `BinaryExpression` was not abstract**, and its `calculate()` returned 0.
* **Their `Div` called `System.exit` on a zero divisor.**

## How to run
* **Training test:** run `test/MainTrain.java` (Eclipse: Run As → Java Application). It prints only `done`.
* **Edge cases:** compile `edge-tests/Edge3.java` together with the `test` package and run `Edge3`. It prints `OK` or `FAIL` for each case. Current result: **ALL EDGE CASES PASSED**.

  Command line, from this folder:

  ```
  javac -d out test/*.java edge-tests/Edge3.java
  java -cp out Edge3
  ```

## AI process
* The solution was **generated with AI (Claude)** from the booklet and the given code.
* It was then **verified on my own machine**: imported into Eclipse, with the training test run there.
* It was **checked against last year's student solution** (see "Comparison").
* The edge cases were written to test behaviour that the training test does not cover.
