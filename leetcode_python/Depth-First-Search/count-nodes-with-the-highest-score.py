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
"""

NOTE !!! core idea


### Intuition

For each node:

1. Pretend we remove that node.
2. The tree splits into:

   * One component for each child subtree.
   * One component containing the parent side 
     (everything outside the node's subtree).

3. The node's **score** is:

```text
(child subtree size 1)
× (child subtree size 2)
× ...
× (remaining nodes outside subtree)
```

Example:

```text
parents = [-1, 2, 0, 2, 0]

Tree:

      0
     / \
    2   4
   / \
  1   3
```

For node `2`:

```text
child subtree sizes = 1 (node 1), 1 (node 3)
remaining nodes = 2 (nodes 0 and 4)

score = 1 × 1 × 2 = 2
```

The DFS computes every subtree size exactly once, giving:

* **Time:** O(n)
* **Space:** O(n)

which is required since `n` can be up to `10^5`.


"""
# time = O(n)  # n = number of nodes; single DFS pass
# space = O(n)  # adjacency list + recursion depth
class Solution(object):
    def countHighestScoreNodes(self, parents):
        """
        :type parents: List[int]
        :rtype: int
        """

        # Total number of nodes in the tree
        n = len(parents)

        # Build adjacency list:
        # children[i] will contain all direct children of node i
        """
        NOTE !!!


        why `children = [[] for _ in range(n)]` ?


        ->

        
        children = [
            [],  # children of node 0
            [],  # children of node 1
            [],  # children of node 2
            [],  # children of node 3
            []   # children of node 4
        ]


        ->

        children[node] = list of node's direct children


        ## Iteration 1

            ```python
            i = 0
            parents[0] = -1
            ```

            ```python
            if parents[i] == -1:
                root = i
            ```

            So:

            ```python
            root = 0
            ```

            Meaning node 0 is the root.

            Current state:

            ```python
            children = [[], [], [], [], []]
            ```

            ---

            ## Iteration 2

            ```python
            i = 1
            parents[1] = 2
            ```

            This means:

            ```text
            2 --> 1
            ```

            Node 1 is a child of node 2.

            ```python
            children[2].append(1)
            ```

            Now:

            ```python
            children = [
                [],
                [],
                [1],
                [],
                []
            ]
            ```




        ....


        ->

        Final result:

        ```python
        children = [
            [2, 4],
            [],
            [1, 3],
            [],
            []
        ]
        ```


        -> so we can use the `parent-children` in below DFS

        (children[node] = list of node's direct children)
        

        """
        children = [[] for _ in range(n)]

        # Find root and build child relationships
        root = 0

        for i in range(n):
            if parents[i] == -1:
                # Root node has no parent
                root = i
            else:
                # Add i as a child of its parent
                children[parents[i]].append(i)

        # max_score[0] stores the highest score seen so far
        # Use a list so nested dfs() can modify it
        max_score = [0]

        # count[0] stores how many nodes achieve max_score
        count = [0]

        def dfs(node):
            """
            Returns the size of the subtree rooted at 'node'.

            While returning subtree sizes, also computes the score
            for the current node.
            """

            # Every subtree includes itself
            subtree_size = 1

            # Score starts at 1 because we'll multiply component sizes
            score = 1

            # Process all children
            for child in children[node]:

                # Get size of child's subtree
                child_size = dfs(child)

                # Add child subtree to current subtree
                subtree_size += child_size

                # If current node is removed,
                # this child subtree becomes one component
                score *= child_size

            # Size of the "parent side" component
            # (all nodes outside current subtree)
            remaining = n - subtree_size

            # Only multiply if such component exists
            if remaining > 0:
                score *= remaining

            # Update global maximum score
            if score > max_score[0]:
                max_score[0] = score
                count[0] = 1

            # Another node achieves the same max score
            elif score == max_score[0]:
                count[0] += 1

            # Return subtree size to parent
            return subtree_size

        # Start DFS from root
        dfs(root)

        # Return number of nodes having highest score
        return count[0]



# V1-2
# IDEA: DFS (gemini)
# time = O(n)  # n = number of nodes; one DFS to compute sizes + one linear scan
# space = O(n)  # adjacency list + subtree_size array + recursion depth
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
# time = O(n)  # n = number of nodes; single post-order DFS
# space = O(n)  # adjacency list + recursion depth
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
# time = O(n)  # n = number of nodes; single DFS pass
# space = O(n)  # graph + Counter + recursion depth
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

