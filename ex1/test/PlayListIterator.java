package test;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class PlayListIterator implements Iterator<String> {

	// one frame per nesting level: which play list we are in and the next index to read
	private static class Frame {
		final Item[] items;
		int index;
		Frame(Item[] items) {
			this.items = items;
			this.index = 0;
		}
	}

	private final Deque<Frame> stack = new ArrayDeque<>();
	private Song nextSong; // the song that next() will return, or null if not found yet

	public PlayListIterator(PlayList pl) {
		if (pl.getItems() != null)
			stack.push(new Frame(pl.getItems()));
	}

	// moves forward until the next song is found (or the whole tree is exhausted)
	private void advance() {
		while (nextSong == null && !stack.isEmpty()) {
			Frame top = stack.peek();
			if (top.index >= top.items.length) {
				stack.pop(); // finished this level, go back to the parent
				continue;
			}
			Item item = top.items[top.index++];
			if (item instanceof PlayList) {
				Item[] sub = ((PlayList) item).getItems();
				if (sub != null)
					stack.push(new Frame(sub)); // go one level deeper
			} else if (item instanceof Song) {
				nextSong = (Song) item;
			}
		}
	}

	@Override
	public boolean hasNext() {
		advance();
		return nextSong != null;
	}

	@Override
	public String next() {
		if (!hasNext())
			throw new NoSuchElementException();
		String name = nextSong.name;
		nextSong = null;
		return name;
	}
}
