package com.snoop.dev2;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Project {

	String modelVersion;
	String groupId;
	String artifactId;
	String version;
	Modules modules=new Modules();

	public String getModelVersion() {
		return modelVersion;
	}

	@XmlElement
	public void setModelVersion(String modelVersion) {
		this.modelVersion = modelVersion;
	}

	public String getGroupId() {
		return groupId;
	}

	@XmlElement
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getArtifactId() {
		return artifactId;
	}

	@XmlElement
	public void setArtifactId(String artifactId) {
		this.artifactId = artifactId;
	}

	public String getVersion() {
		return version;
	}

	@XmlElement
	public void setVersion(String version) {
		this.version = version;
	}

	public Modules getModules() {
		return modules;
	}

	@XmlElement
	public void setModules(Modules modules) {
		this.modules = modules;
	}

}
