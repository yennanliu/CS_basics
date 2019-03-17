

# V1  : dev 

# class Solution(object):
# 	def convertToTitle(self, n):
# 		inverse_alphabet_dict = {1: 'A', 2: 'B', 3: 'C', 4: 'D', 5: 'E', 6: 'F', 7: 'G', 8: 'H', 9: 'I', 10: 'J', 11: 'K', 12: 'L', 13: 'M', 14: 'N', 15: 'O', 16: 'P', 17: 'Q', 18: 'R', 19: 'S', 20: 'T', 21: 'U', 22: 'V', 23: 'W', 24: 'X', 25: 'Y', 26: 'Z'}
# 		r = int(n%26) 
# 		q = int((n - (n%26 ))/26)
# 		return inverse_alphabet_dict[q] + inverse_alphabet_dict[r] 

# V2 
# https://www.jianshu.com/p/591d3a2ab45d
class Solution(object):
    def convertToTitle(self, n):
        tar = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        result = ""
        while n > 0:
            m = (n-1) % 26
            result += tar[m]
            if m == 0:
                n = n + 1
            n = (n-1) / 26
        return result[::-1]

# V3 
# Time:  O(logn)
# Space: O(1)

class Solution(object):
    def convertToTitle(self, n):
        """
        :type n: int
        :rtype: str
        """
        result, dvd = "", n

        while dvd:
            result += chr((dvd - 1) % 26 + ord('A'))
            dvd = (dvd - 1) / 26

        return result[::-1]