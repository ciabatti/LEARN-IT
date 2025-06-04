package DomainModel;

import java.util.ArrayList;

public class Company {

    private String idcode;
    private String name;
    private String email;
    private String address;
    private String cellphone ;
    private ArrayList<Employee> employees;

    public Company(String idcode, String email, String name, String address, String cellphone){
        this.idcode=idcode;
        this.email=email;
        this.name= name;
        this.address = address;
        this.cellphone=cellphone;
    }

    public String getIdcode() {
        return idcode;
    }

    public void setIdcode(String idcode) {
        this.idcode = idcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<Employee> getEmployees() {
        return employees;
    }

    public void setEmployee(ArrayList<Employee> employees) {
        this.employees = employees;
    }
}
