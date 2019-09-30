package yaseerfarah22.com.pharmacy.View;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.*;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import yaseerfarah22.com.pharmacy.Adapter.CardCartAdapter;
import yaseerfarah22.com.pharmacy.Interface.StatusBarColor;
import yaseerfarah22.com.pharmacy.Model.Cart_info;
import yaseerfarah22.com.pharmacy.R;
import yaseerfarah22.com.pharmacy.ViewModel.UserCollectionViewModel;
import yaseerfarah22.com.pharmacy.ViewModel.ViewModelFactory;

import static yaseerfarah22.com.pharmacy.View.MainActivity.slideToTop;

/**
 * A simple {@link Fragment} subclass.
 */
public class Cart extends Fragment {


    @Inject
    ViewModelFactory viewModelFactory;


    private UserCollectionViewModel userCollectionViewModel;

    private RecyclerView cartRecycler;
    private Button confirm;
    private TextView total;
    private RelativeLayout emptyCart;
    private CardView cardView;

    private float price=0f;
    private CardCartAdapter cardCartAdapter;
    private List<Cart_info> cartList=new ArrayList<>();

    private Observer cartObserver;



    public Cart() {
        // Required empty public constructor

        cartObserver=new Observer< java.util.List<Cart_info>>() {
            @Override
            public void onChanged(@Nullable java.util.List<Cart_info> cart_infos) {
                //Toast.makeText(getContext(),String.valueOf(cart_infos.size()),Toast.LENGTH_SHORT).show();
                if (cart_infos.size()>0) {
                    if (cardView.getVisibility()==View.INVISIBLE){
                        cartRecycler.setVisibility(View.VISIBLE);
                        slideToTop(cardView,200,200);
                        emptyCart.setVisibility(View.INVISIBLE);
                    }

                    cardCartAdapter.updateCartList(cart_infos);
                    count_price();
                }else{
                    if (emptyCart.getVisibility()==View.INVISIBLE){

                        cartRecycler.setVisibility(View.INVISIBLE);
                        cardView.setVisibility(View.INVISIBLE);
                        slideToTop(emptyCart,380,300);

                    }

                    cardCartAdapter.updateCartList(cart_infos);
                    count_price();

                }


            }
        };

    }


    @Override
    public void onStart() {
        userCollectionViewModel.getCartLiveData().observe(this,cartObserver);
        super.onStart();
    }


    @Override
    public void onStop() {

        userCollectionViewModel.getCartLiveData().removeObservers(this);

        super.onStop();
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
        View v= inflater.inflate(R.layout.fragment_cart, container, false);

        cartRecycler=(RecyclerView) v.findViewById(R.id.card_recycleview);
        confirm=(Button) v.findViewById(R.id.card_confirm);
        total=(TextView) v.findViewById(R.id.total_price);
        emptyCart=(RelativeLayout)v.findViewById(R.id.empty_cart);
        cardView=(CardView)v.findViewById(R.id.confirm) ;

        StatusBarColor statusBarColor=(StatusBarColor)getActivity();
        statusBarColor.setStatusBarColor(ContextCompat.getColor(getContext(),R.color.cc_black));



        cartRecycler.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));


        cardCartAdapter=new CardCartAdapter(getContext(),cartList,userCollectionViewModel,getActivity());
        cartRecycler.setAdapter(cardCartAdapter);





       /* userCollectionViewModel.getCartLiveData().observe(this, new Observer< java.util.List<Cart_info>>() {
            @Override
            public void onChanged(@Nullable java.util.List<Cart_info> cart_infos) {
              cardCartAdapter.updateCartList(cart_infos);
                count_price();

            }
        });*/

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity mainActivity=(MainActivity)getActivity();
                mainActivity.checkOutOpen();
            }
        });

        count_price();





        return v;
    }





    public void count_price(){
        price=0;
        for (int i = 0; i< cartList.size(); i++){
            if (!cartList.get(i).getisOutOfQuantity()){
                price+=Float.valueOf(cartList.get(i).getPro_price());

            }

        }
        total.setText(String.valueOf(price));
    }











}
