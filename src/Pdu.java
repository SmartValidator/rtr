import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by amir on 03/01/2018.
 */
public class Pdu {

    public Pdu(int serial_number) {
    }

    public enum prefixType {
        IPV4(0),
        IPV6(1);

        prefixType(int prefix_type) {
            this.prefix_type = prefix_type;
        }
        private final int prefix_type;
        public int getPrefixType() {
            return prefix_type;
        }
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

    enum pdu_type_enum {
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

        pdu_type_enum(int type) {
            this.type = type;
        }

        private final int type;

        public int getType() {
            return type;
        }
    };


    //  An eight-bit unsigned integer, currently 0, denoting the version of this protocol.
    public int protocol_version = 0;
    // An eight-bit unsigned integer, denoting the type of the PDU, e.g., IPv4 prefixType, etc.
    public pdu_type_enum pdu_type;
    // Byte for the header - going to be session_id or filled with zeros
    public int header_bytes = 0;
    // The Serial Number of the RPKI Cache
    public int serial_number;
    // Session ID to identify the instance of the cache
    public int session_id;
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

    public List<List<String>> prefixes_asn;
    public List<Rtr.Roa> roas;


    public Pdu(int serial_number, List<Rtr.Roa> roas) {
        this.setSerial_number(serial_number);
        this.roas = roas;
    }

    //
    // Getters
    //
    public Rtr.Roa getRoaInPos(int idx) {
        return this.roas.get(idx);
    }
    public int getProtocol_version() {
        return protocol_version;
    }
    public int getPdu_type() {
        return pdu_type.getType();
    }
    public int getSerial_number() {
        return serial_number;
    }
    public int getSession_id() {
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
    public int getHeader_bytes() {  return header_bytes; }


    //
    // Setters
    //
    public void setHeader_bytes(int header_bytes) {
        this.header_bytes = header_bytes;
    }
    public void setProtocol_version(int protocol_version) {
        this.protocol_version = protocol_version;
    }
    public void setPdu_type(pdu_type_enum pdu_type) {
        this.pdu_type = pdu_type;
    }
    public void setSerial_number(int serial_number) {
        this.serial_number = serial_number;
    }
    public void setSession_id(int session_id) {
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

    /**
     * The cache notifies the router that the cache has new data.
     * @param session_id
     */
    public void serialNotify(int session_id) {
        this.setPdu_type(pdu_type_enum.SERIAL_NOTIFY);
        this.setLength(12);
        this.setSession_id(session_id);
        this.setHeader_bytes(session_id);
    }

    /**
     * The router sends Serial Query to ask the cache for all
       payload PDUs that have Serial Numbers higher than the Serial Number
       in the Serial Query.
     * @param session_id
     */
    public void serialQuery(int session_id) {
        this.setPdu_type(pdu_type_enum.SERIAL_QUERY);
        this.setLength(12);
        this.setSession_id(session_id);
        this.setHeader_bytes(session_id);
    }

    /**
     * The router tells the cache that it wants to receive the
       total active, current, non-withdrawn database. The cache responds
       with a Cache Response PDU
     */
    public void resetQuery() {
        this.setPdu_type(pdu_type_enum.RESET_QUERY);
        this.setLength(8);
    }

    /**
     * The cache responds with zero or more payload PDUs.
     */
    public Pdu cacheResponsePDU(int session_id) {
        this.setPdu_type(pdu_type_enum.CACHE_RESPONSE);
        this.setLength(8);
        this.setSession_id(session_id);
        this.setHeader_bytes(session_id);

        return this;
    }

    public Pdu ip4Prefix() {
        this.setPdu_type(pdu_type_enum.IPV4_PREFIX);
        this.setLength(20);

        return this;
    }

    public Pdu ip6Prefix() {
        this.setPdu_type(pdu_type_enum.IPV6_PREFIX);
        this.setLength(32);

        return this;
    }

    /**
     * The cache tells the router it has no more data for the
       request.
     * @param session_id
     */
    public Pdu endOfData(int session_id) {
        this.setPdu_type(pdu_type_enum.EOD);
        this.setLength(12);
        this.setSession_id(session_id);
        this.setHeader_bytes(session_id);

        return this;
    }

    /**
     * The cache may respond to a Serial Query informing the router that the
       cache cannot provide an incremental update starting from the Serial
       Number specified by the router.
     */
    public Pdu cacheReset() {
        this.setPdu_type(pdu_type_enum.CACHE_RESET);
        this.setLength(8);

        return this;
    }

    public ByteArrayOutputStream encode(Rtr.Roa roa) {
        // The buffer to write on
        ByteArrayOutputStream pdu_byte_obj = new ByteArrayOutputStream(this.getLength());

        byte[] protocol_bytes = BigInteger.valueOf(this.getProtocol_version()).toByteArray();
        byte[] type_bytes = BigInteger.valueOf(this.getPdu_type()).toByteArray();
        //byte[] type_bytes = Integer.toString(this.getPdu_type()).getBytes();
        byte[] header_bytes = BigInteger.valueOf(this.getHeader_bytes()).toByteArray();
        byte[] length_bytes = BigInteger.valueOf(this.getLength()).toByteArray();
        try {
            pdu_byte_obj.write(protocol_bytes);
            pdu_byte_obj.write(type_bytes);
            pdu_byte_obj.write(header_bytes);
            pdu_byte_obj.write(length_bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }

        switch (this.getPdu_type()) {
            case 0: // SERIAL_NOTIFY
                pdu_byte_obj.write(this.getSerial_number());
                break;
            case 1: // SERIAL_QUERY
                pdu_byte_obj.write(this.getSerial_number());
                break;
            case 2: // RESET_QUERY
                // no payload
                break;
            case 3: // CACHE_RESPONSE
                // no payload
                break;
            case 4: // IPV4_PREFIX
                pdu_byte_obj = writeIPPayload(pdu_byte_obj, roa);
                break;
            case 6: //IPV6_PREFIX
                pdu_byte_obj = writeIPPayload(pdu_byte_obj, roa);
                break;
            case 7: // EOD
                pdu_byte_obj.write(this.getSerial_number());
                break;
            case 8: // CACHE_RESET
                // no payload
                break;
        }

        return pdu_byte_obj;
    }


    public ByteArrayOutputStream writeIPPayload(ByteArrayOutputStream buffer, Rtr.Roa roa) {
        byte[] prefix_min_length_bytes = BigInteger.valueOf(this.getPrefix_length()).toByteArray();
        byte[] prefix_max_length_bytes = BigInteger.valueOf(this.getMax_length()).toByteArray();
        byte[] flags_bytes = BigInteger.valueOf(this.getFlags()).toByteArray();
        try {
            buffer.write(flags_bytes);
            buffer.write(prefix_min_length_bytes);
            buffer.write(prefix_max_length_bytes);
            buffer.write("0".getBytes());
            buffer.write(roa.getPrefix().getBytes());
            buffer.write(roa.getAsn().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return buffer;
    }

}
