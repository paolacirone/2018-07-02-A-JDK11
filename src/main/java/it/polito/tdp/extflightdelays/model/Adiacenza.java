package it.polito.tdp.extflightdelays.model;

public class Adiacenza implements Comparable<Adiacenza> {
	
	private Airport airport1;
	private Airport airport2;
	private Double peso;
	
	public Adiacenza(Airport airport1, Airport airport2, Double peso) {
		super();
		this.airport1 = airport1;
		this.airport2 = airport2;
		this.peso = peso;
	}

	public Airport getAirport1() {
		return airport1;
	}

	public Airport getAirport2() {
		return airport2;
	}

	public Double getPeso() {
		return peso;
	}

	@Override
	public int compareTo(Adiacenza o) {
		return -this.peso.compareTo(o.peso);
	}
	
	public String toString() {
		return airport2 + " | " + peso;
	}

}
