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

**Recognise** — the input is a `ListNode head`, and the answer is a re-wired list or one
node of it. The follow-up is almost always "in O(1) extra memory", which rules out copying
the values into an array.

**Key point** — a singly linked node only knows what comes *after* it. To delete or insert at
a node you must be standing on the node **before** it (`cur.next = cur.next.next`), so every
technique below is a way of making sure you hold the right handle when the surgery happens.

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
    - LC 146, LC 460
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

### Recognition

| Signal in the statement | Constraint cue | Reach for | Not this if |
|---|---|---|---|
| "reverse the linked list" | O(1) extra memory | [Template 1](#template-1-iterative-reversal--lc-206-) iterative reversal | only a range or every k nodes is reversed → [Template 4](#template-4-reverse-a-segment-reconnect-both-ends--lc-92-) |
| "remove all nodes with value …", "delete duplicates", the head may be deleted | one pass | [Template 2](#template-2-dummy-head-deletion--lc-203-) dummy head | you are handed only the node to delete, not the head (LC 237) → copy the next value in |
| "all nodes less than x come before …", "odd indices then even", "preserve the relative order" | O(1) extra memory | [Template 3](#template-3-two-dummies-split-then-join--lc-86-) two dummies | order inside a group may change → an array partition is enough |
| "reverse nodes from position left to right", "in groups of k", "swap every two adjacent nodes" | one pass, O(1) memory | [Template 4](#template-4-reverse-a-segment-reconnect-both-ends--lc-92-) segment reversal | the whole list is reversed → Template 1 |
| "middle node", "n-th node from the end", "split in half", "reorder", "palindrome" | one pass, length unknown | [Template 5](#template-5-fastslow-middle--lc-876-) fast/slow | "does it cycle / where does the cycle start" → [2_pointers_linkedlist.md](./2_pointers_linkedlist.md) |
| "digits stored in reverse order", "add the two numbers" | lists up to 100 nodes, so no fixed-width integer fits | [Template 6](#template-6-carry-loop-over-a-dummy--lc-2-) carry loop | most significant digit first (LC 445) → reverse both first, or use two stacks |
| "design a cache", "get and put must run in O(1)", "evict the least recently used" | O(1) per operation | [Template 7](#template-7-hash-map--doubly-linked-list-lru--lc-146-) hash map + doubly linked list | evict by frequency (LC 460) → [the LFU variant](#lfu-variant--one-list-per-frequency-plus-a-min_freq-pointer-lc-460-) |

## 1) General form

Every template below runs on the `ListNode` defined above. They are ordered the way an
interview builds on them: reversal first, then the dummy head, then the techniques that
combine the two, and the cache designs last.

### Template 1: Iterative Reversal — LC 206 ⭐⭐⭐⭐⭐

**Recognise** — "reverse a singly linked list", or any problem whose plan contains "reverse the
second half" (LC 143, LC 234). Follow-up: O(1) extra memory, so iterate rather than recurse.

**Key point** — `prev` is always the head of the already-reversed part and `curr` the head of
the untouched part; caching `curr.next` before the flip is what keeps the untouched part
reachable.

```python
# python
# LC 206 - Reverse Linked List
# IDEA: walk once; at each node cache next, flip the link back to prev, advance both
# time = O(n), space = O(1)
class Solution(object):
    def reverseList(self, head):
        prev = None              # head of the reversed part (empty at first)
        curr = head              # head of the part not yet reversed
        while curr:
            nxt = curr.next      # 1) cache next: the flip below overwrites it
            curr.next = prev     # 2) flip the link
            prev = curr          # 3) advance prev
            curr = nxt           # 4) advance curr
        return prev              # curr is None, so prev is the old tail = new head
```

```java
// java
// LC 206 - Reverse Linked List
// IDEA: walk once; at each node cache next, flip the link back to prev, advance both
// time = O(n), space = O(1)
public ListNode reverseList(ListNode head) {
    ListNode prev = null;        // head of the reversed part
    ListNode curr = head;        // head of the part not yet reversed
    while (curr != null) {
        ListNode next = curr.next;   // 1) cache next
        curr.next = prev;            // 2) flip the link
        prev = curr;                 // 3) advance prev
        curr = next;                 // 4) advance curr
    }
    return prev;                 // the old tail is the new head
}
```

**Complexity**
- **Time O(n)**: the `while curr` loop visits each node once and does four assignments there.
- **Space O(1)**: only `prev`, `curr` and `nxt`; the nodes are re-linked, never copied.

**Dry run**

```text
1 -> 2 -> 3
step  prev        curr   reversed part
0     None        1      (empty)
1     1           2      1
2     2 -> 1      3      2 -> 1
3     3 -> 2 -> 1 None   return prev
```

**Traps**
- `curr.next = prev` before `nxt = curr.next` loses the rest of the list: `1 -> 2 -> 3` returns `1`.
- Returning `head` instead of `prev`: `head` is now the *tail*, so `[1,2,3]` returns `[1]`.
- No special case is needed for `[]` or `[1]`: the loop runs zero or one times and `prev` is right.

**Not this if** — only part of the list is reversed (a range, every k nodes, pairs) → the
segment reversal in [Template 4](#template-4-reverse-a-segment-reconnect-both-ends--lc-92-),
which is this loop run k times plus two reconnections.

**Drill** — LC 206 ⭐ · LC 234 *(reverse the back half, compare)* · LC 143 ⭐ *(middle, reverse, interleave)* · LC 92 *(a range: Template 4)*

#### Recursive form — O(n) stack

The same answer, top-down: reverse everything after `head`, then hang `head` after the node
that used to follow it. It costs a stack frame per node, so it is the second answer, not the first.

```python
# python
# LC 206 - Reverse Linked List (recursive)
# IDEA: reverse the rest first; head.next is then the rest's tail, so hang head after it
# time = O(n), space = O(n)   call stack
class Solution(object):
    def reverseList(self, head):
        if head is None or head.next is None:   # empty, or the last node: already reversed
            return head
        new_head = self.reverseList(head.next)  # the original last node, passed up unchanged
        head.next.next = head                   # the node after head now points back at it
        head.next = None                        # cut the old forward link, or 2 nodes form a cycle
        return new_head
```

```java
// java
// LC 206 - Reverse Linked List (recursive)
// IDEA: recurse to the tail, then let each frame flip the link BEHIND it on the way back.
//       `newHead` is the original last node and is passed up unchanged.
// time = O(n), space = O(n)   (call stack)
public ListNode reverseList(ListNode head) {
    // base case: empty list, or already at the last node
    if (head == null || head.next == null) {
        return head;
    }
    // reverse everything after `head`; newHead is the tail of the ORIGINAL list
    ListNode newHead = reverseList(head.next);
    // head.next now sits BEHIND head in the reversed list: point it back at head
    head.next.next = head;
    // cut the old forward link, or the two nodes form a 2-cycle
    head.next = null;
    return newHead;
}
```

### Template 2: Dummy Head Deletion — LC 203 ⭐⭐⭐⭐⭐

**Recognise** — "remove every node with value …", "delete duplicates", "remove the n-th node",
or any edit where the **head itself** may be removed or replaced. Without a dummy, each of
these needs an `if head is the target` branch.

**Key point** — the dummy gives `curr` a standing position *before* the head, and `curr` only
ever looks at `curr.next`. After a deletion `curr` stays put, so a run of matches (`[6,6,6]`)
is removed one link at a time without skipping any.

```python
# python
# LC 203 - Remove Linked List Elements
# IDEA: dummy before head; look ahead at curr.next; on a match skip it and do NOT advance
# time = O(n), space = O(1)
class Solution(object):
    def removeElements(self, head, val):
        dummy = ListNode(0)
        dummy.next = head
        curr = dummy                       # always the last node known to be kept
        while curr.next:
            if curr.next.val == val:
                curr.next = curr.next.next # skip the match; curr stays, the new next is unchecked
            else:
                curr = curr.next           # next is kept, so move onto it
        return dummy.next                  # the head may have been removed
```

```java
// java
// LC 203 - Remove Linked List Elements
// IDEA: dummy before head; look ahead at curr.next; on a match skip it and do NOT advance
// time = O(n), space = O(1)
public ListNode removeElements(ListNode head, int val) {
    ListNode dummy = new ListNode(0);
    dummy.next = head;
    ListNode curr = dummy;              // always the last node known to be kept
    while (curr.next != null) {
        if (curr.next.val == val) {
            curr.next = curr.next.next; // skip the match; curr does not move
        } else {
            curr = curr.next;           // no match: move forward
        }
    }
    return dummy.next;
}
```

**Complexity**
- **Time O(n)**: every iteration of `while curr.next` either removes a node or advances `curr`,
  so there are at most n iterations in total.
- **Space O(1)**: one dummy node and one cursor.

**Dry run** (`[6,6,6,3]`, val=6)

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

**Traps**
- Advancing `curr` after a deletion skips the node that slid into `curr.next`: `[6,6,3]`, val=6,
  returns `[6,3]`.
- Returning `head` instead of `dummy.next`: `[7,7,7]`, val=7, returns the deleted `7` instead of `[]`.
- Testing `curr.val` instead of `curr.next.val`: by the time you see the match you are standing
  on it, and a singly linked node cannot reach the node before it.

**Not this if** — you are handed only the node to delete, never the head (LC 237): there is no
`prev` to stand on, so copy `node.next.val` into it and delete the next node instead.

**Drill** — LC 203 · LC 83 *(compare adjacent values)* · LC 19 ⭐ *(a gap of n: worked below)* · LC 82 *(drop every copy: worked below)* · LC 1474

#### **Why Dummy Node? Visual Comparison (LC 19)**

> **Key point** — with `fast` started n + 1 steps ahead of `slow`, `slow` stops on the node
> *before* the target. Starting both on the dummy makes that true even when the target is the head.

**Reduces to**: [Template 2](#template-2-dummy-head-deletion--lc-203-) — the dummy, plus a
fixed gap between two pointers from [Template 5](#template-5-fastslow-middle--lc-876-).

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
# python
# LC 19 - Remove Nth Node From End of List
# IDEA: start fast and slow on the dummy, open a gap of n + 1, walk both until fast falls off
# time = O(n), space = O(1)
class Solution(object):
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

#### Remove Duplicates from Sorted List II — LC 82

> **Key point** — `prev` is the last node known to be kept. When `head` starts a run of equal
> values, skip the whole run and hang what follows on `prev`; the dummy makes a duplicated head
> just another run.

**Reduces to**: [Template 2](#template-2-dummy-head-deletion--lc-203-) — the deletion test
compares a node with its neighbour instead of with a fixed value.

```python
# python
# LC 82 - Remove Duplicates from Sorted List II
# IDEA: dummy holds the last kept node; a run of equal values is skipped as a whole
# time = O(n), space = O(1)
class Solution(object):
    def deleteDuplicates(self, head):
        dummy = ListNode(0)
        dummy.next = head
        prev = dummy                       # last node that is definitely kept

        while head and head.next:
            if head.val == head.next.val:
                # skip every node of this value
                val = head.val
                while head and head.val == val:
                    head = head.next
                prev.next = head           # prev does not move: head is still unchecked
            else:
                prev = head
                head = head.next

        return dummy.next
```

**Traps**
- This removes **every** copy (`[1,1,2]` → `[2]`). LC 83 keeps one copy (`[1,1,2]` → `[1,2]`);
  there no dummy is needed, because the head is never deleted.

### Template 3: Two Dummies, Split then Join — LC 86 ⭐⭐⭐⭐

**Recognise** — every node goes into one of two groups and each group must **keep its
original order**: "all nodes less than x come before nodes greater than or equal to x … preserve
the relative order" (LC 86), "group the odd indices together followed by the even" (LC 328).

**Key point** — each chain has a dummy (a fixed handle on its head) and a tail pointer; every
node is appended to exactly one tail, so after one pass `p1.next = dummy_2.next` joins them
without ever tracking chain 2's first node.

| Handle | What it is | Needed for |
|---|---|---|
| `dummy_1` | sits before the head of chain 1 | the answer — `return dummy_1.next` |
| `p1` | the **tail** of chain 1 | the join: chain 1's tail must point at chain 2's head |
| `dummy_2` | sits before the head of chain 2 | the join: `dummy_2.next` **is** chain 2's head |
| `p2` | the **tail** of chain 2 | termination: `p2.next = None` |

```python
# python
# LC 86 - Partition List
# IDEA: two dummies -> append each node to the tail of its chain, then
#       terminate chain 2 and hang it off chain 1's tail via dummy_2.next
# time = O(n), space = O(1)
class Solution(object):
    def partition(self, head, x):
        dummy_1 = ListNode(0)   # chain 1: values <  x
        dummy_2 = ListNode(0)   # chain 2: values >= x
        p1 = dummy_1            # tail of chain 1
        p2 = dummy_2            # tail of chain 2

        curr = head
        while curr:
            if curr.val < x:
                p1.next = curr
                p1 = p1.next
            else:
                p2.next = curr
                p2 = p2.next
            curr = curr.next

        # terminate chain 2: p2 may still point at a node now in chain 1
        p2.next = None

        # join: dummy_2.next is chain 2's head, so it never has to be tracked
        p1.next = dummy_2.next

        return dummy_1.next
```

```java
// java
// LC 86 - Partition List
// IDEA: two dummies, one tail per chain; cut chain 2's tail, then join chain 1's tail to it
// time = O(n), space = O(1)
public ListNode partition(ListNode head, int x) {
    ListNode dummy1 = new ListNode(0);   // chain 1: values <  x
    ListNode dummy2 = new ListNode(0);   // chain 2: values >= x
    ListNode p1 = dummy1;
    ListNode p2 = dummy2;

    ListNode curr = head;
    while (curr != null) {
        if (curr.val < x) {
            p1.next = curr;
            p1 = p1.next;
        } else {
            p2.next = curr;
            p2 = p2.next;
        }
        curr = curr.next;
    }

    p2.next = null;            // terminate chain 2, or the result can loop
    p1.next = dummy2.next;     // join: chain 1's tail -> chain 2's head

    return dummy1.next;
}
```

**Complexity**
- **Time O(n)**: the single `while curr` pass appends each node to one tail; the join is O(1).
- **Space O(1)**: the nodes are re-linked, never copied; only the two dummies are new.

**Dry run** (`[1,4,3,2,5,2]`, `x = 3`)

```text
node  goes to   chain 1 (< 3)        chain 2 (>= 3)
 1    chain 1   d1 -> 1              d2
 4    chain 2   d1 -> 1              d2 -> 4
 3    chain 2   d1 -> 1              d2 -> 4 -> 3
 2    chain 1   d1 -> 1 -> 2         d2 -> 4 -> 3
 5    chain 2   d1 -> 1 -> 2         d2 -> 4 -> 3 -> 5
 2    chain 1   d1 -> 1 -> 2 -> 2    d2 -> 4 -> 3 -> 5
                          ^p1                       ^p2

p2.next = None        5.next was still the last 2 -> cut it
p1.next = d2.next     2 -> 4

d1 -> 1 -> 2 -> 2 -> 4 -> 3 -> 5   return d1.next  =>  [1,2,2,4,3,5]
```

**Traps**
- **Forgetting `p2.next = None` makes a cycle.** The nodes are re-linked, not copied, so chain
  2's last node still carries its *old* `next`. In the trace that is `5 -> 2`, and the final 2
  sits in chain 1 *before* 5 — a loop. Only chain 2 needs the cut: chain 1's tail is about to be
  overwritten by the join anyway.
- **Join with `dummy_2.next`, not `dummy_2`.** The dummy is a placeholder value `0`; hanging the
  dummy itself off `p1` inserts a phantom node.
- **Empty groups need no guard.** All nodes `< x`: `dummy_2.next` is `None`, and the join just
  re-terminates chain 1. All nodes `>= x`: `p1` is still `dummy_1`, so the join makes
  `dummy_1.next` chain 2's head. Empty input returns `None` the same way.

**Not this if** — the order inside a group may change, or the input is an array: LC 75's
in-place Dutch-flag swap is cheaper there, but it is not stable, and LC 86 requires the original
relative order within each group.

| # | Problem | The predicate that picks the chain |
|---|---------|-----------------------------------|
| 86 | Partition List | `node.val < x` — the template above |
| 328 | Odd Even Linked List | the node's **index** is even — the same two dummies, `p2.next = None`, `p1.next = dummy_2.next` |
| 2161 | Partition Array According to Given Pivot | the array form: three buckets (`<`, `==`, `>`), concatenated in order |
| 21 | Merge Two Sorted Lists | the reverse move — one dummy, *two* inputs, one chain out |

**Drill** — LC 86 · LC 21 ⭐ *(one dummy, two inputs)* · LC 328 *(split by index)* · LC 725 *(k chains)*

### Template 4: Reverse a Segment, Reconnect Both Ends — LC 92 ⭐⭐⭐⭐⭐

**Recognise** — "reverse the nodes from position left to right" (LC 92), "reverse the nodes k
at a time" (LC 25), "swap every two adjacent nodes" (LC 24). Follow-up: one pass, O(1) memory.

**Key point** — reversing k nodes from `start` leaves three handles: the new front (the k-th
node), the new tail (`start` itself) and the first node after the segment. Reconnecting is
always `prev.next = new_head` and `new_tail.next = next_node`; the problems differ only in
**where** the segment is.

| Returned | What it is | Used to reconnect |
|----------|-----------|-------------------|
| `prev` (`new_head`) | new **head** of the reversed chunk | `prev_of_segment.next = new_head` |
| `head` (`new_tail`) | new **tail** (the original first node) | `new_tail.next = next_node` |
| `curr` (`next_node`) | first node **after** the segment | the tail must point here |

```python
# python
# LC 92 - Reverse Linked List II
# IDEA: walk prev to the node before `left`, reverse right-left+1 nodes with the helper,
#       then reconnect both boundaries with the three handles it returns
# time = O(n), space = O(1)
class Solution(object):
    def reverseBetween(self, head, left, right):
        dummy = ListNode(0)
        dummy.next = head

        # 1) walk `prev` to the node BEFORE position `left`
        prev = dummy
        for _ in range(left - 1):
            prev = prev.next

        # 2) reverse (right - left + 1) nodes starting at prev.next
        new_head, new_tail, next_node = self.reverse_helper(prev.next, right - left + 1)

        # 3) reconnect both boundaries
        prev.next = new_head       # front:  prev -> new head of reversed chunk
        new_tail.next = next_node  # back:   old head (now tail) -> rest of list

        return dummy.next

    def reverse_helper(self, head, k):
        """reverse k nodes from head; return (new_head, new_tail, first node after)"""
        prev = None
        curr = head
        while curr and k > 0:
            nxt = curr.next     # 1) cache next
            curr.next = prev    # 2) reverse the link
            prev = curr         # 3) advance prev
            curr = nxt          # 4) advance curr
            k -= 1
        return prev, head, curr
```

```java
// java
// LC 92 - Reverse Linked List II
// IDEA: walk prev to the node before `left`, reverse right-left+1 nodes in place,
//       then reconnect: prev -> new front, old start (now the tail) -> the node after
// time = O(n), space = O(1)
public ListNode reverseBetween(ListNode head, int left, int right) {
    ListNode dummy = new ListNode(0);
    dummy.next = head;

    ListNode prev = dummy;
    for (int i = 0; i < left - 1; i++) {
        prev = prev.next;               // the node BEFORE the segment
    }

    ListNode start = prev.next;         // becomes the segment's tail
    ListNode reversed = null;           // front of the reversed segment
    ListNode curr = start;
    for (int i = 0; i < right - left + 1; i++) {
        ListNode next = curr.next;
        curr.next = reversed;
        reversed = curr;
        curr = next;
    }
    // curr is now the first node AFTER the segment
    prev.next = reversed;               // front
    start.next = curr;                  // back
    return dummy.next;
}
```

**Complexity**
- **Time O(n)**: the walk to `left` and the k-step reversal together touch each node at most once.
- **Space O(1)**: a fixed number of pointers; the helper is a loop, not recursion.

**Dry run** (`[1,2,3,4,5]`, `left=2`, `right=4` → reverse 3 nodes `2,3,4`)

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

**Traps**
- Walking `prev` from `head` instead of `dummy`: with `left = 1` there is no node before the
  segment, so `[1,2,3]`, left=1, right=2 cannot reconnect its front.
- Forgetting `new_tail.next = next_node` loses the tail `5`: the helper already set the old
  start's `next` to `None`, so the result is `1 -> 4 -> 3 -> 2 -> None`, not a cycle.
- In LC 25, calling the helper on a final group shorter than k reverses it too; count k nodes
  first (`has_k`) and leave a short tail as it is.

**Not this if** — the whole list is reversed → [Template 1](#template-1-iterative-reversal--lc-206-);
the list is *rotated* (LC 61), not reversed → find the new tail with a gap and re-stitch, no
reversal at all.

**Drill** — LC 92 · LC 206 ⭐ *(k = length)* · LC 24 *(k = 2, unrolled: below)* · LC 25 *(loop the helper: below)* · LC 61

#### Reverse Nodes in k-Group — LC 25

> **Key point** — after each group, the group's old head is its new tail, and that is exactly
> the node that must own the incoming link for the next group: `prev = new_tail`.

**Reduces to**: [Template 4](#template-4-reverse-a-segment-reconnect-both-ends--lc-92-) — the
same helper, called once per full group of k.

```python
# python
# LC 25 - Reverse Nodes in k-Group
# IDEA: while k nodes remain after prev, reverse them with the helper and reconnect;
#       a final group shorter than k is left as it is
# time = O(n), space = O(1)
class Solution(object):
    def reverseKGroup(self, head, k):
        def has_k(node, k):
            # are there at least k nodes starting at node?
            count = 0
            while node and count < k:
                node = node.next
                count += 1
            return count == k

        dummy = ListNode(0)
        dummy.next = head
        prev = dummy               # node before the current group

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

#### Swap Nodes in Pairs — LC 24

> **Key point** — at k = 2 the helper's three handles become local names: `second` is the new
> front, `first` the new tail, and `second.next` the node after. `prev` is the only cursor.

**Reduces to**: [Template 4](#template-4-reverse-a-segment-reconnect-both-ends--lc-92-) — the
k = 2 case, unrolled, because reversing two nodes is two assignments.

| The helper returns | At `k = 2` it is | Which is written as |
|---|---|---|
| `new_head` | `second` | `prev.next = second` |
| `new_tail` | `first` | `prev = first` — the next iteration's `prev` |
| `next_node` | `second.next` | folded straight into `first.next = second.next` |

```python
# python
# LC 24 - Swap Nodes in Pairs
# IDEA: the k = 2 case of the helper above, unrolled — only the reconnection survives.
#       `prev` is the ONLY cursor: both members of the pair are reachable from it,
#       so there is no second `head` walker to keep in step.
# time = O(n), space = O(1)
class Solution(object):
    def swapPairs(self, head):
        dummy = ListNode(0)
        dummy.next = head
        prev = dummy                  # node BEFORE the pair

        # prev.next and prev.next.next ARE the pair -> no separate cursor needed
        while prev.next and prev.next.next:
            first = prev.next         # becomes the pair's tail  (new_tail)
            second = first.next       # becomes the pair's front (new_head)

            # reverse the 2-node segment
            first.next = second.next  # (A) carry `next_node` straight into the new tail
            second.next = first       # (B) flip the pair

            # reconnect the front
            prev.next = second        # (C) prev adopts the new front

            # prev = new_tail: after the swap `first` sits before the next pair
            prev = first

        return dummy.next
```

```text
input: 1 -> 2 -> 3 -> 4

dummy -> 1 -> 2 -> 3 -> 4
  ^prev  ^first
              ^second

(A) first.next  = second.next     # 1 -> 3
(B) second.next = first           # 2 -> 1
(C) prev.next   = second          # dummy -> 2

dummy -> 2 -> 1 -> 3 -> 4

prev = first

dummy -> 2 -> 1 -> 3 -> 4
              ^prev
                   ^first
                        ^second   <- next iteration reads the pair off prev again

... repeat on (3, 4); then prev.next.next is None -> stop
return dummy.next  =>  2 -> 1 -> 4 -> 3
```

**Traps**
- Running `(B)` before `(A)` loses `second.next`: `[1,2,3,4]` returns `[2,1]`.
- No guard is needed for `[]` or `[1]`: `prev.next.next` is already `None`, so the loop never runs.

The full walkthrough — the three anchors, the per-iteration dry run and the recursive form — is in
[linked_list_examples.md 3)](./linked_list_examples.md#3-swap-nodes-in-pairs--lc-24).

#### Recursive form — reverse the first n, then a range

The recursive version of the same segment reversal. `reverseN` reverses the first `n` nodes
and remembers the node after them in `successor`; `reverseBetween` walks `left` down to 1 and
then calls it. It is O(n) stack, so it is the second answer.

```java
// java
// LC 92 - Reverse Linked List II (recursive)
// IDEA: reverseN(head, n) reverses the first n nodes and reattaches `successor` (node n + 1);
//       reverseBetween shifts the window one node per call until it starts at position 1
// time = O(n), space = O(n)   (call stack)
ListNode successor = null;   // node n + 1, recorded by the base case

ListNode reverseN(ListNode head, int n) {
    if (n == 1) {
        successor = head.next;   // the first node after the reversed part
        return head;
    }
    ListNode last = reverseN(head.next, n - 1);
    head.next.next = head;       // the node after head now points back at it
    head.next = successor;       // head is the new tail: reattach the rest
    return last;
}

ListNode reverseBetween(ListNode head, int m, int n) {
    if (m == 1) {
        return reverseN(head, n);
    }
    // for head.next the range is [m - 1, n - 1]
    head.next = reverseBetween(head.next, m - 1, n - 1);
    return head;
}
```

### Template 5: Fast/Slow Middle — LC 876 ⭐⭐⭐⭐

**Recognise** — "return the middle node", "split the list into two halves" (LC 143, LC 148,
LC 234), "n-th node from the end" — anything that needs a position measured from the end or
the middle of a list whose length is not given.

**Key point** — `fast` moves two nodes for each one `slow` moves, so when `fast` cannot take
another full step, `slow` has covered half the distance: it stands on the middle.

```python
# python
# LC 876 - Middle of the Linked List
# IDEA: fast takes two steps per slow step; when fast runs out, slow is at the middle
# time = O(n), space = O(1)
class Solution(object):
    def middleNode(self, head):
        slow = head
        fast = head
        while fast and fast.next:   # fast can take two more steps
            slow = slow.next
            fast = fast.next.next
        return slow                 # even length: the SECOND of the two middles
```

```java
// java
// LC 876 - Middle of the Linked List
// IDEA: fast takes two steps per slow step; when fast runs out, slow is at the middle
// time = O(n), space = O(1)
public ListNode middleNode(ListNode head) {
    ListNode slow = head;
    ListNode fast = head;
    while (fast != null && fast.next != null) {
        slow = slow.next;
        fast = fast.next.next;
    }
    return slow;   // even length: the second middle
}
```

**Complexity**
- **Time O(n)**: `fast` reaches the end in n / 2 iterations of the `while`.
- **Space O(1)**: two pointers.

**Dry run**

```text
[1,2,3,4,5]          [1,2,3,4,5,6]
slow fast            slow fast
1    1               1    1
2    3               2    3
3    5   stop        3    5
                     4    None   stop
return 3             return 4  (the second middle)
```

**Traps**
- `while fast.next and fast` dereferences `None` on an even-length list; test `fast` first.
- To split a list for merge sort (LC 148) or reorder (LC 143) you need the **first** middle, so
  the left half is never longer: loop on `while fast.next and fast.next.next`. With the
  second-middle form, `[1,2]` never splits and merge sort recurses forever.

**Not this if** — "does the list have a cycle / where does it start" (LC 141, LC 142) uses the
same two speeds but a different stopping rule → [2_pointers_linkedlist.md](./2_pointers_linkedlist.md);
"n-th from the end" uses equal speeds with a gap of n → [the LC 19 card](#why-dummy-node-visual-comparison-lc-19).

**Drill** — LC 876 · LC 141 ⭐ *(cycle: same speeds, stop when they meet)* · LC 234 *(middle + reverse)* · LC 143 ⭐ *(first middle, reverse, interleave)* · LC 148 *(first middle, merge sort)*

### Template 6: Carry Loop over a Dummy — LC 2 ⭐⭐⭐

**Recognise** — "the digits are stored in reverse order and each node contains a single digit;
add the two numbers". The lists can be 100 nodes long, so the numbers do not fit in any
fixed-width integer: add digit by digit.

**Key point** — each step writes `(a + b + carry) % 10` and carries `// 10`; the loop runs while
**either list or the carry** is non-empty, because the carry can outlive both inputs.

```python
# python
# LC 2 - Add Two Numbers
# IDEA: walk both lists at once, append the digit sum mod 10 to a dummy-headed result, carry the rest
# time = O(max(m, n)), space = O(1) besides the output
class Solution(object):
    def addTwoNumbers(self, l1, l2):
        dummy = ListNode(0)
        tail = dummy                 # last node of the result
        carry = 0
        while l1 or l2 or carry:     # the carry can outlive both lists
            total = carry
            if l1:
                total += l1.val
                l1 = l1.next
            if l2:
                total += l2.val
                l2 = l2.next
            carry = total // 10
            tail.next = ListNode(total % 10)
            tail = tail.next
        return dummy.next
```

```java
// java
// LC 2 - Add Two Numbers
// IDEA: walk both lists at once, append the digit sum mod 10 to a dummy-headed result, carry the rest
// time = O(max(m, n)), space = O(1) besides the output
public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
    ListNode dummy = new ListNode(0);
    ListNode tail = dummy;
    int carry = 0;
    while (l1 != null || l2 != null || carry != 0) {
        int total = carry;
        if (l1 != null) {
            total += l1.val;
            l1 = l1.next;
        }
        if (l2 != null) {
            total += l2.val;
            l2 = l2.next;
        }
        carry = total / 10;
        tail.next = new ListNode(total % 10);
        tail = tail.next;
    }
    return dummy.next;
}
```

**Complexity**
- **Time O(max(m, n))**: the `while` takes one digit from each list per step, plus at most one
  step for a final carry.
- **Space O(1)** besides the result list, which has max(m, n) + 1 nodes at most.

**Traps**
- Looping on `while l1 and l2` drops the longer list's tail: `[9,9,9]` + `[1]` returns `[0]`.
- Looping on `while l1 or l2` drops a final carry: `[5]` + `[5]` returns `[0]` instead of `[0,1]`.
- Converting each list to an integer works in Python but overflows `long` in Java on a 100-digit
  input; it is not the answer an interviewer is looking for.

**Not this if** — the most significant digit comes first (LC 445): reverse both lists with
[Template 1](#template-1-iterative-reversal--lc-206-) and reverse the result back, or push the
digits onto two stacks and build the result by prepending.

**Drill** — LC 2 · LC 369 *(add one)* · LC 445 *(most significant first)*

### Template 7: Hash Map + Doubly Linked List (LRU) — LC 146 ⭐⭐⭐⭐⭐

**Recognise** — "design a cache", "`get` and `put` must each run in O(1) average time",
"evict the least recently used key". Any structure that needs both O(1) lookup by key and an
O(1)-maintained order.

**Key point** — the map takes a key to its node, and the node, being doubly linked, can unlink
itself in O(1). Recency is the list order: the most recent node sits just before `tail`, so
the victim is always `head.next`.

```text
head(dummy) <-> [LRU] <-> ... <-> [MRU] <-> tail(dummy)
```

**Key helper operations** — the whole class is these two, called in pairs:
- `remove(node)` — splice a node out of the list in O(1); needs only the node, because it carries both neighbours
- `add_to_tail(node)` — insert a node just before the dummy tail (MRU position) in O(1)
- "touch" a key = `remove(node)` then `add_to_tail(node)` — used by `get` **and** by `put` on an existing key

```python
# python
# LC 146 - LRU Cache
# IDEA: map key -> node for O(1) lookup; the doubly linked list holds recency order,
#       MRU just before `tail`, LRU just after `head`. put inserts first, then evicts
#       `head.next` if the map has grown past capacity.
# time = O(1) per get/put, space = O(capacity)
class Node:
    def __init__(self, key=0, val=0):
        self.key = key        # kept so eviction can delete the map entry
        self.val = val
        self.prev = None
        self.next = None

class LRUCache:
    def __init__(self, capacity):
        self.capacity = capacity
        self.kv_map = {}  # key -> Node

        # sentinel boundaries: head <-> ... <-> tail
        self.head = Node()  # LRU side
        self.tail = Node()  # MRU side
        self.head.next = self.tail
        self.tail.prev = self.head

    def remove(self, node):
        prev_node = node.prev
        next_node = node.next
        prev_node.next = next_node
        next_node.prev = prev_node

    def add_to_tail(self, node):      # insert just before tail (MRU)
        prev_node = self.tail.prev
        prev_node.next = node
        node.prev = prev_node
        node.next = self.tail
        self.tail.prev = node

    def get(self, key):
        if key not in self.kv_map:
            return -1
        node = self.kv_map[key]
        self.remove(node)
        self.add_to_tail(node)        # move to MRU
        return node.val

    def put(self, key, value):
        # case 1) key exists: update in place, refresh to MRU — never evicts
        if key in self.kv_map:
            node = self.kv_map[key]
            node.val = value
            self.remove(node)
            self.add_to_tail(node)
            return

        # case 2) new key: insert first ...
        node = Node(key, value)
        self.kv_map[key] = node
        self.add_to_tail(node)

        # ... then evict the LRU (the first REAL node, not the dummy) if over capacity
        if len(self.kv_map) > self.capacity:
            lru_node = self.head.next
            self.remove(lru_node)
            del self.kv_map[lru_node.key]
```

```java
// java
// LC 146 - LRU Cache
// IDEA: map key -> node; a sentinel doubly linked list keeps recency order (MRU before tail).
//       put inserts first, then evicts head.next if the map has grown past capacity.
// time = O(1) per get/put, space = O(capacity)
class LRUCache {
    private static class Node {
        int key;
        int val;
        Node prev;
        Node next;

        Node(int key, int val) {
            this.key = key;   // kept so eviction can delete the map entry
            this.val = val;
        }
    }

    private final int capacity;
    private final Map<Integer, Node> map = new HashMap<>();
    private final Node head = new Node(0, 0);   // LRU side
    private final Node tail = new Node(0, 0);   // MRU side

    public LRUCache(int capacity) {
        this.capacity = capacity;
        head.next = tail;
        tail.prev = head;
    }

    private void remove(Node node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    private void addToTail(Node node) {   // insert just before tail (MRU)
        node.prev = tail.prev;
        node.next = tail;
        tail.prev.next = node;
        tail.prev = node;
    }

    public int get(int key) {
        Node node = map.get(key);
        if (node == null) {
            return -1;
        }
        remove(node);
        addToTail(node);                  // move to MRU
        return node.val;
    }

    public void put(int key, int value) {
        Node node = map.get(key);
        if (node != null) {               // existing key: update and refresh, never evict
            node.val = value;
            remove(node);
            addToTail(node);
            return;
        }
        node = new Node(key, value);
        map.put(key, node);
        addToTail(node);
        if (map.size() > capacity) {      // evict the first REAL node
            Node lru = head.next;
            remove(lru);
            map.remove(lru.key);
        }
    }
}
```

**Complexity**
- **Time O(1) per call**: `get` and `put` do one map lookup plus a constant number of pointer
  assignments in `remove` / `add_to_tail`; nothing walks the list.
- **Space O(capacity)**: the map and the list each hold at most capacity + 1 entries, and only
  for the moment between the insert and the eviction.

**Dry run** (capacity=2, LC 146 Example 1)

```text
put(1,1): head <-> [1] <-> tail
put(2,2): head <-> [1] <-> [2] <-> tail
get(1):   head <-> [2] <-> [1] <-> tail          ← 1 moved to MRU, returns 1
put(3,3): head <-> [2] <-> [1] <-> [3] <-> tail  ← size 3 > 2
          evict head.next=[2]
          head <-> [1] <-> [3] <-> tail
get(2):   -1
put(4,4): evict head.next=[1]
          head <-> [3] <-> [4] <-> tail
get(1) = -1, get(3) = 3, get(4) = 4
```

**Traps**
- **The node must store its `key`.** Eviction reaches the victim through `head.next` and must
  then delete its map entry; without `node.key` the map keeps a stale entry, `len(kv_map)` never
  shrinks, and `get` returns a node that is no longer in the list.
- **Evicting `head` instead of `head.next`** removes the sentinel: the next `remove` dereferences
  `None`.
- **Evicting before the existing-key check** drops an entry on a plain update: capacity 2,
  `put(1,1) put(2,2) put(1,10)` loses key 2.
- **Mixing orientations** — adding at the tail but evicting `tail.prev` — evicts the key just touched.

| | Insert, then evict (template above) | Evict, then insert |
|---|---|---|
| Capacity test | `len(kv_map) > capacity` after inserting | `len(kv_map) == capacity` before inserting |
| Victim | `head.next` — the new node is at the tail, so never itself | `head.next` |
| The trap | none extra | the eviction must sit **inside** the new-key branch; evicting before the existing-key check drops an entry on a plain update |

**Not this if** — eviction is by **frequency**, recency only breaking ties (LC 460) → one list per
frequency, below. In an interview you may also name the library form first: Python's
`OrderedDict` (`move_to_end`, `popitem(last=False)`) and Java's `LinkedHashMap` (access order +
`removeEldestEntry`) *are* this structure — worked in
[design_examples.md 1)](./design_examples.md#1-lru-cache--lc-146-).

| # | Problem | Key Difference |
|---|---------|----------------|
| 146 | LRU Cache | Classic pattern — evict least recently used |
| 460 | LFU Cache | Two-level structure: one LRU list per frequency + `min_freq` — [worked below](#lfu-variant--one-list-per-frequency-plus-a-min_freq-pointer-lc-460-) |
| 432 | All O(1) Data Structure | Doubly linked list of count buckets |
| 1472 | Design Browser History | Doubly linked list, truncate forward on visit |
| 641 | Design Circular Deque | Doubly linked list with fixed capacity, both ends |
| 716 | Max Stack | Stack + doubly linked list + TreeMap for O(log n) popMax |

**Drill** — LC 146 ⭐ · LC 1472 *(one cursor, truncate forward)* · LC 641 *(fixed capacity, both ends)* · LC 460 *(one list per frequency: below)* · LC 432

#### **LFU Variant — one list per frequency, plus a `min_freq` pointer (LC 460)** ⭐⭐⭐⭐

> **Key point** — every per-frequency list is the LRU list above; `min_freq` names the list the
> victim lives in, and it only ever moves by `+1` on a touch or resets to `1` on an insert.

**Reduces to**: [Template 7](#template-7-hash-map--doubly-linked-list-lru--lc-146-) — the same
sentinel list, once per frequency.

**What changed**: LRU evicts by *recency*, so one list is enough — its `head.next` is always the
victim. LFU evicts by *frequency*, and recency only breaks ties — so the one list becomes **one
LRU list per frequency**, and an integer `min_freq` names the list the victim lives in. Each
per-frequency list is exactly the sentinel list from the template above; none of the pointer
surgery is new, only the bookkeeping around it.

**Layout**:
```text
key_to_node  : key  -> Node(key, val, freq)
freq_to_list : freq -> head(dummy) <-> [LRU] <-> ... <-> [MRU] <-> tail(dummy)
min_freq     : the smallest freq whose list is non-empty

freq 1: head <-> [c] <-> tail                <- min_freq = 1, victim = this list's head.next
freq 2: head <-> [a] <-> [b] <-> tail        <- a is older than b
freq 5: head <-> [d] <-> tail
```

**The three moves** — every operation is one or two of these, in this order:

| Move | What it does | Where `min_freq` goes |
|---|---|---|
| **touch(node)** — `get`, and `put` on an existing key | unlink from `freq_to_list[f]`, `f += 1`, append to the tail of `freq_to_list[f]` | if the list just left is now empty **and** `f == min_freq`: `min_freq += 1`. Exact, not a search — the node itself just landed in `f + 1`, so that list is non-empty |
| **evict** — `put` on a new key, cache full | the victim is `freq_to_list[min_freq].head.next`; unlink it and delete its map entry | untouched — the insert that follows resets it |
| **insert** — `put` on a new key | a new `Node(key, value, 1)` appended to `freq_to_list[1]` | `min_freq = 1`, every time — a brand-new key is the least frequent by definition |

**Template Pattern**:
```python
# python
# LC 460 - LFU Cache
# IDEA: the LRU template, once per frequency. key_to_node finds the node in O(1);
#       freq_to_list[f] is a sentinel doubly linked list of every key seen f times,
#       LRU at head.next and MRU before tail; min_freq names the list the victim is in.
# time = O(1) per get/put, space = O(capacity)
class Node:
    def __init__(self, key=0, val=0, freq=0):
        self.key = key            # for eviction to delete the map entry (as in LRU)
        self.val = val
        self.freq = freq          # and freq, to know which list to unlink from
        self.prev = None
        self.next = None

class DLinkedList:
    """the LRU template's list: sentinel head/tail, O(1) unlink and append"""
    def __init__(self):
        self.head = Node()        # LRU side
        self.tail = Node()        # MRU side
        self.head.next = self.tail
        self.tail.prev = self.head
        self.size = 0

    def remove(self, node):
        node.prev.next = node.next
        node.next.prev = node.prev
        self.size -= 1

    def add_to_tail(self, node):  # insert just before tail (MRU)
        prev_node = self.tail.prev
        prev_node.next = node
        node.prev = prev_node
        node.next = self.tail
        self.tail.prev = node
        self.size += 1

    def pop_head(self):           # the LRU node of this frequency
        node = self.head.next
        self.remove(node)
        return node

class LFUCache:
    def __init__(self, capacity):
        self.capacity = capacity
        self.key_to_node = {}     # key  -> Node
        self.freq_to_list = {}    # freq -> DLinkedList
        self.min_freq = 0

    def touch(self, node):
        """move node from its freq list to the freq+1 list, keeping min_freq exact"""
        old_list = self.freq_to_list[node.freq]
        old_list.remove(node)
        if old_list.size == 0:
            del self.freq_to_list[node.freq]       # keep the map tidy
            # the minimum list just emptied, and this node is about to land one up
            if self.min_freq == node.freq:
                self.min_freq += 1

        node.freq += 1
        if node.freq not in self.freq_to_list:
            self.freq_to_list[node.freq] = DLinkedList()
        self.freq_to_list[node.freq].add_to_tail(node)

    def get(self, key):
        if key not in self.key_to_node:
            return -1
        node = self.key_to_node[key]
        self.touch(node)
        return node.val

    def put(self, key, value):
        if self.capacity == 0:
            return

        # case 1) key exists: update in place, count the access — never evicts
        if key in self.key_to_node:
            node = self.key_to_node[key]
            node.val = value
            self.touch(node)
            return

        # case 2) new key and the cache is full: evict the LRU node of the least-frequent list
        if len(self.key_to_node) == self.capacity:
            victim_list = self.freq_to_list[self.min_freq]
            victim = victim_list.pop_head()
            del self.key_to_node[victim.key]
            if victim_list.size == 0:
                del self.freq_to_list[self.min_freq]

        # case 3) insert at freq 1 — a brand-new key is the least frequent by definition
        node = Node(key, value, 1)
        self.key_to_node[key] = node
        if 1 not in self.freq_to_list:
            self.freq_to_list[1] = DLinkedList()
        self.freq_to_list[1].add_to_tail(node)
        self.min_freq = 1
```

**Visual Trace** (capacity=2, LC 460 Example 1; each list shown LRU → MRU):
```text
put(1,1): f1: [1]                              min_freq=1
put(2,2): f1: [1, 2]                           min_freq=1
get(1):   f1: [2]       f2: [1]                -> 1   f1 still non-empty, min_freq stays 1
put(3,3): full -> evict f1.head.next = [2]
          f1: [3]       f2: [1]                min_freq=1  (reset by the insert)
get(2):   -1
get(3):   f1: []        f2: [1, 3]             -> 3   f1 emptied AND was the min -> min_freq=2
put(4,4): full -> evict f2.head.next = [1]     1 and 3 both have freq 2; 1 is the older
          f1: [4]       f2: [3]                min_freq=1
get(1) = -1, get(3) = 3, get(4) = 4
```

**Pitfalls — the ones that cost the O(1)**:
- **`min_freq += 1` is exact, never a scan.** The only way the minimum list empties mid-run is
  its last node moving to `min_freq + 1`, so the new minimum is known without looking. A
  `min(freq_to_list)` here makes every touch O(#distinct frequencies).
- **`min_freq = 1` on every new insert**, not only when the cache was empty: the new key is
  the least frequent no matter what was there.
- **Evict before insert, and only on the new-key path.** `put` on an existing key is a touch,
  never an eviction — the same trap as LRU's "evict, then insert" order.
- **The `capacity == 0` guard is load-bearing.** Without it the first `put` evicts from
  `freq_to_list[0]`, which does not exist.
- **The tie-break is recency *inside* one list.** Append at the tail, evict at the head; mixing
  ends evicts the key just touched, exactly as in LRU.
- **The node stores `freq` as well as `key`.** `key` is for eviction to reach the map;
  `freq` is for a touch to reach the list it must leave.

**Library shortcut**: one `OrderedDict` per frequency — `popitem(last=False)` is `pop_head`,
`move_to_end` is the append — and in Java one `LinkedHashSet<Integer>` per frequency. That form
is worked in [design_examples.md 2)](./design_examples.md#2-lfu-cache--lc-460-); the structure
pairing that gets you there is the table in [design.md](./design.md). LC 432 goes one step
further and threads the *buckets themselves* on a doubly linked list, so there is no
`min_freq` integer to maintain at all.

---

## 2) Pattern Selection

Linked-list problems are rarely *about* lists. They are about **which handle you have to be
holding** when the surgery happens — and every technique on this sheet exists to make sure you
are holding it. Pick by what the answer needs, not by the problem's title.

| If the problem asks you to… | Reach for | Because | Written out at |
|---|---|---|---|
| delete or insert **anywhere, head included** | **dummy head** | it gives `prev` a standing position one node *before* the head, so "remove the head" stops being a special case | [Template 2](#template-2-dummy-head-deletion--lc-203-) |
| remove **every** node matching a value | **dummy + look-ahead on `curr.next`** | you have to be able to *stay put* after a deletion, or a run like `[6,6,6]` loses one | [Template 2](#template-2-dummy-head-deletion--lc-203-) |
| **split** the nodes into two groups, each keeping its order — by value or by index | **two dummies, one tail per chain, then join** | `p1.next = dummy_2.next` joins without ever tracking chain 2's head; `p2.next = None` stops the cycle | [Template 3](#template-3-two-dummies-split-then-join--lc-86-) |
| reverse the **whole** list | **the 3-step loop**: cache next → flip → advance | O(1) space; the recursive form costs a frame per node for the same answer | [Template 1](#template-1-iterative-reversal--lc-206-) |
| reverse a **segment** — `[left, right]`, every `k`, or pairs | **the reverse-k helper, returning 3 handles** | LC 92 / 25 / 24 differ only in *where the segment is*, never in how it reverses | [Template 4](#template-4-reverse-a-segment-reconnect-both-ends--lc-92-) |
| find the middle, detect a cycle, or reach the n-th **from the end** | **fast/slow pointers** | one pass, O(1) space, and no length to precompute | [Template 5](#template-5-fastslow-middle--lc-876-), [2_pointers_linkedlist.md](./2_pointers_linkedlist.md) |
| **reorder** — interleave, split, rotate, palindrome-check | **split with fast/slow → reverse the back half → merge** | every reorder problem is those three primitives in sequence; none of them is new | [examples 2)](./linked_list_examples.md#2-reorder-list--lc-143), [7)](./linked_list_examples.md#7-palindrome-linked-list--lc-234) |
| merge **two** sorted lists | **dummy + a merge walk**, splicing nodes rather than copying values | the tail pointer is the whole trick: `cur.next = l1 or l2` finishes it | [examples 4)](./linked_list_examples.md#4-merge-two-sorted-lists--lc-21) |
| merge **k** sorted lists, or sort one list | **divide and conquer** — pairwise merge, or merge sort via the middle | O(n log k) / O(n log n); a heap trades the recursion for O(k) space | [examples 5)](./linked_list_examples.md#5-merge-k-sorted-lists--lc-23), [14)](./linked_list_examples.md#14-sort-list-merge-sort-on-a-linked-list--lc-148-), [heap.md](./heap.md) |
| do **arbitrary-position** reads *and* O(1) eviction — by recency, or by frequency | **doubly linked list + hash map** — one list, or one per frequency | the map gives you the node, the doubly-linked node gives you its neighbours — neither alone is enough | [Template 7](#template-7-hash-map--doubly-linked-list-lru--lc-146-), [LFU Variant](#lfu-variant--one-list-per-frequency-plus-a-min_freq-pointer-lc-460-), [design.md](./design.md) |
| do **arithmetic** on digits stored as a list | **carry loop over a dummy**, reversing first if the list is most-significant-first | the carry outlives both inputs, so the loop condition is `l1 or l2 or carry` | [Template 6](#template-6-carry-loop-over-a-dummy--lc-2-), [examples 13)](./linked_list_examples.md#13-plus-one-linked-list--lc-369) |
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
templates above are not buried under them, grouped by the technique each one exercises:

| Group | Problems |
|---|---|
| [Reversal & reordering](./linked_list_examples.md#reversal--reordering) | LC 92, 143, 24 |
| [Merging & splitting](./linked_list_examples.md#merging--splitting) | LC 21, 23, 725 |
| [Fast/slow pointers & structure](./linked_list_examples.md#fastslow-pointers--structure) | LC 234, 160, 19 |
| [Copying, flattening & components](./linked_list_examples.md#copying-flattening--components) | LC 138, 817, 430 |
| [Arithmetic & sorting](./linked_list_examples.md#arithmetic--sorting-on-a-list) | LC 369, 148, 147 |
| [Array techniques borrowed onto a list](./linked_list_examples.md#array-techniques-borrowed-onto-a-list) | LC 1171, 1019 |

## 4) Drill Path

| Stage | Problems (easy → hard) | What it adds |
|---|---|---|
| **Core** ⭐ | LC 206 · LC 21 · LC 141 · LC 19 · LC 143 · LC 146 · LC 23 | reversal, one dummy and a merge walk, fast/slow (cycle and gap), the three reorder primitives together, the cache design, then k-way merging |
| **Practice** | LC 203 · LC 876 · LC 83 · LC 234 · LC 2 · LC 86 · LC 92 · LC 24 · LC 328 · LC 82 · LC 445 · LC 148 | each template once on its anchor problem, then a variation of it |
| **Stretch** | LC 138 · LC 25 · LC 460 · LC 432 | copy with a node map; reverse per group; one list per frequency; buckets on a list |
