"""

238. Product of Array Except Self
Medium

Given an integer array nums, return an array answer such that answer[i] is equal to the product of all the elements of nums except nums[i].

The product of any prefix or suffix of nums is guaranteed to fit in a 32-bit integer.

You must write an algorithm that runs in O(n) time and without using the division operation.

Example 1:

Input: nums = [1,2,3,4]
Output: [24,12,8,6]


Example 2:

Input: nums = [-1,1,0,-3,3]
Output: [0,0,9,0,0]


Constraints:

2 <= nums.length <= 105
-30 <= nums[i] <= 30
The product of any prefix or suffix of nums is guaranteed to fit in a 32-bit integer.
 

Follow up: Can you solve the problem in O(1) extra space complexity? (The output array does not count as extra space for space complexity analysis.)

"""


# V0
# IDEA: prefix,  suffix (gemini)
class Solution(object):

  def productExceptSelf(self, nums):
    """:type nums: List[int] :rtype: List[int]"""
    n = len(nums)
    if not nums:
      return []

    # 1. 建立 prefix 陣列：
    # prefix[i] 代表「索引 i 左側所有元素的乘積」
    # 註：prefix[0] = 1，因為索引 0 的左邊沒有任何元素
    prefix = [1] * n
    for i in range(1, n):
      # 當前左側累積乘積 = 上一個左側累積乘積 * 上一個數字
      prefix[i] = prefix[i - 1] * nums[i - 1]

    # 2. 建立 suffix 陣列：
    # suffix[i] 代表「索引 i 右側所有元素的乘積」
    # 註：suffix[n-1] = 1，因為最後一個元素的右邊沒有任何元素
    suffix = [1] * n

    # [Python 遞減迴圈小筆記]
    # range(start, stop, step) -> range(n-2, -1, -1)
    # 確保終點為 -1 才能順利涵蓋並處理到索引 0
    for i in range(n - 2, -1, -1):
      # 當前右側累積乘積 = 下一個右側累積乘積 * 下一個數字
      suffix[i] = suffix[i + 1] * nums[i + 1]

    # 3. 結合左右兩側乘積得出最終結果：
    # 除了自身以外的乘積 = (左側所有元素乘積) * (右側所有元素乘積)
    res = [1] * n
    for i in range(n):
      res[i] = prefix[i] * suffix[i]

    return res



# V0-0-1
# IDEA: PREFIX, SUFFIX (gpt)
class Solution(object):
    def productExceptSelf(self, nums):
        """
        :type nums: List[int]
        :rtype: List[int]

        Key idea:
        For each index i:

            result[i] =
                product of elements BEFORE i
                *
                product of elements AFTER i

        We pre-compute these two products using:
        1. prefix[i]
        2. suffix[i]
        """

        n = len(nums)

        # ---------------------------------------------------------
        # 1. Build prefix product
        # ---------------------------------------------------------
        #
        # prefix[i] = product of all elements BEFORE index i
        #
        # Example:
        #
        # nums   = [-1, 1, 0, -3, 3]
        #
        # prefix = [1, -1, -1, 0, 0]
        #
        # Why prefix[0] = 1?
        #
        # There is no element before index 0.
        # We use 1 as the multiplicative identity:
        #
        #     1 * anything = anything
        #
        prefix = [1] * n

        # Start from index 1 because:
        # prefix[0] is already initialized to 1.
        for i in range(1, n):

            # Everything before i =
            # everything before (i - 1)
            # * nums[i - 1]
            prefix[i] = prefix[i - 1] * nums[i - 1]

        # ---------------------------------------------------------
        # 2. Build suffix product
        # ---------------------------------------------------------
        #
        # suffix[i] = product of all elements AFTER index i
        #
        # Example:
        #
        # nums   = [-1, 1, 0, -3, 3]
        #
        # suffix = [0, 0, -9, 3, 1]
        #
        # Why suffix[n - 1] = 1?
        #
        # There is no element after the last index.
        #
        suffix = [1] * n

        # Iterate from right to left.
        #
        # range(n - 2, -1, -1) means:
        #
        #     start = n - 2
        #     stop  = -1  (exclusive)
        #     step  = -1
        #
        # Example:
        #
        # n = 5
        #
        # range(3, -1, -1)
        # -> 3, 2, 1, 0
        #
        # We start at n - 2 because suffix[n - 1] = 1
        # is already initialized.
        for i in range(n - 2, -1, -1):

            # Everything after i =
            # everything after (i + 1)
            # * nums[i + 1]
            suffix[i] = suffix[i + 1] * nums[i + 1]

        # ---------------------------------------------------------
        # 3. Build final result
        # ---------------------------------------------------------
        #
        # For every index i:
        #
        #     result[i]
        #         =
        #     prefix[i] * suffix[i]
        #
        # Example:
        #
        # nums = [-1, 1, 0, -3, 3]
        #
        # i = 2:
        #
        # prefix[2] = -1
        # suffix[2] = -9
        #
        # result[2] = (-1) * (-9) = 9
        #
        res = [1] * n

        for i in range(n):
            res[i] = prefix[i] * suffix[i]

        return res



