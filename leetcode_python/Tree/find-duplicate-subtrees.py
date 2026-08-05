"""

652. Find Duplicate Subtrees
Medium

Given the root of a binary tree, return all duplicate subtrees.

For each kind of duplicate subtrees, you only need to return the root node of any one of them.

Two trees are duplicate if they have the same structure with the same node values.

 

Example 1:


Input: root = [1,2,3,4,null,2,4,null,null,4]
Output: [[2,4],[4]]
Example 2:


Input: root = [2,1,1]
Output: [[1]]
Example 3:


Input: root = [2,2,2,3,null,3,null]
Output: [[2,3],[3]]
 

Constraints:

The number of the nodes in the tree will be in the range [1, 10^4]
-200 <= Node.val <= 200

"""


# V0
# IDEA: Post-order DFS + serialization (path) (gpt)
class Solution(object):
    def findDuplicateSubtrees(self, root):
        if not root:
            return []

        self.res = []
        self.graph = {}

        self.helper(root)

        return self.res

    """
    NOTE !!!

    1. NO need to return val in helper func


    2. edge case handling

        ```
        if not root:
            return "#"
        ```


    3. path can be serialized as

        "{},{},{}".format(root.val, left, right)


    4. 

       via cnt == 1,
          -> we can check ONCE and get to know if path already existed
           
          -> we append root to res

    """
    def helper(self, root):
        if not root:
            return "#"

        left = self.helper(root.left)
        right = self.helper(root.right)

        serial = "{},{},{}".format(root.val, left, right)

        cnt = self.graph.get(serial, 0)
        # NOTE !!!
        # 1. via cnt == 1,
        #   -> we can check ONCE and get to know if path already existed
        # 2. we append root to res
        if cnt == 1:
            self.res.append(root)

        self.graph[serial] = cnt + 1

        return serial


# V0-0-1
# IDEA: Post-order DFS (gpt)
class Solution(object):
    def findDuplicateSubtrees(self, root):
        # {serialization: frequency}
        self.count = {}

        # answer
        self.res = []

        self.helper(root)

        return self.res

    # Post-order DFS
    def helper(self, root):
        """
        NOTE !!!


        why we need "#" ?

        ->

        consider below cases:

        ```
        Tree A:          Tree B:

            2                2
           /                  \
          3                    3
        ```


        -> without "#", tree A, B look the SAME !! (which is wrong)


        -> with "#", the tree look like below:

        ```
        Tree A:
        2,3,#,#,#

        Tree B:
        2,#,3,#,#
        ```


        """
        if not root:
            return "#"

        left = self.helper(root.left)
        right = self.helper(root.right)

        """
        NOTE !!!

        update path as


        ` "{},{},{}".format(root.val, left, right)`

        """


        # Serialize current subtree
        serial = "{},{},{}".format(root.val, left, right)

        self.count[serial] = self.count.get(serial, 0) + 1

        # Add only once
        if self.count[serial] == 2:
            self.res.append(root)

        return serial


# V0-0-2
# IDEA: Post-order DFS (gemini)
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
        
    # Post-order DFS
    def helper(self, node):
        # 1. 遇到空節點，回傳特殊符號 (這非常重要，用來區分左右子樹的形狀)
        if not node:
            return "#"
            
        # 2. Post-order DFS：先取得左右子樹的字串
        _left = self.helper(node.left)
        _right = self.helper(node.right)
        
        """
        NOTE !!!

        update path as


        `str(node.val) + "," + _left + "," + _right`

        """
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


# V0
# IDEA: DFS (post order) + serialization + hashmap (GPT)
# time = O(n)
# space = O(n)
class Solution(object):
    def findDuplicateSubtrees(self, root):
        """

        self.node_map: { path: cnt }
        """
        self.node_map = {}
        self.res = []

        self.helper(root)

        return self.res

    def helper(self, root):
        """
        NOTE !!!


        why we need "#" ?

        ->

        consider below cases:

        ```
        Tree A:          Tree B:

            2                2
           /                  \
          3                    3
        ```


        -> without "#", tree A, B look the SAME !! (which is wrong)


        -> with "#", the tree look like below:

        ```
        Tree A:
        2,3,#,#,#

        Tree B:
        2,#,3,#,#
        ```


        """

        if not root:
            return "#"

        left = self.helper(root.left)
        right = self.helper(root.right)

        serial = "{},{},{}".format(root.val, left, right)

        if serial not in self.node_map:
            self.node_map[serial] = 1
        else:
            self.node_map[serial] += 1
            if self.node_map[serial] == 2:
                self.res.append(root)

        return serial


