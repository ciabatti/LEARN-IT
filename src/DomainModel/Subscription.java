package DomainModel;

public class Subscription {
    private int meetings;
    private Employee employee;
    private FeeStrategy feeStrategy;
    private boolean isPaid;

    public Subscription(int meetings, Employee employee, FeeStrategy feeStrategy, boolean isPaid) {
        this.meetings = meetings;
        this.employee = employee;
        this.feeStrategy = feeStrategy;
        this.isPaid = isPaid;
    }

    public double getFee(){
        return feeStrategy.getFee(this);
    }

    public int getMeetings() {
        return meetings;
    }

    public void setMeetings(int meetings) {
        this.meetings = meetings;
    }

    public Employee getEmployee() {
        return employee;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    public FeeStrategy getFeeStrategy() {
        return feeStrategy;
    }

    public void setFeeStrategy(FeeStrategy feeStrategy) {
        this.feeStrategy = feeStrategy;
    }
}
