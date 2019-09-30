package yaseerfarah22.com.pharmacy.Model;

import java.util.List;

/**
 * Created by DELL on 7/26/2019.
 */

public class Suggestion_info {


    private List<String> names;

    public Suggestion_info(){}


    public Suggestion_info(List<String> names) {
        this.names = names;
    }


    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }
}
