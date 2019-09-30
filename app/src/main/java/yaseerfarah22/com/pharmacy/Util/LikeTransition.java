package yaseerfarah22.com.pharmacy.Util;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;

import com.bumptech.glide.load.resource.bitmap.DrawableTransformation;

/**
 * Created by DELL on 9/19/2019.
 */

public class LikeTransition extends TransitionDrawable {

    private boolean isLiked=false;

    public LikeTransition(Drawable[] layers) {
        super(layers);
    }

    public boolean getIsLiked(){
        return isLiked;
    }

    @Override
    public void startTransition(int durationMillis) {
        isLiked=true;
        super.startTransition(durationMillis);
    }


    @Override
    public void reverseTransition(int duration) {
        isLiked=false;
        super.reverseTransition(duration);
    }


    @Override
    public void resetTransition() {
        isLiked=false;
        super.resetTransition();
    }


}


