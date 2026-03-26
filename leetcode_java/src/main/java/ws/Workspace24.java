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
    public boolean isAlienSorted(String[] words, String order) {
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
    // IDEA 1) DFS + GRAPH
    public boolean validPath(int n, int[][] edges, int source, int destination) {
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









}
