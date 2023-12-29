package com.cricket.broadcaster;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import com.cricket.model.BattingCard;
import com.cricket.model.BowlingCard;
import com.cricket.model.Configuration;
import com.cricket.model.Player;
import com.cricket.model.Review;
import com.cricket.model.Speed;
import com.cricket.service.CricketService;
import com.cricket.model.Inning;
import com.cricket.model.MatchAllData;
import com.cricket.containers.Infobar;
import com.cricket.containers.Scene;
import com.cricket.util.CricketFunctions;
import com.cricket.util.CricketUtil;
import net.sf.json.JSONArray;

public class DOAD_FRUIT extends Scene{

	private String status;
	private String slashOrDash = "-";
	public Infobar infobar = new Infobar();
	public String session_selected_broadcaster = "DOAD_FRUIT";
	public String which_graphics_onscreen = "";
	public int Whichside = 1,previous_bowler = 0;
	public int Whichinn = 1;
	
	public DOAD_FRUIT() {
		super();
	}

	public DOAD_FRUIT(String scene_path, String which_Layer) {
		super(scene_path, which_Layer);
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public Infobar updateInfobar(Scene scene, MatchAllData match,PrintWriter print_writer) throws Exception
	{
		if(infobar.isInfobar_on_screen() == true) {
			    populateInfobarTeamScore(print_writer, match, session_selected_broadcaster);
			    populateInfobarTopLeft(print_writer, match, session_selected_broadcaster);
			    populateInfobarTopRight(print_writer, match, session_selected_broadcaster);
				populateComp(print_writer, match, session_selected_broadcaster);
				populateProjectedAndPhaseBy( print_writer, match, session_selected_broadcaster);
				populateThisOver(print_writer, match, session_selected_broadcaster);
				populateFOW(print_writer, match, session_selected_broadcaster);
				populateBattingCard(true, print_writer, match, session_selected_broadcaster);
				populateBowlingCard(true, print_writer, match, session_selected_broadcaster);		
		}
		return infobar;
	}
	
	public Object ProcessGraphicOption(String whatToProcess, MatchAllData match, CricketService cricketService,
			PrintWriter print_writer, List<Scene> scenes, String valueToProcess) throws Exception{
		switch (whatToProcess.toUpperCase()) {
		case "PROMPT_GRAPHICS-OPTIONS":
			return JSONArray.fromObject(cricketService.getInfobarStats()).toString();
		
		case "POPULATE-FRUIT": 
			switch (session_selected_broadcaster.toUpperCase()) {
			case "DOAD_FRUIT":
				switch(whatToProcess.toUpperCase()) {
				case "POPULATE-FRUIT":
					//scenes.get(0).setScene_path(valueToProcess.split(",")[0]);
					//scenes.get(0).scene_load(print_writer,session_selected_broadcaster);
					break;
				default:
					scenes.get(1).setScene_path(valueToProcess.split(",")[0]);
					scenes.get(1).scene_load(print_writer,session_selected_broadcaster);
					break;
				}
				
				switch (whatToProcess.toUpperCase()) {
				
				case "POPULATE-FRUIT":
					populateInfobar(infobar, print_writer, valueToProcess.split(",")[0],match, session_selected_broadcaster);
					break;
				}
			}
		case "ANIMATE-IN-FRUIT": case "ANIMATE-OUT": case "CLEAR-ALL": 
			switch (session_selected_broadcaster.toUpperCase()) {
			case "DOAD_FRUIT":
				switch (whatToProcess.toUpperCase()) {
				
				case "ANIMATE-IN-FRUIT":
					processAnimation(print_writer, "In", "START", session_selected_broadcaster,1);
					infobar.setInfobar_on_screen(true);
					which_graphics_onscreen = "INFOBAR";
					break;
				
				case "CLEAR-ALL":
					print_writer.println("LAYER1*EVEREST*SINGLE_SCENE CLEAR;");
					print_writer.println("LAYER2*EVEREST*SINGLE_SCENE CLEAR;");
					which_graphics_onscreen = "";
					break;
				case "ANIMATE-OUT":
					switch(which_graphics_onscreen) {
					case "INFOBAR":
						processAnimation(print_writer, "Out", "START", session_selected_broadcaster,1);
						which_graphics_onscreen = "";
						infobar.setInfobar_on_screen(false);
						break;
					}
					break;
				}
			}
		}
		return null;
}
		
	public Infobar populateInfobar(Infobar infobar,PrintWriter print_writer,String viz_scene, MatchAllData match, String session_selected_broadcaster) throws Exception 
	{
		switch (session_selected_broadcaster.toUpperCase()) {
		case "DOAD_FRUIT":
			if (match == null) {
				this.status = "ERROR: Match is null";
			} else if (match.getMatch().getInning() == null) {
				this.status = "ERROR: Infobar's inning is null";
			} else {
				   populateInfobarTeamScore(print_writer, match, session_selected_broadcaster);
				   populateInfobarTopLeft(print_writer, match, session_selected_broadcaster);
				   populateInfobarTopRight(print_writer, match, session_selected_broadcaster);
				   populateComp( print_writer, match, session_selected_broadcaster);
				   populateProjectedAndPhaseBy( print_writer, match, session_selected_broadcaster);
				   populateThisOver(print_writer, match, session_selected_broadcaster);
				   populateFOW(print_writer, match, session_selected_broadcaster);
				   populateBattingCard(false, print_writer, match, session_selected_broadcaster);
				   populateBowlingCard(false, print_writer, match, session_selected_broadcaster);

				this.status = CricketUtil.SUCCESSFUL;	
			}
			break;
		}
		return infobar;
		
	}	
	public void populateInfobarTeamScore(PrintWriter print_writer, MatchAllData match, String session_selected_broadcaster) throws Exception
	{
		
		switch (session_selected_broadcaster.toUpperCase()) {
		case "DOAD_FRUIT":
			for(Inning inn : match.getMatch().getInning()) {
				if (inn.getIsCurrentInning().toUpperCase().equalsIgnoreCase(CricketUtil.YES)) {
					if(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.TEST)) {
						if(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.TEST)) {
			    			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectPowerplay " + "0" + ";");
			    		}
						if(match.getSetup().getFollowOn().equalsIgnoreCase(CricketUtil.YES)) {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectPowerplay " + "1" + ";");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPowerPlay " + "f/o" + ";");
						}else {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectPowerplay " + "0" + ";");
						}
				    }else if(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.ODI) || match.getSetup().getMatchType().equalsIgnoreCase("OD")) {
				    	if(CricketFunctions.processPowerPlay(CricketUtil.MINI,match).isEmpty()) {
				    		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectPowerplay " + "0" + ";");
				    	}else {
				    		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectPowerplay " + "1" + ";");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPowerPlay " + 
									CricketFunctions.processPowerPlay(CricketUtil.MINI,match) + ";");
				    	}
		    			
		    		}else if(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.DT20) || match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.IT20)
		    				||match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.D10)) {
		    			if(inn.getFirstPowerplayEndOver()>inn.getTotalOvers()) {
		    				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectPowerplay " + "1" + ";");
		    				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPowerPlay " + "P" + ";");
		    				
		    			}else {
		    				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectPowerplay " + "0" + ";");
		    			}
		    			
		    		}else {
		    			if(!match.getSetup().getTargetOvers().isEmpty() && Double.valueOf(match.getSetup().getTargetOvers()) == 1) {
					    	print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectPowerplay " + "0" + ";");
					    }
		    		}
					
					
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBattingTeamName " + inn.getBatting_team().getTeamName4().toUpperCase() + ";");
					if(inn.getTotalWickets() >= 10) {
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTotalScore " + inn.getTotalRuns() + ";");
					}
					else{
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTotalScore " + inn.getTotalRuns() + "-" + inn.getTotalWickets() + ";");
					}
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tOvers " + CricketFunctions.OverBalls(inn.getTotalOvers(), inn.getTotalBalls())+ ";");
					
					
					if(inn.getTotalPenalties() > 0) {
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectPenalty " + "1" + ";");
					}else {
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectPenalty " + "0" + ";");
					}
					
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tExtras " + "EXTRAS: " + inn.getTotalExtras() + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tWideValue " + inn.getTotalWides() + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tNoBallValue " + inn.getTotalNoBalls() + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tByeValue " + inn.getTotalByes() + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tLegByelValue " + inn.getTotalLegByes() + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPenaltyValue " + inn.getTotalPenalties() + ";");
					
					
					
					if(inn.getInningNumber() == 1) {
						if(match.getSetup().getTossWinningTeam() == match.getSetup().getHomeTeamId()) {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tText " 
									+ match.getSetup().getHomeTeam().getTeamName1().toUpperCase() + 
									" ELECTED TO " + match.getSetup().getTossWinningDecision().toUpperCase() + ";");
						}else {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tText " 
									+ match.getSetup().getAwayTeam().getTeamName1().toUpperCase() + 
									" ELECTED TO " + match.getSetup().getTossWinningDecision().toUpperCase() + ";");
						}
					}else if(inn.getInningNumber() == 2) {
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tText " 
								+ CricketFunctions.generateMatchSummaryStatus(inn.getInningNumber(), match, CricketUtil.SHORT).toUpperCase() + ";");
					}
				}
			}
			break;
		}
	}
	public void populateComp(PrintWriter print_writer, MatchAllData match, String session_selected_broadcaster)
	{	
		 List<Integer> inningNumbers = Arrays.asList(1, 2);
		 List<String> arr = CricketFunctions.getScoreTypeData(CricketUtil.TEAM, match, inningNumbers, 0, ",");
		 List<List<String>> splitList = arr.stream().map(s -> Arrays.asList(s.split(","))).collect(Collectors.toList());
		switch (session_selected_broadcaster.toUpperCase()) {
		case "DOAD_FRUIT":
			
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomeStatValue1 " + match.getMatch().getInning().get(0).getTotalFours() + ";");
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomeStatValue2 " + match.getMatch().getInning().get(0).getTotalSixes() + ";");
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomeStatValue4 " + splitList.get(0).get(0) + ";");
			
			for(Inning inn : match.getMatch().getInning()) {
				if(inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)) {
					String lastFewOverData=CricketFunctions.lastFewOversData(CricketUtil.BOUNDARY, match.getEventFile().getEvents(),inn.getInningNumber());
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSmallFreeText1 " + "BALL" + 
							CricketFunctions.Plural(Integer.valueOf(lastFewOverData)).toUpperCase() 
							+ " SINCE LAST BOUNDARY : " + 
							lastFewOverData + ";");
					//System.out.println(lastFewOverData);
					
					if(inn.getInningNumber() == 1) {
						
						if(match.getMatch().getInning().get(0).getTotalWickets()==10) {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tComparisonHomeTeamName " + match.getMatch().getInning().get(0).getBatting_team().getTeamName2()
									+"    (" +match.getMatch().getInning().get(0).getTotalRuns() +")" + ";");
						}else {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tComparisonHomeTeamName " + match.getMatch().getInning().get(0).getBatting_team().getTeamName2()
									+"    (" +match.getMatch().getInning().get(0).getTotalRuns() +" -"+match.getMatch().getInning().get(0).getTotalWickets()+")" + ";");
						}
						
						if(match.getMatch().getInning().get(1).getTotalWickets()==10) {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tComparisonAwayTeamName " + match.getMatch().getInning().get(1).getBatting_team().getTeamName2() +
									"     (" +match.getMatch().getInning().get(1).getTotalRuns()+ ")" + ";");

						}else {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tComparisonAwayTeamName " + match.getMatch().getInning().get(1).getBatting_team().getTeamName2() +
									"     (" +match.getMatch().getInning().get(1).getTotalRuns()+" -"+match.getMatch().getInning().get(1).getTotalWickets()+ ")" + ";");
						}
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tAwayStatValue1 " + "0" + ";");
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tAwayStatValue2 " + "0" + ";");
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomeStatValue3 " + "-" + ";");
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tAwayStatValue3 " + "-" + ";");
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET AwayStatValue4 " + "0" + ";");
					}
					
					if(inn.getInningNumber() == 2) {
						
						if(match.getMatch().getInning().get(0).getTotalWickets()==10) {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tComparisonHomeTeamName " + match.getMatch().getInning().get(0).getBatting_team().getTeamName2()
									+"    (" +match.getMatch().getInning().get(0).getTotalRuns() +")" + ";");
						}else {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tComparisonHomeTeamName " + match.getMatch().getInning().get(0).getBatting_team().getTeamName2()
									+"    (" +match.getMatch().getInning().get(0).getTotalRuns() +" -"+match.getMatch().getInning().get(0).getTotalWickets()+")" + ";");
						}
						
						if(match.getMatch().getInning().get(1).getTotalWickets()==10) {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tComparisonAwayTeamName " + match.getMatch().getInning().get(1).getBatting_team().getTeamName2() +
									"     (" +match.getMatch().getInning().get(1).getTotalRuns()+ ")" + ";");

						}else {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tComparisonAwayTeamName " + match.getMatch().getInning().get(1).getBatting_team().getTeamName2() +
									"     (" +match.getMatch().getInning().get(1).getTotalRuns()+" -"+match.getMatch().getInning().get(1).getTotalWickets()+ ")" + ";");
						}
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tAwayStatValue1 " + inn.getTotalFours() + ";");
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tAwayStatValue2 " + inn.getTotalSixes() + ";");
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomeStatValue3 " + CricketFunctions.compareInningData(match, "-", 1, match.getEventFile().getEvents()) + ";");
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tAwayStatValue3 " + CricketFunctions.compareInningData(match, "-", 2, match.getEventFile().getEvents()) + ";");
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET AwayStatValue4 " +splitList.get(1).get(0) + ";");
						
					}
				}
				
			}
			break;
		}
	}
	public void populateProjectedAndPhaseBy(PrintWriter print_writer, MatchAllData match, String session_selected_broadcaster)
	{
		switch (session_selected_broadcaster.toUpperCase()) {
		case "DOAD_FRUIT":
				
				List<Integer> inningNumbers = Arrays.asList(1, 2);
				List<String> arr1 =CricketFunctions.getFirstPowerPlayScores(match, inningNumbers, match.getEventFile().getEvents());
				List<String> arr2 =CricketFunctions.getSecPowerPlayScores(match, inningNumbers, match.getEventFile().getEvents());
				List<String> arr3 =CricketFunctions.getThirdPowerPlayScore(match, inningNumbers, match.getEventFile().getEvents());		
			for(Inning inn : match.getMatch().getInning()) {
				if(inn.getInningNumber() == 1 && inn.getIsCurrentInning().equalsIgnoreCase("YES")) {
					if(inn.getRunRate() != null) {
						String[] proj_score_rate = new String[CricketFunctions.projectedScore(match).size()];
					    for (int i = 0; i < CricketFunctions.projectedScore(match).size(); i++) {
					    	proj_score_rate[i] = CricketFunctions.projectedScore(match).get(i);
				        }
					    
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectDataType " + "0" + ";");
						
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tProjectedHead1 " + 
								"@" + proj_score_rate[0] +" (CRR)" + ";");
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tProjectedValue1 " + 
								proj_score_rate[1] + ";");
						
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tProjectedHead2 " + 
								"@" + proj_score_rate[2] +" RPO" + ";");
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tProjectedValue2 " + 
								proj_score_rate[3] + ";");
						
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tProjectedHead3 " + 
								"@" + proj_score_rate[4] +" RPO" + ";");
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tProjectedValue3 " + 
								proj_score_rate[5] + ";");
					}else {
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectDataType " + "0" + ";");
						
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tProjectedHead1 " + 
								"-" + ";");
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tProjectedValue1 " + 
								"-" + ";");
						
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tProjectedHead2 " + 
								"-" + ";");
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tProjectedValue2 " + 
								"-" + ";");
						
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tProjectedHead3 " + 
								"-" + ";");
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tProjectedValue3 " + 
								"-" + ";");
					}
					
				}else if(inn.getInningNumber() == 2 & inn.getIsCurrentInning().equalsIgnoreCase("YES")) {
					 
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectDataType " + "1" + ";");
					
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeamName1 " + 
							match.getMatch().getInning().get(0).getBatting_team().getTeamName4() + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeamName2 " + 
							match.getMatch().getInning().get(1).getBatting_team().getTeamName4() + ";");
					
					
					
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeam1PhaseScore1 " + 
							arr1.get(0) + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeam1PhaseScore2 " + 
							arr2.get(0)+ ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeam1PhaseScore3 " + 
							arr3.get(0) + ";");
					
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeam2PhaseScore1 " + 
							arr1.get(1) + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeam2PhaseScore2 " + 
							arr2.get(1) + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeam2PhaseScore3 " + 
							arr3.get(1) + ";");
					
				}
			}
			break;
		}
	}
	public void populateThisOver(PrintWriter print_writer, MatchAllData match, String session_selected_broadcaster)
	{
		switch (session_selected_broadcaster.toUpperCase()) {
		case "DOAD_FRUIT":
			List<String> Ovr = new ArrayList<String>();
			for(Inning inn : match.getMatch().getInning()) {
				if (inn.getIsCurrentInning().toUpperCase().equalsIgnoreCase(CricketUtil.YES)) {
					if(inn.getBowlingCard() != null) {
						for(BowlingCard boc : inn.getBowlingCard()) {
							if(boc.getStatus().toUpperCase().equalsIgnoreCase("CURRENTBOWLER") || boc.getStatus().toUpperCase().equalsIgnoreCase("LASTBOWLER")) {
								String arr[] = CricketFunctions.getEventsText(CricketUtil.OVER,boc.getPlayerId(),",", match.getEventFile().getEvents(),0).split(",");
								for(int i=0;i <arr.length;i++) {
										Ovr.add(arr[i]);
								}
								String cumm_runs = String.join(" | ", Ovr);
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tThisOverData " + cumm_runs + ";");
							
								if (((inn.getTotalOvers() * 6) + inn.getTotalBalls()) >= 30)
								{
									String[] data=CricketFunctions.getlastthirtyballsdata(match, "-", match.getEventFile().getEvents(), 30).split("-");
									print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSmallFreeText2 " + "LAST 30 BALLS : " + 
											data[0] + "RUN"+CricketFunctions.Plural(Integer.valueOf(data[0])).toUpperCase()+" , " +data[1] +" WICKET"+CricketFunctions.Plural(Integer.valueOf(data[1])).toUpperCase()+ ";");
								}
								else  
								{
									if(inn.getTotalOvers() > 0 || inn.getTotalBalls() > 0) {
										String Run_wicket[]=CricketFunctions.PreOtherRunWicket(previous_bowler,inn.getInningNumber(),",",match,match.getEventFile().getEvents()).split(",");
										int wicket = 0,run =0;
										wicket = Integer.valueOf(Run_wicket[1]);
										run = Integer.valueOf(Run_wicket[0]);
										System.out.println("Wicket = " + wicket + " Run = " + run);
										print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSmallFreeText2 " + "LAST OVER : " + 
												run+ " RUN" +CricketFunctions.Plural(run).toUpperCase() +" AND " + wicket + " WICKET" +CricketFunctions.Plural(wicket).toUpperCase() + ";");
									}else {
										print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSmallFreeText2 " + "LAST OVER : " + ";");
									}
								}
							}
						}
					}
				}
			}
			break;
		}
	}
	public void populateFOW( PrintWriter print_writer, MatchAllData match, String session_selected_broadcaster)
	{
		switch (session_selected_broadcaster.toUpperCase()) {
		case "DOAD_FRUIT":
			for(Inning inn : match.getMatch().getInning()) {
				if (inn.getIsCurrentInning().toUpperCase().equalsIgnoreCase(CricketUtil.YES)) {
					if(inn.getInningNumber() == 1) {
						if(inn.getFallsOfWickets() != null)
						{
						    for (int i = 0; i <= inn.getFallsOfWickets().size() -1; i++)
						    {
						    	print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeam1Fow" + (i+1) + " " + 
						    			inn.getFallsOfWickets().get(i).getFowRuns() + ";");
						    }
						}
					}else if(inn.getInningNumber() == 2) {
						if(match.getMatch().getInning().get(0).getFallsOfWickets() != null)
						{
						    for (int i = 0; i <= match.getMatch().getInning().get(0).getFallsOfWickets().size() -1; i++)
						    {
						    	print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeam1Fow" + (i+1) + " " + 
						    			match.getMatch().getInning().get(0).getFallsOfWickets().get(i).getFowRuns() + ";");
						    }
						}
						
						if(match.getMatch().getInning().get(1).getFallsOfWickets() != null)
						{
						    for (int j = 0; j <= match.getMatch().getInning().get(1).getFallsOfWickets().size() -1; j++)
						    {
						    	print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeam2Fow" + (j+1) + " " + 
						    			match.getMatch().getInning().get(1).getFallsOfWickets().get(j).getFowRuns() + ";");
						    }
						}
					}
				}
			}
			
			break;
		}
	}

	public void populateBattingCard(boolean is_this_updating, PrintWriter print_writer, MatchAllData match, String session_selected_broadcaster) throws Exception
	{
		switch (session_selected_broadcaster.toUpperCase()) {
		case "DOAD_FRUIT":
			boolean player_found = false;
			if(is_this_updating == false) {
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectHomePlayerNumber " + 
						"0" + ";");
			}
			
			for(Inning inn : match.getMatch().getInning()) {
				if (inn.getIsCurrentInning().toUpperCase().equalsIgnoreCase(CricketUtil.YES)) {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomeTeamName " + 
							inn.getBatting_team().getTeamName2() + ";");
					
					if(inn.getBattingTeamId() == match.getSetup().getHomeTeamId()) {
						if(inn.getFallsOfWickets() != null){
							if(inn.getFallsOfWickets().size() > 0){
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectHomePlayerNumber " + 
										inn.getFallsOfWickets().size() + ";");
								for(int i = 0; i <= inn.getFallsOfWickets().size() -1 ; i++) {
									player_found = false;
									if(inn.getFallsOfWickets().get(i) != null){
										if(player_found == false) {
											for(Player hs : match.getSetup().getHomeSquad()) {
												if(inn.getFallsOfWickets().get(i).getFowPlayerID() == hs.getPlayerId()){
													print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomePlayer" + (i+1) + " " + 
											    			hs.getTicker_name() + ";");
													player_found = true;
												}
											}
										}
										
										if(player_found == false) {
											for(Player hos : match.getSetup().getHomeOtherSquad()) {
												if(inn.getFallsOfWickets().get(i).getFowPlayerID() == hos.getPlayerId()){
													print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomePlayer" + (i+1) + " " + 
											    			hos.getTicker_name() + ";");
													player_found = true;
												}
											}
										}
										
										if(player_found == false) {
											for(Player hsub : match.getSetup().getHomeSubstitutes()) {
												if(inn.getFallsOfWickets().get(i).getFowPlayerID() == hsub.getPlayerId()){
													print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomePlayer" + (i+1) + " " + 
															hsub.getTicker_name() + ";");
													player_found = true;
												}
											}
										}
										
										for(BattingCard bc : inn.getBattingCard()) {
											if(inn.getFallsOfWickets().get(i).getFowPlayerID() == bc.getPlayerId()){
												print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectHomePlayer" + (i+1) + " 1" + ";");
												print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vHomeSelectStriker" + (i+1) + " 0" + ";");
												print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeamsHeade " + 
														bc.getHowOutText() + ";");
												print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomeScore" + (i+1) + " " + 
														bc.getRuns() + ";");
												print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomeBalls" + (i+1) + " " + 
														bc.getBalls() + ";");
											}
										}
									}
								}
							}
						}
					}else if(inn.getBattingTeamId() == match.getSetup().getAwayTeamId()) {
						if(inn.getFallsOfWickets() != null) {
							if(inn.getFallsOfWickets().size() > 0){
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectHomePlayerNumber " + 
										inn.getFallsOfWickets().size() + ";");
								for(int i = 0; i <= inn.getFallsOfWickets().size() -1 ; i++) {
									player_found = false;
									if(inn.getFallsOfWickets().get(i) != null){
										if(player_found == false) {
											for(Player as : match.getSetup().getAwaySquad()) {
												if(inn.getFallsOfWickets().get(i).getFowPlayerID() == as.getPlayerId()){
													print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomePlayer" + (i+1) + " " + 
											    			as.getTicker_name() + ";");
												}
											}
										}
										
										if(player_found == false) {
											for(Player aos : match.getSetup().getAwayOtherSquad()) {
												if(inn.getFallsOfWickets().get(i).getFowPlayerID() == aos.getPlayerId()){
													print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomePlayer" + (i+1) + " " + 
											    			aos.getTicker_name() + ";");
												}
											}
										}
										
										if(player_found == false) {
											for(Player asub : match.getSetup().getAwaySubstitutes()) {
												if(inn.getFallsOfWickets().get(i).getFowPlayerID() == asub.getPlayerId()){
													print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomePlayer" + (i+1) + " " + 
															asub.getTicker_name() + ";");
												}
											}
										}
										
										for(BattingCard bc : inn.getBattingCard()) {
											if(inn.getFallsOfWickets().get(i).getFowPlayerID() == bc.getPlayerId()){
												print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectHomePlayer" + (i+1) + " 1" + ";");
												print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vHomeSelectStriker" + (i+1) + " 0" + ";");
												print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeamsHeade " + 
														bc.getHowOutText() + ";");
												print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomeScore" + (i+1) + " " + 
														bc.getRuns() + ";");
												print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomeBalls" + (i+1) + " " + 
														bc.getBalls() + ";");
											}
										}
										
									}
								}
							}
						}
					}
				}
			}
			break;
		}
	}
	public void populateBowlingCard(boolean is_this_updating, PrintWriter print_writer, MatchAllData match, String session_selected_broadcaster) throws IOException
	{
		switch (session_selected_broadcaster.toUpperCase()) {
		case "DOAD_FRUIT":
			boolean player_found = false;
			if(is_this_updating == false) {
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectAwayPlayerNumber " + 
						"0" + ";");
			}
			
			for(Inning inn : match.getMatch().getInning()) {
				if (inn.getIsCurrentInning().toUpperCase().equalsIgnoreCase(CricketUtil.YES)) {
					if(inn.getBowlingCard() != null) {
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tAwayTeamName " + 
								inn.getBowling_team().getTeamName2() + ";");
						
						if(inn.getBowlingTeamId() == match.getSetup().getHomeTeamId()) {
							
							if(inn.getBowlingCard() != null){
								for(int i = 0; i <= inn.getBowlingCard().size() -1 ; i++) {
									player_found = false;
									if(player_found == false) {
										for(Player hs : match.getSetup().getHomeSquad()) {
											if(inn.getBowlingCard().get(i).getPlayerId() == hs.getPlayerId()) {
												print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tAwayPlayer" + (i+1) + " " + 
										    			hs.getTicker_name() + ";");
												player_found = true;
											}
										}
									}
									
									if(player_found == false) {
										for(Player hos : match.getSetup().getHomeOtherSquad()) {
											if(inn.getBowlingCard().get(i).getPlayerId() == hos.getPlayerId()){
												print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tAwayPlayer" + (i+1) + " " + 
										    			hos.getTicker_name() + ";");
												player_found = true;
											}
										}
									}
									
									if(player_found == false) {
										for(Player hsub : match.getSetup().getHomeSubstitutes()) {
											if(inn.getBowlingCard().get(i).getPlayerId() == hsub.getPlayerId()){
												print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tAwayPlayer" + (i+1) + " " + 
														hsub.getTicker_name() + ";");
												player_found = true;
											}
										}
									}
									
									for(BowlingCard boc : inn.getBowlingCard()) {
										if(inn.getBowlingCard().get(i).getPlayerId() == boc.getPlayerId()){
											print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectAwayPlayer" + (i+1) + " 1" + ";");
											print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectAwayStriker3 0" + ";");
											print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vAwaySelectStriker" + (i+1) + " 0" + ";");
											print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tAwayScore" + (i+1) + " " + 
													 boc.getWickets() + "-" + boc.getRuns() + ";");
											print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tAwayBalls" + (i+1) + " " + 
													CricketFunctions.OverBalls(boc.getOvers(), boc.getBalls()) + ";");
										}
									}
								}
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectAwayPlayerNumber " + 
										inn.getBowlingCard().size() + ";");
							}
						}else if(inn.getBowlingTeamId() == match.getSetup().getAwayTeamId()) {
							
							if(inn.getBowlingCard() != null){
								for(int i = 0; i <= inn.getBowlingCard().size() -1 ; i++) {
									player_found = false;
									if(player_found == false) {
										for(Player hs : match.getSetup().getAwaySquad()) {
											if(inn.getBowlingCard().get(i).getPlayerId() == hs.getPlayerId()) {
												print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tAwayPlayer" + (i+1) + " " + 
										    			hs.getTicker_name() + ";");
												player_found = true;
											}
										}
									}
									
									if(player_found == false) {
										for(Player hos : match.getSetup().getAwayOtherSquad()) {
											if(inn.getBowlingCard().get(i).getPlayerId() == hos.getPlayerId()){
												print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tAwayPlayer" + (i+1) + " " + 
										    			hos.getTicker_name() + ";");
												player_found = true;
											}
										}
									}
									
									if(player_found == false) {
										for(Player hsub : match.getSetup().getAwaySubstitutes()) {
											if(inn.getBowlingCard().get(i).getPlayerId() == hsub.getPlayerId()){
												print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tAwayPlayer" + (i+1) + " " + 
														hsub.getTicker_name() + ";");
												player_found = true;
											}
										}
									}
									
									for(BowlingCard boc : inn.getBowlingCard()) {
										if(inn.getBowlingCard().get(i).getPlayerId() == boc.getPlayerId()){
											print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectAwayPlayer" + (i+1) + " 1" + ";");
											print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectAwayStriker3 0" + ";");
											print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vAwaySelectStriker" + (i+1) + " 0" + ";");
											print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tAwayScore" + (i+1) + " " + 
													 boc.getWickets() + "-" + boc.getRuns() + ";");
											print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tAwayBalls" + (i+1) + " " + 
													CricketFunctions.OverBalls(boc.getOvers(), boc.getBalls()) + ";");
										}
									}
								}
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectAwayPlayerNumber " + 
										inn.getBowlingCard().size() + ";");
							}
						}
					}
				}
			}
			break;
		}
	}
	public void populateInfobarTopLeft(PrintWriter print_writer,MatchAllData match, String broadcaster)
	{ 
		populateCurrentBatsmen(print_writer, match, broadcaster);
		
		for(Inning inn : match.getMatch().getInning()) {
			if (inn.getIsCurrentInning().toUpperCase().equalsIgnoreCase(CricketUtil.YES)) {
					if(inn.getInningNumber() == 1) {
						if(inn.getRunRate() != null) {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tRunRate " + 
									"RUN RATE : " + inn.getRunRate() + ";");
						}else {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tRunRate " + 
									"RUN RATE : " + "-" + ";");
						}
						
					}else if(inn.getInningNumber() == 2) {
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tRunRate " + 
								"RRR: " + CricketFunctions.generateRunRate(CricketFunctions.getRequiredRuns(match), 0, CricketFunctions.getRequiredBalls(match), 2,match) + ";");
					}
					if(inn.getPartnerships() != null) {
						if(inn.getPartnerships().size() > 0) {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPartnershipRus " + 
									inn.getPartnerships().get(inn.getPartnerships().size() - 1).getTotalRuns() + ";");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPartnershipBalls " + 
									inn.getPartnerships().get(inn.getPartnerships().size() - 1).getTotalBalls() + ";");
						}
					}
				for (BattingCard bc : inn.getBattingCard()) {
					if(inn.getFallsOfWickets() != null && !inn.getFallsOfWickets().isEmpty()) {
						if(inn.getFallsOfWickets().size() > 0){
							if(inn.getFallsOfWickets().get(inn.getFallsOfWickets().size() - 1).getFowPlayerID() == bc.getPlayerId()) {
								String how_out=bc.getPlayer().getTicker_name()+"  "+bc.getHowOutText();
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tLastWicketValue " 
										+"  "+ bc.getPlayer().getTicker_name() + "  " +bc.getHowOutText()+ "    ;");
								if(how_out.length()>31) {
									print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tLastWicketValue " 
											+ bc.getPlayer().getTicker_name() +  "    ;");
								}else {
									print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tLastWicketValue " 
											+ bc.getPlayer().getTicker_name() + "  " +bc.getHowOutText()+ "    ;");
								}
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tLastWicketScore " 
											+" "+ bc.getRuns() + ";");
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tLastWicketBalls " 
											+ bc.getBalls() + ";");
							}
						}
					}else {
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tLastWicketValue " + "" + ";");
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tLastWicketScore " + "" + ";");
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tLastWicketBalls " + "" + ";");
					}			
		  	}
		 }
	  }
	}
	private void populateCurrentBatsmen(PrintWriter print_writer, MatchAllData match,String broadcaster) {
		
		BattingCard this_batsman1,this_batsman2 = new BattingCard();
		
		for(Inning inn : match.getMatch().getInning()) {
			if (inn.getIsCurrentInning().toUpperCase().equalsIgnoreCase(CricketUtil.YES)) {
				if(inn.getPartnerships() != null && inn.getPartnerships().size() > 0) {
					
					
					this_batsman1 = inn.getBattingCard().stream().filter(batcard -> batcard.getPlayerId() 
							== inn.getPartnerships().get(inn.getPartnerships().size() - 1).getFirstBatterNo()).findAny().orElse(null);
					
					this_batsman2 = inn.getBattingCard().stream().filter(batcard -> batcard.getPlayerId() 
							== inn.getPartnerships().get(inn.getPartnerships().size() - 1).getSecondBatterNo()).findAny().orElse(null);
					
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBatterName1 " + this_batsman1.getPlayer().getTicker_name() + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBatterRun1 " + this_batsman1.getRuns() + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBatterBall1 " + this_batsman1.getBalls() + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBoundaries1 " + this_batsman1.getFours() + "/" + 
							this_batsman1.getSixes() + ";");
					
					if(this_batsman1.getStrikeRate() != null) {
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStrikeRate1 " + this_batsman1.getStrikeRate() + ";");
					}else {
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStrikeRate1 " + "-" + ";");
					}
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tMinutes1 " + this_batsman1.getSeconds()/60 + ";");
					
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPartnershipContribution1 " + 
							inn.getPartnerships().get(inn.getPartnerships().size() - 1).getFirstBatterRuns() + "(" + 
							inn.getPartnerships().get(inn.getPartnerships().size() - 1).getFirstBatterBalls() + ")" + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPartnershipContribution2 " + 
							inn.getPartnerships().get(inn.getPartnerships().size() - 1).getSecondBatterRuns() + "(" + 
							inn.getPartnerships().get(inn.getPartnerships().size() - 1).getSecondBatterBalls() + ")" + ";");
					
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBatterName2 " + this_batsman2.getPlayer().getTicker_name() + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBatterRun2 " + this_batsman2.getRuns() + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBatterBall2 " + this_batsman2.getBalls() + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBoundaries2 " + this_batsman2.getFours() + "/" + 
							this_batsman2.getSixes() + ";");
					if(this_batsman2.getStrikeRate() != null) {
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStrikeRate2 " + this_batsman2.getStrikeRate() + ";");
					}else {
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStrikeRate2 " + "-" + ";");
					}
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tMinutes2 " + this_batsman2.getSeconds()/60 + ";");
					
				}
			}
		}
		
	}
	public void populateInfobarTopRight(PrintWriter print_writer,MatchAllData match, String broadcaster) throws InterruptedException
	{
		for(Inning inn : match.getMatch().getInning()) {
			if (inn.getIsCurrentInning().toUpperCase().equalsIgnoreCase(CricketUtil.YES)) {
				if(inn.getBowlingCard() != null) {
					for(BowlingCard boc : inn.getBowlingCard()) {
						if(boc.getStatus().toUpperCase().equalsIgnoreCase("CURRENTBOWLER")) {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectBowler2" + " " + "0" + ";");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectBowler1" + " " + "1" + ";");
							
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBowlerName1 " + boc.getPlayer().getTicker_name() +";"); 
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFigure1 " + boc.getWickets() + "-" + boc.getRuns() +";");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tOvers1 " + CricketFunctions.OverBalls(boc.getOvers(), boc.getBalls()) +  ";");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tDots1 " + boc.getDots() + ";");
							
							if(boc.getEconomyRate() == null) {
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tEconomy1 " + "-" + ";");
							}else {
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tEconomy1 " + boc.getEconomyRate() + ";");
							}
							
						}
						else if(boc.getStatus().toUpperCase().equalsIgnoreCase("LASTBOWLER")) {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectBowler1" + " " + "0" +";");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectBowler2" + " " + "0" + ";");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBowlerName1 " + boc.getPlayer().getTicker_name()+ ";");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFigure1 " + boc.getWickets() + "-" + boc.getRuns() + ";");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tOvers1 " + CricketFunctions.OverBalls(boc.getOvers(), boc.getBalls()) +";"); 
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tDots1 " + boc.getDots() + ";");
							
							if(boc.getEconomyRate() == null) {
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tEconomy1 " + "-" + ";");
							}else {
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tEconomy1 " + boc.getEconomyRate() + ";");
							}
							
						}
							String prev_bowler=CricketFunctions.previousBowler(match, match.getEventFile().getEvents());
							if(prev_bowler != "") {
								previous_bowler = Integer.valueOf(prev_bowler.split(",")[5]);
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBowlerName2 " + prev_bowler.split(",")[0] +";");
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFigure2 " + prev_bowler.split(",")[1] + ";");
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tOvers2 " + prev_bowler.split(",")[4] + ";");
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tDots2 " + prev_bowler.split(",")[2] + ";");
								
								if(prev_bowler.split(",")[3] == "") {
									print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tEconomy2 " + "-" + ";");
								}else {
									print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tEconomy2 " + prev_bowler.split(",")[3] + ";");
								}
								
							}
							
					}
				}
			}
		}
	}

	public void processAnimation(PrintWriter print_writer, String animationName,String animationCommand, String which_broadcaster,int which_layer)
	{
		switch(which_broadcaster.toUpperCase()) {
		case "DOAD_FRUIT":
			switch(which_layer) {
			case 1:
				print_writer.println("LAYER1*EVEREST*STAGE*DIRECTOR*" + animationName + " " + animationCommand + ";");
				break;
				
			case 2:
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*" + animationName + " " + animationCommand + ";");
				break;
			}
			break;
		}
		
	}
	
	public void populateDuckWorthLewis(PrintWriter print_writer,String viz_scene,String balls,MatchAllData match,String session_selected_broadcaster) throws InterruptedException 
	{
		switch (session_selected_broadcaster.toUpperCase()) {
			case "DOAD_FRUIT":
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFirstName " + match.getMatch().getInning().get(1).getBatting_team().getTeamName1() + " " 
						+ match.getMatch().getInning().get(1).getTotalRuns() + "-" + match.getMatch().getInning().get(1).getTotalWickets() + ";");
				
				for(int i = 0; i<= CricketFunctions.populateDuckWorthLewis(match).size() -1;i++) {
					if(CricketFunctions.populateDuckWorthLewis(match).get(i).getOver_left().equalsIgnoreCase(balls)) {
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tDetails " + "CURRENT DLS PAR SCORE AFTER " 
									+ balls + " OVERS: " + (Integer.valueOf(CricketFunctions.populateDuckWorthLewis(match).get(i).getWkts_down()) + 1) + ";");
					}
				}
				
				print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW ON;");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*CONTAINER SET ACTIVE 0;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In STOP;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out STOP;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 109.0;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out SHOW 0.0;");
				print_writer.println("LAYER1*EVEREST*GLOBAL SNAPSHOT_PATH C:/Temp/Preview.png;");
				print_writer.println("LAYER1*EVEREST*GLOBAL SNAPSHOT 1920 1080;");
				//TimeUnit.SECONDS.sleep(1);
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out SHOW 0.0;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 0.0;");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*CONTAINER SET ACTIVE 1;");
				print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW OFF;");
				
				break;
		}
	}
	public void populateDuckWorthLewisEquation(PrintWriter print_writer,String viz_scene,String balls,MatchAllData match,String session_selected_broadcaster) throws InterruptedException 
	{
		switch (session_selected_broadcaster.toUpperCase()) {
			case "DOAD_FRUIT":
				int runs = 0;
				String team = "",ahead_behind = "";
				
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFirstName " + match.getMatch().getInning().get(1).getBatting_team().getTeamName1() + " " 
						+ match.getMatch().getInning().get(1).getTotalRuns() + "-" + match.getMatch().getInning().get(1).getTotalWickets() + ";");
				
				for(Inning inn : match.getMatch().getInning()) {
					if(inn.getIsCurrentInning().equalsIgnoreCase("YES")) {
						if(inn.getBattingTeamId() == match.getSetup().getHomeTeamId())
		                {
		                    team = match.getSetup().getHomeTeam().getTeamName4();
		                }

		                if (inn.getBattingTeamId() == match.getSetup().getAwayTeamId())
		                {
		                    team = match.getSetup().getAwayTeam().getTeamName4();
		                }
		                
						for(int i = 0; i<= CricketFunctions.populateDuckWorthLewis(match).size() -1;i++) {
							if(CricketFunctions.populateDuckWorthLewis(match).get(i).getOver_left().equalsIgnoreCase(balls)) {
								
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tDetails " + "CURRENT DLS PAR SCORE AFTER " 
											+ balls + " OVERS: " + (Integer.valueOf(CricketFunctions.populateDuckWorthLewis(match).get(i).getWkts_down())) + ";");
								
								runs = (inn.getTotalRuns()) - Integer.valueOf((CricketFunctions.populateDuckWorthLewis(match).get(i).getWkts_down()));
		                        if(runs < 0)
		                        {
		                            ahead_behind = " - " + team + " is " + (Math.abs(runs)) + " runs behind";
		                        }

		                        if (runs > 0)
		                        {
		                            ahead_behind = " - " + team + " is " + runs + " runs ahead";
		                        }
		                        print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tDetail " + ahead_behind + ";");
							}
						}
					}
				}
				
				print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW ON;");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*CONTAINER SET ACTIVE 0;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In STOP;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out STOP;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 109.0;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out SHOW 0.0;");
				print_writer.println("LAYER1*EVEREST*GLOBAL SNAPSHOT_PATH C:/Temp/Preview.png;");
				print_writer.println("LAYER1*EVEREST*GLOBAL SNAPSHOT 1920 1080;");
				//TimeUnit.SECONDS.sleep(1);
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out SHOW 0.0;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 0.0;");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*CONTAINER SET ACTIVE 1;");
				print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW OFF;");
				break;
		}
	}	
	
	public String toString() {
		return "Doad [status=" + status + ", slashOrDash=" + slashOrDash + "]";
	}	 	
	public void initialize_fruit(PrintWriter print_writer, MatchAllData match ,Configuration config) throws IOException {
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSpeedHead " + 
    			"SPEED" + ";");
	
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSpeedUnit " + 
							"("+config.getSpeedUnit()+")" + ";");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTounamentName " + match.getSetup().getTournament().toUpperCase() + ";");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tMatchNumber " + match.getSetup().getMatchIdent() + ";");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tWideHead " + "WD" + ";");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tNoBallHead " + "NB" + ";");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tByeHead " + "B" + ";");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tLegByelHead " + "LB" + ";");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPenaltyHead " + "PEN" + ";");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStatHead1 " + "FOURS" + ";");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStatHead2 " + "SIXES" + ";");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStatHead3 " + "AT THIS STAGE" + ";");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStatHead4 " + "DOTS" + ";");
		//CURR P'SHIP
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPartnershipHead " + 
				"PARTNERSHIP" + ";");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSpeedValue " +"  "
	            + ";");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeamsHeader " + "SUMMARY " + ";");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBowlerName2 " + "-" + ";");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFigure2 " + "-" + ";");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tOvers2 " + "-" + ";");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tDots2 " + "-" + ";");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tEconomy2 " + "-" + ";");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectBowler1" + " " + "0" + ";");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFowTeamName1 " + 
				match.getMatch().getInning().get(0).getBatting_team().getTeamName4() + ";");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFowTeamName2 " + 
				match.getMatch().getInning().get(1).getBatting_team().getTeamName4() + ";");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBowlerName1 " + "-" + ";");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFigure1 " + "-" + ";");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tOvers1 " + "-" + ";");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tDots1 " + "-" + ";");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tEconomy1 " + "-" + ";");
		for (int i = 1; i <= 10; i++)
	    {
	    	print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeam1Fow" + i + " " + 
	    			"" + ";");
	    	print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeam2Fow" + i + " " + 
	    			"" + ";");
	    }
		if(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.ODI)||match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.OD)) {
			
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPhaseHead1 " + 
					"1-10" + ";");
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPhaseHead2 " + 
					"11-40" + ";");
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPhaseHead3 " + 
					"41-50" + ";");
			
		}else if(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.DT20)||match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.IT20)) {
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPhaseHead1 " + 
					"1-6" + ";");
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPhaseHead2 " + 
					"7-16" + ";");
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPhaseHead3 " + 
					"17-20" + ";");
			
		}else if(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.D10)) {
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPhaseHead1 " + 
					"1-2" + ";");
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPhaseHead2 " + 
					"3-6" + ";");
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPhaseHead3 " + 
					"7-10" +";");
		}
	}
	
	public void populateSpeed(PrintWriter printWriter, Speed lastSpeed) throws Exception {
		
		if(CricketFunctions.getCurrentSpeed(CricketUtil.CRICKET_DIRECTORY + "Speed/SPEED.txt", lastSpeed) != null) {
			printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSpeedValue " + lastSpeed.getSpeedValue()  + ";");
		}else {
			printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSpeedValue;");
		}			      
	} 	
	public void populateReview(PrintWriter printWriter, MatchAllData match, Review review) throws Exception {
		switch (this.session_selected_broadcaster) {
		case "DOAD_FRUIT":
			Review rev=CricketFunctions.getCurrentReview(CricketUtil.REVIEWS, review);
			if(rev == null) {
	    		printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeamsHeader " + "SUMMARY " + ";");
			}else {
	    		printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeamsHeader " +  "REVIEWS REMAINING :-" + 
	    				String.format("%-16s", match.getMatch().getInning().get(0).getBatting_team().getTeamName4()) + " : " + 
	    				rev.getReviewStatus().split(",")[0] + String.format("%-8s", match.getMatch().getInning().get(0).getBowling_team().getTeamName4() + " : " + rev.getReviewStatus().split(",")[1]) + ";");
			}			      
			break;
		}
	}
	
}