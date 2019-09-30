package yaseerfarah22.com.pharmacy.View;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import yaseerfarah22.com.pharmacy.Adapter.CardProductAdapter;
import yaseerfarah22.com.pharmacy.Util.GridSpacingItemDecoration;
import yaseerfarah22.com.pharmacy.Interface.OrderFilterFragments;
import yaseerfarah22.com.pharmacy.Interface.StatusBarColor;
import yaseerfarah22.com.pharmacy.Model.Product_info;
import yaseerfarah22.com.pharmacy.R;
import yaseerfarah22.com.pharmacy.ViewModel.ProductViewModel;
import yaseerfarah22.com.pharmacy.ViewModel.UserCollectionViewModel;
import yaseerfarah22.com.pharmacy.ViewModel.ViewModelFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class List extends Fragment implements OrderFilterFragments,Serializable {


    @Inject
    ViewModelFactory viewModelFactory;
    private UserCollectionViewModel userCollectionViewModel;
    private ProductViewModel productViewModel;
    private boolean outComing;

    private RelativeLayout order;
    private RelativeLayout filter;
    private RecyclerView listRecycler;
    private ProgressBar progressBar;
    private RelativeLayout emptyList;



    String order_text;
    String category_text;
    String filter_text;
    boolean newData;

    private java.util.List<Product_info> offersProduct=new ArrayList<>();
    private GridLayoutManager productLayout;
    private boolean isScrolling=false;
    private CardProductAdapter cardProductAdapter;

    private Observer listObserver;


    public List() {
        // Required empty public constructor
         order_text="All";
         category_text="All";
         filter_text="All";
        outComing=true;
        listObserver=new Observer<java.util.List<Product_info>>() {
            @Override
            public void onChanged(@Nullable java.util.List<Product_info> product_infos) {


                if (product_infos.size()>0){
                    if (listRecycler.getVisibility()==View.INVISIBLE) {
                        listRecycler.setVisibility(View.VISIBLE);
                        emptyList.setVisibility(View.INVISIBLE);
                    }
                    if (outComing){
                        listRecycler.smoothScrollToPosition(0);
                        outComing=false;
                    }

                }else {
                    if (emptyList.getVisibility()==View.INVISIBLE) {
                        listRecycler.setVisibility(View.INVISIBLE);
                        MainActivity.slideToTop(emptyList, 380, 300);
                    }

                }

                progressBar.setVisibility(View.INVISIBLE);
                cardProductAdapter.updateProductList(product_infos);



            }
        };


    }


    @Override
    public void onStart() {
        productViewModel.getOfferProductLiveData().observe(this,listObserver);
        super.onStart();
    }


    @Override
    public void onStop() {

        productViewModel.getOfferProductLiveData().removeObservers(this);

        super.onStop();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AndroidSupportInjection.inject(this);

        userCollectionViewModel= ViewModelProviders.of(this,viewModelFactory).get(UserCollectionViewModel.class);
        productViewModel=ViewModelProviders.of(this,viewModelFactory).get(ProductViewModel.class);



    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_list, container, false);

        listRecycler=(RecyclerView) v.findViewById(R.id.list_recyclerview);
        order=(RelativeLayout) v.findViewById(R.id.order_by);
        filter=(RelativeLayout) v.findViewById(R.id.filter_by);
        emptyList=(RelativeLayout) v.findViewById(R.id.empty_list);
        progressBar=(ProgressBar) v.findViewById(R.id.progress);


        StatusBarColor statusBarColor=(StatusBarColor)getActivity();
        statusBarColor.setStatusBarColor(ContextCompat.getColor(getContext(),R.color.cc_black));


        category_text=getArguments().getString("Category");
        newData=getArguments().getBoolean("Order");


        if (newData){
            filter.setVisibility(View.INVISIBLE);
        }else {
            filter.setVisibility(View.VISIBLE);
        }


        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity mainActivity=(MainActivity)getActivity();
                mainActivity.orderOpen(List.this,order_text);
            }
        });


        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity mainActivity=(MainActivity)getActivity();
                mainActivity.filterOpen(List.this,category_text,filter_text);
            }
        });

        productViewModel.fetchData(order_text,category_text,filter_text,false,newData);

        productLayout=new GridLayoutManager(getContext(),2);
        listRecycler.setLayoutManager(productLayout);
        listRecycler.setLayoutManager(productLayout);
        //listRecycler.setHasFixedSize(true);
        listRecycler.addItemDecoration(new GridSpacingItemDecoration(2,GridSpacingItemDecoration.dpToPx(5,getResources()),GridSpacingItemDecoration.ListLayout));


        cardProductAdapter=new CardProductAdapter(getContext(),offersProduct,userCollectionViewModel,CardProductAdapter.listLayout,getActivity(),this);

        listRecycler.setAdapter(cardProductAdapter);

       /* productViewModel.getOfferProductLiveData().observe(this, new Observer<java.util.List<Product_info>>() {
            @Override
            public void onChanged(@Nullable java.util.List<Product_info> product_infos) {


                cardProductAdapter.updateProductList(product_infos);

            }
        });


        listRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState== AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){

                    isScrolling=true;

                }


            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int childCount=productLayout.getChildCount();
                int pastVisibleItem;
                int totalItem=productLayout.getItemCount();
                int[] firstVisibleItem=null;

                productLayout.findFirstVisibleItemPositions(firstVisibleItem);

                if (firstVisibleItem!=null&&firstVisibleItem.length>0){
                    pastVisibleItem=firstVisibleItem[0];

                    if (isScrolling&&((childCount+pastVisibleItem)>=totalItem)){

                        isScrolling=false;
                        productViewModel.fetchData(order_text,category_text,filter_text,false,true);

                    }

                }






            }
        });*/





        return v;
    }






    @Override
    public void setOrder(String order) {
        this.order_text = order;
        outComing=true;
       // productViewModel.fetchData(order_text,category_text,filter_text,false,false);
    }

    @Override
    public void setSubCategory(String subCategory) {
        this.filter_text = subCategory;
        outComing=true;

       // productViewModel.fetchData(order_text,category_text,filter_text,false,false);
    }




}
