package console.sqlplus;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Vector;

public class SQLPlus extends IConnectImpl{
	
	private int sFlag = 0;
	
	public String desc(String query) {
		String tableName = query.substring(5);
		System.out.println(tableName);
		return "SELECT * FROM "+tableName;
	}
	
	
	public void select() throws SQLException {
		String[] descColumnName = new String[] {"Name","Null?","Type"};
		String columnName;
		
		rs=psmt.getResultSet();
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();
		
		List<Integer> dashCount = new Vector<Integer>();
		if(sFlag==1) {
			dashCount.add(20);
			dashCount.add(8);
			dashCount.add(20);
				for(int i=0;i<descColumnName.length;i++) {
				System.out.print(String.format(
						"%-"+(dashCount.get(i)+1)+"s",
						descColumnName[i]
						));
				}
			System.out.println();
			
			/* column metadate 밑의 대쉬 출력하기 */
			for(Integer dash:dashCount) {
				for(int i=0;i<dash;i++)
					System.out.print("=");
				System.out.print(" "); //column 하나 마다 끝에 한칸씩 띄움
			} System.out.println(); //column이름 전부출력후 줄바꿈
			
			for(int i=1;i<=columnCount;i++) {
				int j=0;
					System.out.print(String.format(
												"%-"+(dashCount.get(j++)+1)+"s"
												,rsmd.getColumnName(i) ));
					if(rsmd.isNullable(i)==1)
					System.out.print(String.format(
												"%-"+(dashCount.get(j++)+1)+"s"
												," " ));
					else
					System.out.print(String.format(
												"%-"+(dashCount.get(j++)+1)+"s"
												,"NOT NULL" ));
					System.out.print(String.format(
												"%-"+(dashCount.get(j++)+1)+"s"
												,rsmd.getColumnTypeName(i) ));
					
				System.out.println();
			}System.out.println();
		}else {
			//select
			for(int i=1;i<=columnCount;i++) {
				int types = rsmd.getColumnType(i);
				
				int length = rsmd.getPrecision(i);
				
				switch(types) {
				case Types.NCHAR:
				case Types.NVARCHAR:
					dashCount.add(length*2);break;
				case Types.TIMESTAMP:
				case Types.NUMERIC:
					dashCount.add(10);break;
				default:dashCount.add(length);
				}
				columnName = 
						rsmd.getColumnName(i).length()>dashCount.get(i-1)?
								rsmd.getColumnName(i).substring(0, dashCount.get(i-1)):
									rsmd.getColumnName(i);
				System.out.print(
						String.format(
								"%-"+(dashCount.get(i-1)+1)+"s", columnName));
			}
			System.out.println();
			for(Integer dash:dashCount) {
				for(int i=0;i<dash;i++) System.out.print('-');
				System.out.print(" ");
			}
			System.out.println();
			while(rs.next()) {
				for(int i=1;i<=columnCount;i++) {
					int type = rsmd.getColumnType(i);
					if(type==Types.TIMESTAMP) {
						System.out.print(String.format("%-11s", rs.getDate(i)));
					}
					else {
						System.out.print(
								String.format(
										"%-"+(dashCount.get(i-1)+1)+"s", rs.getString(i)));
					}
				}
				System.out.println();
			}
		}
	}
	
	public void exit() {
		System.out.println("Oracle Database 11g Enterprise Edition Release 11.2.0.1.0 "
				+ "- 64bit Production\r\nWith the Partitioning, OLAP, Data Mining "
				+ "and Real Application Testing options에서 분리되었습니다.");
		close();
		System.exit(0);
	}
	
