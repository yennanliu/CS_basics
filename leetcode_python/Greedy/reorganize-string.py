"""

767. Reorganize String
Medium

Given a string s, rearrange the characters of s so that any two adjacent characters are not the same.

Return any possible rearrangement of s or return "" if not possible.

 

Example 1:

Input: s = "aab"
Output: "aba"
Example 2:

Input: s = "aaab"
Output: ""
 

Constraints:

1 <= s.length <= 500
s consists of lowercase English letters.

"""

# V0 
# IDEA : GREEDY + COUNTER
# IDEA : 
#  step 1) order exists count (big -> small)
#  step 2) select the element which is "most remaining" and DIFFERENT from last ans element and append such element to the end of ans
#  step 3) if can't find such element, return ""
class Solution(object):
    def reorganizeString(self, S):
        cnt = collections.Counter(S)
        # Be aware of it : ans = "#" -> not to have error in ans[-1] when first loop
        ans = '#'
        while cnt:
            stop = True
            for v, c in cnt.most_common():
                """
                NOTE !!! trick here

                1) we only compare last element in ans and current key (v), if they are different, then append
                2) we break at the end of each for loop -> MAKE SURE two adjacent characters are not the same.
                3) we use a flag "stop", to decide whether should stop while loop or not
                """
                if v != ans[-1]:
                    stop = False
                    ans += v
                    cnt[v] -= 1
                    if not cnt[v]:
                        del cnt[v]
                    """
                    NOTE !!!
                     -> we BREAK right after each op, since we want to get next NEW most common element from "updated" cnt.most_common()
                    """
                    break
            # Be aware of it : if there is no valid "v", then the while loop will break automatically at this condition (stop = True)
            if stop:
                break
        return ans[1:] if len(ans[1:]) == len(S) else ''

# V0'
# IDEA : GREEDY + COUNTER
# IDEA : 
#  step 1) order exists count (big -> small)
#  step 2) select the element which is "most remaining" and DIFFERENT from last ans element and append such element to the end of ans
#  step 3) if can't find such element, return ""
from collections import Counter
class Solution(object):
    def reorganizeString(self, s):
        # edge case
        if not s:
            return ""

        cnt = Counter(s)
        # we init ans as "#" for compare its last element with key    
        ans = "#"

        while cnt:
            stop = True
            for k, _count in cnt.most_common():
                """
                NOTE !!! trick here

                1) we only compare last element in ans and current key (v), if they are different, then append
                2) we break at the end of each for loop -> MAKE SURE two adjacent characters are not the same.
                3) we use a flag "stop", to decide whether should stop while loop or not
                """
                if k != ans[-1]:
                    stop = False
                    ans += k
                    cnt[k] -= 1
                    if cnt[k] == 0:
                        del cnt[k]
                    break
            if stop:
                break
        # len(ans) - 1 == len(s), since ans init with "#", we need to substract 1 on length comparison
        return ans[1:] if (len(ans) - 1 == len(s)) else ""

# V0''
class Solution(object):
     def reorganizeString(self, S):
        count = collections.Counter(S)
        c_max, f_max = count.most_common(1)[0]
        if 2 * f_max - 1 > len(S):
            return ''
        count.pop(c_max)
        res = len(S) * ['']
        res[:2*f_max:2] = f_max * [c_max]
        i = 2 * f_max
        for c in count:
            for _ in range(count[c]):
                if i >= len(S):
                    i = 1
                res[i] = c
                i += 2
        return ''.join(res)

# V1
# IDEA : 
#   -> 767. Reorganize String
#   -> 621. Task Scheduler
#   -> 358. Rearrange String k Distance Apart
#   -> Imagine that there are two columns needed to be filled: even-indexed and odd-indexed column,
#   -> fill the most common chars into the even-indexed column if possible,
#   -> fill the remaining chars into the even-indexed column, then odd-indexed column.
# https://leetcode.com/problems/reorganize-string/discuss/442993/Python-greedy-solution-(24ms-beat-98.17)
class Solution(object):
     def reorganizeString(self, S):
        count = collections.Counter(S)
        c_max, f_max = count.most_common(1)[0]
        if 2 * f_max - 1 > len(S):
            return ''
        count.pop(c_max)
        res = len(S) * ['']
        res[:2*f_max:2] = f_max * [c_max]
        i = 2 * f_max
        for c in count:
            for _ in range(count[c]):
                if i >= len(S):
                    i = 1
                res[i] = c
                i += 2
        return ''.join(res)

