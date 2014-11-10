package org.symptomcheck.capstone.repository;

import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.repackaged.org.codehaus.jackson.annotate.JsonIgnoreProperties;
import com.google.common.collect.Lists;

/**
 * Class entity to log GCM messaging history
 * @author Ivan
 *
 */
@PersistenceCapable
@JsonIgnoreProperties("key")
public class GcmTrack {


	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;

	@Persistent
	private String date;
	
	@Persistent
	private String bundleContent;
	
	@Persistent
	private boolean success;
	
	@Persistent
	private String resultInfo;
		
	@Persistent
	private List<String> gcmDestinationIds = Lists.newArrayList();
	
	
	public void addGcmDestinationIds(String gcmDestinationId) {
		if(!this.gcmDestinationIds.contains(gcmDestinationId))
			this.gcmDestinationIds.add(gcmDestinationId);
	}
	public List<String> getGcmDestinationIds() {
		return gcmDestinationIds;
	}
	public void setGcmDestinationIds(List<String> gcmDestinationIds) {
		this.gcmDestinationIds = gcmDestinationIds;
	}
	public String getBundleContent() {
		return bundleContent;
	}
	public void setBundleContent(String bundleContent) {
		this.bundleContent = bundleContent;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getResultInfo() {
		return resultInfo;
	}
	public void setResultInfo(String resultInfo) {
		this.resultInfo = resultInfo;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
}
