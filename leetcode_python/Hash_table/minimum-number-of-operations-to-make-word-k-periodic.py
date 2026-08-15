"""

3137. Minimum Number of Operations to Make Word K-Periodic
Medium

You are given a string word of size n, and an integer k such that k divides n.

In one operation, you can pick any two indices i and j, that are divisible by k, then replace the substring of length k starting at i with the substring of length k starting at j. That is, replace the substring word[i..i + k - 1] with the substring word[j..j + k - 1].

Return the minimum number of operations required to make word k-periodic.

We say that word is k-periodic if there is some string s of length k such that word can be obtained by concatenating s an arbitrary number of times. For example, if word == "ababab", then word is 2-periodic for s = "ab".


Example 1:

Input: word = "leetcodeleet", k = 4
Output: 1
Explanation:
We can obtain a 4-periodic string by picking i = 4 and j = 0. After this operation, word becomes equal to "leetleetleet".

Example 2:

Input: word = "leetcoleet", k = 2
Output: 3
Explanation:
We can obtain a 2-periodic string by applying the operations in the table below.


Constraints:

1 <= n == word.length <= 10^5
1 <= k <= word.length
k divides word.length.

"""

# V0
# IDEA : THE BLOCKS ARE INTERCHANGEABLE — KEEP THE MOST COMMON ONE
#
#   the operation copies one whole k-block over another, so the string is
#   really a multiset of n/k blocks and each operation rewrites exactly one
#   block into any other block's content.
#
#   to make them all identical, keep whichever block content already appears
#   most often and overwrite the rest :
#
#       answer = (n / k) - (highest block frequency)
#
# time = O(n), space = O(n)
from collections import Counter


class Solution(object):
    def minimumOperationsToMakeKPeriodic(self, word, k):
        n = len(word)
        blocks = Counter(word[i:i + k] for i in range(0, n, k))
        return n // k - max(blocks.values())
