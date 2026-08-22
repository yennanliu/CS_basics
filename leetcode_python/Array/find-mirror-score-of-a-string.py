"""

3412. Find Mirror Score of a String
Medium

You are given a string s.

We define the mirror of a letter in the English alphabet as its corresponding
letter when the alphabet is reversed. For example, the mirror of 'a' is 'z', and
the mirror of 'y' is 'b'.

Initially, all characters in the string s are unmarked.

You start with a score of 0, and you perform the following process on the string
s:

Iterate through the string from left to right.
At each index i, find the closest unmarked index j such that j < i and s[j] is
the mirror of s[i]. Then, mark both indices i and j, and add the value i - j to
the total score.
If no such index j exists for the index i, move on to the next index without
making any changes.

Return the total score at the end of the process.

Example 1:

Input: s = "aczzx"

Output: 5

Explanation:

i = 0. There is no index j that satisfies the conditions, so we skip.
i = 1. There is no index j that satisfies the conditions, so we skip.
i = 2. The closest index j that satisfies the conditions is j = 0, so we mark
both indices 0 and 2, and then add 2 - 0 = 2 to the score.
i = 3. There is no index j that satisfies the conditions, so we skip.
i = 4. The closest index j that satisfies the conditions is j = 1, so we mark
both indices 1 and 4, and then add 4 - 1 = 3 to the score.

Example 2:

Input: s = "abcdef"

Output: 0

Explanation:

For each index i, there is no index j that satisfies the conditions.

Constraints:

1 <= s.length <= 10^5
s consists only of lowercase English letters.

"""

# V0
# IDEA : ONE STACK OF UNMARKED INDICES PER LETTER
#
#   the rule "closest unmarked j < i" is last-in-first-out over the indices
#   that carry a given letter: when we look for a partner for s[i] we want the
#   most recent surviving occurrence of its mirror, and any older one stays
#   available for a later i.  that is exactly a stack per letter.
#
#   marking is implicit — popping an index removes it from its stack forever,
#   so it can never be matched twice, and an index that never gets popped
#   simply stays on its own stack.
#
#   the mirror of a letter c is 'a' + 'z' - c, so the pairing is symmetric and
#   we only ever consult one bucket per position.
#
# time = O(n), space = O(n)
class Solution(object):
    def calculateScore(self, s):
        stacks = [[] for _ in range(26)]
        score = 0
        for i, ch in enumerate(s):
            c = ord(ch) - 97
            m = 25 - c
            if stacks[m]:
                score += i - stacks[m].pop()
            else:
                stacks[c].append(i)
        return score


# V0-1
# IDEA : BRUTE FORCE — MARK ARRAY + BACKWARD SCAN FOR THE PARTNER
#
#   the literal transcription of the process : for each i, walk j from i - 1
#   down to 0 and stop at the first unmarked position holding the mirror
#   letter, then mark both and add i - j.
#   no auxiliary structure beyond the marks, but a string with few matches
#   rescans the whole prefix every time -> O(n^2), which TLEs at n = 10^5.
#   kept as the oracle the stack solutions are checked against.
#
# time = O(n^2), space = O(n)
class Solution(object):
    def calculateScore(self, s):
        n = len(s)
        marked = [False] * n
        score = 0
        for i in range(n):
            target = chr(ord('a') + ord('z') - ord(s[i]))
            for j in range(i - 1, -1, -1):
                if not marked[j] and s[j] == target:
                    marked[j] = marked[i] = True
                    score += i - j
                    break
        return score


# V0-2
# IDEA : PARTITION BY MIRROR PAIR — ONE STACK PER PAIR, NOT PER LETTER
#
#   the 26 letters split into 13 mirror PAIRS ((a,z), (b,y), ..., (m,n)) and no
#   letter is its own mirror, so indices belonging to different pairs can never
#   interact. the whole process is therefore 13 disjoint two-letter matchings,
#   with every index living in exactly one of them (pair id = min(c, 25 - c)).
#
#   inside one pair a SINGLE stack is enough : a match is taken as soon as the
#   top carries the other letter, so a pair's stack can only ever hold copies
#   of one and the same letter. "is the top my mirror?" then collapses to "is
#   the stack holding the other side?", one bit per pair.
#
# time = O(n), space = O(n)
class Solution(object):
    def calculateScore(self, s):
        pend = [[] for _ in range(13)]     # unmatched indices, per mirror pair
        side = [0] * 13                    # which side of the pair they hold
        score = 0
        for i, ch in enumerate(s):
            c = ord(ch) - 97
            p = c if c < 13 else 25 - c    # pair id 0..12
            cur = 0 if c < 13 else 1       # 0 = low letter, 1 = high letter
            if pend[p] and side[p] != cur:
                score += i - pend[p].pop()
            else:
                side[p] = cur
                pend[p].append(i)
        return score
