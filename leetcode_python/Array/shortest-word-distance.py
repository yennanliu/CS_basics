"""
Given a list of words and two words word1 and word2, return the shortest distance between these two words in the list.

For example, Assume that words = ["practice", "makes", "perfect", "coding", "makes"].

Given word1 = “coding”, word2 = “practice”, return 3. Given word1 = "makes", word2 = "coding", return 1.

Note: You may assume that word1 does not equal to word2, and word1 and word2 are both in the list.

"""

# V0 

# V1 
# http://www.voidcn.com/article/p-epmviyav-qp.html
# IDEA : LOOP 
# NOTE : have to consider the deplicated element cases 
# e.g. 
# Assume that words = ["practice", "makes", "perfect", "coding", "makes"].
# -> 
# Given word1 = “coding”, word2 = “practice”, return 3. <---- “coding” index = 3, “practice” index = 0, 3-0 = 3 
# Given word1 = "makes", word2 = "coding", return 1. <---- "makes" index = 1,4,  "coding" index = 3, so take the min : 4-3 = 1 
class Solution(object):
    def shortestDistance(self, words, word1, word2):
        """
        :type words: List[str]
        :type word1: str
        :type word2: str
        :rtype: int
        """
        w1 = [i for i in range(len(words)) if words[i] == word1]
        w2 = [i for i in range(len(words)) if words[i] == word2]
        return min([abs(i - j) for i in w1 for j in w2])

# V1' 
# http://www.voidcn.com/article/p-epmviyav-qp.html
def shortestDistance(self, words, word1, word2):
    size = len(words)
    index1, index2 = size, size
    ans = size

    for i in range(size):
        if words[i] == word1:
            index1 = i
            ans = min(ans, abs(index1-index2))
        elif words[i] == word2:
            index2 = i
            ans = min(ans, abs(index1-index2))
    return ans

# V2 
# Time:  O(n)
# Space: O(1)
class Solution(object):
    # @param {string[]} words
    # @param {string} word1
    # @param {string} word2
    # @return {integer}
    def shortestDistance(self, words, word1, word2):
        dist = float("inf")
        i, index1, index2 = 0, None, None
        while i < len(words):
            if words[i] == word1:
                index1 = i
            elif words[i] == word2:
                index2 = i
            if index1 is not None and index2 is not None:
                dist = min(dist, abs(index1 - index2))
            i += 1
        return dist
