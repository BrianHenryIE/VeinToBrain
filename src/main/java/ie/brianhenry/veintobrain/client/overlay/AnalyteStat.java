package ie.brianhenry.veintobrain.client.overlay;

import com.google.gwt.core.client.JavaScriptObject;

public class AnalyteStat extends JavaScriptObject {

	protected AnalyteStat() {}
	
	public final native double getMin() /*-{
		return this.min;
	}-*/;

	public final native double get2p5th() /*-{
		return this.i2p5th;
	}-*/; 

	public final native double get25th() /*-{
		return this.i25th;
	}-*/;
	
	public final native double getMedian() /*-{
		return this.median;
	}-*/;

	public final native double getMean() /*-{
		return this.mean;
	}-*/; 

	public final native double[] getMode() /*-{
	return this.mode;
	}-*/; 

	public final native double get75th() /*-{
	return this.i75th;
	}-*/;

	public final native double get97p5th() /*-{
		return this.i97p5th;
	}-*/;
	
	public final native double getMax() /*-{
		return this.max;
	}-*/;

}
