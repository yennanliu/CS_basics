"""

2268. Minimum Number of Keypresses
Medium

You have a keypad with 9 buttons, numbered from 1 to 9, each mapped to lowercase English letters. You can choose which characters each button is matched to as long as:

All 26 lowercase English letters are mapped to.
Each character is mapped to by exactly 1 button.
Each button maps to at most 3 characters.

To type the first character matched to a button, you press the button once. To type the second character, you press the button twice, and so on.

Given a string s, return the minimum number of keypresses needed to type s using your keypad.

Note that the characters mapped to by each button, and the order they are mapped in cannot be changed.


Example 1:

Input: s = "apple"
Output: 5
Explanation: One optimal way to setup your keypad is shown above.
Type 'a' by pressing button 1 once.
Type 'p' by pressing button 6 once.
Type 'p' by pressing button 6 once.
Type 'l' by pressing button 5 once.
Type 'e' by pressing button 3 once.
A total of 5 button presses are needed, so return 5.

Example 2:

Input: s = "abcdefghijkl"
Output: 15
Explanation: One optimal way to setup your keypad is shown above.
The letters 'a' to 'i' can each be typed by pressing a button once.
Type 'j' by pressing button 1 twice.
Type 'k' by pressing button 2 twice.
Type 'l' by pressing button 3 twice.
A total of 15 button presses are needed, so return 15.


Constraints:

1 <= s.length <= 10^5
s consists of lowercase English letters.

"""

# V0
# IDEA : GREEDY (exchange argument - cheapest slots go to the most frequent letters)
#
#   there are 9 buttons, so exactly 9 letters cost 1 press, the next 9 cost
#   2 presses, and the remaining 8 cost 3 presses. that slot structure is
#   fixed; only the ASSIGNMENT of letters to slots is ours to choose.
#
#   total = sum(cost_of_slot * freq_of_letter_in_it). by the rearrangement
#   inequality this is minimised by pairing the smallest costs with the
#   largest frequencies -> sort frequencies descending and hand out
#   cost 1 to ranks 1..9, cost 2 to ranks 10..18, cost 3 to the rest.
#
# time = O(n + 26 log 26), space = O(26)
from collections import Counter
class Solution(object):
    def minimumKeypresses(self, s):
        freq = sorted(Counter(s).values(), reverse=True)
        res = 0
        for i, f in enumerate(freq):
            res += (i // 9 + 1) * f
        return res
