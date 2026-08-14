"""

1974. Minimum Time to Type Word Using Special Typewriter
Easy

There is a special typewriter with lowercase English letters 'a' to 'z' arranged in a circle with a pointer. A character can only be typed if the pointer is pointing to that character. The pointer is initially pointing to the character 'a'.

Each second, you may perform one of the following operations:

Move the pointer one character counterclockwise or clockwise.
Type the character the pointer is currently on.

Given a string word, return the minimum number of seconds to type out the characters in word.


Example 1:

Input: word = "abc"
Output: 5
Explanation:
The characters are printed as follows:
- Type the character 'a' in 1 second since the pointer is initially on 'a'.
- Move the pointer clockwise to 'b' in 1 second.
- Type the character 'b' in 1 second.
- Move the pointer clockwise to 'c' in 1 second.
- Type the character 'c' in 1 second.

Example 2:

Input: word = "bza"
Output: 7
Explanation:
The characters are printed as follows:
- Move the pointer clockwise to 'b' in 1 second.
- Type the character 'b' in 1 second.
- Move the pointer counterclockwise to 'z' in 2 seconds.
- Type the character 'z' in 1 second.
- Move the pointer clockwise to 'a' in 1 second.
- Type the character 'a' in 1 second.

Example 3:

Input: word = "zjpc"
Output: 34
Explanation:
The characters are printed as follows:
- Move the pointer counterclockwise to 'z' in 1 second.
- Type the character 'z' in 1 second.
- Move the pointer clockwise to 'j' in 10 seconds.
- Type the character 'j' in 1 second.
- Move the pointer clockwise to 'p' in 6 seconds.
- Type the character 'p' in 1 second.
- Move the pointer counterclockwise to 'c' in 13 seconds.
- Type the character 'c' in 1 second.


Constraints:

1 <= word.length <= 100
word consists of lowercase English letters.

"""

# V0
# IDEA : GREEDY (each hop is independent -> take the shorter arc)
#
#   the pointer must visit the letters in order, so the moves between two
#   consecutive letters never interact -> just pay the cheapest arc each time.
#
#   on a 26-letter circle, the distance between a and c is
#     d = abs(a - c)  ->  min(d, 26 - d)
#
#   typing costs 1 per character -> start the answer at len(word).
#
# time = O(n), space = O(1)
class Solution(object):
    def minTimeToType(self, word):
        res = len(word)          # one second per keystroke
        cur = ord("a")           # pointer starts on 'a'
        for ch in word:
            c = ord(ch)
            d = abs(c - cur)
            res += min(d, 26 - d)
            cur = c
        return res
