"""

1153. String Transforms Into Another String
Hard

Given two strings str1 and str2 of the same length, determine whether you can transform
str1 into str2 by doing zero or more conversions.

In one conversion you can convert all occurrences of one character in str1
to any other lowercase English character.

Return true if and only if you can transform str1 into str2.


Example 1:

Input: str1 = "aabcc", str2 = "ccdee"
Output: true
Explanation: Convert 'c' to 'e' then 'b' to 'd' then 'a' to 'c'.
Note that the order of conversions matter.

Example 2:

Input: str1 = "leetcode", str2 = "codeleet"
Output: false
Explanation: There is no way to transform str1 to str2.


Constraints:

1 <= str1.length == str2.length <= 10^4
str1 and str2 contain only lowercase English letters.

"""

# V0
# IDEA : HASH TABLE (functional mapping) + "spare character" argument
#        1) the mapping str1[i] -> str2[i] must be a FUNCTION
#           (one source char cannot map to 2 different targets)
#        2) conversions are done one char at a time, so a cycle (e.g. a->b, b->a)
#           needs a temporary "parking" letter.
#           -> if str2 already uses all 26 letters there is no spare letter,
#              so only the trivial case str1 == str2 works.
# time = O(n)
# space = O(1)
class Solution(object):
    def canConvert(self, str1, str2):
        if str1 == str2:
            return True

        # NOTE !!! no free letter left to break a cycle
        if len(set(str2)) == 26:
            return False

        mapping = {}
        for a, b in zip(str1, str2):
            if a in mapping and mapping[a] != b:
                return False
            mapping[a] = b
        return True
