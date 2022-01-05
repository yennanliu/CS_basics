"""

875. Koko Eating Bananas
Medium

Share
Koko loves to eat bananas. There are n piles of bananas, the ith pile has piles[i] bananas. The guards have gone and will come back in h hours.

Koko can decide her bananas-per-hour eating speed of k. Each hour, she chooses some pile of bananas and eats k bananas from that pile. If the pile has less than k bananas, she eats all of them instead and will not eat any more bananas during this hour.

Koko likes to eat slowly but still wants to finish eating all the bananas before the guards return.

Return the minimum integer k such that she can eat all the bananas within h hours.

 

Example 1:

Input: piles = [3,6,7,11], h = 8
Output: 4
Example 2:

Input: piles = [30,11,23,4,20], h = 5
Output: 30
Example 3:

Input: piles = [30,11,23,4,20], h = 6
Output: 23
 

Constraints:

1 <= piles.length <= 104
piles.length <= h <= 109
1 <= piles[i] <= 109

"""
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