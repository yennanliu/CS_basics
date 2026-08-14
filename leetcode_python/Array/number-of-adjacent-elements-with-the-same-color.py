"""

2672. Number of Adjacent Elements With the Same Color
Medium

You are given an integer n representing an array colors of length n where all elements are set to 0's meaning uncolored. You are also given a 2D integer array queries where queries[i] = [indexi, colori]. For the ith query:

Set colors[indexi] to colori.
Count the number of adjacent pairs in colors which have the same color (regardless of colori).

Return an array answer of the same length as queries where answer[i] is the answer to the ith query.


Example 1:

Input: n = 4, queries = [[0,2],[1,2],[3,1],[1,1],[2,1]]
Output: [0,1,1,0,2]
Explanation:
Initially array colors = [0,0,0,0], where 0 denotes uncolored elements of the array.
After the 1st query colors = [2,0,0,0]. The count of adjacent pairs with the same color is 0.
After the 2nd query colors = [2,2,0,0]. The count of adjacent pairs with the same color is 1.
After the 3rd query colors = [2,2,0,1]. The count of adjacent pairs with the same color is 1.
After the 4th query colors = [2,1,0,1]. The count of adjacent pairs with the same color is 0.
After the 5th query colors = [2,1,1,1]. The count of adjacent pairs with the same color is 2.

Example 2:

Input: n = 1, queries = [[0,100000]]
Output: [0]
Explanation:
After the 1st query colors = [100000]. The count of adjacent pairs with the same color is 0.


Constraints:

1 <= n <= 10^5
1 <= queries.length <= 10^5
queries[i].length == 2
0 <= indexi <= n - 1
1 <= colori <= 10^5

"""

# V0
# IDEA : INCREMENTAL COUNTER -- ONLY THE 2 PAIRS TOUCHING `index` CHANGE
#
#   recounting all n-1 adjacent pairs per query would be O(n * q) = 10^10.
#   But painting one cell can only affect the pair to its LEFT and the pair
#   to its RIGHT, so keep a running total `same` and patch it in O(1):
#
#     1) REMOVE the contribution of the old value at i:
#          if colors[i-1] == colors[i] -> same -= 1
#          if colors[i+1] == colors[i] -> same -= 1
#     2) write colors[i] = c
#     3) ADD the contribution of the new value:
#          if colors[i-1] == c -> same += 1
#          if colors[i+1] == c -> same += 1
#
#   NOTE : uncolored cells are 0 and colori >= 1, so 0 is a safe "no colour"
#          sentinel -- but step 1 MUST also require colors[i] != 0, otherwise
#          two neighbouring uncoloured cells would be counted as a matching
#          pair and `same` would go negative.
#          Step 3 needs no such guard because c >= 1 always.
#
#   NOTE : order matters -- the removal reads the OLD colors[i], so the
#          write has to sit between the two adjustments.
#
#   NOTE : the answer is recorded AFTER the write for this query, and the
#          state carries over to the next query (queries are cumulative).
#
# time = O(n + q), space = O(n)
class Solution(object):
    def colorTheArray(self, n, queries):
        colors = [0] * n
        res = []
        same = 0
        for i, c in queries:
            cur = colors[i]
            if cur:
                if i > 0 and colors[i - 1] == cur:
                    same -= 1
                if i < n - 1 and colors[i + 1] == cur:
                    same -= 1
            colors[i] = c
            if i > 0 and colors[i - 1] == c:
                same += 1
            if i < n - 1 and colors[i + 1] == c:
                same += 1
            res.append(same)
        return res
