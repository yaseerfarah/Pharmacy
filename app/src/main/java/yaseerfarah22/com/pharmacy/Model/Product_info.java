package yaseerfarah22.com.pharmacy.Model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by DELL on 2/27/2019.
 */

public class Product_info implements Serializable {

    String id,name,category,sub_category;
    List<String> image_url;
    float price,ex_price;
    String product_date;



    String offers;
    int  stock,purchases;
    boolean is_offer;
    String description;



    public Product_info(){}


    public Product_info(String id, String name, String category, String sub_category, List<String> images_url, float price, float ex_price, String product_date, String offers, int  stock, int purchases, boolean is_offer, String description) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.sub_category = sub_category;
        this.image_url = images_url;
        this.price = price;
        this.ex_price = ex_price;
        this.product_date = product_date;
        this.offers = offers;
        this.stock = stock;
        this.purchases = purchases;
        this.is_offer = is_offer;
        this.description = description;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSub_category() {
        return sub_category;
    }

    public void setSub_category(String sub_category) {
        this.sub_category = sub_category;
    }

    public List<String> getImages_url() {
        return image_url;
    }

    public void setImages_url(List<String> images_url) {
        this.image_url = images_url;
    }

    public List<String> getImage_url() {
        return image_url;
    }

    public void setImage_url(List<String> image_url) {
        this.image_url = image_url;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getEx_price() {
        return ex_price;
    }

    public void setEx_price(float ex_price) {
        this.ex_price = ex_price;
    }

    public String getProduct_date() {
        return product_date;
    }

    public void setProduct_date(String product_date) {
        this.product_date = product_date;
    }

    public String getOffers() {
        return offers;
    }

    public void setOffers(String offers) {
        this.offers = offers;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getPurchases() {
        return purchases;
    }

    public void setPurchases(int purchases) {
        this.purchases = purchases;
    }

    public boolean isIs_offer() {
        return is_offer;
    }

    public void setIs_offer(boolean is_offer) {
        this.is_offer = is_offer;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



    public boolean compare(Product_info productInfo){

        if (productInfo.getName().trim().matches(getName().trim())&&productInfo.getStock()==getStock()&&productInfo.getEx_price()==getEx_price()&&productInfo.getPrice()==getPrice()&&productInfo.getOffers().trim().matches(getOffers().trim())){

            return true;
        }
        return false;

    }




}
