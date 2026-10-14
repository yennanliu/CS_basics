# https://leetcode.ca/2021-05-16-1794-Count-Pairs-of-Equal-Substrings-With-Minimum-Difference/

"""

1794. Count Pairs of Equal Substrings With Minimum Difference
Medium

You are given two strings firstString and secondString that are 0-indexed
and consist only of lowercase English letters. Count the number of index
quadruples (i,j,a,b) that satisfy the following conditions:

- 0 <= i <= j < firstString.length
- 0 <= a <= b < secondString.length
- The substring of firstString that starts at the ith character and ends at
  the jth character (inclusive) is equal to the substring of secondString
  that starts at the ath character and ends at the bth character (inclusive).
- j - a is the minimum possible value among all quadruples that satisfy the
  previous conditions.

Return the number of such quadruples.


Example 1:

Input: firstString = "abcd", secondString = "bccda"

Output: 1

Explanation:

The quadruple (0,0,4,4) is the only one that satisfies all the conditions
and minimizes j - a.

Example 2:

Input: firstString = "ab", secondString = "cd"

Output: 0

Explanation:

There are no quadruples satisfying all the conditions.


Constraints:

1 <= firstString.length, secondString.length <= 2 * 10^5
Both strings consist only of lowercase English letters.

"""

# V0
class Solution(object):
    def countQuadruples(self, firstString, secondString):
        pass


# V0-1
# IDEA: HASHMAP + IDX IN STR  (gpt)
"""
CORE IDEA:

->
    找出相同子字串在兩個字串中的位置，並最大化 j-i，
    其中 i 是第一個字串中子字串的起始位置，j 是第二個字串中子字串的起始位置。

"""
class Solution(object):
    def countQuadruples(self, firstString, secondString):
        """
        :type firstString: str
        :type secondString: str
        :rtype: int
        """
        # Store the first occurrence of each character in secondString.
        first_pos = {}
        for j, ch in enumerate(secondString):
            if ch not in first_pos:
                first_pos[ch] = j

        res = -1

        # Find the maximum distance between matching characters.
        for i, ch in enumerate(firstString):
            if ch in first_pos:
                res = max(res, first_pos[ch] - i)

        return res


# V0-2
# IDEA: HASHMAP + IDX IN STR  (gemini)
class Solution(object):

  def countQuadruples(self, firstString, secondString):
    """:type firstString: str

    :type secondString: str
    :rtype: int
    """
    # 1. 記錄 secondString 中每個字元「第一次」出現的索引
    second_map = {}
    for j, char in enumerate(secondString):
      if char not in second_map:
        second_map[char] = j

    # 2. 記錄 firstString 中每個字元「最後一次」出現的索引
    first_map = {}
    for i, char in enumerate(firstString):
      first_map[char] = i  # 不斷覆蓋，保留最後一次出現的位置

    min_diff = float("inf")

    # 3. 尋找共同字元，計算最小差值 (j - i)
    for char in first_map:
      if char in second_map:
        diff = second_map[char] - first_map[char]
        if diff < min_diff:
          min_diff = diff

    return min_diff if min_diff != float("inf") else -1


# V0-3
# IDEA : HASH MAP (FIRST IDX IN firstString, LAST IDX IN secondString) (claude)
#
#   only SINGLE-CHAR substrings can reach the minimum of j - a :
#
#   -> if firstString[i..j] == secondString[a..b] with length > 1, then the
#      first char alone, (i, i, a, a), is also a valid match, and its j (= i)
#      is strictly smaller while a stays the same -> smaller j - a
#
#   so for a single char c the quadruple is (i, i, a, a) and j - a = i - a,
#   which is smallest when i is c's FIRST index in firstString and a is c's
#   LAST index in secondString.
#
#   each char then gives exactly ONE best quadruple -> the answer is the
#   number of chars whose (first_idx - last_idx) equals the global minimum.
#
#   e.g. "abcd", "bccda" -> a: 0-4=-4, b: 1-0=1, c: 2-2=0, d: 3-3=0
#                        -> min = -4, reached by 'a' only -> 1
#
#   (brute force over every (i, a) pair of equal chars is O(m * n) -> TLE)
#
# time = O(m + n), space = O(1)  (at most 26 keys per map)
class Solution(object):
    def countQuadruples(self, firstString, secondString):
        """
        :type firstString: str
        :type secondString: str
        :rtype: int
        """
        # char -> its FIRST index in firstString
        first_idx = {}
        for i in range(len(firstString)):
            ch = firstString[i]
            if ch not in first_idx:
                first_idx[ch] = i

        # char -> its LAST index in secondString (later indices overwrite)
        last_idx = {}
        for a in range(len(secondString)):
            last_idx[secondString[a]] = a

        min_diff = float('inf')
        count = 0

        for ch in first_idx:
            # a char missing from secondString cannot form any quadruple
            if ch not in last_idx:
                continue

            diff = first_idx[ch] - last_idx[ch]

            # NOTE !!! a strictly smaller diff RESETS the count to 1,
            #          an equal diff adds one more quadruple
            if diff < min_diff:
                min_diff = diff
                count = 1
            elif diff == min_diff:
                count += 1

        return count


# V0-4
# IDEA : ONE PASS OVER firstString + LAST-IDX ARRAY OF SIZE 26 (claude)
#
#   same observation as V0, but without the first-index map : scan
#   firstString left to right and score EVERY occurrence.
#
#   a repeated char cannot be miscounted : its later occurrence has a larger
#   i and the same last_idx, so its diff is strictly larger than the one its
#   first occurrence already produced -> it can never equal the minimum.
#
# time = O(m + n), space = O(1)
class Solution2(object):
    def countQuadruples(self, firstString, secondString):
        """
        :type firstString: str
        :type secondString: str
        :rtype: int
        """
        NOT_FOUND = -1

        # last_idx[k] = last index of chr(ord('a') + k) in secondString
        last_idx = [NOT_FOUND] * 26
        for a in range(len(secondString)):
            last_idx[ord(secondString[a]) - ord('a')] = a

        min_diff = float('inf')
        count = 0

        for i in range(len(firstString)):
            a = last_idx[ord(firstString[i]) - ord('a')]
            if a == NOT_FOUND:
                continue

            diff = i - a
            if diff < min_diff:
                min_diff = diff
                count = 1
            elif diff == min_diff:
                count += 1

        return count


# V1
# https://leetcode.ca/2021-05-16-1794-Count-Pairs-of-Equal-Substrings-With-Minimum-Difference/
class Solution:
    def countQuadruples(self, firstString: str, secondString: str) -> int:
        last = {c: i for i, c in enumerate(secondString)}
        ans, mi = 0, inf
        for i, c in enumerate(firstString):
            if c in last:
                t = i - last[c]
                if mi > t:
                    mi = t
                    ans = 1
                elif mi == t:
                    ans += 1
        return ans
