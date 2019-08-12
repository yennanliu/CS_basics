# V0 

# V1 
# http://www.voidcn.com/article/p-pqyeirhe-qp.html
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
        return min([abs(i - j) for i in w1 for j in w2 if i != j]) # remove the "0" case (index difference = 0)
# V1'
# # http://www.voidcn.com/article/p-pqyeirhe-qp.html
class Solution(object):
    def shortestWordDistance(self, words, word1, word2):

        dic = {}
        for index, value in enumerate(words): # save the value in dict 
            if value not in dic:
                dic[value] = [index]
            else:
                dic[value] += index,

        return min(set(abs(x-y) for x in dic[word1] for y in dic[word2]) - set([0]))  # remove the "0" case via set (index difference = 0)

# V2 
# Time:  O(n)
# Space: O(1)
class Solution(object):
    # @param {string[]} words
    # @param {string} word1
    # @param {string} word2
    # @return {integer}
    def shortestWordDistance(self, words, word1, word2):
        dist = float("inf")
        is_same = (word1 == word2)
        i, index1, index2 = 0, None, None
        while i < len(words):
            if words[i] == word1:
                if is_same and index1 is not None:
                    dist = min(dist, abs(index1 - i))
                index1 = i
            elif words[i] == word2:
                index2 = i

            if index1 is not None and index2 is not None:
                dist = min(dist, abs(index1 - index2))
            i += 1

        return dist