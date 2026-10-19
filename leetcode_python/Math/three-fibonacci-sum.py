"""

4078. Three Fibonacci Sum
Easy

You are given an integer n.

The Fibonacci sequence starts with 0 and 1, and each subsequent number is the
sum of the previous two numbers. Its first few terms are
0, 1, 1, 2, 3, 5, 8, 13, ....

Return true if n can be expressed as the sum of three consecutive terms in the
Fibonacci sequence, and false otherwise.


Example 1:

Input: n = 16

Output: true

Explanation:

The three consecutive terms 3, 5, and 8 have a sum of 3 + 5 + 8 = 16.

Example 2:

Input: n = 8

Output: false

Explanation:

Although 1 + 2 + 5 = 8, these terms are not consecutive in the Fibonacci
sequence. No three consecutive terms have a sum of 8.

Example 3:

Input: n = 2

Output: true

Explanation:

The first three terms have a sum of 0 + 1 + 1 = 2.


Constraints:

1 <= n <= 10^9

"""

# V0
# IDEA: 1D DP + check (gemini)
class Solution(object):

  def threeFibonacciSum(self, n):
    """:type n: int

    :rtype: bool
    """
    # 邊界條件：負數不可能達成
    if n < 0:
      return False

    # 0 可以由 0 + 0 + 0 達成
    if n == 0:
      return True

    # 建立費波那契數列表（大小 100 足以涵蓋大於 10^9 的數值）
    dp = [0] * 101
    dp[0] = 0
    dp[1] = 1
    dp[2] = 1

    for i in range(2, 101):
      if i >= 3:
        dp[i] = dp[i - 1] + dp[i - 2]

      # 三個連續費波那契數相加：dp[i-2] + dp[i-1] + dp[i]
      tmp = dp[i - 2] + dp[i - 1] + dp[i]

      if tmp == n:
        return True

      # 因為費波那契數是嚴格遞增的，一旦總和超過 n 就可以提早結束
      if tmp > n:
        return False

    return False


# V0-0-1
# IDEA: 1D DP + check (GPT)
class Solution(object):
    def threeFibonacciSum(self, n):
        """
        :type n: int
        :rtype: bool
        """
        if n < 0:
            return False

        # First three Fibonacci numbers: 0, 1, 1
        a, b, c = 0, 1, 1

        while True:
            total = a + b + c

            if total == n:
                return True

            if total > n:
                return False

            # Move to the next consecutive Fibonacci triple
            a, b, c = b, c, b + c


# V0-1
# IDEA: 1D DP + check
class Solution(object):
    def threeFibonacciSum(self, n):
        """
        :type n: int
        :rtype: bool
        """
        if n < 2:
            return False

        fib_seq = [0, 1, 1]

        while True:
            total = sum(fib_seq[-3:])

            if total == n:
                return True
            if total > n:
                return False

            fib_seq.append(fib_seq[-1] + fib_seq[-2])

# V0-2
# IDEA: MATH + DP (gemini)
"""

數學性質 + 雜湊表預先計算（O(1) 查詢）


1.

費波那契數列中，
任三個連續的數字相加具有一個非常強大的數學性質：


    -> ``` F(i+1) = F(i+2) = 2 * F(i+2) ```

    because

    ->



        F(i) + F(i+1) = F(i+2)

        -> so,
            F(i) + F(i+1) + F(i+2) =  F(i+2) + F(i+2) = 2 * F(i+2)


    
    
-> so, ALL we need to check are:

    1. `任何三個連續費波那契數`的`和`必定是`偶數`。

    2. 將該和除以 2 之後（即 n / 2），必須剛好是一個費波那契數。


"""
class Solution(object):

  def threeFibonacciSum(self, n):
    """:type n: int

    :rtype: bool
    """
    # 預先生成到 10^9 為止的費波那契數集合
    _fib_set = set()
    _a, _b = 0, 1
    while _b <= 10**9:
        _a, _b = _b, _a + _b
        _fib_set.add(_b)

    # 1. 三個連續費波那契數的和必定是偶數
    if n % 2 != 0:
      return False

    # 2. 檢查 n / 2 是否存在於費波那契數集合中
    return (n // 2) in _fib_set



# V0
# IDEA : GENERATE FIBONACCI TERMS + SLIDE A WINDOW OF 3 CONSECUTIVE TERMS
#
#   the terms grow exponentially, so only ~45 of them stay <= 10^9 : just
#   build them all, then check every window of 3 consecutive terms.
#
#   the window sums are strictly increasing from the first window on
#   (0+1+1=2, 1+1+2=4, 1+2+3=6, ...), so we can stop as soon as a sum
#   passes n.
#
#   e.g. n = 16 -> fib = [0,1,1,2,3,5,8,13,...] -> 3 + 5 + 8 = 16 -> True
#        n = 8  -> window sums 2, 4, 6, 10 -> jumps over 8 -> False
#
# time = O(log n), space = O(log n)
class Solution(object):
    def threeFibonacciSum(self, n):
        """
        :type n: int
        :rtype: bool
        """
        # build every Fibonacci term up to n (a window containing a term > n
        # already sums to more than n, so later terms are never needed)
        fib = [0, 1]
        while fib[-1] <= n:
            fib.append(fib[-1] + fib[-2])

        # try every window of 3 consecutive terms
        for i in range(len(fib) - 2):
            window_sum = fib[i] + fib[i + 1] + fib[i + 2]
            if window_sum == n:
                return True
            # NOTE !!! window sums only grow, so once past n it is hopeless
            if window_sum > n:
                return False

        return False


# V1
# IDEA : MATH — F(i) + F(i+1) + F(i+2) == 2 * F(i+2)
#
#   since F(i) + F(i+1) = F(i+2), three consecutive terms sum to
#   F(i+2) + F(i+2) = 2 * F(i+2). so n is such a sum exactly when n is even
#   and n / 2 is a Fibonacci term F(k) with k >= 2 (the first window is
#   0 + 1 + 1 = 2 * F(2)).
#
#   e.g. n = 16 -> 16 / 2 = 8 = F(6) -> True ;  n = 8 -> 8 / 2 = 4 -> not fib -> False
#
#   NOTE : same bound as V0, the identity is the thing worth remembering
#
# time = O(log n), space = O(1)
class Solution2(object):
    def threeFibonacciSum(self, n):
        """
        :type n: int
        :rtype: bool
        """
        # 2 * F(k) is always even
        if n % 2 == 1:
            return False

        half = n // 2

        # walk F(2), F(3), ... = 1, 2, 3, 5, ... until it reaches half
        prev = 1   # F(1)
        cur = 1    # F(2)
        while cur < half:
            prev, cur = cur, prev + cur

        return cur == half
