#---------------------------------------------------------------
# DYNAMIC PROGRAMMING (an introduction)
#---------------------------------------------------------------
#
# Scope: what DP is, shown on four small problems, each written the
#        slow way, top-down and bottom-up. See doc/cheatsheet/dp.md
#        for the full pattern catalogue and kadane.py for the
#        smallest real-world example.
#
# DP = recursion + remembering. It applies when a problem has
#
#   1) OVERLAPPING SUBPROBLEMS   the same smaller question is asked
#                                again and again
#   2) OPTIMAL SUBSTRUCTURE      the answer is built from the answers
#                                to those smaller questions
#
#                      fib(5)
#                    /        \
#               fib(4)         fib(3)          fib(3) is computed twice,
#              /      \        /     \         fib(2) three times,
#          fib(3)    fib(2)  fib(2)  fib(1)    fib(1) five times ...
#          /    \
#      fib(2)  fib(1)                          plain recursion: O(2^N)
#
# Compute each distinct subproblem ONCE and the tree collapses to a
# line: N subproblems, O(1) work each -> O(N).
#
# TWO WAYS TO WRITE IT
#   Top-down  (memoisation)  keep the recursion, add a cache: before
#                            computing, look the answer up; after
#                            computing, store it
#   Bottom-up (tabulation)   no recursion: fill a table starting from
#                            the base cases, in an order where every
#                            value needed is already there
#
# THE RECIPE (works for every DP problem)
#   1) STATE       what does dp[i] mean? Say it in one sentence.
#   2) TRANSITION  how is dp[i] built from smaller states?
#   3) BASE CASE   which states are known without any computation?
#   4) ORDER       in what order must the table be filled?
#   5) ANSWER      which cell holds the final result?
#
# Used by: LC 509 Fibonacci, LC 70 Climbing Stairs, LC 322 Coin
#          Change, LC 1143 Longest Common Subsequence -- the four below.


INF = float("inf")


#---------------------------------------------------------------
# 1) Fibonacci  (LC 509) -- three versions of the same function
#---------------------------------------------------------------

# V0 : plain recursion -- correct, and exponentially slow
def fib_recursive(n):
    """O(2^N) time: the tree in the header, with every repeat recomputed."""
    if n < 2:
        return n
    return fib_recursive(n - 1) + fib_recursive(n - 2)


# V1 : top-down -- the same recursion plus a cache
def fib_memo(n, cache=None):
    """O(N) time, O(N) space (cache + call stack).

    Recurses n levels deep, so n is limited by Python's recursion limit
    (about 1000); the bottom-up versions below have no such limit.
    """
    if cache is None:
        cache = {}
    if n < 2:
        return n
    if n in cache:                   # asked before: answer instantly
        return cache[n]
    cache[n] = fib_memo(n - 1, cache) + fib_memo(n - 2, cache)
    return cache[n]


# V2 : bottom-up -- fill the table from the base cases upwards
def fib_table(n):
    """O(N) time, O(N) space, no recursion."""
    if n < 2:
        return n
    dp = [0] * (n + 1)               # state: dp[i] = the i-th Fibonacci number
    dp[1] = 1                        # base cases: dp[0] = 0, dp[1] = 1
    for i in range(2, n + 1):        # order: left to right
        dp[i] = dp[i - 1] + dp[i - 2]        # transition
    return dp[n]                     # answer


# V2-1 : bottom-up, O(1) space. dp[i] reads only the two cells before
# it, so two variables can replace the whole table.
def fib(n):
    """O(N) time, O(1) space -- the version to write."""
    previous = 0
    current = 1
    for _ in range(n):
        previous, current = current, previous + current
    return previous


#---------------------------------------------------------------
# 2) Climbing Stairs  (LC 70) -- recognising Fibonacci in disguise
#---------------------------------------------------------------
#
# Climb n steps taking 1 or 2 at a time. How many different ways?
#
#   STATE       ways[i] = number of ways to stand on step i
#   TRANSITION  the last move was a 1-step (from i - 1) or a 2-step
#               (from i - 2):  ways[i] = ways[i-1] + ways[i-2]
#   BASE CASE   ways[0] = 1 (one way to be at the bottom: do nothing),
#               ways[1] = 1
def climb_stairs(n):
    ways = [0] * (n + 1)
    ways[0] = 1
    if n >= 1:
        ways[1] = 1
    for i in range(2, n + 1):
        ways[i] = ways[i - 1] + ways[i - 2]
    return ways[n]


