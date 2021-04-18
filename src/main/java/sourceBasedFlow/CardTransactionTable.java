package sourceBasedFlow;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import configuration.SystemConfigProp;
import utility.connectionPool.HikariDataPool;
import utility.tableColumnPojo.CardTransPojo;

public class CardTransactionTable {

	public static void fileLoad(String fileName, SystemConfigProp systemconfig) throws Exception {
		String csvFilePath = fileName;
		Connection connection = null;
		CardTransPojo cTPojo = null;

		int batchSize = 20;
		try {

			HikariDataPool hikariObjInstance = HikariDataPool.getInstance();
			connection = hikariObjInstance.dataSource.getConnection();
			cTPojo = new CardTransPojo();
			connection.setAutoCommit(false);

			String sql = "INSERT INTO cardTransaction (CardTypeCode, CardTypeFullName, IssuingBank, CardNumber, CardHolderName, CvvCvv2, IssueDate, ExpiryDate, BillingDate, CardPIN, CreditLimit) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement statement = connection.prepareStatement(sql);

			BufferedReader lineReader = new BufferedReader(new FileReader(csvFilePath));
			String lineText = null;

			int count = 0;

			lineReader.readLine(); // skip header line

			while ((lineText = lineReader.readLine()) != null) {
				cTPojo.setAllColumns(lineText);

				statement.setString(1, cTPojo.getCardTypeCode());
				statement.setString(2, cTPojo.getCardTypeFullName());
				statement.setString(3, cTPojo.getIssuingBank());
				statement.setLong(4, cTPojo.getCardNumber());
				statement.setString(5, cTPojo.getCardHolderName());
				statement.setInt(6, cTPojo.getCvvCvv2());
				statement.setDate(7, cTPojo.getSqlIssueDate());
				statement.setDate(8, cTPojo.getSqlExpiryDate());
				statement.setInt(9, cTPojo.getBillingDate());
				statement.setInt(10, cTPojo.getCardPIN());
				statement.setInt(11, cTPojo.getCreditLimit());


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
