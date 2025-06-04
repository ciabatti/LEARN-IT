package DomainModel;

public class MultipleEmployeesFee implements FeeStrategy{
    final double DISCOUNT = 0.20; // 20% discount for multiple employees
    @Override
    public double getFee(Subscription subscription) {
        return MEETING_FEE - (MEETING_FEE * DISCOUNT);
    }
}
