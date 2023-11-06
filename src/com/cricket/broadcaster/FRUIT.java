package com.cricket.broadcaster;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.xml.bind.JAXBException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.cricket.model.BattingCard;
import com.cricket.model.BowlingCard;
import com.cricket.model.DaySession;
import com.cricket.model.Player;
import com.cricket.model.Statistics;
import com.cricket.service.CricketService;
import com.cricket.model.Inning;
import com.cricket.model.MatchAllData;
import com.cricket.model.InfobarStats;
import com.cricket.containers.DuckWorthLewis;
import com.cricket.containers.Infobar;
import com.cricket.containers.Scene;
import com.cricket.util.CricketFunctions;
import com.cricket.util.CricketUtil;

import net.sf.json.JSONArray;
import net.sourceforge.jtds.jdbc.DateTime;

public class FRUIT extends Scene{

	private String status;
	private String slashOrDash = "-";
	public Infobar infobar = new Infobar();
	public String session_selected_broadcaster = "FRUIT";
	public String which_graphics_onscreen = "";
	private String logo_path = "C:\\Images\\FRUIT\\Logos\\";
	//private String sponsor_logo_path = "D:\\EverestCricket\\Everest_Cric2022\\Logos\\Goa_Cricket_Logos\\Sponsors\\";
	//private String photo_path = "C:\\Images\\FRUIT\\Photos\\";
	public int Whichside = 1;
	public int Whichinn = 1;
	
	public FRUIT() {
		super();
	}

