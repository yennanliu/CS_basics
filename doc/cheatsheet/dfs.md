# DFS 

- Deep-first search
- search algorithm
- Deep first, then breadth
- Use data structure : `Stack`   (FILO)
- To check if some value exists
- Inorder, preorder, postorder (can recreate a tree)
- not most efficient (VS bfs), but can handle some specific problems

<img src ="https://github.com/yennanliu/CS_basics/blob/master/doc/pic/dfs_2.png"></p>


<img src ="https://github.com/yennanliu/CS_basics/blob/master/doc/pic/dfs_vs_bfs.png"></p>


## 0) Concept

### 0-1) Types

- Types
    - normal transversal (pre-order, in-order, post-order)
    - normal transversal with special op:
        - `root.right -> do sth -> root.left`

- Algorithm
    - dfs
    - recursive
    - graph

- Data structure
    - TreeNode
    - dict
    - set
    - array

### 0-2) Pattern

#### 0-2-1) General form

```java
// java
// crack code p.136

// 1) Preorder traversal
public TreeNode PreOrderTraversal(TreeNode root){
    if(root != null){
        System.out.println(root);
        PreOrderTraversal(root.left);
        PreOrderTraversal(root.right);
    }
}

// 2) Postorder traversal
public TreeNode PostOrderTraversal(TreeNode root){
    if(root != null){
        PostOrderTraversal(root.left);
        PostOrderTraversal(root.right);
        System.out.println(root);
    }
}

// 3) Inorder traversal
public TreeNode InOrderTraversal(TreeNode root){
    if(root != null){
        InOrderTraversal(root.left);
        System.out.println(root);
        InOrderTraversal(root.right);
    }
}
```

```python
# form I
def dfs(root):

    # if root, do sth
    if root:
        # do sth, pre-order (root->left->right) in this code

    # if not root, do NOTHING

    # if root.left exist
    if root.left:
        dfs(root.left)
    # if root.right exist
    if root.right:
        dfs(root.right)

# form II
def dfs(root):

    # if root, do sth
    if root:
        # do sth, pre-order (root->left->right) in this code

    # if not root, nothing to do

    dfs(root.left)
    dfs(root.right)
```

#### 0-2-2) Basic Tricks


- Assign sub tree to node, then return updated node at final stage (Important !!!!)

```java
// java
// LC 199
private TreeNode _dfs(TreeNode node){

    if (node == null){
        return null;
    }

    /** NOTE !!! no need to create global node, but can define inside the method */
    TreeNode root2 = node;
    root2.left = this._dfs(node.left);
    root2.right = this._dfs(node.right);

    /** NOTE !!! we need to return root as final step */
    return root2;
}
```
 
- Modify tree `in place`  (Important !!!!)

```java
// java
// LC 701

// ...
public TreeNode insertIntoBST(TreeNode root, int val){
    // ...

    insertNodeHelper(root, val);
    return root;
}

public void insertNodeHelper(TreeNode root, int val) {
    // ...
    if(...){
        root.left = new TreeNode(val);
    }else{
        root.right = new TreeNode(val);
    }

    // ...
    return root;
}

// ... 
```


- Tree transversal (DFS)

```python
# python
# form I : tree transversal
def dfs(root, target):

    if root.val == target:
       # do sth

    if root.val < target:
       dfs(root.left, target)
       # do sth

    if root.val > target:
       dfs(root.right, target)
       # do sth
```

- Tree value moddify (DFS)

```python
# form II : modify values in tree

# 669 Trim a Binary Search Tree
class Solution:
    def trimBST(self, root, L, R):
        if not root:
            return 
        # NOTICE HERE 
        # SINCE IT'S BST
        # SO if root.val < L, THE root.right MUST LARGER THAN L
        # SO USE self.trimBST(root.right, L, R) TO FIND THE NEXT "VALIDATE" ROOT AFTER TRIM
        # THE REASON USE self.trimBST(root.right, L, R) IS THAT MAYBE NEXT ROOT IS TRIMMED AS WELL, SO KEEP FINDING VIA RECURSION
        if root.val < L:
            return self.trimBST(root.right, L, R)
        # NOTICE HERE 
        # SINCE IT'S BST
        # SO if root.val > R, THE root.left MUST SMALLER THAN R
        # SO USE self.trimBST(root.left, L, R) TO FIND THE NEXT "VALIDATE" ROOT AFTER TRIM
        if root.val > R:
            return self.trimBST(root.left, L, R)
        root.left = self.trimBST(root.left, L, R)
        root.right = self.trimBST(root.right, L, R)
        return root 

# 701 Insert into a Binary Search Tree
class Solution(object):
    def insertIntoBST(self, root, val):
        """
        NOTE !!!
            1) we ALWAYS do op first, then do recursive
                -> e.g.
                        ...
                        if not root: 
                            return TreeNode(val)
                        if root.val < val:
                            root.right = self.insertIntoBST(root.right, val)
                        ...
        """
        if not root: 
            return TreeNode(val)

        if root.val < val: 
            root.right = self.insertIntoBST(root.right, val)

        elif root.val > val: 
            root.left = self.insertIntoBST(root.left, val)

        return root
```

```python
# form III : check if a value exist in the BST

def dfs(root, value):
    if not root:
        return False
    if root.val == value:
        return True
    if root.val > value:
        return dfs(root.left, value) 
    if root.val < value:
        return dfs(root.right, value)
```

```python
# form IV : check if duplicated nodes in tree
# LC 652
# python
m = collections.defaultdict(int)
# m is collection for record visited nodes
def dfs(root, m, res):
    if not root:
        return "#"

    ### NOTE : we get path via below, so we can know duplicated nodes
    path = root.val + "-" + self.dfs(root.left, m, res) + "-" + self.dfs(root.right, m, res)

    if m[path] == 1:
        res.append(path)

    m[path] += 1
    return path
```

- Grpah transversal (DFS)


- Transversal in 4 directions (up, down, left, right)
```java
// java
// LC 200

/** NOTE !!!! BELOW approach has same effect */

// V1

// private boolean _is_island(char[][] grid, int x, int y, boolean[][] seen){}

// ....
_is_island(grid, x+1, y, seen);
_is_island(grid, x-1, y, seen);
_is_island(grid, x, y+1, seen);
_is_island(grid, x, y-1, seen);
// ....

// V2
// private boolean _is_island_2(char[][] grid, int x, int y, boolean[][] seen) {}

int[][] directions = { {1, 0}, {-1, 0}, {0, 1}, {0, -1} };

for (int[] dir : directions) {
    int newX = x + dir[0];
    int newY = y + dir[1];
    _is_island(grid, newX, newY, seen);
}
```

