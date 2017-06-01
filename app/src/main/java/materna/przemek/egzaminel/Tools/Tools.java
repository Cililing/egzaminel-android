package materna.przemek.egzaminel.Tools;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.widget.Toast;

import materna.przemek.egzaminel.R;

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

    public static void openEmailIntent(Context context, @StringRes int idEmail) {

        final Intent emailIntent = new Intent(Intent.ACTION_SEND);

        //fill it with data
        emailIntent.setType("plain/text");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{ context.getString(R.string.about_me_email) });

        context.startActivity(Intent.createChooser(emailIntent, context.getString(idEmail)));}

}
