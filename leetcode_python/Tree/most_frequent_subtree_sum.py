"""

508. Most Frequent Subtree Sum
Medium

Given the root of a binary tree, return the most frequent subtree sum. If there is a tie, return all the values with the highest frequency in any order.

The subtree sum of a node is defined as the sum of all the node values formed by the subtree rooted at that node (including the node itself).

 
Example 1:


Input: root = [5,2,-3]
Output: [2,-3,4]
Example 2:


Input: root = [5,2,-5]
Output: [2]
 

Constraints:

The number of nodes in the tree is in the range [1, 104].
-105 <= Node.val <= 105

"""

# V0
# IDEA : DFS + TREE
class Solution(object):
    def findFrequentTreeSum(self, root):

        ### NOTE : this trick : get sum of sub tree 
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
# IDEA : DFS + TREE
class Solution(object):
    def findFrequentTreeSum(self, root):
        if not root: return []
        vals = []
        ### NOTE : this trick : get sum of sub tree 
        def getSum(root):
            if not root:
                return 0
            s = getSum(root.left) + root.val + getSum(root.right)
            vals.append(s)
            # remember to return s 
            return s
        getSum(root)
        count = collections.Counter(vals)
        frequent = max(count.values())
        return [x for x, v in count.items() if v == frequent]

# V1
# https://blog.csdn.net/fuxuemingzhu/article/details/79435381
# IDEA : TREE
class Solution(object):
    def findFrequentTreeSum(self, root):
        """
        :type root: TreeNode
        :rtype: List[int]
        """
        if not root: return []
        vals = []
        def getSum(root):
            if not root:
                return 0
            s = getSum(root.left) + root.val + getSum(root.right)
            vals.append(s)
            # remember to return s 
            return s
        getSum(root)
        count = collections.Counter(vals)
        frequent = max(count.values())
        return [x for x, v in count.items() if v == frequent]

### Tese case : dev
# s=Solution()
# assert s.findFrequentTreeSum([]) == []
# assert s.findFrequentTreeSum([5,2,-3]) == [2,-3,4]
# assert s.findFrequentTreeSum([0,1,2]) == [0,1,2]

# V1'
# https://leetcode.com/problems/most-frequent-subtree-sum/discuss/98675/JavaC%2B%2BPython-DFS-Find-Subtree-Sum
# IDEA : TREE
class Solution(object):
    def findFrequentTreeSum(self, root):
        if root is None: return []

        def dfs(node):
            if node is None: return 0
            s = node.val + dfs(node.left) + dfs(node.right)
            count[s] += 1
            return s

        count = collections.Counter()
        dfs(root)
        maxCount = max(count.values())
        return [s for s in count if count[s] == maxCount]

# V1''
# https://leetcode.com/problems/most-frequent-subtree-sum/discuss/98749/Python-clean-solution-beats-97
# IDEA : TREE
class Solution(object):
    def findFrequentTreeSum(self, root):
        """
        :type root: TreeNode
        :rtype: List[int]
        """
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
        for key in d:
            if d[key] > mostFreq:
                mostFreq = d[key]
                ans = [key]
            elif d[key] == mostFreq:
                ans.append(key)
        return ans

# V1''''
# https://www.jianshu.com/p/c861361dc20f
# IDEA : TREE
import collections
class TreeNode(object):
    def __init__(self, x):
        self.val = x
        self.left = None
        self.right = None

class Solution(object):
    def findFrequentTreeSum(self, root):
        """
        :type root: TreeNode
        :rtype: List[int]
        """
        if not root:
            return []
        self.counter = collections.Counter()
        self.postOrderTraverse(root)
        maxValue = max(self.counter.values())
        return [key for key in self.counter.keys() if self.counter[key] == maxValue]

    def postOrderTraverse(self, node):
        if node.left:
            node.val += self.postOrderTraverse(node.left)
        if node.right:
            node.val += self.postOrderTraverse(node.right)
        self.counter[node.val] += 1
        return node.val

# V2