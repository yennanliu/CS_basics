"""

1899. Merge Triplets to Form Target Triplet
Medium

A triplet is an array of three integers. You are given a 2D integer array
triplets, where triplets[i] = [ai, bi, ci] describes the ith triplet. You are
also given an integer array target = [x, y, z] that describes the triplet you
want to obtain.

To obtain target, you may apply the following operation on triplets any number
of times (possibly zero):

Choose two indices (0-indexed) i and j (i != j) and update triplets[j] to
become [max(ai, aj), max(bi, bj), max(ci, cj)].

For example, if triplets[i] = [2, 5, 3] and triplets[j] = [1, 7, 5],
triplets[j] will be updated to [max(2, 1), max(5, 7), max(3, 5)] = [2, 7, 5].

Return true if it is possible to obtain the target triplet [x, y, z] as an
element of triplets, or false otherwise.

Example 1:

Input: triplets = [[2,5,3],[1,8,4],[1,7,5]], target = [2,7,5]

Output: true

Explanation: Choose the first and last triplets. Update the last triplet to be
[max(2,1), max(5,7), max(3,5)] = [2,7,5]. The target is now an element of
triplets.

Example 2:

Input: triplets = [[3,4,5],[4,5,6]], target = [3,2,5]

Output: false

Explanation: It is impossible to have [3,2,5] as an element because there is
no 2 in any of the triplets.

Example 3:

Input: triplets = [[2,5,3],[2,3,4],[1,2,5],[5,2,3]], target = [5,5,5]

Output: true

Explanation: Merge the first and third triplets into [2,5,5], then merge that
with the fourth into [5,5,5].

Constraints:

1 <= triplets.length <= 10^5

triplets[i].length == target.length == 3

1 <= ai, bi, ci, x, y, z <= 1000

"""

# V0
# IDEA : GREEDY — MERGE EVERY "SAFE" TRIPLET, THEN COMPARE TO TARGET
#
#   max() can only push a value UP, never down. So a triplet with ANY value
#   above the target's in that position can never be used: merging it in
#   overshoots that position for good. Throw those away.
#
#   Every remaining ("safe") triplet is <= target in all 3 positions, so
#   merging ALL of them never hurts — it can only bring each position closer
#   to the target. The answer is true exactly when that merge equals target.
#
#   e.g. triplets = [[2,5,3],[1,8,4],[1,7,5]], target = [2,7,5]
#        [1,8,4] has 8 > 7 -> skip
#        merge [2,5,3] and [1,7,5] -> [2,7,5] == target -> True
#
# time = O(n), space = O(1)
class Solution(object):
    def mergeTriplets(self, triplets, target):
        """
        :type triplets: List[List[int]]
        :type target: List[int]
        :rtype: bool
        """
        if not triplets:
            return False

        # running max of every safe triplet; values are >= 1, so 0 is "nothing yet"
        merged = [0, 0, 0]

        for triplet in triplets:
            # NOTE !!! one value above target poisons the whole triplet
            if (triplet[0] > target[0] or
                    triplet[1] > target[1] or
                    triplet[2] > target[2]):
                continue

            # safe: merging it can only move us toward the target
            for pos in range(3):
                merged[pos] = max(merged[pos], triplet[pos])

        return merged == target


# V0-1
# IDEA : GREEDY — TRACK WHICH TARGET POSITIONS A SAFE TRIPLET HITS EXACTLY
#
#   Same filter as V0. Since a safe triplet is <= target everywhere, the merge
#   reaches target[pos] exactly when SOME safe triplet has target[pos] at pos.
#   So just record which of the 3 positions have been matched, and stop as
#   soon as all 3 are — an early exit V0 does not have.
#
# time = O(n), space = O(1)
class Solution2(object):
    def mergeTriplets(self, triplets, target):
        """
        :type triplets: List[List[int]]
        :type target: List[int]
        :rtype: bool
        """
        if not triplets:
            return False

        # positions (0, 1, 2) some safe triplet matches exactly
        matched = set()

        for triplet in triplets:
            # skip a triplet that would overshoot any position
            if (triplet[0] > target[0] or
                    triplet[1] > target[1] or
                    triplet[2] > target[2]):
                continue

            for pos in range(3):
                if triplet[pos] == target[pos]:
                    matched.add(pos)

            # all three positions can be reached -> done
            if len(matched) == 3:
                return True

        return False
