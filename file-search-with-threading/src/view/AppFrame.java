package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;

public class AppFrame extends JFrame {
	//File Search Inputs
	private JButton chooseRoot = new JButton("Select Directory");
	private JTextField dirPathViewer = new JTextField(45);

	//global inputs
	private JFileChooser fileChooser = new JFileChooser();
	private JTextField search = new JTextField(65);
	private JTextArea display = new JTextArea();
	private JScrollPane displayScroller;
	private JTable fileListTable = new JTable();
	private DefaultTableModel tableModel = new DefaultTableModel();
	private JLabel statusLabel = new JLabel(" ");

	private Highlighter.HighlightPainter mark = new MatchHighlighter(Color.YELLOW);

	private class MatchHighlighter extends DefaultHighlighter.DefaultHighlightPainter{

		public MatchHighlighter(Color color) {
			super(color);
		}
	}
	
	private JPanel getFileSearchPanel(){
		JPanel fileSearchPanel = new JPanel();

		fileSearchPanel.add(new JLabel("Directory: "));
		
		fileSearchPanel.add(dirPathViewer);
		dirPathViewer.setEditable(false);
		
		fileSearchPanel.add(chooseRoot);
		
		chooseRoot.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				if(fileChooser.showOpenDialog(null)==JFileChooser.APPROVE_OPTION){
					dirPathViewer.setText(fileChooser.getSelectedFile().getPath());
				}
			}
		});
		
		return fileSearchPanel;
	}
	
	public AppFrame(){
		initUI();
	}
	
	public void initUI(){
		this.setTitle("Projects");
		this.setMinimumSize(new Dimension(800,450));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel wrapper = new JPanel();
		wrapper.setLayout(new BorderLayout());
		
		JPanel innerTopWrapper = new JPanel();
		innerTopWrapper.setLayout(new GridLayout(2,1));
		
		JTabbedPane tabPane = new JTabbedPane();
		tabPane.add("Search For File", getFileSearchPanel());
		
		JPanel searchPanel = new JPanel();
		searchPanel.add(new JLabel("Search: "));
		searchPanel.add(search);
		
		innerTopWrapper.add(tabPane);
		innerTopWrapper.add(searchPanel);
		
		JPanel displayPanel = new JPanel();
		displayPanel.setLayout(new GridLayout(1,1));
		displayPanel.setBorder(BorderFactory.createTitledBorder("Result"));
		displayScroller = new JScrollPane(display);
		displayPanel.add(displayScroller);
		display.setEditable(false);
		
		JPanel statusPanel = new JPanel();
		statusPanel.setLayout(new GridLayout(1,1));
		statusPanel.setBackground(new Color(225,225,225));
		statusPanel.setBorder(BorderFactory.createRaisedBevelBorder());
		statusPanel.add(statusLabel);
		
		wrapper.add(innerTopWrapper, BorderLayout.NORTH);
		wrapper.add(displayPanel, BorderLayout.CENTER);
		wrapper.add(statusPanel, BorderLayout.SOUTH);
		
		add(wrapper);
	}
	
	public String getRootPath(){
		return dirPathViewer.getText();
	}
	
	public String getSearchPattern(){
		return search.getText().toLowerCase();
	}
	
	public JTextArea getDisplay(){
		return display;
	}
	
	public JLabel getStatusBar(){
		return statusLabel;
	}
	
	public void setStatusLabel(String status){
		statusLabel.setText(status);
	}
	
	public void clearDisplay(){
		display.setText("");
		display.getHighlighter().removeAllHighlights();
		statusLabel.setText("");
		displayScroller.repaint();
	}
	
	public void appendResult(String filename, String filePath){
		Highlighter marker = display.getHighlighter();
		String pat = this.getSearchPattern();
		String oldText = display.getText();
		display.append(filename+" ");
		String newText = display.getText();
		int from = oldText.length();
		int index = newText.toLowerCase().indexOf(pat, from);
		try {
			marker.addHighlight(index, index+pat.length(), mark);	
		} catch (BadLocationException e) {
			System.out.println(index+ " ---  "+(index+pat.length())+"----"+filename);
//			e.printStackTrace();
		}
		display.append("\t\t ---> \t"+filePath+"\n");
		displayScroller.repaint();
	}
	
	public void addSearchKeyListener(KeyListener listener){
		search.addKeyListener(listener);
	}

}
