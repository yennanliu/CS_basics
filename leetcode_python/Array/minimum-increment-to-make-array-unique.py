"""

945. Minimum Increment to Make Array Unique
Medium

You are given an integer array nums. In one move, you can pick an index i where 0 <= i < nums.length and increment nums[i] by 1.

Return the minimum number of moves to make every value in nums unique.

The test cases are generated so that the answer fits in a 32-bit integer.

Example 1:

Input: nums = [1,2,2]
Output: 1
Explanation: After 1 move, the array could be [1, 2, 3].

Example 2:

Input: nums = [3,2,1,2,1,7]
Output: 6
Explanation: After 6 moves, the array could be [3, 4, 1, 2, 5, 7].
It can be shown that it is impossible for the array to have all unique values with 5 or less moves.

Constraints:

1 <= nums.length <= 10^5
0 <= nums[i] <= 10^5

"""

# V0 

# V1 
# https://zxi.mytechroad.com/blog/greedy/leetcode-945-minimum-increment-to-make-array-unique/
# IDEA : GREEDY 
# -> Sort the elements, make sure A[i] >= A[i-1] + 1, 
# -> if not, THEN increase A[i] to A[i – 1] + 1
# (so the needed moves are :  A[i – 1] + 1 - A[i]   )
# time = O(nlogn)
# space = O(1)
class Solution(object):
  def minIncrementForUnique(self, A):
    A.sort()
    ans = 0
    for i in range(1, len(A)):
      if A[i] > A[i - 1]: 
        continue
      ans += A[i - 1] - A[i] + 1
      A[i] = A[i - 1] + 1
    return ans

# V1'
# https://blog.csdn.net/fuxuemingzhu/article/details/84495213
# time = O(nlogn)
# space = O(1)
class Solution(object):
    def minIncrementForUnique(self, A):
        """
        :type A: List[int]
        :rtype: int
        """
        N = len(A)
        if N == 0: return 0
        A.sort()
        res = 0
        prev = A[0]
        for i in range(1, N):
            if A[i] <= prev:
                prev += 1
                res += prev - A[i]
            else:
                prev = A[i]
        return res

# V2
# time = O(nlogn)
# space = O(n)
class Solution(object):
    def minIncrementForUnique(self, A):
        """
        :type A: List[int]
        :rtype: int
        """
        A.sort()
        A.append(float("inf"))
        result, duplicate = 0, 0
        for i in range(1, len(A)):
            if A[i-1] == A[i]:
                duplicate += 1
                result -= A[i]
            else:
                move = min(duplicate, A[i]-A[i-1]-1)
                duplicate -= move
                result += move*A[i-1] + move*(move+1)//2
        return result