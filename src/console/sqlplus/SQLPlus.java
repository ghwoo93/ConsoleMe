package console.sqlplus;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Vector;

public class SQLPlus extends IConnectImpl{
	
	public SQLPlus() {
		super(ORACLE_URL);
	}
	
	@Override
	public void execute() throws Exception {
		while(true) {
			String query = getPartedQueryString().toString();
			if("EXIT".equalsIgnoreCase(query.trim())) {
				System.out.println("Oracle Database 11g Enterprise Edition Release 11.2.0.1.0 "
						+ "- 64bit Production\r\nWith the Partitioning, OLAP, Data Mining "
						+ "and Real Application Testing options에서 분리되었습니다.");
				close();
				System.exit(0);
			}
			psmt=conn.prepareStatement(query);
			
			try {
				boolean flag = psmt.execute();
				if(flag) { //쿼리문이 select
					rs=psmt.getResultSet();
					ResultSetMetaData rsmd = rs.getMetaData();
					int columnCount = rsmd.getColumnCount();
					List<Integer> dashCount = new Vector<Integer>();
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
						String columnName = 
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
				} else {//기타 쿼리문
					int affected = psmt.getUpdateCount();
					if(query.trim().toUpperCase().startsWith("UPDATE")) {
						System.out.println(affected+"행이 수정되었어요.");
					}else if(query.trim().toUpperCase().startsWith("DELETE")) {
						System.out.println(affected+"행이 삭제되었어요.");
					}else if(query.trim().toUpperCase().startsWith("INSERT")) {
						System.out.println(affected+"행이 입력되었어요.");
					}
				}
				
			} catch(SQLException e) {
				System.out.println(e.getMessage());
			}
		}
	}
	
	public static void main(String[] args) throws Exception {
		new SQLPlus().execute();
	}
}
