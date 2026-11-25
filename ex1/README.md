# Exercise 1 — Composite + Iterator

**Submitted file(s):** `PlayListIterator.java`, in package `test`.

## The problem
`Item`, `Song` and `PlayList` form a **Composite**: a play list holds songs and other play lists, nested to any depth. We need an `Iterator<String>` that returns the **song names only**, in **DFS** order. The given classes must not be changed.

## Design
* `PlayListIterator implements java.util.Iterator<String>`, as the booklet requires.
* It is **lazy**: it keeps an explicit **stack of frames** (the items of one play list and the next index to read). `advance()` moves to the next song, pushing a nested play list and popping a finished one.
* `hasNext()` only *finds* the next song, so calling it many times does not skip anything. `next()` returns it.

## Alternatives considered
| Option | Why not chosen |
|---|---|
| **Eager:** copy every name into a list in the constructor | It does all the work up front, uses memory for the whole tree, and ignores later changes. Simpler, but it is not really an iterator over the structure. |
| **Recursive:** an iterator of child iterators | Elegant, but each nesting level adds Java call-stack depth, so very deep lists can overflow. Our explicit stack handled 10,000 levels. |
| Add an `iterator()` to `Item` (polymorphism instead of `instanceof`) | The cleanest Composite, but it requires changing `Item`, `Song` and `PlayList`, which the exercise forbids. |

## Requirements review (gaps and decisions)
* **Empty play lists, including nested ones:** they produce nothing and are skipped.
* **`next()` after the end:** throws `NoSuchElementException`, as the `Iterator` contract says.
* **`null` items, or items that are neither Song nor PlayList:** skipped.
* **Cycles:** not handled. The booklet says to assume there are none.
* **`remove()`:** not supported (the default throws).

## How to run
* **Training test:** run `test/MainTrain.java` (Eclipse: Run As → Java Application). It prints only `done`.
* **Edge cases:** compile `edge-tests/Edge1.java` together with the `test` package and run `Edge1`. It prints `OK` or `FAIL` for each case. Current result: **ALL EDGE CASES PASSED**.

  Command line, from this folder:

  ```
  javac -d out test/*.java edge-tests/Edge1.java
  java -cp out Edge1
  ```
