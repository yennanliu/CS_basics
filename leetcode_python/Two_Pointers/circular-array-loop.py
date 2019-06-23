
# V1 : DEV 



# V2 
# http://bookshadow.com/weblog/2016/11/09/leetcode-circular-array-loop/
# dfs 
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
# Time:  O(n)
# Space: O(1)

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