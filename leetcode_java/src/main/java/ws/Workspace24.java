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





}