### 0-3) Tricks
```python
# we don't need to declare y,z in func, but we can use them in the func directly
# and can get the returned value as well, this trick is being used a lot in the dfs
def test():
    def func(x):
        print ("x = " + str(x) + " y = " + str(y))
        for i in range(3):
            z.append(i)

    x = 0
    y = 100
    z = []
    func(x)
test()
print (z)
```

### 1-1) Basic OP

#### 1-1-1) Add 1 to all node.value in Binary tree?
```python
# Example) Add 1 to all node.value in Binary tree?
def dfs(root):
    if not root:
        return 
    root.val += 1 
    dfs(root.left)
    dfs(root.right)
```

#### 1-1-2) check if 2 Binary tree are the same
```python
# Example) check if 2 Binary tree are the same ? 
def dfs(root1, root2):
    if root1 == root2 == None:
        return True 
    if root1 is not None and root2 is None:
        return False 
    if root1 is None and root2 is not None:
        return False 
    else:
        if root1.val != root2.value:
            return False 
    return dfs(root1.left, root2.left) \
           and dfs(root1.right, root2.right)
```

#### 1-1-3) check if a value exist in the BST
```python
# Example) check if a value exist in the BST
def dfs(root, value):
    if not root:
        return False
    if root.val == value:
        return True
    return dfs(root.left, value) or dfs(root.right, value)

# optimized : BST prpoerty :  root.right > root.val > root.left
def dfs(root, value):
    if not root:
        return False
    if root.val == value:
        return True
    if root.val > value:
        return dfs(root.left, value) 
    if root.val < value:
        return dfs(root.right, value)
```

#### 1-1-4) get sum of sub tree

```python
# get sum of sub tree
# LC 508 Most Frequent Subtree Sum
def get_sum(root):
    if not root:
        return 0
    ### NOTE THIS !!!
    #  -> we need to do get_sum(root.left), get_sum(root.right) on the same time
    s = get_sum(root.left) + root.val + get_sum(root.right)
    res.append(s)
    return s
```

#### 1-1-5) get `aggregated sum` for every node in tree
```python
# LC 663 Equal Tree Partition
# LC 508 Most Frequent Subtree Sum
seen = []
def _sum(root):
    if not root:
        return 0
    seen.append( root.val + _sum(root.left) + _sum(root.right) )
```

#### 1-1-6) Convert BST to Greater Tree 
```python
# Convert BST to Greater Tree 
# LC 538
_sum = 0
def dfs(root):
    dfs(root.right)
    _sum += root.val
    root.val = _sum
    dfs(root.left)
```

#### 1-1-7) Serialize and Deserialize Binary Tree
```python
# LC 297. Serialize and Deserialize Binary Tree
# please check below 2) LC Example
# V0
# IDRA : DFS
class Codec:

    def serialize(self, root):
        """ Encodes a tree to a single string.
        :type root: TreeNode
        :rtype: str
        """
        def rserialize(root, string):
            """ a recursive helper function for the serialize() function."""
            # check base case
            if root is None:
                string += 'None,'
            else:
                string += str(root.val) + ','
                string = rserialize(root.left, string)
                string = rserialize(root.right, string)
            return string
        
        return rserialize(root, '')    

    def deserialize(self, data):
        """Decodes your encoded data to tree.
        :type data: str
        :rtype: TreeNode
        """
        def rdeserialize(l):
            """ a recursive helper function for deserialization."""
            if l[0] == 'None':
                l.pop(0)
                return None
                
            root = TreeNode(l[0])
            l.pop(0)
            root.left = rdeserialize(l)
            root.right = rdeserialize(l)
            return root

        data_list = data.split(',')
        root = rdeserialize(data_list)
        return root
```

```java
// java
// LC 297
public class Codec{
    public String serialize(TreeNode root) {

        /** NOTE !!!
         *
         *     if root == null, return "#"
         */
        if (root == null){
            return "#";
        }

        /** NOTE !!! return result via pre-order, split with "," */
        return root.val + "," + serialize(root.left) + "," + serialize(root.right);
    }

    public TreeNode deserialize(String data) {

        /** NOTE !!!
         *
         *   1) init queue and append serialize output
         *   2) even use queue, but helper func still using DFS
         */
        Queue<String> queue = new LinkedList<>(Arrays.asList(data.split(",")));
        return helper(queue);
    }

    private TreeNode helper(Queue<String> queue) {

        // get val from queue first
        String s = queue.poll();

        if (s.equals("#")){
            return null;
        }
        /** NOTE !!! init current node  */
        TreeNode root = new TreeNode(Integer.valueOf(s));
        /** NOTE !!!
         *
         *    since serialize is "pre-order",
         *    deserialize we use "pre-order" as well
         *    e.g. root -> left sub tree -> right sub tree
         *    -> so we get sub tree via below :
         *
         *       root.left = helper(queue);
         *       root.right = helper(queue);
         *
         */
        root.left = helper(queue);
        root.right = helper(queue);
        /** NOTE !!! don't forget to return final deserialize result  */
        return root;
    }
}
```

#### 1-1-8) Serialize and Deserialize BST
```python
# LC 449. Serialize and Deserialize BST
# please check below 2) LC Example
# NOTE : there is also a bfs approach
# V1'
# IDEA : BST property
# https://leetcode.com/problems/serialize-and-deserialize-bst/discuss/212043/Python-solution
class Codec:

    def serialize(self, root):
        """Encodes a tree to a single string.
        
        :type root: TreeNode
        :rtype: str
        """
        def dfs(root):
            if not root:
                return 
            res.append(str(root.val) + ",")
            dfs(root.left)
            dfs(root.right)
            
        res = []
        dfs(root)
        return "".join(res)

    def deserialize(self, data):
        """Decodes your encoded data to tree.
        
        :type data: str
        :rtype: TreeNode
        """
        lst = data.split(",")
        lst.pop()
        stack = []
        head = None
        for n in lst:
            n = int(n)
            if not head:
                head = TreeNode(n)
                stack.append(head)
            else:
                node = TreeNode(n)
                if n < stack[-1].val:
                    stack[-1].left = node
                else:
                    while stack and stack[-1].val < n: 
                        u = stack.pop()
                    u.right = node
                stack.append(node)
        return head
```

