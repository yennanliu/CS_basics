"""

1395. Count Number of Teams
Medium

There are n soldiers standing in a line. Each soldier is assigned a unique rating value.

You have to form a team of 3 soldiers amongst them under the following rules:

Choose 3 soldiers with index (i, j, k) with rating (rating[i], rating[j], rating[k]).
A team is valid if: (rating[i] < rating[j] < rating[k]) or (rating[i] > rating[j] > rating[k]) where (0 <= i < j < k < n).

Return the number of teams you can form given the conditions. (soldiers can be part of multiple teams).


Example 1:

Input: rating = [2,5,3,4,1]
Output: 3
Explanation: We can form three teams given the conditions. (2,3,4), (5,4,1), (5,3,1).

Example 2:

Input: rating = [2,1,3]
Output: 0
Explanation: We can't form any team given the conditions.

Example 3:

Input: rating = [1,2,3,4]
Output: 4


Constraints:

n == rating.length
3 <= n <= 1000
1 <= rating[i] <= 10^5
All the integers in rating are unique.

"""

# V0
# IDEA : FIX THE MIDDLE SOLDIER (count smaller-left / larger-right)
#
#   every team is determined by its middle index j, so enumerate j and let
#     l = # of i < j with rating[i] < rating[j]
#     r = # of k > j with rating[k] > rating[j]
#   increasing teams through j  = l * r
#   decreasing teams through j  = (j - l) * (n - 1 - j - r)
#     (the complements: bigger on the left, smaller on the right)
#   NOTE : ratings are unique, so no ties to worry about.
#
"""

DP def
    every team is determined by its MIDDLE soldier j, so enumerate j:

    less[j]   : # of i < j with rating[i] < rating[j]
    greater[j]: # of k > j with rating[k] > rating[j]

DP eq

     increasing teams through j = less[j] * greater[j]

     decreasing teams through j = (j - less[j]) * (n - 1 - j - greater[j])

                                  # the complements:
                                  #   bigger on the left, smaller on the right


    -> e.g.
         ans = sum over j of ( up_count + down_count )

     ratings are UNIQUE, so there are no ties to handle
     (less[] / greater[] can also be built in O(n log n) with a BIT)

"""
# time = O(n^2), space = O(1)
class Solution(object):
    def numTeams(self, rating):
        n = len(rating)
        res = 0
        for j in range(n):
            l = r = 0
            for i in range(j):
                if rating[i] < rating[j]:
                    l += 1
            for k in range(j + 1, n):
                if rating[k] > rating[j]:
                    r += 1
            res += l * r                                  # up  : small < mid < big
            res += (j - l) * (n - 1 - j - r)              # down: big > mid > small
        return res
