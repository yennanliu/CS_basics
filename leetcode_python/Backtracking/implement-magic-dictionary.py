"""

676. Implement Magic Dictionary
Medium

Design a data structure that is initialized with a list of different words. Provided a string, you should determine if you can change exactly one character in this string to match any word in the data structure.

Implement the MagicDictionary class:

MagicDictionary() Initializes the object.
void buildDict(String[] dictionary) Sets the data structure with an array of distinct strings dictionary.
bool search(String searchWord) Returns true if you can change exactly one character in searchWord to match any string in the data structure, otherwise returns false.

Example 1:

Input
["MagicDictionary", "buildDict", "search", "search", "search", "search"]
[[], [["hello", "leetcode"]], ["hello"], ["hhllo"], ["hell"], ["leetcoded"]]
Output
[null, null, false, true, false, false]

Explanation
MagicDictionary magicDictionary = new MagicDictionary();
magicDictionary.buildDict(["hello", "leetcode"]);
magicDictionary.search("hello"); // return False
magicDictionary.search("hhllo"); // We can change the second 'h' to 'e' to match "hello" so we return True
magicDictionary.search("hell"); // return False
magicDictionary.search("leetcoded"); // return False

Constraints:

1 <= dictionary.length <= 100
1 <= dictionary[i].length <= 100
dictionary[i] consists of only lower-case English letters.
All the strings in dictionary are distinct.
1 <= searchWord.length <= 100
searchWord consists of only lower-case English letters.
buildDict will be called only once before search.
At most 100 calls will be made to search.

"""

# V0 

# V1 
# http://bookshadow.com/weblog/2017/09/10/leetcode-implement-magic-dictionary/
import collections 
class MagicDictionary(object):

    def __init__(self):
        """
        Initialize your data structure here.
        """
        self.dmap = collections.defaultdict(set)

    # time = O(k * l^2), k = num words, l = word length
    # space = O(k * l^2)
    def buildDict(self, dict):
        """
        Build a dictionary through a list of words
        :type dict: List[str]
        :rtype: void
        """
        for word in dict:
            for x in range(len(word)):
                key = word[:x] + '_' + word[x+1:]
                self.dmap[key].add(word[x])

    # time = O(l^2), l = word length
    # space = O(1)
    def search(self, word):
        """
        Returns if there is any word in the trie that equals to the given word after modifying exactly one character
        :type word: str
        :rtype: bool
        """
        for x in range(len(word)):
            key = word[:x] + '_' + word[x+1:]
            values = self.dmap[key] 
            if not values: continue
            if word[x] not in values or len(values) > 1:
                return True
        return False
# Your MagicDictionary object will be instantiated and called as such:
# obj = MagicDictionary()
# obj.buildDict(dict)
# param_2 = obj.search(word)

# V2
# time = O(n), n is the length of the word
# space = O(d)
import collections
class MagicDictionary(object):

    def __init__(self):
        """
        Initialize your data structure here.
        """
        _trie = lambda: collections.defaultdict(_trie)
        self.trie = _trie()


    def buildDict(self, dictionary):
        """
        Build a dictionary through a list of words
        :type dictionary: List[str]
        :rtype: void
        """
        for word in dictionary:
            reduce(dict.__getitem__, word, self.trie).setdefault("_end")


    def search(self, word):
        """
        Returns if there is any word in the trie that equals to the given word after modifying exactly one character
        :type word: str
        :rtype: bool
        """
        def find(word, curr, i, mistakeAllowed):
            if i == len(word):
                return "_end" in curr and not mistakeAllowed

            if word[i] not in curr:
                return any(find(word, curr[c], i+1, False) for c in curr if c != "_end") \
                           if mistakeAllowed else False

            if mistakeAllowed:
                return find(word, curr[word[i]], i+1, True) or \
                       any(find(word, curr[c], i+1, False) \
                           for c in curr if c not in ("_end", word[i]))
            return find(word, curr[word[i]], i+1, False)

        return find(word, self.trie, 0, True)
