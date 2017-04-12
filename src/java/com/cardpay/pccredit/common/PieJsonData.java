package com.cardpay.pccredit.common;

/**
 * @author chenzhifang
 *
 * 2014-12-22上午10:39:12
 */
public class PieJsonData {
	private String name;
	private double y;
	private boolean sliced;
	private boolean selected;
	
	private double q;
	
	public double getQ() {
		return q;
	}
	public void setQ(double q) {
		this.q = q;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	public boolean isSliced() {
		return sliced;
	}
	public void setSliced(boolean sliced) {
		this.sliced = sliced;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}
