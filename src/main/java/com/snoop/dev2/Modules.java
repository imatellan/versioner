package com.snoop.dev2;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Modules {

	private ArrayList<String> module;

	public Modules() {
		module = new ArrayList<String>();
	}

	public  ArrayList<String> getModule() {
		return module;
	}

	@XmlElement
	public void setModule(ArrayList<String> mod) {
		module = mod;
	}

}
