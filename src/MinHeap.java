
// DO NOT IMPORT ANYTHING.

/**
 * 
 * A min binary heap.
 * 
 * MIN HEAP INVARIANT: For every i = 1, ..., n-1: heap[getParent(i)] <= heap[i].
 * 
 * The data structure should always satisfy the invariant.
 * 
 */
public class MinHeap
{
	/** Number of entries in the heap. **/
	int n;
	/** The array that holds the entries of the heap. **/
	HeapEntry[] heap;

	/**
	 * Construct an empty heap.
	 * 
	 * @param nMax Maximum number of entries the heap can hold.
	 */
	public MinHeap(int nMax) {
		this.heap = new HeapEntry[nMax];
		this.n = 0;
	}

	/**
	 * Can be O(n), but even if you do it in O(n log n) it is fine.
	 * 
	 * Constructs a heap from a collection of entries.
	 * 
	 * Assume: All the references in the collection are distinct.
	 * 
	 * @param entries A collection of heap entries.
	 */
	public MinHeap(HeapEntry[] entries) {
		if (entries == null) {
			throw new IllegalArgumentException();
		}

		boolean moreEntries = true;
		this.n = 0;
		this.heap = new HeapEntry[entries.length];
		for (int i = 0; (i < entries.length) && moreEntries; i++) {
			if (entries[i] == null) {
				moreEntries = false;
			} else {
				add(entries[i]);
			}
		}
	}

	/**
	 * Should be O(log n).
	 * Adds a new entry to the heap.
	 * 
	 * @param e A heap entry.
	 */
	public void add(HeapEntry e) {
		if (e == null) {
			throw new IllegalArgumentException();
		}
		if (n == heap.length) {
			throw new RuntimeException();
		}
		heap[n] = e;
		e.heapIndex = n;
		this.n += 1;
		heapify(e.heapIndex);
	}

	/**
	 * 
	 * @return Number of elements in the heap.
	 */
	public int size() {
		return n;
	}

	/**
	 * Should be O(1).
	 * 
	 * @param i Index in the heap array that specifies a subtree of the heap.
	 * @return The index for the parent of the subtree (-1 if there isn't one).
	 */
	public int getParent(int i) {
		if (i < 0 || i >= n) {
			throw new IllegalArgumentException();
		}

		if (i == 0) {
			return -1;
		} else {
			return (i - 1) / 2;
		}
	}

