# Exercise 6 — Fork-join pool

**Submitted file(s):** `ParMaxSearcher.java`, in package `test`.

## The problem
Find the maximum value in a full binary tree recursively, using a **fork-join pool**. At each step, the **left subtree is searched by another thread** of the pool. The test checks four things: the search finishes within 1 second, the max is correct, the pool was really used (`getStealCount() > 1`), and `BinTree` is unchanged.

## Design
`ParMaxSearcher extends RecursiveTask<Integer>`. In `compute()`:
1. `fork()` a task for the **left** subtree. It goes to the pool, where an idle thread can *steal* it.
2. Search the **right** subtree in the **current** thread, by calling `compute()` directly.
3. `join()` the left result, and return the max of the node, the left result and the right result.

A leaf (no children) returns its own value.

## Alternatives considered
| Option | Why not chosen |
|---|---|
| Fork both subtrees and join both | The current thread would sit idle waiting, which is a known fork-join anti-pattern. |
| Search small subtrees sequentially below a size limit | Faster for huge trees, but it reduces stealing, and the booklet asks for a fork at *every* step. |
| A normal recursive search | No parallelism. The test checks that the pool was used. |

## Requirements review (gaps and decisions)
* A node with **only one child** cannot appear in a full tree. The code still handles it, because each side is checked for `null` separately.
* **Tested at every depth from 0 to 14**, from 1 node to 32,767 nodes, against a plain recursive maximum.

## How to run
* **Training test:** run `test/MainTrain.java` (Eclipse: Run As → Java Application). It prints only `done`.
* **Edge cases:** compile `edge-tests/Edge6.java` together with the `test` package and run `Edge6`. It prints `OK` or `FAIL` for each case. Current result: **ALL EDGE CASES PASSED**.

  Command line, from this folder:

  ```
  javac -d out test/*.java edge-tests/Edge6.java
  java -cp out Edge6
  ```
