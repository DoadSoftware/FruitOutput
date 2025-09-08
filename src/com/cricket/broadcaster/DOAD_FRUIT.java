package com.cricket.broadcaster;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.poi.ss.formula.functions.Substitute;

import com.cricket.model.BattingCard;
import com.cricket.model.BowlingCard;
import com.cricket.model.Configuration;
import com.cricket.model.DaySession;
import com.cricket.model.Player;
import com.cricket.model.Review;
import com.cricket.model.Setup;
import com.cricket.model.Speed;
import com.cricket.model.Team;
import com.cricket.service.CricketService;
import com.cricket.model.Inning;
import com.cricket.model.MatchAllData;
import com.cricket.model.MatchStats;
import com.cricket.containers.Scene;
import com.cricket.util.CricketFunctions;
import com.cricket.util.CricketUtil;

import net.ucanaccess.jdbc.Session;

public class DOAD_FRUIT extends Scene{

	public String session_selected_broadcaster = "DOAD_FRUIT";
	public String which_graphics_onscreen = "",Summary="";
	public String icon_path = "C:\\EVEREST_FRUIT\\Icons\\";
	boolean player_found = false;
	int previousOver=-1,previousBall=-1;
	int cPlayer =0;
	String director_Type = "";
	int batPlayerNum=1;
	boolean played = false;
	int this_over =0;
	List<String>str=new ArrayList<String>();
	
	public DOAD_FRUIT() {
		super();
	}
	
	public DOAD_FRUIT(String scene_path, String which_Layer) {
		super(scene_path, which_Layer);
	}

	public void updateFruit(Scene scene, MatchAllData match,PrintWriter print_writer,Configuration config) throws Exception
	{
		populateFruit(match, print_writer,config);

	}
	
	public Object ProcessGraphicOption(String whatToProcess, MatchAllData match, CricketService cricketService,
			PrintWriter print_writer, List<Scene> scenes, String valueToProcess,Configuration config) throws Exception{
			
			switch (whatToProcess.toUpperCase()) {
			case "POPULATE-FRUIT": case "POPULATE-TEAM":case "POPULATE-LOGO":
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tClientLogoS "+
		    			config.getSelect_Client() + ";");
        		if(which_graphics_onscreen.trim().isEmpty()) {
				  scenes.get(0).setScene_path(valueToProcess);
				  scenes.get(0).scene_load(print_writer,session_selected_broadcaster);
        		}
        		switch (whatToProcess.toUpperCase()) {
	    			case "POPULATE-FRUIT":
	    				showSpeedAndReview(print_writer, config);
	    				initialize_fruit(CricketFunctions.processPrintWriter(config).get(0), match,config);
	    				populateFruit(match, print_writer,config);
	    				break;
	    			case "POPULATE-TEAM":
	    				populateTeam(match, print_writer,config);
    				break;
        		}
			
			 break;
			case "ANIMATE-IN-FRUIT":case"ANIMATE-IN-TEAM": case "ANIMATE-IN-LOGO":case "ANIMATE-OUT": case "CLEAR-ALL": 
				switch (session_selected_broadcaster.toUpperCase()) {
				case "DOAD_FRUIT":
					switch (whatToProcess.toUpperCase()) {
					case "ANIMATE-IN-FRUIT":
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectPage 2;");			
						if(which_graphics_onscreen.isEmpty()) {
							processAnimation(print_writer, "In", "START", session_selected_broadcaster,1);
						}
						which_graphics_onscreen = "FRUIT";
						break;
					case"ANIMATE-IN-TEAM":
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectPage 1;");
						if(which_graphics_onscreen.isEmpty()) {
							processAnimation(print_writer, "In", "START", session_selected_broadcaster,1);
						}
						which_graphics_onscreen = "FRUIT";
						break;
					case "ANIMATE-IN-LOGO":
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectPage 0;");
						if(which_graphics_onscreen.isEmpty()) {
							processAnimation(print_writer, "In", "START", session_selected_broadcaster,1);
						}
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
	private void populateTeam(MatchAllData match, PrintWriter PrintWriter, Configuration config) throws InterruptedException {
	    Setup setup = match.getSetup();

	    PrintWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTounamentName " 
	               + setup.getTournament().toUpperCase() + ";");
	    PrintWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tMatchNumber " 
	               + setup.getMatchIdent() + " : " 
	               + setup.getHomeTeam().getTeamName4() + " vs " 
	               + setup.getAwayTeam().getTeamName4() + ";");
	    PrintWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectTeamStyle 0;");
	    PrintWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTossResult " + CricketFunctions.generateTossResult(match, 
							CricketUtil.FULL, CricketUtil.FIELD, CricketUtil.FULL, CricketUtil.CHOSE).toUpperCase() + ";");

	    setTeamHeaders(PrintWriter, setup.getHomeTeam().getTeamName1(), setup.getAwayTeam().getTeamName1());

	    for (int k = 1; k <= 5; k++) clearSubstitute(PrintWriter, k);

