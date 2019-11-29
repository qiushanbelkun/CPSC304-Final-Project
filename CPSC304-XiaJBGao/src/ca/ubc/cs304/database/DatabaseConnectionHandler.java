package ca.ubc.cs304.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ca.ubc.cs304.model.BranchModel;
import ca.ubc.cs304.model.Models.CustomerModel;
import ca.ubc.cs304.model.Models.RentModel;
import ca.ubc.cs304.model.Models.ReservationModel;
import ca.ubc.cs304.model.Models.ReturnModel;
import ca.ubc.cs304.model.VehicleModel;
import oracle.jdbc.proxy.annotation.Pre;
import org.omg.CORBA.FREE_MEM;


import javax.swing.plaf.nimbus.State;

import static ca.ubc.cs304.database.GeneralOperations.EXCEPTION_TAG;

/**
 * This class handles all database related transactions
 */
public class DatabaseConnectionHandler {
	protected static final String ORACLE_URL = "jdbc:oracle:thin:@localhost:1522:stu";
	protected static final String EXCEPTION_TAG = "[EXCEPTION]";
	protected static final String WARNING_TAG = "[WARNING]";
	protected Connection connection = null;


	public String insertAReservation (String vtname, String cellphone, String fromDate, String toDate) throws SQLException {
		String receipt = null;
		try {
			Random ran =  new Random();
			Integer newconfNo = ran.nextInt();
			if(newconfNo<0){
				newconfNo = 0-newconfNo;
			}
			String newconfNos = newconfNo.toString();
			PreparedStatement tocheckConfNounique = connection.prepareStatement("SELECT * from Reservation WHERE confNo =?");
			tocheckConfNounique.setString(1,newconfNos);
			ResultSet lsr = tocheckConfNounique.executeQuery();
			System.out.println(44);
			List<ReservationModel> fadg = toListReservation(lsr);
			while (!fadg.isEmpty()){
				newconfNo = ran.nextInt();
				if(newconfNo<0){
					newconfNo = 0-newconfNo;
				}
				newconfNos = newconfNo.toString();
				tocheckConfNounique.setString(1,newconfNos);
				lsr = tocheckConfNounique.executeQuery();
				fadg = toListReservation((lsr));
			}
			PreparedStatement cphonenum = connection.prepareStatement("SELECT * FROM Customer Where cellphone = ?");
			cphonenum.setString(1,cellphone);
			List<CustomerModel> lc = toListCustomer(cphonenum.executeQuery());
			if(!lc.isEmpty()){
				System.out.println("You are old customer!");
				String sqlstring = "";
				if (vtname!=null){
					sqlstring = "SELECT * FROM VEHICLE WHERE VTNAME = ? AND STATUS = 1";
				}else {
					sqlstring = "SELECT * FROM VEHICLE WHERE STATUS = 1";
				}
				PreparedStatement togetcar = connection.prepareStatement(sqlstring);
				if(vtname!= null) {
					togetcar.setString(1, vtname);
				}
				ResultSet getcars = togetcar.executeQuery();
				List<VehicleModel> cars = toListVehicleModel(getcars);
				System.out.println(63);
				VehicleModel thecar = cars.get(0);
				PreparedStatement ps = connection.prepareStatement(
						"INSERT INTO  Reservation(confNo, vtname, cellphone, fromDate,  toDate ) VALUES (?,?,?,TO_DATE(?, 'DD-MM-YYYY'),TO_DATE(?, 'DD-MM-YYYY'))");
				ps.setString(1, newconfNos);
				ps.setString(2, thecar.getVtname());
				ps.setString(3, cellphone);
				ps.setString(4, fromDate);
				ps.setString(5, toDate);
				System.out.println(71);
				ps.executeQuery();
				connection.commit();
				int rowCount = ps.executeUpdate();
				if (rowCount == 0) {
					System.out.println(WARNING_TAG + "Wrong Insertion");
				}
				connection.commit();
				//to produce receipt
				receipt = "ConfirmationNo:" + newconfNos.toString() +"; From date: "+ fromDate+" to Date: "+toDate;
				System.out.println(receipt);
				ps.close();}
			else {

				PreparedStatement insertcus = connection.prepareStatement("Insert into Customer values (?,?,?,?) ");
				insertcus.setString(1,cellphone);
				insertcus.setString(2,"newname");
				insertcus.setString(3,"newaddress");
				insertcus.setString(4,"dlicense");
				System.out.println(92);
				insertcus.executeQuery();
				System.out.println(94);
				String sqlstrings = "";
				if (vtname!=null){
					sqlstrings = "SELECT * FROM VEHICLE WHERE VTNAME = ? AND STATUS = 1";
				}else {
					sqlstrings = "SELECT * FROM VEHICLE WHERE STATUS = 1";
				}
				PreparedStatement togetcar = connection.prepareStatement(sqlstrings);
				if(vtname!= null) {
					togetcar.setString(1, vtname);
				}
				ResultSet getcars = togetcar.executeQuery();
				List<VehicleModel> cars = toListVehicleModel(getcars);
				VehicleModel thecar = cars.get(0);
				PreparedStatement ps = connection.prepareStatement(
						"INSERT INTO  Reservation(confNo, vtname, cellphone, fromDate,  toDate ) VALUES (?,?,?,TO_DATE(?, 'DD-MM-YYYY'),TO_DATE(?, 'DD-MM-YYYY'))");
				ps.setString(1, newconfNos);
				ps.setString(2, thecar.getVtname());
				ps.setString(3, cellphone);
				ps.setString(4, fromDate);
				ps.setString(5, toDate);
				ps.executeQuery();
				System.out.println(106);
//				int rowCount = ps.executeUpdate();
//				if (rowCount == 0) {
//					System.out.println(WARNING_TAG + "wrong insertion");
//				}
				System.out.println("You are a new customer!");
				receipt = "ConfirmationNo:" + newconfNos.toString() +"; From date: "+ fromDate+" to Date: "+toDate;
				System.out.println(receipt);
				ps.close();
			}
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
		}
		return receipt;
	}
	private List<VehicleModel> toListVehicleModel(ResultSet rs) throws SQLException {
		List<VehicleModel> result = new ArrayList<>();
		while (rs.next()) {
			Integer vid = rs.getInt("vid");
			String name = rs.getString("vlicense");
			String addr = rs.getString("make");
			String dli = rs.getString("model");
			Integer yr = rs.getInt("year");
			String cl = rs.getString("color");
			Integer odr = rs.getInt("odometer");
			Integer star = rs.getInt("status");
			String vr = rs.getString("vtname");
			String lr = rs.getString("location");
			String crt = rs.getString("city");
			VehicleModel vcl = new VehicleModel(vid,name,addr,dli,yr,cl,odr,star,vr,lr,crt);
			result.add(vcl);
		}
		if(result.isEmpty()){
			System.out.println("No such vehicle yet!");
		}
		return result;
	}
	public DatabaseConnectionHandler() {

	}
	public int countqulifiedvehicle(String type, String city, String location, String fromDate, String toDate){
		List<VehicleModel> templist = selectQualifiedVehicles(type, city, location, fromDate, toDate);
		return templist.size();
	}
	public List<VehicleModel> selectQualifiedVehicles (String type, String city, String location, String fromDate, String toDate){
		List<VehicleModel> result = new ArrayList<>();
        /*String sub1 = "SELECT vid FROM rent WHERE Rent.vid NOT IN ( SELECT Rent.cid FROM Rent WHERE Rent.fromDate >= ? AND Rent.fromTime >= ?)" +
                " and Rent.cid NOT IN (SELECT Rent.vid FROM Rent WHERE Rent.toTime <= ? AND Rent.toTime <= ?)";
        String cmd = "SELECT * FROM Vehicle WHERE location = ? AND city = ? And status AND" +
                "vid NOT IN ( " + sub1 + ")";*/
		try {
			// all  0
			if (type == null && city == null && location == null && fromDate == null && toDate == null) {
//				PreparedStatement ps = connection.prepareStatement("SELECT * FROM Vehicle WHERE location = ? AND city = ? " +
//						"AND status=1 AND vtname = ? AND vid NOT IN ( SELECT vid FROM rent WHERE Rent.vid NOT IN " +
//						"( SELECT Rent.cid FROM Rent WHERE Rent.fromDate >= ?) " +
//						"AND Rent.cid NOT IN (SELECT Rent.vid FROM Rent WHERE Rent.toDate <= ? AND))");
				PreparedStatement ps = connection.prepareStatement("SELECT * FROM Vehicle WHERE  status=1");
//					Statement insers = connection.createStatement();
////					insers.executeUpdate("insert into vehicle values (9,'11','111',9,'red',1,1,'12','21','32')");
////					connection.commit();
//				ps.setString(4, fromDate);
//				ps.setString(5, toDate);
				if (connection == null) {
					System.out.println("null connection");
				}
				ResultSet tempResult = ps.executeQuery();
				connection.commit();
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
//						if (!location.equals(locationInResult) || !city.equals(cityInResult)) {
//							throw new SQLException("ERROR: in selection, the selection failed, did NOT get desired");
//						}
					VehicleModel eachVehicle = new VehicleModel(vid, vlicense, make, model, year, color, odometer, status, vtname, locationInResult, cityInResult);
					result.add(eachVehicle);
				}
				ps.close();
			}
			else if (type != null && city != null && location != null && fromDate != null && toDate != null) {
//				PreparedStatement ps = connection.prepareStatement("SELECT * FROM Vehicle WHERE location = ? AND city = ? " +
//						"AND status=1 AND vtname = ? AND vid NOT IN ( SELECT vid FROM rent WHERE Rent.vid NOT IN " +
//						"( SELECT Rent.cid FROM Rent WHERE Rent.fromDate >= ?) " +
//						"AND Rent.cid NOT IN (SELECT Rent.vid FROM Rent WHERE Rent.toDate <= ? AND))");
				PreparedStatement ps = connection.prepareStatement("SELECT * FROM Vehicle WHERE location = ? AND city = ? " +
						"AND status=1 AND vtname = ?");
//					Statement insers = connection.createStatement();
////					insers.executeUpdate("insert into vehicle values (9,'11','111',9,'red',1,1,'12','21','32')");
////					connection.commit();
				ps.setString(1, location);
				ps.setString(2, city);
				ps.setString(3, type);
//				ps.setString(4, fromDate);
//				ps.setString(5, toDate);
				if (connection == null) {
					System.out.println("null connection");
				}
				ResultSet tempResult = ps.executeQuery();
				connection.commit();
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
//						if (!location.equals(locationInResult) || !city.equals(cityInResult)) {
//							throw new SQLException("ERROR: in selection, the selection failed, did NOT get desired");
//						}
					VehicleModel eachVehicle = new VehicleModel(vid, vlicense, make, model, year, color, odometer, status, vtname, locationInResult, cityInResult);
					result.add(eachVehicle);
				}
				ps.close();
			} else if
				// branch and time 1
			(type == null && city != null && location != null && fromDate != null && toDate != null) {
				PreparedStatement ps1 = connection.prepareStatement("SELECT * FROM Vehicle WHERE location = ? AND city = ? " +
						"AND status=1 ");
//				AND vid NOT IN ( SELECT vid FROM rent WHERE Rent.vid NOT IN " +
//				"( SELECT Rent.vid FROM Rent WHERE Rent.fromDate >= TO_DATE(?,'DD-MM-YYYY')) " +
//						"AND Rent.vid NOT IN (SELECT Rent.vid FROM Rent WHERE Rent.toDate <= TO_DATE(?,'DD-MM-YYYY')))
				ps1.setString(1, location);
				ps1.setString(2, city);
//				ps1.setString(3, fromDate);
//				ps1.setString(4, toDate);
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
//						if (!location.equals(locationInResult) || !city.equals(cityInResult)) {
//							throw new SQLException("ERROR: in selection, the selection failed, did NOT get desired");
//						}
					VehicleModel eachVehicle = new VehicleModel(vid, vlicense, make, model, year, color, odometer, status, vtname, locationInResult, cityInResult);
					result.add(eachVehicle);
				}
				ps1.close();
			}
			//type and time 2
			else if (type != null && city == null && location == null && fromDate != null && toDate != null) {
				PreparedStatement ps2 = connection.prepareStatement("SELECT * FROM Vehicle WHERE vtname =? AND status=1 ");
//				AND vid NOT IN ( SELECT vid FROM rent WHERE Rent.vid NOT IN " +
//				"( SELECT Rent.vid FROM Rent WHERE Rent.fromDate >= TO_DATE(?,'DD-MM-YYYY') ) " +
//						"AND Rent.vid NOT IN (SELECT Rent.vid FROM Rent WHERE Rent.toDate <= TO_DATE(?,'DD-MM-YYYY') ))
				ps2.setString(1, type);
//				ps2.setString(2, fromDate);
//				ps2.setString(3, toDate);
				ResultSet tempResult = ps2.executeQuery();
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
//						if (!location.equals(locationInResult) || !city.equals(cityInResult)) {
//							throw new SQLException("ERROR: in selection, the selection failed, did NOT get desired");
//						}
					VehicleModel eachVehicle = new VehicleModel(vid, vlicense, make, model, year, color, odometer, status, vtname, locationInResult, cityInResult);
					result.add(eachVehicle);
				}
				ps2.close();
			}
			// branch and time 3
			else if (type == null && city != null && location != null && fromDate != null && toDate != null) {
				PreparedStatement ps3 = connection.prepareStatement("SELECT * FROM Vehicle WHERE location = ? AND city = ? " +
						"AND status=1 ");
//				AND  vid NOT IN ( SELECT vid FROM rent WHERE Rent.vid NOT IN " +
//				"( SELECT Rent.vid FROM Rent WHERE Rent.fromDate >= TO_DATE(?,'DD-MM-YYYY')) " +
//						"AND Rent.vid NOT IN (SELECT Rent.vid FROM Rent WHERE Rent.toDate <= TO_DATE(?,'DD-MM-YYYY') ))
				ps3.setString(1, location);
				ps3.setString(2, city);
//				ps3.setString(3, fromDate);
//				ps3.setString(4, toDate);
				ResultSet tempResult = ps3.executeQuery();
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
				ps3.close();
			}
			//type 4
