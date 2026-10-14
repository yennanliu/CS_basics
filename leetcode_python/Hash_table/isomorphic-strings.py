"""

205. Isomorphic Strings
Easy

Given two strings s and t, determine if they are isomorphic.

Two strings s and t are isomorphic if the characters in s can be replaced to get t.

All occurrences of a character must be replaced with another character while preserving the order of characters. No two characters may map to the same character, but a character may map to itself.

Example 1:

Input: s = "egg", t = "add"
Output: true
Explanation:

The strings s and t can be made identical by:

Mapping 'e' to 'a'.
Mapping 'g' to 'd'.

Example 2:

Input: s = "f11", t = "b23"
Output: false
Explanation:

The strings s and t can not be made identical as '1' needs to be mapped to both '2' and '3'.

Example 3:

Input: s = "paper", t = "title"
Output: true

Constraints:

1 <= s.length <= 5 * 10^4
t.length == s.length
s and t consist of any valid ascii character.

"""

# time = O(n)
# space = O(1)

# Given two strings s and t, determine if they are isomorphic.
#
# Two strings are isomorphic if the characters in s can be replaced to get t.
#
# All occurrences of a character must be replaced with another character
# while preserving the order of characters. No two characters may map to
# the same character but a character may map to itself.
#
# For example,
# Given "egg", "add", return true.
#
# Given "foo", "bar", return false.
#
# Given "paper", "title", return true.
#
# Note:
# You may assume both s and t have the same length.

# V0 


# V0-1
# IDEA: HASHMAP: MAPPING, REVERSE MAPPING (gpt)
class Solution(object):
    def isIsomorphic(self, s, t):
        """
        :type s: str
        :type t: str
        :rtype: bool
        """
        # Edge cases
        if len(s) != len(t):
            return False

        # Mapping: s character -> t character
        char_map = {}

        # Reverse mapping: t character -> s character
        reverse_map = {}

        for i in range(len(s)):
            val_s = s[i]
            val_t = t[i]

            # Check s -> t mapping
            if val_s in char_map:
                if char_map[val_s] != val_t:
                    return False
            else:
                char_map[val_s] = val_t

            # Check t -> s mapping
            if val_t in reverse_map:
                if reverse_map[val_t] != val_s:
                    return False
            else:
                reverse_map[val_t] = val_s

        return True



# V0-2
# IDEA: HASH MAP
class Solution(object):
    def isIsomorphic(self, s, t):
        """
        :type s: str
        :type t: str
        :rtype: bool
        """
        # edge
        if not s or not t:
          return False

        if not s and not t:
          return True

        if len(s) != len(t):
          return False

        _len = len(s)

        # {val_s: val_t}
        _map = {}

        for i in range(_len):
          val_s = s[i]
          val_t = t[i]

          if val_s in _map:
            if _map[val_s] != val_t:
              return False

          else:

            """
            NOTE !!!

            -> can ONLY have `1 to 1` mapping

                -> e.g. No two characters may map to the same character,
            """
            if val_t in _map.values():
              return False
            _map[val_s] = val_t

        return True


# V0-3
# IDEA: HASHMAP: MAPPING, REVERSE MAPPING (GEMINI)
class Solution(object):

  def isIsomorphic(self, s, t):
    """:type s: str

    :type t: str
    :rtype: bool
    """
    # 邊界檢查：長度不同直接回傳 False（空字串長度相同會直接通過）
    if len(s) != len(t):
      return False

    s_to_t = {}
    t_to_s = {}

    for char_s, char_t in zip(s, t):
      # 1. 檢查 s 到 t 的對應是否衝突
      if char_s in s_to_t and s_to_t[char_s] != char_t:
        return False

      # 2. 檢查 t 到 s 的對應是否衝突（防止多對一）
      if char_t in t_to_s and t_to_s[char_t] != char_s:
        return False

      # 建立雙向對應
      s_to_t[char_s] = char_t
      t_to_s[char_t] = char_s

    return True


# V1 
# https://blog.csdn.net/liuxiao214/article/details/77587070
# IDEA : HASH TABLE 
# time = O(n)
# space = O(n)
class Solution(object):
    def isIsomorphic(self, s, t):
        """
        :type s: str
        :type t: str
        :rtype: bool
        """
        def sub(s,t):
            m={}
            for i in range(len(s)):
                if s[i] not in m.keys():
                    m[s[i]]=t[i]
                else:
                    if m[s[i]]!=t[i]:
                        return False
            return True
        return sub(s, t) and sub(t,s)

# V1' 
# https://blog.csdn.net/liuxiao214/article/details/77587070
# IDEA : MAPPING + ORD 
# time = O(n)
# space = O(1)
class Solution1(object):
    def isIsomorphic(self, s, t):
        """
        :type s: str
        :type t: str
        :rtype: bool
        """
        a1=[-1]*256
        a2=[-1]*256
        for i in range(len(s)):
            if a1[ord(s[i])] != a2[ord(t[i])]:
                return False
            a1[ord(s[i])]=i
            a2[ord(t[i])]=i
        return True

# V1''
# https://www.jiuzhang.com/solution/isomorphic-strings/#tag-highlight-lang-python
# time = O(n)
# space = O(n)
class Solution:
    """
    @param s: a string
    @param t: a string
    @return: true if the characters in s can be replaced to get t or false
    """
    def isIsomorphic(self, s, t):
        # write your code here
        map1 = {}
        map2 = {}
        for i in range(len(s)):
            if s[i] not in map1:
                map1[s[i]] = t[i]
            elif map1[s[i]] != t[i]:
                return False
        for i in range(len(t)):
            if t[i] not in map2:
                map2[t[i]] = s[i]
            elif map2[t[i]] != s[i]:
                return False
        return True
        
# V2 
# time = O(n)
# space = O(1)
from itertools import izip  # Generator version of zip.
class Solution(object):
    def isIsomorphic(self, s, t):
        """
        :type s: str
        :type t: str
        :rtype: bool
        """
        if len(s) != len(t):
            return False

        s2t, t2s = {}, {}
        for p, w in izip(s, t):
            if w not in s2t and p not in t2s:
                s2t[w] = p
                t2s[p] = w
            elif w not in s2t or s2t[w] != p:
                # Contradict mapping.
                return False
        return True

# time = O(n)
# space = O(1)
class Solution2(object):
    def isIsomorphic(self, s, t):
        if len(s) != len(t):
            return False

        return self.halfIsom(s, t) and self.halfIsom(t, s)

    def halfIsom(self, s, t):
        lookup = {}
        for i in range(len(s)):
            if s[i] not in lookup:
                lookup[s[i]] = t[i]
            elif lookup[s[i]] != t[i]:
                return False
        return True
