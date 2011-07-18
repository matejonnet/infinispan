/**
 * 
 */
package org.infinispan.demo.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.infinispan.Cache;
import org.infinispan.ec2demo.Influenza_N_P_CR_Element;
import org.infinispan.ec2demo.Influenza_Parser;

/**
 * @author <a href="mailto:matejonnet@gmail.com">Matej Lazar</a>
 */
public class InfluenzaLoader
{
	private InputStream inputStream;
	private int loadLimit = 0;

	public void load(Cache<String, Influenza_N_P_CR_Element> cache) throws FileNotFoundException, IOException
	{
		Influenza_Parser iParser = new Influenza_Parser();
		List<Influenza_N_P_CR_Element> iList = iParser.parseFile(new GZIPInputStream(inputStream));
		
		System.out.println("About to load " + iList.size() + " influenza elements into cache");
		
		int loopCount = 0;
		for (Influenza_N_P_CR_Element x : iList) {
			cache.put(x.getGanNucleoid(), x);
			loopCount++;

			if ((loopCount % 100) == 0) {
				System.out.println("Added " + loopCount + " Influenza records");
				System.out.println("Last added Influenza_N_P_CR_Element: " + x);
			}
			
			if (loadLimit > -1 && loopCount > loadLimit) {
				System.out.println("Load linit " + loadLimit + " reached.");
				break;
			}
		}
		System.out.println("Looped throught " + loopCount + " elements.");
	}
	
	public void setFile(File file) throws FileNotFoundException
	{
		this.inputStream = new FileInputStream(file);
	}

	/**
	 * @param loadLimit the loadLimit to set
	 */
	public void setLoadLimit(int loadLimit)
	{
		this.loadLimit = loadLimit;
	}

	/**
	 * @param resourceAsStream
	 */
	public void setStream(InputStream inputStream)
	{
		this.inputStream = inputStream;
		
	}
}
