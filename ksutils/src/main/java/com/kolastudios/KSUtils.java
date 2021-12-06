package com.kolastudios;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Spinner;

import com.pixplicity.easyprefs.library.Prefs;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.regex.Pattern;

public class KSUtils {
    private static String mLogTag;
    private static boolean mLogEnabled = false;
    private static Context mContext;

    public static DBHandler dbHandler;

    private static void initUtils(){
        KSUtils.log("initUtils");

        new Prefs.Builder()
                .setContext(mContext)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .build();

        dbHandler = DBHandler.getInstance(mContext);
    }

    public static void log(String msg){
        if(!mLogEnabled || TextUtils.isEmpty(msg) || TextUtils.isEmpty(mLogTag)){
            return;
        }

        int maxLogSize = 1000;

        for(int i = 0; i <= msg.length() / maxLogSize; i++) {
            int start = i * maxLogSize;
            int end = (i + 1) * maxLogSize;
            end = end > msg.length() ? msg.length() : end;
            Log.d(mLogTag, msg.substring(start, end));
        }
    }

    public static void logE(String msg){
        if(!mLogEnabled || TextUtils.isEmpty(msg) || TextUtils.isEmpty(mLogTag)){
            return;
        }

        Log.e(mLogTag, msg);
    }

    public static String md5(String s){
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            StringBuffer hexString = new StringBuffer();
            for(int i=0; i<messageDigest.length; i++){
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            }

            return hexString.toString();
        }catch(NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static String getInitials(String name) {
        String initials = "";
        String[] words = name.split(" ");
        for(String s: words){
            initials += s.charAt(0);
        }

        return initials.toUpperCase(Locale.US);
    }

    public static boolean isValidEmail(String email){
        if(TextUtils.isEmpty(email)){
            return false;
        }

        String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        return Pattern.compile(EMAIL_PATTERN).matcher(email).matches();
    }

    public static void printFacebookKeyHash(String namespace){
        try {
            PackageInfo info = mContext.getPackageManager().getPackageInfo(namespace, PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
            }
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }
    }

    public static long getVersionCode(){
        try {
            PackageInfo pInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
            return pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            logE("Error getting version code -> " + e.getMessage());
            e.printStackTrace();
        }

        return 0;
    }

    public static String getVersionName(){
        try {
            PackageInfo pInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
            return pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            logE("Error getting version code -> " + e.getMessage());
            e.printStackTrace();
        }

        return "";
    }

    public static String formatDate(int year, int month, int day, String format){
        Calendar c = new GregorianCalendar(year, month, day);
        Date date = new Date(c.getTimeInMillis());
        return new SimpleDateFormat(format).format(date);
    }

    public static String truncateString(String s, int maxCharatcers){
        if(s.length() > maxCharatcers){
            s = s.substring(0, maxCharatcers) + "...";
        }

        return s;
    }

    public static Date getDate(String dateString){
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        try{
            d = sdf.parse(dateString);
        }catch (Exception ex){
            KSUtils.log("Error parsing date -> " + ex.getMessage());
        }
        return d;
    }

    public static void hideKeyboard(Activity cxt){
        View view = cxt.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)cxt.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static int getSpinnerItemIndex(Spinner spinner, String myString){
        int index = 0;
        for(int i = 0; i < spinner.getCount(); i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                index = i;
                break;
            }
        }
        return index;
    }

    public static String getFormattedPrice(long price){
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        return formatter.format(price);
    }

