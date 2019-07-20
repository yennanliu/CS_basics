# V0 

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/82902655
# IDEA : DP 
class Solution(object):
    def wiggleMaxLength(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        n = len(nums)
        if n <= 1:
            return n
        inc, dec = [1] * n, [1] * n
        for x in range(n):
            for y in range(x):
                if nums[x] > nums[y]:
                    inc[x] = max(inc[x], dec[y] + 1)
                elif nums[x] < nums[y]:
                    dec[x] = max(dec[x], inc[y] + 1)
        return max(inc[-1], dec[-1])

# V1' 
# https://blog.csdn.net/fuxuemingzhu/article/details/82902655
# IDEA : DP 
class Solution(object):
    def wiggleMaxLength(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        n = len(nums)
        if n <= 1:
            return n
        inc, dec = [1] * n, [1] * n
        for x in range(1, n):
            if nums[x] > nums[x - 1]:
                inc[x] = dec[x - 1] + 1
                dec[x] = dec[x - 1]
            elif nums[x] < nums[x - 1]:
                inc[x] = inc[x - 1]
                dec[x] = inc[x - 1] + 1
            else:
                inc[x] = inc[x - 1]
                dec[x] = dec[x - 1]
        return max(inc[-1], dec[-1])

# V1'' 
# https://blog.csdn.net/fuxuemingzhu/article/details/82902655
# IDEA : DP 
class Solution(object):
    def wiggleMaxLength(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        n = len(nums)
        if n <= 1:
            return n
        inc, dec = 1, 1
        for x in range(1, n):
            if nums[x] > nums[x - 1]:
                inc = dec + 1
            elif nums[x] < nums[x - 1]:
                dec = inc + 1
        return max(inc, dec)
       
# V2 
# Time:  O(n)
# Space: O(1)
class Solution(object):
    def wiggleMaxLength(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        if len(nums) < 2:
            return len(nums)

        length, up = 1, None

        for i in range(1, len(nums)):
            if nums[i - 1] < nums[i] and (up is None or up is False):
                length += 1
                up = True
            elif nums[i - 1] > nums[i] and (up is None or up is True):
                length += 1
                up = False

        return length