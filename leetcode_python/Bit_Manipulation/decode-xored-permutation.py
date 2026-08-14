"""

1734. Decode XORed Permutation
Medium

There is an integer array perm that is a permutation of the first n positive integers, where n is always odd.

It was encoded into another integer array encoded of length n - 1, such that encoded[i] = perm[i] XOR perm[i + 1]. For example, if perm = [1,3,2], then encoded = [2,1].

Given the encoded array, return the original array perm. It is guaranteed that the answer exists and is unique.


Example 1:

Input: encoded = [3,1]
Output: [1,2,3]
Explanation: If perm = [1,2,3], then encoded = [1 XOR 2,2 XOR 3] = [3,1]

Example 2:

Input: encoded = [6,5,4,6]
Output: [2,4,1,5,3]


Constraints:

3 <= n < 10^5
n is odd.
encoded.length == n - 1

"""

# V0
# IDEA : BIT MANIPULATION - recover ONE element first, then unroll the chain
#
#   unlike LC 1720, perm[0] is not given. exploit that perm is a permutation
#   of 1..n with n ODD:
#
#     total = 1 ^ 2 ^ ... ^ n = perm[0] ^ perm[1] ^ ... ^ perm[n-1]
#
#   now pair the elements up using the ODD-INDEXED encoded entries:
#     encoded[1] = perm[1]^perm[2], encoded[3] = perm[3]^perm[4], ...
#   there are (n-1)/2 of them and together they cover perm[1..n-1] exactly
#   once. call their XOR `odd`. everything cancels except perm[0]:
#
#     perm[0] = total ^ odd
#
#   then perm[i+1] = perm[i] ^ encoded[i] walks the rest of the array.
#
#   NOTE : n odd is essential - it is what leaves exactly one unpaired
#          element (perm[0]) after chaining the pairs.
#
# time = O(n), space = O(n) for the output
class Solution(object):
    def decode(self, encoded):
        n = len(encoded) + 1

        total = 0
        for v in range(1, n + 1):
            total ^= v

        odd = 0
        for i in range(1, n - 1, 2):
            odd ^= encoded[i]

        res = [total ^ odd]
        for x in encoded:
            res.append(res[-1] ^ x)
        return res
