"""

1616. Split Two Strings to Make Palindrome
Medium

You are given two strings a and b of the same length. Choose an index and split both strings at the same index, splitting a into two strings: aprefix and asuffix where a = aprefix + asuffix, and splitting b into two strings: bprefix and bsuffix where b = bprefix + bsuffix. Check if aprefix + bsuffix or bprefix + asuffix forms a palindrome.

When you split a string s into sprefix and ssuffix, either ssuffix or sprefix is allowed to be empty. For example, if s = "abc", then "" + "abc", "a" + "bc", "ab" + "c" , and "abc" + "" are valid splits.

Return true if it is possible to form a palindrome string, otherwise return false.

Notice that x + y denotes the concatenation of strings x and y.


Example 1:

Input: a = "x", b = "y"
Output: true
Explaination: If either a or b are palindromes the answer is true since you can split in the following way:
aprefix = "", asuffix = "x"
bprefix = "", bsuffix = "y"
Then, aprefix + bsuffix = "" + "y" = "y", which is a palindrome.

Example 2:

Input: a = "xbdef", b = "xecab"
Output: false

Example 3:

Input: a = "ulacfd", b = "jizalu"
Output: true
Explaination: Split them at index 3:
aprefix = "ula", asuffix = "cfd"
bprefix = "jiz", bsuffix = "alu"
Then, aprefix + bsuffix = "ula" + "alu" = "ulaalu", which is a palindrome.

Constraints:

1 <= a.length, b.length <= 10^5
a.length == b.length
a and b consist of lowercase English letters

"""

# V0
# IDEA : GREEDY TWO POINTERS (match the outside as far as possible, then
#        the leftover middle must be a palindrome of ONE of the strings)
#
#   for a_prefix + b_suffix, walk i from the left of a and j from the
#   right of b while a[i] == b[j]. Those matched pairs are forced -- the
#   split point can be anywhere, but wherever it is, the characters
#   outside min(i, ...) must pair up like this.
#
#   once a[i] != b[j], the untouched middle a[i..j] has to come entirely
#   from one string (the split is either left of i or right of j), so the
#   answer is True iff a[i..j] or b[i..j] is itself a palindrome.
#
#   NOTE : run the same check twice, swapping the roles of a and b, for
#          the b_prefix + a_suffix case.
#
# time = O(n), space = O(n) for the slice comparisons
class Solution(object):
    def checkPalindromeFormation(self, a, b):
        def is_pal(s, i, j):
            while i < j:
                if s[i] != s[j]:
                    return False
                i += 1
                j -= 1
            return True

        def check(x, y):
            # x_prefix + y_suffix
            i, j = 0, len(y) - 1
            while i < j and x[i] == y[j]:
                i += 1
                j -= 1
            return i >= j or is_pal(x, i, j) or is_pal(y, i, j)

        return check(a, b) or check(b, a)
