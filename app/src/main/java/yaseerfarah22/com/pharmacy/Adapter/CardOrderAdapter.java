package yaseerfarah22.com.pharmacy.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import yaseerfarah22.com.pharmacy.Interface.MyListener;
import yaseerfarah22.com.pharmacy.Model.Order_info;
import yaseerfarah22.com.pharmacy.Model.Product_info;
import yaseerfarah22.com.pharmacy.R;
import yaseerfarah22.com.pharmacy.Util.OrderDiffUtil;
import yaseerfarah22.com.pharmacy.Util.ProductDiffUtil;
import yaseerfarah22.com.pharmacy.ViewModel.UserCollectionViewModel;

/**
 * Created by DELL on 1/17/2019.
 */

public class CardOrderAdapter extends RecyclerView.Adapter<CardOrderAdapter.Pro_holder> {


    private Context context;
    private List<Order_info> ordersInfo;
    private UserCollectionViewModel userCollectionViewModel;





    public CardOrderAdapter(Context context, List<Order_info> ordersInfo,UserCollectionViewModel userCollectionViewModel) {
        this.context = context;
        this.ordersInfo=ordersInfo;
        this.userCollectionViewModel=userCollectionViewModel;

    }

    @NonNull
    @Override
    public Pro_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;


             view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_cardview, parent, false);


        return new Pro_holder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull Pro_holder holder, int position, @NonNull List<Object> payloads) {


        if (payloads.size()>0){

            Bundle bundle=(Bundle)payloads.get(0);
            for (String key:bundle.keySet()){


                if (key.trim().matches("order_status")){
                    holder.orderStatus.setText(ordersInfo.get(holder.getAdapterPosition()).getOrder_status());
                }

            }


        }else {
            super.onBindViewHolder(holder, position, payloads);

        }

    }

    @Override
    public void onBindViewHolder(@NonNull final Pro_holder holder, final int position) {

        Glide.with(context).load(ordersInfo.get(holder.getAdapterPosition()).getCart_info().getPro_imageurl()).into(holder.imageView);
        holder.title.setText(ordersInfo.get(holder.getAdapterPosition()).getCart_info().getPro_name());
        holder.price.setText(ordersInfo.get(holder.getAdapterPosition()).getCart_info().getPro_price());
        holder.orderID.setText(ordersInfo.get(holder.getAdapterPosition()).getOrder_id());
        holder.orderMethod.setText(ordersInfo.get(holder.getAdapterPosition()).getOrder_method());

        if (ordersInfo.get(holder.getAdapterPosition()).getOrder_status().trim().matches("Pending")){
            holder.orderStatus.setBackground(ContextCompat.getDrawable(context, R.drawable.pending_background));
        }else {
            holder.orderStatus.setBackground(ContextCompat.getDrawable(context, R.drawable.delivered_background));
        }

        holder.orderStatus.setText(ordersInfo.get(holder.getAdapterPosition()).getOrder_status());


        holder.orderTime.setText(ordersInfo.get(holder.getAdapterPosition()).getOrder_date());
        holder.quantity.setText(ordersInfo.get(holder.getAdapterPosition()).getCart_info().getQuantity());
        holder.orderDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userCollectionViewModel.deleteOrder(ordersInfo.get(holder.getAdapterPosition()), new MyListener() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onFailure(String e) {

                    }
                });
            }
        });




    }

    @Override
    public int getItemCount() {
        return ordersInfo.size();
    }




    public void updateOrderList(List<Order_info> newList){
        // Toast.makeText(context,String.valueOf(newList.size())+"/"+String.valueOf(likeList.size()),Toast.LENGTH_SHORT).show();


        OrderDiffUtil orderDiffUtil=new OrderDiffUtil(context,this.ordersInfo,newList);

        DiffUtil.DiffResult diffResult=DiffUtil.calculateDiff(orderDiffUtil);

        ordersInfo.clear();
        this.ordersInfo.addAll(newList);

        diffResult.dispatchUpdatesTo(this);



    }





    //////////////////////////////////////////////////////////
    public class Pro_holder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView title,price,quantity,orderID,orderStatus,orderMethod,orderTime;
        Button orderDelete;

        public Pro_holder(View itemView) {
            super(itemView);
           imageView =(ImageView) itemView.findViewById(R.id.order_image);
           title=(TextView) itemView.findViewById(R.id.order_cardtitle);
            quantity=(TextView) itemView.findViewById(R.id.order_anquantity);
            orderID=(TextView) itemView.findViewById(R.id.order_anid);
            orderStatus=(TextView) itemView.findViewById(R.id.order_anstatus);
            orderMethod=(TextView) itemView.findViewById(R.id.order_anmethod);
            orderTime=(TextView) itemView.findViewById(R.id.order_antime);
            orderDelete=(Button) itemView.findViewById(R.id.order_remove);
            price=(TextView) itemView.findViewById(R.id.order_antotalprice);

        }
    }


}
