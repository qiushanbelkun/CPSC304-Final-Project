package ca.ubc.cs304.database;

import ca.ubc.cs304.model.VehicleModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static ca.ubc.cs304.database.GeneralOperations.EXCEPTION_TAG;
import static ca.ubc.cs304.database.GeneralOperations.WARNING_TAG;

public class VehicleOperations{
    Connection connection;

    public VehicleOperations(Connection connection) {
        this.connection = connection;
    }

//    // idk whether this method will be used in the future
//    @Override
//    public List<VehicleModel> getAll() throws SQLException {
//        return null;
//}
    //list all the selected vehicles
    public List<VehicleModel> selectQualifiedVehicles(String type, String city, String location, String fromDate, String toDate) {
        List<VehicleModel> result = new ArrayList<>();
        /*String sub1 = "SELECT vid FROM rent WHERE Rent.vid NOT IN ( SELECT Rent.cid FROM Rent WHERE Rent.fromDate >= ? AND Rent.fromTime >= ?)" +
                " and Rent.cid NOT IN (SELECT Rent.vid FROM Rent WHERE Rent.toTime <= ? AND Rent.toTime <= ?)";
        String cmd = "SELECT * FROM Vehicle WHERE location = ? AND city = ? And status AND" +
                "vid NOT IN ( " + sub1 + ")";*/
        try {
            // all  0
            if (type!=null && city!=null && location!=null && fromDate!=null && toDate!=null){
//                PreparedStatement ps = connection.prepareStatement("SELECT * FROM Vehicle WHERE location = ? AND city = ? " +
//                        "AND status=1 AND vtname = ? AND vid NOT IN ( SELECT vid FROM rent WHERE Rent.vid NOT IN " +
//                        "( SELECT Rent.cid FROM Rent WHERE Rent.fromDate >= ?) " +
//                        "AND Rent.cid NOT IN (SELECT Rent.vid FROM Rent WHERE Rent.toDate <= ? AND))");
//                ps.setString(1, location);
//                ps.setString(2, city);
//                ps.setString(3, type);
//                ps.setString(4, fromDate);
//                ps.setString(5, toDate);
                if(connection==null){
                    System.out.println("null connection");
                }
                Statement ps = connection.createStatement();
                ResultSet tempResult = ps.executeQuery("SELECT * FROM VEHICLE WHERE VID = 1");
                while (tempResult.next()) {
                    Integer vid = tempResult.getInt("vid");
                    String vlicense = tempResult.getString("vlicense");
                    String make = tempResult.getString("make");
                    String model = tempResult.getString("model");
                    Integer year = tempResult.getInt("year");
                    String color = tempResult.getString("color");
                    Integer odometer = tempResult.getInt("odometer");
                    Integer status = tempResult.getInt("status");
                    String vtname = tempResult.getString("vtname");
                    // assume that the location in the sql result is the same with those in the parameters
                    String locationInResult = tempResult.getString("location");
                    String cityInResult = tempResult.getString("city");
                    if (!location.equals(locationInResult) || !city.equals(cityInResult)) {
                        throw new SQLException("ERROR: in selection, the selection failed, did NOT get desired");
                    }
                    VehicleModel eachVehicle = new VehicleModel(vid, vlicense, make, model, year, color, odometer, status, vtname, locationInResult, cityInResult);
                    result.add(eachVehicle);
                }
                ps.close();}
            else if
                // branch and time 1
            (type==null && city!=null && location!=null && fromDate!=null && toDate!=null){
                PreparedStatement ps1 = connection.prepareStatement("SELECT * FROM Vehicle WHERE location = ? AND city = ? " +
                        "AND status=1 AND vid NOT IN ( SELECT vid FROM rent WHERE Rent.vid NOT IN " +
                        "( SELECT Rent.cid FROM Rent WHERE Rent.fromDate >= ?) " +
                        "AND Rent.cid NOT IN (SELECT Rent.vid FROM Rent WHERE Rent.toDate <= ?))");
                ps1.setString(1, location);
                ps1.setString(2, city);
                ps1.setString(3, fromDate);
                ps1.setString(4, toDate);
                ResultSet tempResult = ps1.executeQuery();
                while (tempResult.next()) {
                    Integer vid = tempResult.getInt("vid");
                    String vlicense = tempResult.getString("vlicense");
                    String make = tempResult.getString("make");
                    String model = tempResult.getString("model");
                    Integer year = tempResult.getInt("year");
                    String color = tempResult.getString("color");
                    Integer odometer = tempResult.getInt("odometer");
                    Integer status = tempResult.getInt("status");
                    String vtname = tempResult.getString("vtname");
                    // assume that the location in the sql result is the same with those in the parameters
                    String locationInResult = tempResult.getString("location");
                    String cityInResult = tempResult.getString("city");
                    if (!location.equals(locationInResult) || !city.equals(cityInResult)) {
                        throw new SQLException("ERROR: in selection, the selection failed, did NOT get desired");
                    }
                    VehicleModel eachVehicle = new VehicleModel(vid, vlicense, make, model, year, color, odometer, status, vtname, location, city);
                    result.add(eachVehicle);
                }
                ps1.close();
            }
            //type and time 2
            else if (type!=null && city==null && location==null && fromDate!=null && toDate!=null ){
                PreparedStatement ps2 = connection.prepareStatement("SELECT * FROM Vehicle WHERE vtname =? AND status=1 AND vid NOT IN ( SELECT vid FROM rent WHERE Rent.vid NOT IN " +
                        "( SELECT Rent.cid FROM Rent WHERE Rent.fromDate >= ? ) " +
                        "AND Rent.cid NOT IN (SELECT Rent.vid FROM Rent WHERE Rent.toDate <= ? ))");
                ps2.setString(1,type);
                ps2.setString(2, fromDate);
                ps2.setString(3, toDate);
                ResultSet tempResult = ps2.executeQuery();
                while (tempResult.next()) {
                    Integer vid = tempResult.getInt("vid");
                    String vlicense = tempResult.getString("vlicense");
                    String make = tempResult.getString("make");
                    String model = tempResult.getString("model");
                    Integer year = tempResult.getInt("year");
                    String color = tempResult.getString("color");
                    Integer odometer = tempResult.getInt("odometer");
                    Integer  status = tempResult.getInt("status");
                    String vtname = tempResult.getString("vtname");
                    // assume that the location in the sql result is the same with those in the parameters
                    String locationInResult = tempResult.getString("location");
                    String cityInResult = tempResult.getString("city");
                    if (!location.equals(locationInResult) || !city.equals(cityInResult)) {
                        throw new SQLException("ERROR: in selection, the selection failed, did NOT get desired");
                    }
                    VehicleModel eachVehicle = new VehicleModel(vid, vlicense, make, model, year, color, odometer, status, vtname, location, city);
                    result.add(eachVehicle);
                }
                ps2.close();
            }
            // branch and time 3
            else if(type==null && city!=null && location!=null && fromDate!=null  && toDate!=null ){
                PreparedStatement ps3 = connection.prepareStatement("SELECT * FROM Vehicle WHERE location = ? AND city = ? " +
                        "AND status=1 AND  vid NOT IN ( SELECT vid FROM rent WHERE Rent.vid NOT IN " +
                        "( SELECT Rent.cid FROM Rent WHERE Rent.fromDate >= ?) " +
                        "AND Rent.cid NOT IN (SELECT Rent.vid FROM Rent WHERE Rent.toDate <= ? ))");
                ps3.setString(1, location);
                ps3.setString(2, city);
                ps3.setString(3, fromDate);
                ps3.setString(4, toDate);
                ResultSet tempResult = ps3.executeQuery();
                while (tempResult.next()) {
                    Integer vid = tempResult.getInt("vid");
                    String vlicense = tempResult.getString("vlicense");
                    String make = tempResult.getString("make");
                    String model = tempResult.getString("model");
                    Integer year = tempResult.getInt("year");
                    String color = tempResult.getString("color");
                    Integer odometer = tempResult.getInt("odometer");
                    Integer  status = tempResult.getInt("status");
                    String vtname = tempResult.getString("vtname");
                    // assume that the location in the sql result is the same with those in the parameters
                    String locationInResult = tempResult.getString("location");
                    String cityInResult = tempResult.getString("city");
                    if (!location.equals(locationInResult) || !city.equals(cityInResult)) {
                        throw new SQLException("ERROR: in selection, the selection failed, did NOT get desired");
                    }
                    VehicleModel eachVehicle = new VehicleModel(vid, vlicense, make, model, year, color, odometer, status, vtname, location, city);
                    result.add(eachVehicle);
                }
                ps3.close();
            }
            //type 4
            else if (type!=null && city==null && location==null && fromDate==null&& toDate==null){
                PreparedStatement ps4 = connection.prepareStatement("SELECT * FROM Vehicle WHERE  status=1 AND vtname = ? ");
                ps4.setString(1, type);
                ResultSet tempResult = ps4.executeQuery();
                while (tempResult.next()) {
                    Integer vid = tempResult.getInt("vid");
                    String vlicense = tempResult.getString("vlicense");
                    String make = tempResult.getString("make");
                    String model = tempResult.getString("model");
                    Integer year = tempResult.getInt("year");
                    String color = tempResult.getString("color");
                    Integer odometer = tempResult.getInt("odometer");
                    Integer  status = tempResult.getInt("status");
                    String vtname = tempResult.getString("vtname");
                    // assume that the location in the sql result is the same with those in the parameters
                    String locationInResult = tempResult.getString("location");
                    String cityInResult = tempResult.getString("city");
                    if (!location.equals(locationInResult) || !city.equals(cityInResult)) {
                        throw new SQLException("ERROR: in selection, the selection failed, did NOT get desired");
                    }
                    VehicleModel eachVehicle = new VehicleModel(vid, vlicense, make, model, year, color, odometer, status, vtname, location, city);
                    result.add(eachVehicle);
                }
                ps4.close();}
            //branch 5
            else if(type==null && city!=null && location!=null && fromDate==null  && toDate==null){
                PreparedStatement ps5 = connection.prepareStatement("SELECT * FROM Vehicle WHERE location = ? AND city = ? " +
                        "AND status=1 ");
                ps5.setString(1, location);
                ps5.setString(2, city);
                ResultSet tempResult = ps5.executeQuery();
                while (tempResult.next()) {
                    Integer vid = tempResult.getInt("vid");
                    String vlicense = tempResult.getString("vlicense");
                    String make = tempResult.getString("make");
                    String model = tempResult.getString("model");
                    Integer year = tempResult.getInt("year");
                    String color = tempResult.getString("color");
                    Integer odometer = tempResult.getInt("odometer");
                    Integer  status = tempResult.getInt("status");
                    String vtname = tempResult.getString("vtname");
                    // assume that the location in the sql result is the same with those in the parameters
                    String locationInResult = tempResult.getString("location");
                    String cityInResult = tempResult.getString("city");
                    if (!location.equals(locationInResult) || !city.equals(cityInResult)) {
                        throw new SQLException("ERROR: in selection, the selection failed, did NOT get desired");
                    }
                    VehicleModel eachVehicle = new VehicleModel(vid, vlicense, make, model, year, color, odometer, status, vtname, location, city);
                    result.add(eachVehicle);
                }
                ps5.close();}
            //time 6
            else if (type==null && city==null && location==null && fromDate!=null && toDate!=null){
                PreparedStatement ps6 = connection.prepareStatement("SELECT * FROM Vehicle WHERE status=1  AND vid NOT IN ( SELECT vid FROM rent WHERE Rent.vid NOT IN " +
                        "( SELECT Rent.cid FROM Rent WHERE Rent.fromDate >= ? ) " +
                        "AND Rent.cid NOT IN (SELECT Rent.vid FROM Rent WHERE Rent.toDate <= ? ))");
                ps6.setString(1, fromDate);
                ps6.setString(2, toDate);
                ResultSet tempResult = ps6.executeQuery();
                while (tempResult.next()) {
                    Integer vid = tempResult.getInt("vid");
                    String vlicense = tempResult.getString("vlicense");
                    String make = tempResult.getString("make");
                    String model = tempResult.getString("model");
                    Integer year = tempResult.getInt("year");
                    String color = tempResult.getString("color");
                    Integer odometer = tempResult.getInt("odometer");
                    Integer  status = tempResult.getInt("status");
                    String vtname = tempResult.getString("vtname");
                    // assume that the location in the sql result is the same with those in the parameters
                    String locationInResult = tempResult.getString("location");
                    String cityInResult = tempResult.getString("city");
                    if (!location.equals(locationInResult) || !city.equals(cityInResult)) {
                        throw new SQLException("ERROR: in selection, the selection failed, did NOT get desired");
                    }
                    VehicleModel eachVehicle = new VehicleModel(vid, vlicense, make, model, year, color, odometer, status, vtname, location, city);
                    result.add(eachVehicle);
                }
                ps6.close();}
        }
        catch ( SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
        return result;
    }

    private void rollbackConnection() {
    }

    //counting available vehicles
    public int countqulifiedvehicle (String type, String city, String location, String fromDate, String toDate){
        List<VehicleModel> templist = selectQualifiedVehicles(type, city, location, fromDate, toDate);
        return templist.size();
    }


    public void UpdateVehicleStatus(boolean input, String vid) {
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE Vehicle set status = ? WHERE vid=?");
            ps.setBoolean(1, input);
            ps.setString(2, vid);
            int rowCount = ps.executeUpdate();
            if (rowCount == 0) {
                System.out.println(WARNING_TAG + "wrong update");
            }
            connection.commit();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }
}

