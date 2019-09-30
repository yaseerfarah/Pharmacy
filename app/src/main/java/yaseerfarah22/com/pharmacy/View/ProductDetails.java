package yaseerfarah22.com.pharmacy.View;


import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.UserInfo;

import java.util.Locale;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import yaseerfarah22.com.pharmacy.Adapter.SliderAdapter;
import yaseerfarah22.com.pharmacy.Interface.LikeListener;
import yaseerfarah22.com.pharmacy.Interface.MyListener;
import yaseerfarah22.com.pharmacy.Interface.StatusBarColor;
import yaseerfarah22.com.pharmacy.Model.Like_info;
import yaseerfarah22.com.pharmacy.Model.Product_info;
import yaseerfarah22.com.pharmacy.Model.User_info;
import yaseerfarah22.com.pharmacy.R;
import yaseerfarah22.com.pharmacy.ViewModel.UserCollectionViewModel;
import yaseerfarah22.com.pharmacy.ViewModel.ViewModelFactory;



/**
 * A simple {@link Fragment} subclass.
 */
public class ProductDetails extends Fragment {


    @Inject
    ViewModelFactory viewModelFactory;

    private UserCollectionViewModel userCollectionViewModel;

    private User_info user_info;

    ViewPager pro_image;

    TextView pro_title;
    TextView pro_price;
    TextView pro_Av_quantity;
    TextView pro_exPrice;
    TextView pro_discount;
    RelativeLayout discount;
    private int quantity=1;
    ImageButton add_q,remove_q;
    TextView pro_quantity,order_quantity,pro_Description;
    Button addToCart;
    RelativeLayout likeButton;
    boolean is_in_cart=false;
    boolean isLikeClicked=false;

    private TransitionDrawable transition ;

    private Product_info productInfo;
    private Like_info likeInfo;






    public ProductDetails() {
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
        View v= inflater.inflate(R.layout.fragment_product_details, container, false);

        likeButton=(RelativeLayout) v.findViewById(R.id.like_button);
        add_q=(ImageButton) v.findViewById(R.id.increase);
        remove_q=(ImageButton) v.findViewById(R.id.decrease);
        pro_quantity=(TextView) v.findViewById(R.id.q_text);
        pro_title=(TextView) v.findViewById(R.id.vtitle);
        pro_price=(TextView) v.findViewById(R.id.pro_price);
        pro_exPrice=(TextView) v.findViewById(R.id.pro_exprice);
        pro_discount=(TextView) v.findViewById(R.id.pro_discount);
        pro_Av_quantity=(TextView) v.findViewById(R.id.de_available_q);
        order_quantity=(TextView) v.findViewById(R.id.user_q_number);
        pro_Description=(TextView) v.findViewById(R.id.pro_dec);
        addToCart=(Button) v.findViewById(R.id.desc_add_tocart);
        pro_image=(ViewPager) v.findViewById(R.id.v_view_pager);
        discount=(RelativeLayout) v.findViewById(R.id.ex_dis);

        transition= (TransitionDrawable) likeButton.getBackground();

        StatusBarColor statusBarColor=(StatusBarColor)getActivity();
        statusBarColor.setStatusBarColor(ContextCompat.getColor(getContext(),R.color.cc_black));


        userCollectionViewModel.getUserLiveData().observe(this, new Observer<User_info>() {
            @Override
            public void onChanged(@Nullable User_info userInfo) {

                user_info=userInfo;
            }

        });





        pro_quantity.setText(String.valueOf(quantity));
        order_quantity.setText(String.valueOf(quantity));

        productInfo=(Product_info) getArguments().getSerializable("Product");
        likeInfo=(Like_info) getArguments().getSerializable("Like");

        isINCart();

        pro_title.setText(productInfo.getName());
        pro_price.setText(String.valueOf(productInfo.getPrice()));
        pro_Description.setText(productInfo.getDescription());
        pro_Av_quantity.setText(String.valueOf(productInfo.getStock()));

        if (productInfo.isIs_offer()){
            discount.setVisibility(View.VISIBLE);
            pro_exPrice.setText(String.valueOf(productInfo.getEx_price()));
            pro_discount.setText(productInfo.getOffers()+" Discount");

        }
        else {
            discount.setVisibility(View.INVISIBLE);
        }





        pro_image.setAdapter(new SliderAdapter(getContext(),productInfo.getImages_url()));
        if (likeInfo!=null){
            transition.startTransition(200);
        }



        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userCollectionViewModel.isUserLogin()) {

                    if (!isLikeClicked) {
                        isLikeClicked = true;

                        if (likeInfo!=null) {


                            userCollectionViewModel.deleteLike(likeInfo, new MyListener() {
                                @Override
                                public void onSuccess() {
                                    transition.reverseTransition(200);
                                    isLikeClicked = false;
                                }

                                @Override
                                public void onFailure(String e) {

                                }
                            });

                        } else {


                            userCollectionViewModel.addLike(productInfo, new LikeListener() {
                                @Override
                                public void onSuccess(Like_info likeInfo1) {
                                    transition.startTransition(200);
                                    likeInfo = likeInfo1;
                                    isLikeClicked = false;
                                }

                                @Override
                                public void onFailure(String e) {

                                }
                            });
                        }
                    }
                }else {
                    MainActivity mainActivity=(MainActivity)getActivity();
                    mainActivity.loginOpen();
                }
            }
        });


        add_q.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (quantity<Integer.valueOf(productInfo.getStock())){
                    quantity++;
                }

                pro_quantity.setText(String.valueOf(quantity));
                order_quantity.setText(String.valueOf(quantity));
            }
        });

        remove_q.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (quantity>1){
                    quantity--;
                }

                pro_quantity.setText(String.valueOf(quantity));
                order_quantity.setText(String.valueOf(quantity));
            }
        });



        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(userCollectionViewModel.isUserLogin()) {
                    if (!is_in_cart) {
                        create_dialog();
                    }
                }
                else {
                    MainActivity mainActivity=(MainActivity)getActivity();
                    mainActivity.loginOpen();
                }

            }
        });




        return v;
    }


    //////////////////////////////

    private void create_dialog(){


        final Dialog dialog=new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_succ);
        TextView title=(TextView)dialog.findViewById(R.id.dialog_title);
        TextView quan=(TextView)dialog.findViewById(R.id.dialog_anquantity);
        TextView price=(TextView)dialog.findViewById(R.id.dialog_price);
        title.setText(productInfo.getName().toString());
        quan.setText(String.valueOf(quantity));
        price.setText(String.format(Locale.US,"%.2f",quantity*productInfo.getPrice()));

        FloatingActionButton submit=(FloatingActionButton) dialog.findViewById(R.id.dialog_submit);
        ImageButton close=(ImageButton)dialog.findViewById(R.id.dialog_close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               userCollectionViewModel.addCart(String.valueOf(quantity), productInfo,String.format(Locale.US,"%.2f",quantity*productInfo.getPrice()), new MyListener() {
                   @Override
                   public void onSuccess() {
                       dialog.dismiss();
                       isINCart();
                   }

                   @Override
                   public void onFailure(String e) {

                   }
               });
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();





    }



    private void isINCart(){

        if (userCollectionViewModel.isInCart(productInfo.getId())){
            is_in_cart=true;
            addToCart.setText("Added To Cart");
        }
    }









    public interface OnProductDetailsInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Product_info productInfo, Like_info likeInfo);
    }

}
