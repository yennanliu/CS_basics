"""

1805. Number of Different Integers in a String
Easy

You are given a string word that consists of digits and lowercase English letters.

You will replace every non-digit character with a space. For example, "a123bc34d8ef34" will become " 123  34 8  34". Notice that you are left with some integers that are separated by at least one space: "123", "34", "8", and "34".

Return the number of different integers after performing the replacement operations on word.

Two integers are considered different if their decimal representations without any leading zeros are different.


Example 1:

Input: word = "a123bc34d8ef34"
Output: 3
Explanation: The three different integers are "123", "34", and "8". Notice that "34" is only counted once.

Example 2:

Input: word = "leet1234code234"
Output: 2

Example 3:

Input: word = "a1b01c001"
Output: 1
Explanation: The three integers "1", "01", and "001" all represent the same integer because
the leading zeros are ignored when comparing their decimal values.


Constraints:

1 <= word.length <= 1000
word consists of digits and lowercase English letters.

"""

# V0
# IDEA : SCAN + STRIP LEADING ZEROS, STORE AS STRING IN A SET
#
#   walk the string; whenever a digit starts a run, skip its leading '0's,
#   take the rest of the run as the canonical decimal representation, add to
#   a set. the set size is the answer.
#
#   NOTE : the numbers can be up to 1000 digits long -- comparing them as
#          STRINGS (after zero-stripping) is exact; int() would work in python
#          but the string form is the language-neutral trick.
#   NOTE : "000" strips down to "" which is a fine (and unique) canonical form
#          for zero.
#
# time = O(n), space = O(n)
class Solution(object):
    def numDifferentIntegers(self, word):
        seen = set()
        i, n = 0, len(word)
        while i < n:
            if not word[i].isdigit():
                i += 1
                continue
            # skip leading zeros
            while i < n and word[i] == '0':
                i += 1
            j = i
            while j < n and word[j].isdigit():
                j += 1
            seen.add(word[i:j])
            i = j
        return len(seen)
