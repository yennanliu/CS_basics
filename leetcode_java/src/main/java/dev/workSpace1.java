package dev;

import LeetCodeJava.DataStructure.ListNode;
import LeetCodeJava.DataStructure.TreeNode;

import java.sql.Array;
import java.util.*;

public class workSpace1 {

    public int carFleet(int target, int[] position, int[] speed) {

        if (position.length == 0 || position.equals(null)) {
            return 0;
        }

        int ans = 0;
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < position.length; i++) {
            int _position = position[i];
            int _speed = speed[i];
            map.put(_position, _speed);
        }

        Arrays.sort(map.keySet().toArray());

        //TreeMap<Integer, Integer> _map = new TreeMap<>(map);
        //int[] _positions = Arrays.asList(position).stream().sorted((x, y) -> Integer.compare(x, y)).collect(Collectors.toList());
        Arrays.sort(position);
        //int[] time = new int[position.length];
        Stack<Integer> times = new Stack<>();
        Stack<Integer> res = new Stack<>();
        for (int j = 0; j < position.length; j++) {
            int _arrived_time = (target - position[j]) / map.get(position[j]);
            times.push(_arrived_time);
        }

        while (!times.isEmpty()) {
            int cur = times.pop();
            if (cur > times.peek()) {
                res.push(cur);
            }
        }

        return res.size();
    }

