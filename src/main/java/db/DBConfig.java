package db;

public class DBConfig {
    public final static String DRIVER_NAME = "org.h2.Driver";
    public final static String CONNECTION_URL = "jdbc:h2:tcp://localhost:9092/~/test";
    public final static String USERNAME = "sa";
    public final static String PASSWORD = "";
    public final static int DEFAULT_POOL_SIZE = 10;
}
