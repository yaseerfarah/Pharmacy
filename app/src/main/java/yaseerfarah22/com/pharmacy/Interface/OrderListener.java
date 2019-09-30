package yaseerfarah22.com.pharmacy.Interface;

import java.util.List;

import yaseerfarah22.com.pharmacy.Model.Cart_info;


/**
 * Created by DELL on 7/25/2019.
 */

public interface OrderListener {

    void onSuccess(List<Cart_info> cartInfoList);
    void onFailure(String e);

}