	/**
	 * Should be O(1).
	 * 
	 * @param i Index in the heap array that specifies a subtree of the heap.
	 * @return True iff the subtree rooted at i is a leaf.
	 */
	public boolean isLeaf(int i) {
		if (i < 0 || i >= n) {
			throw new IllegalArgumentException();
		}

		if (2 * i + 1 >= n) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Should be O(1).
	 * 
	 * @param i Index in the heap array that specifies a subtree of the heap.
	 * @return The index for the left child of the subtree (-1 if there isn't one).
	 */
	public int getLeft(int i) {
		if (i < 0 || i >= n) {
			throw new IllegalArgumentException();
		}

		if (isLeaf(i)) {
			return -1;
		} else {
			return 2 * i + 1;
		}
	}

	/**
	 * Should be O(1).
	 * 
	 * @param i Index in the heap array that specifies a subtree of the heap.
	 * @return The index for the right child of the subtree (-1 if there isn't one).
	 */
	public int getRight(int i) {
		if (i < 0 || i >= n) {
			throw new IllegalArgumentException();
		}

		if (2 * i + 2 >= n) {
			return -1;
		} else {
			return 2 * i + 2;
		}
	}

	/**
	 * Should be O(log n).
	 * 
	 * Assume: - The subtree rooted at getLeft(i) satisfies the heap invariant. - The subtree rooted at getRight(i) satisfies the heap invariant.
	 * After the method terminates, the subtree rooted at i must satisfy the heap invariant.
	 * 
	 * @param i Index in the heap array that specifies a subtree of the heap.
	 */
	private void heapify(int i) {
		int newIndex = -1;
		if (i < 0 || i >= n) {
			throw new IllegalArgumentException();
		}

		// Check if value is less than the parent
		if (!(i == 0) && heap[i].compareTo(heap[getParent(i)]) < 0) {
			swap(i, getParent(i));
			newIndex = getParent(i);
		}

		else if (getLeft(i) != -1 && getRight(i) == -1) {
			// Check if value is more than the single child
			if (heap[i].compareTo(heap[getLeft(i)]) > 0) {
				swap(i, getLeft(i));
				newIndex = getLeft(i);
			}
		}

		else if (getLeft(i) != -1 && getRight(i) != -1) {
			// Check if the value is more than either child
			if (heap[i].compareTo(heap[getLeft(i)]) > 0 || heap[i].compareTo(heap[getRight(i)]) > 0) {
				if (heap[getLeft(i)].compareTo(heap[getRight(i)]) > 0) {
					// swap with right since the right is smaller than the left
					swap(i, getRight(i));
					newIndex = getRight(i);
				} else {
					// swap with left since the left is smaller than the right
					swap(i, getLeft(i));
					newIndex = getLeft(i);
				}
			}
		}
		if (newIndex != -1) {
			heapify(newIndex);
		}
	}

	/**
	 * Swaps the two entries of heap for the specified indices
	 * @param index1
	 * @param index2
	 */
	private void swap(int index1, int index2) {
		HeapEntry x = heap[index1];
		heap[index1] = heap[index2];
		heap[index2] = x;
		updateIndex(index1);
		updateIndex(index2);
	}

	/**
	 * Updates the heapIndex to the new index number
	 * @param index
	 */
	private void updateIndex(int index) {
		heap[index].heapIndex = index;
	}

	/**
	 * 
	 * @return The entry at the top of the heap.
	 */
	public HeapEntry peekMin() {
		if (n == 0) {
			throw new IllegalArgumentException();
		}

		return heap[0];
	}

	/**
	 * Should be O(log n).
	 * 
	 * Removes the entry from the top.
	 * 
	 * @return The entry at the top of the heap.
	 */
	public HeapEntry extractMin() {
		if (n == 0) {
			throw new IllegalArgumentException();
		}

		HeapEntry min = heap[0];
		if (n != 1) {
			heap[0] = heap[n - 1];
			updateIndex(0);
		}
		heap[n - 1] = null;
		n = n - 1;
		if (n != 0) {
			heapify(0);
		}
		return min;
	}

	/**
	 * Should be O(log n).
	 * 
	 * Updates a tuple with a new value <= current value.
	 * 
	 * @param e An entry that is inside the heap.
	 * @param newValue New value, has to be <= e.value.
	 */
	public void update(HeapEntry e, double newValue) {
		if (e.value < newValue) {
			System.err.println("e.value = " + e.value + ", newValue = " + newValue);
			throw new IllegalArgumentException();
		}

		e.value = newValue;
		heapify(e.heapIndex);

	}

	// YOU CAN USE THE METHODS BELOW TO DEBUG YOUR PROGRAM.
	// Of course, passing the sanity check does not mean your program is correct.
	// You don't have to use these methods or understand them, if you don't want to.

	/**
	 * Checks the heap.
	 */
	public boolean checkHeap() {
		// Check heap indexes.
		for (int i = 0; i < n; i++) {
			System.out.println(heap[i].heapIndex);
			if (heap[i].heapIndex != i) {
				System.err.println("Heap indexes not maintained correctly.");
				return false;
			}
		}

		// Check heap invariant.
		for (int i = 1; i < n; i++) {
			if (heap[i].compareTo(heap[getParent(i)]) < 0) {
				System.err.println("Heap invariant violated @" + i + ".");
				System.err.println("Current: " + heap[i] + ".");
				System.err.println("Parent: " + heap[getParent(i)] + ".");
				return false;
			}
		}

		return true;
	}

	private final String indentation = "   ";

	@Override
	public String toString() {
		if (n == 0) {
			return "";
		}

		return toStringHelper(0, "");
	}

	private String toStringHelper(int i, String indent) {
		if (i < 0 || i >= n) {
			throw new IllegalArgumentException();
		}

		String str = indent + heap[i] + "\n";

		int left = getLeft(i);
		if (left == -1) {
			return str;
		}
		String newIndent = indent + indentation;
		str += toStringHelper(left, newIndent);

		int right = getRight(i);
		if (right == -1) {
			return str;
		}
		str += toStringHelper(right, newIndent);

		return str;
	}
}
