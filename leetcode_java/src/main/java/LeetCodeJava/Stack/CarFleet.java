package LeetCodeJava.Stack;

// https://leetcode.com/problems/car-fleet/

import java.util.*;

public class CarFleet {

    // V0
    // IDEA : STACK
//    public int carFleet(int target, int[] position, int[] speed) {
//
//        if (position.length == 1){
//            return 1;
//        }
//
//        if (target == 0){
//            return position.length;
//        }
//
//        int fleetCount = 0;
//        Stack<Integer> stack = new Stack<>();
//        HashMap<Integer, Integer> map = new HashMap<>();
//
//        for (int i = 0; i < position.length; i++){
//            int p = position[i];
//            int s = speed[i];
//            map.put(p, s);
//        }
//
//        // reorder position, speed with increasing ordering
//        // https://stackoverflow.com/questions/922528/how-can-i-sort-map-values-by-key-in-java
//        Map<Integer, Integer> tree_map = new TreeMap(map);
//        for (Integer k : tree_map.keySet()){
//            System.out.println("position = " + k + " speed = " + map.get(k));
//            // push into stack
//            stack.push(k);
//        }
//
//        // check fleet count
//        Stack<Integer> tmp = new Stack<>();
//        while (!stack.isEmpty()){
//            int cur = stack.pop();
//            // case 1 : tmp is empty, push into tmp stack directly
//            if (tmp.size() == 0){
//                tmp.add(cur);
//            // case 2 : tmp is NOT empty, and previous car speed < cur car speed
//            //          -> previous car CAN'T catchup current car
//            //          -> previous car as its own fleet
//            }else if (tmp.peek() < tree_map.get(cur)){
//                tmp.pop();
//                tmp.push(cur);
//                fleetCount += 1;
//            // case 3 : tmp is NOT empty, previous car speed > cur car speed
//            //         ->  previous car CAN catchup current car
//            //
//            }else{
//                int newSpeed = getHighestCommonFactor(tmp.peek(), tree_map.get(cur));
//
//            }
//        }
//
//        return fleetCount;
//    }
//
//    private int getHighestCommonFactor(int val1, int val2){
//
//        if (val1 == 0  && val2 == 0){
//            return 0;
//        }
//        if (val1 == 1 && val2 == 1){
//            return 1;
//        }
//        if (val1 == 1 || val2 == 1){
//            return 1;
//        }
//
//        int ans = 1;
//        int bigger = Math.max(val1, val2);
//        for (int i = 2; i < bigger; i ++){
//            if (val1 % i == 0 && val2 % i == 0){
//                ans = Math.max(ans, i);
//            }
//        }
//
//        return ans;
//    }

    // V0
    // IDEA : STACK
    // https://blog.csdn.net/fuxuemingzhu/article/details/81867361
//    public int carFleet(int target, int[] position, int[] speed) {
//
//        HashMap<Integer, Integer> map = new HashMap<>();
//        for (int i = 0; i < position.length; i++){
//            int p = -1 * position[i]; // for inverse sorting
//            int s = speed[i];
//            map.put(p, s);
//        }
//        // order by map key
//        Map<Integer, Integer> tree_map = new TreeMap(map);
//        List<Double> times = new ArrayList<>();
//        for (Integer k : tree_map.keySet()){
//            Double _time = Double.valueOf((target - (-1 * k)) / tree_map.get(k));
//            times.add(_time);
//        }
//
//
//        Stack<Double> stack = new Stack<>();
//
//        // loop over times, check fleet count
//        for (Double time: times){
//            // case 1 : if stacks is empty, push to stack directly
//            if (stack.isEmpty()){
//                stack.push(time);
//            }else{
//                // case 2: if previous car takes longer time to destination than next car
//                //         -> Not possible for them become a fleet, add to stack
//                if (time > stack.peek()){
//                    stack.push(time);
//                }
//            }
//        }
//
//        return stack.size();
//    }

    // V1
    // https://leetcode.com/problems/car-fleet/solutions/2013259/java-simple-solution-100-faster/
    public int carFleet_2(int target, int[] position, int[] speed) {

        int res = 0;
        double[] timeArr = new double[target];
        for (int i = 0; i < position.length; i++)
        {
            timeArr[position[i]]= (double)(target - position[i]) / speed[i];
        }
        double prev = 0.0;
        for (int i = target-1; i >=0 ; i--)
        {
            double cur = timeArr[i];
            if (cur > prev)
            {
                prev = cur;
                res++;
            }
        }
        return res;

    }

    // V2
    // https://leetcode.com/problems/car-fleet/solutions/411641/java-easy-understand-fast-solution/
    // IDEA :
    // calculate distance left for each car
    // calculate time needed for each car
    // if a car has greater distance left than another car & its time needed is smaller than another car, it wil chase up and become the same fleet
    public int carFleet_3(int target, int[] position, int[] speed) {
        int n = position.length;
        int[] dist = new int[n];
        double[] timeAr = new double[n];
        HashMap<Integer, Double> map = new HashMap<>();
        for (int i = 0; i < n; i++) {
            dist[i] = target - position[i]; //calculate distance left for each car
            timeAr[i] = (double) dist[i] / (double) speed[i]; //calculate original time needed for each car
            map.put(dist[i], timeAr[i]); //map distance left with original time needed
        }

        Arrays.sort(dist); //the cars with larger distance left & smaller time needed will chase up the car before
        List<Double> rTimeL = new ArrayList<Double>(); //store time used for each fleet in ascending order
        for (int i = 0; i < n; i++) {
            Double time = map.get(dist[i]);
            int m = rTimeL.size();
            if (m == 0 || time > rTimeL.get(m - 1)) {
                rTimeL.add(time); //new fleet is created
            }
        }
        return rTimeL.size();
    }

    // V3
    // https://leetcode.com/problems/car-fleet/solutions/3224677/fast-100-16ms-space-efficient-99-511-mb-and-compact/
    public int carFleet_4(int target, int[] position, int[] speed) {
        float arraytime[] = new float[target+1],max=0;
        for(int i=0;i<position.length;i++){
            arraytime[position[i]] = (float)(target-position[i])/speed[i];
        }
        int count=0;
        for(int i=target;i>=0;i--){
            if(arraytime[i]>max){
                count++;
                max=arraytime[i];
            }
        }
        return count;
    }

}
