"""

Given the head of a linked list, reverse the nodes of the list k at a time, and return the modified list.

k is a positive integer and is less than or equal to the length of the linked list. If the number of nodes is not a multiple of k then left-out nodes, in the end, should remain as it is.

You may not alter the values in the list's nodes, only nodes themselves may be changed.

 

Example 1:


Input: head = [1,2,3,4,5], k = 2
Output: [2,1,4,3,5]
Example 2:


Input: head = [1,2,3,4,5], k = 3
Output: [3,2,1,4,5]
 

Constraints:

The number of nodes in the list is n.
1 <= k <= n <= 5000
0 <= Node.val <= 1000
 

Follow-up: Can you solve the problem in O(1) extra memory space?

"""


# V0
# https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/linked_list.md#1-1-6-reverse-nodes-in-k-group--linked-list-iteration

# V1
# IDEA : ITERATION + help func
# https://leetcode.com/problems/reverse-nodes-in-k-group/discuss/462808/Python-Clear-Solution
class Solution:
    def reverseKGroup(self, head, k):
        dummy = ListNode(None)
        dummy.next = head
        d = dummy
        pre = None
        curHead = head
        preHead = curHead
        while self.check(curHead, k):
            for _ in range(k):
                temp = curHead.next
                curHead.next = pre
                pre = curHead
                curHead = temp
            dummy.next = pre
            dummy = preHead
            preHead.next = curHead
            preHead = curHead
        return d.next
            
        
        
    def check(self, head, k):
        ans = 0
        while head:
            ans += 1
            if ans >= k:
                return True
            head = head.next
        return False

# V1'
# IDEA : RECURSIVE
# https://leetcode.com/problems/reverse-nodes-in-k-group/discuss/11676/64ms-python-solution1
class Solution(object):
    def reverseKGroup(self, head, k):
        if not head or not head.next:
            return head

        tail = head
        for i in range(k):
            if not tail:
                return head

            tail = tail.next

        tail = self.reverseKGroup(tail, k)

        for i in range(k):
            next = head.next
            head.next = tail
            tail = head
            head = next

        return tail

# V1''
# https://leetcode.com/problems/reverse-nodes-in-k-group/discuss/11508/Python-solution-with-detailed-explanation
class Solution(object):
    def reverseKGroup(self, head, K):
        """
        :type head: ListNode
        :rtype: ListNode
        """
        splits = [ListNode(-1) for _ in range(K)]
        tails = [splits[i] for i in range(K)]
        
        k = 0
        while head:
            temp = head.next
            tails[k % K].next = head
            tails[k % K] = head
            head.next = None
            head = temp
            k = k + 1

        start, tails = ListNode(-1), [splits[i].next for i in range(K)]
        result = start
        while k > 0:
            (s, e, inc) = (len(tails)-1,-1,-1) if tails[-1] else (0, len(tails), 1)
            for i in range(s, e, inc):
                if tails[i] != None:
                    temp = tails[i].next
                    result.next = tails[i]
                    result = tails[i]
                    tails[i].next = None
                    tails[i] = temp
                    k -= 1
        return start.next
            
# V1'''
# https://leetcode.com/problems/reverse-nodes-in-k-group/discuss/211534/Python-solution
class Solution(object):
    def reverseKGroup(self, head, k):
        """
        :type head: ListNode
        :type k: int
        :rtype: ListNode
        """
        while not head or not head.next:
            return head
        if k == 1:
            return head
        dummy = ListNode(0)
        dummy.next = head
        record = dummy
        left = head
        right = head.next
        count = 1
        while True:
            while right and count < k: # reverse links within a group
                tmp = right.next
                right.next = left
                prev = left
                left = right
                right = tmp
                count += 1
            if count == k: # rotate the group 
                tmp = dummy.next
                dummy.next = left
                tmp.next = right
                dummy = tmp
                count = 1
                left = right
                if right:
                    right = right.next
            else:
                if count >= 2:
                    left.next = None
                    if count > 2: # the last group has size < k, and we need to reverse the links within the group again.
                        while True:
                            tmp = prev.next
                            prev.next = left
                            if tmp.next == prev:
                                return record.next
                            left = prev
                            prev = tmp
                return record.next

# V1''''
# https://zxi.mytechroad.com/blog/list/leetcode-25-reverse-nodes-in-k-group/
# C++
# class Solution {
# public:
#   ListNode *reverseKGroup(ListNode *head, int k) {
#     if (!head || k == 1) return head;
#     ListNode dummy(0);
#     dummy.next = head;
#     int len = 1;
#     while (head = head->next) len++;
#     ListNode* pre = &dummy;    
#     for (int l = 0; l + k <= len; l += k) {
#       ListNode* cur = pre->next;
#       ListNode* nxt = cur->next;
#       for (int i = 1; i < k; ++i) {
#         cur->next = nxt->next;
#         nxt->next = pre->next;
#         pre->next = nxt;
#         nxt = cur->next;
#       }
#       pre = cur;
#     }
#     return dummy.next;
#   }
# };

# V2