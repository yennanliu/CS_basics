public class BinaryHeap<Key extends Comparable<key>>
{
	private Key[] pq;
	private int N; 

	public MaxPQ(int capacity)
	{ pq = (Key[]) new Comparable[capacity+1]; }

	public Boolean isEempty()
	{ return N  == 0; }

	public void insert(Key, Key)
	public Key delMax()

	private void swim(int k)
	private void sink(int k)

	private boolean less(int i, int j)
	{ return pq[i].compareTo(pq[j]) < 0;}

	private void exch(int i, int j)
	{ key t = pq[i]; pq[i] = pq[j]; pq[j] = t;}

}