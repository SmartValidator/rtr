/**
 * Created by amir on 03/01/2018.
 */
public class Pdu2 {

    public enum prefixType {
        IPV4,
        IPV6
    }

    public enum pdu_error_type {
        CORRUPT_DATA(0),
        INTERNAL_ERROR(1),
        NO_DATA_AVAIL(2),
        INVALID_REQUEST(3),
        UNSUPPORTED_PROTOCOL_VER(4),
        UNSUPPORTED_PDU_TYPE(5),
        WITHDRAWAL_OF_UNKNOWN_RECORD(6),
        DUPLICATE_ANNOUNCEMENT(7),
        UNEXPECTED_PROTOCOL_VERSION(8),
        PDU_TOO_BIG(32);

        pdu_error_type(int err_type) {
            this.err_type = err_type;
        }

        private final int err_type;

        public int getErr_type() {
            return err_type;
        }
    };

    enum pdu_type {
        SERIAL_NOTIFY(0),
        SERIAL_QUERY(1),
        RESET_QUERY(2),
        CACHE_RESPONSE(3),
        IPV4_PREFIX(4),
        RESERVED(5),
        IPV6_PREFIX(6),
        EOD(7),
        CACHE_RESET(8),
        ROUTER_KEY(9),
        ERROR(10);

        pdu_type(int type) {
            this.type = type;
        }

        private final int type;

        public int getType() {
            return type;
        }
    };

    public class pdu_header {
        int ver;
        pdu_type type;
        int reserved;
        int len;
    };

    public class pdu_cache_response {
        int ver;
        int type;
        int session_id;
        int len;
    };

    public class pdu_serial_notify {
        int ver;
        int type;
        int session_id;
        int len;
        int sn;
    };

    public class pdu_serial_query {
        int ver;
        int type;
        int session_id;
        int len;
        int sn;
    };

    public class pdu_ipv4 {
        int ver;
        int type;
        int reserved;
        int len;
        int flags;
        int prefix_len;
        int max_prefix_len;
        int zero;
        int prefix;
        int asn;
    };

    public class pdu_ipv6 {
        int ver;
        int type;
        int reserved;
        int len;
        int flags;
        int prefix_len;
        int max_prefix_len;
        int zero;
        int prefix;
        int asn;
    };

    public class pdu_error {
        int ver;
        int type;
        int error_code;
        int len;
        int len_enc_pdu;
        int rest;
    };

    public class pdu_router_key {
        int ver;
        int type;
        int flags;
        int zero;
        int len;
        int asn;
    };

    public class pdu_reset_query {
        int ver;
        int type;
        int flags;
        int len;
    };

    public class pdu_end_of_data {
        int ver;
        int type;
        int session_id;
        int len;
        int sn;
    };

    //  An eight-bit unsigned integer, currently 0, denoting the version of this protocol.
    public int protocol_version = 0;

    // An eight-bit unsigned integer, denoting the type of the PDU, e.g., IPv4 prefixType, etc.
    public prefixType pdu_type2;

    // The Serial Number of the RPKI Cache
    public int serial_number;

    // Session ID to identify the instance of the cache
    public String session_id;

    // A 32-bit unsigned integer that has as its value the count of the bytes in the entire PDU
    public int length;

    // Flags
    public int flags;

    // An 8-bit unsigned integer denoting the shortest prefix allowed for the prefix.
    public int prefix_length;

    // An 8-bit unsigned integer denoting the longest prefix allowed by the prefix.
    public int max_length;

    // The IPv4 or IPv6 prefix of the ROA.
    public String prefix;

    // ASN allowed to announce this prefix, a 32-bit unsigned integer.
    public int asn;

    public pdu_serial_notify pdu_serial_notify;
    public pdu_cache_response pdu_cache_response;
    public pdu_ipv4 pdu_ipv4;
    public pdu_ipv6 pdu_ipv6;
    public pdu_end_of_data pdu_end_of_data;
    public pdu_header pdu_header;
    public pdu_router_key pdu_router_key;
    public pdu_serial_query pdu_serial_query;
    public pdu_reset_query pdu_reset_query;

