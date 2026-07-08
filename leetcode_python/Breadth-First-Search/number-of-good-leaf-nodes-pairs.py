# https://leetcode.com/problems/number-of-good-leaf-nodes-pairs/description/

"""

1530. Number of Good Leaf Nodes Pairs
Solved
Medium
Topics
premium lock icon
Companies
Hint
You are given the root of a binary tree and an integer distance. A pair of two different leaf nodes of a binary tree is said to be good if the length of the shortest path between them is less than or equal to distance.

Return the number of good leaf node pairs in the tree.

 

Example 1:


Input: root = [1,2,3,null,4], distance = 3
Output: 1
Explanation: The leaf nodes of the tree are 3 and 4 and the length of the shortest path between them is 3. This is the only good pair.
Example 2:


Input: root = [1,2,3,4,5,6,7], distance = 3
Output: 2
Explanation: The good pairs are [4,5] and [6,7] with shortest path = 2. The pair [4,6] is not good because the length of ther shortest path between them is 4.
Example 3:

Input: root = [7,1,4,6,null,5,3,null,null,null,null,null,2], distance = 3
Output: 1
Explanation: The only good pair is [2,5].
 

Constraints:

The number of nodes in the tree is in the range [1, 210].
1 <= Node.val <= 100
1 <= distance <= 10


"""

# V0
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, val=0, left=None, right=None):
#         self.val = val
#         self.left = left
#         self.right = right
class Solution(object):
    def countPairs(self, root, distance):
    	pass


# V0-1
# IDEA: BFS + graph (node <-> parent) (GPT)
from collections import defaultdict, deque

class Solution(object):
    def countPairs(self, root, distance):

        self.graph = defaultdict(list)
        self.leaves = []

        self.build_graph(root, None)

        ans = 0

        for start in self.leaves:

            q = deque([(start, 0)])
            visited = {start}

            while q:
                node, dist = q.popleft()

                if node != start and not node.left and not node.right:
                    ans += 1

                if dist == distance:
                    continue

                for nxt in self.graph[node]:
                    if nxt not in visited:
                        visited.add(nxt)
                        q.append((nxt, dist + 1))

        return ans // 2

    def build_graph(self, node, parent):

        if not node:
            return

        self.graph[node]

        if parent:
            self.graph[node].append(parent)
            self.graph[parent].append(node)

        if not node.left and not node.right:
            self.leaves.append(node)

        self.build_graph(node.left, node)
        self.build_graph(node.right, node)



# V1-1
# IDEA: BFS (gemini)
from collections import deque, defaultdict

class Solution(object):
    def countPairs(self, root, distance):
        """
        :type root: TreeNode
        :type distance: int
        :type rtype: int
        """
        leaves = []
        graph = defaultdict(list)
        
        # Step 1: Traverse the tree to 
        # find all leaves and build an undirected `graph` map
        def build_graph(node, parent=None):
            if not node:
                return
            
            # If it's a `leaf` node, save it to our leaves list
            if not node.left and not node.right:
                leaves.append(node)
                
            # If there is a `parent` connection, make it 
            # `bidirectional` in our graph
            if parent:
                graph[node].append(parent)
                graph[parent].append(node)
                
            build_graph(node.left, node)
            build_graph(node.right, node)
            
        build_graph(root)
        
        cnt = 0
        

        """
        NOTE !!!

        	-> loop over EVERY leaf node,
        	   and run BFS inside
        """
        # Step 2: From each leaf node,
        # run a `limited` `BFS` to 
        # find reachable target `leaves`
        for leaf in leaves:
        	"""
        	NOTE !!!

        	queue:  (cur_node, cur_dist)
        	"""
            queue = deque([(leaf, 0)]) # Holds (current_node, current_distance)
            visited = {leaf}
            
            while queue:
                curr, dist = queue.popleft()
                
                # If we've reached a leaf that isn't our starting leaf, we found a pair!
                if curr != leaf and not curr.left and not curr.right:
                    cnt += 1
                    
                """
                NOTE !!!
                """
                # Explore neighbors if we are still strictly under our distance limit
                if dist < distance:
                    for neighbor in graph[curr]:
                        if neighbor not in visited:
                            visited.add(neighbor)
                            queue.append((neighbor, dist + 1))
                            
        # Since every pair A-B will be discovered twice (once from A, once from B),
        # divide the total count by 2 before returning.
        return cnt // 2


