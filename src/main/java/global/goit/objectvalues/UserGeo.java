package global.goit.objectvalues;

public class UserGeo {
    private String lat;
    private String lng;

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    @Override
    public String toString() {
        return "UserGeo{" +
                "lat='" + lat + '\'' +
                ", lng='" + lng + '\'' +
                '}';
    }
}
