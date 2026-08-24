"""

3595. Once Twice
Medium

You are given an integer array nums. In this array:

Exactly one element appears once.
Exactly one element appears twice.
All other elements appear exactly three times.

Return an integer array of length 2, where the first element is the one that
appears once, and the second is the one that appears twice.

Your solution must run in O(n) time and O(1) space.


Example 1:

Input: nums = [2,2,3,2,5,5,5,7,7]
Output: [3,7]
Explanation:
The element 3 appears once, and the element 7 appears twice. The remaining
elements each appear three times.

Example 2:

Input: nums = [4,4,6,4,9,9,9,6,8]
Output: [8,6]
Explanation:
The element 8 appears once, and the element 6 appears twice. The remaining
elements each appear three times.


Constraints:

3 <= nums.length <= 10^5
-2^31 <= nums[i] <= 2^31 - 1
nums.length is a multiple of 3
Exactly one element appears once, one element appears twice, and all other
elements appear three times.

"""

# V0
# IDEA : BIT-COUNT MOD 3, THEN SPLIT ON A BIT WHERE THE TWO ANSWERS DIFFER
#
#   count the 1s at each bit position modulo 3. every value occurring three
#   times contributes 0 there, so the residue at bit i is (a_i + 2*b_i) % 3,
#   with a the once-element and b the twice-element. residue 1 means a owns
#   the bit and b does not, residue 2 means the reverse, and residue 0 is
#   ambiguous — it covers both "neither owns it" and "both own it" — so a
#   single pass cannot separate a from b.
#
#   what the pass does give is a bit on which a and b differ, and such a bit
#   must exist: a == b is impossible, because a value appearing once and also
#   twice would appear three times. splitting the array on that bit sends a
#   and b to opposite halves while every triple stays intact inside one half,
#   since all three copies agree on the bit.
#
#   each half is then an unambiguous single-number problem. the half holding a
#   is {a} plus whole triples, so its residues are exactly a's bits; the half
#   holding b is {b, b} plus whole triples, so residue 2 marks exactly b's
#   bits.
#
#   the mod-3 counting is done with the usual two-register state machine, which
#   advances every bit position through 00 -> 01 -> 10 -> 00 in lockstep, so no
#   per-bit array is needed and the space stays O(1). values are folded into
#   32-bit two's complement on the way in and unfolded on the way out.
#
"""

DP def
    (BIT-COUNT MOD 3 as a two-register state machine)

    ones, twos: the bit positions whose 1-count is 1 resp. 2 modulo 3

               -> every value occurring three times contributes 0, so the
                  residue at bit i is (a_i + 2 * b_i) % 3

DP eq

     per element v:  ones = (ones ^ v) & ~twos

                     twos = (twos ^ v) & ~ones

        (each bit position advances 00 -> 01 -> 10 -> 00 in lockstep,
         so no per-bit array is needed -> O(1) space)


    -> e.g. residue 1 -> a owns the bit, b does not
              residue 2 -> the reverse
              residue 0 -> AMBIGUOUS (neither owns it, or both do)

         so ONE pass cannot separate a from b. what it does give is a bit
         where a and b DIFFER - and such a bit must exist, since a == b
         would mean a value appearing once AND twice, i.e. three times.

         SPLIT the array on that bit: a and b land in opposite halves while
         every triple stays intact (all three copies agree on the bit)

     then each half is unambiguous: the a-half's residues ARE a's bits;
     in the b-half residue 2 marks exactly b's bits

     ans = [a, b]

"""
# time = O(n), space = O(1)
class Solution(object):
    def onceTwice(self, nums):
        MASK32 = (1 << 32) - 1

        # (ones, twos) = bit positions whose count is 1 resp. 2 modulo 3,
        # restricted to the elements selected by `keep`
        def count_mod3(keep):
            ones = twos = 0
            for v in nums:
                v &= MASK32
                if not keep(v):
                    continue
                ones = (ones ^ v) & (MASK32 ^ twos)
                twos = (twos ^ v) & (MASK32 ^ ones)
            return ones, twos

        def signed(x):
            return x - (1 << 32) if x >> 31 else x

        ones, twos = count_mod3(lambda v: True)

        # any non-zero residue marks a bit where the two answers disagree
        diff = ones | twos
        bit = diff & -diff

        inside = count_mod3(lambda v: v & bit)
        outside = count_mod3(lambda v: not (v & bit))

        # ones & bit tells which half the once-element landed in
        if ones & bit:
            once, twice = inside[0], outside[1]
        else:
            once, twice = outside[0], inside[1]
        return [signed(once), signed(twice)]