	public FRUIT(String scene_path, String which_Layer) {
		super(scene_path, which_Layer);
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	public Infobar updateInfobar(Scene scene, MatchAllData match,boolean show_speed, PrintWriter print_writer) throws InterruptedException, IOException
	{
		if(infobar.isInfobar_on_screen() == true) {
			infobar = populateInfobarTeamScore(infobar,true, print_writer, match, session_selected_broadcaster);
			infobar = populateInfobarTopLeft(infobar,true, print_writer, match, session_selected_broadcaster);
			infobar = populateInfobarTopRight(infobar,true, print_writer, match, session_selected_broadcaster);
			infobar = populateComp(infobar,true, print_writer, match, session_selected_broadcaster);
			infobar = populateProjectedAndPhaseBy(infobar,true, print_writer, match, session_selected_broadcaster);
			infobar = populateThisOver(infobar,true, print_writer, match, session_selected_broadcaster);
			infobar = populateFOW(infobar,true, print_writer, match, session_selected_broadcaster);
			infobar = populateSpeed(infobar,true, print_writer, match, session_selected_broadcaster);
			infobar = populateBattingCard(infobar,true, print_writer, match, session_selected_broadcaster);
			infobar = populateBowlingCard(infobar,true, print_writer, match, session_selected_broadcaster);
//			if(infobar.getBottom_right_bottom_section() != null && !infobar.getBottom_right_bottom_section().isEmpty()) {
//				infobar = populateInfobarBottom(infobar,true, print_writer,  match, session_selected_broadcaster);
//			}
			
		}
//		CricketFunctions.getInteractive(match);
		return infobar;
	}
	public Infobar updateSpeed(Scene scene, MatchAllData match,boolean show_speed, PrintWriter print_writer) throws InterruptedException, IOException
	{
		if(infobar.isInfobar_on_screen() == true) {
			infobar = populateSpeed(infobar,true, print_writer, match, session_selected_broadcaster);
		}
		return infobar;
	}
	
	public Object ProcessGraphicOption(String whatToProcess, MatchAllData match, CricketService cricketService, List<MatchAllData> tournament_matches,
			PrintWriter print_writer, List<Scene> scenes, String valueToProcess, List<Statistics> statistics) throws InterruptedException, ParseException, JAXBException, NumberFormatException, IOException, IllegalAccessException, InvocationTargetException{
		switch (whatToProcess.toUpperCase()) {
		case "PROMPT_GRAPHICS-OPTIONS":
			return JSONArray.fromObject(cricketService.getInfobarStats()).toString();
		
		case "POPULATE-FRUIT": case "POPULATE-INFOBAR-BOTTOMLEFT": case "POPULATE-INFOBAR-BOTTOMRIGHT": case "POPULATE-INFOBAR-BOTTOM": 
		case "POPULATE-INFOBAR-PROMPT": case "POPULATE-INFOBAR-TOP":
			if(which_graphics_onscreen == "INFOBAR") {
			}
			else if(which_graphics_onscreen != "") {
				AnimateOutGraphics(print_writer, which_graphics_onscreen.toUpperCase());
			}
			
			switch (session_selected_broadcaster.toUpperCase()) {
			case "FRUIT":
				switch(whatToProcess.toUpperCase()) {
				case"POPULATE-INFOBAR-BOTTOMLEFT": case"POPULATE-INFOBAR-BOTTOMRIGHT": case"POPULATE-INFOBAR-PROMPT": case "POPULATE-INFOBAR-BOTTOM": case "POPULATE-DIRECTOR":
				case "POPULATE-INFOBAR-TOPRIGHT":
					break;
				case "POPULATE-FRUIT":
					scenes.get(0).setScene_path(valueToProcess.split(",")[0]);
					scenes.get(0).scene_load(print_writer,session_selected_broadcaster);
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
				//return JSONObject.fromObject(this_doad).toString();
			}
		case "ANIMATE-IN-FRUIT": case "ANIMATE-OUT": case "CLEAR-ALL": 
			switch (session_selected_broadcaster.toUpperCase()) {
			case "FRUIT":
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
				//return JSONObject.fromObject(this_doad).toString();
			}
		}
		return null;
}
	
	
	public void populateDoubleteams(PrintWriter print_writer,String viz_scene, MatchAllData match, String session_selected_broadcaster) throws InterruptedException
	{
		switch (session_selected_broadcaster.toUpperCase()) {
		case "FRUIT":
			if (match == null) {
				this.status = "ERROR: Match is null";
			} else if (match.getMatch().getInning() == null) {
				this.status = "ERROR: DoubleTeam's inning is null";
			} else {
				
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHeader " + "TEAMS" + ";");
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSubHeader " + match.getSetup().getMatchIdent().toUpperCase() + ";");
				if(match.getSetup().getTossWinningTeam() == match.getSetup().getHomeTeamId()) {
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInfo " + match.getSetup().getHomeTeam().getTeamName1().toUpperCase() + 
							" WON TOSS & CHOSE TO " + match.getSetup().getTossWinningDecision() + ";");
				}else {
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInfo " + match.getSetup().getAwayTeam().getTeamName1().toUpperCase() + 
							" WON TOSS & CHOSE TO " + match.getSetup().getTossWinningDecision() + ";");
				}
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgHomeTeamLogo " + logo_path + 
						match.getSetup().getHomeTeam().getTeamName4().toUpperCase() + CricketUtil.PNG_EXTENSION + ";");
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgAwayTeamLogo " + logo_path + 
						match.getSetup().getAwayTeam().getTeamName4().toUpperCase() + CricketUtil.PNG_EXTENSION + ";");
				
				int row_id = 0;
				for(int i = 1; i <= 2 ; i++) {
					if(i == 1) {
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomeTeamName " + match.getSetup().getHomeTeam().getTeamName1().toUpperCase() + ";");
						
						for(Player hs : match.getSetup().getHomeSquad()) {
							row_id = row_id + 1;
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomePlayerName" + row_id + " " + hs.getFull_name() + ";");
							//System.out.println(hs.getFull_name().toUpperCase());
							if(hs.getCaptainWicketKeeper() != null ) {
								if(hs.getCaptainWicketKeeper().equalsIgnoreCase(CricketUtil.CAPTAIN)) {
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgHomeIcon"+ row_id +" "+ "1" + ";");
								}
								else if(hs.getCaptainWicketKeeper().equalsIgnoreCase(CricketUtil.WICKET_KEEPER)) {
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgHomeIcon"+ row_id +" "+ "2" + ";");
								}
								else if(hs.getCaptainWicketKeeper().equalsIgnoreCase("CAPTAIN_WICKET_KEEPER")) {
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgHomeIcon"+ row_id +" "+ "3" + ";");
								}
								else {
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgHomeIcon"+ row_id +" "+ "0" + ";");
								}						
							}
						}
					} else {
						row_id = 0;
						
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tAwayTeamName " + match.getSetup().getAwayTeam().getTeamName1().toUpperCase() + ";");
						for(Player as : match.getSetup().getAwaySquad()) {
							row_id = row_id + 1;
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tAwayPlayerName" + row_id + " " + as.getFull_name() + ";");
							if(as.getCaptainWicketKeeper() != null ) {
								if(as.getCaptainWicketKeeper().equalsIgnoreCase(CricketUtil.CAPTAIN)) {
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgAwayIcon"+ row_id +" "+ "1" + ";");
								}
								else if(as.getCaptainWicketKeeper().equalsIgnoreCase(CricketUtil.WICKET_KEEPER)) {
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgAwayIcon"+ row_id +" "+ "2" + ";");
								}
								else if(as.getCaptainWicketKeeper().equalsIgnoreCase("CAPTAIN_WICKET_KEEPER")) {
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgAwayIcon"+ row_id +" "+ "3" + ";");
								}
								else {
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgAwayIcon"+ row_id +" "+ "0" + ";");
								}						
							}
						}
					}
				}		
				
				print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW ON;");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*CONTAINER SET ACTIVE 0;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In STOP;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out STOP;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 152.0;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out SHOW 0.0;");
				print_writer.println("LAYER1*EVEREST*GLOBAL SNAPSHOT_PATH C:/Temp/Preview.png;");
				print_writer.println("LAYER1*EVEREST*GLOBAL SNAPSHOT 1920 1080;");
				TimeUnit.SECONDS.sleep(1);
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out SHOW 0.0;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 0.0;");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*CONTAINER SET ACTIVE 1;");
				print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW OFF;");
				this.status = CricketUtil.SUCCESSFUL;	
			}
			break;
		}
		
	}
	public Infobar populateInfobar(Infobar infobar,PrintWriter print_writer,String viz_scene, MatchAllData match, String session_selected_broadcaster) throws InterruptedException, IOException 
	{
		switch (session_selected_broadcaster.toUpperCase()) {
		case "FRUIT":
			if (match == null) {
				this.status = "ERROR: Match is null";
			} else if (match.getMatch().getInning() == null) {
				this.status = "ERROR: Infobar's inning is null";
			} else {
				infobar = populateInfobarTeamScore(infobar,false, print_writer, match, session_selected_broadcaster);
				infobar = populateInfobarTopRight(infobar,false, print_writer, match, session_selected_broadcaster);
				//infobar = populateInfobarBottomLeft(infobar,false, print_writer, match, session_selected_broadcaster);
				//infobar = populateInfobarBottomRight(infobar,false, print_writer, match, session_selected_broadcaster);
				infobar = populateInfobarTopLeft(infobar,false, print_writer, match, session_selected_broadcaster);
				infobar = populateComp(infobar,false, print_writer, match, session_selected_broadcaster);
				infobar = populateProjectedAndPhaseBy(infobar,false, print_writer, match, session_selected_broadcaster);
				infobar = populateThisOver(infobar,false, print_writer, match, session_selected_broadcaster);
				infobar = populateFOW(infobar,false, print_writer, match, session_selected_broadcaster);
				infobar = populateSpeed(infobar,false, print_writer, match, session_selected_broadcaster);
				infobar = populateBattingCard(infobar,false, print_writer, match, session_selected_broadcaster);
				infobar = populateBowlingCard(infobar,false, print_writer, match, session_selected_broadcaster);

				this.status = CricketUtil.SUCCESSFUL;	
			}
			break;
		}
		return infobar;
		
	}	
	public Infobar populateInfobarTeamScore(Infobar infobar,boolean is_this_updating, PrintWriter print_writer, MatchAllData match, String session_selected_broadcaster)
	{
		
		switch (session_selected_broadcaster.toUpperCase()) {
		case "FRUIT":
			for(Inning inn : match.getMatch().getInning()) {
				if(is_this_updating == false) {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTounamentName " + match.getSetup().getTournament().toUpperCase() + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tMatchNumber " + match.getSetup().getMatchIdent() + ";");
				}
				if (inn.getIsCurrentInning().toUpperCase().equalsIgnoreCase(CricketUtil.YES)) {
					if(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.TEST)) {
						if(match.getSetup().getFollowOn().equalsIgnoreCase(CricketUtil.YES)) {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectPowerplay " + "1" + ";");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPowerPlay " + "f/o" + ";");
						}else {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectPowerplay " + "0" + ";");
						}
				    }else {
				    	if(!match.getSetup().getTargetOvers().isEmpty() && Double.valueOf(match.getSetup().getTargetOvers()) == 1) {
					    	print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectPowerplay " + "0" + ";");
					    }
					    else {
					    	if(CricketFunctions.processPowerPlay(CricketUtil.MINI,match).isEmpty()) {
					    		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectPowerplay " + "0" + ";");
					    	}else {
					    		if(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.TEST)) {
					    			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectPowerplay " + "0" + ";");
					    		}
					    		else if(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.ODI) || match.getSetup().getMatchType().equalsIgnoreCase("OD")) {
					    			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectPowerplay " + "1" + ";");
									print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPowerPlay " + 
											CricketFunctions.processPowerPlay(CricketUtil.MINI,match) + ";");
					    		}else {
					    			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectPowerplay " + "1" + ";");
									print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPowerPlay " + "P" + ";");
					    		}
				    			
					    	}
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
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tWideHead " + "WD" + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tNoBallHead " + "NB" + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tByeHead " + "B" + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tLegByelHead " + "LB" + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPenaltyHead " + "PEN" + ";");
					
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
		return infobar;
	}
	public Infobar populateComp(Infobar infobar,boolean is_this_updating, PrintWriter print_writer, MatchAllData match, String session_selected_broadcaster)
	{
		String[] Count1 = CricketFunctions.getScoreTypeData(CricketUtil.TEAM, match, 1, 0, ",", match.getEventFile().getEvents()).split(",");
		switch (session_selected_broadcaster.toUpperCase()) {
		case "FRUIT":
			if(is_this_updating == false) {
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStatHead1 " + "4s" + ";");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStatHead2 " + "6s" + ";");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStatHead3 " + "AT THIS STAGE" + ";");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStatHead4 " + "DOTS" + ";");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tComparisonHomeTeamName " + match.getMatch().getInning().get(0).getBatting_team().getTeamName1() + ";");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tComparisonAwayTeamName " + match.getMatch().getInning().get(1).getBatting_team().getTeamName1() + ";");
			}
			
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomeStatValue1 " + match.getMatch().getInning().get(0).getTotalFours() + ";");
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomeStatValue2 " + match.getMatch().getInning().get(0).getTotalSixes() + ";");
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomeStatValue3 " + CricketFunctions.compareInningData(match, "-", 1, match.getEventFile().getEvents()) + ";");
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomeStatValue4 " + Count1[0] + ";");
			
			
			for(Inning inn : match.getMatch().getInning()) {
				if(inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)) {
					
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSmallFreeText1 " + "BALL SINCE LAST BOUNDARY : " + 
							CricketFunctions.lastFewOversData(CricketUtil.BOUNDARY, match.getEventFile().getEvents(),inn.getInningNumber()) + ";");
					
					if (((inn.getTotalOvers() * 6) + inn.getTotalBalls()) > 30)
					{
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSmallFreeText2 " + "LAST 30 BALLS : " + 
								CricketFunctions.getlastthirtyballsdata(match, "-", match.getEventFile().getEvents(), 30).split("-")[0] + "-" +
								CricketFunctions.getlastthirtyballsdata(match, "-", match.getEventFile().getEvents(), 30).split("-")[1] + ";");
					}
					else
					{
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSmallFreeText2 " + "LAST 30 BALLS : " + 
								"-" + ";");
					}
					
					
					if(inn.getInningNumber() == 1) {
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tAwayStatValue1 " + "0" + ";");
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tAwayStatValue2 " + "0" + ";");
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tAwayStatValue3 " + "0" + ";");
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET AwayStatValue4 " + "0" + ";");
					}
					
					if(inn.getInningNumber() == 2) {
						String[] Count = CricketFunctions.getScoreTypeData(CricketUtil.TEAM, match, 2, 0, ",", match.getEventFile().getEvents()).split(",");
						
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tAwayStatValue1 " + inn.getTotalFours() + ";");
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tAwayStatValue2 " + inn.getTotalSixes() + ";");
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tAwayStatValue3 " + CricketFunctions.compareInningData(match, "-", 2, match.getEventFile().getEvents()) + ";");
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET AwayStatValue4 " + Count[0] + ";");
					}
				}
				
			}
			break;
		}
		return infobar;
	}
	public Infobar populateProjectedAndPhaseBy(Infobar infobar,boolean is_this_updating, PrintWriter print_writer, MatchAllData match, String session_selected_broadcaster)
	{
		switch (session_selected_broadcaster.toUpperCase()) {
		case "FRUIT":
			
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
							match.getMatch().getInning().get(0).getBatting_team().getTeamName1() + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeamName2 " + 
							match.getMatch().getInning().get(1).getBatting_team().getTeamName1() + ";");
					
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeam1PhaseScore1 " + 
							CricketFunctions.getFirstPowerPlayScore(match, 1, match.getEventFile().getEvents()) + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeam1PhaseScore2 " + 
							CricketFunctions.getSecPowerPlayScore(match, 1, match.getEventFile().getEvents()) + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeam1PhaseScore3 " + 
							CricketFunctions.getThirdPowerPlayScore(match, 1, match.getEventFile().getEvents()) + ";");
					
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeam2PhaseScore1 " + 
							CricketFunctions.getFirstPowerPlayScore(match, 2, match.getEventFile().getEvents()) + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeam2PhaseScore2 " + 
							CricketFunctions.getSecPowerPlayScore(match, 2, match.getEventFile().getEvents()) + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeam2PhaseScore3 " + 
							CricketFunctions.getThirdPowerPlayScore(match, 2, match.getEventFile().getEvents()) + ";");
					
				}
			}
			break;
		}
		return infobar;
	}
	public Infobar populateThisOver(Infobar infobar,boolean is_this_updating, PrintWriter print_writer, MatchAllData match, String session_selected_broadcaster)
	{
		switch (session_selected_broadcaster.toUpperCase()) {
		case "FRUIT":
			List<String> Ovr = new ArrayList<String>();
			for(Inning inn : match.getMatch().getInning()) {
				if (inn.getIsCurrentInning().toUpperCase().equalsIgnoreCase(CricketUtil.YES)) {
					for(BowlingCard boc : inn.getBowlingCard()) {
						if(boc.getStatus().toUpperCase().equalsIgnoreCase("CURRENTBOWLER") || boc.getStatus().toUpperCase().equalsIgnoreCase("LASTBOWLER")) {
							String arr[] = CricketFunctions.getEventsText(CricketUtil.OVER,0,",", match.getEventFile().getEvents(),0).split(",");
							for(int i=0;i < CricketFunctions.getEventsText(CricketUtil.OVER,0,",", match.getEventFile().getEvents(),0).split(",").length;i++) {
									Ovr.add(arr[i]);
							}
							String cumm_runs = String.join(" | ", Ovr);
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tThisOverData " + cumm_runs + ";");
						}
					}
				}
			}
			break;
		}
		return infobar;
	}
	public Infobar populateFOW(Infobar infobar,boolean is_this_updating, PrintWriter print_writer, MatchAllData match, String session_selected_broadcaster)
	{
		switch (session_selected_broadcaster.toUpperCase()) {
		case "FRUIT":
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFowTeamName1 " + 
	    			match.getMatch().getInning().get(0).getBatting_team().getTeamName4() + ";");
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFowTeamName2 " + 
					match.getMatch().getInning().get(1).getBatting_team().getTeamName4() + ";");
			
			if(is_this_updating == false) {
				for (int i = 1; i <= 10; i++)
			    {
			    	print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeam1Fow" + i + " " + 
			    			"" + ";");
			    	print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeam2Fow" + i + " " + 
			    			"" + ";");
			    }
			}
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
		return infobar;
	}
	public Infobar populateSpeed(Infobar infobar,boolean is_this_updating, PrintWriter print_writer, MatchAllData match, String session_selected_broadcaster) throws IOException
	{
		switch (session_selected_broadcaster.toUpperCase()) {
		case "FRUIT":
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSpeedHead " + 
	    			"SPEED" + ";");
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSpeedUnit " + 
					"(KM/H)" + ";");
			
			if(is_this_updating == false) {
			}
			
			String data = new String(Files.readAllBytes(Paths.get("C:\\Sports\\Cricket\\Speed\\SPEED.txt")));
	        // Split the content by lines and print each line separately
	        String[] lines = data.split("\n");
	        
	        String speed = lines[0].trim();
	        
			
		    File file = new File("C:\\Sports\\Cricket\\Speed\\SPEED.txt");
		    Path path = file.toPath();
		    BasicFileAttributes file_att = Files.readAttributes(
		            path, BasicFileAttributes.class);
		    FileTime dt = file_att.creationTime();
		    
		    FileTime lastModified = file_att.lastModifiedTime();
		    if (lastModified != dt)
		    {
		    	print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSpeedValue " + 
		    			speed + ";");
		    }
			
			break;
		}
		return infobar;
	}
	public Infobar populateBattingCard(Infobar infobar,boolean is_this_updating, PrintWriter print_writer, MatchAllData match, String session_selected_broadcaster) throws IOException
	{
		switch (session_selected_broadcaster.toUpperCase()) {
		case "FRUIT":
			boolean player_found = false;
			if(is_this_updating == false) {
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeamsHeader " + 
		    			"INNING SUMMARY" + ";");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectHomePlayerNumber " + 
						"0" + ";");
			}
			
			for(Inning inn : match.getMatch().getInning()) {
				if (inn.getIsCurrentInning().toUpperCase().equalsIgnoreCase(CricketUtil.YES)) {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomeTeamName " + 
							inn.getBatting_team().getTeamName1() + ";");
					
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
		return infobar;
	}
	public Infobar populateBowlingCard(Infobar infobar,boolean is_this_updating, PrintWriter print_writer, MatchAllData match, String session_selected_broadcaster) throws IOException
	{
		switch (session_selected_broadcaster.toUpperCase()) {
		case "FRUIT":
			boolean player_found = false;
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectAwayPlayerNumber " + 
					"0" + ";");
			for(Inning inn : match.getMatch().getInning()) {
				if (inn.getIsCurrentInning().toUpperCase().equalsIgnoreCase(CricketUtil.YES)) {
					if(inn.getBowlingCard() != null) {
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tAwayTeamName " + 
								inn.getBowling_team().getTeamName1() + ";");
						
						if(inn.getBowlingTeamId() == match.getSetup().getHomeTeamId()) {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectAwayPlayerNumber " + 
									inn.getBowlingCard().size() + ";");
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
											print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectHomePlayer" + (i+1) + " 1" + ";");
											print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectAwayStriker3 0" + ";");
											print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vAwaySelectStriker" + (i+1) + " 0" + ";");
											print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tAwayScore" + (i+1) + " " + 
													 boc.getWickets() + "-" + boc.getRuns() + ";");
											print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tAwayBalls" + (i+1) + " " + 
													CricketFunctions.OverBalls(boc.getOvers(), boc.getBalls()) + ";");
										}
									}
								}
							}
						}else if(inn.getBowlingTeamId() == match.getSetup().getAwayTeamId()) {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectAwayPlayerNumber " + 
									inn.getBowlingCard().size() + ";");
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
							}
						}
					}
				}
			}
			break;
		}
		return infobar;
	}
	public Infobar populateInfobarTopLeft(Infobar infobar, boolean is_this_updating, PrintWriter print_writer,MatchAllData match, String broadcaster)
	{ 
		List<BattingCard> current_batsmen = new ArrayList<BattingCard>();
		for(Inning inn : match.getMatch().getInning()) {
			if (inn.getIsCurrentInning().toUpperCase().equalsIgnoreCase(CricketUtil.YES)) {
				for (BattingCard bc : inn.getBattingCard()) {
					
					if(inn.getPartnerships() != null && inn.getPartnerships().size() > 0) {
						if(bc.getPlayerId() == inn.getPartnerships().get(inn.getPartnerships().size() - 1).getFirstBatterNo()) {
							current_batsmen.add(bc);
						} else if(bc.getPlayerId() == inn.getPartnerships().get(inn.getPartnerships().size() - 1).getSecondBatterNo()) {
							current_batsmen.add(bc);
						}
					}
					
// -----------------------------------------------------------------Implement Last Wicket--------------------------------------------
					
					if(inn.getFallsOfWickets() != null && !inn.getFallsOfWickets().isEmpty()) {
						if(inn.getFallsOfWickets().size() > 0){
							if(inn.getFallsOfWickets().get(inn.getFallsOfWickets().size() - 1).getFowPlayerID() == bc.getPlayerId()) {
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tLastWicketValue " 
											+ bc.getPlayer().getTicker_name() + ";");
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tLastWicketScore " 
											+ bc.getRuns() + ";");
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
				
				populateCurrentBatsmen(infobar,print_writer, match, broadcaster,current_batsmen);
				
				if(current_batsmen != null && current_batsmen.size() >= 1) {
					infobar.setLast_batsmen(current_batsmen);
				}
			}
		}
		return infobar;
	}
	public Infobar populateCurrentBatsmen(Infobar infobar, PrintWriter print_writer, MatchAllData match, String broadcaster,List<BattingCard> current_batsmen)
	{
		for(Inning inn : match.getMatch().getInning()) {
			
			if (inn.getIsCurrentInning().toUpperCase().equalsIgnoreCase(CricketUtil.YES)) {
				if(current_batsmen != null && current_batsmen.size() >= 1) {
					if(current_batsmen.get(0).getPlayerId() != current_batsmen.get(0).getPlayerId()) {
						//processAnimation(print_writer, "Batsman1ChangeOut", "START", session_selected_broadcaster,1);
						//TimeUnit.SECONDS.sleep(1);
					}
					if(current_batsmen.get(1).getPlayerId() != current_batsmen.get(1).getPlayerId()) {
						//processAnimation(print_writer, "Batsman2ChangeOut", "START", session_selected_broadcaster,1);
						//TimeUnit.SECONDS.sleep(1);
					}
				}
				
				if(current_batsmen != null && current_batsmen.size() >= 2) {
					
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBatterName1 " + current_batsmen.get(0).getPlayer().getTicker_name() + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBatterRun1 " + current_batsmen.get(0).getRuns() + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBatterBall1 " + current_batsmen.get(0).getBalls() + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBoundaries1 " + current_batsmen.get(0).getFours() + "/" + current_batsmen.get(0).getSixes() + ";");
					if(current_batsmen.get(0).getStrikeRate() != null) {
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStrikeRate1 " + current_batsmen.get(0).getStrikeRate() + ";");
					}else {
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStrikeRate1 " + "-" + ";");
					}
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tMinutes1 " + current_batsmen.get(0).getSeconds()/60 + ";");
					
					if(inn.getPartnerships() != null) {
						if(inn.getPartnerships().size() > 0) {
							if (inn.getPartnerships().get(inn.getPartnerships().size() - 1).getFirstBatterNo() == current_batsmen.get(0).getPlayerId())
							{
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPartnershipContribution1 " + 
										inn.getPartnerships().get(inn.getPartnerships().size() - 1).getFirstBatterRuns() + "(" + 
										inn.getPartnerships().get(inn.getPartnerships().size() - 1).getFirstBatterBalls() + ")" + ";");
							}
							else if (inn.getPartnerships().get(inn.getPartnerships().size() - 1).getSecondBatterNo() == current_batsmen.get(0).getPlayerId())
							{
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPartnershipContribution1 " + 
										inn.getPartnerships().get(inn.getPartnerships().size() - 1).getSecondBatterRuns() + "(" + 
										inn.getPartnerships().get(inn.getPartnerships().size() - 1).getSecondBatterBalls() + ")" + ";");
							}
						}
					}
					
					
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBatterName2 " + current_batsmen.get(1).getPlayer().getTicker_name() + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBatterRun2 " + current_batsmen.get(1).getRuns() + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBatterBall2 "+ current_batsmen.get(1).getBalls() + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBoundaries2 "+ current_batsmen.get(1).getFours() + "/" + current_batsmen.get(1).getSixes() + ";");
					if(current_batsmen.get(1).getStrikeRate() != null) {
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStrikeRate2 "+ current_batsmen.get(1).getStrikeRate() + ";");
					}else {
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStrikeRate2 "+ "-" + ";");
					}
					
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tMinutes2 "+ current_batsmen.get(1).getSeconds()/60 + ";");
					
					if(inn.getPartnerships() != null) {
						if(inn.getPartnerships().size() > 0) {
							if (inn.getPartnerships().get(inn.getPartnerships().size() - 1).getFirstBatterNo() == current_batsmen.get(1).getPlayerId())
							{
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPartnershipContribution2 " + 
										inn.getPartnerships().get(inn.getPartnerships().size() - 1).getFirstBatterRuns() + "(" + 
										inn.getPartnerships().get(inn.getPartnerships().size() - 1).getFirstBatterBalls() + ")" + ";");
							}
							else if (inn.getPartnerships().get(inn.getPartnerships().size() - 1).getSecondBatterNo() == current_batsmen.get(1).getPlayerId())
							{
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPartnershipContribution2 " + 
										inn.getPartnerships().get(inn.getPartnerships().size() - 1).getSecondBatterRuns() + "(" + 
										inn.getPartnerships().get(inn.getPartnerships().size() - 1).getSecondBatterBalls() + ")" + ";");
							}
						}
					}
					
					if(current_batsmen.get(0).getStatus().toUpperCase().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
						if(current_batsmen.get(0).getOnStrike().equalsIgnoreCase(CricketUtil.YES)) {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectStriker1" + " " + "1" + ";");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectStriker2" + " " + "0" + ";");
							//processAnimation(print_writer, "Batsman1DeHighlight", "SHOW 0.0", session_selected_broadcaster,1);
						}
					}
					if(current_batsmen.get(1).getStatus().toUpperCase().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
						if(current_batsmen.get(1).getOnStrike().equalsIgnoreCase(CricketUtil.YES)) {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectStriker1" + " " + "0" + ";");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectStriker2" + " " + "1" + ";");
							//processAnimation(print_writer, "Batsman2DeHighlight", "SHOW 0.0", session_selected_broadcaster,1);
						}	
					}
					
					
					
					//CRR And RRR
					
					if(inn.getInningNumber() == 1) {
						if(inn.getRunRate() != null) {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tRunRate " + 
									"CRR: " + inn.getRunRate() + ";");
						}else {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tRunRate " + 
									"CRR: " + "-" + ";");
						}
						
					}else if(inn.getInningNumber() == 2) {
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tRunRate " + 
								"RRR: " + CricketFunctions.generateRunRate(CricketFunctions.getRequiredRuns(match), 0, CricketFunctions.getRequiredBalls(match), 2) + ";");
					}
					
					
					// Current Partnership
					if(inn.getPartnerships() != null) {
						if(inn.getPartnerships().size() > 0) {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPartnershipHead " + 
									"CURR P'SHIP" + ";");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPartnershipRus " + 
									inn.getPartnerships().get(inn.getPartnerships().size() - 1).getTotalRuns() + ";");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPartnershipBalls " + 
									inn.getPartnerships().get(inn.getPartnerships().size() - 1).getTotalBalls() + ";");
						}
					}
				}
			}
		}
			
		infobar.setLast_batsmen(current_batsmen);
		return infobar;
	}
	public Infobar populateInfobarTopRight(Infobar infobar,boolean is_this_updating, PrintWriter print_writer, 
			MatchAllData match, String broadcaster) throws InterruptedException
	{
		if(is_this_updating == false) {
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBowlerName2 " + "-" + ";");
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFigure2 " + "-" + ";");
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tOvers2 " + "-" + ";");
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tDots2 " + "-" + ";");
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tEconomy2 " + "-" + ";");
		}
			//print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$AllSection$Section1_2$BowlerbaseGrp$TopLIne*ACTIVE SET " + "0" + "\0");
		for(Inning inn : match.getMatch().getInning()) {
			if (inn.getIsCurrentInning().toUpperCase().equalsIgnoreCase(CricketUtil.YES)) {
				if(inn.getBowlingCard() != null) {
					for(BowlingCard boc : inn.getBowlingCard()) {
						if(boc.getStatus().toUpperCase().equalsIgnoreCase("CURRENTBOWLER")) {
							if(infobar.getLast_bowler() == null || infobar.getLast_bowler().getPlayerId() != boc.getPlayerId()) {
								//processAnimation(print_writer, "BowlerChangeOut", "START", session_selected_broadcaster,1);
								//TimeUnit.SECONDS.sleep(1);
							}
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectBowler1" + " " + "1" + ";");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectBowler2" + " " + "0" + ";");
							
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBowlerName1 " + boc.getPlayer().getTicker_name() + ";");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFigure1 " + boc.getWickets() + "-" + boc.getRuns() + ";");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tOvers1 " + CricketFunctions.OverBalls(boc.getOvers(), boc.getBalls()) + ";");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tDots1 " + boc.getDots() + ";");
							
							if(boc.getEconomyRate() == null) {
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tEconomy1 " + "-" + ";");
							}else {
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tEconomy1 " + boc.getEconomyRate() + ";");
							}
							if(infobar.getLast_bowler() == null || infobar.getLast_bowler().getPlayerId() != boc.getPlayerId()) {
								//processAnimation(print_writer, "BowlerChangeIn", "START", session_selected_broadcaster,1);
							}
							infobar.setLast_bowler(boc);
							infobar.setLast_bottom_right_top_section(CricketUtil.BOWLER);
						}
						
						if(CricketFunctions.previousBowler(match, match.getEventFile().getEvents()) != "") {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBowlerName2 " + CricketFunctions.previousBowler(match, match.getEventFile().getEvents()).split(",")[0] + ";");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFigure2 " + CricketFunctions.previousBowler(match, match.getEventFile().getEvents()).split(",")[1] + ";");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tOvers2 " + CricketFunctions.previousBowler(match, match.getEventFile().getEvents()).split(",")[4] + ";");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tDots2 " + CricketFunctions.previousBowler(match, match.getEventFile().getEvents()).split(",")[2] + ";");
							
							if(CricketFunctions.previousBowler(match, match.getEventFile().getEvents()).split(",")[3] == "") {
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tEconomy2 " + "-" + ";");
							}else {
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tEconomy2 " + CricketFunctions.previousBowler(match, match.getEventFile().getEvents()).split(",")[3] + ";");
							}
						}
					}
				}
			}
		}
		return infobar;
	}
	
	/*public String resetAnimation(PrintWriter print_writer,String which_broadcaster, String which_graphic) {
		String status = "";
		
		switch(which_broadcaster) {
		case "FRUIT":
			switch(which_graphic) {
			case "INFOBAR":
				print_writer.println("LAYER1*EVEREST*STAGE*DIRECTOR*Batsman1ChangeOut SHOW 0.0;");
				print_writer.println("LAYER1*EVEREST*STAGE*DIRECTOR*Batsman1DeHighlight SHOW 0.0;");
				print_writer.println("LAYER1*EVEREST*STAGE*DIRECTOR*Batsman2ChangeOut SHOW 0.0;");
				print_writer.println("LAYER1*EVEREST*STAGE*DIRECTOR*Batsman2DeHighlight SHOW 0.0;");
				print_writer.println("LAYER1*EVEREST*STAGE*DIRECTOR*Batsman1ChangeIn SHOW 0.0;");
				print_writer.println("LAYER1*EVEREST*STAGE*DIRECTOR*Batsman2ChangeIn SHOW 0.0;");
				print_writer.println("LAYER1*EVEREST*STAGE*DIRECTOR*In STOP;");
				print_writer.println("LAYER1*EVEREST*STAGE*DIRECTOR*In SHOW 0.0;");
				break;
			}
			break;
		}
		
		return status;
	}
	
	public String processVariousAnimation(PrintWriter print_writer,String which_broadcaster,Match match ,String which_graphic, List<BattingCard> infobar_batsman) throws InterruptedException {
		String status = "";
		
		switch(which_broadcaster) {
		case "FRUIT":
			switch(which_graphic) {
			case "INFOBAR_TOPLEFT":
				List<BattingCard> batsman = new ArrayList<BattingCard>();
				for(Inning inn : match.getInning()) {
					if (inn.getIsCurrentInning().toUpperCase().equalsIgnoreCase(CricketUtil.YES)) {
						for (BattingCard bc : inn.getBattingCard()) {
							if(inn.getPartnerships() != null && inn.getPartnerships().size() > 0) {
								if(bc.getPlayerId() == inn.getPartnerships().get(inn.getPartnerships().size() - 1).getFirstBatterNo()) {
									batsman.add(bc);
								}
								else if(bc.getPlayerId() == inn.getPartnerships().get(inn.getPartnerships().size() - 1).getSecondBatterNo()) {
									batsman.add(bc);
								}
							}
						}
						
						if(infobar_batsman == null || infobar_batsman.size() <= 0) {
							infobar_batsman = batsman;
						}
						
						if(infobar_batsman != null && infobar_batsman.size() >= 1 && batsman != null && batsman.size() >= 1) {
							if(infobar_batsman.get(0).getPlayerId() != batsman.get(0).getPlayerId()) {
								processAnimation(print_writer, "Batsman1ChangeOut", "START", which_broadcaster);
							}
							if(infobar_batsman.get(1).getPlayerId() != batsman.get(1).getPlayerId()) {
								processAnimation(print_writer, "Batsman2ChangeOut", "START", which_broadcaster);
							}
						}
						if(batsman != null && batsman.size() >= 1) {
							
							if(infobar_batsman != null && infobar_batsman.size() >= 1) {
								if(infobar_batsman.get(0).getStatus().equalsIgnoreCase(CricketUtil.OUT)) {
									processAnimation(print_writer, "Batsman1DeHighlight", "SHOW 250.0", which_broadcaster);
								}
								else if(infobar_batsman.get(0).getStatus().equalsIgnoreCase(CricketUtil.NOT_OUT)){
									processAnimation(print_writer, "Batsman1DeHighlight", "SHOW 0.0", which_broadcaster);
								}
							}
							
							if(infobar_batsman != null && infobar_batsman.size() >= 1) {
								if(infobar_batsman.get(1).getStatus().equalsIgnoreCase(CricketUtil.OUT)) {	
									processAnimation(print_writer, "Batsman2DeHighlight", "SHOW 250.0", which_broadcaster);
								}
								else if(infobar_batsman.get(1).getStatus().equalsIgnoreCase(CricketUtil.NOT_OUT)){
									processAnimation(print_writer, "Batsman2DeHighlight", "SHOW 0.0", which_broadcaster);
								}
							}
						}
						
						if(infobar_batsman != null && infobar_batsman.size() >= 1 && batsman != null && batsman.size() >= 1) {
							if(infobar_batsman.get(0).getPlayerId() != batsman.get(0).getPlayerId()) {
								processAnimation(print_writer, "Batsman1ChangeIn", "START", which_broadcaster);
							}
							if(infobar_batsman.get(1).getPlayerId() != batsman.get(1).getPlayerId()) {
								processAnimation(print_writer, "Batsman2ChangeIn", "START", which_broadcaster);
							}
						}
						TimeUnit.SECONDS.sleep(1);
					}
				}
				break;
			}
			break;
		}
		
		return status;
	}*/
	public Infobar populateInfobarBottomLeft(Infobar infobar,boolean is_this_updating, PrintWriter print_writer, MatchAllData match, String session_selected_broadcaster)
	{
		switch (session_selected_broadcaster.toUpperCase()) {
		case "FRUIT":
			switch(infobar.getBottom_left_section().toUpperCase()) {
			case "WHICH_INNING":
				if(is_this_updating == false) {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vSection6Selection " + "6" + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vSection4Selection " + "1" + ";");
				}
				//print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tHowOut " + " "+ ";");
				for(Inning inn : match.getMatch().getInning()) {
					if (inn.getIsCurrentInning().toUpperCase().equalsIgnoreCase(CricketUtil.YES)) {
						//System.out.println("Number - " + inn.getInningNumber());
						if(inn.getInningNumber() == 1 || inn.getInningNumber() == 2) {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tFreeTextSec4 " + "1st INNINGS" + ";");
						}else if(inn.getInningNumber() == 3 || inn.getInningNumber() == 4) {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tFreeTextSec4 " + "2nd INNINGS" + ";");
						}
					}
				}
				break;
			case "DAY_SESSION":
				if(is_this_updating == false) {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vSection6Selection " + "6" + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vSection4Selection " + "1" + ";");
				}
				//print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tHowOut " + " "+ ";");
				if(match.getMatch().getDaysSessions().get(match.getMatch().getDaysSessions().size()-1).getIsCurrentSession().equalsIgnoreCase(CricketUtil.YES)) {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tFreeTextSec4 " + 
							"DAY " + match.getMatch().getDaysSessions().get(match.getMatch().getDaysSessions().size()-1).getDayNumber() + "- SESSION " + 
							match.getMatch().getDaysSessions().get(match.getMatch().getDaysSessions().size()-1).getSessionNumber() + ";");
				}
				
				break;
			case "VS_BOWLING_TEAM":
				for(Inning inn : match.getMatch().getInning()) {
					if (inn.getIsCurrentInning().toUpperCase().equalsIgnoreCase(CricketUtil.YES)) {
						if(is_this_updating == false) {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vSection6Selection " + "6" + ";");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vSection4Selection " + "2" + ";");
						}
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tBallTeamName " + 
								inn.getBowling_team().getTeamName4().toUpperCase() + ";");
					}
				}
				break;
			case "CURRENT_RUN_RATE":
				for(Inning inn : match.getMatch().getInning()) {
					if(is_this_updating == false) {
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vSection6Selection " + "6" + ";");
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vSection4Selection " + "4" + ";");
					}
					if(inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)) {
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tCurrentRunRateText " + "CRR @ " + ";");
						if(inn.getRunRate() != null) {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tCurrentRunRate " + inn.getRunRate() + ";");
						}else {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tCurrentRunRate " + "0.0" + ";");
						}
						
					}
				}
				break;
			case"TARGET":
				if(is_this_updating == false) {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vSection6Selection " + "6" + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vSection4Selection " + "3" + ";");
				}
				//print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tCurrentRunRateText " + "CRR@ " + ";");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tTargetScore " + CricketFunctions.getTargetRuns(match) + ";");
				break;
			case"REQUIRED_RUN_RATE":
				if(is_this_updating == false) {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vSection6Selection " + "6" + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vSection4Selection " + "5" + ";");
				}
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tRequiredRunRateText " + "RRR @ " + ";");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tRequiredRunRate " + 
					CricketFunctions.generateRunRate(CricketFunctions.getRequiredRuns(match), 0, CricketFunctions.getRequiredBalls(match), 2) + ";");
				break;
			}
			break;
		}
		infobar.setLast_bottom_left_section(infobar.getBottom_left_section().toUpperCase());
		return infobar;
	}
	public Infobar populateInfobarBottomRight(Infobar infobar,boolean is_this_updating, PrintWriter print_writer, MatchAllData match, String session_selected_broadcaster)
	{
		switch (session_selected_broadcaster.toUpperCase()) {
		case "FRUIT":
			switch(infobar.getBottom_right_section().toUpperCase()) {
			case "TOSS_WINNING":
				if(is_this_updating == false) {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vSection6Selection " + "6" + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vSection5Selection " + "0" + ";");
				}
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tTossResult " + CricketFunctions.generateTossResult(match, "", "", CricketUtil.FULL,CricketUtil.CHOSE).toUpperCase() + ";");
				break;
			case "BALL_SINCE":
				for(Inning inn : match.getMatch().getInning()) {
					if(inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)) {
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tFreeTextSmallText " + "BALLS SINCE LAST BOUNDARY - "  + 
								CricketFunctions.lastFewOversData(CricketUtil.BOUNDARY, match.getEventFile().getEvents(),inn.getInningNumber()) + ";");
					}
				}
				
				if(is_this_updating == false) {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vSection6Selection " + "6" + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vSection5Selection " + "1" + ";");
				}
				break;	
			case "SUPER_OVER":
				if(is_this_updating == false) {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vSection6Selection " + "6" + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vSection5Selection " + "1" + ";");
				}
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tFreeTextSmallText " + "SUPER OVER" + ";");
				break;
			case "TOURNAMENT_NAME":
				if(is_this_updating == false) {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vSection6Selection " + "6" + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vSection5Selection " + "1" + ";");
				}
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tFreeTextSmallText " + match.getSetup().getTournament().toUpperCase() + ";");
				break;
			case "VENUE_NAME":
				if(is_this_updating == false) {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vSection6Selection " + "6" + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vSection5Selection " + "1" + ";");
				}
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tFreeTextSmallText " + match.getSetup().getVenueName().toUpperCase() + ";");
				break;
			case "PAR_SCORE":
				if(is_this_updating == false) {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vSection6Selection " + "6" + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vSection5Selection " + "1" + ";");
				}
				String balls=""; 
				Document htmlFile = null; 
				try { 
					for(Inning inn : match.getMatch().getInning()) {
						if (inn.getIsCurrentInning().toUpperCase().equalsIgnoreCase(CricketUtil.YES)) {
							//int totalball = 0;
							//totalball =((inn.getTotalOvers()*6) + inn.getTotalBalls());
							
							htmlFile = Jsoup.parse(new File("C:\\Sports\\ParScores BB.html"), "ISO-8859-1");
							balls = CricketFunctions.OverBalls(inn.getTotalOvers(), inn.getTotalBalls());
							
//							if(totalball < 42) {
//								htmlFile = Jsoup.parse(new File("C:\\Sports\\ParScores BB.html"), "ISO-8859-1");
//								balls = CricketFunctions.OverBalls(inn.getTotalOvers(), inn.getTotalBalls());
//
//							}else if(totalball >= 42) {
//								htmlFile = Jsoup.parse(new File("C:\\Sports\\ParScores OO.html"), "ISO-8859-1");
//								balls = String.valueOf(inn.getTotalOvers());
//							}
						}
					}
				} catch (IOException e) {  
					e.printStackTrace(); 
				} 
				
