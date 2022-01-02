"""

937. Reorder Data in Log Files
Easy

You are given an array of logs. Each log is a space-delimited string of words, where the first word is the identifier.

There are two types of logs:

Letter-logs: All words (except the identifier) consist of lowercase English letters.
Digit-logs: All words (except the identifier) consist of digits.
Reorder these logs so that:

The letter-logs come before all digit-logs.
The letter-logs are sorted lexicographically by their contents. If their contents are the same, then sort them lexicographically by their identifiers.
The digit-logs maintain their relative ordering.
Return the final order of the logs.

 

Example 1:

Input: logs = ["dig1 8 1 5 1","let1 art can","dig2 3 6","let2 own kit dig","let3 art zero"]
Output: ["let1 art can","let3 art zero","let2 own kit dig","dig1 8 1 5 1","dig2 3 6"]
Explanation:
The letter-log contents are all different, so their ordering is "art can", "art zero", "own kit dig".
The digit-logs have a relative order of "dig1 8 1 5 1", "dig2 3 6".
Example 2:

Input: logs = ["a1 9 2 3 1","g1 act car","zo4 4 7","ab1 off key dog","a8 act zoo"]
Output: ["g1 act car","a8 act zoo","ab1 off key dog","a1 9 2 3 1","zo4 4 7"]
 

Constraints:

1 <= logs.length <= 100
3 <= logs[i].length <= 100
All the tokens of logs[i] are separated by a single space.
logs[i] is guaranteed to have an identifier and at least one word after the identifier.

"""

# V0
# IDEA : SORT BY KEY
class Solution:
    def reorderLogFiles(self, logs):
        def f(log):
            id_, rest = log.split(" ", 1)
            if rest[0].isalpha():
                return 0, rest, id_
            else:
                return 1, None, None

        logs.sort(key = lambda x : f(x))
        return logs

# V0'
# IDEA : SORT BY KEY
class Solution:
    def reorderLogFiles(self, logs):
        def f(log):
            id_, rest = log.split(" ", 1)
            return (0, rest, id_) if rest[0].isalpha() else (1,)

        logs.sort(key = lambda x : f(x))
        return logs

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/83961188
# IDEA :
# THE NEEDED RETURN FORM :
# sorted alphabet-log + sorted nums-log 
class Solution(object):
    def reorderLogFiles(self, logs):
        letters = []
        nums = []
        for log in logs:
            logsplit = log.split(" ")
            if logsplit[1].isalpha():
                letters.append((" ".join(logsplit[1:]), logsplit[0]))
            else:
                nums.append(log)
        letters.sort()
        return [letter[1] + " " + letter[0] for letter in letters] + nums

# V1'
# https://leetcode.com/problems/reorder-data-in-log-files/solution/
# IDEA : SORT BY KEY
class Solution:
    def reorderLogFiles(self, logs: List[str]) -> List[str]:

        def get_key(log):
            _id, rest = log.split(" ", maxsplit=1)
            return (0, rest, _id) if rest[0].isalpha() else (1, )

        return sorted(logs, key=get_key)

# V2
# https://www.programiz.com/python-programming/methods/string/isalpha
# isalpha()
# The isalpha() method returns True if all characters in the string are alphabets. If not, it returns False.
# https://blog.csdn.net/GQxxxxxl/article/details/83961863
class Solution:
    def reorderLogFiles(self, logs):
        def f(log):
            id_, rest = log.split(" ", 1)
            return (0, rest, id_) if rest[0].isalpha() else (1,)

        logs.sort(key = lambda x : f(x))
        return logs #sorted(logs, key = f)

# V2'
class Solution:
    def reorderLogFiles(self, logs):
        """
        :type logs: List[str]
        :rtype: List[str]
        """
        def f(log):
            id_, rest = log.split(" ", 1)[0], log.split(" ", 1)[1:][0]
            return (0, rest, id_) if rest[0].isalpha() else (1,)

        return sorted(logs, key = f)

# V3 
# Time:  O(nlogn * l), n is the length of files, l is the average length of strings
# Space: O(l)
class Solution(object):
    def reorderLogFiles(self, logs):
        """
        :type logs: List[str]
        :rtype: List[str]
        """
        def f(log):
            i, content = log.split(" ", 1)
            return (0, content, i) if content[0].isalpha() else (1,)

        logs.sort(key=f)
        return logs