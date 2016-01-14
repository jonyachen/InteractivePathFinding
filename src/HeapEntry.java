
/**
 * NOTHING FOR YOU TO DO HERE.
 * 
 * An entry for a heap.
 * 
 */
public class HeapEntry implements Comparable<HeapEntry>
{
	/** Key. */
	public int key;
	/** Value. */
	public double value;
	/** Position in the heap. */
	public int heapIndex;

	public HeapEntry(int k, double v) {
		key = k;
		value = v;
		heapIndex = -1;
	}

	@Override
	public int compareTo(HeapEntry o) {
		return Double.compare(value, o.value);
	}

	@Override
	public String toString() {
		return "(" + key + "," + value + ")";
	}
}
