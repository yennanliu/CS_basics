# V0
# IDEA 
# h-index = 1 : AT LEAST 0 essay with power 1 
# h-index = 2 : AT LEAST 0 essay with power 2  
# h-index = 3 : AT LEAST 0 essay with power 3  
# h-index = 4 : AT LEAST 0 essay with power 4  
#.
#.
class Solution(object):
    def hIndex(self, citations):
        """
        :type citations: List[int]
        :rtype: int
        """
        # reverse ordeing the citations first, so the first validated case is the max h-index 
        # i : how many digit in the list that are >= c 
        # c : the value of "possible" h-index
        for i, c in enumerate(sorted(citations, reverse = True)):
            if i >= c:
                return i
        # BE AWARE OF IT
        # for the [] or [1] ... cases
        return len(citations)

# V1
# http://bookshadow.com/weblog/2015/09/03/leetcode-h-index/
class Solution(object):
    def hIndex(self, citations):
        """
        :type citations: List[int]
        :rtype: int
        """
        N = len(citations)
        cnts = [0] * (N + 1)
        for c in citations:
            cnts[[c, N][c > N]] += 1
        sums = 0
        for h in range(N, 0, -1):
            if sums + cnts[h] >= h:
                return h
            sums += cnts[h]
        return 0

# V1''
# http://bookshadow.com/weblog/2015/09/03/leetcode-h-index/
class Solution(object):
    def hIndex(self, citations):
        """
        :type citations: List[int]
        :rtype: int
        """
        for i, c in enumerate(sorted(citations, reverse = True)):
            if i >= c:
                return i
        return len(citations)

# V1'''
# http://bookshadow.com/weblog/2015/09/03/leetcode-h-index/
class Solution(object):
    def hIndex(self, citations):
        """
        :type citations: List[int]
        :rtype: int
        """
        return sum(i < c for i, c in enumerate(sorted(citations, reverse = True)))

# V1''''
# http://bookshadow.com/weblog/2015/09/03/leetcode-h-index/
class Solution(object):
    def hIndex(self, citations):
        """
        :type citations: List[int]
        :rtype: int
        """
        N = len(citations)
        for i, c in enumerate(sorted(citations)):
            if N - i <= c:
                return N - i
        return 0

# V1'''''
# http://bookshadow.com/weblog/2015/09/03/leetcode-h-index/
class Solution(object):
    def hIndex(self, citations):
        """
        :type citations: List[int]
        :rtype: int
        """
        return max(min(c, len(citations) - i) 
                for i, c in enumerate(sorted(citations) + [0]))

# V2
class Solution(object):
    def hIndex(self, citations):
        """
        :type citations: List[int]
        :rtype: int
        """
        n = len(citations)
        count = [0] * (n + 1)
        for x in citations:
            # Put all x >= n in the same bucket.
            if x >= n:
                count[n] += 1
            else:
                count[x] += 1

        h = 0
        for i in reversed(range(0, n + 1)):
            h += count[i]
            if h >= i:
                return i
        return h

# V4 
# Time:  O(nlogn)
# Space: O(1)
class Solution2(object):
    def hIndex(self, citations):
        """
        :type citations: List[int]
        :rtype: int
        """
        citations.sort(reverse=True)
        h = 0
        for x in citations:
            if x >= h + 1:
                h += 1
            else:
                break
        return h

# V5 
# Time:  O(nlogn)
# Space: O(n)
class Solution3(object):
    def hIndex(self, citations):
        """
        :type citations: List[int]
        :rtype: int
        """
        return sum(x >= i + 1 for i, x in enumerate(sorted(citations, reverse=True)))