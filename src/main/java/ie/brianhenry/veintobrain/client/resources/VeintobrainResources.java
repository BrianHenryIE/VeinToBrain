package ie.brianhenry.veintobrain.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

public interface VeintobrainResources extends ClientBundle {

	public static final VeintobrainResources INSTANCE = GWT.create(VeintobrainResources.class);

	@Source("veintobrain.css")
	Style css();

	public interface Style extends CssResource {
		
		String summaryLab();
		String version();
		String top();
		String middle();
		String menuPanel();
		String appPanel();
		String helpPanel();
		String bottom();
		String copyright();
	}

//	@Source("loading.gif")
//	ImageResource loadingAnimation();
//
//	@Source("smallloading.gif")
//	ImageResource smallLoadingAnimation();
//
//	@Source("redx.png")
//	ImageResource redX();
//
//	@Source("greenplus.png")
//	ImageResource greenPlus();

}