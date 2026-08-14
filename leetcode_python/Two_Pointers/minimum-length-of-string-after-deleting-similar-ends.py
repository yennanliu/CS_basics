"""

1750. Minimum Length of String After Deleting Similar Ends
Medium

Given a string s consisting only of characters 'a', 'b', and 'c'. You are asked to apply the following algorithm on the string any number of times:

1. Pick a non-empty prefix from the string s where all the characters in the prefix are equal.
2. Pick a non-empty suffix from the string s where all the characters in this suffix are equal.
3. The prefix and the suffix should not intersect at any index.
4. The characters from the prefix and suffix must be the same.
5. Delete both the prefix and the suffix.

Return the minimum length of s after performing the above operation any number of times (possibly zero times).


Example 1:

Input: s = "ca"
Output: 2
Explanation: You can't remove any characters, so the string stays as is.

Example 2:

Input: s = "cabaabac"
Output: 0
Explanation: An optimal sequence of operations is:
- Take prefix = "c" and suffix = "c" and remove them, s = "abaaba".
- Take prefix = "a" and suffix = "a" and remove them, s = "baab".
- Take prefix = "b" and suffix = "b" and remove them, s = "aa".
- Take prefix = "a" and suffix = "a" and remove them, s = "".

Example 3:

Input: s = "aabccabba"
Output: 3
Explanation: An optimal sequence of operations is:
- Take prefix = "aa" and suffix = "a" and remove them, s = "bccabb".
- Take prefix = "b" and suffix = "bb" and remove them, s = "cca".


Constraints:

1 <= s.length <= 10^5
s only consists of characters 'a', 'b', and 'c'.

"""

# V0
# IDEA : TWO POINTERS SHRINKING INWARD (greedy - always strip the full block)
#
#   i from the left, j from the right. while s[i] == s[j] and i < j, strip
#   the ENTIRE run of that character from both sides.
#
#   greedy is safe : if the two ends match, deleting them can never hurt -
#   any later operation is still available on the shorter string, and
#   leaving a matching char behind only blocks progress. and when the ends
#   differ, no operation applies at all, so we are done.
#
#   NOTE : advance i past its run and j back past its run BEFORE the final
#          i += 1 / j -= 1, so a run like "aaa...aaa" collapses in one go.
#   NOTE : clamp with max(0, ...) - the pointers can cross when everything
#          is consumed.
#
# time = O(n), space = O(1)
class Solution(object):
    def minimumLength(self, s):
        i, j = 0, len(s) - 1

        while i < j and s[i] == s[j]:
            while i + 1 < j and s[i + 1] == s[i]:
                i += 1
            while j - 1 > i and s[j - 1] == s[j]:
                j -= 1
            i += 1
            j -= 1

        return max(0, j - i + 1)
