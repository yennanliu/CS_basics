package dev;

import LeetCodeJava.DataStructure.TreeNode;

import java.util.*;

public class workspace5 {

    // LC 430
    // 7.23 pm - 7.40 pm
    /*
    // Definition for a Node.
    class Node {
        public int val;
        public Node prev;
        public Node next;
        public Node child;
    };
    */
    // recursive

    // attr
    Node res = new Node();
    //Node ans = new Node();

    class Node {
        public int val;
        public Node prev;
        public Node next;
        public Node child;
    };

    public Node flatten(Node head) {
        if(head == null){
            return head;
        }
        if (head.next == null && head.child == null){
            return head;
        }
        Node node = new Node();
        Node ans = node;

        Node cur = head; // /???

        /**
         *  if without "child"
         *  then it's a regular bi-linked list
         *  -> so we deal with child node via dfs (recursive)
         *  -> and deal with next node via while loop (iteration)
         */
        while(cur != null){

            Node _next = cur.next;
            cur.next = cur.child; // NOTE !!! need to flatten
            cur.next.prev = cur; // NOTE !!! need to connect to prev

            Node _child = this.dfs(cur.child);

            _child.next = _next;

            if (_next != null){
                _next.prev = _child;
            }

            cur.child = null;

            //Node _cur = new Node();

//            while(_child != null){
//                Node _next2 = _child.next;
//                _cur.next = _next2;
//                _child = _next2;
//                _cur = _cur.next;
//            }
//
//            if (_child == null){
//                _cur.next = _next; // ??
//            }

        }

        return ans;
    }

    private Node dfs(Node head){
        if (head == null){
            return null;
        }
        while (head.child != null){
            Node _child = head.child;
            head.child = this.dfs(head);
            head = _child;
        }
        return head;
    }

//    public Node flatten(Node head) {
//
//        Queue<Node> store = new LinkedList<>();
//        /**
//         *     /**
//         *      * Inserts the specified element into this queue if it is possible to do
//         *      * so immediately without violating capacity restrictions.
//         *      * When using a capacity-restricted queue, this method is generally
//         *      * preferable to {@link #add}, which can fail to insert an element only
//         *      * by throwing an exception.
//         */
//        //store.offer(new Node());// ?
//
//        if (head == null){
//            return head;
//        }
//
//        if (head.child == null && head.next == null){
//            return head;
//        }
//
//        Node res = new Node();
//        Node ans = res;
//        while(head.next != null){
//            dfs(head);
//            Node _next = head.next;
//            head = _next;
//        }
//
//        return ans;
//    }
//
//    private void dfs(Node head){
//        while (head.child != null){
//            Node _child = head.child;
//            Node _child_res = this.res.child; // ??
//            this.res.child = _child;
//            head = _child;
//            this.res = _child_res; // ???
//        }
//        //return this.res;
//    }

    // LC 1011
    // 8.23 pm - 8.40 pm
    public int shipWithinDays(int[] weights, int days) {

        if (weights.length==1){
            return weights[0] / days;
        }

        List<Integer> weightsList = new ArrayList<>();
        int maxWeight = 0;
        int totalWeight = 0;
        for (int w : weights){
            weightsList.add(w);
            maxWeight = Math.max(maxWeight, w);
            totalWeight += w;
        }

        int left = maxWeight;
        int right = totalWeight;
        // binary search
        while(right > left){
            int mid = (left + right) / 2;
            int calculatedDays = getDays(weightsList, mid);
            System.out.println(">>> mid = " + mid  + ", maxWeight = " + maxWeight + ", totalWeight = " + totalWeight);
            // need to return max possible speed within D days
            if (calculatedDays <= days){
                right = mid;
            }else{
                left = mid+1;
            }
        }

        return left; // ??? or return mid
    }

    private int getDays(List<Integer> weightsList, int speed){
        int cur = 0;
        int days = 0;
        for (Integer w :weightsList){
            if (cur + w <= speed){
                cur += w;
            }else{
                days += 1;
                cur = w;
            }
        }
        if (cur > 0){
            days += 1;
        }
        System.out.println(">>> speed = " + speed + ", days = " + days);
        return days;
    }

    // LC 939
    // 7.41 - 8.10 pm

    /**
     *  NOTE !!!
     *
     *   Return the minimum area of a rectangle ...
     *
     *
     *   exp 1:
     *
     *   Input: points = [[1,1],[1,3],[3,1],[3,3],[2,2]]
     *   Output: 4
     *
     *
     *
     *   idea  1:
     *     get min "x len"
     *     and get min "y len"
     *     then ans = x_len * y_len
     *
     *  idea 2 :
     *
     *    hash map
     *
     *    {"x": [y1, y2,,,],
     *    "y" : [x1, x2,,,,]
     *    }
     *
     */
    // 2,53 -> 3.05
    public int minAreaRect(int[][] points) {

        // build map
        Map<Integer, Set<Integer>> mapX = new HashMap<>();
        //Map<Integer, List<Integer>> mapY = new HashMap<>();

        for (int[] point : points){

            int x = point[0];
            //int y = point[1];

            // xList : list of "y coordination" with given x
            Set<Integer> xList =  mapX.getOrDefault(x, new HashSet<>());
            //List<Integer> yList =  mapY.getOrDefault(x, new ArrayList<>());

//            if(!xList.contains(y)){
//                xList.add(x);
//            }
            xList.add(x);
            //yList.add(y);

            mapX.put(x, xList);
            //mapY.put(y, yList);
        }

        int res = Integer.MAX_VALUE;

        for (int i = 0; i < points.length; i++){
            // NOTE !! int j = i+1; j <points.length; j++
            // not j = 0
            for (int j = i+1; j <points.length; j++){
                int x1 = points[i][0];
                int y1 = points[i][1];
                int x2 = points[j][0];
                int y2 = points[j][1];
                // check
                // mapX.containsKey(x1) :  there is a y coordination with x1
                // !mapX.get(x1).equals(y1) ; the y coordination is not (x1, y1) (not itself)
                //boolean canFormRectangle = (x1 != x2 && y1 != y2 && mapX.containsKey(x1) && mapX.containsKey(x2) && !mapX.get(x1).equals(y1) && !mapX.get(x2).equals(y2));
                boolean canFormRectangle = mapX.get(x1).contains(y2) && mapX.get(x2).contains(y1);
                if (x1 != x2 && y1 != y2){
                    if (canFormRectangle){
                        int area = Math.abs(x2 - x1) * Math.abs(y2 - y1);
                        res = Math.min(res, area);
                    }
                }
            }
        }

        return res == Integer.MAX_VALUE ? 0 : res;
    }

//    public int minAreaRect(int[][] points) {
//        // edge case
////        if (points.length == 0 && points[0].length == 0){
////            return 0;
////        }
//        if (points.length == 1 || points[0].length == 1) {
//            return points.length * points[0].length;
//        }
//        // init
//        int x_len = 0;
//        int y_len = 0;
//        // find min x_len and min y_len
//
//        return x_len * y_len;
//    }

