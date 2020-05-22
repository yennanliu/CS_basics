# V0

# V1
# https://leetcode.com/problems/split-linked-list-in-parts/discuss/109284/Elegant-Python-with-Explanation-45ms
class Solution(object):
    def splitListToParts(self, root, k):
        # Count the length of the linked list
        curr, length = root, 0
        while curr:
            curr, length = curr.next, length + 1
        # Determine the length of each chunk
        chunk_size, longer_chunks = length // k, length % k
        res = [chunk_size + 1] * longer_chunks + [chunk_size] * (k - longer_chunks)
        # Split up the list
        prev, curr = None, root
        for index, num in enumerate(res):
            if prev:
                prev.next = None
            res[index] = curr
            for i in range(num):
                prev, curr = curr, curr.next
        return res

### Test case : dev 

# V1'
# https://leetcode.com/problems/split-linked-list-in-parts/discuss/139360/Simple-pythonic-solution.-Beats-100
def get_length(root):
    ans = 0
    while root is not None:
        root = root.next
        ans += 1
    return ans

class Solution:
    def splitListToParts(self, root, k):
        ans = [None]*k
        cur = root
        length = get_length(root)
        
        for i in range(k):
            no_elems = (length // k) + (1 if i < (length % k) else 0)
            for j in range(no_elems):
                if j == 0:
                    ans[i] = cur

                if j == no_elems - 1:
                    temp = cur.next
                    cur.next = None
                    cur = temp
                else:
                    cur = cur.next
        
        return ans

# V1''
# https://leetcode.com/problems/split-linked-list-in-parts/discuss/237516/python-solution-beat-100
class Solution:
    def splitListToParts(self, root: 'ListNode', k: 'int') -> 'List[ListNode]':
        n, p, res = 0, root, []
        while p:
            n, p = n + 1, p.next
        a, m, start = n // k, n % k, root
        for _ in range(k):
            if not start: res.append(None)
            else:
                end = start
                for _ in range(a + (1 if m else 0) - 1):
                    end = end.next
                if m > 0: m -= 1
                res.append(start)
                start = end.next
                end.next = None
        return res

# V1'''
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
            root = root.next
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
            for x in range(int(p) - 1):
                node = node.next
            ans.append(root)
            if root:
                root = node.next
                node.next = None
        return ans

# V1''''
# https://blog.csdn.net/fuxuemingzhu/article/details/79543931
class Solution(object):
    def splitListToParts(self, root, k):
        """
        :type root: ListNode
        :type k: int
        :rtype: List[ListNode]
        """
        nodes = []
        counts = 0
        each = root
        while each:
            counts += 1
            each = each.next
        num = int(counts / k)
        rem = int(counts % k)
        for i in range(k):
            head = ListNode(0)
            each = head
            for j in range(num):
                node = ListNode(root.val)
                each.next = node
                each = each.next
                root = root.next
            if rem and root:
                rmnode = ListNode(root.val)
                each.next = rmnode
                if root:
                    root = root.next
                rem -= 1
            nodes.append(head.next)
        return nodes

# V1'''''
# https://leetcode.com/problems/split-linked-list-in-parts/solution/
# IDEA : CREATE NEW LISTS
# time complexity : O(N+K)
# spce complexity : O(N,K)
class Solution(object):
    def splitListToParts(self, root, k):
        cur = root
        for N in range(1001):
            if not cur: break
            cur = cur.next
        width, remainder = divmod(N, k)

        ans = []
        cur = root
        for i in range(k):
            head = write = ListNode(None)
            for j in range(width + (i < remainder)):
                write.next = write = ListNode(cur.val)
                if cur: cur = cur.next
            ans.append(head.next)
        return ans

# V1''''''
# https://leetcode.com/problems/split-linked-list-in-parts/solution/
# IDEA : SPLIT INPUT LIST 
# time complexity : O(N+K)
# spce complexity : O(K)
class Solution(object):
    def splitListToParts(self, root, k):
        cur = root
        for N in range(1001):
            if not cur: break
            cur = cur.next
        width, remainder = divmod(N, k)

        ans = []
        cur = root
        for i in range(k):
            head = cur
            for j in range(width + (i < remainder) - 1):
                if cur: cur = cur.next
            if cur:
                cur.next, cur = None, cur.next
            ans.append(head)
        return ans

# V2
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