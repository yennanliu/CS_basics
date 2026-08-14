"""

1941. Check if All Characters Have Equal Number of Occurrences
Easy

Given a string s, return true if s is a good string, or false otherwise.

A string s is good if all the characters that appear in s have the same number of occurrences (i.e., the same frequency).


Example 1:

Input: s = "abacbc"
Output: true
Explanation: The characters that appear in s are 'a', 'b', and 'c'. All characters occur 2 times in s.

Example 2:

Input: s = "aaabb"
Output: false
Explanation: The characters that appear in s are 'a' and 'b'.
'a' occurs 3 times while 'b' occurs 2 times, which is not the same number of times.


Constraints:

1 <= s.length <= 1000
s consists of lowercase English letters.

"""

# V0
# IDEA : HASH TABLE COUNT, THEN COLLAPSE THE COUNTS INTO A SET
#
#   count every character, then look at the SET of the counts. the string is
#   "good" exactly when that set has a single element.
#
#   NOTE : Counter only stores characters that actually occur, so absent
#          letters never pollute the check.
#   NOTE : s is non-empty by the constraints, so the set is never empty.
#
# time = O(n), space = O(26)
from collections import Counter
class Solution(object):
    def areOccurrencesEqual(self, s):
        return len(set(Counter(s).values())) == 1
