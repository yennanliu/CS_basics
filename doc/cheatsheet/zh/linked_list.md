# Linked List（鏈結串列）

> **範圍** — 單向與雙向鏈結串列的指標手術：反轉、合併、重排、虛擬頭節點技巧，以及環的處理。
> **另見**：[linked_list_examples.md](./linked_list_examples.md) — 這些模板對應的完整題解；[2_pointers_linkedlist.md](./2_pointers_linkedlist.md) — 快慢指標的專門篇；[design.md](./design.md) — LRU 以及其他「串列 + 表」的設計題；[heap.md](./heap.md) — k 路串列合併；[recursion.md](./recursion.md) — 用遞迴改寫串列。

## LeetCode 題目清單

- [Linked List](https://leetcode.com/problem-list/linked-list/)
- [Doubly-Linked List](https://leetcode.com/problem-list/doubly-linked-list/)

## 時間複雜度

| 資料結構 | 搜尋   | 插入   | 刪除   | 最小／最大值  |
| -------------- | -------- | -------- | -------- | -------- |
| Linked List    | O(n)     | O(1)     | O(1)     | O(n)     |

> 只要你手上已經握著目標節點（例如 head，或某個你本來就持有的節點），插入／刪除就是 **O(1)**；但*先找到*那個節點是 **O(n)**。

## 0) 概念
- [fucking algorithm : reverse part of linked list](https://github.com/labuladong/fucking-algorithm/blob/master/%E6%95%B0%E6%8D%AE%E7%BB%93%E6%9E%84%E7%B3%BB%E5%88%97/%E9%80%92%E5%BD%92%E5%8F%8D%E8%BD%AC%E9%93%BE%E8%A1%A8%E7%9A%84%E4%B8%80%E9%83%A8%E5%88%86.md)
- [fucking algorithm : reverse k set of linked list](https://github.com/labuladong/fucking-algorithm/blob/master/%E9%AB%98%E9%A2%91%E9%9D%A2%E8%AF%95%E7%B3%BB%E5%88%97/k%E4%B8%AA%E4%B8%80%E7%BB%84%E5%8F%8D%E8%BD%AC%E9%93%BE%E8%A1%A8.md)
- [fucking algorithm : check palindrome linked list](https://github.com/labuladong/fucking-algorithm/blob/master/%E9%AB%98%E9%A2%91%E9%9D%A2%E8%AF%95%E7%B3%BB%E5%88%97/%E5%88%A4%E6%96%AD%E5%9B%9E%E6%96%87%E9%93%BE%E8%A1%A8.md)


- 善用「虛擬頭節點」（pseudo head node）
    - [代碼隨想錄: LC 203 Remove Linked List Elements](https://youtu.be/Y4oQJklHxVo?t=1111)
- 要從串列刪除節點，必須站在「前一個」節點，才能刪掉下一個節點
    - 也就是說，要站在 `cur`，才能刪掉 `cur.next`
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

### 0-1) 型別
- 鏈結串列
- 環狀鏈結串列
- 雙向鏈結串列
- 雙向串列（Double Linked list）
    - LC 146
- 其他
    - LC 138：
    ```python
    dic = dict()
    m = n = head
    dic[m] = Node(m.val)
    ```
    - LC 208：
    - [trie](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/trie.md)
    ```python
    self.children = defaultdict(Node)
    ```
- 題型
    - 反轉
        - 反轉整條串列
            - LC 206
        - 反轉指定起訖點之間的串列
            - LC 92、LC 25
        - 反轉串列的一部分
        - 每 k 個一組反轉
    - 合併
        - 合併兩條串列
    - 檢查
        - 檢查串列有沒有環
        - 找出環的起點
    - 移除倒數第 N 個節點
        - Remove Nth Node From End of List — LC 19
    - 組合題
        - 上述情況的各種組合

### 0-2) 模式

#### **虛擬頭節點技巧**

**定義**：建一個指向真正 head 的虛擬／假頭節點，讓邊界情況與節點刪除操作變得好寫。

**什麼時候用**：
- 要刪掉串列開頭的節點
- head 節點本身可能被改動
- 想簡化邊界情況的處理
- 需要一路追蹤前一個節點的操作

**時間複雜度**：O(n) — 跟不用虛擬頭一樣
**空間複雜度**：O(1) — 只多一個節點

**模板**：
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

**好處**：
- 不必為 head 節點寫特例
- 邏輯更簡單
- 減少邊界情況的 bug
- 整趟走訪都有一致的 prev 指標

---

#### **為什麼要用虛擬節點？圖解對照（LC 19）**

> **題目**：從 `[1, 2, 3, 4, 5]` 移除**倒數第 n 個**節點。

---

##### 情況 A — 一般刪除：`n = 2`（刪掉節點 `4`）

**不用虛擬節點** — 這裡沒問題：

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

**用虛擬節點** — 一樣可行，邏輯相同：

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

##### 情況 B — 邊界情況：`n = 5`（要刪掉的是 **head** 節點 `1`）

**不用虛擬節點** — 壞掉了，得寫特例：

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

**用虛擬節點** — 一視同仁，**不需要**特例：

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

##### 小結：虛擬節點贏在哪

| | 不用虛擬節點 | 用虛擬節點 |
|---|---|---|
| 一般刪除 | ✓ 可行 | ✓ 可行 |
| 刪掉 head（n = len） | ❌ 得多一句 `if not fast: return head.next` | ✓ 一視同仁 |
| 程式分支 | 多一個條件判斷 | 沒有 |
| `slow` 的起點 | `head`（沒辦法站到 head 之前） | `dummy`（剛好在 head 前一步） |

**關鍵洞見**：虛擬節點給了 `slow` 一個站在 **head 前一個節點**的位置，於是它能跨過任何節點重新接線 — 包括 head 本身 — 完全不用特殊處理。

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

#### **虛擬頭節點 — 其他應用**

再看兩題「虛擬節點把特例消掉」的例子。這個家族其餘的題目寫在它們該待的地方，不在這裡重貼：

| LC | 題目 | 寫在哪 |
|---|---|---|
| 19 | Remove Nth Node From End | 上面那段圖解，以及 [linked_list_examples.md](./linked_list_examples.md#9-remove-nth-node-from-end-of-list--lc-19) 的兩種 Java 寫法 |
| 21 | Merge Two Sorted Lists | [linked_list_examples.md](./linked_list_examples.md#4-merge-two-sorted-lists--lc-21) |
| 2 | Add Two Numbers | [下面的 1-1-7)](#1-1-7-add-2-linked-list--lc-2) |
| 203 | Remove Linked List Elements | 下面的 [依值移除節點的模式](#remove-elements-by-value-pattern) |

**從已排序串列移除重複 — LC 83**：虛擬節點握著最後一個*保留下來*的節點，於是一整串相同的值會自然塌縮，也永遠不用為「head 就是重複值」寫特例。
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

**Partition List — LC 86**：用*兩個*虛擬節點。分別把 `< x` 這條鏈和 `>= x` 這條鏈各自接好，最後串起來 — 不做原地手術，穩定性也免費附贈。
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
**虛擬頭節點的主要好處**：

| 面向 | 不用虛擬節點 | 用虛擬節點 |
|--------|---------------|------------|
| **邊界情況** | head 的處理很囉唆 | 統一處理 |
| **程式長度** | 條件判斷更多 | 更乾淨、更短 |
| **出錯機率** | 較高（邊界情況） | 較低（邏輯一致） |
| **可讀性** | 比較難跟 | 直覺得多 |

**相關題目**：
- LC 19: Remove Nth Node From End of List
- LC 21: Merge Two Sorted Lists
- LC 83: Remove Duplicates from Sorted List
- LC 86: Partition List
- LC 203: Remove Linked List Elements
- LC 328: Odd Even Linked List

---

#### **依值移除節點的模式**

**定義**：把串列中所有等於某個值的節點都移除。做法是虛擬頭節點加上「往前看一格」的技巧 — 當前指標檢查的是 `curr.next`，而不是 `curr` 自己。

**核心概念**：
- **關鍵洞見**：找到要刪的節點時，我們**只**更新指標連接（`curr.next = curr.next.next`），`curr` 本身**不往前走**
- 這樣才能處理連續命中的節點（例如 `[6,6,6,3]`、val=6）
- 只有在 `curr.next.val != val` 時才把 `curr` 往前推

**什麼時候用**：
- 要依值移除串列中任何位置的節點
- 開頭的節點也可能需要被移除
- 要移除連續重複的值

**時間複雜度**：O(n)
**空間複雜度**：O(1)

**模板**：
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

**手動追蹤範例**（`[6,6,6,3]`、val=6）：
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

**為什麼這樣就能處理連續命中**：
| 情境 | 沒有「原地不動」 | 有「原地不動」 |
|----------|------------------------|---------------------|
| `[6,6,3]` val=6 | 會漏掉第二個 6 | 全部的 6 都抓到 |
| 刪除 head | 需要特例 | 一視同仁 |

**類似的 LC 題目**：
- LC 203: Remove Linked List Elements（就是這個模式本身）
- LC 83: Remove Duplicates from Sorted List（類似，比較相鄰節點）
- LC 82: Remove Duplicates from Sorted List II（重複的全部刪掉）
- LC 237: Delete Node in a Linked List（不一樣 — 拿不到前一個節點）
- LC 1474: Delete N Nodes After M Nodes（模式的變化）
- LC 2487: Remove Nodes From Linked List（改用堆疊的變化）

---

#### **雙向鏈結串列 + HashMap（LRU Cache 模式）** ⭐⭐⭐⭐⭐

**核心想法**：用 HashMap 做 O(1) 的 key 查找，配上雙向鏈結串列做 O(1) 的有序淘汰。最近用過的節點靠近**尾端**；最久沒用的靠近**開頭**。頭尾各放一個哨兵節點，所有邊界情況的指標檢查就全消失了。

**結構配置**：
```text
head(dummy) <-> [LRU] <-> ... <-> [MRU] <-> tail(dummy)
```

**什麼時候用**：
- 需要 O(1) 的 get + O(1) 的 put，而且要有序淘汰（LRU/MFU）
- 任何需要「照存取順序追蹤」的集合

**時間複雜度**：get 與 put 都是 O(1)  
**空間複雜度**：O(capacity)

**關鍵輔助操作**：
- `_remove(node)` — 用 O(1) 把節點從串列中摘掉
- `_insert(node)` — 用 O(1) 把節點插到 tail 前面（MRU 位置）

**模板**：
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

**視覺追蹤**（capacity=2）：
```text
put(1,1): head <-> [1] <-> tail
put(2,2): head <-> [1] <-> [2] <-> tail
get(1):   head <-> [2] <-> [1] <-> tail   ← 1 moved to MRU
put(3,3): evict head.next=[2]
          head <-> [1] <-> [3] <-> tail
```

**為什麼要哨兵節點？**
- `_remove` 和 `_insert` 永遠拿得到合法的 `.prev`/`.next` 鄰居
- 不需要 `if node.prev is None` 或 `if node.next is None` 這種防護
- 刪頭、刪尾、刪中間都是同一套程式碼

**類似的 LC 題目**：
| # | 題目 | 差別在哪 |
|---|---------|----------------|
| 146 | LRU Cache | 經典模式 — 淘汰最久沒用的 |
| 460 | LFU Cache | 兩層結構：頻率表 + 每個頻率一條雙向鏈結串列 |
| 432 | All O(1) Data Structure | 由計數桶組成的雙向鏈結串列 |
| 1472 | Design Browser History | 雙向鏈結串列，訪問新頁時把前方截斷 |
| 641 | Design Circular Deque | 固定容量的雙向鏈結串列，兩端都能操作 |
| 716 | Max Stack | 堆疊 + 雙向鏈結串列 + TreeMap，做到 O(log n) 的 popMax |

---

#### **反轉 K 個節點的輔助函式模式** ⭐⭐⭐⭐⭐

**核心想法**：幾乎每一題「反轉某一*段*」（LC 92、LC 25、LC 24、LC 206）都是**同一個基本操作** — 從某個 `head` 開始反轉 `k` 個節點，然後把線接回去。把這個基本操作抽成一個可重用的輔助函式，外層解法就只要煩惱**定位那一段**和**把兩頭縫回去**。

這個輔助函式反轉 `k` 個節點，並回傳你重新接線所需的**三個把手**：

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

**為什麼要回傳三個東西？** 反轉一段*中間*的節點之後，**兩個邊界**都得重新接線：

| 回傳值 | 它是什麼 | 用來接哪裡 |
|----------|-----------|-------------------|
| `prev`（`new_head`） | 反轉後這一塊的新 **head** | `prev_of_segment.next = new_head` |
| `head`（`new_tail`） | 新的 **tail**（原本的第一個節點） | `new_tail.next = next_node` |
| `curr`（`next_node`） | 這一段**之後**的第一個節點 | tail 必須指到這裡 |

**什麼時候用**：
- 反轉子區間 `[left, right]`（LC 92）→ 反轉 `right - left + 1` 個節點
- 每 k 個一組反轉（LC 25）→ 迴圈呼叫輔助函式，直到剩不到 `k` 個
- 反轉整條串列（LC 206）→ 呼叫一次，`k = length`（或 `k = ∞`）

**模板 — 把輔助函式套到 LC 92（Reverse Linked List II）**：
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

**圖解**（`[1,2,3,4,5]`、`left=2`、`right=4` → 反轉 `2,3,4` 這 3 個節點）：

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

**三個邊界把手的視覺化**：
```text
        prev        new_head → ... → new_tail        next_node
          |             |                 |               |
   ... -> 1             4 -> 3 -> 2         (dangling)      5 -> ...
          |_____________|                 |_______________|
             (C1) prev.next = new_head        (C2) new_tail.next = next_node
```

**把輔助函式重用到 LC 25（Reverse Nodes in k-Group）**：
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

> **關鍵洞見**：*同一個* `reverse_helper` 就撐起了 LC 206 / 92 / 25。差別只在外圍邏輯 — **206** 呼叫一次，**92** 先定位一段再呼叫一次，**25** 用迴圈每組呼叫一次。把三把手的回傳值（`new_head, new_tail, next_node`）練熟，這三題就都塌縮成「定位 → 反轉 → 接回去」。

**類似的 LC 題目**：
| # | 題目 | 輔助函式怎麼套 |
|---|---------|------------------------|
| 206 | Reverse Linked List | 呼叫一次，`k = length` — 只有 `new_head` 有用 |
| 92  | Reverse Linked List II | 定位那一段，用 `k = right - left + 1` 呼叫一次，兩頭都接回去 |
| 25  | Reverse Nodes in k-Group | 每組呼叫一次；最後不足 `k` 的尾巴跳過 |
| 24  | Swap Nodes in Pairs | 就是每組 `k = 2` 的特例 |
| 61  | Rotate List | 操作不同，但一樣是「定位邊界 + 重新縫合」那套紀律 |

---

## 1) 通用形式
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

### 1-1) 基本操作


#### 1-1-1) 反轉鏈結串列（迭代） — LC 206
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

#### 1-1-2) 反轉鏈結串列（遞迴） — LC 206
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

#### 1-1-3) 反轉 *[a,b] 區間內的節點*（迭代） — LC 92
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

#### 1-1-4) *每 k 個一組*反轉串列（迭代） — LC 25
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

#### 1-1-5) 反轉*前 N 個*節點（遞迴）
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

#### 1-1-6) 反轉串列*中間的 N 個節點*（以 *start, end* 表示區間）（遞迴） — LC 92
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

#### 1-1-7) 兩條串列相加 — LC 2
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

#### 1-1-8) 找出串列中點 — LC 876
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


## 2) 模式選擇

鏈結串列題其實很少真的在考串列。它們考的是：動手術的當下，**你手上必須握著哪個把手** — 這份文件上的每個技巧，存在的理由都是確保你握著它。挑法要看答案需要什麼，不是看題目叫什麼名字。

| 如果題目要你… | 就用 | 因為 | 詳寫在 |
|---|---|---|---|
| 在**任何位置**刪除或插入，包含 head | **虛擬頭節點** | 它讓 `prev` 有個站在 head *之前*的位置，「刪掉 head」就不再是特例 | [虛擬頭節點技巧](#dummy-head-technique) |
| 移除**所有**符合某個值的節點 | **虛擬節點 + 檢查 `curr.next`** | 刪完之後你必須能*原地不動*，否則像 `[6,6,6]` 這種連續值會漏掉一個 | [依值移除節點的模式](#remove-elements-by-value-pattern) |
| 反轉**整條**串列 | **三步迴圈**：先存下 next → 翻指標 → 前進 | O(1) 空間；遞迴版答案一樣，卻要每個節點吃掉一個 stack frame | [1-1-1)](#1-1-1-reverse-linked-list-iteration--lc-206) |
| 反轉**一段** — `[left, right]`、每 `k` 個，或成對 | **反轉 k 個的輔助函式，回傳三個把手** | LC 92 / 25 / 24 的差別只在*那一段在哪*，反轉的方式完全一樣 | [反轉 K 個節點的輔助函式模式](#reverse-k-nodes-helper-pattern-) |
| 找中點、偵測環，或走到**倒數**第 n 個 | **快慢指標** | 一趟掃完、O(1) 空間，也不用先算長度 | [1-1-8)](#1-1-8-find-linked-list-middle-point--lc-876)、[2_pointers_linkedlist.md](./2_pointers_linkedlist.md) |
| **重排** — 交錯、切分、旋轉、回文判斷 | **快慢指標切一半 → 反轉後半 → 合併** | 每一題重排都是這三個基本操作依序組合；沒有一個是新東西 | [examples 2)](./linked_list_examples.md#2-reorder-list--lc-143)、[7)](./linked_list_examples.md#7-palindrome-linked-list--lc-234) |
| 合併**兩條**已排序串列 | **虛擬節點 + 一趟合併走訪**，接節點而不是複製值 | 尾端指標就是全部的訣竅：`cur.next = l1 or l2` 收尾 | [examples 4)](./linked_list_examples.md#4-merge-two-sorted-lists--lc-21) |
| 合併 **k** 條已排序串列，或把一條串列排序 | **分治法** — 兩兩合併，或用中點做合併排序 | O(n log k) / O(n log n)；用堆積則是拿 O(k) 空間換掉遞迴 | [examples 5)](./linked_list_examples.md#5-merge-k-sorted-lists--lc-23)、[14)](./linked_list_examples.md#14-sort-list-merge-sort-on-a-linked-list--lc-148-)、[heap.md](./heap.md) |
| 同時要**任意位置**讀取*和* O(1) 淘汰 | **雙向鏈結串列 + 雜湊表** | 表給你節點，雙向節點給你它的鄰居 — 少了任一個都不夠 | [雙向鏈結串列 + HashMap](#doubly-linked-list--hashmap-lru-cache-pattern-)、[design.md](./design.md) |
| 對存成串列的位數做**算術** | **在虛擬節點上跑進位迴圈**，若最高位在前就先反轉 | 進位會活得比兩個輸入都久，所以迴圈條件是 `l1 or l2 or carry` | [1-1-7)](#1-1-7-add-2-linked-list--lc-2)、[examples 13)](./linked_list_examples.md#13-plus-one-linked-list--lc-369) |
| 回答需要**隨機存取或視窗**的問題 | **先倒進陣列，再用陣列的技巧** | 前綴和與單調堆疊都需要索引，串列沒有；而且通常允許 O(n) 額外空間 | [examples 15)](./linked_list_examples.md#15-prefix-sum--hashmap-on-a-linked-list--lc-1171-)、[16)](./linked_list_examples.md#16-monotonic-stack-over-a-linked-list--lc-1019-) |

### 四個陷阱

1. **把串列弄丟。** 還沒存下 `curr.next` 就寫 `curr.next = prev`，後面整串就沒了。先存起來 — 這就是反轉迴圈非得照那個順序寫的原因。
2. **回傳錯的 head。** 任何可能動到第一個節點的操作之後，要回傳 `dummy.next`，不是 `head`：`head` 可能已經不在串列裡了。
3. **留下一個環。** 遞迴反轉裡只寫 `head.next.next = head`，卻漏掉後面的 `head.next = null`，最後兩個節點就會互指。
4. **走過頭。** 兩步跳要寫成 `while (fast != null && fast.next != null)`。兩個條件順序寫反，在偶數長度的串列上就會對 null 解參考。

## 3) 實戰題解

完整解法搬到 **[linked_list_examples.md](./linked_list_examples.md)** 了，免得上面的模板被它們埋掉。十七題，依各自演練的技巧分組：

| 分組 | 題目 |
|---|---|
| [反轉與重排](./linked_list_examples.md#reversal--reordering) | LC 92, 143, 24 |
| [合併與切分](./linked_list_examples.md#merging--splitting) | LC 21, 23, 725 |
| [快慢指標與結構](./linked_list_examples.md#fastslow-pointers--structure) | LC 234, 160, 19 |
| [複製、攤平與連通分量](./linked_list_examples.md#copying-flattening--components) | LC 138, 817, 430 |
| [算術與排序](./linked_list_examples.md#arithmetic--sorting-on-a-list) | LC 369, 148, 147 |
| [把陣列技巧借到串列上](./linked_list_examples.md#array-techniques-borrowed-onto-a-list) | LC 1171, 1019 |
