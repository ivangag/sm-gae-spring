package org.symptomcheck.capstone.repository;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.Element;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.repackaged.org.codehaus.jackson.annotate.JsonIgnoreProperties;

@PersistenceCapable
@JsonIgnoreProperties("key")
public class CheckIn {
	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	//@Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")
	//private String key;
	private Key key;

	@Persistent
    private PainLevel issuePainLevel = PainLevel.UNKNOWN;

	@Persistent
    private FeedStatus issueFeedStatus = FeedStatus.UNKNOWN;

	@Persistent
    private String issueDateTime;
	
	@Persistent
    private String patientMedicalNumber;
    

	@Persistent(embeddedElement = "true", serialized = "true", defaultFetchGroup="true") 
	@Element(embedded="true") 
	private List<Question> questions = new ArrayList<>();
	
	
    public CheckIn(){}
	public CheckIn(String date, PainLevel painLevel, FeedStatus feedStatus) {
		super();
		this.issueDateTime = date;
		this.issueFeedStatus = feedStatus;
		this.issuePainLevel = painLevel;
	}

	
    public PainLevel getIssuePainLevel() {
		return issuePainLevel;
	}

	public void setIssuePainLevel(PainLevel issuePainLevel) {
		this.issuePainLevel = issuePainLevel;
	}

	public FeedStatus getIssueFeedStatus() {
		return issueFeedStatus;
	}

	public void setIssueFeedStatus(FeedStatus issueFeedStatus) {
		this.issueFeedStatus = issueFeedStatus;
	}

	public String getIssueDateTime() {
		return issueDateTime;
	}

	public void setIssueDateTime(String issueDateTime) {
		this.issueDateTime = issueDateTime;
	}
	public String getPatientMedicalNumber() {
		return patientMedicalNumber;
	}
	public void setPatientMedicalNumber(String patientMedicalNumber) {
		this.patientMedicalNumber = patientMedicalNumber;
	}

	public List<Question> getQuestions() {
		return questions;
	}
	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}

	public void addQuestions(Question question) {
		this.questions.add(question);
	}

    
}
