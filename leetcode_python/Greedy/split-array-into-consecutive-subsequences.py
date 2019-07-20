# V0 

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/82185011
class Solution(object):
    def isPossible(self, nums):
        """
        :type nums: List[int]
        :rtype: bool
        """
        saved = collections.defaultdict(list)
        for num in nums:
            last = saved[num - 1]
            _len = 0 if (not last) else heapq.heappop(last)
            current = saved[num]
            heapq.heappush(current, _len + 1)
        for values in saved.values():
            for v in values:
                if v < 3:
                    return False
        return True
# V2 
# Time:  O(n)
# Space: O(1)
class Solution(object):
    def isPossible(self, nums):
        """
        :type nums: List[int]
        :rtype: bool
        """
        pre, cur = float("-inf"), 0
        cnt1, cnt2, cnt3 = 0, 0, 0
        i = 0
        while i < len(nums):
            cnt = 0
            cur = nums[i]
            while i < len(nums) and cur == nums[i]:
                cnt += 1
                i += 1

            if cur != pre + 1:
                if cnt1 != 0 or cnt2 != 0:
                    return False
                cnt1, cnt2, cnt3 = cnt, 0, 0
            else:
                if cnt < cnt1 + cnt2:
                    return False
                cnt1, cnt2, cnt3 = max(0, cnt - (cnt1 + cnt2 + cnt3)), \
                                   cnt1, \
                                   cnt2 + min(cnt3, cnt - (cnt1 + cnt2))
            pre = cur
        return cnt1 == 0 and cnt2 == 0