# V0-0-2
# IDEA: prefix,  suffix (gemini)
# Time: O(N)
# Space: O(1)
"""
CORE IDEA:

    -> 

        這道題目的經典解法是利用回傳陣列 ans 本身來暫存「左側乘積」，
        然後用一個變數在右側回掃時動態維護「右側乘積」，
        這樣可以將額外空間複雜度壓到 O(1)
        （輸出陣列不計入額外空間


---

Example:

    
    nums = [1, 2, 3, 4]

    ->

    index 0 → 2 × 3 × 4
    index 1 → 1 × 3 × 4
    index 2 → 1 × 2 × 4
    index 3 → 1 × 2 × 3


    ->

    ans[i]
    = product(left of i)
    × product(right of i)


"""
class Solution(object):

  def productExceptSelf(self, nums):
    """:type nums: List[int] :rtype: List[int]"""
    if not nums:
      return []

    n = len(nums)
    ans = [1] * n

    # 1. 第一輪掃描：計算每個元素左側所有數字的乘積，並存入 ans
    left_prod = 1
    for i in range(n):
      ans[i] = left_prod
      left_prod *= nums[i]  # 累積左側乘積，供下一個元素使用

    # 2. 第二輪掃描（從右往左）：用變數維護右側乘積，並直接乘以 ans 中的左側乘積
    right_prod = 1
    for i in range(n - 1, -1, -1):
      ans[i] *= right_prod  # 左側乘積 * 右側乘積 = 除自身外的乘積
      right_prod *= nums[i]  # 累積右側乘積，供上一個元素使用

    return ans


