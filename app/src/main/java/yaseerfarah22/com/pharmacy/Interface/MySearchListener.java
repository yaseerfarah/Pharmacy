package yaseerfarah22.com.pharmacy.Interface;

import java.util.List;

import yaseerfarah22.com.pharmacy.Model.Cart_info;
import yaseerfarah22.com.pharmacy.Model.Product_info;

/**
 * Created by DELL on 7/26/2019.
 */

public interface MySearchListener {
    void onSuccess(List<Product_info> productInfoList);
    void onFailure(String e);

}
