"""

1160. Find Words That Can Be Formed by Characters
Easy

You are given an array of strings words and a string chars.

A string is good if it can be formed by characters from chars
(each character can only be used once for each word in words).

Return the sum of lengths of all good strings in words.


Example 1:

Input: words = ["cat","bt","hat","tree"], chars = "atach"
Output: 6
Explanation: The strings that can be formed are "cat" and "hat" so the answer is 3 + 3 = 6.

Example 2:

Input: words = ["hello","world","leetcode"], chars = "welldonehoneyr"
Output: 10
Explanation: The strings that can be formed are "hello" and "world"
so the answer is 5 + 5 = 10.


Constraints:

1 <= words.length <= 1000
1 <= words[i].length, chars.length <= 100
words[i] and chars consist of lowercase English letters.

"""

# V0
# IDEA : HASH TABLE (counting)
#        count letters of `chars` once, then for every word check
#        that each of its letter counts fits inside that budget.
#        (budget is NOT consumed across words -> re-check from the same counter)
# time = O(L), L = total length of chars + all words
# space = O(1)
from collections import Counter
class Solution(object):
    def countCharacters(self, words, chars):
        cnt = Counter(chars)
        res = 0
        for w in words:
            wc = Counter(w)
            if all(wc[c] <= cnt[c] for c in wc):
                res += len(w)
        return res
