"""

1650. Lowest Common Ancestor of a Binary Tree III
Medium

Given two nodes of a binary tree p and q, return their lowest common ancestor (LCA).

Each node will have a reference to its parent node. The definition for Node is below:

class Node {
    public int val;
    public Node left;
    public Node right;
    public Node parent;
}
According to the definition of LCA on Wikipedia: "The lowest common ancestor of two nodes p and q in a tree T is the lowest node that has both p and q as descendants (where we allow a node to be a descendant of itself)."

 

Example 1:


Input: root = [3,5,1,6,2,0,8,null,null,7,4], p = 5, q = 1
Output: 3
Explanation: The LCA of nodes 5 and 1 is 3.
Example 2:


Input: root = [3,5,1,6,2,0,8,null,null,7,4], p = 5, q = 4
Output: 5
Explanation: The LCA of nodes 5 and 4 is 5 since a node can be a descendant of itself according to the LCA definition.
Example 3:

Input: root = [1,2], p = 1, q = 2
Output: 1
 

Constraints:

The number of nodes in the tree is in the range [2, 105].
-109 <= Node.val <= 109
All Node.val are unique.
p != q
p and q exist in the tree.

"""

# V0
# IDEA : HASH MAP
class Solution:
    def lowestCommonAncestor(self, p, q):     
        # help func
        # traverse from the provided node to the root and store the path in a dictionary.
        def traverse(node):
            dic = {}

            while node:
                dic[node.val] = node
                node = node.parent

            return dic
    
        dic1 = traverse(p)
        dic2 = traverse(q)
        
        for key in dic1.keys():
            if key in dic2:
                return dic1[key]

# V0'
# IDEA : recursive
class Solution:
    def lowestCommonAncestor(self, p, q):
        a, b = p, q
        while a != b:
            a = a.parent if a else q
            b = b.parent if b else p
        return a

# V0''
# IDEA : set
class Solution:
    def lowestCommonAncestor(self, p, q):
        visited = set()
        while p:
            visited.add(p)
            p = p.parent
        while q:
            if q in visited:
                return q
            q = q.parent

# V1
# IDEA : HASH MAP
# https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree-iii/discuss/1792941/Python-or-hash-map
"""
# One thing you need first to know is that dictionary in Python is insertion ordered ! This is really important!
# This means if you add 1 then add 2 in a dic and when you traverse this dic, you first get 1 and then get 2, i.e. First In First Get!
# This will make sure we can get the lowest common ancestor (LCA)
# So my intuition is :
    # 1. store the p and q respectively in a dictionary
    # 2. traverse the dictionary of p and the first common key with the dictionary of q is the lowest common ancestor (LCA)
# Note: 
    # 1. Here all Node.val are unique. 
    # 2. As the dictionary is insertion ordered you will always traverse the child at first then its parent. 
    # So this will make sure you find the lowest common ancestor (LCA)
"""
class Solution:
    def lowestCommonAncestor(self, p: 'Node', q: 'Node') -> 'Node':
        dic1 = self.traverse(p)
        dic2 = self.traverse(q)
        
        for key in dic1.keys():
            if key in dic2:
                return dic1[key]
    
    # traverse from the provided node to the root and store the path in a dictionary.
    def traverse(self, node):
        dic = {}
        
        while node:
            dic[node.val] = node
            node = node.parent
        
        return dic

# V1
# IDEA : recursive
# https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree-iii/discuss/1159063/Three-python-solutions
class Solution:
    def lowestCommonAncestor(self, p: 'Node', q: 'Node') -> 'Node':
        a, b = p, q
        while a != b:
            a = a.parent if a else q
            b = b.parent if b else p
        return a

# V1
# IDEA : set
# https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree-iii/discuss/1159063/Three-python-solutions
class Solution:
    def lowestCommonAncestor(self, p: 'Node', q: 'Node') -> 'Node':
        visited = set()
        while p:
            visited.add(p)
            p = p.parent
        while q:
            if q in visited:
                return q
            q = q.parent

# V1
# https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree-iii/discuss/1159063/Three-python-solutions
class Solution:
    def lowestCommonAncestor(self, p: 'Node', q: 'Node') -> 'Node':
        while p:
            p.parent, p = None, p.parent
        while q.parent:
            q = q.parent
        return q

# V1
# https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree-iii/discuss/2002710/Python
class Solution:
    def lowestCommonAncestor(self, p, q):
        ans = set()

        while p:
            ans.add(p)
            p = p.parent

        while q:
            if q in ans:
                return q
            q = q.parent

# V1
# https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree-iii/discuss/933696/Simple-Python-Solution-using-Recursion-%2B-Set()
class Solution(object):
    def lowestCommonAncestor(self, p, q):
        pVals = set()
        def traverse_up(root):
            if root == None or root in pVals:
                return root
            pVals.add(root)
            return traverse_up(root.parent)
            
        return traverse_up(p) or traverse_up(q)

# V1
# https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree-iii/discuss/979616/Python-iterative-solution
class Solution:
    def lowestCommonAncestor(self, p: 'Node', q: 'Node') -> 'Node':
        path = set()
        while p:
            path.add(p)
            p = p.parent 
        while q not in path:
            q = q.parent 
        return q

# V1
# https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree-iii/discuss/951584/Python%3A-Simple-Python-with-set-lookup
class Solution:
    def lowestCommonAncestor(self, p: 'Node', q: 'Node') -> 'Node':
        
        #use a set to store the path from p to root
        path = set()
        
        #helper method
        def storePath(node,path):
            while(node.parent):
                path.add(node)
                node=node.parent
        storePath(p,path)
        
        #now, check for the path from q to root. The first common node is the answer , else root is the ans
        while(q.parent):
            if q not in path:
                q=q.parent
            else:
                return q
        return q

# V2