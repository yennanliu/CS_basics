#---------------------------------------------------------------
# BUCKET SORT
#---------------------------------------------------------------
#
# Do not compare every value with every other value: first DEAL the
# values into buckets by range, then sort each small bucket, then
# read the buckets back in order.
#
#     nums = [29, 25, 3, 49, 9, 37, 21, 43]      bucket_size = 10
#
#     bucket 0  [ 0..9 ]   3, 9
#     bucket 1  [10..19]
#     bucket 2  [20..29]   29, 25, 21   -> sorted: 21, 25, 29
#     bucket 3  [30..39]   37
#     bucket 4  [40..49]   49, 43       -> sorted: 43, 49
#
#     read back:  3, 9, 21, 25, 29, 37, 43, 49
#
#     a value's bucket:  (value - smallest) // bucket_size
#
# Bucket k holds only values smaller than everything in bucket k + 1,
# so once each bucket is sorted, concatenating them is the answer.
#
# WHEN IT IS FAST
#   Only when the values are spread EVENLY over the range. Then each
#   bucket holds a handful of values and the total work is linear.
#   If the values clump -- [1, 2, 3, 1000000] -- almost everything
#   lands in one bucket and it degrades to the sort used inside
#   (insertion sort here, so O(N^2)), with thousands of empty buckets
#   allocated for nothing.
#
# Time  : Avg O(N + K) for evenly spread input (K = number of buckets),
#         Worst O(N^2) (everything in one bucket)
# Space : O(N + K)
#
# Stable, because values are appended to a bucket in input order and
# insertion sort is stable.
#
# See also: counting_sort.py -- the special case of one bucket per value.


from insertion_sort import insertion_sort

DEFAULT_BUCKET_SIZE = 5


# V0
def bucket_sort(nums, bucket_size=DEFAULT_BUCKET_SIZE):
    """Return a new sorted list. Works for negative numbers and floats too."""
    if bucket_size <= 0:
        raise ValueError("bucket_size must be positive, got {}".format(bucket_size))
    if len(nums) == 0:
        return []

    min_value = min(nums)
    max_value = max(nums)

    # enough buckets to cover the whole range min_value .. max_value
    bucket_count = int((max_value - min_value) // bucket_size) + 1
    buckets = [[] for _ in range(bucket_count)]

    # deal: subtracting min_value shifts the range to start at bucket 0,
    # which is also what makes negative numbers work
    for num in nums:
        index = int((num - min_value) // bucket_size)
        buckets[index].append(num)

    # sort each bucket, then read them back in order
    result = []
    for bucket in buckets:
        result.extend(insertion_sort(bucket))
    return result


if __name__ == "__main__":
    assert bucket_sort([29, 25, 3, 49, 9, 37, 21, 43], bucket_size=10) == \
        [3, 9, 21, 25, 29, 37, 43, 49]
    assert bucket_sort([12, 23, 4, 5, 3, 2, 12, 81, 56, 95]) == \
        [2, 3, 4, 5, 12, 12, 23, 56, 81, 95]
    assert bucket_sort([]) == [] and bucket_sort([7]) == [7]

    # negatives and floats: the bucket index is relative to the minimum
    assert bucket_sort([3, -8, 0, -1, 5]) == [-8, -1, 0, 3, 5]
    assert bucket_sort([0.42, 0.07, 0.91, 0.33], bucket_size=0.25) == \
        [0.07, 0.33, 0.42, 0.91]

    # the input list is left alone
    original = [3, 1, 2]
    assert bucket_sort(original) == [1, 2, 3] and original == [3, 1, 2]

    # stable: 1.0 and 1 are equal and keep their original order
    assert repr(bucket_sort([1.0, 0, 1])) == "[0, 1.0, 1]"

    try:
        bucket_sort([1, 2], bucket_size=0)
        raise AssertionError("expected ValueError")
    except ValueError:
        pass

    # agree with the built-in sort, whatever the bucket size
    import random

    random.seed(1)
    for _ in range(200):
        data = [random.randint(-50, 50) for _ in range(random.randint(0, 20))]
        size = random.randint(1, 30)
        assert bucket_sort(data, bucket_size=size) == sorted(data)

    print("Success.")
