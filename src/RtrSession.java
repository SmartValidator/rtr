
/**
 * Created by amir on 03/01/2018.
 */
public class RtrSession {
    // Session ID to identify the instance of the cache
    public String session_id;

    // The pdu to send to the router
    public String pdu;

    public Router router;

    public RtrSession(String session_id) {
        this.session_id = session_id;
    }

    public void notifyClients() {

    }

    public void cacheResponsePDU() {

    }

    public void cacheResetPDU() {

    }

    public void ip4_prefix() {

    }

    public void ip6_prefix() {

    }

    public void end_of_data() {

    }

    public void cache_reset() {

    }

    public void error_report() {

    }



}
