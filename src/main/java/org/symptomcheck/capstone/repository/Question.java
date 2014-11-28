package org.symptomcheck.capstone.repository;

import java.io.Serializable;

import javax.jdo.annotations.EmbeddedOnly;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.appengine.api.datastore.Key;



//TODO#FDAR_3 Questions Response associated to a Chec-In unit data
@PersistenceCapable
@EmbeddedOnly
@JsonIgnoreProperties("key")
public class Question implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5432181308639253357L;

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;
	
	@Persistent
	private String question;
	@Persistent
	private String response;
	@Persistent
	private String medicatationTakingTime;
	@Persistent
	private QuestionType questionType;
	
	public Question(){}
	public Question(String question, String response, QuestionType questionType, String medicatationTakingTime){
		this.question = question;
		this.response = response;
		this.questionType = questionType;
		this.medicatationTakingTime = medicatationTakingTime;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}
	public String getMedicatationTakingTime() {
		return medicatationTakingTime;
	}
	public void setMedicatationTakingTime(String medicatationTakingTime) {
		this.medicatationTakingTime = medicatationTakingTime;
	}
	public QuestionType getQuestionType() {
		return questionType;
	}
	public void setQuestionType(QuestionType questionType) {
		this.questionType = questionType;
	}
	

}

