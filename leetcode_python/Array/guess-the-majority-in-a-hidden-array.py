"""

1538. Guess the Majority in a Hidden Array
Medium

We have an integer array nums, where all the integers in nums are 0 or 1. You will not be given direct access to the array, instead, you will have an API ArrayReader which have the following functions:

int query(int a, int b, int c, int d): where 0 <= a < b < c < d < ArrayReader.length(). The function returns the distribution of the value of the 4 elements and returns:
    4 : if the values of the 4 elements are the same (0 or 1).
    2 : if three elements have a value equal to 0 and one element has value equal to 1 or vice versa.
    0 : if two element have a value equal to 0 and two elements have a value equal to 1.
int length(): Returns the size of the array.

You are allowed to call query() 2 * n times at most where n is equal to ArrayReader.length().

Return any index of the most frequent value in nums, in case of tie, return -1.


Example 1:

Input: nums = [0,0,1,0,1,1,1,1]
Output: 5
Explanation: The following calls to the API
reader.length() // returns 8 because there are 8 elements in the hidden array.
reader.query(0,1,2,3) // returns 2 this is a query that compares the elements nums[0], nums[1], nums[2], nums[3]
// Three elements have a value equal to 0 and one element has value equal to 1 or viceversa.
reader.query(4,5,6,7) // returns 4 because nums[4], nums[5], nums[6], nums[7] have the same value.
we can infer that the most frequent value is found in the last 4 elements.
Index 2, 4, 6, 7 is also a correct answer.

Example 2:

Input: nums = [0,0,1,1,0]
Output: 0

Example 3:

Input: nums = [1,0,1,0,1,0,1,0]
Output: -1


Constraints:

5 <= nums.length <= 10^5
0 <= nums[i] <= 1


Follow up: What is the minimum number of calls needed to find the majority element?

"""

# V0
# IDEA : COMPARE EVERY INDEX AGAINST A FIXED REFERENCE INDEX
#
#   key fact : if three of the four query slots are HELD FIXED, the return
#   value is a strictly different number for a 0 vs a 1 in the free slot.
#   so query(a, b, c, i) == query(a, b, c, j)  <=>  nums[i] == nums[j].
#
#   pick index 3 as the reference :
#     - x = query(0,1,2,3); for every i >= 4, query(0,1,2,i) == x tells us
#       whether nums[i] == nums[3].                        [n - 4 calls]
#     - indices 0,1,2 cannot use that trio, so anchor on {..,4} instead :
#       y = query(0,1,2,4), then
#         query(1,2,3,4) == y  <=>  nums[3] == nums[0]   (fixed 1,2,4)
#         query(0,2,3,4) == y  <=>  nums[3] == nums[1]   (fixed 0,2,4)
#         query(0,1,3,4) == y  <=>  nums[3] == nums[2]   (fixed 0,1,4)
#
#   a = how many indices match nums[3] (index 3 itself counts), b = the rest,
#   and k remembers one index that differs.
#   NOTE : total calls = (n - 4) + 5 = n + 1, well inside the 2n budget.
#
# time = O(n) calls, space = O(1)
# """
# This is the ArrayReader's API interface.
# You should not implement it, or speculate about its implementation
# """
# class ArrayReader(object):
#    # Compares 4 different elements in the array
#    # return 4 if the values of the 4 elements are the same (0 or 1).
#    # return 2 if three elements have a value equal to 0 and one element has value equal to 1 or vice versa.
#    # return 0 if two element have a value equal to 0 and two elements have a value equal to 1.
#    def query(self, a, b, c, d):
#
#    # Returns the length of the array
#    def length(self):
class Solution(object):
    def guessMajority(self, reader):
        n = reader.length()

        x = reader.query(0, 1, 2, 3)
        a, b = 1, 0          # index 3 itself matches nums[3]
        k = -1               # some index whose value differs from nums[3]

        for i in range(4, n):
            if reader.query(0, 1, 2, i) == x:
                a += 1
            else:
                b += 1
                k = i

        y = reader.query(0, 1, 2, 4)
        for idx, trio in ((0, (1, 2, 3, 4)), (1, (0, 2, 3, 4)), (2, (0, 1, 3, 4))):
            if reader.query(trio[0], trio[1], trio[2], trio[3]) == y:
                a += 1
            else:
                b += 1
                k = idx

        if a == b:
            return -1
        return 3 if a > b else k


