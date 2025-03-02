package AlgorithmJava;

/**
 * UnionFind implementation V3
 *
 * <p>(chatgpt)
 */
public class UnionFind3 {

  private int[] parent;

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

    // Simple union without rank: just attach one root to the other
    parent[rootX] = rootY;

    return true;
  }
}