    // LC 1087
    // https://leetcode.ca/2018-11-21-1087-Brace-Expansion/
    // 6.19 pm - 6.30 pm
    // TODO : implement, validate
    List<String> collected = new ArrayList<>();

    public String[] expand(String s) {
        if (s.length() < 3){
            return new String[]{s};
        }

        // prepare list

        // dfs (backtrack)
        //String[] x = new String[]{};
        return null;
    }

    private List<String> getCandidates(String s){
        return null;
    }

    private void backtrack(int startIdx, List<String> candidates, List<String> cur){
        if (cur.size() == candidates.size()){
            StringBuilder sb = new StringBuilder();
            for (String x : cur){
                sb.append(x);
            }
            String str = sb.toString();
            if (!this.collected.contains(str)){
                this.collected.add(str);
            }
        }
        if (cur.size() > candidates.size()){
            return;
        }
        // ??? check
        for (int i = startIdx; i < candidates.size(); i++) {
            // update cur
            cur.add(candidates.get(i));
            // recursive
            this.backtrack(i, candidates, cur);
            // undo
            cur.remove(cur.size() - 1);
        }
    }


    // 5.02 pm - 5.20 pm
//    public static void main(String[] args) {
//        System.out.println(this.expand(s));
//    }

//    List<String> collected = new ArrayList<>();
//    List<String> regular = new ArrayList<>();
//    public String[] expand(String s) {
//
//        // no possible to have bracket ("{}")
//        if (s.length() < 3){
//            return new String[]{s};
//        }
//        List<String> candidates = new ArrayList<>();
//        for (String x : s.split("")){
//            Queue<String> q = new LinkedList<>();
//            boolean tooAdd = false;
//            if (x == "{"){
//                tooAdd =  true;
//                break;
//            }
//            else if (!tooAdd){
//                q.add(x);
//                break;
//            }
//            else if (x == "}"){
//                tooAdd = false;
//                StringBuilder sb = new StringBuilder();
//                while(!q.isEmpty()){
//                    sb.append(q.poll());
//                    candidates.add(sb.toString());
//                }
//            }else{
//            }
//
//
//        }
//
//
//        // dfs
//        //List<String> collected = new ArrayList<>();
//        String[] x= (String[]) collected.toArray();
//        // order with lexicographical
//
//        return (String[]) collected.toArray(); // TODO : double check
//    }
//
//    int strLen = 5; // TODO : fix
//    private void dfs(List<String> regular, List<String> candidates, int startIdx, String cur){
//        if (candidates.size()==0){
//            return;
//        }
//        if (cur.length() == strLen){
//            this.collected.add(cur);
//            cur = "";
//            return;
//        }
//        if (cur.length() > strLen){
//            cur = "";
//            return;
//        }
//
//    }

    // LC 981
    // https://leetcode.com/problems/time-based-key-value-store/
    // 4.25 pm
    /**
     * Your TimeMap object will be instantiated and called as such:
     * TimeMap obj = new TimeMap();
     * obj.set(key,value,timestamp);
     * String param_2 = obj.get(key,timestamp);
     *
     *
     *
     *  NOTE :
     *
     * String get(String key, int timestamp)
     * Returns a value such that set was called previously,
     * with timestamp_prev <= timestamp. If there are multiple such values,
     * it returns the value associated with the largest timestamp_prev.
     * If there are no values, it returns "".
     *
     */
    /**
     *
     *  exp 1:
     *
     *  input : "foo", "bar", 1
     *
     *  {"foo" :  ["bar", 1]}
     *
     *  {"foo" :  ["bar", [1]]}
     *
     *  {"foo" :  {"bar" : [1]} }
     *
     *  {"foo" :  ["bar" : [1]] }
     *
     *  {"foo" : ["bar-1", "bar2-4", ...]}
     *
     *   map : {"foo" : ["bar", "bar2", ...]}
     *   time_array : [ 1,4,...]
     *
     *   {"foo" : [1,4,...]}
     *
     *
     *   input : "foo", "zee", 2
     *
     *
     *   {"foo" : {"bar":1, "bar2" : 4, ....}}
     *
     */
    // 5.57 PM - 6.20 pm

    class TimeMap {

        // attr
        Map<String, List<String>> keyValueMap;
        Map<String, List<Integer>> InsertTimeMap;

        public TimeMap() {
            this.keyValueMap = new HashMap<>();
            this.InsertTimeMap = new HashMap<>();
        }

        public void set(String key, String value, int timestamp) {

            // update keyValueMap
            List<String> values = this.keyValueMap.getOrDefault(key, new ArrayList<>());
            //values = this.keyValueMap.get(key);
            values.add(value);
            this.keyValueMap.put(key, values);

            // update InsertTimeMap
            List<Integer> times = this.InsertTimeMap.getOrDefault(key, new ArrayList<>());
            times.add(timestamp);
            this.InsertTimeMap.put(key, times);
        }

        public String get(String key, int timestamp) {
            if (!this.keyValueMap.containsKey(key)){
                return "";
            }
            // linear search
            List<Integer> times = this.InsertTimeMap.get(key);
//            while (timestamp >= 0){
//                if (times.contains(timestamp)){
//                    int idx = times.indexOf(timestamp);
//                    return this.keyValueMap.get(key).get(idx);
//                }
//                timestamp -= 1;
//            }
            int idx = this.binaryGetLatestTime(timestamp, times);
            System.out.println(">>> idx = " + idx);

            return idx >= 0 ? this.keyValueMap.get(key).get(idx) : "";
        }

