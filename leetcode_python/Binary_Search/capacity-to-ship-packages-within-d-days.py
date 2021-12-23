"""

1011. Capacity To Ship Packages Within D Days
Medium

A conveyor belt has packages that must be shipped from one port to another within days days.

The ith package on the conveyor belt has a weight of weights[i]. Each day, we load the ship with packages on the conveyor belt (in the order given by weights). We may not load more weight than the maximum weight capacity of the ship.

Return the least weight capacity of the ship that will result in all the packages on the conveyor belt being shipped within days days.

 

Example 1:

Input: weights = [1,2,3,4,5,6,7,8,9,10], days = 5
Output: 15
Explanation: A ship capacity of 15 is the minimum to ship all the packages in 5 days like this:
1st day: 1, 2, 3, 4, 5
2nd day: 6, 7
3rd day: 8
4th day: 9
5th day: 10

Note that the cargo must be shipped in the order given, so using a ship of capacity 14 and splitting the packages into parts like (2, 3, 4, 5), (1, 6, 7), (8), (9), (10) is not allowed.
Example 2:

Input: weights = [3,2,2,4,1,4], days = 3
Output: 6
Explanation: A ship capacity of 6 is the minimum to ship all the packages in 3 days like this:
1st day: 3, 2
2nd day: 2, 4
3rd day: 1, 4
Example 3:

Input: weights = [1,2,3,1,1], days = 4
Output: 3
Explanation:
1st day: 1
2nd day: 2
3rd day: 3
4th day: 1, 1
 

Constraints:

1 <= days <= weights.length <= 5 * 104
1 <= weights[i] <= 500

"""

# V0

# V1
# IDEA : BINARY SEARCH
# https://leetcode.com/problems/capacity-to-ship-packages-within-d-days/discuss/390359/Simple-Python-Binary-Search
class Solution(object):
     def shipWithinDays(self, weights, D):
            def cannot_split(weights, D, max_wgt):
                s = 0
                days = 1
                for w in weights:
                    s += w
                    if s > max_wgt:
                        s = w
                        days += 1
                return days > D

            low, high = max(weights), sum(weights)
            while low < high:
                mid = low + (high - low) // 2
                if cannot_split(weights, D, mid):
                    low = mid + 1
                else:
                    high = mid
            return low

# V1'
# IDEA : BINARY SEARCH
# --> Given the number of bags,
# --> return the minimum capacity of each bag,
# --> so that we can put items one by one into all bags.
# --> We binary search the final result.
# --> The left bound is max(A),
# --> The right bound is sum(A).
# --> https://leetcode.com/problems/capacity-to-ship-packages-within-d-days/discuss/256729/JavaC%2B%2BPython-Binary-Search
class Solution(object):
    def shipWithinDays(self, weights, D):
            left, right = max(weights), sum(weights)
            while left < right:
                mid, need, cur = (left + right) / 2, 1, 0
                for w in weights:
                    if cur + w > mid:
                        need += 1
                        cur = 0
                    cur += w
                if need > D:
                	left = mid + 1
                else: 
                	right = mid
            return left

# V1''
# IDEA : BINARY SEARCH
# https://leetcode.com/problems/capacity-to-ship-packages-within-d-days/discuss/884307/Python-binary-search
class Solution(object):
    def shipWithinDays(self, weights, D):
            left = max(weights)
            right = sum(weights)
            while left < right:
                mid = left + (right - left) // 2
                if self.condition(mid, weights) <= D:
                    right = mid
                else:
                    left = mid+1
            return left
    
    def condition(self, k, weights):
        d = 0
        cur_weight = 0
        for weight in weights:
            if cur_weight + weight == k:
                d += 1
                cur_weight = 0
            elif cur_weight + weight > k:
                d += 1
                cur_weight = weight
            else:
                cur_weight += weight
        return d+1 if cur_weight > 0 else d

# V1'''
# https://leetcode.com/problems/capacity-to-ship-packages-within-d-days/discuss/257887/Python-Binary-Search
class Solution:
    def shipWithinDays(self, weights, D):
        low, high = max(weights), sum(weights)
        while low < high:
            mid = (low + high) // 2
            days = self.findDays(weights, mid)
            if days <= D:
                high = mid
            elif days > D:
                low = mid + 1
        return high
         
    def findDays(self, weights, capacity):
        days = 1
        cur_w = 0
        
        for w in weights:
            if w + cur_w > capacity:
                cur_w = w
                days += 1
            else:
                cur_w += w
        return days

# V2