package yaseerfarah22.com.pharmacy.Util;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import java.util.List;

import yaseerfarah22.com.pharmacy.Model.Order_info;
import yaseerfarah22.com.pharmacy.Model.Product_info;

/**
 * Created by DELL on 9/13/2019.
 */

public class OrderDiffUtil extends DiffUtil.Callback {

    private List<Order_info> oldList;
    private List<Order_info> newList;

    Context context;

    public OrderDiffUtil(Context context, List<Order_info> oldList, List<Order_info> newList) {
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
        return oldList.get(oldItemPosition).getOrder_id().trim().matches(newList.get(newItemPosition).getOrder_id().trim());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).compare(newList.get(newItemPosition));
    }


    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {

        Order_info newModel = newList.get(newItemPosition);
        Order_info oldModel = oldList.get(oldItemPosition);

        Bundle diff = new Bundle();

        if (!newModel.getOrder_status().trim().matches(oldModel.getOrder_status().trim())) {
            diff.putString("order_status", newModel.getOrder_status());
        }
        if (diff.size() == 0) {
            return super.getChangePayload(oldItemPosition, newItemPosition);
        }
        return diff;

       //
    }
}
