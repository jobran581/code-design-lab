# Exercise 5 — Active Object, imitation of Future

**Submitted file(s):** `Q1a.java`, `Q1b.java`, in package `test`.

## The problem
* **a.** `threadIt(f)` runs a function in another thread and returns its result as a `Future<V>`.
* **b.** Build an **Active Object**: `push(Runnable)` adds a task, one background thread runs the tasks one after another, and `close()` exits in order after **every task pushed before close** has run. No `ExecutorService` or ready-made thread pool is allowed.

## Design
* **a.** `public <V> Future<V> threadIt(Callable<V> f) { return es.submit(f); }`. It is generic, so it works for any type. The booklet encourages using the existing libraries here.
* **b.**
  * A `BlockingQueue<Runnable>` and **one thread**, started in the constructor, that loops on `take()` and runs each task.
  * **Close with a "poison pill":** `close()` puts a special STOP task at the end of the queue. The queue is first-in-first-out, so every earlier task runs first; then the thread sees STOP and ends.
  * After `close()`, `push()` ignores new tasks. `push` and `close` are `synchronized`, so a task cannot slip in after the pill.
  * **A task that throws does not kill the thread.**

## Alternatives considered
| Option | Why not chosen |
|---|---|
| A `volatile` stop flag, plus `poll` with a timeout | The thread wakes up again and again for nothing, and tasks still in the queue can be lost when the flag is checked first. |
| `close()` interrupts the thread | That would cancel tasks that were already pushed, which is against the requirement. |
| `close()` waits (`join`) until the thread ends | It would deadlock if called from inside a task. The test only needs the thread gone shortly after. |

## Requirements review (gaps and decisions)
* **`close()` twice:** the second call is ignored.
* **`push(null)`:** ignored.
* **`push` after close:** ignored.
* **Queue capacity:** 10,000 tasks. Beyond that, `push` waits for space.
* **Does `close()` return immediately?** Yes. The remaining tasks still run, then the thread ends.

## How to run
* **Training test:** run `test/MainTrain1a.java / MainTrain1b.java` (Eclipse: Run As → Java Application). It prints only `done`.
* **Edge cases:** compile `edge-tests/Edge5.java` together with the `test` package and run `Edge5`. It prints `OK` or `FAIL` for each case. Current result: **ALL EDGE CASES PASSED**.

  Command line, from this folder:

  ```
  javac -d out test/*.java edge-tests/Edge5.java
  java -cp out Edge5
  ```
