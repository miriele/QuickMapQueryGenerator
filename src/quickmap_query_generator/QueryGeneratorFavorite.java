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


public class QueryGeneratorFavorite {
	public void generate(String userPath, String sqlPath, int nSep) throws IOException {
		File				fUser	= new File(userPath);				// 사용자 파일 경로
		FileReader			frUser	= new FileReader(fUser);
		BufferedReader		brUser	= new BufferedReader(frUser);

		File				fSql	= new File(sqlPath); 
		FileOutputStream	fosSql	= new FileOutputStream(fSql);
		OutputStreamWriter	oswSql	= new OutputStreamWriter(fosSql, "MS949");		// sqldeveloper 에서 읽기 위해 MS949로 설정
		BufferedWriter		bwSql	= new BufferedWriter(oswSql);
		
		ArrayList<String>	arrUser		= new ArrayList<>();
		
		String	line	= "";

		while((line = brUser.readLine()) != null) {
			arrUser.add(line.split("\\s*,\\s*")[0]);
		}
		
		int		NUM_HOS		= 18997;	// 20_office_hos.sql 파일 최하단에 total row 값
		int		NUM_FAV		= 30;		// 사용자당 넣을 최대 favorite 수
		int		cntTotal	= 0;		// 처리한 항목 카운트
		
		long	seed	= System.currentTimeMillis();
		Random	rand	= new Random(seed);

		bwSql.write("set define off;\n\n");
		bwSql.write("insert all\n");
		
		for(int j=0 ; j<arrUser.size() ; j++) {
			
			int	nFav	= rand.nextInt(NUM_FAV-1) + 1;	// 1개 ~ NUM_FAV개

			for(int i=0 ; i<nFav ; i++) {
				String   insert_query	=
							"into qm_favorite (user_id, office_id, favorite_reg) "
						+	"values ("
						+		"'" + arrUser.get(j) + "', "
						+		rand.nextInt(NUM_HOS) + ", "
						+		"sysdate - " + rand.nextInt(365) + ")\n";
				
				cntTotal++;
				bwSql.write(insert_query);
				
				if(cntTotal%nSep == 0) {
					bwSql.write("select * from dual;\n\n");
					bwSql.write("insert all\n");
				}
			}
		}

		if(cntTotal%nSep != 0) {
			bwSql.write("select * from dual;\n\n");
		}

		bwSql.write("commit;\n\n");
		bwSql.write("-- total row    : " + (cntTotal) + "\n");
		
		brUser.close();
		frUser.close();
		bwSql.close();
		oswSql.close();
		fosSql.close();
	}
}
