package yaseerfarah22.com.pharmacy.View;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Network;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.transition.Explode;
import android.support.transition.Fade;
import android.support.transition.Slide;
import android.support.transition.TransitionInflater;
import android.support.transition.TransitionSet;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;
import yaseerfarah22.com.pharmacy.Interface.OrderFilterFragments;
import yaseerfarah22.com.pharmacy.Interface.OrderFilterMethod;
import yaseerfarah22.com.pharmacy.Interface.StatusBarColor;
import yaseerfarah22.com.pharmacy.Model.Cart_info;
import yaseerfarah22.com.pharmacy.Model.Like_info;
import yaseerfarah22.com.pharmacy.Model.Order_info;
import yaseerfarah22.com.pharmacy.Model.Product_info;
import yaseerfarah22.com.pharmacy.Model.User_info;
import yaseerfarah22.com.pharmacy.R;
import yaseerfarah22.com.pharmacy.Util.NetworkReceiver;
import yaseerfarah22.com.pharmacy.ViewModel.UserCollectionViewModel;
import yaseerfarah22.com.pharmacy.ViewModel.ViewModelFactory;

public class MainActivity extends AppCompatActivity implements HasSupportFragmentInjector,StatusBarColor,OrderFilterMethod ,ProductDetails.OnProductDetailsInteractionListener,BottomNavigationView.OnNavigationItemSelectedListener,Serializable {
    public static final long MOVE_DEFAULT_TIME = 300;
    public static final long FADE_DEFAULT_TIME = 200;
    public  boolean isConnecting=true;
    public static final String HOME = "Home";
    public static final String OFFERS = "Offers";
    public static final String ACCOUNT = "Account";
    public static final String CART = "Cart";
    public static final String LIST = "List";
    public static final String LIKE = "Like";
    public static final String NETWORK = "Network";

    private long timeExit=System.currentTimeMillis();

    Toolbar toolbar;
    BottomNavigationViewEx bottomNavigationView;
    public static User_info user_info=null;
    private  String TAG="Hash Key";
    private QBadgeView cartBadge;



    private FrameLayout frameLayout;

    private NetworkReceiver networkReceiver;


    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;
    @Inject
    ViewModelFactory viewModelFactory;

    Observer cartObserver;
    Observer likeObserver;
    Observer orderObserver;

    private UserCollectionViewModel userCollectionViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        networkReceiver=new NetworkReceiver(this);

        //sendBroadcastActivity();
        //setStatusBarColor(ContextCompat.getColor(this,R.color.gray));

        AndroidInjection.inject(this);

        userCollectionViewModel= ViewModelProviders.of(this,viewModelFactory).get(UserCollectionViewModel.class);

       /* toolbar = (Toolbar) findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        */




        bottomNavigationView=(BottomNavigationViewEx) findViewById(R.id.bottom_navigation);
        frameLayout=(FrameLayout) findViewById(R.id.main_frame);


      /*  userCollectionViewModel.getCartLiveData().observe(this, new Observer<java.util.List<Cart_info>>() {
            @Override
            public void onChanged(@Nullable java.util.List<Cart_info> cart_infos) {

                if (cart_infos.size()>0){
                   addBadgeAt(3,cart_infos.size());

                }else {
                    removeBadge(3,cartBadge);
                }

            }

        });*/


        cartObserver=new Observer<java.util.List<Cart_info>>() {
            @Override
            public void onChanged(@Nullable java.util.List<Cart_info> cart_infos) {

                if (cart_infos.size()>0){
                    addBadgeAt(3,cart_infos.size());

                }else {
                    if (cartBadge!=null){
                        removeBadge(3,cartBadge);
                    }

                }

            }

        };

        likeObserver=new Observer<java.util.List<Like_info>>() {
            @Override
            public void onChanged(@Nullable java.util.List<Like_info> like_infos) {

            }

        };


        orderObserver=new Observer<java.util.List<Order_info>>() {
            @Override
            public void onChanged(@Nullable java.util.List<Order_info> order_infos) {

            }

        };








        cartBadge=(QBadgeView) new QBadgeView(this)
                .setGravityOffset(12, 2, true)
                .bindTarget(bottomNavigationView.getBottomNavigationItemView(3));

        bottomNavigationView.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);

        bottomNavigationView.enableShiftingMode(false);

       bottomNavigationView.enableAnimation(false);
        bottomNavigationView.setCurrentItem(0);
        homeOpen();
        bottomNavigationView.enableItemShiftingMode(false);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);




    }

