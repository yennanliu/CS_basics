#---------------------------------------------------------------
# MERGE SORT - bottomup
#---------------------------------------------------------------

# https://leetcode.com/explore/learn/card/recursion-ii/470/divide-and-conquer/2868/
# https://leetcode.com/problems/sort-an-array/discuss/568255/Python-Merge-Sort
# https://rust-algo.club/sorting/mergesort/

"""

Time complexity

Best : O(N Log N)
Avg : O(N Log N)
Worst : O(N Log N)`


Space complexity

O(N)
"""

class Solution:
    def sortArray(self, nums: List[int]) -> List[int]:
        self.mergeSort(nums)
        return nums
    
    def mergeSort(self, nums: List[int]) -> None:
        if len(nums) > 1:
            mid = len(nums) // 2
            L, R = nums[:mid], nums[mid:]

            self.mergeSort(L)
            self.mergeSort(R)

            i = j = k = 0

            while i < len(L) and j < len(R):
                if L[i] < R[j]:
                    nums[k] = L[i]
                    i += 1
                else:
                    nums[k] = R[j]
                    j += 1
                k += 1

            while i < len(L):
                nums[k] = L[i]
                i += 1
                k += 1

            while j < len(R):
                nums[k] = R[j]
                j += 1
                k += 1