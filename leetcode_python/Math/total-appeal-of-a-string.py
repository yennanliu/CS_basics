"""

2262. Total Appeal of A String
Hard

The appeal of a string is the number of distinct characters found in the string.

For example, the appeal of "abbca" is 3 because it has 3 distinct characters: 'a', 'b', and 'c'.

Given a string s, return the total appeal of all of its substrings.

A substring is a contiguous sequence of characters within a string.


Example 1:

Input: s = "abbca"
Output: 28
Explanation: The following are the substrings of "abbca":
- Substrings of length 1: "a", "b", "b", "c", "a" have an appeal of 1, 1, 1, 1, and 1 respectively. The sum is 5.
- Substrings of length 2: "ab", "bb", "bc", "ca" have an appeal of 2, 1, 2, and 2 respectively. The sum is 7.
- Substrings of length 3: "abb", "bbc", "bca" have an appeal of 2, 2, and 3 respectively. The sum is 7.
- Substrings of length 4: "abbc", "bbca" have an appeal of 3 and 3 respectively. The sum is 6.
- Substrings of length 5: "abbca" has an appeal of 3. The sum is 3.
The total sum is 5 + 7 + 7 + 6 + 3 = 28.

Example 2:

Input: s = "code"
Output: 20
Explanation: The following are the substrings of "code":
- Substrings of length 1: "c", "o", "d", "e" have an appeal of 1, 1, 1, and 1 respectively. The sum is 4.
- Substrings of length 2: "co", "od", "de" have an appeal of 2, 2, and 2 respectively. The sum is 6.
- Substrings of length 3: "cod", "ode" have an appeal of 3 and 3 respectively. The sum is 6.
- Substrings of length 4: "code" has an appeal of 4. The sum is 4.
The total sum is 4 + 6 + 6 + 4 = 20.


Constraints:

1 <= s.length <= 10^5
s consists of lowercase English letters.

"""

# V0
# IDEA : CONTRIBUTION / RUNNING DP (sum over substrings ending at i)
#
#   let cur = sum of appeal over all substrings ending exactly at index i.
#   going from i-1 to i we append s[i] to every substring ending at i-1, and
#   also add the single-char substring s[i].
#
#   appending s[i] raises the appeal by 1 for exactly those substrings whose
#   left end is AFTER the previous occurrence of s[i] (otherwise s[i] was
#   already inside). left ends in (pos[c], i] -> i - pos[c] of them.
#
#     cur += i - pos[c]        with pos[c] = -1 when c is unseen
#     res += cur
#
#   NOTE : the "+1" for the new length-1 substring is already included in the
#          i - pos[c] term (left end == i is one of them).
#
# time = O(n), space = O(26)
class Solution(object):
    def appealSum(self, s):
        pos = [-1] * 26
        res = 0
        cur = 0
        for i, ch in enumerate(s):
            c = ord(ch) - ord('a')
            cur += i - pos[c]
            res += cur
            pos[c] = i
        return res
