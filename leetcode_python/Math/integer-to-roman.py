

# V1 
# class Solution(object):
#     def intToRoman(self, num):
#         """
#         :type num: int
#         :rtype: str
#         """
#         numeral_map = {1: "I", 4: "IV", 5: "V", 9: "IX", \
#                        10: "X", 40: "XL", 50: "L", 90: "XC", \
#                        100: "C", 400: "CD", 500: "D", 900: "CM", \
#                        1000: "M"}



# V2 
# https://blog.csdn.net/NXHYD/article/details/72461202
class Solution(object):
    def intToRoman(self, num):
        import math 
        """
        :type num: int
        :rtype: str
        """
        M = ["", "M", "MM", "MMM"]
        C = ["", "C", "CC", "CCC", "CD", "D", "DC", "DCC", "DCCC", "CM"]
        X = ["", "X", "XX", "XXX", "XL", "L", "LX", "LXX", "LXXX", "XC"]
        I = ["", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX"]
        return M[(math.floor(num/1000))] + C[(math.floor(num%1000)/100)] + X[(math.floor(num%100)/10)] + I[(math.floor(num%10))]


# V3
# Time:  O(n)
# Space: O(1)

class Solution(object):
    def intToRoman(self, num):
        """
        :type num: int
        :rtype: str
        """
        numeral_map = {1: "I", 4: "IV", 5: "V", 9: "IX", \
                       10: "X", 40: "XL", 50: "L", 90: "XC", \
                       100: "C", 400: "CD", 500: "D", 900: "CM", \
                       1000: "M"}
        keyset, result = sorted(numeral_map.keys()), []

        while num > 0:
            for key in reversed(keyset):
                while num / key > 0:
                    num -= key
                    result += numeral_map[key]

        return "".join(result)