# https://leetcode.com/problems/count-nodes-with-the-highest-score/description/

"""
2049. Count Nodes With the Highest Score
Medium
Topics
premium lock icon
Companies
Hint
There is a binary tree rooted at 0 consisting of n nodes. The nodes are labeled from 0 to n - 1. You are given a 0-indexed integer array parents representing the tree, where parents[i] is the parent of node i. Since node 0 is the root, parents[0] == -1.

Each node has a score. To find the score of a node, consider if the node and the edges connected to it were removed. The tree would become one or more non-empty subtrees. The size of a subtree is the number of the nodes in it. The score of the node is the product of the sizes of all those subtrees.

Return the number of nodes that have the highest score.

 

Example 1:

example-1
Input: parents = [-1,2,0,2,0]
Output: 3
Explanation:
- The score of node 0 is: 3 * 1 = 3
- The score of node 1 is: 4 = 4
- The score of node 2 is: 1 * 1 * 2 = 2
- The score of node 3 is: 4 = 4
- The score of node 4 is: 4 = 4
The highest score is 4, and three nodes (node 1, node 3, and node 4) have the highest score.
Example 2:

example-2
Input: parents = [-1,2,0]
Output: 2
Explanation:
- The score of node 0 is: 2 = 2
- The score of node 1 is: 2 = 2
- The score of node 2 is: 1 * 1 = 1
The highest score is 2, and two nodes (node 0 and node 1) have the highest score.
 

Constraints:

n == parents.length
2 <= n <= 105
parents[0] == -1
0 <= parents[i] <= n - 1 for i != 0
parents represents a valid binary tree.

"""

# V0
# class Solution(object):
#     def countHighestScoreNodes(self, parents):
#         """
#         :type parents: List[int]
#         :rtype: int
#         """

# V1-1
# IDEA: DFS (gpt)
class Solution(object):
    def countHighestScoreNodes(self, parents):
        """
        :type parents: List[int]
        :rtype: int
        """
        n = len(parents)

        children = [[] for _ in range(n)]
        root = 0

        for i in range(n):
            if parents[i] == -1:
                root = i
            else:
                children[parents[i]].append(i)

        max_score = [0]
        count = [0]

        def dfs(node):
            subtree_size = 1
            score = 1

            for child in children[node]:
                child_size = dfs(child)
                subtree_size += child_size
                score *= child_size

            remaining = n - subtree_size
            if remaining > 0:
                score *= remaining

            if score > max_score[0]:
                max_score[0] = score
                count[0] = 1
            elif score == max_score[0]:
                count[0] += 1

            return subtree_size

        dfs(root)
        return count[0]


# V1-2
# IDEA: DFS (gemini)
class Solution(object):
    def countHighestScoreNodes(self, parents):
        """
        :type parents: List[int]
        :rtype: int
        """
        n = len(parents)
        
        # 1. Build an adjacency tree mapping (parent -> list of children)
        tree = [[] for _ in range(n)]
        for child_idx, parent_idx in enumerate(parents):
            if parent_idx != -1:
                tree[parent_idx].append(child_idx)
                
        # Array to store the pre-calculated subtree size for each node
        subtree_size = [0] * n
        
        # --- STAGE 1: Pure Subtree Size Calculator ---
        def calculate_sizes(node):
            size = 1
            for child in tree[node]:
                size += calculate_sizes(child)
            subtree_size[node] = size
            return size
            
        # Run size calculations starting from the root (0)
        calculate_sizes(0)
        
        # --- STAGE 2: Pure Sequential Score Evaluation Loop ---
        max_score = 0
        max_score_count = 0
        
        for i in range(n):
            current_score = 1
            total_children_size = 0
            
            # Multiply the size of each child's subtree
            for child in tree[i]:
                child_size = subtree_size[child]
                current_score *= child_size
                total_children_size += child_size
                
            # The size of the parent/above component left over
            above_size = n - 1 - total_children_size
            if above_size > 0:
                current_score *= above_size
                
            # Update running maxima benchmarks
            if current_score > max_score:
                max_score = current_score
                max_score_count = 1
            elif current_score == max_score:
                max_score_count += 1
                
        return max_score_count


# V1-3
# IDEA: TREE DP (gemini)
class Solution(object):
    def countHighestScoreNodes(self, parents):
        """
        :type parents: List[int]
        :rtype: int
        """
        n = len(parents)
        
        # Step 1: Reconstruct the tree layout as an adjacency list mapping: parent -> [children]
        tree = [[] for _ in range(n)]
        for child_idx, parent_idx in enumerate(parents):
            if parent_idx != -1:
                tree[parent_idx].append(child_idx)
                
        # State variables to keep track of highest score benchmarks
        self.max_score = 0
        self.max_score_count = 0
        
        # Step 2: Define a post-order traversal function that computes subtree node counts
        def dfs(node):
            total_subtree_nodes = 1
            current_node_score = 1
            
            # Look at all children of the current node
            for child in tree[node]:
                child_subtree_size = dfs(child)
                # Add child subtree size to current node's total size accumulation
                total_subtree_nodes += child_subtree_size
                # Multiply component sizes for the score tracking
                current_node_score *= child_subtree_size
                
            # Step 3: Compute the "above/parent" component size (everything outside this subtree)
            above_component_size = n - total_subtree_nodes
            if above_component_size > 0:
                current_node_score *= above_component_size
                
            # Step 4: Compare results against global benchmarks
            if current_node_score > self.max_score:
                self.max_score = current_node_score
                self.max_score_count = 1
            elif current_node_score == self.max_score:
                self.max_score_count += 1
                
            # Return total node size of this subtree back to its parent caller
            return total_subtree_nodes

        # Kick off DFS starting at the root node (0)
        dfs(0)
        
        return self.max_score_count



# V2
class Solution:
    def countHighestScoreNodes(self, parents):
        graph = collections.defaultdict(list)
        for node, parent in enumerate(parents):  # build graph
            graph[parent].append(node)
        n = len(parents)                         # total number of nodes
        d = collections.Counter()
        def count_nodes(node):                   # number of children node + self
            p, s = 1, 0                          # p: product, s: sum
            for child in graph[node]:            # for each child (only 2 at maximum)
                res = count_nodes(child)         # get its nodes count
                p *= res                         # take the product
                s += res                         # take the sum
            p *= max(1, n - 1 - s)               # times up-branch (number of nodes other than left, right children ans itself)
            d[p] += 1                            # count the product
            return s + 1                         # return number of children node + 1 (self)
        count_nodes(0)                           # starting from root (0)
        return d[max(d.keys())]                  # return max count