# V0-1
# IDEA: DFS (post order) + serialization + hashmap (GEMINI)
# time = O(n)
# space = O(n)
class Solution(object):
    def findDuplicateSubtrees(self, root):
        """
        :type root: Optional[TreeNode]
        :rtype: List[Optional[TreeNode]]
        """
        self.count_map = {}
        self.res = []
        
        # Start the bottom-up serialization traversal
        self.serialize(root)
        
        return self.res
        
    def serialize(self, node):
        if not node:
            return "#"
            
        # 1. Collect child sub-structures bottom-up (Post-order traversal)
        left_serialized = self.serialize(node.left)
        right_serialized = self.serialize(node.right)
        
        # 2. Build the unique signature for the subtree rooted at the current node
        # Using commas ensures numbers like 11 and 1 don't blend together ambiguously
        subtree_signature = "{},{},{}".format(node.val, left_serialized, right_serialized)
        
        # 3. Log the signature into our frequency tracking map
        self.count_map[subtree_signature] = self.count_map.get(subtree_signature, 0) + 1
        
        # 4. If this is exactly the SECOND time we've seen this identical structure,
        # collect its root node as a duplicate representative.
        if self.count_map[subtree_signature] == 2:
            self.res.append(node)
            
        # Return the structural layout upward so the parent node can include it
        return subtree_signature


# V0
# IDEA : DFS
# DEMO : defaultdict
# In [26]: import collections 
# In [27]: m = collections.defaultdict(int)
#
# In [28]: m
# Out[28]: defaultdict(int, {})
#
# In [29]: m[0]
# Out[29]: 0
#
# In [30]: m
# Out[30]: defaultdict(int, {0: 0})
#
# In [31]: m[1]
# Out[31]: 0
#
# In [32]: m
# Out[32]: defaultdict(int, {0: 0, 1: 0})
#
# In [33]: m[1]=1
#
# In [34]: m
# Out[34]: defaultdict(int, {0: 0, 1: 1})
#
# In [35]: m[2] +=1
#
# In [36]: m
# Out[36]: defaultdict(int, {0: 0, 1: 1, 2: 1})
#
# Example 1 : root = [2,1,1]
# output :
#      >>> m = defaultdict(<type 'int'>, {'2-1-#-#-1-#-#': 1, '1-#-#': 2})
# 
# Example 2 : root = [2,2,2,3,null,3,null]
# output :
#      >>> m = defaultdict(<type 'int'>, {'2-2-3-#-#-#-2-3-#-#-#': 1, '2-3-#-#-#': 2, '3-#-#': 2})
#
# Example 3 : root = [1,2,3,4,null,2,4,null,null,4]
# output :
#     >>> m = defaultdict(<type 'int'>, {'4-#-#': 3, '1-2-4-#-#-#-3-2-4-#-#-#-4-#-#': 1, '2-4-#-#-#': 2, '3-2-4-#-#-#-4-#-#': 1})
#
#
import collections
# time = O(n)
# space = O(n)
class Solution(object):
    def findDuplicateSubtrees(self, root):
        res = []
        m = collections.defaultdict(int)
        self.dfs(root, m, res)
        #print(">>> m = " + str(m))
        return res

    def dfs(self, root, m, res):
        if not root:
            return '#'
        ### Notice here 
        # every "self.dfs" means a "new start", so it will re-consider the duplicated sub-tree from current node
        # i.e. : self.dfs(root.left, m, res) will re-consider the duplicated sub-tree left node
        path = str(root.val) + '-' + self.dfs(root.left, m, res) + '-' + self.dfs(root.right, m, res)
        ### Notice here
        # if m[path] == 1 means if there is already one element in the m 
        #   -> so res append the path  (if m[path] == 1)
        # becasue it means "deplicate"
        if m[path] == 1:
            res.append(root) 
        m[path] += 1
        return path

