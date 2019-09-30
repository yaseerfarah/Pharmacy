package yaseerfarah22.com.pharmacy.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import yaseerfarah22.com.pharmacy.Interface.CategoryAdapterListener;
import yaseerfarah22.com.pharmacy.Interface.OrderFilterMethod;
import yaseerfarah22.com.pharmacy.R;


/**
 * Created by DELL on 1/17/2019.
 */

public class CardCategoryAdapter extends RecyclerView.Adapter<CardCategoryAdapter.Pro_holder> {


    private Context context;
    private List<String>categories;
    private List<Integer>categoriesIcon;
    private List<Integer>categoriesColor;
    private String currantCategory;
    private CategoryAdapterListener categoryAdapterListener;
    private View lastCheck;


    int button_index;




    public CardCategoryAdapter(Context context,String currantCategory,int index, List<String>categories, List<Integer>categoriesIcon, List<Integer>categoriesColor, CategoryAdapterListener categoryAdapterListener) {
        this.context = context;
        this.categories=categories;
        this.categoriesIcon=categoriesIcon;
        this.categoriesColor=categoriesColor;
        this.categoryAdapterListener=categoryAdapterListener;
        this.currantCategory=currantCategory;
        button_index=index;



    }

    @NonNull
    @Override
    public Pro_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;


        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.offers_category, parent, false);


        return new Pro_holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Pro_holder holder, final int position) {

        holder.cat_text.setText(categories.get(holder.getAdapterPosition()));
        holder.cat_icon.setBackgroundResource(categoriesIcon.get(holder.getAdapterPosition()));
        holder.cat_color.setBackgroundResource(categoriesColor.get(holder.getAdapterPosition()));

        if(button_index==holder.getLayoutPosition()){
            lastCheck=holder.cat_icon_container;
            scaleUpView(lastCheck);

        }

        holder.setIsRecyclable(false);

        holder.cat_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(button_index!=holder.getLayoutPosition()) {

                    currantCategory = categories.get(holder.getAdapterPosition());
                    button_index = holder.getLayoutPosition();
                    scaleUpView(holder.cat_icon_container);

                    scaleDownView(lastCheck);
                    lastCheck = holder.cat_icon_container;
                    //notifyDataSetChanged();
                    //Toast.makeText(context,"hi",Toast.LENGTH_SHORT).show();

                    categoryAdapterListener.onCategoryChange(currantCategory,button_index);


                }

            }

        });





    }



    ///////////////////////////////////////// Scale Animation///////////////////////////////////////////////////////

    public void scaleUpView(View v) {
        Animation anim = new ScaleAnimation(
                1f, 1.2f, // Start and end values for the X axis scaling
                1f, 1.2f, // Start and end values for the Y axis scaling
                Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
                Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling
        anim.setFillAfter(true); // Needed to keep the result of the animation
        anim.setDuration(200);
        v.startAnimation(anim);
    }

    public void scaleDownView(View v) {
        Animation anim = new ScaleAnimation(
                1.2f, 1f, // Start and end values for the X axis scaling
                1.2f, 1f, // Start and end values for the Y axis scaling
                Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
                Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling
        anim.setFillAfter(true); // Needed to keep the result of the animation
        anim.setDuration(200);
        v.startAnimation(anim);
    }



    public String getCurrantCategory(){
        return currantCategory;
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }


    //////////////////////////////////////////////////////////
    public class Pro_holder extends RecyclerView.ViewHolder{

        ImageButton cat_color,cat_icon;
        TextView cat_text;
        RelativeLayout cat_icon_container,cat_container;

        public Pro_holder(View itemView) {
            super(itemView);
            cat_color =(ImageButton) itemView.findViewById(R.id.skin_color);
            cat_icon =(ImageButton) itemView.findViewById(R.id.skin_icon);
            cat_icon_container =(RelativeLayout) itemView.findViewById(R.id.skin_container);
            cat_container =(RelativeLayout) itemView.findViewById(R.id.con);
            cat_text =(TextView) itemView.findViewById(R.id.skin_text);
        }
    }


}


