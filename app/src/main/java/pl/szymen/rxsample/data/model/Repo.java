package pl.szymen.rxsample.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by szymen on 2015-11-25.
 */
public class Repo {

    @SerializedName("name") String mName;

    public String getName() {
        return mName;
    }

    public String toString() {
        return getName();
    }
}
