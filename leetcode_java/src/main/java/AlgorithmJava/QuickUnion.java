package AlgorithmJava;

/**
     (Created by GPT)

     Quick Union with Path Compression Example

     Input: n = 5, edges = [[0,1],[1,2],[3,4]]

     This input represents 5 nodes and 3 edges connecting them. The goal is to manage the union and find operations using Quick Union with path compression.

     ### Initial State

     1. **Initialization**:
     Each element is its own root.
     ```java
     int[] root = new int[n];
     for (int i = 0; i < n; i++) {
     root[i] = i;
     }
     // root = [0, 1, 2, 3, 4]
     ```

     ### Union Operations

     2. **Union(0, 1)**:
     - Find the root of 0 and 1.
     - Set the root of 1 to be the root of 0.
     ```java
     root[1] = find(root, 0); // root[1] = 0
     // root = [0, 0, 2, 3, 4]
     ```

     3. **Union(1, 2)**:
     - Find the root of 1 (which involves path compression).
     - Set the root of 2 to be the root of 1.
     ```java
     root[2] = find(root, 1); // root[2] = 0 (after path compression)
     // root = [0, 0, 0, 3, 4]
     ```

     4. **Union(3, 4)**:
     - Find the root of 3 and 4.
     - Set the root of 4 to be the root of 3.
     ```java
     root[4] = find(root, 3); // root[4] = 3
     // root = [0, 0, 0, 3, 3]
     ```

     ### Find Operations with Path Compression

     Let's perform some `find` operations to understand how path compression works.

     5. **Find(2)**:
     - Find the root of 2.
     - Root of 2 is 0 (which is already its root).
     ```java
     int root2 = find(root, 2); // root2 = 0
     ```

     6. **Find(1)**:
     - Find the root of 1.
     - Root of 1 is 0 (which is already its root).
     ```java
     int root1 = find(root, 1); // root1 = 0
     ```

     ### Explanation of the Find Method with Path Compression

     Here is the `find` method implementation again for reference:

     ```java
     private int find(int[] root, int e) {
     if (root[e] == e) {
     return e; // The element is the root of its set
     } else {
     root[e] = find(root, root[e]); // Path compression
     return root[e];
     }
     }
     ```

     ### Detailed Example with Steps

     #### Union(0, 1)

     - Initial roots: `[0, 1, 2, 3, 4]`
     - Find root of 0: `find(root, 0) -> 0`
     - Find root of 1: `find(root, 1) -> 1`
     - Union: `root[1] = 0`
     - Roots after union: `[0, 0, 2, 3, 4]`

     #### Union(1, 2)

     - Initial roots: `[0, 0, 2, 3, 4]`
     - Find root of 1: `find(root, 1) -> 0`
     - `root[1]` is 0, and `find(root, 0) -> 0`
     - Path compression: `root[1] = 0`
     - Find root of 2: `find(root, 2) -> 2`
     - Union: `root[2] = 0`
     - Roots after union: `[0, 0, 0, 3, 4]`

     #### Union(3, 4)

     - Initial roots: `[0, 0, 0, 3, 4]`
     - Find root of 3: `find(root, 3) -> 3`
     - Find root of 4: `find(root, 4) -> 4`
     - Union: `root[4] = 3`
     - Roots after union: `[0, 0, 0, 3, 3]`

     ### Final State

     After performing all the union operations, the root array is:

     ```
     root = [0, 0, 0, 3, 3]
     ```

     This indicates two distinct sets:
     - One set with root 0: {0, 1, 2}
     - Another set with root 3: {3, 4}

     ### Checking Connections

     To check if two nodes are connected, we use the `find` method to see if they share the same root.

     For example:
     - `find(root, 2) -> 0`
     - `find(root, 1) -> 0`

     Both nodes 2 and 1 have the same root (0), indicating they are in the same set.

 */

public class QuickUnion {
    private int[] root;

    // Initialize the root array where each element is its own root initially
    public QuickUnion(int size) {
        root = new int[size];
        for (int i = 0; i < size; i++) {
            root[i] = i;
        }
    }

    // Find the root of the element
    public int find(int e) {
        if (root[e] == e) {
            return e;
        } else {
            root[e] = find(root[e]); // Path compression
            return root[e];
        }
    }

    // Union operation to connect two elements
    public void union(int e1, int e2) {
        int rootE1 = find(e1);
        int rootE2 = find(e2);
        if (rootE1 != rootE2) {
            root[rootE1] = rootE2; // Merge the sets
        }
    }

    // Check if two elements are connected
    public boolean connected(int e1, int e2) {
        return find(e1) == find(e2);
    }

    public static void main(String[] args) {
        QuickUnion qu = new QuickUnion(5);
        qu.union(0, 1);
        qu.union(1, 2);
        qu.union(3, 4);

        System.out.println("0 and 2 connected: " + qu.connected(0, 2)); // true
        System.out.println("0 and 3 connected: " + qu.connected(0, 3)); // false
    }
}
