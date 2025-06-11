# Tree

> Tree data structure and algorithm/LC relative to it

- [Neetcode - type of Tree](https://www.linkedin.com/posts/neetcodeio_must-know-tree-structures-in-coding-interviews-activity-7301790861690892288-_0ni?utm_source=social_share_send&utm_medium=member_desktop_web&rcm=ACoAAA6fzw4BpOSBO1YeSrJwPZ-dNBhjC3jXTDE)

## 0) Concept  

- Tree in Array representation
- https://www.prepbytes.com/blog/tree/array-representation-of-binary-tree/
- https://www.geeksforgeeks.org/binary-tree-array-implementation/

```

# Tree

      1
     / \
    2   3
   / \  
  4   5


# Array

# We can represent Tree in Array below :

[1,2,3,4,5]

# so, in tree, node with val 1 is idx 0, node with val is 2... and node with val 5 is idx 4
# we can transform between tree and array
```

### 0-1) Types
- Tree
- [binary tree](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/binary_tree.md)
    - complete binary tree
    - perfect binary tree
- [BST (binary search tree)](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/bst.md)
- [Heap](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/heap.md)
- [Trie (dictionary tree)](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/trie.md)


### 0-2) Pattern

- Gat max, min element in path
    - LC 1448 : ONLY need to maintain cur max val in path, and compare cur node with it
- Check sub tree (children)
    - use post traverse
    - LC 101
        - https://www.bilibili.com/video/BV1ue4y1Y7Mf/?spm_id_from=333.999.0.0&vd_source=28459ac8543f6a81e3a8c993dc73b54e
- Get tree height, depth
    - https://www.bilibili.com/video/BV1Gd4y1V75u/?share_source=copy_web&vd_source=771d0eba9b524b4f63f92e37bde71301
    - https://www.bilibili.com/video/BV1Ug411S7my/?share_source=copy_web

    - height
        - distance from any node to lowest node (start from 0 or 1 is OK)
        - via Post-order traverse (後序遍歷)
        - 後序遍歷求 高度 (`子節點 到 bottom 距離`) (`node -> bottom`)
    - depth
        - distance from any node to root (start from 0 or 1 is OK)
        - via Pre-order traverse (前序遍歷)
        - LC 104
        - 前序遍歷求 深度 (`root 到 子節點 距離`) (`root -> node`)
    - [tree : height vs depth](https://stackoverflow.com/questions/2603692/what-is-the-difference-between-depth-and-height-in-a-tree)

- <img src ="https://github.com/yennanliu/CS_basics/blob/master/doc/pic/tree_depth_vs_height.jpeg" ></p>

- Ordering (pre-order or in-order ..)
    - If there is NO "root op logic" (e.g. 沒有中節點處理邏輯)
        -> then it can be any order
        -> e.g. pre-order or in-order or post-order is fine
        - https://www.bilibili.com/video/BV19t4y1L7CR/?share_source=copy_web
        - https://www.bilibili.com/video/BV1424y1Z7pn/?share_source=copy_web

## 1) General form

- When `tree` -> think about *recursion*
- there are also `while loop` approaches, but depends

### 1-1) Basic OP

#### 1-1-1) Traverse
- How to traverse the tree
    - 2 general strategies to traverse a tree:
        - Depth First Search (DFS)
            - In this strategy, we adopt the depth as the priority, so that one would start from a root and reach all the way down to certain leaf, and then back to root to reach another branch.
            - The DFS strategy can further be distinguished as `preorder, inorder, and postorder` depending on the relative order among the root node, left node and right node.
        - Breadth First Search (BFS)
            - We scan through the tree level by level, following the order of height, from top to bottom. The nodes on higher level would be visited before the ones with lower levels.
        - NOTE:
            - ONLY DFS has `preorder, inorder, and postorder` mode
            - BFS only has `preorder` mode
    - Ref
        - https://leetcode.com/problems/convert-binary-search-tree-to-sorted-doubly-linked-list/solutions/263241/convert-binary-search-tree-to-sorted-doubly-linked-list/

- <img src ="https://github.com/yennanliu/CS_basics/blob/master/doc/pic/tree_traverse.png" ></p>

- Tree traverse
    - https://www.bilibili.com/video/BV1Hy4y1t7ij/?share_source=copy_web&vd_source=771d0eba9b524b4f63f92e37bde71301
- Tree traverse ordering != Tree operation ordering (for iterative approach)
    - https://www.bilibili.com/video/BV1Zf4y1a77g/?share_source=copy_web&vd_source=771d0eba9b524b4f63f92e37bde71301
    - example : Inorder traverse VS Inorder operation

- pre-order traverse
    - root -> left -> right
    - use in "need root val in sub-tree calculation"
        - quick sort

```python
# pre-order traversal
r = []
def pre_order_traverse(TreeNode):
    r.append(root.value)
    if root.left:
        pre_order_traverse(root.left)
    if root.right:
        pre_order_traverse(root.right)
```
- in-order traverse
    - left -> root -> right
```python
# LC 094
# in-order traversel
# LC 94
# https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Tree/binary-tree-inorder-traversal.py
# V0
# IDEA : recursion
# https://leetcode.com/problems/binary-tree-inorder-traversal/solutions/332283/python-recursive-and-iterative-solutions/

class Solution(object):
    # left -> root -> right
    def inorderTraversal(self, root):
        # help func
        def dfs(root, res):
            if not root:
                return
            if root.left:
                dfs(root.left)
            res.append(root.val)
            if root.right:
                dfs(root.right)
        res = []
        dfs(root)
        return root

# V0'
# IDEA : ITERATION
class Solution:
    def inorderTraversal(self, root):
        stack = []
        res = []
        
        while True:
            # NOTE !!! : we GO THROUGH left sub tree to the end first, and form our stack on the same time, then do in-order transversal
            """
            Why the Left Traversal is Necessary ?

            To process nodes in in-order (left -> root -> right), you need to traverse the left subtree of every node before processing the node itself. This is a characteristic of in-order traversal—without traversing left first, you can't get the correct order.

            In summary, the `hile root: stack.append(root); root = root.left part is critical for simulating the recursive left-first behavior in an iterative manner. There’s no way to completely skip this step if you want an in-order traversal (using an iterative approach), but alternatives like Morris traversal exist to avoid using a stack and still traverse efficiently.

            """
            while root:
                stack.append(root)
                root = root.left
            
            if len(stack) == 0:
                # NOTE here
                break
            
            root = stack.pop()
            res.append(root.val)
            root = root.right
        return res
```
- post-order traverse
    - left -> right -> root
    - use in "need sub-tree val in root calculation"
        - divide and conquer algorithm
        - DP (dynamic programming)

```python
# postorder traversal
r = []
def post_order_traverse(TreeNode):
    if root.left:
        post_order_traverse(root.left)
    if root.right:
        post_order_traverse(root.right)
    r.append(root.value)
```

- layer traverse (BST)
    - layer by layer

```python
# python
# Init a tree in py
class TreeNode(object):
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right

root = TreeNode(0)
root.left = TreeNode(1)
root.right =  TreeNode(2)
print (root)
```

```python
# python
def traverse(TreeNode):
    for child in root.children:
        # op in pre-traverse
        traverse(child)
        # op in post-traverse
```

```java
// java
// layer traverse (BST)
// algorithm book (labu) p. 262
void traverse(TreeNode root){
    if (root == null) return;

    // init queue, add root into queue
    Queue<TreeNode> q = new LinkedList<>();
    q.offer(root);

    while (! q.isEmpty()){
        TreeNode cur = q.poll();
        
        /********* layer traverse *********/
        System.out.println(root.val);
        /**********************************/

        if (cur.left != null){
            q.offer(cur.left);
        }

        if (cur.right != null){
            q.offer(cur.right);
        }
    }
}
```

```python
# init a tree with root value
class TreeNode(object):
    def __init__(self, x):
        self.val = x
        self.left = None
        self.right = None

s = 3
root = TreeNode(int(s))
```

### 1-1-1-2) Tree `Right Side` View

```java
// java

// LC 199
List<Integer> res = new ArrayList<>();
Queue<TreeNode> q = new LinkedList<>();
while (!q.isEmpty()) {
    TreeNode rightSide = null;
    int qLen = q.size();

    /**
     *  NOTE !!!
     *
     *   1) via for loop, we can get `most right node` (since the order is root -> left -> right)
     *   2) via `TreeNode rightSide = null;`, we can get the `most right node` object
     *      - rightSide could be `right sub tree` or `left sub tree`
     *
     *      e.g.
     *         1
     *       2   3
     *       
     *       
     *       1
     *     2   3
     *   4
     *
     */
    for (int i = 0; i < qLen; i++) {
        TreeNode node = q.poll();
        if (node != null) {
            rightSide = node;
            q.offer(node.left);
            q.offer(node.right);
        }
    }
    if (rightSide != null) {
        res.add(rightSide.val);
    }
}
```

#### 1-1-2) Get node counts
```java
// get nodes count of binary tree

// get nodes count of perfect tree

// get nodes count of complete tree
// LC 222

// dfs
class Solution {
    public int countNodes(TreeNode root) {
        if (root == null) {
            return 0;
        }

        // Recursively count the nodes in the left subtree
        int leftCount = countNodes(root.left);

        // Recursively count the nodes in the right subtree
        int rightCount = countNodes(root.right);

        // Return the total count (current node + left subtree + right subtree)
        return 1 + leftCount + rightCount;
    }
}


// bfs
public int countNodes_2(TreeNode root) {

    if (root == null){
        return 0;
    }
    List<TreeNode> collected = new ArrayList<>();
    Queue<TreeNode> q = new LinkedList<>();
    q.add(root);
    while (!q.isEmpty()){
        TreeNode cur = q.poll();
        collected.add(cur);
        if (cur.left != null) {
            q.add(cur.left);
        }
        if (cur.right != null) {
            q.add(cur.right);
        }
    }

    //return this.count;
    System.out.println("collected = " + collected.toString());
    return collected.size();
}
```

#### 1-1-3 -1) Get Maximum depth

- LC 104 : Maximum Depth of Binary Tree
- LC 110 : Balanced Binary Tree

```java
// java
// V0
// IDEA : RECURSIVE (DFS)
public int maxDepth(TreeNode root) {

    if (root == null){
        return 0;
    }

    // NOTE : below conditon is optional (have or not use is OK)
//        if (root.left == null && root.right == null){
//            return 1;
//        }

    int leftD = maxDepth(root.left) + 1;
    int rightD = maxDepth(root.right) + 1;

    return Math.max(leftD, rightD);
}

// V1
public int getDepthDFS(TreeNode root) {
    if (root == null) {
        return 0;
    }

  return Math.max(getDepthDFS(root.left), getDepthDFS(root.right)) + 1;
}  
```

```python
#-----------------
# BFS
#-----------------
# ....
layer = 1
q = [[layer, root]]
res = []
while q:
    # NOTE !!! FIFO, so we pop first added element (new element added at right hand side)
    layer, tmp = root.pop(0)
    """
    KEY here !!!!
    """
    if tmp and not tmp.left and not tmp.right:
        res.append(layer)
    if tmp.left:
        q.append([layer+1, tmp.left])
    if tmp.right:
        q.append([layer+1, tmp.right])
    # ...
```

#### 1-1-3 -2) Get Minimum depth
- LC 111 : Minimum Depth of Binary Tree 

```java
// java

// V0'
// IDEA : DFS
public int minDepth(TreeNode root) {

    if (root == null){
        return  0;
    }

    return getDepth(root);
}

private int getDepth(TreeNode root){

    if (root == null){
        return 0;
    }

    /**
     *  NOTE !!! below condition
     *  -> we need to go till meat a node, then calculate min depths (number of node)
     *  -> Note: A leaf is a node with no children.
     *  -> plz check below example for idea
     *  example : [2,null,3,null,4,null,5,null,6]
     *
     *
     */
    if (root.left == null) {
        return 1 + getDepth(root.right);
    } else if (root.right == null) {
        return 1 + getDepth(root.left);
    }

    return 1 + Math.min(getDepth(root.left), getDepth(root.right));
}
```

#### 1-1-3 -2) Get Max path

- LC 124

```java

int maxPathSum = 0;
// ...

public int getMaxPathHelper(TreeNode node){
    if(node == null){
        return 0;
    }
    int leftMaxDepth = Math.max(getMaxPathHelper(root.left), 0);
    int rightMaxDepth = Math.max(getMaxPathHelper(root.right), 0);

    maxPathSum = Math.max(maxPathSum, root.val + leftMaxDepth + rightMaxDepth);

    return root.val + Math.max(leftMaxDepth, rightMaxDepth);
}

//... 
```


#### 1-1-4) Get LCA (Lowest Common Ancestor) of a tree
```python
# LC 236 Lowest Common Ancestor of a Binary Tree
# LC 235 Lowest Common Ancestor of a Binary Search Tree
# LC 1650 Lowest Common Ancestor of a Binary Tree III
# V0
# IDEA : RECURSION + POST ORDER TRANSVERSAL
### NOTE : we need POST ORDER TRANSVERSAL for this problem
#          -> left -> right -> root
#          -> we can make sure that if p == q, then the root must be p and q's "common ancestor"
class Solution(object):
    def lowestCommonAncestor(self, root, p, q):
        ### NOTE here
        # if not root or find p in tree or find q in tree
        # -> then we quit the recursion and return root
        if not root or p == root or q == root:
            return root
        ### NOTE here
        #  -> not root.left, root.right, BUT left, right
        left = self.lowestCommonAncestor(root.left, p, q)
        right = self.lowestCommonAncestor(root.right, p, q)
        ### NOTE here
        # find q and p on the same time -> LCA is the current node (root)
        # if left and right -> p, q MUST in left, right sub tree respectively
        if left and right:
            return root
        ### NOTE here
        # if p, q both in left sub tree or both in right sub tree
        return left if left else right
```

```java
// java
// algorithm book p. 271
TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q){
    // base case
    if (root == null) return null;
    if (root == p || root == q) return root;
    TreeNode left = lowestCommonAncestor(root.left, p, q);
    TreeNode right = lowestCommonAncestor(root.right, p, q);
    // case 1
    if (left != null && right != null){
        return root;
    }
    // case 2
    if (left == null && right == null){
        return null;
    }
    // case 3
    return left == null ? right: left;
}
```

#### 1-1-5) Merge Two Binary Trees
```python
# LC 617 Merge Two Binary Trees
# NOTE !!! there is also BFS solution
# V0
# IDEA : DFS + BACKTRACK
class Solution:
    def mergeTrees(self, t1, t2):
        return self.dfs(t1,t2)

    def dfs(self, t1, t2):
        if not t1 and not t2:
            return
        
        if t1 and t2:
            ### NOTE here
            newT = TreeNode(t1.val +  t2.val)
            newT.right = self.mergeTrees(t1.right, t2.right)
            newT.left = self.mergeTrees(t1.left, t2.left)   
            return newT
        
        ### NOTE here
        else:
            return t1 or t2
```

```java
// java
// V0
// IDEA : RECURSIVE
public TreeNode mergeTrees(TreeNode t1, TreeNode t2) {

    if (t1 == null && t2 == null){
        return null;
    }

    if (t1 != null && t2 != null){
        t1.val += t2.val;
    }

    if (t1 == null && t2 != null){
        // NOTE!!! return t2 directly here
        return t2;
    }

    if (t1 != null && t2 == null){
        // NOTE!!! return t1 directly here
        return t1;
    }

    t1.left = mergeTrees(t1.left, t2.left);
    t1.right = mergeTrees(t1.right, t2.right);

    return t1;
}
```

#### 1-1-6) Count nodes on a `basic` binary tree
```java
// java
// algorithm book (labu) p. 250
public int countNodes (TreeNode root){
    if (root == null) return 0;
    return 1 + countNodes(root.left) + countNodes(root.right);
}
```

#### 1-1-7) Count nodes on a `perfect` binary tree
```java
// java
// algorithm book (labu) p. 250
public int countNodes(TreeNode root){
    int h = 0;
    // get tree depth
    while (root != null){
        root = root.left;
        h += 1;
    }
    // total nodes = 2**n + 1
    return (int)Math.pow(2, h) - 1;
}
```

#### 1-1-8) Count nodes on a `complete` binary tree
```java
// java
// algorithm book (labu) p. 251
public int countNodes(TreeNode root){

    TreeNode l = root;
    TreeNode r = root;
    int hl = 0;
    int hr = 0;

    while (l != null){
        l = l = left;
        h1 += 1;
    }

    while (r != null){
        r = r.right;
        hr += 1;
    }

    // if left, right sub tree have SAME depth -> this is a perfect binary tree
    if (hl == hr){
        return (int)Math.pow(2, hl) - 1;
    }

    // if left, right sub tree have DIFFERENT depth, then we follow the simple bianry tree approach
    return 1 + countNodes(root.left) + countNodes(root.right);
}
```

#### 1-1-9) Serialize binary tree with pre-order traverse
```java
// java
// algorithm book (labu) p.256

String SEP = ",";
String NULL = "#";

/* main func : serialize binary tree to string */
String serialize(TreeNode root){
    StringBuilder sb = new StringBuilder();
    serialize(root, sb);
    return sb.toString();

/* help func : put binary tree to StringBuilder */
void serialize(TreeNode root, StringBuilder sb){
    if (root == null){
        sb.append(NULL).append(SEP);
        return;
    }
}

/********* pre-order traverse *********/
sb.append(root.val).append(SEP);
/**************************************/


serialize(root.left, sb);
serialize(root.right, sb);
}
``` 

#### 1-1-10) Deserialize binary tree with pre-order traverse
```java
// java
// algorithm book (labu) p.256

String SEP = ",";
String NULL = "#";

/* main func : dserialize string to binary tree */
TreeNode deserlialize(String data){
    // transform string to linkedlist
    LinkedList<String> nodes = new LinkedList<>();
    for (String s: data.split(SEP)){
        nodes.addLast(s);
    }
    return deserlialize(nodes);
}


/* **** help func : build binary tree via linkedlist (nodes) */
TreeNode deserlialize(LinkedList<String> nodes){
    if (nodes.isEmpty()) return null;

    /********* pre-order traverse *********/
    // the 1st element on left is the root node of the binary tree
    String first = nodes.removeFirst();
    if (first.equals(NULL)) return null;
    TreeNode root = new TreeNode(Integer.parseInt(first));
    /**************************************/

    root.left = deserlialize(nodes);
    root.right = deserlialize(nodes);

    return root;
}
```

#### 1-1-11) Serialize binary tree with post-order traverse
```java
// java
// algorithm book (labu) p.258

String SEP = ",";
String NULL = "#";

StringBuilder sb = new StringBuilder();

/* help func : pit binary tree to StringBuilder*/
void serialize(TreeNode root, StringBuilder sb){
    if (root == null){
        sb.append(NULL).append(SEP);
        return;
    }

    serialize(root.left, sb);
    serialize(root.right, sb);

    /********* post-order traverse *********/
    sb.append(root.val).append(SEP);
    /**************************************/
}
```

#### 1-1-12) Deserialize binary tree with post-order traverse
```java
// java
// algorithm book (labu) p.260

/* main func : deserialize string to binary tree */
TreeNode deserlialize(String data){
    LinkedList<String> nodes = new LinkedList<>();
    for (String s : data.split(SEP)){
        nodes.addLast(s);
    }
    return deserlialize(nodes);
}

/* help func : build binary tree via linkedlist */
TreeNode deserlialize(LinkedList<String> nodes){
    if (nodes.isEmpty()) return null;
    // get element from last to beginning
    String last = nodes.removeLast();

    if (last.equals(NULL)) return null;
    TreeNode root = new TreeNode(Integer.parseInt(last));
    // build right sub tree first, then left sub tree
    root.right = deserlialize(nodes);
    root.left = deserlialize(nodes);

    return root;
}
```

#### 1-1-13) Serialize binary tree with layer traverse
```java
// java
// layer traverse : https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/tree.md#1-1-basic-op
// algorithm book (labu) p.263
String SEP = ",";
String NULL = "#";

/* Serialize binary tree to string */
String serialize(TreeNode root){

    if (root == null) return "";
    StringBuilder sb = new StringBuilder();

    // init queue, put root into it
    Queue<TreeNode> q = new LinkedList<>();
    q.offer(root);

    while (!q.isEmpty()){
        TreeNode cur = q.poll();

        /***** layer traverse ******/
        if (cur == null){
            sb.append(NULL).append(SEP);
            continue;
        }
        sb.append(cur.val).append(SEP);
        /**************************/

        q.offer(cur.left);
        q.offer(cur.right);
    }
    return sb.toString();
}
```

#### 1-1-14) Deserialize binary tree with layer traverse
```java
// java
// algorithm book (labu) p.264

String SEP = ",";
String NULL = "#";

/* Deserialize binary tree to string */
TreeNode deserlialize(String data){
    
    if (data.isEmpty()) return null;

    String[] nodes = data.split(SEP);

    // root's value = 1st element's value
    TreeNode root = new TreeNode(Integer.parseInt(node[0]));

    // queue records parent node, put root into queue
    Queue<TreeNode> q = new LinkedList<>();
    q.offer(root);

    for (int i = 1; i < nodes.length){

        // queue saves parent nodes
        TreeNode parent = q.poll();
        
        // parent node's left sub node
        String left = nodes[i++];
        if (!left.equals(NULL)){
            parent.left = new TreeNode(Integer.parseInt(left));
            q.offer(parent.left);
        }else {
            parent.left = null;
        }
        // parent node's right sub node
        String right = nodes[i++];
        if (!right.equals(NULL)){
            parent.right = new TreeNode(Integer.parseInt(right));
            q.offer(parent.right);
        }else{
            parent.right = null;
        }
    }

    return root;
}
```


#### 1-1-14-1) Serialize and Deserialize Binary Tree

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

### 1-1-15) Invert Binary Tree
```python
# LC 226 Invert Binary Tree

# V0
# IDEA : DFS
# -> below code shows a good example that tree is a type of "linked list"
# -> we don't really modify tree's "value", but we modify the pointer
# -> e.g. make root.left point to root.right, make root.right point to root.left
class Solution(object):
    def invertTree(self, root):
        def dfs(root):
            if not root:
                return root
            ### NOTE THIS
            if root:
                # NOTE : have to do root.left, root.right ON THE SAME TIME
                root.left, root.right = dfs(root.right), dfs(root.left)
        dfs(root)
        return root

# V0'
# IDEA BFS
class Solution(object):
    def invertTree(self, root):
        if root == None:
            return root
        queue = [root]
        while queue:
            # queue = queue[::-1] <-- this one is NOT working
            for i in range(len(queue)):         
                tmp = queue.pop()
                ### NOTE here !!!!!!
                # -> we do invert op via below
                tmp.left, tmp.right = tmp.right, tmp.left
                if tmp.left:
                    queue.append(tmp.left)
                if tmp.right:
                    queue.append(tmp.right)
        return root
```

```java
// java
// DFS
    // V0
    // IDEA : DFS
    public TreeNode invertTree(TreeNode root) {
        if (root == null) {
            return null;
        }
        /** NOTE !!!!
         *
         *   instead of calling invertTree and assign value to sub tree directly,
         *   we need to CACHE invertTree result, and assign later
         *   -> since assign directly will cause tree changed, and affect the other invertTree call
         *
         *   e.g. below is WRONG,
         *      root.left = invertTree(root.right);
         *      root.right = invertTree(root.left);
         *
         *   need to cache result
         *
         *       TreeNode left = invertTree(root.left);
         *       TreeNode right = invertTree(root.right);
         *
         *   then assign to sub tree
         *
         *       root.left = right;
         *       root.right = left;
         */
        TreeNode left = invertTree(root.left);
        TreeNode right = invertTree(root.right);
        root.left = right;
        root.right = left;
        /** NOTE !!!! below is WRONG */
//        root.left = invertTree(root.right);
//        root.right = invertTree(root.left);
        return root;
    }
```

### 1-1-16) Get neighborhood node (for each node) from tree
```python
# LC 863. All Nodes Distance K in Binary Tree
# dfs
# IDEA : DFS
# ...
from collections import defaultdict
def build(parent,child):
    graph = defaultdict(list)
    if parent and child:
        graph[parent.val].append(child.val)
        graph[child.val].append(parent.val)
    if child.left:
        build(child,child.left)
    if child.right:
        build(child,child.right)
# ....
```

### 1-1-17) check Symmetric Tree
```python
# LC 101
class Solution(object):
    def isSymmetric(self, root):
        if not root:
            return True
        return self.mirror(root.left, root.right)

    def mirror(self, left, right):
        if not left or not right:
            return left == right
        if left.val != right.val:
            return False
        return self.mirror(left.left, right.right) and self.mirror(left.right, right.left)
```

## 2) LC Example

### 2-1) Binary Tree Right Side View
```python 
# LC 199 Binary Tree Right Side View
# V0
# IDEA : DFS
class Solution(object):
    def rightSideView(self, root):
        def dfs(root, layer):
            if not root:
                return
            if len(res) <= layer+1:
            #if len(res) == layer:     # this works as well
                res.append([])
            res[layer].append(root.val)
            if root.right:
                dfs(root.right, layer+1)
            if root.left:
                dfs(root.left, layer+1)
            
        if not root:
            return []
        res =[[]]
        dfs(root, 0)
        return [x[0] for x in res if len(x) > 0]
```

### 2-2) Construct String from Binary Tree
```python
# LC 606. Construct String from Binary Tree
# V0
class Solution:
    def tree2str(self, t):
        if not t:
            return ''
        if not t.left and not t.right:
            return str(t.val)
        ### NOTICE HERE
        if not t.left:
            return str(t.val) + '()' + '(' + self.tree2str(t.right) + ')'
        ### NOTICE HERE
        if not t.right:
            return str(t.val) + '(' + self.tree2str(t.left) + ')'
        ### NOTICE HERE
        return str(t.val) + '(' + self.tree2str(t.left) + ')' + '(' + self.tree2str(t.right) + ')'

# V0'
class Solution:
    def tree2str(self, root):
        if not root:
            return ""
        ### NOTE : this condition can make effect 
        #          as ` return str(root.val) + "(" + self.tree2str(root.left) + ")" +  "(" + self.tree2str(root.right) + ")" ` at the bottom
        if not root.left and not root.right:
            return str(root.val)
        if root.left and root.right:
            return str(root.val) + "(" + self.tree2str(root.left) + ")" + "(" + self.tree2str(root.right) + ")"
        if not root.right:
            return str(root.val) + "(" + self.tree2str(root.left) + ")"
        if not root.left:
            return str(root.val) + "()" + "(" + self.tree2str(root.right) + ")"
        #return str(root.val) + "(" + self.tree2str(root.left) + ")" +  "(" + self.tree2str(root.right) + ")"

# V0''
class Solution(object):
    def tree2str(self, t):
        if not t: return ""
        s = str(t.val)
        if t.left or t.right:
            s += "(" + self.tree2str(t.left) + ")"
        if t.right:
            s += "(" + self.tree2str(t.right) + ")"
        return s
```

### 2-3) Maximum Width of Binary Tree
```python
# LC 662 Maximum Width of Binary Tree
# V0
# IDEA : defaultdict + DFS
from collections import defaultdict
class Solution:
    def widthOfBinaryTree(self, root):
        
        def dfs(node, level, idx):
            if node:
                d[level] += [idx]
                dfs(node.left, level+1, 2*idx)
                dfs(node.right, level+1, 2*idx+1)
                
        d = defaultdict(list)
        dfs(root, 0, 0)
        return max(v[-1] - v[0] + 1 for _, v in d.items())

# V0'
# IDEA : BFS
# IDEA : GIVEN index = idx -> its left tree index = idx*2 ; its right tree index = idx*2 + 1
#        -> SO GO THROUGH ALL LAYERS IN THE TREE, CALCULATE THEIR WIDTH, AND RETRUN THE MAX WIDTH WHICH IS THE NEEDED RESPONSE
from collections import defaultdict
class Solution(object):
    def widthOfBinaryTree(self, root):
        # edge case
        if not root:
            return 0
        layer = 0
        idx = 0
        q = [[root, layer, idx]]
        res = defaultdict(list)
        while q:
            for i in range(len(q)):
                tmp, layer, idx = q.pop(0)
                res[layer].append(idx)
                if tmp.left:
                    q.append([tmp.left, layer+1, idx*2])
                if tmp.right:
                    q.append([tmp.right, layer+1, idx*2+1])
        #print ("res = " + str(res))
        _res = [max(res[x]) - min(res[x]) + 1 for x in list(res.keys()) if res[x] > 1]
        #print ("_res = " + str(_res))
        return max(_res)
```

### 2-4) Construct String from Binary Tree
```python
# LC 606 Construct String from Binary Tree
# V0
# IDEA : tree + check problem examples
#        -> if root.right and not root.left
#        -> if root.left and not root.right
class Solution(object):
    def tree2str(self, root):
        def dfs(root):
            if not root:
                ### NOTICE HERE
                return ""
            ### NOTICE HERE
            if not root.left and not root.right:
                return str(root.val)
            ### NOTICE HERE : "2()(4)" case
            if root.right and not root.left:
                return str(root.val) + '()' + '(' + dfs(root.right) + ')'
            ### NOTICE HERE
            if root.left and not root.right:
                return str(root.val) + '(' + dfs(root.left) + ')'
            ### NOTICE HERE
            return str(root.val) + '(' + dfs(root.left) + ')' + '(' + dfs(root.right) + ')'
        
        res = dfs(root)
        return res
```

### 2-5) Closest Leaf in a Binary Tree
```python
# LeetCode 742. Closest Leaf in a Binary Tree
# V0
# IDEA : DFS build GRAPH + BFS find ans
### NOTE :  closest to a leaf means the least number of edges travelled on the binary tree to reach any leaf of the tree. Also, a node is called a leaf if it has no children.
#         -> We only consider the min distance between left (no sub tree) and k
### NOTE : we need DFS create the graph
# https://www.youtube.com/watch?v=x1wXkRrpavw
# https://blog.csdn.net/qq_17550379/article/details/87778889
import collections
class Solution:
    # build graph via DFS
    # node : current node
    # parent : parent of current node
    def buildGraph(self, node, parent, k):
        if not node:
            return
        # if node.val == k, THEN GET THE start point FROM current "node",
        # then build graph based on above
        if node.val == k:
            self.start = node
        if parent:
            self.graph[node].append(parent)
            self.graph[parent].append(node)
        self.buildGraph(node.left, node, k)
        self.buildGraph(node.right, node, k)

    # search via DFS
    def findClosestLeaf(self, root, k):


        self.start = None
        ### NOTE : we need DFS create the graph
        self.buildGraph(root, None, k)
        q, visited = [root], set()
        #q, visited = [self.start], set() # need to validate this
        self.graph = collections.defaultdict(list)
        while q:
            for i in range(len(q)):
                cur = q.pop(0)
                # add cur to visited, NOT to visit this node again
                visited.add(cur)
                ### NOTICE HERE 
                # if not cur.left and not cur.right: means this is the leaf (HAS NO ANY left/right node) of the tree
                # so the first value of this is what we want, just return cur.val as answer directly
                if not cur.left and not cur.right:
                    # return the answer
                    return cur.val
                # if not find the leaf, then go through all neighbors of current node, and search again
                for node in self.graph:
                    if node not in visited: # need to check if "if node not in visited" or "if node in visited"
                        q.append(node)
```

### 2-7) Same Tree
```python
# LC 100 Same tree
# V0
# IDEA : Recursion
class Solution(object):
    def isSameTree(self, p, q):
        
        def dfs(p, q):
            ### NOTE : we need to put this as 1st condition, or will cause "not sub tree" error
            if not p and not q:
                return True
            ### NOTE : elif (but not `if`)
            elif (not p and q) or (p and not q):
                return False
            ### NOTE : elif (but not `if`)
            elif p.val != q.val:
                return False
            return dfs(p.left, q.left) and dfs(p.right, q.right)
        
        res = dfs(p, q)
        return res
```

### 2-9) Validate Binary Search Tree
```python
# 98. Validate Binary Search Tree
# V0
# IDEA : BFS
#  -> trick : we make sure current tree and all of sub tree are valid BST
#   -> not only compare tmp.val with tmp.left.val, tmp.right.val,
#   -> but we need compare if tmp.left.val is SMALLER then `previous node val`
#   -> but we need compare if tmp.right.val is BIGGER then `previous node val`
class Solution(object):
    def isValidBST(self, root):
        if not root:
            return True
        _min = -float('inf')
        _max = float('inf')
        ### NOTE : we set q like below
        q = [[root, _min, _max]]
        while q:
            for i in range(len(q)):
                tmp, _min, _max = q.pop(0)
                if tmp.left:
                    ### NOTE : below condition
                    if tmp.left.val >= tmp.val or tmp.left.val <= _min:
                        return False
                    ### NOTE : we append tmp.val as _max
                    q.append([tmp.left, _min, tmp.val])
                if tmp.right:
                    ### NOTE : below condition
                    if tmp.right.val <= tmp.val or tmp.right.val >= _max:
                        return False
                    ### NOTE : we append tmp.val as _min
                    q.append([tmp.right, tmp.val, _max])
        return True

# V0'
# IDEA: RECURSION 
class Solution(object):
    def isValidBST(self, root):
        """
        :type root: TreeNode
        :rtype: bool
        """
        return self.valid(root, float('-inf'), float('inf'))
        
    def valid(self, root, min_, max_):
        if not root: return True
        if root.val >= max_ or root.val <= min_:
            return False
        return self.valid(root.left, min_, root.val) and self.valid(root.right, root.val, max_)
```

### 2-10) Construct Binary Tree from Preorder and Inorder Traversal
```python
#  Construct Binary Tree from Preorder and Inorder Traversal
# V0
# IDEA : BST property
class Solution(object):
    def buildTree(self, preorder, inorder):
        if len(preorder) == 0:
            return None
        if len(preorder) == 1:
            return TreeNode(preorder[0])
        ### NOTE : init root like below (via TreeNode and root value (preorder[0]))
        root = TreeNode(preorder[0])
        # get the index of root.val in order to SPLIT TREE
        index = inorder.index(root.val)  # the index of root at inorder, and we can also get the length of left-sub-tree, right-sub-tree ( preorder[1:index+1]) for following using
        # recursion for root.left
        #### NOTE : preorder[1 : index + 1] (for left sub tree)
        root.left = self.buildTree(preorder[1 : index + 1], inorder[ : index]) ### since the BST is symmery so the length of left-sub-tree is same in both Preorder and Inorder, so we can use the index to get the left-sub-tree of Preorder as well
        # recursion for root.right 
        root.right = self.buildTree(preorder[index + 1 : ], inorder[index + 1 :]) ### since the BST is symmery so the length of left-sub-tree is same in both Preorder and Inorder, so we can use the index to get the right-sub-tree of Preorder as well
        return root
```

### 2-11) Construct Binary Tree from String
```python
# LC 536 Construct Binary Tree from String
# V0
# IDEA : tree property + recursive
class Solution(object):
    def str2tree(self, s):
        if not s: 
            return None
        n = ''
        while s and s[0] not in ('(', ')'):
            n += s[0]
            s = s[1:]
        ### NOTE this
        node = TreeNode(int(n))
        ### NOTE this
        left, right = self.divide(s)
        node.left = self.str2tree(left[1:-1])
        node.right = self.str2tree(right[1:-1])
        return node

    def divide(self, s):
        part, deg = '', 0
        while s:
            """
            syntax exmaple :
            In [9]: x = {'(' : 1, ')' : -1}
            In [10]: x.get('(')
            Out[10]: 1
            In [11]: x.get('(', 0 )
            Out[11]: 1
            In [12]: x.get('&', 0 )
            Out[12]: 0
            """
            deg += {'(' : 1, ')' : -1}.get(s[0], 0)
            part += s[0]
            s = s[1:]
            if deg == 0:
                break
        return part, s

# V0'
# IDEA : tree property + recursive
class TreeNode(object):
    def __init__(self, x):
        self.val = x
        self.left = None
        self.right = None

class Solution(object):
    def str2tree(self, s):
        """
        :type s: str
        :rtype: TreeNode
        """
        def str2treeHelper(s, i):
            start = i
            if s[i] == '-': i += 1
            while i < len(s) and s[i].isdigit(): 
                i += 1
            node = TreeNode(int(s[start:i]))
            if i < len(s) and s[i] == '(':
                i += 1
                node.left, i = str2treeHelper(s, i)
                i += 1
            if i < len(s) and s[i] == '(':
                i += 1
                node.right, i = str2treeHelper(s, i)
                i += 1
            return node, i

        return str2treeHelper(s, 0)[0] if s else None
```

### 2-12) Minimum Depth of Binary Tree
```python
# LC 111 Minimum Depth of Binary Tree

# V0
# IDEA : BFS
class Solution(object):
    def minDepth(self, root):
        # edge case
        if not root:
            return 0
        if root and not root.left and not root.right:
            return 1
        layer = 1
        q = [[layer, root]]
        res = []
        while q:
            for i in range(len(q)):
                layer, tmp = q.pop(0)
                """
                NOTE !!! : via below condition, we get "layer" of " A leaf is a node with no children."
                """
                if tmp and not tmp.left and not tmp.right:
                    res.append(layer)
                if tmp.left:
                    q.append([layer+1, tmp.left])
                if tmp.right:
                    q.append([layer+1, tmp.right])
        # get min
        #print ("res = " + str(res))
        return min(res)

# V0'
# IDEA : DFS
# compare with LC 104 : Maximum Depth of Binary Tree
class Solution(object):
    def minDepth(self, root):
        if not root:
            return 0
        ### NOTE here : we need min depth, so if not root.left, then we need to return directly
        if not root.left:
            return 1 + self.minDepth(root.right)
        ### NOTE here : we need min depth, so if not root.right, then we need to return directly
        elif not root.right:
            return 1 + self.minDepth(root.left)
        else:
            return 1 + min(self.minDepth(root.left), self.minDepth(root.right))
```

### 2-13) Maximum Depth of Binary Tree
```python
# LC 104 Maximum Depth of Binary Tree
# V0
# IDEA : DFS
class Solution(object):
    def maxDepth(self, root):

        if root == None:
            return 0
        return 1 + max(self.maxDepth(root.left), self.maxDepth(root.right))

# V0'
# bfs
class Solution(object):
    def maxDepth(self, root):
        # edge case
        if not root:
            return 0
        res = 0
        layer = 0
        q = [[root, layer]]
        while q:
            for i in range(len(q)):
                tmp, layer = q.pop(0)
                res = max(res, layer)
                if tmp.left:
                    q.append([tmp.left, layer+1])
                if tmp.right:
                    q.append([tmp.right, layer+1])                   
        return res + 1
```

### 2-14) All Nodes Distance K in Binary Tree
```python
# LC 863. All Nodes Distance K in Binary Tree
# V0
# IDEA : DFS + BFS
from collections import defaultdict
class Solution:
    
    def build(self,parent,child):
        if parent and child:
            self.graph[parent.val].append(child.val)
            self.graph[child.val].append(parent.val)
        if child.left:
            self.build(child,child.left)
        if child.right:
            self.build(child,child.right)
            
    def distanceK(self, root, target, K):
        self.graph=defaultdict(list)
        self.build(None,root)
        q=[(target.val,1)]
        vis=set([target.val])
        ans=[]
        while q:
            i,j=q.pop(0)
            for node in self.graph[i]:
                if node not in vis:
                    if j==K:
                        ans.append(node)
                    vis.add(node)
                    q.append((node,j+1))
        return ans if len(q) < K else [target.val]
```

### 2-15) Boundary of Binary Tree
```python
# LC 545. Boundary of Binary Tree
# V0
# IDEA : DFS
# https://xiaoguan.gitbooks.io/leetcode/content/LeetCode/545-boundary-of-binary-tree-medium.html
# https://www.cnblogs.com/lightwindy/p/9583723.html
class Solution(object):
    def boundaryOfBinaryTree(self, root):
        def leftBoundary(root, nodes):
            if not root or (not root.left and not root.right):
                return
            nodes.append(root.val)
            """
            NOTE this !!!
            """
            if not root.left:
                leftBoundary(root.right, nodes)
            else:
                leftBoundary(root.left, nodes)
 
        def rightBoundary(root, nodes):
            if not root or (not root.left and not root.right):
                return
            """
            NOTE this !!!
            """
            if not root.right:
                rightBoundary(root.left, nodes)
            else:
                rightBoundary(root.right, nodes)
            nodes.append(root.val)
 
        def leaves(root, nodes):
            if not root:
                return
            if not root.left and not root.right:
                nodes.append(root.val)
                return
            leaves(root.left, nodes)
            leaves(root.right, nodes)
 
        if not root:
            return []
 
        nodes = [root.val]
        leftBoundary(root.left, nodes)
        """
        NOTE this !!!
        """
        leaves(root.left, nodes)
        leaves(root.right, nodes)
        rightBoundary(root.right, nodes)
        return nodes

# V0'
class Solution(object):
    def boundaryOfBinaryTree(self, root):
        if not root: return []

        left_bd_nodes = [root]
        cur = root.left
        while cur:
            left_bd_nodes.append(cur)
            cur = cur.left or cur.right

        right_bd_nodes = [root]
        cur = root.right
        while cur:
            right_bd_nodes.append(cur)
            cur = cur.right or cur.left

        leaf_nodes = []
        stack = [root]
        while stack:
            node = stack.pop()
            if node.right:
                stack.append(node.right)
            if node.left:
                stack.append(node.left)
            if not node.left and not node.right:
                leaf_nodes.append(node)

        ans = []
        seen = set()
        def visit(node):
            if node not in seen:
                seen.add(node)
                ans.append(node.val)

        for node in left_bd_nodes: visit(node)
        for node in leaf_nodes: visit(node)
        for node in reversed(right_bd_nodes): visit(node)

        return ans
```

### 2-16) Binary Tree Maximum Path Sum

```java
// java
// LC 124

// V0-1
// IDEA: DFS (GPT)
private int maxSum = Integer.MIN_VALUE;

public int maxPathSum_0_1(TreeNode root) {
    if (root == null) {
        return Integer.MIN_VALUE; // Handle null case
    }

    dfs(root);
    return maxSum;
}

/** NOTE !!!
 *
 *  the response type of dfs is `integer`
 *  e.g. the `max path sum` per input node
 */
private int dfs(TreeNode node) {
    if (node == null) {
        return 0;
    }

    // Compute max path sum of left and right children, discard negative values
    /**
     *  NOTE !!!
     *
     *   we cache `leftMax` on current node
     *   we cache `rightMax` on current node
     *
     *   so we can update global `max path sum` below
     */
    int leftMax = Math.max(dfs(node.left), 0);
    int rightMax = Math.max(dfs(node.right), 0);

    // Update global max sum with current node as the highest ancestor
    /**
     *  NOTE !!!
     *
     *  we update global `max path sum`,
     *  but the `maxSum` is NOT return as method reponse,
     *  we simply update the global variable `maxSum`
     *
     *  -> the method return val is local max path (node.val + Math.max(leftMax, rightMax))
     */
    maxSum = Math.max(maxSum, node.val + leftMax + rightMax);

    // Return max sum path including this node (but only one subtree path)
    /**
     *  NOTE !!!
     *
     *
     *  -> the method return val is local max path (node.val + Math.max(leftMax, rightMax)),
     *     instead of `maxSum`
     *
     */
    return node.val + Math.max(leftMax, rightMax);
}

```

```python
# 124. Binary Tree Maximum Path Sum
# V0
# IDEA : DFS
# https://leetcode.com/problems/binary-tree-maximum-path-sum/discuss/209995/Python-solution
class Solution(object):
    def maxPathSum(self, root):
        def dfs(root):
            if not root:
                return 0
            l_max = dfs(root.left)
            r_max = dfs(root.right)
            """
            handle if l_max < 0:
                    -> start again from root.val
                   else:
                    -> l_max += root.val
            """
            if l_max < 0:
                l_max = root.val
            else:
                l_max += root.val
            """
            handle if r_max < 0:
                    -> start again from root.val
                   else:
                    -> r_max += root.val
            """
            if r_max < 0:
                r_max = root.val
            else:
                r_max += root.val

            self.maximum = max(self.maximum, l_max + r_max - root.val)
            return max(l_max, r_max)
           
        self.maximum = -float('inf')
        dfs(root)
        return self.maximum
```

### 2-17) Build Binary Expression Tree From Infix Expression
```python
# LC 1597 Build Binary Expression Tree From Infix Expression
# V0
# IDEA : LC 224 Basic Calculator
class Solution(object):

    def help(self, numSt, opSt):
        right = numSt.pop()
        left = numSt.pop()
        # Node(val=op, left=lhs, right=rhs)
        return Node(opSt.pop(), left, right)

    def expTree(self, s):
        # hashmap for operator ordering
        pr = {'*': 1, '/': 1, '+': 2, '-': 2, ')': 3, '(': 4}
        numSt = []
        opSt = []
        i = 0
        while i < len(s):
            c = s[i]
            i += 1
            # check if int(c) if string
            if c.isnumeric():
                numSt.append(Node(c))
            else:                
                if c == '(':
                    opSt.append('(')
                else:
                    while(len(opSt) > 0 and pr[c] >= pr[opSt[-1]]):
                        numSt.append(self.help(numSt, opSt))
                    if c == ')':
                        opSt.pop() # Now what remains is the closing bracket ')'
                    else:
                        opSt.append(c)
        while len(opSt) > 0:
            numSt.append(self.help(numSt, opSt))
        print (">>> numSt = {}, opSt = {}".format(str(numSt), opSt))
        return numSt.pop()

# V0'
# IDEA : RECURSIVE
class Solution:
    def expTree(self, s):
        n = len(s)
        if n == 1:
            return Node(s)

        fstOpIdx = None
        kets = 0
        for i in range(n-1, 0, -1):
            if s[i] == ")":
                kets += 1
            elif s[i] == "(":
                kets -= 1
            elif kets == 0:
                if s[i] in "+-":
                    fstOpIdx = i
                    break
                elif s[i] in "*/" and fstOpIdx is None:
                    fstOpIdx = i
        if fstOpIdx is None:
            return self.expTree(s[1:-1])
        rtNd = Node(s[fstOpIdx])
        rtNd.left = self.expTree(s[:fstOpIdx])
        rtNd.right = self.expTree(s[fstOpIdx+1:])
        return rtNd
```

### 2-18) Count Good Nodes in Binary Tree
```java
// java
// LC 1448
 // V1
    // IDEA : DFS
    // https://leetcode.com/problems/count-good-nodes-in-binary-tree/editorial/
    private int numGoodNodes = 0;

    public int goodNodes_2(TreeNode root) {
        dfs(root, Integer.MIN_VALUE);
        return numGoodNodes;
    }

    private void dfs(TreeNode node, int maxSoFar) {
        if (maxSoFar <= node.val) {
            numGoodNodes++;
        }

        if (node.right != null) {
            dfs(node.right, Math.max(node.val, maxSoFar));
        }

        if (node.left != null) {
            dfs(node.left, Math.max(node.val, maxSoFar));
        }
    }


    // V2
    // IDEA : DFS + Iterative
    // https://leetcode.com/problems/count-good-nodes-in-binary-tree/editorial/
    class Pair {

        public TreeNode node;
        public int maxSoFar;

        public Pair(TreeNode node, int maxSoFar) {
            this.node = node;
            this.maxSoFar = maxSoFar;
        }
    }

    public int goodNodes_3(TreeNode root) {
        int numGoodNodes = 0;
        Stack<Pair> stack = new Stack<>();
        stack.push(new Pair(root, Integer.MIN_VALUE));

        while (stack.size() > 0) {
            Pair curr = stack.pop();
            if (curr.maxSoFar <= curr.node.val) {
                numGoodNodes++;
            }

            if (curr.node.left != null) {
                stack.push(new Pair(curr.node.left, Math.max(curr.node.val, curr.maxSoFar)));
            }

            if (curr.node.right != null) {
                stack.push(new Pair(curr.node.right, Math.max(curr.node.val, curr.maxSoFar)));
            }
        }

        return numGoodNodes;
    }
```

### 2-19) Balanced Binary Tree
```java
// java
// LC 110


// V0
// IDEA : DFS
// https://www.bilibili.com/video/BV1Ug411S7my/?share_source=copy_web
public boolean isBalanced(TreeNode root) {
    // edge
    if (root == null) {
        return true;
    }
    if (root.left == null && root.right == null) {
        return true;
    }

    int leftDepth = getDepthDFS(root.left);
    int rightDepth = getDepthDFS(root.right);

    // check if `current` node is `balanced`
    if (Math.abs(leftDepth - rightDepth) > 1) {
        return false;
    }

    // dfs call
    // recursively check if `sub left node` and  `sub right node` are `balanced`
    return isBalanced(root.left) && isBalanced(root.right);
}

// LC 104
public int getDepthDFS(TreeNode root) {
    if (root == null) {
        return 0;
    }

  return Math.max(getDepthDFS(root.left), getDepthDFS(root.right)) + 1;
}

// V1
// IDEA :  TOP DOWN RECURSION
// https://leetcode.com/problems/balanced-binary-tree/editorial/
// Recursively obtain the height of a tree. An empty tree has -1 height
private int height(TreeNode root) {
    // An empty tree has height -1
    if (root == null) {
        return -1;
    }
    return 1 + Math.max(height(root.left), height(root.right));
}

public boolean isBalanced(TreeNode root) {
    // An empty tree satisfies the definition of a balanced tree
    if (root == null) {
        return true;
    }

    // Check if subtrees have height within 1. If they do, check if the
    // subtrees are balanced
    return Math.abs(height(root.left) - height(root.right)) < 2
            && isBalanced(root.left)
            && isBalanced(root.right);
}
```

### 2-20) Reverse Odd Levels of Binary Tree

```java
// java
// LC 2415

// V0-1
// IDEA: DFS + `left, right, layer as helper func parameter` (fixed by gpt)
public TreeNode reverseOddLevels_0_1(TreeNode root) {
    if (root == null)
        return null;

    reverseHelper(root.left, root.right, 1);
    return root;
}

/**
 *  NOTE !!!
 *
 *   we NEED to setup 3 parameter in the helper func
 *
 *   1. left node
 *   2. right node
 *   3. layer
 *
 *
 *  NOTE !!!
 *
 *   the helper func return NOTHING !!! (e.g. void)
 */
private void reverseHelper(TreeNode left, TreeNode right, int level) {
    if (left == null || right == null)
        return;

    // Swap values if we're at an odd level
    if (level % 2 == 1) {
        int temp = left.val;
        left.val = right.val;
        right.val = temp;
    }

    /**  NOTE !!! below
     *
     *
     */
    // Recurse into symmetric children
    reverseHelper(left.left, right.right, level + 1);
    reverseHelper(left.right, right.left, level + 1);
}
```