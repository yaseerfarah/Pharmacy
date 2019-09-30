package yaseerfarah22.com.pharmacy.Dagger2;

import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.content.SharedPreferences;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;

import javax.inject.Provider;
import javax.inject.Singleton;

import dagger.MapKey;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;
import yaseerfarah22.com.pharmacy.Repository.FacebookGoogleLogin;
import yaseerfarah22.com.pharmacy.Repository.FirestoreMethod;
import yaseerfarah22.com.pharmacy.Repository.SharedPreferencesMethod;
import yaseerfarah22.com.pharmacy.ViewModel.ProductViewModel;
import yaseerfarah22.com.pharmacy.ViewModel.UserCollectionViewModel;
import yaseerfarah22.com.pharmacy.ViewModel.ViewModelFactory;


/**
 * Created by DELL on 5/16/2019.
 */
@Module
public class ViewModelModule {

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @MapKey
    @interface ViewModelKey {
        Class<? extends ViewModel> value();
    }





    @Provides
    ViewModelFactory viewModelFactory(Map<Class<? extends ViewModel>, Provider<ViewModel>> providerMap) {

        return new ViewModelFactory(providerMap);
    }

    @Provides
    @Singleton
    @IntoMap
    @ViewModelKey(ProductViewModel.class)
    ViewModel productViewModel(Context context, FirestoreMethod firestoreMethod) {
        return new ProductViewModel(context,firestoreMethod);
    }


    @Provides
    @Singleton
    @IntoMap
    @ViewModelKey(UserCollectionViewModel.class)
    ViewModel userCollectoinViewModel(Context context, FirestoreMethod firestoreMethod, SharedPreferencesMethod sharedPreferencesMethod, FacebookGoogleLogin facebookGoogleLogin) {
        return new UserCollectionViewModel(context,firestoreMethod,sharedPreferencesMethod,facebookGoogleLogin);
    }

}
