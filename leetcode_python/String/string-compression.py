# Time:  O(n)
# Space: O(1)

# Given an array of characters, compress it in-place.
# The length after compression must always be smaller than or equal to the original array.
# Every element of the array should be a character (not int) of length 1.
# After you are done modifying the input array in-place, return the new length of the array.
#
# Follow up:
# Could you solve it using only O(1) extra space?
#
# Example 1:
# Input:
# ["a","a","b","b","c","c","c"]
#
# Output:
# Return 6, and the first 6 characters of the input array should be: ["a","2","b","2","c","3"]
#
# Explanation:
# "aa" is replaced by "a2". "bb" is replaced by "b2". "ccc" is replaced by "c3".
# Example 2:
# Input:
# ["a"]
#
# Output:
# Return 1, and the first 1 characters of the input array should be: ["a"]
#
# Explanation:
# Nothing is replaced.
# Example 3:
# Input:
# ["a","b","b","b","b","b","b","b","b","b","b","b","b"]
#
# Output:
# Return 4, and the first 4 characters of the input array should be: ["a","b","1","2"].
#
# Explanation:
# Since the character "a" does not repeat, it is not compressed. "bbbbbbbbbbbb" is replaced by "b12".
# Notice each digit has it's own entry in the array.
# Note:
# All characters have an ASCII value in [35, 126].
# 1 <= len(chars) <= 1000.


# V1  : dev 
"""
class Solution:
    def compress(self, chars):
        output=[]
        count=1 
        for i,j in enumerate(chars):
            if chars[i] != chars[i+1]:
                output.append(chars[i])
                count=1
            elif (i==0 and chars[i] == chars[i+1]):
                output.append(chars[i])
                count=1 
            elif (chars[i] == chars[i+1] and chars[i] != chars[i-1]):
                output.append(chars[i])
                count=1
            else:
                count = count+1 
                pass
            
 """
    
 
# V1' : dev 
"""
class Solution(object):
    def compress(self, chars):
        output=[]
        temp=[]
        count=0
        for i in range(0,len(chars)-1):
            print (chars[i])
            if chars[i] == chars[i+1]:
                count = count + 1 
                temp.append(chars[i])
                temp.append(count) 
            else:
                output.append(temp)
                temp=[]
                pass 
        return output
"""

# https://leetcode.com/articles/string-compression/
class Solution(object):
    def compress(self, chars):
        anchor = write = 0
        for read, c in enumerate(chars):
            if read + 1 == len(chars) or chars[read + 1] != c:
                chars[write] = chars[anchor]
                write += 1
                if read > anchor:
                    for digit in str(read - anchor + 1):
                        chars[write] = digit
                        write += 1
                anchor = read + 1
        return write



#--------------
        
# V2 
class Solution(object):
    def compress(self, chars):
        """
        :type chars: List[str]
        :rtype: int
        """
        anchor, write = 0, 0
        for read, c in enumerate(chars):
            if read+1 == len(chars) or chars[read+1] != c:
                chars[write] = chars[anchor]
                write += 1
                if read > anchor:
                    n, left = read-anchor+1, write
                    while n > 0:
                        chars[write] = chr(n%10+ord('0'))
                        write += 1
                        n /= 10
                    right = write-1
                    while left < right:
                        chars[left], chars[right] = chars[right], chars[left]
                        left += 1
                        right -= 1
                anchor = read+1
        return write


