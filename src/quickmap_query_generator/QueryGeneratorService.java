package quickmap_query_generator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;


public class QueryGeneratorService {
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
		int		nMaxNameLen	= 0;

		bufWriter.write("set define off;\n\n");
		bufWriter.write("insert all\n");

		while((line = bufReader.readLine()) != null) {
			// [0] : 서비스ID / [1] : 진료과명
			String[] arr			= line.split("\\s*,\\s*");
			//insert into qm_service (service_id, service_value) values (0, '감염내과');
			String   insert_query	=
					"into qm_service "
					+ 	"(service_id, service_name)"
					+ "values "
					+ 	"(" + arr[0] + ", '" + arr[1] + "')\n";

			bufWriter.write(insert_query);
			cntTotal++;
			
			if(cntTotal%nSep == 0) {
				bufWriter.write("select * from dual;\n\n");
				bufWriter.write("insert all\n");
			}

			if(nMaxNameLen < arr[1].length()) {
				nMaxNameLen = arr[1].length();
			}
		}
		
		if(cntTotal%nSep != 0) {
			bufWriter.write("select * from dual;\n\n");
		}

		bufWriter.write("commit;\n\n");
		
		bufWriter.write("-- total row    : " + cntTotal    + "\n");
		bufWriter.write("-- max name len : " + nMaxNameLen + "\n");
		
		bufReader.close();
		fileReader.close();
		bufWriter.close();
		osw.close();
		fos.close();
	}
}
