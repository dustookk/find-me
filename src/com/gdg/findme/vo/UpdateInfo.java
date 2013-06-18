package com.gdg.findme.vo;

public class UpdateInfo {
	private Double version;
	private String changeLog;
	private String link;
	
	public UpdateInfo() {
	}
	public UpdateInfo(Double version, String changeLog,
			String link) {
		this.version = version;
		this.changeLog = changeLog;
		this.link = link;
	}
	public Double getVersion() {
		return version;
	}
	public void setVersion(Double version) {
		this.version = version;
	}
	public String getChangeLog() {
		return changeLog;
	}
	public void setChangeLog(String changeLog) {
		this.changeLog = changeLog;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
}
