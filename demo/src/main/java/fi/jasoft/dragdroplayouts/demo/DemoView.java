package fi.jasoft.dragdroplayouts.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;

public abstract class DemoView extends CustomComponent implements View {

	private final Navigator navigator;
	
	public DemoView(Navigator navigator) {
		this.navigator = navigator;
		setSizeFull();
		setCompositionRoot(getLayout());
	}
	
	@Override
	public void enter(ViewChangeEvent event) {		
	}
	
	public String getSource(){
		 String path = getClass().getCanonicalName().replaceAll("\\.", "/")
	                + ".java";

	        InputStream is = getClass().getClassLoader().getResourceAsStream(path);
	        if (is == null) {	         
	            return "No source code available.";
	        }

	        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

	        try{
	        
	        	boolean inCodeBlock = false;
	        	
	        StringBuilder codelines = new StringBuilder();
	        String line = reader.readLine();
	        while (line != null) {	        
	        	if(line.contains("//start-source")){
	        		inCodeBlock = true;
	        	} else if(line.contains("//end-source")){
	        		inCodeBlock = false;
	        	} else if(inCodeBlock){
	        		codelines.append(line);
	                codelines.append("\n");
	        	}
	            line = reader.readLine();
	        }

	        reader.close();

	        String code = codelines.toString();
	        
	        return code;
	        } catch (IOException e) {
	        	 return "No source code available.";
	        }
	}
	
	public abstract Component getLayout();
	
	public abstract String getCaption();
	
}