"""

443. String Compression
Medium

Given an array of characters chars, compress it using the following algorithm:

Begin with an empty string s. For each group of consecutive repeating characters in chars:

If the group's length is 1, append the character to s.
Otherwise, append the character followed by the group's length.

The compressed string s should not be returned separately, but instead, be stored in the input character array chars. Note that group lengths that are 10 or longer will be split into multiple characters in chars.

After you are done modifying the input array, return the new length of the array.

You must write an algorithm that uses only constant extra space.

Note: The characters in the array beyond the returned length do not matter and should be ignored.

Example 1:

Input: chars = ["a","a","b","b","c","c","c"]
Output: 6
Explanation: The groups are "aa", "bb", and "ccc". This compresses to "a2b2c3".
After modifying the input array in-place, the first 6 characters of chars should be ["a","2","b","2","c","3"].

Example 2:

Input: chars = ["a"]
Output: 1
Explanation: The only group is "a", which remains uncompressed since it is a single character.
After modifying the input array in-place, the first character of chars should be ["a"].

Example 3:

Input: chars = ["a","b","b","b","b","b","b","b","b","b","b","b","b"]
Output: 4
Explanation: The groups are "a" and "bbbbbbbbbbbb". This compresses to "ab12".
After modifying the input array in-place, the first 4 characters of chars should be ["a","b","1","2"].

Constraints:

1 <= chars.length <= 2000
chars[i] is a lowercase English letter, uppercase English letter, digit, or symbol.

"""

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

# V0
# time = O(n)
# space = O(n)
import collections
class Solution(object):
    def compress(self, chars):
        count = collections.Counter(chars)
        key_count = len(count.keys())
        val_count = len([ x for x in list(count.values()) if x > 1 ])
        return key_count + val_count

# V1
# http://bookshadow.com/weblog/2017/10/29/leetcode-string-compression/
# IDEA : QUEUE
# time = O(n)
# space = O(1)
class Solution(object):
    def compress(self, chars):
        """
        :type chars: List[str]
        :rtype: int
        """
        last, n, y = chars[0], 1, 0
        for x in range(1, len(chars)):
            c = chars[x]
            if c == last: n += 1
            else:
                for ch in last + str(n > 1 and n or ''):
                    chars[y] = ch
                    y += 1
                last, n = c, 1
        for ch in last + str(n > 1 and n or ''):
            chars[y] = ch
            y += 1
        while len(chars) > y: chars.pop()
        return y

# V1'
# https://blog.csdn.net/androidchanhao/article/details/81408345
# IDEA : 2 POINTERS
# time = O(n)
# space = O(1)
class Solution(object):
    def compress(self, chars):
        """
        :type chars: List[str]
        :rtype: int
        """
        n = len(chars)
        cur = 0 # index of current element, for compress the list 
        i = 0
        while i < n:
            j = i
            while j < n - 1 and chars[j] == chars[j+1]:# find the count of sequential exist of an elment 
                j += 1
            chars[cur] = chars[i] # record current element 
            cur += 1
            if i != j:
                times = str(j-i+1) # write the count into original list 
                tLen = len(times)
                for k in range(tLen):
                    chars[cur+k] = times[k]
                cur += tLen
            i = j + 1 # move to next element 
        return cur

# V2
# time = O(n)
# space = O(1)
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