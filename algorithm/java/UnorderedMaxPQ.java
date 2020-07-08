// Priority Queue (unsorted array)
// https://www.coursera.org/learn/algorithms-part1/lecture/A3kA3/apis-and-elementary-implementations

public Class UnorderedMaxPQ<Key extends Comparable<Key>>
{
    private Key[] pq; 
    private int N; 

    public UnorderedMaxPQ(int capacity)
    { pq = (Key[]) new Comparable[capacity]; }

    public boolean IsEmpty()
    { return N  == 0; }

    public void insert(Key x)
    { pq[N++] = x ; }

    public Key delMax()
    {
        int max = 0;
        for (int i = 1; i < N; i++)
            if (less(max, i)) max = i;
        exch(max, N-1);
        return pq[--N];
    }

}