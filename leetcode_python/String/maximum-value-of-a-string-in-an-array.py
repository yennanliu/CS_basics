"""

2496. Maximum Value of a String in an Array
Easy

The value of an alphanumeric string can be defined as:

The numeric representation of the string in base 10, if it comprises of digits only.
The length of the string, otherwise.

Given an array strs of alphanumeric strings, return the maximum value of any string in strs.


Example 1:

Input: strs = ["alic3","bob","3","4","00000"]
Output: 5
Explanation:
- "alic3" consists of both letters and digits, so its value is its length, i.e. 5.
- "bob" consists only of letters, so its value is also its length, i.e. 3.
- "3" consists only of digits, so its value is its numeric equivalent, i.e. 3.
- "4" also consists only of digits, so its value is 4.
- "00000" consists only of digits, so its value is 0.
Hence, the maximum value is 5, of "alic3".

Example 2:

Input: strs = ["1","01","001","0001"]
Output: 1
Explanation:
Each string in the array has value 1. Hence, we return 1.


Constraints:

1 <= strs.length <= 100
1 <= strs[i].length <= 9
strs[i] consists of only lowercase English letters and digits.

"""

# V0
# IDEA : STRING SCAN + MAX
#
#   per string : if every char is a digit -> int(s), else len(s).
#
#   NOTE : "00000" must map to 0 (int() strips leading zeros), NOT to its
#          length 5 -- so the all-digit check has to come first and win.
#   NOTE : str.isdigit() on the whole string is the concise all-digit test,
#          and it is False for the empty string (which cannot occur here).
#
# time = O(n * L), space = O(1)
class Solution(object):
    def maximumValue(self, strs):
        res = 0
        for s in strs:
            val = int(s) if s.isdigit() else len(s)
            res = max(res, val)
        return res
