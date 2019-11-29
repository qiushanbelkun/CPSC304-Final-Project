package ca.ubc.cs304.database;


import oracle.jdbc.driver.OracleDriver;

import java.sql.*;
import java.util.List;

// this file is the interface to for running queries on different type of models

public abstract class GeneralOperations<T> {
    protected static final String ORACLE_URL = "jdbc:oracle:thin:@localhost:1522:stu";
    protected static final String EXCEPTION_TAG = "[EXCEPTION]";
    protected static final String WARNING_TAG = "[WARNING]";
    protected Connection connection = null;

    // constructor
    protected GeneralOperations(Connection connection) {
        this.connection = connection;
        try {
            // Load the Oracle JDBC driver
            // Note that the path could change for new drivers
            DriverManager.registerDriver(new OracleDriver());
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }

    // whether the getall method will be used in subclasses is still undetermined
    public abstract List<T> getAll() throws SQLException;

    protected ResultSet executeSQLCmd(String cmd) throws SQLException {
        Statement statement = this.connection.createStatement();
        return statement.executeQuery(cmd);
    }
    protected ResultSet executeSQLCmd(String cmd, List<Integer> types, List<Object> values) throws SQLException {
        PreparedStatement preparedStatement = this.connection.prepareStatement(cmd);
        setUPPs(preparedStatement, types, values);
        return preparedStatement.executeQuery();
    }

    // this the switch for each cases
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
    protected void rollbackConnection() {
        try  {
            connection.rollback();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }

    public boolean login(String username, String password) {
        try {
            if (connection != null) {
                connection.close();
            }

            connection = DriverManager.getConnection(ORACLE_URL, username, password);
            connection.setAutoCommit(false);

            System.out.println("\nConnected to Oracle!");
            return true;
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            return false;
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

}
