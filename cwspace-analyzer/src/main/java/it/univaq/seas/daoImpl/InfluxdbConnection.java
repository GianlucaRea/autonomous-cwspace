package it.univaq.seas.daoImpl;

import it.univaq.seas.utilities.Utility;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;

/**
 * @author gianlucarea
 */
public class InfluxdbConnection {

    private static final String urlDOcker = "http://influxdb:8086", urlLocalhost = "http://localhost:8086";
    protected InfluxDB influxDB;

    public InfluxdbConnection() {
        String serverURL = (Utility.dockerized) ? urlDOcker : urlLocalhost;
        String username = "telegraf";
        String password = "secretpassword";
        this.influxDB = InfluxDBFactory.connect(serverURL,username,password);
    }

    @Override
    protected void finalize() throws Throwable {
        Runtime.getRuntime().addShutdownHook(new Thread(this.influxDB::close));
        super.finalize();
    }

}
