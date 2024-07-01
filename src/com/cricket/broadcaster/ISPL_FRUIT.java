package com.cricket.broadcaster;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.cricket.model.BattingCard;
import com.cricket.model.BowlingCard;
import com.cricket.model.Configuration;
import com.cricket.model.Player;
import com.cricket.model.Review;
import com.cricket.model.Speed;
import com.cricket.service.CricketService;
import com.cricket.model.Inning;
import com.cricket.model.MatchAllData;
import com.cricket.containers.Scene;
import com.cricket.util.CricketFunctions;
import com.cricket.util.CricketUtil;

public class ISPL_FRUIT extends Scene{

	public String session_selected_broadcaster = "ISPL_FRUIT";
	public String which_graphics_onscreen = "";
	boolean player_found = false,IsLastEventRead = false;
	int previousOver=-1,previousBall=-1,shotSize=0,EventSize=0;
	List<String>str=new ArrayList<String>();
	
	public ISPL_FRUIT() {
		super();
	}
	
	public ISPL_FRUIT(String scene_path, String which_Layer) {
		super(scene_path, which_Layer);
	}

	public void updateFruit(Scene scene, MatchAllData match,PrintWriter print_writer,Configuration config) throws Exception
	{
		populateFruit(match, print_writer,config);

	}
	
	public Object ProcessGraphicOption(String whatToProcess, MatchAllData match, CricketService cricketService,
			PrintWriter print_writer, List<Scene> scenes, String valueToProcess,Configuration config) throws Exception{
			
			switch (whatToProcess.toUpperCase()) {
			case "POPULATE-FRUIT": 
				populateFruit(match, print_writer,config);
				break;
			case "ANIMATE-IN-FRUIT": case "ANIMATE-OUT": case "CLEAR-ALL": 
				switch (session_selected_broadcaster.toUpperCase()) {
				case "ISPL_FRUIT":
					switch (whatToProcess.toUpperCase()) {
					case "ANIMATE-IN-FRUIT":
						processAnimation(print_writer, "In", "START", session_selected_broadcaster,1);
						which_graphics_onscreen = "FRUIT";
						break;
					
					case "CLEAR-ALL":
						print_writer.println("LAYER1*EVEREST*SINGLE_SCENE CLEAR;");
						which_graphics_onscreen = "";
						break;
					case "ANIMATE-OUT":
						switch(which_graphics_onscreen) {
						case "FRUIT":
							processAnimation(print_writer, "Out", "START", session_selected_broadcaster,1);
							which_graphics_onscreen = "";
							break;
						}
						break;
					}
				}
			}
			return null;
		}
	public void populateInfobar(PrintWriter print_writer,String viz_scene, MatchAllData match, String session_selected_broadcaster,Configuration config) throws Exception 
	{
		populateFruit(match, print_writer,config);
	}
	public void populateFruit( MatchAllData match,PrintWriter print_writer,Configuration config) throws Exception {
		BattingCard this_bc;
		List<Integer> data_int = new ArrayList<Integer>();
		List<String> data_str = new ArrayList<String>();
		data_str.add("");
						
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomeStatValue1 " + match.getMatch().getInning().get(0).getTotalFours() + ";");
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomeStatValue2 " + match.getMatch().getInning().get(0).getTotalSixes() + ";");
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomeStatValue5 " + match.getMatch().getInning().get(0).getTotalNines() + ";");
		for(Inning inn : match.getMatch().getInning()) {
			if (inn.getIsCurrentInning().toUpperCase().equalsIgnoreCase(CricketUtil.YES)) {
				
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomeTeamName " + 
						inn.getBatting_team().getTeamName2() + ";");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tAwayTeamName " + 
						inn.getBowling_team().getTeamName2() + ";");
/********************************************************** EVENTS  *********************************************/
			
				data_str = new ArrayList<String>();
				for(int i = 0; i <= 12; i++) {
					if(i==0||i==1) {
						data_str.add("0");
					}else {
						data_str.add(""); 
					}
					
				}
				data_int = new ArrayList<Integer>();
				for(int i = 0; i <= 3; i++) {
					data_int.add(0); 
				} 
				Event_of_fruit(data_str, data_int,inn,match);
/****************************************** Powerplay ***************************************************************/
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
	    			
	    		}else if(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.DT20) 
	    			|| match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.IT20)
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
				
/******************************************* Team total and comparision ***********************************************/
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
/************************************************  comparision  ********************************************************************  */
				
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
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET AwayStatValue5 " + "0" + ";");

				}else if(inn.getInningNumber() == 2) {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tText " 
							+ CricketFunctions.generateMatchSummaryStatus(inn.getInningNumber(), match, CricketUtil.SHORT, session_selected_broadcaster).toUpperCase() + ";");
						
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
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomeStatValue1 " + match.getMatch().getInning().get(0).getTotalFours() + ";");
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomeStatValue2 " + match.getMatch().getInning().get(0).getTotalSixes() + ";");

						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tAwayStatValue1 " + inn.getTotalFours() + ";");
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tAwayStatValue2 " + inn.getTotalSixes() + ";");
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET AwayStatValue5 " + inn.getTotalNines() + ";");
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomeStatValue3 " + CricketFunctions.compareInningData(match, "-", 1, match.getEventFile().getEvents()) + ";");
						if(inn.getTotalWickets()==10) {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tAwayStatValue3 " +inn.getTotalRuns()+ ";");

						}else {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tAwayStatValue3 " +inn.getTotalRuns()+" - "+inn.getTotalWickets() + ";");
	
						}
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET AwayStatValue4 " +data_str.get(1) + ";");
					
				}

				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomeStatValue4 " + data_str.get(0) + ";");
				Integer lastFewOverData=data_int.get(1);
				
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSmallFreeText1 " + "BALL" + 
						CricketFunctions.Plural(lastFewOverData).toUpperCase()+ " SINCE LAST BOUNDARY : " +data_int.get(1) + ";");
				
/****************************************** Projected And PhaseBy *************************************************************/
	
				if(inn.getInningNumber() == 1) {
					if(inn.getRunRate() != null) {
						str = CricketFunctions.projectedScore(match);			    
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectDataType " + "0" + ";");
						
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tProjectedHead1 " + 
								"@" +str.get(0) +" (CRR)" + ";");
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tProjectedValue1 " + 
								str.get(1) + ";");
						
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tProjectedHead2 " + 
								"@" + str.get(2) +" RPO" + ";");
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tProjectedValue2 " + 
								str.get(3) + ";");
						
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tProjectedHead3 " + 
								"@" + str.get(4) +" RPO" + ";");
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tProjectedValue3 " + 
								str.get(5)+ ";");
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
					
				}
				if(inn.getInningNumber() == 2 & inn.getIsCurrentInning().equalsIgnoreCase("YES")) {
					 
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectDataType " + "1" + ";");
					
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeamName1 " + 
							match.getMatch().getInning().get(0).getBatting_team().getTeamName4() + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeamName2 " + 
							match.getMatch().getInning().get(1).getBatting_team().getTeamName4() + ";");
					
					
					
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeam1PhaseScore1 " + 
							data_str.get(6) + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeam1PhaseScore2 " + 
							data_str.get(7)+ ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeam1PhaseScore3 " + 
							data_str.get(8) + ";");
					
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeam2PhaseScore1 " + 
							data_str.get(9) + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeam2PhaseScore2 " + 
							data_str.get(10) + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeam2PhaseScore3 " + 
							data_str.get(11) + ";");
					
				}
