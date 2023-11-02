package com.cricket.containers;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="container")
@XmlAccessorType(XmlAccessType.FIELD)
public class Container implements Comparable<Container> {
//public class Container {
	
	@XmlElement(name = "container_id")
	private String container_id;
	  
	@XmlElement(name = "container_value")
	private String container_value;

	public Container(String container_id, String container_value) {
		super();
		this.container_id = container_id;
		this.container_value = container_value;
	}
	
	public Container() {
		super();
	}

	public String getContainer_id() {
		return container_id;
	}

	public void setContainer_id(String container_id) {
		this.container_id = container_id;
	}

	public String getContainer_value() {
		return container_value;
	}

	public void setContainer_value(String container_value) {
		this.container_value = container_value;
	}

	@Override
	public int compareTo(Container con) {
		
		return this.container_id.compareTo(con.container_id);
	}
	
	
}
