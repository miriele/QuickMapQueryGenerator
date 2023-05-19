package quickmap_query_generator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;


public class QueryGeneratorPha {
	public void generate(String filePath, String sqlPath, int nSep) throws IOException {
		File				srcFile		= new File(filePath);					// 약국 데이터 파일 경로
		FileReader			fileReader	= new FileReader(srcFile);
		BufferedReader		bufReader	= new BufferedReader(fileReader);

		File				destFile	= new File(sqlPath); 
		FileOutputStream	fos			= new FileOutputStream(destFile);
		OutputStreamWriter	osw			= new OutputStreamWriter(fos, "MS949");	// sqldeveloper 에서 읽기 위해 MS949로 설정
		BufferedWriter		bufWriter	= new BufferedWriter(osw);
		
		String	line		= "";
    	int		STR_NUM		= 100000;	// 병원의 office_id와 중복을 피하기 위해
		int		cntTotal	= 0;
		int		nMaxAddrLen	= 0;
		int		nMaxNameLen	= 0;

		bufWriter.write("set define off;\n\n");
		bufWriter.write("insert all\n");

		while ((line = bufReader.readLine()) != null) {
			// [1] : 주소 / [2] : 약국 이름 / [3] : 전화번호
			// [4~10] : 영업 종료 시각
			// [12~18] : 영업 시작 시각
			// [23] : 위도 / [22] : 경도
			String[] arr			= line.split("\",\"");
			int		 service_id		= 99;								// 약국은 별도의 service_id가 없으므로 99로 저장
			String   insert_query	=
					"into qm_office "
					+	"(office_id, grade_id, office_regist_num, office_name, service_id, "
					+	"office_ad_state, office_review_num, office_star, "
					+	"office_loc_latitude, office_loc_longitude, "
					+	"office_start_mon, office_start_tue, office_start_wed, office_start_thu, office_start_fri, office_start_sat, office_start_sun, "
					+	"office_end_mon, office_end_tue, office_end_wed, office_end_thu, office_end_fri, office_end_sat, office_end_sun, "
					+	"office_address, office_tel, office_class)\n"
					+ "values "
					+	"(" + (cntTotal + STR_NUM) + " , 0, 'null" + (cntTotal + STR_NUM) + "' , '" + arr[2] + "', " + service_id + ", "
					+	"'0', 0, 0.0, "
					+	arr[23] + ", " + arr[22]+ ", '"
					+	arr[12] + "', '" + arr[13] + "', '" + arr[14] + "', '" + arr[15] + "', '" + arr[16] + "', '" + arr[17] + "', '" + arr[18] + "', '"
					+	arr[4] + "', '" + arr[5] + "', '" + arr[6] + "', '" + arr[7] + "', '" + arr[8] + "', '" + arr[9] + "', '" + arr[10] + "', '"
					+	arr[1] + "', '" + arr[3] + "', 1)\n";
			         	  
			bufWriter.write(insert_query);
			cntTotal++;
			
			if(cntTotal%nSep == 0) {
				bufWriter.write("select * from dual;\n\n");
				bufWriter.write("insert all\n");
			}

			if(nMaxAddrLen < arr[1].length()) {
				nMaxAddrLen = arr[1].length();
			}
			
			if(nMaxNameLen < arr[10].length()) {
				nMaxNameLen = arr[10].length();
			}
		}
		
		if(cntTotal%nSep != 0) {
			bufWriter.write("select * from dual;\n\n");
		}

		bufWriter.write("commit;\n\n");
		
		bufWriter.write("-- total row    : " + cntTotal    + "\n");
		bufWriter.write("-- max addr len : " + nMaxAddrLen + "\n");
		bufWriter.write("-- max name len : " + nMaxNameLen + "\n");
		
		bufReader.close();
		fileReader.close();
		bufWriter.close();
		osw.close();
		fos.close();
    }
}
