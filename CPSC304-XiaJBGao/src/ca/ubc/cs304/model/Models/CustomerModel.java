package ca.ubc.cs304.model.Models;

public class CustomerModel {
    private String cellphone;
    private String name;
    private String address;
    private String dlicense;

    public CustomerModel(String cellphone, String name, String address, String dlicense){
        this.cellphone = cellphone;
        this.name = name;
        this.address = address;
        this.dlicense = dlicense;
    }

    public String getAddress() {
        return address;
    }

    public String getCellphone() {
        return cellphone;
    }

    public String getDlicense() {
        return dlicense;
    }

    public String getName() {
        return name;
    }
}