package ApiConsumer;


import org.h2.jdbcx.JdbcDataSource;
import org.pmw.tinylog.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.ib.client.*;
import com.ib.client.Bar;

import Formulas.F1CoreCalculation;
import Formulas.F2CoreCalculation;
import Formulas.F3CoreCalculation;
import Formulas.F4CoreCalculation;
import Formulas.F5CoreCalculation;
import commons.db_commons;

public class consumeIB implements EWrapper 
{
	
	
	private EClientSocket clientSocket;
	private EReaderSignal readerSignal;
	private EReader reader;
	public String secTickTime;
	public int loopcount=24;
	public boolean isDataGot=false;
	public boolean isFirstData=false;
	db_commons dbobj = new db_commons();
	public int tradecount =0;
	public  String currentsymbol;
	
	public String [][] tradeData;
	public static SimpleDateFormat fmt;

	
	
	public String[] calculateF1PL(String [] contractDetails, String dateStartTime, String WhichData, String researchtype) throws ParseException
	{
		
		
		//assignF1Variable(dateStartTime.split(" ")[0]);
		String [] plData = null;
		double f1pl=0.00, f1percent=0,flTradecount=0;
		readerSignal = new EJavaSignal();
		clientSocket = new EClientSocket(this, readerSignal);
        String formattedStartDate =  dateStartTime.split(" ")[0]+" ";
        secTickTime= dateStartTime.split(" ")[1];
        int F1T=0, F2T=0,F3T=0,F4T=0,F5T=0;
		try
		{
			clientSocket.eConnect("127.0.0.1", 7497, 0);
			System.out.println("clientSocket is connected : "+clientSocket.isConnected());
			reader = new EReader(clientSocket, readerSignal);   
	        reader.start();
	        Contract cont = new ContractDetails().contract();
	        cont.symbol(contractDetails[0].trim());
    			cont.currency(contractDetails[2].trim());cont.exchange(contractDetails[3].trim());
	        if (researchtype == "STK")
	        {
	        		cont.secType(contractDetails[1].trim());
	        }
	        else if (researchtype == "FUT")
	        {
	        		cont.secType(contractDetails[1].trim());
	        		cont.lastTradeDateOrContractMonth(contractDetails[4].trim());
	        }
	        else if (researchtype == "OPT")
	        {
	        		cont.secType(contractDetails[1].trim());
	        		cont.lastTradeDateOrContractMonth(contractDetails[4].trim());
	        		cont.strike(Double.parseDouble((contractDetails[5].trim())));
	        		
	        		if (contractDetails[6].trim() == "CALL")
	        		{
	        			cont.right("C");
	        		}
	        		else 
	        		{
	        			cont.right("P");
	        		}
	        }
	        else if (researchtype == "IND")
	        {
	        		cont.secType(contractDetails[1].trim());
	        }
	        
            currentsymbol = contractDetails[0].trim();
	        new Thread(() -> {
	            while (clientSocket.isConnected()) {
	            	readerSignal.waitForSignal();
	                try {
	                    reader.processMsgs();	                    
	                } catch (Exception e) {
	                    System.out.println("Exception: "+e.getMessage());
	                }
	            }
	        }).start();
	        for (int i=1; i<loopcount; i++)
            {
	        		if  ((isDataGot==true))
	        		{
	        			break;
	        		}
		        	System.out.println(i+" checking for legs * "+formattedStartDate+secTickTime);
	            clientSocket.reqHistoricalTicks(i, cont, formattedStartDate+secTickTime , null, 1000, WhichData, 1, true, null);
	            Thread.sleep(4000);
	            if (tradeData == null)
	            {
	            		
	            		if (!clientSocket.isConnected())
	            		{
	            			clientSocket.eConnect("127.0.0.1", 7497, 0);
	            			System.out.println("clientSocket is connected : "+clientSocket.isConnected());
	            			reader = new EReader(clientSocket, readerSignal);   
	            	        reader.start();
	            			System.out.println("trying to connect");
	            			 new Thread(() -> {
	            		            while (clientSocket.isConnected()) {
	            		            	readerSignal.waitForSignal();
	            		                try {
	            		                    reader.processMsgs();	                    
	            		                } catch (Exception e) {
	            		                    System.out.println("Exception: "+e.getMessage());
	            		                }
	            		            }
	            		        }).start();
	            		}
	            		System.out.println("clientSocket is connected : "+clientSocket.isConnected());
	            		System.out.println("retrying");
	            		clientSocket.cancelHistoricalData(i);
	            	    clientSocket.reqHistoricalTicks(i, cont, formattedStartDate+secTickTime , null, 1000, WhichData, 1, true, null);
	            	    Thread.sleep(4000);
	            }
	            
	           
	            if ((tradeData != null))
	            {

	            		
	            			F1CoreCalculation F1Calobj=new F1CoreCalculation();
	            			F1Calobj.Calculate(i,dateStartTime, contractDetails[0].trim(),tradeData);
	            			
	            			F2CoreCalculation F2Calobj=new F2CoreCalculation();
	            			F2Calobj.Calculate(i,dateStartTime, contractDetails[0].trim(),tradeData);
	            		
	            			F3CoreCalculation F3Calobj=new F3CoreCalculation();
	            			F3Calobj.Calculate(i,dateStartTime, contractDetails[0].trim(),tradeData);
	            		    
	            			F4CoreCalculation F4Calobj=new F4CoreCalculation();
	            			F4Calobj.Calculate(i,dateStartTime, contractDetails[0].trim(),tradeData);
	            		
	            			F5CoreCalculation F5Calobj=new F5CoreCalculation();
	            			F5Calobj.Calculate(i,dateStartTime, contractDetails[0].trim(),tradeData);
	            		
		            
	            }
	            
            }
		}
		catch(Exception ex)
		{
			System.out.println(ex.toString());	
		}
		finally	
		{			
			//clientSocket.cancelHistoricalData(101);
			clientSocket.eDisconnect();
			System.out.println("clientSocket is connected ?: "+clientSocket.isConnected());
			
			
		}
		return plData;
	}

