"""

1897. Redistribute Characters to Make All Strings Equal
Easy

You are given an array of strings words (0-indexed).

In one operation, pick two distinct indices i and j, where words[i] is a non-empty string, and move any character from words[i] to any position in words[j].

Return true if you can make every string in words equal using any number of operations, and false otherwise.


Example 1:

Input: words = ["abc","aabc","bc"]
Output: true
Explanation: Move the first 'a' in words[1] to the front of words[2],
to make words[1] = "abc" and words[2] = "abc".
All the strings are now equal to "abc", so return true.

Example 2:

Input: words = ["ab","a"]
Output: false
Explanation: It is impossible to make all the strings equal using the operation.


Constraints:

1 <= words.length <= 100
1 <= words[i].length <= 100
words[i] consists of lowercase English letters.

"""

# V0
# IDEA : COUNTING (characters are conserved, so each total must split evenly)
#
#   moves never create or destroy a character - they only relocate one.
#   so the multiset of ALL characters is invariant, and the target state
#   is n identical strings, meaning every letter's total count must be
#   divisible by n = len(words).
#
#   that condition is also SUFFICIENT : with free relocation we can always
#   hand each string exactly total[c] / n copies of every letter c.
#
# time = O(L), L = total number of characters
# space = O(1), at most 26 counters
from collections import Counter
class Solution(object):
    def makeEqual(self, words):
        cnt = Counter()
        for w in words:
            cnt.update(w)

        n = len(words)
        for c in cnt:
            if cnt[c] % n != 0:
                return False
        return True
