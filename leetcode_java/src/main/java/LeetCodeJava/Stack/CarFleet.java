package LeetCodeJava.Stack;

// https://leetcode.com/problems/car-fleet/

import java.util.*;

public class CarFleet {

    // V0
    // IDEA : STACK + HASH MAP
    public int carFleet(int target, int[] position, int[] speed) {

        int[] dist = new int[position.length];
        double[] arrived_time = new double[position.length];
        HashMap<Integer, Double> map = new HashMap<>();
        for (int i = 0; i < position.length; i++){
            int _position = position[i];
            int _speed = speed[i];
            dist[i] = target - _position;
            arrived_time[i] = (double) dist[i] / (double) speed[i];
            map.put(target - _position, (double) dist[i] / (double) speed[i]);
        }
        Arrays.sort(dist);
        List<Double> fleet = new ArrayList<Double>();
        for (int j = 0; j < dist.length; j++){
            int _size = fleet.size();
            double _time = map.get(dist[j]);
            if (_size == 0 || _time > fleet.get(_size-1)){
                fleet.add(_time);
            }
        }

        return fleet.size();
    }


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
