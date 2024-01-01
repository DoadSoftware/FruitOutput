package com.cricket.containers;

import com.cricket.model.Inning;

public class BowlingCardFF {
	
	private String status;
	private Inning inning;
	private String header_text;
	private String sub_header_text;
	
	public String getHeader_text() {
		return header_text;
	}

	public void setHeader_text(String header_text) {
		this.header_text = header_text;
	}

	public String getSub_header_text() {
		return sub_header_text;
	}

	public void setSub_header_text(String sub_header_text) {
		this.sub_header_text = sub_header_text;
	}
	
	public BowlingCardFF() {
		super();
	}
	
	public BowlingCardFF(Inning inning) {
		super();
		this.inning = inning;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Inning getInning() {
		return inning;
	}

	public void setInning(Inning inning) {
		this.inning = inning;
	}

}
