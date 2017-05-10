package com.pulamsi.myinfo.order.entity;

import java.io.Serializable;

/**
 * 模板实体类，用于存放付款方式，配送方式等
 */

public class MasterplateEntity implements Serializable {

	private static final long serialVersionUID = -1;

	private String id;// id
	private String name;//名字
	private String description;//备注

	public String getId() {
		return id;
	}

	public MasterplateEntity setId(String id) {
		this.id = id;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public MasterplateEntity setDescription(String description) {
		this.description = description;
		return this;
	}

	public String getName() {
		return name;
	}

	public MasterplateEntity setName(String name) {
		this.name = name;
		return this;
	}
}