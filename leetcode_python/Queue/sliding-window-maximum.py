"""

239. Sliding Window Maximum
Hard

You are given an array of integers nums, there is a sliding window of size k which is moving from the very left of the array to the very right. You can only see the k numbers in the window. Each time the sliding window moves right by one position.

Return the max sliding window.

 
Example 1:

Input: nums = [1,3,-1,-3,5,3,6,7], k = 3
Output: [3,3,5,5,6,7]
Explanation: 
Window position                Max
---------------               -----
[1  3  -1] -3  5  3  6  7       3
 1 [3  -1  -3] 5  3  6  7       3
 1  3 [-1  -3  5] 3  6  7       5
 1  3  -1 [-3  5  3] 6  7       5
 1  3  -1  -3 [5  3  6] 7       6
 1  3  -1  -3  5 [3  6  7]      7
Example 2:

Input: nums = [1], k = 1
Output: [1]
Example 3:

Input: nums = [1,-1], k = 1
Output: [1,-1]
Example 4:

Input: nums = [9,11], k = 2
Output: [11]
Example 5:

Input: nums = [4,-2], k = 2
Output: [4]
 

Constraints:

1 <= nums.length <= 105
-104 <= nums[i] <= 104
1 <= k <= nums.length

"""

# V0
# IDEA : DEQUE
class Solution:
    def maxSlidingWindow(self, nums, k):
        q = collections.deque()
        ans = []
        for i in range(len(nums)):
            while q and nums[q[-1]] <= nums[i]:
                q.pop()
            q.append(i)
            if q[0] == i - k:
                q.popleft()
            if i >= k - 1:
                ans.append(nums[q[0]])
        return ans

# V1
# http://bookshadow.com/weblog/2015/07/18/leetcode-sliding-window-maximum/
# IDEA : DEQUE
class Solution:
    # @param {integer[]} nums
    # @param {integer} k
    # @return {integer[]}
    def maxSlidingWindow(self, nums, k):
        dq = collections.deque()
        ans = []
        for i in range(len(nums)):
            while dq and nums[dq[-1]] <= nums[i]:
                dq.pop()
            dq.append(i)
            if dq[0] == i - k:
                dq.popleft()
            if i >= k - 1:
                ans.append(nums[dq[0]])
        return ans

### Test case : dev 

# V1'
# IDEA : heapq
# https://leetcode.com/problems/sliding-window-maximum/discuss/65957/Python-solution-with-detailed-explanation
import heapq as h
class Solution(object):
    def get_next_max(self, heap, start):
        while True:
            x,idx = h.heappop(heap)
            if idx >= start:
                return x*-1, idx
    
    def maxSlidingWindow(self, nums, k):
        """
        :type nums: List[int]
        :type k: int
        :rtype: List[int]
        """
        if k == 0:
            return []
        heap = []
        for i in range(k):
            h.heappush(heap, (nums[i]*-1, i))
        result, start, end = [], 0, k-1
        while end < len(nums):
            x, idx = self.get_next_max(heap, start)
            result.append(x)
            h.heappush(heap, (x*-1, idx)) 
            start, end = start + 1, end + 1
            if end < len(nums):
                h.heappush(heap, (nums[end]*-1, end))
        return result

# V1''
# IDEA : heapq
# https://leetcode.com/problems/sliding-window-maximum/discuss/65957/Python-solution-with-detailed-explanation
from collections import deque
class Solution(object):
    def add_to_dq(self, dq, nums, i):
        while len(dq) and nums[dq[-1]] <= nums[i]:
            dq.pop()
        dq.append(i)
        return

    def maxSlidingWindow(self, nums, k):
        """
        :type nums: List[int]
        :type k: int
        :rtype: List[int]
        """
        if k == 0:
            return []
        dq = deque()
        for i in range(k):
            self.add_to_dq(dq, nums, i)
        result, start, end = [], 0, k-1
        while end < len(nums):
            while True:
                if dq[0] >= start:
                    result.append(nums[dq[0]])
                    break
                else:
                    dq.popleft()
            start, end = start+1,end+1
            if end < len(nums):
                self.add_to_dq(dq, nums, end)
        return result

# V1'''
# IDEA : Use a Hammer (Bruteforce)
# https://leetcode.com/problems/sliding-window-maximum/solution/
class Solution:
    def maxSlidingWindow(self, nums: 'List[int]', k: 'int') -> 'List[int]':
        n = len(nums)
        if n * k == 0:
            return []
        
        return [max(nums[i:i + k]) for i in range(n - k + 1)]

