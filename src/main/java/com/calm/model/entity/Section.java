package com.calm.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@Entity
@Table(name = "sections")
public class Section {
	
	@Id
	private int id;
	
	private String number;
	
	private String name;
	
	public Section(int id, String number, String name) {
		this.id = id;
		this.number = number;
		this.name = name;
	}
}
