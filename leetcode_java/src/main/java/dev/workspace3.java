package dev;

import LeetCodeJava.DataStructure.ListNode;
import LeetCodeJava.DataStructure.Node;
import LeetCodeJava.DataStructure.TreeNode;

import java.util.*;
import java.util.stream.Collectors;

// https://javaguide.cn/java/concurrent/java-concurrent-questions-01.html#%E4%BD%95%E4%B8%BA%E7%BA%BF%E7%A8%8B

/** Progress, Thread test */

public class workspace3 {
    public static void main(String[] args) {

//        // 获取 Java 线程管理 MXBean
//        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
//        // 不需要获取同步的 monitor 和 synchronizer 信息，仅获取线程和线程堆栈信息
//        ThreadInfo[] threadInfos = threadMXBean.dumpAllThreads(false, false);
//        // 遍历线程信息，仅打印线程 ID 和线程名称信息
//        for (ThreadInfo threadInfo : threadInfos) {
//            System.out.println("[" + threadInfo.getThreadId() + "] " + threadInfo.getThreadName());
//        }

//        int n1 = 1;
//        int n2 = 2;
//        int n3 = 0;
//
//        String binary1 = Integer.toBinaryString(n1);
//        String binary2 = Integer.toBinaryString(2);
//        String binary3 = Integer.toBinaryString(3);
//
//        System.out.println(n1 + " in binary is: " + binary1);
//        System.out.println(n2 + " in binary is: " + binary2);
//        System.out.println(n3 + " in binary is: " + binary3);


//        // Small PQ (default min-heap)
//        PriorityQueue<Integer> smallPQ = new PriorityQueue<>();
//
//        // Big PQ (max-heap)
//        PriorityQueue<Integer> bigPQ = new PriorityQueue<>(Comparator.reverseOrder());
//
//        // Add elements to PQs
//        smallPQ.add(5);
//        smallPQ.add(10);
//        smallPQ.add(1);
//
//        bigPQ.add(5);
//        bigPQ.add(10);
//        bigPQ.add(1);
//
//        // Print elements from PQs
//        System.out.println("Small PQ (min-heap):");
//        while (!smallPQ.isEmpty()) {
//            System.out.println(smallPQ.poll());
//        }
//
//        System.out.println("Big PQ (max-heap):");
//        while (!bigPQ.isEmpty()) {
//            System.out.println(bigPQ.poll());
//        }

//        for (int i = 0; i <= 3; i++){
//            int b_cnt = Integer.bitCount(i);
//
//            /**
//             *
//             * i = 0 , b_cnt = 0
//             * i = 1 , b_cnt = 1
//             * i = 2 , b_cnt = 1
//             * i = 3 , b_cnt = 2
//             *
//             */
//            System.out.println("i = " + i + " , b_cnt = " + b_cnt);
//        }

//        int x = 1;
//        int y = 1;
//        System.out.println(x / y);
//
//        int[] a = new int[1];
//        a[0] = 1;
//        System.out.println(a[0] / 1);

        int[] newInterval = new int[]{1,2,3};

        /** NOTE !!!  create list from array */
        List<int[]> intervalList = new ArrayList<>(Arrays.asList(newInterval));

    }

    // LC 125
    public boolean isPalindrome(String s) {

        if (s == null || s.length() == 0){
            return true;
        }
        String s_updated = "";
        StringBuilder sb = new StringBuilder();
        //String[] s_split = s.split(",");
        char[] array = s.toCharArray();
        for (char x : array){
            String x_ = Character.toString(x);
            if (x_ != null && x_ != "" && Character.isAlphabetic(x)){
                sb.append(x_.toLowerCase());
            }
        }

        System.out.println(sb.toString());
        return sb.toString() == sb.reverse().toString();
    }

    // LC 121
    public int maxProfit(int[] prices) {

        if (prices.length == 0){
            return 0;
        }

        int res = 0;
        int min = -1;
        int max = -1;

        for (int i : prices){
            int cur = i; //prices[i];
            System.out.println("cur = " + cur);
            if (min == -1){
                min = cur;
                continue;
            }
            if (min > cur){
                min = cur;
                continue;
            }
            if (max == -1){
                max = cur;
                max = cur;
            }
            if (cur > max){
                max = cur;
            }
            int tmp = max - min;
            System.out.println("max = " + max + " min = " + min + " tmp = " + tmp);
            max = -1;
            res = Math.max(tmp, res);
        }

        return res;
    }

    // LC 371
    public int getSum(int a, int b) {

        String a_1 = Integer.toBinaryString(a);
        String b_1 = Integer.toBinaryString(b);

        if (a == 0 && b == 0){
            return 0;
        }

        if(a == 0 || b == 0){
            if (a == 0){
                return b;
            }
            return a;
        }

        int len = Math.max(a_1.length(), b_1.length());

        if (len > a_1.length()){
            a_1 = multiplyStr(a_1, len - a_1.length());
        }else{
            b_1 = multiplyStr(b_1, len - b_1.length());
        }

        char[] a_str = a_1.toCharArray();
        char[] b_str = b_1.toCharArray();

        int plus = 0;
        StringBuilder sb = new StringBuilder();

        for (int i = a_str.length-1; i >= 0; i--){

            int a_val = Integer.parseInt(String.valueOf(a_str[i]));
            int b_val = Integer.parseInt(String.valueOf(b_str[i]));

            int cur = a_val + b_val + plus;
            plus = 0;
            if (cur >= 2){
                plus = 1;
                cur -= 2;
            }
            sb.append(cur);
        }

        if (plus != 0){
            if (plus == 1){
                sb.append("1");
            }else{
                int gap = plus - 2;
                sb.append(gap);
                sb.append("1");
            }
        }

        String collected = sb.reverse().toString();
        System.out.println("collected = " + collected);
        int res = Integer.parseInt(collected, 2);
        System.out.println("res = " + res);
        return res;
    }

    // https://www.studytonight.com/java-examples/how-to-multiply-string-in-java
    public String multiplyStr(String str, int multiplier){

        if (str == null){
            return str;
        }

        StringBuilder sb = new StringBuilder();
        for (int x = 0; x < multiplier; x++){
            sb.append(0);
        }

        sb.append(str);
        return sb.toString();
    }

    // LC 242
    public boolean isAnagram(String s, String t) {

        if (s == null && t == null){
            return true;
        }

        if (s == null || t == null){
            return false;
        }

        Map<String, Integer> map = new HashMap();
        char[] s_array = s.toCharArray();
        char[] t_array = t.toCharArray();

        for (char x : s_array){
            String cur = String.valueOf(x);
            if (!map.containsKey(cur)) {
                map.put(cur, 1);
            }else{
                map.put(cur, map.get(cur)+1);
            }
        }

        for (char y: t_array){
            String cur = String.valueOf(y);
            if (!map.containsKey(cur)) {
                return false;
            }else{
                map.put(cur, map.get(cur)-1);
                if (map.get(cur) == 0){
                    map.remove(cur);
                }
            }
        }

        System.out.println("map = " + map);

        if (map.keySet().size() != 0){
            return false;
        }

        return true;
    }

    // LC 235
    // DFS
//    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
//
//        if (root.equals(p) || root.equals(q)) {
//            return root;
//        }
//
//        // BST property : right > root > left
//        if (p.val > root.val && q.val > root.val){
//            return this.lowestCommonAncestor(root.right, p, q);
//        }
//        if (p.val < root.val && q.val < root.val){
//            return this.lowestCommonAncestor(root.left, p, q);
//        }
//
//        // p, q are in different side (sub tree)
//        return root;
//    }

    // LC 105
    // DFS
//    public TreeNode buildTree(int[] preorder, int[] inorder) {
//
//        if (preorder == null && inorder == null){
//            return null;
//        }
//
//        if (preorder.length == 1 && inorder.length == 1){
//            return new TreeNode(preorder[0]);
//        }
//
//        TreeNode root = new TreeNode(preorder[0]);
//
//        int root_idx = -1;
//        for (int i = 0; i < inorder.length; i++){
//            if (inorder[i] == root.val){
//                root_idx = i;
//                break;
//            }
//        }
//
//        root.left = this.buildTree(
//                Arrays.copyOfRange(preorder, 1,root_idx+1),
//                Arrays.copyOfRange(inorder, 0,root_idx)
//        );
//
//        root.right = this.buildTree(
//                Arrays.copyOfRange(preorder, root_idx+1,preorder.length),
//                Arrays.copyOfRange(inorder, root_idx+1,inorder.length)
//        );
//
//        return root;
//    }

//    public TreeNode help(List<Integer> preorderList, List<Integer> inorderList, TreeNode res){
//
//        int root = preorderList.get(0);
//
//        int leftIdx = inorderList.get(root) - 1;
//
//        int left = inorderList.get(leftIdx);
//
//        res = new TreeNode(root);
//
//        res.left = new TreeNode(left);
//
//        res.right = this.help(Arrays.stream(preorderList, 1,2), inorderList, res);
//
//        return res;
//    }

    // LC 106
    // inorder : left -> root -> right
    // postorder : left -> right -> root
    public TreeNode buildTree(int[] inorder, int[] postorder) {

        if (postorder.length == 0) {
            return null;
        }

        // either each of below works
        if (postorder.length == 1) {
            return new TreeNode(postorder[0]);
        }

//        if (inorder.length == 1) {
//            return new TreeNode(inorder[0]);
//        }

        TreeNode root = new TreeNode(postorder[postorder.length - 1]);
        int root_idx = -1;
        for (int i = 0; i < inorder.length; i++) {
            /** NOTE !!! need to get root_idx from inorder */
            if (inorder[i] == postorder[postorder.length - 1]) {
                root_idx = i;
                break;
            }
        }

        root.left = this.buildTree(
                Arrays.copyOfRange(inorder, 0, root_idx),
                Arrays.copyOfRange(postorder, 0, root_idx)
        );
        root.right = this.buildTree(
                Arrays.copyOfRange(inorder, root_idx+1, inorder.length),
                Arrays.copyOfRange(postorder, root_idx, postorder.length-1)
        );

        return root;
    }

    // LC 104
    // DFS
//    public int maxDepth(TreeNode root) {
//
//        if (root == null){
//            return 0;
//        }
//
//        if (root.left == null && root.right == null){
//            return 1;
//        }
//
//        int leftDepth = this.maxDepth(root.left) + 1;
//        int rightDepth = this.maxDepth(root.right) + 1;
//
//        return Math.max(leftDepth, rightDepth);
//    }


    // LC 230
    // bfs
    public int kthSmallest(TreeNode root, int k) {

        if (root.left == null && root.right == null){
            return root.val;
        }

        PriorityQueue<Integer> pq = new PriorityQueue();
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);

        while(!queue.isEmpty()){
            TreeNode node = queue.poll();
            pq.add(node.val);
            if (node.left != null){
                queue.add(node.left);
            }
            if (node.right != null){
                queue.add(node.right);
            }
        }

        while (k > 1){
            pq.poll();
            k -= 1;
        }

        return pq.poll();
    }

    // LC 102
    // bfs
    public List<List<Integer>> levelOrder(TreeNode root) {

        if (root == null){
            return new ArrayList();
        }

        List<List<Integer>> res = new ArrayList<>();
        Queue<MyQueue> queue = new LinkedList<>(); // double check
        int layer = 0;
        queue.add(new MyQueue(layer, root));

        while (!queue.isEmpty()){
            MyQueue cur = queue.poll();
            int curLayer = cur.layer;
            if (curLayer >= res.size()){
                res.add(new ArrayList());
            }
            List<Integer> collected = res.get(curLayer);
            res.set(curLayer, collected);
            collected.add(cur.root.val);
            if (cur.root.left != null){
                queue.add(new MyQueue(curLayer+1, cur.root.left));
            }
            if (cur.root.right != null){
                queue.add(new MyQueue(curLayer+1, cur.root.right));
            }
        }

        return res;
    }

    public class MyQueue{
        public int layer;
        public TreeNode root;

        MyQueue(){

        }

        MyQueue(int layer, TreeNode root){
            this.layer = layer;
            this.root = root;
        }
    }

    // LC 100
    public boolean isSameTree(TreeNode p, TreeNode q) {

//        if (q == null && q == null){
//            return true;
//        }

        if (p == null && q == null) return true;

//        if ((p == null && q != null) || (p != null && q == null)){
//            return false;
//        }
        if (q == null || p == null){
            return false;
        }
        if (p.val != q.val){
            return false;
        }

        return this.isSameTree(p.left, q.left) &&
                this.isSameTree(p.right, q.right);
    }

    // LC 226
    // DFS