////////////////////////////////////////////////////////////////////////////////////////////


    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.search:

                if (!isFragmentOpened(HOME)&&isConnecting){
                    setExitTransitions(false);
                    homeOpen();
                }

                return true;

            case R.id.offers:
                if (!isFragmentOpened(OFFERS)&&isConnecting) {
                    setExitTransitions(false);
                    offersOpen();
                }
                return true;

            case R.id.my_account:
                if (!isFragmentOpened(ACCOUNT)&&isConnecting) {
                    setExitTransitions(false);
                    if (userCollectionViewModel.isUserLogin()) {
                        userProfileOpen();
                    } else {
                        loginOpen();
                    }
                }
                return true;

            case R.id.my_cart:
                if (!isFragmentOpened(CART)&&isConnecting) {
                    setExitTransitions(false);
                    if (userCollectionViewModel.isUserLogin()) {
                        cartOpen();
                    } else {
                        loginOpen();
                    }
                }
                return true;

        }

        return false;
    }



    private void addBadgeAt(int position, int number) {
        // add badge

        cartBadge.setVisibility(View.VISIBLE);
        cartBadge.setBadgeNumber(number);


    }


    private void removeBadge(int position, QBadgeView badgeView) {
        badgeView.hide(true);
        badgeView.setVisibility(View.INVISIBLE);
    }



    private void setExitTransitions(boolean enable){


        if (getSupportFragmentManager().findFragmentByTag(HOME)!=null&&getSupportFragmentManager().findFragmentByTag(HOME).isVisible()){

            if (enable){
                getSupportFragmentManager().findFragmentByTag(HOME).setExitTransition(new Fade().setDuration(FADE_DEFAULT_TIME));
            }else {
                getSupportFragmentManager().findFragmentByTag(HOME).setExitTransition(null);
            }

        }else if (getSupportFragmentManager().findFragmentByTag(OFFERS)!=null&&getSupportFragmentManager().findFragmentByTag(OFFERS).isVisible()){

            if (enable){
                getSupportFragmentManager().findFragmentByTag(OFFERS).setExitTransition(new Fade().setDuration(FADE_DEFAULT_TIME));
            }else {
                getSupportFragmentManager().findFragmentByTag(OFFERS).setExitTransition(null);
            }

        }else if (getSupportFragmentManager().findFragmentByTag(LIST)!=null&&getSupportFragmentManager().findFragmentByTag(LIST).isVisible()){

            if (enable){
                getSupportFragmentManager().findFragmentByTag(LIST).setExitTransition(new Fade().setDuration(FADE_DEFAULT_TIME));
            }else {
                getSupportFragmentManager().findFragmentByTag(LIST).setExitTransition(null);
            }

        }else if (getSupportFragmentManager().findFragmentByTag(LIKE)!=null&&getSupportFragmentManager().findFragmentByTag(LIKE).isVisible()){

            if (enable){
                getSupportFragmentManager().findFragmentByTag(LIKE).setExitTransition(new Fade().setDuration(FADE_DEFAULT_TIME));
            }else {
                getSupportFragmentManager().findFragmentByTag(LIKE).setExitTransition(null);
            }

        }

    }




    private boolean isFragmentOpened(String fragment){


        if (getSupportFragmentManager().findFragmentByTag(fragment)!=null&&getSupportFragmentManager().findFragmentByTag(fragment).isVisible()){

            return true;

        }else {
            return false;
        }



    }


    private void sendBroadcastActivity(){

        Intent intent =new Intent();
        Bundle bundle=new Bundle();
        bundle.putSerializable("Activity",MainActivity.this);
        intent.setAction("com.MainActivity.Activity");
        intent.putExtras(bundle);
        sendBroadcast(intent);

    }


