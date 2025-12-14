package com.joblink.model;

public class Job {
    private int id;
    private String title;
    private String description;
    private double salary;
    private String location;
    private int postedBy;

    public Job() {
    }

    public Job(int id, String title, String description, double salary, String location, int postedBy) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.salary = salary;
        this.location = location;
        this.postedBy = postedBy;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(int postedBy) {
        this.postedBy = postedBy;
    }
}
