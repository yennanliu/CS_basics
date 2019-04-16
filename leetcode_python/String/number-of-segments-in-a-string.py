# Time:  O(n)
# Space: O(1)

# Count the number of segments in a string,
# where a segment is defined to be a contiguous
# sequence of non-space characters.
#
# Please note that the string does not
# contain any non-printable characters.
#
# Example:
#
# Input: "Hello, my name is John"
# Output: 5


# V1  : dev, need to oouble check 
"""
class Solution:
    def countSegments(self, s):
        if len(s.replace(' ','')) <= 1 :
            return len(s.replace(' ',''))
        else:
            count=0
            for i in s.replace(',','').strip().split(' '):
                if i == '':
                    pass
                else:
                    count=count+1
            return count
"""


# V1' 
# Python booleans - if x:, vs if x == True, vs if x is True
# https://stackoverflow.com/questions/20420934/python-booleans-if-x-vs-if-x-true-vs-if-x-is-true
# V2 
class Solution(object):
    def countSegments(self, s):
        return len([i for i in s.strip().split(' ') if i])





# V2 
class Solution(object):
    def countSegments(self, s):
        """
        :type s: str
        :rtype: int
        """
        result = int(len(s) and s[-1] != ' ')
        for i in range(1, len(s)):
            if s[i] == ' ' and s[i-1] != ' ':
                result += 1
        return result

    def countSegments2(self, s):
        """
        :type s: str
        :rtype: int
        """
        return len([i for i in s.strip().split(' ') if i])




