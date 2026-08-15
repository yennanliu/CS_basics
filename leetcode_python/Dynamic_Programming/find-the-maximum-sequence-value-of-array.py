"""

3287. Find the Maximum Sequence Value of Array
Hard

You are given an integer array nums and a positive integer k.

The value of a sequence seq of size 2 * x is defined as:

(seq[0] OR seq[1] OR ... OR seq[x - 1]) XOR (seq[x] OR seq[x + 1] OR ... OR seq[2 * x - 1]).

Return the maximum value of any subsequence of nums having size 2 * k.


Example 1:

Input: nums = [2,6,7], k = 1
Output: 5
Explanation:
The subsequence [2, 7] has the maximum value of 2 XOR 7 = 5.

Example 2:

Input: nums = [4,2,5,6,7], k = 2
Output: 2
Explanation:
The subsequence [4, 5, 6, 7] has the maximum value of (4 OR 5) XOR (6 OR 7) = 2.


Constraints:

2 <= nums.length <= 400
1 <= nums[i] < 2^7
1 <= k <= nums.length / 2

"""

# V0
# IDEA : SPLIT AT EVERY CUT, TRACKING THE ACHIEVABLE ORs AS A 128-BIT SET
#
#   once the split point is fixed the two halves are independent, so
#
#       answer = max over cuts of  max { a XOR b :
#                   a an OR of k elements taken left of the cut,
#                   b an OR of k elements taken right of it }
#
#   values stay under 2^7, so an OR is one of 128 possibilities and the whole
#   reachable SET fits in a single python integer — bit p set means "OR = p
#   is achievable".
#
#   the trick that makes it fast : OR-ing every member of such a set with a
#   fixed value v is a handful of shifts rather than a loop over members. for
#   one bit b of v, members that already own bit b stay put, and the others
#   move up by exactly 2^b positions :
#
#       S = ((S & ~M[b]) << (1 << b)) | (S & M[b])
#
#   where M[b] pre-marks the positions whose index has bit b set. seven such
#   steps apply any v.
#
# time = O(n * k * 7 + n * 128^2), space = O(n * k)
class Solution(object):
    def maxValue(self, nums, k):
        n = len(nums)
        BITS = 7
        LIM = 1 << BITS
        FULL = (1 << LIM) - 1

        # M[b] : positions p (0..127) whose index has bit b set
        M = []
        for b in range(BITS):
            mask = 0
            for p in range(LIM):
                if (p >> b) & 1:
                    mask |= 1 << p
            M.append(mask)

        def apply_or(S, v):
            for b in range(BITS):
                if (v >> b) & 1:
                    S = (((S & ~M[b]) << (1 << b)) | (S & M[b])) & FULL
            return S

        pre = [[0] * (k + 1) for _ in range(n + 1)]
        for i in range(n + 1):
            pre[i][0] = 1                                  # OR of nothing = 0
        for i in range(1, n + 1):
            v = nums[i - 1]
            row, up = pre[i], pre[i - 1]
            for j in range(1, k + 1):
                row[j] = up[j] | apply_or(up[j - 1], v)

        suf = [[0] * (k + 1) for _ in range(n + 2)]
        for i in range(n + 2):
            suf[i][0] = 1
        for i in range(n, 0, -1):
            v = nums[i - 1]
            row, down = suf[i], suf[i + 1]
            for j in range(1, k + 1):
                row[j] = down[j] | apply_or(down[j - 1], v)

        def members(S):
            out = []
            while S:
                low = S & -S
                out.append(low.bit_length() - 1)
                S ^= low
            return out

        res = 0
        for i in range(k, n - k + 1):                      # k left, k right
            left, right = pre[i][k], suf[i + 1][k]
            if not left or not right:
                continue
            bs = members(right)
            for a in members(left):
                for b in bs:
                    if (a ^ b) > res:
                        res = a ^ b
                if res == LIM - 1:
                    return res
        return res
