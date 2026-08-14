"""

1093. Statistics from a Large Sample
Medium

You are given a large sample of integers in the range [0, 255]. Since the sample
is so large, it is represented by an array count where count[k] is the number of
times that k appears in the sample.

Calculate the following statistics:

- minimum: The minimum element in the sample.
- maximum: The maximum element in the sample.
- mean: The average of the sample, calculated as the total sum of all elements
  divided by the total number of elements.
- median:
    - If the sample has an odd number of elements, then the median is the middle
      element once the sample is sorted.
    - If the sample has an even number of elements, then the median is the average
      of the two middle elements once the sample is sorted.
- mode: The number that appears the most in the sample. It is guaranteed to be unique.

Return the statistics of the sample as an array of floating-point numbers
[minimum, maximum, mean, median, mode]. Answers within 10^-5 of the actual answer
will be accepted.


Example 1:

Input: count = [0,1,3,4,0,0,...,0]   (256 entries, every entry after index 3 is 0)
Output: [1.00000,3.00000,2.37500,2.50000,3.00000]
Explanation: The sample represented by count is [1,2,2,2,3,3,3,3].
The minimum and maximum are 1 and 3 respectively.
The mean is (1+2+2+2+3+3+3+3) / 8 = 19 / 8 = 2.375.
Since the size of the sample is even, the median is the average of the two middle
elements 2 and 3, which is 2.5.
The mode is 3 as it appears the most in the sample.

Example 2:

Input: count = [0,4,3,2,2,0,0,...,0]   (256 entries, every entry after index 4 is 0)
Output: [1.00000,4.00000,2.18182,2.00000,1.00000]
Explanation: The sample represented by count is [1,1,1,1,2,2,2,3,3,4,4].
The minimum and maximum are 1 and 4 respectively.
The mean is (1+1+1+1+2+2+2+3+3+4+4) / 11 = 24 / 11 = 2.18181818...
Since the size of the sample is odd, the median is the middle element 2.
The mode is 1 as it appears the most in the sample.


Constraints:

count.length == 256
0 <= count[i] <= 10^9
1 <= sum(count) <= 10^9
The mode of the sample that count represents is unique.

"""

# V0
# IDEA: COUNTING ARRAY (bucket) + prefix walk for the median
#
#   -> the sample is never materialized (it can hold 10^9 numbers),
#      everything is derived from the 256 buckets.
#      the median is found by walking the buckets until the
#      running prefix count reaches the wanted 1-based rank.
#
# time = O(256)
# space = O(1)
class Solution(object):
    def sampleStats(self, count):
        # k-th (1-based) smallest element of the sample
        def kth(k):
            acc = 0
            for v, c in enumerate(count):
                acc += c
                if acc >= k:
                    return v
            return -1

        mn = -1
        mx = -1
        total = 0
        n = 0
        mode = 0

        for v, c in enumerate(count):
            if c == 0:
                continue
            if mn < 0:
                mn = v
            mx = v
            total += v * c
            n += c
            if c > count[mode]:
                mode = v

        mean = float(total) / n

        # NOTE !!! odd -> single middle, even -> avg of the 2 middles
        if n % 2 == 1:
            median = float(kth(n // 2 + 1))
        else:
            median = (kth(n // 2) + kth(n // 2 + 1)) / 2.0

        return [float(mn), float(mx), mean, median, float(mode)]
