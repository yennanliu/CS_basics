"""

4071. Minimum Rotations to Dial a Number II
Medium

You are given an integer n and a string s of length n consisting of digits.

The dial contains the digits 0 through 9 in order and is circular, so 0 and
9 are adjacent. The pointer initially points to 0.

To dial each digit of s in order, rotate the pointer until it points to that
digit. Each rotation moves the pointer to an adjacent digit, and you may
rotate in either direction. Dialing a digit that the pointer already points
to requires no rotations.

Before dialing, you may perform the following operation at most once:

Choose an index k such that 0 <= k < n and reverse the suffix s[k..n - 1].

Return the minimum total number of rotations needed to dial the string after
optimally choosing whether to perform the operation and which suffix to
reverse.

Example 1:

Input: n = 4, s = "1502"

Output: 9

Explanation:

Reverse the suffix starting at k = 1 to obtain "1205", then dial it.
0 -> 1 : 1,  1 -> 2 : 1,  2 -> 0 : 2,  0 -> 5 : 5

The total is 1 + 1 + 2 + 5 = 9, which is the minimum total number of
rotations.

Example 2:

Input: n = 4, s = "2916"

Output: 12

Explanation:

Choose not to reverse a suffix and dial "2916".
0 -> 2 : 2,  2 -> 9 : 3,  9 -> 1 : 2,  1 -> 6 : 5

The total is 2 + 3 + 2 + 5 = 12, which is the minimum total number of
rotations.

Example 3:

Input: n = 4, s = "4219"

Output: 6

Explanation:

Reverse the suffix starting at k = 0, which reverses the entire string, to
obtain "9124", then dial it.
0 -> 9 : 1,  9 -> 1 : 2,  1 -> 2 : 1,  2 -> 4 : 2

The total is 1 + 2 + 1 + 2 = 6, which is the minimum total number of
rotations.

Constraints:

1 <= n == s.length <= 10^5
s consists only of digits '0' to '9'

"""


# V0
class Solution(object):
    def minRotations(self, n, s):
        """
        :type n: int
        :type s: str
        :rtype: int
        """
        pass


# V0-1
# IDEA: SUFFIX/EDGE changes + MATH (GPT)
"""
NOTE !!!! (key of this LC)


    new_op = base_total - old_cost + new_cost


    ->

    令 base_total 為原始字串的總旋轉成本。


    當反轉位置 i > 0 時：

    ->

        ```
        candidate = base_total - dist(s[i-1], s[i]) + dist(s[n-1], s[i-1])
        ```

    ->

    意思就是：
        - 減去原本進入 suffix 的轉移成本。
        - 加上反轉後進入 suffix 的轉移成本。
        - 其他轉移成本完全不變。
"""


