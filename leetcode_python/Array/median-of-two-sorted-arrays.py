"""

4. Median of Two Sorted Arrays
Hard


Given two sorted arrays nums1 and nums2 of size m and n respectively, return the median of the two sorted arrays.

The overall run time complexity should be O(log (m+n)).

 

Example 1:

Input: nums1 = [1,3], nums2 = [2]
Output: 2.00000
Explanation: merged array = [1,2,3] and median is 2.
Example 2:

Input: nums1 = [1,2], nums2 = [3,4]
Output: 2.50000
Explanation: merged array = [1,2,3,4] and median is (2 + 3) / 2 = 2.5.
 

Constraints:

nums1.length == m
nums2.length == n
0 <= m <= 1000
0 <= n <= 1000
1 <= m + n <= 2000
-106 <= nums1[i], nums2[i] <= 106

"""

# V0
class Solution(object):
    def findMedianSortedArrays(self, nums1, nums2):
        """
        :type nums1: List[int]
        :type nums2: List[int]
        :rtype: float
        """
        pass
     


# V0-1
# IDEA: binary search on the smaller array (gemini)
"""
CORE IDEA:

### 🛠️ 修正後的 Python 程式碼 (分割線二分搜尋)

對於 LeetCode 004，最優解法是在較短的陣列上進行**分割線（Partition）的二分搜尋**：

->

不是在找 median。

    -> 是在找一個 `partition`，讓`左邊剛好是一半，而且左邊所有東西都 ≤ 右邊所有東西`。

        -> 一旦找到這個 partition，median 就只是從 4 個 boundary values 裡取出來。


---

Steps:

        1. Binary search smaller array

        2. Cut nums1
                ↓
           partition1

        3. Calculate nums2 cut
                ↓
           partition2 = half - partition1

        4. Check:
           left1 <= right2
           left2 <= right1

        5. Valid partition:
           odd  → max(left1, left2)
           even → (max(left1,left2) + min(right1,right2)) / 2


---

### 💡 核心分割線機制 (Partitioning)

將兩個陣列各自切一刀，使「左半邊元素總和」等於「右半邊元素總和」：

```text
nums1:  [  ...  | nums1[i]  ...  ]   (nums1_left | nums1_right)
nums2:  [  ...  | nums2[j]  ...  ]   (nums2_left | nums2_right)

```

只要滿足以下兩個條件，代表分割線位置完美：

1. **`nums1_left <= nums2_right`**
2. **`nums2_left <= nums1_right`**

---

### ⏱️ 複雜度分析

* **時間複雜度**：O(log(min(m, n)) — 僅在較短的陣列長度內做二分搜尋。
* **空間複雜度**：O(1) — 僅使用常數級指標，無任何額外陣列開銷。


"""
class Solution(object):

    def findMedianSortedArrays(self, nums1, nums2):
        """
        :type nums1: List[int]

        :type target: List[int]

        :rtype: float
        """
        # 1. 確保 nums1 是較短的陣列，確保時間複雜度為 O(log(min(M, N)))
        if len(nums1) > len(nums2):
            nums1, nums2 = nums2, nums1

        m, n = len(nums1), len(nums2)
        l, r = 0, m
        half_len = (m + n + 1) // 2  # 左半部分包含的元素個數

        while l <= r:
            i = l + (r - l) // 2  # nums1 的分割點
            j = half_len - i  # nums2 的分割點

            # 2. 定義分割線左右兩側的 4 個關鍵數值（使用 +-inf 處理邊界越界）
            nums1_left = nums1[i - 1] if i > 0 else float("-inf")
            nums1_right = nums1[i] if i < m else float("inf")

            nums2_left = nums2[j - 1] if j > 0 else float("-inf")
            nums2_right = nums2[j] if j < n else float("inf")

            # 3. 檢查分割線是否合法
            if nums1_left <= nums2_right and nums2_left <= nums1_right:
                # 總數為奇數：中位數即為左半部的最大值
                if (m + n) % 2 == 1:
                    return float(max(nums1_left, nums2_left))
                # 總數為偶數：中位數為 (左半部最大值 + 右半部最小值) / 2.0
                else:
                    return (
                        max(nums1_left, nums2_left)
                        + min(nums1_right, nums2_right)
                    ) / 2.0

            elif nums1_left > nums2_right:
                # nums1 分割點太靠右，需左移
                r = i - 1
            else:
                # nums1 分割點太靠左，需右移
                l = i + 1

        return 0.0


# V0-2
# IDEA: binary search on the smaller array (GPT)
class Solution(object):
    def findMedianSortedArrays(self, nums1, nums2):
        """
        :type nums1: List[int]
        :type nums2: List[int]
        :rtype: float
        """

        # Always binary search the smaller array
        if len(nums1) > len(nums2):
            nums1, nums2 = nums2, nums1

        m = len(nums1)
        n = len(nums2)

        left = 0
        right = m

        while left <= right:
            # Partition nums1
            partition1 = left + (right - left) // 2

            # Partition nums2
            partition2 = (m + n + 1) // 2 - partition1

            # Boundary values
            left1 = float("-inf") if partition1 == 0 else nums1[partition1 - 1]
            right1 = float("inf") if partition1 == m else nums1[partition1]

            left2 = float("-inf") if partition2 == 0 else nums2[partition2 - 1]
            right2 = float("inf") if partition2 == n else nums2[partition2]

            # Correct partition
            if left1 <= right2 and left2 <= right1:

                # Odd total length
                if (m + n) % 2 == 1:
                    return float(max(left1, left2))

                # Even total length
                left_max = max(left1, left2)
                right_min = min(right1, right2)

                return (left_max + right_min) / 2.0

            # nums1 partition is too far right
            elif left1 > right2:
                right = partition1 - 1

            # nums1 partition is too far left
            else:
                left = partition1 + 1

        return 0.0


