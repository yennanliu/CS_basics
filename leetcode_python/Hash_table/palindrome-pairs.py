"""

336. Palindrome Pairs
Hard

You are given a 0-indexed array of unique strings words.

A palindrome pair is a pair of integers (i, j) such that:

0 <= i, j < words.length,
i != j, and
words[i] + words[j] (the concatenation of the two strings) is a palindrome.

Return an array of all the palindrome pairs of words.

You must write an algorithm with O(sum of words[i].length) runtime complexity.


Example 1:

Input: words = ["abcd","dcba","lls","s","sssll"]

Output: [[0,1],[1,0],[3,2],[2,4]]

Explanation: The palindromes are ["abcddcba","dcbaabcd","slls","llssssll"]

Example 2:

Input: words = ["bat","tab","cat"]

Output: [[0,1],[1,0]]

Explanation: The palindromes are ["battab","tabbat"]

Example 3:

Input: words = ["a",""]

Output: [[0,1],[1,0]]

Explanation: The palindromes are ["a","a"]


Constraints:

1 <= words.length <= 5000
0 <= words[i].length <= 300
words[i] consists of lowercase English letters.

"""

# V0
# IDEA : HASH MAP {word : idx} + SPLIT EACH WORD INTO left | right
#
#   for words[k] + partner to be a palindrome, cut words[k] = left + right:
#
#   case 1) left is a palindrome  -> partner = reverse(right), placed BEFORE:
#           reverse(right) + left + right   -> pair (partner, k)
#   case 2) right is a palindrome -> partner = reverse(left), placed AFTER:
#           left + right + reverse(left)    -> pair (k, partner)
#
#   trying every cut (0..len) finds every partner, and the hash map makes each
#   partner lookup O(l) instead of scanning all n words.
#   (brute force: test all n^2 concatenations -> O(n^2 * l), TLE at n = 5000)
#
#   e.g. "lls": cut "ll" | "s" -> left "ll" is a palindrome,
#        reverse("s") = "s" is word 3 -> (3, 2) -> "s" + "lls" = "slls"
#
# time = O(n * l^2), space = O(n * l)
#   (n = number of words, l = longest word)
class Solution(object):
    def palindromePairs(self, words):
        """
        :type words: List[str]
        :rtype: List[List[int]]
        """
        # word -> its index, so a partner is found in O(l)
        word_to_idx = {}
        for idx, word in enumerate(words):
            word_to_idx[word] = idx

        pairs = []
        for k, word in enumerate(words):
            for cut in range(len(word) + 1):
                left = word[:cut]
                right = word[cut:]

                # case 1: partner goes in FRONT of word
                if self.is_palindrome(left):
                    partner = self.reverse(right)
                    if partner in word_to_idx and word_to_idx[partner] != k:
                        pairs.append([word_to_idx[partner], k])

                # case 2: partner goes BEHIND word
                # NOTE !!! skip cut == len(word): there right == "" and left is
                #          the whole word, the same split case 1 already
                #          handled at cut == 0 -> it would add the pair twice
                if cut != len(word) and self.is_palindrome(right):
                    partner = self.reverse(left)
                    if partner in word_to_idx and word_to_idx[partner] != k:
                        pairs.append([k, word_to_idx[partner]])

        return pairs

    def is_palindrome(self, s):
        """True if s reads the same both ways (two pointers)."""
        left = 0
        right = len(s) - 1
        while left < right:
            if s[left] != s[right]:
                return False
            left += 1
            right -= 1
        return True

    def reverse(self, s):
        """s written backwards."""
        chars = []
        for i in range(len(s) - 1, -1, -1):
            chars.append(s[i])
        return "".join(chars)


# V1
# IDEA : TRIE OF REVERSED WORDS + "PALINDROME BELOW" LISTS
#
#   insert every word REVERSED, then walk each word i FORWARD down the trie.
#   reaching a node means words[i]'s prefix == reverse of a path, so the two
#   outer parts already mirror each other; what is left must be a palindrome:
#
#   a) a word k ENDS mid-walk at position j -> words[i][j:] must be a palindrome
#   b) words[i] is used up at node -> the unread part of word k (below node)
#      must be a palindrome. every node keeps `pal_below`: the words whose
#      remaining part from that node down is a palindrome, filled at insert.
#
#   e.g. words = ["lls", "s"]: trie holds "sll", "s". walking "s" ends at node
#        "s"; "sll" continues with "ll" (a palindrome) -> (1, 0) = "slls"
#
# NOTE : same bound as V0 here, more code; worth knowing as the trie follow-up
#
# time = O(n * l^2), space = O(n * l)
class TrieNode(object):
    def __init__(self):
        # letter -> child node
        self.children = {}
        # index of the word whose reversed form ends here, -1 if none
        self.word_idx = -1
        # indexes of words whose part BELOW this node is a palindrome
        self.pal_below = []


class Solution2(object):
    def palindromePairs(self, words):
        """
        :type words: List[str]
        :rtype: List[List[int]]
        """
        root = TrieNode()
        for idx, word in enumerate(words):
            self.insert_reversed(root, word, idx)

        pairs = []
        for i, word in enumerate(words):
            node = root
            matched_all = True

            for j in range(len(word)):
                # case a: a word ends here, and the rest of words[i] is a palindrome
                if node.word_idx != -1 and node.word_idx != i:
                    if self.is_palindrome(word, j, len(word) - 1):
                        pairs.append([i, node.word_idx])

                if word[j] not in node.children:
                    matched_all = False
                    break
                node = node.children[word[j]]

            # case b: words[i] is used up; every word continuing with a
            # palindrome below this node completes it (incl. one ending here)
            if matched_all:
                for k in node.pal_below:
                    if k != i:
                        pairs.append([i, k])

        return pairs

    def insert_reversed(self, root, word, idx):
        """Add word to the trie last char first, recording palindromic tails."""
        node = root
        for j in range(len(word) - 1, -1, -1):
            # the part still to insert is word[0..j] (read backwards); if it is
            # a palindrome, any search ending at this node can pair with idx
            if self.is_palindrome(word, 0, j):
                node.pal_below.append(idx)
            if word[j] not in node.children:
                node.children[word[j]] = TrieNode()
            node = node.children[word[j]]

        # nothing left below the end node: "" is a palindrome
        node.word_idx = idx
        node.pal_below.append(idx)

    def is_palindrome(self, s, left, right):
        """True if s[left..right] (inclusive) is a palindrome."""
        while left < right:
            if s[left] != s[right]:
                return False
            left += 1
            right -= 1
        return True
