"""

3545. Minimum Deletions for At Most K Distinct Characters
Easy

You are given a string s consisting of lowercase English letters, and an integer k.

Your task is to delete some (possibly none) of the characters in the string so
that the number of distinct characters in the resulting string is at most k.

Return the minimum number of deletions required to achieve this.


Example 1:

Input: s = "abc", k = 2
Output: 1
Explanation:
s has three distinct characters: 'a', 'b' and 'c', each with a frequency of 1.
Since we can have at most k = 2 distinct characters, remove all occurrences of
any one character from the string.
For example, removing all occurrences of 'c' results in at most k distinct
characters. Thus, the answer is 1.

Example 2:

Input: s = "aabb", k = 2
Output: 0
Explanation:
s has two distinct characters ('a' and 'b') with frequencies of 2 and 2,
respectively.
Since we can have at most k = 2 distinct characters, no deletions are required.
Thus, the answer is 0.

Example 3:

Input: s = "yyyzz", k = 1
Output: 2
Explanation:
s has two distinct characters ('y' and 'z') with frequencies of 3 and 2,
respectively.
Since we can have at most k = 1 distinct character, remove all occurrences of
any one character from the string.
Removing all 'z' results in at most k distinct characters. Thus, the answer is 2.


Constraints:

1 <= s.length <= 16
1 <= k <= 16
s consists only of lowercase English letters.

"""

# V0
# IDEA : DROP THE RAREST LETTERS FIRST
#
#   a letter is either kept entirely or deleted entirely — keeping some copies
#   of a letter still counts it as distinct, so there is never a reason to do a
#   partial deletion.
#
#   with that, the problem is "delete whole letters until at most k remain",
#   and the cheapest set of letters to delete is the ones with the smallest
#   counts.  sort the frequencies ascending and sum the first (d - k) of them.
#
# time = O(n + 26 log 26), space = O(26)
from collections import Counter


class Solution(object):
    def minDeletion(self, s, k):
        freqs = sorted(Counter(s).values())
        drop = len(freqs) - k
        if drop <= 0:
            return 0
        return sum(freqs[:drop])