#### 1-1-9) find longest distance between nodes
```java
// java
// LC 543 Diameter of Binary Tree
// V1
// IDEA : DFS
// https://leetcode.com/problems/diameter-of-binary-tree/editorial/

int diameter;

public int diameterOfBinaryTree_2(TreeNode root) {
    diameter = 0;
    longestPath(root);
    return diameter;
}
private int longestPath(TreeNode node){
    if(node == null) return 0;
    // recursively find the longest path in
    // both left child and right child
    int leftPath = longestPath(node.left);
    int rightPath = longestPath(node.right);

    // update the diameter if left_path plus right_path is larger
    diameter = Math.max(diameter, leftPath + rightPath);

    // return the longest one between left_path and right_path;
    // remember to add 1 for the path connecting the node and its parent
    return Math.max(leftPath, rightPath) + 1;
}
```

#### 1-1-10) Compare node val with path

```java
// java
// LC 1448

private void dfsCheckGoodNode(TreeNode node, int maxSoFar) {
    if (node == null)
        return;

    // Check if the current node is good
    if (node.val >= maxSoFar) {
        res++;
        maxSoFar = node.val; // Update max value seen so far
    }

    // Recur for left and right children
    dfsCheckGoodNode(node.left, maxSoFar);
    dfsCheckGoodNode(node.right, maxSoFar);
}
```

## 2) LC Example

### 2-1) Validate Binary Search Tree
```python
# 098 Validate Binary Search Tree
### NOTE : there is also bfs solution
# https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Recursion/validate-binary-search-tree.py
class Solution(object):
    def isValidBST(self, root):
        return self.valid(root, float('-inf'), float('inf'))
        
    def valid(self, root, min_, max_):
        if not root: return True
        if root.val >= max_ or root.val <= min_:
            return False
        return self.valid(root.left, min_, root.val) and self.valid(root.right, root.val, max_)
```

### 2-2) Insert into a Binary Search Tree


```java
// java
// LC 701

public TreeNode insertIntoBST_0_1(TreeNode root, int val) {
    if (root == null) {
        return new TreeNode(val);
    }

    /** 
     *  NOTE !!! 
     *  
     *   via below, we can still `MODIFY root value`,
     *   even it's not declared as a global variable
     *   
     *   -> e.g. we have root as input,
     *      within `insertNodeHelper` method,
     *      we append `new sub tree` to root as its left, right sub tree
     *
     */
    insertNodeHelper(root, val); // helper modifies the tree in-place
    return root;
}

public void insertNodeHelper(TreeNode root, int val) {
    if (val < root.val) {
        if (root.left == null) {
            root.left = new TreeNode(val);
        } else {
            /** NOTE !!!
             * 
             *  no need to return val,
             *  since we `append sub tree` to root directly
             *  in the method (e.g. root.left == ..., root.right = ...)
             */
            insertNodeHelper(root.left, val);
        }
    } else {
        if (root.right == null) {
            root.right = new TreeNode(val);
        } else {
            insertNodeHelper(root.right, val);
        }
    }
}
```

```python
# 701 Insert into a Binary Search Tree

# VO : recursion + dfs
class Solution(object):
    def insertIntoBST(self, root, val):
        if not root: 
            return TreeNode(val)
        if root.val < val: 
            root.right = self.insertIntoBST(root.right, val);
        elif root.val > val: 
            root.left = self.insertIntoBST(root.left, val);
        return(root)
```
`

### 2-3) Delete Node in a BST
```python
# 450 Delete Node in a BST
# V0
# IDEA : RECURSION + BST PROPERTY
#### 2 CASES :
#   -> CASE 1 : root.val == key and NO right subtree 
#                -> swap root and root.left, return root.left
#   -> CASE 2 : root.val == key and THERE IS right subtree
#                -> 1) go to 1st RIGHT sub tree
#                -> 2) iterate to deepest LEFT subtree
#                -> 3) swap root and  `deepest LEFT subtree` then return root
class Solution(object):
    def deleteNode(self, root, key):
        if not root: return None
        if root.val == key:
            # case 1 : NO right subtree 
            if not root.right:
                left = root.left
                return left
            # case 2 : THERE IS right subtree
            else:
                ### NOTE : find min in "right" sub-tree
                #           -> because BST property, we ONLY go to 1st right tree (make sure we find the min of right sub-tree)
                #           -> then go to deepest left sub-tree
                right = root.right
                while right.left:
                    right = right.left
                ### NOTE : we need to swap root, right ON THE SAME TIME
                root.val, right.val = right.val, root.val
        root.left = self.deleteNode(root.left, key)
        root.right = self.deleteNode(root.right, key)
        return root
```

### 2-4) Find Duplicate Subtrees
```python
# 652 Find Duplicate Subtrees
import collections
class Solution(object):
    def findDuplicateSubtrees(self, root):
        res = []
        m = collections.defaultdict(int)
        self.dfs(root, m, res)
        return res

    def dfs(self, root, m, res):
        if not root:
            return '#'
        path = str(root.val) + '-' + self.dfs(root.left, m, res) + '-' + self.dfs(root.right, m, res)
        if m[path] == 1:
            res.append(root) 
        m[path] += 1
        return path
```

### 2-5) Trim a BST
```python
# 669 Trim a Binary Search Tree
class Solution:
    def trimBST(self, root, L, R):
        if not root:
            return None
        if root.val > R:
            return self.trimBST(root.left, L, R)
        elif root.val < L:
            return self.trimBST(root.right, L, R)
        else:
            root.left = self.trimBST(root.left, L, R)
            root.right = self.trimBST(root.right, L, R)
            return root
```

### 2-6) Maximum Width of Binary Tree
```python
# 662 Maximum Width of Binary Tree
class Solution(object):
    def widthOfBinaryTree(self, root):
        self.ans = 0
        left = {}
        def dfs(node, depth = 0, pos = 0):
            if node:
                left.setdefault(depth, pos)
                self.ans = max(self.ans, pos - left[depth] + 1)
                dfs(node.left, depth + 1, pos * 2)
                dfs(node.right, depth + 1, pos * 2 + 1)

        dfs(root)
        return self.ans
```

### 2-7) Equal Tree Partition
```python
# 663 Equal Tree Partition
# V0
# IDEA : DFS + cache
class Solution(object):
    def checkEqualTree(self, root):
        seen = []

        def sum_(node):
            if not node: return 0
            seen.append(sum_(node.left) + sum_(node.right) + node.val)
            return seen[-1]

        sum_(root)
        #print ("seen = " + str(seen))
        return seen[-1] / 2.0 in seen[:-1]

