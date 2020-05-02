# Time:  O(n)
# Space: O(1)

# Compare two version numbers version1 and version1.
# If version1 > version2 return 1, if version1 < version2
# return -1, otherwise return 0.
#
# You may assume that the version strings are non-empty and
# contain only digits and the . character.
# The . character does not represent a decimal point and
# is used to separate number sequences.
# For instance, 2.5 is not "two and a half" or "half way to
# version three", it is the fifth second-level revision of
# the second first-level revision.
#
# Here is an example of version numbers ordering:
#
# 0.1 < 1.1 < 1.2 < 13.37
#

# V0 
# IDEA : STRING
class Solution(object):
    def compareVersion(self, version1, version2):
        v1_split = version1.split('.')
        v2_split = version2.split('.')
        v1_len, v2_len = len(v1_split), len(v2_split)
        maxLen = max(v1_len, v2_len)
        for i in range(maxLen):
            temp1, temp2 = 0, 0
            if i < v1_len:
                temp1 = int(v1_split[i])
            if i < v2_len:
                temp2 = int(v2_split[i])
            if temp1 < temp2:
                return -1
            elif temp1 > temp2:
                return 1
        return 0

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/80821268
# IDEA : STRING
class Solution(object):
    def compareVersion(self, version1, version2):
        """
        :type version1: str
        :type version2: str
        :rtype: int
        """
        v1_split = version1.split('.')
        v2_split = version2.split('.')
        v1_len, v2_len = len(v1_split), len(v2_split)
        maxLen = max(v1_len, v2_len)
        for i in range(maxLen):
            temp1, temp2 = 0, 0
            if i < v1_len:
                temp1 = int(v1_split[i])
            if i < v2_len:
                temp2 = int(v2_split[i])
            if temp1 < temp2:
                return -1
            elif temp1 > temp2:
                return 1
        return 0
      
### Test case
s=Solution()
assert s.compareVersion("1.10", "1.9") == 1 
assert s.compareVersion("1.10", "1.10.10") == -1 
assert s.compareVersion("1.10.1.2", "1.10.1.3") == -1
assert s.compareVersion("1.10", "1.10") == 0
assert s.compareVersion("1.10.1", "1.10.1.1") == -1 
assert s.compareVersion("0", "0") == 0 
assert s.compareVersion("7.1", "7.12") == -1
assert s.compareVersion("0.0.1", "0") == 1
assert s.compareVersion("0.1", "0.2") == -1

# V2 
class Solution(object):
    def compareVersion(self, version1, version2):
        version1_ = list(version1.split('.'))
        version2_ = list(version2.split('.'))
        len_ = max(len(version1_), len(version2_))
        version1_ = version1_ + (len_ - len(version1_))*['0']
        version2_ = version2_ + (len_ - len(version2_))*['0']
        for i in range(len(version1_)):
            if int(version1_[i]) > int(version2_[i]):
                return 1 
            elif int(version1_[i]) < int(version2_[i]) :
                return -1 
            else:
                pass
        return 0 

# V3 
import itertools
class Solution(object):
    def compareVersion(self, version1, version2):
        """
        :type version1: str
        :type version2: str
        :rtype: int
        """
        n1, n2 = len(version1), len(version2)
        i, j = 0, 0
        while i < n1 or j < n2:
            v1, v2 = 0, 0
            while i < n1 and version1[i] != '.':
                v1 = v1 * 10 + int(version1[i])
                i += 1
            while j < n2 and version2[j] != '.':
                v2 = v2 * 10 + int(version2[j])
                j += 1
            if v1 != v2:
                return 1 if v1 > v2 else -1
            i += 1
            j += 1

        return 0

# Time:  O(n)
# Space: O(n)
# V4 
def cmp(a, b):
    return (a > b) - (a < b) 
    
class Solution2(object):
    def compareVersion(self, version1, version2):
        """
        :type version1: str
        :type version2: str
        :rtype: int
        """
        v1, v2 = version1.split("."), version2.split(".")

        if len(v1) > len(v2):
            v2 += ['0' for _ in range(len(v1) - len(v2))]
        elif len(v1) < len(v2):
            v1 += ['0' for _ in range(len(v2) - len(v1))]

        i = 0
        while i < len(v1):
            if int(v1[i]) > int(v2[i]):
                return 1
            elif int(v1[i]) < int(v2[i]):
                return -1
            else:
                i += 1

        return 0

    def compareVersion2(self, version1, version2):
        """
        :type version1: str
        :type version2: str
        :rtype: int
        """
        v1 = [int(x) for x in version1.split('.')]
        v2 = [int(x) for x in version2.split('.')]
        while len(v1) != len(v2):
            if len(v1) > len(v2):
                v2.append(0)
            else:
                v1.append(0)
        return cmp(v1, v2)

    def compareVersion3(self, version1, version2):
        splits = (list(map(int, v.split('.'))) for v in (version1, version2))
        return cmp(*list(zip(*itertools.zip_longest(*splits, fillvalue=0))))

    def compareVersion4(self, version1, version2):
        main1, _, rest1 = ('0' + version1).partition('.')
        main2, _, rest2 = ('0' + version2).partition('.')
        return cmp(int(main1), int(main2)) or len(rest1 + rest2) and self.compareVersion4(rest1, rest2)

#V5 
# Time:  O(n)
# Space: O(1)
import itertools
class Solution(object):
    def compareVersion(self, version1, version2):
        """
        :type version1: str
        :type version2: str
        :rtype: int
        """
        n1, n2 = len(version1), len(version2)
        i, j = 0, 0
        while i < n1 or j < n2:
            v1, v2 = 0, 0
            while i < n1 and version1[i] != '.':
                v1 = v1 * 10 + int(version1[i])
                i += 1
            while j < n2 and version2[j] != '.':
                v2 = v2 * 10 + int(version2[j])
                j += 1
            if v1 != v2:
                return 1 if v1 > v2 else -1
            i += 1
            j += 1

        return 0

# Time:  O(n)
# Space: O(n)
class Solution2(object):
    def compareVersion(self, version1, version2):
        """
        :type version1: str
        :type version2: str
        :rtype: int
        """
        v1, v2 = version1.split("."), version2.split(".")

        if len(v1) > len(v2):
            v2 += ['0' for _ in range(len(v1) - len(v2))]
        elif len(v1) < len(v2):
            v1 += ['0' for _ in range(len(v2) - len(v1))]

        i = 0
        while i < len(v1):
            if int(v1[i]) > int(v2[i]):
                return 1
            elif int(v1[i]) < int(v2[i]):
                return -1
            else:
                i += 1

        return 0

    def compareVersion2(self, version1, version2):
        """
        :type version1: str
        :type version2: str
        :rtype: int
        """
        v1 = [int(x) for x in version1.split('.')]
        v2 = [int(x) for x in version2.split('.')]
        while len(v1) != len(v2):
            if len(v1) > len(v2):
                v2.append(0)
            else:
                v1.append(0)
        return cmp(v1, v2)

    def compareVersion3(self, version1, version2):
        splits = (map(int, v.split('.')) for v in (version1, version2))
        return cmp(*zip(*itertools.izip_longest(*splits, fillvalue=0)))

    def compareVersion4(self, version1, version2):
        main1, _, rest1 = ('0' + version1).partition('.')
        main2, _, rest2 = ('0' + version2).partition('.')
        return cmp(int(main1), int(main2)) or len(rest1 + rest2) and self.compareVersion4(rest1, rest2)