        private int binaryGetLatestTime(int timestamp, List<Integer> times){
            int left = 0;
            int right = times.size() - 1;
            while (right >= left){
                int mid = (left + right) / 2;
                Integer val = times.get(mid);
                if (val.equals(timestamp)){
                    return mid;
                }
                if (val > timestamp){
                    right = mid - 1;
                }else{
                    left = mid + 1;
                }
            }

            //return right <= timestamp ? right : -1;
            return right >= 0 ? right : -1;
            //return -1;
        }
    }


//    class TimeMap {
//
//        // attr
////        private String key;
////        private List<String> value;
//        private Map<String, List<String>> map; // save key-value info
//        //private List<Integer> timeArray;
//        private Map<String, List<Integer>> timeArray; // save key-insert time history info
//
//        public TimeMap() {
//            this.map = new HashMap<>();
//            this.timeArray = new HashMap<>(); //new ArrayList<>();
//        }
//
//        public void set(String key, String value, int timestamp) {
//            //String _key = key + "-" + timestamp;
//            List<String> values; // ???
//            if(!this.map.containsKey(key)){
//                values = new ArrayList<>();
//            }else{
//                //this.map.put()
//                values = this.map.get(key);
//            }
//            values.add(value);
//            this.map.put(key, values);
//            List<Integer> timeArray =  this.timeArray.getOrDefault(key, new ArrayList<>());
//            timeArray.add(timestamp);
//            this.timeArray.put(key, timeArray);
//            //this.timeArray.add(timestamp); // insert with idx ???
//        }
//
//        public String get(String key, int timestamp) {
//            String res = "";
//            if (!this.map.containsKey(key)){
//                return res;
//            }
//            // binary search
//            List<Integer> list = this.timeArray.get(key);
//            int idx  = getRecentAddedValue(list, timestamp);
//            res = this.map.get(key).get(idx);
//            return res;
//        }
//        private Integer getRecentAddedValue(List<Integer> input, int x){
//            int left = 0;
//            int right = input.size();
//            while (right > left){
//                int mid = (left + right) / 2;
//                int val = input.get(mid);
//                if (val >= x){
//                    //left = mid+1;
//                    right = mid;
//                }else{
//                    left = mid+1;
//                }
//            }
//            return left; // ?
//        }
//    }

//    class TimeMap {
//
//        // attr
//        // HashMap<String, HashMap<Integer, String>> keyTimeMap;
//        Map<String, Map<Integer, String>> keyTimeMap;
//
//        public TimeMap() {
//            this.keyTimeMap = new HashMap<>();
//        }
//
//        public void set(String key, String value, int timestamp) {
//            Map<Integer, String> valueMap = null;
//            if (!this.keyTimeMap.containsKey(key)){
//                valueMap = new HashMap<>();
//            }else{
//                valueMap = this.keyTimeMap.get(key);
//            }
//            valueMap.put(timestamp, value);
//            this.keyTimeMap.put(key, valueMap);
//        }
//
//        public String get(String key, int timestamp) {
//            // linear search ???
//            if (!this.keyTimeMap.containsKey(key)){
//                return "";
//            }
//            // binary search
//            for (int x = timestamp; x >= 1; x-=1){
//                if (this.keyTimeMap.get(key).containsKey(x)){
//                    return this.keyTimeMap.get(key).get(x);
//                    //return x;
//                }
//            }
//            return "";
//        }
//    }

    // LC 837
    // https://leetcode.com/problems/new-21-game/description/
    // 5.26 pm - 5.40
    /**
     *  n : find prob that point < n
     *  k : will stop when point  >= k
     * [1, maxPts] : get point randomly from [1, maxPts] range
     *
     *  -> find the prob that point < n
     */
//    public double new21Game(int n, int k, int maxPts) {
//        return 0.0;
//    }

    // LC 743
    // https://leetcode.com/problems/network-delay-time/description/
    // 2.01 pm - 2.15 pm
    /**
     *  n : we have nodes,
     *      from 1 to n
     *
     *  times[i] = (ui, vi, wi):
     *      ui is the source node,
     *      vi is the target node,
     *      wi is the time it takes for a signal to travel from source to target.
     *
     *  -> k: We will send a signal from a given node k.
     *     Return the minimum time it takes for "all the n nodes" to receive the signal.
     *     If it is impossible for all the n nodes to receive the signal, return -1.
     */
    /**
     *  idea :
     *
     *
     */
    public int networkDelayTime(int[][] times, int n, int k) {
        Set<Integer> nodes = new HashSet<>();
        for (int[] x :times){
            nodes.add(x[0]);
        }
        if(!nodes.contains(k)){
            return -1; // not possible to visit "k" node
        }
        int time = 0;

        // build graph
        Map<Integer, Set<Integer>> map = new HashMap<>();

        for (int[] x :times){
            Set<Integer> neighbors = map.getOrDefault(x[0], new HashSet<>());
            neighbors.add(x[1]);
            map.put(x[0], neighbors);
        }

        // bfs (since need to find min time)
        Set<Integer> visited = new HashSet<>();
        Queue<Integer> queue = new LinkedList<>();
        queue.add(k);
        while(!queue.isEmpty()){
            int cur = queue.poll();
            if (map.containsKey(cur) && !visited.contains(cur)){
                visited.add(cur);
                Set<Integer> neighbors = map.get(cur);
                //time += map.g
                for (Integer node : neighbors){
                    if(!visited.contains(cur)){ // check ?
                        queue.add(node);
                    }
                }
            }
        }

        if (visited.size() < k){
            return -1;
        }
        return time;
    }

    // LC 889
    // https://leetcode.com/problems/construct-binary-tree-from-preorder-and-postorder-traversal/
    // 4.19 - 4.39 pm

    /**
     * preorder  :  root -> left -> right
     * postorder : left -> right -> root
     *
     * idea 1:
     *   1. preorder : get root
     *   2. postorder : split left, right sub tree and root
     *   3. preorder : get next root
     *
     *   4. 1st element in left sub tree in pre-order
     *      is the LAST element of  left sub tree in post-order (???)
     *
     *
     *  idea 2 :
     *
     *
     *  1. 2nd element (idx=1) in pre-order is
     *     lest element of sub left tree (e.g. sub left tree root) in post-order
     *
     *  2. we then can know "tree width" via above
     *     -> distance between left and root
     *
     */
    public TreeNode constructFromPrePost(int[] preorder, int[] postorder) {

        // edge case
        if(preorder == null || postorder == null || preorder.length == 0 || postorder.length == 0){
            return null;
        }

        if (preorder.length == 1){
            return new TreeNode(postorder[0]);
        }

        TreeNode root = new TreeNode(preorder[0]);
        int mid = 0;

        // find mid
        for (int i = 0; i < postorder.length; i++){
            if (postorder[i] == preorder[0]){
                mid = i;
                break;
            }
        }

        // recursive build tree

        root.left = this.constructFromPrePost(Arrays.copyOfRange(preorder, 1, mid-1), Arrays.copyOfRange(postorder, 1, mid-1));
        root.right = this.constructFromPrePost(preorder, postorder);

        return root;
    }