# V0
# IDEA: prefix,  suffix
"""
Dry run: (input = [-1, 1, 0, -3, 3])


Let's dry-run the standard prefix/suffix solution on:

```python
nums = [-1, 1, 0, -3, 3]
```

### Step 1: Build prefix array

Definition:

```python
prefix[i] = product of all elements before i
```

Initialize:

```python
prefix = [1, 1, 1, 1, 1]
```

Fill it:

| i | Calculation                    | prefix            |
| - | ------------------------------ | ----------------- |
| 1 | prefix[0] * nums[0] = 1 * (-1) | [1, -1, 1, 1, 1]  |
| 2 | prefix[1] * nums[1] = -1 * 1   | [1, -1, -1, 1, 1] |
| 3 | prefix[2] * nums[2] = -1 * 0   | [1, -1, -1, 0, 1] |
| 4 | prefix[3] * nums[3] = 0 * (-3) | [1, -1, -1, 0, 0] |

Final:

```python
prefix = [1, -1, -1, 0, 0]
```

---

### Step 2: Build suffix array

Definition:

```python
suffix[i] = product of all elements after i
```

Initialize:

```python
suffix = [1, 1, 1, 1, 1]
```

Fill from right to left:

| i | Calculation                    | suffix       |
| - | ------------------------------ | ------------ |
| 3 | suffix[4] * nums[4] = 1 * 3    | [1,1,1,3,1]  |
| 2 | suffix[3] * nums[3] = 3 * (-3) | [1,1,-9,3,1] |
| 1 | suffix[2] * nums[2] = -9 * 0   | [1,0,-9,3,1] |
| 0 | suffix[1] * nums[1] = 0 * 1    | [0,0,-9,3,1] |

Final:

```python
suffix = [0, 0, -9, 3, 1]
```

---

### Step 3: Compute answer

Formula:

```python
res[i] = prefix[i] * suffix[i]
```

| i | prefix[i] | suffix[i] | result |
| - | --------- | --------- | ------ |
| 0 | 1         | 0         | 0      |
| 1 | -1        | 0         | 0      |
| 2 | -1        | -9        | 9      |
| 3 | 0         | 3         | 0      |
| 4 | 0         | 1         | 0      |

Result:

```python
res = [0, 0, 9, 0, 0]
```

### Why does the zero work automatically?

At index `2` (where the zero is):

```python
product_except_self =
(-1) * 1 * (-3) * 3
= 9
```

For every other index, the multiplication includes the original zero, so the result becomes:

```python
0
```

That's why the final answer is:

```python
[0, 0, 9, 0, 0]
```

"""
# time = O(n)
# space = O(n)
class Solution(object):
    def productExceptSelf(self, nums):
        # Number of elements in the input array
        n = len(nums)

        """
        NOTE !!!

        
        1. prefix[i]: save product of all element BEFORE cur idx

        2. prefix[0] = 1 !!!
            -> since there NO any element BEFORE idx = 0
        """
        # prefix[i] will store the product of all elements
        # to the LEFT of index i
        prefix = [1] * n

        
        """
        NOTE !!!

        
        1. suffix[i]: save product of all element AFTER cur idx

        2. prefix[n-1] = 1 !!!
            -> since there NO any element AFTER idx = 0
        """

        # suffix[i] will store the product of all elements
        # to the RIGHT of index i
        suffix = [1] * n

        # Build prefix products
        #
        # Example:
        # nums = [-1, 1, 0, -3, 3]
        #
        # prefix[0] = 1
        # prefix[1] = -1
        # prefix[2] = -1
        # prefix[3] = 0
        # prefix[4] = 0
        #
        # Result:
        # prefix = [1, -1, -1, 0, 0]
        # NOTE !!! start from i = 1
        for i in range(1, n):
            # Product of everything before i
            prefix[i] = prefix[i - 1] * nums[i - 1]

        # Build suffix products
        #
        # Example:
        # nums = [-1, 1, 0, -3, 3]
        #
        # suffix[4] = 1
        # suffix[3] = 3
        # suffix[2] = -9
        # suffix[1] = 0
        # suffix[0] = 0
        #
        # Result:
        # suffix = [0, 0, -9, 3, 1]
        """

        # NOTE !!! end at i = -1
        # so the reverse loop can cover idx=0
        # doc/cheatsheet/python_trick.md

        ->

        >>> x = [1,2,3]
        >>> for i in range(len(x)-1, -1, -1):
        ...     print (x[i])
        ...
        3
        2
        1
        >>>
        >>>
        >>> for i in range(len(x)-1, 0, -1):
        ...     print (x[i])
        ...
        3
        2
        """
        for i in range(n - 2, -1, -1):
            # Product of everything after i
            suffix[i] = suffix[i + 1] * nums[i + 1]

        # Allocate result array
        res = [1] * n

        # For each position:
        #
        # product_except_self =
        # (product of left side) *
        # (product of right side)
        #
        # Example:
        # i = 2
        # prefix[2] = -1
        # suffix[2] = -9
        # res[2] = (-1) * (-9) = 9
        for i in range(n):
            res[i] = prefix[i] * suffix[i]

        return res


# V0-1
# IDEA : array product + deal with 0 case
#  get all products, 
#  if val != 0, productExceptSelf  = all_products / val
#  if val == 0, set val = 1, get tmp_all_products,  productExceptSelf  = tmp_all_products / 1
# https://github.com/yennanliu/CS_basics/blob/master/leetcode_java/src/main/java/LeetCodeJava/Array/ProductOfArrayExceptSelf.java#L9

# V0-2
# time = O(n)
# space = O(1)
class Solution:
    def productExceptSelf(self, nums):
        # CASE 1: has `more that one 0` -> the `Product of Array Except Self` should always equal 0
        if nums.count(0) > 1:
            return [0] * len(nums)
        # CASE 2: has `only one 0` -> we get the `Product of Array Except Self` for non 0 and 0 separately
        if nums.count(0) == 1:
            _prod_except_z = 1
            for n in nums:
                if n != 0:
                    _prod_except_z = n * _prod_except_z
        # for non 0 elements, we still get the Product of Array for op later
        _prod = 1
        for n in nums:
            _prod = n * _prod
        r = []
        for i in nums:
            if i != 0:
                r.append(int(_prod / i))
            else:
                r.append(int(_prod_except_z))
        return r

