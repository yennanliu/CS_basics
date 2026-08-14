"""

1261. Find Elements in a Contaminated Binary Tree
Medium

Given a binary tree with the following rules:

root.val == 0
For any treeNode:
    If treeNode.val has a value x and treeNode.left != null, then treeNode.left.val == 2 * x + 1
    If treeNode.val has a value x and treeNode.right != null, then treeNode.right.val == 2 * x + 2

Now the binary tree is contaminated, which means all treeNode.val have been changed to -1.

Implement the FindElements class:

FindElements(TreeNode* root) Initializes the object with a contaminated binary tree and recovers it.
bool find(int target) Returns true if the target value exists in the recovered binary tree.


Example 1:

Input
["FindElements","find","find"]
[[[-1,null,-1]],[1],[2]]
Output
[null,false,true]
Explanation
FindElements findElements = new FindElements([-1,null,-1]);
findElements.find(1); // return False
findElements.find(2); // return True

Example 2:

Input
["FindElements","find","find","find"]
[[[-1,-1,-1,-1,-1]],[1],[3],[5]]
Output
[null,true,true,false]

Example 3:

Input
["FindElements","find","find","find","find"]
[[[-1,null,-1,-1,null,-1]],[2],[3],[4],[5]]
Output
[null,true,false,false,true]


Constraints:

TreeNode.val == -1
The height of the binary tree is less than or equal to 20
The total number of nodes is between [1, 10^4]
Total calls of find() is between [1, 10^4]
0 <= target <= 10^6

"""

# V0
# IDEA : DFS RECOVER + HASH SET
#        rebuild every value once at construction time, cache them in a set,
#        so each find() is O(1)
# time = O(n) for __init__, O(1) for find
# space = O(n)
class TreeNode(object):
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right


class FindElements(object):
    def __init__(self, root):
        self.seen = set()
        # iterative DFS, so a 10^4 nodes tree can NOT blow the recursion stack
        stack = []
        if root:
            root.val = 0
            stack.append(root)
        while stack:
            node = stack.pop()
            self.seen.add(node.val)
            if node.left:
                node.left.val = 2 * node.val + 1
                stack.append(node.left)
            if node.right:
                node.right.val = 2 * node.val + 2
                stack.append(node.right)

    def find(self, target):
        return target in self.seen


# V1
# IDEA : WALK DOWN FROM THE TARGET (no pre computed set)
#        from `target`, repeatedly go to its parent ((target - 1) // 2)
#        to get the root -> leaf path, then replay that path on the tree
# time = O(log(target)) per find
# space = O(log(target)) per find
class FindElements2(object):
    def __init__(self, root):
        self.root = root
        if self.root:
            self.root.val = 0

    def find(self, target):
        if not self.root:
            return False
        # collect the moves (target -> root), then reverse them
        path = []
        while target > 0:
            path.append(1 if target % 2 else 2)  # 1 = left child, 2 = right child
            target = (target - 1) // 2
        node = self.root
        for step in reversed(path):
            node = node.left if step == 1 else node.right
            if not node:
                return False
        return True
