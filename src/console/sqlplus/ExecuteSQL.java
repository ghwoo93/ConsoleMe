//package jdbc25.etc;
//
//import java.sql.DriverManager;
//import java.sql.ResultSetMetaData;
//import java.sql.SQLException;
//import java.sql.Types;
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.Scanner;
//
//import collection20.ArrayListApp;
//import jdbc25.service.IConnectImpl;
//
//public class ExecuteSQL extends IConnectImpl {
//	String showUser;
//
//	ExecuteSQL(){
//
//	}
//	
//	
//	public int myconn(int f, String sqlplus) {
//		int returnnum=0;
//		switch (f) {
//		case 1 : 	
//			
//			int count=0;
//			if(sqlplus.equals("add"))count=1;
//			while(true) {
//				if(count>2) {
//					System.out.println("SP2-0157: 3회 시도후 ORACLE에 CONNECT 하지못하고 SQL*Plus을 종료했습니다.");
//					return -1;
//				}
//				Scanner sc = new Scanner(System.in);
//				System.out.print("사용자명 입력:");
//				String id= sc.nextLine();
//				System.out.print("사용자명 비밀번호:");
//				String pw= sc.nextLine();
//				try {
//					Class.forName(ORACLE_DRIVER);
//					conn = DriverManager.getConnection(ORACLE_URL,id,pw);
//					conn.setAutoCommit(false);
//				} catch (ClassNotFoundException e) {
//					System.out.println(e.getMessage());
//					continue;
//				} catch (SQLException e) {
//					System.out.println(e.getMessage());
//					count++;
//					continue; 
//
//				}//try  -- catch
//				showUser=id;
//				System.out.println(CONNECTION_STR);
//				break;
//			}//while 
//			return 0;
//		case 2 : 	
//			while(true) {
//				String id;
//				String pw;
//				String temp = null;
//				if(sqlplus.toLowerCase().contains("sqlplus")) 
//					temp=sqlplus.substring(8);
//				else if (sqlplus.toLowerCase().contains("conn")) 
//					temp=sqlplus.substring(5);
//				try {
//					int snum=temp.indexOf("/") ;
//					id = temp.substring(0,snum);
//					pw=sqlplus.substring(sqlplus.indexOf("/")+1,sqlplus.length());
//					Class.forName(ORACLE_DRIVER);
//					conn = DriverManager.getConnection(ORACLE_URL,id,pw);
//				} catch (ClassNotFoundException e) {
//					System.out.println(e.getMessage());
//					return -1;
//				} catch (SQLException e) {
//
//					System.out.println(e.getMessage());
//					myconn(1,sqlplus);
//					return 0;
//					
//		
//				}catch (Exception e) {
//					return -1;
//				}
//				//try  -- catch
//				showUser=id;
//				System.out.println(CONNECTION_STR);
//				return 0;
//				
//			}//while 
//
//		case 3 : 	
//			int count1=0;
//			while(true) {
//				if(count1>2) {
//					System.out.println("SP2-0157: 3회 시도후 ORACLE에 CONNECT 하지못하고 SQL*Plus을 종료했습니다.");
//					return -1;
//				}
//				String temp;
//				String id = null ;
//				if(sqlplus.toLowerCase().contains("sqlplus")) 
//					id=sqlplus.trim().substring(8);
//				else if (sqlplus.toLowerCase().contains("conn")) 
//					id=sqlplus.substring(5);
//				
//				Scanner sc = new Scanner(System.in);
//				
//				System.out.print("사용자명 비밀번호:");
//				String pw= sc.nextLine();
//				try {
//					Class.forName(ORACLE_DRIVER);
//					conn = DriverManager.getConnection(ORACLE_URL,id,pw);
//					conn.setAutoCommit(false);
//				} catch (ClassNotFoundException e) {
//					System.out.println(e.getMessage());
//					continue;
//				} catch (SQLException e) {
//					System.out.println(e.getMessage());
//					returnnum=myconn(1, "add");
//				}//try  -- catch
//				showUser=id;
//				if(returnnum==0)System.out.println(CONNECTION_STR);
//				break;
//			}//while 
//			return returnnum;
//
//
//		}	//switch
//		return 0;
//
//	}
//
//
//	public int  printSQLPlus() {
//
//		while(true) {
//			System.out.print("C:\\Users\\kosmo_22>");
//
//			Scanner sc1 = new Scanner(System.in);
//			String sqlplus = sc1.nextLine();
//
//			if(sqlplus.toLowerCase().contains("sqlplus")&&!(sqlplus.contains("/"))&&sqlplus.length()<=8) {
//				SQLPrint.printConnMessage();
//				int f= myconn(1,sqlplus);
//				if(f==-1) return -1;
//				break;
//
//			}else if(sqlplus.toLowerCase().startsWith("sqlplus ")&&!(sqlplus.contains("/"))) {
//				int f =myconn(3,sqlplus);
//				SQLPrint.printConnMessage();
//				if(f==-1) return -1;
//				break;
//
//			}else if(sqlplus.toLowerCase().startsWith("sqlplus ")&&sqlplus.contains("/")) {
//				int f =myconn(2,sqlplus);
//				SQLPrint.printConnMessage();
//				if(f==-1) return -1;
//				break;
//			}else {
//				System.out.println("'"+sqlplus+"'은(는) 내부 또는 외부 명령, 실행할 수 있는 프로그램, 또는\r\n" + 
//						"배치 파일이 아닙니다.\r\n");
//			}
//		}
//		return 0;
//	}
//
//
//	@Override
//	public void execute() throws Exception {
//
//		if(printSQLPlus()==-1)return;
//
//
//
//		while(true) {
//			StringBuffer queryB = new StringBuffer();
//			String query = null;
//			queryB.append(getQueryString()) ;
//			if("EXIT".equalsIgnoreCase(queryB.toString().trim())) {
//				System.out.println(EXIT_STR);
//				close();
//				return;
//			}
//
//			int f=0;
//			if(queryB.toString().equalsIgnoreCase("conn")) {
//				try {
//					myconn  (1, queryB.toString().toLowerCase());
//				} catch (Exception e) {
//					System.out.println("SP2-0042: 알 수 없는 명령어");
//					return;
//				}
//				System.out.println("연결되었습니다.");
//				continue;
//				
//			//conn 아이디 외	
//			}else if(queryB.toString().toLowerCase().startsWith("conn")&&queryB.toString().contains("/")) {
//				try {
//					f=myconn(2, queryB.toString().toLowerCase());
//				} catch (Exception e) {	}
//				if(f==-1) {
//					System.out.println("SP2-0042: 알 수 없는 명령어");
//					continue;
//				}
//				System.out.println("연결되었습니다.");
//				continue;
//// con 아이디만입력시
//			}else if(queryB.toString().toLowerCase().startsWith("conn ")&&!(queryB.toString().contains("/"))) {
//				try {
//					f=myconn(3, queryB.toString().toLowerCase());
//				} catch (Exception e) {	}
//				if(f==-1) {
//					System.out.println("SP2-0042: 알 수 없는 명령어");
//					continue;
//				}
//				System.out.println("연결되었습니다.");
//				continue;
//			}else if(queryB.toString().equalsIgnoreCase("show user")) {
//				System.out.println("USER는 '"+showUser+"' 입니다.");
//				continue;
//			}else if(queryB.toString().equalsIgnoreCase("show user;")) {
//				System.out.println("USER는 '"+showUser+"' 입니다.");
//				continue;
//			}else if(queryB.toString().equalsIgnoreCase("commit;")) {
//				conn.commit();
//				System.out.println("커밋 되었습니다");
//			}
//			//; 처리
//			int sqlnum=2;
//			while(true) {
//				if(queryB.toString().endsWith(";")) {	
//					break;
//				}else {
//					queryB.append(" "+getQueryStringsub(sqlnum));
//				}	
//				sqlnum++;
//			}
//			query=queryB.toString();
//			if(query.contains(";")) query=query.substring(0,query.length()-1);
//
//			try {
//				psmt = conn.prepareStatement(query);
//			} catch (Exception e) {
//				continue;
//			}
//
//
//			//desc
//			try {
//				if (query.trim().toUpperCase().startsWith("DESC ")) { 
//					String tableName=query.substring(query.indexOf(" ")+1).toUpperCase();
//					descprint(tableName);	
//					continue;
//				}
//				boolean bFlag=psmt.execute();
//
//				if(bFlag) {
//
//					rs=psmt.getResultSet();
//					ResultSetMetaData rsm=rs.getMetaData();
//					int columnCount = rsm.getColumnCount();
//					String length = null ;
//					ArrayList unerLine = new ArrayList(); 
//
//
//
//					for(int i=1; i<=columnCount;i++) {
//						int type = rsm.getColumnType(i);
//
//						switch (type) {
//
//						case Types.CHAR : length =rsm.getPrecision(i)+""; 
//						unerLine.add(Integer.parseInt(length));   
//						break; 
//
//						case Types.VARCHAR : length =rsm.getPrecision(i)+"";
//						unerLine.add(Integer.parseInt(length)); 
//						break; 
//
//						case Types.TIMESTAMP : length ="10";  
//						unerLine.add(Integer.parseInt(length)); 
//						break;
//
//						default: length =rsm.getPrecision(i)*2+"";  
//						unerLine.add(Integer.parseInt(length)); 
//
//						}
//						//좌우로 정렬
//						if(type==Types.NUMERIC)System.out.print(String.format("%"+length+"s ", rsm.getColumnName(i) ));
//						else System.out.print(String.format("%"+length+"s ", rsm.getColumnName(i) ));
//					}System.out.println();
//
//
//					//밑줄
//
//					for(int i =1; i<=columnCount; i++) {
//						int type = rsm.getColumnType(i);
//
//						switch (type) {
//						case Types.CHAR : length =rsm.getPrecision(i)+"";  break; 
//						case Types.VARCHAR : length =rsm.getPrecision(i)+"";  break; 
//						case Types.TIMESTAMP : length ="10";  break;
//						default: length =rsm.getPrecision(i)*2+"";  
//						}
//						for(int j =1; j<=Integer.parseInt(length); j++) {
//							System.out.print("=");
//						}System.out.print(" ");
//
//					}
//					System.out.println();
//
//
//
//					while(rs.next()) {
//						for(int i =1; i<=columnCount; i++) {
//							int type = rsm.getColumnType(i);
//
//							switch (type) {
//
//							case Types.CHAR : length =rsm.getPrecision(i)+"";  break; 
//
//							case Types.VARCHAR : length =rsm.getPrecision(i)+"";  break; 
//
//							case Types.TIMESTAMP : length ="10";  break;
//
//							default: length =rsm.getPrecision(i)*2+"";  
//
//							}
//							if(type==Types.NUMERIC)
//								System.out.print(String.format("%"+length+"s ", rs.getString(i)==null? "" :
//									rsm.getColumnType(i) ==Types.TIMESTAMP ?
//											rs.getString(i).substring(0,10) : rs.getString(i)));
//							else System.out.print(String.format("%-"+length+"s ", rs.getString(i)==null? "" :
//								rsm.getColumnType(i) ==Types.TIMESTAMP ?
//										rs.getString(i).substring(0,10) : rs.getString(i)));
//						}
//						System.out.println();
//					}
//
//				}else {
//
//					int result = psmt.getUpdateCount();
//					if(query.trim().toUpperCase().startsWith("UPDATE"))System.out.println(result+"개의 행이 수정되었습니다. ");
//					else if (query.trim().toUpperCase().startsWith("DELETE"))System.out.println(result+"개의 행이 삭제되었습니다.");
//					else if (query.trim().toUpperCase().startsWith("INSERT"))System.out.println(result+"개의 행이 입력되었습니다. ");
//					else if (query.trim().toUpperCase().startsWith("CREATE TABLE"))System.out.println("테이블이 생성되었습니다.");
//					else if (query.trim().toUpperCase().startsWith("DROP TABLE"))System.out.println("테이블이 삭제되었습니다.");
//					else if (query.trim().toUpperCase().startsWith("ALTER TABLE"))System.out.println("테이블이 수정되었습니다.");
//				}//else
//			}catch (Exception e) {
//				System.out.println(e.getMessage());
//
//			}
//		}//while
//	}
//
//	public void underLine(String length) {
//
//	}
//
//	public void descprint(String tableName) {
//		try {
//
//			String sql = DESC_CODE;
//
//			psmt = conn.prepareStatement(sql);
//			psmt.setString(1, tableName);
//			psmt.execute();
//			rs=psmt.getResultSet();
//			ResultSetMetaData rsm=rs.getMetaData();
//			int columnCount = rsm.getColumnCount();
//			String length = null ;
//
//			for(int i=1; i<=columnCount;i++) {
//
//				int type = rsm.getColumnType(i);
//
//				switch (type) {
//				case Types.CHAR : length =rsm.getPrecision(i)+"";  break; 
//				case Types.VARCHAR : length =rsm.getPrecision(i)+"";  break; 
//				case Types.TIMESTAMP : length ="10";  break;
//				default: length =rsm.getPrecision(i)*2+"";  
//				}
//				//좌우로 정렬
//				if(type==Types.NUMERIC)System.out.print(String.format("%"+length+"s ", rsm.getColumnName(i) ));
//				else System.out.print(String.format("%-"+length+"s ", rsm.getColumnName(i) ));
//
//
//			}System.out.println();
//
//			//밑줄
//			for(int i =1; i<=columnCount; i++) {
//				int type = rsm.getColumnType(i);
//
//				switch (type) {
//				case Types.CHAR : length =rsm.getPrecision(i)+"";  break; 
//				case Types.VARCHAR : length =rsm.getPrecision(i)+"";  break; 
//				case Types.TIMESTAMP : length ="10";  break;
//				default: length =rsm.getPrecision(i)*2+"";  
//				}
//				for(int j =1; j<=Integer.parseInt(length); j++) {
//					System.out.print("=");
//					if(j>30)break;
//				}System.out.print(" ");
//
//			}
//			System.out.println();
//
//
//			while(rs.next()) {
//				for(int i =1; i<=columnCount; i++) {
//					int type = rsm.getColumnType(i);
//					switch (type) {
//					case Types.CHAR : length =rsm.getPrecision(i)+"";  break; 
//					case Types.VARCHAR : length =rsm.getPrecision(i)+"";  break; 
//					case Types.TIMESTAMP : length ="10";  break;
//					default: length =rsm.getPrecision(i)*2+"";  
//					}
//					//좌우로 정렬
//					if(type==Types.NUMERIC) System.out.print(String.format("%"+length+"s ", rs.getString(i)));
//					else System.out.print(String.format("%-"+length+"s ", rs.getString(i)));
//
//				}			
//				System.out.println();
//			}
//		} catch (SQLException e) {
//
//			System.out.println(e.getMessage());
//		}
//	}
//
//
//	public static void main(String[] args) throws Exception {
//		System.out.println("Microsoft Windows [Version 10.0.18362.657]\r\n" + 
//				"(c) 2019 Microsoft Corporation. All rights reserved.\r\n");
//		while(true) {
//			new ExecuteSQL().execute();
//		}
//	}
//
//}
