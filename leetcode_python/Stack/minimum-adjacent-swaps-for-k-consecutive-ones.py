"""

1703. Minimum Adjacent Swaps for K Consecutive Ones
Hard

You are given an integer array, nums, and an integer k. nums comprises of only 0's and 1's. In one move, you can choose two adjacent indices and swap their values.

Return the minimum number of moves required so that nums has k consecutive 1's.

 

Example 1:

Input: nums = [1,0,0,1,0,1], k = 2
Output: 1
Explanation: In 1 move, nums could be [1,0,0,0,1,1] and have 2 consecutive 1's.
Example 2:

Input: nums = [1,0,0,0,0,0,1,1], k = 3
Output: 5
Explanation: In 5 moves, the leftmost 1 can be shifted right until nums = [0,0,0,0,0,1,1,1].
Example 3:

Input: nums = [1,1,0,1], k = 2
Output: 0
Explanation: nums already has 2 consecutive 1's.
 

Constraints:

1 <= nums.length <= 105
nums[i] is 0 or 1.
1 <= k <= sum(nums)

"""

# V0

# V1
# IDEA : SLIDING WINDOW + prefix sum
# https://leetcode.com/problems/minimum-adjacent-swaps-for-k-consecutive-ones/discuss/987347/JavaC%2B%2BPython-Solution
# IDEA :
# Find all index of nums[i] if nums[i] == 1.
# Now the problem changes to,
# find k consecute element in A,
# that has minimum distance to their median sequence.
# To solve this, we need to use the prefix sum of A,
# which is B in this solution.
class Solution:
     def minMoves(self, A, k):
            A = [i for i, a in enumerate(A) if a]
            n = len(A)
            B = [0] * (n + 1)
            res = float('inf')
            for i in range(n):
                B[i + 1] = B[i] + A[i]
            for i in xrange(len(A) - k + 1):
                res = min(res, B[i + k] - B[k / 2 + i] - B[(k + 1) / 2 + i] + B[i])
            res -= (k / 2) * ((k + 1) / 2)
            return res

# V1'
# IDEA : SLIDING WINDOW
# https://leetcode.com/problems/minimum-adjacent-swaps-for-k-consecutive-ones/discuss/987607/O(n)-explanation-with-picture
class Solution:
    def minMoves(self, nums: List[int], k: int) -> int:
        p = [i for i, v in enumerate(nums) if v == 1]
        # p[i]: the position of i-th 1
        n = len(p)
        presum = [0]*(n+1)
        # presum[i]: sum(p[0]...p[i-1])
        for i in range(n):
            presum[i+1] = presum[i]+p[i]

        res = inf

        # sliding window
        if k % 2 == 1:
            # if odd
            radius = (k-1)//2
            for i in range(radius, n-radius):
                # i-radius ... i ... i+radius
                # move radius to i
                # i+1, ..., i+radius
                right = presum[i+radius+1]-presum[i+1]
                # i-radius, ..., i-1
                left = presum[i]-presum[i-radius]
                res = min(res, right-left)
            return res-radius*(radius+1)
        else:
            # even
            radius = (k-2)//2
            for i in range(radius, n-radius-1):
                # i-radius ... i i+1 ... i+radius+1
                # move radius to i (moving to i+1 is also OK)
                # i+1, ..., i+radius+1
                right = presum[i+radius+2]-presum[i+1]
                # i-radius, ..., i-1
                left = presum[i]-presum[i-radius]
                res = min(res, right-left-p[i])
            return res-radius*(radius+1)-(radius+1)

