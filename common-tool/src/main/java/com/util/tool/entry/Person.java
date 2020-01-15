/**
 * fshows.com
 * Copyright (C) 2013-2019 All Rights Reserved.
 */
package com.util.tool.entry;

import lombok.Data;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author zhaoj
 * @version Person.java, v 0.1 2019-04-17 11:43
 */
@Data
public class Person {
    private String name;
    private FullName fullName;
    private int age;
    private Date birthday;
    private List<String> hobbies;
    private Map<String, String> clothes;
    private List<Person> friends;

    public Person() {
    }

    public Person(String name, FullName fullName, int age, Date birthday, List<String> hobbies, Map<String, String> clothes, List<Person> friends) {
        this.name = name;
        this.fullName = fullName;
        this.age = age;
        this.birthday = birthday;
        this.hobbies = hobbies;
        this.clothes = clothes;
        this.friends = friends;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.reflectionToString(this,
                ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
