package ws;

import LeetCodeJava.DataStructure.TreeNode;
//import com.sun.org.apache.bcel.internal.generic.SIPUSH;

import java.util.*;

public class Workspace24 {

    // LC 953
    // 7.22 - 32 am
    /**
     *  ->  return true if and only
     *  if the given words are sorted
     *  lexicographically in this alien language.
     *
     *  permutation: 排列
     *
     *
     *
     *  ---------------
     *
     *  IDEA 1) HASHMAP + BRUTE FORCE ???
     *
     *   { val : idx }
     *   -> {'a': 1, 'b': 2, .....}
     *
     *   -> map word to above
     *   -> sort, compare to the original input, check if they are the same.
     *
     *
     *  ---------------
     *
     */

    // LC 953
    // 7.24 - 34 am
    /**
     *  ->
     *  return `true` if and only if the given words are sorted
     *  lexicographically in this alien language.
     *
     *  ---------------------
     *
     *   IDEA 1) char op + looping ??
     *
     *
     *  ---------------------
     *
     *
     *
     */
    // 11.05 - 15 am
    // IDEA 1)  LOOP + char op ???
//    public boolean isAlienSorted(String[] words, String order) {
//        // edge
//        if(words == null || words.length == 0){
//            return true;
//        }
//        if(order == null){
//            return true; // ??
//        }
//
//        // build up `ordering` array
//        // ???
//        //int n = order.length();
//        // [adjusted_order_1, adjusted_order_2, ... ]
//        //     idx: `mapping to original alphabet system`
//        // use 26 instead of n, more explicit
//
//
//        int[] orderArr = new int[26]; // idx: the alphabet, val: the `actual` ordering
//        for(int i = 0; i < 26; i++){
//            int idx = order.charAt(i) - 'a'; // /??
//            orderArr[idx] = i;
//        }
//
//        System.out.println(">>> orderArr = " + Arrays.toString(orderArr));
//
//        // ???
//        for(int i = 1; i < words.length; i++){
//            String cur = words[i];
//            String prev = words[i-1];
//
//            // ???
//            if(!isCorrectOrder(cur, prev, orderArr)){
//                return false;
//            }
//
//        }
//
//        return true;
//    }
//
//    private boolean isCorrectOrder(String cur, String prev, int[] orderArr){
//        // ??
//        int size = Math.min(cur.length(), prev.length());
//
//        for(int i = 0; i < size; i++){
//
//            int prevIdx = prev.charAt(i) - 'a'; // get idx, so we can use `orderArr` mapping
//            int curIdx = cur.charAt(i) - 'a';
//
//            // ??? NOTE !!!
//            // if idx are different,
//            // MUST compare and make `decision` right away
//            if(prevIdx != curIdx){
////                if( orderArr[prevIdx] > orderArr[curIdx] ){
////                    return false;
////                }
////                return true;
//                return orderArr[prevIdx] < orderArr[curIdx];
//            }
//        }
//
//        // and check the `remaining` alphabet
//        // NOTE `<=`
//        return prev.length() <= cur.length();
//    }
//









    // IDEA 1) char op + looping ?? ???
    public boolean isAlienSorted_97(String[] words, String order) {
        // edge

        int[] orderArr = new int[26]; // ??
        for(int i = 0; i < order.length(); i++){
            // ???
            char val = order.toCharArray()[i];
            orderArr[val - 'a'] = i; // ????
        }

        System.out.println(">>> orderArr = " + Arrays.toString(orderArr));

        // 2. Compare adjacent words
        for (int i = 1; i < words.length; i++) {
            if (!isSorted(words[i - 1], words[i], orderArr)) {
                return false;
            }
        }


        for(int i = 1; i < words.length; i++){

            String cur = words[i];
            String prev = words[i-1];

            // ???
            //int size = Math.max(cur.length(), prev.length());
            int size = Math.max(cur.length(), prev.length());


            for(int j = 0; j < size; j++){

                // prev should < cur
                int curOrder = orderArr[cur.charAt(j) - 'a'];
                int preOrder = orderArr[prev.charAt(j) - 'a'];
                System.out.println(">> curOrder = " + curOrder +
                ", preOrder = " + preOrder);

                if(curOrder >= preOrder){
                    return true;
                }else{
                    return false;
                }
                // ???
                /**
                 * case:
                 *
                 * Input: words = ["apple","app"],
                 * order = "abcdefghijklmnopqrstuvwxyz"
                 *
                 */
//                if(cur.length() < prev.length()){
//                    return false;
//                }
            }


        }


        return true;
    }



    private boolean isSorted(String prev, String curr, int[] orderArr) {
        return  false;
    }










    public boolean isAlienSorted_99(String[] words, String order) {
        // edge

        // V2
        // 1. Map each character to its alien rank for O(1) lookup
        int[] alienOrder = new int[26];
        for (int i = 0; i < order.length(); i++) {
            alienOrder[order.charAt(i) - 'a'] = i;
        }


        Map<Character, Integer> orderMap = new HashMap<>();
        int i = 0;
        for(char ch: order.toCharArray()){
            orderMap.put(ch, i);
            i += 1;
        }

        // cache original words
        String[] words2 = words; // ???

        // map
        // ????
        Arrays.sort(words, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                int diff = orderMap.get(o1) - orderMap.get(o2);
                // ????
                if(diff == 0){
                    return o1.length() - o2.length();
                }
                return diff;
            }
        });

        for(int j = 0; j < words.length; j++){
            if(!words[j].equals(words2[j])){
                return false;
            }
        }


        return true;
    }


    // LC 165
    // 8.11 - 21 am
    /**
     * ->
     *  Return the following:
     *
     * If version1 < version2, return -1.
     * If version1 > version2, return 1.
     * Otherwise, return 0.
     *
     * -------------
     *
     *  IDEA 1) string op (split) + compare
     *
     *
     * -------------
     *
     *
     *
     */
    // IDEA 1) string op (split) + compare
    // time: O(N)  // ????
    // space: O(N)
    public int compareVersion(String version1, String version2) {
        // edge
        if (version1.equals(version2)) {
            return 0;
        }

        List<Integer> l1 = new ArrayList<>();
        List<Integer> l2 = new ArrayList<>();

        for(String x: version1.split("\\.")){
            System.out.println(" x = " + x);
            if(!x.equals("0")){
                if(x.startsWith("0")){
                    l1.add(Integer.parseInt(removeZeroPrefix(x)));
                }
               // l1.add(Integer.parseInt(removeZeroPrefix(x)));
            }
        }

        for(String x: version2.split("\\.")){
            if(!x.equals("0")){
                if(x.startsWith("0")){
                    l2.add(Integer.parseInt(removeZeroPrefix(x)));
                }
            }
        }

        System.out.println(">>> l1 = " + l1 +
        ", l2 = " + l2);

        int size = Math.min(l1.size(), l2.size());
        for(int j = 0; j < size; j++){
            if(l1.get(j) > l2.get(j)){
                return 1;
            }else{
                return -1;
            }
        }


        return 0;
    }

    private String removeZeroPrefix(String x){
        // ???
//        String[] arr = x.split("0");
//        int idx = arr[0].length();
        int idx = 0;
        for(String str: x.split("")){
            if(!str.equals("0")){
                return x.substring(idx);
            }
            idx += 1;
        }
      // return x.substring(idx + 1); //???
        return x;
    }


    // LC 524
    // 8.51 - 10.02 am
    /**
     *  -> return the longest string in the
     *  dictionary that can be formed
     *  by deleting some of the
     *  given string characters.
     *
     *
     *
     *   string s
     *   arr[] dictionary
     *
     *  NOTE:
     *      - If there is more than one possible result,
     *        return the longest word with the
     *        smallest lexicographical order.
     *
     *      - if no result, return ""
     *
     *  -----------
     *
     *   IDEA 1) BRUTE FORCE 2 POINTERS ??
     *    -> collect candidates
     *    -> if a tie,
     *        return longest word with the
     *        smallest lexicographical order
     *
     *   IDEA 2) HASHMAP + 1 PASS ???
     *
     *  -----------
     */
    public String findLongestWord(String s, List<String> dictionary) {
//        if (dictionary.size() == 0 && s != null){
//            return "";
//        }
        // edge
        if (s == null || dictionary == null || dictionary.isEmpty()) {
            return "";
        }

        List<String> list = new ArrayList<>();

        for(String d: dictionary){
            if(canForm(d, s)){
                list.add(d);
            }
        }

        // sort ???
        Collections.sort(list, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
//                int diff = o1.compareTo(o2); // ???
//                if(diff == 0){
//                  //  return o1.length() - o2.length();
//                    return o2.length() - o1.length();
//                }
//                return diff;
                if (o1.length() != o2.length()) {
                    return o2.length() - o1.length(); // Longest first
                }
                return o1.compareTo(o2); // Alphabetical tie-breaker
            }
        });



        return list.isEmpty() ? "" : list.get(0);
    }

    private boolean canForm(String x, String s){
        // edge
        if(x.isEmpty()){
            return false;
        }
        if(x.equals(s)){
            return true;
        }
        // ???
        // 2 pointers ???
        int i = 0;
        int j = 0;
        while (i < s.length()){
            if(x.charAt(i) == s.charAt(j)){
                // early quit
                if(j == s.length() - 1){
                    return true;
                }
                // move s's pointer
               // i += 1;
                j += 1;
            }

            // move x's pointer anyway
            i += 1;

        }

        return j == s.length() - 1;
    }










    public String findLongestWord_99(String s, List<String> dictionary) {
        // edge

        // { val : [idx_1, idx_2, ..] }
        Map<String, List<Integer>> map = new HashMap<>();
        int i = 0;
        for(String x: s.split("")){
            if(!map.containsKey(x)){
                map.put(x, new ArrayList<>());
            }
            List<Integer> list = map.get(x);
            list.add(i);
            map.put(x, list);
            //map.put(x, map.getOrDefault(x, 0) + 1);
            i += 1;
        }

        String res = "";

        // ???
        for(String d: dictionary){
            // ???
            if(map.containsKey(d)){
                if(d.length() > res.length()){
                    res = d;
                }
            }

            // ???
        }






        return res;
    }





    // LC 997
    // 11.08 - 18 am
    /**
     *  -> Return the label of the town judge
     *  if the town judge exists and can
     *  be identified, or return -1 otherwise.
     *
     *    - n people: 1 to n
     *    - if a person is a judge
     *      - the judge trusts NOBODY
     *      - every body (except judge) trusts the judge
     *      - there is EXACTLY one judge
     *
     *      trust[i] = [ai, bi]:
     *           - ai trusts bi
     *
     *
     *  -----------------
     *
     *   IDEA 1) GRAPH + DFS
     *
     *
     *  -----------------
     *
     *
     */
    // IDEA 1) GRAPH + DFS
    public int findJudge(int n, int[][] trust) {
        // edge
        if(trust == null || trust.length == 0){
            return -1;
        }

        // person - the people he trusts
        // map: { person, [trust_1, trust_2, ...] }  ( --> )
        Map<Integer, List<Integer>> map = new HashMap<>();

        // person - the people who trust person  ( <-- )
        // map2: { person, [trust_1, trust_2, ...] }
        Map<Integer, List<Integer>> map2 = new HashMap<>();

        for (int[] x : trust) {
            /**
             *      *      trust[i] = [ai, bi]:
             *      *           - ai trusts bi
             *      *
             */
            int ai = x[0];
            int bi = x[1];

            if (!map.containsKey(ai)) {
                map.put(ai, new ArrayList<>());
            }
            List<Integer> list = map.get(ai);
            list.add(bi);
            map.put(ai, list);

            // -----------

            if (!map2.containsKey(bi)) {
                map2.put(bi, new ArrayList<>());
            }
            List<Integer> list2 = map2.get(bi);
            list2.add(ai);
            map2.put(bi, list2);
        }

        Set<Integer> set = new HashSet<>();

        System.out.println(">>> map2 = " + map2);

        for (Integer k : map.keySet()) {
            getNotJudgeList(map, set, k);
        }

        if (n - set.size() > 1) {
            return -1;
        }
        // ??
        for (int i = 1; i < n + 1; i++) {
            if (!set.contains(i) &&  map2.containsKey(i) && map2.get(i).size() == n - 1) {
                return i;
            }
        }

        return -1; // ???
    }

    private void getNotJudgeList(Map<Integer, List<Integer>> map, Set<Integer> set, Integer x) {
        // edge
        if (map.isEmpty()) {
            return;
        }

        // add as `visited`
        set.add(x);

        // visit `next`
        for (Integer next : map.get(x)) {
            if (!set.contains(x)) {
                getNotJudgeList(map, set, next);
            }
        }

    }



    // LC 669
    // 1.31 - 41 pm
    /**
     *
     * -> Return the root of the trimmed binary search tree.
     * Note that the root may change depending
     * on the given bounds.
     *
     *  -  trim the tree so that all its
     *  elements lies in [low, high].
     *
     *
     *  BST: binary search tree
     *  [low, high]
     *
     *
     *  ---------------
     *
     *   IDEA 1) DFS + BST property ???
     *
     *    - BST property: left < root < right
     *
     *    -> pre-order ??????
     *
     *
     *  ---------------
     *
     */
    // IDEA 1) DFS + BST property ???
    // post order ???
    public TreeNode trimBST(TreeNode root, int low, int high) {
        // edge
        if(root == null){
            return null;
        }

        if(root.val < low || root.val > high){
            return null;
        }


        TreeNode _left = trimBST(root.left, low, high);
        TreeNode _right = trimBST(root.right, low, high);

        root.left = _left;
        root.right = _right;

        return root;
    }





    // IDEA 1) DFS + BST property ???
    // pre-order ???
    // TODO: validate below ??
    public TreeNode trimBST_99(TreeNode root, int low, int high) {
        // edge
        if(root == null){
            return null;
        }
        if(root.val < low || root.val > high){
            return null;
        }

        TreeNode _left = trimBST_99(root.left, low, high);
        TreeNode _right = trimBST_99(root.right, low, high);

        root.left = _left;
        root.right = _right;

        return root;
    }


    // LC 99
    // 2. 06 - 16 pm
    /**
     *
     *  ------------
     *
     *   IDEA 1) bst property + dfs??? + in-order ??
     *    bst: left < root < right
     *
     *  -------------
     *
     *
     */
    List<TreeNode> inOrderNodes = new ArrayList<>();

    public void recoverTree(TreeNode root) {
        if (root == null)
            return;

        // 1. Get the list of nodes in In-order
        getInorder(root);

        // 2. Identify the two swapped nodes
        TreeNode first = null;
        TreeNode second = null;

        for (int i = 1; i < inOrderNodes.size(); i++) {
            // Check for a "drop" in the sorted sequence
            if (inOrderNodes.get(i - 1).val > inOrderNodes.get(i).val) {
                // If it's the first drop, 'first' is the larger node (i)
                if (first == null) {
                    first = inOrderNodes.get(i);
                }
                // 'second' is always the smaller node (i + 1)
                // If there's a second drop later, this will overwrite 'second' correctly
                second = inOrderNodes.get(i + 1);
            }
        }


        /** NOTE !!
         *
         *   since we already the `first` and `second` node.
         *   in order to `swap` them, all we need to do is:
         *   -> swap their `value`.
         *
         *   that's it. the swap is not really `swap all node object`
         *   , just change values
         */
        // 3. Swap the values of the two identified nodes
        if (first != null && second != null) {
            int temp = first.val;
            first.val = second.val;
            second.val = temp;
        }
    }

    // get in-order list,
    // for BST, it's a increasing list
    private void getInorder(TreeNode root) {
        if (root == null)
            return;
        getInorder(root.left);
        inOrderNodes.add(root); // Store the node, not just the value!
        getInorder(root.right);
    }