	    SetPlayers(PrintWriter, setup.getHomeSquad(), setup.getHomeSubstitutes(), "Home","vHomeSubPlayerGrp");
	    SetPlayers(PrintWriter, setup.getAwaySquad(), setup.getAwaySubstitutes(), "Away","vAwaySubPlayerGrp");
	}

	private void setTeamHeaders(PrintWriter PrintWriter, String home, String away) {
		PrintWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tMainHomeTeamName " 
	               + home + ";");
		PrintWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tMainAwayTeamName " 
	               + away + ";");
		PrintWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tMainHomeTeamHeader PLAYING XI ;");
		PrintWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tMainAwayTeamHeader PLAYING XI ;");
		PrintWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tMainHomeSubHeader SUBSTITUTES;");
		PrintWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tMainAwaySubHeader SUBSTITUTES;");
	}

	private void clearSubstitute(PrintWriter PrintWriter, int i) {
		PrintWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tMainHomeSubPlayer" + i + " ;");
		PrintWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tMainAwaySubPlayer" + i + " ;");
		PrintWriter.println("LAYER1*EVEREST*TREEVIEW*Main$All$Slect_Page$TeamPage$TeamsGrp$Style2"
	               + "$HomeGrp$HomeSubPlayerGrp$PlayerGrp" + i + "*CONTAINER SET ACTIVE 0;");
		PrintWriter.println("LAYER1*EVEREST*TREEVIEW*Main$All$Slect_Page$TeamPage$TeamsGrp$Style2"
	               + "$AwayGrp$AwaySubPlayerGrp$PlayerGrp" + i + "*CONTAINER SET ACTIVE 0;");
	}

	private void SetPlayers(PrintWriter PrintWriter, List<Player> squad, List<Player> subs, String teamPrefix,String select_tag) throws InterruptedException {
	    for (int i = 0; i < squad.size(); i++) {
	        PrintWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tMain" 
	              + teamPrefix + "Player" + (i + 1) + " " + squad.get(i).getFull_name() + ";");
	        PrintWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tMain" 
	              + teamPrefix + "Role" + (i + 1) + " "  + squad.get(i).getRole().replace("-", "").toUpperCase() + ";");
	        PrintWriter.println("LAYER1*EVEREST*TREEVIEW*Main$All$Slect_Page$TeamPage$TeamsGrp$Style2"
		              + "$" + teamPrefix + "Grp$" + teamPrefix + "PlayerGrp$PlayerGrp" + (i + 1)+"$IconGrp$IconBase" + "*CONTAINER SET ACTIVE 0 ;");
	        writeCaptainTag(PrintWriter, squad.get(i), teamPrefix, (i + 1));
	        setPlayerIcon(PrintWriter, squad.get(i), "lgMain" + teamPrefix + "Role" + (i + 1));
	        Thread.sleep(5);
	    }
	    if(subs != null && !subs.isEmpty()) {
	    	for (int i = 0; i < subs.size(); i++) {
		        PrintWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tMain" 
		              + teamPrefix + "SubPlayer" + (i + 1) + " " + subs.get(i).getFull_name() + ";");
		        setPlayerIcon(PrintWriter, subs.get(i), "lgMain" + teamPrefix + "SubRole" + (i + 1));
		        PrintWriter.println("LAYER1*EVEREST*TREEVIEW*Main$All$Slect_Page$TeamPage$TeamsGrp$Style2"
			              + "$" + teamPrefix + "Grp$" + teamPrefix + "SubPlayerGrp$PlayerGrp" + (i + 1)+"$IconGrp$IconBase" + "*CONTAINER SET ACTIVE 0 ;");
		        PrintWriter.println("LAYER1*EVEREST*TREEVIEW*Main$All$Slect_Page$TeamPage$TeamsGrp$Style2"
		              + "$" + teamPrefix + "Grp$" + teamPrefix + "SubPlayerGrp$PlayerGrp" + (i + 1) + "*CONTAINER SET ACTIVE 1 ;");
		    }
		    PrintWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET "+select_tag+" "+ subs.size() + ";");
	    }
	}

	private void writeCaptainTag(PrintWriter PrintWriter, Player ply, String tp, int idx) {
	    String role = ply.getCaptainWicketKeeper();
	    String suffix = "";
	    if (CricketUtil.CAPTAIN.equalsIgnoreCase(role)) suffix = " (C)";
	    else if ("CAPTAIN_WICKET_KEEPER".equalsIgnoreCase(role)) suffix = " (C & WK)";
	    else if (CricketUtil.WICKET_KEEPER.equalsIgnoreCase(role)) suffix = " (WK)";
	    PrintWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tMain" 
	               + tp + "Captain" + idx + suffix + ";");
	}

	private void setPlayerIcon(PrintWriter PrintWriter, Player ply, String tag) {
	    String icon = "";
	    String role = ply.getRole().toUpperCase();
	    String bat = ply.getBattingStyle(), bowl = ply.getBowlingStyle();

	    if (role.contains("BAT") && !role.contains("BOWL") || role.contains("KEEPER")) {
	        icon = (bat.equalsIgnoreCase("LHB") ? "LeftHandBatsman" : "Batsman");
	    } else if (role.contains("ALL")) {
	        icon = getBowlerIcon(bowl, true);
	    } else if (role.contains("BOWL")) {
	        icon = getBowlerIcon(bowl, false);
	    }

	    if (!icon.isEmpty()) {
	    	PrintWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET " 
	                   + tag + " " + icon_path + icon + CricketUtil.PNG_EXTENSION + ";");
	    }
	}

	private String getBowlerIcon(String style, boolean allround) {
	    if (style == null) return allround ? "FastBowlerAllrounder" : "FastBowler";
	    switch (style) {
	        case "RF": case "RFM": case "RMF": case "RM": case "RSM":
	        case "LF": case "LFM": case "LMF": case "LM":
	            return allround ? "FastBowlerAllrounder" : "FastBowler";
	        case "ROB": case "RLB": case "LSL": case "WSL":
	        case "LCH": case "RLG": case "WSR": case "LSO":
	            return allround ? "SpinBowlerAllrounder" : "SpinBowlerIcon";
	        default:
	            return allround ? "FastBowlerAllrounder" : "FastBowler";
	    }
	}

	public void populateInfobar(PrintWriter print_writer,String viz_scene, MatchAllData match, String session_selected_broadcaster,Configuration config) throws Exception 
	{
		populateFruit(match, print_writer,config);
	}
	public void populateFruit( MatchAllData match,PrintWriter print_writer,Configuration config) throws Exception {
     		BattingCard this_bc;
     		
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomeStatValue1 " + match.getMatch().getInning().get(0).getTotalFours() + ";");
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomeStatValue2 " + match.getMatch().getInning().get(0).getTotalSixes() + ";");

/********************************************************** EVENTS  **************************************************************/
		MatchStats  Stats = CricketFunctions.getAllEvents(match, config.getBroadcaster(), match.getEventFile().getEvents());
/*********************************************************************************************************************************/
		
		for(Inning inn : match.getMatch().getInning()) {
			if (inn.getIsCurrentInning().toUpperCase().equalsIgnoreCase(CricketUtil.YES)) {
				
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomeTeamName " + 
						inn.getBatting_team().getTeamBadge() + ";");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tAwayTeamName " + 
						inn.getBowling_team().getTeamBadge() + ";");
/****************************************** Powerplay ***************************************************************/
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tDots D;");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tDotsMaiden DOTS;");
				if(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.TEST)||
						match.getSetup().getMatchType().equalsIgnoreCase("FC")) {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tDots M;");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tDotsMaiden MAIDENS;");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBallSince 1;");
					if(inn.getInningNumber()==1) {
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSmallFreeText2 " + inn.getBatting_team().getTeamName4() +" "+
								" LEAD BY "+inn.getTotalRuns()+ " RUN"+CricketFunctions.Plural(inn.getTotalRuns()).toUpperCase() +";");
					}else {
						Team team = (match.getMatch().getMatchStatus().contains(match.getSetup().getHomeTeam().getTeamName1())?
								match.getSetup().getHomeTeam():match.getSetup().getAwayTeam());
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSmallFreeText2 " + team.getTeamBadge() +" "+
								match.getMatch().getMatchStatus().replace(team.getTeamName1(),"").toUpperCase() + ";");
					}
					
					 TestMatch(print_writer, match);
					if(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.TEST)||
							match.getSetup().getMatchType().equalsIgnoreCase("FC")) {
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
	    			if(CricketFunctions.getBallCountStartAndEndRange(match, inn).get(1) > (inn.getTotalOvers()*6) && 
	    					CricketFunctions.getBallCountStartAndEndRange(match, inn).get(1)>1) {
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
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBattingTeamName " + inn.getBatting_team().getTeamBadge().toUpperCase() + ";");
				if(inn.getTotalWickets() >= 10) {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTotalScore " + inn.getTotalRuns() + ";");
				}
				else{
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTotalScore " + inn.getTotalRuns() + "-" + inn.getTotalWickets() + ";");
				}
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tOvers " + CricketFunctions.OverBalls(inn.getTotalOvers(), inn.getTotalBalls())+ ";");
				//print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tOvers " + Balls(inn.getTotalOvers(), inn.getTotalBalls())+ ";");
				
				if(inn.getTotalPenalties() > 0) {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectPenalty " + "1" + ";");
					
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main$All$SplitGrp$PenaltyGrp$SelectPenalty*CONTAINER SET ACTIVE 1 ;");

					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPenaltyValue " + inn.getTotalPenalties() + ";");
				}else {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectPenalty " + "0" + ";");
				}

				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tExtras " + "EXTRAS: " + inn.getTotalExtras() + ";");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tWideValue " + inn.getTotalWides() + ";");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tNoBallValue " + inn.getTotalNoBalls() + ";");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tByeValue " + inn.getTotalByes() + ";");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tLegByelValue " + inn.getTotalLegByes() + ";");
/************************************************  comparision  ********************************************************************  */
				
				if(!match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.TEST) && !match.getSetup().getMatchType().equalsIgnoreCase("FC")) {
					if(inn.getInningNumber() == 1) {
						if(match.getSetup().getTargetType()!=null && !match.getSetup().getTargetType().isEmpty()) {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tRevisedOvers (" +match.getSetup().getReducedOvers()+");");	
						}else {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tRevisedOvers ;");
						}
			
//---------------------------------------------------------------- TOSS ---------------------------------------------------------------------------------------------------------------						
			            print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectDataType 0;");
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET TossResult1 CHOSE TO " +  match.getSetup().getTossWinningDecision().toUpperCase() + ";");
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET TossTeam1 " +(match.getSetup().getTossWinningTeam() == match.getSetup().getHomeTeamId() ? 
								match.getSetup().getHomeTeam().getTeamName1() : match.getSetup().getAwayTeam().getTeamName1()).toUpperCase()  + ";");
						
						
