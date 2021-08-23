"""

The Hamming distance between two integers is the number of positions at which the corresponding bits are different.

Given an integer array nums, return the sum of Hamming distances between all the pairs of the integers in nums.

 

Example 1:

Input: nums = [4,14,2]
Output: 6
Explanation: In binary representation, the 4 is 0100, 14 is 1110, and 2 is 0010 (just
showing the four bits relevant in this case).
The answer will be:
HammingDistance(4, 14) + HammingDistance(4, 2) + HammingDistance(14, 2) = 2 + 2 + 2 = 6.
Example 2:

Input: nums = [4,14,4]
Output: 4
 

Constraints:

1 <= nums.length <= 104
0 <= nums[i] <= 109
The answer for the given input will fit in a 32-bit integer.

"""

# V0 -> TIME OUT ERROR
class Solution(object):
    def get_dis(self, a, b):
        if a == b:
            return 0
        r = 0
        tmp1 = bin(a)[2:]
        tmp2 = bin(b)[2:]
        _len = max(len(tmp1), len(tmp2))
        tmp1 = (_len - len(tmp1)) * '0' + tmp1
        tmp2 = (_len - len(tmp2)) * '0' + tmp2
        for i in range(_len):
            diff = abs(int(tmp1[i]) - int(tmp2[i]))
            r += diff
        return r
      
    def totalHammingDistance(self, nums):
        r = 0
        for i in range(len(nums)-1):
            for j in range(i+1, len(nums)):
                dis = self.get_dis(nums[i], nums[j])
                #print ( 'i = ' + str(i) + ' j = ' + str(j) + " nums[i] = " + str(nums[i]) + " nums[j] = " + str(nums[j]) + " dis = " + str(dis))
                r += dis
        return r

# V0'

# V1
# https://leetcode.com/problems/total-hamming-distance/discuss/96252/Python-O(nlogV)-time
class Solution(object):
    def totalHammingDistance(self, nums):
        ans = 0
        mask = 1
        for j in range(0, 32):
            ones = zeros = 0
            for num in nums:
                if num & mask:
                    ones += 1
                else:
                    zeros += 1
            ans += ones * zeros
            mask = mask << 1
        return ans

# V1'
# https://blog.csdn.net/fuxuemingzhu/article/details/79494653
class Solution(object):
    def totalHammingDistance(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        res = 0
        for pos in range(32):
            bitCount = 0
            for i in range(len(nums)):
                bitCount += (nums[i] >> pos) & 1
            res += bitCount * (len(nums) - bitCount)
        return res

# V1'
# http://bookshadow.com/weblog/2016/12/18/leetcode-total-hamming-distance/
class Solution(object):
    def totalHammingDistance(self, nums):
        ans = 0
        for x in range(32):
            mask = 1 << x
            zero = one = 0
            for num in nums:
                if num & mask:
                    one += 1
                else:
                    zero += 1
            ans += zero * one
        return ans
        
# V2 
# Time:  O(n)
# Space: O(1)
class Solution(object):
    def totalHammingDistance(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        result = 0
        for i in range(32):
            counts = [0] * 2
            for num in nums:
                counts[(num >> i) & 1] += 1
            result += counts[0] * counts[1]
        return result