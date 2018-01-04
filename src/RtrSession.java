import java.util.ArrayList;
import java.util.List;

/**
 * Created by amir on 03/01/2018.
 */
public class RtrSession {
    // Session ID to identify the instance of the cache
    public String session_id;

    // The pdu to send to the router
    public Pdu pdu;

    // The data records this session has
    List<Pdu> pdu_list = new ArrayList<Pdu>();

    // List of valid ROAs
    public List<Rtr.Roa> roa_list = new ArrayList<>();

    public Router router;

    public RtrSession(String session_id, Pdu pdu) {
        this.pdu = pdu;
        this.session_id = session_id;
    }

    public void serialNotify() {
        pdu.serialNotify(this.session_id);
    }

    public void serialQuery() {
        pdu.serialQuery(this.session_id);
    }

    public void resetQuery() {
        pdu.resetQuery();
    }

    public void cacheResponsePDU(int sn) {
        pdu.cacheResponsePDU();
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