//    public static void main(String[] args) {
//        int[] x = new int[5];
//        x[0] = -1;
//        x[1] = 5;
//        x[2] = 3;
//
//        Arrays.stream(x).forEach(System.out::println);
//
//        System.out.println("-----------");
//
//        Arrays.sort(x);
//
//        Arrays.stream(x).forEach(System.out::println);
//
//    }

    public boolean searchMatrix(int[][] matrix, int target) {

        int length = matrix[0].length;
        int width = matrix.length;

        // flatten matrix + binary search
        int l = 0;
        int r = length * width - 1;
        while (r >= l){
            int mid = (l + r) / 2;
            int cur = matrix[mid / length][mid % length];
            if (cur == target){
                return true;
            }else if (cur < target){
                l = mid + 1;
            }else{
                r = mid - 1;
            }
        }

        return false;
    }

    public List<String> generateParenthesis(int n) {

        return null;
    }


    // https://leetcode.com/problems/maximum-depth-of-binary-tree/
    // recursive
    public int maxDepth(TreeNode root) {

        if (root == null){
            return 0;
        }

//        if (root.left == null && root.right == null){
//            return 1;
//        }

        int leftD = maxDepth(root.left) + 1;
        int rightD = maxDepth(root.right) + 1;

        return Math.max(leftD, rightD);
    }

    private int getDepth(TreeNode root){
        return 0;
    }


    // https://leetcode.com/problems/minimum-depth-of-binary-tree/

    int depth = 0;
    List<Integer> cache = new ArrayList<>();
    public int help(TreeNode root) {
        if (root == null){
            cache.add(this.depth);
            this.depth = 0;
            return 0;
        }

        int leftD = help(root.left) + 1;
        int rightD = help(root.right) + 1;
        return Math.min(leftD, rightD);
    }

    // https://leetcode.com/problems/diameter-of-binary-tree/
    public int diameterOfBinaryTree(TreeNode root) {

        if (root == null){
            return 0;
        }

        int leftD = diameterOfBinaryTree(root.left) + 1;
        int rightD = diameterOfBinaryTree(root.right) + 1;

        return Math.max(leftD, rightD) + 1;
    }



    // A height-balanced binary tree
    // is a binary tree in which the depth of the two subtrees of every node
    // never differs by more than one.
    public boolean isBalanced(TreeNode root) {

        Boolean result = (
                    (Math.abs(getDepth2(root.left) - getDepth2(root.right)) <= 1) &&
                    (isBalanced(root.left)) &&
                    (isBalanced(root.right))
                );
        return result;
    }

    private int getDepth2(TreeNode root){

        if (root == null){
            return 0;
        }

        int leftDep = getDepth2(root.left) + 1;
        int rightDep = getDepth2(root.right) + 1;

        return Math.max(leftDep, rightDep) + 1;
    }

    // https://leetcode.com/problems/subtree-of-another-tree/
    public boolean isSubtree(TreeNode root, TreeNode subRoot) {

        if (root == null) {
            return false;
        }

//        if (root == null && subRoot != null){
//            return false;
//        }


        // check

        // case 1)
        if (isSame(root, subRoot)){
            return true;
        }

        // case 2)
        return isSame(root.left, subRoot) || isSame(root.right, subRoot);
    }

    private Boolean isSame(TreeNode node1, TreeNode node2){

        if (node1 == null || node2 == null){
            return node1 == null && node2 == null;
        }

//        if (node1.val != node2.val){
//            return false;
//        }

        return node1.val == node2.val && isSame(node1.left, node2.left) && isSame(node1.right, node2.right);
    }


    // https://leetcode.com/problems/top-k-frequent-elements/
    public int[] topKFrequent(int[] nums, int k) {

        if (nums.equals(null) || nums.length == 0){
            return null;
        }

        // min stack
//        PriorityQueue<Integer> pq = new PriorityQueue<>();
//        for (int x : nums){
//            if (pq.isEmpty()){
//                pq.add(x);
//            }
//        }

        HashMap<Integer, Integer> map = new HashMap<>();
        for (int x : nums){
            if (!map.containsKey(x)){
                map.put(x, 1);
            }else{
                int cur = map.get(x);
                map.put(x, cur+1);
            }
        }

        int _size = map.keySet().size();
        int[][] tmp = new int[_size][2];
        int z = 0;
        for (int j : map.keySet()){
            tmp[z][0] = j;
            tmp[z][1] = map.get(j);
            z += 1;
        }

        // order
        Arrays.sort(tmp, (x, y) -> Integer.compare(-x[1], -y[1]));
        System.out.println(">>> ");
        Arrays.stream(tmp).forEach(x -> System.out.println(x[0]));
        System.out.println(">>> ");

        int[] res = new int[k];
//        for (int i = tmp.length-1; i >=0;  i--){
//            res[0] = tmp[i][0];
//        }
        for (int i = 0 ; i < tmp.length; i++){
            res[i] = tmp[i][0];
        }

        return res;
    }

    // https://leetcode.com/problems/evaluate-reverse-polish-notation/
    public int evalRPN(String[] tokens) {

        if (tokens.length == 0){
            return 0;
        }

        int ans = 0;
        String signs = "+-*/";
        Stack<Integer> stack = new Stack<>();
        for(String x : tokens){

            if (!signs.contains(x)){
                stack.push(Integer.parseInt(x));
                continue;
            }

            int _tmp = 0;
            int _first = stack.pop();
            int _second = stack.pop();

            if (x.equals("+")){
                _tmp = _second + _first;
                stack.push(_tmp);

            } else if (x.equals("-")){
                _tmp = _second - _first;
                stack.push(_tmp);

            }else if (x.equals("+")){
                _tmp = _second * _first;
                stack.push(_tmp);

            }else{
                _tmp = _second / _first;
                stack.push(_tmp);
            }
        }

        System.out.println(">>> stack size = " + stack.size());
        return stack.pop();
    }

    // https://leetcode.com/problems/remove-all-adjacent-duplicates-in-string/
    public String removeDuplicates(String s) {

        if (s.equals(null)){
            return s;
        }

        String ans = "";
        Stack<String> stack = new Stack<>();
        //Arrays.
        // char array [] = strs.toCharArray();
        char[] s_array = s.toCharArray();
        for (char x : s_array){
            String _x = String.valueOf(x);
            if (stack.isEmpty()){
                stack.push(_x);
            }else{
                if (_x.equals(stack.peek())){
                    stack.pop();
                }else{
                    stack.push(_x);
                }
            }
        }

        StringBuilder sb = new StringBuilder();

        while(!stack.isEmpty()){
            String cur = stack.pop();
            //ans += cur;
            sb.append(cur);
        }

        return sb.reverse().toString();
    }

    // Test
    //TreeNode root2 = new TreeNode();
    public List<Integer> rightSideView(TreeNode root) {
        if (root == null){
            return null;
        }

        TreeNode root2 = this._dfs(root);
        System.out.println("---> ");
        System.out.println(">>> root2 = " + root2.toString());
        System.out.println(">>> root2.val  = " + root2.val);
        System.out.println(">>> root2.right  = " + root2.right);
        System.out.println(">>> root2.left  = " + root2.left);
        System.out.println("---> ");
        return null;
    }

    private TreeNode _dfs(TreeNode node){

        if (node == null){
            return null;
        }

        TreeNode root2 = node;
        root2.left = this._dfs(node.left);
        root2.right = this._dfs(node.right);

        /** NOTE !!! we need to return root as final step */
        return root2;
    }


    // https://leetcode.com/problems/kth-largest-element-in-a-stream/
    class KthLargest {

        // attr
        PriorityQueue<Integer> heap;
        int k;

        // constructor
        public KthLargest(int k, int[] nums) {

            this.k = k;
            this.heap = new PriorityQueue<>();
            for (int x : nums){
                //this.heap.add(x);
                this.heap.offer(x);
            }

            // pop elements if heap size > k
            while(this.heap.size() > k){
                this.heap.poll();
            }
        }

        public int add(int val) {

            this.heap.offer(val);
            if (heap.size() > k){
                this.heap.poll();
            }

            return this.heap.peek();

//            this.heap.add(val);
//            List<Integer> tmp = new ArrayList<>();
//            int _size = this.heap.size();
//            int _cnt = 0;
//            Integer toPop = null;
//            while (_size > 0){
//                int cur = this.heap.remove();
//                if (_cnt == this.k){
//                    toPop = cur;
//                    return cur;
//                }
//                tmp.add(cur);
//                _cnt += 1;
//            }
//
//            // put rest of elements back to current heap
//            for (int x : tmp) {
//                if (toPop != null && x != toPop){
//                    this.heap.add(x);
//                }
//            }
//
//            return 0;
        }

    }


    // https://leetcode.com/problems/design-add-and-search-words-data-structure/
