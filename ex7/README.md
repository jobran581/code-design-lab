# Exercise 7 — CompletableFuture imitation (MyFuture)

**Submitted file(s):** `MyFuture.java`, in package `test`.

## The problem
Imitate `CompletableFuture`. Steps are registered **in advance** with `thenDo(function)`, and each may change the type. `finallyDo(consumer)` ends the chain. `set(value)` starts the chain reaction.

## Design
* Each `MyFuture<V>` keeps a list of **actions to run when its value arrives**.
* **`thenDo(f)`** creates a new `MyFuture<R>` and registers the action "apply `f`, then `set` the result into the new future", and returns the new future. That is how `String → Integer → Boolean` chains work.
* **`finallyDo(c)`** registers `c` as the last action and returns nothing, so the chain ends.
* **`set(v)`** stores the value and runs the actions in order. Each action sets the next future, which runs *its* actions, and so on. It is a chain of observers.
* **Thread safety:** registering an action and `set` are synchronized. The actions themselves run outside the lock.

## Alternatives considered
| Option | Why not chosen |
|---|---|
| A single "next action" field | Only one chain per future, and `set` crashes when nothing is registered. |
| Run the actions in a thread pool | More like the real `CompletableFuture`, but the test expects the results right after `set` returns. |

## Requirements review (gaps and decisions)
* **`set` with nothing registered:** stores the value and does not crash.
* **An action registered *after* `set`:** runs at once with the stored value, which is how `CompletableFuture` behaves.
* **One future feeding several chains:** all of them run.

## How to run
* **Training test:** run `test/MainTrain1.java` (Eclipse: Run As → Java Application). It prints only `result1: 84, result2: 126, result3: 42, done`.
* **Edge cases:** compile `edge-tests/Edge7.java` together with the `test` package and run `Edge7`. It prints `OK` or `FAIL` for each case. Current result: **ALL EDGE CASES PASSED**.

  Command line, from this folder:

  ```
  javac -d out test/*.java edge-tests/Edge7.java
  java -cp out Edge7
  ```
