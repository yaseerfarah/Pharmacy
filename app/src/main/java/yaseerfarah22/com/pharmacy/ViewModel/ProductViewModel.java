package yaseerfarah22.com.pharmacy.ViewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import yaseerfarah22.com.pharmacy.Interface.MyListener;
import yaseerfarah22.com.pharmacy.Interface.MySearchListener;
import yaseerfarah22.com.pharmacy.Model.Cart_info;
import yaseerfarah22.com.pharmacy.Model.DocumentSnapListener;
import yaseerfarah22.com.pharmacy.Model.Product_info;
import yaseerfarah22.com.pharmacy.Interface.MyCallback;
import yaseerfarah22.com.pharmacy.Model.Suggestion_info;
import yaseerfarah22.com.pharmacy.R;
import yaseerfarah22.com.pharmacy.Repository.FirestoreMethod;
import yaseerfarah22.com.pharmacy.Repository.ListenFirestoreItem;

/**
 * Created by DELL on 5/5/2019.
 */

public class ProductViewModel extends ViewModel  {


    private MediatorLiveData<List<Product_info>> productLiveData=new MediatorLiveData<>();
    private MediatorLiveData<List<Product_info>> productNewLiveData=new MediatorLiveData<>();
    private MediatorLiveData<List<Product_info>> highToLowProductLiveData=new MediatorLiveData<>();
    private MediatorLiveData<List<Product_info>> lowToHighProductLiveData=new MediatorLiveData<>();
    private MediatorLiveData<List<Product_info>> offerProductLiveData=new MediatorLiveData<>();
    private MediatorLiveData<Suggestion_info> suggestionLiveData=new MediatorLiveData<>();
    private List<Product_info> productInfoList=new ArrayList<>();
    private List<Product_info> newProductInfoList=new ArrayList<>();
    private List<Product_info> hToLProductInfoList=new ArrayList<>();
    private List<Product_info> lToHProductInfoList=new ArrayList<>();
    private List<Product_info> offerProductInfoList=new ArrayList<>();
    private List<Product_info> lastSearch=new ArrayList<>();

    private FirestoreMethod firestoreMethod;
    private List<String> letters;
    private DocumentSnapshot productLastDocument;
    private DocumentSnapshot newProductLastDocument;
    private DocumentSnapshot hToLProductLastDocument;
    private DocumentSnapshot lToHProductLastDocument;
    private  MediatorLiveData<Boolean> isEndOfProduct=new MediatorLiveData<>();
    private MediatorLiveData<Boolean> isEndOfNewProduct=new MediatorLiveData<>();
    private MediatorLiveData<Boolean> isEndOfHighToLowProduct=new MediatorLiveData<>();
    private MediatorLiveData<Boolean> isEndOfLowToHighProduct=new MediatorLiveData<>();



    private int limit=40;
    private Context context;


    @Inject
    public ProductViewModel(final Context context, FirestoreMethod firestoreMethod){

        this.context=context;
        this.firestoreMethod=firestoreMethod;
        letters= Arrays.asList(context.getResources().getStringArray(R.array.Letters));
        productAddSource();
        isEndOfProduct.setValue(false);
        isEndOfNewProduct.setValue(false);
        isEndOfHighToLowProduct.setValue(false);
        isEndOfLowToHighProduct.setValue(false);


        if (this.productNewLiveData.getValue()==null) {


            getNewProduct(new MyListener() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onFailure(String e) {

                }
            });

        }


        if (this.productLiveData.getValue()==null){
           getProduct(new MyListener() {
               @Override
               public void onSuccess() {

               }

               @Override
               public void onFailure(String e) {

               }
           });
        }




