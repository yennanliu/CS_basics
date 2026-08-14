"""

1813. Sentence Similarity III
Medium

You are given two strings sentence1 and sentence2, each representing a sentence composed of words. A sentence is a list of words that are separated by a single space with no leading or trailing spaces. Each word consists of only uppercase and lowercase English characters.

Two sentences s1 and s2 are considered similar if it is possible to insert an arbitrary sentence (possibly empty) inside one of these sentences such that the two sentences become equal. Note that the inserted sentence must be separated from existing words by spaces.

For example,

s1 = "Hello Jane" and s2 = "Hello my name is Jane" can be made equal by inserting "my name is" between "Hello" and "Jane" in s1.
s1 = "Frog cool" and s2 = "Frogs are cool" are not similar, since although there is a sentence "s are" inserted into s1, it is not separated from "Frog" by a space.

Given two sentences sentence1 and sentence2, return true if sentence1 and sentence2 are similar. Otherwise, return false.


Example 1:

Input: sentence1 = "My name is Haley", sentence2 = "My Haley"
Output: true
Explanation:
sentence2 can be turned to sentence1 by inserting "name is" between "My" and "Haley".

Example 2:

Input: sentence1 = "of", sentence2 = "A lot of words"
Output: false
Explanation:
No single sentence can be inserted inside one of the sentences to make it equal to the other.

Example 3:

Input: sentence1 = "Eating right now", sentence2 = "Eating"
Output: true
Explanation:
sentence2 can be turned to sentence1 by inserting "right now" at the end of the sentence.


Constraints:

1 <= sentence1.length, sentence2.length <= 100
sentence1 and sentence2 consist of lowercase and uppercase English letters and spaces.
The words in sentence1 and sentence2 are separated by a single space.

"""

# V0
# IDEA : TWO POINTERS ON WORD LISTS (common prefix + common suffix must cover
#        the shorter sentence)
#
#   split into WORDS (the insertion is word-aligned, so char comparison is wrong).
#   let the longer list be a (len m) and the shorter be b (len n).
#   the inserted block sits somewhere in the middle of a, so b must be
#   a's prefix of length i followed by a's suffix of length j, with i + j == n.
#
#   count the longest common prefix i and the longest common suffix j (each
#   capped at n) and return i + j >= n.
#
#   NOTE : >= not == : prefix and suffix may overlap when the words repeat
#          (e.g. "a a a" vs "a"), and any overlap only makes the split easier.
#
# time = O(n), space = O(n)
class Solution(object):
    def areSentencesSimilar(self, sentence1, sentence2):
        a, b = sentence1.split(), sentence2.split()
        if len(a) < len(b):
            a, b = b, a
        m, n = len(a), len(b)

        i = 0
        while i < n and a[i] == b[i]:
            i += 1

        j = 0
        while j < n and a[m - 1 - j] == b[n - 1 - j]:
            j += 1

        return i + j >= n
