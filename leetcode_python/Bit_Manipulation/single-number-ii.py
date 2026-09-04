"""

137. Single Number II
Solved
Medium
Topics
premium lock icon
Companies
Given an integer array nums where every element appears three times except for one, which appears exactly once. Find the single element and return it.

You must implement a solution with a linear runtime complexity and use only constant extra space.

 

Example 1:

Input: nums = [2,2,3,2]
Output: 3
Example 2:

Input: nums = [0,1,0,1,0,1,99]
Output: 99
 

Constraints:

1 <= nums.length <= 3 * 104
-231 <= nums[i] <= 231 - 1
Each element in nums appears exactly three times except for one element which appears once.
 


"""


# V0
class Solution(object):
    def singleNumber(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        pass


# V0-1
# time = O(n)
# space = O(n)  # set(nums)
class Solution:
    def singleNumber(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        # for [a,a,b,a] array
        # 3*(a+b) - (a+a+b+a) = 2b
        return int((3*(sum(set(nums))) - sum(nums))//2)


# V0-2
# IDEA: BIT OP (gemini)
class Solution(object):
    def singleNumber(self, nums):
        res = 0
        
        # 1. 遍歷 32 位元整數的每一個 Bit (位置 0 到 31)
        for i in range(32):
            bit_sum = 0
            
            # 2. 統計目前陣列中所有數字在第 i 位元上有多少個 1
            for n in nums:
                bit_sum += (n >> i) & 1  # (n >> i) & 1 用於提取 n 的第 i 個 bit
            
            # 3. 對 3 取餘數，過濾掉出現 3 次的數字的影響
            bit = bit_sum % 3
            
            # 4. 若餘數為 1，將該 Bit 設定到結果 res 的第 i 位上
            if bit:
                res |= (1 << i)
                
        # 5. 處理 Python 特有的 32 位有號整數（Signed Integer）補碼溢位問題
        if res >= 2**31:
            res -= 2**32
            
        return res

# V0-3
# IDEA: BIT OP
class Solution(object):
    def singleNumber(self, nums):
        res = 0
        for i in range(32):
            bit_sum = 0
            for n in nums:
                bit_sum += (n >> i) & 1        # count 1s at position i
            bit = bit_sum % 3                  # 0 or 1 -> the unique number's bit
            if bit:
                res |= (1 << i)
        # handle negative numbers (Python ints are unbounded)
        if res >= 2**31:
            res -= 2**32
        return res


# V1
# https://blog.csdn.net/fuxuemingzhu/article/details/79554959
# time = O(32 * n) = O(n)
# space = O(1)
class Solution(object):
    def singleNumber(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        res = 0
        for i in range(32):
            cnt = 0
            mask = 1 << i
            for num in nums:
                if num & mask:
                    cnt += 1
            if cnt % 3 == 1:
                res |= mask
        if res >= 2 ** 31:
            res -= 2 ** 32
        return res

# V1'
# time = O(n)
# space = O(n)  # set(nums)
class Solution:
    def singleNumber(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        # for [a,a,b,a] array
        # 3*(a+b) - (a+a+b+a) = 2b
        return int((3*(sum(set(nums))) - sum(nums))/2)
 
# V2
# time = O(n)
# space = O(n)
import collections
class Solution3(object):
    def singleNumber(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        return list((collections.Counter(list(set(nums)) * 3) - collections.Counter(nums)).keys())[0]
