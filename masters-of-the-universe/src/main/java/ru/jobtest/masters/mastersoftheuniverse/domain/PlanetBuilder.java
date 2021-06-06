package ru.jobtest.masters.mastersoftheuniverse.domain;

public class PlanetBuilder {

    private static PlanetBuilder instance = new PlanetBuilder();
    private String id = null;
    private String name = null;
    private String masterId = null;

    private PlanetBuilder(){}

    public static PlanetBuilder create(){return instance;}

    public PlanetBuilder withId(String id){
        this.id = id;
        return instance;
    }

    public PlanetBuilder withName(String name){
        this.name = name;
        return instance;
    }

    public PlanetBuilder withMaster(String masterId){
        this.masterId = masterId;
        return instance;
    }

    public PlanetBuilder withAll(String id, String name, String masterId){
        this.id = id;
        this.name = name;
        this.masterId = masterId;
        return instance;
    }

    public Planet build(){
        Planet result = new Planet();
        if (id != null) result.setId(id);
        if (name != null) result.setName(name);
        if (masterId != null) result.setMasterId(masterId);
        return result;
    }
}
