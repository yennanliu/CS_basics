package dev;

import LeetCodeJava.DataStructure.TreeNode;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.*;

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



        // Small PQ (default min-heap)
        PriorityQueue<Integer> smallPQ = new PriorityQueue<>();

        // Big PQ (max-heap)
        PriorityQueue<Integer> bigPQ = new PriorityQueue<>(Comparator.reverseOrder());

        // Add elements to PQs
        smallPQ.add(5);
        smallPQ.add(10);
        smallPQ.add(1);

        bigPQ.add(5);
        bigPQ.add(10);
        bigPQ.add(1);

        // Print elements from PQs
        System.out.println("Small PQ (min-heap):");
        while (!smallPQ.isEmpty()) {
            System.out.println(smallPQ.poll());
        }

        System.out.println("Big PQ (max-heap):");
        while (!bigPQ.isEmpty()) {
            System.out.println(bigPQ.poll());
        }

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

}
