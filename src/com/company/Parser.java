package com.company;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Parser {

    //Главный Парсер
    public static ArrayList<Student> ParseCSVFile(String filepath) throws IOException {
        List<String> fileLines = Files.readAllLines(Paths.get(filepath));
        ArrayList<Themes> themes;
        ArrayList<Student> students = new ArrayList<>(fileLines.size()-1);
        int lineIndex = 0;
        for (String line : fileLines) {
            themes = parseThemesRow(fileLines.get(0), fileLines.get(1));
            if (lineIndex > 2){
                ArrayList<String> parsedRow = parseRowToArray(line);
                String[] studentNames = parsedRow.get(0).split(" ");
                Student student = new Student(studentNames[1], studentNames[0]);
                String groupName = parsedRow.get(1);

                parsedRow.remove(0);
                parsedRow.remove(1);

                var scores = new ArrayList<Integer>();
                parsedRow.forEach(s -> {if (isNumeric(s)) scores.add(Integer.parseInt(s));});
                addScoresToThemes(themes, scores);
                student.addCourse(new Course("Java", groupName, themes));
                students.add(student);
            }
            lineIndex++;
        }

        return students;
    }

    public static boolean isNumeric(String str) {
        //Является ли номером
        return str.matches("-?\\d+(\\.\\d+)?");
    }

    public static ArrayList<String> parseRowToArray(String str) {
        return new ArrayList<>(Arrays.asList(str.split(";")));
    }

    //Парсим Темы
    public static ArrayList<Themes> parseThemesRow(String str, String tasksRow) {
        var list = new ArrayList<Themes>();
        var splitted = str.split(";", -1);
        var splittedTasks = tasksRow.split(";", -1);
        String themeName = "";
        var scoresCount = 0;
        var taskNames = new ArrayList<String>();
        for (var i = 3; i < splitted.length; i++) {
            if (!splitted[i].equals("") || i == splitted.length - 1) {
                if (!(themeName.equals(""))) {
                    Themes theme = new Themes(themeName, taskNames.toArray(new String[0]), scoresCount);
                    list.add(theme);
                }

                themeName = splitted[i];
                scoresCount = 0;
                taskNames = new ArrayList<>();
            }
            else {
                scoresCount++;
                taskNames.add(splittedTasks[i]);
            }
        }

        return list;
    }

    //Парсим Баллы
    public static void addScoresToThemes(List<Themes> themes, List<Integer> scores) {
        var indexThemes = 0;
        for (var theme : themes) {
            theme.setTotalScore(scores.get(indexThemes));
            indexThemes++;
            theme.setScoresTasks(scores.subList(indexThemes, indexThemes + theme.getCountScores()).toArray(new Integer[0]));
            indexThemes += theme.getCountScores();
        }
    }
}
