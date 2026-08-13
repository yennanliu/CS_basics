"""

1338. Reduce Array Size to The Half
Medium

You are given an integer array arr. You can choose a set of integers and
remove all the occurrences of these integers in the array.

Return the minimum size of the set so that at least half of the integers
of the array are removed.


Example 1:

Input: arr = [3,3,3,3,5,5,5,2,2,7]
Output: 2
Explanation: Choosing {3,7} will make the new array [5,5,5,2,2] which has
size 5 (i.e equal to half of the size of the old array).
Possible sets of size 2 are {3,5},{3,2},{5,2}.
Choosing set {2,7} is not possible as it will make the new array
[3,3,3,3,5,5,5] which has a size greater than half of the size of the
old array.

Example 2:

Input: arr = [7,7,7,7,7,7]
Output: 1
Explanation: The only possible set you can choose is {7}.
This will make the new array empty.


Constraints:

2 <= arr.length <= 10^5
arr.length is even.
1 <= arr[i] <= 10^5

"""

# V0
# IDEA: GREEDY + COUNTER
#
#  to remove as many elements as possible with as few distinct values
#  as possible -> always pick the value with the LARGEST frequency first.
#
# time = O(n log n)
# space = O(n)
from collections import Counter
class Solution(object):
    def minSetSize(self, arr):
        n = len(arr)
        cnt = Counter(arr)

        removed = 0
        res = 0
        # most_common() -> sorted by frequency, high to low
        for _, freq in cnt.most_common():
            removed += freq
            res += 1
            if removed * 2 >= n:
                break
        return res


# V0-1
# IDEA: GREEDY + COUNTING SORT on frequency
#  distinct trick: frequencies are bounded by n, so bucket them -> O(n)
# time = O(n)
# space = O(n)
from collections import Counter
class Solution(object):
    def minSetSize(self, arr):
        n = len(arr)
        cnt = Counter(arr)

        # bucket[f] = how many distinct values have frequency f
        bucket = [0] * (n + 1)
        for freq in cnt.values():
            bucket[freq] += 1

        removed = 0
        res = 0
        f = n
        while f > 0:
            take = bucket[f]
            while take > 0 and removed * 2 < n:
                removed += f
                res += 1
                take -= 1
            if removed * 2 >= n:
                break
            f -= 1
        return res
