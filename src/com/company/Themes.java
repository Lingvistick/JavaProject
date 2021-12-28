package com.company;

import java.util.ArrayList;
import java.util.List;

public class Themes {
    private String studentName;
    private String name;
    private int totalScore;
    private int countScores;
    private List<Task> tasks = new ArrayList();

    public Themes(String name, String[] taskName,  int scoresCount) {
        this.name = name;
        this.countScores = scoresCount;
        for (String tName: taskName) {
            tasks.add(new Task(tName));
        }
    }

    public Themes(String studentName, String name, List<Task> tasks) {
        this.studentName = studentName;
        this.name = name;
        this.tasks = tasks;
    }

    public void addTask(Task task) {
        this.tasks.add(task);
    }

    public String getStudentName() {
        return this.studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Task> getTasks() {
        return this.tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public void setScoresTasks(Integer[] scoresTasks){
        int i = 0;
        for (Task task: tasks){
            task.setScore(scoresTasks[i]);
            i++;
        }
    }

    public String toString() {
        return "Themes{studentName='" + this.studentName + "', name='" + this.name + "', tasks=" + this.tasks + "}";
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public int getCountScores() {
        return countScores;
    }

    public void setCountScores(int countScores) {
        this.countScores = countScores;
    }

}
