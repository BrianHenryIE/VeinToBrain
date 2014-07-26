package ie.brianhenry.veintobrain.representations;

import java.util.List;

import com.kfuntak.gwt.json.serialization.client.JsonSerializable;

/**
 * Config for a particular analyte beyond the dafaults used when computing the stats
 * 
 * Should this be per user?
 * 
 * @author BrianHenry.ie
 *
 */
public class AnalyteConfig implements JsonSerializable {
	
	private String username;
	
	private String analyteType;
	
	private List<Double> additionalPercentiles;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAnalyteType() {
		return analyteType;
	}

	public void setAnalyteType(String analyteType) {
		this.analyteType = analyteType;
	}

	public List<Double> getAdditionalPercentiles() {
		return additionalPercentiles;
	}

	public void setAdditionalPercentiles(List<Double> additionalPercentiles) {
		this.additionalPercentiles = additionalPercentiles;
	}
	

	
	
}
