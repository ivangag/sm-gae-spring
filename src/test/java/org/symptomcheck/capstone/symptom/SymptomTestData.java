package org.symptomcheck.capstone.symptom;

import java.util.Map;
import java.util.UUID;

import org.symptomcheck.capstone.repository.CheckIn;
import org.symptomcheck.capstone.repository.Doctor;
import org.symptomcheck.capstone.repository.FeedStatus;
import org.symptomcheck.capstone.repository.PainLevel;
import org.symptomcheck.capstone.repository.Patient;
import org.symptomcheck.capstone.repository.Question;
import org.symptomcheck.capstone.repository.QuestionType;
import org.symptomcheck.capstone.repository.Video;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This is a utility class to aid in the construction of
 * Video objects with random names, urls, and durations.
 * The class also provides a facility to convert objects
 * into JSON using Jackson, which is the format that the
 * VideoSvc controller is going to expect data in for
 * integration testing.
 * 
 * @author jules
 *
 */
public class SymptomTestData {

	public static long YEAR_SECONDS = 31536000;
	private static final ObjectMapper objectMapper = new ObjectMapper();
	
	/**
	 * Construct and return a Video object with a
	 * rnadom name, url, and duration.
	 * 
	 * @return
	 */
	public static Patient createDummyPatient(String medicalRecordNumber, 
			String firstName, String LastName) {
		return new Patient(medicalRecordNumber, firstName, LastName);
	}
	

	public static Doctor createDummyDoctor(String uniqueDoctorId, String firstName,
			String lastName) {
		// TODO Auto-generated method stub
		return new Doctor(uniqueDoctorId,firstName,lastName);
	}
	
	public static CheckIn createDummyCheckIn(Long timestamp, 
			PainLevel painLevel, 
			FeedStatus feedStatus,
			Map<String,String> Medications){
	
			CheckIn checkIn = new CheckIn(timestamp.toString(),painLevel,feedStatus);
			for(String medication : Medications.keySet()){
				Question question = new Question(String.format("Did you Take %s ?", medication), Medications.get(medication), 
						QuestionType.Medication, timestamp.toString());
				checkIn.addQuestions(question);			
				//checkIn.setQuestion(question);
			}
		
			
		/*Question question1 = new Question("Did you Take Lortan?","YES",QuestionType.Medication,timestamp.toString());
		Question question2 = new Question("Did you Take Oxytocin?","NO",QuestionType.Medication,timestamp.toString());
		Question question3 = new Question("Did you Take Mosticotol?","NO",QuestionType.Medication,timestamp.toString());
		checkIn.addQuestions(question1);
		checkIn.addQuestions(question2);
		checkIn.addQuestions(question3);*/
		return checkIn;
	}
	
	/**
	 *  Convert an object to JSON using Jackson's ObjectMapper
	 *  
	 * @param o
	 * @return
	 * @throws Exception
	 */
	public static String toJson(Object o) throws Exception{
		return objectMapper.writeValueAsString(o);
	}

}
