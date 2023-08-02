package com.sohbet.domain;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.sohbet.enums.RoleType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Table(name="role")
@Entity
public class Role {
	
	@Id
	@GeneratedValue(strategy =  GenerationType.AUTO)
	private Integer id;      // 1- customer / 2 admin 
	
	@Enumerated(EnumType.STRING)
	@Column(length = 20)
	private RoleType type;

	@Override
	public String toString() {
		return "Role [type=" + type + "]";
	}
	
	
	
	

}