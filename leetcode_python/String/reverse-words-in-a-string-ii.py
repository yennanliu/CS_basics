# V0 
# IDEA : REVERSE WHOLE STRING -> REVERSE EACH WORD 
class Solution(object):
    def reverseWords(self, s):
        s_ =  s[::-1]
        s_list = s_.split(" ")
        return " ".join([ i[::-1] for  i in s_list])

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
