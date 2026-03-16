package ws;

import LeetCodeJava.DataStructure.ListNode;
import LeetCodeJava.DataStructure.TreeNode;
//import com.sun.org.apache.bcel.internal.generic.FADD;

import java.util.*;


public class Workspace22 {

    // LC 198
    // 7.33 - 43 am
    /**
     *  -> Given an integer array nums representing
     *  the amount of money of each house,
     *  return the maximum amount of money you can rob
     *  tonight without alerting the police.
     *
     *   NOTE:
     *
     *     it will automatically contact the police if
     *     two adjacent houses were broken into on the same night.
     *
     *
     *  -------------------
     *
     *   IDEA 1) BRUTE FORCE
     *
     *   IDEA 2) DP ????
     *
     *     - DP definition
     *        dp[i] = max money that robber can rob at idx = i
     *
     *     - DP eq
     *         can rob cur idx (idx=i) or not
     *         dp[i] = max( dp[i-2] + nums[i], dp[i-1] )
     *
     *
     *  -------------------
     *
     *
     */
    //  IDEA 2) DP ????
    public int rob_1_99(int[] nums) {
        // edge
        if(nums == null || nums.length == 0){
            return 0;
        }
//        if(nums.length == 1){
//            return nums[0];
//        }

        // ??
        int[] dp = new int[nums.length]; // ???
        dp[0] = nums[0]; // ??
        dp[1] = Math.max(nums[0], nums[1]); // ??

        for(int i = 2; i < nums.length; i++){
            // dp[i] = max( dp[i-2] + nums[i], dp[i-1] )
            dp[i] = Math.max( dp[i-2] + nums[i], dp[i-1] );
        }

        return dp[nums.length - 1];
    }


    // LC 213
    // 7.52 - 8.02 am
    /**
     *
     *  Given an integer array nums representing the amount of money of each house,
     *
     *  arranged in a circle.
     *
     *  -> return the maximum amount of money you can rob
     *  tonight without alerting the police.
     *
     *
     *  ------------------
     *
     *      *   IDEA 2) DP ????
     *      *
     *      *     - DP definition
     *      *        dp[i] = max money that robber can rob at idx = i
     *      *
     *      *     - DP eq
     *              2 cases
     *               1. rob at idx = 0
     *
     *                  (for idx in 0 to n-2)  // ????
     *                  dp[i] = max( dp[i-2] + nums[i], dp[i-1] )
     *
     *               2. rob at idx = n -1
     *
     *                  (for idx in 2 to n-1)
     *                  dp[i] = max( dp[i-2] + nums[i], dp[i-1] )
     *
     *
     *
     *  ------------------
     *
     *
     */
    // 18.25 - 19.01 pm
    /**
     *  IDEA 1) 1D DP
     *
     *
     *   - DP def
     *      - dp[i] = max money can rob at idx = i
     *
     *   - DP eq
     *      *   - 2 cases
     *      *     - rob idx = 0
     *      *     - rob idx = nums.len - 1
     */
    public int rob(int[] nums) {
        // edge
        if(nums == null || nums.length == 0){
            return 0;
        }

        if (nums.length == 1) {
            return nums[0];
        }

        if (nums.length == 2) {
            return Math.max(nums[0], nums[1]);
        }


        int n = nums.length;

        // ??
        // dp: rob idx = 0, but NOT rob idx = n-1
        int[] dp1 = new int[n]; // ???
        // init !!!
        dp1[0] = nums[0];
        //dp1[1] = nums[0]; // ???
        dp1[1] = Math.max(nums[0], nums[1]);

        for(int i = 2; i < n - 1; i++){
           // dp1[i] = Math.max(dp1[i-2] + nums[i], dp1[i-1]);
            dp1[i] = Math.max(dp1[i - 2] + nums[i], dp1[i - 1]);
        }

        // dp2: NOT rob idx = 0, but rob idx = n-1
        int[] dp2 = new int[n]; // ???
        dp2[0] = 0;
        dp2[1] = nums[1]; /// ???

        for(int i = 2; i < n; i++){
            dp2[i] = Math.max(dp2[i-2] + nums[i], dp2[i-1]);
        }

        // ???
        return Math.max(dp1[n-2], dp2[n-1]);
    }











    public int rob_99(int[] nums) {
        // edge
        if(nums == null || nums.length == 0){
            return 0;
        }

        // ??
        // dp: rob idx = 0, but NOT rob idx = n-1
        int[] dp = new int[nums.length]; // ???

        // dp2: NOT rob idx = 0, but rob idx = n-1
        int[] dp2 = new int[nums.length]; // ???

        dp[0] = nums[0]; // ??
        //dp[1] = 0;
        // NOTE !!! below
        dp[1] = Math.max(nums[0], nums[1]);

        dp2[0] = 0;
        dp2[1] = nums[1];

        int globalMax = 0;

        for(int i = 2; i < nums.length; i++){
            dp[i] = Math.max( dp[i-2] + nums[i], dp[i-1] );

            globalMax = Math.max(dp[i], Math.max(globalMax, nums[i]));
        }


        // ????
        for(int i = 2; i < nums.length - 1; i++){
            dp2[i] = Math.max( dp2[i-2] + nums[i], dp2[i-1] );

            globalMax = Math.max(dp2[i], Math.max(globalMax, nums[i]));
        }


        // /?????
       // return Math.max(dp[nums.length - 2], dp2[nums.length - 1]);
        return globalMax;

    }


    // LC 437
    // 8.45 - 55 am
    /**
     *  -> return the number of paths
     *  where the sum of the values
     *  along the path equals targetSum.
     *
     *
     *
     *  -------------
     *
     *  IDEA 1) DFS (post order traverse)
     *
     *  IDEA 2) DFS (post order traverse) + prefix sum + hashmap ???????
     *
     *
     */

    // path:
    //
    public int pathSum(TreeNode root, int targetSum) {

        return 0;
    }





    int nodeCnt = 0; // ???
    // ????
    Map<Integer, Integer> map = new HashMap<>(); // { prefix_sum : cnt }
    public int pathSum_99(TreeNode root, int targetSum) {
        // edge
        if(root == null){
            return 0;
        }

        // NOTE !!!
        // Base case: a prefix sum of 0 has occurred once (the empty path)
        map.put(0, 1);

        nodeSumDfs(root, targetSum, 0);
        return nodeCnt;
    }

    private void nodeSumDfs(TreeNode root, int targetSum, int curSum){
        if(root == null){
            return;
        }

        nodeSumDfs(root.left, targetSum, curSum);
        nodeSumDfs(root.right, targetSum, curSum);

        // ???
        curSum += root.val;
        // ???

        // cumsum_x - cumsum_y = target
        // -> cumsum_x - target = cumsum_y
//        if(root.val == targetSum){
//            nodeCnt += 1;
//        }

        // ???
        // cumsum_x - cumsum_y = target
        // -> cumsum_x - target = cumsum_y
        if(map.containsKey(targetSum - curSum)){
            nodeCnt += map.get(targetSum - curSum);
        }

        // update map
        map.put(curSum, map.getOrDefault(curSum, 0) + 1);


    }


