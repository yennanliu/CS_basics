"""

1550. Three Consecutive Odds
Easy

Given an integer array arr, return true if there are three consecutive odd numbers in the array. Otherwise, return false.


Example 1:

Input: arr = [2,6,4,1]
Output: false
Explanation: There are no three consecutive odds.

Example 2:

Input: arr = [1,2,34,3,4,5,7,23,12]
Output: true
Explanation: [5,7,23] are three consecutive odds.


Constraints:

1 <= arr.length <= 1000
1 <= arr[i] <= 1000

"""

# V0
# IDEA : RUNNING STREAK COUNTER
#
#   walk the array keeping `streak` = how many odd numbers we have seen
#   back-to-back. an even value resets it to 0.
#   the moment streak hits 3 we can answer immediately.
#
#   NOTE : no need to remember positions or look backwards — the counter
#          alone captures "three in a row".
#
# time = O(n), space = O(1)
class Solution(object):
    def threeConsecutiveOdds(self, arr):
        streak = 0
        for x in arr:
            if x % 2 == 1:
                streak += 1
                if streak == 3:
                    return True
            else:
                streak = 0
        return False
