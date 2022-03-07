"""

545. Boundary of Binary Tree (Medium)

https://xiaoguan.gitbooks.io/leetcode/content/LeetCode/545-boundary-of-binary-tree-medium.html

Given a binary tree, return the values of its boundary in anti-clockwise direction starting from root. Boundary includes left boundary, leaves, and right boundary in order without duplicate nodes.

Left boundary is defined as the path from root to the left-most node. Right boundary is defined as the path from root to the right-most node. If the root doesn't have left subtree or right subtree, then the root itself is left boundary or right boundary. Note this definition only applies to the input binary tree, and not applies to any subtrees.

The left-most node is defined as a leaf node you could reach when you always firstly travel to the left subtree if exists. If not, travel to the right subtree. Repeat until you reach a leaf node.

The right-most node is also defined by the same way with left and right exchanged.

Example 1

Input:
  1
   \
    2
   / \
  3   4

Ouput:
[1, 3, 4, 2]

Explanation:
The root doesn't have left subtree, so the root itself is left boundary.
The leaves are node 3 and 4.
The right boundary are node 1,2,4. Note the anti-clockwise direction means you should output reversed right boundary.
So order them in anti-clockwise without duplicates and we have [1,3,4,2].
Example 2

Input:
    ____1_____
   /          \
  2            3
 / \          / 
4   5        6   
   / \      / \
  7   8    9  10  

Ouput:
[1,2,4,7,8,9,10,6,3]

Explanation:
The left boundary are node 1,2,4. (4 is the left-most node according to definition)
The leaves are node 4,7,8,9,10.
The right boundary are node 1,3,6,10. (10 is the right-most node).
So order them in anti-clockwise without duplicate nodes we have [1,2,4,7,8,9,10,6,3].
Summary
Given a binary tree, return the values of its boundary in anti-clockwise direction starting from root. Boundary includes left boundary, leaves, and right boundary in order without duplicate nodes.

Left boundary is defined as the path from root to the left-most node. Right boundary is defined as the path from root to the right-most node. If the root doesn't have left subtree or right subtree, then the root itself is left boundary or right boundary. Note this definition only applies to the input binary tree, and not applies to any subtrees.

The left-most node is defined as a leaf node you could reach when you always firstly travel to the left subtree if exists. If not, travel to the right subtree. Repeat until you reach a leaf node.

The right-most node is also defined by the same way with left and right exchanged.

"""

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

# V1
# https://www.cnblogs.com/lightwindy/p/9583723.html
class Solution(object):
    def boundaryOfBinaryTree(self, root):
        """
        :type root: TreeNode
        :rtype: List[int]
        """
        def leftBoundary(root, nodes):
            if not root or (not root.left and not root.right):
                return
            nodes.append(root.val)
            if not root.left:
                leftBoundary(root.right, nodes)
            else:
                leftBoundary(root.left, nodes)
 
        def rightBoundary(root, nodes):
            if not root or (not root.left and not root.right):
                return
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
        leaves(root.left, nodes)
        leaves(root.right, nodes)
        rightBoundary(root.right, nodes)
        return nodes

### Test case : dev

# V1
# https://leetcode.com/problems/boundary-of-binary-tree/discuss/101309/Python-Straightforward-with-Explanation
# IDEA :
# Let's merely get the nodes from the left boundary, the right boundary, and the leaves, in counter-clockwise order.
# To get nodes from the left boundary, we start from root.left and move left if we can, else right, until we can't move anymore. The right boundary is similar.
# To get nodes from the leaves, we DFS until we hit a leaf (until node.left and node.right are both None). We should take care to add to our stack in the order (right, left) so that they are popped in the order (left, right).
# Now armed with all the nodes we could visit, let's visit them in order. As we visit a node, we should skip over ones we've seen before (comparing node objects by pointer, not node.val), and otherwise add node.val to our answer.
# We could also rewrite this answer by calling visit(cur) directly instead of appending to left_bd_nodes, etc. to save a little space.
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

# V1
# https://leetcode.com/problems/boundary-of-binary-tree/discuss/422802/python-concise-solution
class Solution:
    def boundaryOfBinaryTree(self, root: TreeNode) -> List[int]:
        def dfs(root,isl,isr):
            if root:
                isleaf=not root.left and not root.right
                if isl or isleaf:
                    res.append(root.val)
                dfs(root.left,isl,isr and not root.right)
                dfs(root.right,isl and not root.left,isr)
                if isr and not isleaf:
                    res.append(root.val)
                
        if not root:
            return []
        res=[root.val]
        dfs(root.left,True,False)
        dfs(root.right,False,True)
        return res

