package ca.ubc.cs304.model.Models;

import java.sql.Date;
import java.sql.Time;

public class ReservationModel {
    private String confNo;
    private String vtname;
    private String cellphone;
    private String fromDate;
    private String toDate;



    public ReservationModel(String confNo, String vtname, String cellphone, String fromDate, String toDate) {
        this.confNo = confNo;
        this.vtname = vtname;
        this.cellphone = cellphone;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public String getConfNo(){
        return confNo;
    }
    public String getVtname(){
        return vtname;
    }
    public String getCellphone(){return cellphone;}
    public String getFromDate(){return fromDate;}
    public String getToDate(){return toDate;}
    public String ReservationtoString (){
        String confNo = this.getConfNo();
        String vtname = this.getVtname();
        String cellphone = this.getCellphone();
        String fromdate = this.getFromDate();
        String todate = this.getToDate();
        String result = "Confirmation No:" + confNo +"; Vehicle Type:" + vtname +"; Cellphone: "+ cellphone+ "; Date "+ fromdate
        +" to " +todate;
        return result;
    }
}
