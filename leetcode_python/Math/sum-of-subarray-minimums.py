"""

907. Sum of Subarray Minimums
Medium

Given an array of integers arr, find the sum of min(b), where b ranges over every (contiguous) subarray of arr. Since the answer may be large, return the answer modulo 109 + 7.

 

Example 1:

Input: arr = [3,1,2,4]
Output: 17
Explanation: 
Subarrays are [3], [1], [2], [4], [3,1], [1,2], [2,4], [3,1,2], [1,2,4], [3,1,2,4]. 
Minimums are 3, 1, 2, 4, 1, 1, 2, 1, 1, 1.
Sum is 17.
Example 2:

Input: arr = [11,81,94,43,3]
Output: 444
 

Constraints:

1 <= arr.length <= 3 * 104
1 <= arr[i] <= 3 * 104

"""


"""

NOTE !!!

we CAN'T solve this LC with regular `mono stack` pattern.

-> e.g. CAN'T solve with algo as LC 739 
"""




# V0
# IDEA: MONO STACK (INCREASING)
"""

CORE IDEA:

-V1


- left[i] =
    number of consecutive elements to 
    the left (including itself) where arr[i] 
    is the minimum.


- right[i] = 
    number of consecutive elements 
    to the right (including itself) 
    where arr[i] remains the minimum.


-> ans = SUM( arr[i] * left[i] * right[i] )



-> NOTE:

The only subtlety is how to handle duplicates.

On the left, use >.
On the right, use >=.


-> Dry run


if arr = [3,1,2,4]

->

left  = [1,2,1,1]
right = [1,3,2,1]


-> so

3 * 1 * 1 = 3
1 * 2 * 3 = 6
2 * 1 * 2 = 4
4 * 1 * 1 = 4

Total = 17


-> ans = 17





----------


-V2

For each element arr[i], count:

  - how many subarrays choose arr[i] as 
    their `minimum` extending to the `left`

  - how many subarrays choose arr[i] as 
    their `minimum` extending to the `right`



-> Then its contribution is:

     -> arr[i] * left_count * right_count


"""
# V0
# IDEA: MONO STACK (Gemini)
class Solution(object):
    def sumSubarrayMins(self, arr):
        """
        :type arr: List[int]
        :rtype: int
        """
        if not arr:
            return 0
            
        MOD = 10**9 + 7
        n = len(arr)
        
        left = [0] * n
        right = [0] * n
        
        # --- 1. PRE-CALCULATE LEFT DISTANCES ---
        # Stores indices of a strictly increasing sequence
        mono_st = [] 
        for i in range(n):
            val = arr[i]
            # Pop elements that are greater than or equal to current val
            while mono_st and arr[mono_st[-1]] >= val:
                """
                # NOTE !!!
                """
                mono_st.pop()
            
            """
            # NOTE !!!
            """
            # If stack is empty, it means 'val' is the smallest element seen so far
            # Distance to left boundary is i - (-1) = i + 1
            left[i] = i + 1 if not mono_st else i - mono_st[-1]
            mono_st.append(i)

        # --- 2. PRE-CALCULATE RIGHT DISTANCES ---
        # Reset the stack for the backward pass
        mono_st = [] 
        for i in range(n - 1, -1, -1):
            val = arr[i]
            # Pop elements that are strictly greater than current val
            while mono_st and arr[mono_st[-1]] > val:
                mono_st.pop()
            
            # If stack is empty, there is no smaller element to its right
            # Distance to right boundary is n - i
            right[i] = n - i if not mono_st else mono_st[-1] - i
            mono_st.append(i)

        # --- 3. AGGREGATE TOTAL SUM ---
        ans = 0
        for i in range(n):
            ans += arr[i] * left[i] * right[i]
            ans %= MOD
            
        return ans


