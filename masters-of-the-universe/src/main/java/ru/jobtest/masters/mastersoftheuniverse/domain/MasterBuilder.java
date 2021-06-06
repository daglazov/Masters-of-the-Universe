package ru.jobtest.masters.mastersoftheuniverse.domain;

public class MasterBuilder {

    private static MasterBuilder instance = new MasterBuilder();
    private String id = null;
    private String name = null;
    private String age = null;

    private MasterBuilder(){}

    public static MasterBuilder create(){
        return instance;
    }

    public MasterBuilder withId(String id){
        this.id = id;
        return instance;
    }

    public MasterBuilder withName(String name){
        this.name = name;
        return instance;
    }

    public MasterBuilder withAge(String age){
        this.age = age;
        return instance;
    }

    public MasterBuilder withAll(String id, String name, String age){
        this.id = id;
        this.name = name;
        this.age = age;
        return instance;
    }

    public Master build(){
        Master result = new Master();
        if (id != null) result.setId(id);
        if (name != null) result.setName(name);
        if (age != null) result.setAge(age);
        return result;
    }
}