# V1''
# IDEA : SLIDING WINDOW 
# https://leetcode.com/problems/minimum-adjacent-swaps-for-k-consecutive-ones/discuss/988056/Python-O(n)-Sliding-Window-with-detailed-explanation
class Solution:
    def minMoves(self, nums: List[int], k: int) -> int:
        # we use a rolling window, which keeps indices of consecutive k appearences of 1s
        # lets say the index of 1s in current window is A = [a0, a1, ... ak-1]
        # then we are trying to find an index x, such that
        # we transform A to consecutive indices [x, x+1, ... x+k-1].
        # Notice the moves needed is |a0 - x| + |a1 - (x+1)| + ... + |a(k-1) - (x+k-1)|
        # do a little transformation -> |a0-x| + |(a1-1) - x| + |(a(k-1) - (k-1)) - x|
        # it is obvious that when x is the median of [a0, a1-1, a2-2, ... a(k-1) - (k-1)]
        # the solution reach the minimum
        # Also, notice that if we shift A for any constant, that is for each i, ai -> ai+a,
        # the median x changes but the summation does not change
        # therefore, for ith occurence of 1, we subtract its index with k, ai -> ai-i.
        # To find the rolling maximum value of c = |a0-x| + |(a1-1) - x| + |(a(k-1) - (k-1)) - x|
        # we just need to update the value instead of recalculating. 
        # When we update the median from x -> y, for the first
        # half A we add (y-x) to each of them, for the second half of A we subtract (y-x)
        # and we need to remove |a0-x| and include |ak-y| where ak is the next index of 1
		
		# Time complexity: O(n), space complexity: O(n)
        
        ones = []
        for i in range(len(nums)):
            if nums[i] == 1:
                ones.append(i - len(ones))
                
        # rolling window
        cur = 0
        # calculate initial summation
        med = k//2
        for i in range(k):
            cur += abs(ones[i] - ones[med])
            
        res = cur
        # rolling window update 
        for i in range(1, len(ones) - k + 1):
            # find median of ones[i:i+k]
            new_med = k//2+i
            if k%2==0:
                # if k is odd, the sum of abs difference will not change excluding the first value
                cur += ones[new_med] - ones[med] 
            # update first value and last value
            cur -= ones[med] - ones[i-1]
            cur += ones[i+k-1] - ones[new_med]
            med = new_med
            res = min(res, cur)
        return res

# V1'''
# IDEA : SLIDING WINDOW
# https://leetcode.com/problems/minimum-adjacent-swaps-for-k-consecutive-ones/discuss/987362/Python-The-trick-and-related-problems
class Solution:
    def minMoves(self, nums: List[int], k: int) -> int:
        pos = [p for p,x in enumerate(nums) if x == 1]
        print(pos)

        pos = [p-i for i,p in enumerate(pos)]        
        print(pos)
        
        mid = k//2
        sizeleft = mid
        sizeright = k-1-sizeleft
        
        curleft = sum(abs(x - pos[mid]) for x in pos[:sizeleft])
        curright = sum(abs(x - pos[mid]) for x in pos[mid+1:k])        
        minres = curleft + curright
        
        print(curleft, curright, minres)
        
        for ptr in range(1, len(pos)-k+1):
            # print("ptr", ptr, ptr+mid, ptr+k)
            curleft -= (pos[ptr+mid-1] - pos[ptr-1])
            curleft += (pos[ptr+mid] - pos[ptr+mid-1])*sizeleft
        
            curright -= (pos[ptr+mid] - pos[ptr+mid-1])*sizeright
            curright += (pos[ptr+k-1] - pos[ptr+mid])
        
            minres = min(minres, curleft+curright)
            # print(curleft, curright, minres)  
        print()
        return minres

# V1''''
# IDEA : SLIDING WINDOW
# https://blog.51cto.com/u_15344287/3647241
class Solution:
    def minMoves(self, nums, k):
        # count distance between each "1"
        lst1 = []
        now = -1
        for num in nums:
            if num == 1:
                if now != -1:
                    lst1.append(now)
                now = 0
            else:
                if now != -1:
                    now += 1

        # edge case
        if k == 2:
            return min(lst1)

        # create small list for each part
        # [1,2(2次),3(3次),4] = [1,2] + [2,3] + [3,4]
        size = k // 2
        times = (k + 1) // 2
        now = sum(lst1[:size])
        lst2 = [now]
        for i in range(len(lst1) - size):
            now = now - lst1[i] + lst1[i + size]
            lst2.append(now)

        # sliding window
        ans = now = sum(lst2[:times])
        for i in range(len(lst2) - times):
            now = now - lst2[i] + lst2[i + times]
            ans = min(ans, now)

        return ans

# V1'''''''
# IDEA : SLIDING WINDOW
# https://zhuanlan.zhihu.com/p/448747037
class Solution(object):
    def minMoves(self, nums, k):
        p = []
        for index, num in enumerate(nums):
            if num == 1:
                p.append(index)   
        l = 0
        r = k - 1
        now = 0
        while l < r:
            now += p[r] - p[l]
            l += 1
            r -= 1      
        res = now   
        m = l
        l = 0
        r = k - 1
        res = now
        n = len(p)

        while r < n - 1:
            if k & 1:
                now += p[r + 1] - p[m + 1]
                now += p[l] - p[m]
            else:
                now += p[r + 1] - p[m]
                now += p[l] - p[m]
            res = min(res, now)
            l += 1
            r += 1
            m += 1
        
        res -= (1 + (k - 1) / 2) * ((k - 1) / 2)
        if k % 2 == 0:
            res -= k / 2
        
        return res

# V2