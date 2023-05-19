package quickmap_query_generator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;


public class QueryGeneratorHos {
	public void generate(String filePath, String svcPath, String sqlPath, int nSep) throws IOException {
		File				srcFile		= new File(filePath);					// 병원 데이터 파일 경로
		FileReader			fileReader	= new FileReader(srcFile);
		BufferedReader		bufReader	= new BufferedReader(fileReader);

		File				destFile	= new File(sqlPath); 
		FileOutputStream	fos			= new FileOutputStream(destFile);
		OutputStreamWriter	osw			= new OutputStreamWriter(fos, "MS949");	// sqldeveloper 에서 읽기 위해 MS949로 설정
		BufferedWriter		bufWriter	= new BufferedWriter(osw);
		
		File				svcFile			= new File(svcPath);				// service code file path
		FileReader			svcFileReader	= new FileReader(svcFile);
		BufferedReader		svcBufReader	= new BufferedReader(svcFileReader);

		String	line		= "";
		int		cntTotal	= 0;
		int		nMaxAddrLen	= 0;
		int		nMaxNameLen	= 0;
		int		nMaxNotiLen	= 0;

		HashMap<Integer, String>	services	= new HashMap<>();				// service_id 생성하기 위해 data_qm_service.txt에서 data 불러와서 저장
//		HashMap<String, String>		codes		= new HashMap<>();				// data_qm_office_hospital.txt에서 병원분류 코드 얻기위해 임시로 작성해 둠
		
		// service code 로딩
		while((line = svcBufReader.readLine()) != null) {
			String[] arr	= line.split("\\s*,\\s*");
			
			services.put(Integer.parseInt(arr[0]), arr[1]);
		}
		
		svcBufReader.close();
		svcFileReader.close();

		ArrayList<Integer>	keylist	= new ArrayList<>(services.keySet());		// service_id의 random 생성을 위해 services HashMap에서 keySet 가져옴

		// query 생성
		bufWriter.write("set define off;\n\n");
		bufWriter.write("insert all\n");

		while((line = bufReader.readLine()) != null) {
			// [1] : 주소 / [2] : 병원분류 / [3] : 병원분류명 / [7] : 비고(영업공지)
			// [10] : 병원 이름 / [11] : 전화번호
			// [13~19] : 영업 종료 시각
			// [21~27] : 영업 시작 시각
			// [32] : 위도 / [31] : 경도
			String[] arr			= line.split("\",\"");
			
			// 병원 분류가 C, G, N 인 병원만 목록에 추가
			if(!arr[2].equals("C") && !arr[2].equals("G") && !arr[2].equals("N")) {
				continue;
			}
			
			// service_id 생성
			int	service_id	= 0;

			if(arr[2].equals("C")) {
				for(Entry<Integer, String> entry : services.entrySet()) {
					if(arr[10].contains(entry.getValue())) {
						service_id = entry.getKey();
						break;
					}
				}
				
				if(service_id == 0) {
					long	seed	= System.currentTimeMillis();
					Random	rand	= new Random(seed);
					
					service_id	= keylist.get(rand.nextInt(keylist.size()-1));	// 약국 코드가 마지막에 들어가 있어서 -1 해줘야 한다 
				}
			} else if(arr[2].equals("G")) {
				service_id	= 60;	// 한의과
			} else if(arr[2].equals("N")) {
				service_id	= 50;	// 치과
			}
			
			String   insert_query	=
					"into qm_office "
					+ 	"(office_id, grade_id, office_regist_num, office_name, service_id, "
					+ 	"office_ad_state, office_review_num, office_star, "
					+ 	"office_loc_latitude, office_loc_longitude, "
					+	"office_business_hours_noti, "
					+	"office_start_mon, office_start_tue, office_start_wed, office_start_thu, office_start_fri, office_start_sat, office_start_sun, "
					+	"office_end_mon, office_end_tue, office_end_wed, office_end_thu, office_end_fri, office_end_sat, office_end_sun, "
					+ 	"office_address, office_tel, office_class)\n"
					+ "values "
					+ 	"(" + cntTotal + ", 0, 'null" + cntTotal + "' , '" + arr[10] + "', " + service_id + ", "
					+ 	"'0', 0, 0.0, "
					+	arr[32] + ", " + arr[31] + ", '"
					+	arr[7] + "', '"
					+	arr[21] + "', '" + arr[22] + "', '" + arr[23] + "', '" + arr[24] + "', '" + arr[25] + "', '" + arr[26] + "', '" + arr[27] + "', '" 
					+	arr[13] + "', '" + arr[14] + "', '" + arr[15] + "', '" + arr[16] + "', '" + arr[17] + "', '" + arr[18] + "', '" + arr[19] + "', '" 
					+	arr[1] + "', '" + arr[11] + "', 0)\n";

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
			
			if(nMaxNotiLen < arr[7].length()) {
				nMaxNotiLen = arr[7].length();
			}

//			codes.put(arr[2], arr[3]);											// data_qm_office_hospital.txt에서 병원분류 코드 얻기위해 임시로 작성해 둠
		}
		
		if(cntTotal%nSep != 0) {
			bufWriter.write("select * from dual;\n\n");
		}

		bufWriter.write("commit;\n\n");
		
		bufWriter.write("-- total row    : " + cntTotal    + "\n");
		bufWriter.write("-- max addr len : " + nMaxAddrLen + "\n");
		bufWriter.write("-- max name len : " + nMaxNameLen + "\n");
		bufWriter.write("-- max noti len : " + nMaxNotiLen + "\n");
		
		bufReader.close();
		fileReader.close();
		bufWriter.close();
		osw.close();
		fos.close();

		// data_qm_office_hospital.txt에서 병원분류 코드 얻기위해 임시로 작성해 둠
//		for(Entry<String, String> entry : codes.entrySet()) {
//			System.out.println(entry.getKey() + " : " + entry.getValue());
//		}
		//////////////////////////////////////////////////////////////////////
	}
}
