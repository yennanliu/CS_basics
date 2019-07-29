# V0 

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/84924672
# https://blog.csdn.net/GQxxxxxl/article/details/84927823
class Solution:
    def isAlienSorted(self, words, order):
        """
        :type words: List[str]
        :type order: str
        :rtype: bool
        """

        for i in range(len(words)-1):
            for j in range(len(words[i])):
                if j>len(words[i+1])-1:
                    return(False)
                elif order.index(words[i][j])<order.index(words[i+1][j]):
                    break
                elif order.index(words[i][j])==order.index(words[i+1][j]):
                    continue
                else:
                    return(False)
        return(True)
        
# V2
# Time:  O(n * l), l is the average length of words
# Space: O(1)
class Solution(object):
    def isAlienSorted(self, words, order):
        """
        :type words: List[str]
        :type order: str
        :rtype: bool
        """
        lookup = {c: i for i, c in enumerate(order)}
        for i in range(len(words)-1):
            word1 = words[i]
            word2 = words[i+1]
            for k in range(min(len(word1), len(word2))):
                if word1[k] != word2[k]:
                    if lookup[word1[k]] > lookup[word2[k]]:
                        return False
                    break
            else:
                if len(word1) > len(word2):
                    return False
        return True