# V0'
class Solution(object):
    def checkEqualTree(self, root):
        seen = []

        def sum_(node):
            if not node: return 0
            seen.append(sum_(node.left) + sum_(node.right) + node.val)
            return seen[-1]
            
        total = sum_(root)
        seen.pop()
        return total / 2.0 in seen
```

### 2-8) Split BST
```python
# 776 Split BST
# V0
# IDEA : BST properties (left < root < right) + recursion
# https://blog.csdn.net/magicbean2/article/details/79679927
# https://www.itdaan.com/tw/d58594b92742689b5769f9827365e8b4
### STEPS
#  -> 1) check whether root.val > or < V
#     -> if root.val > V : 
#           - NO NEED TO MODIFY ALL RIGHT SUB TREE
#           - BUT NEED TO re-connect nodes in LEFT SUB TREE WHICH IS BIGGER THAN V (root.left = right)
#     -> if root.val < V : 
#           - NO NEED TO MODIFY ALL LEFT SUB TREE
#           - BUT NEED TO re-connect nodes in RIGHT SUB TREE WHICH IS SMALLER THAN V (root.right = left)
# -> 2) return result
class Solution(object):
    def splitBST(self, root, V):
        if not root: return [None, None]
        ### NOTE : if root.val <= V
        if root.val > V:
            left, right = self.splitBST(root.left, V)
            root.left = right
            return [left, root]
        ### NOTE : if root.val > V
        else:
            left, right = self.splitBST(root.right, V)
            root.right = left
            return [root, right]
```

### 2-9) Evaluate Division
```python
# 399 Evaluate Division
# there is also an "union find" solution
class Solution:
    def calcEquation(self, equations, values, queries):
        from collections import defaultdict
        # build graph
        graph = defaultdict(dict)
        for (x, y), v in zip(equations, values):
            graph[x][y] = v
            graph[y][x] = 1.0/v
        ans = [self.dfs(x, y, graph, set()) for (x, y) in queries]
        return ans

    def dfs(self, x, y, graph, visited):
        if not graph:
            return
        if x not in graph or y not in graph:
            return -1
        if x == y:
            return 1
        visited.add(x)
        for n in graph[x]:
            if n in visited:
                continue
            visited.add(n)
            d = self.dfs(n, y, graph, visited)
            if d > 0:
                return d * graph[x][n]
        return -1.0
```

```java
// java
// V1
// IDEA: DFS
// https://leetcode.com/problems/evaluate-division/solutions/3543256/image-explanation-easiest-concise-comple-okpu/
public double[] calcEquation_1(List<List<String>> equations, double[] values, List<List<String>> queries) {
    HashMap<String, HashMap<String, Double>> gr = buildGraph(equations, values);
    double[] finalAns = new double[queries.size()];

    for (int i = 0; i < queries.size(); i++) {
        String dividend = queries.get(i).get(0);
        String divisor = queries.get(i).get(1);

        /** NOTE !!!
         *
         *  either dividend nor divisor NOT in graph, return -1.0 directly
         */
        if (!gr.containsKey(dividend) || !gr.containsKey(divisor)) {
            finalAns[i] = -1.0;
        } else {

            /** NOTE !!!
             *
             *  we use `vis` to check if element already visited
             *  (to avoid repeat accessing)
             *  `vis` init again in every loop
             */

            HashSet<String> vis = new HashSet<>();
            /**
             *  NOTE !!!
             *
             *   we init `ans` and pass it to dfs method
             *   (but dfs method return NOTHING)
             *   -> `ans` is init, and pass into dfs,
             *   -> so `ans` value is updated during dfs recursion run
             *   -> and after dfs run completed, we get the result `ans` value
             */
            double[] ans = { -1.0 };
            double temp = 1.0;
            dfs(dividend, divisor, gr, vis, ans, temp);
            finalAns[i] = ans[0];
        }
    }

    return finalAns;
}

/** NOTE !!! below dfs method */
public void dfs(String node, String dest, HashMap<String, HashMap<String, Double>> gr, HashSet<String> vis,
                double[] ans, double temp) {

    /** NOTE !!! we use `vis` to check if element already visited */
    if (vis.contains(node))
        return;

    vis.add(node);
    if (node.equals(dest)) {
        ans[0] = temp;
        return;
    }

    for (Map.Entry<String, Double> entry : gr.get(node).entrySet()) {
        String ne = entry.getKey();
        double val = entry.getValue();
        /** NOTE !!! update temp as `temp * val` */
        dfs(ne, dest, gr, vis, ans, temp * val);
    }
}

public HashMap<String, HashMap<String, Double>> buildGraph(List<List<String>> equations, double[] values) {
    HashMap<String, HashMap<String, Double>> gr = new HashMap<>();

    for (int i = 0; i < equations.size(); i++) {
        String dividend = equations.get(i).get(0);
        String divisor = equations.get(i).get(1);
        double value = values[i];

        gr.putIfAbsent(dividend, new HashMap<>());
        gr.putIfAbsent(divisor, new HashMap<>());

        gr.get(dividend).put(divisor, value);
        gr.get(divisor).put(dividend, 1.0 / value);
    }

    return gr;
}
```

### 2-10) Most Frequent Subtree Sum
```python
# LC 508 Most Frequent Subtree Sum
# V0
# IDEA : DFS + TREE
class Solution(object):
    def findFrequentTreeSum(self, root):
        """
        ### NOTE : this trick : get sum of sub tree
        # LC 663 Equal Tree Partition
        """
        def get_sum(root):
            if not root:
                return 0
            s = get_sum(root.left) + root.val + get_sum(root.right)
            res.append(s)
            return s

        if not root:
            return []
        res = []
        get_sum(root)
        counts = collections.Counter(res)
        _max = max(counts.values())
        return [x for x in counts if counts[x] == _max]

# V0'
# IDEA : DFS + COUNTER
from collections import Counter
class Solution(object):
    def findFrequentTreeSum(self, root):
        def helper(root, d):
            if not root:
                return 0
            left = helper(root.left, d)
            right = helper(root.right, d)
            subtreeSum = left + right + root.val
            d[subtreeSum] = d.get(subtreeSum, 0) + 1
            return subtreeSum      
        d = {}
        helper(root, d)
        mostFreq = 0
        ans = []
        print ("d = " + str(d))
        _max_cnt = max(d.values())
        ans = []
        return [x for x in d if d[x] == _max_cnt]
