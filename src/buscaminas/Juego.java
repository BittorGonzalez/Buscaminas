package buscaminas;

import java.awt.EventQueue;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;

public class Juego extends JFrame implements ActionListener {

	private static final long serialVersionUID = -3196529513457234315L;
	private JPanel contentPane;
	private static int[][] confPartida;
	private JButton[][]botonesTablero;
	private JMenuItem mntmNewMenuItem,  mntmNewMenuItem_1,mntmNewMenuItem_2 ;
	Tablero t;
	private JButton botonPartida;
	private JPanel campoJuego;
	
	int numFilas;
	int numColumnas;
	int numMinas;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		confPartida = new int[3][3];
	//Cargar valores para principiante
	confPartida[0][0] = 10;
	confPartida[0][1] = 10;
	confPartida[0][2] = 10;
	
	//Cargar valores para intermedio
	confPartida[1][0] =18;
	confPartida[1][1] = 22;
	confPartida[1][2] = 60;
	
	//Cargar valores para dificil
	confPartida[2][0] = 30;
	confPartida[2][1] = 35;
	confPartida[2][2] = 210;
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Juego frame = new Juego();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
					
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @throws IOException 
	 * @throws SecurityException 
	 */
	public Juego(){
		tamañoJuego(confPartida[0][0],confPartida[0][1],confPartida[0][2]);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, (numColumnas+2)*30, (numFilas+6)*30);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnNewMenu = new JMenu("Juego");
		menuBar.add(mnNewMenu);
		
		 mntmNewMenuItem = new JMenuItem("Principiante");
		mnNewMenu.add(mntmNewMenuItem);
		mntmNewMenuItem.addActionListener(this);
		
		 mntmNewMenuItem_1 = new JMenuItem("Medio");
		mnNewMenu.add(mntmNewMenuItem_1);
		mntmNewMenuItem_1.addActionListener(this);
		
		 mntmNewMenuItem_2 = new JMenuItem("Avanzado");
		mnNewMenu.add(mntmNewMenuItem_2);
		mntmNewMenuItem_2.addActionListener(this);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel menu = new JPanel();
		contentPane.add(menu, BorderLayout.NORTH);
		
		botonPartida = new JButton("");
		botonPartida.setIcon(new ImageIcon(Juego.class.getResource("/iconos/caraFeliz.PNG")));
		menu.add(botonPartida);
		botonPartida.addActionListener(this);
		 
