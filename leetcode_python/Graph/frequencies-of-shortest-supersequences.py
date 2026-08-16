"""

3435. Frequencies of Shortest Supersequences
Hard

You are given an array of strings words. Find all shortest common supersequences
(SCS) of words that are not permutations of each other.

A shortest common supersequence is a string of minimum length that contains each
string in words as a subsequence.

Return a 2D array of integers freqs that represent all the SCSs. Each freqs[i]
is an array of size 26, representing the frequency of each letter in the
lowercase English alphabet for a single SCS. You may return the frequency arrays
in any order.

Example 1:

Input: words = ["ab","ba"]

Output: [[1,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],[2,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]]

Explanation:

The two SCSs are "aba" and "bab". The output is the letter frequencies for each
one.

Example 2:

Input: words = ["aa","ac"]

Output: [[2,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]]

Explanation:

The two SCSs are "aac" and "aca". Since they are permutations of each other,
keep only "aac".

Example 3:

Input: words = ["aa","bb","cc"]

Output: [[2,2,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]]

Explanation:

"aabbcc" and all its permutations are SCSs.

Constraints:

1 <= words.length <= 256
words[i].length == 2
All strings in words will altogether be composed of no more than 16 unique
lowercase letters.
All strings in words are unique.

"""

# V0
# IDEA : THE SCS DUPLICATES EXACTLY A "FEEDBACK VERTEX SET" OF THE ORDER GRAPH
#
#   every word is two letters "ab", i.e. the demand "some a comes before some
#   b".  read them as directed edges a -> b (a word "aa" is a self-loop).
#
#   claim: a letter never needs more than two copies, and if we decide that the
#   set S of letters gets two copies while the rest get one, a valid
#   supersequence of length |V| + |S| exists iff the graph induced on V \ S is
#   acyclic.  the construction is: one copy of every S-letter up front, then the
#   V \ S letters in topological order, then the second copy of every S-letter.
#   any edge that touches S is satisfied by the front or back copy; edges inside
#   V \ S are satisfied by the topological order.  conversely a cycle among
#   single-copy letters is unsatisfiable, since the demands would force a strict
#   circular ordering of distinct positions.
#
#   so we want every *minimum size* S whose complement is acyclic — a minimum
#   feedback vertex set.  with at most 16 letters that is a subset DP:
#     acyclic[m] = some vertex of m has no incoming edge from inside m, and
#                  removing it leaves an acyclic set.
#   self-loops fall out for free — such a vertex is never a source, so any m
#   containing it is reported cyclic and it is forced into S.
#
# time = O(2^V * V) with V <= 16, space = O(2^V)
class Solution(object):
    def supersequences(self, words):
        letters = sorted(set(c for w in words for c in w))
        idx = {c: i for i, c in enumerate(letters)}
        V = len(letters)
        full = (1 << V) - 1

        inc = [0] * V           # inc[v] = bitmask of u with an edge u -> v
        for w in words:
            u, v = idx[w[0]], idx[w[1]]
            inc[v] |= 1 << u

        acyclic = [False] * (full + 1)
        acyclic[0] = True
        for m in range(1, full + 1):
            mm = m
            while mm:
                b = mm & (-mm)
                i = b.bit_length() - 1
                mm ^= b
                if not (inc[i] & m) and acyclic[m ^ b]:
                    acyclic[m] = True
                    break

        bestBits = V + 1
        chosen = []
        for s in range(full + 1):
            bits = bin(s).count('1')
            if bits > bestBits:
                continue
            if acyclic[full ^ s]:
                if bits < bestBits:
                    bestBits = bits
                    chosen = [s]
                else:
                    chosen.append(s)

        res = []
        for s in chosen:
            freq = [0] * 26
            for i, c in enumerate(letters):
                freq[ord(c) - 97] = 2 if (s >> i) & 1 else 1
            res.append(freq)
        return res
