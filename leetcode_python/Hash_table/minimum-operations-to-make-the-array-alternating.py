"""

2170. Minimum Operations to Make the Array Alternating
Medium

You are given a 0-indexed array nums consisting of n positive integers.

The array nums is called alternating if:

nums[i - 2] == nums[i], where 2 <= i <= n - 1.
nums[i - 1] != nums[i], where 1 <= i <= n - 1.

In one operation, you can choose an index i and change nums[i] into any positive integer.

Return the minimum number of operations required to make the array alternating.


Example 1:

Input: nums = [3,1,3,2,4,3]
Output: 3
Explanation:
One way to make the array alternating is by converting it to [3,1,3,1,3,1].
The number of operations required in this case is 3.
It can be proven that it is not possible to make the array alternating in less than 3 operations.

Example 2:

Input: nums = [1,2,2,2,2]
Output: 2
Explanation:
One way to make the array alternating is by converting it to [1,2,1,2,1].
The number of operations required in this case is 2.
Note that the array cannot be converted to [2,2,2,2,2] because in this case nums[0] == nums[1] which violates the constraint that nums[i - 1] != nums[i].


Constraints:

1 <= nums.length <= 10^5
1 <= nums[i] <= 10^5

"""

# V0
# IDEA : PICK ONE VALUE FOR THE EVEN SLOTS AND ANOTHER FOR THE ODD SLOTS
#
#   after the rewrite the array looks like a, b, a, b, ... with a != b. so
#   only two values are chosen, and the cost is
#       (even slots not already a) + (odd slots not already b)
#
#   count the values at even and at odd indices separately, and keep each
#   side's TOP TWO most frequent values. usually the two winners differ and
#   the answer uses both; if they collide, the best is the better of
#       (even 1st, odd 2nd)  and  (even 2nd, odd 1st)
#   which is why the runner-up must be tracked.
#
# time = O(n), space = O(n)
from collections import Counter


class Solution(object):
    def minimumOperations(self, nums):
        n = len(nums)
        even = Counter(nums[0::2])
        odd = Counter(nums[1::2])
        n_even = (n + 1) // 2
        n_odd = n // 2

        def top2(cnt):
            # (value, count) for the two most frequent, padded with a dummy
            best = cnt.most_common(2)
            while len(best) < 2:
                best.append((0, 0))     # 0 is not a valid nums value
            return best

        (e1, ec1), (e2, ec2) = top2(even)
        (o1, oc1), (o2, oc2) = top2(odd)

        if e1 != o1:
            return (n_even - ec1) + (n_odd - oc1)
        return min((n_even - ec1) + (n_odd - oc2),
                   (n_even - ec2) + (n_odd - oc1))
