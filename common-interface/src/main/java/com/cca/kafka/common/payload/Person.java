package com.cca.kafka.common.payload;
import java.util.Random;

public class Person {

    private char gender;
    private long salary;
    private int age;
    private long creationTime;

    public Person() {
        this.salary = getNumber(90000, 120000);
        this.gender = getNumber(0, 2) == 0 ? 'F' : 'M';
        this.age = getNumber(22, 65);
        this.creationTime = System.nanoTime();
    }

    public Person(char gender, long salary, long time) {
        this.gender = gender;
        this.salary = salary;
        this.creationTime = time;
    }

    public int getNumber(int min, int max) {
        Random random = new Random();
        return random.ints(min, max)
            .findFirst()
            .getAsInt();
    }

    @Override
    public String toString() {
        return "{" +
            "gender=" + gender +
            ", salary=" + salary +
            ", age=" + age +
            ", creationTime=" + creationTime +
            '}';
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public long getSalary() {
        return salary;
    }

    public void setSalary(long salary) {
        this.salary = salary;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }
}