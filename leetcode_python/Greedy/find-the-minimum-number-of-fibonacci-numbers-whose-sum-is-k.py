"""

1414. Find the Minimum Number of Fibonacci Numbers Whose Sum Is K
Medium

Given an integer k, return the minimum number of Fibonacci numbers whose sum is equal to k. The same Fibonacci number can be used multiple times.

The Fibonacci numbers are defined as:

F1 = 1
F2 = 1
Fn = Fn-1 + Fn-2 for n > 2.

It is guaranteed that for the given constraints we can always find such Fibonacci numbers that sum up to k.


Example 1:

Input: k = 7
Output: 2
Explanation: The Fibonacci numbers are: 1, 1, 2, 3, 5, 8, 13, ...
For k = 7 we can use 2 + 5 = 7.

Example 2:

Input: k = 10
Output: 2
Explanation: For k = 10 we can use 2 + 8 = 10.

Example 3:

Input: k = 19
Output: 3
Explanation: For k = 19 we can use 1 + 5 + 13 = 19.


Constraints:

1 <= k <= 10^9

"""

# V0
# IDEA : GREEDY (Zeckendorf - always take the biggest Fibonacci <= k)
#
#   build the Fibonacci numbers up to k, then repeatedly subtract the
#   largest one that still fits.
#   why greedy is optimal: if F[i] <= k < F[i+1] then k - F[i] < F[i-1],
#   so the next pick is strictly smaller than F[i-1] -> no number is ever
#   reused and no better decomposition exists (Zeckendorf's theorem).
#   NOTE : walk the list downward instead of re-searching each round.
#
# time = O(log k), space = O(log k)
class Solution(object):
    def findMinFibonacciNumbers(self, k):
        fib = [1, 1]
        while fib[-1] + fib[-2] <= k:
            fib.append(fib[-1] + fib[-2])

        res = 0
        i = len(fib) - 1
        while k > 0:
            if fib[i] <= k:
                k -= fib[i]
                res += 1
            i -= 1
        return res
