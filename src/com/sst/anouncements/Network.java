package com.sst.anouncements;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**ma
 * Created by eternitysst on 11/13/13.
 */
public class Network {
    static boolean wifiConnected;
    static boolean mobileConnected;
    static Activity parent;

    public static void init(Activity context) {
        parent = context;
    }

    public static boolean getNetwork() {
        ConnectivityManager connMgr = (ConnectivityManager) parent.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileInfor = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        assert networkInfo != null;
        wifiConnected = networkInfo.isConnected();
        if (mobileInfor != null) {
            mobileConnected = mobileInfor.isConnected();
        }
        if (wifiConnected || mobileConnected) {
            return true;
        }
        return false;
    }
    //TODO add eevent listeners for network connection changes

}
