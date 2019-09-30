package yaseerfarah22.com.pharmacy.Adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.List;

import yaseerfarah22.com.pharmacy.R;


/**
 * Created by DELL on 1/19/2019.
 */

public class SliderAdapter extends PagerAdapter {
    private Context context;

    private List<String> slider_image;


    public SliderAdapter(Context context, List<String> slider_image) {
        this.context = context;
        this.slider_image = slider_image;
    }

    @Override
    public int getCount() {

        return slider_image.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==(ConstraintLayout)object;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view;

            view = LayoutInflater.from(container.getContext()).inflate(R.layout.slider, container, false);

            ImageView imageView=(ImageView) view.findViewById(R.id.image_slider);
            Glide.with(context).load(slider_image.get(position))
                    .into(imageView);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ConstraintLayout)object);
    }




}
