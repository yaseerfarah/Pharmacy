package yaseerfarah22.com.pharmacy.Model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by DELL on 4/16/2019.
 */

public class User_info implements Serializable {

    private String user_id,firstName,lastName,email,address,city,phoneNumber,image,purchases;


    public User_info() {

    }

    public User_info(String user_id, String firstName, String lastName, String email, String image, String address, String city, String phoneNumber, String purchases) {
        this.user_id = user_id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.image=image;
        this.email = email;
        this.address = address;
        this.city = city;
        this.phoneNumber = phoneNumber;
        this.purchases=purchases;

    }


    public String getUser_id() {
        return user_id;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


}
