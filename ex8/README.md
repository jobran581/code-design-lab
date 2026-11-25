# Exercise 8 — Stream imitation (Pipe)

**Submitted file(s):** `Pipe.java`, in package `test`.

## The problem
Build `Pipe<E>`, a production line in the style of the Pipes & Filters pattern. Each pipe has a **thread-safe queue of up to 100 items** and **its own background thread** (like an Active Object). The operations:
* **`filter`** passes the items that match a condition to the next pipe.
* **`map`** converts each item, possibly to another type, and passes it on.
* **`forEach`** consumes the items. It is terminal: nothing chains after it.
* **`add`** waits while the queue is full.
* **`stop`** stops this pipe and every pipe after it.

The test checks that exactly 3 threads open for 3 pipes, that the result is right, and that `stop` closes all 3 threads.

## Design
* **Each pipe starts its thread in the constructor.** The thread waits until an operation is defined (a `CountDownLatch`), then loops on `queue.take()`. It sleeps while the queue is empty and runs the operation on each item.
* **`filter` and `map` create the next pipe** and register an operation that adds into it, so they can be chained. `map` is generic: `Pipe<R>`.
* **`stop()`** sets a `volatile` flag, interrupts the thread (which wakes it even inside `take()` or a full `put()`), and calls `stop()` on the next pipes.
* The threads are **daemon** threads.

## Alternatives considered
| Option | Why not chosen |
|---|---|
| Start the thread only when the operation is defined | Also gives 3 threads in the test. Starting in the constructor matches "one thread per Pipe defined" more literally. |
| One shared thread pool for all pipes | That breaks the "one thread per pipe" requirement, and the thread-count test. |

## Requirements review (gaps and decisions)
* **Items added before the next operation is defined:** they wait in the queue. They are not dropped.
* **More than 100 items:** `add` waits for space. It is tested with 1,000 items through 5 pipes.
* **`add` after `stop`:** ignored.
* The booklet says the grading test uses **other numbers of pipes, orders, types and amounts**. The edge test uses 5 pipes and 3 type changes.

## How to run
* **Training test:** run `test/MainTrain1.java` (Eclipse: Run As → Java Application). It prints only `done`.
* **Edge cases:** compile `edge-tests/Edge8.java` together with the `test` package and run `Edge8`. It prints `OK` or `FAIL` for each case. Current result: **ALL EDGE CASES PASSED**.

  Command line, from this folder:

  ```
  javac -d out test/*.java edge-tests/Edge8.java
  java -cp out Edge8
  ```