# V1''''
# IDEA : Deque
# https://leetcode.com/problems/sliding-window-maximum/solution/
from collections import deque
class Solution:
    def maxSlidingWindow(self, nums: 'List[int]', k: 'int') -> 'List[int]':
        # base cases
        n = len(nums)
        if n * k == 0:
            return []
        if k == 1:
            return nums
        
        def clean_deque(i):
            # remove indexes of elements not from sliding window
            if deq and deq[0] == i - k:
                deq.popleft()
                
            # remove from deq indexes of all elements 
            # which are smaller than current element nums[i]
            while deq and nums[i] > nums[deq[-1]]:
                deq.pop()
        
        # init deque and output
        deq = deque()
        max_idx = 0
        for i in range(k):
            clean_deque(i)
            deq.append(i)
            # compute max in nums[:k]
            if nums[i] > nums[max_idx]:
                max_idx = i
        output = [nums[max_idx]]
        
        # build output
        for i in range(k, n):
            clean_deque(i)          
            deq.append(i)
            output.append(nums[deq[0]])
        return output

# V1''''''
# IDEA : Dynamic programming
# https://leetcode.com/problems/sliding-window-maximum/solution/
class Solution:
    def maxSlidingWindow(self, nums: 'List[int]', k: 'int') -> 'List[int]':
        n = len(nums)
        if n * k == 0:
            return []
        if k == 1:
            return nums
        
        left = [0] * n
        left[0] = nums[0]
        right = [0] * n
        right[n - 1] = nums[n - 1]
        for i in range(1, n):
            # from left to right
            if i % k == 0:
                # block start
                left[i] = nums[i]
            else:
                left[i] = max(left[i - 1], nums[i])
            # from right to left
            j = n - i - 1
            if (j + 1) % k == 0:
                # block end
                right[j] = nums[j]
            else:
                right[j] = max(right[j + 1], nums[j])
        
        output = []
        for i in range(n - k + 1):
            output.append(max(left[i + k - 1], right[i]))
            
        return output

# V1'''''''
# IDEA : DEQUE
# https://leetcode.com/problems/sliding-window-maximum/discuss/823635/Python-Intuitive-Solution
from collections import deque
class Solution:
    def maxSlidingWindow(self, nums, k):
        if k > len(nums) or not nums:
            return []
        
        ret = []
        q = deque()
        
        for i in range(len(nums)):
            # remove everything from the back that is <= the current num
            while q and q[-1][0] <= nums[i]:
                q.pop()
            # add the new num to the back
            q.append((nums[i], i))
            # remove everything from the front if it's not in the window
            while q[0][1] <= i - k:
                q.popleft()
            # start adding results to output array once we have our first size k window
            if i >= k - 1:
                ret.append(q[0][0])
        
        return ret

# V1''''''''''''
# IDEA : monoclic queue
# https://leetcode.com/problems/sliding-window-maximum/discuss/952539/Python-mono-queue
from collections import deque
class Solution:
    def maxSlidingWindow(self, nums, k):
        ans = []
        mono = deque([])
        for i,e in enumerate(nums):
            if mono and i-mono[0][0]>=k:
                mono.popleft()
            while mono and mono[-1][1]<e:
                mono.pop()
            mono.append((i,e))
            if i>=k-1:
                ans.append(mono[0][1])
        return ans

# V1'''''''''''
# https://blog.csdn.net/PKU_Jade/article/details/77934644
# IDEA : DEQUE
class Solution(object):
    def maxSlidingWindow(self, nums, k):
        """
        :type nums: List[int]
        :type k: int
        :rtype: List[int]
        """
        if not nums:
            return []
        from collections import deque
        dq = deque()
        n = len(nums)
        ans = []

        for i in range(k-1):
        	# push k-1 elements into queue
            while len(dq) > 0:
                if nums[dq[-1]] <= nums[i]:
                    dq.pop()
                else:
                    break
            dq.append(i)  

        for i in range(k-1, n):
            while len(dq) > 0:
                if nums[dq[-1]] <= nums[i]:
                    dq.pop()
                else:
                    break
            dq.append(i)

            while dq[0] < i-k+1:
                dq.popleft()
            ans.append(nums[dq[0]])

        return ans

# V1''''''''''''
# https://blog.csdn.net/fuxuemingzhu/article/details/100828798
# IDEA : DEQUE
class Solution(object):
    def maxSlidingWindow(self, nums, k):
        """
        :type nums: List[int]
        :type k: int
        :rtype: List[int]
        """
        que = collections.deque() # [[i, num]]
        res = []
        for i, num in enumerate(nums):
            if que and i - que[0][0] >= k:
                que.popleft()
            while que and que[-1][1] <= num:
                que.pop()
            que.append([i, num])
            if i >= k - 1:
                res.append(que[0][1])
        return res

# V2 