//    public TreeNode invertTree(TreeNode root) {
//
//        if (root == null){
//            return root;
//        }
//
//        TreeNode tmp = root.left;
//        root.left = invertTree(root.right);
//        root.right = invertTree(tmp);
//
//        return root;
//    }

    // LC 98
    // dfs

    /**
     *
     * A valid BST is defined as follows:
     *
     * 1) The left subtree of a node contains only nodes with keys less than the node's key.
     * 2) The right subtree of a node contains only nodes with keys greater than the node's key.
     * 3) Both the left and right subtrees must also be binary search trees.
     */
    public boolean isValidBST(TreeNode root) {

        if (root == null){
            return true;
        }

        if (root.left == null && root.right == null){
            return true;
        }

        int smallest_val = -1 * Integer.MAX_VALUE - 1;
        int biggest_val = Integer.MAX_VALUE + 1;
        return this.check_(root, smallest_val, biggest_val);
    }

    public boolean check_(TreeNode root, int smallest_val, int biggest_val){

        if (root == null){
            return true;
        }

        if (root.val <= smallest_val){
            return false;
        }

        if (root.val >= biggest_val){
            return false;
        }

        return this.check_(root.left, smallest_val, root.val) &&
                this.check_(root.right, root.val, biggest_val);
    }

    // LC 347
    public int[] topKFrequent(int[] nums, int k) {

        if (nums.length == 1 || nums.length == k){
            return nums;
        }


        Map<Integer, Integer> map = new HashMap<>();
        for (int x : nums){
            if (!map.containsKey(x)) {
                map.put(x, 1);
            }else{
                map.put(x, map.get(x)+1);
            }
        }

        // map(k,v) ->  inverseMap(v, k)
        Map<Integer, Integer> inverseMap = new HashMap<>();
        for (int key: map.keySet()){
            inverseMap.put(map.get(key), key);
        }

        System.out.println("map = " + map);
        System.out.println("inverseMap = " + inverseMap);

        int[] res = new int[k];
        List<Integer> countSort = map.values().stream().sorted(Collections.reverseOrder()).collect(Collectors.toList());
        System.out.println("countSort = " + countSort);

        for (int i = 0; i < k; i++){
            int val = inverseMap.get(countSort.get(i));
            System.out.println("val = " + val);
            res[i]= val;
        }

        System.out.println("res = " + res.toString());
        return res;
    }

    // LC 347 V2
    public int[] topKFrequent_1(int[] nums, int k) {
        // Step 1. Count the frequency of each element
        Map<Integer, Integer> countMap = new HashMap<>();
        for (int num : nums) {
            countMap.put(num, countMap.getOrDefault(num, 0) + 1);
        }

        // Step 2. Use a Min-Heap (Priority Queue) to keep track of top K elements
        PriorityQueue<Map.Entry<Integer, Integer>> heap = new PriorityQueue<>(
                (a, b) -> a.getValue() - b.getValue()
        );

        // Step 3. Maintain the heap of size k
        for (Map.Entry<Integer, Integer> entry : countMap.entrySet()) {
            heap.add(entry);
            if (heap.size() > k) {
                heap.poll();  // Remove the element with the smallest frequency
            }
        }

        // Step 4. Extract the elements from the heap
        int[] topK = new int[k];
        for (int i = 0; i < k; i++) {
            topK[i] = heap.poll().getKey();
        }

        return topK;
    }


    public int[] topKFrequent_0(int[] nums, int k) {

        // O(1) time
        if (k == nums.length) {
            return nums;
        }

        // Step 1. build hash map : character and how often it appears
        // O(N) time
        Map<Integer, Integer> count = new HashMap();
        for (int n: nums) {
            count.put(n, count.getOrDefault(n, 0) + 1);
        }

        // init heap 'the less frequent element first'
        //Queue<Integer> heap = new PriorityQueue<>((n1, n2) -> count.get(n1) - count.get(n2));
        Queue<Integer> heap = new PriorityQueue<>(
                // (a, b) -> a.getValue() - b.getValue()
                (a, b) -> a - b
        );

        /** NOTE !!! here */
        // Step 2. keep k top frequent elements in the heap
        // O(N log k) < O(N log N) time
        for (int n: count.keySet()) {
            heap.add(n);
            /** if size > k, remove smallest element (e.g. keep k top frequent elements in the heap) */
            if (heap.size() > k){
                heap.poll();
            }
        }

        // Step 3. build an output array
        // O(k log k) time
        int[] top = new int[k];
        for(int i = k - 1; i >= 0; --i) {
            top[i] = heap.poll();
        }
        return top;
    }

    // LC 91
    public int numDecodings(String s) {

        return 0;
    }

    // LC 213
    // brute force
    public int rob(int[] nums) {

        if (nums.length == 0){
            return 0;
        }


        return 0;
    }

    // LC 211
    // trie ? (dict + tree)
    // 1.30 pm
    class WordDictionary {

        class TrieNode{
            // attr
            private Map<String, TrieNode> child;
            private Boolean isEnd;

            TrieNode(){}
        }

        // attr
        private TrieNode trie;

        // constructor
        public WordDictionary() {
            this.trie = new TrieNode();
        }

        public void addWord(String word) {
            if (word == "" || word.length() == 0){
                return;
            }
            for (String x : word.split("")){
                if (!this.trie.child.containsKey(x)){
                    this.trie.child.put(x, new TrieNode());
                }
                // move to child node
                this.trie = this.trie.child.get(x);
            }

            // set cur node isEnd as true
            this.trie.isEnd = true;
        }

        public boolean search(String word) {
            TrieNode trie = this.trie;
            return this.search_sub(word, this.trie);
        }

        public boolean search_sub(String word, TrieNode trie) {

            for (int i = 0; i < word.length(); i++){
                char w = word.charAt(i);
                String x = String.valueOf(w);
                // case 1) no such element
                if (!trie.child.containsKey(x)){
                    // case 1-1) if "."
                    if (x == "."){
                        for (String key : trie.child.keySet()){
                            TrieNode new_trie = this.trie.child.get(key);
                            // start from idx+1 sub string
                            this.search_sub(word.substring(i+1), new_trie);
                        }
                    }
                    // case 1-2) if not "."
                    else{
                        return false;
                    }
                }
                // case 2) has such element
                else{
                    trie = trie.child.get(x);
                }
            }

            return trie.isEnd;
        }

    }

    // LC 338
    public int[] countBits(int n) {
        if (n == 0){
            return new int[]{0};
        }

        int[] res = new int[n+1];

        for (int i = 0; i < n+1; i++){
            /**
             *  Integer.bitCount
             *
             *  -> java default get number of "1" from binary representation of a 10 based integer
             *
             *  -> e.g.
             *      Integer.bitCount(0) = 0
             *      Integer.bitCount(1) = 1
             *      Integer.bitCount(2) = 1
             *      Integer.bitCount(3) = 2
             */
            int cnt = Integer.bitCount(i);
            res[i] = cnt;
        }
        return res;
    }


    // LC 208
    class Trie {

        public Trie() {

        }

        public void insert(String word) {

        }

        public boolean search(String word) {

            return false;
        }

        public boolean startsWith(String prefix) {

            return false;
        }
    }

    // LC 207
    // dfs ?
    // 0930 am

    /**
     *  prerequisites[i] = [ai, bi]
     *  indicates that
     *  you must take course "bi" first
     *  if you want to take course "ai"
     */
    public boolean canFinish(int numCourses, int[][] prerequisites) {

        if (numCourses == 0 && prerequisites.length == 0){
            return true;
        }

        if (prerequisites.length == 0){
            return true;
        }

        Map<Integer, List<Integer>> map = new HashMap<>();

        for (int[] x : prerequisites){
            if (!map.containsKey(x[0])){
                List<Integer> cur = new ArrayList<>();
                cur.add(x[1]);
                map.put(x[0], cur);
            }else{
                List<Integer> cur = map.get(x[0]);
                cur.add(x[1]);
                map.put(x[0], cur);
            }
        }

        System.out.println("map = " + map);

        int[] visited = new int[numCourses];
//         for (int i = 0; i < numCourses; i++){
//             visited[i] = 0;
//         }

        int res = 0;

        for (int i = 0; i < numCourses; i++){
            if (! this._help_check(i, visited, map, res)){
                return false;
            }
        }

        return res > 0;
    }

    public boolean _help_check(int courseId, int[] visited, Map<Integer, List<Integer>> map, int res){

        System.out.println("---> courseId = " + courseId + " map = " + map + " visited = " + visited);

        if (visited[courseId] == 1){
            return false;
        }

        if (visited[courseId] == 2){
            return true;
        }

        if (map.containsKey(courseId)){
            for (int val : map.get(courseId)){
                if (!this._help_check(val, visited, map, res)){
                    return false;
                }
            }
        }

        visited[courseId] = 2;
        res += 1;

        return true;
    }


    // LC 79
    // dfs
    public boolean exist(char[][] board, String word) {

        if (board.length == 1 && board[0].length == 1){
            return String.valueOf(board[0][0]) == word;
        }

        int l = board.length;
        int w = board[0].length;

        boolean[][] visited = new boolean[l][w];

        for (int i = 0; i < l; i++){
            for (int j = 0; j < w; j++){

                int idx = 0;
                String cur = "";
                String[] word_ = word.split("");

                if (dfs_(board, j, i, idx, word_, cur, visited)){
                    return true;
                }
            }
        }

        return false;
    }

    public boolean dfs_(char[][] board, int x, int y, int idx, String[] word_, String cur, boolean[][] visited){

        int l = board.length;
        int w = board[0].length;

        if (cur == word_.toString()){
            return true;
        }

        int[][] dirs = new int[][] { {0,1}, {0,-1}, {1,0}, {-1,0} };
        for (int[] dir : dirs){
            int x_ = x + dir[1];
            int y_ = y + dir[0];
            if (x_ >= 0 && x_ < w && y_ >= 0 && y_ < l){
                String tmp = String.valueOf(board[y_][x_]);
                if (tmp == word_[idx]){
                    cur += tmp;
                    visited[y_][x_] = true;
                    this.dfs_(board, x_, y_, idx+1, word_, cur, visited);
                }
            }
            visited[y_][x_] = false;
        }

        return false;
    }

    // LC 206
    public ListNode reverseList(ListNode head) {

        if (head == null){
            return head;
        }

        /**
         *    1 -> 2 -> 3
         *
         *   pre 1 -> 2 -> 3
         *    ↑  ↑
         *
         *   pre <- 1 -> 2 -> 3
         *          ↑    ↑
         *
         *   pre <- 1 <- 2 -> 3
         *               ↑    ↑
         */

        //ListNode root = head;
        ListNode prev = new ListNode();
        System.out.println("prev.val = " + prev.val);

        while (head != null){
            System.out.println("head = " + head.val);

            ListNode next = head.next;
            head.next = prev;
            prev = head;
            head = next;
        }

        return prev;
    }

    // LC 198
    // dp
    public int rob_(int[] nums) {

        if (nums.length <= 3){

            if (nums.length == 0){
                return 0;
            }

            if (nums.length == 1){
                return nums[0];
            }

            if (nums.length == 2){
                return Math.max(nums[0], nums[1]);
            }

            return Math.max(nums[0]+nums[2], nums[1]);
        }

        int[] dp = new int[nums.length];

        dp[0] = nums[0];
        dp[1] = Math.max(nums[0], nums[1]);

        // 1, 2, 3, ... k-2, k-1, k
        // dp[k] = max(dp(k-2) + k, dp(k-1))
        /**
         *  2,1,1,2
         *
         *  -> dp[0] = 2
         *  -> dp[1] = 2
         *
         *  -> dp[2] = max(dp[0]+nums[2], dp[1]) = 3
         *  -> dp[3] = max(dp[1]+nums[3], dp[2]) = 4
         *
         */
        for (int i = 2; i < nums.length; i++){
            dp[i] = Math.max(dp[i-2] + nums[i], dp[i-1]);
        }

//        System.out.println("dp = " + dp.toString());
//        for (int x : dp){
//            System.out.println(x);
//        }
        return dp[nums.length-1];
    }

    // LC 213
    public int rob_2(int[] nums) {

        if (nums.length <= 3){

            if (nums.length == 0){
                return 0;
            }

            if (nums.length == 1){
                return nums[0];
            }

            if (nums.length == 2){
                return Math.max(nums[0], nums[1]);
            }

            return Math.max(nums[0]+nums[2], nums[1]);
        }

        int[] dp = new int[nums.length];

        dp[0] = nums[0];
        dp[1] = Math.max(nums[0], nums[1]);

        /**
         *
         *  // 1, 2, 3, ... k-2, k-1, k
         *  // case 1) take dp[1]
         *
         *   dp[k] = max(dp(k-2), dp(k-1))
         *
         *  // case 2) NOT take dp[1]
         *
         *   dp[0] = 0
         *   dp[1] = nums[1]
         *   dp[k] = max(dp(k-2) + nums[k], dp(k-1))
         *
         *
         *  nums = [1,2,3,1]
         *
         *  case 1)
         *   dp[0] = 1
         *   dp[1] = 2
         *   dp[2] = 4
         *   dp[3] =
         *    max(dp(k-2), dp(k-1))  = 4
         *     or
         *    max(dp(k-2) + k, dp(k-1)) = 3
         *
         */

        for (int i = 2; i < nums.length; i++){
            dp[i] = Math.max(dp[i-2] + nums[i], dp[i-1]);
        }

        System.out.println("dp = " + dp.toString());
        for (int x : dp){
            System.out.println(x);
        }
        return dp[nums.length-1];
    }

    // LC 73
    public void setZeroes(int[][] matrix) {

        if (matrix.length == 0 && matrix[0].length == 0){
            return;
        }

        int l = matrix.length;
        int w = matrix[0].length;

        // get zero
        List<int[]> collected = new ArrayList<>();
        for (int i = 0; i < w; i++){
            for (int j = 0; j < l; j++){
                if(matrix[j][i] == 0){
                    collected.add(new int[]{i, j});
                }
            }
        }

        //System.out.println("collected = " + collected);
        for (int[] point : collected){

            System.out.println("x = " + point[0] + " y = " + point[1]);
            int x = point[0];
            int y = point[1];

            // make x - direction as zero
            for (int i = 0; i < w; i++){
                matrix[y][i] = 0;
            }

            // make y - direction as zero
            for (int j = 0; j < l; j++){
                matrix[j][x] = 0;
            }
        }

        return;
    }

    // LC 200
    // dfs
    public int numIslands(char[][] grid) {

        if (grid.length == 0 && grid[0].length == 0){
            return 0;
        }

        int l = grid.length;
        int w = grid[0].length;

        // get "1"
        List<int[]> collected = new ArrayList<>();
        for (int i = 0; i < w; i++){
            for (int j = 0; j < l; j++){
                if (grid[j][i] == '1'){
                    collected.add(new int[]{i,j});
                }
            }
        }

        System.out.println("--> collected = ");
        for (int[] point : collected){
            System.out.println("x = " + point[0] + " y = " + point[1]);
        }

        int res = 0;
        // dfs
        for (int[] point : collected){
            if (this.dfs_help(grid, point[0], point[1])){
                res += 1;
            }
        }

        return res;
    }

    public boolean dfs_help(char[][] grid, int x, int y){

        int l = grid.length;
        int w = grid[0].length;

        // dirs
        int[][] dirs = new int[][]{{0,1}, {0,-1}, {1,0}, {-1,0} };

        if (x < 0 || x >= w || y < 0 || y >= l || grid[y][x] != '1'){
            return false;
        }

        for (int[] dir: dirs){

            int x_ = x + dir[0];
            int y_ = y + dir[1];

            // mark as visited
            grid[y_][x_] = '#'; // NOTE !!! we setup char via single quote ('#')
            this.dfs_help(grid, x_, y_);
        }
        return true;
    }

    // LC 70
    public int climbStairs(int n) {

        if (n <= 2){
            if (n == 1){
                return 1;
            }
            return 2;
        }

        /**
         *
         *  0, 1, 2, 3..... k-2, k-1, k
         *  dp[k] = dp[k-2] + dp[k-1]
         *
         */

        int[] dp = new int[n+1];
        dp[0] = 1;
        dp[1] = 2;

        for (int i = 2; i < n; i++){
            //dp[i] = Math.max(dp[i-2]+1, dp[i-1]);
            dp[i] = dp[i-2] + dp[i-1];
            //System.out.println("dp[i] = " + dp[i]);
        }

        return dp[n-1];
    }

    // LC 323
    public int countComponents_1(int n, int[][] edges){


        return 0;
    }

    // LC 322
    // backtrack
    //int res = 0;
    List<List<Integer>> cache = new ArrayList<>();
    int minCnt = -1;

    public int coinChange(int[] coins, int amount) {

        if (coins.length == 1) {
            if (amount % coins[0] == 0){
                return  amount / coins[0];
            }
            return -1;
        }

        if (coins.length == 0){
            return -1;
        }


        List<Integer> cur = new ArrayList<>();
        this._backtrack(coins, amount, cur);

        return this.minCnt;
    }

    public void _backtrack(int[] coins, int amount, List<Integer> cur){

        System.out.println("_backtrack START");

        // TODO : double check
        int curSum = cur.stream().mapToInt(Integer::intValue).sum();

        if (curSum == amount){
            this.cache.add(cur);
            if (this.minCnt > 0){
                this.minCnt = Math.min(minCnt, cur.size());
            }else{
                this.minCnt = cur.size();
            }
            return;
        }

        if (curSum > amount){
            return;
        }

        for (int i = 0; i < coins.length; i++){
            if (coins[i] <= amount){
                cur.add(coins[i]);
                this._backtrack(coins, amount, cur);
                cur.remove(cur.size()-1);
            }
        }
    }


    // LC 191
    public int hammingWeight(int n) {

        String bin = Integer.toBinaryString(n);
        //System.out.println("bin = " + bin);
        int res = count_one(bin);
        //System.out.println("res = " + res);
        return res;
    }

    public int count_one(String input){
        int res = 0;
        for (String x : input.split("")){
            //System.out.println("x = " + x);
            if (x.equals("1")) {
                res += 1;
            }
        }
        return res;
    }


    // LC 190
    public int reverseBits(int n) {

        //String bin = Integer.toBinaryString(n);
//        // reverse
//        System.out.println("bin = " + bin);
        String res = reverse(String.valueOf(n));
//        System.out.println("res = " + res);

        int ans = Integer.parseInt(res, 2);
        System.out.println("ans = " + ans);
        return ans;
    }

    public String reverse(String input){
        String res = "";
        for (String x : input.split("")){
            //System.out.println("x = " + x);
            if (x.equals("1")) {
                res += "0";
            }else{
                res += "1";
            }
        }
        return res;
    }

    // LC 62
    // dp, math ?
    public int uniquePaths(int m, int n) {

        if (m == 1 && n == 1){
            return 1;
        }

        int m_ = m -1;
        int n_ = n - 1;
        int res = get_factorial(m_ + n_) / (m_ * n_);
        System.out.println("m_ = " + m_ + " n_ = " + n_ + " res = " + res);

        return res;
    }

    public int get_factorial(int n){
        if (n == 1){
            return 1;
        }
        int res = 1;
        for (int i = 1; i <= n; i++){
            res = res * i;
        }
        return res;
    }

    // LC 572
    // dfs
    public boolean isSubtree(TreeNode root, TreeNode subRoot) {

        if (root == null && subRoot.val == root.val){
            return true;
        }

        if (root == null || subRoot.val == root.val){
            return false;
        }

        //return false;
        return this.isSameTree_(root, subRoot) ||
                this.isSameTree_(root.left, subRoot) ||
                this.isSameTree_(root.right, subRoot);
    }

    public boolean isSameTree_(TreeNode root, TreeNode subRoot){

        System.out.println("root = " + root.val + " subRoot = " + subRoot.val);

        if (root.val != subRoot.val){
            return false;
        }

        if (root.left == null || subRoot.left == null){
            return false;
        }

        if (root.right == null || subRoot.right == null){
            return false;
        }

        return root.val == subRoot.val &&
                this.isSameTree_(root.left, subRoot.left) &&
                this.isSameTree_(root.right, subRoot.right);
    }

    // LC 57
    // idea : array
    public int[][] insert(int[][] intervals, int[] newInterval) {

        if (intervals.length == 0){
            if (newInterval.length == 0){
                return new int[][]{};
            }
            return new int[][]{newInterval};
        }

        List<int[]> intervalList = new ArrayList<>(Arrays.asList(intervals));
        intervalList.add(newInterval);
        intervalList.sort(Comparator.comparingInt(a -> a[0]));

        List<int[]> merged = new ArrayList<>();

        for (int[] x : intervalList){
            System.out.println("1st = " + x[0] + " 2nd = " + x[1]);
            // case 1) & case 2)
            if (merged.isEmpty() || merged.get(merged.size()-1)[1] < x[0]){
                merged.add(x);
            }
            // case 3)
            else{
                /**
                 *  if overlap
                 *   last : |-----|
                 *   x :      |------|
                 */
                merged.get(merged.size()-1)[0] = Math.min(merged.get(merged.size()-1)[0], x[0]);
                merged.get(merged.size()-1)[1] = Math.max(merged.get(merged.size()-1)[1], x[1]);
            }
        }

        return merged.toArray(new int[merged.size()][]);
    }

    // LC 56
    public int[][] merge(int[][] intervals) {

        if (intervals.length <= 1){
            return intervals;
        }

        List<int[]> intervalList = new ArrayList<>(Arrays.asList(intervals));

        // sort on 1st element (0 idx)
        //Object[] y = Arrays.stream(intervals).sorted(Comparator.comparingInt(x -> x[0])).toArray();
        intervalList.sort(Comparator.comparingInt(x -> x[0]));

        List<int[]> tmp = new ArrayList<>();
        for (int[] x : intervalList){
            System.out.println("x = " + x);
            // case 1 : tmp is null
            if (tmp.size() == 0){
                tmp.add(x);
            }
            // case 2 : no overlap
            if (tmp.get(tmp.size() - 1)[1] < x[0] || x[0] == x[1]){
                tmp.add(x);
            }
            // case 3 : overlap
            else{
                int[] last = tmp.get(tmp.size()-1);
                tmp.remove(tmp.size()-1);
                tmp.add(new int[]{Math.min(last[0], x[0]), Math.max(last[1], x[1])});
            }
        }

        // list -> array
        return tmp.toArray(new int[tmp.size()][]);
    }

    // LC 55
    public boolean canJump(int[] nums) {

        if (nums.length == 1){
            return true;
        }

        if (nums.length == 2){
            if (nums[0] > 0){
                return true;
            }
        }

        if (nums[0] == 0){
            return false;
        }

        int tmp = -1;
        int dist = 0;
        int startIdx = 0;

        for (int i = 0; i < nums.length - 1; i ++){
            int cur = nums[i];
            if (i == 0 || i - startIdx >=  tmp){
                startIdx = i;
                tmp = cur;
                dist += tmp;
            } else if (cur > tmp) {
                startIdx = i;
                tmp = Math.max(tmp, cur);
                dist += tmp;
            }
        }

        System.out.println("dist = " + dist);

        return dist >= nums.length-1; // dist < nums.length ? false : true;
    }

    // LC 53
    // sliding window
    public int maxSubArray(int[] nums) {

        if (nums.length==1){
            return nums[0];
        }

        int res = -1 * Integer.MAX_VALUE;
        int cur = 0;
        //List<Integer> cache = new ArrayList<>();
        int l = 0;
        int r = 0;

        while (r < nums.length){
            System.out.println("l = " + l + " r = " + r + " cur = " + cur + " res = " + res);
            int updated = cur + nums[r];
            if (r == 0 || updated > 0) {
                cur += nums[r];
                r += 1;
                res = Math.max(res, cur);
            } else {
                r += 1;
                l = r;
                cur = 0;
                res = Math.max(res, cur);
            }
        }

        return res;
    }

    /**
     *
     *  example 1)
     *
     *
     *  [[1,2],[2,3],[3,4],[1,3]]
     *
     *  -> sort
     *  [[1,2],[1,3],[2,3],[3,4]]
     *
     *
     *   1-2
     *   1---3
     *
     *
     *   example 2)
     *
     *   [[1,100],[11,22],[1,11],[2,12]]
     *
     *   -> sort
     *
     *   [[1,11],[2,12],[11,22], [1,100]]
     *
     *   -> [[1,11]]
     *   -> [[1,11]]
     *   -> [[1,11], [11,22]]
     *   -> [[1,11], [11,22]]
     */
    // LC 435
    public int eraseOverlapIntervals(int[][] intervals) {

        if (intervals.length <= 1){
            return 0;
        }

        int res = 0;

        // array -> list
        List<int[]> cur = new ArrayList<>(Arrays.asList(intervals));
        // sort on 2nd**** element
        cur.sort(Comparator.comparingInt(x -> x[1]));

        Stack<int[]> stack = new Stack<>();

        for(int[] x : cur){
            System.out.println("1st = " + x[0] + "2nd = " + x[1]);
            if (stack.empty()){
                stack.add(x);
            }else if (x[1] > stack.peek()[0]){
                res += 1;
            }else{
                if (stack.peek()[1] < x[1]) {
                    stack.pop();
                    stack.add(x);
                }
            }
        }

        return res;
    }

    // LC 49
    public List<List<String>> groupAnagrams(String[] strs) {

        List<List<String>> res = new ArrayList<>();

        if (strs.length == 1){
            List<String> tmp = new ArrayList<>();
            tmp.add(strs[0]);
            res.add(tmp);
            return res;
        }

        HashMap<String, List<String>> map = new HashMap<>();
        for (String x : strs){
            String[] x_ = x.split("");
            Arrays.sort(x_);
            String key = x_.toString();
            if (!map.containsKey(x_)){
                map.put(key, new ArrayList<>());
            }else{
                List<String> cur = map.get(key);
                cur.add(x);
                map.put(key, cur);
            }
        }

        for (String k : map.keySet()){
            res.add(map.get(k));
        }

        return res;
    }

    // LC 300
    // DP ?

    /**
     * A subsequence is an array that
     * can be derived from another array by deleting some or
     * no elements without changing the order of
     * the remaining elements.
     */
    public int lengthOfLIS(int[] nums) {

        if (nums.length <= 1){
            return nums.length;
        }

        int res = 0;

        // init dp
        int[] dp = new int[nums.length];
        dp[0] = 1;
        if (nums[1] > nums[0]){
            dp[1] = 2;
        }else{
            dp[1] = 1;
        }
        /**
         *  0, 1, 2, .... k-2, k-1, k
         *
         *  dp[k] =
         *
         */

        return res;
    }

    // LC 424
//    public int characterReplacement(String s, int k) {
//
//        if (s.length() < k){
//            return 0;
//        }
//
//        Map<String, Integer> map = new HashMap<>();
//        String[] s_array = s.split("");
//
//        // key : element, val : "closet" index
//        for (int i = 1; i < s_array.length; i++){
//            String key = s_array[i];
//            //map.put(key, i);
//            if (!map.containsKey(key)){
//                map.put(key, i);
//            }
//        }
//
//
//        int res = 0;
//        int tmp = 0;
//
//        String cur = null;
//
//        int l = 0;
//        int k_ = k;
//
//        for (int r = 1; r < s_array.length; r++){
//            if (s_array[r] != s_array[l]){
//                if (k_ > 0){
//                    k_ -= 1;
//                    cur += s_array[r];
//                    res = Math.max(res, r - map.get(s_array[l]) + 1);
//                }
//                else{
//                    k_ = k;
//                    l = r;
//                    cur = s_array[r];
//                }
//            }else{
//                cur = s_array[r];
//                res = Math.max(res, r - map.get(s_array[l]) + 1);
//            }
//        }
//
//        return res;
//    }
    public int characterReplacement(String s, int k) {

        if (s.length() < k){
            return 0;
        }

        Map<String, Integer> map = new HashMap<>();
        String[] s_array = s.split("");
        int l = 0;
        int res = 0;
        for (int r = 0; r < s_array.length; r++){
            String key = s_array[r];
            map.put(key, map.getOrDefault(key,0)+1);

            while ((r - l + 1) - getMaxValue(map) > k){
                map.put(s_array[l], map.get(s_array[l])-1);
                if (map.get(s_array[l]) == 0){
                    map.remove(s_array[l]);
                }
                l += 1;
            }

            res = Math.max(res, r - l + 1);
        }

        return res;

    }

    private int getMaxValue(Map<String, Integer> map) {
        int max = 0;
        for (int value : map.values()) {
            max = Math.max(max, value);
        }
        return max;
    }

    // LC 39
    // backtrack
