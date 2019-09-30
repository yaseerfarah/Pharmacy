package yaseerfarah22.com.pharmacy.ViewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import javax.inject.Inject;

import yaseerfarah22.com.pharmacy.Interface.LikeListener;
import yaseerfarah22.com.pharmacy.Interface.LiveDataListener;
import yaseerfarah22.com.pharmacy.Interface.MyCallback;
import yaseerfarah22.com.pharmacy.Interface.MyListener;
import yaseerfarah22.com.pharmacy.Interface.OrderListener;
import yaseerfarah22.com.pharmacy.Interface.UserInfoListener;
import yaseerfarah22.com.pharmacy.Model.Cart_info;
import yaseerfarah22.com.pharmacy.Model.DocumentSnapListener;
import yaseerfarah22.com.pharmacy.Model.Like_info;
import yaseerfarah22.com.pharmacy.Model.Order_info;
import yaseerfarah22.com.pharmacy.Model.Product_info;
import yaseerfarah22.com.pharmacy.Model.User_info;
import yaseerfarah22.com.pharmacy.Repository.FacebookGoogleLogin;
import yaseerfarah22.com.pharmacy.Repository.FirestoreMethod;
import yaseerfarah22.com.pharmacy.Repository.ListenFirestoreItem;
import yaseerfarah22.com.pharmacy.Repository.ListenFirestoreItemByID;
import yaseerfarah22.com.pharmacy.Repository.SharedPreferencesMethod;

/**
 * Created by DELL on 5/5/2019.
 */

public class UserCollectionViewModel extends ViewModel  {



    private MediatorLiveData<List<Cart_info>> cartLiveData=new MediatorLiveData<>();
    private MediatorLiveData<List<Order_info>> orderLiveData=new MediatorLiveData<>();
    private MediatorLiveData<User_info> userLiveData=new MediatorLiveData<>();
    private List<Cart_info>cart_infos=new ArrayList<>();
    private List<Like_info>likesList=new ArrayList<>();
    private List<Order_info>orderList=new ArrayList<>();
    private boolean isLogin;

    private ListenFirestoreItemByID cartSource;
    private ListenFirestoreItemByID orderSource;
    private ListenFirestoreItemByID likeSource;

    private MediatorLiveData<List<Like_info>> likeLiveData=new MediatorLiveData<>();
    private FirestoreMethod firestoreMethod;
    private Context context;
    private ListenFirestoreItem cartListen;
    private SharedPreferencesMethod sharedPreferencesMethod;
    private FacebookGoogleLogin facebookGoogleLogin;



    @Inject
    public UserCollectionViewModel(Context context, FirestoreMethod firestoreMethod, SharedPreferencesMethod sharedPreferencesMethod, FacebookGoogleLogin facebookGoogleLogin){

        this.context=context;
        this.firestoreMethod=firestoreMethod;
        this.sharedPreferencesMethod=sharedPreferencesMethod;
        this.facebookGoogleLogin=facebookGoogleLogin;




        isLogin=getisLoginSharedPre();
        getUserInfo();


        if (isLogin&&userLiveData.getValue()!=null){
            addSources(userLiveData.getValue().getUser_id());

            cartAddSource();
            likeAddSource();
            orderAddSource();
        }else {
           /* cartRemoveSource();
            orderRemoveSource();
            likeRemoveSource();*/
        }





    }


    public LiveData getCartLiveData(){

        return cartLiveData;
    }






    public LiveData getLikeLiveData(){

        return likeLiveData;
    }


    public LiveData getOrderLiveData(){

        return orderLiveData;
    }


    public LiveData getUserLiveData(){

        return userLiveData;
    }



    private boolean getisLoginSharedPre(){
        return sharedPreferencesMethod.getBoolean("IsLogin");
    }

    public boolean isUserLogin(){
        return isLogin;
    }


    private void getUserInfo(){

        if (isUserLogin()){
            User_info userInfo=  sharedPreferencesMethod.getUserInfo("UserInfo");
            userLiveData.setValue(userInfo);
        }


    }

    public void logOut(){

        facebookGoogleLogin.logOut();
        sharedPreferencesMethod.setBoolean("IsLogin",false);
        isLogin=false;

        cartRemoveSource();
        orderRemoveSource();
        likeRemoveSource();

    }


