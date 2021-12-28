
package com.company;

public class Task {
    private String name;
    private int score;

    public Task() {
    }

    public Task(String name) {
        this.name = name;
    }

    public Task(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return this.score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String toString() {
        return "Task{name='" + this.name + "', score=" + this.score + "}";
    }
}
