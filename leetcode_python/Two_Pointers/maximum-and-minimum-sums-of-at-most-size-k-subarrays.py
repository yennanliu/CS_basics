"""

3430. Maximum and Minimum Sums of at Most Size K Subarrays
Hard

You are given an integer array nums and a positive integer k. Return the sum of
the maximum and minimum elements of all subarrays with at most k elements.

Example 1:

Input: nums = [1,2,3], k = 2

Output: 20

Explanation:

The subarrays of nums with at most 2 elements are:

Subarray
Minimum
Maximum
Sum

[1]
1
1
2

[2]
2
2
4

[3]
3
3
6

[1, 2]
1
2
3

[2, 3]
2
3
5

Final Total

20

The output would be 20.

Example 2:

Input: nums = [1,-3,1], k = 2

Output: -6

Explanation:

The subarrays of nums with at most 2 elements are:

Subarray
Minimum
Maximum
Sum

[1]
1
1
2

[-3]
-3
-3
-6

[1]
1
1
2

[1, -3]
-3
1
-2

[-3, 1]
-3
1
-2

Final Total

-6

The output would be -6.

Constraints:

1 <= nums.length <= 80000
1 <= k <= nums.length
-10^6 <= nums[i] <= 10^6

"""

# V0
# IDEA : CONTRIBUTION BY MONOTONIC STACK, WITH A LATTICE-COUNT FOR THE LENGTH CAP
#
#   instead of enumerating subarrays, ask for each index i how many subarrays
#   have nums[i] as their maximum.  the answer is a rectangle of choices:
#   the left end may be anywhere after the previous element that is >= nums[i],
#   the right end anywhere before the next element that is > nums[i].  the
#   deliberate >= / > asymmetry hands every subarray of equal values to exactly
#   one index, so nothing is counted twice.  a monotonic stack finds both
#   boundaries in linear time; the minimum is the same computation with the
#   comparisons flipped.
#
#   the "at most k elements" cap turns the rectangle into a triangle-clipped
#   rectangle.  writing x = i - l and y = r - i, we need
#     0 <= x <= P, 0 <= y <= Q, x + y <= k - 1,
#   which is counted in O(1) by inclusion-exclusion on the three half-planes:
#     tri(K) - tri(K-P-1) - tri(K-Q-1) + tri(K-P-Q-2),   tri(t) = C(t+2, 2).
#
# time = O(n), space = O(n)
class Solution(object):
    def minMaxSubarraySum(self, nums, k):
        n = len(nums)
        K = k - 1

        def tri(t):
            if t < 0:
                return 0
            return (t + 1) * (t + 2) // 2

        def rect(P, Q):
            return (tri(K) - tri(K - P - 1) - tri(K - Q - 1)
                    + tri(K - P - Q - 2))

        def side(sign):
            # sum over i of nums[i] * (#subarrays where i is the extreme)
            st = []
            left = [0] * n
            for i in range(n):
                v = nums[i] * sign
                while st and nums[st[-1]] * sign < v:
                    st.pop()
                left[i] = st[-1] + 1 if st else 0
                st.append(i)
            st = []
            right = [0] * n
            for i in range(n - 1, -1, -1):
                v = nums[i] * sign
                while st and nums[st[-1]] * sign <= v:
                    st.pop()
                right[i] = st[-1] - 1 if st else n - 1
                st.append(i)
            tot = 0
            for i in range(n):
                tot += nums[i] * rect(i - left[i], right[i] - i)
            return tot

        return side(1) + side(-1)
