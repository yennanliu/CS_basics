"""

1002. Find Common Characters
Easy

Given a string array words, return an array of all characters that show up in all strings within the words (including duplicates). You may return the answer in any order.


Example 1:

Input: words = ["bella","label","roller"]
Output: ["e","l","l"]

Example 2:

Input: words = ["cool","lock","cook"]
Output: ["c","o"]


Constraints:

1 <= words.length <= 100
1 <= words[i].length <= 100
words[i] consists of lowercase English letters.

"""

# V0
# IDEA : COUNTER + take the MIN count of each char over all words
# time = O(n * L), n = len(words), L = avg word length
# space = O(1), at most 26 letters
from collections import Counter
class Solution(object):
    def commonChars(self, words):
        common = Counter(words[0])
        for w in words[1:]:
            cnt = Counter(w)
            for c in list(common.keys()):
                common[c] = min(common[c], cnt[c])

        res = []
        for c, k in common.items():
            res += [c] * k
        return res


# V0-1
# IDEA : COUNTER `&` (intersection) operator
# time = O(n * L)
# space = O(1)
from collections import Counter
class Solution(object):
    def commonChars(self, words):
        common = Counter(words[0])
        for w in words[1:]:
            common &= Counter(w)
        return list(common.elements())