# V0-1
# IDEA: MONO STACK
class Solution(object):
    def sumSubarrayMins(self, arr):
        """
        :type arr: List[int]
        :rtype: int
        """
        MOD = 10**9 + 7
        n = len(arr)

        # left[i]:
        # number of consecutive elements to the left
        # (including itself)
        # where arr[i] remains the minimum
        left = [0] * n

        # right[i]:
        # number of consecutive elements to the right
        # (including itself)
        # where arr[i] remains the minimum
        right = [0] * n

        stack = []

        # ----------------------------------------
        # Previous Less Element
        #
        # Use >
        #
        # Example:
        # arr = [3,1,2,4]
        #
        # left = [1,2,1,1]
        # ----------------------------------------
        for i in range(n):

            while stack and arr[stack[-1]] > arr[i]:
                stack.pop()

            if not stack:
                left[i] = i + 1
            else:
                left[i] = i - stack[-1]

            stack.append(i)

        stack = []

        # ----------------------------------------
        # Next Less Or Equal Element
        #
        # Use >=
        #
        # This avoids double counting duplicates.
        # ----------------------------------------
        for i in range(n - 1, -1, -1):

            while stack and arr[stack[-1]] >= arr[i]:
                stack.pop()

            if not stack:
                right[i] = n - i
            else:
                right[i] = stack[-1] - i

            stack.append(i)

        ans = 0

        # contribution of each element
        for i in range(n):
            ans += arr[i] * left[i] * right[i]
            ans %= MOD

        return ans




# V0-1
# IDEA: MONO STACK (INCREASING)
class Solution(object):
    def sumSubarrayMins(self, arr):
        """
        :type arr: List[int]
        :rtype: int
        """
        MOD = 10**9 + 7
        # Append a sentinel value -1 to flush out remaining elements in the stack at the end
        arr.append(-1)
        
        stack = []  # Stores indices of elements in a monotonic increasing order
        total_sum = 0
        
        for i in range(len(arr)):
            # While the stack is not empty and the current element is smaller than 
            # the element at the top of the stack, we found the right boundary for stack[-1].
            while stack and arr[i] < arr[stack[-1]]:
                mid = stack.pop()
                
                # Right boundary distance (how far it can expand right)
                R = i - mid
                
                # Left boundary distance (how far it can expand left)
                # If stack is empty, it means mid was the smallest element seen so far
                L = mid - stack[-1] if stack else mid + 1
                
                # Contribution calculation: element * Left choices * Right choices
                total_sum += arr[mid] * L * R
                total_sum %= MOD
                
            stack.append(i)
            
        # Pop the sentinel -1 to restore the original array structure (good practice)
        arr.pop()
        
        return total_sum



# V0
# IDEA :  increasing stacks
class Solution:
    def sumSubarrayMins(self, A):
        n, mod = len(A), 10**9 + 7
        left, right, s1, s2 = [0] * n, [0] * n, [], []

        for i in range(n):
            count = 1
            while s1 and s1[-1][0] > A[i]:
                count += s1.pop()[1]
            left[i] = count
            s1.append([A[i], count])

        for i in range(n)[::-1]:
            count = 1
            while s2 and s2[-1][0] >= A[i]:
                count += s2.pop()[1]
            right[i] = count
            s2.append([A[i], count])
        return sum(a * l * r for a, l, r in zip(A, left, right)) % mod

# V0'
# IDEA : BRUTE FORCE (TLE)
# brute force
# class Solution(object):
#     def sumSubarrayMins(self, arr):
#         # edge case
#         if not arr:
#             return 0
#         # double loop
#         res = []
#         for i in range(len(arr)):
#             for j in range(i+1, len(arr)+1):
#                 res.append(min(arr[i:j]))
#         return sum(res)

# V1
# IDEA :  increasing stacks
# https://leetcode.com/problems/sum-of-subarray-minimums/discuss/284184/Python-Max-Histogram
class Solution:
    def sumSubarrayMins(self, A):
        A.append(-1)
        stack=[-1]
        res=0
        for i in range(len(A)):
            while A[i]<A[stack[-1]]:
                idx=stack.pop()
                res+=A[idx]*(i-idx)*(idx-stack[-1])
            stack.append(i)
        return res%(10**9+7)

