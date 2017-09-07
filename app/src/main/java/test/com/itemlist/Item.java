package test.com.itemlist;

import io.realm.RealmObject;

/**
 * Created by Arsen on 01.08.2017.
 */

public class Item extends RealmObject {
    private String url;
    private String name;

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
