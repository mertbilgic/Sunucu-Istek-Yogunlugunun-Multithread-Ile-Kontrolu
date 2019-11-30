//Bu sınıfı listelerimize verilerek eklerken gerekli objeleri oluşturmak için kullanıyoruz.

public class RequestData {

    private String clientIP;
    private String request;
    private String response;

    public RequestData(String clientIP, String request, String response) {
        this.clientIP = clientIP;
        this.request = request;
        this.response = response;
    }

    public RequestData(String clientIP, String request) {
        this.clientIP = clientIP;
        this.request = request;
    }

    public String getClientIP() {
        return clientIP;
    }

    public void setClientIP(String clientIP) {
        this.clientIP = clientIP;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

}
