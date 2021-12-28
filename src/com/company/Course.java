package com.company;
import java.util.ArrayList;


public class Course {
    private String name;
    private ArrayList<Themes> themes;
    private String groupName;

    public Course(String name, String groupNumber, ArrayList<Themes> themes) {
        this.name = name;
        this.themes = themes;
        this.groupName = groupNumber;
    }
    @Override
    public String toString() {
        return (String.format("%s \t %s \t %s", name, groupName, getThemesString()));
    }

    public void updateScores() {

    }

    public String getGroupName() {return groupName;}

    public String getName() {return name;}

    public void setThemes(ArrayList<Themes> themes) {
        this.themes = themes;
    }

    public ArrayList<Themes> getThemes() {return themes;}

    private String getThemesString() {
        var sb = new StringBuilder();
        for (Themes theme : themes) {
            sb.append(theme.toString());
            sb.append(", ");
        }

        return sb.toString();
    }

//    private String convertScoresArrayToString() {
//        var str = new StringBuilder();
//        for (var score : scores) {
//            str.append(score + " ");
//        }
//        return str.toString();
//    }

}
