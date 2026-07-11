"""

189. Rotate Array
Medium

Given an array, rotate the array to the right by k steps, where k is non-negative.


Example 1:

Input: nums = [1,2,3,4,5,6,7], k = 3
Output: [5,6,7,1,2,3,4]
Explanation:
rotate 1 steps to the right: [7,1,2,3,4,5,6]
rotate 2 steps to the right: [6,7,1,2,3,4,5]
rotate 3 steps to the right: [5,6,7,1,2,3,4]
Example 2:

Input: nums = [-1,-100,3,99], k = 2
Output: [3,99,-1,-100]
Explanation: 
rotate 1 steps to the right: [99,-1,-100,3]
rotate 2 steps to the right: [3,99,-1,-100]
 

Constraints:

1 <= nums.length <= 105
-231 <= nums[i] <= 231 - 1
0 <= k <= 105
 

Follow up:

Try to come up with as many solutions as you can. There are at least three different ways to solve this problem.
Could you do it in-place with O(1) extra space?

"""

# V0
# IDEA: MATH (GPT)
"""
KEY IDEA:


(O(n) solution)


->

F(0) = Σ(i * nums[i])

total = Σ(nums)

F(k) = F(k-1) + total - n * nums[n-k]


"""
class Solution(object):
    def maxRotateFunction(self, nums):
        n = len(nums)

        if n == 1:
            return 0

        cache = [0] * n

        cache[0] = self.helper(nums)
        res = cache[0]

        total = sum(nums)

        for i in range(1, n):
            cache[i] = cache[i - 1] + total - n * nums[n - i]
            res = max(res, cache[i])

        return res

    def helper(self, nums):
        res = 0
        for i in range(len(nums)):
            res += i * nums[i]
        return res


# V0
# IDEA: BRUTE FORCE (TLE) (GPT)
class Solution(object):
    def maxRotateFunction(self, nums):
        if len(nums) == 1:
            return 0

        rotations = []

        n = len(nums)

        for _ in range(n):
            nums.insert(0, nums[-1])
            nums.pop()
            rotations.append(nums[:])

        res = float("-inf")

        for arr in rotations:
            res = max(res, self.helper(arr))

        return res

    def helper(self, x):
        res = 0
        for i in range(len(x)):
            res += i * x[i]
        return res