# V1
# IDEA :  increasing stacks
# https://leetcode.com/problems/sum-of-subarray-minimums/discuss/170750/JavaC%2B%2BPython-Stack-Solution
# IDEA :
# I guess this is a general intuition for most solution.
# res = sum(A[i] * f(i))
# where f(i) is the number of subarrays,
# in which A[i] is the minimum.
# To get f(i), we need to find out:
# left[i], the length of strict bigger numbers on the left of A[i],
# right[i], the length of bigger numbers on the right of A[i].
# Then,
# left[i] + 1 equals to
# the number of subarray ending with A[i],
# and A[i] is single minimum.
# right[i] + 1 equals to
# the number of subarray starting with A[i],
# and A[i] is the first minimum.
# Finally f(i) = (left[i] + 1) * (right[i] + 1)
# For [3,1,2,4] as example:
# left + 1 = [1,2,1,1]
# right + 1 = [1,3,2,1]
# f = [1,6,2,1]
# res = 3 * 1 + 1 * 6 + 2 * 2 + 4 * 1 = 17
class Solution:
    def sumSubarrayMins(self, A):
        n, mod = len(A), 10**9 + 7
        left, right, s1, s2 = [0] * n, [0] * n, [], []

        for i in range(n):
            count = 1
            while s1 and s1[-1][0] > A[i]:
                count += s1.pop()[1]
            left[i] = count
            s1.append([A[i], count])

        for i in range(n)[::-1]:
            count = 1
            while s2 and s2[-1][0] >= A[i]:
                count += s2.pop()[1]
            right[i] = count
            s2.append([A[i], count])
        return sum(a * l * r for a, l, r in zip(A, left, right)) % mod

# V1
# IDEA :  increasing stacks + above improvement
# https://leetcode.com/problems/sum-of-subarray-minimums/discuss/170750/JavaC%2B%2BPython-Stack-Solution
class Solution:
     def sumSubarrayMins(self, A):
            res = 0
            s = []
            A = [0] + A + [0]
            for i, x in enumerate(A):
                while s and A[s[-1]] > x:
                    j = s.pop()
                    k = s[-1]
                    res += A[j] * (i - j) * (j - k)
                s.append(i)
            return res % (10**9 + 7)

# V1
# IDEA : STACK
# https://leetcode.com/problems/sum-of-subarray-minimums/discuss/374000/stack-python
# IDEA :
# Using a stack like the other solutions
#
# left[i] = k
# means a[i] is the unique minima in a[ i - k + 1:i + 1]
#
# right[i] = k
# means a[i] is a minima (possibly among others) in a[ i:i + k]
#
# final answer
# sigma a[i] * left[i] * right[i]
#
# two passes O(n)
# plus a third to sigma and compute that cumulative sum
class Solution(object):
    def sumSubarrayMins(self, A):
        mod = 10 ** 9 + 7
        left = self.f(A)
        right = self.g(A)
        cs = 0
        for i in range(len(A)): 
            cs = (cs + left[i] * right[i] * A[i]) % mod
        return cs
        
    def f(self,A):
        stack = collections.deque([])
        left = [0] * len(A)
        for i in range(len(A)):
            while(stack and A[stack[-1]] > A[i]):
                stack.pop()
            left[i] = i - (stack[-1] if stack else -1)
            stack.append(i)
        return left
    def g(self,A):
        stack = collections.deque([])
        right = [0] * len(A)
        for i in range(len(A)-1,-1,-1):
            while(stack and A[stack[-1]] >= A[i]):
                stack.pop()
            right[i] = (stack[-1] if stack else len(A)) - i
            stack.append(i)
        return right

# V1
# https://leetcode.com/problems/sum-of-subarray-minimums/discuss/170927/Python-Stack
class Solution(object):
    def sumSubarrayMins(self, A):
        an = 0
        stack = [[float('-inf'), 0, 0]]
        for x in A:
            c = 0
            while stack[-1][0] >= x:
                c += stack.pop()[1]
            increase = stack[-1][2] + (c + 1)* x
            stack.append([x, c + 1, increase])
            an += increase
        return an%(10**9+7)

