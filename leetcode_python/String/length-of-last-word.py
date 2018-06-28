
# Time:  O(n)
# Space: O(1)
#
# Given a string s consists of upper/lower-case alphabets and empty space characters ' ', return the length of last word in the string.
#
# If the last word does not exist, return 0.
#
# Note: A word is defined as a character sequence consists of non-space characters only.
#
# For example,
# Given s = "Hello World",
# return 5.
#



# V1 

class Solution:
    def lengthOfLastWord(self, s):
        string_set = s.strip().split(' ')
        if string_set == '':
            return 0
        else:
            return len(string_set[-1])


# V2 
class Solution:
    def lengthOfLastWord(self, s):
        string_set = s.strip().split(' ')
        return len(string_set[-1])

# V3  
class Solution:
    # @param s, a string
    # @return an integer
    def lengthOfLastWord(self, s):
        length = 0
        for i in reversed(s):
            if i == ' ':
                if length:
                    break
            else:
                length += 1
        return length


# V4
# Time:  O(n)
# Space: O(n)
class Solution2:
    # @param s, a string
    # @return an integer
    def lengthOfLastWord(self, s):
        return len(s.strip().split(" ")[-1])


