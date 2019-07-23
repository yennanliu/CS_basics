# V0 

# V1 
# http://bookshadow.com/weblog/2014/10/16/leetcode-reverse-words-string/
class Solution:
    # @param s, a string
    # @return a string
    def reverseWords(self, s):
        words = s.split()
        words.reverse()
        return " ".join(words)
        
# V2
# Time:  O(n)
# Space: O(n)
class Solution(object):
    # @param s, a string
    # @return a string
    def reverseWords(self, s):
        return ' '.join(reversed(s.split()))