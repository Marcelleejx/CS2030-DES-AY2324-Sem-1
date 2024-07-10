public class LeaveEvent extends Event {
    LeaveEvent(double time, Customer customer) {
        super(time, customer);
    }

    @Override
    public String toString() {
        return super.toString() + " leaves\n";
    }
}
