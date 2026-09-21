"""

2033. Minimum Operations to Make a Uni-Value Grid
Medium

You are given a 2D integer grid of size m x n and an integer x. In one operation, you can add x to or subtract x from any element in the grid.

A uni-value grid is a grid where all the elements of it are equal.

Return the minimum number of operations to make the grid uni-value. If it is not possible, return -1.


Example 1:

Input: grid = [[2,4],[6,8]], x = 2
Output: 4
Explanation: We can make every element equal to 4 by doing the following:
- Add x to 2 once.
- Subtract x from 6 once.
- Subtract x from 8 twice.
A total of 4 operations were used.

Example 2:

Input: grid = [[1,5],[2,3]], x = 1
Output: 5
Explanation: We can make every element equal to 3.

Example 3:

Input: grid = [[1,2],[3,4]], x = 2
Output: -1
Explanation: It is impossible to make every element equal.


Constraints:

m == grid.length
n == grid[i].length
1 <= m, n <= 10^5
1 <= m * n <= 10^5
1 <= x, grid[i][j] <= 10^4

"""


# V0
class Solution(object):
    def minOperations(self, grid, x):
        """
        :type grid: List[List[int]]
        :type x: int
        :rtype: int
        """
        pass


# V0-1
# IDEA: MATH + SORT (gemini)
class Solution(object):

  def minOperations(self, grid, x):
    """
        :type grid: List[List[int]]
        :type x: int
        :rtype: int
        """
    # 1. 將 2D 矩陣壓平為 1D 串列，便於後續的模組檢查與中位數計算
    arr = []
    for row in grid:
      arr.extend(row)

    # 2. 檢查可行性：所有元素對 x 取餘數必須一致
    # 只要存在任一元素的餘數不同於基準元素，就無法透過增減 x 互通
    base_rem = arr[0] % x
    for val in arr:
      if val % x != base_rem:
        return -1

    # 3. 排序 1D 陣列。根據 L1 範數（Manhattan 距離性質），
    # 將所有數轉為同一個目標值的最小總步數，目標值選在「中位數」最優。
    arr.sort()
    n = len(arr)
    median = arr[n // 2]

    # 4. 計算每個元素變成中位數所需的步數
    # 每次操作可以加減 x，故所需次數為 abs(val - median) // x
    total_ops = 0
    for val in arr:
      total_ops += abs(val - median) // x

    return total_ops


# V0-2
# IDEA: MATH + SORT + `median` (GPT)
"""
CORE IDEA:


1. 先判斷能不能做到：所有數字對 x 的餘數必須相同。
2. 把 2D grid flatten 成 1D。
3. 排序後，讓所有數字變成 `median`（中位數），總 operation 最少。
4. 每個元素需要幾次操作就是 abs(num - median) // x。

"""
class Solution(object):
    def minOperations(self, grid, x):
        # Flatten the 2D grid into a 1D array
        nums = []

        for row in grid:
            for num in row:
                nums.append(num)

        # All numbers must have the same remainder modulo x.
        # Otherwise, we can never make them equal.
        remainder = nums[0] % x

        for num in nums:
            if num % x != remainder:
                return -1

        # Sort the numbers so we can find the median.
        nums.sort()

        # The median minimizes the total absolute distance:
        #
        #     sum(abs(nums[i] - target))
        #
        # Since every operation changes the value by x,
        # minimizing distance is equivalent to minimizing operations.
        median = nums[len(nums) // 2]

        operations = 0

        for num in nums:
            # Number of +x / -x operations needed
            operations += abs(num - median) // x

        return operations


# V0-3
# IDEA : FEASIBILITY BY REMAINDER mod x, THEN THE MEDIAN MINIMIZES THE COST
#
#   every operation moves a value by exactly x, so two cells can ever meet
#   only if they share the same remainder mod x. check that first -> else -1.
#
#   given they all agree mod x, the cost of targeting value t is
#       sum( abs(v - t) ) / x
#   and sum of absolute deviations is minimized at the MEDIAN of the values.
#
#   NOTE : the target must be one of the existing values (any other value with
#          the same remainder is worse), so picking the sorted middle element
#          is enough.
#
# time = O(m * n * log(m * n)), space = O(m * n)
class Solution(object):
    def minOperations(self, grid, x):
        vals = [v for row in grid for v in row]
        r = vals[0] % x
        if any(v % x != r for v in vals):
            return -1
        vals.sort()
        median = vals[len(vals) // 2]
        return sum(abs(v - median) // x for v in vals)
