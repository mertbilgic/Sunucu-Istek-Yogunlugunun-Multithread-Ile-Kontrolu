
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SubServer extends Server {

    //Main server için bir obje oluşturuyoruz.
    Server server = Main.server.get(MainServerIndex);

    public SubServer(String serverName, int capacity, int requestTime, int responseTime, int serverIP, boolean sleep, int maxRequestCount) {
        super(serverName, capacity, requestTime, responseTime, serverIP, sleep, maxRequestCount);
    }

    //Balance metodu tarafından yönllendirilen requestler ilgili sunucuya burda ekleniyor.
    public ArrayList<RequestData> request(ArrayList<RequestData> request, int serverIndex, int requestCount) {
        synchronized (lock) {
            //System.out.println(getServerName());
            ArrayList<RequestData> temp = null;
            requestCount = requestCountControl(requestData.size(), requestCount);
            try {
                Thread.sleep(getRequestTime());

                temp = new ArrayList<RequestData>(request.subList(startIndex, requestCount));

                requestData.addAll(temp);
                /*for (int i = 0; i < requestCount; i++) {
                    requestData.add(new RequestData("2", "2"));

                }*/
            } catch (InterruptedException ex) {
                Logger.getLogger(SubServer.class.getName()).log(Level.SEVERE, null, ex);
            }
            return temp;
        }
    }

    public void response() {

        while (true) {
            try {
                //Bölme işlemi sırasında response işlemini sleep yapıyoruz
                while (isSleep() == true) {
                    Thread.sleep(100);
                }

                Thread.sleep(getResponseTime());

                int requestCount = random.nextInt(getMaxRequestCount());
                if (requestCount > requestData.size()) {

                    Thread.sleep(100);
                    requestCount = (requestData.size());
                }

                synchronized (lock) {
                    ArrayList<RequestData> temp;
                    temp = new ArrayList<RequestData>(requestData.subList(startIndex, requestCount));
                    requestData.removeAll(temp);
                    server.responseData.addAll(temp);
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(SubServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