# 1-2
# IDEA: DFS (gemini)
class Solution(object):
    def countPairs(self, root, distance):

        self.ans = 0
        
        """
        NOTE !!!

        ouput of dfs is `list of integers.`

        -> the `distance` from that 
           specific `child node` down 
           to one of its leaf nodes.
        """
        def dfs(node):
            if not node:
                return []
            
            # Base Case: If it's a leaf node, return a list containing a distance of 0
            if not node.left and not node.right:
                return [0]
            
            # Post-Order Traversal: Go deep into left and right subtrees first
            left_distances = dfs(node.left)
            right_distances = dfs(node.right)
            
            # Count valid pairs that cross through the current node (acting as the LCA)
            for l in left_distances:
                for r in right_distances:
                    # +2: for the `1` edge from left leaf to current node
                    #     plus the `1` edge from current node to right leaf
                    #
                    #     -> so that's why `+2`
                    if l + r + 2 <= distance:
                        self.ans += 1
            
            # Increment distances by 1 to account for the edge up to the parent node
            parent_distances = []
            for d in left_distances + right_distances:
                if d + 1 < distance:  # Optimization: drop paths that already exceed the limit
                    parent_distances.append(d + 1)
                    
            return parent_distances

        dfs(root)
        return self.ans


# 2-1
# IDEA: Graph Conversion + BFS
# https://leetcode.com/problems/number-of-good-leaf-nodes-pairs/editorial/
class Solution:
    def _traverse_tree(self, curr_node, prev_node, graph, leaf_nodes):
        if curr_node is None:
            return
        if curr_node.left is None and curr_node.right is None:
            leaf_nodes.add(curr_node)
        if prev_node is not None:
            if prev_node not in graph:
                graph[prev_node] = []
            graph[prev_node].append(curr_node)

            if curr_node not in graph:
                graph[curr_node] = []
            graph[curr_node].append(prev_node)

        self._traverse_tree(curr_node.left, curr_node, graph, leaf_nodes)
        self._traverse_tree(curr_node.right, curr_node, graph, leaf_nodes)

    def countPairs(self, root, distance):
        graph = {}
        leaf_nodes = set()

        self._traverse_tree(root, None, graph, leaf_nodes)

        ans = 0

        for leaf in leaf_nodes:
            bfs_queue = []
            seen = set()
            bfs_queue.append(leaf)
            seen.add(leaf)
            for i in range(distance + 1):
                # Clear all nodes in the queue (distance i away from leaf node)
                # Add the nodes' neighbors (distance i+1 away from leaf node)
                size = len(bfs_queue)
                for j in range(size):
                    curr_node = bfs_queue.pop(0)
                    if curr_node in leaf_nodes and curr_node != leaf:
                        ans += 1
                    if curr_node in graph:
                        for neighbor in graph.get(curr_node):
                            if neighbor not in seen:
                                bfs_queue.append(neighbor)
                                seen.add(neighbor)
        return ans // 2



# 2-2
# IDEA: `Post`-Order Traversal
# https://leetcode.com/problems/number-of-good-leaf-nodes-pairs/editorial/
class Solution:
    def _post_order(self, current_node, distance):
        if current_node is None:
            return [0] * 12
        elif current_node.left is None and current_node.right is None:
            current = [0] * 12
            # Leaf node's distance from itself is 0
            current[0] = 1
            return current

        # Leaf node count for a given distance i
        left = self._post_order(current_node.left, distance)
        right = self._post_order(current_node.right, distance)

        current = [0] * 12

        # Combine the counts from the left and right subtree and shift by
        # +1 distance
        for i in range(10):
            current[i + 1] += left[i] + right[i]

        # Initialize to total number of good leaf nodes pairs from left and right subtrees.
        current[11] = left[11] + right[11]

        # Iterate through possible leaf node distance pairs
        for d1 in range(distance + 1):
            for d2 in range(distance + 1):
                if 2 + d1 + d2 <= distance:
                    # If the total path distance is less than the given distance limit,
                    # then add to he total number of good pairs
                    current[11] += left[d1] * right[d2]

        return current

    def countPairs(self, root: TreeNode, distance: int) -> int:
        return self._post_order(root, distance)[11]




# 2-3
# IDEA: `Post`-Order Traversal With Prefix Sum Counting
# https://leetcode.com/problems/number-of-good-leaf-nodes-pairs/editorial/
class Solution:
    def _post_order(self, current_node, distance):
        if current_node is None:
            return [0] * 12
        elif current_node.left is None and current_node.right is None:
            current = [0] * 12
            # Leaf node's distance from itself is 0
            current[0] = 1
            return current

        # Leaf node count for a given distance i
        left = self._post_order(current_node.left, distance)
        right = self._post_order(current_node.right, distance)

        current = [0] * 12

        # Combine the counts from the left and right subtree and shift by
        # +1 distance
        for i in range(10):
            current[i + 1] += left[i] + right[i]

        # Initialize to total number of good leaf nodes pairs from left and right subtrees.
        current[11] = left[11] + right[11]

        # Count all good leaf node distance pairs
        prefix_sum = 0
        i = 0
        for d2 in range(distance - 2, -1, -1):
            prefix_sum += left[i]
            current[11] += prefix_sum * right[d2]
            i += 1

        return current

    def countPairs(self, root: TreeNode, distance: int) -> int:
        return self._post_order(root, distance)[11]







        

