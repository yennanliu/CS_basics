# Time:  O(n)
# Space: O(1)

# You are given a string representing an attendance record for a student.
# The record only contains the following three characters:
#
# 'A' : Absent.
# 'L' : Late.
# 'P' : Present.
# A student could be rewarded if his attendance record
# doesn't contain more than one 'A' (absent) or more than two continuous 'L' (late).
#
# You need to return whether the student could be rewarded according to his attendance record.
#
# Example 1:
# Input: "PPALLP"
# Output: True
# Example 2:
# Input: "PPALLL"
# Output: False


# V0 

# V0' 
#  if p -> q  then  ~q -> ~p 
#  => s with more then 1  "A" or more than 2 CONTINUOUS "LL" then false
#  => if else, true
class Solution:
    def checkRecord(self, s):
        if ( s.count('A') > 1  or s.find('LLL') >= 0):
            return False
        else:
            return True

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/70337973
# IDEA : REGULAR EXPRESSION 
class Solution:
    def checkRecord(self, s):
        """
        :type s: str
        :rtype: bool
        """
        return not re.match(".*A.*A.*", s) and not re.match(".*LLL.*", s)
        
# V2 
class Solution(object):
    def checkRecord(self, s):
        """
        :type s: str
        :rtype: bool
        """
        count_A = 0
        for i in range(len(s)):
            if s[i] == 'A':
                count_A += 1
                if count_A == 2:
                    return False
            if i < len(s) - 2 and s[i] == s[i+1] == s[i+2] == 'L':
                return False
        return True
