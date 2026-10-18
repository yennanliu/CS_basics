"""

720. Longest Word in Dictionary
Medium

Given an array of strings words representing an English Dictionary, return the longest word in words that can be built one character at a time by other words in words.

If there is more than one possible answer, return the longest word with the smallest lexicographical order. If there is no answer, return the empty string.

Note that the word should be built from left to right with each additional character being added to the end of a previous word.

Example 1:

Input: words = ["w","wo","wor","worl","world"]
Output: "world"
Explanation: The word "world" can be built one character at a time by "w", "wo", "wor", and "worl".

Example 2:

Input: words = ["a","banana","app","appl","ap","apply","apple"]
Output: "apple"
Explanation: Both "apply" and "apple" can be built from other words in the dictionary. However, "apple" is lexicographically smaller than "apply".

Constraints:

1 <= words.length <= 1000
1 <= words[i].length <= 30
words[i] consists of lowercase English letters.

"""

# Time:  O(n), n is the total sum of the lengths of words
# Space: O(t), t is the number of nodes in trie

# Given a list of strings words representing an English Dictionary,
# find the longest word in words that can be built one character at a time by other words in words.
# If there is more than one possible answer, return the longest word with the smallest lexicographical order.
#
# If there is no answer, return the empty string.
# Example 1:
# Input:
# words = ["w","wo","wor","worl", "world"]
# Output: "world"
# Explanation:
# The word "world" can be built one character at a time by "w", "wo", "wor", and "worl".
#
# Example 2:
# Input:
# words = ["a", "banana", "app", "appl", "ap", "apply", "apple"]
# Output: "apple"
# Explanation:
# Both "apply" and "apple" can be built from other words in the dictionary.
# However, "apple" is lexicographically smaller than "apply".
#
# Note:
# - All the strings in the input will only contain lowercase letters.
# - The length of words will be in the range [1, 1000].
# - The length of words[i] will be in the range [1, 30].

# V0
# IDEA: BFS (gpt)
from collections import deque

class Solution(object):
    def longestWord(self, words):
        """
        :type words: List[str]
        :rtype: str
        """
        words_set = set(words)
        q = deque()

        # Start BFS from all valid 1-character words
        for w in words:
            if len(w) == 1:
                q.append(w)

        res = ""

        while q:
            word = q.popleft()

            # Update answer: longer word, or lexicographically smaller
            if (len(word) > len(res) or
                (len(word) == len(res) and word < res)):
                res = word

            # Find valid words that extend the current word by one char
            for c in "abcdefghijklmnopqrstuvwxyz":
                next_word = word + c

                if next_word in words_set:
                    q.append(next_word)

        return res

# V0-1
# IDEA: SORT + STR OP + prefix (gpt)
class Solution(object):
    def longestWord(self, words):
        """
        :type words: List[str]
        :rtype: str
        """
        words_set = set(words)
        res = ""

        for w in words:
            # Every prefix must exist in the dictionary
            if all(w[:i] in words_set for i in range(1, len(w))):
                # Prefer longer words; for equal length, choose
                # lexicographically smaller words
                if len(w) > len(res) or (
                    len(w) == len(res) and w < res
                ):
                    res = w

        return res


# V0-2
# IDEA: 排序 + 雜湊表貪心法（Sorting + Hash Set）(gemini)
class Solution(object):

  def longestWord(self, words):
    """:type words: List[str]

    :rtype: str
    """
    # 1. 排序單字：讓短的在前、字典序小的在前
    words.sort()

    word_set = set()
    longest = ""

    for w in words:
      # 2. 如果單字長度為 1，或者它的前綴 (去掉最後一個字元) 已經在 word_set 中
      if len(w) == 1 or w[:-1] in word_set:
        word_set.add(w)

        # 3. 更新最長單字 (因為已經排序過，若長度相同不更新，直接保留字典序最小的第一個)
        if len(w) > len(longest):
          longest = w

    return longest


# V0-3
# IDEA: BFS (gpt)
from collections import deque

