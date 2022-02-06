# Union Find
> Check "connection status" for nodes in graphs
- How many connections ?
- Whether 2 nodes are "connected" (with same parent)
- Ref
    - [Union find basic](https://github.com/labuladong/fucking-algorithm/blob/master/%E7%AE%97%E6%B3%95%E6%80%9D%E7%BB%B4%E7%B3%BB%E5%88%97/UnionFind%E7%AE%97%E6%B3%95%E8%AF%A6%E8%A7%A3.md)
    - [Union find use cases](https://github.com/labuladong/fucking-algorithm/blob/master/%E7%AE%97%E6%B3%95%E6%80%9D%E7%BB%B4%E7%B3%BB%E5%88%97/UnionFind%E7%AE%97%E6%B3%95%E5%BA%94%E7%94%A8.md)

## 0) Concept
- Key: Implement 2 API:
    - `Union`
    - `connected`

### 0-1) Types

### 0-2) Pattern

## 1) General form
```java
// java
//---------------------------------------------------
// UnionFind implemented in java (basic) (V1)
// (algorithm book (labu) p.412)
//---------------------------------------------------
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
        //parent[rootQ] = rootP is OK as well
        parent[rootP] = rootQ
        count --;
    }

    public boolean connected(int p , int q){
        int rootP = find(p);
        int rootQ = find(q);
        return rootP == rootQ;
    }

    public int find(int x){
        while (parent[x] != x){
            x = parent[x];
        }
        return x;
    }

    public int count(){
        return count;
    }
}
```

```java
// https://github.com/yennanliu/CS_basics/blob/master/leetcode_java/src/main/java/AlgorithmJava/UnionFind.java
package AlgorithmJava;
//---------------------------------------------------
// UnionFind implemented in java (basic) (V2)
// (algorithm book (labu) p.418)
//---------------------------------------------------
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

        // balance tree : put small tree under big tree (NOT necessary)
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

```python
#--------------------------
# UNION FIND V1 (basic)
#--------------------------
class UnionFind:

    def __init__(self, n):
        self.count = n
        self.parent = [None] * n
        self.size = [None] * n

        for i in range(len(n)):
            self.parent[i] = i
            self.size = 1

    def union(self, p, q):
        rootP = self.find(p)
        rootQ = self.find(q)
        if rootP == rootQ:
            return
        #self.parent[rootQ] = rootP  # this is OK as well
        self.parent[rootP] = rootQ
        self.count -= 1

    def connected(self, p, q):
        rootP = self.find(p)
        rootQ = self.find(q)
        return rootP == rootQ

    def find(self, x):
        while self.parent[x] != x:
            x = parent[x]
        return x

    def count(self):
        return self.count
```

```python
#-------------------
# UNION FIND V2
#-------------------
class UnionFind(object):
    def __init__(self, n):
        self.set = range(n)
        self.count = n
 
    def find_set(self, x):
       if self.set[x] != x:
           self.set[x] = self.find_set(self.set[x])  # path compression.
       return self.set[x]
 
    def union_set(self, x, y):
        x_root, y_root = map(self.find_set, (x, y))
        if x_root != y_root:
            self.set[min(x_root, y_root)] = max(x_root, y_root)
            self.count -= 1
            
class Solution(object):
    def countComponents(self, n, edges):
        union_find = UnionFind(n)
        for i, j in edges:
            union_find.union_set(i, j)
        return union_find.count
```

### 1-1) Basic OP

## 2) LC Example
