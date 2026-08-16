"""

2868. The Wording Game
Hard

Alice and Bob each have a lexicographically sorted array of strings named a and b respectively.

They are playing a wording game with the following rules:

On each turn, the current player should play a word from their list such that the new word is closely greater than the last played word; then it's the other player's turn.
If a player can't play a word on their turn, they lose.

Alice starts the game by playing her lexicographically smallest word.

Given a and b, return true if Alice can win knowing that both players play their best, and false otherwise.

A word w is closely greater than a word z if the following conditions are met:

w is lexicographically greater than z.
If w1 is the first letter of w and z1 is the first letter of z, w1 should either be equal to z1 or be the letter after z1 in the alphabet.
For example, the word "care" is closely greater than "book" and "car", but is not closely greater than "ant" or "cook".

A string s is lexicographically greater than a string t if in the first position where s and t differ, string s has a letter that appears later in the alphabet than the corresponding letter in t. If the first min(s.length, t.length) characters do not differ, then the longer string is the lexicographically greater one.


Example 1:

Input: a = ["avokado","dabar"], b = ["brazil"]
Output: false
Explanation: Alice must start the game by playing the word "avokado" since it's her smallest word, then Bob plays his only word, "brazil", which he can play because its first letter, 'b', is the letter after Alice's word's first letter, 'a'.
Alice can't play a word since the first letter of the only word left is not equal to 'b' or the letter after 'b', 'c'.
So, Alice loses, and the game ends.

Example 2:

Input: a = ["ananas","atlas","banana"], b = ["albatros","cikla","nogomet"]
Output: true
Explanation: Alice must start the game by playing the word "ananas".
Bob can't play a word since the only word he has that starts with the letter 'a' or 'b' is "albatros", which is smaller than Alice's word.
So Alice wins, and the game ends.

Example 3:

Input: a = ["hrvatska","zastava"], b = ["bijeli","galeb"]
Output: true
Explanation: Alice must start the game by playing the word "hrvatska".
Bob can't play a word since the first letter of both of his words are smaller than the first letter of Alice's word, 'h'.
So Alice wins, and the game ends.


Constraints:

1 <= a.length, b.length <= 10^5
a[i] and b[i] consist only of lowercase English letters.
a and b are lexicographically sorted.
All the words in a and b combined are distinct.
The sum of the lengths of all the words in a and b combined does not exceed 10^6.

"""

# V0
# IDEA : GREEDY SIMULATION + TWO POINTERS
#
#   Both lists are already sorted, and the played word strictly increases, so a
#   word that is unplayable now can never become playable later (it is either
#   already too small, or its first letter is >= 2 letters ahead — and every
#   word after it in the sorted list starts with a letter that is at least as
#   far ahead). That means each player only ever scans forward: one monotone
#   pointer per list.
#
#   NOTE : the optimal move is always the SMALLEST playable word. Playing a
#          bigger word only shifts the "first letter window" further along,
#          which throws away your own future words without taking anything
#          away from the opponent (they answer the smallest word with the very
#          same set of candidates, or more).
#
#   NOTE : "closely greater" is w > z AND ord(w[0]) - ord(z[0]) in {0, 1}.
#          When the first letters differ by exactly 1 the lexicographic check
#          is automatic, so only the equal-first-letter case needs w > z.
#
#   The game ends the moment the player to move has no playable word left:
#   Bob stuck -> Alice wins, Alice stuck -> Alice loses.
#
# time = O(m + n), space = O(1)
class Solution(object):
    def canAliceWin(self, a, b):
        def closely_greater(w, z):
            gap = ord(w[0]) - ord(z[0])
            if gap == 1:
                return True
            return gap == 0 and w > z

        i, j = 1, 0            # next candidate index for Alice / Bob
        word = a[0]            # Alice opens with her smallest word
        alice_turn = False     # Bob moves next

        while True:
            if alice_turn:
                while i < len(a) and not closely_greater(a[i], word):
                    i += 1
                if i == len(a):
                    return False
                word = a[i]
                i += 1
            else:
                while j < len(b) and not closely_greater(b[j], word):
                    j += 1
                if j == len(b):
                    return True
                word = b[j]
                j += 1
            alice_turn = not alice_turn