    // LC 652
    // 11.02 - 12 am
    /**
     *  ->  all duplicate subtrees.
     *
     *  Two trees are duplicate if they
     *  have the same structure with the same node values.
     *
     *  ----------------
     *
     *   IDEA 1) DFS + POST order traverse
     *
     *
     *  ----------------
     *
     *
     */

    // LC 652
    // 17.31 - 41 pm
    // IDEA 1) DFS + POST ORDER TRAVERSE ???
    Map<String, Integer> pathCnt = new HashMap<>();
    List<TreeNode> res_100 = new ArrayList<>();
    public List<TreeNode> findDuplicateSubtrees(TreeNode root) {
        // edge


       // List<TreeNode> res = new ArrayList<>();
        helperDFS(root, "");
        return res_100;
    }

    private String helperDFS(TreeNode root, String path){
        // edge ???
        if(root == null){
            return "#"; // ???
        }

        // post order
        // left -> right -> root

        String _left = helperDFS(root.left, path);
        String _right = helperDFS(root.right, path);

        path = root.val + "," + _left + "," + _right;

        // ???
        if(pathCnt.containsKey(path) && pathCnt.get(path) == 1){
            res_100.add(root);
        }
        // update map
        pathCnt.put(path, pathCnt.getOrDefault(path, 0) + 1);

        return path;
    }







    // 10.48 - 58 AM
    // POST ORDER ???
    // -> path root.val + "," + _left + "," + _right // ???
    // ????
    // NOTE !!! Map to store: SerializedPath -> Count
    Map<String, Integer> pathCntMap = new HashMap<>();
    List<TreeNode> duplicatedNode = new ArrayList<>();
    public List<TreeNode> findDuplicateSubtrees_95(TreeNode root) {
//        // edge
//        if(root == null){
//            // return null;
//            return duplicatedNode;
//        }
//        if(root.left == null && root.right == null){
//            // return null;
//            return duplicatedNode;
//        }

        // ???
        dfsHelper(root, "");

        return duplicatedNode;
    }

    private String dfsHelper(TreeNode root, String path){
        // ??
        if(root == null){
            // return null;
           // return res;
           // return null;
            return "#"; // ????
        }

        String _left = dfsHelper(root.left, path);
        String _right = dfsHelper(root.right, path);

        // ???
        path = root.val + "," + _left + "," + _right;

        // note !!!
        // update to map
        pathCntMap.put(path,
                pathCntMap.getOrDefault(path, 0) + 1);

        // ???
        // only add to res when cnt == 2
        if(pathCntMap.get(path) == 2){
            duplicatedNode.add(root);
        }

       // return root;

        return path; // ???
    }







    // IDEA 1) DFS + POST order traverse
    List<TreeNode> res = new ArrayList<>();
    // ???
    // { path : cnt }
    // /??
    // v2: { path: [node_1, node_2 ,.. ] }
    Map<String, List<TreeNode>> pathMap = new HashMap<>();
    public List<TreeNode> findDuplicateSubtrees_99(TreeNode root) {
        // edge
        if(root == null){
            // return null;
            return res;
        }

        getDuplicatedNode(root, new StringBuilder());

        for(String p: pathMap.keySet()){
            if(pathMap.get(p).size() > 1){
                for(TreeNode t: pathMap.get(p)){
                    res.add(t);
                }
            }
        }

        return res;
    }

    private void getDuplicatedNode(TreeNode root, StringBuilder sb){
        if(root == null){
           // return null;
            return;
        }

//        TreeNode _left = getDuplicatedNode(root.left, sb);
//        TreeNode _right = getDuplicatedNode(root.right, sb);

        getDuplicatedNode(root.left, sb);
        getDuplicatedNode(root.right, sb);

        // ???
        sb.append(root.val);
        // ???
        List<TreeNode> list = new ArrayList<>();
        if(pathMap.containsKey(sb.toString())){
            list = pathMap.get(sb.toString());
        }

        list.add(root);
        pathMap.put(sb.toString(), list);
        // undo ??? (backtrack)
        sb.deleteCharAt(sb.length() - 1); // ???


        // ???
       // return root;
    }


