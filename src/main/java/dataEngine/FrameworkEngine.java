package dataEngine;


import configuration.SystemConfigProp;
import sourceBasedFlow.CardTransactionTable;

public class FrameworkEngine {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			SystemConfigProp systemconfig = SystemConfigProp.getConfig();
			String fileName = args[0];
			String tableName = args[1];

			switch(tableName) {
			case "CardTransaction" :
				CardTransactionTable.fileLoad(fileName, systemconfig);
				break;
			default :
				System.out.println("Given Table Name is not Matching with any of our Source");
			}

		}catch (Exception e) {
			System.out.println(e);
		}

	}

}