//				String h1 = htmlFile.body().getElementsByTag("font").text();
				
				List<DuckWorthLewis> this_dls = new ArrayList<DuckWorthLewis>();
				for(int i=14; i<htmlFile.body().getElementsByTag("font").size() - 1;i++) {
					if(htmlFile.body().getElementsByTag("font").get(i).text().contains("TableID")) {
						i = i + 15;
						if(i > htmlFile.body().getElementsByTag("font").size()) {
							break;
						}
					}
					
					for(Inning inn : match.getMatch().getInning()) {
						if (inn.getIsCurrentInning().toUpperCase().equalsIgnoreCase(CricketUtil.YES)) {
							//System.out.println(" i = " + (i+(1+(inn.getTotalWickets()))));
							this_dls.add(new DuckWorthLewis(htmlFile.body().getElementsByTag("font").get(i).text(),
									htmlFile.body().getElementsByTag("font").get(i+(2+(inn.getTotalWickets()))).text()));
						}
					}
//					this_dls.add(new DuckWorthLewis(htmlFile.body().getElementsByTag("font").get(i).text(),
//							htmlFile.body().getElementsByTag("font").get(i+(1+(Integer.valueOf(wkts)))).text()));
					i = i +11;
					
				}
				for(int i = 0; i<= this_dls.size() -1;i++) {
					if(this_dls.get(i).getOver_left().equalsIgnoreCase(balls)) {
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tFreeTextSmallText " + "DLS PAR SCORE AFTER "
								+ balls + " OVERS: " + (Integer.valueOf(this_dls.get(i).getWkts_down())+1) + ";");
					}
				}

				break;
			case "EQUATION":
				if(is_this_updating == false) {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vSection6Selection " + "6" + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vSection5Selection " + "3" + ";");
				}
				if(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.TEST)) {
					if(match.getMatch().getInning().get(1).getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)) {
						if(is_this_updating == false) {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vSection5Selection " + "1" + ";");
						}
						if(CricketFunctions.getTeamRunsAhead(2,match) > 0) {

							print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tFreeTextSmallText " + 
									match.getMatch().getInning().get(1).getBatting_team().getTeamName2() + " LEAD BY " + CricketFunctions.getTeamRunsAhead(2,match) + " RUN" + CricketFunctions.Plural(CricketFunctions.getTeamRunsAhead(2,match)).toUpperCase() + ";");
						} else if(CricketFunctions.getTeamRunsAhead(2,match) == 0) {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tFreeTextSmallText " + "SCORES ARE LEVEL" + ";");
						} else {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tFreeTextSmallText " + 
									match.getMatch().getInning().get(1).getBatting_team().getTeamName2() + " TRAIL BY " + (-1 * CricketFunctions.getTeamRunsAhead(2,match)) + " RUN" + CricketFunctions.Plural(-1 * CricketFunctions.getTeamRunsAhead(2,match)).toUpperCase() + ";");
						}
					}else if(match.getMatch().getInning().get(2).getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)) {
						if(is_this_updating == false) {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vSection5Selection " + "1" + ";");
						}
						if(CricketFunctions.getTeamRunsAhead(3,match) > 0) {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tFreeTextSmallText " + 
									match.getMatch().getInning().get(2).getBatting_team().getTeamName2() + " LEAD BY " + CricketFunctions.getTeamRunsAhead(3,match) + " RUN" + CricketFunctions.Plural(CricketFunctions.getTeamRunsAhead(3,match)).toUpperCase() + ";");
						} else if(CricketFunctions.getTeamRunsAhead(3,match) == 0) {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tFreeTextSmallText " + "SCORES ARE LEVEL" + ";");
						} else {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tFreeTextSmallText " + 
									match.getMatch().getInning().get(2).getBatting_team().getTeamName2() + " TRAIL BY " + (-1 * CricketFunctions.getTeamRunsAhead(3,match)) + " RUN" + CricketFunctions.Plural(-1 * CricketFunctions.getTeamRunsAhead(3,match)).toUpperCase() + ";");
						}
					}else if(match.getMatch().getInning().get(3).getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)) {
						if(is_this_updating == false) {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vSection5Selection " + "1" + ";");
						}
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tFreeTextSmallText " + match.getMatch().getMatchStatus().toUpperCase() + ";");
					}
				}else {
					if(CricketFunctions.getRequiredRuns(match) == 0 || match.getMatch().getInning().get(1).getTotalWickets() >= 10 || CricketFunctions.getRequiredBalls(match) == 0) {
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vSection5Selection " + "1" + ";");
						
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tFreeTextSmallText " + CricketFunctions.generateMatchSummaryStatus(2, match, CricketUtil.FULL).toUpperCase() + ";");
					}else {
						if(match.getSetup().getTargetOvers() == null || match.getSetup().getTargetOvers().trim().isEmpty() && match.getSetup().getTargetRuns() == 0) {
							if(CricketFunctions.getRequiredRuns(match) == 0) {
								if(match.getMatch().getMatchStatus() != null) {
									if(match.getMatch().getMatchResult().toUpperCase().equalsIgnoreCase("DRAWN")){
										print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vSection5Selection " + "1" + ";");
										
										print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tFreeTextSmallText " + "MATCH TIED" + ";");
									}
									else if(match.getMatch().getMatchResult().split(",")[1].toUpperCase().equalsIgnoreCase("SUPER_OVER")){
										print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vSection5Selection " + "1" + ";");
										
										print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tFreeTextSmallText " + match.getMatch().getMatchStatus().toUpperCase() + ";");
									}
									else {
										print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vSection5Selection " + "1" + ";");
										
										print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tFreeTextSmallText " + match.getMatch().getMatchStatus().toUpperCase() + ";");
									}
								}

							}else if(CricketFunctions.getRequiredRuns(match) > 0 && match.getMatch().getInning().get(1).getTotalWickets() >= 10 
									|| match.getMatch().getInning().get(1).getTotalOvers() >= match.getSetup().getMaxOvers()) 
							{
								if(match.getMatch().getMatchStatus() != null) {
									if(match.getMatch().getMatchResult().toUpperCase().equalsIgnoreCase("DRAWN")){
										print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vSection5Selection " + "1" + ";");
										
										print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tFreeTextSmallText " + "MATCH TIED" + ";");
									}
									else if(match.getMatch().getMatchResult().split(",")[1].toUpperCase().equalsIgnoreCase("SUPER_OVER")){
										print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vSection5Selection " + "1" + ";");
										
										print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tFreeTextSmallText " + match.getMatch().getMatchStatus().toUpperCase() + ";");
									}
									else {
										print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vSection5Selection " + "1" + ";");
										
										print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tFreeTextSmallText " + match.getMatch().getMatchStatus().toUpperCase() + ";");
									}
								}
							}
							else{
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vSection5Selection " + "3" + ";");
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tNeedRun " + CricketFunctions.getRequiredRuns(match) + ";");
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tRuns " + "RUN" + CricketFunctions.Plural(CricketFunctions.
										getRequiredRuns(match)).toUpperCase() + ";");
								if (CricketFunctions.getRequiredBalls(match) >= 100) {
									print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tNeedBall " + CricketFunctions.OverBalls(0,CricketFunctions.getRequiredBalls(match)) + ";");
									print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tBalls " + "OVERS" + ";");
								}else {
									print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tNeedBall " + CricketFunctions.getRequiredBalls(match) + ";");
									print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tBalls " + "BALL" + CricketFunctions.Plural(CricketFunctions.getRequiredBalls(match)).toUpperCase() + ";");
								}
								
							}
						}else {
							if(Double.valueOf(match.getSetup().getTargetOvers()) == 1 && match.getSetup().getTargetRuns() == 0) {
								if(CricketFunctions.getRequiredRuns(match) == 0) {
									if(match.getMatch().getMatchStatus() != null) {
										if(match.getMatch().getMatchResult().toUpperCase().equalsIgnoreCase("DRAWN")){
											print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vSection5Selection " + "1" + ";");
											
											print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tFreeTextSmallText " + "MATCH TIED" + ";");
										}
										else if(match.getMatch().getMatchResult().split(",")[1].toUpperCase().equalsIgnoreCase("SUPER_OVER")){
											print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vSection5Selection " + "1" + ";");
											
											print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tFreeTextSmallText " + match.getMatch().getMatchStatus().toUpperCase() + ";");
										}
										else {
											print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vSection5Selection " + "1" + ";");
											
											print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tFreeTextSmallText " + match.getMatch().getMatchStatus().toUpperCase() + ";");
										}
									}

								}else if(CricketFunctions.getRequiredRuns(match) > 0 && match.getMatch().getInning().get(1).getTotalWickets() >= 10 
										|| match.getMatch().getInning().get(1).getTotalOvers() >= match.getSetup().getMaxOvers()) 
								{
									if(match.getMatch().getMatchStatus() != null) {
										if(match.getMatch().getMatchResult().toUpperCase().equalsIgnoreCase("DRAWN")){
											print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vSection5Selection " + "1" + ";");
											
											print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tFreeTextSmallText " + "MATCH TIED" + ";");
										}
										else if(match.getMatch().getMatchResult().split(",")[1].toUpperCase().equalsIgnoreCase("SUPER_OVER")){
											print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vSection5Selection " + "1" + ";");
											
											print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tFreeTextSmallText " + match.getMatch().getMatchStatus().toUpperCase() + ";");
										}
										else {
											print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vSection5Selection " + "1" + ";");
											
											print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tFreeTextSmallText " + match.getMatch().getMatchStatus().toUpperCase() + ";");
										}
									}
								}
								else{
									print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vSection5Selection " + "3" + ";");
									
									print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tNeedRun " + CricketFunctions.getRequiredRuns(match) + ";");
									print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tRuns " + "RUN" + CricketFunctions.Plural(CricketFunctions.
											getRequiredRuns(match)).toUpperCase() + ";");
									if (CricketFunctions.getRequiredBalls(match) >= 100) {
										print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tNeedBall " + CricketFunctions.OverBalls(0,CricketFunctions.getRequiredBalls(match)) + ";");
										print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tBalls " + "OVERS" + ";");
									}else {
										print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tNeedBall " + CricketFunctions.getRequiredBalls(match) + ";");
										print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tBalls " + "BALL" + CricketFunctions.Plural(CricketFunctions.getRequiredBalls(match)).toUpperCase() + ";");
									}
								}
							}else if(Double.valueOf(match.getSetup().getTargetOvers()) != 0 && match.getSetup().getTargetRuns() == 0) {
								if(CricketFunctions.getRequiredRuns(match) == 0) {
									if(match.getMatch().getMatchStatus() != null) {
										if(match.getMatch().getMatchResult().toUpperCase().equalsIgnoreCase("DRAWN")){
											print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vSection5Selection " + "1" + ";");
											
											print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tFreeTextSmallText " + "MATCH TIED" + ";");
										}
										else if(match.getMatch().getMatchResult().split(",")[1].toUpperCase().equalsIgnoreCase("SUPER_OVER")){
											print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vSection5Selection " + "1" + ";");
											
											print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tFreeTextSmallText " + match.getMatch().getMatchStatus().toUpperCase() + ";");
										}
										else {
											print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vSection5Selection " + "1" + ";");
											
											print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tFreeTextSmallText " + match.getMatch().getMatchStatus().toUpperCase() + ";");
										}
									}

								}else if(CricketFunctions.getRequiredRuns(match) > 0 && match.getMatch().getInning().get(1).getTotalWickets() >= 10 
										|| match.getMatch().getInning().get(1).getTotalOvers() >= match.getSetup().getMaxOvers()) 
								{
									if(match.getMatch().getMatchStatus() != null) {
										if(match.getMatch().getMatchResult().toUpperCase().equalsIgnoreCase("DRAWN")){
											print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vSection5Selection " + "1" + ";");
											
											print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tFreeTextSmallText " + "MATCH TIED" + ";");
										}
										else if(match.getMatch().getMatchResult().split(",")[1].toUpperCase().equalsIgnoreCase("SUPER_OVER")){
											print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vSection5Selection " + "1" + ";");
											
											print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tFreeTextSmallText " + match.getMatch().getMatchStatus().toUpperCase() + ";");
										}
										else {
											print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vSection5Selection " + "1" + ";");
											
											print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tFreeTextSmallText " + match.getMatch().getMatchStatus().toUpperCase() + ";");
										}
									}
								}
								else{
									print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vSection5Selection " + "3" + ";");
									
									print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tNeedRun " + CricketFunctions.getRequiredRuns(match) + ";");
									print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tRuns " + "RUN" + CricketFunctions.Plural(CricketFunctions.
											getRequiredRuns(match)).toUpperCase() + ";");
									if (CricketFunctions.getRequiredBalls(match) >= 100) {
										print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tNeedBall " + CricketFunctions.OverBalls(0,CricketFunctions.getRequiredBalls(match)) + ";");
										print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tBalls " + "OVERS" + ";");
									}else {
										print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tNeedBall " + CricketFunctions.getRequiredBalls(match) + ";");
										print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tBalls " + "BALL" + CricketFunctions.Plural(CricketFunctions.getRequiredBalls(match)).toUpperCase() + ";");
									}
								}
							}
							else {
								if((match.getSetup().getTargetRuns() - match.getMatch().getInning().get(1).getTotalRuns()) == 0) {
									if(match.getMatch().getMatchResult() != null) {
										if(match.getMatch().getMatchResult().toUpperCase().equalsIgnoreCase("DRAWN")){
											print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vSection5Selection " + "1" + ";");
											
											print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tFreeTextSmallText " + "MATCH TIED" + ";");
										}
										else if(match.getMatch().getMatchResult().split(",")[1].toUpperCase().equalsIgnoreCase("SUPER_OVER")){
											print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vSection5Selection " + "1" + ";");
											
											print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tFreeTextSmallText " + match.getMatch().getMatchStatus().toUpperCase() + ";");
										}
										else {
											print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vSection5Selection " + "1" + ";");
											
											print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tFreeTextSmallText " + match.getMatch().getMatchStatus().toUpperCase() + ";");
										}
									}
									
								}else if((match.getSetup().getTargetRuns() - match.getMatch().getInning().get(1).getTotalRuns()) > 0 && match.getMatch().getInning().get(1).getTotalWickets() >= 10 
										|| Double.valueOf(CricketFunctions.OverBalls(match.getMatch().getInning().get(1).getTotalOvers(), match.getMatch().getInning().get(1).getTotalBalls())) 
										>= Double.valueOf(match.getSetup().getTargetOvers())) 
								{
									if(match.getMatch().getMatchStatus() != null) {
										if(match.getMatch().getMatchResult().toUpperCase().equalsIgnoreCase("DRAWN")){
											print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vSection5Selection " + "1" + ";");
											
											print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tFreeTextSmallText " + "MATCH TIED" + ";");
										}
										else if(match.getMatch().getMatchResult().split(",")[1].toUpperCase().equalsIgnoreCase("SUPER_OVER")){
											print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vSection5Selection " + "1" + ";");
											
											print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tFreeTextSmallText " + match.getMatch().getMatchStatus().toUpperCase() + ";");
										}
										else {
											print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vSection5Selection " + "1" + ";");
											
											print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tFreeTextSmallText " + match.getMatch().getMatchStatus().toUpperCase() + ";");
										}
									}
								}
								else{
									print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vSection5Selection " + "3" + ";");
									if(match.getSetup().getTargetType() != null && !match.getSetup().getTargetType().isEmpty()) {
										print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tNeedRun " + CricketFunctions.getRequiredRuns(match) + ";");
										print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tRuns " + "RUN" + CricketFunctions.Plural(CricketFunctions.
												getRequiredRuns(match)).toUpperCase() + ";");
										if (CricketFunctions.getRequiredBalls(match) >= 100) {
											print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tNeedBall " + CricketFunctions.OverBalls(0,CricketFunctions.getRequiredBalls(match)) + ";");
											print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tBalls " + "OVERS" + " (" + match.getSetup().getTargetType().toUpperCase() + ")" + ";");
										}else {
											print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tNeedBall " + CricketFunctions.getRequiredBalls(match) + ";");
											print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tBalls " + "BALL" + CricketFunctions.Plural(CricketFunctions.
													getRequiredBalls(match)).toUpperCase() + " (" + match.getSetup().getTargetType().toUpperCase() + ")" + ";");
										}
									}
									else {
										print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tNeedRun " + CricketFunctions.getRequiredRuns(match) + ";");
										print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tRuns " + "RUN" + CricketFunctions.Plural(CricketFunctions.
												getRequiredRuns(match)).toUpperCase().toUpperCase() + ";");
										if (CricketFunctions.getRequiredBalls(match) >= 100) {
											print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tNeedBall " + CricketFunctions.OverBalls(0,CricketFunctions.getRequiredBalls(match)) + ";");
											print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tBalls " + "OVERS" + ";");
										}else {
											print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tNeedBall " + CricketFunctions.getRequiredBalls(match) + ";");
											print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tBalls " + "BALL" + CricketFunctions.Plural(CricketFunctions.getRequiredBalls(match)).toUpperCase() + ";");
										}
									}
								}
							}
						}
					}
				}
				break;
			case "LAST_WICKET":
				if(is_this_updating == false) {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vSection6Selection " + "6" + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vSection5Selection " + "1" + ";");
				}
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tFreeTextSmallText " + "LAST WICKET : " + CricketFunctions.getLastWicket(match) + ";");
				
				break;
			case "REMAINING_OVERS":
				int daysnumber=0;
				int over_bowled=0,remain_overs=0;
				if(is_this_updating == false) {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vSection6Selection " + "6" + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vSection5Selection " + "1" + ";");
				}
				daysnumber = match.getMatch().getDaysSessions().get(match.getMatch().getDaysSessions().size()-1).getDayNumber();
				for(DaySession day_session : match.getMatch().getDaysSessions()) {
					if(daysnumber == day_session.getDayNumber()) {
						over_bowled = over_bowled + day_session.getTotalBalls();
					}
				}
				remain_overs = (90 * 6) - over_bowled;
				if(remain_overs % 6 == 0) {
					remain_overs = remain_overs / 6 ;
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tFreeTextSmallText " + "REMAINING OVERS : " + remain_overs + ";");
					
				}else {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tFreeTextSmallText " + "REMAINING OVERS : " + CricketFunctions.OverBalls(0, remain_overs) + ";");
				}
				
				break;
				
			case "FOLLOW_ON":
				int rem_runs = 0;
				if(is_this_updating == false) {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vSection6Selection " + "6" + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vSection5Selection " + "1" + ";");
				}
				rem_runs = ((match.getMatch().getInning().get(0).getTotalRuns() - match.getSetup().getFollowOnThreshold()) + 1 );
				rem_runs = rem_runs - match.getMatch().getInning().get(1).getTotalRuns();
				if(rem_runs > 0) {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tFreeTextSmallText " + "RUNS TO AVOID FOLLOW-ON : " + rem_runs + ";");
				}else {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tFreeTextSmallText " + "FOLLOW-ON AVOIDED" + ";");
				}
				break;
			
			case"COMPARISION":
				if(is_this_updating == false) {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vSection6Selection " + "6" + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vSection5Selection " + "2" + ";");
				}
				for(Inning inn : match.getMatch().getInning()) {
					if(inn.getInningNumber() == 1 & inn.getIsCurrentInning().toUpperCase().equalsIgnoreCase("NO")) {
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tCompHead " + "AT THIS STAGE :" + ";");
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tBallTeamName " + match.getMatch().getInning().get(0).getBatting_team().getTeamName1().toUpperCase() + ";");
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tBallTeamScore " + CricketFunctions.compareInningData(match,"/", 1 , match.getEventFile().getEvents()) + ";");
					}
				}
				break;
			}
			break;
		}
		infobar.setLast_bottom_right_section(infobar.getBottom_right_section().toUpperCase());
		return infobar;
	}	
	public void populateInfobarPrompt(boolean is_this_updating, PrintWriter print_writer, InfobarStats ibs, MatchAllData match, String session_selected_broadcaster)
	{
		switch (session_selected_broadcaster.toUpperCase()) {
		case "FRUIT":
			
			if(is_this_updating == false) {
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vSection6Selection " + "6" + ";");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vSection5Selection " + "1" + ";");
				
				if(ibs.getText2() == null) {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tFreeTextSmallText " + ibs.getText1() + ";");
				}else if(ibs.getText1() == null)  {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tFreeTextSmallText " + ibs.getText2() + ";");
				}else {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tFreeTextSmallText " + ibs.getText1() + "-" + ibs.getText2()+ ";");
				}
				infobar.setLast_bottom_right_section("INFOBAR-PROMPT");
			}
			break;
		}
	}	
	public Infobar populateInfobarBottom(Infobar infobar,boolean is_this_updating, PrintWriter print_writer, MatchAllData match, String session_selected_broadcaster) throws InterruptedException
	{
		DecimalFormat df = new DecimalFormat("0.00");
		switch (session_selected_broadcaster.toUpperCase()) {
		case "FRUIT":
			switch(infobar.getBottom_right_bottom_section().toUpperCase()) {
			case "EXTRAS":
				if(is_this_updating == false) {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vSection4Selection " + "0" + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vSection5Selection " + "4" + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vSection6Selection " + "3" + ";");
				}
				for(Inning inn : match.getMatch().getInning()) {
					if(inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)) {
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tFreeTextSec6 " + "EXTRAS - " + inn.getTotalExtras() +  " ( NB " + inn.getTotalNoBalls() + ", WD " 
								+ inn.getTotalWides() + ", B " + inn.getTotalByes() + ", LB " + inn.getTotalLegByes() + ", PN "+ inn.getTotalPenalties() + " )" + ";");
					}
				}
				break;
			case "CURRENT_SESSION":
				double dayovers=0;
				if(is_this_updating == false) {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vSection4Selection " + "0" + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vSection5Selection " + "4" + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vSection6Selection " + "4" + ";");
				}
				
				if(match.getMatch().getDaysSessions().get(match.getMatch().getDaysSessions().size()-1).getTotalBalls() % 6 == 0) {
					dayovers = match.getMatch().getDaysSessions().get(match.getMatch().getDaysSessions().size()-1).getTotalBalls() / 6 ;
				}else {
					dayovers = Double.valueOf(String.valueOf(match.getMatch().getDaysSessions().get(match.getMatch().getDaysSessions().size()-1).getTotalBalls()/6) + 
							"." + String.valueOf(match.getMatch().getDaysSessions().get(match.getMatch().getDaysSessions().size()-1).getTotalBalls()%6)); 
				}
				
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tSessionHead " + "SESSION " + match.getMatch().getDaysSessions().
						get(match.getMatch().getDaysSessions().size()-1).getSessionNumber() + ";");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tStatValue1 " + 
						match.getMatch().getDaysSessions().get(match.getMatch().getDaysSessions().size()-1).getTotalRuns() + ";");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tStatValue2 " + dayovers + ";");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tStatValue3 " + match.getMatch().getDaysSessions().get(match.getMatch().getDaysSessions().size()-1).getTotalWickets() + ";");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tStatValue4 " + df.format(match.getMatch().getDaysSessions().get(match.getMatch().getDaysSessions().size()-1).getTotalRuns()/dayovers) + ";");
				
				break;
			case "DAY_PLAY":
				int daynumber=0, dayruns=0, daywickets=0;
				int dayballs=0;
				if(is_this_updating == false) {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vSection4Selection " + "0" + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vSection5Selection " + "4" + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vSection6Selection " + "4" + ";");
				}
				daynumber = match.getMatch().getDaysSessions().get(match.getMatch().getDaysSessions().size()-1).getDayNumber();
				for(DaySession day_session : match.getMatch().getDaysSessions()) {
					if(daynumber == day_session.getDayNumber()) {
						dayruns = dayruns + day_session.getTotalRuns();
						dayballs = dayballs + day_session.getTotalBalls();
						daywickets = daywickets + day_session.getTotalWickets();
					}
				}
				
				if(dayballs % 6 == 0) {
					dayballs = dayballs / 6 ;
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tStatValue2 " + dayballs + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tStatValue4 " + df.format(dayruns/dayballs) + ";");
					
				}else {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tStatValue2 " + Double.valueOf(CricketFunctions.OverBalls(0, dayballs)) + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tStatValue4 " + df.format(dayruns/Double.valueOf(CricketFunctions.OverBalls(0, dayballs))) + ";");
				}
				
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tSessionHead " + "DAY " + daynumber + ";");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tStatValue1 " + dayruns + ";");
				
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tStatValue3 " + daywickets + ";");
				
				
				break;
			case "TIMELINE":
				String this_ball_data="";
				int ball_count=0;
				for(Inning inn : match.getMatch().getInning()) {
					if (inn.getIsCurrentInning().toUpperCase().equalsIgnoreCase(CricketUtil.YES)) {
							
						if ((match.getEventFile().getEvents() != null) && (match.getEventFile().getEvents().size() > 0)) {
							  for (int i=match.getEventFile().getEvents().size() - 1; i>=0; i--)
							  {  
								
								switch(match.getEventFile().getEvents().get(i).getEventType()) {
								case CricketUtil.CHANGE_BOWLER: case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE:  case CricketUtil.FIVE : case CricketUtil.DOT:
								case CricketUtil.FOUR: case CricketUtil.SIX: case CricketUtil.WIDE: case CricketUtil.NO_BALL: case CricketUtil.BYE: case CricketUtil.LEG_BYE: 
								case CricketUtil.PENALTY: case CricketUtil.LOG_WICKET: case CricketUtil.LOG_ANY_BALL:
									ball_count = ball_count + 1;
									switch (match.getEventFile().getEvents().get(i).getEventType())
								    {
								    case CricketUtil.CHANGE_BOWLER:
										print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vBall" + ball_count + " " + "0" + ";");
										break;
								    case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE:  case CricketUtil.FIVE : case CricketUtil.DOT:
								    	print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vBall" + ball_count + " " + "2" + ";");
										print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tTimelineRun"+ ball_count + " " + match.getEventFile().getEvents().get(i).getEventRuns() + ";");
										break;
								    case CricketUtil.FOUR: case CricketUtil.SIX: 
								    	print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vBall" + ball_count + " " + "3" + ";");
										print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tTimelineRun"+ ball_count + " " + match.getEventFile().getEvents().get(i).getEventRuns() + ";");
										break;
								    case CricketUtil.WIDE: case CricketUtil.NO_BALL: case CricketUtil.BYE: case CricketUtil.LEG_BYE: case CricketUtil.PENALTY: 
								    	print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vBall" + ball_count + " " + "10" + ";");
								    	if(match.getEventFile().getEvents().get(i).getEventType().equalsIgnoreCase(CricketUtil.WIDE)) {
								    		this_ball_data = "WD";
								    	}else if(match.getEventFile().getEvents().get(i).getEventType().equalsIgnoreCase(CricketUtil.NO_BALL)) {
								    		this_ball_data = "NB";
								    	}else if(match.getEventFile().getEvents().get(i).getEventType().equalsIgnoreCase(CricketUtil.LEG_BYE)) {
								    		this_ball_data = "LB";
								    	}else if(match.getEventFile().getEvents().get(i).getEventType().equalsIgnoreCase(CricketUtil.BYE)) {
								    		this_ball_data = "B";
								    	}else if(match.getEventFile().getEvents().get(i).getEventType().equalsIgnoreCase(CricketUtil.PENALTY)) {
								    		this_ball_data = "P";
								    	}
										print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tTimelineRun"+ ball_count + " " + 
												(match.getEventFile().getEvents().get(i).getEventRuns() + match.getEventFile().getEvents().get(i).getEventSubExtraRuns()) + this_ball_data.toUpperCase() + ";");
										break;
								    case CricketUtil.LOG_WICKET: 
								    	if (match.getEventFile().getEvents().get(i).getEventRuns() > 0) {
								    	  print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vBall" + ball_count + " " + "15" + ";");
								    	  print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tTimelineRun"+ ball_count + " " + 
								    			  String.valueOf(match.getEventFile().getEvents().get(i).getEventRuns()) + "+W" + ";");
								      } else {
								    	  print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vBall" + ball_count + " " + "15" + ";");
								    	  print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tTimelineRun"+ ball_count + " " + "W" + ";");
								      }
								      break;
								    case CricketUtil.LOG_ANY_BALL:
								    	if(match.getEventFile().getEvents().get(i).getEventSubExtra().equalsIgnoreCase(CricketUtil.PENALTY)) {
								    		this_ball_data = String.valueOf(match.getEventFile().getEvents().get(i).getEventSubExtraRuns()) + "Pn";
								    		print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vBall" + ball_count + " " + "5" + ";");
							    			print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tTimelineRun"+ ball_count + " " + 
							    					this_ball_data + ";");
								    	}else {
								    		if(match.getEventFile().getEvents().get(i).getEventExtra() != null) {
									    		if(match.getEventFile().getEvents().get(i).getEventExtra().equalsIgnoreCase(CricketUtil.WIDE)){
									    			if(match.getEventFile().getEvents().get(i).getEventSubExtra().equalsIgnoreCase(CricketUtil.WIDE)) {
									    				this_ball_data = String.valueOf(match.getEventFile().getEvents().get(i).getEventRuns() + match.getEventFile().getEvents().get(i).getEventExtraRuns()+
										    					match.getEventFile().getEvents().get(i).getEventSubExtraRuns()) + "WD";
									    			}else {
									    				this_ball_data = String.valueOf(match.getEventFile().getEvents().get(i).getEventRuns() + match.getEventFile().getEvents().get(i).getEventExtraRuns()) + "WD";
									    			}
									    		}
									    		else if(match.getEventFile().getEvents().get(i).getEventExtra().equalsIgnoreCase(CricketUtil.NO_BALL)) {
									    			if(match.getEventFile().getEvents().get(i).getEventSubExtra().equalsIgnoreCase(CricketUtil.NO_BALL) && match.getEventFile().getEvents().get(i).getEventWasABoundary() != null 
									    					&& match.getEventFile().getEvents().get(i).getEventWasABoundary().equalsIgnoreCase(CricketUtil.YES)) {
									    				
									    				this_ball_data = "NB + " + String.valueOf(match.getEventFile().getEvents().get(i).getEventRuns());
									    				
									    			}else {
									    				this_ball_data = String.valueOf(match.getEventFile().getEvents().get(i).getEventRuns() + match.getEventFile().getEvents().get(i).getEventExtraRuns()+
										    					match.getEventFile().getEvents().get(i).getEventSubExtraRuns()) + "NB";
									    			}
									    			
								    			}else {
								    				if(match.getEventFile().getEvents().get(i).getEventRuns()>0) {
								    					this_ball_data = String.valueOf(match.getEventFile().getEvents().get(i).getEventRuns());
								    				}
								    			}
									    	}
								    		
								    		if(match.getEventFile().getEvents().get(i).getEventSubExtra() != null && match.getEventFile().getEvents().get(i).getEventSubExtraRuns()>0) {
									    		if(!match.getEventFile().getEvents().get(i).getEventSubExtra().equalsIgnoreCase(CricketUtil.WIDE) && 
									    				!match.getEventFile().getEvents().get(i).getEventSubExtra().equalsIgnoreCase(CricketUtil.NO_BALL)) {
									    			if(this_ball_data.isEmpty()) {
									    				this_ball_data = String.valueOf(match.getEventFile().getEvents().get(i).getEventSubExtraRuns());
									    			}else {
									    				this_ball_data = this_ball_data + "+" + match.getEventFile().getEvents().get(i).getEventSubExtraRuns();
									    			}
									    			if(match.getEventFile().getEvents().get(i).getEventSubExtra().equalsIgnoreCase(CricketUtil.LEG_BYE)) {
										    			this_ball_data = this_ball_data + "LB";
										    		}else if(match.getEventFile().getEvents().get(i).getEventSubExtra().equalsIgnoreCase(CricketUtil.BYE)) {
										    			this_ball_data = this_ball_data + "B";
										    		}
									    		}
								    		}
								    		if (match.getEventFile().getEvents().get(i).getEventHowOut() != null && !match.getEventFile().getEvents().get(i).getEventHowOut().isEmpty()) {
									    		this_ball_data = this_ball_data + "+W";
									    		print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vBall" + ball_count + " " + "14" + ";");
								    			print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tTimelineRun"+ ball_count + " " + 
								    					this_ball_data + ";");
									    	}else {
									    		print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vBall" + ball_count + " " + "5" + ";");
								    			print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tTimelineRun"+ ball_count + " " + 
								    					this_ball_data + ";");
									    	}
								    	}
								    }
									break;
								}
									
							    if(ball_count >= 17) {
							    	break;
							    }
							  }
							}
						if(is_this_updating == false) {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vSection4Selection " + "0" + ";");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vSection5Selection " + "4" + ";");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vSection6Selection " + "5" + ";");
						}
					}
				}
				break;
			
			case"BOUNDARIES":
				for(Inning inn : match.getMatch().getInning()) {
					if(inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)) {
						if(is_this_updating == false) {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vSection4Selection " + "0" + ";");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vSection5Selection " + "4" + ";");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vSection6Selection " + "2" + ";");
						}
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tFoursValue " + inn.getTotalFours() + ";");
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tSixValue " + inn.getTotalSixes() + ";");
					}
				}
				break;
			case"PARTNERSHIP":
				for(Inning inn : match.getMatch().getInning()) {
					if(inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)) {
						if(is_this_updating == false) {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vSection4Selection " + "0" + ";");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vSection5Selection " + "4" + ";");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vSection6Selection " + "1" + ";");
						}
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tPartnershiplPlayer1Runs " + 
								inn.getPartnerships().get(inn.getPartnerships().size() - 1).getFirstBatterRuns() + ";");
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tPartnershiplPlayer1Balls " + 
								 "(" + inn.getPartnerships().get(inn.getPartnerships().size() - 1).getFirstBatterBalls() + ")"  + ";");
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tPartnershipPlayer2Runs " + 
								inn.getPartnerships().get(inn.getPartnerships().size() - 1).getSecondBatterRuns() + ";");
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tPartnershipPlayer2Balls " + 
								"(" + inn.getPartnerships().get(inn.getPartnerships().size() - 1).getSecondBatterBalls() + ")"  + ";");
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tPartnershipRuns " + 
								inn.getPartnerships().get(inn.getPartnerships().size() - 1).getTotalRuns() + ";");
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tPartnershipBalls " + 
								 "(" + inn.getPartnerships().get(inn.getPartnerships().size() - 1).getTotalBalls() + ")"  + ";");
					}
				}
				break;
			
			case"PROJECTED_SCORE":
				
				if(is_this_updating == false) {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vSection4Selection " + "0" + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vSection5Selection " + "4" + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vSection6Selection " + "0" + ";");
				}
				
			    String[] proj_score_rate = new String[CricketFunctions.projectedScore(match).size()];
			    for (int i = 0; i < CricketFunctions.projectedScore(match).size(); i++) {
			    	proj_score_rate[i] = CricketFunctions.projectedScore(match).get(i);
		        }
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tProjectedHead1 " + "@" + proj_score_rate[0] +" (CRR)" + ";");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tProjectedValue1 " + proj_score_rate[1] + ";");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tProjectedHead2 " + "@" + proj_score_rate[2] + " RPO"+ ";");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tProjectedValue2 " + proj_score_rate[3] + ";");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tProjectedHead3 " + "@" + proj_score_rate[4] + " RPO" + ";");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tProjectedValue3 " + proj_score_rate[5] + ";");
				break;	
			}
			break;
		}
		infobar.setLast_bottom_right_bottom_section(infobar.getBottom_right_bottom_section().toUpperCase());
		return infobar;
	}
	public void populateInfobarDirector(PrintWriter print_writer,String Dir_value,String session_selected_broadcaster) {
		switch (session_selected_broadcaster.toUpperCase()) {
		case "FRUIT":
			switch (Dir_value.toUpperCase()) {
			case "FOURS":
				print_writer.println("LAYER1*EVEREST*STAGE*DIRECTOR*SixIn STOP;");
				print_writer.println("LAYER1*EVEREST*STAGE*DIRECTOR*SixIn SHOW 0.0;");
				processAnimation(print_writer, "FourIn", "START", session_selected_broadcaster,1);
				break;

			case "SIXES":
				processAnimation(print_writer, "SixIn", "START", session_selected_broadcaster,1);
				break;
			
			case "WICKETS":
				processAnimation(print_writer, "WicketIn", "START", session_selected_broadcaster,1);
				break;

			case "FREE-HIT":
				processAnimation(print_writer, "FreeHitIn", "START", session_selected_broadcaster,1);
				break;
			}
			break;
		}
	}
	
	public void processAnimation(PrintWriter print_writer, String animationName,String animationCommand, String which_broadcaster,int which_layer)
	{
		switch(which_broadcaster.toUpperCase()) {
		case "FRUIT":
			switch(which_layer) {
			case 1:
				print_writer.println("LAYER1*EVEREST*STAGE*DIRECTOR*" + animationName + " " + animationCommand + ";");
				
				break;
				
			case 2:
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*" + animationName + " " + animationCommand + ";");
				
				//print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*LOOP START;");	
				break;
			}
			break;
		}
		
	}
	
	public void populateDuckWorthLewis(PrintWriter print_writer,String viz_scene,String balls,MatchAllData match,String session_selected_broadcaster) throws InterruptedException 
	{
		switch (session_selected_broadcaster.toUpperCase()) {
			case "FRUIT":
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFirstName " + match.getMatch().getInning().get(1).getBatting_team().getTeamName1() + " " 
						+ match.getMatch().getInning().get(1).getTotalRuns() + "-" + match.getMatch().getInning().get(1).getTotalWickets() + ";");
				
				for(int i = 0; i<= CricketFunctions.populateDuckWorthLewis(match).size() -1;i++) {
					if(CricketFunctions.populateDuckWorthLewis(match).get(i).getOver_left().equalsIgnoreCase(balls)) {
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tDetails " + "CURRENT DLS PAR SCORE AFTER " 
									+ balls + " OVERS: " + (Integer.valueOf(CricketFunctions.populateDuckWorthLewis(match).get(i).getWkts_down()) + 1) + ";");
					}
				}
				
				print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW ON;");
				//print_writer.println("LAYER1*EVEREST*STAGE*DIRECTOR*Out SHOW 0.0;");
				//print_writer.println("LAYER1*EVEREST*STAGE*DIRE CTOR*In SHOW 0.0;");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*CONTAINER SET ACTIVE 0;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In STOP;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out STOP;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 109.0;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out SHOW 0.0;");
				print_writer.println("LAYER1*EVEREST*GLOBAL SNAPSHOT_PATH C:/Temp/Preview.png;");
				print_writer.println("LAYER1*EVEREST*GLOBAL SNAPSHOT 1920 1080;");
				TimeUnit.SECONDS.sleep(1);
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
			case "FRUIT":
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
				//print_writer.println("LAYER1*EVEREST*STAGE*DIRECTOR*Out SHOW 0.0;");
				//print_writer.println("LAYER1*EVEREST*STAGE*DIRE CTOR*In SHOW 0.0;");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*CONTAINER SET ACTIVE 0;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In STOP;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out STOP;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 109.0;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out SHOW 0.0;");
				print_writer.println("LAYER1*EVEREST*GLOBAL SNAPSHOT_PATH C:/Temp/Preview.png;");
				print_writer.println("LAYER1*EVEREST*GLOBAL SNAPSHOT 1920 1080;");
				TimeUnit.SECONDS.sleep(1);
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out SHOW 0.0;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 0.0;");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*CONTAINER SET ACTIVE 1;");
				print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW OFF;");
				break;
		}
	}
	
	public void AnimateInGraphics(PrintWriter print_writer, String whichGraphic)
	{
		switch(whichGraphic) {
		case "FF_IN":
			print_writer.println("LAYER1*EVEREST*STAGE*DIRECTOR*" + "FF_In" + " " + "START" + ";");
			break;
		}	
	}	
	public void AnimateOutGraphics(PrintWriter print_writer, String whichGraphic) throws InterruptedException
	{
		switch(whichGraphic) {
		case "INFOBAR":
			processAnimation(print_writer, "Out", "START", session_selected_broadcaster,1);
			break;
		
		case "SCORECARD": 
			processAnimation(print_writer, "BattingCardOut", "START", session_selected_broadcaster,2);
			TimeUnit.SECONDS.sleep(1);
			break;
		case "BOWLINGCARD":
			processAnimation(print_writer, "BowlingCardOut", "START", session_selected_broadcaster,2);
			TimeUnit.SECONDS.sleep(1);
			break;
		case "SUMARRY": case "PREVIOUS_SUMARRY":
			processAnimation(print_writer, "SummaryOut", "START", session_selected_broadcaster,2);
			TimeUnit.SECONDS.sleep(1);
			break;
		case "POINTSTABLE":
			processAnimation(print_writer, "PointsTableOut", "START", session_selected_broadcaster,2);
			TimeUnit.SECONDS.sleep(1);
			break;
		
		case "BUG": case "HOWOUT": case "BATSMANSTATS": case "BOWLERSTATS": case "BUG-DB": case "NAMESUPER": case "NAMESUPER-PLAYER": case "DOUBLETEAMS": 
		case "MATCHID": case "L3MATCHID": case "PLAYINGXI": case "TARGET": case "TEAMSUMMARY": case "EQUATION":case "PLAYERSUMMARY": case "L3PLAYERPROFILE": 
		case "FALLOFWICKET": case "SPLIT": case "COMPARISION": case "BUG-DISMISSAL": case "HOWOUT_WITHOUT_FIELDER": case "BATSMAN_STYLE": case "BUG-BOWLER": 
		case "MATCH_PROMO": case "TEAMS_LOGO": case "BOWLER_STYLE": case "TIEID-DOUBLE": case "GENERIC": case "MOSTRUNS": case "MOSTWICKETS": 
		case "MOSTFOURS": case "MOSTSIXES": case "HIGHESTSCORE": case "MANHATTAN": case "PARTNERSHIP": case "PROJECTED": case "FF_TARGET": case "THISOVER":
		case "L3HOWOUT": case "CURRENT_PARTNERSHIP": case "WORM": case "PLAYERPROFILE": case "MATCHSTATUS": case "HOWOUT_BOTH": case "BATSMANSTATS_BOTH":
		case "THIS_SESSION": case "SESSION": case "FF_EQUATION": case "BUG-TOSS": case "BOWLERDETAILS":
			processAnimation(print_writer, "Out", "START", session_selected_broadcaster,2);
			TimeUnit.SECONDS.sleep(1);
			break;
			
		 case "LEADERBOARD":
			processAnimation(print_writer, "FF_Out", "START", session_selected_broadcaster,1);
			break;
			
			
		
		case "FF_OUT":
			print_writer.println("LAYER1*EVEREST*STAGE*DIRECTOR*" + "FF_Out" + " START" + ";");
			//print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*FF_Out START \0");
			break;
		}	
	}
	public String toString() {
		return "Doad [status=" + status + ", slashOrDash=" + slashOrDash + "]";
	}

	
}