    // LC 524
    // https://leetcode.com/problems/longest-word-in-dictionary-through-deleting/
    // 1.45 pm - 1.55 pm

    /**
     * NOTE !!!
     *
     *  return the longest string in the dictionary
     *  that can be formed by deleting some of the
     *  given string characters.
     *
     *  If there is more than one possible result,
     *  -> return the 1) longest word with 2) the smallest lexicographical order.
     *
     *  If there is no possible result, return the empty string.
     *
     *
     *  lexicographical : 字母序 (?)
     *
     *
     *  idea 1)
     *
     *   1. loop over dict
     *      maintain a res string
     *   2. check if can "delete some element" in s,
     *      -> check via compare dict element
     *      so can be same as dict
     *      if ok, save value
     *
     *      e.g. :
     *         s = {a:2, b:1, p:2, c:1, l:1, e:1}
     *         so, for ale, it is {a:1, l:1, e:1} OK, ale
     *             for apple, it is {a:1, p:2, l:1, e: 1}, OK, apple
     *         ...
     *         collected = [ale, apple, plea],
     *         apple is apple, so return apple as ans
     *
     *
     *
     *      e.g:
     *        s = {a:2, b:1, p:2, c:1, l:e, e:1}
     *        ...
     *        collected = [a,b,c]
     *        return a as ans
     *
     *
     *   3. check longest and return ans
     *
     */
    public String findLongestWord(String s, List<String> dictionary) {
        if (dictionary.size() == 0 && s != null){
            return "";
        }

        Map<String, Integer> sMap = this.getElementCount(s);
        System.out.println(">>> sMap = " + sMap);

        List<String> collected = new ArrayList<>();
        // check
        for (String item : dictionary){
            //Map<String, Integer> curMap = this.getElementCount(item);
            if (canForm(item, s)){
                collected.add(item);
            }
        }

        System.out.println(">>> collected = " + collected);

        if (collected.size()==0){
            return "";
        }

        Collections.sort(collected, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                // First compare by length
                int lengthComparison = Integer.compare(o2.length(), o1.length());
                // If lengths are equal, compare lexicographically
                if (lengthComparison == 0) {
                    return o1.compareTo(o2); // lexicographical order
                }
                return lengthComparison; // sort by length
            }
        });

        System.out.println(">>> (sorted) collected = " + collected);

        return collected.get(0);
    }

    private Map<String, Integer> getElementCount(String s){
        Map<String, Integer> map = new HashMap<>();
        for(String x : s.split("")){
            int cnt = map.getOrDefault(x, 0);
            map.put(x, cnt+1);
        }
        return map;
    }

    // isSubsequence_3(str, s)
    private boolean canForm(String x, String y){
//        Map<String, Integer> sMap = this.getElementCount(s);
//        Map<String, Integer> curMap = this.getElementCount(item);
//        for (String k: curMap.keySet()){
//            if(!sMap.keySet().contains(k)){
//                return false;
//            }
//        }
//        int idx_s = 0;
//        int idx_i = 0;
//        while(idx_s < s.length() && idx_i < item.length()){
//
//            if (idx_s >= s.length() || idx_i >= item.length()){
//                return false;
//            }
//
//            if(s.charAt(idx_s) != item.charAt(idx_i)){
//                return false;
//            }
//            while(s.charAt(idx_s) == item.charAt(idx_i)){
//                idx_i += 1;
//            }
//            idx_i += 1;
//            idx_s += 1;
//        }
//        return true;

        int j = 0;
        for (int i = 0; i < y.length() && j < x.length(); i++)
            if (x.charAt(j) == y.charAt(i))
                j++;
        return j == x.length();
    }

    // LC 221
    // https://leetcode.com/problems/maximal-square/
    // 3.38 pm - 3.48

    /**
     *  find the largest square containing only 1's and return its area.
     *
     *
     *  idea 1)
     *
     *  1
     *
     *  11
     *  1?
     *
     *  111
     *  111
     *  11?
     *
     *
     *  dp[0][0] = 1 or 0
     *
     *  dp[i][j] = if (dp[i-1]dp[j]==1 && dp[i]dp[j-1]==1), then 1
     *             else 0
     *
     *  ...
     *
     */
    public int maximalSquare(char[][] matrix) {
        if (matrix.length == 1 && matrix[0].length == 1){
            return matrix[0][0];
        }

        int res = 0;
        return 0;
    }

    // LC 1145
    // https://leetcode.com/problems/binary-tree-coloring-game/
    // 8.10 pm - 8.30 pm
