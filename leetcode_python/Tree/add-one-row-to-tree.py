"""

623. Add One Row to Tree
Solved
Medium
Topics
premium lock icon
Companies
Given the root of a binary tree and two integers val and depth, add a row of nodes with value val at the given depth depth.

Note that the root node is at depth 1.

The adding rule is:

Given the integer depth, for each not null tree node cur at the depth depth - 1, create two tree nodes with value val as cur's left subtree root and right subtree root.
cur's original left subtree should be the left subtree of the new left subtree root.
cur's original right subtree should be the right subtree of the new right subtree root.
If depth == 1 that means there is no depth depth - 1 at all, then create a tree node with value val as the new root of the whole original tree, and the original tree is the new root's left subtree.
 

Example 1:


Input: root = [4,2,6,3,1,5], val = 1, depth = 2
Output: [4,1,1,2,null,null,6,3,1,5]
Example 2:


Input: root = [4,2,null,3,1], val = 1, depth = 3
Output: [4,2,null,1,1,3,null,null,1]
 

Constraints:

The number of nodes in the tree is in the range [1, 104].
The depth of the tree is in the range [1, 104].
-100 <= Node.val <= 100
-105 <= val <= 105
1 <= depth <= the depth of tree + 1
 

"""


# V0
# IDEA: BFS (gpt)
from collections import deque

class Solution(object):
    def addOneRow(self, root, val, depth):
        if depth == 1:
            new_root = TreeNode(val)
            new_root.left = root
            return new_root


        """

        NOTE !! 

        1. q: [node, depth]
        2. depth init as 1

        """
        q = deque([(root, 1)])

        while q:
            node, cur_depth = q.popleft()

            if cur_depth == depth - 1:
                old_left = node.left
                old_right = node.right

                node.left = TreeNode(val)
                node.right = TreeNode(val)

                node.left.left = old_left
                node.right.right = old_right

                """
                NOTE !!!

                NO need to add node to q
                in this path (if cur_depth == depth - 1)
                """

            else:
                if node.left:
                    q.append((node.left, cur_depth + 1))
                if node.right:
                    q.append((node.right, cur_depth + 1))

        return root

# V0-0-1
# IDEA: DFS (gpt)
class Solution(object):
    def findDuplicateSubtrees(self, root):
        # {serialization: frequency}
        self.count = {}

        # answer
        self.res = []

        self.helper(root)

        return self.res

    def helper(self, root):
        if not root:
            return "#"

        left = self.helper(root.left)
        right = self.helper(root.right)

        # Serialize current subtree
        serial = "{},{},{}".format(root.val, left, right)

        self.count[serial] = self.count.get(serial, 0) + 1

        # Add only once
        if self.count[serial] == 2:
            self.res.append(root)

        return serial


# V0-0-2
# IDEA: DFS (gemini)
import collections

class Solution(object):
    def findDuplicateSubtrees(self, root):
        """
        :type root: TreeNode
        :rtype: List[TreeNode]
        """
        # 記錄每種子樹字串出現的次數
        self.subtree_counts = collections.defaultdict(int)
        self.res = []
        
        # 啟動 DFS
        self.helper(root)
        
        return self.res
        
    def helper(self, node):
        # 1. 遇到空節點，回傳特殊符號 (這非常重要，用來區分左右子樹的形狀)
        if not node:
            return "#"
            
        # 2. Post-order DFS：先取得左右子樹的字串
        _left = self.helper(node.left)
        _right = self.helper(node.right)
        
        # 3. 將自己與左右子樹組合成當前子樹的「唯一簽名檔 (Signature)」
        # 格式: "節點值,左子樹字串,右子樹字串"
        current_str = str(node.val) + "," + _left + "," + _right
        
        # 4. 統計這個簽名檔出現的次數
        self.subtree_counts[current_str] += 1
        
        # 5. 如果是「第二次」看到這個簽名檔，代表找到重複子樹了！
        # (只在 == 2 時加入，避免出現 3 次時重複加入 res)
        if self.subtree_counts[current_str] == 2:
            self.res.append(node)
            
        # 6. 把自己的簽名檔往上回傳給父節點
        return current_str


