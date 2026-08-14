"""

2517. Maximum Tastiness of Candy Basket
Medium

You are given an array of positive integers price where price[i] denotes the price of the ith candy and a positive integer k.

The store sells baskets of k distinct candies. The tastiness of a candy basket is the smallest absolute difference of the prices of any two candies in the basket.

Return the maximum tastiness of a candy basket.


Example 1:

Input: price = [13,5,1,8,21,2], k = 3
Output: 8
Explanation: Choose the candies with the prices [13,5,21].
The tastiness of the candy basket is: min(|13 - 5|, |13 - 21|, |5 - 21|) = min(8, 8, 16) = 8.
It can be proven that 8 is the maximum tastiness that can be achieved.

Example 2:

Input: price = [1,3,1], k = 2
Output: 2
Explanation: Choose the candies with the prices [1,3].
The tastiness of the candy basket is: min(|1 - 3|) = min(2) = 2.
It can be proven that 2 is the maximum tastiness that can be achieved.

Example 3:

Input: price = [7,7,7,7], k = 2
Output: 0
Explanation: Choosing any two distinct candies from the candies we have will result in a tastiness of 0.


Constraints:

2 <= k <= price.length <= 10^5
1 <= price[i] <= 10^9

"""

# V0
# IDEA : BINARY SEARCH ON THE ANSWER + GREEDY FEASIBILITY CHECK
#
#   "can we pick k candies whose pairwise gaps are all >= x ?" is monotone in x:
#   if x works then anything smaller works too. so binary search the largest
#   feasible x (the classic "maximum minimum distance" / aggressive-cows shape).
#
#   check(x) : sort the prices, then greedily take a candy whenever it is at
#   least x above the last taken one. taking the earliest possible candy each
#   time leaves the most room for the rest, so this greedy is optimal — if it
#   cannot reach k picks, nothing can.
#
#   NOTE : after sorting, only ADJACENT chosen candies need checking; once
#          consecutive picks are >= x apart, every further-apart pair is too.
#
#   NOTE : use mid = (l + r + 1) // 2 with the "l = mid" branch. the plain
#          (l + r) // 2 would loop forever here when r == l + 1.
#
# time = O(n * log n + n * log(maxPrice)), space = O(1) beyond the sort
class Solution(object):
    def maximumTastiness(self, price, k):
        price.sort()

        def check(x):
            cnt = 1
            pre = price[0]
            for cur in price[1:]:
                if cur - pre >= x:
                    pre = cur
                    cnt += 1
                    if cnt >= k:
                        return True
            return cnt >= k

        left, right = 0, price[-1] - price[0]
        while left < right:
            mid = (left + right + 1) // 2
            if check(mid):
                left = mid
            else:
                right = mid - 1
        return left