//    int player2Cnt = 0;
//    int nodeCnt = 0;
    /**
     *  idea 1
     *
     *   1. if player1 color at root
     *       - player 2 can color at one of left sub tree
     *       - player 2 can color at one of right sub tree
     *
     *   2. if player1 color at one of left sub tree
     *      - player 2 can color at root
     *      - player 2 can color at one of left sub tree
     *      - player 2 can color at one of right sub tree
     *
     *   3. if player1 color at one of right sub tree
     *      - player 2 can color at root
     *      - player 2 can color at one of left sub tree
     *      - player 2 can color at one of right sub tree
     *
     *
     *  -> finally, compare player1 color VS total color
     *  e.g.  (n - player1_color) > 2 / n ?
     *
     */
    public boolean btreeGameWinningMove(TreeNode root, int n, int x) {

        if (root.left==null && root.right==null){
            return false;
        }

        // case 1: player color "root"
        if (x == root.val){
            return btreeGameWinningMove(root.left, n, x)
                    || btreeGameWinningMove(root.right, n, x);
        }

        // case 2: player color "sub left tree"
        if (root.left != null && x == root.left.val){
            return btreeGameWinningMove(root, n, x)
                    || btreeGameWinningMove(root.left, n, x)
                    || btreeGameWinningMove(root.right, n, x);
        }

        // case 3: player color "sub right tree"
        if (root.right != null && x == root.right.val){
            return btreeGameWinningMove(root, n, x)
                    || btreeGameWinningMove(root.left, n, x)
                    || btreeGameWinningMove(root.right, n, x);
        }

        return false;
    }

    private TreeNode findNode(TreeNode root, int x){
        // ??
        if (root == null){
            return null;
        }
        if (root.val == x){
            return root;
        }
        root.left = findNode(root.left, x);
        root.right = findNode(root.right, x);

        return root;
    }

    private int getColorCountAtX(TreeNode root, int x){
        // ???
        if (root == null){
            return 0;
        }
        TreeNode node = this.findNode(root, x);
        return 1 +
                this.getColorCountAtX(node.left, x)
                + this.getColorCountAtX(node.right, x);
    }

    public boolean btreeGameWinningMove_1(TreeNode root, int n, int x) {

        if (root.left==null && root.right==null){
            return false;
        }

        if (root.left==null || root.right==null){
            return false;
        }

        // get total count
        //int player2Cnt = this.getAllNodeCount(root, 0);
        int player1Cnt = this.getSubNodeCountWithStartPoint(root, 0, x, false);

        // get max possible count of "player2" color
//        int nodeCnt = Math.max(
//                this.getAllNodeCount(root.left, 0),
//                this.getAllNodeCount(root.right, 0)
//        );

        // check whether player2 (us) can color > node / 2
        // then we're sure that player 2 can win the game
        //return player2Cnt > n / 2;

        // if player1Cnt < n / 2, then player2 can win for sure
        return player1Cnt < n / 2;
    }

    private int getSubNodeCountWithStartPoint(TreeNode root, int cnt, int x, boolean flag){

        if (root == null){
            return cnt;
        }
        // if "found" the start point
        if (root.val == x){
            flag = true;
        }
        // once start point is found, all the following nodes need to be count
        if (flag){
            cnt += 1;
        }
        return this.getSubNodeCountWithStartPoint(root.left, cnt, x, flag) +
                this.getSubNodeCountWithStartPoint(root.right, cnt, x, flag);
    }

    private int getAllNodeCount(TreeNode root, int cnt){
        if (root == null){
            return cnt;
        }
        return 1 + this.getAllNodeCount(root.left, cnt) +
                this.getAllNodeCount(root.right, cnt);
    }

    // LC 1219
    // https://leetcode.com/problems/path-with-maximum-gold/
    // 4.06 pm - 4.26pm
    int maxGold = 0;

    public int getMaximumGold(int[][] grid) {

        // dfs
        // edge case
        if (grid.length == 1 && grid[0].length == 1){
            return grid[0][0];
        }

        int l = grid.length;
        int w = grid[0].length;

        // collect "start point" candidate
        List<List<Integer>> startCandidates = new ArrayList<>();
        for (int i = 0; i < l; i++){
            for (int j = 0; j < w; j++){
                if (grid[i][j] > 0){
                    //int[][] cur = new int[][]{{i,j}};
                    List<Integer> cur = new ArrayList<>();
                    cur.add(i);
                    cur.add(j);
                    startCandidates.add(cur);
                }
            }
        }

        // dfs + backtrack
        for (int i = 0; i < l; i++){
            for (int j = 0; j < w; j++){
                if (grid[i][j] > 0){
                    Boolean[][] visited = new Boolean[l][w];
                    int val = 0;
                    int goldValue = this.getGold(j, i, val, grid, visited);
                    maxGold = Math.max(maxGold, goldValue);
                }
            }
        }

//        System.out.println("startCandidates = ");
//        for (List<Integer> item : startCandidates){
//            System.out.println("x = " + item.get(0) + ", y = " + item.get(1));
//        }

        return maxGold;
    }

    private int getGold(int x, int y, int val, int[][] grid, Boolean[][] visited){

        int[][] moves = new int[][]{{0,1}, {0,-1}, {1,0}, {-1,0}};

        int l = grid.length;
        int w = grid[0].length;

//        if (visited[y][x]){
//            return 0;
//        }
        val += grid[y][x];
        for (int[] move : moves){
            int x_ = x + move[0];
            int y_ = y + move[1];
            if (x_ >= 0 && x_ < w && y_ >= 0 && y_ < l && !visited[y_][x_]){
                visited[y_][x_] = true;
                val += grid[y_][x_];
                this.getGold(x_, y_, val, grid, visited);
                // undo
                visited[x_][y_] = false;
                val -= grid[y_][x_];
            }
        }

        return val;
    }

    // LC 528
    // https://leetcode.com/problems/random-pick-with-weight/
    //6.35 pm - 6.40 pm
    // idea : presum array + binary search (?
    /**
     *  ep 1:
     *
     *   w = [1, 3]
     *   -> preSum = [0,1,4]
     *   -> so
     *       sum(0) = preSum(1) - preSum(0) = 1
     *       sum(1) = preSum(2) - preSum(0) = 4
     *
     *
     *
     */
    class Solution {

        int[] w;
        int[] preSumArray;

        public Solution(int[] w) {

            this.w = w;
            this.preSumArray = new int[w.length+1];
            preSumArray[0] = 0;

            // build preSumArray
            int preSum = 0;
            for (int i = 0; i < w.length; i++){
                int cur = w[i];
                preSum += cur;
                preSumArray[i+1] = preSum;
            }
        }

        public int pickIndex() {
            if (this.w.length==1){
                return 0;
            }

            // binary search ???
            Random random = new Random();
            int x = random.nextInt();
            int l = 0;
            int r = preSumArray.length;
            while (r >= l){
                int mid = (l+r) / 2;
                // ???? TODO : fix
                if (mid == x){
                    int curSum = preSumArray[mid+1] - preSumArray[0];
                    int total = preSumArray[preSumArray.length-1];
                    return curSum / total;
                }if (mid > x){
                    r = mid-1;
                }else{
                    l = mid + 1;
                }
            }
            return 0;
        }
    }

//    class Solution {
//
//        int[] w;
//
//        public Solution(int[] w) {
//            this.w = w;
//        }
//
//        public int pickIndex() {
//            if (this.w.length == 1){
//                return 0;
//            }
//            Random r = new Random();
//            int low = 0;
//            int high = w.length-1;
//            return r.nextInt(high-low) + low;
//        }
//    }


    // LC 334
    // https://leetcode.com/problems/increasing-triplet-subsequence/
    // 0720 - 0730

    /**
     *
     * Given an integer array nums,
     * return true if there exists a triple of indices (i, j, k)
     * such that i < j < k and nums[i] < nums[j] < nums[k].
     * If no such indices exists, return false.
     *
     */
    public boolean increasingTriplet(int[] nums) {

        Integer biggest_1 = Integer.MAX_VALUE;
        Integer biggest_2 = Integer.MAX_VALUE;
        //Integer biggest_3 = Integer.MAX_VALUE;

        for (int x : nums){
            if (biggest_1 >= x){
                biggest_1 = x;
            }
            // else if (biggest_2 >= x){
            else if (x > biggest_1){
                biggest_2 = x;
            }else{
                //biggest_3 = x;
                return true;
            }
        }

        return false;
    }

