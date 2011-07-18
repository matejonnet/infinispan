/**
 * 
 */
package org.infinispan.demo.web;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import org.infinispan.Cache;
import org.infinispan.ec2demo.Influenza_N_P_CR_Element;

/**
 * @author <a href="mailto:matejonnet@gmail.com">Matej Lazar</a>
 */
public class RandomLoader
{
	private static final int DEFAULT_LOAD_LIMIT = 100;
	private int loadLimit = DEFAULT_LOAD_LIMIT;


	public void load(Cache<String, Influenza_N_P_CR_Element> cache) throws FileNotFoundException, IOException {
		Influenza_Generator iParser = new Influenza_Generator();
		List<Influenza_N_P_CR_Element> iList = iParser.generate();
		
		System.out.println("About to load " + iList.size() + " influenza elements into cache");
		
		int loopCount = 0;
		for (Influenza_N_P_CR_Element x : iList) {
			cache.put(x.getGanNucleoid(), x);
			loopCount++;
		}
		System.out.println("Looped throught " + loopCount + " elements.");
		System.out.println("Loaded " + cache.size() + " influenza elements into cache");
	}
	
	
	class Influenza_Generator {
		private SecureRandom random = new SecureRandom();
		
		public List<Influenza_N_P_CR_Element> generate() {
			List<Influenza_N_P_CR_Element> list = new ArrayList<Influenza_N_P_CR_Element>();
			
			for (int loopCount = 0; loopCount < loadLimit; loopCount++) {
				Influenza_N_P_CR_Element el = new Influenza_N_P_CR_Element();
				el.setGanNucleoid(getRnd());
				el.setProtein_Data(getRnd(), getRnd());
				if ((loopCount % 100) == 0) {
					System.out.println("Added " + loopCount + " Influenza records");
					System.out.println("Last added Influenza_N_P_CR_Element: " + el);
				}
				list.add(el);
			}
			return list;
		}
		
		String getRnd() {
			return new BigInteger(130, random).toString(32);
		}
		
	}


	/**
	 * @param limit
	 */
	public void setLoadLimit(int limit)	{
		if (limit > -1) {
			loadLimit = limit;
		} else {
			loadLimit = DEFAULT_LOAD_LIMIT;
		}
	}
	
}
