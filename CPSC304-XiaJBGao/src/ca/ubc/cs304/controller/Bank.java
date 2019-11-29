package ca.ubc.cs304.controller;

import ca.ubc.cs304.database.DatabaseConnectionHandler;
import ca.ubc.cs304.delegates.LoginWindowDelegate;
import ca.ubc.cs304.delegates.TerminalTransactionsDelegate;
import ca.ubc.cs304.model.BranchModel;
import ca.ubc.cs304.ui.LoginWindow;
import ca.ubc.cs304.ui.TerminalTransactions;
import oracle.jdbc.driver.DBConversion;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * This is the main controller class that will orchestrate everything.
 */
public class Bank implements LoginWindowDelegate, TerminalTransactionsDelegate {
	private DatabaseConnectionHandler dbHandler = null;
	private LoginWindow loginWindow = null;
	private Connection connection = null;
	public Bank() {
		dbHandler = new DatabaseConnectionHandler(connection);
	}
	
	private void start() {
		loginWindow = new LoginWindow();
		loginWindow.showFrame(this);
	}
	
	/**
	 * LoginWindowDelegate Implementation
	 * 
     * connects to Oracle database with supplied username and password
     */ 
	public void login(String username, String password) throws SQLException {
		boolean didConnect = dbHandler.login(username, password);

		if (didConnect) {
			// Once connected, remove login window and start text transaction flow
			loginWindow.dispose();

			TerminalTransactions transaction = new TerminalTransactions();
			transaction.showMainMenu(this);
		} else {
			loginWindow.handleLoginFailed();

			if (loginWindow.hasReachedMaxLoginAttempts()) {
				loginWindow.dispose();
				System.out.println("You have exceeded your number of allowed attempts");
				System.exit(-1);
			}
		}
	}
	
	/**
	 * TermainalTransactionsDelegate Implementation
	 * 
	 * Insert a branch with the given info
	 */
    public void insertBranch(BranchModel model) {
    	dbHandler.insertBranch(model);
    }

    /**
	 * TermainalTransactionsDelegate Implementation
	 * 
	 * Delete branch with given branch ID.
	 */ 
    public void deleteBranch(int branchId) {
    	dbHandler.deleteBranch(branchId);
    }
    
    /**
	 * TermainalTransactionsDelegate Implementation
	 * 
	 * Update the branch name for a specific ID
	 */

    public void updateBranch(int branchId, String name) {
    	dbHandler.updateBranch(branchId, name);
    }

    /**
	 * TermainalTransactionsDelegate Implementation
	 * 
	 * Displays information about varies bank branches.
	 */
    public void showBranch() {
    	BranchModel[] models = dbHandler.getBranchInfo();
    	
    	for (int i = 0; i < models.length; i++) {
    		BranchModel model = models[i];
    		
    		// simplified output formatting; truncation may occur
    		System.out.printf("%-10.10s", model.getId());
    		System.out.printf("%-20.20s", model.getName());
    		if (model.getAddress() == null) {
    			System.out.printf("%-20.20s", " ");
    		} else {
    			System.out.printf("%-20.20s", model.getAddress());
    		}
    		System.out.printf("%-15.15s", model.getCity());
    		if (model.getPhoneNumber() == 0) {
    			System.out.printf("%-15.15s", " ");
    		} else {
    			System.out.printf("%-15.15s", model.getPhoneNumber());
    		}
    		
    		System.out.println();
    	}
    }
	
    /**
	 * TerminalTransactionsDelegate Implementation
	 * 
     * The TerminalTransaction instance tells us that it is done with what it's 
     * doing so we are cleaning up the connection since it's no longer needed.
     */ 
    public void terminalTransactionsFinished() {
    	dbHandler.close();
    	dbHandler = null;
    	
    	System.exit(0);
    }

	@Override
	public void findselectV(String vtname, String city, String location, String fromdate, String tomdate) {
		dbHandler.findnselectv(vtname,city,location,fromdate,tomdate);
	}

	@Override
	public int countqulifiedvehicle(String vtname, String city, String location, String fromdate, String tomdate) {
		return dbHandler.countqulifiedvehicle(vtname,city,location,fromdate,tomdate);
	}

	@Override
	public void makesreservation(String vtname, String pn, String fromdate, String tomdate) throws SQLException {
		dbHandler.insertAReservation(vtname,pn,fromdate,tomdate);
	}

	@Override
	public void showReservation() {
		dbHandler.showReservation();
	}

	@Override
	public void showReturn() {
		try {
			dbHandler.shwoRenturn();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void showRent() {
		try {
			dbHandler.showRent();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void returnReportall(String rdate) {
		try {
			dbHandler.returnReportAll(rdate);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void returnReport(String rdate, String rloc, String rcit) {
		try {
			dbHandler.returnReport(rdate,rloc,rcit);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void rentReport(String rdate, String rloc, String rcit) {
		try {
			dbHandler.rentReport(rdate, rloc,rcit);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void rentReportAll(String rdatea) {
		try {
			dbHandler.rentReportAll(rdatea);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void insertrent(String rid) {
		dbHandler.insertRent(rid);
	}

	@Override
	public void insertReturn(String rid, String returndate, Integer odor, Integer fuk) {
		dbHandler.insertReturn(rid, returndate, odor, fuk);
	}


	/**
	 * Main method called at launch time
	 */
	public static void main(String args[]) {
		Bank bank = new Bank();
		bank.start();
	}
}
