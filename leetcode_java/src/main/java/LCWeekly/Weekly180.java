package LCWeekly;

import LeetCodeJava.DataStructure.TreeNode;

import java.util.*;

/**
 * LeetCode biweekly contest 180
 * https://leetcode.com/contest/weekly-contest-180/
 * 中文題目
 * https://leetcode.cn/contest/weekly-contest-180/
 *
 */
public class Weekly180 {

    // LC 1380 -> ok (again)
    // https://leetcode.com/problems/lucky-numbers-in-a-matrix/
    // 15.49 - 59 pm
    /**
     *
     *  -> return all lucky numbers in the matrix in any order.
     *
     *    - Given an `m x n` matrix of `distinct` numbers,
     *
     *
     *   - A lucky number is an element of the
     *     matrix such that it's
     *       - min of row
     *       - max of col
     *
     *      -> its val is BOTH max val in `row and col`
     *
     *
     *  ---------------
     *
     *   IDEA 1) BRUTE FORCE + ARRAY OP
     *
     *
     *  ---------------
     *
     *   ex1)
     *
     *    Input: matrix = [[3,7,8],[9,11,13],[15,16,17]]
     *    Output: [15]
     *
     *    ->
     *
     *     [
     *     [3,7,8],
     *     [9,11,13],
     *     [15,16,17]
     *    ]
     *
     *
     *  ex 2)
     *
     *  Input: matrix = [[1,10,4,2],[9,3,8,7],[15,16,17,12]]
     *   Output: [12]
     *
     *   ->
     *
     *   [
     *    [1,10,4,2],
     *    [9,3,8,7],
     *    [15,16,17,12]
     *   ]
     *
     *
     *   ex 3)
     *
     *   Input: matrix = [[7,8],[1,2]]
     *   Output: [7]
     *
     *
     *   ->
     *
     *   [
     *   [7,8],
     *   [1,2]
     *   ]
     *
     *
     *
     */
    public List<Integer> luckyNumbers(int[][] matrix) {
        // edge

        int l = matrix.length;
        int w = matrix[0].length;

        List<Integer> res = new ArrayList<>();
        // get `max of each col
        List<Integer> colMax = new ArrayList<>();

        // ??
        for(int x = 0; x < w; x++){
            int colMaxVal = -1; // ??
            for(int y = 0; y < l; y++){
                colMaxVal = Math.max(colMaxVal, matrix[y][x]);
            }
            colMax.add(colMaxVal);
        }

        for(int y = 0; y < l; y++){
            int tmpVal = 0;
            int[] tmp = matrix[y];
            // ???
            for(int x = 0; x < w; x++){
                tmpVal = Math.min(tmpVal, tmp[x]);
            }
            if(tmpVal == colMax.get(y)){
                res.add(tmpVal);
                return res;
            }

        }


        return res;
    }


    // LC 1381
    // https://leetcode.com/problems/design-a-stack-with-increment-operation/
    // 16.08 - 18 pm
    /**
     *  -> Design a stack that supports increment operations on its elements.
     *
     *  void inc(int k, int val):
     *      Increments the `bottom` `k elements` of
     *      the stack by val.
     *        - If there are less than k elements
     *            -> in the stack, increment all the elements in the stack.
     *
     *
     *  ---------------
     *
     *   IDEA 1) DEQUEUE  ???
     *
     *
     *
     *   ---------------
     *
     *
     *
     */
    class CustomStack {
        // attr
        int maxSize;
        Deque<Integer> deque;
        //int size;

        public CustomStack(int maxSize) {
            this.maxSize = maxSize;
            this.deque = new ArrayDeque<>();
            //this.deque.size();
        }

        public void push(int x) {
            // ?? add VS push ??
            //this.deque.add(x);
            this.deque.push(x);

        }

        public int pop() {
            if(!this.deque.isEmpty()){
                return this.deque.pop();
            }
            return -1; // ???

        }

        public void increment(int k, int val) {
            //boolean lessEqualsThanK = (getSize() <= k);
            // FIFO
            Queue<Integer> tmpQ = new LinkedList<>();

            // case 1) size <= k
            if(getSize() <= k){
                while(!this.deque.isEmpty()){
                    int x = this.deque.poll();
                    tmpQ.add(x + 1);
                }
            }
            // case 1) size > k
            else{
                for(int i = 0; i < k; i++){
                    int x = this.deque.poll();
                    tmpQ.add(x + 1);
                }
            }

            // add back to dequeue
            while(!tmpQ.isEmpty()){
                this.deque.add(tmpQ.poll());
            }

        }

        public int getSize(){
            return this.deque.size();
        }

    }