//    List<List<Integer>> res = new ArrayList<>();
//    public List<List<Integer>> combinationSum(int[] candidates, int target) {
//        List<Integer> cur = new ArrayList<>();
//        //Arrays.sort(candidates); // Optional: sort to improve efficiency
//        backtrack_2(candidates, target, cur, res, 0);
//        return res;
//    }
//
//    public void backtrack_2(int[] candidates, int target, List<Integer> cur, List<List<Integer>> res, int idx){
//
//        if (getSum(cur) == target){
//            cur.sort(Comparator.comparingInt(x -> x));
//            if (!res.contains(cur)) {
//                res.add(new ArrayList<>(cur)); // NOTE here
//            }
//        }
//
//        if (getSum(cur) > target){
//            return;
//        }
//
//        for (int i = idx; i < candidates.length; i++){
//            cur.add(candidates[i]);
//            this.backtrack_2(candidates, target, cur, res, i);
//            // undo
//            cur.remove(cur.size()-1);
//        }
//    }
//
//    public int getSum(List<Integer> cur){
//        int res = 0;
//        for (int x : cur){
//            res += x;
//        }
//        return res;
//    }

    List<List<Integer>> res = new ArrayList<>();

    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        List<Integer> cur = new ArrayList<>();
        Arrays.sort(candidates); // Optional: sort to improve efficiency
        backtrack(candidates, target, cur, res);
        return res;
    }

    public void backtrack(int[] candidates, int target, List<Integer> cur, List<List<Integer>> res) {
        if (getSum(cur) == target) {
            res.add(new ArrayList<>(cur)); // Add a copy of the current list
            return;
        }

        if (getSum(cur) > target) {
            return;
        }

        for (int i = 0; i < candidates.length; i++) {
            cur.add(candidates[i]);
            backtrack(candidates, target, cur, res); // Not i+1 because we can reuse the same elements
            cur.remove(cur.size() - 1); // Undo the last addition
        }
    }

    public int getSum(List<Integer> cur) {
        int res = 0;
        for (int x : cur) {
            res += x;
        }
        return res;
    }

    // LC 206
    public ListNode reverseList_(ListNode head) {

        if (head == null) {
            return null;
        }

        ListNode root = new ListNode();
        ListNode _prev = null;
        root.next = _prev;

        while (head != null) {
            /**
             *  NOTE !!!!
             *
             *   4 operations
             *
             *    Step 0) set _prev as null
             *    step 1) cache next
             *    step 2) point cur to prev
             *    step 3) move prev to cur  (NOTE !!! the step)
             *    step 4) move cur to next
             */
            ListNode _next = head.next;
            head.next = _prev;
            /** NOTE !!!
             *
             *  -> have to assign _prev val to head first,
             *     then assign head val to _next,
             *     since if we assign head val to _next first,
             *     then head is changed (become "_next" node), then we will assign _prev to _next node,
             *     which is WRONG
             */
            _prev = head;
            head = _next;
        }

        // NOTE!!! we return _prev here, since it's now "new head"
        return root.next; //_prev;
    }

    // LC 417

    /**
     * Return a 2D list of grid coordinates result where result[i] = [ri, ci]
     * Denotes that rain water can flow from cell (ri, ci) to both the Pacific and Atlantic oceans.
     */

    // DFS
    public List<List<Integer>> pacificAtlantic(int[][] heights) {

        if (heights.length == 0 || heights[0].length == 0) {
            return new ArrayList<>();
        }

        int l = heights.length;
        int w = heights[0].length;

        int[][] landHeights = heights;
        boolean[][] pacificReachable = new boolean[l][w];
        boolean[][] atlanticReachable = new boolean[l][w];

        // move x-axis
        for (int x = 0; x < w; x++){
            this.dfs_flow(heights, x,0, atlanticReachable, "atlantic");
            this.dfs_flow(heights, x, l, pacificReachable, "pacific");
        }

        // move y-axis
        for (int y = 0; y < l; y++){
            this.dfs_flow(heights, 0, y, atlanticReachable, "atlantic");
            this.dfs_flow(heights, w, y, pacificReachable, "pacific");
        }

        List<List<Integer>> commonCells = new ArrayList<>();
        for (int i = 0; i < l; i++) {
            for (int j = 0; j < w; j++) {
                if (pacificReachable[i][j] && atlanticReachable[i][j]) {
                    commonCells.add(Arrays.asList(i, j));
                }
            }
        }
        return commonCells;
    }

    public void dfs_flow(int[][] heights, int x, int y, boolean[][] collected, String target){

        collected[y][x] = true;

        int[][] DIRECTIONS = new int[][]{{0, 1}, {1, 0}, {-1, 0}, {0, -1}};

        int l = heights.length;
        int w = heights[0].length;

        if (x < 0 || x >= w || y < 0 || y >= l){
            return;
        }

        // pacific
        if (target == "pacific") {
            if (y == 0 || x == 0){
                collected[y][x] = true;
                return;
            }
        }else{
            // atlantic
            if (y == l-1 || x == w-1){
                collected[y][x] = true;
                return;
            }
        }

        for (int[] dir : DIRECTIONS){
            int x_ = x+dir[0];
            int y_ = y+dir[1];
            if (x_ < 0 || x_ >= w || y_ < 0 || y_>= l || heights[y_][x_] < heights[y][x]){
                return;
            }
            this.dfs_flow(heights, x+dir[0], y+dir[1], collected, target);
        }
    }

    // LC 33
    // There is an integer array nums sorted in ascending order (with distinct values).
    // binary search
    public int search(int[] nums, int target) {

        if (nums.length == 0 || nums.equals(null)){
            return -1;
        }

        if (nums.length <= 1){
            if (nums[0] == target){
                return 0;
            }
            return -1;
        }

        /**
         *  rotated sorted array
         *
         *   if
         *      nums[k] < nums[k-1]
         *      and nums[k-1] > nums[k-2]
         *      and nums[k+2] < nums[k+3]
         *
         *   [4,5,6,7,0,1,2]
         *
         *
         *   example 1:
         *
         *    [4,5,6,7,0,1,2] , target = 0
         *     l     m     r   -> m > target, and nums[m] > nums[m+1]
         *                     -> m is pivot
         *                     -> move right
         *
         *             l m r    -> l = m+1, r = r
         *                      -> m > target,
         *                      -> r = m-1
         *             l
         *             r
         *             m        -> m = target, found!
         *
         *
         *   example 2 :
         *
         *   [4,5,6,7,0,1,2],  target = 3
         *    l     m     r   -> m > target, and nums[m] > nums[m+1]
         *                    -> m is pivot
         *                    -> move right
         *
         *            l m r    -> target > r, can't found -> return -1
         *
         */

        // 2 pm

        int l = 0;
        int r = nums.length - 1;

        while (r > l){

            int m = (l + r) / 2;

            System.out.println("l = " + l + " r = " + r + " mid = " + m);

            if (nums[m] == target){
                return m;
            }
            // target > m
            if (nums[m] > target){
                // m is pivot, right sub array is increasing
                if (nums[m+1] < nums[m]){
                    // not possible to find a solution ??
                    if (nums[r] > target){
                        l = m + 1;
                    }else{
                        r = m;
                    }
                }
            } // target < m
            else{
                // m is pivot, right sub array is increasing
                if (nums[m+1] < nums[m]){
                    // not possible to find a solution ??
                    if (nums[r] > target){
                        l = m+1;
                    }else{
                        r = m;
                    }
                }
            }
        }

        return -1;
    }

    public int search_2(int[] nums, int target) {

        if (nums.length == 0){
            return -1;
        }

        int l = 0;
        int r = nums.length - 1;

        // NOTE : "r >= l" as binary search condition
        while (r >= l){

            int mid = (l + r) / 2;
            int cur = nums[mid];

            // case 1)
            if (cur == target){
                return mid;
            }

            /**
             *  case 2) mid is pivot,  left half is sorted.
             *  left array is sorted (increasing)
             */
            else if (nums[mid] >= nums[l]){
                /**
                 *  NOTE !!!
                 *
                 *  since left sub array is sorted (increasing)
                 *  the only condition we can use is : left sub array
                 *  e.g. : check if target is bigger than left boundary or not
                 */
                /**
                 *
                 *  NOTE !!!
                 *  below is WRONG!!! (right sub array may have "pivot",
                 *  can't use right sub array  as condition
                 */
//                if (target > nums[mid] && nums[r] >= target){
//                    l = mid + 1;
//                }else{
//                    r = mid - 1; // NOTE !!!! not "r = mid"
//                }
                if (target >= nums[l] && target < cur) {
                    r = mid - 1;
                } else {
                    l = mid + 1;
                }
            }
            /**
             *  Case 3): right half is sorted (increasing)
             */
            /**
             *  NOTE !!!
             *
             *  since right sub array is sorted (increasing)
             *  the only condition we can use is : right sub array
             *  e.g. : check if target is smaller than right boundary or not
             */
            else{
                if (target > nums[mid] && target <= nums[r]){
                    l = mid + 1;
                }else{
                    r = mid - 1; // NOTE !!! not " r = mid"
                }
            }
        }

        return -1;
    }

    // LC 153

    /**
     * Suppose an array of length n sorted in ascending order is rotated
     * between 1 and n times. For example,
     *
     * the array nums = [0,1,2,4,5,6,7] might become:
     *
     * [4,5,6,7,0,1,2] if it was rotated 4 times.
     * [0,1,2,4,5,6,7] if it was rotated 7 times.
     *
     *
     * Given the sorted rotated array nums of unique elements,
     * return the minimum element of this array.
     *
     *
     * [1,2,3,4]
     *
     * [4,1,2,3]
     *  p
     *
     * [3,4,1,2]
     *    p
     * [2,3,4,1]
     *      p
     *
     * [1,2,3,4]
     *
     *
     *  3.10
     */
    public int findMin(int[] nums) {

        if (nums.length == 1){
            return nums[0];
        }

        // binary search
        int l = 0;
        int r = nums.length - 1;

        while (r >= l){

            int mid = (l + r) / 2;
            System.out.println("l = " + l + " r = " + r + " mid = " + mid);

//            // case 1) found pivot
//            if (nums[mid] > nums[mid-1]){
//                return nums[mid-1];
//            }

            if (nums[mid] > nums[mid+1]){
                return nums[mid+1];
            }

            if (nums[mid-1] > nums[mid]){
                return nums[mid];
            }

            // case 2) left half is sorted (increasing)
            else if (nums[mid] >= nums[l]){
                l = mid + 1;
            }
            // case 3) right half is sorted (increasing)
            else{
                r = mid - 1;
            }

        }

        // ??
        return nums[l];
    }

    // LC 152
    // A subarray is a contiguous non-empty sequence of elements within an array.

    /**
     *  dp
     *
     *  0, 1, 2,,, k-1, k
     *
     *  dp[k] = max(dp[k-2], dp[k-1] * num[k])
     *
     */
    public int maxProduct(int[] nums) {

        if(nums.length == 1){
            return nums[0];
        }

        int ans = 0;

        int[] dp = new int[nums.length+1];
        if (nums[0] > 0 && nums[1] > 0){
            dp[0] = nums[0];
            dp[1] = nums[0] * nums[1];
        }
        if (nums[0] < 0 && nums[1] < 0){
            dp[0] = 0;
            dp[1] = nums[0] * nums[1];
        }
        if (nums[0] == 0 || nums[1] == 0){
            if (nums[0] == 0){
                dp[0] = 0;
            }
        }


        return ans;
    }

    // LC 162
    public int findPeakElement_3(int[] nums) {
        int l = 0, r = nums.length - 1;
        while (l < r) {
            int mid = (l + r) / 2;
            if (nums[mid] > nums[mid + 1])
                r = mid;
            else
                l = mid + 1;
        }
        return l;
    }

    public int findPeakElement(int[] nums) {

        int l = 0;
        int r = nums.length - 1;

        if (nums.length == 1){
            return nums[0];
        }

        while (l < r) {
            int mid = (l + r) / 2;
            if (nums[mid] > nums[mid + 1])
                r = mid - 1;
            else
                l = mid + 1;
        }
        //  return r is OK as well
        // e.g. return r;
        return l;
    }

    // LC 21
    public ListNode mergeTwoLists(ListNode list1, ListNode list2) {

        if (list1 == null && list2 == null){
            return null;
        }

        if (list1 == null || list2 == null){
            if (list1 == null){
                return list2;
            }
            return list1;
        }

        /** NOTE here !!!
         *
         * Can't set res as null, but need to set as ListNode(),
         * since null has NO next attr
         */
        ListNode res = new ListNode();
        ListNode root = res;

        while (list1 != null && list2 != null){
            if (list1.val < list2.val){
                res.next = list1;
                list1 = list1.next;
            }else{
                res.next = list2;
                list2 = list2.next;
            }
            res = res.next;
        }

        if (list1 != null){
            res.next = list1;
        }else{
            res.next = list2;
        }

        return root.next;
    }

    // LC 20
    public boolean isValid(String s) {

        if (s == null || s.length() == 0){
            return true;
        }

        Map<String, String> map = new HashMap<>();
        map.put("(", ")");
        map.put("{", "}");
        map.put("[", "]");

        Stack<String> stack = new Stack<>(); // TODO : check

        for (String x : s.split("")){
            System.out.println("x = " + x + " stack = " + stack);
            if (map.containsKey(x)){
                stack.add(x);
            }else{
                if (stack.isEmpty()){
                    return false;
                }
                String last = stack.pop();
                if (!map.get(last).equals(x)){
                    return false;
                }
            }
        }

        return stack.isEmpty();
    }

    // LC 19
    // Given the head of a linked list,
    // remove the nth node from the "end" of the list and return its head.
    // 6.47
    public ListNode removeNthFromEnd(ListNode head, int n) {

        if (head.next == null){
            return null;
        }

        if (head.next.next == null){
            if (n == 1){
                return new ListNode(head.val);
            }
            return new ListNode(head.next.val);
        }

        // get len
        int len = 0;
        ListNode head_ = head;
        while (head_ != null){
            head_ = head_.next;
            len += 1;
        }


        System.out.println("len = " + len);

        ListNode root = new ListNode();
        ListNode root_ = root;

        // if n == len
        if (n == len){
            head = head.next;
            root.next = head;
            root = root.next;
        }

        /**
         *   0, 1, 2 , 3, 4 .... k-2, k-1, k
         *
         *   if n = 1, then "k-1" is the node to remove.
         *   -> so we find "k-2" node, and connect it to "k" node
         *
         *  example 1 :
         *    n = 2
         *    0, 1, 2, 3 ,4
         *             *
         *
         *   len = 5
         *   (len - n - 1) =  5 - 2 -1 = 2
         *
         */
        // find n-1 node, connect n-2 node and n node
        int idx = len - n;
        while (idx > 0){
            System.out.println("idx = " + idx + " head.val = " + head.val);
            root.next = head;
            root = root.next;
            head = head.next;
            idx -= 1;
        }

        ListNode next = head.next;
        root.next = next;

        return root_.next;
    }

    // LC 271
    public String encode(List<String> strs) {

        if (strs == null || strs.size() == 0){
            return "";
        }

        String res = "";
        for (String x : strs){
            if (x != ""){
                res = res + x + ",";
            }else{
                res = res + "?" + ","; // ?
            }
        }

        return res;
    }

    public List<String> decode(String s) {

        List<String> res = new ArrayList<>();

        if (s == ""){
            res.add("");
            return res;
        }

        for (String x : s.split(",")){
            if (x == "?"){
                res.add("");
            }else{
                res.add(x);
            }
        }

        return res;
    }

    // LC 269
    // idea : topological sort
    public String alienOrder(String[] words) {


        return null;
    }

    // LC 143

    /**
     *
     *
     * You are given the head of a singly linked-list.
     * The list can be represented as:
     *
     * L0 → L1 → … → Ln - 1 → Ln
     *
     *
     * Reorder the list to be on the following form:
     *
     * L0 → Ln → L1 → Ln - 1 → L2 → Ln - 2 → …
     *
     *
     *  example 1:
     *
     *  Input: head = [1,2,3,4]
     *  Output: [1,4,2,3]
     *
     *  make 2 node
     *   - 1st half node : 1->2
     *   - 2nd half  node : 3->4
     *
     * then reverse even node: 4 -> 3
     * then merge 2 node:
     *   start from odd, then even
     *   ...
     *
     *   1 -> 4 -> 2 -> 3
     *
     *
     *
     *  example 2 :
     *
     *  Input: head = [1,2,3,4,5]
     *  Output: [1,5,2,4,3]
     *
     *
     *  make 2 node
     *
     *  - 1st half node : 1->2 -> 3 (?)
     *  - 2nd half  node : 4 -> 5
     *
     *  reverse even node : 5 -> 4
     *
     *  then merge 2 node:
     *      start from odd, then even
     *      ...
     *
     *   1 -> 5 -> 2 -> 4 -> 3
     *
     */
    public void reorderList(ListNode head) {

        if (head == null || head.next == null){
            return;
        }

        // get len
        int len = 0;
        ListNode head_1 = head;
        while (head_1 != null){
            head_1 = head_1.next;
            len += 1;
        }

        // get 1st half, and 2nd half sub list node
        ListNode firstHalf = new ListNode();
        ListNode secondHalf = new ListNode();

        ListNode head_2 = head;
        int i = 0;
        while (i < len / 2){
            i += 1;
            firstHalf.next = head_2;
            head_2 = head_2.next;
        }

        secondHalf.next = head_2;

        // reverse secondHalf
        //ListNode reversedSecondHalf = new ListNode();
        ListNode prev = null;
        ListNode reversedSecondHalf = prev;
        while (secondHalf != null){
            ListNode next = secondHalf.next;
            secondHalf.next = prev;
            prev = secondHalf;
            secondHalf = next;
        }

        //ListNode reversedSecondHalf = secondHalf;

        // connect firstHalf and secondHalf
        ListNode res = new ListNode();
        while (firstHalf != null && reversedSecondHalf != null){
            res.next = firstHalf;
            firstHalf = firstHalf.next;
            res.next = reversedSecondHalf;
            reversedSecondHalf = reversedSecondHalf.next;
        }

    }

    // LC 268

    /**
     *  n = 3
     *  so sum_ = ((1+3)/ 2 ) * 3 = 6
     *  curSum = 4
     *  si 6 - 4 = 2
     *
     *  n = 2
     *  so sum_ = ((1+2)/ 2 ) * 2 = 3
     *  3 - 1 = 2
     *
     *
     */
    public int missingNumber(int[] nums) {

        int n = nums.length;
        int sum_ = ((1+n) * (n)) / 2;
        int curSum = Arrays.stream(nums).sum();
        System.out.println("sum_ = " + sum_ + " curSum = " + curSum);
        return sum_ - curSum;
    }

    // LC 139
    // backtrack
    boolean found = false;

    public boolean wordBreak(String s, List<String> wordDict) {

        if (wordDict.size() == 1){
            if (wordDict.get(0).equals(s)){
                return true;
            }
            return false;
        }

        // backtrack
        //return false;
        String cur = "";
        int startIdx = 0;
        this.canBuild(s, wordDict, startIdx, cur);
        return found;
    }

    public void canBuild(String s, List<String> wordDict, int startIdx, String cur){

        System.out.println("cur.equals(s) = " + cur.equals(s));
        if (cur.equals(s)){
            found = true;
            return;
        }

        if (cur.length() >= s.length()){
            return;
        }

        for (int idx = startIdx; idx < wordDict.size(); idx++){
            String tmp = wordDict.get(idx);
            String cur_ = cur + tmp;
            System.out.println("idx = " + idx + " tmp = " + tmp + " cur_ = " + cur_ + " cur = " + cur);
            if (s.contains(cur_)){
                this.canBuild(s, wordDict, startIdx, cur_);
                // undo
                cur_ = cur;
            }
        }
    }

    // LC 647
    // brute force + odd/even handling
//    public int countSubstrings(String s) {
//
//        if (s.length() == 1){
//            return 1;
//        }
//
//        int len = s.length();
//        int res = 0;
//        String[] s_arr = s.split("");
//        for (String x : s_arr){
//            System.out.println("---> x = " + x);
//        }
//
//        String s1 = s_arr[0];
//
//        /**
//         *  abc
//         *
//         *  abcd
//         *
//         *  aaa
//         *  l r
//         *
//         *
//         */
//
//        System.out.println("len = " + len);
//        System.out.println("s_arr = " + s_arr);
//
//        // odd
//        if (len % 2 == 1){
//            int l = 0;
//            int r = len - 1;
//            System.out.println("odd, l = " + l + ", r= " + r + "  s_arr[l] = " +  s_arr[l].toString());
//            System.out.println("s_arr[l].equals(s_arr[r]) = " + s_arr[l].equals(s_arr[r]));
//            while (l < len && r >= 0 && r >= l && s.charAt(l) == (s.charAt(r))){
//                l += 1;
//                r -= 1;
//                res = Math.max(res, r - l + 1);
//            }
//        }
//        // even
//        else{
//            int l = 1;
//            int r = len - 2;
//            System.out.println("even, l = " + l + ", r= " + r);
//            while (l < len && r >= 0 && r >= l && s.charAt(l) == (s.charAt(r))){
//                l += 1;
//                r -= 1;
//                res = Math.max(res, r - l + 1);
//            }
//
//        }
//
//        return res;
//    }

    public int countSubstrings(String s) {
        int ans = 0;
        for (int i = 0; i < s.length(); i++) {
            // odd
            ans += helper(s, i, i);
            // even
            ans += helper(s, i, i + 1);
        }
        return ans;
    }

    private int helper(String s, int l, int r) {
        int ans = 0;
        while (l >= 0 && r < s.length() && s.charAt(l) == s.charAt(r)) {
            l--;
            r++;
            ans++;
        }
        return ans;
    }


    // LC 261
    // dfs
    public boolean validTree_1(int n, int[][] edges) {

        if (edges.length == 0){
            return true;
        }

        // build map
        /**
         * edges = [[0,1], [0,2], [0,3], [1,4]]
         *
         *  -> [0,1] : 0 : start, 1 : root
         *  -> ... so map :
         *   {
         *    0 : [1,2,3],
         *    1: [4],
         *    2: [0],
         *    3: [0]
         *  }
         *
         */
        Map<Integer, List<Integer>>map = new HashMap<>();
        for (int[] x : edges){
            List<Integer> cur = map.getOrDefault(x, new ArrayList<>());
            cur.add(x[1]);
            map.put(x[0], cur);
        }
        System.out.println("map = " + map);

        return true;
    }

    public boolean dfsValidTree(int n, int[][] edges, int[] state, int status, Map<Integer, List<Integer>> map, Integer cur){
        /**
         *  3 state:
         *
         *  0 : not visited
         *  1 : visiting
         *  2 : visited
         */
        if (status == 1){
            return false;
        }

        if (n == 0){
            return true;
        }

        status = 1;

        n -= 1;

        for (Integer k : map.get(cur)){
            this.dfsValidTree(n, edges, state, status, map, k);
        }

        return false;
    }

    // LC 133
    public Node cloneGraph(Node node) {

        return null;
    }

    // LC 005
    // A string is palindromic if it reads the same forward and backward.
    // 4.20
    public String longestPalindrome(String s) {

        if (s.length() == 1){
            return s;
        }

        if (s.length() == 2){
            if (s.charAt(0) == s.charAt(1)){
                return s;
            }
            return String.valueOf(s.charAt(0));
        }

        int len = s.length();
        int max_len = 1;
        String res = "";
        for (int i = 0; i < len-1; i++){
            for (int j = i; j < len; j++){
                System.out.println("i = " + i + " j = " + j);
                if (IsPalindromic_(s, i, j)){
                    if (j-i+1 > max_len){
                        max_len = j-i+1;
                        res = s.substring(i, j+1);
                    }
                }
            }
        }

//        // odd
//        String odd_res = getMaxPalindromicLen(s, 0, 0);
//        // even
//        String even_res = getMaxPalindromicLen(s, 0, 1);
//        System.out.println("odd_res = " + odd_res + " even_res = " + even_res);
//        if (odd_res.length() > even_res.length()){
//            return odd_res;
//        }
        return res.length() > 0 ? res : String.valueOf(s.charAt(0));
    }

    public boolean IsPalindromic_(String s,int l, int r){
        while (r >= l){
            if (s.charAt(l) != s.charAt(r)){
                return false;
            }else{
                r -= 1;
                l += 1;
            }
        }
        return true;
    }

    // LC 003
    // A substring is a contiguous non-empty sequence of characters within a string.
    // s consists of English letters, digits, symbols and spaces.
    // 2 pointers
    // 5.10

    /**
     *  s = "abcabcbb"
     *       l
     *          r
     *
     *
     */
    public int lengthOfLongestSubstring(String s) {

        if (s.length() <= 1){
            return s.length();
        }
        int len = s.length();
        int ans = 0;
        int l = 0;
        int r = 1;
        Map<String, Integer> map = new HashMap<>();
        map.put(String.valueOf(s.charAt(l)), 1);
        while (r > l && r < len){
            System.out.println("l = " + l + " r = " + r + " map = " + map);
            String cur = String.valueOf(s.charAt(r));
            if (map.containsKey(cur)){
                //ans = Math.max(ans, r - l); // the len is from "last" r and l
                l = r;
                r += 1;
                // reset map
                //System.out.println(">>>> reset map ");
                map = new HashMap<>();
                //System.out.println(">>>> updated map  = " + map);
            }else{
                System.out.println("ohh");
                map.put(cur, 1);
                r += 1;
                ans = Math.max(ans, r - l + 1);
            }
        }

        return ans;
    }

    // LC 128
    // 2 pointers

    /**
     *
     *  example 1 :
     *
     *  [1, 2, 3, 4, 100, 200]
     *   l  r
     *   l     r
     *   l        r
     *   l            r
     *                l     r
     *
     *  example 2 :
     *
     *   [0, 0, 1, 2, 3, 4, 5, 6, 7, 8]
     *    l  r
     *       l  r
     *       l     r
     *       l        r
     *       l           r
     *       l              r
     *       l                 r
     *       l                   r
     *       l                       r
     *
     *
     */
    public int longestConsecutive(int[] nums) {

        if (nums.length <= 1){
            return nums.length;
        }
        //System.out.println(Arrays.toString(Arrays.stream(nums).toArray()));

        // only get non duplicated elements
        HashSet<Integer> set = new HashSet<>();
        for (int x : nums){
            set.add(x);
        }

        int i = 0;
        int[] nums2 = new int[set.size()];
        for (Iterator<Integer> it = set.iterator(); it.hasNext(); ) {
            int x = it.next();
            it.remove();
            nums2[i] = x;
            i += 1;
        }

        System.out.println(Arrays.toString(Arrays.stream(nums2).toArray()));

        // sort
        Arrays.sort(nums2);
        System.out.println(Arrays.toString(Arrays.stream(nums2).toArray()));

        int l = 0;
        int r = 1;
        int ans = 1;
        while (r < nums2.length && r > l){
            System.out.println("l = " + l + ", r = " + r + ", ans = " + ans);
            if (nums2[r-1] + 1 == nums2[r]){
                r += 1;
                ans = Math.max(ans, r - l);
            }else{
                System.out.println("ohhhh");
                ans = Math.max(ans, r - l);
                l = r;
                r += 1;
            }
        }

        //ans = Math.max(ans, r - l);

        return ans;
    }

    // LC 261
    // quick find
    public boolean validTree_2(int n, int[][] edges) {
        if (edges.length == 0){
            return true;
        }
        // init
        int[] relations = new int[n];
        for (int i = 0; i < n; i++){
            relations[i] = i;
        }

        // union find
        for (int[] edge: edges){
            int root1 = this.union_(edge[0], relations);
            int root2 = this.union_(edge[1], relations);
            if (root1 == root2){
                return false;
            }else{
                relations[root1] = root2;
            }
        }

        return edges.length == n-1;
    }

    public int union_(int x, int[] relations){
        if (x != relations[x]){
            return this.union_(relations[x], relations);
        }
        return x;
    }

    // LC 207
    // union find (CAN'T USE THIS)
    // 3.20
