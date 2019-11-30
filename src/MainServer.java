
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainServer extends Server {

    private int controlTime;
    private final int minCount = 0;
    static int Balence = 0, Reponse = 0;

    public MainServer(String serverName, int capacity, int requestTime, int responseTime, int serverIP,
            boolean sleep, int controlTime, int maxRequestCount) {
        super(serverName, capacity, requestTime, responseTime, serverIP, sleep, maxRequestCount);
        this.controlTime = controlTime;
    }

    public int getControlTime() {
        return controlTime;
    }

    public void setControlTime(int controlTime) {
        this.controlTime = controlTime;
    }

    //Listemize fake verilerimizden yararlanarak request ekliyoruz.
    public void request() {

        int value, requestCount;
        String ip;
        while (true) {
            try {
                Thread.sleep(getRequestTime());

                requestCount = random.nextInt(getMaxRequestCount());
                if (getRequestData().size() + requestCount <= getCapacity()) {
                    add(requestCount);
                } else {
                    requestCount = getCapacity() - getRequestData().size();
                    add(requestCount);

                }
            } catch (InterruptedException ex) {
                Logger.getLogger(MainServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public void add(int requestCount) {
        int value;
        String ip;
        for (int i = 0; i < requestCount; i++) {
            value = random.nextInt(12);
            ip = String.valueOf(value);

            requestData.add(new RequestData(clientIP + ip, requestName[value]));

        }
    }
}
