package yaseerfarah22.com.pharmacy.View;

import android.arch.lifecycle.*;
import android.arch.lifecycle.Observer;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.*;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import yaseerfarah22.com.pharmacy.Adapter.CardProductAdapter;
import yaseerfarah22.com.pharmacy.Util.GridSpacingItemDecoration;
import yaseerfarah22.com.pharmacy.Interface.StatusBarColor;
import yaseerfarah22.com.pharmacy.Model.Product_info;
import yaseerfarah22.com.pharmacy.R;
import yaseerfarah22.com.pharmacy.ViewModel.ProductViewModel;
import yaseerfarah22.com.pharmacy.ViewModel.UserCollectionViewModel;
import yaseerfarah22.com.pharmacy.ViewModel.ViewModelFactory;

public class Home extends Fragment {


    @Inject
    ViewModelFactory viewModelFactory;
    private UserCollectionViewModel userCollectionViewModel;
    private ProductViewModel productViewModel;


    ImageView imageView;
    CardView search;
    RelativeLayout vitamin;
    RelativeLayout pill;
    RelativeLayout skin;
    RelativeLayout body;
    RelativeLayout baby;
    RelativeLayout med;
    RelativeLayout hair;
    RelativeLayout beauty;
    RelativeLayout perfume;
    RecyclerView newArrival;
    Button more;
    ProgressBar progressBar;
    ProgressBar imageProgressBar;

    View.OnClickListener vitaminClick;
    View.OnClickListener pillClick;
    View.OnClickListener skinClick;
    View.OnClickListener bodyClick;
    View.OnClickListener babyClick;
    View.OnClickListener medicalClick;
    View.OnClickListener hairClick;
    View.OnClickListener beautyClick;
    View.OnClickListener perfumeClick;

    private java.util.List<Product_info> newProduct=new ArrayList<>();
    private GridLayoutManager productLayout;
    private Observer newProductObserver;
    private Observer checkObserver;
    private CardProductAdapter cardProductAdapter;



    String order_text="All";
    String category_text="All";
    String filter_text="All";




    public Home() {
        // Required empty public constructor

        newProductObserver=new Observer<List<Product_info>>() {
            @Override
            public void onChanged(@Nullable java.util.List<Product_info> product_infos) {
                List<Product_info> proList=new ArrayList<>();

                //Toast.makeText(getContext(),String.valueOf(product_infos.size()),Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
                if (product_infos.size()>6) {
                    for (int i = 0; i < 6; i++) {
                        proList.add(product_infos.get(i));
                    }

                }else {
                    proList.addAll(product_infos);
                }


                cardProductAdapter.updateProductList(proList);


            }
        };







    }


    @Override
    public void onStart() {
        //productViewModel.getIsEndOfNewProductLiveData().observe(this,checkObserver);
        productViewModel.getOfferProductLiveData().observe(this,newProductObserver);
        super.onStart();
    }


