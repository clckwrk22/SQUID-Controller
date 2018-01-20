import java.awt.EventQueue;

import javax.swing.JFrame;

//import org.jfree.chart.ChartFactory;
//import org.jfree.chart.ChartPanel;
//import org.jfree.chart.JFreeChart;

//import org.jfree.data.xy.XYSeries;
//import org.jfree.data.xy.XYSeriesCollection;

import com.fazecast.jSerialComm.SerialPort;


import javax.swing.JButton;
import javax.swing.JComboBox;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
//import java.io.PrintWriter;
import java.io.Writer;
import java.util.Hashtable;
//import java.util.Scanner;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import java.awt.Font;
//import java.awt.Color;
//import java.awt.Container;

import javax.swing.JSlider;
//import javax.swing.SwingConstants;
import javax.swing.JTextField;

public class Squid {

	private JFrame window;
	static SerialPort dataPort; //A0 bei Arduino, nur Daten
	static SerialPort controlPort; //USB an Netbook, nur Kontrollsignale
	static int x = 0;
	private JTextField txtTuneVoltage;
	private JTextField txtHeaterActivationTime;
	private JTextField txtWaitTime;
	private JTextField txtOffsetValue;
	
	// SQUID Controller Application for Tristan iMC-303 LTS System
	// Controlled via Notebook and RS-232->USB Adapter
	// Copyright Paul Wagner 2017
	// Part of my research at the HLD Dresden 

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Squid window = new Squid();
					window.window.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	
	}
	

	/**
	 * Create the application.
	 */
	public Squid() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		window = new JFrame();
		window.setTitle("SQUID Controller");
		window.setBounds(200, 200, 780, 650);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.getContentPane().setLayout(null);
		
		
		JSlider sliderGain = new JSlider();
		sliderGain.setMinimum(1);
		sliderGain.setValue(1);
		sliderGain.setMaximum(9);
		sliderGain.setMajorTickSpacing(1);
		sliderGain.setPaintTicks(true);
		Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
		labelTable.put(new Integer(1), new JLabel("x1"));
		labelTable.put(new Integer(2), new JLabel("x2"));
		labelTable.put(new Integer(3), new JLabel("x5"));
		labelTable.put(new Integer(4), new JLabel("x10"));
		labelTable.put(new Integer(5), new JLabel("x20"));
		labelTable.put(new Integer(6), new JLabel("x50"));
		labelTable.put(new Integer(7), new JLabel("x100"));
		labelTable.put(new Integer(8), new JLabel("x200"));
		labelTable.put(new Integer(9), new JLabel("x500"));
		sliderGain.setLabelTable(labelTable);
		sliderGain.setPaintLabels(true);
		sliderGain.setOrientation(JSlider.VERTICAL);
		sliderGain.setBounds(431, 234, 200, 330);
		window.getContentPane().add(sliderGain);
		
		JButton btnHeater = new JButton("Heater ON");
		btnHeater.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				OutputStream heat = controlPort.getOutputStream();
				Writer heater = new BufferedWriter(new OutputStreamWriter(heat));
				String h="SHEAT;",w,t;
				w="SYSWAIT"+txtWaitTime.getText()+";";
				t="SYSHEAT"+txtHeaterActivationTime.getText()+";";
				try {
					heater.write(w,0,w.length());
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				try {
					heater.flush();
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				try {
					heater.write(t,0,t.length());
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				try {
					heater.flush();
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				try {
					heater.write(h,0,h.length());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					heater.flush();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					heater.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					heat.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		btnHeater.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnHeater.setBounds(27, 237, 109, 23);
		window.getContentPane().add(btnHeater);
		
		JButton btnTune = new JButton("Auto Tune");
		btnTune.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnTune.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {	
				String a,b="SISTATE3;";		
				a = "SYSVOLT" + txtTuneVoltage.getText()+";"; //eingabe der Tune Spannung, wird an iMC-303 gesendet
				OutputStream tune = controlPort.getOutputStream();
				Writer auto = new BufferedWriter(new OutputStreamWriter(tune));
				try {
				auto.write(a, 0, a.length());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally{
				try {
					auto.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				}
				
				
				
				try {
					auto.write(b, 0, b.length());
					auto.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
				
				try {
					auto.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					tune.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				}
			}
		});
		btnTune.setBounds(27, 305, 109, 23);
		window.getContentPane().add(btnTune);
		
		txtTuneVoltage = new JTextField();
		txtTuneVoltage.setBounds(146, 305, 112, 20);
		window.getContentPane().add(txtTuneVoltage);
		txtTuneVoltage.setColumns(10);
		
		JLabel lblAutoTuneVoltage = new JLabel("Auto Tune Voltage");
		lblAutoTuneVoltage.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblAutoTuneVoltage.setBounds(146, 280, 112, 23);
		window.getContentPane().add(lblAutoTuneVoltage);
		
		JLabel lblGain = new JLabel("Gain");
		lblGain.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblGain.setBounds(508, 562, 53, 14);
		window.getContentPane().add(lblGain);
		
		JLabel lblSlew = new JLabel("Slew Rate");
		lblSlew.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblSlew.setBounds(250, 115, 86, 14);
		window.getContentPane().add(lblSlew);
		
		JButton btnSlow = new JButton("Slow");
		btnSlow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String s="SLEW2,1;";
				OutputStream slow = controlPort.getOutputStream();
				Writer rate = new BufferedWriter(new OutputStreamWriter(slow));
				try {
					rate.write(s, 0, s.length());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					rate.flush();
					rate.close();
					slow.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 //langsame Slew Rate, nur bei Gain groesser x100
				
			}
		});
		btnSlow.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnSlow.setBounds(295, 141, 89, 23);
		window.getContentPane().add(btnSlow);
		
		JButton btnFast = new JButton("Normal");
		btnFast.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String s="SLEW2,0;";
				OutputStream slow = controlPort.getOutputStream();
				Writer rate = new BufferedWriter(new OutputStreamWriter(slow));
				try {
					rate.write(s, 0, s.length());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					rate.flush();
					rate.close();
					slow.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		btnFast.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnFast.setBounds(179, 141, 89, 23);
		window.getContentPane().add(btnFast);
		
		JComboBox<String> comboControlPort = new JComboBox<String>();
		comboControlPort.setBounds(92, 12, 111, 23);
		window.getContentPane().add(comboControlPort);
		
		JButton btnControl = new JButton("Connect");
		btnControl.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				// konfigurieren Connect Btn, nur fuer Steuerbefehle
				if (btnControl.getText().equals("Connect")) {
					controlPort = SerialPort.getCommPort(comboControlPort.getSelectedItem().toString());
					controlPort.openPort();// Versuchen zu verbinden
					controlPort.setComPortTimeouts(SerialPort.TIMEOUT_NONBLOCKING, 20, 20);
					controlPort.setComPortParameters(9600, 8, 1, 0);
					
					if(controlPort.isOpen()) {
						btnControl.setText("Disconnect");
						comboControlPort.setEnabled(false);
						String s="SLLOCK1;"; //auf remote betrieb umstellen
						OutputStream locking = controlPort.getOutputStream();
						Writer lock = new BufferedWriter(new OutputStreamWriter(locking));
						try {
							lock.write(s, 0, s.length());
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						try {
							lock.flush();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						try {
							lock.close();
							locking.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
					
				} else {
					String s="SLLOCK0;"; //auf local berieb umstellen
					OutputStream locking = controlPort.getOutputStream();
					Writer lock = new BufferedWriter(new OutputStreamWriter(locking));
					try {
						lock.write(s, 0, s.length());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						lock.flush();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						locking.close();
						lock.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					// Verbindung trennen
					controlPort.closePort();
				if (controlPort.isOpen() == false) {//pruefen ob wirklich geschlossen
					comboControlPort.setEnabled(true); 
					btnControl.setText("Connect");
					}
				
				}
				
			}
		});
		btnControl.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnControl.setBounds(198, 11, 111, 25);
		window.getContentPane().add(btnControl);
		

		
		JLabel lblControl = new JLabel("Control Port");
		lblControl.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblControl.setBounds(23, 11, 86, 20);
		window.getContentPane().add(lblControl);
		
		txtHeaterActivationTime = new JTextField();
		txtHeaterActivationTime.setBounds(146, 234, 110, 26);
		window.getContentPane().add(txtHeaterActivationTime);
		txtHeaterActivationTime.setColumns(10);
		
		JLabel lblHeaterActivationTime = new JLabel("Heater Activation Time");
		lblHeaterActivationTime.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		lblHeaterActivationTime.setBounds(146, 217, 145, 16);
		window.getContentPane().add(lblHeaterActivationTime);
		
		txtWaitTime = new JTextField();
		txtWaitTime.setBounds(295, 234, 110, 26);
		window.getContentPane().add(txtWaitTime);
		txtWaitTime.setColumns(10);
		
		JLabel lblWaitTimeAfter = new JLabel("Wait Time after heating");
		lblWaitTimeAfter.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		lblWaitTimeAfter.setBounds(297, 217, 163, 16);
		window.getContentPane().add(lblWaitTimeAfter);
		
		JButton btnRunMode = new JButton("Run Mode");
		btnRunMode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String r="SISTATE1;";
				OutputStream mode = controlPort.getOutputStream();
				Writer run = new BufferedWriter(new OutputStreamWriter(mode));
				try {
					run.write(r, 0, r.length());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					run.flush();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					run.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					mode.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnRunMode.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		btnRunMode.setBounds(27, 68, 109, 29);
		window.getContentPane().add(btnRunMode);
		
		JButton btnOffset = new JButton("Offset ");
		btnOffset.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		btnOffset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String o;
				o="SOFFSET2,"+txtOffsetValue.getText()+";";
				OutputStream offset = controlPort.getOutputStream();
				Writer value = new BufferedWriter(new OutputStreamWriter(offset));
				try {
					value.write(o,0,o.length());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					value.flush();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					value.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					offset.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnOffset.setBounds(27, 366, 109, 29);
		window.getContentPane().add(btnOffset);
		
		txtOffsetValue = new JTextField();
		txtOffsetValue.setBounds(146, 366, 112, 26);
		window.getContentPane().add(txtOffsetValue);
		txtOffsetValue.setColumns(10);
		
		JLabel lblOffsetValue = new JLabel("Offset Value(-100% to +100%)");
		lblOffsetValue.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		lblOffsetValue.setBounds(148, 350, 188, 16);
		window.getContentPane().add(lblOffsetValue);
		
		JButton btnSetGain = new JButton("Set Gain");
		btnSetGain.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String g;
				g="SGAIN2,"+String.valueOf(sliderGain.getValue())+";";
				OutputStream Gain = controlPort.getOutputStream();
				Writer setGain = new BufferedWriter(new OutputStreamWriter(Gain));
				try {
					setGain.write(g,0,g.length());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					setGain.flush();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					setGain.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					Gain.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnSetGain.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		btnSetGain.setBounds(590, 300, 109, 29);
		window.getContentPane().add(btnSetGain);
		
		String [] HighPass = {"DC","0.3 Hz"};
		JComboBox<String> comboHighPass = new JComboBox<String>(HighPass);
		comboHighPass.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		//comboHighPass.setSelectedIndex(1);
		comboHighPass.setBounds(146, 435, 112, 27);
		window.getContentPane().add(comboHighPass);
		
		String [] LowPass = {"Off","500 Hz", "5000 Hz", "5 Hz"};
		JComboBox<String> comboLowPass = new JComboBox<String>(LowPass); 
		//comboLowPass.setSelectedIndex();
		comboLowPass.setBounds(146, 493, 112, 27);
		window.getContentPane().add(comboLowPass);
		
		JButton btnSetHighPass = new JButton("Set High Pass");
		btnSetHighPass.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int a=comboHighPass.getSelectedIndex();
				String h="SHPF2,"+String.valueOf(a)+";";
				OutputStream Filter = controlPort.getOutputStream();
				Writer HighPass = new BufferedWriter(new OutputStreamWriter(Filter));
				try {
					HighPass.write(h,0,h.length());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					HighPass.flush();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					HighPass.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					Filter.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnSetHighPass.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		btnSetHighPass.setBounds(27, 434, 109, 29);
		window.getContentPane().add(btnSetHighPass);
		
		JButton btnSetLowPass = new JButton("Set Low Pass");
		btnSetLowPass.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int a=comboLowPass.getSelectedIndex();
				String h="SLPF2,"+String.valueOf(a)+";";
				OutputStream Filter = controlPort.getOutputStream();
				Writer LowPass = new BufferedWriter(new OutputStreamWriter(Filter));
				try {
					LowPass.write(h,0,h.length());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					LowPass.flush();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					LowPass.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					Filter.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnSetLowPass.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		btnSetLowPass.setBounds(27, 492, 109, 29);
		window.getContentPane().add(btnSetLowPass);
		
		SerialPort[] ports = SerialPort.getCommPorts();
		for (int k = 0; k < ports.length; k++)
			comboControlPort.addItem(ports[k].getSystemPortName()); 

			
	}
}
		
		
			
		
		

		
		
	

