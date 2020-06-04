// V0

// V1
// https://leetcode.com/problems/path-sum/discuss/157106/Easy-4-lines-Scala
object Solution {
    def hasPathSum(root: TreeNode, sum: Int): Boolean = (root, sum) match {
	    case (null, _) => false
	    case (r, s) if r.left == null && r.right == null => r.value == sum
	    case (r, s) =>
	      hasPathSum(r.left, sum - r.value) || hasPathSum(r.right, sum - r.value)
	  }
	  
};

// V2