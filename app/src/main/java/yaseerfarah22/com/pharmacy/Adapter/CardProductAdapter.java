package yaseerfarah22.com.pharmacy.Adapter;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.CardView;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.ArrayList;
import java.util.List;

import yaseerfarah22.com.pharmacy.Interface.DatabaseMethod;
import yaseerfarah22.com.pharmacy.Interface.LikeListener;
import yaseerfarah22.com.pharmacy.Interface.MyListener;
import yaseerfarah22.com.pharmacy.Model.Like_info;
import yaseerfarah22.com.pharmacy.Model.Product_info;
import yaseerfarah22.com.pharmacy.Model.User_info;
import yaseerfarah22.com.pharmacy.R;
import yaseerfarah22.com.pharmacy.Util.LikeProductDiffUtil;
import yaseerfarah22.com.pharmacy.Util.LikeTransition;
import yaseerfarah22.com.pharmacy.Util.ProductDiffUtil;
import yaseerfarah22.com.pharmacy.View.MainActivity;
import yaseerfarah22.com.pharmacy.View.ProductDetails;
import yaseerfarah22.com.pharmacy.ViewModel.UserCollectionViewModel;




/**
 * Created by DELL on 1/17/2019.
 */

public class CardProductAdapter extends RecyclerView.Adapter<CardProductAdapter.Pro_holder> {

    ProductDetails.OnProductDetailsInteractionListener connection;
    private Context context;
    private List<Product_info> products=new ArrayList<>();
    private List<Product_info> productLike=new ArrayList<>();
    int layout_view;
    private List<Like_info> likeList=new ArrayList<>();
    private List<Boolean> likeBooleanList=new ArrayList<>();
    private FragmentActivity fragmentActivity;
    private UserCollectionViewModel userCollectionViewModel;
    private Fragment fragment;
    private boolean isLikeClicked=false;
    private boolean isLikeDeletedDone=true;




    public final static int homeLayout=1;
    public final static int listLayout=0;
    public final static int likeLayout=3;


    public CardProductAdapter(final Context context, final List<Product_info> product, UserCollectionViewModel userCollectionViewModel, final int layout_view, FragmentActivity activity, Fragment fragment) {
        this.context = context;

        this.userCollectionViewModel=userCollectionViewModel;
        this.layout_view=layout_view;
        this.connection=(ProductDetails.OnProductDetailsInteractionListener) activity;
        this.fragmentActivity=activity;
        this.fragment=fragment;
        this.productLike=product;


        userCollectionViewModel.getLikeLiveData().observe(fragment, new Observer<List<Like_info>>() {
            @Override
            public void onChanged(@Nullable List<Like_info> like_infos) {


                if (likeList.size()>like_infos.size()){

                    isLikeDeletedDone=false;
                }else {
                    isLikeDeletedDone=true;
                }

                likeList.clear();
                likeList.addAll(like_infos);

                if (layout_view==likeLayout){

                    //Toast.makeText(context,String.valueOf(productLike.size()),Toast.LENGTH_SHORT).show();

                    updateProductList(productLike);
                }else {
                   // updateLikedProductList(convertLikeToProduct(like_infos));
                    if (isLikeDeletedDone){
                       // Toast.makeText(context,String.valueOf(likeList.size())+"/"+String.valueOf(like_infos.size()),Toast.LENGTH_SHORT).show();

                        notifyDataSetChanged();
                    }


                }

            }
        });



        if (layout_view==likeLayout){
            this.products =getLikeProduct(product,likeList);
        }else {
            this.products = product;
        }




    }





