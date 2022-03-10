"""

353. Design Snake Game
Medium

Design a Snake game that is played on a device with screen size height x width. Play the game online if you are not familiar with the game.

The snake is initially positioned at the top left corner (0, 0) with a length of 1 unit.

You are given an array food where food[i] = (ri, ci) is the row and column position of a piece of food that the snake can eat. When a snake eats a piece of food, its length and the game's score both increase by 1.

Each piece of food appears one by one on the screen, meaning the second piece of food will not appear until the snake eats the first piece of food.

When a piece of food appears on the screen, it is guaranteed that it will not appear on a block occupied by the snake.

The game is over if the snake goes out of bounds (hits a wall) or if its head occupies a space that its body occupies after moving (i.e. a snake of length 4 cannot run into itself).

Implement the SnakeGame class:

SnakeGame(int width, int height, int[][] food) Initializes the object with a screen of size height x width and the positions of the food.
int move(String direction) Returns the score of the game after applying one direction move by the snake. If the game is over, return -1.
 

Example 1:


Input
["SnakeGame", "move", "move", "move", "move", "move", "move"]
[[3, 2, [[1, 2], [0, 1]]], ["R"], ["D"], ["R"], ["U"], ["L"], ["U"]]
Output
[null, 0, 0, 1, 1, 2, -1]

Explanation
SnakeGame snakeGame = new SnakeGame(3, 2, [[1, 2], [0, 1]]);
snakeGame.move("R"); // return 0
snakeGame.move("D"); // return 0
snakeGame.move("R"); // return 1, snake eats the first piece of food. The second piece of food appears at (0, 1).
snakeGame.move("U"); // return 1
snakeGame.move("L"); // return 2, snake eats the second food. No more food appears.
snakeGame.move("U"); // return -1, game over because snake collides with border
 

Constraints:

1 <= width, height <= 104
1 <= food.length <= 50
food[i].length == 2
0 <= ri < height
0 <= ci < width
direction.length == 1
direction is 'U', 'D', 'L', or 'R'.
At most 104 calls will be made to move.

"""

# V0

# V1
# IDEA : Queue and Hash Set
# https://leetcode.com/problems/design-snake-game/solution/
class SnakeGame:

    def __init__(self, width: int, height: int, food: List[List[int]]):
        """
        Initialize your data structure here.
        @param width - screen width
        @param height - screen height 
        @param food - A list of food positions
        E.g food = [[1,1], [1,0]] means the first food is positioned at [1,1], the second is at [1,0].
        """
        self.snake = collections.deque([(0,0)])    # snake head is at the front
        self.snake_set = {(0,0) : 1}
        self.width = width
        self.height = height
        self.food = food
        self.food_index = 0
        self.movement = {'U': [-1, 0], 'L': [0, -1], 'R': [0, 1], 'D': [1, 0]}
        

    def move(self, direction: str) -> int:
        """
        Moves the snake.
        @param direction - 'U' = Up, 'L' = Left, 'R' = Right, 'D' = Down 
        @return The game's score after the move. Return -1 if game over. 
        Game over when snake crosses the screen boundary or bites its body.
        """
        
        newHead = (self.snake[0][0] + self.movement[direction][0],
                   self.snake[0][1] + self.movement[direction][1])
        
        # Boundary conditions.
        crosses_boundary1 = newHead[0] < 0 or newHead[0] >= self.height
        crosses_boundary2 = newHead[1] < 0 or newHead[1] >= self.width
        
        # Checking if the snake bites itself.
        bites_itself = newHead in self.snake_set and newHead != self.snake[-1]
     
        # If any of the terminal conditions are satisfied, then we exit with rcode -1.
        if crosses_boundary1 or crosses_boundary2 or bites_itself:
            return -1

        # Note the food list could be empty at this point.
        next_food_item = self.food[self.food_index] if self.food_index < len(self.food) else None
        
        # If there's an available food item and it is on the cell occupied by the snake after the move, eat it
        if self.food_index < len(self.food) and \
            next_food_item[0] == newHead[0] and \
                next_food_item[1] == newHead[1]:  # eat food
            self.food_index += 1
        else:    # not eating food: delete tail                 
            tail = self.snake.pop()  
            del self.snake_set[tail]
            
        # A new head always gets added
        self.snake.appendleft(newHead)
        
        # Also add the head to the set
        self.snake_set[newHead] = 1

        return len(self.snake) - 1
        
# Your SnakeGame object will be instantiated and called as such:
# obj = SnakeGame(width, height, food)
# param_1 = obj.move(direction)

