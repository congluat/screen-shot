package intershoot;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.Window.Type;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;
import javax.swing.FocusManager;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

public class InterShoot {

	private JFrame frmIntershoot;
	private JTextField txtPfx;
	private JTextField txtX;
	private JTextField txtY;
	private JTextField txtH;
	private JTextField txtW;
	private JTextField txtFolder;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					InterShoot window = new InterShoot();
					window.frmIntershoot.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public InterShoot() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmIntershoot = new JFrame();

		frmIntershoot.setType(Type.UTILITY);
		frmIntershoot.setAlwaysOnTop(true);
		frmIntershoot.setResizable(false);
		frmIntershoot.setTitle("InterShoot");
		frmIntershoot.setBounds(100, 100, 135, 188);
		frmIntershoot.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmIntershoot.getContentPane().setLayout(new FormLayout(
				new ColumnSpec[] { FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"),
						FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), FormSpecs.RELATED_GAP_COLSPEC,
						FormSpecs.DEFAULT_COLSPEC, FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC,
						FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC, FormSpecs.RELATED_GAP_COLSPEC,
						FormSpecs.DEFAULT_COLSPEC, FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC, },
				new RowSpec[] { FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC,
						FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC,
						FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC,
						FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, }));

		JLabel lblNextText = new JLabel("Next");
		frmIntershoot.getContentPane().add(lblNextText, "2, 2");

		final JLabel lblNext = new JLabel("0");
		frmIntershoot.getContentPane().add(lblNext, "4, 2");

		frmIntershoot.addKeyListener(new MKeyListener());

		JLabel lblPrefix = new JLabel("Prefix");
		frmIntershoot.getContentPane().add(lblPrefix, "2, 4, right, default");

		txtPfx = new JTextField();
		frmIntershoot.getContentPane().add(txtPfx, "4, 4");
		txtPfx.setColumns(10);

		txtX = new JTextField();
		txtX.setToolTipText("X");
		txtX.setText("X");
		frmIntershoot.getContentPane().add(txtX, "2, 6, fill, default");
		txtX.setColumns(10);

		txtW = new JTextField();
		txtW.setToolTipText("Width");
		txtW.setText("Width");
		frmIntershoot.getContentPane().add(txtW, "4, 6");
		txtW.setColumns(10);

		txtY = new JTextField();
		txtY.setToolTipText("Y");
		txtY.setText("Y");
		frmIntershoot.getContentPane().add(txtY, "2, 8, fill, default");
		txtY.setColumns(10);

		txtH = new JTextField();
		txtH.setToolTipText("Height");
		txtH.setText("Height");
		frmIntershoot.getContentPane().add(txtH, "4, 8");
		txtH.setColumns(10);
		JButton btnR = new JButton("R");

		txtFolder = new JTextField();
		frmIntershoot.getContentPane().add(txtFolder, "2, 10, 3, 1");
		txtFolder.setColumns(10);

		frmIntershoot.getContentPane().add(btnR, "2, 12, left, default");
		JButton btnCapture = new JButton("Capture");

		initValue();
		/*
		 * Button R Press Event
		 */
		btnR.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String ZERO = 0 + "";
				lblNext.setText(ZERO);
			}
		});

		/*
		 * Button Capture Press Event
		 */
		btnCapture.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String fol = txtFolder.getText();
				String nextI = lblNext.getText();
				int x = Integer.parseInt(txtX.getText());
				int y = Integer.parseInt(txtY.getText());
				int w = Integer.parseInt(txtW.getText());
				int h = Integer.parseInt(txtH.getText());
				boolean isCaptured = capture(fol, nextI, x, y, w, h);
				if (isCaptured) {
					String next = (Integer.parseInt(lblNext.getText()) + 1) + "";

					lblNext.setText(next);
				} else {

				}
			}
		});
		frmIntershoot.getContentPane().add(btnCapture, "4, 12");
	}

	private void initValue() {
		txtFolder.setText("C:\\Screenshot");
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		txtX.setText(0+"");
		txtY.setText(50+"");
		txtW.setText((int)screenSize.getWidth()+"");
		txtH.setText((int)screenSize.getHeight()+"");
	}

	private boolean capture(String fol, String nIndex, int x, int y, int w, int h) {

		Window activeWindow = FocusManager.getCurrentManager().getActiveWindow();
		String folder = "C:\\Screenshot";
		if(!fol.equals("") && !fol.equals(null)){
			folder=fol;
		}
		Path checkExist = Paths.get(folder);
		while (!Files.exists(checkExist)) {
			new File(folder).mkdir();
		}
		String path = folder + "\\";
		String prefix = txtPfx.getText();
		path = path + prefix + "_" + nIndex + ".jpg";
		Rectangle screenRect = new Rectangle(x, y, w, h);
		try {
			BufferedImage capture = new Robot().createScreenCapture(screenRect);
			ImageIO.write(capture, "bmp", new File(path));
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	class MKeyListener extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent event) {
			char ch = event.getKeyChar();
			if (ch == 'a' || ch == 'b' || ch == 'c') {
				System.out.println(event.getKeyChar());
			}
			if (event.getKeyCode() == KeyEvent.VK_HOME) {
				System.out.println("Key codes: " + event.getKeyCode());
			}

		}
	}
}
