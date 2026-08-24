"""

2052. Minimum Cost to Separate Sentence Into Rows
Medium

You are given a string sentence containing words separated by spaces, and an integer k. Your task is to separate sentence into rows where the number of characters in each row is at most k. You may assume that sentence does not begin or end with a space, and the words in sentence are separated by a single space.

You can split sentence into rows by inserting line breaks between words in sentence. A word cannot be split between two rows. Each word must be used exactly once, and the word order cannot be rearranged. Adjacent words in a row should be separated by a single space, and rows should not begin or end with spaces.

The cost of a row with length n is (k - n)^2, and the total cost is the sum of the costs for all rows except the last one.

For example if sentence = "i love leetcode" and k = 12:

Separating sentence into "i", "love", and "leetcode" has a cost of (12 - 1)^2 + (12 - 4)^2 = 185.
Separating sentence into "i love", and "leetcode" has a cost of (12 - 6)^2 = 36.
Separating sentence into "i", and "love leetcode" is not possible because the length of "love leetcode" is greater than k.

Return the minimum possible total cost of separating sentence into rows.


Example 1:

Input: sentence = "i love leetcode", k = 12
Output: 36
Explanation:
Separating sentence into "i", "love", and "leetcode" has a cost of (12 - 1)^2 + (12 - 4)^2 = 185.
Separating sentence into "i love", and "leetcode" has a cost of (12 - 6)^2 = 36.
Separating sentence into "i", "love leetcode" is not possible because "love leetcode" has length 13.
36 is the minimum possible total cost so return it.

Example 2:

Input: sentence = "apples and bananas taste great", k = 7
Output: 21
Explanation
Separating sentence into "apples", "and", "bananas", "taste", and "great" has a cost of (7 - 6)^2 + (7 - 3)^2 + (7 - 7)^2 + (7 - 5)^2 = 21.
21 is the minimum possible total cost so return it.

Example 3:

Input: sentence = "a", k = 5
Output: 0
Explanation:
The cost of the last row is not included in the total cost, and since there is only one row, return 0.


Constraints:

1 <= sentence.length <= 5000
1 <= k <= 5000
The length of each word in sentence is at most k.
sentence consists of only lowercase English letters and spaces.
sentence does not begin or end with a space.
Words in sentence are separated by a single space.

"""

# V0
# IDEA : PREFIX SUM + BOTTOM-UP DP (word-break with squared slack cost)
#
#   s[i] = total length of the first i words, so the row holding words
#   [i, j) occupies  m = s[j] - s[i] + (j - i - 1)  characters (the
#   (j-i-1) term counts the single spaces between them).
#
#   f[i] = min cost of laying out words i..n-1
#        = 0                                   if words i..n-1 fit in one row
#        = min over feasible j of  f[j] + (k - m)^2
#
#   NOTE : the LAST row is free, hence the early 0 return; and NOTE the loop
#          is written bottom-up (i from n-1 down to 0) because n can reach
#          2500 words and a recursive dfs would risk the recursion limit.
#
"""

DP def
    s[i] = total length of the first i words, so the row holding words [i, j)
    occupies  m = s[j] - s[i] + (j - i - 1)  characters
    (the (j-i-1) term counts the single spaces between them)

    f[i]: MIN cost of laying out words i..n-1

DP eq

     f[i] = 0                                if words i..n-1 all fit in one row

     f[i] = min over feasible j of  f[j] + (k - m)^2


    -> e.g. NOTE !!! the LAST row is FREE, which is exactly the early
              0 return

     written BOTTOM-UP (i from n-1 down to 0) because n can reach 2500 words
     and a recursive dfs would risk the recursion limit

     ans = f[0]

"""
# time = O(n^2), space = O(n), n = number of words
class Solution(object):
    def minimumCost(self, sentence, k):
        words = sentence.split()
        n = len(words)
        s = [0] * (n + 1)
        for i in range(n):
            s[i + 1] = s[i] + len(words[i])

        INF = float('inf')
        f = [0] * (n + 1)
        for i in range(n - 1, -1, -1):
            # can everything from i onward sit on the (free) last row ?
            if s[n] - s[i] + n - i - 1 <= k:
                f[i] = 0
                continue
            best = INF
            j = i + 1
            while j < n:
                m = s[j] - s[i] + j - i - 1
                if m > k:
                    break
                cost = f[j] + (k - m) * (k - m)
                if cost < best:
                    best = cost
                j += 1
            f[i] = best
        return f[0]
