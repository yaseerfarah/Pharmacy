package yaseerfarah22.com.pharmacy.View;


import android.arch.lifecycle.*;
import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.transition.Fade;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.*;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import yaseerfarah22.com.pharmacy.Adapter.CardSearchAdapter;
import yaseerfarah22.com.pharmacy.Interface.SearchAdapterListener;
import yaseerfarah22.com.pharmacy.Interface.StatusBarColor;
import yaseerfarah22.com.pharmacy.Model.Suggestion_info;
import yaseerfarah22.com.pharmacy.R;
import yaseerfarah22.com.pharmacy.ViewModel.ProductViewModel;
import yaseerfarah22.com.pharmacy.ViewModel.UserCollectionViewModel;
import yaseerfarah22.com.pharmacy.ViewModel.ViewModelFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class Search extends Fragment {


    @Inject
    ViewModelFactory viewModelFactory;

    private ProductViewModel productViewModel;


    private EditText editText;
    private RecyclerView recyclerView;
    private ImageButton back;
    private List<String> suggestions=new ArrayList<>();
    private List<String> suggList=new ArrayList<>();
    private CardSearchAdapter cardSearchAdapter;

    public Search() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setEnterTransition(new Fade().setStartDelay(MainActivity.MOVE_DEFAULT_TIME).setDuration(MainActivity.FADE_DEFAULT_TIME));
        //setExitTransition(new Fade().setDuration(MainActivity.FADE_DEFAULT_TIME));
        //setReturnTransition(new Fade().setStartDelay(500).setDuration(MainActivity.FADE_DEFAULT_TIME));

        AndroidSupportInjection.inject(this);

        productViewModel=ViewModelProviders.of(this,viewModelFactory).get(ProductViewModel.class);



    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_search, container, false);

        editText=(EditText)v.findViewById(R.id.search);
        recyclerView=(RecyclerView)v.findViewById(R.id.sugg_recyclerview);
        back=(ImageButton)v.findViewById(R.id.back);

        StatusBarColor statusBarColor=(StatusBarColor)getActivity();
        statusBarColor.setStatusBarColor(ContextCompat.getColor(getContext(),R.color.cc_black));


        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));

        cardSearchAdapter=new CardSearchAdapter(getContext(), suggList, new SearchAdapterListener() {
            @Override
            public void onSearchSelected(String search) {

                goSearch(search);

            }
        });

        recyclerView.setAdapter(cardSearchAdapter);


        productViewModel.getSuggestionsLiveData().observe(this, new Observer<Suggestion_info>() {
            @Override
            public void onChanged(@Nullable Suggestion_info suggestion_info) {
                suggestions.clear();
                suggestions.addAll(suggestion_info.getNames());
                suggList.clear();
                suggList.addAll(suggestion_info.getNames());
                cardSearchAdapter.notifyDataSetChanged();


            }

        });



        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                suggList.clear();
                suggList.addAll(getSuggestion(charSequence));
                cardSearchAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                if(i== EditorInfo.IME_ACTION_SEARCH){

                    if(editText.getText().toString().trim().length()>0) {
                        goSearch(editText.getText().toString());
                    }
                }

                return false;
            }
        });





        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });


        return v;
    }




    private List<String> getSuggestion(CharSequence charSequence){

        List<String> sugg=new ArrayList<>();

        for(String s:suggestions){

            if(s.toLowerCase().trim().startsWith(charSequence.toString().toLowerCase().trim())){
                sugg.add(s);
            }
        }


        return sugg;
    }


    private void goSearch(String search){

        MainActivity mainActivity=(MainActivity)getActivity();

        mainActivity.searchResultOpen(search);

    }





}
