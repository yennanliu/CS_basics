"""

843. Guess the Word
Hard

You are given an array of unique strings words where words[i] is six letters long.
One word of words was chosen as a secret word.

You are also given the helper object Master. You may call Master.guess(word) where
word is a six-letter-long string, and it must be from words.
Master.guess(word) returns:

-1 if word is not from words, or
an integer representing the number of exact matches (value and position) of your
guess to the secret word.

There is a parameter allowedGuesses for each test case where allowedGuesses is the
maximum number of times you can call Master.guess(word).

For each test case, you should call Master.guess with the secret word without
exceeding the maximum number of allowed guesses.


Example 1:

Input: secret = "acckzz", words = ["acckzz","ccbazz","eiowzz","abcczz"], allowedGuesses = 10
Output: You guessed the secret word correctly.
Explanation:
master.guess("aaaaaa") returns -1, because "aaaaaa" is not in words.
master.guess("acckzz") returns 6, because "acckzz" is secret and has all 6 matches.
master.guess("ccbazz") returns 3, because "ccbazz" has 3 matches.
master.guess("eiowzz") returns 2, because "eiowzz" has 2 matches.
master.guess("abcczz") returns 4, because "abcczz" has 4 matches.
We made 5 calls to master.guess, and one of them was the secret,
so we pass the test case.

Example 2:

Input: secret = "hamada", words = ["hamada","khaled"], allowedGuesses = 10
Output: You guessed the secret word correctly.
Explanation: Since there are two words, you can guess both.


Constraints:

1 <= words.length <= 100
words[i].length == 6
words[i] consist of lowercase English letters.
All the strings of words are unique.
secret exists in words.
10 <= allowedGuesses <= 30


This is an interactive problem: the Master class is provided by the judge.

    class Master(object):
        def guess(self, word):
            ...   # returns the number of exact position matches, or -1

"""

# V0
# IDEA : GREEDY MINIMAX ("guess the word that shrinks the worst case most")
#
#   After each guess we learn the match count and can drop every candidate
#   that is inconsistent with it. Guessing a RANDOM word is risky: most pairs
#   of random 6-letter words share 0 characters, so a 0 answer barely prunes.
#
#   Instead, before every guess we score each candidate w by
#
#       worst(w) = max over m in 0..5 of  #{c in candidates : match(w, c) == m}
#
#   i.e. the size of the largest bucket we could be left with, and guess the
#   word minimising it. This keeps the candidate set shrinking fast enough to
#   finish within 10 guesses.
#
# time  = O(g * n^2 * L)   # g <= 10 guesses, n = len(words), L = 6
# space = O(n)
class Solution(object):
    def findSecretWord(self, words, master):

        def match(a, b):
            return sum(x == y for x, y in zip(a, b))

        candidates = list(words)

        for _ in range(10):
            # pick the minimax guess among the remaining candidates
            best, best_worst = candidates[0], float('inf')
            for w in candidates:
                buckets = [0] * 7
                for c in candidates:
                    buckets[match(w, c)] += 1
                # bucket 6 means "we already won", so it is not a risk
                worst = max(buckets[:6])
                if worst < best_worst:
                    best_worst, best = worst, w

            m = master.guess(best)
            if m == 6:
                return

            # keep only the words consistent with the feedback
            candidates = [c for c in candidates if match(best, c) == m]
