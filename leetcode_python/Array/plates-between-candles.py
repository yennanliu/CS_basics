"""

2055. Plates Between Candles
Medium

There is a long table with a line of plates and candles arranged on top of it. You are given a 0-indexed string s consisting of characters '*' and '|' only, where a '*' represents a plate and a '|' represents a candle.

You are also given a 0-indexed 2D integer array queries where queries[i] = [left_i, right_i] denotes the substring s[left_i...right_i] (inclusive). For each query, you need to find the number of plates between candles that are in the substring. A plate is considered between candles if there is at least one candle to its left and at least one candle to its right in the substring.

For example, s = "||**||**|*", and a query [3, 8] denotes the substring "*||**|". The number of plates between candles in this substring is 2, as each of the two plates has at least one candle in the substring to its left and right.

Return an integer array answer where answer[i] is the answer to the ith query.


Example 1:

Input: s = "**|**|***|", queries = [[2,5],[5,9]]
Output: [2,3]
Explanation:
- queries[0] has two plates between candles.
- queries[1] has three plates between candles.

Example 2:

Input: s = "***|**|*****|**||**|*", queries = [[1,17],[4,5],[14,17],[5,11],[15,16]]
Output: [9,0,0,0,0]
Explanation:
- queries[0] has nine plates between candles.
- The other queries have zero plates between candles.


Constraints:

3 <= s.length <= 10^5
s consists of '*' and '|' characters.
1 <= queries.length <= 10^5
queries[i].length == 2
0 <= left_i <= right_i < s.length

"""

# V0
# IDEA : PREFIX SUM OF PLATES + NEAREST-CANDLE ARRAYS (O(1) per query)
#
#   pre[i]  = number of '*' in s[0..i-1]
#   right[i]= index of the nearest candle at or AFTER  i (-1 if none)
#   left[i] = index of the nearest candle at or BEFORE i (-1 if none)
#
#   for a query [l, r] the useful window is [ right[l] .. left[r] ]:
#   the first candle inside on the left and the last candle inside on the
#   right. every plate strictly between them qualifies ->
#       pre[left[r]] - pre[right[l] + 1]
#
#   NOTE : if either candle is missing, or they cross (i >= j), answer is 0.
#
# time = O(n + q), space = O(n)
class Solution(object):
    def platesBetweenCandles(self, s, queries):
        n = len(s)
        pre = [0] * (n + 1)
        for i, c in enumerate(s):
            pre[i + 1] = pre[i] + (1 if c == '*' else 0)

        left = [-1] * n
        j = -1
        for i in range(n):
            if s[i] == '|':
                j = i
            left[i] = j

        right = [-1] * n
        j = -1
        for i in range(n - 1, -1, -1):
            if s[i] == '|':
                j = i
            right[i] = j

        res = []
        for l, r in queries:
            i, j = right[l], left[r]
            if i >= 0 and j >= 0 and i < j:
                res.append(pre[j] - pre[i + 1])
            else:
                res.append(0)
        return res