# V1
# https://leetcode.com/problems/boundary-of-binary-tree/discuss/422802/python-concise-solution
class Solution:
    def boundaryOfBinaryTree(self, root: TreeNode) -> List[int]:
        def leaf(root):
            if not root:
                return 
            if not root.left and not root.right:
                res.append(root.val)
            leaf(root.left)
            leaf(root.right)
            
        def left_boundary(root):#not take leaf to avoid duplicate
            if not root or not root.left and not root.right:
                return 
            res.append(root.val)
            if root.left:
                left_boundary(root.left)
            else:
                left_boundary(root.right)
                
        
        def right_boundary(root):
            if not root or not root.left and not root.right:
                return 
            if root.right:
                right_boundary(root.right)
            else:
                right_boundary(root.left)
            res.append(root.val)
            
        if not root:
            return []
        res=[root.val]
        left_boundary(root.left)#not include leaf
        leaf(root.left)
        leaf(root.right)
        right_boundary(root.right)
        return res

# V1'
# https://www.jiuzhang.com/solution/boundary-of-binary-tree/#tag-highlight-lang-python
class Solution:
    """
    @param root: a TreeNode
    @return: a list of integer
    """
    def boundaryOfBinaryTree(self, root):
        self.ans = []
        if root == None:
            return self.ans
        self.ans.append(root.val)
        if root.left == None and root.right == None:
            return self.ans
        self.dfsLeft(root.left)
        self.dfsLeaf(root)
        self.dfsRight(root.right)
        return self.ans
    
    def dfsLeft(self, rt):
        if rt == None or (rt.left == None and rt.right == None):
            return
        self.ans.append(rt.val)
        if rt.left != None:
            self.dfsLeft(rt.left)
        else:
            self.dfsLeft(rt.right)
        
    def dfsLeaf(self, rt):
        if rt == None:
            return 
        if rt.left == None and rt.right == None:
            self.ans.append(rt.val)
            return
        self.dfsLeaf(rt.left)
        self.dfsLeaf(rt.right)
    
    def dfsRight(self, rt):
        if rt == None or (rt.left == None and rt.right == None):
            return
        if rt.right != None:
            self.dfsRight(rt.right)
        else:
            self.dfsRight(rt.left)
        self.ans.append(rt.val)

# V1''
# https://www.jiuzhang.com/solution/boundary-of-binary-tree/#tag-highlight-lang-python
class Solution:
    """
    @param root: a TreeNode
    @return: return a list of integer
    """

    def boundaryOfBinaryTree(self, root):
        self.ans = []
        if(root == None):
            return self.ans
        self.ans.append(root.val)
        self.dfs(root.left, True, False)
        self.dfs(root.right, False, True)
        return self.ans

    def dfs(self, root, lft, rgt):
        if(root == None):
            return
        if(root.left == None and root.right == None):
            self.ans.append(root.val)
            return
        if(lft):
            self.ans.append(root.val)
        self.dfs(root.left, lft, rgt and root.right == None)
        self.dfs(root.right, lft and root.left == None, rgt)
        if(rgt):
            self.ans.append(root.val)
            
# V1'''
# http://bookshadow.com/weblog/2017/03/26/leetcode-boundary-of-binary-tree/
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
class Solution(object):
    def boundaryOfBinaryTree(self, root):
        """
        :type root: TreeNode
        :rtype: List[int]
        """
        if not root: return []
        if not root.left and not root.right: return [root.val]

        leaves = []
        def traverse(root):
            if not root.left and not root.right:
                leaves.append(root)
            if root.left:
                traverse(root.left)
            if root.right:
                traverse(root.right)
        traverse(root)

        left = []
        node = root
        while node and node != leaves[0]:
            left.append(node)
            if node.left: node = node.left
            else: node = node.right

        right = []
        node = root
        while node and node != leaves[-1]:
            right.append(node)
            if node.right: node = node.right
            else: node = node.left

        left = left[1:] if root.left else []
        right = right[1:] if root.right else []
        return [node.val for node in [root] + left + leaves + right[::-1]]

# V1''''
# https://blog.csdn.net/danspace1/article/details/86659807
class Solution:
    def boundaryOfBinaryTree(self, root):
        """
        :type root: TreeNode
        :rtype: List[int]
        """
        def leftBoundary(node):
            if not node or (node.left is None and node.right is None):
                return 
            b.append(node.val)
            if node.left:
                leftBoundary(node.left)
            else:
                leftBoundary(node.right)
            
        def leaves(node):
            if not node:
                return
            leaves(node.left)
            if node != root and node.left is None and node.right is None:
                b.append(node.val)
            
            leaves(node.right)
            
        def rightBoundary(node):
            if not node or (node.left is None and node.right is None):
                return            
            if node.right:
                rightBoundary(node.right)
            else:
                rightBoundary(node.left)
            b.append(node.val)
                 
        # base case
        if not root: return []
        b = [root.val]
        leftBoundary(root.left)
        leaves(root)
        rightBoundary(root.right)
        return b

