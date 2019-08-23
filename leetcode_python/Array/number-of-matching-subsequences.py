# V0 

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/82834760
# IDEA : BINARY SEARCH + DICT 
import collections, bisect
class Solution(object):
    def numMatchingSubseq(self, S, words):
        """
        :type S: str
        :type words: List[str]
        :rtype: int
        """
        m = dict()
        def isMatch(word, d):
            if word in m:
                return m[word]
            prev = -1
            for w in word:
                i = bisect.bisect_left(d[w], prev + 1)
                if i == len(d[w]):
                    return 0
                prev = d[w][i]
            m[word] = 1
            return 1
        
        d = collections.defaultdict(list)
        for i, s in enumerate(S):
            d[s].append(i)
        ans = [isMatch(word, d) for word in words]
        return sum(ans)

# V1'
# https://blog.csdn.net/u010829672/article/details/79833283
# IDEA : BINARY SEARCH 
class Solution(object):
    def numMatchingSubseq(self, S, words):
        """
        :type S: str
        :type words: List[str]
        :rtype: int
        938MS
        """
        import collections, bisect
        ans = 0
        S_dicts = collections.defaultdict(list)
        # record index for every element in S 
        for i, char in enumerate(S):
            S_dicts[char].append(i)
        for word in words:
            # binary search from -1 index in S 
            start = -1
            flag = True
            # search the index in every element in word 
            for char in word:
                # if the element not exists in S, then S can't be formed by such substring (of S) 
                if S_dicts[char] == []:
                    flag = False
                    break
                else:
                    # find the nearest index (after the last choose) in S 
                    pos = bisect.bisect(S_dicts[char], start)
                    # if the element exists, update its current index 
                    if pos < len(S_dicts[char]):
                        start = S_dicts[char][pos]
                    # if can't find the index that fit needed
                    else:
                        flag = False
            # word，ans+1
            if flag:
                ans += 1
        return ans

# V1''
# https://blog.csdn.net/u010829672/article/details/79833283
class Solution(object):
    def numMatchingSubseq(self, S, words):
        """
        :type S: str
        :type words: List[str]
        :rtype: int
        527ms
        """
        import collections
        waiting = collections.defaultdict(list)
        for w in words:
            waiting[w[0]].append(iter(w[1:]))
        for c in S:
            # use pop() method delete dict key's value, and return such value, if no, return default value
            # remove all words start with "c"
            for it in waiting.pop(c, ()):
                # if there are still other element in such word, then merge it witg before, or put into None
                waiting[next(it, None)].append(it)
        return len(waiting[None])

# V2 
# Time:  O(n + w), n is the size of S, w is the size of words
# Space: O(k), k is the number of words
import collections
class Solution(object):
    def numMatchingSubseq(self, S, words):
        """
        :type S: str
        :type words: List[str]
        :rtype: int
        """
        waiting = collections.defaultdict(list)
        for word in words:
            it = iter(word)
            waiting[next(it, None)].append(it)
        for c in S:
            for it in waiting.pop(c, ()):
                waiting[next(it, None)].append(it)
        return len(waiting[None])