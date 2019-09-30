package yaseerfarah22.com.pharmacy.View;


import android.app.Dialog;
import android.arch.lifecycle.*;
import android.arch.lifecycle.Observer;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import es.dmoral.toasty.Toasty;
import yaseerfarah22.com.pharmacy.Interface.MyListener;
import yaseerfarah22.com.pharmacy.Interface.OrderListener;
import yaseerfarah22.com.pharmacy.Interface.StatusBarColor;
import yaseerfarah22.com.pharmacy.Model.Cart_info;
import yaseerfarah22.com.pharmacy.Model.User_info;
import yaseerfarah22.com.pharmacy.R;
import yaseerfarah22.com.pharmacy.ViewModel.UserCollectionViewModel;
import yaseerfarah22.com.pharmacy.ViewModel.ViewModelFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class CheckOut extends Fragment {


    @Inject
    ViewModelFactory viewModelFactory;


    private UserCollectionViewModel userCollectionViewModel;

    private final int success=0;
    private final int warning=1;
    private final int error=2;

    private String firstN;
    private String lastN;
    private String email_t;
    private String phone_t;
    private String city_t;
    private String address_t;

    private User_info user_info;

    private EditText name,email,phone,city,address;
    private Button done;
    private ProgressBar progressBar;
    private CheckBox checkBox;


    public CheckOut() {
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
        View view= inflater.inflate(R.layout.fragment_check_out, container, false);

        name=(EditText)view.findViewById(R.id.name_edit);
        email=(EditText)view.findViewById(R.id.email_edit);
        phone=(EditText)view.findViewById(R.id.phone_edit);
        city=(EditText)view.findViewById(R.id.city_edit);
        address=(EditText)view.findViewById(R.id.address_edit);
        done=(Button)view.findViewById(R.id.check_done);
        progressBar=(ProgressBar)view.findViewById(R.id.progressbar);
        checkBox=(CheckBox) view.findViewById(R.id.remember);


        StatusBarColor statusBarColor=(StatusBarColor)getActivity();
        statusBarColor.setStatusBarColor(ContextCompat.getColor(getContext(),R.color.cc_black));

        user_info= (User_info) userCollectionViewModel.getUserLiveData().getValue();


        setFields();



        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (is_ok()){
                   // done.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.VISIBLE);

                    done.setClickable(false);

                    User_info userInfo=getFields();

                    SimpleDateFormat order_dateFormat=new SimpleDateFormat("yyyy-MM-dd",Locale.US);
                    String order_date=order_dateFormat.format(Calendar.getInstance().getTime());
                    userCollectionViewModel.addOrder(userInfo,"Pending", "Cash", order_date, new OrderListener() {
                        @Override
                        public void onSuccess(List<Cart_info> cartInfoList) {

                            progressBar.setVisibility(View.INVISIBLE);
                            if (cartInfoList.size()>0){
                                create_toast("ALL Success Except "+cartInfoList.size()+" Products",warning);
                            }else {
                                create_toast("Success Orders",success);
                            }

                            MainActivity mainActivity=(MainActivity)getActivity();
                            mainActivity.orderListOpen(false);

                        }

                        @Override
                        public void onFailure(String e) {
                            progressBar.setVisibility(View.INVISIBLE);
                            create_toast("Wrong Orders",error);
                            done.setClickable(true);
                        }
                    });

                    if (checkBox.isChecked()){
                        updateUserData(userInfo);
                    }


                }
                else {

                    create_toast("Something Wrong",error);
                    done.setClickable(true);

                }
            }
        });


        return view;
    }


///////////////
    public boolean is_ok(){

        if(name.getText().toString().trim().length()!=0&&email.getText().toString().trim().length()!=0&&phone.getText().toString().trim().length()!=0&&city.getText().toString().trim().length()!=0&&address.getText().toString().trim().length()!=0){
            if(email.getText().toString().contains("@")&&email.getText().toString().contains(".")){
                return true;
            }

        }


        return false;
    }


//////////////////

    private void create_toast(String text,int type){

        switch (type){

            case success:
                Toasty.success(getContext(),text,Toast.LENGTH_LONG,true).show();
                break;

            case warning:
                Toasty.warning(getContext(),text,Toast.LENGTH_LONG,true).show();
                break;


            case error:
                Toasty.error(getContext(),text,Toast.LENGTH_LONG,true).show();
                break;

        }

    }


////////////

    private void setFields(){

        if (user_info.getFirstName().trim().length()>0&& user_info.getLastName().trim().length()>0){

            name.setText(user_info.getFirstName()+" "+ user_info.getLastName());

        }

        if (user_info.getEmail().trim().length()>0){

            email.setText(user_info.getEmail());

        }

        if (user_info.getCity().trim().length()>0){

            city.setText(user_info.getCity());

        }

        if (user_info.getAddress().trim().length()>0){

            address.setText(user_info.getAddress());

        }

        if (user_info.getPhoneNumber().trim().length()>0){

            phone.setText(user_info.getPhoneNumber());

        }

    }

////////
    private User_info getFields(){

        String[] namePerson=name.getText().toString().split(" ");
        if (namePerson.length>1){
            firstN=namePerson[0];
            lastN=namePerson[1];
        }else {
            firstN=namePerson[0];
            lastN="";
        }

        city_t=city.getText().toString();

        address_t=address.getText().toString();

        phone_t=phone.getText().toString();


       return new User_info(user_info.getUser_id(),firstN,lastN,user_info.getEmail().trim(),user_info.getImage().trim(),address_t,city_t,phone_t,user_info.getPhoneNumber());

    }

////////////
    private void updateUserData(User_info userInfo){



        userCollectionViewModel.updateUser(userInfo, new MyListener() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(String e) {

            }
        });

    }






}
