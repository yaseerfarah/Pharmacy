package yaseerfarah22.com.pharmacy.View;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import es.dmoral.toasty.Toasty;
import yaseerfarah22.com.pharmacy.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class NetworkFailed extends Fragment {


    public NetworkFailed() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_network_failed, container, false);

        Button button=(Button)v.findViewById(R.id.try_again);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkConnection();
            }
        });

        return v;
    }



    private void checkConnection(){

        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if (activeNetwork!=null&&activeNetwork.isConnectedOrConnecting()){
            MainActivity mainActivity=(MainActivity) getActivity();
            mainActivity.isConnecting=true;
            getActivity().onBackPressed();

        }else {
            Toasty.warning(getContext(),"Check your Connections").show();
        }


    }



}
