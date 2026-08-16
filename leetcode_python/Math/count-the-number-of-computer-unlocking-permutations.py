"""

3577. Count the Number of Computer Unlocking Permutations
Medium

You are given an array complexity of length n.

There are n locked computers in a room with labels from 0 to n - 1, each with its own unique password. The password of the computer i has a complexity complexity[i].

The password for the computer labeled 0 is already decrypted and serves as the root. All other computers must be unlocked using it or another previously unlocked computer, following this rule:

You can decrypt the password for the computer j using the password for computer i, where i is any integer less than j with a lower complexity. (i.e. i < j and complexity[i] < complexity[j])

To decrypt the password for computer j, you must have already unlocked a computer i such that i < j and complexity[i] < complexity[j].

Find the number of permutations of [0, 1, 2, ..., (n - 1)] that represent a valid order in which the computers can be unlocked, starting from computer 0.

Since the answer may be large, return it modulo 10^9 + 7.

Note that the password for the computer with label 0 is decrypted, and not the computer with the first index.


Example 1:

Input: complexity = [1,2,3]
Output: 2
Explanation:
The valid permutations are:
[0, 1, 2]
Unlock computer 0 first with root password.
Unlock computer 1 with password of computer 0 since complexity[0] < complexity[1].
Unlock computer 2 with password of computer 0 since complexity[0] < complexity[2].
[0, 2, 1]
Unlock computer 0 first with root password.
Unlock computer 2 with password of computer 0 since complexity[0] < complexity[2].
Unlock computer 1 with password of computer 0 since complexity[0] < complexity[1].

Example 2:

Input: complexity = [3,3,3,4,4,4]
Output: 0
Explanation:
There are no valid permutations. Since computers 1 and 2 can not be unlocked.


Constraints:

2 <= complexity.length <= 10^5
1 <= complexity[i] <= 10^9

"""

# V0
# IDEA : EITHER COMPUTER 0 UNLOCKS EVERYTHING, OR NOTHING WORKS
#
#   computer 0 is the only one that starts unlocked and it has the smallest
#   index, so if complexity[0] is strictly below every other complexity then
#   computer 0 alone is a legal parent for all the rest — the order of the
#   remaining n-1 computers is then completely free, giving (n-1)!.
#
#   otherwise some computer j has complexity[j] <= complexity[0]; every
#   candidate parent i < j must itself be unlocked first and must have an
#   even smaller complexity, which cascades down to a computer that nothing
#   can open. so the count is 0.
#
# time = O(n), space = O(1)
class Solution(object):
    def countPermutations(self, complexity):
        MOD = 10 ** 9 + 7
        n = len(complexity)
        root = complexity[0]
        for i in range(1, n):
            if complexity[i] <= root:
                return 0
        res = 1
        for i in range(1, n):
            res = res * i % MOD
        return res
