"""

567. Permutation in String
Medium

Share
Given two strings s1 and s2, return true if s2 contains a permutation of s1, or false otherwise.

In other words, return true if one of s1's permutations is the substring of s2.

 
Example 1:

Input: s1 = "ab", s2 = "eidbaooo"
Output: true
Explanation: s2 contains one permutation of s1 ("ba").
Example 2:

Input: s1 = "ab", s2 = "eidboaoo"
Output: false
 

Constraints:

1 <= s1.length, s2.length <= 104
s1 and s2 consist of lowercase English letters.

"""

# V0
# IDEA: HASHMAP + SLIDE WINDOW
from collections import Counter

class Solution(object):
    def checkInclusion(self, s1, s2):
        if len(s1) > len(s2):
            return False

        cnt_map_1 = Counter(s1)
        cnt_map_2 = Counter()

        l = 0

        for r in range(len(s2)):
            # NOTE !!!
            # we `expand` the right boundary ANYWAY !!
            # e.g. NO matter if s2[r] is in cnt_map_1 or not
            #      -> we will validate above later
            #      -> in the `left boundary` shrink
            #      -> e.g. `while r - l + 1 > len(s1):` ...
            cnt_map_2[s2[r]] += 1

            # keep window size <= len(s1)
            while r - l + 1 > len(s1):
                cnt_map_2[s2[l]] -= 1

                if cnt_map_2[s2[l]] == 0:
                    del cnt_map_2[s2[l]]

                l += 1

            # NOTE !!! how we check if the permutations sub str is found
            # window size == len(s1)
            if cnt_map_2 == cnt_map_1:
                return True

        return False


# V0-1
# IDEA: HASHMAP + SLIDE WINDOW
from collections import Counter

class Solution(object):
    def checkInclusion(self, s1, s2):
        """
        :type s1: str
        :type s2: str
        :rtype: bool
        """
        # Edge Case: If s1 is longer than s2, s2 cannot contain a permutation of s1.
        if len(s1) > len(s2):
            return False
            
        # Frequency map for the target string we are looking for
        cnt_map_1 = Counter(s1)
        
        # Dynamic frequency map tracking our current sliding window in s2
        cnt_map_2 = Counter()
        
        l = 0
        for r in range(len(s2)):
            # 1. Expand the window 
            # by adding the incoming character on the right
            cnt_map_2[s2[r]] += 1
            
            # 2. Maintain fixed window size: If the window length exceeds len(s1),
            # pop the leftmost character out of our count map.
            if (r - l + 1) > len(s1):
                left_char = s2[l]
                cnt_map_2[left_char] -= 1
                
                # Clean up zero counts so map comparisons (==) match perfectly
                if cnt_map_2[left_char] == 0:
                    del cnt_map_2[left_char]
                    
                l += 1 # Slide the left pointer forward
                
            # 3. Match Check: Because we maintain an exact window size of len(s1), 
            # if the character frequencies match perfectly, a permutation is found!
            if cnt_map_2 == cnt_map_1:
                return True
                
        return False


# V0
# IDEA : SORTRD + ARRAY INDEX
class Solution(object):
    def checkInclusion(self, s1, s2):
        n1 = len(s1)
        n2 = len(s2)

        if n1 > n2:
            return False

        """
        NOTE !!!
        """
        target = sorted(s1)

        for l in range(n2 - n1 + 1):
            """
            NOTE !!!
            """
            if sorted(s2[l:l + n1]) == target:
                return True

        return False


# V0-1
# IDEA :  sliding window + HASHMAP
# https://github.com/yennanliu/CS_basics/blob/master/leetcode_java/src/main/java/LeetCodeJava/TwoPointer/PermutationInString.java#L48
class Solution(object):
    def checkInclusion(self, s1, s2):
        # edge
        if not s1 or not s2 or len(s1) > len(s2):
            return False

        if s1 == s2:
            return True

        # build frequency map for s1
        s1_map = {}
        for c in s1:
            s1_map[c] = s1_map.get(c, 0) + 1

        window_size = len(s1)

        # slide over s2
        for i in range(len(s2) - window_size + 1):
            window = s2[i:i + window_size]

            window_map = {}
            for c in window:
                window_map[c] = window_map.get(c, 0) + 1

            if window_map == s1_map:
                return True

        return False


# V0-2
# IDEA :  sliding window
class Solution(object):
    def checkInclusion(self, s1, s2):
        if len(s1) > len(s2):
            return False

        count1 = [0] * 26
        count2 = [0] * 26

        for i in range(len(s1)):
            count1[ord(s1[i]) - ord('a')] += 1
            count2[ord(s2[i]) - ord('a')] += 1

        if count1 == count2:
            return True

        l = 0
        for r in range(len(s1), len(s2)):
            count2[ord(s2[r]) - ord('a')] += 1
            count2[ord(s2[l]) - ord('a')] -= 1
            l += 1

            if count1 == count2:
                return True

        return False

# V0-3
class Solution(object):
    def checkInclusion(self, s1, s2):
        n1 = len(s1)
        n2 = len(s2)

        if n1 > n2:
            return False

        target = sorted(s1)

        l = 0  # FIX: initialize l

        for r in range(n2):
            # shrink window if it exceeds size n1
            while r - l + 1 > n1:
                l += 1

            # only check when window size matches n1
            if r - l + 1 == n1:
                if sorted(s2[l:r + 1]) == target:
                    return True

        return False