//    public boolean canFinish_1(int numCourses, int[][] prerequisites) {
//
//        if (prerequisites.length <= 1){
//            return true;
//        }
//
//        // init
//        // at beginning, every node is itself parent
//        int[] parents = new int[numCourses];
//        for (int i = 0; i < numCourses; i++){
//            parents[i] = i;
//        }
//
//        // union find
//        for (int[] item : prerequisites){
//            int p1 = this.findParent(item[0], parents);
//            int p2 = this.findParent(item[1], parents);
//            // if 2 node have same parent -> cyclic
//            if (p1 == p2){
//                return false;
//            }
//
//            // update parents (route compression)
//            parents[p1] = p2;
//        }
//
//        System.out.println("reach here !!!");
//        // Return true if you can finish all courses. Otherwise, return false.
//        return prerequisites.length == numCourses-1; // ?
//    }
//
//    public int findParent(int x, int[] parents){
//        if (x == parents[x]){
//            return x;
//        }else{
//            //parents[x] = this.findParent(parents[x], parents);
//            //return parents[x];
//            return this.findParent(parents[x], parents);
//        }
//    }

    // LC 207
    // dfs
    public boolean canFinish_3(int numCourses, int[][] prerequisites) {

        if (prerequisites.length == 0){
            return true;
        }

        // init
        int[] visited = new int[numCourses];
        Arrays.fill(visited, 0);

        Map<Integer, List<Integer>> map = new HashMap<>();
        for (int[] pre: prerequisites){
            int k = pre[0];
            if (!map.containsKey(k)){
                List<Integer> cur = new ArrayList<>();
                cur.add(pre[1]);
                map.put(k, cur);
            }else{
                List<Integer> cur = map.get(k);
                cur.add(pre[1]);
                map.put(k, cur);
            }
        }

        // dfs
        for (int i = 0; i < numCourses; i++){
            if (!canAttend(i, visited, map)){
                return false;
            }
        }

        return Arrays.stream(visited).sum() == numCourses;
    }

    /**
     *  0 : not visited
     *  1 : visiting
     *  2 : visited
     *
     */
    public boolean canAttend(int x, int[] visited, Map<Integer, List<Integer>> map){

        if (visited[x] == 2){
            return true;
        }

        if (visited[x] == 1){
            //if (x != )
            return false;
        }

        for (Integer key: map.keySet()){
            if (key == x){
                visited[x] = 1;
                this.canAttend(key, visited, map);
            }
        }

        visited[x] = 0;
        return true; // ??
    }

    // LC 133

    /**
     * Given a reference of a node in a connected undirected graph.
     *
     * Return a deep copy (clone) of the graph.
     *
     */
    // dfs
    // 4.50
    public Node cloneGraph_3(Node node) {

        if (node == null || node.neighbors == null){
            return node;
        }

        HashMap<Integer, Node> visited = new HashMap<>();

        Node res = new Node();
        // dfs

        return this.copyNode(visited, res);
    }

    public Node copyNode(HashMap<Integer, Node> visited, Node node){
        if (node == null){
            return null;
        }
        int cur_val = node.val;
        // case 1) already visited
        if (visited.containsKey(cur_val)){
            return visited.get(cur_val);
        }

        // NOTE !!! we init copied node as below
        Node copiedNode = new Node(node.val, new ArrayList());
        visited.put(cur_val, copiedNode);

        // case 2) node is NOT visited yet, we go through all its neighbors,
        for (Node _node : node.neighbors){
//            if (!visited.containsKey(_node)){
//                //visited.put(x.val, x);
//                //this.copyNode(visited, x);
//                copiedNode.neighbors.add(_clone(visited, _node));
//            }
            copiedNode.neighbors.add(copyNode(visited, _node));
        }

        return copiedNode;

//        //if (node ==)
//        if(node == res){
//            return node;
//        }
//        for (Node x: node.neighbors){
//            res.neighbors.add(this.copyNode(x, res));
//        }
//        return res;
    }

    // LC 139
    // backtrack
    public boolean wordBreak_1(String s, List<String> wordDict) {

        if (wordDict.size()==1){
            if (wordDict.get(0) == s){
                return true;
            }
        }

        int startIdx = 0;

        // backtrack
        if (!check_(startIdx, s, wordDict)){
            return false;
        }

        return true;
    }

    public boolean check_(int startIdx, String s, List<String> wordDict){

        if (startIdx == s.length()){
            return true;
        }

        if (startIdx > s.length()){
            return false;
        }

        for (int i = startIdx; i < wordDict.size(); i++){
            String tmp = wordDict.get(i);
            int rIdx = tmp.length() + startIdx;
            if (rIdx < s.length() && tmp.equals(s.substring(startIdx, rIdx))){
                this.check_(startIdx+1, s, wordDict);
                startIdx -= 1;
            }
        }

        return false;
    }

    // LC 139
    // BFS
    public boolean wordBreak_2(String s, List<String> wordDict){
        if (s == null || s.length() == 0 || wordDict == null || wordDict.size() == 0) {
            return false;
        }

        Queue<Integer> q = new LinkedList<>();
        //q.add()
        boolean[] visited = new boolean[s.length()];
        while (!q.isEmpty()){
            int i = q.poll();
            for (int j = i + 1; j <= s.length(); j++){
                if (wordDict.contains(s.substring(i, j))){
                    if (j == s.length()){
                        return true;
                    }
                    q.offer(j);
                }
            }
            visited[i] = true;
        }

        return false;
    }

    // LC 582

    /**
     *  Input:
     * pid =  [1, 3, 10, 5]
     * ppid = [3, 0, 5, 3]
     * kill = 5
     * Output: [5,10]
     *
     * Explanation:
     *            3
     *          /   \
     *         1     5
     *              /
     *             10
     * Kill 5 will also kill 10.
     *
     */
    public List<Integer> killProcess(List<Integer> pid, List<Integer> ppid, int kill) {

        if (!ppid.contains(kill) && !pid.contains(kill)){
            return null;
        }

        if (!ppid.contains(kill)){
            List<Integer> ans = new ArrayList<>();
            ans.add(kill);
            return ans;
        }

        // init
        /**
         *  {ppid : [pid1, pid2, ....], }
         *
         * pid =  [1, 3, 10, 5]
         * ppid = [3, 0, 5, 3]
         *
         * kill = 5
         *
         *
         *  so for
         *             3
         *          /   \
         *         1     5
         *              /
         *             10
         *
         *  map as below :
         *
         *   {
         *       3 : [1, 5,10],
         *       1: [],
         *       5: [10],
         *   }
         *
         *
         */
        Map<Integer, List<Integer>> map = new HashMap<>();
        for (int i = 0; i < ppid.size(); i+=1){
            int ppidVal = ppid.get(i);
            int pidVal = pid.get(i);
            map.put(ppidVal, new ArrayList<>());
//            if (!map.containsKey(ppidVal)){
//                map.put(ppidVal, new ArrayList<>());
//            }else{
//                List<Integer> list_ = map.get(ppidVal);
//                list_.add(pidVal);
//                map.put(ppidVal, list_);
//            }
        }

        // bfs
        Queue<Integer> q = new LinkedList<>();
        q.add(kill);
        //List<Integer> ans = new ArrayList<>();
        while (q.isEmpty()){
            int cur = q.poll();
            // ans.add(cur);
            if (ppid.contains(cur)){
                for (int j = 0; j < ppid.size(); j+=1){
                    if (ppid.get(j) == cur){
                        List<Integer> list_ = map.get(ppid.get(j));
                        list_.add(ppid.get(j));
                        map.put(ppid.get(j),list_);
                        q.add(pid.get(j));
                    }
                }
            }
        }
        System.out.println("map = " + map);
        return map.get(kill);
    }

    // LC 146

    /**
     * Design a data structure
     * that follows the constraints
     * of a Least Recently Used (LRU) cache.
     */
    class LRUCache {

        // key, value
        Map<Integer, Integer> map = new HashMap<>();
        int capacity;
        int elementCnt;
        Queue<Integer> queue; // FIFO
        //int timeClock;

        public LRUCache(int capacity) {
            this.map = new HashMap<>();
            this.capacity = capacity;
            this.elementCnt = 0;
            this.queue = new LinkedList<>();
        }

        public int get(int key) {
            if (this.map.keySet().size()==0){
                return -1;
            }
            return this.map.get(key);
        }

        public void put(int key, int value) {
            if (this.elementCnt >= capacity){
                if (!queue.isEmpty()){
                    queue.poll();
                    this.map.put(key, map.get(key)-1);
                    if (this.map.get(key) == 0){
                        this.map.remove(key);
                    }
                }
            }
            if(!this.map.containsKey(key)){
                map.put(key, value);
            }
        }
    }

    // LC 167
    // 2 ponter
    public int[] twoSum(int[] numbers, int target) {
        int[] ans = new int[2];
        int l = 0;
        int r = numbers.length-1;
        while (r >= l){
            int cur_s = numbers[l] + numbers[r];
            System.out.println("l = " + l + ", r = " + r + ", cur_s = " + cur_s);
            if (cur_s == target){
                ans[0] = l+1;
                ans[1] = r+1;
                return ans;
            } else if (cur_s < target){
                l += 1;
            }else{
                r -= 1;
            }
        }
        return ans;
    }

    // LC 121

    /**
     *  prices = [7,1,5,3,6,4]
     *
     *
     *  [7, 1 ,5,    3,  6,   4]
     *   r
     *  m=7
     *     r
     *     m=1
     *         r    r    r   r
     *         m=1  m=1 m=1  m=1
     *         M=5  M=5 M=6  M=6
     *         a=4  a=4 a=5  a=5
     *
     *
     *
     *  4.15
     */
    public int maxProfit_3(int[] prices) {
        if (prices.length <= 1){
            return 0;
        }

        int res = 0;
        int min = -1; //Integer.MAX_VALUE;
        int max = -1; //0;

        for (int x: prices){
            if (min == -1){
                min = x;
                continue;
            }
            if (max == -1){
                max = x;
                //continue;
            }
            if (x > max){
                max = x;
                //continue;
            }
            //int tmp = max - min;
            res = Math.max(res, max - min);
        }

        return res;
    }

    // LC 567

    /**
     * Given two strings s1 and s2,
     *
     * return true if s2 contains a permutation of s1, or false otherwise.
     *
     * In other words, return true if one of
     *
     * s1's permutations is the substring of s2.
     *
     */
    // sliding window
    public boolean checkInclusion(String s1, String s2) {
        if (s1.equals(s2)){
            return true;
        }

        Map<String, Integer> map = new HashMap<>();
        for (char x : s1.toCharArray()){
            map.put(String.valueOf(x), map.getOrDefault(x, 0)+1);
        }

        System.out.println("map = " + map);

        int l = 0;
        //int r = 0;
        while (l < s2.length()){
            int r = l;
            while (r < s1.length()){
                String cur = String.valueOf(s2.charAt(r));
                map.put(String.valueOf(r), map.getOrDefault(r, 0)+1);
                System.out.println("l = " + l + ", r = " + r + ", map = " + map);
                // l = r;
                r += 1;
                // reset map
                //map = new HashMap<>();
            }
            if (this.hasPermutation(l, r, s2, map)){
                return true;
            }
            //l += s1.length();
            l = r+1;
            //r += 1;
            map = new HashMap<>();
            System.out.println("new l = " + l  + ", new r = " + r + " reset map = " + map);
        }

        return false;
    }

    public boolean hasPermutation(int l, int r, String s, Map<String, Integer> map){
        String subS = s.substring(l, r);
        for (String x : subS.split("")){
            if (!map.containsKey(x)){
                return false;
            }
        }
        return true;
    }


    // LC 155
    // stack : FILO
    // PQ (?
    class MinStack {

        PriorityQueue<Integer> pq;

        public MinStack() {
            // ??
            //this.pq = new PriorityQueue<>(Comparator.reverseOrder());
            //this.pq = new PriorityQueue<>();
            //Comparator.comparingInt(a);
            this.pq = new PriorityQueue<>(Integer::compare);
        }

        public void push(int val) {
            this.pq.add(val);
        }

        public void pop() {
            if (!this.pq.isEmpty()){
                this.pq.poll();
            }
        }

        public int top() {
            if (!this.pq.isEmpty()){
                return this.pq.poll();
            }
            // ?
            return -1;
        }

        public int getMin() {
            return this.pq.peek();
        }
    }

    // LC 150
    // 4.35

    /**
     *  example 1)
     *
     *  Input: tokens = ["2","1","+","3","*"]
     *  Output: 9
     *  Explanation: ((2 + 1) * 3) = 9
     *
     *  ->
     *
     *  [2,1]
     *  [+]
     *
     *  ->
     *  (2+1) = 3
     *
     *  [3]
     *  []
     *
     *  ->
     *
     *  [3,3]
     *  [*]
     *
     *  ->
     *  [9]
     *
     *
     *  example 2)
     *
     *  Input: tokens = ["4","13","5","/","+"]
     *  Output: 6
     *  Explanation: (4 + (13 / 5)) = 6
     *                     i2   i1
     *
     *  ->
     *   [4,13,5]
     *   []
     *
     *  ->
     *
     *   [4,13,5]
     *   [/]
     *
     *   ->
     *
     *   [4, 13/5]
     *   [+]
     *
     *   ->
     *
     *   [4 + (13/5) ]
     *
     */
    public int evalRPN(String[] tokens) {

        if (tokens.length == 0){
            return 0;
        }
        int ans = 0;
        Stack<Integer> st1 = new Stack<>();
        Stack<String> st2 = new Stack<>();

        String operators = "+-*/";

        for (String x : tokens){
            System.out.println("x = " + x + ", st1 = " + st1 + ", st2 = " + st2);
            // case 1) st1 is NOT empty, so pop operator, and do calculation
            if (operators.contains(x)){
                st2.add(x);
                String op = st2.pop();
                Integer i1 = st1.pop();
                Integer i2 = st1.pop();
                Integer res = this.calculate(i1, i2, op);
                System.out.println("--> res = " + res);
                st1.add(res);
            }
            // case 2) st1 is empty, and input is in "+-*/", so append op to st2
//            else if (operators.contains(x)){
//                st2.add(x);
//            }
            // case 3) input is "number", append to st1
            else{
                st1.add(Integer.parseInt(x));
            }
        }

        System.out.println("---> st1 = " + st1);
        System.out.println("---> st2 = " + st2);

        while (!st1.empty()){
            ans += st1.pop();
        }

        return ans;
    }

    public Integer calculate(Integer i1, Integer i2, String op){
        if (op.equals("+")){
            return i1 + i2;
        }
        if(op.equals("-")){
            return  i2 - i1;
        }
        if(op.equals( "*")){
            return i1 * i2;
        }else{
            if (i1 == 0){
                return 0;
            }
        }
        return  i2 / i1;
    }

    // LC 22
    // 5.20

    /**
     *  example 1)
     *
     *  Input: n = 3
     *  Output: ["((()))","(()())","(())()","()(())","()()()"]
     *
     *   ((( )))
     *   () () ()
     *
     */
    public List<String> generateParenthesis(int n) {
        List<String> res = new ArrayList<>();
        if(n==1){
            res.add("(");
            res.add(")");
            return res;
        }

        return null;
    }

    // LC 739
    // 6.10
    /**
     *  example 1)
     *
     *  Input: temperatures = [73,74,75,71,69,72,76,73]
     *  Output: [1,1,4,2,1,1,0,0]
     *
     *  -> st = [0,0,0,0,0,0,0,0]
     *
     *  -> (inverse order), and find element which is SMALLER than cur,
     *  save the distance
     *
     *
     *
     *
     */
//    public int[] dailyTemperatures(int[] temperatures) {
//        if (temperatures.length == 1){
//            return new int[]{0};
//        }
//        int[] ans = new int[temperatures.length];
//        Arrays.fill(ans, 0);
//        Stack<Integer> st1 = new Stack<>();
//        for (Integer x : temperatures){
//            st1.add(x);
//        }
//
//        // reverse traverse
//        for (int j = ans.length-2; j > 0; j++){
//            Stack<Integer> st1_tmp = st1;
//            int idx = j;
//            while (!st1_tmp.isEmpty()){
//                int curTmp = st1_tmp.pop();
//                System.out.println("j = " + j + ",curTmp =" + curTmp + ", idx = " + idx);
//                if (curTmp > temperatures[j-1]){
//                    ans[j-1] = (idx - j);
//                    //continue;
//                }else{
//                    idx -= 1;
//                }
//            }
//        }
//
//        return ans;
//    }

    // LC 739

    /**
     *
     *  tmps = [73,74,75,71,69,72,76,73]
     *  st = []
     *  ans = [0,0,0,0,0,0,0,0]
     *
     *  -> [73,74,75,71,69,72,76,73]
     *     idx
     *  st = [73]
     *  ans = [0,0,0,0,0,0,0,0]
     *
     *  -> [73,74,75,71,69,72,76,73]
     *         idx
     *
     *  st = [74]
     *  ans = [1,0,0,0,0,0,0,0]
     *
     *
     *  -> [73,74,75,71,69,72,76,73]
     *            idx
     *
     *  st = [75]
     *  ans = [1,1,0,0,0,0,0,0]
     *
     *
     *  -> [73,74,75,71,69,72,76,73]
     *               idx
     *  st = [75, 71]
     *  ans = [1,1,0,0,0,0,0,0]
     *
     *
     * -> [73,74,75,71,69,72,76,73]
     *                 idx
     *  st = [75, 71, 69]
     *  ans = [1,1,0,0,0,0,0,0]
     *
     *  -> [73,74,75,71,69,72,76,73]
     *                     idx
     *
     * st = [75, 71, 69, 72]
     * ans = [1,1,0,0,0,0,0,0]
     *
     *
     * -> [73,74,75,71,69,72,76,73]
     *                       idx
     *
     * st = [76]
     * ans = [1,1,4,2,1,0,0,0]
     *
     *
     * -> [73,74,75,71,69,72,76,73]
     *                          idx
     *
     *  st = [76, 73]
     *  ans = [1,1,4,2,1,0,0,0]
     *
     *
     *
     **/
    public int[] dailyTemperatures(int[] temperatures) {

        if (temperatures.length == 1){
            return new int[]{0};
        }
        return null;
    }

    // LC 206
    public ListNode reverseList_2(ListNode head) {

        if (head == null || head.next == null){
            return head;
        }

        /**
         *  5 steps
         *
         *  1) init null pointer
         *  2) prev
         *  3) next
         *  4) cur -> prev
         *  5) cur = next
         */

        // NOTE !!! here
        ListNode prev = null; //new ListNode();
        while (head != null){
            ListNode next = head.next;
            head.next = prev;
            prev = head;
            head = next;
        }

        return prev;
    }

    // LC 143

    /**
     *  step 1) get len, and split listNode to left half, right half
     *
     *  step 2) reverse right half,
     *
     *  step 3) merge left and right half
     *
     */
    // 5.00
    public void reorderList_3(ListNode head) {

        if (head == null || head.next == null) {
            return;
        }

        ListNode fast = new ListNode();
        ListNode slow = new ListNode();
        // ?
        while (fast != null && fast.next != null){
            fast = fast.next.next;
            slow = slow.next;
        }
        // so fast is now "right half"
        // and slow is "left half"

        // reverse "right half"
        ListNode prev = null;
        ListNode cur = slow; // NOTE !! here
        while (cur != null){
            ListNode next = cur.next;
            cur.next = prev;
            prev = cur;
            cur = next;
        }

        ListNode secondHalf = prev;
        ListNode firstHalf = head;

        // merge 2 sub linded list
        // ?
        while (secondHalf.next != null){

            ListNode sec_next = prev.next;
            ListNode first_next = firstHalf.next;

            firstHalf.next = secondHalf;
            secondHalf.next = first_next;

            firstHalf = first_next;
            secondHalf = sec_next;
        }

//        while (prev != null && prev.next != null && slow != null &&  slow.next != null){
//            ListNode slow_next = slow.next;
//            ListNode prev_next = prev.next;
//            ListNode _slow = slow;
//            ListNode _prev = prev;
//            slow.next = _prev;
//            slow = slow_next.next;
//            prev = prev_next.next;
//        }

    }

    // LC 138
    class Node2 {

        // attr
        int val;
        Node2 next;
        Node2 random;

        // constructor
        public Node2(int val) {
            this.val = val;
            this.next = null;
            this.random = null;
        }

        public Node2(int _val,Node2 _next,Node2 _random) {
            val = _val;
            next = _next;
            random = _random;
        }
    }

    // LC 141
    public boolean hasCycle(ListNode head) {

        if (head == null || head.next == null || head.next.next == null){
            return false;
        }

        ListNode fast = head;
        ListNode slow = head;
        while (fast != null && fast.next != null){
//            fast = fast.next.next;
//            slow = slow.next;
            if (fast == slow){ // ?
                return true;
            }
            fast = fast.next.next;
            slow = slow.next;
        }

        return false;
    }


    // LC 226
    public TreeNode invertTree(TreeNode root) {
        if (root == null){
            return root;
        }
        // cache left, right sub tree first,
        // so below assign NOT affect each other
        TreeNode _left = root.left;
        TreeNode _right = root.right;

        root.left = this.invertTree(_right);
        root.right = this.invertTree(_left);
        return root;
    }

    // LC 104
    //int ans = 0;
    public int maxDepth(TreeNode root) {
        if (root == null){
            //return this.ans;
            return 0;
        }
//        if (root.left != null){
//            return this.maxDepth(root.left) + 1;
//        }

        return Math.max(
                this.maxDepth(root.left) + 1,
                this.maxDepth(root.right) + 1
        );

    }

    // LC 543
    public int diameterOfBinaryTree(TreeNode root) {

        return 0;
    }

    // LC 110
    // 6.55
    // Given a binary tree, determine if it is
    // height-balanced
    //.

    /**
     * A height-balanced binary tree
     * is a binary tree in which the depth of the
     * two subtrees of every node never differs by more than one.
     */
    public boolean isBalanced(TreeNode root) {

        if (root == null){
            return true;
        }

        // ?
        return Math.abs(this.height(root.left) - this.height(root.right)) < 2
                && isBalanced(root.left)
                && isBalanced(root.right);
    }

    private int height(TreeNode root) {
        // An empty tree has height -1
        if (root == null) {
            return -1;
        }
        return 1 + Math.max(height(root.left), height(root.right));
    }


