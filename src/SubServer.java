
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SubServer extends Server {

    public SubServer(String serverName, int capacity, int requestTime, int responseTime, int serverIP, boolean sleep, int maxRequestCount) {
        super(serverName, capacity, requestTime, responseTime, serverIP, sleep, maxRequestCount);
    }

    //
    public ArrayList<RequestData> request(ArrayList<RequestData> request, int serverIndex, int requestCount) {
        synchronized (lock) {
            //System.out.println(getServerName());
            ArrayList<RequestData> temp = null;
            requestCount = requestCountControl(requestData.size(), requestCount);
            try {
                Thread.sleep(getRequestTime());

                for (int i = 0; i < requestCount; i++) {
                    requestData.add(new RequestData("2", "2"));

                }

            } catch (InterruptedException ex) {
                Logger.getLogger(SubServer.class.getName()).log(Level.SEVERE, null, ex);
            }
            return temp;
        }
    }

}
