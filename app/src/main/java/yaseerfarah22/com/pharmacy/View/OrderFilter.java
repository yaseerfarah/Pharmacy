package yaseerfarah22.com.pharmacy.View;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import yaseerfarah22.com.pharmacy.Adapter.CardOrderFilterAdapter;
import yaseerfarah22.com.pharmacy.Interface.StatusBarColor;
import yaseerfarah22.com.pharmacy.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderFilter extends Fragment {


    private TextView title;
    private RecyclerView item;
    public static final int orderLayout=1;
    public static final int filterLayout=2;
    private CardOrderFilterAdapter cardOrderFilterAdapter;

    public OrderFilter() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_order_filter, container, false);

        title=(TextView)v.findViewById(R.id.title_text);
        item=(RecyclerView)v.findViewById(R.id.order_filter_recyclerview);
        item.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));

        StatusBarColor statusBarColor=(StatusBarColor)getActivity();
        statusBarColor.setStatusBarColor(ContextCompat.getColor(getContext(),R.color.cc_black));


        int layout=getArguments().getInt("OrderOrFilter");

        if (layout==orderLayout){
            title.setText("Order By ");
            Fragment fragment= (Fragment) getArguments().getSerializable("Fragment");
            String order=getArguments().getString("Order");
            cardOrderFilterAdapter=new CardOrderFilterAdapter(getContext(),getSubCategories("Order"),order,fragment,orderLayout,getActivity());

        }else {
            title.setText("Filter By");
            Fragment fragment= (Fragment) getArguments().getSerializable("Fragment");
            String cat=getArguments().getString("Category");
            String filter=getArguments().getString("Filter");
            cardOrderFilterAdapter=new CardOrderFilterAdapter(getContext(),getSubCategories(cat),filter,fragment,filterLayout,getActivity());

        }

        item.setAdapter(cardOrderFilterAdapter);


        return v;
    }



    private List<String> getSubCategories(String category){

        List<String> cat=new ArrayList<>();

        switch (category.trim()){

            case "Order":
                cat.addAll(Arrays.asList(getActivity().getResources().getStringArray(R.array.Order)));
                break;

            case "Vitamin":
                cat.addAll(Arrays.asList(getActivity().getResources().getStringArray(R.array.Vitamin)));
                break;

            case "Pill":
                cat.addAll(Arrays.asList(getActivity().getResources().getStringArray(R.array.Pill)));
                break;

            case "Skin":
                cat.addAll(Arrays.asList(getActivity().getResources().getStringArray(R.array.Skin)));
                break;

            case "Body":
                cat.addAll(Arrays.asList(getActivity().getResources().getStringArray(R.array.Body)));
                break;


            case "Baby":
                cat.addAll(Arrays.asList(getActivity().getResources().getStringArray(R.array.Baby)));
                break;


            case "MedicalSupplies":
                cat.addAll(Arrays.asList(getActivity().getResources().getStringArray(R.array.Medical)));
                break;


            case "Hair":
                cat.addAll(Arrays.asList(getActivity().getResources().getStringArray(R.array.Hair)));
                break;

            case "Beauty":
                cat.addAll(Arrays.asList(getActivity().getResources().getStringArray(R.array.Beauty)));
                break;


            case "Perfume":
                cat.addAll(Arrays.asList(getActivity().getResources().getStringArray(R.array.Perfume)));
                break;
        }

        return cat;

    }







}
