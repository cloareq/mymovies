package eu.epitech.mymovies.mymovies.Models;

public class Users {

    private long id;
    private String name;

    public Users(long id,String name) {
        this.id=id;
        this.name=name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String nom) {
        this.name = nom;
    }

}