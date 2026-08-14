"""

1927. Sum Game
Medium

Alice and Bob take turns playing a game, with Alice starting first.

You are given a string num of even length consisting of digits and '?' characters. On each turn, a player will do the following if there is still at least one '?' in num:

1. Choose an index i where num[i] == '?'.
2. Replace num[i] with any digit between '0' and '9'.

The game ends when there are no more '?' characters in num.

For Bob to win, the sum of the digits in the first half of num must be equal to the sum of the digits in the second half. For Alice to win, the sums must not be equal.

For example, if the game ended with num = "243801", then Bob wins because 2+4+3 = 8+0+1. If the game ended with num = "243803", then Alice wins because 2+4+3 != 8+0+3.

Assuming Alice and Bob play optimally, return true if Alice will win and false if Bob will win.


Example 1:

Input: num = "5023"
Output: false
Explanation: There are no moves to be made.
The sum of the first half is equal to the sum of the second half: 5 + 0 = 2 + 3.

Example 2:

Input: num = "25??"
Output: true
Explanation: Alice can replace one of the '?'s with '9' and it will be impossible for Bob to make the sums equal.

Example 3:

Input: num = "?3295???"
Output: false
Explanation: It can be proven that Bob will always win. One possible outcome is:
- Alice replaces the first '?' with '9'. num = "93295???".
- Bob replaces one of the '?' in the right half with '9'. num = "932959??".
- Alice replaces one of the '?' in the right half with '2'. num = "9329592?".
- Bob replaces the last '?' with '7'. num = "93295927".
Bob wins because 9 + 3 + 2 + 9 = 5 + 9 + 2 + 7.


Constraints:

2 <= num.length <= 10^5
num.length is even.
num consists of only digits and '?'.

"""

# V0
# IDEA : GAME THEORY / CASE ANALYSIS (closed form, no simulation)
#
#   let s1, s2 be the known digit sums of the two halves and c1, c2 the number
#   of '?' in each half.
#
#   1) if c1 + c2 is ODD, Alice moves last -> she can always break equality
#      with the final digit  => Alice wins.
#
#   2) if it is EVEN, Bob can always mirror Alice: whatever digit d Alice puts
#      in one half, Bob answers 9 - d in the other half. each such pair moves
#      the difference by exactly 9 in favour of the side with more '?'.
#      after cancelling, the remaining |c2 - c1| question marks sit in ONE half
#      and Bob can force the totals to meet exactly when
#
#            s1 - s2 == 9 * (c2 - c1) / 2
#
#      any other value lets Alice push the difference off target (she plays 9
#      or 0 to overshoot / undershoot)  => Alice wins.
#
#   NOTE : compare 2*(s1 - s2) with 9*(c2 - c1) to stay in integers.
#
# time = O(n), space = O(1)
class Solution(object):
    def sumGame(self, num):
        n = len(num)
        s1 = s2 = c1 = c2 = 0
        for i, ch in enumerate(num):
            if i < n // 2:
                if ch == '?':
                    c1 += 1
                else:
                    s1 += int(ch)
            else:
                if ch == '?':
                    c2 += 1
                else:
                    s2 += int(ch)
        if (c1 + c2) % 2 == 1:
            return True
        return 2 * (s1 - s2) != 9 * (c2 - c1)
