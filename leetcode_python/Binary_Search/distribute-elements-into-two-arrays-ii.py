"""

3072. Distribute Elements Into Two Arrays II
Hard

You are given a 1-indexed array of integers nums of length n.

We define a function greaterCount such that greaterCount(arr, val) returns the number of elements in arr that are strictly greater than val.

You need to distribute all the elements of nums between two arrays arr1 and arr2 using n operations. In the first operation, append nums[1] to arr1. In the second operation, append nums[2] to arr2. Afterwards, in the ith operation:

If greaterCount(arr1, nums[i]) > greaterCount(arr2, nums[i]), append nums[i] to arr1.
If greaterCount(arr1, nums[i]) < greaterCount(arr2, nums[i]), append nums[i] to arr2.
If greaterCount(arr1, nums[i]) == greaterCount(arr2, nums[i]), append nums[i] to the array with a lesser number of elements.
If there is still a tie, append nums[i] to arr1.

The array result is formed by concatenating the arrays arr1 and arr2. For example, if arr1 == [1,2,3] and arr2 == [4,5,6], then result = [1,2,3,4,5,6].

Return the integer array result.


Example 1:

Input: nums = [2,1,3,3]
Output: [2,3,1,3]
Explanation: After the first 2 operations, arr1 = [2] and arr2 = [1].
In the 3rd operation, the number of elements greater than 3 is zero in both arrays. Also, the lengths are equal, hence, append nums[3] to arr1.
In the 4th operation, the number of elements greater than 3 is zero in both arrays. As the length of arr2 is lesser, hence, append nums[4] to arr2.
After 4 operations, arr1 = [2,3] and arr2 = [1,3].
Hence, the array result formed by concatenation is [2,3,1,3].

Example 2:

Input: nums = [5,14,3,1,2]
Output: [5,3,1,2,14]
Explanation: After the first 2 operations, arr1 = [5] and arr2 = [14].
In the 3rd operation, the number of elements greater than 3 is one in both arrays. Also, the lengths are equal, hence, append nums[3] to arr1.
In the 4th operation, the number of elements greater than 1 is greater in arr1 than arr2 (2 > 1). Hence, append nums[4] to arr1.
In the 5th operation, the number of elements greater than 2 is greater in arr1 than arr2 (2 > 1). Hence, append nums[5] to arr1.
After 5 operations, arr1 = [5,3,1,2] and arr2 = [14].
Hence, the array result formed by concatenation is [5,3,1,2,14].

Example 3:

Input: nums = [3,3,3,3]
Output: [3,3,3,3]
Explanation: At the end of 4 operations, arr1 = [3,3] and arr2 = [3,3].
Hence, the array result formed by concatenation is [3,3,3,3].


Constraints:

3 <= n <= 10^5
1 <= nums[i] <= 10^9

"""

# V0
# IDEA : TWO FENWICK TREES OVER THE COMPRESSED VALUES
#
#   the rule needs greaterCount(arr, v) after every append, which a linear
#   scan would answer in O(n) per step — 10^10 in total. a Fenwick tree per
#   array answers it in O(log n) instead :
#
#       greaterCount(arr, v) = size(arr) - (how many entries are <= v)
#
#   values reach 10^9 but there are only n of them, so compress them to
#   ranks 1..n first and index the trees by rank.
#
#   the tie-breaks are then just the sizes, and finally arr1 + arr2.
#
# time = O(n log n), space = O(n)
class Solution(object):
    def resultArray(self, nums):
        order = sorted(set(nums))
        rank = {v: i + 1 for i, v in enumerate(order)}
        size = len(order)

        class BIT(object):
            def __init__(self, n):
                self.n = n
                self.t = [0] * (n + 1)

            def add(self, i):
                while i <= self.n:
                    self.t[i] += 1
                    i += i & -i

            def query(self, i):          # count of entries with rank <= i
                s = 0
                while i > 0:
                    s += self.t[i]
                    i -= i & -i
                return s

        bit1, bit2 = BIT(size), BIT(size)
        arr1, arr2 = [nums[0]], [nums[1]]
        bit1.add(rank[nums[0]])
        bit2.add(rank[nums[1]])

        for x in nums[2:]:
            r = rank[x]
            g1 = len(arr1) - bit1.query(r)
            g2 = len(arr2) - bit2.query(r)
            if g1 > g2 or (g1 == g2 and len(arr1) <= len(arr2)):
                arr1.append(x)
                bit1.add(r)
            else:
                arr2.append(x)
                bit2.add(r)

        return arr1 + arr2
