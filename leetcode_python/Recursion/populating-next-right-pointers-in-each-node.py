"""

116. Populating Next Right Pointers in Each Node
Medium

You are given a perfect binary tree where all leaves are on the same level, and every parent has two children. The binary tree has the following definition:

struct Node {
  int val;
  Node *left;
  Node *right;
  Node *next;
}
Populate each next pointer to point to its next right node. If there is no next right node, the next pointer should be set to NULL.

Initially, all next pointers are set to NULL.

 

Example 1:


Input: root = [1,2,3,4,5,6,7]
Output: [1,#,2,3,#,4,5,6,7,#]
Explanation: Given the above perfect binary tree (Figure A), your function should populate each next pointer to point to its next right node, just like in Figure B. The serialized output is in level order as connected by the next pointers, with '#' signifying the end of each level.
Example 2:

Input: root = []
Output: []
 

Constraints:

The number of nodes in the tree is in the range [0, 212 - 1].
-1000 <= Node.val <= 1000
 

Follow-up:

You may only use constant extra space.
The recursive approach is fine. You may assume implicit stack space does not count as extra space for this problem.

"""


# V0
# IDEA: BFS by layer + prev node (GEMINI)
"""


Q: why we do the op with prev but still return root as final result?
does the effect applied on root ? 


->




這是一個非常經典且重要的指標（Reference / Pointer）觀念問題！讓我們分兩部分來徹底釐清：

---

### Q1：為什麼我們操作的是 `prev` 和 `cur`，最後卻回傳 `root`？

**答案：因為我們使用的是「原地修改（In-place Modification）」，而 `root` 是整棵樹的「唯一入口」。**

1. **Python 的物件參照（Reference）機制**：
在 Python 中，變數（像是 `root`、`cur`、`prev`）並不直接儲存整棵樹的資料，而是儲存**記憶體位置的參照（指標）**。
2. **原地修改內部屬性**：
當你在程式碼中執行 `prev.next = cur` 時，你並不是在建立一棵新樹，而是**直接去修改原本那棵樹中，某個節點內部的 `.next` 屬性**。
3. **透過入口掌控全貌**：
既然整棵樹的節點（包含垂直的 `left`/`right` 以及我們剛才串好的水平 `next`）都被串在了一起，我們只需要回傳最頂層的根節點 `root`，主程式或測資（Test Case）就能從這個「火車頭」出發，順藤摸瓜訪問到整棵樹的所有節點。

---

### Q2：這個修改的效果真的有套用到 `root` 身上嗎？

**答案：有的，效果確實存在於以 `root` 為根節點的整張樹狀圖中。**

* **關於 `root` 本身**：
`root` 是整棵樹的最頂層（第 0 層），這一層只有它自己一個節點，沒有其他兄弟節點可以連接。因此它的 `.next` 自然會維持預設的 `None`。
* **關於 `root` 的子孫節點**：
雖然 `root` 本身的 `.next` 是 `None`，但藉由它延伸出去的**第二層、第三層及所有子節點**，它們的 `.next` 指標都在 `while` 迴圈的運作中被確實修改了。

> 🚂 **一個生動的比喻：**
> 把整棵二元樹想像成一列**多層次的火車**：
> * `root` 就是**第一層的火車頭**。
> * 透過 `.left` 和 `.right`，火車頭連接了下面各車廂；而我們寫的程式碼，就像是幫每一層的車廂之間加上了**橫向的連接鉤（`.next`）**。
> * 當我們把整列火車的所有內部連接都搞定後，我們只要把**火車頭（`root`）**交還給別人（`return root`），別人就能順著火車頭，把整列火車（包含所有的橫向連結）完好無缺地帶走。
> 
>


"""
from collections import deque

class Solution(object):
    def connect(self, root):
        if not root:
            return None

        # 將根節點放入佇列中作為 BFS 的起點
        q = deque([root])

        while q:
            # 取得當前層的節點總數
            size = len(q)
            prev = None # 每進入新的一層，將 prev 重設為 None

            for i in range(size):
                cur = q.popleft() # 取出當前層的最左側節點

                # 如果 prev 不是 None，代表這不是該層的第一個節點
                # 我們將前一個節點的 next 指向當前的 cur
                if prev:
                    prev.next = cur
                prev = cur # 將 prev 移動到當前節點，準備連接下一個

                # 將下一層的左右子節點依序推進佇列
                if cur.left:
                    q.append(cur.left)
                if cur.right:
                    q.append(cur.right)

            # 該層最後一個節點的 next 會維持預設的 None
            # (雖然 Python 中樹節點初始化時 next 本身就是 None，這裡寫出是為了邏輯完整)
            prev.next = None

        return root


# V0
# IDEA: BFS by layer + prev node (gpt)
from collections import deque

