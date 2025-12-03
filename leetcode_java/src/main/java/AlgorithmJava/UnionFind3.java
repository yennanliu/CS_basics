package AlgorithmJava;

import java.util.HashSet;

/**
 * UnionFind implementation V3 (LC 684)
 *
 * <p>(chatgpt)
 */
public class UnionFind3 {
  // Union-find data structure
  int[] parents;
  int[] size;
  int n;

  // Constructor to initialize the union-find data structure
  public UnionFind3(int[][] edges) {
    HashSet<Integer> set = new HashSet<>();
    for (int[] x : edges) {
      set.add(x[0]);
      set.add(x[1]);
    }
    this.n = set.size();

    // Initialize parent and size arrays
    this.parents = new int[n + 1]; // Using 1-based indexing
    this.size = new int[n + 1];
    for (int i = 1; i <= n; i++) {
      this.parents[i] = i;
      this.size[i] = 1;
    }
  }

  // Find the root of the set containing 'x' with path compression
  public int getParent(int x) {
    /**
     * NOTE !!!
     *
     *  we use `!=` logic below to simplify code
     */
    if (x != this.parents[x]) {
      // Path compression: recursively find the parent and update the current node's
      // parent
      /**
       *  NOTE !!!
       *
       *  we should update parent as `getParent(this.parents[x])`,
       *  e.g. -> use `this.parents[x]` as parameter, send into getParent method,
       *       -> then assign result to this.parents[x]
       */
      this.parents[x] = getParent(this.parents[x]);
    }
    return this.parents[x];
  }

  // Union the sets containing x and y, return false if they are already connected
  public boolean union(int x, int y) {
    int rootX = getParent(x);
    int rootY = getParent(y);

    // If they are already in the same set, a cycle is detected
    if (rootX == rootY) {
      return false;
    }

    // Union by size: attach the smaller tree to the root of the larger tree
    if (size[rootX] < size[rootY]) {
      parents[rootX] = rootY;
      size[rootY] += size[rootX];
    } else {
      parents[rootY] = rootX;
      size[rootX] += size[rootY];
    }
    return true;
  }
}
