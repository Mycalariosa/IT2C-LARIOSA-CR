package it2c.lariosa.cr;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CONFIG {

    // Method to establish a database connection
    public static Connection connectDB() {
        Connection con = null;
        try {
            Class.forName("org.sqlite.JDBC"); // Load the SQLite JDBC driver
            con = DriverManager.getConnection("jdbc:sqlite:lariosa.db"); // Establish connection
        } catch (Exception e) {
            System.out.println("Connection Failed: " + e);
        }
        return con;
    }

    // Method to check existence of a clothing item by its ID
    public int checkExistence(String query, int clothingItemId) {
        int count = 0;
        try (Connection con = connectDB();
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, clothingItemId);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt(1); // Get the count of matching records
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return count;
    }

    // Method to add a record with dynamic data types
    public void addRecord(String sql, Object... values) {
        try (Connection conn = connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            setPreparedStatementValues(pstmt, values);
            pstmt.executeUpdate();
            System.out.println("Record added successfully!");
        } catch (SQLException e) {
            System.out.println("Error adding record: " + e.getMessage());
        }
    }

    // Method to update a record with dynamic data types
    public void updateRecord(String sql, Object... values) {
        try (Connection conn = connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            setPreparedStatementValues(pstmt, values);
            pstmt.executeUpdate();
            System.out.println("Record updated successfully!");
        } catch (SQLException e) {
            System.out.println("Error updating record: " + e.getMessage());
        }
    }

    // Method to delete a record
    public void deleteRecord(String sql, Object... values) {
        try (Connection conn = connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            setPreparedStatementValues(pstmt, values);
            pstmt.executeUpdate();
            System.out.println("Record deleted successfully!");
        } catch (SQLException e) {
            System.out.println("Error deleting record: " + e.getMessage());
        }
    }
public void viewCustomerList(String query, String[] headers) {
    viewRecords(query, headers, new String[]{"c_id", "firstname", "lastname"});
}

    // Method to view records with specified headers and columns
    public void viewRecords(String sqlQuery, String[] columnHeaders, String[] columnNames) {
        if (columnHeaders.length != columnNames.length) {
            System.out.println("Error: Mismatch between column headers and column names.");
            return;
        }

        try (Connection conn = connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
             ResultSet rs = pstmt.executeQuery()) {

            // Print headers
            System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------");
            for (String header : columnHeaders) {
                System.out.printf("%-20s | ", header);
            }
            System.out.println("\n-----------------------------------------------------------------------------------------------------------------------------------------");

            // Print rows
            while (rs.next()) {
                for (String colName : columnNames) {
                    String value = rs.getString(colName);
                    System.out.printf("%-20s | ", value != null ? value : "");
                }
                System.out.println();
            }
            System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------");

        } catch (SQLException e) {
            System.out.println("Error retrieving records: " + e.getMessage());
        }
    }

    // Method to get a count of records based on a query and parameter
    public int getCount(String query, int parameter) {
        int count = 0;  
        try (Connection conn = connectDB();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, parameter);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Error getting count: " + e.getMessage());
        }
        return count;
    }

    // Method to check if a record with a specific ID exists
    public boolean reqIdExists(String reqId) {
        String query = "SELECT COUNT(1) FROM Rental WHERE rental_id=?";
        try (Connection conn = connectDB();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, reqId);
            ResultSet rs = pstmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.out.println("Error checking record existence: " + e.getMessage());
            return false;
        }
    }

    // Helper method to set values in PreparedStatement dynamically
    private void setPreparedStatementValues(PreparedStatement pstmt, Object... values) throws SQLException {
        for (int i = 0; i < values.length; i++) {
            if (values[i] instanceof Integer) {
                pstmt.setInt(i + 1, (Integer) values[i]);
            } else if (values[i] instanceof Double) {
                pstmt.setDouble(i + 1, (Double) values[i]);
            } else if (values[i] instanceof Float) {
                pstmt.setFloat(i + 1, (Float) values[i]);
            } else if (values[i] instanceof Long) {
                pstmt.setLong(i + 1, (Long) values[i]);
            } else if (values[i] instanceof Boolean) {
                pstmt.setBoolean(i + 1, (Boolean) values[i]);
            } else if (values[i] instanceof java.util.Date) {
                pstmt.setDate(i + 1, new java.sql.Date(((java.util.Date) values[i]).getTime()));
            } else if (values[i] instanceof java.sql.Date) {
                pstmt.setDate(i + 1, (java.sql.Date) values[i]);
            } else if (values[i] instanceof java.sql.Timestamp) {
                pstmt.setTimestamp(i + 1, (java.sql.Timestamp) values[i]);
            } else {
                pstmt.setString(i + 1, values[i].toString());
            }
        }
    }
   public void showRentalDetails(String query, int rentalId) {
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
        stmt.setInt(1, rentalId);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            System.out.println("Rental Details:");
            System.out.println("Rental ID: " + rs.getInt("rental_id"));
            System.out.println("Customer ID: " + rs.getInt("customer_id"));
            System.out.println("Clothing Item ID: " + rs.getInt("clothing_item_id"));
            System.out.println("Rental Date: " + rs.getDate("rental_date"));
            System.out.println("Return Date: " + rs.getDate("return_date"));
        } else {
            System.out.println("No details found for the given rental ID.");
        }
    } catch (SQLException e) {
        System.out.println("Error retrieving rental details. Please check the following:");
        System.out.println("- Ensure the database connection is active.");
        System.out.println("- Verify that the `rental_id` exists in the database.");
        System.out.println("- Confirm the query syntax is correct.");
        e.printStackTrace(); // For detailed error output; consider removing in production
    }
}

    // Method to retrieve a single result from a query (e.g., price or other attribute)
    public double getSingleResult(String query, int clothingItemId) {
        double result = 0.0;
        try (Connection conn = connectDB();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, clothingItemId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    result = rs.getDouble(1);  // Assuming the result is a numeric value
                }
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving single result: " + e.getMessage());
        }
        return result;
    }
