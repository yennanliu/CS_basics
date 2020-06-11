# V0
# IDEA : GRAPH + BFS  
# https://www.youtube.com/watch?v=x1wXkRrpavw
# https://blog.csdn.net/qq_17550379/article/details/87778889
import collections
class Solution:
    # search via DFS
    def findClosestLeaf(self, root, k):
        self.start = None
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

# V1 
# http://bookshadow.com/weblog/2017/12/10/leetcode-closest-leaf-in-a-binary-tree/
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
class Solution(object):
    def findClosestLeaf(self, root, k):
        """
        :type root: TreeNode
        :type k: int
        :rtype: int
        """
        parents = {}
        leaves = []
        self.knode = None
        def traverse(root):
            if root.val == k: self.knode = root
            if not root.left and not root.right:
                leaves.append(root)
                return
            for child in (root.left, root.right):
                if not child: continue
                traverse(child)
                parents[child.val] = root
        def findParents(node):
            ans = [node.val]
            while node.val in parents:
                node = parents[node.val]
                ans.append(node.val)
            return ans
        traverse(root)
        kParents = findParents(self.knode)
        ans, dist = None, 0x7FFFFFFF
        for leaf in leaves:
            leafParents = findParents(leaf)
            cross = [n for n in leafParents if n in kParents][0]
            ndist = leafParents.index(cross) + kParents.index(cross)
            if ndist < dist:
                dist = ndist
                ans = leaf
        return ans.val

### Test case : dev 

# V1'
# https://www.jiuzhang.com/solution/closest-leaf-in-a-binary-tree/#tag-highlight-lang-python
"""
Definition of TreeNode:
class TreeNode:
    def __init__(self, val):
        self.val = val
        self.left, self.right = None, None
"""
class Solution:
    """
    @param root: the root
    @param k: an integer
    @return: the value of the nearest leaf node to target k in the tree
    """
    def findClosestLeaf(self, root, k):
        parents = {}
        pValK = self._dfs(root, k, parents)
        
        q = [pValK]
        vis = [pValK]
        
        while len(q) != 0:
            if q[0].left == None and q[0].right == None:
                return q[0].val
            
            if q[0].left != None and (not q[0].left in vis):
                q.append(q[0].left)
                vis.append(q[0].left)
                
            if q[0].right != None and (not q[0].right in vis):
                q.append(q[0].right)
                vis.append(q[0].right)
             
            if q[0] in parents and (not parents[q[0]] in vis): 
                q.append(parents[q[0]])
                vis.append(parents[q[0]])
            
            q.pop(0)
        
        return 0
            
    def _dfs(self, rt, k, parents):
        res = rt
        tmp = None
        
        if rt.left != None:
            parents[rt.left] = rt
            tmp = self._dfs(rt.left, k, parents)
            if tmp.val == k:
                res = tmp
            
        if rt.right != None:
            parents[rt.right] = rt
            tmp = self._dfs(rt.right, k, parents)
            if tmp.val == k:
                res = tmp      
        return res

# V1''
# https://blog.csdn.net/qq_17550379/article/details/87778889
# https://coordinate.wang/index.php/archives/2057/
# IDEA : BFS + GRAPH 
import collections
class Solution:
    def findClosestLeaf(self, root: 'List[TreeNode]', k: 'int') -> 'int':
        self.start = None
        self.buildGraph(root, None, k)
        q, visited = [root], set()
        self.graph = collections.defaultdict(list)
        while q:
            for i in range(len(q)):
                cur = q.pop(0)
                visited.add(cur)
                if not cur.left and not cur.right:
                    return cur.val
                for node in self.graph:
                    if node in visited:
                        q.append(node)

    def buildGraph(self, node, parent, k):
        if not node:
            return

        if node.val == k:
            self.start = node
        if parent:
            self.graph[node].append(parent)
            self.graph[parent].append(node)
        self.buildGraph(node.left, node, k)
        self.buildGraph(node.right, node, k)

# V1'''
# http://zxi.mytechroad.com/blog/tree/742-closest-leaf-in-a-binary-tree/
# C++
# // Author: Huahua
# // Runtime: 16 ms
# class Solution {
# public:
#     int findClosestLeaf(TreeNode* root, int k) {
#         graph_.clear();
#         start_ = nullptr;
#         buildGraph(root, nullptr, k);
#         queue<TreeNode*> q;
#         q.push(start_);
#         unordered_set<TreeNode*> seen;
#         while (!q.empty()) {
#             int size = q.size();
#             while (size-->0) {
#                 TreeNode* curr = q.front();
#                 q.pop();                
#                 seen.insert(curr);                
#                 if (!curr->left && !curr->right) return curr->val;
#                 for (TreeNode* node : graph_[curr])
#                     if (!seen.count(node)) q.push(node);
#             }
#         }
#         return 0;
#     }
# private:
#     void buildGraph(TreeNode* node, TreeNode* parent, int k) {
#         if (!node) return;
#         if (node->val == k) start_ = node;
#         if (parent) {
#             graph_[node].push_back(parent);
#             graph_[parent].push_back(node);
#         }
#         buildGraph(node->left, node, k);
#         buildGraph(node->right, node, k);
#     }
    
#     unordered_map<TreeNode*, vector<TreeNode*>> graph_;
#     TreeNode* start_;
# };

# V2 
# Time:  O(n)
# Space: O(n)
import collections
class Solution(object):
    def findClosestLeaf(self, root, k):
        """
        :type root: TreeNode
        :type k: int
        :rtype: int
        """
        def traverse(node, neighbors, leaves):
            if not node:
                return
            if not node.left and not node.right:
                leaves.add(node.val)
                return
            if node.left:
                neighbors[node.val].append(node.left.val)
                neighbors[node.left.val].append(node.val)
                traverse(node.left, neighbors, leaves)
            if node.right:
                neighbors[node.val].append(node.right.val)
                neighbors[node.right.val].append(node.val)
                traverse(node.right, neighbors, leaves)

        neighbors, leaves = collections.defaultdict(list), set()
        traverse(root, neighbors, leaves)
        q, lookup = [k], set([k])
        while q:
            next_q = []
            for u in q:
                if u in leaves:
                    return u
                for v in neighbors[u]:
                    if v in lookup:
                        continue
                    lookup.add(v)
                    next_q.append(v)
            q = next_q
        return 0
