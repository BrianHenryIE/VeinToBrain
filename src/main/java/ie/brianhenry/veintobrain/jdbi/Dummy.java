package ie.brianhenry.veintobrain.jdbi;

import ie.brianhenry.veintobrain.core.AnalyteDate;
import ie.brianhenry.veintobrain.core.AnalyteStat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Dummy {

	@SuppressWarnings({ "static-access", "deprecation" })
	public static AnalyteStat getIndividualStat(){

		Date statDate = new Date(new Date().UTC(2009, 0, 2, 0,0,0));//, hrs, min, sec);
		
		String statType = "Folate";
		
		String[] readings = {"11.1","14.5","17.9","8.7","15.7","12.3","2.8","14.9","4.5","7.8","6.6","10.6","23.0","19.0","4.7","9.5","7.1","13.5","9.5","3.5","16.1","21.7","11.2","9.8","13.8","20.7","6.6","4.2","6.2","19.9","15.9","21.8","9.0"};
		
		// Maybe this should take in an array of number and just have getters
		AnalyteDate folate02Jan2009 = new AnalyteDate(statType, statDate, readings);

		return new AnalyteStat(folate02Jan2009);
		
	}
	
	@SuppressWarnings({ "static-access", "deprecation" })
	public static List<AnalyteStat> getListOfStats(String name) {
		
		List<AnalyteStat> list = new ArrayList<AnalyteStat>();
		
		if(name.toLowerCase().equals("folate")){
			String statType = "Folate";
			
			list.add(new AnalyteStat(new AnalyteDate(statType, new Date(new Date().UTC(2009, 0, 2, 0,0,0)), new String[]{"11.1","14.5","17.9","8.7","15.7","12.3","2.8","14.9","4.5","7.8","6.6","10.6","23.0","19.0","4.7","9.5","7.1","13.5","9.5","3.5","16.1","21.7","11.2","9.8","13.8","20.7","6.6","4.2","6.2","19.9","15.9","21.8","9.0"})));
			list.add(new AnalyteStat(new AnalyteDate(statType, new Date(new Date().UTC(2009, 0, 5, 0,0,0)), new String[]{"6.8","11.8","7.2","13.4","15.9","19.9","7.2","9.7","6.2","9.2","17.6","13.6","15.3","19.0","12.6","5.9","15.5","15.1","4.8","16.2","6.2","4.6","10.4","11.0","16.4","7.1","4.6","12.1","7.3","16.3","19.3","15.2","12.4","18.9","2.9","4.4","15.3","11.4","5.5","15.4","13.5","13.5","14.6","5.5","6.3","12.6","17.1","14.3","16.7","4.7","5.5","7.2","18.5","6.7","23.9","19.4","11.9","19.9","5.7","6.6","6.1","12.5","18.9","7.6","8.9","6.5","8.4","18.3","19.0","6.4","7.3","13.1","13.2","4.3","11.2","7.3","5.0","14.2","13.4","21.9"})));
			list.add(new AnalyteStat(new AnalyteDate(statType, new Date(new Date().UTC(2009, 0, 6, 0,0,0)), new String[]{"12.4","12.4","9.7","10.5","17.4","6.3","20.7","11.4","8.6","14.3","21.0","9.7","5.4","17.5","21.1","7.5","22.2","9.4","19.6","7.5","21.6","10.5","14.5","4.6","12.8","5.4","16.1","19.9","6.4","11.8","11.2","8.6","11.1","11.4","10.0","4.8","6.2","14.3","5.0","10.0","13.7","8.9","11.2","12.6","22.1","6.7","10.7","10.1","17.6","10.0","13.5","6.5","18.1","13.6","14.7","18.1","5.9","7.4","9.5","13.3","21.3","3.7","16.4","12.1","17.3","9.2","9.3","8.8","21.1","12.3","4.6","12.6","5.6","20.0","20.5","16.3","4.2","11.0","12.5","8.7","6.7","7.1","3.3","5.1","11.1","10.2","11.4","8.6","18.4","7.2","18.6","5.3","10.1","11.3","16.5","16.2","6.9","5.7","13.3","6.2","7.1"})));
			list.add(new AnalyteStat(new AnalyteDate(statType, new Date(new Date().UTC(2009, 0, 7, 0,0,0)), new String[]{"15.3","4.9","18.2","11.7","14.4","9.3","10.0","12.7","21.1","9.2","10.9","14.9","22.3","6.1","6.0","20.4","8.9","9.8","11.0","6.2","15.2","21.4","20.6","20.7","19.1","6.6","7.4","9.6","10.9","6.3","11.5","13.4","11.1","6.1","15.9","12.7","14.3","22.2","2.5","3.6","10.3","9.2","15.9","9.3","9.6","6.6","10.8","14.2","12.6","12.4","6.4","14.6","7.3","17.0","10.5","9.6","11.9","16.0","14.4","18.7","14.1","6.1","11.2","12.2","12.4","9.9","8.5","15.5","22.7","10.0","7.2","15.1","9.6","10.1","15.5","16.4","21.9","8.5","7.6","11.6","11.4","18.8","14.8","17.8","7.0","10.5","17.3","8.8","4.6","11.9","8.6","6.9","7.0","6.1"})));
			list.add(new AnalyteStat(new AnalyteDate(statType, new Date(new Date().UTC(2009, 0, 8, 0,0,0)), new String[]{"13.0","12.7","12.9","14.7","10.8","11.0","6.9","7.4","14.3","19.3","15.4","7.8","9.7","9.4","19.4","13.4","19.7","23.8","6.5","14.9","3.7","10.4","13.8","15.1","11.1","19.9","11.8","7.4","8.6","17.0","10.2","21.8","22.2","6.3","22.4","15.4","13.0","10.0","13.4","8.9","19.4","11.6","22.8","8.7","9.8","17.4","12.4","6.4","20.9","7.0"})));
			list.add(new AnalyteStat(new AnalyteDate(statType, new Date(new Date().UTC(2009, 0, 9, 0,0,0)), new String[]{"11.0","3.1","20.0","8.0","5.4","11.9","5.7","8.0","13.9","15.2","4.2","5.5","6.0","13.0","9.0","16.5","8.8","3.9","8.5","8.9","4.8","7.9","15.3","23.6","16.2","13.3","19.5","5.4","12.6","16.6","12.0","14.0","11.9","10.2","9.0","19.8","9.2","14.4","8.1","4.6","21.3","7.6","8.9","22.1","8.0","5.9","9.3","11.6","17.6","20.4","7.8","14.4","7.7","5.6","4.9","7.6","6.6","19.5","5.8","16.6","18.4","12.0","13.3","8.0","16.4","10.0","8.0","6.0","7.4","9.8","15.2","11.3","20.5","10.3","12.9","22.7","4.9","7.5","13.5","4.4","8.3","10.1","12.9","4.9","18.3","7.6","15.8","7.6"})));
	
		}
		
		if(name.toLowerCase().equals("psa")){
		
			list.add(new AnalyteStat(PSAdata.psaJuly()));
			list.add(new AnalyteStat(PSAdata.psaAugust()));
			list.add(new AnalyteStat(PSAdata.psaSeptember()));
			list.add(new AnalyteStat(PSAdata.psaOctober()));
			list.add(new AnalyteStat(PSAdata.psaNovember()));
			list.add(new AnalyteStat(PSAdata.psaDecember()));
			list.add(new AnalyteStat(PSAdata.psaJan()));
			list.add(new AnalyteStat(PSAdata.psaFeb()));
			list.add(new AnalyteStat(PSAdata.psaMarch()));
			list.add(new AnalyteStat(PSAdata.psaApril()));
			list.add(new AnalyteStat(PSAdata.psaMay()));
			list.add(new AnalyteStat(PSAdata.psaJune()));
		}
		
		
		return list;
	}
	
}
