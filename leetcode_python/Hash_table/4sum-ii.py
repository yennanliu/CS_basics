"""

454. 4Sum II
Medium

Given four integer arrays nums1, nums2, nums3, and nums4 all of length n, return the number of tuples (i, j, k, l) such that:

0 <= i, j, k, l < n
nums1[i] + nums2[j] + nums3[k] + nums4[l] == 0
 

Example 1:

Input: nums1 = [1,2], nums2 = [-2,-1], nums3 = [-1,2], nums4 = [0,2]
Output: 2
Explanation:
The two tuples are:
1. (0, 0, 0, 1) -> nums1[0] + nums2[0] + nums3[0] + nums4[1] = 1 + (-2) + (-1) + 2 = 0
2. (1, 1, 0, 0) -> nums1[1] + nums2[1] + nums3[0] + nums4[0] = 2 + (-1) + (-1) + 0 = 0
Example 2:

Input: nums1 = [0], nums2 = [0], nums3 = [0], nums4 = [0]
Output: 1
 

Constraints:

n == nums1.length
n == nums2.length
n == nums3.length
n == nums4.length
1 <= n <= 200
-228 <= nums1[i], nums2[i], nums3[i], nums4[i] <= 228

"""

# V0 

# V1 
# https://blog.csdn.net/danspace1/article/details/87854254
# http://bookshadow.com/weblog/2016/11/13/leetcode-4sum-ii/
class Solution(object):
    def fourSumCount(self, A, B, C, D):
        ans = 0
        cnt = collections.defaultdict(int)
        for a in A:
            for b in B:
                cnt[a + b] += 1
        for c in C:
            for d in D:
                ans += cnt[-(c + d)]
        return ans

# V1'
# https://www.jiuzhang.com/solution/4sum-ii/#tag-highlight-lang-python
class Solution:
    def fourSumCount(self, A, B, C, D):
        # Write your code here
        map = {}
        ans = 0
        for i in range(len(A)):
            for j in range(len(B)):
                sum = A[i] + B[j]
                if sum in map:
                    map[sum] += 1
                else:
                    map[sum] = 1
        for i in range(len(C)):
            for j in range(len(D)):
                sum = -(C[i] + D[j])
                if sum in map:
                    ans += map[sum]
        return ans

# V1'
# https://leetcode.com/problems/4sum-ii/discuss/761528/Python-solution
class Solution:
    def fourSumCount(self, A, B, C, D):
        count = 0
        num_dict = {}
        for c in C:
            for d in D:
                nsum = c + d
                if nsum in num_dict:
                    num_dict[nsum] += 1
                else:
                    num_dict[nsum] = 1

        for a in A:
            for b in B:
                target = 0 - (a+b)
                if target in num_dict:
                    count += num_dict[target]
        return count

# V1''
# https://leetcode.com/problems/4sum-ii/discuss/432974/python
class Solution:
    def fourSumCount(self, A, B, C, D):
        ab = {}
        for a in A:
            for b in B:
                ab[a+b] = ab.get(a+b, 0) + 1
        ans = 0
        for c in C:
            for d in D:
                ans = ans + ab.get(-(c+d), 0)
        return ans

# V1'''
# https://leetcode.com/problems/4sum-ii/discuss/331940/Python-O(n2)-with-hashing
class Solution:
    def fourSumCount(self, A, B, C, D):
        twosum = collections.defaultdict(int)
        res = 0
        for x in A:
            for y in B:
                z = x + y
                twosum[z] += 1
        for x in C:
            for y in D:
                z = -(x + y)
                if z in twosum:
                    res += twosum[z]
        return res

# V2 
# Time:  O(n^2)
# Space: O(n^2)
import collections
class Solution(object):
    def fourSumCount(self, A, B, C, D):
        A_B_sum = collections.Counter(a+b for a in A for b in B)
        return sum(A_B_sum[-c-d] for c in C for d in D)