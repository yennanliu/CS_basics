"""

2231. Largest Number After Digit Swaps by Parity
Easy

You are given a positive integer num. You may swap any two digits of num that have the same parity (i.e. both odd digits or both even digits).

Return the largest possible value of num after any number of swaps.


Example 1:

Input: num = 1234
Output: 3412
Explanation: Swap the digit 3 with the digit 1, this results in the number 3214.
Swap the digit 2 with the digit 4, this results in the number 3412.
Note that there may be other sequences of swaps but it can be shown that 3412 is the largest possible number.
Also note that we may not swap the digit 4 with the digit 1 since they are of different parities.

Example 2:

Input: num = 65875
Output: 87655
Explanation: Swap the digit 8 with the digit 6, this results in the number 85675.
Swap the first digit 5 with the digit 7, this results in the number 87655.
Note that there may be other sequences of swaps but it can be shown that 87655 is the largest possible number.


Constraints:

1 <= num <= 10^9

"""

# V0
# IDEA : SORT EACH PARITY CLASS DESCENDING, THEN REFILL IN PLACE
#
#   swaps are unrestricted WITHIN a parity class, so the multiset of odd
#   digits can be permuted freely among the odd positions, same for even.
#   to maximise the number, put the largest available digit of the right
#   parity at each position, left to right.
#
#   NOTE : positions keep their parity - digit at index i must stay odd if it
#          started odd, so the two classes never mix.
#
# time = O(d log d), d = number of digits, space = O(d)
class Solution(object):
    def largestInteger(self, num):
        digits = [int(c) for c in str(num)]
        # descending pools, popped from the back
        odd = sorted([d for d in digits if d % 2 == 1])
        even = sorted([d for d in digits if d % 2 == 0])

        res = 0
        for d in digits:
            pick = odd.pop() if d % 2 == 1 else even.pop()
            res = res * 10 + pick
        return res
