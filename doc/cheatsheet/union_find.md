# Union Find
> Check "connection status" for odes in graphs
- how many connections ?
- if 2 nodes are connected (with same parent)
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
# UNION FIND
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

```python
class Graph(object):

    def __init__(self):
        self.numberOfNodes = 0
        self.adjacentList = {}

    def addVertex(self, node):
        self.adjacentList[node] = []
        self.numberOfNodes += 1 

    def addEdge(self, node1, node2):
        self.adjacentList[node1].append(node2)
        self.adjacentList[node2].append(node1)

    def showConnections(self):
        allNodes = self.adjacentList.keys()
        for node in allNodes:
            nodeConnections = self.adjacentList[node]
            connections = ""
            for vertex in nodeConnections:
                connections += vertex + " "
            print (node + "-->" + connections)         
```

### 1-1) Basic OP

## 2) LC Example