# V0-1
# IDEA: DFS (gpt)
class Solution(object):
    def addOneRow(self, root, val, depth):
        if depth == 1:
            new_root = TreeNode(val)
            new_root.left = root
            return new_root

        self.dfs(root, val, 1, depth)
        return root

    def dfs(self, node, val, cur_depth, target_depth):
        if not node:
            return

        if cur_depth == target_depth - 1:
            old_left = node.left
            old_right = node.right

            node.left = TreeNode(val)
            node.right = TreeNode(val)

            """
            # NOTE below !!!

            -> 

            Reconnect the node to the original sub node
             via a `one-node` jump.

            """
            node.left.left = old_left
            node.right.right = old_right
            return

        self.dfs(node.left, val, cur_depth + 1, target_depth)
        self.dfs(node.right, val, cur_depth + 1, target_depth)


# V0-2
# IDEA: DFS (GEMINI)
class Solution(object):
    def addOneRow(self, root, val, depth):
        """
        :type root: TreeNode
        :type val: int
        :type depth: int
        :rtype: TreeNode
        """
        # Edge case: If depth is 1, create a new root and attach the old tree to its left
        if depth == 1:
            new_root = TreeNode(val)
            new_root.left = root
            return new_root
            
        def dfs(node, curr_depth):
            # Base case: Hit a dead end
            if not node:
                return
                
            # If we reached the row RIGHT ABOVE where we need to insert
            if curr_depth == depth - 1:
                # 1. Save original children
                old_left = node.left
                old_right = node.right
                
                # 2. Insert new nodes
                node.left = TreeNode(val)
                node.right = TreeNode(val)
                
                
                """
                # NOTE below !!!

                -> 

                Reconnect the node to the original sub node
                 via a `one-node` jump.

                """
                # 3. Reattach original children
                node.left.left = old_left
                node.right.right = old_right
                
                # 4. Stop traversing this branch! 
                # (The new nodes are at `depth`, no need to process them or go deeper)
                return 
                
            # Otherwise, keep moving down the tree
            dfs(node.left, curr_depth + 1)
            dfs(node.right, curr_depth + 1)

        # Kick off the DFS starting at the root (depth 1)
        dfs(root, 1)
        
        return root



# V0-3-1
# IDEA: BFS (gpt)
from collections import deque

class Solution(object):
    def addOneRow(self, root, val, depth):
        if depth == 1:
            new_root = TreeNode(val)
            new_root.left = root
            return new_root

        q = deque([root])
        cur_depth = 1

        while q:
            size = len(q)

            for _ in range(size):
                node = q.popleft()

                if cur_depth == depth - 1:
                    old_left = node.left
                    old_right = node.right

                    node.left = TreeNode(val)
                    node.right = TreeNode(val)

                    node.left.left = old_left
                    node.right.right = old_right

                else:
                    if node.left:
                        q.append(node.left)
                    if node.right:
                        q.append(node.right)

            if cur_depth == depth - 1:
                break

            cur_depth += 1

        return root


# V0-3
# IDEA: BFS (gpt)
from collections import deque

class Solution(object):
    def addOneRow(self, root, val, depth):
        if depth == 1:
            new_root = TreeNode(val)
            new_root.left = root
            return new_root

        q = deque([root])
        cur_depth = 1

        while q:
            size = len(q)

            """
            NOTE !!!


            we treat  `curr_depth == depth - 1`
            as a different path

            -> we do `looping, reconnect` inside the path
            """

            if cur_depth == depth - 1:
                for _ in range(size):
                    node = q.popleft()

                    # Save original children
                    old_left = node.left
                    old_right = node.right

                    # Insert new nodes
                    node.left = TreeNode(val)
                    node.right = TreeNode(val)

                    # Reattach original subtrees
                    node.left.left = old_left
                    node.right.right = old_right

                break

            for _ in range(size):
                node = q.popleft()

                if node.left:
                    q.append(node.left)
                if node.right:
                    q.append(node.right)

            cur_depth += 1

        return root



