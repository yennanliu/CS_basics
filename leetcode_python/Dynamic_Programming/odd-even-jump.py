"""

975. Odd Even Jump
Hard

You are given an integer array arr. From some starting index, you can make a series of jumps. The (1st, 3rd, 5th, ...) jumps in the series are called odd-numbered jumps, and the (2nd, 4th, 6th, ...) jumps in the series are called even-numbered jumps. Note that the jumps are numbered, not the indices.

You may jump forward from index i to index j (with i < j) in the following way:

During odd-numbered jumps (i.e., jumps 1, 3, 5, ...), you jump to the index j such that arr[i] <= arr[j] and arr[j] is the smallest possible value. If there are multiple such indices j, you can only jump to the smallest such index j.
During even-numbered jumps (i.e., jumps 2, 4, 6, ...), you jump to the index j such that arr[i] >= arr[j] and arr[j] is the largest possible value. If there are multiple such indices j, you can only jump to the smallest such index j.
It may be the case that for some index i, there are no legal jumps.

A starting index is good if, starting from that index, you can reach the end of the array (index arr.length - 1) by jumping some number of times (possibly 0 or more than once).

Return the number of good starting indices.

Example 1:

Input: arr = [10,13,12,14,15]
Output: 2
Explanation:
From starting index i = 0, we can make our 1st jump to i = 2, then we cannot jump any more.
From starting index i = 1 and i = 2, we can make our 1st jump to i = 3, then we cannot jump any more.
From starting index i = 3, we can make our 1st jump to i = 4, so we have reached the end.
From starting index i = 4, we have reached the end already.
In total, there are 2 different starting indices i = 3 and i = 4.

Example 2:

Input: arr = [2,3,1,1,4]
Output: 3
Explanation: The good starting indices are i = 1, i = 3, and i = 4.

Example 3:

Input: arr = [5,1,3,4,2]
Output: 3
Explanation: We can reach the end from starting indices 1, 2, and 4.

Constraints:

1 <= arr.length <= 2 * 10^4
0 <= arr[i] < 10^5

"""

# V0
# IDEA : SORTING + MONOTONIC STACK (precompute jump targets) + BACKWARD DP
#
#  Step 1 - precompute, for every index i:
#     odd_next[i]  = the index we land on with an odd  jump (or None)
#     even_next[i] = the index we land on with an even jump (or None)
#
#     Trick: sort indices by (value, index) ascending. Walking that order and
#     keeping a monotonic stack of indices, whenever the incoming index is
#     LARGER than the stack top, the incoming index is exactly the "next
#     greater-or-equal value at a later position" for the popped index.
#     Sorting by (-value, index) gives the even jumps the same way.
#
#  Step 2 - backward DP:
#     odd[i]  = True if starting at i and making an ODD  jump we can reach the end
#     even[i] = True if starting at i and making an EVEN jump we can reach the end
#     odd[i]  = even[odd_next[i]]
#     even[i] = odd[even_next[i]]
#     odd[n-1] = even[n-1] = True
#
#  answer = number of i with odd[i] True (the 1st jump is always odd)
#
# time = O(n log n)
# space = O(n)
class Solution(object):
    def oddEvenJumps(self, arr):
        n = len(arr)

        def next_target(sorted_indices):
            # for each index, the next index to its right that comes
            # right after it in this sorted order
            res = [None] * n
            stack = []
            for i in sorted_indices:
                while stack and i > stack[-1]:
                    res[stack.pop()] = i
                stack.append(i)
            return res

        # odd jump  : smallest value >= arr[i], ties -> smallest index
        odd_next = next_target(sorted(range(n), key=lambda i: (arr[i], i)))
        # even jump : largest value <= arr[i], ties -> smallest index
        even_next = next_target(sorted(range(n), key=lambda i: (-arr[i], i)))

        odd = [False] * n
        even = [False] * n
        odd[n - 1] = even[n - 1] = True

        for i in range(n - 2, -1, -1):
            if odd_next[i] is not None:
                odd[i] = even[odd_next[i]]
            if even_next[i] is not None:
                even[i] = odd[even_next[i]]

        return sum(odd)