# V0-1
# IDEA: MATH (GEMINI)
class Solution(object):
    def maxRotateFunction(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        if len(nums) == 1:
            return 0
            
        n = len(nums)
        cache = [0] * n
        
        # Calculate F(0) using the helper
        cache[0] = self.helper(nums)
        
        # Initialize res to the first calculated sum, NOT 0
        res = cache[0]
        total = sum(nums)
        
        # Start loop from 1
        for i in range(1, n):
            cache[i] = cache[i-1] + total - n * nums[n - i]
            res = max(res, cache[i])
            
        return res
        
    def helper(self, x):
        res = 0
        for i in range(len(x)):
            # Access the integer from the list directly
            res += i * x[i]
        return res


# V0
# IDEA: SPLIT + MOD
# time = O(n)
# space = O(n)
class Solution(object):
    def rotate(self, nums, k):
        if not nums or len(nums) <= 1:
            return

        n = len(nums)
        """
        NOTE !!!!
        """
        k = k % n
        if k == 0:
            return

        # split
        """
        NOTE !!!!
        """
        tail = nums[n - k:]
        head = nums[:n - k]

        """
        NOTE !!!!
        """
        # write back in-place
        nums[:] = tail + head


# V0-0-1
# time = O(n)
# space = O(n)
class Solution(object):
    def rotate(self, nums, k):
        """
        Do not return anything, modify nums in-place instead.
        """
        if nums is None or len(nums) <= 1:
            return
            
        # Handle cases where k is greater than the length of the array
        n = len(nums)
        k = k % n
        
        if k == 0:
            return

        # CRITICAL FIX: Use nums[:] to overwrite the existing array elements in-place
        nums[:] = nums[n - k:] + nums[:n - k]


# V0
# IDEA : pop + insert (python 3)
# time = O(k * n)
# space = O(1)
class Solution(object):
    def rotate(self, nums, k):
        # edge case
        if not nums:
            return
        _len = len(nums)
        # optimize
        k = k % _len
        for i in range(k):
            tmp = nums.pop(-1)
            """
            NOTE !!! we need to user "insert" here, but not append 
            """
            #nums = [tmp] + nums # this one is WRONG
            nums.insert(0, tmp)
            #print("i = " + str(i) + " nums = " + str(nums))



# V0-1
# time = O(n)
# space = O(1)
class Solution(object):
    def rotate(self, nums, k):
        n = len(nums)
        k %= n

        def reverse(l, r):
            while l < r:
                nums[l], nums[r] = nums[r], nums[l]
                l += 1
                r -= 1

        reverse(0, n - 1)
        reverse(0, k - 1)
        reverse(k, n - 1)


# V0
# IDEA : pop + insert (python 3)
# time = O(k * n)
# space = O(1)
class Solution(object):
    def rotate(self, nums, k):
        _len = len(nums)
        k = k % _len
        while k > 0:
            tmp = nums.pop(-1)
            nums.insert(0, tmp)
            k -= 1

# V0'
# IDEA : SLICE (in place)
# time = O(n)
# space = O(n)
class Solution(object):
    def rotate(self, nums, k):
        # edge case
        if k == 0 or not nums or len(nums) == 1:
            return nums
        ### NOTE this
        k = k % len(nums)
        if k == 0:
            return nums
        """
        NOTE this !!!!
        """
        nums[:k], nums[k:] = nums[-k:], nums[:-k]
        return nums

# V0''
# IDEA : SLICE (in place)
# time = O(n)
# space = O(n)
class Solution(object):
    def rotate(self, nums, k):
        k = k % len(nums)
        nums[:k], nums[k:] = nums[-k:], nums[:len(nums)-k]

# V0'' : TODO : fix this
# class Solution(object):
#     def rotate(self, nums, k):
#         _nums = nums[:]
#         for i in range(k):
#             #print("nums = " + str(nums))
#             nums = [_nums[-1]] + _nums[:-1]
#         return nums

# V0''''
# IDEA : SLICE
# time = O(n)
# space = O(n)
class Solution(object):
    def rotate(self, nums, k):
        k = k % len(nums) # since the rotate operation is cyclic. i.e. if len(nums)=7, k=17 -> rotate(17) = rotate(17%7) = rotate(3)
        nums[:k], nums[k:] = nums[len(nums)-k:], nums[:len(nums)-k]

# V1
# https://blog.csdn.net/coder_orz/article/details/52052767
# IDEA : SLICE 
# time = O(n)
# space = O(n)
class Solution(object):
    def rotate(self, nums, k):
        """
        :type nums: List[int]
        :type k: int
        :rtype: void Do not return anything, modify nums in-place instead.
        """
        k = k % len(nums) # since the rotate operation is cyclic. i.e. if len(nums)=7, k=17 -> rotate(17) = rotate(17%7) = rotate(3)
        nums[:k], nums[k:] = nums[len(nums)-k:], nums[:len(nums)-k]
        # for test cases
        return nums

### Test case
s=Solution()
assert s.rotate([1,2,3,4,5,6,7], 3) == [5, 6, 7, 1, 2, 3, 4]
assert s.rotate([1,2,3,4,5,6,7], 4) == [4, 5, 6, 7, 1, 2, 3]
assert s.rotate([1,2,3,4,5,6,7], 5) == [3, 4, 5, 6, 7, 1, 2]
assert s.rotate([1], 0) == [1]
assert s.rotate([1], 1) == [1]
assert s.rotate([1], 2) == [1]

# V1'
# https://blog.csdn.net/coder_orz/article/details/52052767
# time = O(n)
# space = O(n)
class Solution(object):
    def rotate(self, nums, k):
        """
        :type nums: List[int]
        :type k: int
        :rtype: void Do not return anything, modify nums in-place instead.
        """
        old_nums = nums[:]
        for i in range(len(nums)):
            nums[(i + k) % len(nums)] = old_nums[i]

# V1''
# https://blog.csdn.net/coder_orz/article/details/52052767
# time = O(n)
# space = O(1)
class Solution(object):
    def rotate(self, nums, k):
        """
        :type nums: List[int]
        :type k: int
        :rtype: void Do not return anything, modify nums in-place instead.
        """
        k = k % len(nums)
        self.reversePart(nums, 0, len(nums)-k-1)
        self.reversePart(nums, len(nums)-k, len(nums)-1)
        self.reversePart(nums, 0, len(nums)-1)

    def reversePart(self, nums, start, end):
        while start < end:
            nums[start], nums[end] = nums[end], nums[start]
            start, end = start+1, end-1

# V1'''
# https://blog.csdn.net/coder_orz/article/details/52052767
# time = O(n)
# space = O(1)
class Solution(object):
    def rotate(self, nums, k):
        """
        :type nums: List[int]
        :type k: int
        :rtype: void Do not return anything, modify nums in-place instead.
        """
        k, start, n = k % len(nums), 0, len(nums)
        while k % n != 0 and n > 0:
            for i in range(k):
                nums[start + i], nums[len(nums) - k + i] = nums[len(nums) - k + i], nums[start + i]
            start, n = start + k, n - k
            k = k % n

# V2
# time = O(k * n)
# space = O(n)
class Solution:
    def rotate(self, nums, k):
        for epoch in range(k):
            nums = [nums[-1]] + nums
            nums = nums[:-1]
        return nums 

# V3
# time = O(n)
# space = O(1)
class Solution(object):
    """
    :type nums: List[int]
    :type k: int
    :rtype: void Do not return anything, modify nums in-place instead.
    """

    def rotate(self, nums, k):
        k %= len(nums)
        self.reverse(nums, 0, len(nums))
        self.reverse(nums, 0, k)
        self.reverse(nums, k, len(nums))

    def reverse(self, nums, start, end):
        while start < end:
            nums[start], nums[end - 1] = nums[end - 1], nums[start]
            start += 1
            end -= 1

# time = O(n)
# space = O(1)
from fractions import gcd
class Solution2(object):
    """
    :type nums: List[int]
    :type k: int
    :rtype: void Do not return anything, modify nums in-place instead.
    """

    def rotate(self, nums, k):
        def apply_cycle_permutation(k, offset, cycle_len, nums):
            tmp = nums[offset]
            for i in range(1, cycle_len):
                nums[(offset + i * k) % len(nums)], tmp = tmp, nums[(offset + i * k) % len(nums)]
            nums[offset] = tmp

        k %= len(nums)
        num_cycles = gcd(len(nums), k)
        cycle_len = len(nums) / num_cycles
        for i in range(num_cycles):
            apply_cycle_permutation(k, i, cycle_len, nums)


# time = O(n)
# space = O(1)
class Solution3(object):
    """
    :type nums: List[int]
    :type k: int
    :rtype: void Do not return anything, modify nums in-place instead.
    """

    def rotate(self, nums, k):
        count = 0
        start = 0
        while count < len(nums):
            curr = start
            prev = nums[curr]
            while True:
                idx = (curr + k) % len(nums)
                nums[idx], prev = prev, nums[idx]
                curr = idx
                count += 1
                if start == curr:
                    break
            start += 1

# time = O(n)
# space = O(n)
class Solution4(object):
    """
    :type nums: List[int]
    :type k: int
    :rtype: void Do not return anything, modify nums in-place instead.
    """
    def rotate(self, nums, k):
        """
        :type nums: List[int]
        :type k: int
        :rtype: void Do not return anything, modify nums in-place instead.
        """
        nums[:] = nums[len(nums) - k:] + nums[:len(nums) - k]

# time = O(k * n)
# space = O(1)
class Solution5(object):
    """
    :type nums: List[int]
    :type k: int
    :rtype: void Do not return anything, modify nums in-place instead.
    """
    def rotate(self, nums, k):
        while k > 0:
            nums.insert(0, nums.pop())
            k -= 1