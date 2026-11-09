package test;

import java.util.Observable;
import java.util.concurrent.Future;

@SuppressWarnings("deprecation") // java.util.Observable is deprecated since Java 9, but it is the Observable this exercise uses
public class ObservableFuture<V> extends Observable {

	private volatile V value; // null until the Future's value arrives

	public ObservableFuture(Future<V> future) {
		// wait for the value in the background, so the constructor returns immediately
		Thread t = new Thread(() -> {
			try {
				V v = future.get(); // blocks only this background thread
				value = v;
				setChanged();
				notifyObservers(v);
			} catch (Exception e) {
				// the task failed or was cancelled: there is no value to report
			}
		});
		t.setDaemon(true); // never keeps the program alive on its own
		t.start();
	}

	// returns the value without waiting (null if it is not ready yet)
	public V get() {
		return value;
	}
}
