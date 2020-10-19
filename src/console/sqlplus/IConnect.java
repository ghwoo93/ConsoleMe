package console.sqlplus;


public interface IConnect {
	String ORACLE_DRIVER = "oracle.jdbc.OracleDriver";
	String ORACLE_URL = "jdbc:oracle:thin:@localhost:1521:orcl";
	int connectCount();
	void login(String url);
	void execute() throws Exception;
	void close();
	void connecter(String url, String user, String pwd);
	String getValue(String message);
	StringBuffer getPartedQueryString();
	String printLocal();
}
