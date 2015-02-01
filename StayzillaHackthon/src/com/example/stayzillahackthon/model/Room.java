package com.example.stayzillahackthon.model;

public class Room {

	public String rid;
	public String rtype;
	public String rtariffDisp;
	public String rdiscount;
	public String rdiscountDisp;
	public String rdiscountPriceWithTax;
	public String roccupants;
	public String totalnoofrooms;
	public boolean isExtraBedType;
	public String withTax;
	public String tax;
	public String rate999;
	public String rate9;
	public boolean isICHRoom;
	
	
	@Override
	public String toString() {
		return "Room [rid=" + rid + ", rtype=" + rtype + ", rtariffDisp="
				+ rtariffDisp + ", rdiscount=" + rdiscount + ", rdiscountDisp="
				+ rdiscountDisp + ", rdiscountPriceWithTax="
				+ rdiscountPriceWithTax + ", roccupants=" + roccupants
				+ ", totalnoofrooms=" + totalnoofrooms + ", isExtraBedType="
				+ isExtraBedType + ", withTax=" + withTax + ", tax=" + tax
				+ ", rate999=" + rate999 + ", rate9=" + rate9 + ", isICHRoom="
				+ isICHRoom + "]";
	}

	
}
