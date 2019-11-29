package ca.ubc.cs304.model;

public class VehicleModel {
    private final Integer vid;
    private final String vlicense;
    private final String make;
    private final String model;
    private final Integer year;
    private final String color;
    public Integer odometer;
    public Integer status; //todo ??????????????????????????????????????????
    private final String vtname;
    public String location;
    public String city;

    public VehicleModel (Integer vid, String vlicense, String make, String model, Integer year, String color, Integer odometer,
                         Integer status, String vtname, String location, String city) {
        this.vid = vid;
        this.vlicense = vlicense;
        this.make = make;
        this.model = model;
        this.year = year;
        this.color = color;
        this.odometer = odometer;
        this.status = status;
        this.vtname = vtname;
        this.location = location;
        this.city = city;
    }

    public Integer getVid(){
        return this.vid;
    }
    public String getVlicense(){
        return this.vlicense;
    }
    public String getMake(){
        return this.make;
    }
    public String getModel(){
        return this.model;
    }
    public String getColor(){
        return this.color;
    }

    public Integer getOdometer() {
        return this.odometer;
    }
    public Integer getYear(){
        return this.year;
    }
    public Integer getStatus(){
        return this.status;
    }
    public String getVtname(){
        return this.vtname;
    }
    public String getLocation(){
        return this.location;
    }
    public String getCity(){
        return this.city;
    }
    public String VehicletoString (){
        String vids  = getVid().toString();
        String vlicenses = getVlicense().toString();
        String makes = getMake();
        String models = getModel();
        String years = getYear().toString();
        String colors = getColor();
        String odometers = getOdometer().toString();
        String statuss = getStatus().toString();
        String vtnames = getVtname();
        String locations = getLocation();
        String citys = getCity();
        String result = "VID: "+vids + "; VLicense: "+vlicenses + "; Brand: " + makes  +"; Models: "+models
                +"; Year:" +years + "; Color:" + colors+ "; Odometer:" + odometers+ "; Status: " +statuss +
                "; VType: "+ vtnames+"; Location:" + locations + "; City:" + citys;
        return result;

    }
}