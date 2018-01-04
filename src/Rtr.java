import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Rtr {

    private Connection connection;
    private List<Roa> validated_roas = new ArrayList<>();
    //private Server srvr;

    public class Roa{
        private int roa_id;
        private String asn;
        private String prefix;
        private String maxLength;
        private String validity = "";

        public Roa(int roa_id, String asn, String prefix, String maxLength){
            this.roa_id = roa_id;
            this.asn = asn;
            this.prefix = prefix;
            this.maxLength = maxLength;
        }

        public void printRoa() {
            System.out.println("-------- ROA " + this.roa_id + " ------------");
            System.out.println("ASN: "+ this.asn);
            System.out.println("Prefix: "+ this.prefix);
            System.out.println("MaxLength: "+ this.maxLength);
            System.out.println("Validatiy: "+ this.validity);
        }
    }

    public Rtr() {

        // Register to PostegrSQL
        System.out.println("-------- PostgreSQL JDBC Connection ------------");
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }
        System.out.println("PostgreSQL JDBC Driver Registered!");

        // Connect to the db
        connection = null;
        try {
            connection = DriverManager.getConnection(
                    "jdbc:postgresql://smart-validator.net/smart_validator_test_3", "svt1",
                    "XagO7kfuGA1ZMxhP45u7zkna7eMB235zSTfCSzFpOy76OckuubXqlCxGyyC");
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
            return;
        }

        if (connection != null) {
            System.out.println("Connectection established");
        } else {
            System.out.println("Failed to make connection!");
        }
    }

    private void getRoas( ) {
        try {
            // Get the database metadata
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM validated_roas");

            int roa_id;
            String asn;
            String prefix;
            String maxLength;
            while (rs.next()){
                roa_id = rs.getInt(1);
                asn = rs.getString(2);
                prefix = rs.getString(3);
                maxLength = rs.getString(4);
                validated_roas.add(new Roa(roa_id, asn, prefix, maxLength));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void printRoas() {
        for (Roa roa : validated_roas) {
            roa.printRoa();
        }
    }

    public List<Roa> sendRoas() {
        // send the ROAs to the router
        System.out.println("I am sending");
        return validated_roas;
    }

    public static void main(String[] args) {

        System.out.println("Hello, World!");

        Rtr rpki = new Rtr();
        rpki.getRoas();

        Server srvr = new Server();
        srvr.server(rpki);
    }
}
