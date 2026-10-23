"""

1552. Magnetic Force Between Two Balls
Medium

In the universe Earth C-137, Rick discovered a special form of magnetic force
between two balls if they are put in his new invented basket. Rick has n empty
baskets, the ith basket is at position[i], Morty has m balls and needs to
distribute the balls into the baskets such that the minimum magnetic force
between any two balls is maximum.

Rick stated that magnetic force between two different balls at positions x and
y is |x - y|.

Given the integer array position and the integer m. Return the required force.

Example 1:

Input: position = [1,2,3,4,7], m = 3

Output: 3

Explanation:

Distributing the 3 balls into baskets 1, 4 and 7 will make the magnetic force
between ball pairs [3, 3, 6]. The minimum magnetic force is 3. We cannot
achieve a larger minimum magnetic force than 3.

Example 2:

Input: position = [5,4,3,2,1,1000000000], m = 2

Output: 999999999

Explanation:

We can use baskets 1 and 1000000000.

Constraints:

n == position.length
2 <= n <= 10^5
1 <= position[i] <= 10^9
All integers in position are distinct.
2 <= m <= position.length

"""

# V0
class Solution(object):
    def maxDistance(self, position, m):
        """
        :type position: List[int]
        :type m: int
        :rtype: int
        """
        pass


# V0-1
# IDEA : SORT + BINARY SEARCH ON ANSWER + GREEDY CHECK (GEMINI)
class Solution(object):

  def maxDistance(self, position, m):
    """:type position: List[int]

    :type m: int
    :rtype: int
    """
    if not position or len(position) < m:
      return 0

    # 1. 必須先將籃子位置排序，才能進行`貪婪`放置
    position.sort()
    n = len(position)

    # 2. 二分搜尋的範圍：最小距離為 1，最大距離為`頭尾位置之差` (NOTE !!!!)
    l = 1
    r = position[-1] - position[0]
    res = 0

    while l <= r:
      mid = l + (r - l) // 2

      # 3. 檢查以 mid 作為最小距離時，是否能放得下至少 m 個球
      if self.can_place(position, m, mid):
        res = mid  # 記錄當前可行的最大化距離
        l = mid + 1  # 嘗試挑戰更大的距離
      else:
        r = mid - 1  # 距離太大導致放不下，縮小範圍

    return res

  def can_place(self, position, m, min_dist):
    # 貪婪地檢查在最小距離為 min_dist 的限制下，能放幾顆球
    count = 1  # 第一顆球一定放在第一個位置
    last_pos = position[0]

    for i in range(1, len(position)):
      # 如果當前籃子與上一顆球的位置大於等於指定距離，就可以放球
      if position[i] - last_pos >= min_dist:
        
        count += 1
        # NOTE !!! below
        last_pos = position[i]
        
        # early quit
        if count >= m:
          return True

    return count >= m


# V0-2
# IDEA : SORT + BINARY SEARCH ON ANSWER + GREEDY CHECK (GPT)
class Solution(object):
    def maxDistance(self, position, m):
        """
        :type position: List[int]
        :type m: int
        :rtype: int
        """
        # Sort basket positions from left to right
        position.sort()

        # Binary search the minimum distance between any two balls.
        # The answer ranges from 1 to the entire position span.
        left = 1
        right = position[-1] - position[0]

        res = 0

        while left <= right:
            mid = left + (right - left) // 2

            # Can we place m balls with at least mid distance
            # between every pair of adjacent placed balls?
            if self.get_min_force(position, m, mid):
                # mid is feasible. Try to maximize the distance.
                res = mid
                left = mid + 1
            else:
                # mid is too large. Try a smaller distance.
                right = mid - 1

        return res

    def get_min_force(self, position, m, x):
        """
        Return True if we can place m balls such that
        the minimum distance between any two balls is >= x.
        """
        count = 1
        last_position = position[0]

        # Greedily place each next ball in the earliest
        # available basket at least x units away.
        for i in range(1, len(position)):
            if position[i] - last_position >= x:
                count += 1
                last_position = position[i]

                if count >= m:
                    return True

        return False



# V0
# IDEA : SORT + BINARY SEARCH ON ANSWER + GREEDY CHECK (LC 410, LC 1231)(CLAUDE)
#
#   "max of the min gap" -> binary search the GAP, not an index.
#   The check is monotonic: if m balls fit with every gap >= d, they also fit
#   with every gap >= d - 1. So the answers form  [yes, yes, ..., yes, no, no],
#   and we want the LAST yes.
#
#   Greedy check for a given gap d: put the first ball in the leftmost basket,
#   then each next ball in the first basket >= last + d. Placing as early as
#   possible never hurts, because it leaves the most room for the balls after.
#
#   e.g. position = [1,2,3,4,7], m = 3
#        d = 3 -> balls at 1, 4, 7 -> 3 balls -> ok
#        d = 4 -> balls at 1, 7    -> 2 balls -> not ok   => answer 3
#
#   (brute force: try every d from 1 to max - min -> O(n * range), TLE)
#
# time = O(n log n + n log(max - min)), space = O(n)
class Solution(object):
    def maxDistance(self, position, m):
        """
        :type position: List[int]
        :type m: int
        :rtype: int
        """
        # NOTE !!! sort first: the greedy check walks baskets left to right.
        #          sorted() makes a copy, so the input is not mutated
        baskets = sorted(position)

        # smallest possible min gap is 1 (positions are distinct);
        # the largest is reached with only 2 balls at both ends
        left = 1
        right = baskets[-1] - baskets[0]
        best = 0

        while left <= right:
            mid = left + (right - left) // 2

            if self.can_place(baskets, m, mid):
                # gap mid works -> record it, and try a larger one
                best = mid
                left = mid + 1
            else:
                # gap mid is too large -> shrink it
                right = mid - 1

        return best

    def can_place(self, baskets, m, min_gap):
        """Return True if m balls fit in sorted baskets with every gap >= min_gap."""
        # first ball always goes in the leftmost basket
        placed = 1
        last_pos = baskets[0]

        for pos in baskets[1:]:
            # place the next ball at the earliest basket far enough away
            if pos - last_pos >= min_gap:
                placed += 1
                last_pos = pos
                # stop early: enough balls already placed
                if placed >= m:
                    return True

        return placed >= m
