"""

833. Find And Replace in String
Medium

You are given a 0-indexed string s that you must perform k replacement operations on. The replacement operations are given as three 0-indexed parallel arrays, indices, sources, and targets, all of length k.

To complete the i^th replacement operation:

Check if the substring sources[i] occurs at index indices[i] in the original string s.
If it does not occur, do nothing.
Otherwise if it does occur, replace that substring with targets[i].

For example, if s = "abcd", indices[i] = 0, sources[i] = "ab", and targets[i] = "eee", then the result of this replacement will be "eeecd".

All replacement operations must occur simultaneously, meaning the replacement operations should not affect the indexing of each other. The testcases will be generated such that the replacements will not overlap.

For example, a testcase with s = "abc", indices = [0, 1], and sources = ["ab","bc"] will not be generated because the "ab" and "bc" replacements overlap.

Return the resulting string after performing all replacement operations on s.

A substring is a contiguous sequence of characters in a string.

Example 1:

https://assets.leetcode.com/uploads/2021/06/12/833-ex1.png

Input: s = "abcd", indices = [0, 2], sources = ["a", "cd"], targets = ["eee", "ffff"]
Output: "eeebffff"
Explanation:
"a" occurs at index 0 in s, so we replace it with "eee".
"cd" occurs at index 2 in s, so we replace it with "ffff".

Example 2:

https://assets.leetcode.com/uploads/2021/06/12/833-ex2-1.png

Input: s = "abcd", indices = [0, 2], sources = ["ab","ec"], targets = ["eee","ffff"]
Output: "eeecd"
Explanation:
"ab" occurs at index 0 in s, so we replace it with "eee".
"ec" does not occur at index 2 in s, so we do nothing.

Constraints:

1 <= s.length <= 1000
k == indices.length == sources.length == targets.length
1 <= k <= 100
0 <= indexes[i] < s.length
1 <= sources[i].length, targets[i].length <= 50
s consists of only lowercase English letters.
sources[i] and targets[i] consist of only lowercase English letters.

"""

# V0 
# IDEA: HASHMAP (map, reverse map) + LC 205
class Solution(object):
    def findAndReplacePattern(self, words, pattern):
        """
        :type words: List[str]
        :type pattern: str
        :rtype: List[str]
        """
        # edge
        if not words or len(words) == 0:
          return []

        if not pattern or len(pattern) == 0:
          return words

        res = []

        for w in words:
          if self.helper(w, pattern):
            res.append(w)


        return res


    def helper(self, w, pattern):
        if w == pattern:
          return True

        if not w and not pattern:
          return True

        if not w or not pattern:
          return False

        if len(w) != len(pattern):
          return False

        # {w_val : p_val}
        _map = {}

        # {p_val: w_val}
        _reverse_map = {}

        _len = len(w)

        for i in range(_len):

          w_val = w[i]
          p_val = pattern[i]

          if w_val in _map:
            if _map[w_val] != p_val:
              return False

          else:
            _map[w_val] = p_val


          if p_val in _reverse_map:
            if _reverse_map[p_val] != w_val:
              return False
          else:
            _reverse_map[p_val] = w_val


        return True


# V0-1
# IDEA: HASHMAP (map, reverse map) + LC 205 (gpt)
class Solution(object):
    def findAndReplacePattern(self, words, pattern):
        """
        :type words: List[str]
        :type pattern: str
        :rtype: List[str]
        """
        if not words:
            return []

        res = []

        for w in words:
            if self.helper(w, pattern):
                res.append(w)

        return res

    def helper(self, w, pattern):
        # Strings must have the same length
        if len(w) != len(pattern):
            return False

        # Mapping: word character -> pattern character
        char_map = {}

        # Reverse mapping: pattern character -> word character
        reverse_map = {}

        for i in range(len(w)):
            w_val = w[i]
            p_val = pattern[i]

            # Check word -> pattern
            if w_val in char_map:
                if char_map[w_val] != p_val:
                    return False
            else:
                char_map[w_val] = p_val

            # Check pattern -> word
            if p_val in reverse_map:
                if reverse_map[p_val] != w_val:
                    return False
            else:
                reverse_map[p_val] = w_val

        return True


# V0-2
# IDEA: HASHMAP (map, reverse map) + LC 205 (GEMINI)
class Solution(object):

  def findAndReplacePattern(self, words, pattern):
    """:type words: List[str]

    :type pattern: str
    :rtype: List[str]
    """
    if not words or not pattern:
      return []

    res = []
    for w in words:
      if self.isIsomorphic(w, pattern):
        res.append(w)

    return res

  def isIsomorphic(self, w, pattern):
    # 長度不同直接回傳 False
    if len(w) != len(pattern):
      return False

    w_to_p = {}
    p_to_w = {}

    for char_w, char_p in zip(w, pattern):
      # 1. 檢查 w 到 pattern 的對應是否衝突
      if char_w in w_to_p and w_to_p[char_w] != char_p:
        return False

      # 2. 檢查 pattern 到 w 的對應是否衝突（防止多對一）
      if char_p in p_to_w and p_to_w[char_p] != char_w:
        return False

      # 建立雙向對應
      w_to_p[char_w] = char_p
      p_to_w[char_p] = char_w

    return True


# V1
# https://blog.csdn.net/fuxuemingzhu/article/details/81094404
# time = O(n * m)  # n = len(S), m = len(indexes)
# space = O(n)
class Solution(object):
    def findReplaceString(self, S, indexes, sources, targets):
        """
        :type S: str
        :type indexes: List[int]
        :type sources: List[str]
        :type targets: List[str]
        :rtype: str
        """
        ans = ""
        i = 0
        while i < len(S):
            if i not in indexes:
                ans += S[i]
                i += 1
            else:
                ind = indexes.index(i)
                source = sources[ind]
                target = targets[ind]
                part = S[i : i + len(source)]
                if part == source:
                    ans += target
                else:
                    ans += part
                i += len(source)
        return ans
        
# V2
# time = O(n + m), m is the number of targets
# space = O(n)
class Solution(object):
    def findReplaceString(self, S, indexes, sources, targets):
        """
        :type S: str
        :type indexes: List[int]
        :type sources: List[str]
        :type targets: List[str]
        :rtype: str
        """
        S = list(S)
        bucket = [None] * len(S)
        for i in range(len(indexes)):
            if all(indexes[i]+k < len(S) and
                   S[indexes[i]+k] == sources[i][k]
                   for k in range(len(sources[i]))):
                bucket[indexes[i]] = (len(sources[i]), list(targets[i]))
        result = []
        last = 0
        for i in range(len(S)):
            if bucket[i]:
                result.extend(bucket[i][1])
                last = i + bucket[i][0]
            elif i >= last:
                result.append(S[i])
        return "".join(result)


# time = O(mlogm + m * n)
# space = O(n + m)
class Solution2(object):
    def findReplaceString(self, S, indexes, sources, targets):
        """
        :type S: str
        :type indexes: List[int]
        :type sources: List[str]
        :type targets: List[str]
        :rtype: str
        """
        for i, s, t in sorted(zip(indexes, sources, targets), reverse=True):
            if S[i:i+len(s)] == s:
                S = S[:i] + t + S[i+len(s):]

        return S