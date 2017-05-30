package com.example.przemek.egzaminel.Tools;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.widget.Toast;

public class Tools {

    public static void reloadFragment(FragmentManager fragmentManager, Fragment fragment, Bundle bundle, @IdRes int holderId, String tag) {

        if (bundle != null) {
            fragment.setArguments(bundle);
        }

        fragmentManager
                .beginTransaction()
                .replace(holderId,
                        fragment,
                        tag)
                .commit();
    }

    public static void makeLongToast(Context mContext, String toast) {
        Toast.makeText(mContext, toast, Toast.LENGTH_LONG).show();
    }

    public static void makeShortToast(Context mContext, String toast) {
        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}
