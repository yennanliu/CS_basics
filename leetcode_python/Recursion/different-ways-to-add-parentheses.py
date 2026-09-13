"""

241. Different Ways to Add Parentheses
Medium

Given a string expression of numbers and operators, return all possible results from computing all the different possible ways to group numbers and operators. You may return the answer in any order.

The test cases are generated such that the output values fit in a 32-bit integer and the number of different results does not exceed 10^4.

Example 1:

Input: expression = "2-1-1"
Output: [0,2]
Explanation:
((2-1)-1) = 0
(2-(1-1)) = 2

Example 2:

Input: expression = "2*3-4*5"
Output: [-34,-14,-10,-10,10]
Explanation:
(2*(3-(4*5))) = -34
((2*3)-(4*5)) = -14
((2*(3-4))*5) = -10
(2*((3-4)*5)) = -10
(((2*3)-4)*5) = 10

Constraints:

1 <= expression.length <= 20
expression consists of digits and the operator '+', '-', and '*'.
All the integer values in the input expression are in the range [0, 99].
The integer values in the input expression do not have a leading '-' or '+' denoting the sign.

"""

# V0 : DEV 

# V1 
# Time:  O(n * 4^n / n^(3/2)) ~= n * Catalan numbers = n * (C(2n, n) - C(2n, n - 1)),
#                                due to the size of the results is Catalan numbers,
#                                and every way of evaluation is the length of the string,
#                                so the time complexity is at most n * Catalan numbers.
# Space: O(n * 4^n / n^(3/2)), the cache size of lookup is at most n * Catalan numbers.



# V2
# time = O(n * 4^n / n^(3/2)), n = number of operators; interval DP w/ memoization, Catalan-number bound
# space = O(n * 4^n / n^(3/2)), memoization table size
import operator
import re
class Solution(object):
    # @param {string} input
    # @return {integer[]}
    def diffWaysToCompute(self, input):
        tokens = re.split('(\D)', input)
        nums = list(map(int, tokens[::2]))
        ops = list(map({'+': operator.add, '-': operator.sub, '*': operator.mul}.get, tokens[1::2]))
        lookup = [[None for _ in range(len(nums))] for _ in range(len(nums))]

        def diffWaysToComputeRecu(left, right):
            if left == right:
                return [nums[left]]
            if lookup[left][right]:
                return lookup[left][right]
            lookup[left][right] = [ops[i](x, y)
                                   for i in range(left, right)
                                   for x in diffWaysToComputeRecu(left, i)
                                   for y in diffWaysToComputeRecu(i + 1, right)]
            return lookup[left][right]

        return diffWaysToComputeRecu(0, len(nums) - 1)

# time = O(n * 4^n / n^(3/2)), n = number of operators; interval DP w/ memoization, Catalan-number bound
# space = O(n * 4^n / n^(3/2)), memoization table size
class Solution2(object):
    # @param {string} input
    # @return {integer[]}
    def diffWaysToCompute(self, input):
        lookup = [[None for _ in range(len(input) + 1)] for _ in range(len(input) + 1)]
        ops = {'+': operator.add, '-': operator.sub, '*': operator.mul}

        def diffWaysToComputeRecu(left, right):
            if lookup[left][right]:
                return lookup[left][right]
            result = []
            for i in range(left, right):
                if input[i] in ops:
                    for x in diffWaysToComputeRecu(left, i):
                        for y in diffWaysToComputeRecu(i + 1, right):
                            result.append(ops[input[i]](x, y))

            if not result:
                result = [int(input[left:right])]
            lookup[left][right] = result
            return lookup[left][right]

        return diffWaysToComputeRecu(0, len(input))
