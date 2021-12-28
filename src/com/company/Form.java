package com.company;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ui.RectangleInsets;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

import static java.awt.Frame.MAXIMIZED_BOTH;

public class Form {
    private ChartPanel panel;

    private ArrayList<JFreeChart> charts;
    private JFrame frame;
    private int chartNum;
    private JPanel buttonPanel;

    public enum Buttons {
        NEXT,
        PREVIEW
    }

    //Собираем окно
    public Form() throws SQLException, ClassNotFoundException, ParseException {
        charts = createChartsArray();
        createButtonsPanel();
        createPanel(charts.get(0));
        createAndShowGUI();
    }

    //Создаем график
    public void createAndShowGUI() throws SQLException, ClassNotFoundException {
        frame = new JFrame("Статистика");
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.setBackground(Color.GRAY);
        frame.add(panel);
        frame.setExtendedState(MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    //Панель для кнопок
    private void createButtonsPanel() {
        var next = new JButton("Далее");
        next.setForeground(Color.white);
        next.setBackground(Color.darkGray);
        next.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changePanelChart(Buttons.NEXT);
            }
        });
        var previous = new JButton("Назад");
        previous.setForeground(Color.white);
        previous.setBackground(Color.darkGray);
        previous.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changePanelChart(Buttons.PREVIEW);
            }
        });
        buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.GRAY);
        buttonPanel.setLayout(new BorderLayout());
        buttonPanel.add(next, BorderLayout.WEST);
        buttonPanel.add(previous, BorderLayout.EAST);
    }

    //Панель
    private void createPanel(JFreeChart chart){
        panel = new ChartPanel(chart);
        buttonPanel.setPreferredSize(new Dimension(400, 70));
        panel.setFillZoomRectangle(true);
        panel.setMouseWheelEnabled(true);
        panel.setVisible(true);
    }

    //Реакция кнопок
    private void changePanelChart(Buttons action) {
        if (action == Buttons.NEXT)
            chartNum = chartNum < charts.size()-1 ? chartNum + 1 : 0;
        else chartNum = chartNum == 0 ? charts.size() - 1 : chartNum - 1;
        var chart = charts.get(chartNum);
        chart.setPadding(new RectangleInsets(4, 8, 2, 2));
        panel.setChart(chart);
    }

    //Вывод статистики
    private ArrayList<JFreeChart> createChartsArray() throws SQLException, ClassNotFoundException, ParseException {
        var charts = new ArrayList<JFreeChart>();
        charts.add(Dataset.createPieChart(SqlLite.GetCityStat(), "Статистика по странам"));
        charts.add(Dataset.createPieChart(SqlLite.getAgeStat(), "Статистика по возрасту"));
        charts.add(Dataset.createPieChart(SqlLite.getSexStat(), "Статистика по полу"));

        charts.addAll(Dataset.createBarCharts(SqlLite.getStudentStats()));

        return charts;
    }
}