    public void facebookLogin(final AccessToken accessToken, final MyListener myListener){
        facebookGoogleLogin.facebookLogin(accessToken, new MyListener() {
            @Override
            public void onSuccess() {
                sharedPreferencesMethod.setBoolean("IsLogin",true);
                isLogin=true;

                //Get User Info from Facebook

                facebookGoogleLogin.getFacebookUserInfo(accessToken, new UserInfoListener() {
                    @Override
                    public void onSuccess(User_info userInfo) {
                        final User_info user_info=userInfo;

                        // Check if user is already in firestor

                        firestoreMethod.getInfoByID("User", user_info.getUser_id().trim(), User_info.class, new MyCallback() {
                            @Override
                            public void onCallback(List list, String actionType, DocumentSnapshot lastResult) {

                                if (list==null){

                                    // User not in Firestor
                                       // so Set the User in Firestore

                                    firestoreMethod.addToDatabase("User", user_info, user_info.getUser_id().trim(), new MyListener() {
                                        @Override
                                        public void onSuccess() {
                                            myListener.onSuccess();
                                            addSources(user_info.getUser_id());
                                            cartAddSource();
                                            likeAddSource();
                                            orderAddSource();
                                        }

                                        @Override
                                        public void onFailure(String e) {
                                            myListener.onFailure(e);
                                        }
                                    });

                                    //set User in Shared Preference
                                    sharedPreferencesMethod.setObject("UserInfo",user_info);

                                    userLiveData.postValue(user_info);

                                }else {
                                    // User already in Firestor
                                    // so Get the User from Firestore

                                    List<User_info> user_infoList=list;
                                    User_info userInfoFirestore=user_infoList.get(0);

                                    //set User in Shared Preference
                                    sharedPreferencesMethod.setObject("UserInfo",userInfoFirestore);

                                    userLiveData.postValue(userInfoFirestore);
                                    myListener.onSuccess();
                                    addSources(userInfoFirestore.getUser_id());
                                    cartAddSource();
                                    likeAddSource();
                                    orderAddSource();


                                }



                            }

                            @Override
                            public void onFailure(String e) {
                                myListener.onFailure(e);
                            }
                        });


                    }

                    @Override
                    public void onFailure(String e) {
                        myListener.onFailure(e);
                    }
                });

            }

            @Override
            public void onFailure(String e) {
                myListener.onFailure(e);
            }
        });
    }





    public void googleLogin(final GoogleSignInAccount account, final MyListener myListener){

        facebookGoogleLogin.googleLogin(account, new MyListener() {
            @Override
            public void onSuccess() {

                sharedPreferencesMethod.setBoolean("IsLogin",true);
                isLogin=true;
                //Get User Info from Google

                final User_info userInfo=facebookGoogleLogin.getGoogleUserInfo(account);

                // Check if user is already in firestor

                firestoreMethod.getInfoByID("User", userInfo.getUser_id().trim(), User_info.class, new MyCallback() {
                    @Override
                    public void onCallback(List list, String actionType, DocumentSnapshot lastResult) {

                        if (list==null){

                            // User not in Firestor
                            // so Set the User in Firestore

                            firestoreMethod.addToDatabase("User", userInfo, userInfo.getUser_id().trim(), new MyListener() {
                                @Override
                                public void onSuccess() {
                                    myListener.onSuccess();
                                    addSources(userInfo.getUser_id());
                                    cartAddSource();
                                    likeAddSource();
                                    orderAddSource();
                                }

                                @Override
                                public void onFailure(String e) {
                                    myListener.onFailure(e);
                                }
                            });

                            //set User in Shared Preference
                            sharedPreferencesMethod.setObject("UserInfo",userInfo);

                            userLiveData.postValue(userInfo);

                        }else {
                            // User already in Firestor
                            // so Get the User from Firestore

                            List<User_info> user_infoList=list;
                            User_info userInfoFirestore=user_infoList.get(0);

                            //set User in Shared Preference
                            sharedPreferencesMethod.setObject("UserInfo",userInfoFirestore);

                            userLiveData.postValue(userInfoFirestore);
                            myListener.onSuccess();
                            addSources(userInfoFirestore.getUser_id());
                            cartAddSource();
                            likeAddSource();
                            orderAddSource();


                        }

                    }

                    @Override
                    public void onFailure(String e) {
                        myListener.onFailure(e);
                    }
                });

            }

            @Override
            public void onFailure(String e) {

            }
        });


    }



    private void addSources(String id){
        cartSource=new ListenFirestoreItemByID("Cart", id, "userId", context, new LiveDataListener() {
            @Override
            public void onActive() {

            }

            @Override
            public void onInActive() {
                cart_infos.clear();
            }
        });

        orderSource=new ListenFirestoreItemByID("Order", id, "user_id", context, new LiveDataListener() {
            @Override
            public void onActive() {

            }

            @Override
            public void onInActive() {
                orderList.clear();
            }
        });

        likeSource=new ListenFirestoreItemByID("Like", id, "userId", context, new LiveDataListener() {
            @Override
            public void onActive() {

            }

            @Override
            public void onInActive() {
                likesList.clear();
            }
        });
    }




    public boolean isInCart(String proID){


        for (Cart_info cartInfo:cart_infos){
            if (cartInfo.getProduct_id().trim().matches(proID.trim())){
                return true;
            }
        }

        return false;

    }



