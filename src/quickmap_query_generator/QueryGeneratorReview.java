package quickmap_query_generator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Random;


public class QueryGeneratorReview {
	public void generate(String datapath, String userPath, String sqlPath, int nSep) throws IOException {
		File				fData	= new File(datapath);				// 데이터 파일 경로
		FileReader			frData	= new FileReader(fData);
		BufferedReader		brData	= new BufferedReader(frData);

		File				fUser	= new File(userPath);				// 사용자 파일 경로
		FileReader			frUser	= new FileReader(fUser);
		BufferedReader		brUser	= new BufferedReader(frUser);

		File				fSql	= new File(sqlPath); 
		FileOutputStream	fosSql	= new FileOutputStream(fSql);
		OutputStreamWriter	oswSql	= new OutputStreamWriter(fosSql, "MS949");		// sqldeveloper 에서 읽기 위해 MS949로 설정
		BufferedWriter		bwSql	= new BufferedWriter(oswSql);
		
		ArrayList<String>	arrReview	= new ArrayList<>();
		ArrayList<String>	arrUser		= new ArrayList<>();
		
		String	line	= "";

		while((line = brData.readLine()) != null) {
			arrReview.add(line.split("\\s*,\\s*")[0]);
		}
		
		while((line = brUser.readLine()) != null) {
			arrUser.add(line.split("\\s*,\\s*")[0]);
		}
		
		int		NUM_HOS	= 18997;			// 20_office_hos.sql 파일 최하단에 total row 값
		int		NUM_PHA	= 5581;				// 21_office_pha.sql 파일 최하단에 total row 값
		int		NUM_REV	= 15;	// arrUser.size()-1;	// 업장당 넣을 최대 review 수 // -1 : admin 제외
		int		nTotal	= 0;
		int		cnt		= 0;				// 처리한 항목 카운트
		
		long	seed	= System.currentTimeMillis();
		Random	rand	= new Random(seed);

		// 병원 리뷰
		bwSql.write("set define off;\n\n");
		bwSql.write("insert all\n");
		
		for(int i=0 ; i<NUM_HOS ; i++) {
			int	nRev	= rand.nextInt(NUM_REV-1) + 1;	// 1개 ~ nRevMax개
			
			nTotal += nRev;
			
			for(int j=0 ; j<nRev ; j++) {
				String   insert_query	=
						"into qm_review (office_id, user_id, review_data, review_reg, review_star)\n"
						+ "values ("
						+ i + ", "
						+ "'" + arrUser.get(j) + "', "
						+ "'" + arrReview.get(rand.nextInt(arrReview.size())) + "', "
						+ "sysdate-" + rand.nextInt(365) + ", "							// 365 : 리뷰 작성 시기 조정
						+ (rand.nextInt(5)+1) + ")\n";
				
				cnt++;
				bwSql.write(insert_query);
				
				if(cnt%nSep == 0) {
					bwSql.write("select * from dual;\n\n");
					bwSql.write("insert all\n");
				}
			}
		}

		if(cnt%nSep != 0) {
			bwSql.write("select * from dual;\n\n");
		}

		// 약국 리뷰
		cnt	= 0;
		bwSql.write("insert all\n");
		
		for(int i=0 ; i<NUM_PHA ; i++) {
			int	nRev	= rand.nextInt(NUM_REV-1) + 1;	// 1개 ~ nRevMax개
			
			nTotal += nRev;
			
			for(int j=0 ; j<nRev ; j++) {
				String   insert_query	=
						"into qm_review (office_id, user_id, review_data, review_reg, review_star)\n"
						+ "values ("
						+ (i+100000) + ", "												// 100000 : QueryGeneratorPha.java STR_NUM 값
						+ "'" + arrUser.get(j) + "', "
						+ "'" + arrReview.get(rand.nextInt(arrReview.size())) + "', "
						+ "sysdate-" + rand.nextInt(365) + ", "							// 365 : 리뷰 작성 시기 조정
						+ (rand.nextInt(5)+1) + ")\n";
						
				cnt++;
				bwSql.write(insert_query);
				
				if(cnt%nSep == 0) {
					bwSql.write("select * from dual;\n\n");
					bwSql.write("insert all\n");
				}
			}
		}

		if(cnt%nSep != 0) {
			bwSql.write("select * from dual;\n\n");
		}

		bwSql.write("commit;\n\n");
		bwSql.write("-- total row    : " + (nTotal) + "\n");
		
		brUser.close();
		frUser.close();
		brData.close();
		frData.close();
		bwSql.close();
		oswSql.close();
		fosSql.close();
	}
}
