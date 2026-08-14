"""

2582. Pass the Pillow
Easy
There are n people standing in a line labeled from 1 to n. The first person in the line is holding
a pillow initially. Every second, the person holding the pillow passes it to the next person
standing in the line. Once the pillow reaches the end of the line, the direction changes, and
people continue passing the pillow in the opposite direction.

For example, once the pillow reaches the nth person they pass it to the n - 1th person,
then to the n - 2th person and so on.

Given the two positive integers n and time, return the index of the person holding the pillow
after time seconds.


Example 1:

Input: n = 4, time = 5
Output: 2
Explanation: People pass the pillow in the following way: 1 -> 2 -> 3 -> 4 -> 3 -> 2.
After five seconds, the 2nd person is holding the pillow.

Example 2:

Input: n = 3, time = 2
Output: 3
Explanation: People pass the pillow in the following way: 1 -> 2 -> 3.
After two seconds, the 3rd person is holding the pillow.


Constraints:

2 <= n <= 1000
1 <= time <= 1000

Note: This question is the same as 3178: Find the Child Who Has the Ball After K Seconds.

"""

# V0
# IDEA : MATH (divmod on the half-period)
#
#   one "leg" (1 -> n, or n -> 1) takes exactly n - 1 seconds, so the motion is
#   periodic with half-period n - 1. Split time into full legs + leftover:
#       k, r = divmod(time, n - 1)
#
#   NOTE : k = number of COMPLETED legs, so it also tells us the direction we
#          are moving in right now:
#            k even -> we are walking forward from person 1  -> answer r + 1
#            k odd  -> we are walking backward from person n -> answer n - r
#
#   NOTE : n >= 2 is guaranteed, so n - 1 is never 0 and divmod is safe.
#
# time = O(1), space = O(1)
class Solution(object):
    def passThePillow(self, n, time):
        k, r = divmod(time, n - 1)
        if k % 2 == 1:
            return n - r
        return r + 1
