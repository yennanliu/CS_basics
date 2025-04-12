package LeetCodeJava.Tree;

// https://leetcode.com/problems/construct-quad-tree/description/
// https://leetcode.cn/problems/construct-quad-tree/solutions/

/**
 * 427. Construct Quad Tree
 * Medium
 * Topics
 * Companies
 * Given a n * n matrix grid of 0's and 1's only. We want to represent grid with a Quad-Tree.
 *
 * Return the root of the Quad-Tree representing grid.
 *
 * A Quad-Tree is a tree data structure in which each internal node has exactly four children. Besides, each node has two attributes:
 *
 * val: True if the node represents a grid of 1's or False if the node represents a grid of 0's. Notice that you can assign the val to True or False when isLeaf is False, and both are accepted in the answer.
 * isLeaf: True if the node is a leaf node on the tree or False if the node has four children.
 * class Node {
 *     public boolean val;
 *     public boolean isLeaf;
 *     public Node topLeft;
 *     public Node topRight;
 *     public Node bottomLeft;
 *     public Node bottomRight;
 * }
 * We can construct a Quad-Tree from a two-dimensional area using the following steps:
 *
 * If the current grid has the same value (i.e all 1's or all 0's) set isLeaf True and set val to the value of the grid and set the four children to Null and stop.
 * If the current grid has different values, set isLeaf to False and set val to any value and divide the current grid into four sub-grids as shown in the photo.
 * Recurse for each of the children with the proper sub-grid.
 *
 * If you want to know more about the Quad-Tree, you can refer to the wiki.
 *
 * Quad-Tree format:
 *
 * You don't need to read this section for solving the problem. This is only if you want to understand the output format here. The output represents the serialized format of a Quad-Tree using level order traversal, where null signifies a path terminator where no node exists below.
 *
 * It is very similar to the serialization of the binary tree. The only difference is that the node is represented as a list [isLeaf, val].
 *
 * If the value of isLeaf or val is True we represent it as 1 in the list [isLeaf, val] and if the value of isLeaf or val is False we represent it as 0.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: grid = [[0,1],[1,0]]
 * Output: [[0,1],[1,0],[1,1],[1,1],[1,0]]
 * Explanation: The explanation of this example is shown below:
 * Notice that 0 represents False and 1 represents True in the photo representing the Quad-Tree.
 *
 * Example 2:
 *
 *
 *
 * Input: grid = [[1,1,1,1,0,0,0,0],[1,1,1,1,0,0,0,0],[1,1,1,1,1,1,1,1],[1,1,1,1,1,1,1,1],[1,1,1,1,0,0,0,0],[1,1,1,1,0,0,0,0],[1,1,1,1,0,0,0,0],[1,1,1,1,0,0,0,0]]
 * Output: [[0,1],[1,1],[0,1],[1,1],[1,0],null,null,null,null,[1,0],[1,0],[1,1],[1,1]]
 * Explanation: All values in the grid are not the same. We divide the grid into four sub-grids.
 * The topLeft, bottomLeft and bottomRight each has the same value.
 * The topRight have different values so we divide it into 4 sub-grids where each has the same value.
 * Explanation is shown in the photo below:
 *
 *
 *
 * Constraints:
 *
 * n == grid.length == grid[i].length
 * n == 2x where 0 <= x <= 6
 *
 */
public class ConstructQuadTree {

    /*
    // Definition for a QuadTree node.
    class Node {
        public boolean val;
        public boolean isLeaf;
        public Node topLeft;
        public Node topRight;
        public Node bottomLeft;
        public Node bottomRight;


        public Node() {
            this.val = false;
            this.isLeaf = false;
            this.topLeft = null;
            this.topRight = null;
            this.bottomLeft = null;
            this.bottomRight = null;
        }

        public Node(boolean val, boolean isLeaf) {
            this.val = val;
            this.isLeaf = isLeaf;
            this.topLeft = null;
            this.topRight = null;
            this.bottomLeft = null;
            this.bottomRight = null;
        }

        public Node(boolean val, boolean isLeaf, Node topLeft, Node topRight, Node bottomLeft, Node bottomRight) {
            this.val = val;
            this.isLeaf = isLeaf;
            this.topLeft = topLeft;
            this.topRight = topRight;
            this.bottomLeft = bottomLeft;
            this.bottomRight = bottomRight;
        }
    }
    */

    // NOTE !!! we have custom Node defined for this problem below
    class Node {
        public boolean val;
        public boolean isLeaf;
        public Node topLeft;
        public Node topRight;
        public Node bottomLeft;
        public Node bottomRight;


        public Node() {
            this.val = false;
            this.isLeaf = false;
            this.topLeft = null;
            this.topRight = null;
            this.bottomLeft = null;
            this.bottomRight = null;
        }

        public Node(boolean val, boolean isLeaf) {
            this.val = val;
            this.isLeaf = isLeaf;
            this.topLeft = null;
            this.topRight = null;
            this.bottomLeft = null;
            this.bottomRight = null;
        }

        public Node(boolean val, boolean isLeaf, Node topLeft, Node topRight, Node bottomLeft, Node bottomRight) {
            this.val = val;
            this.isLeaf = isLeaf;
            this.topLeft = topLeft;
            this.topRight = topRight;
            this.bottomLeft = bottomLeft;
            this.bottomRight = bottomRight;
        }
    }

    // V0
//    public Node construct(int[][] grid) {
//
//    }


