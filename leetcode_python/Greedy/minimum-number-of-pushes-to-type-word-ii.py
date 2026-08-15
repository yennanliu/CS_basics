"""

3016. Minimum Number of Pushes to Type Word II
Medium

You are given a string word containing lowercase English letters.

Telephone keypads have keys mapped with distinct collections of lowercase English letters, which can be used to form words by pushing them. For example, the key 2 is mapped with ["a","b","c"], we need to push the key one time to type "a", two times to type "b", and three times to type "c".

It is allowed to remap the keys numbered 2 to 9 to distinct collections of letters. The keys can be remapped to any amount of letters, but each letter must be mapped to exactly one key. You need to find the minimum number of times the keys will be pushed to type the string word.

Return the minimum number of pushes needed to type word after remapping the keys.

An example mapping of letters to keys on a telephone keypad is given below. Note that 1, *, #, and 0 do not map to any letters.


Example 1:

Input: word = "abcde"
Output: 5
Explanation: The remapped keypad given in the image provides the minimum cost.
"a" -> one push on key 2
"b" -> one push on key 3
"c" -> one push on key 4
"d" -> one push on key 5
"e" -> one push on key 6
Total cost is 1 + 1 + 1 + 1 + 1 = 5.
It can be shown that no other mapping can provide a lower cost.

Example 2:

Input: word = "xyzxyzxyzxyz"
Output: 12
Explanation: The remapped keypad given in the image provides the minimum cost.
"x" -> one push on key 2
"y" -> one push on key 3
"z" -> one push on key 4
Total cost is 1 * 4 + 1 * 4 + 1 * 4 = 12
It can be shown that no other mapping can provide a lower cost.
Note that the key 9 is not mapped to any letter: it is not necessary to map letters to every key, but to map all the letters.

Example 3:

Input: word = "aabbccddeeffgghhiiiiii"
Output: 24
Explanation: The remapped keypad given in the image provides the minimum cost.
"a" -> one push on key 2
"b" -> one push on key 3
"c" -> one push on key 4
"d" -> one push on key 5
"e" -> one push on key 6
"f" -> one push on key 7
"g" -> one push on key 8
"h" -> two pushes on key 9
"i" -> one push on key 9
Total cost is 1 * 2 + 1 * 2 + 1 * 2 + 1 * 2 + 1 * 2 + 1 * 2 + 1 * 2 + 2 * 2 + 1 * 6 = 24.
It can be shown that no other mapping can provide a lower cost.


Constraints:

1 <= word.length <= 10^5
word consists of lowercase English letters.

"""

# V0
# IDEA : EXCHANGE ARGUMENT — THE MOST FREQUENT LETTERS GET THE CHEAPEST SLOTS
#
#   the eight keys offer slots in tiers : 8 slots costing 1 push, then 8 more
#   costing 2, and so on. a letter with frequency f placed in a slot of cost
#   c contributes f * c, so pairing the largest frequency with the smallest
#   cost minimises the total (swapping any inverted pair strictly helps).
#
#   sort the 26 frequencies descending, and the i-th of them lands in a slot
#   costing i // 8 + 1.
#
# time = O(n), space = O(1)  (26 counters)
from collections import Counter


class Solution(object):
    def minimumPushes(self, word):
        freq = sorted(Counter(word).values(), reverse=True)
        return sum(f * (i // 8 + 1) for i, f in enumerate(freq))
