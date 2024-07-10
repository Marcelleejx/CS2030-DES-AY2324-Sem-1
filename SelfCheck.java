import java.util.function.Supplier;

class SelfCheck extends Server {

    public SelfCheck(int serverId, double serverTime, boolean isIdle, 
            ImList<Customer> waitingCustomers, int qmax, Supplier<Double> restTimes) {
        super(serverId, serverTime, isIdle, waitingCustomers, qmax, restTimes);
    }
    
    @Override
    public double getRestTime() {
        return 0.0;
    }

    @Override
    public Server updateQueue(ImList<Customer> queue) {
        return new SelfCheck(super.getServerId(), super.getServerTime(), super.getIsIdle(), queue, 
                super.getQmax(), super.getRestTimes());
    }

    @Override
    public Server updateIdle(boolean status) {
        return new SelfCheck(super.getServerId(), super.getServerTime(), status, 
                super.getWaitingCustomers(), super.getQmax(), super.getRestTimes());
    }

    @Override
    public Server updateTime(double time) {
        return new SelfCheck(super.getServerId(), time, super.getIsIdle(), 
                super.getWaitingCustomers(), super.getQmax(), super.getRestTimes());
    }
    
    @Override
    public boolean isSelfCheck() {
        return true;
    }

    @Override
    public String toString() {
        return String.format("self-check %d", this.getServerId());
    }
}