//    List<Integer> inOrderList = new ArrayList<>();
//    public void recoverTree(TreeNode root) {
//        // edge
//        if(root == null){
//            return;
//        }
//
//        getInorder(root);
//        // ???
//        // loop over `in-order` list
//        // so if node arranged correctly,
//        // should be in `increasing` order
//        int first = -1; // ???
//        int second = -1;
//
//        // ????
//        boolean firstFound = false;
//        boolean secondFound = false;
//
//        for(int i = 1; i < inOrderList.size(); i++){
//            if(inOrderList.get(i) < inOrderList.get(i-1)){
//                if(!firstFound){
//                    first = inOrderList.get(i); //???
//                    firstFound = true;
//                }else{
//                    second = inOrderList.get(i); //???
//                    secondFound = true;
//                }
//            }
//
//        }
//
//        // swap ???
//
//        // build tree ???
//
//    }
//
//    private void getInorder(TreeNode root){
//        // edge
//        if(root == null){
//            return;
//        }
//        getInorder(root.left);
//        inOrderList.add(root.val);
//        getInorder(root.right);
//    }
//


    // LC 1971
    // 7.34 - 44 pm
    /**
     *  -> Given edges and the integers n, source, and destination,
     *  return
     *   - true
     *     - if there is a valid
     *       path from source to destination
     *   - false
     *      - otherwise.
     *
     *
     *    vertex: 0 -> n-1
     *
     *    edges[i] = [ui, vi]
     *       - bi-directional edge between vertex ui, vi
     *
     *    - Every vertex pair is connected by at most one edge,
     *
     *    - no vertex has an edge to itself.
     *
     *
     *
     *  -------------
     *
     *   IDEA 1) DFS + GRAPH
     *
     *   IDEA 2) FIND PARENT NODE + ROUTE COMPRESSION ???
     *
     *
     *  -------------
     *
     *
     */
    // 7.22 - 32 am
    // IDEA HASH SET ???
    public boolean validPath(int n, int[][] edges, int source, int destination) {
        // edge
        if(source == destination){
            return true; // ???
        }
        if(edges == null){
            return false;
        }

        // build graph
        // map: { node : [neighbor_1, neighbor_2, ..] }
        Map<Integer, List<Integer>> map = new HashMap<>();

        for(int[] e: edges){
            // ???
            int ui = e[0];
            int vi = e[1];

            if(!map.containsKey(ui)){
                map.put(ui, new ArrayList<>());
            }

            if(!map.containsKey(vi)){
                map.put(vi, new ArrayList<>());
            }

            List<Integer> l1 = map.get(ui);
            List<Integer> l2 = map.get(vi);

            l1.add(vi);
            l2.add(ui);

            map.put(ui, l1);
            map.put(vi, l2);
        }

        System.out.println(">>> map = " + map);

        // edge
        if(map.isEmpty() || !map.containsKey(source)){
            return false;
        }

        return map.get(source).contains(destination);
    }






    // IDEA 1) DFS + GRAPH
    public boolean validPath_99(int n, int[][] edges, int source, int destination) {
        // edge
        // check if n == edges.size() - 1????

        // build graph
        // map: { node : [neighbor_1, neighbor_2, ..] }
        Map<Integer, List<Integer>> map = new HashMap<>();

        for(int[] e: edges){
            // ???
            int ui = e[0];
            int vi = e[1];

            if(!map.containsKey(ui)){
                map.put(ui, new ArrayList<>());
            }

            if(!map.containsKey(vi)){
                map.put(vi, new ArrayList<>());
            }

            List<Integer> l1 = map.get(ui);
            List<Integer> l2 = map.get(vi);

            l1.add(vi);
            l2.add(ui);

            map.put(ui, l1);
            map.put(vi, l2);
        }

        System.out.println(">>> map = " + map);


        // ???
        return canVisit(map, source, destination, source, new HashSet<>());
       //return false;
    }


    private boolean canVisit(Map<Integer, List<Integer>> map, int src, int dest, int node, Set<Integer> visited){
        System.out.println(">>> node = " + node
        + ", visited = " + visited);

        // edge
        // ??
        //if(node == null)
        if(dest == src){
            return true;
        }
        if(node == dest){
            return true;
        }

        // mark as visited
        visited.add(node);

        // visit next
        // ???
        if (map.containsKey(node)) {
            for(int next: map.get(node)){
                if(!visited.contains(next)){
                    if(canVisit(map, src, dest, next, visited)){
                        return true;
                    }
                }
            }
        }

        // ???
        return false;
    }


    // LC 1791
    // 8.14 - 24 am
    /**
     *
     *  -> Return the center of the
     *     given star graph.
     *
     *
     *  n nodes: 1 -> n
     *
     *  - A star graph:
     *      a graph where there is one center node and
     *      exactly n - 1 edges that connect the center
     *      node with every other node.
     *
     *  - edges[i] = [ui, vi]
     *      - edge between ui and vi
     *
     *  ---------------
     *
     *   IDEA 1)  DFS + GRAPH
     *    DFS
     *    0. build graph
     *    1. get nodes had `n-1` direct conn
     *    2. validate above
     *
     *
     *
     * ---------------
     *
     */
    // IDEA 1)  DFS + GRAPH
    public int findCenter(int[][] edges) {
        // edge

        // build graph
        // map: { node : [neighbor_1, neighbor_2, ..] }
        Map<Integer, List<Integer>> map = new HashMap<>();

        Set<Integer> set = new HashSet<>();
        for(int[] e: edges){
            // ???
            int ui = e[0];
            int vi = e[1];


            set.add(ui);
            set.add(vi);

            if(!map.containsKey(ui)){
                map.put(ui, new ArrayList<>());
            }

            if(!map.containsKey(vi)){
                map.put(vi, new ArrayList<>());
            }

            List<Integer> l1 = map.get(ui);
            List<Integer> l2 = map.get(vi);

            l1.add(vi);
            l2.add(ui);

            map.put(ui, l1);
            map.put(vi, l2);
        }



        // check ???
        // 1. get nodes had `n-1` direct conn
        int n = set.size();

        for(int k: map.keySet()){
            if(map.get(k).size() == n - 1){
                return k;
            }
        }


        return -1; // ??? should NOT visit here
    }



    // LC 277
    // 8.49 - 59 am
    /**
     *
     *  -> implement a function int findCelebrity(n).
     *  There will be exactly one celebrity
     *  if he/she is in the party.
     *
     *
     *   -  there may exist one celebrity.
     *      The definition of a celebrity is that all the other
     *      n - 1 people know him/her but he/she
     *      does not know any of them.
     *
     *   -  n people: 0 - n-1
     *
     *   NOTE:
     *
     *    1. The directed graph
     *       is represented as an adjacency matrix,
     *       which is an n x n matrix where a[i][j] = 1
     *       means person i knows person j
     *       while a[i][j] = 0 means the contrary.
     *
     *
     *    2. Remember that you won't have direct
     *       access to the adjacency matrix.
     *
     *
     *
     *
     *   --------
     *
     *    IDEA 1) DFS + GRAPH
     *
     *
     *
     *
     */
    // dummy API for passing java syntax check
    // offered by LC platform when submission
    private boolean knows(int a, int b){
        return true;
    }

    // V0
    // IDEA 1) DFS + GRAPH
    public int findCelebrity(int n) {
        // edge

        /**
         *
         *
         *      *    1. The directed graph
         *      *       is represented as an adjacency matrix,
         *      *       which is an n x n matrix where a[i][j] = 1
         *      *       means person i knows person j
         *      *       while a[i][j] = 0 means the contrary.
         *      *
         *
         *
         *  ----
         *
         *  The only thing you are allowed to do is to ask question
         *  s like: "Hi, A. Do you know B?"
         *  to get information of whether A knows B.
         *  You need to find out the celebrity (or verify
         *  there is not one) by asking as few questions as possible
         *  (in the asymptotic sense).
         *
         *  ----
         *        private boolean knows(int a, int b){
         *         return true;
         *     }
         *
         *
         */

        // ???
        // Set<Integer> notCele = new HashSet<>();
        // ???
        int ans = 0;
        // ??
        for(int i = 0; i < n; i++){
            // ???
            if(knows(ans, i)){
                ans = i;
            }
        }

        for(int i = 0; i < n; i++){
            // ???
            if(!knows(i, ans)){
                return -1;
            }
        }


        // ???
        return ans;
    }



    // LC 752
    // 7.51 - 8.01 am
    /**
     *  -> Given a `target`  representing the value of the wheels
     *  that will unlock the lock, return the
     *  `minimum total number`
     *  of turns required to open the lock,
     *  or -1 if it is impossible.
     *
     *  initially starts at '0000'
     *
     *
     *   ---------------
     *
     *   IDEA 1) BFS
     *
     *   ---------------
     *
     */
    //  IDEA 1) BFS
    public int openLock(String[] deadends, String target) {
        // edge
        if(target.equals("0000")){
            return 0;
        }

        HashSet<String> deadSet = new HashSet<>();
        for(String s: deadends){
            deadSet.add(s);
        }

        // ??
        HashSet<String> visited = new HashSet<>();

        Queue<String> q = new LinkedList<>();
        String initStatus = "0000";
        q.add(initStatus);
        visited.add(initStatus);

        int op = 0;

        while (!q.isEmpty()){
            int size = q.size();
            for(int i = 0; i < size; i++){
                String cur = q.poll();
                // early quit
                if(cur != null && cur.equals(target)){
                    return op;
                }
                // note !!! we use `char array`
                // handle string update val by digit

                // NOTE !!! loop over idx
                for(int j = 0; j < cur.length(); j++){

                    // NOTE !!! below
                    int val = cur.toCharArray()[j] - '0'; // ????

                    char[] chars1 = cur.toCharArray();
                    char[] chars2 = cur.toCharArray();

                    // note !!! 2 directions
                    int val1 = (val + 1) > 9 ? 0: val + 1;
                    int val2 = (val - 1) < 0 ? 9: val - 1;

                    chars1[j] = (char) val1;
                    chars2[j] = (char) val2;

                    String next1 = String.valueOf(chars1);

                    chars2[j] -= 1;
                    String next2 = String.valueOf(chars2);

                    if(!deadSet.contains(next1) && !visited.contains(next1)){
                        q.add(next1);
                        visited.add(next1);
                    }

                    if(!deadSet.contains(next2) && !visited.contains(next2)){
                        q.add(next2);
                        visited.add(next2);
                    }


                }
            }
            op += 1; // ???
        }

        return op;
    }






    // time: O(9 ^ N)
    // space: O(N)
    public int openLock_99(String[] deadends, String target) {

//        Set<String> dead = new HashSet<>(Arrays.asList(deadends));
//        Set<String> visited = new HashSet<>();
//        Queue<String> queue = new LinkedList<>();


        // edge
        if(target.equals("0000")){
            return 0;
        }


        List<String> deadLocks = new ArrayList<>();
        //deadLocks.addAll(List.of(deadends));
        deadLocks.addAll(Arrays.asList(deadends));
        // ???
        if(deadLocks.contains(target)){
            return -1;
        }

        Set<String> visited = new HashSet<>();

        // ??
        Queue<String> q = new LinkedList<>();
        q.add("0000");
        visited.add("0000");

        int opCnt = 0;

        // ???
        while (!q.isEmpty()){

            int size = q.size();
            //opCnt += 1;

            for(int i = 0; i < size; i++){

               // opCnt += 1;
                String cur = q.poll();
                //visited.add(cur);

                // ??
                if(cur == null){
                    continue;
                }

                if(cur.equals(target)){
                    return opCnt;
                }
                // ???
                // loop over 4 indices in lock
                for(int j = 0; j < 4; j++){
                    // loop digits ???
                    // ???
                    int val = cur.charAt(j); // ??
                    int val1 = val + 1 > 9 ? 0 : val + 1;
                    int val2 = val - 1 < 0 ? 9: val - 1;

                    String newVal1 = cur.substring(0, j) + val1 + cur.substring(j+1);
                    String newVal2 = cur.substring(0, j) + val2 + cur.substring(j+1);

                    // ??
                    if(!visited.contains(newVal1) && !deadLocks.contains(newVal1)){
                            q.add(newVal1);
                            visited.add(newVal1);
                        }

                    if(!visited.contains(newVal2) && !deadLocks.contains(newVal2)){
                        q.add(newVal2);
                        visited.add(newVal2);
                    }


//                    for(int k = 0; k < 10; k++){
//                        // ???
//                        String newVal = cur.substring(0, j) + k + cur.substring(j+1);
//                        if(!visited.contains(newVal) && !deadLocks.contains(newVal)){
//                            q.add(newVal);
//                        }
//                    }
                }
            }

            opCnt += 1;

        }



        return -1;
    }



    // LC 733
    // 8.32 - 9.00 am
    /**
     *  -> Return the modified image
     *  after performing the flood fill.
     *
     *  ------------
     *
     *   IDEA 1) BFS ???
     *
     *
     *  ------------
     *
     */
    // 17.00 - 10 pm
    public int[][] floodFill(int[][] image, int sr, int sc, int color) {
        // edge
        int initColor = image[sr][sc];
        if (initColor == color) return image;


        int[][] moves = new int[][]{ {0,1}, {0,-1}, {1,0}, {-1,0} };
        int l = image.length;
        int w = image[0].length;

        //boolean[][] visited = new boolean[l][w];

        Queue<Integer[]> q = new LinkedList<>();

       // int initColor = image[sr][sc];

        // add (sr, sc) to Q
        q.add(new Integer[]{sr,sc});
        //visited[sr][sc] = true;
        image[sr][sc] = color; // ???


       // int initColor = image[sr][sc];

        while (!q.isEmpty()){
            // /?
            //int size = q.size();
            Integer[] cur = q.poll();
            int x = cur[0];
            int y = cur[1];


            for(int[] m: moves){
                int x_ = x + m[0];
                int y_ = y + m[1];

                if(x_ >= 0 && x_ < w && y_ >= 0 && y_ < l){
                    if(image[y_][x_] != initColor && image[y_][x_] == initColor){
                        // modfify color
                        image[y_][x_] = color;
                        q.add(new Integer[]{y_, x_});
                        //visited[y_][x_] = true;
                    }
                }
            }

            // modify the color
            //image[y][x] = color;
//
//            for(int i = 0; i < size; i++){
//                for(int[] m: moves){
//                    int x_ = x + m[0];
//                    int y_ = y + m[1];
//
//                    if(x_ >= 0 && x_ < w && y_ >= 0 && y_ < l){
//                        if(!visited[y_][x_] && image[y_][x_] == initColor){
//                            // modfify color
//                            image[y_][x_] = color;
//                            q.add(new Integer[]{y_, x_});
//                            visited[y_][x_] = true;
//                        }
//                    }
//                }
//            }
        }



        return image; // ??
    }





    public int[][] floodFill_99(int[][] image, int sr, int sc, int color) {
        // edge

        int[][] moves = new int[][]{ {0,1}, {0,-1}, {1,0}, {-1,0} };
        int l = image.length;
        int w = image[0].length;

        boolean[][] visited = new boolean[l][w];

        // bfs
        Queue<Integer[]> q = new LinkedList<>();
//        q.add(new Integer[]{0,0});
//        visited[0][0] = true;
        q.add(new Integer[]{sr,sc});
        visited[sr][sc] = true;

        int initColor = image[sr][sc];

        image[sr][sc] = color;




        while (!q.isEmpty()){
            // /?
            int size = q.size();
            Integer[] cur = q.poll();
            int x = cur[0];
            int y = cur[1];

            // modify the color
            image[y][x] = color;

            for(int i = 0; i < size; i++){
                for(int[] m: moves){
//                    int x_ = x + m[1];
//                    int y_ = y + m[0];
                    int x_ = x + m[0];
                    int y_ = y + m[1];

                    if(x_ >= 0 && x_ < w && y_ >= 0 && y_ < l){
                        if(!visited[y_][x_] && image[y_][x_] == initColor){
                            q.add(new Integer[]{y_, x_});
                            visited[y_][x_] = true;
                        }
                    }
                }
            }
        }

        return image;

    }


    // LC 994
    // 16.35 - 45 pm
    public int orangesRotting(int[][] grid) {
        // edge

        int[][] moves = new int[][]{ {0,1}, {0,-1}, {1,0}, {-1,0} };
        int l = grid.length;
        int w = grid[0].length;

        boolean[][] visited = new boolean[l][w];

        int freshCnt = 0;

        // bfs
        Queue<Integer[]> q = new LinkedList<>();
        // get `rotten` orange
        for(int y = 0; y < l; y++){
            for(int x = 0; x < w; x++){
                // rotten
                if(grid[y][x] == 2){
                    //rotttenList.add(new Integer[]{x, y});
                    q.add(new Integer[]{x, y});
                    visited[y][x] = true;
                }
                // fresh
                else if(grid[y][x] == 1){
                    freshCnt += 1;
                }
            }
        }

        int days = 0;

        // edge
        if(freshCnt == 0){
            return 0;
        }

        while (!q.isEmpty()){
            int size = q.size();
            for(int i = 0; i < size; i++){
                // early quit
                // ??
                if(freshCnt == 0){
                    return days;
                }
                Integer[] cur = q.poll();
                int x = cur[0];
                int y = cur[1];
                for(int[] m: moves){
                    int x_ =  x + m[0];
                    int y_ = y + m[1];
                    if(x_ >= 0 && x_ < w && y_ >= 0 && y_ < l){
                        if(grid[y_][x_] == 1 && !visited[y_][x_]){
                            q.add(new Integer[]{x_, y_});
                            visited[y_][x_] = true;
                            freshCnt -= 1;
                        }
                    }
                }

            }

            days += 1;

        }


        return freshCnt == 0 ? days : -1;
    }






    // IDEA 1) BFS
    public int orangesRotting_99(int[][] grid) {
        // edge

        int[][] moves = new int[][]{ {0,1}, {0,-1}, {1,0}, {-1,0} };
        int l = grid.length;
        int w = grid[0].length;

        boolean[][] visited = new boolean[l][w];

        //List<Integer[]> rotttenList = new ArrayList<>();

        int freshCnt = 0;

        // bfs
        Queue<Integer[]> q = new LinkedList<>();
        // get `rotten` orange
        for(int y = 0; y < l; y++){
            for(int x = 0; x < w; x++){
                if(grid[y][x] == 2){
                    //rotttenList.add(new Integer[]{x, y});
                    q.add(new Integer[]{x, y});
                    visited[y][x] = true;
                }
                else if(grid[y][x] == 1){
                    freshCnt += 1;
                }
            }
        }

        // early quit
        if(q.isEmpty() && freshCnt > 0){
            return -1;
        }
        if(freshCnt == 0){
            return 0;
        }



        int minutes = 0;

        while (!q.isEmpty()){

            int size = q.size();
            for(int i = 0; i < size; i++){
                // early quit
                if(freshCnt == 0){
                    return minutes;
                }
                Integer[] cur = q.poll();
                int x = cur[0];
                int y = cur[1];

                // mark as `rotten`
//                if(grid[y][x] == 1){
//                    grid[y][x] = 2;
//                    freshCnt -= 1;
//                }

                for(int[] m: moves){
                    int x_ = x + m[0];
                    int y_ = y + m[1];

                    if(x_ >= 0 && x_ < w && y_ >= 0 && y_ < l){
                        if(grid[y_][x_] == 1 && !visited[y_][x_]){
                            q.add(new Integer[]{y_, x_});
                            visited[y_][x_] = true;

                            grid[y][x] = 2;
                            freshCnt -= 1;


                            //visited[y_][x_] = true;
                        }
                    }
                }
            }

            minutes += 1;
        }



        return minutes;
    }



    // LC 1436
    // 7.21 - 31 am
    /**
     *  -> Return the destination city,
     *  that is, the city `without `
     *  any path outgoing to another city.
     *
     *
     *  - paths[i] = [cityAi, cityBi]
     *  means there exists a `direct path` going
     *  from cityAi to cityBi
     *
     *  --------------------
     *
     *   IDEA 1) HASH SET
     *
     *
     *  --------------------
     *
     */
    // IDEA 1) HASH SET
    public String destCity(List<List<String>> paths) {
        // edge

        Set<String> all = new HashSet<>();
        Set<String> outgoing = new HashSet<>();

        for(List<String> p: paths){
            outgoing.add(p.get(0));

            // ??
            for(String x: p){
                all.add(x);
            }
        }

        // get res
        for(String y: all){
            if(!outgoing.contains(y)){
                return y;
            }
        }


        return null;
    }


    // LC 542, 1765
    // 7.32 - 52 am
    /**
     *  ->  return the distance of
     *  the `nearest` 0 for each cell.
     *
     *
     *  NOTE:
     *   - The distance between two cells
     *     sharing a common edge is 1.
     *
     *
     *  ------------------
     *
     *   IDEA 1) multi-source BFS ???? and maintain the
     *   `nearest` dist ????
     *
     *    -> loop over `0`, and update its dist
     *      to `1` when new_dist < old_dist
     *
     *
     *  ------------------
     *
     *
     */
    // IDEA 1) multi-source BFS
    public int[][] updateMatrix(int[][] mat) {
        // edge

        int l = mat.length;
        int w = mat[0].length;

        int[][] moves = new int[][]{ {0,1}, {0,-1}, {1,0}, {-1,0} };

        // get 0 cells
//        List<Integer[]> zeroList = new ArrayList<>();
//        for(int y = 0; y < l; y++){
//            for(int x = 0; x < w; x++){
//                if(mat[y][x] == 0){
//                    zeroList.add(new Integer[]{x, y});
//                }
//            }
//        }

        // bfs and maintain `min` dist
        // q: { [x, y, dist], ..... }
        //q: { [x, y], ..... }
        Queue<Integer[]> q = new LinkedList<>();

        for (int r = 0; r < l; r++) {
            for (int c = 0; c < w; c++) {
                if (mat[r][c] == 0) {
                    q.offer(new Integer[]{r, c});
                } else {
                    // Use -1 to represent "not yet visited/calculated"
                    mat[r][c] = -1;
                }
            }
        }


        // can we `revisit` ???
        boolean[][] updated = new boolean[l][w];

        // bfs and maintain `min` dist
        // q: { [x, y, dist], ..... }
        //Queue<Integer[]> q = new LinkedList<>();
        // add all `0` cells to queue
//        for(Integer[] z: zeroList){
//            int x = z[0];
//            int y = z[1];
//            q.add(new Integer[]{x, y, 0}); // init `dist` as 0 ??????
//
//            // add to visited ??
//            //visited[y][x] = true;
//        }


        // run BFS
        while (!q.isEmpty()){
            int size = q.size();

            for(int i = 0; i < size; i++){

                Integer[] cur = q.poll();
                // check dist
                int x = cur[0];
                int y = cur[1];
                int dist = cur[2];

                //  ??? only check if need to update
                // dist when meet `1` cell
                if(mat[y][x] == 1){
                   // ????
                   if(!updated[y][x]){
                       mat[y][x] = dist;
                       updated[y][x] = true; // ????
                   }else{
                       mat[y][x] = Math.min(dist, mat[y][x]);
                   }
                }

                // move 4 dirs
                for(int[] m: moves){
                    int x_ = x + m[0];
                    int y_ = y + m[1];
                    if(x_ >= 0 && x_ < w && y_ >= 0 && y_ < l){
                        // ?? above validation is enough ??
                        q.add(new Integer[]{x_, y_, dist + 1});
                    }
                }
            }
        }



        return mat;
    }



    // LC 815
    // 8.27 - 37 am
    /**
     *  -> Return the `least number` of `buses`
     *  must take to travel from `source to target. `
     *  Return -1 if it is not possible.
     *
     *   - routes[i] is a bus route that the ith bus repeats forever.
     *   - e.g.:
     *     - routes[0] = [1, 5, 7]
     *       ->   1 -> 5 -> 7 -> 1 -> 5 -> 7 -> ....
     *
     *   - can ONLY travel via bus
     *   - is NOT in any bus initially
     *
     *
     *
     *  -----------------
     *
     *   IDEA 1) BFS
     *
     *   IDEA 2) OTHER GRAPH ALGO ???
     *
     *   IDEA 3) UNION FIND ???
     *
     *  IDEA 4) DFS + BFS
     *
     *-----------------
     *
     */
    // 15.43 - 53 pm
    /**
     *
     *   // // IDEA 1) BFS ???
     *
     *   --------
     *
     *   ex 1)
     *
     *   Input: routes = [[1,2,7],[3,6,7]], source = 1, target = 6
     *   Output: 2
     *
     *
     *   -> graph =  { 1: [2,7], 2: [1,7],
     *                 3: [6,7], 6:[3,7], 7: [3,6, 1,2]  }
     *
     *
     *   ex 2)
     *
     *    Input: routes = [[7,12],[4,5,15],[6],[15,19],[9,12,13]],
     *    source = 15, target = 12
     *
     *    -> graph = {
     *         7: [12], 12: [7], ....
     *    }
     *
     *
     */
    // IDEA 1) BFS
    public int numBusesToDestination(int[][] routes, int source, int target) {
        // edge
        if(source == target){
            //return 1;
            return 0;
        }

        // NOTE !!!
        // // 2. Build Mapping: Stop -> List of Route Indices it belongs to
        // { stop -> [ route_1, route_2, ... ] }

        // 1. build graph
        // map: { node: [next_1, next_2, ...] }
        // better way to below ?????
        Map<Integer, List<Integer>> map = new HashMap<>();


        for(int[] r: routes){
            // ???
            int start = -1;
            List<Integer> list = null;

            for(int i = 0; i < r.length; i++){
                start = r[i];
                if(!map.containsKey(start)){
                    map.put(start, new ArrayList<>());
                }
                list = map.get(start);
                if(i != start){
                    list.add(i);
                }

            }
            map.put(start, list);
        }

        // edge: if source or target NOT in map
        if(!map.containsKey(source) || !map.containsKey(target)){
            return -1;
        }

        Set<Integer> visited = new HashSet<>();

        // NOTE !!!
        boolean[] visitedRoutes = new boolean[routes.length];




        //Queue
        Queue<Integer> q = new LinkedList<>();
        q.add(source);
        visited.add(source);
        //visitedStops.add(source);

       // visitedRoutes.

        int layer = 0;

        // 2. BFS
        // ???
        while (!q.isEmpty()){
            // ???
            layer += 1;
            int size = q.size();
            for(int j = 0; j < size; j++){
                // ???
                if(visited.contains(j)){
                    continue;
                }

                int cur = q.poll();
                if(cur == target){
                    return layer;
                }
                for(int next: map.get(cur)){
                    if(!visited.contains(next)){
                        visited.add(next);
                        q.add(next);
                    }
                }
            }
        }

        return -1;
    }








    //  IDEA 1) BFS
    public int numBusesToDestination_99(int[][] routes, int source, int target) {
        // edge
        if(source == target){
            return 0;
        }

        // build graph
        // map { node: [next_1, next_2, ...] }
        Map<Integer, List<Integer>> map = new HashMap<>();
        for(int[] s: routes){
            // ??? remove the `non valid` bus
            if(s.length > 1){
                // ???
                for(int x: s){
                    // ??
                    //if()
                }
            }
        }


        return -1;
    }



    // LC 95
    // 10.36 - 46 am
    /**
     *  -> return all the structurally
     *  unique BST's (binary search trees),
     *
     *  ans could be in ANY order
     *
     *
     *  int n
     *  node has val in [1, n]
     *
     *  -----------------
     *
     *  IDEA 1) BST + DFS + BRUTE FORCE ??
     *
     *   -> start from every node, make node as root
     *   -> navigate all possible trees based on above
     *   -> collect and return ans
     *
     *  IDEA 2) DP ?????
     *
     *
     *  -----------------
     *
     *
     */
    public List<TreeNode> generateTrees(int n) {

        return null;
    }




    // LC 126
    // 17.53 - 16.03 pm
    /**
     *
     *
     *
     */
    public List<List<String>> findLadders(String beginWord, String endWord, List<String> wordList) {

        return null;
    }

    // LC 126
    // 18.04 - 14 pm
    /**
     *
     *  IDEA 1) BST + DFS
     *
     *   [smaller_tree, bigger_tree]
     *
     *
     */
    //TreeNode[] splitNodes = new TreeNode[2]; // ????
    public TreeNode[] splitBST(TreeNode root, int target) {
        // edge
        if(root.val == target){
            return new TreeNode[]{}; // ???
        }

        if (root == null) {
            return new TreeNode[]{null, null};
        }

        TreeNode[] splitNodes = new TreeNode[2]; // ????

        return splitNodes;
    }

    // ????
    private TreeNode[] splitHelper(TreeNode root, int target, TreeNode[] splitNodes){
        // edge
        // ???
        if(root == null || root.val == target){
            //return new TreeNode[]{}; // ???
            return new TreeNode[]{null, null};
        }

        // ???
        /**  case 1)
         *
         *   if `root.val <= target`
         *   -> move right
         *   -> but there may still in right sub tree < target ???
         *   -> need to split them and re-connect
         */
        if(root.val <= target){
            TreeNode[] res1 =  splitHelper(root.right, target, splitNodes);
            // ???
            //root.left = res1[0];
            root.right = res1[0];

            // /??
            return new TreeNode[]{root, res1[1]};
        }
        /**  case 2)
         *
         *   if `root.val > target`
         *   -> move left
         *   -> but there may still in left sub tree > target ???
         *   -> need to split them and re-connect
         */
        else{
            TreeNode[] res2 =  splitHelper(root.left, target, splitNodes);
            root.left = res2[1]; // ???
            // ???
            return new TreeNode[]{res2[0], root};
        }

     //   return splitNodes;
    }


    // LC 207
    // 7.32 - 42 am
    /**
     *  -> Return true if you can finish all courses.
     *  Otherwise, return false.
     *
     *  course:  [0, n-1]
     *
     *  prerequisites[i] = [ai, bi]
     *     -> MUST take bi first, then can take ai
     *
     *
     *
     *
     *
     *  --------------
     *
     *   IDEA 1) DFS + GRAPH + state ????
     *
     *
     *  --------------
     *
     */
    // 16.29 - 39 pm
    // IDEA 1) DFS + GRAPH + 3 state !!
    /**
     *  0: NOT visited
     *  1: visiting
     *  2: visited
     *
     */
    public boolean canFinish(int numCourses, int[][] prerequisites) {
        // edge

        // map: { course : [pre_1, pre_2, ... ] }
        Map<Integer, List<Integer>> map = new HashMap<>();
        for(int[] p: prerequisites){
            int ai = p[0];
            int bi = p[1];

            /**
             * prerequisites[i] = [ai, bi] indicates that you
             * must take course bi first if you want to take course ai.
             *
             *  e.g.:  bi -> ai
             *
             */

            // NOTE !!!
            // { pre: [next_1, next_2, ...] }
            // ??
//            if(!map.containsKey(ai)){
//                map.put(ai, new ArrayList<>());
//            }
//            List<Integer> list = map.get(ai);
//            list.add(bi);
//            map.put(ai, list);

            if(!map.containsKey(bi)){
                map.put(bi, new ArrayList<>());
            }
            List<Integer> list = map.get(bi);
            list.add(ai);
            map.put(bi, list);
        }

        int[] status = new int[numCourses];

        for(int i = 0; i < numCourses; i++){
            // NOTE !!!
            if(status[i] == 0){
                if(!courseHelper2(map, i, status)){
                    return false;
                }
            }
        }

        return true;
    }

    /**
     *  0: NOT visited
     *  1: visiting
     *  2: visited
     *
     */
    private boolean courseHelper2(Map<Integer, List<Integer>> map, int course, int[] status){
        // edge

        if(status[course] == 2){
            return true;
        }
        // ???
        if(status[course] == 1){
            return false;
        }

        // mark as `visiting`
        status[course] = 1;

        // visit next
        if(map.containsKey(course)){
            for(int next: map.get(course)){
                if(!courseHelper2(map, next, status)){
                    return false;
                }
            }
        }

        // mark as `visited` ???
        status[course] = 2;


        // ???
        return true;
    }













    public boolean canFinish_99(int numCourses, int[][] prerequisites) {
        // edge

        // map: { course: [ pre_1, pre_2, ... ] } // ???

        /** NOTE !!!
         *
         *  // map: { pre_course : [next_course_1, next_course_2, ...] }
         */


        Map<Integer, List<Integer>> map = new HashMap<>();
        for(int[] p: prerequisites){
            int ai = p[0];
            int bi = p[1];

            /**
             *     *  prerequisites[i] = [ai, bi]
             *      *     -> MUST take bi first, then can take ai
             */

//            if(!map.containsKey(ai)){
//                map.put(ai, new ArrayList<>());
//            }
//
//            List<Integer> list = map.get(ai);
//            list.add(bi);
//            map.put(ai, list);

            if(!map.containsKey(bi)){
                map.put(bi, new ArrayList<>());
            }

            List<Integer> list = map.get(bi);
            list.add(ai);
            map.put(bi, list);
        }

        System.out.println(">>> map = " + map);

        // NOTE !!!
        // we use `int array` to define node state
        int[] states = new int[numCourses];

        //Set<Integer> visited = new HashSet<>();

        // ??
        for(int i = 0; i < numCourses; i++){
            if(!courseHelper(map, i, states)){
                return false;
            }
        }


        return true;
    }

    // state:
    // 0: not visited
    // 1: visiting
    // 2: visited
    private boolean courseHelper(Map<Integer, List<Integer>> map, int course, int[] states){
        // edge
        // ??
        if(map.isEmpty()){
            return true; // ???
        }
        if(states[course] == 2){
            return true; // ???
        }
        if(states[course]  == 1){
            return false; // ???
        }


        // mark as `visiting` ???
        states[course] = 1;


        // visit next
        for(int next: map.get(course)){
            // mark as `visiting` ???
           // states[next] = 1;
            if(!courseHelper(map, next, states)){
                return false; // ???
            }
        }

        // need backtrack ???
        // but `state` is primary type ???

        // mark as visited
        states[course] = 2;


        return true;
    }


    // LC 210
    // 7.56 - 8.06 am
    /**
     *
     *  -> Return the ordering of courses you should take
     *  to finish all courses.
     *  If there are many valid answers, return any of them.
     *  If it is impossible to finish all courses,
     *  return an empty array.
     *
     *  -----------------
     *
     *   IDEA 1) TOPO SORT ??
     *     -> order arr
     *     -> add all `order=0` node to queue
     *     -> BFS
     *     -> update order, and iterate to next nodes
     *     -> return final orders
     *
     *
     *   IDEA 2) DFS ??? -> possible ???
     *
     *
     *  -----------------
     *
     */
    // IDEA 1) TOPO SORT ??
    /**
     *      *   IDEA 1) TOPO SORT ??
     *      *     -> order arr
     *      *     -> add all `order=0` node to queue
     *      *     -> BFS
     *      *     -> update order, and iterate to next nodes
     *      *     -> return final orders
     */
    //  IDEA 1) TOPO SORT ??
    // 16.51 - 17.01 pm
    public int[] findOrder(int numCourses, int[][] prerequisites) {
        // edge
        // edge
        if (numCourses == 0) {
            return null;
        }
        if (numCourses == 1) {
            return new int[] { 0 };
        }


        int[] degree = new int[numCourses]; // /??
        Arrays.fill(degree, 0); // ???

        // build map
        // { pre: [next_1, next_2, ...] }
        Map<Integer, List<Integer>> map = new HashMap<>();
        for(int[] p: prerequisites){
            /**
             * prerequisites[i] = [ai, bi] indicates
             * that you must take course bi first if you want to take course ai.
             *
             */
            int prev = p[1];
            int cur = p[0];

            // { pre: [next_1, next_2, ...] }
            if(!map.containsKey(prev)){
                map.put(prev, new ArrayList<>());
            }

            List<Integer> list = map.get(prev);
            list.add(cur);
            map.put(prev, list);

            // update degree ???
            degree[cur] += 1; // ????
        }

        // Topo sort ??
        Queue<Integer> q = new LinkedList<>();
        Set<Integer> visited = new HashSet<>(); // ???

        // add `degree=0` to q
        for(int i = 0; i < degree.length; i++){
            if(degree[i] == 0){
                q.add(i); // /??
                visited.add(i);
            }
        }

       // int[] res = new int[numCourses]; // ???
        List<Integer> collected = new ArrayList<>(); // ???

        while (!q.isEmpty()){
            int cur = q.poll();
            collected.add(cur); // ???
            // ??
            if(map.containsKey(cur)){
                for(int next: map.get(cur)){
                    if(!visited.contains(next)){
                        degree[next] -= 1; // ??
                        if(degree[next] == 0){
                            q.add(next);
                            visited.add(next); // ???
                        }
                    }
                }
            }
        }

        // edge
        if(collected.size() != numCourses){
            return null;
        }

        int[] res = new int[collected.size()];
        int j = 0;
        for(int x: collected){
           res[j] = x;
           j += 1;
        }


        return res;
    }












    public int[] findOrder_99(int numCourses, int[][] prerequisites) {
        // edge


        // indegree array
        int[] indegree = new int[numCourses];


        // init `order array`
        int[] orders = new int[numCourses];
        Arrays.fill(orders, 0); // init val as 0 ???



        // graph: pre -> list of next courses


        // build map
        /** NOTE !!!
         *
         *  // map: { pre_course : [next_course_1, next_course_2, ...] }
         */
        Map<Integer, List<Integer>> map = new HashMap<>();
        for(int[] p: prerequisites){
            int ai = p[0];
            int bi = p[1];

            // ???
            // build map
            /**
             *     *  prerequisites[i] = [ai, bi]
             *      *     -> MUST take bi first, then can take ai
             */
            if(!map.containsKey(bi)){
                map.put(bi, new ArrayList<>());
            }

            List<Integer> list = map.get(bi);
            list.add(ai);
            map.put(bi, list);

            // update `order` arr
            orders[ai] += 1; // ????
        }

        // visited ??? needed???
        boolean[] visited = new boolean[numCourses];


        // ??? collected order of course
        List<Integer> collected = new ArrayList<>();

        // add all `order=0` to queue
        Queue<Integer> q = new LinkedList<>();
        // ??
        for(int i = 0; i < orders.length; i++){
            if(orders[i] == 0){
                q.add(i);
                visited[i] = true; // ???
            }
        }

        while (!q.isEmpty()){
            // ??? visit by layer is needed ??
            //int size = q.size();
            Integer cur = q.poll();
            // add to list
            collected.add(cur);
            // visit next ???
            if(map.containsKey(cur)){
                for(int next: map.get(cur)){
                    // update order ???
                    orders[next] -= 1;
                    // ???
                    if(orders[next] == 0 && !visited[next]){
                        q.add(next);
                        visited[next] = true;
                    }
                }
            }
        }


        // ??? reverse
        //Collections.reverse(collected);

        int[] res = new int[collected.size()];
        for(int i = 0; i < collected.size(); i++){
            res[i] = collected.get(i);
        }


        return res; // ???
    }


    // LC 547
    // 8.46 - 10.15 am
    /**
     *  Return the total number of provinces.
     *
     *
     *  -----------
     *
     *  IDEA 1) UNION FIND ??
     *
     *  IDEA 2) DFS ???
     *
     *  -----------
     *
     *
     */
    //  IDEA 1) UNION FIND ??
    public int findCircleNum(int[][] isConnected) {
        // edge

        int n = isConnected.length; // ???

        MyUF myUF = new MyUF(n);

        // ???
        for(int y = 0; y < n; y++){
            // ??
            for(int x = 0; x < n; x++){
                int state = isConnected[y][x];
                // ???
                if(state == 1){
                    myUF.union(y, x);
                }
            }
        }

        return myUF.cluster; // ???
    }

    class MyUF{
        // ???
        int n;
        int[] parents;
        int cluster;

        MyUF(int n){
            this.n = n;
            this.parents = new int[this.n];
            // ???
//            for(int i = 0; i < this.parents.length; i++){
//                this.parents[i] = i; // ?? init node's parent as itself
//            }

            // NOTE !!! NO need to check all matrix,
            // just `half` matrix
            for(int i = 0; i < this.parents.length; i++){
                this.parents[i] = i; // ?? init node's parent as itself
            }

            //this.cluster = 0;
            this.cluster = n; // NOTE !!!
        }

        public void union(int x, int y){
            if(x == y){
                return;
            }

            int parentX = this.getParent(x);
            int parentY = this.getParent(y);

            // ???
            //this.parents[x] = parentY;
            // NOTE !!!
            // NOTE !!! ONLY `union` if different node
            if(parentX != parentY){
                this.parents[parentX] = parentY;
                this.cluster -= 1;
            }
        }

        public int getParent(int x){
            // ???
            int parent = this.parents[x];
            if(parent != x){
                //  parent = this.getParent(x);
               // return this.getParent(x);
                parent = this.getParent(parent);
            }
            // ???
            // NOTE !!! should use `parent`
            // or will cause `infinite` loop

            //parent = this.getParent(x);
            //parent = this.getParent(parent);
            this.parents[x] = parent; // ?????

            return parent;
        }

        public boolean isConnected(int x, int y){
            // /??
            if(x == y){
                return true;
            }
            int parentX = this.getParent(x);
            int parentY = this.getParent(y);

            return parentX == parentY; // ???
        }

    }


    // LC 444
    // https://leetcode.ca/all/444.html
    public boolean sequenceReconstruction(int[] nums, List<List<Integer>> sequences) {



        return false;
    }


    // LC 1020
    // 14.55 - 15.05 pm
    /**
     *  -> Return the `number` of land cells in grid
     *  for which we `cannot` walk off
     *  the boundary of the grid
     *  in any number of moves.
     *
     *   - m x n matrix
     *   -  0: sea
     *   -  1: land
     *
     *   can move 4 dirs
     *
     *
     *
     *
     *  ---------------
     *
     *   IDEA 1) 2 PASS DFS
     *
     *
     *
     *  ---------------
     *
     */
    // IDEA 1) 2 PASS DFS
    public int numEnclaves(int[][] grid) {
        // edge
        if(grid == null || grid.length == 0 || grid[0].length == 0){
            return 0;
        }

        int l = grid.length;
        int w = grid[0].length;

        // 1st pass: color from `boundary`
        for(int y = 0; y < l; y++){
            color(grid, 0, y, 3);
            color(grid, w-1, y, 3);
        }

        for(int x = 0; x < w; x++){
            color(grid, x, 0, 3);
            color(grid, x, l-1, 3);
        }


        int cnt = 0;

        // 2nd pass: color from `remaining 1`
        for(int y = 0; y < l; y++){
            for(int x = 0; x < w; x++){
                // /?
                if(grid[y][x] == 1){
                    //color(grid, x, y, -1);
                    cnt += color(grid, x, y, -1);
                }
            }
        }

        return cnt;
    }


    // ??? DFS
    private int color(int[][] grid, int x, int y, int color){
        // edge

        int l = grid.length;
        int w = grid[0].length;

        // ????
        if(x < 0 || x >= w || y < 0 || y >= l || grid[y][x] != 1){
            return 0;
        }

        //int[][] moves = new int[][] { {0,1}, {0,-1}, {1,0}, {-1,0} };
        // mark as visited
        grid[y][x] = color;

//        for(int[] m: moves){
//            int x_ = x + m[0];
//            int y_ = y + m[1];
//            if(x_ >= 0 && x_ < w && y_ >= 0 && y_ < l){
//                if(grid[y_][x_] == 1){
//                   return 1 + color(grid, x_, y_, color);
//                }
//            }
//        }

        return 1 + color(grid, x + 1, y, color)
                + color(grid, x - 1, y, color) +
                color(grid, x, y + 1, color) +
                color(grid, x, y - 1, color);

    }



    // LC 694
    // 15.21 - 31 pm
    /**
     *  IDEA 1) DFS + `path record`
     *
     *
     */
    //  IDEA 1) DFS + `path record`
    // 16.17 - 27 pm
    public int numDistinctIslands(int[][] grid) {
        // edge
        if (grid == null || grid.length == 0) return 0;


        HashSet<String> set = new HashSet<>();

        int l = grid.length;
        int w = grid[0].length;

        // ???
        for(int y = 0; y < l; y++){
            for(int x = 0; x < w; x++){
                if(grid[y][x] == 1){
                    // ???
                    String path = "";
                    String tmpIsland = getIslandNode(grid, x, y, path,"S");
                    set.add(tmpIsland);
                }
            }
        }

        // /??
        return set.size();
    }

    private String getIslandNode(int[][] grid, int x, int y, String path, String direction){
        // edge

        int l = grid.length;
        int w = grid[0].length;

        // validate
        // 1. Boundary and Base Case
        if (x < 0 || x >= w || y < 0 || y >= l || grid[y][x] != 1) {
            return ""; // ???
        }

        // NOTE !!!
        // mark as visited
        grid[y][x] = 0;
        // NOTE !!!
        path += direction; // ????

        // ???
        //path += grid
        getIslandNode(grid, x-1, y, path, "L");
        getIslandNode(grid, x+1, y, path, "R");
        getIslandNode(grid, x, y-1, path, "D");
        getIslandNode(grid, x, y+1, path, "U");

        // ???
        return path + "E"; // ????
    }








    //  IDEA 1) DFS + `path record`
    // ??
    // NOTE !! better to use set
    Map<String, Integer> islandMap = new HashMap();
    public int numDistinctIslands_99(int[][] grid) {
        // edge
        if (grid == null || grid.length == 0) return 0;

        int l = grid.length;
        int w = grid[0].length;

        for(int y = 0; y < l; y++){
            for(int x = 0; x < w; x++){
                // /?
                if(grid[y][x] == 1){
                    //color(grid, x, y, -1);
                    //cnt += color(grid, x, y, -1);
                    String island = distinctIsland(grid, x, y);
                    // ????
                    islandMap.put(island, islandMap.getOrDefault(island, 0) + 1);
                }
            }
        }


        System.out.println(">>> islandMap = " + islandMap);

        return islandMap.size(); // ????
    }

    // ??? serialization + path collection ???
    private String distinctIsland(int[][] grid, int x, int y){
        // ??

        int l = grid.length;
        int w = grid[0].length;

        // ????
        if(x < 0 || x >= w || y < 0 || y >= l || grid[y][x] != 1){
            return null; // ???
        }

        // ??? mark as `visited
        grid[y][x] = -1;

        // ???
        //path += grid[y][x];

        return "<" +  1 + distinctIsland(grid, x + 1, y)
                + distinctIsland(grid, x - 1, y) +
                distinctIsland(grid, x, y + 1) +
                distinctIsland(grid, x, y - 1) + ">";

    }


    // LC 130
    // 15.49 - 16.09 pm
    /**
     *  -> capture regions that are surrounded
     *
     *  -----------
     *
     *   IDEA 1) 2 PASS DFS
     *
     *    -> step 1) collect all `boundary` O, make it as '#'
     *       step 2) loop over remaining `O`, make it as 'X'
     *       step 3) loop overr `#`, make it as 'O`
     *
     *
     *   -----------
     *
     *
     *
     */
    public void solve(char[][] board) {

    }

    // LC 392
    // 16.14 - 24 pm
    /**
     *  ->
     *  return true if `s` is a
     *  subsequence of `t`, or false otherwise.
     *
     *
     *  ---------------
     *   IDEA 1) 2 POINTERS ???
     *
     *  ---------------
     *
     */
    // IDEA 1) 2 POINTERS ???
    public boolean isSubsequence(String s, String t) {
        // edge
        if(s == null && t == null){
            return true;
        }
        if(s.equals(t)){
            return true;
        }
        if(t == null && s != null){
            return false;
        }

        int l1 = s.length();
        int l2 = t.length();

        int i = 0;
        int j = 0;

        // ???
        while (j < l2){
            // case 1) equals
            if(s.charAt(i) == t.charAt(j)){
                // early quit ??
                if(i == l1 - 1){
                    return true;
                }
                i += 1;
            }
            // NOTE !!! need to move j anyway ??
            j += 1;
        }

        return false; // ???
    }



    // LC 865
    // 17.19 - 29 pm
    /**
     *  -> Return the smallest subtree
     *  such that it contains all the
     *  deepest nodes in the original tree.
     *
     *
     *  -----------------
     *
     *
     *  -----------------
     *
     */
    // 10.30 - 40 am
    /**
     *
     *  -> Return the `smallest` subtree such that it
     *     contains `all the deepest nodes`
     *     in the `original tree.`
     *
     *   - the `depth` of each node
     *        is the `shortest distance` to the `root.`
     *
     *
     *   - A node is called the `deepest`
     *     if it has the `largest` depth
     *     possible among any node in
     *     the entire tree.
     *
     *
     *  ----------------
     *
     *
     *   IDEA 1) DFS ??
     *     - post order  ????
     *     (left -> right -> root)
     *
     *     -> find `deepest` node
     *     -> find the `smallest` parent node ???
     *
     *  IDEA 1) DFS + path collection ???
     *
     *  ----------------
     *
     */
    // 12. 41 - 12.56 pm
    /**
     * -> Return the `smallest` subtree such
     *    that it contains all the deepest
     *    nodes in the original tree.
     *
     *  ------------
     *
     *   IDEA 1) LCA ???
     *    -> the lowest common ancestor ??
     *
     *
     *   NOTE !!! CAN'T just use regular dfs ...
     *
     *
     *  ------------
     *
     */
    // IDEA 1) CUSTOM CLASS + LCA + DFS ???
    // 13.43 - 53 am
    class MyNode5{
        TreeNode node;
        int depth; // ???

        MyNode5(){
        }

        MyNode5(TreeNode node, int depth){
            this.node = node;
            this.depth = depth;
        }

    }


    public TreeNode subtreeWithAllDeepest(TreeNode root) {
        // edge
        if(root == null){
            return null;
        }
        if(root.left == null && root.right == null){
            return root;
        }

        // ??


        // ???
        MyNode5 res = DepthHelper(root);
        // /???
        return res.node;
    }

    private MyNode5 DepthHelper(TreeNode root){
        // edge
        // base: return depth = 0 for null
        if (root == null) {
            return new MyNode5(null, 0);
        }

//        // ??
//        MyNode5 _left = DepthHelper(root.left, depth + 1);
//        MyNode5 _right = DepthHelper(root.right, depth + 1);
        // ??
        MyNode5 _left = DepthHelper(root.left);
        MyNode5 _right = DepthHelper(root.right);

        // ??
        if(_left.depth > _right.depth){
            //return DepthHelper()
            //return _left; ///???
            return new MyNode5(_left.node, _left.depth + 1);
        }else if(_left.depth < _right.depth){
           // return _right;
            return new MyNode5(_right.node, _right.depth + 1);
        }

        // ???
        return new MyNode5(root, _left.depth +1);
    }







    // 12.26 - 36 pm
    // IDEA: LCA + CUSTOM CLASS
    class MyRes{
        TreeNode node;
        int depth; // /???

        MyRes(TreeNode node, int depth){
            this.node = node;
            this.depth = depth;
        }
    }


    public TreeNode subtreeWithAllDeepest_97(TreeNode root) {
        // edge
        if(root == null){
            return null;
        }
        if(root.left == null && root.right == null){
            return root;
        }

        // ???
        //MyRes res = dfsHelper3(root);
        // ?????
        //return res.node;
        return dfsHelper3(root).node;
    }


    private MyRes dfsHelper4(TreeNode root){
        // edge ???
        if(root == null){
            // ????
            //  return myRes; // ??? //new MyRes(root, 0); // ???
            return new MyRes(root, 0); // ????
        }

        MyRes _left = dfsHelper4(root.left);
        MyRes _right = dfsHelper4(root.right);

        // ???
        int maxDepth = Math.max(_left.depth, _right.depth);

        if(_left.depth > _right.depth){
            return new MyRes(root.left, maxDepth + 1);
        }else{
            if(_left.depth < _right.depth){
                return new MyRes(root.right, maxDepth + 1);
            }
        }

        return new MyRes(root, maxDepth + 1);
    }




    // ????
    private MyRes dfsHelper3(TreeNode root){
        // edge ???
        if(root == null){
            // ????
          //  return myRes; // ??? //new MyRes(root, 0); // ???
            return new MyRes(root, 0); // ????
        }

        // ??
        MyRes _left = dfsHelper3(root.left);
        MyRes _right = dfsHelper3(root.right);

        // ???
        int currentMaxDepth = Math.max(_left.depth, _right.depth);

        // ???
        //int depth = myRes.depth;

        // ???
        if(_left.depth > _right.depth){
            return dfsHelper3(root.left);
        }else if(_left.depth < _right.depth){
            return dfsHelper3(root.right);
        }

        // if left, right sub tree have same depth,
        // cur node is LCA
        //return myRes;

        return new MyRes(root, currentMaxDepth); // /??
    }











    // IDEA 1) LCA ??? Lowest Common Ancestor (LCA
    TreeNode deepestNode = null; // /??
    int maxDepth = 0; // /??
    public TreeNode subtreeWithAllDeepest_95(TreeNode root) {
        // edge
        if(root == null){
            return null;
        }
        if(root.left == null && root.right == null){
            return root;
        }


        LCAHelper(root, 0);

        return deepestNode;
    }

    private int LCAHelper2(TreeNode root, int depth){
        // edge
        if(root == null){
            return 0; // ????
        }

        int _left = LCAHelper2(root.left, depth + 1);
        int _right = LCAHelper2(root.right, depth + 1);

        // ???
        if(_left == _right){
            // ???
            deepestNode = root;
            return depth; // ????
        }

        // ???
        if(_left > _right){
            // search on left
        }else{
            // search on right
        }

        // ??? do we `really use` the return val ???
        //return root;


        return depth;
    }






    // ???
    // POST order DFS ???? (left -> right -> root )
    private TreeNode LCAHelper(TreeNode root, int depth){
        // edge
        if(root == null){
            return null; // ????
        }

        TreeNode _left = LCAHelper(root.left, depth + 1);
        TreeNode _right = LCAHelper(root.right, depth + 1);

        // ???
        if(_left == null && _right == null){
            if(depth > maxDepth){
                maxDepth = depth;
                deepestNode = root; // ????
            }
        }

        // ??? do we `really use` the return val ???
        return root;
    }









    // map { node: path }
    //Map<TreeNode, String> nodePath = new HashMap<>();
    int deepest = 0;
    TreeNode resNode = null;
    public TreeNode subtreeWithAllDeepest_99(TreeNode root) {
        // edge
        if(root == null){
            return null;
        }
        // ???
        if(root.left == null && root.right == null){
            // ???
            return root;
        }


       // int deepest = 0;
        getPath(root, null, 0);

        return resNode; // ???
    }


    // ??? pre-order DFS ????
    private void getPath(TreeNode root, TreeNode parent, int depth){
        // edge
        if(root == null){
            return;
        }
        // ???
        if(root.left == null && root.right == null){
            // ???
            if(depth > deepest){
                resNode = parent;
                deepest = depth;
            }
        }

        getPath(root.left, root, depth + 1);
        getPath(root.right, root, depth + 1);
    }








    // LC 399
    // 7.55 - 8.12 am
    /**
     *
     *  -> Return the answers to all queries.
     *  If a single answer cannot be determined, return -1.0.
     *
     *
     *  -------------
     *
     *   IDEA 1) DFS
     *
     *
     *
     *  -------------
     *
     *
     *
     */
    // 11.35 - 45 AM
    /**
     *
     *  -> Return the answers to all queries.
     *  If a single answer cannot be determined, return -1.0.
     *
     *
     *  -----------
     *
     *  IDEA 1) DFS + HASHMAP
     *
     *   if ai / bi = val
     *
     *  ->  hashmap: {  ai : [ bi, val], ..... }
     *
     */
    // ???
    class MyInfo{
        String ai;
        String bi;
        double val;

        MyInfo(){
        }

        MyInfo(String ai, String bi, double val){
            this.ai = ai;
            this.bi = bi;
            this.val = val;
        }
    }


    class Node2 {
        String name;
        double val;

        Node2(String name, double val) {
            this.name = name;
            this.val = val;
        }
    }

    // DFS
    public double[] calcEquation(List<List<String>> equations, double[] values, List<List<String>> queries) {

        /** NOTE !!!
         *
         *  the graph structure
         *
         *  Map<String, Map<String, Double>>
         *
         *  so
         *     string1 = a
         *     string2 = b
         *     Double = a / b
         */
        // build graph
        Map<String, Map<String, Double>> graph = new HashMap<>();

        for (int i = 0; i < equations.size(); i++) {
            String a = equations.get(i).get(0);
            String b = equations.get(i).get(1);
            double val = values[i];

            graph.putIfAbsent(a, new HashMap<>());
            graph.putIfAbsent(b, new HashMap<>());

            graph.get(a).put(b, val);
            graph.get(b).put(a, 1.0 / val);
        }

        double[] res = new double[queries.size()];

        for (int i = 0; i < queries.size(); i++) {
            String start = queries.get(i).get(0);
            String end = queries.get(i).get(1);

            /** NOTE !!!
             *
             *   we can `pre-calculate` per below cases
             *
             *    1. map NOT contains key (!graph.containsKey(start) || !graph.containsKey(end))
             *    2. start == end
             */
            if (!graph.containsKey(start) || !graph.containsKey(end)) {
                res[i] = -1.0;
            } else if (start.equals(end)) {
                res[i] = 1.0;
            } else {
                /** NOTE !!!
                 *
                 *   1. we init `visited` before every DFS call
                 *   2. dfs res as res[i] val
                 */
                Set<String> visited = new HashSet<>();
                res[i] = dfs_0(graph, start, end, 1.0, visited);
            }
        }

        return res;
    }

    /** NOTE !!!
     *
     *  we pass `product` as DFS param
     */
    private double dfs_0(Map<String, Map<String, Double>> graph,
                         String cur,
                         String target,
                         double product,
                         Set<String> visited) {

        // edge
//        if(visited.contains(cur)){
//            return 1.0; // ???
//        }



        visited.add(cur);

        Map<String, Double> neighbors = graph.get(cur);

        if (neighbors.containsKey(target)) {
            return product * neighbors.get(target);
        }

        for(String next: neighbors.keySet()){
            // ???
            if(!visited.contains(next)){
                double res = dfs_0(graph,
                        next,
                        target,
                        //product * graph.get(neighbors),
                        product * neighbors.get(next),
                        visited);

                if(res != -1.0){
                    return res;
                }
            }



            return -1.0;
        }






//        if(target.equals(cur)){
//            return product; // ???
//        }
//
//        // ???
//        Map<String, Double> values = graph.get(cur);
//       // product = product * values.values()[0]; // ???
//
//

        return 0.0;
    }





    // BFS