class Solution(object):
    def longestWord(self, words):
        """
        :type words: List[str]
        :rtype: str
        """
        words_set = set(words)
        q = deque()

        # Start BFS from all valid 1-character words
        for w in words:
            if len(w) == 1:
                q.append(w)

        res = ""

        while q:
            word = q.popleft()

            # Update answer: longer word, or lexicographically smaller
            if (len(word) > len(res) or
                (len(word) == len(res) and word < res)):
                res = word

            # Find valid words that extend the current word by one char
            for c in "abcdefghijklmnopqrstuvwxyz":
                next_word = word + c

                if next_word in words_set:
                    q.append(next_word)

        return res

# V0-4
# IDEA: BFS (gemini)
from collections import deque


class Solution(object):

  def longestWord(self, words):
    """:type words: List[str]

    :rtype: str
    """
    # 1. 將所有單字放入 set 中，以供 O(1) 快速查找
    word_set = set(words)

    # 2. 找出所有長度為 1 且存在於 word_set 中的單字作為 BFS 的起始點
    # 並且先進行排序，確保起始順序是字典序由小到大
    start_words = sorted([w for w in word_set if len(w) == 1])

    q = deque(start_words)
    best_word = ""

    # 3. 開始 BFS 逐層擴展
    while q:
      curr = q.popleft()

      # 更新最長單字：
      # 條件 1：當前長度大於目前紀錄的最長長度
      # 條件 2：長度相同時，選擇字典序較小的單字
      if len(curr) > len(best_word) or (
          len(curr) == len(best_word) and curr < best_word
      ):
        best_word = curr

      # 4. 嘗試在當前單字後面加上 'a' 到 'z'
      # 因為迴圈順序是 'a' 到 'z'，所以字典序較小的擴展會先被放入 queue 中 (FIFO 保持優勢)
      for i in range(26):
        nxt = curr + chr(ord("a") + i)
        if nxt in word_set:
          q.append(nxt)

    return best_word


# V1
# https://blog.csdn.net/fuxuemingzhu/article/details/79123277
# IDEA : GREEDY
# time = O(sum(len(w)^2)) ~ O(n * l), n = number of words, l = avg word length
# space = O(n * l)
class Solution(object):
    def longestWord(self, words):
        """
        :type words: List[str]
        :rtype: str
        """
        wset = set(words)
        res = ""
        for w in words:
            isIn = True
            for i in range(1, len(w)):
                if w[:i] not in wset:
                    isIn = False
                    break
            if isIn:
                if not res or len(w) > len(res):
                    res = w
                elif len(w) == len(res) and res > w:
                    res = w
        return res

# V1'
# https://blog.csdn.net/fuxuemingzhu/article/details/79123277
# IDEA : SORT
# time = O(n*log(n)*l), n = number of words, l = avg word length (sort + set membership)
# space = O(n * l)
class Solution(object):
    def longestWord(self, words):
        """
        :type words: List[str]
        :rtype: str
        """
        words.sort()
        res = set([''])
        longestWord = ''
        for word in words:
            if word[:-1] in res:
                res.add(word)
                if len(word) > len(longestWord):
                    longestWord = word
        return longestWord
        
# V2
# time = O(n * l), n = number of words, l = avg word length (trie build + DFS)
# space = O(n * l)
import collections
from functools import reduce
class Solution(object):
    def longestWord(self, words):
        """
        :type words: List[str]
        :rtype: str
        """
        _trie = lambda: collections.defaultdict(_trie)
        trie = _trie()
        for i, word in enumerate(words):
            reduce(dict.__getitem__, word, trie)["_end"] = i

        # DFS
        stack = list(trie.values())
        result = ""
        while stack:
            curr = stack.pop()
            if "_end" in curr:
                word = words[curr["_end"]]
                if len(word) > len(result) or (len(word) == len(result) and word < result):
                    result = word
                stack += [curr[letter] for letter in curr if letter != "_end"]
        return result