```

### 2-11) Convert BST to Greater Tree
```python
# LC 538 Convert BST to Greater Tree
# V0
# IDEA : DFS + recursion
#      -> NOTE : via DFS, the op will being executed in `INVERSE` order (last visit will be run first, then previous, then ...)
#      -> e.g. node1 -> node2 -> ... nodeN
#      ->      will run nodeN -> nodeN-1 ... node1
class Solution(object):

    def convertBST(self, root):
        self.sum = 0
        self.dfs(root)
        return root

    def dfs(self, node):
        if not node: 
            return
        #print ("node.val = " + str(node.val))
        self.dfs(node.right)
        self.sum += node.val
        node.val = self.sum
        self.dfs(node.left)

# V0'
# NOTE : the implementation difference on cur VS self.cur
# 1) if cur : we need to ssign output of help() func to cur
# 2) if self.cur : no need to assign, plz check V0 as reference
class Solution(object):
    def convertBST(self, root):
        def help(cur, root):
            if not root:
                ### NOTE : if not root, still need to return cur
                return cur
            ### NOTE : need to assign output of help() func to cur
            cur = help(cur, root.right)
            cur += root.val
            root.val = cur
            ### NOTE : need to assign output of help() func to cur
            cur = help(cur, root.left)
            ### NOTE : need to return cur
            return cur

        if not root:
            return

        cur = 0
        help(cur, root)
        return root
```

### 2-12) Number of Islands
```python
# LC 200 Number of Islands, check LC 694, 711 as well
# V0 
# IDEA : DFS
class Solution(object):
    def numIslands(self, grid):
        def dfs(grid, item):
            if grid[item[0]][item[1]] == "0":
                return

            ### NOTE : MAKE grid[item[0]][item[1]] = 0 -> avoid visit again
            grid[item[0]][item[1]] = 0
            moves = [(0,1),(0,-1),(1,0),(-1,0)]
            for move in moves:
                _x = item[0] + move[0]
                _y = item[1] + move[1]
                ### NOTE : the boundary
                #       -> _x < l, _y < w
                if 0 <= _x < l and 0 <= _y < w and grid[_x][_y] != 0:
                    dfs(grid, [_x, _y])
  
        if not grid:
            return 0
        res = 0
        l = len(grid)
        w = len(grid[0])
        for i in range(l):
            for j in range(w):
                if grid[i][j] == "1":
                    ### NOTE : we go through every "1" in grids, and run dfs once
                    #         -> once dfs completed, we make res += 1 in each iteration
                    dfs(grid, [i,j])
                    res += 1
        return res
```


### 2-13) Max Area of Island
```python
# LC 695. Max Area of Island
# V1
# https://blog.csdn.net/fuxuemingzhu/article/details/79182435
# IDEA : DFS 
# * PLEASE NOTE THAT IT IS NEEDED TO GO THROUGH EVERY ELEMENT IN THE GRID 
#   AND RUN THE DFS WITH IN THIS PROBLEM
class Solution(object):
    def maxAreaOfIsland(self, grid):
        """
        :type grid: List[List[int]]
        :rtype: int
        """
        self.res = 0
        self.island = 0
        M, N = len(grid), len(grid[0])
        for i in range(M):
            for j in range(N):
                if grid[i][j]:
                    self.dfs(grid, i, j)
                    self.res = max(self.res, self.island)
                    self.island = 0
        return self.res
    
    def dfs(self, grid, i, j): # ensure grid[i][j] == 1
        M, N = len(grid), len(grid[0])
        grid[i][j] = 0
        self.island += 1
        dirs = [(0, 1), (0, -1), (-1, 0), (1, 0)]
        for d in dirs:
            x, y = i + d[0], j + d[1]
            if 0 <= x < M and 0 <= y < N and grid[x][y]:
                self.dfs(grid, x, y)
```

### 2-14) Binary Tree Paths
```python
# LC 257. Binary Tree Paths
# V0 
# IDEA : DFS 
class Solution:
    # @param {TreeNode} root
    # @return {string[]}
    def binaryTreePaths(self, root):
        res, path_list = [], []
        self.dfs(root, path_list, res)
        return res

    def dfs(self, root, path_list, res):
        if not root:
            return
        path_list.append(str(root.val))
        if not root.left and not root.right:
            res.append('->'.join(path_list))
        if root.left:
            self.dfs(root.left, path_list, res)
        if root.right:
            self.dfs(root.right, path_list, res)
        path_list.pop()
```

### 2-15) Lowest Common Ancestor of a Binary Tree
```python
# LC 236 Lowest Common Ancestor of a Binary Tree
# V0
# IDEA : RECURSION + POST ORDER TRANSVERSAL
class Solution(object):
    def lowestCommonAncestor(self, root, p, q):

        ### NOTE here
        # if not root or find p in tree or find q in tree
        # -> then we quit the recursion and return root

        ### NOTE : we compare `p == root` and  `q == root`
        if not root or p == root or q == root:
            return root
        ### NOTE here
        #  -> not root.left, root.right, BUT left, right
        left = self.lowestCommonAncestor(root.left, p, q)
        right = self.lowestCommonAncestor(root.right, p, q)

        ### NOTE here
        # find q and p on the same time -> LCA is the current node (root)
        # if left and right -> p, q MUST in left, right sub tree respectively

        ### NOTE : if left and right, means this root is OK for next recursive
        if left and right:
            return root
        ### NOTE here
        # if p, q both in left sub tree or both in right sub tree
        return left if left else right
```

### 2-16) Path Sum
```python
# LC 112 Path Sum
# V0
# IDEA : DFS 
class Solution(object):
    def hasPathSum(self, root, sum):
        if not root:
            return False
        if not root.left and not root.right:
            return True if sum == root.val else False
        else:
            return self.hasPathSum(root.left, sum-root.val) or self.hasPathSum(root.right, sum-root.val)
```

### 2-17) Path Sum II
```python
# LC 113 Path Sum II
# V0
# IDEA : DFS
class Solution(object):
    def pathSum(self, root, sum):
        if not root: return []
        res = []
        self.dfs(root, sum, res, [root.val])
        return res

    def dfs(self, root, target, res, path):
        if not root: return
        if sum(path) == target and not root.left and not root.right:
            res.append(path)
            return
        if root.left:
            self.dfs(root.left, target, res, path + [root.left.val])
        if root.right:
            self.dfs(root.right, target, res, path + [root.right.val])
