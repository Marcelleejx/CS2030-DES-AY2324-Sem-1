class ArriveEvent extends Event {

    public ArriveEvent(double time, Customer customer) {
        super(time, customer);
    }

    public Pair<Event, ImList<Server>> getNextEvent(ImList<Server> servers) {

        int counter = servers.size() - 1;
        for (int i = 0; i < servers.size(); i++) {
            Server s = servers.get(i);
            if (s.isSelfCheck()) {
                counter = i;
                break;
            }
        }

        /*
        System.out.println(String.format("%s customer, counter is %s", 
                this.getCustomer().getCustomerId(), counter));
        for (int i = 0; i <= 2; i++) {
            Server ps = servers.get(i);
            System.out.println(String.format("%s customer %s server %s idle: %s queuesize: %s",
                    this.getCustomer().getCustomerId(), ps.getServerId(), ps.getServerTime(), 
                                ps.getIsIdle(), ps.getWaitingCustomers().size()));
        }
        */

        for (int i = 0; i < servers.size(); i++) {
            Server s = servers.get(i);
            if (s.isAvailable(this.getCustomer()) && s.getIsIdle()) {
                Server updateServer = s.updateIdle(false)
                    .updateTime(this.getCustomer().getArrivalTime());
                servers = servers.set(i, updateServer);
                return new Pair<Event, ImList<Server>>(
                        new ServeEvent(this.getTime(), getCustomer(), updateServer), servers);
            }
        }
        for (int i = 0; i <= counter; i++) {
            Server s = servers.get(i);
            if (s.canQueue()) {
                ImList<Customer> queue = s.getWaitingCustomers();
                queue = queue.add(this.getCustomer());
                Server updateServer = s.updateIdle(false).updateQueue(queue);
                servers = servers.set(i, updateServer);
                return new Pair<Event, ImList<Server>>(new WaitEvent(this.getTime(), 
                            getCustomer(), updateServer, true), servers);
            }
        }
        return new Pair<Event, ImList<Server>>(new LeaveEvent(this.getTime(), this.getCustomer()), 
                servers);
    }

    @Override
    public String toString() {
        return super.toString() + " arrives\n";
    }
}
