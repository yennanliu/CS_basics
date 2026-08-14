"""

1718. Construct the Lexicographically Largest Valid Sequence
Medium

Given an integer n, find a sequence with elements in the range [1, n] that satisfies all of the following:

The integer 1 occurs once in the sequence.
Each integer between 2 and n occurs twice in the sequence.
For every integer i between 2 and n, the distance between the two occurrences of i is exactly i.

The distance between two numbers on the sequence, a[i] and a[j], is the absolute difference of their indices, |j - i|.

Return the lexicographically largest sequence. It is guaranteed that under the given constraints, there is always a solution.

A sequence a is lexicographically larger than a sequence b (of the same length) if in the first position where a and b differ, sequence a has a number greater than the corresponding number in b. For example, [0,1,9,0] is lexicographically larger than [0,1,5,6] because the first position they differ is at the third number, and 9 is greater than 5.


Example 1:

Input: n = 3
Output: [3,1,2,3,2]
Explanation: [2,3,2,1,3] is also a valid sequence, but [3,1,2,3,2] is the lexicographically largest valid sequence.

Example 2:

Input: n = 5
Output: [5,3,1,4,3,5,2,4,2]


Constraints:

1 <= n <= 20

"""

# V0
# IDEA : BACKTRACKING, TRY BIG VALUES FIRST (first success = the answer)
#
#   the sequence has length 2n - 1 (value 1 appears once, 2..n appear twice).
#
#   fill positions left to right. at the leftmost EMPTY slot i, try values
#   n, n-1, ..., 1 in DESCENDING order. because we commit greedily to the
#   biggest value that can still be completed, the FIRST full assignment we
#   reach is already the lexicographically largest one - no need to compare
#   candidates afterwards.
#
#   placing v at i also fixes its partner:
#     v >= 2 -> needs slot i + v free and inside the array (v appears twice)
#     v == 1 -> occupies only slot i
#
#   NOTE : a slot may already be filled by an earlier value's partner - then
#          just skip ahead to i + 1 instead of choosing anything.
#
# time = O(n!) worst case (heavily pruned; n <= 20), space = O(n)
class Solution(object):
    def constructDistancedSequence(self, n):
        size = 2 * n - 1
        res = [0] * size
        used = [False] * (n + 1)

        def dfs(i):
            if i == size:
                return True
            if res[i]:
                return dfs(i + 1)
            for v in range(n, 0, -1):
                if used[v]:
                    continue
                if v == 1:
                    used[1] = True
                    res[i] = 1
                    if dfs(i + 1):
                        return True
                    res[i] = 0
                    used[1] = False
                elif i + v < size and res[i + v] == 0:
                    used[v] = True
                    res[i] = res[i + v] = v
                    if dfs(i + 1):
                        return True
                    res[i] = res[i + v] = 0
                    used[v] = False
            return False

        dfs(0)
        return res