#---------------------------------------------------------------
# 3) Coin Change  (LC 322) -- a choice at every step, minimised
#---------------------------------------------------------------
#
# Fewest coins that add up to `amount`, with unlimited coins of each
# kind. Returns -1 when the amount cannot be made.
#
#   STATE       dp[a] = fewest coins that make the amount a
#   TRANSITION  the LAST coin used is some coin c; before it the
#               amount was a - c:   dp[a] = 1 + min(dp[a - c]) over all c
#   BASE CASE   dp[0] = 0 (zero coins make zero)
#
#     coins = [1, 2, 5]
#     amount   0  1  2  3  4  5  6  7  8  9  10  11
#     dp       0  1  1  2  2  1  2  2  3  3   2   3      11 = 5 + 5 + 1
#
# Greedy ("always take the biggest coin") is NOT correct in general:
# with coins [1, 3, 4] and amount 6 it gives 4 + 1 + 1 (three coins),
# while 3 + 3 uses two. DP tries every last coin, so it cannot miss.
def coin_change(coins, amount):
    dp = [INF] * (amount + 1)        # INF = "cannot be made (yet)"
    dp[0] = 0
    for a in range(1, amount + 1):
        for coin in coins:
            if coin <= a and dp[a - coin] + 1 < dp[a]:
                dp[a] = dp[a - coin] + 1
    if dp[amount] == INF:
        return -1
    return dp[amount]


# the same problem top-down, to show the two styles side by side.
#
# LIMIT: the recursion goes as deep as the longest chain of
# subproblems -- with a coin of 1, that is `amount` nested calls. Past
# Python's recursion limit (about 1000) this raises RecursionError,
# however good the cache is. That is the practical reason to prefer the
# bottom-up form: coin_change above handles any amount.
def coin_change_memo(coins, amount):
    cache = {}

    def fewest(a):
        """Fewest coins for amount a, or INF if impossible."""
        if a == 0:
            return 0
        if a in cache:
            return cache[a]
        best = INF
        for coin in coins:
            if coin <= a:
                best = min(best, 1 + fewest(a - coin))
        cache[a] = best
        return best

    result = fewest(amount)
    if result == INF:
        return -1
    return result


#---------------------------------------------------------------
# 4) Longest Common Subsequence  (LC 1143) -- a two-dimensional table
#---------------------------------------------------------------
#
# The longest sequence of characters that appears in BOTH strings, in
# order but not necessarily next to each other.
#
#   STATE       dp[i][j] = LCS length of the first i characters of a
#               and the first j characters of b
#   TRANSITION  last characters match  -> 1 + dp[i-1][j-1]   (use it)
#               they differ            -> max(dp[i-1][j],    (drop a's)
#                                             dp[i][j-1])    (drop b's)
#   BASE CASE   row 0 and column 0 are 0: an empty string has nothing
#               in common with anything
#
#           ""  a  c  e
#       ""   0  0  0  0
#       a    0  1  1  1
#       b    0  1  1  1
#       c    0  1  2  2
#       d    0  1  2  2
#       e    0  1  2  3     -> "ace", length 3
def longest_common_subsequence(a, b):
    rows = len(a) + 1
    cols = len(b) + 1
    dp = [[0] * cols for _ in range(rows)]
    for i in range(1, rows):
        for j in range(1, cols):
            if a[i - 1] == b[j - 1]:             # dp is 1-based, the strings 0-based
                dp[i][j] = dp[i - 1][j - 1] + 1
            else:
                dp[i][j] = max(dp[i - 1][j], dp[i][j - 1])
    return dp[len(a)][len(b)]


def count_calls(n, counter):
    """fib_recursive, counting how many calls it makes."""
    counter[0] += 1
    if n < 2:
        return n
    return count_calls(n - 1, counter) + count_calls(n - 2, counter)


if __name__ == "__main__":
    expected = [0, 1, 1, 2, 3, 5, 8, 13, 21, 34, 55]
    for function in (fib_recursive, fib_memo, fib_table, fib):
        assert [function(n) for n in range(11)] == expected

    # the cost of NOT remembering: fib(20) by plain recursion makes 21891
    # calls for 21 distinct subproblems
    calls = [0]
    count_calls(20, calls)
    assert calls[0] == 21891

    # the O(N) versions go where the recursion never could
    assert fib(90) == 2880067194370816120
    assert fib_table(90) == fib_memo(90) == fib(90)

    # climbing stairs IS Fibonacci, shifted by one
    assert [climb_stairs(n) for n in range(1, 7)] == [1, 2, 3, 5, 8, 13]
    assert climb_stairs(0) == 1
    assert all(climb_stairs(n) == fib(n + 1) for n in range(30))

    # coin change: the table from the header, and LC 322's examples
    assert [coin_change([1, 2, 5], a) for a in range(12)] == [0, 1, 1, 2, 2, 1, 2, 2, 3, 3, 2, 3]
    assert coin_change([2], 3) == -1                     # cannot be made
    assert coin_change([1], 0) == 0
    assert coin_change([1, 3, 4], 6) == 2                # 3 + 3, where greedy says 4 + 1 + 1
    for coins in ([1, 2, 5], [2], [1, 3, 4], [5, 7]):
        for amount in range(30):
            assert coin_change(coins, amount) == coin_change_memo(coins, amount)

    # top-down is limited by recursion depth; bottom-up is not
    assert coin_change([1], 10000) == 10000
    try:
        coin_change_memo([1], 10000)
        raise AssertionError("expected RecursionError")
    except RecursionError:
        pass

    # LCS: the table from the header, and LC 1143's examples
    assert longest_common_subsequence("abcde", "ace") == 3
    assert longest_common_subsequence("abc", "abc") == 3
    assert longest_common_subsequence("abc", "def") == 0
    assert longest_common_subsequence("", "abc") == 0

    print("Success.")
