"""

2829. Determine the Minimum Sum of a k-avoiding Array
Medium

You are given two integers, n and k.

An array of distinct positive integers is called a k-avoiding array if there does not exist any pair of distinct elements that sum to k.

Return the minimum possible sum of a k-avoiding array of length n.


Example 1:

Input: n = 5, k = 4
Output: 18
Explanation: Consider the k-avoiding array [1,2,4,5,6], which has a sum of 18.
It can be proven that there is no k-avoiding array with a sum less than 18.

Example 2:

Input: n = 2, k = 6
Output: 3
Explanation: We can construct the array [1,2], which has a sum of 3.
It can be proven that there is no k-avoiding array with a sum less than 3.


Constraints:

1 <= n, k <= 50

"""

# V0
# IDEA : GREEDY (take the smallest still-legal integer each time)
#
#   Scan i = 1, 2, 3, ... and take i whenever it is not banned. Taking i bans
#   its partner k - i (that pair is the only way i can ever create the sum k).
#
#   NOTE : greedy is optimal because the integers pair up as
#          {1, k-1}, {2, k-2}, ... — at most one member of each pair may be
#          used, and the banned partner k - i is always LARGER than the i we
#          took (we meet i first), so we never trade a small number away for
#          a bigger one.
#
#   NOTE : i == k - i (i.e. k even, i = k/2) is not a conflict: the pair must
#          use two DISTINCT elements and all elements are distinct, so k/2
#          alone is fine. Marking k - i == i as banned after we already took
#          i is harmless since i is never revisited.
#
#   NOTE : once i passes k every later integer is free, so the loop always
#          terminates after at most n + k steps.
#
# time = O(n + k), space = O(n + k)
class Solution(object):
    def minimumSum(self, n, k):
        banned = set()
        total = 0
        i = 1
        for _ in range(n):
            while i in banned:
                i += 1
            total += i
            banned.add(k - i)
            i += 1
        return total