//----------------------------------------------------------------StatComparison---------------------------------------------------------------------------------------------------------------						
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tComparisonHomeTeamName " + match.getMatch().getInning().get(0).getBatting_team().getTeamBadge()
								+"    (" + CricketFunctions.getTeamScore(match.getMatch().getInning().get(0), "-", false) + ")" + ";");
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tComparisonAwayTeamName " + match.getMatch().getInning().get(1).getBatting_team().getTeamBadge() +
								"     (" + CricketFunctions.getTeamScore(match.getMatch().getInning().get(1), "-", false)+ ")" + ";");
							
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tAwayStatValue1 " + "0" + ";");
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tAwayStatValue2 " + "0" + ";");
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomeStatValue3 " + "-" + ";");
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tAwayStatValue3 " + "-" + ";");
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET AwayStatValue4 " + "0" + ";");

					}else if(inn.getInningNumber() == 2) {
						if(match.getSetup().getTargetType()!=null && !match.getSetup().getTargetType().isEmpty()) {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tRevisedOvers (" +match.getSetup().getTargetOvers()+");");	
						}else {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tRevisedOvers ;");
						}
/************************************************  phasewise ********************************************************************  */						
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectDataType 1;");
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeamName1 " + 
								inn.getBowling_team().getTeamName4() + ";");
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeamName2 " + 
								inn.getBatting_team().getTeamName4() + ";");
						
						if(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.IT20)||
							match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.DT20)||
							match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.D10)) {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main$All$InningsDataGrp$PhaseScoreAll$PhaseScoreHead*CONTAINER SET ACTIVE 1 ;");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main$All$InningsDataGrp$PhaseScoreAll$Header*CONTAINER SET ACTIVE 1 ;");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeam1PhaseScore1 " + 
									Stats.getHomeFirstPowerPlay().getTotalRuns()+"-"+Stats.getHomeFirstPowerPlay().getTotalWickets() + ";");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeam1PhaseScore2 " + 
									Stats.getHomeSecondPowerPlay().getTotalRuns()+"-"+Stats.getHomeSecondPowerPlay().getTotalWickets()+ ";");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeam1PhaseScore3 " + 
									Stats.getHomeThirdPowerPlay().getTotalRuns()+"-"+Stats.getHomeThirdPowerPlay().getTotalWickets() + ";");
							
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeam2PhaseScore1 " + 
									Stats.getAwayFirstPowerPlay().getTotalRuns()+"-"+Stats.getAwayFirstPowerPlay().getTotalWickets() + ";");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeam2PhaseScore2 " + 
									Stats.getAwaySecondPowerPlay().getTotalRuns()+"-"+Stats.getAwaySecondPowerPlay().getTotalWickets()+ ";");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeam2PhaseScore3 " + 
									Stats.getAwayThirdPowerPlay().getTotalRuns()+"-"+Stats.getAwayThirdPowerPlay().getTotalWickets() + ";");
						}
						else if(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.ODI)) {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main$All$InningsDataGrp$PhaseScoreAll$PhaseScoreHead*CONTAINER SET ACTIVE 1 ;");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main$All$InningsDataGrp$PhaseScoreAll$Header*CONTAINER SET ACTIVE 1 ;");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeam1PhaseScore1 " + 
									Stats.getHomeFirstPowerPlay().getTotalRuns()+"-"+Stats.getHomeFirstPowerPlay().getTotalWickets() + ";");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeam1PhaseScore2 " + 
									Stats.getHomeSecondPowerPlay().getTotalRuns()+"-"+Stats.getHomeSecondPowerPlay().getTotalWickets()+ ";");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeam1PhaseScore3 " + 
									Stats.getHomeThirdPowerPlay().getTotalRuns()+"-"+Stats.getHomeThirdPowerPlay().getTotalWickets() + ";");
							
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeam2PhaseScore1 " + 
									Stats.getAwayFirstPowerPlay().getTotalRuns()+"-"+Stats.getAwayFirstPowerPlay().getTotalWickets() + ";");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeam2PhaseScore2 " + 
									Stats.getAwaySecondPowerPlay().getTotalRuns()+"-"+Stats.getAwaySecondPowerPlay().getTotalWickets()+ ";");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeam2PhaseScore3 " + 
									Stats.getAwayThirdPowerPlay().getTotalRuns()+"-"+Stats.getAwayThirdPowerPlay().getTotalWickets() + ";");	
							
							}else {
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeamName2 ;");
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeamName1 ;");
							  	print_writer.println("LAYER1*EVEREST*TREEVIEW*Main$All$InningsDataGrp$PhaseScoreAll$PhaseScoreHead*CONTAINER SET ACTIVE 0 ;");
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main$All$InningsDataGrp$PhaseScoreAll$Header*CONTAINER SET ACTIVE 0 ;");
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeam2PhaseScore1 ;");
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeam2PhaseScore2 ;");
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeam2PhaseScore3 ;");
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeam1PhaseScore1 ;");
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeam1PhaseScore2 ;");
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeam1PhaseScore3 ;");

						  }
//----------------------------------------------------------------StatComparison---------------------------------------------------------------------------------------------------------------						
					
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tComparisonHomeTeamName " + match.getMatch().getInning().get(0).getBatting_team().getTeamBadge()
								+"    (" + CricketFunctions.getTeamScore(match.getMatch().getInning().get(0), "-", false) + ")" + ";");
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tComparisonAwayTeamName " + match.getMatch().getInning().get(1).getBatting_team().getTeamBadge() +
								"     (" + CricketFunctions.getTeamScore(match.getMatch().getInning().get(1), "-", false)+ ")" + ";");
						
						if(config.getShowReview().equalsIgnoreCase("WITH")) {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET 1 " + "4" + ";");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main$All$Slect_Page$REWORK$StatComparisonGrp$StatGrp$StatHeadGrp"
									+ "*FUNCTION_SET_PROP*GRID_ARRANGE distRow=50.00;");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main$All$Slect_Page$REWORK$StatComparisonGrp$StatGrp$HomeStatValueGrp"
									+ "*FUNCTION_SET_PROP*GRID_ARRANGE distRow=50.00;");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main$All$Slect_Page$REWORK$StatComparisonGrp$StatGrp$AwayStatValueGrp"
									+ "*FUNCTION_SET_PROP*GRID_ARRANGE distRow=50.00;");
						}else {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET 1 " + "3" + ";");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main$All$Slect_Page$REWORK$StatComparisonGrp$StatGrp$StatHeadGrp"
									+ "*FUNCTION_SET_PROP*GRID_ARRANGE distRow=60.00;");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main$All$Slect_Page$REWORK$StatComparisonGrp$StatGrp$HomeStatValueGrp"
									+ "*FUNCTION_SET_PROP*GRID_ARRANGE distRow=60.00;");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main$All$Slect_Page$REWORK$StatComparisonGrp$StatGrp$AwayStatValueGrp"
									+ "*FUNCTION_SET_PROP*GRID_ARRANGE distRow=60.00;");
						}
						
						
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomeStatValue1 " + match.getMatch().getInning().get(0).getTotalFours() + ";");
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomeStatValue2 " + match.getMatch().getInning().get(0).getTotalSixes() + ";");

						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tAwayStatValue1 " + inn.getTotalFours() + ";");
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tAwayStatValue2 " + inn.getTotalSixes() + ";");
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomeStatValue3 " + CricketFunctions.compareInningData(match, "-", 1, match.getEventFile().getEvents()) + ";");
						if(inn.getTotalWickets()==10) {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tAwayStatValue3 " +inn.getTotalRuns()+ ";");
						}else {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tAwayStatValue3 " +inn.getTotalRuns()+" - "+inn.getTotalWickets() + ";");
						}
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET AwayStatValue4 " +Stats.getAwayTeamScoreData().getTotalDots() + ";");
					
					}

					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomeStatValue4 " + Stats.getHomeTeamScoreData().getTotalDots() + ";");	
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBallSince 0;");	
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSmallFreeText1 " + "SINCE LAST B'DRY : " + 
							Stats.getBallsSinceLastBoundary() +" BALL" + CricketFunctions.Plural(Stats.getBallsSinceLastBoundary()).toUpperCase() + ";");
						
				}