//    public boolean checkSub(TreeNode root){
//        if (root.left == null && root.right == null){
//            return true;
//        }
//
//        if (root.left == null && root.right != null){
//            if (root.right.right != null || root.right.left != null){
//                return false;
//            }
//        }
//
//        if (root.right == null && root.left != null){
//            if (root.left.right != null || root.left.left != null){
//                return false;
//            }
//        }
//
//        if (root.right != null && root.left != null){
////            if (root.left.right != null || root.left.left != null){
////                return false;
////            }
//        }
//
//        return false;
//    }

    // LC 110
    public boolean isBalanced_2(TreeNode root) {

        if (root == null){
            return true;
        }

        return this.isBalanced_2(root.left) &&
                this.isBalanced_2(root.right) &&
                Math.abs(this.getMaxDepth(root.left) - this.getMaxDepth(root.right)) < 2;
    }

    public int getMaxDepth(TreeNode root){
        if (root == null){
            return 0;
        }
        return Math.max(
                this.getMaxDepth(root.left)+1,
                this.getMaxDepth(root.right)+1
        );
    }

    // LC 235
    // dfs
    // 4.10
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {

//        if (root == null){
//            return root;  // ?
//        }

        // NOTE !!! equals
        if (root.equals(p) || root.equals(q)){
            return root;
        }

        // case 1) root > p, q
        if (root.val > p.val && root.val > q.val){
            return this.lowestCommonAncestor(root.left, p, q);
        }

        // case 1') root < p, q
        if (root.val < p.val && root.val < q.val){
            return this.lowestCommonAncestor(root.right, p, q);
        }

        // case 2) root is between p, q
        // ->  // return root directly, since p, q MUST in different sides
//        if ((root.val < p.val && root.val > q.val) || (root.val > p.val && root.val < q.val)){
//            return root;
//        }
        return root;

        // case 2) found p, q in different side
        // case 3) root == p or root == q

        //return null;
    }

    // LC 236
    // 4.25
    public TreeNode lowestCommonAncestor2(TreeNode root, TreeNode p, TreeNode q) {

        // since p != q
//        if (root.equals(p) && root.equals(q)){
//            return root;
//        }

        if ((root == null) || (root.equals(p) || root.equals(q))){
            return root;
        }


        // ???
        TreeNode left = this.lowestCommonAncestor2(root.left, p, q);
        TreeNode right = this.lowestCommonAncestor2(root.right, p, q);

        if (left != null && right != null){
            return root;
        }

//        if (left == null || right == null){
//            if (left == null){
//                return right;
//            }
//            return left;
//        }
        return (left != null && right == null) ? left : right;
    }

    // LC 496

    /**
     *  example 1)
     *
     *   Input: nums1 = [4,1,2], nums2 = [1,3,4,2]
     *  -> Output: [-1,3,-1]
     *
     *
     */
    public int[] nextGreaterElement(int[] nums1, int[] nums2) {

        // init
        Map<Integer, Integer> nextGreater = new HashMap<>();
        //Deque<Integer> stack = new ArrayDeque<>();
        Stack<Integer> st = new Stack<>();

        // prepare next greater map
        for (int x : nums2){
            while (!st.isEmpty() && st.peek() < x){
                nextGreater.put(st.pop(), x);
            }
            st.push(x);
        }

        // prepare ans
        int[] ans = new int[nums1.length];
        for (int i = 0; i < nums1.length; i++){
            // NOTE !!!
            ans[i] = nextGreater.getOrDefault(nums1[i], -1);
        }

        return ans;
    }

    // LC 1448
    /**
     *  Given a binary tree root,
     *  a node X in the tree is named good if in the path
     *  from root to X there are no nodes with a value greater than X.
     *  Return the number of good nodes in the binary tree.
     *
     *
     *  NOTE : if in the path from root to X there are no nodes with a value greater than X.
     */

    // dfs (post order) (left -> right -> root)
    // step 1) go through nodes, collect path (root -> node)
    // step 2) check if  elements in path < X
    // step 3) return res
    int goodNodeCnt = 0;

    public int goodNodes(TreeNode root) {

        if (root == null){
            return 0;
        }

        if (root.left == null && root.right == null){
            return 1;
        }

        // dfs
        //List<Integer> collected = new ArrayList<>();
        this.isGoodNode(root, Integer.MIN_VALUE); // ?
        return goodNodeCnt;
    }

    public void isGoodNode(TreeNode root, int maxSoFar){

        if (root.val <= maxSoFar){
            //return;
            this.goodNodeCnt += 1;
        }

        if (root.left != null){
            this.isGoodNode(root.left, Math.max(root.val, maxSoFar));
        }

        if (root.right != null){
            this.isGoodNode(root.right, Math.max(root.val, maxSoFar));
        }

////        if (root == null){
////            //return true; // ? already reach the end
////        }
//
//        if (root.val <= cur.val){
//            //return false;
//        }
//
//        return this.isGoodNode(root, cur.left, collected) &&
//                this.isGoodNode(root, cur.right, collected);

    }

    // LC 703
    class KthLargest {

        PriorityQueue<Integer> pq;
        int k;
//        public KthLargest(){
//            this.pq = new PriorityQueue();
//            this.k = 0;
//        }

        public KthLargest(int k, int[] nums) {
            this.pq = new PriorityQueue();
            this.k = 0;
            for (int x : nums){
                pq.add(x);
            }
            while(pq.size() > k){
                pq.poll();
                //k -= 1;
            }
        }

        public int add(int val) {
            this.pq.add(val);
            if (this.pq.size() > k){
                pq.poll();
            }
            //this.pq.add(val);

            return this.pq.peek();
        }
    }


    // LC 1046
    public int lastStoneWeight(int[] stones) {

        if (stones.length == 0){
            return 0;
        }

        if (stones.length == 1){
            return stones[0];
        }

        // init a Max pq
        int ans = 0;
        PriorityQueue<Integer> pq = new PriorityQueue<>(Comparator.reverseOrder()); // ?
        for (int x : stones){
            pq.add(x);
        }

        System.out.println("pq = " + pq);

        // pop 2 biggest elements
        while (pq.size() >= 2){
            int first = pq.poll();
            int second = pq.poll();
            int tmp = 0;
            if (first > second){
                tmp = first - second;
            }
            pq.add(tmp);
        }

        return pq.size() == 0 ? 0 : pq.peek();
    }

    // LC 215
    public int findKthLargest(int[] nums, int k) {
        if (nums.length == 1){
            if (k == 1){
                return nums[0];
            }
            return -1; // ?
        }

        // init
        int ans = 0;
        PriorityQueue<Integer> pq = new PriorityQueue<>(Comparator.reverseOrder());
        for (int x : nums){
            pq.add(x);
        }
        System.out.println("pq = " + pq);
        int cur = -1;
        while (k > 0){
            cur = pq.poll();
            k -= 1;
        }

        return cur;
    }

    // LC 621

    /**
     *  Input: tasks = ["A","A","A","B","B","B"], n = 2
     *
     *
     */
//    public int leastInterval(char[] tasks, int n) {
//
//        return 0;
//    }

    // LC 78
    public List<List<Integer>> subsets(int[] nums) {

        List<List<Integer>> res = new ArrayList<>();
        if (nums.length == 0){
            return res;
        }
        // backtrack
        int start_idx = 0;
        List<Integer> cur = new ArrayList<>();
        System.out.println("(before) res = " + res);
        this.getSubSet(start_idx, nums, cur, res);
        System.out.println("(after) res = " + res);
        return res;
    }

    public void getSubSet(int start_idx, int[] nums, List<Integer> cur, List<List<Integer>> res){

        if (!res.contains(cur)){
            // NOTE !!! init new list via below
            res.add(new ArrayList<>(cur));
        }

        if (cur.size() > nums.length){
            return;
        }

        for (int i = start_idx; i < nums.length; i++){
            if (!cur.contains(nums[i])){
                cur.add(nums[i]);
                this.getSubSet(i+1, nums, cur, res);
                // undo
                cur.remove(cur.size()-1);
            }
        }
    }

    // LC 46
    public List<List<Integer>> permute(int[] nums) {

        List<List<Integer>> res = new ArrayList<>();

        if (nums.length == 1){
            List<Integer> cur = new ArrayList<>();
            cur.add(nums[0]);
            System.out.println("cur = " + cur);
            res.add(cur);
            System.out.println("res = " + res);
            return res;
        }

        // backtrack
        List<Integer> cur = new ArrayList<>();
        System.out.println("res = " + res);
        this.getPermutation(nums, cur, res);
        System.out.println("(after) res = " + res);
        return res;
    }

    public void getPermutation(int[] nums, List<Integer> cur, List<List<Integer>> res){

        if (cur.size() > nums.length){
            return;
        }

        if (cur.size() == nums.length){
            if (!res.contains(cur)){
                res.add(new ArrayList<>(cur));
            }
        }

        for (int i = 0; i < nums.length; i++){
            if (!cur.contains(nums[i])){
                cur.add(nums[i]);
                this.getPermutation(nums, cur, res);
                // undo
                cur.remove(cur.size()-1);
            }
        }
    }

    // LC 40
//    public List<List<Integer>> combinationSum2(int[] candidates, int target) {
//
//        List<List<Integer>> res = new ArrayList<>();
//        if (candidates.length == 1){
//            if (candidates[0] == target){
//                List<Integer> cur = new ArrayList<>();
//                cur.add(candidates[0]);
//                res.add(cur);
//                return res;
//            }
//        }
//
//        return null;
//    }

    // LC 496
    // stack + map

    /**
     *
     *  Example 1)
     *
     *  nums2 = [1,3,4,2]
     *           x
     *             x
     *               x
     *                 x
     *  st = [1]
     *  st = [3]  map : {1:3}
     *  st = [4], map : {1:3, 3:4}
     *  st = [], map : {1:3, 3:4}
     *
     *
     *  nums1 = [4,1,2]
     *
     *  so, res = [-1, 3, -1]
     *
     *
     *  Example 2)
     *
     *   nums2 = [6,5,4,3,2,1,7]
     *            x
     *              x
     *               x
     *                 x
     *                   x
     *                     x
     *                       x
     *                         x
     *
     *  st = [6], map :{}
     *  st = [6,5],  map :{}
     *  ..
     *
     *  st = [6,5,4,3,2,1], map = {}
     *
     *  st = [], map = {6:7, 5:7,4:7,3:7,2:7,1:7}
     *
     *
     *
     *   nums1 = [1,3,5,2,4]
     *
     *
     */
    public int[] nextGreaterElement_2(int[] nums1, int[] nums2) {

        if (nums1.length == 1 && nums2.length == 1){
            return new int[]{-1};
        }

        // map collect next greater element
        // map : {element, next-greater-element}
        Map<Integer, Integer> map = new HashMap<>();
        Stack<Integer> st = new Stack<>();

        for (int x : nums2){
            while(!st.isEmpty() && st.peek() < x){
                int cur = st.pop();
                map.put(cur, x);
            }
            st.add(x);
        }

        System.out.println("map = " + map);

        int[] res = new int[nums1.length];
        Arrays.fill(res, -1);
        for (int j = 0; j < nums1.length; j++){
            if(map.containsKey(nums1[j])){
                res[j] = map.get(nums1[j]);
            }
        }

        System.out.println("res = " + res);

        return res;
    }

    // LC 739
    // stack

    /**
     *  Example 1)
     *
     *     [73,74,75,71,69,72,76,73]
     *      x
     *         x
     *            x
     *               x
     *                 x
     *                    x
     *                       x
     *                           x
     *
     *
     *   st = [73], map = {}
     *   st = [74], map = {73:1}
     *   st = [75], map = {73:1, 74:1}
     *   st = [75, 71],   map = {73:1, 74: 1}
     *   st = [75,71,69],  map = {73:1, 74: 1}
     *   st = [75,72],  map = {73:1, 74: 1, 69:1, 71:2}
     *   st = [76],  map = {73:1, 74: 1, 69:1, 71:2, 72:1, 75:3}
     *   st = [76,73], map = {73:1, 74: 1, 69:1, 71:2, 72:1, 75:3}
     */
    public int[] dailyTemperatures2(int[] temperatures) {

        if (temperatures.length == 1){
            return temperatures;
        }

        // get "next warmer" temperature
        Stack<List<Integer>> st = new Stack<>(); // element, idx
        //Map<Integer, Integer> map = new HashMap<>(); // {temperature : idx-of-next-warmer-temperature}
        int[] nextGreater = new int[temperatures.length];
        Arrays.fill(nextGreater, 0); // idx : temperature, val : idx-of-next-warmer-temperature
        for (int j = 0; j < temperatures.length; j++){
            int x = temperatures[j];
            while (!st.isEmpty() && st.peek().get(0) < x){
                //map.put(st.peek().get(0), j - st.peek().get(1));
                nextGreater[st.peek().get(1)] = j - st.peek().get(1);
                st.pop();
            }
            List<Integer> cur = new ArrayList<>();
            cur.add(x); // element
            cur.add(j); // idx
            st.add(cur);
        }

        System.out.println("nextGreater = " + nextGreater);
        return nextGreater;
    }

    // LC 236
    // dfs
    // 2.15
//    public TreeNode lowestCommonAncestor3(TreeNode root, TreeNode p, TreeNode q) {
//
//        if(root == null && p == null && q == null){
//            return root;
//        }
//
//        if (root == p || root == q){
//            return root;
//        }
//
//        //TreeNode res = null;
//        //this.findLCA(res, p, q);
//        return this.findLCA(root, p, q);
//    }
//
//    public TreeNode findLCA(TreeNode root, TreeNode p, TreeNode q){
//
//        if(root == null){
//            return root; // ?
//        }
//
//        if (root == p || root == q){
//            return root;
//        }
//
//        if (root.left != null){
//           this.findLCA(root.left, p, q);
//        }
//        if (root.right != null){
//            this.findLCA(root.right, p, q);
//        }
//
//        // ?
//        return root;
//    }

    public TreeNode lowestCommonAncestor3(TreeNode root, TreeNode p, TreeNode q) {

        if (root == null  || (root.equals(p) || root.equals(q)) ){
            return root;
        }

        TreeNode left = this.lowestCommonAncestor3(root.left, p, q);
        TreeNode right = this.lowestCommonAncestor3(root.right, p, q);

        if (left != null && right != null){
            return root;
        }

        return left != null ? left : right;
    }

    // LC 1448
    /**
     * Given a binary tree root,
     *
     * a node X in the tree is named good if in the path from root to X
     *
     * there are no nodes with a value greater than X.
     *
     * Return the number of good nodes in the binary tree.
     *
     */
    // 2.33
    int cnt = 0;

    public int goodNodes2(TreeNode root) {

        if (root.left == null && root.right == null){
            return 1;
        }

        // dfs
        //int cnt = 0;
        TreeNode cur = root;
        //Boolean found = true;
        this.checkGoodNode2(root, cur, root.val);
        return this.cnt;
    }

    public void checkGoodNode2(TreeNode root, TreeNode cur, int curSmall){

        System.out.println("cur = " + cur.val + " , curSmall = " + curSmall);

//        if (cur.val < curSmall){
//            flag = false;
//        }

        if (cur.val > curSmall){
            this.cnt += 1;
        }

        if(cur.left != null){
            this.checkGoodNode2(root, cur.left, Math.max(cur.val, root.val));
        }

        if(cur.right != null){
            this.checkGoodNode2(root, cur.right, Math.max(cur.val, root.val));
        }
    }

    // LC 208
    // 3.20
    class Trie3 {

        // attr
        //Trie3 node;
        //Map<Trie3, List<Trie3>> children;
        Map<String, Trie3> children;
        Boolean isEnd;

        public Trie3() {
            //this.node = new Trie3();
            //this.val = wor
            this.children = new HashMap<>();
            this.isEnd = true;
        }

        public void insert(String word) {
            Trie3 cur = null;
            //this.children.put(new Trie3())
            //this.node = new Trie3();
            for (String x : word.split("")){
                if (!this.children.containsKey(x)){
                    this.children.put(x, new Trie3());
                }
                cur = this.children.get(x);
            }

            cur.isEnd = true;
        }

        public boolean search(String word) {

            Trie3 cur = null;
            for (String x : word.split("")){
                cur = this.children.get(x);
                if(!this.children.containsKey(x)){
                    return false;
                }
                //cur = this.children.get(x);
            }

            return cur.isEnd; // check if is end
        }

        public boolean startsWith(String prefix) {

            Trie3 cur = null;
            for (String x : prefix.split("")){
                cur = this.children.get(x);
                if(!this.children.containsKey(x)){
                    return false;
                }
                //cur = this.children.get(x);
            }

            return true;
        }
    }

    // LC 695
    // dfs
