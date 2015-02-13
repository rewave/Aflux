package com.aflux.core;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("People")
public class Person extends ParseObject {

    private OnCoreInteractionListener mListner;

    public Person() {

    }

    public String getName() {
        return getString("name");
    }

    public void setName(String name) {
        put("name", name);
    }

    public int getAge() {
        return getInt("age");
    }

    public void setAge(int age) {
        put("age", age);
    }

    public String getSex() {
        return getString("sex");
    }

    public void setSex(String sex) {
        put("sex", sex);
    }

    public interface OnCoreInteractionListener {

    }
}

