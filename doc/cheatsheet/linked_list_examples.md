# Linked List — Worked Examples

> **Scope** — The problem set behind [linked_list.md](./linked_list.md): each problem filed under the core template it reduces to, as a problem card that names the one step it adds; the templates themselves, and their anchor problems, live in the core sheet.
> **See also**: [linked_list.md](./linked_list.md) — the seven templates these problems reduce to; [2_pointers_linkedlist.md](./2_pointers_linkedlist.md) — cycle detection, intersection and the palindrome check, worked there; [heap.md](./heap.md) — the k-way merge of LC 23; [hash_map.md](./hash_map.md) — the prefix-sum map behind LC 1171 and the node map behind LC 138; [monotonic_stack.md](./monotonic_stack.md) — the stack behind LC 1019.

## LeetCode Problem Lists

- [Linked List](https://leetcode.com/problem-list/linked-list/)
- [Doubly-Linked List](https://leetcode.com/problem-list/doubly-linked-list/)

## Overview

[linked_list.md](./linked_list.md) holds the templates — iterative reversal, the dummy head,
two dummies, segment reversal, fast/slow, the carry loop and the hash map plus doubly linked
list — each worked on its anchor problem. This sheet is the problems that apply them, one
section per template in the same order. Each card says which template it reduces to and the one
step it adds; a problem worked in another sheet is a pointer row, not a second copy.

## Index

| LC | Problem | Reduces to | Difficulty | Where |
|---|---|---|---|---|
| 143 | Reorder List | [linked_list § Template 1](./linked_list.md#template-1-iterative-reversal--lc-206-) | Medium | [card](#reorder-list--lc-143) |
| 1019 | Next Greater Node In Linked List | [monotonic_stack § Template 1](./monotonic_stack.md#template-1-next-greater-element-decreasing-stack--lc-496) | Medium | [card](#next-greater-node-in-linked-list--lc-1019) |
| 19 | Remove Nth Node From End of List | [linked_list § Template 2](./linked_list.md#template-2-dummy-head-deletion--lc-203-) | Medium | [card](#remove-nth-node-from-end-of-list--lc-19) |
| 82 | Remove Duplicates from Sorted List II | [linked_list § Template 2](./linked_list.md#template-2-dummy-head-deletion--lc-203-) | Medium | worked in [2_pointers_linkedlist § 2-2](./2_pointers_linkedlist.md#2-2-remove-duplicates-from-sorted-list-ii--lc-82-) |
| 147 | Insertion Sort List | [linked_list § Template 2](./linked_list.md#template-2-dummy-head-deletion--lc-203-) | Medium | [card](#insertion-sort-list--lc-147) |
| 1171 | Remove Zero Sum Consecutive Nodes from Linked List | [linked_list § Template 2](./linked_list.md#template-2-dummy-head-deletion--lc-203-) + [hash_map § Template 4](./hash_map.md#template-4-prefix-sum-count-map--lc-560-) | Medium | [card](#remove-zero-sum-consecutive-nodes-from-linked-list--lc-1171) |
| 21 | Merge Two Sorted Lists | [linked_list § Template 3](./linked_list.md#template-3-two-dummies-split-then-join--lc-86-) | Easy | [card](#merge-two-sorted-lists--lc-21) |
| 725 | Split Linked List in Parts | [linked_list § Template 3](./linked_list.md#template-3-two-dummies-split-then-join--lc-86-) | Medium | [card](#split-linked-list-in-parts--lc-725) |
| 817 | Linked List Components | [linked_list § Template 3](./linked_list.md#template-3-two-dummies-split-then-join--lc-86-) | Medium | [card](#linked-list-components--lc-817) |
| 23 | Merge k Sorted Lists | [heap § Template 3](./heap.md#template-3-k-way-merge--lc-23-) | Hard | worked in heap.md |
| 92 | Reverse Linked List II | [linked_list § Template 4](./linked_list.md#template-4-reverse-a-segment-reconnect-both-ends--lc-92-) | Medium | the anchor, worked in linked_list.md |
| 24 | Swap Nodes in Pairs | [linked_list § Template 4](./linked_list.md#template-4-reverse-a-segment-reconnect-both-ends--lc-92-) | Medium | [card](#swap-nodes-in-pairs--lc-24) |
| 430 | Flatten a Multilevel Doubly Linked List | [linked_list § Template 4](./linked_list.md#template-4-reverse-a-segment-reconnect-both-ends--lc-92-) | Medium | [card](#flatten-a-multilevel-doubly-linked-list--lc-430-) |
| 25 | Reverse Nodes in k-Group | [linked_list § Template 4](./linked_list.md#template-4-reverse-a-segment-reconnect-both-ends--lc-92-) | Hard | [card](#reverse-nodes-in-k-group--lc-25) |
| 160 | Intersection of Two Linked Lists | [2_pointers_linkedlist § 1-1-5](./2_pointers_linkedlist.md#1-1-5--intersection-merge-point-of-2-linked-lists) | Easy | worked in 2_pointers_linkedlist.md |
| 234 | Palindrome Linked List | [linked_list § Template 5](./linked_list.md#template-5-fastslow-middle--lc-876-) | Easy | worked in [2_pointers_linkedlist § 1-1-6](./2_pointers_linkedlist.md#1-1-6--palindrome-linked-list-find-mid---reverse-2nd-half---compare) |
| 142 | Linked List Cycle II | [2_pointers_linkedlist § 1-1-2](./2_pointers_linkedlist.md#1-1-2--return-the-ring-start-point-of-circular-linked-list) | Medium | worked in 2_pointers_linkedlist.md |
| 148 | Sort List | [linked_list § Template 5](./linked_list.md#template-5-fastslow-middle--lc-876-) | Medium | worked in [advanced_divide_and_conquer § LC 148](./advanced_divide_and_conquer.md#variation-lc-148--sort-list) |
| 369 | Plus One Linked List | [linked_list § Template 6](./linked_list.md#template-6-carry-loop-over-a-dummy--lc-2-) | Medium | [card](#plus-one-linked-list--lc-369) |
| 146 | LRU Cache | [linked_list § Template 7](./linked_list.md#template-7-hash-map--doubly-linked-list-lru--lc-146-) | Medium | the anchor, worked in linked_list.md |
| 138 | Copy List with Random Pointer | [hash_map § Template 8](./hash_map.md#template-8-old-to-new-node-map--lc-133) | Medium | [card](#copy-list-with-random-pointer--lc-138) |
| 460 | LFU Cache | [linked_list § Template 7, LFU variant](./linked_list.md#lfu-variant--one-list-per-frequency-plus-a-min_freq-pointer-lc-460-) | Hard | worked in linked_list.md |

## Iterative Reversal Examples

### Reorder List — LC 143

> **Key point** — the answer is the first half interleaved with the *reversed* second half. Cut
> after the **first** middle so the two halves share no node, reverse the back half, then take
> one node from each in turn; the back half is never longer, so the loop runs while it lasts.

**Reduces to**: [linked_list § Template 1](./linked_list.md#template-1-iterative-reversal--lc-206-)
— run on the back half only, found with [Template 5](./linked_list.md#template-5-fastslow-middle--lc-876-)'s
first-middle loop, then an interleave walk.

```python
# python
# LC 143 - Reorder List
# IDEA: first middle (fast/slow) -> cut -> reverse the back half -> interleave the two halves
# time = O(n), space = O(1)
class Solution(object):
    def reorderList(self, head):
        if not head or not head.next:
            return
        # 1) first middle: the left half is never the shorter one
        slow = fast = head
        while fast.next and fast.next.next:
            slow = slow.next
            fast = fast.next.next

        # 2) cut after the middle, then reverse the back half (Template 1)
        second = slow.next
        slow.next = None
        prev = None
        while second:
            nxt = second.next
            second.next = prev
            prev = second
            second = nxt

        # 3) interleave: one node from the front half, one from the reversed back half
        first, second = head, prev
        while second:
            first_next = first.next      # cache both successors before relinking
            second_next = second.next
            first.next = second
            second.next = first_next
            first = first_next
            second = second_next
```

```java
// java
// LC 143 - Reorder List
// IDEA: first middle (fast/slow) -> cut -> reverse the back half -> interleave the two halves
// time = O(n), space = O(1)
public void reorderList(ListNode head) {
    if (head == null || head.next == null) {
        return;
    }
    ListNode slow = head, fast = head;
    while (fast.next != null && fast.next.next != null) {
        slow = slow.next;
        fast = fast.next.next;
    }

    ListNode second = slow.next;
    slow.next = null;                       // cut: the halves must not share a node
    ListNode prev = null;
    while (second != null) {
        ListNode next = second.next;
        second.next = prev;
        prev = second;
        second = next;
    }

    ListNode first = head;
    second = prev;
    while (second != null) {
        ListNode firstNext = first.next;
        ListNode secondNext = second.next;
        first.next = second;
        second.next = firstNext;
        first = firstNext;
        second = secondNext;
    }
}
```

**Dry run** (`[1,2,3,4,5]`)

```text
first middle:   slow = 3          front = 1 -> 2 -> 3     back = 4 -> 5
reverse back:                     back  = 5 -> 4
interleave:     1 -> 5 -> 2 -> 4 -> 3
```

**Traps**
- Skipping the cut `slow.next = None`: on `[1,2,3,4]` node 2 still points at 3, the reversed
  back half ends at 3 as well, and the interleave closes a cycle `3 -> 3`.
- Reading `first.next` *after* `first.next = second`: the front half is lost after one step.

**Similar** — LC 234 *(middle + reverse, then compare instead of interleave)* · LC 2130 *(middle + reverse, then pair the sums)*

### Next Greater Node In Linked List — LC 1019

> **Key point** — the stack holds the indices still waiting for a larger value, their values
> decreasing from bottom to top. A new value pops every smaller one and is their answer; what is
> left at the end has none, so it stays `0`. The list is only *read*, so copy it to an array
> first: the stack needs indices.

**Reduces to**: [monotonic_stack § Template 1](./monotonic_stack.md#template-1-next-greater-element-decreasing-stack--lc-496)
— the one change is a [Template 1](./linked_list.md#template-1-iterative-reversal--lc-206-)-style
single walk that turns the list into an array.

```python
# python
# LC 1019 - Next Greater Node In Linked List
# IDEA: list -> array, then a decreasing stack of indices; a larger value answers every index it pops
# time = O(n), space = O(n)
class Solution(object):
    def nextLargerNodes(self, head):
        vals = []
        while head:
            vals.append(head.val)
            head = head.next

        res = [0] * len(vals)           # 0 = no greater node
        stack = []                      # indices, values decreasing bottom -> top
        for i, v in enumerate(vals):
            while stack and vals[stack[-1]] < v:
                res[stack.pop()] = v
            stack.append(i)
        return res
```

```java
// java
// LC 1019 - Next Greater Node In Linked List
// IDEA: list -> array, then a decreasing stack of indices; a larger value answers every index it pops
// time = O(n), space = O(n)
public int[] nextLargerNodes(ListNode head) {
    List<Integer> vals = new ArrayList<>();
    for (ListNode cur = head; cur != null; cur = cur.next) {
        vals.add(cur.val);
    }
    int n = vals.size();
    int[] res = new int[n];                      // 0 = no greater node
    Deque<Integer> stack = new ArrayDeque<>();   // indices, values decreasing
    for (int i = 0; i < n; i++) {
        while (!stack.isEmpty() && vals.get(stack.peek()) < vals.get(i)) {
            res[stack.pop()] = vals.get(i);
        }
        stack.push(i);
    }
    return res;
}
```

**Dry run** (`[2,7,4,3,5]`)

```text
i=0 v=2  stack=[0]
i=1 v=7  pop 0 (res[0]=7)                    stack=[1]
i=2 v=4                                      stack=[1,2]
i=3 v=3                                      stack=[1,2,3]
i=4 v=5  pop 3 (res[3]=5), pop 2 (res[2]=5)  stack=[1,4]
res = [7,0,5,5,0]
```

**Traps**
- Popping on `<=` instead of `<`: an equal value is not *greater*, so `[1,1,2]` would give
  `[1,2,0]` instead of `[2,2,0]`.
- Pushing values instead of indices: you can no longer say *whose* answer a pop is.

## Dummy Head Deletion Examples

### Remove Nth Node From End of List — LC 19

> **Key point** — start `fast` and `slow` on the dummy and move `fast` n + 1 steps first; when
> `fast` falls off the end, `slow` stands on the node **before** the target. Starting on the
> dummy is what makes that true when the target is the head itself.

**Reduces to**: [linked_list § Template 2](./linked_list.md#template-2-dummy-head-deletion--lc-203-)
— the dummy, plus a fixed gap between two pointers instead of a value test.

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

        for _ in range(n + 1):        # the gap: slow will stop one node before the target
            fast = fast.next

        while fast:                   # walk both until fast falls off the end
            fast = fast.next
            slow = slow.next

        slow.next = slow.next.next    # unlink the target
        return dummy.next             # the head may have been the target
```

```java
// java
// LC 19 - Remove Nth Node From End of List
// IDEA: start fast and slow on the dummy, open a gap of n + 1, walk both until fast falls off
// time = O(n), space = O(1)
public ListNode removeNthFromEnd(ListNode head, int n) {
    ListNode dummy = new ListNode(0);
    dummy.next = head;
    ListNode fast = dummy, slow = dummy;
    for (int i = 0; i < n + 1; i++) {
        fast = fast.next;
    }
    while (fast != null) {
        fast = fast.next;
        slow = slow.next;
    }
    slow.next = slow.next.next;
    return dummy.next;
}
```

**Dry run** — removing the head (`[1,2,3,4,5]`, `n = 5`), the case the dummy exists for:

```text
fast = slow = dummy
fast moves n+1 = 6 steps:  dummy -> 1 -> 2 -> 3 -> 4 -> 5 -> None   (fast = None)
while fast: never runs     slow stays on dummy, one node before the head
slow.next = slow.next.next dummy -> 2 -> 3 -> 4 -> 5
return dummy.next          [2,3,4,5]

Without the dummy, slow starts ON the head: it can never stand before it, and
slow.next = slow.next.next removes 2 instead of 1 unless a special case returns head.next.
```

**Traps**
- Starting both on `head` with a gap of n: correct for `[1,2,3,4,5]`, n = 2, but `n = 5` needs
  an `if fast is None: return head.next` branch, the one the dummy removes.
- Returning `head` instead of `dummy.next`: `[1]`, n = 1, returns the deleted node instead of `[]`.

**Similar** — LC 82 *(below, a pointer)* · LC 61 *(the same gap finds the new tail of a rotation)*

### Insertion Sort List — LC 147

> **Key point** — the dummy heads the *sorted* prefix, so inserting a node in front of the
> current smallest is the same splice as inserting it anywhere else. For each detached node,
> walk `p` from the dummy to the last node smaller than it and splice it after `p`.

**Reduces to**: [linked_list § Template 2](./linked_list.md#template-2-dummy-head-deletion--lc-203-)
— the dummy stands before the head of a list being *built*, and the look-ahead on `p.next`
finds an insertion point instead of a match.

```python
# python
# LC 147 - Insertion Sort List
# IDEA: detach each node and splice it into a dummy-headed sorted list after the last smaller node
# time = O(n^2), space = O(1)
class Solution(object):
    def insertionSortList(self, head):
        dummy = ListNode(0)              # sorted part: dummy.next onward
        cur = head
        while cur:
            nxt = cur.next               # detach cur: cache the rest first
            p = dummy
            while p.next and p.next.val < cur.val:
                p = p.next               # p = last sorted node smaller than cur
            cur.next = p.next            # splice cur after p
            p.next = cur
            cur = nxt
        return dummy.next
```

```java
// java
// LC 147 - Insertion Sort List
// IDEA: detach each node and splice it into a dummy-headed sorted list after the last smaller node
// time = O(n^2), space = O(1)
public ListNode insertionSortList(ListNode head) {
    ListNode dummy = new ListNode(0);
    ListNode cur = head;
    while (cur != null) {
        ListNode next = cur.next;
        ListNode p = dummy;
        while (p.next != null && p.next.val < cur.val) {
            p = p.next;
        }
        cur.next = p.next;
        p.next = cur;
        cur = next;
    }
    return dummy.next;
}
```

**Traps**
- Forgetting `nxt = cur.next` before the splice: `cur.next = p.next` rewires it, and the
  unsorted rest of `[4,2,1,3]` is lost after the first insertion.
- Setting `dummy.next = head` first: the sorted part must start empty, or the first node is
  compared with itself and the list loops.

**Not this if** — the follow-up asks for O(n log n): that is merge sort on a list,
[LC 148](#index), with Template 5's first-middle split.

### Remove Zero Sum Consecutive Nodes from Linked List — LC 1171

> **Key point** — two equal prefix sums mean the nodes between them sum to 0. Record the
> **last** node that reaches each prefix sum, then walk again and from each node jump to the
> node after that last occurrence: every zero-sum run between them is skipped at once.

**Reduces to**: [linked_list § Template 2](./linked_list.md#template-2-dummy-head-deletion--lc-203-)
— the dummy (prefix 0) is what lets a zero-sum run that starts at the head be removed — combined
with [hash_map § Template 4](./hash_map.md#template-4-prefix-sum-count-map--lc-560-)'s prefix-sum
map, which stores a node here instead of a count.

```python
# python
# LC 1171 - Remove Zero Sum Consecutive Nodes from Linked List
# IDEA: prefix sum -> last node reaching it; second pass jumps from each prefix to its last occurrence
# time = O(n), space = O(n)
class Solution(object):
    def removeZeroSumSublists(self, head):
        dummy = ListNode(0, head)

        # pass 1: remember the LAST node reaching each prefix sum
        last_seen = {}
        prefix, cur = 0, dummy
        while cur:
            prefix += cur.val
            last_seen[prefix] = cur          # overwrite -> keeps the last one
            cur = cur.next

        # pass 2: from the first node with prefix p, jump past the last node with prefix p
        prefix, cur = 0, dummy
        while cur:
            prefix += cur.val
            cur.next = last_seen[prefix].next
            cur = cur.next
        return dummy.next
```

```java
// java
// LC 1171 - Remove Zero Sum Consecutive Nodes from Linked List
// IDEA: prefix sum -> last node reaching it; second pass jumps from each prefix to its last occurrence
// time = O(n), space = O(n)
public ListNode removeZeroSumSublists(ListNode head) {
    ListNode dummy = new ListNode(0);
    dummy.next = head;
    Map<Integer, ListNode> lastSeen = new HashMap<>();
    int prefix = 0;
    for (ListNode cur = dummy; cur != null; cur = cur.next) {
        prefix += cur.val;
        lastSeen.put(prefix, cur);
    }
    prefix = 0;
    for (ListNode cur = dummy; cur != null; cur = cur.next) {
        prefix += cur.val;
        cur.next = lastSeen.get(prefix).next;
    }
    return dummy.next;
}
```

**Dry run** (`[1,2,-3,3,1]`)

```text
node    : dummy  1   2   -3   3   1
prefix  :   0    1   3    0   3   4
last    : prefix 0 -> node -3,  prefix 3 -> node 3
pass 2  : dummy (0) jumps past -3  -> dummy -> 3
          3 (3)     jumps past 3   -> 3 -> 1
result  : [3,1]
```

**Traps**
- Starting the prefix at `head` instead of a dummy with value 0: `[1,-1]` must return `[]`, but
  with no prefix 0 recorded before the head it returns `[-1]`.
- Keeping the **first** node per prefix instead of the last: at the second occurrence the jump
  points back to the start of the run, so `[1,-1]` becomes a cycle and pass 2 never ends.

## Two Dummies, Split then Join Examples

### Merge Two Sorted Lists — LC 21

> **Key point** — one dummy and one tail, two inputs: splice the smaller head onto the tail
> and advance that input. When one input runs out, the other is already sorted, so
> `tail.next = l1 or l2` finishes in O(1).

**Reduces to**: [linked_list § Template 3](./linked_list.md#template-3-two-dummies-split-then-join--lc-86-)
— run backwards: two chains in, one chain out, and a comparison picks which input donates the
next node.

```python
# python
# LC 21 - Merge Two Sorted Lists
# IDEA: dummy + tail; splice the smaller head each step, then hang the leftover list on the tail
# time = O(m + n), space = O(1)
class Solution(object):
    def mergeTwoLists(self, l1, l2):
        dummy = ListNode(0)
        tail = dummy                    # last node of the merged list
        while l1 and l2:
            if l1.val <= l2.val:        # <= keeps equal values in input order
                tail.next = l1          # splice the node itself, not a copy of its value
                l1 = l1.next
            else:
                tail.next = l2
                l2 = l2.next
            tail = tail.next
        tail.next = l1 or l2            # one list is empty; the other is already sorted
        return dummy.next
```

**Traps**
- Writing `tail = l1` instead of `tail.next = l1` then `tail = tail.next`: nothing is ever
  linked, and `[1,2,4]` + `[1,3,4]` returns `[]`.
- Copying values into new nodes works but costs O(m + n) space the problem does not need.

**Not this if** — the interviewer asks for the recursive form: it is
[recursion.md § 4-3](./recursion.md#4-3-merge-two-sorted-lists-lc-21), O(m + n) call stack. For
k lists, see [LC 23](#index).

### Split Linked List in Parts — LC 725

> **Key point** — with n nodes, part i has `n // k` nodes plus one more when `i < n % k`, so
> the longer parts come first. Walk that many nodes from the current head and cut after the
> last; a part of size 0 is `None`.

**Reduces to**: [linked_list § Template 3](./linked_list.md#template-3-two-dummies-split-then-join--lc-86-)
— k chains instead of two, cut by count instead of by a predicate, and each one terminated
with `.next = None` just as chain 2's tail is.

```python
# python
# LC 725 - Split Linked List in Parts
# IDEA: count n; part i gets n // k nodes, plus one when i < n % k; walk size - 1 nodes, then cut
# time = O(n + k), space = O(k) for the output
class Solution(object):
    def splitListToParts(self, head, k):
        length = 0
        node = head
        while node:
            length += 1
            node = node.next

        base, extra = divmod(length, k)
        parts = []
        cur = head
        for i in range(k):
            parts.append(cur)                      # None once the nodes run out
            size = base + (1 if i < extra else 0)
            if size == 0:
                continue
            for _ in range(size - 1):              # stand on the part's last node
                cur = cur.next
            nxt = cur.next
            cur.next = None                        # cut: terminate this part
            cur = nxt
        return parts
```

**Traps**
- Giving the extra nodes to the *last* parts: `[1..10]`, k = 3, must be `[1,2,3,4]`,
  `[5,6,7]`, `[8,9,10]`; the earlier parts are the longer ones.
- Skipping the cut: every part still runs to the end of the original list.

### Linked List Components — LC 817

> **Key point** — a component is a maximal run of nodes whose values are in `nums`. Count
> the run **ends**: a node in the set whose next node is missing or not in the set closes
> exactly one component.

**Reduces to**: [linked_list § Template 3](./linked_list.md#template-3-two-dummies-split-then-join--lc-86-)
— a set-membership predicate splits the nodes into runs, as Template 3's predicate splits them
into chains, but the runs are only counted, never built.

```python
# python
# LC 817 - Linked List Components
# IDEA: a node in the set whose next is absent or outside the set ends one component
# time = O(n + len(nums)), space = O(len(nums))
class Solution(object):
    def numComponents(self, head, nums):
        in_set = set(nums)
        count = 0
        while head:
            run_ends = head.next is None or head.next.val not in in_set
            if head.val in in_set and run_ends:
                count += 1
            head = head.next
        return count
```

```java
// java
// LC 817 - Linked List Components
// IDEA: a node in the set whose next is absent or outside the set ends one component
// time = O(n + nums.length), space = O(nums.length)
public int numComponents(ListNode head, int[] nums) {
    Set<Integer> inSet = new HashSet<>();
    for (int num : nums) {
        inSet.add(num);
    }
    int count = 0;
    for (ListNode cur = head; cur != null; cur = cur.next) {
        boolean runEnds = cur.next == null || !inSet.contains(cur.next.val);
        if (inSet.contains(cur.val) && runEnds) {
            count++;
        }
    }
    return count;
}
```

**Traps**
- Counting every node in the set: `[0,1,2,3]`, nums = `[0,1,3]`, has 3 such nodes but 2 components.
- Testing membership with a list instead of a set makes each test O(len(nums)).

| LC | Problem | Reduces to | Difficulty | Where |
|---|---|---|---|---|
| 23 | Merge k Sorted Lists | [heap § Template 3](./heap.md#template-3-k-way-merge--lc-23-) | Hard | worked in heap.md |

## Reverse a Segment Examples

### Swap Nodes in Pairs — LC 24

> **Key point** — at k = 2 the segment helper's three handles become local names: `second` is
> the new front, `first` the new tail, and `second.next` the node after. `prev` is the only
> cursor: both members of the next pair are reachable from it.

**Reduces to**: [linked_list § Template 4](./linked_list.md#template-4-reverse-a-segment-reconnect-both-ends--lc-92-)
— the k = 2 case, unrolled, because reversing two nodes is two assignments.

| The helper returns | At `k = 2` it is | Which is written as |
|---|---|---|
| `new_head` | `second` | `prev.next = second` |
| `new_tail` | `first` | `prev = first` — the next iteration's `prev` |
| `next_node` | `second.next` | folded straight into `first.next = second.next` |

```python
# python
# LC 24 - Swap Nodes in Pairs
# IDEA: the k = 2 case of the segment helper, unrolled; prev is the only cursor
# time = O(n), space = O(1)
class Solution(object):
    def swapPairs(self, head):
        dummy = ListNode(0)
        dummy.next = head
        prev = dummy                  # node BEFORE the pair

        while prev.next and prev.next.next:
            first = prev.next         # becomes the pair's tail  (new_tail)
            second = first.next       # becomes the pair's front (new_head)

            first.next = second.next  # (A) carry next_node straight into the new tail
            second.next = first       # (B) flip the pair
            prev.next = second        # (C) prev adopts the new front

            prev = first              # new_tail sits before the next pair
        return dummy.next
```

**Dry run** (`[1,2,3,4]`)

```text
dummy -> 1 -> 2 -> 3 -> 4         prev = dummy, first = 1, second = 2
(A) first.next  = second.next     1 -> 3
(B) second.next = first           2 -> 1
(C) prev.next   = second          dummy -> 2 -> 1 -> 3 -> 4
prev = first (1)                  next pair is 1.next, 1.next.next = (3, 4)
... same three links on (3, 4)    dummy -> 2 -> 1 -> 4 -> 3
prev.next.next is None -> stop    return [2,1,4,3]
```

**Traps**
- Running `(B)` before `(A)` overwrites `second.next` first, so `(A)` points `first` at
  itself: `[1,2,3,4]` leaves `1 -> 1` and the loop never ends.
- Moving `prev` to `second` instead of `first`: after the swap `second` is the *front* of the
  pair, so the next "pair" straddles two pairs and `[1,2,3,4]` returns `[2,3,4,1]`.
- No guard is needed for `[]`, `[1]` or an odd tail: `[1,2,3]` swaps once and leaves `3`.

**Not this if** — the interviewer asks for the recursive form (O(n) stack): it is worked in
[recursion.md § 5-1](./recursion.md#5-1-recursive-linked-list-rewiring--lc-206--24--25-).

### Flatten a Multilevel Doubly Linked List — LC 430 ⭐⭐⭐⭐

> **Key point** — splicing a child chain in is a segment reconnect: two seams, `cur <-> child`
> and `tail <-> next`, each set in both directions. Then keep walking forward: the spliced chain
> is visited next, so deeper levels are flattened without recursion or a stack.

**Reduces to**: [linked_list § Template 4](./linked_list.md#template-4-reverse-a-segment-reconnect-both-ends--lc-92-)
— splice instead of reverse: the segment (the child chain) goes in whole, and only the two
reconnections remain, on a doubly linked list.

```python
# python
# LC 430 - Flatten a Multilevel Doubly Linked List
# IDEA: in-place splice: insert the child chain between cur and cur.next, then keep walking
# time = O(n), space = O(1)   each chain is tail-scanned once
class Solution(object):
    def flatten(self, head):
        cur = head
        while cur:
            if cur.child:
                nxt = cur.next                  # save the successor
                child = cur.child
                cur.child = None                # no child pointer may survive

                cur.next = child                # seam 1: cur <-> child
                child.prev = cur

                tail = child                    # last node of the child chain
                while tail.next:
                    tail = tail.next

                tail.next = nxt                 # seam 2: tail <-> nxt
                if nxt:
                    nxt.prev = tail
            cur = cur.next                      # walks INTO the spliced child
        return head
```

```java
// java
// LC 430 - Flatten a Multilevel Doubly Linked List
// IDEA: in-place splice: insert the child chain between cur and cur.next, then keep walking
// time = O(n), space = O(1)   each chain is tail-scanned once
public Node flatten(Node head) {
    Node cur = head;
    while (cur != null) {
        if (cur.child != null) {
            Node next = cur.next;
            Node child = cur.child;
            cur.child = null;

            cur.next = child;
            child.prev = cur;

            Node tail = child;
            while (tail.next != null) {
                tail = tail.next;
            }
            tail.next = next;
            if (next != null) {
                next.prev = tail;
            }
        }
        cur = cur.next;
    }
    return head;
}
```

**Dry run**

```text
1 <-> 2 <-> 3 <-> 4
            |
            7 <-> 8 <-> 9
                  |
                  11 <-> 12

at node 3:  1 <-> 2 <-> 3 <-> 7 <-> 8 <-> 9 <-> 4
at node 8:  1 <-> 2 <-> 3 <-> 7 <-> 8 <-> 11 <-> 12 <-> 9 <-> 4
```

**Traps**
- Leaving `cur.child` set: the judge rejects a flattened list that still has child pointers.
- Setting only the `next` side of a seam: walking backwards from `4` then skips the child chain.
- A child on the last node: `nxt` is `None`, so `nxt.prev = tail` must be guarded.

**Similar** — LC 114 *(the same splice on a tree: hang the left subtree between root and right)*

### Reverse Nodes in k-Group — LC 25

> **Key point** — after each group the group's old head is its new tail, and that is exactly
> the node that must own the incoming link of the next group: `prev = new_tail`. Count k nodes
> before reversing, so a short final group is left as it is.

**Reduces to**: [linked_list § Template 4](./linked_list.md#template-4-reverse-a-segment-reconnect-both-ends--lc-92-)
— the same three-handle helper, called once per full group of k instead of once.

```python
# python
# LC 25 - Reverse Nodes in k-Group
# IDEA: while k nodes remain after prev, reverse them with the helper and reconnect;
#       a final group shorter than k is left as it is
# time = O(n), space = O(1)
class Solution(object):
    def reverseKGroup(self, head, k):
        dummy = ListNode(0)
        dummy.next = head
        prev = dummy                           # node before the current group

        while self.has_k(prev.next, k):
            new_head, new_tail, next_node = self.reverse_helper(prev.next, k)
            prev.next = new_head               # front of the group
            new_tail.next = next_node          # tail of the group -> rest
            prev = new_tail                    # the next group hangs off this tail
        return dummy.next

    def has_k(self, node, k):
        # are there at least k nodes starting at node?
        count = 0
        while node and count < k:
            node = node.next
            count += 1
        return count == k

    def reverse_helper(self, head, k):
        # reverse k nodes from head; return (new_head, new_tail, first node after)
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

**Dry run** (`[1,2,3,4,5]`, `k = 2`)

```text
group 1: reverse 1,2   dummy -> 2 -> 1 -> 3 -> 4 -> 5    prev = 1
group 2: reverse 3,4   dummy -> 2 -> 1 -> 4 -> 3 -> 5    prev = 3
has_k(5, 2) is False   the short group [5] stays as it is
return [2,1,4,3,5]
```

**Traps**
- Reversing without `has_k`: the helper also reverses a short final group, so `[1,2,3,4,5]`,
  k = 3, returns `[3,2,1,5,4]` instead of `[3,2,1,4,5]`.
- Setting `prev = new_head`: the next group is hung off the front of this group instead of its
  tail, and `[1,2,3,4,5]`, k = 2, returns `[2,3,4,5,1]`.

**Not this if** — the interviewer asks for the recursive form (O(n / k) stack): it is worked in
[recursion.md § 5-1](./recursion.md#5-1-recursive-linked-list-rewiring--lc-206--24--25-).

| LC | Problem | Reduces to | Difficulty | Where |
|---|---|---|---|---|
| 92 | Reverse Linked List II | [linked_list § Template 4](./linked_list.md#template-4-reverse-a-segment-reconnect-both-ends--lc-92-) | Medium | the anchor, worked in linked_list.md |

## Fast/Slow Middle Examples

The problems for this template are worked in other sheets; they are listed here so the
section is where a reader of Template 5 looks for them.

| LC | Problem | Reduces to | Difficulty | Where |
|---|---|---|---|---|
| 160 | Intersection of Two Linked Lists | [2_pointers_linkedlist § 1-1-5](./2_pointers_linkedlist.md#1-1-5--intersection-merge-point-of-2-linked-lists) | Easy | worked in 2_pointers_linkedlist.md |
| 234 | Palindrome Linked List | [linked_list § Template 5](./linked_list.md#template-5-fastslow-middle--lc-876-) + Template 1 | Easy | worked in [2_pointers_linkedlist § 1-1-6](./2_pointers_linkedlist.md#1-1-6--palindrome-linked-list-find-mid---reverse-2nd-half---compare) |
| 142 | Linked List Cycle II | [2_pointers_linkedlist § 1-1-2](./2_pointers_linkedlist.md#1-1-2--return-the-ring-start-point-of-circular-linked-list) | Medium | worked in 2_pointers_linkedlist.md |
| 148 | Sort List | [linked_list § Template 5](./linked_list.md#template-5-fastslow-middle--lc-876-) + LC 21's merge | Medium | worked in [advanced_divide_and_conquer § LC 148](./advanced_divide_and_conquer.md#variation-lc-148--sort-list) |

## Carry Loop Examples

### Plus One Linked List — LC 369

> **Key point** — the carry runs from the least significant digit, which is the list's
> **tail**. Reverse the list so the carry loop can walk forward, stop as soon as the carry is
> 0, append a node if it survives the last digit, then reverse back.

**Reduces to**: [linked_list § Template 6](./linked_list.md#template-6-carry-loop-over-a-dummy--lc-2-)
— one input with a starting carry of 1, wrapped in two [Template 1](./linked_list.md#template-1-iterative-reversal--lc-206-)
reversals because the digits are most significant first.

```python
# python
# LC 369 - Plus One Linked List
# IDEA: reverse, add a carry of 1 until it dies (appending a node if it survives), reverse back
# time = O(n), space = O(1)
class Solution(object):
    def plusOne(self, head):
        head = self.reverse(head)
        cur = head
        carry = 1
        while cur and carry:
            total = cur.val + carry
            cur.val = total % 10
            carry = total // 10
            if carry and cur.next is None:   # the carry outlives the last digit
                cur.next = ListNode(carry)
                carry = 0
            cur = cur.next
        return self.reverse(head)

    def reverse(self, head):
        prev = None
        while head:
            nxt = head.next
            head.next = prev
            prev = head
            head = nxt
        return prev
```

```java
// java
// LC 369 - Plus One Linked List
// IDEA: reverse, add a carry of 1 until it dies (appending a node if it survives), reverse back
// time = O(n), space = O(1)
public ListNode plusOne(ListNode head) {
    head = reverse(head);
    ListNode cur = head;
    int carry = 1;
    while (cur != null && carry > 0) {
        int total = cur.val + carry;
        cur.val = total % 10;
        carry = total / 10;
        if (carry > 0 && cur.next == null) {
            cur.next = new ListNode(carry);
            carry = 0;
        }
        cur = cur.next;
    }
    return reverse(head);
}

private ListNode reverse(ListNode head) {
    ListNode prev = null;
    while (head != null) {
        ListNode next = head.next;
        head.next = prev;
        prev = head;
        head = next;
    }
    return prev;
}
```

**Traps**
- Dropping the final carry: `[9,9]` must become `[1,0,0]`; without the appended node it is `[0,0]`.
- Converting the list to an integer works in Python but overflows `long` in Java on a long list.

**Similar** — LC 2 *(the anchor: two inputs, digits already least significant first)* · LC 445 *(most significant first, two inputs)*

## Hash Map + Doubly Linked List Examples

### Copy List with Random Pointer — LC 138

> **Key point** — a `random` pointer can aim at a node that has not been copied yet, so copy in
> two passes: first map every old node to a fresh copy, then wire each copy's `next` and
> `random` through the map. `map.get(None)` gives `None`, so the list's end needs no branch.

**Reduces to**: [hash_map § Template 8](./hash_map.md#template-8-old-to-new-node-map--lc-133)
— the old-to-new node map, on a list whose extra pointer makes it a graph.

```python
# python
# LC 138 - Copy List with Random Pointer
# IDEA: pass 1 maps every old node to a new node; pass 2 wires next and random through the map
# time = O(n), space = O(n)
class Solution(object):
    def copyRandomList(self, head):
        old_to_new = {}
        cur = head
        while cur:                                   # pass 1: create every copy
            old_to_new[cur] = Node(cur.val)
            cur = cur.next
        cur = head
        while cur:                                   # pass 2: wire the copies
            old_to_new[cur].next = old_to_new.get(cur.next)      # None -> None
            old_to_new[cur].random = old_to_new.get(cur.random)
            cur = cur.next
        return old_to_new.get(head)
```

```java
// java
// LC 138 - Copy List with Random Pointer
// IDEA: pass 1 maps every old node to a new node; pass 2 wires next and random through the map
// time = O(n), space = O(n)
public Node copyRandomList(Node head) {
    Map<Node, Node> oldToNew = new HashMap<>();
    for (Node cur = head; cur != null; cur = cur.next) {
        oldToNew.put(cur, new Node(cur.val));
    }
    for (Node cur = head; cur != null; cur = cur.next) {
        Node copy = oldToNew.get(cur);
        copy.next = oldToNew.get(cur.next);       // get(null) returns null
        copy.random = oldToNew.get(cur.random);
    }
    return oldToNew.get(head);
}
```

**Traps**
- Wiring `random` during the first pass: `random` may point *ahead*, at a node with no copy yet.
- Pointing a copy's `random` at the old node (`cur.random`) instead of its copy: the result
  looks right when printed but shares nodes with the input, and the judge rejects it.

**Not this if** — the follow-up asks for O(1) extra space: interleave each copy after its
original (`A -> A' -> B -> B'`), set `A'.random = A.random.next`, then unweave.

| LC | Problem | Reduces to | Difficulty | Where |
|---|---|---|---|---|
| 146 | LRU Cache | [linked_list § Template 7](./linked_list.md#template-7-hash-map--doubly-linked-list-lru--lc-146-) | Medium | the anchor, worked in linked_list.md |
| 460 | LFU Cache | [linked_list § Template 7, LFU variant](./linked_list.md#lfu-variant--one-list-per-frequency-plus-a-min_freq-pointer-lc-460-) | Hard | worked in linked_list.md |
