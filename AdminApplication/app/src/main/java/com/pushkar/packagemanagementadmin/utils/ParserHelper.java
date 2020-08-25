package com.pushkar.packagemanagementadmin.utils;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class ParserHelper {
    private static final String TAG = ParserHelper.class.getSimpleName();
    public static ArrayList<String> getListOfParsedBarcodes(String message){
        ArrayList<String> barcodesList = new ArrayList<>();
        String[] messageArray = message.replaceAll("\\p{P}", "").split(" ");
        for (String word:
                messageArray) {
            if(word.matches("^\\w{1}\\d{8}$")){
                Log.d(TAG, "Parsed barcode is: "+word);
                barcodesList.add(word);
            }
        }
        return barcodesList;
    }

    public static String getNameFromTitle(String title){
        String result = "";
        if(title.length() > 9){
            result = title.replace("Customer ","").replace(" at store.","");
        }
        return result;
    }

    public static boolean timedOut(String lastLogInTimeStr){
        if(lastLogInTimeStr.equalsIgnoreCase("0"))
            return true;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss", Locale.CANADA);
        try {
            Date lastLogInDate = simpleDateFormat.parse(lastLogInTimeStr);
            Date now = new Date();
            return !(hoursDifference(now,lastLogInDate) <= 23);
        } catch (ParseException e) {
            return true;
        }
    }

    private static long hoursDifference(Date date1, Date date2) {
        long result = TimeUnit.MILLISECONDS.toHours(date1.getTime()-date2.getTime());
        return result;
    }

    public static String getStringFromDate(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss", Locale.CANADA);
        return simpleDateFormat.format(date);
    }
}
