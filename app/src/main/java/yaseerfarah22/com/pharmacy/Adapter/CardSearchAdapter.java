package yaseerfarah22.com.pharmacy.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import yaseerfarah22.com.pharmacy.Interface.SearchAdapterListener;
import yaseerfarah22.com.pharmacy.R;

/**
 * Created by DELL on 1/17/2019.
 */

public class CardSearchAdapter extends RecyclerView.Adapter<CardSearchAdapter.Pro_holder> {


    private Context context;
    private List<String>sugg;
    private SearchAdapterListener searchAdapterListener;


    public CardSearchAdapter(Context context, List<String> sugg,SearchAdapterListener searchAdapterListener) {
        this.context = context;
        this.sugg = sugg;
        this.searchAdapterListener=searchAdapterListener;
    }

    @NonNull
    @Override
    public Pro_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;


             view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_cart, parent, false);


        return new Pro_holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Pro_holder holder, final int position) {


        holder.suggText.setText(sugg.get(holder.getAdapterPosition()));
        holder.suggestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchAdapterListener.onSearchSelected(sugg.get(holder.getAdapterPosition()));
            }
        });


    }







    @Override
    public int getItemCount() {
        return sugg.size();
    }


    //////////////////////////////////////////////////////////
    public class Pro_holder extends RecyclerView.ViewHolder{

        RelativeLayout suggestion;
        TextView suggText;

        public Pro_holder(View itemView) {
            super(itemView);
           suggestion =(RelativeLayout) itemView.findViewById(R.id.suggestion_box);
           suggText =(TextView) itemView.findViewById(R.id.suggestion);

        }
    }


}
