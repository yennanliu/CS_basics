package LeetCodeJava.Design;

// https://leetcode.ca/2016-11-17-353-Design-Snake-Game/
// https://leetcode.com/problems/design-snake-game/description/

import java.util.*;

/**
 * 353 - Design Snake Game
 * Posted on November 17, 2016 · 8 minute read
 * Welcome to Subscribe On Youtube
 * <p>
 * Question
 * Formatted question description: https://leetcode.ca/all/353.html
 * <p>
 * Design a Snake game that is played on a device with screen size height x width. Play the game online if you are not familiar with the game.
 * <p>
 * The snake is initially positioned at the top left corner (0, 0) with a length of 1 unit.
 * <p>
 * You are given an array food where food[i] = (ri, ci) is the row and column position of a piece of food that the snake can eat. When a snake eats a piece of food, its length and the game's score both increase by 1.
 * <p>
 * Each piece of food appears one by one on the screen, meaning the second piece of food will not appear until the snake eats the first piece of food.
 * <p>
 * When a piece of food appears on the screen, it is guaranteed that it will not appear on a block occupied by the snake.
 * <p>
 * The game is over if the snake goes out of bounds (hits a wall) or if its head occupies a space that its body occupies after moving (i.e. a snake of length 4 cannot run into itself).
 * <p>
 * Implement the SnakeGame class:
 * <p>
 * SnakeGame(int width, int height, int[][] food) Initializes the object with a screen of size height x width and the positions of the food.
 * int move(String direction) Returns the score of the game after applying one direction move by the snake. If the game is over, return -1.
 * <p>
 * <p>
 * Example 1:
 * <p>
 * <p>
 * <p>
 * Input
 * ["SnakeGame", "move", "move", "move", "move", "move", "move"]
 * [[3, 2, [[1, 2], [0, 1]]], ["R"], ["D"], ["R"], ["U"], ["L"], ["U"]]
 * Output
 * [null, 0, 0, 1, 1, 2, -1]
 * <p>
 * Explanation
 * SnakeGame snakeGame = new SnakeGame(3, 2, [[1, 2], [0, 1]]);
 * snakeGame.move("R"); // return 0
 * snakeGame.move("D"); // return 0
 * snakeGame.move("R"); // return 1, snake eats the first piece of food. The second piece of food appears at (0, 1).
 * snakeGame.move("U"); // return 1
 * snakeGame.move("L"); // return 2, snake eats the second food. No more food appears.
 * snakeGame.move("U"); // return -1, game over because snake collides with border
 * <p>
 * <p>
 * <p>
 * Constraints:
 * <p>
 * 1 <= width, height <= 104
 * 1 <= food.length <= 50
 * food[i].length == 2
 * 0 <= ri < height
 * 0 <= ci < width
 * direction.length == 1
 * direction is 'U', 'D', 'L', or 'R'.
 * At most 104 calls will be made to move.
 *
 *
 *
 * Example:
 *
 * Given width = 3, height = 2, and food = [[1,2],[0,1]].
 *
 * Snake snake = new Snake(width, height, food);
 *
 * Initially the snake appears at position (0,0) and the food at (1,2).
 *
 * |S| | |
 * | | |F|
 *
 * snake.move("R"); -> Returns 0
 *
 * | |S| |
 * | | |F|
 *
 * snake.move("D"); -> Returns 0
 *
 * | | | |
 * | |S|F|
 *
 * snake.move("R"); -> Returns 1 (Snake eats the first food and right after that, the second food appears at (0,1) )
 *
 * | |F| |
 * | |S|S|
 *
 * snake.move("U"); -> Returns 1
 *
 * | |F|S|
 * | | |S|
 *
 * snake.move("L"); -> Returns 2 (Snake eats the second food)
 *
 * | |S|S|
 * | | |S|
 *
 * snake.move("U"); -> Returns -1 (Game over because snake collides with border)
 *
 */
public class DesignSnakeGame {

