package com.cca;

public class Test {
    public static void main(String[] args) {
        double x = 26.8921234;
        long records = 100003;
        double startReceiveTime = System.currentTimeMillis() -100000;
        double x1 = 1.0D * ( System.currentTimeMillis() - startReceiveTime)/records;
        System.out.println(x1);
    }
}
