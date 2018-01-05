import java.util.ArrayList;
import java.util.List;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

/**
 * Created by amir on 05/01/2018.
 */
public class RtrSessionHandler {
    // The cache serial number
    public int serial_number;
    // Session ID to identify the instance of the cache
    public int session_id;
    // List of valid ROAs
    // From here we get the prefixes
    public List<Rtr.Roa> roa_list = new ArrayList<>();

    public RtrSessionData sessionData;

    public RtrSessionHandler(RtrSessionData sessionData) {
        this.sessionData = sessionData;
    }

    public void connect() {
        this.sessionData.setConnected(TRUE);
    }
    public void disconnect() {
        this.sessionData.setConnected(FALSE);
    }
    public void serialNotify(Pdu pdu) {
        this.sessionData.setLastPduSent(pdu);
    }

    public void processRequest(Pdu pdu) {
        Pdu result = processRequestPdu(pdu);
        this.sessionData.setLastPduSent(result);
    }

    private Pdu processRequestPdu(Pdu pdu) {
        int type = pdu.getPdu_type();

        switch (type) {
            case 2: // Reset Query
                this.sessionData.setLastPduReceived("ResetQuery");
                processResetQuery(pdu);
                break;
            case 1: // Serial Query
                this.sessionData.setLastPduReceived("SerialQuery");
                processSerialQuery(pdu, pdu.getSession_id(), pdu.getSerial_number());
                break;
        }

        return pdu;
    }

    private List<Pdu> processResetQuery(Pdu pdu) {
        List<Pdu> responsePdus = new ArrayList<Pdu>();
        int currSessionId = this.session_id;
        responsePdus.add(pdu.cacheResponsePDU(currSessionId));

        for (Rtr.Roa roa: roa_list) {
            String prefix = roa.getPrefix();
            String asn = roa.getAsn();
            String prefixLength = roa.getLength();
            String maxLength = roa.getMaxLength();
            Pdu.prefixType prefix_type = getPrefixType(prefix);

            if (prefix_type == Pdu.prefixType.IPV4)
            {
                responsePdus.add(pdu.ip4Prefix());
            }
            else if(prefix_type == Pdu.prefixType.IPV6) {
                responsePdus.add(pdu.ip6Prefix());
            }
        }

        responsePdus.add(pdu.endOfData(currSessionId));

        return responsePdus;
    }

    private List<Pdu> processSerialQuery(Pdu pdu, int session_id, int serial_number) {
        List<Pdu> responsePdus = new ArrayList<Pdu>();

        if ((session_id == getSession_id()) && (serial_number == getSerial_number()))
        {
            responsePdus.add(pdu.cacheResponsePDU(session_id));
            responsePdus.add(pdu.endOfData(session_id));
        }
        else
        {
            responsePdus.add(pdu.cacheReset());
        }

        return responsePdus;
    }

    private Pdu.prefixType getPrefixType(String prefix) {
        // TODO:
        // return prefix type from prefix string
        return Pdu.prefixType.IPV4;
    }

    //
    // Getters
    //
    public int getSerial_number() {
        return this.serial_number;
    }

    public int getSession_id() {
        return this.session_id;
    }
}
