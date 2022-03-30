"""

1603. Design Parking System
Easy

Design a parking system for a parking lot. The parking lot has three kinds of parking spaces: big, medium, and small, with a fixed number of slots for each size.

Implement the ParkingSystem class:

ParkingSystem(int big, int medium, int small) Initializes object of the ParkingSystem class. The number of slots for each parking space are given as part of the constructor.
bool addCar(int carType) Checks whether there is a parking space of carType for the car that wants to get into the parking lot. carType can be of three kinds: big, medium, or small, which are represented by 1, 2, and 3 respectively. A car can only park in a parking space of its carType. If there is no space available, return false, else park the car in that size space and return true.
 

Example 1:

Input
["ParkingSystem", "addCar", "addCar", "addCar", "addCar"]
[[1, 1, 0], [1], [2], [3], [1]]
Output
[null, true, true, false, false]

Explanation
ParkingSystem parkingSystem = new ParkingSystem(1, 1, 0);
parkingSystem.addCar(1); // return true because there is 1 available slot for a big car
parkingSystem.addCar(2); // return true because there is 1 available slot for a medium car
parkingSystem.addCar(3); // return false because there is no available slot for a small car
parkingSystem.addCar(1); // return false because there is no available slot for a big car. It is already occupied.
 

Constraints:

0 <= big, medium, small <= 1000
carType is 1, 2, or 3
At most 1000 calls will be made to addCar

"""

# V0

# V1
# https://leetcode.com/problems/design-parking-system/discuss/876953/JavaC%2B%2BPython-3-Lines
class ParkingSystem:

    def __init__(self, big, medium, small):
        self.A = [big, medium, small]

    def addCar(self, carType):
        self.A[carType - 1] -= 1
        return self.A[carType - 1] >= 0

# V1'
# https://leetcode.com/problems/design-parking-system/discuss/876893/python-Easy-4-line-code
class ParkingSystem:

    def __init__(self, big: int, medium: int, small: int):
        self.arr  =[big,medium,small]
    def addCar(self, carType: int) -> bool:
        if(self.arr[carType-1]):
            self.arr[carType-1]-=1
            return True
        return False

# V1''
# https://leetcode.com/problems/design-parking-system/discuss/1128843/Python-fast-and-efficient-(97)
class ParkingSystem:
    __slots__ = ['_parking']
	
    def __init__(self, big: int, medium: int, small: int):
        self._parking = {
            1: big, 
            2: medium, 
            3: small
        }

    def addCar(self, carType: int) -> bool:
        available_spots = self._parking[carType]
        if available_spots:
            self._parking[carType] = available_spots - 1
            return True

# V2