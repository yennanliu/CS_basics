# Time:  O(l)
# Space: O(1)

# We define the usage of capitals in a word to be right when one of the following cases holds:
#
# All letters in this word are capitals, like "USA".
# All letters in this word are not capitals, like "leetcode".
# Only the first letter in this word is capital if it has more than one letter, like "Google".
# Otherwise, we define that this word doesn't use capitals in a right way.
# Example 1:
# Input: "USA"
# Output: True
# Example 2:
# Input: "FlaG"
# Output: False
# Note: The input will be a non-empty word consisting of uppercase and lowercase latin letters.


# V1 

class Solution(object):
    def detectCapitalUse(self, word):
        capital_len = len([x for x in word if x.isupper()])
        lower_len = len([x for x in word if x.islower()])
        if ( capital_len == len(word) or  lower_len == len(word)):
            return True
        elif ( word[0].isupper() and  len([x for x in word[1:] if x.islower()]) == len(word) -1 ):
            return True
        else:
            return False
        
        

# V2 
class Solution(object):
    def detectCapitalUse(self, word):
        """
        :type word: str
        :rtype: bool
        """
        # https://www.tutorialspoint.com/python/string_istitle.htm
        # The method istitle() checks whether all the case-based characters 
        # in the string following non-casebased letters are uppercase and all other case-based characters are lowercase.
        return word.isupper() or word.islower() or word.istitle()



