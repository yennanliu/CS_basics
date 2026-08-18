#---------------------------------------------------------------
# TRIE (prefix tree)
#---------------------------------------------------------------
#
# A tree keyed by CHARACTER: the path from the root to a node spells
# a prefix, and shared prefixes are stored exactly once.
#
#            (root)
#           /      \
#          a        t
#          |        |
#          n*       h
#         / \      / \
#        t   y*   e*  i
#        |         |   |
#        s*        r   s*
#                  |
#                  e*
#
#        (* = end of a word: an, any, ants, the, there, this)
#
# Why not just a hash set of words? A set answers "is this an exact
# word?" in O(L), but cannot answer "which words start with 'th'?"
# without scanning everything. A trie walks straight to the 'th' node
# and reads the subtree.
#
# Time  : insert / search / starts_with   O(L)   (L = length of the key,
#                                                 INDEPENDENT of how many
#                                                 words are stored)
# Space : O(total characters), shared prefixes stored once
#
# Used by: LC 208 Implement Trie, LC 211 Add and Search Word,
#          LC 212 Word Search II, LC 1268 Search Suggestions System.
#
# References:
#   - https://www.geeksforgeeks.org/trie-insert-and-search/


class TrieNode:
    """One character position. `children` maps next-char -> TrieNode."""

    def __init__(self):
        self.children = {}
        self.is_word = False        # True only if a word ENDS here


class Trie:
    def __init__(self, words=()):
        self.root = TrieNode()
        for word in words:
            self.insert(word)

    def insert(self, word):
        """Walk down the word, creating nodes as needed, then flag the end."""
        node = self.root
        for char in word:
            if char not in node.children:
                node.children[char] = TrieNode()
            node = node.children[char]
        node.is_word = True         # NOTE: the flag, not the node, defines a word

    def _walk(self, prefix):
        """Return the node reached by following `prefix`, or None."""
        node = self.root
        for char in prefix:
            if char not in node.children:
                return None
            node = node.children[char]
        return node

    def search(self, word):
        """True only for a COMPLETE word that was inserted."""
        node = self._walk(word)
        return node is not None and node.is_word

    def starts_with(self, prefix):
        """True if any inserted word begins with `prefix`."""
        return self._walk(prefix) is not None

    def words_with_prefix(self, prefix, limit=None):
        """All stored words beginning with `prefix`, in sorted order.

        Walk to the prefix node, then DFS its subtree collecting every
        node flagged is_word. Visiting children in sorted key order
        makes the output lexicographic, which is what autocomplete wants.
        """
        node = self._walk(prefix)
        if node is None:
            return []

        found = []

        def collect(node, path):
            if limit is not None and len(found) >= limit:
                return
            if node.is_word:
                found.append(path)
            for char in sorted(node.children):
                collect(node.children[char], path + char)

        collect(node, prefix)
        return found


if __name__ == "__main__":
    trie = Trie(["an", "any", "ants", "the", "there", "this"])

    # a complete word vs. a mere prefix
    assert trie.search("an") is True
    assert trie.search("ant") is False        # "ant" is only a prefix of "ants"
    assert trie.starts_with("ant") is True
    assert trie.starts_with("zoo") is False

    assert trie.search("there") is True
    assert trie.search("ther") is False

    # autocomplete
    assert trie.words_with_prefix("th") == ["the", "there", "this"]
    assert trie.words_with_prefix("an") == ["an", "ants", "any"]
    assert trie.words_with_prefix("th", limit=2) == ["the", "there"]
    assert trie.words_with_prefix("zoo") == []

    # inserting a prefix of an existing word only flips a flag
    trie.insert("ant")
    assert trie.search("ant") is True
    assert trie.words_with_prefix("ant") == ["ant", "ants"]

    # the empty prefix matches everything
    assert trie.words_with_prefix("") == ["an", "ant", "ants", "any",
                                          "the", "there", "this"]

    print("Success.")
