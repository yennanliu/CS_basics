"""

1944. Number of Visible People in a Queue
Hard

There are n people standing in a queue, and they numbered from 0 to n - 1 in left to right order. You are given an array heights of distinct integers where heights[i] represents the height of the ith person.

A person can see another person to their right in the queue if everybody in between is shorter than both of them. More formally, the ith person can see the jth person if i < j and min(heights[i], heights[j]) > max(heights[i+1], heights[i+2], ..., heights[j-1]).

Return an array answer of length n where answer[i] is the number of people the ith person can see to their right in the queue.


Example 1:

Input: heights = [10,6,8,5,11,9]
Output: [3,1,2,1,1,0]
Explanation:
Person 0 can see person 1, 2, and 4.
Person 1 can see person 2.
Person 2 can see person 3 and 4.
Person 3 can see person 4.
Person 4 can see person 5.
Person 5 can see no one since nobody is to the right of them.

Example 2:

Input: heights = [5,1,2,3,10]
Output: [4,1,1,1,0]


Constraints:

n == heights.length
1 <= n <= 10^5
1 <= heights[i] <= 10^5
All the values of heights are unique.

"""

# V0
# IDEA : MONOTONIC (DECREASING) STACK, SCANNING RIGHT -> LEFT
#
#   the people person i can see form a strictly INCREASING chain of heights to
#   his right, ending at the first person taller than him (that one blocks
#   everybody behind).
#
#   keep a stack that is decreasing from bottom to top holding the heights to
#   the right of i. for person i :
#     - pop every shorter person : each of them is directly visible
#     - if the stack is still non-empty, the remaining top is the first TALLER
#       person -> he is visible too, and he blocks the rest
#     - push heights[i]
#
#   NOTE : every height is pushed and popped once -> the whole scan is linear
#          even though it contains a while-loop.
#
# time = O(n), space = O(n)
class Solution(object):
    def canSeePersonsCount(self, heights):
        n = len(heights)
        res = [0] * n
        stack = []
        for i in range(n - 1, -1, -1):
            h = heights[i]
            while stack and stack[-1] < h:
                stack.pop()
                res[i] += 1
            if stack:
                res[i] += 1
            stack.append(h)
        return res
