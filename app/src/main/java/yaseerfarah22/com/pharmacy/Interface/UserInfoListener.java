package yaseerfarah22.com.pharmacy.Interface;

import yaseerfarah22.com.pharmacy.Model.User_info;

/**
 * Created by DELL on 7/31/2019.
 */

public interface UserInfoListener {

    void onSuccess(User_info userInfo);
    void onFailure(String e);

}