	public void conn(String firstInput) {
		int dashFlag = firstInput.indexOf("/");
		String user="";
		String pwd="";
		if(firstInput.equalsIgnoreCase("conn")) {//아이디/비번 입력필요
			System.out.print("사용자명 입력:");
			user = sc.nextLine();
			System.out.print("비밀번호 입력:");
			pwd = sc.nextLine();
		}else if(dashFlag==-1) {//아이디입력됨
			System.out.print("비밀번호 입력:");
			user = firstInput.substring(firstInput.indexOf(" ")+1);
			pwd = sc.nextLine();
		}else if(dashFlag>=0){//아이디/비번 입력됨
			user = firstInput.substring(firstInput.indexOf(" ")+1,dashFlag);
			pwd = firstInput.substring(dashFlag+1);
			System.out.println(user+":"+pwd);
		}else {//아무것도 아닐때
			System.out.println(
				firstInput+"은(는) 내부 또는 외부 명령, 실행할 수 있는 프로그램, "
				+ "또는\r\n배치 파일이 아닙니다.");
			return;
		}
		connecter(ORACLE_URL, user, pwd);
	}
	
	public String show() {return getUser();}
	
	public void ed(String query) {
		try {
			Runtime.getRuntime().exec(
					"notepad "+query.trim().substring(3)+".txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//System.getProperty("user.home")
	//프로시저 안됨
	public String fileRead(String query) {
		BufferedReader br=null;
		String data="";
		String descQuery="";
		try {
//			System.out.println(System.getProperty("user.home")
//					+"\\"+query.substring(query.indexOf("@")+1,query.length()-1)+".sql");
			br = 
				new BufferedReader(
						new FileReader(
								System.getProperty("user.home")
									+"\\"+query.substring(query.indexOf("@")+1,query.length()-1)+".txt"));
			while((data=br.readLine())!=null) {
				descQuery+=data;
			}
			if(descQuery.endsWith(";")) 
				descQuery=descQuery.substring(0,descQuery.length()-1);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return descQuery;
	}
	
	public void commit() throws SQLException {
		conn.commit();
	}
	
	@Override
	public void execute() throws Exception {
		boolean ccFlag = false; 
		String query="";
		while(true) {
			//펄스면 입력받게
			if(!ccFlag) query = getPartedQueryString().toString();
			
			//conn,show,ed,@,commit
			if("EXIT".equalsIgnoreCase(query.trim())) {
				exit();
			}else if(query.startsWith("conn")) {
				conn(query);
			}else if(query.startsWith("show")) {
				System.out.println(show());
			}else if(query.startsWith("commit")) {
				commit();
				System.out.println("커밋완료");
			}else if(query.startsWith("ed")) {
				ed(query);
			}else if(query.startsWith("@")){
				query = fileRead(query);
				ccFlag = true;
				continue;
			}else {
				//여기서 desc
				if(query.startsWith("desc")) {
					//desc
					query = desc(query);
					sFlag = 1;
					ccFlag = true;
					System.out.println(query);
					continue;
				}else if(query.contains("SELECT")){
					psmt=conn.prepareStatement(query);
					boolean flag = psmt.execute();
					//select
					try {
						if(flag) { //쿼리문이 select
							select();
						} else {//기타 쿼리문
							int affected = psmt.getUpdateCount();
							if(query.trim().toUpperCase().startsWith("UPDATE")) {
								System.out.println(affected+"행이 수정되었어요.");
							}else if(query.trim().toUpperCase().startsWith("DELETE")) {
								System.out.println(affected+"행이 삭제되었어요.");
							}else if(query.trim().toUpperCase().startsWith("INSERT")) {
								System.out.println(affected+"행이 입력되었어요.");
							}else if(query.trim().toUpperCase().startsWith("CREATE")) {
								System.out.println(affected);
							}else if(query.trim().toUpperCase().startsWith("GRANT")) {
								System.out.println(affected);
							}
						}
						
					} catch(SQLException e) {
						System.out.println(e.getMessage());
					}
				}
			}
			ccFlag = false; 
			sFlag = 0;
		}//while
	}
	
	public static void main(String[] args) throws Exception {
		SQLPlus main = new SQLPlus();
		while(true) {
			if(main.conn==null) {
				main.login(ORACLE_URL);
			} else if(main.conn!=null) {
				main.execute();
			}
			if(main.getConnectCounter()>3) {
				main.setConnectCounter(1);
				System.out.println("종료합니다");
				break;
			}
			
		}
	}
}
