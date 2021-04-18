package utility.tableColumnPojo;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class CardTransPojo {

	private static String cardTypeCode;
	private static String cardTypeFullName;
	private static String issuingBank;
	private static long cardNumber;
	private static String cardHolderName;
	private static int cvvCvv2;
	private static Date sqlIssueDate;
	private static Date sqlExpiryDate;
	private static int billingDate;
	private static int cardPIN;
	private static int creditLimit;

	public String getCardTypeCode() {
		return cardTypeCode;
	}

	public static void setCardTypeCode(String cardTypeCode) {
		CardTransPojo.cardTypeCode = cardTypeCode;
	}

	public String getCardTypeFullName() {
		return cardTypeFullName;
	}

	public static void setCardTypeFullName(String cardTypeFullName) {
		CardTransPojo.cardTypeFullName = cardTypeFullName;
	}

	public String getIssuingBank() {
		return issuingBank;
	}

	public static void setIssuingBank(String issuingBank) {
		CardTransPojo.issuingBank = issuingBank;
	}

	public long getCardNumber() {
		return cardNumber;
	}

	public static void setCardNumber(long cardNumber) {
		CardTransPojo.cardNumber = cardNumber;
	}

	public String getCardHolderName() {
		return cardHolderName;
	}

	public static void setCardHolderName(String cardHolderName) {
		CardTransPojo.cardHolderName = cardHolderName;
	}

	public int getCvvCvv2() {
		return cvvCvv2;
	}

	public static void setCvvCvv2(int cvvCvv2) {
		CardTransPojo.cvvCvv2 = cvvCvv2;
	}

	public Date getSqlIssueDate() {
		return sqlIssueDate;
	}

	public static void setSqlIssueDate(Date sqlIssueDate) {
		CardTransPojo.sqlIssueDate = sqlIssueDate;
	}

	public Date getSqlExpiryDate() {
		return sqlExpiryDate;
	}

	public static void setSqlExpiryDate(Date sqlExpiryDate) {
		CardTransPojo.sqlExpiryDate = sqlExpiryDate;
	}

	public int getBillingDate() {
		return billingDate;
	}

	public static void setBillingDate(int billingDate) {
		CardTransPojo.billingDate = billingDate;
	}

	public int getCardPIN() {
		return cardPIN;
	}

	public static void setCardPIN(int cardPIN) {
		CardTransPojo.cardPIN = cardPIN;
	}

	public int getCreditLimit() {
		return creditLimit;
	}

	public static void setCreditLimit(int creditLimit) {
		CardTransPojo.creditLimit = creditLimit;
	}


	public void setAllColumns(String lineText) throws Exception {
		String[] data = lineText.split(",");
		
        setCardTypeCode(data[0]);
        setCardTypeFullName(data[1]);
        setIssuingBank(data[2]);
        setCardNumber(Long.parseLong(data[3]));
        setCardHolderName(data[4]);
        setCvvCvv2(Integer.parseInt(data[5]));
        String issueDate = data[6]; //date
        
        SimpleDateFormat sDF1 = new SimpleDateFormat("MM/yyyy");
        java.util.Date date1 = sDF1.parse(issueDate);
        java.sql.Date sqlIssueDate = new java.sql.Date(date1.getTime());
        setSqlIssueDate(sqlIssueDate);
        
        String expiryDate = data[7]; //date
        SimpleDateFormat sDF2 = new SimpleDateFormat("MM/yyyy");
        java.util.Date date2 = sDF2.parse(expiryDate);
        java.sql.Date sqlExpiryDate = new java.sql.Date(date2.getTime());
        setSqlExpiryDate(sqlExpiryDate);
        
        
        setBillingDate(Integer.parseInt(data[8]));
        setCardPIN(Integer.parseInt(data[9]));
        setCreditLimit(Integer.parseInt(data[10]));

	}

}
