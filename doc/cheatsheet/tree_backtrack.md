# Tree Backtracking Template (gpt)

Great — many **tree path problems** share the same DFS + backtracking structure. The following template works for problems like:

* LeetCode 113 Path Sum II
* LeetCode 257 Binary Tree Paths
* LeetCode 129 Sum Root to Leaf Numbers
* LeetCode 437 Path Sum III (slightly modified)

I'll show the **universal template**, then how each problem plugs into it.

---

# 1️⃣ Universal Tree Backtracking Template

```java
void dfs(TreeNode node, ...) {

    if (node == null) return;

    // 1. choose (update path / sum / state)
    
    // 2. check goal (usually leaf condition)
    
    // 3. explore children
    dfs(node.left, ...);
    dfs(node.right, ...);

    // 4. undo choice (backtrack)
}
```

For **path problems**, we typically maintain:

```
List<Integer> path
some running state (sum / number / string)
```

---

# 2️⃣ Full Generic Template

```java
void dfs(TreeNode node, List<Integer> path, int state) {

    if (node == null) return;

    // choose
    path.add(node.val);
    state = update(state, node.val);

    // goal check
    if (isLeaf(node) && condition(state)) {
        save(path);
    }

    // explore
    dfs(node.left, path, state);
    dfs(node.right, path, state);

    // undo (backtrack)
    path.remove(path.size() - 1);
}
```

Key parts:

| Step    | Purpose             |
| ------- | ------------------- |
| choose  | add node to path    |
| check   | see if goal reached |
| explore | DFS children        |
| undo    | restore state       |

---

# 3️⃣ Apply to LC 113 (Path Sum II)

For LeetCode 113 Path Sum II:

State = **remaining sum**

```java
void dfs(TreeNode node, int remain, List<Integer> path) {

    if (node == null) return;

    path.add(node.val);

    if (node.left == null && node.right == null && remain == node.val) {
        res.add(new ArrayList<>(path));
    }

    dfs(node.left, remain - node.val, path);
    dfs(node.right, remain - node.val, path);

    path.remove(path.size() - 1);
}
```

---

# 4️⃣ Apply to LC 257 (Binary Tree Paths)

For LeetCode 257 Binary Tree Paths:

State = **path string**

```java
void dfs(TreeNode node, String path) {

    if (node == null) return;

    path += node.val;

    if (node.left == null && node.right == null) {
        res.add(path);
        return;
    }

    path += "->";

    dfs(node.left, path);
    dfs(node.right, path);
}
```

---

# 5️⃣ Apply to LC 129 (Root → Leaf Numbers)

For LeetCode 129 Sum Root to Leaf Numbers:

State = **number**

```
123 means path: 1 → 2 → 3
```

```java
void dfs(TreeNode node, int num) {

    if (node == null) return;

    num = num * 10 + node.val;

    if (node.left == null && node.right == null) {
        total += num;
        return;
    }

    dfs(node.left, num);
    dfs(node.right, num);
}
```

---

# 6️⃣ Special Case: LC 437 (Path Sum III)

LeetCode 437 Path Sum III is different because paths **can start anywhere**, not only root.

So we run DFS **from every node**.

But the inner DFS still follows the same template.

---

# 7️⃣ Mental Model (Very Important)

Whenever you see:

```
root → leaf
path
all paths
sum of paths
string of paths
```

Think immediately:

```
DFS + Backtracking
```

Template:

```
add node
check leaf
dfs children
remove node
```

---

# 8️⃣ Interview Cheat Code ⭐

Just memorize this **5-line skeleton**:

```java
path.add(node.val);

if (leaf condition)
    save result;

dfs(left)
dfs(right)

path.remove(path.size()-1);
```

You can solve **most root-to-leaf problems** with this.

---

✅ **Pro tip for interviews**

If a problem contains these words:

```
all paths
root to leaf
return list of paths
```

It's almost **guaranteed DFS + backtracking**.
