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
# IDEA : HASH MAP (FIRST IDX IN firstString, LAST IDX IN secondString)
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


# V0-1
# IDEA : ONE PASS OVER firstString + LAST-IDX ARRAY OF SIZE 26
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
