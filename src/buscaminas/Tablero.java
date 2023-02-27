package buscaminas;

import java.util.ArrayList;
import java.util.function.Consumer;

public class Tablero {

	Casilla[][] casillas;
	int numFilas;
	int numColumnas;
	int numMinas;
	int numCasillasAbiertas;
	boolean juegoTerminado;
	
	
	Consumer<ArrayList<Casilla>> eventoPartidaPerdida;
	Consumer<ArrayList<Casilla>> eventoPartidaGanada;
	Consumer<Casilla> eventoCasillaAbierta;
	



	public Tablero(int numFilas, int numColumnas, int numMinas) {
		this.numFilas = numFilas;
		this.numColumnas = numColumnas;
		this.numMinas = numMinas;
		inicializarCasillas();
	}
	
	public void inicializarCasillas() {
		casillas = new Casilla[this.numFilas][this.numColumnas];
		for(int i = 0; i<casillas.length; i++) {
			for(int j = 0; j<casillas[i].length; j++) {
				casillas[i][j] = new Casilla(i, j);
			}
		}
		
		generarMinas();
	}
	
	private void generarMinas() {
		int minasGeneradas = 0;
		while(minasGeneradas != numMinas) {
			int posTmpFila = (int)(Math.random()*casillas.length);
			int posTmpColumna = (int)(Math.random()*casillas[0].length);
			if(!casillas[posTmpFila][posTmpColumna].isMina()) {
				casillas[posTmpFila][posTmpColumna].setMina(true);
				minasGeneradas++;
			}
		}
		
		actualizarNumeroMinasAlrededor();
	}
	
	private void actualizarNumeroMinasAlrededor() {
		for(int i = 0; i<casillas.length; i++) {
			for(int j = 0; j<casillas[i].length; j++) {
				if(casillas[i][j].isMina()) {
					ArrayList<Casilla> casillasAlrededor = obtenerCasillasAlrededor(i,j);
					for(Casilla c:casillasAlrededor) {
						c.incrementarNumeroMinasAlrededor();
					}
				}
			}
		}
	}
	
	private ArrayList<Casilla> obtenerCasillasAlrededor(int posFila, int posColumna){
		ArrayList<Casilla> listaCasillas = new ArrayList<>();
		for(int i = 0; i<8; i++) {
			int posTmpFila = posFila;
			int posTmpColumna = posColumna;
			switch(i) {
				case 0: posTmpFila--; break;
				case 1: posTmpFila--; posTmpColumna++; break;
				case 2: posTmpColumna++; break;
				case 3: posTmpFila++; posTmpColumna++; break;
				case 4: posTmpFila++;  break;
				case 5: posTmpColumna--; posTmpFila++; break;
				case 6: posTmpColumna--; break;
				case 7: posTmpFila--;posTmpColumna--;break;
				
			}
			
			if(posTmpFila >= 0 && posTmpFila<this.casillas.length && posTmpColumna >= 0 && posTmpColumna < this.casillas[0].length) {
				listaCasillas.add(casillas[posTmpFila][posTmpColumna]);
			}
			
		}
		return listaCasillas;
		
	}
	
	public ArrayList<Casilla> obtenerCasillasConMinas(){
		ArrayList<Casilla> casillasConMina = new ArrayList<>();
		for(int i = 0; i<casillas.length; i++) {
			for(int j = 0; j<casillas[i].length; j++) {
				if(casillas[i][j].isMina()) {
					casillasConMina.add(casillas[i][j]);
				}
			}
			
			
		}
		return casillasConMina;
		
	}

	public void seleccionarCasilla(int posFila, int posColumna) {
		eventoCasillaAbierta.accept(this.casillas[posFila][posColumna]);

		if(this.casillas[posFila][posColumna].isMina()) {
			eventoPartidaPerdida.accept(obtenerCasillasConMinas());

		}else if(this.casillas[posFila][posColumna].getNumMinasAlrededor() == 0) {
			marcarCasillaAbierta(posFila, posColumna);
			ArrayList<Casilla> casillasAlrededor = obtenerCasillasAlrededor(posFila, posColumna);
			for(Casilla c:casillasAlrededor) {
				if(!c.isAbierta()) {
					seleccionarCasilla(c.getPosFila(), c.getPosColumna());
					
				}
				
			}
		}else {
			marcarCasillaAbierta(posFila, posColumna);
		}
		
		if(partidaGanada()) {
			eventoPartidaGanada.accept(obtenerCasillasConMinas());
		}
	}
	
	public void marcarCasillaAbierta(int posFila, int posColumna) {
		if(!this.casillas[posFila][posColumna].isAbierta()) {
			numCasillasAbiertas++;
			this.casillas[posFila][posColumna].setAbierta(true);
			
		}
	}
	
	public boolean partidaGanada() {
		
		return numCasillasAbiertas>=(numFilas*numColumnas)-numMinas;
		
	}
	
	

	public void setEventoPartidaPerdida(Consumer<ArrayList<Casilla>> eventoPartidaPerdida) {
		this.eventoPartidaPerdida = eventoPartidaPerdida;
	}
	

	public void setEventoCasillaAbierta(Consumer<Casilla> eventoCasillaAbierta) {
		this.eventoCasillaAbierta = eventoCasillaAbierta;
	}
	
	
	public void setEventoPartidaGanada(Consumer<ArrayList<Casilla>> eventoPartidaGanada) {
		this.eventoPartidaGanada = eventoPartidaGanada;
	}

	void imprimirTablero() {
		for(int i = 0; i<casillas.length; i++) {
			for(int j = 0; j<casillas[i].length; j++) {
				if(casillas[i][j].isMina()) {
					System.out.print("*");
				}else {
					System.out.print("0");
				}
			}
			
			System.out.println();
		}
	}
	
	private void imprimirPistas() {
		for(int i = 0; i<casillas.length; i++) {
			for(int j = 0; j<casillas[i].length; j++) {
				System.out.print(casillas[i][j].getNumMinasAlrededor());
			}
			
			System.out.println();
		}
	}
	
	
	public static void main(String[] args) {
		Tablero t = new Tablero(6, 6, 10);
		t.imprimirTablero();
		System.out.println("-------");
		t.imprimirPistas();
	}
}
