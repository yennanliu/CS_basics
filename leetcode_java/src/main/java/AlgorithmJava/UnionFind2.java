package AlgorithmJava;

/**
 * UnionFind implementation V2
 *
 * <p>(chatgpt)
 */
public class UnionFind2 {

  private int[] parent;
  private int[] rank;

  private int find(int x) {
    if (parent[x] != x) {
      parent[x] = find(parent[x]); // Path compression
    }
    return parent[x];
  }

  private boolean union(int x, int y) {
    int rootX = find(x);
    int rootY = find(y);

    if (rootX == rootY) {
      return false; // Cycle detected
    }

    // Union by rank
    if (rank[rootX] > rank[rootY]) {
      parent[rootY] = rootX;
    } else if (rank[rootX] < rank[rootY]) {
      parent[rootX] = rootY;
    } else {
      parent[rootY] = rootX;
      rank[rootX]++;
    }

    return true;
  }
}
