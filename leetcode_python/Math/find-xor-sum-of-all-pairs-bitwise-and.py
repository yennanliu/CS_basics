"""

1835. Find XOR Sum of All Pairs Bitwise AND
Hard

The XOR sum of a list is the bitwise XOR of all its elements. If the list only contains one element, then its XOR sum will be equal to this element.

For example, the XOR sum of [1,2,3,4] is equal to 1 XOR 2 XOR 3 XOR 4 = 4, and the XOR sum of [3] is equal to 3.

You are given two 0-indexed arrays arr1 and arr2 that consist only of non-negative integers.

Consider the list containing the result of arr1[i] AND arr2[j] (bitwise AND) for every (i, j) pair where 0 <= i < arr1.length and 0 <= j < arr2.length.

Return the XOR sum of the aforementioned list.


Example 1:

Input: arr1 = [1,2,3], arr2 = [6,5]
Output: 0
Explanation: The list = [1 AND 6, 1 AND 5, 2 AND 6, 2 AND 5, 3 AND 6, 3 AND 5] = [0,1,2,0,2,1].
The XOR sum = 0 XOR 1 XOR 2 XOR 0 XOR 2 XOR 1 = 0.

Example 2:

Input: arr1 = [12], arr2 = [4]
Output: 4
Explanation: The list = [12 AND 4] = [4]. The XOR sum = 4.


Constraints:

1 <= arr1.length, arr2.length <= 10^5
0 <= arr1[i], arr2[j] <= 10^9

"""

# V0
# IDEA : AND DISTRIBUTES OVER XOR  ->  (XOR arr1) AND (XOR arr2)
#
#   look at one bit position b. it is set in arr1[i] & arr2[j] iff it is set in
#   both. so the number of pairs contributing bit b is c1 * c2, where c1/c2 are
#   how many elements of each array have bit b set. xor keeps the bit iff that
#   product is ODD, i.e. iff c1 is odd AND c2 is odd.
#
#   "c1 odd" is exactly "bit b of (arr1[0] ^ arr1[1] ^ ...) is 1", so
#     answer = (xor of arr1) & (xor of arr2)
#
#   NOTE : the naive double loop is 10^10 operations; the distributive law
#          (a & b) ^ (a & c) == a & (b ^ c) is the whole problem.
#
# time = O(n + m), space = O(1)
class Solution(object):
    def getXORSum(self, arr1, arr2):
        a = 0
        for v in arr1:
            a ^= v
        b = 0
        for v in arr2:
            b ^= v
        return a & b
