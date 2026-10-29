"""

1647. Minimum Deletions to Make Character Frequencies Unique
Medium

A string s is called good if there are no two different characters in s that
have the same frequency.

Given a string s, return the minimum number of characters you need to delete
to make s good.

The frequency of a character in a string is the number of times it appears in
the string. For example, in the string "aab", the frequency of 'a' is 2, while
the frequency of 'b' is 1.

Example 1:

Input: s = "aab"

Output: 0

Explanation: s is already good.

Example 2:

Input: s = "aaabbbcc"

Output: 2

Explanation: You can delete two 'b's resulting in the good string "aaabcc".
Another way it to delete one 'b' and one 'c' resulting in the good string "aaabbc".

Example 3:

Input: s = "ceabaacb"

Output: 2

Explanation: You can delete both 'c's resulting in the good string "eabaab".
Note that we only care about characters that are still in the string at the
end (i.e. frequency of 0 is ignored).

Constraints:

1 <= s.length <= 10^5
s contains only lowercase English letters.

"""

# V0
# IDEA : MAX HEAP OF FREQUENCIES + GREEDY
#
#   Pop the largest frequency. If the next largest is the SAME, this one has to
#   shrink: delete one char, and push the smaller frequency back so it is
#   re-checked against everything below it. A frequency that reaches 0 means
#   the char is gone entirely, and 0 never collides (it is ignored).
#
#   Shrinking the largest of a tie is safe: every tied value is equivalent,
#   and lowering by exactly 1 is the cheapest move that might break the tie.
#
#   e.g. "aaabbbcc" -> heap [3,3,2]
#        pop 3, top 3 (tie) -> push 2, del=1 -> heap [3,2,2]
#        pop 3, top 2 (unique)              -> heap [2,2]
#        pop 2, top 2 (tie) -> push 1, del=2 -> heap [2,1]
#        pop 2, top 1 (unique)              -> heap [1] -> answer 2
#
# time = O(n + k^2 log k), k = 26 distinct letters -> O(n)
# space = O(k) -> O(1)
import heapq
from collections import Counter


class Solution(object):
    def minDeletions(self, s):
        """
        :type s: str
        :rtype: int
        """
        char_count = Counter(s)

        # heapq is a min heap -> store negated frequencies for a max heap
        max_heap = []
        for freq in char_count.values():
            heapq.heappush(max_heap, -freq)

        deletions = 0

        # NOTE !!! need at least 2 entries: pop one, compare with the next top
        while len(max_heap) > 1:
            top = -heapq.heappop(max_heap)

            # tie with the next largest frequency -> delete one char of `top`
            if top == -max_heap[0]:
                deletions += 1
                top -= 1
                # still present in the string -> re-check it against the rest
                if top > 0:
                    heapq.heappush(max_heap, -top)

        return deletions


# V1
# IDEA : HASH SET OF USED FREQUENCIES + GREEDY
#
#   Walk the frequencies in any order. For each one, keep deleting a char while
#   that frequency is already taken by another char (and is still > 0), then
#   claim it. Every frequency ends at the nearest free value at or below it,
#   which is the fewest deletions for that char.
#
#   e.g. freqs [3,3,2] -> claim 3
#                      -> 3 taken -> 2 free, claim it   (1 del)
#                      -> 2 taken -> 1 free, claim it   (1 del) -> answer 2
#
# time = O(n + k^2), k = 26 -> O(n)
# space = O(k) -> O(1)
from collections import Counter


class Solution2(object):
    def minDeletions(self, s):
        """
        :type s: str
        :rtype: int
        """
        char_count = Counter(s)

        used_freqs = set()
        deletions = 0

        for freq in char_count.values():
            # lower this char's frequency until no other char holds it
            while freq > 0 and freq in used_freqs:
                freq -= 1
                deletions += 1
            # 0 means the char was deleted entirely; 0 is allowed to repeat
            if freq > 0:
                used_freqs.add(freq)

        return deletions


# V2
# IDEA : SORT FREQUENCIES DESC + RUNNING CAP
#
#   Sorted from largest down, each frequency may be at most one less than the
#   frequency kept just before it (`max_allowed`). Clamp it to that cap, pay the
#   difference in deletions, and the clamped value becomes the next cap.
#   Once the cap hits 0, every remaining char is deleted completely.
#
#   e.g. [3,3,2] -> keep 3 (cap 2) -> 3 clamped to 2, +1 (cap 1)
#        -> 2 clamped to 1, +1 (cap 0) -> 2
#
# time = O(n + k log k), k = 26 -> O(n)
# space = O(k) -> O(1)
from collections import Counter


class Solution3(object):
    def minDeletions(self, s):
        """
        :type s: str
        :rtype: int
        """
        char_count = Counter(s)
        freqs = sorted(char_count.values(), reverse=True)

        deletions = 0
        # the largest frequency is always allowed to stay as it is
        max_allowed = freqs[0] if freqs else 0

        for freq in freqs:
            # NOTE !!! this char may keep at most `max_allowed` (never below 0)
            kept = min(freq, max_allowed)
            deletions += freq - kept
            # the next (smaller) char must stay strictly below this one
            max_allowed = max(kept - 1, 0)

        return deletions