    public void updateUser(final User_info userInfo, final MyListener myListener){

        firestoreMethod.addToDatabase("User", userInfo, userInfo.getUser_id().trim(), new MyListener() {
            @Override
            public void onSuccess() {
                //set User in Shared Preference
                sharedPreferencesMethod.setObject("UserInfo",userInfo);
                myListener.onSuccess();
            }

            @Override
            public void onFailure(String e) {
                myListener.onFailure(e);
            }
        });


    }




   public void addLike(Product_info product_info, final LikeListener myListener){

        User_info userInfo=userLiveData.getValue();
        String likeId=firestoreMethod.getRandomID("Like");
       final Like_info likes=new Like_info(likeId,userInfo.getUser_id().trim(),product_info);
       firestoreMethod.addToDatabase("Like", likes, likeId, new MyListener() {
           @Override
           public void onSuccess() {
               myListener.onSuccess(likes);
           }

           @Override
           public void onFailure(String e) {
               myListener.onFailure(e);
               Toast.makeText(context,e,Toast.LENGTH_SHORT).show();
           }
       });

   }


    public void addCart(String quantity, Product_info product_info,String price , final MyListener myListener){

        User_info userInfo=userLiveData.getValue();
        String cartId=firestoreMethod.getRandomID("Cart");
        Cart_info cart_info=new Cart_info(cartId,userInfo.getUser_id(),product_info.getId(),quantity,product_info.getName(),product_info.getCategory(),product_info.getSub_category(),price,product_info.getImages_url().get(0));

        firestoreMethod.addToDatabase("Cart", cart_info, cartId, new MyListener() {
            @Override
            public void onSuccess() {
                myListener.onSuccess();
            }

            @Override
            public void onFailure(String e) {
                myListener.onFailure(e);
                Toast.makeText(context,e,Toast.LENGTH_SHORT).show();
            }
        });

    }



    public void addOrder(User_info userInfo,final String order_st, final String order_md, final String order_dt, final OrderListener orderListener){

       // User_info userInfo=userLiveData.getValue();
        firestoreMethod.addOrder(cart_infos, order_st, order_md, order_dt, userInfo, new OrderListener() {
            @Override
            public void onSuccess(final List<Cart_info> cartInfoList) {


                    deleteSpecificCart(cartInfoList);
                    orderListener.onSuccess(cart_infos);

            }

            @Override
            public void onFailure(String e) {

                orderListener.onFailure(e);
                Toast.makeText(context,e,Toast.LENGTH_SHORT).show();

            }
        });

    }




    private void deleteSpecificCart(List<Cart_info> cartInfoList ){



        if(cartInfoList.size()>0) {

            for (Cart_info cartInfo : cartInfoList) {
                for (int i = 0; i < cart_infos.size(); i++) {
                    if (cart_infos.get(i).getCartId().trim().matches(cartInfo.getCartId().trim())) {
                        cart_infos.remove(i);
                    }
                }
            }

        }

        if (cart_infos.size()>0){

            for (Cart_info cartInfo:cart_infos){
                cartInfo.setOutOfQuantity(true);
            }

        }



        cartLiveData.postValue(cart_infos);


    }



    private void cartAddSource(){

        cartLiveData.addSource(cartSource, new Observer<List<DocumentSnapListener>>() {
            @Override
            public void onChanged(@Nullable List<DocumentSnapListener> documentSnapListeners) {

                if (documentSnapListeners.size()>0) {

                    for (DocumentSnapListener documentSnapListener:documentSnapListeners) {

                        switch (documentSnapListener.getActionType()) {

                            case ListenFirestoreItem.addedTag:

                                Cart_info cart_info = documentSnapListener.getDocumentSnapshot().toObject(Cart_info.class) != null ? documentSnapListener.getDocumentSnapshot().toObject(Cart_info.class) : new Cart_info();
                                cart_infos.add(cart_info);
                                break;

                           /* case ListenFirestoreItem.removedTag:

                                for (int i = 0; i < cart_infos.size(); i++) {
                                    if (cart_infos.get(i).getCartId() == documentSnapListener.getDocumentSnapshot().toObject(Cart_info.class).getCartId()) {
                                        cart_infos.remove(i);
                                        break;
                                    }
                                }

                                break;*/

                        }
                    }



                }
                cartLiveData.postValue(cart_infos);
            }
        });

    }





    private void cartRemoveSource(){

        cartLiveData.removeSource(cartSource);
        cartSource.removeListener();
        cart_infos.clear();
        cartLiveData.postValue(cart_infos);

    }












