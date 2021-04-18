package dataEngine;


import configuration.SystemConfigProp;

public class FrameworkEngine {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			SystemConfigProp systemconfig = SystemConfigProp.getConfig();
			
		}catch (Exception e) {
			System.out.println(e);
		}
	    
	}

}
