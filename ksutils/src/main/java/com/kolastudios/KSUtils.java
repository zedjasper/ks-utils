package com.kolastudios;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.text.TextUtils;
import android.util.Log;

import com.pixplicity.easyprefs.library.Prefs;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

    public static String getVersionName(Context ctx){
        try {
            PackageInfo pInfo = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0);
            return pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            logE("Error getting version code -> " + e.getMessage());
            e.printStackTrace();
        }

        return "";
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
