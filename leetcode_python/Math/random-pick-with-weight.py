"""

528. Random Pick with Weight
Medium

You are given a 0-indexed array of positive integers w where w[i] describes the
weight of the i^th index.

You need to implement the function pickIndex(), which randomly picks an index in
the range [0, w.length - 1] (inclusive) and returns it. The probability of
picking an index i is w[i] / sum(w).

For example, if w = [1, 3], the probability of picking index 0 is
1 / (1 + 3) = 0.25 (i.e., 25%), and the probability of picking index 1 is
3 / (1 + 3) = 0.75 (i.e., 75%).

Example 1:

Input
["Solution","pickIndex"]
[[[1]],[]]
Output
[null,0]

Explanation
Solution solution = new Solution([1]);
solution.pickIndex(); // return 0. The only option is to return 0 since there
is only one element in w.

Example 2:

Input
["Solution","pickIndex","pickIndex","pickIndex","pickIndex","pickIndex"]
[[[1,3]],[],[],[],[],[]]
Output
[null,1,1,1,1,0]

Explanation
Solution solution = new Solution([1, 3]);
solution.pickIndex(); // return 1. It is returning the second element
(index = 1) that has a probability of 3/4.
solution.pickIndex(); // return 1
solution.pickIndex(); // return 1
solution.pickIndex(); // return 1
solution.pickIndex(); // return 0. It is returning the first element
(index = 0) that has a probability of 1/4.

Since this is a randomization problem, multiple answers are allowed.
All of the following outputs can be considered correct:
[null,1,1,1,1,0]
[null,1,1,1,1,1]
[null,1,1,1,0,0]
[null,1,1,1,0,1]
[null,1,0,1,0,0]
......
and so on.

Constraints:

1 <= w.length <= 10^4
1 <= w[i] <= 10^5
pickIndex will be called at most 10^4 times.

"""

import random
import bisect

# V0
# IDEA : PREFIX SUM + BINARY SEARCH (FIRST PREFIX > TARGET)
#
#   Lay the weights end to end on a number line [0, total). Index i owns the
#   half-open slice [prefix[i] - w[i], prefix[i]), whose length is exactly
#   w[i]. Draw a uniform integer target in [0, total - 1]; the index whose
#   slice contains it is picked with probability w[i] / total.
#
#   The owner of target is the FIRST i with prefix[i] > target, and prefix is
#   strictly increasing (every w[i] >= 1), so that is a lower-bound binary
#   search.
#
#   e.g. w = [1, 3] -> prefix = [1, 4], total = 4
#        target 0       -> first prefix > 0 is prefix[0] = 1 -> index 0 (1/4)
#        target 1, 2, 3 -> first prefix > t is prefix[1] = 4 -> index 1 (3/4)
#
#   (A linear scan per pick is O(n) -> 10^4 picks * 10^4 weights = 10^8, too
#    slow; the prefix array is built once so every pick is O(log n).)
#
# time = ctor: O(n), pickIndex: O(log n), space = O(n)
class Solution(object):

    def __init__(self, w):
        """
        :type w: List[int]
        """
        # prefix[i] = w[0] + ... + w[i]
        self.prefix = []
        running_sum = 0
        for weight in w:
            running_sum += weight
            self.prefix.append(running_sum)
        self.total = running_sum

    def pickIndex(self):
        """
        :rtype: int
        """
        # uniform over the total weight: randint is inclusive on both ends
        target = random.randint(0, self.total - 1)

        # find the first index whose prefix sum is > target
        left = 0
        right = len(self.prefix) - 1
        while left < right:
            mid = left + (right - left) // 2
            # NOTE !!! strictly greater: target == prefix[mid] is the first
            #          point of the NEXT slice, so mid cannot be the answer
            if self.prefix[mid] > target:
                # mid could be the answer -> keep it
                right = mid
            else:
                # target lies past mid's slice
                left = mid + 1

        return left


# V0-1
# IDEA : PREFIX SUM + bisect_right (SAME AS V0, LIBRARY BINARY SEARCH)
#
#   bisect_right(prefix, target) returns the first index whose value is
#   > target -- exactly the hand-written search in V0. Fine to use in an
#   interview once you can say what it returns; write V0 if asked to.
#
# time = ctor: O(n), pickIndex: O(log n), space = O(n)
class Solution2(object):

    def __init__(self, w):
        """
        :type w: List[int]
        """
        self.prefix = []
        running_sum = 0
        for weight in w:
            running_sum += weight
            self.prefix.append(running_sum)
        self.total = running_sum

    def pickIndex(self):
        """
        :rtype: int
        """
        target = random.randint(0, self.total - 1)
        # first index with prefix > target
        return bisect.bisect_right(self.prefix, target)


# V1
# IDEA : ALIAS METHOD (VOSE) -- O(1) PER PICK
#
#   Scale every weight to w[i] * n / total, so the average is exactly 1.
#   Build n buckets of capacity 1. Each bucket is filled by one "small"
#   index (scaled weight < 1) and topped up by one "large" index (> 1),
#   which donates the shortfall and may itself become small. Afterwards
#   bucket i holds index i with probability prob[i] and alias[i] otherwise.
#
#   A pick = choose a bucket uniformly, then flip a biased coin inside it.
#   Over all buckets, index i's total share is still w[i] / total.
#
#   NOTE : O(1) picks, but much harder to derive live -- learn V0 first.
#          Floats are used for the scaled weights; leftovers that should be
#          exactly 1 are set to 1 to absorb rounding error.
#
# time = ctor: O(n), pickIndex: O(1), space = O(n)
class Solution3(object):

    def __init__(self, w):
        """
        :type w: List[int]
        """
        n = len(w)
        total = float(sum(w))

        # scaled[i] = w[i] * n / total, so the scaled weights average to 1
        scaled = []
        for weight in w:
            scaled.append(weight * n / total)

        self.prob = [0.0] * n
        self.alias = [0] * n

        small = []   # indices whose scaled weight is < 1 (need a top-up)
        large = []   # indices whose scaled weight is >= 1 (can donate)
        for i in range(n):
            if scaled[i] < 1.0:
                small.append(i)
            else:
                large.append(i)

        # pair one small with one large to fill exactly one bucket
        while small and large:
            under = small.pop()
            over = large.pop()

            # bucket `under` keeps itself with prob scaled[under] ...
            self.prob[under] = scaled[under]
            # ... and hands the rest of the bucket to `over`
            self.alias[under] = over

            # `over` gave away (1 - scaled[under]) of its weight
            scaled[over] = scaled[over] - (1.0 - scaled[under])
            if scaled[over] < 1.0:
                small.append(over)
            else:
                large.append(over)

        # whatever is left should be exactly 1 (up to float error)
        for i in large:
            self.prob[i] = 1.0
        for i in small:
            self.prob[i] = 1.0

    def pickIndex(self):
        """
        :rtype: int
        """
        # pick a bucket uniformly, then decide between it and its alias
        bucket = random.randint(0, len(self.prob) - 1)
        if random.random() < self.prob[bucket]:
            return bucket
        return self.alias[bucket]


# Your Solution object will be instantiated and called as such:
# obj = Solution(w)
# param_1 = obj.pickIndex()
