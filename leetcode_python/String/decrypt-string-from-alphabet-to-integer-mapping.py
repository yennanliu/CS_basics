"""

1309. Decrypt String from Alphabet to Integer Mapping
Easy

You are given a string s formed by digits and '#'. We want to map s to English
lowercase characters as follows:

- Characters ('a' to 'i') are represented by ('1' to '9') respectively.
- Characters ('j' to 'z') are represented by ('10#' to '26#') respectively.

Return the string formed after mapping.

The test cases are generated so that a unique mapping will always exist.


Example 1:

Input: s = "10#11#12"
Output: "jkab"
Explanation: "j" -> "10#" , "k" -> "11#" , "a" -> "1" , "b" -> "2".

Example 2:

Input: s = "1326#"
Output: "acz"


Constraints:

1 <= s.length <= 1000
s consists of digits and the '#' letter.
s will be a valid string such that mapping is always possible.

"""

# V0
# IDEA : simulation, scan left -> right and look ahead for '#'
#        s[i] and s[i+1] form a 2 digit code only when s[i+2] == '#'
# time = O(n)
# space = O(n)
class Solution(object):
    def freqAlphabets(self, s):
        res = []
        i, n = 0, len(s)
        while i < n:
            if i + 2 < n and s[i + 2] == "#":
                res.append(chr(int(s[i:i + 2]) + ord("a") - 1))
                i += 3
            else:
                res.append(chr(int(s[i]) + ord("a") - 1))
                i += 1
        return "".join(res)
