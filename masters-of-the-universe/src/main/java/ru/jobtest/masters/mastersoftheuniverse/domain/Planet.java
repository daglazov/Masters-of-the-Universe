package ru.jobtest.masters.mastersoftheuniverse.domain;

import lombok.Data;

@Data
public class Planet {

    private String id;
    private String name;
    private String masterId;

    public Planet(String id, String name, String masterId){
        this.id = id;
        this.name = name;
        this.masterId = masterId;
    }

    public Planet(String id, String name){
        this(id, name, null);
    }

    public Planet(String id){
        this(id, null, null);
    }

    public Planet(){
        this(null, null, null);
    }
}
