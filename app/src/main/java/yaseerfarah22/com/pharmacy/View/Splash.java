package yaseerfarah22.com.pharmacy.View;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import yaseerfarah22.com.pharmacy.R;


public class Splash extends AppCompatActivity {

    Handler handler=new Handler();
    Runnable runnable=new Runnable() {
        @Override
        public void run() {
          close();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
        );

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
        );

       handler.postDelayed(runnable,4000);
    }



    private void close(){
        startActivity(new Intent(Splash.this,MainActivity.class));
        finish();
    }


}
