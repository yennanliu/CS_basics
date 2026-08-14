"""

1998. GCD Sort of an Array
Hard

You are given an integer array nums, and you can perform the following operation any number of times on nums:

Swap the positions of two elements nums[i] and nums[j] if gcd(nums[i], nums[j]) > 1 where gcd(nums[i], nums[j]) is the greatest common divisor of nums[i] and nums[j].

Return true if it is possible to sort nums in non-decreasing order using the above swap method, or false otherwise.


Example 1:

Input: nums = [7,21,3]
Output: true
Explanation: We can sort [7,21,3] by performing the following operations:
- Swap 7 and 21 because gcd(7,21) = 7. nums = [21,7,3]
- Swap 21 and 3 because gcd(21,3) = 3. nums = [3,7,21]

Example 2:

Input: nums = [5,2,6,2]
Output: false
Explanation: It is impossible to sort the array because 5 cannot be swapped with any other element.

Example 3:

Input: nums = [10,5,9,3,15]
Output: true
We can sort [10,5,9,3,15] by performing the following operations:
- Swap 10 and 15 because gcd(10,15) = 5. nums = [15,5,9,3,10]
- Swap 15 and 3 because gcd(15,3) = 3. nums = [3,5,9,15,10]
- Swap 10 and 15 because gcd(10,15) = 5. nums = [3,5,9,10,15]


Constraints:

1 <= nums.length <= 3 * 10^4
2 <= nums[i] <= 10^5

"""

# V0
# IDEA : SIEVE OF SMALLEST PRIME FACTOR + UNION FIND ON VALUES
#
#   two values can swap iff they share a prime factor; swaps compose, so the
#   values split into connected components and inside a component ANY
#   permutation is reachable (a connected swap graph generates all of S_k).
#
#   build the components without testing gcd pairwise :
#     - sieve the smallest prime factor up to max(nums)
#     - for each value v, union(v, p) for every prime p dividing v
#     -> two values sharing a prime land in the same component
#
#   then compare nums with sorted(nums) position by position : a mismatch is
#   only fixable when the two values sit in the same component.
#
#   NOTE : union-find is over VALUES (0..max), not indices.
#   NOTE : find() is iterative - values reach 10^5 and a recursive find can
#          hit python's recursion limit on a long chain.
#
# time = O(M log log M + n log M * alpha), M = max(nums), space = O(M)
class Solution(object):
    def gcdSort(self, nums):
        mx = max(nums)

        # smallest prime factor sieve
        spf = list(range(mx + 1))
        i = 2
        while i * i <= mx:
            if spf[i] == i:                     # i is prime
                for j in range(i * i, mx + 1, i):
                    if spf[j] == j:
                        spf[j] = i
            i += 1

        parent = list(range(mx + 1))

        def find(x):
            root = x
            while parent[root] != root:
                root = parent[root]
            while parent[x] != root:
                parent[x], x = root, parent[x]
            return root

        def union(a, b):
            ra, rb = find(a), find(b)
            if ra != rb:
                parent[rb] = ra

        for v in nums:
            x = v
            while x > 1:
                p = spf[x]
                union(v, p)
                while x % p == 0:
                    x //= p

        target = sorted(nums)
        for i in range(len(nums)):
            if nums[i] != target[i] and find(nums[i]) != find(target[i]):
                return False
        return True
