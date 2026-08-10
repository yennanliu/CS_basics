# https://leetcode.com/problems/most-frequent-ids/description/

"""

3092. Most Frequent IDs
Solved
Medium
Topics
premium lock icon
Companies
Hint
The problem involves tracking the frequency of IDs in a collection that changes over time. You have two integer arrays, nums and freq, of equal length n. Each element in nums represents an ID, and the corresponding element in freq indicates how many times that ID should be added to or removed from the collection at each step.

Addition of IDs: If freq[i] is positive, it means freq[i] IDs with the value nums[i] are added to the collection at step i.
Removal of IDs: If freq[i] is negative, it means -freq[i] IDs with the value nums[i] are removed from the collection at step i.
Return an array ans of length n, where ans[i] represents the count of the most frequent ID in the collection after the ith step. If the collection is empty at any step, ans[i] should be 0 for that step.

 

Example 1:

Input: nums = [2,3,2,1], freq = [3,2,-3,1]

Output: [3,3,2,2]

Explanation:

After step 0, we have 3 IDs with the value of 2. So ans[0] = 3.
After step 1, we have 3 IDs with the value of 2 and 2 IDs with the value of 3. So ans[1] = 3.
After step 2, we have 2 IDs with the value of 3. So ans[2] = 2.
After step 3, we have 2 IDs with the value of 3 and 1 ID with the value of 1. So ans[3] = 2.

Example 2:

Input: nums = [5,5,3], freq = [2,-2,1]

Output: [2,0,1]

Explanation:

After step 0, we have 2 IDs with the value of 5. So ans[0] = 2.
After step 1, there are no IDs. So ans[1] = 0.
After step 2, we have 1 ID with the value of 3. So ans[2] = 1.

 

Constraints:

1 <= nums.length == freq.length <= 105
1 <= nums[i] <= 105
-105 <= freq[i] <= 105
freq[i] != 0
The input is generated such that the occurrences of an ID will not be negative in any step.


"""

# V0
# IDEA: PQ + `Lazy Deletion` + hashmap (gemini)
import heapq

class Solution(object):
    def mostFrequentIDs(self, nums, freq):
        """
        :type nums: List[int]
        :type freq: List[int]
        :rtype: List[int]
        """
        c_map = {}  # Tracks the absolute TRUE frequency of each ID
        pq = []     # Max-Heap storing tuples of (-frequency, ID)
        
        n = len(nums)
        ans = [0] * n
        
        for i in range(n):
            val = nums[i]
            cnt = freq[i]
            
            # 1. Update the true frequency in the hash map
            c_map[val] = c_map.get(val, 0) + cnt
            
            # 2. Push the updated frequency to the heap. 
            # (We do NOT delete the old frequency from the heap yet)
            # 
            # NOTE !!! 
            # -> push the updated cnt (-c_map[val])
            heapq.heappush(pq, (-c_map[val], val))
            
            """
            NOTE !!!!

            how we do `lazy delete`


            -> we ONLY do delete `till we reach a correct cnt one`
            -> e.g. we leave ALL other cnt (pq element) unchanged
            	-> we ONLY do `lazy delete` till the needed idx
            """
            # 3. Lazy Deletion: Check if the top of the heap is stale.
            # If the frequency at the top of the heap doesn't match the true 
            # frequency in our hash map, it's outdated data. Pop it!
            while pq and -pq[0][0] != c_map[pq[0][1]]:
                heapq.heappop(pq)
                

            """
            NOTE !!!


            in py, we get top element from PQ via below:


            ```
            pq[0]
            ``
            """
            # 4. The top of the heap is now guaranteed to be accurate.
            ans[i] = -pq[0][0] if pq else 0
            
        return ans


# V0-1
# IDEA: PQ + `Lazy Deletion` + hashmap (GPT)
import heapq

class Solution(object):
    def mostFrequentIDs(self, nums, freq):
        """
        :type nums: List[int]
        :type freq: List[int]
        :rtype: List[int]
        """
        cnt = {}          # id -> current frequency
        heap = []         # (-frequency, id)
        ans = []

        for x, delta in zip(nums, freq):
            cnt[x] = cnt.get(x, 0) + delta

            heapq.heappush(heap, (-cnt[x], x))

            # Remove outdated entries
            while heap and -heap[0][0] != cnt[heap[0][1]]:
                heapq.heappop(heap)

            if heap:
                ans.append(-heap[0][0])
            else:
                ans.append(0)

        return ans


# V0-2
# IDEA: PQ + `Lazy Deletion` + hashmap (GEMINI)
import heapq

class Solution(object):
    def mostFrequentIDs(self, nums, freq):
        """
        :type nums: List[int]
        :type freq: List[int]
        :rtype: List[int]
        """
        c_map = {}  # Tracks the absolute TRUE frequency of each ID
        pq = []     # Max-Heap storing tuples of (-frequency, ID)
        
        n = len(nums)
        ans = [0] * n
        
        for i in range(n):
            val = nums[i]
            cnt = freq[i]
            
            # 1. Update the true frequency in the hash map
            c_map[val] = c_map.get(val, 0) + cnt
            
            # 2. Push the updated frequency to the heap. 
            # (We do NOT delete the old frequency from the heap yet)
            heapq.heappush(pq, (-c_map[val], val))
            
            """
            NOTE !!!!

            how we do `lazy delete`


            -> we ONLY do delete `till we reach a correct cnt one`
            -> e.g. we leave ALL other cnt (pq element) unchanged
            	-> we ONLY do `lazy delete` till the needed idx
            """
            # 3. Lazy Deletion: Check if the top of the heap is stale.
            # If the frequency at the top of the heap doesn't match the true 
            # frequency in our hash map, it's outdated data. Pop it!
            while pq and -pq[0][0] != c_map[pq[0][1]]:
                heapq.heappop(pq)
                
            # 4. The top of the heap is now guaranteed to be accurate.
            ans[i] = -pq[0][0] if pq else 0
            
        return ans



# V1

# V2
