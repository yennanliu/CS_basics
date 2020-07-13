"""
An abbreviation of a word follows the form <first letter><number><last letter>. Below are some examples of word abbreviations:

a) it                      --> it    (no abbreviation)

     1
b) d|o|g                   --> d1g

              1    1  1
     1---5----0----5--8
c) i|nternationalizatio|n  --> i18n

              1
     1---5----0
d) l|ocalizatio|n          --> l10n
Assume you have a dictionary and given a word, find whether its abbreviation is unique in the dictionary. A word's abbreviation is unique if no other word from the dictionary has the same abbreviation.

Example: 

Given dictionary = [ "deer", "door", "cake", "card" ]

isUnique("dear") -> false
isUnique("cart") -> true
isUnique("cane") -> false
isUnique("make") -> true

http://leetcode.com/problems/unique-word-abbreviation/
"""

# V0 

# V1
# https://www.jiuzhang.com/solution/unique-word-abbreviation/#tag-highlight-lang-python
class ValidWordAbbr:
    
    def __init__(self, dictionary):
        # do intialization if necessary
        self.map = {}
        for word in dictionary:
            abbr = self.word_to_abbr(word)
            if abbr not in self.map:
                self.map[abbr] = set() 
            self.map[abbr].add(word)

    def word_to_abbr(self, word):
        if len(word) <= 1:
            return word
        return word[0] + str(len(word[1:-1])) + word[-1]
        
    def isUnique(self, word):
        # write your code here
        abbr = self.word_to_abbr(word)
        if abbr not in self.map:
            return True
        for word_in_dict in self.map[abbr]:
            if word != word_in_dict:
                return False
        return True

### Test case : dev

# V1 
# http://www.voidcn.com/article/p-cfjjycdw-qp.html
class ValidWordAbbr(object):
    def __init__(self, dictionary):
        """
        initialize your data structure here.
        :type dictionary: List[str]
        """
        self.dic = {} # 空字典
        for word in dictionary:
            abb = word if len(word) <= 2 else word[0] + str(len(word) - 2) + word[-1]
            self.dic[abb] = word if abb not in self.dic else "" if self.dic[abb] != word else self.dic[abb]

    def isUnique(self, word):
        """
        check if a word is unique.
        :type word: str
        :rtype: bool
        """
        abb = word if len(word) <= 2 else word[0] + str(len(word) - 2) + word[-1]
        return abb not in self.dic or self.dic[abb] == word

# V2
# Time:  ctor:   O(n), n is number of words in the dictionary.
#        lookup: O(1)
# Space: O(k), k is number of unique words.
import collections
class ValidWordAbbr(object):
    def __init__(self, dictionary):
        """
        initialize your data structure here.
        :type dictionary: List[str]
        """
        self.lookup_ = collections.defaultdict(set)
        for word in dictionary:
            abbr = self.abbreviation(word)
            self.lookup_[abbr].add(word)

    def isUnique(self, word):
        """
        check if a word is unique.
        :type word: str
        :rtype: bool
        """
        abbr = self.abbreviation(word)
        return self.lookup_[abbr] <= {word}

    def abbreviation(self, word):
        if len(word) <= 2:
            return word
        return word[0] + str(len(word)-2) + word[-1]