# V1
# https://leetcode.com/problems/design-snake-game/discuss/514781/fast-python-solution
class SnakeGame:

    def __init__(self, width: int, height: int, food: List[List[int]]):
        """
        Initialize your data structure here.
        @param width - screen width
        @param height - screen height 
        @param food - A list of food positions
        E.g food = [[1,1], [1,0]] means the first food is positioned at [1,1], the second is at [1,0].
        """
        self.snake = [[0, 0]]
        self.food = food
        self.width = width
        self.height = height
        self.eat = 0

    def move(self, direction: str) -> int:
        """
        Moves the snake.
        @param direction - 'U' = Up, 'L' = Left, 'R' = Right, 'D' = Down 
        @return The game's score after the move. Return -1 if game over. 
        Game over when snake crosses the screen boundary or bites its body.
        """
        #print(self.snake)
        x, y = self.snake[0]
        
        if direction == 'U':
            x = x - 1
        elif direction == 'L':
            y = y - 1
        elif direction == 'R':
            y = y + 1
        elif direction == 'D':
            x = x + 1
            
        if 0 <=  x < self.height and 0 <= y < self.width and [x, y] not in self.snake[:-1]:
            self.snake.insert(0, [x, y])
        else:
            return -1
            
        if self.eat < len(self.food) and self.snake[0] == self.food[self.eat]:
            self.eat += 1
        else:
            del self.snake[-1]
        return self.eat

# V1
# IDEA : OrderedDict
# https://leetcode.com/problems/design-snake-game/discuss/1751562/Python-or-OrderedDict
class SnakeGame:

    def __init__(self, width: int, height: int, food: List[List[int]]):
        self.snake = OrderedDict() #keep track of the cells where occupied by the snake
        self.snake[(0, 0)] = True
        self.food = food
        self.foodPointer = 0
        self.width = width
        self.height = height
        self.score = 0
        self.head = (0, 0)
        self.ate = False
        

    def move(self, direction: str) -> int:
        directionMoves = {"U": (-1, 0), "D": (1, 0), "L": (0, -1), "R":(0, 1)}
        dy, dx = directionMoves[direction]
        i, j = self.head
        ni, nj = i + dy, j + dx
        if not self.ate:
            self.snake.popitem(last = False)
        if 0 <= ni < self.height and 0 <= nj < self.width and (ni, nj) not in self.snake:
            self.snake[(ni, nj)] = True
            self.head = (ni, nj)
            if self.foodPointer < len(self.food) and [ni, nj] == self.food[self.foodPointer]:
                self.score += 1
                self.foodPointer += 1
                self.ate = True
            else:
                self.ate = False
            return self.score
        else:
            return -1

# V1
# IDEA : QUEUE
# https://leetcode.com/problems/design-snake-game/discuss/1778152/Python-Queue
class SnakeGame:

    def __init__(self, width: int, height: int, food: List[List[int]]):
        self.body = deque([(0,0)])
        self.C = width
        self.R = height
        self.food = food[::-1]
        self.score = 0
        
    def out_of_bounds(self, row, col):
        if row < 0 or col < 0 or row >= self.R or col >= self.C:
            return True        
        return False
        
    def food_eaten(self, row, col):   
        return (row, col) == tuple(self.food[-1])   
    
    def self_bite(self, row, col):
        for i in range(len(self.body) - 1):
            if self.body[i] == (row, col):
                return True
            
        return False
                    
    def move(self, direction: str) -> int:        
        r, c  = self.body[-1]        
        row, col = {'U': (r - 1,c), 'D': (r + 1, c), 'L': (r, c - 1), 'R': (r, c + 1)}[direction]
        
        if self.out_of_bounds(row, col):
            return -1
                
        self.body.append((row, col))
        
        if self.food and self.food_eaten(row, col):            
            self.food.pop()
            self.score += 1
        else:            
            self.body.popleft()
                
        if self.self_bite(row, col):
            return -1 

        return self.score

