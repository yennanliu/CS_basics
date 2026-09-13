"""

434. Number of Segments in a String
Easy

Given a string s, return the number of segments in the string.

A segment is defined to be a contiguous sequence of non-space characters.

Example 1:

Input: s = "Hello, my name is John"
Output: 5
Explanation: The five segments are ["Hello,", "my", "name", "is", "John"]

Example 2:

Input: s = "Hello"
Output: 1

Constraints:

0 <= s.length <= 300
s consists of lowercase and uppercase English letters, digits, or one of the following characters "!@#$%^&*()_+-=',.:".
The only space character in s is ' '.

"""

# time = O(n)
# space = O(1)

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

# V0 

# V1 
# http://bookshadow.com/weblog/2016/12/04/leetcode-number-of-words-in-a-string/
# e.g. 
# In [6]: x="there are 2 pigs "
# In [7]: x.split()
# Out[7]: ['there', 'are', '2', 'pigs']
# In [8]: x.split(" ")
# Out[8]: ['there', 'are', '2', 'pigs', '']
# time = O(n)
# space = O(n)
class Solution(object):
    def countSegments(self, s):
        """
        :type s: str
        :rtype: int
        """
        return len(s.split())

# V2
# time = O(n)
# space = O(n)
class Solution(object):
    def countSegments(self, s):
        return len([i for i in s.strip().split(' ') if i])

# V3
# time = O(n)
# space = O(1)
class Solution(object):
    def countSegments(self, s):
        """
        :type s: str
        :rtype: int
        """
        result = int(len(s) and s[-1] != ' ')
        for i in xrange(1, len(s)):
            if s[i] == ' ' and s[i-1] != ' ':
                result += 1
        return result

    # time = O(n)
    # space = O(n)
    def countSegments2(self, s):
        """
        :type s: str
        :rtype: int
        """
        return len([i for i in s.strip().split(' ') if i])
