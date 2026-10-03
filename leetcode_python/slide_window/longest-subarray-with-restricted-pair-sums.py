"""

4067. Longest Subarray With Restricted Pair Sums
Medium

You are given an integer array nums.

A subarray nums[l..r] is valid if there are no three distinct indices i, j, and k
such that l <= i, j, k <= r and:

    nums[i] + nums[j] == nums[k]

Return the maximum length of a valid subarray of nums.

A subarray is a contiguous non-empty sequence of elements within an array.

Example 1:

Input: nums = [2,3,5,3,2,1]

Output: 3

Explanation:

Consider the subarray [3, 5, 3]. The pairs of elements at distinct indices have the
following sums:

    3 + 5 = 8
    3 + 3 = 6, using the two different occurrences of 3
    5 + 3 = 8

None of these sums is an element at the remaining index, so the subarray is valid.

Every subarray of length 4 contains 2, 3, and 5 at distinct indices, where
2 + 3 = 5. Therefore, no longer valid subarray exists, and the answer is 3.

Example 2:

Input: nums = [3,4,5,6]

Output: 4

Explanation:

The sums obtained from every pair of elements at distinct indices are 7, 8, 9, 9,
10, and 11. None of these values appears at the remaining index, so the entire
array is valid.

Constraints:

1 <= nums.length <= 1000
1 <= nums[i] <= 500

"""

