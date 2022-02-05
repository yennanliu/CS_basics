# Union Find 

- https://github.com/labuladong/fucking-algorithm/blob/master/%E7%AE%97%E6%B3%95%E6%80%9D%E7%BB%B4%E7%B3%BB%E5%88%97/UnionFind%E7%AE%97%E6%B3%95%E8%AF%A6%E8%A7%A3.md
- https://github.com/labuladong/fucking-algorithm/blob/master/%E7%AE%97%E6%B3%95%E6%80%9D%E7%BB%B4%E7%B3%BB%E5%88%97/UnionFind%E7%AE%97%E6%B3%95%E5%BA%94%E7%94%A8.md

## 0) Concept  

### 0-1) Types

### 0-2) Pattern

## 1) General form
```java
// java
// https://github.com/yennanliu/CS_basics/blob/master/leetcode_java/src/main/java/AlgorithmJava/UnionFind.java
package AlgorithmJava;

/** UnionFind implemented in java
 *
 *  (algorithm book (labu) p.418)
 */
public class UnionFind {
    // attr

    // connect count
    private int count;
    // save each node's parent node
    private int[] parent;
    // record tree's "weight"
    private int[] size;

    // constructor
    public UnionFind(int n){
        this.count = n;
        parent = new int[n];
        size = new int[n];
        for (int i = 0; i < n; i++){
            parent[i] = i;
            size[i] = 1;
        }
    }

    // method
    public void union(int p, int q){
        int rootP = find(p);
        int rootQ = find(q);
        if (rootP == rootQ){
            return;
        }

        // balance tree : put small tree under big tree
        if (size[rootP] > size[rootQ]){
            parent[rootQ] = rootP;
            size[rootP] += size[rootQ];
        }else{
            parent[rootP] = rootQ;
            size[rootQ] += size[rootP];
        }
        count --;
    }

    public boolean connected(int p , int q){
        int rootP = find(p);
        int rootQ = find(q);
        return rootP == rootQ;
    }

    public int find(int x){
        while (parent[x] != x){
            /** do route compression !!! */
            parent[x] = parent[parent[x]];
            x = parent[x];
        }
        return x;
    }

    public int count(){
        return count;
    }
}
```

### 1-1) Basic OP

## 2) LC Example
