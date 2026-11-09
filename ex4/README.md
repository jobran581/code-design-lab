# Exercise 4 — Observer + Future

**Submitted file(s):** `ObservableFuture.java`, in package `test`.

## The problem
`Future.get()` blocks until the value arrives. `ObservableFuture` must wrap a `Future`, return immediately, wait for the value **in the background**, and **notify its observers** when the value arrives. Its `get()` must never wait: it returns `null` until the value is ready.

## Design
* `ObservableFuture<V> extends java.util.Observable`. That is the Observable the test uses, through `addObserver((o, arg) -> …)`.
* The constructor starts one **background thread** that calls `future.get()`. When the value arrives, the thread stores it, then calls `setChanged()` and `notifyObservers(value)`.
* The value field is **`volatile`**, so observers on other threads see it at once.
* The thread is a **daemon** thread, so a future that never completes cannot keep the program alive.

## Alternatives considered
| Option | Why not chosen |
|---|---|
| `CompletableFuture` callbacks | Neater, but the exercise is about building the Observer mechanism, and the test needs `Observable`. |
| A shared thread pool for the waiting | Better when there are thousands of futures. One thread per future is simpler and enough here. |

## Requirements review (gaps and decisions)
* **The task fails or is cancelled:** no value and no notification, and `get()` stays `null`. Nothing crashes.
* **An observer added after the value arrived:** it is not notified. This is how `Observable` works, and the booklet assumes observers subscribe first.
* `Observable` has been deprecated since Java 9. It is kept because the exercise and its test are built on it, and the warning is suppressed with a comment.

## Comparison with last year's solution
Last year's file was **left as the empty starter file** (not solved), so there was nothing to compare.

## How to run
* **Training test:** run `test/MainTrain1.java` (Eclipse: Run As → Java Application). It prints only `done`.
* **Edge cases:** compile `edge-tests/Edge4.java` together with the `test` package and run `Edge4`. It prints `OK` or `FAIL` for each case. Current result: **ALL EDGE CASES PASSED**.

  Command line, from this folder:

  ```
  javac -d out test/*.java edge-tests/Edge4.java
  java -cp out Edge4
  ```

## AI process
* The solution was **generated with AI (Claude)** from the booklet and the given code.
* It was then **verified on my own machine**: imported into Eclipse, with the training test run there.
* It was **checked against last year's student solution** (see "Comparison").
* The edge cases were written to test behaviour that the training test does not cover.