//    public boolean increasingTriplet(int[] nums) {
//
//        if (nums.length < 3){
//            return false;
//        }
//
//        // 2 pointers
//        for (int i = 0; i < nums.length-2; i++){
//            int cnt = 1;
//            int prev = nums[i];
//            for (int j = i+1; j < nums.length; j++){
//                if (cnt >= 3){
//                    return true;
//                }
//                if (nums[j] > prev){
//                    cnt += 1;
//                    prev = nums[j];
//                }
//                j += 1;
//            }
//        }
//
//        return false;
//    }

    // LC 353
    // https://leetcode.ca/2016-11-17-353-Design-Snake-Game/
    // 6.24 pm - 6.40 pm
    class SnakeGame {
        private int m;
        private int n;
        private int[][] food;
        private int score;
        private int idx;
        private Deque<Integer> q = new ArrayDeque<>();
        private Set<Integer> vis = new HashSet<>();

        public SnakeGame(int width, int height, int[][] food) {
            this.m = width;
            this.n = height;
            this.food = food;
        }

        public int move(String direction) {

            if (this.m == 0 && this.n == 0){
                return -1;
            }

            int[][] move = getMove(direction);
            //int[][] current = this.q.getFirst();


            // move and update score

            return this.score;
        }

        private int[][] getMove(String direction){
            if (direction == null){
                //return new int[][]{{-1,-1}};
                throw new RuntimeException("invalid direction");
            }
            switch (direction){
                case "R":
                    return new int[][]{{0,1}};
                case "L":
                    return new int[][]{{0,-1}};
                case "D":
                    return new int[][]{{1,0}};
                case "U":
                    return new int[][]{{-1,0}};
                default:
                    return new int[][]{{-1,-1}};
            }
        }

    }

    // LC 686
    // https://leetcode.com/problems/repeated-string-match/
    // 5.13 pm - 5.30 pm

    /**
     * Given two strings a and b,
     * return the minimum number of times you should repeat
     * string a so that string b is a substring of it.
     *
     * Notice: string "abc" repeated 0 times is "",
     * repeated 1 time is "abc"
     * and repeated 2 times is "abcabc".
     *
     *  exp 1:
     *
     *   Input: a = "abcd", b = "cdabcdab"
     *
     *   -> return 3
     *
     *   abcdabcdabcd
     *     x      x
     *
     *
     *  exp 2:
     *
     *  Input: a = "a", b = "aa"
     *  -> Output: 2
     *
     *
     *  idea 1:
     *
     */
    public int repeatedStringMatch(String a, String b) {

        // edge case
//        if (a.length() == b.length()){
//            if(a.equals(b)){
//                return 1;
//            }
//            return -1;
//        }

        if (a.contains(b)){
            return 1;
        }

        int cnt = 1;
        StringBuilder sb = new StringBuilder();
        sb.append(a);
        // TODO : check ??
        // 1 <= a.length, b.length <= 10^4
        while (sb.toString().length() <= 10000){
            if (sb.toString().contains(b)) {
                return cnt;
            }
            sb.append(a);
            cnt += 1;
        }

        return -1;
    }


    // LC 734
    // https://leetcode.ca/all/734.html
    // 5.59 pm - 6.10 pm
    /**
     *  1) NOT transitive:
     *
     *  great, fine -> s
     *  fine, good -> s
     *
     *  but great, good -> NOT necessary s
     *
     *  2) Symmetric:
     *
     *  great, fine -> s
     *
     *  -> fine, great -> s
     *
     *  3) Also, a word is always similar with itself.:
     *
     *   e.g.
     *  -> great, great -> s
     *
     *
     *  4)  sentences can only be similar if they have the same number of words.
     *
     *  ->  words1 = ["great"] can never be similar to words2 = ["doubleplus","good"].
     *  (different # of word)
     *
     *
     *  Given two sentences words1, words2 (each represented
     *  as an array of strings),
     *  and a list of similar word pairs pairs,
     *  determine if two sentences are similar.
     *
     *  exp 2:
     *
     * Input: sentence1 = ["great","acting","skills"]
     * sentence2 = ["fine","drama","talent"]
     *
     * similarPairs = [["great","fine"],["drama","acting"],["skills","talent"]]
     * Output: true
     *
     * Explanation: The two sentences have the same length and each word i of sentence1 is also similar to the corresponding word in sentence2.
     *
     *
     *  -> idea 1
     *   a hashmap record "similar" relation"
     *   {great: fine,
     *    fine: great,
     *   }
     *
     *
     *
     */
    public boolean areSentencesSimilar(
            String[] sentence1, String[] sentence2, List<List<String>> similarPairs) {
        if (sentence1.length != sentence2.length){
            return false;
        }
        Map<String, String> map = new HashMap<>();
        for(int i = 0; i < similarPairs.size(); i++){
            List<String> cur = similarPairs.get(i);
            map.putIfAbsent(cur.get(0), cur.get(1));
            //map.putIfAbsent(cur.get(1), cur.get(0));
        }

        for (int j = 0; j < sentence1.length; j++){
            String s_1 = sentence1[j];
            String s_2 = sentence2[j];
            if (!map.containsKey(s_1) && !map.containsKey(s_2)){
                return false;
            }
            if (!map.get(s_1).equals(s_2) && !map.get(s_2).equals(s_1)){
                return false;
            }
        }

        return true;
    }

    // LC 737
    // https://leetcode.ca/all/737.html
    public boolean areSentencesSimilarTwo(){
        return false;
    }

    // LC 1146
    // https://leetcode.com/problems/snapshot-array/
    // 4.29 pm - 4.50 pm

    /**
     * idea 1:
     * {snap_id: cnt}
     * <p>
     * idea 2:
     * <p>
     * use TreeMap to record "UPDATED records with its index"
     * {snap_id : [[new_val_1, idx_1], [new_val_2, idx_2] ....]}
     * <p>
     * -> whenever get() is called
     * -> read from TreeMap (with snapshot_id) and update elements
     * -> then return res
     */
    class SnapshotArray {

        //TreeMap<Integer, List<List<Integer>>> map;
        Map<Integer, List<List<Integer>>> map;
        Integer[] values;

        int snapshot_id;

        List<List<Integer>> updatedValues;

        public SnapshotArray(int length) {
            this.map = new HashMap<>();
            this.values = new Integer[length];
            this.snapshot_id = 0;
            this.updatedValues = new ArrayList<>();
        }

        public void set(int index, int val) {
            List<Integer> newValue = new ArrayList<>();
            newValue.add(val);
            newValue.add(index);
            this.updatedValues.add(newValue);
        }

        public int snap() {
            map.putIfAbsent(snapshot_id, this.updatedValues);
            // init again
            this.updatedValues = new ArrayList<>();
            this.snapshot_id += 1;
            return this.snapshot_id - 1;
        }

        public int get(int index, int snap_id) {
            if (this.map.containsKey(snap_id)){
                //int key = this.map.floorKey(index);
                //List<List<Integer>> updatedValues = this.map.get(key);
                for (List<Integer> value : updatedValues) {
                    int idx = value.get(1);
                    if (idx == index) {
                        return value.get(0);
                    }
                }
            }
            // if val not updated (not in updatedValues)
            // means val is unchanged, then return 0, as default
            return 0;
        }
    }


