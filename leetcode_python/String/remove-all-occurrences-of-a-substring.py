"""

1910. Remove All Occurrences of a Substring
Medium

Given two strings s and part, perform the following operation on s until all occurrences of the substring part are removed:

Find the leftmost occurrence of the substring part and remove it from s.

Return s after removing all occurrences of part.

A substring is a contiguous sequence of characters in a string.


Example 1:

Input: s = "daabcbaabcbc", part = "abc"
Output: "dab"
Explanation: The following operations are done:
- s = "daabcbaabcbc", remove "abc" starting at index 2, so s = "dabaabcbc".
- s = "dabaabcbc", remove "abc" starting at index 4, so s = "dababc".
- s = "dababc", remove "abc" starting at index 3, so s = "dab".
Now s has no occurrences of "abc".

Example 2:

Input: s = "axxxxyyyyb", part = "xy"
Output: "ab"
Explanation: The following operations are done:
- s = "axxxxyyyyb", remove "xy" starting at index 4 so s = "axxxyyyb".
- s = "axxxyyyb", remove "xy" starting at index 3 so s = "axxyyb".
- s = "axxyyb", remove "xy" starting at index 2 so s = "axyb".
- s = "axyb", remove "xy" starting at index 1 so s = "ab".
Now s has no occurrences of "xy".


Constraints:

1 <= s.length <= 1000
1 <= part.length <= 1000
s and part consists of lowercase English letters.

"""

# V0
# IDEA : STACK (one pass, pop the tail whenever it just became `part`)
#
#   push chars of s onto a stack; after every push, if the top m = len(part)
#   chars equal `part`, pop them all off.
#
#   this reproduces "always remove the leftmost occurrence" exactly, because a
#   match can only be completed by the char we just pushed, and deleting it may
#   expose an earlier match which the stack tail already holds.
#
#   NOTE : repeated s.replace(part, '', 1) is O(n^2 / m) worst case; the stack
#          is a single left-to-right pass.
#
# time = O(n * m), space = O(n)
class Solution(object):
    def removeOccurrences(self, s, part):
        m = len(part)
        stack = []
        for c in s:
            stack.append(c)
            if len(stack) >= m and "".join(stack[-m:]) == part:
                del stack[-m:]
        return "".join(stack)