    // LC 905
    // 7.19 - 29 am
    /**
     *  -> Return any array
     *  that satisfies this condition.
     *
     *
     *  --------------------
     *
     *  IDEA 1) 2 POINTERS ???
     *
     *  IDEA 2) BRUTE FORCE
     *
     *  IDEA 3) CUSTOM SORTING ??
     *
     *  --------------------
     *
     */
    // LC 905
    public int[] sortArrayByParity(int[] nums) {
        // edge
        if(nums == null || nums.length <= 1){
            return nums; // ??
        }

        List<Integer> list = new ArrayList<>();
        for(int x: nums){
            list.add(x);
        }

        Collections.sort(list, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                /**
                 *
                 * // o1 is even, o2 is odd
                 * // o1 is even, o2 is even
                 * // o1 is odd, o2 is even
                 * // o1 is odd, o2 is odd
                 */
                // ???
                boolean isEven1 = o1 % 2 == 0;
                boolean isEven2 = o2 % 2 == 0;
                //return o1.compareTo(o2); // ????
                if(isEven1 && !isEven2){
                    return -1;
                }else if(!isEven1 && isEven2){
                    return 1;
                }
                return 0;
            }
        });

        // prepare res
        int[] res= new int[list.size()];
        int i = 0;
        for(int x: list){
            res[i] = x; //list.get(i);
            i += 1;
        }

        return res;
    }



    // IDEA 3) CUSTOM SORTING ??
    public int[] sortArrayByParity_99(int[] nums) {
        // edge
        if(nums == null || nums.length <= 1){
            return nums; // ??
        }

        List<Integer> list = new ArrayList<>();
        for(int x: nums){
            list.add(x);
        }
        // custom sort
        Collections.sort(list, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                // ????
                // 4 cases:
                /**
                 *
                 * // o1 is even, o2 is odd
                 * // o1 is even, o2 is even
                 * // o1 is odd, o2 is even
                 * // o1 is odd, o2 is odd
                 */
                int diff = 0; // /??
                if(o1 % 2 == 0 && o2 % 2 == 1){
                    diff = 1;
                }else if(o1 % 2 == 1 && o2 % 2 == 0){
                    diff = 1;
                }
                return diff;
            }
        });

        // prepare res
        int[] res= new int[list.size()];
        int i = 0;
        for(int x: list){
            res[i] = x; //list.get(i);
            i += 1;
        }

        return res;
    }


    // LC 26
    // 7.40 - 8.00 am
    /**
     *  -> remove the duplicates in-place such that each unique
     *  element appears only once.
     *  The relative order of the elements should be kept the same.
     *
     *  --------------
     *
     *   IDEA 1) 2 POINTERS ???
     *     - slow - fast pointers
     *
     *   IDEA 2) BRUTE FORCE ???
     *
     *
     *  --------------
     *
     */

    // LC 1209
    // 8.33 - 43 am
    /**
     *  IDEA 1) STACK
     *
     *   FILO
     *
     *   stack: { [val : cnt ]  }
     */
    class ValCnt3{
        String val;
        int cnt;

        ValCnt3(String s, int cnt){
            this.val = s;
            this.cnt = cnt;
        }
    }

    // IDEA 1) STACK
    public String removeDuplicates(String s, int k) {
        // edge
        if(s == null || s.length() == 0){
            return "";
        }
        if(s.length() == 1){
            return s;
        }

        //Stack<ValCnt3> st = new Stack<>();
        Deque<ValCnt3> deque = new LinkedList<>();

        for(String x: s.split("")){
            // case 1) st is empty
            if(deque.isEmpty()){
                deque.add(new ValCnt3(x, 1));
            }else{
                // case 2) st is NOT empty && prev != cur
                if(!deque.peek().val.equals(x)){
                    deque.add(new ValCnt3(x, 1));
                }
                // case 3) st is NOT empty && prev == cur
                else{
                    // case 3-1) continuous val cnt + 1 < k
                    if(deque.peek().cnt + 1 < k){
                        // ????
                        ValCnt3 tmp = deque.poll();
                        tmp.val += 1;
                        deque.addLast(tmp);
                    }
                    // case 3-2) continuous val cnt + 1 >= k
                    else{
                        ValCnt3 tmp = deque.poll();
                        tmp.val += k;
                        if(tmp.cnt > 0){
                            deque.addLast(tmp);
                        }
                    }

                }
            }
        }

        StringBuilder sb = new StringBuilder();
        // ???
        for(ValCnt3 v: deque){
            sb.append(multiplyStr(v.val, v.cnt));
        }


        return sb.toString();
    }

    private String multiplyStr(String str, int cnt){
        String res = "";
        for(int i = 0; i < cnt; i++){
            res += str;
        }
        return res;
    }








    // 10.20 - 30 am
    public int removeDuplicates_99(int[] nums) {
        // edge
        if(nums == null || nums.length == 0){
            return 0;
        }
        if(nums.length == 1){
            return 1;
        }

        // 2 pointers (fast - slow ???)
        int l = 0;
        int r = 1;

        while(r < nums.length){
            // ???
            if(nums[r] == nums[l]){
                r += 1;
            }
            // ??? swap
            // and move left pointer  ???
            else{
                // note !!! WE MOVE left pointer first
                l += 1;
                /**  NOTE !!
                 *
                 * Overwriting vs. Swapping:
                 * You don't actually need to swap.
                 * Since the array is sorted,
                 * you just need to copy the next unique element to the position
                 * right after the current unique element.
                 *
                 */
                int tmp = nums[r];
                //nums[r] = l;
                //nums[l] = tmp;
                //l += 1;
                nums[l] = nums[r]; // ???
            }
        }


        // note !!! return `l+1` instead
        return l + 1;
        //return l;
    }


    // LC 27
    // 10.57 - 11.07 am
    /**
     *
     *  -------------------
     *
     *  IDEA 1) 2 POINTERS
     *    - left, right pointer ???
     *    - 0, nume.len - 1
     *
     *  -------------------
     *
     */
    // IDEA 1) 2 POINTERS
    public int removeElement(int[] nums, int val) {
        // edge
        if(nums == null || nums.length == 0){
            return 0;
        }
        if(nums.length == 1){
            return nums[0] == val ? 0: 1;
        }

        int n = nums.length;
        int l = 0;
        int r = n - 1;

        //  >= ????
        while(r >= l){

//            if(nums[r] != val){
//                r -= 1;
//            }
//
//            // ????
//            if(nums[l] != val){
//                l += 1;
//            }
            if(nums[l] == val){
                int tmp = nums[l];
                nums[l] = nums[r];
                nums[r] = tmp;
                // ???
                // since nums[l] == val, so we are sure the
                // swapped nums[r] MUST == val,
                // so we need to move right pointer (r -= 1)
                r -= 1;
            }

            //  ???
            // if(nums[l] != val)
            else{
                l += 1;
            }


        }

        return l + 1; // ???
    }









    // LC 113
    // 7.55 - 8.05 am
    /**
     *  -> return all root-to-leaf paths
     *  where the sum of the node values
     *  in the path equals targetSum.
     *
     *
     *  ---------------
     *
     *   IDEA 1) DFS (post traverse) + path sum + hashmap + backtrack ????
     *
     *
     *  ---------------
     *
     */
    // LC 113
    // 18.04 - 14 pm
    // IDEA 1) DFS (PRE ORDER traverse) + path sum + hashmap
    Map<Integer, List<List<Integer>>> pathMap3 = new HashMap<>();
    public List<List<Integer>> pathSum_95(TreeNode root, int targetSum) {
        // edge
        if(root == null){
            return new ArrayList<>();
        }

        getPathBySum(root, targetSum, new ArrayList<>());

        // edge
        if(pathMap3.isEmpty() || !pathMap3.containsKey(targetSum)){
            return new ArrayList<>();
        }

        return pathMap3.get(targetSum);
    }

    // ???
    private void getPathBySum(TreeNode root, int targetSum, List<Integer> list){
        // edge
        if(root == null){
            return;
        }

        // update cur sum
        //accSum += root.val;
        list.add(root.val);
        int accSum = getArrSum(list);

        // ??
        if(accSum == targetSum){
            if(root.left == null && root.right == null){
                if(!pathMap3.containsKey(accSum)){
                    pathMap3.put(accSum, new ArrayList<>());
                }
                List<List<Integer>> tmp = pathMap3.get(accSum);
                tmp.add(new ArrayList<>(list)); // ????
                pathMap3.put(targetSum, tmp);
                // ???
                //pathMap3.get(targetSum).add(list);
            }
        }

        getPathBySum(root.left, targetSum, list);
        getPathBySum(root.right, targetSum, list);

        // undo ??? backtrack
        list.remove(list.size() - 1);

    }

    private int getArrSum(List<Integer> list){
        int res = 0;
        for(int x: list){
            res += x;
        }
        return res;
    }











    // IDEA 1) DFS (post traverse) + path sum + hashmap + backtrack ????
    //Map<I>
    List<List<Integer>> nodeList = new ArrayList<>();
    public List<List<Integer>> pathSum_90(TreeNode root, int targetSum) {
        // edge
        if(root == null){
            return nodeList;
        }

        pathSumHelper(root, targetSum, 0, new ArrayList<>());

        return nodeList;
    }

    // ???
    private void pathSumHelper(TreeNode root, int targetSum, int curSum, List<Integer> cache){
        System.out.println(">>> root = " + root.val
        + ", cursum =" + curSum
        + ", cache = " + cache);

        // edge
        if(root == null){
            return;
        }
        // ????
        if(curSum > targetSum){
            // ????
            return;
        }

        // post order traverse
        pathSumHelper(root.left, targetSum, curSum, cache);
        pathSumHelper(root.right, targetSum, curSum, cache);

        // ???
        curSum += root.val;
        // ???
//        deque.add(root.val);
//        List<Integer> test = new ArrayList<>();
        cache.add(root.val);

        if(curSum == targetSum){
            // ????
            nodeList.add(new ArrayList<>(cache));
        }


        // backtrack ??? undo
        cache.remove(cache.size() - 1);
        curSum -= root.val;
    }



    // LC 1544
    // 7.07 m- 17 am
    /**
     * -> Return the string after making it good.
     * The answer is guaranteed to be unique
     * under the given constraints.
     *
     * Notice that an empty string is also good.
     *
     *
     * ---------------
     *
     *  IDEA 1) STACK
     *
     *
     * ---------------
     *
     *
     */
    public String makeGood(String s) {
        // edge
        if(s == null || s.isEmpty() || s.length() == 1){
            return s;
        }

        Stack<String> st = new Stack<>();
        for(char ch: s.toCharArray()){
            // ????
            String str = String.valueOf(ch);
            if(!st.isEmpty() && ( st.peek().toLowerCase().equals(str) || st.peek().toUpperCase().equals(str) )){
                // ???
                if(!st.peek().equals(str)){
                    st.pop();
                }
            }else{
                st.add(str);
            }
        }

        StringBuilder sb = new StringBuilder();
        for(String x: st){
            sb.append(x);
        }

        return sb.toString();
    }



    // LC 108
    // 7.28 - 38 am
    /**
     *
     *  ->  convert it to a height-balanced binary search tree.
     *
     *  NOTE:
     *   - nums: where the elements are sorted in ascending order,
     *
     *   - A height-balanced binary tree is a
     *    binary tree in which the depth of
     *   the two subtrees of every node
     *   never differs by more than one.
     *
     *
     *  ---------------
     *
     *   IDEA 1) DFS (inorder traverse ????)
     *
     *     BST:  left < root < right
     *      -> so if visit in `in-order traverse`,
     *         the value is an ascending arr (small-> big)
     *
     *  ---------------
     */
    public TreeNode sortedArrayToBST(int[] nums) {
        // edge
        if(nums == null || nums.length == 0){
            return null;
        }
        if(nums.length == 1){
            return new TreeNode(nums[0]);
        }

        // ????
        List<Integer> list = new ArrayList<>();
        for(int x: nums){
            list.add(x);
        }

        return buildBST(list);
    }

    private TreeNode buildBST(List<Integer> list){
        // edge
        if(list == null || list.size() == 0){
            return null;
        }

        // ????
      //  TreeNode _left = buildBST(nums)
        TreeNode root = new TreeNode(list.get(0));
        int rootIdx = list.indexOf(root.val); // ??????
        // /???
        TreeNode _left = buildBST(list.subList(rootIdx + 1, list.size() -1));
        TreeNode _right = buildBST(list.subList(rootIdx + 1, list.size() -1));

        // ???
        root.left = _left;
        root.right = _right;


        return root;
    }


    // LC 938
    // 7.58 - 8.08 AM
    /**
     *
     *
     */
    int nodeSum = 0;
    public int rangeSumBST(TreeNode root, int low, int high) {
        // edge
        if(root == null){
            return 0;
        }
        if(root.val <= high && low <= root.val){
            nodeSum += root.val;
        }
        rangeSumBST(root.left, low, high);
        rangeSumBST(root.right, low, high);

        return nodeSum;
    }



    // LC 109
    // 8.11 - 21 am
    /**
     *  -> convert it to a height-balanced binary search tree.
     *
     *
     *  ----------
     *
     *  IDEA 1)  DFS + MID INDEX + LINKED LIST OP
     */
    // IDEA 1)  DFS + MID INDEX + LINKED LIST OP
    public TreeNode sortedListToBST(ListNode head) {
        // edge
        if(head == null){
            return null;
        }

        return buildBSTFromLinkedList(head, 0, getNodeLen(head));
    }

    private TreeNode buildBSTFromLinkedList(ListNode head, int left, int right){
        // edge
        if(head == null){
            return null;
        }
        if(right < left){
            return null;
        }

        // /????
        int mid = left + (right - left) / 2;
        ListNode midNode = getNodeByIdx(head, mid);
        if(midNode == null){
            return null;
        }

        TreeNode root = new TreeNode(midNode.val);

        // ??? pre-order traverse ???
        TreeNode _left = buildBSTFromLinkedList(head, left, mid);
        TreeNode _right = buildBSTFromLinkedList(head, mid + 1, right);

        // ????
        root.left = _left;
        root.right = _right;


        return root;
    }


    private ListNode getNodeByIdx(ListNode head, int idx){
        // edge
        while (head != null){
            head = head.next;
            idx -= 1;
            if(idx == 0){
                return head;
            }
        }
        return null;
    }

    private int getNodeLen(ListNode head){
        int len = 0;
        while(head != null){
            head = head.next;
            len += 1;
        }
        return len;
    }


    // LC 112
    // 11.02 - 12 am
    /**
     *  -> return true if the tree has a root-to-leaf path
     *  such that adding up all the values
     *  along the path equals targetSum.
     *
     *
     *  ------------------
     *
     *   IDEA 1) DFS + POST ORDER ??? + BACKTRACK
     *
     *
     *   ------------------
     *
     */
    int pathSum = 0; /// /?????
    // NOTE !!! should use `pre-order` for path sum LC problems
    public boolean hasPathSum(TreeNode root, int targetSum) {
        // edge
        // ????
        if(root == null){
            //return targetSum == 0 ? true : false;
            return false;
        }

        ///  ???
        // ???
        pathSum += root.val;
        if(root.left == null && root.right == null){
            if(pathSum == targetSum){
                return true;
            }
        }

        hasPathSum(root.left, targetSum);
        hasPathSum(root.right, targetSum);

        // backtrack ??? undo ???
        pathSum -= root.val;

        return false;
    }







    // IDEA 1) DFS + POST ORDER ??? + BACKTRACK
    //boolean isFound = fa
    public boolean hasPathSum_99(TreeNode root, int targetSum) {
        // edge
        if(root == null){
            //return targetSum == 0 ? true : false;
            return targetSum == 0;
        }

        return checkPathSum(root, targetSum, 0);
    }

    private boolean checkPathSum(TreeNode root, int targetSum, int curSum){
        if(root == null){
            return false; // ???
        }

        checkPathSum(root.left, targetSum, curSum);
        checkPathSum(root.right, targetSum, curSum);

        // ???
        curSum += root.val;
        if(root.left == null && root.right == null){
            if(curSum == targetSum){
                return true;
            }
        }

        // undo ??? backtrack


        return false;
    }


    // LC 1405
    // 7,10 - 20 am
    /**
     *  -> return the longest possible happy string.
     *  If there are multiple longest happy strings,
     *  return any of them.
     *     - If there is no such string,
     *        - return the empty string "".
     *
     *  ------------------
     *
     *   IDEA 1) BRUTE FORCE
     *
     *   IDEA 2) PQ ????
     *
     *     -> hashmap : { val : cnt }
     *
     *     PQ: { [val, cnt ]  }   // ????
     *     // sort on cnt ( big -> small )
     *
     *     put `aa`, `bb` ... at once if possible
     *
     *
     *  ------------------
     *
     *
     */
    class valCnt{
        char val;
        int cnt;

        valCnt(char val, int cnt){
            this.val = val;
            this.cnt = cnt;
        }
    }

    public String longestDiverseString_99(int a, int b, int c) {
        // edge

        /**
         *
         *    *     PQ: { [val, cnt ]  }   // ????
         *      *     // sort on cnt ( big -> small )
         */
        PriorityQueue<Integer[]> pq = new PriorityQueue<>(new Comparator<Integer[]>() {
            @Override
            public int compare(Integer[] o1, Integer[] o2) {
                int diff = o2[1] - o1[1];
                return diff;
            }
        });

        PriorityQueue<int[]> pq2 = new PriorityQueue<>((o1, o2) -> o2[1] - o1[1]);

        // ????
        if (a > 0) pq2.add(new int[]{'a', a});
        if (b > 0) pq2.add(new int[]{'b', b});
        if (c > 0) pq2.add(new int[]{'c', c});

        // ???
        pq.add(new Integer[]{a, a});
        pq.add(new Integer[]{b, b});
        pq.add(new Integer[]{c, c});

        // ???
        StringBuilder sb = new StringBuilder();

        // ??
        int prev = -1;

        // ???
        boolean canProceed = true;

        // ????
        while(!pq.isEmpty() && canProceed){
            // ???
            Integer[] item = pq.poll();
            // ?? to avoid `poll` same element again
            if(item[0] == prev){
                Integer[] item2 = pq.poll();
                pq.add(item);
                item = item2;
            }

            int val = item[0];
            int cnt = item[1];
            if(cnt >= 2){
                sb.append(val);
                sb.append(val);

                if(cnt - 2 > 0){
                    pq.add(new Integer[]{val, cnt - 2});
                }

            }else if (cnt == 1){
                sb.append(val);
            }
        }

        // ???

        return pq.isEmpty() ? sb.toString() : "";
    }



    // LC 767
    // 7.55 - 8.10 am
    class ValCnt2{
        String val;
        int cnt;

        ValCnt2(String val, int cnt){
            this.val = val;
            this.cnt = cnt;
        }
    }

    public String reorganizeString(String s) {
        // edge
//        if(s.isEmpty() || s.length() == 0){
//            return s;
//        }

        if (s == null || s.length() == 0) {
            return s;
        }



        Map<String, Integer> map = new HashMap<>();
        for(String x: s.split("")){
            map.put(x, map.getOrDefault(x, 0) + 1);
        }

        // bit PQ: sort on cnt (big -> small)
        PriorityQueue<ValCnt2> pq  = new PriorityQueue<>(new Comparator<ValCnt2>() {
            @Override
            public int compare(ValCnt2 o1, ValCnt2 o2) {
                int diff = o2.cnt - o1.cnt;
                return diff;
            }
        });

        // ??
        for(String val: map.keySet()){
            pq.add(new ValCnt2(val, map.get(val)));
        }

        //StringBuilder sb = new StringBuilder();
        String res = ""; //????

        // ???
        while(!pq.isEmpty()){
//            // case 0) res is ""
//            if(res.equals("")){
//
//            }
            // case 1) if cur val != prev val
            // if (res.length() == 0 || pq.peek().val != res.charAt(res.length() - 1))
            if(res.equals("") ||  !pq.peek().val.equals(res.charAt(res.length()-1))){
                ValCnt2 valCnt2 = pq.poll();
                // ??
                int newCnt = valCnt2.cnt -= 1;
                res += valCnt2.val;
                if(newCnt > 0){
                    // ???
                    // can we deduct the attr directly ?
                    pq.add(new ValCnt2(valCnt2.val, newCnt ));
                }
            }
            // case 2)  if cur val == prev val
            else{

                ValCnt2 valCnt2 = pq.poll();

                // NOTE !!! if NOT possible to form a valid str
                // break the while loop directly
                if(pq.isEmpty()){
                    break;
                }

                ValCnt2 valCnt2_2 = pq.poll();

                int newCnt = valCnt2_2.cnt -= 1;
                res += valCnt2_2.val;
                if(newCnt > 0){
                    // ???
                    // can we deduct the attr directly ?
                    pq.add(new ValCnt2(valCnt2_2.val, newCnt ));
                }

                pq.add(valCnt2);

            }
        }



        return res.length() == s.length() ? res : "";
    }


    // LC 251
    // 10.47 - 57 AM
    /**
     *
     * -> Design and implement an iterator to flatten a 2d vector.
     * It should support the following operations: next and hasNext.
     *
     * ------------------
     *
     *   IDEA 1) RECURSION ??
     *
     *   IDEA 2) ARRAY OP ???
     *
     *
     * ------------------
     *
     */
    // IDEA 2) ARRAY OP ???
    class Vector2D_99{

        // attr
        List<Integer> list;
        int[][] twoDArr;
        int idx;


        public Vector2D_99(int[][] vec) {
            this.list = new ArrayList<>();
            this.twoDArr = vec;
            this.idx = 0;

            // ???
            for(int[] arr: this.twoDArr){
                for(int x: arr){
                    this.list.add(x);
                }
            }
        }

        public int next() {
            // edge
            if(this.idx >= this.list.size()){
                return -1; // ??
            }
            idx += 1;
            return this.list.get(idx - 1);
        }

        public boolean hasNext() {
            return this.idx >= this.list.size();
        }

//        private void forward() {
//        }


    }


    // LC 404
    // 7.18 - 28 am
    /**
     *  ->
     *  Given the root of a binary tree,
     *  return the sum of all `left` leaves.
     *
     *   NOTE:
     *     1.  A leaf is a node with no children
     *
     *     2. A left leaf is a leaf that is the
     *        left child of another node.
     *
     *
     *  --------
     *
     *  IDEA 1) DFS ????
     *
     *
     *  IDEA 2) BFS ????
     *
     *
     */
    //  IDEA 1) DFS ???? + pre-order traverse
    // ???
    int leftLeavesSum = 0;
    public int sumOfLeftLeaves(TreeNode root) {
        // edge
        if(root == null){
            return 0;
        }

//        // pre-order !!!
//        if(root.left == null && root.right == null){
//          // return root.val; // ????
//            leftLeavesSum += root.val;
//        }

      //  sumOfLeftLeaves(root.left);
        // ????
        getLeftNodesHelper(root, "");

        // ??????
        return leftLeavesSum;
    }

    // ???
    private void getLeftNodesHelper(TreeNode root, String preOp){
        if(root == null){
            return; //0;
        }

        // pre-order !!!
        if(root.left == null && root.right == null && preOp.equals("left")){
            // return root.val; // ????
            leftLeavesSum += root.val;
        }

        getLeftNodesHelper(root.left, "left");
        getLeftNodesHelper(root.right, "right");
    }


    // LC 1634
    // 7.39 - 49 am
    /**
     *
     *  -> Given a sorted integer array nums,
     *  where the range of elements are in the inclusive
     *  range [lower, upper],
     *
     *    -> return its missing ranges.
     *
     *  -----------------------
     *
     *   IDEA 1) INTERVAL OP + 2 pointers ??
     *
     *
     *  -----------------------
     *
     */
    // IDEA 1) INTERVAL OP + 2 pointers ??
    public List<List<Integer>> findMissingRanges(int[] nums, int lower, int upper) {
        List<List<Integer>> res = new ArrayList<>();
        // edge
        if(nums == null || nums.length == 0){
            return res;
        }
        if(lower > upper){
            return res;
        }

       // int prev = lower - 1; // ????

        List<Integer> list = new ArrayList<>();
        list.add(lower);
        for(int x: nums){
            list.add(x);
        }
        list.add(upper);

        // add lower, upper to arr
        for(int i = 1; i < list.size(); i++){
            int diff = list.get(i) - list.get(i-1);
           if(diff != 1){
               if(diff == 2){
                   //list.add(diff); // ???
                   res.add(new ArrayList<>(diff));
               }else{
                   int start = list.get(i-1) + 1;
                   int end = list.get(i) - 1;
                   // ???
                   //list.add(Integer.valueOf(start + "->" + end));
                   res.add(new ArrayList<>(
                           Integer.valueOf(start + "->" + end)));
               }
           }
        }



        return res;
    }


    // LC 1150
    // 8.11 - 21 am
    /**
     *
     *  IDEA 1) HASHMAP
     *
     *  IDEA 2) BINARY SEARCH
     *
     */
    public boolean isMajorityElement(int[] nums, int target) {
        // edge
        if(nums == null){
            return false;
        }
        if(nums.length == 1){
            return nums[0] == target;
        }

        // map: { val : cnt }
        Map<Integer, Integer> map = new HashMap<>();
        for(int x: nums){
            map.put(x, map.getOrDefault(x, 0) + 1);
        }

        // edge
        if(!map.containsKey(target)){
            return false;
        }

        return map.get(target) > nums.length / 2;
    }



    // LC 214
    // 10.08 - 18 am
    /**
     *
     *  -> Return the shortest palindrome
     *  you can find by performing this transformation.
     *
     *   NOTE:
     *
     *    You can convert s to a
     *    palindrome by adding characters in front of it.
     *
     *
     *  -----------------
     *
     *   IDEA 1) BRUTE FORCE ???
     *
     *   IDEA 2) 2 POINTERS ???
     *     l, r
     *
     *
     *
     *  -----------------
     *
     *  ex 1)
     *
     *   Input: s = "aacecaaa"
     *   Output: "aaacecaaa"
     *
     *    "aacecaaa"
     *     l      r
     *
     *    "aacecaaa"
     *      l    r
     *
     *    "aacecaaa"
     *       l  r       c != a, so add `a` at left
     *
     *     "aaacecaaa"
     *        l   r
     *
     *     "aaacecaaa"
     *         l r
     *
     *
     *   ex 2)
     *
     *   Input: s = "abcd"
     *   Output: "dcbabcd"
     *
     *
     *    "abcd"
     *     l  r         a != d,  so add a
     *
     *    "dabcd"
     *     l   r
     *
     *    "dabcd"
     *      l r        a != c, so add c
     *
     *    "dcabcd"
     *      l  r
     *
     *     "dcabcd"
     *        lr          a != b, so add b
     *
     *     "dcbabcd"
     *        s l
     *
     *
     */
    public String shortestPalindrome(String s) {

        return null;
    }




    //  IDEA 2) 2 POINTERS ??? (l, r)
    public String shortestPalindrome_99(String s) {
        // edge
        if(s.isEmpty() || s.length() <= 1){
            return s;
        }
        if(isPalindrome(s)){
            return s;
        }

        // ????
        int l = 0;
        int r = s.length() - 1;

        // ???
        //char[] chars = s.toCharArray();
        //char z = chars[2];

        while (r > l){

            if(isPalindrome(s)){
                return s;
            }
//            char left = chars[l]; //s.charAt(l);
//            char right = chars[r]; //s.charAt(r);
            char left = s.charAt(l);
            char right = s.charAt(r);

            // case 1) equals
            if(left == right){
                l += 1;
                r -= 1;
            }
            // case 2) NOT equals
            else{
                // NOTE !!! `insert` new element to str
                //  via substring
                // ???
                s = s.substring(0, l-1) + right + s.substring(l, r);
            }
        }

        return s;
    }



    /**  NOTE !!
     *
     * You check the whole string every iteration,
     * but the algorithm should check prefix palindromes,
     * not the entire string repeatedly.
     *
     *
     */
    private boolean isPalindrome(String s){
        if(s.isEmpty() || s.length() <= 1){
            return true;
        }
        int l = 0;
        int r = s.length() - 1;
        while (r > l){
            if(s.charAt(l) != s.charAt(r)){
                return false;
            }
            l += 1;
            r -= 1;
        }
        return true;
    }



    // LC 1288
    // 11.35 - 45 am
    /**
     *
     *  -> Return the number of remaining intervals.
     *
     *    remove all intervals that are
     *    covered by another interval in the list.
     *
     *
     *  ------------------
     *
     *   IDEA 1) SORT + INTERVAL OP
     *
     *
     *  ------------------
     *
     */
    // IDEA 1) SORT + INTERVAL OP
    public int removeCoveredIntervals(int[][] intervals) {
        // edge
        if(intervals == null || intervals.length == 0){
            return 0;
        }
        if(intervals.length == 1){
            return 1;
        }

        // sort
        // 1. start (small -> big)
        // 2. end (big -> small) // ?????
        /**  NOTE !!!
         *  if we sort on `start (small -> big)`,
         *  there will ONLY be below cases:
         *
         *   1.   |----|             old
         *                |----|     new
         *
         *   2.   |----------|       old
         *          |----|           new
         *
         *   3.  |----|                 old
         *         |------|              new
         *
         */
        // so, we ONLY need to remove an interval
        // when case 2)
        List<int[]> list = new ArrayList<>();
        for(int[] x: intervals){
            list.add(x);
        }

        // ???
        Collections.sort(list, new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
//                int diff = o1[0] - o2[0];
//                return diff;
                return  o1[0] - o2[0] != 0 ?  o1[0] - o2[0] :  o2[1] - o1[1];
            }
        });

        List<int[]> cache = new ArrayList<>();
        for(int[] x: list){
            if(cache.isEmpty()){
                cache.add(x);
            }else{
                int[] prev = cache.get(cache.size() - 1);
                // case 1) `new` is included in `old`
                if(prev[0] <= x[0] && prev[1] >= x[1]){
                    continue;
                }
                // case 2) `new` is NOT included in `old`
                else{
                    cache.add(x);
                }
            }
        }


        return cache.size();
    }






    // LC 179
    public String largestNumber(int[] nums) {

        return null;
    }


    // LC 14
    // 11.19 - 29 am
    /**
     * IDEA 1) trie
     *
     * IDEA 2) prefix + string op
     *
     *
     */
    // IDEA 2) sort ??? + prefix + string op
    public String longestCommonPrefix(String[] strs) {
        // edge
        if (strs == null || strs.length == 0) {
            return "";
        }
        if (strs.length == 1) {
            return strs[0];
        }

        //List<String>
        // sort: len (small -> big) // ???
        Arrays.sort(strs, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                int diff = o1.length() - o2.length();
                return diff;
            }
        });

        String res = "";
        //String prefix = strs[0]; // ??
        List<String> candidates = new ArrayList<>();
        StringBuilder prefix = new StringBuilder();
        for(String x: strs[0].split("")){
            prefix.append(x);
            candidates.add(prefix.toString());
            //prefix.append(x);
        }


        // sort
        Collections.sort(candidates, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                int diff = o2.length() - o1.length();
                return diff;
            }
        });

        System.out.println(">>> candidates = " + candidates);

        for(String word: candidates){
            boolean shouldSkip = false;
            for(int i = 1; i < strs.length; i++){
                String cur = strs[i];
                if(!cur.startsWith(word)){
                    shouldSkip = true;
                    break; // ???
                }
            }
            if(!shouldSkip && word.length() > res.length()){
                res = word;
            }
        }


        return res;
    }



    // LC 984
    // 12.01 - 11 pm
    /**
     *
     *  -> Given two integers a and b,
     *  return any string s
     *    - such that:
     *
     *
     *  --------------
     *
     *  IDEA 1) GREEDY
     *
     *  IDEA 2) BRUTE FORCE ???
     *
     *  IDEA 3) 2 POINTERS ??
     *
     *  --------------
     *
     */

    // 11.06 - 16 am
    //  IDEA 1) GREEDY
    //    boolean canProceedA, canProceedB
    //    while loop, append str per above flag
    public String strWithout3a3b(int a, int b) {
        StringBuilder sb = new StringBuilder();

        while (a > 0 || b > 0) {
            boolean writeA = false;
            int n = sb.length();

            // Rule 1: If we have 'bb' at the end, we MUST write 'a'
            if (n >= 2 && sb.charAt(n - 1) == 'b' && sb.charAt(n - 2) == 'b') {
                writeA = true;
            }
            // Rule 2: If we have 'aa' at the end, we MUST write 'b' (so writeA = false)
            else {
                writeA = false;
            }
            // Rule 3: Otherwise, just write the one that has more left
//            else {
//                writeA = a >= b;
//            }

            if (writeA) {
                sb.append('a');
                a--;
            } else {
                sb.append('b');
                b--;
            }
        }

        return sb.toString();
    }



