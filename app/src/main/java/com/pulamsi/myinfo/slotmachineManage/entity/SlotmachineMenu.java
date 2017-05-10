package com.pulamsi.myinfo.slotmachineManage.entity;


public class SlotmachineMenu {

	private Integer imageRID;
	private String content;
	private int tag;//标记位

	public Integer getImageRID() {
		return imageRID;
	}

	public SlotmachineMenu setImageRID(Integer imageRID) {
		this.imageRID = imageRID;
		return this;
	}

	public String getContent() {
		return content;
	}

	public SlotmachineMenu setContent(String content) {
		this.content = content;
		return this;
	}

	public int getTag() {
		return tag;
	}

	public SlotmachineMenu setTag(int tag) {
		this.tag = tag;
		return this;
	}
}
