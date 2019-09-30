package yaseerfarah22.com.pharmacy.View;


import android.arch.lifecycle.*;
import android.arch.lifecycle.Observer;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.*;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import es.dmoral.toasty.Toasty;
import yaseerfarah22.com.pharmacy.Adapter.CardProductAdapter;
import yaseerfarah22.com.pharmacy.Util.GridSpacingItemDecoration;
import yaseerfarah22.com.pharmacy.Interface.MySearchListener;
import yaseerfarah22.com.pharmacy.Interface.StatusBarColor;
import yaseerfarah22.com.pharmacy.Model.Like_info;
import yaseerfarah22.com.pharmacy.Model.Product_info;
import yaseerfarah22.com.pharmacy.R;
import yaseerfarah22.com.pharmacy.ViewModel.ProductViewModel;
import yaseerfarah22.com.pharmacy.ViewModel.UserCollectionViewModel;
import yaseerfarah22.com.pharmacy.ViewModel.ViewModelFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class Like extends Fragment {


    @Inject
    ViewModelFactory viewModelFactory;

    private UserCollectionViewModel userCollectionViewModel;
    private ProductViewModel productViewModel;

    private RecyclerView likeRecycler;
    private ProgressBar progressBar;
    private RelativeLayout emptyLike;
    private TextView emptyTitle,emptySubTitle;
    private ImageView emptyIcon;

    private String search;
    private boolean isSearched=false;
    public final static int  likeType=0;
    public   final static int searchType=1;
    private int type=-1;


    private  List<Product_info> productInfoList=new ArrayList<>();
    private CardProductAdapter cardProductAdapter;


    private Observer likeObserver;
    private Observer proObserver;



    public Like() {
        // Required empty public constructor

        likeObserver=new Observer<List<Like_info>>() {
            @Override
            public void onChanged(@Nullable List<Like_info> like_infos) {

                if (like_infos.size()>0){
                    // INVisible nothing Layout
                    likeRecycler.setVisibility(View.VISIBLE);
                    emptyLike.setVisibility(View.INVISIBLE);
                }
                else {
                    // Visible nothing Layout
                    if (emptyLike.getVisibility()==View.INVISIBLE){
                        emptyIcon.setImageDrawable(ContextCompat.getDrawable(getContext(), R.mipmap.favempty));
                        likeRecycler.setVisibility(View.INVISIBLE);
                    MainActivity.slideToTop(emptyLike,400,400);
                    }
                }

            }


        };



        proObserver=new Observer<List<Product_info>>() {
            @Override
            public void onChanged(@Nullable List<Product_info> product_infos) {
                progressBar.setVisibility(View.INVISIBLE);
                // Toast.makeText(getContext(),String.valueOf(product_infos.size()),Toast.LENGTH_SHORT).show();
                cardProductAdapter=new CardProductAdapter(getContext(),product_infos,userCollectionViewModel,CardProductAdapter.likeLayout,getActivity(),Like.this);

                likeRecycler.setAdapter(cardProductAdapter);


            }

        };


    }


    @Override
    public void onStart() {

        //Toast.makeText(getContext(),String.valueOf(type),Toast.LENGTH_SHORT).show();
        if(type==likeType){
            userCollectionViewModel.getLikeLiveData().observe(this, likeObserver);

            productViewModel.getProductLiveData().observe(this, proObserver);

        }

        super.onStart();
    }

    @Override
    public void onStop() {
        productViewModel.getProductLiveData().removeObservers(this);
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
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_like, container, false);
        progressBar=(ProgressBar) v.findViewById(R.id.progressbar);
        emptyLike=(RelativeLayout) v.findViewById(R.id.empty_like);
        emptyIcon=(ImageView) v.findViewById(R.id.empty_icon);
        emptySubTitle=(TextView) v.findViewById(R.id.empty_text2);
        emptyTitle=(TextView) v.findViewById(R.id.empty_text);
        likeRecycler=(RecyclerView) v.findViewById(R.id.like_recyclerview);
        likeRecycler.setLayoutManager(new GridLayoutManager(getContext(),2));
       // likeRecycler.setHasFixedSize(true);
        likeRecycler.addItemDecoration(new GridSpacingItemDecoration(2,GridSpacingItemDecoration.dpToPx(5,getResources()),GridSpacingItemDecoration.ListLayout));




        StatusBarColor statusBarColor=(StatusBarColor)getActivity();
        statusBarColor.setStatusBarColor(ContextCompat.getColor(getContext(),R.color.cc_black));


        type=getArguments().getInt("Type");

        if (type==searchType&&!isSearched){

            cardProductAdapter=new CardProductAdapter(getContext(),productInfoList,userCollectionViewModel,CardProductAdapter.listLayout,getActivity(),this);

            likeRecycler.setAdapter(cardProductAdapter);


            search=getArguments().getString("Search");
            productViewModel.search(search, new MySearchListener() {
                @Override
                public void onSuccess(List<Product_info> productInfos) {
                    progressBar.setVisibility(View.INVISIBLE);
                    if (productInfos.size()>0){
                        if (likeRecycler.getVisibility()==View.INVISIBLE) {
                            likeRecycler.setVisibility(View.VISIBLE);
                            emptyLike.setVisibility(View.INVISIBLE);
                        }
                        productInfoList.addAll(productInfos);
                        cardProductAdapter.notifyDataSetChanged();
                        Toasty.success(getContext(),String.valueOf(productInfoList.size())).show();
                        isSearched=true;
                    }

                    else {
                        progressBar.setVisibility(View.INVISIBLE);
                       // Toasty.warning(getContext(),"Nothing Match").show();
                        //Visible nothing layout
                        if (emptyLike.getVisibility()==View.INVISIBLE) {
                            emptyIcon.setImageDrawable(ContextCompat.getDrawable(getContext(), R.mipmap.proempty));
                            emptyTitle.setText(getResources().getString(R.string.emptylist1));
                            emptySubTitle.setText(getResources().getString(R.string.emptylist2));
                            MainActivity.slideToTop(emptyLike,380,300);
                            //emptyLike.setVisibility(View.VISIBLE);
                        }

                    }

                }

                @Override
                public void onFailure(String e) {

                }
            });

        }
        else if(type==searchType&&isSearched){


            progressBar.setVisibility(View.INVISIBLE);
            if (likeRecycler.getVisibility()==View.INVISIBLE) {
                likeRecycler.setVisibility(View.VISIBLE);
                emptyLike.setVisibility(View.INVISIBLE);
            }
            cardProductAdapter=new CardProductAdapter(getContext(),productInfoList,userCollectionViewModel,CardProductAdapter.listLayout,getActivity(),this);

            likeRecycler.setAdapter(cardProductAdapter);


        }

        else {









        }


        return v;

    }







}
