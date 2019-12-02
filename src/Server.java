
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {

    private String serverName;
    private int capacity;
    private int requestTime;
    private int responseTime;
    private int maxRequestCount;
    private int serverIP;
    private boolean sleep;
    private int totalRequest;
    final int MainServerIndex = 0;
    final int startIndex = 0;

    //Kullanıcı isteklerini tutuğumuz liste
    ArrayList<RequestData> requestData = new ArrayList<RequestData>();
    //Kullanıcıya dönüceğimiz değerleri tuttuğumuz liste
    ArrayList<RequestData> responseData = new ArrayList<RequestData>();

    Random random = new Random();

    Object lock = new Object();

    public Object lock2 = new Object();
    //Random request oluştururken gerçekcilik katsın diye oluşturduğum fake veriler
    final String[] requestName = {"AnaSayfa", "Spor", "Sondakika", "TvProgramları", "Video", "Foto Haber", "Yazarlar", "Canlı Yayın",
        "Youtube", "Facebook", "Twitter", "İnstagram", "Search"};
    final String response = "localhost:8888";
    final String clientIP = "192.168.1.";

    public Server(String serverName, int capacity, int requestTime, int responseTime, int serverIP, boolean sleep, int maxRequestCount, int totalRequest) {
        this.serverName = serverName;
        this.capacity = capacity;
        this.requestTime = requestTime;
        this.responseTime = responseTime;
        this.serverIP = serverIP;
        this.sleep = sleep;
        this.maxRequestCount = maxRequestCount;
        this.totalRequest = totalRequest;
    }

    public int getTotalRequest() {
        return totalRequest;
    }

    public void setTotalRequest(int totalRequest) {
        this.totalRequest = totalRequest;
    }

    public int getMaxRequestCount() {
        return maxRequestCount;
    }

    public void setMaxRequestCount(int maxRequestCount) {
        this.maxRequestCount = maxRequestCount;
    }

    public ArrayList<RequestData> getRequestData() {
        return requestData;
    }

    public void setRequestData(ArrayList<RequestData> requestData) {
        this.requestData = requestData;
    }

    public boolean isSleep() {
        return sleep;
    }

    public void setSleep(boolean sleep) {
        this.sleep = sleep;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(int requestTime) {
        this.requestTime = requestTime;
    }

    public int getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(int responseTime) {
        this.responseTime = responseTime;
    }

    public int getServerIP() {
        return serverIP;
    }

    public void setServerIP(int serverIP) {
        this.serverIP = serverIP;
    }

    //Burada bu fonskiyonları oluşturmadaki amacımız listenimiz Server sınıfından oluşturduk
    //Eğer Server sınıfının içinde bu veriler olmazsa aşağıdaki fonskiyonları kullanmayız.
    //Polymorphism den yararlanıyoruz
    public void request() {
        System.out.println("Parent request" + getServerName());
    }

    public void serverBalance() {
        System.out.println("Parent balance" + getServerName());
    }

    public ArrayList<RequestData> request(ArrayList<RequestData> requestData, int requestCount, int serverIndex) {
        System.out.println("Parent request2" + getServerName());
        return requestData;
    }

    public void balance() {
        System.out.println("SubServerin bölme yetkisi yok...");
    }

    //Response değerlerini olutşur
    public void response() {
        System.out.println("parent response");
    }

    public int requestCountControl(int size, int requestCount) {
        int result = 0;
        //System.out.println("size:"+size+" request"+requestCount);
        // System.out.println("////////////////////////////////////"+size+"*********"+getServerName());
        if (size == getCapacity()) {
            return result;
        } else if (size + requestCount > getCapacity()) {
            return (getCapacity() - size);
        } else {
            return requestCount;
        }

    }

}
