package com.gdg.findme.vo;

public class Contact {
	private int _id;
	private String name;
	private String number;
	
	public Contact() {
	}
	public Contact(String name, String number) {
		this.name = name;
		this.number = number;
	}
	
	public Contact(int _id, String name, String number) {
		this._id = _id;
		this.name = name;
		this.number = number;
	}
	
	public int getId() {
		return _id;
	}
	public void setId(int _id) {
		this._id = _id;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}
	@Override
	public String toString() {
		return "Contact [_id=" + _id + ", name=" + name + ", number=" + number
				+ "]";
	}
	
}
