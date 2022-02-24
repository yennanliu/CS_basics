"""

1151. Minimum Swaps to Group All 1's Together
Medium

Given a binary array data, return the minimum number of swaps required to group all 1’s present in the array together in any place in the array.

 

Example 1:

Input: data = [1,0,1,0,1]
Output: 1
Explanation: There are 3 ways to group all 1's together:
[1,1,1,0,0] using 1 swap.
[0,1,1,1,0] using 2 swaps.
[0,0,1,1,1] using 1 swap.
The minimum is 1.
Example 2:

Input: data = [0,0,0,1,0]
Output: 0
Explanation: Since there is only one 1 in the array, no swaps are needed.
Example 3:

Input: data = [1,0,1,0,1,0,0,1,1,0,1]
Output: 3
Explanation: One possible solution that uses 3 swaps is [0,0,0,0,0,1,1,1,1,1,1].
 

Constraints:

1 <= data.length <= 105
data[i] is either 0 or 1.

"""

# V0
# IDEA : Sliding Window with Two Pointers
# IDEA : core : Find which sub-array HAS MOST "1", since it means it needs MINIMUM SWAP for getting all "1" toogether
# https://leetcode.com/problems/minimum-swaps-to-group-all-1s-together/solution/
class Solution:
    def minSwaps(self, data):
        ones = sum(data)
        cnt_one = max_one = 0
        left = right = 0
        while right < len(data):
            # updating the number of 1's by adding the new element
            cnt_one += data[right]
            right += 1
            # maintain the length of the window to ones
            if right - left > ones:
                # updating the number of 1's by removing the oldest element
                cnt_one -= data[left]
                left += 1
            # record the maximum number of 1's in the window
            max_one = max(max_one, cnt_one)
        return ones - max_one

# V1
# IDEA : Sliding Window with Two Pointers
# IDEA : core : Find which sub-array HAS MOST "1", since it means it needs MINIMUM SWAP for getting all "1" toogether
# https://leetcode.com/problems/minimum-swaps-to-group-all-1s-together/solution/
class Solution:
    def minSwaps(self, data):
        ones = sum(data)
        cnt_one = max_one = 0
        left = right = 0
        while right < len(data):
            # updating the number of 1's by adding the new element
            cnt_one += data[right]
            right += 1
            # maintain the length of the window to ones
            if right - left > ones:
                # updating the number of 1's by removing the oldest element
                cnt_one -= data[left]
                left += 1
            # record the maximum number of 1's in the window
            max_one = max(max_one, cnt_one)
        return ones - max_one

# V1'
# IDEA : Sliding Window with Deque (Double-ended Queue)
# https://leetcode.com/problems/minimum-swaps-to-group-all-1s-together/solution/
class Solution:
    def minSwaps(self, data):
        ones = sum(data)
        cnt_one = max_one = 0

        # maintain a deque with the size = ones
        deque = collections.deque()
        for i in range(len(data)):

            # we would always add the new element into the deque
            deque.append(data[i])
            cnt_one += data[i]

            # when there are more than ones elements in the deque,
            # remove the leftmost one
            if len(deque) > ones:
                cnt_one -= deque.popleft()
            max_one = max(max_one, cnt_one)
        return ones - max_one

# V1''
# https://leetcode.com/problems/minimum-swaps-to-group-all-1s-together/discuss/710135/python
class Solution:
    def minSwaps(self, data):
        # window size
        k = sum(data)
        
        # first window
        min_swaps = zeros = k - sum(data[:k])
                
        # window size of k, count zeros within the windows
        for end in range(k, len(data)):
            zeros += 1 if data[end] == 0 else 0
            zeros -= 1 if data[end-k] == 0 else 0
            min_swaps = min(zeros, min_swaps)
            
        return min_swaps

# V1'''
# IDEA : 2 POINTERS + SLIDING WINDOW
# https://leetcode.com/problems/minimum-swaps-to-group-all-1s-together/discuss/715540/Concise-Python
class Solution(object):
    def minSwaps(self, data):
        left, count, res, k = 0, 0, len(data), sum(data)
        for right in range(len(data)):
            if data[right] == 1: count += 1
            if right - left >= k - 1:
                res = min(res, k - count)
                if data[left] == 1: count -= 1
                left += 1

        return res

# V1''''
# IDEA : 2 POINTERS + SLIDING WINDOW
# https://leetcode.com/problems/minimum-swaps-to-group-all-1s-together/discuss/619208/Python-Sliding-Window
class Solution(object):
    def minSwaps(self, data):
        window_size, ans = sum(data), len(data)
        if window_size == 0:
            return 0
        subsum = 0
        for right in range(len(data)):
            subsum += data[right]
            if right >= window_size - 1:
                ans = min(ans, window_size - subsum)
                subsum -= data[right - window_size + 1]
        return ans

# V1'''''
# https://cloud.tencent.com/developer/article/1659664

# V1'''''''
# https://leetcode.ca/2019-01-24-1151-Minimum-Swaps-to-Group-All-1's-Together/

# V2