package yaseerfarah22.com.pharmacy.View;


import android.arch.lifecycle.*;
import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;


import java.util.*;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import yaseerfarah22.com.pharmacy.Adapter.CardOrderAdapter;
import yaseerfarah22.com.pharmacy.Adapter.CardOrderFilterAdapter;
import yaseerfarah22.com.pharmacy.Interface.StatusBarColor;
import yaseerfarah22.com.pharmacy.Model.Order_info;
import yaseerfarah22.com.pharmacy.R;
import yaseerfarah22.com.pharmacy.ViewModel.UserCollectionViewModel;
import yaseerfarah22.com.pharmacy.ViewModel.ViewModelFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class Order extends Fragment {

    @Inject
    ViewModelFactory viewModelFactory;

    private UserCollectionViewModel userCollectionViewModel;

    private RecyclerView orderRecycler;
    private RelativeLayout emptyOrder;
    private ProgressBar progressBar;

    private Observer orderObserver;


    private List<Order_info> orderList=new ArrayList<>();
    private CardOrderAdapter orderAdapter;


    public Order() {
        // Required empty public constructor

        orderObserver=new Observer<List<Order_info>>() {
            @Override
            public void onChanged(@Nullable List<Order_info> order_infos) {
                //Toast.makeText(getContext(),String.valueOf(order_infos.size()),Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);

                if (order_infos.size()>0){
                    if (orderRecycler.getVisibility()==View.INVISIBLE) {
                        orderRecycler.setVisibility(View.VISIBLE);
                        emptyOrder.setVisibility(View.INVISIBLE);
                    }
                }else {
                    if (emptyOrder.getVisibility()==View.INVISIBLE){
                       // Toast.makeText(getContext(),String.valueOf(order_infos.size()),Toast.LENGTH_SHORT).show();

                        orderRecycler.setVisibility(View.INVISIBLE);
                       // emptyOrder.setVisibility(View.VISIBLE);
                        MainActivity.slideToTop(emptyOrder,380,300);

                    }

                }

                orderAdapter.updateOrderList(order_infos);
            }
        };

    }


    @Override
    public void onStart() {
        userCollectionViewModel.getOrderLiveData().observe(this, orderObserver);
        super.onStart();
    }


    @Override
    public void onStop() {

        userCollectionViewModel.getOrderLiveData().removeObservers(this);
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
        View v= inflater.inflate(R.layout.fragment_order, container, false);

        orderRecycler=(RecyclerView) v.findViewById(R.id.order_recyclerview);
        emptyOrder=(RelativeLayout) v.findViewById(R.id.empty_order);
        progressBar=(ProgressBar) v.findViewById(R.id.progress);

        orderRecycler.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));

        orderAdapter=new CardOrderAdapter(getContext(),orderList,userCollectionViewModel);
        orderRecycler.setAdapter(orderAdapter);


        StatusBarColor statusBarColor=(StatusBarColor)getActivity();
        statusBarColor.setStatusBarColor(ContextCompat.getColor(getContext(),R.color.cc_black));







        return v;
    }







}
