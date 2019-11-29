package ca.ubc.cs304.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

import ca.ubc.cs304.database.VehicleOperations;
import ca.ubc.cs304.delegates.TerminalTransactionsDelegate;
import ca.ubc.cs304.model.BranchModel;
import ca.ubc.cs304.model.VehicleModel;

/**
 * The class is only responsible for handling terminal text inputs. 
 */
public class TerminalTransactions {
	private static final String EXCEPTION_TAG = "[EXCEPTION]";
	private static final String WARNING_TAG = "[WARNING]";
	private static final int INVALID_INPUT = Integer.MIN_VALUE;
	private static final int EMPTY_INPUT = 0;
	
	private BufferedReader bufferedReader = null;
	private TerminalTransactionsDelegate delegate = null;

	public TerminalTransactions() {
	}

	/**
	 * Displays simple text interface
	 */ 
	public void showMainMenu(TerminalTransactionsDelegate delegate) throws SQLException {
		this.delegate = delegate;
		
	    bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		int choice = INVALID_INPUT;
		
		while (choice != 5) {
			System.out.println();
//			System.out.println("1. Insert branch");
//			System.out.println("2. Delete branch");
//			System.out.println("3. Update branch name");
//			System.out.println("4. Show branch");
//			System.out.println("5. Quit");
			System.out.println("1. Customer");
			System.out.println("2. Clerk");
			System.out.print("Please choose one of the above 5 options: ");
			choice = readInteger(false);
//			choice = 7;
			if (choice != 5) {
				switch (choice) {
//				case 1:
//					handleInsertOption();
//					break;
//				case 2:
//					handleDeleteOption();
//					break;
//				case 3:
//					handleUpdateOption();
//					break;
//				case 4:
//					delegate.showBranch();
//					break;
				case 5:
					handleQuitOption();
					break;
				case 1:
					handleCustomerOption();
					break;
				case 2:
					handleClerkOption();
					break;
				default:
					System.out.println(WARNING_TAG + " The number that you entered was not a valid option.");
					break;
				}
			}
		}		
	}
	public void handleCustomerOption() throws SQLException {
		int choice = INVALID_INPUT;
		System.out.println("type 'quit' to quit");
		while (choice!=000) {
			System.out.println("vehicle type?");
			String vtname = readLine().trim();
			//vtname = "Full-size"; //!!!
			if(vtname.equals("quit")){
				choice=000;
				break;
			}
			if (vtname.equals("null")){
				vtname = null;
			}
			System.out.println("location?");
			String location = readLine().trim();
			//location = "UBC";//!!!
			if(location.equals("quit")){
				choice=000;
				break;
			}
			if(location.equals("null")){
				location = null;
			}
			System.out.println("city");
			String city = readLine().trim();
			//city = "Vancouver";//!!!
			if(city.equals("quit")){
				choice=000;
				break;
			}
			if(city.equals("null")){
				city= null;
			}
			System.out.println("fromdate in formate dd-mm-yyyy");
			String fromdate = readLine().trim();
			//String fromdate = "11-11-1111";//!!!
			if(fromdate.equals("quit")){
				choice=000;
				break;
			}
			if(fromdate.equals("null")){
				fromdate= null;
			}
			System.out.println("tomdate in formate dd-mm-yyyy");
			String tomdate = readLine().trim();
			//String tomdate = "11-11-1111";//!!!
			if(tomdate.equals("quit")){
				choice=000;
				break;
			}
			if(tomdate.equals("null")){
				tomdate=null;
			}
			int k = delegate.countqulifiedvehicle(vtname, city, location, fromdate, tomdate);
			if(k==1 || k >1){
			System.out.println("There are "+ k + " availible vehicles. Type yes to continue");
			String anss = readLine().trim();
			if(anss.equals("yes")){
				System.out.println("Here is you options:");
				delegate.findselectV(vtname, city, location, fromdate, tomdate);
				System.out.println("Type res to make a reservation");
				String asns2 = readLine().trim();
				if(asns2.equals("res")) {
					System.out.println("phone number:");
					String pn = readLine().trim();
					delegate.makesreservation(vtname,pn,fromdate, tomdate);
				}
			}
			if(anss.equals("quit")){
				break;
			}}
			else {
				break;
			}
		}
	}
	public void handleClerkOption(){
		int choice = INVALID_INPUT;
		System.out.println("Type 'quit' to quit");
		System.out.println("1. Rent");
		System.out.println("2. View daily Rents for all branches");
		System.out.println("3. View daily Rents for some branch");
		System.out.println("4. Return");
		System.out.println("5. View daily Returns for all branches");
		System.out.println("6. View daily Returns for some branch");
		System.out.println("7. View all reservations");
		System.out.println("8. View all rents");
		System.out.println("9. View all returns");
		choice = readInteger(false);
		System.out.println(" ");
		while (choice!= 999){
			switch (choice){
				case 1:
					handleRentOption();
					choice=999;
					break;
				case 2:
					handleRentReportall();
					choice=999;
					break;
				case 3:
					handleRentReport();
					choice=999;
					break;
				case 4:
					handleReturnOption();
					choice=999;
					break;
				case 5:
					handleReturnReportall();
					choice=999;
					break;
				case 6:
					handleReturnReport();
					choice=999;
					break;
				case 7:
					handleViewReservations();
					choice=999;
					break;
				case 8:
					handleViewRent();
					choice=999;
					break;
				case 9:
					handleViewReturn();
					choice=999;
					break;
				default:
					System.out.println(WARNING_TAG + " The number that you entered was not a valid option.");
					choice=999;
					break;
			}

		}

	}