# V0-4
# IDEA: BFS (gemini)
from collections import deque

class Solution(object):
    def addOneRow(self, root, val, depth):
        """
        :type root: TreeNode
        :type val: int
        :type depth: int
        :rtype: TreeNode
        """
        # Edge case: If depth is 1, the problem states the new node becomes the new root
        if depth == 1:
            new_root = TreeNode(val)
            new_root.left = root
            return new_root
            
        # Use deque for O(1) pops from the front
        q = deque([root])
        curr_depth = 1  # The root is at depth 1
        
        while q:

            """
            NOTE !!!


            we treat  `curr_depth == depth - 1`
            as a different path

            -> we do `looping, reconnect` inside the path
            """
            # If we reached the row RIGHT ABOVE where we need to insert
            if curr_depth == depth - 1:
                for _ in range(len(q)):
                    node = q.popleft()
                    
                    # 1. Cache the original children
                    _ori_left = node.left
                    _ori_right = node.right
                    
                    # 2. Create the new nodes and attach them to the current node
                    node.left = TreeNode(val)
                    node.right = TreeNode(val)
                    
                    # 3. IMMEDIATELY attach the original children to the new nodes
                    node.left.left = _ori_left
                    node.right.right = _ori_right
                    
                # We added the row, we are completely done!
                return root
                
            # Otherwise, just do standard BFS to move down a level
            size = len(q)
            for _ in range(size):
                node = q.popleft()
                if node.left:
                    q.append(node.left)
                if node.right:
                    q.append(node.right)
                    
            curr_depth += 1
            
        return root



# V1
# https://www.jiuzhang.com/solution/add-one-row-to-tree/#tag-highlight-lang-python
# time = O(n)  # n = number of tree nodes
# space = O(h)  # h = tree height (recursion stack)
class Solution:
    """
    @param root: the root of binary tree
    @param v: a integer
    @param d: a integer
    @return: return a TreeNode
    """
    def addOneRow(self, root, v, d):
        # write your code here
        if not root:
            return None
        if d==1:
            new_root = TreeNode(v)
            new_root.left = root
            return new_root
        if d==2:
            root.left, root.left.left = TreeNode(v), root.left
            root.right, root.right.right = TreeNode(v), root.right
            return root
        elif d>2:
            root.left = self.addOneRow(root.left, v, d-1)
            root.right = self.addOneRow(root.right, v, d-1)
        return root
        
# V1' 
# https://blog.csdn.net/fuxuemingzhu/article/details/79645198
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
# time = O(n)  # n = number of tree nodes
# space = O(h)  # h = tree height (recursion stack)
class Solution(object):
    def addOneRow(self, root, v, d):
        """
        :type root: TreeNode
        :type v: int
        :type d: int
        :rtype: TreeNode
        """
        if not root: return root
        if d == 1:
            left = TreeNode(v)
            left.left = root
            root = left
        elif d == 2:
            left = TreeNode(v)
            right = TreeNode(v)
            left.left = root.left
            right.right = root.right
            root.left = left
            root.right = right
        else:
            self.addOneRow(root.left, v, d - 1)
            self.addOneRow(root.right, v, d - 1)
        return root

# V2 
# time = O(n)
# space = O(h)
class TreeNode(object):
    def __init__(self, x):
        self.val = x
        self.left = None
        self.right = None

class Solution(object):
    def addOneRow(self, root, v, d):
        """
        :type root: TreeNode
        :type v: int
        :type d: int
        :rtype: TreeNode
        """
        if d in (0, 1):
            node = TreeNode(v)
            if d == 1:
                node.left = root
            else:
                node.right = root
            return node
        if root and d >= 2:
            root.left = self.addOneRow(root.left,  v, d-1 if d > 2 else 1)
            root.right = self.addOneRow(root.right, v, d-1 if d > 2 else 0)
        return root
