package it.polito.tdp.extflightdelays.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.extflightdelays.db.ExtFlightDelaysDAO;

public class Model {
	
	private Graph<Airport, DefaultWeightedEdge> graph;
	private ExtFlightDelaysDAO dao;
	
	private Map<Integer, Airport> idMap;
	private List<Airport> bestPath;
	private Double migliaBest;
	
	
	public Model() {
		this.dao = new ExtFlightDelaysDAO();
	}
	
	public List<Airport> buildGraph(Double miglia) {
		this.graph = new SimpleWeightedGraph<Airport, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		this.idMap = new HashMap<Integer, Airport>();
		
		this.dao.loadAllAirports(idMap);
		
		for(Adiacenza a : this.dao.getAdiacenze(idMap, miglia)) {
			if(!this.graph.containsVertex(a.getAirport1())) {
				this.graph.addVertex(a.getAirport1());
			}
			if(!this.graph.containsVertex(a.getAirport2())) {
				this.graph.addVertex(a.getAirport2());
			}
			Graphs.addEdge(this.graph, a.getAirport1(), a.getAirport2(), a.getPeso());
		}
		
		List<Airport> list = new ArrayList<Airport>(this.graph.vertexSet());
		list.sort(null);
		return list;
	}
	
	public List<Adiacenza> getConnessi(Airport source) {
		List<Adiacenza> result = new ArrayList<Adiacenza>();
		for(Airport vicino : Graphs.neighborListOf(this.graph, source)) {
			result.add(new Adiacenza(source, vicino, this.graph.getEdgeWeight(this.graph.getEdge(source, vicino))));
		}
		
		result.sort(null);
		return result;
	}
	
	
	public List<Airport> trovaPercorso(Double migliaDisponibili, Airport source) {
		this.bestPath = new ArrayList<>();
		//this.bestPath.add(source);
		this.migliaBest = 0.0;
		List<Airport> parziale = new ArrayList<>();
		parziale.add(source);
		
		this.ricorsiva(parziale, migliaDisponibili, 1);
		
		return this.bestPath;
	}

	private void ricorsiva(List<Airport> parziale, Double migliaDisponibili, Integer livello) {
		Airport last = parziale.get(parziale.size()-1);
		
		if(livello > this.bestPath.size()) {
			this.bestPath = new ArrayList<>(parziale);
			this.migliaBest = migliaDisponibili;
		}
		
		if(livello == this.bestPath.size() && migliaDisponibili > this.migliaBest) {
			this.bestPath = new ArrayList<>(parziale);
			this.migliaBest = migliaDisponibili;
		}
	
		
		for(Airport vicino : Graphs.neighborListOf(this.graph, last)) {
			Double peso = this.graph.getEdgeWeight(this.graph.getEdge(last, vicino));
			if(!parziale.contains(vicino) && migliaDisponibili >= peso) {
				parziale.add(vicino);
				migliaDisponibili -= peso;
				this.ricorsiva(parziale, migliaDisponibili, livello++);
				
				parziale.remove(parziale.size()-1);
				migliaDisponibili += peso;
			}
		}
		
	}
	
	
	public Double getMigliaBest() {
		return this.migliaBest;
	}

}
