"""

3076. Shortest Uncommon Substring in an Array
Medium

You are given an array arr of size n consisting of non-empty strings.

Find a string array answer of size n such that:

answer[i] is the shortest substring of arr[i] that does not occur as a substring in any other string in arr. If multiple such substrings exist, answer[i] should be the lexicographically smallest. And if no such substring exists, answer[i] should be an empty string.

Return the array answer.


Example 1:

Input: arr = ["cab","ad","bad","c"]
Output: ["ab","","ba",""]
Explanation: We have the following:
- For the string "cab", the shortest substring that does not occur in any other string is either "ca" or "ab", we choose the lexicographically smaller substring, which is "ab".
- For the string "ad", there is no substring that does not occur in any other string.
- For the string "bad", the shortest substring that does not occur in any other string is "ba".
- For the string "c", there is no substring that does not occur in any other string.

Example 2:

Input: arr = ["abc","bcd","abcd"]
Output: ["","","abcd"]
Explanation: We have the following:
- For the string "abc", there is no substring that does not occur in any other string.
- For the string "bcd", there is no substring that does not occur in any other string.
- For the string "abcd", the shortest substring that does not occur in any other string is "abcd".


Constraints:

n == arr.length
2 <= n <= 100
1 <= arr[i].length <= 20
arr[i] consists only of lowercase English letters.

"""

# V0
# IDEA : THE INPUT IS TINY — ENUMERATE SUBSTRINGS SHORTEST-FIRST
#
#   a 20-character string has at most 20*21/2 = 210 substrings, and there are
#   at most 100 strings, so testing everything is cheap.
#
#   iterating lengths from 1 upwards, and left-to-right within a length,
#   would give the first hit in length order but NOT in lexicographic order
#   among equals — so collect all candidates of the current length, and take
#   min() of the ones that appear nowhere else. the first length with any
#   survivor wins.
#
# time = O(n^2 * L^3), space = O(L^2)
class Solution(object):
    def shortestSubstrings(self, arr):
        n = len(arr)
        res = []
        for i, s in enumerate(arr):
            others = [arr[j] for j in range(n) if j != i]
            found = ""
            for length in range(1, len(s) + 1):
                cands = [s[t:t + length] for t in range(len(s) - length + 1)]
                unique = [c for c in cands if all(c not in o for o in others)]
                if unique:
                    found = min(unique)
                    break
            res.append(found)
        return res
