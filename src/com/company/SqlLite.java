package com.company;

import java.sql.*;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.Period;
import java.util.Collections;
import java.util.HashMap;
import java.util.TreeMap;

public class SqlLite {
    public static Connection conn;
    public static Statement statmt;
    public static Statement statmt2;
    public static Statement statmt3;
    public static Statement statmt4;
    public static ResultSet resSet;

    // --------ПОДКЛЮЧЕНИЕ К БАЗЕ ДАННЫХ--------
    public static void Conn() throws ClassNotFoundException, SQLException
    {
        conn = null;
        Class.forName("org.sqlite.JDBC");
        conn = DriverManager.getConnection("jdbc:sqlite:BD.s3db");
        statmt = conn.createStatement();

    }

    // --------Создание таблицы--------
    public static void CreateDB() throws SQLException
    {
        statmt = conn.createStatement();

        //Создаем таблицу users
        statmt.execute("CREATE TABLE if not exists 'users' ('id' INTEGER  CONSTRAINT students_pk PRIMARY KEY AUTOINCREMENT, 'name' text, 'surname' text, 'city' text" +
                ", 'bdate' DATE, 'nickname' text, 'photo' text, 'sex' text);");

        //Создаем таблицу themes
        statmt.execute("CREATE TABLE if not exists themes (id INTEGER NOT NULL constraint themes_pk primary key autoincrement, " +
                " name text not null, UNIQUE(name) );");

        //Создаем таблицу task
        statmt.execute("CREATE TABLE if not exists task ( task_id integer not null constraint task_pk primary key autoincrement, " +
                "user_id references users(id), theme_id references themes(id), score integer , task_name  TEXT);");

        System.out.println("Таблица создана или уже существует.");
    }

