"""

734. Sentence Similarity
Easy

We can represent a sentence as an array of words, for example, the sentence "I am happy with leetcode" can be represented as arr = ["I","am",happy","with","leetcode"].

Given two sentences sentence1 and sentence2 each represented as a string array and given an array of string pairs similarPairs where similarPairs[i] = [xi, yi] indicates that the two words xi and yi are similar.

Return true if sentence1 and sentence2 are similar, or false if they are not similar.

Two sentences are similar if:

They have the same length (i.e., the same number of words)
sentence1[i] and sentence2[i] are similar.
Notice that a word is always similar to itself, also notice that the similarity relation is not transitive. For example, if the words a and b are similar, and the words b and c are similar, a and c are not necessarily similar.

 

Example 1:

Input: sentence1 = ["great","acting","skills"], sentence2 = ["fine","drama","talent"], similarPairs = [["great","fine"],["drama","acting"],["skills","talent"]]
Output: true
Explanation: The two sentences have the same length and each word i of sentence1 is also similar to the corresponding word in sentence2.
Example 2:

Input: sentence1 = ["great"], sentence2 = ["great"], similarPairs = []
Output: true
Explanation: A word is similar to itself.
Example 3:

Input: sentence1 = ["great"], sentence2 = ["doubleplus","good"], similarPairs = [["great","doubleplus"]]
Output: false
Explanation: As they don't have the same length, we return false.
 

Constraints:

1 <= sentence1.length, sentence2.length <= 1000
1 <= sentence1[i].length, sentence2[i].length <= 20
sentence1[i] and sentence2[i] consist of English letters.
0 <= similarPairs.length <= 1000
similarPairs[i].length == 2
1 <= xi.length, yi.length <= 20
xi and yi consist of lower-case and upper-case English letters.
All the pairs (xi, yi) are distinct.

"""

# V0
# IDEA : array op
#   -> Apart from edge cases
#   -> there are cases we need to consider
#     -> 1) if sentence1[i] == sentence2[i]
#     -> 2) if sentence1[i] != sentence2[i] and
#           -> [sentence1[i], sentence2[i]] in similarPairs
#           -> [sentence2[i], sentence1[i]] in similarPairs
# Examples:
#
# sentence1=["great","acting","skills"]
# sentence2=["fine","drama","talent"]
# similarPairs=[["great","fine"],["drama","acting"],["skills","talent"]]
#
# sentence1 = ["a","very","delicious","meal"]
# sentence2 = ["one","really","delicious","dinner"]
# similarPairs = [["great","good"],["extraordinary","good"],["well","good"],["wonderful","good"],["excellent","good"],["fine","good"],["nice","good"],["any","one"],["some","one"],["unique","one"],["the","one"],["an","one"],["single","one"],["a","one"],["truck","car"],["wagon","car"],["automobile","car"],["auto","car"],["vehicle","car"],["entertain","have"],["drink","have"],["eat","have"],["take","have"],["fruits","meal"],["brunch","meal"],["breakfast","meal"],["food","meal"],["dinner","meal"],["super","meal"],["lunch","meal"],["possess","own"],["keep","own"],["have","own"],["extremely","very"],["actually","very"],["really","very"],["super","very"]]
#
# sentence1=["great","acting","skills"]
# sentence2=["fine","drama","talent"]
# similarPairs=[["great","fine"],["drama","acting"],["skills","talent"]]
#
# sentence1=["an","extraordinary","meal"]
# sentence2=["one","good","dinner"]
# similarPairs=[["great","good"],["extraordinary","good"],["well","good"],["wonderful","good"],["excellent","good"],["fine","good"],["nice","good"],["any","one"],["some","one"],["unique","one"],["the","one"],["an","one"],["single","one"],["a","one"],["truck","car"],["wagon","car"],["automobile","car"],["auto","car"],["vehicle","car"],["entertain","have"],["drink","have"],["eat","have"],["take","have"],["fruits","meal"],["brunch","meal"],["breakfast","meal"],["food","meal"],["dinner","meal"],["super","meal"],["lunch","meal"],["possess","own"],["keep","own"],["have","own"],["extremely","very"],["actually","very"],["really","very"],["super","very"]]
class Solution(object):
    def areSentencesSimilar(self, sentence1, sentence2, similarPairs):
        # edge case
        if sentence1 == sentence2:
            return True
        if len(sentence1) != len(sentence2):
            return False
        for i in range(len(sentence1)):
            tmp = [sentence1[i], sentence2[i]]
            """
            NOTE : below condition
                1) sentence1[i] != sentence2[i]
                  AND
                2) (tmp not in similarPairs and tmp[::-1] not in similarPairs)

                -> return false
            """
            if sentence1[i] != sentence2[i] and (tmp not in similarPairs and tmp[::-1] not in similarPairs):
                return False
        return True