//    public double[] calcEquation(List<List<String>> equations, double[] values, List<List<String>> queries) {
//        // edge
//
//        // build map
//        /**
//         *  *  IDEA 1) DFS + HASHMAP
//         *
//         *   if ai / bi = val
//         *
//         *  ->  hashmap: {  ai : [ bi, val], ..... }
//         *
//         */
//        Map<String, List<MyInfo>> map = new HashMap<>();
//        for(int i = 0; i < equations.size(); i++){
//            List<String> list = equations.get(i);
//            String ai = list.get(0);
//            String bi = list.get(1);
//            double val = values[i]; // ??
//
//            // ??
//            if(!map.containsKey(ai)){
//                //map.put(ai, new MyInfo()); // ???
//                map.put(ai, new ArrayList<>()); // ???
//            }
//            List<MyInfo> tmpList = map.get(ai);
//            tmpList.add(new MyInfo(ai, bi, val));
//            map.put(ai, tmpList);
//
//            // NOTE !!!
//            //  Your map must store both directions. If $a/b = 2.0$, then $b/a = 0.5$
//
//        }
//
//        //List<Double> collected = new ArrayList<>();
//        double[] res = new double[queries.size()];
//        // ??? fill with -1.0
//        Arrays.fill(res, -1.0);
//
//        for(int i = 0; i < queries.size(); i++){
//            String ai = queries.get(i).get(0);
//            String bi = queries.get(i).get(1);
//            // ???
//            if(!map.containsKey(ai) || !map.containsKey(bi)){
//                continue;
//            }
//            // ????
//            res[i] = bfsCal(ai, bi, map, 1.0);
//        }
//
//
//        return res;
//    }
//
//    private double dfsCal(String ai, String bi){
//
//
//        return 0.0;
//    }


    // BFS State:
    // The Queue should store the "current node" and the "product so far."

    // Visited Set: BFS (and DFS) in a graph must use a
    // visited set to avoid infinite loops (cycles like $a \to b \to a$).

    // Edge Case: If the start or end variable in a query
    // has never been seen in the equations, the answer is immediately -1.0.

    // /??
    private double bfsCal(String ai, String bi, Map<String, List<MyInfo>> map, double cur){
        // edge
        if(ai == bi){
            return 1.0;
        }

       Queue<MyInfo> q = new LinkedList<>();
       if(map.containsKey(ai)){
            for(MyInfo info: map.get(ai)){
                q.add(info); // /??
            }
        }


        while (!q.isEmpty()){

        }


        return 0.0;
    }












    //  IDEA 1) DFS
    public double[] calcEquation_98(List<List<String>> equations, double[] values, List<List<String>> queries) {
        // edge

        // map
        // { string : val }
        // e.g. {a/b : 1.5, b/c: 2.5,. ... }

        Map<String, Double> map = new HashMap<>();
        for(int i = 0; i < equations.size(); i++){
            List<String> e =  equations.get(i);
            String a = e.get(0);
            String b = e.get(1);

            double val = values[i];

            // ??
            map.put(a + b, val);
            map.put(b + a,  1 / val);
        }

        System.out.println(">>> map = " + map);

        // ???
        double[] res = new double[queries.size()];
        // List<List<String>> queries
        for(int i = 0; i < equations.size(); i++){
            List<String> e =  equations.get(i);
            String a = e.get(0);
            String b = e.get(1);

            // ??
            res[i] = dfsDivision(map, a, b, 1.0);
        }


        return res;
    }


    private double dfsDivision(Map<String, Double> map, String x, String y, double result){
        // edge
        if(!map.containsKey(x) || !map.containsKey(y)){
            return -1.0;
        }

        if(map.containsKey(x+y)){
            return map.get(x+y);
        }

        if(map.containsKey(y+x)){
            return map.get(y+x);
        }

        // ???
       //double result = map.

        return -1.0;
    }



    // ???
    private String multiply(String s1, String s2){
        return null;
    }


    // LC 700
    // 6.53 - 7.03 am
    /**
     *  ->
     *  Find the node in the BST that the node's value equal
     *  s val and return the subtree rooted with that node.
     *  If such a node does not exist, return null.
     *
     *
     *  -------------
     *
     *  IDEA 1) BST + DFS
     *
     *
     */
    // IDEA 1) BST + DFS
    TreeNode res;
    public TreeNode searchBST(TreeNode root, int val) {
        // edge
        if(root == null){
            return null;
        }

        findNodeHelper(root, val);

        return res;
    }

    private void findNodeHelper(TreeNode root, int val){
        if(root == null){
            return; // null;
        }
        if(root.val == val){
            //return root;
            res = root;
        }
        if(root.val < val){
           // return searchBST(root.left, val);
            searchBST(root.right, val);
        }else{
            //return searchBST(root.right, val);
            searchBST(root.left, val);
        }

    }


    // LC 310
    // 7.08 - 18 am
    /**
     *
     *  -> Return a list of all ` MHTs'` root labels.
     *  You can return the answer in any order.
     *
     *
     *   -  Among all possible rooted trees, those with
     *      minimum height (i.e. min(h))
     *      are called minimum height trees (MHTs).
     *
     *
     *  --------------
     *
     *   IDEA 1) DFS ???
     *
     *   IDEA 2) UNION FIND ????
     *
     *     -> loop over nodes, make every node as root,
     *        and re-connect / `re-root` with the new root ????
     *
     *
     *  --------------
     *
     */
    //  IDEA 2) UNION FIND ????
    public List<Integer> findMinHeightTrees(int n, int[][] edges) {
        // edge

        // get nodes

        // ????
        int minDepth = Integer.MAX_VALUE;

        // tmp: { [node, depth] }
        List<Integer[]> tmp = new ArrayList<>();

        // res: { [node] }
        List<Integer> res = new ArrayList<>();

        for(int i = 0; i < n; i++){
            // ???
            MyUF2 uf2 = new MyUF2(n, edges);
            uf2.setRoot(i);

            int depth = uf2.getMinDepth();
            minDepth = Math.min(minDepth, depth);
            tmp.add(new Integer[]{i, depth});
        }

        for(Integer[] x: tmp){
            if(x[1] == minDepth){
                res.add(x[0]);
            }
        }

        return res;
    }


    class MyUF2{
        // ??
        int n;
        int[] parents;
        int[][] edges; // ???


        MyUF2(int n, int[][] edges){
            this.n = n;
            this.parents = new int[n];
            // ?? init itself as parent at first stage
            // ...

            this.edges = edges;
        }

        public void union(int x, int y){

        }

        public int getParent(int x){
            return 0;
        }

        public boolean isSameParent(int x, int y){
            return false;
        }

        // ???
        public int getMinDepth(){
            return 0; // ???
        }

        // ???
        private void setRoot(int x){

        }


    }


    // LC 133
    // 8.22 - 32 am
    /**
     *
     *   -> Return a deep copy (clone) of the graph.
     *
     *
     *   ------------------
     *
     *
     *    IDEA 1) DFS ?? RECURSION
     *
     *
     *   ------------------
     *
     */
    // 15.12 - 22 pm
    /**
     *   Return a deep copy (clone) of the graph.
     *
     *   ------
     *
     *   IDEA 1) DFS
     *
     *
     *
     */
    // helper class
    class Node {
        public int val;
        public List<Node> neighbors;
        public Node() {
            val = 0;
            neighbors = new ArrayList<Node>();
        }
        public Node(int _val) {
            val = _val;
            neighbors = new ArrayList<Node>();
        }
        public Node(int _val, ArrayList<Node> _neighbors) {
            val = _val;
            neighbors = _neighbors;
        }
    }


    // ???
    private Map<Node, Node> visited3 = new HashMap<>();

    public Node cloneGraph(Node node) {
        // /???
        if(node == null){
            return null;
        }

       // cloneHelper3(node);
        // ????
        return cloneHelper3(node);
    }

    private Node cloneHelper3(Node node){
        // /???
        if(node == null){
            return null;
        }
        // ???
        if(visited3.containsKey(node)){
            return visited3.get(node); // ???
        }

        // ???
        Node clonedNode = new Node(node.val);


        // NOTE !!
        // // 2. Store the mapping BEFORE recursing into neighbors
        // update visited
        visited3.put(node, clonedNode); // ???

        if(node.neighbors != null){
            for(Node next: node.neighbors){
                clonedNode.neighbors.add(cloneHelper3(next));
            }
        }


        return clonedNode;
    }










    // main func
    // IDEA 1) DFS ?? RECURSION
    private Map<Node, Node> visited = new HashMap<>();

    public Node cloneGraph_96(Node node) {
        // edge
        if(node == null){
            return null;
        }
        if(node.neighbors == null){
            return node;
        }

        // NOTE !!!
        // 2. If we have already cloned this node, return the clone
        if (visited.containsKey(node)) {
            return visited.get(node);
        }


        // ???
        //Node copiedNode = new Node(node.val);
       //Node copiedNode = new Node();
//       Node copiedNode = copyHelper(node);
//       return copiedNode;

       return copyHelper(node);
    }


    // ???
    private Node copyHelper(Node node){
        // edge ???
        if(node == null){
            return null;
        }

        // ????
        Node copiedNode = new Node(node.val);


        // visit children
        // ??
        if(node.neighbors != null){
            copiedNode.neighbors = new ArrayList<>(); // ????
            for(Node next: node.neighbors){
                // ???
                //copyHelper(next, copiedNode); // ???
                copiedNode.neighbors.add(copyHelper(next));
            }
        }


        return copiedNode;
    }



    // LC 508
    // 10.50 - 11.00 am
    /**
     *  -> Given the root of a binary tree,
     *  return the most frequent subtree sum.
     *  If there is a tie, return all the values
     *  with the highest frequency in any order
     *
     *
     *  The subtree sum of a node is defined as the sum of
     *  all the node values formed by the subtree rooted
     *  at that node (including the node itself).
     *
     *
     *  --------------------
     *
     *  IDEA 1) DFS + PREFIX SUM ???
     *
     */
    // IDEA 1) pre-order DFS + PREFIX SUM ???
    // map: { sum : [node_1, node_2, ...] }
    // ????
    Map<Integer, List<Integer>> nodeSumMap = new HashMap<>();
    public int[] findFrequentTreeSum(TreeNode root) {
        // edge
        if(root == null){
            return new int[]{}; // ???
        }


        nodeSumHelper(root,  0);

        //int maxCnt = 0; // ?
        List<Integer> tmp = new ArrayList<>();
        for(List<Integer> list : nodeSumMap.values()){
            if(list.size() > tmp.size()){
                tmp = list;
            }
        }

        if(tmp.isEmpty()){
            return null;
        }

        int[] res = new int[tmp.size()];
        // ??
        for(int i = 0; i < tmp.size(); i++){
            res[i] = tmp.get(i);
        }


        return res;
    }

    // post-order DFS  ???
    private void nodeSumHelper(TreeNode root, int curSum){
        if(root == null){
            return;
        }

        /**  NOTE !!! sub tree sum should be as below:
         *
         * // Post-order: children first, then self
         *         int leftSum = calculateSubtreeSum(node.left);
         *         int rightSum = calculateSubtreeSum(node.right);
         *
         *         int totalSum = node.val + leftSum + rightSum;
         *
         *
         *
         */

        // ??
        nodeSumHelper(root.left, curSum);
        nodeSumHelper(root.right, curSum);


        // ???
        updateMap(nodeSumMap, root.val);
        curSum += root.val;
        updateMap(nodeSumMap, curSum);
    }


    private Map<Integer, List<Integer>> updateMap(Map<Integer, List<Integer>> nodeSumMap, int val){

        if(!nodeSumMap.containsKey(val)){
            nodeSumMap.put(val, new ArrayList<>());
        }
        List<Integer> list = nodeSumMap.get(val);
        list.add(val);
        nodeSumMap.put(val, list);

        return nodeSumMap;

    }


    // LC 1137
    // 7.43 - 53 am
    /**
     *  -> Given n, return the value of Tn.
     *
     *  - Tribonacci sequence Tn
     *    is defined as follows:
     *
     *     T0 = 0, T1 = 1, T2 = 1,
     *     Tn+3 = Tn + Tn+1 + Tn+2 for n >= 0.
     *
     *
     *  -----------------
     *
     *   IDEA 1) 1D DP
     *      - dp def:
     *
     *          dp[i]: Tn with idx = i
     *
     *      - dp eq:
     *         dp[Tn+3] = dp[Tn] + dp[Tn+1] + dp[Tn+2] for n >= 0.
     *
     *   IDEA 2) BRUTE FORCE
     *
     *
     *  -----------------
     *
     *
     */
    public int tribonacci(int n) {
        // edge
        if (n == 0) return 0;
        if (n == 1 || n == 2) return 1;


        int[] dp = new int[n+1]; // ??
        dp[0] = 0;
        dp[1] = 1;
        dp[2] = 1;

        // ???
        for(int i = 3; i <= n; i++){
            dp[i] = dp[i-3] + dp[i-2] + dp[i-1];
            System.out.println(">>> i = " + i + ", " +
                    " dp = " + dp[i-3] + dp[i-2] + dp[i-1]);
        }

        return dp[n]; // ???
    }



    // LC 509
    // 8.00 - 10 am
    /**
     *  1D DP
     *
     *
     */
