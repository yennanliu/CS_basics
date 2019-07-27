# Time:  O(n), n is the sume of all word lengths
# Space: O(n)

# International Morse Code defines a standard encoding where each letter
# is mapped to a series of dots and dashes, as follows: "a" maps to ".-", "b" maps
# to "-...", "c" maps to "-.-.", and so on.
#
# For convenience, the full table for the 26 letters of the English alphabet is given below:
#
# [".-","-...","-.-.","-..",".","..-.","--.",
#  "....","..",".---","-.-",".-..","--","-.",
#  "---",".--.","--.-",".-.","...","-","..-",
#  "...-",".--","-..-","-.--","--.."]
# Now, given a list of words, each word can be written as a concatenation of
# the Morse code of each letter. For example, "cab" can be written as "-.-.-....-",
# (which is the concatenation "-.-." + "-..." + ".-").
# We'll call such a concatenation, the transformation of a word.
#
# Return the number of different transformations among all words we have.
#
# Example:
# Input: words = ["gin", "zen", "gig", "msg"]
# Output: 2
# Explanation:
# The transformation of each word is:
# "gin" -> "--...-."
# "zen" -> "--...-."
# "gig" -> "--...--."
# "msg" -> "--...--."
#
# There are 2 different transformations, "--...-." and "--...--.".
#
# Note:
# - The length of words will be at most 100.
# - Each words[i] will have length in range [1, 12].
# - words[i] will only consist of lowercase letters.

# V0 

# V1
# https://blog.csdn.net/fuxuemingzhu/article/details/79774003
# IDEA : MAP + SET 
# IDEA : ORD
# In [14]: ord('C') 
# Out[14]: 67
# In [15]: ord('C')  - ord('A')
# Out[15]: 2
# In [16]: ord('B')  - ord('A')
class Solution(object):
    def uniqueMorseRepresentations(self, words):
        """
        :type words: List[str]
        :rtype: int
        """
        moorse = [".-","-...","-.-.","-..",".","..-.","--.","....","..",".---","-.-",".-..","--","-.","---",".--.","--.-",".-.","...","-","..-","...-",".--","-..-","-.--","--.."]
        trans = lambda x: moorse[ord(x) - ord('a')]
        map_word = lambda word: ''.join([trans(x) for x in word])
        res = map(map_word, words)
        return len(set(res))

# V1' 
# https://blog.csdn.net/fuxuemingzhu/article/details/79774003
# IDEA : SET + DICT 
class Solution:
    def uniqueMorseRepresentations(self, words):
        """
        :type words: List[str]
        :rtype: int
        """
        morse = [".-","-...","-.-.","-..",".","..-.","--.","....","..",".---","-.-",".-..","--","-.","---",".--.","--.-",".-.","...","-","..-","...-",".--","-..-","-.--","--.."]
        english = ['a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z']
        edict = dict(zip(english, morse))
        res = set()
        for word in words:
            mword = ""
            for w in word:
                mword = mword + edict[w]
            res.add(mword)
        return len(res)
        
# V2 
class Solution(object):
    def uniqueMorseRepresentations(self, words):
        """
        :type words: List[str]
        :rtype: int
        """
        MORSE = [".-", "-...", "-.-.", "-..", ".", "..-.", "--.",
                 "....", "..", ".---", "-.-", ".-..", "--", "-.",
                 "---", ".--.", "--.-", ".-.", "...", "-", "..-",
                 "...-", ".--", "-..-", "-.--", "--.."]

        lookup = {"".join(MORSE[ord(c) - ord('a')] for c in word) \
                  for word in words}
        return len(lookup)
