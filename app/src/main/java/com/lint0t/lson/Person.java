package com.lint0t.lson;

import java.util.ArrayList;
import java.util.List;

public class Person {
    private int age;
    private String name;
    private ArrayList<String> major;
    private ArrayList<myData> data;

    public void setAge(int age) {
        this.age = age;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getMajor() {
        return major;
    }

    public void setMajor(ArrayList<String> major) {
        this.major = major;
    }

    public ArrayList<myData> getData() {
        return data;
    }

    public int getAge() {
        return age;
    }

    public String getName() {
        return name;
    }

    public class myData {
        private int id;

        public myData() {
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
