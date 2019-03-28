# V1 
# this problem ask us to write a function can generate random points 
# inside a circle with given center and radius 
# please notice that the points should exist "equally" in the "area" of the circle (not radius)
############# dev  #############
# idea : 
# expection of points to exist in circle is : 
# E = k*(pi)*(r)^2
# where E is the expection = (probability)^2
# -> probability =  r*(k*pi)^(1/2)
# -> probability is the function of radius (r)
# -> we can generate points inside a circle with constant*r 
############# dev  #############



# V2 
# https://blog.csdn.net/fuxuemingzhu/article/details/83051190
class Solution:

    def __init__(self, radius, x_center, y_center):
        """
        :type radius: float
        :type x_center: float
        :type y_center: float
        """
        self.r = radius
        self.x = x_center
        self.y = y_center

    def randPoint(self):
        """
        :rtype: List[float]
        """
        nr = math.sqrt(random.random()) * self.r
        alpha = random.random() * 2 * 3.141592653
        newx = self.x + nr * math.cos(alpha)
        newy = self.y + nr * math.sin(alpha)
        return [newx, newy]
        
# V3 
# Time:  O(1)
# Space: O(1)

import random
import math

class Solution(object):

    def __init__(self, radius, x_center, y_center):
        """
        :type radius: float
        :type x_center: float
        :type y_center: float
        """
        self.__radius = radius
        self.__x_center = x_center
        self.__y_center = y_center
        

    def randPoint(self):
        """
        :rtype: List[float]
        """
        r = (self.__radius) * math.sqrt(random.uniform(0, 1))
        theta = (2*math.pi) * random.uniform(0, 1)
        return (r*math.cos(theta) + self.__x_center,
                r*math.sin(theta) + self.__y_center)