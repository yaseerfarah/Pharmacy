package yaseerfarah22.com.pharmacy.View;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import yaseerfarah22.com.pharmacy.Model.User_info;
import yaseerfarah22.com.pharmacy.R;
import yaseerfarah22.com.pharmacy.ViewModel.UserCollectionViewModel;
import yaseerfarah22.com.pharmacy.ViewModel.ViewModelFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserProfile extends Fragment {


    @Inject
    ViewModelFactory viewModelFactory;

    private UserCollectionViewModel userCollectionViewModel;

    private ImageView userImage;
    private TextView userName,userEmail,firstName,lastName,city,address,phone;
    private RelativeLayout favorite,order,logOut;
    private ImageButton setting;
    private MainActivity mainActivity;


    private User_info user_info;

    public UserProfile() {
        // Required empty public constructor
    }






    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AndroidSupportInjection.inject(this);
        userCollectionViewModel= ViewModelProviders.of(this,viewModelFactory).get(UserCollectionViewModel.class);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_user_profile, container, false);

        mainActivity=(MainActivity)getActivity();
        userImage=(ImageView)v.findViewById(R.id.user_image);
        setting=(ImageButton)v.findViewById(R.id.setting);
        favorite=(RelativeLayout)v.findViewById(R.id.like);
        order=(RelativeLayout)v.findViewById(R.id.ship);
        logOut=(RelativeLayout)v.findViewById(R.id.logout);
        userName=(TextView)v.findViewById(R.id.person_name);
        userEmail=(TextView)v.findViewById(R.id.person_email);
        firstName=(TextView)v.findViewById(R.id.anfirst);
        lastName=(TextView)v.findViewById(R.id.anlast);
        city=(TextView)v.findViewById(R.id.ancity);
        address=(TextView)v.findViewById(R.id.anaddress);
        phone=(TextView)v.findViewById(R.id.anphone);


        mainActivity.setStatusBarColor(ContextCompat.getColor(getContext(),R.color.light_blue));

        user_info= (User_info) userCollectionViewModel.getUserLiveData().getValue();
        if (!userCollectionViewModel.isUserLogin()){
            MainActivity mainActivity=(MainActivity)getActivity();
            mainActivity.homeOpen();
        }


        userCollectionViewModel.getUserLiveData().observe(this, new Observer<User_info>() {

            @Override
            public void onChanged(@Nullable User_info userInfo) {
                user_info=userInfo;
            }
        });


        Glide.with(getContext()).load(user_info.getImage()).into(userImage);
        userName.setText(user_info.getFirstName()+" "+ user_info.getLastName());
        userEmail.setText(user_info.getEmail());
        firstName.setText(user_info.getFirstName());
        lastName.setText(user_info.getLastName());

        if (user_info.getCity()==null||user_info.getCity().trim().length()==0){
            city.setText("Unknown");
        }else {
            city.setText(user_info.getCity());
        }

        if (user_info.getAddress()==null||user_info.getPhoneNumber().trim().length()==0){
            address.setText("Unknown");
        }else {
            address.setText(user_info.getAddress());
        }

        if (user_info.getPhoneNumber()==null||user_info.getPhoneNumber().trim().length()==0){
            phone.setText("Unknown");
        }else {
            phone.setText(user_info.getPhoneNumber());
        }


        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.likeOpen();
            }
        });



        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mainActivity.orderListOpen(true);

            }
        });


        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //

            }
        });



        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //

            }
        });



        return v;

    }

}