//    public String strWithout3a3b(int a, int b) {
//        // edge
//        // edge
//        if(a == 0 && b == 0){
//            return "";
//        }
//
//        StringBuilder sb = new StringBuilder();
//        // ??
//        boolean canProceedA = true;
//        boolean canProceedB = true;
//
//        while(a > 0 || b > 0){
//
//            System.out.println(">>> a = " + a
//            + " canProceedA = " + canProceedA
//            + ", b = " + b
//             + " canProceedB = " + canProceedB
//            + " sb = " + sb.toString());
//
//            if(a > 0 && canProceedA){
//                sb.append("a");
//                // ??? reset canProceedB
//               // canProceedB = true;
//                a -= 1;
//                // ???
//                int size = sb.length();
//                if(size >= 2){
//                    // ??? better way ???
//                    String str = sb.toString();
//                    if(str.charAt(str.length() - 1) == 'a' &&
//                            str.charAt(str.length() - 2) == 'a'){
//                        canProceedA = false;
//                        canProceedB = true;
//                    }
//                }else{
//                    canProceedB = false; // ??
//                }
//            }
//
//            if(b > 0 && canProceedB){
//                sb.append("b");
//                // ??? reset canProceedA
//               // canProceedA = true;
//                b -= 1;
//                // ???
//                int size = sb.length();
//                if(size >= 2){
//                    // ??? better way ???
//                    String str = sb.toString();
//                    if(str.charAt(str.length() - 1) == 'b' &&
//                            str.charAt(str.length() - 2) == 'b'){
//                        canProceedB = false;
//                        canProceedA = true;
//                    }
//                }
//                else{
//                    canProceedA = false; // ??
//                }
//            }
//
//        }
//
//        return sb.toString();
//    }
//







    public String strWithout3a3b_99(int a, int b) {
        // edge
        if(a < 3 && b < 3){
            String res = multipleStr2("a", a) + multipleStr2("b", b);
            System.out.println(">>> res = " + res);
           return res;
        }

        int continueA = 0;
        int continueB = 0;

        String res = "";

        while(a > 0 || b > 0){
            while(a > 0 && continueA <= 1){
                // ??
//                if(continueA <= 1){
//                    res += "a";
//                    continueA += 1;
//                    a -= 1;
//                }
                res += "a";
                continueA += 1;
                a -= 1;
                // reset
                continueB = 0;
            }
            // ???
            while(b > 0 && continueB <= 1) {
//                if(continueB <= 1){
//                    res += "b";
//                    continueB += 1;
//                    b -= 1;
//                }
                res += "b";
                continueB += 1;
                b -= 1;
                // reset
                continueA = 0;
            }

        }

        return res;
    }



    private String multipleStr2(String x, int times){
        String res = "";
        for(int i = 0; i < times; i++){
            res += x;
        }
        return res;
    }


    // LC 1314
    // 12.49 - 59 pm
    /**
     *
     *  -> return a matrix answer
     *    where each answer[i][j] is the sum
     *    of all elements mat[r][c]
     *
     *      - i - k <= r <= i + k,
     *      - j - k <= c <= j + k, and
     *      - (r, c) is a `valid` position
     *         in the matrix.
     *
     *
     *    - m x n matrix
     *    - int k
     *
     *  ----------------
     *
     *   IDEA 1) BRUTE FORCE (matrix op)
     *
     *   IDEA 2)
     *
     *
     *  ----------------
     *
     *  ex 1)
     *
     *  Input: mat = [
     *    [1,2,3],
     *    [4,5,6],
     *    [7,8,9]
     *    ],
     *
     *    k = 1
     *
     *
     *  Output: [
     *   [12,21,16],
     *   [27,45,33],
     *   [24,39,28]
     *  ]
     *
     *
     *
     */
    public int[][] matrixBlockSum(int[][] mat, int k) {

        return null;
    }


    // LC 2614
    // 6.59 - 7.09 am
    /**
     *  IDEA 1) DIAGONAL OP + MATH (prime num)
     *
     * -------
     *
     *  ex 1)
     *
     *   Input: nums = [[1,2,3],[5,6,7],[9,10,11]]
     *   Output: 11
     *
     *   [
     *   [1,2,3],
     *   [5,6,7],
     *   [9,10,11]
     *   ]
     *
     *
     *   -> 2nd diagonal
     *      [3,6,9]
     *
     *      [0,2], [1,1], [2,0]
     *
     *      y = 0      y =  1      y =2
     *      x = 3-0-1  x = 3-1-1   x=3-2-1
     *
     */
    // IDEA 1) DIAGONAL OP + MATH (prime num)
    public int diagonalPrime(int[][] nums) {
        // edge

        int l = nums.length;
        int w = nums[0].length;

        int res = -1; // ???

        // prime diagonal (x, y), x==y
        for(int y = 0; y < l; y++){
            // ???
            int val = nums[y][y];
            if(isPrime(val)){
                res = Math.max(res, val);
            }
        }

        // 2nd diagonal  (x, y), y = l - x - 1 // ???
        for(int x = 0; x < w; x++){
            // ???
            int val = nums[l - x - 1][x];
            if(isPrime(val)){
                res = Math.max(res, val);
            }
        }

        return res;
    }

    private boolean isPrime(int x){
        // edge
        if(x <= 1){
            return false;
        }
        int val = (int) Math.sqrt(x);
        // ???
        // NOTE !!!  <=
        for(int i = 2; i <= val; i++){
            if(x % i == 0){
                return false;
            }
        }

        return true;
    }


    // LC 2615
    // 7.24 - 34 am
    /**
     *  ->
     *  Return the array arr.
     *
     *
     *  -------------------
     *
     *
     *  IDEA 1) PREFIX SUM + 2 HASH MAP ????
     *
     *   { val: [idx_1, idx_2, ... ] }
     *
     *   given idx_x = idx_2,
     *   what's the sum of |idx_1 - idx_x| + |idx_3 - idx_x | ... ?
     *
     *   -> 2 groups:
     *     - smaller than idx_x
     *         ( idx_x - idx_1 ) + (idx_x - idx_0)
     *
     *
     *     - bigger than idx_x
     *        (idx_3 - idx_x) + (idx_4 - idx_x)
     *
     *
     *     ->  (idx_3 - idx_x) + (idx_4 - idx_x) + ( idx_x - idx_1 ) + (idx_x - idx_0)
     *
     *      = (idx_3 + idx_4) - (idx_1 + idx_0 ) + (m - n) * idx_x
     *             N                 M
     *
     *
     *  IDEA 1) LEFT, RIGHT SUM  (PASS) + 2 HASH MAP ????
     *
     *
     *       *
     *      * ### Left pass
     *      *
     *      * ```
     *      * (i - j1) + (i - j2) + ...
     *      * = count * i - sum(indices)
     *      * ```
     *      *
     *      * ### Right pass
     *      *
     *      * ```
     *      * (j1 - i) + (j2 - i) + ...
     *      * = sum(indices) - count * i
     *      * ```
     *      *
     *      * Add both → final distance.
     *      *
     *
     *
     *  ->
     *   map1 :  ( val : index_list )
     *      { val : [idx_1, idx_2, .... ] }
     *
     *
     *   map2 :  ( val : [left_idx_cnt, right_idx_2] )  // ???
     *
     *  -------------------
     *
     *
     */
    public long[] distance(int[] nums) {
        // edge

        // ??
        /**
         *      *   map1 :  ( val : index_list )
         *      *      { val : [idx_1, idx_2, .... ] }
         *      *
         *      *
         *      *   map2 :  ( val : [left_idx_cnt, right_idx_2] )  // ???
         *      *
         */
        Map<Integer, List<Integer>> map1 = new HashMap<>();
        Map<Integer, Integer[]> map2 = new HashMap<>();



       // NOTE ??????
        Map<Integer, Long> count = new HashMap<>();
        Map<Integer, Long> sum = new HashMap<>();

        long[] res = new long[nums.length];

        for(int i = 0; i < nums.length; i++){
            int val = nums[i];
            if(!map1.containsKey(val)){
                map1.put(val, new ArrayList<>());
            }
            List<Integer> list = map1.get(val);
            list.add(i);
            map1.put(val, list);
        }

        for(int i = 0; i < nums.length; i++){
            int val = nums[i];

            List<Integer> list = map1.get(val);
            int idxInList = list.indexOf(val); // ????
            int left = idxInList;
            int right = list.size() - left;

            // ????
            map2.put(val, new Integer[]{left, right});

            // left pass
            // = count * i - sum(indices)
            int leftPass = left * i - val * list.size();

            // right pass
            // = sum(indices) - count * i
            int rightPass = val * list.size() - right * i;



            // sum over above
            res[i] = leftPass + rightPass;
        }


        return res;
    }



    // LC 2616
    // 10.16 - 31 am
    /**
     *   IDEA 1) BINARY SEARCH ??? + SORTING + GREEDY ????
     *
     *
     *
     *
     *   ------------------
     *
     *   ex 1)
     *
     *   Input: nums = [10,1,2,7,1,3], p = 2
     *   Output: 1
     *
     *   -> sorting:
     *      [1,1,2,3,7,10]
     *
     *      -> binary search find the next bigger val ???
     *
     *
     *
     *
     *
     *   ------------------
     *
     *
     */
    public int minimizeMax(int[] nums, int p) {
        // edge

        // sort ??? (small -> big)
        Arrays.sort(nums);

        // binary search ???
//        int l = 0;
//        int r = nums.length - 1; // ???

        int n = nums.length;


        // The 'answer' (max difference) must be between 0 and the max spread of the array
        int l = 0;
        int r = nums[n - 1] - nums[0]; // NOTE !!! the `biggest diff`


        //int res = 0; // ????
        int res = r; // ????

        while (r >= l){
            // mid is the `boundary` ???
            // could be left or right ???
            // depends on we shrink
            // left or right boundary ????
            int mid = l + ( r - l ) / 2;
            if(canFormPairs(nums, p, mid)){
                // note !!! NOT stop here,
                // but try to find if there is
                // a `smaller` solution
                res = mid; //???
                r = mid - 1;
            }else{
                l = mid + 1;
            }
        }

        return res;
    }


    // ???
    // x is the `boundary` ???
    // could be left or right ???
    // depends on we shrink
    // left or right boundary ????
    private boolean canFormPairs(int[] nums, int p, int maxDiff){
        // ??? edge

        int cnt = 0;
        // greedy
        for(int i = 0; i < nums.length - 1; i++){
            if(nums[i+1] - nums[i] <= maxDiff){
                cnt += 1;
                i += 1;
            }
            // early quit
            if(cnt >= p){
                return true;
            }
        }
//        for(int i = 0; i < p; i++){
//
//        }

        return cnt >= p;
    }




    // LC 623
    // 18.07 - 33 pm
    /**
     *  DFS DEPTH
     *  -> pre order ?????
     *
     *  (root -> left -> right)
     *
     */
    public TreeNode addOneRow(TreeNode root, int val, int depth) {
        // edge ??
        if(root == null){
            return root;
        }

        // pre-order
        // ???

        TreeNode _left = null;
        TreeNode _right = null;
        if(depth == 0){
            //root.
//            TreeNode _left = new TreeNode(val);
//            TreeNode _right = new TreeNode(val);
            _left = new TreeNode(val);
            _right = new TreeNode(val);

            // /?? re-connect

            root.left = _left;
            root.right = _right;

            _left.left = addOneRow(root.left, val, depth);
            _right.right = addOneRow(root.right, val, depth);
        }
        // ????
        else{
            root.left = addOneRow(root.left, val, depth);
            root.right = addOneRow(root.right, val, depth);
        }


        // ???
        return root;
    }




