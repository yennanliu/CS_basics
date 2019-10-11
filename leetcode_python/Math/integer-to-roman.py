
# V0 
# IDEA 
# In [8]: 1200%100
# Out[8]: 0
# In [9]: 1234%1000
# Out[9]: 234
# In [10]: 1234%100
# Out[10]: 34
# In [11]: 1234%10
# Out[11]: 4
class Solution(object):
    def intToRoman(self, num):
        """
        :type num: int
        :rtype: str
        """
        M = ["", "M", "MM", "MMM"]
        C = ["", "C", "CC", "CCC", "CD", "D", "DC", "DCC", "DCCC", "CM"]
        X = ["", "X", "XX", "XXX", "XL", "L", "LX", "LXX", "LXXX", "XC"]
        I = ["", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX"]
        return M[num/1000] + C[(num%1000)/100] + X[(num%100)/10] + I[num%10]

# V1
# https://blog.csdn.net/NXHYD/article/details/72461202
class Solution(object):
    def intToRoman(self, num):
        """
        :type num: int
        :rtype: str
        """
        c = [['','I','II','III','IV','V','VI','VII','VIII','IX'],
             ['','X','XX','XXX','XL','L','LX','LXX','LXXX','XC'],
             ['','C','CC','CCC','CD','D','DC','DCC','DCCC','CM'],
             ['','M','MM','MMM']]
        s = []
        i = 3
        while num :
            s.append(c[i][num/pow(10, i)])
            num %= pow(10, i)
            i -= 1
        return ''.join(s)

# V1' 
# https://blog.csdn.net/NXHYD/article/details/72461202
class Solution(object):
    def intToRoman(self, num):
        """
        :type num: int
        :rtype: str
        """
        M = ["", "M", "MM", "MMM"]
        C = ["", "C", "CC", "CCC", "CD", "D", "DC", "DCC", "DCCC", "CM"]
        X = ["", "X", "XX", "XXX", "XL", "L", "LX", "LXX", "LXXX", "XC"]
        I = ["", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX"]
        return M[num/1000] + C[(num%1000)/100] + X[(num%100)/10] + I[num%10]

# V1'' 
# https://www.jiuzhang.com/solution/integer-to-roman/#tag-highlight-lang-python
class Solution:
    def parse(self, digit, index):
        NUMS = {
            1: 'I',
            2: 'II',
            3: 'III',
            4: 'IV',
            5: 'V',
            6: 'VI',
            7: 'VII',
            8: 'VIII',
            9: 'IX',
        }
        ROMAN = {
            'I': ['I', 'X', 'C', 'M'],
            'V': ['V', 'L', 'D', '?'],
            'X': ['X', 'C', 'M', '?']
        }
        
        s = NUMS[digit]
        return s.replace('X', ROMAN['X'][index]).replace('I', ROMAN['I'][index]).replace('V', ROMAN['V'][index])

    # @param {integer} num
    # @return {string}        
    def intToRoman(self, num):
        s = ''
        index = 0
        while num != 0:
            digit = num % 10
            if digit != 0:
                s = self.parse(digit, index) + s
            num = num / 10
            index += 1
        return s

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