"""

775. Global and Local Inversions
Medium

You are given an integer array nums of length n which represents a permutation of all the integers in the range [0, n - 1].

The number of global inversions is the number of the different pairs (i, j) where:

0 <= i < j < n
nums[i] > nums[j]
The number of local inversions is the number of indices i where:

0 <= i < n - 1
nums[i] > nums[i + 1]
Return true if the number of global inversions is equal to the number of local inversions.

 

Example 1:

Input: nums = [1,0,2]
Output: true
Explanation: There is 1 global inversion and 1 local inversion.
Example 2:

Input: nums = [1,2,0]
Output: false
Explanation: There are 2 global inversions and 1 local inversion.
 

Constraints:

n == nums.length
1 <= n <= 105
0 <= nums[i] < n
All the integers of nums are unique.
nums is a permutation of all the numbers in the range [0, n - 1].

"""

# V0
class Solution(object):
    def isIdealPermutation(self, A):
        for idx, value in enumerate(A):
            if abs(idx - value) > 1:
                return False
        return True

# V0'
# IDEA : a local inversion MUST a global inversion as well,
#     -> so if we want  (# of local inversion) = (# of global inversion)
#     -> ALL global inversion MUST be local inversion as well
#     -> SO for any j > i+1
#     ->   we CAN'T have A[i] > A[j] -> A[i] <= A[j] for all i, j
class Solution(object):
    def isIdealPermutation(self, A):
        cmax = 0
        for i in range(len(A) - 2):
            cmax = max(cmax, A[i])
            if cmax > A[i + 2]:
                return False
        return True

# V1
# https://blog.csdn.net/fuxuemingzhu/article/details/82915149
class Solution(object):
    def isIdealPermutation(self, A):
        """
        :type A: List[int]
        :rtype: bool
        """
        for i, a in enumerate(A):
            if abs(a - i) > 1:
                return False
        return True

### Test case : dev 

# V1'
# https://blog.csdn.net/fuxuemingzhu/article/details/82915149
class Solution(object):
    def isIdealPermutation(self, A):
        """
        :type A: List[int]
        :rtype: bool
        """
        cmax = 0
        for i in range(len(A) - 2):
            cmax = max(cmax, A[i])
            if cmax > A[i + 2]:
                return False
        return True

# V1'''
# https://leetcode.com/problems/global-and-local-inversions/discuss/635114/Python-intuitive-approach
class Solution(object):
    def isIdealPermutation(self, A: List[int]) -> bool:
        for idx, value in enumerate(A):
            if abs(idx - value) > 1:
                return False
        return True


# V1'''
# http://bookshadow.com/weblog/2018/01/28/leetcode-global-and-local-inversions/
# JAVA

# V1''''
# https://leetcode.com/problems/global-and-local-inversions/discuss/535557/775-Global-and-Local-Inversions-Py-All-in-One-by-Talse
class Solution(object):
    def isIdealPermutation(self, A: List[int]) -> bool:
            mx = 0
            for i in range(len(A) - 2):
                mx = max(mx, A[i])
                if mx > A[i+2]: return False
            return True

# V1'''
# http://bookshadow.com/weblog/2018/01/28/leetcode-global-and-local-inversions/
class Solution(object):
    def isIdealPermutation(self, A: List[int]) -> bool:
            return all(abs(A[i] - i) <= 1 for i in range(len(A)))

# V2
# https://github.com/kamyu104/LeetCode-Solutions/blob/master/Python/global-and-local-inversions.py
# Time:  O(n)
# Space: O(1)
class Solution(object):
    def isIdealPermutation(self, A):
        """
        :type A: List[int]
        :rtype: bool
        """
        return all(abs(v-i) <= 1 for i,v in enumerate(A))