/****************************************************Current Two Batsmen*******************************/
				if(inn.getPartnerships() != null && inn.getPartnerships().size() > 0) {
					
					this_bc = inn.getBattingCard().stream().filter(batcard -> batcard.getPlayerId() 
							== inn.getPartnerships().get(inn.getPartnerships().size() - 1).getFirstBatterNo()).findAny().orElse(null);
					
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBatterName1 " + this_bc.getPlayer().getTicker_name() + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBatterRun1 " + this_bc.getRuns() + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBatterBall1 " + this_bc.getBalls() + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBoundaries1 " + this_bc.getFours() + "/" + 
						this_bc.getSixes() + ";");
					
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tNines1 " + this_bc.getNines() + ";");
					if(this_bc.getStatus().toUpperCase().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
						if(this_bc.getOnStrike().equalsIgnoreCase(CricketUtil.YES)) {	
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectStriker1 1;");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectStriker2 0;");
						
						}
					}
					
					
					if(this_bc.getStrikeRate() != null) {
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStrikeRate1 " + this_bc.getStrikeRate() + ";");
					}else {
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStrikeRate1 " + "-" + ";");
					}
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tMinutes1 " + this_bc.getSeconds()/60 + ";");
					
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPartnershipContribution1 " + 
							inn.getPartnerships().get(inn.getPartnerships().size() - 1).getFirstBatterRuns() + "(" + 
							inn.getPartnerships().get(inn.getPartnerships().size() - 1).getFirstBatterBalls() + ")" + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPartnershipContribution2 " + 
							inn.getPartnerships().get(inn.getPartnerships().size() - 1).getSecondBatterRuns() + "(" + 
							inn.getPartnerships().get(inn.getPartnerships().size() - 1).getSecondBatterBalls() + ")" + ";");

					BattingCard this_bc1 = inn.getBattingCard().stream().filter(batcard -> batcard.getPlayerId() 
							== inn.getPartnerships().get(inn.getPartnerships().size() - 1).getSecondBatterNo()).findAny().orElse(null);
	
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBatterName2 " + this_bc1.getPlayer().getTicker_name() + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBatterRun2 " + this_bc1.getRuns() + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBatterBall2 " + this_bc1.getBalls() + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBoundaries2 " + this_bc1.getFours() + "/" + 
							this_bc1.getSixes() + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tNines2 " + this_bc1.getNines() + ";");
					
					if(this_bc1.getStatus().toUpperCase().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
						if(this_bc1.getOnStrike().equalsIgnoreCase(CricketUtil.YES)) {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectStriker1 0;");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectStriker2 1;");
						}	
					}
					if(this_bc1.getStrikeRate() != null) {
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStrikeRate2 " + this_bc1.getStrikeRate() + ";");
					}else {
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStrikeRate2 " + "-" + ";");
					}
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tMinutes2 " + this_bc1.getSeconds()/60 + ";");

					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPartnershipRus " + 
							inn.getPartnerships().get(inn.getPartnerships().size() - 1).getTotalRuns() + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPartnershipBalls " + 
							inn.getPartnerships().get(inn.getPartnerships().size() - 1).getTotalBalls() + ";");
					
				}
				
/******************************************Run rate**********************************/
				
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

/******************************************************* FALL OF WICKETS AND LAST WICKET *******************************/
				
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main$All$LastWicketGrp$DataGrp$XPosition$LastWicketValue*CONTAINER SET ACTIVE 1;");
				
				if(inn.getFallsOfWickets() != null && inn.getFallsOfWickets().size() > 0) {
					this_bc = inn.getBattingCard().stream().filter(batC -> inn.getFallsOfWickets().get(inn.getFallsOfWickets().size() - 1).getFowPlayerID() 
							== batC.getPlayerId()).findAny().orElse(null);
					
					if(this_bc != null) {
						String how_out = this_bc.getPlayer().getTicker_name()+"  "+this_bc.getHowOutText();
			
						if(how_out.length()>31) {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tLastWicketValue " 
									+ this_bc.getPlayer().getTicker_name() +  "    ;");
						}else {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tLastWicketValue " 
									+ this_bc.getPlayer().getTicker_name() + "  " +this_bc.getHowOutText()+ "  ;");
						}
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tLastWicketScore " 
									+" "+ this_bc.getRuns() + ";");
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tLastWicketBalls " 
									+ this_bc.getBalls() + ";");
					}
					if(inn.getInningNumber() == 1 && inn.getFallsOfWickets() != null) {
						
						    for (int i = 0; i <= inn.getFallsOfWickets().size() -1; i++)
						    {
						    	print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeam1Fow" + (i+1) + " " + 
						    			inn.getFallsOfWickets().get(i).getFowRuns() + ";");
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
				}else {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tLastWicketValue " + "" + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tLastWicketScore " + "" + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tLastWicketBalls " + "" + ";");
				}
/**************************************** BATTING CARD  *******************************************************************/
				if(inn.getFallsOfWickets()!=null) {
					if(inn.getBattingTeamId() == match.getSetup().getHomeTeamId() && inn.getFallsOfWickets().size() > 0) {
						//print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectHomePlayerNumber " + 
						//		inn.getFallsOfWickets().size() + ";");
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
										}int in=i;
										inn.getBattingCard().stream()
									    .filter(bc -> inn.getFallsOfWickets().get(in).getFowPlayerID() == bc.getPlayerId())
									    .forEach(bc -> {
									        print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectHomePlayer" + (in + 1) + " 1" + ";");
									        print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vHomeSelectStriker" + (in + 1) + " 0" + ";");
									        print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeamsHeade " +
									                " "+ ";");
									        print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomeScore" + (in + 1) + " " +
									                bc.getRuns() + ";");
									        print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomeBalls" + (in + 1) + " " +
									                bc.getBalls() + ";");
									    });
									}
								}
					}else if(inn.getBattingTeamId() == match.getSetup().getAwayTeamId()) {
						if(inn.getFallsOfWickets() != null) {
							if(inn.getFallsOfWickets().size() > 0){
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
										int in=i;
										inn.getBattingCard().stream()
									    .filter(bc -> inn.getFallsOfWickets().get(in).getFowPlayerID() == bc.getPlayerId())
									    .forEach(bc -> {
												print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectHomePlayer" + (in+1) + " 1" + ";");
												print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vHomeSelectStriker" + (in+1) + " 0" + ";");
												print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeamsHeade " + 
														" " + ";");
												print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomeScore" + (in+1) + " " + 
														bc.getRuns() + ";");
												print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomeBalls" + (in+1) + " " + 
														bc.getBalls() + ";");
									 });
										
									}
								}
							}
						}
					}
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectHomePlayerNumber " + 
							inn.getFallsOfWickets().size() + ";");
				}
