# Time:  O(n)
# Space: O(1)
# Given a string, you need to reverse the order of characters in each word within a sentence
# while still preserving whitespace and initial word order.
#
# Example 1:
# Input: "Let's take LeetCode contest"
# Output: "s'teL ekat edoCteeL tsetnoc"
# Note: In the string, each word is separated by single space and
# there will not be any extra space in the string.

# V0 

# V1 
# http://bookshadow.com/weblog/2017/04/09/leetcode-reverse-words-in-a-string-iii/
# IDEA : SPLIT + REVERSE 
class Solution(object):
    def reverseWords(self, s):
        """
        :type s: str
        :rtype: str
        """
        return ' '.join(w[::-1] for w in s.split())
# V1' 
# http://bookshadow.com/weblog/2017/04/09/leetcode-reverse-words-in-a-string-iii/
class Solution(object):
    def reverseWords(self, s):
        """
        :type s: str
        :rtype: str
        """
        clist, size = list(s), len(s)
        p = q = 0
        while p < size:
            while q < size and s[q] != ' ': q += 1
            for x in range((q - p) / 2):
                clist[p + x], clist[q - x - 1] = clist[q - x - 1], clist[p + x]
            p = q = q + 1
        return ''.join(clist)

# V2  
class Solution:
    def reverseWords(self, s):
        output= []
        for item in s.split(' '):
            item_ = str(item)[::-1]
            #print (item_)
            output.append(item_)
        return ''.join( str(x) + ' ' for x in output ).strip()

# V3 
class Solution(object):
    def reverseWords(self, s):
        """
        :type s: str
        :rtype: str
        """
        def reverse(s, begin, end):
            for i in range((end - begin) // 2):
                s[begin + i], s[end - 1 - i] = s[end - 1 - i], s[begin + i]

        s, i = list(s), 0
        for j in range(len(s) + 1):
            if j == len(s) or s[j] == ' ':
                reverse(s, i, j)
                i = j + 1
        return "".join(s)

class Solution2(object):
    def reverseWords(self, s):
        reversed_words = [word[::-1] for word in s.split(' ')]
        return ' '.join(reversed_words)

