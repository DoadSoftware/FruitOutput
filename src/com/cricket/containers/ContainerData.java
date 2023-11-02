package com.cricket.containers;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name="containerData")
@XmlAccessorType(XmlAccessType.FIELD)
public class ContainerData {
	
	@XmlElementWrapper(name = "containers")
	@XmlElement(name = "container")
	private List<Container> containers;
	//List<Container> containers = new ArrayList<Container>
	
	@XmlTransient
	 private String manual_file_timestamp;
	
	public ContainerData(List<Container> containers) {
		super();
		this.containers = containers;
	}
	
	public ContainerData() {
		super();
	}

	public List<Container> getContainers() {
		return containers;
	}

	public void setContainers(List<Container> containers) {
		this.containers = containers;
	}

	public String getManual_file_timestamp() {
		return manual_file_timestamp;
	}

	public void setManual_file_timestamp(String manual_file_timestamp) {
		this.manual_file_timestamp = manual_file_timestamp;
	}
	
	
	
	
}
