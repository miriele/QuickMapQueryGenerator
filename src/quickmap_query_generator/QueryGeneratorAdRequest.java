package quickmap_query_generator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Random;


public class QueryGeneratorAdRequest {
	public void generate(String filePath, String sqlPath, int nSep) throws IOException {
		File				srcFile		= new File(filePath);				// 데이터 파일 경로
		FileReader			fileReader	= new FileReader(srcFile);
		BufferedReader		bufReader	= new BufferedReader(fileReader);

		File				destFile	= new File(sqlPath); 
		FileOutputStream	fos			= new FileOutputStream(destFile);
		OutputStreamWriter	osw			= new OutputStreamWriter(fos, "MS949");		// sqldeveloper 에서 읽기 위해 MS949로 설정
		BufferedWriter		bufWriter	= new BufferedWriter(osw);
		
		String	line		= "";
		int		cntTotal	= 0;

		bufWriter.write("set define off;\n\n");
		bufWriter.write("insert all\n");

		long	seed	= System.currentTimeMillis();
		Random	rand	= new Random(seed);

		int		nMethod	= 1;	// 0 : data_qm_ad_request.txt 사용, 1 : random 수 사용
		
		if(nMethod == 0) {
			while((line = bufReader.readLine()) != null) {
				// [0] : 업장 ID
				String[]	arr		= line.split("\\s*,\\s*");
				int			dateAd	= rand.nextInt(120);
				
				String	insert_query	=
							"into qm_ad_request (office_id, ad_request_start, ad_request_end, ad_request_level, ad_request_submit, ad_request_confirm)\n"
						+	"values (" 
						+		arr[0] + ", "
						+		"sysdate - " + dateAd + ", "
						+		"sysdate + " + dateAd + ", "
						+		(rand.nextInt(6)+2) + ",  "
						+		"sysdate - " + (dateAd+5) + ", "
						+		"sysdate - " + (dateAd+5) + ")\n";

				bufWriter.write(insert_query);
				cntTotal++;
				
				if(cntTotal%nSep == 0) {
					bufWriter.write("select * from dual;\n\n");
					bufWriter.write("insert all\n");
				}
			}
		} else if(nMethod == 1) {
			int		NUM_HOS	= 18997;	// 20_office_hos.sql 파일 최하단에 total row 값
			int		NUM_PER	= 20;		// 20%
			int		cntMax	= (int)(NUM_HOS*NUM_PER/100);

			for(int i=0 ; i<cntMax ; i++) {						// 전체의 20% 수준의 officeId로 AdRequest 데이터 생성
				int		dateAd	= rand.nextInt(120);
				
				String	insert_query	=
						  "into qm_ad_request (office_id, ad_request_start, ad_request_end, ad_request_level, ad_request_submit, ad_request_confirm)\n"
						+ "values (" 
						+	rand.nextInt(NUM_HOS) + ", "
						+	"sysdate - " + dateAd + ", "
						+	"sysdate + " + dateAd + ", "
						+	(rand.nextInt(6)+2) + ",  "
						+	"sysdate - " + (dateAd+5) + ", "
						+	"sysdate - " + (dateAd+5) + ")\n";

				bufWriter.write(insert_query);
				cntTotal++;
				
				if(cntTotal%nSep == 0) {
					bufWriter.write("select * from dual;\n\n");
					bufWriter.write("insert all\n");
				}
			}
		}
		
		if(cntTotal%nSep != 0) {
			bufWriter.write("select * from dual;\n\n");
		}

		bufWriter.write("commit;\n\n");
		bufWriter.write("-- total row    : " + cntTotal    + "\n");
		
		bufReader.close();
		fileReader.close();
		bufWriter.close();
		osw.close();
		fos.close();
	}
}
