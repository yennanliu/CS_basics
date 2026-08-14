"""

1065. Index Pairs of a String
Easy

Given a string text and an array of strings words, return an array of all index
pairs [i, j] so that the substring text[i...j] is in words.

Return the pairs [i, j] in sorted order (i.e., sort them by their first coordinate,
and in case of ties sort them by their second coordinate).


Example 1:

Input: text = "thestoryofleetcodeandme", words = ["story","fleet","leetcode"]
Output: [[3,7],[9,13],[10,17]]

Example 2:

Input: text = "ababa", words = ["aba","ab"]
Output: [[0,1],[0,2],[2,3],[2,4]]
Explanation: Notice that matches can overlap, see "aba" is found in [0,2] and [2,4].


Constraints:

1 <= text.length <= 100
1 <= words.length <= 20
1 <= words[i].length <= 50
text and words[i] consist of lowercase English letters.
All the strings of words are unique.

"""

# V0
# IDEA : TRIE
#
#  build a trie of `words`, then for every start index i walk the trie
#  along text[i:], recording [i, j] whenever we land on a word end.
#  walking left -> right for i, then j, already gives the required sorted order,
#  and we can break as soon as the prefix leaves the trie.
# time = O(sum(len(w)) + n * L), n = len(text), L = max word length
# space = O(sum(len(w)))
class Trie(object):
    def __init__(self):
        self.children = {}
        self.is_end = False

    def insert(self, word):
        node = self
        for c in word:
            if c not in node.children:
                node.children[c] = Trie()
            node = node.children[c]
        node.is_end = True


class Solution(object):
    def indexPairs(self, text, words):
        root = Trie()
        for w in words:
            root.insert(w)

        n = len(text)
        res = []
        for i in range(n):
            node = root
            for j in range(i, n):
                c = text[j]
                if c not in node.children:
                    break
                node = node.children[c]
                if node.is_end:
                    res.append([i, j])
        return res

# V1
# IDEA : BRUTE FORCE (n <= 100, so all O(n^2) substrings is fine)
# time = O(n^3)
# space = O(n * m)
class Solution(object):
    def indexPairs(self, text, words):
        word_set = set(words)
        n = len(text)
        return [[i, j]
                for i in range(n)
                for j in range(i, n)
                if text[i:j + 1] in word_set]
