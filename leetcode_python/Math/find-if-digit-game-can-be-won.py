"""

3232. Find if Digit Game Can Be Won
Easy

You are given an array of positive integers nums.

Alice and Bob are playing a game. In the game, Alice can choose either all single-digit numbers or all double-digit numbers from nums, and the rest of the numbers are given to Bob. Alice wins if the sum of her numbers is strictly greater than the sum of Bob's numbers.

Return true if Alice can win this game, otherwise, return false.


Example 1:

Input: nums = [1,2,3,4,10]
Output: false
Explanation:
Alice cannot win by choosing either single-digit or double-digit numbers.

Example 2:

Input: nums = [1,2,3,4,5,14]
Output: true
Explanation:
Alice can win by choosing single-digit numbers which have a sum equal to 15.

Example 3:

Input: nums = [5,5,5,25]
Output: true
Explanation:
Alice can win by choosing double-digit numbers which have a sum equal to 25.


Constraints:

1 <= nums.length <= 100
1 <= nums[i] <= 99

"""

# V0
# IDEA : THE TWO CHOICES ARE COMPLEMENTARY — SO ALICE LOSES ONLY ON A TIE
#
#   the numbers split into exactly two groups, single-digit and double-digit,
#   and Alice takes one while Bob takes the other. so her two options give
#   sums s1 vs s2 and s2 vs s1.
#
#   she wins whenever the two sums differ (take the larger one), and can only
#   fail when they are exactly equal.
#
# time = O(n), space = O(1)
class Solution(object):
    def canAliceWin(self, nums):
        single = sum(x for x in nums if x < 10)
        double = sum(x for x in nums if x >= 10)
        return single != double