```

```java
// java
// LC 113
// V0
// IDEA : DFS + backtracking
// NOTE !!! we have res attr, so can use this.res collect result
private List<List<Integer>> res = new ArrayList<>();

public List<List<Integer>> pathSum(TreeNode root, int targetSum) {

    if (root == null){
        return this.res;
    }

    List<Integer> cur = new ArrayList<>();
    getPath(root, cur, targetSum);
    return this.res;
}

 private void getPath(TreeNode root, List<Integer> cur, int targetSum){

    // return directly if root is null (not possible to go further, so just quit directly)
    if (root == null){
        return;
    }

    // NOTE !!! we add val to cache here instead of while calling method recursively ( e.g. getPath(root.left, cur, targetSum - root.val))
    //          -> so we just need to backtrack (cancel last operation) once (e.g. cur.remove(cur.size() - 1);)
    //          -> please check V0' for example with backtrack in recursively step
    cur.add(root.val);

    if (root.left == null && root.right == null && targetSum == root.val){
        this.res.add(new ArrayList<>(cur));
    }else{
        // NOTE !!! we update targetSum here (e.g. targetSum - root.val)
        getPath(root.left, cur, targetSum - root.val);
        getPath(root.right, cur, targetSum - root.val);
    }

     // NOTE !!! we do backtrack here (cancel previous adding to cur)
     cur.remove(cur.size() - 1);
}
```

### 2-7) Clone graph
```python
# 133 Clone graph
# note : there is also a BFS solution
# V0
# IDEA : DFS
# NOTE :
#  -> 1) we init node via : node_copy = Node(node.val, [])
#  -> 2) we copy graph via dict
class Solution(object):
    def cloneGraph(self, node):
        """
        :type node: Node
        :rtype: Node
        """
        node_copy = self.dfs(node, dict())
        return node_copy
    
    def dfs(self, node, hashd):
        if not node: return None
        if node in hashd: return hashd[node]
        node_copy = Node(node.val, [])
        hashd[node] = node_copy
        for n in node.neighbors:
            n_copy = self.dfs(n, hashd)
            if n_copy:
                node_copy.neighbors.append(n_copy)
        return node_copy
```

### 2-8) Sentence Similarity II
```python
# LC 737. Sentence Similarity II
# NOTE : there is also union-find solution
# V0
# IDEA : DFS
from collections import defaultdict
class Solution(object):
    def areSentencesSimilarTwo(self, sentence1, sentence2, similarPairs):
        # helper func
        def dfs(w1, w2, visited):
            for j in d[w2]:
                if w1 == w2:
                    return True
                elif j not in visited:
                    visited.add(j)
                    if dfs(w1, j, visited):
                        return True
            return False
        
        # edge case
        if len(sentence1) != len(sentence2):
            return False
      
        d = defaultdict(list)
        for a, b in similarPairs:
            d[a].append(b)
            d[b].append(a)
            
        for i in range(len(sentence1)):
            visited =  set([sentence2[i]])
            if sentence1[i] != sentence2[i] and not dfs(sentence1[i],  sentence2[i], visited):
                return False
        return True
```

### 2-9) Concatenated Words
```python
# LC 472. Concatenated Words
# V1
# http://bookshadow.com/weblog/2016/12/18/leetcode-concatenated-words/
# IDEA : DFS 
class Solution(object):
    def findAllConcatenatedWordsInADict(self, words):
        """
        :type words: List[str]
        :rtype: List[str]
        """
        ans = []
        self.wordSet = set(words)
        for word in words:
            self.wordSet.remove(word) # avoid the search process find itself (word) when search all word in words  
            if self.search(word):
                ans.append(word)
            self.wordSet.add(word)    # add the word back for next search with new "word"
        return ans

    def search(self, word):
        if word in self.wordSet:
            return True
        for idx in range(1, len(word)):
            if word[:idx] in self.wordSet and self.search(word[idx:]):
                return True
        return False
```

### 2-10) Maximum Product of Splitted Binary Tree
```python
# LC 1339. Maximum Product of Splitted Binary Tree
# V0
# IDEA : DFS
class Solution(object):
    def maxProduct(self, root):
        all_sums = []

        def tree_sum(subroot):
            if subroot is None: return 0
            left_sum = tree_sum(subroot.left)
            right_sum = tree_sum(subroot.right)
            total_sum = left_sum + right_sum + subroot.val
            all_sums.append(total_sum)
            return total_sum

        total = tree_sum(root)
        best = 0
        for s in all_sums:
            best = max(best, s * (total - s))   
        return best % (10 ** 9 + 7)
```

### 2-11) Serialize and Deserialize Binary Tree
```python
# LC 297. Serialize and Deserialize Binary Tree
# V0
# IDRA : DFS
class Codec:

    def serialize(self, root):
        """ Encodes a tree to a single string.
        :type root: TreeNode
        :rtype: str
        """
        def rserialize(root, string):
            """ a recursive helper function for the serialize() function."""
            # check base case
            if root is None:
                string += 'None,'
            else:
                string += str(root.val) + ','
                string = rserialize(root.left, string)
                string = rserialize(root.right, string)
            return string
        
        return rserialize(root, '')    

    def deserialize(self, data):
        """Decodes your encoded data to tree.
        :type data: str
        :rtype: TreeNode
        """
        def rdeserialize(l):
            """ a recursive helper function for deserialization."""
            if l[0] == 'None':
                l.pop(0)
                return None
                
            root = TreeNode(l[0])
            l.pop(0)
            root.left = rdeserialize(l)
            root.right = rdeserialize(l)
            return root

        data_list = data.split(',')
        root = rdeserialize(data_list)
        return root

# V1
# IDEA : same as LC 297
# https://leetcode.com/problems/serialize-and-deserialize-bst/discuss/93283/Python-solution-using-BST-property
class Codec:

    def serialize(self, root):
        vals = []
        self._preorder(root, vals)
        return ','.join(vals)
        
    def _preorder(self, node, vals):
        if node:
            vals.append(str(node.val))
            self._preorder(node.left, vals)
            self._preorder(node.right, vals)
        
    def deserialize(self, data):
        vals = collections.deque(map(int, data.split(','))) if data else []
        return self._build(vals, -float('inf'), float('inf'))

    def _build(self, vals, minVal, maxVal):
        if vals and minVal < vals[0] < maxVal:
            val = vals.popleft()
            root = TreeNode(val)
            root.left = self._build(vals, minVal, val)
            root.right = self._build(vals, val, maxVal)
            return root
        else:
            return None
