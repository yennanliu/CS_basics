
# V1 : dev 




# V2 
# http://bookshadow.com/weblog/2017/11/13/leetcode-split-linked-list-in-parts/
class Solution(object):
    def splitNum(self, m, n):
        q, r = m / n, m % n
        if r > 0: return [q + 1] * r + [q] * (n - r)
        if r < 0: return [q] * (n + r) + [q - 1] * -r
        return  [q] * n

    def listLength(self, root):
        ans = 0
        while root:
            ans += 1
            root = root.__next__
        return ans

    def splitListToParts(self, root, k):
        """
        :type root: ListNode
        :type k: int
        :rtype: List[ListNode]
        """
        ans = []
        s = self.listLength(root)
        for p in self.splitNum(s, k):
            if not p:
                ans.append(None)
                continue
            node = root
            for x in range(p - 1):
                node = node.__next__
            ans.append(root)
            root = node.__next__
            node.next = None
        return ans


# V3 
# Time:  O(n + k)
# Space: O(1)

class Solution(object):
    def splitListToParts(self, root, k):
        """
        :type root: ListNode
        :type k: int
        :rtype: List[ListNode]
        """
        n = 0
        curr = root
        while curr:
            curr = curr.__next__
            n += 1
        width, remainder = divmod(n, k)

        result = []
        curr = root
        for i in range(k):
            head = curr
            for j in range(width-1+int(i < remainder)):
                if curr:
                    curr = curr.__next__
            if curr:
                curr.next, curr = None, curr.next
            result.append(head)
        return result