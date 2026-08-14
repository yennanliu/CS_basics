"""

2023. Number of Pairs of Strings With Concatenation Equal to Target
Medium

Given an array of digit strings nums and a digit string target, return the number of pairs of indices (i, j) (where i != j) such that the concatenation of nums[i] + nums[j] equals target.


Example 1:

Input: nums = ["777","7","77","77"], target = "7777"
Output: 4
Explanation: Valid pairs are:
- (0, 1): "777" + "7"
- (1, 0): "7" + "777"
- (2, 3): "77" + "77"
- (3, 2): "77" + "77"

Example 2:

Input: nums = ["123","4","12","34"], target = "1234"
Output: 2
Explanation: Valid pairs are:
- (0, 1): "123" + "4"
- (2, 3): "12" + "34"

Example 3:

Input: nums = ["1","1","1"], target = "11"
Output: 6
Explanation: Valid pairs are:
- (0, 1): "1" + "1"
- (1, 0): "1" + "1"
- (0, 2): "1" + "1"
- (2, 0): "1" + "1"
- (1, 2): "1" + "1"
- (2, 1): "1" + "1"


Constraints:

2 <= nums.length <= 100
1 <= nums[i].length <= 100
2 <= target.length <= 100
nums[i] and target consist of digits.
nums[i] and target do not have leading zeros.

"""

# V0
# IDEA : COUNT PREFIXES SEEN SO FAR, THEN THE MATCHING SUFFIXES
#
#   walk the array once holding two counters :
#       prefix_cnt[w] = how many earlier elements equal w
#       suffix_cnt[w] = how many earlier elements equal w
#   for the current element w :
#       - if w is a PREFIX of target, it can pair with every EARLIER element
#         equal to the remaining suffix  -> += suffix_cnt[target[len(w):]]
#       - if w is a SUFFIX of target, it can pair with every EARLIER element
#         equal to the leading prefix    -> += prefix_cnt[target[:-len(w)]]
#
#   this counts every ordered pair (i, j), i != j, exactly once (the later
#   index of the pair is the one being processed), which is what the problem
#   asks for.
#
# time = O(n * L), space = O(n * L)
from collections import defaultdict


class Solution(object):
    def numOfPairs(self, nums, target):
        res = 0
        prefix_cnt = defaultdict(int)
        suffix_cnt = defaultdict(int)
        for w in nums:
            if len(w) < len(target):
                if target.startswith(w):
                    res += suffix_cnt[target[len(w):]]
                if target.endswith(w):
                    res += prefix_cnt[target[:len(target) - len(w)]]
            if target.startswith(w):
                prefix_cnt[w] += 1
            if target.endswith(w):
                suffix_cnt[w] += 1
        return res