# V0-3
# IDEA : ARRAY OP
# time = O(n)
# space = O(1)
class Solution(object):
    def productExceptSelf(self, nums):
        _len = len(nums)
        ans = [1 for _ in range(_len)]
        ### NOTE HERE, we define 2 vars (left, right) for left, right multiply
        left = 1
        right = 1
        ### NOTE : we need to start from idx = 0 ~ idx = _len - 1
        for i in range(0, _len-1):
            ### NOTE here, we multuply left and nums[i]
            left = left * nums[i]
            ### NOTE HERE !!! we modify nums[i+1] WHEN idx = i, and the updated value =  ans[i+1] * left 
            ans[i+1] = ans[i+1] * left
        ### NOTE here, we start fron _len-1, and STOP at idx 0 (inverse order)
        for j in range(_len-1, 0, -1):
            right = right * nums[j]
            ### NOTE HERE !!! we modify nums[j-1] WHEN idx = j, and the updated value = ans[j-1] * right
            ans[j-1] = ans[j-1] * right
        return ans

# V0-4
# time = O(n)
# space = O(1)
class Solution:
    # @param {integer[]} nums
    # @return {integer[]}
    def productExceptSelf(self, nums):
        size = len(nums)
        output = [1] * size
        left = 1
        for x in range(size - 1):
            left *= nums[x]
            output[x + 1] *= left
        right = 1
        for x in range(size - 1, 0, -1):
            right *= nums[x]
            output[x - 1] *= right
        return output
        
# V1 
# http://bookshadow.com/weblog/2015/07/16/leetcode-product-array-except-self/
# https://blog.csdn.net/fuxuemingzhu/article/details/79325534
# IDEA : 
# SINCE output[i] = (x0 * x1 * ... * xi-1) * (xi+1 * .... * xn-1)
# -> SO DO A 2 LOOP
# -> 1ST LOOP : GO THROGH THE ARRAY (->) : (x0 * x1 * ... * xi-1)
# -> 2ND LOOP : GO THROGH THE ARRAY (<-) : (xi+1 * .... * xn-1)
# e.g.
# given [1,2,3,4], return [24,12,8,6].
# -> output = [2*3*4, 1,1,1]  <-- 2*3*4    (right of 1: 2,3,4)
# -> output = [2*3*4, 1*3*4,1,1] <-- 1*3*4 (left of 2 :1, right of 2: 3,4)
# -> output = [2*3*4, 1*3*4,1*2*4,1] <-- 1*2*4 (left of 3: 1,2 right of 3 : 4)
# -> output = [2*3*4, 1*3*4,1*2*4,1*2*3] <-- 1*2*3 (left of 4 : 1,2,3)
# -> final output  = [2*3*4, 1*3*4,1*2*4,1*2*3] = [24,12,8,6]
# time = O(n)
# space = O(1)
class Solution:
    # @param {integer[]} nums
    # @return {integer[]}
    def productExceptSelf(self, nums):
        size = len(nums)
        output = [1] * size
        left = 1
        for x in range(size - 1):
            left *= nums[x]
            output[x + 1] *= left
        right = 1
        for x in range(size - 1, 0, -1):
            right *= nums[x]
            output[x - 1] *= right
        return output

# V1'
# https://www.jiuzhang.com/solution/product-of-array-except-self/#tag-highlight-lang-python
# time = O(n)
# space = O(1)
class Solution(object):
    def productExceptSelf(self, nums):
        """
        :type nums: List[int]
        :rtype: List[int]
        """
        result = [1 for i in nums]
        nf = 1
        nb = 1
        length = len(nums)
        for i in range(length):
            result[i] *= nf
            nf *= nums[i]
            result[length-i-1] *= nb
            nb *= nums[length-i-1]
        return result

# V2
# time = O(n)
# space = O(1)
class Solution(object):
    # @param {integer[]} nums
    # @return {integer[]}
    def productExceptSelf(self, nums):
        if not nums:
            return []
        left_product = [1 for _ in range(len(nums))]
        for i in range(1, len(nums)):
            left_product[i] = left_product[i - 1] * nums[i - 1]

        right_product = 1
        for i in range(len(nums) - 2, -1, -1):
            right_product *= nums[i + 1]
            left_product[i] = left_product[i] * right_product
        return left_product