# V1 
# http://bookshadow.com/weblog/2018/01/21/leetcode-reorganize-string/
# IDEA : GREEDY
# IDEA : 
#  step 1) order exists count (big -> small)
#  step 2) select the element which is "most remaining" and DIFFERENT from last ans element and append such element to the end of ans
#  step 3) if can't find such element, return ""
class Solution(object):
    def reorganizeString(self, S):
        """
        :type S: str
        :rtype: str
        """
        cnt = collections.Counter(S)
        ans = '#'
        while cnt:
            stop = True
            for v, c in cnt.most_common():
                if v != ans[-1]:
                    stop = False
                    ans += v
                    cnt[v] -= 1
                    if not cnt[v]: del cnt[v]
                    break
            if stop: break
        return ans[1:] if len(ans) == len(S) + 1 else ''

# V1'
# https://blog.csdn.net/fuxuemingzhu/article/details/80680454
class Solution(object):
    def reorganizeString(self, S):
        """
        :type S: str
        :rtype: str
        """
        counter = collections.Counter(S)
        ans = "#"
        while counter:
            stop = True
            for item, times in counter.most_common():
                if ans[-1] != item:
                    ans += item
                    counter[item] -= 1
                    if not counter[item]:
                        del counter[item]
                    stop = False
                    break
            if stop: break
        return ans[1:] if len(ans) == len(S) + 1 else ""

# V1''
# https://blog.csdn.net/fuxuemingzhu/article/details/80680454
class Solution(object):
    def reorganizeString(self, S):
        """
        :type S: str
        :rtype: str
        """
        _len = len(S)
        count = collections.Counter(S)
        que = [(-v, c) for c, v in count.items()]
        heapq.heapify(que)
        res = ""
        while _len:
            cnt = min(_len, 2)
            temp = list()
            for i in range(cnt):
                if not que:
                    return ""
                v, c = heapq.heappop(que)
                res += c
                if v + 1 != 0:
                    temp.append((v + 1, c))
                _len -= 1
            for x in temp:
                heapq.heappush(que, x)
        return res

# V1''
# https://www.jiuzhang.com/solution/reorganize-string/#tag-highlight-lang-python
class Solution:
    """
    @param S: a string
    @return: return a string
    """
    def reorganizeString(self, S):
        # write your code here
        if (S == ""):
            return ""
        from collections import Counter
        counter = Counter(S).most_common() # 1
        _, max_freq = counter[0]
        if max_freq > (len(S)+1)//2:
            return ""
        else:
            buckets = [[] for i in range(max_freq)] #2
            begin = 0
            for letter, count in counter:
                for i in range(count):
                    buckets[(i+begin)%max_freq].append(letter) #3
                begin += count
        return "".join("".join(bucket) for bucket in buckets) #4 

# V2 
# Time:  O(nloga) = O(n), a is the size of alphabet
# Space: O(a) = O(1)
import collections
import heapq
class Solution(object):
    def reorganizeString(self, S):
        """
        :type S: str
        :rtype: str
        """
        counts = collections.Counter(S)
        if any(v > (len(S)+1)/2 for k, v in counts.iteritems()):
            return ""

        result = []
        max_heap = []
        for k, v in counts.iteritems():
            heapq.heappush(max_heap, (-v, k))
        while len(max_heap) > 1:
            count1, c1 = heapq.heappop(max_heap)
            count2, c2 = heapq.heappop(max_heap)
            if not result or c1 != result[-1]:
                result.extend([c1, c2])
                if count1+1: heapq.heappush(max_heap, (count1+1, c1))
                if count2+1: heapq.heappush(max_heap, (count2+1, c2))
        return "".join(result) + (max_heap[0][1] if max_heap else '')