/**
 * Created by amir on 05/01/2018.
 */
public class RtrSessionData {
    public boolean connected;
    public Pdu lastPduSent;
    public String lastPduReceived;


    //
    // Getters
    //
    public boolean isConnected() {
        return connected;
    }

    public String getLastPduReceived() {
        return lastPduReceived;
    }

    public Pdu getLastPduSent() {
        return lastPduSent;
    }

    //
    // Setters
    //
    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public void setLastPduSent(Pdu pdu) {
        this.lastPduSent = pdu;
    }

    public void setLastPduReceived(String lastPduReceived) {
        this.lastPduReceived = lastPduReceived;
    }
}