# V0'
import collections
# time = O(n)
# space = O(n)
class Solution(object):
    def findDuplicateSubtrees(self, root):
        self.res = []
        self.m = collections.defaultdict(int)
        self.dfs(root)
        return self.res

    def dfs(self, root):
        if not root:
            return ''
        path = str(root.val) + '-' + self.dfs(root.left) + '-' + self.dfs(root.right)
        if self.m[path] == 1:
            self.res.append(root) 
        self.m[path] += 1
        return path

# V0''
# IDEA : DFS
# time = O(n)
# space = O(n)
class Solution(object):
    def findDuplicateSubtrees(self, root):
        count = collections.Counter()
        ans = []
        def dfs(node):
            if not node: return "#"
            serial = "{}-{}-{}".format(node.val, dfs(node.left), dfs(node.right))
            count[serial] += 1
            if count[serial] == 2:
                ans.append(node)
            return serial

        dfs(root)
        return ans

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/81053453
# IDEA : DFS
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
# time = O(n)
# space = O(n)
class Solution(object):
    def findDuplicateSubtrees(self, root):
        """
        :type root: TreeNode
        :rtype: List[TreeNode]
        """
        res = []
        m = collections.defaultdict(int)
        self.helper(root, m, res)
        return res

    def helper(self, root, m, res):
        if not root:
            return '#'
        path = str(root.val) + ',' + self.helper(root.left, m, res) + ',' + self.helper(root.right, m, res)
        ### Notice here
        # if m[path] == 1 means if there is already one element in the m 
        #   -> so res append the path  (if m[path] == 1)
        # becasue it means "deplicate"
        if m[path] == 1:
            res.append(root) 
        m[path] += 1
        return path

### Test case : dev 

# V1'
# IDEA : DFS
# https://leetcode.com/problems/find-duplicate-subtrees/solution/
# time = O(n)
# space = O(n)
class Solution(object):
    def findDuplicateSubtrees(self, root):
        count = collections.Counter()
        ans = []
        def collect(node):
            if not node: return "#"
            serial = "{},{},{}".format(node.val, collect(node.left), collect(node.right))
            count[serial] += 1
            if count[serial] == 2:
                ans.append(node)
            return serial

        collect(root)
        return ans
     
# V1''
# IDEA :  Unique Identifier
# https://leetcode.com/problems/find-duplicate-subtrees/solution/
# time = O(n)
# space = O(n)
class Solution(object):
    def findDuplicateSubtrees(self, root):
        trees = collections.defaultdict()
        trees.default_factory = trees.__len__
        count = collections.Counter()
        ans = []
        def lookup(node):
            if node:
                uid = trees[node.val, lookup(node.left), lookup(node.right)]
                count[uid] += 1
                if count[uid] == 2:
                    ans.append(node)
                return uid
        lookup(root)
        return ans

# V2
# time = O(n)
# space = O(n)
import collections
class Solution(object):
    def findDuplicateSubtrees(self, root):
        """
        :type root: TreeNode
        :rtype: List[TreeNode]
        """
        def getid(root, lookup, trees):
            if root:
                node_id = lookup[root.val, \
                                 getid(root.left, lookup, trees), \
                                 getid(root.right, lookup, trees)]
                trees[node_id].append(root)
                return node_id
        trees = collections.defaultdict(list)
        lookup = collections.defaultdict()
        lookup.default_factory = lookup.__len__
        getid(root, lookup, trees)
        return [roots[0] for roots in trees.values() if len(roots) > 1]

# time = O(n * h)
# space = O(n * h)
class Solution2(object):
    def findDuplicateSubtrees(self, root):
        """
        :type root: TreeNode
        :rtype: List[TreeNode]
        """
        def postOrderTraversal(node, lookup, result):
            if not node:
                return ""
            s = "(" + postOrderTraversal(node.left, lookup, result) + \
                str(node.val) + \
                postOrderTraversal(node.right, lookup, result) + \
                ")"
            if lookup[s] == 1:
                result.append(node)
            lookup[s] += 1
            return s
        lookup = collections.defaultdict(int)
        result = []
        postOrderTraversal(root, lookup, result)
        return result