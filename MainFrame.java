import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
import java.io.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.*;
import java.text.*;
import javazoom.jl.player.Player;
import java.io.FileInputStream;
import java.security.*;
import java.math.*;


public class MainFrame extends JFrame{
	
	private AboutPanel ap;
	private GenresPanel gp;
	private ChartsPanel cp;
	private NewRePanel nr;
	private LoginPanel lp;
	private AlbumPanel albumpanel;
	private SignupPanel sp;
	
	private int userIdentification;
	

	public MainFrame(){
		super("Archify");
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		
		
		setSize(800,650);
		getContentPane().setBackground(Color.darkGray);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new GridBagLayout());	
		setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		
		//GenresPanel gp = new GenresPanel(this);
		//AboutPanel ap = new AboutPanel(this);
		//ChartsPanel cp = new ChartsPanel(ap,gp,this);
		lp = new LoginPanel(this);
		cp = new ChartsPanel(this);
		gp = new GenresPanel(this);
		nr = new NewRePanel(this);
		ap = new AboutPanel(this);
		Top50Panel t5p = new Top50Panel(this);
		PlaylistsPanel pp = new PlaylistsPanel(this);
		//SearchPanel sp = new SearchPanel(this, "");
		//AdminPanel adminpanel = new AdminPanel(this);
		//sp = new SignupPanel(this);
		//albumpanel = new AlbumPanel(this , "The Element of Freedom");
		getContentPane().add(lp);//initialize Starting Frame
		setVisible(true);
	}
	
	public ChartsPanel getChartsPanel(){
		return cp;
	}
	
	public GenresPanel getGenresPanel(){
		return gp;
	}
	
	public AboutPanel getAboutPanel(){
		return ap;
	}
	
	public NewRePanel getNewRePanel(){
		return nr;
	}
	
	public void setUserID(int n){
		userIdentification = n;
	}
	
	public int getUserID(){
		return userIdentification;
	}
	
	
	
	//connect to MySql
	public static Connection getConnection() throws Exception{
		
		try{
			String driver = "com.mysql.jdbc.Driver";
			String url = "jdbc:mysql://localhost:3306/spotify_database";
			String username = "root";//USERNAME
			String password = "root";//PASSWORD
			Class.forName(driver);
			
			Connection conn = DriverManager.getConnection(url,username,password);
			System.out.println("Connected");
			return conn;
		} catch(Exception e){System.out.println(e);}	
		
		return null;
	}
		
	class LoginPanel extends JPanel{
		
		private JFrame frame;
		private ImageIcon logo;
		private JLabel logolabel;
		
		private JLabel userLabel;
		private JLabel passLabel;
		
		private JTextField userText;
		private JPasswordField passText;
		
		public LoginPanel(JFrame f)
		{	
			frame = f;	
			GridBagConstraints gbc = new GridBagConstraints();
			
			setLayout(new GridBagLayout());
			setBackground(Color.darkGray);
			
			/****************SIGN UP BUTTON*****************/
			JButton butt2 = new JButton("Sign up");
			butt2.setAlignmentX(Component.CENTER_ALIGNMENT);
			butt2.setCursor(new Cursor(Cursor.HAND_CURSOR));// DENRICK
			gbc.gridx = 5;
			gbc.gridy = 11;
			gbc.insets = new Insets(5, 0, 5, 0);
			add(butt2, gbc);	
			butt2.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
					frame.getContentPane().removeAll();
					frame.setSize(650,450);
					frame.getContentPane().add(new SignupPanel(frame));
					//frame.setLayout(new FlowLayout(FlowLayout.LEFT));
					frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
					frame.revalidate();
					frame.repaint();
				}
			});
			
			/****************SIGN UP BUTTON*****************/
			
			logo = new ImageIcon(getClass().getResource("archify logo.png"));
			logolabel = new JLabel(logo);
			logolabel.setAlignmentX(Component.CENTER_ALIGNMENT);
			logolabel.setOpaque(false);
			gbc.gridx = 5;
			gbc.gridy = 5;
			gbc.insets = new Insets(0, 0, 40, 0);
			add(logolabel, gbc);
			
			userLabel = new JLabel("   Username:");
			userLabel.setOpaque(false);
			userLabel.setForeground(Color.white);
			userLabel.setFont(new Font("Arial", Font.BOLD, 20));
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.ipady = 0;       //reset to default
			gbc.weighty = 1.0;   //request any extra vertical space
			gbc.gridx = 5;
			gbc.gridy = 6;
			gbc.insets = new Insets(5, 0, 1, 0);
			add(userLabel, gbc);
			
			makeTextfield();
			
			
			passLabel = new JLabel("   Password:");
			passLabel.setOpaque(false);
			passLabel.setForeground(Color.white);
			passLabel.setFont(new Font("Arial", Font.BOLD, 20));
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.ipady = 0;       //reset to default
			gbc.weighty = 1.0;   //request any extra vertical space
			gbc.gridx = 5;
			gbc.gridx = 5;
			gbc.gridy = 8;
			gbc.insets = new Insets(5, 0, 1, 0);
			add(passLabel, gbc);
			
			
			
			ImageIcon img = new ImageIcon("sign in.png");
			JButton butt = new JButton("");
			butt.setAlignmentX(Component.CENTER_ALIGNMENT);
			butt.setIcon(img);
			butt.setBorderPainted(false); 
			butt.setContentAreaFilled(false); 
			butt.setFocusPainted(false); 
			butt.setCursor(new Cursor(Cursor.HAND_CURSOR));// DENRICK
			gbc.gridx = 5;
			gbc.gridy = 10;
			gbc.insets = new Insets(35, 0, 5, 0);
			add(butt, gbc);	
			
			frame.getRootPane().setDefaultButton(butt);
			
			butt.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					try{
						username = userText.getText();
						String pass = String.valueOf(passText.getPassword());
						MessageDigest md = MessageDigest.getInstance("SHA-256");
						md.update(pass.getBytes(), 0, pass.length());
						password = new BigInteger(1, md.digest()).toString(16);		
					}catch(Exception lololol){
						System.out.println(lololol);
					}
					
					
					if(check()){
						Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
						frame.getContentPane().removeAll();
						frame.setSize(1300,900);
						frame.getContentPane().add(getChartsPanel());
						frame.setLayout(new FlowLayout(FlowLayout.LEFT));
						frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
						frame.revalidate();
					}
					
					if(checkifadmin()){
						frame.getContentPane().removeAll();
						frame.getContentPane().add(new AdminPanel(frame));
						frame.revalidate();
						frame.repaint();
					}
				}
			});	
		}
		
		public boolean checkifadmin(){
			try{
				Connection con = getConnection();
				PreparedStatement statement = con.prepareStatement("SELECT adminName, adminPassword FROM admin where adminName = '"+username+"'");
				ResultSet result = statement.executeQuery();
				
				
				while(result.next()){
					String s = result.getString("adminName");
					String p = result.getString("adminPassword");
					if(s.equals(username) && p.equals(password))
						return true;
				}
				con.close();
				
			}catch(Exception e){System.out.println(e);}
			finally{
				System.out.println("Query Completed");
			}
			
			return false;
		}
		
		public boolean check(){
			try{
				Connection con = getConnection();
				PreparedStatement statement = con.prepareStatement("SELECT userUsername, userPassword, userID FROM user where userUsername = '" + username + "'");
				ResultSet result = statement.executeQuery();
				
				while(result.next()){
					
					String s = result.getString("userUsername");
					String p = result.getString("userPassword");
					int n = result.getInt("userID");
					setUserID(n);
					
					if(s.equals(username) && p.equals(password))
						return true;
				}
				con.close();
				
			}catch(Exception e){System.out.println(e);}
			finally{
				System.out.println("Query Completed");
			}
			
			return false;
		}
		
		public void makeTextfield(){
			GridBagConstraints gbc = new GridBagConstraints();
			Font font1 = new Font("SansSerif", Font.BOLD, 20);
			
			userText = new JTextField(29);
			gbc.gridx = 5;
			gbc.gridy = 7;
			userText.setFont(font1);
			gbc.insets = new Insets(1, 0, 10, 0);
			add(userText, gbc);
			
			passText = new JPasswordField(29);
			gbc.gridx = 5;
			gbc.gridy = 9;
			passText.setFont(font1);
			gbc.insets = new Insets(1, 0, 10, 0);
			add(passText, gbc);
			
		}
		
		private String username;
		private String password;
	}
	
	class ChartsPanel extends JPanel{
		
		private JFrame frame;
		private JPanel jp;
		private ImageIcon logo;
		private JLabel logolabel;
		private JLabel header;
		private JLabel teamlogo;
		private JLabel de_team;
		
		private JTextField userText;
		private JTextField passText;
		
		private JButton butt;
		private JButton aboutbutt;
		
		private JButton Cbutt;
		private JButton Gbutt;
		private JButton Newbutt;
		
		private JButton GT50butt;
		private JButton PHT50butt;
		private JButton G50butt;
		private JButton PH50butt;
		
		public ChartsPanel(JFrame f)
		{	
			frame = f;
			jp = new JPanel();
			
			GridBagConstraints gbc = new GridBagConstraints();
			
			setLayout(new GridBagLayout());
			setBackground(Color.darkGray);
			
			logo = new ImageIcon(getClass().getResource("archifylogo1.png"));
			logolabel = new JLabel(logo);
			logolabel.setOpaque(false);
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.insets = new Insets(0, 0, 0, 20);
			add(logolabel, gbc);
			
			
			
			
			butt = new JButton("BROWSE");
			butt.setBorderPainted(false); 
			butt.setFocusPainted(false);
			butt.setForeground(Color.darkGray);
			butt.setFont(new Font("Arial", Font.BOLD, 20));
			gbc.gridx = 0;
			gbc.gridy = 1;
			gbc.insets = new Insets(0, 0, 0, 20);
			butt.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseEntered(java.awt.event.MouseEvent evt) {
					butt.setBackground(Color.GREEN);
				}

				public void mouseExited(java.awt.event.MouseEvent evt) {
					butt.setBackground(UIManager.getColor("control"));
				}
			});
			
			butt.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					frame.getContentPane().removeAll();
					frame.getContentPane().add(new ChartsPanel(frame));
					frame.setLayout(new FlowLayout(FlowLayout.LEFT));
					frame.revalidate();
					frame.repaint();
				}
			});
			
			add(butt, gbc);
			
			JButton profileButton = new JButton("My Profile");
			profileButton.setBorderPainted(false); 
			profileButton.setFocusPainted(false);
			profileButton.setForeground(Color.darkGray);
			profileButton.setFont(new Font("Arial", Font.BOLD, 20));
			gbc.gridx = 0;
			gbc.gridy = 2;
			gbc.insets = new Insets(0, 0, 0, 20);
			profileButton.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseEntered(java.awt.event.MouseEvent evt) {
					profileButton.setBackground(Color.GREEN);
				}

				public void mouseExited(java.awt.event.MouseEvent evt) {
					profileButton.setBackground(UIManager.getColor("control"));
				}
			});
			
			profileButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){		
					Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		
					frame.getContentPane().removeAll();
					frame.getContentPane().add(new ProfilePanel(frame,1));
					frame.setLayout(new GridBagLayout());
					frame.setSize(1000,800);	
					frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
					frame.revalidate();
					frame.repaint();
				}
			});
			
			add(profileButton, gbc);
			
			
			
			JButton playlistbutt = new JButton("Playlists");
			playlistbutt.setBorderPainted(false); 
			playlistbutt.setFocusPainted(false);
			playlistbutt.setForeground(Color.darkGray);
			playlistbutt.setFont(new Font("Arial", Font.BOLD, 20));
			gbc.gridx = 0;
			gbc.gridy = 3;
			gbc.insets = new Insets(0, 0, 0, 20);
			playlistbutt.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseEntered(java.awt.event.MouseEvent evt) {
					playlistbutt.setBackground(Color.GREEN);
				}

				public void mouseExited(java.awt.event.MouseEvent evt) {
					playlistbutt.setBackground(UIManager.getColor("control"));
				}
			});
			
			playlistbutt.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					JScrollPane scrollPane = new JScrollPane(new PlaylistsPanel(frame));
					scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
					scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
					scrollPane.setPreferredSize(new Dimension(1300, 800));
					scrollPane.getVerticalScrollBar().setUnitIncrement(16);
					scrollPane.setOpaque(false);
					
					
					frame.getContentPane().removeAll();
					frame.getContentPane().add(scrollPane);
					frame.setLayout(new FlowLayout(FlowLayout.LEFT));
					frame.pack();
					frame.revalidate();
					frame.repaint();
				}
			});
			
			add(playlistbutt, gbc);
			
			JButton artistbutt = new JButton("Artists");
			artistbutt.setBorderPainted(false); 
			artistbutt.setFocusPainted(false);
			artistbutt.setForeground(Color.darkGray);
			artistbutt.setFont(new Font("Arial", Font.BOLD, 20));
			gbc.gridx = 0;
			gbc.gridy = 4;
			gbc.insets = new Insets(10, 10, 10, 10);
			artistbutt.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseEntered(java.awt.event.MouseEvent evt) {
					artistbutt.setBackground(Color.GREEN);
				}

				public void mouseExited(java.awt.event.MouseEvent evt) {
					artistbutt.setBackground(UIManager.getColor("control"));
				}
			});
			
			artistbutt.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					JScrollPane scrollPane = new JScrollPane(new ArtistsPanel(frame));
					scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
					scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
					scrollPane.setPreferredSize(new Dimension(1300, 800));
					scrollPane.getVerticalScrollBar().setUnitIncrement(16);
					scrollPane.setOpaque(false);
					
					
					frame.getContentPane().removeAll();
					frame.getContentPane().add(scrollPane);
					frame.setLayout(new FlowLayout(FlowLayout.LEFT));
					frame.pack();
					frame.revalidate();
					frame.repaint();
				}
			});
			
			add(artistbutt, gbc);
			
			
			
			aboutbutt = new JButton("ABOUT");
			//butt.setAlignmentX(Component.CENTER_ALIGNMENT);
			aboutbutt.setBorderPainted(false); 
			//aboutbutt.setContentAreaFilled(false); 
			aboutbutt.setFocusPainted(false);
			aboutbutt.setForeground(Color.darkGray);
			aboutbutt.setFont(new Font("Arial", Font.BOLD, 30));
			//butt.setOpaque(false);
			gbc.gridx = 0;
			gbc.gridy = 10;
			//gbc.insets = new Insets(0, 0, 0, 0);
			aboutbutt.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseEntered(java.awt.event.MouseEvent evt) {
					aboutbutt.setBackground(Color.GREEN);
				}

				public void mouseExited(java.awt.event.MouseEvent evt) {
					aboutbutt.setBackground(UIManager.getColor("control"));
				}
			});
			aboutbutt.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					frame.getContentPane().removeAll();
					frame.getContentPane().add(new AboutPanel(frame));
					frame.setLayout(new FlowLayout(FlowLayout.LEFT));
					frame.revalidate();
					frame.repaint();
				}
			});
			
			add(aboutbutt, gbc);
			
			
			header = new JLabel("BROWSE");
			header.setFont(new Font("Arial", Font.BOLD, 65));
			header.setForeground(Color.WHITE);
			gbc.gridx = 5;
			gbc.gridy = 0;
			gbc.weighty = 0;
			gbc.weightx = 0;
			gbc.anchor = GridBagConstraints.WEST;
			gbc.insets = new Insets(0, 0, 0, 0);
			add(header, gbc);
			
			
			Cbutt = new JButton("Charts");
			Cbutt.setBorderPainted(false); 
			Cbutt.setFocusPainted(false);
			Cbutt.setForeground(Color.darkGray);
			Cbutt.setFont(new Font("Arial", Font.BOLD, 20));
			gbc.gridx = 5;
			gbc.gridy = 1;
			gbc.insets = new Insets(0, 0, 0, 0);
			Cbutt.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseEntered(java.awt.event.MouseEvent evt) {
					Cbutt.setBackground(Color.GREEN);
				}

				public void mouseExited(java.awt.event.MouseEvent evt) {
					Cbutt.setBackground(UIManager.getColor("control"));
				}
			});
			
			/*
			Cbutt.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					frame.getContentPane().removeAll();
					frame.getContentPane().add(this);
					revalidate();
				}
			});
			*/
			add(Cbutt, gbc);

			
			Gbutt = new JButton("Genres & Moods");
			Gbutt.setBorderPainted(false); 
			Gbutt.setFocusPainted(false);
			Gbutt.setForeground(Color.darkGray);
			Gbutt.setFont(new Font("Arial", Font.BOLD, 20));
			gbc.gridx = 6;
			gbc.gridy = 1;
			gbc.insets = new Insets(0, 0, 0, 0);
			Gbutt.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseEntered(java.awt.event.MouseEvent evt) {
					Gbutt.setBackground(Color.GREEN);
				}

				public void mouseExited(java.awt.event.MouseEvent evt) {
					Gbutt.setBackground(UIManager.getColor("control"));
				}
			});
			
			Gbutt.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					frame.getContentPane().removeAll();
					frame.getContentPane().add(new GenresPanel(frame));
					frame.setLayout(new FlowLayout(FlowLayout.LEFT));
					frame.revalidate();
					frame.repaint();
				}
			});
			
			add(Gbutt, gbc);
			
			
			
			Newbutt = new JButton("New Releases");
			Newbutt.setBorderPainted(false); 
			Newbutt.setFocusPainted(false);
			Newbutt.setForeground(Color.darkGray);
			Newbutt.setFont(new Font("Arial", Font.BOLD, 20));
			gbc.gridx = 7;
			gbc.gridy = 1;
			gbc.insets = new Insets(0, 0, 0, 0);
			Newbutt.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseEntered(java.awt.event.MouseEvent evt) {
					Newbutt.setBackground(Color.GREEN);
				}

				public void mouseExited(java.awt.event.MouseEvent evt) {
					Newbutt.setBackground(UIManager.getColor("control"));
				}
			});
			
			Newbutt.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					JScrollPane scrollPane = new JScrollPane(new NewRePanel(frame));
					scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
					scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
					scrollPane.setPreferredSize(new Dimension(1300, 800));
					scrollPane.getVerticalScrollBar().setUnitIncrement(16);
					scrollPane.setOpaque(false);
					
					
					frame.getContentPane().removeAll();
					frame.getContentPane().add(scrollPane);
					frame.setLayout(new FlowLayout(FlowLayout.LEFT));
					frame.pack();
					frame.revalidate();
					frame.repaint();
				}
			});
			
			add(Newbutt, gbc);
			
			
			logo = new ImageIcon("globaltop50.png");
			GT50butt = new JButton("New Releases");
			GT50butt.setBorderPainted(false); 
			GT50butt.setFocusPainted(false); 
			GT50butt.setContentAreaFilled(false); 
			GT50butt.setForeground(Color.darkGray);
			GT50butt.setIcon(logo);
			gbc.gridx = 5;
			gbc.gridy = 2;
			gbc.insets = new Insets(0, 0, 0, 0);
			GT50butt.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseEntered(java.awt.event.MouseEvent evt) {
					GT50butt.setBackground(Color.GREEN);
				}

				public void mouseExited(java.awt.event.MouseEvent evt) {
					GT50butt.setBackground(UIManager.getColor("control"));
				}
			});
			
			GT50butt.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					frame.getContentPane().removeAll();
					frame.getContentPane().add(new Top50Panel(frame));
					frame.setLayout(new GridBagLayout());
					frame.setSize(1000,800);	
					frame.revalidate();
					frame.repaint();
				}
			});
					
			add(GT50butt, gbc);
			
			
			logo = new ImageIcon("philippinestop50.png");
			PHT50butt = new JButton("New Releases");
			PHT50butt.setBorderPainted(false); 
			PHT50butt.setFocusPainted(false); 
			PHT50butt.setContentAreaFilled(false); 
			PHT50butt.setForeground(Color.darkGray);
			PHT50butt.setIcon(logo);
			gbc.gridx = 6;
			gbc.gridy = 2;
			gbc.insets = new Insets(0, 0, 0, 0);
			PHT50butt.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseEntered(java.awt.event.MouseEvent evt) {
					PHT50butt.setBackground(Color.GREEN);
				}

				public void mouseExited(java.awt.event.MouseEvent evt) {
					PHT50butt.setBackground(UIManager.getColor("control"));
				}
			});
			add(PHT50butt, gbc);
			
			
			logo = new ImageIcon("globalviral50.png");
			G50butt = new JButton("New Releases");
			G50butt.setBorderPainted(false); 
			G50butt.setFocusPainted(false); 
			G50butt.setContentAreaFilled(false); 
			G50butt.setForeground(Color.darkGray);
			G50butt.setIcon(logo);
			gbc.gridx = 5;
			gbc.gridy = 3;
			gbc.insets = new Insets(0, 0, 0, 0);
			G50butt.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseEntered(java.awt.event.MouseEvent evt) {
					G50butt.setBackground(Color.GREEN);
				}

				public void mouseExited(java.awt.event.MouseEvent evt) {
					G50butt.setBackground(UIManager.getColor("control"));
				}
			});
			//add(G50butt, gbc);
			

			logo = new ImageIcon("philippinesviral50.png");
			PH50butt = new JButton("New Releases");
			PH50butt.setBorderPainted(false); 
			PH50butt.setFocusPainted(false); 
			PH50butt.setContentAreaFilled(false); 
			PH50butt.setForeground(Color.darkGray);
			PH50butt.setIcon(logo);
			gbc.gridx = 6;
			gbc.gridy = 3;
			gbc.insets = new Insets(0, 0, 0, 0);
			PH50butt.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseEntered(java.awt.event.MouseEvent evt) {
					PH50butt.setBackground(Color.GREEN);
				}

				public void mouseExited(java.awt.event.MouseEvent evt) {
					PH50butt.setBackground(UIManager.getColor("control"));
				}
			});
			//add(PH50butt, gbc);
			
			JTextField jt = new JTextField(10);
			gbc.gridx = 0;
			gbc.gridy = 6;
			gbc.insets = new Insets(10, 10, 10, 10);
			add(jt, gbc);
			
			JButton searchbutt = new JButton("Search");
			gbc.gridx = 0;
			gbc.gridy = 7;
			gbc.insets = new Insets(10, 10, 10, 10);
			searchbutt.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseEntered(java.awt.event.MouseEvent evt) {
					searchbutt.setBackground(Color.GREEN);
				}

				public void mouseExited(java.awt.event.MouseEvent evt) {
					searchbutt.setBackground(UIManager.getColor("control"));
				}
			});
			
			searchbutt.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					

					frame.getContentPane().removeAll();
					frame.getContentPane().add(new SearchPanel(frame, jt.getText()));
					frame.setSize(1000,800);
					frame.setLayout(new GridBagLayout());	
					frame.revalidate();
					frame.repaint();
				}
			});
			add(searchbutt, gbc);
		}
	}
	
	class GenresPanel extends JPanel{
		
		private JFrame frame;
		private JPanel jp;
		private ImageIcon logo;
		private JLabel logolabel;
		private JLabel header;
		private JLabel teamlogo;
		private JLabel de_team;
		
		private JTextField userText;
		private JTextField passText;
		
		private JButton butt;
		private JButton aboutbutt;
		
		private JButton Cbutt;
		private JButton Gbutt;
		private JButton Newbutt;
				
		private JButton popbutt;
		private JButton rockbutt;
		private JButton moodbutt;
		private JButton jazzbutt;

		
		public GenresPanel(JFrame f)
		{	
			frame = f;
			
			GridBagConstraints gbc = new GridBagConstraints();
			
			setLayout(new GridBagLayout());
			setBackground(Color.darkGray);
			
			logo = new ImageIcon(getClass().getResource("archifylogo1.png"));
			logolabel = new JLabel(logo);
			logolabel.setOpaque(false);
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.weighty = 1;
			gbc.weightx = 1;
			//gbc.anchor = GridBagConstraints.WEST;
			gbc.insets = new Insets(0, 0, 0, 20);
			add(logolabel, gbc);
			
			
			
			
			butt = new JButton("BROWSE");
			butt.setBorderPainted(false); 
			butt.setFocusPainted(false);
			butt.setForeground(Color.darkGray);
			butt.setFont(new Font("Arial", Font.BOLD, 20));
			gbc.gridx = 0;
			gbc.gridy = 1;
			gbc.insets = new Insets(0, 0, 0, 20);
			butt.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseEntered(java.awt.event.MouseEvent evt) {
					butt.setBackground(Color.GREEN);
				}

				public void mouseExited(java.awt.event.MouseEvent evt) {
					butt.setBackground(UIManager.getColor("control"));
				}
			});
			
			butt.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					frame.getContentPane().removeAll();
					frame.getContentPane().add(new ChartsPanel(frame));
					frame.setLayout(new FlowLayout(FlowLayout.LEFT));
					frame.revalidate();
					frame.repaint();
				}
			});
			
			add(butt, gbc);
			
			JButton profileButton = new JButton("My Profile");
			profileButton.setBorderPainted(false); 
			profileButton.setFocusPainted(false);
			profileButton.setForeground(Color.darkGray);
			profileButton.setFont(new Font("Arial", Font.BOLD, 20));
			gbc.gridx = 0;
			gbc.gridy = 2;
			gbc.insets = new Insets(0, 0, 0, 20);
			profileButton.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseEntered(java.awt.event.MouseEvent evt) {
					profileButton.setBackground(Color.GREEN);
				}

				public void mouseExited(java.awt.event.MouseEvent evt) {
					profileButton.setBackground(UIManager.getColor("control"));
				}
			});
			
			profileButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){		
					Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		
					frame.getContentPane().removeAll();
					frame.getContentPane().add(new ProfilePanel(frame,1));
					frame.setLayout(new GridBagLayout());
					frame.setSize(1000,800);	
					frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
					frame.revalidate();
					frame.repaint();
				}
			});
			
			add(profileButton, gbc);
			
			
			aboutbutt = new JButton("ABOUT");
			//butt.setAlignmentX(Component.CENTER_ALIGNMENT);
			aboutbutt.setBorderPainted(false); 
			//aboutbutt.setContentAreaFilled(false); 
			aboutbutt.setFocusPainted(false);
			aboutbutt.setForeground(Color.darkGray);
			aboutbutt.setFont(new Font("Arial", Font.BOLD, 30));
			//butt.setOpaque(false);
			gbc.gridx = 0;
			gbc.gridy = 10;
			//gbc.insets = new Insets(0, 0, 0, 0);
			aboutbutt.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseEntered(java.awt.event.MouseEvent evt) {
					aboutbutt.setBackground(Color.GREEN);
				}

				public void mouseExited(java.awt.event.MouseEvent evt) {
					aboutbutt.setBackground(UIManager.getColor("control"));
				}
			});
			aboutbutt.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					frame.getContentPane().removeAll();
					frame.getContentPane().add(new AboutPanel(frame));
					frame.setLayout(new FlowLayout(FlowLayout.LEFT));
					frame.revalidate();
					frame.repaint();
				}
			});
			
			add(aboutbutt, gbc);
			
			
			header = new JLabel("BROWSE");
			header.setFont(new Font("Arial", Font.BOLD, 65));
			header.setForeground(Color.WHITE);
			gbc.gridx = 5;
			gbc.gridy = 0;
			gbc.weighty = 0;
			gbc.weightx = 0;
			gbc.anchor = GridBagConstraints.WEST;
			gbc.insets = new Insets(0, 0, 0, 0);
			add(header, gbc);
			
			
			Cbutt = new JButton("Charts");
			Cbutt.setBorderPainted(false); 
			Cbutt.setFocusPainted(false);
			Cbutt.setForeground(Color.darkGray);
			Cbutt.setFont(new Font("Arial", Font.BOLD, 20));
			gbc.gridx = 5;
			gbc.gridy = 1;
			gbc.insets = new Insets(0, 0, 0, 0);
			Cbutt.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseEntered(java.awt.event.MouseEvent evt) {
					Cbutt.setBackground(Color.GREEN);
				}

				public void mouseExited(java.awt.event.MouseEvent evt) {
					Cbutt.setBackground(UIManager.getColor("control"));
				}
			});
			
			
			Cbutt.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					frame.getContentPane().removeAll();
					frame.getContentPane().add(new ChartsPanel(frame));
					frame.setLayout(new FlowLayout(FlowLayout.LEFT));
					frame.revalidate();
					frame.repaint();
				}
			});
			
			add(Cbutt, gbc);

			
			Gbutt = new JButton("Genres & Moods");
			Gbutt.setBorderPainted(false); 
			Gbutt.setFocusPainted(false);
			Gbutt.setForeground(Color.darkGray);
			Gbutt.setFont(new Font("Arial", Font.BOLD, 20));
			gbc.gridx = 6;
			gbc.gridy = 1;
			gbc.insets = new Insets(0, 0, 0, 0);
			Gbutt.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseEntered(java.awt.event.MouseEvent evt) {
					Gbutt.setBackground(Color.GREEN);
				}

				public void mouseExited(java.awt.event.MouseEvent evt) {
					Gbutt.setBackground(UIManager.getColor("control"));
				}
			});
			/*
			Gbutt.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					frame.getContentPane().removeAll();
					frame.getContentPane().add();
					revalidate();
				}
			});
			*/
			add(Gbutt, gbc);
			
			
			
			Newbutt = new JButton("New Releases");
			Newbutt.setBorderPainted(false); 
			Newbutt.setFocusPainted(false);
			Newbutt.setForeground(Color.darkGray);
			Newbutt.setFont(new Font("Arial", Font.BOLD, 20));
			gbc.gridx = 7;
			gbc.gridy = 1;
			gbc.insets = new Insets(0, 0, 0, 0);
			Newbutt.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseEntered(java.awt.event.MouseEvent evt) {
					Newbutt.setBackground(Color.GREEN);
				}

				public void mouseExited(java.awt.event.MouseEvent evt) {
					Newbutt.setBackground(UIManager.getColor("control"));
				}
			});
			
			Newbutt.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					JScrollPane scrollPane = new JScrollPane(new NewRePanel(frame));
					scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
					scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
					scrollPane.setPreferredSize(new Dimension(1300, 800));
					//scrollPane.setBounds(1, 1, 800, 800);
					//scrollPane.setBorderPainted(false);
					scrollPane.getVerticalScrollBar().setUnitIncrement(16);
					scrollPane.setOpaque(false);
					
					
					frame.getContentPane().removeAll();
					frame.getContentPane().add(scrollPane);
					frame.setLayout(new FlowLayout(FlowLayout.LEFT));
					frame.pack();
					frame.revalidate();
					frame.repaint();
				}
			});
			
			add(Newbutt, gbc);
			
			
			logo = new ImageIcon("pop.png");
			popbutt = new JButton();
			popbutt.setBorderPainted(false); 
			popbutt.setFocusPainted(false); 
			popbutt.setContentAreaFilled(false); 
			popbutt.setForeground(Color.darkGray);
			popbutt.setIcon(logo);
			gbc.gridx = 5;
			gbc.gridy = 2;
			gbc.insets = new Insets(0, 0, 0, 0);
			popbutt.setCursor(new Cursor(Cursor.HAND_CURSOR));
			popbutt.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseEntered(java.awt.event.MouseEvent evt) {
					popbutt.setBackground(Color.GREEN);
				}

				public void mouseExited(java.awt.event.MouseEvent evt) {
					popbutt.setBackground(UIManager.getColor("control"));
				}
			});
			
			popbutt.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
						frame.getContentPane().removeAll();
						frame.setSize(1000,800);
						frame.getContentPane().add(new GenreInPanel(frame, "Pop"));
						frame.setLayout(new GridBagLayout());
						frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);					
						frame.revalidate();
						frame.repaint();
					}
				});
			
			add(popbutt, gbc);
			
			
			logo = new ImageIcon("rock.png");
			rockbutt = new JButton();
			rockbutt.setBorderPainted(false); 
			rockbutt.setFocusPainted(false); 
			rockbutt.setContentAreaFilled(false); 
			rockbutt.setForeground(Color.darkGray);
			rockbutt.setIcon(logo);
			gbc.gridx = 6;
			gbc.gridy = 2;
			gbc.insets = new Insets(0, 0, 0, 0);
			rockbutt.setCursor(new Cursor(Cursor.HAND_CURSOR));
			rockbutt.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseEntered(java.awt.event.MouseEvent evt) {
					rockbutt.setBackground(Color.GREEN);
				}

				public void mouseExited(java.awt.event.MouseEvent evt) {
					rockbutt.setBackground(UIManager.getColor("control"));
				}
			});
			
			rockbutt.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
						frame.getContentPane().removeAll();
						frame.setSize(1000,800);
						frame.getContentPane().add(new GenreInPanel(frame, "Rock"));
						frame.setLayout(new GridBagLayout());
						frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);					
						frame.revalidate();
						frame.repaint();
					}
				});
			
			add(rockbutt, gbc);
			
			logo = new ImageIcon("mood.png");
			moodbutt = new JButton();
			moodbutt.setBorderPainted(false); 
			moodbutt.setFocusPainted(false); 
			moodbutt.setContentAreaFilled(false); 
			moodbutt.setForeground(Color.darkGray);
			moodbutt.setIcon(logo);
			gbc.gridx = 5;
			gbc.gridy = 3;
			gbc.insets = new Insets(0, 0, 0, 0);
			moodbutt.setCursor(new Cursor(Cursor.HAND_CURSOR));
			moodbutt.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseEntered(java.awt.event.MouseEvent evt) {
					moodbutt.setBackground(Color.GREEN);
				}

				public void mouseExited(java.awt.event.MouseEvent evt) {
					moodbutt.setBackground(UIManager.getColor("control"));
				}
			});
			
			moodbutt.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
						frame.getContentPane().removeAll();
						frame.setSize(1000,800);
						frame.getContentPane().add(new GenreInPanel(frame, "Mood"));
						frame.setLayout(new GridBagLayout());
						frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);					
						frame.revalidate();
						frame.repaint();
					}
				});
			
			add(moodbutt, gbc);
			
			logo = new ImageIcon("jazz.png");
			jazzbutt = new JButton();
			jazzbutt.setBorderPainted(false); 
			jazzbutt.setFocusPainted(false); 
			jazzbutt.setContentAreaFilled(false); 
			jazzbutt.setForeground(Color.darkGray);
			jazzbutt.setIcon(logo);
			gbc.gridx = 6;
			gbc.gridy = 3;
			gbc.insets = new Insets(0, 0, 0, 0);
			jazzbutt.setCursor(new Cursor(Cursor.HAND_CURSOR));
			
			jazzbutt.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseEntered(java.awt.event.MouseEvent evt) {
					jazzbutt.setBackground(Color.GREEN);
				}

				public void mouseExited(java.awt.event.MouseEvent evt) {
					jazzbutt.setBackground(UIManager.getColor("control"));
				}
			});
			
			jazzbutt.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
						frame.getContentPane().removeAll();
						frame.setSize(1000,800);
						frame.getContentPane().add(new GenreInPanel(frame, "Jazz"));
						frame.setLayout(new GridBagLayout());
						frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);					
						frame.revalidate();
						frame.repaint();
					}
				});
			
			add(jazzbutt, gbc);
			
			
			
		}
	}
	
	class GenreInPanel extends JPanel{
		
		private JFrame frame;
		private ImageIcon logo;
		private JLabel logolabel;
		private JLabel header;
		private JLabel teamlogo;
		private JLabel de_team;
		
		private JTextField userText;
		private JTextField passText;
		
		private JButton butt;
		private JButton aboutbutt;
		
		//private GridBagConstraints gbc; 
		
		public GenreInPanel(JFrame f, String genreString)
		{	
			num = 0;
			frame = f;
			int n = 14; // n is the number of songs;
			
			checkgenre(genreString);
			
			GridBagConstraints gbc = new GridBagConstraints();
			
			setLayout(new GridBagLayout());
			setBackground(Color.darkGray);
			/*
			logo = new ImageIcon(getClass().getResource("archifylogo1.png"));
			logolabel = new JLabel(logo);
			//logolabel.setAlignmentX(Component.CENTER_ALIGNMENT);
			logolabel.setOpaque(false);
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.weighty = 1;
			gbc.weightx = 1;
			//gbc.anchor = GridBagConstraints.WEST;
			gbc.insets = new Insets(0, 0, 0, 20);
			
			*/
			
			String AL = genreName + ".png";
			BufferedImage img = scaleImage(300,300, AL); //genreName is the name of the genre image
			logo = new ImageIcon(img);
			logolabel = new JLabel(logo); 
			//logolabel.setAlignmentX(Component.CENTER_ALIGNMENT);
			logolabel.setOpaque(false);
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.weighty = 1;
			gbc.weightx = 1;
			//gbc.anchor = GridBagConstraints.WEST;
			gbc.insets = new Insets(0, 0, 20, 0);
			add(logolabel, gbc);
			
			logolabel = new JLabel ("" + genreName); //GENRE NAME
			logolabel.setOpaque(false);
			logolabel.setForeground(Color.white);
			logolabel.setFont(new Font("Arial", Font.BOLD, 20));
			gbc.gridx = 0;
			gbc.gridy = 1;
			gbc.weighty = 1;
			gbc.weightx = 1;
			gbc.insets = new Insets(0, 0, 40, 0);
			add(logolabel, gbc);
			
			checkSongs();
			
			//PUT SONG COUNT HERE
			logolabel = new JLabel ("" + songArray.length + " Songs");
			logolabel.setOpaque(false);
			logolabel.setForeground(Color.white);
			logolabel.setFont(new Font("Arial", Font.PLAIN, 16));
			gbc.gridx = 0;
			gbc.gridy = 3;
			gbc.weighty = 1;
			gbc.weightx = 1;
			gbc.insets = new Insets(0, 0, 20, 0);
			add(logolabel, gbc);
			
			
			JButton butt = new JButton("PLAY");
			//butt.setOpaque(false);
			gbc.gridx = 0;
			gbc.gridy = 10;
			gbc.insets = new Insets(10, 0, 5, 0);
			add(butt, gbc);
			
			try{
				DefaultListModel<String> songname = new DefaultListModel<>();
				
				songs = getSongName();//ARRAY
				int n1;
				for(int i = 0; i < songs.size(); i++){
					n1 = i+1;
					songname.addElement(n1 + ".) " + songs.get(i));// <------ eto 
				}
				  
				
				JList<String> songList = new JList<>(songname); //<------- tapos eto
				songList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				songList.setSelectedIndex(0);
				songList.setVisibleRowCount(7); 
				songList.setFont(new Font("Arial", Font.PLAIN, 25));
				
				JScrollPane songListScrollPane = new JScrollPane(songList); // tapos eto
				gbc.gridx = 1;
				gbc.gridy = 0;
		
				gbc.insets = new Insets(0, 50, 0, 0);

				add(songListScrollPane, gbc);
				
				
				butt.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						if(num == 0){
							String str = songList.getSelectedValue();
							selectedSong = str.substring(4,str.length());
							
							String artistName = null;
							try {
								Connection con = getConnection();
								PreparedStatement statement = con.prepareStatement("select artistName from songs, artist where songTitle = '"+ selectedSong + "' and songs.artistID = artist.artistID");
								ResultSet result = statement.executeQuery();
								
								while(result.next()){
										 artistName = result.getString(1);
								}
								
								mp3player = new MP3(selectedSong, artistName);
								mp3player.start();
								num = 1;
								checkplaycount();
								playcount++;
								addplaycount();
							} catch (Exception except) {except.printStackTrace();}
							butt.setText("STOP");
						}
						
						else if(num == 1){
							mp3player.pause();
							num = 0;
							butt.setText("PLAY");
						}

					}
					
				});
				
			} catch(Exception e){
				System.out.println(e);
			}
				
				
			JButton returnbutt = new JButton("Back");
			//butt.setOpaque(false);
			gbc.gridx = 2;
			gbc.gridy = 0;
			gbc.insets = new Insets(35, 0, 5, 0);
			returnbutt.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					if(mp3player != null)
						mp3player.pause();
				
					frame.getContentPane().removeAll();
					frame.getContentPane().add(new GenresPanel(frame));
					frame.setLayout(new FlowLayout(FlowLayout.LEFT));
					frame.revalidate();
					frame.repaint();
				}
			});
			add(returnbutt, gbc);
					
				
		}
		
		public void checkplaycount(){
			try{
				
				Connection con = getConnection();
				
				String s = makesqlString();
				System.out.println(s);
				PreparedStatement statement = con.prepareStatement("select playcount from songs where songTitle = '"+ selectedSong + "'");
				System.out.println("select songID from songs where songTitle = '"+ selectedSong + "'");
				ResultSet result = statement.executeQuery();
				
				while(result.next()){
					     playcount = result.getInt(1);
				}
				
				System.out.println("playcount: " + playcount);
				
			}catch(Exception e){System.out.println(e);}
			finally{
				System.out.println("Query Completed");
			}
		}
		
		public void addplaycount(){
			try{
				
				Connection con = getConnection();
				
				String s = makesqlString();
				System.out.println(s);
				PreparedStatement posted = con.prepareStatement("UPDATE songs SET playcount = "+playcount +" where songTitle = '"+selectedSong+"'");
				System.out.println("UPDATE songs SET playcount = "+playcount +" where songTitle = '"+selectedSong+"'");
				
				posted.executeUpdate();
				
				//System.out.println("playcount: " + playcount);
				
			}catch(Exception e){System.out.println(e);}
			finally{
				System.out.println("Update Completed");
			}
		}
		
		
		public void checkSelectedSong(){
			try{
				
				Connection con = getConnection();
				
				String s = makesqlString();
				System.out.println(s);
				PreparedStatement statement = con.prepareStatement("select songID from songs where songTitle = '"+ songTitle + "'");
				System.out.println("select songID from songs where songTitle = '"+ songTitle + "'");
				ResultSet result = statement.executeQuery();
				
				while(result.next()){
					     songID = result.getInt("songID");
				}
				
			}catch(Exception e){System.out.println(e);}
			finally{
				System.out.println("Query Completed");
			}
		}
		
		
		
		public String makesqlString(){
			String s = "";
			for(int i = 0; i < songArray.length; i++){			
				s +=  "songID = "+ songArray[i] + "";
				if(i == songArray.length - 1){
					
				} else{
					s += " or ";
					
				}
			}
			
			return s;
		}
		
		public ArrayList<String> getSongName(){
			
			songs = new ArrayList<>(); 
			try{
				
				Connection con = getConnection();
				
				String s = makesqlString();
				System.out.println(s);
				PreparedStatement statement = con.prepareStatement("select songTitle from songs where "+ s + "");
				
				ResultSet result = statement.executeQuery();
				
				while(result.next()){
					     songs.add(result.getString(1)); 
				}
				
			}catch(Exception e){System.out.println(e);}
			finally{
				System.out.println("Query Completed");
			}
			
			return songs;
		}
		
		public void checkSongs(){
			
			String[] parts = genreSongs.split(",");
			int[] n1 = new int[parts.length];
			for(int n = 0; n < parts.length; n++) {
				n1[n] = Integer.parseInt(parts[n]);
				System.out.println("Number" + n +": " + n1[n]+ "");
				
			}
			
			songArray = n1;
			
			
		}
		
		public BufferedImage scaleImage(int WIDTH, int HEIGHT, String filename) {
			BufferedImage bi = null;
			try {
				ImageIcon ii = new ImageIcon(filename);//path to image
				bi = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
				Graphics2D g2d = (Graphics2D) bi.createGraphics();
				g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY));
				g2d.drawImage(ii.getImage(), 0, 0, WIDTH, HEIGHT, null);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			return bi;
		}
		
		public void checkgenre(String g){
			
			try{
				Connection con = getConnection();
				PreparedStatement statement = con.prepareStatement("SELECT * FROM genres g where g.genreName =  '"+ g + "'");
				System.out.println("SELECT * FROM genres g where g.genreName =  '"+ g + "'");
				ResultSet result = statement.executeQuery();
				
				while(result.next()){
					
					genreName = result.getString("genreName");
					genreID = result.getInt("genreID");
		
					genreSongs = result.getString("genreSongs");
					      
					System.out.println("\nGenre Name: " + genreName + "\ngenreID: "+ genreID + "\ngenreSongs: " + genreSongs);
				}
				
			}catch(Exception e){System.out.println(e);}
			finally{
				System.out.println("Query Completed");
			}
		}
			
		private String genreName;
		private int genreID;
		
		private String genreSongs;
		private ArrayList<String> songs;
		private int[] songArray;
		
		private String songTitle;
		private int songID;
		
		private String selectedSong;
		private MP3 mp3player;
		private int playcount;
		private int num = 0;
		
		
		
	}

	class NewRePanel extends JPanel{
	
		private JFrame frame;
		private JPanel jp;
		private ImageIcon logo;
		private JLabel logolabel;
		private JLabel header;
		private JLabel teamlogo;
		private JLabel de_team;
		
		private JTextField userText;
		private JTextField passText;
		
		private JButton butt;
		private JButton aboutbutt;
		
		private JButton Cbutt;
		private JButton Gbutt;
		private JButton Newbutt;

		private JButton Albumbutt;
		
		public NewRePanel(JFrame f)
		{	
			frame = f;
			/*JLabel label = new JLabel("SPOTIFY");
			label.setFont(new Font("Comic sans MS", Font.BOLD, 30));
			label.setAlignmentX(Component.CENTER_ALIGNMENT);
			add(label);*/
			
			GridBagConstraints gbc = new GridBagConstraints();
			
			setLayout(new GridBagLayout());
			setBackground(Color.darkGray);
			
			logo = new ImageIcon(getClass().getResource("archifylogo1.png"));
			logolabel = new JLabel(logo);
			//logolabel.setAlignmentX(Component.CENTER_ALIGNMENT);
			logolabel.setOpaque(false);
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.weighty = 1;
			gbc.weightx = 1;
			//gbc.anchor = GridBagConstraints.WEST;
			gbc.insets = new Insets(0, 0, 10, 10);
			add(logolabel, gbc);
			
			
			
			
			butt = new JButton("BROWSE");
			butt.setBorderPainted(false); 
			butt.setFocusPainted(false);
			butt.setForeground(Color.darkGray);
			butt.setFont(new Font("Arial", Font.BOLD, 20));
			gbc.gridx = 0;
			gbc.gridy = 1;
			gbc.insets = new Insets(0, 0, 10, 10);
			butt.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseEntered(java.awt.event.MouseEvent evt) {
					butt.setBackground(Color.GREEN);
				}

				public void mouseExited(java.awt.event.MouseEvent evt) {
					butt.setBackground(UIManager.getColor("control"));
				}
			});
			
			butt.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					frame.getContentPane().removeAll();
					frame.getContentPane().add(new ChartsPanel(frame));
					frame.setLayout(new FlowLayout(FlowLayout.LEFT));
					frame.revalidate();
					frame.repaint();
				}
			});
			
			add(butt, gbc);
			
			JButton profileButton = new JButton("My Profile");
			profileButton.setBorderPainted(false); 
			profileButton.setFocusPainted(false);
			profileButton.setForeground(Color.darkGray);
			profileButton.setFont(new Font("Arial", Font.BOLD, 20));
			gbc.gridx = 0;
			gbc.gridy = 2;
			gbc.insets = new Insets(0, 0, 10, 10);
			profileButton.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseEntered(java.awt.event.MouseEvent evt) {
					profileButton.setBackground(Color.GREEN);
				}

				public void mouseExited(java.awt.event.MouseEvent evt) {
					profileButton.setBackground(UIManager.getColor("control"));
				}
			});
			
			profileButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){		
					Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		
					frame.getContentPane().removeAll();
					frame.getContentPane().add(new ProfilePanel(frame,1));
					frame.setLayout(new GridBagLayout());
					frame.setSize(1000,800);	
					frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
					frame.revalidate();
					frame.repaint();
				}
			});
			
			add(profileButton, gbc);
			
			
			
			aboutbutt = new JButton("ABOUT");
			//butt.setAlignmentX(Component.CENTER_ALIGNMENT);
			aboutbutt.setBorderPainted(false); 
			//aboutbutt.setContentAreaFilled(false); 
			aboutbutt.setFocusPainted(false);
			aboutbutt.setForeground(Color.darkGray);
			aboutbutt.setFont(new Font("Arial", Font.BOLD, 30));
			//butt.setOpaque(false);
			gbc.gridx = 0;
			gbc.gridy = 10;
			//gbc.insets = new Insets(0, 0, 0, 0);
			aboutbutt.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseEntered(java.awt.event.MouseEvent evt) {
					aboutbutt.setBackground(Color.GREEN);
				}

				public void mouseExited(java.awt.event.MouseEvent evt) {
					aboutbutt.setBackground(UIManager.getColor("control"));
				}
			});
			aboutbutt.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					frame.getContentPane().removeAll();
					frame.getContentPane().add(new AboutPanel(frame));
					frame.setLayout(new FlowLayout(FlowLayout.LEFT));
					frame.revalidate();
					frame.repaint();
				}
			});
			
			add(aboutbutt, gbc);
			
			
			header = new JLabel("BROWSE");
			header.setFont(new Font("Arial", Font.BOLD, 65));
			header.setForeground(Color.WHITE);
			gbc.gridx = 5;
			gbc.gridy = 0;
			gbc.weighty = 0;
			gbc.weightx = 0;
			gbc.anchor = GridBagConstraints.WEST;
			gbc.insets = new Insets(0, 0, 10, 10);
			add(header, gbc);
			
			
			Cbutt = new JButton("Charts");
			Cbutt.setBorderPainted(false); 
			Cbutt.setFocusPainted(false);
			Cbutt.setForeground(Color.darkGray);
			Cbutt.setFont(new Font("Arial", Font.BOLD, 20));
			gbc.gridx = 5;
			gbc.gridy = 1;
			gbc.insets = new Insets(0, 0, 10, 10);
			Cbutt.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseEntered(java.awt.event.MouseEvent evt) {
					Cbutt.setBackground(Color.GREEN);
				}

				public void mouseExited(java.awt.event.MouseEvent evt) {
					Cbutt.setBackground(UIManager.getColor("control"));
				}
			});
			
			
			Cbutt.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					frame.getContentPane().removeAll();
					frame.getContentPane().add(new ChartsPanel(frame));
					frame.setLayout(new FlowLayout(FlowLayout.LEFT));
					frame.revalidate();
					frame.repaint();
				}
			});
			
			add(Cbutt, gbc);

			
			Gbutt = new JButton("Genres & Moods");
			Gbutt.setBorderPainted(false); 
			Gbutt.setFocusPainted(false);
			Gbutt.setForeground(Color.darkGray);
			Gbutt.setFont(new Font("Arial", Font.BOLD, 20));
			gbc.gridx = 6;
			gbc.gridy = 1;
			gbc.insets = new Insets(0, 0, 10, 10);
			Gbutt.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseEntered(java.awt.event.MouseEvent evt) {
					Gbutt.setBackground(Color.GREEN);
				}

				public void mouseExited(java.awt.event.MouseEvent evt) {
					Gbutt.setBackground(UIManager.getColor("control"));
				}
			});
			
			Gbutt.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					frame.getContentPane().removeAll();
					frame.getContentPane().add(new GenresPanel(frame));
					frame.setLayout(new FlowLayout(FlowLayout.LEFT));
					frame.revalidate();
					frame.repaint();
				}
			});
			
			add(Gbutt, gbc);
			
			
			
			Newbutt = new JButton("New Releases");
			Newbutt.setBorderPainted(false); 
			Newbutt.setFocusPainted(false);
			Newbutt.setForeground(Color.darkGray);
			Newbutt.setFont(new Font("Arial", Font.BOLD, 20));
			gbc.gridx = 7;
			gbc.gridy = 1;
			gbc.insets = new Insets(0, 0, 10, 10);
			Newbutt.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseEntered(java.awt.event.MouseEvent evt) {
					Newbutt.setBackground(Color.GREEN);
				}

				public void mouseExited(java.awt.event.MouseEvent evt) {
					Newbutt.setBackground(UIManager.getColor("control"));
				}
			});
			
			/*
			Newbutt.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					frame.getContentPane().removeAll();
					frame.getContentPane().add();
					revalidate();
				}
			});
			*/
			
			add(Newbutt, gbc);
			
			checkalbum();
			gbc.gridy = 2;
			gbc.gridx = 4;
			for(int i = 1; i <= ai.getAlbumIDarray().size(); i++){
				int n = i - 1;
				String sI = ai.getalbumName(n);
				String s = "Icon/"+ sI + ".jpg" ; 
				
				
				gbc.gridx++;
				
				BufferedImage img = scaleImage(300,300, s); 
				logo = new ImageIcon(img);
				Albumbutt = new JButton(" ");
				Albumbutt.setBorderPainted(false); 
				Albumbutt.setFocusPainted(false); 
				Albumbutt.setContentAreaFilled(false); 
				Albumbutt.setForeground(Color.darkGray);
				Albumbutt.setIcon(logo);
				Albumbutt.setCursor(new Cursor(Cursor.HAND_CURSOR));
				
				
				
				if(gbc.gridx > 7){
					gbc.gridx = 5;
					gbc.gridy++;
				}
				gbc.insets = new Insets(0, 0, 10, 10);

				add(Albumbutt, gbc);
				
				Albumbutt.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
						frame.getContentPane().removeAll();
						frame.setSize(1000,800);
						frame.getContentPane().add(new AlbumPanel(frame, sI));
						frame.setLayout(new GridBagLayout());
						frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);					
						frame.revalidate();
						frame.repaint();
					}
				});
				
				
				
			}
			
			
			
		}
		
		public void checkalbum(){
			ai = new AlbumInfo();
			
			try{
				Connection con = getConnection();
				PreparedStatement statement = con.prepareStatement("SELECT * FROM albums");
				
				ResultSet result = statement.executeQuery();
				
				while(result.next()){
					int albumID = result.getInt("albumID");
					int artistID = result.getInt("artistID");
					String albumstring = result.getString("albumName");
					String albumsSong = result.getString("albumSongs");
					
					ai.addInfo(albumID, artistID, albumstring, albumsSong);
					
					System.out.println("\nAlbum ID: " + albumID + "\nArtist ID: "+ artistID + "\nAlbum Name:" + albumstring + "\nAlbum Songs: "+ albumsSong + "");
				}
				con.close();
				
			}catch(Exception e){System.out.println(e);}
			finally{
				System.out.println("Query Completed");
			}
		}
		
		public BufferedImage scaleImage(int WIDTH, int HEIGHT, String filename) {
			BufferedImage bi = null;
			try {
				ImageIcon ii = new ImageIcon(filename);//path to image
				bi = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
				Graphics2D g2d = (Graphics2D) bi.createGraphics();
				g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY));
				g2d.drawImage(ii.getImage(), 0, 0, WIDTH, HEIGHT, null);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			return bi;
		}
		
		public AlbumInfo ai;
		public int albumid;
	}
	
	class AboutPanel extends JPanel{
		
		private JFrame frame;
		private ImageIcon logo;
		private JLabel logolabel;
		private JLabel header;
		private JLabel teamlogo;
		private JLabel de_team;
		
		private JTextField userText;
		private JTextField passText;
		
		private JButton butt;
		private JButton aboutbutt;
		
		//private GridBagConstraints gbc; 
		
		public AboutPanel(JFrame f)
		{	
			frame = f;
			/*JLabel label = new JLabel("SPOTIFY");
			label.setFont(new Font("Comic sans MS", Font.BOLD, 30));
			label.setAlignmentX(Component.CENTER_ALIGNMENT);
			add(label);*/
			
			GridBagConstraints gbc = new GridBagConstraints();
			
			setLayout(new GridBagLayout());
			setBackground(Color.darkGray);
			
			logo = new ImageIcon(getClass().getResource("archifylogo1.png"));
			logolabel = new JLabel(logo);
			//logolabel.setAlignmentX(Component.CENTER_ALIGNMENT);
			logolabel.setOpaque(false);
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.weighty = 1;
			gbc.weightx = 1;
			//gbc.anchor = GridBagConstraints.WEST;
			gbc.insets = new Insets(0, 0, 0, 20);
			add(logolabel, gbc);
			
			
			
			
			butt = new JButton("BROWSE");
			//butt.setAlignmentX(Component.CENTER_ALIGNMENT);
			butt.setBorderPainted(false); 
			//butt.setContentAreaFilled(false); 
			butt.setFocusPainted(false);
			butt.setForeground(Color.darkGray);
			butt.setFont(new Font("Arial", Font.BOLD, 20));
			//butt.setOpaque(false);
			gbc.gridx = 0;
			gbc.gridy = 1;
			gbc.insets = new Insets(0, 0, 20, 20);
			butt.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseEntered(java.awt.event.MouseEvent evt) {
					butt.setBackground(Color.GREEN);
				}

				public void mouseExited(java.awt.event.MouseEvent evt) {
					butt.setBackground(UIManager.getColor("control"));
				}
			});
			
			butt.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					frame.getContentPane().removeAll();
					frame.getContentPane().add(new ChartsPanel(frame));
					frame.revalidate();
					frame.repaint();
				}
			});
			
			add(butt, gbc);
			
			JButton profileButton = new JButton("My Profile");
			profileButton.setBorderPainted(false); 
			profileButton.setFocusPainted(false);
			profileButton.setForeground(Color.darkGray);
			profileButton.setFont(new Font("Arial", Font.BOLD, 20));
			gbc.gridx = 0;
			gbc.gridy = 2;
			gbc.insets = new Insets(0, 0, 0, 20);
			profileButton.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseEntered(java.awt.event.MouseEvent evt) {
					profileButton.setBackground(Color.GREEN);
				}

				public void mouseExited(java.awt.event.MouseEvent evt) {
					profileButton.setBackground(UIManager.getColor("control"));
				}
			});
			
			profileButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){		
					Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		
					frame.getContentPane().removeAll();
					frame.getContentPane().add(new ProfilePanel(frame, 1));
					frame.setLayout(new GridBagLayout());
					frame.setSize(1000,800);	
					frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
					frame.revalidate();
					frame.repaint();
				}
			});
			
			add(profileButton, gbc);
			
			aboutbutt = new JButton("ABOUT");
			//butt.setAlignmentX(Component.CENTER_ALIGNMENT);
			aboutbutt.setBorderPainted(false); 
			//aboutbutt.setContentAreaFilled(false); 
			aboutbutt.setFocusPainted(false);
			aboutbutt.setForeground(Color.darkGray);
			aboutbutt.setFont(new Font("Arial", Font.BOLD, 30));
			//butt.setOpaque(false);
			gbc.gridx = 0;
			gbc.gridy = 10;
			//gbc.insets = new Insets(0, 0, 0, 0);
			aboutbutt.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseEntered(java.awt.event.MouseEvent evt) {
					aboutbutt.setBackground(Color.GREEN);
				}

				public void mouseExited(java.awt.event.MouseEvent evt) {
					aboutbutt.setBackground(UIManager.getColor("control"));
				}
			});
			aboutbutt.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					frame.getContentPane().removeAll();
					frame.getContentPane().add(new AboutPanel(frame));
					frame.revalidate();
				}
			});
			add(aboutbutt, gbc);
			
			
			
			
			
			header = new JLabel("ABOUT");
			header.setFont(new Font("Arial", Font.BOLD, 65));
			header.setForeground(Color.WHITE);
			gbc.gridx = 5;
			gbc.gridy = 0;
			gbc.weighty = 0;
			gbc.weightx = 0;
			gbc.anchor = GridBagConstraints.PAGE_START;
			gbc.insets = new Insets(20, 0, 20, 0);
			add(header, gbc);
			
			logo = new ImageIcon(getClass().getResource("team logo.png"));
			teamlogo = new JLabel(logo);
			//teamlogo.setAlignmentX(Component.CENTER_ALIGNMENT);
			teamlogo.setOpaque(false);
			gbc.gridx = 7;
			gbc.gridy = 2;
			gbc.weighty = 0;
			gbc.weightx = 0;
			add(teamlogo, gbc);
			
			
			de_team = new JLabel("ROGUE ONE");
			de_team.setFont(new Font("Arial", Font.BOLD, 40));
			de_team.setForeground(Color.WHITE);
			gbc.gridx = 7;
			gbc.gridy = 3;
			gbc.weighty = 0;
			gbc.weightx = 0;
			gbc.anchor = GridBagConstraints.PAGE_START;
			gbc.insets = new Insets(20, 0, 0, 0);
			add(de_team, gbc);
			
			de_team = new JLabel("\u00a9 CHONG, GUANSING, LABORADA, RAYMUNDO");
			de_team.setFont(new Font("Arial", Font.BOLD, 20));
			de_team.setForeground(Color.WHITE);
			gbc.gridx = 7;
			gbc.gridy = 4;
			gbc.weighty = 0;
			gbc.weightx = 0;
			gbc.anchor = GridBagConstraints.PAGE_START;
			gbc.insets = new Insets(0, 0, 0, 0);
			add(de_team, gbc);
			
			
			
			//testFrame();
		
			
			
			
		}
	}
	
	class AlbumPanel extends JPanel{
		
		private JFrame frame;
		private ImageIcon logo;
		private JLabel logolabel;
		private JLabel header;
		private JLabel teamlogo;
		private JLabel de_team;
		
		private JTextField userText;
		private JTextField passText;
		
		private JButton butt;
		private JButton aboutbutt;
		
		//private GridBagConstraints gbc; 
		
		public AlbumPanel(JFrame f, String albumString)
		{	
			num = 0;
			frame = f;
			int n = 14; // n is the number of songs;
			
			checkalbum(albumString);
			
			GridBagConstraints gbc = new GridBagConstraints();
			
			setLayout(new GridBagLayout());
			setBackground(Color.darkGray);
			/*
			logo = new ImageIcon(getClass().getResource("archifylogo1.png"));
			logolabel = new JLabel(logo);
			//logolabel.setAlignmentX(Component.CENTER_ALIGNMENT);
			logolabel.setOpaque(false);
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.weighty = 1;
			gbc.weightx = 1;
			//gbc.anchor = GridBagConstraints.WEST;
			gbc.insets = new Insets(0, 0, 0, 20);
			
			*/
			
			String AL = "Icon/" + albumName + ".jpg";
			BufferedImage img = scaleImage(300,300, AL); //albumName is the name of the album image
			logo = new ImageIcon(img);
			logolabel = new JLabel(logo); 
			//logolabel.setAlignmentX(Component.CENTER_ALIGNMENT);
			logolabel.setOpaque(false);
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.weighty = 1;
			gbc.weightx = 1;
			//gbc.anchor = GridBagConstraints.WEST;
			gbc.insets = new Insets(0, 0, 20, 0);
			add(logolabel, gbc);
			
			logolabel = new JLabel ("" + albumName); //ALBUM NAME
			logolabel.setOpaque(false);
			logolabel.setForeground(Color.white);
			logolabel.setFont(new Font("Arial", Font.BOLD, 20));
			gbc.gridx = 0;
			gbc.gridy = 1;
			gbc.weighty = 1;
			gbc.weightx = 1;
			gbc.insets = new Insets(0, 0, 40, 0);
			add(logolabel, gbc);
			
			logolabel = new JLabel ("By " + artistName);
			logolabel.setOpaque(false);
			logolabel.setForeground(Color.white);
			logolabel.setFont(new Font("Arial", Font.PLAIN, 16));
			gbc.gridx = 0;
			gbc.gridy = 2;
			gbc.weighty = 1;
			gbc.weightx = 1;
			gbc.insets = new Insets(0, 0, 0, 0);
			add(logolabel, gbc);
			
			
			checkSongs();
			
			//PUT SONG COUNT HERE
			logolabel = new JLabel ("" + songArray.length + " Songs");
			logolabel.setOpaque(false);
			logolabel.setForeground(Color.white);
			logolabel.setFont(new Font("Arial", Font.PLAIN, 16));
			gbc.gridx = 0;
			gbc.gridy = 3;
			gbc.weighty = 1;
			gbc.weightx = 1;
			gbc.insets = new Insets(0, 0, 20, 0);
			add(logolabel, gbc);
			
			
			
			JButton butt = new JButton("PLAY");
			//butt.setOpaque(false);
			gbc.gridx = 0;
			gbc.gridy = 10;
			gbc.insets = new Insets(10, 0, 5, 0);
			add(butt, gbc);
			
			
			
			JTextField playlisttext = new JTextField(15);
			gbc.gridx = 1;
			gbc.gridy = 10;
			gbc.insets = new Insets(0, 0, 5, 0);
			add(playlisttext, gbc);
			
			JButton butt5 = new JButton("ADD SONG TO PLAYLIST");
			//butt.setOpaque(false);
			gbc.gridx = 1;
			gbc.gridy = 11;
			gbc.insets = new Insets(0, 0, 5, 0);
			add(butt5, gbc);
			
		
			
		
			
				try{
					DefaultListModel<String> songname = new DefaultListModel<>();
					
					songs = getSongName();//ARRAY
					int n1;
					for(int i = 0; i < songs.size(); i++){
						n1 = i+1;
						songname.addElement(n1 + ".) " + songs.get(i));// <------ eto 
					}
					  
					
					JList<String> songList = new JList<>(songname); //<------- tapos eto
					songList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
					songList.setSelectedIndex(0);
					songList.setVisibleRowCount(7); 
					songList.setFont(new Font("Arial", Font.PLAIN, 25));
					
					JScrollPane songListScrollPane = new JScrollPane(songList); // tapos eto
					gbc.gridx = 1;
					gbc.gridy = 0;
			
					gbc.insets = new Insets(0, 50, 0, 0);

					add(songListScrollPane, gbc);
					
					
					/*ACTION LISTENER FOR ADD TO PLAYLIST BUTTON*/
					butt5.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent e){	
							String str = songList.getSelectedValue();
							playlistName = playlisttext.getText();
							songTitle = str.substring(4,str.length());
							System.out.println(songTitle);
							checkSelectedSong();
							insertplaylisttodb();			
						}
					});
					
					
					butt.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent e){
							if(num == 0){
								String str = songList.getSelectedValue();
								selectedSong = str.substring(4,str.length());
								mp3player = new MP3(selectedSong, artistName);
								mp3player.start();
								num = 1;
								checkplaycount();
								playcount++;
								addplaycount();
								butt.setText("STOP");
							}
							
							else if(num == 1){
								mp3player.pause();
								num = 0;
								butt.setText("PLAY");
							}

						}
						
					});
					
					
					
					
				} catch(Exception e){
					System.out.println(e);
				}
				
				
			JButton returnbutt = new JButton("Back");
			//butt.setOpaque(false);
			gbc.gridx = 2;
			gbc.gridy = 0;
			gbc.insets = new Insets(35, 0, 5, 0);
			returnbutt.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					if(mp3player != null)
						mp3player.pause();
					
					
					Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
					
					JScrollPane scrollPane = new JScrollPane(new NewRePanel(frame));
					scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
					scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
					scrollPane.setPreferredSize(new Dimension(1300, 800));
					//scrollPane.setBounds(1, 1, 800, 800);
					//scrollPane.setBorderPainted(false);
					scrollPane.getVerticalScrollBar().setUnitIncrement(16);
					scrollPane.setOpaque(false);

					frame.getContentPane().removeAll();
					frame.getContentPane().add(scrollPane);
					frame.setLayout(new FlowLayout(FlowLayout.LEFT));
					frame.setSize(1300,900);
					frame.pack();
					frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);					
					frame.revalidate();
					frame.repaint();
				}
			});
			add(returnbutt, gbc);
					
				
		}
		
		public void checkplaycount(){
			try{
				
				Connection con = getConnection();
				
				String s = makesqlString();
				System.out.println(s);
				PreparedStatement statement = con.prepareStatement("select playcount from songs where songTitle = '"+ selectedSong + "'  AND artistID = " + artistID + "");
				System.out.println("select songID from songs where songTitle = '"+ selectedSong + "'");
				ResultSet result = statement.executeQuery();
				
				while(result.next()){
					     playcount = result.getInt(1);
				}
				con.close();
				
				System.out.println("playcount: " + playcount);
				
			}catch(Exception e){System.out.println(e);}
			finally{
				System.out.println("Query Completed");
			}
		}
		
		public void addplaycount(){
			try{
				
				Connection con = getConnection();
				
				String s = makesqlString();
				System.out.println(s);
				PreparedStatement posted = con.prepareStatement("UPDATE songs SET playcount = "+playcount +" where songTitle = '"+selectedSong+"' and artistID = "+ artistID);
				System.out.println("UPDATE songs SET playcount = "+playcount +" where songTitle = '"+selectedSong+"' and artistID = "+ artistID);
				
				posted.executeUpdate();
				
				//System.out.println("playcount: " + playcount);
				con.close();
			}catch(Exception e){System.out.println(e);}
			finally{
				System.out.println("Update Completed");
			}
		}
		
		
		public void checkSelectedSong(){
			try{
				
				Connection con = getConnection();
				
				String s = makesqlString();
				System.out.println(s);
				PreparedStatement statement = con.prepareStatement("select songID from songs where songTitle = '"+ songTitle + "'");
				System.out.println("select songID from songs where songTitle = '"+ songTitle + "'");
				ResultSet result = statement.executeQuery();
				
				while(result.next()){
					     songID = result.getInt("songID");
				}
				con.close();
				
			}catch(Exception e){System.out.println(e);}
			finally{
				System.out.println("Query Completed");
			}
		}
		
		
		
		public String makesqlString(){
			String s = "";
			for(int i = 0; i < songArray.length; i++){			
				s +=  "songID = "+ songArray[i] + "";
				if(i == songArray.length - 1){
					
				} else{
					s += " or ";
					
				}
			}
			
			return s;
		}
		
		public ArrayList<String> getSongName(){
			
			songs = new ArrayList<>(); 
			try{
				
				Connection con = getConnection();
				
				String s = makesqlString();
				System.out.println(s);
				PreparedStatement statement = con.prepareStatement("select songTitle from songs where "+ s + "");
				
				ResultSet result = statement.executeQuery();
				
				while(result.next()){
					     songs.add(result.getString(1)); 
				}
				con.close();
				
			}catch(Exception e){System.out.println(e);}
			finally{
				System.out.println("Query Completed");
			}
			
			return songs;
		}
		
		public void checkSongs(){
			
			String[] parts = albumSongs.split(",");
			int[] n1 = new int[parts.length];
			for(int n = 0; n < parts.length; n++) {
				n1[n] = Integer.parseInt(parts[n]);
				System.out.println("Number" + n +": " + n1[n]+ "");
				
			}
			
			songArray = n1;
			
			
		}
		
		public BufferedImage scaleImage(int WIDTH, int HEIGHT, String filename) {
			BufferedImage bi = null;
			try {
				ImageIcon ii = new ImageIcon(filename);//path to image
				bi = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
				Graphics2D g2d = (Graphics2D) bi.createGraphics();
				g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY));
				g2d.drawImage(ii.getImage(), 0, 0, WIDTH, HEIGHT, null);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			return bi;
		}
		
		public void checkalbum(String a){
			
			try{
				Connection con = getConnection();
				PreparedStatement statement = con.prepareStatement("SELECT * FROM albums a, artist ai where a.albumName =  '"+ a + "' AND a.artistID  = ai.artistID");
				System.out.println("SELECT * FROM albums a, artist ai where a.albumName =  '"+ a + "' AND a.artistID  = ai.artistID");
				ResultSet result = statement.executeQuery();
				
				while(result.next()){

					
					albumName = result.getString("albumName");
					albumID = result.getInt("albumID");
		
					artistID = result.getInt("artistID");
					artistName = result.getString("artistName");//in artist table
		
					albumSongs = result.getString("albumSongs");
					      
					System.out.println("\nAlbum Name: " + albumName + "\nalbumID: "+ albumID + "\nartistID: " + artistID + "\nartistName: "+ artistName + "\nalbumSongs: " + albumSongs);
				}
				con.close();
				
			}catch(Exception e){System.out.println(e);}
			finally{
				System.out.println("Query Completed");
			}
		}
		
		public boolean checkPlaylist(){
			try{
				Connection con = getConnection();
				PreparedStatement statement = con.prepareStatement("SELECT max(playlistID) FROM playlists");
				System.out.println("SELECT max(playlistID) FROM playlists");
				
				ResultSet result = statement.executeQuery();
				
				while(result.next()){
					playlistID = result.getInt(1) + 1;  
					
				}
				con.close();
				
				if(playlistID == 0)
					playlistID = 1;
				
			}catch(Exception e){System.out.println(e);}
			finally{
				System.out.println("Query Completed playlistID");
			}
			
			
			
			try{
				Connection con = getConnection();
				PreparedStatement statement = con.prepareStatement("SELECT playlistID, playlistSongs FROM playlists p where p.userID =  "+getUserID()+" AND p.playlistName  = '"+playlistName+"'");
				System.out.println("SELECT playlistID, playlistSongs FROM playlist p where p.userID =  "+getUserID()+" AND p.playlistName  = '"+playlistName+"'");
				ResultSet result = statement.executeQuery();
				
				while(result.next()){

					
					playlistID = result.getInt("playlistID");  
					playlistSongs = result.getString("playlistSongs");
					
					return false; //IT IS NOT NEW
				}
				con.close();
				
			}catch(Exception e){System.out.println(e);}
			finally{
				System.out.println("Query Completed");
			}
			
			return true; //IT IS NEW
		}
		
		public void insertplaylisttodb(){
			if(checkPlaylist()){
				try{ 
					System.out.println("--=-=-=-=-=-=-=-=-=-=-=-=-==-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
					Connection con = getConnection();
					PreparedStatement posted = con.prepareStatement("INSERT INTO playlists (userID, playlistID, playlistName, playlistSongs) VALUES ("+getUserID()+", '"+playlistID+"', '"+playlistName+"', '"+ songID+ "')");
					System.out.println("INSERT INTO playlists (userID, playlistID, playlistName, playlistSongs) VALUES ("+getUserID()+", '"+playlistID+"', "+playlistName+", '"+ playlistSongs + "')");
					posted.executeUpdate();
					con.close();
				}catch(Exception e){System.out.println(e);}
				finally{
					System.out.println("Insert Completed");
				}
			}
			
			else{
			try{
					Connection con = getConnection();
					PreparedStatement posted = con.prepareStatement("UPDATE playlists SET playlistSongs = '" +playlistSongs + ","+  songID +"' where userID =  "+getUserID()+" AND playlistName  = '"+playlistName+"'");
					System.out.println("UPDATE playlists SET playlistSongs = '" +playlistSongs + ","+  songID +"' where userID =  "+getUserID()+" AND playlistName  = '"+playlistName+"'");
					
					posted.executeUpdate();
					con.close();
				}catch(Exception e){System.out.println(e);}
				finally{
					System.out.println("Update Completed");
				}
			}
		}
		
		private String albumName;
		private int albumID;
		
		private int artistID;
		private String artistName;//in artist table
		
		private String albumSongs;
		private ArrayList<String> songs;
		private int[] songArray;
		
		private String songTitle;
		private int songID;
		
		
		private String playlistName;
		private int playlistID;
		private String playlistSongs = "";
		
		private String selectedSong;
		private MP3 mp3player;
		private int playcount;
		private int num = 0;
		
		
		
	}
	
	class ProfilePanel extends JPanel{
		
		private JFrame frame;
		private ImageIcon logo;
		private JLabel logolabel;
		private JLabel header;
		private JLabel teamlogo;
		private JLabel de_team;
		
		private JTextField userText;
		private JTextField passText;
		
		private JButton butt;
		private JButton aboutbutt;
		
		//private GridBagConstraints gbc; 
		
		public ProfilePanel(JFrame f, int n)
		{	
			frame = f;
			
			index = n - 1;
			mainindex = n;
			
			GridBagConstraints gbc = new GridBagConstraints();
			
			setLayout(new GridBagLayout());
			setBackground(Color.darkGray);
			
			checkMyPlaylist();
			if(playlistID.size() != 0)
					checkSongs();
			
			
			logo = new ImageIcon(getClass().getResource("archifylogo1.png"));
			logolabel = new JLabel(logo);
			//logolabel.setAlignmentX(Component.CENTER_ALIGNMENT);
			logolabel.setOpaque(false);
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.weighty = 1;
			gbc.weightx = 1;
			//gbc.anchor = GridBagConstraints.WEST;
			gbc.insets = new Insets(0, 0, 0, 20);
			add(logolabel, gbc);
			
			butt = new JButton("BROWSE");
			butt.setBorderPainted(false); 
			butt.setFocusPainted(false);
			butt.setForeground(Color.darkGray);
			butt.setFont(new Font("Arial", Font.BOLD, 20));
			gbc.gridx = 0;
			gbc.gridy = 1;
			gbc.insets = new Insets(0, 0, 0, 20);
			butt.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseEntered(java.awt.event.MouseEvent evt) {
					butt.setBackground(Color.GREEN);
				}

				public void mouseExited(java.awt.event.MouseEvent evt) {
					butt.setBackground(UIManager.getColor("control"));
				}
			});
			
			butt.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

					frame.getContentPane().removeAll();
					frame.setSize(1300,900);
					frame.getContentPane().add(new ChartsPanel(frame));
					frame.setLayout(new FlowLayout(FlowLayout.LEFT));
					frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
					frame.revalidate();
					frame.repaint();
				}
			});
			
			add(butt, gbc);
			
			checkMyPlaylist();
			
			header = new JLabel("MY PLAYLIST");
			header.setForeground(Color.WHITE);
			header.setFont(new Font("Arial", Font.BOLD, 30));
			gbc.gridx = 2;
			gbc.gridy = 0;
			gbc.insets = new Insets(0, 0, 0, 0);
			add(header, gbc);
			
			JButton prevbutt = new JButton("<");
			prevbutt.setBorderPainted(false); 
			prevbutt.setFocusPainted(false);
			prevbutt.setForeground(Color.darkGray);
			prevbutt.setFont(new Font("Arial", Font.BOLD, 20));
			gbc.gridx = 1;
			gbc.gridy = 1;
			gbc.insets = new Insets(0, 0, 0, 0);
			prevbutt.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseEntered(java.awt.event.MouseEvent evt) {
					prevbutt.setBackground(Color.GREEN);
				}

				public void mouseExited(java.awt.event.MouseEvent evt) {
					prevbutt.setBackground(UIManager.getColor("control"));
				}
			});
			
			prevbutt.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){		
					if(mainindex == 1)
						mainindex = playlistID.size();
					
					else{
						mainindex--;
					}
						
					
					
					frame.getContentPane().removeAll();
					frame.getContentPane().add(new ProfilePanel(frame,mainindex));
					frame.setLayout(new GridBagLayout());
					frame.setSize(1000,800);	
					frame.revalidate();
					frame.repaint();

				}
			});
			
			add(prevbutt, gbc);
			
			JLabel PlaylistCount;
			if(playlistID.size() != 0)
				PlaylistCount = new JLabel(playlistName.get(index));
			else
				PlaylistCount = new JLabel("No Playlist");
			PlaylistCount.setForeground(Color.WHITE);
			PlaylistCount.setFont(new Font("Arial", Font.BOLD, 16));
			gbc.gridx = 2;
			gbc.gridy = 1;
			gbc.insets = new Insets(0, 0, 0, 0);
			add(PlaylistCount, gbc);
			
	
			
			JButton nextbutt = new JButton(">");
			nextbutt.setBorderPainted(false); 
			nextbutt.setFocusPainted(false);
			nextbutt.setForeground(Color.darkGray);
			nextbutt.setFont(new Font("Arial", Font.BOLD, 20));
			gbc.gridx = 3;
			gbc.gridy = 1;
			gbc.insets = new Insets(0, 0, 0, 0);
			nextbutt.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseEntered(java.awt.event.MouseEvent evt) {
					nextbutt.setBackground(Color.GREEN);
				}

				public void mouseExited(java.awt.event.MouseEvent evt) {
					nextbutt.setBackground(UIManager.getColor("control"));
				}
			});
			
			nextbutt.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					if(mainindex == playlistID.size())
						mainindex = 1;
					
					else{
						mainindex++;
					}
						
					frame.getContentPane().removeAll();
					frame.getContentPane().add(new ProfilePanel(frame,mainindex));
					frame.setLayout(new GridBagLayout());
					frame.setSize(1000,800);	
					frame.revalidate();
					frame.repaint();
					
				}
			});
			
			add(nextbutt, gbc);
			
			if(playlistID.size() != 0){
				try{
					DefaultListModel<String> songname = new DefaultListModel<>();
					//ArrayList<String> playlist = scanlist();
					songs = getSongName();
						
					for(int i = 0; i < songs.size(); i++){
						int ni = i+1;
						songname.addElement(ni + ".) "+ songs.get(i) + "");
					}
						
						
					JList<String> songList = new JList<>(songname);
					songList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
					songList.setSelectedIndex(0);
					songList.setVisibleRowCount(7); 
					songList.setFont(new Font("Arial", Font.PLAIN, 25));
						
					JScrollPane songListScrollPane = new JScrollPane(songList);
					gbc.gridx = 2;
					gbc.gridy = 2;
				
					gbc.insets = new Insets(0, 0, 0, 0);

					add(songListScrollPane, gbc);
					//DELETE PLAYLIST
					JButton deletebutt = new JButton("Delete Playlist");
					deletebutt.setBorderPainted(false); 
					deletebutt.setFocusPainted(false);
					deletebutt.setForeground(Color.darkGray);
					deletebutt.setFont(new Font("Arial", Font.BOLD, 20));
					gbc.gridx = 0;
					gbc.gridy = 4;
					gbc.insets = new Insets(0, 0, 0, 0);
					deletebutt.addMouseListener(new java.awt.event.MouseAdapter() {
						public void mouseEntered(java.awt.event.MouseEvent evt) {
							deletebutt.setBackground(Color.GREEN);
							}
						public void mouseExited(java.awt.event.MouseEvent evt) {
							deletebutt.setBackground(UIManager.getColor("control"));
						}
					});
					deletebutt.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent e){
							
							deleteplaylist();
							mainindex = 1;
							frame.getContentPane().removeAll();
							frame.getContentPane().add(new ProfilePanel(frame,mainindex));
							frame.setLayout(new GridBagLayout());
							frame.setSize(1000,800);	
							frame.revalidate();
							frame.repaint();
							
						}
					});
					
					add(deletebutt, gbc);
					
					//DELETE Songs
					JButton deleteSbutt = new JButton("Delete Song");
					deleteSbutt.setBorderPainted(false); 
					deleteSbutt.setFocusPainted(false);
					deleteSbutt.setForeground(Color.darkGray);
					deleteSbutt.setFont(new Font("Arial", Font.BOLD, 20));
					gbc.gridx = 0;
					gbc.gridy = 5;
					gbc.insets = new Insets(0, 0, 0, 0);
					deleteSbutt.addMouseListener(new java.awt.event.MouseAdapter() {
						public void mouseEntered(java.awt.event.MouseEvent evt) {
							deleteSbutt.setBackground(Color.GREEN);
							}
						public void mouseExited(java.awt.event.MouseEvent evt) {
							deleteSbutt.setBackground(UIManager.getColor("control"));
						}
					});
						deleteSbutt.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent e){		
							deletesong(playlistSongs.get(index), songList.getSelectedIndex());

							frame.getContentPane().removeAll();
							frame.getContentPane().add(new ProfilePanel(frame,mainindex));
							frame.setLayout(new GridBagLayout());
							frame.setSize(1000,800);	
							frame.revalidate();
							frame.repaint();
							
						}
					});
					
					add(deleteSbutt, gbc);
					
				} catch(Exception e){
					System.out.println(e);
				}					
			}		
				
		}
		
		
		public void deletesong(String songStrings, int songindex){
			
			System.out.println("");
			System.out.println("");
			System.out.println("");
			System.out.println("");
			String[] parts = playlistSongs.get(index).split(",");
			int[] n1 = new int[parts.length];
			String s = "";
			for(int n = 0; n < parts.length; n++) {
				n1[n] = Integer.parseInt(parts[n]);
				
				if(songindex != n && songindex == parts.length - 1 && n == parts.length - 2)
					s += n1[n]+ "";
				
				
				else if(songindex != n && n == parts.length - 1)
					s += n1[n]+ "";
				
				else if(songindex != n)
					s += n1[n] + ",";
				
			}
			System.out.println("" + s);
			System.out.println("");
			System.out.println("");
			System.out.println("");
			
			try{
					Connection con = getConnection();
					PreparedStatement posted = con.prepareStatement("UPDATE playlists SET playlistSongs = '" + s +"' where userID =  "+getUserID()+" AND playlistID  = "+playlistID.get(index)+"");
					System.out.println("UPDATE playlists SET playlistSongs = '" + s +"' where userID =  "+getUserID()+" AND playlistID  = "+playlistID.get(index)+"");
					
					posted.executeUpdate();
					con.close();
				}catch(Exception e){System.out.println(e);}
				finally{
					System.out.println("Update Completed");
				}
			
			
		}
		
		public void deleteplaylist(){
			try{
				Connection con = getConnection();
				PreparedStatement statement = con.prepareStatement("delete from playlists where playlistID = " + playlistID.get(index) +"");
				System.out.println("delete from playlists where playlistID = " + playlistID.get(index) +"");
				statement.execute();
				con.close();
			}catch(Exception e){System.out.println(e);}
			finally{
				System.out.println("Delete Completed");
			}
		}
		
		public void checkMyPlaylist(){
			
			userID = new ArrayList<>();
			playlistID = new ArrayList<>();
			playlistName = new ArrayList<>();
			playlistSongs = new ArrayList<>();
			
			try{
				Connection con = getConnection();
				PreparedStatement statement = con.prepareStatement("SELECT * FROM playlists where  userID = "+getUserID()+"");
				System.out.println("SELECT * FROM playlists where  userID = "+getUserID()+"");
				ResultSet result = statement.executeQuery();
				
				while(result.next()){
					userID.add(result.getInt("userID"));
					playlistID.add(result.getInt("playlistID"));
					playlistName.add(result.getString("playlistName"));
					playlistSongs.add(result.getString("playlistSongs"));
					 
					//System.out.println("\nuserID: " + userID + "\nplaylistID: " + playlistID + "\nplaylistName: " + playlistName+ "\nplaylistSongs: " + playlistSongs);
				}
				con.close();
				
			}catch(Exception e){System.out.println(e);}
			finally{
				System.out.println("Query Completed");
			}
					
					
		}
		
		public ArrayList<String> scanlist(){
			try{
			fSC = new Scanner(new File("songlist1.txt"));
			}
			catch(Exception e){
				System.out.println("file not found");
			}
			ArrayList<String> playlist = new ArrayList<String>();
			
			while(fSC.hasNext())
				playlist.add(fSC.nextLine());
			
			return playlist;
		}
		
		public void checkSongs(){
			
			String[] parts = playlistSongs.get(index).split(",");
			int[] n1 = new int[parts.length];
			for(int n = 0; n < parts.length; n++) {
				n1[n] = Integer.parseInt(parts[n]);
				System.out.println("Number" + n +": " + n1[n]+ "");
				
			}
			
			songArray = n1;
				
		}
		
		public String makesqlString(int i){
			String s = "";	
				s +=  "songID = "+ songArray[i] + "";
			
			return s;
		}
		
		public ArrayList<String> getSongName(){
			
			songs = new ArrayList<>(); 
			
			for(int i = 0; i< songArray.length; i++){
				try{
					
					Connection con = getConnection();
					
					String s = makesqlString(i);
					System.out.println(s);
					PreparedStatement statement = con.prepareStatement("select songTitle from songs where "+ s + "");
					
					ResultSet result = statement.executeQuery();
					
					while(result.next()){
							 songs.add(result.getString(1)); 
					}
					con.close();
					
				}catch(Exception e){System.out.println(e);}
				finally{
					System.out.println("Query Completed");
				}
			}
			
			return songs;
		}
		
		private ArrayList<Integer> userID;
		private ArrayList<Integer> playlistID;
		private ArrayList<String> playlistName;
		private ArrayList<String> playlistSongs;
		
		private ArrayList<String> songs;
		
		private int[] songArray;
		
		private int index;
		private int mainindex;
		

		
		private Scanner fSC;
	}
	
	class SignupPanel extends JPanel{
		private JFrame frame;
		private ImageIcon logo;
		private JLabel logolabel;
		private JLabel header;
		private JLabel teamlogo;
		private JLabel de_team;
		
		private JTextField userText;
		private JTextField passText;
		
		private JButton butt;
		private JButton aboutbutt;
		
		
		public SignupPanel(JFrame f)
		{	
			frame = f;
			
			GridBagConstraints gbc = new GridBagConstraints();
			
			setLayout(new GridBagLayout());
			setBackground(Color.darkGray);
			
			logo = new ImageIcon(getClass().getResource("archifylogo1.png"));
			logolabel = new JLabel(logo);
			//logolabel.setAlignmentX(Component.CENTER_ALIGNMENT);
			logolabel.setOpaque(false);
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.insets = new Insets(0, 0, 0, 20);
			add(logolabel, gbc);
			
			
			
			logolabel = new JLabel ("WELCOME TO ARCHIFY ");
			logolabel.setOpaque(false);
			logolabel.setForeground(Color.white);
			logolabel.setFont(new Font("Arial", Font.BOLD, 20));
			gbc.gridx = 1;
			gbc.gridy = 0;
			gbc.insets = new Insets(0, 0, 40, 0);
			add(logolabel, gbc);
			
			logolabel = new JLabel ("Username: ");
			logolabel.setOpaque(false);
			logolabel.setForeground(Color.white);
			logolabel.setFont(new Font("Arial", Font.PLAIN, 16));
			gbc.gridx = 0;
			gbc.gridy = 2;
			gbc.insets = new Insets(5, 0, 5, 0);
			add(logolabel, gbc);
			
			
			Font font1 = new Font("SansSerif", Font.BOLD, 20);
			
			
			JTextField userText = new JTextField(20);
			gbc.gridx = 1;
			gbc.gridy = 2;
			userText.setFont(font1);
			//gbc.weighty = 1.0;
			gbc.insets = new Insets(5, 0, 5, 0);
			add(userText, gbc);
			
			
			logolabel = new JLabel ("Email: ");
			logolabel.setOpaque(false);
			logolabel.setForeground(Color.white);
			logolabel.setFont(new Font("Arial", Font.PLAIN, 16));
			gbc.gridx = 0;
			gbc.gridy = 3;
			gbc.insets = new Insets(5, 0, 5, 0);
			add(logolabel, gbc);
			
			JTextField emailText = new JTextField(20);
			gbc.gridx = 1;
			gbc.gridy = 3;
			emailText.setFont(font1);
			//gbc.weighty = 1.0;
			gbc.insets = new Insets(5, 0, 5, 0);
			add(emailText, gbc);
			
			logolabel = new JLabel ("Password: ");
			logolabel.setOpaque(false);
			logolabel.setForeground(Color.white);
			logolabel.setFont(new Font("Arial", Font.PLAIN, 16));
			gbc.gridx = 0;
			gbc.gridy = 4;
			gbc.insets = new Insets(5, 0, 5, 0);
			add(logolabel, gbc);
			
			JPasswordField passText = new JPasswordField(20);
			gbc.gridx = 1;
			gbc.gridy = 4;
			passText.setFont(font1);
			//gbc.weighty = 1.0;
			gbc.insets = new Insets(5, 0, 5, 0);
			add(passText, gbc);
			
			logolabel = new JLabel ("Birthday: ");
			logolabel.setOpaque(false);
			logolabel.setForeground(Color.white);
			logolabel.setFont(new Font("Arial", Font.PLAIN, 16));
			gbc.gridx = 0;
			gbc.gridy = 5;
			gbc.insets = new Insets(5, 0, 5, 0);
			add(logolabel, gbc);
			
			
			JTextField dateText = new JTextField("MM/dd/yyyy",20);
			gbc.gridx = 1;
			gbc.gridy = 5;
			dateText.setFont(font1);
			//gbc.weighty = 1.0;
			gbc.insets = new Insets(5, 0, 5, 0);
			add(dateText, gbc);
			
			JButton butt = new JButton("Join Archify");
			//butt.setOpaque(false);
			gbc.gridx = 1;
			gbc.gridy = 10;
			gbc.insets = new Insets(5, 0, 5, 0);
			add(butt, gbc);
			
			frame.getRootPane().setDefaultButton(butt);
			
			butt.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					try{
						username = userText.getText();
						email = emailText.getText();
						String pass = String.valueOf(passText.getPassword());
						MessageDigest md = MessageDigest.getInstance("SHA-256");
						md.update(pass.getBytes(), 0, pass.length());
						password = new BigInteger(1, md.digest()).toString(16);
					}catch(Exception xd){
						System.out.println(xd);
					}
					
					
					//System.out.println(password + " HERE");
					Birthday = dateText.getText();
					ConvertdateString();
					
					if(checkusername(username)){//checkusername(username)
						inserttodb();

						Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
						frame.getContentPane().removeAll();
						frame.setSize(800,650);
						frame.getContentPane().add(new LoginPanel(frame));
						//frame.setLayout(new FlowLayout(FlowLayout.LEFT));
						frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
						frame.revalidate();
						frame.repaint();
					}
				}
			});
			
			JButton backbutt = new JButton("Cancel");
			//butt.setOpaque(false);
			gbc.gridx = 1;
			gbc.gridy = 11;
			gbc.insets = new Insets(5, 0, 5, 0);
			add(backbutt, gbc);
			
			backbutt.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
					frame.getContentPane().removeAll();
					frame.setSize(800,650);
					frame.getContentPane().add(new LoginPanel(frame));
					//frame.setLayout(new FlowLayout(FlowLayout.LEFT));
					frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
					frame.revalidate();
					frame.repaint();
				}
			});
			
			check();
		}
		
		public boolean checkusername(String username1){
			try{
				Connection con = getConnection();
				PreparedStatement statement = con.prepareStatement("SELECT * FROM user where userUsername = '"+ username1 +"'");
				
				ResultSet result = statement.executeQuery();
				
				while(result.next()){
					System.out.println("Username is taken");
					JOptionPane.showMessageDialog(frame, "Username is taken");
					return false; //USERNAME IS TAKEN
				}
				con.close();
				
			}catch(Exception e){System.out.println(e);}
			finally{
				System.out.println("Query Completed");
				System.out.println("*****Username is taken****");
			}
			
			return true;// username is not taken
		}
		
		
		public void ConvertdateString(){
			DateFormat df = new SimpleDateFormat("MM/dd/yyyy"); 
			java.util.Date startDate = null;
			try {
				startDate = df.parse(Birthday);
				String Birthday2 = df.format(startDate);
				System.out.println(Birthday2);
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(frame, "Invalid date! Follow MM/dd/yyyy format");
			}
			
			BirthdayDate = new java.sql.Date(startDate.getTime());
			System.out.println("SQL DATE "+BirthdayDate);
		}
		
		public void inserttodb(){
			try{
				Connection con = getConnection();
				PreparedStatement posted = con.prepareStatement("INSERT INTO user (userID,userUsername, userEmail, userPassword, userBirthday) VALUES ('"+userID+"', '"+username+"', '"+email+"', '"+password+"', '"+BirthdayDate+"')");
				
				posted.executeUpdate();
				con.close();
			}catch(Exception e){System.out.println(e);}
			finally{
				System.out.println("Insert Completed");
			}
		}
		
		public void check(){
			try{
				Connection con = getConnection();
				PreparedStatement statement = con.prepareStatement("SELECT max(userID) FROM user");
				
				ResultSet result = statement.executeQuery();
				
				while(result.next()){
					userID = result.getInt(1) + 1;
					System.out.println(userID + " ");
				}
				con.close();
				
			}catch(Exception e){System.out.println(e);}
			finally{
				System.out.println("Query Completed");
			}
		}
		
		private String username;
		private String email;
		private String password;
		private String Birthday;
		private java.sql.Date BirthdayDate;
		private int userID = 001;
		
		
	}
	
	class AdminPanel extends JPanel{
		private JFrame frame;
		private ImageIcon logo;
		private JLabel logolabel;
		private JLabel header;
		private JLabel teamlogo;
		private JLabel de_team;
		
		private JTextField userText;
		private JTextField passText;
		
		private JButton butt;
		private JButton aboutbutt;
		
		
		public AdminPanel(JFrame f)
		{	
			frame = f;
			
			GridBagConstraints gbc = new GridBagConstraints();
			
			setLayout(new GridBagLayout());
			setBackground(Color.darkGray);
			
			logo = new ImageIcon(getClass().getResource("archifylogo1.png"));
			logolabel = new JLabel(logo);
			//logolabel.setAlignmentX(Component.CENTER_ALIGNMENT);
			logolabel.setOpaque(false);
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.insets = new Insets(0, 0, 0, 20);
			add(logolabel, gbc);
			
			
			
			logolabel = new JLabel ("Admin");
			logolabel.setOpaque(false);
			logolabel.setForeground(Color.white);
			logolabel.setFont(new Font("Arial", Font.BOLD, 20));
			gbc.gridx = 1;
			gbc.gridy = 0;
			gbc.insets = new Insets(0, 0, 40, 0);
			add(logolabel, gbc);
			
			logolabel = new JLabel ("Song name: ");
			logolabel.setOpaque(false);
			logolabel.setForeground(Color.white);
			logolabel.setFont(new Font("Arial", Font.PLAIN, 16));
			gbc.gridx = 0;
			gbc.gridy = 2;
			gbc.insets = new Insets(5, 0, 5, 0);
			add(logolabel, gbc);
			
			
			Font font1 = new Font("SansSerif", Font.BOLD, 20);
			
			
			JTextField songText = new JTextField(20);
			gbc.gridx = 1;
			gbc.gridy = 2;
			songText.setFont(font1);
			//gbc.weighty = 1.0;
			gbc.insets = new Insets(5, 0, 5, 0);
			add(songText, gbc);
			
			
			logolabel = new JLabel ("Artist: ");
			logolabel.setOpaque(false);
			logolabel.setForeground(Color.white);
			logolabel.setFont(new Font("Arial", Font.PLAIN, 16));
			gbc.gridx = 0;
			gbc.gridy = 3;
			gbc.insets = new Insets(5, 0, 5, 0);
			add(logolabel, gbc);
			
			JTextField artistText = new JTextField(20);
			gbc.gridx = 1;
			gbc.gridy = 3;
			artistText.setFont(font1);
			//gbc.weighty = 1.0;
			gbc.insets = new Insets(5, 0, 5, 0);
			add(artistText, gbc);
			
			logolabel = new JLabel ("Album: ");
			logolabel.setOpaque(false);
			logolabel.setForeground(Color.white);
			logolabel.setFont(new Font("Arial", Font.PLAIN, 16));
			gbc.gridx = 0;
			gbc.gridy = 4;
			gbc.insets = new Insets(5, 0, 5, 0);
			add(logolabel, gbc);
			
			JTextField albumText = new JTextField(20);
			gbc.gridx = 1;
			gbc.gridy = 4;
			albumText.setFont(font1);
			//gbc.weighty = 1.0;
			gbc.insets = new Insets(5, 0, 5, 0);
			add(albumText, gbc);
			
			logolabel = new JLabel ("Song Duration(seconds): ");
			logolabel.setOpaque(false);
			logolabel.setForeground(Color.white);
			logolabel.setFont(new Font("Arial", Font.PLAIN, 16));
			gbc.gridx = 0;
			gbc.gridy = 5;
			gbc.insets = new Insets(5, 0, 5, 0);
			add(logolabel, gbc);
			
			
			JTextField timeText = new JTextField(20);
			gbc.gridx = 1;
			gbc.gridy = 5;
			timeText.setFont(font1);
			//gbc.weighty = 1.0;
			gbc.insets = new Insets(5, 0, 5, 0);
			add(timeText, gbc);
			
			JButton butt = new JButton("Submit");
			//butt.setOpaque(false);
			gbc.gridx = 1;
			gbc.gridy = 10;
			gbc.insets = new Insets(5, 0, 5, 0);
			add(butt, gbc);
			
			frame.getRootPane().setDefaultButton(butt);
			
			butt.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					song = songText.getText();
					artist = artistText.getText();
					album = albumText.getText();
					seconds = Integer.parseInt(timeText.getText());
					
					insertartisttodb();
					insertsongtodb();
					insertalbumtodb();

					Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
					frame.getContentPane().removeAll();
					frame.setSize(800,650);
					frame.getContentPane().add(new AdminPanel(frame));
					//frame.setLayout(new FlowLayout(FlowLayout.LEFT));
					//frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
					frame.revalidate();
					frame.repaint();
				}
			});
			
			
			JButton backbutt = new JButton("Exit");
			//butt.setOpaque(false);
			gbc.gridx = 1;
			gbc.gridy = 11;
			gbc.insets = new Insets(5, 0, 5, 0);
			add(backbutt, gbc);
			
			backbutt.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
					frame.getContentPane().removeAll();
					frame.setSize(800,650);
					frame.getContentPane().add(new LoginPanel(frame));
					//frame.setLayout(new FlowLayout(FlowLayout.LEFT));
					frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
					frame.revalidate();
					frame.repaint();
				}
			});
			
			JButton butt2 = new JButton("Submit");
			//butt.setOpaque(false);
			gbc.gridx = 1;
			gbc.gridy = 10;
			gbc.insets = new Insets(5, 0, 5, 0);
			add(butt2, gbc);
			
			butt2.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
					frame.getContentPane().removeAll();
					frame.setSize(1000,800);
					frame.getContentPane().add(new AdminPanel(frame));
					//frame.setLayout(new FlowLayout(FlowLayout.LEFT));
					//frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
					frame.revalidate();
					frame.repaint();
				}
			});
			
			
		}
			
		public boolean checkartist(){
			try{
				Connection con = getConnection();
				
				PreparedStatement statement1 = con.prepareStatement("SELECT max(artistID) FROM artist");
				
				ResultSet result1 = statement1.executeQuery();
				
				while(result1.next()){
					artistID = result1.getInt(1) + 1;
					System.out.println(artistID + " ARTISTTTTTT");
				}
				con.close();
			}catch(Exception lolol){System.out.println(lolol);}
			finally{
				System.out.println("Query 1 Completed");
			}
				
			try{
				Connection con = getConnection();
				PreparedStatement statement = con.prepareStatement("SELECT artistID FROM artist where artistName = '"+ artist +"'");
				
				ResultSet result = statement.executeQuery();
				
				while(result.next()){
					artistID = result.getInt(1);
					System.out.println(artistID + " ");
					return false; //IT IS NOT NEW
				}
				con.close();
				
			}catch(Exception e){System.out.println(e);}
			finally{
				System.out.println("Query 2 Completed");
			}
			
			return true;
		}
		
		public void insertartisttodb(){
			
			if(checkartist()){
				try{
					Connection con = getConnection();
					PreparedStatement posted = con.prepareStatement("INSERT INTO artist (artistID, artistName) VALUES ('"+artistID+"', '"+artist+"')");
					
					posted.executeUpdate();
					con.close();
				}catch(Exception e){System.out.println(e);}
				finally{
					System.out.println("Insert Completed");
				}
			}
			
		}
		
		public boolean checkSong(){
			try{
				Connection con = getConnection();
				PreparedStatement statement1 = con.prepareStatement("SELECT max(songID) FROM songs");
				
				ResultSet result1 = statement1.executeQuery();
				
				while(result1.next()){
					songID = result1.getInt(1) + 1;
					System.out.println(songID + " *******");
				}
				con.close();
			}catch(Exception e){System.out.println(e);}
			finally{
				System.out.println("Query 1 Completed");
			}
			
			
			try{
				Connection con = getConnection();
				PreparedStatement statement = con.prepareStatement("SELECT songID FROM songs where songTitle = '"+ song +"'");
				
				ResultSet result = statement.executeQuery();
				
				while(result.next()){
					songID = result.getInt(1);
					System.out.println(songID + "()()()() ");
					return false; // IT IS NOT new
				}
				con.close();
			}catch(Exception e){System.out.println(e);}
			finally{
				System.out.println("Query 2 Completed");
			}
			
			return true;
		}
		
		public void insertsongtodb(){
			if(checkSong()){
				try{
					Connection con = getConnection();
					PreparedStatement posted = con.prepareStatement("INSERT INTO songs (artistID, songID, songTitle, songDuration, playcount) VALUES ('"+artistID+"', '"+songID+"', '"+song+"', '"+seconds+"', 0)");
					System.out.println("INSERT INTO songs (artistID, songID, songTitle, songDuration, playcount) VALUES ('"+artistID+"', '"+songID+"', '"+song+"', '"+seconds+"', 0)");
					
					posted.executeUpdate();
					con.close();
				}catch(Exception e){System.out.println(e);}
				finally{
					System.out.println("Insert Completed");
				}
			}
		}
			
		public boolean checkalbum(){
			try{ System.out.println("--=-=-=-=-=-=-=-=-=-=-=-=-==-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
				Connection con = getConnection();
				PreparedStatement statement1 = con.prepareStatement("SELECT max(albumID) FROM albums");
				
				ResultSet result1 = statement1.executeQuery();
				
				while(result1.next()){
					albumID = result1.getInt(1) + 1;
					System.out.println(albumID + " *******");
				}
				con.close();
				
			}catch(Exception e){System.out.println(e);}
			finally{
				System.out.println("Query Completed");
			}
			
			try{
				Connection con = getConnection();
				PreparedStatement statement = con.prepareStatement("SELECT albumID, albumSongs FROM albums where albumName = '"+ album +"'");
				
				ResultSet result = statement.executeQuery();
				
				while(result.next()){
					albumID = result.getInt("albumID");
					albumSongs = result.getString("albumSongs");
					System.out.println(albumID + " " + albumSongs);
					return false;// IT IS NOT NEW
				}
				con.close();
				
			}catch(Exception e){System.out.println(e);}
			finally{
				System.out.println("Query Completed");
			}
			
			return true;
		}
		
		public void insertalbumtodb(){
			if(checkalbum()){
				try{ System.out.println("--=-=-=-=-=-=-=-=-=-=-=-=-==-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
					Connection con = getConnection();
					PreparedStatement posted = con.prepareStatement("INSERT INTO albums (albumID, albumName, artistID, albumSongs) VALUES ("+albumID+", '"+album+"', "+artistID+", '"+ songID + "')");
					
					posted.executeUpdate();
					con.close();
				}catch(Exception e){System.out.println(e);}
				finally{
					System.out.println("Insert Completed");
				}
			}
			
			else{
			try{
					Connection con = getConnection();
					PreparedStatement posted = con.prepareStatement("UPDATE albums SET albumSongs = '" +albumSongs + ","+  songID +"' WHERE albumID = " + albumID);
					System.out.println("UPDATE albums SET albumSongs = '" +albumSongs + ","+  songID +"' WHERE albumID = " + albumID + ")");
					
					posted.executeUpdate();
					con.close();
				}catch(Exception e){System.out.println(e);}
				finally{
					System.out.println("Insert Completed");
				}
			}
			
		}
		

		
		/*
		
		public void check(){
			try{
				Connection con = getConnection();
				PreparedStatement statement = con.prepareStatement("SELECT max(userID) FROM user");
				
				ResultSet result = statement.executeQuery();
				
				while(result.next()){
					userID = result.getInt(1) + 1;
					System.out.println(userID + " ");
				}
				
			}catch(Exception e){System.out.println(e);}
			finally{
				System.out.println("Query Completed");
			}
		}*/
		
		private String song;
		private int songID;
		
		private String artist;
		private int artistID;
		
		private String album;
		private String albumSongs = "";
		private int albumID;
		
		private int seconds;
	}
	
	class ChangePanel extends JPanel{
		
		private JFrame frame;
		private ImageIcon logo;
		private JLabel logolabel;
		private JLabel header;
		private JLabel teamlogo;
		private JLabel de_team;
		
		private JTextField userText;
		private JTextField passText;
		
		private JButton butt;
		private JButton aboutbutt;
		
		public ChangePanel(JFrame f, String songString)
		{	
			frame = f;

			GridBagConstraints gbc = new GridBagConstraints();
			
			setLayout(new GridBagLayout());
			setBackground(Color.darkGray);
			
			JTextField searchText = new JTextField(15);
			gbc.gridx = 1;
			gbc.gridy = 0;
			//gbc.anchor = GridBagConstraints.WEST;
			gbc.insets = new Insets(0, 0, 10, 0);
			add(searchText, gbc);
			
			JButton butt = new JButton("Search");
			//butt.setOpaque(false);
			gbc.gridx = 2;
			gbc.gridy = 0;
			gbc.insets = new Insets(0, 0, 10, 0);
			add(butt, gbc);
			
			JButton butt6 = new JButton(" EDIT SONG ");
			//butt.setOpaque(false);
			gbc.gridx = 2;
			gbc.gridy = 10;
			//gbc.insets = new Insets(0, 0, 5, 0);
			gbc.insets = new Insets(5, 0, 5, 0);
			//add(butt6, gbc);
			
			JButton butt5 = new JButton("DELETE SONG");
			//butt.setOpaque(false);
			gbc.gridx = 1;
			gbc.gridy = 10;
			//gbc.insets = new Insets(0, 0, 5, 0);
			gbc.insets = new Insets(5, 0, 5, 0);
			add(butt5, gbc);
		
			
		
			
				try{
					checkSearch(songString); //initialize si(SongInfo)

					System.out.println("HERE");
					String[] columns = {"Song", "Artist", "Album"};
					System.out.println("HERE");
					String[][] rows = new String[si.getsongIDarray().size()][3];
					
					
					/*transfer to rows*/
					for(int i = 0; i < si.getsongIDarray().size(); i++){
						rows[i][0] = si.getsongTitle(i);
						rows[i][1] = si.getartistName(i);
						rows[i][2] = checkalbumofartist(si.getartistID(i), si.getsongID(i),i);
					}
					
					
					
					
					JTable songTable = new JTable(rows, columns);
					songTable.setPreferredScrollableViewportSize(new Dimension(600,400));
					songTable.setFillsViewportHeight(true);
					songTable.setFont(new Font("Arial", Font.BOLD, 20));
					songTable.setRowHeight(30);
					
					JScrollPane songListScrollPane = new JScrollPane(songTable);
					gbc.gridx = 1;
					gbc.gridy = 1;
			
					//gbc.insets = new Insets(0, 50, 0, 0);
					gbc.gridwidth = 3;
					add(songListScrollPane, gbc);
					
					
					/*ACTION LISTENER FOR DELETE SONG BUTTON*/
					butt5.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent e){	
							deleteSong(songTable.getSelectedRow());
							frame.getContentPane().removeAll();
							frame.getContentPane().add(new ChangePanel(frame, songString));
							frame.revalidate();
							frame.repaint();
						}
					});
					
					/*ACTION LISTENER FOR EDIT SONG BUTTON*/
					butt6.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent e){	
							int st = songTable.getSelectedRow();
							songID = si.getsongID(st);
							//checkSelectedSong();
							//insertplaylisttodb();
						}
					});
					
					/*ACTION LISTENER FOR SEARCH BUTTON*/
					butt.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent e){
							frame.getContentPane().removeAll();
							frame.getContentPane().add(new ChangePanel(frame, searchText.getText()));
							frame.revalidate();
							frame.repaint();
						}
								
					});
	
				} catch(Exception e){
					System.out.println(e);
				}
				
				
			JButton returnbutt = new JButton("Back");
			//butt.setOpaque(false);
			gbc.gridx = 2;
			gbc.gridy = 12;
			gbc.insets = new Insets(35, 0, 5, 0);
			gbc.gridwidth = 1;
			returnbutt.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					if(num == 1){
						mp3player.pause();
						num = 0;
					}
					
					Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
					frame.getContentPane().removeAll();
					frame.setSize(1300,900);
					frame.getContentPane().add(new ChartsPanel(frame));
					frame.setLayout(new FlowLayout(FlowLayout.LEFT));
					frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
					frame.revalidate();
					frame.repaint();
					
				}
			});
			add(returnbutt, gbc);
					
		}
		
		public void deleteSong(int offset){			
			try{
				
				Connection con = getConnection();
				PreparedStatement statement = con.prepareStatement("delete from songs where songID = " + si.getsongID(offset));
				//System.out.println("select songID from songs where songTitle = '"+ selectedSong + "'");
				statement.executeUpdate();
				

				con.close();
				
			}catch(Exception e){System.out.println(e);}
			finally{
				System.out.println("Query Completed");
			}
		}
		
		
		public void checkplaycount(String selectedSong, int idartist){
			try{
				
				Connection con = getConnection();
				
				PreparedStatement statement = con.prepareStatement("select playcount from songs where songTitle = '"+ selectedSong + "'  AND artistID = " + idartist + "");
				System.out.println("select songID from songs where songTitle = '"+ selectedSong + "'");
				ResultSet result = statement.executeQuery();
				
				while(result.next()){
					     playcount = result.getInt(1);
				}
				con.close();
				
				System.out.println("playcount: " + playcount);
				
			}catch(Exception e){System.out.println(e);}
			finally{
				System.out.println("Query Completed");
			}
		}
		
		public void addplaycount(String selectedSong, int idartist){
			try{
				
				Connection con = getConnection();

				PreparedStatement posted = con.prepareStatement("UPDATE songs SET playcount = "+playcount +" where songTitle = '"+selectedSong+"' and artistID = "+ idartist);
				System.out.println("UPDATE songs SET playcount = "+playcount +" where songTitle = '"+selectedSong+"' and artistID = "+ idartist);
				
				posted.executeUpdate();
				con.close();
				//System.out.println("playcount: " + playcount);
				
			}catch(Exception e){System.out.println(e);}
			finally{
				System.out.println("Update Completed");
			}
		}
		
		
		public String checkalbumofartist(int idartist, int idsong , int i){
			
			ArrayList<String> albumSongs = new ArrayList<String>();
			ArrayList<String> albumNames = new ArrayList<String>();
			String s = "album not found";
			try{
				Connection con = getConnection();
				
				PreparedStatement statement = con.prepareStatement("select * from albums where artistID = "+ idartist +" AND albumSongs Like '%" + idsong +"%'");
				System.out.println("select * from albums where artistID = " + idartist +" AND albumSongs Like '%" + idsong +"%'");
				ResultSet result = statement.executeQuery();
				
				while(result.next()){
					String jk = result.getString("albumSongs");
					String kj = result.getString("albumName");
					albumSongs.add(jk);
					albumNames.add(kj);	
					System.out.println(jk + " " + kj);
				}
				con.close();
				
				for(int j = 0; j < albumSongs.size(); j++){
					String songStrings = albumSongs.get(j)	;			
					String[] parts = songStrings.split(",");
					int[] n1 = new int[parts.length];
					for(int n = 0; n < parts.length; n++) {
						n1[n] = Integer.parseInt(parts[n]);
						System.out.println("***checking for song*** " + n1[n] + " = "+idsong);
						if(n1[n] == idsong)
							s = albumNames.get(j);
						
					}
	
				}
				
			}catch(Exception e){System.out.println(e);}
			finally{
				System.out.println("Query Completed");
				return s;
			}
			
			
		}
		
		
		public void checkSearch(String songString){
			try{
				si = new SongInfo();
				Connection con = getConnection();
				
				PreparedStatement statement = con.prepareStatement("select s.songID, s.songTitle, ai.artistID, ai.artistName from songs s join artist ai join albums a where s.songTitle LIKE '%" + songString + "%' and s.artistID = ai.artistID Group by s.songID order by s.songTitle");
				System.out.println("select s.songID, s.songTitle, ai.artistID, ai.artistName from songs s join artist ai join albums a where s.songTitle LIKE '%" + songString + "%' and s.artistID = ai.artistID Group by s.songID");
				ResultSet result = statement.executeQuery();
				
				while(result.next()){
						int songID = result.getInt("s.songID");
						String songTitle = result.getString("s.songTitle");
						int artistID = result.getInt("ai.artistID");
						String artistName = result.getString("ai.artistName");
						
						System.out.println("songID: "+ songID + "\nsongTitle: "+ songTitle +"\nartistID: "+ artistID +"\nartistName: "+ artistName);
						si.addInfo(songID,songTitle,artistID,artistName);
				}
				con.close();
				
			}catch(Exception e){System.out.println(e);}
			finally{
				System.out.println("Query Completed");
			}
	
		}
		
		public void checkSelectedSong(){
			try{
				
				Connection con = getConnection();
				
				String s = makesqlString(1);
				System.out.println(s);
				PreparedStatement statement = con.prepareStatement("select songID from songs where songTitle = '"+ songTitle + "'");
				System.out.println("select songID from songs where songTitle = '"+ songTitle + "'");
				ResultSet result = statement.executeQuery();
				
				while(result.next()){
					     songID = result.getInt("songID");
				}
				con.close();
				
			}catch(Exception e){System.out.println(e);}
			finally{
				System.out.println("Query Completed");
			}
		}
	
		public String makesqlString(int i){
			String s = "";	
				s +=  "songID = "+ songArray[i] + "";
			
			return s;
		}
		
		public ArrayList<String> getSongName(){
			
			songs = new ArrayList<>(); 
			
			for(int i = 0; i<songArray.length;i++){
				try{
					
					Connection con = getConnection();
					
					String s = makesqlString(i);
					System.out.println(s);
					PreparedStatement statement = con.prepareStatement("select songTitle from songs where "+ s + "");
					
					ResultSet result = statement.executeQuery();
					
					while(result.next()){
							 songs.add(result.getString(1)); 
					}
					con.close();
					
				}catch(Exception e){System.out.println(e);}
				finally{
					System.out.println("Query Completed");
				}
			}
			
			return songs;
		}
		
		public void checkSongs(){
			
			String[] parts = playlistSongs.split(",");
			int[] n1 = new int[parts.length];
			for(int n = 0; n < parts.length; n++) {
				n1[n] = Integer.parseInt(parts[n]);
				System.out.println("Number" + n +": " + n1[n]+ "");
				
			}
			
			songArray = n1;
			
			
		}
		
		public BufferedImage scaleImage(int WIDTH, int HEIGHT, String filename) {
			BufferedImage bi = null;
			try {
				ImageIcon ii = new ImageIcon(filename);//path to image
				bi = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
				Graphics2D g2d = (Graphics2D) bi.createGraphics();
				g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY));
				g2d.drawImage(ii.getImage(), 0, 0, WIDTH, HEIGHT, null);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			return bi;
		}
		
		public void checkPlaylist(String a, int id){
			try{
				Connection con = getConnection();
				PreparedStatement statement = con.prepareStatement("SELECT u.userUsername, p.userID , p.playlistID , p.playlistName , p.playlistSongs FROM playlists p, user u where p.playlistID = " + id + " AND p.userID = u.userID");
				System.out.println("SELECT u.userUsername, p.userID , p.playlistID , p.playlistName , p.playlistSongs FROM playlists p, user u where p.playlistID = " + id + " AND p.userID = u.userID");
				
				ResultSet result = statement.executeQuery();
				
				while(result.next()){
					playlistID = result.getInt("p.playlistID");
					userID = result.getInt("p.userID");
					userUsername = result.getString("u.userUsername");
					playlistName = result.getString("p.playlistName");
					playlistSongs = result.getString("p.playlistSongs");

					System.out.println("playlistID: " + playlistID);
					System.out.println("userID: " + userID);
					System.out.println("userUsername: " + userUsername);
					System.out.println("playlistName: " + playlistName);
					System.out.println("playlistSongs: " + playlistSongs);
				}
				con.close();
				
			}catch(Exception e){System.out.println(e);}
			finally{
				System.out.println("Query Completed");
			}
			
		}
		public void checkmaxPlaylist(){
			try{
					Connection con = getConnection();
					PreparedStatement statement = con.prepareStatement("select max(playlistID) from playlists");
					System.out.println("select max(playlistID) from playlists");
					ResultSet result = statement.executeQuery();
					
					while(result.next()){
						uplaylistID = result.getInt(1) +1;
					}
					con.close();
					
				}catch(Exception e){System.out.println(e);}
				finally{
					System.out.println("Query Completed");
				}
				
		}

		
		
		public boolean checkifPlaylist(){
			try{
					Connection con = getConnection();
					PreparedStatement statement = con.prepareStatement("select playlistName, playlistSongs from playlists where userID = "+ getUserID() +" and playlistName = '"+playlistName+"'");
					System.out.println("select playlistName from playlists where userID = "+ getUserID() +" and playlistName = '"+playlistName+"'");
					ResultSet result = statement.executeQuery();
					
					while(result.next()){
						uplaylistName = result.getString("playlistName");
						uplaylistSongs = result.getString("playlistSongs");
							
						return false;
					}
					con.close();
					
				}catch(Exception e){System.out.println(e);}
				finally{
					System.out.println("Query Completed");
				}
				
				return true;
		}
		
		
		public void insertplaylisttodb(){
			
			checkmaxPlaylist();
			
			if(checkifPlaylist()){
				try{ 
					System.out.println("--=-=-=-=-=-=-=-=-=-=-=-=-==-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
					Connection con = getConnection();
					PreparedStatement posted = con.prepareStatement("INSERT INTO playlists (userID, playlistID, playlistName, playlistSongs) VALUES ("+getUserID()+", "+uplaylistID+", '"+playlistName+"', '"+ songID+ "')");
					System.out.println("INSERT INTO playlists (userID, playlistID, playlistName, playlistSongs) VALUES ("+getUserID()+", "+uplaylistID+", '"+playlistName+"', '"+ songID+ "')");
					posted.executeUpdate();
					con.close();
				}catch(Exception e){System.out.println(e);}
				finally{
					System.out.println("Insert Completed");
				}
			}
			
			else{
			try{
					Connection con = getConnection();
					PreparedStatement posted = con.prepareStatement("UPDATE playlists SET playlistSongs = '" +uplaylistSongs + ","+ songID +"' where userID =  "+getUserID()+" AND playlistName  = '"+ uplaylistName+"'");
					System.out.println("UPDATE playlists SET playlistSongs = '" +uplaylistSongs + ","+ songID +"' where userID =  "+getUserID()+" AND playlistName  = '"+ uplaylistName+"'");
					
					posted.executeUpdate();
					con.close();
				}catch(Exception e){System.out.println(e);}
				finally{
					System.out.println("Update Completed");
				}
			}
		}
		
		
		private int userID;
		private String userUsername;
		
		private String songTitle;
		private int songID;
		private int[] songArray;
		private ArrayList<String> songs;
		
		
		private String playlistName;
		private int playlistID;
		private String playlistSongs = "";
		
		private SongInfo si;
		
		private int uplaylistID;
		private String uplaylistName;
		private String uplaylistSongs;
		
		private MP3 mp3player;
		private int playcount;
		private int num = 0;
		
		
	}
		
	class PlaylistsPanel extends JPanel{
		
		private JFrame frame;
		private JPanel jp;
		private ImageIcon logo;
		private JLabel logolabel;
		private JLabel header;
		private JLabel teamlogo;
		private JLabel de_team;
		
		private JTextField userText;
		private JTextField passText;
		
		private JButton butt;
		private JButton aboutbutt;
		
		private JButton Cbutt;
		private JButton Gbutt;
		private JButton Newbutt;

		private JButton Albumbutt;
		
		public PlaylistsPanel(JFrame f)
		{	
			frame = f;
			/*JLabel label = new JLabel("SPOTIFY");
			label.setFont(new Font("Comic sans MS", Font.BOLD, 30));
			label.setAlignmentX(Component.CENTER_ALIGNMENT);
			add(label);*/
			
			GridBagConstraints gbc = new GridBagConstraints();
			
			setLayout(new GridBagLayout());
			setBackground(Color.darkGray);
			
			logo = new ImageIcon(getClass().getResource("archifylogo1.png"));
			logolabel = new JLabel(logo);
			//logolabel.setAlignmentX(Component.CENTER_ALIGNMENT);
			logolabel.setOpaque(false);
			gbc.gridx = 0;
			gbc.gridy = 0;
			//gbc.weighty = 1;
			//gbc.weightx = 1;
			//gbc.anchor = GridBagConstraints.WEST;
			gbc.insets = new Insets(0, 0, 10, 10);
			add(logolabel, gbc);
			
			
			
			
			butt = new JButton("BROWSE");
			butt.setBorderPainted(false); 
			butt.setFocusPainted(false);
			butt.setForeground(Color.darkGray);
			butt.setFont(new Font("Arial", Font.BOLD, 20));
			gbc.gridx = 0;
			gbc.gridy = 1;
			gbc.insets = new Insets(0, 0, 10, 10);
			butt.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseEntered(java.awt.event.MouseEvent evt) {
					butt.setBackground(Color.GREEN);
				}

				public void mouseExited(java.awt.event.MouseEvent evt) {
					butt.setBackground(UIManager.getColor("control"));
				}
			});
			
			butt.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					frame.getContentPane().removeAll();
					frame.getContentPane().add(new ChartsPanel(frame));
					frame.setLayout(new FlowLayout(FlowLayout.LEFT));
					frame.revalidate();
					frame.repaint();
				}
			});
			
			add(butt, gbc);
			
			JButton profileButton = new JButton("My Profile");
			profileButton.setBorderPainted(false); 
			profileButton.setFocusPainted(false);
			profileButton.setForeground(Color.darkGray);
			profileButton.setFont(new Font("Arial", Font.BOLD, 20));
			gbc.gridx = 0;
			gbc.gridy = 2;
			gbc.insets = new Insets(0, 0, 10, 10);
			profileButton.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseEntered(java.awt.event.MouseEvent evt) {
					profileButton.setBackground(Color.GREEN);
				}

				public void mouseExited(java.awt.event.MouseEvent evt) {
					profileButton.setBackground(UIManager.getColor("control"));
				}
			});
			
			profileButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){		
					Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		
					frame.getContentPane().removeAll();
					frame.getContentPane().add(new ProfilePanel(frame,1));
					frame.setLayout(new GridBagLayout());
					frame.setSize(1000,800);	
					frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
					frame.revalidate();
					frame.repaint();
				}
			});
			
			add(profileButton, gbc);
			
			
			
			aboutbutt = new JButton("ABOUT");
			//butt.setAlignmentX(Component.CENTER_ALIGNMENT);
			aboutbutt.setBorderPainted(false); 
			//aboutbutt.setContentAreaFilled(false); 
			aboutbutt.setFocusPainted(false);
			aboutbutt.setForeground(Color.darkGray);
			aboutbutt.setFont(new Font("Arial", Font.BOLD, 30));
			//butt.setOpaque(false);
			gbc.gridx = 0;
			gbc.gridy = 10;
			//gbc.insets = new Insets(0, 0, 0, 0);
			aboutbutt.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseEntered(java.awt.event.MouseEvent evt) {
					aboutbutt.setBackground(Color.GREEN);
				}

				public void mouseExited(java.awt.event.MouseEvent evt) {
					aboutbutt.setBackground(UIManager.getColor("control"));
				}
			});
			aboutbutt.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					frame.getContentPane().removeAll();
					frame.getContentPane().add(new AboutPanel(frame));
					frame.setLayout(new FlowLayout(FlowLayout.LEFT));
					frame.revalidate();
					frame.repaint();
				}
			});
			
			add(aboutbutt, gbc);
			
			
			header = new JLabel("PLAYLISTS");
			header.setFont(new Font("Arial", Font.BOLD, 45));
			header.setForeground(Color.WHITE);
			gbc.gridx = 3;
			gbc.gridy = 0;
			gbc.gridwidth = 2;
			gbc.weighty = 0;
			gbc.weightx = 0;
			gbc.anchor = GridBagConstraints.WEST;
			gbc.insets = new Insets(0, 0, 10, 10);
			add(header, gbc);
			gbc.gridwidth = 1;
			
			/*
			Cbutt = new JButton("Charts");
			Cbutt.setBorderPainted(false); 
			Cbutt.setFocusPainted(false);
			Cbutt.setForeground(Color.darkGray);
			Cbutt.setFont(new Font("Arial", Font.BOLD, 20));
			gbc.gridx = 5;
			gbc.gridy = 1;
			gbc.insets = new Insets(0, 0, 10, 10);
			Cbutt.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseEntered(java.awt.event.MouseEvent evt) {
					Cbutt.setBackground(Color.GREEN);
				}

				public void mouseExited(java.awt.event.MouseEvent evt) {
					Cbutt.setBackground(UIManager.getColor("control"));
				}
			});
			
			
			Cbutt.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					frame.getContentPane().removeAll();
					frame.getContentPane().add(new ChartsPanel(frame));
					frame.setLayout(new FlowLayout(FlowLayout.LEFT));
					frame.revalidate();
					frame.repaint();
				}
			});
			
			add(Cbutt, gbc);

			
			Gbutt = new JButton("Genres & Moods");
			Gbutt.setBorderPainted(false); 
			Gbutt.setFocusPainted(false);
			Gbutt.setForeground(Color.darkGray);
			Gbutt.setFont(new Font("Arial", Font.BOLD, 20));
			gbc.gridx = 6;
			gbc.gridy = 1;
			gbc.insets = new Insets(0, 0, 10, 10);
			Gbutt.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseEntered(java.awt.event.MouseEvent evt) {
					Gbutt.setBackground(Color.GREEN);
				}

				public void mouseExited(java.awt.event.MouseEvent evt) {
					Gbutt.setBackground(UIManager.getColor("control"));
				}
			});
			
			Gbutt.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					frame.getContentPane().removeAll();
					frame.getContentPane().add(new GenresPanel(frame));
					frame.setLayout(new FlowLayout(FlowLayout.LEFT));
					frame.revalidate();
					frame.repaint();
				}
			});
			
			add(Gbutt, gbc);

			Newbutt = new JButton("New Releases");
			Newbutt.setBorderPainted(false); 
			Newbutt.setFocusPainted(false);
			Newbutt.setForeground(Color.darkGray);
			Newbutt.setFont(new Font("Arial", Font.BOLD, 20));
			gbc.gridx = 7;
			gbc.gridy = 1;
			gbc.insets = new Insets(0, 0, 10, 10);
			Newbutt.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseEntered(java.awt.event.MouseEvent evt) {
					Newbutt.setBackground(Color.GREEN);
				}

				public void mouseExited(java.awt.event.MouseEvent evt) {
					Newbutt.setBackground(UIManager.getColor("control"));
				}
			});
			
			Newbutt.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					frame.getContentPane().removeAll();
					frame.getContentPane().add();
					revalidate();
				}
			});
			
			add(Newbutt, gbc);
			*/
			
			checkplaylist();
			
			gbc.gridy = 2;
			gbc.gridx = 2;
			
			for(int i = 1; i <= pi.getplaylistIDarray().size(); i++){
				int n = i - 1;
				//String s = ai.getalbumName(n) + ".jpg" ; 
				//String sI = ai.getalbumName(n);
				
				String pString = pi.getplaylistName(n);
				int pID = pi.getplaylistID(n) + 0;
				
				String s = pi.getplaylistName(n) + " by \n" + pi.getuserID(n);
				//makeImage(s);
				//s = s + ".jpg";
				//BufferedImage img = scaleImage(300,300, s); 
				gbc.gridx++;
				
				
				//logo = new ImageIcon(img);
				
				JButton Playlistbutt = new JButton(s);
				//Playlistbutt.setBorderPainted(false); 
				//Playlistbutt.setFocusPainted(false); 
				//Playlistbutt.setContentAreaFilled(false); 
				Playlistbutt.setForeground(Color.darkGray);
				Playlistbutt.setPreferredSize(new Dimension(300, 300));
				Playlistbutt.setMaximumSize(new Dimension(300,300));
				//Playlistbutt.setIcon(logo);
				Playlistbutt.setCursor(new Cursor(Cursor.HAND_CURSOR));
				
				
				
				if(gbc.gridx > 5){
					gbc.gridx = 3;
					gbc.gridy++;
				}
				gbc.insets = new Insets(0, 0, 10, 10);

				add(Playlistbutt, gbc);
				
				
				Playlistbutt.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
					frame.getContentPane().removeAll();
					frame.setSize(1000,800);
					frame.getContentPane().add(new PlaylistPanel(frame, pString, pID));
					frame.setLayout(new GridBagLayout());
					frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);					
					frame.revalidate();
					frame.repaint();
				}
			});
			
			
			
			}
			
		}
		
		public void makeImage(String s){
			String text = "Hello";

			/*
			   Because font metrics is based on a graphics context, we need to create
			   a small, temporary image so we can ascertain the width and height
			   of the final image
			 */
			BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2d = img.createGraphics();
			Font font = new Font("Arial", Font.PLAIN, 48);
			g2d.setFont(font);
			FontMetrics fm = g2d.getFontMetrics();
			int width = fm.stringWidth(s);
			int height = fm.getHeight();
			g2d.dispose();

			img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			g2d = img.createGraphics();
			g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
			g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
			g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
			g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
			g2d.setFont(font);
			fm = g2d.getFontMetrics();
			g2d.setColor(Color.BLACK);
			g2d.drawString(s, 0, fm.getAscent());
			g2d.dispose();
			try {
				ImageIO.write(img, "jpg", new File(s +".jpg"));
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		public void checkplaylist(){
			
			pi = new PlaylistsInfo();
			
			try{
				Connection con = getConnection();
				PreparedStatement statement = con.prepareStatement("SELECT * FROM playlists");
				
				ResultSet result = statement.executeQuery();
				
				while(result.next()){
					int playlistID = result.getInt("playlistID");
					int userID = result.getInt("userID");
					String playliststring = result.getString("playlistName");
					String playlistSongs = result.getString("playlistSongs");
					
					pi.addInfo(playlistID, userID, playliststring, playlistSongs);
					
					System.out.println("\nplaylistID: " + playlistID + "\nuserID: "+ userID + "\nplayliststring:" + playliststring + "\nplaylistSongs: "+ playlistSongs + "");
				}
				con.close();
				
			}catch(Exception e){System.out.println(e);}
			finally{
				System.out.println("Query Completed");
			}
		}
		
		public BufferedImage scaleImage(int WIDTH, int HEIGHT, String filename) {
			BufferedImage bi = null;
			try {
				ImageIcon ii = new ImageIcon(filename);//path to image
				bi = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
				Graphics2D g2d = (Graphics2D) bi.createGraphics();
				g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY));
				g2d.drawImage(ii.getImage(), 0, 0, WIDTH, HEIGHT, null);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			return bi;
		}
		
		public PlaylistsInfo pi;
		public int albumid;
	}
	
	class PlaylistPanel extends JPanel{
		
		private JFrame frame;
		private ImageIcon logo;
		private JLabel logolabel;
		private JLabel header;
		private JLabel teamlogo;
		private JLabel de_team;
		
		private JTextField userText;
		private JTextField passText;
		
		private JButton butt;
		private JButton aboutbutt;
		
		//private GridBagConstraints gbc; 
		
		public PlaylistPanel(JFrame f, String playlistString, int playlistID)
		{	
			frame = f;
			int n = 14; // n is the number of songs;
			
			checkPlaylist(playlistString, playlistID);
			
			GridBagConstraints gbc = new GridBagConstraints();
			
			setLayout(new GridBagLayout());
			setBackground(Color.darkGray);
			/*
			logo = new ImageIcon(getClass().getResource("archifylogo1.png"));
			logolabel = new JLabel(logo);
			//logolabel.setAlignmentX(Component.CENTER_ALIGNMENT);
			logolabel.setOpaque(false);
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.weighty = 1;
			gbc.weightx = 1;
			//gbc.anchor = GridBagConstraints.WEST;
			gbc.insets = new Insets(0, 0, 0, 20);
			
			*/
			
			//String AL = albumName + ".jpg";
			BufferedImage img = scaleImage(300,300, " "); //albumName is the name of the album image
			logo = new ImageIcon(img);
			logolabel = new JLabel(logo); 
			//logolabel.setAlignmentX(Component.CENTER_ALIGNMENT);
			logolabel.setOpaque(false);
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.weighty = 1;
			gbc.weightx = 1;
			//gbc.anchor = GridBagConstraints.WEST;
			gbc.insets = new Insets(0, 0, 20, 0);
			add(logolabel, gbc);
			
			logolabel = new JLabel ("" + playlistString); //ALBUM NAME
			logolabel.setOpaque(false);
			logolabel.setForeground(Color.white);
			logolabel.setFont(new Font("Arial", Font.BOLD, 20));
			gbc.gridx = 0;
			gbc.gridy = 1;
			gbc.weighty = 1;
			gbc.weightx = 1;
			gbc.insets = new Insets(0, 0, 40, 0);
			add(logolabel, gbc);
			
			logolabel = new JLabel ("By " +userUsername);
			logolabel.setOpaque(false);
			logolabel.setForeground(Color.white);
			logolabel.setFont(new Font("Arial", Font.PLAIN, 16));
			gbc.gridx = 0;
			gbc.gridy = 2;
			gbc.weighty = 1;
			gbc.weightx = 1;
			gbc.insets = new Insets(0, 0, 0, 0);
			add(logolabel, gbc);
			
			
			checkSongs();
			
			//PUT SONG COUNT HERE
			logolabel = new JLabel ("" + songArray.length + " Songs");
			logolabel.setOpaque(false);
			logolabel.setForeground(Color.white);
			logolabel.setFont(new Font("Arial", Font.PLAIN, 16));
			gbc.gridx = 0;
			gbc.gridy = 3;
			gbc.weighty = 1;
			gbc.weightx = 1;
			gbc.insets = new Insets(0, 0, 20, 0);
			add(logolabel, gbc);
			
			
			
			JButton butt = new JButton("PLAY");
			//butt.setOpaque(false);
			gbc.gridx = 0;
			gbc.gridy = 10;
			gbc.insets = new Insets(10, 0, 5, 0);
			add(butt, gbc);
			
			JTextField playlisttext = new JTextField(15);
			gbc.gridx = 1;
			gbc.gridy = 10;
			gbc.insets = new Insets(0, 0, 5, 0);
			add(playlisttext, gbc);
			
			JButton butt5 = new JButton("ADD SONG TO PLAYLIST");
			//butt.setOpaque(false);
			gbc.gridx = 1;
			gbc.gridy = 11;
			gbc.insets = new Insets(0, 0, 5, 0);
			add(butt5, gbc);
			
		
			
		
			
				try{
					DefaultListModel<String> songname = new DefaultListModel<>();
					
					songs = getSongName();
					int n1;
					for(int i = 0; i < songs.size(); i++){
						n1= i+1;
						songname.addElement(n1 + ".) " + songs.get(i));
					}
						 
					  
					
					JList<String> songList = new JList<>(songname);
					songList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
					songList.setSelectedIndex(0);
					songList.setVisibleRowCount(7); 
					songList.setFont(new Font("Arial", Font.PLAIN, 25));
					
					JScrollPane songListScrollPane = new JScrollPane(songList);
					gbc.gridx = 1;
					gbc.gridy = 0;
			
					gbc.insets = new Insets(0, 50, 0, 0);

					add(songListScrollPane, gbc);
					
					
					//ACTION LISTENER FOR ADD TO PLAYLIST BUTTON
					butt5.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent e){	
							String str = songList.getSelectedValue();
							playlistName = playlisttext.getText();
							songTitle = str.substring(4,str.length());
							System.out.println(songTitle);
							checkSelectedSong();
							insertplaylisttodb();								
						}
					});
					
					butt.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent e){
							if(num == 0){
								String str = songList.getSelectedValue();
								selectedSong = str.substring(4,str.length());
								mp3player = new MP3(selectedSong, checkartist());
								mp3player.start();
								num = 1;
								checkplaycount();
								playcount++;
								addplaycount();
								butt.setText("STOP");
							}
							
							else if(num == 1){
								mp3player.pause();
								num = 0;
								butt.setText("PLAY");
							}

						}
						
					});
					
					
					
					
				} catch(Exception e){
					System.out.println(e);
				}
				
				
			JButton returnbutt = new JButton("Back");
			//butt.setOpaque(false);
			gbc.gridx = 2;
			gbc.gridy = 0;
			gbc.insets = new Insets(35, 0, 5, 0);
			returnbutt.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					if(num == 1){
						mp3player.pause();
						num = 0;
					}
					JScrollPane scrollPane = new JScrollPane(new PlaylistsPanel(frame));
					scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
					scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
					scrollPane.setPreferredSize(new Dimension(1300, 800));
					//scrollPane.setBounds(1, 1, 800, 800);
					//scrollPane.setBorderPainted(false);
					scrollPane.getVerticalScrollBar().setUnitIncrement(16);
					scrollPane.setOpaque(false);
					
					
					frame.getContentPane().removeAll();
					frame.getContentPane().add(scrollPane);
					frame.setLayout(new FlowLayout(FlowLayout.LEFT));
					frame.pack();
					frame.revalidate();
					frame.repaint();
				}
			});
			add(returnbutt, gbc);
					
				
		}
		
		public String checkartist(){
			
			String artistName = "";
			try{
				Connection con = getConnection();
				PreparedStatement statement = con.prepareStatement("select * from artist a join songs s where s.artistID = a.artistID and s.songTitle = '" + selectedSong + "'");
				System.out.println("select * from artist a join songs s where s.artistID = a.artistID and s.songTitle = '" + selectedSong + "'");
				ResultSet result = statement.executeQuery();
				
				while(result.next()){
					 artistName = result.getString("artistName");   
					 artistID = result.getInt("artistID");
				}
				con.close();
				
				System.out.println("playcount: " + playcount);
				
			}catch(Exception e){System.out.println(e);}
			finally{
				System.out.println("Query Completed");
				return artistName;
			}
		}
		
		
		public void checkplaycount(){
			try{
				
				Connection con = getConnection();
				
				PreparedStatement statement = con.prepareStatement("select playcount from songs where songTitle = '"+ selectedSong + "'  AND artistID = " + artistID + "");
				System.out.println("select songID from songs where songTitle = '"+ selectedSong + "'");
				ResultSet result = statement.executeQuery();
				
				while(result.next()){
					     playcount = result.getInt(1);
				}
				con.close();
				
				System.out.println("playcount: " + playcount);
				
			}catch(Exception e){System.out.println(e);}
			finally{
				System.out.println("Query Completed");
			}
		}
		
		public void addplaycount(){
			try{
				
				Connection con = getConnection();
				
				PreparedStatement posted = con.prepareStatement("UPDATE songs SET playcount = "+playcount +" where songTitle = '"+selectedSong+"' and artistID = "+ artistID);
				System.out.println("UPDATE songs SET playcount = "+playcount +" where songTitle = '"+selectedSong+"' and artistID = "+ artistID);
				
				posted.executeUpdate();
				
				//System.out.println("playcount: " + playcount);
				con.close();
			}catch(Exception e){System.out.println(e);}
			finally{
				System.out.println("Update Completed");
			}
		}
		
		public void checkSelectedSong(){
			try{
				
				Connection con = getConnection();
				
				PreparedStatement statement = con.prepareStatement("select songID from songs where songTitle = '"+ songTitle + "'");
				System.out.println("select songID from songs where songTitle = '"+ songTitle + "'");
				ResultSet result = statement.executeQuery();
				
				while(result.next()){
					     songID = result.getInt("songID");
				}
				con.close();
				
			}catch(Exception e){System.out.println(e);}
			finally{
				System.out.println("Query Completed");
			}
		}
	
		public String makesqlString(int i){
			String s = "";	
				s +=  "songID = "+ songArray[i] + "";
			
			return s;
		}
		
		public ArrayList<String> getSongName(){
			
			songs = new ArrayList<>(); 
			
			for(int i = 0; i<songArray.length;i++){
				try{
					
					Connection con = getConnection();
					
					String s = makesqlString(i);
					System.out.println(s);
					PreparedStatement statement = con.prepareStatement("select songTitle from songs where "+ s + "");
					
					ResultSet result = statement.executeQuery();
					
					while(result.next()){
							 songs.add(result.getString(1)); 
					}
					con.close();
					
				}catch(Exception e){System.out.println(e);}
				finally{
					System.out.println("Query Completed");
				}
			}
			
			return songs;
		}
		
		public void checkSongs(){
			
			String[] parts = playlistSongs.split(",");
			int[] n1 = new int[parts.length];
			for(int n = 0; n < parts.length; n++) {
				n1[n] = Integer.parseInt(parts[n]);
				System.out.println("Number" + n +": " + n1[n]+ "");
				
			}
			
			songArray = n1;
			
			
		}
		
		public BufferedImage scaleImage(int WIDTH, int HEIGHT, String filename) {
			BufferedImage bi = null;
			try {
				ImageIcon ii = new ImageIcon(filename);//path to image
				bi = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
				Graphics2D g2d = (Graphics2D) bi.createGraphics();
				g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY));
				g2d.drawImage(ii.getImage(), 0, 0, WIDTH, HEIGHT, null);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			return bi;
		}
		
		
		public void checkPlaylist(String a, int id){
			try{
				Connection con = getConnection();
				PreparedStatement statement = con.prepareStatement("SELECT u.userUsername, p.userID , p.playlistID , p.playlistName , p.playlistSongs FROM playlists p, user u where p.playlistID = " + id + " AND p.userID = u.userID");
				System.out.println("SELECT u.userUsername, p.userID , p.playlistID , p.playlistName , p.playlistSongs FROM playlists p, user u where p.playlistID = " + id + " AND p.userID = u.userID");
				
				ResultSet result = statement.executeQuery();
				
				while(result.next()){
					playlistID = result.getInt("p.playlistID");
					userID = result.getInt("p.userID");
					userUsername = result.getString("u.userUsername");
					playlistName = result.getString("p.playlistName");
					playlistSongs = result.getString("p.playlistSongs");

					System.out.println("playlistID: " + playlistID);
					System.out.println("userID: " + userID);
					System.out.println("userUsername: " + userUsername);
					System.out.println("playlistName: " + playlistName);
					System.out.println("playlistSongs: " + playlistSongs);
				}
				con.close();
				
			}catch(Exception e){System.out.println(e);}
			finally{
				System.out.println("Query Completed");
			}
			
		}
		public void checkmaxPlaylist(){
			try{
					Connection con = getConnection();
					PreparedStatement statement = con.prepareStatement("select max(playlistID) from playlists");
					System.out.println("select max(playlistID) from playlists");
					ResultSet result = statement.executeQuery();
					
					while(result.next()){
						uplaylistID = result.getInt(1) +1;
					}
					con.close();
					
				}catch(Exception e){System.out.println(e);}
				finally{
					System.out.println("Query Completed");
				}
				
		}

		
		
		public boolean checkifPlaylist(){
			try{
					Connection con = getConnection();
					PreparedStatement statement = con.prepareStatement("select playlistName, playlistSongs from playlists where userID = "+ getUserID() +" and playlistName = '"+playlistName+"'");
					System.out.println("select playlistName from playlists where userID = "+ getUserID() +" and playlistName = '"+playlistName+"'");
					ResultSet result = statement.executeQuery();
					
					while(result.next()){
						uplaylistName = result.getString("playlistName");
						uplaylistSongs = result.getString("playlistSongs");
							
						return false;
					}
					con.close();
					
				}catch(Exception e){System.out.println(e);}
				finally{
					System.out.println("Query Completed");
				}
				
				return true;
		}
		
		
		public void insertplaylisttodb(){
			
			checkmaxPlaylist();
			
			if(checkifPlaylist()){
				try{ 
					System.out.println("--=-=-=-=-=-=-=-=-=-=-=-=-==-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
					Connection con = getConnection();
					PreparedStatement posted = con.prepareStatement("INSERT INTO playlists (userID, playlistID, playlistName, playlistSongs) VALUES ("+getUserID()+", "+uplaylistID+", '"+playlistName+"', '"+ songID+ "')");
					System.out.println("INSERT INTO playlists (userID, playlistID, playlistName, playlistSongs) VALUES ("+getUserID()+", "+uplaylistID+", '"+playlistName+"', '"+ songID+ "')");
					posted.executeUpdate();
					con.close();
				}catch(Exception e){System.out.println(e);}
				finally{
					System.out.println("Insert Completed");
				}
			}
			
			else{
			try{
					Connection con = getConnection();
					PreparedStatement posted = con.prepareStatement("UPDATE playlists SET playlistSongs = '" +uplaylistSongs + ","+ songID +"' where userID =  "+getUserID()+" AND playlistName  = '"+ uplaylistName+"'");
					System.out.println("UPDATE playlists SET playlistSongs = '" +uplaylistSongs + ","+ songID +"' where userID =  "+getUserID()+" AND playlistName  = '"+ uplaylistName+"'");
					
					posted.executeUpdate();
					con.close();
				}catch(Exception e){System.out.println(e);}
				finally{
					System.out.println("Update Completed");
				}
			}
		}
	
		private int userID;
		private String userUsername;
		
		private String songTitle;
		private int songID;
		private int[] songArray;
		private ArrayList<String> songs;

		private String playlistName;
		private int playlistID;
		private String playlistSongs = "";
		
		private int uplaylistID;
		private String uplaylistName;
		private String uplaylistSongs;
		
		private String selectedSong;
		private MP3 mp3player;
		private int playcount;
		private int num = 0;
		private int artistID;
		
	}
	
	class SearchPanel extends JPanel{
		
		private JFrame frame;
		private ImageIcon logo;
		private JLabel logolabel;
		private JLabel header;
		private JLabel teamlogo;
		private JLabel de_team;
		
		private JTextField userText;
		private JTextField passText;
		
		private JButton butt;
		private JButton aboutbutt;
		
		public SearchPanel(JFrame f, String songString)
		{	
			frame = f;

			GridBagConstraints gbc = new GridBagConstraints();
			
			setLayout(new GridBagLayout());
			setBackground(Color.darkGray);
			
			header = new JLabel("Search Results");
			header.setFont(new Font("Arial", Font.BOLD, 45));
			header.setForeground(Color.WHITE);
			gbc.gridx = 1;
			gbc.gridy = 0;
			//gbc.anchor = GridBagConstraints.WEST;
			gbc.insets = new Insets(0, 0, 10, 0);
			add(header, gbc);
			
			JButton butt = new JButton("PLAY");
			//butt.setOpaque(false);
			gbc.gridx = 2;
			gbc.gridy = 1;
			gbc.insets = new Insets(5, 5, 5, 5);
			add(butt, gbc);
			
			JTextField playlisttext = new JTextField(15);
			gbc.gridx = 1;
			gbc.gridy = 10;
			//gbc.insets = new Insets(0, 0, 5, 0);
			gbc.insets = new Insets(5, 0, 5, 0);
			add(playlisttext, gbc);
			
			JButton butt5 = new JButton("ADD SONG TO PLAYLIST");
			//butt.setOpaque(false);
			gbc.gridx = 1;
			gbc.gridy = 11;
			//gbc.insets = new Insets(0, 0, 5, 0);
			gbc.insets = new Insets(5, 0, 5, 0);
			add(butt5, gbc);
		
			
		
			
				try{
					checkSearch(songString); //initialize si(SongInfo)
					/*
					DefaultListModel<String> songname = new DefaultListModel<>();
					
					
					
					
					int n1;
					for(int i = 0; i < si.getsongIDarray().size(); i++){
						n1= i+1;
						songname.addElement(si.getsongTitle(i) +" "+ si.getartistName(i));
					}
						 
					  
					
					JList<String> songList = new JList<>(songname);
					songList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
					songList.setSelectedIndex(0);
					songList.setVisibleRowCount(7); 
					songList.setFont(new Font("Arial", Font.PLAIN, 25));
					*/
					System.out.println("HERE");
					String[] columns = {"Song", "Artist", "Album"};
					System.out.println("HERE");
					String[][] rows = new String[si.getsongIDarray().size()][3];
					
					
					/*transfer to rows*/
					for(int i = 0; i < si.getsongIDarray().size(); i++){
						rows[i][0] = si.getsongTitle(i);
						rows[i][1] = si.getartistName(i);
						rows[i][2] = checkalbumofartist(si.getartistID(i), si.getsongID(i),i);
						//rows[i][2] = si.getalbumName(i);
					}
					
					
					
					
					JTable songTable = new JTable(rows, columns);
					songTable.setPreferredScrollableViewportSize(new Dimension(600,400));
					songTable.setFillsViewportHeight(true);
					songTable.setFont(new Font("Arial", Font.BOLD, 20));
					songTable.setRowHeight(30);
					
					JScrollPane songListScrollPane = new JScrollPane(songTable);
					gbc.gridx = 1;
					gbc.gridy = 1;
			
					//gbc.insets = new Insets(0, 50, 0, 0);

					add(songListScrollPane, gbc);
					
					
					/*ACTION LISTENER FOR ADD TO PLAYLIST BUTTON*/
					butt5.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent e){	
							int st = songTable.getSelectedRow();
							playlistName = playlisttext.getText();
							//songTitle = str.substring(4,str.length());
							songID = si.getsongID(st);
							System.out.println(playlistName + " " + st + " " + si.getsongID(st));
							//checkSelectedSong();
							insertplaylisttodb();
						}
					});
					
					butt.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent e){
							if(num == 0){
								int st = songTable.getSelectedRow();
								mp3player = new MP3(si.getsongTitle(st), si.getartistName(st));
								mp3player.start();
								num = 1;
								checkplaycount(si.getsongTitle(st), si.getartistID(st));
								playcount++;
								addplaycount(si.getsongTitle(st), si.getartistID(st));
								butt.setText("STOP");
							}
									
							else if(num == 1){
								mp3player.pause();
								num = 0;
								butt.setText("PLAY");
							}
						}
								
					});
					
					
					
					
				} catch(Exception e){
					System.out.println(e);
				}
				
				
			JButton returnbutt = new JButton("Back");
			//butt.setOpaque(false);
			gbc.gridx = 1;
			gbc.gridy = 12;
			gbc.insets = new Insets(35, 0, 5, 0);
			returnbutt.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					if(num == 1){
						mp3player.pause();
						num = 0;
					}
					
					Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
					frame.getContentPane().removeAll();
					frame.setSize(1300,900);
					frame.getContentPane().add(new ChartsPanel(frame));
					frame.setLayout(new FlowLayout(FlowLayout.LEFT));
					frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
					frame.revalidate();
					frame.repaint();
					
				}
			});
			add(returnbutt, gbc);
					
		}
		
		
		public void checkplaycount(String selectedSong, int idartist){
			try{
				
				Connection con = getConnection();
				
				PreparedStatement statement = con.prepareStatement("select playcount from songs where songTitle = '"+ selectedSong + "'  AND artistID = " + idartist + "");
				System.out.println("select songID from songs where songTitle = '"+ selectedSong + "'");
				ResultSet result = statement.executeQuery();
				
				while(result.next()){
					     playcount = result.getInt(1);
				}
				con.close();
				
				System.out.println("playcount: " + playcount);
				
			}catch(Exception e){System.out.println(e);}
			finally{
				System.out.println("Query Completed");
			}
		}
		
		public void addplaycount(String selectedSong, int idartist){
			try{
				
				Connection con = getConnection();

				PreparedStatement posted = con.prepareStatement("UPDATE songs SET playcount = "+playcount +" where songTitle = '"+selectedSong+"' and artistID = "+ idartist);
				System.out.println("UPDATE songs SET playcount = "+playcount +" where songTitle = '"+selectedSong+"' and artistID = "+ idartist);
				
				posted.executeUpdate();
				con.close();
				//System.out.println("playcount: " + playcount);
				
			}catch(Exception e){System.out.println(e);}
			finally{
				System.out.println("Update Completed");
			}
		}
		
		
		public String checkalbumofartist(int idartist, int idsong , int i){
			
			ArrayList<String> albumSongs = new ArrayList<String>();
			ArrayList<String> albumNames = new ArrayList<String>();
			String s = "album not found";
			try{
				Connection con = getConnection();
				
				PreparedStatement statement = con.prepareStatement("select * from albums where artistID = "+ idartist +" AND albumSongs Like '%" + idsong +"%'");
				System.out.println("select * from albums where artistID = " + idartist +" AND albumSongs Like '%" + idsong +"%'");
				ResultSet result = statement.executeQuery();
				
				while(result.next()){
					String jk = result.getString("albumSongs");
					String kj = result.getString("albumName");
					albumSongs.add(jk);
					albumNames.add(kj);	
					System.out.println(jk + " " + kj);
				}
				con.close();
				
				for(int j = 0; j < albumSongs.size(); j++){
					String songStrings = albumSongs.get(j)	;			
					String[] parts = songStrings.split(",");
					int[] n1 = new int[parts.length];
					for(int n = 0; n < parts.length; n++) {
						n1[n] = Integer.parseInt(parts[n]);
						System.out.println("***checking for song*** " + n1[n] + " = "+idsong);
						if(n1[n] == idsong)
							s = albumNames.get(j);
						
					}
	
				}
				
			}catch(Exception e){System.out.println(e);}
			finally{
				System.out.println("Query Completed");
				return s;
			}
			
			
		}
		
		
		public void checkSearch(String songString){
			try{
				si = new SongInfo();
				Connection con = getConnection();
				
				PreparedStatement statement = con.prepareStatement("select s.songID, s.songTitle, ai.artistID, ai.artistName from songs s join artist ai join albums a where s.songTitle LIKE '%" + songString + "%' and s.artistID = ai.artistID Group by s.songID order by s.songTitle");
				System.out.println("select s.songID, s.songTitle, ai.artistID, ai.artistName from songs s join artist ai join albums a where s.songTitle LIKE '%" + songString + "%' and s.artistID = ai.artistID Group by s.songID");
				ResultSet result = statement.executeQuery();
				
				while(result.next()){
						int songID = result.getInt("s.songID");
						String songTitle = result.getString("s.songTitle");
						int artistID = result.getInt("ai.artistID");
						String artistName = result.getString("ai.artistName");
						
						System.out.println("songID: "+ songID + "\nsongTitle: "+ songTitle +"\nartistID: "+ artistID +"\nartistName: "+ artistName);
						si.addInfo(songID,songTitle,artistID,artistName);
				}
				con.close();
				
			}catch(Exception e){System.out.println(e);}
			finally{
				System.out.println("Query Completed");
			}
	
		}
		
		public void checkSelectedSong(){
			try{
				
				Connection con = getConnection();
				
				String s = makesqlString(1);
				System.out.println(s);
				PreparedStatement statement = con.prepareStatement("select songID from songs where songTitle = '"+ songTitle + "'");
				System.out.println("select songID from songs where songTitle = '"+ songTitle + "'");
				ResultSet result = statement.executeQuery();
				
				while(result.next()){
					     songID = result.getInt("songID");
				}
				con.close();
				
			}catch(Exception e){System.out.println(e);}
			finally{
				System.out.println("Query Completed");
			}
		}
	
		public String makesqlString(int i){
			String s = "";	
				s +=  "songID = "+ songArray[i] + "";
			
			return s;
		}
		
		public ArrayList<String> getSongName(){
			
			songs = new ArrayList<>(); 
			
			for(int i = 0; i<songArray.length;i++){
				try{
					
					Connection con = getConnection();
					
					String s = makesqlString(i);
					System.out.println(s);
					PreparedStatement statement = con.prepareStatement("select songTitle from songs where "+ s + "");
					
					ResultSet result = statement.executeQuery();
					
					while(result.next()){
							 songs.add(result.getString(1)); 
					}
					con.close();
					
				}catch(Exception e){System.out.println(e);}
				finally{
					System.out.println("Query Completed");
				}
			}
			
			return songs;
		}
		
		public void checkSongs(){
			
			String[] parts = playlistSongs.split(",");
			int[] n1 = new int[parts.length];
			for(int n = 0; n < parts.length; n++) {
				n1[n] = Integer.parseInt(parts[n]);
				System.out.println("Number" + n +": " + n1[n]+ "");
				
			}
			
			songArray = n1;
			
			
		}
		
		public BufferedImage scaleImage(int WIDTH, int HEIGHT, String filename) {
			BufferedImage bi = null;
			try {
				ImageIcon ii = new ImageIcon(filename);//path to image
				bi = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
				Graphics2D g2d = (Graphics2D) bi.createGraphics();
				g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY));
				g2d.drawImage(ii.getImage(), 0, 0, WIDTH, HEIGHT, null);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			return bi;
		}
		
		public void checkPlaylist(String a, int id){
			try{
				Connection con = getConnection();
				PreparedStatement statement = con.prepareStatement("SELECT u.userUsername, p.userID , p.playlistID , p.playlistName , p.playlistSongs FROM playlists p, user u where p.playlistID = " + id + " AND p.userID = u.userID");
				System.out.println("SELECT u.userUsername, p.userID , p.playlistID , p.playlistName , p.playlistSongs FROM playlists p, user u where p.playlistID = " + id + " AND p.userID = u.userID");
				
				ResultSet result = statement.executeQuery();
				
				while(result.next()){
					playlistID = result.getInt("p.playlistID");
					userID = result.getInt("p.userID");
					userUsername = result.getString("u.userUsername");
					playlistName = result.getString("p.playlistName");
					playlistSongs = result.getString("p.playlistSongs");

					System.out.println("playlistID: " + playlistID);
					System.out.println("userID: " + userID);
					System.out.println("userUsername: " + userUsername);
					System.out.println("playlistName: " + playlistName);
					System.out.println("playlistSongs: " + playlistSongs);
				}
				con.close();
				
			}catch(Exception e){System.out.println(e);}
			finally{
				System.out.println("Query Completed");
			}
			
		}
		public void checkmaxPlaylist(){
			try{
					Connection con = getConnection();
					PreparedStatement statement = con.prepareStatement("select max(playlistID) from playlists");
					System.out.println("select max(playlistID) from playlists");
					ResultSet result = statement.executeQuery();
					
					while(result.next()){
						uplaylistID = result.getInt(1) +1;
					}
					con.close();
					
				}catch(Exception e){System.out.println(e);}
				finally{
					System.out.println("Query Completed");
				}
				
		}

		
		
		public boolean checkifPlaylist(){
			try{
					Connection con = getConnection();
					PreparedStatement statement = con.prepareStatement("select playlistName, playlistSongs from playlists where userID = "+ getUserID() +" and playlistName = '"+playlistName+"'");
					System.out.println("select playlistName from playlists where userID = "+ getUserID() +" and playlistName = '"+playlistName+"'");
					ResultSet result = statement.executeQuery();
					
					while(result.next()){
						uplaylistName = result.getString("playlistName");
						uplaylistSongs = result.getString("playlistSongs");
							
						return false;
					}
					con.close();
					
				}catch(Exception e){System.out.println(e);}
				finally{
					System.out.println("Query Completed");
				}
				
				return true;
		}
		
		
		public void insertplaylisttodb(){
			
			checkmaxPlaylist();
			
			if(checkifPlaylist()){
				try{ 
					System.out.println("--=-=-=-=-=-=-=-=-=-=-=-=-==-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
					Connection con = getConnection();
					PreparedStatement posted = con.prepareStatement("INSERT INTO playlists (userID, playlistID, playlistName, playlistSongs) VALUES ("+getUserID()+", "+uplaylistID+", '"+playlistName+"', '"+ songID+ "')");
					System.out.println("INSERT INTO playlists (userID, playlistID, playlistName, playlistSongs) VALUES ("+getUserID()+", "+uplaylistID+", '"+playlistName+"', '"+ songID+ "')");
					posted.executeUpdate();
					con.close();
				}catch(Exception e){System.out.println(e);}
				finally{
					System.out.println("Insert Completed");
				}
			}
			
			else{
			try{
					Connection con = getConnection();
					PreparedStatement posted = con.prepareStatement("UPDATE playlists SET playlistSongs = '" +uplaylistSongs + ","+ songID +"' where userID =  "+getUserID()+" AND playlistName  = '"+ uplaylistName+"'");
					System.out.println("UPDATE playlists SET playlistSongs = '" +uplaylistSongs + ","+ songID +"' where userID =  "+getUserID()+" AND playlistName  = '"+ uplaylistName+"'");
					
					posted.executeUpdate();
					con.close();
				}catch(Exception e){System.out.println(e);}
				finally{
					System.out.println("Update Completed");
				}
			}
		}
		
		
		private int userID;
		private String userUsername;
		
		private String songTitle;
		private int songID;
		private int[] songArray;
		private ArrayList<String> songs;
		
		
		private String playlistName;
		private int playlistID;
		private String playlistSongs = "";
		
		private SongInfo si;
		
		private int uplaylistID;
		private String uplaylistName;
		private String uplaylistSongs;
		
		private MP3 mp3player;
		private int playcount;
		private int num = 0;
		
		
	}
	
	class Top50Panel extends JPanel{
		
		private JFrame frame;
		private ImageIcon logo;
		private JLabel logolabel;
		private JLabel header;
		private JLabel teamlogo;
		private JLabel de_team;
		
		private JTextField userText;
		private JTextField passText;
		
		private JButton butt;
		private JButton aboutbutt;
		
		public Top50Panel(JFrame f)
		{	
			frame = f;

			GridBagConstraints gbc = new GridBagConstraints();
			
			setLayout(new GridBagLayout());
			setBackground(Color.darkGray);
			
			header = new JLabel("TOP 50");
			header.setFont(new Font("Arial", Font.BOLD, 45));
			header.setForeground(Color.WHITE);
			gbc.gridx = 1;
			gbc.gridy = 0;
			//gbc.anchor = GridBagConstraints.WEST;
			gbc.insets = new Insets(0, 0, 10, 0);
			add(header, gbc);
			
			JButton butt = new JButton("PLAY");
			//butt.setOpaque(false);
			gbc.gridx = 2;
			gbc.gridy = 1;
			gbc.insets = new Insets(5, 5, 5, 5);
			add(butt, gbc);
			
			JTextField playlisttext = new JTextField(15);
			gbc.gridx = 1;
			gbc.gridy = 10;
			//gbc.insets = new Insets(0, 0, 5, 0);
			gbc.insets = new Insets(5, 0, 5, 0);
			add(playlisttext, gbc);
			
			JButton butt5 = new JButton("ADD SONG TO PLAYLIST");
			//butt.setOpaque(false);
			gbc.gridx = 1;
			gbc.gridy = 11;
			//gbc.insets = new Insets(0, 0, 5, 0);
			gbc.insets = new Insets(5, 0, 5, 0);
			add(butt5, gbc);
			
			
				try{
					checkTop50(); //initialize si(SongInfo)
					/*
					DefaultListModel<String> songname = new DefaultListModel<>();
					
					
					
					
					int n1;
					for(int i = 0; i < si.getsongIDarray().size(); i++){
						n1= i+1;
						songname.addElement(si.getsongTitle(i) +" "+ si.getartistName(i));
					}
						 
					  
					
					JList<String> songList = new JList<>(songname);
					songList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
					songList.setSelectedIndex(0);
					songList.setVisibleRowCount(7); 
					songList.setFont(new Font("Arial", Font.PLAIN, 25));
					*/
					System.out.println("HERE");
					String[] columns = {"Song", "Artist", "Album"};
					System.out.println("HERE");
					String[][] rows = new String[50][3];
					
					
					/*transfer to rows*/
					for(int i = 0; i < 50; i++){
						rows[i][0] = si.getsongTitle(i);
						rows[i][1] = si.getartistName(i);
						rows[i][2] = checkalbumofartist(si.getartistID(i), si.getsongID(i),i);
						//rows[i][2] = si.getalbumName(i);
					}
					
					JTable songTable = new JTable(rows, columns);
					songTable.setPreferredScrollableViewportSize(new Dimension(600,400));
					songTable.setFillsViewportHeight(true);
					songTable.setFont(new Font("Arial", Font.BOLD, 20));
					songTable.setRowHeight(30);
					
					JScrollPane songListScrollPane = new JScrollPane(songTable);
					gbc.gridx = 1;
					gbc.gridy = 1;
			
					//gbc.insets = new Insets(0, 50, 0, 0);

					add(songListScrollPane, gbc);
					
					
					/*ACTION LISTENER FOR ADD TO PLAYLIST BUTTON*/
					butt5.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent e){	
							int st = songTable.getSelectedRow();
							playlistName = playlisttext.getText();
							//songTitle = str.substring(4,str.length());
							songID = si.getsongID(st);
							System.out.println(playlistName + " " + st + " " + si.getsongID(st));
							//checkSelectedSong();
							insertplaylisttodb();								
						}
					});
					
					butt.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent e){
							if(num == 0){
								int st = songTable.getSelectedRow();
								mp3player = new MP3(si.getsongTitle(st), si.getartistName(st));
								mp3player.start();
								num = 1;
								checkplaycount(si.getsongTitle(st), si.getartistID(st));
								playcount++;
								addplaycount(si.getsongTitle(st), si.getartistID(st));
								butt.setText("STOP");
							}
									
							else if(num == 1){
								mp3player.pause();
								num = 0;
								butt.setText("PLAY");
							}
						}
								
					});
					
					
					
					
				} catch(Exception e){
					System.out.println(e);
				}
				
				
			JButton returnbutt = new JButton("Back");
			//butt.setOpaque(false);
			gbc.gridx = 1;
			gbc.gridy = 12;
			gbc.insets = new Insets(35, 0, 5, 0);
			returnbutt.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					
					Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
					frame.getContentPane().removeAll();
					frame.setSize(1300,900);
					frame.getContentPane().add(new ChartsPanel(frame));
					frame.setLayout(new FlowLayout(FlowLayout.LEFT));
					frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
					frame.revalidate();
					frame.repaint();
					
				}
			});
			add(returnbutt, gbc);
					
		}
		
		public void checkplaycount(String selectedSong, int idartist){
			try{
				
				Connection con = getConnection();
				
				PreparedStatement statement = con.prepareStatement("select playcount from songs where songTitle = '"+ selectedSong + "'  AND artistID = " + idartist + "");
				System.out.println("select songID from songs where songTitle = '"+ selectedSong + "'");
				ResultSet result = statement.executeQuery();
				
				while(result.next()){
					     playcount = result.getInt(1);
				}
				con.close();
				System.out.println("playcount: " + playcount);
				
			}catch(Exception e){System.out.println(e);}
			finally{
				System.out.println("Query Completed");
			}
		}
		
		public void addplaycount(String selectedSong, int idartist){
			try{
				
				Connection con = getConnection();

				PreparedStatement posted = con.prepareStatement("UPDATE songs SET playcount = "+playcount +" where songTitle = '"+selectedSong+"' and artistID = "+ idartist);
				System.out.println("UPDATE songs SET playcount = "+playcount +" where songTitle = '"+selectedSong+"' and artistID = "+ idartist);
				
				posted.executeUpdate();
				con.close();
				//System.out.println("playcount: " + playcount);
				
			}catch(Exception e){System.out.println(e);}
			finally{
				System.out.println("Update Completed");
			}
		}
		
		
		public String checkalbumofartist(int idartist, int idsong , int i){
			
			ArrayList<String> albumSongs = new ArrayList<String>();
			ArrayList<String> albumNames = new ArrayList<String>();
			String s = "album not found";
			try{
				Connection con = getConnection();
				
				PreparedStatement statement = con.prepareStatement("select * from albums where artistID = "+ idartist +" AND albumSongs Like '%" + idsong +"%'");
				System.out.println("select * from albums where artistID = " + idartist +" AND albumSongs Like '%" + idsong +"%'");
				ResultSet result = statement.executeQuery();
				
				while(result.next()){
					String jk = result.getString("albumSongs");
					String kj = result.getString("albumName");
					albumSongs.add(jk);
					albumNames.add(kj);	
					System.out.println(jk + " " + kj);
				}
				con.close();
				
				for(int j = 0; j < albumSongs.size(); j++){
					String songStrings = albumSongs.get(j)	;			
					String[] parts = songStrings.split(",");
					int[] n1 = new int[parts.length];
					for(int n = 0; n < parts.length; n++) {
						n1[n] = Integer.parseInt(parts[n]);
						System.out.println("***checking for song*** " + n1[n] + " = "+idsong);
						if(n1[n] == idsong)
							s = albumNames.get(j);
						
					}
	
				}
				
			}catch(Exception e){System.out.println(e);}
			finally{
				System.out.println("Query Completed");
				return s;
			}
			
			
		}
		
		
		public void checkTop50(){
			try{
				si = new SongInfo();
				Connection con = getConnection();
				
				PreparedStatement statement = con.prepareStatement("select s.songID, s.songTitle, ai.artistID, ai.artistName from songs s join artist ai join albums a where s.songTitle LIKE '%" +""+ "%' and s.artistID = ai.artistID Group by s.songID order by s.playcount DESC");
				//System.out.println("select s.songID, s.songTitle, ai.artistID, ai.artistName from songs s join artist ai join albums a where s.songTitle LIKE '%" + songString + "%' and s.artistID = ai.artistID Group by s.songID");
				ResultSet result = statement.executeQuery();
				
				while(result.next()){
						int songID = result.getInt("s.songID");
						String songTitle = result.getString("s.songTitle");
						int artistID = result.getInt("ai.artistID");
						String artistName = result.getString("ai.artistName");
						
						System.out.println("songID: "+ songID + "\nsongTitle: "+ songTitle +"\nartistID: "+ artistID +"\nartistName: "+ artistName);
						si.addInfo(songID,songTitle,artistID,artistName);
				}
				con.close();
				
			}catch(Exception e){System.out.println(e);}
			finally{
				System.out.println("Query Completed");
			}
	
		}
		
		public void checkSelectedSong(){
			try{
				
				Connection con = getConnection();
				
				String s = makesqlString(1);
				System.out.println(s);
				PreparedStatement statement = con.prepareStatement("select songID from songs where songTitle = '"+ songTitle + "'");
				System.out.println("select songID from songs where songTitle = '"+ songTitle + "'");
				ResultSet result = statement.executeQuery();
				
				while(result.next()){
					     songID = result.getInt("songID");
				}
				con.close();
				
			}catch(Exception e){System.out.println(e);}
			finally{
				System.out.println("Query Completed");
			}
		}
	
		public String makesqlString(int i){
			String s = "";	
				s +=  "songID = "+ songArray[i] + "";
			
			return s;
		}
		
		public ArrayList<String> getSongName(){
			
			songs = new ArrayList<>(); 
			
			for(int i = 0; i<songArray.length;i++){
				try{
					
					Connection con = getConnection();
					
					String s = makesqlString(i);
					System.out.println(s);
					PreparedStatement statement = con.prepareStatement("select songTitle from songs where "+ s + "");
					
					ResultSet result = statement.executeQuery();
					
					while(result.next()){
							 songs.add(result.getString(1)); 
					}
					con.close();
					
				}catch(Exception e){System.out.println(e);}
				finally{
					System.out.println("Query Completed");
				}
			}
			
			return songs;
		}
		
		public void checkSongs(){
			
			String[] parts = playlistSongs.split(",");
			int[] n1 = new int[parts.length];
			for(int n = 0; n < parts.length; n++) {
				n1[n] = Integer.parseInt(parts[n]);
				System.out.println("Number" + n +": " + n1[n]+ "");
				
			}
			
			songArray = n1;
			
			
		}
		
		public BufferedImage scaleImage(int WIDTH, int HEIGHT, String filename) {
			BufferedImage bi = null;
			try {
				ImageIcon ii = new ImageIcon(filename);//path to image
				bi = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
				Graphics2D g2d = (Graphics2D) bi.createGraphics();
				g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY));
				g2d.drawImage(ii.getImage(), 0, 0, WIDTH, HEIGHT, null);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			return bi;
		}
		
		public void checkPlaylist(String a, int id){
			try{
				Connection con = getConnection();
				PreparedStatement statement = con.prepareStatement("SELECT u.userUsername, p.userID , p.playlistID , p.playlistName , p.playlistSongs FROM playlists p, user u where p.playlistID = " + id + " AND p.userID = u.userID");
				System.out.println("SELECT u.userUsername, p.userID , p.playlistID , p.playlistName , p.playlistSongs FROM playlists p, user u where p.playlistID = " + id + " AND p.userID = u.userID");
				
				ResultSet result = statement.executeQuery();
				
				while(result.next()){
					playlistID = result.getInt("p.playlistID");
					userID = result.getInt("p.userID");
					userUsername = result.getString("u.userUsername");
					playlistName = result.getString("p.playlistName");
					playlistSongs = result.getString("p.playlistSongs");

					System.out.println("playlistID: " + playlistID);
					System.out.println("userID: " + userID);
					System.out.println("userUsername: " + userUsername);
					System.out.println("playlistName: " + playlistName);
					System.out.println("playlistSongs: " + playlistSongs);
				}
				con.close();
				
			}catch(Exception e){System.out.println(e);}
			finally{
				System.out.println("Query Completed");
			}
			
		}
		
		
		public void checkmaxPlaylist(){
			try{
					Connection con = getConnection();
					PreparedStatement statement = con.prepareStatement("select max(playlistID) from playlists");
					System.out.println("select max(playlistID) from playlists");
					ResultSet result = statement.executeQuery();
					
					while(result.next()){
						uplaylistID = result.getInt(1) +1;
					}
					con.close();
					
				}catch(Exception e){System.out.println(e);}
				finally{
					System.out.println("Query Completed");
				}
				
		}

		
		
		public boolean checkifPlaylist(){
			try{
					Connection con = getConnection();
					PreparedStatement statement = con.prepareStatement("select playlistName, playlistSongs from playlists where userID = "+ getUserID() +" and playlistName = '"+playlistName+"'");
					System.out.println("select playlistName from playlists where userID = "+ getUserID() +" and playlistName = '"+playlistName+"'");
					ResultSet result = statement.executeQuery();
					
					while(result.next()){
						uplaylistName = result.getString("playlistName");
						uplaylistSongs = result.getString("playlistSongs");
							
						return false;
					}
					con.close();
					
				}catch(Exception e){System.out.println(e);}
				finally{
					System.out.println("Query Completed");
				}
				
				return true;
		}
		
		
		public void insertplaylisttodb(){
			
			checkmaxPlaylist();
			
			if(checkifPlaylist()){
				try{ 
					System.out.println("--=-=-=-=-=-=-=-=-=-=-=-=-==-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
					Connection con = getConnection();
					PreparedStatement posted = con.prepareStatement("INSERT INTO playlists (userID, playlistID, playlistName, playlistSongs) VALUES ("+getUserID()+", "+uplaylistID+", '"+playlistName+"', '"+ songID+ "')");
					System.out.println("INSERT INTO playlists (userID, playlistID, playlistName, playlistSongs) VALUES ("+getUserID()+", "+uplaylistID+", '"+playlistName+"', '"+ songID+ "')");
					posted.executeUpdate();
					con.close();
				}catch(Exception e){System.out.println(e);}
				finally{
					System.out.println("Insert Completed");
				}
			}
			
			else{
			try{
					Connection con = getConnection();
					PreparedStatement posted = con.prepareStatement("UPDATE playlists SET playlistSongs = '" +uplaylistSongs + ","+ songID +"' where userID =  "+getUserID()+" AND playlistName  = '"+ uplaylistName+"'");
					System.out.println("UPDATE playlists SET playlistSongs = '" +uplaylistSongs + ","+ songID +"' where userID =  "+getUserID()+" AND playlistName  = '"+ uplaylistName+"'");
					
					posted.executeUpdate();
					con.close();
				}catch(Exception e){System.out.println(e);}
				finally{
					System.out.println("Update Completed");
				}
			}
		}
		
		
		private int userID;
		private String userUsername;
		
		private String songTitle;
		private int songID;
		private int[] songArray;
		private ArrayList<String> songs;
		
		
		private String playlistName;
		private int playlistID;
		private String playlistSongs = "";
		
		private SongInfo si;
		
		private int uplaylistID;
		private String uplaylistName;
		private String uplaylistSongs;
		
		private MP3 mp3player;
		private int playcount;
		private int num = 0;
		
		
		
	}

	class ArtistsPanel extends JPanel{
		
		private JFrame frame;
		private JPanel jp;
		private ImageIcon logo;
		private JLabel logolabel;
		private JLabel header;
		private JLabel teamlogo;
		private JLabel de_team;
		
		private JTextField userText;
		private JTextField passText;
		
		private JButton butt;
		private JButton aboutbutt;
		
		private JButton Cbutt;
		private JButton Gbutt;
		private JButton Newbutt;

		private JButton Albumbutt;
		
		public ArtistsPanel(JFrame f)
		{	
			frame = f;
			GridBagConstraints gbc = new GridBagConstraints();
			
			setLayout(new GridBagLayout());
			setBackground(Color.darkGray);
			
			logo = new ImageIcon(getClass().getResource("archifylogo1.png"));
			logolabel = new JLabel(logo);
			//logolabel.setAlignmentX(Component.CENTER_ALIGNMENT);
			logolabel.setOpaque(false);
			gbc.gridx = 0;
			gbc.gridy = 0;
			//gbc.weighty = 1;
			//gbc.weightx = 1;
			//gbc.anchor = GridBagConstraints.WEST;
			gbc.insets = new Insets(0, 0, 10, 10);
			add(logolabel, gbc);
			
			
			
			
			butt = new JButton("BROWSE");
			butt.setBorderPainted(false); 
			butt.setFocusPainted(false);
			butt.setForeground(Color.darkGray);
			butt.setFont(new Font("Arial", Font.BOLD, 20));
			gbc.gridx = 0;
			gbc.gridy = 1;
			gbc.insets = new Insets(0, 0, 10, 10);
			butt.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseEntered(java.awt.event.MouseEvent evt) {
					butt.setBackground(Color.GREEN);
				}

				public void mouseExited(java.awt.event.MouseEvent evt) {
					butt.setBackground(UIManager.getColor("control"));
				}
			});
			
			butt.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					frame.getContentPane().removeAll();
					frame.getContentPane().add(new ChartsPanel(frame));
					frame.setLayout(new FlowLayout(FlowLayout.LEFT));
					frame.revalidate();
					frame.repaint();
				}
			});
			
			add(butt, gbc);
			
			JButton profileButton = new JButton("My Profile");
			profileButton.setBorderPainted(false); 
			profileButton.setFocusPainted(false);
			profileButton.setForeground(Color.darkGray);
			profileButton.setFont(new Font("Arial", Font.BOLD, 20));
			gbc.gridx = 0;
			gbc.gridy = 2;
			gbc.insets = new Insets(0, 0, 10, 10);
			profileButton.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseEntered(java.awt.event.MouseEvent evt) {
					profileButton.setBackground(Color.GREEN);
				}

				public void mouseExited(java.awt.event.MouseEvent evt) {
					profileButton.setBackground(UIManager.getColor("control"));
				}
			});
			
			profileButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){		
					Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		
					frame.getContentPane().removeAll();
					frame.getContentPane().add(new ProfilePanel(frame,1));
					frame.setLayout(new GridBagLayout());
					frame.setSize(1000,800);	
					frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
					frame.revalidate();
					frame.repaint();
				}
			});
			
			add(profileButton, gbc);
			
			
			
			aboutbutt = new JButton("ABOUT");
			//butt.setAlignmentX(Component.CENTER_ALIGNMENT);
			aboutbutt.setBorderPainted(false); 
			//aboutbutt.setContentAreaFilled(false); 
			aboutbutt.setFocusPainted(false);
			aboutbutt.setForeground(Color.darkGray);
			aboutbutt.setFont(new Font("Arial", Font.BOLD, 30));
			//butt.setOpaque(false);
			gbc.gridx = 0;
			gbc.gridy = 10;
			//gbc.insets = new Insets(0, 0, 0, 0);
			aboutbutt.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseEntered(java.awt.event.MouseEvent evt) {
					aboutbutt.setBackground(Color.GREEN);
				}

				public void mouseExited(java.awt.event.MouseEvent evt) {
					aboutbutt.setBackground(UIManager.getColor("control"));
				}
			});
			aboutbutt.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					frame.getContentPane().removeAll();
					frame.getContentPane().add(new AboutPanel(frame));
					frame.setLayout(new FlowLayout(FlowLayout.LEFT));
					frame.revalidate();
					frame.repaint();
				}
			});
			
			add(aboutbutt, gbc);
			
			
			header = new JLabel("ARTISTS");
			header.setFont(new Font("Arial", Font.BOLD, 45));
			header.setForeground(Color.WHITE);
			gbc.gridx = 3;
			gbc.gridy = 0;
			gbc.gridwidth = 2;
			gbc.weighty = 0;
			gbc.weightx = 0;
			gbc.anchor = GridBagConstraints.WEST;
			gbc.insets = new Insets(0, 0, 10, 10);
			add(header, gbc);
			gbc.gridwidth = 1;
			
			checkartist();
			
			gbc.gridy = 2;
			gbc.gridx = 2;
			
			/*artistNames = new ArrayList<String>();
			artisIDs = new ArrayList<Integer>();*/
			
			for(int i = 1; i <= artistNames.size(); i++){
				int n = i - 1;
				
				String artistString = artistNames.get(n);
				int artistInt = artistIDs.get(n);
				gbc.gridx++;
				
				JButton artistbutt = new JButton(artistString); 
				artistbutt.setForeground(Color.darkGray);
				artistbutt.setPreferredSize(new Dimension(300, 300));
				artistbutt.setMaximumSize(new Dimension(300,300));
				artistbutt.setCursor(new Cursor(Cursor.HAND_CURSOR));
				
				
				
				if(gbc.gridx > 5){
					gbc.gridx = 3;
					gbc.gridy++;
				}
				gbc.insets = new Insets(0, 0, 10, 10);

				add(artistbutt, gbc);
				
				
				artistbutt.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
					frame.getContentPane().removeAll();
					frame.setSize(1000,800);
					frame.getContentPane().add(new ArtistPanel(frame, artistString, artistInt));
					frame.setLayout(new GridBagLayout());
					frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);					
					frame.revalidate();
					frame.repaint();
				}
			});
			
			
			
			}
			
		}
		
		public void makeImage(String s){
			String text = "Hello";

			/*
			   Because font metrics is based on a graphics context, we need to create
			   a small, temporary image so we can ascertain the width and height
			   of the final image
			 */
			BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2d = img.createGraphics();
			Font font = new Font("Arial", Font.PLAIN, 48);
			g2d.setFont(font);
			FontMetrics fm = g2d.getFontMetrics();
			int width = fm.stringWidth(s);
			int height = fm.getHeight();
			g2d.dispose();

			img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			g2d = img.createGraphics();
			g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
			g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
			g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
			g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
			g2d.setFont(font);
			fm = g2d.getFontMetrics();
			g2d.setColor(Color.BLACK);
			g2d.drawString(s, 0, fm.getAscent());
			g2d.dispose();
			try {
				ImageIO.write(img, "jpg", new File(s +".jpg"));
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		public void checkartist(){
			
			artistNames = new ArrayList<String>();
			artistIDs = new ArrayList<Integer>();
			
			try{
				Connection con = getConnection();
				PreparedStatement statement = con.prepareStatement("Select * from artist");
				
				ResultSet result = statement.executeQuery();
				
				while(result.next()){
					artistNames.add(result.getString("artistName"));
					artistIDs.add(result.getInt("artistID"));
				}
				con.close();
				
			}catch(Exception e){System.out.println(e);}
			finally{
				System.out.println("Query Completed");
			}
		}
		
		public BufferedImage scaleImage(int WIDTH, int HEIGHT, String filename) {
			BufferedImage bi = null;
			try {
				ImageIcon ii = new ImageIcon(filename);//path to image
				bi = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
				Graphics2D g2d = (Graphics2D) bi.createGraphics();
				g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY));
				g2d.drawImage(ii.getImage(), 0, 0, WIDTH, HEIGHT, null);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			return bi;
		}
		
		public ArrayList<String> artistNames;
		public ArrayList<Integer> artistIDs;
		public int albumid;
	}
	
	class ArtistPanel extends JPanel{
		
		private JFrame frame;
		private ImageIcon logo;
		private JLabel logolabel;
		private JLabel header;
		private JLabel teamlogo;
		private JLabel de_team;
		
		private JTextField userText;
		private JTextField passText;
		
		private JButton butt;
		private JButton aboutbutt;
		
		//private GridBagConstraints gbc; 
		
		public ArtistPanel(JFrame f, String nameofartist, int idofartist)
		{	
			frame = f;
			int n = 14; // n is the number of songs;
			artistID = idofartist;
			checkArtist(nameofartist, idofartist);
			
			GridBagConstraints gbc = new GridBagConstraints();
			
			setLayout(new GridBagLayout());
			setBackground(Color.darkGray);
			
			logolabel = new JLabel(nameofartist);
			logolabel.setFont(new Font("Arial", Font.BOLD, 45));
			logolabel.setForeground(Color.WHITE);
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.gridwidth = 3;
			gbc.anchor = GridBagConstraints.WEST;
			gbc.insets = new Insets(0, 0, 0, 20);	
			add(logolabel, gbc);
			
			gbc.gridwidth = 1;
			logolabel = new JLabel("Popular");
			logolabel.setFont(new Font("Arial", Font.BOLD, 20));
			logolabel.setForeground(Color.WHITE);
			gbc.gridx = 0;
			gbc.gridy = 1;
			//gbc.anchor = GridBagConstraints.WEST;
			gbc.insets = new Insets(0, 0, 0, 0);	
			add(logolabel, gbc);
			
			
			JButton butt = new JButton("PLAY");
			//butt.setOpaque(false);
			gbc.gridx = 1;
			gbc.gridy = 10;
			gbc.insets = new Insets(10, 5, 5, 5);
			add(butt, gbc);
			
			JTextField playlisttext = new JTextField(15);
			gbc.gridx = 0;
			gbc.gridy = 10;
			gbc.insets = new Insets(10, 5, 5, 5);
			add(playlisttext, gbc);
			
			JButton butt5 = new JButton("ADD SONG TO PLAYLIST");
			//butt.setOpaque(false);
			gbc.gridx = 0;
			gbc.gridy = 11;
			gbc.insets = new Insets(0, 5, 5, 5);
			add(butt5, gbc);	
			
				try{
					
					String[] columns = {"Song", "Playcount"};
					System.out.println("HERE");
					String[][] rows = new String[songs.size()][2];
					
					
					/*transfer to rows*/
					for(int i = 0; i < songs.size(); i++){
						rows[i][0] = songs.get(i);
						rows[i][1] = playcounts.get(i) + "";
					}
					
					
					
					
					JTable songTable = new JTable(rows, columns);
					songTable.setPreferredScrollableViewportSize(new Dimension(600,150));
					songTable.setFillsViewportHeight(true);
					songTable.setFont(new Font("Arial", Font.BOLD, 20));
					songTable.setRowHeight(30);
					
					JScrollPane songListScrollPane = new JScrollPane(songTable);
					gbc.gridx = 0;
					gbc.gridy = 2;
					gbc.gridwidth = 3;
			
					gbc.insets = new Insets(0, 0, 0, 0);

					add(songListScrollPane, gbc);
					gbc.gridwidth = 1;
					
					//ACTION LISTENER FOR ADD TO PLAYLIST BUTTON
					butt5.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent e){	
							int jt = songTable.getSelectedRow();
							playlistName = playlisttext.getText();
							songTitle = songs.get(jt);
							System.out.println(songTitle);
							checkSelectedSong();
							insertplaylisttodb();								
						}
					});
					
					
					butt.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent e){
							if(num == 0){
								int n = songTable.getSelectedRow();
								selectedSong = songs.get(n);
								mp3player = new MP3(selectedSong, nameofartist);
								mp3player.start();
								num = 1;
								checkplaycount();
								playcount++;
								addplaycount();
								butt.setText("STOP");
							}
							
							else if(num == 1){
								mp3player.pause();
								num = 0;
								butt.setText("PLAY");
							}

						}
						
					});
					
					
					
					
				} catch(Exception e){
					System.out.println(e);
				}
				
				
			JButton returnbutt = new JButton("Back");
			//butt.setOpaque(false);
			gbc.gridx = 2;
			gbc.gridy = 10;
			gbc.insets = new Insets(10, 5, 5, 5);
			returnbutt.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					if(num == 1){
						mp3player.pause();
						num = 0;
					}
					JScrollPane scrollPane = new JScrollPane(new ArtistsPanel(frame));
					scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
					scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
					scrollPane.setPreferredSize(new Dimension(1300, 800));
					//scrollPane.setBounds(1, 1, 800, 800);
					//scrollPane.setBorderPainted(false);
					scrollPane.getVerticalScrollBar().setUnitIncrement(16);
					scrollPane.setOpaque(false);
					
					
					frame.getContentPane().removeAll();
					frame.getContentPane().add(scrollPane);
					frame.setLayout(new FlowLayout(FlowLayout.LEFT));
					frame.pack();
					frame.revalidate();
					frame.repaint();
				}
			});
			add(returnbutt, gbc);
					
				
		}
		
		public void checkplaycount(){
			try{
				
				Connection con = getConnection();
				
				PreparedStatement statement = con.prepareStatement("select playcount from songs where songTitle = '"+ selectedSong + "'  AND artistID = " + artistID + "");
				System.out.println("select songID from songs where songTitle = '"+ selectedSong + "'");
				ResultSet result = statement.executeQuery();
				
				while(result.next()){
					     playcount = result.getInt(1);
				}
				con.close();
				
				System.out.println("playcount: " + playcount);
				
			}catch(Exception e){System.out.println(e);}
			finally{
				System.out.println("Query Completed");
			}
		}
		
		public void addplaycount(){
			try{
				
				Connection con = getConnection();
				
				PreparedStatement posted = con.prepareStatement("UPDATE songs SET playcount = "+playcount +" where songTitle = '"+selectedSong+"' and artistID = "+ artistID);
				System.out.println("UPDATE songs SET playcount = "+playcount +" where songTitle = '"+selectedSong+"' and artistID = "+ artistID);
				
				posted.executeUpdate();
				
				//System.out.println("playcount: " + playcount);
				con.close();
			}catch(Exception e){System.out.println(e);}
			finally{
				System.out.println("Update Completed");
			}
		}
		
		public void checkSelectedSong(){
			try{
				
				Connection con = getConnection();
				
				PreparedStatement statement = con.prepareStatement("select songID from songs where songTitle = '"+ songTitle + "'");
				System.out.println("select songID from songs where songTitle = '"+ songTitle + "'");
				ResultSet result = statement.executeQuery();
				
				while(result.next()){
					     songID = result.getInt("songID");
				}
				con.close();
				
			}catch(Exception e){System.out.println(e);}
			finally{
				System.out.println("Query Completed");
			}
		}
	
		public String makesqlString(int i){
			String s = "";	
				s +=  "songID = "+ songArray[i] + "";
			
			return s;
		}
		
		public void checkSongs(){
			
			String[] parts = playlistSongs.split(",");
			int[] n1 = new int[parts.length];
			for(int n = 0; n < parts.length; n++) {
				n1[n] = Integer.parseInt(parts[n]);
				System.out.println("Number" + n +": " + n1[n]+ "");
				
			}
			
			songArray = n1;
			
			
		}
		
		public BufferedImage scaleImage(int WIDTH, int HEIGHT, String filename) {
			BufferedImage bi = null;
			try {
				ImageIcon ii = new ImageIcon(filename);//path to image
				bi = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
				Graphics2D g2d = (Graphics2D) bi.createGraphics();
				g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY));
				g2d.drawImage(ii.getImage(), 0, 0, WIDTH, HEIGHT, null);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			return bi;
		}
		
		
		public void checkArtist(String a, int id){
			songs = new ArrayList<String>();
			playcounts = new ArrayList<Integer>();
			
			try{
				Connection con = getConnection();
				PreparedStatement statement = con.prepareStatement("select * from songs where artistID =  "+ id + " order by playcount desc limit 5");
				System.out.println("select * from songs where artistID =  "+ id + " order by playcount desc limit 5");
				
				ResultSet result = statement.executeQuery();
				
				while(result.next()){
					songs.add(result.getString("songTitle"));
					playcounts.add(result.getInt("playcount"));
				}
				con.close();
				
			}catch(Exception e){System.out.println(e);}
			finally{
				System.out.println("Query Completed");
			}
			
		}
		
		public void checkmaxPlaylist(){
			try{
					Connection con = getConnection();
					PreparedStatement statement = con.prepareStatement("select max(playlistID) from playlists");
					System.out.println("select max(playlistID) from playlists");
					ResultSet result = statement.executeQuery();
					
					while(result.next()){
						uplaylistID = result.getInt(1) +1;
					}
					con.close();
					
				}catch(Exception e){System.out.println(e);}
				finally{
					System.out.println("Query Completed");
				}
				
		}
			
		public boolean checkifPlaylist(){
			try{
					Connection con = getConnection();
					PreparedStatement statement = con.prepareStatement("select playlistName, playlistSongs from playlists where userID = "+ getUserID() +" and playlistName = '"+playlistName+"'");
					System.out.println("select playlistName from playlists where userID = "+ getUserID() +" and playlistName = '"+playlistName+"'");
					ResultSet result = statement.executeQuery();
					
					while(result.next()){
						uplaylistName = result.getString("playlistName");
						uplaylistSongs = result.getString("playlistSongs");
							
						return false;
					}
					con.close();
					
				}catch(Exception e){System.out.println(e);}
				finally{
					System.out.println("Query Completed");
				}
				
				return true;
		}
			
		public void insertplaylisttodb(){
			
			checkmaxPlaylist();
			
			if(checkifPlaylist()){
				try{ 
					System.out.println("--=-=-=-=-=-=-=-=-=-=-=-=-==-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
					Connection con = getConnection();
					PreparedStatement posted = con.prepareStatement("INSERT INTO playlists (userID, playlistID, playlistName, playlistSongs) VALUES ("+getUserID()+", "+uplaylistID+", '"+playlistName+"', '"+ songID+ "')");
					System.out.println("INSERT INTO playlists (userID, playlistID, playlistName, playlistSongs) VALUES ("+getUserID()+", "+uplaylistID+", '"+playlistName+"', '"+ songID+ "')");
					posted.executeUpdate();
					con.close();
				}catch(Exception e){System.out.println(e);}
				finally{
					System.out.println("Insert Completed");
				}
			}
			
			else{
			try{
					Connection con = getConnection();
					PreparedStatement posted = con.prepareStatement("UPDATE playlists SET playlistSongs = '" +uplaylistSongs + ","+ songID +"' where userID =  "+getUserID()+" AND playlistName  = '"+ uplaylistName+"'");
					System.out.println("UPDATE playlists SET playlistSongs = '" +uplaylistSongs + ","+ songID +"' where userID =  "+getUserID()+" AND playlistName  = '"+ uplaylistName+"'");
					
					posted.executeUpdate();
					con.close();
				}catch(Exception e){System.out.println(e);}
				finally{
					System.out.println("Update Completed");
				}
			}
		}
	
		private int userID;
		private String userUsername;
		
		private String songTitle;
		private int songID;
		private int[] songArray;
		
		private ArrayList<String> songs;
		private ArrayList<Integer> playcounts;

		private String playlistName;
		private int playlistID;
		private String playlistSongs = "";
		
		private int uplaylistID;
		private String uplaylistName;
		private String uplaylistSongs;
		
		private String selectedSong;
		private MP3 mp3player;
		private int playcount;
		private int num = 0;
		private int artistID;
		 
		
	}


	
	/*class ArtistInfo {
		
		
		public ArrayList<Integer> artistIDs;
		public ArrayList<String> artistNames;
		public ArrayList<String> popularSongs;
		public ArrayList<Integer> albumIDs;
		public ArrayList<String> albumNames;
		
		
		
		public ArtistInfo(){
			artistIDs = new  ArrayList<Integer>();
			artistNames = new ArrayList<String>();
			popularSongs = new ArrayList<String>();
			albumIDs = new ArrayList<Integer>();
			albumNames = new ArrayList<String>();
		}
		
		public void addInfo(int artistID, String artistName, String popularSong, int albumID, ){
			songIDs.add(songID);
			songTitles.add(songTitle);
			artistIDs.add(artistID);
			artistNames.add(artistName);
		}
		
		public void addalbumName(String albumName){
			albumNames.add(albumName);
		}
		
		public ArrayList<Integer> getsongIDarray(){
			return songIDs;
		}
		
		public int getsongID (int n){
			return songIDs.get(n);
		}
		
		public String getsongTitle(int n){
			return songTitles.get(n);
		}
		
		public int getartistID(int n){
			return artistIDs.get(n);
		}
		
		public String getartistName(int n){
			return artistNames.get(n);
		}
		
		public String getalbumName(int n){
			return albumNames.get(n);
		}
	}*/
	
	class SongInfo {
		public ArrayList<Integer> songIDs;
		public ArrayList<String> songTitles;
		public ArrayList<Integer> artistIDs;
		public ArrayList<String>  artistNames;
		public ArrayList<String> albumNames;
		
		public SongInfo(){
			songIDs = new ArrayList<Integer>();
			songTitles = new ArrayList<String>();
			artistIDs = new  ArrayList<Integer>();
			artistNames = new ArrayList<String>();
			albumNames = new ArrayList<String>();
		}
		
		public void addInfo(int songID, String songTitle, int artistID, String artistName){
			songIDs.add(songID);
			songTitles.add(songTitle);
			artistIDs.add(artistID);
			artistNames.add(artistName);
		}
		
		public void addalbumName(String albumName){
			albumNames.add(albumName);
		}
		
		public ArrayList<Integer> getsongIDarray(){
			return songIDs;
		}
		
		public int getsongID (int n){
			return songIDs.get(n);
		}
		
		public String getsongTitle(int n){
			return songTitles.get(n);
		}
		
		public int getartistID(int n){
			return artistIDs.get(n);
		}
		
		public String getartistName(int n){
			return artistNames.get(n);
		}
		
		public String getalbumName(int n){
			return albumNames.get(n);
		}
	}
	
	class AlbumInfo {
		public ArrayList<Integer> albumIDs;
		public ArrayList<Integer> artistIDs;
		public ArrayList<String> albumstrings;
		public ArrayList<String> albumsSongs;
		
		public AlbumInfo(){
			albumIDs = new ArrayList<Integer>();
			artistIDs = new ArrayList<Integer>();
			albumstrings = new ArrayList<String>();
			albumsSongs = new ArrayList<String>();
		}
		
		public void addInfo(int albumID, int artistID, String albumName, String SongsInalbum ){
			albumIDs.add(albumID);
			artistIDs.add(artistID);
			albumstrings.add(albumName);
			albumsSongs.add(SongsInalbum);
		}
		
		public int getalbumID (int n){
			return albumIDs.get(n);
		}
		
		public int getartistID(int n){
			return artistIDs.get(n);
		}
		
		public ArrayList<Integer> getAlbumIDarray(){
			return albumIDs;
		}
		
		public String getalbumName(int n){
			return albumstrings.get(n);
		}
		
		public String getalbumsSongs(int n){
			return albumsSongs.get(n);
		}
		
	}
	
	class PlaylistsInfo{
		public ArrayList<Integer> playlistIDs;
		public ArrayList<Integer> userIDs;
		public ArrayList<String> playliststrings;
		public ArrayList<String> playlistSongs;
		
		public PlaylistsInfo(){
			playlistIDs = new ArrayList<Integer>();
			userIDs = new ArrayList<Integer>();
			playliststrings = new ArrayList<String>();
			playlistSongs = new ArrayList<String>();
		}
		
		public void addInfo(int playlistID, int userID, String playliststring, String playlistSong ){
			playlistIDs.add(playlistID);
			userIDs.add(userID);
			playliststrings.add(playliststring);
			playlistSongs.add(playlistSong);
		}
		
		public int getplaylistID (int n){
			return playlistIDs.get(n);
		}
		
		public int getuserID(int n){
			return userIDs.get(n);
		}
		
		public ArrayList<Integer> getplaylistIDarray(){
			return playlistIDs;
		}
		
		public String getplaylistName(int n){
			return playliststrings.get(n);
		}
		
		public String getplaylistSongs(int n){
			return playlistSongs.get(n);
		}
		
	}
	
	class MP3 extends Thread{
		
		MP3(String str, String artist){
			try{
				FileInputStream fis = new FileInputStream("Audio/"+ str + "_" + artist+ ".mp3");
				playMp3 = new Player(fis);
			}catch(Exception exce){
				System.out.println(exce);
			}
		}
		
		public void pause(){
			if(playMp3 != null)
				playMp3.close();
		}
		
		public void run(){
			try{
				playMp3.play();
			}catch(Exception e){
				
			}
		}
		
		private Player playMp3;
		
	}
	
	public static void main(String[] args)throws Exception{
		MainFrame mf = new MainFrame();
		//getConnection();
	}
	
	
}