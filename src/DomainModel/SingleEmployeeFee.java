package DomainModel;

public class SingleEmployeeFee implements FeeStrategy{
    @Override
    public double getFee(Subscription subscription) {
        return MEETING_FEE; // No discount for single employee
    }
}
