"""

1646. Get Maximum in Generated Array
Easy

You are given an integer n. A 0-indexed integer array nums of length n + 1 is generated in the following way:

nums[0] = 0
nums[1] = 1
nums[2 * i] = nums[i] when 2 <= 2 * i <= n
nums[2 * i + 1] = nums[i] + nums[i + 1] when 2 <= 2 * i + 1 <= n

Return the maximum integer in the array nums.


Example 1:

Input: n = 7
Output: 3
Explanation: According to the given rules:
  nums[0] = 0
  nums[1] = 1
  nums[(1 * 2) = 2] = nums[1] = 1
  nums[(1 * 2) + 1 = 3] = nums[1] + nums[2] = 1 + 1 = 2
  nums[(2 * 2) = 4] = nums[2] = 1
  nums[(2 * 2) + 1 = 5] = nums[2] + nums[3] = 1 + 2 = 3
  nums[(3 * 2) = 6] = nums[3] = 2
  nums[(3 * 2) + 1 = 7] = nums[3] + nums[4] = 2 + 1 = 3
Hence, nums = [0,1,1,2,1,3,2,3], and the maximum is max(0,1,1,2,1,3,2,3) = 3.

Example 2:

Input: n = 2
Output: 1
Explanation: According to the given rules, nums = [0,1,1]. The maximum is max(0,1,1) = 1.

Example 3:

Input: n = 3
Output: 2
Explanation: According to the given rules, nums = [0,1,1,2]. The maximum is max(0,1,1,2) = 2.


Constraints:

0 <= n <= 100

"""

# V0
# IDEA : SIMULATION (build the array left to right, every rule looks back)
#
#   rewrite both rules in terms of i alone:
#     i even -> nums[i] = nums[i // 2]
#     i odd  -> nums[i] = nums[i // 2] + nums[i // 2 + 1]
#   both referenced indices are < i (for i >= 2), so a single forward pass
#   already has them computed.
#
#   NOTE : n = 0 and n = 1 must be answered directly -- the array is too
#          short for the loop to run.
#
# time = O(n), space = O(n)
class Solution(object):
    def getMaximumGenerated(self, n):
        if n < 2:
            return n
        nums = [0] * (n + 1)
        nums[1] = 1
        for i in range(2, n + 1):
            half = i // 2
            if i % 2 == 0:
                nums[i] = nums[half]
            else:
                nums[i] = nums[half] + nums[half + 1]
        return max(nums)


# V0-1
# IDEA : TOP-DOWN RECURSION + MEMOIZATION
#
#   same two rules, but read as a recursive definition instead of a forward
#   pass :
#     val(0) = 0, val(1) = 1
#     val(i) = val(i // 2)                       if i is even
#     val(i) = val(i // 2) + val(i // 2 + 1)     if i is odd
#   the memo dict turns the (exponentially branching) recursion into one
#   evaluation per index, and the recursion depth is only O(log n) because
#   every call halves the argument.
#
#   NOTE : we still have to ASK for every index 0..n to know the maximum --
#          the largest value is not always at the end (nums[7] == nums[5] == 3
#          for n = 7), so there is no shortcut to a single call.
#
# time = O(n)
# space = O(n)
class Solution(object):
    def getMaximumGenerated(self, n):
        if n < 2:
            return n

        memo = {0: 0, 1: 1}

        def val(i):
            if i in memo:
                return memo[i]
            half = i // 2
            if i % 2 == 0:
                res = val(half)
            else:
                res = val(half) + val(half + 1)
            memo[i] = res
            return res

        return max(val(i) for i in range(n + 1))


# V0-2
# IDEA : BIT WALK (STERN'S DIATOMIC SERIES CLOSED FORM), O(1) SPACE
#
#   nums is exactly Stern's diatomic series (fusc) :
#     s(0) = 0, s(1) = 1, s(2i) = s(i), s(2i + 1) = s(i) + s(i + 1)
#   fusc has a classic O(log i) evaluation that walks the BINARY DIGITS of i
#   from the least significant end while carrying a pair (a, b) :
#
#       a, b = 1, 0
#       for each bit of i, low -> high:
#           bit 1 -> b += a
#           bit 0 -> a += b
#       answer = b
#
#   the pair (a, b) is the running 2x2-matrix product of the two branch
#   transforms, so no array of previous values is needed at all -- each index
#   is evaluated completely on its own.
#
#   NOTE : this trades the O(n) table for O(1) memory at the price of an extra
#          log factor in time; with n <= 100 both are trivial, but the point is
#          that a single nums[i] can be answered without building the array.
#
# time = O(n * log n)
# space = O(1)
class Solution(object):
    def getMaximumGenerated(self, n):
        if n < 2:
            return n

        best = 0
        for i in range(n + 1):
            a, b = 1, 0
            x = i
            while x:
                if x & 1:
                    b += a
                else:
                    a += b
                x >>= 1
            best = max(best, b)
        return best
