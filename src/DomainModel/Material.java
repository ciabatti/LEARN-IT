package DomainModel;

public abstract class Material {

    private byte[] file;
    private String filename;
    private String date;
    private String description;
    private Trainer uploader;

    public Material(byte[] file, String filename, String date, String description, Trainer uploader) {
        this.file = file;
        this.filename = filename;
        this.date = date;
        this.description = description;
        this.uploader = uploader;
    }

    public byte[] getFile() {
        return file;
    }

    public String getFilename() {
        return filename;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public Trainer getUploader() {
        return uploader;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUploader(Trainer uploader) {
        this.uploader = uploader;
    }


}
