# V0 

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/82716042
# IDEA : BINARY SEARCH 
class Solution:
    def minEatingSpeed(self, piles, H):
        """
        :type piles: List[int]
        :type H: int
        :rtype: int
        """
        minSpeed, maxSpeed = 1, max(piles)
        while minSpeed <= maxSpeed:
            speed = minSpeed + (maxSpeed - minSpeed) // 2
            hour = 0
            for pile in piles:
                hour += math.ceil(pile / speed)
            if hour <= H:
                maxSpeed = speed - 1
            else:
                minSpeed = speed + 1
        return minSpeed

# V1' 
# https://blog.csdn.net/fuxuemingzhu/article/details/82716042
# IDEA : BINARY SEARCH 
class Solution(object):
    def minEatingSpeed(self, piles, H):
        """
        :type piles: List[int]
        :type H: int
        :rtype: int
        """
        l, r = 1, sum(piles)
        # [l, r)
        while l < r:
            K = l + (r - l) / 2
            curH = 0
            for p in piles:
                curH += p // K + (1 if p % K else 0)
            if curH > H:
                l = K + 1
            else:
                r = K
        return l
        
# V2 
# Time:  O(nlogr)
# Space: O(1)
class Solution(object):
    def minEatingSpeed(self, piles, H):
        """
        :type piles: List[int]
        :type H: int
        :rtype: int
        """
        def possible(piles, H, K):
            return sum((pile-1)//K+1 for pile in piles) <= H

        left, right = 1, max(piles)
        while left <= right:
            mid = left + (right-left)//2
            if possible(piles, H, mid):
                right = mid-1
            else:
                left = mid+1
        return left