//			else if (type != null && city == null && location == null && fromDate == null && toDate == null) {
//				PreparedStatement ps4 = connection.prepareStatement("SELECT * FROM Vehicle WHERE  status=1 AND vtname = ? ");
//				ps4.setString(1, type);
//				ResultSet tempResult = ps4.executeQuery();
//				while (tempResult.next()) {
//					Integer vid = tempResult.getInt("vid");
//					String vlicense = tempResult.getString("vlicense");
//					String make = tempResult.getString("make");
//					String model = tempResult.getString("model");
//					Integer year = tempResult.getInt("year");
//					String color = tempResult.getString("color");
//					Integer odometer = tempResult.getInt("odometer");
//					Integer status = tempResult.getInt("status");
//					String vtname = tempResult.getString("vtname");
//					// assume that the location in the sql result is the same with those in the parameters
//					String locationInResult = tempResult.getString("location");
//					String cityInResult = tempResult.getString("city");
//					if (!location.equals(locationInResult) || !city.equals(cityInResult)) {
//						throw new SQLException("ERROR: in selection, the selection failed, did NOT get desired");
//					}
//					VehicleModel eachVehicle = new VehicleModel(vid, vlicense, make, model, year, color, odometer, status, vtname, locationInResult, cityInResult);
//					result.add(eachVehicle);
//				}
//				ps4.close();
//			}
			//branch 5
