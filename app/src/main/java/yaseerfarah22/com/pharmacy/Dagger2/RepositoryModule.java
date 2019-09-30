package yaseerfarah22.com.pharmacy.Dagger2;


import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import yaseerfarah22.com.pharmacy.Repository.FacebookGoogleLogin;
import yaseerfarah22.com.pharmacy.Repository.FirestoreMethod;
import yaseerfarah22.com.pharmacy.Repository.SharedPreferencesMethod;


/**
 * Created by DELL on 5/24/2019.
 */

@Module
public class RepositoryModule {

    @Provides
    @Singleton
    public FirestoreMethod firestoreMethod(){

       return new FirestoreMethod();
    }



    @Provides
    @Singleton
    public SharedPreferences sharedPreferences(Context context){

        return context.getSharedPreferences("UserInfo",Context.MODE_PRIVATE);
    }


    @Provides
    @Singleton
    public FacebookGoogleLogin facebookGoogleLogin(){

        return new FacebookGoogleLogin();

    }


    @Provides
    @Singleton
    public SharedPreferencesMethod sharedPreferencesMethod(SharedPreferences sharedPreferences){

       return new SharedPreferencesMethod(sharedPreferences);

    }


}
