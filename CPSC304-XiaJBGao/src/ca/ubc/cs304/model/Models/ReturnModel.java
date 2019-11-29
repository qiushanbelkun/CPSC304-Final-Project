package ca.ubc.cs304.model.Models;

public class ReturnModel {
    private String rid;
    private String date;
    private Integer odometer;
    private Integer fulltank;
    private Integer value;

    public ReturnModel (String rid, String date, Integer odometer, Integer fulltank, Integer value){
        this.date = date;
        this.rid = rid;
        this.fulltank = fulltank;
        this.odometer = odometer;
        this.value = value;
    }

    public Integer getOdometer() {
        return odometer;
    }

    public String getRid() {
        return rid;
    }

    public String getDate() {
        return date;
    }

    public Integer getValue() {
        return value;
    }
    public Integer getFullTank (){
        return fulltank;
    }
    public String RenturntoString (){
        String fk = "";
        if(fulltank == 1){
            fk = "Yes";
        }
        else if(fulltank ==0){
            fk = "No";
        }
        return "RentID:" + this.rid+ "; Return-Date:"+ this.getDate() +"; Odometer:" + this.odometer+"; Fulltank:" + fk + "; Value:"+ this.value;
    }
}