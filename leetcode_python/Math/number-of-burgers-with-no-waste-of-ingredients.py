"""

1276. Number of Burgers with No Waste of Ingredients
Medium

Given two integers tomatoSlices and cheeseSlices.
The ingredients of different burgers are as follows:

Jumbo Burger: 4 tomato slices and 1 cheese slice.
Small Burger: 2 Tomato slices and 1 cheese slice.

Return [total_jumbo, total_small] so that the number of remaining tomatoSlices
equal to 0 and the number of remaining cheeseSlices equal to 0.
If it is not possible to make the remaining tomatoSlices and cheeseSlices
equal to 0 return [].


Example 1:

Input: tomatoSlices = 16, cheeseSlices = 7
Output: [1,6]
Explantion: To make one jumbo burger and 6 small burgers we need 4*1 + 2*6 = 16 tomato and 1 + 6 = 7 cheese.
There will be no remaining ingredients.

Example 2:

Input: tomatoSlices = 17, cheeseSlices = 4
Output: []
Explantion: There will be no way to use all ingredients to make small and jumbo burgers.

Example 3:

Input: tomatoSlices = 4, cheeseSlices = 17
Output: []
Explantion: Making 1 jumbo burger there will be 16 cheese remaining and making 2 small burgers there will be 15 cheese remaining.


Constraints:

0 <= tomatoSlices, cheeseSlices <= 10^7

"""

# V0
# IDEA : MATH (solve the 2 x 2 linear system)
#
#   4 * x + 2 * y = tomatoSlices
#       x +     y = cheeseSlices
#
#   ->  x = tomatoSlices / 2 - cheeseSlices
#       y = 2 * cheeseSlices - tomatoSlices / 2
#
#   valid only if tomatoSlices is even and both x, y >= 0
#
# time = O(1)
# space = O(1)
class Solution(object):
    def numOfBurgers(self, tomatoSlices, cheeseSlices):
        if tomatoSlices % 2 != 0:
            return []
        half = tomatoSlices // 2
        jumbo = half - cheeseSlices
        small = 2 * cheeseSlices - half
        if jumbo < 0 or small < 0:
            return []
        return [jumbo, small]
