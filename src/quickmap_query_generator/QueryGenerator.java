package quickmap_query_generator;

import java.io.IOException;

public class QueryGenerator {

	public static void main(String[] args) throws IOException {
		String	path	= System.getProperty("user.dir") + "\\src\\quickmap_query_generator\\";
    	int		nSep	= 500;		// insert all을 몇 개 단위로 할건지
		
		QueryGeneratorUserGrade	userGrade	= new QueryGeneratorUserGrade();
		userGrade.generate(path + "data_qm_user_grade.txt", path + "10_user_grade.sql", nSep);
		
		QueryGeneratorOfficeGrade	officeGrade	= new QueryGeneratorOfficeGrade();
		officeGrade.generate(path + "data_qm_office_grade.txt", path + "11_office_grade.sql", nSep);
		
		QueryGeneratorService	service	= new QueryGeneratorService();
		service.generate(path + "data_qm_service.txt", path + "12_service.sql", nSep);
		
		QueryGeneratorSymptom	symptom	= new QueryGeneratorSymptom();
		symptom.generate(path + "data_qm_symptom.txt", path + "13_symptom.sql", nSep);
		
		QueryGeneratorHos	genHos	= new QueryGeneratorHos();
		genHos.generate(path + "data_qm_office_hospital.txt", path + "data_qm_service.txt", path + "20_office_hos.sql", nSep);
		
		QueryGeneratorPha	genPha	= new QueryGeneratorPha();
		genPha.generate(path + "data_qm_office_pharmacy.txt", path + "21_office_pha.sql", nSep);
		
		System.out.println("Generating Query Finished");
	}
}
