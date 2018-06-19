

"""
Given a list of words and two words word1 and word2, return the shortest distance between these two words in the list.

For example, Assume that words = ["practice", "makes", "perfect", "coding", "makes"].

Given word1 = “coding”, word2 = “practice”, return 3. Given word1 = "makes", word2 = "coding", return 1.

Note: You may assume that word1 does not equal to word2, and word1 and word2 are both in the list.

"""




# V1 

class Solution:
	def shortestDistance(self, words, word1, word2):
		word_index_1 = [i for i,j in enumerate(words) if j == word1] 
		word_index_2 = [i for i,j in enumerate(words) if j == word2]
		shortest_dis = min([ abs(a - b ) for a in word_index_1 for  b in  word_index_2] )
		return shortest_dis



# V2 
class Solution:
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


