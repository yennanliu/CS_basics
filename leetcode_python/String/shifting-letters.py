# V0 

# V1
# https://blog.csdn.net/fuxuemingzhu/article/details/80644702
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
# Time:  O(n)
# Space: O(1)
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