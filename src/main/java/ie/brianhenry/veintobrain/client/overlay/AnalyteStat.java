package ie.brianhenry.veintobrain.client.overlay;

import java.util.Date;

import com.google.gwt.core.client.JavaScriptObject;

public class AnalyteStat extends JavaScriptObject {

	protected AnalyteStat() {}
	
	public final native double getMin() /*-{
		return this.min;
	}-*/;
	
	public final native double getMean() /*-{
		return this.mean;
	}-*/; 

	public final native double[] getMode() /*-{
	return this.mode;
	}-*/; 
	
	public final native double getMax() /*-{
		return this.max;
	}-*/;

	public final double getPercentile(double p){
		return this.getPercentiles().getPercentile(p);
	}

	public final native Percentiles getPercentiles() /*-{
		return this.percentiles;
	}-*/;
	
    public static class Percentiles extends JavaScriptObject {

		 protected Percentiles() { }

			public final native double getPercentile(double p) /*-{
				return this[p];
			}-*/;
		 
    }
    
	public final native Date getDate() /*-{
		return this.date;
	}-*/;
}