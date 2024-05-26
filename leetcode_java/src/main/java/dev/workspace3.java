package dev;

import LeetCodeJava.DataStructure.ListNode;
import LeetCodeJava.DataStructure.TreeNode;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {

        if (root.equals(p) || root.equals(q)) {
            return root;
        }

        // BST property : right > root > left
        if (p.val > root.val && q.val > root.val){
            return this.lowestCommonAncestor(root.right, p, q);
        }
        if (p.val < root.val && q.val < root.val){
            return this.lowestCommonAncestor(root.left, p, q);
        }

        // p, q are in different side (sub tree)
        return root;
    }

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
    public int maxDepth(TreeNode root) {

        if (root == null){
            return 0;
        }

        if (root.left == null && root.right == null){
            return 1;
        }

        int leftDepth = this.maxDepth(root.left) + 1;
        int rightDepth = this.maxDepth(root.right) + 1;

        return Math.max(leftDepth, rightDepth);
    }


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
    public TreeNode invertTree(TreeNode root) {

        if (root == null){
            return root;
        }

        TreeNode tmp = root.left;
        root.left = invertTree(root.right);
        root.right = invertTree(tmp);

        return root;
    }

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
                (a,b) -> a - b
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


}