/****************************************** Projected And Equation & Result *************************************************************/				
					if(!match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.TEST) && !match.getSetup().getMatchType().equalsIgnoreCase("FC")) {
						
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTossAway " +
								(match.getSetup().getTossWinningTeam() == inn.getBattingTeamId() ? 0 : 1) + ";");
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTossHome " +
								(match.getSetup().getTossWinningTeam() == inn.getBattingTeamId() ? 1 : 0)  + ";");
						
						if(inn.getInningNumber() == 1 ) {
							if(inn.getRunRate() != null ) { 
								str = CricketFunctions.projectedScore(match);			    
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tProEqua " + "0" + ";");
								
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTextc " + 
										"@" +str.get(0) +" (CRR)" + ";");
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tScore1 " + 
										str.get(1) + ";");
								
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTextc1 " + 
										"@" + str.get(2) +" RPO" + ";");
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tScore2 " + 
										str.get(3) + ";");
								
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTextc2 " + 
										"@" + str.get(4) +" RPO" + ";");
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tScore3 " + 
										str.get(5)+ ";");
							}else {
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tProEqua " + "0" + ";");							
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTextc " + 
										"-" + ";");
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tScore1 " + 
										"-" + ";");
								
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTextc1 " + 
										"-" + ";");
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tScore2 " + 
										"-" + ";");
								
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTextc2 " + 
										"-" + ";");
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tScore3 " + 
										"-" + ";");
							}
					}else if(inn.getInningNumber() == 2) {
						int runsNeeded = CricketFunctions.GetTargetData(match).getRemaningRuns();
						int ballsRemaining = CricketFunctions.GetTargetData(match).getRemaningBall();
						if(CricketFunctions.GetTargetData(match).getRemaningRuns() == 0 || match.getMatch().getInning().get(1).getTotalWickets() >= 10 || 
								CricketFunctions.GetTargetData(match).getRemaningBall()  == 0) {
							
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tProEqua " + "2" + ";");
							
							if(match.getMatch().getMatchStatus().contains("Match tied")) {
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTname \n\n\n\n\n\n" + 
										match.getMatch().getMatchStatus().toUpperCase() + ";");
							}else {
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTname \n\n\n" + match.getMatch().getMatchStatus().split("win")[0] 
										+"\n\n\n\n\n\n\n\n" + "WIN " + match.getMatch().getMatchStatus().split("win")[1].toUpperCase() + ";");
							}
							
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main$All$REWORK$Projected\\Equation\\Result$Result$NeedRuns*CONTAINER SET ACTIVE 0;");
						}else {
							
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tProEqua " + "1" + ";");
							
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main$All$Projected\\Equation$Equation$NeedRuns*CONTAINER SET ACTIVE 1 ;");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main$All$Projected\\Equation$Equation$FromBalls*CONTAINER SET ACTIVE 1 ;");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main$All$Projected\\Equation$Equation$RRR*CONTAINER SET ACTIVE 1 ;");
							
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTname " + inn.getBatting_team().getTeamName1() + ";");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tNeedRuns " + runsNeeded + ";");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tNeedBalls " + ballsRemaining + ";");
							if(match.getSetup().getTargetType()!=null && !match.getSetup().getTargetType().isEmpty()) {
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vDls 1;");
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tDlsTxt "+
										match.getSetup().getTargetType().trim().toUpperCase() + ";");
							}else {
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vDls 0;");
							}
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tRRR @ REQUIRED RUN RATE " + 
									CricketFunctions.generateRunRate(CricketFunctions.GetTargetData(match).getRemaningRuns(),0,
											CricketFunctions.GetTargetData(match).getRemaningBall(),2,match) + ";");	
						}
					}
				}	

/******************************************Run rate**********************************/
				
				if(inn.getInningNumber() == 1||inn.getInningNumber() == 3) {
					if(inn.getRunRate() != null) {
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tRunRate " + 
								"CRR : " + inn.getRunRate() + ";");
					}else {
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tRunRate " + 
								"CRR : " + "-" + ";");
					}
					
				}else if(inn.getInningNumber() == 2||inn.getInningNumber() == 4) {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tRunRate " + "RRR: " + CricketFunctions.generateRunRate(CricketFunctions.
							GetTargetData(match).getRemaningRuns(), 0, CricketFunctions.GetTargetData(match).getRemaningBall(), 2,match) + ";");
				}

/******************************************************* FALL OF WICKETS AND LAST WICKET *******************************/
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFowTeamName2 " + 
						inn.getBatting_team().getTeamName4() + ";");
				if(inn.getFallsOfWickets() != null && inn.getFallsOfWickets().size() > 0) {
					 for (int j = 0; j <= inn.getFallsOfWickets().size() -1; j++){
				    	print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeam2Fow" + (j+1) + " " + 
				    			inn.getFallsOfWickets().get(j).getFowRuns() + ";");
				     }	
					this_bc = inn.getBattingCard().stream().filter(batC -> inn.getFallsOfWickets().get(inn.getFallsOfWickets().size() - 1).getFowPlayerID() 
							== batC.getPlayerId()).findAny().orElse(null);
					
					if(this_bc != null) {
						String how_out = this_bc.getPlayer().getTicker_name()+"  "+this_bc.getHowOutText();
			
						if(how_out.length()>31) {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tLastWicketValue " 
									+ this_bc.getPlayer().getTicker_name() +  "    ;");
						}else {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tLastWicketValue " + how_out + ";");
						}
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tLastWicketScore " 
									+" "+ this_bc.getRuns() + ";");
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tLastWicketBalls " 
									+ this_bc.getBalls() + ";");
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tLastWicketHead " + "LAST WICKET:" + ";");
					}
				
				}else {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tLastWicketHead " + "" + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tLastWicketValue " + "" + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tLastWicketScore " + "" + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tLastWicketBalls " + "" + ";");
				}
