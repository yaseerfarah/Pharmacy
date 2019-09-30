package yaseerfarah22.com.pharmacy.Model;

import java.io.Serializable;

/**
 * Created by DELL on 4/18/2019.
 */

public class Like_info implements Serializable {


    String userId;

    String likeId;
    Product_info product_info;

    public Like_info(){

    }

    public Like_info(String likeId, String userId, Product_info product_info) {
        this.likeId=likeId;
        this.userId = userId;
        this.product_info = product_info;
    }


    public String getLikeId() {
        return likeId;
    }

    public void setLikeId(String likeId) {
        this.likeId = likeId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Product_info getProduct_info() {
        return product_info;
    }

    public void setProduct_info(Product_info product_info) {
        this.product_info = product_info;
    }






}
