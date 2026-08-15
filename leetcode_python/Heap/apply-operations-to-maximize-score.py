"""

2818. Apply Operations to Maximize Score
Hard

You are given an array nums of n positive integers and an integer k.

Initially, you start with a score of 1. You have to maximize your score by applying the following operation at most k times:

Choose any non-empty subarray nums[l, ..., r] that you haven't chosen previously.
Choose an element x of nums[l, ..., r] with the highest prime score. If multiple such elements exist, choose the one with the smallest index.
Multiply your score by x.

Here, nums[l, ..., r] denotes the subarray of nums starting at index l and ending at the index r, both ends being inclusive.

The prime score of an integer x is equal to the number of distinct prime factors of x. For example, the prime score of 300 is 3 since 300 = 2 * 2 * 3 * 5 * 5.

Return the maximum possible score after applying at most k operations.

Since the answer may be large, return it modulo 10^9 + 7.


Example 1:

Input: nums = [8,3,9,3,8], k = 2
Output: 81
Explanation: To get a score of 81, we can apply the following operations:
- Choose subarray nums[2, ..., 2]. nums[2] is the only element in this subarray. Hence, we multiply the score by nums[2]. The score becomes 1 * 9 = 9.
- Choose subarray nums[2, ..., 3]. Both nums[2] and nums[3] have a prime score of 1, but nums[2] has the smaller index. Hence, we multiply the score by nums[2]. The score becomes 9 * 9 = 81.
It can be proven that 81 is the highest score one can obtain.

Example 2:

Input: nums = [19,12,14,6,10,18], k = 3
Output: 4788
Explanation: To get a score of 4788, we can apply the following operations:
- Choose subarray nums[0, ..., 0]. nums[0] is the only element in this subarray. Hence, we multiply the score by nums[0]. The score becomes 1 * 19 = 19.
- Choose subarray nums[5, ..., 5]. nums[5] is the only element in this subarray. Hence, we multiply the score by nums[5]. The score becomes 19 * 18 = 342.
- Choose subarray nums[2, ..., 3]. Both nums[2] and nums[3] have a prime score of 2, but nums[2] has the smaller index. Hence, we multipy the score by nums[2]. The score becomes 342 * 14 = 4788.
It can be proven that 4788 is the highest score one can obtain.


Constraints:

1 <= nums.length == n <= 10^5
1 <= nums[i] <= 10^5
1 <= k <= min(n * (n + 1) / 2, 10^9)


"""

# V0
# IDEA : PRIME SCORE (LINEAR SIEVE) + MONOTONIC STACK "DOMINANCE RANGE"
#        + GREEDY (TAKE THE BIGGEST VALUES FIRST)
#
#   Step 1 - prime score : a sieve of smallest-prime-factor style pass over
#            1..max(nums) lets us count DISTINCT prime factors of every value
#            in O(V log log V) total, far cheaper than factoring each number.
#
#   Step 2 - how many subarrays does index i "win"? Index i is the chosen
#            element of nums[l..r] exactly when it beats everything else in
#            that window under the (prime score, then smallest index) rule:
#              - every j in [l, i-1] must have score[j] <  score[i]
#              - every j in [i+1, r] must have score[j] <= score[i]
#            NOTE the asymmetry - that is precisely the "smallest index wins
#            ties" tie-break, and getting it wrong double counts subarrays.
#            Two monotonic-stack passes give
#              left[i]  = last index on the left with score >= score[i]
#              right[i] = first index on the right with score >  score[i]
#            and the count is cnt[i] = (i - left[i]) * (right[i] - i).
#            Every subarray has exactly one winner, so sum(cnt) = n(n+1)/2.
#
#   Step 3 - greedy : each operation costs one distinct subarray and multiplies
#            the score by that subarray's winner. Since all values are >= 1,
#            we simply spend our k operations on the LARGEST values first,
#            taking min(cnt[i], k remaining) copies of value nums[i].
#            Sorting by value descending is the whole "priority queue" here.
#
#   NOTE : use pow(v, take, MOD) - repeated multiplication would be O(k) with
#          k up to 10^9. The exponent must NOT be taken mod anything.
#
# time = O(V log log V + n log n), space = O(V + n)   # V = max(nums) <= 10^5
class Solution(object):
    def maximumScore(self, nums, k):
        MOD = 10 ** 9 + 7
        n = len(nums)
        top = max(nums)

        # --- step 1 : distinct prime factor count for every value <= top ---
        omega = [0] * (top + 1)
        for p in range(2, top + 1):
            if omega[p] == 0:            # p is prime (untouched so far)
                for mult in range(p, top + 1, p):
                    omega[mult] += 1
        score = [omega[v] for v in nums]

        # --- step 2 : dominance range of each index via monotonic stacks ---
        left = [-1] * n                  # last j < i with score[j] >= score[i]
        stack = []
        for i in range(n):
            while stack and score[stack[-1]] < score[i]:
                stack.pop()
            left[i] = stack[-1] if stack else -1
            stack.append(i)

        right = [n] * n                  # first j > i with score[j] > score[i]
        stack = []
        for i in range(n - 1, -1, -1):
            while stack and score[stack[-1]] <= score[i]:
                stack.pop()
            right[i] = stack[-1] if stack else n
            stack.append(i)

        # --- step 3 : greedily spend k operations on the largest values ---
        order = sorted(range(n), key=lambda i: -nums[i])
        ans = 1
        for i in order:
            if k <= 0:
                break
            cnt = (i - left[i]) * (right[i] - i)
            take = cnt if cnt < k else k
            ans = ans * pow(nums[i], take, MOD) % MOD
            k -= take
        return ans
