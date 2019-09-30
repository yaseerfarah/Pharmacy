package yaseerfarah22.com.pharmacy.Dagger2;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import yaseerfarah22.com.pharmacy.View.Login;
import yaseerfarah22.com.pharmacy.View.MainActivity;

/**
 * Created by DELL on 5/18/2019.
 */
@Module
public abstract class ActivityBuilder {


    @ContributesAndroidInjector(modules = MainActivityFragments.class)
    abstract MainActivity contributeMainActivity();

    @ContributesAndroidInjector()
    abstract Login contributeLoginActivity();





}