//			else if (type == null && city != null && location != null && fromDate == null && toDate == null) {
//				PreparedStatement ps5 = connection.prepareStatement("SELECT * FROM Vehicle WHERE location = ? AND city = ? " +
//						"AND status=1 ");
//				ps5.setString(1, location);
//				ps5.setString(2, city);
//				ResultSet tempResult = ps5.executeQuery();
//				while (tempResult.next()) {
//					Integer vid = tempResult.getInt("vid");
//					String vlicense = tempResult.getString("vlicense");
//					String make = tempResult.getString("make");
//					String model = tempResult.getString("model");
//					Integer year = tempResult.getInt("year");
//					String color = tempResult.getString("color");
//					Integer odometer = tempResult.getInt("odometer");
//					Integer status = tempResult.getInt("status");
//					String vtname = tempResult.getString("vtname");
//					// assume that the location in the sql result is the same with those in the parameters
//					String locationInResult = tempResult.getString("location");
//					String cityInResult = tempResult.getString("city");
////						if (!location.equals(locationInResult) || !city.equals(cityInResult)) {
////							throw new SQLException("ERROR: in selection, the selection failed, did NOT get desired");
////						}
//					VehicleModel eachVehicle = new VehicleModel(vid, vlicense, make, model, year, color, odometer, status, vtname, location, city);
//					result.add(eachVehicle);
//				}
//				ps5.close();
//			}
			//time 6
			else if (type == null && city == null && location == null && fromDate != null && toDate != null) {
				PreparedStatement ps6 = connection.prepareStatement("SELECT * FROM Vehicle  WHERE status=1 ");
//				AND vid NOT IN ( SELECT vid FROM rent WHERE Rent.vid NOT IN " +
//				"( SELECT Rent.vid FROM Rent WHERE Rent.fromDate >= TO_DATE(?,'DD-MM-YYYY') ) " +
//						"AND Rent.vid NOT IN (SELECT Rent.vid FROM Rent WHERE Rent.toDate <= TO_DATE(?,'DD-MM-YYYY')))
//				ps6.setString(1, fromDate);
//				ps6.setString(2, toDate);
				ResultSet tempResult = ps6.executeQuery();
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
//						if (!location.equals(locationInResult) || !city.equals(cityInResult)) {
//							throw new SQLException("ERROR: in selection, the selection failed, did NOT get desired");
//						}
					VehicleModel eachVehicle = new VehicleModel(vid, vlicense, make, model, year, color, odometer, status, vtname, locationInResult, cityInResult);
					result.add(eachVehicle);
				}
				ps6.close();
			}
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
		}
		for(int i = 0; i<result.size(); i++){
			VehicleModel vm = result.get(i);
			System.out.println(vm.VehicletoString());
		}
		return result;
	}
	private List<CustomerModel> toListCustomer (ResultSet rs) throws SQLException {
		List<CustomerModel> result = new ArrayList<>();
		while (rs.next()) {
			String cp = rs.getString("cellPhone");
			String name = rs.getString("name");
			String addr = rs.getString("Address");
			String dli = rs.getString("dlicense");
			CustomerModel customerModel = new CustomerModel(cp,name,addr,dli);
			result.add(customerModel);
		}
		if(result.isEmpty()){
			System.out.println("No such customer yet!");
		}
		return result;
	}
	private List<ReservationModel> toListReservation (ResultSet rk) throws SQLException {
		List<ReservationModel> result = new ArrayList<>();
		while (rk.next()) {
			String cn = rk.getString("confNo");
			String vn = rk.getString("vtname");
			String cp = rk.getString("cellphone");
			String fd = rk.getDate("fromDate").toString();
			String td = rk.getDate("toDate").toString();
			ReservationModel reservationModel = new ReservationModel(cn,vn,cp,fd,td);
			result.add(reservationModel);
		}
		if(result.isEmpty()){
			System.out.println("No such reservation yet!");
		}
		return result;
	}
	private List<RentModel> toListRent (ResultSet rk) throws SQLException {
		List<RentModel> result = new ArrayList<>();
		while (rk.next()) {
			String rid = rk.getString("rid");
			String vid = rk.getString("vid");
			String cell = rk.getString("cellphone");
			String fdd = rk.getDate("fromDate").toString();
			String tdd = rk.getDate("toDate").toString();
			Integer odd = rk.getInt("odometer");
			String cdn = rk.getString("cardname");
			String cdnu = rk.getString("cardnumber");
			String exd = rk.getDate("expdate").toString();
			String connu = rk.getString("confno");
			RentModel ren = new RentModel(rid,vid,cell,fdd,tdd,odd,cdn,cdnu,exd,connu);
			result.add(ren);
		}
		if(result.isEmpty()){
			System.out.println("No such rent yet!");
		}
		return result;
	}
	private List<ReturnModel> toListReturn (ResultSet rm) throws SQLException{
		List<ReturnModel> result = new ArrayList<>();
		while (rm.next()) {
			String rid = rm.getString("rid");
			String rdt = rm.getDate("rdate").toString();
			Integer odd = rm.getInt("odometer");
			Integer ft = rm.getInt("fulltank");
			Integer value = rm.getInt("value");
			ReturnModel rtm = new ReturnModel(rid,rdt,odd,ft,value);
			result.add(rtm);
		}
		if(result.isEmpty()){
			System.out.println("No such return yet!");
		}
		return result;
	}
	public void findnselectv (String type, String city, String location, String fromDate, String toDate){
		selectQualifiedVehicles(type,city,location,fromDate,toDate);
	}
	protected ResultSet executeSQLCmd(String cmd) throws SQLException {
		Statement statement = this.connection.createStatement();
		return statement.executeQuery(cmd);
	}
	protected ResultSet executeSQLCmd(String cmd, List<Integer> types, List<Object> values) throws SQLException {
		PreparedStatement preparedStatement = this.connection.prepareStatement(cmd);
		setUPPs(preparedStatement, types, values);
		return preparedStatement.executeQuery();
	}
	protected void setUPPs(PreparedStatement preparedStatement, List<Integer> types, List<Object> values) throws SQLException {
		// garnette that the length can match
		if (types.size() != values.size()) {
			throw new SQLException("error in setupPS: the mismatch in length of types and values");
		}
		for (int i = 0; i < types.size(); i++) {
			int type = types.get(i);
			Object value = values.get(i);
			switch (type) {
				case Types.INTEGER:
					if (value == null) {
						preparedStatement.setNull(i+1, Types.INTEGER);
					} else {
						preparedStatement.setInt(i+1, (int) value);
					}
					break;
				case Types.CHAR:
					if (value == null ){
						preparedStatement.setNull(i+1, Types.CHAR);
					} else {
						preparedStatement.setString(i+1, (String) value);
					}
					break;
				case Types.TIME:
					if (value == null) {
						preparedStatement.setNull(i+1, Types.TIME);
					} else {
						preparedStatement.setTime(i+1, (Time) value);
					}
					break;
				case Types.DATE:
					if (value == null) {
						preparedStatement.setNull(i+1, Types.TIME);
					} else {
						preparedStatement.setDate(i+1, (Date) value);
					}
					break;
				case Types.FLOAT:
					if (value == null) {
						preparedStatement.setNull(i+1, Types.FLOAT);
					} else {
						preparedStatement.setDouble(i+1, (Double) value);
					}
				default:
					throw new SQLException("error: unknown type - " + type + " passed to setupPS");
			}
		}
	}
	public DatabaseConnectionHandler(Connection connection) {
		try {
			// Load the Oracle JDBC driver
			// Note that the path could change for new drivers
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
	}
	public void close() {
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
	}
	public void deleteBranch(int branchId) {
		try {
			PreparedStatement ps = connection.prepareStatement("DELETE FROM branch WHERE branch_id = ?");
			ps.setInt(1, branchId);
			
			int rowCount = ps.executeUpdate();
			if (rowCount == 0) {
				System.out.println(WARNING_TAG + " Branch " + branchId + " does not exist!");
			}
			
			connection.commit();
	
			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
		}
	}
	public void insertBranch(BranchModel model) {
		try {
			PreparedStatement ps = connection.prepareStatement("INSERT INTO branch VALUES (?,?,?,?,?)");
			ps.setInt(1, model.getId());
			ps.setString(2, model.getName());
			ps.setString(3, model.getAddress());
			ps.setString(4, model.getCity());
			if (model.getPhoneNumber() == 0) {
				ps.setNull(5, java.sql.Types.INTEGER);
			} else {
				ps.setInt(5, model.getPhoneNumber());
			}

			ps.executeUpdate();
			connection.commit();

			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
		}
	}
	public BranchModel[] getBranchInfo() {
		ArrayList<BranchModel> result = new ArrayList<BranchModel>();
		
		try {
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM branch");
		
//    		// get info on ResultSet
   		  ResultSetMetaData rsmd = rs.getMetaData();

//    		System.out.println(" ");
//
    		 //display column names;
    		for (int i = 0; i < rsmd.getColumnCount(); i++) {
    			// get column name and print it
    			System.out.printf("%-15s", rsmd.getColumnName(i + 1));
    		}
			
			while(rs.next()) {
				BranchModel model = new BranchModel(rs.getString("branch_addr"),
													rs.getString("branch_city"),
													rs.getInt("branch_id"),
													rs.getString("branch_name"),
													rs.getInt("branch_phone"));
				result.add(model);
			}

			rs.close();
			stmt.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}	
		
		return result.toArray(new BranchModel[result.size()]);
	}
	public void updateBranch(int id, String name) {
		try {
		  PreparedStatement ps = connection.prepareStatement("UPDATE branch SET branch_name = ? WHERE branch_id = ?");
		  ps.setString(1, name);
		  ps.setInt(2, id);
		
		  int rowCount = ps.executeUpdate();
		  if (rowCount == 0) {
		      System.out.println(WARNING_TAG + " Branch " + id + " does not exist!");
		  }
	
		  connection.commit();
		  
		  ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
		}	
	}
	public boolean login(String username, String password) {
		try {
			if (connection != null) {
				connection.close();
			}
	
			connection = DriverManager.getConnection(ORACLE_URL, "ora_xuyunan1", "a54829650");
			connection.setAutoCommit(false);
	
			System.out.println("\nConnected to Oracle!");
			return true;
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			return false;
		}
	}
	public Connection getConnection() {
		if(this.connection == null){
			System.out.println( " invalid connection");
		}
		return this.connection;
	}
	protected void rollbackConnection() {
		try  {
			connection.rollback();	
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
	}
	public void showReservation() {
		try {
			PreparedStatement sr = connection.prepareStatement("SELECT * FROM RESERVATION");
			ResultSet srr = sr.executeQuery();
			List<ReservationModel> lsrd = toListReservation(srr);
			System.out.println(lsrd.size());
			for(int i = 0; i< lsrd.size();i++){
				System.out.println(lsrd.get(i).ReservationtoString());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	public void shwoRenturn() throws SQLException {
		System.out.println("case 82");
		Statement srtu = connection.createStatement();
		ResultSet srss = srtu.executeQuery("SELECT * FROM RETURN");
		List<ReturnModel> lsdf = toListReturn(srss);
		for(int i =0; i< lsdf.size(); i++){
			System.out.println(lsdf.get(i).RenturntoString());
		}
	}
	public void showRent() throws SQLException {
		Statement srtet = connection.createStatement();
		ResultSet sdgwe = srtet.executeQuery("SELECT * FROM RENT");
		List<RentModel> ksdg = toListRent(sdgwe);
		for(int i =0; i< ksdg.size(); i++ ){
			System.out.println(ksdg.get(i).RenttoString());
		}
	}
	public void returnReportAll(String rdate) throws SQLException {
		PreparedStatement rra = connection.prepareStatement("SELECT * FROM RETURN rt, RENT rit, VEHICLE vec  WHERE  rt.rid = rit.rid AND rit.vid = vec.vid AND rt.rdate = TO_DATE(?, 'DD-MM-YYYY')");
		rra.setString(1,rdate);
		ResultSet rrra = rra.executeQuery();
		List<ReturnModel> lrra = toListReturn(rrra);
		for (int i =0; i< lrra.size(); i++){
			System.out.println(lrra.get(i).RenturntoString());
		}
//		while (rrra.next()){
//			String branch = rrra.getString("location") + rrra.getString("city");
//
//		}
	}
	public void returnReport(String rdate, String rloc, String rcit) throws SQLException {
		PreparedStatement rras = connection.prepareStatement("SELECT * FROM RETURN renturn, RENT rent, VEHICLE vehicle WHERE renturn.rid = rent.rid AND rent.vid = vehicle.vid AND vehicle.location =? and vehicle.city =? and rdate = TO_DATE(?, 'DD-MM-YYYY')");
		rras.setString(1,rloc);
		rras.setString(2,rcit);
		rras.setString(3,rdate);
		ResultSet resrf = rras.executeQuery();
		List<ReturnModel> sdg = toListReturn(resrf);
		for (int i =0; i< sdg.size(); i++){
			System.out.println(sdg.get(i).RenturntoString());
		}
	}
	public void rentReport(String rdate, String rloc, String rcit) throws SQLException {
		PreparedStatement wer = connection.prepareStatement("SELECT * FROM RENT rent, Vehicle vehicle WHERE rent.vid = vehicle.vid AND vehicle.LOCATION = ? AND vehicle.CITY = ? AND FROMDATE = TO_DATE(?,'DD-MM-YYYY')");
		wer.setString(1,rloc);
		wer.setString(2,rcit);
		wer.setString(3,rdate);
		ResultSet resgcs = wer.executeQuery();
		List<RentModel> fghb = toListRent(resgcs);
		for (int i =0; i< fghb.size(); i++){
			System.out.println(fghb.get(i).RenttoString());
		}
	}
	public void rentReportAll(String rdatea) throws SQLException {
		PreparedStatement frgew = connection.prepareStatement("SELECT  * FROM RENT rent, VEHICLE vehicle WHERE rent.vid = vehicle.vid AND FROMDATE = TO_DATE(?,'DD-MM-YYYY')");
		frgew.setString(1, rdatea);
		ResultSet gsge = frgew.executeQuery();
		List<RentModel> sgegh = toListRent(gsge);
		for (int i =0; i< sgegh.size(); i++){
			System.out.println(sgegh.get(i).RenttoString());
		}

	}
	public void insertRent(String confNo) {
		try {

			PreparedStatement tocheckrented = connection.prepareStatement("SELECT * FROM RENT WHERE confNo = ?");
			tocheckrented.setString(1,confNo);
			ResultSet rented  = tocheckrented.executeQuery();
			List<RentModel> sges  = toListRent(rented);
			if(sges.isEmpty()){
			PreparedStatement ps1 = connection.prepareStatement("SELECT * from Reservation WHERE confNo = ?");
			ps1.setString(1,confNo);
			ResultSet rs1 = ps1.executeQuery();
			List<ReservationModel> reslis = toListReservation(rs1);
			if(!reslis.isEmpty()) {
				String vtnamef = reslis.get(0).getVtname();
				String fromdatef = reslis.get(0).getFromDate();
				String todatef = reslis.get(0).getToDate();

				List<VehicleModel> lstv = selectQualifiedVehicles(vtnamef, null, null, fromdatef, todatef);
				//cellphone

				String cellphonef = reslis.get(0).getCellphone();

				VehicleModel vf = lstv.get(0);
				String vidf = vf.getVid().toString();

//				//to find cardName cardNumber expDate
//            PreparedStatement ps2 = connection.prepareStatement("SELECT * from customer WHERE cellphone=?");
//            ps2.setString(1,cellphonef);
//            ResultSet cseges = ps2.executeQuery();
//            List<CustomerModel> csf = toListCustomer(cseges);
//            String expDatef = csf.get(0).g

				Integer odometerf = vf.getOdometer();
				String cardNumberf = "0000-0000-0000-0000";
				String cardNamef = "David Ng";
				String expDatef = "11-11-1111";
				insertARent(vidf, cellphonef, fromdatef, todatef, odometerf, cardNamef, cardNumberf, expDatef, confNo);

			}else {
				System.out.println("No such reservation exists.");
			}}else {
				System.out.println("This reservation has been signed.");
			}

		}catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
		}


	}
	public void insertARent (String vid, String cellphone, String fromDate, String toDate,
							 Integer odometer, String cardName, String cardNumber, String expDate, String confNo){
		try {
			Random ran =  new Random();
			Integer newrid = ran.nextInt();
			if(newrid<0){
				newrid = 0- newrid;
			}
			String newrids = newrid.toString();
			//random a rentid and check its uniqueness
			PreparedStatement tocheckConfNounique = connection.prepareStatement("SELECT * from Reservation WHERE confNo =?");
			tocheckConfNounique.setString(1,newrids);
			ResultSet lsr = tocheckConfNounique.executeQuery();
			List<RentModel> sdga = toListRent(lsr);
			while (!sdga.isEmpty()){
				newrid = ran.nextInt();
				if(newrid<0){
					newrid = 0- newrid;
				}
				newrids = newrid.toString();
				tocheckConfNounique.setString(1,newrids);
				lsr = tocheckConfNounique.executeQuery();
				sdga = toListRent(lsr);

			}
			PreparedStatement ps = connection.prepareStatement(
					"INSERT INTO  Rent VALUES (?,?,?,TO_DATE(?,'YYYY-MM-DD'),TO_DATE(?,'YYYY-MM-DD'),?,?,?,TO_DATE(?,'DD-MM-YYYY'),?)");
			ps.setString(1, newrids);
			ps.setString(2, vid);
			ps.setString(3, cellphone);
			ps.setString(4, fromDate);
			ps.setString(5, toDate);
			ps.setInt(6, odometer);
			ps.setString(7,cardName);
			ps.setString(8, cardNumber);
			ps.setString(9, expDate);
			ps.setString(10, confNo);
			int rowCount = ps.executeUpdate();
			if (rowCount == 0) {
				System.out.println(WARNING_TAG + "Wrong Insertion");
			}
			connection.commit();

			// update the car status
			System.out.println(884);
			PreparedStatement updatevs = connection.prepareStatement("UPDATE Vehicle SET status = 0 WHERE vid =?");
			updatevs.setString(1, vid);
			updatevs.executeQuery();
			connection.commit();
			System.out.println(888);
			updatevs.close();

			//to find the type of the car
			PreparedStatement ctpf = connection.prepareStatement("SELECT  * FROM VEHICLE WHERE VID = ?");
			ctpf.setString(1,vid);
			ResultSet fseghweg = ctpf.executeQuery();
			List<VehicleModel> egwh = toListVehicleModel(fseghweg);
			String cartp = egwh.get(0).getVtname();
			String rate = toFindRate(cartp);

			System.out.println("Rent successful");
			if(cardName.equals(""))
			System.out.println("Your Rent-ID is: " + newrids +"; vehicle ID:" + vid +"; FromDate:" + fromDate + " to "+toDate+"; You will paid with your card:" +cardNumber);
			System.out.println(rate);
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
		}

	}

	private String toFindRate(String cartp) throws SQLException {
		System.out.println(912);
		PreparedStatement findr = connection.prepareStatement("SELECT * FROM VEHICLETYPE WHERE VTNAME = ?");
		findr.setString(1,cartp);
		ResultSet fweg = findr.executeQuery();
		String recipt ="";
		while (fweg.next()){
			String feature = fweg.getString("features");
			String wrate = fweg.getString("wrate");
			String drate = fweg.getString("drate");
			String hrage = fweg.getString("hrate");
			String wirate = fweg.getString("wirate");
			String diage = fweg.getString("dirate");
			String hiage = fweg.getString("hirate");
			String krage = fweg.getString("krate");
			recipt = "Your choose "+ cartp +" with feature:"+ feature +" The rate is: "+
					wrate +"(wrate) "+drate+"(drate) "+ hrage+"(hrate) "+ wirate+"(wirate) "+
					diage+"(dirate) "+hiage+"(hirate) "+krage+"(krate) ";
		}
		return recipt;
	}

	public void insertReturn(String rid, String date, Integer odometer, Integer fullTank) {

		try {
			PreparedStatement ps = connection.prepareStatement(
					"INSERT INTO  Return VALUES (?,TO_DATE(?,'DD-MM-YYYY'),?,?,8)");
			ps.setString(1, rid);
			ps.setString(2, date);
			ps.setInt(3, odometer);
			ps.setInt(4, fullTank);

			int rowCount = ps.executeUpdate();
			if (rowCount == 0) {
				System.out.println(WARNING_TAG + "Wrong Insertion");
			}

			connection.commit();


			//te fetch vid
			PreparedStatement fetchvid = connection.prepareStatement("SELECT * FROM Rent WHERE rid =?");
			fetchvid.setString(1, rid);
			ResultSet ewgs = fetchvid.executeQuery();
			List<RentModel> lstvid = toListRent(ewgs);

			//save the details of this rent  and present info of this rent
			String vidi = lstvid.get(0).getVid();
			Integer odoi = lstvid.get(0).getOdometer();
			String fromdate = lstvid.get(0).getFromDate().toString();
			String cardNames = lstvid.get(0).getCardName();
			String cellphones = lstvid.get(0).getCellphone();
			int i = Integer.parseInt(vidi);
			PreparedStatement findtp = connection.prepareStatement("SELECT * FROM VEHICLE WHERE VID = ?");
			findtp.setInt(1,i);
			String reportRent = cardNames + ": " + cellphones + ". From date: " + fromdate + " to Date:" + date + ". Odometer: " + odoi + "-" + odometer;
			ResultSet fsrg = findtp.executeQuery();
			List<VehicleModel> fwegw = toListVehicleModel(fsrg);
			String vtnamefound = fwegw.get(0).getVtname();
			reportRent = reportRent+ toFindRate(vtnamefound);


			System.out.println(reportRent);

			//to update the detail of car
			connection.commit();
			PreparedStatement updatevstatus = connection.prepareStatement("UPDATE Vehicle SET status = 1 WHERE vid=?");
			updatevstatus.setString(1, vidi);
			updatevstatus.executeQuery();

			PreparedStatement updatevodo = connection.prepareStatement("UPDATE Vehicle SET odometer = ? WHERE vid = ?");
			updatevodo.setInt(1, odometer);
			updatevodo.setString(2, vidi);
			updatevodo.executeQuery();

			connection.commit();


			ps.close();
			updatevstatus.close();
			fetchvid.close();
			updatevodo.close();
			connection.commit();

		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
		}
	}
}