/****************************************Current and last bowlers*******************************************************************/
				if(inn.getBowlingCard() != null) {
					for(BowlingCard boc : inn.getBowlingCard()) {
						if(boc.getStatus().toUpperCase().equalsIgnoreCase(CricketUtil.CURRENT + CricketUtil.BOWLER)) {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectBowler2" + " " + "0" + ";");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectBowler1" + " " + "1" + ";");
							
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBowlerName1 " + boc.getPlayer().getTicker_name() + ";");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFigure1 " + boc.getWickets() + "-" + boc.getRuns() + ";");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tOvers1 " + CricketFunctions.OverBalls(boc.getOvers(), boc.getBalls())+ ";");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tDots1 " +  boc.getDots() + ";");
							
							if(boc.getEconomyRate() == ""||boc.getEconomyRate() == null) {
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tEconomy1 " + "-" + ";");
							}else {
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tEconomy1 " +boc.getEconomyRate()+ ";");
							}
						}
						else if(boc.getStatus().toUpperCase().equalsIgnoreCase(CricketUtil.LAST + CricketUtil.BOWLER)) {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectBowler1" + " " + "0" + ";");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectBowler2" + " " + "0" + ";");
							
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBowlerName1 " + boc.getPlayer().getTicker_name() + ";");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFigure1 " + boc.getWickets() + "-" + boc.getRuns() + ";");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tOvers1 " + CricketFunctions.OverBalls(boc.getOvers(), boc.getBalls())+ ";");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tDots1 " +  boc.getDots() + ";");
							
							if(boc.getEconomyRate() == ""||boc.getEconomyRate() == null) {
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tEconomy1 " + "-" + ";");
							}else {
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tEconomy1 " +boc.getEconomyRate()+ ";");
							}
							
						}
						
							if(data_int.get(0)!=0) {
								if(boc.getPlayerId()==data_int.get(0)) {
									print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBowlerName2 " + boc.getPlayer().getTicker_name() + ";");
									print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFigure2 " + boc.getWickets() + "-" + boc.getRuns() + ";");
									print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tOvers2 " + CricketFunctions.OverBalls(boc.getOvers(), boc.getBalls())+ ";");
									print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tDots2 " +  boc.getDots() + ";");
									
									if(boc.getEconomyRate() == "") {
										print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tEconomy2 " + "-" + ";");
									}else {
										print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tEconomy2 " +boc.getEconomyRate()+ ";");
									}
								}	
							}else {
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBowlerName2  -;");
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFigure2  -;");
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tOvers2  -;");
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tDots2  -;");
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tEconomy2 " + "-" + ";");
							}
	/******************************************  THIS OVER  *****************************************************/

					if(match.getEventFile().getEvents().get(match.getEventFile().getEvents().size() - 1).getEventType().equalsIgnoreCase(CricketUtil.LOG_50_50)) {
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tThisOverHead " + 
								"THIS OVER       "+ data_int.get(2)+"  RUN"+ CricketFunctions.Plural(Integer.valueOf( data_int.get(2))).toUpperCase() + 
								" (" + match.getEventFile().getEvents().get(match.getEventFile().getEvents().size() - 1).getEventExtra() 
								+ match.getEventFile().getEvents().get(match.getEventFile().getEvents().size() - 1).getEventExtraRuns() + ")"+";");
					}else {
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tThisOverHead " + 
								"THIS OVER       "+ data_int.get(2)+"  RUN"+ CricketFunctions.Plural(Integer.valueOf( data_int.get(2))).toUpperCase() + ";");
					}
					
							
					if(boc.getStatus().toUpperCase().equalsIgnoreCase("CURRENTBOWLER") || boc.getStatus().toUpperCase().equalsIgnoreCase("LASTBOWLER")) {
						String str=replaceTermsInString(data_str.get(4));
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tThisOverData " + 
								reverseStringWithPreservation(str) + ";");
						
						if (((inn.getTotalOvers() * Integer.valueOf(match.getSetup().getBallsPerOver())) + inn.getTotalBalls()) >= 30)
						{	
							String[] last_30 = data_str.get(5).split("-");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSmallFreeText2 " + "LAST 30 BALLS : " + 
									last_30[0] + " RUN" + CricketFunctions.Plural(Integer.valueOf(last_30[0])).toUpperCase() + " , " + last_30[1] +
									" WICKET" + CricketFunctions.Plural(Integer.valueOf(last_30[1])).toUpperCase()+ ";");
						}
						else  
						{
							if(inn.getTotalOvers() > 0 || inn.getTotalBalls() > 0) {
								/********************************PreOtherRunWicket*/
								String Run_wicket[]=data_str.get(3).split("-");
								int wicket = Integer.valueOf(Run_wicket[1]);
								int run = Integer.valueOf(Run_wicket[0]);
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSmallFreeText2 " + "LAST OVER : " + 
										run+ " RUN" +CricketFunctions.Plural(run).toUpperCase() +" AND " + wicket + " WICKET" +CricketFunctions.Plural(wicket).toUpperCase() + ";");
							}else {
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSmallFreeText2 " + "LAST OVER : " + ";");
							}
						}
					}
