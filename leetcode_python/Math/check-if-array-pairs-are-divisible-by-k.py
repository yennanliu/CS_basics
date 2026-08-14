"""

1497. Check If Array Pairs Are Divisible by k
Medium

Given an array of integers arr of even length n and an integer k.

We want to divide the array into exactly n / 2 pairs such that the sum of each pair is divisible by k.

Return true If you can find a way to do that or false otherwise.


Example 1:

Input: arr = [1,2,3,4,5,10,6,7,8,9], k = 5
Output: true
Explanation: Pairs are (1,9),(2,8),(3,7),(4,6) and (5,10).

Example 2:

Input: arr = [1,2,3,4,5,6], k = 7
Output: true
Explanation: Pairs are (1,6),(2,5) and(3,4).

Example 3:

Input: arr = [1,2,3,4,5,6], k = 10
Output: false
Explanation: You can try all possible pairs to see that there is no way to divide arr into 3 pairs each with sum divisible by 10.


Constraints:

arr.length == n
1 <= n <= 10^5
n is even.
-10^9 <= arr[i] <= 10^9
1 <= k <= 10^5

"""

# V0
# IDEA : COUNT REMAINDERS
#
#   a + b is divisible by k
#     <=> (a % k) + (b % k) is divisible by k
#
#   so remainder r must pair with remainder (k - r).
#   -> cnt[r] must equal cnt[k - r] for r in 1 .. k-1
#   -> cnt[0] must be even (those pair among themselves)
#
#   NOTE : python's `%` already returns a non negative remainder,
#          so we do NOT need the ((x % k) + k) % k trick
# time = O(n + k)
# space = O(k)
class Solution(object):
    def canArrange(self, arr, k):
        cnt = [0] * k
        for x in arr:
            cnt[x % k] += 1

        # remainder 0 : must be pairable within itself
        if cnt[0] % 2 != 0:
            return False

        # remainder k/2 (only when k is even) also pairs with itself
        if k % 2 == 0 and cnt[k // 2] % 2 != 0:
            return False

        for r in range(1, k // 2 + 1):
            if cnt[r] != cnt[k - r]:
                return False

        return True


# V0-1
# IDEA : COUNT REMAINDERS (hash map, no k sized array)
# time = O(n)
# space = O(k)
from collections import Counter
class Solution(object):
    def canArrange(self, arr, k):
        cnt = Counter(x % k for x in arr)
        for r, c in cnt.items():
            if r == 0:
                if c % 2 != 0:
                    return False
            elif cnt[k - r] != c:
                return False
        return True