public String getAvailabilityStatus(int clothingItemId) {
        String status = null;
        String query = "SELECT c_availability FROM ClothingItem WHERE clothing_ID = ?";
        try (Connection conn = connectDB();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, clothingItemId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                status = rs.getString("c_availability");
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving availability status: " + e.getMessage());
        }
        return status;
    }

 public boolean deleteRecord(String sqlQuery, int id) {
        try (Connection conn = connectDB();
             PreparedStatement stmt = conn.prepareStatement(sqlQuery)) {
            
            // Set the parameter for the prepared statement (e.g., customer ID)
            stmt.setInt(1, id);

            // Execute the delete query
            int rowsAffected = stmt.executeUpdate();

            // If rowsAffected is greater than 0, it means the deletion was successful
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Error executing delete query: " + e.getMessage());
            return false;
        }
    }
public String getRecord(String query, int parameter) {
    String result = null;
    try (Connection conn = connectDB();
         PreparedStatement pstmt = conn.prepareStatement(query)) {
        pstmt.setInt(1, parameter);
        try (ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                result = rs.getString(1); // Get the string result from the first column
            }
        }
    } catch (SQLException e) {
        System.out.println("Error retrieving record: " + e.getMessage());
    }
    return result;
}

public Integer getIntegerRecord(String query, int parameter) {
    Integer result = null;
    try (Connection conn = connectDB();
         PreparedStatement pstmt = conn.prepareStatement(query)) {
        pstmt.setInt(1, parameter);
        try (ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                result = rs.getInt(1); // Get the integer result from the first column
            }
        }
    } catch (SQLException e) {
        System.out.println("Error retrieving record: " + e.getMessage());
    }
    return result;
}

public ResultSet getRecords(String query, String[] columns) {
        ResultSet resultSet = null;
        Connection con = connectDB();
        try {
            PreparedStatement pst = con.prepareStatement(query);
            resultSet = pst.executeQuery(); // Execute the query and get the result
        } catch (SQLException e) {
            System.out.println("Error executing query: " + e.getMessage());
        }
        return resultSet; // Return the ResultSet for processing
    }

    public double getDoubleValue(String priceQuery, int clothingItemId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public int getInt(String clothingItemIdQuery, int rentalId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String[] viewRecords(String rentalQuery, int rentalId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void viewRecords(String rentalQuery, String[] headers, String[] columns, int rentalId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public int getSingleIntResult(String clothingItemIdQuery, int rentalId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private Connection getConnection() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String[] getSingleRecord(String dateQuery, int rentalId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
   

    private static class connection {

        private static PreparedStatement prepareStatement(String query) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        public connection() {
        }
    }
}
