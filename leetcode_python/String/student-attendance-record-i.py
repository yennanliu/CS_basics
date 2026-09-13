"""

551. Student Attendance Record I
Easy

You are given a string s representing an attendance record for a student where each character signifies whether the student was absent, late, or present on that day. The record only contains the following three characters:

'A': Absent.
'L': Late.
'P': Present.

The student is eligible for an attendance award if they meet both of the following criteria:

The student was absent ('A') for strictly fewer than 2 days total.
The student was never late ('L') for 3 or more consecutive days.

Return true if the student is eligible for an attendance award, or false otherwise.

Example 1:

Input: s = "PPALLP"
Output: true
Explanation: The student has fewer than 2 absences and was never late 3 or more consecutive days.

Example 2:

Input: s = "PPALLL"
Output: false
Explanation: The student was late 3 consecutive days in the last 3 days, so is not eligible for the award.

Constraints:

1 <= s.length <= 1000
s[i] is either 'A', 'L', or 'P'.

"""

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

# V0-1
#  if p -> q  then  ~q -> ~p
#  => s with more then 1  "A" or more than 2 CONTINUOUS "LL" then false
#  => if else, true
# time = O(n)
# space = O(1)
class Solution:
    def checkRecord(self, s):
        if ( s.count('A') > 1  or s.find('LLL') >= 0):
            return False
        else:
            return True

# V1
# https://blog.csdn.net/fuxuemingzhu/article/details/70337973
# IDEA : REGULAR EXPRESSION
# time = O(n)
# space = O(1)
class Solution:
    def checkRecord(self, s):
        """
        :type s: str
        :rtype: bool
        """
        return not re.match(".*A.*A.*", s) and not re.match(".*LLL.*", s)
        
# V2
# time = O(n)
# space = O(1)
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
