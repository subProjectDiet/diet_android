package com.cookandroid.subdietapp.model.eda;

public class EdaWeek {

//
//                "userId": 124,
//                        "AvgWeight": 52.2,
//                        "AvgKcal": 416.9,
//                        "AvgKcalBurn": 167.0,
//                        "start": "2023-04-02",
//                        "end": "2023-04-08"

    private int userId;
    private Double AvgWeight;
    private Double AvgKcal;
    private Double AvgKcalBurn;
    private String start;
    private String end;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Double getAvgWeight() {
        return AvgWeight;
    }

    public void setAvgWeight(Double avgWeight) {
        AvgWeight = avgWeight;
    }

    public Double getAvgKcal() {
        return AvgKcal;
    }

    public void setAvgKcal(Double avgKcal) {
        AvgKcal = avgKcal;
    }

    public Double getAvgKcalBurn() {
        return AvgKcalBurn;
    }

    public void setAvgKcalBurn(Double avgKcalBurn) {
        AvgKcalBurn = avgKcalBurn;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }
}
