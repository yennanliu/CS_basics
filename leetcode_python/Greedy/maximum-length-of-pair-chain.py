# V0 

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/79826524
# IDEA : GREEDY
class Solution(object):
    def findLongestChain(self, pairs):
        """
        :type pairs: List[List[int]]
        :rtype: int
        """
        pairs.sort(key=lambda x: x[1])
        currTime, ans = float('-inf'), 0
        for x, y in pairs:
            if currTime < x:
                currTime = y
                ans += 1
        return ans
        
# V2 
# Time:  O(nlogn)
# Space: O(1)
class Solution(object):
    def findLongestChain(self, pairs):
        """
        :type pairs: List[List[int]]
        :rtype: int
        """
        pairs.sort(key=lambda x: x[1])
        cnt, i = 0, 0
        for j in range(len(pairs)):
            if j == 0 or pairs[i][1] < pairs[j][0]:
                cnt += 1
                i = j
        return cnt