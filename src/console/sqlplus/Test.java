package console.sqlplus;

import java.util.Scanner;

public class Test {
	Scanner sc = new Scanner(System.in);
	
	public StringBuffer getPartedQueryString() {
		//반환 값 생성 초기화
		StringBuffer resultSb = new StringBuffer();
		//플래그
		boolean flag = false;
		// >lineNum 첫번쩨로 실행 sql 나옴
		System.out.print("SQL> ");
		// 쿼리 입력받음
		String inputQuery = sc.nextLine();
		if(inputQuery.indexOf(";")>=0) {//;이 있다
			//resultSb에서 ; 제거
			resultSb.append(inputQuery.substring(0, inputQuery.indexOf(";")-1));
		}else {// -1 ;가 없다 
			flag = true;
			resultSb.append(inputQuery+" ");
			inputQuery="";
		}
		int lineNum = 2;
		while(flag) {
			// >lineNum 출력 2부터 출력
			System.out.print("  "+lineNum+"  ");
			inputQuery = sc.nextLine();
			if(inputQuery.indexOf(";")>=0) {//;이 있다
				// ;가 써지면 쿼리 종료 와일 탈출
				//resultSb에서 ; 제거
				resultSb.append(inputQuery.substring(0, inputQuery.length()-1));
				flag = false;
			}else {// -1 ;가 없다 
				// ;가 안써진 성공적 쿼리면 lineNum 증가
				resultSb.append(inputQuery+" ");
				inputQuery="";
				lineNum++;
				flag = true;
			}
		
		}
		return resultSb;
	}

	public static void main(String[] args) {
		Test test = new Test();
		System.out.println(test.getPartedQueryString());

	}

}
