
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SubServer extends Server {

    //Main server için bir obje oluşturuyoruz.
    //Server server = Main.server.get(MainServerIndex);

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

                //System.out.println(requestCount + "      " + mainServer.requestData.size());
                //Bu işlemleri wait metodu ile yönetmem gerekiyor.
                /*if (getRequestData().size() + requestCount < getCapacity()) {
                    
                    //Main.server.get(serverIndex);
                    temp = new ArrayList<RequestData>(request.subList(startIndex, requestCount));

                    requestData.addAll(temp);

                } else {
                    requestCount = getCapacity() - getRequestData().size();
                    temp = new ArrayList<RequestData>(request.subList(startIndex, requestCount));

                    requestData.addAll(temp);
                }*/
                //System.out.println("**************************************************************requestCount"+requestCount);
                for (int i = 0; i < requestCount; i++) {
                    requestData.add(new RequestData("2", "2"));

                }
                // temp = new ArrayList<RequestData>(request.subList(startIndex, requestCount));

                //requestData.addAll(temp);
            } catch (InterruptedException ex) {
                Logger.getLogger(SubServer.class.getName()).log(Level.SEVERE, null, ex);
            }
            return temp;
        }
    }
}