    @NonNull
    @Override
    public Pro_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if(layout_view==listLayout||layout_view==likeLayout) {
             view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);

        }else {
             view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_cardview, parent, false);


        }

        return new Pro_holder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull Pro_holder holder, int position, @NonNull List<Object> payloads) {

        boolean islike=false;
        for (int i=0;i<likeList.size();i++){
            if(products.get(holder.getAdapterPosition()).getId().matches(likeList.get(i).getProduct_info().getId())){

                    islike=true;

            }
        }
        if (!islike){
            holder.likeTransition.resetTransition();

        }

            // holder.likeButton.setBackground(holder.likelist);




        if (payloads.size()>0){

            Bundle bundle=(Bundle)payloads.get(0);
            for (String key:bundle.keySet()){

                if (key.trim().matches("Like")){
                    Toast.makeText(context,"hi",Toast.LENGTH_SHORT).show();

                    for (int i=0;i<likeList.size();i++){
                        if(products.get(holder.getAdapterPosition()).getId().matches(likeList.get(i).getProduct_info().getId())){
                            if (!holder.likeTransition.getIsLiked()){
                                holder.likeTransition.startTransition(200);
                            }

                            // Toast.makeText(context,likeList.get(i).getProduct_info().getName(),Toast.LENGTH_SHORT).show();

                        }
                    }

                   // holder.pos.setText(String.valueOf(holder.getOldPosition())+"/"+String.valueOf(holder.getLayoutPosition()));

                }

            }

        }else {
            super.onBindViewHolder(holder, position, payloads);

        }

    }


    @Override
    public void onBindViewHolder(@NonNull final Pro_holder holder, final int position) {


        /*if (holder.likeTransition.findDrawableByLayerId(holder.likeTransition.getCurrent().getLevel()).getConstantState().equals(holder.likeTransition.findDrawableByLayerId(0).getConstantState())){
            Toast.makeText(context,"hi",Toast.LENGTH_SHORT).show();
        }*/


       // holder.pos.setText(String.valueOf(holder.getOldPosition())+"/"+String.valueOf(holder.getLayoutPosition()));

       final Like_info[] likes=new Like_info[1];

        if (Integer.valueOf(products.get(holder.getAdapterPosition()).getStock())==0){
            holder.sold_layout.setVisibility(View.VISIBLE);
            holder.soldOut.setVisibility(View.VISIBLE);
        }


        Glide.with(context).load(products.get(position).getImages_url().get(0))
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        holder.progressBar.setVisibility(View.INVISIBLE);
                        holder.pro_image.setImageDrawable(resource);

                    }
                });

        for (int i=0;i<likeList.size();i++){
            if(products.get(holder.getAdapterPosition()).getId().matches(likeList.get(i).getProduct_info().getId())){
                likes[0]=likeList.get(i);
                if (!holder.likeTransition.getIsLiked()){

                    holder.likeTransition.startTransition(200);



                }

               // Toast.makeText(context,likeList.get(i).getProduct_info().getName(),Toast.LENGTH_SHORT).show();

            }
        }


        //holder.pro_image.setTransitionName(products.get(position).getImages_url().get(0));

        if(layout_view==1){
            if(products.get(position).getName().trim().toCharArray().length>10){

                String title="";

                for(int i=0;i<10;i++){
                    title+=products.get(position).getName().toString().charAt(i);
                }
                holder.name.setText(title+"...");
            }
            else {
                holder.name.setText(products.get(position).getName());
            }
        }
        else {

            holder.name.setText(products.get(position).getName());
        }

        holder.price.setText(String.valueOf(products.get(position).getPrice()));

        holder.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (userCollectionViewModel.isUserLogin()) {

                    if (!isLikeClicked) {
                        isLikeClicked = true;


                        if (holder.likeTransition.getIsLiked()) {

                            userCollectionViewModel.deleteLike(likes[0], new MyListener() {
                                @Override
                                public void onSuccess() {
                                   holder.likeTransition.reverseTransition(200);
                                    likes[0] = null;
                                    isLikeClicked = false;
                                }

                                @Override
                                public void onFailure(String e) {

                                }
                            });


                        } else {

                            // holder.fav_button.setBackgroundResource(R.drawable.ic_favorite_white_24dp);

                            userCollectionViewModel.addLike(products.get(holder.getAdapterPosition()), new LikeListener() {
                                @Override
                                public void onSuccess(Like_info likeInfo) {
                                    //transition.startTransition(200);
                                    //likes[0]=likeInfo;
                                    isLikeClicked = false;
                                }

                                @Override
                                public void onFailure(String e) {

                                }
                            });
                        }
                    }
                }
                else {
                    MainActivity mainActivity=(MainActivity)fragmentActivity;
                    mainActivity.loginOpen();
                }

            }
        });


        if(products.get(holder.getAdapterPosition()).isIs_offer()){

            holder.ex_price.setText(String.valueOf(products.get(holder.getAdapterPosition()).getEx_price()));
            holder.discount.setText((products.get(holder.getAdapterPosition()).getOffers()));
        }else {
            holder.discountLayout.setVisibility(View.INVISIBLE);
        }





        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Integer.valueOf(products.get(holder.getAdapterPosition()).getStock())!=0){
                connection.onFragmentInteraction(products.get(position),likes[0]);
                }

            }
        });



    }



    @Override
    public int getItemCount() {
        return products.size();
    }






    private List<Product_info> getLikeProduct(List<Product_info> product_infos,List<Like_info> like_infos){

        List<Product_info> likeProducts=new ArrayList<>();

        for (Like_info likeInfo:like_infos){

            for (Product_info productInfo:product_infos){

                if (likeInfo.getProduct_info().getId().trim().matches(productInfo.getId().trim())){
                    likeProducts.add(productInfo);
                    break;
                }
            }

        }

        return likeProducts;

    }



    public void updateProductList(List<Product_info> newList){
       // Toast.makeText(context,String.valueOf(newList.size())+"/"+String.valueOf(likeList.size()),Toast.LENGTH_SHORT).show();

        ProductDiffUtil productDiffUtil;

        List<Product_info> proList=new ArrayList<>();

        if (layout_view==likeLayout){
            proList.addAll(getLikeProduct(newList,likeList));
            //Toast.makeText(context,String.valueOf(this.productLike.size())+"/"+String.valueOf(getLikeProduct(newList,likeList).size())+"/"+String.valueOf(likeList.size()),Toast.LENGTH_SHORT).show();
            productDiffUtil=new ProductDiffUtil(context,this.products,proList);



        }else {
            proList.addAll(newList);
             productDiffUtil=new ProductDiffUtil(context,this.products,proList);
        }



        DiffUtil.DiffResult diffResult=DiffUtil.calculateDiff(productDiffUtil);

        this.products.clear();
        this.products.addAll(proList);


        diffResult.dispatchUpdatesTo(this);




    }



    public void updateOffersProductList(List<Product_info> newList){
        // Toast.makeText(context,String.valueOf(newList.size())+"/"+String.valueOf(likeList.size()),Toast.LENGTH_SHORT).show();

        ProductDiffUtil productDiffUtil;

        List<Product_info> proList=new ArrayList<>();

            productDiffUtil=new ProductDiffUtil(context,this.products,newList);




        DiffUtil.DiffResult diffResult=DiffUtil.calculateDiff(productDiffUtil,true);

        this.products.clear();
        this.products.addAll(newList);

        diffResult.dispatchUpdatesTo(this);





    }



    public void updateLikedProductList(List<Product_info> newList){
        // Toast.makeText(context,String.valueOf(newList.size())+"/"+String.valueOf(likeList.size()),Toast.LENGTH_SHORT).show();


        LikeProductDiffUtil likeProductDiffUtil;

        List<Product_info> proList=new ArrayList<>();
        proList.addAll(this.products);

        likeProductDiffUtil=new LikeProductDiffUtil(context,proList,newList);
        DiffUtil.DiffResult diffResult=DiffUtil.calculateDiff(likeProductDiffUtil);

        this.products.clear();
        this.products.addAll(newList);
       // Toast.makeText(context,String.valueOf(newList.size())+"/"+String.valueOf(proList.size()),Toast.LENGTH_SHORT).show();

        diffResult.dispatchUpdatesTo(this);




    }



    private List<Product_info> convertLikeToProduct(List<Like_info> likeList){

        List<Product_info> productInfos=new ArrayList<>();

        for (Like_info likeInfo:likeList){

            productInfos.add(likeInfo.getProduct_info());

        }
        return productInfos;
    }







    //////////////////////////////////////////////////////////
    public class Pro_holder extends RecyclerView.ViewHolder{

        ImageView pro_image;
        TextView name,price,ex_price,discount,soldOut;

        RelativeLayout discountLayout,sold_layout,likeButton;
        CardView cardView;
        ProgressBar progressBar;
        Drawable likelist,likelist2;
        LikeTransition likeTransition;



        public Pro_holder(View itemView) {

            super(itemView);
            cardView=(CardView) itemView.findViewById(R.id.card_view);
            pro_image=(ImageView)itemView.findViewById(R.id.pro_image);
            name=(TextView)itemView.findViewById(R.id.pro_name);
            soldOut=(TextView)itemView.findViewById(R.id.out_of_quantity);
            ex_price=(TextView)itemView.findViewById(R.id.pro_exprice);
            discount=(TextView)itemView.findViewById(R.id.pro_discount);
            price=(TextView)itemView.findViewById(R.id.pro_price);
            discountLayout=(RelativeLayout)itemView.findViewById(R.id.ex_dis);
            sold_layout=(RelativeLayout)itemView.findViewById(R.id.sold_layout);
            likeButton=(RelativeLayout)itemView.findViewById(R.id.like_button);
            progressBar=(ProgressBar)itemView.findViewById(R.id.progress);
            likeTransition=new LikeTransition(new Drawable[]{
                    ContextCompat.getDrawable(context,R.drawable.likelist),
                    ContextCompat.getDrawable(context,R.drawable.likelist2)

            });

            likelist=ContextCompat.getDrawable(context,R.drawable.likelist);
            likelist2=ContextCompat.getDrawable(context,R.drawable.likelist2);

            likeButton.setBackground(likeTransition);



        }
    }


}
