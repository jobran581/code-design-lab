package test;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class Pipe<E> implements Stoppable {

	// thread safe queue of up to 100 elements
	private final BlockingQueue<E> queue = new ArrayBlockingQueue<>(100);

	// the predefined operations of this pipe (normally exactly one: filter, map or forEach)
	private final List<Consumer<E>> operations = new CopyOnWriteArrayList<>();
	private final CountDownLatch hasOperation = new CountDownLatch(1);

	// the pipes this pipe passes data to (stopped together with it)
	private final List<Stoppable> nextPipes = new CopyOnWriteArrayList<>();

	private final Thread worker;
	private volatile boolean stopped = false;

	public Pipe() {
		// like an Active Object: one background thread per pipe, sleeping until there is data
		worker = new Thread(() -> {
			try {
				hasOperation.await(); // keep queued data until an operation is defined
				while (!stopped) {
					E e = queue.take(); // waits while the queue is empty
					for (Consumer<E> op : operations)
						op.accept(e);
				}
			} catch (InterruptedException ex) {
				// stop() interrupts the thread: just end
			}
		});
		worker.setDaemon(true);
		worker.start();
	}

	// inserts e to the queue, or waits as long as the queue is full
	public void add(E e) {
		if (stopped)
			return;
		try {
			queue.put(e);
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
	}

	// passes every E for which the condition is true to the next pipe
	public Pipe<E> filter(Predicate<E> condition) {
		Pipe<E> next = new Pipe<>();
		connect(next, e -> {
			if (condition.test(e))
				next.add(e);
		});
		return next;
	}

	// converts every E to an R and passes it to the next pipe
	public <R> Pipe<R> map(Function<E, R> f) {
		Pipe<R> next = new Pipe<>();
		connect(next, e -> next.add(f.apply(e)));
		return next;
	}

	// terminal operation: consumes every E (nothing can be chained after it)
	public void forEach(Consumer<E> consumer) {
		connect(null, consumer);
	}

	private void connect(Stoppable next, Consumer<E> operation) {
		if (next != null)
			nextPipes.add(next);
		operations.add(operation);
		hasOperation.countDown();
	}

	// immediately stops this pipe and the pipes after it
	@Override
	public void stop() {
		if (stopped)
			return;
		stopped = true;
		worker.interrupt();
		for (Stoppable next : nextPipes)
			next.stop();
	}
}
