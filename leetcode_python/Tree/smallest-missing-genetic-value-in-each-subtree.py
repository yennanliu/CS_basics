"""

2003. Smallest Missing Genetic Value in Each Subtree
Hard

There is a family tree rooted at 0 consisting of n nodes numbered 0 to n - 1. You are given a 0-indexed integer array parents, where parents[i] is the parent for node i. Since node 0 is the root, parents[0] == -1.

There are 10^5 genetic values, each represented by an integer in the inclusive range [1, 10^5]. You are given a 0-indexed integer array nums, where nums[i] is a distinct genetic value for node i.

Return an array ans of length n where ans[i] is the smallest genetic value that is missing from the subtree rooted at node i.

The subtree rooted at a node x contains node x and all of its descendant nodes.


Example 1:

Input: parents = [-1,0,0,2], nums = [1,2,3,4]
Output: [5,1,1,1]
Explanation: The answer for each subtree is calculated as follows:
- 0: The subtree contains nodes [0,1,2,3] with values [1,2,3,4]. 5 is the smallest missing value.
- 1: The subtree contains only node 1 with value 2. 1 is the smallest missing value.
- 2: The subtree contains nodes [2,3] with values [3,4]. 1 is the smallest missing value.
- 3: The subtree contains only node 3 with value 4. 1 is the smallest missing value.

Example 2:

Input: parents = [-1,0,1,0,3,3], nums = [5,4,6,2,1,3]
Output: [7,1,1,4,2,1]
Explanation: The answer for each subtree is calculated as follows:
- 0: The subtree contains nodes [0,1,2,3,4,5] with values [5,4,6,2,1,3]. 7 is the smallest missing value.
- 1: The subtree contains nodes [1,2] with values [4,6]. 1 is the smallest missing value.
- 2: The subtree contains only node 2 with value 6. 1 is the smallest missing value.
- 3: The subtree contains nodes [3,4,5] with values [2,1,3]. 4 is the smallest missing value.
- 4: The subtree contains only node 4 with value 1. 2 is the smallest missing value.
- 5: The subtree contains only node 5 with value 3. 1 is the smallest missing value.

Example 3:

Input: parents = [-1,2,3,0,2,4,1], nums = [2,3,4,5,6,7,8]
Output: [1,1,1,1,1,1,1]
Explanation: The value 1 is missing from all the subtrees.


Constraints:

n == parents.length == nums.length
2 <= n <= 10^5
0 <= parents[i] <= n - 1 for i != 0
parents[0] == -1
parents represents a valid tree.
1 <= nums[i] <= 10^5
Each nums[i] is distinct.

"""

# V0
# IDEA : WALK UP THE PATH FROM THE NODE HOLDING VALUE 1 (incremental DFS)
#
#   any subtree that does NOT contain the value 1 answers 1.
#   so only the nodes on the root-path of the node holding 1 can answer > 1,
#   and those subtrees are NESTED : each one contains the previous.
#
#   -> start at the node with value 1, climb to the root; at every step add the
#      newly reachable nodes with a DFS that never revisits a marked node, and
#      keep a "seen value" table plus a pointer `miss` that only moves forward.
#      each node is visited once overall, and `miss` moves at most n times.
#
#   NOTE : n can be 10^5, so the DFS is written with an explicit stack.
#
# time = O(n), space = O(n)
class Solution(object):
    def smallestMissingValueSubtree(self, parents, nums):
        n = len(nums)
        res = [1] * n

        start = -1
        g = [[] for _ in range(n)]
        for i in range(1, n):
            g[parents[i]].append(i)
        for i in range(n):
            if nums[i] == 1:
                start = i

        # no node holds value 1 -> every subtree misses 1
        if start == -1:
            return res

        seen = [False] * (n + 2)
        visited = [False] * n
        miss = 2

        node = start
        while node != -1:
            # add every not-yet-visited node of this subtree
            stack = [node]
            while stack:
                cur = stack.pop()
                if visited[cur]:
                    continue
                visited[cur] = True
                if nums[cur] <= n + 1:
                    seen[nums[cur]] = True
                for nxt in g[cur]:
                    if not visited[nxt]:
                        stack.append(nxt)

            while miss <= n + 1 and seen[miss]:
                miss += 1
            res[node] = miss
            node = parents[node]

        return res
