package com.calm.pdd.core.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "sections")
public class Section {
	
	@Id
	private int id;
	
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int sectionOrder;
	
	private String number;
	
	private String name;
	
	public Section(int id, String number, String name) {
		this.id = id;
		this.number = number;
		this.name = name;
	}
}
