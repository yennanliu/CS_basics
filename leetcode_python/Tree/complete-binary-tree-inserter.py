"""

919. Complete Binary Tree Inserter
Medium

A complete binary tree is a binary tree in which every level, except possibly the last, is completely filled, and all nodes are as far left as possible.

Design an algorithm to insert a new node to a complete binary tree keeping it complete after the insertion.

Implement the CBTInserter class:

CBTInserter(TreeNode root) Initializes the data structure with the root of the complete binary tree.
int insert(int v) Inserts a TreeNode into the tree with value Node.val == val so that the tree remains complete, and returns the value of the parent of the inserted TreeNode.
TreeNode get_root() Returns the root node of the tree.

Example 1:

https://assets.leetcode.com/uploads/2021/08/03/lc-treeinsert.jpg

Input
["CBTInserter", "insert", "insert", "get_root"]
[[[1, 2]], [3], [4], []]
Output
[null, 1, 2, [1, 2, 3, 4]]

Explanation
CBTInserter cBTInserter = new CBTInserter([1, 2]);
cBTInserter.insert(3);  // return 1
cBTInserter.insert(4);  // return 2
cBTInserter.get_root(); // return [1, 2, 3, 4]

Constraints:

The number of nodes in the tree will be in the range [1, 1000].
0 <= Node.val <= 5000
root is a complete binary tree.
0 <= val <= 5000
At most 10^4 calls will be made to insert and get_root.

"""

# V0

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/82958284
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
class CBTInserter(object):

    # time = O(n)  # n = number of tree nodes
    # space = O(n)  # self.tree stores all n nodes
    def __init__(self, root):
        """
        :type root: TreeNode
        """
        self.tree = list()
        queue = collections.deque()
        queue.append(root)
        while queue:
            node = queue.popleft()
            self.tree.append(node)
            if node.left:
                queue.append(node.left)
            if node.right:
                queue.append(node.right)

    # time = O(1)
    # space = O(1)
    def insert(self, v):
        """
        :type v: int
        :rtype: int
        """
        _len = len(self.tree)
        father = self.tree[(_len - 1) / 2]
        node = TreeNode(v)
        if not father.left:
            father.left = node
        else:
            father.right = node
        self.tree.append(node)
        return father.val
        

    # time = O(1)
    # space = O(1)
    def get_root(self):
        """
        :rtype: TreeNode
        """
        return self.tree[0]


# Your CBTInserter object will be instantiated and called as such:
# obj = CBTInserter(root)
# param_1 = obj.insert(v)
# param_2 = obj.get_root()

# V2
class TreeNode(object):
    def __init__(self, x):
        self.val = x
        self.left = None
        self.right = None

class CBTInserter(object):

    # time = O(n)  # n = number of tree nodes
    # space = O(n)  # self.__tree stores all n nodes
    def __init__(self, root):
        """
        :type root: TreeNode
        """
        self.__tree = [root]
        for i in self.__tree:
            if i.left:
                self.__tree.append(i.left)
            if i.right:
                self.__tree.append(i.right)        

    # time = O(1)
    # space = O(1)
    def insert(self, v):
        """
        :type v: int
        :rtype: int
        """
        n = len(self.__tree)
        self.__tree.append(TreeNode(v))
        if n % 2:
            self.__tree[(n-1)//2].left = self.__tree[-1]
        else:
            self.__tree[(n-1)//2].right = self.__tree[-1]
        return self.__tree[(n-1)//2].val

    # time = O(1)
    # space = O(1)
    def get_root(self):
        """
        :rtype: TreeNode
        """
        return self.__tree[0]