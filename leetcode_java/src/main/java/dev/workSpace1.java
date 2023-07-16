package dev;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Stack;

public class workSpace1 {

    public int carFleet(int target, int[] position, int[] speed) {

        if (position.length == 0 || position.equals(null)) {
            return 0;
        }

        int ans = 0;
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < position.length; i++) {
            int _position = position[i];
            int _speed = speed[i];
            map.put(_position, _speed);
        }

        Arrays.sort(map.keySet().toArray());

        //TreeMap<Integer, Integer> _map = new TreeMap<>(map);
        //int[] _positions = Arrays.asList(position).stream().sorted((x, y) -> Integer.compare(x, y)).collect(Collectors.toList());
        Arrays.sort(position);
        //int[] time = new int[position.length];
        Stack<Integer> times = new Stack<>();
        Stack<Integer> res = new Stack<>();
        for (int j = 0; j < position.length; j++) {
            int _arrived_time = (target - position[j]) / map.get(position[j]);
            times.push(_arrived_time);
        }

        while (!times.isEmpty()) {
            int cur = times.pop();
            if (cur > times.peek()) {
                res.push(cur);
            }
        }

        return res.size();
    }

    public static void main(String[] args) {
        int[] x = new int[5];
        x[0] = -1;
        x[1] = 5;
        x[2] = 3;

        Arrays.stream(x).forEach(System.out::println);

        System.out.println("-----------");

        Arrays.sort(x);

        Arrays.stream(x).forEach(System.out::println);

    }

    public boolean searchMatrix(int[][] matrix, int target) {

        int length = matrix[0].length;
        int width = matrix.length;

        // flatten matrix + binary search
        int l = 0;
        int r = length * width - 1;
        while (r >= l){
            int mid = (l + r) / 2;
            int cur = matrix[mid / length][mid % length];
            if (cur == target){
                return true;
            }else if (cur < target){
                l = mid + 1;
            }else{
                r = mid - 1;
            }
        }

        return false;
    }

}
