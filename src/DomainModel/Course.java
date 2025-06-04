package DomainModel;

public class Course {
    private String date;
    private String time;
    private String description;
    private FocusCourse type;

    public Course(String date, String time, String description, FocusCourse type) {
        this.date = date;
        this.time = time;
        this.description = description;
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getDescription() {
        return description;
    }

    public FocusCourse getType() {
        return type;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setType(FocusCourse type) {
        this.type = type;
    }






}
