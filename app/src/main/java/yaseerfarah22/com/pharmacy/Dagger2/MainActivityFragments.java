package yaseerfarah22.com.pharmacy.Dagger2;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import yaseerfarah22.com.pharmacy.View.Cart;
import yaseerfarah22.com.pharmacy.View.CheckOut;
import yaseerfarah22.com.pharmacy.View.Home;
import yaseerfarah22.com.pharmacy.View.Like;
import yaseerfarah22.com.pharmacy.View.List;
import yaseerfarah22.com.pharmacy.View.Offers;
import yaseerfarah22.com.pharmacy.View.Order;
import yaseerfarah22.com.pharmacy.View.ProductDetails;
import yaseerfarah22.com.pharmacy.View.Search;
import yaseerfarah22.com.pharmacy.View.UserProfile;


/**
 * Created by DELL on 5/18/2019.
 */
@Module
public abstract class MainActivityFragments {


    @ContributesAndroidInjector()
    abstract Cart contributeCartFragment();

    @ContributesAndroidInjector()
    abstract Home contributeHomeFragment();

    @ContributesAndroidInjector()
    abstract Offers contributeOffersFragment();

    @ContributesAndroidInjector()
    abstract ProductDetails contributeProductDetailsFragment();

    @ContributesAndroidInjector()
    abstract Order contributeOrderFragment();

    @ContributesAndroidInjector()
    abstract Like contributeLikeFragment();

    @ContributesAndroidInjector()
    abstract List contributeListFragment();

    @ContributesAndroidInjector()
    abstract CheckOut contributeCheckOutFragment();

    @ContributesAndroidInjector()
    abstract UserProfile contributeUserProfileFragment();

    @ContributesAndroidInjector()
    abstract Search contributeSearchFragment();




}