//    int biggestArea = 0;
//    public int maxAreaOfIsland(int[][] grid) {
//
//        if (grid.length == 1 && grid[0].length == 1){
//            if (grid[0][0] == 1){
//                return 1;
//            }
//            return 0;
//        }
//
//        int l = grid.length;
//        int w = grid[0].length;
//
//        for (int i = 0; i < w; i++){
//            for (int j = 0; j < l; j++){
//                if (grid[j][i] == 1){
//                    this.getBiggestArea(i, j, grid, 1);
//                }
//            }
//        }
//
//        return this.biggestArea;
//    }
//
//    public void getBiggestArea(int x, int y, int[][] grid, int curArea){
//
//        int l = grid.length;
//        int w = grid[0].length;
//
//        System.out.println("x = " + x + ", y = " + y + ", curArea = " + curArea + ", this.biggestArea = " + this.biggestArea);
//
//        this.biggestArea = Math.max(this.biggestArea, curArea);
//
//        if (grid[y][x] != 1){
//            //this.biggestArea = Math.max(this.biggestArea, curArea);
//            curArea = 0;
//            //return false;
//            return;
//        }
//
//        grid[y][x] = -1; // mark as "visited"
//
//        int[][] dirs = { {0,1}, {0,-1}, {1,0}, {-1,0} };
//        for (int[] dir : dirs){
//            int x_ = x + dir[0];
//            int y_ = y + dir[1];
//            if ( x_ < w && x_ >= 0 && y_ < l && y_ >= 0 && grid[y_][x_] == 1){
//                this.getBiggestArea(x_, y_, grid, curArea+1);
//            }
//        }
//
//        //return true;
//    }

    int biggestArea = 0;

    public int maxAreaOfIsland(int[][] grid) {
        int l = grid.length;
        int w = grid[0].length;

        for (int i = 0; i < l; i++) {
            for (int j = 0; j < w; j++) {
                if (grid[i][j] == 1) {
                    /** NOTE !!!
                     *
                     *  Reset curArea for each new island found
                     */
                    int curArea = 1;
                    getBiggestArea(j, i, grid, curArea);
                    biggestArea = Math.max(biggestArea, curArea);
                }
            }
        }
        return biggestArea;
    }

    public void getBiggestArea(int x, int y, int[][] grid, int curArea) {

        int l = grid.length;
        int w = grid[0].length;

        // NOTE !!! below check if optional
//        if (x < 0 || x >= w || y < 0 || y >= l || grid[y][x] != 1) {
//            return 0;
//        }

        grid[y][x] = -1; // mark as visited
        //int area = 1; // current cell

        int[][] dirs = { {0, 1}, {0, -1}, {1, 0}, {-1, 0} };
        for (int[] dir : dirs) {
            int x_ = x + dir[0];
            int y_ = y + dir[1];
            if (x_ >= 0 && x_ < w && y_ >= 0 && y_ < l && grid[y_][x_] == 1) {
                curArea += 1;
                this.getBiggestArea(x_, y_, grid, curArea);
            }
        }

        //return curArea;
    }

    // LC 22
    // IDEA : Backtrack
    // 1 <= n <= 8
    // 3.30
    List<String> res_ = new ArrayList<>();

    public List<String> generateParenthesis2(int n) {

        //List<String> res = new ArrayList<>();
        if (n == 1){
            this.res_.add("(");
            this.res_.add(")");
            return this.res_;
        }

        // backtrack
        String cur = "";
        this.getParenthesis_(n, cur);
        System.out.println("this.res_ = " + this.res_);
        return this.res_;
    }

    // leftCnt : "(" count
    // rightCnt : ") count
    public void getParenthesis_(int n, String cur) {

        if (cur.length() > 2 * n){
            return;
        }

        if (isValidP(cur.toString()) && cur.toString().length() == 2 * n) {
            if (!this.res_.contains(cur)) {
                this.res_.add(cur);
                cur = ""; // ?
                return;
            }
        }

        String[] choices = {"(", ")"};
        StringBuilder sb = new StringBuilder(cur);
        for (String c : choices) {
            /**
             *  2 conditions
             *
             *  1)  leftCnt > rightCnt
             *  2)  1st element must be "("
             *
             */
            // case 1) invalid string
            if (!isValidP(cur)) {
                return;
            }

            // case 2) cur is null
            if (sb.length() == 0) {
                sb.append("(");
            }else{
                // case 3) OK to append "(" or ")"
                sb.append(c);
            }
            // undo
            this.getParenthesis_(n, sb.toString());
            sb.deleteCharAt(sb.length() - 1);
        }
    }

    public boolean isValidP(String input){
        if (input.length() == 0){
            return true;
        }
        // leftCnt : "(" count
        // rightCnt : ") count
        int leftCnt = 0;
        int rightCnt = 0;
        for (String x : input.split("")){
            if (x.equals("(")){
                leftCnt += 1;
            }else{
                rightCnt += 1;
            }
            if (rightCnt > leftCnt){
                return false;
            }
        }
        return true;
    }

    // LC 207
    public boolean canFinish_5(int numCourses, int[][] prerequisites) {

        if (prerequisites.length == 0){
            return true;
        }

        if (prerequisites.length == 1 && numCourses == 1){
            return true;
        }

        // init
        // map : {course, [course_prerequisites]}
        Map<Integer, List<Integer>> map = new HashMap<>();
        for (int[] item : prerequisites){
            if (!map.containsKey(item[0])){
                map.put(item[0], new ArrayList<>());
            }else{
                List<Integer> cur = map.get(item[0]);
                cur.add(item[1]);
                map.put(item[0], cur);
            }
        }
        // set : current visiting courses
        HashSet<Integer> set = new HashSet<>();
        HashSet<Integer> visited = new HashSet<>();

        // dfs
        int cur = 0; // ?
        if (!checkPrereq(cur, map, set, visited)){
            return false;
        }
        return set.size() == numCourses;
    }

    public boolean checkPrereq(int cur, Map<Integer, List<Integer>> map, HashSet<Integer> set, HashSet<Integer> visited){

        visited.add(cur);

        if (set.contains(cur)){
            return false;
        }

        set.add(cur);
        if (!map.containsKey(cur) || map.get(cur).size() == 0){
            return true;
        }

        for (Integer x : map.get(cur)){
            this.checkPrereq(x, map, set, visited);
        }

        // undo
        set.remove(cur);

        return true;
    }

    // LC 207
    //import java.util.*;
    public boolean canFinish_6(int numCourses, int[][] prerequisites) {
        // Initialize adjacency list for storing prerequisites
        Map<Integer, List<Integer>> preMap = new HashMap<>();
        for (int i = 0; i < numCourses; i++) {
            preMap.put(i, new ArrayList<>());
        }

        // Populate the adjacency list with prerequisites
        for (int[] pair : prerequisites) {
            int crs = pair[0];
            int pre = pair[1];
            preMap.get(crs).add(pre);
        }

        // Set for tracking courses during the current DFS path
        Set<Integer> visiting = new HashSet<>();

        // Recursive DFS function
        for (int c = 0; c < numCourses; c++) {
            if (!dfs(c, preMap, visiting)) {
                return false;
            }
        }
        return true;
    }

    private boolean dfs(int crs, Map<Integer, List<Integer>> preMap, Set<Integer> visiting) {
        if (visiting.contains(crs)) {
            return false;
        }
        if (preMap.get(crs).isEmpty()) {
            return true;
        }

        visiting.add(crs);
        for (int pre : preMap.get(crs)) {
            if (!dfs(pre, preMap, visiting)) {
                return false;
            }
        }
        visiting.remove(crs);
        preMap.get(crs).clear(); // Clear prerequisites as the course is confirmed to be processed
        return true;
    }

    // LC 323
    // Quick Union (route compression)
    /**
     *  Example 1)
     *
     *   Input: n = 5, edges = [[0,1],[1,2],[3,4]]
     *   Output: 2
     *
     *
     *  -> parents = [0,1,2,3,4]
     *  -> quick union ...
     *
     *  ->   e = 0
     *  ->   parents = [0,1,2,3,4]
     *
     *  -> e = 1
     *  -> parents = [0,1,2,3,4]
     *
     *  -> e = 3
     *  ->  parents = [0,1,2,3,4]
     *
     *
     *  -> e = 1
     *  ->  parents = [0,1,2,3,4]
     *
     *
     *
     *
     *
     */
    int connectCnt = 0;

    public int countComponents_3(int n, int[][] edges) {

        if (n == 1){
            return 1;
        }

        // init
        // every element is its parent initially
        int[] parents = new int[n];
        for (int i = 0; i < n; i++){
            parents[i] = i;
        }

        // union find
        for (int[] e : edges){
            int n1 = this.unionFind_(e[0], parents);
            int n2 = this.unionFind_(e[1], parents);
//            if (n1 != n2){
//                this.connectCnt += 1;
//            }else{
//                // union
//                parents[n1] = n2;
//            }

            parents[n1] = n2;
        }

        System.out.println("---> parents = " + parents);

        return connectCnt;
    }

    public int unionFind_(int x, int[] parents){

        if (x == parents[x]){
            return x;
        }

        // same as below ?
        // return this.unionFind_(parents[x], parents);
        parents[x] = this.unionFind_(parents[x], parents);
        return parents[x];
    }

    /**
     *  Input: matrix = [[1,2,3],[4,5,6],[7,8,9]]
     *  Output: [[7,4,1],[8,5,2],[9,6,3]]
     *
     *
     *
     * step 1)
     *
     *  i, j -> j, i
     *
     * [
     * [1,4,7],
     * [2,5,8],
     * [3,6,9]
     * ]
     *
     *  step 2)
     *
     *  inverse
     *
     *  [
     *  [7,4,1],
     *  [8,5,2],
     *  [9,6,3]
     *  ]
     *
     *
     */

    // LC 48
    public void rotate(int[][] matrix) {

        if (matrix.length == 0 || matrix[0].length == 0){
            return;
        }

        // step 1) i, j -> j, i
        int l = matrix.length;
        int w = matrix[0].length;
        for (int i = 0; i < l; i++){
            // (1,0) <-> (0,1)
            // (2,0) <-> (0,2)

            /**
             *  (0,1), (0,2), (0,3)
             *  (1,2), (1,3)
             *  (2,3
             *
             *
             */
            for (int j = i+1; j < w; j++){
                int tmp = matrix[i][j];
                matrix[i][j] = matrix[j][i];
                matrix[j][i] = tmp;
            }
        }

        // step 2) inverse
        for (int i = 1; i < l; i++){
//            int[] cur = matrix[i];
//            //Arrays.stream(cur).toArray().
//            matrix[i] = reverse(matrix[i]);
            reverse(matrix[i]);
        }
    }

    public int[] reverse(int[] input){
        int l = 0;
        int r = input.length-1;
        while (r > l){
            int tmp = input[r];
            input[r] = input[l];
            input[l] = tmp;
        }
        return input;
    }

    // LC 50
    public double myPow(double x, int n) {

        if (x == 1.0 || x == 0.0){
            return x;
        }

        if (n == 0){
            return 1;
        }

        boolean negative = false;
        if (n < 0){
            negative = true;
            n = n * -1;
        }
        double res = 1.0;
        while (n > 0){
            res = res * x;
            n -= 1;
        }

        if (negative){
            return 1 / res;
        }

        return res;
    }


    // LC 43
    public String multiply(String num1, String num2) {

        if (num1.equals("0") || num2.equals("0")){
            return "0";
        }

        if (num1.equals("1") || num2.equals("1")){
            if (num1.equals("1")){
                return num2;
            }
            return num1;
        }

        Long res = 0L;
        Long num2Int = Long.parseLong(num2);
        Long num1Int = Long.parseLong(num1);
        while (num2Int > 0){
            res += num1Int;
            num2Int -= 1L;
        }

        return String.valueOf(res);
    }

    // LC 684
    // graph

    /**
     * example 1)
     *
     * edges = [[1,2],[1,3],[2,3]]
     * -> map = {
     *     1: [2,3],
     *     2: [3]
     * }
     *
     * example 2)
     *
     *  edges = [[1,2],[2,3],[3,4],[1,4],[1,5]]
     * -> map = {
     *     1: [2, 5],
     *     2: [3],
     *     3: [4],
     * }
     *
     *
     */
    public int[] findRedundantConnection(int[][] edges) {

        if (edges.length <= 1){
            return null;
        }

        if (edges.length == 3){
            return edges[2];
        }

        // init graph
        Map<Integer, List<Integer>> map = new HashMap<>();
        for (int[] x : edges){
            List<Integer> cur = map.getOrDefault(x[0], new ArrayList<>());
            cur.add(x[1]);
            map.put(x[0], cur);
        }

        System.out.println("map = " + map);

        for (int [] x : edges){
            // update map
            Map<Integer, List<Integer>> newMap = updateMap(x, map);
            if (!isCycle(x, newMap)){
                return x;
            }
        }

        return null;
    }

    public boolean isCycle(int[] x, Map<Integer, List<Integer>> map){

        return true;
    }

    public Map<Integer, List<Integer>> updateMap(int[] x, Map<Integer, List<Integer>> map){
        for (int k : map.keySet()){
            if (k == x[0]){
                map.remove(k);
                continue;
            }
            if (map.get(k).contains(x[1])){
                List<Integer> cur = map.get(k);
                cur.remove(x[1]);
                map.put(k, cur);
                continue;
            }
        }
        return map;
    }

    // LC 79
    //List<String> collected = new ArrayList<>();
    public boolean exist_1(char[][] board, String word) {

        if (board.length == 1 && board[0].length == 1){
            return String.valueOf(board[0][0]).equals(word);
        }

        // dfs
        int l = board.length;
        int w = board[0].length;
        //for (int i)
        for (int i = 0; i < w; i++){
            for (int j = 0; j < l; j++){
                int idx = 0;
                List<String> cur = new ArrayList<>();
                boolean[][] visited = new boolean[l][w];
                if (dfs_check(board, word, i, j, idx, visited)){
                    return true;
                }
            }
        }

        return false;
    }

    public boolean dfs_check(char[][] board, String word, int x, int y, int idx, boolean[][] visited){

        int l = board.length;
        int w = board[0].length;

        // mark as visited
        visited[x][y] = true;

        if (idx == word.length()){
            return true;
        }

        if (idx > word.length()){
            return false;
        }

//        if (cur.toString().equals(word)){
//            //collected.add(String.valueOf(cur)); // ?
//            return true;
//        }
//
//        if (cur.size() > word.length()){
//            return false;
//        }

        int[][] dirs = {{1,0}, {-1,0}, {0,1}, {0,-1}};
        for (int[] dir : dirs){
            int x_ = x + dir[0];
            int y_ = y + dir[1];
            if (x_ >= 0 && x_ < w && y_ >= 0 && y_ < l && word.indexOf(idx) == board[y_][x_] && !visited[x_][y_]){
                //cur.add(String.valueOf(board[y_][x_]));
                if (this.dfs_check(board, word, x_, y_, idx+1, visited)){
                    return true;
                }
                // undo ?? -> mark as Not visitet
                visited[x_][y_] = false;
            }
        }

        return false;
    }

    // LC 212
