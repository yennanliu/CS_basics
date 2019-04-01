

# V1  : dev 

# V2
# https://www.programiz.com/python-programming/methods/string/isalpha
# isalpha()
# The isalpha() method returns True if all characters in the string are alphabets. If not, it returns False.

# https://blog.csdn.net/GQxxxxxl/article/details/83961863

class Solution:
    def reorderLogFiles(self, logs):
        """
        :type logs: List[str]
        :rtype: List[str]
        """
        def f(log):
            id_, rest = log.split(" ", 1)
            return (0, rest, id_) if rest[0].isalpha() else (1,)

        return sorted(logs, key = f)

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