//    class SnapshotArray {
//
//        Integer[] elements;
//        Map<Integer, Integer[]> snapshotMap;
//        Integer snapshotCount;
//
//        public SnapshotArray(int length) {
//            this.elements = new Integer[length];
//            this.snapshotMap = new HashMap<>();
//            this.snapshotCount = 0;
//            // Store the initial snapshot (snapshot 0)
//            this.snapshotMap.put(this.snapshotCount, this.elements.clone());
//        }
//
//        public void set(int index, int val) {
//            // Set value in the current snapshot (current version of elements)
//            this.elements[index] = val;
//        }
//
//        public int snap() {
//            // Take a snapshot of the current elements array by creating a new copy
//            snapshotMap.put(snapshotCount, elements.clone());
//            // Increment snapshotCount to prepare for the next snapshot
//            return snapshotCount++;
//        }
//
//        public int get(int index, int snap_id) {
//            // Retrieve the value from the snapshot with the given snap_id
//            return snapshotMap.get(snap_id)[index];
//        }
//    }
//    class SnapshotArray {
//
//        Integer[] elements;
//        Map<Integer, Integer[]> snapshotMap;
//
//        Integer snapshotCount;
//
//        public SnapshotArray(int length) {
//            this.elements = new Integer[length];
//            this.snapshotMap = new HashMap<>();
//            this.snapshotCount = 0;
//            // this.elements.clone() ???
//            this.snapshotMap.put(this.snapshotCount, this.elements.clone());
//            System.out.println(">>> this.snapshotMap = " + this.snapshotMap);
//        }
//
//        public void set(int index, int val) {
////            Integer[] curElements = this.snapshotMap.get(this.snapshotCount);
////            System.out.println(">>> curElements = " + curElements);
////            curElements[index] = val;
////            //this.elements[index] = val;
////            this.snapshotMap.put(this.snapshotCount, curElements);
//            this.elements[index] = val;
//        }
//
//        public int snap() {
////            int curSnapShotCnt = this.snapshotCount;
////            snapshotMap.put(curSnapShotCnt+1, snapshotMap.get(snapshotMap));
////            this.snapshotCount += 1; // ?
////            return this.snapshotCount -1;
//            this.snapshotMap.put(this.snapshotCount, this.elements.clone());
//            this.snapshotCount += 1;
//            return this.snapshotCount;
//        }
//
//        public int get(int index, int snap_id) {
//            return this.snapshotMap.get(snap_id)[index];
//        }
//    }

    // LC 298
    /// https://leetcode.ca/all/298.html
    // 2.33 pm - 3.00 pm
    /**
     * A consecutive sequence path is a
     * path where the values increase by one along the path.
     *
     * -> start from each nodes, get longest path
     * -> compare with current longest path
     * -> return final ans
     *
     */
    int longestCnt = 0;
    public int longestConsecutive_1_1(TreeNode root) {
        if (root.left == null && root.right == null){
            return 0;
        }

        // bfs
        Queue<TreeNode> q = new LinkedList<>();
        q.add(root);
        while (!q.isEmpty()){
            TreeNode node = q.poll();
            // dfs
            int length = dfsCount(node);
            longestCnt = Math.max(longestCnt, length);
            if (node.left != null){
                q.add(node.left);
            }
            if (node.right != null){
                q.add(node.right);
            }
        }

        return longestCnt;
    }

    private int dfsCount(TreeNode root){

        if (root == null){
            return 0;
        }

        if (root.left != null && root.val + 1 == root.left.val){
            return this.dfsCount(root.left) + 1;
        }

        if (root.right != null && root.val + 1 == root.right.val){
            return this.dfsCount(root.right) + 1;
        }

        // TODO : double check ???
        return Math.max(
                Math.max(this.dfsCount(root.left), this.dfsCount(root.right)),
                this.dfsCount(root)
        );

    }

    // LC 729
    // https://leetcode.com/problems/my-calendar-i/
    // 3.11 pm - 3.30 pm
    /**
     *
     * -> We can add a new event if adding the event
     * will not cause a double booking.
     *
     *
     * -> A double booking happens when two events have
     * some non-empty intersection
     * (i.e., some moment is common to both events.).
     *
     *
     * -> boolean book(int start, int end)
     * Returns true if the event can be added to
     * the calendar successfully without
     * causing a double booking. Otherwise,
     * return false and do not add the event to the calendar.
     *
     *  exp 1
     *
     *  Input
     *   ["MyCalendar", "book", "book", "book"]
     *   [[], [10, 20], [15, 25], [20, 30]]
     *   Output
     *   [null, true, false, true]
     *
     *
     *   10   20           -> true
     *      15    25       -> false
     *        20     30    -> true
     *
     */
    // https://leetcode.com/problems/my-calendar-i/
    // 4.37 pm - 5.00 pm
    class MyCalendar {

        List<List<Integer>> bookings;

        public MyCalendar() {
            this.bookings = new ArrayList<>();
        }

        public boolean book(int start, int end) {
            if (this.bookings.size()==0){
                List<Integer> newBooking = new ArrayList<>();
                newBooking.add(start);
                newBooking.add(end);
                this.bookings.add(newBooking);
                return true;
            }
            /**
             *  overlap 3 cases
             *
             *  case 1)
             *
             *    |-----| old       new[1] > old[0] && new[0] < old[1]
             * |----|     new
             *
             *
             * case 2)
             *    |-----|      old     new[1] > old[0] && new[0] < old[1]
             *        |-----|  new
             *
             * case 3)
             *
             *    |------|       old    new[1] > old[0] && new[0] < old[1]
             *  |-------------|  new
             *
             */
            for (List<Integer> booking: bookings){
                if (booking.get(1) > start && booking.get(0) < end){
                    return false;
                }
            }
            List<Integer> newBooking = new ArrayList<>();
            newBooking.add(start);
            newBooking.add(end);
            this.bookings.add(newBooking);
            return true;
        }
    }

