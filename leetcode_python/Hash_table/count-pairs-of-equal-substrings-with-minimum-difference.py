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
# IDEA: HASHMAP + IDX IN STR  (gemini)
"""
CORE IDEA:

    
    雜湊表紀錄`首, 末` 次出現位置



Steps:

    1. 雜湊表紀錄`首, 末` 次出現位置

    2. get `min diff`

    3. count how many different chars' diff equals to `min diff`

"""
class Solution(object):

  def countQuadruples(self, firstString, secondString):
    """:type firstString: str

    :type secondString: str
    :rtype: int
    """
    # 1. 記錄每個字元在 firstString 中「第一次」出現的索引 (min_i)
    first_min = {}
    for i, char in enumerate(firstString):
      if char not in first_min:
        first_min[char] = i

    # 2. 記錄每個字元在 secondString 中「最後一次」出現的索引 (max_a)
    second_max = {}
    for j, char in enumerate(secondString):
      second_max[char] = j  # 持續覆蓋，保留最後一次出現的位置

    # 3. 找出所有共同字元中的全域最小差值 (min_diff)
    min_diff = float("inf")
    for char in first_min:
      if char in second_max:
        diff = first_min[char] - second_max[char]
        if diff < min_diff:
          min_diff = diff

    if min_diff == float("inf"):
      return 0

    # 4. 計算有多少個不同的字元剛好達到了這個全域最小差值
    count = 0
    for char in first_min:
      if char in second_max:
        if first_min[char] - second_max[char] == min_diff:
          count += 1

    return count


# V0-2
# IDEA: HASHMAP + IDX IN STR  (gpt)
"""
CORE IDEA:

->
    雜湊表紀錄首末次出現位置


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
        # Store all positions of each character in secondString.
        positions = {}
        for a, ch in enumerate(secondString):
            positions.setdefault(ch, []).append(a)

        min_diff = float("inf")
        cnt = 0

        # Enumerate all substrings of firstString.
        n = len(firstString)

        for i in range(n):
            for j in range(i, n):
                sub = firstString[i:j + 1]

                # Find matching substrings in secondString.
                for a in positions.get(sub[0], []):
                    b = a + len(sub) - 1

                    if b < len(secondString) and secondString[a:b + 1] == sub:
                        diff = j - a

                        if diff < min_diff:
                            min_diff = diff
                            cnt = 1
                        elif diff == min_diff:
                            cnt += 1

        return cnt


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
