import java.util.Iterator;
import java.util.function.Supplier;

class Simulator {
    private final int numOfServers;
    private final int numOfSelfChecks;
    private final int qmax;
    private final ImList<Double> arrivalTimes;
    private final Supplier<Double> serviceTime;
    private final Supplier<Double> restTimes;

    public Simulator(int numOfServers, int numOfSelfChecks, int qmax, 
            ImList<Double> arrivalTimes, Supplier<Double> serviceTime, Supplier<Double> restTimes) {
        this.numOfServers = numOfServers;
        this.numOfSelfChecks = numOfSelfChecks;
        this.qmax = qmax;
        this.arrivalTimes = arrivalTimes;
        this.serviceTime = serviceTime;
        this.restTimes = restTimes;
    }

    public String simulate() {
        String output = "";
        int numServed = 0;
        int numLeft = 0;
        double waitingTime = 0.000;
        double avgWaitingTime = 0.00;
        ImList<Customer> customers = new ImList<Customer>();
        ImList<Server> servers = new ImList<Server>();
        PQ<Event> pq = new PQ<Event>(new EventComparator());

        for (int i = 1; i <= arrivalTimes.size(); i++) {
            Customer nowCustomer = new Customer(i, arrivalTimes.get(i - 1),
                    this.serviceTime);
            customers = customers.add(nowCustomer);
            Event arrive = new ArriveEvent(nowCustomer.getArrivalTime(), nowCustomer);
            pq = pq.add(arrive);
        }

        for (int i = 1; i <= numOfServers; i++) {
            servers = servers.add(new Server(i, 0.0, true, new ImList<Customer>(), 
                        this.qmax, this.restTimes));
        }

        for (int i = numOfServers + 1; i <= numOfSelfChecks + numOfServers; i++) {
            servers = servers.add(new SelfCheck(i, 0.0, true, new ImList<Customer>(), 
                        this.qmax, this.restTimes));
        }

        while (!pq.isEmpty()) {
            /*
            for (Server s : servers) {
                System.out.println(String.format("%s; server %s;  idle: %s; queuesize: %s",
                        s.getServerTime(), s.toString(), 
                            s.getIsIdle(), s.getWaitingCustomers().size()));
            }
            */

            Pair<Event, PQ<Event>> pair = pq.poll();
            Event currentEvent = pair.first();
            pq = pair.second();

            output = output + currentEvent.toString();
            numServed = numServed + currentEvent.served();
            waitingTime = waitingTime + currentEvent.waitingTime(servers);
            
            Pair<Event, ImList<Server>> nextPair = currentEvent.getNextEvent(servers);
            Event nextEvent = nextPair.first();
            servers = nextPair.second();

            if (!nextEvent.equals(currentEvent)) {
                pq = pq.add(nextEvent);
            }

        }

        numLeft = customers.size() - numServed;
        
        if (numServed == 0) {
            avgWaitingTime = 0;
        } else {
            avgWaitingTime = (waitingTime / numServed);
        }

        output = output + String.format("[%.3f %d %d]", avgWaitingTime, numServed, numLeft);
        return output;
    }
}
