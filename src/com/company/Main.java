package com.company;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.sql.SQLException;
import java.util.*;

public class Main {

    public static void main(String[] args) throws Exception {
        SqlLite.Conn();
        SqlLite.CreateDB();
        ArrayList<Student> allStudent;
        String filename = "CSV.csv";
        allStudent = Parser.ParseCSVFile(filename);
        parseVk(allStudent);
        new Form();
        SqlLite.ReadDB();
        SqlLite.CloseDB();
    }


    public static void parseVk(ArrayList<Student> allStudent) throws SQLException {
        int offset = 0;
        int counter = 1000;

        for(int i  = 0; i < 2; i++) {
            String access_token = "4a284131dd99a556b61099aaaf3c3cbc6802b826757b65c6ffb771ddb8b9a6b462985bcc68339fbc117b6";
            String API_URL = "https://api.vk.com/method/groups.getMembers?group_id=198188261&offset=" + offset + "&fields=city,bdate,nickname,photo_max,sex&access_token=" + access_token + "&v=5.81";

            URL url = Url_JsonUtils.createUrl(API_URL);


            String resultJson = Url_JsonUtils.parseUrl(url);
            //System.out.println("\nПолученный JSON:\n" + resultJson);


            List<JSONObject> s = Url_JsonUtils.parseCurrentAPIJson(resultJson);
            
            for (JSONObject jsonObject : s) {
                for(Student student: allStudent) {
                    if (student.getFamily().equals(jsonObject.get("last_name")) && student.getName().equals(jsonObject.get("first_name")) || student.getFamily().equals(jsonObject.get("first_name")) && student.getName().equals(jsonObject.get("last_name"))) {
                        try {
                            //Страна
                            JSONObject city = (JSONObject)(s.get(0)).get("city");
                            student.setCity((String) city.get("title"));
                        } catch (JSONException e) {

                        }
                        try {
                            //Дата рождения
                            Calendar bdate = null;
                            if (jsonObject.get("bdate") != null) {
                                String str = (String) jsonObject.get("bdate");
                                String[] parsedDate = str.split("\\.");
                                var date = new ArrayList<Integer>();
                                Arrays.stream(parsedDate).forEach(b -> date.add(Integer.parseInt(b)));
                                if (date.size() == 2)
                                    bdate = new GregorianCalendar(1999, date.get(1), date.get(0));
                                else bdate = new GregorianCalendar(date.get(2), date.get(1), date.get(0));
                            }
                            student.setBirthday(bdate);
                        } catch (Exception e) {
                        }
                        try {
                            //ВК-НИК
                            student.setNickname((String)jsonObject.get("nickname"));
                        } catch (Exception e) {

                        }
                        try {
                            //ВК-ФОТО
                            student.setPhoto((String)jsonObject.get("photo_max"));
                        } catch (Exception e) {

                        }
                        try {
                            //Пол
                            student.setSex((Integer)jsonObject.get("sex"));
                        } catch (JSONException e) {

                        }
                        SqlLite.WriteDB(student);
                    }
                }
            }
            offset += counter;
        }
    }
}
