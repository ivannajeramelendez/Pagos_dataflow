package es.espai.windowbuilder.Referenciados;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.SimpleDateFormat;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.jdatepicker.DateModel;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Ejemplo {
	private static JFrame frame;

	private JFileChooser fileChooser;

	private String fileName;

	private List<Pago> pagos;

	private static JSONObject archivo;

	private JDatePickerImpl datePicker;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Ejemplo window = new Ejemplo();
					Ejemplo.frame.setVisible(true);
					try {
						Ejemplo.archivo = Archivo.inicializar();
					} catch (IOException e2) {
						e2.printStackTrace();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Ejemplo() {
		initialize();
	}

	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 550, 500);
		frame.setDefaultCloseOperation(3);
		ImageIcon img = new ImageIcon("codigo.png");
		frame.setIconImage(img.getImage());
		frame.getContentPane().setLayout((LayoutManager)null);
		JTabbedPane tabbedPane = new JTabbedPane(1);
		tabbedPane.setBounds(0, 0, 534, 461);
		frame.getContentPane().add(tabbedPane);
		final JPanel panelScroll = new JPanel();
		final JPanel panelScroll2 = new JPanel();
		final JPanel panelScroll3 = new JPanel();
		JPanel tarjetasDebito = new JPanel();

		// DOMICILIACION
		tabbedPane.addTab("Domiciliados/ CLABE", tarjetasDebito);
		tarjetasDebito.setLayout(new BorderLayout(0, 0));
		final JPanel panelSuperiorDebito = new JPanel();
		tarjetasDebito.add(panelSuperiorDebito, "North");
		JButton btnNewButton = new JButton("Importar domiciliados");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panelScroll.removeAll();
				Ejemplo.frame.setVisible(true);
				Ejemplo.this.fileChooser = new JFileChooser();
				FileNameExtensionFilter filtroImagen = new FileNameExtensionFilter("txt", new String[] { "txt", "rep" });
				Ejemplo.this.fileChooser.setFileFilter(filtroImagen);
				try {
					int returnVal = Ejemplo.this.fileChooser.showOpenDialog((Component)e.getSource());
					if (returnVal == 0) {
						File file = Ejemplo.this.fileChooser.getSelectedFile();
						try {
							Ejemplo.this.fileName = file.toString();
						} catch (Exception ex) {
							System.out.println("problem accessing file" + file.getAbsolutePath());
						}
					} else {
						System.out.println("File access cancelled by user.");
					}
					BufferedReader br = null;
					try {
						br = new BufferedReader(new FileReader(Ejemplo.this.fileName));
						String line = br.readLine();
						line = br.readLine();
						line = br.readLine();
						line = br.readLine();
						Ejemplo.this.pagos = new ArrayList();
						JLabel titulos = new JLabel();
						titulos.setText("Id de Cliente     -     Membresia     -     Nombre del Cliente     -     Importe");
						titulos.setAlignmentX(0.5F);
						panelScroll.add(Box.createRigidArea(new Dimension(0, 10)));
						panelScroll.add(titulos);
						while (line != null) {
							line.length();
							try {
								if (line.length() > 50) {
									Pago pago = new Pago();
									pago.setNoReferencia(line.substring(73, 99).trim());
									pago.setImporte(line.substring(16, 32).trim());
									pago.setNoMembresia(String.valueOf(String.valueOf(line.substring(line.substring(99).indexOf("0") + 127, line.substring(99).indexOf("0") + 135).trim())) + "00");
									String resp = Ejemplo.connectApi("http://192.168.20.102:8090/alpha/clienteByMembresia/" + pago.getNoMembresia());
									JSONObject cliente = new JSONObject(resp);
									pago.setIdCliente(cliente.getInt("idCliente"));
									pago.setNombreTitular(cliente.getString("nombreCompleto"));
									pago.setText(String.valueOf(String.valueOf(pago.getIdCliente())) + "   -   " + pago.getNoMembresia() + "   -   " + pago.getNombreTitular() + "   -   " + pago.getImporte());
									Ejemplo.this.pagos.add(pago);
									pago.setAlignmentX(0.5F);
									panelScroll.add(Box.createRigidArea(new Dimension(0, 10)));
									panelScroll.add(pago);
									UtilDateModel model = new UtilDateModel();
									Properties p = new Properties();
									p.put("text.today", "Today");
									p.put("text.month", "Month");
									p.put("text.year", "Year");
									JDatePanelImpl datePanel = new JDatePanelImpl((DateModel)model, p);
									Ejemplo.this.datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
									panelSuperiorDebito.add(Box.createRigidArea(new Dimension(0, 10)));
									panelSuperiorDebito.add((Component)Ejemplo.this.datePicker);
								}
							} catch (Exception exception) {}
							line = br.readLine();
						}
						br.close();
					} catch (Exception ex1) {
						ex1.printStackTrace();
					} finally {
						if (br != null)
							try {
								br.close();
							} catch (IOException ex2) {
								ex2.printStackTrace();
							}
					}
					JButton procesarPago = new JButton("Procesar Pago");
					procesarPago.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							int dialogButton = 0;
							int dialogResult = JOptionPane.showConfirmDialog(Ejemplo.frame, "seguro de aplicar el pago en DataFlow?", "Aplicar Pago", dialogButton);
							if (dialogResult == 0) {
								String selectedDate = DateLabelFormatter.fecha;
								System.out.println(selectedDate);
								if (selectedDate != null) {
									Ejemplo.aplicarPagoDomiciliacion(pagos, selectedDate);
								} else {
									JOptionPane.showMessageDialog(Ejemplo.frame, "No se ha seleccionado una fecha", "Error",
											2);
								}
							}
						}
					});
					JLabel total = new JLabel();
					float importeTotal = 0.0F;
					for (int i = 0; i < Ejemplo.this.pagos.size(); i++)
						importeTotal += Float.parseFloat(((Pago)Ejemplo.this.pagos.get(i)).getImporte().trim());
					total.setText("                                   Importe Total: $" + importeTotal);
					JLabel totalUsuarios = new JLabel();
					totalUsuarios.setText("                                   Total usuarios: " + Ejemplo.this.pagos.size());
					total.setAlignmentX(-0.0F);
					panelScroll.add(Box.createRigidArea(new Dimension(0, 10)));
					panelScroll.add(total);
					totalUsuarios.setAlignmentX(0.0F);
					panelScroll.add(Box.createRigidArea(new Dimension(0, 10)));
					panelScroll.add(totalUsuarios);
					procesarPago.setAlignmentX(0.5F);
					panelScroll.add(Box.createRigidArea(new Dimension(0, 10)));
					panelScroll.add(procesarPago);
					Ejemplo.frame.pack();
					Ejemplo.frame.setBounds(100, 100, 550, 500);
					Ejemplo.frame.setVisible(true);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		panelSuperiorDebito.add(btnNewButton);
		panelScroll.setLayout(new BoxLayout(panelScroll, 1));
		JScrollPane panelPane = new JScrollPane(panelScroll);
		tarjetasDebito.add(panelPane);
		JPanel referenciados = new JPanel();

		// REFERENCIADO
		tabbedPane.addTab("Referenciados", referenciados);
		referenciados.setLayout(new BorderLayout(0, 0));
		final JPanel panelSuperiorRef = new JPanel();
		referenciados.add(panelSuperiorRef, "North");
		JButton btnNewButton_1 = new JButton("Importar Referenciados");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panelScroll2.removeAll();
				Ejemplo.frame.setVisible(true);
				Ejemplo.this.fileChooser = new JFileChooser();
				FileNameExtensionFilter filtroImagen = new FileNameExtensionFilter("txt", new String[] { "txt" });
				Ejemplo.this.fileChooser.setFileFilter(filtroImagen);
				try {
					int returnVal = Ejemplo.this.fileChooser.showOpenDialog((Component)e.getSource());
					if (returnVal == 0) {
						File file = Ejemplo.this.fileChooser.getSelectedFile();
						try {
							Ejemplo.this.fileName = file.toString();
						} catch (Exception ex) {
							System.out.println("problem accessing file" + file.getAbsolutePath());
						}
					} else {
						System.out.println("File access cancelled by user.");
					}
					BufferedReader br = null;
					try {
						br = new BufferedReader(new FileReader(Ejemplo.this.fileName));
						String line = br.readLine();
						Ejemplo.this.pagos = new ArrayList();
						JLabel titulos = new JLabel();
						titulos.setText("Id de Cliente     -     Membresia     -     Nombre del Cliente     -     Importe");
						titulos.setAlignmentX(0.5F);
						panelScroll2.add(Box.createRigidArea(new Dimension(0, 10)));
						panelScroll2.add(titulos);
						while (line != null) {
							String[] fields = line.split("\t");
							try {
								boolean subcadena = line.contains("Ordenante");
								boolean subcadena2 = line.contains("Titular");
								if (subcadena) {
									Pago pago = new Pago();
									pago.setNoReferencia(line.substring(line.indexOf("Referencia") + 11, line.indexOf("Hora") - 1));
									pago.setImporte(fields[5]);
									pago.setFechaPago(fields[0]);
									pago.setNoMembresia(String.valueOf(String.valueOf(line.substring(line.indexOf("Concepto del Pago") + 19, line.indexOf("Concepto del Pago") + 27))) + "00");
									String resp = Ejemplo.connectApi("http://192.168.20.102:8090/alpha/clienteByMembresia/" + pago.getNoMembresia());
									JSONObject cliente = new JSONObject(resp);
									pago.setIdCliente(cliente.getInt("idCliente"));
									pago.setNombreTitular(cliente.getString("nombreCompleto"));
									pago.setText(String.valueOf(String.valueOf(pago.getIdCliente())) + "   -   " + pago.getNoMembresia() + "   -   " + pago.getNombreTitular() + "   -   " + pago.getImporte());
									Ejemplo.this.pagos.add(pago);
									pago.setAlignmentX(0.5F);
									panelScroll2.add(Box.createRigidArea(new Dimension(0, 10)));
									panelScroll2.add(pago);
									UtilDateModel model = new UtilDateModel();
									Properties p = new Properties();
									p.put("text.today", "Today");
									p.put("text.month", "Month");
									p.put("text.year", "Year");
									JDatePanelImpl datePanel = new JDatePanelImpl((DateModel)model, p);
									Ejemplo.this.datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
									panelSuperiorRef.add(Box.createRigidArea(new Dimension(0, 10)));
									panelSuperiorRef.add((Component)Ejemplo.this.datePicker);
								} else if (subcadena2) {
									Pago pago = new Pago();
									pago.setNoReferencia(fields[1]);
									pago.setImporte(fields[5]);
									pago.setFechaPago(fields[0]);
									pago.setNoMembresia(String.valueOf(String.valueOf(line.substring(line.indexOf("Ref3:") + 5, line.indexOf("Ref3:") + 13))) + "00");
									String resp = Ejemplo.connectApi("http://192.168.20.102:8090/alpha/clienteByMembresia/" + pago.getNoMembresia());
									JSONObject cliente = new JSONObject(resp);
									pago.setIdCliente(cliente.getInt("idCliente"));
									pago.setNombreTitular(cliente.getString("nombreCompleto"));
									pago.setText(String.valueOf(String.valueOf(pago.getIdCliente())) + "   -   " + pago.getNoMembresia() + "   -   " + pago.getNombreTitular() + "   -   " + pago.getImporte());
									Ejemplo.this.pagos.add(pago);
									pago.setAlignmentX(0.5F);
									panelScroll2.add(Box.createRigidArea(new Dimension(0, 10)));
									panelScroll2.add(pago);
									UtilDateModel model = new UtilDateModel();
									Properties p = new Properties();
									p.put("text.today", "Today");
									p.put("text.month", "Month");
									p.put("text.year", "Year");
									JDatePanelImpl datePanel = new JDatePanelImpl((DateModel)model, p);
									Ejemplo.this.datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
									panelSuperiorRef.add(Box.createRigidArea(new Dimension(0, 10)));
									panelSuperiorRef.add((Component)Ejemplo.this.datePicker);
								}
							} catch (Exception exception) {}
							line = br.readLine();
						}
						br.close();
					} catch (Exception ex1) {
						ex1.printStackTrace();
					} finally {
						if (br != null)
							try {
								br.close();
							} catch (IOException ex2) {
								ex2.printStackTrace();
							}
					}
					JButton procesarPago = new JButton("Procesar Pago");
					procesarPago.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							int dialogButton = 0;
							int dialogResult = JOptionPane.showConfirmDialog(Ejemplo.frame, "¿Se aplicaran los pagos en DataFlow?", "Aplicar Pago", dialogButton);
							if (dialogResult == 0) {
								String selectedDate = DateLabelFormatter.fecha;
								System.out.println(selectedDate);
								if (selectedDate != null) {
									Ejemplo.aplicarPago(pagos, selectedDate);
								} else {
									JOptionPane.showMessageDialog(Ejemplo.frame, "No se ha seleccionado una fecha", "Error",
											2);
								}
							}
						}
					});
					JLabel total = new JLabel();
					float importeTotal = 0.0F;
					for (int i = 0; i < Ejemplo.this.pagos.size(); i++)
						importeTotal += Float.parseFloat(((Pago)Ejemplo.this.pagos.get(i)).getImporte().trim());
					total.setText("                                   Importe Total: $" + importeTotal);
					JLabel totalUsuarios = new JLabel();
					totalUsuarios.setText("                                   Usuarios Totales: " + Ejemplo.this.pagos.size());
					total.setAlignmentX(0.0F);
					panelScroll2.add(Box.createRigidArea(new Dimension(0, 10)));
					panelScroll2.add(total);
					totalUsuarios.setAlignmentX(0.0F);
					panelScroll2.add(Box.createRigidArea(new Dimension(0, 10)));
					panelScroll2.add(totalUsuarios);
					procesarPago.setAlignmentX(0.5F);
					panelScroll2.add(Box.createRigidArea(new Dimension(0, 10)));
					panelScroll2.add(procesarPago);
					Ejemplo.frame.pack();
					Ejemplo.frame.setBounds(100, 100, 550, 500);
					Ejemplo.frame.setVisible(true);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		panelSuperiorRef.add(btnNewButton_1);
		panelScroll2.setLayout(new BoxLayout(panelScroll2, 1));
		JScrollPane scrollPane_1 = new JScrollPane(panelScroll2);
		referenciados.add(scrollPane_1);
		JPanel tarjetasCredito = new JPanel();
		tarjetasCredito.setLayout(new BorderLayout(0, 0));

		// DOMICILIACION TD - TC
		tabbedPane.addTab("Domiciliados/ TD - TC", tarjetasCredito);
		final JPanel panelSuperiorCredito = new JPanel();
		tarjetasCredito.add(panelSuperiorCredito, "North");
		JButton btnNewButton_2 = new JButton("Importar Domiciliados");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panelScroll3.removeAll();
				Ejemplo.frame.setVisible(true);
				Ejemplo.this.fileChooser = new JFileChooser();
				FileNameExtensionFilter filtroImagen = new FileNameExtensionFilter("rep", new String[] { "rep" });
				Ejemplo.this.fileChooser.setFileFilter(filtroImagen);
				try {
					int returnVal = Ejemplo.this.fileChooser.showOpenDialog((Component)e.getSource());
					if (returnVal == 0) {
						File file = Ejemplo.this.fileChooser.getSelectedFile();
						try {
							Ejemplo.this.fileName = file.toString();
						} catch (Exception ex) {
							System.out.println("problem accessing file" + file.getAbsolutePath());
						}
					} else {
						System.out.println("File access cancelled by user.");
					}
					BufferedReader br = null;
					try {
						br = new BufferedReader(new FileReader(Ejemplo.this.fileName));
						String line = br.readLine();
						Ejemplo.this.pagos = new ArrayList();
						JLabel titulos = new JLabel();
						titulos.setText("Id de Cliente     -     Membresia     -     Nombre del Cliente     -     Importe");
						titulos.setAlignmentX(0.5F);
						panelScroll3.add(Box.createRigidArea(new Dimension(0, 10)));
						panelScroll3.add(titulos);
						while (line != null) {
							try {
								if (line.toLowerCase().contains("transacciones aceptadas")) {
									line = br.readLine();
									line = br.readLine();
									line = br.readLine();
									line = br.readLine();
									line = br.readLine();
									line = br.readLine();
									line = br.readLine();
									line = br.readLine();
									line = br.readLine();
									do {
										line = br.readLine();
										System.out.println(line);
										Pago pago = new Pago();
										pago.setNoReferencia(line.substring(131, 151).trim());
										pago.setImporte(line.substring(100, 125).trim().replace(",", ""));
										pago.setNoMembresia(String.valueOf(String.valueOf(line.substring(20, 28).trim())) + "00");
										String resp = Ejemplo.connectApi("http://192.168.20.102:8090/alpha/clienteByMembresia/" + pago.getNoMembresia());
										JSONObject cliente = new JSONObject(resp);
										pago.setIdCliente(cliente.getInt("idCliente"));
										pago.setNombreTitular(cliente.getString("nombreCompleto"));
										pago.setText(String.valueOf(String.valueOf(pago.getIdCliente())) + "   -   " + pago.getNoMembresia() + "   -   " + pago.getNombreTitular() + "   -   " + pago.getImporte());
										Ejemplo.this.pagos.add(pago);
										pago.setAlignmentX(0.5F);
										panelScroll3.add(Box.createRigidArea(new Dimension(0, 10)));
										panelScroll3.add(pago);
										UtilDateModel model = new UtilDateModel();
										Properties p = new Properties();
										p.put("text.today", "Today");
										p.put("text.month", "Month");
										p.put("text.year", "Year");
										JDatePanelImpl datePanel = new JDatePanelImpl((DateModel)model, p);
										Ejemplo.this.datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
										panelSuperiorCredito.add(Box.createRigidArea(new Dimension(0, 10)));
										panelSuperiorCredito.add((Component)Ejemplo.this.datePicker);
										System.out.println(line.toLowerCase().contains("totales"));
									} while (!line.toLowerCase().contains("totales"));
									break;
								}
							} catch (Exception exception) {
								exception.printStackTrace();
							}
							line = br.readLine();
						}
						br.close();
					} catch (Exception ex1) {
						ex1.printStackTrace();
					} finally {
						if (br != null)
							try {
								br.close();
							} catch (IOException ex2) {
								ex2.printStackTrace();
							}
					}
					JButton procesarPago = new JButton("Procesar Pago");
					procesarPago.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							int dialogButton = 0;
							int dialogResult = JOptionPane.showConfirmDialog(Ejemplo.frame, "seguro de aplicar el pago en DataFlow?", "Aplicar Pago", dialogButton);
							if (dialogResult == 0) {
								String selectedDate = DateLabelFormatter.fecha;
								System.out.println(selectedDate);
								if (selectedDate != null) {
									Ejemplo.aplicarPagoDomiciliacion(pagos, selectedDate);
								} else {
									JOptionPane.showMessageDialog(Ejemplo.frame, "No se ha seleccionado una fecha", "Error",
											2);
								}
							}
						}
					});
					JLabel total = new JLabel();
					float importeTotal = 0.0F;
					for (int i = 0; i < Ejemplo.this.pagos.size(); i++)
						importeTotal += Float.parseFloat(((Pago)Ejemplo.this.pagos.get(i)).getImporte().trim());
					total.setText("                                   Importe Total: $" + importeTotal);
					JLabel totalUsuarios = new JLabel();
					totalUsuarios.setText("                                   Total usuarios: " + Ejemplo.this.pagos.size());
					total.setAlignmentX(0.0F);
					panelScroll3.add(Box.createRigidArea(new Dimension(0, 10)));
					panelScroll3.add(total);
					totalUsuarios.setAlignmentX(0.0F);
					panelScroll3.add(Box.createRigidArea(new Dimension(0, 10)));
					panelScroll3.add(totalUsuarios);
					procesarPago.setAlignmentX(0.5F);
					panelScroll3.add(Box.createRigidArea(new Dimension(0, 10)));
					panelScroll3.add(procesarPago);
					Ejemplo.frame.pack();
					Ejemplo.frame.setBounds(100, 100, 550, 500);
					Ejemplo.frame.setVisible(true);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		panelSuperiorCredito.add(btnNewButton_2);
		panelScroll3.setLayout(new BoxLayout(panelScroll3, 1));
		JScrollPane scrollPane = new JScrollPane(panelScroll3);
		tarjetasCredito.add(scrollPane);
		JPanel panel = new JPanel();
		tabbedPane.addTab("Crear Archivo", panel);
		JButton btnNewButton_3 = new JButton("Generar Archivo Domiciliados");
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String resp = Ejemplo.connectApi("http://192.168.20.102:8090/alpha/generarArchivoDomiciliacion");
					JSONArray lista = new JSONArray(resp);
					File myObj = new File("Domiciliados.txt");
					myObj.createNewFile();
					FileWriter myWriter = new FileWriter("Domiciliados.txt");
					for (int i = 0; i < lista.length(); i++) {
						JSONObject element = lista.getJSONObject(i);
						System.out.println(element.toString());
						myWriter.write(String.valueOf(String.valueOf(element.getString("referencia"))) + "|" + element.getString("nombre") + "|" +
								element.getString("paterno") + "|" + element.getString("materno") + "|" +
								element.getString("cuenta") + "|" + element.getString("inicio") + "|" +
								element.getString("fin") + "|" + element.getString("vencimiento") + "|" +
								element.getFloat("monto") + "|" + element.getString("contrato") + "\n");
					}
					myWriter.close();
					JOptionPane.showMessageDialog(Ejemplo.frame, "Archivo Domiciliados.txt generado exitosamente");
				} catch (JSONException e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(Ejemplo.frame, "No se encontro lista de domiciliados", "Error",
							2);
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(Ejemplo.frame, "Fallo al crear archivo Domiciliados.txt", "Error",
							2);
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(Ejemplo.frame, "Ha sucedido un error durante el proceso de generacion del archivo", "Error",
							2);
				}
			}
		});
		panel.add(btnNewButton_3);
	}

	public static String connectApi(String endpoint) {
		HttpRequest request1 = HttpRequest.newBuilder().uri(
						URI.create(endpoint))
				.header("Content-Type", "application/json")
				.GET().build();
		CompletableFuture<String> client = HttpClient.newHttpClient().<String>sendAsync(request1, HttpResponse.BodyHandlers.ofString())
				.thenApply(HttpResponse::body);
		String json = "";
		try {
			json = String.valueOf(client.get());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return json;
	}

	public static JSONObject connectApi(String endpoint, String body) {
		HttpRequest request1 = HttpRequest.newBuilder().uri(
						URI.create(endpoint))
				.header("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString(body)).build();
		CompletableFuture<String> client = HttpClient.newHttpClient().<String>sendAsync(request1, HttpResponse.BodyHandlers.ofString())
				.thenApply(HttpResponse::body);
		String json = "";
		JSONObject resp = null;
		try {
			json = String.valueOf(client.get());
			if (!json.equals(""))
				resp = new JSONObject(json);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// RESPUESTA DATAFLOW REFERENCIADOS
	public static void aplicarPago(List<Pago> pagos, String selectedDate) {
		try {
			List<Pago> pagosAux = new ArrayList<>();
			for (int i = 0; i < pagos.size(); i++) {
				Float sum = Float.valueOf(0.0F);
				int idCliente = ((Pago)pagos.get(i)).getIdCliente();
				sum = Float.valueOf(sum.floatValue() + Float.parseFloat(((Pago)pagos.get(i)).getImporte()));
				for (int k = i + 1; k < pagos.size(); k++) {
					int idClienteCom = ((Pago)pagos.get(k)).getIdCliente();
					if (idCliente == idClienteCom && idClienteCom != 0) {
						sum = Float.valueOf(sum.floatValue() + Float.parseFloat(((Pago)pagos.get(k)).getImporte()));
						((Pago)pagos.get(k)).setIdCliente(0);
					}
				}
				if (idCliente != 0) {
					Pago pagoAux = pagos.get(i);
					pagoAux.setImporte(sum.toString());
					pagosAux.add(pagoAux);
				}
			}
			pagos = pagosAux;
			System.out.println("Pagos: "+ pagos);
			File myObj = new File("Respuesta_dataflow.txt");
			myObj.createNewFile();
			FileWriter myWriter = new FileWriter("Respuesta_dataflow.txt");
			System.out.println("FileWriter: "+ pagos.size());
			myWriter.write("Fecha del Movimiento,Id de cliente, Membresia,Nombre de titular,Folio Interbancario,Fecha de aplicacion,Monto, Pago Aplicado,Observaciones\n");
			for (int j = 0; j < pagos.size(); j++) {
				String noReferenciaVal = ((Pago)pagos.get(j)).getNoReferencia();
				ObjectMapper mapper = new ObjectMapper();
				String jsonString = mapper.writeValueAsString(new Pago2(((Pago)pagos.get(j)).getIdCliente(), selectedDate, ((Pago)pagos.get(j)).getNoReferencia(), ((Pago)pagos.get(j)).getImporte(),((Pago)pagos.get(j)).getNoReferencia(), ((Pago)pagos.get(j)).getNoMembresia(), ((Pago)pagos.get(j)).getNombreTitular(), archivo.getString("nombre")));
				JSONObject respuesta = connectApi(String.valueOf(String.valueOf(archivo.getString("endpoint2"))) + "alpha/referencia", jsonString);
				if (respuesta.getString("respuesta").equals("el pago no ha sido aplicado")) {
					double importeVal = Double.parseDouble(((Pago)pagos.get(j)).getImporte());
					int idClienteVal = ((Pago)pagos.get(j)).getIdCliente();
					String fechaDelPago_ = (((Pago)pagos.get(j)).getFechaPago());
					String anio = fechaDelPago_.substring(0, 4);
					String mes = fechaDelPago_.substring(4, 6);
					String dia = fechaDelPago_.substring(6, 8);
					String fechaDelPago = anio + "-" + mes + "-" + dia;
					String membresia = ((Pago)pagos.get(j)).getNoMembresia();
					String nombreTitularVal = ((Pago)pagos.get(j)).getNombreTitular();
					String body = "{\r\n\"IdCliente\":" + idClienteVal + ",\r\n" + "\"Token\":\"77D5BDD4-1FEE-4A47-86A0-1E7D19EE1C74\"\r\n" + "}";
					try {
						JSONObject resp = connectApi(String.valueOf(String.valueOf(archivo.getString("endpoint"))) + "ServiciosClubAlpha/api/Pagos/GetPedidoByCliente", body);
						SimpleDateFormat formatter2 = new SimpleDateFormat("HH:mm:ss.SSS");
						String[] fechas = selectedDate.split("-");
						if (resp != null) {
							int noPedido = resp.getInt("NoPedido");
							String bodyPago = "{\r\n\"NoPedido\":" +
									noPedido + ",\r\n" +
									"\"Monto\":" + importeVal + ",\r\n" +
									"\"Notarjeta\":\"1111\",\r\n" +
									"\"FolioInterbancario\":\"" + noReferenciaVal + "\",\r\n" +
									"\"NoAutorizacion\":\"" + noReferenciaVal + "\",\r\n" +
									"\"FechaPago\":\"" + selectedDate + "\",\r\n" +
									"\"HoraPago\":\"" + formatter2.format(new Date()) + "\",\r\n" +
									"\"TitularCuenta\":\"" + nombreTitularVal + "\",\r\n" +
									"\"FormaPago\":22,\r\n" +
									"\"ReciboName\":\"%REF%\",\r\n" +
									"\"IDBanco\":22,\r\n" +
									"\"IDCuentaDeBanco\":9,\r\n" +
									"\"FechaImpresion\":\"" + fechas[0] + "-" + fechas[2] + "-" + fechas[1] + " " + formatter2.format(new Date()) + "\"" +
									"}";
							System.out.println(bodyPago);
							resp = connectApi(String.valueOf(String.valueOf(archivo.getString("endpoint2"))) + "alpha/aplicarPago", bodyPago);
							System.out.println(resp.toString());
							try {
								myWriter.write(String.valueOf(fechaDelPago) + "," + String.valueOf(idClienteVal) + "," + String.valueOf(membresia) + "," + nombreTitularVal + "," + noReferenciaVal + "," + selectedDate + "," + importeVal + ", SI\n");
								connectApi(String.valueOf(String.valueOf(archivo.getString("endpoint2"))) + "alpha/guardarReferencia", jsonString);
							} catch (IOException ex) {
								System.out.println("Ocurrio un Error.");
								ex.printStackTrace();
							}
						} else {
							try {
								myWriter.write(String.valueOf(fechaDelPago) + "," + String.valueOf(idClienteVal) + "," + String.valueOf(membresia) + "," + nombreTitularVal + "," + noReferenciaVal + "," + selectedDate + "," + importeVal + ", NO, no se encontro ningun adeudo en dataflow\n");
							} catch (IOException ex) {
								System.out.println("Ocurrio un Error.");
								ex.printStackTrace();
							}
						}
					} catch (Exception exception) {}
				} else {
					myWriter.write(String.valueOf(((Pago)pagos.get(j)).getFechaPago()) + "," + String.valueOf(((Pago)pagos.get(j)).getIdCliente()) + "," + String.valueOf(((Pago)pagos.get(j)).getNoMembresia()) + "," + ((Pago)pagos.get(j)).getNombreTitular() + "," + noReferenciaVal + "," + selectedDate + "," + ((Pago)pagos.get(j)).getImporte() + " Pago Aplicado: NO porque este pago ya fue aplicado anteriormente\n");
				}
			}
			myWriter.close();
			JOptionPane.showMessageDialog(frame, "El proceso se ejecuto correctamente");
		} catch (Exception excep) {
			excep.printStackTrace();
			JOptionPane.showMessageDialog(frame, "Ha ocurrido un error durante el proceso\n" + excep.getMessage(), "Error", 2);
		}
	}

	// RESPUESTA DATAFLOW DOMICILIACION
	public static void aplicarPagoDomiciliacion(List<Pago> pagos, String selectedDate) {
		try {
			File myObj = new File("Respuesta dataflow.txt");
			myObj.createNewFile();
			FileWriter myWriter = new FileWriter("Respuesta dataflow.txt");
			System.out.println(pagos.size());
			myWriter.write("Id de cliente, Nombre de titular,Folio Interbancario,Fecha de aplicacion,Monto, Pago Aplicado,Observaciones\n");
			for (int i = 0; i < pagos.size(); i++) {
				String noReferenciaVal = ((Pago)pagos.get(i)).getNoReferencia();
				ObjectMapper mapper = new ObjectMapper();
				String jsonString = mapper.writeValueAsString(new Pago2(((Pago)pagos.get(i)).getIdCliente(), selectedDate, ((Pago)pagos.get(i)).getNoReferencia(), ((Pago)pagos.get(i)).getImporte(), ((Pago)pagos.get(i)).getNoReferencia(), ((Pago)pagos.get(i)).getNoMembresia(), ((Pago)pagos.get(i)).getNombreTitular(), archivo.getString("nombre")));
				System.out.println("Salida:"+jsonString);
				JSONObject respuesta = connectApi(String.valueOf(String.valueOf(archivo.getString("endpoint2"))) + "alpha/referenciaDomiciliado", jsonString);
				System.out.println("La respuesta es: "+respuesta);
				if (respuesta.getString("respuesta").equals("el pago no ha sido aplicado")) {
					double importeVal = Double.parseDouble(((Pago)pagos.get(i)).getImporte());
					int idClienteVal = ((Pago)pagos.get(i)).getIdCliente();
					String nombreTitularVal = ((Pago)pagos.get(i)).getNombreTitular();
					String body = "{\r\n\"IdCliente\":" + idClienteVal + ",\r\n" + "\"Token\":\"77D5BDD4-1FEE-4A47-86A0-1E7D19EE1C74\"\r\n" + "}";
					try {
						JSONObject resp = connectApi(String.valueOf(String.valueOf(archivo.getString("endpoint"))) + "ServiciosClubAlpha/api/Pagos/GetPedidoByCliente", body);
						SimpleDateFormat formatter2 = new SimpleDateFormat("HH:mm:ss.SSS");
						String[] fechas = selectedDate.split("-");
						if (resp != null) {
							int noPedido = resp.getInt("NoPedido");
							String bodyPago = "{\r\n\"NoPedido\":" +
									noPedido + ",\r\n" +
									"\"Monto\":" + importeVal + ",\r\n" +
									"\"Notarjeta\":\"1111\",\r\n" +
									"\"FolioInterbancario\":\"" + noReferenciaVal + "\",\r\n" +
									"\"NoAutorizacion\":\"" + noReferenciaVal + "\",\r\n" +
									"\"FechaPago\":\"" + selectedDate + "\",\r\n" +
									"\"HoraPago\":\"" + formatter2.format(new Date()) + "\",\r\n" +
									"\"TitularCuenta\":\"" + nombreTitularVal + "\",\r\n" +
									"\"FormaPago\":8,\r\n" +
									"\"ReciboName\":\"%REC%\",\r\n" +
									"\"IDBanco\":22,\r\n" +
									"\"IDCuentaDeBanco\":9,\r\n" +
									"\"FechaImpresion\":\"" + fechas[0] + "-" + fechas[2] + "-" + fechas[1] + " " + formatter2.format(new Date()) + "\"" +
									"}";
							System.out.println(bodyPago);
							resp = connectApi(String.valueOf(String.valueOf(archivo.getString("endpoint2"))) + "alpha/aplicarPago", bodyPago);
							System.out.println(resp.toString());
							try {
								myWriter.write(String.valueOf(idClienteVal) + "," + nombreTitularVal + "," + noReferenciaVal + "," + selectedDate + "," + importeVal + ", SI\n");
								connectApi(String.valueOf(String.valueOf(archivo.getString("endpoint2"))) + "alpha/guardarReferencia", jsonString);
							} catch (IOException ex) {
								System.out.println("An error occurred.");
								ex.printStackTrace();
							}
						} else {
							try {
								myWriter.write(String.valueOf(idClienteVal) + "," + nombreTitularVal + "," + noReferenciaVal + "," + selectedDate + "," + importeVal + ", NO, no se encontro ningun adeudo en dataflow\n");
							} catch (IOException ex) {
								System.out.println("An error occurred.");
								ex.printStackTrace();
							}
						}
					} catch (Exception exception) {}
				} else {
					myWriter.write(String.valueOf(((Pago)pagos.get(i)).getIdCliente()) + "," + ((Pago)pagos.get(i)).getNombreTitular() + "," + noReferenciaVal + "," + selectedDate + "," + ((Pago)pagos.get(i)).getImporte() + " Pago Aplicado: NO porque este pago ya fue aplicado anteriormente\n");
				}
			}
			myWriter.close();
			JOptionPane.showMessageDialog(frame, "El proceso se ejecuto correctamente");
		} catch (Exception excep) {
			excep.printStackTrace();
			JOptionPane.showMessageDialog(frame, "Ha ocurrido un error durante el proceso\n" + excep.getMessage(), "Error", 2);
		}
	}
}
