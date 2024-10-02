package LeetCodeJava.String;

// https://leetcode.com/problems/minimum-area-rectangle/description/

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *  939. Minimum Area Rectangle
 *
 *  You are given an array of points in the X-Y plane points where points[i] = [xi, yi].
 *
 * Return the minimum area of a rectangle formed from these points, with sides parallel to the X and Y axes. If there is not any such rectangle, return 0.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: points = [[1,1],[1,3],[3,1],[3,3],[2,2]]
 * Output: 4
 * Example 2:
 *
 *
 * Input: points = [[1,1],[1,3],[3,1],[3,3],[4,1],[4,3]]
 * Output: 2
 *
 *
 * Constraints:
 *
 * 1 <= points.length <= 500
 * points[i].length == 2
 * 0 <= xi, yi <= 4 * 104
 * All the given points are unique.
 *
 *
 */
public class MinimumAreaRectangle {

    // V0
    // IDEA : HASHMAP + BRUTE FORCE + LOGIC (fixed by gpt)
    public int minAreaRect(int[][] points) {

        // Step 1: Build a map where key = x-coordinate and value = list of y-coordinates for that x
        Map<Integer, Set<Integer>> mapX = new HashMap<>();

        for (int[] point : points) {
            int x = point[0];
            int y = point[1];
            mapX.putIfAbsent(x, new HashSet<>());

            /** NOTE : below are equivalent
             */
            // V1
            //mapX.get(x).add(y);

            // V2
            Set<Integer> set = mapX.get(x);
            set.add(y);
            mapX.put(x, set);
        }

        // Step 2: Iterate through pairs of points to calculate the area of rectangles
        int res = Integer.MAX_VALUE;
        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                int x1 = points[i][0], y1 = points[i][1];
                int x2 = points[j][0], y2 = points[j][1];

                // Step 3: Check if we have a potential rectangle by ensuring x1 != x2 and y1 != y2
                if (x1 != x2 && y1 != y2) {
                    // Step 4: Check if the other two corners (x1, y2) and (x2, y1) exist
                    if (mapX.get(x1).contains(y2) && mapX.get(x2).contains(y1)) {
                        // Calculate the area of the rectangle
                        int area = Math.abs(x2 - x1) * Math.abs(y2 - y1);
                        res = Math.min(res, area);
                    }
                }
            }
        }

        // Return the result, if no rectangle was found return 0
        return res == Integer.MAX_VALUE ? 0 : res;
    }

    // V1

    // V2
    // IDEA : HASHMAP
    // https://leetcode.com/problems/minimum-area-rectangle/solutions/1265321/java-hashmap-set-solution-short-code-o-n-2/
    public int minAreaRect_2(int[][] points) {

        HashMap<Integer,HashSet<Integer>> map=new HashMap<>();

        /**
         *  NOTE !!!
         *
         *  use hashmap to record the points that with same x or y coordinator
         *
         *  e.g.  {1: [2,3,4]} means [1,2], [1,3], [1,4] existed
         *
         */
        for(int p[]:points){
            if(!map.containsKey(p[0])){
                map.put(p[0],new HashSet<Integer>());
            }
            map.get(p[0]).add(p[1]);
        }

        int area=Integer.MAX_VALUE;

        for(int i=0;i<points.length;i++){
            for(int j=i+1;j<points.length;j++){

                int x1=points[i][0];
                int y1=points[i][1];
                int x2=points[j][0];
                int y2=points[j][1];

                /**
                 *  NOTE !!!
                 *
                 *  The condition if(x1!=x2 && y1!=y2) ensures that
                 *  the two points are diagonally opposite
                 *  (i.e., their x and y coordinates must differ).
                 *
                 *  -> x1, x2 must be different, and y1, y2 must be different as well
                 *  -> then there is a way to form a rectangle
                 */
                if(x1!=x2 && y1!=y2){

                    /**
                     *  NOTE !!!
                     *
                     *
                     *  If the selected two points have different x and y coordinates, the code checks if the remaining two corners of the potential rectangle exist using the map. Specifically, the code checks whether:
                     * 	•	Point (x1, y2) exists (using map.get(x1).contains(y2)).
                     * 	•	Point (x2, y1) exists (using map.get(x2).contains(y1)).
                     *
                     *
                     *  if map.get(x1).contains(y2)
                     *      -> means there is the point exists : (x1, y2)
                     *      -> so (x1, y1), and (x1, y2) both exist
                     *
                     *  if map.get(x2).contains(y1)
                     *      -> means there is the point exists : (x2, y1)
                     *      -> so (x2, y2), and (x2, y1)  both exist
                     *
                     *
                     *  -> so (x1, y1), (x1, y2), (x2, y2), and (x2, y1)
                     *     are all existed,
                     *     -> it can form a rectangle !!!
                     *
                     */
                    if(map.get(x1).contains(y2) && map.get(x2).contains(y1)){
                        // get min area
                        area=Math.min(area, Math.abs(x1-x2) * Math.abs(y1-y2));
                    }
                }
            }
        }
        return area==Integer.MAX_VALUE ? 0 : area;
    }

    // V3
    // IDEA : HASHMAP
    // https://leetcode.com/problems/minimum-area-rectangle/
    public int minAreaRect_3(int[][] points) {
        HashMap<Integer, Set<Integer>> hashmap = new HashMap<>();  // Key => Integer | Value => Set

        for(int single_point[] : points) // Iterate through every single point & store the coordinates in map
        {
            if(!hashmap.containsKey(single_point[0])){ // Check if x coordinate of the single point already exisits as a Key in hasmap.
                hashmap.put(single_point[0], new HashSet<Integer>()); // Insert a new Key (equal to x coordinate) in the hashmap with corresponding Value (equal to newly created empty HashSet).
            }
            hashmap.get(single_point[0]).add(single_point[1]); // Insert y coordinate into Value (HashSet) corresponding to Key (equal to x coordinate)
            // Explanation :
            //		single_point => refering to a single point [x,y]
            //		x-coordinate of a single point => single_point[0]
            //		y-coordinate of a single point => single_point[1]

            //		Key => x coordinate of a point
            //			=> single_point[0]

            //		Value => HashSet of y coordinates
            //			=> hashmap.get(Key)
            //			=> hashmap.get(x-coordinate of a single point)
            //			=> hashmap.get(single_point[0])

            //		Insert y coordinate into corresponding Value (HashSet)
            //			=> Value.add(y-coordinate of a single point)
            //			=> hashmap.get(Key).add(y-coordinate of a single point)
            //			=> hashmap.get(x-coordinate of a single point).add(y-coordinate of a single point)
            //			=> hashmap.get(single_point[0]).add(single_point[1])
        }

        int minimum_area = Integer.MAX_VALUE; // Vairable to store the minimum area

        for(int i=0; i<points.length; i++){  // Iterator corresponding to different A points
            for(int j=0; j<points.length; j++){ // Iterator corresponding to different B points

                int x1 = points[i][0], y1 = points[i][1]; // Coordinates of Point A

                int x2 = points[j][0], y2 = points[j][1]; // Coordinates of Point B

                if(x1 != x2 && y1 != y2) // Point A & B must form a diagonal of the rectangle.
                    if( hashmap.get(x1).contains(y2)  // Check if D exists in hashmap
                            &&
                            hashmap.get(x2).contains(y1) ) // Check if C exists in hashmap
                        // Explanation :
                        //		Check existence of y2 in Value (HashSet) corresponding to x1 Key
                        //			=> Value.contains(y-coordinate of point B)
                        //			=> hashmap.get(Key).contains(y-coordinate of point B)
                        //			=> hashmap.get(x-coordinate of point A).contains(y-coordinate of point B)
                        //			=> hashmap.get(x1).contains(y2) => Checks if point D exists in hashmap
                        //		Check existence of y1 in Value (HashSet) corresponding to x2 Key
                        //			=> Value.contains(y-coordinate of point A)
                        //			=> hashmap.get(Key).contains(y-coordinate of point A)
                        //			=> hashmap.get(x-coordinate of point B).contains(y-coordinate of point A)
                        //			=> hashmap.get(x2).contains(y1) => Checks if point C exists in hashmap

                        minimum_area = Math.min(minimum_area, Math.abs(x1-x2) * Math.abs(y1-y2)); // Store the minimum area possible
            }
        }
        return minimum_area != Integer.MAX_VALUE ? minimum_area : 0; // Return 0 if no rectangle exists
    }

    // V4
    // IDEA : HASHMAP
    // https://leetcode.com/problems/minimum-area-rectangle/solutions/1664233/java-hashmap-o-n-2-57ms/
    public int minAreaRect_4(int[][] points) {
        HashMap<Integer,Set<Integer>> hm = new HashMap<>();
        int area=Integer.MAX_VALUE;

        for(int[] point: points)
        {
            if(!hm.containsKey(point[0]))
                hm.put(point[0],new HashSet());
            hm.get(point[0]).add(point[1]); // x-coordinate already exits, just add the y-coordinate to the set
        }

        for(int i=0;i<points.length-1;i++)
        {
            for(int j=i+1;j<points.length;j++)
            {
                int x1=points[i][0],x2=points[j][0],y1=points[i][1],y2=points[j][1]; // this step reduced the time from 368ms to 186ms
                if(x1!=x2 && y1!=y2) //diagonal is not parallel to x or y axis
                {
                    if(area>Math.abs((x2-x1) * (y2-y1))) //pre-calulate the area to avoid unecessary lookup. This step reduced the time from 186ms to 57ms.
                    {
                        if(hm.get(x1).contains(y2) && hm.get(x2).contains(y1)) // learnt from other leetcoders. Thank you.
                            area=Math.abs((x2-x1) * (y2-y1));
                    }
                }

            }
        }

        return area<Integer.MAX_VALUE? area:0;
    }

}
