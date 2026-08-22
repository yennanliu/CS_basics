"""

2433. Find The Original Array of Prefix Xor
Medium

You are given an integer array pref of size n. Find and return the array arr of size n that satisfies:

pref[i] = arr[0] ^ arr[1] ^ ... ^ arr[i].

Note that ^ denotes the bitwise-xor operation.

It can be proven that the answer is unique.


Example 1:

Input: pref = [5,2,0,3,1]
Output: [5,7,2,3,2]
Explanation: From the array [5,7,2,3,2] we have the following:
- pref[0] = 5.
- pref[1] = 5 ^ 7 = 2.
- pref[2] = 5 ^ 7 ^ 2 = 0.
- pref[3] = 5 ^ 7 ^ 2 ^ 3 = 3.
- pref[4] = 5 ^ 7 ^ 2 ^ 3 ^ 2 = 1.

Example 2:

Input: pref = [13]
Output: [13]
Explanation: We have pref[0] = arr[0] = 13.


Constraints:

1 <= pref.length <= 10^5
0 <= pref[i] <= 10^6

"""

# V0
# IDEA : XOR IS ITS OWN INVERSE — arr[i] = pref[i] ^ pref[i-1]
#
#   from  pref[i] = pref[i-1] ^ arr[i]  XOR both sides by pref[i-1] to get
#   arr[i] directly. the first element is pref[0] unchanged.
#
#   that also shows the answer is unique, as the statement promises.
#
# time = O(n), space = O(n) for the output
class Solution(object):
    def findArray(self, pref):
        return [pref[0]] + [pref[i] ^ pref[i - 1] for i in range(1, len(pref))]


# V0-1
# IDEA : IN-PLACE BACKWARD PASS — O(1) EXTRA SPACE
#
#   walking FORWARD in place would destroy pref[i-1] before it is needed, so
#   walk backward instead : at index i, both pref[i] and pref[i-1] are still
#   untouched, so pref[i] ^= pref[i-1] overwrites the slot with the answer
#   and never corrupts a value still to be read.
#
#   pref[0] already equals arr[0], so the loop stops at i == 1.
#
#   NOTE : this mutates the caller's list (LeetCode accepts that, and it is
#          the version to use when memory is tight).
#
# time = O(n), space = O(1) beyond the input
class Solution(object):
    def findArray(self, pref):
        for i in range(len(pref) - 1, 0, -1):
            pref[i] ^= pref[i - 1]
        return pref


# V0-2
# IDEA : BITWISE — EACH BIT COLUMN IS AN INDEPENDENT PARITY STREAM
#
#   xor acts on every bit position independently, so bit b of pref is just
#   the running PARITY of bit b of arr :
#
#       bit b of arr[i] = 1  <=>  bit b flips between pref[i-1] and pref[i]
#
#   so reconstruct one bit column at a time : scan the column, remember the
#   previous bit (0 before the array starts), and set the bit in the answer
#   wherever it changes. this is the "difference of a parity prefix" view, and
#   it is what makes the O(1)-space trick above believable.
#
#   values are <= 10^6 < 2^20, so at most 20 columns.
#
# time = O(n * B) with B = bit width, space = O(n) for the output
class Solution(object):
    def findArray(self, pref):
        n = len(pref)
        arr = [0] * n
        bits = max(pref).bit_length() if n else 0
        for b in range(bits):
            mask = 1 << b
            prev = 0
            for i in range(n):
                cur = (pref[i] >> b) & 1
                if cur != prev:
                    arr[i] |= mask
                prev = cur
        return arr
