"""

186. Reverse Words in a String II
Medium

Given a character array s, reverse the order of the words.

A word is defined as a sequence of non-space characters. The words in s will be separated by a single space.

Your code must solve the problem in-place, i.e. without allocating extra space.

 

Example 1:

Input: s = ["t","h","e"," ","s","k","y"," ","i","s"," ","b","l","u","e"]
Output: ["b","l","u","e"," ","i","s"," ","s","k","y"," ","t","h","e"]
Example 2:

Input: s = ["a"]
Output: ["a"]
 

Constraints:

1 <= s.length <= 105
s[i] is an English letter (uppercase or lowercase), digit, or space ' '.
There is at least one word in s.
s does not contain leading or trailing spaces.
All the words in s are guaranteed to be separated by a single space.

"""

# V0 
# IDEA : REVERSE WHOLE STRING -> REVERSE EACH WORD 
class Solution(object):
    def reverseWords(self, s):
        s_ =  s[::-1]
        s_list = s_.split(" ")
        return " ".join([ i[::-1] for  i in s_list])

# V0'
class Solution(object):
    def reverseWords(self, s):
        res = "".join(x).split(" ")[::-1]   
        return list(" ".join(res))

# V1
# IDEA : Reverse the Whole String and Then Reverse Each Word
# https://leetcode.com/problems/reverse-words-in-a-string-ii/solution/
class Solution:
    def reverse(self, l: list, left: int, right: int) -> None:
        while left < right:
            l[left], l[right] = l[right], l[left]
            left, right = left + 1, right - 1
            
    def reverse_each_word(self, l: list) -> None:
        n = len(l)
        start = end = 0
        
        while start < n:
            # go to the end of the word
            while end < n and l[end] != ' ':
                end += 1
            # reverse the word
            self.reverse(l, start, end - 1)
            # move to the next word
            start = end + 1
            end += 1
            
    def reverseWords(self, s: List[str]) -> None:
        """
        Do not return anything, modify s in-place instead.
        """
        # reverse the whole string
        self.reverse(s, 0, len(s) - 1)
        
        # reverse each word
        self.reverse_each_word(s)

# V1 
# http://www.voidcn.com/article/p-eggrnnob-zo.html
class Solution(object):
    def reverseWords(self, s):
        """
        :type s: a list of 1 length strings (List[str])
        :rtype: nothing
        """
        s.reverse()
        i = 0
        while i < len(s):
            j = i
            while j < len(s) and s[j] != " ":
                j += 1
            for k in range(i, i + (j - i) / 2 ):
                t = s[k]
                s[k] = s[i + j - 1 - k]
                s[i + j - 1 - k] = t
            i = j + 1
            
# V2 
# Time: O(n)
# Space:O(1)
class Solution(object):
    def reverseWords(self, s):
        """
        :type s: a list of 1 length strings (List[str])
        :rtype: nothing
        """
        def reverse(s, begin, end):
            for i in range((end - begin) / 2):
                s[begin + i], s[end - 1 - i] = s[end - 1 - i], s[begin + i]

        reverse(s, 0, len(s))
        i = 0
        for j in range(len(s) + 1):
            if j == len(s) or s[j] == ' ':
                reverse(s, i, j)
                i = j + 1