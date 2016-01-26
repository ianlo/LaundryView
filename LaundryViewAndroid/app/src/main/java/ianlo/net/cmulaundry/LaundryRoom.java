package ianlo.net.cmulaundry;

/**
 * Created by ianlo on 2016-01-07.
 */
public class LaundryRoom {
    private String url;
    private String name;
    public LaundryRoom(String name, String url) {
        this.setUrl(url);
        this.setName(name);
    }

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