//    public List<String> findWords(char[][] board, String words) {
//
//        List<String> res = new ArrayList<>();
//
//        if (board == null || board.length == 0) {
//            return res;
//        }
//
//        int l = board.length;
//        int w = board[0].length;
//
//        boolean[][] visited = new boolean[l][w];
//
//        for (int i = 0; i < l; i++) {
//            for (int j = 0; j < w; j++) {
//                String resp = check(board, i, j, 0, word, visited);
//                if (words.contains(resp)){
//                    res.add(resp);
//                }
//            }
//        }
//
//        return res;
//    }
//
//    private String  check(char[][] board, int y, int x, int idx, String word, boolean[][] visited) {
//        if (idx == word.length()) {
//            return true;
//        }
//
//        int l = board.length;
//        int w = board[0].length;
//
//        if (y < 0 || y >= l || x < 0 || x >= w || visited[y][x] || board[y][x] != word.charAt(idx)) {
//            return false;
//        }
//
//        // Mark this cell as visited
//        visited[y][x] = true;
//
//        // Directions array for exploring up, down, left, and right
//        int[][] dirs = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
//
//        /** NOTE !!! init found flag, for tracking found status */
//        boolean found = false; // Variable to store if the word is found in any direction
//
//        // Explore all four directions
//        for (int[] dir : dirs) {
//            if (check(board, y + dir[0], x + dir[1], idx + 1, word, visited)) {
//                /** NOTE !!! if found, update found status */
//                found = true;
//                /** NOTE !!!
//                 *
//                 *  if found, break the loop,
//                 *  then will go to final line in dfs (e.g. return found;),
//                 *  and return found status directly
//                 */
//                break; // We can break out of the loop if we found the word in any direction
//            }
//        }
//
//        // Backtrack: unmark this cell as visited
//        visited[y][x] = false;
//
//        return found;
//    }

    // LC 3190
    /**
     *  Return the minimum number of operations
     *  to make all elements of nums divisible by 3.
     *
     */
    /**
     *  nums = [1,2,3,4]
     *
     *  init sum = 10
     *
     *
     *  -> [0, 1, 2, 3]
     *  -> so output = 3
     *
     *
     *
     *
     */
    public int minimumOperations(int[] nums) {

        if (nums.length == 1){
            return nums[0] % 3; // to check
        }

        //int sum = 0;
        //boolean canDivided = true;
        int res = 0;
        for (int x : nums){
//            if (x % 3 != 0){
//                canDivided = false;
//                break;
//            }
            int diff = (x % 3);
            int diff_ = diff;
            if (diff == 2){
                diff_ = 1;
            }
            System.out.println("x = " + x + ", diff = " + diff + " diff_ = " + diff_);
            res += Math.abs(diff_);
        }

//        if (canDivided){
//            return 0;
//        }

        return res;
    }

    // LC 3191
    // 6.15
    /**
     *  A binary array is an array which contains only 0 and 1.
     *
     *  You can do the following operation on the array any number of times (possibly zero):
     *
     *  -> Choose any 3 consecutive elements from the array and flip all of them.
     *
     *
     *   - Flipping an element means changing its value from 0 to 1, and from 1 to 0.
     *
     */
    /**
     *  example 1 :
     *
     *    [0,1,1,1]
     *
     *    -> [1,0,0,1]
     *    -> [1, 1,1,0]
     *    -> return -1
     *
     * example 2 :
     *
     *   [0,1,1,1,0,0]
     *
     *   -> [1,0,0 1,0,0]
     *           i
     *   -> [ 1, 1,1,0, 0, 0]
     *               i
     *   -> [1,1,1,1,1,1,1]
     *
     *   -> 3 ( 3 ops)
     *
     *
     *
     *   example 3
     *
     *    [0,1,1,1,0,1]
     *
     *    -> [1,0,0 1,0,1]
     *    ->  [1,1,1,0,0,1]
     *
     */
    /**
     * nums = [0,1,1,1,0,0]
     *
     *  idx = 0, [0,1,1] -> [1,0,0]. idx = 1, nums=[1,0,0,1,0,0]
     *
     *  idx = 1, [0,0,1] -> [1,1,0], idx =3, nums = [1,1,1,0,0,0]
     *
     *  idx = 3, [0,0,0] -> [1,1,1]. idx=-1, nums = [1,1,1,1,1,1]
     *
     */
    // brute force, pointers
    public int minOperations(int[] nums) {

        int res = 0;

        if (nums.length == 3){
            if (Arrays.stream(nums).sum() == 0){
                return 1;
            }
            if (Arrays.stream(nums).sum() == 3){
                return 0;
            }
            return -1;
        }

        int idx = 0;

        // get 1st 0 element
        for (int j = 0; j < nums.length; j++){
            if (nums[j] == 0){
                idx = j;
                break;
            }
        }

        System.out.println("idx = " + idx);

        while (idx <= nums.length-3){
            idx = flip(nums, idx);
            res += 1;
            if (idx==-1){
                break;
            }
        }

        System.out.println("---> nums =  " + Arrays.toString(nums) + ", idx = " + idx);
        //return idx == nums.length ? res : -1;
        return nums.length == Arrays.stream(nums).sum() ? res : -1;
    }

    public int flip(int[] input, int idx){
        System.out.println("input =  " + Arrays.toString(input) + ", idx = " + idx);
        int zeroIdx = -1;
        for (int i = idx; i < idx+3; i++){
            if (input[i] == 0){
                input[i] = 1;
            }else{
                input[i] = 0;
                if (zeroIdx==-1){
                    zeroIdx = i;
                }
            }
        }
        if (zeroIdx == input.length){
            return input.length;
        }
        System.out.println("input =  " + Arrays.toString(input) + ", idx = " + idx + ", zeroIdx = " + zeroIdx);
        return zeroIdx;
    }

    // LC 3191
    // 2 pointers
    public int minOperations_2(int[] nums) {

        int res = 0;

        int l = 0;
        int r = 2;
        while (r < nums.length){
            if (nums[l] == 0){
                for (int j = l; j <= l+2; j++){
                    if (nums[j] == 0){
                        nums[j] = 1;
                    }else{
                        nums[j] = 0;
                    }
                }
                System.out.println("nums =  " + Arrays.toString(nums));
                res += 1;
            }
            r += 1;
            l += 1;
        }

        // check if still has 0
        for (int i = 0; i < nums.length; i++){
            if (nums[i] == 0){
                return -1;
            }
        }

        return res;
    }

    // LC 3192
    // 5.35
    /**
     *  You can do the following operation on the
     *  array any number of times (possibly zero):
     *
     *
     *  -> Choose any index i from the array and flip all the elements
     *   from index i to the end of the array.
     *
     */
    /**
     *  example 1)  [0,1,1,0,1]
     *
     *   i=0, [1,0,0,1,0]
     *   i=1, [1,1,1,0,1]
     *   i=3, [1,1,1,1,0]
     *   i=4, [1,1,1,1,1]
     *
     *
     *  example 2) [1,0,0,0]
     *
     *  i = 1, [1,1,1,1]
     */
    public int minOperations_2_1(int[] nums) {

        if (Arrays.stream(nums).sum() == nums.length){
            return 0;
        }

        int res = 0;
        int i = 0;
        while (i < nums.length){
            if (nums[i] == 0){
                for (int j = i; j < nums.length; j++){
                    // if 0, do op
                    if (nums[j] == 0){
                        nums[j] = 1;
                    }else{
                        nums[j] = 0;
                    }
                }
                res += 1;
            }
            System.out.println("nums =  " + Arrays.toString(nums));
            i += 1;
        }

        for (int k = 0; k < nums.length; k++){
            if (nums[k] == 0){
                return -1;
            }
        }

        return res;
    }

    // LC 217
    public boolean containsDuplicate(int[] nums) {
        if (nums.length== 1){
            return false;
        }
        HashSet set = new HashSet();
        for (int i : nums){
            if (set.contains(i)){
                return true;
            }
            set.add(i);
        }
        return false;

    }

    // LC 2357
    // brute force

    /**
     *  {idx : val}
     *  {
     *      0: 1,
     *      1: 5,
     *      3: 3,
     *      4: 5
     *  }
     *
     */
    public int minimumOperations_1(int[] nums) {

        if (nums.length == 0){
            if (nums[0] == 0){
                return 0;
            }
            return 1;
        }

        int res = 0;
        //int i = 0;
        for (int i = 0; i < nums.length; i++){
            if (nums[i] != 0){
                int[] sub = Arrays.copyOfRange(nums, i, nums.length-1);
                int minVal = Arrays.stream(sub).min().getAsInt();
                for (int j = i; j < nums.length; j++){
                    if (nums[j] != 0){
                        nums[j] -= minVal;
                    }
                }
                res += 1;
            }
        }

        return res;
    }

    // LC 49
    public List<List<String>> groupAnagrams_2(String[] strs) {

        List<List<String>> res = new ArrayList<>();
        if (strs.length == 0){
            List<String> val = new ArrayList<>();
            val.add("");
            res.add(val);
            return res;
        }

        Map<String, List<String>> map = new HashMap<>();
        for (String x : strs){
            char[] x_array = x.toCharArray();
            Arrays.sort(x_array);
            String x_str  = new String(x_array);
            //System.out.println("x = " + x + " x_str = " + x_str);
            if(!map.containsKey(x_str)){
                List<String> val = new ArrayList<>();
                val.add(x);
                map.put(x_str, val);
            }else{
                List<String> cur = map.get(x_str);
                cur.add(x);
                map.put(x_str, cur);
            }
        }

        //System.out.println("map = " + map);

        for (String k : map.keySet()){
            res.add(map.get(k));
        }

        //System.out.println("res = " + res);

        return res;
    }

    // LC 347
    // PQ
    public int[] topKFrequent_2(int[] nums, int k) {

        if (nums.length == 1){
            return nums;
        }

        Map<Integer, Integer> map = new HashMap<>();
        for (int x : nums){
            Integer cnt = map.getOrDefault(x, 0);
            map.put(x, cnt+1);
        }

        // init a "big" PQ
        //PriorityQueue<Integer> pq = new PriorityQueue<>(Comparator.reverseOrder());
        // Queue<Integer> heap = new PriorityQueue<>((n1, n2) -> count.get(n1) - count.get(n2));
        PriorityQueue<Integer> pq = new PriorityQueue<>((x, y) -> {
            if(map.get(x) > map.get(y)){
                return 1;
            }else if (map.get(x) < map.get(y)){
                return -1;
            }
            return 0;
        });

//        for(int val : map.values()){
//           pq.add(val);
//        }

        for (int key : map.keySet()){
            pq.add(key);
            //if (pq.size()>)
        }

        System.out.println("map = " + map);
        System.out.println("pq = " + pq);
        int[] res = new int[k];
        while(k > 0){
            res[k] = pq.poll();
            k -= 1;
        }

        return res;
    }

    // LC 128
    public int longestConsecutive_2(int[] nums) {

        if (nums.length <= 1){
            return nums.length;
        }

        Set<Integer> set = new HashSet<>();
        for (int x: nums){
            set.add(x);
        }
        Integer[] array = new Integer[set.size()];
        int j = 0;
        for(int y : set){
            array[j] = y;
            j += 1;
        }

        System.out.println("nums before sort = " + Arrays.toString(array));
        Arrays.sort(array);
        System.out.println("nums after sort = " + Arrays.toString(array));

        int res = 1;
        int l = 0;
        int r = 1;
        while (r < array.length){
            if (array[r-1] + 1 != array[r]){
                l = r;
                //break;
            }
            res = Math.max(res, r-l+1);
            r += 1;
        }

        return res;
    }

    // LC 2177
    /**
     *  Given an integer num, return three consecutive integers (as a sorted array)
     *  that sum to num. If num cannot be expressed as the sum of three consecutive
     *  integers, return an empty array.
     *
     */
    // 5.10 pm

    /**
     *  array = [1,2,3,4...... N]
     *
     *
     */
    public long[] sumOfThree(long num) {

        long avg = num / 3;
        System.out.println("avg = " + avg);

        long long_1 = avg - 1L;
        long long_2 = avg;
        long long_3 = avg + 1L;

        long[] res = new long[3];

        boolean found = false;

        if (isValid(long_1, long_1+1L, long_1+2L, num)){
            res[0] = long_1;
            res[1] = long_1+1L;
            res[2] = long_1+2L;
            found = true;
        }else if (isValid(long_2, long_2+1L, long_2+2L, num)){
            res[0] = long_2;
            res[1] = long_2+1L;
            res[2] = long_2+2L;
            found = true;
        }else if (isValid(long_3, long_3+1L, long_3+2L, num)){
            res[0] = long_3;
            res[1] = long_3+1L;
            res[2] = long_3+2L;
            found = true;
        }

        if (!found){
            return new long[0];
        }

        return res;
    }

    public boolean isValid(long x, long y, long z, long num){
        if (x + y + z  == num){
            return true;
        }
        return false;
    }

    // LC 234
    public boolean isPalindrome(ListNode head) {

        if (head == null || head.next == null){
            return true;
        }

        List<Integer> tmp = new ArrayList<>();
        while (head != null){
            tmp.add(head.val);
            head = head.next;
        }

        System.out.println("tmp = " + tmp);
        int l = 0;
        int r = tmp.size()-1;
        while (r > l){
            if (!tmp.get(l).equals(tmp.get(r))){
                return false;
            }
            l += 1;
            r -= 1;
        }

        return true;
    }

    // LC 121
    public int maxProfit_2(int[] prices) {

        if (prices.length == 1){
            return 0;
        }

        int profit = 0;
        int local_min = Integer.MAX_VALUE;
        int local_max = -1;

        for(int x : prices){
            System.out.println("x = " + x + ", local_min = " + local_min + ", local_max = " + local_max + ", profit = " + profit);
            if (local_min == Integer.MAX_VALUE){
                local_min = x;
            }else if (local_min > x){
                local_min = x;
            }else if(x > local_min){
                local_max = x;
                profit = Math.max(profit, local_max - local_min);
                // already "sold", can't reuse local_max, so make it as initial value again
                local_max = -1;
            }
        }

        return profit;
    }

    // LC 3
    public int lengthOfLongestSubstring_2(String s) {

        return 0;
    }

    // LC 3194
    public double minimumAverage(int[] nums) {

        if (nums.length == 1){
            return nums[0];
        }

        List<Float> collected = new ArrayList<>();

        // sort, in increasing order
        System.out.println("before sort : " + Arrays.toString(nums));
        Arrays.sort(nums);
        System.out.println("after sort : " + Arrays.toString(nums));

        while(nums.length > 0){
            int max_ = nums[nums.length-1];
            int min_ = nums[0];
            nums = Arrays.copyOfRange(nums, 1, nums.length-1);
            float val = (float) (max_ + min_) / 2; // ?
            collected.add(val);
        }

        System.out.println("(before) collected  = " + collected);
        collected.sort((a,b) -> {
            if (a > b){
                return 1;
            }else if (a < b){
                return -1;
            }
            return 0;
        });

        System.out.println("(after) collected  = " + collected);
        // return collected.stream().min();
        return collected.get(0);
    }

    // LC 3195
    /**
     *
     *  Find a rectangle with horizontal and vertical sides with
     *  the smallest area, such that all
     *  the 1's in grid lie inside this rectangle.
     *
     */
    /**
     *  example 1)
     *
     *  grid = [
     *      [0,1,0],
     *      [1,0,1]
     *   ]
     *
     *  -> 6
     *
     *
     *  example 2)
     *
     *  grid = [
     *      [0,0,0],
     *      [0,0,0],
     *      [0,0,0],
     *      [1,0,1]
     *    ]
     *
     */
    // 2 pointers ?
    // 4.15
    public int minimumArea(int[][] grid) {

        if (grid.length == 1 && grid[0].length == 1){
            return 1;
        }

        // define x, y pointers, move x, y direction respectively
        int x_start = -1;
        int x_end = -1;
        int y_start = -1;
        int y_end = -1;

        // y-direction
        for (int i = 0; i < grid.length; i++){
            if (Arrays.stream(grid[i]).filter(x -> x==1).count() > 0){
                if(x_start == -1){
                    x_start = i;
                }else{
                    //x_end = Math.max(i, x_end);
                    x_end = i;
                }
            }
        }

        // x-direction
        for (int j = 0; j < grid[0].length; j++){
            int[] yArray = collectYArray(grid, j);
            if (Arrays.stream(yArray).filter(x -> x==1).count() > 0){
                if(y_start == -1){
                    y_start = j;
                }else{
                    //y_end = Math.max(j, y_end);
                    y_end = j;
                }
            }
;        }

        System.out.println("x_start = " + x_start +
                ", x_end = " + x_end +
                " y_start = " + y_start +
                ", y_end = " + y_end
        );

//        if (x_end == -1){
//            x_end = 0;
//        }
//
//        if (y_end == -1){
//            y_end = 0;
//        }

        if (x_start == -1 || y_start == -1) {
            return 0;
        }


        if (x_end == -1) {
            x_end = x_start;
        }

        if (y_end == -1) {
            y_end = y_start;
        }

        return  (x_start - x_end + 1) * (y_end - y_start + 1);
    }

    public int[] collectYArray(int[][] grid, int x){
        int[] res = new int[grid.length];
        for (int y = 0; y < grid.length; y++){
            res[y] = grid[y][x];
        }
        return res;
    }

    // 6.40
    // LC 3196
    // https://leetcode.com/problems/maximize-total-cost-of-alternating-subarrays/description/

    /**
     *
     *  A subarray is a contiguous non-empty sequence of elements within an array.
     *
     *
     *  ex 1)
     *   nums = [1,-2,3,4]
     *
     *   -> [1,2,3], [4]
     *   -> 10
     *
     *
     *  ex 2)
     *
     *  nums = [1,-1,1,-1]
     *
     *  -> [1,-1], [1,-1]
     *  -> 4
     *
     *
     */
    public long maximumTotalCost(int[] nums) {

        if (nums.length == 1){
            //return Math.abs(nums[0]);
            return nums[0];
        }

        if (nums.length == 2){
            int v1 = nums[0] + nums[1];
            int v2 = nums[0] - nums[1];
            return Math.max(v1, v2);
        }

        Long res = 0L;

        List<int []> cache = new ArrayList<>();
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < nums.length; i++){
            if (nums[i] < 0){
                queue.add(i);
            }
        }

        System.out.println("queue = " + queue); // ?

        int j = 0;
        while (!queue.isEmpty()){
            Integer idx = queue.poll();
            cache.add(Arrays.copyOfRange(nums, j, idx+1));
            j = idx+1;
        }

        if (j < nums.length){
            cache.add(Arrays.copyOfRange(nums, j, nums.length));
        }

        System.out.println("------>");
        //System.out.println("cache = " + String.join("\n", cache)); // ?
        cache.stream().forEach(x -> {System.out.println(Arrays.toString(x));});

        for (int[] item : cache){
            int cur = Arrays.stream(item).map(x -> Math.abs(x)).sum();
            res += cur;
        }

        return res;
    }

    // LC 1007
    // 5.30
    /**
     * Return the minimum number of rotations
     * so that all the values in tops
     * are the same, or all the values in bottoms are the same.
     */
    /**
     * ex1)
     *
     * tops =    [2,1,2,4,2,2],
     * bottoms = [5,2,6,2,3,2]
     *
     *
     * ex 2)
     *
     * tops =    [3,5,1,2,3],
     * bottoms = [3,6,3,3,4]
     *
     *
     */
    public int minDominoRotations(int[] tops, int[] bottoms) {

        //if (tops.length == 2 && )

        Map<Integer, Integer> map1 = new HashMap<>();
        Map<Integer, Integer> map2 = new HashMap<>();
        List<Integer[]> zip = new ArrayList<>();

        int cnt = tops.length;

        for (int j = 0; j < tops.length; j++){
            Integer[] tmp = new Integer[2];
            tmp[0] = tops[j];
            tmp[1] = bottoms[j];
            zip.add(tmp);
        }


        for(int i : tops){
            //int cnt = map1.getOrDefault(i,0);
            map1.put(i, map1.getOrDefault(i,0)+1);
        }

        for(int i : bottoms){
            //int cnt = map1.getOrDefault(i,0);
            map2.put(i, map2.getOrDefault(i,0)+1);
        }

        System.out.println("map1 = " + map1 + ", map2 = " + map2);
        HashSet<Integer> candidates = new HashSet<>();

        for (Integer[] item : zip){
            System.out.println("item = " + item.toString());
            if (map1.getOrDefault(item[0],0) + map2.getOrDefault(item[0],0) < cnt
                    && map1.getOrDefault(item[1],0) +  map2.getOrDefault(item[1],0) < cnt){
                return -1;
            }
            if (map1.getOrDefault(item[0],0) + map2.getOrDefault(item[0],0) >= cnt){
                candidates.add(item[0]);
            }
            if (map1.getOrDefault(item[1],0) + map2.getOrDefault(item[1],0) >= cnt){
                candidates.add(item[1]);
            }
        }

        System.out.println("candidates = " + candidates);

        return 0;

    }

    // LC 410
    // https://leetcode.com/problems/split-array-largest-sum/description/
    /**
     *
     *  ... such that the largest sum of any subarray is minimized.
     *
     *  A subarray is a contiguous part of the array.
     */
    /**
     *  ex 1)
     *
     *  nums = [7,2,5,10,8], k = 2
     *
     *  -> can split nums as below
     *  -> total 4 ways
     *
     *   - [7], [2,5,10,8] -> 7, 25
     *   - [7,2], [5,10,8] -> 9, 23
     *   - [7,2,5], [10,8] -> 14, 18
     *   - [7,2,5,10], [8] -> 24, 8
     *
     */
    public int splitArray(int[] nums, int k) {

        if (nums.length == 1){
            return nums[0];
        }

        int res = 0;
        for (int i = 1; i < nums.length; i += 1){
            System.out.println("i = " + i);
            getSubArraySum(nums, i);
        }

        return res;
    }

    public List<Integer> getSubArraySum(int[] nums, int idx){
        List<Integer> res = new ArrayList<>();
        int len = nums.length;
        if (nums.length == 0){
            return res;
        }
        if (nums.length == 1){
            res.add(nums[0]);
            return res;
        }
        int[] leftArray = Arrays.copyOfRange(nums, 0, idx);
        int[] rightArray = Arrays.copyOfRange(nums, idx, len);
        Arrays.stream(leftArray).sum();
        res.add(Arrays.stream(leftArray).sum());
        res.add(Arrays.stream(rightArray).sum());
        System.out.println("idx = " + idx + " res = " + res.toString());
        return res;
    }

    // LC 222
    // https://leetcode.com/problems/count-complete-tree-nodes/
    //int count = 0;
    // bfs
    public int countNodes_2(TreeNode root) {

        if (root == null){
            return 0;
        }
        List<TreeNode> collected = new ArrayList<>();
        Queue<TreeNode> q = new LinkedList<>();
        q.add(root);
        while (!q.isEmpty()){
            TreeNode cur = q.poll();
            collected.add(cur);
            if (cur.left != null) {
                q.add(cur.left);
            }
            if (cur.right != null) {
                q.add(cur.right);
            }
        }

        //return this.count;
        System.out.println("collected = " + collected.toString());
        return collected.size();
    }

    // dfs
    //int count = 1;
    public int countNodes(TreeNode root) {

        if (root == null){
            return 0;
        }

        //this.count += 1;

//        if (root.left != null){
//            return this.countNodes(root.left) + 1;
//        }
//
//        if (root.right != null){
//            return this.countNodes(root.right) + 1;
//        }
        int count = 1;
        count += this.countNodes(root.left);
        count += this.countNodes(root.right);

        return count;
    }

    // LC 1170
    // 5.20

    /**
     * count the number of words in words
     * such that f(queries[i]) < f(W) for each W in words.
     */
    public int[] numSmallerByFrequency(String[] queries, String[] words) {

        Map<Integer, Integer> q_map = new HashMap<>();
        Map<String, List<Integer>> w_map = new HashMap<>();


        if (queries.length == 1 && words.length == 1){
            String q = getSmallestChar(queries[0]);
            String w = getSmallestChar(words[0]);

            int q_cnt = getCharCount(q, queries[0]);
            int w_cnt = getCharCount(w, words[0]);

            return w_cnt > q_cnt ? new int[]{1} : new int[]{0};
        }

        Integer[] w_cnt = new Integer[words.length];
        //Integer[] q_cnt = new Integer[queries.length];

        for (String w : words){
            String w_cur = getSmallestChar(w);
            Integer w_cur_cnt = getCharCount(w_cur, w);
            List<Integer> cur_cnt = w_map.getOrDefault(w_cur, new ArrayList<>());
            cur_cnt.add(w_cur_cnt);
            w_map.put(w_cur, cur_cnt);
        }

        System.out.println(">>> w_map = " + w_map);

        //List<Integer> res = new ArrayList<>();
        int[] res = new int[queries.length];

        for (int i = 0; i < queries.length; i++){
            String q = getSmallestChar(queries[i]);
            res[i] = 1;
        }

        return res;
    }

    public String getSmallestChar(String input){
        String alphabets = "abcdefghijklmnopqrstuvwyz";
        //char[] charArr = input.toCharArray();
        for (String alpha : alphabets.split("")){
            //System.out.println(alpha);
            if (input.contains(alpha)){
                return alpha;
            }
        }

        System.out.println("alphabet not matched");
        return null;
    }

    public Integer getCharCount(String str, String input){
        int res = 0;
        for (String x : input.split("")){
            if (x.equals(str)){
                res += 1;
            }
        }
        return res;
    }

    // LC 1170
    public int[] numSmallerByFrequency_2(String[] queries, String[] words) {

        int[] wordsFrequency = new int[words.length];
        Map<String, Integer> frequencyMap = new HashMap<>();

        // Calculate the frequency of the smallest character for each word and store in map
        for (int i = 0; i < words.length; i++) {
            wordsFrequency[i] = getFrequencyOfSmallestCharacter(words[i], frequencyMap);
        }

        System.out.println(">>> (before sort) wordsFrequency = " + Arrays.toString(wordsFrequency));

        // sort, for binary search
        Arrays.sort(wordsFrequency);
        System.out.println(">>> (after sort) wordsFrequency = " + Arrays.toString(wordsFrequency));

        int[] result = new int[queries.length];

        for (int i = 0; i < queries.length; i++) {
            int queryFrequency = getFrequencyOfSmallestCharacter(queries[i], frequencyMap);
            result[i] = countGreater(wordsFrequency, queryFrequency);
        }


        System.out.println(">>> result = " + Arrays.toString(result));

        return result;
    }

    public int getFrequencyOfSmallestCharacter(String word, Map<String, Integer> map){
        int cnt = 1;
        String prev = null;
        for (String w : word.split("")){
            System.out.println("w = " + w + ", prev = " + prev + ", cnt = " + cnt);
            if (prev == null){
                prev = w;
                cnt = 1;
            } else if (prev.compareTo(w) > 0){
                prev = w;
                cnt = 1;
            } else if (prev.equals(w)){
                cnt += 1;
            }
        }
        return cnt;
    }

//    private int countGreater(int[] arr, int value) {
//        int left = 0, right = arr.length;
//
//        while (left < right) {
//            int mid = left + (right - left) / 2;
//            if (arr[mid] <= value) {
//                left = mid + 1;
//            } else {
//                right = mid;
//            }
//        }
//
//        return arr.length - left;
//    }

    public int countGreater(int[] wordFreq, int freq){
        // binary search (find bigger num idx)
        int l = 0;
        int r = wordFreq.length - 1; // TODO : check wordFreq.length - 1 or wordFreq.length ??
        while (r > l){
            int mid = (l + r) / 2;
            if (wordFreq[mid] <= freq){
                l = mid + 1;
            }
//            else if (wordFreq[mid] > freq){
//                r = mid;
//            }
            else{
                r = mid;
            }

            if (wordFreq[l] <= freq) {
                return 0;
            }
        }

        return wordFreq.length - l;
    }

    // LC 482
    // https://leetcode.com/problems/license-key-formatting/description/

    /**
     *
     * We want to reformat the string s such that each group contains exactly k characters,
     * except for the first group, which could be shorter than k but
     * still must contain at least one character.
     */
    public String licenseKeyFormatting(String s, int k) {

        if (s.length() == 1){
            return s;
        }

        StringBuilder sb = new StringBuilder();
        for (String x : s.split("")){
            if (!x.equals("-")){
                sb.append(x);
            }
        }
        String s_ = sb.toString();
        System.out.println("s_ = " + s_);

        int remain = s_.length() % k;
        int len = s_.length() / k;

        StringBuilder sb2 = new StringBuilder();
        sb2.append(s_, 0, remain);
        sb2.append("-");

        String s_2 = s_.substring(remain+1);
        for (int i=0; i < s_2.length(); i+=len){
            sb2.append(s_2, i, i+len);
            sb2.append("-");
        }

        return sb2.toString();
    }

    //  LC 482
    public String licenseKeyFormatting_2(String s, int k) {

        return null;
    }


    // LC 809

    /**
     * And add some number of characters c to the group so
     * that the size of the group is three or more.
     */
    public int expressiveWords_1(String s, String[] words) {

        int ans = 0;

        if (words.length == 0) {
            return 0;
        }

        Map<String, Integer> map = getCountMap(s);

        List<Map<String, Integer>> list = new ArrayList<>();
        for (String w: words){
            Map<String, Integer> tmpMap = getCountMap(w);
            System.out.println("tmpMap = " + tmpMap);
            boolean canExpress = true;
            int keyCnt = 0;

            // s = "aaa", words = ["aaaa"]

            for (String k : tmpMap.keySet()){
                keyCnt += 1;
                if (!map.containsKey(k)){
                    canExpress = false;
                    break;
                }
                if (tmpMap.get(k) < map.get(k) && map.get(k) < 3){
                    canExpress = false;
                    break;
                }
                if (tmpMap.get(k) > map.get(k)){
                    canExpress = false;
                    break;
                }

            }
            if (canExpress && keyCnt == map.keySet().size()){
                ans += 1;
            }
        }

        System.out.println("map = " + map);

        return ans;
    }

    private Map<String, Integer> getCountMap(String s){
        Map<String, Integer> map = new HashMap<>();
        for (String x : s.split("")){
            map.put(x, map.getOrDefault(x, 0)+1);
        }
        return map;
    }

    // LC 315
    // 6.28
    public List<Integer> countSmaller(int[] nums) {
        List<Integer> res = new ArrayList<>();
        if (nums == null || nums.length == 0){
            return null;
        }
        if (nums.length == 1){
            res.add(0);
            return res;
        }
        // brute force
        for (int i = 0; i < nums.length; i++){
//            int cnt = 0;
//            for (int j = i+1; j < nums.length; j++){
//                if (nums[i] > nums[j]){
//                    cnt += 1;
//                }
//            }
//            res.add(cnt);
            int cnt = findSmallerCnt(i, nums);
            System.out.println(">>> cnt = " + cnt + ", i = " + i + " , nums = " + nums);
            res.add(cnt);
        }

        return res;
    }

    private int findSmallerCnt(int idx, int[] nums){
        int cnt = 0;
        // binary search
        int toCompare = nums[idx];
        int[] nums_ = Arrays.copyOfRange(nums, idx+1, nums.length);
        Arrays.sort(nums_);
        int l = 0;
        int r = nums_.length;
        int mid = (l + r) / 2;
        while (r > l){
            System.out.println("l = " + l + ", r = " + r + ", mid = " + mid);
            // found
            if (toCompare < nums[mid+1] && toCompare > nums[mid-1]){
                return nums_.length - mid;
            }
            if (toCompare > nums[mid]){
                l = mid + 1;
            }else{
                r = mid;
            }
        }

        return cnt;
    }

    ///////
    public List<Integer> countSmaller_1(int[] nums) {

        List<Integer> res = new ArrayList<>();

        if (nums.length == 1){
            res.add(0);
            return res;
        }

        if (nums.length == 2){
            if (nums[0] > nums[1]){
                res.add(1);
            }else{
                res.add(0);
            }
            res.add(0);
            return res;
        }

        // V1 : brute force (TLE)
//        for (int i = 0; i < nums.length-1; i++){
//            int cnt = 0;
//            for (int j = i+1; j < nums.length; j++){
//                if (nums[j] < nums[i]){
//                    cnt += 1;
//                }
//            }
//            System.out.println("i = " + i + ", cnt = " + cnt);
//            res.add(cnt);
//        }

        // V2 : sub array + binary search
        /**
         *  {5: 0, 2: 1, 6:2, 1: 3}
         *
         *  from right to left, (pass last element)
         *
         *  5,2,6,  1 -> [1]
         *  5,2    6,1, reorder, -> [1,1]
         *  5,    2,6,1, reorder, 1,2,6, find smaller element cnt via binary search -> [2]
         *
         */
        for (int i = nums.length-1; i >= 0; i--){
            //int cnt2 = 0;
            int[] subNums = Arrays.copyOfRange(nums, i, nums.length);
            // ascending order
            Arrays.sort(subNums);
            // binary search
            int cnt2 = getSmallerCnt(subNums, nums[i]);
            res.add(cnt2);
            System.out.println("subNums = " + subNums + ", i = " + i + ", cnt2 = " + cnt2);
            //res.add(cnt);
        }

        res.add(0);
        return res;
    }

    private int getSmallerCnt(int[] input, int value){
        int left = 0;
        int right = input.length-1;
        int mid = 0;
        while (right > left){
            mid = (left + right) / 2;
            if (value < input[mid+1] && value > input[mid]){
                return mid+1;
            }
            if (value < input[mid]){
                left = mid+1;
            } else if (value > input[mid]){
                right = mid;
            }
        }
        return mid+1;
    }

    // LC 359
    // https://leetcode.ca/2016-11-23-359-Logger-Rate-Limiter/
    // https://leetcode.com/problems/logger-rate-limiter/description/
    class Logger {

        private Map<String, Integer> limiter;

        /** Initialize your data structure here. */
        public Logger() {
            limiter = new HashMap<>();
        }

        public boolean shouldPrintMessage(int timestamp, String message) {
            if (!this.limiter.containsKey(message)){
                this.limiter.put(message, timestamp+10);
                return true;
            }
            int canPrintTime = this.limiter.get(message);
            if (timestamp > canPrintTime){
                return true;
            }
            return false;
        }
    }

    // LC 1057
    // https://leetcode.ca/all/1057.html
    // 6.50 pm
    /**
     *
     *  NOTE !!
     *  Return an array answer of length n, where answer[i] is the
     *  index (0-indexed) of the bike that the ith worker is assigned to.
     */
    /**
     *  example 1 :
     *
     *  Input: workers = [[0,0],[2,1]], bikes = [[1,2],[3,3]]
     *  Output: [1,0]
     *          1: bike 0 is assigned to worker 1
     *          0: bike 1 is assigned to worker 0
     *
     *  ->
     *
     *  worker-bike :
     *    0-0 : 3
     *    0-1 : 6
     *    1-0 : 2
     *    1-1 : 3
     *
     *  -> so, after ordering,
     *   0-0 : 3
     *   1-1 : 3
     *   1-0 : 2
     *   0-1 : 6
     *
     *  -> so,
     *
     *
     */
    public int[] assignBikes(int[][] workers, int[][] bikes) {
        // step 1) get bike - worker distance
        List<bikeWorkerDist> distances = new ArrayList<>();
        List<Integer> result = new ArrayList<>();
        int bikeId = 0;
        int workerId = 0;
        for (int[] bike : bikes){
            for (int[] worker : workers){
                int x1 = bike[0];
                int y1 = bike[1];
                int x2 = worker[0];
                int y2 = worker[1];
                int dist = getManhattanDist(x1,y1,x2,y2);
                distances.add(new bikeWorkerDist(worker, bike, bikeId, workerId, dist));

                bikeId += 1;
                workerId += 1;
            }
        }

        System.out.println("distances = " + distances);


        // step 2) sorting
        distances.sort(new Comparator<bikeWorkerDist>() {
            @Override
            public int compare(bikeWorkerDist o1, bikeWorkerDist o2) {
                if (o1.dist < o2.dist){
                    return 1;
                }
                if (o1.dist > o2.dist){
                    return -1;
                }
                // if same dist, choose smallest worker id
                if (o1.dist == o2.dist){
                    if (o1.getWorkerId() < o2.getWorkerId()){
                        return 1;
                    }  if (o1.getWorkerId() > o2.getWorkerId()){
                        return -1;
                    }

                    // if still same, choose smallest bike id
                    if (o1.getBikeId() > o2.getBikeId()){
                        return 1;
                    }
                    return -1;
                }
                // TODO : double check
                return -1;
            }
        });


        // step 3) prepare ans
        return null;
    }

    private int getManhattanDist(int x1, int y1, int x2, int y2){
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }

    class bikeWorkerDist{
        private int[] worker;
        private int[] bike;

        private int workerId;
        private int bikeId;

        public int getWorkerId() {
            return workerId;
        }

        public void setWorkerId(int workerId) {
            this.workerId = workerId;
        }

        public int getBikeId() {
            return bikeId;
        }

        public void setBikeId(int bikeId) {
            this.bikeId = bikeId;
        }

        private int dist;

        public bikeWorkerDist(){

        }

        public bikeWorkerDist(int[] worker, int[] bike, int workerId, int bikeId, int dist){
            this.worker = worker;
            this.bike = bike;
            this.workerId = workerId;
            this.bikeId = bikeId;
            this.dist = dist;
        }

        public int[] getWorker() {
            return worker;
        }

        public void setWorker(int[] worker) {
            this.worker = worker;
        }

        public int[] getBike() {
            return bike;
        }

        public void setBike(int[] bike) {
            this.bike = bike;
        }

        public int getDist() {
            return dist;
        }

        public void setDist(int dist) {
            this.dist = dist;
        }
    }

    // LC 1055
    // https://leetcode.ca/all/1055.html
    // bfs
    public int shortestWay_0(String source, String target) {

        // check if target has element NOT existrd in soruce
        Set<String> srcSet = new HashSet<>();
        Set<String> targetSet = new HashSet<>();
        for (String t : target.split("")){
            targetSet.add(t);
        }
        for (String s : source.split("")){
            if (!targetSet.contains(s)){
                return -1;
            }
            srcSet.add(s);
        }

        int cnt = 0;

        // count num of concatenation
        // sliding window (?)
        int l = 0;
        //int r = 0;
        String[] targetArr = target.split("");
        String[] srcArr = source.split("");

        while (l < target.length()){
            int r = l;
            String t_val = targetArr[l];
            String s_val = targetArr[l];
            int srcIdx = source.indexOf(t_val);
            while (t_val.equals(s_val)) {
                r += 1;
            }
            cnt += 1;
            l = r;
        }

        return cnt;
    }

    // LC 1055
    // https://leetcode.ca/all/1055.html
    // bfs
    public int shortestWay_1(String source, String target) {

        for (String x : target.split("")){
            if (!source.contains(x)) {
                return -1;
            }
        }

        String[] src_list = source.split("");
        String[] target_list = target.split("");

        int res = 0;
        boolean sameSubStr = true;

        for (int i = 0; i < target.length(); i++){
            //int j = Arrays.asList(src_list).indexOf(target_list[i]);
            int j = 1;
            while (!target_list[i].equals(src_list[j]) && j < target.length()){
                j += 1;
                sameSubStr = false;
            }
            if(!sameSubStr){
                res += 1;
                j = 1;
            }
        }

        return res > 0 ? res : - 1;
    }

    public int shortestWay(String source, String target) {
        if (source.length() == 0 || target.length() == 0){
            return 0;
        }
        // 2 pointers
        int cnt = 0;
        //int i = 0;
        int j = 0;
        while (j < target.length()){
            for (int i = 0; i < source.length(); i++){

                // j meat source len, repeat loop over source
                if (j == source.length()-1){
                    break;
                }
                if (source.charAt(i) == target.charAt(j)){
                    //i += 1;
                    j += 1;
                }
                else{
                    cnt += 1;
                    break;
                }
            }
        }

        return cnt;
    }

    // LC 809
    // https://leetcode.com/problems/expressive-words/
    // 2 pointers
