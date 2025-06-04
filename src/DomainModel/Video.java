package DomainModel;

public class Video extends Material {
    public Video(byte[] file, String filename, String date, String description, Trainer uploader) {
        super(file, filename, date, description, uploader);
    }
}