/**************************************** BATTING CARD  *******************************************************************/
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeamsHeader1 " + inn.getBatting_team().getTeamName1() + ";");
				
			    for (int i = 0; i < inn.getBattingCard().size(); i++) {
			        BattingCard bc = inn.getBattingCard().get(i);
			        	print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomePlayer" + (i + 1) + " " + 
				    			bc.getPlayer().getTicker_name() + ";");
			        	if(inn.getBattingCard().get(i).getStatus().toUpperCase().equalsIgnoreCase(CricketUtil.NOT_OUT)) {	
				            print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vHomeSelectStriker" + (i + 1) + " 1;");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomeHowOut" + (i + 1) + " ;");
						}else {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vHomeSelectStriker" + (i + 1) + " 0;");
							if(bc.getHowOut() != null) {
								switch (bc.getHowOut().toUpperCase()) {
								case CricketUtil.RETIRED_HURT: case CricketUtil.CONCUSSED:
									print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomeHowOut" + (i + 1) + " " 
											+ bc.getHowOut().replace("_", " ") + ";");
									break;
								default:
									print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomeHowOut" + (i + 1) + " " 
											+ (bc.getHowOutText()!=null ? bc.getHowOutText():"") + ";");
									break;
								}
							}else {
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomeHowOut" + (i + 1) + " " 
										+ (bc.getHowOutText()!=null ? bc.getHowOutText():"") + ";");
							}
						}
			            print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectHomePlayer" + (i + 1) + " 1;");
			            print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeamsHeade  ;");
			            if(bc.getBalls()==0 &&bc.getRuns()==0 && bc.getStatus().equalsIgnoreCase(CricketUtil.STILL_TO_BAT)) {
			            	print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomeScore" + (i + 1) + " ;");
				            print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomeBalls" + (i + 1) + " ;");
			            }else {
			            	print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomeScore" + (i + 1) + " " + bc.getRuns() + ";");
				            print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomeBalls" + (i + 1) + " " + bc.getBalls() + ";");	
			            }
			    }

				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectHomePlayerNumber " + 
						(inn.getBattingCard().size()-1) + ";");
				if(inn.getBattingCard().size()==13) {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main$All$Slect_Page$REWORK$TeamsGrp$group$HomePlayerGrp*FUNCTION_SET_PROP*GRID_ARRANGE distRow=35.00;");
				}else if(inn.getBattingCard().size()==12) {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main$All$Slect_Page$REWORK$TeamsGrp$group$HomePlayerGrp*FUNCTION_SET_PROP*GRID_ARRANGE distRow=38.00;");
				} else{
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main$All$Slect_Page$REWORK$TeamsGrp$group$HomePlayerGrp*FUNCTION_SET_PROP*GRID_ARRANGE distRow=41.00;");
				}
/****************************************Current and last bowlers*******************************************************************/
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeamsHeader2 " + inn.getBowling_team().getTeamName1() + ";");
				if(inn.getBowlingCard() != null) {
					int id = 0,last_bolwer=0;
					for(BowlingCard boc : inn.getBowlingCard()) {
						if(boc.getStatus().toUpperCase().equalsIgnoreCase(CricketUtil.CURRENT + CricketUtil.BOWLER)) {
							cPlayer = boc.getPlayerId();
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectBowler2" + " " + "0" + ";");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectBowler1" + " " + "1" + ";");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBowlerName1 " + boc.getPlayer().getTicker_name() + ";");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFigure1 " + boc.getWickets() + "-" + boc.getRuns() + ";");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tOvers1 " + CricketFunctions.OverBalls(boc.getOvers(), boc.getBalls())+ ";");
							if(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.FC)||
									match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.TEST)) {
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tDots1 " +  boc.getMaidens() + ";");

							}else {
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tDots1 " +  boc.getDots() + ";");
							}
							if(boc.getEconomyRate() == ""||boc.getEconomyRate() == null) {
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tEconomy1 " + "-" + ";");
							}else {
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tEconomy1 " +boc.getEconomyRate()+ ";");
							}
						}
						else if(boc.getStatus().toUpperCase().equalsIgnoreCase(CricketUtil.LAST + CricketUtil.BOWLER)) {
							last_bolwer = boc.getPlayerId();
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectBowler1" + " " + "0" + ";");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectBowler2" + " " + "0" + ";");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBowlerName1 " + boc.getPlayer().getTicker_name() + ";");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFigure1 " + boc.getWickets() + "-" + boc.getRuns() + ";");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tOvers1 " + CricketFunctions.OverBalls(boc.getOvers(), boc.getBalls())+ ";");
							if(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.FC)||
									match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.TEST)) {
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tDots1 " +  boc.getMaidens() + ";");

							}else {
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tDots1 " +  boc.getDots() + ";");
							}							
							id= Stats.getBowlingCard().getReplacementBowlerId();
							
							if(boc.getEconomyRate() == ""||boc.getEconomyRate() == null) {
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tEconomy1 " + "-" + ";");
							}else {
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tEconomy1 " +boc.getEconomyRate()+ ";");
							}
						}
						if(id==0) {
							id = Stats.getBowlingCard().getLastBowlerId();
						}
						if(id!=0) {
							if(boc.getPlayerId()==id && last_bolwer!=id) {
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBowlerName2 " + boc.getPlayer().getTicker_name() + ";");
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFigure2 " + boc.getWickets() + "-" + boc.getRuns() + ";");
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tOvers2 " + CricketFunctions.OverBalls(boc.getOvers(), boc.getBalls())+ ";");
								if(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.FC)||
										match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.TEST)) {
									print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tMaidens M;");
									print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tDots2 " +  boc.getMaidens() + ";");
	
								}else {
									print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tDots2 " +  boc.getDots() + ";");
	
								}
								
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
						
/****************************************************Current Two Batsmen*******************************/
				if(inn.getPartnerships() != null && inn.getPartnerships().size() > 0) {
					
					this_bc = inn.getBattingCard().stream().filter(batcard -> batcard.getPlayerId() 
							== inn.getPartnerships().get(inn.getPartnerships().size() - 1).getFirstBatterNo()).findAny().orElse(null);
					
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBatterName1 " + this_bc.getPlayer().getTicker_name() + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBatterRun1 " + this_bc.getRuns() + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBatterBall1 " + this_bc.getBalls() + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBoundaries1 " + this_bc.getFours() + "/" + this_bc.getSixes() + ";");
					
					if(this_bc.getStatus().toUpperCase().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
						if(this_bc.getOnStrike().equalsIgnoreCase(CricketUtil.YES)) {
							batPlayerNum = 1;
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectStriker1 1;");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectStriker2 0;");
						}
					}else {
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectStriker1 0;");
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
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBoundaries2 " + this_bc1.getFours() + "/" + this_bc1.getSixes() + ";");
					
					if(this_bc1.getStatus().toUpperCase().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
						if(this_bc1.getOnStrike().equalsIgnoreCase(CricketUtil.YES)) {
							batPlayerNum = 2;
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectStriker1 0;");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectStriker2 1;");
						}	
					}else {
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectStriker2 0;");
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
				
	/******************************************  THIS OVER  *****************************************************/
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tThisOverExtra " 
						+ Stats.getOverData().getTotalRuns()+" RUN"+ CricketFunctions.Plural(Stats.getOverData().getTotalRuns()).toUpperCase() 
						+ (Stats.getOverData().getTotalWickets()>0 ? " & " + Stats.getOverData().getTotalWickets()+" WICKET"+
								CricketFunctions.Plural(Stats.getOverData().getTotalWickets()).toUpperCase() :"")+ ";");
					
					if(boc.getStatus().toUpperCase().equalsIgnoreCase("CURRENTBOWLER") || boc.getStatus().toUpperCase().equalsIgnoreCase("LASTBOWLER")) {
						String str = replaceTermsInString(Stats.getOverData().getThisOverTxt().replace("9BOUNDARY", "9").replace("4BOUNDARY", "4").replace("6BOUNDARY", "6"));
						int ov_num=0;
						director_Type = EventType (Stats.getOverData().getThisOverTxt());
						String reversed = String.join(",", 
								new java.util.ArrayList<String>(java.util.Arrays.asList(str.split(","))) 
								{{ java.util.Collections.reverse(this); }});

						if(reversed.split(",").length > 10) {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vThisOver " + ov_num + ";");
						}else {
							for(String over :reversed.split(",")) {
								if(!over.trim().isEmpty()) {
									ov_num++;
									if (over.contains(CricketUtil.WIDE + "+")||over.contains(CricketUtil.NO_BALL + "+")) {
										String cont =(over.contains(CricketUtil.WIDE + "+")? CricketUtil.WIDE :over.contains(CricketUtil.NO_BALL + "+")?
												"NO BALL" : "");
										String cont1 = over.replace(CricketUtil.WIDE + "+", "").replace(CricketUtil.NO_BALL + "+", "");
										print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vb" + ov_num + " " + 1 + ";");
										print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tThisOverSB" + ov_num + " " + cont+ ";");
										print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tThisOverSBv" + ov_num + " " + cont1+ ";");

									}else {
										String cont1 = over.replace(CricketUtil.WIDE , "WD").replace(CricketUtil.NO_BALL , "NB");
										print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vb" + ov_num + " " + 0 + ";");
										print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tThisOverB" + ov_num + " " + cont1+ ";");

									}
								}
							}
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vThisOver " + ov_num + ";");
						}
						if(!match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.TEST)&& 
								!match.getSetup().getMatchType().equalsIgnoreCase("FC")) {
							/********************************LAST 30 BALLS*************************************************/
							if (((inn.getTotalOvers() * Integer.valueOf(match.getSetup().getBallsPerOver())) + inn.getTotalBalls()) >= 30)
							{	
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSmallFreeText2 " + "LAST 30 BALLS : " + 
										Stats.getLastThirtyBalls().getTotalRuns() + " RUN" + CricketFunctions.Plural(Stats.getLastThirtyBalls().getTotalRuns()).toUpperCase() + " , " + Stats.getLastThirtyBalls().getTotalWickets() +
										" WICKET" + CricketFunctions.Plural(Stats.getLastThirtyBalls().getTotalWickets()).toUpperCase()+ ";");
							}else {
								/********************************LastOverRunWicket*************************************************/
								if(inn.getTotalOvers() > 0) {
										print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSmallFreeText2 " + "LAST OVER : " + 
												Stats.getLastOverData().getTotalRuns()+ " RUN" +CricketFunctions.Plural(Stats.getLastOverData().getTotalRuns()).toUpperCase() +" AND " 
												+ Stats.getLastOverData().getTotalWickets() + " WICKET" +CricketFunctions.Plural(Stats.getLastOverData().getTotalWickets()).toUpperCase() + ";");	
								}else {
									print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSmallFreeText2 " + "LAST OVER : ;");
								}
							}
						}
					}
				}
