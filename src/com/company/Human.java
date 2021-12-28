package com.company;

import java.util.ArrayList;
import java.util.Calendar;

public class Human {
    String family;
    String name;
    String city = "Не указано";
    Calendar birthday = null;
    String nickname = "Не указано";
    String photo = "Не указано";
    String sex = "Не указано";
    String points_for_topics;
    private ArrayList<Course> courses;

    Human(){

    }

    Human(String name, String family) {
        this.name = name;
        this.family = family;
        courses = new ArrayList<>();
    }

    Human(String name, String family, String points_for_topics, ArrayList<Course> courses) {
        this.family = family;
        this.name = name;
        this.points_for_topics = points_for_topics;
        this.courses = courses;
    }

    void setCity(String city) {
        this.city = city;
    }

    public String getStringBirthdate() {
        if (birthday == null) return null;
        return String.format("%d-%d-%d", birthday.get(Calendar.YEAR), birthday.get(Calendar.MONTH), birthday.get(Calendar.DATE));
    }

    public void setBirthday(Calendar birthday) { this.birthday = birthday; }

    void setNickname(String nickname) {
        this.nickname = nickname;
    }

    void setPhoto(String photo) {
        this.photo = photo;
    }

    void setSex(Integer sex) {
        if (sex == 1) {
            this.sex = "Жен";
        } else if (sex == 2) {
            this.sex = "Муж";
        }

    }

    String getFullName(){
        return getName() + " " + getFamily();
    }
    String getName() {
        return this.name;
    }

    String getFamily() {
        return this.family;
    }

    String getCity() {
        return this.city;
    }

    Calendar getBirthday() {
        return this.birthday;
    }

    String getNickname() {
        return this.nickname;
    }

    String getPhoto() {
        return this.photo;
    }

    String getSex() {
        return this.sex;
    }

    String Point_for_topics() {
        return this.points_for_topics;
    }

    public ArrayList<Course> getCourses() {
        return courses;
    }

    public void setCourses(ArrayList<Course> courses) {
        this.courses = courses;
    }
}