"""
Dry run

->


        ## LC 4071 - Dry Run

        使用範例：

        n = 4
        s = "1502"

        公式：

        candidate = base_total - dist(s[i-1], s[i]) + dist(s[n-1], s[i-1])

        ### 1. 計算 base_total

        原始字串為 "1502"，從數字 0 開始轉動。

        | 轉移 | 計算 | 旋轉成本 |
        |---|---|---|
        | 0 -> 1 | min(1, 9) | 1 |
        | 1 -> 5 | min(4, 6) | 4 |
        | 5 -> 0 | min(5, 5) | 5 |
        | 0 -> 2 | min(2, 8) | 2 |
        | 總計 | | 12 |

        因此：

        base_total = 12

        ### 2. 當 i = 1 時

        反轉 suffix s[1:]：

        Original: "1502"
        Reversed: "1205"

        原本的轉移是 1 -> 5，反轉後變成 1 -> 2。

        #### 第一步：減去原本的轉移成本

        dist(s[i-1], s[i])
        = dist(s[0], s[1])
        = dist(1, 5)
        = min(4, 6)
        = 4

        #### 第二步：加上反轉後的轉移成本

        dist(s[n-1], s[i-1])
        = dist(s[3], s[0])
        = dist(2, 1)
        = min(1, 9)
        = 1

        #### 第三步：計算 candidate

        candidate = base_total - old_cost + new_cost
        candidate = 12 - 4 + 1
        candidate = 9

        因此，反轉後的總旋轉成本為 9。

        ### 3. 為什麼只需要調整一條邊？

        原始轉移：

        0 -> 1 -> 5 -> 0 -> 2

        反轉 suffix 後：

        0 -> 1 -> 2 -> 0 -> 5

        比較兩者：

        - 0 -> 1：成本不變。
        - 1 -> 5 改成 1 -> 2：邊界成本改變。
        - suffix 內部的轉移順序反轉，但因為 dist(a, b) = dist(b, a)，內部總成本不變。

        所以只需要從原始總成本減去舊的邊界成本，再加上新的邊界成本。

        ### 4. 面試記憶點

        反轉 suffix 不會改變 suffix 內部所有轉移的總成本，因為環形數字盤的距離具有對稱性。

        因此，不需要真的建立反轉後的字串，只需要調整進入 suffix 的邊界成本，就能把時間複雜度從 O(N^2) 降低到 O(N)。


"""
class Solution(object):
    def minRotations(self, n, s):
        """
        :type n: int
        :type s: str
        :rtype: int
        """
        # Edge case
        if not s:
            return 0

        n = len(s)

        # Step 1: Calculate the original total rotation cost.
        base_total = self.get_total_op(s)

        # Option 1: Do not reverse any suffix.
        res = base_total

        # Step 2: Try reversing each suffix s[i:].
        for i in range(n):

            # Case 1) i == 0
            # NOTE !!! for i == 0, it's a special case, need to handle specifically
            if i == 0:
                # Reverse the entire string.
                # The first digit changes from s[0] to s[-1].
                old_cost = self.get_distance(0, int(s[0]))
                new_cost = self.get_distance(0, int(s[-1]))
            
            #  Case 2) i != 0
            else:
                # The transition entering the suffix changes:
                # s[i - 1] -> s[i]
                # becomes
                # s[i - 1] -> s[-1]
                prev = int(s[i - 1])

                old_cost = self.get_distance(prev, int(s[i]))
                new_cost = self.get_distance(prev, int(s[-1]))


            """
            NOTE !!!! (key of this LC)


            new_op = base_total - old_cost + new_cost

            
            ->

            令 base_total 為原始字串的總旋轉成本。

            
            當反轉位置 i > 0 時：

            ->

                ```
                candidate = base_total - dist(s[i-1], s[i]) + dist(s[n-1], s[i-1])
                ```

            ->

            意思就是：
                - 減去原本進入 suffix 的轉移成本。
                - 加上反轉後進入 suffix 的轉移成本。
                - 其他轉移成本完全不變。


            """
            # Only replace the changed transition cost.
            candidate = base_total - old_cost + new_cost

            res = min(res, candidate)

        return res


    """
    NOTE !!!

    below helper func
    """
    def get_total_op(self, s):
        # The pointer initially starts at digit 0.
        total_op = self.get_distance(0, int(s[0]))

        # Add the cost between every adjacent pair.
        for i in range(1, len(s)):
            prev = int(s[i - 1])
            curr = int(s[i])

            total_op += self.get_distance(prev, curr)

        return total_op

    def get_distance(self, a, b):
        # Minimum distance on a circular dial from 0 to 9.
        direct = abs(a - b)
        return min(direct, 10 - direct)



# V0-1
# IDEA: SUFFIX/EDGE changes + MATH (gemini)
class Solution(object):

  def minRotations(self, n, s):
    """:type n: int

    :type s: str
    :rtype: int
    """
    if not s:
      return 0

    # 1. 輔助函式：計算環形撥號盤上兩個數字間的最短距離 (總長 10)
    def dist(a, b):
      d = abs(int(a) - int(b))
      return min(d, 10 - d)

    # 2. 計算原始字串內部相鄰數字的總步數 T
    total_internal = sum(dist(s[i - 1], s[i]) for i in range(1, n))

    # 情況 A: 不翻轉 (k 不變) -> 從 0 走到 s[0]
    cost_orig = dist('0', s[0]) + total_internal

    # 情況 B: k = 0 (翻轉整條字串 s[0..n-1]) -> 從 0 走到原本的末尾 s[-1]
    cost_rev_all = dist('0', s[-1]) + total_internal

    min_cost = min(cost_orig, cost_rev_all)

    # 情況 C: 1 <= k < n (翻轉後綴 s[k..n-1])
    # 只需要將邊 (s[k-1] -> s[k]) 替換為 (s[k-1] -> s[-1])
    for k in range(1, n):
      cur_cost = (
          dist('0', s[0])
          + (total_internal - dist(s[k - 1], s[k]))
          + dist(s[k - 1], s[-1])
      )
      min_cost = min(min_cost, cur_cost)

    return min_cost


