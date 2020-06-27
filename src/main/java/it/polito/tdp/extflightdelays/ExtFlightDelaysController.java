/**
 * Sample Skeleton for 'ExtFlightDelays.fxml' Controller Class
 */

package it.polito.tdp.extflightdelays;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.extflightdelays.model.Adiacenza;
import it.polito.tdp.extflightdelays.model.Airport;
import it.polito.tdp.extflightdelays.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ExtFlightDelaysController {

	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML // fx:id="distanzaMinima"
    private TextField distanzaMinima; // Value injected by FXMLLoader

    @FXML // fx:id="btnAnalizza"
    private Button btnAnalizza; // Value injected by FXMLLoader

    @FXML // fx:id="cmbBoxAeroportoPartenza"
    private ComboBox<Airport> cmbBoxAeroportoPartenza; // Value injected by FXMLLoader

    @FXML // fx:id="btnAeroportiConnessi"
    private Button btnAeroportiConnessi; // Value injected by FXMLLoader

    @FXML // fx:id="numeroVoliTxtInput"
    private TextField numeroVoliTxtInput; // Value injected by FXMLLoader

    @FXML // fx:id="btnCercaItinerario"
    private Button btnCercaItinerario; // Value injected by FXMLLoader

    @FXML
    void doAnalizzaAeroporti(ActionEvent event) {
    	txtResult.clear();
    	Double miglia = null;
    	try {
    		miglia = Double.parseDouble(distanzaMinima.getText());
    	} catch(NumberFormatException e) {
    		txtResult.appendText("Inserire un numero valido!");
    		return;
    	}
    	
    	List<Airport> airports = this.model.buildGraph(miglia);
    	this.cmbBoxAeroportoPartenza.getItems().setAll(airports);
    }

    @FXML
    void doCalcolaAeroportiConnessi(ActionEvent event) {
    	txtResult.clear();
    	Airport source = this.cmbBoxAeroportoPartenza.getValue();
    	if(source == null) {
    		txtResult.appendText("Selezionare un aereporto di partenza");
    		return;
    	}
    	
    	List<Adiacenza> connessi = this.model.getConnessi(source);
    	txtResult.appendText("Aeroporti connessi a "+source+"\n\n");
    	for(Adiacenza a : connessi) {
    		txtResult.appendText(a.toString()+"\n");
    	}
    }

    @FXML
    void doCercaItinerario(ActionEvent event) {
    	txtResult.clear();
    	Airport source = this.cmbBoxAeroportoPartenza.getValue();
    	if(source == null) {
    		txtResult.appendText("Selezionare un aereporto di partenza");
    		return;
    	}
    	
    	Double miglia = null;
    	try {
    		miglia = Double.parseDouble(numeroVoliTxtInput.getText());
    	} catch(NumberFormatException e) {
    		txtResult.appendText("Inserire un numero valido!");
    		return;
    	}
    	
    	List<Airport> path = this.model.trovaPercorso(miglia, source);
    	txtResult.appendText(String.format("L'itenaria migliore con %.1f miglia permette di visitare %d città con %.1f miglia \n\n", 
    			miglia, path.size(), miglia-this.model.getMigliaBest()));
    	for(Airport a : path)
    		txtResult.appendText(a.toString()+"\n");
    	
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert distanzaMinima != null : "fx:id=\"distanzaMinima\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert btnAnalizza != null : "fx:id=\"btnAnalizza\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert cmbBoxAeroportoPartenza != null : "fx:id=\"cmbBoxAeroportoPartenza\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert btnAeroportiConnessi != null : "fx:id=\"btnAeroportiConnessi\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert numeroVoliTxtInput != null : "fx:id=\"numeroVoliTxtInput\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert btnCercaItinerario != null : "fx:id=\"btnCercaItinerario\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";

    }
    
    public void setModel(Model model) {
		this.model = model;
		
	}
}