/////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////




    @Override
    public void orderSelected(Fragment fragment, String order) {


        OrderFilterFragments orderFilterFragments=(OrderFilterFragments)fragment;
        orderFilterFragments.setOrder(order);
        onBackPressed();

    }

    @Override
    public void filterSelected(Fragment fragment, String sub_Category) {



        OrderFilterFragments orderFilterFragments=(OrderFilterFragments)fragment;
        orderFilterFragments.setSubCategory(sub_Category);
        onBackPressed();

    }

    @Override
    public void setStatusBarColor(int color){
        Window window=getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(color);


    }


    public static void slideToTop(final View view, final int alphaD, final int tranD){


       /* view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                view.setVisibility(View.VISIBLE);
                //Toast.makeText(,String.valueOf(view.getMeasuredHeightAndState()),Toast.LENGTH_SHORT).show();
                TranslateAnimation animate;
                animate = new TranslateAnimation(0,0,view.getMeasuredHeightAndState(),0);
                animate.setDuration(tranD);
                AlphaAnimation alphaAnimation=new AlphaAnimation(0,1);
                alphaAnimation.setDuration(alphaD);
                AnimationSet animationSet=new AnimationSet(true);
                animationSet.addAnimation(animate);
                animationSet.addAnimation(alphaAnimation);
                view.startAnimation(animationSet);



                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });*/

        view.post(new Runnable() {
            @Override
            public void run() {
                view.setVisibility(View.VISIBLE);
                //Toast.makeText(getContext(),String.valueOf(view.getMeasuredHeightAndState()),Toast.LENGTH_SHORT).show();
                TranslateAnimation animate;
                animate = new TranslateAnimation(0, 0, view.getMeasuredHeightAndState(), 0);
                animate.setDuration(tranD);
                AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
                alphaAnimation.setDuration(alphaD);
                AnimationSet animationSet = new AnimationSet(true);
                animationSet.addAnimation(animate);
                animationSet.addAnimation(alphaAnimation);
                view.startAnimation(animationSet);
            }
        });




    }


    @Override
    protected void onResume() {

        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(networkReceiver,intentFilter);

        super.onResume();
    }


    @Override
    protected void onPause() {
        unregisterReceiver(networkReceiver);
        super.onPause();
    }

    @Override
    public void onBackPressed() {


        if (getSupportFragmentManager().findFragmentByTag(HOME)!=null&&getSupportFragmentManager().findFragmentByTag(HOME).isVisible()||getSupportFragmentManager().findFragmentByTag(OFFERS)!=null&&getSupportFragmentManager().findFragmentByTag(OFFERS).isVisible()||getSupportFragmentManager().findFragmentByTag(ACCOUNT)!=null&&getSupportFragmentManager().findFragmentByTag(ACCOUNT).isVisible()||getSupportFragmentManager().findFragmentByTag(CART)!=null&&getSupportFragmentManager().findFragmentByTag(CART).isVisible()){



            if (System.currentTimeMillis()<timeExit+2000){

                finish();

            }else {
                timeExit=System.currentTimeMillis();
                Toast.makeText(this,"Press Again To Exit",Toast.LENGTH_SHORT).show();


            }



        }else if(getSupportFragmentManager().findFragmentByTag(NETWORK)!=null&&getSupportFragmentManager().findFragmentByTag(NETWORK).isVisible()) {

            if (isConnecting){
                super.onBackPressed();
            }else {
                finish();
            }


        }else {
            super.onBackPressed();
        }

    }


    ///////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void orderOpen(Fragment fragment,String order){
        Bundle bundle=new Bundle();
        bundle.putSerializable("Fragment", (Serializable) fragment);
        bundle.putInt("OrderOrFilter",OrderFilter.orderLayout);
        bundle.putString("Order",order);
        OrderFilter orderFilter=new OrderFilter();
        orderFilter.setArguments(bundle);
        android.support.v4.app.FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame,orderFilter);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void filterOpen(Fragment fragment,String category,String filter){
        Bundle bundle=new Bundle();
        bundle.putSerializable("Fragment", (Serializable) fragment);
        bundle.putInt("OrderOrFilter",OrderFilter.filterLayout);
        bundle.putString("Category",category);
        bundle.putString("Filter",filter);
        OrderFilter orderFilter=new OrderFilter();
        orderFilter.setArguments(bundle);
        android.support.v4.app.FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame,orderFilter);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }



    public void listOpen(boolean newData,String category){
        Bundle bundle=new Bundle();
        bundle.putBoolean("Order",newData);
        bundle.putString("Category",category);
        List list=new List();
       // list.setExitTransition(new Fade().setDuration(200));
        list.setAllowReturnTransitionOverlap(false);
        list.setArguments(bundle);
        android.support.v4.app.FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame,list,LIST);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }



    public void checkOutOpen(){
        CheckOut checkOut=new CheckOut();
        android.support.v4.app.FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame,checkOut);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }



    public void orderListOpen(boolean backStack){
        Order order=new Order();
        android.support.v4.app.FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame,order);
        if (backStack) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
    }




    public void searchOpen(CardView cardSearch){

        setExitTransitions(true);
        TransitionSet enterSharedElement=new TransitionSet();
        enterSharedElement.addTransition(TransitionInflater.from(this).inflateTransition(android.R.transition.move));
        //enterSharedElement.setStartDelay(FADE_DEFAULT_TIME);
        enterSharedElement.setDuration(MOVE_DEFAULT_TIME);


        Search search=new Search();

        search.setSharedElementEnterTransition(enterSharedElement);
        search.setEnterTransition(new Fade().setStartDelay(MOVE_DEFAULT_TIME).setDuration(FADE_DEFAULT_TIME));

        search.setReturnTransition(new Fade().setDuration(FADE_DEFAULT_TIME));
        //search.setSharedElementReturnTransition(null);

        android.support.v4.app.FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.addSharedElement(cardSearch,cardSearch.getTransitionName());

        fragmentTransaction.replace(R.id.main_frame,search);


        fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commit();
    }


    public void searchResultOpen(String search){
        Bundle bundle=new Bundle();
        bundle.putInt("Type",Like.searchType);
        bundle.putString("Search",search);
        Like like=new Like();
       // like.setExitTransition(new Fade().setDuration(200));
        like.setAllowReturnTransitionOverlap(false);
        like.setArguments(bundle);
        android.support.v4.app.FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame,like,LIKE);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


    public void likeOpen(){
        Bundle bundle=new Bundle();
        bundle.putInt("Type",Like.likeType);

        Like like=new Like();
        //like.setExitTransition(new Fade().setDuration(200));
        like.setAllowReturnTransitionOverlap(false);
        like.setArguments(bundle);
        android.support.v4.app.FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame,like,LIKE);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


    public void homeOpen(){
        Home home=new Home();
        android.support.v4.app.FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        //home.setExitTransition(new Fade().setDuration(200));
        home.setAllowReturnTransitionOverlap(false);
        fragmentTransaction.replace(R.id.main_frame,home,HOME);
        fragmentTransaction.commit();
    }


    public void offersOpen(){
        Offers offers=new Offers();
        //offers.setExitTransition(new Fade().setDuration(200));
        offers.setAllowReturnTransitionOverlap(false);
        android.support.v4.app.FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame,offers,OFFERS);
        fragmentTransaction.commit();
    }


    public void userProfileOpen(){
        UserProfile userProfile=new UserProfile();
        //userProfile.setExitTransition(new Fade().setDuration(200));
        //userProfile.setAllowReturnTransitionOverlap(false);
        android.support.v4.app.FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame,userProfile,ACCOUNT);
        fragmentTransaction.commit();
    }


    public void cartOpen(){
        Cart cart=new Cart();
        //cart.setExitTransition(new Fade().setDuration(200));
        //cart.setAllowReturnTransitionOverlap(false);
        android.support.v4.app.FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame,cart,CART);
        fragmentTransaction.commit();
    }





    public void loginOpen(){

        startActivity(new Intent(MainActivity.this,Login.class));

    }



    public void networkFailed(){

        frameLayout.post(new Runnable() {
            @Override
            public void run() {
                isConnecting=false;
                NetworkFailed networkFailed=new NetworkFailed();
                setExitTransitions(true);
                networkFailed.setEnterTransition(new Slide(Gravity.RIGHT).setDuration(300));
                networkFailed.setAllowEnterTransitionOverlap(false);
                networkFailed.setAllowReturnTransitionOverlap(false);
                android.support.v4.app.FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.main_frame,networkFailed, NETWORK);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });



    }

    /////////////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onFragmentInteraction(Product_info productInfo, Like_info likeInfo) {
        setExitTransitions(true);
        Bundle bundle=new Bundle();
        bundle.putSerializable("Product",productInfo);
        bundle.putSerializable("Like",likeInfo);
        ProductDetails productDetails=new ProductDetails();
        productDetails.setArguments(bundle);
        productDetails.setEnterTransition(new Slide(Gravity.RIGHT).setDuration(200));
        productDetails.setAllowEnterTransitionOverlap(false);
        productDetails.setAllowReturnTransitionOverlap(false);

        android.support.v4.app.FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame,productDetails);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }


    ///////////////////////////////////////////////////////////////////////////////////////////////

    public void printHashKey(Context pContext) {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (android.content.pm.Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i(TAG, "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "printHashKey()", e);
        } catch (Exception e) {
            Log.e(TAG, "printHashKey()", e);
        }
    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////


    @Override
    protected void onStart() {

        userCollectionViewModel.getLikeLiveData().observeForever(likeObserver);
        userCollectionViewModel.getCartLiveData().observeForever(cartObserver);
        userCollectionViewModel.getOrderLiveData().observeForever(orderObserver);

        super.onStart();
    }


    @Override
    protected void onStop() {
        userCollectionViewModel.getCartLiveData().removeObserver(cartObserver);
        userCollectionViewModel.getOrderLiveData().removeObserver(orderObserver);

        userCollectionViewModel.getLikeLiveData().removeObserver(likeObserver);
        super.onStop();
    }

    @Override
    protected void onDestroy() {

       // userCollectionViewModel.removeSourceListener();

        super.onDestroy();
    }
}