    private void orderAddSource(){


        orderLiveData.addSource(orderSource, new Observer<List<DocumentSnapListener>>() {
            @Override
            public void onChanged(@Nullable List<DocumentSnapListener> documentSnapListeners) {
                if (documentSnapListeners.size()>0) {

                    for (DocumentSnapListener documentSnapListener:documentSnapListeners) {

                        switch (documentSnapListener.getActionType()) {

                            case ListenFirestoreItem.addedTag:

                                Order_info order_info = documentSnapListener.getDocumentSnapshot().toObject(Order_info.class) != null ? documentSnapListener.getDocumentSnapshot().toObject(Order_info.class) : new Order_info();
                                orderList.add(order_info);
                                break;

                           /* case ListenFirestoreItem.removedTag:

                                for (int i = 0; i < cart_infos.size(); i++) {
                                    if (cart_infos.get(i).getCartId() == documentSnapListener.getDocumentSnapshot().toObject(Cart_info.class).getCartId()) {
                                        cart_infos.remove(i);
                                        break;
                                    }
                                }

                                break;*/

                        }
                    }



                }
                orderLiveData.postValue(orderList);
            }
        });

    }


    private void orderRemoveSource(){

        orderLiveData.removeSource(orderSource);
        orderSource.removeListener();
        orderList.clear();
        orderLiveData.postValue(orderList);

    }




    private void likeAddSource(){



        likeLiveData.addSource(likeSource, new Observer<List<DocumentSnapListener>>() {
            @Override
            public void onChanged(@Nullable List<DocumentSnapListener> documentSnapListeners) {
                //boolean isdone=false;
                if (documentSnapListeners.size()>0) {

                    for (DocumentSnapListener documentSnapListener:documentSnapListeners) {

                        switch (documentSnapListener.getActionType()) {

                            case ListenFirestoreItem.addedTag:

                                Like_info likes = documentSnapListener.getDocumentSnapshot().toObject(Like_info.class) != null ? documentSnapListener.getDocumentSnapshot().toObject(Like_info.class) : new Like_info();
                                likesList.add(likes);


                                break;

                           /* case ListenFirestoreItem.removedTag:

                                for (int i = 0; i < cart_infos.size(); i++) {
                                    if (cart_infos.get(i).getCartId() == documentSnapListener.getDocumentSnapshot().toObject(Cart_info.class).getCartId()) {
                                        cart_infos.remove(i);
                                        break;
                                    }
                                }

                                break;*/

                        }
                    }



                }



                    likeLiveData.postValue(likesList);
            }
        });

    }



    private void likeRemoveSource(){

        likeLiveData.removeSource(likeSource);
        likeSource.removeListener();
        likesList.clear();
        likeLiveData.postValue(likesList);

    }



    public void deleteCart(final Cart_info cart_info, final MyListener myListener){

        firestoreMethod.deleteFromDatabase("Cart", cart_info.getCartId(), new MyListener() {
            @Override
            public void onSuccess() {

                for (int i = 0; i < cart_infos.size(); i++) {
                    if (cart_infos.get(i).getCartId().trim().matches(cart_info.getCartId().trim())) {
                        cart_infos.remove(i);
                        break;
                    }
                }

                cartLiveData.postValue(cart_infos);

                myListener.onSuccess();

            }

            @Override
            public void onFailure(String e) {
              //  Toast.makeText(context,"error del cart",Toast.LENGTH_LONG).show();
                myListener.onFailure(e);
            }
        });



    }






    public void deleteOrder(final Order_info order_info, final MyListener myListener){

        firestoreMethod.deleteOrder(order_info, new MyListener() {
            @Override
            public void onSuccess() {
                for (int i = 0; i < orderList.size(); i++) {
                    if (orderList.get(i).getOrder_id().trim().matches(order_info.getOrder_id().trim())) {
                        orderList.remove(i);
                        break;
                    }
                }
                orderLiveData.postValue(orderList);

                myListener.onSuccess();
            }

            @Override
            public void onFailure(String e) {
                //Toast.makeText(context,"error del order",Toast.LENGTH_LONG).show();
                myListener.onFailure(e);
            }
        });


    }





    public void deleteLike(final Like_info likes, final MyListener myListener){

        firestoreMethod.deleteFromDatabase("Like", likes.getLikeId(), new MyListener() {
            @Override
            public void onSuccess() {


                for (int i = 0; i < likesList.size(); i++) {
                    if (likesList.get(i).getLikeId().trim().matches(likes.getLikeId().trim())) {
                        likesList.remove(i);
                        break;
                    }
                }

                likeLiveData.postValue(likesList);
                myListener.onSuccess();
            }

            @Override
            public void onFailure(String e) {
                //Toast.makeText(context,"error del like",Toast.LENGTH_LONG).show();

                myListener.onFailure(e);
            }
        });



    }


    public void removeSourceListener(){

        if (isLogin){
            cartSource.removeListener();
            likeSource.removeListener();
            orderSource.removeListener();
        }

    }

}
