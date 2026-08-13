"""

65. Valid Number
Hard

Given a string s, return whether s is a valid number.

For example, all the following are valid numbers:
"2", "0089", "-0.1", "+3.14", "4.", "-.9", "2e10", "-90E3", "3e+7", "+6e-1", "53.5e93", "-123.456e789",
while the following are not valid numbers:
"abc", "1a", "1e", "e3", "99e2.5", "--6", "-+3", "95a54e53".

Formally, a valid number is defined using one of the following definitions:

1. An integer number followed by an optional exponent.
2. A decimal number followed by an optional exponent.

An integer number is defined with an optional sign '-' or '+' followed by digits.

A decimal number is defined with an optional sign '-' or '+' followed by one of the following definitions:

1. Digits followed by a dot '.'.
2. Digits followed by a dot '.' followed by digits.
3. A dot '.' followed by digits.

An exponent is defined with an exponent notation 'e' or 'E' followed by an integer number.

The digits are defined as one or more digits.


Example 1:

Input: s = "0"
Output: true

Example 2:

Input: s = "e"
Output: false

Example 3:

Input: s = "."
Output: false


Constraints:

1 <= s.length <= 20
s consists of only English letters (both uppercase and lowercase), digits (0-9),
plus '+', minus '-', or dot '.'.

"""

# V0
# IDEA : SPLIT ON 'e'/'E' -> validate "mantissa" + "exponent" separately
#        mantissa must be an integer OR a decimal, exponent must be an integer
# time = O(n)
# space = O(n)
class Solution(object):
    def isNumber(self, s):
        def all_digits(t):
            # non-empty and every char is 0-9
            # NOTE: don't use str.isdigit(), it also accepts unicode digits
            return len(t) > 0 and all(c in "0123456789" for c in t)

        def is_integer(t):
            # [+|-] digits
            if t and t[0] in "+-":
                t = t[1:]
            return all_digits(t)

        def is_decimal(t):
            # [+|-] ( digits "." | digits "." digits | "." digits )
            if t and t[0] in "+-":
                t = t[1:]
            if t.count(".") != 1:
                return False
            left, right = t.split(".")
            # at least one side must carry digits (so "." alone is invalid)
            if not left and not right:
                return False
            if left and not all_digits(left):
                return False
            if right and not all_digits(right):
                return False
            return True

        lower = s.lower()
        if "e" in lower:
            # only the FIRST 'e' splits; a 2nd 'e' lands in `exp` and fails is_integer
            i = lower.index("e")
            mantissa, exp = s[:i], s[i + 1:]
            return (is_integer(mantissa) or is_decimal(mantissa)) and is_integer(exp)

        return is_integer(s) or is_decimal(s)
