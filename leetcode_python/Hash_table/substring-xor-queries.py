"""

2564. Substring XOR Queries
Medium

You are given a binary string s, and a 2D integer array queries where queries[i] = [firsti, secondi].

For the ith query, find the shortest substring of s whose decimal value, val, yields secondi when bitwise XORed with firsti. In other words, val ^ firsti == secondi.

The answer to the ith query is the endpoints (0-indexed) of the substring [lefti, righti] or [-1, -1] if no such substring exists. If there are multiple answers, choose the one with the minimum lefti.

Return an array ans where ans[i] = [lefti, righti] is the answer to the ith query.

A substring is a contiguous non-empty sequence of characters within a string.


Example 1:

Input: s = "101101", queries = [[0,5],[1,2]]
Output: [[0,2],[2,3]]
Explanation: For the first query the substring in range [0,2] is "101" which has a decimal value of 5, and 5 ^ 0 = 5, hence the answer to the first query is [0,2]. In the second query, the substring in range [2,3] is "11", and has a decimal value of 3, and 3 ^ 1 = 2. So, [2,3] is returned for the second query.

Example 2:

Input: s = "0101", queries = [[12,8]]
Output: [[-1,-1]]
Explanation: In this example there is no substring that answers the query, hence [-1,-1] is returned.

Example 3:

Input: s = "1", queries = [[4,5]]
Output: [[0,0]]
Explanation: For this example, the substring in range [0,0] has a decimal value of 1, and 1 ^ 4 = 5. So, the answer is [0,0].


Constraints:

1 <= s.length <= 10^4
s[i] is either '0' or '1'.
1 <= queries.length <= 10^5
0 <= firsti, secondi <= 10^9

"""

# V0
# IDEA : HASH TABLE PRE-COMPUTATION OVER SHORT WINDOWS + XOR IDENTITY
#
#   val ^ first == second  <=>  val == first ^ second, so every query reduces
#   to "where is the shortest substring whose binary value equals target?".
#
#   first/second < 2^30, so the target fits in 32 bits and any USEFUL
#   substring has length <= 32. we therefore precompute, for every start i,
#   the values of the substrings s[i..i+31], and record the FIRST (i, j)
#   seen for each value in a dict.
#
#   NOTE : the canonical (shortest) representation of a non-zero value has no
#          leading zero, so once x hits 0 we break out — a run of '0's only
#          ever contributes the single-character substring "0" (value 0), and
#          any longer zero-padded form is strictly longer than the version
#          starting at the first '1'.
#   NOTE : scanning i ascending and inserting only when the key is absent
#          gives, for each value, the smallest left endpoint; all substrings
#          representing the same value (without leading zeros) share the same
#          length, so "first seen" is both shortest and leftmost.
#
# time = O(n * 32 + m), space = O(n * 32)
class Solution(object):
    def substringXorQueries(self, s, queries):
        n = len(s)
        first_pos = {}
        for i in range(n):
            x = 0
            for j in range(i, min(n, i + 32)):
                x = (x << 1) | (1 if s[j] == '1' else 0)
                if x not in first_pos:
                    first_pos[x] = [i, j]
                if x == 0:
                    break
        return [first_pos.get(a ^ b, [-1, -1]) for a, b in queries]
