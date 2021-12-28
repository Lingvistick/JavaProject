package com.company;

import java.util.ArrayList;

public class Student extends Human {
    Student(String name, String family, String points_for_topics, ArrayList<Course> courses) {
        super(name, family, points_for_topics, courses);
    }

    public Student(String name, String family){
        super(name, family);
        this.name = name;
        this.family = family;
    }

    public void addCourse(Course course){
        this.getCourses().add(course);
    }

    void Allinfo() {
        String family = this.family;
        System.out.println("\nФамилия: " + family + "\nИмя: " + this.name + "\nБаллы: " + this.points_for_topics + "\nГород: " + this.city + "\nДата рождения: " + this.birthday + "\nПол: " + this.sex + "\nНикнейм: " + this.nickname + "\nСсылка на фото: " + this.photo + "\nЗадачи: " + this.getCourses().toString());
        System.out.println("***********************************************************************************");
    }
}
