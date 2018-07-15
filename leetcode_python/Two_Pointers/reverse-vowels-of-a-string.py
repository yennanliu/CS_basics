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


# V1  : dev 
"""        
class Solution(object):
    def reverseVowels(self, s):
        vowels=['a','e','i','o','u']
        vowels_list = [x for x in s if x in vowels ]
        s_ = [x for x in s if x not in vowels ]
        output = s_ + vowels_list[::-1]
        return output
"""     




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




