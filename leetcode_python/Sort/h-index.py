



# V1 : dev 
# class Solution(object):
# 	def hIndex(self, citations):
# 		"""
# 		:type citations: List[int]
# 		:rtype: int
# 		"""
# 		if len(citations) <=1:
# 			return 0
# 		citations.sort(reverse=True)
# 		for i in range(len(citations)):
# 			if (i+1) > (len(citations) - (i+1)):
# 				return i+1
# 			else:
# 				pass  

# V3 
# Time:  O(n)
# Space: O(n)

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




