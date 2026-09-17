"""

1888. Minimum Number of Flips to Make The Binary String Alternating
Medium

You are given a binary string s. You are allowed to perform two types of operations
on the string in any sequence:

Type-1: Remove the character at the start of the string s and append it to the end
of the string.
Type-2: Pick any character in s and flip its value, i.e., if its value is '0' it
becomes '1' and vice-versa.

Return the minimum number of type-2 operations you need to perform such that s
becomes alternating.

The string is called alternating if no two adjacent characters are equal.

For example, the strings "010" and "1010" are alternating, while the string "0100"
is not.


Example 1:

Input: s = "111000"
Output: 2
Explanation: Use the first operation two times to make s = "100011".
Then, use the second operation on the third and sixth elements to make s = "101010".

Example 2:

Input: s = "010"
Output: 0
Explanation: The string is already alternating.

Example 3:

Input: s = "1110"
Output: 1
Explanation: Use the second operation on the second element to make s = "1010".


Constraints:

1 <= s.length <= 10^5
s[i] is either '0' or '1'.

"""

# V0
# IDEA : DOUBLE THE STRING + SLIDING WINDOW OF SIZE n
#
#   a type-1 op is a rotation, and every rotation of s is a length-n window of
#   s + s. so the question is : over all n windows, and the two alternating
#   targets, what is the fewest mismatches.
#
#   the cost is kept incrementally against the two ABSOLUTE-index patterns
#
#     A : index even -> '0', odd -> '1'      ("0101...")
#     B : the complement of A                ("1010...")
#
#   a character either mismatches A or mismatches B -- never both, never
#   neither -- so one comparison updates both counters.
#
#   e.g. "111000" -> best window is "100011" rotated, costing 2.
#
#   NOTE !!! using absolute parity is safe even though a window may start at an
#            odd index : starting odd just swaps which of A/B it is being scored
#            against, and we take min(costA, costB) anyway.
#
# time = O(n), space = O(1)
class Solution(object):
    def minFlips(self, s):
        """
        :type s: str
        :rtype: int
        """
        # edge
        if not s:
            return 0

        n = len(s)
        t = s + s

        cost_a = 0   # mismatches vs "0101..." (by absolute index)
        cost_b = 0   # mismatches vs "1010..."
        res = n

        for i in range(len(t)):
            want_a = '0' if i % 2 == 0 else '1'
            if t[i] != want_a:
                cost_a += 1
            else:
                cost_b += 1

            # window longer than n -> drop the character falling off the left
            if i >= n:
                j = i - n
                want_j = '0' if j % 2 == 0 else '1'
                if t[j] != want_j:
                    cost_a -= 1
                else:
                    cost_b -= 1

            if i >= n - 1:
                res = min(res, cost_a, cost_b)

        return res
