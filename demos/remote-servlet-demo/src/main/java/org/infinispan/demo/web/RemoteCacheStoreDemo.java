package org.infinispan.demo.web;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.ec2demo.Influenza_N_P_CR_Element;

/**
 * 
 * @author <a href="mailto:matejonnet@gmail.com">Matej Lazar</a>
 */
public class RemoteCacheStoreDemo extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public RemoteCacheStoreDemo() {
        super();
    }

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		handlerequest(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		handlerequest(request, response);
	}


	private void handlerequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
		boolean connected = false;
		
		PrintWriter out = response.getWriter();
		
		String host = request.getParameter("host");
		int port = 11222;
		if (request.getParameter("port") != null) { 
		port = Integer.parseInt(request.getParameter("port"));
		}
		
		String action = request.getParameter("action");
		String query = request.getParameter("query");
		int limit = -1;
		if (request.getParameter("limit") != null){
			limit = Integer.parseInt(request.getParameter("limit"));
		}
		
		response.setContentType("text/html");
		out.println("<html><body>");
		
		out.println("Response from: " + request.getServerName() + " [" + InetAddress.getLocalHost().getHostAddress() + "]<br /><br />");

		RemoteCacheManager cacheManager = null;
		RemoteCache<String, Influenza_N_P_CR_Element> cache = null;

		
		if ("connect".equals(action)) {
			cacheManager = new RemoteCacheManager(host, port);
			getServletContext().setAttribute("cm", cacheManager);
		} else {
			if (getServletContext().getAttribute("cm") != null) {
				cacheManager = (RemoteCacheManager) getServletContext().getAttribute("cm");
			}
		}
		
		if (cacheManager != null) {
			connected = true;
			cache = cacheManager.getCache();
			printCacheManagerInfo(cacheManager, out);
			printCacheInfo(cache, out);
		}
		
		if ("load".equals(action)) {
			InfluenzaLoader il = new InfluenzaLoader();
			il.setLoadLimit(limit);
			InputStream inStream = getClass().getClassLoader().getResourceAsStream("influenza.dat.gz");
			il.setStream(inStream);
			il.load(cache);
			inStream.close();
			out.println("Cache loaded.<br />");

		} else if ("load rnd".equals(action)) {
			RandomLoader il = new RandomLoader();
			il.setLoadLimit(limit);
			il.load(cache);
			out.println("Cache loaded.<br />");
			
		} else if ("search".equals(action)) {
			Influenza_N_P_CR_Element myRec = cache.get(query);
			if (myRec != null) {
				out.println("<br />Result: " + myRec.getGanNucleoid() + ":" + myRec.getProtein_Data() + "<br /><br />");
			} else {
				out.println("Record not found.<br />");
			}
		}
		
		/* html command fields */
		out.println(htmlConnect() + "<br /><br />");
		if (connected) {
			out.println(htmlLoad() + "<br /><br />");
			out.println(htmlSearch() + "<br /><br />");
		}
		
		out.println("</body></html>");
	}
	
	/**
	 * @param cache
	 * @param out
	 */
	private void printCacheInfo(RemoteCache<String, Influenza_N_P_CR_Element> cache, PrintWriter out) {
		out.println("<br />Cache statistics:<br />");
		for (Map.Entry<String, String> entry : cache.stats().getStatsMap().entrySet()) {
			out.println(entry.getKey() + " - " + entry.getValue() + ":<br />");
		}
	}

	/**
	 * @param out
	 * @param cacheManager 
	 */
	private void printCacheManagerInfo(RemoteCacheManager cacheManager, PrintWriter out)	{
		out.println("<br />Cache manager properties:<br />");
		for (Entry<Object, Object> entry : cacheManager.getProperties().entrySet()) {
			out.println(entry.getKey() + " - " + entry.getValue() + ":<br />");
		}
		
	}

	private String htmlSearch() {
		StringBuilder builder = new StringBuilder();
		builder.append("<form metgod=\"get\">");
		builder.append("<input type=\"text\" name=\"query\" />");
		builder.append("<input type=\"submit\" name=\"action\" value=\"search\"/>");
		builder.append("</form>");
		return builder.toString();
	}

	private String htmlLoad() {
		StringBuilder builder = new StringBuilder();
		builder.append("<form metgod=\"get\">");
		builder.append("<input type=\"text\" name=\"limit\" value=\"-1\"/>");
		builder.append("<input type=\"submit\" name=\"action\" value=\"load\" title=\"limit -1 means load all\" />");
		builder.append("<input type=\"submit\" name=\"action\" value=\"load rnd\" title=\"limit -1 means load default (100)\" />");
		builder.append("</form>");
		return builder.toString();
	}

	private String htmlConnect() throws UnknownHostException {
		StringBuilder builder = new StringBuilder();
		builder.append("<form metgod=\"get\">");
		builder.append("<input type=\"text\" name=\"host\" value=\"" + InetAddress.getLocalHost().getHostAddress() + "\"/> ");
		builder.append("<input type=\"text\" name=\"port\" value=\"11222\" /> ");
		builder.append("<input type=\"submit\" name=\"action\" value=\"connect\"/>");
		builder.append("</form>");
		return builder.toString();
	}

	
}
