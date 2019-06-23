


# V1 




# V2 
# Time:  O(n^2)
# Space: O(1)

class Solution(object):
    def triangleNumber(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        result = 0
        nums.sort()
        for i in range(len(nums)-2):
            if nums[i] == 0:
                continue
            k = i+2
            for j in range(i+1, len(nums)-1):
                while k < len(nums) and nums[i] + nums[j] > nums[k]:
                    k += 1
                result += k-j-1
        return result

