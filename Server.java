import java.util.function.Supplier;

class Server {
    private final int serverId;
    private final double serverTime;
    private final boolean isIdle;
    private final ImList<Customer> waitingCustomers;
    private final int qmax;
    private final Supplier<Double> restTimes;

    public Server(int serverId, double serverTime, boolean isIdle, 
            ImList<Customer> waitingCustomers, int qmax, Supplier<Double> restTimes) {
        this.serverId = serverId;
        this.serverTime = serverTime;
        this.isIdle = isIdle;
        this.waitingCustomers = waitingCustomers;
        this.qmax = qmax;
        this.restTimes = restTimes;
    }

    public int getServerId() {
        return this.serverId;
    }

    public double getServerTime() {
        return this.serverTime;
    }

    public boolean getIsIdle() {
        return this.isIdle;
    }

    public int getQmax() {
        return this.qmax;
    }

    public Supplier<Double> getRestTimes() {
        return this.restTimes;
    }

    public ImList<Customer> getWaitingCustomers() {
        return this.waitingCustomers;
    }

    public Customer getFirstWaitingCustomer() {
        return this.waitingCustomers.get(0);
    }

    public boolean isAvailable(Customer customer) {
        return this.serverTime <= customer.getArrivalTime();
    }
    
    public boolean canQueue() {
        if (this.waitingCustomers.size() < qmax) {
            return true;
        }
        return false;
    }

    public Server updateQueue(ImList<Customer> queue) {
        return new Server(this.serverId, this.serverTime, this.isIdle, queue, 
                this.qmax, this.restTimes);
    }

    public Server updateIdle(boolean status) {
        return new Server(this.serverId, this.serverTime, status, this.waitingCustomers, 
                this.qmax, this.restTimes);
    }

    public Server updateTime(double time) {
        return new Server(this.serverId, time, this.isIdle, this.waitingCustomers, 
                this.qmax, this.restTimes);
    }

    public double getRestTime() {
        return this.restTimes.get();
    }

    public boolean isSelfCheck() {
        return false;
    }
    
    public String toString() {
        return String.format("%d", this.serverId);
    }
}
