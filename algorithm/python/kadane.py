#---------------------------------------------------------------
# KADANE'S ALGORITHM (maximum subarray sum)
#---------------------------------------------------------------
#
# Find the contiguous run of numbers with the largest sum, in one pass.
#
#     nums = [-2, 1, -3, 4, -1, 2, 1, -5, 4]
#                        \_________/
#                         4 - 1 + 2 + 1 = 6     the best run
#
# Trying every start and every end is O(N^2). Kadane's observation
# removes the inner loop:
#
#   Let best_here = the largest sum of a run that ENDS at this element.
#   A run ending here is either
#       - the best run ending at the previous element, extended by this one, or
#       - this element alone, starting fresh
#   and starting fresh wins exactly when what came before is NEGATIVE --
#   a negative running total can only drag the next element down.
#
#       best_here = max(num, best_here + num)
#       best      = max(best, best_here)
#
#     num        -2   1  -3   4  -1   2   1  -5   4
#     best_here  -2   1  -2   4   3   5   6   1   5
#     best       -2   1   1   4   4   5   6   6   6
#                         ^   ^
#                         |   restart: -2 + 4 < 4
#                         extending was still the better choice here
#
# It is the smallest useful example of dynamic programming: the answer
# for "ending here" is built from the answer for "ending one earlier",
# and only that one previous value has to be kept.
#
# Time  : O(N) -- one pass
# Space : O(1)
#
# Used by: LC 53 Maximum Subarray, LC 918 Maximum Sum Circular
#          Subarray, LC 152 Maximum Product Subarray (same idea with
#          a min tracked too), LC 121 Best Time to Buy and Sell Stock
#          (Kadane on the day-to-day differences).
#
# See also: doc/cheatsheet/kadane_algorithm.md


# V0 : the maximum sum  (LC 53)
def max_subarray(nums):
    """Largest sum of a non-empty contiguous subarray."""
    if not nums:
        raise ValueError("max_subarray needs at least one number")
    best_here = nums[0]              # best sum of a run ending at the current element
    best = nums[0]                   # best sum seen anywhere so far
    for num in nums[1:]:
        best_here = max(num, best_here + num)    # start fresh, or extend
        best = max(best, best_here)
    return best


# V0-1 : also report WHERE the best run is
def max_subarray_range(nums):
    """Return (best_sum, start, end) with nums[start..end] the best run."""
    if not nums:
        raise ValueError("max_subarray_range needs at least one number")
    best_here = nums[0]
    best = nums[0]
    run_start = 0                    # where the run ending at the current element begins
    best_start = 0
    best_end = 0
    for i in range(1, len(nums)):
        if best_here < 0:            # what came before only hurts: restart at i
            best_here = nums[i]
            run_start = i
        else:
            best_here += nums[i]
        if best_here > best:
            best = best_here
            best_start = run_start
            best_end = i
    return best, best_start, best_end


# V1 : circular array  (LC 918)
#
# On a ring, the best run either sits in the middle (plain Kadane) or
# WRAPS around the end. A wrapping run is everything EXCEPT a run in
# the middle, so its sum is  total - (the SMALLEST middle run).
#
#     [5, -3, 5]:   plain best = 7 (5 - 3 + 5)
#                   wrapping   = total 7 - min run (-3) = 10   <- 5 + 5
#
# One trap: if every number is negative, the "smallest run" is the
# whole array and the wrapping answer would be an EMPTY run (sum 0),
# which is not allowed. Then the plain answer stands.
def max_subarray_circular(nums):
    """Largest sum of a non-empty contiguous run on a circular array."""
    if not nums:
        raise ValueError("max_subarray_circular needs at least one number")
    total = 0
    max_here = 0
    min_here = 0
    max_sum = nums[0]
    min_sum = nums[0]
    for num in nums:
        total += num
        max_here = max(num, max_here + num)
        max_sum = max(max_sum, max_here)
        min_here = min(num, min_here + num)      # Kadane, mirrored for the minimum
        min_sum = min(min_sum, min_here)
    if max_sum < 0:                  # all negative: wrapping would mean taking nothing
        return max_sum
    return max(max_sum, total - min_sum)


# V2 : maximum PRODUCT  (LC 152)
#
# Same shape, one twist: multiplying by a negative number turns the
# smallest product into the largest. So track both the largest and the
# smallest product ending here.
def max_product_subarray(nums):
    """Largest product of a non-empty contiguous subarray."""
    if not nums:
        raise ValueError("max_product_subarray needs at least one number")
    max_here = nums[0]
    min_here = nums[0]
    best = nums[0]
    for num in nums[1:]:
        candidates = (num, max_here * num, min_here * num)
        max_here = max(candidates)
        min_here = min(candidates)
        best = max(best, max_here)
    return best


if __name__ == "__main__":
    # the trace from the header
    nums = [-2, 1, -3, 4, -1, 2, 1, -5, 4]
    assert max_subarray(nums) == 6
    assert max_subarray_range(nums) == (6, 3, 6)
    assert nums[3:7] == [4, -1, 2, 1]

    assert max_subarray([1]) == 1
    assert max_subarray([5, 4, -1, 7, 8]) == 23          # the whole array
    # all negative: the answer is the least bad single element, not 0
    assert max_subarray([-3, -1, -2]) == -1
    assert max_subarray_range([-3, -1, -2]) == (-1, 1, 1)

    # LC 918's examples
    assert max_subarray_circular([1, -2, 3, -2]) == 3
    assert max_subarray_circular([5, -3, 5]) == 10       # wraps: 5 + 5
    assert max_subarray_circular([-3, -2, -3]) == -2     # all negative

    # LC 152's examples
    assert max_product_subarray([2, 3, -2, 4]) == 6
    assert max_product_subarray([-2, 0, -1]) == 0
    assert max_product_subarray([-2, 3, -4]) == 24       # two negatives make a positive

    for function in (max_subarray, max_subarray_circular, max_product_subarray):
        try:
            function([])
            raise AssertionError("expected ValueError")
        except ValueError:
            pass

    # agree with trying every start and end
    import random

    random.seed(1)
    for _ in range(300):
        data = [random.randint(-6, 6) for _ in range(random.randint(1, 10))]
        n = len(data)

        best_sum = max(sum(data[i:j + 1]) for i in range(n) for j in range(i, n))
        assert max_subarray(data) == best_sum
        total, start, end = max_subarray_range(data)
        assert total == best_sum == sum(data[start:end + 1])

        doubled = data + data                            # a ring, unrolled twice
        best_ring = max(sum(doubled[i:i + length])
                        for i in range(n) for length in range(1, n + 1))
        assert max_subarray_circular(data) == best_ring

        best_product = None
        for i in range(n):
            product = 1
            for j in range(i, n):
                product *= data[j]
                if best_product is None or product > best_product:
                    best_product = product
        assert max_product_subarray(data) == best_product

    print("Success.")
