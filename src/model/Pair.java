package model;

public class Pair<E,T> {
	E first;
	T second;
	public Pair(E first, T second) {
		this.first = first;
		this.second = second;
	}

	public E getFirst() {
		return first;
	}

	public void setFirst(E first) {
		this.first = first;
	}

	public T getSecond() {
		return second;
	}

	@Override
	public String toString() {
		return "{"+first+","+second.toString()+"}";
	}

	public void setSecond(T second) {
		this.second = second;
	}
}
