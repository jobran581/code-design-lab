package test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class MyFuture<V> {
	private V v;

	// what to do with v once it arrives (registered in advance by thenDo / finallyDo)
	private final List<Consumer<V>> actions = new ArrayList<>();
	private boolean isSet = false;

	// sets the value and starts the chain reaction of all the registered actions, in order
	public void set(V v) {
		List<Consumer<V>> toRun;
		synchronized (this) {
			this.v = v;
			isSet = true;
			toRun = new ArrayList<>(actions);
		}
		for (Consumer<V> a : toRun)
			a.accept(v);
	}

	// registers f to run on the value, and returns a new MyFuture for f's result (which may have another type)
	public <R> MyFuture<R> thenDo(Function<V, R> f) {
		MyFuture<R> next = new MyFuture<>();
		addAction(value -> next.set(f.apply(value)));
		return next;
	}

	// ends the chain: c consumes the value when it arrives and returns nothing
	public void finallyDo(Consumer<V> c) {
		addAction(c);
	}

	private void addAction(Consumer<V> a) {
		boolean runNow;
		V value;
		synchronized (this) {
			actions.add(a);
			runNow = isSet; // the value already arrived: run the new action right away
			value = v;
		}
		if (runNow)
			a.accept(value);
	}
}