```

### 2-12) Serialize and Deserialize BST
```python
# LC 449. Serialize and Deserialize BST
# V0
# IDEA : BFS + queue op
class Codec:
    def serialize(self, root):
        if not root:
            return '{}'

        res = [root.val]
        q = [root]

        while q:
            new_q = []
            for i in range(len(q)):
                tmp = q.pop(0)
                if tmp.left:
                    q.append(tmp.left)
                    res.extend( [tmp.left.val] )
                else:
                    res.append('#')
                if tmp.right:
                    q.append(tmp.right)
                    res.extend( [tmp.right.val] )
                else:
                    res.append('#')

        while res and res[-1] == '#':
                    res.pop()

        return '{' + ','.join(map(str, res)) + '}' 


    def deserialize(self, data):
        if data == '{}':
            return

        nodes = [ TreeNode(x) for x in data[1:-1].split(",") ]
        root = nodes.pop(0)
        p = [root]
        while p:
            new_p = []
            for n in p:
                if nodes:
                    left_node = nodes.pop(0)
                    if left_node.val != '#':
                        n.left = left_node
                        new_p.append(n.left)
                    else:
                        n.left = None
                if nodes:
                    right_node = nodes.pop(0)
                    if right_node.val != '#':
                        n.right = right_node
                        new_p.append(n.right)
                    else:
                        n.right = None
            p = new_p 
             
        return root

# V1
# IDEA : same as LC 297
# https://leetcode.com/problems/serialize-and-deserialize-bst/discuss/93283/Python-solution-using-BST-property
class Codec:

    def serialize(self, root):
        vals = []
        self._preorder(root, vals)
        return ','.join(vals)
        
    def _preorder(self, node, vals):
        if node:
            vals.append(str(node.val))
            self._preorder(node.left, vals)
            self._preorder(node.right, vals)
        
    def deserialize(self, data):
        vals = collections.deque(map(int, data.split(','))) if data else []
        return self._build(vals, -float('inf'), float('inf'))

    def _build(self, vals, minVal, maxVal):
        if vals and minVal < vals[0] < maxVal:
            val = vals.popleft()
            root = TreeNode(val)
            root.left = self._build(vals, minVal, val)
            root.right = self._build(vals, val, maxVal)
            return root
        else:
            return None
```

### 2-12) Pacific Atlantic Water Flow

```java
// java
// LC 417
// V0
// IDEA : DFS (fixed by GPT)

public List<List<Integer>> pacificAtlantic(int[][] heights) {

    if (heights == null || heights.length == 0 || heights[0].length == 0) {
        return new ArrayList<>();
    }

    int l = heights.length;
    int w = heights[0].length;

    /**
     *
     * The pacificReachable and atlanticReachable arrays are used to keep track
     * of which cells in the matrix can reach the Pacific and Atlantic oceans, respectively.
     *
     *
     * - pacificReachable[i][j] will be true if water
     *   can flow from cell (i, j) to the Pacific Ocean.
     *   The Pacific Ocean is on the top and left edges of the matrix.
     *
     * - atlanticReachable[i][j] will be true if water
     *   can flow from cell (i, j) to the Atlantic Ocean.
     *   The Atlantic Ocean is on the bottom and right edges of the matrix.
     *
     *
     * NOTE !!!!
     *
     * The pacificReachable and atlanticReachable arrays serve a dual purpose:
     *
     * Tracking Reachability: They track whether each cell can reach the respective ocean.
     *
     * Tracking Visited Cells: They also help in tracking whether a cell has already
     *                         been visited during the depth-first search (DFS)
     *                         to prevent redundant work and infinite loops.
     *
     *
     *   NOTE !!!
     *
     *    we use `boolean[][]` to track if a cell is reachable
     */
    boolean[][] pacificReachable = new boolean[l][w];
    boolean[][] atlanticReachable = new boolean[l][w];

    // check on x-axis
    /**
     *  NOTE !!!
     *
     *   we loop EVERY `cell` at x-axis  ( (x_1, 0), (x_2, 0), .... (x_1, l - 1), (x_2, l - 1) ... )
     *
     */
    for (int x = 0; x < w; x++) {
        dfs(heights, pacificReachable, 0, x);
        dfs(heights, atlanticReachable, l - 1, x);
    }

    // check on y-axis
    /**
     *  NOTE !!!
     *
     *   we loop EVERY `cell` at y-axis  (  (0, y_1), (0, y_2), .... (w-1, y_1), (w-1, y_2), ... )
     *
     */
    for (int y = 0; y < l; y++) {
        dfs(heights, pacificReachable, y, 0);
        dfs(heights, atlanticReachable, y, w - 1);
    }

    List<List<Integer>> commonCells = new ArrayList<>();
    for (int i = 0; i < l; i++) {
        for (int j = 0; j < w; j++) {
            if (pacificReachable[i][j] && atlanticReachable[i][j]) {
                commonCells.add(Arrays.asList(i, j));
            }
        }
    }
    return commonCells;
}

/**
 *  NOTE !!!
 *
 *   this dfs func return NOTHING,
 *   e.g. it updates the matrix value `in place`
 *
 *   example:  we pass `pacificReachable` as param to dfs,
 *             it modifies values in pacificReachable in place,
 *             but NOT return pacificReachable as response
 */
private void dfs(int[][] heights, boolean[][] reachable, int y, int x) {

    int l = heights.length;
    int w = heights[0].length;

    reachable[y][x] = true;

    int[][] directions = new int[][]{ {0, 1}, {1, 0}, {-1, 0}, {0, -1} };
    for (int[] dir : directions) {
        int newY = y + dir[0];
        int newX = x + dir[1];

        /**
         *  NOTE !!!  only meet below conditions, then do recursion call
         *
         *  1. newX, newY still in range
         *  2. newX, newY is still not reachable (!reachable[newY][newX])
         *  3. heights[newY][newX] >= heights[y][x]
         *
         *
         *  NOTE !!!
         *
         *  The condition !reachable[newY][newX] in the dfs function
         *  ensures that each cell is only processed once
         *
         *  1. Avoid Infinite Loops
         *  2. Efficiency
         *  3. Correctness
         *
         *
         *  NOTE !!! "inverse" comparison
         *
         *  we use the "inverse" comparison, e.g.  heights[newY][newX] >= heights[y][x]
         *  so we start from "cur point" (heights[y][x]), and compare with "next point" (heights[newY][newX])
         *  if "next point" is "higher" than "cur point"  (e.g. heights[newY][newX] >= heights[y][x])
         *  -> then means water at "next point" can flow to "cur point"
         *  -> then we keep track back to next point of then "next point"
         *  -> repeat ...
         */
        if (newY >= 0 && newY < l && newX >= 0 && newX < w && !reachable[newY][newX] && heights[newY][newX] >= heights[y][x]) {
            dfs(heights, reachable, newY, newX);
        }
    }
} 
```

### 2-12) Minesweeper

```java
// java
// LC 529

