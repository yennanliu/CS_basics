"""

4078. Three Fibonacci Sum
Easy

You are given an integer n.

The Fibonacci sequence starts with 0 and 1, and each subsequent number is the
sum of the previous two numbers. Its first few terms are
0, 1, 1, 2, 3, 5, 8, 13, ....

Return true if n can be expressed as the sum of three consecutive terms in the
Fibonacci sequence, and false otherwise.


Example 1:

Input: n = 16

Output: true

Explanation:

The three consecutive terms 3, 5, and 8 have a sum of 3 + 5 + 8 = 16.

Example 2:

Input: n = 8

Output: false

Explanation:

Although 1 + 2 + 5 = 8, these terms are not consecutive in the Fibonacci
sequence. No three consecutive terms have a sum of 8.

Example 3:

Input: n = 2

Output: true

Explanation:

The first three terms have a sum of 0 + 1 + 1 = 2.


Constraints:

1 <= n <= 10^9

"""

# V0
# IDEA : GENERATE FIBONACCI TERMS + SLIDE A WINDOW OF 3 CONSECUTIVE TERMS
#
#   the terms grow exponentially, so only ~45 of them stay <= 10^9 : just
#   build them all, then check every window of 3 consecutive terms.
#
#   the window sums are strictly increasing from the first window on
#   (0+1+1=2, 1+1+2=4, 1+2+3=6, ...), so we can stop as soon as a sum
#   passes n.
#
#   e.g. n = 16 -> fib = [0,1,1,2,3,5,8,13,...] -> 3 + 5 + 8 = 16 -> True
#        n = 8  -> window sums 2, 4, 6, 10 -> jumps over 8 -> False
#
# time = O(log n), space = O(log n)
class Solution(object):
    def threeFibonacciSum(self, n):
        """
        :type n: int
        :rtype: bool
        """
        # build every Fibonacci term up to n (a window containing a term > n
        # already sums to more than n, so later terms are never needed)
        fib = [0, 1]
        while fib[-1] <= n:
            fib.append(fib[-1] + fib[-2])

        # try every window of 3 consecutive terms
        for i in range(len(fib) - 2):
            window_sum = fib[i] + fib[i + 1] + fib[i + 2]
            if window_sum == n:
                return True
            # NOTE !!! window sums only grow, so once past n it is hopeless
            if window_sum > n:
                return False

        return False


# V1
# IDEA : MATH — F(i) + F(i+1) + F(i+2) == 2 * F(i+2)
#
#   since F(i) + F(i+1) = F(i+2), three consecutive terms sum to
#   F(i+2) + F(i+2) = 2 * F(i+2). so n is such a sum exactly when n is even
#   and n / 2 is a Fibonacci term F(k) with k >= 2 (the first window is
#   0 + 1 + 1 = 2 * F(2)).
#
#   e.g. n = 16 -> 16 / 2 = 8 = F(6) -> True ;  n = 8 -> 8 / 2 = 4 -> not fib -> False
#
#   NOTE : same bound as V0, the identity is the thing worth remembering
#
# time = O(log n), space = O(1)
class Solution2(object):
    def threeFibonacciSum(self, n):
        """
        :type n: int
        :rtype: bool
        """
        # 2 * F(k) is always even
        if n % 2 == 1:
            return False

        half = n // 2

        # walk F(2), F(3), ... = 1, 2, 3, 5, ... until it reaches half
        prev = 1   # F(1)
        cur = 1    # F(2)
        while cur < half:
            prev, cur = cur, prev + cur

        return cur == half
