package iliaxcorp.imagecomp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Neuron implements Serializable {
	
	public static final int SLEEP_COUNT = 0;
	
	private static final long serialVersionUID = 8492166129539633455L;

	private static final int DEFAULT_LINKS_COUNT = 16;
	
	private List<Color> links = new ArrayList<Color>(DEFAULT_LINKS_COUNT);
	
	private int active = 0;
	
	public Neuron() {
	}
	
	public Color getLinkColor(int linkNumber){
		return links.get(linkNumber);
	}
	
	public void setLinkColor(int linkNumber, Color color){
		if (linkNumber >= links.size()) {
			for (int i = 0; i < linkNumber - links.size() + 1; i++) {
				links.add(null);
			}
		}
		links.set(linkNumber, color);
	}
	
	public int linksCount(){
		return links.size();
	}
	
	public List<Color> getLinks(){
		return links;
	}
	
	public void setLinks(List<Color> links) {
		this.links = links;
	}
	
	@Override
	public String toString() {
		return links.toString();
	}
	
	public int getActive() {
		return active;
	}
	
	public void setActive(int active) {
		this.active = active;
	}
}
