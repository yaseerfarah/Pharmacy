package yaseerfarah22.com.pharmacy.View;


import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import android.arch.lifecycle.ViewModelProviders;
import android.widget.Toast;

import dagger.android.support.AndroidSupportInjection;
import yaseerfarah22.com.pharmacy.Adapter.CardCategoryAdapter;
import yaseerfarah22.com.pharmacy.Adapter.CardProductAdapter;
import yaseerfarah22.com.pharmacy.Util.GridSpacingItemDecoration;
import yaseerfarah22.com.pharmacy.Interface.CategoryAdapterListener;
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
public class Offers extends Fragment implements OrderFilterFragments,Serializable {


    @Inject
    ViewModelFactory viewModelFactory;

    private UserCollectionViewModel userCollectionViewModel;
    private ProductViewModel productViewModel;



    RecyclerView category;
    RecyclerView offers_product;
    RelativeLayout order;
    RelativeLayout filter;
    ProgressBar progressBar;

    CardCategoryAdapter cardCategoryAdapter;
    CardProductAdapter cardProductAdapter;
    String order_text;
    String category_text;
    String filter_text;

    int index;

    private List<Product_info> offersProduct=new ArrayList<>();
    private GridLayoutManager productLayout;
    private boolean isScrolling=false;
    private boolean scrollToZero;


    String[] categories;
    Integer[] categoryIcon={
            R.drawable.ic_all_inclusive_black_24dp,
            R.drawable.ic_vitamin_,
            R.drawable.ic_pills,
            R.drawable.ic_lotion,
            R.drawable.ic_shaving,
            R.drawable.ic_smiling_baby,
            R.drawable.ic_stethoscope,
            R.drawable.ic_hair_conditioner,
            R.drawable.ic_mascara,
            R.drawable.ic_perfume
    };

    Integer[] categoryColor={
            R.drawable.gradient_black,
            R.drawable.gradient_green,
            R.drawable.gradient_blue,
            R.drawable.gradient_yallow,
            R.drawable.gradient_orange,
            R.drawable.gradient_purple,
            R.drawable.gradient_red,
            R.drawable.gradient_blue2,
            R.drawable.gradient_red2,
            R.drawable.gradient_green2
    };




    private Observer offerObserver;


    public Offers() {
        // Required empty public constructor
        order_text="All";
        category_text="All";
        filter_text="All";
        index=0;
        scrollToZero=true;

        offerObserver=new Observer<List<Product_info>>() {
            @Override
            public void onChanged(@Nullable List<Product_info> product_infos) {
                progressBar.setVisibility(View.INVISIBLE);
                cardProductAdapter.updateOffersProductList(product_infos);
                if (scrollToZero){
                    offers_product.smoothScrollToPosition(0);
                    scrollToZero=false;
                }



            }
        };

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AndroidSupportInjection.inject(this);
        categories=getContext().getResources().getStringArray(R.array.categories);
        userCollectionViewModel=ViewModelProviders.of(this,viewModelFactory).get(UserCollectionViewModel.class);
        productViewModel=ViewModelProviders.of(this,viewModelFactory).get(ProductViewModel.class);


    }



    @Override
    public void onStart() {

        productViewModel.getOfferProductLiveData().observe(this,offerObserver);

        super.onStart();
    }


    @Override
    public void onStop() {
        productViewModel.getOfferProductLiveData().removeObservers(this);
        super.onStop();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_offers, container, false);

        category=(RecyclerView) v.findViewById(R.id.offers_category);
        offers_product=(RecyclerView) v.findViewById(R.id.offers_products);
        order=(RelativeLayout) v.findViewById(R.id.order_by);
        filter=(RelativeLayout) v.findViewById(R.id.filter_by);
        progressBar=(ProgressBar) v.findViewById(R.id.progress);


        StatusBarColor statusBarColor=(StatusBarColor)getActivity();
        statusBarColor.setStatusBarColor(ContextCompat.getColor(getContext(),R.color.cc_black));



        productViewModel.fetchData(order_text,category_text,filter_text,true,false);


        if (category_text.matches("All")){
            filter.setVisibility(View.INVISIBLE);
        }else {
            filter.setVisibility(View.VISIBLE);
        }


        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity mainActivity=(MainActivity)getActivity();
                mainActivity.orderOpen(Offers.this,order_text);
            }
        });


        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity mainActivity=(MainActivity)getActivity();
                mainActivity.filterOpen(Offers.this,category_text,filter_text);
            }
        });


        category.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
        cardCategoryAdapter=new CardCategoryAdapter(getContext(),category_text,index, Arrays.asList(categories), Arrays.asList(categoryIcon), Arrays.asList(categoryColor), new CategoryAdapterListener() {
            @Override
            public void onCategoryChange(String category,int index1) {

                index=index1;
                category_text=category;
                if (category_text.matches("All")){

                    filter.setVisibility(View.INVISIBLE);
                }else {
                    filter.setVisibility(View.VISIBLE);
                }
                order_text="All";
                filter_text="All";
                scrollToZero=true;
                productViewModel.fetchData(order_text,category_text,filter_text,true,false);

            }
        });
        category.setAdapter(cardCategoryAdapter);

        productLayout=new GridLayoutManager(getContext(),2);
        offers_product.setLayoutManager(productLayout);
        //offers_product.setHasFixedSize(true);
        offers_product.addItemDecoration(new GridSpacingItemDecoration(2,GridSpacingItemDecoration.dpToPx(5,getResources()),GridSpacingItemDecoration.ListLayout));

        cardProductAdapter=new CardProductAdapter(getContext(),offersProduct,userCollectionViewModel,CardProductAdapter.listLayout,getActivity(),this);

        offers_product.setAdapter(cardProductAdapter);

        offers_product.setItemAnimator(new DefaultItemAnimator());
       /* productViewModel.getOfferProductLiveData().observe(this, new Observer<List<Product_info>>() {
            @Override
            public void onChanged(@Nullable List<Product_info> product_infos) {
                Toast.makeText(getContext(),String.valueOf(product_infos.size()),Toast.LENGTH_LONG).show();
                cardProductAdapter.updateProductList(product_infos);
                offers_product.smoothScrollToPosition(0);

            }
        });


        offers_product.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

                        productViewModel.fetchData(order_text,category_text,filter_text,true,true);

                    }

                }






            }
        });*/



        return v;
    }







    @Override
    public void setOrder(String order) {
        this.order_text = order;
        //Toast.makeText(getContext(),order,Toast.LENGTH_SHORT).show();
        scrollToZero=true;
        //productViewModel.fetchData(order_text,category_text,filter_text,true,false);
    }

    @Override
    public void setSubCategory(String subCategory) {
        this.filter_text = subCategory;
        scrollToZero=true;
        //productViewModel.fetchData(order_text,category_text,filter_text,true,false);
    }







}