// (there is also BFS solution)

// V1
// IDEA: DFS + ARRAY OP (GPT)
public char[][] updateBoard_1(char[][] board, int[] click) {
    int rows = board.length;
    int cols = board[0].length;

    int x = click[0], y = click[1];

    // Edge case: 1x1 grid
    if (rows == 1 && cols == 1) {
        if (board[0][0] == 'M') {
            board[0][0] = 'X';
        } else {
            board[0][0] = 'B'; // Fix: properly set 'B' if it's 'E'
        }
        return board;
    }

    // If a mine is clicked, mark as 'X'
    if (board[x][y] == 'M') {
        board[x][y] = 'X';
        return board;
    }

    // Otherwise, reveal cells recursively
    reveal_1(board, x, y);
    return board;
}

private void reveal_1(char[][] board, int x, int y) {
    int rows = board.length;
    int cols = board[0].length;

// Boundary check and already revealed check
/** NOTE !!!
 *
 *  - 1) 'E' represents an unrevealed empty square,
 *
 *  - 2) board[x][y] != 'E'
 *      -> ensures that we only process unrevealed empty cells ('E')
 *         and avoid unnecessary recursion.
 *
 *   - 3) board[x][y] != 'E'
 *   •  Avoids re-processing non-'E' cells
 *   •  The board can have:
 *      •   'M' → Mine (already handled separately)
 *      •   'X' → Clicked mine (game over case)
 *      •   'B' → Blank (already processed)
 *      •   '1' to '8' → Number (already processed)
 *  •   If a cell is not 'E', it means:
 *      •   It has already been processed
 *      •   It does not need further expansion
 *  •   This prevents infinite loops and redundant checks.
 *
 *
 *  - 4) example:
 *
 *     input:
 *          E E E
 *          E M E
 *          E E E
 *
 *   Click at (0,0)
 *      1.  We call reveal(board, 0, 0), which:
 *          •   Counts 1 mine nearby → Updates board[0][0] = '1'
 *          •   Does NOT recurse further, avoiding unnecessary work.
 *
 *      What If We Didn't Check board[x][y] != 'E'?
 *          •   It might try to expand into already processed cells, leading to redundant computations or infinite recursion.
 *
 */
if (x < 0 || x >= rows || y < 0 || y >= cols || board[x][y] != 'E') {
        return;
    }

    // Directions for 8 neighbors
    int[][] directions = {
            { -1, -1 }, { -1, 0 }, { -1, 1 },
            { 0, -1 }, { 0, 1 },
            { 1, -1 }, { 1, 0 }, { 1, 1 }
    };

    // Count adjacent mines
    int mineCount = 0;
    for (int[] dir : directions) {
        int newX = x + dir[0];
        int newY = y + dir[1];
        if (newX >= 0 && newX < rows && newY >= 0 && newY < cols && board[newX][newY] == 'M') {
            mineCount++;
        }
    }

    // If there are adjacent mines, show count
    if (mineCount > 0) {
        board[x][y] = (char) ('0' + mineCount);
    } else {
        // Otherwise, reveal this cell and recurse on neighbors
        board[x][y] = 'B';
        for (int[] dir : directions) {
            reveal_1(board, x + dir[0], y + dir[1]);
        }
    }
}
```

### 2-13) K-th Largest Perfect Subtree Size in Binary Tree

```java
// java
// LC 3319

// V0-1
// IDEA: DFS (fixed by gpt)
//  Time Complexity: O(N log N)
//  Space Complexity: O(N)
/**
*  Objective recap:
*
*   We want to:
*    •   Find all perfect binary subtrees in the given tree.
*    •   A perfect binary tree is one where:
*        •   Every node has 0 or 2 children (i.e., full),
*        •   All leaf nodes are at the `same depth`.
*    •   Return the k-th largest size among these perfect subtrees.
*    •   If there are fewer than k perfect subtrees, return -1.
*
*/
// This is a class-level list that stores the sizes of all perfect subtrees we discover during traversal.
List<Integer> perfectSizes = new ArrayList<>();

public int kthLargestPerfectSubtree_0_1(TreeNode root, int k) {
    dfs(root);
    if (perfectSizes.size() < k)
        return -1;

    Collections.sort(perfectSizes, Collections.reverseOrder());
    return perfectSizes.get(k - 1);
}

// Helper class to store information about each subtree
/**
*
* It returns a helper object SubtreeInfo, which contains:
*    •   height: depth of the subtree rooted at node.
*    •   size: number of nodes in the subtree.
*    •   isPerfect: boolean indicating whether this subtree is perfect.
*
*/
private static class SubtreeInfo {
    int height;
    int size;
    boolean isPerfect;

    SubtreeInfo(int height, int size, boolean isPerfect) {
        this.height = height;
        this.size = size;
        this.isPerfect = isPerfect;
    }
}

/**
* Inside dfs():
*    1.  Base case:
*        •   If node == null, we return a SubtreeInfo with height 0, size 0, and isPerfect = true.
*    2.  Recurse on left and right children.
*    3.  Check if the subtree rooted at this node is perfect:
*
*/
private SubtreeInfo dfs(TreeNode node) {
    if (node == null) {
        return new SubtreeInfo(0, 0, true);
    }

    SubtreeInfo left = dfs(node.left);
    SubtreeInfo right = dfs(node.right);

/**  NOTE !!!  below logic:
 *
 * This ensures:
 *  •   Both left and right subtrees are perfect.
 *  •   Their `heights` are the same → leaves are at the `same level`.
 */
boolean isPerfect = left.isPerfect && right.isPerfect
        && (left.height == right.height);


    int size = left.size + right.size + 1;
    int height = Math.max(left.height, right.height) + 1;

    /**
     *  NOTE !!!
     *
     *  If the current subtree is perfect, we record its size:
     *
     */
    if (isPerfect) {
        perfectSizes.add(size);
    }

    return new SubtreeInfo(height, size, isPerfect);
}
```