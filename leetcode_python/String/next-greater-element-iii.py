# V0 

# V1 
# http://bookshadow.com/weblog/2017/04/09/leetcode-next-greater-element-iii/
class Solution(object):
    def nextGreaterElement(self, n):
        """
        :type n: int
        :rtype: int
        """
        nums = list(str(n))
        size = len(nums)
        for x in range(size - 1, -1, -1):
            if nums[x - 1] < nums[x]:
                break
        if x > 0:
            for y in range(size - 1, -1, -1):
                if nums[y] > nums[x - 1]:
                    nums[x - 1], nums[y] = nums[y], nums[x - 1]
                    break
        for z in range((size - x) / 2):
            nums[x + z], nums[size - z - 1] = nums[size - z - 1], nums[x + z]
        ans = int(''.join(nums))
        return n < ans <= 0x7FFFFFFF and ans or -1
        
# V2 
# Time:  O(logn) = O(1)
# Space: O(logn) = O(1)
class Solution(object):
    def nextGreaterElement(self, n):
        """
        :type n: int
        :rtype: int
        """
        digits = map(int, list(str(n)))
        k, l = -1, 0
        for i in range(len(digits) - 1):
            if digits[i] < digits[i + 1]:
                k = i

        if k == -1:
            digits.reverse()
            return -1

        for i in range(k + 1, len(digits)):
            if digits[i] > digits[k]:
                l = i

        digits[k], digits[l] = digits[l], digits[k]
        digits[k + 1:] = digits[:k:-1]
        result = int("".join(map(str, digits)))
        return -1 if result >= 0x7FFFFFFF else result