        if (this.highToLowProductLiveData.getValue()==null) {

            getHighToLowProduct(new MyListener() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onFailure(String e) {

                }
            });

        }


        if (this.lowToHighProductLiveData.getValue()==null) {

            getLowToHighProduct(new MyListener() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onFailure(String e) {

                }
            });

        }


        if (this.suggestionLiveData.getValue()==null) {

            getSuggestions();

        }




    }













    private void getProduct(final MyListener myListener){
        if (!isEndOfProduct.getValue()) {

            firestoreMethod.getInfo("Products", Product_info.class, limit, productLastDocument, new MyCallback() {
                @Override
                public void onCallback(List list, String actionType, DocumentSnapshot lastResult) {
                    List<Product_info> product_infos = list;
                    productInfoList.addAll(product_infos);
                    productLiveData.postValue(productInfoList);

                    if (lastResult != null) {
                        productLastDocument = lastResult;
                    }


                    if (product_infos.size()<limit){
                        isEndOfProduct.setValue(true);
                        myListener.onSuccess();
                    }else {
                        getProduct(myListener);
                    }

                }

                @Override
                public void onFailure(String e) {
                    myListener.onFailure(e);
                }
            });
        }else {
            myListener.onSuccess();
        }
    }



    private void getNewProduct(final MyListener myListener){

        if(!isEndOfNewProduct.getValue()) {

            firestoreMethod.getInfoOrderBy("Products", "product_date", FirestoreMethod.ascending, Product_info.class, limit, newProductLastDocument, new MyCallback() {
                @Override
                public void onCallback(List list, String actionType, DocumentSnapshot lastResult) {
                    List<Product_info> product_infos = list;
                    newProductInfoList.addAll(product_infos);
                    productNewLiveData.postValue(newProductInfoList);

                    if (lastResult != null) {
                        newProductLastDocument = lastResult;
                    }

                    if(product_infos.size()<limit){
                        isEndOfNewProduct.setValue(true);
                        myListener.onSuccess();
                    }else {
                        getNewProduct(myListener);
                    }

                }

                @Override
                public void onFailure(String e) {
                    myListener.onFailure(e);
                }
            });

        }else {
            myListener.onSuccess();

        }

    }




    private void getHighToLowProduct(final MyListener myListener){
        if (!isEndOfHighToLowProduct.getValue()) {


            firestoreMethod.getInfoOrderBy("Products", "price", FirestoreMethod.descending, Product_info.class, limit, hToLProductLastDocument, new MyCallback() {
                @Override
                public void onCallback(List list, String actionType, DocumentSnapshot lastResult) {
                    List<Product_info> product_infos = list;
                    hToLProductInfoList.addAll(product_infos);
                    highToLowProductLiveData.postValue(hToLProductInfoList);

                    if (lastResult != null) {
                        hToLProductLastDocument = lastResult;
                    }

                    if(product_infos.size()<limit){
                        isEndOfHighToLowProduct.setValue(true);
                        myListener.onSuccess();
                    }else {
                        getHighToLowProduct(myListener);
                    }

                }

                @Override
                public void onFailure(String e) {
                    myListener.onFailure(e);
                }
            });

        }else {
            myListener.onSuccess();
        }

    }





    private void getLowToHighProduct(final MyListener myListener){
        if(!isEndOfLowToHighProduct.getValue()) {

            firestoreMethod.getInfoOrderBy("Products", "price", FirestoreMethod.ascending, Product_info.class, limit, lToHProductLastDocument, new MyCallback() {
                @Override
                public void onCallback(List list, String actionType, DocumentSnapshot lastResult) {
                    List<Product_info> product_infos = list;
                    lToHProductInfoList.addAll(product_infos);
                    lowToHighProductLiveData.postValue(lToHProductInfoList);

                    if (lastResult != null) {
                        lToHProductLastDocument = lastResult;
                    }

                    if(product_infos.size()<limit){
                        isEndOfLowToHighProduct.setValue(true);
                        myListener.onSuccess();
                    }else {
                        getLowToHighProduct(myListener);
                    }

                }

                @Override
                public void onFailure(String e) {
                    myListener.onFailure(e);
                }
            });

        }else {
            myListener.onSuccess();
        }

    }



    private void getSuggestions(){
        firestoreMethod.getInfo("Suggestions", Suggestion_info.class, new MyCallback() {
            @Override
            public void onCallback(List list, String actionType, DocumentSnapshot lastResult) {
                List<Suggestion_info>suggestion_infos=list;
                if (suggestion_infos.size()>0){

                    suggestionLiveData.postValue(suggestion_infos.get(0));
                }

            }

            @Override
            public void onFailure(String e) {

            }
        });
    }






    //////////////////////////////////////////////////////////////////////////////////////////////////


    public void fetchData(final String order, final String category, final String filter, final boolean offers, final boolean newData){


        getAllData(new MyListener() {
            @Override
            public void onSuccess() {

                //Toast.makeText(context,"FetchData",Toast.LENGTH_SHORT).show();

                if(newData){

                    offerProductInfoList.clear();
                    offerProductInfoList.addAll(getOrderNewData(order,category,filter,offers));


                }else {

                    offerProductInfoList.clear();
                    offerProductInfoList.addAll(getOrder(order,category,filter,offers));

                }

                offerProductLiveData.postValue(offerProductInfoList);


            }

            @Override
            public void onFailure(String e) {

            }
        });







    }

    /////////

    private void getAllData(final MyListener myListener){

        final int[] count=new int[1];
        count[0]=0;


        Observer checkObserver=new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {

                if (aBoolean){
                    count[0]++;
                    if (count[0]==4){

                        myListener.onSuccess();
                        removeObserver(this);
                    }
                }

            }
        };



        isEndOfProduct.observeForever(checkObserver);
        isEndOfNewProduct.observeForever(checkObserver);
        isEndOfHighToLowProduct.observeForever(checkObserver);
        isEndOfLowToHighProduct.observeForever(checkObserver);




    }


    private void removeObserver(Observer checkObserver){
        isEndOfProduct.removeObserver(checkObserver);
        isEndOfNewProduct.removeObserver(checkObserver);
        isEndOfHighToLowProduct.removeObserver(checkObserver);
        isEndOfLowToHighProduct.removeObserver(checkObserver);


    }


    //////////

    private List<Product_info> getOrder(String order,String category,String filter,boolean offers){

        List<Product_info> orderList=new ArrayList<>();

        if(order.trim().matches("All")){
            if(offers){
                orderList.addAll( getFilter(getOffers(productInfoList),category,filter));
            }else {
                orderList.addAll( getFilter(productInfoList,category,filter));
            }

        }
        else if(order.trim().matches("High To Low Price")){

            if(offers){
                orderList.addAll( getFilter(getOffers(hToLProductInfoList),category,filter));
            }else {
                orderList.addAll( getFilter(hToLProductInfoList,category,filter));
            }

        }

        else if(order.trim().matches("Low To High Price")){

            if(offers){
                orderList.addAll( getFilter(getOffers(lToHProductInfoList),category,filter));
            }else {
                orderList.addAll( getFilter(lToHProductInfoList,category,filter));
            }

        }
        else if(order.trim().matches("New")){

            if(offers){
                orderList.addAll( getFilter(getOffers(newProductInfoList),category,filter));
            }else {
                orderList.addAll( getFilter(newProductInfoList,category,filter));
            }

        }

        return orderList;


    }


    /////////

    private List<Product_info> getOrderNewData(String order,String category,String filter,boolean offers){

        List<Product_info> orderList=new ArrayList<>();

        if(order.trim().matches("All")){
            if(offers){
                orderList.addAll( getFilter(getOffers(newProductInfoList),category,filter));
            }else {
                orderList.addAll( getFilter(newProductInfoList,category,filter));
            }

        }
        else if(order.trim().matches("High To Low Price")){

            if(offers){
                orderList.addAll( getFilter(getOffers(newProductInfoList),category,filter));
            }else {
                orderList.addAll( getFilter(sortProduct(newProductInfoList,false),category,filter));
            }

        }

        else if(order.trim().matches("Low To High Price")){

            if(offers){
                orderList.addAll( getFilter(getOffers(newProductInfoList),category,filter));
            }else {
                orderList.addAll( getFilter(sortProduct(newProductInfoList,true),category,filter));
            }

        }



        return orderList;


    }







    /////////

    private List<Product_info> getFilter(List<Product_info> infoList ,String category,String sub_Category){

        List<Product_info> filter=new ArrayList<>();

        if(category.trim().matches("All")){
            filter.addAll(infoList);
        }
        else {

            if (sub_Category.trim().matches("All")){

                for(Product_info productInfo:infoList){

                    if(productInfo.getCategory().trim().matches(category)){
                        filter.add(productInfo);
                    }

                }

            }else {

                for(Product_info productInfo:infoList){

                    if(productInfo.getCategory().trim().matches(category)&&productInfo.getSub_category().trim().matches(sub_Category)){
                        filter.add(productInfo);
                    }

                }

            }

        }




        return filter;
    }






    //////////////

    private List<Product_info> getOffers(List<Product_info> infoList){

        List<Product_info> offers=new ArrayList<>();


        for (Product_info productInfo:infoList){

            if (productInfo.isIs_offer()){
                offers.add(productInfo);
            }

        }

       return offers;

    }








