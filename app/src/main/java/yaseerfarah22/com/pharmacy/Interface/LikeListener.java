package yaseerfarah22.com.pharmacy.Interface;

import java.util.List;

import yaseerfarah22.com.pharmacy.Model.Cart_info;
import yaseerfarah22.com.pharmacy.Model.Like_info;

/**
 * Created by DELL on 9/11/2019.
 */

public interface LikeListener {

    void onSuccess(Like_info likeInfo);
    void onFailure(String e);


}
