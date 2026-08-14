"""

1668. Maximum Repeating Substring
Easy

For a string sequence, a string word is k-repeating if word concatenated k times is a substring of
sequence. The word's maximum k-repeating value is the highest value k where word is k-repeating in
sequence. If word is not a substring of sequence, word's maximum k-repeating value is 0.

Given strings sequence and word, return the maximum k-repeating value of word in sequence.


Example 1:

Input: sequence = "ababc", word = "ab"
Output: 2
Explanation: "abab" is a substring in "ababc".

Example 2:

Input: sequence = "ababc", word = "ba"
Output: 1
Explanation: "ba" is a substring in "ababc". "baba" is not a substring in "ababc".

Example 3:

Input: sequence = "ababc", word = "ac"
Output: 0
Explanation: "ac" is not a substring in "ababc".


Constraints:

1 <= sequence.length <= 100
1 <= word.length <= 100
sequence and word contains only lowercase English letters.

"""

# V0
# IDEA : BRUTE FORCE SEARCH DOWNWARD (n <= 100 so k is at most 100)
#
#   k is monotone : if word*k is a substring then word*(k-1) is too.
#   so scan k from the largest possible value len(sequence)//len(word) downwards
#   and return the first k whose repetition is contained in sequence.
#
#   NOTE : k = 0 gives the empty string, always a substring -> the loop always
#          terminates with the correct answer 0 for a non-matching word.
#
# time = O(n^2 / m * m) = O(n^2) with n = len(sequence), space = O(n)
class Solution(object):
    def maxRepeating(self, sequence, word):
        k = len(sequence) // len(word)
        while k > 0:
            if word * k in sequence:
                return k
            k -= 1
        return 0
