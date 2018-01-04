/**
 * Created by amir on 03/01/2018.
 */
public class Pdu {

    //  An eight-bit unsigned integer, currently 0, denoting the version of this protocol.
    public int protocol_version = 0;

    // An eight-bit unsigned integer, denoting the type of the PDU, e.g., IPv4 Prefix, etc.
    public int pdu_type;

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

    public Pdu(int protocol_version, int pdu_type, int serial_number, String session_id, int length, int flags, int prefix_length, int max_length, String prefix, int asn) {
        this.protocol_version = protocol_version;
        this.pdu_type = pdu_type;
        this.serial_number = serial_number;
        this.session_id = session_id;
        this.length = length;
        this.flags = flags;
        this.prefix_length = prefix_length;
        this.max_length = max_length;
        this.prefix = prefix;
        this.asn = asn;
    }

    //
    // Getters
    //

    public int getProtocol_version() {
        return protocol_version;
    }

    public int getPdu_type() {
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

    public void setPdu_type(int pdu_type) {
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
