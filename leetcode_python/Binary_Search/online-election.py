# V0 

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/82833704
class TopVotedCandidate(object):

    def __init__(self, persons, times):
        """
        :type persons: List[int]
        :type times: List[int]
        """
        self.leads, self.times, count = [], times, {}
        lead = -1
        for p, t in zip(persons, times):
            count[p] = count.get(p, 0) + 1
            if count.get(lead, 0) <= count[p]:
                lead = p
            self.leads.append(lead)
            
    def q(self, t):
        """
        :type t: int
        :rtype: int
        """
        l, r = 0, len(self.times) - 1
        while l <= r:
            mid = (l + r) // 2
            if self.times[mid] == t:
                return self.leads[mid]
            elif self.times[mid] > t:
                r = mid - 1
            else:
                l = mid + 1
        return self.leads[l - 1]
        
# V2 
# Time:  ctor: O(n)
#        q:    O(logn)
# Space: O(n)
import collections
import itertools
import bisect
class TopVotedCandidate(object):

    def __init__(self, persons, times):
        """
        :type persons: List[int]
        :type times: List[int]
        """
        lead = -1
        self.__lookup, count = [], collections.defaultdict(int)
        for t, p in itertools.izip(times, persons):
            count[p] += 1
            if count[p] >= count[lead]:
                lead = p
                self.__lookup.append((t, lead))

    def q(self, t):
        """
        :type t: int
        :rtype: int
        """
        return self.__lookup[bisect.bisect(self.__lookup,
                                           (t, float("inf")))-1][1]