# V1
# IDEA : Simple Solution
# https://leetcode.com/problems/boundary-of-binary-tree/solution/
# JAVA
# /**
#  * Definition for a binary tree node.
#  * public class TreeNode {
#  *     int val;
#  *     TreeNode left;
#  *     TreeNode right;
#  *     TreeNode(int x) { val = x; }
#  * }
#  */
# public class Solution {
#
#     public boolean isLeaf(TreeNode t) {
#         return t.left == null && t.right == null;
#     }
#
#     public void addLeaves(List<Integer> res, TreeNode root) {
#         if (isLeaf(root)) {
#             res.add(root.val);
#         } else {
#             if (root.left != null) {
#                 addLeaves(res, root.left);
#             }
#             if (root.right != null) {
#                 addLeaves(res, root.right);
#             }
#         }
#     }
#
#     public List<Integer> boundaryOfBinaryTree(TreeNode root) {
#         ArrayList<Integer> res = new ArrayList<>();
#         if (root == null) {
#             return res;
#         }
#         if (!isLeaf(root)) {
#             res.add(root.val);
#         }
#         TreeNode t = root.left;
#         while (t != null) {
#             if (!isLeaf(t)) {
#                 res.add(t.val);
#             }
#             if (t.left != null) {
#                 t = t.left;
#             } else {
#                 t = t.right;
#             }
#
#         }
#         addLeaves(res, root);
#         Stack<Integer> s = new Stack<>();
#         t = root.right;
#         while (t != null) {
#             if (!isLeaf(t)) {
#                 s.push(t.val);
#             }
#             if (t.right != null) {
#                 t = t.right;
#             } else {
#                 t = t.left;
#             }
#         }
#         while (!s.empty()) {
#             res.add(s.pop());
#         }
#         return res;
#     }
# }

# V1
# IDEA :  PreOrder Traversal 
# https://leetcode.com/problems/boundary-of-binary-tree/solution/
# JAVA
# public class Solution {
#     public List < Integer > boundaryOfBinaryTree(TreeNode root) {
#         List < Integer > left_boundary = new LinkedList < > (), right_boundary = new LinkedList < > (), leaves = new LinkedList < > ();
#         preorder(root, left_boundary, right_boundary, leaves, 0);
#         left_boundary.addAll(leaves);
#         left_boundary.addAll(right_boundary);
#         return left_boundary;
#     }
#
#     public boolean isLeaf(TreeNode cur) {
#         return (cur.left == null && cur.right == null);
#     }
#
#     public boolean isRightBoundary(int flag) {
#         return (flag == 2);
#     }
#
#     public boolean isLeftBoundary(int flag) {
#         return (flag == 1);
#     }
#
#     public boolean isRoot(int flag) {
#         return (flag == 0);
#     }
#
#     public int leftChildFlag(TreeNode cur, int flag) {
#         if (isLeftBoundary(flag) || isRoot(flag))
#             return 1;
#         else if (isRightBoundary(flag) && cur.right == null)
#             return 2;
#         else return 3;
#     }
#
#     public int rightChildFlag(TreeNode cur, int flag) {
#         if (isRightBoundary(flag) || isRoot(flag))
#             return 2;
#         else if (isLeftBoundary(flag) && cur.left == null)
#             return 1;
#         else return 3;
#     }
#
#     public void preorder(TreeNode cur, List < Integer > left_boundary, List < Integer > right_boundary, List < Integer > leaves, int flag) {
#         if (cur == null)
#             return;
#         if (isRightBoundary(flag))
#             right_boundary.add(0, cur.val);
#         else if (isLeftBoundary(flag) || isRoot(flag))
#             left_boundary.add(cur.val);
#         else if (isLeaf(cur))
#             leaves.add(cur.val);
#         preorder(cur.left, left_boundary, right_boundary, leaves, leftChildFlag(cur, flag));
#         preorder(cur.right, left_boundary, right_boundary, leaves, rightChildFlag(cur, flag));
#     }
# }

# V2 
# Time:  O(n)
# Space: O(h)
class Solution(object):
    def boundaryOfBinaryTree(self, root):
        """
        :type root: TreeNode
        :rtype: List[int]
        """
        def leftBoundary(root, nodes):
            if not root or (not root.left and not root.right):
                return
            nodes.append(root.val)
            if not root.left:
                leftBoundary(root.right, nodes)
            else:
                leftBoundary(root.left, nodes)

        def rightBoundary(root, nodes):
            if not root or (not root.left and not root.right):
                return
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
        leaves(root.left, nodes)
        leaves(root.right, nodes)
        rightBoundary(root.right, nodes)
        return nodes