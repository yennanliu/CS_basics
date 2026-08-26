# Linked List 

> **Scope** — Pointer surgery on singly and doubly linked lists — reversal, merging, reordering, dummy-head technique, and cycle handling.
> **See also**: [linked_list_examples.md](./linked_list_examples.md) — the worked solutions these templates are for; [2_pointers_linkedlist.md](./2_pointers_linkedlist.md) — the fast/slow pointer specialisation; [design.md](./design.md) — LRU and other list+map designs; [heap.md](./heap.md) — k-way list merging; [recursion.md](./recursion.md) — recursive list rewriting.

## LeetCode Problem Lists

- [Linked List](https://leetcode.com/problem-list/linked-list/)
- [Doubly-Linked List](https://leetcode.com/problem-list/doubly-linked-list/)

## Time Complexity

| Data structure | Search   | Insert   | Delete   | Min/Max  |
| -------------- | -------- | -------- | -------- | -------- |
| Linked List    | O(n)     | O(1)     | O(1)     | O(n)     |

> Insert / Delete are **O(1)** given the target node (e.g. head, or a node you already hold); *locating* that node first is **O(n)**.

## 0) Concept
- [fucking algorithm : reverse part of linked list](https://github.com/labuladong/fucking-algorithm/blob/master/%E6%95%B0%E6%8D%AE%E7%BB%93%E6%9E%84%E7%B3%BB%E5%88%97/%E9%80%92%E5%BD%92%E5%8F%8D%E8%BD%AC%E9%93%BE%E8%A1%A8%E7%9A%84%E4%B8%80%E9%83%A8%E5%88%86.md)
- [fucking algorithm : reverse k set of linked list](https://github.com/labuladong/fucking-algorithm/blob/master/%E9%AB%98%E9%A2%91%E9%9D%A2%E8%AF%95%E7%B3%BB%E5%88%97/k%E4%B8%AA%E4%B8%80%E7%BB%84%E5%8F%8D%E8%BD%AC%E9%93%BE%E8%A1%A8.md)
- [fucking algorithm : check palindrome linked list](https://github.com/labuladong/fucking-algorithm/blob/master/%E9%AB%98%E9%A2%91%E9%9D%A2%E8%AF%95%E7%B3%BB%E5%88%97/%E5%88%A4%E6%96%AD%E5%9B%9E%E6%96%87%E9%93%BE%E8%A1%A8.md)


- Use "pseudo head node" 虛擬頭節點
    - [代碼隨想錄: LC 203 Remove Linked List Elements](https://youtu.be/Y4oQJklHxVo?t=1111)
- When delete node from linked list, we need to be at "previous" node, then can delete NEXT node
    - so, need to be at `cur` node, than can `cur.next` node
    ```python
    # python
    # https://youtu.be/Y4oQJklHxVo?t=965
    cur.next = cur.next.next
    ```

```python
# python
# Definition for singly-linked list.
class ListNode(object):
    def __init__(self, val=0, next=None):
        self.val = val
        self.next = next
```

```java
// java

// Single Linkedlist

public class ListNode{

    // attr
    public int val;
    public ListNode next;

    // constructor
    public ListNode(){

    }

    public ListNode(int val){
        this.val = val;
    }

    ListNode(int val, ListNode next){
        this.val = val;
        this.next = next;
    }

}

// init a ListNode
ListNode node1 = new ListNode(1);
ListNode node2 = new ListNode(2);
ListNode node3 = new ListNode(3);

// motify node's value
node1.val = 0;

// connect nodes
node1.next = node2;
node2.next = node3;
```


```java
// java

// Double linked list

// LC 146

public class Node {
    int key;
    int val;
    Node prev;
    Node next;

    public Node(int key, int val) {
        this.key = key;
        this.val = val;
        this.prev = null;
        this.next = null;
    }
}
```

### 0-1) Types
- Linked list
- Cycle linked list
- Bi-direction linked list
- Double Linked list
    - LC 146
- Others
    - LC 138 : 
    ```python
    dic = dict()
    m = n = head
    dic[m] = Node(m.val)
    ```
    - LC 208 : 
    - [trie](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/trie.md)
    ```python
    self.children = defaultdict(Node)
    ```
- problem types
    - reverse
        - reverse linked list
            - LC 206
        -  reverse linked list within start, end point
            - LC 92, LC 25
        - reverse part of linked list
        - reverse k set of linked list
    - merge
        - merge 2 linked list
    - check
        - check cyclic linked list
        - check beginning of cyclic linked list
    - remove N th node
        - Remove Nth Node From End of List - LC 19
    -  combinations
        - combinations of above cases

### 0-2) Pattern

#### **Dummy Head Technique**

**Definition**: Create a dummy/pseudo head node that points to the actual head, making it easier to handle edge cases and node removal operations.

**When to Use**:
- Removing nodes from the beginning of the list
- When the head node might be modified
- Simplifying edge case handling
- Operations that need to track the previous node

**Time Complexity**: O(n) - same as without dummy head
**Space Complexity**: O(1) - only one extra node

**Template Pattern**:
```python
def linked_list_operation(head):
    # Create dummy head
    dummy = ListNode(0)
    dummy.next = head

    # Use prev to track previous node
    prev = dummy
    curr = head

    while curr:
        # Perform operations
        if condition:
            # Remove current node
            prev.next = curr.next
        else:
            prev = curr
        curr = curr.next

    # Return new head (dummy.next)
    return dummy.next
```

**Advantages**:
- Eliminates need for special handling of head node
- Simplifies code logic
- Reduces edge case bugs
- Consistent prev pointer throughout traversal

---

#### **Why Dummy Node? Visual Comparison (LC 19)**

> **Problem**: Remove the **n-th node from the end** of `[1, 2, 3, 4, 5]`.

---

##### Case A — Normal removal: `n = 2` (remove node `4`)

**Without dummy** — works fine here:

```text
fast = slow = head = [1]

Step 1: move fast n=2 steps ahead
  [1] -> [2] -> [3] -> [4] -> [5]
  ^slow          ^fast

Step 2: move both until fast.next is None
  [1] -> [2] -> [3] -> [4] -> [5]
                ^slow          ^fast

Step 3: slow.next = slow.next.next  →  removes [4]
  [1] -> [2] -> [3] -> [5]  ✓
```

**With dummy** — also works, same logic:

```text
fast = slow = dummy[0]

Step 1: move fast n+1=3 steps ahead
  [0] -> [1] -> [2] -> [3] -> [4] -> [5]
  ^slow                ^fast

Step 2: move both until fast is None
  [0] -> [1] -> [2] -> [3] -> [4] -> [5]
                ^slow                 ^fast → None (stop)

Step 3: slow.next = slow.next.next  →  removes [4]
  [0] -> [1] -> [2] -> [3] -> [5]  → return dummy.next = [1] ✓
```

---

##### Case B — Edge case: `n = 5` (remove the **head** node `1`)

**Without dummy** — BREAKS, needs special-case code:

```text
fast = slow = head = [1]

Step 1: move fast n=5 steps
  fast: 1 -> 2 -> 3 -> 4 -> 5 -> None

  [1] -> [2] -> [3] -> [4] -> [5] -> None
  ^slow                               ^fast (None!)

Step 2: while fast.next → fast is None, loop NEVER runs
  slow is still at [1]  (the head itself!)

Step 3: slow.next = slow.next.next
  → This removes [2], NOT the head — WRONG ❌

  Must add a special case:
  if not fast:
      return head.next  # ← extra branch needed
```

**With dummy** — works uniformly, NO special case:

```text
fast = slow = dummy[0]

Step 1: move fast n+1=6 steps ahead
  fast: dummy -> 1 -> 2 -> 3 -> 4 -> 5 -> None

  [0] -> [1] -> [2] -> [3] -> [4] -> [5] -> None
  ^slow                                       ^fast (None)

Step 2: while fast → fast is None, loop NEVER runs
  slow stays at dummy[0]  ← one node BEFORE the head

Step 3: slow.next = slow.next.next
  dummy.next = [2]  →  head [1] is removed ✓

Return dummy.next = [2] -> [3] -> [4] -> [5]  ✓  No special case!
```

---

##### Summary: Why dummy wins

| | Without Dummy | With Dummy |
|---|---|---|
| Normal removal | ✓ Works | ✓ Works |
| Remove head (n = len) | ❌ Needs `if not fast: return head.next` | ✓ Works uniformly |
| Code branches | Extra conditional | None |
| `slow` start position | `head` (can't reach before head) | `dummy` (one step before head) |

**Key insight**: the dummy node gives `slow` a "standing position" **one node before the head**, so it can reconnect across any node — including the head itself — without special handling.

```python
# LC 19 — with dummy (handles all cases cleanly)
def removeNthFromEnd(self, head, n):
    dummy = ListNode(0)
    dummy.next = head
    fast = slow = dummy

    for _ in range(n + 1):   # fast moves n+1 steps
        fast = fast.next

    while fast:               # move both until fast is None
        fast = fast.next
        slow = slow.next

    slow.next = slow.next.next   # remove the target node
    return dummy.next
```

---

#### **Dummy Head — Other Applications**

Two more problems where the dummy is what removes the special case. The rest of the
family is worked where it belongs, not re-pasted here:

| LC | Problem | Worked in |
|---|---|---|
| 19 | Remove Nth Node From End | the visual walkthrough above, and [linked_list_examples.md](./linked_list_examples.md#9-remove-nth-node-from-end-of-list--lc-19) for both Java forms |
| 21 | Merge Two Sorted Lists | [linked_list_examples.md](./linked_list_examples.md#4-merge-two-sorted-lists--lc-21) |
| 2 | Add Two Numbers | [1-1-7) below](#1-1-7-add-2-linked-list--lc-2) |
| 203 | Remove Linked List Elements | [Remove Elements by Value Pattern](#remove-elements-by-value-pattern) below |

**Remove duplicates from a sorted list — LC 83**: the dummy holds the last *kept* node, so a
run of equal values collapses without ever special-casing a duplicated head.
```python
def deleteDuplicates(self, head):
    dummy = ListNode(0)
    dummy.next = head
    prev = dummy

    while head and head.next:
        if head.val == head.next.val:
            # Skip all duplicates
            val = head.val
            while head and head.val == val:
                head = head.next
            prev.next = head
        else:
            prev = head
            head = head.next

    return dummy.next
```

**Partition List — LC 86**: *two* dummies. Build the `< x` chain and the `>= x` chain
independently, then join them — no in-place surgery, and stability falls out for free.
```python
def partition(self, head, x):
    before_dummy = ListNode(0)
    after_dummy = ListNode(0)
    before = before_dummy
    after = after_dummy

    while head:
        if head.val < x:
            before.next = head
            before = before.next
        else:
            after.next = head
            after = after.next
        head = head.next

    # Connect the two parts
    after.next = None
    before.next = after_dummy.next
    return before_dummy.next
```
**Key Benefits of Dummy Head**:

| Aspect | Without Dummy | With Dummy |
|--------|---------------|------------|
| **Edge Cases** | Complex head handling | Unified approach |
| **Code Length** | More conditional logic | Cleaner, shorter |
| **Bug Probability** | Higher (edge cases) | Lower (consistent) |
| **Readability** | Harder to follow | More intuitive |

**Related Problems**:
- LC 19: Remove Nth Node From End of List
- LC 21: Merge Two Sorted Lists
- LC 83: Remove Duplicates from Sorted List
- LC 86: Partition List
- LC 203: Remove Linked List Elements
- LC 328: Odd Even Linked List

---

#### **Remove Elements by Value Pattern**

**Definition**: Remove all nodes from a linked list that match a specific value. Uses dummy head and a "look ahead" technique where the current pointer examines `curr.next` rather than `curr` itself.

**Core Concept**:
- **Key Insight**: When we find a node to remove, we ONLY update the pointer connection (`curr.next = curr.next.next`), but the `curr` pointer itself does NOT move forward
- This allows handling consecutive matching nodes (e.g., `[6,6,6,3]` with val=6)
- Only move `curr` forward when `curr.next.val != val`

**When to Use**:
- Removing nodes by value from anywhere in the list
- Handling cases where head node(s) might need removal
- Removing consecutive duplicate values

**Time Complexity**: O(n)
**Space Complexity**: O(1)

**Template Pattern**:
```java
// Java
public ListNode removeElements(ListNode head, int val) {
    // 1. Create dummy node pointing to head
    ListNode dummy = new ListNode(0);
    dummy.next = head;

    // 2. Use curr pointer (starts at dummy, looks ahead)
    ListNode curr = dummy;

    // 3. Look ahead at NEXT node
    while (curr.next != null) {
        if (curr.next.val == val) {
            // Found match - skip the next node
            // NOTE: curr does NOT move!
            curr.next = curr.next.next;
        } else {
            // No match - move pointer forward
            curr = curr.next;
        }
    }

    // 4. Return actual head
    return dummy.next;
}
```

```python
# Python
def removeElements(self, head: ListNode, val: int) -> ListNode:
    dummy = ListNode(0)
    dummy.next = head
    curr = dummy

    while curr.next:
        if curr.next.val == val:
            curr.next = curr.next.next  # skip, don't move curr
        else:
            curr = curr.next  # move forward

    return dummy.next
```

**Dry Run Example** (`[6,6,6,3]`, val=6):
```text
Initial: dummy -> 6 -> 6 -> 6 -> 3, curr at dummy

Step 1: curr.next.val = 6 (match!)
  Action: curr.next = curr.next.next
  Result: dummy -> 6 -> 6 -> 3 (curr STAYS at dummy)

Step 2: curr.next.val = 6 (match!)
  Action: curr.next = curr.next.next
  Result: dummy -> 6 -> 3 (curr STAYS at dummy)

Step 3: curr.next.val = 6 (match!)
  Action: curr.next = curr.next.next
  Result: dummy -> 3 (curr STAYS at dummy)

Step 4: curr.next.val = 3 (no match)
  Action: curr = curr.next
  Result: curr moves to node 3

Step 5: curr.next = null, exit loop
Return: dummy.next = [3]
```

**Why This Works for Consecutive Matches**:
| Scenario | Without "stay in place" | With "stay in place" |
|----------|------------------------|---------------------|
| `[6,6,3]` val=6 | Would skip second 6 | Catches all 6s |
| Head removal | Needs special case | Handled uniformly |

**Similar LC Problems**:
- LC 203: Remove Linked List Elements (exact pattern)
- LC 83: Remove Duplicates from Sorted List (similar, compare adjacent)
- LC 82: Remove Duplicates from Sorted List II (remove all duplicates)
- LC 237: Delete Node in a Linked List (different - no access to prev)
- LC 1474: Delete N Nodes After M Nodes (pattern variation)
- LC 2487: Remove Nodes From Linked List (stack-based variation)

---

#### **Doubly Linked List + HashMap (LRU Cache Pattern)** ⭐⭐⭐⭐⭐

**Core Idea**: Combine a HashMap for O(1) key lookup with a doubly linked list for O(1) ordered eviction. Most-recently-used nodes sit near the **tail**; least-recently-used sits near the **head**. Sentinel dummy head/tail nodes eliminate all edge-case pointer checks.

**Layout**:
```text
head(dummy) <-> [LRU] <-> ... <-> [MRU] <-> tail(dummy)
```

**When to Use**:
- Need O(1) get + O(1) put with ordered eviction (LRU/MFU)
- Any problem requiring a ordered access-tracked collection

**Time Complexity**: O(1) get and put  
**Space Complexity**: O(capacity)

**Key Helper Operations**:
- `_remove(node)` — splice a node out of the list in O(1)
- `_insert(node)` — insert a node just before tail (MRU position) in O(1)

**Template Pattern**:
```python
# python
# LC 146 - LRU Cache
class Node:
    def __init__(self, key, val):
        self.key = key
        self.val = val
        self.prev = None
        self.next = None

class LRUCache:
    def __init__(self, capacity):
        self.capacity = capacity
        self.cache = {}  # key -> Node

        # sentinel boundaries: head <-> ... <-> tail
        self.head = Node(0, 0)  # LRU side
        self.tail = Node(0, 0)  # MRU side
        self.head.next = self.tail
        self.tail.prev = self.head

    def _remove(self, node):
        prev = node.prev
        nxt  = node.next
        prev.next = nxt
        nxt.prev  = prev

    def _insert(self, node):          # insert just before tail (MRU)
        prev = self.tail.prev
        prev.next  = node
        node.prev  = prev
        node.next  = self.tail
        self.tail.prev = node

    def get(self, key):
        if key not in self.cache:
            return -1
        node = self.cache[key]
        self._remove(node)
        self._insert(node)            # move to MRU
        return node.val

    def put(self, key, value):
        if key in self.cache:
            node = self.cache[key]
            node.val = value
            self._remove(node)
            self._insert(node)        # refresh to MRU
            return

        if len(self.cache) == self.capacity:
            lru = self.head.next      # evict LRU (closest to head)
            self._remove(lru)
            del self.cache[lru.key]

        node = Node(key, value)
        self.cache[key] = node
        self._insert(node)
```

**Visual Trace** (capacity=2):
```text
put(1,1): head <-> [1] <-> tail
put(2,2): head <-> [1] <-> [2] <-> tail
get(1):   head <-> [2] <-> [1] <-> tail   ← 1 moved to MRU
put(3,3): evict head.next=[2]
          head <-> [1] <-> [3] <-> tail
```

**Why sentinel nodes?**
- `_remove` and `_insert` always have valid `.prev`/`.next` neighbors
- No `if node.prev is None` or `if node.next is None` guards needed
- Works uniformly for head removal, tail removal, and middle removal

**Similar LC Problems**:
| # | Problem | Key Difference |
|---|---------|----------------|
| 146 | LRU Cache | Classic pattern — evict least recently used |
| 460 | LFU Cache | Two-level structure: frequency map + per-freq doubly linked list |
| 432 | All O(1) Data Structure | Doubly linked list of count buckets |
| 1472 | Design Browser History | Doubly linked list, truncate forward on visit |
| 641 | Design Circular Deque | Doubly linked list with fixed capacity, both ends |
| 716 | Max Stack | Stack + doubly linked list + TreeMap for O(log n) popMax |

---

#### **Reverse K Nodes Helper Pattern** ⭐⭐⭐⭐⭐

**Core Idea**: Almost every "reverse a *segment*" problem (LC 92, LC 25, LC 24, LC 206) is the **same primitive** — reverse `k` nodes starting from a `head`, then reconnect. Factor that primitive into a single reusable helper so the outer solution only worries about **locating the segment** and **stitching the ends back together**.

The helper reverses `k` nodes and returns **three handles** you need to reconnect cleanly:

```python
# python — reusable helper: reverse k nodes starting at `head`
# time = O(k), space = O(1)
def reverse_helper(self, head, k):
    prev = None
    curr = head

    while curr and k > 0:
        nxt = curr.next     # 1) cache next
        curr.next = prev    # 2) reverse the link
        prev = curr         # 3) advance prev
        curr = nxt          # 4) advance curr
        k -= 1

    # prev = new head of reversed list   (was the k-th node)
    # head = new tail  (original head, now points forward to `curr`)
    # curr = first node AFTER the reversed segment
    return prev, head, curr
```

**Why return 3 things?** After reversing an *inner* segment you must re-wire **both boundaries**:

| Returned | What it is | Used to reconnect |
|----------|-----------|-------------------|
| `prev` (`new_head`) | new **head** of the reversed chunk | `prev_of_segment.next = new_head` |
| `head` (`new_tail`) | new **tail** (the original first node) | `new_tail.next = next_node` |
| `curr` (`next_node`) | first node **after** the segment | the tail must point here |

**When to Use**:
- Reverse a sub-range `[left, right]` (LC 92) → reverse `right - left + 1` nodes
- Reverse every k-group (LC 25) → call helper in a loop until fewer than `k` remain
- Reverse whole list (LC 206) → call helper once with `k = length` (or `k = ∞`)

**Template — apply helper to LC 92 (Reverse Linked List II)**:
```python
# python
# LC 92 - reverse nodes from position `left` to `right`
# time = O(n), space = O(1)
class Solution(object):
    def reverseBetween(self, head, left, right):
        # edge case
        if not head or left == right:
            return head

        dummy = ListNode(0)
        dummy.next = head

        # 1) walk `prev` to the node BEFORE position `left`
        prev = dummy
        for _ in range(left - 1):
            prev = prev.next

        # 2) `start` = first node of the segment to reverse
        start = prev.next

        # 3) reverse (right - left + 1) nodes via the helper
        new_head, new_tail, next_node = self.reverse_helper(
            start, right - left + 1
        )

        # 4) reconnect both boundaries
        prev.next = new_head       # front:  prev -> new head of reversed chunk
        new_tail.next = next_node  # back:   old head (now tail) -> rest of list

        return dummy.next

    def reverse_helper(self, head, k):
        prev = None
        curr = head
        while curr and k > 0:
            nxt = curr.next
            curr.next = prev
            prev = curr
            curr = nxt
            k -= 1
        return prev, head, curr
```

**Visualization** (`[1,2,3,4,5]`, `left=2`, `right=4` → reverse 3 nodes `2,3,4`):

```text
dummy -> 1 -> 2 -> 3 -> 4 -> 5
              └──── reverse these 3 ────┘

Step 1) walk prev (left-1 = 1 step) to node before segment
   dummy -> 1 -> 2 -> 3 -> 4 -> 5
            ^prev  ^start
                   (start = prev.next = node 2)

Step 2) reverse_helper(start=2, k=3)
   -- reverses links of 2,3,4 in isolation --
   before:   2 -> 3 -> 4 -> 5
   after:    2 <- 3 <- 4      5
             |              |
          new_tail       new_head
   returns:
     new_head  = 4   (was k-th node, now front of chunk)
     new_tail  = 2   (was `start`, now points nowhere yet)
     next_node = 5   (first node after the reversed part)

Step 3) reconnect boundaries
   (C1) prev.next = new_head
        node1.next -> 4
   (C2) new_tail.next = next_node
        node2.next -> 5

Final:
   dummy -> 1 -> 4 -> 3 -> 2 -> 5
                 └── reversed ──┘
   return dummy.next  =>  [1, 4, 3, 2, 5]  ✓
```

**The 3 boundary handles, visually**:
```text
        prev        new_head → ... → new_tail        next_node
          |             |                 |               |
   ... -> 1             4 -> 3 -> 2         (dangling)      5 -> ...
          |_____________|                 |_______________|
             (C1) prev.next = new_head        (C2) new_tail.next = next_node
```

**Reusing the helper for LC 25 (Reverse Nodes in k-Group)**:
```python
# python
# LC 25 - reverse every k nodes; leave the tail (< k) as-is
# time = O(n), space = O(1)
class Solution(object):
    def reverseKGroup(self, head, k):
        # count if >= k nodes remain
        def has_k(node, k):
            cnt = 0
            while node and cnt < k:
                node = node.next
                cnt += 1
            return cnt == k

        dummy = ListNode(0)
        dummy.next = head
        prev = dummy               # node before current group

        while has_k(prev.next, k):
            start = prev.next
            new_head, new_tail, next_node = self.reverse_helper(start, k)
            prev.next = new_head        # front of group
            new_tail.next = next_node   # tail of group -> rest
            prev = new_tail             # move `prev` to end of this group
        return dummy.next

    def reverse_helper(self, head, k):
        prev = None
        curr = head
        while curr and k > 0:
            nxt = curr.next
            curr.next = prev
            prev = curr
            curr = nxt
            k -= 1
        return prev, head, curr
```

> **Key insight**: the *same* `reverse_helper` powers LC 206 / 92 / 25. Only the surrounding logic differs — **206** calls it once, **92** locates one segment then calls it once, **25** loops and calls it per group. Master the 3-handle return (`new_head, new_tail, next_node`) and all three collapse into "locate → reverse → reconnect".

**Similar LC Problems**:
| # | Problem | How the helper applies |
|---|---------|------------------------|
| 206 | Reverse Linked List | One call, `k = length` — only `new_head` matters |
| 92  | Reverse Linked List II | Locate segment, one call with `k = right - left + 1`, reconnect both ends |
| 25  | Reverse Nodes in k-Group | Loop the helper per group; skip the final `< k` tail |
| 24  | Swap Nodes in Pairs | Special case `k = 2` per group |
| 61  | Rotate List | Different op, but same "locate boundary + re-stitch" discipline |

---

## 1) General form
```java
// java
// single Linklist
public class ListNode {
    int val;
    ListNode next;
    ListNode(int x) { val = x; }
}
```
```python
# python
class Node:
  """
  # constructor
  # A single node of a singly linked list
  """
  def __init__(self, data=None, next=None): 
    self.data = data
    self.next = next

class LinkedList:
  """
  # A Linked List class with a single head node
  """
  def __init__(self):  
    self.head = Node()

  def get_length(self):
    """
    # get list length method for the linked list
    i.e. 
       before : 1 -> 2 -> 3
       after  : 3
    """
    current = self.head
    length = 0 
    while current:
        current = current.next
        length += 1 
    return length

  def get_tail(self):
    """
    # get list tail method for the linked list
    i.e. 
       before : a -> b -> c
       after  : c
    """
    current = self.head
    while current:
        current = current.next
    return current

  def print(self):
    """
    # print method for the linked list
    i.e. 
       before : 1 -> 2 -> 3
       after  : 1 2 3 
    """
    current = self.head
    while current:
      print (current.data)
      current = current.next

  def append(self, data):
    """
    # append method that append a new item at the end of the linkedlist 
    i.e. 
         before :  1 -> 2 -> 3
         after  :  1 -> 2 -> 3 -> 4
    """
    newNode = Node(data)
    if self.head:
      current = self.head
      while current.next:
        current = current.next
      current.next = newNode
    else:
      self.head = newNode
  
  def prepend(self, data):
    """
    # append method that append a new item at the head of the linkedlist 
    i.e. 
         before :  1 -> 2 -> 3
         after  :  0 -> 1 -> 2 -> 3
    """
    newNode = Node(data)
    if self.head:
        current = self.head
        self.head = newNode
        newNode.next = current
        current = current.next
    else:
        self.head = newNode

  def insert(self, idx, data):
    """
    # append method that append a new item within the linkedlist 
    i.e. 
         before :  1 -> 2 -> 3
         insert(1, 2.5)
         after  :  1 -> 2 -> 2.5 -> 3
         before :  1 -> 2 -> 3
         insert(0, 0)
         after  :  0 -> 1 -> 2 -> 3
         before :  1 -> 2 -> 3
         insert(2, 4)
         after  :  1 -> 2 -> 3 -> 4
    """
    current = self.head
    ll_length = self.get_length()

    if idx < 0 or idx > self.get_length():
      print ("idx out of linkedlist range, idx : {}".format(idx))
      return
    elif idx == 0:
        self.prepend(data)
    elif idx == ll_length:
        self.append(data)
    else:
        newNode = Node(data)
        current = self.head
        cur_idx = 0 
        while cur_idx < idx-1:
            current = current.next
            cur_idx += 1 
        newNode.next = current.next
        current.next = newNode

  def remove(self, idx):
    """
    # remove method for the linked list
    i.e. 
       before : 1 -> 2 -> 3
       remove(1) 
       after  : 1 -> 3
       before : 1 -> 2 -> 3
       remove(2) 
       after  : 1 -> 2
       before : 1 -> 2 -> 3
       remove(0) 
       after  : 2 -> 3
    """
    if idx < 0 or idx > self.get_length():
        print ("idx out of linkedlist range, idx : {}".format(idx))
        return 
    elif idx == 0:
        current = self.head
        self.head = current.next
    elif idx == self.get_length():
        current = self.head
        cur_idx = 0
        while cur_idx < idx -1:
            current = current.next
            cur_idx += 1
        current.next = None
    else:
        current = self.head
        cur_idx = 0 
        while cur_idx < idx - 1:
            current = current.next
            cur_idx += 1 
        next_ = current.next.next
        current.next = next_
        current = next_ 

  def reverse(self): 
    """
    https://www.youtube.com/watch?v=D7y_hoT_YZI

    # reverse method for the linked list
    # https://www.geeksforgeeks.org/python-program-for-reverse-a-linked-list/
    i.e. 
     before : 1 -> 2 -> 3
     after  : 3 -> 2 -> 1 
    """
    prev = None
    current = self.head 
    while(current is not None): 
        next_ = current.next
        current.next = prev 
        prev = current 
        current = next_
    self.head = prev 

```

### 1-1) Basic OP


#### 1-1-1) Reverse linked list (iteration) — LC 206
```python
# python
#-------------------------
# iteration
#-------------------------
# LC 206

# V0
# IDEA : Linkedlist basics
# https://www.youtube.com/watch?v=D7y_hoT_YZI
# STEPS)
# -> STEP 1) cache "next"
# -> STEP 2) point head.next to prev
# -> STEP 3) move prev to head
# -> STEP 4) move head to "next"
class Solution(object):
    def reverseList(self, head):
        # edge case
        if not head:
            return
        prev = None
        while head:
            # cache "next"
            tmp = head.next
            # point head.next to prev
            head.next = prev
            # move prev to head (for next iteration)
            prev = head
            # move head to "next" (for next iteration)
            head = tmp
        # NOTE!!! we return prev
        return prev
```

```java
// java
//---------------------------
// iteration
//---------------------------
// LC 206
// V0
public ListNode reverseList(ListNode head) {

    if (head == null) {
        return null;
    }

    ListNode _prev = null;

    while (head != null) {
        /**
         *  NOTE !!!!
         *
         *   4 operations
         *
         *    step 1) cache next
         *    step 2) point cur to prev
         *    step 3) move prev to cur
         *    step 4) move cur to next
         *
         */
        ListNode _next = head.next;
        head.next = _prev;
        _prev = head;
        head = _next;
    }

    // NOTE!!! we return _prev here, since it's now "new head"
    return _prev;

}
```

#### 1-1-2) Reverse linked list (recursion) — LC 206
```java
// java
//---------------------------
// recursion
//---------------------------
// LC 206
// algorithm book (labu) p.290
// IDEA: recurse to the tail, then let each frame flip the link BEHIND it on the way back.
//       `newHead` is the original last node and is passed up unchanged.
// time = O(n), space = O(n)   (call stack)
ListNode reverseList(ListNode head) {
    // base case: empty list, or already at the last node
    if (head == null || head.next == null) {
        return head;
    }

    // reverse everything after `head`; newHead is the tail of the ORIGINAL list
    ListNode newHead = reverseList(head.next);

    /** NOTE !!!
     *
     *  head.next is the node that now sits BEHIND head in the reversed list,
     *  so pointing it back at head is the whole flip.
     */
    head.next.next = head;
    // cut the old forward link, or the two nodes form a 2-cycle
    head.next = null;

    return newHead;
}
```

#### 1-1-3) Reverse *nodes in [a,b]*  linked list (iteration) — LC 92
```java
// java
//---------------------------
// iteration
//---------------------------
// algorithm book (labu) p.298
ListNode reverse(ListNode a, Listnode b){
    ListNode pre, cur, nxt;
    pre = null;
    cur = a;
    nxt = a;
    /** THE ONLY DIFFERENCE (reverse nodes VS reverse nodes in [a,b]) */
    while (cur != b){
        nxt = cur.next;
        // reverse on each node
        cur.next = pre;
        // update pointer
        pre = cur;
        cur = nxt;
    }

    // return reversed nodes
    return pre;
}
```

#### 1-1-4) Reverse *nodes in k group*  linked list (iteration) — LC 25
```java
// java
//---------------------------
// iteration
//---------------------------
// LC 25
// algorithm book (labu) p.298
/** NOTE !!! `reverse(a, b)` is exactly the primitive from 1-1-3) above — reverse the
 *  half-open interval [a, b) and return its new head. Reproduced there, not here. */
ListNode reverseKGroup(ListNode head, int k){
    if (head == null) return null;
    // inverval [a,b] has k to-reverse elements
    ListNode a, b;
    a = b = head;
    for (int i = 0; i < k; i++){
        // not enough elements (amount < k), no need to reverse -> base case
        if (b == null) return head;
        b = b.next;
    }
    // reverse k elements
    ListNode newHead = reverse(a,b);
    // reverse remaining nodes, and connect with head
    a.next = reverseKGroup(b,k);
    return newHead;
}
```
```python
# LC 025
class Solution:
    def reverseKGroup(self, head, k):
        # help func
        # check if # of sub nodes still > k
        def check(head, k):
            ans = 0
            while head:
                ans += 1
                if ans >= k:
                    return True
                head = head.next
            return False

        # edge case
        if not head:
            return
        d = dummy = ListNode(None)
        pre = None
        preHead = curHead = head
        while check(curHead, k):
            for _ in range(k):
                # reverse linked list
                tmp = curHead.next
                curHead.next = pre
                pre = curHead
                curHead = tmp
            # reverse linked list
            # ???
            dummy.next = pre
            dummy = preHead
            preHead.next = curHead
            preHead = curHead
        return d.next
```

#### 1-1-5) Reverse *first N*  linked list (recursion)
```java
//---------------------------
// recursion
//---------------------------
// java
// algorithm book (labu) p.293

// "postorder" node
ListNode successor = null;

// reverse first N node (from head), and return new head
ListNode reverseN(ListNode head, int n){
    if (n == 1){
        // record n + 1 nodes, will be used in following steps
        successor = head.next;
        return head;
    }

    // set head.next as start point, return first n - 1 nodes
    ListNode last = reverseN(head.next, n-1);

    head.next.next = head;
    // connect reversed head node and following nodes
    head.next = successor;
    return last;
}
```

#### 1-1-6) Reverse *middle N nodes* in linked list (*start, end* as interval) (recursion) — LC 92
```java
// java
//---------------------------
// recursion
//---------------------------
// algorithm book (labu) p.293

/** NOTE !!! `reverseN` is the primitive from 1-1-5) above, unchanged. Only the
 *  `reverseBetween` wrapper below is new: walk m down to 1, then reverse the first n. */
// reverse nodes in index = m to index = n
ListNode reverseBetween(ListNode head, int m, int n){
    // base case
    if (m == 1){
        return reverseN(head, n);
    }

    // for head.next, the op is reverse interval : [m-1, n-1]
    // will trigger base case when when meet reverse start point
    head.next = reverseBetween(head.next, m - 1, n - 1);
    return head;
}
```

#### 1-1-7) add 2 linked list — LC 2
```python
# LC 002
class Solution(object):
    def addTwoNumbers(self, l1, l2):
        """
        NOTE :
         1. we init linkedlist via ListNode()
         2. we NEED make extra head refer same linkedlist, since we need to return beginning of linkedlist of this func, while res will meet "tail" at the end of while loop
        """
        head = res = ListNode()
        plus = 0
        tmp = 0
        while l1 or l2:
            tmp += plus
            plus = 0
            if l1:
                tmp += l1.val
                l1 = l1.next
            if l2:
                tmp += l2.val
                l2 = l2.next
            if tmp > 9:
                tmp -= 10
                plus = 1

            res.next = ListNode(tmp)
            res = res.next
            tmp = 0
        ### NOTE : need to deal with case : l1, l2 are completed, but still "remaining" plus
        if plus != 0:
            res.next = ListNode(plus)
            res = res.next
        #print ("res = " + str(res))
        #print ("head = " + str(head))
        return head.next
```

```python
# LC 445 Add Two Numbers II
# V0
# IDEA : string + linked list
# DEMO
# input :
# [7,2,4,3]
# [5,6,4]
# intermedia output : 
# l1_num = 7243
# l2_num = 564
class Solution:
    def addTwoNumbers(self, l1, l2):
        if not l1 and not l2:
            return None

        l1_num = 0
        while l1:
            l1_num = l1_num * 10 + l1.val
            l1 = l1.next

        l2_num = 0
        while l2:
            l2_num = l2_num * 10 + l2.val
            l2 = l2.next

        print ("l1_num = " + str(l1_num))
        print ("l2_num = " + str(l2_num))


        ### NOTE : trick here :
        #    -> get int format of 2 linked list first (l1, l2)
        #    -> then sum them (l1_num + l2_num)
        lsum = l1_num + l2_num

        head = ListNode(None)
        cur = head
        ### NOTE : go thrpigh the linked list int sum, append each digit to ListNode and return it
        for istr in str(lsum):
            cur.next = ListNode(int(istr))
            cur = cur.next
        # NOTE : need to return head (but not cur, since cur already meet the end of ListNode)
        return head.next
```

#### 1-1-8) Find linked list middle point — LC 876
```java
// algorithm book p. 286
// java
Listnode slow, fast;
slow = fast = head;
while (fast && fast.next){
    fast = fast.next.next;
    slow = slow.next;
}
// slow pointer will be linked list middle point

// if element count in linked list is odd (TO VERIFY)
if (fast != null){
    slow = slow.next;
}
```

```python
# LC 876 Middle of the Linked List
# V0
# IDEA : fast, slow pointers + linkedlist
class Solution(object):
    def middleNode(self, head):
        # edge case
        if not head:
            return
        s = f = head
        while f and f.next:
            # if not f:
            #     break
            f = f.next.next
            s = s.next
        return s
```


## 2) Pattern Selection

Linked-list problems are rarely *about* lists. They are about **which handle you have to be
holding** when the surgery happens — and every technique on this sheet exists to make sure you
are holding it. Pick by what the answer needs, not by the problem's title.

| If the problem asks you to… | Reach for | Because | Written out at |
|---|---|---|---|
| delete or insert **anywhere, head included** | **dummy head** | it gives `prev` a standing position one node *before* the head, so "remove the head" stops being a special case | [Dummy Head Technique](#dummy-head-technique) |
| remove **every** node matching a value | **dummy + look-ahead on `curr.next`** | you have to be able to *stay put* after a deletion, or a run like `[6,6,6]` loses one | [Remove Elements by Value Pattern](#remove-elements-by-value-pattern) |
| reverse the **whole** list | **the 3-step loop**: cache next → flip → advance | O(1) space; the recursive form costs a frame per node for the same answer | [1-1-1)](#1-1-1-reverse-linked-list-iteration--lc-206) |
| reverse a **segment** — `[left, right]`, every `k`, or pairs | **the reverse-k helper, returning 3 handles** | LC 92 / 25 / 24 differ only in *where the segment is*, never in how it reverses | [Reverse K Nodes Helper Pattern](#reverse-k-nodes-helper-pattern-) |
| find the middle, detect a cycle, or reach the n-th **from the end** | **fast/slow pointers** | one pass, O(1) space, and no length to precompute | [1-1-8)](#1-1-8-find-linked-list-middle-point--lc-876), [2_pointers_linkedlist.md](./2_pointers_linkedlist.md) |
| **reorder** — interleave, split, rotate, palindrome-check | **split with fast/slow → reverse the back half → merge** | every reorder problem is those three primitives in sequence; none of them is new | [examples 2)](./linked_list_examples.md#2-reorder-list--lc-143), [7)](./linked_list_examples.md#7-palindrome-linked-list--lc-234) |
| merge **two** sorted lists | **dummy + a merge walk**, splicing nodes rather than copying values | the tail pointer is the whole trick: `cur.next = l1 or l2` finishes it | [examples 4)](./linked_list_examples.md#4-merge-two-sorted-lists--lc-21) |
| merge **k** sorted lists, or sort one list | **divide and conquer** — pairwise merge, or merge sort via the middle | O(n log k) / O(n log n); a heap trades the recursion for O(k) space | [examples 5)](./linked_list_examples.md#5-merge-k-sorted-lists--lc-23), [14)](./linked_list_examples.md#14-sort-list-merge-sort-on-a-linked-list--lc-148-), [heap.md](./heap.md) |
| do **arbitrary-position** reads *and* O(1) eviction | **doubly linked list + hash map** | the map gives you the node, the doubly-linked node gives you its neighbours — neither alone is enough | [Doubly Linked List + HashMap](#doubly-linked-list--hashmap-lru-cache-pattern-), [design.md](./design.md) |
| do **arithmetic** on digits stored as a list | **carry loop over a dummy**, reversing first if the list is most-significant-first | the carry outlives both inputs, so the loop condition is `l1 or l2 or carry` | [1-1-7)](#1-1-7-add-2-linked-list--lc-2), [examples 13)](./linked_list_examples.md#13-plus-one-linked-list--lc-369) |
| answer a question that needs **random access or a window** | **dump to an array first, then use the array technique** | prefix sums and monotonic stacks need indices; a list has none, and O(n) extra space is usually allowed | [examples 15)](./linked_list_examples.md#15-prefix-sum--hashmap-on-a-linked-list--lc-1171-), [16)](./linked_list_examples.md#16-monotonic-stack-over-a-linked-list--lc-1019-) |

### The four traps

1. **Losing the list.** `curr.next = prev` before caching `curr.next` throws away everything
   downstream. Cache first — that is why the reversal loop is written in that order.
2. **Returning the wrong head.** After any operation that can touch the first node, return
   `dummy.next`, not `head`: `head` may no longer be in the list.
3. **Leaving a cycle behind.** In the recursive reversal, `head.next.next = head` without the
   following `head.next = null` leaves the last two nodes pointing at each other.
4. **Advancing past the end.** `while (fast != null && fast.next != null)` for a two-step hop.
   Getting the two clauses in the wrong order dereferences null on an even-length list.

## 3) Worked Examples

The full solutions moved to **[linked_list_examples.md](./linked_list_examples.md)** so the
templates above are not buried under them. Seventeen problems, grouped by the technique each
one exercises:

| Group | Problems |
|---|---|
| [Reversal & reordering](./linked_list_examples.md#reversal--reordering) | LC 92, 143, 24 |
| [Merging & splitting](./linked_list_examples.md#merging--splitting) | LC 21, 23, 725 |
| [Fast/slow pointers & structure](./linked_list_examples.md#fastslow-pointers--structure) | LC 234, 160, 19 |
| [Copying, flattening & components](./linked_list_examples.md#copying-flattening--components) | LC 138, 817, 430 |
| [Arithmetic & sorting](./linked_list_examples.md#arithmetic--sorting-on-a-list) | LC 369, 148, 147 |
| [Array techniques borrowed onto a list](./linked_list_examples.md#array-techniques-borrowed-onto-a-list) | LC 1171, 1019 |