//    class Trie{
//
//        // attr
//        HashMap<String, Trie> children;
//        Boolean isEnd;
//
//        // constructor
//        public Trie(){
//
//        }
//
//    }
//    class WordDictionary {
//
//        // attr
//        Trie trie;
//
//        public WordDictionary() {
//            trie = new Trie();
//        }
//
//        public void addWord(String word) {
//
//            Trie node = this.trie;
//
//            if (word.length() == 0){
//                return;
//            }
//
//            char[] w_array = word.toCharArray();
//            for (char x : w_array){
//                String cur = String.valueOf(x);
//                node.children.put(cur, new Trie());
//                node = node.children.get(cur);
//            }
//
//            node.isEnd = true;
//        }
//
//        public boolean search(String word) {
//
//            Trie node = this.trie;
//
//            if (word.length() == 0){
//                return true;
//            }
//
//            char[] w_array = word.toCharArray();
//            int _len = w_array.length;
//            for (int i = 0; i < w_array.length; i++){
//
//                String cur = String.valueOf(w_array[i]);
//
//                if (node.children.keySet().contains(cur)){
//
//                    node = node.children.get(cur);
//
//                }else{
//
//                    // recursive call
//                    if (cur.equals(".")){
//                        for (String x : node.children.keySet()){
//                            char[] _w_array = Arrays.copyOfRange(w_array, i+1, _len);
//                            search(word.substring(i+1, _len));
//                        }
//                    }else{
//                        if (!this.trie.children.containsKey(cur)){
//                            return false;
//                        }
//                    }
//
//                }
//
//            }
//
//            return false;
//        }
//
//    }


    // https://leetcode.com/problems/implement-trie-prefix-tree/

    class Trie {

        // attr
        Trie node;
        HashMap<String, Trie> children;
        Boolean isEnd;

        // constructor
        public Trie() {
            this.node = new Trie();
            this.children = new HashMap<>();
            this.isEnd = false;
        }

        public void insert(String word) {

            Trie _node = this.node;
            char[] _array_word = word.toCharArray();
            for (char x : _array_word){
                String key = String.valueOf(x);
                if (!_node.children.containsKey(key)){
                    _node.children.put(key, new Trie());
                }

                _node = _node.children.get(key);
            }

            _node.isEnd = true;
        }

        public boolean search(String word) {

            Trie _node = this.node;
            char[] _array_word = word.toCharArray();
            for (char x : _array_word){
                String key = String.valueOf(x);
                if (!_node.children.containsKey(key)){
                    return false;
                }

                _node = _node.children.get(key);
            }

            return true ? _node.isEnd.equals(true) : false;
        }

        public boolean startsWith(String prefix) {

            Trie _node = this.node;
            char[] _array_word = prefix.toCharArray();
            for (char x : _array_word){
                String key = String.valueOf(x);
                if (!_node.children.containsKey(key)){
                    return false;
                }

                _node = _node.children.get(key);
            }
            return true;
        }

    }


    // https://leetcode.com/problems/subsets/