///////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////

    private void productAddSource(){

        productLiveData.addSource(new ListenFirestoreItem("Products",context), new Observer<List<DocumentSnapListener>>() {
            @Override
            public void onChanged(@Nullable List<DocumentSnapListener> documentSnapListeners) {
                if (documentSnapListeners.size()>0) {

                    List<Product_info> updatedProductList=new ArrayList<>();

                    for (DocumentSnapListener documentSnapListener:documentSnapListeners) {

                        switch (documentSnapListener.getActionType()) {

                            case ListenFirestoreItem.modifiedTag:

                                Product_info productInfo = documentSnapListener.getDocumentSnapshot().toObject(Product_info.class) != null ? documentSnapListener.getDocumentSnapshot().toObject(Product_info.class) : new Product_info();
                                updatedProductList.add(productInfo);
                                break;
                        }
                    }

                    updateProduct(updatedProductList);
                    updateNewProduct(updatedProductList);
                    updateHtoLProduct(updatedProductList);
                    updateLtoHProduct(updatedProductList);

                }
            }
        });

    }




    private void updateProduct(List<Product_info> infoList){

        if (infoList.size()!=0){

           for (Product_info productInfo:infoList){

               for (int i=0;i<productInfoList.size();i++){

                   if(productInfo.getId().trim().matches(productInfoList.get(i).getId().trim())){

                       productInfoList.remove(i);
                       productInfoList.add(i,productInfo);

                   }

               }

           }

           productLiveData.postValue(productInfoList);

        }

    }



    private void updateNewProduct(List<Product_info> infoList){

        if (infoList.size()!=0){

            for (Product_info productInfo:infoList){

                for (int i=0;i<newProductInfoList.size();i++){

                    if(productInfo.getId().trim().matches(newProductInfoList.get(i).getId().trim())){

                        newProductInfoList.remove(i);
                        newProductInfoList.add(i,productInfo);

                    }

                }

            }

            productNewLiveData.postValue(newProductInfoList);

        }

    }





    private void updateHtoLProduct(List<Product_info> infoList){

        if (infoList.size()!=0){

            for (Product_info productInfo:infoList){

                for (int i=0;i<hToLProductInfoList.size();i++){

                    if(productInfo.getId().trim().matches(hToLProductInfoList.get(i).getId().trim())){

                        hToLProductInfoList.remove(i);
                        hToLProductInfoList.add(i,productInfo);

                    }

                }

            }

            highToLowProductLiveData.postValue(hToLProductInfoList);

        }

    }






    private void updateLtoHProduct(List<Product_info> infoList){

        if (infoList.size()!=0){

            for (Product_info productInfo:infoList){

                for (int i=0;i<lToHProductInfoList.size();i++){

                    if(productInfo.getId().trim().matches(lToHProductInfoList.get(i).getId().trim())){

                        lToHProductInfoList.remove(i);
                        lToHProductInfoList.add(i,productInfo);

                    }

                }

            }


            lowToHighProductLiveData.postValue(lToHProductInfoList);

        }

    }

    /////////////////////////////////////////////////////////////////////////////////////////////////


    public void search(String search, final MySearchListener mySearchListener){

        String search2=getSearchWord(search);

        firestoreMethod.search("Products", "name", search, search2, Product_info.class, new MySearchListener() {
            @Override
            public void onSuccess(List<Product_info> productInfoList) {

                lastSearch.clear();
                lastSearch.addAll(productInfoList);
                mySearchListener.onSuccess(productInfoList);
            }

            @Override
            public void onFailure(String e) {
                mySearchListener.onFailure(e);
            }
        });

    }


    public List<Product_info> getLastSearch(){
        return lastSearch;
    }




    /////

    private String getSearchWord(String search){


        char lastchar=search.trim().toLowerCase().charAt(search.length()-1);
        String search2="";
        for (int i=0;i<search.length()-1;i++){
            search2+=search.charAt(i);
        }

        for (int i=0;i<letters.size();i++){

            if (lastchar==letters.get(i).trim().charAt(0)){
                if(i==letters.size()-1){
                    search2+=letters.get(0);

                }else {

                    search2+=letters.get(i+1);
                }

            }

        }

        return search2;


    }

    ////////////////////////////////////////////////////////////////////////////////////////////////





    public LiveData getProductLiveData(){

        return productLiveData;
    }



    public LiveData getProductNewLiveData(){

        return productNewLiveData;
    }

    public LiveData getHighToLowProductLiveData(){

        return highToLowProductLiveData;
    }

    public LiveData getLowToHighProductLiveData(){

        return lowToHighProductLiveData;
    }

    public LiveData getOfferProductLiveData(){

        return offerProductLiveData;
    }

    public LiveData getSuggestionsLiveData(){

        return suggestionLiveData;
    }



    public LiveData getIsEndOfProductLiveData(){

        return isEndOfProduct;
    }


    public LiveData getIsEndOfNewProductLiveData(){

        return isEndOfNewProduct;
    }



    public LiveData getIsEndOfHighToLowProductLiveData(){

        return isEndOfHighToLowProduct;
    }

    public LiveData getIsEndOfLowToHighProductLiveData(){

        return isEndOfLowToHighProduct;
    }




    public static List<Product_info> sortProduct(List<Product_info> productInfos,boolean ascending){

        List<Product_info> proList=new ArrayList<>();
        proList.addAll(productInfos);

        if (ascending){
            Collections.sort(proList, new Comparator<Product_info>() {
                @Override
                public int compare(Product_info productInfo1, Product_info productInfo2) {
                    return Double.compare(Double.valueOf(productInfo1.getPrice()),Double.valueOf(productInfo2.getPrice()));
                }
            });

        }else {

            Collections.sort(proList,Collections.reverseOrder( new Comparator<Product_info>() {
                @Override
                public int compare(Product_info productInfo1, Product_info productInfo2) {
                    return Double.compare(Double.valueOf(productInfo1.getPrice()),Double.valueOf(productInfo2.getPrice()));
                }
            }));
        }

        return proList;

    }



}
