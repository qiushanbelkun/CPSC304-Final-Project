package ca.ubc.cs304.delegates;

import ca.ubc.cs304.model.BranchModel;

import java.sql.SQLException;

/**
 * This interface uses the delegation design pattern where instead of having
 * the TerminalTransactions class try to do everything, it will only
 * focus on handling the UI. The actual logic/operation will be delegated to the 
 * controller class (in this case Bank).
 * 
 * TerminalTransactions calls the methods that we have listed below but 
 * Bank is the actual class that will implement the methods.
 */
public interface TerminalTransactionsDelegate {
	public void deleteBranch(int branchId);
	public void insertBranch(BranchModel model);
	public void showBranch();
	public void updateBranch(int branchId, String name);
	public void terminalTransactionsFinished();

	void findselectV(String vtname, String city, String location, String fromdate, String tomdate);

	int countqulifiedvehicle(String vtname, String city, String location, String fromdate, String tomdate);

	void makesreservation(String vtname, String pn, String fromdate, String tomdate) throws SQLException;

	void showReservation();

	void showReturn();

	void showRent();

	void returnReportall(String rdate);

	void returnReport(String rdate, String rloc, String rcit);

	void rentReport(String rdate, String rloc, String rcit);

	void rentReportAll(String rdatea);

	void insertrent(String rid);

	void insertReturn(String rid, String returndate, Integer odor, Integer fuk);
}
