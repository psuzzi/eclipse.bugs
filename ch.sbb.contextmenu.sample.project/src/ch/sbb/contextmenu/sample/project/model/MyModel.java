package ch.sbb.contextmenu.sample.project.model;

import java.util.ArrayList;

public class MyModel {
	public MyModel parent;

	public ArrayList child = new ArrayList();

	public int counter;

	public MyModel(int counter, MyModel parent) {
		this.parent = parent;
		this.counter = counter;
	}

	public String toString() {
		String rv = "Item ";
		if (parent != null) {
			rv = parent.toString() + ".";
		}

		rv += counter;
		return rv;
	}

	public int getCounter() {
		return this.counter;
	}
}
