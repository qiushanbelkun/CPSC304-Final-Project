package ca.ubc.cs304.model.Models;

import java.sql.Time;
import java.sql.Date;

public class RentModel {
    private String rid;
    private String vid;
    private String cellphone;
    private String fromDate;
    private String toDate;
    /*    private Time fromTime;
        private Time toTime;*/
    private Integer odometer;
    private String cardName;
    private String cardNumber;
    private String  expDate;
    private String confNo;

    public RentModel (String rid, String vid, String cellphone, String fromDate, String toDate,
                      Integer odometer, String cardName, String cardNumber, String expDate, String confNo){
        this.cellphone = cellphone;
        this.vid = vid;
        this.rid = rid;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.odometer = odometer;
        this.cardName = cardName;
        this.cardNumber = cardNumber;
        this.expDate = expDate;
        this.confNo = confNo;
    }

    public String getCellphone() {
        return cellphone;
    }

    public String getRid() {
        return rid;
    }

    public String getVid() {
        return vid;
    }

    public String getConfNo() {
        return confNo;
    }

    public String getExpDate() {
        return expDate;
    }

    public String getFromDate() {
        return fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getCardName() {
        return cardName;
    }

    public Integer getOdometer() {
        return odometer;
    }

    public String RenttoString(){
        String rids = getRid();
        String vids = getVid();
        String cellphones = getCellphone();
        String fromDates = getFromDate().toString();
        String toDates = getToDate().toString();
        String odometers = getOdometer().toString();
        String cardNumbers = getCardNumber();
        String expDates = getExpDate().toString();
        String confNos = getConfNo();
        String result = "RentID: " + rids + "; VehcleID: " + vids + "; CellPhone#: " +cellphones+ "; FromDate: " +fromDates +"; ToDate: " + toDates+
                "; Odometer: " + odometers + "; CardNumber: " + cardNumbers+ "; ExpDate: " + expDates+ "; CnfirmationNo: "+confNos;
        return result;
    }
//    public Time getFromTime() {
//        return fromTime;
//    }
//
//    public Time getToTime() {
//        return toTime;
//    }
}