#---------------------------------------------------------------
# DIFFERENCE ARRAY
#---------------------------------------------------------------
#
# Apply MANY "add x to every element from index l to index r" updates
# in O(1) each, and pay for them all at once at the end.
#
# The naive way touches every element of every range: Q updates on N
# elements is O(Q * N). The difference array records only where each
# update STARTS and STOPS:
#
#     diff[l]     += x      "from here on, everything is x higher"
#     diff[r + 1] -= x      "... and from here on, it is not any more"
#
# A running total (prefix sum) over diff then rebuilds the real values.
#
#     5 elements, all 0.   add 10 to [1..3],  add 5 to [2..4]
#
#     index            0    1    2    3    4    (5)
#     after update 1   0  +10    0    0  -10
#     after update 2   0  +10   +5    0  -10    -5     <- diff
#     running total    0   10   15   15    5           <- the result
#
#     check:           0   10   10+5 10+5   5    correct
#
# It is the mirror image of a prefix-sum array:
#
#     prefix sum        build O(N), then range SUM   in O(1), no updates
#     difference array  range UPDATE in O(1), then rebuild in O(N)
#     Fenwick tree      both, mixed in any order, O(log N) each
#                       (data_structure/python/binary_indexed_tree.py)
#
# So use it when ALL the updates come first and the values are only
# read afterwards.
#
# Time  : O(1) per update, O(N) to read the result -> O(N + Q) in total
# Space : O(N)
#
# Used by: LC 370 Range Addition, LC 1109 Corporate Flight Bookings,
#          LC 1094 Car Pooling, LC 2381 Shifting Letters II, and any
#          "how many intervals cover each point?" question.


class DifferenceArray:
    """Range updates in O(1); the final values on demand in O(N)."""

    def __init__(self, nums):
        self.n = len(nums)
        # diff[i] = nums[i] - nums[i-1]: the STEP up or down at index i.
        # One extra slot at the end absorbs the "stop" write of a range
        # that runs to the last element, so update() needs no bounds check.
        self.diff = [0] * (self.n + 1)
        previous = 0
        for i in range(self.n):
            self.diff[i] = nums[i] - previous
            previous = nums[i]

    def update(self, left, right, value):
        """Add `value` to every element in nums[left..right], both ends INCLUDED."""
        if not 0 <= left <= right < self.n:
            raise IndexError("bad range: [{}, {}]".format(left, right))
        self.diff[left] += value             # the step up, at the start of the range
        self.diff[right + 1] -= value        # the step back down, just past its end

    def result(self):
        """The current values: a running total over the steps."""
        values = []
        running = 0
        for i in range(self.n):
            running += self.diff[i]
            values.append(running)
        return values


# LC 370 : Range Addition -- start from zeros, apply [start, end, inc] updates
def range_addition(length, updates):
    array = DifferenceArray([0] * length)
    for start, end, increment in updates:
        array.update(start, end, increment)
    return array.result()


# LC 1109 : Corporate Flight Bookings -- flights are numbered from 1,
# each booking [first, last, seats] reserves seats on flights first..last
def corp_flight_bookings(bookings, n):
    array = DifferenceArray([0] * n)
    for first, last, seats in bookings:
        array.update(first - 1, last - 1, seats)     # 1-based -> 0-based
    return array.result()


# LC 1094 : Car Pooling -- trips are [passengers, from, to]; people get
# off AT `to`, so they occupy the car on stops from..to-1
def car_pooling(trips, capacity):
    if not trips:
        return True
    last_stop = max(to for _, _, to in trips)
    array = DifferenceArray([0] * last_stop)
    for passengers, start, to in trips:
        if start < to:
            array.update(start, to - 1, passengers)
    return max(array.result()) <= capacity


if __name__ == "__main__":
    # the trace from the header
    array = DifferenceArray([0, 0, 0, 0, 0])
    array.update(1, 3, 10)
    assert array.diff == [0, 10, 0, 0, -10, 0]
    array.update(2, 4, 5)
    assert array.diff == [0, 10, 5, 0, -10, -5]
    assert array.result() == [0, 10, 15, 15, 5]

    # starting from existing values, and with a negative update
    prices = DifferenceArray([8, 2, 6, 3, 1])
    assert prices.result() == [8, 2, 6, 3, 1]            # no updates: unchanged
    prices.update(0, 4, -1)                              # the whole array
    prices.update(2, 2, 100)                             # a single element
    assert prices.result() == [7, 1, 105, 2, 0]

    assert range_addition(5, [[1, 3, 2], [2, 4, 3], [0, 2, -2]]) == [-2, 0, 3, 5, 3]
    assert corp_flight_bookings([[1, 2, 10], [2, 3, 20], [2, 5, 25]], 5) == [10, 55, 45, 25, 25]
    assert corp_flight_bookings([[1, 2, 10], [2, 2, 15]], 2) == [10, 25]
    assert car_pooling([[2, 1, 5], [3, 3, 7]], 4) is False
    assert car_pooling([[2, 1, 5], [3, 3, 7]], 5) is True
    assert car_pooling([[2, 1, 5], [3, 5, 7]], 3) is True    # they get off as the others get on
    assert car_pooling([], 1) is True

    try:
        array.update(3, 5, 1)                            # index 5 does not exist
        raise AssertionError("expected IndexError")
    except IndexError:
        pass

    # agree with updating every element one by one
    import random

    random.seed(1)
    for _ in range(200):
        n = random.randint(1, 12)
        plain = [random.randint(-5, 5) for _ in range(n)]
        fast = DifferenceArray(plain)
        for _ in range(random.randint(0, 10)):
            left = random.randrange(n)
            right = random.randrange(left, n)
            value = random.randint(-9, 9)
            fast.update(left, right, value)
            for i in range(left, right + 1):
                plain[i] += value
        assert fast.result() == plain

    print("Success.")
