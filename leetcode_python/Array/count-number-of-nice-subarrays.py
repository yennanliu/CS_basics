"""

1248. Count Number of Nice Subarrays
Medium

Given an array of integers nums and an integer k. A continuous subarray is called nice if there are k odd numbers on it.

Return the number of nice sub-arrays.

 

Example 1:

Input: nums = [1,1,2,1,1], k = 3
Output: 2
Explanation: The only sub-arrays with 3 odd numbers are [1,1,2,1] and [1,2,1,1].
Example 2:

Input: nums = [2,4,6], k = 1
Output: 0
Explanation: There is no odd numbers in the array.
Example 3:

Input: nums = [2,2,2,1,2,2,1,2,2,2], k = 2
Output: 16
 

Constraints:

1 <= nums.length <= 50000
1 <= nums[i] <= 10^5
1 <= k <= nums.length

"""

# V0
# IDEA : cumsum + dict (Prefix sum)
class Solution:
    def numberOfSubarrays(self, nums, k):
        d = collections.defaultdict(int)
        d[0] = 1
        cur_sum = 0
        res = 0
        for i in nums:
            if i % 2 == 1:
                cur_sum += 1
            if cur_sum - k in d:
                res += d[cur_sum - k]
            d[cur_sum] += 1
        return res

# V1
# IDEA : cumsum + dict (Prefix sum)
# https://leetcode.com/problems/count-number-of-nice-subarrays/discuss/868119/python-solution
class Solution:
    def numberOfSubarrays(self, nums, k):
        d = collections.defaultdict(int)
        d[0] = 1
        cur_sum = 0
        res = 0
        for i in nums:
            if i % 2 == 1:
                cur_sum += 1
            if cur_sum - k in d:
                res += d[cur_sum - k]
            d[cur_sum] += 1
        return res

# V1''
# IDEA : exact k
# https://leetcode.com/problems/count-number-of-nice-subarrays/discuss/419668/Python-3-ways-summarize
# IDEA :
# Convert "exact k" to "at most k". O(n) time O(1) space
# Lee215's way. We are looking for result of "exact k", we can calculate "at most k" first, and final result will be: "exact k" == "atmost k" - "atmost k-1".
class Solution:
    def numberOfSubarrays(self, nums: List[int], k: int) -> int:
            l=len(nums)
            def atmost(k):
                res=0
                cnt=0
                j=0
                for i in range(l):
                    if nums[i]%2==1:
                        cnt+=1
                    while cnt>k:
                        if nums[j]%2==1:
                            cnt-=1
                        j+=1
                    res+=i-j+1
                return res
            return atmost(k)-atmost(k-1)

# V1'''
# IDEA : deque
# https://leetcode.com/problems/count-number-of-nice-subarrays/discuss/419668/Python-3-ways-summarize
# IDEA :
# Record all odd indexes. O(n) time O(n) space
# Record indexes of all odd numbers in a deque. If len(que)==k+1, that means we have k+1 odd numbers now, all subarray starting between que[0] and que[1], ending at current index i, are valid sub arrays.
class Solution:
    def numberOfSubarrays(self, nums: List[int], k: int) -> int:
            res=0
            que=collections.deque([-1])
            for i,n in enumerate(nums):
                if n%2==1:
                    que.append(i)
                if len(que)==k+2:
                    que.popleft()
                if len(que)==k+1:
                    res+=que[1]-que[0]
            return res

# V1'''''
# IDEA : prefix sum
# https://leetcode.com/problems/count-number-of-nice-subarrays/discuss/419668/Python-3-ways-summarize
# IDEA
# Prefix sum. O(n) time O(n) space
# Odd number being 1, even number being 0, calculate prefix sum of the whole nums. "visited" is a dictionary, key being prefix, value being the repeating count of that prefix.
# Prefix sum is an increasing sequence, so visited[p] will always represent the prefix happened before current index.
class Solution:
    def numberOfSubarrays(self, nums: List[int], k: int) -> int:
            res=cursum=0
            visited=collections.defaultdict(int)
            for v in nums:
                visited[cursum]+=1
                cursum+=v%2
                if cursum-k in visited:
                    res+=visited[cursum-k]
            return res

