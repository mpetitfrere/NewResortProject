package applicationPackage;

import java.io.File;

import javax.swing.filechooser.*;

public class ExcelFilter extends FileFilter {

	private String format = "xlsx";
	private char dotIndex = '.';
	
	public boolean accept(File f) {
		if(f.isDirectory())
		{
			return true;
		}
		if(extension(f).equalsIgnoreCase(format))
		{
			return true;

		}
		else
			return false;
	}

	public String getDescription() {
		return "Excel .xls Only";
	}
	public String extension(File f)
	{
		String fileName = f.getName();
		int indexFile = fileName.indexOf(dotIndex);
		if(indexFile > 0 && indexFile < fileName.length()-1)
		{
			return fileName.substring(indexFile+1);
		}
		else
			return "";
	}
	

}
