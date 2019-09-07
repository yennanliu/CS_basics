# V0 

# V1 
# https://zxi.mytechroad.com/blog/greedy/leetcode-945-minimum-increment-to-make-array-unique/
# IDEA : GREEDY 
# -> Sort the elements, make sure A[i] >= A[i-1] + 1, 
# -> if not, THEN increase A[i] to A[i – 1] + 1
# (so the needed moves are :  A[i – 1] + 1 - A[i]   )
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
# Time:  O(nlogn)
# Space: O(n)
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