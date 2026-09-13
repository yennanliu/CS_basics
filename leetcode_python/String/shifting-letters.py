"""

848. Shifting Letters
Medium

You are given a string s of lowercase English letters and an integer array shifts of the same length.

Call the shift() of a letter, the next letter in the alphabet, (wrapping around so that 'z' becomes 'a').

For example, shift('a') = 'b', shift('t') = 'u', and shift('z') = 'a'.

Now for each shifts[i] = x, we want to shift the first i + 1 letters of s, x times.

Return the final string after all such shifts to s are applied.

Example 1:

Input: s = "abc", shifts = [3,5,9]
Output: "rpl"
Explanation: We start with "abc".
After shifting the first 1 letters of s by 3, we have "dbc".
After shifting the first 2 letters of s by 5, we have "igc".
After shifting the first 3 letters of s by 9, we have "rpl", the answer.

Example 2:

Input: s = "aaa", shifts = [1,2,3]
Output: "gfd"

Constraints:

1 <= s.length <= 10^5
s consists of lowercase English letters.
shifts.length == s.length
0 <= shifts[i] <= 10^9

"""

# V0 

# V1
# https://blog.csdn.net/fuxuemingzhu/article/details/80644702
# time = O(n)
# space = O(n)
class Solution(object):
    def shiftingLetters(self, S, shifts):
        """
        :type S: str
        :type shifts: List[int]
        :rtype: str
        """
        _len = len(S)
        shifts_sum = sum(shifts)
        shifts_real = []
        for shift in shifts:
            shifts_real.append(shifts_sum)
            shifts_sum -= shift
        def shift_map(string, shift_time):
            shifted = ord(s) + (shift_time % 26)
            return chr(shifted if shifted <= ord('z') else shifted - 26)
        ans = ''
        for i, s in enumerate(S):
            ans += shift_map(s, shifts_real[i])
        return ans

# V2
# time = O(n)
# space = O(1)
# IDEA
# since the alphabet is cyclic. i.e. a, b, c..z, a ,b, c...
# so we can get shift(num) = shift(num % 26) 
class Solution(object):
    def shiftingLetters(self, S, shifts):
        """
        :type S: str
        :type shifts: List[int]
        :rtype: str
        """

        result = []
        times = sum(shifts) % 26
        for i, c in enumerate(S):
            index = ord(c) - ord('a')
            result.append(chr(ord('a') + (index+times) % 26))
            times = (times-shifts[i]) % 26 # each shifts[i] = x, we want to shift the first i+1 letters of S, x times.
        return "".join(result)