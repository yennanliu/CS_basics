"""
# python 3 
# https://leetcode.com/problems/single-number/description/

# solution 
# https://leetcode.com/articles/single-number/

136. Single Number
Easy

Given a non-empty array of integers nums, every element appears twice except for one. Find that single one.

You must implement a solution with a linear runtime complexity and use only constant extra space.

 

Example 1:

Input: nums = [2,2,1]
Output: 1
Example 2:

Input: nums = [4,1,2,1,2]
Output: 4
Example 3:

Input: nums = [1]
Output: 1
 

Constraints:

1 <= nums.length <= 3 * 104
-3 * 104 <= nums[i] <= 3 * 104
Each element in the array appears twice except for one element which appears only once.

"""

# V0
# time = O(n)
# space = O(n)
import collections 
class Solution(object):
    def singleNumber(self, nums):
        num_count = collections.Counter(nums)
        return [x for x in num_count if num_count[x] == 1] 


# V0-1
# IDEA : BIT XOR
"""
CORE IDEA:

    相同的數字 XOR 兩次會抵消掉，最後只剩下出現一次的數字。



---


1. ^ 是什麼？
     ^ 是 bitwise XOR（位元互斥或)

     ->

     ```
     rule:

     a ^ a = 0
     a ^ 0 = a



     example:

        0 ^ 0 = 0
        0 ^ 1 = 1
        1 ^ 0 = 1
        1 ^ 1 = 0
     ```


2.  res ^= n
    -> equals to res = res ^ n


3.  NOTE !!! 
    
    -> we do `bit op` in `binary system` (二進位)

      (NOT the Decimal （(十進位))



----


Dry run:


這段程式碼利用了 **位元運算中的異或（XOR，符號 `^`）** 的特性，以高效的方式尋找陣列中唯一只出現一次的數字。

---

### 💡 核心原理：異或（XOR）的三大數學性質

1. **歸零律（相同為 0）**：$a \oplus a = 0$（任何數與自己進行 XOR，結果必然為 0）
2. **恆等律（與 0 異或保持不變）**：$a \oplus 0 = a$（任何數與 0 進行 XOR，結果仍為其本身）
3. **交換律與結合律**：$a \oplus b \oplus c = a \oplus c \oplus b$（運算順序不影響結果）

因為題目保證**除了某一個數字只出現一次外，其他數字都剛好出現兩次**：
當我們將陣列中的所有數字依次進行 XOR 運算時，所有成對出現的數字都會**兩兩抵消變為 0**，最後剩下的那個只出現一次的數字與 0 進行 XOR，**最終結果就是該目標數字**。

---

### 📝 逐行程式碼拆解

```python
class Solution(object):
    def singleNumber(self, nums):
        res = 0  # 1. 初始化變數 res 為 0 ( 0 ^ x = x )
        
        for n in nums:
            res ^= n  # 2. 將陣列中的每個數字與 res 進行 XOR 運算並賦值 (等價於 res = res ^ n)
            
        return res   # 3. 走訪結束後，成對的數字已全部抵消為 0，剩餘的 res 即為目標數字

```

---

### 📌 模擬執行過程

假設 `nums = [4, 1, 2, 1, 2]`：

1. `res = 0`
2. `0 ^ 4 = 4`
3. `4 ^ 1 = 5` （二進位：`100 ^ 001 = 101`）
4. `5 ^ 2 = 7` （二進位：`101 ^ 010 = 111`）
5. `7 ^ 1 = 6` （二進位：`111 ^ 001 = 110`，相當於把先前加上的 `1` 抵消）
6. `6 ^ 2 = 4` （二進位：`110 ^ 010 = 100`，相當於把先前加上的 `2` 抵消）

最終傳回 `4`。

用交換律展開看其本質：

$0 \oplus 4 \oplus 1 \oplus 2 \oplus 1 \oplus 2 = 4 \oplus (1 \oplus 1) \oplus (2 \oplus 2) = 4 \oplus 0 \oplus 0 = 4$

---

### ⏱️ 複雜度分析

* **時間複雜度**：$\mathcal{O}(N)$ — 僅需走訪一次長度為 $N$ 的陣列。
* **空間複雜度**：$\mathcal{O}(1)$ — 僅使用額外的變數 `res`，符合題目要求的常數級空間限制。


"""
class Solution(object):
    def singleNumber(self, nums):
        res = 0

        for n in nums:
            res ^= n

        return res



# V0'
# IDEA : BIT XOR 
# IDEA 
# Solution with XOR #
# Recall the following two properties of XOR:
# It returns zero if we take XOR of two same numbers.
# It returns the same number if we XOR with zero.
# So we can XOR all the numbers in the input; 
# -> duplicate numbers will zero out each other and we will be left with the single number.
# time = O(n)
# space = O(1)
class Solution(object):
    def singleNumber(self, nums):
        a = 0
        for i in nums:
            a ^= i
        return a

# V1
# https://blog.csdn.net/qq_20141867/article/details/82314035
# time = O(n)
# space = O(n)  # set(nums)
class Solution(object):
    def singleNumber(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        return 2 * sum(set(nums)) - sum(nums)
        
# V1'
# https://www.jiuzhang.com/solution/single-number/#tag-highlight-lang-python
# time = O(n)
# space = O(1)
class Solution:
    """
   @param A : an integer array
   @return : a integer
   """
    def singleNumber(self, A):
        # write your code here
        ans = 0;
        for x in A:
            ans = ans ^ x
        return ans

# V1
# time = O(n^2)  # `in` check and `.remove` on a list are each O(n)
# space = O(n)
class Solution:
	def singleNumber(self, nums):
		no_repeat_array=[]
		for index, item in enumerate(nums):
			if item in no_repeat_array:
				no_repeat_array.remove(item)
			else:
				no_repeat_array.append(item)
		return no_repeat_array.pop()

# V2
# to know the non-repeat element 
# 2∗(a+b+c)−(a+a+b+b+c)=c
# time = O(n)
# space = O(n)  # set(nums)
class Solution(object):
    def singleNumber(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        return 2 * sum(set(nums)) - sum(nums)

# V3
# XOR logic 
# https://www.programiz.com/python-programming/operators
# x ^ 0 = x 
# x ^ x = 0
# time = O(n)
# space = O(1)
class Solution(object):
    def singleNumber(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        a = 0
        for i in nums:
            a ^= i
        return a