    // LC 1382
    // https://leetcode.com/problems/balance-a-binary-search-tree/
    // 16.19 - 35 pm
    /**
     *  ->  return a balanced binary search tree with the same node values.
     *    If there is more than one answer, return any of them.
     *
     *      - given `root` of BST
     *         - return balanced BST
     *
     *
     *
     *    - NOTE:
     *       - A binary search tree
     *          is balanced if the depth of
     *          the two subtrees of every node never differs by more than 1.
     *
     *
     *  ------------------
     *
     *   IDEA 1) DFS
     *       -> build BST tree ???
     *
     *   IDEA 2) BFS ???
     *
     *
     *  ------------------
     *
     *
     */
    List<Integer> nodeList = new ArrayList<>(); //new ArrayList<>();
    public TreeNode balanceBST(TreeNode root) {
        // edge
        if(root == null){
            return root; // ???
        }
        if(root.left == null && root.right == null){
            return null;
        }

        // ?? get node list (in-order traversal)
        //List<Integer> list = getTreeNode(); //new ArrayList<>();
        getTreeNode(root);

        // ???
        return buildBSTHelper(root,
                nodeList,
                Integer.MAX_VALUE,
                -1 * Integer.MAX_VALUE);
    }

    // BST:  left < root < right
    // so, in-order traversal is already `ascending` order
    //
    private TreeNode buildBSTHelper(TreeNode root, List<Integer> list, int minVal, int maxVal){
        // edge
        if(root == null){
            return root; // ???
        }

        // ???
        int rootIdx = list.indexOf(root.val); // /???

        root.left = buildBSTHelper(root,
                list.subList(0, rootIdx),
                minVal,
                root.val);

        root.right = buildBSTHelper(root,
                list.subList(rootIdx + 1, list.size() - 1),
                root.val,
                maxVal);

        // /??
        return root;
    }

    // in-order traverse
    private void getTreeNode(TreeNode root){
        if(root == null){
            return;
        }
        getTreeNode(root.left);
        nodeList.add(root.val);
        getTreeNode(root.right);
       // return null;
    }


    // LC 1383
    // https://leetcode.com/problems/maximum-performance-of-a-team/
    // 16.43 - 53 pm
    /**
     *
     * -> Return the `maximum` performance of this team.
     *    Since the answer can be a huge number,
     *    return it modulo 109 + 7.
     *
     *    - Choose at most `k different engineers` out of the n engineers
     *      to form a team with the `maximum` performance.
     *
     *   performance = sum(speed)  * minimum efficiency
     *
     *
     *   -  engineers numbered from 1 to n
     *      -  1 to n (ID)
     *
     *
     *  -------------------
     *
     *   IDEA 1) MATH
     *
     *    -> per = (s1 + s2 + .. sk) * min(e1, e2, ... ek)
     *
     *    -> how to find max `per` ???
     *
     *    ->
     *
     *
     *   IDEA 2) PQ
     *
     *    -> per = (s1 + s2 + .. sk) * min(e1, e2, ... ek)
     *    -> max per if either ???
     *       - max (s1 + s2 + .. sk)
     *       or
     *       - max min(e1, e2, ... ek)
     *
     *
     *   IDEA 3) BRUTE FORCE ???
     *
     *
     *
     *  -------------------
     *
     *   ex 1)
     *
     *   Input: n = 6, speed = [2,10,3,1,5,8], efficiency = [5,4,3,9,7,2], k = 2
     *   Output: 60
     *
     *   ->
     *   speed = [2,10,3,1,5,8]
     *   efficiency = [5,4,3,9,7,2]
     *
     *   speed * eff = [10, 40, 9, 9, 35, 16]
     *
     *   -> id = 2 ,5
     *
     *   -> performance =  (10 + 5) * min(4, 7)
     *                  = 15 * 4 =  60
     *
     *
     *   ex 2)
     *    Input: n = 6, speed = [2,10,3,1,5,8], efficiency = [5,4,3,9,7,2], k = 3
     *    Output: 68
     *
     *    ->
     *
     *    speed = [2,10,3,1,5,8]
     *    efficiency = [5,4,3,9,7,2]
     *
     *,
     *    speed * eff = [10, 40, 9, 9, 35, 16]
     *
     *
     *    -> id = 1,2,5
     *
     *    -> performance = (2+10+5) * min(5,4,7)
     *                   = 17 * 4 = 68
     *
     *
     *
     *  ex 3)
     *  Input: n = 6, speed = [2,10,3,1,5,8], efficiency = [5,4,3,9,7,2], k = 4
     *  Output: 72
     *
     *
     *  ->
     *    speed = [2,10,3,1,5,8]
     *    efficiency = [5,4,3,9,7,2]
     *
     *
     *
     *
     *
     */
    public int maxPerformance(int n, int[] speed, int[] efficiency, int k) {

        return 0;
    }



}