    // V1
    // https://youtu.be/UQ-1sBMV0v4?si=1dy0ZiqZHndfU4vh
    // https://github.com/neetcode-gh/leetcode/blob/main/java%2F0427-construct-quad-tree.java
    private int[][] grid;

    public Node construct_1(int[][] grid) {
        this.grid = grid;
        return dfs(0, 0, grid.length);
    }

    private Node dfs(int row, int column, int n) {
        Node node = new Node();

        if (areAllEqual(row, column, n)) {
            node.isLeaf = true;
            node.val = grid[row][column] == 1;
        } else {
            n /= 2;
            node.isLeaf = false;
            node.val = false;

            node.topLeft = dfs(row, column, n);
            node.bottomLeft = dfs(row + n, column, n);
            node.topRight = dfs(row, column + n, n);
            node.bottomRight = dfs(row + n, column + n, n);
        }

        return node;
    }

    private boolean areAllEqual(int row, int column, int n) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[row][column] != grid[row + i][column + j]) {
                    return false;
                }
            }
        }

        return true;
    }


    // V2
    // https://leetcode.com/problems/construct-quad-tree/solutions/3234703/clean-codes-full-explanation-helper-meth-oifr/
    public Node construct_2(int[][] grid) {
        return helper(grid, 0, 0, grid.length);
    }

    private Node helper(int[][] grid, int i, int j, int w) {
        if (allSame(grid, i, j, w))
            return new Node(grid[i][j] == 1 ? true : false, true);

        Node node = new Node(true, false);
        node.topLeft = helper(grid, i, j, w / 2);
        node.topRight = helper(grid, i, j + w / 2, w / 2);
        node.bottomLeft = helper(grid, i + w / 2, j, w / 2);
        node.bottomRight = helper(grid, i + w / 2, j + w / 2, w / 2);
        return node;
    }

    private boolean allSame(int[][] grid, int i, int j, int w) {
        for (int x = i; x < i + w; ++x)
            for (int y = j; y < j + w; ++y)
                if (grid[x][y] != grid[i][j])
                    return false;
        return true;
    }

    // V3-1
    // https://leetcode.com/problems/construct-quad-tree/solutions/3235464/java-with-explanation-2-approaches-by-ka-ppc5/
    public Node construct_3_1(int[][] grid) {
        return construct_3_1(grid, 0, 0, grid.length);
    }

    public Node construct_3_1(int[][] grid, int i, int j, int length) {
        if (isSame(grid, i, j, length)) {
            return new Node(grid[i][j] == 1, true);
        } else {
            Node node = new Node(false, false);
            node.topLeft = construct_3_1(grid, i, j, length / 2);
            node.topRight = construct_3_1(grid, i, j + length / 2, length / 2);
            node.bottomLeft = construct_3_1(grid, i + length / 2, j, length / 2);
            node.bottomRight = construct_3_1(grid, i + length / 2, j + length / 2, length / 2);
            return node;
        }
    }

    public boolean isSame(int[][] grid, int x1, int y1, int length) {
        for (int i = x1; i < x1 + length; i++) {
            for (int j = y1; j < y1 + length; j++) {
                if (grid[i][j] != grid[x1][y1]) {
                    return false;
                }
            }
        }
        return true;
    }

    // V3-2
    // https://leetcode.com/problems/construct-quad-tree/solutions/3235464/java-with-explanation-2-approaches-by-ka-ppc5/
    public Node construct_3_2(int[][] grid) {
        return construct_3_2(grid, 0, 0, grid.length);
    }

    public Node construct_3_2(int[][] grid, int i, int j, int length) {
        if (length == 1) {
            return new Node(grid[i][j] == 1, true);
        } else {
            Node node = new Node(false, false);
            Node topLeft = construct_3_2(grid, i, j, length / 2);
            Node topRight = construct_3_2(grid, i, j + length / 2, length / 2);
            Node bottomLeft = construct_3_2(grid, i + length / 2, j, length / 2);
            Node bottomRight = construct_3_2(grid, i + length / 2, j + length / 2, length / 2);
            if (topLeft.isLeaf && topRight.isLeaf && bottomLeft.isLeaf && bottomRight.isLeaf
                    && topLeft.val == topRight.val && topLeft.val == bottomRight.val && topLeft.val == bottomLeft.val) {
                node.isLeaf = true;
                node.val = topLeft.val;
            } else {
                node.topLeft = topLeft;
                node.topRight = topRight;
                node.bottomLeft = bottomLeft;
                node.bottomRight = bottomRight;
            }
            return node;
        }
    }

    // V4
    // https://leetcode.com/problems/construct-quad-tree/solutions/1199472/java-easy-to-understand-100-faster-78-le-tu6i/
    public Node construct_4(int[][] grid) {
        return make(grid, 0, 0, grid.length);
    }

    private Node make(int grid[][], int r, int c, int length) {
        if (length == 1)
            return new Node(grid[r][c] == 1 ? true : false, true);
        Node topLeft = make(grid, r, c, length / 2);
        Node topRight = make(grid, r, c + length / 2, length / 2);
        Node bottomLeft = make(grid, r + length / 2, c, length / 2);
        Node bottomRight = make(grid, r + length / 2, c + length / 2, length / 2);
        if (topLeft.val == topRight.val && bottomLeft.val == bottomRight.val && topLeft.val == bottomLeft.val
                && topLeft.isLeaf && topRight.isLeaf && bottomLeft.isLeaf && bottomRight.isLeaf)
            return new Node(topLeft.val, true);
        else
            return new Node(true, false, topLeft, topRight, bottomLeft, bottomRight);
    }


}
