"""

457. Circular Array Loop
Medium

You are playing a game involving a circular array of non-zero integers nums. Each nums[i] denotes the number of indices forward/backward you must move if you are located at index i:

If nums[i] is positive, move nums[i] steps forward, and
If nums[i] is negative, move abs(nums[i]) steps backward.

Since the array is circular, you may assume that moving forward from the last element puts you on the first element, and moving backwards from the first element puts you on the last element.

A cycle in the array consists of a sequence of indices seq of length k where:

Following the movement rules above results in the repeating index sequence seq[0] -> seq[1] -> ... -> seq[k - 1] -> seq[0] -> ...
Every nums[seq[j]] is either all positive or all negative.
k > 1

Return true if there is a cycle in nums, or false otherwise.

Example 1:

https://assets.leetcode.com/uploads/2022/09/01/img1.jpg

Input: nums = [2,-1,1,2,2]
Output: true
Explanation: The graph shows how the indices are connected. White nodes are jumping forward, while red is jumping backward.
We can see the cycle 0 --> 2 --> 3 --> 0 --> ..., and all of its nodes are white (jumping in the same direction).

Example 2:

https://assets.leetcode.com/uploads/2022/09/01/img2.jpg

Input: nums = [-1,-2,-3,-4,-5,6]
Output: false
Explanation: The graph shows how the indices are connected. White nodes are jumping forward, while red is jumping backward.
The only cycle is of size 1, so we return false.

Example 3:

https://assets.leetcode.com/uploads/2022/09/01/img3.jpg

Input: nums = [1,-1,5,1,4]
Output: true
Explanation: The graph shows how the indices are connected. White nodes are jumping forward, while red is jumping backward.
We can see the cycle 0 --> 1 --> 0 --> ..., and while it is of size > 1, it has a node jumping forward and a node jumping backward, so it is not a cycle.
We can see the cycle 3 --> 4 --> 3 --> ..., and all of its nodes are white (jumping in the same direction).

Constraints:

1 <= nums.length <= 5000
-1000 <= nums[i] <= 1000
nums[i] != 0

Follow up: Could you solve it in O(n) time complexity and O(1) extra space complexity?

"""

# V0

# V1 : DEV 



# V2 
# http://bookshadow.com/weblog/2016/11/09/leetcode-circular-array-loop/
# dfs 
# time = O(n)  # n = len(nums)
# space = O(n)  # recursion stack
class Solution(object):
    def circularArrayLoop(self, nums):
        """
        :type nums: List[int]
        :rtype: bool
        """
        size = len(nums)
        def dfs(idx, cnt):
            if cnt < size:
                nidx = (idx + nums[idx] + size) % size
                if nidx == idx or \
                  nums[nidx] * nums[idx] <= 0 or \
                  dfs(nidx, cnt + 1) == 0:
                    nums[idx] = 0
            return nums[idx]
        for idx in range(size):
            if nums[idx] and dfs(idx, 0):
                return True
        return False

# V3 
# http://bookshadow.com/weblog/2016/11/09/leetcode-circular-array-loop/
# time = O(n)  # n = len(nums)
# space = O(1)
class Solution(object):
    def circularArrayLoop(self, nums):
        """
        :type nums: List[int]
        :rtype: bool
        """
        size = len(nums)
        next = lambda x : (x + nums[x] + size) % size
        for x in range(size):
            if not nums[x]:
                continue
            y, c = x, 0
            while c < size:
                z = next(y)
                if y == z:
                    nums[y] = 0
                if nums[y] * nums[z] <= 0:
                    break
                y = z
                c += 1
            if c == size:
                return True
            y = x
            while c > 0:
                z = next(y)
                nums[y] = 0
                c -= 1
        return False


# V4 
# time = O(n)
# space = O(1)

class Solution(object):
    def circularArrayLoop(self, nums):
        """
        :type nums: List[int]
        :rtype: bool
        """
        def next_index(nums, i):
            return (i + nums[i]) % len(nums)

        for i in range(len(nums)):
            if nums[i] == 0:
                continue

            slow, fast = i, i
            while nums[next_index(nums, slow)] * nums[i] > 0 and \
                  nums[next_index(nums, fast)] * nums[i] > 0 and \
                  nums[next_index(nums, next_index(nums, fast))] * nums[i] > 0:
                slow = next_index(nums, slow)
                fast = next_index(nums, next_index(nums, fast))
                if slow == fast:
                    if slow == next_index(nums, slow):
                        break
                    return True

            slow, val = i, nums[i]
            while nums[slow] * val > 0:
                tmp = next_index(nums, slow)
                nums[slow] = 0
                slow = tmp

        return False