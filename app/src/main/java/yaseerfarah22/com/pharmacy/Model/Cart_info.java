package yaseerfarah22.com.pharmacy.Model;



/**
 * Created by DELL on 2/27/2019.
 */

public class Cart_info {




    String cartId;
    String userId,product_id,quantity,pro_name,pro_category,pro_sub_category,pro_price,pro_imageurl;
    boolean isOutOfQuantity=false;

    public Cart_info() {
    }


    public Cart_info(String cartId, String userId, String product_id, String quantity, String pro_name, String pro_category, String pro_sub_category, String pro_price, String pro_imageurl) {
        this.cartId = cartId;
        this.userId = userId;
        this.product_id = product_id;
        this.quantity = quantity;
        this.pro_name = pro_name;
        this.pro_category = pro_category;
        this.pro_sub_category = pro_sub_category;
        this.pro_price = pro_price;
        this.pro_imageurl = pro_imageurl;
    }


    public boolean getisOutOfQuantity() {
        return isOutOfQuantity;
    }

    public void setOutOfQuantity(boolean outOfQuantity) {
        isOutOfQuantity = outOfQuantity;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPro_name() {
        return pro_name;
    }

    public void setPro_name(String pro_name) {
        this.pro_name = pro_name;
    }

    public String getPro_category() {
        return pro_category;
    }

    public void setPro_category(String pro_category) {
        this.pro_category = pro_category;
    }

    public String getPro_sub_category() {
        return pro_sub_category;
    }

    public void setPro_sub_category(String pro_sub_category) {
        this.pro_sub_category = pro_sub_category;
    }

    public String getPro_price() {
        return pro_price;
    }

    public void setPro_price(String pro_price) {
        this.pro_price = pro_price;
    }

    public String getPro_imageurl() {
        return pro_imageurl;
    }

    public void setPro_imageurl(String pro_imageurl) {
        this.pro_imageurl = pro_imageurl;
    }



    public boolean compare(Cart_info cartInfo){

        if (cartInfo.getProduct_id().trim().matches(getProduct_id().trim())&&cartInfo.getisOutOfQuantity()==getisOutOfQuantity()&&cartInfo.getPro_price().trim().matches(getPro_price().trim())){

            return true;
        }
        return false;

    }



}
