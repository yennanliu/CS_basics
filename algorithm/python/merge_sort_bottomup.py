#---------------------------------------------------------------
# Merge sort - bottom up
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

"""

Steps

Mergesort 演算法分為以下步驟：

step 1) Divide：將含有 n 個元素的序列分割成含有 n / 2 個子序列。
step 2) Conquer：排序分割後的兩個子序列。
step 3) Combine：合併排序完成的兩子序列，成為一個排好序的序列。


其中，Conquer 步驟中的「排序」可以不斷遞迴 Mergesort 自身，
因此需要停止遞迴的條件（base case），
我們將條件設定為「子序列的長度小於 2」，因為長度為 1 的序列可視為已完成排序。


Time complexity

- 分割子序列: O (logN)
- 合併 : O(N)

-> N log(N)

透過遞迴關係式，很容易計算 Mergesort 的時間複雜度。假設排序長度為 n
 的序列最多需要 T(n) 時間。
 可以觀察到，如果序列只有一個元素，Mergesort 僅需要常數時間就可以完成排序，寫成 T(n)=1

如果 n>2
，Mergesort 會將序列分為 ⌈n2⌉
 部分，以及 ⌊n2⌋
 部分。我們可以將排序前者寫成 T(⌈n2⌉)
，而後者花費時間為 T(⌊n2⌋)

最後，合併兩個子序列僅需 n
 個操作。可得下列遞迴關係式。
（為了方便計算，把 floor 和 ceil 捨去）
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