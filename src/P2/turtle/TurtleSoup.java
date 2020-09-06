/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package P2.turtle;

import javax.xml.crypto.dsig.spec.XSLTTransformParameterSpec;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;

public class TurtleSoup {

    /**
     * Draw a square.
     * 
     * @param turtle the turtle context
     * @param sideLength length of each side
     */
    public static void drawSquare(Turtle turtle, int sideLength) {

        int turnTimes = 4;
        for(int i = 0; i < turnTimes; ++i){
            turtle.forward(sideLength);
            turtle.turn(90);
        }

    }

    /**
     * Determine inside angles of a regular polygon.
     * 
     * There is a simple formula for calculating the inside angles of a polygon;
     * you should derive it and use it here.
     * 
     * @param sides number of sides, where sides must be > 2
     * @return angle in degrees, where 0 <= angle < 360
     */
    public static double calculateRegularPolygonAngle(int sides) {
        double angle = 180.0 - 360.0 / sides;
        return angle;
    }

    /**
     * Determine number of sides given the size of interior angles of a regular polygon.
     * 
     * There is a simple formula for this; you should derive it and use it here.
     * Make sure you *properly round* the answer before you return it (see java.lang.Math).
     * HINT: it is easier if you think about the exterior angles.
     * 
     * @param angle size of interior angles in degrees, where 0 < angle < 180
     * @return the integer number of sides
     */
    public static int calculatePolygonSidesFromAngle(double angle) {
        int sides = (int)Math.round(360.0 / (180 - angle));
        return sides;
    }

    /**
     * Given the number of sides, draw a regular polygon.
     * 
     * (0,0) is the lower-left corner of the polygon; use only right-hand turns to draw.
     * 
     * @param turtle the turtle context
     * @param sides number of sides of the polygon to draw
     * @param sideLength length of each side
     */
    public static void drawRegularPolygon(Turtle turtle, int sides, int sideLength) {
        double angle = 180 - calculateRegularPolygonAngle(sides);
        for(int i = 0; i < sides; ++i){
            turtle.forward(sideLength);
            turtle.turn(angle);
        }
    }

    /**
     * Given the current direction, current location, and a target location, calculate the Bearing
     * towards the target point.
     * 
     * The return value is the angle input to turn() that would point the turtle in the direction of
     * the target point (targetX,targetY), given that the turtle is already at the point
     * (currentX,currentY) and is facing at angle currentBearing. The angle must be expressed in
     * degrees, where 0 <= angle < 360. 
     *
     * HINT: look at http://en.wikipedia.org/wiki/Atan2 and Java's math libraries
     * 
     * @param currentBearing current direction as clockwise from north
     * @param currentX current location x-coordinate
     * @param currentY current location y-coordinate
     * @param targetX target point x-coordinate
     * @param targetY target point y-coordinate
     * @return adjustment to Bearing (right turn amount) to get to target point,
     *         must be 0 <= angle < 360
     */
    public static double calculateBearingToPoint(double currentBearing, int currentX, int currentY,
                                                 int targetX, int targetY) {
        int deltaX = targetX - currentX;
        int deltaY = targetY - currentY;
        double relativeBearing = Math.toDegrees(Math.atan2(deltaY, deltaX));
        double degree = 90 - relativeBearing - currentBearing;
        if(degree < 0){
            degree += 360;
        }
        return degree;

    }

    /**
     * Given a sequence of points, calculate the Bearing adjustments needed to get from each point
     * to the next.
     * 
     * Assumes that the turtle starts at the first point given, facing up (i.e. 0 degrees).
     * For each subsequent point, assumes that the turtle is still facing in the direction it was
     * facing when it moved to the previous point.
     * You should use calculateBearingToPoint() to implement this function.
     * 
     * @param xCoords list of x-coordinates (must be same length as yCoords)
     * @param yCoords list of y-coordinates (must be same length as xCoords)
     * @return list of Bearing adjustments between points, of size 0 if (# of points) == 0,
     *         otherwise of size (# of points) - 1
     */
    public static List<Double> calculateBearings(List<Integer> xCoords, List<Integer> yCoords) {
       double currentBearing = 0;
       List<Double> ans = new ArrayList<>();
       for(int i = 0; i < xCoords.size() - 1; ++i){

           double relativeBearing = calculateBearingToPoint(currentBearing, xCoords.get(i), yCoords.get(i), xCoords.get(i + 1), yCoords.get(i + 1));
           relativeBearing = relativeBearing % 360;
           ans.add(relativeBearing);
           currentBearing += relativeBearing;

       }
       return ans;
    }
    
