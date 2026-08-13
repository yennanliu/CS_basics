"""

481. Magical String
Medium

A magical string s consists of only '1' and '2' and obeys the following rule:

- Concatenating the sequence of lengths of its consecutive groups of identical
  characters '1' and '2' generates the string s itself.

The first few elements of s is s = "1221121221221121122......". If we group the
consecutive 1's and 2's in s, it will be "1 22 11 2 1 22 1 22 11 2 11 22 ......"
and counting the occurrences of 1's or 2's in each group yields the sequence
"1 2 2 1 1 2 1 2 2 1 2 2 ......".

You can see that concatenating the occurrence sequence gives us s itself.

Given an integer n, return the number of 1's in the first n number in the
magical string s.

Example 1:

Input: n = 6
Output: 3
Explanation: The first 6 elements of magical string s is "122112" and it
contains three 1's, so return 3.

Example 2:

Input: n = 1
Output: 1

Constraints:

1 <= n <= 10^5

"""

# V0
# IDEA : SELF-REFERENTIAL SIMULATION (TWO POINTERS)
#
#  The string describes its own run lengths, so we grow it while reading it:
#
#    - seed with "122"  (the first two groups: "1" and "22")
#    - a pointer i walks the string; s[i] is the LENGTH of the next group
#    - group values alternate 1, 2, 1, 2, ... so the next value is 3 - s[-1]
#
#      1 2 2            i = 2, s[i] = 2, last = 2 -> append "11"
#          ^
#      1 2 2 1 1        i = 3, s[i] = 1, last = 1 -> append "2"
#            ^
#      1 2 2 1 1 2      i = 4, s[i] = 1, last = 2 -> append "1"
#              ^
#
#  Stop once the string is at least n long, then count the 1's in s[:n].
#
# time = O(n)
# space = O(n)
class Solution(object):
    def magicalString(self, n):
        s = [1, 2, 2]
        i = 2                       # points at the digit giving the next group size

        while len(s) < n:
            nxt = 3 - s[-1]         # groups alternate between 1 and 2
            s.extend([nxt] * s[i])
            i += 1

        return s[:n].count(1)
