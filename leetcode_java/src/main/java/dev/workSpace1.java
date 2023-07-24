package dev;

import LeetCodeJava.DataStructure.TreeNode;

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

    public static void main(String[] args) {
        int[] x = new int[5];
        x[0] = -1;
        x[1] = 5;
        x[2] = 3;

        Arrays.stream(x).forEach(System.out::println);

        System.out.println("-----------");

        Arrays.sort(x);

        Arrays.stream(x).forEach(System.out::println);

    }

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


}