//    List<List<Integer>> ans = new ArrayList();
//    public List<List<Integer>> subsets(int[] nums) {
//
//        List<List<Integer>> ans = new ArrayList();
//
////        if (nums.length == 1){
////            List<List<Integer>> _ans = new ArrayList();
////            this.ans.add(new ArrayList<>());
////            this.ans.add(new ArrayList<>(nums[0]));
////            return this.ans;
////        }
//
//        ans.add(new ArrayList<>());
//        //ans.add();
//        List<Integer> cur = new ArrayList<>();
//        _helper(nums, cur, 0);
//        return this.ans;
//    }
//
//    private void _helper(int[] nums, List<Integer> cur, int idx){
//
//        if (cur.size() > nums.length){
//            return;
//        }
//
//        Collections.sort(cur);
//        if (!this.ans.contains(cur)){
//            System.out.println("new ArrayList<>(cur) = " + new ArrayList<>(cur));
//            //   this.ans.add(new ArrayList<>(cur));
//            this.ans.add(new ArrayList<>(cur));
//            //return;
//        }
//
//        for (int i = 0; i < nums.length; i++){
//            System.out.println(i);
//            int val = nums[i];
//
////            if(!cur.contains(val)){
////                cur.add(val);
////                // recursive
////                _helper(nums, cur, i+1);
////                // backtrack
////                cur.remove(cur.size()-1);
////            }
//
//            cur.add(val);
//            _helper(nums, cur, i+1);
//        }
//
//    }

    List<List<Integer>> ans = new ArrayList();
    int k = 0;

    public List<List<Integer>> subsets(int[] nums) {

        for (k = 0; k < nums.length+1; k++){
            ArrayList<Integer> cur = new ArrayList<>();
            _help(k, nums, cur);
        }

        return ans;
    }

    private void _help(int first, int[] nums, ArrayList<Integer> cur){

        if (cur.size() == k){
            ans.add(new ArrayList<>(cur));
            return;
        }

        for(int i = first; i < nums.length; i++){
            int val = nums[i];
            cur.add(val);
            _help(i + 1, nums, cur);
            cur.remove(cur.size()-1);
        }
    }

    // https://leetcode.com/problems/sum-of-left-leaves/
    // A leaf is a node with no children. A left leaf is a leaf that is the left child of another node.
    int res = 0;
    public int sumOfLeftLeaves(TreeNode root) {

        if (root == null){
            return this.res;
        }

        if (root.left == null && root.right == null){
            return this.res;
        }

        if (root.left != null && root.right == null){
            return root.left.val;
        }

        // helper
        this._help(root, false, 0);
        return this.res;
    }

    private void _help(TreeNode root, boolean isLeft, int lastVal){

        // post traverse
        if (root == null){
            return;
        }

        _help(root.left, true, root.val);
        _help(root.right, false, root.val);

        if (root.left == null && root.right == null){
            if(isLeft){
                System.out.println(">>> lastval = " + lastVal);
                this.res += root.val;
            }
        }
    }

    private int sumOfLeftLeaves(TreeNode subtree, boolean isLeft) {

        // Base case: This is an empty subtree.
        if (subtree == null) {
            return 0;
        }

        // Base case: This is a leaf node.
        if (subtree.left == null && subtree.right == null) {
            if (isLeft){
                return subtree.val;
            }else{
                return 0;
            }
        }

        // Recursive case: We need to add and return the results of the
        // left and right subtrees.
        return sumOfLeftLeaves(subtree.left, true) + sumOfLeftLeaves(subtree.right, false);
    }

    // https://leetcode.com/problems/word-search/
    // backtrack + recursive
    public boolean exist(char[][] board, String word) {

        int len = board.length;
        int width = board[0].length;

        if (board.length == 1 && board[0].length == 1){
             board[0].toString();
        }

        for (int i = 0; i < len; i++){
            for (int j = 0; j < width; j++){
                List<String> cur = new ArrayList<>();
                if(_help(board, word, cur, j, i, 0)){
                    return true;
                }
            }
        }

        return false;
    }

    private boolean _help(char[][] board, String word, List<String> cur, int x, int y, int idx) {

        int len = board.length;
        int width = board[0].length;

        // directions : up, down, left, right
        List<List<Integer>> direc = new ArrayList<>();
        direc.add(Arrays.asList(0, 1)); // (x, y)
        direc.add(Arrays.asList(0, -1));
        direc.add(Arrays.asList(-1, 0));
        direc.add(Arrays.asList(1, 0));

        if (cur.size() >= word.length()) {
            return true;
        }

//        if (arrayToStr(cur).equals(word)){
//            return true;
//        }

        if (x < 0 || x >= width || y < 0 || y >= len || board[y][x] != word.charAt(idx)) {
            return false;
        }

        //boolean res = false;

        for (List<Integer> d : direc) {
            x += d.get(0);
            y += d.get(1);
            //boolean res = false;
            char val = board[y][x];
            String _val = String.valueOf(val);
            board[y][x] = '#';
            cur.add(_val);
            // _help(char[][] board, String word, List<String> cur, int x, int y){
            if (_help(board, word, cur, x, y, idx + 1)) {
                return true;
            }
            //idx -= 1;
            board[y][x] = val;
            return false;
        }

        // ?
        return false;
    }


    private static String arrayToStr(List<String> input){
        String output = "";
        for (String x : input){
            output += String.valueOf(x);
        }

        return output;
    }

    // https://leetcode.com/problems/palindrome-partitioning/
    List<List<String>> _ans = new ArrayList<>();
    public List<List<String>> partition(String s) {

//        if (s.length() == 1){
//            List<String> tmp = new ArrayList<>();
//            tmp.add(s);
//            this._ans.add(tmp);
//            return this._ans;
//        }

        // helper func
        List<String> _cur = new ArrayList<>();
        _help(s, 0, _cur);
        return this._ans;
    }

    private void _help(String s, int start_idx, List<String> cur){

        // ?? >= or ==
        if (start_idx >= s.length()){
            this._ans.add(new ArrayList<>(cur));
        }

        for(int i = start_idx; i < s.length(); i++){
            if (isPalindrome(s, start_idx, i+1)){
                cur.add(s.substring(start_idx, i + 1));
                _help(s, start_idx+1, cur);
                //end_idx -= 1;
                cur.remove(cur.size()-1);
            }
        }

    }

    boolean isPalindrome(String s, int low, int high) {
        while (low < high) {
            if (s.charAt(low++) != s.charAt(high--)) return false;
        }
        return true;
    }

    private Boolean _check(String input){
        int l = 0;
        int r = input.length()-1;
        System.out.println(">>> _check" + " l = " + l + " r = " + r);
        while (r > l){
            if (input.charAt(r) != input.charAt(l)){
                return false;
            }
            r -= 1;
            l += 1;
        }
        return true;
    }


    public static void main(String[] args) {

//        char[] my_char = new char[3];
//        my_char[0] = 'a';
//        my_char[1] = 'b';
//        my_char[2] = 'c';
//        System.out.println(String.valueOf(my_char));
        //System.out.println(arrayToStr(my_char));

        String my_str = "abc";
        System.out.println(my_str.substring(0, 1));
        System.out.println(my_str.substring(0, my_str.length()));
        System.out.println(my_str.charAt(2));
    }


}