//    public int fib(int n) {
//        // edge
//        if(n <= 1){
//            return n;
//        }
//
//        //Map<Integer, Integer> map = new HashMap<>();
//
//        int[] dp = new int[ + 1]; // ???
//        dp[0] = 0;
//        dp[1] = 1;
//        dp[2] = 1;
////
////        map.put(0,0);
////        map.put(1,1);
////        map.put(2,1);
//
//
//        for(int i = 2; i <= n; i++){
//            dp[i] = dp[i-2] + dp[i-1];
//        }
//
//        return dp[n];
//    }



    public int fib_99(int n) {
        // edge
        if(n <= 1){
            return n;
        }
        int[] dp = new int[ + 1]; // ???
        dp[0] = 0;
        dp[1] = 1;
        dp[2] = 1;


        for(int i = 2; i <= n; i++){
            dp[i] = dp[i-2] + dp[i-1];
        }

        return dp[n];
    }




    // LC 213
    // 8.12 -22 AM
    /**
     *
     *   ->  return the maximum amount of money you
     *   can rob tonight without alerting the police.
     *
     *
     *   -  All houses at this place are arranged in a circle
     *
     *
     *  --------------
     *
     *  IDEA 1) 1D DP + 2 CASES
     *
     *   - case 1) rob 0 idx
     *   - case 2) rob last (n-1) idx
     *
     *
     *  --------------
     *
     *
     *
     */
    // 10.45 - 55 pm
    /**
     *      *  IDEA 1) 1D DP + 2 CASES + helper func
     *      *
     *      *   - case 1) rob 0 idx
     *      *   - case 2) rob last (n-1) idx
     *
     */
    public int rob(int[] nums) {
        // edge
        int n = nums.length;
        if(n == 1){
            return nums[0];
        }

        //int n = nums.length;
        int v1 = robHelper(nums, 0, n-2);
        int v2 = robHelper(nums, 1, n-1);

        return Math.max(v1, v2);
    }

    private int robHelper(int[] nums, int start, int end){
        // ??

        // NOTE !!!
        int len = end - start + 1;
        int n = nums.length;
        //int[] dp = new int[n + 1];
        int[] dp = new int[len];

        // edge
        if(start == len){
            return nums[start];
        }

        // ??
        dp[start] = nums[start]; // ???
        //dp[start + 1] = nums[start]; // ???
        // NOTE !!!
        dp[start + 1] = Math.max(nums[start], nums[start + 1]); // ???



        for(int i = start + 1; i < end; i++){
            // ???
            dp[i] = Math.max(dp[i-2] + nums[i], dp[i-1]);
        }


        return dp[end]; // ????
    }





    // IDEA 1) 1D DP + 2 CASES
    public int rob_07(int[] nums) {
        // edge
        int n = nums.length;
        if(n == 1){
            return nums[0];
        }

        // ??
        int maxVal = 0;
        /**
         *      *  IDEA 1) 1D DP + 2 CASES
         *      *
         *      *   - case 1) rob 0 idx
         *      *   - case 2) rob last (n-1) idx
         *      *
         */
       // int[] dp1 = new int[n + 1]; // ???
        int[] dp1 = new int[n]; // ???

        dp1[0] = nums[0];
        dp1[1] = nums[0]; // CAN'T rob idx = 1
        dp1[2] = Math.max(dp1[0] + nums[2], dp1[1]); // ???

        // ??? i < n, since we CAN'T run n-1 idx anyway ????
        for(int i = 2; i < n; i++){
            dp1[i] = Math.max(dp1[i-2] + nums[i], dp1[i-1]);
            maxVal = Math.max(dp1[i], maxVal);
        }


        //int[] dp2 = new int[n + 1]; // ???
        int[] dp2 = new int[n]; // ???

        dp2[0] = 0;
        dp2[1] = Math.max(dp2[0], nums[1]);  // ????
        dp2[2] = Math.max(dp2[0] + nums[2], dp2[1]);  // ????

        for(int i = 2; i <= n; i++){
            dp2[i] = Math.max(dp2[i-2] + nums[i], dp2[i-1]);
            maxVal = Math.max(dp2[i], maxVal);
        }


        return maxVal;
    }

    // LC 91
    // 10.38 - 54 am
    /**
     *  -> Given a string s containing only digits,
     *     return the number of ways to decode it.
     *     If the entire string cannot be decoded
     *     in any valid way, return 0.
     *
     *  ----------------
     *
     *   IDEA 1) 2D dp ???
     *
     *     - DP def:
     *         dp[i, j] = # of `valid` decoded way
     *                    in idx = [i,j]
     *
     *     - DP eq:
     *          // ???
     *          dp[i,j] = dp[i-1, j] + dp[i,j]
     *
     *
     *
     *      NOTE:  mapping relation !!
     *         "1" -> "A"
     *         ...
     *         "26" -> "Z"
     *
     *         so ONLY 1 -> 26 range
     *         so 1 - 2 digits range
     *
     *
     *  ----------------
     *
     *   ex 1)
     *    "11"
     *
     *     ->  1,1
     *         11
     *
     *   "110"
     *
     *     -> 1,10
     *
     *
     *   "1110"
     *
     *    ->  1,1,10
     *        11,10
     *
     *
     *
     */
    // 10.21 - 10.31 am
    /**
     *   IDEA 1) 1D DP
     *      - 1 digit
     *      - 2 digit
     *
     *      - DP def
     *         - dp[i]: num of decode way for s[0, i]
     *                  (sub string from 0 to i)
     *
     *      - DP eq
     *          if 1 digit:
     *
     *             if( 1 <= val <= 26):
     *                dp[i] = dp[i-1]
     *
     *          if 2 digit:
     *             if( 1 <= val <= 26):
     *
     *               dp[i] = dp[i-2]
     *
     *
     */
    // 11.17 - 27 am
    /**
     *
     *   1D DP
     *
     *    - 1-digit
     *    - 2-digit
     *
     *    - DP def
     *
     *       - dp[i] = decode way for substring of s: [0, i]
     *
     *    - DP eq
     *
     *       - dp[i] =
     *
     *         - 1-digit
     *
     *         - 2-digit
     *
     */
    // 13.06 - 16 pm
    //  1D DP
    // 1-digit, 2-digit
    public int numDecodings(String s) {
        // edge
        if (s == null || s.length() == 0 || s.charAt(0) == '0') {
            return 0;
        }

        // ?
        int n = s.length();
        // dp def: decode way of string with len = i  ???
        int[] dp = new int[n + 1]; // ???

        dp[0] = 1; // ??
        dp[1] = 1;
        // ??
        //dp[2] = 1; // ???

        // ??
        for(int i = 2; i < n + 1; i++){

            // ???
            // int oneDigit = Integer.parseInt(s.substring(i - 1, i));
            //int val1 = s.charAt(i) - 'a';

            // V1
            //int val1 = s.charAt(i) - '0';
            // V2
            int val1 = Integer.parseInt(s.substring(i-1, i));


            int val2 = Integer.parseInt(s.substring(i-2, i));

            // 1-digit
            if(val1 >= 1 && val1 <= 9){
                dp[i] += dp[i-1];
            }

            // 2-digit
            if(val2 >= 10 && val2 <= 26){
                dp[i] += dp[i-2];
            }
        }

        return dp[n];
    }









    public int numDecodings_96(String s) {
        // edge
        if (s == null || s.length() == 0 || s.charAt(0) == '0') {
            return 0;
        }

        int n = s.length();

        // - dp[i] = `total decode way` for substring of s: [0, i]
        int[] dp = new int[n];

//        dp[0] = 1; // 1 - 9
//        dp[1] = 17;  // 11 -> 26 ???

        dp[0] = 1; // empty string
        dp[1] = 1; // first char already checked != '0'

        // ???
        for(int i = 2; i < n; i++){

            int val1 = new Integer(s.charAt(i));
            int val2 = new Integer(s.substring(i-2, i)); /// ????

            // 1-digit ???
            if(val1 >= 1 && val1 <= 9){
                // ????
                dp[i] += dp[i-1];
            }

            // 2-digit
            if(val2 >= 10 && val2 <= 26){
                // ????
                dp[i] += dp[i-2];
            }
        }


        return dp[n-1]; // ???
    }






    // IDEA 1) 1D DP
    public int numDecodings_90(String s) {
        // edge ???
        // Edge case: empty string or starts with '0' → invalid
        if (s == null || s.length() == 0 || s.charAt(0) == '0') {
            return 0;
        }

        int len = s.length();

        // Because dp[i] represents first i characters, not index i.
        //int[] dp = new int[len]; // ???
        int[] dp = new int[len + 1]; // ???

        //dp[0] = 0;
        dp[0] = 1;
        dp[1] = 1;
        // ??? dp[2] ?

        for(int i = 2; i < len + 1; i++){
            // get val
            int val1 = new Integer(s.charAt(i));

            // ???
            //int val2 = new Integer(s.substring(i-1, i));
            int val2 = new Integer(s.substring(i-2, i));

            // case 1) 1 digit
            // ???
            if(val1 > 0 && val1 <= 9){
               // dp[i] = dp[i-1]; // ???
                dp[i] += dp[i-1];
            }

            // case 2) 2 digit
            if(val2 >= 10 && val2 <= 26 && !s.substring(i-1, i).startsWith("0")){
                //dp[i] = dp[i-2]; // ???
                dp[i] += dp[i-2]; // ???
            }
        }

        return dp[len - 1];  // ???
    }








    public int numDecodings_99(String s) {

        return 0;
    }


    // LC 639
    // 11.40 - 50 AM
    /**
     *
     * Given a string s consisting of digits
     *  and '*' characters,
     *
     * -> return the number of ways to decode it.
     *
     * Since the answer may be very large,
     * return it modulo 109 + 7.
     *
     *
     * --------------
     *
     *  IDEA 1) 1D DP
     *
     *    ->
     *      1 digit
     *
     *      2 digit
     *
     *
     * --------------
     *
     *
     */
    //  IDEA 1) 1D DP
    public int numDecodings_98(String s) {
        // edge ??

        int len = s.length();
        //int[] dp = new int[len - 1]; // ???
        int[] dp = new int[len + 1]; // ???

        // init dp ??
        dp[0] = 1;
        dp[1] = 1;
        // ???
//        if(Integer.parseInt(s) <= 26){
//            dp[2] = 2;
//        }else{
//            dp[2] = 1;
//        }

        for(int i = 2; i < s.length(); i++){

            char val = s.charAt(i);


            // case 1) != '*'
            if (val != '*'){

                // ??? for `*`:
                // loop over 1 - 9 ????
                // Check one-digit decoding
                int oneDigit = Integer.parseInt(s.substring(i - 1, i));
                if (oneDigit >= 1 && oneDigit <= 9) {
                    dp[i] += dp[i - 1];
                }

                /**  NOTE !!!
                 *
                 *  Check `two-digit` decoding
                 *
                 */
                // Check two-digit decoding
                int twoDigits = Integer.parseInt(s.substring(i - 2, i));
                if (twoDigits >= 10 && twoDigits <= 26) {
                    dp[i] += dp[i - 2];
                }
            }

            // case 2) == '*'
            else{


            }


        }




         return dp[len]; // ????
    }



    // LC 70
    // 7.14 -24 am
    /**
     *    -> how many distinct ways can you climb to the top?
     *
     *    - n steps
     *    - can climb 1 or 2 steps
     *
     *    ----------------
     *
     *    IDEA 1) 1D DP
     *       - DP def
     *          - dp[i]: counts to reach idx = i ???
     *
     *       - DP eq
     *          - dp[i] = max( dp[i-2], dp[i-1] )
     *
     *
     *    ----------------
     *
     *
     *
     */
    public int climbStairs(int n) {
        // edge
        if(n == 1){
            return 1; // ??
        }

        int[] dp = new int[n + 1]; // ????
        dp[0] = 1;
        dp[1] = 1; // ??
        dp[2] = 2; // ??

        for(int i = 2; i < n + 1; i++){
            //dp[i] = Math.max( dp[i-2], dp[i-1] );
            dp[i] = dp[i-2] + dp[i-1];
        }


        return dp[n];
    }


    // LC 322
    // 7.27 - 37 am
    /**
     *   -> Return the `fewest` number of coins that you
     *   need to `make up that amount.`
     *
     *   - coins: arr with different types of coin
     *
     *   If that amount of money cannot be made
     *   up by any combination of the coins,
     *      -> return -1.
     *
     *
     *   NOTE:
     *      - You may assume that you have an
     *        infinite number of each kind of coin.
     *
     *  --------------
     *
     *  IDEA 1) BRUTE FORCE
     *
     *  IDEA 2) 1D DP ????
     *     - sort
     *     - 2 loops ???
     *     - AVOID `duplicated combination`
     *     coin_loop
     *      total_amt_loop ????
     *
     *
     *     - DP def:
     *        dp[i]  = ``fewest` number of coins`
     *                  make up val = i ???
     *
     *     - DP eq:
     *          if cur + coin <= amount:
     *             // ???
     *             dp[i] = max( dp[i - coin] + 1,  dp[i] )
     *          else:
     *             ????
     *
     *
     *   --------------
     *
     */
    // 7.48 - 58 am
    // IDEA: 1D DP + loop ????
    /**
     *   DP def:
     *      - dp[i]: max combination counts (coins) make sum = i
     *
     *   DP eq:
     *      - // ??
     *      loop_over_coin
     *         loop over coin - amount
     *           dp[i - coin] = max( dp[i - coin], dp[i] )
     *
     */
    public int coinChange(int[] coins, int amount) {
        // 1. Base case
        if (amount == 0)
            return 0;

        // dp[i] = minimum coins needed to make amount i


        // 2. Initialize DP with a value larger than any possible answer
        // amount + 1 is a safe "Infinity" because the max coins
        // needed is 'amount' (all 1-cent coins).
        int max = amount + 1;
        int[] dp = new int[amount + 1];

        // ??
        Arrays.fill(dp, max);

        dp[0] = 0; // ????

        // ??
        for(int coin: coins){
            // ???
            for(int i = coin; i < amount + 1; i++){
               // dp[i - coin] = max( dp[i - coin], dp[i] );
               // dp[i] += Math.min( dp[i - coin], dp[i] ) + 1;
                // NOTE !!
                //  dp[i] = Math.min(dp[i], dp[i - coin] + 1);
                dp[i] = Math.min( dp[i - coin] + 1, dp[i] );
            }
        }


        return dp[amount] == max ? -1 : dp[amount];  //????
    }









    public int coinChange_99(int[] coins, int amount) {
        // 1. Base case
        if (amount == 0)
            return 0;

        // 2. Initialize DP with a value larger than any possible answer
        // amount + 1 is a safe "Infinity" because the max coins
        // needed is 'amount' (all 1-cent coins).
        int max = amount + 1;
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, max);

        // Base case: 0 coins needed to make amount 0
        dp[0] = 0;

        // 3. Loop through every amount from 1 to target
