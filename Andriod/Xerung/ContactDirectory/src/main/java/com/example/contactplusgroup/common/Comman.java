package com.example.contactplusgroup.common;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import com.example.contactplusgroup.widgets.DialogDismiss;
import com.mityung.base64.Base64;
import com.mityung.contactdirectory.ContactSync;
import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

public class Comman {
	
	public Comman() {
		addWebServiceMethod();
	}

	public boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null;
	}
	
	public String secretKey = "FSAAAAA111abc4567F14DLn3JyKXRA8h==";
	
	public void alertDialog(Context context, String title, String msg) {
		DialogDismiss dialog = new DialogDismiss(context, title, msg);
		dialog.setOnAcceptButtonClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				//Register.this.finish();
			}
		});
		dialog.show();
	}
	
	
	
	public void hideSoftKeyBoard(Context context, View view){		
		if (view != null) {  
		    InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
		    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	}
	
	

	/**
	 * Used to encrypt the data
	 * @param text take input of text 
	 * @param secretKey take input of key from which data will encrypt
	 * @return return encrypt data
	 */
	public  String symmetricEncrypt(String text, String secretKey) {
		byte[] raw;
		String encryptedString;
		SecretKeySpec skeySpec;
		byte[] encryptText = text.getBytes();
		Cipher cipher;
		try {			
			raw = Base64.decodeBase64(secretKey);
			skeySpec = new SecretKeySpec(raw, "AES");
			cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
			encryptedString = Base64.encodeBase64String(cipher.doFinal(encryptText));
		} 
		catch (Exception e) {
			e.printStackTrace();
			return "Error";
		}
		return encryptedString;
	}
	/**
	 * Used to Decrypt the data
	 * @param text take input of text to be decrypt
	 * @param secretKey take input of key
	 * @return return decrypt data
	 */
	public  String symmetricDecrypt(String text, String secretKey) {
		Cipher cipher;
		String encryptedString;
		byte[] encryptText = null;
		byte[] raw;
		SecretKeySpec skeySpec;
		try {
			raw = Base64.decodeBase64(secretKey);
			skeySpec = new SecretKeySpec(raw, "AES");
			encryptText = Base64.decodeBase64(text);
			cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, skeySpec);
			encryptedString = new String(cipher.doFinal(encryptText));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return encryptedString;
	}

	public static String replaceLastFive(String s) {
		int length = s.length();
		//Check whether or not the string contains at least four characters; if not, this method is useless
		if (length < 5)
			return "Error: The provided string is not greater than four characters long.";
		return s.substring(0, length - 5) + "*****";
	}
	
	
	/**
	 * Used to Decrypt the data
	 * @param text take input of text to be decrypt
	 * @param secretKey take input of key
	 * @return return decrypt data
	 */
	public  StringBuffer symmetricDecryptBuffer(String text, String secretKey) {
		Cipher cipher;
		StringBuffer encryptedString = null;
		byte[] encryptText = null;
		byte[] raw;
		SecretKeySpec skeySpec;
		try {
			raw = Base64.decodeBase64(secretKey);
			skeySpec = new SecretKeySpec(raw, "AES");
			encryptText = Base64.decodeBase64(text);
			cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, skeySpec);
			encryptedString = new StringBuffer(""+cipher.doFinal(encryptText));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return encryptedString;
	}
	
	public int getAppVersionCode(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			// should never happen
			throw new RuntimeException("Could not get package name: " + e);
		}
	}
	
	public boolean isTabletDevice(Context activityContext) {
        boolean device_large = ((activityContext.getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK) ==
                Configuration.SCREENLAYOUT_SIZE_LARGE);

        if (device_large) {
            DisplayMetrics metrics = new DisplayMetrics();
            Activity activity = (Activity) activityContext;
            activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

            if (metrics.densityDpi == DisplayMetrics.DENSITY_DEFAULT
                    || metrics.densityDpi == DisplayMetrics.DENSITY_HIGH
                    || metrics.densityDpi == DisplayMetrics.DENSITY_MEDIUM
                    || metrics.densityDpi == DisplayMetrics.DENSITY_TV
                    || metrics.densityDpi == DisplayMetrics.DENSITY_XHIGH) {
                return true;
            }
        }
        return false;
    }
	
	public String getDateFromFieldWithoutMonthZero_(String date){
		try {
			ArrayList<String> d  = splitString(date, " ");
			int year             = Integer.parseInt(d.get(2));
			String mo            = d.get(1);
			int day              = Integer.parseInt(d.get(0));
			String m            = changeWordInMonthWithZero_(mo);
			String days = String.valueOf(day);
			if(days.length()<2){
				days = "0"+days;
			}
			String dat = days+"/"+m+"/"+year ;
			return dat;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}

	}
	
	
	
	public String changeWordInMonthWithZero_(String month){
		String mm = "0";
		if (month.equals("January")) {
			mm = "01";
		} else if (month.equals("February")) {
			mm = "02";
		} else if (month.equals("March")) {
			mm = "03";
		} else if (month.equals("April")) {
			mm = "04";
		} else if (month.equals("May")) {
			mm = "05";
		} else if (month.equals("June")) {
			mm = "06";
		} else if (month.equals("July")) {
			mm = "07";
		} else if (month.equals("August")) {
			mm = "08";
		} else if (month.equals("September")) {
			mm = "09";
		} else if (month.equals("October")) {
			mm = "10";
		} else if (month.equals("November")) {
			mm = "11";
		} else if (month.equals("December")) {
			mm = "12";
		}
		return mm;
	}
	
	public String setDateFormatInMonth(String date){
		try {
			ArrayList<String> d  	= splitString(date, "/");
			int day              	= Integer.parseInt(d.get(0));
			int mo            		= Integer.parseInt(d.get(1));
			int year             	= Integer.parseInt(d.get(2));
			String dat = day+" "+changeMonthInWord(mo)+" "+year ;
			return dat;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}

	}
	
	
	public void openSetting(Context context){
		try {
			context.startActivity(new Intent(Settings.ACTION_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(context, "Unable to Open Settings", Toast.LENGTH_SHORT).show();
		}
	}
	
	public ArrayList<String> setInitDate(String dat) {

		String[] m = dat.split(" ");
		String[] d = m[0].split("-");

		int year = 0;
		int month = 0;
		int day = 0;
		try {
			year = Integer.parseInt(d[0].trim());
			month = Integer.parseInt(d[1].trim());
			day = Integer.parseInt(d[2].trim());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			// Log.e("Error", "Error in break data "+e.getMessage());

			return null;
		}

		String value = day + " " + changeMonthInWord(month) + " " + year;
		String time = m[1].substring(0, 8);

		ArrayList<String> a = new ArrayList<String>();
		a.add(value);
		a.add(time);
		return a;
		// displayDateInField(year, month, day);
	}

	public String changeMonthInWord(int month) {
		String mm = "";
		if (month == 1) {
			mm = "January";
		} else if (month == 2) {
			mm = "February";
		} else if (month == 3) {
			mm = "March";
		} else if (month == 4) {
			mm = "April";
		} else if (month == 5) {
			mm = "May";
		} else if (month == 6) {
			mm = "June";
		} else if (month == 7) {
			mm = "July";
		} else if (month == 8) {
			mm = "August";
		} else if (month == 9) {
			mm = "September";
		} else if (month == 10) {
			mm = "October";
		} else if (month == 11) {
			mm = "November";
		} else if (month == 12) {
			mm = "December";
		}
		return mm;
	}
	


	public ArrayList<String> splitString(String data, String key) {
		try {
			ArrayList<String> arry = new ArrayList<String>();
			String[] data1 = data.split(key);

			for (String da : data1) {
				arry.add(da.trim());

			}
			return arry;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}

	}

	public String getDateFromField(String date) {
		try {
			ArrayList<String> d = splitString(date, " ");
			int year = Integer.parseInt(d.get(0));
			String mo = d.get(1);
			int day = Integer.parseInt(d.get(2));
			String Day1 = String.valueOf(year);
			int month = changeWordInMonth(mo);
			String m = String.valueOf(month);
			if(Day1.length()<2)
				Day1 = "0"+ Day1;
			if (m.length() < 2)
				m = "0" + m;
			String dat = day + "-" + m + "-" + Day1;
			return dat;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}

	}

	public int changeWordInMonth(String month) {
		int mm = 0;
		if (month.equals("January")) {
			mm = 1;
		} else if (month.equals("February")) {
			mm = 2;
		} else if (month.equals("March")) {
			mm = 3;
		} else if (month.equals("April")) {
			mm = 4;
		} else if (month.equals("May")) {
			mm = 5;
		} else if (month.equals("June")) {
			mm = 6;
		} else if (month.equals("July")) {
			mm = 7;
		} else if (month.equals("August")) {
			mm = 8;
		} else if (month.equals("September")) {
			mm = 9;
		} else if (month.equals("October")) {
			mm = 10;
		} else if (month.equals("November")) {
			mm = 11;
		} else if (month.equals("December")) {
			mm = 12;
		}
		return mm;
	}
	
	public String convertByteTo(long byteData){
		NumberFormat formatter = NumberFormat.getInstance();
		formatter.setMaximumFractionDigits(2);
		try {
			double kb = byteData/1024L;
			if(kb >= 1024){
				double mb = kb/1024L;
				if(mb >= 1024){
					double gb = mb/1024L;
					return formatter.format(gb) +" GB";
				}else
					return  formatter.format(mb)+" MB";
			}else{				
				return formatter.format(kb)+" KB";
			}

		} catch (Exception e) {
			// TODO: handle exception
			return String.valueOf(byteData);
		}
	}
	

	
	public String getAppVersionName(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionName;
		} catch (NameNotFoundException e) {
			// should never happen
			throw new RuntimeException("Could not get package name: " + e);
		}
	}
	
	public String getCurrentDate(){

		Calendar c = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String formattedDate = df.format(c.getTime());	 
		String [] date = formattedDate.split(" ");
		String [] d = date[0].split("-");
		int year = 0;
		int month = 0;
		int day = 0;
		try {
			year = Integer.parseInt(d[0].trim());
			month = Integer.parseInt(d[1].trim());
			day = Integer.parseInt(d[2].trim());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			// Log.e("Error", "Error in break data "+e.getMessage());

			return null;
		}

		String value = day + " " + changeMonthInWord(month) + " " + year;
		return value ;
	}
	
	public ArrayList<String> convertDateToMonth(String dat) {

		String[] m = dat.split(" ");
		String[] d = m[0].split("-");

		int year = 0;
		int month = 0;
		int day = 0;
		try {
			year  = Integer.parseInt(d[0].trim());
			month = Integer.parseInt(d[1].trim());
			day   = Integer.parseInt(d[2].trim());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			// Log.e("Error", "Error in break data "+e.getMessage());

			return null;
		}

		String value = day + " " + changeMonthInWord(month) + " " + year;
		String time = m[1].substring(0, 8);

		ArrayList<String> a = new ArrayList<String>();
		a.add(value);
		a.add(time);
		return a;
		// displayDateInField(year, month, day);
	}
	
	public int monthDiff(String sdate ,String edate,String dateFormat){
        int diffMonth=0;
         SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
         try {
         Date ssdate = formatter.parse(sdate);
         Date eedate = formatter.parse(edate);
            Calendar startCalendar = new GregorianCalendar();
            startCalendar.setTime(ssdate);
            Calendar endCalendar = new GregorianCalendar();
            endCalendar.setTime(eedate);
            int diffYear = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
            diffMonth = diffYear * 12 + endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);
            System.out.println("diffMonth  "+diffMonth);
             return  diffMonth;
         
        } catch (Exception e) {
        e.printStackTrace();
        return  diffMonth;
 }
    }
   

	public String getCurrentDateCal(){

		Calendar c = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String formattedDate = df.format(c.getTime());	 
		String [] date = formattedDate.split(" ");
		String nDate = (String)date[0];
		return formattedDate ;
	}
	
	public String getLargerTimeStamp(String fromtime, String totime){
		SimpleDateFormat defaultDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date datefrom;
		Date dateTo;
		String largeDate = "";
		try {
			datefrom = defaultDateFormat.parse(fromtime);
			dateTo = defaultDateFormat.parse(totime);
			if(datefrom.compareTo(dateTo)>0){
				System.out.println("Date1 is after Date2");
				largeDate = defaultDateFormat.format(datefrom);
			}else if(datefrom.compareTo(dateTo)<0){
				System.out.println("Date1 is before Date2");
				largeDate = defaultDateFormat.format(dateTo);
			}else if(datefrom.compareTo(dateTo)==0){
				System.out.println("Date1 is equal to Date2");
				largeDate = defaultDateFormat.format(dateTo);
			}else{
				System.out.println("How to get there");
				largeDate = defaultDateFormat.format(dateTo);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return largeDate;
	}
	
	/**
	 * Used to check email pattern is correct or incorrect
	 * @param email take email as string input
	 * @return return true if string is a email id or false if string is not a email id 
	 */
	public boolean emailValidator(String email) {
		Pattern pattern;
		Matcher matcher;
		final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		pattern = Pattern.compile(EMAIL_PATTERN);
		matcher = pattern.matcher(email);		
		return matcher.matches();
	}
	
	public String encodeTobase64(Bitmap image) {
		Bitmap immage = image;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		immage.compress(Bitmap.CompressFormat.PNG, 100, baos);
		byte[] b = baos.toByteArray();
		String imageEncoded = android.util.Base64.encodeToString(b, android.util.Base64.DEFAULT);
		return imageEncoded;
	}
	
	public Bitmap decodeBase64(String input) {
		try {
			byte[] encodeByte = android.util.Base64.decode(input, 0);
			Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0,encodeByte.length);
			return bitmap;
		} catch (Exception e) {
			e.getMessage();
			return null;
		}
	}
	
	public Uri getImageUri(Context inContext, Bitmap inImage) {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
		String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
		return Uri.parse(path);
	}
	
	public String encodeBase64Text(String data){	


		byte[] encoded = org.apache.commons.codec.binary.Base64.encodeBase64(data.getBytes());      

		return new String(encoded);
	}
	
	@SuppressWarnings("deprecation")
	public String compressImage(Context context,String imageUri) {

		String filePath = getRealPathFromURI(context,imageUri);
		Bitmap scaledBitmap = null;
		BitmapFactory.Options options = new BitmapFactory.Options();

		// by setting this field as true, the actual bitmap pixels are not
		// loaded in the memory. Just the bounds are loaded. If
		// you try the use the bitmap here, you will get null.
		options.inJustDecodeBounds = true;
		Bitmap bmp = BitmapFactory.decodeFile(filePath, options);
		int actualHeight = options.outHeight;
		int actualWidth = options.outWidth;
		// max Height and width values of the compressed image is taken as
		// 816x612
		float maxHeight = 816.0f;
		float maxWidth = 612.0f;
		float imgRatio = actualWidth / actualHeight;
		float maxRatio = maxWidth / maxHeight;
		// width and height values are set maintaining the aspect ratio of the
		// image
		if (actualHeight > maxHeight || actualWidth > maxWidth) {
			if (imgRatio < maxRatio) {
				imgRatio = maxHeight / actualHeight;
				actualWidth = (int) (imgRatio * actualWidth);
				actualHeight = (int) maxHeight;
			} else if (imgRatio > maxRatio) {
				imgRatio = maxWidth / actualWidth;
				actualHeight = (int) (imgRatio * actualHeight);
				actualWidth = (int) maxWidth;
			} else {
				actualHeight = (int) maxHeight;
				actualWidth = (int) maxWidth;

			}
		}

		// setting inSampleSize value allows to load a scaled down version of
		// the original image

		options.inSampleSize = calculateInSampleSize(options, actualWidth,
				actualHeight);

		// inJustDecodeBounds set to false to load the actual bitmap
		options.inJustDecodeBounds = false;

		// this options allow android to claim the bitmap memory if it runs low
		// on memory
		options.inPurgeable = true;
		options.inInputShareable = true;
		options.inTempStorage = new byte[16 * 1024];

		try {
			// load the bitmap from its path
			bmp = BitmapFactory.decodeFile(filePath, options);
		} catch (OutOfMemoryError exception) {
			exception.printStackTrace();

		}
		try {
			scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight,
					Bitmap.Config.ARGB_8888);
		} catch (OutOfMemoryError exception) {
			exception.printStackTrace();
		}

		float ratioX = actualWidth / (float) options.outWidth;
		float ratioY = actualHeight / (float) options.outHeight;
		float middleX = actualWidth / 2.0f;
		float middleY = actualHeight / 2.0f;

		Matrix scaleMatrix = new Matrix();
		scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

		Canvas canvas = new Canvas(scaledBitmap);
		canvas.setMatrix(scaleMatrix);
		canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2,middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));
		// check the rotation of the image and display it properly
		ExifInterface exif;
		try {
			exif = new ExifInterface(filePath);
			int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
			Matrix matrix = new Matrix();
			if (orientation == 6) {
				matrix.postRotate(90);
			} else if (orientation == 3) {
				matrix.postRotate(180);
			} else if (orientation == 8) {
				matrix.postRotate(270);
			}
			scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,true);
		} catch (IOException e) {
			e.printStackTrace();
		}

		FileOutputStream out = null;
		String filename = getFilename();
		try {
			out = new FileOutputStream(filename);
			scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return filename;

	}
	
	public String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "MyFolder/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;

    }
	
	
    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height/ (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;      }       final float totalPixels = width * height;       final float totalReqPixelsCap = reqWidth * reqHeight * 2;       while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    public String getRealPathFromURI(Context context, String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = context.getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }
    
    public String random(int size) {

        StringBuilder generatedToken = new StringBuilder();
        try {
            SecureRandom number = SecureRandom.getInstance("SHA1PRNG");
            // Generate 20 integers 0..20
            for (int i = 0; i < size; i++) {
                generatedToken.append(number.nextInt(9));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return generatedToken.toString();
    }
	
	public void addWebServiceMethod() {
		Vars.webMethodName.put(0, "RegLogin");   //0 for registration 1 for update  				
		Vars.webMethodName.put(1, "SendOTP");      				
		Vars.webMethodName.put(2, "Mibackupsend");
		Vars.webMethodName.put(3, "Mibackupfetch");
		Vars.webMethodName.put(4, "fetchCity");
		Vars.webMethodName.put(5, "fetchProfile");
		Vars.webMethodName.put(6, "GroupService");
		Vars.webMethodName.put(7, "FetchGroupDelta");
		Vars.webMethodName.put(8, "sendINV");
		Vars.webMethodName.put(9, "acceptINV");
		Vars.webMethodName.put(10, "fetchINV");
		Vars.webMethodName.put(11, "GroupSearch");
		Vars.webMethodName.put(12, "updatePhotoandAccess");
		Vars.webMethodName.put(13, "saveID");
		Vars.webMethodName.put(14, "sendNotiMSG");
		Vars.webMethodName.put(15, "DeleteMember");
	}
	
	public void callPhone(String number,Context context){
		 Intent callIntent = new Intent(Intent.ACTION_CALL);
	     callIntent.setData(Uri.parse("tel:" + number));
		 context.startActivity(callIntent);
	}
	
	public void callEmail(Context context,String recipient){
		Intent i = new Intent(Intent.ACTION_SEND);
		i.setType("message/rfc822");
		i.putExtra(Intent.EXTRA_EMAIL  , new String[]{recipient});
		i.putExtra(Intent.EXTRA_SUBJECT, "");
		i.putExtra(Intent.EXTRA_TEXT, "");
		try {
		    context.startActivity(Intent.createChooser(i, "Send mail..."));
		} catch (android.content.ActivityNotFoundException ex) {
		    Toast.makeText(context, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
		}
	}

	public boolean isNumeric(String s) {
		return s.matches("[-+]?\\d*\\.?\\d+");
	}

	public void addNewContact(Context context,String DisplayName,String MobileNumber,String WorkNumber,
			 String emailID ,String company, String jobTitle  ){
		
//		 String DisplayName = "abcde";
//		 String MobileNumber = "123456";
//		// String HomeNumber = "1111";
//		 String WorkNumber = "2222";
//		 String emailID = "adb@nomail.com";
//		 String company = "Company Name";
//		 String jobTitle = "Job Title";

		 ArrayList < ContentProviderOperation > ops = new ArrayList < ContentProviderOperation > ();

		 ops.add(ContentProviderOperation.newInsert(
		 ContactsContract.RawContacts.CONTENT_URI)
		     .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
		     .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
		     .build());

		 //------------------------------------------------------ Names
		 if (DisplayName != null) {
		     ops.add(ContentProviderOperation.newInsert(
		     ContactsContract.Data.CONTENT_URI)
		         .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
		         .withValue(ContactsContract.Data.MIMETYPE,
		     ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
		         .withValue(
		     ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
		     DisplayName).build());
		 }

		 //------------------------------------------------------ Mobile Number                     
		 if (MobileNumber != null) {
		     ops.add(ContentProviderOperation.
		     newInsert(ContactsContract.Data.CONTENT_URI)
		         .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
		         .withValue(ContactsContract.Data.MIMETYPE,
		     ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
		         .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, MobileNumber)
		         .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
		     ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
		         .build());
		 }

		 //------------------------------------------------------ Home Numbers
//		 if (HomeNumber != null) {
//		     ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
//		         .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
//		         .withValue(ContactsContract.Data.MIMETYPE,
//		     ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
//		         .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, HomeNumber)
//		         .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
//		     ContactsContract.CommonDataKinds.Phone.TYPE_HOME)
//		         .build());
//		 }

		 //------------------------------------------------------ Work Numbers
		 if (WorkNumber != null) {
		     ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
		         .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
		         .withValue(ContactsContract.Data.MIMETYPE,
		     ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
		         .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, WorkNumber)
		         .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
		     ContactsContract.CommonDataKinds.Phone.TYPE_WORK)
		         .build());
		 }

		 //------------------------------------------------------ Email
		 if (emailID != null) {
		     ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
		         .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
		         .withValue(ContactsContract.Data.MIMETYPE,
		     ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
		         .withValue(ContactsContract.CommonDataKinds.Email.DATA, emailID)
		         .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
		         .build());
		 }

		 //------------------------------------------------------ Organization
		 if (company !=null && jobTitle != null) {
		     ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
		         .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
		         .withValue(ContactsContract.Data.MIMETYPE,
		     ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)
		         .withValue(ContactsContract.CommonDataKinds.Organization.COMPANY, company)
		         .withValue(ContactsContract.CommonDataKinds.Organization.TYPE, ContactsContract.CommonDataKinds.Organization.TYPE_WORK)
		         .withValue(ContactsContract.CommonDataKinds.Organization.TITLE, jobTitle)
		         .withValue(ContactsContract.CommonDataKinds.Organization.TYPE, ContactsContract.CommonDataKinds.Organization.TYPE_WORK)
		         .build());
		 }

		 // Asking the Contact provider to create a new contact                 
		 try {
			 context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
		 } catch (Exception e) {
		     e.printStackTrace();
		     Toast.makeText(context, "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
		 } 
	}


	public void readContacts(Context context) {
		try {
			ContentResolver cr = context.getContentResolver();
			Cursor cur         = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
			JSONArray jArray   = new JSONArray();
			//Log.e("Sync","Count == "+cur.getCount());
			int count = 0;
			if (cur.getCount() > 0) {
				while (cur.moveToNext()) {
					//Log.e("Sync","=========================Count================="+count);
					++count;
					JSONObject jbContact = new JSONObject();
					String id   = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
					String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
					if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
						//Log.e("Sync","name : " + name + ", ID : " + id);
						jbContact.put("name", name);
						jbContact.put("id", id);
						Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
								ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",new String[]{id}, null);
						StringBuffer output = new StringBuffer();
						while (pCur.moveToNext()) {
							String phone = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
							String mphone = phone.replaceAll("\\s+","").replaceAll("\\-", "").replaceAll("\\(", "").replaceAll("\\)", "").replaceAll("\\#", "").replaceAll("\\_", "");
							output.append("\n Phone number:" + mphone);
							//Log.e("Sync","Phone number "+phone);
						}
						pCur.close();

						//Log.e("Sync","-----------------Phone-------------");
						Cursor emailCur = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,null,
								ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",new String[]{id}, null);
						while (emailCur.moveToNext()) {
							// This would allow you get several email addresses
							// if the email addresses were stored in an array
							String email = emailCur.getString(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
							String emailType = emailCur.getString(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));
							//Log.e("Sync","Email " + email + " Email Type : " + emailType);
						}
						emailCur.close();
						//Log.e("Sync", "-----------------Email-------------");

						// Get note.......
						String noteWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
						String[] noteWhereParams = new String[]{id,ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE};
						Cursor noteCur = cr.query(ContactsContract.Data.CONTENT_URI, null, noteWhere, noteWhereParams, null);
						if (noteCur.moveToFirst()) {
							String note = noteCur.getString(noteCur.getColumnIndex(ContactsContract.CommonDataKinds.Note.NOTE));
							//Log.e("Sync","Note " + note);
						}
						noteCur.close();
						//Log.e("Sync", "-----------------Note-------------");

						//Get Postal Address....
						String addrWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
						String[] addrWhereParams = new String[]{id,ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE};
						Cursor addrCur = cr.query(ContactsContract.Data.CONTENT_URI,null, null, null, null);
						while(addrCur.moveToNext()) {
							String poBox = addrCur.getString(
									addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POBOX));
							String street = addrCur.getString(
									addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.STREET));
							String city = addrCur.getString(
									addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.CITY));
							String state = addrCur.getString(
									addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.REGION));
							String postalCode = addrCur.getString(
									addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE));
							String country = addrCur.getString(
									addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY));
							String type = addrCur.getString(
									addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.TYPE));
							//Log.e("", "Note " + "poBox: " + poBox + "street: " + street + "city: " + city);
							// Do something with these....

						}
						addrCur.close();
						//Log.e("Sync", "-----------------Address-------------");

						// Get Instant Messenger.........
						String imWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
						String[] imWhereParams = new String[]{id,ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE};
						Cursor imCur = cr.query(ContactsContract.Data.CONTENT_URI,null, imWhere, imWhereParams, null);
						if (imCur.moveToFirst()) {
							String imName = imCur.getString(imCur.getColumnIndex(ContactsContract.CommonDataKinds.Im.DATA));
							String imType = imCur.getString(imCur.getColumnIndex(ContactsContract.CommonDataKinds.Im.TYPE));
							//Log.e("Sync","imName "+imName+" imType "+imType);
						}
						imCur.close();
						//Log.e("Sync", "-----------------IMTYPE-------------");

						// Get Organizations.........
						String orgWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
						String[] orgWhereParams = new String[]{id,ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE};
						Cursor orgCur = cr.query(ContactsContract.Data.CONTENT_URI,null, orgWhere, orgWhereParams, null);
						if (orgCur.moveToFirst()) {
							String orgName = orgCur.getString(orgCur.getColumnIndex(ContactsContract.CommonDataKinds.Organization.DATA));
							String title = orgCur.getString(orgCur.getColumnIndex(ContactsContract.CommonDataKinds.Organization.TITLE));
							//Log.e("Sync","OrgName "+orgName +" title "+title);
						}
						orgCur.close();
						//Log.e("Sync", "-----------------Title-------------");
					}
				}
			}
		}catch (Exception e){
			//Log.e("Error in Backup","Backup "+e.getMessage());
			e.printStackTrace();
		}
		Intent i = new Intent(context, ContactSync.class);
		context.stopService(i);
	}


	public int checkValidNumber(String number){
		if(number.startsWith("+")){
			return 0;
		}else if(number.startsWith("0")){
			return 1;
		}else if(number.startsWith("00")){
			return 0;
		}else{
			return 1;
		}
	}

	/*public String upperCaseAllFirst(String value) {
		try{
			char[] array = value.toCharArray();
			// Uppercase first letter.
			array[0] = Character.toUpperCase(array[0]);

			// Uppercase all letters that follow a whitespace character.
			for (int i = 1; i < array.length; i++) {
				if (Character.isWhitespace(array[i - 1])) {
					array[i] = Character.toUpperCase(array[i]);
				}
			}
			return new String(array);
		}catch (Exception e) {
			// TODO: handle exception
			Log.e("dadaad", "sddderoor" + e.getMessage());
			e.printStackTrace();
			return "";
		}

	}

	*/

	public String upperCaseAllFirst(String data ){

		try {
			ArrayList<String> value = splitString(data, " ");
			String name = "";
			for(String d : value){
				if(d.length()>3){
					String result = d.substring(0,1).toUpperCase()+d.substring(1, d.length()).toLowerCase()+" ";
					name = name.concat(result);
				}else{
					String result = d.toUpperCase()+" ";
					name = name.concat(result);
				}
			}
			return name;
		} catch (Exception e) {
			// TODO: handle exception
			return data;
		}
	}

	public String trimEnd(String s)
	{
		if ( s == null || s.length() == 0 )
			return s;
		int i = s.length();
		while ( i > 0 &&  Character.isWhitespace(s.charAt(i - 1)) )
			i--;
		if ( i == s.length() )
			return s;
		else
			return s.substring(0, i);
	}
}