# V0-3
# IDEA: ARRAY OP + mid (TLE) (gpt)
# NOTE !!! this code works, but the time complexity exceed.
class Solution(object):
    def findMedianSortedArrays(self, nums1, nums2):
        """
        :type nums1: List[int]
        :type nums2: List[int]
        :rtype: float
        """

        # Merge two arrays
        tmp = []
        tmp += nums1
        tmp += nums2

        # Sort: small -> big
        tmp.sort()

        size = len(tmp)
        mid = size // 2

        # Odd length
        if size % 2 == 1:
            return float(tmp[mid])

        # Even length
        left = tmp[mid - 1]
        right = tmp[mid]

        return float((left + right) / 2.0)


# V0-5
# IDEA : heapq
# time = O(m+n)
# space = O(m+n)
import heapq
class Solution:
    def findMedianSortedArrays(self, nums1, nums2):
        final = heapq.merge(nums1,nums2)
        final = list(final)
        if len(final)%2 != 0:
            return final[len(final)//2]
        else:
            left = int(len(final)/2) - 1
            right = int(len(final)/2)
            return (final[left]+final[right])/2.0

# V1
# IDEA : medium definiton
# https://leetcode.com/problems/median-of-two-sorted-arrays/discuss/520489/Simple-Python-solution
# IDEA 
# This very simple Python solution beats 94% (speed) and 100% (memory).
# Merge both arrays and sort the new array.
# If the length is odd, return the number in the middle of the array.
# If the length is even, return the mean between the two numbers in the middle.
# time = O((m+n)log(m+n))
# space = O(m+n)
class Solution:
    def findMedianSortedArrays(self, nums1, nums2):
        nums = nums1 + nums2
        nums.sort()
        
        L = len(nums)
        if L==1:
            return nums[0]
        if L % 2 != 0:
            return nums[L//2]
        else:
            return (nums[L//2 - 1] + nums[L//2])/2.0 # NOTE this !!!


# V1
# IDEA : heapq
# https://leetcode.com/problems/median-of-two-sorted-arrays/discuss/1373385/Python-3-Easy-With-explanation-Heaps
# IDEA :
#  -> We use the property of heaps to our advantage for this problem. We can easily get a merged sorted list using heaps, once we have this it is simple arithmetic.
#  -> If the length of the merged list is even, then the median is the avg of the two middle numbers. If the length of the merged list is odd, then the median will be the middle number.
#  -> building a heap is O(Nlog(n)) (n is number of elements in the array).
# time = O(m+n)
# space = O(m+n)
import heapq
class Solution:
    def findMedianSortedArrays(self, nums1, nums2):
        final = heapq.merge(nums1,nums2)
        final = list(final)
        if len(final)%2 != 0:
            return final[len(final)//2]
        else:
            left = int(len(final)/2) - 1
            right = int(len(final)/2)
            return (final[left]+final[right])/2.0

# V1
# IDEA : bisect
# https://leetcode.com/problems/median-of-two-sorted-arrays/discuss/2755/9-lines-O(log(min(mn)))-Python
# time = O(log(min(m,n)))
# space = O(1)
class Solution:
    def findMedianSortedArrays(self, nums1, nums2):
        a, b = sorted((nums1, nums2), key=len)
        m, n = len(a), len(b)
        after = (m + n - 1) / 2
        class Range:
            def __getitem__(self, i):
                return after-i-1 < 0 or a[i] >= b[after-i-1]
        i = bisect.bisect_left(Range(), True, 0, m)
        nextfew = sorted(a[i:i+2] + b[after-i:after-i+2])
        return (nextfew[0] + nextfew[1 - (m+n)%2]) / 2.0

# V1
# IDEA : binary search
# https://leetcode.com/problems/median-of-two-sorted-arrays/discuss/2755/9-lines-O(log(min(mn)))-Python
# time = O(log(min(m,n)))
# space = O(1)
class Solution:
    def findMedianSortedArrays(self, nums1, nums2):
        a, b = sorted((nums1, nums2), key=len)
        m, n = len(a), len(b)
        after = (m + n - 1) / 2
        lo, hi = 0, m
        while lo < hi:
            i = (lo + hi) / 2
            if after-i-1 < 0 or a[i] >= b[after-i-1]:
                hi = i
            else:
                lo = i + 1
        i = lo
        nextfew = sorted(a[i:i+2] + b[after-i:after-i+2])
        return (nextfew[0] + nextfew[1 - (m+n)%2]) / 2.0

# V1
# https://leetcode.com/problems/median-of-two-sorted-arrays/discuss/468832/Simple-python-solution
# time = O((m+n)log(m+n))
# space = O(m+n)
class Solution:
    def findMedianSortedArrays(self, nums1: List[int], nums2: List[int]) -> float:      
        concat = sorted(nums1+nums2) 
        if len(concat)%2 == 1:
            med = concat[int(len(concat)/2)]
        else:
            tot_len = int(len(concat)/2)
            med = (concat[tot_len-1]+concat[tot_len]) / 2
                         
        return med

# V1
# https://leetcode.com/problems/median-of-two-sorted-arrays/discuss/952647/Simple-python
# time = O((m+n)log(m+n))
# space = O(m+n)
import statistics
class Solution:
    def findMedianSortedArrays(self, nums1: List[int], nums2: List[int]) -> float:
        nums3 = nums1 + nums2
        nums3 = sorted(nums3)
        return statistics.median(nums3)

# V2
