package com.bean;

import java.util.ArrayList;
import java.util.List;

public class Person {

	String firstNameBasic;
	String secondNameBasic;
	String firstNameAddit;
	String secondNameAddit;	
	String address;
	String intencja;
	int omadla;
	public List<Integer> history;
	List<Integer> historyStat;
	int id;
	
	public String getFirstNameBasic() {
		return firstNameBasic;
	}

	public String getSecondNameBasic() {
		return secondNameBasic;
	}

	public String getFirstNameAddit() {
		return firstNameAddit;
	}

	public String getSecondNameAddit() {
		return secondNameAddit;
	}

	public String getAddress() {
		return address;
	}

	public int getId() {
		return id;
	}
	
	public int getOmadla() {
		return omadla;
	}
	
	public List<Integer> getHistory() {
		return history;
	}

	public List<Integer> getHistoryStat() {
		return historyStat;
	}
	
	public Person(int id, String firstNameBasic, String secondNameBasic,
			String firstNameAddit, String secondNameAddit, String address, String intencja,
			int omadla, List<Integer> history) {
		super();
		this.id = id;
		this.firstNameBasic = firstNameBasic;
		this.secondNameBasic = secondNameBasic;
		this.firstNameAddit = firstNameAddit;
		this.secondNameAddit = secondNameAddit;
		this.address = address;
		this.intencja=intencja;
		this.omadla=omadla;
		this.history =new ArrayList<Integer>();
		this.history= history;
		this.historyStat = history;
	}
}
