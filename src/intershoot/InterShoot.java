package intershoot;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.Window.Type;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.FocusManager;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

import lc.kra.system.keyboard.GlobalKeyboardHook;
import lc.kra.system.keyboard.event.GlobalKeyAdapter;
import lc.kra.system.keyboard.event.GlobalKeyEvent;

public class InterShoot implements ClipboardOwner {

	private JFrame frmIntershoot;
	private JTextField txtPfx;
	private JTextField txtX;
	private JTextField txtY;
	private JTextField txtH;
	private JTextField txtW;
	private JTextField txtFolder;
	private List<String> imgList = new ArrayList<String>();
	private String folder = "C:\\Screenshot";
	private static boolean run = true;
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
		frmIntershoot.setTitle("Screenshot");
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
				try {
					exportDocFile();
				} catch (InvalidFormatException e1) {			
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				imgList.clear();
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
		
		// might throw a UnsatisfiedLinkError if the native library fails to
		// load or a RuntimeException if hooking fails
		GlobalKeyboardHook keyboardHook = new GlobalKeyboardHook();

		System.out.println("Global keyboard hook successfully started, press [escape] key to shutdown.");
		keyboardHook.addKeyListener(new GlobalKeyAdapter() {
			@Override
			public void keyPressed(GlobalKeyEvent event) {
				String fol = txtFolder.getText();
				String nextI = lblNext.getText();
				int x = Integer.parseInt(txtX.getText());
				int y = Integer.parseInt(txtY.getText());
				int w = Integer.parseInt(txtW.getText());
				int h = Integer.parseInt(txtH.getText());
				
				System.out.println(event);
				/*
				 * if (event.getVirtualKeyCode() == GlobalKeyEvent.VK_A) run =
				 * false;
				 */
				if (event.getVirtualKeyCode() == GlobalKeyEvent.VK_SNAPSHOT) {
					boolean isCaptured = capture(fol, nextI, x, y, w, h);
					if (isCaptured) {
						String next = (Integer.parseInt(lblNext.getText()) + 1) + "";

						lblNext.setText(next);
					} else {

					}
				}
				if (event.getVirtualKeyCode() == GlobalKeyEvent.VK_END) {
					try {
						Thread.sleep(100);
						Robot r = new Robot();
						r.keyPress(KeyEvent.VK_CONTROL);
						r.keyPress(KeyEvent.VK_V);
						r.keyRelease(KeyEvent.VK_V);
						r.keyRelease(KeyEvent.VK_CONTROL);
						// scroll and wait a bit to give the impression of
						// smooth scrolling
					
						r.mouseWheel(h/60);
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
						}

					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (AWTException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}

			@Override
			public void keyReleased(GlobalKeyEvent event) {
				System.out.println(event);
			}
		});

		/*try {
			while (run)
				Thread.sleep(128);
		} catch (InterruptedException e) {
			 nothing to do here  } finally {
			keyboardHook.shutdownHook();
		}*/
	}

	private void initValue() {
		txtFolder.setText("C:\\Screenshot");
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		/*txtX.setText(0+"");
		txtY.setText(50+"");
		txtW.setText((int)screenSize.getWidth()+"");
		txtH.setText((int)screenSize.getHeight()+"");*/
		txtX.setText(0+"");
		txtY.setText(0+"");
		txtW.setText(1120+"");
		txtH.setText(720+"");
	}

	private boolean capture(String fol, String nIndex, int x, int y, int w, int h) {

		Window activeWindow = FocusManager.getCurrentManager().getActiveWindow();
		
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
			imgList.add(path);
			TransferableImage trans = new TransferableImage( capture );
            Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
            c.setContents( trans, this );
            
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

	public void lostOwnership(Clipboard clipboard, Transferable contents) {
		System.out.println( "Lost Clipboard Ownership" );
		
	}
	
	public void exportDocFile() throws InvalidFormatException, IOException{
		XWPFDocument doc  = new XWPFDocument();
        XWPFParagraph title  = doc.createParagraph();
        XWPFRun run  = title.createRun();
//        run.setText("Fig.1 A Natural Scene");
//        run.setBold(true);
//        title.setAlignment(ParagraphAlignment.CENTER);

        
        for (int i = 0; i < imgList.size(); i++){
        	FileInputStream is = new FileInputStream(imgList.get(i));
        	//run.addPicture(is, XWPFDocument.PICTURE_TYPE_JPEG, imgFile, Units.toEMU(200), Units.toEMU(200)); // 200x200 pixels
        	BufferedImage img = ImageIO.read(new File(imgList.get(i)));
        	int width          = img.getWidth();
        	int height         = img.getHeight();
        	System.out.println("Printed file: "+imgList.get(i)+ "-"+ width +"-"+height);
        	run.addPicture(is, XWPFDocument.PICTURE_TYPE_JPEG, imgList.get(i) , Units.toEMU(width/3), Units.toEMU(height/3));
        	run.addBreak();
        	run.addBreak();
        	is.close();
		}
        String name = new Date().toString().replaceAll(" ", "").replaceAll(":", "").toLowerCase();
        FileOutputStream fos = new FileOutputStream(folder+"\\"+name+".docx");
        doc.write(fos);
        fos.close(); 
        
	}
}
