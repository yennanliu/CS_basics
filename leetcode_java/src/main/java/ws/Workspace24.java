package ws;

import LeetCodeJava.DataStructure.ListNode;
import LeetCodeJava.DataStructure.TreeNode;

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
    // IDEA 1) char op + looping ?? ???
    public boolean isAlienSorted(String[] words, String order) {
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
    //  IDEA 1) BFS
    public int numBusesToDestination(int[][] routes, int source, int target) {
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
    // map { node: path }
    //Map<TreeNode, String> nodePath = new HashMap<>();
    int deepest = 0;
    TreeNode resNode = null;
    public TreeNode subtreeWithAllDeepest(TreeNode root) {
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
    //  IDEA 1) DFS
    public double[] calcEquation(List<List<String>> equations, double[] values, List<List<String>> queries) {
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







}