# V0
# IDEA : collections + sliding window
from collections import Counter
class Solution(object):
    def checkInclusion(self, s1, s2):
        if len(s1) > len(s2):
            return False   
        l = 0
        tmp = ""
        _s1 = Counter(s1)
        _s2 = Counter()     
        for i, item in enumerate(s2):
            ### NOTE : we need to append new element first, then compare
            _s2[item] += 1
            tmp = s2[l:i+1]
            if _s2 == _s1 and len(tmp) > 0:
                return True
            if len(tmp) >= len(s1):
                _s2[tmp[0]] -= 1
                if _s2[tmp[0]] == 0:
                    del _s2[tmp[0]]
                l += 1
        return False

# V0'
# IDEA : collections + sliding window
class Solution(object):
    def checkInclusion(self, s1, s2):
        if len(s1) > len(s2):
            return False
        _c1 = Counter(s1)
        _c2 = Counter([])
        l = r = 0
        while r < len(s2):
            while r - l + 1 <= len(s1):
                ### NOTE : we need to append new element, then compare
                _c2[s2[r]] += 1
                #print ("l = " + str(l) + " r = " + str(r) + " s2[l:r+1] = " + str(s2[l:r+1]) + " _c2 = " + str(_c2))
                if _c1 == _c2:
                    return True
                r += 1
            _c2[s2[l]] -= 1
            if _c2[s2[l]] == 0:
                _c2.pop(s2[l])
            l += 1
        return False

# V0
# IDEA : collections + sliding window
import collections
class Solution(object):
    def checkInclusion(self, s1, s2):
        l1, l2 = len(s1), len(s2)
        c1 = collections.Counter(s1)
        c2 = collections.Counter()
        p = q = 0
        while q < l2:
            c2[s2[q]] += 1
            if c1 == c2:
                return True
            q += 1
            if q - p + 1 > l1:
                c2[s2[p]] -= 1
                if c2[s2[p]] == 0:
                    del c2[s2[p]]
                p += 1
        return False

# V0' NEED TO FIX 
# import collections
# class Solution(object):
#     def checkInclusion(self, s1, s2):
#         """
#         :type s1: str
#         :type s2: str
#         :rtype: bool
#         """
#         l1, l2 = len(s1), len(s2)
#         c1 = collections.Counter(s1)
#         c2 = collections.Counter()
#         l, r = 0, l+len(s1)
#         for i in range(len(s2) - len(s1)):
#             c2[s2[i]] += 1 
#             if c1 == c2:
#                 return True
#             if r - l + 1 > l1:
#                 c2[s2[l]] -= 1
#                 if c2[s2[l]] == 0:
#                     del c2[s2[l]]
#             l += 1
#             r += 1 
#         return False

# V1
# http://bookshadow.com/weblog/2017/04/30/leetcode-permutation-in-string/
import collections
class Solution(object):
    def checkInclusion(self, s1, s2):
        """
        :type s1: str
        :type s2: str
        :rtype: bool
        """
        l1, l2 = len(s1), len(s2)
        c1 = collections.Counter(s1)
        c2 = collections.Counter()
        p = q = 0
        while q < l2:
            c2[s2[q]] += 1
            if c1 == c2:
                return True
            q += 1
            if q - p + 1 > l1:
                c2[s2[p]] -= 1
                if c2[s2[p]] == 0:
                    del c2[s2[p]]
                p += 1
        return False

# V1'
# http://bookshadow.com/weblog/2017/04/30/leetcode-permutation-in-string/
import collections
class Solution(object):
    def checkInclusion(self, s1, s2):
        """
        :type s1: str
        :type s2: str
        :rtype: bool
        """
        l1, l2 = len(s1), len(s2)
        c1 = collections.Counter(s1)
        c2 = collections.Counter()
        cnt = 0
        p = q = 0
        while q < l2:
            c2[s2[q]] += 1
            if c1[s2[q]] == c2[s2[q]]:
                cnt += 1
            if cnt == len(c1):
                return True
            q += 1
            if q - p + 1 > l1:
                if c1[s2[p]] == c2[s2[p]]:
                    cnt -= 1
                c2[s2[p]] -= 1
                if c2[s2[p]] == 0:
                    del c2[s2[p]]
                p += 1
        return False

# V1'
# https://www.jiuzhang.com/solution/permutation-in-string/#tag-highlight-lang-python
import collections
class Solution:
    def checkInclusion(self, s1, s2):
        """
        :type s1: str
        :type s2: str
        :rtype: bool
        """
        l1 = len(s1)
        need = collections.Counter(s1)
        missing = l1
        for i,c in enumerate(s2):
            if c in need: 
                if need[c] > 0: missing -= 1    
                need[c] -= 1                    
            if i>=l1 and s2[i-l1] in need:      
                need[s2[i-l1]] += 1            
                if need[s2[i-l1]]>0: missing += 1  
            if missing == 0:
                return True
        return False

# V2
# Time:  O(n)
# Space: O(1)
import collections
class Solution(object):
    def checkInclusion(self, s1, s2):
        """
        :type s1: str
        :type s2: str
        :rtype: bool
        """
        counts = collections.Counter(s1)
        l = len(s1)
        for i in range(len(s2)):
            if counts[s2[i]] > 0:
                l -= 1
            counts[s2[i]] -= 1
            if l == 0:
                return True
            start = i + 1 - len(s1)
            if start >= 0:
                counts[s2[start]] += 1
                if counts[s2[start]] > 0:
                    l += 1