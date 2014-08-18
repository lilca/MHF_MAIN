package jp.skd.lilca.mhf.main.kafus;

import java.util.Comparator;

public class LayoutComparator implements Comparator<Object> {

	@Override
	public int compare(Object obj1, Object obj2) {
		int num1;
		int num2;

		ListLayout lay1 = (ListLayout) obj1;
		ListLayout lay2 = (ListLayout) obj2;

		num1 = Integer.parseInt((String) lay1.getTextRight());
		num2 = Integer.parseInt((String) lay2.getTextRight());
		return num2 - num1;
	}

}