//    public int expressiveWords(String s, String[] words) {
//
//        if (words.length == 0) {
//            return 0;
//        }
//
//        int cnt = 0;
//
//        for (String w : words) {
//            if (canExpress(s, w)) {
//                cnt += 1;
//            }
//        }
//
//        return cnt;
//    }
//
//    private Boolean canExpress(String s, String input) {
//
//        int i = 0;
//        int j = 0;
//        String[] s_array = s.split("");
//        String[] input_array = input.split("");
//
//        // i : pointer of input
//        //
//        while (i < input.length()){
//            //int j = i;
//            int cnt = 0;
//            String x = s_array[i];
//            Boolean found = false;
//            while (s_array[i].equals(input_array[i])){
//                found = true;
//                j += 1;
//                cnt += 1;
//            }
//            if (cnt < 3 && found){
//                return false;
//            }
//            found = false;
//            i += 1;
//        }
//
//        return true;
//    }

//    public int expressiveWords(String s, String[] words) {
//
//        if (words.length == 0){
//            return 0;
//        }
//        int cnt = 0;
//        Map<String, Integer> targetMap = getElementCnt(s);
//
//        for (String w : words){
//            // get cnt map
//            Map<String, Integer> map = getElementCnt(w);
//            System.out.println("map = " + map + ", w = " + w);
//            // check
//            if (canExpress(map, targetMap)){
//                cnt += 1;
//            }
//        }
//
//        return cnt;
//    }
//
//    private Boolean canExpress(Map<String, Integer> map, Map<String, Integer> mapTarget){
//        for (String k: mapTarget.keySet()){
//            if (!map.containsKey(k)){
//                return false;
//            }
//            if (map.get(k) > mapTarget.get(k)){
//                return false;
//            }
//            if (map.get(k).equals(mapTarget.get(k))){
//                continue;
//            }
//            if (map.get(k) < mapTarget.get(k) && mapTarget.get(k) < 3){
//                return false;
//            }
//        }
//        return true;
//    }
//
//    private Map<String, Integer> getElementCnt(String input){
//        Map<String, Integer> map = new HashMap<>();
//        for (String x : input.split("")){
//            if (!map.containsKey(x)){
//                map.put(x, 1);
//            }else{
//                map.put(x, map.get(x)+1);
//            }
//        }
//        return map;
//    }


//    public int expressiveWords(String s, String[] words) {
//        if (words == null || words.length == 0){
//            return 0;
//        }
//
//        int res = 0;
//        for (String x : words){
//            System.out.println("x = " + x + ",  canForm(x, s) = " + canForm(x, s));
//            if (canForm(x, s)) {
//                res += 1;
//            }
//        }
//
//        return res;
//    }
//
//    private boolean canForm(String x, String target){
//        List<String> x_arr = Arrays.asList(x.split(""));
//        List<String> t_arr = Arrays.asList(target.split(""));
//        //int i = 0;
//        int j = 0;
//        for (int i = 0; i < x_arr.size(); i++){
//            if (!t_arr.contains(x_arr.get(i))){
//                return false;
//            }
//            if (j == target.length() && i < x.length()-1){
//                return false;
//            }
//            //int j = i;
//            int cnt = 0;
//            while(t_arr.get(j).equals(x_arr.get(i))){
//                j += 1;
//                cnt += 1;
//            }
//            if (cnt == 2){
//                return false;
//            }
//            j += 1;
//        }
//
//        return true;
//    }


    public int expressiveWords(String s, String[] words) {
        if (words == null || words.length == 0){
            return 0;
        }

        int res = 0;
        for (String x : words){
            System.out.println("x = " + x + ",  canForm(x, s) = " + canForm(x, s));
            if (canForm(x, s)) {
                res += 1;
            }
        }

        return res;
    }

    private boolean canForm(String x, String target) {
        int i = 0, j = 0;
        int n = x.length(), m = target.length();

        while (i < n && j < m) {
            if (x.charAt(i) != target.charAt(j)) {
                return false;
            }

            int len1 = getRepeatedLen(x, i);
            int len2 = getRepeatedLen(target, j);

            if (len1 > len2 || (len2 < 3 && len1 != len2)) {
                return false;
            }

            i += len1;
            j += len2;
        }

        // Both strings should be fully traversed for a match
        return i == n && j == m;
    }

//    private boolean canForm(String x, String target){
//        int i = 0;
//        int j = 0;
//        int n = x.length();
//        int m = target.length();
//        while (i < n && j < m){
//            if (x.charAt(i) != target.charAt(j)){
//                return false;
//            }
//            int len1 = getRepeatedLen(x, i);
//            int len2 = getRepeatedLen(target, i);
//
//            if (len1 > len2 || (len2 < 3 && len1 != len2)) {
//                return false;
//            }
//
//
//            i += len1;
//            j += len2;
//        }
//
//        return i == n && j == m;
//    }

    private int getRepeatedLen(String input, int index) {
        int count = 0;
        char currentChar = input.charAt(index);
        while (index + count < input.length() && input.charAt(index + count) == currentChar) {
            count++;
        }
        return count;
    }

//    private int getRepeatedLen(String input, int i){
//        int init = input.charAt(i);
//        int j = i;
//        int res = 0;
//        while(input.charAt(j) == init && j + i < input.length()){
//            j += 1;
//            res += 1;
//        }
//        return res;
//    }


    // LC 1110
    // https://leetcode.com/problems/delete-nodes-and-return-forest/
    // 6.13 start -> 6.40

    /**
     * ex 1) root = [1,2,3,4,5,6,7], to_delete = [3,5]
     * <p>
     * 1
     * 2     3
     * 4  5   6  7
     * <p>
     * ex 2)
     * <p>
     * Input: root = [1,2,4,null,3], to_delete = [3]
     * <p>
     * 1
     * 2    4
     * 3
     */
//    public List<TreeNode> delNodes(TreeNode root, int[] to_delete) {
//
//        List<TreeNode> res = new ArrayList<>();
//        // TODO : optimize below
//        List<Integer> toDeleteList = new ArrayList<>();
//        for (int x : to_delete) {
//            toDeleteList.add(x);
//        }
//
//        if (root == null || to_delete.length == 0) {
//            return res;
//        }
//
//        // bfs
//        Queue<TreeNode> q = new LinkedList<>();
//        //q.add(new TreeInfo(0, root));
//        q.add(root);
//        while (!q.isEmpty()) {
//            TreeNode node = q.poll();
//            if (toDeleteList.contains(node.val)) {
//                // mark as -1
//                root.val = root.val * (-1);
//            }
//
//            if (node.left != null) {
//                q.add(node.left);
//            }
//            if (node.right != null) {
//                q.add(node.right);
//            }
//        }
//
//        // dfs
//        getForest(root, new ArrayList<>());
//        System.out.println("res = " + res);
//
//        return res;
//    }
//
//    public void getForest(TreeNode root, List<Integer> cur){
//        if (root.val < 0){
//            cur.add(null);
//            res.add(cur);
//            cur = new ArrayList<>();
//        }
//        cur.add(root.val);
//        if (root.left != null){
//            getForest(root.left, cur);
//        }
//        if (root.right != null){
//            getForest(root.right, cur);
//        }
//    }

    // bfs
    public List<TreeNode> delNodes(TreeNode root, int[] to_delete) {

        List<TreeNode> res = new ArrayList<>();

        if (root == null){
            return res;
        }

        List<Integer> toDelList = new ArrayList<>();
        for (int x : to_delete){
            toDelList.add(x);
        }

        if (root.left == null && root.right == null){
            if (toDelList.contains(root.val)){
                return res;
            }
            res.add(root);
            return res;
        }

        // go through tree
        Queue<TreeNode> q = new LinkedList<>();
        q.add(root);
        while (!q.isEmpty()){

            TreeNode node = q.poll();
            System.out.println(">>> node val = " + node.val);
            System.out.println(">>> q = ");
            q.forEach(System.out::println);

            if (node.left != null){
                q.add(root.left);
            }

            if (node.right != null){
                q.add(root.right);
            }

            // TODO : check
            if (toDelList.contains(root.left.val)){
                root.left = null;
            }

            if (toDelList.contains(root.right.val)){
                root.right = null;
            }

        }

        // TODO : check
        // prepare final res
        res.add(root);
        return res;
    }


    public List<TreeNode> delNodes_2(TreeNode root, int[] to_delete) {
        if (root == null) {
            return new ArrayList<>();
        }

        Set<Integer> toDeleteSet = new HashSet<>();
        for (int val : to_delete) {
            toDeleteSet.add(val);
        }

        List<TreeNode> forest = new ArrayList<>();

        Queue<TreeNode> nodesQueue = new LinkedList<>();
        nodesQueue.add(root);

        while (!nodesQueue.isEmpty()) {
            TreeNode currentNode = nodesQueue.poll();

            if (currentNode.left != null) {
                nodesQueue.add(currentNode.left);
                // Disconnect the left child if it needs to be deleted
                if (toDeleteSet.contains(currentNode.left.val)) {
                    currentNode.left = null;
                }
            }

            if (currentNode.right != null) {
                nodesQueue.add(currentNode.right);
                // Disconnect the right child if it needs to be deleted
                if (toDeleteSet.contains(currentNode.right.val)) {
                    currentNode.right = null;
                }
            }

            // If the current node needs to be deleted, add its non-null children to the forest
            if (toDeleteSet.contains(currentNode.val)) {
                if (currentNode.left != null) {
                    forest.add(currentNode.left);
                }
                if (currentNode.right != null) {
                    forest.add(currentNode.right);
                }
            }
        }

        // Ensure the root is added to the forest if it is not to be deleted
        if (!toDeleteSet.contains(root.val)) {
            forest.add(root);
        }

        return forest;
    }



//    public class TreeInfo{
//        public int idx;
//        public TreeNode treeNode;
//
//        public TreeInfo(){
//
//        }
//
//        public TreeInfo(int idx, TreeNode treeNode) {
//            this.idx = idx;
//            this.treeNode = treeNode;
//        }
//    }

    // LC 1066
    // https://leetcode.ca/all/1066.html#google_vignette
    // 4.30
    /**
     * exp 1:
     *
     * Input: workers = [[0,0],[2,1]], bikes = [[1,2],[3,3]]
     * Output: 6
     *
     *  op1 : 3 + 3 = 6
     *      - We assign bike 0 to worker 0, bike 1 to worker 1. The Manhattan distance of both assignments is 3, so the output is 6.)
     *
     *  op2 : 2 + 6 = 8
     *
     *  b0 -> w 0
     *  b1 -> w 1
     *
     *  b0 -> w1
     *  b1 -> w0
     *
     *  6 < 8,
     *  so, choose op1
     *
     *  exp 2 :
     *
     * Input: workers = [[0,0],[1,1],[2,0]], bikes = [[1,0],[2,2],[2,1]]
     * Output: 4
     *
     *   op1 :  1 + 2 + 1 = 4
     *     -  We first assign bike 0 to worker 0, then assign bike 1 to worker 1 or worker 2, bike 2 to worker 2 or worker 1. Both assignments lead to sum of the Manhattan distances as 4.
     *
     *   op2 : 4 + 1 + 1 = 6
     *    - bike 0 -> worker 1
     *    - bike 1 -> worker 0
     *    - bike 2 -> worker 2
     *
     *   op3 : 1 + 1 + 2 = 4
     *    - bike 0 -> w 0
     *    - bike 1 -> w 2
     *    - bike 2 -> w 1
     *
     *   op4 : 3 + 2 + 1
     *    - bike 0 -> w2
     *    - bike 1 -> bike 1
     *    - bike 2 -> w 0
     *
     *    ..
     *
     *    op 6
     *
     *
     */
    public int assignBikes_1_1(int[][] workers, int[][] bikes){

        int n = workers.length;
        int m = bikes.length;
        int k = 0;
        // get all possible Manhattan distances
        int[][] cache = new int[n * m][3];
        for (int i = 0; i < n; i++){
            for (int j = 0; j < m; j++){
                int dist
                        = Math.abs(workers[i][0] - bikes[j][0]) + Math.abs(workers[i][1] - bikes[j][1]);
                // NOTE !!! here we append 3 var
                //cache.add(dist, i, j);
                cache[k] = new int[]{dist, i, j};
                k += 1;
            }
        }

        // sorting
        Arrays.stream(cache).sorted(new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                // dist
                if (o1[0] != o2[0]){
                    return o1[0] - o2[0];
                }
                // compare i (worker index)
                if (o1[1] != o2[1]){
                    return o1[1] - o2[1];
                }
                // compare j (bike index)
//                if (o1[2] != o2[2]){
//                    return o1[2] - o2[2];
//                }
                // TODO : check
               // return 0;
                return o1[2] - o2[2];
            }
        });

        int res = 0;

        // prepare final result
        Boolean[] visited_1 = new Boolean[n];
        Boolean[] visited_2 = new Boolean[m];
        for (int[] c : cache){
            //Object y = x;
            int dist = c[0];
            int i = c[1];
            int j = c[2];
            if (!visited_1[i] && !visited_2[j]){
                res += dist;
                visited_1[i] = true;
                visited_2[j] = true;
            }
        }

        return res;
    }

    // LC 659
    // https://leetcode.com/problems/split-array-into-consecutive-subsequences/
    // 7.51 pm - 8.10 pm
    /**
     *  NOTE
     *
     *  1. nums that is sorted in non-decreasing order.
     *
     *  conditions are true:
     *
     *  - 1) Each subsequence is a consecutive increasing sequence (i.e. each integer is exactly one more than the previous integer).
     *  - 2) All subsequences have a length of 3 or more.
     *
     *   exp 1
     *
     *   Input: nums = [1,2,3,3,4,5]
     *   Output: true
     *   Explanation: nums can be split into the following subsequences:
     *   [1,2,3,3,4,5] --> 1, 2, 3
     *    x x x
     *
     *
     *   [1,2,3,3,4,5] --> 3, 4, 5
     *          x x x
     *
     *   exp 2
     *
     *   Input: nums = [1,2,3,3,4,4,5,5]
     *   Output: true
     *   Explanation: nums can be split into the following subsequences:
     *   [1,2,3,3,4,4,5,5] --> 1, 2, 3, 4, 5
     *   [1,2,3,3,4,4,5,5] --> 3, 4, 5
     *
     *
     *   [1,2]
     *
     *   3,4,5
     *   3,4,5
     *
     *
     *  exp 3
     *
     *   nums = [1,2,3,4,4,5]
     *
     *   1,2,3,4
     *   4
     *
     *  exp 4
     *
     *  nums = [1,2,3,3,4,4,5,5]
     *
     *  1,2,3,4,5
     *  3,4,5
     *
     *  exp 5
     *
     *  nums = [1,2,3,3,3,4,4,4,5,5,5]
     *
     *  1,2,3,4,5
     *  3,4,5
     *  3,4,5
     */
//    public boolean isPossible(int[] nums) {
//
//        if (nums.length == 1){
//            return true;
//        }
//
//        if (nums.length < 3){
//            return false;
//        }
//
//        List<List<Integer>> cache = new ArrayList<>();
//
//        //cache.add(new ArrayList<>());
//        // brute force
//        for(int x : nums){
//            int size = cache.size();
//            if(size==0){
//                List<Integer> tmp = new ArrayList<>();
//                tmp.add(x);
//                cache.add(tmp);
//                continue;
//            } if (!cache.get(size).contains(x)){
//                List<Integer> tmp = cache.get(size);
//                tmp.add(x);
//                cache.add(tmp);
//                continue;
//        }
//
//        return false;
//    }

    // IDEA : HASHMAP + PQ
    public boolean isPossible(int[] nums) {
        // edge case
        if (nums == null || nums.length == 0){
            return false;
        }
        // check subsequence
        // key : last element (?)
        // val : PQ (subsequence ?)
        Map<Integer, PriorityQueue<Integer>> map = new HashMap<>();
        for (int x : nums){
            int subSeqCnt = 0;
            if (map.containsKey(x-1)){
                /**
                 * .poll() : get and remove last element
                 */
                subSeqCnt = map.get(x-1).poll();
                if (map.get(x-1).isEmpty()){
                    map.remove(x-1);
                }
            }
            PriorityQueue<Integer> pq = new PriorityQueue<>();
            PriorityQueue<Integer> curPq = map.get(x);
            if (curPq.isEmpty()){
                pq.add(1);
                map.put(x, pq);
            }else{
                curPq.add(subSeqCnt+1); // ?
                map.put(x, curPq);
            }
            //map.putIfAbsent(x, pq);
        }

        // check
        int cnt = 0;
        for (Integer k : map.keySet()){
            PriorityQueue<Integer> _pq = map.get(k);
            if (_pq.size() < 3){
                return false;
            }
            cnt += _pq.size();
        }
        return cnt == nums.length;
    }


    // LC 271
    /**
     *  exp 1:
     *   input : ["", "helo"]
     *   output : ["", "helo"]
     *
     *   encode : "?,helo"
     *
     *   exp 2:
     *
     *   input : ["helo", "world"]
     *   output :  ["helo", "world"]
     *
     *   encode : "helo,world"
     *
     *
     */
    public class Codec {

        // Encodes a list of strings to a single string.
        public String encode(List<String> strs) {
            //String res = null;
            StringBuilder sb = new StringBuilder();
            for (String x : strs){
                if (x == null){
                    sb.append("?");
                    sb.append(",");
                }else{
                    sb.append(x);
                    sb.append(",");
                }
            }
            // remove last ","
            return sb.deleteCharAt(sb.length()-1).toString();
        }

        // Decodes a single string to a list of strings.
        public List<String> decode(String s) {
            List<String> res = new ArrayList<>();
            if (s == null){
                return res;
            }
            for (String x : s.split(",")){
                if (x.equals("?")){
                    res.add("");
                }else{
                    res.add(x);
                }
            }
            return res;
        }
    }

    // LC 727
    // https://leetcode.ca/all/727.html
    // 6.18 pm - 6.30 pm
    /**
     *  dp equation:
     *
     *  dp[i][j] = min(dp[i-1][j-1]
     *
     */
//    public String minWindow_2(String s1, String s2) {
//        return null;
//    }

    // LC 753
    // 7.34 - 8.00 PM
    // https://leetcode.com/problems/cracking-the-safe/
    /**
     * n digit
     * each digit in [0, k-1] range
     * check recent n digits if it same as expect pwd
     *
     *  There is a safe protected by a password.
     *  The password is a sequence of n digits where each digit
     *  can be in the range [0, k - 1].
     *
     */
    public String crackSafe(int n, int k) {
        return null;
    }




}
