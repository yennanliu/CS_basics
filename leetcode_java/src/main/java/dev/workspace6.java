package dev;

import java.util.*;

public class workspace6 {
    public static void main(String[] args) {

        // inverse order
        int[] array_1 = new int[3];
        array_1[0] = 1;
        array_1[1] = 2;
        array_1[2] = 3;

//        for (int i = array_1.length - 1; i >= 0; i--) {
//            System.out.println(array_1[i]);
//        }

        // s = "3[a]2[bc]", return "aaabcbc".
        /**
         *
         *      * s = "3[a]2[bc]", return "aaabcbc".
         *      * s = "3[a2[c]]", return "accaccacc".
         *      * s = "2[abc]3[cd]ef", return "abcabccdcdcdef".
         *
         */
        String x = "3[a]2[bc]"; //"3[a2[c]]"; //"2[abc]3[cd]ef"; //"3[a2[c]]"; //"3[a]2[bc]"; //"2[abc]3[cd]ef"; //"3[a2[c]]"; //"3[a]2[bc]";
        String y = decodeString(x);
       // System.out.println(">>> y = " + y);
    }

    public static String decodeString(String s) {
        if (s.length() == 0) {
            return null;
        }
        // init
        Stack<String> stack = new Stack<>(); // ??
        StringBuilder sb = new StringBuilder();
        String A_TO_Z = "abcdefghijklmnopqrstuvwxyz";
        for (String x : s.split("")) {
            System.out.println(">>> x = " + x);
            String tmp = "";
            StringBuilder tmpSb = new StringBuilder();
            if (!x.equals("]")) {
                if (!x.equals("[")) {
                    stack.add(x);
                }
            } else {
                // pop all elements from stack, multiply, and add to res
                //String tmp = "";
                while (!stack.isEmpty()) {
                    String cur = stack.pop(); // ??
                    if (A_TO_Z.contains(cur)) {
                        tmp = cur + tmp;
                        //tmpSb.append(tmpSb);
                    } else {
                        tmp = getMultiplyStr(tmp, Integer.parseInt(cur));
                    }
                }

            }

            sb.append(tmp);
        }

        //String tmpRes = sb.toString();

        StringBuilder tmpSb = new StringBuilder();

        // add remaining stack element to result
        while(!stack.isEmpty()){
            tmpSb.append(stack.pop());
            //tmpRes =  tmpRes + stack.pop();
        }

        sb.append(tmpSb.reverse());

        return sb.toString();
    }

    private static String getMultiplyStr(String cur, Integer multiply) {
        StringBuilder sb = new StringBuilder();
        for (int x = 0; x < multiply; x++) {
            sb.append(cur);
        }
        return sb.toString();
    }

    // LC 815
    // https://leetcode.com/problems/bus-routes/
    // 12.47 pm - 1.00 pm

    /**
     * For example, if routes[0] = [1, 5, 7],
     * this means that the 0th bus travels in the
     * sequence 1 -> 5 -> 7 -> 1 -> 5 -> 7 -> 1 -> ... forever.
     * <p>
     * -> Return the least number of buses you must take to
     * travel from source to target.
     * Return -1 if it is not possible.
     * <p>
     * <p>
     * <p>
     * IDEA : BFS
     * <p>
     * step 1) build graph
     * step 2) find min path via BFS
     * step 3) if can't find, return -1
     * <p>
     * <p>
     * exp 1)
     * <p>
     * routes = [[1,2,7],[3,6,7]], source = 1, target = 6
     * <p>
     * -> graph = {
     * 1: [2,7],
     * 2: [1,7],
     * 7: [1,2, 3,6],
     * 3: [6,7],
     * 6: [3,7],
     * // 7: [3,6]
     * }
     * <p>
     * -> so, we start src = 1,
     * then can visit 2, 7
     * - for 2, can visit 1,7 (already visited)
     * - for 7, can visit 1,2,3,6
     * and 6 is the target
     * -> so return 2 as result
     **/
    public int numBusesToDestination(int[][] routes, int source, int target) {

        // edge case
        if (routes.length == 1) {
            if (source == target) {
                return 0;
            }
            return 1;
        }
        // build graph
        Map<Integer, List<Integer>> graph = new HashMap<>();
        for (int[] x : routes) {
            int start = x[0];
            int end = x[1];
            List<Integer> cur = graph.getOrDefault(start, new ArrayList<>());
            cur.add(end);
            graph.put(start, cur); // double check ???
        }
        // bfs
        int cnt = 0;
        Queue<Integer> queue = new LinkedList<>();
        queue.add(source);
        Set<Integer> visited = new HashSet<>();
        while (!queue.isEmpty()) {
            Integer curr = queue.poll();
            // visit "neighbor"
            if (!graph.keySet().isEmpty()) {
                for (Integer x : graph.get(curr)) {
                    if (!visited.contains(x)) {
                        if (target == x) {
                            return cnt;
                        }
                        cnt += 1;
                        queue.add(x);
                        visited.add(x);
                    }
                }
            }
        }

        //return cnt > 0 ? cnt : -1;
        return -1;
    }

}