    // V0
    // TODO : implement
//    class SnakeGame {
//        private int m;
//        private int n;
//        private int[][] food;
//        private int score;
//        private int idx;
//        private Deque<Integer> q = new ArrayDeque<>();
//        private Set<Integer> vis = new HashSet<>();
//
//        public SnakeGame(int width, int height, int[][] food) {
//        }
//
//        public int move(String direction) {
//        }
//
//    }

    // V1
    // IDEA : QUEUE + SET + LOGIC
    // https://leetcode.ca/2016-11-17-353-Design-Snake-Game/
    /**
     * Overall Flow:
     *
     * 	•	The snake starts at position (0, 0).
     * 	•	It moves based on the given direction, checking for boundary violations or collisions with itself.
     * 	•	If it eats food, it grows and the score is incremented.
     * 	•	The game continues until the snake either runs into a wall or itself.
     *
     * Example:
     *
     * Given a 3x3 grid with food at positions [[1,2], [0,1]], the snake moves as follows:
     *
     * 	•	move("R"): Moves to (0, 1), no food eaten.
     * 	•	move("D"): Moves to (1, 1), eats food, score increments.
     *
     * This code efficiently handles the snake’s movement and checks for game-ending conditions.
     *
     */
    class SnakeGame_1 {

        /**
         * 1. Class Attributes
         *
         * 	•	m and n: The height and width of the game grid.
         * 	•	food: A 2D array that stores the coordinates of the food items on the grid.
         * 	•	score: Keeps track of the player’s score (number of food items eaten).
         * 	•	idx: Tracks the current food item to be eaten.
         * 	•	q: A Deque (double-ended queue) that stores the positions of the snake’s body. The head of the snake is at the front, and the tail is at the end.
         * 	•	vis: A Set that keeps track of the snake’s body positions to detect collisions with itself.
         */
        private int m;
        private int n;
        private int[][] food;
        private int score;
        private int idx;
        private Deque<Integer> q = new ArrayDeque<>();
        private Set<Integer> vis = new HashSet<>();

        /**
         * 2. Constructor (SnakeGame_1)
         *
         * 	•	Initializes the grid with width n and height m, as well as the food array.
         * 	•	The snake initially starts at position (0, 0). This position is both added to the Deque (q.offer(0)) and marked as visited in the Set (vis.add(0)).
         */

        public SnakeGame_1(int width, int height, int[][] food) {
            m = height;
            n = width;
            this.food = food;
            q.offer(0);
            vis.add(0);
        }


        /**
         *
         * 3. Move Method (move(String direction))
         *
         * This method is responsible for moving the snake based on the input direction:
         *
         * 	•	First, the current position of the snake’s head is retrieved (p = q.peekFirst()), and its row (i) and column (j) on the grid are computed using division and modulus.
         * 	•	The method then updates the coordinates (x, y) based on the direction:
         * 	•	"U" (Up): Decrease row (x--).
         * 	•	"D" (Down): Increase row (x++).
         * 	•	"L" (Left): Decrease column (y--).
         * 	•	"R" (Right): Increase column (y++).
         * 	•	If the snake moves out of bounds (x < 0 || x >= m || y < 0 || y >= n), the game is lost, and -1 is returned.
         *
         */
        public int move(String direction) {
            int p = q.peekFirst();
            int i = p / n, j = p % n;
            int x = i, y = j;
            if ("U".equals(direction)) {
                --x;
            } else if ("D".equals(direction)) {
                ++x;
            } else if ("L".equals(direction)) {
                --y;
            } else {
                ++y;
            }
            if (x < 0 || x >= m || y < 0 || y >= n) {
                return -1;
            }
            /**
             * 4. Food Collision Check
             *
             * 	•	If the snake moves to a cell where a food item is located (x == food[idx][0] && y == food[idx][1]), the score is incremented, and the index idx is updated to point to the next food item.
             * 	•	If no food is eaten, the snake moves normally, and the tail is removed from both the Deque (q.pollLast()) and the Set (vis.remove(t)).
             */
            if (idx < food.length && x == food[idx][0] && y == food[idx][1]) {
                ++score;
                ++idx;
            } else {
                int t = q.pollLast();
                // NOTE : vis: A Set that keeps track of the snake’s body positions to detect collisions with itself.
                vis.remove(t);
            }
            int cur = f(x, y);
            /**
             * 5. Collision with Itself
             *
             * 	•	After moving, the method checks if the new position (x, y) is already occupied by the snake itself (vis.contains(cur)). If so, the game is lost, and -1 is returned.
             */
            if (vis.contains(cur)) {
                return -1;
            }
            q.offerFirst(cur);
            vis.add(cur);
            return score;
        }