//        for (int i = 1; i <= amount; i++) {
//            // Try every coin for the current amount i
//            for (int coin : coins) {
//                if (i >= coin) {
//                    // The current amount i can be reached by
//                    // taking (i - coin) and adding 1 coin.
//                    dp[i] = Math.min(dp[i], dp[i - coin] + 1);
//                }
//            }
//        }
        for (int coin : coins) {
            for (int i = 1; i <= amount; i++) {
                // Try every coin for the current amount i
                if (i >= coin) {
                    // The current amount i can be reached by
                    // taking (i - coin) and adding 1 coin.
                    dp[i] = Math.min(dp[i], dp[i - coin] + 1);
                }
            }
        }


        // 4. If dp[amount] is still 'max', it means the amount is unreachable
        return dp[amount] > amount ? -1 : dp[amount];
    }




//    public int coinChange(int[] coins, int amount) {
//        // edge
//        if(coins.length == 1){
//            if(amount % coins[0] != 0){
//                return -1;
//            }
//            return amount / coins[0]; // ??
//        }
//
//        if(amount == 0){
//            return 0;
//        }
//
//        // sort ??? (big -> small)
//        //Arrays.sort(coins,); -> how to do reverse sort in Arrays.sort ???
//        //int maxVal = 0;
//        List<Integer> coinList = new ArrayList<>();
//        for(int c: coins){
//            coinList.add(c);
//           // maxVal = Math.max(c, maxVal);
//        }
//
//
//        // sort is NOT NEEDED ????
////        Collections.sort(coinList, new Comparator<Integer>() {
////            @Override
////            public int compare(Integer o1, Integer o2) {
////                int diff = o2 - o1;
////                return diff;
////            }
////        });
//
//
//        // ??
//        int[] dp = new int[amount + 1]; // ??
//        // NOTE !!!
//        // we pre-fill dp array with `max` val
//        // max val = `amount + 1`
//        //Arrays.fill(dp, -1); // ???
//        Arrays.fill(dp, amount + 1); // ???
//
//        dp[0] = 0;
//
//        //dp[1] =
//
//        /**
//         *      *     - DP def:
//         *      *        dp[i]  = ``fewest` number of coins`
//         *      *                  make up val = i ???
//         *      *
//         *      *     - DP eq:
//         *      *          if cur + coin <= amount:
//         *      *             // ???
//         *      *             dp[i] = max( dp[i - coin] + 1,  dp[i] )
//         *      *          else:
//         *      *             ????
//         *
//         */
//
//        // ??? looping ordering ???
//        for(int coin: coinList){
//            // ???
//            for(int i = 1; i < amount + 1; i++){
//                // ???
//                if(coin + i <= amount){
//                    // ?? ???
//                    if(i - coin >= 0){
//                        // if still init val ??
//                        if(dp[i] == -1){
//                            dp[i] =  dp[i - coin] + 1;
//                        }else{
//                            dp[i] = Math.min( dp[i - coin] + 1,  dp[i] );
//                        }
//                    }
//                }
//            }
//        }
//
//
//        // ???
//        return dp[amount];
//    }
//






     // LC 518
     //  9. 45 - 55 am
    /**
     *  ->
     *   Return the `number of combinations`
     *   that make up that `amount.`
     *
     *   If that amount
     *   of money cannot be made up
     *   by any combination of the coins, return 0.
     *
     *   coins: coin array
     *   amount: total amt of money
     *
     *
     *  -------------
     *
     *   IDEA 1) 1D DP ???
     *
     *      - DP def:
     *
     *          - dp[i] = max combination count of num = i
     *
     *      - DP eq:
     *
     *         - if (i + coin <= amount):
     *             dp[i] = max( dp[i - coin], dp[i] ) // ???
     *
     *       NOTE !!!
     *        need to loop coin, then amount
     *        -> to make sure we DON'T have `duplicated` combinations
     *
     *
     *  -------------
     *
     *
     */
    // 9.08 - 18 am
    // IDEA 1) 1D DP
    public int change(int amount, int[] coins) {
        // edge

        int n = amount; // ???
        // - dp[i] = max combination count of num = i
        int[] dp = new int[n + 1]; // ???
        // ???
        Arrays.fill(dp, 0);

        dp[0] = 1; // ????

        // ??
        for(int coin: coins){
            for(int i = coin; i < amount + 1; i++){
                // Return the number of combinations that make up that amount.
                // If that amount of money
                // cannot be made up by any combination of the coins, return 0.
                //dp[i] =  Math.max(dp[i - coin], dp[i]);
                dp[i] += dp[i - coin];  // ?????
            }
        }


        return dp[n] == 0 ? -1: dp[n];
    }








    //  IDEA 1) 2D DP ???
    public int change_95(int amount, int[] coins) {
        // edge

        int n = amount; // ???
        // - dp[i] = max combination count of num = i
        int[] dp = new int[n + 1]; // ???
        // ???
        Arrays.fill(dp, 0);

        // init ??
        dp[0] = 1;


        for(int c: coins){
            for(int i = 1; i < n + 1; i++){
                // ??
                if(i + c <= amount){
                    //dp[i] = Math.max( dp[i - c], dp[i] ); // ???
                    dp[i] += dp[i - c]; //Math.max( dp[i - c], dp[i] ); // ???
                }
            }
        }

        return dp[n];  //???
    }













    // 8.27 - 37 am
    public int change_99(int amount, int[] coins) {
        // 1. Base case
        if (amount == 0){
            return 1;
        }

        if (coins.length == 1) {
            if (amount % coins[0] != 0) {
                return 0;
            }
            return 1; //?
        }

        // 2. Initialize DP with a value larger than any possible answer
        // amount + 1 is a safe "Infinity" because the max coins
        // needed is 'amount' (all 1-cent coins).
        // max = amount + 1;
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, 0); // ???

        // Base case: 0 coins needed to make amount 0
        dp[0] = 1;

        // 3. Loop through every amount from 1 to target
        for (int coin : coins) {
            for (int i = 1; i <= amount; i++) {
                // Try every coin for the current amount i
                if (i >= coin) {
                    // The current amount i can be reached by
                    // taking (i - coin) and adding 1 coin.
                    //dp[i] = Math.min(dp[i], dp[i - coin] + 1);
                    dp[i] = dp[i - coin] + dp[i]; // ????
                }
            }
        }


        // 4. If dp[amount] is still 'max', it means the amount is unreachable
        return dp[amount] == 0 ? -1 : dp[amount];
    }


    // LC 746
    // 7.44 - 57 am
    /**
     *  -> Return the `minimum` cost
     *  to reach the `top of the floor.`
     *
     *   NOTE:
     *      can start from idx = 0 or idx = 1
     *
     *
     *  ---------------
     *
     *   IDEA 1) 1D DP
     *      - DP def
     *          dp[i] = min cost to reach idx = i
     *
     *      - DP eq
     *         dp[i] = min( dp[i-2] + cost[i-2], dp[i-1] + cost[i-1] )   // ???
     *
     *       2 cases
     *           - idx = 0 or 1
     *
     *
     *  ---------------
     *
     *
     */
    public int minCostClimbingStairs(int[] cost) {
        // edge

        int n = cost.length;
        int minCost = 1000 * n; // ???

//        int[] dp = new int[n + 1]; // ??
//        dp[0] = cost[0];
//        // ???
//        dp[1] = Math.min(dp[0], cost[1]);


        minCost = Math.min( Math.min(
                dpHelper(cost, 0),
                dpHelper(cost, 1)),
                minCost
        );


        return minCost;
    }


    private int dpHelper(int[] cost, int startIdx){
        // /??
        int n = cost.length;
        //int minCost = 1000 * n;

        int[] dp = new int[n + 1]; // ??
        dp[0] = cost[0];
        dp[1] = Math.min(dp[0], cost[1]);

        for(int i = startIdx; i < n + 1; i++){
            dp[i] = Math.min( dp[i-2] + cost[i-2], dp[i-1] + cost[i-1] );   // ???
        }


        return dp[n];
    }



    // LC 279
    // 8.24 - 34 am
    /**
     *   -> Given an integer n,
     *   return the  ` least number `of `perfect square numbers`
     *   that sum to n.
     *
     * -----------------
     *
     *  IDEA 1) 1D DP
     *
     *  IDEA 2) BFS ???
     *
     *
     * -----------------
     *
     */
    // DP
    // 9.07 - 17 am
    /**
     *   IDEA 1) 1D DP
     *
     *    - DP def
     *       - dp[i]: min number of perfect int sum up == i
     *
     *    - DP eq
     *       if (val == perfect int)
     *         dp[i] = dp[i-val] + 1 // ????
     *
     */
    public int numSquares(int n) {
        // edge

        // get `perfect vals`
        List<Integer> list = new ArrayList<>();
        // ???
        for(int i = 1; i * i <= n; i++){
            list.add(i * i); // ??
        }
        // sort (big -> small)
        /** NOTE !!
         *
         *  no need to sort, NOT affect DP algo;
         *  (it helps in greedy/backtracking, but not here).
         */
        Collections.sort(list, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                int diff = o2 - o1;
                return diff;
            }
        });


        int[] dp = new int[n + 1]; // ???
        // Initial Fill: Initializing with n is okay,
        // but n + 1 or Integer.MAX_VALUE is safer to represent "infinity."
        //Arrays.fill(dp, n);  // ???
        Arrays.fill(dp, n + 1);  // ???

        // Base Case: The value of dp[0] should be 0,
        // not 1. It takes zero squares to sum up to zero.
        dp[0] = 0; // 1; // ??

        dp[1] = n;

        /**
         *      *    - DP eq
         *      *       if (val == perfect int)
         *      *         dp[i] = dp[i-val] + 1 // ????
         *      *
         *
         */
        for(int i = 2; i < n + 1; i++){
            // ???
            for(int val: list){
                if(val <= i){
                    dp[i] = Math.min(dp[i - val] + 1, dp[i]);
                }
            }

        }



        return dp[n];  //???
    }








    // IDEA 2) BFS ???
    public int numSquares_99(int n) {
        // edge

        int minCnt = n; // ???
        // PQ: big PQ ???
        // { [ val, remaining_val ] }
        PriorityQueue<Integer[]> pq = new PriorityQueue<>(new Comparator<Integer[]>() {
            @Override
            public int compare(Integer[] o1, Integer[] o2) {
                int diff = o2[0] - o1[0];
                return diff;
            }
        });

        // insert to PQ
        List<Integer> list = getSqureList(n); /// ???
        System.out.println(">>> list = " + list);
        for(Integer x: list){
            if(x <= n){
                pq.add(new Integer[]{x, n});
            }
        }

        // ??? PQ
        int layer = 0;
        while (!pq.isEmpty()){
            int size = pq.size();
            layer += 1; // ???


            for(int i = 0; i < size; i++){
                Integer[] x = pq.poll();
                int val = x[0];
                int remaining = x[1];
                if(remaining == 0){
                    return layer;
                }

                // ???
                //if(val <)

            }
        }


        return minCnt;
    }



    private List<Integer> getSqureList(int val){
        List<Integer> res = new ArrayList<>();
        // ???
        for(int i = 1; i < val + 1; i++){
            res.add(i * i); // ???
        }


        return res;
    }





    private boolean isSquare(){
        return false;
    }


    // LC 494
    // 9.45 - 55 am
    /**
     *
     *
     *   -> Return the number of different expressions
     *   that you can build,
     *   which evaluates to target.
     *
     *
     *   -------------
     *
     *
     *    -------------
     *
     */
    // 10.19 - 36 am
    /**
     *  -> Return the `number` of `different
     *  expressions` that you can build,
     *  which evaluates to target.
     *
     *
     *  --------
     *
     *  // int[] dp = new int[subsetTarget + 1]` is a **1D array**.
     *
     *
     *   IDEA 1) 1D DP ?
     *
     *      - DP def:
     *         - dp[i][j]:
     *            # of expressions at target = i
     *            j: op type ???
     *
     *      - DP eq:
     *
     *
     *   IDEA 2) BRUTE FORCE ???
     *
     *  --------
     *
     *
     */
    // 18.04 - 14 pm
    // IDEA: 2D DP
    /**
     *  IDEA 1) 0/1 knapsack counting
     *
     *   -> get `half`
     *   -> check if exists ???
     *
     */
    public int findTargetSumWays(int[] nums, int target) {

        return 0;
    }




    // IDEA 1) 1D DP + total_sum / 2 ??
    public int findTargetSumWays_99(int[] nums, int target) {
        int totalSum = 0;
        for (int n : nums)
            totalSum += n;

        // Edge cases:
        // 1. target is too large to reach
        // 2. target + totalSum must be even (from the formula above)
        if (Math.abs(target) > totalSum || (target + totalSum) % 2 != 0) {
            return 0;
        }

        int subsetTarget = (target + totalSum) / 2;

        // ????
        int[] dp = new int[subsetTarget + 1];

        dp[0] = 1; // Base case: 1 way to make sum 0 (empty subset)

        // /??
        for(int x: nums){
            // ???
            for(int i = subsetTarget; i >= x; i--){
                dp[i] += dp[subsetTarget - x];
            }
        }


        // ???
        return dp[subsetTarget];
    }








    // LC 582
    // 10.54 - 11.04
    // IDEA 1) DFS
    public List<Integer> killProcess(List<Integer> pid, List<Integer> ppid, int kill) {
        // edge

        // // 1. Build the Adjacency List: Parent -> List of Children
        // { node: [sub_node_1, sub_node_2, ..] }
        Map<Integer, List<Integer>> map = new HashMap<>();

        // build graph
        for(int i = 0; i < ppid.size(); i++){
            // ???
            int p = ppid.get(i);

            if(!map.containsKey(p)){
                map.put(p, new ArrayList<>());
            }
            List<Integer> list = map.get(p);
            list.add(pid.get(i));
            map.put(p, list);


            // ???
            int p2 = pid.get(i);
            if(!map.containsKey(p2)){
                map.put(p2, new ArrayList<>());
            }

//            int parent = ppid.get(i);
//            int child = pid.get(i);
//            map.putIfAbsent(parent, new ArrayList<>());
//            map.get(parent).add(child);
        }

        System.out.println(">>> map = " + map);

        List<Integer> res = new ArrayList<>();
        HashSet<Integer> set = new HashSet<>();

        res.add(kill);

        // dfs call
        dfsKill(map, set, kill);

        for(int x: set){
            res.add(x);
        }

        return res;
    }

    private void dfsKill(Map<Integer, List<Integer>> map,  HashSet<Integer> set, int kill){
        // edge
        if(!map.containsKey(kill)){
            return;
        }

        set.add(kill);

        // next
        for(int next: map.get(kill)){
            dfsKill(map, set, next);
        }

    }

    // LC 343
    // 8.27 - 37 am
    /**
     *  -> Return the `maximum product`  you can get.
     *
     *
     *   Given an integer n, break it into the sum of k positive integers,
     *   where k >= 2, and maximize the product of those integers.
     *
     *   - int n,
     *   - break into the sum of k int
     *   - k >= 2
     *
     *  ----------------------
     *
     *  IDEA 1) 1-D DP
     *      - DP def
     *         - dp[i]: `max product` when `init split` with i
     *
     *      - DP eq
     *         - if can split:
     *            - dp[i] = max( dp[ i / val ] * val, dp[i] )  // ?????
     *
     *
     *  IDEA 2) BRUTE FORCE / GREEDY
     *
     *  IDEA 3) MATH ????
     *
     *
     *  ----------------------
     *
     *  Input: n = 10
     *  Output: 36
     *
     *  dp[1] = 1
     *  dp[2] = 2 ^ 5 = 32
     *  dp[3] = 36 ???
     *  dp[4] = 32 ??
     *  ....
     *
     */
    // 12.59 - 1.43 pm
    /**
     *
     *
     *  -> Return the maximum product you can get.
     *
     *  ---------
     *
     *
     *
     *  IDEA 1) 1D DP + double loop !!
     *
     *   -> split i to ` j + (i - j )` ????
     *
     *    - DP def:
     *       - dp[i] = max product of int = n; // ???
     *
     *    - DP eq:
     *
     *       - // /??
     *       - dp[i] = max( dp[i-j] * (j) , dp[i] ) // ???
     *
     *
     *  ---------
     *
     *
     *
     */
    public int integerBreak(int n) {
        // edge

        // ??
        // with `n+1` size dp,
        // so we can update dp till `idx = n` ?????
        int[] dp = new int[n + 1]; // ???

        // ???
        dp[0] = 0;
        dp[1] = 1; // ???
        dp[2] = 1;

        /**
         *
         *    ->
         *
         *     i = 3, j = 2
         *     dp[3] = max ( dp[1] * 2, dp[2] )
         *
         *     i = 3, j = 1
         *     dp[3] = max ( dp[2] * 1, dp[1] )
         */

        for(int i = 3; i < n; i++){
            // ???
            for(int j = i-1; j < i; j++){
                // - dp[i] = max( dp[i-j] * (j) , dp[i] ) // ???
                dp[i] = Math.max(dp[i-j] * j, dp[i]);
            }
        }


        return dp[n];
    }










    public int integerBreak_99(int n) {
        // edge

        // 1. Base cases for small n (must break into at least 2 numbers)
        if (n == 2) return 1; // 1*1
        if (n == 3) return 2; // 2*1



        int maxProduct = 1;

        // /?
        // NOTE !!! size = n + 1
       // int[] dp = new int[n];
        int[] dp = new int[n + 1];


//        dp[1] = 1;
//        // ???
//        dp[2] = Math.max(dp[1], dp[2]);

        // NOTE !!!
        // 2. Initialize DP
        dp[1] = 1;
        dp[2] = 1;




        return maxProduct;
    }


    // ???
    private int splitAndProduct(int n, int initVal){
        int res = 1;
        // ???
        while (n > initVal){
            n = (n / initVal);
            // ???
            res *= initVal;
        }



        return res;
    }



    // LC 486
    // 11.14 - 24 am
    /**
     *  -> Return `true`
     *      if `Player 1` can win the game.
     *      If the scores of both players are `equal,`
     *      then player 1 is still the winner,
     *      -> and you should also return `true.`
     *
     *
     *   - int array: nums
     *   - 2 players: p1, p2
     *   - p1 -> p2
     *   - init score = 0
     *
     *   - game op:
     *       - At each turn, the player takes `one of the numbers` from
     *         EITHER END of the array
     *           (i.e., nums[0] or nums[nums.length - 1]) !!!!
     *
     *
     *         which `reduces the size of the array by 1. `
     *          -> The player adds the chosen number to their score.
     *          ->  The game ends when there are NO more elements in the array.
     *
     *  --------------------
     *
     *   IDEA 1) GREEDY
     *
     *
     *   IDEA 2) DP ????
     *
     *
     *  --------------------
     *
     *   ex 1)
     *
     *    Input: nums = [1,5,2]
     *    Output: false
     *
     *    [1,5,2]
     *     1
     *
     *     or
     *
     *     [1,5,2]
     *          1
     *
     *
     */
    // 8.44 - 54 am
    /**
     *  IDEA 1) 2D DP
     *
     *   - DP def
     *      - dp[i][j]: `relative score` (player 1 VS player 2)
     *                   at idx = i ?????
     *
     *   - DP eq
     *
     *       dp[i][j] =
     *          ???
     *            max ( nums[i] + dp[i-1][j],  nums[j] + dp[i][j-1] )
     *
     *
     *
     *  IDEA 2) 1D DP
     *
     *
     *
     */
    // IDEA 1) 2D DP
    public boolean predictTheWinner(int[] nums) {
        // edge
        int n = nums.length;
        // 1. Edge case: If there's only one number, P1 takes it and wins.
        if (n <= 1) return true;




        // ???
        //int n = nums.length;
        // NOTE !!!
        // DP Definition: dp[i][j] should be
        // the maximum relative score a player
        // can get from the subarray nums[i...j].

        // Minimax Transition: The formula is dp[i][j] =
        // Math.max(nums[i] - dp[i+1][j], nums[j] - dp[i][j-1]).
        //
        //Why minus? Because dp[i+1][j] is the best
        // the next player can do. You take your pick
        // and subtract their best possible outcome.
        int[][] dp = new int[n][n];

        // ??? init
        dp[0][0] = 0; // ??
        dp[0][1] = nums[0];
        dp[1][0] = nums[0];

        ///  /??
        /**
         *      *       dp[i][j] =
         *      *          ???
         *      *            max ( nums[i] + dp[i-1][j],  nums[j] + dp[i][j-1] )
         *      *
         *
         */

        // NOTE !!!
        // DP Definition: dp[i][j] should be
        // the maximum relative score a player
        // can get from the subarray nums[i...j].

        // Minimax Transition: The formula is dp[i][j] =
        // Math.max(nums[i] - dp[i+1][j], nums[j] - dp[i][j-1]).
        //
        //Why minus? Because dp[i+1][j] is the best
        // the next player can do. You take your pick
        // and subtract their best possible outcome.

        for(int i = 1; i < n; i++){
            // ???
            for(int j = 1; j < n; j++){
                // ???
                // case 1)
                if(i == j){
                    dp[i][j] = i;
                }
                // case 2)
                else{
                    dp[i][j] = Math.max (
                            nums[i] - dp[i + 1][j],
                            nums[j] - dp[i][j + 1]
                    );
                }

            }
        }


        // ???
        return dp[n][n] >= 0;
    }













    // greedy ????
    public boolean predictTheWinner_99(int[] nums) {
        // edge
        if(nums == null || nums.length == 0){
            return true;
        }
        if(nums.length == 1){
            return true; // ???
        }

        // ??
        Deque<Integer> dq = new LinkedList<>();
        for(int x: nums){
            dq.add(x);
        }

        int score1 = 0;
        int score2 = 0;

        // player1: 1,3,5,...
        // player2: 2,4,6...
        int i = 1;


        // ????
        while (!dq.isEmpty()){
            if(i % 2 == 1){
                if(dq.peekFirst() > dq.peekLast()){
                    score1 += dq.pollFirst();
                }else{
                    score1 += dq.pollLast();
                }
            }else{
                if(dq.peekFirst() > dq.peekLast()){
                    score2 += dq.pollFirst();
                }else{
                    score2 += dq.pollLast();
                }
            }

            i += 1;
        }


        return score1 >= score2;
    }




    // LC 1406
    // 11.50 - 12 pm
    /**
     *
     *  -> Return "Alice" if Alice will win,
     *  "Bob" if Bob will win, or "Tie"
     *  if they will end the game with the same score.
     *
     *
     *  --------------
     *
     *  IDEA 1) 2D DP, LC 486
     *
     *   - dp def:
     *
     *     - dp[i][j] = `relative` score within
     *
     *   - dp eq:
     *
     */
    public String stoneGameIII(int[] stoneValue) {

        return null;
    }



    // LC 62
    // 7.50 - 8.00 am
    /**
     *
     *  -> Given the two integers m and n, return the number of possible
     *  unique paths that the robot can take
     *  to reach the bottom-right corner.
     *    (i.e., grid[m - 1][n - 1]).
     *
     *
     *
     *   - The robot is initially located
     *   at the `top-left corner (i.e., grid[0][0]).`
     *
     *   NOTE:
     *
     *    - The robot can only move either
     *      `down or right` at any point in time.
     *
     *
     *  -------------
     *
     *   IDEA 1) 2D DP
     *
     *    - DP def
     *        - dp[i][j] = # of unique path can move
     *                     from (0,0) to (i,j)
     *
     *    - DP eq
     *
     *       - dp[i][j] =
     *            dp[i-1][j] + dp[i][j-1]
     *
     *  -------------
     *
     */
    public int uniquePaths(int m, int n) {
        // edge
        if(m == 0 || n == 0){
            return 0;
        }
        if(m == 1 || n == 1){
            return 1;
        }

        // ???
        int[][] dp = new int[m][n];

        // init ???
        for(int x = 0; x < n; x++){
            dp[0][x] = 1;
        }
        for(int y = 0; y < m; y++){
            dp[y][0] = 1;
        }

        // ???
        /**
         *      *       - dp[i][j] =
         *      *            dp[i-1][j] + dp[i][j-1]
         *      *
         *
         */
        for(int y = 1; y < m; y++){
            for(int x = 1; x < n; x++){
                dp[y][x] = dp[y-1][x] + dp[y][x-1];
            }
        }


        return dp[m-1][n-1];
    }


    // LC 63
    // 8.11 - 21 am
    /**
     *  -> Return the number of
     *  possible unique paths that the robot
     *  can take to reach the bottom-right corner.
     *
     *
     *   - robot init at grid[0][0]
     *   - destination is grid[m-1][n-1]
     *
     *    NOTE:
     *     - The robot can only move
     *       either down or right at any point in time.
     *
     *
     *       An obstacle and space are marked as 1 or 0 respectively in grid.
     *       A path that the robot takes `cannot` include any square that is an `obstacle.`
     *
     *
     *
     *
     *  -----------
     *   IDEA 1) 2D DP
     *
     *    - DP def
     *        - dp[i][j] = # of unique path can move
     *                     from (0,0) to (i,j)
     *
     *    - DP eq
     *
     *       - dp[i][j] =
     *             if not obstacle
     *               dp[i-1][j] + dp[i][j-1]
     *             else:
     *               0
     *
     *
     *
     *  -----------
     *
     *
     */
    public int uniquePathsWithObstacles(int[][] obstacleGrid) {
        int m = obstacleGrid.length;
        int n = obstacleGrid[0].length;

        // edge
        if(m == 0 || n == 0){
            return 0;
        }
        if(m == 1 || n == 1){
            return 1;
        }

        // 1. Edge Case: If the start or end is blocked, no paths exist
        if (obstacleGrid[0][0] == 1 || obstacleGrid[m - 1][n - 1] == 1) {
            return 0;
        }

        // ???
        int[][] dp = new int[m][n];

        // init ???
        for(int x = 0; x < n; x++){
            // ???
            if(obstacleGrid[0][x] == 1){
                break;
            }
            dp[0][x] = 1;
        }
        for(int y = 0; y < m; y++){
            // ???
            if(obstacleGrid[y][0] == 1){
                break;
            }
            dp[y][0] = 1;
        }

        // ???
        /**
         *      *       - dp[i][j] =
         *      *            dp[i-1][j] + dp[i][j-1]
         *      *
         *
         */
        for(int y = 1; y < m; y++){
            for(int x = 1; x < n; x++){
                // ???
                if(obstacleGrid[y][x] == 1){
                    dp[y][x] = 0;
                }else{
                    dp[y][x] = dp[y-1][x] + dp[y][x-1];
                }
            }
        }


        return dp[m-1][n-1];
    }


    // LC 814
    // 16.31 - 41 pm
    /**
     *  -> return the same tree where every
     *    subtree (of the given tree)
     *    not containing a 1 has been removed.
     *
     *
     *  ---------------
     *
     *   IDEA 1) POST ORDER DFS ???
     *
     *    -> left -> right -> root
     *
     *
     *   ---------------
     *
     */

    // 11.27 - 37 am
    // IDEA: LCA + POST ORDER DFS
    public TreeNode pruneTree(TreeNode root) {
        // edge
        if(root == null){
            return root;
        }

        TreeNode _left = pruneTree(root.left);
        TreeNode _right = pruneTree(root.right);

        // ???
        if(root.val == 0 && _left == null && _right == null){
            return null;
        }

//        if(_left == null){
//            return _right;
//        }else if(_right == null){
//            return _left;
//        }

        // ??? still need to `re-connect` ????
        root.left = _left;
        root.right = _right;


        return root;
    }














    public TreeNode pruneTree_99(TreeNode root) {
        // edge
        if(root == null){
            return root;
        }

        TreeNode _left = pruneTree_99(root.left);
        TreeNode _right = pruneTree_99(root.right);

//        if(_left == null && _right != null){
//            return root; // ???
//        }

        // ???
        if(root.val == 0 && _left == null && _right == null){
            return null;
        }

        root.left = _left;
        root.right = _right;

        return root;
    }




    // ???
    // 1. check if `should delete` via DFS
    // 2. update node via BFS ????
    public TreeNode pruneTree_98(TreeNode root) {
        // edge
        if(root == null){
            return root;
        }

        // ??? bfs
        Queue<TreeNode> q = new LinkedList<>();
        q.add(root);
        // ??
        while (!q.isEmpty()){
            TreeNode cur = q.poll();
            // ????
            if(deleteHelper(cur)){
                continue;
            }
            // ???
            if(cur.left != null){
                q.add(cur.left);
            }
            if(cur.right != null){
                q.add(cur.right);
            }
        }


        return root;
    }


    // ???
    // if has NO `1` node in child nodes -> return true
    // otherwise, return false
    private boolean deleteHelper(TreeNode root){
        if(root == null){
            return false;
        }

        boolean _left = deleteHelper(root.left);
        boolean _right = deleteHelper(root.right);

        if(_left && _right){
            return true;
        }

        // ???
        if(root.val != 1){
            return true;
        }


        return false;
    }


    // LC 64
    // 7.48 - 8.03 am
    /**
     *   -> , find a `path` from
     *   `top left to bottom right`, which `minimizes`
     *   the sum of all numbers along its path.
     *
     *    - m x n grid
     *    - Note: can only move
     *      either `down or right` at any point in time.
     *
     *   -----------------
     *
     *    IDEA 1) BFSD / DFS + BRUTE FORCE ????
     *
     *    IDEA 2) 2d DP ???
     *
     *       - DP def:
     *           dp[i][j]: min cost visit (i,j)
     *
     *       - DP eq:
     *
     *
     *
     *    IDEA 3) dijkstra algo ????
     *
     *    -----------------
     *
     *
     */
    // 11.11 - 21 am
    // 2D dp
    public int minPathSum(int[][] grid) {
        // edge
        if (grid == null || grid.length == 0 || grid[0].length == 0) {
            return 0;
        }
        int l = grid.length;
        int w = grid[0].length;

        // ??
        int[][] dp = new int[l][w];

        // init start
        dp[0][0] = grid[0][0];

        // init ??
        for(int x = 1; x < w; x++){
            // ????
            //dp[0][x] += grid[0][x];
            dp[0][x] =  dp[0][x - 1] + grid[0][x];
        }

        for(int y = 1; y < l; y++){
            // ????
            //dp[y][0] += grid[y][0];
            dp[y][0] = dp[y - 1][0] +  grid[y][0];
        }



        for(int y = 1; y < l; y++){
            for(int x = 1; x < w; x++){
                int minVal = Math.min(dp[y-1][x], dp[y][x - 1]);
                dp[y][x] = minVal + grid[y][x];
            }
        }


        return dp[l-1][w-1]; // /??
    }








    // IDEA: 2D DP
    public int minPathSum_94(int[][] grid) {
        // edge
        if (grid == null || grid.length == 0 || grid[0].length == 0) {
            return 0;
        }
        int l = grid.length;
        int w = grid[0].length;

        // ??
        int[][] dp = new int[l][w];

        /**
         * Note: You can only move either
         * `down or right`
         * at any point in time.
         *
         */

        // init start
        dp[0][0] = grid[0][0];

//        // init ???
//        for(int x = 0; x < w; x++){
//            dp[0][x] = grid[0][x];
//        }
//        for(int y = 0; y < l; y++){
//            dp[y][0] = grid[y][0];
//        }
        // first row (prefix sum)
        for (int x = 1; x < w; x++) {
            dp[0][x] = dp[0][x - 1] + grid[0][x];
        }

        // first column (prefix sum)
        for (int y = 1; y < l; y++) {
            dp[y][0] = dp[y - 1][0] + grid[y][0];
        }


        // ???
        for(int y = 1; y < l; y++){
            for(int x = 1; x < w; x++){
                dp[y][x] += Math.min(
                        dp[y-1][x],
                        dp[y][x-1]
                );
            }
        }



        return dp[l - 1][w - 1];
    }








    // IDEA 3) dijkstra algo ????
    public int minPathSum_99(int[][] grid) {
        // edge
        if (grid == null || grid.length == 0 || grid[0].length == 0) {
            return 0;
        }

        int l = grid.length;
        int w = grid[0].length;

        // pq: { [x, y, cost], .... }
        PriorityQueue<Integer[]> pq = new PriorityQueue<>(new Comparator<Integer[]>() {
            @Override
            public int compare(Integer[] o1, Integer[] o2) {
                int diff = o1[2] - o2[2];
                return diff;
            }
        });

        boolean[][] visited = new boolean[l][w]; // ???

        pq.add(new Integer[]{0, 0, grid[0][0]}); // ??
        visited[0][0] = true;

        // ??? either `down or right`
        // (x,y)
        int[][] moves = new int[][] { {1,0}, {0,1} };

        while (!pq.isEmpty()){
            // ???
            Integer[] cur = pq.poll();

            int x = cur[0];
            int y = cur[1];
            int cost = cur[2];

            // ??? check if arrived ???
            if(x == w - 1 && y == l - 1){
                return cost;
            }

            // ?? move
            for(int[] m: moves){
                int x_ = x + m[0];
                int y_ = y + m[1];
                // ??
                if(x_ >= 0 && x_ < w && y_ >= 0 && y_ < l){
                    if(!visited[y_][x_]){
                        visited[y_][x_] = true;
                        // NOTE !!! below
                        pq.add(new Integer[]{x_, y_, cost + grid[y_][x_]});
                    }
                }
            }
        }


        return -1;  // shouldn't reach here if input is valid
    }



    // LC 1631
    // 10.11 -21 am
    /**
     *  -> Return the `minimum` effort required to
     *  travel from the `top-left cell to the bottom-right cell.`
     *
     *  - You can move up, down, left, or right,
     *  and you wish to find a route that requires
     *  the `minimum` effort.
     *
     *  -------------
     *
     *   IDEA 1) 2D DP
     *
     *   IDEA 2) Dijkstra ALGO
     *
     *  -------------
     *
     */
    // 14.12 - 22 pm
    // IDEA 2) Dijkstra ALGO
    // BFS + PQ + `cost array`
    public int minimumEffortPath(int[][] heights) {
        // edge

        int l = heights.length;
        int w = heights[0].length;

        // ??? `cost array`
        int[][] costArr = new int[l][w];
        // ????

        // NOTE !!!!
        //Arrays.fill(costArr, 1000000 + 1); // /???
        for (int i = 0; i < l; i++) {
            Arrays.fill(costArr[i], Integer.MAX_VALUE);
        }

        // PQ: { [x, y, cost] }
        // min PQ: cost
        PriorityQueue<Integer[]> pq = new PriorityQueue<>(new Comparator<Integer[]>() {
            @Override
            public int compare(Integer[] o1, Integer[] o2) {
                int diff = o1[2] - o2[2];
                return diff;
            }
        });

        pq.add(new Integer[]{0, 0, 0}); // ???
        costArr[0][0] = 0; // ???

        int[][] moves = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };

        // ??
        while (!pq.isEmpty()){

            Integer[] cur = pq.poll();
            int x = cur[0];
            int y = cur[1];
            int cost = cur[2];

            // ???
            if(x == w - 1 && y == l - 1){
                return cost;
            }

            // check `effort`
       /*     if(heights[y][x] >= costArr[y][x]){
                continue;
            }*/

            // !!! NOTE !!!!
            if(cost >= costArr[y][x]){
                continue;
            }



            // ?? update effort
            costArr[y][x] = heights[y][x]; // ????

            // move
            // ???
            for(int[] m: moves){
                int x_ = x + m[0];
                int y_ = y + m[1];
                // ???
                if(x_ >= 0 && x_ < w && y_ >= 0 && y_ < l){
                    // ????????
                    //int newCost = cost + heights[y_][y_];

                    int diff = Math.abs(heights[y][x] - heights[y_][x_]);
                    int newCost = Math.max(cost, diff);


                    // ????
                    if(newCost < costArr[y_][x_]){
                        // ????
                        pq.add(new Integer[]{x_, x_, newCost});
                    }
                }
            }


        }


        return -1; // ??? shpuld not visit here
    }






    // IDEA 2) Dijkstra ALGO
    public int minimumEffortPath_94(int[][] heights) {
        // edge

        int l = heights.length;
        int w = heights[0].length;

        int[][] costArr = new int[l][w];
        // ????
        Arrays.fill(costArr, Integer.MAX_VALUE); // /???

        // PQ: { [x, y, cost] }
        PriorityQueue<Integer[]> pq = new PriorityQueue<>(new Comparator<Integer[]>() {
            @Override
            public int compare(Integer[] o1, Integer[] o2) {
                int diff = o1[2] - o2[2];
                return diff;
            }
        });

        pq.add(new Integer[]{0, 0, heights[0][0]});

        int[][] moves = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };

        // /??
        while (!pq.isEmpty()){
            // ???
            Integer[] cur = pq.poll();
            int x = cur[0];
            int y = cur[1];
            int cost = cur[2];
            // ???
            if(x == w - 1 && y == l - 1){
                return cost;
            }

            // ???
//            if(costArr[y][x] < cost){
//                continue;
//            }
            if(costArr[y][x] > heights[y][x]){
                continue;
            }

            // ??? update costArr with `cur` cost ???
            costArr[y][x] = cost;

            // ???
            for(int[] m: moves){
                int x_ = x + m[0];
                int y_ = y + m[1];
                // ???
                if(x_ >= 0 && x_ < w && y_ >= 0 && y_ < l){
                    // ????????
                    int newCost = cost + heights[y_][y_];
                    if(newCost < heights[y_][x_]){
                       // ????

                       pq.add(new Integer[]{x_, x_, newCost});
                    }
                }
            }

        }



        return -1; // ??? should not visit here ???
    }









    // IDEA 1) 2D DP
    public int minimumEffortPath_99(int[][] heights) {

        int m = heights.length;
        int n = heights[0].length;

        // 1. Initialize DP table (or use grid in-place to save space)
        int[][] dp = new int[m][n];

        // 2. Base Case: Starting point
        dp[0][0] = heights[0][0];

//        // 3. Initialize First Column (only one way: from above)
//        for (int i = 1; i < m; i++) {
//            dp[i][0] = dp[i - 1][0] + heights[i][0];
//        }
//
//        // 4. Initialize First Row (only one way: from left)
//        for (int j = 1; j < n; j++) {
//            dp[0][j] = dp[0][j - 1] + heights[0][j];
//        }

        // 5. Fill the rest of the table
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                // Min cost to reach (i, j) = grid[i][j] + min(above, left)
                // ???
                int y_ = i + 1;
                int x_ = j + 1;
                // ???
                int val1 = -1;
                int val2 = -1;
                if(y_ < m){
                    val1 = heights[y_][j];
                }
                if(x_ < n){
                    val2 = heights[i][x_];
                }

                // ???
                int tmp = Math.min(
                        dp[i - 1][j], dp[i][j - 1]);

                if(val1 > 0){
                    tmp = Math.min(tmp, val1);
                }
                if(val2 > 0){
                    tmp = Math.min(tmp, val2);
                }


                dp[i][j] = heights[i][j] + tmp;
            }
        }

        return dp[m - 1][n - 1];
    }


    // LC 877
    // 8.27 - 37 am
    /**
     *  -> Assuming Alice and Bob play optimally,
     *  return true if Alice wins the game,
     *  or false if Bob wins.
     *
     *   - The objective of the game is to
     *   end with `the most` stones.
     *
     *   - The total number of stones across
     *      all the piles is `odd`,
     *
     *
     *  -  even number of piles
     *  -   positive integer number of stones piles[i].
     *
     *
     *
     *  --------------------
     *
     *
     *  --------------------
     *
     */
    public boolean stoneGame(int[] piles) {
        // edge

        int[] nums = piles;
        int n = nums.length;
        if (n <= 1){
            return true;
        }


        // ???
        // DP def:
        //   dp[i][j] =    `relative` score at sub arr [i.j]
        int[][] dp = new int[n][n];
        // ??
        dp[0][0] = piles[0]; // ???


        // 3. Base Case: Subarrays of length 1
        for (int i = 0; i < n; i++) {
            dp[i][i] = nums[i];
        }



        // ???
        for(int i = 1; i < n; i++){
            for(int j = 1; j < n; j++){

                dp[i][j] = Math.max(
                        dp[i-1][j] - piles[i],
                        dp[i][j-1] - piles[j]
                );

            }
        }

        //return dp[n][n] >= 0; // ????
        return dp[0][n - 1] >= 0; // ????
    }










    public boolean stoneGame_99(int[] piles) {

        // /??
        int[] nums = piles;

        int n = nums.length;
        // 1. Edge case: If there's only one number, P1 takes it and wins.
        if (n <= 1)
            return true;

        // 2. dp[i][j] = max relative score from subarray nums[i...j]
        int[][] dp = new int[n][n];

        // 3. Base Case: Subarrays of length 1
        for (int i = 0; i < n; i++) {
            dp[i][i] = nums[i];
        }

        // 4. Fill the table for lengths 2 up to n
        // We move i from bottom up to ensure dp[i+1] is always ready
        for (int i = n - 2; i >= 0; i--) {
            for (int j = i + 1; j < n; j++) {

                // Choice A: Take nums[i],
                // subtract the opponent's best relative score from the rest
                int pickLeft = nums[i] - dp[i + 1][j];

                // Choice B: Take nums[j],
                // subtract the opponent's best relative score from the rest
                int pickRight = nums[j] - dp[i][j - 1];


                dp[i][j] = Math.max(pickLeft, pickRight);
            }
        }

        // 5. If the relative score for the full array is >= 0, P1 wins or ties
        return dp[0][n - 1] >= 0;
    }



    // LC 464
    public boolean canIWin(int maxChoosableInteger, int desiredTotal) {

        return false;
    }


    // LC 1325
    // 14.34 - 44 pm
    /**
     *
     *
     */
    public TreeNode removeLeafNodes(TreeNode root, int target) {
        // ?? edge
        if(root == null){
            return null;
        }
//        if(root.val == target){
//            if(root.left == null && root.right == null){
//                return null;
//            }
//            // ???
//            if(root.left == null || root.right == null){
//                if(root.left == null){
//                    return root.right;
//                }
//                if(root.right == null){
//                    return root.left;
//                }
//            }
//        }

        TreeNode res = deleteNodeHelper2(root, target);
        return res;
    }

    // POST ORDER + DFS ??
    private TreeNode deleteNodeHelper2(TreeNode root, int target){
        // ?? edge
        if(root == null){
            return null;
        }

        // ???
        TreeNode _left = deleteNodeHelper2(root.left, target);
        TreeNode _right = deleteNodeHelper2(root.right, target);

        // ???
        if(_left == null && _right == null && root.val == target){
            return null;
        }

        root.left = _left;
        root.right = _right;

        return root;
    }


    // LC 1110
    // 14.56 - 15.06
    /**
     *
     *  -> Return the roots of the trees in the
     *  remaining forest. You may return the result in any order.
     *
     *
     *   NOTE:
     *     - After deleting all nodes with a value in to_delete,
     *     we are left with a  `forest (a disjoint union of trees).`
     *
     *  -------------
     *
     *   IDEA 1) POST ORDER DFS ?????
     *
     *
     *  -------------
     */
    // IDEA 1) POST ORDER DFS ?????
     ///  ???
    List<TreeNode> res99 = new ArrayList<>();
    public List<TreeNode> delNodes(TreeNode root, int[] to_delete) {
        // edge


        Set<Integer> set = new HashSet<>();
        for(int x: to_delete){
            set.add(x);
        }

        deleteHelper5(root, set, false);
        return res99;
    }

    // PRE order DFS ??
    private TreeNode deleteHelper5(TreeNode root, Set<Integer> set, boolean isParentDeleted){
        // ???
        if(root == null){
            return null; // ????
        }

        // ???
        if(set.contains(root.val)){
            isParentDeleted = true;
            //return null; // ???
        }

        // ???
        if(isParentDeleted){
            // ??
            if(root.left != null){
                res99.add(root.left);
            }
            if(root.right != null){
                res99.add(root.right);
            }
        }else{
            TreeNode _left = deleteHelper5(root.left, set, isParentDeleted);
            TreeNode _right = deleteHelper5(root.right, set, isParentDeleted);

            // ???
            root.left = _left;
            root.right = _right;
        }

////        TreeNode _left = deleteHelper5(root.left, set, isParentDeleted);
////        TreeNode _right = deleteHelper5(root.right, set, isParentDeleted);
//
//        root.left = _left;
//        root.right = _right;

        return root; // ???
    }


    // LC 1143
    // 7.34 - 44 am
    /**
     *  -> Given two strings text1 and text2,
     *  return the length of
     *  their `longest common subsequence`.
     *  If there is no common subsequence, return 0.
     *
     *
     *  ------------
     *
     *   IDEA 1) BRUTE FORCE
     *
     *   IDEA 2) SLIDE WINDOW
     *
     *   IDEA 3) 2D DP ???
     *
     *    - DP def:
     *        dp[i][j] = max sub seq for t1 [0..i] and t2 [0, ..j ]
     *
     *    - DP eq:
     *        // ??
     *        if t1[i] == t2[j]
     *         dp[i][j] = max (dp[i-1][j], dp[i][j-1] ) + 1
     *
     *
     */
    // 11.02 - 12 am
    // idea 1) 2d DP
    /**
     *   - DP def:
     *
     *       - ??? dp[i][j]: max common seq
     *                    for s1[0...i], s2[0..j]
     *
     *   - DP eq:
     *
     *     - dp[i][j]
     *        = max
     *
     */
    public int longestCommonSubsequence(String text1, String text2) {
        // edge

        int n1 = text1.length();
        int n2 = text2.length();

        int[][] dp = new int[n1 + 1][n2 + 1];

        // ??
        // init
        dp[0][0] = 0; // ???

        // if text2 is null
        for(int i = 1; i < n1 + 1; i++){
            dp[i][0] = 0; // ???
        }

        // if text1 is null
        for(int i = 1; i < n2 + 1; i++){
            dp[0][i] = 0; // ???
        }

        //  longest common subsequence
        int lcs = 0;

        for(int i = 1; i < n1 + 1; i++){
            for(int j = 1; j < n2 + 1; j++){
                //dp[i][j] =
                // ??
                // case 1) text1[i] == text2[j]
                if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1; // ????
                }


                // case 2) text1[i] != text2[j]
                else{
                    dp[i][j] = Math.max(dp[i-1][j], dp[i][j-1]);
                }

                // ??
                lcs = Math.max(lcs, dp[i][j]);
            }
        }


        return lcs;
    }











    // IDEA 3) 2D DP ???
    public int longestCommonSubsequence_99(String text1, String text2) {
        // edge

        int n1 = text1.length();
        int n2 = text2.length();

        // ??
        int[][] dp = new int[n1 + 1][n2 + 1];

        // init ??
        dp[0][0] = 0;
        // ???
//        for(int i = 1; i < n1 + 1; i++){
//            dp[i][0] =
//        }

        /**
         *      *    - DP eq:
         *      *        // ??
         *      *        if t1[i] == t2[j]
         *      *         dp[i][j] = max ( dp[i-1][j], dp[i][j-1] ) + 1
         */
        for(int i = 1; i < n1 + 1; i++){
            for(int j = 1; j < n2 + 1; j++){
                // ??
                if(text1.charAt(i - 1) == text2.charAt(j - 1)){
                   // dp[i][j] = Math.max( dp[i-1][j], dp[i][j-1] ) + 1;
                    dp[i][j] = dp[i-1][j-1] + 1;
                }else{
                    dp[i][j] = Math.max( dp[i-1][j], dp[i][j-1] ); // ????
                }
            }
        }


        // ???
        return dp[n1][n2];
    }


    // LC 1049
    // 8.07 - 26 am
    /**
     *  -> Return the smallest possible weight of the left stone.
     *  If there are no stones left, return 0.
     *
     *   - stones where stones[i] is the weight of the ith stone.
     *
     *
     *  ----------------
     *
     *   IDEA 1) GREEDY / BRUTE FORCE
     *
     *   IDEA 2) 2D DP ???
     *
     *      - DP def:
     *         dp[i,j] = smallest weight result of arr [i, j] // ???
     *
     *
     *      - DP eq:
     *
     *
     *  ----------------
     *
     *   ex 1)
     *   stones = [2,7]
     *
     *    7 - 2 = ans
     *
     *  ex 2)
     *  stones = [2,7, 4, 1]
     *
     *   ->
     *
     *
     */
    // IDEA 2) 2D DP ???
    public int lastStoneWeightII(int[] stones) {
        // ?? edge

        int n = stones.length;
        // ??
        int[][] dp = new int[n+1][n+1];
        // ???
        dp[0][0] = 0;

        // ???
        for(int i = 1; i < n + 1; i++){
            for(int j = 1; j < n + 1; j++){
                // ???? if len is even
                if(j - i + 1 > 0 && (j - i + 1) % 2 == 0){
                    // ???
                    int curVal = Math.abs(stones[j-1] - stones[j-1]);
                    dp[i][j] = Math.min(dp[i][j], dp[i-2][j-2] + curVal);
                }
            }
        }


        return dp[n][n];
    }


    // LC 394
    // 13.16 - 26 pm
    /**
     *   IDEA 1) STACK ?? (FILO)
     *
     *    -> 3 cases
     *
     *     - `[`
     *       - keep adding to stack
     *     - digit  or alpha
     *        - keep adding to stack,
     *        - but if `digit`, need to trigger `multiply` op
     *     - `]`
     *        - pop all cur elements from tmp stack
     *
     */
    //  IDEA 1) STACK ?? (FILO)
    public String decodeString(String s) {
        // edge
        if (s == null || s.isEmpty()) {
            return "";
        }

        // ???
        Stack<String> st = new Stack<>();
        StringBuilder sb = new StringBuilder();

        String digit = "0123456789";

        // ???
        for(char ch: s.toCharArray()){
            // case 1)  != `]`
            // // Case 1: normal char
            if(ch != ']'){
                st.push(String.valueOf(ch));
            }
            // case 2)  `]`
            // Case 2: closing bracket
            else{
                // ??
                //Stack<String> tmpSt = new Stack<>();
                StringBuilder sb1 = new StringBuilder();
                int val1 = 0;

                while (!st.isEmpty() && !Objects.equals(st.peek(), "[")){
//                    String val = st.pop(); // ??
//                    if(digit.contains(val)){
//                        // ???
//                        val1  = val1 * 10 + Integer.valueOf(val);
//                    }else{
//                        sb1.append(val);
//                    }

                    sb1.insert(0, st.pop());
                }

                // pop the `last [ from st`
                st.pop();


                // NOTE !!! below
                StringBuilder numSb = new StringBuilder();
                String nums = "0123456789";

//
//                // multiply ??
//                String tmp2 =  multiply(sb1.toString(), val1);
//                st.add(tmp2);

                while (!st.isEmpty() && nums.contains(st.peek())){
                    numSb.insert(0, st.pop());
                }

                int repeat = Integer.parseInt(numSb.toString());
                String updatedStr = multiply(sb1.toString(), repeat);
                st.push(updatedStr);
            }

        }

        //StringBuilder sb = new StringBuilder();
        // ???
        for(String x: st){
            sb.append(x);
        }

        return sb.toString();
    }



    private String multiply(String s, int times){
        String res = "";
        for(int i = 0; i < times; i++){
            res += s;
        }
        return res;
    }



    // LC 1047
    // 13.58 - 14.08 pm
    /**
     *  -> Return the `final string` after all such `duplicate removals`
     *     have been made.
     *     It can be proven that the answer is unique.
     *
     *   - A duplicate removal consists of choosing
     *     two adjacent and equal letters and removing them.
     *
     *  -------------------
     *
     *   IDEA 1) STACK
     *
     */
    public String removeDuplicates_888(String s) {
        // edge

        Stack<String> st = new Stack<>();
        // ??
        String prev = null; // ???
        for(char ch: s.toCharArray()){
            String x = String.valueOf(ch);
            if(!st.isEmpty() && st.peek().equals(x)){
                st.pop(); // /??
               // continue;
            }else{
                st.add(x);
            }
        }


        // ???
        StringBuilder sb = new StringBuilder();
        for(String y: st){
            sb.append(y);
        }


        return sb.toString();
    }


    // LC 1209
    // 14.32 - 42 pm
    /**
     *
     *  -> Return the final string after all such
     *  duplicate removals have been made.
     *  It is guaranteed that the answer is unique.
     *
     *   - String s
     *   - int k
     *      - k adjacent and equal
     *        letters from s and removing them
     *
     *   - We repeatedly make k duplicate
     *      removals on s until we no longer can.
     *
     *
     *  -----------
     *
     *   IDEA 1) STACK -> special structure
     *       - [ [alphabet, counts] ]
     *
     *
     *  -----------
     *
     */
    class AlphabetCnt{
        String alpha;
        int cnt;

        AlphabetCnt(String alpha, int cnt){
            this.alpha = alpha;
            this.cnt = cnt;
        }
    }

    public String removeDuplicates(String s, int k) {
        // edge

        Stack<AlphabetCnt> st = new Stack<>();
        // ??
        for(char ch: s.toCharArray()){
            // case 1) st != empty && prev == cur and >= k
            String str = String.valueOf(ch);
            if(!st.isEmpty()
                    && st.peek().alpha.equals(str) &&
                    st.peek().cnt + 1 >= k){
                st.pop(); // ???
            }
            // case 2) st == empty or prev != cur or < k
            else{
                if(st.isEmpty()){
                    st.add(new AlphabetCnt(str, 1));
                }else{
                    if(st.peek().alpha.equals(str)){
                        AlphabetCnt alphabetCnt = st.pop();
                        alphabetCnt.cnt += 1;
                        st.add(alphabetCnt);
                    }else{
                        st.add(new AlphabetCnt(str, 1));
                    }
                }
            }
        }

        // ???
        StringBuilder sb = new StringBuilder();
        for(AlphabetCnt y: st){
            //sb.append(y);
            sb.append(mutiply97(y.alpha, y.cnt));
        }


        return sb.toString();
    }



    private String mutiply97(String s, int times){
        String res = "";
        for(int i = 0; i < times; i++){
            res += s;
        }
        return res;
    }


    // LC 358
    // 14.51 - 15.00 pm
    /**
     *
     *  -> Given a string s and an integer k, `rearrange` s
     *    such that the `same characters` are `at least distance k` from each other.
     *    If it is not possible to rearrange the string,
     *    return an empty string "".
     *
     *
     *  ---------------
     *
     *   IDEA 1) STACK + HASHMAP ????
     *
     *   map_1: {val: cnt}
     *
     *   map_2: {val: last_exited_idx }
     *
     *
     *  ---------------
     *
     */
    public String rearrangeString(String s, int k) {
        // edge
        if (k <= 1) return s;        // no distance constraint
        if (s == null || s.length() == 0) return "";
        if (s.length() == 1) return k == 0 ? s : "";


        // frequency map
        Map<String, Integer> map1 = new HashMap<>();

        Map<String, Integer> map2 = new HashMap<>();

        // NOTE !!!

        // Max heap by remaining count
        PriorityQueue<Character> pq =
                new PriorityQueue<>((a, b) -> map1.get(b) - map1.get(a));


       // pq.addAll(map1.keySet());



        // ??
        //int idx = 0;
        for(char ch: s.toCharArray()){
            String str = String.valueOf(ch);
            // update map1
            map1.put(str, map1.getOrDefault(str, 0) + 1);

            // update map2 ???
            map2.put(str, -1); // ???

            //idx += 1;
        }

        // ??
        Stack<String> st = new Stack<>();
        StringBuilder sb = new StringBuilder();

        // ??
        int idx = 0;
        while (!map1.isEmpty()){
            // ???
            // case 1) st is empty or `last alpha is `k idx far away from` cur string
            if(st.isEmpty() ||
                    st.peek() != String.valueOf(sb.charAt(sb.length()-1)) ||
                    map2.get(st.peek()) - idx > k){
             //   sb.append(map)
            }

            idx +=1;
        }



        return sb.toString();
    }


    // LC 309
    // 8.22 - 32 am
    /**
     *  -> Find the maximum profit you can achieve.
     *
     *   - prices[i] is the price of a given stock on the ith day.
     *
     *
     *   NOTE:
     *
     *    - After you sell your stock,
     *      you cannot buy stock on the next day
     *      (i.e., cooldown `one` day).
     *
     *  --------------
     *
     *
     *   IDEA 1) BRUTE FORCE
     *
     *   IDEA 2) 2D DP ????
     *
     *     - DP def:
     *
     *         - dp[i][j]:
     *            - max profit can get at `i th` day
     *            - j: op types:
     *               - 0: cool down
     *               - 1: buy
     *               - -1: sell
     *
     *     - DP eq:
     *        // ???
     *        dp[i][j] =
     *           max (
     *
     *
     *  --------------
     *
     *
     */
    // 7.21 - 31 am
    // // IDEA 2) 2D DP ????
    public int maxProfit(int[] prices) {
        // edge
        if (prices == null || prices.length <= 1)
            return 0;



        int n = prices.length;
        // ??
        //int[][] dp = new int[n + 1][3];
        int[][] dp = new int[n][3];

        /**  NOTE: the `day 0` base case
         *
         *  -> we init day 0 per each case
         *   - 0: `bought` OP
         *   - 1: `sold` op
         *   - 2: `do nothing` op
         */

        // ???
        dp[0][0] = -1 * prices[0];
        dp[0][1] = 0;
        dp[0][2] = 0;


        // init
        for(int i = 1; i < n + 1; i++){
            // case 1) hold
            //        -> prev day: SOLD!!!! or  `do nothing` ???
            //dp[i][2] = Math.max(dp[i-1][0], dp[i-1][2]);
            dp[i][2] = Math.max(dp[i-1][1], dp[i-1][2]);

            // case 2) sale
            //       -> prev day: buy ???
            //dp[i][1] = Math.max(dp[i-1][0], dp[i-1][2]) +  prices[i];
            dp[i][1] = dp[i-1][0] +  prices[i];

            // case 3) buy ???
            //      -> prev day: `do nothing` ???
            // // Either you held it yesterday OR you were resting yesterday and bought today
            dp[i][0] = Math.max(dp[i-1][2] - prices[i], dp[i-1][2]);

        }


        return Math.max(dp[n - 1][1], dp[n - 1][2]);
    }









    // IDEA 2) 2D DP ????
    public int maxProfit_99(int[] prices) {
        // edge
        if (prices == null || prices.length <= 1)
            return 0;



        int n = prices.length;
        // ??
        //int[][] dp = new int[n + 1][3];
        int[][] dp = new int[n][3];


        int maxProfit = 0; // ???

        // Base Case: Day 0
        /**  NOTE: the `day 0` base case
         *
         *  -> we init day 0 per each case
         *   - 0: `bought` OP
         *   - 1: `sold` op
         *   - 2: `do nothing` op
         */
        // init ???
        dp[0][0] = -1 * prices[0];
        dp[0][1] = 0;
        dp[0][2] = 0; // /??

        for(int i = 1; i < n; i++){
            // 1) if hold
            dp[i][2] = Math.max(dp[i-1][1] + prices[i-1], dp[i-1][2]);

            // ????
            dp[i][0] = dp[i-1][2] + prices[i-1]; // Math.max(dp[i-1][2] + prices[i-1], dp[i-1][2]);

            // 2) if sell ???
            dp[i][1] = Math.max(dp[i-1][0] - prices[i-1], dp[i-1][2]);

            maxProfit = Math.max(
                    maxProfit,
                    Math.max(
                            Math.max(dp[i][2], dp[i][1]
                            ),
                            dp[i][0]
                    )
            );
        }

        return maxProfit;
    }


    // LC 518
