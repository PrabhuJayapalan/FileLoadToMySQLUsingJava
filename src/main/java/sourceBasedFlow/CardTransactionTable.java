package sourceBasedFlow;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import configuration.SystemConfigProp;
import utility.connectionPool.HikariDataPool;

public class CardTransactionTable {

	public static void main(String[] args) throws Exception {
		
		SystemConfigProp systemconfig = SystemConfigProp.getConfig();
        String csvFilePath = "/home/prabhuj/Desktop/CardTransactionInputFile/CardTransaction.csv";
        Connection connection = null;
        
        int batchSize = 20;
        try {
        	
            HikariDataPool hikariObjInstance = HikariDataPool.getInstance();
            connection = hikariObjInstance.dataSource.getConnection();
            connection.setAutoCommit(false);
 
            String sql = "INSERT INTO cardTransaction (CardTypeCode, CardTypeFullName, IssuingBank, CardNumber, CardHolderName, CvvCvv2, IssueDate, ExpiryDate, BillingDate, CardPIN, CreditLimit) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
 
            BufferedReader lineReader = new BufferedReader(new FileReader(csvFilePath));
            String lineText = null;
 
            int count = 0;
 
            lineReader.readLine(); // skip header line
 
            while ((lineText = lineReader.readLine()) != null) {
                String[] data = lineText.split(",");
                
                String cardTypeCode = data[0];
                String cardTypeFullName = data[1];
                String issuingBank = data[2];
                long cardNumber = Long.parseLong(data[3]);
                String cardHolderName = data[4];
                int cvvCvv2 = Integer.parseInt(data[5]);
                String issueDate = data[6]; //date
                String expiryDate = data[7]; //date
                int billingDate = Integer.parseInt(data[8]);
                int cardPIN = Integer.parseInt(data[9]);
                int creditLimit = Integer.parseInt(data[10]);
 
                statement.setString(1, cardTypeCode);
                statement.setString(2, cardTypeFullName);
                statement.setString(3, issuingBank);
                statement.setLong(4, cardNumber);
                statement.setString(5, cardHolderName);
                statement.setInt(6, cvvCvv2);
                
                SimpleDateFormat sDF1 = new SimpleDateFormat("MM/yyyy");
                java.util.Date date1 = sDF1.parse(issueDate);
                java.sql.Date sqlIssueDate = new java.sql.Date(date1.getTime());
                statement.setDate(7, sqlIssueDate);
                
                SimpleDateFormat sDF2 = new SimpleDateFormat("MM/yyyy");
                java.util.Date date2 = sDF2.parse(expiryDate);
                java.sql.Date sqlExpiryDate = new java.sql.Date(date2.getTime());
                statement.setDate(8, sqlExpiryDate);
                
                statement.setInt(9, billingDate);
                statement.setInt(10, cardPIN);
                statement.setInt(11, creditLimit);
 
 
                statement.addBatch();
 
                if (count % batchSize == 0) {
                    statement.executeBatch();
                }
            }
 
            lineReader.close();
 
            // execute the remaining queries
            statement.executeBatch();
 
            connection.commit();
            connection.close();
 
        } catch (IOException ex) {
            System.err.println(ex);
        } catch (SQLException ex) {
            ex.printStackTrace();
 
            try {
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        

	}

}
