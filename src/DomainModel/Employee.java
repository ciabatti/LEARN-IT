package DomainModel;

public class Employee {

    private String idcode;
    private String name;
    private String surname;
    private int age;
    private String role;
    private Subscription subscription;

    public Employee(String idcode, String name, String surname, int age, String role){
        this.idcode=idcode;
        this.name=name;
        this.surname=surname;
        this.age=age;
        this.role = role;
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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }

    public Subscription getSubscription() {
        return subscription;
    }
}

