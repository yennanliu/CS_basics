"""

1296. Divide Array in Sets of K Consecutive Numbers
Medium

Given an array of integers nums and a positive integer k, check whether it is possible to divide this array into sets of k consecutive numbers.

Return true if it is possible. Otherwise, return false.

 

Example 1:

Input: nums = [1,2,3,3,4,4,5,6], k = 4
Output: true
Explanation: Array can be divided into [1,2,3,4] and [3,4,5,6].
Example 2:

Input: nums = [3,2,1,2,3,4,3,4,5,9,10,11], k = 3
Output: true
Explanation: Array can be divided into [1,2,3] , [2,3,4] , [3,4,5] and [9,10,11].
Example 3:

Input: nums = [1,2,3,4], k = 3
Output: false
Explanation: Each array should be divided in subarrays of size 3.
 

Constraints:

1 <= k <= nums.length <= 105
1 <= nums[i] <= 109
 

Note: This question is the same as 846: https://leetcode.com/problems/hand-of-straights/

"""

# V0

# V1
# IDEA : collections.Counter
# https://leetcode.com/problems/divide-array-in-sets-of-k-consecutive-numbers/discuss/470238/JavaC%2B%2BPython-Exactly-Same-as-846.-Hand-of-Straights
from collections import Counter
class Solution:
    def isPossibleDivide(self, A, k):
            c = collections.Counter(A)
            for i in sorted(c):
                if c[i] > 0:
                    for j in range(k)[::-1]:
                        c[i + j] -= c[i]
                        if c[i + j] < 0:
                            return False
            return True

# V1
# IDEA : collections.Counter
# https://leetcode.com/problems/divide-array-in-sets-of-k-consecutive-numbers/discuss/872280/python-solution
class Solution:
	def isPossibleDivide(self, nums: List[int], k: int) -> bool:
		cnt = collections.Counter(nums)
		while cnt:
			m = min(cnt)
			for i in range(m, m + k):
				if i not in cnt:
					return False
				elif cnt[i] == 1:
					del cnt[i]
				else:
					cnt[i] -= 1
		return True

# V1''
# IDEA : collections.Counter
# https://leetcode.com/problems/divide-array-in-sets-of-k-consecutive-numbers/discuss/796529/Python-simple-easy-to-understand-solution
class Solution:
    def isPossibleDivide(self, nums: List[int], k: int) -> bool:
        if len(nums) % k != 0:
            return False
        
        my_dict = defaultdict(int)
        for num in nums:
            my_dict[num]+=1
        numbers_left = len(nums)
        
        while numbers_left > 0:
            mini = min(my_dict.keys())
            for i in range(k):
                if not my_dict.get(mini+i):
                    return False
                my_dict[mini+i]-=1
                numbers_left -= 1
                if my_dict[mini+i] == 0:
                    del my_dict[mini+i]
        return True

# V1'''
# IDEA : collections.Counter
# https://leetcode.com/problems/divide-array-in-sets-of-k-consecutive-numbers/discuss/644577/Python-or-StraightForward-or-Simple
from collections import Counter
class Solution:
    def isPossibleDivide(self, nums: List[int], k: int) -> bool:
        d = Counter(nums)
        for x in sorted(nums):
            if x in d:
                for y in range(x, x + k):
                    if y in d:
                        d[y] -= 1
                        if d[y] == 0:
                            del d[y]
                    else:
                        return False
        return True

# V1''''
# IDEA : collections.Counter
# https://leetcode.com/problems/divide-array-in-sets-of-k-consecutive-numbers/discuss/457625/Python-Counter-Solution
from collections import Counter
class Solution:
    def isPossibleDivide(self, nums: List[int], k: int) -> bool:
        count = Counter(nums)
        for n in sorted(count):
            if count[n] > 0:
                need = count[n]
                for i in range(n,n+k):
                    if count[i] < need:
                        return False
                    count[i] -= need
        return True

# V2