//    private TreeNode dfsNodeHelper(){
//
//    }


    // LC 303
    // 7.14 - 24 AM
    /**
     *  IDEA 1) PREFIX SUM
     *
     *  IDEA 2) DOUBLE LOOP
     *
     *
     *  -------------
     *
     *  ex 1)
     *
     *  [-2, 0, 3, -5, 2, -1]
     *
     *  -> prefix =
     *    [-2, -2, 1, -4, -2, -3]
     *
     *
     *    -> sum[left, right]
     *      = prefix[right] - prefix[left - 1] ????
     *
     *    sum[0,2] = 1
     *
     *    [a0, a1, a2]
     *
     *    [a0, a0+a1, a0+a1+a2, a0+a1+a2]
     *
     *    ->
     *
     *
     *  ---------
     *
     *  - **Core Idea**: `prefixSum[i] = nums[0] + nums[1] + ... + nums[i-1]`
     * - **Subarray Sum**: `sum(i, j) = prefixSum[j+1] - prefixSum[i]`
     *
     *
     *
     *
     *
     *
     */
    //  IDEA 1) PREFIX SUM
    class NumArray {
        int[] prefix;

        public NumArray(int[] nums) {
            int n = nums.length;
            this.prefix = new int[n+1];
            // init prefix arr
            int cumSum = 0;
            for(int i = 0; i < n; i++){
                cumSum += nums[i];
                this.prefix[n+1] = cumSum;
            }
        }

        public int sumRange(int left, int right) {
            if(left > right){
                return -1; // ??
            }
            return this.prefix[right + 1] - this.prefix[left];
        }

    }


    // LC 58
    public int lengthOfLastWord(String s) {

        return 0;
    }




}
