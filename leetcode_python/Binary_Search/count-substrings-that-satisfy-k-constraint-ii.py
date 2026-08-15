"""

3261. Count Substrings That Satisfy K-Constraint II
Hard

You are given a binary string s and an integer k.

You are also given a 2D integer array queries, where queries[i] = [l_i, r_i].

A binary string satisfies the k-constraint if either of the following conditions holds:

The number of 0's in the string is at most k.
The number of 1's in the string is at most k.

Return an integer array answer, where answer[i] is the number of substrings of s[l_i..r_i] that satisfy the k-constraint.


Example 1:

Input: s = "0001111", k = 2, queries = [[0,6]]
Output: [26]
Explanation:
For the query [0, 6], all substrings of s[0..6] = "0001111" satisfy the k-constraint except for the substrings s[0..5] = "000111" and s[0..6] = "0001111".

Example 2:

Input: s = "010101", k = 1, queries = [[0,5],[1,4],[2,5],[0,2],[3,5]]
Output: [15,9,11,5,5]
Explanation:
The substrings of s with a length greater than 3 do not satisfy the k-constraint.


Constraints:

1 <= s.length <= 10^5
1 <= k <= s.length
1 <= queries.length <= 10^5
queries[i] == [l_i, r_i]
0 <= l_i <= r_i < s.length
All queries are distinct.

"""

# V0
# IDEA : PRECOMPUTE THE SHORTEST VALID LEFT END PER RIGHT END, THEN PREFIX SUM
#
#   for each right index j let left[j] be the smallest start such that
#   s[left[j]..j] still satisfies the constraint. the two-pointer sweep of
#   LC 3258 produces it, and left is NON-DECREASING because shrinking the
#   window can only relax the counts.
#
#   a query [l, r] then wants, for every j in [l, r],
#       j - max(l, left[j]) + 1
#   and left's monotonicity splits that at one point : left[j] < l on a
#   prefix of the range and left[j] >= l afterwards.
#
#       prefix part : j - l + 1, an arithmetic series in closed form
#       suffix part : j - left[j] + 1, answered by a prefix-sum array
#
#   so binary search that split point once per query and read both parts in
#   O(1).
#
# time = O(n + q log n), space = O(n)
import bisect


class Solution(object):
    def countKConstraintSubstrings(self, s, k, queries):
        n = len(s)
        left = [0] * n
        zeros = ones = 0
        lo = 0
        for j, ch in enumerate(s):
            if ch == '0':
                zeros += 1
            else:
                ones += 1
            while zeros > k and ones > k:
                if s[lo] == '0':
                    zeros -= 1
                else:
                    ones -= 1
                lo += 1
            left[j] = lo

        # prefix sums of (j - left[j] + 1)
        pre = [0] * (n + 1)
        for j in range(n):
            pre[j + 1] = pre[j] + (j - left[j] + 1)

        res = []
        for l, r in queries:
            # first index in [l, r] whose window already starts at or after l
            split = bisect.bisect_left(left, l, l, r + 1)
            total = 0
            if split > l:                       # j in [l, split-1] : capped by l
                cnt = split - l
                total += (1 + cnt) * cnt // 2   # lengths 1, 2, ..., cnt
            if split <= r:
                total += pre[r + 1] - pre[split]
            res.append(total)
        return res
