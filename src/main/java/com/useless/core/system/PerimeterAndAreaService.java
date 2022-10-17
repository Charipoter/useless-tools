package com.useless.core.system;

public class PerimeterAndAreaService {

    public double getCirclePerimeter(double radius) {
        return 2 * Math.PI * radius;
    }

    public double getCircleArea(double radius) {
        return Math.PI * radius * radius;
    }

    public double getSquarePerimeter(double length) {
        return 4 * length;
    }

    public double getSquareArea(double length) {
        return length * length;
    }

}