    // --------Заполнение таблицы--------
    public static void WriteDB(Student student) throws SQLException
    {
        try{
            //Начало транзакции
            statmt.execute("BEGIN");

            //Создаем пользователя
            statmt.execute("INSERT INTO 'users' ('name', 'surname', 'city', 'bdate', 'nickname', 'photo', 'sex')" +
                    " VALUES ('" + student.getName() + "', '" + student.getFamily() + "', '" + student.getCity() + "', '" + student.getStringBirthdate() + "', '" + student.getNickname() + "', '" + student.getPhoto() + "', '" + student.getSex() + "'); ");

            //Получаем id последнего добавленного студента
            int user_id = statmt.executeQuery("SELECT last_insert_rowid()\n").getInt("last_insert_rowid()");

            //Перебираем по курсу
            for (Course course: student.getCourses()) {
                //Перебираем по теме
                for (Themes theme: course.getThemes() ) {
                    try {
                        //Добавляем название тем в БД
                        statmt.execute("INSERT OR IGNORE INTO themes ('name') VALUES ('"+ theme.getName() +"');");
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }

                    //Получаем id Темы, по его названию
                    resSet = statmt.executeQuery("SELECT id FROM themes WHERE name='"+ theme.getName() +"';");
                    resSet.next();
                    int themeId = resSet.getInt("id");

                    //Заполняем балы
                    for (Task task: theme.getTasks()) {
                        statmt.execute("INSERT INTO task ('user_id', 'theme_id', 'score', 'task_name') VALUES ("+ user_id +", "+ themeId +", "+ task.getScore() +", '"+ task.getName() +"');");
                    }
                }

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            //Конец транзакции
            statmt.execute("END");
        }
    }

    // -------- Вывод таблицы--------
    public static void ReadDB() throws SQLException
    {
        resSet = statmt.executeQuery("SELECT * FROM users");

        while(resSet.next())
        {
            int id = resSet.getInt("id");
            String  name = resSet.getString("name");
            String  surname = resSet.getString("surname");
            String  city = resSet.getString("city");
            String  bdate = resSet.getString("bdate");
            String  nickname = resSet.getString("nickname");
            String  photo = resSet.getString("photo");
            String  sex = resSet.getString("sex");
            /*System.out.println( "ID = " + id );
            System.out.println( "name = " + name );
            System.out.println( "surname = " + surname );
            System.out.println( "city = " + city );
            System.out.println( "bdate = " + bdate );
            System.out.println( "nickname = " + nickname );
            System.out.println( "photo = " + photo );
            System.out.println( "sex = " + sex );
            System.out.println();*/
        }

        //System.out.println("Таблица выведена");
    }

    //Получаем статистику студента
    public static TreeMap<String, HashMap<String, int[]>> getStudentStats() throws ClassNotFoundException, SQLException
    {
        var result = new TreeMap<String, HashMap<String, int[]>>(Collections.reverseOrder());
        try{
            //Подключение к бд
            Conn();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        statmt = conn.createStatement();
        statmt2 = conn.createStatement();
        statmt3 = conn.createStatement();
        statmt4 = conn.createStatement();
        var studTable = statmt.executeQuery("SELECT * FROM users ORDER BY surname;");

        //Перебираем студента
        while(studTable.next())
        {
            int id = studTable.getInt("id");
            String stName = studTable.getString("name");
            String  stSurname = studTable.getString("surname");
            var themes = statmt2.executeQuery("SELECT * FROM themes");
            var themeMap = new HashMap<String, int[]>();
            //Перебираем задачи и тесты студента
            while (themes.next()){
                var theme_id = themes.getInt("id");
                //Запрос к БД для получения количество тестов студента
                var tests = statmt3.executeQuery("SELECT count(*) AS total FROM task WHERE user_id="+ id +" AND theme_id="+ theme_id +" AND (task_name LIKE '%Контрольн% вопрос%') AND score>0;");
                //Запрос к БД для получения количество задач студента
                var tasks = statmt4.executeQuery("SELECT count(*) AS total FROM task WHERE user_id="+ id +" AND theme_id="+ theme_id +" AND (task_name NOT LIKE '%Контрольн% вопрос%') AND score>0;");
                //Название темы
                var theme = themes.getString("name");

                //Добавляем темы в список
                themeMap.put(theme, new int[]{tests.getInt("total"), tasks.getInt("total")});
            }
            //Добавляем данные студента в список
            result.put(stSurname + " " + stName, themeMap);
        }
        return result;
    }

    //Получаем статистику по полу студентов
    public static HashMap<String, Integer> getSexStat() throws SQLException, ClassNotFoundException {
        //Подключение к бд
        Conn();
        var dict = new HashMap<String, Integer>();
        //Делаем запрос к БД, для получения пола всех студентов
        var cities = statmt.executeQuery("SELECT sex, count(sex) as 'total' from users group by sex");
        while (cities.next()) {
            //Добавляем в список
            dict.put(cities.getString("sex"), cities.getInt("total"));
        }

        return dict;
    }

    //Получаем статистику по странам студентов
    public static HashMap<String, Integer> GetCityStat() throws SQLException, ClassNotFoundException {
        //Подключение к бд
        Conn();
        var dict = new HashMap<String, Integer>();
        //Делаем запрос к БД для получения стран студентов
        var cities = statmt.executeQuery("SELECT city, count(city) as 'total' from users group by city");
        while (cities.next()) {
            //Добавляем в список
            dict.put(cities.getString("city"), cities.getInt("total"));
        }

        return dict;
    }

    //Получаем статистику по возросту
    public static HashMap<String, Integer> getAgeStat() throws SQLException, ClassNotFoundException, ParseException {
        try {
            //Подключение к бд
            Conn();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        var dict = new HashMap<String, Integer>();
        try {
            //Делаем запрос к БД для получения дату рождений студентов
            var dates = statmt.executeQuery("SELECT bdate FROM users WHERE bdate is not 'null';");
            while (dates.next()) {
                //Получаем текущую дату
                var today = LocalDate.now();
                //Переводим в массив через разделитель '-'
                var dateInArray = dates.getString("bdate").split("-");
                //Переобразуем в формат LocalDate
                var birthdate = LocalDate.of(Integer.parseInt(dateInArray[0]), Integer.parseInt(dateInArray[1]) + 1, Integer.parseInt(dateInArray[2]));
                //Получаем текущий возраст студента
                var age = Integer.toString(Period.between(birthdate, today).getYears());
                //Добавляем в список
                if (dict.containsKey(age))
                    dict.put(age, dict.get(age)+1);
                else dict.put(age, 1);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        dict.put("Не указан", dict.get("121"));
        return dict;
    }


    // --------Закрытие--------
    public static void CloseDB() throws SQLException
    {
        conn.close();
        statmt.close();
        resSet.close();

        //System.out.println("Соединения закрыты");
    }

}

