"""

1850. Minimum Adjacent Swaps to Reach the Kth Smallest Number
Medium

You are given a string num, representing a large integer, and an integer k.

We call some integer wonderful if it is a permutation of the digits in num and is greater in value than num. There can be many wonderful integers. However, we only care about the smallest-valued ones.

For example, when num = "5489355142":

The 1st smallest wonderful integer is "5489355214".
The 2nd smallest wonderful integer is "5489355241".
The 3rd smallest wonderful integer is "5489355412".
The 4th smallest wonderful integer is "5489355421".

Return the minimum number of adjacent digit swaps that needs to be applied to num to reach the kth smallest wonderful integer.

The tests are generated in such a way that kth smallest wonderful integer exists.


Example 1:

Input: num = "5489355142", k = 4
Output: 2
Explanation: The 4th smallest wonderful number is "5489355421". To get this number:
- Swap index 7 with index 8: "5489355142" -> "5489355412"
- Swap index 8 with index 9: "5489355412" -> "5489355421"

Example 2:

Input: num = "11112", k = 4
Output: 4
Explanation: The 4th smallest wonderful number is "21111". To get this number:
- Swap index 3 with index 4: "11112" -> "11121"
- Swap index 2 with index 3: "11121" -> "11211"
- Swap index 1 with index 2: "11211" -> "12111"
- Swap index 0 with index 1: "12111" -> "21111"

Example 3:

Input: num = "00123", k = 1
Output: 1
Explanation: The 1st smallest wonderful number is "00132". To get this number:
- Swap index 3 with index 4: "00123" -> "00132"


Constraints:

2 <= num.length <= 1000
1 <= k <= 1000
num only consists of digits.

"""

# V0
# IDEA : APPLY NEXT-PERMUTATION k TIMES, THEN GREEDY BUBBLE COUNT
#
#   part 1 : "the i-th smallest permutation greater than num" is literally
#   next_permutation applied i times, so run it k times to build the target.
#
#   part 2 : minimum adjacent swaps turning num into target. scan left to
#   right; at the first position i where they disagree, find the NEAREST j > i
#   with cur[j] == target[i] and bubble it left one step at a time (j - i
#   swaps). taking the nearest matching digit is optimal -- moving a further
#   duplicate would cross the nearer one and cost strictly more.
#
#   NOTE : k, n <= 1000 so k next-permutations is O(k*n) and the bubbling is
#          O(n^2) -- both comfortably fast.
#
# time = O(k*n + n^2), space = O(n)
class Solution(object):
    def getMinSwaps(self, num, k):
        def next_permutation(a):
            n = len(a)
            i = n - 2
            while i >= 0 and a[i] >= a[i + 1]:
                i -= 1
            if i < 0:
                return False
            j = n - 1
            while a[j] <= a[i]:
                j -= 1
            a[i], a[j] = a[j], a[i]
            a[i + 1:] = reversed(a[i + 1:])
            return True

        target = list(num)
        for _ in range(k):
            next_permutation(target)

        cur = list(num)
        n = len(cur)
        res = 0
        for i in range(n):
            if cur[i] == target[i]:
                continue
            j = i + 1
            while cur[j] != target[i]:
                j += 1
            while j > i:
                cur[j], cur[j - 1] = cur[j - 1], cur[j]
                j -= 1
                res += 1
        return res
