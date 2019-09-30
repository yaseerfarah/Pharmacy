package yaseerfarah22.com.pharmacy.Util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;
import yaseerfarah22.com.pharmacy.View.MainActivity;

/**
 * Created by DELL on 9/24/2019.
 */

public class NetworkReceiver extends BroadcastReceiver {


    MainActivity mainActivity;


    public NetworkReceiver(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

         if(intent.getAction().trim().matches("android.net.conn.CONNECTIVITY_CHANGE")){

            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

            if (activeNetwork==null){

                if (this.mainActivity.isConnecting) {
                    this.mainActivity.isConnecting=false;
                   // Toast.makeText(context,"not",Toast.LENGTH_SHORT).show();
                    this.mainActivity.networkFailed();
                }

            }else {
               // Toast.makeText(context,"yes",Toast.LENGTH_SHORT).show();
            }



        }


    }
}
