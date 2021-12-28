package com.company;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

public class Dataset {

    private static PieDataset<String> createPieDataset(HashMap<String, Integer> statistic) {
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        for (var data : statistic.entrySet())
            dataset.setValue(data.getKey(), data.getValue());
        return dataset;
    }

    //Категория
    private static CategoryDataset createCategoryDataset(HashMap<String, int[]> statistic) {
        var dataset = new DefaultCategoryDataset();
        for (var data : statistic.entrySet()) {
            dataset.addValue(data.getValue()[0], "Тесты", data.getKey().split("\\.")[0]);
            dataset.addValue(data.getValue()[1], "Задачи", data.getKey().split("\\.")[0]);
        }
        return dataset;
    }


    //Круглая диаграмма
    public static JFreeChart createPieChart(HashMap<String, Integer> statistic, String name) {
        PieDataset<String> dataset = createPieDataset(statistic);
        var chart = ChartFactory.createPieChart(name, dataset, false, true, false);
        chart.setBackgroundPaint(Color.WHITE);

        PiePlot plot = (PiePlot) chart.getPlot();
        chart.setBackgroundPaint(null);
        plot.setInteriorGap(0.04);
        plot.setOutlineVisible(false);
        plot.setExplodePercent(statistic.entrySet().iterator().next().getKey(), 0.01);

        plot.setLabelFont(new Font("Aharoni", Font.BOLD, 14));
        plot.setLabelLinkPaint(Color.red);
        plot.setLabelLinkStroke(new BasicStroke((2.0f)));
        plot.setLabelOutlineStroke(null);
        plot.setLabelPaint(Color.white);
        plot.setLabelBackgroundPaint(null);

        return chart;
    }

    //Столбичная диаграмма
    private static JFreeChart createBarChart(HashMap<String, int[]> data, String name) {
        var barChart = ChartFactory.createBarChart("Статистика по студенту: " + name, "Темы", "Балы", createCategoryDataset(data), PlotOrientation.VERTICAL, true, true, false);
        return barChart;
    }

    public static ArrayList<JFreeChart> createBarCharts(TreeMap<String, HashMap<String, int[]>> statistic) {
        var charts = new ArrayList<JFreeChart>();
        for (var stud : statistic.entrySet())
            charts.add(createBarChart(stud.getValue(), stud.getKey()));
        return charts;
    }

}
