# V0
class Solution:
    def insert(self, node, x):
        # write your code 
        originNode = node
        tmp = ListNode(x)
        if node == None:
            node = tmp
            node.next = node
            return node
        else:
            while True:
                if node.next.next == node:
                    tmp.next = node.next
                    node.next = tmp
                    return node
                if (node.val<=x and node.next.val>x) or (node.val<x and node.next.val>=x) or (node.val>node.next.val and node.val<x and node.next.val<x) or (node.val>node.next.val and node.val>x and node.next.val>x):
                    tmp.next = node.next
                    node.next = tmp
                    return node
                node = node.next
                if node == originNode:
                    tmp.next = node.next
                    node.next = tmp
                    return node
                    
# V1
# https://blog.csdn.net/weixin_41677877/article/details/81200818
class Solution:
    def insert(self, node, x):
        # write your code 
        originNode = node
        tmp = ListNode(x)
        if node == None:
            node = tmp
            node.next = node
            return node
        else:
            while True:
                if node.next.next == node:
                    tmp.next = node.next
                    node.next = tmp
                    return node
                if (node.val<=x and node.next.val>x) or (node.val<x and node.next.val>=x) or (node.val>node.next.val and node.val<x and node.next.val<x) or (node.val>node.next.val and node.val>x and node.next.val>x):
                    tmp.next = node.next
                    node.next = tmp
                    return node
                node = node.next
                if node == originNode:
                    tmp.next = node.next
                    node.next = tmp
                    return node

# V2