# V1'''''''''
# https://leetcode.com/problems/count-number-of-nice-subarrays/discuss/421157/Simple-Python-solution
class Solution:
    def numberOfSubarrays(self, nums, k):
        memo = {0:-1}
        ans = cur = 0
        for i in range(len(nums)):
            if nums[i] % 2:
                cur += 1
                memo[cur] = i
            if cur >= k:
                ans += memo[cur + 1 - k] - memo[cur - k]
        return ans

# V1'''''''''
# IDEA : LC 828 + windows
# https://leetcode.com/problems/count-number-of-nice-subarrays/discuss/421003/Python-straightforward-solution
# https://leetcode-cn.com/problems/count-number-of-nice-subarrays/
# IDEA :  Straightforward Solution: similar to the idea in the solution for 828. Unique Letter String
class Solution:
    def numberOfSubarrays(self, nums, k):
            lst = [-1]
            for i in range(len(nums)):
                if nums[i] % 2:
                    lst.append(i)
            lst.append(len(nums))
            res = 0
            for i in range(1, len(lst) - k):
                # plus the number of windows containing [lst[i], lst[i+k-1]]
                res += (lst[i] - lst[i-1]) * (lst[i+k] - lst[i+k-1])
            return res

# V1''''''''''
# https://leetcode.com/problems/count-number-of-nice-subarrays/discuss/420390/Simple-Python-Solution
class Solution:
    def numberOfSubarrays(self, nums, k) :
        odd = [i for i in range(len(nums)) if nums[i] % 2 == 1]
        if len(odd) < k: return 0
        res = 0
        odd = [-1] + odd + [len(nums)]
        for i in range(1, len(odd) - k):
            res += (odd[i]- odd[i-1]) * (odd[i + k] - odd[i + k - 1])
        return res

# V1'''''''''''
# IDEA : AT MOST
# https://leetcode.com/problems/count-number-of-nice-subarrays/discuss/419378/JavaC%2B%2BPython-Sliding-Window-O(1)-Space
class Solution:
    def numberOfSubarrays(self, A, k):
        def atMost(k):
            res = i = 0
            for j in range(len(A)):
                k -= A[j] % 2
                while k < 0:
                    k += A[i] % 2
                    i += 1
                res += j - i + 1
            return res

        return atMost(k) - atMost(k - 1)

# V1''''''''''''
# IDEA : ONE PASS
# https://leetcode.com/problems/count-number-of-nice-subarrays/discuss/419378/JavaC%2B%2BPython-Sliding-Window-O(1)-Space
class Solution:
    def numberOfSubarrays(self, A, k):
        i = count = res = 0
        for j in range(len(A)):
            if A[j] & 1:
                k -= 1
                count = 0
            while k == 0:
                k += A[i] & 1
                i += 1
                count += 1
            res += count
        return res

# V1''''''''''''''
# https://littlebees.github.io/2021/08/leetcode-1248/
class Solution:
    def numberOfSubarrays(self, nums: List[int], k: int) -> int:
        def atmost(limit):
            i = cnt = ret = 0
            for j,n in enumerate(nums):
                if n % 2 == 1:
                    cnt += 1
                while i <= j and cnt > limit:
                    if nums[i] % 2 == 1:
                        cnt -= 1
                    i += 1
                ret += j-i+1
            return ret
        return atmost(k)-atmost(k-1)

# V1'''''''''''''''
# https://littlebees.github.io/2021/08/leetcode-1248/
class Solution:
    def numberOfSubarrays(self, A, k):
        i = count = res = 0
        for j in range(len(A)):
            if A[j] & 1:
                k -= 1
                count = 0 # get len till meet the condition
            while k == 0:
                k += A[i] & 1
                i += 1
                count += 1
            res += count
        return res

# V2