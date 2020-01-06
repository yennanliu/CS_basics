# Time:  O(n + w^2), n = w * l,
#                    n is the length of S,
#                    w is the number of word,
#                    l is the average length of word
# Space: O(n)
# A sentence S is given, composed of words separated by spaces.
# Each word consists of lowercase and uppercase letters only.
#
# We would like to convert the sentence to "Goat Latin"
# (a made-up language similar to Pig Latin.)
#
# The rules of Goat Latin are as follows:
#
# If a word begins with a vowel (a, e, i, o, or u), append "ma" to the end of
# the word.
# For example, the word 'apple' becomes 'applema'.
#
# If a word begins with a consonant (i.e. not a vowel),
# remove the first letter and append it to the end, then add "ma".
# For example, the word "goat" becomes "oatgma".
#
# Add one letter 'a' to the end of each word per its word index in the
# sentence,
# starting with 1.
# For example, the first word gets "a" added to the end,
# the second word gets "aa" added to the end and so on.
# Return the final sentence representing the conversion from S to Goat Latin.
#
# Example 1:
#
# Input: "I speak Goat Latin"
# Output: "Imaa peaksmaaa oatGmaaaa atinLmaaaaa"
# Example 2:
#
# Input: "The quick brown fox jumped over the lazy dog"
# Output: "heTmaa uickqmaaa rownbmaaaa oxfmaaaaa umpedjmaaaaaa
#          overmaaaaaaa hetmaaaaaaaa azylmaaaaaaaaa ogdmaaaaaaaaaa"
#
# Notes:
# - S contains only uppercase, lowercase and spaces. Exactly one space between
#   each word.
# - 1 <= S.length <= 100.

# V0 
class Solution:
    def toGoatLatin(self, S):
        """
        :type S: str
        :rtype: str
        """
        vowels = ['a', 'e', 'i', 'o', 'u', 'A', 'E', 'I', 'O', 'U']
        words = S.split(' ')
        new_words = []
        for i, word in enumerate(words):
            if word[0] in vowels:
                word += 'ma'
            else:
                word = word[1:] + word[0] + 'ma'
            word += 'a' * (i + 1)
            new_words.append(word)
        return ' '.join(new_words)

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/80471925
class Solution:
    def toGoatLatin(self, S):
        """
        :type S: str
        :rtype: str
        """
        vowels = ['a', 'e', 'i', 'o', 'u', 'A', 'E', 'I', 'O', 'U']
        words = S.split(' ')
        new_words = []
        for i, word in enumerate(words):
            if word[0] in vowels:
                word += 'ma'
            else:
                word = word[1:] + word[0] + 'ma'
            word += 'a' * (i + 1)
            new_words.append(word)
        return ' '.join(new_words)
        
# V1' 
# http://bookshadow.com/weblog/2018/04/29/leetcode-goat-latin/
class Solution(object):
    def toGoatLatin(self, S):
        """
        :type S: str
        :rtype: str
        """
        ans = []
        for idx, word in enumerate(S.split()):
            latin = word
            if word[0].lower() not in 'aeiou':
                latin = word[1:] + word[0]
            latin += 'ma' + 'a' * (idx + 1)
            ans.append(latin)
        return ' '.join(ans)

# V1''
# https://www.jiuzhang.com/solution/1394-goat-latin/#tag-other-lang-python
class Solution:
    """
    @param S: 
    @return: nothing
    """
    def  toGoatLatin(self, S):
        if not S:
            return ''
        
        start, end = 0, 0
        word_count = 0
        answer = ''   
        vowels = ['a','e','i','o','u', 'A', 'E', 'I', 'O', 'U']   
        while start < len(S):
            while end < len(S) and S[end] != ' ':
                end += 1            
            word_count += 1
            word = S[start:end]           
            if word[0] in vowels:
                word += 'ma'
            else:
                word = word[1:] + word[0] + 'ma'             
            postfix = ''
            for i in range(word_count):
                postfix += 'a'
            answer+= word + postfix + ' '         
            start, end = end + 1, end + 2      
        return answer[:-1]

# V2 
class Solution(object):
    def toGoatLatin(self, S):
        """
        :type S: str
        :rtype: str
        """
        def convert(S):
            vowel = set('aeiouAEIOU')
            for i, word in enumerate(S.split(), 1):
                if word[0] not in vowel:
                    word = word[1:] + word[:1]
                yield word + 'ma' + 'a'*i
        return " ".join(convert(S))
