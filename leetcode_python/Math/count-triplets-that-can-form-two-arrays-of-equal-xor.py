"""

1442. Count Triplets That Can Form Two Arrays of Equal XOR
Medium

Given an array of integers arr.

We want to select three indices i, j and k where (0 <= i < j <= k < arr.length).

Let's define a and b as follows:

a = arr[i] ^ arr[i + 1] ^ ... ^ arr[j - 1]
b = arr[j] ^ arr[j + 1] ^ ... ^ arr[k]

Note that ^ denotes the bitwise-xor operation.

Return the number of triplets (i, j and k) Where a == b.


Example 1:

Input: arr = [2,3,1,6,7]
Output: 4
Explanation: The triplets are (0,1,2), (0,2,2), (2,3,4) and (2,4,4)

Example 2:

Input: arr = [1,1,1,1,1]
Output: 10


Constraints:

1 <= arr.length <= 300
1 <= arr[i] <= 10^8

"""

# V0
# IDEA : XOR PREFIX TRICK
#
#  -> a == b  <=>  a ^ b == 0  <=>  xor of arr[i..k] == 0
#  -> j drops out of the condition entirely! once (i, k) has xor 0,
#     ANY j in (i, k] works -> that is (k - i) choices
#  -> so just enumerate i, extend k, and add (k - i) whenever the
#     running xor hits 0
#
# time = O(n^2)
# space = O(1)
class Solution(object):
    def countTriplets(self, arr):
        n = len(arr)
        res = 0
        for i in range(n):
            s = arr[i]
            for k in range(i + 1, n):
                s ^= arr[k]
                if s == 0:
                    res += k - i
        return res


# V1
# IDEA : HASH MAP ON PREFIX XOR
#
#  -> let p[x] = arr[0] ^ ... ^ arr[x-1].  xor(i..k) == 0 <=> p[i] == p[k+1]
#  -> for each right end r = k + 1, every earlier index i with p[i] == p[r]
#     contributes (r - i - 1) triplets
#  -> keep, per xor value, the COUNT of indices and the SUM of indices,
#     so the contribution is  cnt * (r - 1) - sum_of_indices
#
# time = O(n)
# space = O(n)
from collections import defaultdict
class Solution(object):
    def countTriplets(self, arr):
        cnt = defaultdict(int)
        idx_sum = defaultdict(int)

        # prefix xor 0 occurs at index 0
        cnt[0] = 1
        idx_sum[0] = 0

        res = 0
        p = 0
        for r in range(1, len(arr) + 1):
            p ^= arr[r - 1]
            if cnt[p]:
                res += cnt[p] * (r - 1) - idx_sum[p]
            cnt[p] += 1
            idx_sum[p] += r
        return res