/**************************************** BOWLING CARD  *******************************************************************/
					if(inn.getBowlingTeamId() == match.getSetup().getHomeTeamId() && inn.getBowlingCard() != null) {
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
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectAwayPlayerNumber " + 
									inn.getBowlingCard().size() + ";");
						}else if(inn.getBowlingTeamId() == match.getSetup().getAwayTeamId()&& inn.getBowlingCard() != null) {
							
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
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectAwayPlayerNumber " + 
										inn.getBowlingCard().size() + ";");
							}
						}	
				}
			}
				if(config.getAudio().equalsIgnoreCase(CricketUtil.LastBallAudio)) // LastBall 
				{
					if(inn.getTotalBalls()==5 && previousOver != inn.getTotalOvers()) {
						if(new File(CricketUtil.CRICKET_DIRECTORY+"Audio/Last ball.WAV").exists()) {
							playAudio(CricketUtil.CRICKET_DIRECTORY+"Audio/Last ball.WAV");	
						}
						previousOver = inn.getTotalOvers();
						//previousBall = inn.getTotalBalls();
					}
				}
			}
			
		}
		
	}

	public void processAnimation(PrintWriter print_writer, String animationName,String animationCommand, String which_broadcaster,int which_layer)
	{
		switch(which_broadcaster.toUpperCase()) {
		case CricketUtil.ISPL_FRUIT:
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
	public void initialize_fruit(PrintWriter print_writer, MatchAllData match ,Configuration config) throws IOException {
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSpeedHead " + 
    			"SPEED" + ";");
	
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSpeedUnit " + 
							"("+config.getSpeedUnit()+")" + ";");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTounamentName " + match.getSetup().getTournament().toUpperCase() + ";");
		
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tMatchNumber " + match.getSetup().getMatchIdent()+" : "+
				match.getSetup().getHomeTeam().getTeamName4()+" vs "+match.getSetup().getAwayTeam().getTeamName4()+ ";");
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
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tAwayStatValue1 " + "-" + ";");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomeStatValue1 " + "-" + ";");
		
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
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tThisOverData " + " " + ";");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBatterName1 " +  ";");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStrikeRate1 " + "-" + ";");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBatterName2 " +  ";");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStrikeRate " + "-" + ";");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSmallFreeText2 " + "LAST OVER : " + ";");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPartnershipRus " +";");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPartnershipBalls " +";");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBatterRun1 " +";"); 
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBatterRun2 " +";"); 
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBatterBall1 " +";");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBatterBall2 " +";"); 



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
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectHomePlayerNumber " + 
				"0" + ";");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectAwayPlayerNumber " + 
				"0" + ";");
	}	
	public void populateSpeed(PrintWriter printWriter, Speed lastSpeed) throws Exception {
		
		if(lastSpeed != null) {
			printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSpeedValue " + lastSpeed.getSpeedValue()  + ";");
		}else {
			printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSpeedValue;");
		}			      
	} 	
	public void populateReview(PrintWriter printWriter, MatchAllData match, Review review) throws Exception {
		switch (this.session_selected_broadcaster) {
		case CricketUtil.ISPL_FRUIT:
			if(review == null) {
	    		printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeamsHeader " + "SUMMARY " + ";");
			}else {
				
	    		printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeamsHeader " +  "REVIEWS REMAINING :-" + 
	    				String.format("%-16s", match.getMatch().getInning().get(0).getBatting_team().getTeamName4()) + " : " + 
	    				review.getReviewStatus().split(",")[0] + String.format("%-8s", match.getMatch().getInning().get(0).getBowling_team().getTeamName4() + " : " + review.getReviewStatus().split(",")[1]) + ";");
			}			      
			break;
		}
	}

	private void Event_of_fruit(List<String> data_str, List<Integer> data_int, Inning inn,MatchAllData match) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		
		boolean bowler = false,this_bowler = false,ballsSinceLastBoundary = false, last_ovr = false;
		
		int wicket_count = 0, run_count=0,run_c=0,wkt_c=0,ball=0,this_ovr_run=0,this_ovr_wk=0
				,firstInnCompTotRuns=0, firstInnCompTotWkts = 0,firstInnDotBalls = 0,secondInnDotBalls=0,PPIndex = -1,ballCount=0,ballCount1=0,over_c=0;
		
		List<Integer> firstInnPPData = new ArrayList<Integer>();
		List<Integer> secondInnPPData = new ArrayList<Integer>();
		
		for(int i = 1; i <= 7; i++) {
			firstInnPPData.add(0);
			secondInnPPData.add(0);
		}
		BowlingCard this_bowC ;data_int.set(1, data_int.get(1) + 0);
		if (match.getEventFile().getEvents() != null && match.getEventFile().getEvents().size() > 0) {
		    for (int i = match.getEventFile().getEvents().size() - 1; i >= 0; i--) {
		    	
		    	if(!IsLastEventRead) {
					switch (match.getEventFile().getEvents().get(i).getEventType())
					 {
					 case CricketUtil.WIDE: case CricketUtil.NO_BALL: case CricketUtil.BYE: 
					  case CricketUtil.LEG_BYE: case CricketUtil.PENALTY: 
						  if(EventSize != match.getEventFile().getEvents().size()) {
							  CricketFunctions.getInteractive(match,"OVERWRITE"); 
							  EventSize=match.getEventFile().getEvents().size();
							  IsLastEventRead=true;
						  }
						 
						 break;
					  case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE:  case CricketUtil.FIVE : 
					  case CricketUtil.DOT:case CricketUtil.LOG_WICKET: case CricketUtil.LOG_ANY_BALL:
					  case CricketUtil.FOUR: case CricketUtil.SIX:
						  
						  if( match.getMatch().getShots()!=null && shotSize!= match.getMatch().getShots().size()) {
							  CricketFunctions.getInteractive(match,"OVERWRITE"); 
							  shotSize=match.getMatch().getShots().size();
							  IsLastEventRead=true;
						  }else if( match.getMatch().getShots()==null ) {
							  if(EventSize != match.getEventFile().getEvents().size()) {
								  CricketFunctions.getInteractive(match,"OVERWRITE"); 
								  EventSize=match.getEventFile().getEvents().size();
							  }
						  }							

						  break;
						default:
							 IsLastEventRead=true;
							break;
					 }	
				}
		    	/**************************************POWERPLAY***********************************************/
				switch(match.getEventFile().getEvents().get(i).getEventType()) {
				case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE:  case CricketUtil.FIVE : case CricketUtil.DOT:
				case CricketUtil.FOUR: case CricketUtil.SIX: case CricketUtil.LOG_WICKET: case CricketUtil.BYE: case CricketUtil.LEG_BYE:
				case CricketUtil.NINE:
					if(match.getEventFile().getEvents().get(i).getEventInningNumber() == 1) {
						ballCount += 1;
					}else if(match.getEventFile().getEvents().get(i).getEventInningNumber() == 2) {
						ballCount1 += 1;
					}
					if(match.getEventFile().getEvents().get(i).getEventInningNumber() == 1) {
						if(ballCount >= ((match.getMatch().getInning().get(1).getFirstPowerplayStartOver() - 1) * Integer.valueOf(match.getSetup().getBallsPerOver())) && 
								ballCount <= (match.getMatch().getInning().get(1).getFirstPowerplayEndOver() * Integer.valueOf(match.getSetup().getBallsPerOver()))) {
			                	PPIndex = 0;
			                } else if(ballCount > ((match.getMatch().getInning().get(1).getSecondPowerplayStartOver() - 1) * Integer.valueOf(match.getSetup().getBallsPerOver())) && 
			                		ballCount <= (match.getMatch().getInning().get(1).getSecondPowerplayEndOver() * Integer.valueOf(match.getSetup().getBallsPerOver()))) {
				                PPIndex = 2;
			                } else if(ballCount > ((match.getMatch().getInning().get(1).getThirdPowerplayStartOver() - 1) * Integer.valueOf(match.getSetup().getBallsPerOver())) && 
			                		ballCount <= (match.getMatch().getInning().get(1).getThirdPowerplayEndOver() * Integer.valueOf(match.getSetup().getBallsPerOver()))) {
				                PPIndex = 4;
				            }
						
					}	
				 else if(match.getEventFile().getEvents().get(i).getEventInningNumber() == 2) {
						if(ballCount1 >= ((match.getMatch().getInning().get(1).getFirstPowerplayStartOver() - 1) * Integer.valueOf(match.getSetup().getBallsPerOver())) && 
								ballCount1 <= (match.getMatch().getInning().get(1).getFirstPowerplayEndOver() * Integer.valueOf(match.getSetup().getBallsPerOver()))) {
			                	PPIndex = 0;
			        
			                } else if(ballCount1 > ((match.getMatch().getInning().get(1).getSecondPowerplayStartOver() - 1) * Integer.valueOf(match.getSetup().getBallsPerOver())) && 
			                		ballCount1 <= (match.getMatch().getInning().get(1).getSecondPowerplayEndOver() * Integer.valueOf(match.getSetup().getBallsPerOver()))) {
				                    PPIndex = 2;
				           
			                } else if(ballCount1 > ((match.getMatch().getInning().get(1).getThirdPowerplayStartOver() - 1) * Integer.valueOf(match.getSetup().getBallsPerOver())) && 
			                		ballCount1 <= (match.getMatch().getInning().get(1).getThirdPowerplayEndOver() * Integer.valueOf(match.getSetup().getBallsPerOver()))) {
			                		PPIndex = 4;
				            }
						
				 }	                
	                if(PPIndex >= 0) {
	        			switch (match.getEventFile().getEvents().get(i).getEventType()){
	                	case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE:  case CricketUtil.FIVE : 
	                	case CricketUtil.DOT: case CricketUtil.FOUR: case CricketUtil.SIX: case CricketUtil.WIDE: 
	                	case CricketUtil.NO_BALL: case CricketUtil.BYE: case CricketUtil.LEG_BYE: case CricketUtil.PENALTY:
	                	case CricketUtil.LOG_WICKET: case CricketUtil.LOG_ANY_BALL: case CricketUtil.NINE:
	                		
	    					if(match.getEventFile().getEvents().get(i).getEventInningNumber() == 1) {
	    						
	    						firstInnPPData.set(PPIndex, firstInnPPData.get(PPIndex) + 
	                    				match.getEventFile().getEvents().get(i).getEventRuns());

	    						
	    					} else if(match.getEventFile().getEvents().get(i).getEventInningNumber() == 2) {
	    						
	    						secondInnPPData.set(PPIndex, secondInnPPData.get(PPIndex) + 
	                    				match.getEventFile().getEvents().get(i).getEventRuns());
	    					}
	    					
	    					
		        			switch (match.getEventFile().getEvents().get(i).getEventType()){
		        			case CricketUtil.WIDE: case CricketUtil.NO_BALL: 
		        			case CricketUtil.BYE: case CricketUtil.LEG_BYE: case CricketUtil.PENALTY:
		        				if(match.getEventFile().getEvents().get(i).getEventInningNumber() == 1) {
		    						
		                    		firstInnPPData.set(PPIndex, firstInnPPData.get(PPIndex) + 
		                    				match.getEventFile().getEvents().get(i).getEventExtraRuns()+
		                    				match.getEventFile().getEvents().get(i).getEventSubExtraRuns());
		                    		
		    					} else if(match.getEventFile().getEvents().get(i).getEventInningNumber() == 2) {
		    						
		    						secondInnPPData.set(PPIndex, secondInnPPData.get(PPIndex) + 
		                    				match.getEventFile().getEvents().get(i).getEventExtraRuns()+
		                    				match.getEventFile().getEvents().get(i).getEventSubExtraRuns());

		    					}
		        				break;
	                    	case CricketUtil.LOG_WICKET:
	        					if(match.getEventFile().getEvents().get(i).getEventInningNumber() == 1) {
	        						
	                        		firstInnPPData.set(PPIndex + 1, firstInnPPData.get(PPIndex + 1) + 1);
	        					} else if(match.getEventFile().getEvents().get(i).getEventInningNumber() == 2) {
	        						
	        						secondInnPPData.set(PPIndex + 1, secondInnPPData.get(PPIndex + 1) + 1);
	        					}
	                    		break;
	                    	case CricketUtil.LOG_ANY_BALL:
	                            if (match.getEventFile().getEvents().get(i).getEventExtra() != null)
	                            {
	            					if(match.getEventFile().getEvents().get(i).getEventInningNumber() == 1) {
	                            		firstInnPPData.set(PPIndex, firstInnPPData.get(PPIndex) + match.getEventFile().getEvents().get(i).getEventExtraRuns());
	            					} else if(match.getEventFile().getEvents().get(i).getEventInningNumber() == 2) {
	                            		secondInnPPData.set(PPIndex, secondInnPPData.get(PPIndex) + match.getEventFile().getEvents().get(i).getEventExtraRuns());
	            					}
	                            }
	                            if (match.getEventFile().getEvents().get(i).getEventSubExtra() != null)
	                            {
	            					if(match.getEventFile().getEvents().get(i).getEventInningNumber() == 1) {
	                            		firstInnPPData.set(PPIndex, firstInnPPData.get(PPIndex) + match.getEventFile().getEvents().get(i).getEventSubExtraRuns());
	            					} else if(match.getEventFile().getEvents().get(i).getEventInningNumber() == 2) {
	                            		secondInnPPData.set(PPIndex, secondInnPPData.get(PPIndex) + match.getEventFile().getEvents().get(i).getEventSubExtraRuns());
	            					}
	                            }
	                            if (match.getEventFile().getEvents().get(i).getEventHowOut() != null 
	                            	&& !match.getEventFile().getEvents().get(i).getEventHowOut().isEmpty())
	                            {
	            					if(match.getEventFile().getEvents().get(i).getEventInningNumber() == 1) {
	            						
	                            		firstInnPPData.set(PPIndex + 1, firstInnPPData.get(PPIndex + 1) + 1);
	            					} else if(match.getEventFile().getEvents().get(i).getEventInningNumber() == 2) {
	            						secondInnPPData.set(PPIndex + 1, secondInnPPData.get(PPIndex + 1) + 1);
	            					}
	                            }
	                    		break;
		        			}
	                        break;
	        			}
	                }
					break;
	        	}
		    	
/*********************************************** INNING COMPARISON **********************************************************************/
		     if (match.getEventFile().getEvents().get(i).getEventInningNumber() == 1) {
		        if (match.getEventFile().getEvents().get(i).getEventOverNo() <= match.getMatch().getInning().get(1).getTotalOvers()
		                && match.getEventFile().getEvents().get(i).getEventBallNo() <= match.getMatch().getInning().get(1).getTotalBalls()) {
		            switch (match.getEventFile().getEvents().get(i).getEventType()) {
		                case CricketUtil.ONE: case CricketUtil.TWO: case CricketUtil.THREE: case CricketUtil.FIVE:
		                case CricketUtil.DOT: case CricketUtil.FOUR: case CricketUtil.SIX: case CricketUtil.NINE:
		                    firstInnCompTotRuns = firstInnCompTotRuns + match.getEventFile().getEvents().get(i).getEventRuns();
		                    break;
		                case CricketUtil.WIDE: case CricketUtil.NO_BALL: case CricketUtil.BYE: case CricketUtil.LEG_BYE: case CricketUtil.PENALTY:
		                	 firstInnCompTotRuns = firstInnCompTotRuns +  match.getEventFile().getEvents().get(i).getEventRuns() +
		                            match.getEventFile().getEvents().get(i).getEventSubExtraRuns() +
		                            match.getEventFile().getEvents().get(i).getEventExtraRuns();
		                    break;
		                case CricketUtil.LOG_WICKET:
		                	 firstInnCompTotRuns = firstInnCompTotRuns +  match.getEventFile().getEvents().get(i).getEventRuns();
		                    switch (match.getEventFile().getEvents().get(i).getEventHowOut()) {
		                        case CricketUtil.RETIRED_HURT: case CricketUtil.ABSENT_HURT: case CricketUtil.CONCUSSED:
		                            break;
		                        default:
		                            firstInnCompTotWkts =firstInnCompTotWkts + 1;
		                            break;
		                    }
		                    break;
		                case CricketUtil.LOG_ANY_BALL:
		                	 firstInnCompTotRuns = firstInnCompTotRuns +  match.getEventFile().getEvents().get(i).getEventRuns();
		                    if (match.getEventFile().getEvents().get(i).getEventExtra() != null
		                            && !match.getEventFile().getEvents().get(i).getEventExtra().isEmpty()) {
		                    	 firstInnCompTotRuns = firstInnCompTotRuns +  match.getEventFile().getEvents().get(i).getEventExtraRuns();
		                    }
		                    if (match.getEventFile().getEvents().get(i).getEventSubExtra() != null
		                            && !match.getEventFile().getEvents().get(i).getEventSubExtra().isEmpty()) {
		                        firstInnCompTotRuns = firstInnCompTotRuns + match.getEventFile().getEvents().get(i).getEventSubExtraRuns();
		                    }
		                    if (match.getEventFile().getEvents().get(i).getEventHowOut() != null
		                            && !match.getEventFile().getEvents().get(i).getEventHowOut().isEmpty()) {
		                    	 firstInnCompTotWkts =firstInnCompTotWkts + 1;
		                    }
		                    break;
		            }
		        }
		        data_str.set(2, firstInnCompTotRuns + "-" + firstInnCompTotWkts);
		    }
		    
/*********************************************LAST 30 BALLS ***********************************************/
					if (ball <= 30) {
						switch (match.getEventFile().getEvents().get(i).getEventType())
						    {
						    case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE:  case CricketUtil.FIVE : case CricketUtil.DOT:
						    case CricketUtil.FOUR: case CricketUtil.SIX: case CricketUtil.BYE: case CricketUtil.LEG_BYE: case CricketUtil.PENALTY:
						    case CricketUtil.NINE:
						    	ball++;
					    		run_c+=match.getEventFile().getEvents().get(i).getEventRuns() + match.getEventFile().getEvents().get(i).getEventSubExtraRuns();					    	break;
						    	case CricketUtil.WIDE: case CricketUtil.NO_BALL: 
						    		run_c+=match.getEventFile().getEvents().get(i).getEventRuns() + match.getEventFile().getEvents().get(i).getEventSubExtraRuns();
						      break;
						    	case CricketUtil.LOG_ANY_BALL:
						    		run_c+=match.getEventFile().getEvents().get(i).getEventRuns()+match.getEventFile().getEvents().get(i).getEventExtraRuns()
						    		  + match.getEventFile().getEvents().get(i).getEventSubExtraRuns();
						    		if(!match.getEventFile().getEvents().get(i).getEventSubExtra().equalsIgnoreCase(CricketUtil.WIDE) || 
							    			!match.getEventFile().getEvents().get(i).getEventSubExtra().equalsIgnoreCase(CricketUtil.NO_BALL)) {
						    			if (match.getEventFile().getEvents().get(i).getEventHowOut() != null
										    	&& !match.getEventFile().getEvents().get(i).getEventHowOut().isEmpty()) {
							    			 wkt_c=wkt_c+1;
						                }
						    		}
						    		
						    		break;
						    case CricketUtil.LOG_WICKET: 
						    	ball++;
						    	run_c+=match.getEventFile().getEvents().get(i).getEventRuns()+match.getEventFile().getEvents().get(i).getEventExtraRuns()
					    		  + match.getEventFile().getEvents().get(i).getEventSubExtraRuns();
						    	if(match.getEventFile().getEvents().get(i).getEventHowOut().equalsIgnoreCase(CricketUtil.RETIRED_HURT)||
						    			match.getEventFile().getEvents().get(i).getEventHowOut().equalsIgnoreCase(CricketUtil.ABSENT_HURT)||
						    			match.getEventFile().getEvents().get(i).getEventHowOut().equalsIgnoreCase(CricketUtil.CONCUSSED)) {
						    		continue;
						    	}else {
						    		 wkt_c=wkt_c+1;	
						    	}	
						      break;
						    }
						if(ball==30) {
							data_str.set(5, String.valueOf(run_c)+"-"+String.valueOf(wkt_c));
							}
						  }	

	/****************** Last bowler Id *************************************************************************/
					if(match.getEventFile().getEvents().get(i).getEventInningNumber() 
							== match.getMatch().getInning().stream().filter(in -> in.getIsCurrentInning()
							.equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null).getInningNumber()) {
					
						this_bowC = match.getMatch().getInning().stream().filter(in -> in.getIsCurrentInning()
						        .equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null).getBowlingCard().stream()
						        .filter(bowlingCard -> bowlingCard.getStatus().equalsIgnoreCase(CricketUtil.LAST + CricketUtil.BOWLER)).findAny().orElse(null);
					
						if (this_bowC!=null && this_bowC.getPlayerId()!=0) {
						if (match.getEventFile().getEvents().get(i).getEventType().equalsIgnoreCase(CricketUtil.END_OVER)) {
							over_c++;
							if(over_c==2) {
								data_int.set(0, match.getEventFile().getEvents().get(i).getEventBowlerNo());
							}
						}
					}else {
						if (match.getEventFile().getEvents().get(i).getEventType().equalsIgnoreCase(CricketUtil.END_OVER)) {
							over_c++;
							if(over_c==1) {
								data_int.set(0, match.getEventFile().getEvents().get(i).getEventBowlerNo());
							}
						}
						
					}
				}
				
				
				
				
		/***********************************************Balls since last boundary********************************************/
				if(match.getEventFile().getEvents().get(i).getEventInningNumber() 
						== match.getMatch().getInning().stream().filter(in -> in.getIsCurrentInning()
						.equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null).getInningNumber()) {
					
					if (match.getEventFile().getEvents().get(i).getEventType().equalsIgnoreCase(CricketUtil.SIX) 
				    		|| match.getEventFile().getEvents().get(i).getEventType().equalsIgnoreCase(CricketUtil.FOUR)
				    		||match.getEventFile().getEvents().get(i).getEventType().equalsIgnoreCase(CricketUtil.NINE)
				    		&& match.getEventFile().getEvents().get(i).getEventWasABoundary() != null
				    		&& match.getEventFile().getEvents().get(i).getEventWasABoundary().equalsIgnoreCase(CricketUtil.YES)) {
							
							ballsSinceLastBoundary = true;
							data_int.set(1, data_int.get(1) + 0);
							
						} else {
							if(ballsSinceLastBoundary == false) {
								
								switch (match.getEventFile().getEvents().get(i).getEventType()) {
									case CricketUtil.ONE: case CricketUtil.TWO: case CricketUtil.THREE: 
									case CricketUtil.DOT: case CricketUtil.FIVE:case CricketUtil.BYE: case CricketUtil.NINE:
									case CricketUtil.FOUR: case CricketUtil.SIX:
									case CricketUtil.LEG_BYE: case CricketUtil.PENALTY: case CricketUtil.LOG_WICKET:
										data_int.set(1, data_int.get(1) + 1);
										
										break;
									case CricketUtil.LOG_ANY_BALL:
										if (match.getEventFile().getEvents().get(i).getEventType().equalsIgnoreCase(CricketUtil.SIX) 
									    		|| match.getEventFile().getEvents().get(i).getEventType().equalsIgnoreCase(CricketUtil.FOUR)
									    		|| match.getEventFile().getEvents().get(i).getEventType().equalsIgnoreCase(CricketUtil.NINE)
									    		&& match.getEventFile().getEvents().get(i).getEventWasABoundary() != null
									    		&& match.getEventFile().getEvents().get(i).getEventWasABoundary().equalsIgnoreCase(CricketUtil.YES)) {
											ballsSinceLastBoundary = true;
										} else {
											data_int.set(1, data_int.get(1) + 1);
										}
										break;
								}
							}
						}
				}
				
					
		 /******************************************  Both inning dot balls*********************************************************/
				
				switch (match.getEventFile().getEvents().get(i).getEventType()) {
				case CricketUtil.DOT: case CricketUtil.LOG_WICKET:
					if(match.getEventFile().getEvents().get(i).getEventInningNumber() == 1) {
						firstInnDotBalls += 1;
					} else if(match.getEventFile().getEvents().get(i).getEventInningNumber() == 2) {
						secondInnDotBalls += 1;
					}
					break;	
				}data_str.set(0,String.valueOf(firstInnDotBalls));
				data_str.set(1,String.valueOf(secondInnDotBalls));
				
				
					
		/************************************** PreOtherRunWicket ***********************************************************/

				if(match.getEventFile().getEvents().get(i).getEventInningNumber() 
						== match.getMatch().getInning().stream().filter(in -> in.getIsCurrentInning()
						.equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null).getInningNumber()) {
					
					this_bowC = match.getMatch().getInning().stream().filter(in -> in.getIsCurrentInning()
					        .equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null).getBowlingCard().stream()
					        .filter(bowlingCard -> bowlingCard.getStatus().equalsIgnoreCase(CricketUtil.LAST + CricketUtil.BOWLER)).findAny().orElse(null);
					if(!last_ovr) {
						if(this_bowC!=null) {
							if (this_bowC.getPlayerId() == match.getEventFile().getEvents().get(i).getEventBowlerNo()) {
						        switch (match.getEventFile().getEvents().get(i).getEventType()) {
						           
						            case CricketUtil.ONE: case CricketUtil.TWO: case CricketUtil.THREE: case CricketUtil.FIVE:
						            case CricketUtil.DOT: case CricketUtil.FOUR: case CricketUtil.SIX:
						                run_count += match.getEventFile().getEvents().get(i).getEventRuns();
						                break;
						            case CricketUtil.WIDE: case CricketUtil.NO_BALL: case CricketUtil.BYE: case CricketUtil.LEG_BYE: case CricketUtil.PENALTY:
						                run_count += match.getEventFile().getEvents().get(i).getEventRuns();
						                break;
						            case CricketUtil.LOG_ANY_BALL:
						                run_count += match.getEventFile().getEvents().get(i).getEventRuns();
						                if (match.getEventFile().getEvents().get(i).getEventExtra() != null) {
						                    run_count += match.getEventFile().getEvents().get(i).getEventExtraRuns();
						                }
						                if (match.getEventFile().getEvents().get(i).getEventSubExtra() != null) {
						                    run_count += match.getEventFile().getEvents().get(i).getEventSubExtraRuns();
						                }if (match.getEventFile().getEvents().get(i).getEventHowOut() != null
										    	&& !match.getEventFile().getEvents().get(i).getEventHowOut().isEmpty()) {
						                	 wicket_count =wicket_count+ 1;
						                }
						                break;
						            case CricketUtil.LOG_WICKET:
						            	if(match.getEventFile().getEvents().get(i).getEventHowOut().equalsIgnoreCase(CricketUtil.RETIRED_HURT)||
									    		  match.getEventFile().getEvents().get(i).getEventHowOut().equalsIgnoreCase(CricketUtil.ABSENT_HURT)||
									    		  match.getEventFile().getEvents().get(i).getEventHowOut().equalsIgnoreCase(CricketUtil.CONCUSSED)) {
									    	  continue;
						            	}else {
								    		wicket_count =wicket_count+ 1;
								    	}
						                break;
						        }
								data_str.set(3, String.valueOf(run_count) + "-" + String.valueOf(wicket_count));
						    }else if(match.getEventFile().getEvents().get(i).getEventBowlerNo() != this_bowC.getPlayerId()
									&& !match.getEventFile().getEvents().get(i).getEventType().equalsIgnoreCase(CricketUtil.NEW_BATSMAN)
									&& !match.getEventFile().getEvents().get(i).getEventType().contains(CricketUtil.SWAP_BATSMAN)
									&& !match.getEventFile().getEvents().get(i).getEventType().equalsIgnoreCase(CricketUtil.LOG_REVIEW)
									&& !match.getEventFile().getEvents().get(i).getEventType().contains("LOG_OVERWRITE")){
						    		
								data_str.set(3, String.valueOf(run_count) + "-" + String.valueOf(wicket_count));
									last_ovr = true;
							}
						}else {
							if (data_int.get(0) == match.getEventFile().getEvents().get(i).getEventBowlerNo()) {
						        switch (match.getEventFile().getEvents().get(i).getEventType()) {
						            case CricketUtil.ONE: case CricketUtil.TWO: case CricketUtil.THREE: case CricketUtil.FIVE:
						            case CricketUtil.DOT: case CricketUtil.FOUR: case CricketUtil.SIX:
						                run_count += match.getEventFile().getEvents().get(i).getEventRuns();
						                break;
						            case CricketUtil.WIDE: case CricketUtil.NO_BALL: case CricketUtil.BYE: case CricketUtil.LEG_BYE: case CricketUtil.PENALTY:
						                run_count += match.getEventFile().getEvents().get(i).getEventRuns();
						                break;
						            case CricketUtil.LOG_ANY_BALL:
						                run_count += match.getEventFile().getEvents().get(i).getEventRuns();
						                if (match.getEventFile().getEvents().get(i).getEventExtra() != null) {
						                    run_count += match.getEventFile().getEvents().get(i).getEventExtraRuns();
						                }
						                if (match.getEventFile().getEvents().get(i).getEventSubExtra() != null) {
						                    run_count += match.getEventFile().getEvents().get(i).getEventSubExtraRuns();
						                }if (match.getEventFile().getEvents().get(i).getEventHowOut() != null
										    	&& !match.getEventFile().getEvents().get(i).getEventHowOut().isEmpty()) {
						                	 wicket_count =wicket_count+ 1;
						                }
						                break;
						            case CricketUtil.LOG_WICKET:
						            	if(match.getEventFile().getEvents().get(i).getEventHowOut().equalsIgnoreCase(CricketUtil.RETIRED_HURT)||
									    		  match.getEventFile().getEvents().get(i).getEventHowOut().equalsIgnoreCase(CricketUtil.ABSENT_HURT)||
									    		  match.getEventFile().getEvents().get(i).getEventHowOut().equalsIgnoreCase(CricketUtil.CONCUSSED)) {
									    	  continue;
						            	}else {
								    		wicket_count =wicket_count+ 1;
								    	}
						                break;
						        }
						    }else if(match.getEventFile().getEvents().get(i).getEventBowlerNo() != data_int.get(0) && data_int.get(0) != 0
									&& !match.getEventFile().getEvents().get(i).getEventType().equalsIgnoreCase(CricketUtil.NEW_BATSMAN)
									&& !match.getEventFile().getEvents().get(i).getEventType().contains(CricketUtil.SWAP_BATSMAN)
									&& !match.getEventFile().getEvents().get(i).getEventType().equalsIgnoreCase(CricketUtil.LOG_REVIEW)
									&& !match.getEventFile().getEvents().get(i).getEventType().contains("LOG_OVERWRITE")){
						    		
								data_str.set(3, String.valueOf(run_count) + "-" + String.valueOf(wicket_count));
									last_ovr = true;
							}
						}
					}
					data_str.set(3, String.valueOf(run_count) + "-" + String.valueOf(wicket_count));
				}
				
		/*********************************************************** This over  ***********************************************************************/							

			if (inn.getBowlingCard() != null) {
				
				this_bowC = inn.getBowlingCard().stream().filter(bowlingCard ->bowlingCard.getStatus().equalsIgnoreCase(CricketUtil.CURRENT + CricketUtil.BOWLER) ||
			                    bowlingCard.getStatus().equalsIgnoreCase(CricketUtil.LAST + CricketUtil.BOWLER)).findAny().orElse(null);
				if(this_bowler == false && this_bowC !=null) {
					if((match.getEventFile().getEvents().get(i).getEventBowlerNo() == this_bowC.getPlayerId())) {
						switch (match.getEventFile().getEvents().get(i).getEventType())
						 {
						    case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE:  case CricketUtil.FIVE : case CricketUtil.DOT:
						    case CricketUtil.FOUR: case CricketUtil.SIX: case CricketUtil.NINE:
						    	this_ovr_run=this_ovr_run+match.getEventFile().getEvents().get(i).getEventRuns();
						      data_str.set(4, data_str.get(4) + String.valueOf(match.getEventFile().getEvents().get(i).getEventRuns() + " | "));
						      break;
						    case CricketUtil.NO_BALL:
						    	this_ovr_run=this_ovr_run+match.getEventFile().getEvents().get(i).getEventRuns();
						    	
						    		 data_str.set(4, data_str.get(4) + match.getEventFile().getEvents().get(i).getEventType() + " | ");
						    		 break;
						    case CricketUtil.BYE: case CricketUtil.LEG_BYE:
						    	
						    	this_ovr_run=this_ovr_run+match.getEventFile().getEvents().get(i).getEventRuns();
						    	
						    	if(match.getEventFile().getEvents().get(i).getEventRuns() > 1) {
							    	
						    		data_str.set(4, data_str.get(4) + String.valueOf(match.getEventFile().getEvents().get(i).getEventRuns() +
								    		  + match.getEventFile().getEvents().get(i).getEventSubExtraRuns()) + match.getEventFile().getEvents().get(i).getEventType() + " | ");
						    	}else {
						    		data_str.set(4, data_str.get(4) + match.getEventFile().getEvents().get(i).getEventType() + " | ");
						    	}
						    		 
						    	break;		 
						    case CricketUtil.WIDE:
						    	
						    	this_ovr_run=this_ovr_run+match.getEventFile().getEvents().get(i).getEventRuns();
						    	
						    		if(match.getEventFile().getEvents().get(i).getEventSubExtra() != null) {
						    			data_str.set(4, data_str.get(4) + match.getEventFile().getEvents().get(i).getEventType() + "+" + match.getEventFile().getEvents().get(i).getEventSubExtraRuns() + " | ");
						    		}else {
						    			data_str.set(4, data_str.get(4) + match.getEventFile().getEvents().get(i).getEventType() + " | ");
						    		}
						    		 
						    		 break;
						    case CricketUtil.PENALTY:
						    	this_ovr_run=this_ovr_run+match.getEventFile().getEvents().get(i).getEventRuns();

					    		 data_str.set(4, data_str.get(4) + "P" + String.valueOf(match.getEventFile().getEvents().get(i).getEventRuns() +
							    		  + match.getEventFile().getEvents().get(i).getEventSubExtraRuns()) + " | ");
					    		 break;		 
						    case CricketUtil.LOG_ANY_BALL:
						    	System.out.println(Integer.valueOf(match.getEventFile().getEvents().get(i).getEventRuns() + match.getEventFile().getEvents().get(i).getEventExtraRuns()
					    		  + match.getEventFile().getEvents().get(i).getEventSubExtraRuns()));
						    	
						    	this_ovr_run=this_ovr_run + match.getEventFile().getEvents().get(i).getEventRuns() + match.getEventFile().getEvents().get(i).getEventExtraRuns()
					    		  + match.getEventFile().getEvents().get(i).getEventSubExtraRuns();
						    	
						    	if (match.getEventFile().getEvents().get(i).getEventHowOut() != null
						    	&& !match.getEventFile().getEvents().get(i).getEventHowOut().isEmpty()) {
						    		if(match.getEventFile().getEvents().get(i).getEventExtra()!=null
						    		&& !match.getEventFile().getEvents().get(i).getEventExtra().isEmpty()) {
						    			data_str.set(4, data_str.get(4) + "w+");
						    		}else {
						    			data_str.set(4, data_str.get(4) + "w ");
						    		}
						    		
						    	}
						    	 if(match.getEventFile().getEvents().get(i).getEventSubExtra().equalsIgnoreCase(CricketUtil.BYE) || 
						    			match.getEventFile().getEvents().get(i).getEventSubExtra().equalsIgnoreCase(CricketUtil.LEG_BYE)) {
						    		 
						    		 if(!match.getEventFile().getEvents().get(i).getEventExtra().equalsIgnoreCase(CricketUtil.NO_BALL)) {
						    			 if(match.getEventFile().getEvents().get(i).getEventRuns()+ match.getEventFile().getEvents().get(i).getEventSubExtraRuns()>0) {
							    			 data_str.set(4, data_str.get(4) + match.getEventFile().getEvents().get(i).getEventSubExtra() + "+" + String.valueOf(match.getEventFile().getEvents().get(i).getEventRuns()
										    		  + match.getEventFile().getEvents().get(i).getEventSubExtraRuns())  + " | ");
							    		 }else {
							    			 data_str.set(4, data_str.get(4) +match.getEventFile().getEvents().get(i).getEventSubExtra() + " | "); 
							    		 }
						    		 }else {
						    			 if(match.getEventFile().getEvents().get(i).getEventRuns()+ match.getEventFile().getEvents().get(i).getEventSubExtraRuns()>0) {
							    			 data_str.set(4, data_str.get(4) + match.getEventFile().getEvents().get(i).getEventExtra() + "+" + String.valueOf(match.getEventFile().getEvents().get(i).getEventRuns()
										    		  + match.getEventFile().getEvents().get(i).getEventSubExtraRuns())  + " | ");
							    		 }else {
							    			 data_str.set(4, data_str.get(4) +match.getEventFile().getEvents().get(i).getEventSubExtra() + " | "); 
							    		 }
						    		 }
						    		 
						    		
						    	}else{
						    		if(match.getEventFile().getEvents().get(i).getEventRuns()+ match.getEventFile().getEvents().get(i).getEventSubExtraRuns()>0) {
						    			if(match.getEventFile().getEvents().get(i).getEventSubExtra().equalsIgnoreCase(CricketUtil.PENALTY)) {
						    				data_str.set(4, data_str.get(4) + "P" + String.valueOf(match.getEventFile().getEvents().get(i).getEventRuns()
										    		  + match.getEventFile().getEvents().get(i).getEventSubExtraRuns()) + " | ");
						    			}else if(match.getEventFile().getEvents().get(i).getEventExtra().equalsIgnoreCase(CricketUtil.WIDE) 
						    					&& match.getEventFile().getEvents().get(i).getEventSubExtra().equalsIgnoreCase(CricketUtil.WIDE)){
						    				data_str.set(4, data_str.get(4) + String.valueOf(match.getEventFile().getEvents().get(i).getEventExtraRuns()
										    		  + match.getEventFile().getEvents().get(i).getEventSubExtraRuns()) + "wd" + " | ");
						    			}else {
						    				data_str.set(4, data_str.get(4) + match.getEventFile().getEvents().get(i).getEventExtra() + "+" + String.valueOf(match.getEventFile().getEvents().get(i).getEventRuns()
										    		  + match.getEventFile().getEvents().get(i).getEventSubExtraRuns()) + " | ");
						    			}
							    		
						    		}else {
							    		data_str.set(4, data_str.get(4) + match.getEventFile().getEvents().get(i).getEventExtra()+ " | ");
						    		}
						    		
						    		 
						    	}
						      	break;
						    case CricketUtil.LOG_WICKET: 
						    	if(match.getEventFile().getEvents().get(i).getEventHowOut().equalsIgnoreCase(CricketUtil.RETIRED_HURT)||
						    			match.getEventFile().getEvents().get(i).getEventHowOut().equalsIgnoreCase(CricketUtil.ABSENT_HURT)||
						    			match.getEventFile().getEvents().get(i).getEventHowOut().equalsIgnoreCase(CricketUtil.CONCUSSED)) {
						    		continue;
						    	}else {
						    		this_ovr_wk=this_ovr_wk+1;
						    		 if (match.getEventFile().getEvents().get(i).getEventRuns() > 0) {
						    			 this_ovr_run=this_ovr_run+match.getEventFile().getEvents().get(i).getEventRuns() + match.getEventFile().getEvents().get(i).getEventExtraRuns()
									    		  + match.getEventFile().getEvents().get(i).getEventSubExtraRuns();
								    	  data_str.set(4, data_str.get(4) + String.valueOf(match.getEventFile().getEvents().get(i).getEventRuns() + match.getEventFile().getEvents().get(i).getEventExtraRuns()
									    		  + match.getEventFile().getEvents().get(i).getEventSubExtraRuns()) + match.getEventFile().getEvents().get(i).getEventType() + " | ");
								      } else {
								    	  data_str.set(4, data_str.get(4) + match.getEventFile().getEvents().get(i).getEventType() + " | ");
								      }
						    	}	
						    	
						    	
					    		 break;
						 }
						
					}else if(match.getEventFile().getEvents().get(i).getEventBowlerNo() != this_bowC.getPlayerId()
							&& !match.getEventFile().getEvents().get(i).getEventType().equalsIgnoreCase(CricketUtil.NEW_BATSMAN)
							&& !match.getEventFile().getEvents().get(i).getEventType().contains("LOG_REVIEW")
							&& !match.getEventFile().getEvents().get(i).getEventType().contains(CricketUtil.RESULT)
							&& !match.getEventFile().getEvents().get(i).getEventType().contains(CricketUtil.SWAP_BATSMAN)
							&& !match.getEventFile().getEvents().get(i).getEventType().contains("LOG_OVERWRITE")){
						this_bowler = true;
					}
				}
			}
			

		  }
		}
		data_int.set(2, this_ovr_run);
		data_int.set(3, this_ovr_wk);
		if(match.getMatch().getInning().get(0).getFirstPowerplayEndOver()>1) {
			data_str.set(6, CricketFunctions.getFirstPowerPlayScore(match, 1, match.getEventFile().getEvents()).split(",")[0]);	
		}
		if(match.getMatch().getInning().get(0).getSecondPowerplayStartOver()>1) {
			data_str.set(7,CricketFunctions.getSecPowerPlayScore(match, 1, match.getEventFile().getEvents()).split(",")[0]);
		}
		if(match.getMatch().getInning().get(0).getThirdPowerplayStartOver()>1) {
			data_str.set(8, CricketFunctions.getThirdPowerPlayScore(match, 1, match.getEventFile().getEvents()).split(",")[0]);
		}
		if(match.getMatch().getInning().get(1).getFirstPowerplayEndOver()>1) {
			data_str.set(9, CricketFunctions.getFirstPowerPlayScore(match, 2, match.getEventFile().getEvents()).split(",")[0]);
		}
		if(match.getMatch().getInning().get(1).getSecondPowerplayStartOver()>1) {
			data_str.set(10,CricketFunctions.getSecPowerPlayScore(match, 2, match.getEventFile().getEvents()).split(",")[0]);
		}
		if(match.getMatch().getInning().get(1).getThirdPowerplayStartOver()>1) {
			data_str.set(11,CricketFunctions.getThirdPowerPlayScore(match, 2, match.getEventFile().getEvents()).split(",")[0]);
		}
		
		
	}
	public static String replaceTermsInString(String input) {
	    if (input != null && !input.isEmpty() && (input.contains("WIDE") || input.contains("NO_BALL") ||
	            input.contains("LEG_BYE") || input.contains("BYE") || input.contains("PENALTY") ||
	            input.contains("LOG_WICKET") || input.contains("WICKET"))) {
	        input = input.replace("WIDE", "wd")
	                .replace("NO_BALL", "nb")
	                .replace("LEG_BYE", "lb")
	                .replace("BYE", "b")
	                .replace("PENALTY", "pn")
	                .replace("LOG_WICKET", "w")
	                .replace("WICKET", "w");
	    }
	    return input;
	}

	public static String reverseStringWithPreservation(String input) {
        String[] parts = input.split("\\s*\\|\\s*"); // Split the string by "|"
        StringBuilder reversedString = new StringBuilder();

        for (int i = parts.length - 1; i >= 0; i--) {
            reversedString.append(parts[i]);
            if (i > 0) {
                reversedString.append(" | "); // Add back the "|" separator
            }
        }

        return reversedString.toString();
    }
	public static void playAudio(String audioFilePath) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		if(new File(audioFilePath).exists()) {
			 File audioFile = new File(audioFilePath);
			 AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile);
			 Clip clip = AudioSystem.getClip();
			 clip.open(audioInputStream);
			 clip.start();
			 while(clip.isRunning()) {
				 try {
					 Thread.sleep(100);
				 }catch(Exception e) {
					 e.printStackTrace();
				 }
			 }
		}
	}
}