# V0-2
# IDEA: SUFFIX/EDGE changes + MATH (GPT)
class Solution(object):
    def minRotations(self, n, s):
        """
        :type n: int
        :type s: str
        :rtype: int
        """
        if not s:
            return 0

        n = len(s)

        # Keep the original cost unchanged.
        original_total = self.helper(s)

        # The best answer cannot be worse than doing nothing.
        min_rotate = original_total

        for i in range(n):
            # Digit immediately before the suffix
            prev = 0 if i == 0 else int(s[i - 1])

            # Original transition: prev -> s[i]
            old_cost = self.get_distance(prev, int(s[i]))

            # After reversing s[i:], the suffix starts with s[-1].
            new_cost = self.get_distance(prev, int(s[-1]))

            # Only the transition entering the suffix changes.
            candidate = original_total - old_cost + new_cost

            min_rotate = min(min_rotate, candidate)

        return min_rotate

    def helper(self, s):
        op = 0
        pos = 0

        for x in s:
            val = int(x)
            op += self.get_distance(pos, val)
            pos = val

        return op

    def get_distance(self, a, b):
        direct = abs(a - b)
        return min(direct, 10 - direct)


# V0-3
# IDEA: BRUTE FORCE (TLE)
class Solution(object):

  def minRotations(self, n, s):
    """:type n: int

    :type s: str
    :rtype: int
    """
    if not s:
      return 0

    n = len(s)
    min_rotate = self.helper(s)  # 情況 1: 不翻轉

    for i in range(n):
      # 修正：後綴切片應為 s[i:] 而非 s[i+1:]
      tmp_s = s[:i] + s[i:][::-1]
      min_rotate = min(min_rotate, self.helper(tmp_s))

    return min_rotate

  def helper(self, s):
    op = 0
    pos = 0  # 撥號盤指針初值為 0

    for x in s:
      val = int(x)
      diff = abs(val - pos)
      op += min(diff, 10 - diff)
      pos = val

    return op


# V0-4
# IDEA : TRY EVERY k + PREFIX / SUFFIX SUMS (price each reversal in O(1))
#
#   Brute force (the contest draft): build s[:k] + s[k:][::-1] for every k and
#   re-dial it -> O(n^2), TLE at n = 10^5. (The draft also built
#   s[:i] + s[i+1:][::-1], which DROPS s[i] - the suffix starts AT k.)
#
#   Split the cost of dialing s[:k] + reversed(s[k:]) into 3 parts:
#
#       1) prefix[k]   = cost to dial s[0..k-1] starting from 0
#       2) the jump    = dist(s[k-1], s[n-1])   (or dist(0, s[n-1]) if k == 0)
#                        -> reversed, the suffix starts with its LAST digit
#       3) inner[k]    = sum of dist(s[i], s[i+1]) for i in [k, n-2]
#
#   Part 3 is why this works: dist is symmetric, so walking the suffix
#   backwards costs exactly what walking it forwards does.
#   k = n - 1 reverses one digit, which is the same as not reversing at all,
#   so "no operation" is already one of the candidates.
#
#   e.g. s = "1502", k = 1 -> prefix = dist(0,1) = 1
#                             jump   = dist(1,2) = 1
#                             inner  = dist(5,0) + dist(0,2) = 5 + 2 = 7
#                             total  = 9
#
# time = O(n), space = O(n)
DIAL_SIZE = 10


class Solution(object):
    def minRotations(self, n, s):
        """
        :type n: int
        :type s: str
        :rtype: int
        """
        # edge case: nothing to dial
        if not s:
            return 0

        n = len(s)
        digits = [int(ch) for ch in s]

        # prefix[k] = cost to dial digits[0..k-1], pointer starting at 0
        prefix = [0] * (n + 1)
        pos = 0
        for i in range(n):
            prefix[i + 1] = prefix[i] + self.dist(pos, digits[i])
            pos = digits[i]

        # inner[k] = cost of walking digits[k..n-1] between adjacent digits
        #            (same in either direction, since dist is symmetric)
        inner = [0] * n
        for k in range(n - 2, -1, -1):
            inner[k] = inner[k + 1] + self.dist(digits[k], digits[k + 1])

        last_digit = digits[n - 1]
        best = prefix[n]  # no operation

        for k in range(n):
            # where the pointer rests before the reversed suffix starts
            if k == 0:
                pos_before = 0
            else:
                pos_before = digits[k - 1]

            # NOTE !!! the reversed suffix is dialed starting from s[n-1]
            total = prefix[k] + self.dist(pos_before, last_digit) + inner[k]
            best = min(best, total)

        return best

    def dist(self, a, b):
        """Fewest rotations between digits a and b on the circular dial."""
        direct = abs(a - b)
        return min(direct, DIAL_SIZE - direct)
