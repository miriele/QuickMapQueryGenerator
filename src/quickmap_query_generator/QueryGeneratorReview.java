package quickmap_query_generator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Random;


public class QueryGeneratorReview {
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
		
		int[] officeId = {313, 137, 147, 150, 171, 432, 454, 466, 472, 614, 542, 555, 907, 916, 814, 834, 842, 856, 1010, 1131, 1031, 1065, 1088, 1205, 1216, 1240, 1462, 1505, 1507, 1317, 1340, 1390, 1392, 1708, 1815, 2482, 2413, 2615, 2497, 2942, 6338, 6346, 6381, 5640, 6109, 6216, 5030, 5448, 3370, 4259, 4309, 3927, 3937, 3552, 4007, 4495, 3651, 3656, 4515, 3721, 3732, 4155, 9283, 9707, 8489, 9732, 8911, 9783, 8932, 8991, 9441, 9048, 9486, 9495, 9581, 7567, 7569, 7629, 6747, 7837, 7419, 7894, 7463, 7071, 7487, 12122, 12126, 12130, 12541, 12180, 11859, 12632, 12243, 12754, 12861, 12905, 10915, 10944, 10080, 10578, 11032, 11120, 11154, 10286, 10716, 10347, 10383, 10855, 10467, 15485, 16385, 16391, 15990, 16001, 15247, 16085, 15722, 14975, 16223, 14701, 13458, 13960, 14424, 13283, 13305, 14159, 18805, 18842, 18855, 18512, 18931, 18132, 18652, 18284, 18285, 17579, 17608, 16839, 17685, 17371, 17824, 16986, 16989, 17419};
		String[] userId = {"aaa", "bbb", "ccc", "ddd", "eee"};

		while((line = bufReader.readLine()) != null) {
			// [0] : 업장 ID
			String[] arr			= line.split("\\s*,\\s*");
			String   insert_query	=
					"into qm_review (office_id, user_id, review_data, review_reg, review_star)\n"
					+ "values ("
					+ officeId[rand.nextInt(officeId.length)] + ", "
					+ "'" + userId[rand.nextInt(userId.length)] + "', "
					+ "'" + arr[0] + "', sysdate, "
					+ (rand.nextInt(5)+1) + ")\n";
					
			bufWriter.write(insert_query);
			cntTotal++;
			
			if(cntTotal%nSep == 0) {
				bufWriter.write("select * from dual;\n\n");
				bufWriter.write("insert all\n");
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