/**
 * Your MyCalendar object will be instantiated and called as such:
 * MyCalendar obj = new MyCalendar();
 * boolean param_1 = obj.book(start,end);
 */

//        class MyCalendar {
//            List<List<Integer>> dates;
//        public MyCalendar() {
//            this.dates = new ArrayList<>();
//        }
//
//        public boolean book(int start, int end) {
//            for (List<Integer> date : dates){
//                if (start < date.get(1) && end < date.get(0)){
//                    return false;
//                }
//            }
//            List<Integer> newBook = new ArrayList<>();
//            newBook.add(start);
//            newBook.add(end);
//            this.dates.add(newBook);
//            return true;
//        }
//    }


//    class MyCalendar {
//        List<List<Integer>> dates;
//
//        public MyCalendar() {
//            this.dates = new ArrayList<>();
//        }
//
//        public boolean book(int start, int end) {
//            if (this.dates.size() == 0){
//                List<Integer> newBook = new ArrayList<>();
//                newBook.add(start);
//                newBook.add(end);
//                this.dates.add(newBook);
//                return true;
//            }
//
//            // sort (from small to big)
//            // scanning line
//            //Arrays.sort(this.dates);
//            if(isOverlap(start, end, this.dates)){
//                return false;
//            }
//
//            List<Integer> newBook = new ArrayList<>();
//            newBook.add(start);
//            newBook.add(end);
//            this.dates.add(newBook);
//            return true;
//        }
//
//        /**
//         *
//         *    2     10
//         *      3  8
//         *  1           11
//         *
//         */
//        private Boolean isOverlap(int start, int end, List<List<Integer>> dates){
//            int overlap = 0;
//            for (List<Integer> date : dates){
//                Integer start_ = date.get(0);
//                Integer end_ = date.get(1);
//                if (end_ > end || start_ < start){
//                    return false;
//                }
//            }
//            return true;
//        }
//    }

    // LC 163
    // https://leetcode.ca/all/163.html
    // 3.51 pm - 4.20 pm
    /**
     *  Given a sorted integer array nums, where the range of
     *  elements are in the inclusive range [lower, upper],
     *  return its missing ranges.
     *
     *
     *  exp 1:
     *  Input: nums = [0, 1, 3, 50, 75], lower = 0 and upper = 99,
     *  Output: ["2", "4->49", "51->74", "76->99"]
     *
     *  -> idea 1
     *
     *  0,1, [2] 3, [50], [75]
     *
     *  -> "2",  "4,49",  "51,74", "76,99"
     *
     *
     *  -> loop over elements, compare prev, and current element
     *  -> if any missing, then collect, then add to result array
     *  -> then finally check upper bound and last element
     *  -> add to result accordingly if there is a missing element
     */
    public List<List<Integer>> findMissingRanges(int[] nums, int lower, int upper) {
        if (nums.length == 0){
            return null;
        }

        /**
         * Input: nums = [0, 1, 3, 50, 75], lower = 0 and upper = 99,
         * Output: ["2", "4->49", "51->74", "76->99"]
         *
         *  nums = [0, 1, 3, 50, 75]     res = []
         *          x
         *             x                 res = []
         *                x              res = [[2]]
         *                    x          res = [[2], [4,49]]
         *                        x      res = [[2], [4,49], [51,74]]
         *
         *                                res = [[2], [4,49], [51,74], [76,99]]
         */
        List<List<Integer>> res = new ArrayList<>();
        for (int i = 1; i < nums.length; i++){
            // case 1:  nums = [1, 3]
            if (nums[i] == nums[i-1]+2){
                List<Integer> missingPeriod = new ArrayList<>();
                missingPeriod.add(nums[i-1]+1);
                res.add(missingPeriod);
            }
            // case 2 : nums = [3, 50]
            else if (nums[i] != nums[i-1]){
                List<Integer> missingPeriod = new ArrayList<>();
                missingPeriod.add(nums[i-1]+1);
                missingPeriod.add(nums[i]-1);
                res.add(missingPeriod);
            }
        }

        // finally, check last element and upper bound
        if (upper != nums[nums.length-1]){
            List<Integer> missingPeriod = new ArrayList<>();
            missingPeriod.add(nums[nums.length-1] + 1);
            missingPeriod.add(upper);
            res.add(missingPeriod);
        }

        return res;
    }

    // LC 1048
    // 4.19 pm - 4.30 pm
    // https://leetcode.com/problems/longest-string-chain/
    public int longestStrChain(String[] words) {
        return 0;
    }

    // LC 951
    // https://leetcode.com/problems/flip-equivalent-binary-trees/
    // 7.23 pm - 7.40 pm
    public boolean flipEquiv(TreeNode root1, TreeNode root2) {

        // edge cases
        if (root1 == null || root2 == null) {
            return false;
        }

        if (root1.val != root2.val) {
            return false;
        }

        if (root1 == null && root2 == null) {
            return true;
        }


        return isSameTree(root1, root2);
    }

    private boolean isSameTree(TreeNode root1, TreeNode root2) {

        if (root1 == null || root2 == null) {
            return false;
        }

        if (root1.val != root2.val) {
            return false;
        }

        if (root1 == null && root2 == null) {
            return true;
        }

        return (isSameTree(root1.left, root2.right)
                && isSameTree(root1.right, root2.left))
                || (isSameTree(root1.right, root2.left)
                && isSameTree(root1.right, root2.left)) ;
                //&& isSameTree(root1, root2); // ?? needed ?
    }

}


