"""

165. Compare Version Numbers
Medium


Given two version numbers, version1 and version2, compare them.

Version numbers consist of one or more revisions joined by a dot '.'. Each revision consists of digits and may contain leading zeros. Every revision contains at least one character. Revisions are 0-indexed from left to right, with the leftmost revision being revision 0, the next revision being revision 1, and so on. For example 2.5.33 and 0.1 are valid version numbers.

To compare version numbers, compare their revisions in left-to-right order. Revisions are compared using their integer value ignoring any leading zeros. This means that revisions 1 and 001 are considered equal. If a version number does not specify a revision at an index, then treat the revision as 0. For example, version 1.0 is less than version 1.1 because their revision 0s are the same, but their revision 1s are 0 and 1 respectively, and 0 < 1.

Return the following:

If version1 < version2, return -1.
If version1 > version2, return 1.
Otherwise, return 0.
 

Example 1:

Input: version1 = "1.01", version2 = "1.001"
Output: 0
Explanation: Ignoring leading zeroes, both "01" and "001" represent the same integer "1".
Example 2:

Input: version1 = "1.0", version2 = "1.0.0"
Output: 0
Explanation: version1 does not specify revision 2, which means it is treated as "0".
Example 3:

Input: version1 = "0.1", version2 = "1.1"
Output: -1
Explanation: version1's revision 0 is "0", while version2's revision 0 is "1". 0 < 1, so version1 < version2.
 

Constraints:

1 <= version1.length, version2.length <= 500
version1 and version2 only contain digits and '.'.
version1 and version2 are valid version numbers.
All the given revisions in version1 and version2 can be stored in a 32-bit integer.

"""

# V0
# IDEA : STRING + while op
class Solution(object):
    def compareVersion(self, version1, version2):
        # edge case
        if not version1 and not version2:
            return
        # split by "." as list
        v_1 = version1.split(".")
        v_2 = version2.split(".")
        # compare
        while v_1 and v_2:
            tmp1 = int(v_1.pop(0))
            tmp2 = int(v_2.pop(0))

            if tmp1 > tmp2:
                return 1
            elif tmp1 < tmp2:
                return -1
        # if v_1 remains
        if v_1:
            while v_1:
                tmp1 = int(v_1.pop(0))
                if tmp1 != 0:
                    return 1
        # if v_2 remains
        if v_2:
            while v_2:
                tmp2 = int(v_2.pop(0))
                if tmp2 != 0:
                    return -1
        return 0

# V0'
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


# V1
# IDEA : Split + Parse, Two Pass
# https://leetcode.com/problems/compare-version-numbers/solution/
class Solution:
    def compareVersion(self, version1: str, version2: str) -> int:
        nums1 = version1.split('.')
        nums2 = version2.split('.')
        n1, n2 = len(nums1), len(nums2)
        
        # compare versions
        for i in range(max(n1, n2)):
            i1 = int(nums1[i]) if i < n1 else 0
            i2 = int(nums2[i]) if i < n2 else 0
            if i1 != i2:
                return 1 if i1 > i2 else -1
        
        # the versions are equal
        return 0 

# V1
# IDEA : Two Pointers, One Pass
# https://leetcode.com/problems/compare-version-numbers/solution/
class Solution:
    def get_next_chunk(self, version: str, n: int, p: int) -> List[int]:
        # if pointer is set to the end of string
        # return 0
        if p > n - 1:
            return 0, p
        
        # find the end of chunk
        p_end = p
        while p_end < n and version[p_end] != '.':
            p_end += 1
        # retrieve the chunk
        i = int(version[p:p_end]) if p_end != n - 1 else int(version[p:n])
        # find the beginning of next chunk
        p = p_end + 1
        
        return i, p
        
    def compareVersion(self, version1: str, version2: str) -> int:
        p1 = p2 = 0
        n1, n2 = len(version1), len(version2)
        
        # compare versions
        while p1 < n1 or p2 < n2:
            i1, p1 = self.get_next_chunk(version1, n1, p1)
            i2, p2 = self.get_next_chunk(version2, n2, p2)            
            if i1 != i2:
                return 1 if i1 > i2 else -1
        
        # the versions are equal
        return 0    

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

# V5 
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