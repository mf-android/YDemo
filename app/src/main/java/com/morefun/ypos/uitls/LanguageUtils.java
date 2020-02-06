package com.morefun.ypos.uitls;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;

import com.morefun.ypos.MainActivity;

import java.util.Locale;

public class LanguageUtils {

    private static void updateConfiguration(Configuration conf, Locale locale) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            conf.setLocale(locale);
        } else {
            //noinspection deprecation
            conf.locale = locale;
        }
    }

    public static void saveLanguage(int id) {
        final SharedPreferences sharedPreferences = MainActivity.getContext().getSharedPreferences("language_choice"
                , Context.MODE_PRIVATE);
        sharedPreferences.edit().putInt("id", id).commit();
    }

    public static void setLanguage() {
        Resources resources = MainActivity.getContext().getResources();
        final SharedPreferences sharedPreferences = MainActivity.getContext().getSharedPreferences("language_choice"
                , Context.MODE_PRIVATE);

        int language_id = sharedPreferences.getInt("id", 3);
        final Configuration configuration = resources.getConfiguration();

        switch (language_id) {
            case 0:
                updateConfiguration(configuration, Locale.getDefault());
                break;
            case 1:
                updateConfiguration(configuration, Locale.SIMPLIFIED_CHINESE);
                break;
            case 2:
                updateConfiguration(configuration, Locale.ENGLISH);
                break;
            default:
                updateConfiguration(configuration, Locale.getDefault());
                break;
        }
        MainActivity.getContext().getResources().updateConfiguration(configuration, resources.getDisplayMetrics());
    }
}
