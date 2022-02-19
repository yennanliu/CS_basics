# Time:  O(n + p)
# Space: O(p)
# Given two sentences words1, words2 (each represented as an array of strings),
# and a list of similar word pairs pairs, determine if two sentences are similar.
#
# For example, "great acting skills" and "fine drama talent" are similar,
# if the similar word pairs are pairs = [["great", "fine"], ["acting","drama"], ["skills","talent"]].
#
# Note that the similarity relation is not transitive.
# For example, if "great" and "fine" are similar, and "fine" and "good" are similar,
# "great" and "good" are not necessarily similar.
#
# However, similarity is symmetric.
# For example, "great" and "fine" being similar is the same as "fine" and "great" being similar.
#
# Also, a word is always similar with itself.
# For example, the sentences words1 = ["great"], words2 = ["great"], pairs = [] are similar,
# even though there are no specified similar word pairs.
#
# Finally, sentences can only be similar if they have the same number of words.
# So a sentence like words1 = ["great"] can never be similar to words2 = ["doubleplus","good"].
#
# Note:
# - The length of words1 and words2 will not exceed 1000.
# - The length of pairs will not exceed 2000.
# - The length of each pairs[i] will be 2.
# - The length of each words[i] and pairs[i][j] will be in the range [1, 20].

# V0
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

# V0'
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