	@Override
	public void tickPrice(int tickerId, int field, double price, TickAttr attrib) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tickSize(int tickerId, int field, int size) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tickOptionComputation(int tickerId, int field, double impliedVol, double delta, double optPrice,
			double pvDividend, double gamma, double vega, double theta, double undPrice) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tickGeneric(int tickerId, int tickType, double value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tickString(int tickerId, int tickType, String value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tickEFP(int tickerId, int tickType, double basisPoints, String formattedBasisPoints,
			double impliedFuture, int holdDays, String futureLastTradeDate, double dividendImpact,
			double dividendsToLastTradeDate) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void orderStatus(int orderId, String status, double filled, double remaining, double avgFillPrice,
			int permId, int parentId, double lastFillPrice, int clientId, String whyHeld, double mktCapPrice) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void openOrder(int orderId, Contract contract, Order order, OrderState orderState) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void openOrderEnd() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateAccountValue(String key, String value, String currency, String accountName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updatePortfolio(Contract contract, double position, double marketPrice, double marketValue,
			double averageCost, double unrealizedPNL, double realizedPNL, String accountName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateAccountTime(String timeStamp) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void accountDownloadEnd(String accountName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void nextValidId(int orderId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void contractDetails(int reqId, ContractDetails contractDetails) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void bondContractDetails(int reqId, ContractDetails contractDetails) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void contractDetailsEnd(int reqId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void execDetails(int reqId, Contract contract, Execution execution) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void execDetailsEnd(int reqId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateMktDepth(int tickerId, int position, int operation, int side, double price, int size) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateMktDepthL2(int tickerId, int position, String marketMaker, int operation, int side, double price,
			int size) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateNewsBulletin(int msgId, int msgType, String message, String origExchange) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void managedAccounts(String accountsList) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void receiveFA(int faDataType, String xml) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void historicalData(int reqId, Bar bar) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void scannerParameters(String xml) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void scannerData(int reqId, int rank, ContractDetails contractDetails, String distance, String benchmark,
			String projection, String legsStr) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void scannerDataEnd(int reqId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void realtimeBar(int reqId, long time, double open, double high, double low, double close, long volume,
			double wap, int count) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void currentTime(long time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fundamentalData(int reqId, String data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deltaNeutralValidation(int reqId, DeltaNeutralContract underComp) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tickSnapshotEnd(int reqId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void marketDataType(int reqId, int marketDataType) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void commissionReport(CommissionReport commissionReport) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void position(String account, Contract contract, double pos, double avgCost) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void positionEnd() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void accountSummary(int reqId, String account, String tag, String value, String currency) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void accountSummaryEnd(int reqId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void verifyMessageAPI(String apiData) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void verifyCompleted(boolean isSuccessful, String errorText) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void verifyAndAuthMessageAPI(String apiData, String xyzChallenge) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void verifyAndAuthCompleted(boolean isSuccessful, String errorText) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void displayGroupList(int reqId, String groups) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void displayGroupUpdated(int reqId, String contractInfo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void error(Exception e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void error(String str) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void error(int id, int errorCode, String errorMsg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void connectionClosed() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void connectAck() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void positionMulti(int reqId, String account, String modelCode, Contract contract, double pos,
			double avgCost) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void positionMultiEnd(int reqId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void accountUpdateMulti(int reqId, String account, String modelCode, String key, String value,
			String currency) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void accountUpdateMultiEnd(int reqId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void securityDefinitionOptionalParameter(int reqId, String exchange, int underlyingConId,
			String tradingClass, String multiplier, Set<String> expirations, Set<Double> strikes) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void securityDefinitionOptionalParameterEnd(int reqId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void softDollarTiers(int reqId, SoftDollarTier[] tiers) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void familyCodes(FamilyCode[] familyCodes) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void symbolSamples(int reqId, ContractDescription[] contractDescriptions) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void historicalDataEnd(int reqId, String startDateStr, String endDateStr) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mktDepthExchanges(DepthMktDataDescription[] depthMktDataDescriptions) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tickNews(int tickerId, long timeStamp, String providerCode, String articleId, String headline,
			String extraData) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void smartComponents(int reqId, Map<Integer, Entry<String, Character>> theMap) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tickReqParams(int tickerId, double minTick, String bboExchange, int snapshotPermissions) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void newsProviders(NewsProvider[] newsProviders) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void newsArticle(int requestId, int articleType, String articleText) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void historicalNews(int requestId, String time, String providerCode, String articleId, String headline) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void historicalNewsEnd(int requestId, boolean hasMore) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void headTimestamp(int reqId, String headTimestamp) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void histogramData(int reqId, List<HistogramEntry> items) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void historicalDataUpdate(int reqId, Bar bar) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void rerouteMktDataReq(int reqId, int conId, String exchange) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void rerouteMktDepthReq(int reqId, int conId, String exchange) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void marketRule(int marketRuleId, PriceIncrement[] priceIncrements) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pnl(int reqId, double dailyPnL, double unrealizedPnL) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pnlSingle(int reqId, int pos, double dailyPnL, double unrealizedPnL, double value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void historicalTicks(int reqId, List<HistoricalTick> ticks, boolean done) 
	{
		// TODO Auto-generated method stub
		for (HistoricalTick tick : ticks) 
		{
            System.out.println(EWrapperMsgGenerator.historicalTick(reqId, tick.time(), tick.price(), tick.size()));
        }
		
	}

	@Override
	public void historicalTicksBidAsk(int reqId, List<HistoricalTickBidAsk> ticks, boolean done) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void historicalTicksLast(int reqId, List<HistoricalTickLast> ticks, boolean done) {
		
		// TODO Auto-generated method stub
		try
		{
			int j=1;
			tradeData = new String[ticks.size()][3];
			DecimalFormat f = new DecimalFormat("##.00");
			   	for (HistoricalTickLast tick : ticks) 
				{	
			   		tradeData[j][0]=String.valueOf(unixToRegularTime(tick.time()));
			   		tradeData[j][1]= String.valueOf(tick.price());
			   		tradeData[j][2]=	String.valueOf(tick.size());	
			   		if (secTickTime.contains("09:18:"))
			   		{
		    	    			isFirstData=true;
		    	    			dbobj.executeNonQuery("update TBL_PLDATATABLE set first="+Double.valueOf(f.format(Double.valueOf(tradeData[j][1])))+" where symbol='"+currentsymbol+"'");
		    	    		
			   		}
			   		
		            //System.out.println(ticks.size()+EWrapperMsgGenerator.historicalTickLast(reqId, tick.time(), tick.mask(), tick.price(), tick.size(), tick.exchange(), 
		                //tick.specialConditions()));
			   		//System.out.println(unixToRegularTime(tick.time())+","+tick.price()+","+tick.size());
    	    		  		secTickTime =  unixToRegularTime(tick.time()).toString().split(" ")[1].trim();
    	    		  		if (secTickTime.contains("15:20:"))
			    	    {
			    	    		//isDataGot=true;
			    	    		//System.out.println("Reached Market End Time isDataGot true");
                         dbobj.executeNonQuery("update TBL_PLDATATABLE set last="+Double.valueOf(f.format(Double.valueOf(tradeData[j][1])))+" where symbol='"+currentsymbol+"'");	
			    	    		//break;
			    	    }
    	    		  		if (secTickTime.contains("15:29:"))
    			    	    {
    			    	    		isDataGot=true;
    			    	    		System.out.println("Reached Market End Time isDataGot true");
                        //dbobj.executeNonQuery("update TBL_PLDATATABLE set last="+Double.valueOf(f.format(Double.valueOf(tradeData[j][1])))+" where symbol='"+currentsymbol+"'");	
    			    	    		break;
    			    	    }
    	    		  		
		            j++;
		        }			
		}
		catch(Exception ex)
		{			
			//Logger.error(ex);
		}		
	}
	 public static String unixToRegularTime(long time)
	    {
	     	Date date = new Date(time*1000L); // *1000 is to convert seconds to milliseconds
			//SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss z"); // the format of your date
	     	fmt = new SimpleDateFormat("yyyyMMdd HH:mm:ss z");
			String formattedDate = fmt.format(date);
			return formattedDate;
	    }

}
