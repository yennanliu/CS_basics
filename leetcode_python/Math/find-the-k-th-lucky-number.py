"""

2802. Find The K-th Lucky Number
Medium

We know that 4 and 7 are lucky digits. Also, a number is called lucky if it contains only lucky digits.

You are given an integer k, return the kth lucky number represented as a string.


Example 1:

Input: k = 4
Output: "47"
Explanation: The first lucky number is 4, the second one is 7, the third one is 44 and the fourth one is 47.

Example 2:

Input: k = 10
Output: "477"
Explanation: Here are lucky numbers sorted in increasing order:
4, 7, 44, 47, 74, 77, 444, 447, 474, 477. So the 10th lucky number is 477.

Example 3:

Input: k = 1000
Output: "777747447"
Explanation: It can be shown that the 1000th lucky number is 777747447.


Constraints:

1 <= k <= 10^9

"""

# V0
# IDEA : MATH / BINARY ENCODING (4 -> bit 0, 7 -> bit 1)
#
#   lucky numbers sorted increasingly group by digit length : all 1-digit
#   ones first (2 of them), then all 2-digit ones (4), then 8, 16, ...
#   there are exactly 2^n lucky numbers with n digits.
#
#   step 1 : peel off whole length-blocks to find the length n of the answer
#            and the rank of k inside that block.
#   step 2 : inside a block, the numbers are in increasing order, which is
#            just lexicographic order of the digit string over {4 < 7}.
#            that is plain binary counting : the (k-1)-th string of the
#            block written in base 2 with 4=0 and 7=1.
#
#   NOTE : k is 1-indexed, so we compare with `k <= 2^(n-1)` to pick '4'
#          (the lower half) and subtract the half-size before recursing when
#          picking '7'. equivalently, encode k-1 in binary - both are done
#          below via the halving loop.
#
#   NOTE : k <= 10^9 < 2^30, so the total number of lucky numbers of length
#          <= 30 already exceeds k and the loop runs at most ~30 times.
#          no big-integer or overflow concern in python.
#
# time = O(log k), space = O(log k)
class Solution(object):
    def kthLuckyNumber(self, k):
        # find the digit length n : there are 2^n lucky numbers of length n
        n = 1
        while k > (1 << n):
            k -= (1 << n)
            n += 1

        res = []
        while n > 0:
            n -= 1
            half = 1 << n            # count of length-n suffixes per branch
            if k <= half:
                res.append('4')
            else:
                res.append('7')
                k -= half
        return ''.join(res)
