"""

1073. Adding Two Negabinary Numbers
Medium

Given two numbers arr1 and arr2 in base -2, return the result of adding them together.

Each number is given in array format: as an array of 0s and 1s, from most
significant bit to least significant bit. For example, arr = [1,1,0,1] represents
the number (-2)^3 + (-2)^2 + (-2)^0 = -3. A number arr in array format is also
guaranteed to have no leading zeros: either arr == [0] or arr[0] == 1.

Return the result of adding arr1 and arr2 in the same format: as an array of 0s
and 1s with no leading zeros.


Example 1:

Input: arr1 = [1,1,1,1,1], arr2 = [1,0,1]
Output: [1,0,0,0,0]
Explanation: arr1 represents 11, arr2 represents 5, the output represents 16.

Example 2:

Input: arr1 = [0], arr2 = [0]
Output: [0]

Example 3:

Input: arr1 = [0], arr2 = [1]
Output: [1]


Constraints:

1 <= arr1.length, arr2.length <= 1000
arr1[i] and arr2[i] are 0 or 1
arr1 and arr2 have no leading zeros

"""

# V0
# IDEA : SCHOOL BOOK ADDITION with a base -2 carry
#
#  add column by column from the least significant bit, x = a + b + carry.
#  since the next place is worth (-2) times this one :
#    x == 2  -> write 0, carry -1     (2 * v = (-1) * (-2 * v))
#    x == 3  -> write 1, carry -1
#    x == -1 -> write 1, carry  1     (-1 * v = 1 * v + 1 * (-2 * v))
#  so the carry is in {-1, 0, 1} and x stays in [-1, 3]
#  finally strip leading zeros and reverse
# time = O(n), n = max(len(arr1), len(arr2))
# space = O(n)
class Solution(object):
    def addNegabinary(self, arr1, arr2):
        i, j = len(arr1) - 1, len(arr2) - 1
        carry = 0
        res = []        # least significant bit first

        while i >= 0 or j >= 0 or carry:
            a = arr1[i] if i >= 0 else 0
            b = arr2[j] if j >= 0 else 0
            x = a + b + carry
            carry = 0
            if x >= 2:
                x -= 2
                carry = -1
            elif x == -1:
                x = 1
                carry = 1
            res.append(x)
            i -= 1
            j -= 1

        # NOTE !!! drop leading zeros, but keep at least one digit
        while len(res) > 1 and res[-1] == 0:
            res.pop()

        return res[::-1]
