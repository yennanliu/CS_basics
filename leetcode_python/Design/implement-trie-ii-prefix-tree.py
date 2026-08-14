"""

1804. Implement Trie II (Prefix Tree)
Medium

A trie (pronounced as "try") or prefix tree is a tree data structure used to efficiently store and retrieve keys in a dataset of strings. There are various applications of this data structure, such as autocomplete and spellchecker.

Implement the Trie class:

Trie() Initializes the trie object.
void insert(String word) Inserts the string word into the trie.
int countWordsEqualTo(String word) Returns the number of instances of the string word in the trie.
int countWordsStartingWith(String prefix) Returns the number of strings in the trie that have the string prefix as a prefix.
void erase(String word) Erases the string word from the trie.


Example 1:

Input
["Trie", "insert", "insert", "countWordsEqualTo", "countWordsStartingWith", "erase", "countWordsEqualTo", "countWordsStartingWith", "erase", "countWordsStartingWith"]
[[], ["apple"], ["apple"], ["apple"], ["app"], ["apple"], ["apple"], ["app"], ["apple"], ["app"]]
Output
[null, null, null, 2, 2, null, 1, 1, null, 0]

Explanation
Trie trie = new Trie();
trie.insert("apple");               // Inserts "apple".
trie.insert("apple");               // Inserts another "apple".
trie.countWordsEqualTo("apple");    // There are two instances of "apple" so return 2.
trie.countWordsStartingWith("app"); // "app" is a prefix of "apple" so return 2.
trie.erase("apple");                // Erases one "apple".
trie.countWordsEqualTo("apple");    // Now there is only one instance of "apple" so return 1.
trie.countWordsStartingWith("app"); // return 1
trie.erase("apple");                // Erases "apple". Now the trie is empty.
trie.countWordsStartingWith("app"); // return 0


Constraints:

1 <= word.length, prefix.length <= 2000
word and prefix consist only of lowercase English letters.
At most 3 * 10^4 calls in total will be made to insert, countWordsEqualTo, countWordsStartingWith, and erase.
It is guaranteed that for any function call to erase, the string word will exist in the trie.

"""

# V0
# IDEA : TRIE WITH TWO COUNTERS PER NODE (word count + prefix count)
#
#   each node keeps
#     - v  : how many inserted words END here      -> countWordsEqualTo
#     - pv : how many inserted words PASS through  -> countWordsStartingWith
#
#   insert : ++pv on every node along the path, ++v on the last node.
#   erase  : mirror image, --pv along the path, --v on the last node.
#
#   NOTE : erase is guaranteed to be called only for a word that exists, so we
#          never have to worry about a missing node / negative counter, and we
#          can leave the (now empty) nodes in place -- pv == 0 answers 0 anyway.
#
# time = O(L) per operation, L = len(word), space = O(total inserted chars)
class Trie(object):

    def __init__(self):
        self.children = [None] * 26
        self.v = 0    # words ending here
        self.pv = 0   # words passing through here

    def insert(self, word):
        node = self
        for c in word:
            i = ord(c) - ord('a')
            if node.children[i] is None:
                node.children[i] = Trie()
            node = node.children[i]
            node.pv += 1
        node.v += 1

    def countWordsEqualTo(self, word):
        node = self._find(word)
        return 0 if node is None else node.v

    def countWordsStartingWith(self, prefix):
        node = self._find(prefix)
        return 0 if node is None else node.pv

    def erase(self, word):
        node = self
        for c in word:
            i = ord(c) - ord('a')
            node = node.children[i]
            node.pv -= 1
        node.v -= 1

    def _find(self, word):
        node = self
        for c in word:
            i = ord(c) - ord('a')
            if node.children[i] is None:
                return None
            node = node.children[i]
        return node


# Your Trie object will be instantiated and called as such:
# obj = Trie()
# obj.insert(word)
# param_2 = obj.countWordsEqualTo(word)
# param_3 = obj.countWordsStartingWith(prefix)
# obj.erase(word)