	private void handleViewReturn() {
		delegate.showReturn();
	}

	private void handleViewRent() {
		delegate.showRent();
	}

	private void handleViewReservations() {
		delegate.showReservation();
	}

	private void handleReturnReportall() {
		System.out.println("Given date in format DD-MM-YYYY ");
		String rdate = readLine().trim();
		delegate.returnReportall(rdate);
	}

	private void handleReturnReport() {
		System.out.println("Give date in format DD-MM-YYYY");
		String rdate = readLine().trim();
		System.out.println("Give location");
		String rloc = readLine().trim();
		System.out.println("Give city");
		String rcit = readLine().trim();
		delegate.returnReport(rdate, rloc, rcit);
	}

	private void handleReturnOption() {
		System.out.println("Give rid");
		String rid = readLine().trim();
		System.out.println("Give return date in formate dd-mm-yyyy");
		String returndate = readLine().trim();
		System.out.println("Odometer:");
		Integer odor = readInteger(false);
		System.out.println("Fulltank?");
		Integer fuk = readInteger(false);
		delegate.insertReturn(rid, returndate, odor, fuk);

	}
//	public void insertAReturn(String rid, String date, Integer odometer, Integer fullTank) {

	private void handleRentReport() {
		System.out.println("Give date in format DD-MM-YYYY");
		String rdate = readLine().trim();
		System.out.println("Give location");
		String rloc = readLine().trim();
		System.out.println("Give city");
		String rcit = readLine().trim();
		delegate.rentReport(rdate,rloc,rcit);

	}

	private void handleRentReportall() {
		System.out.println("Give date in format DD-MM-YYYY");
		String rdatea = readLine().trim();
		delegate.rentReportAll(rdatea);

	}

	private void handleRentOption() {
		System.out.println("Give Confirmation No");
		String confNo = readLine().trim();
		delegate.insertrent(confNo);
	}

	private void handleDeleteOption() {
		int branchId = INVALID_INPUT;
		while (branchId == INVALID_INPUT) {
			System.out.print("Please enter the branch ID you wish to delete: ");
			branchId = readInteger(false);
			if (branchId != INVALID_INPUT) {
				delegate.deleteBranch(branchId);
			}
		}
	}

	private void handleInsertOption() {
		int id = INVALID_INPUT;
		while (id == INVALID_INPUT) {
			System.out.print("Please enter the branch ID you wish to insert: ");
			id = readInteger(false);
		}

		String name = null;
		while (name == null || name.length() <= 0) {
			System.out.print("Please enter the branch name you wish to insert: ");
			name = readLine().trim();
		}

		// branch address is allowed to be null so we don't need to repeatedly ask for the address
		System.out.print("Please enter the branch address you wish to insert: ");
		String address = readLine().trim();
		if (address.length() == 0) {
			address = null;
		}

		String city = null;
		while (city == null || city.length() <= 0) {
			System.out.print("Please enter the branch city you wish to insert: ");
			city = readLine().trim();
		}

		int phoneNumber = INVALID_INPUT;
		while (phoneNumber == INVALID_INPUT) {
			System.out.print("Please enter the branch phone number you wish to insert: ");
			phoneNumber = readInteger(true);
		}

		BranchModel model = new BranchModel(address,
											city,
											id,
											name,
											phoneNumber);
		delegate.insertBranch(model);
	}

	private void handleQuitOption() {
		System.out.println("Good Bye!");

		if (bufferedReader != null) {
			try {
				bufferedReader.close();
			} catch (IOException e) {
				System.out.println("IOException!");
			}
		}

		delegate.terminalTransactionsFinished();
	}

	private void handleUpdateOption() {
		int id = INVALID_INPUT;
		while (id == INVALID_INPUT) {
			System.out.print("Please enter the branch ID you wish to update: ");
			id = readInteger(false);
		}

		String name = null;
		while (name == null || name.length() <= 0) {
			System.out.print("Please enter the branch name you wish to update: ");
			name = readLine().trim();
		}

		delegate.updateBranch(id, name);
	}

	private int readInteger(boolean allowEmpty) {
		String line = null;
		int input = INVALID_INPUT;
		try {
			line = bufferedReader.readLine();
			input = Integer.parseInt(line);
		} catch (IOException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		} catch (NumberFormatException e) {
			if (allowEmpty && line.length() == 0) {
				input = EMPTY_INPUT;
			} else {
				System.out.println(WARNING_TAG + " Your input was not an integer");
			}
		}
		return input;
	}

	private String readLine() {
		String result = null;
		try {
			result = bufferedReader.readLine();
		} catch (IOException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
		return result;
	}
}
