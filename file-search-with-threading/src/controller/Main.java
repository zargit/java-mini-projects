package controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JOptionPane;

import view.*;

public class Main {
	
	private AppFrame mainFrame = new AppFrame();
	
	private void bfs(String rootPath, String pat) throws FileNotFoundException{
		int found = 0;
		File root = new File(rootPath);
		Queue<File> q = new LinkedList<File>();
		q.add(root);
		while(!q.isEmpty()){
			File folder = (File) q.peek();
			q.remove();
			File[] files = folder.listFiles();
			if(files == null) continue;
			for(int i=0;i<files.length;i++){
				if(files[i].isDirectory()){
					q.add(files[i]);
				}else{
					if(files[i].getName().toLowerCase().lastIndexOf(pat)>-1){
						mainFrame.appendResult(files[i].getName(), files[i].getParentFile().getAbsolutePath());
						found++;
					}
				}
			}

			mainFrame.setStatusLabel(found+" match found. Searching ...");
		}
		mainFrame.setStatusLabel(found+" match found. Done");
	}
	
	private class SearchKeyListener implements KeyListener{
		KeyListenerThread t = new KeyListenerThread("searching");
		public void keyPressed(KeyEvent arg0) {
			// not needed
		}
		public void keyReleased(KeyEvent k) {
			char ch = k.getKeyChar();
			if(!Character.isDefined(ch) || (k.isActionKey())){
				return;
			}
			if(t.started){
				t.stop();
			}
			String pat = mainFrame.getSearchPattern();
			if(pat.length()>0){
				String rootPath = mainFrame.getRootPath();
				t.start(rootPath, pat);
			}else{
				mainFrame.clearDisplay();
			}
		}
		public void keyTyped(KeyEvent arg0) {
			// not needed
		}		
	}
	
	private class KeyListenerThread implements Runnable{
		Thread thread;
		String name,rootPath,pat;
		boolean started = false;
		
		public KeyListenerThread(String name){
			this.name = name;
		}
		
		public void run() {
			started = true;
			if(pat.length()>0 && rootPath.trim().length()>0){
				String rootPath = mainFrame.getRootPath();
				try {
					mainFrame.clearDisplay();
					bfs(rootPath, pat);
				} catch (FileNotFoundException e) {
					JOptionPane.showMessageDialog(null, "Choose a proper directory path.");
					e.printStackTrace();
				}
			}else{
				mainFrame.clearDisplay();
			}
		}
		
		public void start(String rootPath, String pat){
			this.rootPath = rootPath;
			this.pat = pat;
			if(!started){
				thread = new Thread (this, name);
				thread.start ();
			}
		}
		
		public void stop(){
			started = false;
			thread.stop();
		}
	}
	
	public Main(){
		mainFrame.setVisible(true);
		
		//adding the listeners
		mainFrame.addSearchKeyListener(new SearchKeyListener());
		
	}
	
	public static void main(String[] args) {
		Main app = new Main();
	}
}
