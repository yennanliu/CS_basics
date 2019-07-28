# V0 

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/81749170
class Solution:
    def uncommonFromSentences(self, A, B):
        """
        :type A: str
        :type B: str
        :rtype: List[str]
        """
        count_A = collections.Counter(A.split(' '))
        count_B = collections.Counter(B.split(' '))
        words = list((count_A.keys() | count_B.keys()) - (count_A.keys() & count_B.keys()))
        ans = []
        for word in words:
            if count_A[word] == 1 or count_B[word] == 1:
                ans.append(word)
        return ans

# V2 
# Time:  O(m + n)
# Space: O(m + n)
import collections
class Solution(object):
    def uncommonFromSentences(self, A, B):
        """
        :type A: str
        :type B: str
        :rtype: List[str]
        """
        count = collections.Counter(A.split())
        count += collections.Counter(B.split())
        return [word for word in count if count[word] == 1]