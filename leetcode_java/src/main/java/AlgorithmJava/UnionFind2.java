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
    /** Explain why
     *  1. if `parent[x] != x
     *  2. parent[x] = find(parent[x])
     *
     *  (from gpt)
     *
     *
     *
     ---

     ### ✅ Yes, you're right: `parent[i] = i` at initialization.

     That means:
     - Every node **starts as its own parent**, i.e., it is its own set.
     - So, `parent[x] == x` means that **`x` is the root of its set**. !!!!!

     ---

     ### ❓ So then, **why do we check `if (parent[x] != x)`**?

     Because that tells us:
     - **"Is `x` a child of another node?"**
     - If `parent[x] != x`, then **`x` is not the root**, so we need to keep going up the tree to find the root of its set.  !!!!

     ---

     ### 🔄 What happens during `find(x)`?

     Let’s say:
     ```java
     parent = [0, 0, 1, 2]  // node 3 → 2 → 1 → 0
     ```

     If we call `find(3)`:

     1. `parent[3] = 2` → not root → recurse `find(2)`
     2. `parent[2] = 1` → not root → recurse `find(1)`
     3. `parent[1] = 0` → not root → recurse `find(0)`
     4. `parent[0] = 0` → root found

     Now we go back up the call stack and do path compression:
     - `parent[3] = 0`
     - `parent[2] = 0`
     - `parent[1] = 0`

     So next time we call `find(3)`, it's **just one step**!

     ---

     ### 📌 Why we use `find(parent[x])`, not `find(x)`:
     This is what actually **walks up the tree**. If we called `find(x)` again, it would recurse infinitely if `x` is not its own parent.

     We want:
     ```java
     parent[x] = find(parent[x]);  // Not: parent[x] = find(x);
     ```

     Otherwise, you're just stuck calling the same function with the same `x` forever if `parent[x] != x`.

     ---

     ### 🔁 Summary

     | Concept | Meaning |
     |--------|---------|
     | `parent[x] == x` | `x` is the **root** of its set |
     | `parent[x] != x` | `x` is **not root**, follow `parent[x]` up the tree |
     | `find(parent[x])` | Recurse up until you hit the root |
     | `parent[x] = ...` | **Path compression**: flatten the tree for speed |

     ---
     *
     *
     *
     */
    if (parent[x] != x) {
      /**
       *
       * NOTE !!! we put `parent[x]` into `find` func as parameter
       *
       *  (instead of `x`)
       */
      /**
       *  NOTE !!!
       *
       *  we should update parent as `getParent(this.parents[x])`,
       *  e.g. -> use `this.parents[x]` as parameter, send into getParent method,
       *       -> then assign result to this.parents[x]
       */
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
