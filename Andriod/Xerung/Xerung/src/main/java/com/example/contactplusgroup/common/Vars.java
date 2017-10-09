package com.example.contactplusgroup.common;

import java.util.HashMap;

public class Vars {
	
	public static HashMap<Integer,String> webMethodName = new HashMap<Integer ,String>();
	public static final String SHARED_PREFS_FILE  = "dash.db";
	//public static final String gateKeperHit       = "http://192.168.1.120:8084/MIPHONEDIRGATEKEEPER/webresources/gatekeeper/AND";
	public static boolean onAdd = false;
	public static boolean onSync = false;
	//public static boolean onDB = false;
	public static final String gateKeperHit       = "http://www.xerung.com/MIPHONEDIRGATEKEEPER/webresources/gatekeeper/AND";


	public static String gPhoto = null;
	public static int gMCount = 0;
	public static String gUID = null;
	public static String gMadeByNumber = null;
	public static String gAdmin = null;
	public static boolean onUpadte = false;

	public static final String URL_SMS = "https://www.medicosa.com/SMSService/rs/sms";
	public static final String DEVICE_TOKEN_ID = "TOKEN";
	// special character to prefix the otp. Make sure this character appears only once in the sms
}