# V0'
# https://zxi.mytechroad.com/blog/hashtable/leetcode-734-sentence-similarity/
import collections
class Solution(object):
    def areSentencesSimilar(self, words1, words2, pairs):
        if len(words1) != len(words2): return False
        similars = collections.defaultdict(set)
        for w1, w2 in pairs:
            similars[w1].add(w2)
            similars[w2].add(w1)
        for w1, w2 in zip(words1, words2):
            if w1 != w2 and w2 not in similars[w1]:
                return False
        return True

# V0''
import collections
class Solution(object):
    def areSentencesSimilar(self, words1, words2, pairs):
        if len(words1) != len(words2):
            return False
        map_ = collections.defaultdict(set)
        # build the grpah
        for i, j in pairs:
            map_[i].add(j)
            map_[j].add(i)
        for w1, w2 in zip(words1, words2):
            if w1 != w2 and w1 not in map_[w2]:
                return False
        return True

# V1 
# http://bookshadow.com/weblog/2017/11/26/leetcode-sentence-similarity/
# https://blog.csdn.net/danspace1/article/details/88942077
# IDEA : ollections.defaultdict(set) --> set IS "ADDABLE"
# IDEA : 
# IF len(words1) != len(words2) -> return False
# HAVE A MAPPING RELATIONSHIP (similars) ON PAIRS :
# e.g. 
# if pairs = [["great", "fine"], ["acting","drama"], ["skills","talent"]]
# -> 
# similars = collections.defaultdict(set)
# for w1, w2 in pairs:
#     similars[w1].add(w2)
#     similars[w2].add(w1)
# -> similars = 
# defaultdict(set,
#             {'acting': {'drama'},
#              'drama': {'acting'},
#              'fine': {'great'},
#              'great': {'fine'},
#              'skills': {'talent'},
#              'talent': {'skills'}})
# THEN COMPARE THROUGH words1, words2 WITH similars PATTERN
# RETURN FALSE IF ANY ELEMENT IN words1 !=  words2 AND ELEMENT NOT IN similars RELATIONSHIP
import collections
class Solution(object):
    def areSentencesSimilar(self, words1, words2, pairs):
        """
        :type words1: List[str]
        :type words2: List[str]
        :type pairs: List[List[str]]
        :rtype: bool
        """
        if len(words1) != len(words2): return False
        similars = collections.defaultdict(set)
        for w1, w2 in pairs:
            similars[w1].add(w2)
            similars[w2].add(w1)
        for w1, w2 in zip(words1, words2):
            if w1 != w2 and w2 not in similars[w1]:
                return False
        return True

# V1'
# https://zxi.mytechroad.com/blog/hashtable/leetcode-734-sentence-similarity/
class Solution:
    def areSentencesSimilar(self, words1, words2, pairs):
        if len(words1) != len(words2): return False
        
        similar_words = {}
        
        for w1, w2 in pairs:
            if not w1 in similar_words: similar_words[w1] = set()
            if not w2 in similar_words: similar_words[w2] = set()
            similar_words[w1].add(w2)
            similar_words[w2].add(w1)
        
        for w1, w2 in zip(words1, words2):
            if w1 == w2: continue
            if w1 not in similar_words: return False
            if w2 not in similar_words[w1]: return False      
        return True

# V1''
# https://www.jiuzhang.com/solution/sentence-similarity/#tag-highlight-lang-python
class Solution:
    """
    @param words1: a list of string
    @param words2: a list of string
    @param pairs: a list of string pairs
    @return: return a boolean, denote whether two sentences are similar or not
    """
    def isSentenceSimilarity(self, words1, words2, pairs):
        # write your code here
        if(not (len(words1) == len(words2))):
            return False
        mp = {}
        for i in range(len(pairs)):
            S = set()
            if(not (mp.get(pairs[i][0]) == None)):
                S = mp[pairs[i][0]]
            S.add(pairs[i][1])
            mp[pairs[i][0]] = S
        for i in range(len(words1)):
            if(words1[i] == words2[i]):
                continue
            if((mp.get(words1[i]) == None or words2[i] not in mp[words1[i]]) and (mp.get(words2[i]) == None or words1[i] not in mp[words2[i]])):
                return False
        return True

# V2 
import collections
class Solution(object):
    def areSentencesSimilar(self, words1, words2, pairs):
        """
        :type words1: List[str]
        :type words2: List[str]
        :type pairs: List[List[str]]
        :rtype: bool
        """
        if len(words1) != len(words2): return False
        similars = collections.defaultdict(set)
        for w1, w2 in pairs:
            similars[w1].add(w2)
            similars[w2].add(w1)
        for w1, w2 in zip(words1, words2):
            if w1 != w2 and w2 not in similars[w1]:
                return False
        return True

# V3 
import itertools
class Solution(object):
    def areSentencesSimilar(self, words1, words2, pairs):
        """
        :type words1: List[str]
        :type words2: List[str]
        :type pairs: List[List[str]]
        :rtype: bool
        """
        if len(words1) != len(words2): return False
        lookup = set(map(tuple, pairs))
        return all(w1 == w2 or (w1, w2) in lookup or (w2, w1) in lookup \
                   for w1, w2 in zip(words1, words2))