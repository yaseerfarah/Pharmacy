package yaseerfarah22.com.pharmacy.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import yaseerfarah22.com.pharmacy.Interface.MyListener;
import yaseerfarah22.com.pharmacy.Model.Cart_info;
import yaseerfarah22.com.pharmacy.R;
import yaseerfarah22.com.pharmacy.Util.CartDiffUtil;
import yaseerfarah22.com.pharmacy.ViewModel.UserCollectionViewModel;

/**
 * Created by DELL on 1/17/2019.
 */

public class CardCartAdapter extends RecyclerView.Adapter<CardCartAdapter.Pro_holder> {

    private Context context;
    private List<Cart_info> cartsInfo;

    private FragmentActivity fragmentActivity;
    private UserCollectionViewModel userCollectionViewModel;





    public CardCartAdapter(Context context, List<Cart_info> cartsInfo, UserCollectionViewModel userCollectionViewModel, FragmentActivity fragmentActivity) {
        this.context = context;
        this.cartsInfo=cartsInfo;
        this.userCollectionViewModel=userCollectionViewModel;
        this.fragmentActivity=fragmentActivity;

    }

    @NonNull
    @Override
    public Pro_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;


             view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_cardview, parent, false);


        return new Pro_holder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull Pro_holder holder, int position, @NonNull List<Object> payloads) {
        if (payloads.size()>0){


            Bundle bundle=(Bundle)payloads.get(0);

            for (String key:bundle.keySet()){
                if (key.trim().matches("price")){
                    holder.price.setText(cartsInfo.get(holder.getAdapterPosition()).getPro_price());
                    holder.quantity.setText(cartsInfo.get(holder.getAdapterPosition()).getQuantity());
                }else if (key.trim().matches("quantity")){

                    if (cartsInfo.get(holder.getAdapterPosition()).getisOutOfQuantity()){
                        holder.outOfQuantity.setVisibility(View.VISIBLE);
                    }

                }
            }



        }else {
            super.onBindViewHolder(holder, position, payloads);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final Pro_holder holder, final int position) {

        holder.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                userCollectionViewModel.deleteCart(cartsInfo.get(holder.getAdapterPosition()), new MyListener() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onFailure(String e) {

                    }
                });

            }
        });

        if (cartsInfo.get(holder.getAdapterPosition()).getisOutOfQuantity()){
            holder.outOfQuantity.setVisibility(View.VISIBLE);
        }

        Glide.with(context).load(cartsInfo.get(holder.getAdapterPosition()).getPro_imageurl()).into(holder.imageView);
        holder.title.setText(cartsInfo.get(holder.getAdapterPosition()).getPro_name());
        holder.price.setText(cartsInfo.get(holder.getAdapterPosition()).getPro_price());
        holder.quantity.setText(cartsInfo.get(holder.getAdapterPosition()).getQuantity());
    }

    @Override
    public int getItemCount() {
        return cartsInfo.size();
    }




    public void updateCartList(List<Cart_info> cartInfoList){
        CartDiffUtil cartDiffUtil=new CartDiffUtil(context,this.cartsInfo,cartInfoList);
        DiffUtil.DiffResult diffResult=DiffUtil.calculateDiff(cartDiffUtil);

        this.cartsInfo.clear();
        this.cartsInfo.addAll(cartInfoList);
        diffResult.dispatchUpdatesTo(this);

    }




    //////////////////////////////////////////////////////////
    public class Pro_holder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView title,quantity,price,outOfQuantity;
        ImageButton close;

        public Pro_holder(View itemView) {
            super(itemView);
            close=(ImageButton) itemView.findViewById(R.id.card_close);
           imageView =(ImageView) itemView.findViewById(R.id.card_image);
           title=(TextView) itemView.findViewById(R.id.card_title);
            outOfQuantity=(TextView) itemView.findViewById(R.id.out_of_quantity);
            quantity=(TextView) itemView.findViewById(R.id.card_anquantity);
            price=(TextView) itemView.findViewById(R.id.card_price);

        }
    }


}
