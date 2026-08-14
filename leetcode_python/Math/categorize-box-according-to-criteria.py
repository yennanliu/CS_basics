"""

2525. Categorize Box According to Criteria
Easy

Given four integers length, width, height, and mass, representing the dimensions
and mass of a box, respectively, return a string representing the category of the box.

The box is "Bulky" if:
    Any of the dimensions of the box is greater or equal to 10^4.
    Or, the volume of the box is greater or equal to 10^9.
If the mass of the box is greater or equal to 100, it is "Heavy".
If the box is both "Bulky" and "Heavy", then its category is "Both".
If the box is neither "Bulky" nor "Heavy", then its category is "Neither".
If the box is "Bulky" but not "Heavy", then its category is "Bulky".
If the box is "Heavy" but not "Bulky", then its category is "Heavy".

Note that the volume of the box is the product of its length, width and height.


Example 1:

Input: length = 1000, width = 35, height = 700, mass = 300
Output: "Heavy"
Explanation:
None of the dimensions of the box is greater or equal to 10^4.
Its volume = 24500000 <= 10^9. So it cannot be categorized as "Bulky".
However mass >= 100, so the box is "Heavy".
Since the box is not "Bulky" but "Heavy", we return "Heavy".

Example 2:

Input: length = 200, width = 50, height = 800, mass = 50
Output: "Neither"
Explanation:
None of the dimensions of the box is greater or equal to 10^4.
Its volume = 8 * 10^6 <= 10^9. So it cannot be categorized as "Bulky".
Its mass is also less than 100, so it cannot be categorized as "Heavy" either.
Since its neither of the two above categories, we return "Neither".


Constraints:

1 <= length, width, height <= 10^5
1 <= mass <= 10^3

"""

# V0
# IDEA : SIMULATION -> 2 INDEPENDENT FLAGS -> LOOKUP TABLE
#
#   the category is a pure function of two booleans (bulky, heavy), so compute
#   each flag once and index a 4-entry table with (heavy << 1 | bulky). This
#   keeps the 4 outcomes in one place instead of a chain of if/elif.
#
#     index 0 -> Neither, 1 -> Bulky, 2 -> Heavy, 3 -> Both
#
#   NOTE : the thresholds are INCLUSIVE ( >= 10^4, >= 10^9, >= 100 ), an easy
#          off-by-one to get wrong.
#
#   NOTE : the volume can reach 10^15, which overflows 32-bit ints in other
#          languages; Python ints are arbitrary precision so no cast needed.
#
# time = O(1), space = O(1)
class Solution(object):
    def categorizeBox(self, length, width, height, mass):
        volume = length * width * height
        bulky = 1 if (length >= 10000 or width >= 10000 or height >= 10000
                      or volume >= 10 ** 9) else 0
        heavy = 1 if mass >= 100 else 0
        table = ["Neither", "Bulky", "Heavy", "Both"]
        return table[(heavy << 1) | bulky]
