"""

793. Preimage Size of Factorial Zeroes Function
Hard

Let f(x) be the number of zeroes at the end of x!.
Recall that x! = 1 * 2 * 3 * ... * x and by convention, 0! = 1.

For example, f(3) = 0 because 3! = 6 has no zeroes at the end,
while f(11) = 2 because 11! = 39916800 has two zeroes at the end.

Given an integer k, return the number of non-negative integers x
have the property that f(x) = k.


Example 1:

Input: k = 0
Output: 5
Explanation: 0!, 1!, 2!, 3!, and 4! end with k = 0 zeroes.

Example 2:

Input: k = 5
Output: 0
Explanation: There is no x such that x! ends in k = 5 zeroes.

Example 3:

Input: k = 3
Output: 5


Constraints:

0 <= k <= 10^9

"""

# V0
# IDEA : BINARY SEARCH on a monotonic function
#
#   f(x) = x//5 + x//25 + x//125 + ...  (number of factor-5s in x!)
#   f is non-decreasing, so
#
#       answer = (smallest x with f(x) >= k+1) - (smallest x with f(x) >= k)
#
#   i.e. the width of the "plateau" where f equals exactly k.
#   The answer is always 0 or 5 (f jumps by >= 2 whenever x crosses 25, 50, ...).
#
#   Search bound: f(5*m) >= m, so 5*(k+1) is always a safe upper bound.
#
# time = O(log(k) * log(k))   # binary search x log5 steps inside f
# space = O(1)
class Solution(object):
    def preimageSizeFZF(self, k):

        def f(x):
            # trailing zeros of x!
            cnt = 0
            while x:
                x //= 5
                cnt += x
            return cnt

        def lowest(target):
            # smallest x such that f(x) >= target
            lo, hi = 0, 5 * target + 5
            while lo < hi:
                mid = (lo + hi) // 2
                if f(mid) >= target:
                    hi = mid
                else:
                    lo = mid + 1
            return lo

        return lowest(k + 1) - lowest(k)
