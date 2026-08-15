"""

2122. Recover the Original Array
Hard

Alice had a 0-indexed array arr consisting of n positive integers. She chose an arbitrary positive integer k and created two new 0-indexed integer arrays lower and higher in the following manner:

lower[i] = arr[i] - k, for every index i where 0 <= i < n
higher[i] = arr[i] + k, for every index i where 0 <= i < n

Unfortunately, Alice lost all three arrays. However, she remembers the integers that were present in the arrays lower and higher, but not the array each integer belonged to. Help Alice and recover the original array.

Given an array nums consisting of 2n integers, where exactly n of the integers were present in lower and the remaining in higher, return the original array arr. In case the answer is not unique, return any valid array.

Note: The test cases are generated such that there exists at least one valid array arr.


Example 1:

Input: nums = [2,10,6,4,8,12]
Output: [3,7,11]
Explanation:
If arr = [3,7,11] and k = 1, we get lower = [2,6,10] and higher = [4,8,12].
Combining lower and higher gives us [2,6,10,4,8,12], which is a permutation of nums.
Another valid possibility is that arr = [5,7,9] and k = 3. In that case, lower = [2,4,6] and higher = [8,10,12].

Example 2:

Input: nums = [1,1,3,3]
Output: [2,2]
Explanation:
If arr = [2,2] and k = 1, we get lower = [1,1] and higher = [3,3].
Combining lower and higher gives us [1,1,3,3], which is equal to nums.
Note that arr cannot be [1,3] and k = 0, as the values in arr must be positive.

Example 3:

Input: nums = [5,435]
Output: [220]
Explanation:
The only possible combination is arr = [220] and k = 215. Using them, we get lower = [5] and higher = [435].


Constraints:

2 * n == nums.length
1 <= n <= 1000
1 <= nums[i] <= 10^9
The test cases are generated such that there exists at least one valid array arr.

"""

# V0
# IDEA : GUESS 2k FROM THE SMALLEST ELEMENT, THEN GREEDILY PAIR
#
#   sort nums. the smallest element must come from `lower` (nothing can be
#   below it), so it pairs with SOME later element, and that pairing fixes
#   2k = nums[j] - nums[0].  only n candidates for j, and 2k must be
#   positive and even.
#
#   for a fixed k, the pairing is forced : walk the sorted array, and the
#   first unused value x must be a `lower` value, so x + 2k has to be
#   available to pair with it. a Counter of the remaining values makes each
#   check O(1), and arr[i] = x + k.
#
#   the first k that consumes every element cleanly is an answer.
#
# time = O(n^2), space = O(n)
from collections import Counter


class Solution(object):
    def recoverArray(self, nums):
        nums = sorted(nums)
        n = len(nums)

        for j in range(1, n):
            diff = nums[j] - nums[0]
            if diff <= 0 or diff % 2:
                continue
            k2 = diff                       # = 2k
            cnt = Counter(nums)
            arr = []
            ok = True
            for x in nums:
                if cnt[x] == 0:
                    continue
                if cnt[x + k2] == 0:
                    ok = False
                    break
                cnt[x] -= 1
                cnt[x + k2] -= 1
                arr.append(x + k2 // 2)
            if ok and len(arr) == n // 2:
                return arr
        return []
