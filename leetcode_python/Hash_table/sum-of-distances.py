"""

2615. Sum of Distances
Solved
Medium
Topics
premium lock icon
Companies
Hint
You are given a 0-indexed integer array nums.

There exists an array arr of length nums.length, where arr[i] is the sum of |i - j| over all j such that nums[j] == nums[i] and j != i. If there is no such j, set arr[i] to be 0.

Return the array arr.

 

Example 1:

Input: nums = [1,3,1,1,2]
Output: [5,0,3,4,0]
Explanation: 
When i = 0, nums[0] == nums[2] and nums[0] == nums[3]. Therefore, arr[0] = |0 - 2| + |0 - 3| = 5. 
When i = 1, arr[1] = 0 because there is no other index with value 3.
When i = 2, nums[2] == nums[0] and nums[2] == nums[3]. Therefore, arr[2] = |2 - 0| + |2 - 3| = 3. 
When i = 3, nums[3] == nums[0] and nums[3] == nums[2]. Therefore, arr[3] = |3 - 0| + |3 - 2| = 4. 
When i = 4, arr[4] = 0 because there is no other index with value 2. 

Example 2:

Input: nums = [0,5,3]
Output: [0,0,0]
Explanation: Since each element in nums is distinct, arr[i] = 0 for all i.
 

Constraints:

1 <= nums.length <= 105
0 <= nums[i] <= 109
 

Note: This question is the same as 2121: Intervals Between Identical Elements.


"""


# V0
# IDEA: PREFIX SUM + LEFT, RIGHT dist
from collections import defaultdict

class Solution(object):
    def distance(self, nums):
        """
        :type nums: List[int]
        :rtype: List[int]
        """
        n = len(nums)
        res = [0] * n
        c_map = defaultdict(list)

        # 1. 將相同數值的索引分組 (索引會自然維持遞增)
        for i, val in enumerate(nums):
            c_map[val].append(i)

        # 2. 對於每一組相同的數值，使用前後綴累加計算距離
        for val, indices in c_map.items():
            k = len(indices)
            if k <= 1:
                continue

            total_sum = sum(indices)
            left_sum = 0

            for p, idx in enumerate(indices):
                # 動態算出右側索引和
                right_sum = total_sum - left_sum - idx

                left_count = p
                right_count = k - 1 - p

                # 左右距離和計算
                left_dist = left_count * idx - left_sum
                right_dist = right_sum - right_count * idx

                res[idx] = left_dist + right_dist

                # 更新 left_sum 供下一個索引使用
                left_sum += idx

        return res


# V0-0-1
# IDEA: PREFIX SUM + LEFT, RIGHT dist (gpt)
from collections import defaultdict

class Solution(object):
    def distance(self, nums):
        """
        :type nums: List[int]
        :rtype: List[int]
        """

        n = len(nums)

        # {value: [index1, index2, ...]}
        c_map = defaultdict(list)

        for i in range(n):
            c_map[nums[i]].append(i)

        res = [0] * n

        # Process each value independently
        for k in c_map:

            indices = c_map[k]

            if len(indices) <= 1:
                continue

            m = len(indices)

            # prefix[i] = sum of indices[0:i]
            prefix = [0] * (m + 1)

            for j in range(m):
                prefix[j + 1] = prefix[j] + indices[j]

            # Calculate answer for every occurrence of this value
            for i in range(m):

                cur = indices[i]

                # Number of same values on the left
                left_cnt = i

                # Number of same values on the right
                right_cnt = m - i - 1

                # Sum of distances to the left
                left = cur * left_cnt - prefix[i]

                # Sum of distances to the right
                right = (prefix[m] - prefix[i + 1]) - cur * right_cnt

                res[cur] = left + right

        return res


# V1-1
# IDEA: PREFIX SUM (gpt)
"""
CORE IDEA:


1. 

	HashMap
	   ↓
	value → 所有出現位置
	   ↓
	Prefix Sum
	   ↓
	左邊距離 + 右邊距離
	   ↓
	O(N)



2. 


	left  = cur * i - prefix[i]

	right = (total - prefix[i+1])
	        - cur * (m-i-1)



3. explaination of #2


(`p` idx on the left)

left = 
	(idx - i0) + (idx - i1) + ..... (idx - ip-1) = idx * p - left_sum


(`k-1-p` idx on the left)

right = 
	 (ip+1 - idx) + .... + (ik-1 - idx) = right_sum - (k-1-(p+1) + 1) * idx
	 									= right_sum - (k - p - 1)


"""
from collections import defaultdict

class Solution(object):

    def distance(self, nums):
        """
        :type nums: List[int]
        :rtype: List[int]
        """

        n = len(nums)

        # {value: [index1, index2, ...]}
        c_map = defaultdict(list)

        for i in range(n):
            val = nums[i]
            c_map[val].append(i)

        # answer for every index
        res = [0] * n

        # Process each value independently
        for val in c_map:

            indices = c_map[val]

            # No need to calculate if value appears only once
            if len(indices) <= 1:
                continue

            m = len(indices)

            # prefix[j] = sum of indices[0:j]
            prefix = [0] * (m + 1)

            for i in range(m):
                prefix[i + 1] = prefix[i] + indices[i]

            # Calculate distance for each occurrence
            for i in range(m):

                cur = indices[i]

                # Left side:
                # cur - indices[0]
                # + cur - indices[1]
                # + ...
                #
                # = cur * i - prefix[i]
                left = cur * i - prefix[i]

                # Right side:
                # indices[i+1] - cur
                # + indices[i+2] - cur
                # + ...
                #
                # = (prefix[m] - prefix[i+1]) - cur * (m-i-1)
                right = (
                    prefix[m]
                    - prefix[i + 1]
                    - cur * (m - i - 1)
                )

                res[cur] = left + right

        return res


# V1-2
# IDEA: PREFIX SUM (GEMINI)
from collections import defaultdict

class Solution(object):
    def distance(self, nums):
        """
        :type nums: List[int]
        :rtype: List[int]
        """
        n = len(nums)
        res = [0] * n
        c_map = defaultdict(list)

        # 1. 將相同數值的索引分組 (索引會自然維持遞增)
        for i, val in enumerate(nums):
            c_map[val].append(i)

        # 2. 對於每一組相同的數值，使用前後綴累加計算距離
        for val, indices in c_map.items():
            k = len(indices)
            if k <= 1:
                continue

            total_sum = sum(indices)
            left_sum = 0

            for p, idx in enumerate(indices):
                # 動態算出右側索引和
                right_sum = total_sum - left_sum - idx

                left_count = p
                right_count = k - 1 - p

                # 左右距離和計算
                left_dist = left_count * idx - left_sum
                right_dist = right_sum - right_count * idx

                res[idx] = left_dist + right_dist

                # 更新 left_sum 供下一個索引使用
                left_sum += idx

        return res


# V2
# IDEA: PREFIX SUM
# https://leetcode.com/problems/sum-of-distances/editorial/
class Solution:
    def distance(self, nums: list[int]) -> list[int]:
        n = len(nums)
        groups = defaultdict(list)
        for i, v in enumerate(nums):
            groups[v].append(i)
        res = [0] * n
        for group in groups.values():
            total = sum(group)
            prefix_total = 0
            sz = len(group)
            for i, idx in enumerate(group):
                res[idx] = total - prefix_total * 2 + idx * (2 * i - sz)
                prefix_total += idx
        return res