//    public int change(int amount, int[] coins) {
//
//        return 0;
//    }


    // LC 153
    // 15.26 - 36 pm
    /**
     *
     *  -> Given the sorted rotated array nums of
     *  unique elements, return the minimum element of this array.
     *
     *
     *  --------------
     *
     *   IDEA 1) BRUTE FORCE
     *
     *   IDEA 2) BINARY SEARCH
     *
     *  --------------
     *
     *
     *   ex 1)
     *
     *   [11,13,15,17]
     *
     *   [17,11,13,15]
     *
     *   [15,17,11,13]
     *
     *   [13, 15,17,11]
     */
    // 17.24 - 45 pm
    public int findMin(int[] nums) {
        // edge

        //int len = nums.length;
        int l = 0;
        int r = nums.length - 1;

        // already sorted (small -> big)
        if(nums[r] >= nums[l]){
            return nums[l];
        }


        /** NOTE !!!
         *
         *  ONLY 2 cases:
         *
         *   1. nums[mid] >= nums[l]
         *   2. nums[mid] < nums[r]
         *
         *
         *  ---------------
         *
         *   DON'T just memorize, get understand via below practical example:
         *
         *             // [1,2,3,4,5]     // already in acending order
         *             // [5,1,2,3,4]     // mid < r, right part is sorted
         *             // [4,5,1,2,3]     // mid < r, right part is sorted
         *             // [3,4,5,1,2]     // mid >= l, left part is sorted
         *             // [2,3,4,5,1]     / mid >= l, left part is sorted
         *
         *             // cycle
         *             // [1,2,3,4,5]
         *
         *
         *
         *     -> so via above, we are VERY CLEAR on
         *        why we need to check `nums[mid] >= nums[l]` and else
         */

        int res = 5000 + 1; // ???
        while (r >= l){
            /**  Using `practical example`
             *
             *  [0,1,2,4,5,6,7]   already in ascending order
             *  [7,0,1,2,4,5,6]   mid belong to right
             *  [6,7,0,1,2,4,5]   mid belong to right
             *  [5,6,7,0,1,2,4]   mid belong to right
             *  [4,5,6,7,0,1,2]   mid belong to left
             *  [2,4,5,6,7,0,1]   mid belong to left
             *  [1,2,4,5,6,7,0]   mid belong to left
             *
             *  ---- cycle -------
             *  [0,1,2,4,5,6,7]
             *
             */

            int mid = l + (r - l) / 2;
            res = Math.min(res, nums[mid]);
            // if already sorted (ascending)
            if(nums[l] <= nums[r]){
                res = Math.min(res, nums[l]);
                //return nums[l]; /// ????
                return res;
            }
            // ???
            if(nums[mid] <= nums[r]){
                //l = mid + 1;
                r = mid - 1;
            }else{
               // r = mid - 1;
                l = mid + 1;
            }

        }

        return res; // should NOT visit here ???
    }







    // IDEA 2) BINARY SEARCH
    public int findMin_96(int[] nums) {
        // edge

        int len = nums.length;
        // already sorted (small -> big)
        if(nums[len - 1] > nums[0]){
            return nums[0];
        }

        // ??
        int l = 0;
        int r = len - 1;
        // ???

        while (r >= l){

            int mid = l + (r - l) / 2;

            // [1,2,3,4,5]     // already in acending order
            // [5,1,2,3,4]     // mid < r, right part is sorted
            // [4,5,1,2,3]     // mid < r, right part is sorted
            // [3,4,5,1,2]     // mid >= l, left part is sorted
            // [2,3,4,5,1]     / mid >= l, left part is sorted
            // cycle
            // [1,2,3,4,5]

            // KEY !!!
            //
        }


        return -1;
    }


    public int findMin_99(int[] nums) {
        // edge

        int len = nums.length;
        // already sorted (small -> big)
        if(nums[len - 1] > nums[0]){
            return nums[0];
        }

        // ??
        int l = 0;
        int r = len - 1;
        // ???
        while (r >= l){
            int mid = l + (r - l) / 2;

            // ??? if already sorted ??
            if(nums[mid] >= nums[0]){
                return nums[0];
            }

            // [3,4,5,1,2]
            // case 1) mid belongs `left`
            // ???
            if(nums[mid] >= nums[l]){
                // /?
                if(nums[l] < nums[mid + 1]){
                    r = mid - 1;
                }else{
                    l = mid + 1;
                }
            }
            // [5,1,2,3,4]
            // case 2) mid belongs `right` ???
            else{
                if(nums[mid] <= nums[r]){
                    // ??
                    if(nums[mid] < nums[l]){
                        r = mid - 1;
                    }else{
                        l = mid + 1;
                    }
                }
            }

        }

        return l; // ????
    }


    // LC 336
    // 16.06 - 16 pm
    /**
     * -> Return an array of all the palindrome pairs of words.
     *
     *  - 0 idx arr
     *
     *  - palindrome pair:
     *
     *    - 0 <= i, j < words.length,
     *    - i != j, and
     *    - words[i] + words[j]
     *      (the concatenation of the two strings)
     *       is a palindrome.
     *
     *
     * ------------
     *
     *  IDEA 1) ?? BRUTE FORCE + `palindrome` check ???
     *
     *  IDEA 1) hashmap + `palindrome check`
     *
     *
     * ------------
     *
     *  ex 1)
     *
     *  sssll ->  ll sssll
     *
     *
     */
    // 16.45 - 55 pm
    // IDEA: STR OP + `palindrome` check
    public List<List<Integer>> palindromePairs(String[] words) {
        // edge


        // ???
        List<List<Integer>> res = new ArrayList<>();
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < words.length; i++){
            map.put(words[i], i);
        }

        // ???
        // double loop
        //    -> 1. word 2. idx of word
        //    -> within double loop
        //    -> we check if sub str if `Palindrome`

        // ??
        for(int k = 0; k < words.length; k++){

            // ???
            String w = words[k];

            for(int i = 0; i < w.length(); i++){

                // /??
                //String subStr = w.substring(0,i);

                String leftSub = w.substring(0, i);
                String rightSub = w.substring(i, w.length() - 1);

                /** NOTE !!!
                 *
                 *  2 cases
                 *
                 *  - case 1) left is palindrome
                 *  - case 2) right is palindrome
                 *
                 */

                // - case 1) left is palindrome
                if(isPalindrome5(leftSub)){
                    String revRight = new StringBuilder(rightSub).reverse().toString();
                    if(map.containsKey(rightSub) && map.get(revRight) != k){
                        res.add(Arrays.asList(map.get(revRight), k));
                    }
                }

                // - case 2) right is palindrome
                if(isPalindrome5(rightSub)){
                    String revLeft = new StringBuilder(leftSub).reverse().toString();
                    if(map.containsKey(leftSub) && map.get(revLeft) != k){
                        res.add(Arrays.asList(map.get(revLeft), k));
                    }

                }




//
//
//                // case 1) w[0,i] is `Palindrome` already
//                if(isPalindrome5(subStr)){
//                    String reversedStr = new StringBuilder(subStr).reverse().toString();
//                    if(map.containsKey(reversedStr)){
//                        // ???
//                        res.add(Arrays.asList(i, map.get(reversedStr)));
//                    }
//                }
//                // case 2) w[0,i] is NOT `Palindrome`, check if there existed the other word
//                //         can make `w[0,i] + x`  as `Palindrome`
//                else{
//                    String reversedStr = new StringBuilder(subStr).reverse().toString();
//                    if(map.containsKey(reversedStr)){
//                        // ???
//                        res.add(Arrays.asList(i, map.get(reversedStr)));
//                    }
//                }
//

            }
        }


        return res;
    }



    private boolean isPalindrome5(String str){
        if(str == null || str.isEmpty() || str.length() <= 1){
            return true;
        }
        // ??
        StringBuilder sb = new StringBuilder(str);
        return sb.reverse().toString().equals(str);
    }







    public List<List<Integer>> palindromePairs_93(String[] words) {
        // edge

        // ???
        List<List<Integer>> res = new ArrayList<>();
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < words.length; i++)
            map.put(words[i], i);


        //List<List<Integer>> res = new ArrayList<>();

        for (int i = 0; i < words.length; i++) {

            // ???
            String word = words[i];
            // ?? loop over word idx ??

            for(int j = 0; j < word.length(); j++){

                // ????
                String str1 = word.substring(0, j);
                String str2 = word.substring(j);


                // Case 1: If str1 is a palindrome, find the reverse of str2
                if(isPalindrome(str1)){
                    String reverse2 = new StringBuilder(str2).reverse().toString();
                    if(map.containsKey(reverse2) && map.get(reverse2) != i){
                       // res.add(Arrays.asList())
                        res.add(Arrays.asList(i, map.get(reverse2)));
                    }
                }

                // Case 2: If str2 is a palindrome, find the reverse of str1
                // ???
                // NOTE !!!!
                if(isPalindrome(str2) && j < word.length() ){
                    String reverse1 = new StringBuilder(str1).reverse().toString();
                    if(map.containsKey(reverse1) && map.get(reverse1) != i){
                        // res.add(Arrays.asList())
                        res.add(Arrays.asList(i, map.get(reverse1)));
                    }
                }

            }


        }



        return res;
    }




    // time: O(N ^ 2)
    // space: O(N)
    public List<List<Integer>> palindromePairs_99(String[] words) {
        // edge

        List<List<Integer>> res = new ArrayList<>();
        // /??
        HashSet<String> visited = new HashSet<>();
        // ??
        for(int i = 0; i < words.length; i++){
            // ???
            for(int j = i + 1; j < words.length; j++){
                // ???
                String str1 = words[i] + words[j];
                String str2 = words[j] + words[i];

                // ???
                if(isPalindrome(str1) && !visited.contains(str1)){
                    List<Integer> list = new ArrayList<>();
                    list.add(i);
                    list.add(j);
                    res.add(list);

                    visited.add(str1);
                }

                if(isPalindrome(str2) && !visited.contains(str2)){
                    List<Integer> list = new ArrayList<>();
                    list.add(j);
                    list.add(i);
                    res.add(list);

                    visited.add(str2);
                }

            }
        }

        return res;
    }


    private boolean isPalindrome(String str){
        if(str == null || str.isEmpty() || str.length() <= 1){
            return true;
        }
        // ??
        StringBuilder sb = new StringBuilder(str);
        return sb.reverse().toString().equals(str);
    }


    // LC 1032
    // 17.32 - 42 pm
    /**
     *  -> checks if a suffix of these characters is
     *  a string of a given array of strings words.
     *
     *
     *  ----------------
     *
     *   IDEA 1) TRIE -> REVERSE -> CHECK ???
     *
     *   IDEA 2) prefix  ???
     *
     *   IDEA 3) reverse + startWith ???
     *
     *  ----------------
     *
     */
    class StreamChecker {
        // attr
        Map<String, Integer> map;
        List<String> list;

        public StreamChecker(String[] words) {
            this.map = new HashMap<>();
            this.list = new ArrayList<>(); // ???
            for(String w: words){
                StringBuilder sb = new StringBuilder(w);
                String str = sb.reverse().toString();

                map.put(str, 1); // ???

                this.list.add(str);
            }

            System.out.println(">>> this.map = " + this.map);

        }

        public boolean query(char letter) {
            // edge
            // ???
            if(String.valueOf(letter).isEmpty()){
                return true; // ???
            }

            //StringBuilder sb = new StringBuilder();
            // ??
            String str = String.valueOf(letter);
            // ??
            for(String k: this.map.keySet()){
                // ??
                if(k.equals(str) || k.startsWith(str)){
                    return true;
                }
            }

            // ???
            return false;
        }
    }


    // LC 97
    // 11.08 - 18 am
    /**
     *  ->  find whether
     *      `s3` is formed by an
     *        interleaving of  `s1 and s2.`
     *
     *   given str: s1, s2, s3
     *
     *
     *
     *
     *  ---------------
     *
     *   IDEA 1) BRUTE FORCE ???
     *
     *   IDEA 2) DP ???? + SUB STRING OP ????
     *
     *    - DP def:
     *
     *      - boolean dp[i][j]:
     *             - can s1[..i], s2[..j] form s3[.. i+j]  ???
     *
     *    - DP eq:
     *
     *
     *  ---------------
     *
     *
     *
     */
    // 16.12 - 22 pm
    // IDEA 1) 2D DP
    public boolean isInterleave(String s1, String s2, String s3) {
        // edge

        int size1 = s1.length();
        int size2 = s2.length();
        int size3 = s3.length();

        if(size1 + size2 != size3){
            return false;
        }

        // ??
        boolean[][] dp = new boolean[size1 + 1][size2 + 1];

        // init ??
        dp[0][0] = true;  //???


        // if only s1
        // end idx = ????
        for(int i = 1; i < size1 + 1; i++){
           // dp[i][0] = s1.charAt(i - 1) == s3.charAt(i - 1); // ???
            dp[i][0] = ( s1.charAt(i - 1) == s3.charAt(i - 1) )
                    &&  dp[i - 1][0]; // ???
        }

        // if only s2
        for(int i = 1; i < size2 + 1; i++){
            //dp[0][i] = s2.charAt(i - 1) == s3.charAt(i - 1); // ???
            dp[0][i] = (s2.charAt(i - 1) == s3.charAt(i - 1))
                    && dp[0][i- 1]; // ???
        }

        // ????
        // fill DB table
        for(int i = 1; i < size1 + 1; i++){
            for(int j = 1; j < size2 + 1; j++){

                // NOTE !!!
                char c3 = s3.charAt(i + j - 1);

//                // ????
//                if(s1.charAt(i-1) != s3.charAt(i + j - 1) && s2.charAt(j - 1) != s3.charAt(i + j - 1)){
//                    dp[i][j] = false;
//                }
//                // ???
//                dp[i][j] = dp[i- 1][j] || dp[i][j - 1];

                boolean formS1 = s1.charAt(i-1) == c3 && dp[i - 1][j];
                boolean formS2 = s2.charAt(j-1) == c3 && dp[i][j - 1];

                // ???
                dp[i][j] = formS1 || formS2;
            }
        }


        // ???
        //return dp[size1 + 1][size2 + 1];
        return dp[size1][size2];
    }













    public boolean isInterleave_99(String s1, String s2, String s3) {
        // edge

        // ???
        int size1 = s1.length();
        int size2 = s2.length();
        int size3 = s3.length();

        // Edge case: total length must match
        if (size1 + size2 != size3) return false;

        // ??? ??? size or size + 1
        boolean[][] dp = new boolean[size1 + 1][size2 + 1]; // ????

        dp[0][0] = true; // ???

        // NOTE !!! below init (ONLY s1 or ONLY s2)
        // Initialize first column (only s1 contributes)
        for (int i = 1; i <= size1; i++) {
            dp[i][0] = dp[i - 1][0] && s1.charAt(i - 1) == s3.charAt(i - 1);
        }

        // Initialize first row (only s2 contributes)
        for (int j = 1; j <= size2; j++) {
            dp[0][j] = dp[0][j - 1] && s2.charAt(j - 1) == s3.charAt(j - 1);
        }


        for(int i = 1; i < size1 + 1; i++){
            for(int j = 1; j < size2 + 1; j++){
                // ???
                // case 1) s1 char.at == s3 && s2 char.at != s3
                // case 2) s1 char.at != s3 && s2 char.at == s3
                // case 3) s1 char.at == s3 && s2 char.at == s3
                // case 4) s1 char.at != s3 && s2 char.at != s3


                // NOTE !!!
                char c = s3.charAt(i + j - 1); // ???

                // Case 1: take from s1
                if(s1.charAt(i - 1) == c){
                    dp[i][j] = dp[i-1][j] || dp[i][j]; // ????
                }

                // Case 2: take from s2
                if(s2.charAt(j - 1) == c){
                    dp[i][j] = dp[i][j-1] || dp[i][j]; // ????
                }
            }
        }




        // ??
        return dp[size1][size2];
    }


    // LC 1353
    // 11.51 - 12.01 pm
    /**
     *  -> Return the maximum number of events you can attend.
     *
     *   - You can only attend one event at any time d.
     *
     *  -------------
     *
     *   IDEA 1) SCANNING LINE ??
     *
     *   IDEA 2) PQ ???
     *
     *   IDEA 3) BRUTE FORCE
     *
     *   IDEA 4) INTERVAL OP
     *
     *
     *  -------------
     *
     *
     *
     */
    // IDEA 4) INTERVAL OP
    public int maxEvents(int[][] events) {
        // edge
        if(events == null){
            return 0;
        }
        if(events.length == 1){
            return 1;
        }

        List<Integer[]> list = new ArrayList<>();
        for(int[] e: events){
//            list.add(new Integer[]{e[0], 1});
//            list.add(new Integer[]{e[1], -1});
            list.add(new Integer[]{e[0], e[1]});
        }

        // ???
        Collections.sort(list, new Comparator<Integer[]>() {
            @Override
            public int compare(Integer[] o1, Integer[] o2) {
                int diff = o1[0] - o2[0];
                if(diff != 0){
                    return o1[1] - o2[1];
                }
                return diff;
            }
        });

        // ??? sort on
        // 1) open (small -> big) // ???
        // 2) close (small -> big) // ???
        System.out.println(">>> list = " + list);



        int maxAttend = 0;
        int prevStart = -1;
        int prevEnd = -1;

        // ??
        for(Integer[] x: list){
            int start = x[0];
            int end = x[1];
            // case 1) prev == -1 or `NOT overlap`
            /**  NOTE !!
             *
             *  since we already sorted on 1) start 2) end (small -> big),
             *
             *
             *  - the OVERLAP case are as below:
             *
             *   |-----|       prev
             *     |-------|   cur
             *
             *
             *  |------|
             *    |--|
             *
             *  |------ |
             *  |----|
             *
             *
             *  -------
             *
             *   the NON OVERLAP case as below:
             *
             *   |-----|
             *            |-----|
             *
             *
             */
            if( (prevStart == -1 && prevEnd == -1) || (start > prevEnd)  ){
                prevStart = Math.min(start, prevStart);
                prevEnd = Math.min(end, prevEnd);
                maxAttend += 1;
            }
        }



        return maxAttend;
    }


    // LC 1481
    // 12.32 - 42 pm
    /**
     *
     *  ->  Find the `least number` of unique integers
     *  after `removing ``exactly k` elements.
     *
     *
     *  --------------
     *
     *  IDEA 1) PQ + hashmap ????
     *
     *   -> min PQ sort on freq
     *   -> pop till meet req
     *
     *
     *
     */
    //  IDEA 1) PQ + hashmap ????
    public int findLeastNumOfUniqueInts(int[] arr, int k) {
        // edge

        // map: { val : cnt}
        Map<Integer, Integer> map = new HashMap<>();
        for(int x: arr){
            map.put(x, map.getOrDefault(x, 0) + 1);
        }

        // ??
        PriorityQueue<Integer> pq = new PriorityQueue<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                // ??? ??
                // sort on map val ????
                int diff = map.get(o1) - map.get(o2);
                return diff;
            }
        });

       // int res = 0;

        // ??
       for(Integer key: map.keySet()){
           pq.add(key);
       }

       // ??
        while (!pq.isEmpty() && k > 0){
            k -= 1; // ???
            Integer cur = pq.poll();
            if(map.get(cur) - 1 == 0){
                map.remove(cur);
            }else{
                map.put(cur, map.get(cur) - 1);
                // put back to PQ
                pq.add(cur);
            }
        }


        // /??
        return map.keySet().size();
    }


    // LC 1140
    // 15.46 - 56 pm
    /**
     * ->
     * Assuming Alice and Bob play optimally,
     * return the `maximum` number of stones `Alice` can get.
     *
     *  - piles: # of stone at idx = i
     *
     *  - game op:
     *      - On each player's turn, that player
     *        can take all the stones in the first X remaining piles,
     *        where 1 <= X <= 2M. Then,
     *        we set M = max(M, X). Initially, M = 1.
     *
     *
     *    - The game continues until all the stones have been taken.
     *
     *
     * -----------
     *
     *   IDEA 1) BRUTE FORCE ??
     *
     *   IDEA 2) 2D DP ??
     *
     *    - DP def:
     *        dp[i][j]: max # of stone one can get between piles[j] ????
     *                  with first i piles ???
     *
     *    - DP eq:
     *       dp[i][j] =
     *            dp[i][j] =  dp[i][j] +
     *
     *
     *
     *   IDEA 3) 1D DP ??
     *
     *
     * -----------
     *
     *
     */
    // IDEA 2) 2D DP ??
    public int stoneGameII(int[] piles) {

        return 0;
    }

    // LC 871
    // 11.25 - 35 am
    /**
     *
     *  IDEA 1) DP
     *
     *  IDEA 2) PQ ???
     *
     *
     */
    // IDEA 1) DP
    public int minRefuelStops(int target, int startFuel, int[][] stations) {

        return 0;
    }


    // LC 329
    public int longestIncreasingPath(int[][] matrix) {

        return 0;
    }


    







}
