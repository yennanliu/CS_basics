"""

3518. Smallest Palindromic Rearrangement II
Hard

You are given a palindromic string s and an integer k.

Return the k-th lexicographically smallest palindromic permutation of s. If
there are fewer than k distinct palindromic permutations, return an empty
string.

Note: Different rearrangements that yield the same palindromic string are
considered identical and are counted once.

Example 1:

Input: s = "abba", k = 2

Output: "baab"

Explanation:

The two distinct palindromic rearrangements of "abba" are "abba" and "baab".

Lexicographically, "abba" comes before "baab". Since k = 2, the output is
"baab".

Example 2:

Input: s = "aa", k = 2

Output: ""

Explanation:

There is only one palindromic rearrangement: "aa".

The output is an empty string since k = 2 exceeds the number of possible
rearrangements.

Example 3:

Input: s = "bacab", k = 1

Output: "abcba"

Explanation:

The two distinct palindromic rearrangements of "bacab" are "abcba" and "bacab".

Lexicographically, "abcba" comes before "bacab". Since k = 1, the output is
"abcba".

Constraints:

1 <= s.length <= 10^4

s consists of lowercase English letters.

s is guaranteed to be palindromic.

1 <= k <= 10^6

"""

# V0
# IDEA : ONLY A SHORT SUFFIX OF THE HALF CAN VARY -- FREEZE THE REST, THEN
#        WALK THE K-TH MULTISET PERMUTATION
#
#   a palindrome is fully determined by its first half (plus the forced middle
#   character when the length is odd), and comparing two palindromes is the
#   same as comparing their halves.  so the task is the k-th smallest
#   permutation of the half multiset, which has counts cnt[c] = freq(c) / 2.
#
#   the number of those permutations is astronomically large, but k <= 10^6.
#   that means all k smallest halves share a long common prefix: the smallest
#   half is simply the sorted one, and everything below the k-th differs only
#   inside a *suffix* whose own permutation count already reaches k.
#
#   so grow a "free suffix" by handing it characters starting from 'z'
#   downwards, updating the multinomial count incrementally with
#       count <- count * total / (copies of the letter just added),
#   and stop the moment count >= k.  everything left over is frozen as the
#   sorted prefix -- it is the smallest possible prefix, so the halves carrying
#   it really are the first `count` in lexicographic order.  if the whole half
#   is consumed and count is still below k, fewer than k palindromes exist.
#
#   the free suffix is then filled greedily: the arrangements starting with
#   letter c number count * cnt[c] / total, so either k falls inside that block
#   (fix c) or k skips past it.  both count and k stay bounded by ~k * n.
#
# time = O(26 * n), space = O(n)
class Solution(object):
    def smallestPalindrome(self, s, k):
        n = len(s)
        half = n // 2
        cnt = [0] * 26
        for i in range(half):
            cnt[ord(s[i]) - 97] += 1

        free = [0] * 26          # the tail of the half that is allowed to move
        total = 0
        count = 1                # permutations of `free`
        c = 25
        while count < k and c >= 0:
            while free[c] < cnt[c] and count < k:
                free[c] += 1
                total += 1
                count = count * total // free[c]
            if count < k:
                c -= 1
        if count < k:
            return ""

        out = []
        for j in range(26):      # frozen prefix, smallest possible => sorted
            out.append(chr(97 + j) * (cnt[j] - free[j]))

        while total:             # k-th permutation of the free suffix
            for j in range(26):
                if not free[j]:
                    continue
                block = count * free[j] // total
                if block < k:
                    k -= block
                    continue
                count = block
                free[j] -= 1
                total -= 1
                out.append(chr(97 + j))
                break

        first = "".join(out)
        mid = s[half] if n % 2 else ""
        return first + mid + first[::-1]
