package gh.polyu.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class propertyHandle {

	public Properties getProperties(String fileName)
	{
		Properties prop = new Properties();
		File file = new File(fileName);
		InputStream is = null;		
	
        try {
        	if (file.exists()) {
        		is = new FileInputStream(file);
        		prop.load(is); 
        		return prop;
            }else
            {
            	return null;            	
            }  
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		
	}
	
}
