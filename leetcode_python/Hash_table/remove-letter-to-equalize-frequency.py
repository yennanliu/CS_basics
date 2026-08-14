"""

2423. Remove Letter To Equalize Frequency
Easy

You are given a 0-indexed string word, consisting of lowercase English letters. You need to select one index and remove the letter at that index from word so that the frequency of every letter present in word is equal.

Return true if it is possible to remove one letter so that the frequency of all letters in word are equal, and false otherwise.

Note:

The frequency of a letter x is the number of times it occurs in the string.
You must remove exactly one letter and cannot choose to do nothing.


Example 1:

Input: word = "abcc"
Output: true
Explanation: Select index 3 and delete it: word becomes "abc" and each character has a frequency of 1.

Example 2:

Input: word = "aazz"
Output: false
Explanation: We must delete a character, so either the frequency of "a" is 1 and the frequency of "z" is 2, or vice versa. It is impossible to make all present letters have equal frequency.


Constraints:

2 <= word.length <= 100
word consists of lowercase English letters only.

"""

# V0
# IDEA : TRY REMOVING ONE COPY OF EACH DISTINCT LETTER AND RE-CHECK
#
#   only 26 candidates exist, and after each trial removal the test is simply
#   "do all the SURVIVING letters share one frequency?" — a letter whose
#   count drops to 0 disappears from the alphabet and must be excluded.
#
#   word is at most 100 characters, so re-checking per candidate is trivial.
#
# time = O(26 * 26), space = O(26)
from collections import Counter


class Solution(object):
    def equalFrequency(self, word):
        cnt = Counter(word)
        for c in list(cnt):
            cnt[c] -= 1
            remaining = set(v for v in cnt.values() if v > 0)
            cnt[c] += 1
            if len(remaining) == 1:
                return True
        return False
