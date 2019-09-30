package yaseerfarah22.com.pharmacy.Adapter;

        import android.app.Activity;
        import android.content.Context;
        import android.graphics.Typeface;
        import android.support.annotation.NonNull;
        import android.support.v4.app.Fragment;
        import android.support.v4.content.ContextCompat;
        import android.support.v7.widget.RecyclerView;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.view.animation.Animation;
        import android.view.animation.ScaleAnimation;
        import android.widget.Button;
        import android.widget.ImageButton;
        import android.widget.RadioButton;
        import android.widget.RelativeLayout;
        import android.widget.TextView;

        import java.util.List;

        import yaseerfarah22.com.pharmacy.Interface.CategoryAdapterListener;
        import yaseerfarah22.com.pharmacy.Interface.MyListener;
        import yaseerfarah22.com.pharmacy.Interface.OrderFilterMethod;
        import yaseerfarah22.com.pharmacy.R;
        import yaseerfarah22.com.pharmacy.View.OrderFilter;


/**
 * Created by DELL on 1/17/2019.
 */

public class CardOrderFilterAdapter extends RecyclerView.Adapter<CardOrderFilterAdapter.Pro_holder> {


    private Context context;
    private List<String>items;
    private OrderFilterMethod connection;

    private Fragment fragmentName;
    private String curruntIndex;
    private int orderOrFilter=0;


    public CardOrderFilterAdapter(Context context, List<String> items,String curruntIndex,Fragment fragmentName, int orderOrFilter, Activity activity) {
        this.context = context;
        this.items = items;
        this.orderOrFilter = orderOrFilter;
        this.fragmentName=fragmentName;
        this.connection=(OrderFilterMethod)activity;
        this.curruntIndex=curruntIndex;

    }

    @NonNull
    @Override
    public Pro_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;


        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_order_filter, parent, false);


        return new Pro_holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Pro_holder holder, final int position) {

        holder.item.setText(items.get(holder.getAdapterPosition()));

        if (curruntIndex.matches(items.get(holder.getAdapterPosition()))){
            holder.item.setChecked(true);
            holder.item.setTypeface(holder.item.getTypeface(),Typeface.BOLD);
        }


        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (orderOrFilter== OrderFilter.orderLayout){

                    connection.orderSelected(fragmentName,items.get(holder.getAdapterPosition()));

                }
                else {
                    connection.filterSelected(fragmentName,items.get(holder.getAdapterPosition()));

                }
            }
        });



    }



    ///////////////////////////////////////// Scale Animation///////////////////////////////////////////////////////





    @Override
    public int getItemCount() {
        return items.size();
    }


    //////////////////////////////////////////////////////////
    public class Pro_holder extends RecyclerView.ViewHolder{


        RelativeLayout container;
        RadioButton item;

        public Pro_holder(View itemView) {
            super(itemView);
            container =(RelativeLayout) itemView.findViewById(R.id.container);
            item =(RadioButton) itemView.findViewById(R.id.order_filter_item);

        }
    }


}
