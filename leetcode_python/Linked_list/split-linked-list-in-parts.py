"""

725. Split Linked List in Parts
Medium

0Given the head of a singly linked list and an integer k, split the linked list into k consecutive linked list parts.

The length of each part should be as equal as possible: no two parts should have a size differing by more than one. This may lead to some parts being null.

The parts should be in the order of occurrence in the input list, and parts occurring earlier should always have a size greater than or equal to parts occurring later.

Return an array of the k parts.

 

Example 1:


Input: head = [1,2,3], k = 5
Output: [[1],[2],[3],[],[]]
Explanation:
The first element output[0] has output[0].val = 1, output[0].next = null.
The last element output[4] is null, but its string representation as a ListNode is [].
Example 2:


Input: head = [1,2,3,4,5,6,7,8,9,10], k = 3
Output: [[1,2,3,4],[5,6,7],[8,9,10]]
Explanation:
The input has been split into consecutive parts with size difference at most 1, and earlier parts are a larger size than the later parts.
 

Constraints:

The number of nodes in the list is in the range [0, 1000].
0 <= Node.val <= 1000
1 <= k <= 50

"""

# V0
# IDEA : LINKED LIST OP + mod op
class Solution(object):
    def splitListToParts(self, head, k):
        # NO need to deal with edge case !!!
        # get linked list length
        _len = 0
        _head = cur = head
        while _head:
            _len += 1
            _head = _head.next
        # init res
        res = [None] * k
        ### NOTE : we loop over k
        for i in range(k):
            """
            2 cases

            case 1) i < (_len % k) : there is "remainder" ((_len % k)), so we need to add extra 1
                    -> _cnt_elem = (_len // k) + 1
            case 2) i == (_len % k) : there is NO "remainder"
                    -> _cnt_elem = (_len // k)
            """
            # NOTE THIS !!!
            _cnt_elem = (_len // k) + (1 if i < (_len % k) else 0)
            ### NOTE : we loop over _cnt_elem (length of each "split" linkedlist)
            for j in range(_cnt_elem):
                """
                3 cases
                 1) j == 0                (begin of sub linked list)
                 2) j == _cnt_elem - 1    (end of sub linked list)
                 3) 0 < j < _cnt_elem - 1 (middle within sub linked list)
                """
                # NOTE THIS !!!
                # NOTE we need keep if - else in BELOW ORDER !!
                #  -> j == 0, j == _cnt_elem - 1, else
                if j == 0:
                    res[i] = cur
                ### NOTE this !!! : 
                #    -> IF (but not elif)
                #    -> since we also need to deal with j == 0 and j == _cnt_elem - 1 case
                if j == _cnt_elem - 1:  # note this !!!
                    # get next first
                    tmp = cur.next
                    # point cur.next to None
                    cur.next = None
                    # move cur to next (tmp) for op in next i (for i in range(k))
                    cur = tmp
                else:
                    cur = cur.next
        #print ("res = " + str(res))
        return res

# V0'
class Solution(object):
    def splitListToParts(self, head, k):
        # NO need to deal with edge case !!!
        # get len
        root = cur = head
        _len = 0
        while root:
            root = root.next
            _len += 1
        res = [None] * k
        for i in range(k):
            tmp_cnt = (_len // k) + (1 if i < (_len % k) else 0)
            for j in range(tmp_cnt):
                # 3 cases
                # j == 0
                if j == 0:
                    res[i] = cur
                # IF !!!! j == tmp_cnt - 1 !!!
                if j == tmp_cnt-1:
                    _next = cur.next
                    cur.next = None
                    cur = _next
                # 0 < j < tmp_cnt
                else:
                    cur = cur.next

        print ("res = " + str(res))
        return res

# V0'
# IDEA : LINKED LIST OP
class Solution:
    def splitListToParts(self, root, k):
        def get_length(root):
            ans = 0
            while root is not None:
                root = root.next
                ans += 1
            return ans
            
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