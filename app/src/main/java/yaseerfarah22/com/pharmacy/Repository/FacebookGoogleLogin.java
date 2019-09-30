package yaseerfarah22.com.pharmacy.Repository;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import yaseerfarah22.com.pharmacy.Interface.MyListener;
import yaseerfarah22.com.pharmacy.Interface.UserInfoListener;
import yaseerfarah22.com.pharmacy.Model.User_info;

/**
 * Created by DELL on 7/31/2019.
 */

public class FacebookGoogleLogin {

    private FirebaseAuth firebaseAuth;


    public FacebookGoogleLogin() {
        this.firebaseAuth = FirebaseAuth.getInstance();
    }


    public void facebookLogin(AccessToken token, final MyListener myListener) {

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                myListener.onSuccess();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                myListener.onFailure(e.getMessage().toString());
            }
        });

    }


    public void googleLogin(GoogleSignInAccount account, final MyListener myListener) {

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                myListener.onSuccess();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                myListener.onFailure(e.getMessage().toString());
            }
        });

    }


    public void logOut() {

        firebaseAuth.signOut();

    }


    public void getFacebookUserInfo(AccessToken token, final UserInfoListener userInfoListener) {

        /**
         Creating the GraphRequest to fetch user details
         1st Param - AccessToken
         2nd Param - Callback (which will be invoked once the request is successful)
         **/


        GraphRequest request = GraphRequest.newMeRequest(token, new GraphRequest.GraphJSONObjectCallback() {
            //OnCompleted is invoked once the GraphRequest is successful
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    String firstN = "";
                    String lastN = "";
                    String[] name = object.getString("name").split(" ");
                    if (name.length > 1) {
                        firstN = name[0];
                        lastN = name[name.length - 1];
                    } else {
                        firstN = name[name.length - 1];
                    }

                    User_info userInfo = new User_info(object.getString("email").trim(), firstN, lastN, object.getString("email").trim(), object.getJSONObject("picture").getJSONObject("data").getString("url").trim(), "", "", "", "0");

                    userInfoListener.onSuccess(userInfo);

                } catch (JSONException e) {
                    //e.printStackTrace();
                    userInfoListener.onFailure(e.getMessage().toString());

                }
            }
        });
        // We set parameters to the GraphRequest using a Bundle.
        Bundle bundle = new Bundle();
        bundle.putString("fields", "id,name,email,picture.width(200)");
        request.setParameters(bundle);
        // Initiate the GraphRequest
        request.executeAsync();

    }


    public User_info getGoogleUserInfo(GoogleSignInAccount account) {

        String firstN = "";
        String lastN = "";
        String[] name = account.getDisplayName().split(" ");
        if (name.length > 1) {
            firstN = name[0];
            lastN = name[name.length - 1];
        } else {
            firstN = name[name.length - 1];
        }

        return new User_info(account.getEmail().trim(),firstN,lastN,account.getEmail().trim(),account.getPhotoUrl().toString(),"","","","0");


    }


}