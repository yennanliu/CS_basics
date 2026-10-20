"""

937. Reorder Data in Log Files
Medium

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
class Solution(object):
    def reorderLogFiles(self, logs):
        """
        :type logs: List[str]
        :rtype: List[str]
        """
        pass


# V0-1
# IDEA: SORING (gpt)
class Solution(object):
    def reorderLogFiles(self, logs):
        """
        :type logs: List[str]
        :rtype: List[str]
        """
        letter_logs = []
        digit_logs = []

        # Step 1: Separate letter-logs and digit-logs
        for log in logs:
            if self.is_letter_log(log):
                letter_logs.append(log)
            else:
                digit_logs.append(log)

        # Step 2: Sort letter-logs by content, then identifier
        letter_logs.sort(
            key=lambda log: (log.split(" ", 1)[1], log.split(" ", 1)[0])
        )

        # Step 3: Keep digit-logs in original order
        return letter_logs + digit_logs

    def is_letter_log(self, log):
        identifier, content = log.split(" ", 1)
        return content[0].isalpha()


# V0-2
# IDEA: SORING (gemini)
class Solution(object):

  def reorderLogFiles(self, logs):
    """:type logs: List[str]

    :rtype: List[str]
    """
    letters = []
    digits = []

    for log in logs:
      # 將日誌切分為「識別碼 (identifier)」與「內容 (content)」兩部分
      _id, content = log.split(" ", 1)

      # 判斷內容的第一個字元是否為數字
      if content[0].isdigit():
        digits.append(log)
      else:
        letters.append(log)

    # 依照題目要求排序 letter-logs：
    # 優先以內容 (content) 排序；若內容相同，則以識別碼 (identifier) 排序
    letters.sort(key=lambda x: (x.split(" ", 1)[1], x.split(" ", 1)[0]))

    # 字母日誌在前，數字日誌在後（數字日誌維持原本相對順序）
    return letters + digits


# V0-3
# IDEA : SORT BY KEY
# time = O(n log n * l)  # n = len(logs), l = avg length of a log
# space = O(n)
class Solution:
    def reorderLogFiles(self, logs):
        def f(log):
            id_, rest = log.split(" ", 1)
            """
            NOTE !!!
              2 cases:
               1) case 1: rest[0].isalpha() => sort by rest, id_
               2) case 2: rest[0] is digit =>  DO NOTHING (keep original order)

               syntax:
                 if condition:
                    return key1, key2, key3 ....
            """
            if rest[0].isalpha():
                return 0, rest, id_
            else:
                return 1, None, None
                #return 100, None, None  # since we need to put Digit-logs behind of Letter-logs, so first key should be ANY DIGIT BIGGER THAN 0 

        logs.sort(key = lambda x : f(x))
        return logs

# V1
# IDEA : SORT BY keys
# https://leetcode.com/problems/reorder-data-in-log-files/solution/
# time = O(n log n * l)  # n = len(logs), l = avg length of a log
# space = O(n)
class Solution:
    def reorderLogFiles(self, logs):

        def get_key(log):
            _id, rest = log.split(" ", maxsplit=1)
            """
            NOTE !!!
              2 cases:
               1) case 1: rest[0].isalpha() => sort by rest, id_
               2) case 2: rest[0] is digit =>  DO NOTHING (keep original order)
            """
            return (0, rest, _id) if rest[0].isalpha() else (1, )

        return sorted(logs, key=get_key)

# V1'
# https://blog.csdn.net/fuxuemingzhu/article/details/83961188
# IDEA :
# THE NEEDED RETURN FORM :
# sorted alphabet-log + sorted nums-log 
# time = O(n log n * l)  # n = len(logs), l = avg length of a log
# space = O(n)
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

# V1''
# https://leetcode.com/problems/reorder-data-in-log-files/solution/
# IDEA : SORT BY KEY
# time = O(n log n * l)  # n = len(logs), l = avg length of a log
# space = O(n)
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
# time = O(n log n * l)  # n = len(logs), l = avg length of a log
# space = O(n)
class Solution:
    def reorderLogFiles(self, logs):
        def f(log):
            id_, rest = log.split(" ", 1)
            return (0, rest, id_) if rest[0].isalpha() else (1,)

        logs.sort(key = lambda x : f(x))
        return logs #sorted(logs, key = f)

# V2'
# time = O(n log n * l)  # n = len(logs), l = avg length of a log
# space = O(n)
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
# time = O(nlogn * l), n is the length of files, l is the average length of strings
# space = O(l)
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