        /**
         * 6. Utility Method (f(int i, int j))
         *
         * 	•	This helper method converts the row and column coordinates (i, j) of the grid into a single integer that uniquely identifies each cell. It uses the formula i * n + j to calculate a unique value for each cell.
         *
         *
         * 	NOTE :
         * 	 1. purpose of "f" method : calculate a unique value
         * 	 2. can be something different, as long as it can create unique value
         */
        private int f(int i, int j) {
            return i * n + j;
        }
    }

    // V2
    // https://www.jiakaobo.com/leetcode/353.%20Design%20Snake%20Game.html
    // https://www.youtube.com/watch?v=NPIkFa2GbME
    class SnakeGame_2 {
        // 记录自己的位置,包括全部的身体
        // 因此也可以从这个set来判断是不是撞到了自己
        HashSet<Integer> set;
        // 当前蛇的情况,全部坐标
        Deque<Integer> deque;
        // 当前得分
        int score;
        // 第几个食物
        int foodIndex;
        int width;
        int height;
        // 食物坐标
        int[][] food;

        /** Initialize your data structure here.
         @param width - screen width
         @param height - screen height
         @param food - A list of food positions
         E.g food = [[1,1], [1,0]] means the first food is positioned at [1,1], the second is at [1,0]. */
        public SnakeGame_2(int width, int height, int[][] food) {
            this.width = width;
            this.height = height;
            this.food = food;
            set = new HashSet<>();
            deque = new LinkedList<>();
            score = 0;
            foodIndex = 0;
            set.add(0);
            deque.offerLast(0);
        }

        /** Moves the snake.
         @param direction - 'U' = Up, 'L' = Left, 'R' = Right, 'D' = Down
         @return The game's score after the move. Return -1 if game over.
         Game over when snake crosses the screen boundary or bites its body. */
        public int move(String direction) {
            // 当前头部的坐标
            int rowHead = deque.peekFirst() / width;
            int colHead = deque.peekFirst() % width;

            switch(direction){
                case "U": rowHead--;
                    break;
                case "D":rowHead++;
                    break;
                case "L":colHead--;
                    break;
                default : colHead++;
            }

            // 新头部的位置
            int head = rowHead * width + colHead;
            // 删除尾部,因为马上要走下一步了,尾部会往前移动
            // 尾部不可能有食物
            set.remove(deque.peekLast());
            // 1. 撞到墙或者自己
            if(rowHead < 0 || rowHead == height || colHead < 0 || colHead == width || set.contains(head)){
                return -1;
            }

            // 2. 吃东西或者走路
            // 加入head
            set.add(head);
            deque.offerFirst(head);

            // 是不是吃到了食物
            // foodIndex记录了第几个food
            // foodIndex < food.length表示还有食物可以吃
            // 并且此时新的头部还正好在食物的位置
            if(foodIndex < food.length && rowHead == food[foodIndex][0] && colHead == food[foodIndex][1]){
                foodIndex++;
                score++;
                // 身体有增长了!!
                // 在set中再把尾部加上
                // 同时不对deque进行poll()
                set.add(deque.peekLast());
                return score;
            }

            // 只走路,没吃东西
            deque.pollLast();
            return score;
        }
    }

    // V3
}
