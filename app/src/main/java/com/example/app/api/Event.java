package com.example.app.api;

/**
 * Created by andy on 3/12/14.
 */
public class Event {

    int id;
    String type;
    Actor actor;
    Repo repo;
    String created_at;

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public Actor getActor() {
        return actor;
    }

    public Repo getRepo() {
        return repo;
    }

    public String getCreatedAt() {
        return created_at;
    }
}
