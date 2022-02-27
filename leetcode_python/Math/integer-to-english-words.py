"""

273. Integer to English Words
Hard

Convert a non-negative integer num to its English words representation.

 

Example 1:

Input: num = 123
Output: "One Hundred Twenty Three"
Example 2:

Input: num = 12345
Output: "Twelve Thousand Three Hundred Forty Five"
Example 3:

Input: num = 1234567
Output: "One Million Two Hundred Thirty Four Thousand Five Hundred Sixty Seven"
 

Constraints:

0 <= num <= 231 - 1

"""

# V0

# V1
# https://leetcode.com/problems/integer-to-english-words/discuss/70643/Short-Python
class Solution(object):
    def numberToWords(self, num):
        """
        :type num: int
        :rtype: str
        """
        A = ["", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", \
             "Nine", "Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", \
             "Sixteen", "Seventeen", "Eighteen", "Nineteen"]
        B = ["Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety"]
        C = ["Billion", "Million", "Thousand"]
        def trans(n):
            return [] if not n else [A[n // 100], "Hundred"] + trans(n%100) if n >= 100 \
                   else [A[n]] if n < 20 else [B[(n//10)-2]] + trans(n%10)
                    
        res, N = [], [10**9, 10**6, 1000, 1]
        for i in range(4):
            digit, num = divmod(num, N[i])
            if digit: res += trans(digit) + C[i:i+1]
        return " ".join(res) or "Zero"

# V1'
class Solution:
    def numberToWords(self, num):
        tens = [
            "",
            "Ten",
            "Twenty",
            "Thirty",
            "Forty",
            "Fifty",
            "Sixty",
            "Seventy",
            "Eighty",
            "Ninety"
        ]
        
        teens = {
            10: 'Ten',
            11: 'Eleven',
            12: 'Twelve',
            13: 'Thirteen',
            14: 'Fourteen',
            15: 'Fifteen',
            16: 'Sixteen',
            17: 'Seventeen',
            18: 'Eighteen',
            19: 'Nineteen',
        }        
        
        digits = [
            "",
            "One",
            "Two",
            "Three",
            "Four",
            "Five",
            "Six",
            "Seven",
            "Eight",
            "Nine"
        ]
        
        def parse(num):
            if num >= 1000000000:
                return parse(num // 1000000000).strip() + " Billion " + parse(num % 1000000000).strip()
            elif num >= 1000000:
                return parse(num // 1000000).strip() + " Million " + parse(num % 1000000).strip()
            elif num >= 1000:
                return parse(num // 1000).strip() + " Thousand " + parse(num % 1000).strip()
            elif num >= 100:
                return digits[num // 100] + " Hundred " + parse(num % 100).strip()
            elif num >= 20:
                return tens[num//10] + " " + parse(num % 10).strip()
            elif num >= 10 and num < 20:
                return teens[num]             
            else:
                return digits[num]
        
        if num == 0:
            return "Zero"
        return parse(num).strip()

# V1''
# (TLE)
# https://leetcode.com/problems/integer-to-english-words/discuss/213494/Python-solution
class Solution:
    def numberToWords(self, num):
        """
        :type num: int
        :rtype: str
        """
        def helper(num):
            if num < 10:
                return dic1[num]
            elif 10 <= num < 20:
                return dic3[num]
            elif num < 100:
                res = []
                q, num = divmod(num, 10)
                res.append(dic2[q*10])
                if num > 0:
                    res.append(" "+dic1[num])
                return "".join(res)
            elif num < 1000:
                q, num = divmod(num, 100)
                if num == 0:
                    return dic1[q]+" "+"Hundred"
                else:
                    return dic1[q]+" "+"Hundred"+" "+helper(num)
            elif num < 1000000:
                q, num = divmod(num, 1000)
                if num == 0:
                    return helper(q)+" "+"Thousand"
                else:
                    return helper(q)+" "+"Thousand"+" "+helper(num)
            elif num < 1000000000:
                q, num = divmod(num, 1000000)
                if num == 0:
                    return helper(q)+" "+"Million"
                else:
                    return helper(q)+" "+"Million"+" "+helper(num)
            else:
                q, num = divmod(num, 1000000000)
                if num == 0:
                    return helper(q)+" "+"Billion"
                else:
                    return helper(q)+" "+"Billion"+" "+helper(num)  
                
        if num == 0:
            return "Zero"
        
        dic1 = {9:"Nine", 8:"Eight", 7:"Seven", 6:"Six", 
                5:"Five", 4:"Four", 3:"Three", 2:"Two", 1:"One"}
        dic2 = {90:"Ninety", 80:"Eighty", 70:"Seventy", 60:"Sixty", 
                50:"Fifty", 40:"Forty", 30:"Thirty", 20:"Twenty"} 
        dic3 = {19:"Nineteen", 18:"Eighteen", 17:"Seventeen", 
                16:"Sixteen", 15:"Fifteen", 14:"Fourteen", 13:"Thirteen", 
                12:"Twelve", 11:"Eleven", 10:"Ten"}
        
        return helper(num)

# V2