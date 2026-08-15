"""

3133. Minimum Array End
Medium

You are given two integers n and x. You have to construct an array of positive integers nums of size n where for every 0 <= i < n - 1, nums[i + 1] is greater than nums[i], and the result of the bitwise AND operation between all elements of nums is x.

Return the minimum possible value of nums[n - 1].


Example 1:

Input: n = 3, x = 4
Output: 6
Explanation:
nums can be [4,5,6] and its last element is at its minimum possible value of 6.

Example 2:

Input: n = 2, x = 7
Output: 15
Explanation:
nums can be [7,15] and its last element is at its minimum possible value of 15.


Constraints:

1 <= n, x <= 10^8

"""

# V0
# IDEA : EVERY ELEMENT MUST BE A SUPERSET OF x'S BITS — SO COUNT IN THE FREE BITS
#
#   the AND of all elements equals x, so every element has all of x's bits
#   set. the elements are strictly increasing and we want the last one as
#   small as possible, which means the n values should be the n SMALLEST
#   supersets of x :
#
#       x itself, then x with 1 added in the free (zero) bit positions,
#       counting 0, 1, 2, ... in those positions
#
#   so the last element is x with the binary digits of (n - 1) scattered into
#   x's zero-bit slots, lowest slot first.
#
#   NOTE : any bit of x is left untouched, and the free slots act like an
#          ordinary binary counter, which is why the n-th superset is just
#          "write n-1 into the holes".
#
# time = O(log n + log x), space = O(1)
class Solution(object):
    def minEnd(self, n, x):
        res = x
        k = n - 1                    # how far to count in the free positions
        bit = 0                      # position being examined in `res`
        while k:
            if not (x >> bit) & 1:   # a free slot : take the next bit of k
                if k & 1:
                    res |= 1 << bit
                k >>= 1
            bit += 1
        return res