# time = O(N)
# space = O(N)
class Solution(object):
    def connect(self, root):
        if not root:
            return None

        q = deque([root])

        while q:
            size = len(q)
            prev = None

            for i in range(size):
                cur = q.popleft()

                if prev:
                    prev.next = cur
                prev = cur

                if cur.left:
                    q.append(cur.left)
                if cur.right:
                    q.append(cur.right)

            """
            NOTE !!!

            below
            """
            # last node in this level
            prev.next = None

        return root


# V0-1
# IDEA: BFS by layer + prev node (GEMINI)
from collections import deque

# time = O(N)
# space = O(N)
class Solution(object):
    def connect(self, root):
        """
        :type root: Node
        :rtype: Node
        """
        if not root:
            return None
            
        # Initialize queue using deque for O(1) pops from the front
        q = deque([root])
        
        while q:
            size = len(q)
            
            for i in range(size):
                cur = q.popleft()
                
                # If this is NOT the last node of the current level,
                # point its next pointer to the current front of the queue
                if i < size - 1:
                    cur.next = q[0]
                else:
                    cur.next = None  # Last node of the level points to None
                    
                # Standard child collection step (no parentheses!)
                if cur.left:
                    q.append(cur.left)
                if cur.right:
                    q.append(cur.right)
                    
        # Return the original root node of the modified tree
        return root



# V0-2
# IDEA: BFS by layer + O(1) space (GEMINI)
# time = O(N)
# space = O(1)
class Solution(object):
    def connect(self, root):
        if not root:
            return None

        # Start at the leftmost node of the current level
        leftmost = root
        
        while leftmost.left:
            # Step across the current level using the established .next pointers
            head = leftmost
            while head:
                # Connection 1: Connect children sharing the same parent
                head.left.next = head.right
                
                # Connection 2: Connect children spanning across different parents
                if head.next:
                    head.right.next = head.next.left
                    
                # Move to the next parent node on this same level
                head = head.next
                
            # Move down to the next level's leftmost starting point
            leftmost = leftmost.left
            
        return root


# V0-3
# IDEA : BFS
# REF : LC # 117 : populating-next-right-pointers-in-each-node-ii/
# time = O(N)
# space = O(N)
class Solution:
    def connect(self, root):
        if not root:
            return None
        queue = []
        queue.append(root)
        while queue:
            _len = len(queue)
            for i in range(_len):
                node = queue.pop(0)
                ### IF NOT LAST NODE, POINT NEXT TO FIRST NODE IN THE QUEUE
                if i < _len - 1:
                    node.next = queue[0]
                if node.left:
                    queue.append(node.left)
                if node.right:
                    queue.append(node.right)
        return root

# V0-4
# IDEA : BFS
### NOTE : in this problem, we need to populate the result as LINKED LIST form
from collections import deque
# time = O(N)
# space = O(N)
class Solution(object):
    def connect(self, root):
        if not root:
            return None
        root.next = None
        queue = deque([root])
        while queue:
            temp, size=[], len(queue)
            for i in range(size):
                node = queue.popleft()
                if node:
                    temp.append(node)
                if node.left:
                    queue.append(node.left)
                if node.right:
                    queue.append(node.right)
            ### NOTE : this op is OUTSIDE OF FOR LOOP
            ###    -> DO ONCE WHEN EVERY BFS LAYER
            # >>> a
            # [1, 2, 3]
            # >>> a[:-1]
            # [1, 2]
            # -> enumerate(temp[:-1]) will looping from last n-1 elements
            for index, node in enumerate(temp[:-1]):
                node.next = temp[index+1]
            # point last element to None (linked list)
            temp[-1].next = None ###  NOTE : temp[-1].next, is pointing tmp's next to None
        return root

# V0-5
# IDEA : BFS
### NOTE : in this problem, we need to populate the result as LINKED LIST form
from collections import deque
# time = O(N)
# space = O(N)
class Solution(object):
    def connect(self, root):
        """
        :type root: Node
        :rtype: Node
        """
        if not root:
            return None
        root.next = None
        queue = deque([root])
        while queue:
            temp,size=[],len(queue)
            for i in range(size):
                node = queue.popleft()
                if node:
                    temp.append(node)
                if node.left and node.right:
                    queue.append(node.left)
                    queue.append(node.right)
            for index,node in enumerate(temp[:-1]):
                node.next = temp[index+1]
            temp[-1].next = None
        return root

# V0-6
### NOTE : in this problem, we need to populate the result as LINKED LIST form
# IDEA : DFS
# DEMO
#      1 -> NULL
#    /  \
#   2 -> 3 -> NULL
#  / \  / \
# 4->5->6->7 -> NULL
# time = O(N)
# space = O(log N)
class Solution:
    # @param root, a tree link node
    # @return nothing
    def connect(self, root):
        if not root: return
        if root.right:
            # when at 1 node, connect 2->3 
            root.left.next = root.right
            # when at 2 node, the root.next = 3 
            if root.next:
                # so will connect 5 -> 6
                root.right.next = root.next.left
        self.connect(root.left)
        self.connect(root.right) 

