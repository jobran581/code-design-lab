package test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Q1b {

	// special task that tells the background thread to stop (it is queued after all real tasks)
	private static final Runnable STOP = () -> {};

	private final BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(10000);
	private final Thread worker;
	private boolean closed = false;

	public Q1b() {
		worker = new Thread(() -> {
			try {
				while (true) {
					Runnable r = queue.take(); // waits until there is a task
					if (r == STOP)
						break;
					try {
						r.run();
					} catch (RuntimeException e) {
						// a failing task must not kill the Active Object
					}
				}
			} catch (InterruptedException e) {
				// interrupted: end the thread
			}
		});
		worker.start();
	}

	public synchronized void push(Runnable r) {
		if (closed || r == null)
			return; // no new tasks after close
		try {
			queue.put(r);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	public synchronized void close() {
		if (closed)
			return;
		closed = true;
		try {
			queue.put(STOP); // all tasks pushed before close still run first (FIFO)
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
}
