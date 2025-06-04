package DomainModel;

public class Slide extends Material {
    public Slide(byte[] file, String filename, String date, String description, Trainer uploader) {
        super(file, filename, date, description, uploader);
    }
}
