"""

3307. Find the K-th Character in String Game II
Hard

Alice and Bob are playing a game. Initially, Alice has a string word = "a".

You are given a positive integer k. You are also given an integer array operations, where operations[i] represents the type of the ith operation.

Now Bob will ask Alice to perform all operations in sequence:

If operations[i] == 0, append a copy of word to itself.
If operations[i] == 1, generate a new string by changing each character in word to its next character in the English alphabet, and append it to the original word.

Return the value of the kth character in word after performing all the operations.

Note that the character 'z' can be changed to 'a' in the operation.


Example 1:

Input: k = 5, operations = [0,0,0]
Output: "a"
Explanation:
Initially, word == "a". Alice performs the three operations as follows:
Appends "a" to "a", word becomes "aa".
Appends "aa" to "aa", word becomes "aaaa".
Appends "aaaa" to "aaaa", word becomes "aaaaaaaa".

Example 2:

Input: k = 10, operations = [0,1,0,1]
Output: "b"
Explanation:
Initially, word == "a". Alice performs the four operations as follows:
Appends "a" to "a", word becomes "aa".
Appends "bb" to "aa", word becomes "aabb".
Appends "aabb" to "aabb", word becomes "aabbaabb".
Appends "bbccbbcc" to "aabbaabb", word becomes "aabbaabbbbccbbcc".


Constraints:

1 <= k <= 10^14
1 <= operations.length <= 100
operations[i] is either 0 or 1.
The input is generated such that word has at least k characters after all operations.

"""

# V0
# IDEA : WALK THE OPERATIONS BACKWARDS, FOLDING k INTO THE FIRST HALF
#
#   every operation doubles the length, so the round that first covers index
#   k is easy to find : keep the lengths until they reach k, then unwind.
#
#   unwinding one operation : if k lies in the first half nothing happened to
#   it, and if it lies in the second half it is a copy of position k - half,
#   shifted by one letter when the operation was of type 1.
#
#   so the answer is 'a' plus the number of type-1 operations whose second
#   half contained k, taken modulo 26.
#
#   k reaches 10^14, which is why the lengths are capped rather than built.
#
# time = O(len(operations)), space = O(1)
class Solution(object):
    def kthCharacter(self, k, operations):
        lengths = []
        size = 1
        for op in operations:
            size *= 2
            lengths.append(size)
            if size >= k:
                break

        shifts = 0
        pos = k - 1                       # 0-indexed
        for i in range(len(lengths) - 1, -1, -1):
            half = lengths[i] // 2
            if pos >= half:               # came from the appended copy
                pos -= half
                if operations[i] == 1:
                    shifts += 1
        return chr(ord('a') + shifts % 26)
