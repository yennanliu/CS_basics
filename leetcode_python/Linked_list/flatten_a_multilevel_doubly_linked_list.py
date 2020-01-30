# V0
class Solution:
    def flatten(self, head: 'Node') -> 'Node':
        self.dfs(head)
        return head

    def dfs(self, head):
        cur = head
        while cur:
            # if there is a "next layer" child
            if cur.child:
                next = cur.next
                cur.next = cur.child
                cur.next.prev = cur
                # keep go through the same layer linked list via dfs, and connect "childLast" to the next node 
                childLast = self.dfs(cur.child)
                childLast.next = next
                if next: 
                    next.prev = childLast
                cur.child = None
            head = cur
            cur = cur.next
        return head

# V1
# https://blog.csdn.net/fuxuemingzhu/article/details/81985172
class Solution(object):
    def flatten(self, head):
        """
        :type head: Node
        :rtype: Node
        """
        if not head: return None
        node = head
        while node:
            node_next = node.next
            if node.child:
                flattened = self.flatten(node.child)
                node.child = None
                nextNode = self.appendToList(node, flattened)
                node = nextNode
            else:
                node = node.next
        return head

    def appendToList(self, node, listToAppendHead):
        next_node = node.next
        node.next = listToAppendHead
        listToAppendHead.prev = node
        while node.next:
            node = node.next
        node.next = next_node
        if next_node:
            next_node.prev = node
        return next_node

# V1'
# https://lequ7.com/2019/07/27/java/LeetCode430-bian-ping-hua-duo-ji-shuang-xiang-lian-biao-FlattenaMultilevelDoublyLinkedList/
class Solution:
    def flatten(self, head: 'Node') -> 'Node':
        self.dfs(head)
        return head

    def dfs(self, head):
        cur = head
        while cur:
            if cur.child:
                next = cur.next
                cur.next = cur.child
                cur.next.prev = cur
                childLast = self.dfs(cur.child)
                childLast.next = next
                if next: next.prev = childLast
                cur.child = None
            head = cur
            cur = cur.next
        return head
        
# V2