# V1
# IDEA :  Level Order Traversal
# https://leetcode.com/problems/populating-next-right-pointers-in-each-node/solution/
import collections

# time = O(N)
# space = O(N)
class Solution:
    def connect(self, root: 'Node') -> 'Node':
        
        if not root:
            return root
        
        # Initialize a queue data structure which contains
        # just the root of the tree
        Q = collections.deque([root])
        
        # Outer while loop which iterates over 
        # each level
        while Q:
            
            # Note the size of the queue
            size = len(Q)
            
            # Iterate over all the nodes on the current level
            for i in range(size):
                
                # Pop a node from the front of the queue
                node = Q.popleft()
                
                # This check is important. We don't want to
                # establish any wrong connections. The queue will
                # contain nodes from 2 levels at most at any
                # point in time. This check ensures we only 
                # don't establish next pointers beyond the end
                # of a level
                if i < size - 1:
                    node.next = Q[0]
                
                # Add the children, if any, to the back of
                # the queue
                if node.left:
                    Q.append(node.left)
                if node.right:
                    Q.append(node.right)
        
        # Since the tree has now been modified, return the root node
        return root

# V1'
# IDEA : Using previously established next pointers
# https://leetcode.com/problems/populating-next-right-pointers-in-each-node/solution/
# time = O(N)
# space = O(1)
class Solution:
    def connect(self, root: 'Node') -> 'Node':
        
        if not root:
            return root
        
        # Start with the root node. There are no next pointers
        # that need to be set up on the first level
        leftmost = root
        
        # Once we reach the final level, we are done
        while leftmost.left:
            
            # Iterate the "linked list" starting from the head
            # node and using the next pointers, establish the 
            # corresponding links for the next level
            head = leftmost
            while head:
                
                # CONNECTION 1
                head.left.next = head.right
                
                # CONNECTION 2
                if head.next:
                    head.right.next = head.next.left
                
                # Progress along the list (nodes on the current level)
                head = head.next
            
            # Move onto the next level
            leftmost = leftmost.left
        
        return root 

# V1''
# https://blog.csdn.net/fuxuemingzhu/article/details/79559645
# time = O(N)
# space = O(log N)
class Solution:
    # @param root, a tree link node
    # @return nothing
    def connect(self, root):
        if not root: return
        if root.right:
            root.left.next = root.right
            if root.next:
                root.right.next = root.next.left
        self.connect(root.left)
        self.connect(root.right)

# V1'''
# IDEA : BFS
# https://leetcode.com/problems/populating-next-right-pointers-in-each-node/discuss/291622/Python-recursive-solution
from collections import deque
# time = O(N)
# space = O(N)
class Solution(object):
    def connect(self, root):
        """
        :type root: Node
        :rtype: Node
        """
        if not root:
            return None
        root.next = None
        queue = deque([root])
        while queue:
            temp,size=[],len(queue)
            for i in range(size):
                node = queue.popleft()
                if node:
                    temp.append(node)
                if node.left and node.right:
                    queue.append(node.left)
                    queue.append(node.right)
            for index,node in enumerate(temp[:-1]):
                node.next = temp[index+1]
            temp[-1].next = None
        return root

# V1''''
# IDEA : LINKED LIST
# https://leetcode.com/problems/populating-next-right-pointers-in-each-node/discuss/291622/Python-recursive-solution
# time = O(N)
# space = O(1)
class Solution(object):
    def connect(self, root):
        """
        :type root: Node
        :rtype: Node
        """
        if not root:
            return None
        cur = root
        nex = cur.left
        while cur.left:
            cur.left.next = cur.right
            if cur.next:
                cur.right.next = cur.next.left
                cur = cur.next
            else:
                cur = nex
                nex = cur.left
        return root

# V1'''''
# https://www.cnblogs.com/loadofleaf/p/5523911.html
# time = O(N)
# space = O(log N)
class Solution(object):
    def connect(self, root):
        if root and root.left:
            root.left.next = root.right
            if root.next:
                root.right.next = root.next.left
            else:
                root.right.next = None
            self.connect(root.left)
            self.connect(root.right)

# V2
class TreeNode(object):
    def __init__(self, x):
        self.val = x
        self.left = None
        self.right = None
        self.next = None

    def __repr__(self):
        if self is None:
            return "Nil"
        else:
            return "{} -> {}".format(self.val, repr(self.__next__))

# time = O(N)
# space = O(1)
class Solution(object):
    # @param root, a tree node
    # @return nothing
    def connect(self, root):
        head = root
        while head:
            cur = head
            while cur and cur.left:
                cur.left.next = cur.right
                if cur.__next__:
                    cur.right.next = cur.next.left
                cur = cur.__next__
            head = head.left