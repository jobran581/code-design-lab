package test;

import java.util.concurrent.RecursiveTask;

public class ParMaxSearcher extends RecursiveTask<Integer> {

	private static final long serialVersionUID = 1L;

	private final BinTree node;

	public ParMaxSearcher(BinTree node) {
		this.node = node;
	}

	@Override
	protected Integer compute() {
		int max = node.get();
		BinTree left = node.getLeft();
		BinTree right = node.getRight();

		// the left subtree is searched by another thread of the fork join pool
		ParMaxSearcher leftTask = null;
		if (left != null) {
			leftTask = new ParMaxSearcher(left);
			leftTask.fork();
		}

		// meanwhile, this thread searches the right subtree
		if (right != null)
			max = Math.max(max, new ParMaxSearcher(right).compute());

		if (leftTask != null)
			max = Math.max(max, leftTask.join());

		return max;
	}
}
