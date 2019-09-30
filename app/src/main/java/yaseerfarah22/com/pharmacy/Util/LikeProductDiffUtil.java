package yaseerfarah22.com.pharmacy.Util;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import java.util.List;

import yaseerfarah22.com.pharmacy.Model.Like_info;
import yaseerfarah22.com.pharmacy.Model.Product_info;

/**
 * Created by DELL on 9/13/2019.
 */

public class LikeProductDiffUtil extends DiffUtil.Callback {

    private List<Product_info> oldList;
    private List<Product_info> newList;

    Context context;

    public LikeProductDiffUtil(Context context, List<Product_info> oldList, List<Product_info> newList) {
        this.oldList = oldList;
        this.newList = newList;
        this.context=context;
    }



    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getId().trim().matches(newList.get(newItemPosition).getId().trim());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return false;
    }


    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {

        Bundle bundle=new Bundle();

        bundle.putString("Like","Like");

        if (bundle.size()>0){
            return bundle;
        }else {
            return super.getChangePayload(oldItemPosition, newItemPosition);
        }


    }
}
