package com.example.sentiment.entities;

import java.util.List;

public class Statistics {

    // Fields
    private List<StatisticsTestObject> objectList;

    // Constructors
    public Statistics(List<StatisticsTestObject> objectList) {
        this.objectList = objectList;
    }

//     Getters & Setters
    public List<StatisticsTestObject> getObjectList() {
        return objectList;
    }

    // Perhaps no need for keyword as parameter? Does the object list only contain objects with same keyword?
    public double getAverageByKeyWord(String keyword, List<StatisticsTestObject> objectList) {

        double average;
        double totalScore = 0;
        int objectCount = objectList.size();

        for (StatisticsTestObject object : objectList) {
            totalScore += object.getScore();
        }

        average = totalScore / objectCount;

        return average;
    }
}
