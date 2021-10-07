# BST (Binary Search Tree)

## 0) Concept
```java
// java

// class
public class TreeNode{
	// attr
	int val;  // value on node
	TreeNode left;  // point to left clild
	TreeNode right;  // point to right clild

	// constructor
	TreeNode(int val){
		this.val = val;
		this.left = null;
		this.right = null;
	}
}

// init a BST
TreeNode node1 = new TreeNode(2);
TreeNode node2 = new TreeNode(3);
TreeNode node3 = new TreeNode(4);

// modify node1's val
node1.val = 10;

// connect nodes
node1.left = node2;
node1.right = node3;
```

### 0-1) Framework

### 0-2) Pattern

## 1) General form

### 1-1) Basic OP

## 2) LC Example
