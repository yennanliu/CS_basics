"""

3295. Report Spam Message
Medium

You are given an array of strings message and an array of strings bannedWords.

An array of words is considered spam if there are at least two words in it that exactly match any word in bannedWords.

Return true if the array message is spam, and false otherwise.


Example 1:

Input: message = ["hello","world","leetcode"], bannedWords = ["world","hello"]
Output: true
Explanation:
The words "hello" and "world" from the message array both appear in the bannedWords array.

Example 2:

Input: message = ["hello","programming","fun"], bannedWords = ["world","programming","leetcode"]
Output: false
Explanation:
Only one word from the message array ("programming") appears in the bannedWords array.


Constraints:

1 <= message.length, bannedWords.length <= 10^5
1 <= message[i].length, bannedWords[i].length <= 15
message[i] and bannedWords[i] consist only of lowercase English letters.

"""

# V0
# IDEA : HASH THE BANNED LIST, THEN COUNT HITS AND STOP AT TWO
#
#   a set turns each membership test into O(1) — scanning the banned array
#   per word would be 10^10 comparisons.
#
#   the answer only needs to know whether the count REACHES two, so the scan
#   returns as soon as it does.
#
# time = O(n + m), space = O(m)
class Solution(object):
    def reportSpam(self, message, bannedWords):
        banned = set(bannedWords)
        hits = 0
        for w in message:
            if w in banned:
                hits += 1
                if hits == 2:
                    return True
        return False
