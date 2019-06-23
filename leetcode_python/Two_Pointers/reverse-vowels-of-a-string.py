# Time:  O(n)
# Space: O(1)

# Write a function that takes a string as input
# and reverse only the vowels of a string.
#
# Example 1:
# Given s = "hello", return "holle".
#
# Example 2:
# Given s = "leetcode", return "leotcede".

# V1  :  
import re 
class Solution(object):
    def reverseVowels(self, s):
        index = []
        vowels = ['a','e','i','o','u', 'A','E','I','O','U'] 
        for k, v  in enumerate(list(s)):
            if v in vowels:
                index.append(k)     
        s_list = list(s)
        for i in range(int(len(index)/2)):
            s_list[index[i]],  s_list[index[::-1][i]]  =  s_list[index[::-1][i]], s_list[index[i]]
        return ''.join(x for x in s_list)

# V2 
class Solution(object):
    def reverseVowels(self, s):
        """
        :type s: str
        :rtype: str
        """
        vowels = "aeiou"
        string = list(s)
        i, j = 0, len(s) - 1
        while i < j:
            if string[i].lower() not in vowels:
                i += 1
            elif string[j].lower() not in vowels:
                j -= 1
            else:
                string[i], string[j] = string[j], string[i]
                i += 1
                j -= 1
        return "".join(string)

# V3 
class Solution(object):
    def reverseVowels(self, s):
        """
        :type s: str
        :rtype: str
        """
        vowels = re.findall('(?i)[aeiou]', s)
        return re.sub('(?i)[aeiou]', lambda m: vowels.pop(), s)
