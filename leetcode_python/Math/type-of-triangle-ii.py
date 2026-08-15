"""

3024. Type of Triangle II
Easy

You are given a 0-indexed integer array nums of size 3 which can form the sides of a triangle.

A triangle is called equilateral if it has all sides of equal length.
A triangle is called isosceles if it has exactly two sides of equal length.
A triangle is called scalene if all its sides are of different lengths.

Return a string representing the type of triangle that can be formed or "none" if it cannot form a triangle.


Example 1:

Input: nums = [3,3,3]
Output: "equilateral"
Explanation: Since all the sides are of equal length, therefore, it will form an equilateral triangle.

Example 2:

Input: nums = [3,4,5]
Output: "scalene"
Explanation:
nums[0] + nums[1] = 3 + 4 = 7, which is greater than nums[2] = 5.
nums[0] + nums[2] = 3 + 5 = 8, which is greater than nums[1] = 4.
nums[1] + nums[2] = 4 + 5 = 9, which is greater than nums[0] = 3.
Since the sum of the two sides is greater than the third side for all three cases, therefore, it can form a triangle.
As all the sides are of different lengths, it will form a scalene triangle.


Constraints:

nums.length == 3
1 <= nums[i] <= 100

"""

# V0
# IDEA : SORT FIRST — THEN ONE INEQUALITY DECIDES EXISTENCE
#
#   with a <= b <= c the triangle inequality only needs checking once :
#   a + b > c implies the other two automatically. if it fails, "none".
#
#   otherwise the type follows from how many sides coincide :
#       all three equal   -> equilateral
#       exactly two equal -> isosceles
#       none equal        -> scalene
#
# time = O(1), space = O(1)
class Solution(object):
    def triangleType(self, nums):
        a, b, c = sorted(nums)
        if a + b <= c:
            return "none"
        if a == b == c:
            return "equilateral"
        if a == b or b == c:
            return "isosceles"
        return "scalene"
