# V0 

# V1 
# http://bookshadow.com/weblog/2018/06/17/leetcode-exam-room/
# https://blog.csdn.net/fuxuemingzhu/article/details/83141523
# IDEA : bisect.insort :  https://www.cnblogs.com/skydesign/archive/2011/09/02/2163592.html
class ExamRoom(object):

    def __init__(self, N):
        """
        :type N: int
        """
        self.N, self.L = N, list()

    def seat(self):
        """
        :rtype: int
        """
        N, L = self.N, self.L
        if not self.L: res = 0
        else:
            d, res = L[0], 0
            # d means cur distance, res means cur pos
            for a, b in zip(L, L[1:]):
                if (b - a) / 2 > d:
                    d = (b - a) / 2
                    res = (b + a) / 2
            if N - 1 - L[-1] > d:
                res = N - 1
        bisect.insort(L, res)
        return res
        

    def leave(self, p):
        """
        :type p: int
        :rtype: void
        """
        self.L.remove(p)


# Your ExamRoom object will be instantiated and called as such:
# obj = ExamRoom(N)
# param_1 = obj.seat()
# obj.leave(p)

# V2 
# Time:  seat:  O(logn), amortized
#        leave: O(logn)
# Space: O(n)
import heapq
class ExamRoom(object):

    def __init__(self, N):
        """
        :type N: int
        """
        self.__num = N
        self.__seats = {-1: [-1, self.__num], self.__num: [-1, self.__num]}
        self.__max_heap = [(-self.__distance((-1, self.__num)), -1, self.__num)]

    def seat(self):
        """
        :rtype: int
        """
        while self.__max_heap[0][1] not in self.__seats or \
              self.__max_heap[0][2] not in self.__seats or \
              self.__seats[self.__max_heap[0][1]][1] != self.__max_heap[0][2] or \
              self.__seats[self.__max_heap[0][2]][0] !=  self.__max_heap[0][1]:
            heapq.heappop(self.__max_heap)  # lazy deletion

        _, left, right = heapq.heappop(self.__max_heap)
        mid = 0 if left == -1 \
              else self.__num-1 if right == self.__num \
              else (left+right) // 2
        self.__seats[mid] = [left, right]
        heapq.heappush(self.__max_heap, (-self.__distance((left, mid)), left, mid))
        heapq.heappush(self.__max_heap, (-self.__distance((mid, right)), mid, right))
        self.__seats[left][1] = mid
        self.__seats[right][0] = mid
        return mid

    def leave(self, p):
        """
        :type p: int
        :rtype: void
        """
        left, right = self.__seats[p]
        self.__seats.pop(p)
        self.__seats[left][1] = right
        self.__seats[right][0] = left
        heapq.heappush(self.__max_heap, (-self.__distance((left, right)), left, right))
        
    def __distance(self, segment):
        return segment[1]-segment[0]-1 if segment[0] == -1 or segment[1] == self.__num \
               else (segment[1]-segment[0]) // 2