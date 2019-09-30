package yaseerfarah22.com.pharmacy.View;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.util.Arrays;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import es.dmoral.toasty.Toasty;
import yaseerfarah22.com.pharmacy.Interface.MyListener;
import yaseerfarah22.com.pharmacy.R;
import yaseerfarah22.com.pharmacy.ViewModel.UserCollectionViewModel;
import yaseerfarah22.com.pharmacy.ViewModel.ViewModelFactory;

public class Login extends AppCompatActivity {

    @Inject
    ViewModelFactory viewModelFactory;

    private UserCollectionViewModel userCollectionViewModel;

    private final int success=0;
    private final int warning=1;
    private final int error=2;

    private Button facebookLogin,googleLogin;
    private GoogleSignInClient googleSignInClient;
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private final int  googleRequest=102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Window window=getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.light_blue));


        AndroidInjection.inject(this);

        userCollectionViewModel= ViewModelProviders.of(this,viewModelFactory).get(UserCollectionViewModel.class);

        facebookLogin=(Button)findViewById(R.id.facebook);
        googleLogin=(Button)findViewById(R.id.google);

        //  Google SignIn

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.ClintID))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);


        googleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, googleRequest);
            }
        });



        //Facebook SignIn

        // Creating CallbackManager
        callbackManager = CallbackManager.Factory.create();

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                // currentAccessToken is null if the user is logged out
                if (currentAccessToken != null) {
                    // AccessToken is not null implies user is logged in and hence we sen the GraphRequest

                    userCollectionViewModel.facebookLogin(currentAccessToken,new MyListener() {
                        @Override
                        public void onSuccess() {
                            create_toast("Success Login",success);
                            finish();
                        }

                        @Override
                        public void onFailure(String e) {
                            create_toast("Something wrong happened",error);
                        }
                    });


                }
            }
        };


       facebookLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                LoginManager.getInstance().logInWithReadPermissions(Login.this, Arrays.asList("email", "public_profile"));

            }
        });




    }




    @Override
    public void onActivityResult(int requestCode, int resulrCode, Intent data) {
        super.onActivityResult(requestCode, resulrCode, data);

        if(resulrCode==RESULT_OK){

            if(requestCode==googleRequest){
                //google sign in

                try {
                    // The Task returned from this call is always completed, no need to attach
                    // a listener.
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                    GoogleSignInAccount account = task.getResult(ApiException.class);

                    userCollectionViewModel.googleLogin(account, new MyListener() {
                        @Override
                        public void onSuccess() {
                            create_toast("Success Login",success);
                            finish();
                        }

                        @Override
                        public void onFailure(String e) {
                            create_toast("Something wrong happened",error);
                        }
                    });

                } catch (ApiException e) {
                    // The ApiException status code indicates the detailed failure reason.
                    Log.w("google sign in error", "signInResult:failed code=" + e.getStatusCode());
                }

            }
            else {
                callbackManager.onActivityResult(requestCode,resulrCode,data);
            }

        }
        else {

        }
    }






    @Override
    protected void onStart() {
        super.onStart();

        accessTokenTracker.startTracking();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
    }





    private void create_toast(String text,int type){

        switch (type){

            case success:
                Toasty.success(this,text,Toast.LENGTH_LONG,true).show();
                break;

            case warning:
                Toasty.warning(this,text,Toast.LENGTH_LONG,true).show();
                break;


            case error:
                Toasty.error(this,text,Toast.LENGTH_LONG,true).show();
                break;

        }

    }




}
