package DomainModel;

public interface FeeStrategy {
    int MEETING_FEE = 100;
    double getFee(Subscription subscription);
}