    public static String getFormattedPrice(float price){
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        return formatter.format(price);
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getWindow().getDecorView().getRootView()
                .getWindowToken(), 0);
    }

    public static String getCurrentDateFormatted(){
        GregorianCalendar calendar = new GregorianCalendar();
        return formatDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), "d MMM, yyyy HH:mm");
    }

    public static String[] getCountries(){
        String[] countries = new String[]{"Afghanistan", "Albania", "Algeria", "American Samoa", "Andorra", "Angola", "Anguilla", "Antarctica", "Antigua and Barbuda", "Argentina", "Armenia", "Aruba", "Australia", "Austria", "Azerbaijan", "Bahamas", "Bahrain", "Bangladesh", "Barbados", "Belarus", "Belgium", "Belize", "Benin", "Bermuda", "Bhutan", "Bolivia", "Bosnia and Herzegowina", "Botswana", "Bouvet Island", "Brazil", "British Indian Ocean Territory", "Brunei Darussalam", "Bulgaria", "Burkina Faso", "Burundi", "Cambodia", "Cameroon", "Canada", "Cape Verde", "Cayman Islands", "Central African Republic", "Chad", "Chile", "China", "Christmas Island", "Cocos (Keeling) Islands", "Colombia", "Comoros", "Congo", "Congo, the Democratic Republic of the", "Cook Islands", "Costa Rica", "Cote d'Ivoire", "Croatia (Hrvatska)", "Cuba", "Cyprus", "Czech Republic", "Denmark", "Djibouti", "Dominica", "Dominican Republic", "East Timor", "Ecuador", "Egypt", "El Salvador", "Equatorial Guinea", "Eritrea", "Estonia", "Ethiopia", "Falkland Islands (Malvinas)", "Faroe Islands", "Fiji", "Finland", "France", "France Metropolitan", "French Guiana", "French Polynesia", "French Southern Territories", "Gabon", "Gambia", "Georgia", "Germany", "Ghana", "Gibraltar", "Greece", "Greenland", "Grenada", "Guadeloupe", "Guam", "Guatemala", "Guinea", "Guinea-Bissau", "Guyana", "Haiti", "Heard and Mc Donald Islands", "Holy See (Vatican City State)", "Honduras", "Hong Kong", "Hungary", "Iceland", "India", "Indonesia", "Iran (Islamic Republic of)", "Iraq", "Ireland", "Israel", "Italy", "Jamaica", "Japan", "Jordan", "Kazakhstan", "Kenya", "Kiribati", "Korea, Democratic People's Republic of", "Korea, Republic of", "Kuwait", "Kyrgyzstan", "Lao, People's Democratic Republic", "Latvia", "Lebanon", "Lesotho", "Liberia", "Libyan Arab Jamahiriya", "Liechtenstein", "Lithuania", "Luxembourg", "Macau", "Macedonia, The Former Yugoslav Republic of", "Madagascar", "Malawi", "Malaysia", "Maldives", "Mali", "Malta", "Marshall Islands", "Martinique", "Mauritania", "Mauritius", "Mayotte", "Mexico", "Micronesia, Federated States of", "Moldova, Republic of", "Monaco", "Mongolia", "Montserrat", "Morocco", "Mozambique", "Myanmar", "Namibia", "Nauru", "Nepal", "Netherlands", "Netherlands Antilles", "New Caledonia", "New Zealand", "Nicaragua", "Niger", "Nigeria", "Niue", "Norfolk Island", "Northern Mariana Islands", "Norway", "Oman", "Pakistan", "Palau", "Panama", "Papua New Guinea", "Paraguay", "Peru", "Philippines", "Pitcairn", "Poland", "Portugal", "Puerto Rico", "Qatar", "Reunion", "Romania", "Russian Federation", "Rwanda", "Saint Kitts and Nevis", "Saint Lucia", "Saint Vincent and the Grenadines", "Samoa", "San Marino", "Sao Tome and Principe", "Saudi Arabia", "Senegal", "Seychelles", "Sierra Leone", "Singapore", "Slovakia (Slovak Republic)", "Slovenia", "Solomon Islands", "Somalia", "South Africa", "South Georgia and the South Sandwich Islands", "Spain", "Sri Lanka", "St. Helena", "St. Pierre and Miquelon", "Sudan", "Suriname", "Svalbard and Jan Mayen Islands", "Swaziland", "Sweden", "Switzerland", "Syrian Arab Republic", "Taiwan, Province of China", "Tajikistan", "Tanzania, United Republic of", "Thailand", "Togo", "Tokelau", "Tonga", "Trinidad and Tobago", "Tunisia", "Turkey", "Turkmenistan", "Turks and Caicos Islands", "Tuvalu", "Uganda", "Ukraine", "United Arab Emirates", "United Kingdom", "United States", "United States Minor Outlying Islands", "Uruguay", "Uzbekistan", "Vanuatu", "Venezuela", "Vietnam", "Virgin Islands (British)", "Virgin Islands (U.S.)", "Wallis and Futuna Islands", "Western Sahara", "Yemen", "Yugoslavia", "Zambia", "Zimbabwe"};

        return countries;
    }

    public static String roundToThousands(int n){
        if(n < 1000){
            return Integer.toString(n);
        }

        return new DecimalFormat("#.#").format(n/(double)1000) + "K";
    }

    public static String formatPhoneNumber(String phone, String prefix){
        phone = phone.replaceAll("[^\\d.]", "");
        if(phone.startsWith("0")){
            phone = phone.substring(1);
        }

        if(!phone.startsWith(prefix)){
            phone = prefix + phone;
        }

        return phone;
    }

    public static String formatDateShort(String date){
        return formatDateShort(getDate(date));
    }

    public static String formatDateShort(long millis){
        return formatDateShort(new Date(millis));
    }

    public static String formatDateShort(Date date){
        if(DateUtils.isToday(date)){
            return SimpleDateFormat.getTimeInstance().format(date);
        }

        return SimpleDateFormat.getDateTimeInstance().format(date);
    }

    /**
     * Builder for the KSUtils class.
     */
    public final static class Builder {
        /**
         * Set the context to be used for Utils
         * @param cxt
         * @return Builder
         */
        public Builder setContext(final Context cxt){
            KSUtils.mContext = cxt;
            return this;
        }

        /**
         * Set the log tag for the android logcat logs
         * @param logTag The log tag
         * @return Builder
         */
        public Builder setLogTag(String logTag){
            KSUtils.mLogTag = logTag;
            return this;
        }

        /**
         * Sets whether to print log cats or not
         * @param logEnabled
         * @return Builder
         */
        public Builder setLogEnabled(boolean logEnabled){
            KSUtils.mLogEnabled = logEnabled;
            return this;
        }

        /**
         * Build the KSUtils
         */
        public void build(){
            if (mContext == null) {
                throw new RuntimeException("Context not set, please set context before building the Utils instance.");
            }

            initUtils();
        }
    }
}
