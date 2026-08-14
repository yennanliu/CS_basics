"""

2351. First Letter to Appear Twice
Easy

Given a string s consisting of lowercase English letters, return the first letter to appear twice.

Note:

A letter a appears twice before another letter b if the second occurrence of a is before the second occurrence of b.
s will contain at least one letter that appears twice.


Example 1:

Input: s = "abccbaacz"
Output: "c"
Explanation:
The letter 'a' appears on the indexes 0, 5 and 6.
The letter 'b' appears on the indexes 1 and 4.
The letter 'c' appears on the indexes 2, 3 and 7.
The letter 'z' appears on the index 8.
The letter 'c' is the first letter to appear twice, because out of all the letters the index of its second occurrence is the smallest.

Example 2:

Input: s = "abcdd"
Output: "d"
Explanation:
The only letter that appears twice is 'd' so we return 'd'.


Constraints:

2 <= s.length <= 100
s consists of lowercase English letters.
s has at least one repeated letter.

"""

# V0
# IDEA : HASH SET (first char whose 2nd occurrence comes earliest = first repeat while scanning)
#
#   Walking left to right, the first character we meet that is already in the
#   seen-set is exactly the letter whose SECOND occurrence has the smallest
#   index -- which is the definition asked for.
#
# time = O(n), space = O(26)
class Solution(object):
    def repeatedCharacter(self, s):
        seen = set()
        for c in s:
            if c in seen:
                return c
            seen.add(c)
        return ""
