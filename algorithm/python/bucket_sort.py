#---------------------------------------------------------------
#  BUCKET SORT
#---------------------------------------------------------------
#
# Distribute elements into buckets by value range, sort each bucket
# (here with insertion sort), then concatenate the buckets.
#
# Time  : Avg O(N + K), Worst O(N^2) (everything in one bucket)
# Space : O(N + K)


from insertion_sort import insertion_sort

DEFAULT_BUCKET_SIZE = 5


# V0
def bucket_sort(nums, bucket_size=DEFAULT_BUCKET_SIZE):
    if len(nums) == 0:
        return []

    min_value, max_value = min(nums), max(nums)

    # create enough buckets to cover the value range
    bucket_count = (max_value - min_value) // bucket_size + 1
    buckets = [[] for _ in range(bucket_count)]

    # distribute the input into buckets
    for num in nums:
        buckets[(num - min_value) // bucket_size].append(num)

    # sort each bucket, then concatenate them in order
    result = []
    for bucket in buckets:
        result.extend(insertion_sort(bucket))
    return result


if __name__ == "__main__":
    assert bucket_sort([12, 23, 4, 5, 3, 2, 12, 81, 56, 95]) == \
        [2, 3, 4, 5, 12, 12, 23, 56, 81, 95]
    print("Success.")
