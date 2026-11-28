#---------------------------------------------------------------
# COUNTING SORT
#---------------------------------------------------------------
#
# Sort small integers WITHOUT comparing them: count how many times
# each value occurs, then write the values back out in order.
#
#     nums   = [3, 1, 2, 3, 0, 1, 3]
#
#     value     0  1  2  3
#     count     1  2  1  3          one pass to count
#
#     output :  0 | 1 1 | 2 | 3 3 3          one pass to write
#
# No comparison happens, which is how it gets under the O(N log N)
# floor that every comparison sort (merge, quick, heap) is stuck at.
# The price: it only works on integers in a KNOWN, SMALL range, and it
# needs one counter per possible value.
#
#     N = how many values,  K = size of the value range (max - min + 1)
#
# Time  : O(N + K)
# Space : O(K) for the counts (+ O(N) for the output)
#
# Good:  ages, grades, bytes, characters -- K is tiny and fixed
# Bad:   [1, 1000000000] -- two values, a billion counters
#
# Used by: LC 75 Sort Colors (K = 3), LC 1122 Relative Sort Array,
#          LC 274 H-Index, and as the per-digit pass inside radix sort.
#
# See also: bucket_sort.py -- the generalisation to ranges of values.


# V0 : count, then rebuild -- right when the values ARE the data
def counting_sort(nums):
    """Return a new sorted list of integers."""
    if len(nums) == 0:
        return []

    smallest = min(nums)
    largest = max(nums)

    # counts[i] = how many times the value (smallest + i) appears.
    # Offsetting by `smallest` lets the range start anywhere, including
    # below zero.
    counts = [0] * (largest - smallest + 1)
    for num in nums:
        counts[num - smallest] += 1

    result = []
    for offset in range(len(counts)):
        value = smallest + offset
        for _ in range(counts[offset]):
            result.append(value)
    return result


# V1 : stable version -- right when each value is a KEY with a record
# attached (sort people by age, keep equal ages in input order). This
# is the form radix sort depends on.
#
# The trick is a running total: after it, starts[k] is the position in
# the output where the FIRST item with key k belongs.
#
#     keys      0  1  2  3
#     count     1  2  1  3
#     starts    0  1  3  4       (0, 0+1, 1+2, 3+1)
def counting_sort_by_key(items, key):
    """Return a new list of `items` sorted by the integer key(item), stably."""
    if len(items) == 0:
        return []

    keys = [key(item) for item in items]
    smallest = min(keys)
    largest = max(keys)

    counts = [0] * (largest - smallest + 1)
    for k in keys:
        counts[k - smallest] += 1

    # turn the counts into starting positions
    starts = [0] * len(counts)
    position = 0
    for offset in range(len(counts)):
        starts[offset] = position
        position += counts[offset]

    # place each item at the next free slot for its key. Walking the
    # input front to back is what keeps equal keys in their original order.
    result = [None] * len(items)
    for item, k in zip(items, keys):
        result[starts[k - smallest]] = item
        starts[k - smallest] += 1
    return result


def age_of(person):
    return person[1]


if __name__ == "__main__":
    assert counting_sort([3, 1, 2, 3, 0, 1, 3]) == [0, 1, 1, 2, 3, 3, 3]
    assert counting_sort([]) == [] and counting_sort([7]) == [7]
    assert counting_sort([2, -3, 0, -3, 1]) == [-3, -3, 0, 1, 2]     # negatives

    # LC 75 Sort Colors: three possible values, so K = 3
    assert counting_sort([2, 0, 2, 1, 1, 0]) == [0, 0, 1, 1, 2, 2]

    # records sorted by a key; equal ages stay in input order (stable)
    people = [("ann", 30), ("bob", 25), ("cat", 30), ("dan", 25), ("eve", 41)]
    assert counting_sort_by_key(people, age_of) == \
        [("bob", 25), ("dan", 25), ("ann", 30), ("cat", 30), ("eve", 41)]
    assert counting_sort_by_key([], age_of) == []

    # agree with the built-in sort (which is also stable)
    import random

    random.seed(1)
    for _ in range(200):
        data = [random.randint(-20, 20) for _ in range(random.randint(0, 30))]
        assert counting_sort(data) == sorted(data)
        tagged = [(index, value) for index, value in enumerate(data)]
        assert counting_sort_by_key(tagged, age_of) == sorted(tagged, key=age_of)

    print("Success.")
