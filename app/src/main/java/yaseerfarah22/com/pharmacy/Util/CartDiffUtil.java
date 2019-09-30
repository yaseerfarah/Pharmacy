package yaseerfarah22.com.pharmacy.Util;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import java.util.List;

import yaseerfarah22.com.pharmacy.Model.Cart_info;
import yaseerfarah22.com.pharmacy.Model.Order_info;
import yaseerfarah22.com.pharmacy.Model.Product_info;

/**
 * Created by DELL on 9/13/2019.
 */

public class CartDiffUtil extends DiffUtil.Callback {

    private List<Cart_info> oldList;
    private List<Cart_info> newList;

    Context context;

    public CartDiffUtil(Context context, List<Cart_info> oldList, List<Cart_info> newList) {
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
        return oldList.get(oldItemPosition).getCartId().trim().matches(newList.get(newItemPosition).getCartId().trim());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).compare(newList.get(newItemPosition));
    }


    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {

        Cart_info newModel = newList.get(newItemPosition);
        Cart_info oldModel = oldList.get(oldItemPosition);

        Bundle diff = new Bundle();

        if (!newModel.getPro_price().trim().matches(oldModel.getPro_price().trim())) {
            diff.putString("price", newModel.getPro_price());
        }else if (newModel.getisOutOfQuantity()==oldModel.getisOutOfQuantity()){
            diff.putString("quantity",newModel.getQuantity());

        }

        if (diff.size() == 0) {
            return super.getChangePayload(oldItemPosition, newItemPosition);
        }
        return diff;

        //return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
