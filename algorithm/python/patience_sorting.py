#---------------------------------------------------------------
#  PATIENCE SORTING  (Longest Increasing Subsequence in O(N log N))
#---------------------------------------------------------------
#
# Deal the array out as a game of patience (solitaire):
#
#   - place each card on the LEFTMOST pile whose top card is >= card
#   - if there is no such pile, START A NEW PILE on the right
#   - the NUMBER OF PILES is the length of the longest increasing subsequence
#
# Pile tops increase from left to right, so "leftmost pile whose top is >= x"
# is a plain lower_bound (bisect_left) over the tops -> O(log P) per card.
#
# Keeping only the tops gives the familiar `tails` array:
#
#   tails[k] = the SMALLEST possible ending value of an increasing
#              subsequence of length k + 1
#
# tails is NOT itself a subsequence (see lis_reconstruct below for the real
# one) - only len(tails) is meaningful.
#
# Time  : O(N log N)   (N cards x binary search over <= N piles)
# Space : O(N)         (O(N) for tails only; the full piles are O(N) too)
#
# Why the pile count is the answer:
#   - each pile is, read in DEALING ORDER (bottom to top), a NON-INCREASING
#     subsequence, so no increasing subsequence can use two cards from one pile
#     -> LIS <= number of piles
#   - every card placed on pile k sat on a card of pile k-1 that was already
#     there, so following that chain back gives an increasing subsequence of
#     length k + 1  -> LIS >= number of piles
#   (This is Dilworth's theorem: the piles are a minimum non-increasing cover.)
#
# References:
#   - https://en.wikipedia.org/wiki/Patience_sorting
#   - https://en.wikipedia.org/wiki/Longest_increasing_subsequence
#   - doc/cheatsheet/patience_sorting.md
#   - doc/cheatsheet/binary_search.md  (section 1.5 - the lower bound it runs on)

import bisect


# V0 : LIS length, hand-rolled lower bound (what an interview wants to see)
def lis_length(nums):
    # tails[k] : smallest possible tail of an increasing run of length k + 1
    tails = []

    for num in nums:
        # lower bound : first index with tails[lo] >= num
        lo, hi = 0, len(tails) - 1
        while lo <= hi:
            mid = lo + (hi - lo) // 2
            if tails[mid] < num:
                lo = mid + 1     # num belongs strictly right of mid
            else:
                hi = mid - 1     # an EQUAL tail fails the strict <, so it lands
                                 # here too -> hi moves left, lo stays put

        if lo == len(tails):
            tails.append(num)    # beats every tail -> a NEW longest length exists
        else:
            tails[lo] = num      # same length, cheaper tail -> overwrite

    return len(tails)


# V0-1 : the same thing via bisect (what you actually write in Python)
def lis_length_bisect(nums):
    tails = []
    for num in nums:
        pos = bisect.bisect_left(tails, num)   # first tail >= num
        if pos == len(tails):
            tails.append(num)
        else:
            tails[pos] = num
    return len(tails)


# V1 : longest NON-DECREASING subsequence - the only change is bisect_right
#      (first tail > num), so an equal value extends instead of replacing
def lis_length_non_decreasing(nums):
    tails = []
    for num in nums:
        pos = bisect.bisect_right(tails, num)  # first tail > num
        if pos == len(tails):
            tails.append(num)
        else:
            tails[pos] = num
    return len(tails)


# V2 : the actual card game - keep the whole piles, not just their tops
#      (LIS length is len(piles); each pile is non-increasing read bottom to
#       top, and together they are a minimum non-increasing cover of the
#       array - i.e. Dilworth's theorem)
def patience_piles(nums):
    piles = []       # piles[k] : the cards on pile k, top card LAST
    tops = []        # tops[k] == piles[k][-1], kept separately so bisect can see it

    for num in nums:
        pos = bisect.bisect_left(tops, num)
        if pos == len(piles):
            piles.append([num])
            tops.append(num)
        else:
            piles[pos].append(num)
            tops[pos] = num

    return piles


# V3 : recover the subsequence itself, not only its length
#      tails alone cannot be read off as the answer: for [3, 4, 5, 1] it ends
#      up as [1, 4, 5], which is not even a subsequence of the input
def lis_reconstruct(nums):
    if not nums:
        return []

    tails = []        # tail VALUES (the array bisect searches)
    tails_idx = []    # index in nums of each tail
    prev = [-1] * len(nums)

    for i, num in enumerate(nums):
        pos = bisect.bisect_left(tails, num)

        # whatever currently ends the run of length pos becomes num's predecessor
        if pos > 0:
            prev[i] = tails_idx[pos - 1]

        if pos == len(tails):
            tails.append(num)
            tails_idx.append(i)
        else:
            tails[pos] = num
            tails_idx[pos] = i

    # walk back from the tail of the longest run
    out = []
    i = tails_idx[-1]
    while i != -1:
        out.append(nums[i])
        i = prev[i]
    return out[::-1]


# V4 : the O(N^2) DP, kept as the correctness oracle for the tests below
def lis_length_dp(nums):
    if not nums:
        return 0
    dp = [1] * len(nums)
    for i in range(len(nums)):
        for j in range(i):
            if nums[j] < nums[i]:
                dp[i] = max(dp[i], dp[j] + 1)
    return max(dp)


if __name__ == "__main__":
    import random

    nums = [10, 9, 2, 5, 3, 7, 101, 18]
    assert lis_length(nums) == 4                      # [2, 3, 7, 101]
    assert lis_length_bisect(nums) == 4
    assert lis_length([7] * 7) == 1
    assert lis_length([]) == 0

    # strictly increasing vs non-decreasing : the bisect_left / bisect_right twist
    assert lis_length([2, 2, 2, 3]) == 2              # [2, 3]
    assert lis_length_non_decreasing([2, 2, 2, 3]) == 4

    # the piles ARE the algorithm; their count is the answer
    assert patience_piles([10, 9, 2, 5, 3, 7]) == [[10, 9, 2], [5, 3], [7]]
    assert len(patience_piles(nums)) == lis_length(nums)

    # tails is not a subsequence, the reconstruction is
    assert lis_reconstruct([3, 4, 5, 1]) == [3, 4, 5]
    # any valid LIS is a correct answer: 18 replaced 101 as the length-4 tail,
    # so the walk-back finds [2, 3, 7, 18] rather than [2, 3, 7, 101]
    assert lis_reconstruct(nums) == [2, 3, 7, 18]

    # randomized cross-check against the O(N^2) DP
    random.seed(0)
    for _ in range(2000):
        arr = [random.randint(-8, 8) for _ in range(random.randint(0, 14))]
        expected = lis_length_dp(arr)
        assert lis_length(arr) == expected
        assert lis_length_bisect(arr) == expected
        assert len(patience_piles(arr)) == expected

        got = lis_reconstruct(arr)
        assert len(got) == expected
        assert all(a < b for a, b in zip(got, got[1:]))    # strictly increasing
        it = iter(arr)
        assert all(v in it for v in got)                   # and a real subsequence

    print("Success.")