    /**
     * Given a set of points, compute the convex hull, the smallest convex set that contains all the points 
     * in a set of input points. The gift-wrapping algorithm is one simple approach to this problem, and 
     * there are other algorithms too.
     * 
     * @param points a set of points with xCoords and yCoords. It might be empty, contain only 1 point, two points or more.
     * @return minimal subset of the input points that form the vertices of the perimeter of the convex hull
     */
    public static Set<Point> convexHull(Set<Point> points) {
        int n = points.size();
        if(n < 3){
            return new HashSet<>(points);
        }
        //找到凸包最左下的点
        Point leftDownPoint = findLeftDownPoint(points);
        //按照极角从小到大排序
        List<Point> pointList = new ArrayList<>();
        pointList.addAll(points);
        Collections.sort(pointList, new Comparator<Point>() {
            @Override
            public int compare(Point o1, Point o2) {
                //保证最左点在第一个
                if(o1 == leftDownPoint) return -1;
                if(o2 == leftDownPoint) return 1;
                //计算极角
                double degree1 = Math.atan2(o1.y() - leftDownPoint.y(), o1.x() - leftDownPoint.x());
                double degree2 = Math.atan2(o2.y() - leftDownPoint.y(), o2.x() - leftDownPoint.x());
                if(Math.abs(degree1 - degree2) < 0.001){//如果极角相等，按照离最左下点的距离从小到大排序
                    if(getDistance(o1, leftDownPoint) < getDistance(o2, leftDownPoint)){
                        return -1;
                    }else{
                        return 1;
                    }
                }else if(degree1 < degree2){//极角小的排在前面
                    return -1;
                }else{
                    return 1;
                }
            }
        });

        Deque<Point> stack = new ArrayDeque<>();
        stack.push(pointList.get(0));
        stack.push(pointList.get(1));
        for(int i = 2; i < n; ++i){
            while(stack.size() >= 2){
                Point k2 = stack.peek();stack.pop();
                Point k1 = stack.peek(); stack.push(k2);//得到栈里的栈顶元素和栈顶下一个元素
                if(getCross(k1, k2, pointList.get(i)) <= 0){//如果直走或者右拐了
                    stack.pop();
                }else{
                    break;
                }
            }
            stack.push(pointList.get(i));
        }
        Set<Point> ans = new HashSet<>();
        ans.addAll(stack);
        return ans;
    }
    private static double getDistance(Point a, Point b){
        double dx = a.x() - b.x();
        double dy = a.y() - b.y();
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * 寻找凸包最左下的点。
     * 首先要最下，如果有多个最下，再找最左
     * @param points
     * @return
     */
    private static Point findLeftDownPoint(Set<Point> points){
        Point leftDownPoint = null;
        for(Point p: points){
            if(leftDownPoint == null){
                leftDownPoint = p;
            }else{
                if(leftDownPoint.y() > p.y()){
                    leftDownPoint = p;
                }else if(Math.abs(leftDownPoint.y() - p.y()) < 0.001){//如果都是最下
                    if(leftDownPoint.x() > p.x()){
                        leftDownPoint = p;
                    }
                }
            }
        }
        return leftDownPoint;
    }

    /**
     * a,b,c三点按照从左下到右上的顺序。计算向量(b-a),(c-b)的叉积
     * @param a
     * @param b
     * @param c
     * @return
     */
    private static double getCross(Point a, Point b, Point c){
        double x1 = b.x() - a.x();
        double y1 = b.y() - a.y();
        double x2 = c.x() - b.x();
        double y2 = c.y() - b.y();
        return x1 * y2 - x2 * y1;
    }
    
    /**
     * Draw your personal, custom art.
     * 
     * Many interesting images can be drawn using the simple implementation of a turtle.  For this
     * function, draw something interesting; the complexity can be as little or as much as you want.
     * 
     * @param turtle the turtle context
     */
    public static void drawPersonalArt(Turtle turtle) {

        for(int i = 0; i < 5; ++i){
            turtle.forward(50);
            turtle.turn(144);
        }
    }

    /**
     * Main method.
     * 
     * This is the method that runs when you run "java TurtleSoup".
     * 
     * @param args unused
     */
    public static void main(String args[]) {
        DrawableTurtle turtle = new DrawableTurtle();

        drawSquare(turtle, 40);
        drawRegularPolygon(turtle, 7, 40);
        drawPersonalArt(turtle);
        // draw the window
        turtle.draw();
    }

}
