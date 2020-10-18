package console.sqlplus;


import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class IConnectImpl implements IConnect{
	public Connection conn;
	public ResultSet rs;
	public Statement stmt;
	public PreparedStatement psmt;
	public CallableStatement csmt;
	public Scanner sc = new Scanner(System.in);
	
	static {
		try {
			Class.forName(ORACLE_DRIVER);
		} catch (ClassNotFoundException e) {
			System.out.println("Driver loading failed!:"+e.getMessage());
		}
	}
	
	public IConnectImpl() {}
	
	public IConnectImpl(String url) {
		connectCount(url);
	}
	@Override
	public void connectCount(String url) {
		boolean flag = false;
		int tryCounter =1;
		while(!flag) {//2번더틀림 cmd돌아감
			//커넥트로 cmd 출력 로그인시도
			flag = connect(url);
			tryCounter++;
			if(tryCounter==3) {
				close();
				break;
			}
		}
	}
	@Override
	public boolean connect(String url) {
		boolean result = false;
		String firstInput = printLocal();
		int dashFlag = firstInput.indexOf("/");
		String id="";
		String pwd="";
		if(firstInput.equalsIgnoreCase("sqlplus")) {//아이디/비번 입력필요
			System.out.print("사용자명 입력:");
			id = sc.nextLine();
			System.out.print("비밀번호 입력:");
			pwd = sc.nextLine();
		}else if(dashFlag==-1) {//아이디입력됨
			System.out.print("비밀번호 입력:");
			id = firstInput.substring(firstInput.indexOf(" ")+1);
			pwd = sc.nextLine();
		}else if(dashFlag>=0){//아이디/비번 입력됨
			id = firstInput.substring(firstInput.indexOf(" ")+1,dashFlag);
			pwd = firstInput.substring(dashFlag+1);
		}else {//아무것도 아닐때
			System.out.println(
				firstInput+"은(는) 내부 또는 외부 명령, 실행할 수 있는 프로그램, "
				+ "또는\r\n배치 파일이 아닙니다.");
		}
		try {
			conn = DriverManager.getConnection(url, id, pwd);
			System.out.println("다음에 접속됨:\r\n" + 
				"Oracle Database 11g Enterprise Edition Release 11.2.0.1.0 - 64bit Production\r\n" + 
				"With the Partitioning, OLAP, Data Mining and Real Application Testing options");
			result = true;
		} catch (SQLException e) {
			System.out.println("ERROR:\r\n"+e.getMessage());
		}
		return result;
	}

	@Override
	public void execute() throws Exception {}

	@Override
	public void close() {
		try {
			if(rs!=null) rs.close();
			if(stmt!=null) stmt.close();
			if(psmt!=null) psmt.close();
			if(csmt!=null) csmt.close();
			if(conn!=null) conn.close();
			
		} catch(SQLException e) {
			
		}
	}
	
	@Override
	public String getValue(String message) {
		System.out.println(message+"을(를) 입력하세요");
		String value = sc.nextLine();
		if("exit".equalsIgnoreCase(value)) {
			close();
			System.out.println("프로그램 종료");
			System.exit(0);
		}
		return value;
	}
	
	@Override
	public StringBuffer getPartedQueryString() {
		StringBuffer resultSb = new StringBuffer();
		boolean flag = false;
		System.out.print("SQL> ");
		String inputQuery = sc.nextLine();
		if(inputQuery.indexOf(";")>=0) {//;이 있다
			resultSb.append(inputQuery.substring(0, inputQuery.indexOf(";")-1));
		}else {// -1 ;가 없다 
			flag = true;
			resultSb.append(inputQuery+" ");
			inputQuery="";
		}
		int lineNum = 2;
		while(flag) {
			System.out.print("  "+lineNum+"  ");
			inputQuery = sc.nextLine();
			if(inputQuery.indexOf(";")>=0) {//;이 있다
				resultSb.append(inputQuery.substring(0, inputQuery.length()-1));
				flag = false;
			}else {// -1 ;가 없다 
				resultSb.append(inputQuery+" ");
				inputQuery="";
				lineNum++;
				flag = true;
			}
		}
		return resultSb;
	}
	
//	@Override
//	public String getQueryString() {
//		System.out.print("SQL>");
//		return sc.nextLine();
//	}
	
	//로컬 C:\Users\계정명> 출력 입력대기
	@Override
	public String printLocal() {
		System.out.print(System.getProperty("user.home")+">");
		return sc.nextLine();
	}

}