# V0-1
# IDEA : SLIDING WINDOW OF CONSECUTIVE QUADRUPLES -> 4 CHAINS MOD 4
#
#   query the n - 3 CONSECUTIVE quadruples
#       Q[i] = query(i, i + 1, i + 2, i + 3)
#   two neighbouring windows share the trio (i+1, i+2, i+3) and differ only in
#   the free slot (i vs i + 4), so
#       Q[i] == Q[i + 1]   <=>   nums[i] == nums[i + 4]
#
#   that is a relation with STRIDE 4, so the single sweep resolves each of the
#   4 residue classes mod 4 internally (labels propagate by XOR along the
#   chain) but says nothing across classes.
#
#   the classes are then stitched together with 4 more queries, all sharing
#   the quadruple {1,2,3,4} as the right-hand side :
#       {0,2,3,4} vs {1,2,3,4}  (trio 2,3,4)  ->  nums[0] ?= nums[1]
#       {0,1,3,4} vs {1,2,3,4}  (trio 1,3,4)  ->  nums[0] ?= nums[2]
#       {0,1,2,4} vs {1,2,3,4}  (trio 1,2,4)  ->  nums[0] ?= nums[3]
#
#   NOTE : total calls = (n - 3) + 4 = n + 1, same budget as the star version,
#          but the information comes from OVERLAPPING windows instead of one
#          fixed reference trio.
#   NOTE : all labels are relative to nums[0]; the absolute values are
#          unknowable (complementing the whole array leaves every query
#          unchanged) which is fine -- only the group SIZES matter.
#
# time = O(n) calls, space = O(n)
class Solution(object):
    def guessMajority(self, reader):
        n = reader.length()

        window = [reader.query(i, i + 1, i + 2, i + 3) for i in range(n - 3)]

        lab = [0] * n                       # 0 == "same value as nums[0]"
        base = reader.query(1, 2, 3, 4)
        lab[1] = 0 if reader.query(0, 2, 3, 4) == base else 1
        lab[2] = 0 if reader.query(0, 1, 3, 4) == base else 1
        lab[3] = 0 if reader.query(0, 1, 2, 4) == base else 1

        for i in range(n - 4):
            if window[i] == window[i + 1]:
                lab[i + 4] = lab[i]
            else:
                lab[i + 4] = lab[i] ^ 1

        zeros = lab.count(0)
        ones = n - zeros
        if zeros == ones:
            return -1
        return lab.index(0) if zeros > ones else lab.index(1)


# V0-2
# IDEA : KNOWN-EQUAL ANCHOR PAIR -> TWO INDICES PER QUERY (~n / 2 CALLS)
#
#   answers the follow-up (fewer calls) by putting a pair of indices KNOWN TO
#   BE EQUAL into the query instead of a trio.
#
#   step 1 -- find such a pair among 0, 1, 2. By pigeonhole two of three bits
#   are equal, so at most two comparisons are needed: if nums[0] != nums[1] and
#   nums[0] != nums[2] then nums[1] == nums[2]. This also labels 0, 1, 2.
#   A comparison of i and j is "same trio, swap the 4th slot", and the shared
#   quadruple {1,2,3,4} is reused, so step 1 costs only 3 calls.
#
#   step 2 -- with the anchor pair (a, b), nums[a] == nums[b] == v, a single
#   query on {a, b, i, j} reads off HOW MANY of nums[i], nums[j] equal v :
#       4  ->  both equal v            (v, v, v, v)
#       2  ->  exactly one equals v    (v, v, v, x)
#       0  ->  neither equals v        (v, v, x, x)
#   so one call classifies TWO fresh indices.
#
#   the ambiguous "2" case never needs resolving: such a pair contributes
#   exactly +1 to each side of the count, and if the majority turns out to be
#   the non-v group then known-1 labels must already outnumber the known-0
#   labels (a and b are both 0), so a representative index is already in hand.
#
#   NOTE : calls = 3 + ceil((n - 3) / 2) (+2 for an odd leftover index),
#          roughly n / 2 -- half of the star / sliding-window versions.
#
# time = O(n) calls (~n / 2), space = O(n)
class Solution(object):
    def guessMajority(self, reader):
        n = reader.length()
        memo = {}

        def ask(quad):
            key = tuple(sorted(quad))
            if key not in memo:
                memo[key] = reader.query(*key)
            return memo[key]

        def same(i, j):
            # 3 fixed slots + swap the 4th -> equal answers iff equal values
            trio = [t for t in range(n) if t != i and t != j][:3]
            return ask(trio + [i]) == ask(trio + [j])

        lab = [None] * n                    # 0 == "equals the anchor value v"
        if same(0, 1):
            lab[0] = lab[1] = 0
            lab[2] = 0 if same(0, 2) else 1
            anchor = (0, 1)
        elif same(0, 2):
            lab[0] = lab[2] = 0
            lab[1] = 1
            anchor = (0, 2)
        else:
            # nums[0] differs from both -> nums[1] == nums[2], anchor on them
            lab[1] = lab[2] = 0
            lab[0] = 1
            anchor = (1, 2)

        a, b = anchor
        split = 0                           # pairs known to be one-of-each
        i = 3
        while i + 1 < n:
            got = ask([a, b, i, i + 1])
            if got == 4:
                lab[i] = lab[i + 1] = 0
            elif got == 0:
                lab[i] = lab[i + 1] = 1
            else:
                split += 1
            i += 2
        if i < n:                           # odd number of remaining indices
            lab[i] = 0 if same(i, a) else 1

        zeros = sum(1 for x in lab if x == 0) + split
        ones = sum(1 for x in lab if x == 1) + split
        if zeros == ones:
            return -1
        return lab.index(0) if zeros > ones else lab.index(1)
