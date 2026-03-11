package ws;

import LeetCodeJava.DataStructure.ListNode;
import LeetCodeJava.DataStructure.TreeNode;

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
    // IDEA 1) DFS + POST order traverse
    List<TreeNode> res = new ArrayList<>();
    // ???
    // { path : cnt }
    // /??
    // v2: { path: [node_1, node_2 ,.. ] }
    Map<String, List<TreeNode>> pathMap = new HashMap<>();
    public List<TreeNode> findDuplicateSubtrees(TreeNode root) {
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
    // 10.20 - 30 am
    public int removeDuplicates(int[] nums) {
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
    public List<List<Integer>> pathSum(TreeNode root, int targetSum) {
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
//    class valCnt{
//        String
//    }
    public String longestDiverseString(int a, int b, int c) {
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





}
