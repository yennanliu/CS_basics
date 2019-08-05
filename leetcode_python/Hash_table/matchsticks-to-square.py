# V0 

# V1 
# http://bookshadow.com/weblog/2016/12/18/leetcode-matchsticks-to-square/
# https://blog.csdn.net/XX_123_1_RJ/article/details/81507252
# IDEA : MEMORY SEARCH 
class Solution(object):
    def makesquare(self, nums):
        """
        :type nums: List[int]
        :rtype: bool
        """
        if sum(nums) % 4: return False
        self.q = sum(nums) / 4
        self.dp = collections.defaultdict(dict)
        return self.solve(nums, 4)
        
    def solve(self, nums, part):
        total = sum(nums)
        if part == 1:
            return total == self.q
        size = len(nums)
        if size < part or total % part:
            return False
        tnums = tuple(nums)
        if part in self.dp[tnums]:
            return self.dp[tnums][part]
        for x in range(1, (1 << size) - 1):
            left, right = [], []
            for y in range(size):
                if x & (1 << y):
                    left.append(nums[y])
                else:
                    right.append(nums[y])
            if sum(left) != self.q:
                continue
            if self.solve(right, part - 1):
                self.dp[tnums][part] = True
                return True
        self.dp[tnums][part] = False
        return False

# V2 
# Time:  O(n * s * 2^n), s is the number of subset of which sum equals to side length.
# Space: O(n * (2^n + s))
class Solution(object):
    def makesquare(self, nums):
        """
        :type nums: List[int]
        :rtype: bool
        """
        total_len = sum(nums)
        if total_len % 4:
            return False

        side_len = total_len / 4
        fullset = (1 << len(nums)) - 1

        used_subsets = []
        valid_half_subsets = [0] * (1 << len(nums))

        for subset in range(fullset+1):
            subset_total_len = 0
            for i in range(len(nums)):
                if subset & (1 << i):
                    subset_total_len += nums[i]

            if subset_total_len == side_len:
                for used_subset in used_subsets:
                    if (used_subset & subset) == 0:
                        valid_half_subset = used_subset | subset
                        valid_half_subsets[valid_half_subset] = True
                        if valid_half_subsets[fullset ^ valid_half_subset]:
                            return True
                used_subsets.append(subset)

        return False