# V1
# https://leetcode.com/problems/sum-of-subarray-minimums/discuss/279705/python-solution
class Solution:
    def sumSubarrayMins(self, A):
        n = len(A)
        left, right, sl, sr = [], [n] * n, [], []
        for i in range(n):
            while sl and A[sl[-1]] > A[i]:
                sl.pop()
            left += [sl[-1]] if sl else [-1]
            sl += [i]
        for i in range(n):
            while sr and A[sr[-1]] > A[i]:
                right[sr.pop()] = i
            sr += [i]
        return sum(A[i] * (i - left[i]) * (right[i] - i) for i in range(n)) % (10**9 +7)


# V1
# https://leetcode.jp/leetcode-907-sum-of-subarray-minimums-%E8%A7%A3%E9%A2%98%E6%80%9D%E8%B7%AF%E5%88%86%E6%9E%90/
# JAVA
# public int sumSubarrayMins(int[] A) {
#     int[] leftBigger = new int[A.length];
#     int[] rightBigger = new int[A.length];
#     long sum = 0;
#     // 计算左边比自身大的数的个数
#     for (int i = 0; i < A.length; i++) {
#         int countLeft = 1;
#         int j = i - 1;
#         while (j >= 0 && A[j] >= A[i]) {
#             countLeft += leftBigger[j];
#             j -= leftBigger[j];
#         }
#         leftBigger[i] = countLeft;
#     }
#     // 计算右边比自身大的数的个数
#     for (int i = A.length - 1; i >= 0; i--) {
#         int countRight = 1;
#         int k = i + 1;
#         while (k < A.length && A[k] > A[i]) {
#             countRight += rightBigger[k];
#             k += rightBigger[k];
#         }
#         rightBigger[i] = countRight;
#     }
#     // 算出结果
#     for (int i = 0; i < A.length; i++) {
#         sum += (A[i] * leftBigger[i] * rightBigger[i]);
#     }
#     return (int) (sum % 1000000007);
# }

# V1 
# https://blog.csdn.net/zjucor/article/details/82721781
class Solution:
    def sumSubarrayMins(self, a):
        """
        :type A: List[int]
        :rtype: int
        """
        n=len(a)
        left,right=[0]*n,[0]*n
        
        st=[]
        for i,v in enumerate(a):
            while st and a[st[-1]]>v:
                idx=st.pop()
                right[idx]=i-idx
            st.append(i)
        while st:
            idx=st.pop()
            right[idx]=n-idx
        
        a=a[::-1]    
        st=[]
        for i,v in enumerate(a):
            while st and a[st[-1]]>=v:
                idx=st.pop()
                left[idx]=i-idx
            st.append(i)
        while st:
            idx=st.pop()
            left[idx]=n-idx
        left=left[::-1]
        
#        print(left,right)
        
        a=a[::-1]
        res=0
        mod=10**9+7
        for i in range(n):
#            print(left[i]*right[i], a[i])
            res+=left[i]*right[i]*a[i]
            res%=mod
        return res

# V2
# Time:  O(n)
# Space: O(n)
import itertools
# Ascending stack solution
class Solution(object):
    def sumSubarrayMins(self, A):
        """
        :type A: List[int]
        :rtype: int
        """
        M = 10**9 + 7

        left, s1 = [0]*len(A), []
        for i in range(len(A)):
            count = 1
            while s1 and s1[-1][0] > A[i]:
                count += s1.pop()[1]
            left[i] = count
            s1.append([A[i], count])

        right, s2 = [0]*len(A), []
        for i in reversed(range(len(A))):
            count = 1
            while s2 and s2[-1][0] >= A[i]:
                count += s2.pop()[1]
            right[i] = count
            s2.append([A[i], count])

        return sum(a*l*r for a, l, r in zip(A, left, right)) % M