		 campoJuego = new JPanel();
		 campoJuego.setLayout(null);
		 contentPane.add(campoJuego, BorderLayout.CENTER);
		juegoNuevo();
		
		
	
	}
	

	private void tamañoJuego(int numFilas, int numColumnas, int numMinas) {
		this.numFilas = numFilas;
		this.numColumnas = numColumnas;
		this.numMinas = numMinas;
		setBounds(100, 100, (numColumnas+2)*30, (numFilas+6)*30);
		
	}

	private void juegoNuevo() {

		botonPartida.setIcon(new ImageIcon(Juego.class.getResource("/iconos/caraFeliz.PNG")));
		eliminarBotones();
		cargarControles();
		crearTablero();
		repaint();
		
	}
	
	
	
	private void eliminarBotones() {
		if(botonesTablero != null) {
			for(int i = 0; i<botonesTablero.length; i++) {
				for(int j = 0; j<botonesTablero[i].length; j++) {
					if(botonesTablero[i][j]!=null) {
						campoJuego.remove(botonesTablero[i][j]);
					}
				}
			}
		}
		
	}

	private void crearTablero() {
		t = new Tablero(numFilas, numColumnas, numMinas);
		t.setEventoPartidaPerdida(new Consumer<ArrayList<Casilla>>() {

	
		
			@Override
			public void accept(ArrayList<Casilla> t) {
				Icon mina = new ImageIcon("src\\iconos\\mina.png");
				for(Casilla c: t) {
					botonesTablero[c.getPosFila()][c.getPosColumna()].setIcon(mina);
					
				}
				
				eliminarActionListener();
	
				botonPartida.setIcon(new ImageIcon(Juego.class.getResource("/iconos/caraMuerta.PNG")));
				
			}
			
		});
		
		t.setEventoPartidaGanada(new Consumer<ArrayList<Casilla>>() {

			@Override
			public void accept(ArrayList<Casilla> t) {
				eliminarActionListener();
				
				botonPartida.setIcon(new ImageIcon(Juego.class.getResource("/iconos/caraGanar.PNG")));
			}
			
		});
		t.setEventoCasillaAbierta(new Consumer<Casilla>() {

			@Override
			public void accept(Casilla t) {
				Icon num = new ImageIcon();
				switch(t.getNumMinasAlrededor()) {
					case 0:
						botonesTablero[t.getPosFila()][t.getPosColumna()].setText("");
						break;
					case 1:
						num = new ImageIcon("src\\iconos\\uno.png");
						botonesTablero[t.getPosFila()][t.getPosColumna()].setIcon(num);
						break;
					case 2:
						num = new ImageIcon("src\\iconos\\dos.png");
						botonesTablero[t.getPosFila()][t.getPosColumna()].setIcon(num);
						break;
					case 3:
						num = new ImageIcon("src\\iconos\\tres.png");
						botonesTablero[t.getPosFila()][t.getPosColumna()].setIcon(num);
						break;
					case 4:
						num = new ImageIcon("src\\iconos\\cuatro.png");
						botonesTablero[t.getPosFila()][t.getPosColumna()].setIcon(num);
						break;
					case 5:
						num = new ImageIcon("src\\iconos\\cinco.png");
						botonesTablero[t.getPosFila()][t.getPosColumna()].setIcon(num);
						break;
					case 6:
						num = new ImageIcon("src\\iconos\\seis.png");
						botonesTablero[t.getPosFila()][t.getPosColumna()].setIcon(num);
						break;
				}
				botonesTablero[t.getPosFila()][t.getPosColumna()].setEnabled(false);
			
				
			}
			
		});
		t.imprimirTablero();
	}

	public void cargarControles() {
		
		int posXreferencia = 17;
		int posYreferencia = 25;
		int ancho = 30;
		int alto = 30;
		botonesTablero = new JButton[numFilas][numColumnas];
		for(int i = 0; i<botonesTablero.length; i++) {
			for(int j = 0; j<botonesTablero[i].length; j++) {
				botonesTablero[i][j] = new JButton();
				botonesTablero[i][j].setName(i+","+j);
		
				
				if(i == 0 && j == 0) {
					botonesTablero[i][j].setBounds(posXreferencia, posYreferencia, ancho, alto);
				}else if(i == 0 && j != 0) {
					botonesTablero[i][j].setBounds(botonesTablero[i][j-1].getX()+botonesTablero[i][j-1].getWidth(), posYreferencia, ancho, alto);
				}else {
					botonesTablero[i][j].setBounds(botonesTablero[i-1][j].getX(), botonesTablero[i-1][j].getY()+botonesTablero[i-1][j].getHeight(), ancho, alto);
				}
				
				botonesTablero[i][j].addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						
						JButton btn = (JButton)e.getSource();
						String[] coordernada = btn.getName().split(",");
						int posFila = Integer.parseInt(coordernada[0]);
						int posColumna = Integer.parseInt(coordernada[1]);
						t.seleccionarCasilla(posFila, posColumna);
					}
					
				});
				campoJuego.add(botonesTablero[i][j]);
			}
			
			 
		}
	}
	
	public void eliminarActionListener() {
		ArrayList<JButton> listaAux = new ArrayList<JButton>();
		
		for(int i = 0; i<botonesTablero.length; i++) {
			for(int j = 0; j<botonesTablero[i].length; j++) {
				listaAux.add(botonesTablero[i][j]);
			}
		}
		
		for (JButton currentButton: listaAux) {
		    for (ActionListener al: currentButton.getActionListeners()) {
		        currentButton.removeActionListener(al);
		    }
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		Object o = e.getSource();
		
		if(o == botonPartida) {
			juegoNuevo();
		}else if(o == mntmNewMenuItem) {
			tamañoJuego(confPartida[0][0],confPartida[0][1],confPartida[0][2]);
			juegoNuevo();
			
		}else if(o ==  mntmNewMenuItem_1) {
			tamañoJuego(confPartida[1][0],confPartida[1][1],confPartida[1][2]);
			juegoNuevo();
			
		}else if(o == mntmNewMenuItem_2 ) {
			tamañoJuego(confPartida[2][0],confPartida[2][1],confPartida[2][2]);
			juegoNuevo();
		}
		
		
		
	}

	public int getNumFilas() {
		return numFilas;
	}

	public void setNumFilas(int numFilas) {
		this.numFilas = numFilas;
	}

	public int getNumColumnas() {
		return numColumnas;
	}

	public void setNumColumnas(int numColumnas) {
		this.numColumnas = numColumnas;
	}

	public int getNumMinas() {
		return numMinas;
	}

	public void setNumMinas(int numMinas) {
		this.numMinas = numMinas;
	}
	
	
}