/**************************************** BOWLING CARD  *******************************************************************/
					if(inn.getBowlingCard() != null) {
						for(int i = 0; i <= inn.getBowlingCard().size() -1 ; i++) {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tAwayPlayer" + (i+1) + " " + 
									inn.getBowlingCard().get(i).getPlayer().getTicker_name() + ";");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectAwayPlayer" + (i+1) + " 1" + ";");
							if(inn.getBowlingCard().get(i).getPlayerId()==cPlayer) {
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vAwaySelectStriker" + (i+1) +" 1;");
							}else {
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vAwaySelectStriker" + (i+1) +" 0;");
							}
							
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBallFigs" + (i+1) + " " + 
									inn.getBowlingCard().get(i).getWickets() + "-" + inn.getBowlingCard().get(i).getRuns() + ";");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBallOvers" + (i+1) + " " + 
									CricketFunctions.OverBalls(inn.getBowlingCard().get(i).getOvers(), inn.getBowlingCard().get(i).getBalls()) + ";");
							if(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.FC)||
									match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.TEST)) {
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBallDots" + (i+1) + " " + 
										inn.getBowlingCard().get(i).getMaidens() + ";");
							}else {
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBallDots" + (i+1) + " " + 
										inn.getBowlingCard().get(i).getDots() + ";");							
							}
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBallEconomy" + (i+1) + " " +(
									inn.getBowlingCard().get(i).getEconomyRate()!=null && !inn.getBowlingCard().get(i).getEconomyRate().isEmpty() ? 
											inn.getBowlingCard().get(i).getEconomyRate() : "-") + ";");
						}
