"""

3306. Count of Substrings Containing Every Vowel and K Consonants II
Medium

You are given a string word and a non-negative integer k.

Return the total number of substrings of word that contain every vowel ('a', 'e', 'i', 'o', and 'u') at least once and exactly k consonants.


Example 1:

Input: word = "aeioqq", k = 1
Output: 0
Explanation:
There is no substring with every vowel.

Example 2:

Input: word = "aeiou", k = 0
Output: 1
Explanation:
The only substring with every vowel and zero consonants is word[0..4], which is "aeiou".

Example 3:

Input: word = "ieaouqqieaouqq", k = 1
Output: 3
Explanation:
The substrings with every vowel and one consonant are:
word[0..5], which is "ieaouq".
word[6..11], which is "qieaou".
word[7..12], which is "ieaouq".


Constraints:

5 <= word.length <= 2 * 10^5
word consists only of lowercase English letters.
0 <= k <= word.length - 5

"""

# V0
# IDEA : SAME "AT LEAST k MINUS AT LEAST k+1" TRICK, AT 2*10^5 SCALE
#
#   the exact-consonant requirement is not monotone, but "at least k" is, so
#   the answer is the difference of two sliding-window counts.
#
#   each pass extends the right end, shrinks the left while the window still
#   holds all five vowels and enough consonants, and adds `left` — the number
#   of valid starts for that right end.
#
#   this is LC 3305 with a larger input; the algorithm was already linear, so
#   only the constant factor matters and the vowel bookkeeping is kept to
#   five counters.
#
# time = O(n), space = O(1)
class Solution(object):
    def countOfSubstrings(self, word, k):
        idx = {'a': 0, 'e': 1, 'i': 2, 'o': 3, 'u': 4}

        def at_least(need):
            cnt = [0] * 5
            distinct = 0
            cons = 0
            left = 0
            res = 0
            for ch in word:
                v = idx.get(ch)
                if v is None:
                    cons += 1
                else:
                    if cnt[v] == 0:
                        distinct += 1
                    cnt[v] += 1
                while distinct == 5 and cons >= need:
                    d = idx.get(word[left])
                    if d is None:
                        cons -= 1
                    else:
                        cnt[d] -= 1
                        if cnt[d] == 0:
                            distinct -= 1
                    left += 1
                res += left
            return res

        return at_least(k) - at_least(k + 1)
