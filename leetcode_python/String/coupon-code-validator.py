"""

3606. Coupon Code Validator
Easy

You are given three arrays of length n that describe the properties of n
coupons: code, businessLine, and isActive. The ith coupon has:

code[i]: a string representing the coupon identifier.
businessLine[i]: a string denoting the business category of the coupon.
isActive[i]: a boolean indicating whether the coupon is currently active.

A coupon is considered valid if all of the following conditions hold:

1. code[i] is non-empty and consists only of alphanumeric characters
   (a-z, A-Z, 0-9) and underscores (_).
2. businessLine[i] is one of the following four categories: "electronics",
   "grocery", "pharmacy", "restaurant".
3. isActive[i] is true.

Return an array of the codes of all valid coupons, sorted first by their
businessLine in the order: "electronics", "grocery", "pharmacy",
"restaurant", and then by code in lexicographical (ascending) order within
each category.


Example 1:

Input: code = ["SAVE20","","PHARMA5","SAVE@20"],
       businessLine = ["restaurant","grocery","pharmacy","restaurant"],
       isActive = [true,true,true,true]
Output: ["PHARMA5","SAVE20"]
Explanation:
First coupon is valid.
Second coupon has empty code (invalid).
Third coupon is valid.
Fourth coupon has special character @ (invalid).

Example 2:

Input: code = ["GROCERY15","ELECTRONICS_50","DISCOUNT10"],
       businessLine = ["grocery","electronics","invalid"],
       isActive = [false,true,true]
Output: ["ELECTRONICS_50"]
Explanation:
First coupon is inactive (invalid).
Second coupon is valid.
Third coupon has invalid business line (invalid).


Constraints:

n == code.length == businessLine.length == isActive.length
1 <= n <= 100
0 <= code[i].length, businessLine[i].length <= 100
code[i] and businessLine[i] consist of printable ASCII characters.
isActive[i] is either true or false.

"""

# V0
# IDEA : FILTER + SORT ON THE (CATEGORY, CODE) KEY
#
#   the three validity rules are independent of each other, so a single
#   pass that ands them together is enough. the only rule with any teeth
#   is the code check: "alphanumeric or underscore" must be tested per
#   character, and the empty string has to be rejected explicitly because
#   an all-pass over zero characters would otherwise accept it.
#
#   the ordering looks like it needs a hand-written rank for the four
#   categories, but the required order — electronics, grocery, pharmacy,
#   restaurant — is already the alphabetical order of those four words.
#   so plain string comparison on businessLine reproduces it, and the
#   tie-break on code is lexicographic too; one tuple key sorts both
#   levels at once.
#
#   note the per-character test is spelled out against explicit ascii
#   ranges rather than str.isalnum(), which would also accept unicode
#   letters and digits outside a-z A-Z 0-9.
#
# time = O(n * L + n * log(n)), space = O(n)
class Solution(object):
    def validateCoupons(self, code, businessLine, isActive):
        lines = ("electronics", "grocery", "pharmacy", "restaurant")

        def ok_code(s):
            if not s:
                return False
            for c in s:
                if not (("a" <= c <= "z") or ("A" <= c <= "Z")
                        or ("0" <= c <= "9") or c == "_"):
                    return False
            return True

        valid = []
        for i in range(len(code)):
            if isActive[i] and businessLine[i] in lines and ok_code(code[i]):
                valid.append((businessLine[i], code[i]))

        valid.sort()
        return [c for _, c in valid]