//						
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectAwayPlayerNumber " + 
							inn.getBowlingCard().size() + ";");
						
					}	
				}
				if(config.getAudio().equalsIgnoreCase(CricketUtil.LastBallAudio)) // LastBall 
				{
					if(inn.getTotalBalls()==5 && previousOver != inn.getTotalOvers()) {
						if(new File(CricketUtil.CRICKET_DIRECTORY+"Audio/Last ball.WAV").exists()) {
							playAudio(CricketUtil.CRICKET_DIRECTORY+"Audio/Last ball.WAV");	
						}
						previousOver = inn.getTotalOvers();
						previousBall = inn.getTotalBalls();
					}
				}
				
				if(this_over != Stats.getOverData().getThisOverTxt().split(",").length) {
					played = false;
				}else if(this_over == 1 && Stats.getOverData().getThisOverTxt().split(",").length == 1) {
					played = false;
				}
				
				System.out.println("director_Type  " + director_Type+"\nplayer_num   "+batPlayerNum);
				if(!played) {
					this_over = Stats.getOverData().getThisOverTxt().split(",").length;
					switch (director_Type.trim()) {
					case "NINE":
						if(batPlayerNum==1) {
			        		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tWipeSelect1 2;");
				        	print_writer.println("LAYER1*EVEREST*STAGE*DIRECTOR*Batter1 START;");
			        	}else {
			        		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tWipeSelect2 2;");
			        		print_writer.println("LAYER1*EVEREST*STAGE*DIRECTOR*Batter2 START;");
			        	}
			            break;
			        case "FOUR":
			        	if(batPlayerNum==1) {
			        		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tWipeSelect1 0;");
			        		print_writer.println("LAYER1*EVEREST*STAGE*DIRECTOR*Batter1 START;");
			        	}else if(batPlayerNum==2) {
			        		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tWipeSelect2 0;");
			        		print_writer.println("LAYER1*EVEREST*STAGE*DIRECTOR*Batter2 START;");
			        	}
			            break;
			        case "SIX":
			        	if(batPlayerNum==1) {
			        		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tWipeSelect1 1;");
			        		print_writer.println("LAYER1*EVEREST*STAGE*DIRECTOR*Batter1 START;");
			        	}else {
			        		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tWipeSelect2 1;");
			        		print_writer.println("LAYER1*EVEREST*STAGE*DIRECTOR*Batter2 START;");
			        	}
			            break;
			        case "HI-TRICK":
			        	if(Stats.getOverData().getConsecutiveW()==3) {
			        		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBowlersGrpSelector " + "3" + ";");
			        		if(config.getShowSpeed().equalsIgnoreCase("WITHOUT")) {
			        			processAnimation(print_writer, "BowlerWithoutSpeed", "START", session_selected_broadcaster,1);
			        		}else {
			        			processAnimation(print_writer, "Bowler", "START", session_selected_broadcaster,1);	
			        		}
			        	}
			            break;
			        case "ON-A-HI-TRICK":
			        	if(Stats.getOverData().getConsecutiveW()==2) {
				        	print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBowlersGrpSelector " + "2" + ";");
				        	if(config.getShowSpeed().equalsIgnoreCase("WITHOUT")) {
			        			processAnimation(print_writer, "BowlerWithoutSpeed", "START", session_selected_broadcaster,1);
			        		}else {
			        			processAnimation(print_writer, "Bowler", "START", session_selected_broadcaster,1);	
			        		}	
			        	}
			            break;
			        case "WICKET": 
			        	if(Stats.getOverData().getTotalWickets()>0) {
			        		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBowlersGrpSelector " + "1" + ";");
			        		if(config.getShowSpeed().equalsIgnoreCase("WITHOUT")) {
			        			processAnimation(print_writer, "BowlerWithoutSpeed", "START", session_selected_broadcaster,1);
			        		}else {
			        			processAnimation(print_writer, "Bowler", "START", session_selected_broadcaster,1);	
			        		}
			        	}
			            break;
			        case "FREE-HIT":
			        	print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBowlersGrpSelector " + "0" + ";");
			        	if(config.getShowSpeed().equalsIgnoreCase("WITHOUT")) {
		        			processAnimation(print_writer, "BowlerWithoutSpeed", "START", session_selected_broadcaster,1);
		        		}else {
		        			processAnimation(print_writer, "Bowler", "START", session_selected_broadcaster,1);	
		        		}
			            break;
					}
					played =true;
				}
			}
		}
	}
	public void Time (PrintWriter print_writer) {
       //LOCAL TIME
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tlocalTime  " +
				new SimpleDateFormat("hh:mm a").format(new Date()).toUpperCase()+ " IST;");
		
	}
	public void TestMatch(PrintWriter print_writer,MatchAllData match) throws Exception {
		int i = 0;
		int currentInningNumber = 0;

		for (Inning inn : match.getMatch().getInning()) {
		    i++;

		    if (CricketUtil.YES.equalsIgnoreCase(inn.getIsCurrentInning())) {
		        currentInningNumber = inn.getInningNumber();
		    }
		    if(currentInningNumber==1) {
		    	if(i>2) {
		    		break;
		    	}
		    }else {
			    if (i > currentInningNumber) break;
		    }

		    String session_name  = (i == 1 ? "tHomeTeam"    : i == 2 ? "tAwayTeam"    : i == 3 ? "tHomeTeaminn2"   : "tAwayTeaminn2");
		    String session_over  = (i == 1 ? "tATeamOvers"  : i == 2 ? "tBTeamOvers"  : i == 3 ? "tATeam2innOvers" : "tBTeam2innOvers");
		    String session_score = (i == 1 ? "tATeamScore"  : i == 2 ? "tBTeamScore"  : i == 3 ? "tATeam2innScore" : "tBTeam2innScore");
		    String session_RR    = (i == 1 ? "tATeamRR"     : i == 2 ? "tBTeamRR"     : i == 3 ? "tATeam2innRR"    : "tBTeam2innRR");
		    String session_OR    = (i == 1 ? "tATeamOR"     : i == 2 ? "tBTeamOR"     : i == 3 ? "tATeam2innOR"    : "tBTeam2innOR");
		    
		    String overRate = CricketFunctions.BetterOverRate(inn.getTotalOvers(), inn.getTotalBalls(), inn.getDuration(), "", false);
		   
		    print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET " + session_over + " " +
		        CricketFunctions.OverBalls(inn.getTotalOvers(), inn.getTotalBalls()) + ";");
		    print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET " + session_score + " " +
		    	CricketFunctions.getTeamScore(inn, "-", false) + ";");
		    print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET " + session_RR + " " +
		        CricketFunctions.generateRunRate(inn.getTotalRuns(), 0, inn.getTotalBalls(), 2, match) + ";");
		    
		    print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET " + session_OR + " " +
		    		(overRate.isEmpty() ? "-" : overRate) + ";");
		    print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET " + session_name + " INNING " +
		        inn.getInningNumber() + ";");
		}
	 	print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeamNameS " + (currentInningNumber==1 ? 1 :currentInningNumber-1) + ";");
	    print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tMBaseS " + (currentInningNumber==1 ? 1 :currentInningNumber-1) + ";");
	    print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tGroupS " + (currentInningNumber==1 ? 1 :currentInningNumber-1) + ";");
		    
		// CURRENT SESSION
		if (match != null && match.getMatch() != null && match.getMatch().getDaysSessions() != null &&
		    !match.getMatch().getDaysSessions().isEmpty()) {
			
			DaySession last = match.getMatch().getDaysSessions().get(match.getMatch().getDaysSessions().size() - 1);
			
			 print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tThisSession "
				 + "DAY " + last.getDayNumber() + "\n\n" + "SESSION " + last.getSessionNumber()+ ";");
			 
			 int balls =(last.getTotalBalls()>0?last.getTotalBalls():0);
			 int Runs =(last.getTotalRuns()>0?last.getTotalRuns():0);
			 int Wicket =(last.getTotalWickets()>0?last.getTotalWickets():0);
			 
			 print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSessionStats " +
		        CricketFunctions.OverBalls(0, balls) + " OVER" + CricketFunctions.Plural(balls/6).toUpperCase() +", " +
		        Runs + " RUN" + CricketFunctions.Plural(Runs).toUpperCase() + ", " +
		        Wicket + " WICKET" + CricketFunctions.Plural(Wicket).toUpperCase()+ ", "+
		        CricketFunctions.BetterOverRate(0, balls, last.getTotalSeconds(), "", false)
		        +" OVER RATE"+ " ;");

		    // SESSION RATE
		    print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSessionRunRate " +
		        CricketFunctions.generateRunRate(last.getTotalRuns(), 0, last.getTotalBalls(), 2,match ) + ";");

		    // REMAINING OVERS
		    print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectDataType 1;");

		    int overBowled = 0;
		    for (DaySession session : match.getMatch().getDaysSessions()) {
		        if (session != null && session.getDayNumber() == last.getDayNumber()) {
		            overBowled += session.getTotalBalls();
		        }
		    }
		    
		    Inning inning = match.getMatch().getInning().stream()
		        .filter(in -> in != null && in.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES))
		        .findAny().orElse(null);

		    if (inning != null && inning.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)) {
//		        int remainBalls = (match.getSetup().getOverRemainingNewBall().get(last.getDayNumber()-1).getOversRemaining() * 6) - overBowled;
//		        print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTodayOverRemaning " +
//		                CricketFunctions.OverBalls(0, remainBalls) + ";"); 
		    } else {
		        print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTodayOverRemaning ;");
		    }
		   
		} else {
		    print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSessionStats ;");
		    print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSessionRunRate ;");
		    print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectDataType 1;");
		    print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTodayOverRemaning ;");
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
	public void initialize_fruit(PrintWriter print_writer, MatchAllData match ,Configuration config) throws IOException {
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSpeedHead " + 
    			"SPEED" + ";");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tClientLogoS "+
    			config.getSelect_Client() + ";");
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
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStatHead5 " + "REVIEWS" + ";");
		//CURR P'SHIP
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPartnershipHead " + 
				"P'SHIP" + ";");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSpeedValue " +"  "
	            + ";");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tAwayStatValue1 " + "-" + ";");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomeStatValue1 " + "-" + ";");
		
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeamsHeader " + "TOSS " + ";");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBowlerName2 " + "-" + ";");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFigure2 " + "-" + ";");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tOvers2 " + "-" + ";");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tDots2 " + "-" + ";");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tEconomy2 " + "-" + ";");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectBowler1" + " " + "0" + ";");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFowTeamName1 " + 
				match.getMatch().getInning().get(0).getBatting_team().getTeamName4() + ";");
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
		
		  if(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.ODI)||match.
				  getSetup().getMatchType().equalsIgnoreCase(CricketUtil.OD)) {
		 
			 print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPhaseHead1 "
					 + "1-10" + ";"); 
			 print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPhaseHead2 "
					 + "11-40" + ";"); 
			 print_writer. println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPhaseHead3 " 
					 + "41-50" + ";");
		 
		  }else if(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.DT20)||
				  match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.IT20)) { 
			 
			  print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPhaseHead1 "
					  + "1-6" + ";"); 
			  print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPhaseHead2 "
					  	+ "7-15" + ";");
			  print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPhaseHead3 "
					  	+ "16-20" + ";");
		
		 }else if(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.D10)) {
			  print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPhaseHead1 "
					  + "1-2" + ";"); 
			  print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPhaseHead2 "
					  + "3-6" + ";"); 
			  print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPhaseHead3 "
				  + "7-10" +";"); 
		  }else {
			  	print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeamName2 ;");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeamName1 ;");
			  	print_writer.println("LAYER1*EVEREST*TREEVIEW*Main$All$InningsDataGrp$PhaseScoreAll$PhaseScoreHead*CONTAINER SET ACTIVE 0 ;");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main$All$InningsDataGrp$PhaseScoreAll$Header*CONTAINER SET ACTIVE 0 ;");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeam2PhaseScore1 ;");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeam2PhaseScore2 ;");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeam2PhaseScore3 ;");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeam1PhaseScore1 ;");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeam1PhaseScore2 ;");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeam1PhaseScore3 ;");
		  }
		  if(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.TEST)||
				  match.getSetup().getMatchType().equalsIgnoreCase("FC")) {
			  	print_writer.println("LAYER1*EVEREST*TREEVIEW*Main$All$LastOver*CONTAINER SET ACTIVE 0 ;");
			  	print_writer.println("LAYER1*EVEREST*TREEVIEW*Main$All$Projected\\Equation*CONTAINER SET ACTIVE 0 ;");
			  	print_writer.println("LAYER1*EVEREST*TREEVIEW*Main$All$StatComparisonGrp*CONTAINER SET ACTIVE 0 ;");
			  	print_writer.println("LAYER1*EVEREST*TREEVIEW*Main$All$InningsDataGrp*CONTAINER SET ACTIVE 0 ;");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main$All$LastOver*CONTAINER SET ACTIVE 0 ;");
			  	print_writer.println("LAYER1*EVEREST*TREEVIEW*Main$All$TestGroup*CONTAINER SET ACTIVE 1 ;");

		  }else {
       			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main$All$LastOver*CONTAINER SET ACTIVE 1 ;");
			  	print_writer.println("LAYER1*EVEREST*TREEVIEW*Main$All$Projected\\Equation*CONTAINER SET ACTIVE 1 ;");
			  	print_writer.println("LAYER1*EVEREST*TREEVIEW*Main$All$StatComparisonGrp*CONTAINER SET ACTIVE 1 ;");
			  	print_writer.println("LAYER1*EVEREST*TREEVIEW*Main$All$InningsDataGrp*CONTAINER SET ACTIVE 1 ;");
			  	print_writer.println("LAYER1*EVEREST*TREEVIEW*Main$All$TestGroup*CONTAINER SET ACTIVE 0 ;");
		  }
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectHomePlayerNumber " + 
				"0" + ";");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectAwayPlayerNumber " + 
				"0" + ";");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectPage 0;");	
		processAnimation(print_writer, "In", "START", session_selected_broadcaster,1);

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
		case "DOAD_FRUIT":
			if(review == null) {
				if(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.TEST)||
						match.getSetup().getMatchType().equalsIgnoreCase("FC")) {
					printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeam1Review ;");
			        printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeam2Review ;");
				}else {
					printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomeStatValue5 0;");
					printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tAwayStatValue5 0;");
				}			
			}else {
				String homeTeam = match.getSetup().getHomeTeam().getTeamName4(), awayTeam = match.getSetup().getAwayTeam().getTeamName4();
//				printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeamsHeader " +  "REVIEWS REMAINING :-" + 
//	    				String.format("%-10s",homeTeam) + " : " + review.getReviewStatus().split(",")[0]+"   " + 
//						String.format("%-8s", awayTeam + " : " + review.getReviewStatus().split(",")[1]) + ";");
				if(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.TEST)||
						match.getSetup().getMatchType().equalsIgnoreCase("FC")) {
					printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeam1Review " + 
							homeTeam +":"+ review.getReviewStatus().split(",")[0]+ " ;");
			        printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeam2Review " + 
			        		awayTeam +":"+ review.getReviewStatus().split(",")[1] + " ;");
				}else {
					printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomeStatValue5 " + review.getReviewStatus().split(",")[0]+ ";");
					printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tAwayStatValue5 " + review.getReviewStatus().split(",")[1] + ";");
				}
			}
			break;
		}
	}
	public static String replaceTermsInString(String input) {
	    if (input != null && !input.isEmpty() && (input.contains("WIDE") || input.contains("NO_BALL") ||
	            input.contains("LEG_BYE") || input.contains("BYE") || input.contains("PENALTY") ||
	            input.contains("LOG_WICKET") || input.contains("WICKET"))) {
			input = input/* .replace("WIDE", "wd") */
					/* .replace("NO_BALL", "nb") */
	                .replace("LEG_BYE", "LB")
	                .replace("BYE", "B")
	                .replace("PENALTY", "PN")
	                .replace("LOG_WICKET", "W")
	                .replace("WICKET", "W");
	    }
	    return input;
	}
	public static String EventType(String input) {
	    String[] parts = input.split(",");

	    if (parts.length > 0 && parts[0].contains("9BOUNDARY")) {
	        return "NINE";
	    } else if (parts.length > 0 && parts[0].contains("4BOUNDARY")) {
	        return "FOUR";
	    } else if (parts.length > 0 && parts[0].contains("6BOUNDARY")) {
	        return "SIX";
	    } else if (parts.length > 0 && parts[0].contains("NO_BALL")) {
	        return "FREE-HIT";
	    }


	    if (parts.length >= 1 && parts[0].contains("LOG_WICKET")) {
	        if (parts.length >= 3 && parts[1].contains("LOG_WICKET") && parts[2].contains("LOG_WICKET")) {
	            return "HI-TRICK";
	        } else if (parts.length >= 2 && parts[1].contains("LOG_WICKET")) {
	            return "ON-A-HI-TRICK";
	        } else {
	            return "WICKET";
	        }
	    }

	    return "";
	}
	public static String reverseStringWithPreservation(String input) {
       // String[] parts = input.split("\\s*\\|\\s*"); // Split the string by "|"
		 String[] parts = input.split("\\s*\\,\\s*"); // Split the string by ","
        StringBuilder reversedString = new StringBuilder();

        for (int i = parts.length - 1; i >= 0; i--) {
            reversedString.append(parts[i]);
            if (i > 0) {
                reversedString.append(" | "); // Add back the "|" separator
            }
        }

        return reversedString.toString();
    }
	
	public void showSpeedAndReview(PrintWriter print_writer, Configuration config) {
	    boolean showSpeed = !"WITHOUT".equals(config.getShowSpeed());
	    boolean showReview = !"WITHOUT".equals(config.getShowReview());

	    print_writer.println("LAYER1*EVEREST*TREEVIEW*Main$All$Slect_Page$REWORK$SpeedGrp*CONTAINER SET ACTIVE " + (showSpeed ? "1" : "0") + ";");
	    print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vBowlerGrp " + (showSpeed ? "0" : "1") + ";");

	    double distRow = showReview ? 50.00 : 60.00;
	    int statGrpValue = showReview ? 4 : 3;

	    String[] statGrps = {"StatHeadGrp", "HomeStatValueGrp", "AwayStatValueGrp"};
	    for (String grp : statGrps) {
	        print_writer.println("LAYER1*EVEREST*TREEVIEW*Main$All$Slect_Page$REWORK$StatComparisonGrp$StatGrp$" + grp + "*FUNCTION_SET_PROP*GRID_ARRANGE distRow=" + distRow + ";");
	        print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET v" + grp + " " + statGrpValue + ";");
	    }
	}
	
	public static void playAudio(String audioFilePath) {
	    try {
	        File audioFile = new File(audioFilePath);
	        if (audioFile.exists()) {
	            try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile)) {
	                Clip clip = AudioSystem.getClip();
	                clip.open(audioInputStream);
	                clip.start();
	                Thread.sleep(clip.getMicrosecondLength() / 1000);
	            } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
	                e.printStackTrace();
	            }
	        } else {
	            System.err.println("Audio file not found: " + audioFilePath);
	        }
	    } catch (InterruptedException e) {
	        e.printStackTrace();
	    }
	}
}