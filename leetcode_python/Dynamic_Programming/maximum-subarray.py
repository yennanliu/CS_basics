# V0 : DEV 

# V1 
# https://blog.csdn.net/hyperbolechi/article/details/43038749
def Solution_brutef(arr):
    maxval=-10000  
    for i in range(len(arr)):
        for j in range(i,len(arr)):
            if maxval<sum(arr[i:j]):
                print((i,j))
                maxval=max(maxval,sum(arr[i:j]))
                result=arr[i:j]
                
# V1' 
def Solution_findmax(arr):
    cursum=0
    maxval=arr[0]
    for index in range(len(arr)):
        if cursum<0:
            cursum=0
        cursum+=arr[index]
        maxval=max(maxval,cursum)

# V2 
# Time:  O(n)
# Space: O(1)
class Solution(object):
    def maxSubArray(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        max_nums = max(nums)
        if max_nums < 0:
            return max_nums
        global_max, local_max = 0, 0
        for x in nums:
            local_max = max(0, local_max + x)
            global_max = max(global_max, local_max)
        return global_max
