package com.shareniu.bpmn.ch10;

import java.io.Serializable;

public class Person implements Serializable {
    private  static  final  long serialVersionUid=45323;

    private  Integer age;
    private  String name;

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