# V0
class Solution(object):
    def maxSubarray(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        pass


# V0-1
# IDEA: SLIDE WINDOW + array val mapping (gpt)
class Solution(object):
    def maxSubarray(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """

        # Edge case
        if not nums:
            return 0

        n = len(nums)

        # nums[i] <= 500
        #
        # count[x] = how many times value x
        # appears in the current window.
        count = [0] * 501

        # Number of invalid triples
        # currently inside the window.
        #
        # Invalid triple:
        #
        # nums[i] + nums[j] == nums[k]
        #
        # where i, j, k are distinct indices.
        bad_count = 0

        max_len = 0
        l = 0

        for r in range(n):

            x = nums[r]

            # Add nums[r].
            #
            # Calculate how many NEW invalid triples
            # are created by adding x.
            bad_count += self.count_new_bad_triples(
                x,
                count
            )

            count[x] += 1

            # If window is invalid,
            # move left until it becomes valid.
            while bad_count > 0:

                x_left = nums[l]

                # Remove x_left first.
                count[x_left] -= 1

                # Calculate how many invalid triples
                # disappear because x_left was removed.
                bad_count -= self.count_new_bad_triples(
                    x_left,
                    count
                )

                l += 1

            # Current window [l, r] is valid.
            max_len = max(
                max_len,
                r - l + 1
            )

        return max_len

    def count_new_bad_triples(self, x, count):
        """
        Assume x is about to be added.

        Return the number of NEW invalid triples
        created by this occurrence of x.

        There are two cases:

        1. a + b = x
        2. x + y = z
        """

        new_bad = 0

        # -----------------------------------------
        # Case 1:
        #
        # a + b = x
        #
        # Example:
        #
        # x = 5
        # 2 + 3 = 5
        # -----------------------------------------

        for a in range(1, x):

            b = x - a

            # Avoid checking the same pair twice.
            #
            # Example:
            #
            # 2 + 3 = 5
            # 3 + 2 = 5
            #
            # These represent the same pair of values.
            if b < a:
                break

            if count[a] == 0 or count[b] == 0:
                continue

            if a == b:
                # Need two DIFFERENT indices.
                #
                # Example:
                #
                # [2, 2, 4]
                #
                # We need two 2s.
                if count[a] >= 2:
                    new_bad += (
                        count[a] * (count[a] - 1) // 2
                    )

            else:
                # Choose one a and one b.
                new_bad += count[a] * count[b]

        # -----------------------------------------
        # Case 2:
        #
        # x + y = z
        #
        # Example:
        #
        # x = 2
        # y = 3
        # z = 5
        #
        # The newly added x forms:
        #
        # 2 + 3 = 5
        # -----------------------------------------

        for y in range(1, 501 - x):

            z = x + y

            if count[y] == 0 or count[z] == 0:
                continue

            new_bad += count[y] * count[z]

        return new_bad


# V0-2
# IDEA: SLIDE WINDOW + hashmap (gemini)
"""
CORE IDEA:


-> explain `is_valid(x)` func below


-> 

    題目的核心限制是：**子陣列中不能存在 3 個不同位置（索引）的數字使得「數字 A + 數字 B = 數字 C」**。

    當滑動視窗加入一個新數字 $x$ 時，**舊視窗原本已經是合法的**。因此，如果加入 $x$ 後產生違規，這個新數字 $x$ **必定參與了這條等式**。

    $x$ 在等式中只有兩種可能扮演的角色：

    ---

    ### 角色 1：$x$ 充當「加數」（$x + a = b$）

    * **邏輯**：新加入的 $x$，跟視窗裡原本有的某個數字 $a$ 相加，**結果恰好也是視窗裡原本就有的數字 $b$**。
    * **舉例**：
    * 視窗原本有：`[2, 5]`
    * 新加入：$x = 3$
    * 檢查：$3(x) + 2(a) = 5(b)$。因為 $2$ 和 $5$ 都在視窗裡，違規！


    * **為什麼索引必定不同？**
    * $x$ 是最新位置，$a$ 與 $b$ 是視窗舊位置，這三個數字在陣列中的位置天然就是 3 個完全不同的索引。



    ---

    ### 角色 2：$x$ 充當「總和」（$a + b = x$）

    * **邏輯**：新加入的 $x$，能否由視窗裡原本的**兩個數字**相加湊出來？
    * 這裡必須區分「兩個加數是否相同」，因為題目嚴格限制「必須是 3 個不同位置」：

    #### 情況 2A：兩個加數不相同（$a \neq b$）

    * **舉例**：
    * 視窗原本有：`[2, 3]`
    * 新加入：$x = 5$
    * 檢查：$5 - 2 = 3$。$2$ 與 $3$ 是兩個不同的數字，分別佔用兩個不同位置，相加等於 $x(5)$，違規！



    #### 情況 2B：兩個加數相同（$a == b$，即 $x = 2a$）

    * **舉例**：
    * 新加入：$x = 6$，這需要兩個 $3$ 相加（$3 + 3 = 6$）。
    * **若視窗裡只有 1 個 $3$**（例如視窗為 `[3]`）：
    * 我們不能把「同一個位置的 $3$」重複使用兩次來湊成 $6$，因為這只佔用了 2 個索引位置。**因此不違規**。


    * **若視窗裡有 2 個（或更多）$3$**（例如視窗為 `[3, 3]`）：
    * 我們可以用第一個位置的 $3$ + 第二個位置的 $3$ = 第三個位置的 $6(x)$，湊滿了 3 個不同位置，**違規！**


    * 這就是為什麼程式碼在 $a == b$ 時，必須檢查 `count_a >= 2`（視窗內至少要有 2 個 $a$）。

"""
from collections import Counter


class Solution(object):

  def maxSubarray(self, nums):
    """:type nums: List[int]

    :rtype: int
    """
    n = len(nums)
    # 邊界特判：長度 <= 2 的子陣列無法湊齊 3 個相異索引，必定合法
    if n <= 2:
      return n

    freq = Counter()
    l = 0
    max_len = 0

    # 檢查加入 x = nums[r] 後，是否會與當前視窗內的元素構成 nums[i] + nums[j] == nums[k]
    def is_valid(x):
      for a, count_a in freq.items():
        # 情況 1：x 作為加數，與視窗中的 a 相加等於視窗中已有的元素 (x + a == existing)
        if (x + a) in freq:
          return False

        # 情況 2：x 作為和，等於視窗中的 a 與另一個元素相加 (a + other == x)
        other = x - a
        if other > 0 and other in freq:
          # 若 a != other，代表已有兩個相異數字；若 a == other，則需要視窗中至少有 2 個 a
          if a != other or count_a >= 2:
            return False

      return True

    # 滑動視窗 (Sliding Window)
    for r in range(n):
      x = nums[r]

      # 若加入 x 會破壞合法性，收縮左界 l 直到 x 可以合法加入
      while not is_valid(x):
        freq[nums[l]] -= 1
        if freq[nums[l]] == 0:
          del freq[nums[l]]
        l += 1

      # 將 x 加入視窗
      freq[x] += 1

      # 更新最大合法子陣列長度
      max_len = max(max_len, r - l + 1)

    return max_len


# V1-1
# IDEA : SLIDING WINDOW + PAIR-SUM HASHMAP, UPDATED INCREMENTALLY (claude)
#
#   validity is MONOTONE : every sub-window of a valid window is valid (dropping
#   elements can not create a triple). so two pointers work -- for each right
#   end, the smallest valid left end never moves backwards.
#
#   a window is invalid when some element equals the sum of two OTHER elements.
#   keep, over the window,
#
#     pair_sum_cnt[s] : how many index pairs (i < j) in the window sum to s
#     val_cnt[v]      : how many elements in the window equal v
#     conflicts       : how many (pair, k) matches  nums[i] + nums[j] == nums[k]
#
#   and the window is valid exactly when conflicts == 0.
#
#   the brute force (rebuild all pair sums for every window) is O(n^3) and
#   times out at n = 1000; the trick is that adding / removing ONE element
#   changes only the pairs it belongs to, so each step is O(window):
#
#     add nums[r]    : it is the "k" of every pair already summing to nums[r],
#                      and the addend of a new pair (i, r) for every i in the
#                      window, each matching the elements equal to that sum
#     remove nums[l] : the same two counts, subtracted
#
#   e.g. nums = [2,3,5,3,2,1] -> window [2,3,5] : pair 2+3 = 5 and 5 is in the
#        window -> conflicts = 1 -> drop 2 -> [3,5,3] : sums 8, 6, 8, none in
#        the window -> valid, length 3
#
#   NOTE !!! values are >= 1, so a pair's sum is strictly bigger than both of
#            its members : the k matching a pair (i, j) can never be i or j
#            itself, and "three DISTINCT indices" comes for free. (3 + 3 = 6
#            from two different 3s still counts, and the pair count sees it.)
#
# time = O(n^2), space = O(V)   (V = max(nums) <= 500 bounds both hashmaps)
from collections import defaultdict


class Solution(object):
    def maxSubarray(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        n = len(nums)
        # edge case: fewer than 3 elements can not form a triple -> whole array is valid
        if n <= 2:
            return n

        # pair sum -> number of index pairs (i < j) inside the window with that sum
        pair_sum_cnt = defaultdict(int)
        # value -> number of window elements equal to it
        val_cnt = defaultdict(int)
        # number of (pair, k) matches inside the window; window is valid iff 0
        conflicts = 0

        max_len = 0
        left = 0
        for right in range(n):
            val = nums[right]

            # 1) nums[right] as the "sum" k : every pair already summing to it
            conflicts += pair_sum_cnt[val]
            # 2) nums[right] as an addend : a new pair with every element in the window
            for i in range(left, right):
                pair_sum = nums[i] + val
                pair_sum_cnt[pair_sum] += 1
                conflicts += val_cnt[pair_sum]
            val_cnt[val] += 1

            # shrink from the left until no triple is left in the window
            while conflicts > 0:
                out = nums[left]
                val_cnt[out] -= 1
                # mirror of the two steps above, for the element leaving
                conflicts -= pair_sum_cnt[out]
                for i in range(left + 1, right + 1):
                    pair_sum = nums[i] + out
                    pair_sum_cnt[pair_sum] -= 1
                    conflicts -= val_cnt[pair_sum]
                left += 1

            max_len = max(max_len, right - left + 1)

        return max_len


# V1-2
# IDEA : FIX THE LEFT END, GROW THE RIGHT END (same counters, no removal) (claude)
#
#   same pair-sum hashmap as V0, but the window is rebuilt from scratch for each
#   left end and only ever grows, so there is no removal bookkeeping to get
#   wrong. by monotonicity the first right end that creates a triple ends the
#   run for that left end. every (left, right) pair is visited once, so it is
#   still O(n^2); easier to derive live, slightly more work in practice.
#
# time = O(n^2), space = O(V)
class Solution2(object):
    def maxSubarray(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        n = len(nums)
        # edge case: fewer than 3 elements can not form a triple
        if n <= 2:
            return n

        max_len = 0
        for left in range(n):
            # no window starting here can beat the best one found so far
            if n - left <= max_len:
                break

            pair_sum_cnt = defaultdict(int)
            val_cnt = defaultdict(int)

            right = left
            while right < n:
                val = nums[right]

                # would nums[right] be the sum of two elements already in the window ?
                if pair_sum_cnt[val] > 0:
                    break
                # would nums[right] plus some window element equal another window element ?
                creates_triple = False
                for i in range(left, right):
                    if val_cnt[nums[i] + val] > 0:
                        creates_triple = True
                        break
                if creates_triple:
                    break

                # safe to extend : record the new pairs and the new value
                for i in range(left, right):
                    pair_sum_cnt[nums[i] + val] += 1
                val_cnt[val] += 1
                right += 1

            # window [left, right) is the longest valid one starting at left
            max_len = max(max_len, right - left)

        return max_len


# V1-3
# IDEA : SLIDING WINDOW OVER VALUE COUNTS, CHECK ONLY THE NEW ELEMENT (claude)
#
#   V0 tracks every pair in the window. but the window before nums[right] joins
#   is already valid, so the ONLY triples that can exist afterwards are the ones
#   that use nums[right]. so instead of pair counts, keep just val_cnt over the
#   window and ask two questions about the newcomer v = nums[right] :
#
#     v is the sum    : is there a + b == v with a, b both in the window ?
#                       (a == b needs two copies of a)
#     v is an addend  : is there an a in the window with a + v also in the window ?
#
#   each question scans the value range once, O(V) with V = max(nums) <= 500,
#   and removing from the left is a plain val_cnt decrement. so the total is
#   O(n * V) instead of O(n^2), and the state is a 501-slot array.
#
#   NOTE : this is the tighter bound and the one to reach for in an interview
#          once V0 is understood -- it is V0 with the "only the newcomer can be
#          in a triple" observation applied.
#
# time = O(n * V), space = O(V)   (V = max(nums))
class Solution3(object):
    def maxSubarray(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        n = len(nums)
        # edge case: fewer than 3 elements can not form a triple
        if n <= 2:
            return n

        max_val = max(nums)
        # value -> number of window elements equal to it (values are 1..max_val)
        val_cnt = [0] * (max_val + 1)

        max_len = 0
        left = 0
        for right in range(n):
            val = nums[right]

            # the window without nums[right] is valid, so only triples using
            # nums[right] can appear : shrink until it joins no triple
            while self.joins_triple(val_cnt, val, max_val):
                val_cnt[nums[left]] -= 1
                left += 1

            val_cnt[val] += 1
            max_len = max(max_len, right - left + 1)

        return max_len

    def joins_triple(self, val_cnt, val, max_val):
        """True if `val` would form nums[i] + nums[j] == nums[k] with the window counted in val_cnt."""
        # case 1: val is the sum -> a + b == val with a <= b, both present (a == b needs two copies)
        for a in range(1, val // 2 + 1):
            b = val - a
            if a == b:
                if val_cnt[a] >= 2:
                    return True
            elif val_cnt[a] >= 1 and val_cnt[b] >= 1:
                return True

        # case 2: val is an addend -> a and a + val both present (a + val > a, so distinct indices)
        for a in range(1, max_val - val + 1):
            if val_cnt[a] >= 1 and val_cnt[a + val] >= 1:
                return True

        return False
