import java.util.ArrayList;
import java.util.List;

/**
 * Created by amir on 03/01/2018.
 TODO:
 - should I have a data structure that saves all session?
 */
public class RtrSession {
    // The cache serial number
    public int serial_number;
    // Session ID to identify the instance of the cache
    public int session_id;
    // List of valid ROAs
    // From here we get the prefixes
    public List<Rtr.Roa> roa_list = new ArrayList<>();
    // The pdu to send to the router
    public Pdu pdu;

    public RtrSession(int session_id, Pdu pdu) {
        this.pdu = pdu;
        this.session_id = session_id;
    }

    public RtrSession(int serian_num, List<Rtr.Roa> roas, String session_id) {
        this.pdu = new Pdu(
                /* serial_number */ this.serial_number
        );
    }

    public Pdu serialNotify() {
        Pdu pdu = new Pdu(this.getSerial_number());
        pdu.serialNotify(this.getSession_id());
        pdu.encode(Pdu.pdu_type_enum.SERIAL_NOTIFY, null);

        return pdu;
    }

    //
    // Getters
    //
    public int getSerial_number() {
        return serial_number;
    }


    public int getSession_id() {
        return session_id;
    }

    //
    // Setters
    //
    public void setSession_id(int session_id) {
        this.session_id = session_id;
    }


    public void setSerial_number(int serial_number) {
        this.serial_number = serial_number;
    }



    public void serialQuery() {
        pdu.serialQuery(this.session_id);

    }

    public void resetQuery() {
        pdu.resetQuery();
    }

    public void cacheResponsePDU(int sn) {
        pdu.cacheResponsePDU(this.getSession_id());
    }

    public void ip4Prefix() {
        pdu.ip4Prefix();
    }

    public void ip6Prefix() {
        pdu.ip6Prefix();
    }

    public void endOfData() {
        pdu.endOfData(this.session_id);
    }

    public void cacheReset() {
        pdu.cacheReset();
    }

    public void errorReport() {
        // TODO
    }



}