# V1
# https://leetcode.com/problems/design-snake-game/discuss/82744/Easy-python-solution
# IDEA : 
# I use a queue to simulate the snake and use a set to keep all parts of the snake body.
# Each move we will first pop the tail, get the head, and get the new head based on the direction.
# If the new head come cross the body or get out of the boundary, it will return -1.
# We enqueue the new head, add the new head to the body set.
# If the new head meets the food, we will put the tail back. Notice the axises of positions of the food are reversed ([1,2] is actually [2,1]).
class SnakeGame(object):

    def __init__(self, width,height,food):
        """
        Initialize your data structure here.
        @param width - screen width
        @param height - screen height 
        @param food - A list of food positions
        E.g food = [[1,1], [1,0]] means the first food is positioned at [1,1], the second is at [1,0].
        :type width: int
        :type height: int
        :type food: List[List[int]]
        """
        self.foodIndex=0
        self.snake=collections.deque() #A queue as the snake
        self.snake.append((0,0))
        self.body={(0,0)} #A set to keep all positions of the snake
        self.foods=food
        self.width=width
        self.height=height
        self.moves={'U':(0,-1),'L':(-1,0),'R':(1,0),'D':(0,1)}


    def move(self, direction):
        """
        Moves the snake.
        @param direction - 'U' = Up, 'L' = Left, 'R' = Right, 'D' = Down 
        @return The game's score after the move. Return -1 if game over. 
        Game over when snake crosses the screen boundary or bites its body.
        :type direction: str
        :rtype: int
        """
        tail=self.snake.popleft() #pop out the tail
        self.body.remove(tail)
        if not self.snake:
            head=tail
        else:
            head=self.snake[-1]
        xm,ym=self.moves[direction]
        nx,ny=head[0]+xm,head[1]+ym
        if (nx,ny) in self.body or nx<0 or nx>=self.width or ny<0 or ny>=self.height:
            return -1
        self.snake.append((nx,ny)) #append the new head
        self.body.add((nx,ny))
        if self.foodIndex<len(self.foods) and nx==self.foods[self.foodIndex][1] and ny==self.foods[self.foodIndex][0]:
            self.foodIndex+=1
            self.snake.appendleft(tail)
            self.body.add(tail)
        #Add back the tail if the snake eat a food
        return len(self.snake)-1

# V1
# IDEA : Set and Deque 
# https://leetcode.com/problems/design-snake-game/discuss/82723/Simple-Python-solution
from collections import deque
class SnakeGame(object):

    def __init__(self, width,height,food):
        """
        Initialize your data structure here.
        @param width - screen width
        @param height - screen height 
        @param food - A list of food positions
        E.g food = [[1,1], [1,0]] means the first food is positioned at [1,1], the second is at [1,0].
        :type width: int
        :type height: int
        :type food: List[List[int]]
        """
        self.food = deque(food)
        self.width = width
        self.height = height
        self.bodyQueue = deque([(0, 0)])
        self.hashSet = set([(0, 0)])
        self.score = 0
        self.moveOps = {"U": (-1, 0), "D": (1, 0), "L": (0, -1), "R": (0, 1)}

    def move(self, direction):
        """
        Moves the snake.
        @param direction - 'U' = Up, 'L' = Left, 'R' = Right, 'D' = Down 
        @return The game's score after the move. Return -1 if game over. 
        Game over when snake crosses the screen boundary or bites its body.
        :type direction: str
        :rtype: int
        """
        
        s = self.hashSet
        q = self.bodyQueue
        ops = self.moveOps
        width = self.width
        height = self.height
        f = self.food
        
        if direction not in ops:
            return -1
        
        headi, headj = q[0]
        di, dj = ops[direction]
        headi, headj = headi + di, headj + dj
        if headi < 0 or headi >= height or headj < 0 or headj >= width:
            return -1
        
        q.appendleft((headi, headj))
        
        if f and [headi, headj] == f[0]:
            self.score += 1
            f.popleft()
        else:
            s.discard(q.pop())
            
        if (headi, headj) in s:
            return -1
        s.add((headi, headj))
        
        return self.score

# V1
# https://www.cnblogs.com/grandyang/p/5558033.html
# JAVA
# class SnakeGame {
# public:
#     /** Initialize your data structure here.
#         @param width - screen width
#         @param height - screen height 
#         @param food - A list of food positions
#         E.g food = [[1,1], [1,0]] means the first food is positioned at [1,1], the second is at [1,0]. */
#     SnakeGame(int width, int height, vector<pair<int, int>> food) {
#         this->width = width;
#         this->height = height;
#         this->food = food;
#         score = 0;
#         snake.push_back({0, 0});
#     }
#    
#     /** Moves the snake.
#         @param direction - 'U' = Up, 'L' = Left, 'R' = Right, 'D' = Down 
#         @return The game's score after the move. Return -1 if game over. 
#         Game over when snake crosses the screen boundary or bites its body. */
#     int move(string direction) {
#         auto head = snake.front(), tail = snake.back();
#         snake.pop_back();
#         if (direction == "U") --head.first;
#         else if (direction == "L") --head.second;
#         else if (direction == "R") ++head.second;
#         else if (direction == "D") ++head.first;
#         if (count(snake.begin(), snake.end(), head) || head.first < 0 || head.first >= height || head.second < 0 || head.second >= width) {
#             return -1;
#         }
#         snake.insert(snake.begin(), head);
#         if (!food.empty() && head == food.front()) {
#             food.erase(food.begin());
#             snake.push_back(tail);
#             ++score;
#         }
#         return score;
#     }
#
# private:
#     int width, height, score;
#     vector<pair<int, int>> food, snake;
# };

# V2