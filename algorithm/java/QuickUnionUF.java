// https://www.coursera.org/learn/algorithms-part1/lecture/ZgecU/quick-union

public class QuickUnionUF {

    private int[] id;

    public QuickUnionUF(int N)
    {
      id = new int[N]
      for (int i = 0 ; i < N; i++)
            id[i] = i;
    }

    // key part of the QuickUnion algorithm
    private int root(int i)
    { 
      // chase parent pointers until reach (depth of i array accesses)
      // example 
      // -> id = [1,2,3,4,5,6]
      // union (1,3)       (ini i = 1, ini j = 3 -> id[1] = 3)
      //  -> id = [3,2,3,4,5,6]
      // union (1,4)       (i =1 -> root(int i) -> i =3 => int i = 3, int j = 4 -> int[3] = 4)
      //  -> id = [3,2,4,4,5,6]
      while (i != id[i]) i = id[i];
      return i;
    }

    public boolean connected(int p, int q)
    { 
      return root[p] == root(q);
    }

    public void union(int p, int q)
    {
      int i = root(p);
      int j = root(q);
      id[i] = j;
    }
}