    @Override
    public void onStop() {
       // productViewModel.getIsEndOfNewProductLiveData().removeObservers(this);
        productViewModel.getOfferProductLiveData().removeObservers(this);
        userCollectionViewModel.getLikeLiveData().removeObservers(this);
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
      View view=inflater.inflate(R.layout.home_fragment,container,false);

        imageView=(ImageView)view.findViewById(R.id.image_view);
        search=(CardView) view.findViewById(R.id.search);
        vitamin=(RelativeLayout) view.findViewById(R.id.vit_cat);
        pill=(RelativeLayout) view.findViewById(R.id.pi_cat);
        skin=(RelativeLayout) view.findViewById(R.id.sk_cat);
        body=(RelativeLayout) view.findViewById(R.id.bo_cat);
        baby=(RelativeLayout) view.findViewById(R.id.ba_cat);
        med=(RelativeLayout) view.findViewById(R.id.md_cat);
        hair=(RelativeLayout) view.findViewById(R.id.hr_cat);
        beauty=(RelativeLayout) view.findViewById(R.id.bu_cat);
        perfume=(RelativeLayout) view.findViewById(R.id.pr_cat);
        more=(Button) view.findViewById(R.id.seemore);
        newArrival=(RecyclerView) view.findViewById(R.id.new_arrival);
        progressBar=(ProgressBar) view.findViewById(R.id.progress);
        imageProgressBar=(ProgressBar) view.findViewById(R.id.image_progress);


        vitaminClick=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openList(false,getActivity().getResources().getString(R.string.Vitamin));
            }
        };


        pillClick=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openList(false,getActivity().getResources().getString(R.string.Pill));

            }
        };

        skinClick=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openList(false,getActivity().getResources().getString(R.string.Skin));

            }
        };
        bodyClick=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openList(false,getActivity().getResources().getString(R.string.Body));

            }
        };
        babyClick=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openList(false,getActivity().getResources().getString(R.string.Baby));

            }
        };
        medicalClick=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openList(false,getActivity().getResources().getString(R.string.Medical));

            }
        };
        hairClick=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openList(false,getActivity().getResources().getString(R.string.Hair));

            }
        };
        beautyClick=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openList(false,getActivity().getResources().getString(R.string.Beauty));

            }
        };
        perfumeClick=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openList(false,getActivity().getResources().getString(R.string.Perfume));

            }
        };





        Glide.with(getContext())
                .load("https://image.freepik.com/free-photo/stethoscope-spiral-notepad-near-yellow-pills-spilled-front-white-bottle-blue-backdrop_23-2148213980.jpg")
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        imageProgressBar.setVisibility(View.INVISIBLE);
                        imageView.setImageDrawable(resource);
                    }
                });

       // newArrival.setHasFixedSize(true);

        StatusBarColor statusBarColor=(StatusBarColor)getActivity();
        statusBarColor.setStatusBarColor(ContextCompat.getColor(getContext(),R.color.cc_black));

        productViewModel.fetchData(order_text,category_text,filter_text,false,true);



        productLayout=new GridLayoutManager(getContext(),2);
        newArrival.setLayoutManager(productLayout);
        newArrival.addItemDecoration(new GridSpacingItemDecoration(2,GridSpacingItemDecoration.dpToPx(10,getResources()),GridSpacingItemDecoration.HomeLayout));

        cardProductAdapter=new CardProductAdapter(getContext(),newProduct,userCollectionViewModel,CardProductAdapter.homeLayout,getActivity(),this);

        newArrival.setAdapter(cardProductAdapter);
       /* productViewModel.getProductNewLiveData().observe(this, new Observer<List<Product_info>>() {
            @Override
            public void onChanged(@Nullable java.util.List<Product_info> product_infos) {
                List<Product_info> proList=new ArrayList<>();

                progressBar.setVisibility(View.INVISIBLE);
                if (product_infos.size()>6) {
                    for (int i = 0; i < 6; i++) {
                        proList.add(product_infos.get(i));
                    }

                }else {
                    proList.addAll(product_infos);
                }


                cardProductAdapter.updateProductList(proList);


            }
        });*/


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity mainActivity=(MainActivity)getActivity();
                mainActivity.searchOpen(search);
            }
        });

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openList(true,"All");
            }
        });


        vitamin.setOnClickListener(vitaminClick);


        pill.setOnClickListener(pillClick);


        skin.setOnClickListener(skinClick);


        body.setOnClickListener(bodyClick);


        baby.setOnClickListener(babyClick);


        med.setOnClickListener(medicalClick);


        hair.setOnClickListener(hairClick);


        beauty.setOnClickListener(beautyClick);


        perfume.setOnClickListener(perfumeClick);




        return view;
    }



    private void openList(boolean newData,String category){

        MainActivity mainActivity=(MainActivity)getActivity();
        mainActivity.listOpen(newData,category);

    }









}