    public Pdu2(prefixType pdu_type, int serial_number, String session_id, int length, int flags, int prefix_length, int max_length, String prefix, int asn) {
        this.protocol_version = 0;
        this.pdu_type = pdu_type;
        this.serial_number = serial_number;
        this.session_id = session_id;
        this.length = length;
        this.flags = flags;
        this.prefix_length = prefix_length;
        this.max_length = max_length;
        this.prefix = prefix;
        this.asn = asn;

        this.pdu_serial_notify = new pdu_serial_notify();
        this.pdu_cache_response = new pdu_cache_response();
        this.pdu_ipv4 = new pdu_ipv4();
        this.pdu_ipv6 = new pdu_ipv6();
        this.pdu_end_of_data = new pdu_end_of_data();
        this.pdu_header = new pdu_header();
        this.pdu_router_key = new pdu_router_key();
        this.pdu_serial_query = new pdu_serial_query();
        this.pdu_reset_query = new pdu_reset_query();
    }




    // -------------------------------------------

    /*
     * Check if the PDU is big enough for the PDU type it
     * pretend to be.
     * @param pdu A pointer to a PDU that is at least pdu->len byte large.
     * @return False if the check fails, else true
     */
    public boolean rtr_pdu_check_size(pdu_header pdu) {
        pdu_type type = pdu.type;
        pdu_error err_pdu = new pdu_error();
        boolean retval = false;
        int min_size = 0;

        switch (type) {
            case SERIAL_NOTIFY:
                if (pdu_serial_notify.len == pdu.len)
                    retval = true;
            case CACHE_RESPONSE:
                if (pdu_cache_response.len == pdu.len)
                    retval = true;
                break;
            case IPV4_PREFIX:
                if (pdu_ipv4.len == pdu.len)
                    retval = true;
                break;
            case IPV6_PREFIX:
                if (pdu_ipv6.len == pdu.len)
                    retval = true;
                break;
            case EOD:
                if (pdu_end_of_data.len == pdu.len)
                    retval = true;
                break;
            case CACHE_RESET:
                if (pdu_header.len == pdu.len)
                    retval = true;
                break;
            case ROUTER_KEY:
                if (pdu_router_key.len == pdu.len)
                    retval = true;
                break;
            case ERROR:
                //
                break;
            case SERIAL_QUERY:
                if (pdu_serial_query.len == pdu.len)
                    retval = true;
                break;
            case RESET_QUERY:
                if (pdu_reset_query.len == pdu.len)
                    retval = true;
                break;
            case RESERVED:
            default:
                retval = false;
                break;
        }

        return retval;
    }


    static int rtr_send_pdu(const struct rtr_socket *rtr_socket, const void *pdu, const unsigned len)
    {
        char pdu_converted[len];
        memcpy(pdu_converted, pdu, len);
        rtr_pdu_to_network_byte_order(pdu_converted);
        if (rtr_socket->state == RTR_SHUTDOWN)
            return RTR_ERROR;
    const int rtval = tr_send_all(rtr_socket->tr_socket, pdu_converted, len, RTR_SEND_TIMEOUT);

        if (rtval > 0)
            return RTR_SUCCESS;
        if (rtval == TR_WOULDBLOCK) {
            RTR_DBG1("send would block");
            return RTR_ERROR;
        }

        RTR_DBG1("Error sending PDU");
        return RTR_ERROR;
    }


    // ----------------------------------------------------------

    //
    // Getters
    //

    public int getProtocol_version() {
        return protocol_version;
    }

    public prefixType getPdu_type() {
        return pdu_type;
    }

    public int getSerial_number() {
        return serial_number;
    }

    public String getSession_id() {
        return session_id;
    }

    public int getLength() {
        return length;
    }

    public int getFlags() {
        return flags;
    }

    public int getPrefix_length() {
        return prefix_length;
    }

    public int getMax_length() {
        return max_length;
    }

    public String getPrefix() {
        return prefix;
    }

    public int getAsn() {
        return asn;
    }

    //
    // Setters
    //

    public void setProtocol_version(int protocol_version) {
        this.protocol_version = protocol_version;
    }

    public void setPdu_type(prefixType pdu_type) {
        this.pdu_type = pdu_type;
    }

    public void setSerial_number(int serial_number) {
        this.serial_number = serial_number;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }

    public void setPrefix_length(int prefix_length) {
        this.prefix_length = prefix_length;
    }

    public void setMax_length(int max_length) {
        this.max_length = max_length;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setAsn(int asn) {
        this.asn = asn;
    }
}
