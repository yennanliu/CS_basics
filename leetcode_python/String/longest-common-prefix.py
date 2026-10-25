"""

14. Longest Common Prefix
Easy

Write a function to find the longest common prefix string amongst an array of strings.

If there is no common prefix, return an empty string "".

Example 1:

Input: ["flower","flow","flight"]
Output: "fl"
Example 2:

Input: ["dog","racecar","car"]
Output: ""
Explanation: There is no common prefix among the input strings.
Note:

All given inputs are in lowercase letters a-z.

"""


# V0
class Solution(object):
    def longestCommonPrefix(self, strs):
        """
        :type strs: List[str]
        :rtype: str
        """
        pass


# V0-1
# IDEA: SORT + prefix + Prefix Shrinking  (GPT)
class Solution(object):
    def longestCommonPrefix(self, strs):
        """
        :type strs: List[str]
        :rtype: str
        """
        # Edge case
        if not strs:
            return ""

        """
        NOTE !!!

        sorting is NOT necessary
        """
        # Sort by length: shortest -> longest
        strs.sort(key=lambda x: len(x))

        prefix = strs[0]

        for i in range(1, len(strs)):
            val = strs[i]

            # Keep shortening prefix until it matches
            while not val.startswith(prefix):
                """
                NOTE !!!


                we shorten prefix from right hand side via below trick:

                ```
                prefix = prefix[:-1]
                ```

                ----

                Demo:

                ```
                >>> x
                'flower'
                >>> x[:-1]
                'flowe'
                >>> x[:-1][:-1]
                'flow'
                >>> x[:-1][:-1][:-1]
                'flo'
                ```

                """
                prefix = prefix[:-1]

                if not prefix:
                    return ""

        return prefix


# V0-2
# IDEA: SORT + prefix + Prefix Shrinking  (gemini)
class Solution(object):

  def longestCommonPrefix(self, strs):
    """:type strs: List[str]

    :rtype: str
    """
    if not strs:
      return ""

    # 以第一個字串作為初始的共同前綴基準
    prefix = strs[0]

    for i in range(1, len(strs)):
      # 當前字串不以 prefix 開頭時，不斷將 prefix 尾端縮減
      while not strs[i].startswith(prefix):
        prefix = prefix[:-1]
        if not prefix:
          return ""

    return prefix


# V0-3
# IDEA: SORT + prefix + TWO POINTERS (gemini)
class Solution(object):

  def longestCommonPrefix(self, strs):
    """:type strs: List[str]

    :rtype: str
    """
    if not strs:
      return ""

    """
    NOTE !!!

    sorting is NOT necessary
    """
    # 按照長度排序，讓最短的字串當作初始基準
    strs.sort(key=lambda x: len(x))
    prefix = strs[0]

    for i in range(1, len(strs)):
      val = strs[i]

      # 直接找出 prefix 與當前 val 從頭開始匹配的最大長度 j
      j = 0
      while j < len(prefix) and j < len(val) and prefix[j] == val[j]:
        j += 1

      # 將 prefix 直接截斷到實際匹配的長度
      prefix = prefix[:j]

      if not prefix:
        return ""

    return prefix


# V0-4
# time = O(n * k), n = number of strings, k = length of common prefix
# space = O(k)
class Solution:
    def longestCommonPrefix(self, strs):
        if not strs:
            return ""
        r = ""
        # NOTE : in 1st loop, we loop element in 1st str
        for i in range(len(strs[0])):
            # NOTE : in 2nd loop, we loop sub-str in strs
            for j in range(1, len(strs)):
                # NOTE : i >= len(strs[j]), meaning if index in 1st sub-str already bigger than the next sub-str, so there is no possible `longest prefix` anymore, then we return current result
                if i >= len(strs[j]) or strs[0][i] != strs[j][i]:
                    return r
            r += strs[0][i]
        return r
    

# V0-5
# IDEA: SORT + prefix  (GPT)
class Solution(object):
    def longestCommonPrefix(self, strs):
        """
        :type strs: List[str]
        :rtype: str
        """
        # Edge case
        if not strs:
            return ""

        # Sort by length
        strs.sort(key=lambda x: len(x))

        # The shortest string is the maximum possible prefix
        prefix = strs[0]

        for i in range(1, len(strs)):
            val = strs[i]

            # Shrink prefix until it matches the current string
            while prefix and not val.startswith(prefix):
                prefix = prefix[:-1]

            # No common prefix remains
            if not prefix:
                return ""

        return prefix


# V0-6
# IDEA: 前綴縮減法（Prefix Shrinking  $\mathcal{O}(S)$ 演算法） (gemini)
class Solution(object):

  def longestCommonPrefix(self, strs):
    """:type strs: List[str]

    :rtype: str
    """
    if not strs:
      return ""

    # 將第一個字串作為初始的公共前綴候選
    prefix = strs[0]

    # 依次與其餘字串進行比對
    for s in strs[1:]:
      # 當前字串 s 不是以 prefix 開頭時，逐步縮減 prefix 的長度
      while not s.startswith(prefix):
        prefix = prefix[:-1]
        if not prefix:
          return ""

    return prefix



# V1
# https://blog.csdn.net/coder_orz/article/details/51706442
# time = O(n * k), n = number of strings, k = length of common prefix
# space = O(k)
class Solution(object):
    def longestCommonPrefix(self, strs):
        """
        :type strs: List[str]
        :rtype: str
        """
        if not strs:
            return ''
        res = ''
        for i in range(len(strs[0])):
            for j in range(1, len(strs)):
                if i >= len(strs[j]) or strs[j][i] != strs[0][i]:
                    return res
            res += strs[0][i]
        return res

# V1'
# https://blog.csdn.net/coder_orz/article/details/51706442
# time = O(n * m * log n), n = number of strings, m = max string length (sort dominates)
# space = O(k), k = length of common prefix
class Solution(object):
    def longestCommonPrefix(self, strs):
        """
        :type strs: List[str]
        :rtype: str
        """
        if not strs:
            return ''
        strs.sort()
        res = ''
        for i in range(len(strs[0])):
            if i >= len(strs[-1]) or strs[-1][i] != strs[0][i]:
                return res
            res += strs[0][i]
        return res

# V1''
# https://blog.csdn.net/coder_orz/article/details/51706442
# IDEA : ZIP PYTHON DEFAULT FUNC
# time = O(n * k), n = number of strings, k = length of common prefix
# space = O(n)
class Solution(object):
    def longestCommonPrefix(self, strs):
        """
        :type strs: List[str]
        :rtype: str
        """
        if not strs:
            return ''
        for i, chars in enumerate(zip(*strs)):
            if len(set(chars)) > 1:
                return strs[0][:i]
        return min(strs)

# V2
# time = O(n * k), n = number of strings, k = length of common prefix
# space = O(1)
class Solution(object):
    def longestCommonPrefix(self, strs):
        """
        :type strs: List[str]
        :rtype: str
        """
        if not strs:
            return ""

        for i in range(len(strs[0])):
            for string in strs[1:]:
                if i >= len(string) or string[i] != strs[0][i]:
                    return strs[0][:i]
        return strs[0]


# time = O(n * k), n = number of strings, k = length of common prefix
# space = O(k)
class Solution2(object):
    def longestCommonPrefix(self, strs):
        """
        :type strs: List[str]
        :rtype: str
        """
        prefix = ""
        
        for chars in zip(*strs):
            if all(c == chars[0] for c in chars):
                prefix += chars[0]
            else:
                return prefix
            
        return prefix
