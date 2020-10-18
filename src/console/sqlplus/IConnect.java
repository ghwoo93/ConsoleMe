package console.sqlplus;


public interface IConnect {
	String ORACLE_DRIVER = "oracle.jdbc.OracleDriver";
	String ORACLE_URL = "jdbc:oracle:thin:@localhost:1521:orcl";
	void connectCount(String url);
	boolean connect(String url);
	void execute() throws Exception;
	void close();
	String getValue(String message);
	StringBuffer getPartedQueryString();
//	String getQueryString();
	String printLocal();
}
