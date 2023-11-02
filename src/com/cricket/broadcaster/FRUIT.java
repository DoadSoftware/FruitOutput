package com.cricket.broadcaster;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.cricket.model.BattingCard;
import com.cricket.model.BestStats;
import com.cricket.model.BowlingCard;
import com.cricket.model.Bugs;
import com.cricket.model.DaySession;
import com.cricket.model.Player;
import com.cricket.model.Statistics;
import com.cricket.model.Team;
import com.cricket.model.Tournament;
import com.cricket.model.VariousText;
import com.cricket.service.CricketService;
import com.cricket.model.Inning;
import com.cricket.model.LeagueTable;
import com.cricket.model.LeagueTeam;
import com.cricket.model.MatchAllData;
import com.cricket.model.NameSuper;
import com.cricket.model.OverByOverData;
import com.cricket.model.Partnership;
import com.cricket.model.FallOfWicket;
import com.cricket.model.Fixture;
import com.cricket.model.Ground;
import com.cricket.model.InfobarStats;
import com.cricket.containers.DuckWorthLewis;
import com.cricket.containers.Infobar;
import com.cricket.containers.Scene;
import com.cricket.util.CricketFunctions;
import com.cricket.util.CricketUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class FRUIT extends Scene{

	private String status;
	private String slashOrDash = "-";
	public Infobar infobar = new Infobar();
	public String session_selected_broadcaster = "FRUIT";
	public String which_graphics_onscreen = "";
	private String logo_path = "C:\\Images\\FRUIT\\Logos\\";
	//private String sponsor_logo_path = "D:\\EverestCricket\\Everest_Cric2022\\Logos\\Goa_Cricket_Logos\\Sponsors\\";
	private String photo_path = "C:\\Images\\FRUIT\\Photos\\";
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
			infobar = populateInfobarBottomLeft(infobar,true, print_writer, match, session_selected_broadcaster);
			infobar = populateInfobarBottomRight(infobar,true, print_writer,  match, session_selected_broadcaster);
			
			if(infobar.getBottom_right_bottom_section() != null && !infobar.getBottom_right_bottom_section().isEmpty()) {
				infobar = populateInfobarBottom(infobar,true, print_writer,  match, session_selected_broadcaster);
			}
			
		}
//		CricketFunctions.getInteractive(match);
		return infobar;
	}
	
	public Object ProcessGraphicOption(String whatToProcess, MatchAllData match, CricketService cricketService, List<MatchAllData> tournament_matches,
			PrintWriter print_writer, List<Scene> scenes, String valueToProcess, List<Statistics> statistics) throws InterruptedException, ParseException, JAXBException, NumberFormatException, IOException, IllegalAccessException, InvocationTargetException{
		switch (whatToProcess.toUpperCase()) {
		case "BUG_GRAPHICS-OPTIONS": case "HOWOUT_GRAPHICS-OPTIONS": case "BATSMANSTATS_GRAPHICS-OPTIONS": case "BOWLERSTATS_GRAPHICS-OPTIONS": case "NAMESUPER_PLAYER_GRAPHICS-OPTIONS": 
		case "L3PLAYERPROFILE_GRAPHICS-OPTIONS": case "PLAYERPROFILE_GRAPHICS-OPTIONS": case "BOTTOMLEFT_GRAPHICS-OPTIONS": case "BOTTOMRIGHT_GRAPHICS-OPTIONS": case "INFOBAR_GRAPHICS-OPTIONS": 
		case "COMPARISION-GRAPHICS-OPTIONS": case "BOTTOM_GRAPHICS-OPTIONS": case "ANIMATE_PLAYINGXI-OPTIONS": case "PROJECTED_GRAPHICS-OPTIONS": case "TARGET_GRAPHICS-OPTIONS": 
		case "PLAYERSUMMARY_GRAPHICS-OPTIONS": case "BUG_DISMISSAL_GRAPHICS-OPTIONS": case "TOP_GRAPHICS-OPTIONS": case "BUG_BOWLER_GRAPHICS-OPTIONS": case "HOWOUT_WITHOUT_FIELDER_GRAPHICS-OPTIONS":
		case "BOWLERDETAILS_GRAPHICS-OPTIONS":	case "NEXTTOBAT_GRAPHICS-OPTIONS": case "BOWLERSUMMARY_GRAPHICS-OPTIONS": case "LANDMARK_GRAPHICS-OPTIONS": case "EQUATION_GRAPHICS-OPTIONS":
		case "POSITION_LANDMARK_GRAPHICS-OPTIONS": case "BATSMAN_THIS_MATCH_GRAPHICS-OPTIONS": case "BOWLER_THIS_MATCH_GRAPHICS-OPTIONS": case "PLAYERS_GRAPHICS-OPTIONS":
		case "BATSMAN_STYLE_GRAPHICS-OPTIONS": case "GENERIC_GRAPHICS-OPTIONS": case "FF_TARGET_GRAPHICS-OPTIONS": case "HOWOUT_BOTH_GRAPHICS-OPTIONS": case "BATSMANSTATS_BOTH_GRAPHICS-OPTIONS":
		case "THIS_SESSION_GRAPHICS-OPTIONS": case "FF_EQUATION_GRAPHICS-OPTIONS": case "L3PLAYERPROFILEBUKHATIR_GRAPHICS-OPTIONS": case "PLAYERPROFILEBUKHATIR_GRAPHICS-OPTIONS":	
			switch (session_selected_broadcaster.toUpperCase()) {
			case "FRUIT":
				return JSONObject.fromObject(match).toString();
			}
		
		case "NAMESUPER_GRAPHICS-OPTIONS": 
			switch (session_selected_broadcaster.toUpperCase()) {
			case "FRUIT":
				return JSONArray.fromObject(cricketService.getNameSupers()).toString();
			}
		//case "MATCH-PROMO_GRAPHICS-OPTIONS": case "PREVIOUS_SUMMARY_GRAPHICS-OPTIONS": case "LT-TIEID-DOUBLE_GRAPHICS-OPTIONS":
			//return JSONArray.fromObject(CricketFunctions.processAllFixtures(cricketService)).toString();
			
		case "PROMPT_GRAPHICS-OPTIONS":
			return JSONArray.fromObject(cricketService.getInfobarStats()).toString();
		case "LEADERBOARD_GRAPHICS-OPTIONS": case "WICKETS_GRAPHICS-OPTIONS": case "FOURS_GRAPHICS-OPTIONS": case "SIXES_GRAPHICS-OPTIONS":
			List<Tournament> tourn_stats = extractTournamentStats("COMBINED_PAST_CURRENT_MATCH_DATA", tournament_matches, cricketService, match,null);
			switch (whatToProcess) {
			case "LEADERBOARD_GRAPHICS-OPTIONS": 
				Collections.sort(tourn_stats,new CricketFunctions.BatsmenMostRunComparator());
				break;
			case "WICKETS_GRAPHICS-OPTIONS": 
				Collections.sort(tourn_stats,new CricketFunctions.BowlerWicketsComparator());
				break;
			case "FOURS_GRAPHICS-OPTIONS": 
				Collections.sort(tourn_stats,new CricketFunctions.BatsmanFoursComparator());
				break;
			case "SIXES_GRAPHICS-OPTIONS":
				Collections.sort(tourn_stats,new CricketFunctions.BatsmanSixesComparator());
				break;
			}
			return JSONArray.fromObject(tourn_stats).toString();
		case "BUG_DB_GRAPHICS-OPTIONS": case "BUG_DB2_GRAPHICS-OPTIONS":
			return JSONArray.fromObject(cricketService.getBugs()).toString();
		
		case "POPULATE-FF-SCORECARD": case "POPULATE-FF-BOWLINGCARD": case "POPULATE-FF-PARTNERSHIP": case "POPULATE-FF-MATCHSUMMARY": case "POPULATE-L3-BUG":  case "POPULATE-L3-HOWOUT":
		case "POPULATE-L3-BATSMANSTATS": case "POPULATE-L3-NAMESUPER": case "POPULATE-L3-NAMESUPER-PLAYER": case "POPULATE-FF-PLAYERPROFILE": case "POPULATE-FF-DOUBLETEAMS":
		case "POPULATE-FF-PLAYERPROFILEBALL": case "POPULATE-DLS": case "POPULATE-DLS-EQUATION":
		case "POPULATE-L3-INFOBAR": 
		case "POPULATE-FF-LEADERBOARD": case "POPULATE-INFOBAR-BOTTOMLEFT": case "POPULATE-INFOBAR-BOTTOMRIGHT": case "POPULATE-INFOBAR-BOTTOM": case "POPULATE-FF-MATCHID": 
		case "POPULATE-FF-PLAYINGXI": case "POPULATE-PLAYING_CHANGE_ON1":	case "POPULATE-PLAYING_CHANGE_ON2":
		case "POPULATE-LT-PROJECTED": case "POPULATE-L3-TARGET": case "POPULATE-L3-TEAMSUMMARY": case "POPULATE-L3-PLAYERSUMMARY": case "POPULATE-L3-PLAYERPROFILE": 
		case "POPULATE-L3-PLAYERPROFILEBAT": case "POPULATE-L3-FALLOFWICKET": 
		case "POPULATE-L3-COMPARISION": case "POPULATE-INFOBAR-PROMPT": case "POPULATE-LT-MATCHID": case "POPULATE-L3-BOWLERSTATS": case "POPULATE-L3-BUG-DISMISSAL": case "POPULATE-L3-SPLIT":
		case "POPULATE-L3-BUG-DB": case "POPULATE-INFOBAR-TOP": case "POPULATE-L3-BUG-BOWLER": case "POPULATE-L3-BUGTARGET": case "POPULATE-L3-HOWOUT_WITHOUT_FIELDER": 
		case "POPULATE-L3-BOWLERSUMMARY": case "POPULATE-LT-BUG_HIGHLIGHT":
		case "POPULATE-L3-NEXT_TO_BAT": case "POPULATE-L3-BOWLERDETAILS": case "POPULATE-LT-POWERPLAY": case "POPULATE-FF-LANDMARK": case "POPULATE-PREVIOUS_SUMMARY": case "POPULATE-LT-EQUATION": 
		case "POPULATE-FF-POSITION_LANDMARK": case "POPULATE-L3-BATSMAN_THIS_MATCH": case "POPULATE-L3-BOWLER_THIS_MATCH": case "POPULATE-POINTS_TABLE": case "POPULATE-LT-PARTNERSHIP":
		case "POPULATE-LTPOINTS_TABLE":	case "POPULATE-BOWLER_STYLE": case "POPULATE-BATSMAN_STYLE": case "POPULATE-MANHATTAN": case "POPULATE-MATCH_PROMO": case "POPULATE-FF-TEAMS_LOGO":
		case "POPULATE-TIEID-DOUBLE": case "POPULATE-L3-GENERIC": case "POPULATE-MOSTRUNS": case "POPULATE-MOSTWICKETS": case "POPULATE-MOSTFOURS": case "POPULATE-MOSTSIXES": 
		case "POPULATE-HIGHESTSCORE":
		case "POPULATE-DIRECTOR": case "POPULATE-FF-TARGET": case "POPULATE-THISOVER": case "POPULATE-HOWOUT_QUICK": case "POPULATE-WORM": case "POPULATE-INFOBAR-TOPRIGHT":
		case "POPULATE-MATCHSTATUS": case "POPULATE-L3-HOWOUT_BOTH": case "POPULATE-L3-BATSMANSTATS_BOTH": case "POPULATE-LT-SPONSOR": case "POPULATE-LT-THIS_SESSION": case "POPULATE-THIS_SESSION":
		case "POPULATE-EQUATION-FF": case "POPULATE-L3-BUG-TOSS": case "POPULATE-L3-PLAYERPROFILE_BUKHATIR": case "POPULATE-FF-PLAYERPROFILE_BUKHATIR": case "POPULATE-HOWOUTWITHOUTFIELDER_QUICK":
		case "POPULATE-BUG_POWERPLAY": case "POPULATE-BUG_PARTNERSHIP": case "POPULATE-L3-THISSERIES": case "POPULATE-FOUR": case "POPULATE-SIX": case "POPULATE-DRONE_BUG":
			if(which_graphics_onscreen == "INFOBAR") {
			}else if(which_graphics_onscreen == "SCORECARD" && whatToProcess.toUpperCase().equalsIgnoreCase("POPULATE-FF-BOWLINGCARD") || 
			 which_graphics_onscreen == "SCORECARD" && whatToProcess.toUpperCase().equalsIgnoreCase("POPULATE-FF-MATCHSUMMARY") ||
			 which_graphics_onscreen == "SCORECARD" && whatToProcess.toUpperCase().equalsIgnoreCase("POPULATE-POINTS_TABLE") ||
			
			 which_graphics_onscreen == "BOWLINGCARD" && whatToProcess.toUpperCase().equalsIgnoreCase("POPULATE-FF-SCORECARD") || 
			 which_graphics_onscreen == "BOWLINGCARD" && whatToProcess.toUpperCase().equalsIgnoreCase("POPULATE-FF-MATCHSUMMARY") ||
			 which_graphics_onscreen == "BOWLINGCARD" && whatToProcess.toUpperCase().equalsIgnoreCase("POPULATE-POINTS_TABLE") ||
			 
			 which_graphics_onscreen == "SUMARRY" && whatToProcess.toUpperCase().equalsIgnoreCase("POPULATE-FF-SCORECARD") || 
			 which_graphics_onscreen == "SUMARRY" && whatToProcess.toUpperCase().equalsIgnoreCase("POPULATE-FF-BOWLINGCARD") ||
			 which_graphics_onscreen == "SUMARRY" && whatToProcess.toUpperCase().equalsIgnoreCase("POPULATE-POINTS_TABLE") ||
			 
			 which_graphics_onscreen == "POINTSTABLE" && whatToProcess.toUpperCase().equalsIgnoreCase("POPULATE-FF-SCORECARD") || 
			 which_graphics_onscreen == "POINTSTABLE" && whatToProcess.toUpperCase().equalsIgnoreCase("POPULATE-FF-MATCHSUMMARY") ||
			 which_graphics_onscreen == "POINTSTABLE" && whatToProcess.toUpperCase().equalsIgnoreCase("POPULATE-FF-BOWLINGCARD") ||
			 
			 which_graphics_onscreen == "PLAYINGXI" && whatToProcess.toUpperCase().equalsIgnoreCase("POPULATE-PLAYING_CHANGE_ON1") || 
			 which_graphics_onscreen == "PLAYINGXI" && whatToProcess.toUpperCase().equalsIgnoreCase("POPULATE-PLAYING_CHANGE_ON2")) {
			}
			else if(which_graphics_onscreen != "") {
				AnimateOutGraphics(print_writer, which_graphics_onscreen.toUpperCase());
			}
			
			switch (session_selected_broadcaster.toUpperCase()) {
			case "FRUIT":
//				switch(whatToProcess.toUpperCase()) {
//				case"POPULATE-INFOBAR-BOTTOMLEFT": case"POPULATE-INFOBAR-BOTTOMRIGHT": case"POPULATE-INFOBAR-PROMPT": case "POPULATE-INFOBAR-BOTTOM": case "POPULATE-DIRECTOR":
//				case "POPULATE-INFOBAR-TOPRIGHT": case "POPULATE-LT-SPONSOR":
//				case "POPULATE-PLAYING_CHANGE_ON1":	case "POPULATE-PLAYING_CHANGE_ON2":
//					break;
//				case "POPULATE-L3-INFOBAR":
//					scenes.get(0).setScene_path(valueToProcess.split(",")[0]);
//					scenes.get(0).scene_load(print_writer,session_selected_broadcaster);
//					break;
//				default:
//					scenes.get(1).setScene_path(valueToProcess.split(",")[0]);
//					scenes.get(1).scene_load(print_writer,session_selected_broadcaster);
//					break;
//				}
				
				switch(whatToProcess.toUpperCase()) {
				case"POPULATE-INFOBAR-BOTTOMLEFT": case"POPULATE-INFOBAR-BOTTOMRIGHT": case"POPULATE-INFOBAR-PROMPT": case "POPULATE-INFOBAR-BOTTOM": case "POPULATE-DIRECTOR":
				case "POPULATE-INFOBAR-TOPRIGHT": case "POPULATE-LT-SPONSOR":
				case "POPULATE-PLAYING_CHANGE_ON1":	case "POPULATE-PLAYING_CHANGE_ON2":
					break;
				case "POPULATE-L3-INFOBAR":
					scenes.get(0).setScene_path(valueToProcess.split(",")[0]);
					scenes.get(0).scene_load(print_writer,session_selected_broadcaster);
					break;
				case "POPULATE-FF-SCORECARD": case "POPULATE-FF-BOWLINGCARD": case "POPULATE-FF-MATCHSUMMARY": case "POPULATE-POINTS_TABLE":
					
					if(which_graphics_onscreen == "SCORECARD" && whatToProcess.toUpperCase().equalsIgnoreCase("POPULATE-FF-BOWLINGCARD") || 
					 which_graphics_onscreen == "SCORECARD" && whatToProcess.toUpperCase().equalsIgnoreCase("POPULATE-FF-MATCHSUMMARY") ||
					 which_graphics_onscreen == "SCORECARD" && whatToProcess.toUpperCase().equalsIgnoreCase("POPULATE-POINTS_TABLE") ||
					
					 which_graphics_onscreen == "BOWLINGCARD" && whatToProcess.toUpperCase().equalsIgnoreCase("POPULATE-FF-SCORECARD") || 
					 which_graphics_onscreen == "BOWLINGCARD" && whatToProcess.toUpperCase().equalsIgnoreCase("POPULATE-FF-MATCHSUMMARY") ||
					 which_graphics_onscreen == "BOWLINGCARD" && whatToProcess.toUpperCase().equalsIgnoreCase("POPULATE-POINTS_TABLE") ||
					 
					 which_graphics_onscreen == "SUMARRY" && whatToProcess.toUpperCase().equalsIgnoreCase("POPULATE-FF-SCORECARD") || 
					 which_graphics_onscreen == "SUMARRY" && whatToProcess.toUpperCase().equalsIgnoreCase("POPULATE-FF-BOWLINGCARD") ||
					 which_graphics_onscreen == "SUMARRY" && whatToProcess.toUpperCase().equalsIgnoreCase("POPULATE-POINTS_TABLE") ||
					 
					 which_graphics_onscreen == "POINTSTABLE" && whatToProcess.toUpperCase().equalsIgnoreCase("POPULATE-FF-SCORECARD") || 
					 which_graphics_onscreen == "POINTSTABLE" && whatToProcess.toUpperCase().equalsIgnoreCase("POPULATE-FF-MATCHSUMMARY") ||
					 which_graphics_onscreen == "POINTSTABLE" && whatToProcess.toUpperCase().equalsIgnoreCase("POPULATE-FF-BOWLINGCARD")) {
					//AnimateOutGraphics(print_writer, which_graphic_on_screen.toUpperCase());
					}else {
						scenes.get(1).setScene_path(valueToProcess.split(",")[0]);
						scenes.get(1).scene_load(print_writer,session_selected_broadcaster);
					}
					
					break;
				default:
					scenes.get(1).setScene_path(valueToProcess.split(",")[0]);
					scenes.get(1).scene_load(print_writer,session_selected_broadcaster);
					break;
				}
				
				switch (whatToProcess.toUpperCase()) {
				case "POPULATE-PLAYING_CHANGE_ON1":	
					print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW ON;");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*CONTAINER SET ACTIVE 0;");
					print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In STOP;");
					print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out STOP;");
					print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 191.0;");
					print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out SHOW 0.0;");
					print_writer.println("LAYER1*EVEREST*GLOBAL SNAPSHOT_PATH C:/Temp/Preview.png;");
					print_writer.println("LAYER1*EVEREST*GLOBAL SNAPSHOT 1920 1080;");
					//TimeUnit.SECONDS.sleep(1);
					print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out SHOW 0.0;");
					print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 107.0;");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*CONTAINER SET ACTIVE 1;");
					print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW OFF;");
					break;
				case "POPULATE-PLAYING_CHANGE_ON2":
					print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW ON;");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*CONTAINER SET ACTIVE 0;");
					print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In STOP;");
					print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out STOP;");
					print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 297.0;");
					print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out SHOW 0.0;");
					print_writer.println("LAYER1*EVEREST*GLOBAL SNAPSHOT_PATH C:/Temp/Preview.png;");
					print_writer.println("LAYER1*EVEREST*GLOBAL SNAPSHOT 1920 1080;");
					//TimeUnit.SECONDS.sleep(1);
					print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out SHOW 0.0;");
					print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 191.0;");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*CONTAINER SET ACTIVE 1;");
					print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW OFF;");
					break;
				case "POPULATE-L3-THISSERIES":
					//System.out.println(valueToProcess);
					populateThisSeries(print_writer,valueToProcess.split(",")[0],Integer.valueOf(valueToProcess.split(",")[1]),valueToProcess.split(",")[2],
							extractTournamentStats("COMBINED_PAST_CURRENT_MATCH_DATA", tournament_matches, cricketService,match,null)
							,match, session_selected_broadcaster);
					break;
				case "POPULATE-DLS":
//				populateDuckWorthLewis(print_writer, valueToProcess.split(",")[0],valueToProcess.split(",")[1],valueToProcess.split(",")[2],valueToProcess.split(",")[3], match, session_selected_broadcaster);
					populateDuckWorthLewis(print_writer, valueToProcess.split(",")[0],valueToProcess.split(",")[1], match, session_selected_broadcaster);
					break;
				case "POPULATE-DLS-EQUATION":
					populateDuckWorthLewisEquation(print_writer, valueToProcess.split(",")[0],valueToProcess.split(",")[1], match, session_selected_broadcaster);
					break;
				case "POPULATE-BUG_PARTNERSHIP":
					populateBugPartnership(print_writer, valueToProcess.split(",")[0], match, session_selected_broadcaster);
					break;
				case "POPULATE-BUG_POWERPLAY":
					populateBugPowerPLay(print_writer, valueToProcess.split(",")[0], Integer.valueOf(valueToProcess.split(",")[1]), match, session_selected_broadcaster);
					break;
				case "POPULATE-L3-BUG-TOSS":
					populateBugToss(print_writer,valueToProcess.split(",")[0],match,session_selected_broadcaster);
					break;
				case "POPULATE-LT-BUG_HIGHLIGHT":
					populateBugHighlight(print_writer, valueToProcess.split(",")[0], Integer.valueOf(valueToProcess.split(",")[1]), match, session_selected_broadcaster);
					break;
				case "POPULATE-THIS_SESSION":
					populateSession(print_writer,valueToProcess.split(",")[0],Integer.valueOf(valueToProcess.split(",")[1]),Integer.valueOf(valueToProcess.split(",")[2]),match,session_selected_broadcaster);
					break;
				case "POPULATE-LT-THIS_SESSION":
					populateThisSession(print_writer,valueToProcess.split(",")[0],match,session_selected_broadcaster);
					break;
				case "POPULATE-LT-SPONSOR":
					populateSponsor(print_writer,session_selected_broadcaster);
					break;
				case "POPULATE-THISOVER":
					populateThisOver(print_writer,valueToProcess.split(",")[0],match,session_selected_broadcaster);
					break;
				case "POPULATE-MOSTRUNS":
					populateMostRuns(print_writer, valueToProcess.split(",")[0],
							extractTournamentStats("COMBINED_PAST_CURRENT_MATCH_DATA", tournament_matches, cricketService,match,null),
							match,session_selected_broadcaster);
					break;
				case "POPULATE-MOSTWICKETS":
					populateMostWickets(print_writer, valueToProcess.split(",")[0],
							extractTournamentStats("COMBINED_PAST_CURRENT_MATCH_DATA", tournament_matches, cricketService,match,null),
							match,session_selected_broadcaster);
					break;
				case "POPULATE-MOSTFOURS":
					populateMostFours(print_writer, valueToProcess.split(",")[0],
							extractTournamentStats("COMBINED_PAST_CURRENT_MATCH_DATA", tournament_matches, cricketService,match,null),
							match,session_selected_broadcaster);
					break;
				case "POPULATE-MOSTSIXES":
					populateMostSixes(print_writer, valueToProcess.split(",")[0],
							extractTournamentStats("COMBINED_PAST_CURRENT_MATCH_DATA", tournament_matches, cricketService,match,null),
							match,session_selected_broadcaster);
					break;
				case "POPULATE-HIGHESTSCORE":
					populateHighestScore(print_writer, valueToProcess.split(",")[0],
							extractTournamentStats("COMBINED_PAST_CURRENT_MATCH_DATA", tournament_matches, cricketService,match,null),
							match,session_selected_broadcaster);
					break;
				case "POPULATE-FF-SCORECARD":
					populateScorecard(print_writer, valueToProcess.split(",")[0], Integer.valueOf(valueToProcess.split(",")[1]), match, session_selected_broadcaster);
					break;
				case "POPULATE-FF-BOWLINGCARD":
					populateBowlingcard(print_writer, valueToProcess.split(",")[0], false, Integer.valueOf(valueToProcess.split(",")[1]), match, session_selected_broadcaster);
					break;
				case "POPULATE-FF-PARTNERSHIP":
					//whichInning = Integer.valueOf(valueToProcess.split(",")[1]);
					populatePartnership(print_writer, valueToProcess.split(",")[0],Integer.valueOf(valueToProcess.split(",")[1]), match, session_selected_broadcaster);
					break;
				case "POPULATE-LT-PARTNERSHIP":
					populateCurrentPartnership(print_writer, valueToProcess.split(",")[0], match, session_selected_broadcaster);
					break;
				case "POPULATE-FF-MATCHSUMMARY":
					populateMatchsummary(print_writer, valueToProcess.split(",")[0], Integer.valueOf(valueToProcess.split(",")[1]), cricketService.getVariousTexts(),match, session_selected_broadcaster);
					break;
				case "POPULATE-FF-TEAMS_LOGO":
					populateTeamsLogo(print_writer, valueToProcess.split(",")[0],cricketService.getTeams(),match, session_selected_broadcaster);
					break;
				case "POPULATE-L3-BUG-DISMISSAL":
					populateBugDismissal(print_writer, valueToProcess.split(",")[0],Integer.valueOf(valueToProcess.split(",")[1]), 
							valueToProcess.split(",")[2],Integer.valueOf(valueToProcess.split(",")[3]), match, session_selected_broadcaster);
					break;
				case "POPULATE-L3-BUG":
					populateBug(print_writer, valueToProcess.split(",")[0],Integer.valueOf(valueToProcess.split(",")[1]), 
							valueToProcess.split(",")[2],Integer.valueOf(valueToProcess.split(",")[3]), match, session_selected_broadcaster);
					break;
				case "POPULATE-L3-BUG-BOWLER":
					populateBugBowler(print_writer, valueToProcess.split(",")[0],Integer.valueOf(valueToProcess.split(",")[1]), 
							valueToProcess.split(",")[2],Integer.valueOf(valueToProcess.split(",")[3]), match, session_selected_broadcaster);
					break;
				case "POPULATE-L3-HOWOUT":
//					CricketFunctions.updateTournamentDataWithStats(null, tournament_matches, match)
					populateHowout(print_writer, valueToProcess.split(",")[0],Integer.valueOf(valueToProcess.split(",")[1]), valueToProcess.split(",")[2]
							, Integer.valueOf(valueToProcess.split(",")[3]), match, session_selected_broadcaster);
					break;
				case "POPULATE-HOWOUT_QUICK":
					populateL3Howout(print_writer, valueToProcess.split(",")[0], match, session_selected_broadcaster);
					break;
				case "POPULATE-HOWOUTWITHOUTFIELDER_QUICK":
					populateL3HowoutWithoutFielder(print_writer, valueToProcess.split(",")[0], match, session_selected_broadcaster);
					break;
				case "POPULATE-L3-HOWOUT_BOTH":
					populateHowoutBoth(print_writer, valueToProcess.split(",")[0],Integer.valueOf(valueToProcess.split(",")[1]), 
							valueToProcess.split(",")[2], Integer.valueOf(valueToProcess.split(",")[3]), match, session_selected_broadcaster);
					break;
				case "POPULATE-L3-HOWOUT_WITHOUT_FIELDER":
					populateHowoutWithoutFielder(print_writer, valueToProcess.split(",")[0],Integer.valueOf(valueToProcess.split(",")[1]), 
							valueToProcess.split(",")[2], Integer.valueOf(valueToProcess.split(",")[3]), match, session_selected_broadcaster);
					break;
				case "POPULATE-L3-BATSMANSTATS":
					populateBatsmanstats(print_writer, valueToProcess.split(",")[0],Integer.valueOf(valueToProcess.split(",")[1]), 
							valueToProcess.split(",")[2], Integer.valueOf(valueToProcess.split(",")[3]), match, session_selected_broadcaster);
					break;
				case "POPULATE-L3-BATSMANSTATS_BOTH":
					populateBatsmanstatsBoth(print_writer, valueToProcess.split(",")[0],Integer.valueOf(valueToProcess.split(",")[1]), 
							valueToProcess.split(",")[2], Integer.valueOf(valueToProcess.split(",")[3]), match, session_selected_broadcaster);
					break;
				case "POPULATE-L3-BOWLERSTATS":
					populateBowlerstats(print_writer, valueToProcess.split(",")[0],Integer.valueOf(valueToProcess.split(",")[1]), 
							valueToProcess.split(",")[2], Integer.valueOf(valueToProcess.split(",")[3]),cricketService.getTeams(), match, session_selected_broadcaster);
					break;
				case "POPULATE-L3-BUG-DB":
					for(Bugs bug : cricketService.getBugs()) {
						  if(bug.getBugId() == Integer.valueOf(valueToProcess.split(",")[1])) {
							  populateBugsDB(print_writer, valueToProcess.split(",")[0], bug, match, session_selected_broadcaster);
						  }
						}
						break;
				case "POPULATE-L3-NAMESUPER":
					for(NameSuper ns : cricketService.getNameSupers()) {
					  if(ns.getNamesuperId() == Integer.valueOf(valueToProcess.split(",")[1])) {
						  populateNameSuper(print_writer, valueToProcess.split(",")[0], ns, match, session_selected_broadcaster);
					  }
					}
					break;
				case "POPULATE-L3-NAMESUPER-PLAYER":
					populateNameSuperPlayer(print_writer, valueToProcess.split(",")[0], Integer.valueOf(valueToProcess.split(",")[1]), 
							valueToProcess.split(",")[2], Integer.valueOf(valueToProcess.split(",")[3]), match, session_selected_broadcaster);
					break;
					
				case "POPULATE-FF-PLAYERPROFILE": case "POPULATE-FF-PLAYERPROFILEBALL":
					for(Statistics stats : cricketService.getAllStats()) {
						if(stats.getPlayer_id() == Integer.valueOf(valueToProcess.split(",")[1])) {
							stats.setStats_type(cricketService.getStatsType(stats.getStats_type_id()));
//							stats = CricketFunctions.updateTournamentDataWithStats(stats, tournament_matches, match);
//							stats = CricketFunctions.updateStatisticsWithMatchData(stats, match);
							
							stats = updateTournamentDataWithStats(stats, tournament_matches, match,valueToProcess.split(",")[2]);
							stats = updateStatisticsWithMatchData(stats, match,valueToProcess.split(",")[2]);
							if(stats.getStats_type().getStats_short_name().equalsIgnoreCase(valueToProcess.split(",")[2])) {
								populatePlayerProfile(print_writer,valueToProcess.split(",")[0],Integer.valueOf(valueToProcess.split(",")[1]),
										valueToProcess.split(",")[2],valueToProcess.split(",")[3],stats,match, session_selected_broadcaster);
							}
						}
					}
					break;
				case "POPULATE-FF-PLAYERPROFILE_BUKHATIR":
					for(Statistics stats : cricketService.getAllStats()) {
						if(stats.getPlayer_id() == Integer.valueOf(valueToProcess.split(",")[1])) {
							stats.setStats_type(cricketService.getStatsType(stats.getStats_type_id()));
//							stats = CricketFunctions.updateTournamentDataWithStats(stats, tournament_matches, match);
//							stats = CricketFunctions.updateStatisticsWithMatchData(stats, match);
							if(stats.getStats_type().getStats_short_name().equalsIgnoreCase(valueToProcess.split(",")[2])) {
								populatePlayerProfile(print_writer,valueToProcess.split(",")[0],Integer.valueOf(valueToProcess.split(",")[1]),
										valueToProcess.split(",")[2],valueToProcess.split(",")[3],stats,match, session_selected_broadcaster);
							}
						}
					}
					break;
				case "POPULATE-FF-DOUBLETEAMS":
					populateDoubleteams(print_writer,valueToProcess.split(",")[0], match, session_selected_broadcaster);
					break;
				case "POPULATE-L3-INFOBAR":
					infobar.setMiddle_section(valueToProcess.split(",")[1]);
					infobar.setTop_right_section(valueToProcess.split(",")[2]);
					infobar.setBottom_left_section(valueToProcess.split(",")[3]);
					infobar.setBottom_right_section(valueToProcess.split(",")[4]);
					
					populateInfobar(infobar, print_writer, valueToProcess.split(",")[0],match, session_selected_broadcaster);
					break;
				case "POPULATE-DIRECTOR":
					populateInfobarDirector(print_writer,valueToProcess,session_selected_broadcaster);
					break;
				case "POPULATE-INFOBAR-BOTTOMLEFT":
					if(infobar.getLast_bottom_right_bottom_section() != null && !infobar.getLast_bottom_right_bottom_section().isEmpty()) {
						infobar.setBottom_left_section(valueToProcess);
						infobar.setBottom_right_section("TOURNAMENT_NAME");
						processAnimation(print_writer, "Section6_Out", "START", session_selected_broadcaster,1);
						TimeUnit.MILLISECONDS.sleep(500);
						populateInfobarBottomLeft(infobar,false,print_writer, match, session_selected_broadcaster);
						populateInfobarBottomRight(infobar,false,print_writer, match, session_selected_broadcaster);
						processAnimation(print_writer, "Section4_In", "START", session_selected_broadcaster,1);
						processAnimation(print_writer, "Section5_In", "START", session_selected_broadcaster,1);
						infobar.setBottom_right_bottom_section("");
						infobar.setLast_bottom_right_bottom_section("");
						
					}else if (infobar.getLast_bottom_left_section() != null && !infobar.getLast_bottom_left_section().isEmpty()) {
						infobar.setBottom_left_section(valueToProcess);
						processAnimation(print_writer, "Section4_Out", "START", session_selected_broadcaster,1);
						TimeUnit.MILLISECONDS.sleep(500);
						populateInfobarBottomLeft(infobar,false,print_writer, match, session_selected_broadcaster);
						processAnimation(print_writer, "Section4_In", "START", session_selected_broadcaster,1);
					}
					break;
				case "POPULATE-INFOBAR-BOTTOMRIGHT":
					if(infobar.getLast_bottom_right_bottom_section() != null && !infobar.getLast_bottom_right_bottom_section().isEmpty()) {
						infobar.setBottom_right_section(valueToProcess);
						infobar.setBottom_left_section("VS_BOWLING_TEAM");
						processAnimation(print_writer, "Section6_Out", "START", session_selected_broadcaster,1);
						TimeUnit.MILLISECONDS.sleep(500);
						populateInfobarBottomLeft(infobar,false,print_writer, match, session_selected_broadcaster);
						populateInfobarBottomRight(infobar,false,print_writer, match, session_selected_broadcaster);
						processAnimation(print_writer, "Section5_In", "START", session_selected_broadcaster,1);
						processAnimation(print_writer, "Section4_In", "START", session_selected_broadcaster,1);
						infobar.setBottom_right_bottom_section("");
						infobar.setLast_bottom_right_bottom_section("");
						
					}else if (infobar.getLast_bottom_right_section() != null && !infobar.getLast_bottom_right_section().isEmpty()) {
						infobar.setBottom_right_section(valueToProcess);
						processAnimation(print_writer, "Section5_Out", "START", session_selected_broadcaster,1);
						TimeUnit.MILLISECONDS.sleep(500);
						populateInfobarBottomRight(infobar,false,print_writer, match, session_selected_broadcaster);
						processAnimation(print_writer, "Section5_In", "START", session_selected_broadcaster,1);
					}
					
					
					break;
				case "POPULATE-INFOBAR-TOPRIGHT":
					populateInfobarTopRight(infobar,false, print_writer, match, session_selected_broadcaster);
					break;
				case "POPULATE-INFOBAR-PROMPT":
					if(infobar.getLast_bottom_right_bottom_section() != null && !infobar.getLast_bottom_right_bottom_section().isEmpty()) {
						infobar.setBottom_left_section("VS_BOWLING_TEAM");
						infobar.setBottom_right_section("STATS");
						infobar.setLast_bottom_right_section("STATS");
						processAnimation(print_writer, "Section6_Out", "START", session_selected_broadcaster,1);
						TimeUnit.MILLISECONDS.sleep(500);
						populateInfobarBottomLeft(infobar,false,print_writer, match, session_selected_broadcaster);
						for(InfobarStats ibs : cricketService.getInfobarStats() ) {
							  if(ibs.getOrder() == Integer.valueOf(valueToProcess)) {
								  populateInfobarPrompt(false,print_writer, ibs, match, session_selected_broadcaster);
							  }
						}
						processAnimation(print_writer, "Section4_In", "START", session_selected_broadcaster,1);
						processAnimation(print_writer, "Section5_In", "START", session_selected_broadcaster,1);
						infobar.setBottom_right_bottom_section("");
						infobar.setLast_bottom_right_bottom_section("");
					}else if (infobar.getLast_bottom_right_section() != null && !infobar.getLast_bottom_right_section().isEmpty()) {
						processAnimation(print_writer, "Section5_Out", "START", session_selected_broadcaster,1);
						TimeUnit.MILLISECONDS.sleep(500);
						for(InfobarStats ibs : cricketService.getInfobarStats() ) {
							  if(ibs.getOrder() == Integer.valueOf(valueToProcess)) {
								  populateInfobarPrompt(false,print_writer, ibs, match, session_selected_broadcaster);
							  }
						}
						processAnimation(print_writer, "Section5_In", "START", session_selected_broadcaster,1);
						infobar.setBottom_right_section("STATS");
						infobar.setLast_bottom_right_section("STATS");
					}
					break;
					
				case "POPULATE-INFOBAR-BOTTOM":
					
					if(infobar.getLast_bottom_right_bottom_section() != null && !infobar.getLast_bottom_right_bottom_section().isEmpty()) {
						infobar.setBottom_right_bottom_section(valueToProcess);
						processAnimation(print_writer, "Section6_Out", "START", session_selected_broadcaster,1);
						TimeUnit.MILLISECONDS.sleep(500);
						populateInfobarBottom(infobar,false, print_writer, match, session_selected_broadcaster);
						processAnimation(print_writer, "Section6_In", "START", session_selected_broadcaster,1);
					}else {
						infobar.setBottom_right_bottom_section(valueToProcess);
						processAnimation(print_writer, "Section4_Out", "START", session_selected_broadcaster,1);
						processAnimation(print_writer, "Section5_Out", "START", session_selected_broadcaster,1);
						TimeUnit.MILLISECONDS.sleep(500);
						populateInfobarBottom(infobar,false, print_writer, match, session_selected_broadcaster);
						processAnimation(print_writer, "Section6_In", "START", session_selected_broadcaster,1);
						infobar.setBottom_left_section("");
						infobar.setLast_bottom_left_section("");
						infobar.setBottom_right_section("");
						infobar.setLast_bottom_right_section("");
					}
					

					break;
				case "POPULATE-FF-MATCHID":
					populateMatchId(print_writer,valueToProcess.split(",")[0],cricketService.getVariousTexts(), match, session_selected_broadcaster);
					break;
				case "POPULATE-LT-MATCHID":
					populateLTMatchId(print_writer,valueToProcess.split(",")[0], match, session_selected_broadcaster);
					break;
				case "POPULATE-MATCH_PROMO":
					populateMatchPromo(print_writer, valueToProcess.split(",")[0] ,Integer.valueOf(valueToProcess.split(",")[1]),cricketService.getTeams(),cricketService.getVariousTexts(),
							cricketService.getFixtures(),cricketService.getGrounds(),match , session_selected_broadcaster);
					break;
				case "POPULATE-FF-PLAYINGXI":
					populatePlayingXI(print_writer, valueToProcess.split(",")[0], Integer.valueOf(valueToProcess.split(",")[1]), 
							match, session_selected_broadcaster);
					break;
				case "POPULATE-FF-LEADERBOARD":
					populateLeaderBoard(print_writer, valueToProcess.split(",")[0], valueToProcess.split(",")[1], Integer.valueOf(valueToProcess.split(",")[2]),
							extractTournamentStats("COMBINED_PAST_CURRENT_MATCH_DATA", tournament_matches, cricketService,match,null),
							cricketService.getTeams(),match, session_selected_broadcaster);
					break;
				case "POPULATE-LT-PROJECTED":
					populateProjectedScore(print_writer,valueToProcess.split(",")[0], match, session_selected_broadcaster);
					break;
				case "POPULATE-L3-TARGET":
					populateTarget(print_writer,valueToProcess.split(",")[0], match, session_selected_broadcaster);
					break;
				case "POPULATE-FF-TARGET":
					populateFFTarget(print_writer,valueToProcess.split(",")[0], match, session_selected_broadcaster);
					break;
				case "POPULATE-EQUATION-FF":
					populateFFEquation(print_writer,valueToProcess.split(",")[0], match, session_selected_broadcaster);
					break;
				case "POPULATE-LT-EQUATION":
					populateLtEquation(print_writer,valueToProcess.split(",")[0], match, session_selected_broadcaster);
					break;
				case "POPULATE-MATCHSTATUS":
					populateLtMatchStatus(print_writer,valueToProcess.split(",")[0], match, session_selected_broadcaster);
					break;
				case "POPULATE-L3-TEAMSUMMARY":
					populateTeamSummary(print_writer, valueToProcess.split(",")[0], Integer.valueOf(valueToProcess.split(",")[1]), 
							match, session_selected_broadcaster);
					break;
				case "POPULATE-L3-PLAYERSUMMARY":
					populateLtBattingSummary(print_writer, valueToProcess.split(",")[0], Integer.valueOf(valueToProcess.split(",")[1]), Integer.valueOf(valueToProcess.split(",")[2]), 
							match, session_selected_broadcaster);
					break;
				case "POPULATE-L3-BOWLERDETAILS":
					populateLtBowlerSummary(print_writer, valueToProcess.split(",")[0], Integer.valueOf(valueToProcess.split(",")[1]), Integer.valueOf(valueToProcess.split(",")[2]), 
							match, session_selected_broadcaster);
					break;
				case "POPULATE-FOUR":
					populatetournamentFour(print_writer, valueToProcess.split(",")[0],tournament_matches,match, session_selected_broadcaster);
//					System.out.println("Four = " + CricketFunctions.gettournamentFoursAndSixes(tournament_matches, match));
					break;
				case "POPULATE-SIX":
					populatetournamentSix(print_writer, valueToProcess.split(",")[0],tournament_matches,match, session_selected_broadcaster);
//					System.out.println("Four = " + CricketFunctions.gettournamentFoursAndSixes(tournament_matches, match));
					break;	
				case "POPULATE-DRONE_BUG":
					populatedroneBug(print_writer, valueToProcess.split(",")[0],match, session_selected_broadcaster);
//					System.out.println("Four = " + CricketFunctions.gettournamentFoursAndSixes(tournament_matches, match));
					break;	
				case "POPULATE-L3-PLAYERPROFILE_BUKHATIR":
					for(Statistics stats : cricketService.getAllStats()) {
						if(stats.getPlayer_id() == Integer.valueOf(valueToProcess.split(",")[1])) {
							stats.setStats_type(cricketService.getStatsType(stats.getStats_type_id()));
							
//							stats = CricketFunctions.updateTournamentDataWithStats(stats, tournament_matches, match);
//							stats = CricketFunctions.updateStatisticsWithMatchData(stats, match);
							if(stats.getStats_type().getStats_short_name().equalsIgnoreCase(valueToProcess.split(",")[2])) {
								populateLTPlayerProfile(print_writer,valueToProcess.split(",")[0],
										valueToProcess.split(",")[2],valueToProcess.split(",")[3],stats,match, session_selected_broadcaster);
							}
						}
					}
					break;
				case "POPULATE-L3-PLAYERPROFILE": case "POPULATE-L3-PLAYERPROFILEBAT":
					//System.out.println("HELLO");
					for(Statistics stats : cricketService.getAllStats()) {
						if(stats.getPlayer_id() == Integer.valueOf(valueToProcess.split(",")[1])) {
							stats.setStats_type(cricketService.getStatsType(stats.getStats_type_id()));
							
							stats = CricketFunctions.updateTournamentDataWithStats(stats, tournament_matches, match);
							stats = CricketFunctions.updateStatisticsWithMatchData(stats, match);
							
							if(stats.getStats_type().getStats_short_name().equalsIgnoreCase(valueToProcess.split(",")[2])) {
								populateLTPlayerProfile(print_writer,valueToProcess.split(",")[0],
										valueToProcess.split(",")[2],valueToProcess.split(",")[3],stats,match, session_selected_broadcaster);
							}
						}
					}
					break;
				case "POPULATE-L3-FALLOFWICKET":
					populateFallofWicket(print_writer, valueToProcess.split(",")[0], Integer.valueOf(valueToProcess.split(",")[1]), match, session_selected_broadcaster);
					break;
				case "POPULATE-L3-SPLIT":
					populateSplit(print_writer, valueToProcess.split(",")[0], Integer.valueOf(valueToProcess.split(",")[1]), Integer.valueOf(valueToProcess.split(",")[2]), match, session_selected_broadcaster);
					break;
				case "POPULATE-L3-COMPARISION":
					populateComparision(print_writer,valueToProcess.split(",")[0], match, session_selected_broadcaster);
					break;
				case "POPULATE-BATSMAN_STYLE":
					populateBatsmanStyle(print_writer, valueToProcess.split(",")[0], Integer.valueOf(valueToProcess.split(",")[1]), Integer.valueOf(valueToProcess.split(",")[2]), cricketService.getAllPlayer(), cricketService.getTeams(), match, session_selected_broadcaster);
					break;
				case "POPULATE-BOWLER_STYLE":
					populateBowlerStyle(print_writer, valueToProcess.split(",")[0], Integer.valueOf(valueToProcess.split(",")[1]),Integer.valueOf(valueToProcess.split(",")[2]),cricketService.getAllPlayer(), cricketService.getTeams(), match, session_selected_broadcaster);
					break;
				case "POPULATE-TIEID-DOUBLE":
					populateTieIdDouble(print_writer, valueToProcess.split(",")[0],valueToProcess.split(",")[1],cricketService.getFixtures(),cricketService.getTeams(), match, session_selected_broadcaster);
					break;
				case "POPULATE-POINTS_TABLE":
					LeagueTable groupA = null;
					LeagueTable groupB = null;
					if(new File(CricketUtil.CRICKET_DIRECTORY + CricketUtil.LEAGUE_TABLE_DIRECTORY + "GroupA.xml").exists()) {
						groupA = (LeagueTable)JAXBContext.newInstance(LeagueTable.class).createUnmarshaller().unmarshal(
								new File(CricketUtil.CRICKET_DIRECTORY + CricketUtil.LEAGUE_TABLE_DIRECTORY + "GroupA.xml"));
					}
					
					if(new File(CricketUtil.CRICKET_DIRECTORY + CricketUtil.LEAGUE_TABLE_DIRECTORY + "GroupB.xml").exists()) {
						groupB = (LeagueTable)JAXBContext.newInstance(LeagueTable.class).createUnmarshaller().unmarshal(
								new File(CricketUtil.CRICKET_DIRECTORY + CricketUtil.LEAGUE_TABLE_DIRECTORY + "GroupB.xml"));
					}
					
					populatePointsTable(print_writer, valueToProcess.split(",")[0],groupA.getLeagueTeams(),groupB.getLeagueTeams(),session_selected_broadcaster,match);
					break;
				case "POPULATE-PREVIOUS_SUMMARY":
					List<MatchAllData> cricket_matches = new ArrayList<MatchAllData>();
					cricket_matches.clear();
					MatchAllData cricket_match = new MatchAllData();
					for(File file :  new File(CricketUtil.CRICKET_SERVER_DIRECTORY + CricketUtil.MATCHES_DIRECTORY).listFiles(new FileFilter() {
						@Override
					    public boolean accept(File pathname) {
					        String name = pathname.getName().toLowerCase();
					        return name.endsWith(".xml") && pathname.isFile();
					    }
					})) {
						cricket_match = (CricketFunctions.populateMatchVariables(cricketService,(MatchAllData) JAXBContext.newInstance(MatchAllData.class).createUnmarshaller().unmarshal(
										new File(CricketUtil.CRICKET_SERVER_DIRECTORY + CricketUtil.MATCHES_DIRECTORY + file.getName()))));
							for(Fixture fx : cricketService.getFixtures()) {
								//System.out.println(valueToProcess.split(",")[1]);
								if(fx.getMatchnumber() == Integer.valueOf(valueToProcess.split(",")[1])) {
									if(cricket_match.getMatch().getMatchFileName().replace(".xml", "").equalsIgnoreCase(fx.getMatchfilename()) 
											&& cricket_match.getSetup().getHomeTeam().getTeamId() == fx.getHometeamid() 
											&& cricket_match.getSetup().getAwayTeam().getTeamId() == fx.getAwayteamid())
									{
										cricket_matches.add(cricket_match);
									}
								}
							}
					}
					//System.out.println("id-" + Integer.valueOf(valueToProcess.split(",")[1]));
					//System.out.println("Player-" + valueToProcess.split(",")[2]);
					populatePreviousSummary(print_writer, valueToProcess.split(",")[0] ,Integer.valueOf(valueToProcess.split(",")[1]),cricket_matches,cricketService.getFixtures(), 
							match, session_selected_broadcaster);
					break;
				case "POPULATE-L3-GENERIC":
					populateGeneric(print_writer, valueToProcess.split(",")[0],valueToProcess.split(",")[1], match, session_selected_broadcaster);
					break;
				case "POPULATE-MANHATTAN":
					populateManhattan(print_writer, valueToProcess.split(",")[0], Integer.valueOf(valueToProcess.split(",")[1]), match, session_selected_broadcaster);
					break;
				case "POPULATE-WORM":
					populateWorm(print_writer, valueToProcess.split(",")[0], Integer.valueOf(valueToProcess.split(",")[1]), match, session_selected_broadcaster);
					break;
				}
				//return JSONObject.fromObject(this_doad).toString();
			}
		case "ANIMATE-IN-SCORECARD": case "ANIMATE-IN-BOWLINGCARD": case "ANIMATE-IN-PARTNERSHIP": case "ANIMATE-IN-MATCHSUMARRY": case "ANIMATE-IN-BUG": case "ANIMATE-IN-HOWOUT": 
		case "ANIMATE-IN-BATSMANSTATS":	case "ANIMATE-IN-NAMESUPER": case "ANIMATE-IN-NAMESUPER-PLAYER": case "ANIMATE-IN-PLAYERPROFILE": case "ANIMATE-IN-PLAYERPROFILEBALL":
		case "ANIMATE-IN-DOUBLETEAMS": case "ANIMATE-IN-INFOBAR":  
		case "ANIMATE-IN-MATCHID": case "ANIMATE-IN-PLAYINGXI": case "ANIMATE-IN-PLAYING_CHANGE_ON1": case "ANIMATE-IN-PLAYING_CHANGE_ON2": case "ANIMATE-IN-L3MATCHID": 
		case "ANIMATE-IN-LEADERBOARD": case "ANIMATE-IN-PROJECTED": case "ANIMATE-IN-TARGET": case "ANIMATE-IN-TEAMSUMMARY":
		case "ANIMATE-IN-PLAYERSUMMARY": case "ANIMATE-IN-L3PLAYERPROFILE": case "ANIMATE-IN-PLAYERPROFILEBAT": case "ANIMATE-IN-FALLOFWICKET": case "ANIMATE-IN-COMPARISION": 
		case "ANIMATE-IN-BOWLERSTATS": case "ANIMATE-IN-SPLIT": case "ANIMATE-IN-BUG-DB": case "ANIMATE-IN-BUG-DISMISSAL": case "ANIMATE-OUT": case "CLEAR-ALL": case "ANIMATE-IN-BUG-BOWLER":
		case "ANIMATE-IN-BUGTARGET": case "ANIMATE-IN-BOWLERSUMMARY": case "ANIMATE-IN-NEXT_TO_BAT": case "ANIMATE-IN-BOWLERDETAILS": case "ANIMATE-IN-LTPOWERPLAY":
		case "ANIMATE-IN-LANDMARK":	case "ANIMATE-IN-EQUATION": case "ANIMATE-IN-POSITION_LANDMARK": case "ANIMATE-IN-BATSMAN_THIS_MATCH": case "ANIMATE-IN-BOWLER_THIS_MATCH":
		case "ANIMATE-IN-POINTSTABLE": case "ANIMATE-IN-LTPOINTSTABLE":	case "ANIMATE-IN-BOWLER_STYLE": case "ANIMATE-IN-MANHATTAN": case "ANIMATE-IN-HOWOUT_WITHOUT_FIELDER":
		case "ANIMATE-IN-BATSMAN_STYLE": case "ANIMATE-IN-MATCH_PROMO": case "ANIMATE-IN-TEAMS_LOGO": case "ANIMATE-IN-PREVIOUS_SUMMARY": case "ANIMATE-IN-TIEID-DOUBLE": case "ANIMATE-IN-GENERIC":
		case "ANIMATE-IN-MOSTRUNS": case "ANIMATE-IN-MOSTWICKETS": case "ANIMATE-IN-MOSTFOURS": case "ANIMATE-IN-MOSTSIXES": case "ANIMATE-IN-HIGHESTSCORE": case "ANIMATE-IN-FF_TARGET":
		case "ANIMATE-IN-THISOVER": case "ANIMATE-IN-HOWOUT_QUICK": case "ANIMATE-IN-LTPARTNERSHIP": case "ANIMATE-IN-WORM": case "ANIMATE-IN-MATCHSTATUS": case "ANIMATE-IN-HOWOUT_BOTH":
		case "ANIMATE-IN-BATSMANSTATS_BOTH": case "ANIMATE-IN-SPONSOR": case "ANIMATE-OUT-SPONSOR": case "ANIMATE-IN-THIS_SESSION": case "ANIMATE-IN-SESSION": case "ANIMATE-IN-FF_EQUATION":
		case "ANIMATE-IN-BUG-TOSS": case "TICKER_LT_OUT": case "TICKER_LT_IN": case "ANIMATE-IN-HOWOUT_WITHOUT_FIELDER_QUICK": case "ANIMATE-IN-BUG_POWERPLAY": case "ANIMATE-IN-BUG_PARTNERSHIP":
		case "ANIMATE-IN-THISSERIES": case "ANIMATE-IN-FOUR": case "ANIMATE-IN-SIX": case "ANIMATE-IN-DRONE_BUG": case "ANIMATE-IN-BUG_HIGHLIGHT": case "ANIMATE-IN-DLS":
		case "ANIMATE-IN-DLS-EQUATION":
		
			switch (session_selected_broadcaster.toUpperCase()) {
			case "FRUIT":
				switch (whatToProcess.toUpperCase()) {
				case "ANIMATE-IN-SCORECARD": case "ANIMATE-IN-BOWLINGCARD": case "ANIMATE-IN-PARTNERSHIP": case "ANIMATE-IN-MATCHSUMARRY": case "ANIMATE-IN-BUG": case "ANIMATE-IN-HOWOUT": 
				case "ANIMATE-IN-BATSMANSTATS":	case "ANIMATE-IN-NAMESUPER": case "ANIMATE-IN-NAMESUPER-PLAYER": case "ANIMATE-IN-PLAYERPROFILE": case "ANIMATE-IN-PLAYERPROFILEBALL":
				case "ANIMATE-IN-DOUBLETEAMS":
				case "ANIMATE-IN-MATCHID": case "ANIMATE-IN-PLAYINGXI": case "ANIMATE-IN-LEADERBOARD": case "ANIMATE-IN-PROJECTED": case "ANIMATE-IN-TARGET": case "ANIMATE-IN-TEAMSUMMARY":
				case "ANIMATE-IN-PLAYERSUMMARY": case "ANIMATE-IN-L3PLAYERPROFILE": case "ANIMATE-IN-PLAYERPROFILEBAT": case "ANIMATE-IN-FALLOFWICKET": case "ANIMATE-IN-COMPARISION": case "ANIMATE-IN-L3MATCHID": 
				case "ANIMATE-IN-BOWLERSTATS": case "ANIMATE-IN-SPLIT": case "ANIMATE-IN-BUG-DB": case "ANIMATE-IN-BUG-DISMISSAL": case "ANIMATE-IN-BUG-BOWLER":
				case "ANIMATE-IN-BUGTARGET": case "ANIMATE-IN-BOWLERSUMMARY": case "ANIMATE-IN-NEXT_TO_BAT": case "ANIMATE-IN-BOWLERDETAILS": case "ANIMATE-IN-LTPOWERPLAY": case "ANIMATE-IN-QUICK_HOWOUT":
				case "ANIMATE-IN-LANDMARK":	case "ANIMATE-IN-EQUATION": case "ANIMATE-IN-POSITION_LANDMARK": case "ANIMATE-IN-BATSMAN_THIS_MATCH": case "ANIMATE-IN-BOWLER_THIS_MATCH":
				case "ANIMATE-IN-POINTSTABLE": case "ANIMATE-IN-LTPOINTSTABLE":	case "ANIMATE-IN-BOWLER_STYLE": case "ANIMATE-IN-MANHATTAN": case "ANIMATE-IN-HOWOUT_WITHOUT_FIELDER":
				case "ANIMATE-IN-BATSMAN_STYLE": case "ANIMATE-IN-MATCH_PROMO": case "ANIMATE-IN-TEAMS_LOGO": case "ANIMATE-IN-PREVIOUS_SUMMARY": case "ANIMATE-IN-TIEID-DOUBLE": case "ANIMATE-IN-GENERIC":
				case "ANIMATE-IN-MOSTRUNS": case "ANIMATE-IN-MOSTWICKETS": case "ANIMATE-IN-MOSTFOURS": case "ANIMATE-IN-MOSTSIXES": case "ANIMATE-IN-HIGHESTSCORE": case "ANIMATE-IN-FF_TARGET":
				case "ANIMATE-IN-THISOVER": case "ANIMATE-IN-HOWOUT_QUICK": case "ANIMATE-IN-LTPARTNERSHIP": case "ANIMATE-IN-WORM": case "ANIMATE-IN-MINI-BOWLINGCARD": case "ANIMATE-IN-SQUAD":
				case "ANIMATE-IN-FF_THIS-SERIES": case "ANIMATE-IN-MINI-SCORECARD": case "ANIMATE-IN-LT_THIS-SERIES": case "ANIMATE-IN-LTMATCH_PROMO": case "ANIMATE-IN-BUG_POWERPLAY":
				case "ANIMATE-IN-MATCHSTATUS":	case "ANIMATE-IN-HOWOUT_BOTH": case "ANIMATE-IN-BATSMANSTATS_BOTH": case "ANIMATE-IN-THIS_SESSION": case "ANIMATE-IN-SESSION": case "ANIMATE-IN-FF_EQUATION":
				case "ANIMATE-IN-BUG-TOSS":	case "ANIMATE-IN-HOWOUT_WITHOUT_FIELDER_QUICK": case "ANIMATE-IN-BUG_PARTNERSHIP": case "ANIMATE-IN-THISSERIES": case "ANIMATE-IN-DRONE_BUG":
				case "ANIMATE-IN-BUG_HIGHLIGHT": case "ANIMATE-IN-DLS": case "ANIMATE-IN-DLS-EQUATION":
					
					if(which_graphics_onscreen == "BOWLINGCARD" || which_graphics_onscreen == "SUMARRY" || which_graphics_onscreen == "POINTSTABLE" || which_graphics_onscreen == "SCORECARD") {
						//print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 30.0;");
					}else {
						if(infobar.isInfobar_on_screen() == true) {
							processAnimation(print_writer, "FF_In", "START", session_selected_broadcaster,1);
							TimeUnit.MILLISECONDS.sleep(200);
						}
					}
					
					break;
				}
				switch (whatToProcess.toUpperCase()) {
				case "ANIMATE-IN-PLAYING_CHANGE_ON1":
					print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In COUNTINUE;");
					//processAnimation(print_writer, "In", "COUNTINUE", session_selected_broadcaster,2);
					break;
				case "ANIMATE-IN-PLAYING_CHANGE_ON2":
					print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In COUNTINUE;");
					//processAnimation(print_writer, "In", "COUNTINUE", session_selected_broadcaster,2);
					break;
				case "ANIMATE-IN-SCORECARD":
					if(which_graphics_onscreen == "BOWLINGCARD") {
						processAnimation(print_writer, "BowlingCardOut", "START", session_selected_broadcaster,2);
						TimeUnit.SECONDS.sleep(1);
						if(match.getMatch().getInning().get(0).getBattingCard().size() == 12) {
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$BattingCard_GRP$DataAll$BAtting$Batting_Card$12*CONTAINER SET ACTIVE 1;");
							processAnimation(print_writer, "CON1In", "START", session_selected_broadcaster,2);
						}
						else if(match.getMatch().getInning().get(1).getBattingCard().size() == 12) {
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$BattingCard_GRP$DataAll$BAtting$Batting_Card$12*CONTAINER SET ACTIVE 1;");
							processAnimation(print_writer, "CON1In", "START", session_selected_broadcaster,2);
						}
						if(match.getMatch().getInning().get(0).getBattingCard().size() == 13) {
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$BattingCard_GRP$DataAll$BAtting$Batting_Card$12*CONTAINER SET ACTIVE 1;");
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$BattingCard_GRP$DataAll$BAtting$Batting_Card$13*CONTAINER SET ACTIVE 1;");
							processAnimation(print_writer, "CON2In", "START", session_selected_broadcaster,2);
						}
						else if(match.getMatch().getInning().get(1).getBattingCard().size() == 13) {
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$BattingCard_GRP$DataAll$BAtting$Batting_Card$12*CONTAINER SET ACTIVE 1;");
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$BattingCard_GRP$DataAll$BAtting$Batting_Card$13*CONTAINER SET ACTIVE 1;");
							processAnimation(print_writer, "CON2In", "START", session_selected_broadcaster,2);
						}
					}else if(which_graphics_onscreen == "SUMARRY") {
						processAnimation(print_writer, "SummaryOut", "START", session_selected_broadcaster,2);
						TimeUnit.SECONDS.sleep(1);
						if(match.getMatch().getInning().get(0).getBattingCard().size() == 12) {
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$BattingCard_GRP$DataAll$BAtting$Batting_Card$12*CONTAINER SET ACTIVE 1;");
							processAnimation(print_writer, "CON1In", "START", session_selected_broadcaster,2);
						}
						else if(match.getMatch().getInning().get(1).getBattingCard().size() == 12) {
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$BattingCard_GRP$DataAll$BAtting$Batting_Card$12*CONTAINER SET ACTIVE 1;");
							processAnimation(print_writer, "CON1In", "START", session_selected_broadcaster,2);
						}
						if(match.getMatch().getInning().get(0).getBattingCard().size() == 13) {
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$BattingCard_GRP$DataAll$BAtting$Batting_Card$12*CONTAINER SET ACTIVE 1;");
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$BattingCard_GRP$DataAll$BAtting$Batting_Card$13*CONTAINER SET ACTIVE 1;");
							processAnimation(print_writer, "CON2In", "START", session_selected_broadcaster,2);
						}
						else if(match.getMatch().getInning().get(1).getBattingCard().size() == 13) {
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$BattingCard_GRP$DataAll$BAtting$Batting_Card$12*CONTAINER SET ACTIVE 1;");
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$BattingCard_GRP$DataAll$BAtting$Batting_Card$13*CONTAINER SET ACTIVE 1;");
							processAnimation(print_writer, "CON2In", "START", session_selected_broadcaster,2);
						}
					}else if(which_graphics_onscreen == "POINTSTABLE") {
						processAnimation(print_writer, "PointsTableOut", "START", session_selected_broadcaster,2);
						TimeUnit.SECONDS.sleep(1);
						if(match.getMatch().getInning().get(0).getBattingCard().size() == 12) {
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$BattingCard_GRP$DataAll$BAtting$Batting_Card$12*CONTAINER SET ACTIVE 1;");
							processAnimation(print_writer, "CON1In", "START", session_selected_broadcaster,2);
						}
						else if(match.getMatch().getInning().get(1).getBattingCard().size() == 12) {
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$BattingCard_GRP$DataAll$BAtting$Batting_Card$12*CONTAINER SET ACTIVE 1;");
							processAnimation(print_writer, "CON1In", "START", session_selected_broadcaster,2);
						}
						if(match.getMatch().getInning().get(0).getBattingCard().size() == 13) {
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$BattingCard_GRP$DataAll$BAtting$Batting_Card$12*CONTAINER SET ACTIVE 1;");
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$BattingCard_GRP$DataAll$BAtting$Batting_Card$13*CONTAINER SET ACTIVE 1;");
							processAnimation(print_writer, "CON2In", "START", session_selected_broadcaster,2);
						}
						else if(match.getMatch().getInning().get(1).getBattingCard().size() == 13) {
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$BattingCard_GRP$DataAll$BAtting$Batting_Card$12*CONTAINER SET ACTIVE 1;");
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$BattingCard_GRP$DataAll$BAtting$Batting_Card$13*CONTAINER SET ACTIVE 1;");
							processAnimation(print_writer, "CON2In", "START", session_selected_broadcaster,2);
						}
					}else {
						if(match.getMatch().getInning().get(0).getBattingCard().size() == 12) {
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$BattingCard_GRP$DataAll$BAtting$Batting_Card$12*CONTAINER SET ACTIVE 1;");
							processAnimation(print_writer, "CON1In", "START", session_selected_broadcaster,2);
						}
						else if(match.getMatch().getInning().get(1).getBattingCard().size() == 12) {
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$BattingCard_GRP$DataAll$BAtting$Batting_Card$12*CONTAINER SET ACTIVE 1;");
							processAnimation(print_writer, "CON1In", "START", session_selected_broadcaster,2);
						}
						if(match.getMatch().getInning().get(0).getBattingCard().size() == 13) {
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$BattingCard_GRP$DataAll$BAtting$Batting_Card$12*CONTAINER SET ACTIVE 1;");
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$BattingCard_GRP$DataAll$BAtting$Batting_Card$13*CONTAINER SET ACTIVE 1;");
							processAnimation(print_writer, "CON2In", "START", session_selected_broadcaster,2);
						}
						else if(match.getMatch().getInning().get(1).getBattingCard().size() == 13) {
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$BattingCard_GRP$DataAll$BAtting$Batting_Card$12*CONTAINER SET ACTIVE 1;");
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$BattingCard_GRP$DataAll$BAtting$Batting_Card$13*CONTAINER SET ACTIVE 1;");
							processAnimation(print_writer, "CON2In", "START", session_selected_broadcaster,2);
						}
						processAnimation(print_writer, "In", "START", session_selected_broadcaster,2);
					}
					
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$BattingCard_GRP*CONTAINER SET ACTIVE 1;");
					processAnimation(print_writer, "BattingCardIn", "START", session_selected_broadcaster,2);
					print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*LOOP START;");
					which_graphics_onscreen = "SCORECARD";
					break;
				case "ANIMATE-IN-BOWLINGCARD":
//					System.out.println("ANIMATE-IN-BOWLINGCARD = " + which_graphics_onscreen);
//					if(which_graphics_onscreen == "") {
//						processAnimation(print_writer, "In", "START", session_selected_broadcaster,2);
//						print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*LOOP START;");
//						print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*FF_Change SHOW 0.0;");
//					}else {
//						processAnimation(print_writer, "FF_Change", "START", session_selected_broadcaster,2);
////						print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*LOOP START;");
//						TimeUnit.SECONDS.sleep(4);
//						populateBowlingcard(print_writer, "D:/DOAD_In_House_Everest/Everest_Cricket/EVEREST_APL2022/Scenes/Bat_Ball_Summ_PTable_Change.sum", 
//								false, Whichinn, match, session_selected_broadcaster,1);
//						TimeUnit.SECONDS.sleep(4);
//						print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*FF_Change SHOW 0.0;");
//					}
					
					if(which_graphics_onscreen == "SCORECARD") {
						processAnimation(print_writer, "BattingCardOut", "START", session_selected_broadcaster,2);
						TimeUnit.SECONDS.sleep(1);
					}else if(which_graphics_onscreen == "SUMARRY") {
						processAnimation(print_writer, "SummaryOut", "START", session_selected_broadcaster,2);
						TimeUnit.SECONDS.sleep(1);
					}else if(which_graphics_onscreen == "POINTSTABLE") {
						processAnimation(print_writer, "PointsTableOut", "START", session_selected_broadcaster,2);
						TimeUnit.SECONDS.sleep(1);
					}else {
						processAnimation(print_writer, "In", "START", session_selected_broadcaster,2);
					}
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$BowlingCard_GRP*CONTAINER SET ACTIVE 1;");
					processAnimation(print_writer, "BowlingCardIn", "START", session_selected_broadcaster,2);
					print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*LOOP START;");
					which_graphics_onscreen = "BOWLINGCARD";
					break;
				case "ANIMATE-IN-PARTNERSHIP":
					if(match.getMatch().getInning().get(0).getBattingCard().size() == 12) {
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$12*CONTAINER SET ACTIVE 1;");
						processAnimation(print_writer, "CON1In", "START", session_selected_broadcaster,2);
					}
					else if(match.getMatch().getInning().get(1).getBattingCard().size() == 12) {
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$12*CONTAINER SET ACTIVE 1;");
						processAnimation(print_writer, "CON1In", "START", session_selected_broadcaster,2);
					}
					if(match.getMatch().getInning().get(0).getBattingCard().size() == 13) {
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$13*CONTAINER SET ACTIVE 1;");
						processAnimation(print_writer, "CON2In", "START", session_selected_broadcaster,2);
					}
					else if(match.getMatch().getInning().get(1).getBattingCard().size() == 13) {
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$13*CONTAINER SET ACTIVE 1;");
						processAnimation(print_writer, "CON2In", "START", session_selected_broadcaster,2);
					}
					processAnimation(print_writer, "In", "START", session_selected_broadcaster,2);
					print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*LOOP START;");
					print_writer.println("LAYER2*EVEREST*STAGE START;");
					which_graphics_onscreen = "PARTNERSHIP";
					break;
				case "ANIMATE-IN-LTPARTNERSHIP":
					processAnimation(print_writer, "In", "START", session_selected_broadcaster,2);
					print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*LOOP START;");
					which_graphics_onscreen = "CURRENT_PARTNERSHIP";
					break;
				case "ANIMATE-IN-MATCHSUMARRY":
					if(which_graphics_onscreen == "SCORECARD") {
						processAnimation(print_writer, "BattingCardOut", "START", session_selected_broadcaster,2);
						TimeUnit.SECONDS.sleep(1);
					}else if(which_graphics_onscreen == "BOWLINGCARD") {
						processAnimation(print_writer, "BowlingCardOut", "START", session_selected_broadcaster,2);
						TimeUnit.SECONDS.sleep(1);
					}else if(which_graphics_onscreen == "POINTSTABLE") {
						processAnimation(print_writer, "PointsTableOut", "START", session_selected_broadcaster,2);
						TimeUnit.SECONDS.sleep(1);
					}else {
						processAnimation(print_writer, "In", "START", session_selected_broadcaster,2);
					}
					
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$Summary_GRP*CONTAINER SET ACTIVE 1;");
					processAnimation(print_writer, "SummaryIn", "START", session_selected_broadcaster,2);
					print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*LOOP START;");
					
					which_graphics_onscreen = "SUMARRY";
					break;
				case "ANIMATE-IN-SESSION":
					processAnimation(print_writer, "In", "START", session_selected_broadcaster,2);
					which_graphics_onscreen = "SESSION";
					break;
				case "ANIMATE-IN-TEAMS_LOGO":
					processAnimation(print_writer, "In", "START", session_selected_broadcaster,2);
					print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*LOOP START;");
					which_graphics_onscreen = "TEAMS_LOGO";
					break;
				case "ANIMATE-IN-DLS":
					processAnimation(print_writer, "In", "START", session_selected_broadcaster,2);
					which_graphics_onscreen = "DLS";
					break;
				case "ANIMATE-IN-DLS-EQUATION":
					processAnimation(print_writer, "In", "START", session_selected_broadcaster,2);
					which_graphics_onscreen = "DLS_EQUATION";
					break;
				case "ANIMATE-IN-SPONSOR":
					processAnimation(print_writer, "Sponsor_In", "START", session_selected_broadcaster,1);
					//which_graphics_onscreen = "SPONSOR";
					break;
				case "ANIMATE-IN-THIS_SESSION":
					processAnimation(print_writer, "In", "START", session_selected_broadcaster,2);
					which_graphics_onscreen = "THIS_SESSION";
					break;
				case "ANIMATE-IN-BOWLERDETAILS":
					processAnimation(print_writer, "In", "START", session_selected_broadcaster,2);
					which_graphics_onscreen = "BOWLERDETAILS";
					break;
				case "ANIMATE-IN-BUG_HIGHLIGHT":
					processAnimation(print_writer, "In", "START", session_selected_broadcaster,2);
					which_graphics_onscreen = "BUG_HIGHLIGHT";
					break;
				case "ANIMATE-IN-BUG_POWERPLAY":
					processAnimation(print_writer, "In", "START", session_selected_broadcaster,2);
					which_graphics_onscreen = "BUG_POWERPLAY";
					break;
				case "ANIMATE-IN-BUG-TOSS":
					processAnimation(print_writer, "In", "START", session_selected_broadcaster,2);
					which_graphics_onscreen = "BUG-TOSS";
					break;
				case "ANIMATE-IN-BUG_PARTNERSHIP":
					processAnimation(print_writer, "In", "START", session_selected_broadcaster,2);
					which_graphics_onscreen = "BUG_PARTNERSHIP";
					break;
				case "ANIMATE-IN-FOUR":
					processAnimation(print_writer, "In", "START", session_selected_broadcaster,2);
					which_graphics_onscreen = "FOUR";
					break;
				case "ANIMATE-IN-SIX":
					processAnimation(print_writer, "In", "START", session_selected_broadcaster,2);
					which_graphics_onscreen = "SIX";
					break;	
				case "ANIMATE-IN-DRONE_BUG":
					processAnimation(print_writer, "In", "START", session_selected_broadcaster,2);
					which_graphics_onscreen = "DRONE_BUG";
					break;
				case "ANIMATE-IN-BUG":
					processAnimation(print_writer, "In", "START", session_selected_broadcaster,2);
					which_graphics_onscreen = "BUG";
					break;
				case "ANIMATE-IN-BUG-DISMISSAL":
					processAnimation(print_writer, "In", "START", session_selected_broadcaster,2);
					which_graphics_onscreen = "BUG-DISMISSAL";
					break;
				case "ANIMATE-IN-BUG-BOWLER":
					processAnimation(print_writer, "In", "START", session_selected_broadcaster,2);
					which_graphics_onscreen = "BUG-BOWLER";
					break;
				case "ANIMATE-IN-HOWOUT":
					processAnimation(print_writer, "In", "START", session_selected_broadcaster,2);
					print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*LOOP START;");
					which_graphics_onscreen = "HOWOUT";
					break;
				case "ANIMATE-IN-HOWOUT_BOTH":
					processAnimation(print_writer, "In", "START", session_selected_broadcaster,2);
					print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*LOOP START;");
					which_graphics_onscreen = "HOWOUT_BOTH";
					break;
				case "ANIMATE-IN-HOWOUT_QUICK":
					processAnimation(print_writer, "In", "START", session_selected_broadcaster,2);
					print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*LOOP START;");
					which_graphics_onscreen = "L3HOWOUT";
					break;
				case "ANIMATE-IN-HOWOUT_WITHOUT_FIELDER_QUICK":
					processAnimation(print_writer, "In", "START", session_selected_broadcaster,2);
					print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*LOOP START;");
					which_graphics_onscreen = "HOWOUT_WITHOUT_FIELDER_QUICK";
					break;
				case "ANIMATE-IN-HOWOUT_WITHOUT_FIELDER":
					processAnimation(print_writer, "In", "START", session_selected_broadcaster,2);
					print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*LOOP START;");
					which_graphics_onscreen = "HOWOUT_WITHOUT_FIELDER";
					break;
				case "ANIMATE-IN-MATCHSTATUS":
					processAnimation(print_writer, "In", "START", session_selected_broadcaster,2);
					which_graphics_onscreen = "MATCHSTATUS";
					break;
				case "ANIMATE-IN-BATSMANSTATS":
					processAnimation(print_writer, "In", "START", session_selected_broadcaster,2);
					print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*LOOP START;");
					which_graphics_onscreen = "BATSMANSTATS";
					break;
				case "ANIMATE-IN-BATSMANSTATS_BOTH":
					processAnimation(print_writer, "In", "START", session_selected_broadcaster,2);
					print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*LOOP START;");
					which_graphics_onscreen = "BATSMANSTATS_BOTH";
					break;
				case "ANIMATE-IN-BOWLERSTATS":
					processAnimation(print_writer, "In", "START", session_selected_broadcaster,2);
					print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*LOOP START;");
					which_graphics_onscreen = "BOWLERSTATS";
					break;
				case "ANIMATE-IN-BUG-DB":
					processAnimation(print_writer, "In", "START", session_selected_broadcaster,2);
					which_graphics_onscreen = "BUG-DB";
					break;
				case "ANIMATE-IN-NAMESUPER":
					processAnimation(print_writer, "In", "START", session_selected_broadcaster,2);
					print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*LOOP START;");
					which_graphics_onscreen = "NAMESUPER";
					break;
				case "ANIMATE-IN-NAMESUPER-PLAYER":
					processAnimation(print_writer, "In", "START", session_selected_broadcaster,2);
					print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*LOOP START;");
					which_graphics_onscreen = "NAMESUPER-PLAYER";
					break;
				case "ANIMATE-IN-PLAYERPROFILE": case "ANIMATE-IN-PLAYERPROFILEBALL":
					processAnimation(print_writer, "In", "START", session_selected_broadcaster,2);
					print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*LOOP START;");
					which_graphics_onscreen = "PLAYERPROFILE";
					break;
				case "ANIMATE-IN-DOUBLETEAMS":
					processAnimation(print_writer, "In", "START", session_selected_broadcaster,2);
					print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*LOOP START;");
					print_writer.println("LAYER2*EVEREST*STAGE START;");
					which_graphics_onscreen = "DOUBLETEAMS";
					break;
				case "ANIMATE-IN-INFOBAR":
					processAnimation(print_writer, "In", "START", session_selected_broadcaster,1);
					infobar.setInfobar_on_screen(true);
					which_graphics_onscreen = "INFOBAR";
					break;
				case "ANIMATE-IN-MATCH_PROMO":
					processAnimation(print_writer, "In", "START", session_selected_broadcaster,2);
					print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*LOOP START;");
					print_writer.println("LAYER2*EVEREST*STAGE START;");
					which_graphics_onscreen = "MATCH_PROMO";
					break;
				case "ANIMATE-IN-MATCHID":
					processAnimation(print_writer, "In", "START", session_selected_broadcaster,2);
					print_writer.println("LAYER2*EVEREST*STAGE START;");
					which_graphics_onscreen = "MATCHID";
					break;
				case "ANIMATE-IN-L3MATCHID":
					processAnimation(print_writer, "In", "START", session_selected_broadcaster,2);
					print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*LOOP START;");
					which_graphics_onscreen = "L3MATCHID";
					break;
				case "ANIMATE-IN-PLAYINGXI":
					processAnimation(print_writer, "In", "START", session_selected_broadcaster,2);
					print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*LOOP START;");
					print_writer.println("LAYER2*EVEREST*STAGE START;");
					which_graphics_onscreen = "PLAYINGXI";
					break;
				case "ANIMATE-IN-LEADERBOARD":
					processAnimation(print_writer, "In", "START", session_selected_broadcaster,2);
					print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*LOOP START;");
					which_graphics_onscreen = "LEADERBOARD";
					break;
				case "ANIMATE-IN-PROJECTED":
					processAnimation(print_writer, "In", "START", session_selected_broadcaster,2);
					print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*LOOP START;");
					which_graphics_onscreen = "PROJECTED";
					break;
				case "ANIMATE-IN-TARGET":
					processAnimation(print_writer, "In", "START", session_selected_broadcaster,2);
					which_graphics_onscreen = "TARGET";
					break;
				case "ANIMATE-IN-FF_TARGET":
					processAnimation(print_writer, "In", "START", session_selected_broadcaster,2);
					print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*LOOP START;");
					which_graphics_onscreen = "FF_TARGET";
					break;
				case "ANIMATE-IN-FF_EQUATION":
					processAnimation(print_writer, "In", "START", session_selected_broadcaster,2);
					print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*LOOP START;");
					which_graphics_onscreen = "FF_EQUATION";
					break;
				case "ANIMATE-IN-EQUATION":
					processAnimation(print_writer, "In", "START", session_selected_broadcaster,2);
					print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*LOOP START;");
					which_graphics_onscreen = "EQUATION";
					break;
				case "ANIMATE-IN-TEAMSUMMARY":
					processAnimation(print_writer, "In", "START", session_selected_broadcaster,2);
					print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*LOOP START;");
					which_graphics_onscreen = "TEAMSUMMARY";
					break;
				case "ANIMATE-IN-PLAYERSUMMARY":
					processAnimation(print_writer, "In", "START", session_selected_broadcaster,2);
					print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*LOOP START;");
					which_graphics_onscreen = "PLAYERSUMMARY";
					break;
				case "ANIMATE-IN-L3PLAYERPROFILE": case "ANIMATE-IN-PLAYERPROFILEBAT":
					processAnimation(print_writer, "In", "START", session_selected_broadcaster,2);
					print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*LOOP START;");
					which_graphics_onscreen = "L3PLAYERPROFILE";
					break;
				case "ANIMATE-IN-THISSERIES":
					processAnimation(print_writer, "In", "START", session_selected_broadcaster,2);
					print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*LOOP START;");
					which_graphics_onscreen = "THISSERIES";
					break;
				case "ANIMATE-IN-FALLOFWICKET":
					processAnimation(print_writer, "In", "START", session_selected_broadcaster,2);
					print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*LOOP START;");
					which_graphics_onscreen = "FALLOFWICKET";
					break;
				case "ANIMATE-IN-SPLIT":
					processAnimation(print_writer, "In", "START", session_selected_broadcaster,2);
					print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*LOOP START;");
					which_graphics_onscreen = "SPLIT";
					break;
				case "ANIMATE-IN-COMPARISION":
					processAnimation(print_writer, "In", "START", session_selected_broadcaster,2);
					print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*LOOP START;");
					which_graphics_onscreen = "COMPARISION";
					break;
				case "ANIMATE-IN-BATSMAN_STYLE":
					processAnimation(print_writer, "In", "START", session_selected_broadcaster,2);
					print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*LOOP START;");
					which_graphics_onscreen = "BATSMAN_STYLE";
					break;
				case "ANIMATE-IN-BOWLER_STYLE":
					processAnimation(print_writer, "In", "START", session_selected_broadcaster,2);
					print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*LOOP START;");
					which_graphics_onscreen = "BOWLER_STYLE";
					break;
				case "ANIMATE-IN-PREVIOUS_SUMMARY":
					processAnimation(print_writer, "In", "START", session_selected_broadcaster,2);
					processAnimation(print_writer, "SummaryIn", "START", session_selected_broadcaster,2);
					print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*LOOP START;");
					print_writer.println("LAYER2*EVEREST*STAGE START;");
					which_graphics_onscreen = "PREVIOUS_SUMARRY";
					break;
				case "ANIMATE-IN-TIEID-DOUBLE":
					processAnimation(print_writer, "In", "START", session_selected_broadcaster,2);
					print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*LOOP START;");
					print_writer.println("LAYER2*EVEREST*STAGE START;");
					which_graphics_onscreen = "TIEID-DOUBLE";
					break;
				case "ANIMATE-IN-POINTSTABLE":
					if(which_graphics_onscreen == "SCORECARD") {
						processAnimation(print_writer, "BattingCardOut", "START", session_selected_broadcaster,2);
						TimeUnit.SECONDS.sleep(1);
					}else if(which_graphics_onscreen == "BOWLINGCARD") {
						processAnimation(print_writer, "BowlingCardOut", "START", session_selected_broadcaster,2);
						TimeUnit.SECONDS.sleep(1);
					}else if(which_graphics_onscreen == "SUMARRY") {
						processAnimation(print_writer, "SummaryOut", "START", session_selected_broadcaster,2);
						TimeUnit.SECONDS.sleep(1);
					}else {
						processAnimation(print_writer, "In", "START", session_selected_broadcaster,2);
					}
					
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$PointsTable_GRP*CONTAINER SET ACTIVE 1;");
					processAnimation(print_writer, "PointsTableIn", "START", session_selected_broadcaster,2);
					print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*LOOP START;");
					print_writer.println("LAYER2*EVEREST*STAGE START;");
					which_graphics_onscreen = "POINTSTABLE";
					break;
				case "ANIMATE-IN-MOSTRUNS":
					processAnimation(print_writer, "In", "START", session_selected_broadcaster,2);
					print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*LOOP START;");
					print_writer.println("LAYER2*EVEREST*STAGE START;");
					which_graphics_onscreen = "MOSTRUNS";
					break;
				case "ANIMATE-IN-MOSTWICKETS":
					processAnimation(print_writer, "In", "START", session_selected_broadcaster,2);
					print_writer.println("LAYER2*EVEREST*STAGE START;");
					print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*LOOP START;");
					which_graphics_onscreen = "MOSTWICKETS";
					break;
				case "ANIMATE-IN-MOSTFOURS":
					processAnimation(print_writer, "In", "START", session_selected_broadcaster,2);
					print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*LOOP START;");
					print_writer.println("LAYER2*EVEREST*STAGE START;");
					which_graphics_onscreen = "MOSTFOURS";
					break;
				case "ANIMATE-IN-MOSTSIXES":
					processAnimation(print_writer, "In", "START", session_selected_broadcaster,2);
					print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*LOOP START;");
					print_writer.println("LAYER2*EVEREST*STAGE START;");
					which_graphics_onscreen = "MOSTSIXES";
					break;
				case "ANIMATE-IN-HIGHESTSCORE":
					processAnimation(print_writer, "In", "START", session_selected_broadcaster,2);
					print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*LOOP START;");
					print_writer.println("LAYER2*EVEREST*STAGE START;");
					which_graphics_onscreen = "HIGHESTSCORE";
					break;
				case "ANIMATE-IN-GENERIC":
					processAnimation(print_writer, "In", "START", session_selected_broadcaster,2);
					print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*LOOP START;");
					which_graphics_onscreen = "GENERIC";
					break;
				case "ANIMATE-IN-MANHATTAN":
					processAnimation(print_writer, "In", "START", session_selected_broadcaster,2);
					print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*LOOP START;");
					print_writer.println("LAYER2*EVEREST*STAGE START;");
					which_graphics_onscreen = "MANHATTAN";
					break;
				case "ANIMATE-IN-WORM":
					processAnimation(print_writer, "In", "START", session_selected_broadcaster,2);
					print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*LOOP START;");
					print_writer.println("LAYER2*EVEREST*STAGE START;");
					which_graphics_onscreen = "WORM";
					break;
				case "ANIMATE-IN-THISOVER":
					processAnimation(print_writer, "In", "START", session_selected_broadcaster,2);
					print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*LOOP START;");
					print_writer.println("LAYER2*EVEREST*STAGE START;");
					which_graphics_onscreen = "THISOVER";
					break;
				case "TICKER_LT_OUT":
					if(!which_graphics_onscreen.isEmpty() && which_graphics_onscreen != "INFOBAR") {
						AnimateOutGraphics(print_writer, which_graphics_onscreen);
						TimeUnit.SECONDS.sleep(1);
					}
					populateInfobar(infobar, print_writer, valueToProcess.split(",")[0],match, session_selected_broadcaster);
					AnimateOutGraphics(print_writer, "FF_OUT");
					TimeUnit.SECONDS.sleep(1);
					which_graphics_onscreen = "INFOBAR";
					infobar.setInfobar_on_screen(true);
					//AnimateOutGraphics(print_writer, which_graphic_on_screen);
					break;
				case "TICKER_LT_IN":
					AnimateInGraphics(print_writer, "FF_IN");
					TimeUnit.SECONDS.sleep(1);
					if(which_graphics_onscreen != "INFOBAR") {
						AnimateOutGraphics(print_writer, which_graphics_onscreen);
					}
					break;
				case "CLEAR-ALL":
					print_writer.println("LAYER1*EVEREST*SINGLE_SCENE CLEAR;");
					print_writer.println("LAYER2*EVEREST*SINGLE_SCENE CLEAR;");
					which_graphics_onscreen = "";
					break;
				case "ANIMATE-OUT-SPONSOR":
					processAnimation(print_writer, "Sponsor_Out", "START", session_selected_broadcaster,1);
					break;
				case "ANIMATE-OUT":
					switch(which_graphics_onscreen) {
					case "INFOBAR":
						processAnimation(print_writer, "Out", "START", session_selected_broadcaster,1);
						which_graphics_onscreen = "";
						infobar.setInfobar_on_screen(false);
						break;
					
					case "SCORECARD": 
						processAnimation(print_writer, "BattingCardOut", "START", session_selected_broadcaster,2);
						TimeUnit.SECONDS.sleep(1);
						which_graphics_onscreen = "";
						//processAnimation(print_writer, "FF_Out", "START", session_selected_broadcaster);
						
						if(infobar.isInfobar_on_screen() == true) {
							processAnimation(print_writer, "FF_Out", "START", session_selected_broadcaster,1);
							//processAnimation(print_writer, "In", "START", session_selected_broadcaster,1);
							which_graphics_onscreen = "INFOBAR";
						}
						break;
					case "BOWLINGCARD":
						processAnimation(print_writer, "BowlingCardOut", "START", session_selected_broadcaster,2);
						TimeUnit.SECONDS.sleep(1);
						which_graphics_onscreen = "";
						//processAnimation(print_writer, "FF_Out", "START", session_selected_broadcaster);
						
						if(infobar.isInfobar_on_screen() == true) {
							processAnimation(print_writer, "FF_Out", "START", session_selected_broadcaster,1);
							which_graphics_onscreen = "INFOBAR";
						}
						break;
					case "SUMARRY": case "PREVIOUS_SUMARRY":
						processAnimation(print_writer, "SummaryOut", "START", session_selected_broadcaster,2);
						TimeUnit.SECONDS.sleep(1);
						which_graphics_onscreen = "";
						//processAnimation(print_writer, "FF_Out", "START", session_selected_broadcaster);
						
						if(infobar.isInfobar_on_screen() == true) {
							processAnimation(print_writer, "FF_Out", "START", session_selected_broadcaster,1);
							which_graphics_onscreen = "INFOBAR";
						}
						break;
					case "POINTSTABLE":
						processAnimation(print_writer, "PointsTableOut", "START", session_selected_broadcaster,2);
						TimeUnit.SECONDS.sleep(1);
						which_graphics_onscreen = "";
						//processAnimation(print_writer, "FF_Out", "START", session_selected_broadcaster);
						
						if(infobar.isInfobar_on_screen() == true) {
							processAnimation(print_writer, "FF_Out", "START", session_selected_broadcaster,1);
							which_graphics_onscreen = "INFOBAR";
						}
						break;
					case "SIX": case "FOUR":
						processAnimation(print_writer, "Out", "START", session_selected_broadcaster,2);
						if(infobar.isInfobar_on_screen() == true) {
//							processAnimation(print_writer, "FF_Out", "START", session_selected_broadcaster,1);
							which_graphics_onscreen = "INFOBAR";
						}
						
						break;
					case "BUG": case "HOWOUT": case "BATSMANSTATS": case "BOWLERSTATS": case "BUG-DB": case "NAMESUPER": case "NAMESUPER-PLAYER": case "DOUBLETEAMS": 
					case "MATCHID": case "L3MATCHID": case "PLAYINGXI": case "TARGET": case "TEAMSUMMARY": case "EQUATION":case "PLAYERSUMMARY": case "L3PLAYERPROFILE": 
					case "FALLOFWICKET": case "SPLIT": case "COMPARISION": case "BUG-DISMISSAL": case "HOWOUT_WITHOUT_FIELDER": case "BATSMAN_STYLE": case "BUG-BOWLER": 
					case "MATCH_PROMO": case "TEAMS_LOGO": case "BOWLER_STYLE": case "TIEID-DOUBLE": case "GENERIC": case "MOSTRUNS": case "MOSTWICKETS": 
					case "MOSTFOURS": case "MOSTSIXES": case "HIGHESTSCORE": case "MANHATTAN": case "PARTNERSHIP": case "PROJECTED": case "FF_TARGET": case "THISOVER":
					case "L3HOWOUT": case "CURRENT_PARTNERSHIP": case "WORM": case "PLAYERPROFILE": case "MATCHSTATUS": case "HOWOUT_BOTH": case "BATSMANSTATS_BOTH":
					case "THIS_SESSION": case "SESSION": case "FF_EQUATION": case "BUG-TOSS": case "BOWLERDETAILS": case "HOWOUT_WITHOUT_FIELDER_QUICK":
					case "BUG_POWERPLAY": case "BUG_PARTNERSHIP": case "LEADERBOARD": case "THISSERIES": case "DRONE_BUG": case "BUG_HIGHLIGHT": case "DLS": case "DLS_EQUATION":
						processAnimation(print_writer, "Out", "START", session_selected_broadcaster,2);
						TimeUnit.SECONDS.sleep(1);
						print_writer.println("LAYER2*EVEREST*SINGLE_SCENE CLEAR;");
						
						which_graphics_onscreen = "";
						//processAnimation(print_writer, "FF_Out", "START", session_selected_broadcaster);
						
						if(infobar.isInfobar_on_screen() == true) {
							processAnimation(print_writer, "FF_Out", "START", session_selected_broadcaster,1);
							which_graphics_onscreen = "INFOBAR";
						}
						
						break;
						
//					 case "LEADERBOARD":
//						//processAnimation(print_writer, "In", "CONTINUE", session_selected_broadcaster);
//						TimeUnit.SECONDS.sleep(2);
//						if(infobar.isInfobar_on_screen() == true) {
//							processAnimation(print_writer, "FF_Out", "START", session_selected_broadcaster,1);
//							which_graphics_onscreen = "INFOBAR";
//						}
//						
//						break;
					}
					break;
				}
				//return JSONObject.fromObject(this_doad).toString();
			}
		}
		return null;
}
	
	public void populateScorecard(PrintWriter print_writer, String viz_scene, int whichInning, MatchAllData match, String session_selected_broadcaster) throws InterruptedException, IOException 
	{
		switch (session_selected_broadcaster.toUpperCase()) {
		case "FRUIT":
			if (match == null) {
				this.status = "ERROR: Match is null";
			} else if (match.getMatch().getInning() == null) {
				this.status = "ERROR: Scorecard's inning is null";
			} else {
				int row_id = 0 ;
				
				if(which_graphics_onscreen == "BOWLINGCARD") {
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$BattingCard_GRP*CONTAINER SET ACTIVE 0;");
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$BowlingCard_GRP*CONTAINER SET ACTIVE 1;");
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$Summary_GRP*CONTAINER SET ACTIVE 0;");
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$PointsTable_GRP*CONTAINER SET ACTIVE 0;");
				}else if(which_graphics_onscreen == "SUMARRY") {
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$BattingCard_GRP*CONTAINER SET ACTIVE 0;");
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$BowlingCard_GRP*CONTAINER SET ACTIVE 0;");
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$Summary_GRP*CONTAINER SET ACTIVE 1;");
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$PointsTable_GRP*CONTAINER SET ACTIVE 0;");
				}else if(which_graphics_onscreen == "POINTSTABLE") {
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$BattingCard_GRP*CONTAINER SET ACTIVE 0;");
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$BowlingCard_GRP*CONTAINER SET ACTIVE 0;");
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$Summary_GRP*CONTAINER SET ACTIVE 0;");
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$PointsTable_GRP*CONTAINER SET ACTIVE 1;");
				}else {
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$BattingCard_GRP*CONTAINER SET ACTIVE 1;");
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$BowlingCard_GRP*CONTAINER SET ACTIVE 0;");
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$Summary_GRP*CONTAINER SET ACTIVE 0;");
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$PointsTable_GRP*CONTAINER SET ACTIVE 0;");
				}
				
				
				
				for(Inning inn : match.getMatch().getInning()) {
					if (inn.getInningNumber() == whichInning) {
//						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBatTeamName " + inn.getBatting_team().getTeamName3().toUpperCase()+ ";");
						
						if(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.TEST)) {
							if(whichInning == 1 || whichInning == 2) {
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBatInnings " + "1st INNINGS" + ";");
							}else if(whichInning == 3 || whichInning == 4) {
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBatInnings " + "2nd INNINGS" + ";");
							}
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBatSubHeader " + " v " + inn.getBowling_team().getTeamName3().toUpperCase() + ";");
						}else {
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBatTeamName " + inn.getBatting_team().getTeamName3().toUpperCase() + ";");
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBatSubHeader " + match.getSetup().getMatchIdent() + ";");
						}
			
						
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgBatTeamlogo " + logo_path + inn.getBatting_team().getTeamName4() + CricketUtil.PNG_EXTENSION + ";");						
						//print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgBatSponsorImage " + logo_path + "Sponsor\\" + "Dafa_News" + CricketUtil.PNG_EXTENSION + ";");

						Collections.sort(inn.getBattingCard());
						for (BattingCard bc : inn.getBattingCard()) {
						
							row_id = row_id + 1;

							switch (bc.getStatus().toUpperCase()) {
	
							case CricketUtil.OUT:
								
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$"+row_id+"$Out*CONTAINER SET ACTIVE 1;");
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$"+row_id+"$NotOut*CONTAINER SET ACTIVE 0;");
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$"+row_id+"$HowOut_GRP*CONTAINER SET ACTIVE 1;");
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$"+row_id+"$Runs*CONTAINER SET ACTIVE 1;");
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$"+row_id+"$Balls*CONTAINER SET ACTIVE 1;");
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBatPlayerName0" +row_id + " " +bc.getPlayer().getTicker_name() + ";");
								
								if(bc.getHowOut().toUpperCase().equalsIgnoreCase(CricketUtil.CONCUSSED)) {
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vHighlight_DeHighlight0" + row_id + " " + "1" + ";");
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHowOut0"+ row_id + "A" + " " + bc.getHowOutPartOne() + ";");
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHowOut0"+ row_id + "B" + " " + " " + ";");
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tRun0" + row_id + " " + bc.getRuns() + ";");
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBalls0" + row_id + " " + bc.getBalls() + ";");
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vBatsman12Visibility " + "1" + ";");
								}
								else if(bc.getHowOut().toUpperCase().equalsIgnoreCase(CricketUtil.RETIRED_OUT)) {
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vHighlight_DeHighlight0" + row_id + " " + "1" + ";");
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHowOut0"+ row_id + "A" + " " + bc.getHowOutPartOne() + ";");
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHowOut0"+ row_id + "B" + " " + " " + ";");
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tRun0" + row_id + " " + bc.getRuns() + ";");
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBalls0" + row_id + " " + bc.getBalls() + ";");
								}else if(bc.getHowOut().toUpperCase().equalsIgnoreCase(CricketUtil.HIT_WICKET)) {
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vHighlight_DeHighlight0" + row_id + " " + "1" + ";");
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHowOut0"+ row_id + "A" + " " + bc.getHowOutPartOne() + ";");
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHowOut0"+ row_id + "B" + " " + " " + ";");
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tRun0" + row_id + " " + bc.getRuns() + ";");
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBalls0" + row_id + " " + bc.getBalls() + ";");
								}else if(bc.getHowOut().toUpperCase().equalsIgnoreCase(CricketUtil.MANKAD)){
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vHighlight_DeHighlight0" + row_id + " " + "1" + ";");
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHowOut0"+ row_id + "A" + " " + bc.getHowOutText() + ";");
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHowOut0"+ row_id + "B" + " " + " " + ";");
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tRun0" + row_id + " " + bc.getRuns() + ";");
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBalls0" + row_id + " " + bc.getBalls() + ";");
								}else if(bc.getHowOut().toUpperCase().equalsIgnoreCase(CricketUtil.RUN_OUT)) {
									if(bc.getWasHowOutFielderSubstitute() != null && bc.getWasHowOutFielderSubstitute().equalsIgnoreCase(CricketUtil.YES)) {
										print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vHighlight_DeHighlight0" + row_id + " " + "1" + ";");
										print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHowOut0"+ row_id + "A" + " " + bc.getHowOutPartOne() + " (sub - " + bc.getHowOutPartTwo().split(" ")[0] + " )" + ";");
										print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHowOut0"+ row_id + "B" + " " + " " + ";");
										print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tRun0" + row_id + " " + bc.getRuns() + ";");
										print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBalls0" + row_id + " " + bc.getBalls() + ";");
									}else {
										print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vHighlight_DeHighlight0" + row_id + " " + "1" + ";");
										print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHowOut0"+ row_id + "A" + " " + bc.getHowOutText() + ";");
										print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHowOut0"+ row_id + "B" + " " + " " + ";");
										print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tRun0" + row_id + " " + bc.getRuns() + ";");
										print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBalls0" + row_id + " " + bc.getBalls() + ";");
									}
								}else if(bc.getHowOut().toUpperCase().equalsIgnoreCase(CricketUtil.CAUGHT_AND_BOWLED) || bc.getHowOut().toUpperCase().equalsIgnoreCase(CricketUtil.LBW)){
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vHighlight_DeHighlight0" + row_id + " " + "1" + ";");
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHowOut0"+ row_id + "A" + " " + " " + ";");
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHowOut0"+ row_id + "B" + " " + bc.getHowOutText() + ";");
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tRun0" + row_id + " " + bc.getRuns() + ";");
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBalls0" + row_id + " " + bc.getBalls() + ";");
								}
								else {
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vHighlight_DeHighlight0" + row_id + " " + "1" + ";");
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHowOut0"+ row_id + "A" + " " + bc.getHowOutPartOne() + ";");
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHowOut0"+ row_id + "B" + " " + bc.getHowOutPartTwo() + ";");
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tRun0" + row_id + " " + bc.getRuns() + ";");
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBalls0" + row_id + " " + bc.getBalls() + ";");
									
								}
							break;
	
							case CricketUtil.NOT_OUT:
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$"+row_id+"$Out*CONTAINER SET ACTIVE 0;");
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$"+row_id+"$NotOut*CONTAINER SET ACTIVE 1;");
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBatPlayerName0" +row_id + " " +bc.getPlayer().getTicker_name() + ";");
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vHighlight_DeHighlight0" + row_id + " " + "2" + ";");
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHowOut0"+ row_id + "A" + " " + bc.getStatus() + ";");
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHowOut0"+ row_id + "B" + " " + " " + ";");
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tRun0" + row_id + " " + bc.getRuns() + ";");
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBalls0" + row_id + " " + bc.getBalls() + ";");
								
							break;
							
							case CricketUtil.STILL_TO_BAT:
							
								if(bc.getHowOut() == null) {
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBatPlayerName0" + row_id + " " + bc.getPlayer().getTicker_name() + ";");
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vHighlight_DeHighlight0" + row_id + " " + "0" + ";");
								}
								else if(bc.getHowOut().toUpperCase().equalsIgnoreCase(CricketUtil.RETIRED_HURT)) {
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$"+row_id+"$Out*CONTAINER SET ACTIVE 1;");
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$"+row_id+"$NotOut*CONTAINER SET ACTIVE 0;");
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$"+row_id+"$HowOut_GRP*CONTAINER SET ACTIVE 1;");
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$"+row_id+"$Runs*CONTAINER SET ACTIVE 1;");
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$"+row_id+"$Balls*CONTAINER SET ACTIVE 1;");
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBatPlayerName0" +row_id + " " +bc.getPlayer().getTicker_name() + ";");
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vHighlight_DeHighlight0" + row_id + " " + "1" + ";");
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHowOut0"+ row_id + "A" + " " + "retired hurt" + ";");
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHowOut0"+ row_id + "B" + " " + " " + ";");
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tRun0" + row_id + " " + bc.getRuns() + ";");
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBalls0" + row_id + " " + bc.getBalls() + ";");
								}
								
								break;
						}
					}
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBatExtras " + inn.getTotalExtras() + ";");
					
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBatOvers "+ CricketFunctions.OverBalls(inn.getTotalOvers(),inn.getTotalBalls()) + ";");
					
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tRunRate " + inn.getRunRate() + ";");

					if(inn.getTotalWickets() >= 10) {
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBatScore " + inn.getTotalRuns() + ";");
					} else {
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBatScore " + inn.getTotalRuns() + slashOrDash + String.valueOf(inn.getTotalWickets()) + ";");
				}
			}
		}
				print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW ON;");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*CONTAINER SET ACTIVE 0;");
				if(which_graphics_onscreen == "BOWLINGCARD") {
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$BowlingCard_GRP*CONTAINER SET ACTIVE 0;");
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$BattingCard_GRP*CONTAINER SET ACTIVE 1;");
				}else if(which_graphics_onscreen == "SUMARRY") {
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$BattingCard_GRP*CONTAINER SET ACTIVE 1;");
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$Summary_GRP*CONTAINER SET ACTIVE 0;");
				}else if(which_graphics_onscreen == "POINTSTABLE") {
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$BattingCard_GRP*CONTAINER SET ACTIVE 1;");
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$PointsTable_GRP*CONTAINER SET ACTIVE 0;");
				}
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In STOP;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out STOP;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 30;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*BattingCardIn SHOW 109.0;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out SHOW 0.0;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*BattingCardOut SHOW 0.0;");
				print_writer.println("LAYER1*EVEREST*GLOBAL SNAPSHOT_PATH C:/Temp/Preview.png;");
				print_writer.println("LAYER1*EVEREST*GLOBAL SNAPSHOT 1920 1080;");
				TimeUnit.SECONDS.sleep(1);
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*BattingCardOut SHOW 0.0;");
				if(which_graphics_onscreen == "BOWLINGCARD" || which_graphics_onscreen == "SUMARRY" || which_graphics_onscreen == "POINTSTABLE") {
					print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 30.0;");
				}else {
					print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 0.0;");
				}
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out SHOW 0.0;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*BattingCardIn SHOW 0.0;");
				
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*CONTAINER SET ACTIVE 1;");
				if(which_graphics_onscreen == "BOWLINGCARD") {
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$BowlingCard_GRP*CONTAINER SET ACTIVE 1;");
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$BattingCard_GRP*CONTAINER SET ACTIVE 0;");
				}else if(which_graphics_onscreen == "SUMARRY") {
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$BattingCard_GRP*CONTAINER SET ACTIVE 0;");
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$Summary_GRP*CONTAINER SET ACTIVE 1;");
				}else if(which_graphics_onscreen == "POINTSTABLE") {
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$BattingCard_GRP*CONTAINER SET ACTIVE 0;");
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$PointsTable_GRP*CONTAINER SET ACTIVE 1;");
				}
				print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW OFF;");
				this.status = CricketUtil.SUCCESSFUL;
			}
			break;
		}
		
	}
	
	public void populateBowlingcard(PrintWriter print_writer,String viz_scene,boolean is_this_updating, int whichInning,   MatchAllData match, String session_selected_broadcaster) throws InterruptedException, IOException 
	{
		switch (session_selected_broadcaster.toUpperCase()) {
		case "FRUIT":
			if (match == null) {
				this.status = "ERROR: Match is null";
			} else if (match.getMatch().getInning() == null) {
				this.status = "ERROR: Bowlingcard's inning is null";
			} else {
				int row_id = 0, row = 1, max_Strap = 9,len = 1;
				
				for(Inning inn : match.getMatch().getInning()) {
					if (inn.getInningNumber() == whichInning) {
						
//						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBallTeamName " + inn.getBowling_team().getTeamName3().toUpperCase() + ";");
						
						if(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.TEST)) {
							if(whichInning == 1 || whichInning == 2) {
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBowlInnings " + "1st INNINGS" + ";");
							}else if(whichInning == 3 || whichInning == 4) {
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBowlInnings " + "2nd INNINGS" + ";");
							}
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBallSubHeader " + " v " + inn.getBatting_team().getTeamName3().toUpperCase() + ";");
						}else {
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBallTeamName " + inn.getBowling_team().getTeamName3().toUpperCase() + ";");
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBallSubHeader " + match.getSetup().getMatchIdent() + ";");
						}
						
						
						//print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBallSubHeader " + "v " + inn.getBatting_team().getTeamName1().toUpperCase() + ";");
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgBallTeamlogo " + logo_path + inn.getBowling_team().getTeamName4() + CricketUtil.PNG_EXTENSION + ";");
						//print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgBallSponsorImage " + logo_path + "Sponsor\\" + "Dafa_News" + CricketUtil.PNG_EXTENSION + ";");

						for (BowlingCard boc : inn.getBowlingCard()) {
							
							row_id = row_id + 1;
							row = row + 1;
							
							if(boc.getRuns() > 0 || ((boc.getOvers()*6)+boc.getBalls()) > 0) {
								len = len + 1;
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$BowlingCard_GRP$DataAll$BowlingBG$Bowling$Bowling_Card$" + len + "*CONTAINER SET ACTIVE 1;");
//								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$BowlingCard_GRP$DataAll$BAtting$Bowling_Card$" + len + "*CONTAINER SET ACTIVE 1;");

//								for(int j = len + 1; j <= max_Strap; j++) {
//									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$BowlingCard_GRP$DataAll$BAtting$Bowling_Card$" + j + "*CONTAINER SET ACTIVE 1;");
//								}
							}
//							print_writer.println("LAYER2*EVEREST*TREEVIEW*"+ row + "*CONTAINER SET ACTIVE 1;");
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBallPlayerName0" + row_id + " "  + boc.getPlayer().getTicker_name() + ";");
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBallStatValue" + row + "A " + CricketFunctions.OverBalls(boc.getOvers(), boc.getBalls()) +";");
							if(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.DT20) || match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.D10) 
									|| match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.IT20)) {
								
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBallStatHeadB " +"DOTS" +";");
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBallStatValue" + row + "B " + boc.getDots() +";");
							}
							else {
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBallStatHeadB " +"MAIDENS" +";");
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBallStatValue" + row + "B " + boc.getMaidens() +";");
							}
							
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBallStatValue" + row + "C " + boc.getRuns() +";");
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBallStatValue" + row + "D " + boc.getWickets() + ";");
							
							if(boc.getEconomyRate() == null) {
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBallStatValue" + row + "E " + "-" + ";");
							}else {
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBallStatValue" + row + "E " + boc.getEconomyRate() + ";");
							}
							
							
						}
						
						  for(int j = len + 1; j <= max_Strap; j++) { 
							  print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$BowlingCard_GRP$DataAll$BowlingBG$Bowling$Bowling_Card$" + j + "*CONTAINER SET ACTIVE 0;"); 
						  }
						 
						row_id= 0 ;
						if(inn.getBowlingCard().size()<=8) {
							if(is_this_updating == false) {
								if(inn.getFallsOfWickets() == null || inn.getFallsOfWickets().size() <= 0) {
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$FF_ALLGRP$DataAll$Side_1$Bowling$Bowling_Card$FOWGRP*CONTAINER SET ACTIVE 0;");
									//print_writer.println("LAYER1*EVEREST*TREEVIEW*Main$DataAll$BAtting$Bowling_Card$10*CONTAINER SET ACTIVE 0;");
									//print_writer.println("LAYER1*EVEREST*TREEVIEW*Main$DataAll$BAtting$Bowling_Card$11*CONTAINER SET ACTIVE 0;");
								}
								else if(inn.getFallsOfWickets() != null || inn.getFallsOfWickets().size() > 0) {
									for(FallOfWicket fow : inn.getFallsOfWickets()) {								
										if(inn.getTotalWickets()>=0 && inn.getTotalWickets() <= 10) {
											row_id = row_id + 1;
											
											print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$FF_ALLGRP$DataAll$Side_1$Bowling$Bowling_Card$FOWGRP$11$RUNS_GRP$" + fow.getFowNumber() + "*CONTAINER SET ACTIVE 1;");
											print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vWKTS "+ fow.getFowNumber() + ";");
											print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tWKTRun0" + row_id + " "  + fow.getFowRuns() + ";");
										}		
									}
								}
							}
						}else {
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$FF_ALLGRP$DataAll$Side_1$Bowling$Bowling_Card$FOWGRP*CONTAINER SET ACTIVE 0;");
						}
						
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBallOvers " + CricketFunctions.OverBalls(inn.getTotalOvers(), inn.getTotalBalls()) + ";");
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBallExtras " + inn.getTotalExtras() + ";");
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tRunRate "+ inn.getRunRate() + ";");
						if(inn.getTotalWickets() >= 10) {
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBallScore " + inn.getTotalRuns() + ";");
						}
						else {
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBallScore " + inn.getTotalRuns() + slashOrDash + String.valueOf(inn.getTotalWickets()) + ";");
						}
					}
				}
				//String commands = "";
				
				if(which_graphics_onscreen == "SCORECARD") {
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$BattingCard_GRP*CONTAINER SET ACTIVE 1;");
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$BowlingCard_GRP*CONTAINER SET ACTIVE 0;");
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$Summary_GRP*CONTAINER SET ACTIVE 0;");
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$PointsTable_GRP*CONTAINER SET ACTIVE 0;");
				}else if(which_graphics_onscreen == "SUMARRY") {
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$BattingCard_GRP*CONTAINER SET ACTIVE 0;");
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$BowlingCard_GRP*CONTAINER SET ACTIVE 0;");
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$Summary_GRP*CONTAINER SET ACTIVE 1;");
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$PointsTable_GRP*CONTAINER SET ACTIVE 0;");
				}else if(which_graphics_onscreen == "POINTSTABLE") {
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$BattingCard_GRP*CONTAINER SET ACTIVE 0;");
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$BowlingCard_GRP*CONTAINER SET ACTIVE 0;");
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$Summary_GRP*CONTAINER SET ACTIVE 0;");
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$PointsTable_GRP*CONTAINER SET ACTIVE 1;");
				}else {
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$BattingCard_GRP*CONTAINER SET ACTIVE 0;");
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$BowlingCard_GRP*CONTAINER SET ACTIVE 1;");
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$Summary_GRP*CONTAINER SET ACTIVE 0;");
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$PointsTable_GRP*CONTAINER SET ACTIVE 0;");
				}
				
				print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW ON;");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*CONTAINER SET ACTIVE 0;");
				if(which_graphics_onscreen == "SCORECARD") {
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$BowlingCard_GRP*CONTAINER SET ACTIVE 1;");
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$BattingCard_GRP*CONTAINER SET ACTIVE 0;");
				}else if(which_graphics_onscreen == "SUMARRY") {
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$BowlingCard_GRP*CONTAINER SET ACTIVE 1;");
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$Summary_GRP*CONTAINER SET ACTIVE 0;");
				}else if(which_graphics_onscreen == "POINTSTABLE") {
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$BowlingCard_GRP*CONTAINER SET ACTIVE 1;");
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$PointsTable_GRP*CONTAINER SET ACTIVE 0;");
				}
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In STOP;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out STOP;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 30.0;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*BowlingCardIn SHOW 114.0;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out SHOW 0.0;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*BowlingCardOut SHOW 0.0;");
				print_writer.println("LAYER1*EVEREST*GLOBAL SNAPSHOT_PATH C:/Temp/Preview.png;");
				print_writer.println("LAYER1*EVEREST*GLOBAL SNAPSHOT 1920 1080;");
				TimeUnit.SECONDS.sleep(1);
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out SHOW 0.0;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*BowlingCardOut SHOW 0.0;");
				if(which_graphics_onscreen == "SCORECARD" || which_graphics_onscreen == "SUMARRY" || which_graphics_onscreen == "POINTSTABLE") {
					print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 30.0;");
				}else {
					print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 0.0;");
				}
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*BowlingCardIn SHOW 0.0;");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*CONTAINER SET ACTIVE 1;");
				if(which_graphics_onscreen == "SCORECARD") {
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$BowlingCard_GRP*CONTAINER SET ACTIVE 0;");
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$BattingCard_GRP*CONTAINER SET ACTIVE 1;");
				}else if(which_graphics_onscreen == "SUMARRY") {
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$BowlingCard_GRP*CONTAINER SET ACTIVE 0;");
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$Summary_GRP*CONTAINER SET ACTIVE 1;");
				}else if(which_graphics_onscreen == "POINTSTABLE") {
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$BowlingCard_GRP*CONTAINER SET ACTIVE 0;");
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$PointsTable_GRP*CONTAINER SET ACTIVE 1;");
				}
				
				print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW OFF;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*BattingCardIn SHOW 109.0;");
				this.status = CricketUtil.SUCCESSFUL;
			}
			break;
		}
	}
	public void populatePartnership(PrintWriter print_writer, String viz_scene, int whichInning,MatchAllData match, String session_selected_broadcaster) throws InterruptedException 
	{
		switch (session_selected_broadcaster.toUpperCase()) {
		case "FRUIT":
			if (match == null) {
				this.status = "ERROR: Match is null";
			} else if (match.getMatch().getInning() == null) {
				this.status = "ERROR: Partnership's inning is null";
			} else {
				
				int row_id = 0, omo_num = 0,Top_Score = 50;
				float Mult = 100, ScaleFac1 = 0, ScaleFac2 = 0;
				String Left_Batsman = "",Right_Batsman="";
				
				for(Inning inn : match.getMatch().getInning()) {
					if(inn.getInningNumber() == whichInning) {
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBatTeamName " + "PARTNERSHIPS" + ";");
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBatSubHeader " + match.getSetup().getMatchIdent().toUpperCase() + ";");
						
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgTeamLogo " + logo_path + inn.getBatting_team().getTeamName4() + 
								CricketUtil.PNG_EXTENSION + ";");
						for(int a = 1; a <= inn.getPartnerships().size(); a++){
							ScaleFac1=0;ScaleFac2=0;
							if(inn.getPartnerships().get(a-1).getFirstBatterRuns() > Top_Score) {
								Top_Score = inn.getPartnerships().get(a-1).getFirstBatterRuns();
							}
							if(inn.getPartnerships().get(a-1).getSecondBatterRuns() > Top_Score) {
								Top_Score = inn.getPartnerships().get(a-1).getSecondBatterRuns();
							}
						}
						
						for(Partnership ps : inn.getPartnerships()) {
							row_id = row_id + 1;
							Left_Batsman="" ; Right_Batsman="";
							for(BattingCard bc : inn.getBattingCard()) {
								if(bc.getPlayerId() == ps.getFirstBatterNo()) {
									Left_Batsman = bc.getPlayer().getTicker_name();
								}
								else if(bc.getPlayerId() == ps.getSecondBatterNo()) {
									Right_Batsman = bc.getPlayer().getTicker_name();
								}
							}
							
							if(inn.getPartnerships().size() >= 10) {
								if(ps.getPartnershipNumber()<=inn.getPartnerships().size()) {
									omo_num = 2;
								}
							}
							else {
								if(ps.getPartnershipNumber()<inn.getPartnerships().size()) {
									omo_num = 2;
								}
								else if(ps.getPartnershipNumber() >= inn.getPartnerships().size()) {
									omo_num = 3;
								}
							}
							
							ScaleFac1 = ((ps.getFirstBatterRuns())*(Mult/Top_Score)) ;
							ScaleFac2 = ((ps.getSecondBatterRuns())*(Mult/Top_Score)) ;
							
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectionOmo" + row_id + " " + omo_num + ";");
							
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tLeftPlayerName" + row_id + " " + Left_Batsman + ";");
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tRightPlayerName" + row_id + " " + Right_Batsman + ";");
							
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tLeftBar" + row_id + " " + ScaleFac1 + ";");
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tRightBar" + row_id + " " + ScaleFac2 + ";");
							
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPartnershipRun" + row_id + " " + ps.getTotalRuns() + ";");
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPartnershipBall" + row_id + " " + ps.getTotalBalls() + ";");
								
						}
						
						if(inn.getPartnerships().size() >= 10) {
							row_id = row_id + 1;
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectionOmo" + row_id + " " + "4" + ";");
						}
						else {
							for (BattingCard bc : inn.getBattingCard()) {
								if(row_id < inn.getBattingCard().size()) {
									if(row_id == inn.getPartnerships().size()) {
										row_id = row_id + 1;
										print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectionOmo" + row_id + " " + "0" + ";");
										if(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.TEST)) {
											if(inn.getInningStatus().equalsIgnoreCase(CricketUtil.START) ) {
												print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tDidNotBat" + row_id + " " + "STILL TO BAT" + ";");
											}else {
												print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tDidNotBat" + row_id + " " + "DID NOT BAT" + ";");
											}
										}else {
											if(inn.getTotalOvers() == match.getSetup().getMaxOvers() || inn.getTotalWickets() >= 10 ) {
												print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tDidNotBat" + row_id + " " + "DID NOT BAT" + ";");
											}else if(CricketFunctions.getRequiredRuns(match) <= 0 || CricketFunctions.getRequiredBalls(match) <= 0 || CricketFunctions.getWicketsLeft(match) <= 0) {
												print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tDidNotBat" + row_id + " " + "DID NOT BAT" + ";");
											}else {
												print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tDidNotBat" + row_id + " " + "STILL TO BAT" + ";");
											}
										}
									}
									else if(bc.getStatus().toUpperCase().equalsIgnoreCase(CricketUtil.STILL_TO_BAT)) {
										row_id = row_id + 1;
										//print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tDidNotBat" + row_id + " " + "DID NOT BAT" + ";");
										print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectionOmo" + row_id + " " + "1" + ";");
										print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tLeftPlayerName" + row_id + " " + bc.getPlayer().getTicker_name() + ";");
									}	
								}
								else {
									break;
								}
							}
						}
						
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tExtras " + inn.getTotalExtras() + ";");
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tOvers "+ CricketFunctions.OverBalls(inn.getTotalOvers(),inn.getTotalBalls()) + ";");
						//print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tRunRate " + inn.getRunRate() + ";");

						if(inn.getTotalWickets() >= 10) {
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tScore " + inn.getTotalRuns() + ";");
						} else {
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tScore " + inn.getTotalRuns() + slashOrDash + String.valueOf(inn.getTotalWickets()) + ";");
						}
					}
				}
				
				print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW ON;");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*CONTAINER SET ACTIVE 0;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In STOP;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out STOP;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 103.0;");
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
	public void populateMatchsummary(PrintWriter print_writer, String viz_scene, int whichInning,List<VariousText> vt ,MatchAllData match, String session_selected_broadcaster) throws InterruptedException
	{
		switch (session_selected_broadcaster.toUpperCase()) {
		case "FRUIT":
			if (match == null) {
				this.status = "ERROR: Match is null";
			} else if (match.getMatch().getInning() == null) {
				this.status = "ERROR: Match Summary's inning is null";
			} else {
				//String path = "D:\\Everest_Scenes\\Mumbai_Indians\\Textures and Images\\Logos\\" ;
				if(which_graphics_onscreen == "BOWLINGCARD") {
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$BattingCard_GRP*CONTAINER SET ACTIVE 0;");
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$BowlingCard_GRP*CONTAINER SET ACTIVE 1;");
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$Summary_GRP*CONTAINER SET ACTIVE 0;");
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$PointsTable_GRP*CONTAINER SET ACTIVE 0;");
				}else if(which_graphics_onscreen == "SCORECARD") {
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$BattingCard_GRP*CONTAINER SET ACTIVE 1;");
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$BowlingCard_GRP*CONTAINER SET ACTIVE 0;");
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$Summary_GRP*CONTAINER SET ACTIVE 0;");
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$PointsTable_GRP*CONTAINER SET ACTIVE 0;");
				}else if(which_graphics_onscreen == "POINTSTABLE") {
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$BattingCard_GRP*CONTAINER SET ACTIVE 0;");
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$BowlingCard_GRP*CONTAINER SET ACTIVE 0;");
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$Summary_GRP*CONTAINER SET ACTIVE 0;");
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$PointsTable_GRP*CONTAINER SET ACTIVE 1;");
				}else {
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$BattingCard_GRP*CONTAINER SET ACTIVE 0;");
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$BowlingCard_GRP*CONTAINER SET ACTIVE 0;");
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$Summary_GRP*CONTAINER SET ACTIVE 1;");
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$PointsTable_GRP*CONTAINER SET ACTIVE 0;");
				}
				
				
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSumHeader " + "SUMMARY" + ";");
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSumSubHeader " + match.getSetup().getMatchIdent().toUpperCase() + ";");
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgHomeTeamLogo " + logo_path + match.getSetup().getHomeTeam().getTeamName4() + CricketUtil.PNG_EXTENSION + ";");
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgAwayTeamLogo " + logo_path + match.getSetup().getAwayTeam().getTeamName4() + CricketUtil.PNG_EXTENSION + ";");
				
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$Summary_GRP$DataAll$HeaderGrp$BigBandGrp$Sponsor*CONTAINER SET ACTIVE 0;");
				
				int row_id = 0, row = 0, total_inn = 0, max_Strap = 0;
				String teamname = ""; 
				
				for(Inning inn : match.getMatch().getInning()) {
					if(inn.getInningStatus() != null) {
						total_inn = total_inn + 1;
					}
				}
				
				if(total_inn > 0 && whichInning > total_inn) {
					whichInning = total_inn;
				}
				/*for(int i = 1; i <= whichInning ; i++) {
					if(i == 1) {
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$Summary_GRP$DataAll$BAtting$Summary_Card$SecondInnings*CONTAINER SET ACTIVE 0;");
					}
					else {
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$Summary_GRP$DataAll$BAtting$Summary_Card$SecondInnings*CONTAINER SET ACTIVE 1;");
					}
				}*/
				
				if(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.DT20)||match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.ODI)||
						match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.IT20)||match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.D10)) {
					
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vInnings " + "0" + ";");
				}
				else if(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.TEST)) {
					if(whichInning == 2) {
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vInnings " + "0" + ";");
					}else if(whichInning == 3) {
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vInnings " + "1" + ";");
					}else if (whichInning == 4) {
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vInnings " + "2" + ";");
					}
					
				}
				
				for(int i = 1; i <= whichInning ; i++) {

					if(whichInning == 2) {
						if(i == 1) {
							row = 0;
							row_id = 0;
							max_Strap = 3;
						}else {
							row = 1;
							row_id = 3;
							max_Strap = 6;
						}
					}else if(whichInning == 3) {
						if(i == 1) {
							row = 0;
							row_id = 0;
							max_Strap = 2;
						} else if(i==2) {
							row = 1;
							row_id = 2;
							max_Strap = 4;
						}else if(i==3) {
							row = 2;
							row_id = 4;
							max_Strap = 6;
						}
					}else if (whichInning == 4) {
						if(i == 1) {
							row = 0;
							row_id = 0;
							max_Strap = 1;
						} else if(i==2) {
							row = 1;
							row_id = 1;
							max_Strap = 2;
						}else if(i==3) {
							row = 2;
							row_id = 2;
							max_Strap = 3;
						}else if(i==4) {
							row = 3;
							row_id = 3;
							max_Strap = 4;
						}
					}
					
					row = row + 1;
					if(match.getMatch().getInning().get(i-1).getBattingTeamId() == match.getSetup().getHomeTeamId()) {
						teamname = match.getSetup().getHomeTeam().getTeamName1().toUpperCase();	
					} else {
						teamname = match.getSetup().getAwayTeam().getTeamName1().toUpperCase();
					}
					
					if(!match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.TEST)) {
						if(match.getSetup().getTossWinningTeam() == match.getMatch().getInning().get(0).getBattingTeamId()) {
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vHomeToss 1;");
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vAwayToss 0;");
//							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$Summary_GRP$DataAll$BAtting$Summary_Card$FirstInnings$1st$TossGrp$TOss*CONTAINER SET ACTIVE 1;");
//							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$Summary_GRP$DataAll$BAtting$Summary_Card$SecondInnings$5$TossGrp$TOss*CONTAINER SET ACTIVE 0;");
//							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInnOrToss1 " + "TOSS" + ";");
//							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInningHead2 " + "" + ";");
						}else {
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vHomeToss 0;");
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vAwayToss 1;");
							
//							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$Summary_GRP$DataAll$BAtting$Summary_Card$FirstInnings$1st$TossGrp$TOss*CONTAINER SET ACTIVE 0;");
//							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$Summary_GRP$DataAll$BAtting$Summary_Card$SecondInnings$5$TossGrp$TOss*CONTAINER SET ACTIVE 1;");
//							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInnOrToss2 " + "TOSS" + ";");
//							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInningHead1 " + "" + ";");
						}
					}
					
					
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSumTeamName0"+ row +" "+ teamname + ";");
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSumOverNo0"+ row + " " + 
							CricketFunctions.OverBalls(match.getMatch().getInning().get(i-1).getTotalOvers(),match.getMatch().getInning().get(i-1).getTotalBalls()) + ";");
					
					if(match.getMatch().getInning().get(i-1).getTotalWickets() >= 10) {
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSumScore0"+ row + " " + match.getMatch().getInning().get(i-1).getTotalRuns() + ";");
					}
					else {
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSumScore0"+ row + " " + match.getMatch().getInning().get(i-1).getTotalRuns() + slashOrDash 
										+ match.getMatch().getInning().get(i-1).getTotalWickets() + ";");	
					}
					if(match.getMatch().getInning().get(i-1).getBattingCard() != null) {
						//row_id = 0;
						Collections.sort(match.getMatch().getInning().get(i-1).getBattingCard(),new CricketFunctions.BatsmenScoreComparator());
						for(BattingCard bc : match.getMatch().getInning().get(i-1).getBattingCard()) {
							if(bc.getRuns() > 0) {
								if(bc.getBatsmanInningStarted() != null && bc.getBatsmanInningStarted().equalsIgnoreCase(CricketUtil.YES) && bc.getStatus().equalsIgnoreCase(CricketUtil.STILL_TO_BAT)) {
									row_id = row_id + 1;
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$Summary_GRP$DataAll$SumBG$Summary_Card$Batsman0" + row_id + "*CONTAINER SET ACTIVE 1;");
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSumBatsmanName0"+ row_id + " " + bc.getPlayer().getTicker_name() + " (rh)" + ";");
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSumBatsmanRuns0"+ row_id + " " + bc.getRuns() + ";");
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSumBatsmanBalls0"+ row_id + " " + bc.getBalls() + ";");
								}
								if(!bc.getStatus().toUpperCase().equalsIgnoreCase(CricketUtil.STILL_TO_BAT)) {
									row_id = row_id + 1;
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$Summary_GRP$DataAll$SumBG$Summary_Card$Batsman0" + row_id + "*CONTAINER SET ACTIVE 1;");
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSumBatsmanName0"+ row_id + " " + bc.getPlayer().getTicker_name() + ";");
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSumBatsmanRuns0"+ row_id + " " + bc.getRuns() + ";");
									
									if(bc.getStatus().toUpperCase().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
										print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSumBatsmanNotOut0"+ row_id + " " + "*" + ";");
									} 
									if(bc.getStatus().toUpperCase().equalsIgnoreCase(CricketUtil.OUT)) {
										print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSumBatsmanNotOut0"+ row_id + " " + " " + ";");
									} 
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSumBatsmanBalls0"+ row_id + " " + bc.getBalls() + ";");
									
									if(whichInning == 2) {
										if(i == 1 && row_id >= 3) {
											break;
										}else if(i == 4 && row_id >= 6) {
											break;
										}
									}else if(whichInning == 3) {
										if(i == 1 && row_id >= 2) {
											break;
										}else if(i == 2 && row_id >= 4) {
											break;
										}
										else if(i == 4 && row_id >= 6) {
											break;
										}
									}else if(whichInning == 4) {
										if(i == 1 && row_id >= 1) {
											break;
										}else if(i == 2 && row_id >= 2) {
											break;
										}
										else if(i == 3 && row_id >= 3) {
											break;
										}else if(i == 4 && row_id >= 4) {
											break;
										}
									}
								}
							}
						}
					}

					for(int j = row_id + 1; j <= max_Strap; j++) {
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$Summary_GRP$DataAll$SumBG$Summary_Card$Batsman0" + j + "*CONTAINER SET ACTIVE 0;");
					}
					
					
					if(whichInning == 2) {
						if(i == 1) {
							row_id = 0;
						}
						else if(i==2){
							row_id = 3;
						}
					}else if(whichInning == 3) {
						if(i == 1) {
							row_id = 0;
						}
						else if(i==2){
							row_id = 2;
						}
						else if(i==3){
							row_id = 4;
						}
					}else if (whichInning == 4) {
						if(i == 1) {
							row_id = 0;
						}
						else if(i==2){
							row_id = 1;
						}
						else if(i==3){
							row_id = 2;
						}
						else if(i==4){
							row_id = 3;
						}
					}

					if(match.getMatch().getInning().get(i-1).getBowlingCard() != null) {
						//row_id = 0;
						Collections.sort(match.getMatch().getInning().get(i-1).getBowlingCard(),new CricketFunctions.BowlerFiguresComparator());

						for(BowlingCard boc : match.getMatch().getInning().get(i-1).getBowlingCard()) {
							if(boc.getWickets() > 0 ) {
								row_id = row_id + 1;
								
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$Summary_GRP$DataAll$SumBG$Summary_Card$Bowler0" + row_id + "*CONTAINER SET ACTIVE 1;");
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSumBowlerName0" + row_id + " " +  boc.getPlayer().getTicker_name() + ";");
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSumBowlerRuns0" + row_id + " " + boc.getWickets() + slashOrDash + boc.getRuns() + ";");
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSumBowlerOver0" + row_id + " " + CricketFunctions.OverBalls(boc.getOvers(), boc.getBalls()) + ";");
								
								if(whichInning == 2) {
									if(i == 1 && row_id >= 3) {
										break;
									}else if(i == 4 && row_id >= 6) {
										break;
									}
								}else if(whichInning == 3) {
									if(i == 1 && row_id >= 2) {
										break;
									}else if(i == 2 && row_id >= 4) {
										break;
									}
									else if(i == 4 && row_id >= 6) {
										break;
									}
								}else if(whichInning == 4) {
									if(i == 1 && row_id >= 1) {
										break;
									}else if(i == 2 && row_id >= 2) {
										break;
									}
									else if(i == 3 && row_id >= 3) {
										break;
									}else if(i == 4 && row_id >= 4) {
										break;
									}
								} 
							}
						}
					}
					
					for(int j = row_id + 1; j <= max_Strap; j++) {
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$Summary_GRP$DataAll$SumBG$Summary_Card$Bowler0" + j + "*CONTAINER SET ACTIVE 0;");

						/*if(whichInning == 3) {
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$Summary_GRP$DataAll$BAtting$Summary_Card$Main$Third$Thirdnnings$group$10$Fifth$Ball$Bowler0" + j + "*CONTAINER SET ACTIVE 0;");
						}else if(whichInning == 4) {
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$Summary_GRP$DataAll$BAtting$Summary_Card$Main$Fourth$Thirdnnings$group$10$Fifth$Ball$Bowler0" + j + "*CONTAINER SET ACTIVE 0;");
						}else {
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$Summary_GRP$DataAll$BAtting$Summary_Card$Bowler0" + j + "*CONTAINER SET ACTIVE 0;");
						}*/
					}
				}
				
				//print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tResult " + 
						//CricketFunctions.generateMatchSummaryStatus(2, match, CricketUtil.FULL).toUpperCase() + ";");
				
				for(VariousText vartext : vt) {
					if(vartext.getVariousType().equalsIgnoreCase("MATCHSUMMARYFOOTER") && vartext.getUseThis().toUpperCase().equalsIgnoreCase(CricketUtil.YES)) {
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSumInfo " + 
								vartext.getVariousText() + ";");
						}else if(vartext.getVariousType().equalsIgnoreCase("MATCHSUMMARYFOOTER") && vartext.getUseThis().toUpperCase().equalsIgnoreCase(CricketUtil.NO)) {
				
							if(match.getMatch().getMatchResult() != null) {
								if(match.getMatch().getMatchResult().toUpperCase().equalsIgnoreCase("")) {
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSumInfo " + 
											CricketFunctions.generateMatchSummaryStatus(whichInning, match, CricketUtil.FULL).toUpperCase() + ";");
								}
								else if(match.getMatch().getMatchResult().toUpperCase().equalsIgnoreCase("DRAWN")){
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSumInfo " + "MATCH TIED" + ";");
								}
								else if(match.getMatch().getMatchResult().split(",")[1].toUpperCase().equalsIgnoreCase("SUPER_OVER")){
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSumInfo " + "MATCH TIED - " + match.getMatch().getMatchStatus().toUpperCase() + ";");
								}
								else {
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSumInfo " + 
											CricketFunctions.generateMatchSummaryStatus(whichInning, match, CricketUtil.FULL).toUpperCase() + ";");
								}
							}
							else {
								if(match.getSetup().getTargetType() == null || match.getSetup().getTargetType().trim().isEmpty()) {
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSumInfo " + 
											CricketFunctions.generateMatchSummaryStatus(whichInning, match, CricketUtil.FULL).toUpperCase() + ";");
								}else {
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSumInfo " + 
											CricketFunctions.generateMatchSummaryStatus(whichInning, match, CricketUtil.FULL).toUpperCase() + " (" + 
												match.getSetup().getTargetType().toUpperCase() + ")" + ";");
								}
							}
						}
					}
				
				print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW ON;");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*CONTAINER SET ACTIVE 0;");
				if(which_graphics_onscreen == "BOWLINGCARD") {
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$BowlingCard_GRP*CONTAINER SET ACTIVE 0;");
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$Summary_GRP*CONTAINER SET ACTIVE 1;");
				}else if(which_graphics_onscreen == "SCORECARD") {
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$BattingCard_GRP*CONTAINER SET ACTIVE 0;");
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$Summary_GRP*CONTAINER SET ACTIVE 1;");
				}else if(which_graphics_onscreen == "POINTSTABLE") {
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$Summary_GRP*CONTAINER SET ACTIVE 1;");
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$PointsTable_GRP*CONTAINER SET ACTIVE 0;");
				}
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In STOP;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out STOP;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 30.0;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*SummaryIn SHOW 109.0;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out SHOW 0.0;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*SummaryOut SHOW 0.0;");
				print_writer.println("LAYER1*EVEREST*GLOBAL SNAPSHOT_PATH C:/Temp/Preview.png;");
				print_writer.println("LAYER1*EVEREST*GLOBAL SNAPSHOT 1920 1080;");
				TimeUnit.SECONDS.sleep(1);
				
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*SummaryOut SHOW 0.0;");
				if(which_graphics_onscreen == "BOWLINGCARD" || which_graphics_onscreen == "SCORECARD" || which_graphics_onscreen == "POINTSTABLE") {
					print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 30.0;");
				}else {
					print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 0.0;");
				}
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out SHOW 0.0;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*SummaryIn SHOW 0.0;");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*CONTAINER SET ACTIVE 1;");
				if(which_graphics_onscreen == "BOWLINGCARD") {
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$BowlingCard_GRP*CONTAINER SET ACTIVE 1;");
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$Summary_GRP*CONTAINER SET ACTIVE 0;");
				}else if(which_graphics_onscreen == "SCORECARD") {
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$BattingCard_GRP*CONTAINER SET ACTIVE 1;");
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$Summary_GRP*CONTAINER SET ACTIVE 0;");
				}else if(which_graphics_onscreen == "POINTSTABLE") {
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$Summary_GRP*CONTAINER SET ACTIVE 0;");
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$PointsTable_GRP*CONTAINER SET ACTIVE 1;");
				}
				print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW OFF;");
				
				this.status = CricketUtil.SUCCESSFUL;	
			
			}
			break;
		}
		
	}
	public void populateTeamsLogo(PrintWriter print_writer,String viz_scene, List<Team> teams ,MatchAllData match, String session_selected_broadcaster) throws InterruptedException {
		
		print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBatTeamName " + "TEAMS" +";");
		print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBatSubHeader " + match.getSetup().getTournament().toUpperCase() + ";");
		
		print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgBatTeamLogo " + logo_path + "TLogo" + CricketUtil.PNG_EXTENSION + ";");
		
//		print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeamFirstName " + match.getMatchIdent().toUpperCase() + ";");
//		print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeamLastName " + match.getMatchIdent().toUpperCase() + ";");
//
//		print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgGroupATeamLogo01 " + logo_path + teams.get(0).getTeamName4() + CricketUtil.PNG_EXTENSION + ";");
//		print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgGroupATeamLogo02 " + logo_path + teams.get(1).getTeamName4() + CricketUtil.PNG_EXTENSION + ";");
//		print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgGroupATeamLogo03 " + logo_path + teams.get(2).getTeamName4() + CricketUtil.PNG_EXTENSION + ";");
//		
//		print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgGroupBTeamLogo01 " + logo_path + teams.get(3).getTeamName4() + CricketUtil.PNG_EXTENSION + ";");
//		print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgGroupBTeamLogo02 " + logo_path + teams.get(4).getTeamName4() + CricketUtil.PNG_EXTENSION + ";");
//		print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgGroupBTeamLogo03 " + logo_path + teams.get(5).getTeamName4() + CricketUtil.PNG_EXTENSION + ";");
//		
//		print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgGroupCTeamLogo01 " + logo_path + teams.get(6).getTeamName4() + CricketUtil.PNG_EXTENSION + ";");
//		print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgGroupCTeamLogo02 " + logo_path + teams.get(7).getTeamName4() + CricketUtil.PNG_EXTENSION + ";");
//		print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgGroupCTeamLogo03 " + logo_path + teams.get(8).getTeamName4() + CricketUtil.PNG_EXTENSION + ";");
//		
//		print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgGroupDTeamLogo01 " + logo_path + teams.get(9).getTeamName4() + CricketUtil.PNG_EXTENSION + ";");
//		print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgGroupDTeamLogo02 " + logo_path + teams.get(10).getTeamName4() + CricketUtil.PNG_EXTENSION + ";");
//		print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgGroupDTeamLogo03 " + logo_path + teams.get(11).getTeamName4() + CricketUtil.PNG_EXTENSION + ";");
		
		print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW ON;");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*CONTAINER SET ACTIVE 0;");
		print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In STOP;");
		print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out STOP;");
		print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 196.0;");
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
	
	public void populateBugToss(PrintWriter print_writer,String viz_scene, MatchAllData match, String session_selected_broadcaster) throws InterruptedException
	{
		switch (session_selected_broadcaster.toUpperCase()) {
		case "FRUIT":
			if (match == null) {
				this.status = "ERROR: Match is null";
			} else if (match.getMatch().getInning() == null) {
				this.status = "ERROR: Bug's inning is null";
			} else {
				
				if(match.getSetup().getTossWinningTeam() == match.getSetup().getHomeTeamId()) {
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerName01 " + match.getSetup().getHomeTeam().getTeamName3().toUpperCase() + ";");
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInfo1A " + " WON TOSS & CHOSE TO " + match.getSetup().getTossWinningDecision() + ";");

				}else {
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerName01 " + match.getSetup().getAwayTeam().getTeamName3().toUpperCase() + ";");
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInfo1A " + " WON TOSS & CHOSE TO " + match.getSetup().getTossWinningDecision() + ";");

				}
				
				
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInfo1B " + "" + ";");
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInfo1C " + "" + ";");	
					
				print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW ON;");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*CONTAINER SET ACTIVE 0;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In STOP;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out STOP;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 32.0;");
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
	public void populateBugDismissal(PrintWriter print_writer,String viz_scene, int whichInning, String statsType, int playerId, MatchAllData match, String session_selected_broadcaster) throws InterruptedException
	{
		switch (session_selected_broadcaster.toUpperCase()) {
		case "FRUIT":
			if (match == null) {
				this.status = "ERROR: Match is null";
			} else if (match.getMatch().getInning() == null) {
				this.status = "ERROR: Bug's inning is null";
			} else {
				
				for(Inning inn : match.getMatch().getInning()) {
					if (inn.getInningNumber() == whichInning) {
						switch(statsType.toUpperCase()) {
						case CricketUtil.BATSMAN :
							for (BattingCard bc : inn.getBattingCard()) {
								if(bc.getPlayerId()==playerId) {
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerName01 " + bc.getPlayer().getFull_name() + ";");
									
									if(bc.getStatus().equalsIgnoreCase(CricketUtil.STILL_TO_BAT)) {
										if(bc.getHowOut().equalsIgnoreCase(CricketUtil.RETIRED_HURT)) {
											print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInfo1A " + "retired hurt" + ";");
										}else if(bc.getHowOut().toUpperCase().equalsIgnoreCase(CricketUtil.ABSENT_HURT)) {
											print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInfo1A " + "absent hurt" + ";");
										}
									}else if(bc.getStatus().equalsIgnoreCase(CricketUtil.OUT)) {
										if(bc.getHowOutPartOne().trim() == "") {
											print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInfo1A " + bc.getHowOutText() + ";");								
										}else {
											if(bc.getHowOut().equalsIgnoreCase(CricketUtil.LBW)) {
												print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInfo1A " + bc.getHowOutText() + ";");
											}else if(bc.getHowOut().equalsIgnoreCase(CricketUtil.RUN_OUT)) {
												if(CricketFunctions.checkImpactPlayerBowler(match.getEventFile().getEvents(), whichInning, bc.getHowOutFielderId()).equalsIgnoreCase(CricketUtil.YES) || 
														CricketFunctions.checkImpactPlayer(match.getEventFile().getEvents(), whichInning, bc.getHowOutFielderId()).equalsIgnoreCase(CricketUtil.YES)) {
													
													print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInfo1A " + 
															bc.getHowOutPartOne() + " " + bc.getHowOutPartTwo().split(" ")[0] + ";");
												}else {
													if(bc.getWasHowOutFielderSubstitute() != null && bc.getWasHowOutFielderSubstitute().equalsIgnoreCase(CricketUtil.YES)) {
														print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInfo1A " + bc.getHowOutPartOne() + 
																" (sub - " + bc.getHowOutPartTwo().split(" ")[0] + " )" + ";");
													}else {
														print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInfo1A " + bc.getHowOutPartOne() + 
																" (" + bc.getHowOutPartTwo().split(" ")[0] + " )" + ";");
													}
												}
											}else if(bc.getHowOut().equalsIgnoreCase(CricketUtil.CAUGHT)) {
												if(CricketFunctions.checkImpactPlayerBowler(match.getEventFile().getEvents(), whichInning, bc.getHowOutFielderId()).equalsIgnoreCase(CricketUtil.YES) || 
														CricketFunctions.checkImpactPlayer(match.getEventFile().getEvents(), whichInning, bc.getHowOutFielderId()).equalsIgnoreCase(CricketUtil.YES)) {
													
													print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInfo1A " + bc.getHowOutPartOne().replace("(SUB)", "") + 
															" " + bc.getHowOutPartTwo() + ";");
												}else {
													print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInfo1A " + bc.getHowOutText() + ";");
												}
											}else {
												print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInfo1A " + bc.getHowOutText() + ";");
											}
										}
									}
									
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInfo1B " + bc.getRuns() + ";");
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInfo1C " + bc.getBalls() + ";");
								}
							}
							break;
						}
					}
				}
				print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW ON;");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*CONTAINER SET ACTIVE 0;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In STOP;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out STOP;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 32.0;");
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
	public void populateBug(PrintWriter print_writer,String viz_scene, int whichInning, String statsType, int playerId, MatchAllData match, String session_selected_broadcaster) throws InterruptedException
	{
		switch (session_selected_broadcaster.toUpperCase()) {
		case "FRUIT":
			if (match == null) {
				this.status = "ERROR: Match is null";
			} else if (match.getMatch().getInning() == null) {
				this.status = "ERROR: Bug's inning is null";
			} else {
				
				for(Inning inn : match.getMatch().getInning()) {
					if (inn.getInningNumber() == whichInning) {
						switch(statsType.toUpperCase()) {
						case CricketUtil.BATSMAN :
							for (BattingCard bc : inn.getBattingCard()) {
								if(bc.getPlayerId()==playerId) {
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerName01 " + bc.getPlayer().getFull_name() + ";");
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInfo1A " + "4s:" + bc.getFours()  + " 6s:"  + bc.getSixes() + ";");
									
									if(bc.getStatus().toUpperCase().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
										print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInfo1B " + bc.getRuns() + "*" + ";");
									}
									else {
										print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInfo1B " + bc.getRuns() + ";");
									}
									
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInfo1C " + bc.getBalls() + ";");
								}
							}
							break;
						}
					}
				}
				print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW ON;");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*CONTAINER SET ACTIVE 0;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In STOP;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out STOP;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 32.0;");
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
	public void populateBugBowler(PrintWriter print_writer,String viz_scene, int whichInning, String statsType, int playerId, MatchAllData match, String session_selected_broadcaster) throws InterruptedException
	{
		switch (session_selected_broadcaster.toUpperCase()) {
		case "FRUIT":
			if (match == null) {
				this.status = "ERROR: Match is null";
			} else if (match.getMatch().getInning() == null) {
				this.status = "ERROR: Bug's inning is null";
			} else {
				for(Inning inn : match.getMatch().getInning()) {
					if (inn.getInningNumber() == whichInning) {
						switch(statsType.toUpperCase()) {
						case CricketUtil.BOWLER :
							for (BowlingCard boc : inn.getBowlingCard()) {
								if(boc.getPlayerId()==playerId) {
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerName01 " + boc.getPlayer().getTicker_name() + ";");
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInfo1A " + " " + ";");
								
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInfo1B " + boc.getWickets() + slashOrDash + boc.getRuns() + ";");
									
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInfo1C " + CricketFunctions.OverBalls(boc.getOvers(), boc.getBalls()) + ";");
								}
							}
							break;
						}
							
					}
						
				}
				print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW ON;");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*CONTAINER SET ACTIVE 0;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In STOP;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out STOP;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 32.0;");
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
	public void populateBugPartnership(PrintWriter print_writer, String viz_scene, MatchAllData match,String session_selected_broadcaster) throws InterruptedException {
		switch (session_selected_broadcaster.toUpperCase()) {
		case "FRUIT":
			if (match == null) {
				this.status = "ERROR: Match is null";
			} else if (match.getMatch().getInning() == null) {
				this.status = "ERROR: Partnership inning is null";
			} else {
				for (Inning inn : match.getMatch().getInning()) {
					if(inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)) {
						String Left_Batsman ="",Right_Batsman="";
						
						for (Player hs : match.getSetup().getHomeSquad()) {
							if(hs.getPlayerId() == inn.getPartnerships().get(inn.getPartnerships().size() - 1).getFirstBatterNo()) {
								Left_Batsman = hs.getTicker_name();
							}
							if(hs.getPlayerId() == inn.getPartnerships().get(inn.getPartnerships().size() - 1).getSecondBatterNo()) {
								Right_Batsman = hs.getTicker_name();
							}
						}
						for (Player as : match.getSetup().getAwaySquad()) {
							if(as.getPlayerId() == inn.getPartnerships().get(inn.getPartnerships().size() - 1).getFirstBatterNo()) {
								Left_Batsman = as.getTicker_name();
							}
							if(as.getPlayerId() == inn.getPartnerships().get(inn.getPartnerships().size() - 1).getSecondBatterNo()) {
								Right_Batsman = as.getTicker_name();
							}
						}
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerName01 " + Left_Batsman + " & " + Right_Batsman + ";");
						if(inn.getTotalWickets() == 0) {
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInfo1A " + (inn.getTotalWickets() + 1) + "st WICKET PARTNERSHIP" + ";");
						}else if(inn.getTotalWickets() == 1) {
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInfo1A " + (inn.getTotalWickets() + 1) + "nd WICKET PARTNERSHIP" + ";");
						}else if(inn.getTotalWickets() == 2) {
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInfo1A " + (inn.getTotalWickets() + 1) + "rd WICKET PARTNERSHIP" + ";");
						}else {
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInfo1A " + (inn.getTotalWickets() + 1) + "th WICKET PARTNERSHIP" + ";");
						}
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInfo1B " + 
								inn.getPartnerships().get(inn.getPartnerships().size() - 1).getTotalRuns() + ";");
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInfo1C " + 
								inn.getPartnerships().get(inn.getPartnerships().size() - 1).getTotalBalls() + ";");
					}
				}
				print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW ON;");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*CONTAINER SET ACTIVE 0;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In STOP;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out STOP;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 32.0;");
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
	public void populateBugsDB(PrintWriter print_writer,String viz_scene, Bugs bug ,MatchAllData match, String session_selected_broadcaster) throws InterruptedException
	{
		switch (session_selected_broadcaster.toUpperCase()) {
		case "FRUIT":
			if (match == null) {
				this.status = "ERROR: Match is null";
			} else {
				
				if(bug.getText1() != null && bug.getText2() != null) {
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerName01 " + bug.getText1() +";");
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInfo1B " + " " +";");
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInfo1C " + " " +";");
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInfo1A " + bug.getText2() +";");
				}else if(bug.getText1() != null) {
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerName01 " + bug.getText1() +";");

					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInfo1B " + " " +";");
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInfo1C " + " " +";");
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInfo1A " + " " +";");
				}else {
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerName01 " + bug.getText2() +";");
					

					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInfo1B " + " " +";");
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInfo1C " + " " +";");
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInfo1A " + " " +";");
					
				}
				
				print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW ON;");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*CONTAINER SET ACTIVE 0;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In STOP;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out STOP;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 32.0;");
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
	public void populateBugHighlight(PrintWriter print_writer, String viz_scene, int whichInning, MatchAllData match,String session_selected_broadcaster) throws InterruptedException {
		
		if (match == null) {
			this.status = "ERROR: Match is null";
		} else if (match.getMatch().getInning() == null) {
			this.status = "ERROR: Powerplay's inning is null";
		} else {
			
			String Value = "";
			
			print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerName01 " + "HIGHLIGHTS" + ";");
			print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInfo1B " + " " + ";");
			print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInfo1C " + "" + ";");
			if (match.getMatch().getInning().get(whichInning-1).getTotalWickets() >= 10) {
				Value = String.valueOf(match.getMatch().getInning().get(whichInning-1).getTotalRuns());
			} else {
				Value = String.valueOf(match.getMatch().getInning().get(whichInning-1).getTotalRuns()) + " - " + String.valueOf(match.getMatch().getInning().get(whichInning-1).getTotalWickets());
			}
			
			print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInfo1A " + match.getMatch().getInning().get(whichInning-1).getBatting_team().getTeamName1().toUpperCase() + " : " + Value + " (" + 
					CricketFunctions.OverBalls(match.getMatch().getInning().get(whichInning-1).getTotalOvers(),match.getMatch().getInning().get(whichInning-1).getTotalBalls()) + ")" + ";");
			
		}
		print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW ON;");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*CONTAINER SET ACTIVE 0;");
		print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In STOP;");
		print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out STOP;");
		print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 32.0;");
		print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out SHOW 0.0;");
		print_writer.println("LAYER1*EVEREST*GLOBAL SNAPSHOT_PATH C:/Temp/Preview.png;");
		print_writer.println("LAYER1*EVEREST*GLOBAL SNAPSHOT 1920 1080;");
		TimeUnit.SECONDS.sleep(1);
		print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out SHOW 0.0;");
		print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 0.0;");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*CONTAINER SET ACTIVE 1;");
		print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW OFF;");
		TimeUnit.MILLISECONDS.sleep(200);		
	}

	public void populateBugPowerPLay(PrintWriter print_writer, String viz_scene, int whichInning, MatchAllData match,String session_selected_broadcaster) throws InterruptedException {
		switch (session_selected_broadcaster.toUpperCase()) {
		case "FRUIT":
			if (match == null) {
				this.status = "ERROR: Match is null";
			} else if (match.getMatch().getInning() == null) {
				this.status = "ERROR: Powerplay's inning is null";
			} else {
				for (Inning inn : match.getMatch().getInning()) {
					print_writer.println(
							"LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInfo1A " + "POWERPLAY" + ";");
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInfo1B "
							+ CricketFunctions.getPowerPlayScore(inn, whichInning,"-", match.getEventFile().getEvents()) + ";");
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInfo1C " + "" + ";");

					if (whichInning == 1) {
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerName01 "
								+ match.getMatch().getInning().get(0).getBatting_team().getTeamName3() + ";");
					} else {
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerName01 "
								+ match.getMatch().getInning().get(1).getBatting_team().getTeamName3().toUpperCase() + ";");
					}
				}
				print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW ON;");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*CONTAINER SET ACTIVE 0;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In STOP;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out STOP;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 32.0;");
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
	public void populatetournamentFour(PrintWriter print_writer, String viz_scene,List<MatchAllData> tourn_matches,MatchAllData match, String broadcaster) throws InterruptedException {
		switch (session_selected_broadcaster.toUpperCase()) {
		case "FRUIT":
			print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInfo1B " + (match.getMatch().getInning().get(0).getTotalFours() + match.getMatch().getInning().get(1).getTotalFours()) + ";");
//			print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInfo1B " + match.getTournament().toUpperCase() + ";");
			print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW ON;");
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In STOP;");
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out STOP;");
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 32.0;");
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out SHOW 0.0;");
			print_writer.println("LAYER1*EVEREST*GLOBAL SNAPSHOT_PATH C:/Temp/Preview.png;");
			print_writer.println("LAYER1*EVEREST*GLOBAL SNAPSHOT 1920 1080;");
			TimeUnit.SECONDS.sleep(1);
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out SHOW 0.0;");
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 0.0;");
			print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW OFF;");
				
			this.status = CricketUtil.SUCCESSFUL;
			break;
		}
	}
	public void populatetournamentSix(PrintWriter print_writer, String viz_scene,List<MatchAllData> tourn_matches,MatchAllData match, String broadcaster) throws InterruptedException {
		switch (session_selected_broadcaster.toUpperCase()) {
		case "FRUIT":
			print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInfo1B " + (match.getMatch().getInning().get(0).getTotalSixes() + match.getMatch().getInning().get(1).getTotalSixes()) + ";");
//			print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInfo1B " + match.getTournament().toUpperCase() + ";");
			print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW ON;");
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In STOP;");
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out STOP;");
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 32.0;");
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out SHOW 0.0;");
			print_writer.println("LAYER1*EVEREST*GLOBAL SNAPSHOT_PATH C:/Temp/Preview.png;");
			print_writer.println("LAYER1*EVEREST*GLOBAL SNAPSHOT 1920 1080;");
			TimeUnit.SECONDS.sleep(1);
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out SHOW 0.0;");
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 0.0;");
			print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW OFF;");
				
			this.status = CricketUtil.SUCCESSFUL;
			break;
		}
	}
	public void populatedroneBug(PrintWriter print_writer, String viz_scene,MatchAllData match, String broadcaster) throws InterruptedException {
		switch (session_selected_broadcaster.toUpperCase()) {
		case "FRUIT":
			print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerName01 " + "DRONE SPONSORED BY DAFANEWS" + ";");
			//print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInfo1A " + " " + ";");
			//print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInfo1B " + "" + ";");
			//print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInfo1C " + " " + ";");
			
			print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW ON;");
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*CONTAINER SET ACTIVE 0;");
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In STOP;");
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out STOP;");
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 32.0;");
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out SHOW 0.0;");
			print_writer.println("LAYER1*EVEREST*GLOBAL SNAPSHOT_PATH C:/Temp/Preview.png;");
			print_writer.println("LAYER1*EVEREST*GLOBAL SNAPSHOT 1920 1080;");
			TimeUnit.SECONDS.sleep(1);
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out SHOW 0.0;");
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 0.0;");
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*CONTAINER SET ACTIVE 1;");
			print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW OFF;");
				
			this.status = CricketUtil.SUCCESSFUL;
			break;
		}
	}
	
	public void populateHowout(PrintWriter print_writer,String viz_scene, int whichInning, String statsType, int playerId, MatchAllData match, String session_selected_broadcaster) throws InterruptedException, IOException
	{
		switch (session_selected_broadcaster.toUpperCase()) {
		case "FRUIT":
			if (match == null) {
				this.status = "ERROR: Match is null";
			} else if (match.getMatch().getInning() == null) {
				this.status = "ERROR: HowOut's inning is null";
			} else {
				//String commands = "";
				for(Inning inn : match.getMatch().getInning()) {
					if (inn.getInningNumber() == whichInning) {
						for (BattingCard bc : inn.getBattingCard()) {
							if(bc.getPlayerId()==playerId) {
								
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgTeamLogo " + logo_path + inn.getBatting_team().getTeamName4() + CricketUtil.PNG_EXTENSION  + ";");
								
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFirstName " + bc.getPlayer().getFull_name() + ";");
							
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerRuns " + bc.getRuns() + ";");
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerBalls " + bc.getBalls() + ";");
								
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStatValue01 " + bc.getFours() + ";");
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStatValue02 " + bc.getSixes() + ";");
								
								//print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStatHead03 " + "S/R" + ";");
								if(bc.getStrikeRate() == null) {
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStatValue03 " + "-" + ";");
								}else {
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStatValue03 " + bc.getStrikeRate() + ";");
								}
								
								if(bc.getStatus().equalsIgnoreCase(CricketUtil.STILL_TO_BAT)) {
									if(bc.getHowOut().equalsIgnoreCase(CricketUtil.RETIRED_HURT)) {
										print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHowOut " + "retired hurt" + ";");
										print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBowler " + " " + ";");
									}else if(bc.getHowOut().toUpperCase().equalsIgnoreCase(CricketUtil.ABSENT_HURT)) {
										print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHowOut " + "absent hurt" + ";");
										print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBowler " + " " + ";");
									}
								}else if(bc.getStatus().equalsIgnoreCase(CricketUtil.OUT)) {
									if(bc.getHowOutPartOne().trim() == "") {
										print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHowOut " + bc.getHowOutText() + ";");
										print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBowler " + " " + ";");								
									}else {
										if(bc.getHowOut().equalsIgnoreCase(CricketUtil.LBW)) {
											print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHowOut " + bc.getHowOutText() + ";");
											print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBowler " + " " + ";");
										}else if(bc.getHowOut().equalsIgnoreCase(CricketUtil.RUN_OUT)) {
											if(CricketFunctions.checkImpactPlayerBowler(match.getEventFile().getEvents(), whichInning, bc.getHowOutFielderId()).equalsIgnoreCase(CricketUtil.YES) || 
													CricketFunctions.checkImpactPlayer(match.getEventFile().getEvents(), whichInning, bc.getHowOutFielderId()).equalsIgnoreCase(CricketUtil.YES)) {
												
												print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHowOut " + bc.getHowOutPartOne() + ";");
												print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBowler " + bc.getHowOutPartTwo().split(" ")[0] + ";");
												
											}else {
												if(bc.getWasHowOutFielderSubstitute() != null && bc.getWasHowOutFielderSubstitute().equalsIgnoreCase(CricketUtil.YES)) {
													print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHowOut " + bc.getHowOutPartOne() + 
															" (sub - " + bc.getHowOutPartTwo().split(" ")[0] + " )" + ";");
													print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBowler " + " " + ";");
												}else {
													print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHowOut " + bc.getHowOutPartOne() + 
															" (" + bc.getHowOutPartTwo().split(" ")[0] + " )" + ";");
													print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBowler " + " " + ";");
												}
												
											}
										}else if(bc.getHowOut().equalsIgnoreCase(CricketUtil.CAUGHT)) {
											if(CricketFunctions.checkImpactPlayerBowler(match.getEventFile().getEvents(), whichInning, bc.getHowOutFielderId()).equalsIgnoreCase(CricketUtil.YES) || 
													CricketFunctions.checkImpactPlayer(match.getEventFile().getEvents(), whichInning, bc.getHowOutFielderId()).equalsIgnoreCase(CricketUtil.YES)) {
												
												print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHowOut " + bc.getHowOutPartOne().replace("(SUB)", "") + ";");
												print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBowler " + bc.getHowOutPartTwo() + ";");
											
											}else {
												print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHowOut " + bc.getHowOutText() + ";");
												print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBowler " + " " + ";");
											}
										}else {
											print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHowOut " + bc.getHowOutText() + ";");
											print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBowler " + " " + ";");
										}
									}
								}
							}
						}
					}
				}
				print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW ON;");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*CONTAINER SET ACTIVE 0;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In STOP;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out STOP;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 108.0;");
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
	public void populateHowoutBoth(PrintWriter print_writer,String viz_scene, int whichInning, String statsType, int playerId, MatchAllData match, String session_selected_broadcaster) throws InterruptedException, IOException
	{
		switch (session_selected_broadcaster.toUpperCase()) {
		
		case "FRUIT":
			if (match == null) {
				this.status = "ERROR: Match is null";
			} else if (match.getMatch().getInning() == null) {
				this.status = "ERROR: HowOut's inning is null";
			} else {
				int row = 0 ; 
				for(Inning inn : match.getMatch().getInning()) {
					//for (int i = 1; i <= whichInning; i++) {
					//if (inn.getInningNumber() == whichInning) {
						for (BattingCard bc : inn.getBattingCard()) {
							if(bc.getPlayerId()==playerId) {
								row = row + 1 ;
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgTeamLogo " + logo_path + inn.getBatting_team().getTeamName4() + CricketUtil.PNG_EXTENSION  + ";");
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgTLogo " + logo_path + "TLogo" + 
										CricketUtil.PNG_EXTENSION + ";");
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFirstName " + bc.getPlayer().getFull_name() + ";");
								
								//print_writer.println("LAYER2*EVEREST*TREEVIEW*LastName*CONTAINER SET ACTIVE 0;");
								
								//print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerRuns " + bc.getRuns() + ";");
								
								//print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerBalls " + bc.getBalls() + ";");
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHowOut0" + row + " " + bc.getHowOutText() + ";");

								/*if (bc.getHowOutPartOne().trim().equalsIgnoreCase("")){
									
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHowOut0" + row + " " + bc.getHowOutText() + ";");
									
									//print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBowler " + " " + ";");
								
								}else {
									
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHowOut01 " + bc.getHowOutText() + ";");
									
									//print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBowler " + bc.getHowOutPartTwo() + ";");
								}*/
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tRuns0" + row + " " + bc.getRuns() + ";");
								
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBalls0" + row + " " + bc.getBalls() + ";");
								
								//print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStatValue01 " + bc.getFours() + ";");
								
								//print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStatValue02 " + bc.getSixes() + ";");
								
								/*if(match.getMatchType().equalsIgnoreCase(CricketUtil.TEST)) {
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFOWValue " + inn.getTotalRuns() + slashOrDash + inn.getTotalWickets() + ";");
								}*/
								//print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStatHead03 " + "S/R" + ";");

								//print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStatValue03 " + bc.getStrikeRate() + ";");
							}
						}
					//}
				}
				print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW ON;");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*CONTAINER SET ACTIVE 0;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In STOP;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out STOP;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 196.0;");
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
	public void populateL3Howout(PrintWriter print_writer,String viz_scene,MatchAllData match, String session_selected_broadcaster) throws InterruptedException, IOException {
		switch (session_selected_broadcaster.toUpperCase()) {
		case "FRUIT":
			if (match == null) {
				this.status = "ERROR: Match is null";
			} else if (match.getMatch().getInning() == null) {
				this.status = "ERROR: HowOut's inning is null";
			} else {
				
				//String commands = "";
				for(Inning inn : match.getMatch().getInning()) {
					if(inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)) {
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgTeamLogo " + logo_path + inn.getBatting_team().getTeamName4() + CricketUtil.PNG_EXTENSION  + ";");
						for(BattingCard bc : inn.getBattingCard()) {
							if(inn.getFallsOfWickets().size() > 0) {
								if(inn.getFallsOfWickets().get(inn.getFallsOfWickets().size() - 1).getFowPlayerID() == bc.getPlayerId()) {
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFirstName " + bc.getPlayer().getFull_name() + ";");
									TimeUnit.MILLISECONDS.sleep(250);
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerRuns " + bc.getRuns() + ";");
									TimeUnit.MILLISECONDS.sleep(250);
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerBalls " + bc.getBalls() + ";");
									TimeUnit.MILLISECONDS.sleep(250);
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStatValue01 " + bc.getFours() + ";");
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStatValue02 " + bc.getSixes() + ";");
									
									//print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStatHead03 " + "S/R" + ";");
									if(bc.getStrikeRate() == null) {
										print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStatValue03 " + "-" + ";");
									}else {
										print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStatValue03 " + bc.getStrikeRate() + ";");
									}
									
									if(bc.getStatus().equalsIgnoreCase(CricketUtil.STILL_TO_BAT)) {
										if(bc.getHowOut().equalsIgnoreCase(CricketUtil.RETIRED_HURT)) {
											print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHowOut " + "retired hurt" + ";");
											print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBowler " + " " + ";");
										}else if(bc.getHowOut().toUpperCase().equalsIgnoreCase(CricketUtil.ABSENT_HURT)) {
											print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHowOut " + "absent hurt" + ";");
											print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBowler " + " " + ";");
										}
									}else if(bc.getStatus().equalsIgnoreCase(CricketUtil.OUT)) {
										if(bc.getHowOutPartOne().trim() == "") {
											print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHowOut " + bc.getHowOutText() + ";");
											print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBowler " + " " + ";");								
										}else {
											if(bc.getHowOut().equalsIgnoreCase(CricketUtil.LBW)) {
												print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHowOut " + bc.getHowOutText() + ";");
												print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBowler " + " " + ";");
											}else if(bc.getHowOut().equalsIgnoreCase(CricketUtil.RUN_OUT)) {
												if(CricketFunctions.checkImpactPlayerBowler(match.getEventFile().getEvents(), inn.getInningNumber(), bc.getHowOutFielderId()).equalsIgnoreCase(CricketUtil.YES) || 
														CricketFunctions.checkImpactPlayer(match.getEventFile().getEvents(), inn.getInningNumber(), bc.getHowOutFielderId()).equalsIgnoreCase(CricketUtil.YES)) {
													
													print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHowOut " + bc.getHowOutPartOne() + ";");
													print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBowler " + bc.getHowOutPartTwo().split(" ")[0] + ";");
													
												}else {
													if(bc.getWasHowOutFielderSubstitute() != null && bc.getWasHowOutFielderSubstitute().equalsIgnoreCase(CricketUtil.YES)) {
														print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHowOut " + bc.getHowOutPartOne() + 
																" (sub - " + bc.getHowOutPartTwo().split(" ")[0] + " )" + ";");
														print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBowler " + " " + ";");
													}else {
														print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHowOut " + bc.getHowOutPartOne() + 
																" (" + bc.getHowOutPartTwo().split(" ")[0] + " )" + ";");
														print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBowler " + " " + ";");
													}
												}
											}else if(bc.getHowOut().equalsIgnoreCase(CricketUtil.CAUGHT)) {
												if(CricketFunctions.checkImpactPlayerBowler(match.getEventFile().getEvents(), inn.getInningNumber(), bc.getHowOutFielderId()).equalsIgnoreCase(CricketUtil.YES) || 
														CricketFunctions.checkImpactPlayer(match.getEventFile().getEvents(), inn.getInningNumber(), bc.getHowOutFielderId()).equalsIgnoreCase(CricketUtil.YES)) {
													
													print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHowOut " + bc.getHowOutPartOne().replace("(SUB)", "") + ";");
													print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBowler " + bc.getHowOutPartTwo() + ";");
												
												}else {
													print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHowOut " + bc.getHowOutText() + ";");
													print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBowler " + " " + ";");
												}
											}else {
												print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHowOut " + bc.getHowOutText() + ";");
												print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBowler " + " " + ";");
											}
										}
									}
									
								}
							}
						}
					}
				}
				print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW ON;");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*CONTAINER SET ACTIVE 0;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In STOP;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out STOP;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 108.0;");
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
	public void populateHowoutWithoutFielder(PrintWriter print_writer,String viz_scene, int whichInning, String statsType, int playerId, MatchAllData match, String session_selected_broadcaster) throws InterruptedException
	{
		switch (session_selected_broadcaster.toUpperCase()) {
		case "FRUIT":
			if (match == null) {
				this.status = "ERROR: Match is null";
			} else if (match.getMatch().getInning() == null) {
				this.status = "ERROR: HowOut's inning is null";
			} else {
				
				for(Inning inn : match.getMatch().getInning()) {
					if (inn.getInningNumber() == whichInning) {
						for (BattingCard bc : inn.getBattingCard()) {
							if(bc.getPlayerId()==playerId) {
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgTeamLogo " + logo_path + inn.getBatting_team().getTeamName4() + CricketUtil.PNG_EXTENSION  + ";");
								
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFirstName " + bc.getPlayer().getFull_name() + ";");
								
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerRuns " + bc.getRuns() + ";");
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerBalls " + ( bc.getBalls() + 1 ) + ";");
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHowOut " + " " + ";");
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBowler " + " " + ";");
								
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStatValue01 " + bc.getFours() + ";");
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStatValue02 " + bc.getSixes() + ";");
								
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStatValue03 " + bc.getStrikeRate() + ";");
							}
						}
					}
				}
				print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW ON;");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*CONTAINER SET ACTIVE 0;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In STOP;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out STOP;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 108.0;");
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
	public void populateL3HowoutWithoutFielder(PrintWriter print_writer,String viz_scene,MatchAllData match, String session_selected_broadcaster) throws InterruptedException, IOException {
		switch (session_selected_broadcaster.toUpperCase()) {
		case "FRUIT":
			if (match == null) {
				this.status = "ERROR: Match is null";
			} else if (match.getMatch().getInning() == null) {
				this.status = "ERROR: HowOut's inning is null";
			} else {
				
				//String commands = "";
				for(Inning inn : match.getMatch().getInning()) {
					if(inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)) {
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgTeamLogo " + logo_path + inn.getBatting_team().getTeamName4() + CricketUtil.PNG_EXTENSION  + ";");
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgTLogo " + logo_path + "TLogo" + 
								CricketUtil.PNG_EXTENSION + ";");
						for(BattingCard bc : inn.getBattingCard()) {
							if(inn.getFallsOfWickets().size() > 0) {
								if(inn.getFallsOfWickets().get(inn.getFallsOfWickets().size() - 1).getFowPlayerID() == bc.getPlayerId()) {
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFirstName " + bc.getPlayer().getFull_name() + ";");
									
									print_writer.println("LAYER2*EVEREST*TREEVIEW*LastName*CONTAINER SET ACTIVE 0;");
									
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerRuns " + bc.getRuns() + ";");
									
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerBalls " + bc.getBalls() + ";");
									
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHowOut " + " " + ";");
									
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBowler " + " " + ";");
									
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStatValue01 " + bc.getFours() + ";");
									
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStatValue02 " + bc.getSixes() + ";");
									
									if(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.TEST)) {
										print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$BigBandGrp$FOW_GRP*CONTAINER SET ACTIVE 1;");
										print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFOWValue " + 
												inn.getFallsOfWickets().get(inn.getFallsOfWickets().size() - 1).getFowRuns() + slashOrDash + 
												inn.getFallsOfWickets().get(inn.getFallsOfWickets().size() - 1).getFowNumber() + ";");
									}else {
										print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$BigBandGrp$FOW_GRP*CONTAINER SET ACTIVE 0;");
									}
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStatHead03 " + "S/R" + ";");
									
									if(bc.getStrikeRate() == null) {
										print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStatValue03 " + "-" + ";");
									}else {
										print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStatValue03 " + bc.getStrikeRate() + ";");
									}
								}
							}
						}
					}
				}
				print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW ON;");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*CONTAINER SET ACTIVE 0;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In STOP;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out STOP;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 196.0;");
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
	public void populateBatsmanstats(PrintWriter print_writer,String viz_scene, int whichInning, String statsType, int playerId, MatchAllData match, String session_selected_broadcaster) throws InterruptedException
	{
		switch (session_selected_broadcaster.toUpperCase()) {
		case "FRUIT":
			if (match == null) {
				this.status = "ERROR: Match is null";
			} else if (match.getMatch().getInning() == null) {
				this.status = "ERROR: PlayerStats's inning is null";
			} else {
				int total_inn = 0;
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgTLogo " + logo_path + "TLogo" + 
						CricketUtil.PNG_EXTENSION + ";");
				for(Inning inn : match.getMatch().getInning()) {
					if(inn.getInningStatus() != null) {
						total_inn = total_inn + 1;
					}
				}
				
				if(total_inn > 0 && whichInning > total_inn) {
					whichInning = total_inn;
				}
				for(Inning inn : match.getMatch().getInning()) {
					if (inn.getInningNumber() == whichInning) {
						String[] Count = CricketFunctions.getScoreTypeData(CricketUtil.BATSMAN,match, whichInning, playerId,",", match.getEventFile().getEvents()).split(",");
						switch(statsType.toUpperCase()) {
						case CricketUtil.BATSMAN :
							for (BattingCard bc : inn.getBattingCard()) {
								if(bc.getPlayerId()==playerId) {
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerName " + bc.getPlayer().getFull_name() + ";");
									if(bc.getStatus().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
										print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBatsmanRuns " + bc.getRuns() + "*" + ";");
									}
									else if (bc.getStatus().equalsIgnoreCase(CricketUtil.OUT)){
										print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBatsmanRuns " + bc.getRuns() + ";");
									}
									if (inn.getBattingTeamId() == match.getSetup().getHomeTeamId()) {
										print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgTeamLogo " + logo_path + match.getSetup().getHomeTeam().getTeamName4() + CricketUtil.PNG_EXTENSION + ";");
									} else {
										print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgTeamLogo " + logo_path + match.getSetup().getAwayTeam().getTeamName4() + CricketUtil.PNG_EXTENSION + ";");
									}
									if(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.TEST)){
										if(inn.getInningNumber() == 1 || inn.getInningNumber() == 2) {
											print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInnings " + "1st INNINGS" + ";");
										}else if(inn.getInningNumber() == 3 || inn.getInningNumber() == 4) {
											print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInnings " + "2nd INNINGS" + ";");
										}
										//print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateValue " + bc.getBalls() + ";");
									}else {
										print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInnings " + "" + ";");
									}
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateValue " + bc.getBalls() + ";");
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateValue1 " + Count[0] + ";");
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateValue2 " + bc.getStrikeRate() + ";");
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateValue3 " + bc.getFours() + ";");
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateValue4 " + bc.getSixes() + ";");
								}
							}
							break;
						}
					}
				}
				print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW ON;");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*CONTAINER SET ACTIVE 0;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In STOP;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out STOP;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 196.0;");
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
	public void populateBatsmanstatsBoth(PrintWriter print_writer,String viz_scene, int whichInning, String statsType, int playerId, MatchAllData match, String session_selected_broadcaster) throws InterruptedException
	{
		switch (session_selected_broadcaster.toUpperCase()) {
		case "FRUIT":
			if (match == null) {
				this.status = "ERROR: Match is null";
			} else if (match.getMatch().getInning() == null) {
				this.status = "ERROR: PlayerStats's inning is null";
			} else {
				int row = 0 ; 
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgTLogo " + logo_path + "TLogo" + 
						CricketUtil.PNG_EXTENSION + ";");
				for(Inning inn : match.getMatch().getInning()) {
					//for (int i = 1; i <= whichInning; i++) {
					//if (inn.getInningNumber() == whichInning) {
						for (BattingCard bc : inn.getBattingCard()) {
							if(bc.getPlayerId()==playerId) {
								row = row + 1 ;
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgTeamLogo " + logo_path + inn.getBatting_team().getTeamName4() + CricketUtil.PNG_EXTENSION  + ";");
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerName " + bc.getPlayer().getFull_name() + ";");
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateHead " + "RUNS" + ";");
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateHead1 " + "BALLS" + ";");
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateValueA" + row + " " + bc.getRuns() + ";");
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateValueB" + row + " " + bc.getBalls() + ";");
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateValueC" + row + " " + bc.getStrikeRate() + ";");
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateValueD" + row + " " + bc.getFours() + ";");
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateValueE" + row + " " + bc.getSixes() + ";");
							}
						}
					//}
				}
				print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW ON;");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*CONTAINER SET ACTIVE 0;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In STOP;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out STOP;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 196.0;");
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
	public void populateBowlerstats(PrintWriter print_writer,String viz_scene, int whichInning, String statsType, int playerId,List<Team> team, MatchAllData match, String session_selected_broadcaster) throws InterruptedException
	{
		switch (session_selected_broadcaster.toUpperCase()) {
		case "FRUIT":
			if (match == null) {
				this.status = "ERROR: Match is null";
			} else if (match.getMatch().getInning() == null) {
				this.status = "ERROR: PlayerStats's inning is null";
			} else {
				int total_inn = 0;
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgTLogo " + logo_path + "TLogo" + 
						CricketUtil.PNG_EXTENSION + ";");
				for(Inning inn : match.getMatch().getInning()) {
					if(inn.getInningStatus() != null) {
						total_inn = total_inn + 1;
					}
				}
				
				if(total_inn > 0 && whichInning > total_inn) {
					whichInning = total_inn;
				}
				for(Inning inn : match.getMatch().getInning()) {
					if (inn.getInningNumber() == whichInning) {
						switch(statsType.toUpperCase()) {
						case CricketUtil.BOWLER:
							for (BowlingCard boc : inn.getBowlingCard()) {
								if(boc.getPlayerId()==playerId) {
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgTeamLogo " + logo_path + team.get(boc.getPlayer().
											getTeamId() - 1).getTeamName4() + CricketUtil.PNG_EXTENSION + ";");
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerName " + boc.getPlayer().getFull_name() + ";");
									if(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.TEST)){
										if(inn.getInningNumber() == 1 || inn.getInningNumber() == 2) {
											print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInnings " + "1st INNINGS" + ";");
										}else if(inn.getInningNumber() == 3 || inn.getInningNumber() == 4) {
											print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInnings " + "2nd INNINGS" + ";");
										}
										//print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateValue " + bc.getBalls() + ";");
									}else {
										print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInnings " + "" + ";");
									}
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateHead " + "OVERS" + ";");
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateValue " + CricketFunctions.OverBalls(boc.getOvers(), boc.getBalls()) + ";");
									if(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.ODI)){
										print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateHead1 " + "MAIDENS" + ";");
										print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateValue1 " + boc.getMaidens() + ";");
									}else {
										print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateHead1 " + "DOTS" + ";");
										print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateValue1 " + boc.getDots() + ";");
									}
									
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateHead2 " + "RUNS" + ";");
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateValue2 " + boc.getRuns() + ";");
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateHead3 " + "WICKETS" + ";");
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateValue3 " + boc.getWickets() + ";");
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateHead4 " + "ECON" + ";");
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateValue4 " + boc.getEconomyRate() + ";");
								}
							}
							break;
						}
					}
				}
				print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW ON;");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*CONTAINER SET ACTIVE 0;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In STOP;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out STOP;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 196.0;");
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
	
	public void populateNameSuper(PrintWriter print_writer,String viz_scene, NameSuper ns ,MatchAllData match, String session_selected_broadcaster) throws InterruptedException
	{
		switch (session_selected_broadcaster.toUpperCase()) {
		case "FRUIT":
			if (match == null) {
				this.status = "ERROR: Match is null";
			} else {
				if(ns.getSponsor() == null) {
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgTeamLogo " + logo_path + "TLogo" + CricketUtil.PNG_EXTENSION + ";");
				}else {
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgTeamLogo " + logo_path + ns.getSponsor() + CricketUtil.PNG_EXTENSION + ";");
				}
				
				if(ns.getFirstname() == null) {
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFirstName " + ns.getSurname() +";");
				}
				else if(ns.getSurname() == null) {
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFirstName " + ns.getFirstname() +";");
				}
				else {
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFirstName " + ns.getFirstname() + " " 
							+ ns.getSurname() +";");
				}
				
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tDetails " + ns.getSubLine() + ";");
				
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
				
				this.status = CricketUtil.SUCCESSFUL;	
			}
			break;
		
		}
	}	
	public void populateNameSuperPlayer(PrintWriter print_writer,String viz_scene, int TeamId, String captainWicketKeeper, int playerId, MatchAllData match, String session_selected_broadcaster) throws InterruptedException
	{
		switch (session_selected_broadcaster.toUpperCase()) {
		case "FRUIT":
			if (match == null) {
				this.status = "ERROR: Match is null";
			} else {
				String Home_or_Away="";
				if(TeamId == match.getSetup().getHomeTeamId()) {
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgTeamLogo " + logo_path + match.getSetup().getHomeTeam().getTeamName4() + 
							CricketUtil.PNG_EXTENSION + ";");
					Home_or_Away = match.getSetup().getHomeTeam().getTeamName1().toUpperCase();
					for(Player hs : match.getSetup().getHomeSquad()) {
						if(playerId == hs.getPlayerId()) {
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFirstName " + hs.getFull_name() +";");

						}
					}
				}
				else {
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgTeamLogo " + logo_path + match.getSetup().getAwayTeam().getTeamName4() + 
							CricketUtil.PNG_EXTENSION + ";");
					Home_or_Away = match.getSetup().getAwayTeam().getTeamName1().toUpperCase();
					for(Player as : match.getSetup().getAwaySquad()) {
						if(playerId == as.getPlayerId()) {
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFirstName " + as.getFull_name() +";");
						}
					}
				}
				
				switch(captainWicketKeeper.toUpperCase())
				{
				case CricketUtil.CAPTAIN:
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tDetails " + captainWicketKeeper.toUpperCase() + ", " + Home_or_Away + ";");
					break;
				case "PLAYER OF THE MATCH":
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tDetails " + captainWicketKeeper.toUpperCase() + ";");
					break;
				case CricketUtil.WICKET_KEEPER:
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tDetails " + "WICKET KEEPER" + ", " + Home_or_Away + ";");
					break;
				case CricketUtil.PLAYER:
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tDetails " + Home_or_Away + ";");
					break;
				case "CAPTAIN-WICKETKEEPER":
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tDetails " + "CAPTAIN & WICKET KEEPER" + ", " + Home_or_Away + ";");
					break;
				}

				print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW ON;");
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
				
				this.status = CricketUtil.SUCCESSFUL;	
			}
			break;
		
		}
	}	
	public void populatePlayerProfile(PrintWriter print_writer,String viz_scene, int playerId,String Profile,String TypeofProfile,Statistics stats, MatchAllData match, String session_selected_broadcaster) throws InterruptedException {
		
		switch (session_selected_broadcaster.toUpperCase()) {
			
		case "FRUIT":
			if (match == null) {
				this.status = "ERROR: Match is null";
			} else {
			double strike_rate = 0 , economy_rate=0;
			Player plyr = getPlayerFromMatchData(stats.getPlayer_id(), match);
			
			if(Profile.equalsIgnoreCase(CricketUtil.ODI)) {
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tCareerHead " + Profile + " CAREER" + ";");
			}else if(Profile.equalsIgnoreCase(CricketUtil.DT20)){
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tCareerHead " + "T20 CAREER" + ";");
			}else if(Profile.equalsIgnoreCase(CricketUtil.IT20)){
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tCareerHead " + "T20I CAREER" + ";");
			}else {
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tCareerHead " + "LIST A CAREER" + ";");
			}
			
			print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSubHeader " + "" + ";");
			print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeamName " + "" + ";");
			
			if(plyr.getTeamId() == match.getSetup().getHomeTeamId()) {
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgTeamLogo " + logo_path + match.getSetup().getHomeTeam().getTeamName4() 
						+ CricketUtil.PNG_EXTENSION + ";");
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgPlayerImage01 " + photo_path + match.getSetup().getHomeTeam().getTeamName4()
						+ CricketUtil.DOUBLE_BACKSLASH + plyr.getPhoto() + CricketUtil.PNG_EXTENSION + ";");
				if(plyr.getSurname() != null) {
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFirstName " + plyr.getFirstname() + ";");
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tLastName " + plyr.getSurname() + ";");
				}else {
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFirstName " + "" + ";");
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tLastName " + plyr.getFirstname() + ";");
				}
			}
			else {
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgTeamLogo " + logo_path +  match.getSetup().getAwayTeam().getTeamName4() 
						+ CricketUtil.PNG_EXTENSION + ";");
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgPlayerImage01 " + photo_path + match.getSetup().getAwayTeam().getTeamName4()
						+ CricketUtil.DOUBLE_BACKSLASH + plyr.getPhoto() + CricketUtil.PNG_EXTENSION + ";");
				if(plyr.getSurname() != null) {
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFirstName " + plyr.getFirstname() + ";");
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tLastName " + plyr.getSurname() + ";");
				}else {
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFirstName " + "" + ";");
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tLastName " + plyr.getFirstname() + ";");
				}
			}
			
			print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tAge "+ " " + ";");
			
			switch(TypeofProfile.toUpperCase()) {
			case CricketUtil.BATSMAN:
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerRole01 " + CricketFunctions.playerStyle(TypeofProfile.toUpperCase(), plyr.getBattingStyle())+ ";");
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStatValue01 " + stats.getMatches()+ ";");
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStatValue02 " + stats.getRuns()+ ";");
				
				strike_rate = stats.getRuns() * 100;
				strike_rate = strike_rate/stats.getBalls_faced();
				DecimalFormat df = new DecimalFormat("0.0");
				
				//print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFoursHead " + "S/R" + ";");
				if(stats.getRuns()== 0 && stats.getBalls_faced() == 0) {
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFoursValue " + "-" +";");
				}else {
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFoursValue " + df.format(strike_rate) +";");
				}
				
				break;
			case CricketUtil.BOWLER:
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerRole01 "+ CricketFunctions.playerStyle(TypeofProfile.toUpperCase(), plyr.getBowlingStyle())+ ";");
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStatValue01 " + stats.getMatches()+ ";");
				
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStatHead02 " + "WICKETS" + ";");
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStatValue02 " + stats.getWickets() + ";");
				
				economy_rate = stats.getRuns_conceded() / stats.getBalls_bowled();
				economy_rate = economy_rate * 6;
				DecimalFormat df_b = new DecimalFormat("0.00");
				
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFoursHead "+"ECONOMY"+";");
				if(stats.getRuns_conceded() == 0 && stats.getBalls_bowled() == 0) {
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFoursValue "+ "-" +";");
				}else {
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFoursValue "+ df_b.format(economy_rate) +";");
				}
				
				break;
			}
			//print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tEventName " + stats.getStats_type().getStats_short_name() + ";");
			
			print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW ON;");
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*CONTAINER SET ACTIVE 0;");
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In STOP;");
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out STOP;");
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 71.0;");
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
	public void populateLTPlayerProfile(PrintWriter print_writer,String viz_scene,String Profile,String TypeofProfile,Statistics stats, MatchAllData match, String session_selected_broadcaster) throws InterruptedException 
	{
		switch (session_selected_broadcaster.toUpperCase()) {
		case "FRUIT":
			if (match == null) {
				this.status = "ERROR: Match is null";
			} else {
			double strike_rate=0, average = 0, economy_rate = 0;
			Player plyr = getPlayerFromMatchData(stats.getPlayer_id(), match);
			if(Profile.equalsIgnoreCase(CricketUtil.ODI)) {
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSubHeader " + Profile + " CAREER" + ";");
			}else if(Profile.equalsIgnoreCase(CricketUtil.DT20)){
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSubHeader " + "T20 CAREER" + ";");
			}else if(Profile.equalsIgnoreCase(CricketUtil.IT20)){
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSubHeader " + "T20I CAREER" + ";");
			}else {
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSubHeader " + "LIST A CAREER" + ";");
			}
			
			if(plyr.getTeamId() == match.getSetup().getHomeTeamId()) {
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgTeamLogo " + logo_path + match.getSetup().getHomeTeam().getTeamName4().toUpperCase() + CricketUtil.PNG_EXTENSION + ";");
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerName " + plyr.getFull_name().toUpperCase() + ";");
			}
			else {
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgTeamLogo " + logo_path + match.getSetup().getAwayTeam().getTeamName4().toUpperCase() + CricketUtil.PNG_EXTENSION + ";");
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerName " + plyr.getFull_name().toUpperCase() + ";");
			}
			switch(TypeofProfile.toUpperCase()) {
			case CricketUtil.BATSMAN:
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateValue " + stats.getMatches()+ ";");
				
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateHead1 " + "RUNS" +";");
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateValue1 " + stats.getRuns() + ";");
				
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateHead2 " + "S/R" + ";");
				if(stats.getBalls_faced() == 0 || stats.getRuns()== 0) {
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateValue2 " + "-" +";");
				}else {
					strike_rate = stats.getRuns() * 100;
					strike_rate = strike_rate/stats.getBalls_faced();
					DecimalFormat df = new DecimalFormat("0.0");
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateValue2 " + df.format(strike_rate)+";");
				}
				
				if(stats.getHundreds() == null &&  stats.getFifties() == null) {
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateHead3 " + "50s/100s" + ";");
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateValue3 " + "0/0"+";");
				}else {
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateHead3 " + "50s/100s" + ";");
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateValue3 " + stats.getFifties()+"/"+stats.getHundreds()+";");
				}
				
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateHead4 " + "BEST" +";");
				if(stats.getBest_score() == null) {
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateValue4 " + "-" + ";");
				}else {
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateValue4 " + stats.getBest_score() + ";");
				}
				
				break;
			case CricketUtil.BOWLER:
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateValue "+stats.getMatches()+ ";");
				
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateHead1 "+"WICKETS"+";");
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateValue1 "+stats.getWickets() + ";");
				

				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateHead2 "+ "AVERAGE"+";");
				if(stats.getRuns_conceded() == 0 || stats.getWickets() == 0) {
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateValue2 "+ "-" +";");
				}else {
					average = stats.getRuns_conceded()/stats.getWickets();
					DecimalFormat df_bo = new DecimalFormat("0.00");
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateValue2 "+ df_bo.format(average) +";");
				}
				
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateHead3 "+"ECON."+";");
				if(stats.getRuns_conceded() == 0 && stats.getBalls_bowled() == 0) {
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateValue3 "+ "-" +";");
				}else {
					economy_rate = stats.getRuns_conceded() / stats.getBalls_bowled();
					economy_rate = economy_rate * 6;
					DecimalFormat df_b = new DecimalFormat("0.00");
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateValue3 "+ df_b.format(economy_rate) +";");
				}
				
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateHead4 " + "BEST" +";");
				if(stats.getBest_figures() == null) {
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateValue4 " + "-" + ";");
				}else {
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateValue4 " + stats.getBest_figures() + ";");
				}
				
				break;
			}
			
			print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW ON;");
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

			this.status = CricketUtil.SUCCESSFUL;

			}
			break;
		}
		
	}
	public Player getPlayerFromMatchData(int plyr_id, MatchAllData match)
	{
		for(Player plyr : match.getSetup().getHomeSquad()) {
			if(plyr_id == plyr.getPlayerId()) { 
				return plyr;
			}
		}
		for(Player plyr : match.getSetup().getAwaySquad()) {
			if(plyr_id == plyr.getPlayerId()) { 
				return plyr;
			}
		}
		for(Player plyr : match.getSetup().getHomeOtherSquad()) {
			if(plyr_id == plyr.getPlayerId()) { 
				return plyr;
			}
		}
		for(Player plyr : match.getSetup().getAwayOtherSquad()) {
			if(plyr_id == plyr.getPlayerId()) { 
				return plyr;
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
	public Infobar populateInfobar(Infobar infobar,PrintWriter print_writer,String viz_scene, MatchAllData match, String session_selected_broadcaster) throws InterruptedException 
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
				infobar = populateInfobarBottomLeft(infobar,false, print_writer, match, session_selected_broadcaster);
				infobar = populateInfobarBottomRight(infobar,false, print_writer, match, session_selected_broadcaster);
				infobar = populateInfobarTopLeft(infobar,false, print_writer, match, session_selected_broadcaster);

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
				if (inn.getIsCurrentInning().toUpperCase().equalsIgnoreCase(CricketUtil.YES)) {
					if(is_this_updating == false) {
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Start*FUNCTION*TAG_CONTROL SET lgHomeTeamLogo " + logo_path + inn.getBatting_team().getTeamName4() + CricketUtil.PNG_EXTENSION + ";");
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Start*FUNCTION*TAG_CONTROL SET lgAwayTeamLogo " + logo_path + inn.getBowling_team().getTeamName4() + CricketUtil.PNG_EXTENSION + ";");
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Start*FUNCTION*TAG_CONTROL SET lgTLogo " + logo_path + "TLogo" + CricketUtil.PNG_EXTENSION + ";");

						print_writer.println("LAYER1*EVEREST*STAGE*DIRECTOR*Sponsor_In SHOW 0.0 ;");
						//processAnimation(print_writer, "Sponsor_In", "START", session_selected_broadcaster,1);
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET lgHomeTeamLogo " + logo_path + inn.getBatting_team().getTeamName4() + CricketUtil.PNG_EXTENSION + ";");
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET lgAwayTeamLogo " + logo_path + inn.getBowling_team().getTeamName4() + CricketUtil.PNG_EXTENSION + ";");
					}
					
					
					if(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.TEST)) {
						if(match.getSetup().getFollowOn().equalsIgnoreCase(CricketUtil.YES)) {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vPowerPlay_DL " + "1" + ";");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tPowerPlay " + "f/o" + ";");
						}else {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vPowerPlay_DL " + "0" + ";");
						}
				    }else {
				    	if(!match.getSetup().getTargetOvers().isEmpty() && Double.valueOf(match.getSetup().getTargetOvers()) == 1) {
					    	print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vPowerPlay_DL " + "0" + ";");
					    }
					    else {
					    	if(CricketFunctions.processPowerPlay(CricketUtil.MINI,match).isEmpty()) {
					    		print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vPowerPlay_DL " + "0" + ";");
					    	}else {
					    		if(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.TEST)) {
					    			print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vPowerPlay_DL " + "0" + ";");
					    		}
					    		else if(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.ODI) || match.getSetup().getMatchType().equalsIgnoreCase("OD")) {
					    			print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vPowerPlay_DL " + "1" + ";");
									print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tPowerPlay " + 
											CricketFunctions.processPowerPlay(CricketUtil.MINI,match) + ";");
					    		}else {
					    			print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vPowerPlay_DL " + "1" + ";");
									print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tPowerPlay " + "P" + ";");
					    		}
				    			
					    	}
					    }
				    }
					
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tBatTeamName " + inn.getBatting_team().getTeamName4().toUpperCase() + ";");
					if(inn.getTotalWickets() >= 10) {
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tTeamScore " + inn.getTotalRuns() + ";");
					}
					else{
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tTeamScore " + inn.getTotalRuns() + "-" + inn.getTotalWickets() + ";");
					}
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tTeamOvers " + CricketFunctions.OverBalls(inn.getTotalOvers(), inn.getTotalBalls())+ ";");
				}
			}
			break;
		}
		return infobar;
	}
	public Infobar populateInfobarTopLeft(Infobar infobar, boolean is_this_updating, PrintWriter print_writer,MatchAllData match, String broadcaster)
	{ 
		List<BattingCard> current_batsmen = new ArrayList<BattingCard>();
		switch (infobar.getMiddle_section().toUpperCase()) {
		case CricketUtil.BATSMAN:
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
					}
					
					populateCurrentBatsmen(infobar,print_writer, match, broadcaster,current_batsmen);
					
					if(current_batsmen != null && current_batsmen.size() >= 1) {
						infobar.setLast_batsmen(current_batsmen);
					}
				}
			}
			break;
		}
		return infobar;
	}
	public Infobar populateCurrentBatsmen(Infobar infobar, PrintWriter print_writer, MatchAllData match, String broadcaster,List<BattingCard> current_batsmen)
	{
		for(Inning inn : match.getMatch().getInning()) {
			
			if (inn.getIsCurrentInning().toUpperCase().equalsIgnoreCase(CricketUtil.YES)) {
				if(current_batsmen != null && current_batsmen.size() >= 1) {
					if(current_batsmen.get(0).getPlayerId() != current_batsmen.get(0).getPlayerId()) {
						processAnimation(print_writer, "Batsman1ChangeOut", "START", session_selected_broadcaster,1);
						//TimeUnit.SECONDS.sleep(1);
					}
					if(current_batsmen.get(1).getPlayerId() != current_batsmen.get(1).getPlayerId()) {
						processAnimation(print_writer, "Batsman2ChangeOut", "START", session_selected_broadcaster,1);
						//TimeUnit.SECONDS.sleep(1);
					}
				}
				
				if(current_batsmen != null && current_batsmen.size() >= 2) {
					
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tBatsmanName1 " + current_batsmen.get(0).getPlayer().getTicker_name() + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tBatsmanScore1 " + current_batsmen.get(0).getRuns() + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tBatsmanBall1 "+ current_batsmen.get(0).getBalls() + ";");
					
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tBatsmanName2 " + current_batsmen.get(1).getPlayer().getTicker_name() + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tBatsmanScore2 " + current_batsmen.get(1).getRuns() + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tBatsmanBall2 "+ current_batsmen.get(1).getBalls() + ";");
					
					if(current_batsmen.get(0).getStatus().toUpperCase().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
						if(current_batsmen.get(0).getOnStrike().equalsIgnoreCase(CricketUtil.YES)) {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vOnStrike" + " " + "0" + ";");
							processAnimation(print_writer, "Batsman1DeHighlight", "SHOW 0.0", session_selected_broadcaster,1);
						}
					}
					if(current_batsmen.get(1).getStatus().toUpperCase().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
						if(current_batsmen.get(1).getOnStrike().equalsIgnoreCase(CricketUtil.YES)) {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET vOnStrike" + " " + "1" + ";");
							processAnimation(print_writer, "Batsman2DeHighlight", "SHOW 0.0", session_selected_broadcaster,1);
						}	
					}
					if(current_batsmen.get(0).getStatus().equalsIgnoreCase(CricketUtil.OUT)) {
						processAnimation(print_writer, "Batsman1DeHighlight", "SHOW 250.0", session_selected_broadcaster,1);
					} else if(current_batsmen.get(0).getStatus().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
						processAnimation(print_writer, "Batsman1DeHighlight", "SHOW 0.0", session_selected_broadcaster,1);
					}
					if(current_batsmen.get(1).getStatus().equalsIgnoreCase(CricketUtil.OUT)) {
						processAnimation(print_writer, "Batsman2DeHighlight", "SHOW 250.0", session_selected_broadcaster,1);
					} else if(current_batsmen.get(1).getStatus().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
						processAnimation(print_writer, "Batsman2DeHighlight", "SHOW 0.0", session_selected_broadcaster,1);
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
		
		switch(infobar.getTop_right_section().toUpperCase()) {
		case CricketUtil.BOWLER:
			//print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$AllSection$Section1_2$BowlerbaseGrp$TopLIne*ACTIVE SET " + "0" + "\0");
			for(Inning inn : match.getMatch().getInning()) {
				if (inn.getIsCurrentInning().toUpperCase().equalsIgnoreCase(CricketUtil.YES)) {
					for(BowlingCard boc : inn.getBowlingCard()) {
						if(boc.getStatus().toUpperCase().equalsIgnoreCase("CURRENTBOWLER") 
								|| boc.getStatus().toUpperCase().equalsIgnoreCase("LASTBOWLER")) {
							if(infobar.getLast_bowler() == null || infobar.getLast_bowler().getPlayerId() != boc.getPlayerId()) {
								processAnimation(print_writer, "BowlerChangeOut", "START", session_selected_broadcaster,1);
								TimeUnit.SECONDS.sleep(1);
							}
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tBowlerName " + boc.getPlayer().getTicker_name() + ";");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tBowlerFigure " + boc.getWickets() + "-" + boc.getRuns() + ";");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET tBowlerOvers " + CricketFunctions.OverBalls(boc.getOvers(), boc.getBalls()) + ";");
							if(infobar.getLast_bowler() == null || infobar.getLast_bowler().getPlayerId() != boc.getPlayerId()) {
								processAnimation(print_writer, "BowlerChangeIn", "START", session_selected_broadcaster,1);
							}
							infobar.setLast_bowler(boc);
							infobar.setLast_bottom_right_top_section(CricketUtil.BOWLER);
						}
					}
				}
			}
			break;	
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
	public void populateThisSession(PrintWriter print_writer,String viz_sence_path, MatchAllData match,String session_selected_broadcaster) throws InterruptedException {
		double dayovers=0;
		DecimalFormat df = new DecimalFormat("0.00");
		if(match.getMatch().getDaysSessions().get(match.getMatch().getDaysSessions().size()-1).getTotalBalls() % 6 == 0) {
			dayovers = match.getMatch().getDaysSessions().get(match.getMatch().getDaysSessions().size()-1).getTotalBalls() / 6 ;
		}else {
			dayovers = Double.valueOf(String.valueOf(match.getMatch().getDaysSessions().get(match.getMatch().getDaysSessions().size()-1).getTotalBalls()/6) + 
					"." + String.valueOf(match.getMatch().getDaysSessions().get(match.getMatch().getDaysSessions().size()-1).getTotalBalls()%6)); 
		}
		print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgTeamLogo " + logo_path + "TLogo" + CricketUtil.PNG_EXTENSION + ";");
		print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerName " + "THIS SESSION" + ";");
		print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInnings " + " " + ";");
		print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateHead " + "RUNS" + ";");
		print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateValue " + match.getMatch().getDaysSessions().get(match.getMatch().getDaysSessions().size()-1).getTotalRuns() + ";");
		print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateHead1 " + "OVERS" + ";");
		print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateValue1 " + dayovers + ";");
		print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateHead2 " + "WICKETS" + ";");
		print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateValue2 " + match.getMatch().getDaysSessions().get(match.getMatch().getDaysSessions().size()-1).getTotalWickets() + ";");
		print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateHead3 " + "RUN RATE" + ";");
		print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateValue3 " + df.format(match.getMatch().getDaysSessions().get(match.getMatch().getDaysSessions().size()-1).getTotalRuns()/dayovers) + ";");
		print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateHead4 " + "" + ";");
		print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateValue4 " + "" + ";");
		
		print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW ON;");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*CONTAINER SET ACTIVE 0;");
		print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In STOP;");
		print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out STOP;");
		print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 196.0;");
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
	public void populateSession(PrintWriter print_writer,String viz_sence_path,int day,int session, MatchAllData match,String session_selected_broadcaster) throws InterruptedException {
		double dayovers=0;
		DecimalFormat df = new DecimalFormat("0.00");
		for(int i =0; i <= match.getMatch().getDaysSessions().size() -1; i++ ) {
			if(match.getMatch().getDaysSessions().get(i).getTotalBalls() % 6 == 0) {
				dayovers = match.getMatch().getDaysSessions().get(i).getTotalBalls() / 6 ;
			}else {
				dayovers = Double.valueOf(String.valueOf(match.getMatch().getDaysSessions().get(i).getTotalBalls()/6) + 
						"." + String.valueOf(match.getMatch().getDaysSessions().get(i).getTotalBalls()%6)); 
			}
			print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgTeamLogo " + logo_path + "TLogo" + CricketUtil.PNG_EXTENSION + ";");

			if(match.getMatch().getDaysSessions().get(i).getDayNumber() == day && match.getMatch().getDaysSessions().get(i).getSessionNumber() == session) {
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerName " + "DAY " + match.getMatch().getDaysSessions().
						get(i).getDayNumber() + " SESSION " + match.getMatch().getDaysSessions().get(i).getSessionNumber() + ";");
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInnings " + " " + ";");
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateHead " + "RUNS" + ";");
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateValue " + match.getMatch().getDaysSessions().get(i).getTotalRuns() + ";");
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateHead1 " + "OVERS" + ";");
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateValue1 " + dayovers + ";");
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateHead2 " + "WICKETS" + ";");
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateValue2 " + match.getMatch().getDaysSessions().get(i).getTotalWickets() + ";");
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateHead3 " + "RUN RATE" + ";");
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateValue3 " + df.format(match.getMatch().getDaysSessions().get(i).getTotalRuns()/dayovers) + ";");
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateHead4 " + "" + ";");
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateValue4 " + "" + ";");
			}
		}
		//match.getDaysSessions().get(0).get
		

		
		
		print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW ON;");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*CONTAINER SET ACTIVE 0;");
		print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In STOP;");
		print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out STOP;");
		print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 196.0;");
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
	public void populateSponsor(PrintWriter print_writer,String session_selected_broadcaster) {
		switch (session_selected_broadcaster.toUpperCase()) {
		case "FRUIT":
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET lgSponsorImage01 " + logo_path + "Sponsor\\" + "Dafa_News" + CricketUtil.PNG_EXTENSION + ";");
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*FUNCTION*TAG_CONTROL SET lgSponsorImage02 " + logo_path + "Sponsor\\" + "Dafa_News" + CricketUtil.PNG_EXTENSION + ";");
			break;
		}
	}
	public void populateMatchPromo(PrintWriter print_writer,String viz_sence_path, int match_number ,List<Team> team,List<VariousText> vt,List<Fixture> fix, List<Ground> ground,MatchAllData match, String session_selected_broadcaster) throws InterruptedException 
	{
		switch (session_selected_broadcaster.toUpperCase()) {
		case "FRUIT":
			if (match == null) {
				this.status = "ERROR: Match is null";
			} else if (match.getMatch().getInning() == null) {
				this.status = "ERROR: MatchId's inning is null";
			} else {
				
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHeader " + match.getSetup().getTournament() + ";");
				
				//print_writer.println("LAYER1*EVEREST*TREEVIEW*Subheader1*CONTAINER SET ACTIVE 0;");
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSubHeader " + fix.get(match_number - 1).getMatchfilename() + ";");
				
				for(Team TM : team) {
					if(fix.get(match_number - 1).getHometeamid() == TM.getTeamId()) {
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgHomeTeamLogo " + logo_path + TM.getTeamName4().toUpperCase() + CricketUtil.PNG_EXTENSION + ";");
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomeTeam01 " + TM.getTeamName1().toUpperCase() + ";");
					}
					if(fix.get(match_number - 1).getAwayteamid() == TM.getTeamId()) {
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgAwayTeamLogo " + logo_path + TM.getTeamName4().toUpperCase() + CricketUtil.PNG_EXTENSION + ";");
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tAwayTeam01 " + TM.getTeamName1().toUpperCase() + ";");
					}
				}
				
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DATE, +1);
				
				Calendar calto = Calendar.getInstance();
				calto.add(Calendar.DATE, 0);
				
				for(VariousText varioustext : vt) {
					if(varioustext.getVariousType().equalsIgnoreCase("FFMATCHPROMOFOOTER") && varioustext.getUseThis().toUpperCase().equalsIgnoreCase(CricketUtil.YES)) {
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFooter " + varioustext.getVariousText().toUpperCase() +";");
					}else {
						if(fix.get(match_number - 1).getDate().equalsIgnoreCase(new SimpleDateFormat("dd-MM-yyyy").format(cal.getTime()))) {
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInfo " + "TOMORROW " + "- FROM " + ground.get(Integer.valueOf(fix.get(match_number - 1).getVenue()) - 1).getFullname().toUpperCase() +";");
						}else if(fix.get(match_number - 1).getDate().equalsIgnoreCase(new SimpleDateFormat("dd-MM-yyyy").format(calto.getTime()))) {
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInfo " + "UP NEXT " + "- FROM "+ ground.get(Integer.valueOf(fix.get(match_number - 1).getVenue()) - 1).getFullname().toUpperCase() +";");
						}else {
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInfo " + fix.get(match_number - 1).getDate() + 
									" - FROM "+ ground.get(Integer.valueOf(fix.get(match_number - 1).getVenue()) - 1).getFullname().toUpperCase()  + ";");
						}
					} 
						
				}
				/*if(fix.get(match_number - 1).getDate().equalsIgnoreCase(new SimpleDateFormat("dd-MM-yyyy").format(cal.getTime()))) {
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFooter " + "TOMORROW " + "- LIVE FROM "+ match.getVenueName().toUpperCase() +";");
				}else {
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFooter " + "UP NEXT " + "- LIVE FROM "+ match.getVenueName().toUpperCase()  + ";");
				}*/
				
				
				print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW ON;");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*CONTAINER SET ACTIVE 0;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In STOP;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out STOP;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 71.0;");
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
	public void populateMatchId(PrintWriter print_writer,String viz_scene,List<VariousText> vt, MatchAllData match, String session_selected_broadcaster) throws InterruptedException
	{
		switch (session_selected_broadcaster.toUpperCase()) {
		case "FRUIT":
			if (match == null) {
				this.status = "ERROR: Match is null";
			} else if (match.getMatch().getInning() == null) {
				this.status = "ERROR: MatchId's inning is null";
			} else {
				
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHeader " + match.getSetup().getTournament() + ";");
				
				//print_writer.println("LAYER1*EVEREST*TREEVIEW*Subheader1*CONTAINER SET ACTIVE 0;");
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSubHeader " + match.getSetup().getMatchIdent() + ";");
				
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgHomeTeamLogo " + logo_path + match.getSetup().getHomeTeam().getTeamName4().toUpperCase() + CricketUtil.PNG_EXTENSION + ";");
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgAwayTeamLogo " + logo_path + match.getSetup().getAwayTeam().getTeamName4().toUpperCase() + CricketUtil.PNG_EXTENSION + ";");
				
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomeTeam01 " + match.getSetup().getHomeTeam().getTeamName1().toUpperCase() + ";");
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tAwayTeam01 " + match.getSetup().getAwayTeam().getTeamName1().toUpperCase() + ";");
				
				for(VariousText varioustext : vt) {
					if(varioustext.getVariousType().equalsIgnoreCase("MATCHIDFOOTER") && varioustext.getUseThis().toUpperCase().equalsIgnoreCase(CricketUtil.YES)) {
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInfo " + varioustext.getVariousText().toUpperCase() +";");
					}else {
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInfo " + "FROM " + match.getSetup().getVenueName().toUpperCase() + ";");
						
					} 	
				}
				
				print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW ON;");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*CONTAINER SET ACTIVE 0;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In STOP;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out STOP;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 71.0;");
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
	public void populateLTMatchId(PrintWriter print_writer,String viz_scene, MatchAllData match, String session_selected_broadcaster) throws InterruptedException
	{
		switch (session_selected_broadcaster.toUpperCase()) {
		case "FRUIT":
			if (match == null) {
				this.status = "ERROR: Match is null";
			} else if (match.getMatch().getInning() == null) {
				this.status = "ERROR: MatchId's inning is null";
			} else {
				
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgHomeTeamLogo " + logo_path + 
						match.getSetup().getHomeTeam().getTeamName4().toUpperCase() + CricketUtil.PNG_EXTENSION + ";");
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgAwayTeamLogo " + logo_path + 
						match.getSetup().getAwayTeam().getTeamName4().toUpperCase() + CricketUtil.PNG_EXTENSION + ";");
				
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomeTeamName " + match.getSetup().getHomeTeam().getTeamName1() + ";");
				
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tAwayTeamName " + match.getSetup().getAwayTeam().getTeamName1() + ";");
				
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInfo " + match.getSetup().getMatchIdent().toUpperCase() + " - " + 
						"FROM "+ match.getSetup().getVenueName().toUpperCase() + ";");
				
				print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW ON;");
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
				
				this.status = CricketUtil.SUCCESSFUL;	
			}
			break;
		}
		
	}
	
	public void populatePlayingXI(PrintWriter print_writer,String viz_scene, int TeamId,   MatchAllData match, String session_selected_broadcaster) 
	{
		switch (session_selected_broadcaster.toUpperCase()) {
		case "FRUIT":
			if (match == null) {
				this.status = "ERROR: Match is null";
			} else if (match.getMatch().getInning() == null) {
				this.status = "ERROR: PlayingXI's inning is null";
			} else {
				int row_id = 0;
				
				if(TeamId == match.getSetup().getHomeTeamId()) {
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeamName " + match.getSetup().getHomeTeam().getTeamName1().toUpperCase() + ";");
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSubHeader " + "v " + match.getSetup().getAwayTeam().getTeamName1().toUpperCase() + 
							" - " + match.getSetup().getMatchIdent()  + ";");
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgTeamLogo " + logo_path + match.getSetup().getHomeTeam().getTeamName4() + CricketUtil.PNG_EXTENSION + ";");
					for(Player hs : match.getSetup().getHomeSquad()) {
						row_id = row_id + 1;
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$Data$PlayerImage_Name_GRP$First_Five$Player" + row_id + "_GRP$group$Image1_GRP$Image*CONTAINER SET ACTIVE 1;");
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$Data$PlayerImage_Name_GRP$Second_Six$Player" + row_id + "_GRP$group$Image1_GRP$Image*CONTAINER SET ACTIVE 1;");
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$Data$PlayerImage_Name_GRP$First_Five$Player" + row_id + "_GRP$group$Player Role*CONTAINER SET ACTIVE 1;");
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$Data$PlayerImage_Name_GRP$First_Five$Player" + row_id + "_GRP$group$Player NAME01*CONTAINER SET ACTIVE 1;");
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgPlayerImage0"+ row_id + " " + photo_path + 
								match.getSetup().getHomeTeam().getTeamName4().toUpperCase() + CricketUtil.DOUBLE_BACKSLASH + hs.getPhoto() + CricketUtil.PNG_EXTENSION + ";");
						
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerName0"+ row_id + " " + hs.getTicker_name() + ";");
						
						if(hs.getCaptainWicketKeeper().equalsIgnoreCase(CricketUtil.CAPTAIN)) {
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgIcon"+ row_id +" "+ "1" + ";");
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerRole0"+ row_id + " " + "" + ";");
						}
						else if(hs.getCaptainWicketKeeper().equalsIgnoreCase(CricketUtil.WICKET_KEEPER)) {
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgIcon"+ row_id +" "+ "2" + ";");
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerRole0"+ row_id + " " + "" + ";");
						}
						else if(hs.getCaptainWicketKeeper().equalsIgnoreCase("CAPTAIN_WICKET_KEEPER")) {
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgIcon"+ row_id +" "+ "3" + ";");
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerRole0"+ row_id + " " + "" + ";");
						}
						else {
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgIcon"+ row_id +" "+ "0" + ";");
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerRole0"+ row_id + " " + "" + ";");
						}
						
					}
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Score_GRP*CONTAINER SET ACTIVE 0;");
				}
				
				else {
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeamName " + match.getSetup().getAwayTeam().getTeamName1().toUpperCase() + ";");
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSubHeader " + "v " + match.getSetup().getHomeTeam().getTeamName1().toUpperCase() + 
							" , " + match.getSetup().getMatchIdent() + ";");
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgTeamLogo " + logo_path + match.getSetup().getAwayTeam().getTeamName4() + CricketUtil.PNG_EXTENSION + ";");
					for(Player as : match.getSetup().getAwaySquad()) {
						row_id = row_id + 1;
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgPlayerImage0"+ row_id + " " + photo_path + 
								match.getSetup().getAwayTeam().getTeamName4().toUpperCase() + CricketUtil.DOUBLE_BACKSLASH + as.getPhoto() + CricketUtil.PNG_EXTENSION + ";");
						
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerName0"+ row_id + " " + as.getTicker_name() + ";");
						//System.out.println(as.getCaptainWicketKeeper());
						if(as.getCaptainWicketKeeper().equalsIgnoreCase(CricketUtil.CAPTAIN)) {
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgIcon"+ row_id +" "+ "1" + ";");
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerRole0"+ row_id + " " + "" + ";");
						}
						else if(as.getCaptainWicketKeeper().equalsIgnoreCase(CricketUtil.WICKET_KEEPER)) {
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgIcon"+ row_id +" "+ "2" + ";");
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerRole0"+ row_id + " " + "" + ";");
						}
						else if(as.getCaptainWicketKeeper().equalsIgnoreCase("CAPTAIN_WICKET_KEEPER")) {
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgIcon"+ row_id +" "+ "3" + ";");
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerRole0"+ row_id + " " + "" + ";");
						}
						else {
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgIcon"+ row_id +" "+ "0" + ";");
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerRole0"+ row_id + " " + "" + ";");
						}
					}
					
				}
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Score_GRP*CONTAINER SET ACTIVE 0;");
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tResult " + 
						CricketFunctions.generateTossResult(match, "", "", CricketUtil.FULL,CricketUtil.CHOSE).toUpperCase() + ";");
				}
				
			print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW ON;");
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*CONTAINER SET ACTIVE 0;");
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In STOP;");
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out STOP;");
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 107.0;");
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out SHOW 0.0;");
			print_writer.println("LAYER1*EVEREST*GLOBAL SNAPSHOT_PATH C:/Temp/Preview.png;");
			print_writer.println("LAYER1*EVEREST*GLOBAL SNAPSHOT 1920 1080;");
			//TimeUnit.SECONDS.sleep(1);
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out SHOW 0.0;");
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 0.0;");
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*CONTAINER SET ACTIVE 1;");
			print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW OFF;");
				
			this.status = CricketUtil.SUCCESSFUL;
			break;
		}
		
	}
	public void populateProjectedScore(PrintWriter print_writer,String viz_scene, MatchAllData match, String session_selected_broadcaster) throws InterruptedException
	{
		switch (session_selected_broadcaster.toUpperCase()) {
		case "FRUIT":
			if (match == null) {
				this.status = "ERROR: Match is null";
			} else if (match.getMatch().getInning() == null) {
				this.status = "ERROR: ProjectedScore's inning is null";
			} else {
				String[] proj_score_rate = new String[CricketFunctions.projectedScore(match).size()];
			    for (int i = 0; i < CricketFunctions.projectedScore(match).size(); i++) {
			    	proj_score_rate[i] = CricketFunctions.projectedScore(match).get(i);
		        }
				for(Inning inn : match.getMatch().getInning()) {
					if(inn.getInningNumber() == 1 & inn.getIsCurrentInning().equalsIgnoreCase("YES")) {
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgTeamLogo " + logo_path + inn.getBatting_team().getTeamName4() + CricketUtil.PNG_EXTENSION + ";");
						
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeamName " + inn.getBatting_team().getTeamName3().toUpperCase() + ";");
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateHead1 " + "@"+ proj_score_rate[0] +" (CRR)" + ";");
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateValue1 " + proj_score_rate[1] + ";");
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateHead2 " + "@" + proj_score_rate[2] +" RPO"+ ";");
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateValue2 " + proj_score_rate[3] + ";");
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateHead3 " + "@" + proj_score_rate[4] +" RPO" + ";");
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateValue3 " + proj_score_rate[5] + ";");
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
	public void populateTarget(PrintWriter print_writer,String viz_scene, MatchAllData match, String session_selected_broadcaster) throws InterruptedException
	{
		switch (session_selected_broadcaster.toUpperCase()) {
		case "FRUIT":
			if (match == null) {
				this.status = "ERROR: Match is null";
			} else if (match.getMatch().getInning() == null) {
				this.status = "ERROR: Target's inning is null";
			} else {
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgTeamLogo " + logo_path + "TLogo" + CricketUtil.PNG_EXTENSION + ";");
				
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHeader " +  "TARGET" +";");
					
				if(match.getSetup().getTargetType() == null || match.getSetup().getTargetType().trim().isEmpty()) {
					if(CricketFunctions.getTargetOvers(match) == "1") {
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tDetails " + match.getMatch().getInning().get(1).getBatting_team().getTeamName1().toUpperCase() + " NEED " + 
							CricketFunctions.getTargetRuns(match) + " RUNS" + " TO WIN FROM "+  Integer.valueOf(CricketFunctions.getTargetOvers(match)) * 6 + " BALLS"  +";");
					}else {
						if(Double.valueOf(CricketFunctions.getTargetOvers(match))*6 >= 100) {
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tDetails " + match.getMatch().getInning().get(1).getBatting_team().getTeamName1().toUpperCase() + " NEED " + 
								CricketFunctions.getTargetRuns(match) + " RUNS" + " TO WIN FROM "+  CricketFunctions.getTargetOvers(match) + " OVERS"  +";");
						}else {
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tDetails " + match.getMatch().getInning().get(1).getBatting_team().getTeamName1().toUpperCase() + " NEED " + 
								CricketFunctions.getTargetRuns(match) + " RUNS" + " TO WIN FROM "+ Integer.valueOf(CricketFunctions.getTargetOvers(match)) * 6 + " BALLS"  +";");
						}
					}
				}else {
					if(Double.valueOf(CricketFunctions.getTargetOvers(match))*6 >= 100) {
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tDetails " + match.getMatch().getInning().get(1).getBatting_team().getTeamName1().toUpperCase() + " NEED " + 
							CricketFunctions.getTargetRuns(match) + " RUNS" + " TO WIN FROM "+  CricketFunctions.getTargetOvers(match) + " OVERS" + " ("+ match.getSetup().getTargetType().toUpperCase() +")"  +";");
					}else {
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tDetails " + match.getMatch().getInning().get(1).getBatting_team().getTeamName1().toUpperCase() + " NEED " + 
							CricketFunctions.getTargetRuns(match) + " RUNS" + " TO WIN FROM "+  Integer.valueOf(CricketFunctions.getTargetOvers(match)) * 6 + " BALLS" + " ("+ match.getSetup().getTargetType().toUpperCase() +")" +";");
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
			TimeUnit.SECONDS.sleep(1);
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out SHOW 0.0;");
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 0.0;");
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*CONTAINER SET ACTIVE 1;");
			print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW OFF;");
			
			this.status = CricketUtil.SUCCESSFUL;
			
			break;
		}
		
	}
	public void populateFFTarget(PrintWriter print_writer,String viz_scene, MatchAllData match, String session_selected_broadcaster)throws InterruptedException 
	{
		switch (session_selected_broadcaster.toUpperCase()) {
		case "FRUIT":
			if (match == null) {
				this.status = "ERROR: Match is null";
			} else if (match.getMatch().getInning() == null) {
				this.status = "ERROR: Target's inning is null";
			} else {
				
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSubHeader " + "TARGET - " + match.getSetup().getMatchIdent().toUpperCase() + ";");
				
				for(Inning inn : match.getMatch().getInning()) {
					//if(inn.getInningNumber() == 2 && inn.getIsCurrentInning().equalsIgnoreCase("YES")) {
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHeader " + inn.getBatting_team().getTeamName1() + ";");
					
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgHomeTeamLogo " + logo_path + inn.getBatting_team().getTeamName4() + CricketUtil.PNG_EXTENSION + ";");
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgAwayTeamLogo " + logo_path + inn.getBowling_team().getTeamName4() + CricketUtil.PNG_EXTENSION + ";");
						
						if(match.getSetup().getTargetType() == null || match.getSetup().getTargetType().trim().isEmpty()) {
							if(CricketFunctions.getTargetOvers(match) == "1") {
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTargetRuns " + CricketFunctions.getTargetRuns(match) + ";");
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tRunsHead " + "RUNS" + ";");
								
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTargetBalls " + (Double.valueOf(CricketFunctions.getTargetOvers(match))*6) + ";");
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBallsHead " + "BALLS" + ";");
							}else {
								if(Double.valueOf(CricketFunctions.getTargetOvers(match))*6 >= 100) {
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTargetRuns " + CricketFunctions.getTargetRuns(match) + ";");
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tRunsHead " + "RUNS" + ";");
									
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTargetBalls " + match.getSetup().getMaxOvers() + ";");
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBallsHead " + "OVERS" + ";");
								}else {
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTargetRuns " + CricketFunctions.getTargetRuns(match) + ";");
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tRunsHead " + "RUNS" + ";");
									
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTargetBalls " + (Double.valueOf(CricketFunctions.getTargetOvers(match))*6) + ";");
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBallsHead " + "BALLS" + ";");
								}
							}
						}else {
							if(Double.valueOf(CricketFunctions.getTargetOvers(match))*6 >= 100) {
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTargetRuns " + CricketFunctions.getTargetRuns(match) + ";");
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tRunsHead " + "RUNS" + ";");
								
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTargetBalls " + Double.valueOf(CricketFunctions.getTargetOvers(match)) + ";");
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBallsHead " + "OVERS" + " (" + match.getSetup().getTargetType().toUpperCase() + ")" + ";");
							}else {
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTargetRuns " + CricketFunctions.getTargetRuns(match) + ";");
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tRunsHead " + "RUNS" + ";");
								
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTargetBalls " + Double.valueOf(CricketFunctions.getTargetOvers(match))*6 + ";");
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBallsHead " + "BALLS" + " ("+ match.getSetup().getTargetType().toUpperCase() +")" + ";");
							}
						}
						
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tCRRHead " + " " + ";");
						int requiredRuns = match.getMatch().getInning().get(0).getTotalRuns() + 1;
						
						if(match.getSetup().getTargetRuns() != 0) {
							requiredRuns = match.getSetup().getTargetRuns();
						}
						
						if(requiredRuns <= 0) {
							requiredRuns = 0;
						}
						
						int requiredBalls = 0;
						if(match.getSetup().getTargetOvers() != null && !match.getSetup().getTargetOvers().trim().isEmpty()) {
							if(match.getSetup().getTargetOvers().contains(".")) {
								requiredBalls = ((Integer.valueOf(match.getSetup().getTargetOvers().split(".")[0]) * 6) + Integer.valueOf(match.getSetup().getTargetOvers().split(".")[1]));
							} else {
								requiredBalls = ((Integer.valueOf(match.getSetup().getTargetOvers()) * 6));
							}
						}else {
							requiredBalls = ((match.getSetup().getMaxOvers()) * 6);
						}
						
						if(requiredBalls <= 0) {
							requiredBalls = 0;
						}
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tCRR " + "REQUIRED RUN RATE : " + CricketFunctions.generateRunRate
								(requiredRuns, 0, requiredBalls, 2) + ";");
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tRRRHead " + " " + ";");
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tRRR " + " " + ";");
					//}
				}
			}
			
			print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW ON;");
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*CONTAINER SET ACTIVE 0;");
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In STOP;");
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out STOP;");
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 197.0;");
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out SHOW 0.0;");
			print_writer.println("LAYER1*EVEREST*GLOBAL SNAPSHOT_PATH C:/Temp/Preview.png;");
			print_writer.println("LAYER1*EVEREST*GLOBAL SNAPSHOT 1920 1080;");
			TimeUnit.SECONDS.sleep(1);
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out SHOW 0.0;");
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 0.0;");
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*CONTAINER SET ACTIVE 1;");
			print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW OFF;");
			
			this.status = CricketUtil.SUCCESSFUL;
			
			break;
		}
	}
	public void populateFFEquation(PrintWriter print_writer,String viz_scene, MatchAllData match, String session_selected_broadcaster)throws InterruptedException 
	{
		switch (session_selected_broadcaster.toUpperCase()) {
		case "FRUIT":
			if (match == null) {
				this.status = "ERROR: Match is null";
			} else if (match.getMatch().getInning() == null) {
				this.status = "ERROR: Target's inning is null";
			} else {
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeamFirstName " + match.getSetup().getMatchIdent().toUpperCase() + ";");
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeamLastName " + match.getSetup().getMatchIdent().toUpperCase() + ";");
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgTeamLogo " + logo_path + "TLogo" + CricketUtil.PNG_EXTENSION + ";");
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgTLogo " + logo_path + "TLogo" + 
						CricketUtil.PNG_EXTENSION + ";");
				for(Inning inn : match.getMatch().getInning()) {
					if(inn.getInningNumber() == 2 && inn.getIsCurrentInning().equalsIgnoreCase("YES")) {
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBatTeamName " + inn.getBatting_team().getTeamName1().toUpperCase() + ";");
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBatSubHeader " + "v " + inn.getBowling_team().getTeamName1().toUpperCase() + ";");
						
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgAwayTeamLogo " + logo_path + inn.getBatting_team().getTeamName4() + CricketUtil.PNG_EXTENSION + ";");
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgHomeTeamLogo " + logo_path + inn.getBowling_team().getTeamName4() + CricketUtil.PNG_EXTENSION + ";");
						
						
						if(match.getSetup().getTargetType() == null || match.getSetup().getTargetType().trim().isEmpty()) {
							if(CricketFunctions.getTargetOvers(match) == "1") {
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTargetRuns " + CricketFunctions.getRequiredRuns(match) + ";");
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tRunsHead " + "RUN" + 
										CricketFunctions.Plural(CricketFunctions.getRequiredRuns(match)).toUpperCase() + ";");
								
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTargetBalls " + CricketFunctions.getRequiredBalls(match) + ";");
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBallsHead " + "BALL" + 
										CricketFunctions.Plural(CricketFunctions.getRequiredBalls(match)).toUpperCase() + ";");
							}else {
								if(CricketFunctions.getRequiredBalls(match) >= 100) {
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTargetRuns " + CricketFunctions.getRequiredRuns(match) + ";");
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tRunsHead " + "RUN" + 
											CricketFunctions.Plural(CricketFunctions.getRequiredRuns(match)).toUpperCase() + ";");
									
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTargetBalls " + CricketFunctions.OverBalls(0,CricketFunctions.getRequiredBalls(match)) + ";");
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBallsHead " + "OVERS" + ";");
								}else {
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTargetRuns " + CricketFunctions.getRequiredRuns(match) + ";");
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tRunsHead " + "RUN" + 
											CricketFunctions.Plural(CricketFunctions.getRequiredRuns(match)).toUpperCase() + ";");
									
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTargetBalls " + CricketFunctions.getRequiredBalls(match) + ";");
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBallsHead " + "BALL" + 
											CricketFunctions.Plural(CricketFunctions.getRequiredBalls(match)).toUpperCase() + ";");
								}
							}
						}else {
							if(CricketFunctions.getRequiredBalls(match) >= 100) {
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTargetRuns " + CricketFunctions.getRequiredRuns(match) + ";");
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tRunsHead " + "RUN" + 
										CricketFunctions.Plural(CricketFunctions.getRequiredRuns(match)).toUpperCase() + ";");
								
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTargetBalls " + Double.valueOf(CricketFunctions.getTargetOvers(match)) + ";");
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBallsHead " + CricketFunctions.OverBalls(0,CricketFunctions.getRequiredBalls(match)) + 
									" (" + match.getSetup().getTargetType().toUpperCase() + ")" + ";");
							}else {
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTargetRuns " + CricketFunctions.getRequiredRuns(match) + ";");
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tRunsHead " + "RUN" + 
										CricketFunctions.Plural(CricketFunctions.getRequiredRuns(match)).toUpperCase() + ";");
								
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTargetBalls " + CricketFunctions.getRequiredBalls(match) + ";");
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBallsHead " + "BALL" + 
										CricketFunctions.Plural(CricketFunctions.getRequiredBalls(match)).toUpperCase() + " ("+ match.getSetup().getTargetType().toUpperCase() +")" + ";");
							}
						}
						
						if(inn.getRunRate() == null) {
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tCRRHead " + " " + ";");
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tCRR " + "REQUIRED RUN RATE : " + CricketFunctions.generateRunRate
									(CricketFunctions.getRequiredRuns(match), 0, CricketFunctions.getRequiredBalls(match), 2) + ";");
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tRRRHead " + " " + ";");
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tRRR " + " " + ";");
						}else {
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tCRR " + inn.getRunRate() + ";");
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tRRR " + CricketFunctions.generateRunRate(CricketFunctions.getRequiredRuns(match), 0, 
									CricketFunctions.getRequiredBalls(match), 2) + ";");
						}
					}
				}
			}
			
			print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW ON;");
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*CONTAINER SET ACTIVE 0;");
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In STOP;");
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out STOP;");
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 197.0;");
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out SHOW 0.0;");
			print_writer.println("LAYER1*EVEREST*GLOBAL SNAPSHOT_PATH C:/Temp/Preview.png;");
			print_writer.println("LAYER1*EVEREST*GLOBAL SNAPSHOT 1920 1080;");
			TimeUnit.SECONDS.sleep(1);
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out SHOW 0.0;");
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 0.0;");
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*CONTAINER SET ACTIVE 1;");
			print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW OFF;");
			
			this.status = CricketUtil.SUCCESSFUL;
			
			break;
		}
	}
	public void populateBugTarget(PrintWriter print_writer,String viz_scene, MatchAllData match, String session_selected_broadcaster)
	{
		switch (session_selected_broadcaster.toUpperCase()) {
		case "DOAD_IN_HOUSE_VIZ":
			if (match == null) {
				this.status = "ERROR: Match is null";
			} else if (match.getMatch().getInning() == null) {
				this.status = "ERROR: Target's inning is null";
			} else {
				for(Inning inn : match.getMatch().getInning()) {
					if(inn.getInningNumber() == 2 && inn.getIsCurrentInning().equalsIgnoreCase("YES")) {
						print_writer.println("-1 RENDERER*TREE*$Main$All$DataAll$All_Name$Runs*GEOM*TEXT SET "+ CricketFunctions.getRequiredRuns(match) + " \0");
						print_writer.println("-1 RENDERER*TREE*$Main$All$DataAll$All_Name$Balls*GEOM*TEXT SET "+ CricketFunctions.getRequiredBalls(match) + " \0");
						//print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tNeedRun " + CricketFunctions.getRequiredRuns(match) + ";");
						//print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tNeedBall " + CricketFunctions.getRequiredBalls(match) + ";");
						
					}
				}
			}
			
			this.status = CricketUtil.SUCCESSFUL;
			
			break;
		}
		
	}
	public void populateTeamSummary(PrintWriter print_writer, String viz_scene, int whichInning, MatchAllData match, String session_selected_broadcaster) throws InterruptedException 
	{
		switch (session_selected_broadcaster.toUpperCase()) {
		case "FRUIT":
			if (match == null) {
				this.status = "ERROR: Match is null";
			} else if (match.getMatch().getInning() == null) {
				this.status = "ERROR: TeamSummary's inning is null";
			} else {
				
				
				for(Inning inn : match.getMatch().getInning()) {
					if (inn.getInningNumber() == whichInning) {
						
						String[] Count = CricketFunctions.getScoreTypeData("TEAM", match, whichInning, 0, ",", match.getEventFile().getEvents()).split(",");
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgTeamLogo " + logo_path + inn.getBatting_team().getTeamName4() + CricketUtil.PNG_EXTENSION + ";");
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgTLogo " + logo_path + "TLogo" + 
								CricketUtil.PNG_EXTENSION + ";");
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeamName " + inn.getBatting_team().getTeamName1().toUpperCase() + ";");
						if(inn.getTotalWickets() >= 10) {
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tScore " + inn.getTotalRuns() + ";");
						}
						else {
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tScore " + inn.getTotalRuns() + slashOrDash + inn.getTotalWickets() + ";");
							//print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tScore " + inn.getTotalRuns() + slashOrDash + inn.getTotalWickets() + ";");
						}
						
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateValue " + CricketFunctions.OverBalls(inn.getTotalOvers(), inn.getTotalBalls()) + ";");
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateValue1 " + Count[0] + ";");
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateValue2 " + Count[1] + ";");
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateValue3 " + Count[2] + ";");
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateValue4 " + Count[3] + ";");
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateValue5 " + inn.getTotalFours() + ";");
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateValue6 " + inn.getTotalSixes() + ";");
					}
				}
				
				print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW ON;");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*CONTAINER SET ACTIVE 0;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In STOP;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out STOP;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 196.0;");
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
	public void populateLtBattingSummary(PrintWriter print_writer, String viz_scene,int whichInning, int PlayerId, MatchAllData match, String session_selected_broadcaster) throws InterruptedException 
	{
		switch (session_selected_broadcaster.toUpperCase()) {
		case "FRUIT":
			if (match == null) {
				this.status = "ERROR: Match is null";
			} else if (match.getMatch().getInning() == null) {
				this.status = "ERROR: PlayerSummary's inning is null";
			} else {
				
				for(Inning inn : match.getMatch().getInning()) {
					for(BattingCard bc : inn.getBattingCard()) {
						if (inn.getInningNumber() == whichInning) {
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgTeamLogo " + logo_path + inn.getBatting_team().getTeamName4() + CricketUtil.PNG_EXTENSION + ";");
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgTLogo " + logo_path + "TLogo" + 
									CricketUtil.PNG_EXTENSION + ";");
							String[] Count = CricketFunctions.getScoreTypeData(CricketUtil.BATSMAN,match, whichInning, PlayerId,"-", match.getEventFile().getEvents()).split("-");
							
							if(PlayerId == bc.getPlayerId()) {
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeamName " + bc.getPlayer().getFull_name()+ ";");
								if(bc.getStatus().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tScore " + bc.getRuns() + "*" + ";");
								}
								else if (bc.getStatus().equalsIgnoreCase(CricketUtil.OUT)){
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tScore " + bc.getRuns() + ";");
								}
								
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateHead " + "BALLS" + ";");
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateValue " + bc.getBalls() + ";");
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateValue1 " + Count[0] + ";");
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateValue2 " + Count[1] + ";");
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateValue3 " + Count[2] + ";");
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateValue4 " + Count[3] + ";");
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateValue5 " + bc.getFours() + ";");
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateValue6 " + bc.getSixes() + ";");
							}
						}
					}
				}
				
				print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW ON;");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*CONTAINER SET ACTIVE 0;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In STOP;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out STOP;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 196.0;");
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
	public void populateLtBatsmanThisMatch(PrintWriter print_writer, String viz_scene,int whichInning, int PlayerId, MatchAllData match, String session_selected_broadcaster) 
	{
		switch (session_selected_broadcaster.toUpperCase()) {
			case "DOAD_IN_HOUSE_VIZ":
				if (match == null) {
					this.status = "ERROR: Match is null";
				} else if (match.getMatch().getInning() == null) {
					this.status = "ERROR: PlayerSummary's inning is null";
				} else {
					
					for(Inning inn : match.getMatch().getInning()) {
						for(BattingCard bc : inn.getBattingCard()) {
							if (inn.getInningNumber() == whichInning) {
								//print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgTeamLogo " + logo_path + inn.getBatting_team().getFullname() + CricketUtil.PNG_EXTENSION + ";");
								if(PlayerId == bc.getPlayerId()) {
									print_writer.println("-1 RENDERER*TREE*$Main$All$DataAll$All_Name$SubHead*GEOM*TEXT SET " + " " + "\0");								
									print_writer.println("-1 RENDERER*TREE*$Main$All$DataAll$All_Name$SubHead*GEOM*TEXT SET " + " " + "\0");								

									print_writer.println("-1 RENDERER*TREE*$Main$All$DataAll$All_Name$PlayerName*GEOM*TEXT SET " + bc.getPlayer().getFull_name() + "\0");								
									print_writer.println("-1 RENDERER*TREE*$Main$All$DataAll$All_Score$Score*GEOM*TEXT SET " + " " + "\0");								
									/*if (inn.getBattingTeamId() == match.getHomeTeamId()) {
										print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgTeamLogo " + logo_path + match.getHomeTeam().getFullname() + CricketUtil.PNG_EXTENSION + ";");
									} else {
										print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgTeamLogo " + logo_path + match.getAwayTeam().getFullname() + CricketUtil.PNG_EXTENSION + ";");
									}*/
									print_writer.println("-1 RENDERER*TREE*$Main$All$DataAll$ROW1$All_StatsVal$StatHead02*GEOM*TEXT SET " + "RUNS" + "\0");
									print_writer.println("-1 RENDERER*TREE*$Main$All$DataAll$ROW1$All_StatsVal$StatHead03*GEOM*TEXT SET " + bc.getRuns() + "\0");
									print_writer.println("-1 RENDERER*TREE*$Main$All$DataAll$ROW1$All_StatsVal$StatValues_GRP$StatValue1$StatValue1B*GEOM*TEXT SET " + "BALLS" + "\0");								
									print_writer.println("-1 RENDERER*TREE*$Main$All$DataAll$ROW1$All_StatsVal$StatValues_GRP$StatValue1$StatValue1c*GEOM*TEXT SET " + bc.getBalls() + "\0");
									print_writer.println("-1 RENDERER*TREE*$Main$All$DataAll$ROW1$All_StatsVal$StatValues_GRP$StatValue2$StatValue1B*GEOM*TEXT SET " + "S/R" + "\0");								
									print_writer.println("-1 RENDERER*TREE*$Main$All$DataAll$ROW1$All_StatsVal$StatValues_GRP$StatValue2$StatValue1B*GEOM*TEXT SET " + bc.getStrikeRate() + "\0");
									print_writer.println("-1 RENDERER*TREE*$Main$All$DataAll$ROW1$All_StatsVal$StatValues_GRP$StatValue3$StatValue1B*GEOM*TEXT SET " + "FOURS" + "\0");								
									print_writer.println("-1 RENDERER*TREE*$Main$All$DataAll$ROW1$All_StatsVal$StatValues_GRP$StatValue3$StatValue1B*GEOM*TEXT SET " + bc.getFours() + "\0");
									print_writer.println("-1 RENDERER*TREE*$Main$All$DataAll$ROW1$All_StatsVal$StatValues_GRP$StatValue4$StatValue1B*GEOM*TEXT SET " + "SIXES" + "\0");								
									print_writer.println("-1 RENDERER*TREE*$Main$All$DataAll$ROW1$All_StatsVal$StatValues_GRP$StatValue4$StatValue1B*GEOM*TEXT SET " + bc.getSixes() + "\0");
									print_writer.println("-1 RENDERER*TREE*$Main$All$DataAll$ROW1$All_StatsVal$StatValues_GRP$StatValue5$StatValue1B*GEOM*TEXT SET " + " " + "\0");
									print_writer.println("-1 RENDERER*TREE*$Main$All$DataAll$ROW1$All_StatsVal$StatValues_GRP$StatValue5$StatValue1B*GEOM*TEXT SET " + " " + "\0");
								}
							}
						}
					}
					this.status = CricketUtil.SUCCESSFUL;
				}
				break;
		}
		
	}
	public void populateLtBowlerThisMatch(PrintWriter print_writer, String viz_scene,int whichInning, int PlayerId, MatchAllData match, String session_selected_broadcaster) 
	{
		switch (session_selected_broadcaster.toUpperCase()) {
			case "DOAD_IN_HOUSE_VIZ":
				if (match == null) {
					this.status = "ERROR: Match is null";
				} else if (match.getMatch().getInning() == null) {
					this.status = "ERROR: PlayerSummary's inning is null";
				} else {
					
					for(Inning inn : match.getMatch().getInning()) {
						if (inn.getInningNumber() == whichInning) {
							for (BowlingCard boc : inn.getBowlingCard()) {
								if(boc.getPlayerId()==PlayerId) {
									/*if (inn.getBattingTeamId() == match.getHomeTeamId()) {
									print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgTeamLogo " + logo_path + match.getHomeTeam().getFullname() + CricketUtil.PNG_EXTENSION + ";");
								} else {
									print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgTeamLogo " + logo_path + match.getAwayTeam().getFullname() + CricketUtil.PNG_EXTENSION + ";");
								}*/
									print_writer.println("-1 RENDERER*TREE*$Main$All$DataAll$All_Name$PlayerName*GEOM*TEXT SET " + boc.getPlayer().getFull_name() + "\0");
									print_writer.println("-1 RENDERER*TREE*$Main$All$DataAll$All_Name$PlayerName*GEOM*TEXT SET " + boc.getPlayer().getFull_name() + "\0");
									print_writer.println("-1 RENDERER*TREE*$Main$All$DataAll$ROW1$All_StatsVal$StatHead_GRP$StatAll1$StatHead02*GEOM*TEXT SET " + boc.getOvers() + "\0");
									print_writer.println("-1 RENDERER*TREE*$Main$All$DataAll$ROW1$All_StatsVal$StatValues_GRP$StatValue1$StatValue1A*GEOM*TEXT SET " + "DOTS" + "\0");								
									print_writer.println("-1 RENDERER*TREE*$Main$All$DataAll$ROW1$All_StatsVal$StatValues_GRP$StatValue1$StatValue1B*GEOM*TEXT SET " + boc.getDots() + "\0");								
									print_writer.println("-1 RENDERER*TREE*$Main$All$DataAll$ROW1$All_StatsVal$StatValues_GRP$StatValue2$StatValue1B*GEOM*TEXT SET " + boc.getRuns() + "\0");								
									print_writer.println("-1 RENDERER*TREE*$Main$All$DataAll$ROW1$All_StatsVal$StatValues_GRP$StatValue3$StatValue1B*GEOM*TEXT SET " + boc.getWickets() + "\0");								
									print_writer.println("-1 RENDERER*TREE*$Main$All$DataAll$ROW1$All_StatsVal$StatValues_GRP$StatValue3$StatValue1B*GEOM*TEXT SET " + boc.getEconomyRate() + "\0");
								}
							}	
						}
					}
					this.status = CricketUtil.SUCCESSFUL;
				}
				break;
		}
		
	}
	public void populateLtBowlerSummary(PrintWriter print_writer, String viz_scene,int whichInning, int PlayerId, MatchAllData match, String session_selected_broadcaster) throws InterruptedException 
	{
		switch (session_selected_broadcaster.toUpperCase()) {
		case "FRUIT":
			if (match == null) {
				this.status = "ERROR: Match is null";
			} else if (match.getMatch().getInning() == null) {
				this.status = "ERROR: PlayerSummary's inning is null";
			} else {
				
				for(Inning inn : match.getMatch().getInning()) {
					if (inn.getInningNumber() == whichInning) {
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgTeamLogo " + logo_path + inn.getBowling_team().getTeamName4() + CricketUtil.PNG_EXTENSION + ";");
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgTLogo " + logo_path + "TLogo" + 
								CricketUtil.PNG_EXTENSION + ";");
						for(BowlingCard boc : inn.getBowlingCard()) {
							String[] Count = CricketFunctions.getScoreTypeData(CricketUtil.BOWLER,match, whichInning, PlayerId,"-", match.getEventFile().getEvents()).split("-");
							if(PlayerId == boc.getPlayerId()) {
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeamName " + boc.getPlayer().getFull_name()+ ";");
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tScore " + boc.getWickets() + "-" + boc.getRuns() + ";");
								
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateHead " + "OVERS" + ";");
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateValue " + CricketFunctions.OverBalls(boc.getOvers(), boc.getBalls()) + ";");
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateValue1 " + Count[0] + ";");
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateValue2 " + Count[1] + ";");
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateValue3 " + Count[2] + ";");
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateValue4 " + Count[3] + ";");
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateValue5 " + Count[4] + ";");
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateValue6 " + Count[6] + ";");
							}
						}
					}
				}
				
				print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW ON;");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*CONTAINER SET ACTIVE 0;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In STOP;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out STOP;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 196.0;");
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
	public void populateLtNextToBat(PrintWriter print_writer, String viz_scene, MatchAllData match, String session_selected_broadcaster) 
	{
		switch (session_selected_broadcaster.toUpperCase()) {
		case "DOAD_IN_HOUSE_VIZ":
			if (match == null) {
				this.status = "ERROR: Match is null";
			} else if (match.getMatch().getInning() == null) {
				this.status = "ERROR: PlayerSummary's inning is null";
			} else {
				
				for(Inning inn : match.getMatch().getInning()) {
					if(inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)) {
						for(BattingCard bc : inn.getBattingCard()) {
							if(bc.getStatus().equalsIgnoreCase(CricketUtil.STILL_TO_BAT)) {
								//System.out.println(inn.getBowlingCard().get(inn.getBattingCard().size() -1).getPlayer().getFirstname());
								print_writer.println("-1 RENDERER*TREE*$Main$All$DataAll$All_Stats$StatGrp1$StatAll1$Player1FirstName*GEOM*TEXT SET " + inn.getBowlingCard().get(inn.getBattingCard().size() -1).getPlayer().getFirstname() + "\0");
								print_writer.println("-1 RENDERER*TREE*$Main$All$DataAll$All_Stats$StatGrp1$StatAll1$Player1FirstName*GEOM*TEXT SET " + bc.getPlayer().getFirstname() + "\0");
								print_writer.println("-1 RENDERER*TREE*$Main$All$DataAll$All_Stats$StatGrp1$StatAll1$Player1LastName*GEOM*TEXT SET " + bc.getPlayer().getSurname() + "\0");
								print_writer.println("-1 RENDERER*TREE*$Main$All$DataAll$All_Name$AT1*GEOM*TEXT SET " + bc.getBatterPosition() + "\0");

								//print_writer.println("-1 RENDERER*TREE*$Main$All$DataAll$All_Stats$StatGrp2$StatAll$SRPlayer1*GEOM*TEXT SET " + bc.getStrikeRate() + "\0");

								print_writer.println("-1 RENDERER*TREE*$Main$All$DataAll$All_Stats$StatGrp3$StatAll$Player1FirstName*GEOM*TEXT SET " + bc.getPlayer().getFirstname() + "\0");
								print_writer.println("-1 RENDERER*TREE*$Main$All$DataAll$All_Stats$StatGrp3$StatAll$Player1FirstName*GEOM*TEXT SET " + bc.getPlayer().getFirstname() + "\0");
								print_writer.println("-1 RENDERER*TREE*$Main$All$DataAll$All_Stats$StatGrp3$StatAll$Player1LastName*GEOM*TEXT SET " + bc.getPlayer().getSurname() + "\0");
								print_writer.println("-1 RENDERER*TREE*$Main$All$DataAll$All_Name$AT2*GEOM*TEXT SET " + bc.getBatterPosition() + "\0");
							}
								//print_writer.println("-1 RENDERER*TREE*$Main$All$DataAll$All_Stats$StatGrp3$StatAll$SRPlayer2*GEOM*TEXT SET " + bc.getStrikeRate() + "\0");
						}
					}
					
				}
				this.status = CricketUtil.SUCCESSFUL;
			}
			break;
		}
		
	}
	public void populateLtBowlerDetails(PrintWriter print_writer, String viz_scene,int whichInning, int PlayerId, MatchAllData match, String session_selected_broadcaster) 
	{
		switch (session_selected_broadcaster.toUpperCase()) {
			case "DOAD_IN_HOUSE_VIZ":
				if (match == null) {
					this.status = "ERROR: Match is null";
				} else if (match.getMatch().getInning() == null) {
					this.status = "ERROR: PlayerStats's inning is null";
				} else {
					int total_inn = 0;
					
					for(Inning inn : match.getMatch().getInning()) {
						if(inn.getInningStatus() != null) {
							total_inn = total_inn + 1;
						}
					}
					if(total_inn > 0 && whichInning > total_inn) {
						whichInning = total_inn;
					}
					for(Inning inn : match.getMatch().getInning()) {
						if (inn.getInningNumber() == whichInning) {
								for (BowlingCard boc : inn.getBowlingCard()) {
									if(boc.getPlayerId()==PlayerId) {
										print_writer.println("-1 RENDERER*TREE*$Main$All$DataAll$All_Name$PlayerName*GEOM*TEXT SET " + boc.getPlayer().getFull_name() + "\0");								
										print_writer.println("-1 RENDERER*TREE*$Main$All$DataAll$ROW1$All_StatsVal$StatHead_GRP$StatAll1$StatHead02*GEOM*TEXT SET " + boc.getOvers() + "\0");								
										print_writer.println("-1 RENDERER*TREE*$Main$All$DataAll$ROW1$All_StatsVal$StatValues_GRP$StatValue1$StatValue1B*GEOM*TEXT SET " + boc.getDots() + "\0");								
										print_writer.println("-1 RENDERER*TREE*$Main$All$DataAll$ROW1$All_StatsVal$StatValues_GRP$StatValue2$StatValue1B*GEOM*TEXT SET " + boc.getRuns() + "\0");								
										print_writer.println("-1 RENDERER*TREE*$Main$All$DataAll$ROW1$All_StatsVal$StatValues_GRP$StatValue3$StatValue1B*GEOM*TEXT SET " + boc.getWickets() + "\0");								
										print_writer.println("-1 RENDERER*TREE*$Main$All$DataAll$ROW1$All_StatsVal$StatValues_GRP$StatValue3$StatValue1B*GEOM*TEXT SET " + boc.getEconomyRate() + "\0");
									}
								}
								break;
						}
					}
					this.status = CricketUtil.SUCCESSFUL;	
				}
				break;
		}
		
	}
	public void populateFallofWicket(PrintWriter print_writer,String viz_scene,int whichInning, MatchAllData match, String session_selected_broadcaster) throws InterruptedException
	{
		switch (session_selected_broadcaster.toUpperCase()) {
		case "FRUIT":
			if (match == null) {
				this.status = "ERROR: Match is null";
			} else if (match.getMatch().getInning() == null) {
				this.status = "ERROR: DoubleTeam's inning is null";
			} else {
				int row_id= 0 ;
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgTLogo " + logo_path + "TLogo" + 
						CricketUtil.PNG_EXTENSION + ";");
				for(int i=1;i<=10;i++) {
					print_writer.println("LAYER2*EVEREST*TREEVIEW*" + (i  + 1) + "*CONTAINER SET ACTIVE 0;");
				}
				for(Inning inn : match.getMatch().getInning()) {
					if (inn.getInningNumber() == whichInning) {
						if (inn.getBowlingTeamId() == match.getSetup().getHomeTeamId()) {
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeamName " + match.getSetup().getAwayTeam().getTeamName1().toUpperCase() + ";");
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgTeamLogo " + logo_path + match.getSetup().getAwayTeam().getTeamName4() + CricketUtil.PNG_EXTENSION + ";");
						} else {
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeamName " + match.getSetup().getHomeTeam().getTeamName1().toUpperCase() + ";");
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgTeamLogo " + logo_path  + match.getSetup().getHomeTeam().getTeamName4() + CricketUtil.PNG_EXTENSION + ";");
						}
						if(inn.getTotalWickets() >= 10) {
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tScore " + inn.getTotalRuns() + ";");
						}
						else {
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tScore " + inn.getTotalRuns() + slashOrDash + inn.getTotalWickets() + ";");
						}
						
						
						if(inn.getFallsOfWickets() == null || inn.getFallsOfWickets().size() <= 0) {
							//System.out.println(inn.getFallsOfWickets());
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$BigBandGrp$Data*CONTAINER SET ACTIVE 0;");
						}
						else if(inn.getFallsOfWickets() != null || inn.getFallsOfWickets().size() > 0) {
							for(FallOfWicket fow : inn.getFallsOfWickets()) {								
								if(inn.getTotalWickets()>=0 && inn.getTotalWickets() <= 10) {
									row_id = row_id + 1;
									//System.out.println("row3="+row_id);
									print_writer.println("LAYER2*EVEREST*TREEVIEW*" + (row_id  + 1) + "*CONTAINER SET ACTIVE 1;");
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$BigBandGrp$Data*CONTAINER SET ACTIVE 1;");
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateHead "+ "WICKET:" + ";");
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vWKTS "+ fow.getFowNumber() + ";");
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateValue" + row_id + " "  + fow.getFowRuns() + ";");
								}		
							}
						}
					}
				}
				
				print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW ON;");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*CONTAINER SET ACTIVE 0;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In STOP;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out STOP;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 196.0;");
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
	public void populateSplit(PrintWriter print_writer,String viz_scene,int whichInning,int splitValue, MatchAllData match, String session_selected_broadcaster)
	{
		switch (session_selected_broadcaster.toUpperCase()) {
		case "FRUIT":
			if (match == null) {
				this.status = "ERROR: Match is null";
			} else {
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgTLogo " + logo_path + "TLogo" + 
						CricketUtil.PNG_EXTENSION + ";");
				for(int i=1;i<=5;i++) {
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$"+(i+1)+"*CONTAINER SET ACTIVE 0;");
				}
				for(Inning inn : match.getMatch().getInning()) {
					if (inn.getInningNumber() == whichInning) {
						if (inn.getBowlingTeamId() == match.getSetup().getHomeTeamId() && splitValue == 30 || inn.getBowlingTeamId() == match.getSetup().getHomeTeamId() && splitValue == 50) {
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeamName " + match.getSetup().getAwayTeam().getTeamName1().toUpperCase() + ";");
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSubHeader " + "BALLS PER " + splitValue + ";");
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgTeamLogo " + logo_path + match.getSetup().getAwayTeam().getTeamName4() + CricketUtil.PNG_EXTENSION + ";");
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateHead " + splitValue + CricketFunctions.Plural(splitValue) + " :" + ";");
						} else {
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeamName " + match.getSetup().getHomeTeam().getTeamName1().toUpperCase() + ";");
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSubHeader " + "BALLS PER " + splitValue + ";");
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgTeamLogo " + logo_path  + match.getSetup().getHomeTeam().getTeamName4() + CricketUtil.PNG_EXTENSION + ";");
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateHead " + splitValue + CricketFunctions.Plural(splitValue) + " :" + ";");
						}
						
						if(inn.getTotalWickets() >= 10) {
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tScore " + inn.getTotalRuns() + ";");
						}else {
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tScore " + inn.getTotalRuns() + slashOrDash + inn.getTotalWickets() + ";");
						}
						
						String[] Splitballs = new String[CricketFunctions.getSplit(whichInning, splitValue,match,match.getEventFile().getEvents()).size()];
					    for (int i = 0; i < CricketFunctions.getSplit(whichInning, splitValue,match,match.getEventFile().getEvents()).size(); i++) {
					    	Splitballs[i] = CricketFunctions.getSplit(whichInning, splitValue,match,match.getEventFile().getEvents()).get(i);
					    	
					    	int row_id = i + 1;
					    	
					    	print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$"+(row_id+1)+"*CONTAINER SET ACTIVE 1;");
					    	print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateValue"+ row_id + " "+ Splitballs[i] + ";");
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
				
				this.status = CricketUtil.SUCCESSFUL;	
			}
			break;
		}
		
	}	
	public void populateComparision(PrintWriter print_writer,String viz_scene, MatchAllData match, String session_selected_broadcaster) throws InterruptedException
	{
		switch (session_selected_broadcaster.toUpperCase()) {
		case "FRUIT":
			if (match == null) {
				this.status = "ERROR: Match is null";
			} else if (match.getMatch().getInning() == null) {
				this.status = "ERROR: Target's inning is null";
			} else {
				for(Inning inn : match.getMatch().getInning()) {
					if(inn.getInningNumber() == 2 & inn.getIsCurrentInning().equalsIgnoreCase("YES")) {
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgTeamLogo " + logo_path + "TLogo" + CricketUtil.PNG_EXTENSION + ";");
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgTLogo " + logo_path + "TLogo" + 
								CricketUtil.PNG_EXTENSION + ";");
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHeader " + "AFTER " + CricketFunctions.OverBalls(inn.getTotalOvers(), inn.getTotalBalls()) + " OVERS" + ";");
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSubHead1 " + inn.getBowling_team().getTeamName1().toUpperCase() + " WERE " + CricketFunctions.compareInningData(match, "-", 1, match.getEventFile().getEvents()) + ";");
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSubHead2 " + inn.getBatting_team().getTeamName1().toUpperCase() + " ARE " + CricketFunctions.compareInningData(match, "-", 2, match.getEventFile().getEvents()) + ";");						
					}
					if(match.getMatch().getInning().get(0).getTotalWickets() >= 10) {
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tScore " + match.getMatch().getInning().get(0).getTotalRuns() 
								+ " (" + CricketFunctions.OverBalls(match.getMatch().getInning().get(0).getTotalOvers(), match.getMatch().getInning().get(0).getTotalBalls()) + ")" + ";");
					}else {
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tScore " + match.getMatch().getInning().get(0).getTotalRuns() + "-" + match.getMatch().getInning().get(0).getTotalWickets() + ";");
					}
				}
				print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW ON;");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*CONTAINER SET ACTIVE 0;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In STOP;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out STOP;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 196.0;");
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
	public void populateCurrentPartnership(PrintWriter print_writer, String viz_scene, MatchAllData match, String session_selected_broadcaster) throws InterruptedException 
	{
		switch (session_selected_broadcaster.toUpperCase()) {
		case "FRUIT":
			if (match == null) {
				this.status = "ERROR: Match is null";
			} else if (match.getMatch().getInning() == null) {
				this.status = "ERROR: Partnership's inning is null";
			} else {
				
				String Left_Batsman ="",Right_Batsman="";
		
				for(Inning inn : match.getMatch().getInning()) {
					if(inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)) {
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBatTeamName " + "CURRENT PARTNERSHIP" + ";");
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBatSubHeader " + match.getSetup().getMatchIdent().toUpperCase() + ";");
						
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgTeamLogo " + logo_path + inn.getBatting_team().getTeamName4() + 
								CricketUtil.PNG_EXTENSION + ";");
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgTLogo " + logo_path + "TLogo" + 
								CricketUtil.PNG_EXTENSION + ";");
						
						for(Player hs : match.getSetup().getHomeSquad()) {
							if(hs.getPlayerId() == inn.getPartnerships().get(inn.getPartnerships().size() - 1).getFirstBatterNo()) {
								Left_Batsman = hs.getTicker_name();
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgPlayerImage01 " + photo_path + match.getSetup().getHomeTeam().getTeamName4().toUpperCase() + 
										CricketUtil.DOUBLE_BACKSLASH + hs.getPhoto() + CricketUtil.PNG_EXTENSION + ";");	
							}
							if(hs.getPlayerId() == inn.getPartnerships().get(inn.getPartnerships().size() - 1).getSecondBatterNo()) {
								Right_Batsman = hs.getTicker_name();
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgPlayerImage02 " + photo_path + match.getSetup().getHomeTeam().getTeamName4().toUpperCase() + 
										CricketUtil.DOUBLE_BACKSLASH + hs.getPhoto() + CricketUtil.PNG_EXTENSION + ";");	
							}
						}
						
						for(Player as : match.getSetup().getAwaySquad()) {
							if(as.getPlayerId() == inn.getPartnerships().get(inn.getPartnerships().size() - 1).getFirstBatterNo()) {
								Left_Batsman = as.getTicker_name();
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgPlayerImage01 " + photo_path + match.getSetup().getAwayTeam().getTeamName4().toUpperCase() + 
										CricketUtil.DOUBLE_BACKSLASH + as.getPhoto() + CricketUtil.PNG_EXTENSION + ";");	
							}
							if(as.getPlayerId() == inn.getPartnerships().get(inn.getPartnerships().size() - 1).getSecondBatterNo()) {
								Right_Batsman = as.getTicker_name();
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgPlayerImage02 " + photo_path + match.getSetup().getAwayTeam().getTeamName4().toUpperCase() + 
										CricketUtil.DOUBLE_BACKSLASH + as.getPhoto() + CricketUtil.PNG_EXTENSION + ";");	
							}
						}
						
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tRuns " + inn.getPartnerships().get(inn.getPartnerships().size() - 1).getTotalRuns() + ";");
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBalls " + inn.getPartnerships().get(inn.getPartnerships().size() - 1).getTotalBalls() + ";");
						
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerName01 " + Left_Batsman + ";");
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerName02 " + Right_Batsman + ";");
						
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerContriRuns01 " + inn.getPartnerships().get(inn.getPartnerships().size() - 1).getFirstBatterRuns() + ";");
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerContriBalls01 " + inn.getPartnerships().get(inn.getPartnerships().size() - 1).getFirstBatterBalls() + ";");
						
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerContriRuns02 " + inn.getPartnerships().get(inn.getPartnerships().size() - 1).getSecondBatterRuns() + ";");
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerContriBalls02 " + inn.getPartnerships().get(inn.getPartnerships().size() - 1).getSecondBatterBalls() + ";");
						
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFoursValue " + inn.getPartnerships().get(inn.getPartnerships().size() - 1).getTotalFours() + ";");
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSixesValue " + inn.getPartnerships().get(inn.getPartnerships().size() - 1).getTotalSixes() + ";");
						
						if(inn.getTotalWickets() == 0) {
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInfo " + (inn.getTotalWickets()+1) + "st WICKET PARTNERSHIP" + ";");
						}else if(inn.getTotalWickets() == 1) {
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInfo " + (inn.getTotalWickets()+1) + "nd WICKET PARTNERSHIP" + ";");
						}else if (inn.getTotalWickets() == 2) {
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInfo " + (inn.getTotalWickets()+1) + "rd WICKET PARTNERSHIP" + ";");
						}
						else {
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInfo " + (inn.getTotalWickets()+1) + "th WICKET PARTNERSHIP" + ";");
						}
					}
				}
				
				print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW ON;");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*CONTAINER SET ACTIVE 0;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In STOP;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out STOP;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 71.0;");
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
	public void populateLtPowerPlay(PrintWriter print_writer, String viz_scene, MatchAllData match, String session_selected_broadcaster) 
	{
		if (match == null) {
			this.status = "ERROR: Match is null";
		} else if (match.getMatch().getInning() == null) {
			this.status = "ERROR: Partnership's inning is null";
		} else {
			
			
			
			//print_writer.println("-1 RENDERER*TREE*$Main$TopPart$SubHeaderGrp$SubHeaderText$Langauage1*GEOM*TEXT SET " + match.getTournament() + "\0");
			//print_writer.println("-1 RENDERER*TREE*$Main$TopPart$HeaderGrp$PartHeader$MatchId$Langauage1*FUNCTION*ControlDatapool*input SET " + match.getMatchIdent() + "\0");
			for(Inning inn : match.getMatch().getInning()) {
				//String[] PowerPlay_Over = getPowerPlayScore(match, 1, match.getEvents());
				if(inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)) {
					print_writer.println("-1 RENDERER*TREE*$Main$All$DataAll$All_Name$PlayerName*GEOM*TEXT SET " + inn.getBatting_team().getTeamName1().toUpperCase() + "\0");
					if(inn.getTotalWickets() < 10) {
						print_writer.println("-1 RENDERER*TREE*$Main$All$DataAll$All_Score$Score*GEOM*TEXT SET " + inn.getTotalRuns() + "-" + inn.getTotalWickets() + "\0");
					}
					else {
						print_writer.println("-1 RENDERER*TREE*$Main$All$DataAll$All_Score$Score*GEOM*TEXT SET " + inn.getTotalRuns() + "\0");
					}
					if(inn.getFirstPowerplayEndOver() >= inn.getTotalOvers()) {
						
						/*print_writer.println("-1 RENDERER*TREE*$Main$All$DataAll$ROW1$All_StatsVal$StatHead_GRP$PP1$noname$StatValue1B*GEOM*TEXT SET " +  + "\0");
						print_writer.println("-1 RENDERER*TREE*$Main$All$DataAll$ROW1$All_StatsVal$StatHead_GRP$PP1$noname$StatValue1B*GEOM*TEXT SET " +  + "\0");
						print_writer.println("-1 RENDERER*TREE*$Main$All$DataAll$ROW1$All_StatsVal$StatHead_GRP$PP1$noname$StatValue1B*GEOM*TEXT SET " +  + "\0");
						print_writer.println("-1 RENDERER*TREE*$Main$All$DataAll$ROW1$All_StatsVal$StatHead_GRP$PP1$noname$StatValue1B*GEOM*TEXT SET " +  + "\0");*/
					}
					else if ((inn.getSecondPowerplayEndOver() >= inn.getTotalOvers()) || (inn.getSecondPowerplayStartOver() <= inn.getTotalOvers() )) {
						
					}
					
				}
			}
			
			this.status = CricketUtil.SUCCESSFUL;
		}
	}
	public void populateLeaderBoard(PrintWriter print_writer,String viz_scene,String StatType,int playerid,List<Tournament> tournament,List<Team> team,MatchAllData match, String broadcaster) throws InterruptedException 
	{
		if (match == null) {
			this.status = "ERROR: Match is null";
		} else if (match.getMatch().getInning() == null) {
			this.status = "ERROR: Leaderboard inning is null";
		} else {
			
			int row_no=0;
			print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgTeamLogo " + logo_path + "TLogo" + 
					CricketUtil.PNG_EXTENSION + ";");
			
			switch(StatType.toUpperCase()) {
			case "MOST_RUNS_DATA":
				
				Collections.sort(tournament,new CricketFunctions.BatsmenMostRunComparator());
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHeader " + "MOST RUNS " + ";");
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSubHeader " + match.getSetup().getTournament() + ";");
				for(int i = 0; i <= tournament.size() - 1 ; i++) {
					row_no = row_no + 1;
					if(row_no <= 5) {
						if(tournament.get(i).getPlayerId() == playerid) {
//							print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vHighlight" + row_no + " SET " + 1 + "\0");
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgPlayerImage " + photo_path + 
									team.get(tournament.get(i).getPlayer().getTeamId() -1 ).getTeamName4() + CricketUtil.DOUBLE_BACKSLASH + 
									tournament.get(i).getPlayer().getPhoto() + CricketUtil.PNG_EXTENSION + ";");
						}else {
//							print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vHighlight" + row_no + " SET " + 0 + "\0");
						}
						
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerFirstName0" + row_no + " " + tournament.get(i).getPlayer().getFull_name() + ";");
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerLastName0"  + row_no + " " + " " + ";");
						
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeam0" + row_no + " " + team.get(tournament.get(i).getPlayer().getTeamId() - 1).getTeamName1() + ";");
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tData0" + row_no + "A " + team.get(tournament.get(i).getPlayer().getTeamId() - 1).getTeamName1() + ";");
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tData0" + row_no + "B " + " " + ";");
						
					}
				}
				break;
			case "MOST_RUNS":
				
				Collections.sort(tournament,new CricketFunctions.BatsmenMostRunComparator());
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHeader " + "MOST RUNS " + ";");
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSubHeader " + match.getSetup().getTournament() + ";");
				for(int i = 0; i <= tournament.size() - 1 ; i++) {
					row_no = row_no + 1;
					if(row_no <= 5) {
						if(tournament.get(i).getPlayerId() == playerid) {
//							print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vHighlight" + row_no + " SET " + 1 + "\0");
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgPlayerImage " + photo_path + 
									team.get(tournament.get(i).getPlayer().getTeamId() -1 ).getTeamName4() + CricketUtil.DOUBLE_BACKSLASH + 
									tournament.get(i).getPlayer().getPhoto() + CricketUtil.PNG_EXTENSION + ";");
						}else {
//							print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vHighlight" + row_no + " SET " + 0 + "\0");
						}
						
						if(tournament.get(i).getRuns() > 0) {
							if(tournament.get(i).getPlayer().getSurname() != null) {
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerFirstName0" + row_no + " " + tournament.get(i).getPlayer().getFirstname() + ";");
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerLastName0"  + row_no + " " + tournament.get(i).getPlayer().getSurname() + ";");
							}else {
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerFirstName0" + row_no + " " + "" + ";");
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerLastName0"  + row_no + " " + tournament.get(i).getPlayer().getFirstname() + ";");
							}
							
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeam0" + row_no + " " + team.get(tournament.get(i).getPlayer().getTeamId() - 1).getTeamName1() + ";");
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tData0" + row_no + "A " + tournament.get(i).getRuns() + ";");
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tData0" + row_no + "B " + " " + ";");
						}else {
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerFirstName0" + row_no + " " + "" + ";");
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerLastName0"  + row_no + " " + "" + ";");
							
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeam0" + row_no + " " + "" + ";");
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tData0" + row_no + "A " + "" + ";");
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tData0" + row_no + "B " + " " + ";");
						}
						
					}
				}
				break;
			case "MOST_WICKETS":
				
				Collections.sort(tournament,new CricketFunctions.BowlerWicketsComparator());
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHeader " + "MOST WICKETS " + ";");
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSubHeader " + match.getSetup().getTournament() + ";");
				for(int i = 0; i <= tournament.size() - 1 ; i++) {
					row_no = row_no + 1;
					if(row_no <= 5) {
						if(tournament.get(i).getPlayerId() == playerid) {
//							print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vHighlight" + row_no + " SET " + 1 + "\0");
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgPlayerImage " + photo_path + 
									team.get(tournament.get(i).getPlayer().getTeamId() -1 ).getTeamName4() + CricketUtil.DOUBLE_BACKSLASH + 
									tournament.get(i).getPlayer().getPhoto() + CricketUtil.PNG_EXTENSION + ";");
							
						}else {
//							print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vHighlight" + row_no + " SET " + 0 + "\0");
						}
						
						if(tournament.get(i).getWickets() > 0) {
							if(tournament.get(i).getPlayer().getSurname() != null) {
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerFirstName0" + row_no + " " + tournament.get(i).getPlayer().getFirstname() + ";");
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerLastName0"  + row_no + " " + tournament.get(i).getPlayer().getSurname() + ";");
							}else {
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerFirstName0" + row_no + " " + "" + ";");
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerLastName0"  + row_no + " " + tournament.get(i).getPlayer().getFirstname() + ";");
							}
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeam0" + row_no + " " + team.get(tournament.get(i).getPlayer().getTeamId() - 1).getTeamName1() + ";");
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tData0" + row_no + "A " + tournament.get(i).getWickets() + ";");
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tData0" + row_no + "B " + " " + ";");
						}else {
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerFirstName0" + row_no + " " + "" + ";");
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerLastName0"  + row_no + " " + "" + ";");
							
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeam0" + row_no + " " + "" + ";");
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tData0" + row_no + "A " + "" + ";");
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tData0" + row_no + "B " + " " + ";");
						}
					}
				}
				break;
			case "MOST_FOURS":
				
				Collections.sort(tournament,new CricketFunctions.BatsmanFoursComparator());
				
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHeader " + "MOST FOURS " + ";");
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSubHeader " + match.getSetup().getTournament() + ";");
				for(int i = 0; i <= tournament.size() - 1 ; i++) {
					row_no = row_no + 1;
					if(row_no <= 5) {
						if(tournament.get(i).getPlayerId() == playerid) {
//							print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vHighlight" + row_no + " SET " + 1 + "\0");
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgPlayerImage " + photo_path + 
									team.get(tournament.get(i).getPlayer().getTeamId() -1 ).getTeamName4() + CricketUtil.DOUBLE_BACKSLASH + 
									tournament.get(i).getPlayer().getPhoto() + CricketUtil.PNG_EXTENSION + ";");
						}else {
//							print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vHighlight" + row_no + " SET " + 0 + "\0");
						}
						
						if(tournament.get(i).getFours() > 0) {
							if(tournament.get(i).getPlayer().getSurname() != null) {
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerFirstName0" + row_no + " " + tournament.get(i).getPlayer().getFirstname() + ";");
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerLastName0"  + row_no + " " + tournament.get(i).getPlayer().getSurname() + ";");
							}else {
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerFirstName0" + row_no + " " + "" + ";");
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerLastName0"  + row_no + " " + tournament.get(i).getPlayer().getFirstname() + ";");
							}
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeam0" + row_no + " " + team.get(tournament.get(i).getPlayer().getTeamId() - 1).getTeamName1() + ";");
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tData0" + row_no + "A " + tournament.get(i).getFours() + ";");
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tData0" + row_no + "B " + " " + ";");
						}else {
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerFirstName0" + row_no + " " + "" + ";");
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerLastName0"  + row_no + " " + "" + ";");
							
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeam0" + row_no + " " + "" + ";");
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tData0" + row_no + "A " + "" + ";");
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tData0" + row_no + "B " + " " + ";");
						}
					}
				}
				break;
			case "MOST_SIXES":
				Collections.sort(tournament,new CricketFunctions.BatsmanSixesComparator());
				
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHeader " + "MOST SIXES " + ";");
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSubHeader " + match.getSetup().getTournament() + ";");
				for(int i = 0; i <= tournament.size() - 1 ; i++) {
					row_no = row_no + 1;
					if(row_no <= 5) {
						if(tournament.get(i).getPlayerId() == playerid) {
//							print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vHighlight" + row_no + " SET " + 1 + "\0");
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgPlayerImage " + photo_path + 
									team.get(tournament.get(i).getPlayer().getTeamId() -1 ).getTeamName4() + CricketUtil.DOUBLE_BACKSLASH + 
									tournament.get(i).getPlayer().getPhoto() + CricketUtil.PNG_EXTENSION + ";");
						}else {
//							print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vHighlight" + row_no + " SET " + 0 + "\0");
						}
						
						if(tournament.get(i).getSixes() > 0) {
							if(tournament.get(i).getPlayer().getSurname() != null) {
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerFirstName0" + row_no + " " + tournament.get(i).getPlayer().getFirstname() + ";");
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerLastName0"  + row_no + " " + tournament.get(i).getPlayer().getSurname() + ";");
							}else {
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerFirstName0" + row_no + " " + "" + ";");
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerLastName0"  + row_no + " " + tournament.get(i).getPlayer().getFirstname() + ";");
							}
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeam0" + row_no + " " + team.get(tournament.get(i).getPlayer().getTeamId() - 1).getTeamName1() + ";");
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tData0" + row_no + "A " + tournament.get(i).getSixes() + ";");
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tData0" + row_no + "B " + " " + ";");
						}else {
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerFirstName0" + row_no + " " + "" + ";");
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerLastName0"  + row_no + " " + "" + ";");
							
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeam0" + row_no + " " + "" + ";");
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tData0" + row_no + "A " + "" + ";");
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tData0" + row_no + "B " + " " + ";");
						}
					}
				}
				break;
			}
			
			print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW ON;");
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In STOP;");
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out STOP;");
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 92.0;");
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out SHOW 0.0;");
			print_writer.println("LAYER1*EVEREST*GLOBAL SNAPSHOT_PATH C:/Temp/Preview.png;");
			print_writer.println("LAYER1*EVEREST*GLOBAL SNAPSHOT 1920 1080;");
			TimeUnit.SECONDS.sleep(1);
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out SHOW 0.0;");
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 0.0;");
			print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW OFF;");
			this.status = CricketUtil.SUCCESSFUL;
					
		}
	}	
	public void populateLandMark(PrintWriter print_writer,String viz_scene, int whichInning, String statType, int playerId, MatchAllData match, String session_selected_broadcaster)
	{
		switch (session_selected_broadcaster.toUpperCase()) {
		case "DOAD_IN_HOUSE_VIZ":
			if (match == null) {
				this.status = "ERROR: Match is null";
			} else {
				//String Home_or_Away="";
				for(Inning inn : match.getMatch().getInning()) {
					if (inn.getInningNumber() == whichInning) {
						
						switch(statType.toUpperCase()) {
						case "BATSMAN":
							for(BattingCard bc : inn.getBattingCard()) {
								if(playerId == bc.getPlayerId()) {
									
									print_writer.println("-1 RENDERER*TREE*$Main$All$NameGrp$MaxSize$FirstName*GEOM*TEXT SET "+ bc.getPlayer().getFirstname().toUpperCase() + " \0");
									print_writer.println("-1 RENDERER*TREE*$Main$All$NameGrp$MaxSize$FirstName*GEOM*TEXT SET "+ bc.getPlayer().getFirstname().toUpperCase() + " \0");
									print_writer.println("-1 RENDERER*TREE*$Main$All$NameGrp$MaxSize$LastName*GEOM*TEXT SET "+ bc.getPlayer().getSurname().toUpperCase() + " \0");
									if(bc.getStatus().equals(CricketUtil.OUT)) {
										print_writer.println("-1 RENDERER*TREE*$Main$All$DataAll$WithoutBar$Row1$RowAnimation$Runs*GEOM*TEXT SET "+ bc.getRuns() + " \0");
									}
									else {
										print_writer.println("-1 RENDERER*TREE*$Main$All$DataAll$WithoutBar$Row1$RowAnimation$Runs*GEOM*TEXT SET "+ bc.getRuns() + "*" + " \0");
									}
									
									print_writer.println("-1 RENDERER*TREE*$Main$All$DataAll$WithoutBar$Row1$RowAnimation$Balls*GEOM*TEXT SET "+ bc.getBalls() + " \0");
									print_writer.println("-1 RENDERER*TREE*$Main$All$DataAll$WithoutBar$Row1$RowAnimation$noname$StatHead*GEOM*TEXT SET "+ "S/R " + " \0");
									print_writer.println("-1 RENDERER*TREE*$Main$All$DataAll$WithoutBar$Row1$RowAnimation$noname$StatValue*GEOM*TEXT SET "+ bc.getStrikeRate() + " \0");
									if(inn.getBattingTeamId() == match.getSetup().getHomeTeamId()) {
										print_writer.println("-1 RENDERER*TREE*$Main$All$TeamBadge*TEXTURE*IMAGE SET " + logo_path + 
														match.getSetup().getHomeTeam().getTeamName1() + ".png" + "\0");
										
									}
									else {
										print_writer.println("-1 RENDERER*TREE*$Main$All$TeamBadge*TEXTURE*IMAGE SET " + logo_path + 
														match.getSetup().getAwayTeam().getTeamName1() + ".png" + "\0");
									}
									print_writer.println("-1 RENDERER*TREE*$Main$All$All_Player_Pic$noname$Player_Pic$PlayerImage*TEXTURE*IMAGE SET " + 
											photo_path + bc.getPlayer().getFirstname() + ".png" + "\0");
								}
							}
							
							break;
						case "BOWLER":
							for(BowlingCard boc : inn.getBowlingCard()) {
								if(playerId == boc.getPlayerId()) {
									
									print_writer.println("-1 RENDERER*TREE*$Main$All$NameGrp$MaxSize$FirstName*GEOM*TEXT SET "+ boc.getPlayer().getFirstname().toUpperCase() + " \0");
									print_writer.println("-1 RENDERER*TREE*$Main$All$NameGrp$MaxSize$FirstName*GEOM*TEXT SET "+ boc.getPlayer().getFirstname().toUpperCase() + " \0");
									print_writer.println("-1 RENDERER*TREE*$Main$All$NameGrp$MaxSize$LastName*GEOM*TEXT SET "+ boc.getPlayer().getSurname().toUpperCase() + " \0");
									print_writer.println("-1 RENDERER*TREE*$Main$All$DataAll$WithoutBar$Row1$RowAnimation$Runs*GEOM*TEXT SET "+ boc.getRuns() + " \0");
									print_writer.println("-1 RENDERER*TREE*$Main$All$DataAll$WithoutBar$Row1$RowAnimation$Balls*GEOM*TEXT SET "+ boc.getBalls() + " \0");
									print_writer.println("-1 RENDERER*TREE*$Main$All$DataAll$WithoutBar$Row1$RowAnimation$noname$StatHead*GEOM*TEXT SET "+ "ECO " + " \0");
									print_writer.println("-1 RENDERER*TREE*$Main$All$DataAll$WithoutBar$Row1$RowAnimation$noname$StatValue*GEOM*TEXT SET "+ boc.getEconomyRate() + " \0");
									if(inn.getBowlingTeamId() == match.getSetup().getHomeTeamId()) {
										print_writer.println("-1 RENDERER*TREE*$Main$All$TeamBadge*TEXTURE*IMAGE SET " + logo_path + 
														match.getSetup().getHomeTeam().getTeamName1() + ".png" + "\0");
										
									}
									else {
										print_writer.println("-1 RENDERER*TREE*$Main$All$TeamBadge*TEXTURE*IMAGE SET " + logo_path + 
														match.getSetup().getAwayTeam().getTeamName1() + ".png" + "\0");
									}
									print_writer.println("-1 RENDERER*TREE*$Main$All$All_Player_Pic$noname$Player_Pic$PlayerImage*TEXTURE*IMAGE SET " + 
											photo_path + boc.getPlayer().getFirstname() + ".png" + "\0");
								
								}
							}
							break;
						}
					}
				}
				this.status = CricketUtil.SUCCESSFUL;	
			}
			break;
		
		}
	}
	public void populateFFLandMark(PrintWriter print_writer,String viz_scene, int whichInning, int playerId, MatchAllData match, String session_selected_broadcaster)
	{
		switch (session_selected_broadcaster.toUpperCase()) {
		case "DOAD_IN_HOUSE_VIZ":
			if (match == null) {
				this.status = "ERROR: Match is null";
			} else {
				//String Home_or_Away="";
				for(Inning inn : match.getMatch().getInning()) {
					if (inn.getInningNumber() == whichInning) {
						
						for(BattingCard bc : inn.getBattingCard()) {
							if(playerId == bc.getPlayerId()) {
								
								print_writer.println("-1 RENDERER*TREE*$Main$All$MaxSize$PlayerName*GEOM*TEXT SET "+ bc.getPlayer().getFirstname().toUpperCase() + " \0");
								print_writer.println("-1 RENDERER*TREE*$Main$All$text$Row1$RowAnimation$noname$StatValue*GEOM*TEXT SET "+ bc.getBatterPosition() + " \0");
								
								if(inn.getBattingTeamId() == match.getSetup().getHomeTeamId()) {
									print_writer.println("-1 RENDERER*TREE*$Main$All$TeamBadge*TEXTURE*IMAGE SET " + logo_path + 
													match.getSetup().getHomeTeam().getTeamName1() + ".png" + "\0");
									
								}
								else {
									print_writer.println("-1 RENDERER*TREE*$Main$All$TeamBadge*TEXTURE*IMAGE SET " + logo_path + 
													match.getSetup().getAwayTeam().getTeamName1() + ".png" + "\0");
								}
								print_writer.println("-1 RENDERER*TREE*$Main$All$All_Player_Pic$noname$Plaer_Pic$PlayerImage*TEXTURE*IMAGE SET " + 
										photo_path + bc.getPlayer().getFirstname() + ".png" + "\0");
							}
						}
					}
				}
				this.status = CricketUtil.SUCCESSFUL;	
			}
			break;
		
		}
	}
	public void populateLtEquation(PrintWriter print_writer,String viz_scene, MatchAllData match, String session_selected_broadcaster) throws InterruptedException
	{
		switch (session_selected_broadcaster.toUpperCase()) {
		case "FRUIT":
			if (match == null) {
				this.status = "ERROR: Match is null";
			} else if (match.getMatch().getInning() == null) {
				this.status = "ERROR: Equation's inning is null";
			} else {
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgTeamLogo " + logo_path + "TLogo" + CricketUtil.PNG_EXTENSION + ";");
				for(Inning inn : match.getMatch().getInning()) {
					if(inn.getInningNumber() == 2 && inn.getIsCurrentInning().equalsIgnoreCase("YES")) {
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHeader " +  "EQUATION" +";");
						
						if(match.getMatch().getMatchResult() != null) {
							if(match.getMatch().getMatchResult().toUpperCase().equalsIgnoreCase("")) {
								if(match.getSetup().getTargetType() == null || match.getSetup().getTargetType().trim().isEmpty()) {
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tDetails " + 
											CricketFunctions.generateMatchSummaryStatus(inn.getInningNumber(), match, CricketUtil.FULL).toUpperCase() + ";");
								}else {
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tDetails " + 
											CricketFunctions.generateMatchSummaryStatus(inn.getInningNumber(), match, CricketUtil.FULL).toUpperCase() + 
											" ("+ match.getSetup().getTargetType().toUpperCase() + ")" + ";");
								}
							}
							else if(match.getMatch().getMatchResult().toUpperCase().equalsIgnoreCase("DRAWN")){
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tDetails " + "MATCH TIED" + ";");
							}
							else if(match.getMatch().getMatchResult().split(",")[1].toUpperCase().equalsIgnoreCase("SUPER_OVER")){
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tDetails " + "MATCH TIED - " + match.getMatch().getMatchStatus().toUpperCase() + ";");
							}
							else {
								if(match.getSetup().getTargetType() == null || match.getSetup().getTargetType().trim().isEmpty()) {
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tDetails " + 
											CricketFunctions.generateMatchSummaryStatus(inn.getInningNumber(), match, CricketUtil.FULL).toUpperCase() + ";");
								}else {
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tDetails " + 
											CricketFunctions.generateMatchSummaryStatus(inn.getInningNumber(), match, CricketUtil.FULL).toUpperCase() + 
											" ("+ match.getSetup().getTargetType().toUpperCase() + ")" + ";");
								}
							}
						}
						else {
							if(match.getSetup().getTargetType() == null || match.getSetup().getTargetType().trim().isEmpty()) {
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tDetails " + 
										CricketFunctions.generateMatchSummaryStatus(inn.getInningNumber(), match, CricketUtil.FULL).toUpperCase() + ";");
							}else {
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tDetails " + 
										CricketFunctions.generateMatchSummaryStatus(inn.getInningNumber(), match, CricketUtil.FULL).toUpperCase() + 
										" ("+ match.getSetup().getTargetType().toUpperCase() + ")" + ";");
							}
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
			TimeUnit.SECONDS.sleep(1);
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out SHOW 0.0;");
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 0.0;");
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*CONTAINER SET ACTIVE 1;");
			print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW OFF;");
			
			this.status = CricketUtil.SUCCESSFUL;
			
			break;
		}
		
	}
	public void populateLtMatchStatus(PrintWriter print_writer,String viz_scene, MatchAllData match, String session_selected_broadcaster) throws InterruptedException
	{
		switch (session_selected_broadcaster.toUpperCase()) {
		case "FRUIT":
			if (match == null) {
				this.status = "ERROR: Match is null";
			} else if (match.getMatch().getInning() == null) {
				this.status = "ERROR: Equation's inning is null";
			} else{
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgTeamLogo " + logo_path + "TLogo" + CricketUtil.PNG_EXTENSION + ";");
				//print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFirstName " +  "EQUATION" +";");

				if(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.TEST)) {
					if(match.getMatch().getInning().get(1).getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)) {
						if(CricketFunctions.getTeamRunsAhead(2,match) > 0) {
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerName " +  match.getMatch().getInning().get(1).getBatting_team().getTeamName2() + " LEAD BY" +";");
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSubHeader " +  "" +";");
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tRunsValue " +  CricketFunctions.getTeamRunsAhead(2,match) +";");
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tRunsHead " + 
									      " RUN" + CricketFunctions.Plural(CricketFunctions.getTeamRunsAhead(2,match)).toUpperCase() + ";");
						} else if(CricketFunctions.getTeamRunsAhead(2,match) == 0) {
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerName " +  match.getMatch().getInning().get(1).getBatting_team().getTeamName2() +";");
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSubHeader " +  "" +";");
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tRunsValue " +  "" +";");

							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tRunsHead " + "SCORES ARE LEVEL" + ";");
						} else {
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerName " +  match.getMatch().getInning().get(1).getBatting_team().getTeamName2()+ " TRAIL BY" +";");
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSubHeader " + "" +";");
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tRunsValue " +  (-1 * CricketFunctions.getTeamRunsAhead(2,match)) +";");

							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tRunsHead " + 
									     " RUN" + CricketFunctions.Plural(-1 * CricketFunctions.getTeamRunsAhead(2,match)).toUpperCase() + ";");
						}
					}else if(match.getMatch().getInning().get(2).getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)) {
						if(CricketFunctions.getTeamRunsAhead(3,match) > 0) {
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerName " +  match.getMatch().getInning().get(2).getBatting_team().getTeamName2() + " LEAD BY" +";");
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSubHeader " +  "" +";");
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tRunsValue " +  CricketFunctions.getTeamRunsAhead(3,match) +";");

							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tRunsHead " + 
									     " RUN" + CricketFunctions.Plural(CricketFunctions.getTeamRunsAhead(3,match)).toUpperCase() + ";");
						} else if(CricketFunctions.getTeamRunsAhead(3,match) == 0) {
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerName " +  match.getMatch().getInning().get(2).getBatting_team().getTeamName2() +";");
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSubHeader " +  "" +";");
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tRunsValue " +  "" +";");

							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tRunsHead " + "SCORES ARE LEVEL" + ";");
						} else {
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerName " +  match.getMatch().getInning().get(2).getBatting_team().getTeamName2() + " TRAIL BY" +";");
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSubHeader " + "" +";");
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tRunsValue " +  (-1 * CricketFunctions.getTeamRunsAhead(3,match)) +";");

							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tRunsHead " + 
									    " RUN" + CricketFunctions.Plural(-1 * CricketFunctions.getTeamRunsAhead(3,match)).toUpperCase() + ";");
						}
					}else if(match.getMatch().getInning().get(3).getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)) {
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerName " +  "EQUATION" +";");
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSubHeader " +  "" +";");
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tRunsValue " +  "" +";");

						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tRunsHead " + match.getMatch().getMatchStatus().toUpperCase() + ";");
					}
				}
			}
			
			print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW ON;");
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*CONTAINER SET ACTIVE 0;");
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In STOP;");
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out STOP;");
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 197.0;");
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out SHOW 0.0;");
			print_writer.println("LAYER1*EVEREST*GLOBAL SNAPSHOT_PATH C:/Temp/Preview.png;");
			print_writer.println("LAYER1*EVEREST*GLOBAL SNAPSHOT 1920 1080;");
			TimeUnit.SECONDS.sleep(1);
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out SHOW 0.0;");
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 0.0;");
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*CONTAINER SET ACTIVE 1;");
			print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW OFF;");
			
			this.status = CricketUtil.SUCCESSFUL;
			
			break;
		}
		
	}
	public void populatePointsTable(PrintWriter print_writer,String viz_sence_path,List<LeagueTeam> groupA,List<LeagueTeam> groupB, String session_selected_broadcaster,MatchAllData match) throws InterruptedException 
	{
		switch (session_selected_broadcaster.toUpperCase()) {
		case "FRUIT":
			
			if(which_graphics_onscreen == "BOWLINGCARD") {
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$BattingCard_GRP*CONTAINER SET ACTIVE 0;");
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$BowlingCard_GRP*CONTAINER SET ACTIVE 1;");
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$Summary_GRP*CONTAINER SET ACTIVE 0;");
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$PointsTable_GRP*CONTAINER SET ACTIVE 0;");
			}else if(which_graphics_onscreen == "SCORECARD") {
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$BattingCard_GRP*CONTAINER SET ACTIVE 1;");
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$BowlingCard_GRP*CONTAINER SET ACTIVE 0;");
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$Summary_GRP*CONTAINER SET ACTIVE 0;");
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$PointsTable_GRP*CONTAINER SET ACTIVE 0;");
			}else if(which_graphics_onscreen == "SUMMARY") {
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$BattingCard_GRP*CONTAINER SET ACTIVE 0;");
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$BowlingCard_GRP*CONTAINER SET ACTIVE 0;");
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$Summary_GRP*CONTAINER SET ACTIVE 1;");
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$PointsTable_GRP*CONTAINER SET ACTIVE 0;");
			}else {
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$BattingCard_GRP*CONTAINER SET ACTIVE 0;");
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$BowlingCard_GRP*CONTAINER SET ACTIVE 0;");
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$Summary_GRP*CONTAINER SET ACTIVE 0;");
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$PointsTable_GRP*CONTAINER SET ACTIVE 1;");
			}
			
			
			print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPool01 " + "GROUP A" + ";");
			print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPool02 " + "GROUP B" + ";");
			
			int row_no=0;
			print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPointsHeader " + "POINTS TABLE" + ";");
			print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPointsSubHeader " + match.getSetup().getTournament().toUpperCase() + ";");
			//print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgTLogo " + logo_path + "TLogo" + CricketUtil.PNG_EXTENSION + ";");
			
			print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPointsStatHeadE " + "POINTS" + ";");
			print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPointsStatHeadF " + "NRR" + ";");

			for(int i = 0; i <= groupA.size() - 1 ; i++) {
				row_no = row_no + 1;
				if(groupA.get(i).getQualifiedStatus().trim().equalsIgnoreCase("")) {
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPointsTeamName0" + row_no + " " + 
							groupA.get(i).getTeamName().toUpperCase() + ";");
				}else {
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPointsTeamName0" + row_no + " " + "(Q) " + 
							groupA.get(i).getTeamName().toUpperCase() + ";");
				}
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPointsStatValue" + (row_no + 1) + "A" + " " + groupA.get(i).getPlayed() + ";");
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPointsStatValue" + (row_no + 1) + "B" + " " + groupA.get(i).getWon() + ";");
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPointsStatValue" + (row_no + 1) + "C" + " " + groupA.get(i).getLost() + ";");
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPointsStatValue" + (row_no + 1) + "D" + " " + groupA.get(i).getNoResult() + ";");
				DecimalFormat df = new DecimalFormat("0.000");
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPointsStatValue" + (row_no + 1) + "F" + " " + df.format(groupA.get(i).getNetRunRate()) + ";");
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPointsStatValue" + (row_no + 1) + "E" + " " + groupA.get(i).getPoints() + ";");

			}
			
			for(int i = 0; i <= groupB.size() - 1 ; i++) {
				row_no = row_no + 1;
				if(groupB.get(i).getQualifiedStatus().trim().equalsIgnoreCase("")) {
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPointsTeamName0" + row_no + " " + 
							groupB.get(i).getTeamName().toUpperCase() + ";");
				}else {
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPointsTeamName0" + row_no + " " + "(Q) " + 
							groupB.get(i).getTeamName().toUpperCase() + ";");
				}
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPointsStatValue" + (row_no + 1) + "A" + " " + groupB.get(i).getPlayed() + ";");
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPointsStatValue" + (row_no + 1) + "B" + " " + groupB.get(i).getWon() + ";");
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPointsStatValue" + (row_no + 1) + "C" + " " + groupB.get(i).getLost() + ";");
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPointsStatValue" + (row_no + 1) + "D" + " " + groupB.get(i).getNoResult() + ";");
				DecimalFormat df = new DecimalFormat("0.000");
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPointsStatValue" + (row_no + 1) + "F" + " " + df.format(groupB.get(i).getNetRunRate()) + ";");
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPointsStatValue" + (row_no + 1) + "E" + " " + groupB.get(i).getPoints() + ";");

			}
			
			print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW ON;");
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*CONTAINER SET ACTIVE 0;");
			if(which_graphics_onscreen == "BOWLINGCARD") {
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$BowlingCard_GRP*CONTAINER SET ACTIVE 0;");
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$PointsTable_GRP*CONTAINER SET ACTIVE 1;");
			}else if(which_graphics_onscreen == "SCORECARD") {
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$BattingCard_GRP*CONTAINER SET ACTIVE 0;");
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$PointsTable_GRP*CONTAINER SET ACTIVE 1;");
			}else if(which_graphics_onscreen == "SUMMARY") {
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$Summary_GRP*CONTAINER SET ACTIVE 0;");
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$PointsTable_GRP*CONTAINER SET ACTIVE 1;");
			}
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In STOP;");
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out STOP;");
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 30.0;");
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*PointsTableIn SHOW 97.0;");
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out SHOW 0.0;");
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*PointsTableOut SHOW 0.0;");
			print_writer.println("LAYER1*EVEREST*GLOBAL SNAPSHOT_PATH C:/Temp/Preview.png;");
			print_writer.println("LAYER1*EVEREST*GLOBAL SNAPSHOT 1920 1080;");
			TimeUnit.SECONDS.sleep(1);
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*PointsTableOut SHOW 0.0;");
			
			if(which_graphics_onscreen == "BOWLINGCARD" || which_graphics_onscreen == "SCORECARD" || which_graphics_onscreen == "SUMMARY") {
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 30.0;");
			}else {
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 0.0;");
			}
			
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out SHOW 0.0;");
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*PointsTableIn SHOW 0.0;");
			
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*CONTAINER SET ACTIVE 1;");
			if(which_graphics_onscreen == "BOWLINGCARD") {
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$BowlingCard_GRP*CONTAINER SET ACTIVE 1;");
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$PointsTable_GRP*CONTAINER SET ACTIVE 0;");
			}else if(which_graphics_onscreen == "SCORECARD") {
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$BattingCard_GRP*CONTAINER SET ACTIVE 1;");
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$PointsTable_GRP*CONTAINER SET ACTIVE 0;");
			}else if(which_graphics_onscreen == "SUMMARY") {
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$Summary_GRP*CONTAINER SET ACTIVE 1;");
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$PointsTable_GRP*CONTAINER SET ACTIVE 0;");
			}
			print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW OFF;");
			this.status = CricketUtil.SUCCESSFUL;
			break;
		}
		
	}
	public void populateLtPointsTable(PrintWriter print_writer,String viz_sence_path,List<LeagueTeam> point_table, MatchAllData match, String selectedbroadcaster) throws InterruptedException 
	{
		int row_id=0;
		for(int i = 0; i <= point_table.size()-1; i++) {
			row_id = row_id + 1;
			//System.out.println(point_table.size());
			print_writer.println("-1 RENDERER*TREE*$Main$All$Points_table$RowAll$Table$Row1$RowAni$Data$BowlerName*GEOM*TEXT SET "+ point_table.get(0).getTeamName().toUpperCase() + " \0");
			if(point_table.get(i).getQualifiedStatus().trim().equalsIgnoreCase("")) {
				print_writer.println("-1 RENDERER*TREE*$Main$All$Points_table$RowAll$Table$Row" + row_id + "$RowAni$Qualified*ACTIVE SET 0 \0");
				print_writer.println("-1 RENDERER*TREE*$Main$All$Points_table$RowAll$Table$Row" + row_id + "$RowAni$Qualified*FUNCTION*Omo*vis_con SET 0 \0");
			}
			else {
				print_writer.println("-1 RENDERER*TREE*$Main$All$Points_table$RowAll$Table$Row" + row_id + "$RowAni$Qualified*ACTIVE SET 1 \0");
				print_writer.println("-1 RENDERER*TREE*$Main$All$Points_table$RowAll$Table$Row" + row_id + "$RowAni$Qualified*FUNCTION*Omo*vis_con SET 1 \0");
			}
			//System.out.println(match.getHomeTeam().getShortname().toUpperCase());
			//System.out.println(match.getAwayTeam().getShortname().toUpperCase());
			if(match.getSetup().getHomeTeam().getTeamName4().toUpperCase().equalsIgnoreCase(point_table.get(i).getTeamName().toUpperCase()) || 
					match.getSetup().getAwayTeam().getTeamName4().toUpperCase().equalsIgnoreCase(point_table.get(i).getTeamName().toUpperCase())) {
				print_writer.println("-1 RENDERER*TREE*$Main$All$Points_table$RowAll$Table$Row" + row_id + "$RowAni$Highlight*ACTIVE SET 1 \0");
				print_writer.println("-1 RENDERER*TREE*$Main$All$Points_table$RowAll$Table$Row" + row_id + "$RowAni$Highlight*FUNCTION*Omo*vis_con SET 1 \0");
			}
			else {
				print_writer.println("-1 RENDERER*TREE*$Main$All$Points_table$RowAll$Table$Row" + row_id + "$RowAni$Highlight*ACTIVE SET 0 \0");
				print_writer.println("-1 RENDERER*TREE*$Main$All$Points_table$RowAll$Table$Row" + row_id + "$RowAni$Highlight*FUNCTION*Omo*vis_con SET 0 \0");
			}
			print_writer.println("-1 RENDERER*TREE*$Main$All$Points_table$RowAll$Table$Row" + row_id + "$RowAni$Data$BowlerName*GEOM*TEXT SET "+ point_table.get(i).getTeamName().toUpperCase() + " \0");
			print_writer.println("-1 RENDERER*TREE*$Main$All$Points_table$RowAll$Table$Row" + row_id + "$RowAni$Data$ScoreGrp$OversValue*GEOM*TEXT SET "+ point_table.get(i).getPlayed() + " \0");
			print_writer.println("-1 RENDERER*TREE*$Main$All$Points_table$RowAll$Table$Row" + row_id + "$RowAni$Data$ScoreGrp$MaidensValue*GEOM*TEXT SET "+ point_table.get(i).getWon() + " \0");
			print_writer.println("-1 RENDERER*TREE*$Main$All$Points_table$RowAll$Table$Row" + row_id + "$RowAni$Data$ScoreGrp$RunsValue*GEOM*TEXT SET "+ point_table.get(i).getLost() + " \0");
			print_writer.println("-1 RENDERER*TREE*$Main$All$Points_table$RowAll$Table$Row" + row_id + "$RowAni$Data$ScoreGrp$EconomyValue*GEOM*TEXT SET "+ point_table.get(i).getPoints() + " \0");

			
			//print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStatValue0" + row_id + "A " + point_table.get(i).getTeamName().toUpperCase() + ";");
			//print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStatValue0" + row_id + "B " + point_table.get(i).getPlayed() + ";");
			//print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStatValue0" + row_id + "C " + point_table.get(i).getWon() + ";");
			//print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStatValue0" + row_id + "D " + point_table.get(i).getLost() + ";");
			
			//print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStatValue0" + row_id + "H " + point_table.get(i).getPoints() + ";");

		}
		this.status = CricketUtil.SUCCESSFUL;	
	}
	public void populateBowlerStyle(PrintWriter print_writer,String viz_scene, int whichInning, int playerId,List<Player> plyr, List<Team> team, MatchAllData match, String session_selected_broadcaster) throws InterruptedException
	{
		switch (session_selected_broadcaster.toUpperCase()) {
		case "FRUIT":
			if (match == null) {
				this.status = "ERROR: Match is null";
			} else if (match.getMatch().getInning() == null) {
				this.status = "ERROR: Bowler Style's inning is null";
			} else {
				for(Player plr : plyr) {
					if(plr.getPlayerId() ==playerId) {

						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgTeamLogo " + logo_path + team.get(plr.getTeamId() - 1).
								getTeamName4() + CricketUtil.PNG_EXTENSION + ";");
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgTLogo " + logo_path + "TLogo" + 
								CricketUtil.PNG_EXTENSION + ";");
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFirstName " + plr.getFull_name() +";");
						if(plr.getBowlingStyle() != null) {
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tDetails " + CricketFunctions.getbowlingstyle(plr.getBowlingStyle()).toUpperCase() + ";");
						}else {
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tDetails " + " " + ";");
						}
					}
				}
				/*
				 * print_writer.
				 * println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgTeamLogo " +
				 * logo_path + team.get(plyr.get(playerId - 1).getTeamId() - 1). getTeamName4()
				 * + CricketUtil.PNG_EXTENSION + ";");
				 * 
				 * print_writer.
				 * println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFirstName " +
				 * plyr.get(playerId - 1).getFull_name().toUpperCase() +";");
				 * 
				 * if(plyr.get(playerId - 1).getBowlingStyle() != null) { print_writer.
				 * println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tDetails " +
				 * CricketFunctions.getbowlingstyle(plyr.get(playerId -
				 * 1).getBowlingStyle()).toUpperCase() + ";"); }else { print_writer.
				 * println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tDetails " +
				 * " " + ";"); }
				 */
			}
			
			print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW ON;");
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*CONTAINER SET ACTIVE 0;");
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In STOP;");
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out STOP;");
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 197.0;");
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out SHOW 0.0;");
			print_writer.println("LAYER1*EVEREST*GLOBAL SNAPSHOT_PATH C:/Temp/Preview.png;");
			print_writer.println("LAYER1*EVEREST*GLOBAL SNAPSHOT 1920 1080;");
			TimeUnit.SECONDS.sleep(1);
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out SHOW 0.0;");
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 0.0;");
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*CONTAINER SET ACTIVE 1;");
			print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW OFF;");
			
			this.status = CricketUtil.SUCCESSFUL;
			break;
		}
	}	
	public void populateTieIdDouble(PrintWriter print_writer,String viz_sence_path,String day,List<Fixture> fix,List<Team>team,MatchAllData match, String selectedbroadcaster) throws InterruptedException 
	{
		if (match == null) {
			this.status = "ERROR: Match is null";
		} else {
			int row_id = 1;
			String Date = "";
			print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgTeamLogo " + logo_path + "TLogo" + CricketUtil.PNG_EXTENSION + ";");
			print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBatSubHeader " + match.getSetup().getTournament().toUpperCase() +";");
			Calendar cal = Calendar.getInstance();
			
			if(day.toUpperCase().equalsIgnoreCase("TODAY")) {
				Date =  new SimpleDateFormat("dd-MM-yyyy").format(cal.getTime());
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBatTeamName " + "TODAY MATCHES " +";");
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeamFirstName " + "TODAY" +";");
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeamLastName " + "TODAY" +";");

			}
			else if(day.toUpperCase().equalsIgnoreCase("TOMORROW")) {
				cal.add(Calendar.DATE, +1);
				Date =  new SimpleDateFormat("dd-MM-yyyy").format(cal.getTime());
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBatTeamName " + "TOMORROW MATCHES " +";");
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeamFirstName " + "TOMORROW" +";");
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeamLastName " + "TOMORROW" +";");

			}
			print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgTLogo " + logo_path + "TLogo" + 
					CricketUtil.PNG_EXTENSION + ";");
			//System.out.println(day.compareTo(fix.get(0).getDate()) + 1); // want it to check which day match is this
			for(int i = 0; i <= fix.size()-1; i++) {
				if(fix.get(i).getDate().equalsIgnoreCase(Date)) {
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgHomeTeamLogo0" +row_id + " " + logo_path + team.get(fix.get(i).getHometeamid()-1).getTeamName4().toUpperCase() 
							 + CricketUtil.PNG_EXTENSION +";");
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomeTeam0" +row_id + " " + team.get(fix.get(i).getHometeamid()-1).getTeamName1().toUpperCase() +";");
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tMatch" + row_id + " " + "MATCH " + fix.get(i).getMatchnumber() +";");
					
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgAwayTeamLogo0" +row_id+ " " + logo_path + team.get(fix.get(i).getAwayteamid()-1).getTeamName4().toUpperCase() 
							 + CricketUtil.PNG_EXTENSION +";");
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tAwayTeam0" +row_id+ " " + team.get(fix.get(i).getAwayteamid()-1).getTeamName1().toUpperCase() +";");
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tMatch" + row_id + " " + "MATCH " +fix.get(i).getMatchnumber() +";");
				 row_id = row_id +1;
				}
			}
			//print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFooter " + "LIVE FROM "+ match.getVenueName().toUpperCase() +";");
			print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFooter " + "" + ";");
		print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW ON;");
		print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In STOP;");
		print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out STOP;");
		print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 70.0;");
		print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out SHOW 0.0;");
		print_writer.println("LAYER1*EVEREST*GLOBAL SNAPSHOT_PATH C:/Temp/Preview.png;");
		print_writer.println("LAYER1*EVEREST*GLOBAL SNAPSHOT 1920 1080;");
		TimeUnit.SECONDS.sleep(1);
		print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out SHOW 0.0;");
		print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 0.0;");
		print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW OFF;");
			
		this.status = CricketUtil.SUCCESSFUL;	
		}
	}
	public void populatePreviousSummary(PrintWriter print_writer, String viz_scene, int match_number,List<MatchAllData> mtch,List<Fixture> fix, MatchAllData match, String session_selected_broadcaster) throws InterruptedException
	{
		switch (session_selected_broadcaster.toUpperCase()) {
		case "FRUIT":
			if (match == null) {
				this.status = "ERROR: Match is null";
			} else if (match.getMatch().getInning() == null) {
				this.status = "ERROR: Match Summary's inning is null";
			} else {
				for(int j = 0; j <= mtch.size() - 1; j++) {
					//String path = "D:\\Everest_Scenes\\Mumbai_Indians\\Textures and Images\\Logos\\" ;
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$BattingCard_GRP*CONTAINER SET ACTIVE 0;");
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$BowlingCard_GRP*CONTAINER SET ACTIVE 0;");
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$Summary_GRP*CONTAINER SET ACTIVE 1;");
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$PointsTable_GRP*CONTAINER SET ACTIVE 0;");
					
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSumHeader " + "MATCH SUMMARY" + ";");
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSumSubHeader " + mtch.get(j).getSetup().getMatchIdent().toUpperCase() + ";");
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgHomeTeamLogo " + logo_path + mtch.get(j).getSetup().getHomeTeam().getTeamName4() + CricketUtil.PNG_EXTENSION + ";");
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgAwayTeamLogo " + logo_path + mtch.get(j).getSetup().getAwayTeam().getTeamName4() + CricketUtil.PNG_EXTENSION + ";");
					
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$Summary_GRP$DataAll$HeaderGrp$BigBandGrp$Sponsor*CONTAINER SET ACTIVE 0;");
					
					int row_id = 0, row = 0, max_Strap = 0;
					String teamname = ""; 
					
//					for(int i = 1; i <= 2 ; i++) {
//						if(i == 1) {
//							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$Summary_GRP$DataAll$BAtting$Summary_Card$SecondInnings*CONTAINER SET ACTIVE 0;");
//						}
//						else {
//							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$Summary_GRP$DataAll$BAtting$Summary_Card$SecondInnings*CONTAINER SET ACTIVE 1;");
//						}
//					}
					
					if(mtch.get(j).getSetup().getMatchType().equalsIgnoreCase(CricketUtil.DT20)||mtch.get(j).getSetup().getMatchType().equalsIgnoreCase(CricketUtil.ODI)||
							mtch.get(j).getSetup().getMatchType().equalsIgnoreCase(CricketUtil.IT20)||mtch.get(j).getSetup().getMatchType().equalsIgnoreCase(CricketUtil.D10)) {
						
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vInnings " + "0" + ";");
					}
//					else if(mtch.get(j).getMatchType().equalsIgnoreCase(CricketUtil.TEST)) {
//						if(whichInning == 2) {
//							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vInnings " + "0" + ";");
//						}else if(whichInning == 3) {
//							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vInnings " + "1" + ";");
//						}else if (whichInning == 4) {
//							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vInnings " + "2" + ";");
//						}
//						
//					}
					
					for(int i = 1; i <= 2 ; i++) {

						if(i == 1) {
							row = 0;
							row_id = 0;
							max_Strap = 3;
						} else {
							row = 1;
							row_id = 3;
							max_Strap = 6;
						}
						row = row + 1;
						if(mtch.get(j).getMatch().getInning().get(i-1).getBattingTeamId() == mtch.get(j).getSetup().getHomeTeamId()) {
							teamname = mtch.get(j).getSetup().getHomeTeam().getTeamName1().toUpperCase();	
						} else {
							teamname = mtch.get(j).getSetup().getAwayTeam().getTeamName1().toUpperCase();
						}
						
						if(mtch.get(j).getSetup().getTossWinningTeam() == mtch.get(j).getMatch().getInning().get(0).getBattingTeamId()) {
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vHomeToss 1;");
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vAwayToss 0;");
							
//							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$Summary_GRP$DataAll$BAtting$Summary_Card$Main$First_two$FirstInnings$1st$InningsGrp*CONTAINER SET ACTIVE 1;");
//							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$Summary_GRP$DataAll$BAtting$Summary_Card$Main$First_two$SecondInnings$5$InningsGrp*CONTAINER SET ACTIVE 0;");
//							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInnOrToss1 " + "TOSS" + ";");
//							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInningHead2 " + "" + ";");
						}else {
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vHomeToss 0;");
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vAwayToss 1;");
							
//							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$Summary_GRP$DataAll$BAtting$Summary_Card$Main$First_two$FirstInnings$1st$InningsGrp*CONTAINER SET ACTIVE 0;");
//							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$Summary_GRP$DataAll$BAtting$Summary_Card$Main$First_two$SecondInnings$5$InningsGrp*CONTAINER SET ACTIVE 1;");
//							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInnOrToss2 " + "TOSS" + ";");
//							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInningHead1 " + "" + ";");
						}
						
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSumTeamName0"+ row +" "+ teamname + ";");
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSumOverNo0"+ row + " " + 
								CricketFunctions.OverBalls(mtch.get(j).getMatch().getInning().get(i-1).getTotalOvers(),mtch.get(j).getMatch().getInning().get(i-1).getTotalBalls()) + ";");
						
						if(mtch.get(j).getMatch().getInning().get(i-1).getTotalWickets() >= 10) {
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSumScore0"+ row + " " + mtch.get(j).getMatch().getInning().get(i-1).getTotalRuns() + ";");
						}
						else {
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSumScore0"+ row + " " + mtch.get(j).getMatch().getInning().get(i-1).getTotalRuns() + slashOrDash + mtch.get(j).getMatch().getInning().get(i-1).getTotalWickets() + ";");	
						}
						if(mtch.get(j).getMatch().getInning().get(i-1).getBattingCard() != null) {
							//row_id = 0;
							Collections.sort(mtch.get(j).getMatch().getInning().get(i-1).getBattingCard(),new CricketFunctions.BatsmenScoreComparator());
							for(BattingCard bc : mtch.get(j).getMatch().getInning().get(i-1).getBattingCard()) {
								if(bc.getBatsmanInningStarted() != null && bc.getBatsmanInningStarted().equalsIgnoreCase(CricketUtil.YES) && bc.getStatus().equalsIgnoreCase(CricketUtil.STILL_TO_BAT)) {
									row_id = row_id + 1;
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$Summary_GRP$DataAll$SumBG$Summary_Card$Batsman0"+ row_id + "*CONTAINER SET ACTIVE 1;");
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSumBatsmanName0"+ row_id + " " + bc.getPlayer().getTicker_name() + " (rh)" + ";");
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSumBatsmanRuns0"+ row_id + " " + bc.getRuns() + ";");
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSumBatsmanBalls0"+ row_id + " " + bc.getBalls() + ";");
								}
								if(!bc.getStatus().toUpperCase().equalsIgnoreCase(CricketUtil.STILL_TO_BAT)) {
									row_id = row_id + 1;
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$Summary_GRP$DataAll$BAtting$Summary_Card$Batsman0"+ row_id + "*CONTAINER SET ACTIVE 1;");
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSumBatsmanName0"+ row_id + " " + bc.getPlayer().getTicker_name() + ";");
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSumBatsmanRuns0"+ row_id + " " + bc.getRuns() + ";");
									
									if(bc.getStatus().toUpperCase().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
										print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSumBatsmanNotOut0"+ row_id + " " + "*" + ";");
										
									}
									if(bc.getStatus().toUpperCase().equalsIgnoreCase(CricketUtil.OUT)) {
										print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSumBatsmanNotOut0"+ row_id + " " + " " + ";");
										
									} 
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSumBatsmanBalls0"+ row_id + " " + bc.getBalls() + ";");
									
									if(i == 1 && row_id >= 3) {
										break;
									}else if(i == 4 && row_id >= 6) {
										break;
									}
								}
							}
						}

						for(int k = row_id + 1; k <= max_Strap; k++) {
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$Summary_GRP$DataAll$SumBG$Summary_Card$Batsman0"+ k + "*CONTAINER SET ACTIVE 0;");
						}
						
						if(i == 1) {
							row_id = 0;
						}
						else {
							row_id = 3;
						}

						if(mtch.get(j).getMatch().getInning().get(i-1).getBowlingCard() != null) {
							//row_id = 0;
							Collections.sort(mtch.get(j).getMatch().getInning().get(i-1).getBowlingCard(),new CricketFunctions.BowlerFiguresComparator());

							for(BowlingCard boc : mtch.get(j).getMatch().getInning().get(i-1).getBowlingCard()) {
								
								row_id = row_id + 1;
								
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$Summary_GRP$DataAll$SumBG$Summary_Card$Bowler0"+ row_id + "*CONTAINER SET ACTIVE 1;");
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSumBowlerName0" + row_id + " " +  boc.getPlayer().getTicker_name() + ";");
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSumBowlerRuns0" + row_id + " " + boc.getWickets() + slashOrDash + boc.getRuns() + ";");
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSumBowlerOver0" + row_id + " " + CricketFunctions.OverBalls(boc.getOvers(), boc.getBalls()) + ";");
								
								if(i == 1 && row_id >= 3) {
									break;
								}
								else if(i == 4 && row_id >= 6) {
									break;
								}
							}
						}
						
						for(int k = row_id + 1; k <= max_Strap; k++) {
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$Summary_GRP$DataAll$SumBG$Summary_Card$Bowler0"+ k + "*CONTAINER SET ACTIVE 0;");
						}
					}
					
					if(mtch.get(j).getMatch().getMatchResult() != null) {
						if(mtch.get(j).getMatch().getMatchResult().toUpperCase().equalsIgnoreCase("DRAWN")){
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSumInfo " + "MATCH TIED" + ";");
						}
						else if(mtch.get(j).getMatch().getMatchResult().split(",")[1].toUpperCase().equalsIgnoreCase("SUPER_OVER")){
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSumInfo " + "MATCH TIED - " + mtch.get(j).getMatch().getMatchStatus().toUpperCase() + ";");
						}
						else {
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSumInfo " + mtch.get(j).getMatch().getMatchStatus().toUpperCase() + ";");
						}
					}
					else {
						if(mtch.get(j).getSetup().getTargetType() == "") {
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSumInfo " + 
									mtch.get(j).getMatch().getMatchStatus().toUpperCase() + ";");
						}
						else if(mtch.get(j).getSetup().getTargetType() != null) {
							if(mtch.get(j).getSetup().getTargetType().toUpperCase().equalsIgnoreCase("VJD")) {
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSumInfo " + 
										mtch.get(j).getMatch().getMatchStatus().toUpperCase() + " (VJD)" + ";");
							}
							else if(mtch.get(j).getSetup().getTargetType().toUpperCase().equalsIgnoreCase("DLS")) {
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSumInfo " + 
										mtch.get(j).getMatch().getMatchStatus().toUpperCase() + " (DLS)" + ";");
							}
						}
						//.trim().equalsIgnoreCase("")
						
					}
					print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW ON;");
					print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In STOP;");
					print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out STOP;");
					print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 30.0;");
					print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*SummaryIn SHOW 109.0;");
					print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out SHOW 0.0;");
					print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*SummaryOut SHOW 0.0;");
					print_writer.println("LAYER1*EVEREST*GLOBAL SNAPSHOT_PATH C:/Temp/Preview.png;");
					print_writer.println("LAYER1*EVEREST*GLOBAL SNAPSHOT 1920 1080;");
					TimeUnit.SECONDS.sleep(1);
					print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out SHOW 0.0;");
					print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*SummaryOut SHOW 0.0;");
					print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 0.0;");
					print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*SummaryIn SHOW 0.0;");
					print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW OFF;");
				}
			}
			this.status = CricketUtil.SUCCESSFUL;
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
	public void populateBatsmanStyle(PrintWriter print_writer,String viz_scene, int whichInning, int playerId, List<Player> plyr, List<Team> team, MatchAllData match, String session_selected_broadcaster) throws InterruptedException
	{
		switch (session_selected_broadcaster.toUpperCase()) {
		case "FRUIT":
			if (match == null) {
				this.status = "ERROR: Match is null";
			} else if (match.getMatch().getInning() == null) {
				this.status = "ERROR: Equation's inning is null";
			} else {
				for(Player plr : plyr) {
					if(plr.getPlayerId() ==playerId) {

						//print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgTeamLogo " + sponsor_logo_path + "IOCL" + CricketUtil.PNG_EXTENSION + ";");
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgTeamLogo " + logo_path + team.get(plr.getTeamId()-1).getTeamName4() + 
								CricketUtil.PNG_EXTENSION + ";");
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFirstName " + plr.getFull_name() +";");
						if(plr.getBattingStyle() != null) {
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tDetails " + 
									CricketFunctions.getbattingstyle(plr.getBattingStyle(), CricketUtil.FULL, false, false).toUpperCase() + ";");
						}else {
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tDetails " + " " + ";");
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
			TimeUnit.SECONDS.sleep(1);
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out SHOW 0.0;");
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 0.0;");
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*CONTAINER SET ACTIVE 1;");
			print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW OFF;");
			
			this.status = CricketUtil.SUCCESSFUL;
			
			break;
		}
	}
	public void populateGeneric(PrintWriter print_writer,String viz_scene,String Stats, MatchAllData match, String session_selected_broadcaster) throws InterruptedException 
	{
		switch (session_selected_broadcaster.toUpperCase()) {
		case "FRUIT":
			if (match == null) {
				this.status = "ERROR: Match is null";
			} else if (match.getMatch().getInning() == null) {
				this.status = "ERROR: Generic's inning is null";
			} else {
				
				for(Inning inn : match.getMatch().getInning()) {
					if(inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)) {
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgTeamLogo " + logo_path + inn.getBatting_team().getTeamName4() + CricketUtil.PNG_EXTENSION + ";");
						
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeamName " + inn.getBatting_team().getTeamName1() + ";");
						if(inn.getTotalWickets() >= 10) {
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tScore " + inn.getTotalRuns() + ";");
						}else {
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tScore " + inn.getTotalRuns() + "-" + inn.getTotalWickets() + ";");
						}
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tOvers " + CricketFunctions.OverBalls(inn.getTotalOvers(), inn.getTotalBalls()) + ";");
						
						for(BattingCard bc : inn.getBattingCard()) {
							if(bc.getStatus().toUpperCase().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
								if(bc.getOnStrike().equalsIgnoreCase(CricketUtil.YES)) {
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBatsmanName01 " + 
															bc.getPlayer().getTicker_name() + ";");
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBatsmanRun01 " + 
															bc.getRuns() + ";");
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBatsmanBall01 " + 
															"(" + bc.getBalls() + ")" + ";");
								}
								else {
									print_writer.println("v*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBatsmanName02 " + 
											bc.getPlayer().getTicker_name() + ";");
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBatsmanRun02 " + 
															bc.getRuns() + ";");
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBatsmanBall02 " + 
											"(" + bc.getBalls() + ")"+ ";");
								}
								if(bc.getOnStrike().equalsIgnoreCase(CricketUtil.NO)) {
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBatsmanName02 " + 
															bc.getPlayer().getTicker_name() + ";");
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBatsmanRun02 " + 
															bc.getRuns() + ";");
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBatsmanBall02 " + 
															"(" + bc.getBalls() + ")"+ ";");
								}
								else {
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBatsmanName01 " + 
											bc.getPlayer().getTicker_name() + ";");
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBatsmanRun01 " + 
															bc.getRuns() + ";");
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBatsmanBall01 " + 
											"(" + bc.getBalls() + ")" + ";");
								}
							}
						}
						
						switch(Stats.toUpperCase()) {
						case "BOUNDARIES":
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBottomInfo " + "BOUNDARIES : " + 
									inn.getTotalFours() + " FOURS   " + inn.getTotalSixes() + " SIXES   " + ";");
							break;
						case "RUNS_BALLS":	
							if(match.getMatch().getMatchResult() != null) {
								if(match.getMatch().getMatchResult().toUpperCase().equalsIgnoreCase("")) {
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBottomInfo " + 
											CricketFunctions.generateMatchSummaryStatus(2, match, CricketUtil.FULL).toUpperCase() + ";");
								}
								else if(match.getMatch().getMatchResult().toUpperCase().equalsIgnoreCase("DRAWN")){
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBottomInfo " + "MATCH TIED" + ";");
								}
								else if(match.getMatch().getMatchResult().split(",")[1].toUpperCase().equalsIgnoreCase("SUPER_OVER")){
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBottomInfo " + "MATCH TIED - " + match.getMatch().getMatchStatus().toUpperCase() + ";");
								}
								else {
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBottomInfo " + 
											CricketFunctions.generateMatchSummaryStatus(2, match, CricketUtil.FULL).toUpperCase() + ";");
									//print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tResult " + match.getMatchStatus().toUpperCase() + ";");
								}
							}
							else {
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBottomInfo " + 
										CricketFunctions.generateMatchSummaryStatus(2, match, CricketUtil.FULL).toUpperCase() + ";");
								//print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tResult " + match.getMatchStatus().toUpperCase() + ";");
								
								if(match.getSetup().getTargetType() != null) {
									if(match.getSetup().getTargetType().toUpperCase().equalsIgnoreCase("VJD")) {
										print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBottomInfo " + 
												CricketFunctions.generateMatchSummaryStatus(2, match, CricketUtil.FULL).toUpperCase() + " (VJD)" + ";");
									}else if(match.getSetup().getTargetType().toUpperCase().equalsIgnoreCase("DLS")){
										print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBottomInfo " + 
												CricketFunctions.generateMatchSummaryStatus(2, match, CricketUtil.FULL).toUpperCase() + " (DLS)" + ";");
									}
								}
							}
							break;
						case "PARTNERSHIP":
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBottomInfo " + "CURRENT PARTNERSHIP : " + inn.getPartnerships().get(inn.getPartnerships().size() - 1).getTotalRuns() 
									+ " RUNS OFF " + inn.getPartnerships().get(inn.getPartnerships().size() - 1).getTotalBalls() + " BALLS" + ";");
							break;
						case "CURRENT_RUN_RATE":
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBottomInfo " + "CURRENT RUN RATE : " + inn.getRunRate() + ";");
							break;
						case "REQUIRED_RUN_RATE":
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBottomInfo " + "REQUIRED RUN RATE : " + 
									CricketFunctions.generateRunRate(CricketFunctions.getRequiredRuns(match), 0, CricketFunctions.getRequiredBalls(match), 2) + ";");
							break;
						case "COMPARISION":
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBottomInfo " + "AT THIS STAGE : " + match.getMatch().getInning().get(0).getBatting_team().getTeamName1().toUpperCase() + 
											" " + CricketFunctions.compareInningData(match,"-", 1 , match.getEventFile().getEvents()) + ";");
							break;
						case "LAST_WICKET":
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBottomInfo " + "LAST WICKET : " + CricketFunctions.getLastWicket(match) + ";");
							break;
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
	public void populateManhattan(PrintWriter print_writer,String viz_scene, int whichInning, MatchAllData match, String session_selected_broadcaster) throws InterruptedException
	{
		switch (session_selected_broadcaster.toUpperCase()) {
		case "FRUIT":
			if (match == null) {
				this.status = "ERROR: Match is null";
			} else if(whichInning == 0) {
				this.status = "ERROR: Inning is null";
			}else {
				
				//String cumm_runs1 = "", cumm_runs2 = "";
				int maxRuns = 0,runsIncr = 0;
				
//				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeamFirstName " + match.getMatchIdent().toUpperCase() + ";");
//				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeamLastName " + match.getMatchIdent().toUpperCase() + ";");
//				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgTLogo " + logo_path + "TLogo" + 
//						CricketUtil.PNG_EXTENSION + ";");
				for(Inning inn : match.getMatch().getInning()) {
					if (inn.getInningNumber() == whichInning) {
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgTeamLogo " + logo_path + inn.getBatting_team().getTeamName4() + 
								CricketUtil.PNG_EXTENSION + ";");
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeamName " + inn.getBatting_team().getTeamName1().toUpperCase() + ";");
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSubHeader " + "v " + inn.getBowling_team().getTeamName1().toUpperCase() + ";");
					}
				}
				
				List<String> overByOverRuns = new ArrayList<String>();
//				List<String> overByOverRuns1 = new ArrayList<String>();
//				List<String> overByOverRuns2 = new ArrayList<String>();
				
				for(OverByOverData Over : CricketFunctions.getOverByOverData(match,whichInning,"MANHATTAN",match.getEventFile().getEvents())) {
					overByOverRuns.add(String.valueOf(Over.getOverTotalRuns()));
				}
				
//				for(OverByOverData Over : CricketFunctions.getOverByOverData(match,whichInning,"MANHATTAN",match.getEvents())) {
//					
//					if(bar <=25 ) {
//						overByOverRuns1.add(String.valueOf(Over.getOverTotalRuns()));
//						cumm_runs1 = String.join("#", overByOverRuns1);
//						
//					}else if(bar>25) {
//						overByOverRuns2.add(String.valueOf(Over.getOverTotalRuns()));
//						cumm_runs2 = String.join("#", overByOverRuns2);
//						
//					}
//					bar = bar + 1;
//				}
				
				String cumm_runs = String.join("#", overByOverRuns); // Store Per Overs Runs
				for(String str : cumm_runs.split("#"))   // For Find MaxRun
				{
					if(maxRuns < Integer.valueOf(str))
					{
						maxRuns = Integer.valueOf(str);	
					}
					while (maxRuns % 4 != 0) {     // 5 label in y-axis
						maxRuns = maxRuns + 1;    // keep incrementing till max runs is divisible by 5. maxRuns = 35
					}
				}
				
				for(int i =0; i < 4;i++) {
					runsIncr = maxRuns / 4; // 35/5=7 -> Y axis will be plot like 6,12,18,24,30 & 36	
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET TxtScore" + (4 - i) + " " + runsIncr*(i+1) + ";");
				}
				
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$DataAll$BG$Data$Manhatten$Main$Graph$Bars*GEOMETRY*BARS SET MAX_HEIGHT_VALUE " + maxRuns + ";");
//				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$DataAll$BG$Data$Manhatten$Main$Graph$Bars2*GEOMETRY*BARS SET MAX_HEIGHT_VALUE " + maxRuns + ";");
				
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$DataAll$BG$Data$Manhatten$Main$Graph$Bars*GEOMETRY*BARS SET DATA " + cumm_runs.replaceFirst("0#", "") + ";");
//				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$DataAll$BG$Data$Manhatten$Main$Graph$Bars2*GEOMETRY*BARS SET DATA " + cumm_runs2 + ";");
				
				for(int j=0; j <= match.getSetup().getMaxOvers(); j++) {
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$DataAll$BG$Data$Manhatten$Main$Wickets$Over_" + j +"*CONTAINER SET ACTIVE 0;");
					
					if(j < CricketFunctions.getOverByOverData(match,whichInning,"MANHATTAN", match.getEventFile().getEvents()).size()) {
						//System.out.println("Runs : " + j + " : " + CricketFunctions.getOverByOverData(match, whichInning, match.getEvents()).get(j).getOverTotalRuns());
						//System.out.println("Wickets : " + j + " : " + CricketFunctions.getOverByOverData(match,whichInning, match.getEvents()).get(j).getOverTotalWickets());
//						System.out.println("Wickets = " + CricketFunctions.getOverByOverData(match,whichInning,"MANHATTAN", match.getEvents()).get(j).getOverTotalWickets());
						if(j <= 50) {
							if(CricketFunctions.getOverByOverData(match,whichInning,"MANHATTAN", match.getEventFile().getEvents()).get(j).getOverTotalWickets() > 0) {
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Over_" + (j-1) + "*FUNCTION*TAG_POSITION SET MAX_VALUE " + maxRuns + ";");
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET Over_" + (j-1) + " " + 
										CricketFunctions.getOverByOverData(match, whichInning,"MANHATTAN", match.getEventFile().getEvents()).get(j).getOverTotalRuns() + ";");
								
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$DataAll$BG$Data$Manhatten$Main$Wickets$Over_" + (j-1) +"*CONTAINER SET ACTIVE 1;");
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET Over"+ (j-1) + " " + (Integer.valueOf(
										CricketFunctions.getOverByOverData(match,whichInning,"MANHATTAN", match.getEventFile().getEvents()).get(j).getOverTotalWickets()) - 1) + ";");
							}else {
								if(j==0) {
									
								}else {
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$DataAll$BG$Data$Manhatten$Main$Wickets$Over_" + (j-1) +"*CONTAINER SET ACTIVE 0;");
								}
							}
						}
						
					}
				}
				
				/*for(int j=0; j<=CricketFunctions.getOverByOverData(match,whichInning,"MANHATTAN", match.getEvents()).size()-1;j++) {
					//System.out.println("Runs : " + j + " : " + CricketFunctions.getOverByOverData(match, whichInning, match.getEvents()).get(j).getOverTotalRuns());
					//System.out.println("Wickets : " + j + " : " + CricketFunctions.getOverByOverData(match,whichInning, match.getEvents()).get(j).getOverTotalWickets());
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$DataAll$BAtting$Manhatten$Main$Wickets$Over_" + j +"*CONTAINER SET ACTIVE 0;");
					if(CricketFunctions.getOverByOverData(match,whichInning,"MANHATTAN", match.getEvents()).get(j).getOverTotalWickets() > 0) {
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Over_" + j + "*FUNCTION*TAG_POSITION SET MAX_VALUE " + maxRuns + ";");
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET Over_" + j + " " + 
								CricketFunctions.getOverByOverData(match, whichInning,"MANHATTAN", match.getEvents()).get(j).getOverTotalRuns() + ";");
						
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$DataAll$BAtting$Manhatten$Main$Wickets$Over_" + j +"*CONTAINER SET ACTIVE 1;");
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET Over"+ j + " " + (Integer.valueOf(
								CricketFunctions.getOverByOverData(match,whichInning,"MANHATTAN", match.getEvents()).get(j).getOverTotalWickets()) - 1) + ";");
					}else {
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$DataAll$BAtting$Manhatten$Main$Wickets$Over_" + j +"*CONTAINER SET ACTIVE 0;");
					}
				}*/	
			}
			print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW ON;");
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*CONTAINER SET ACTIVE 0;");
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In STOP;");
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out STOP;");
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 197.0;");
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out SHOW 0.0;");
			print_writer.println("LAYER1*EVEREST*GLOBAL SNAPSHOT_PATH C:/Temp/Preview.png;");
			print_writer.println("LAYER1*EVEREST*GLOBAL SNAPSHOT 1920 1080;");
			TimeUnit.SECONDS.sleep(1);
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out SHOW 0.0;");
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 0.0;");
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*CONTAINER SET ACTIVE 1;");
			print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW OFF;");
			
			this.status = CricketUtil.SUCCESSFUL;
			break;
		}
			
	}
	public void populateMostRuns(PrintWriter print_writer,String viz_scene,List<Tournament> tournament,MatchAllData match ,String session_selected_broadcaster) throws InterruptedException {
		switch (session_selected_broadcaster.toUpperCase()) {
		case "FRUIT":
			int row_no=0;
			
			Collections.sort(tournament,new CricketFunctions.BatsmenMostRunComparator());
			print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHeader " + "MOST RUNS" + ";");
			
			print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSubHeader " + match.getSetup().getTournament().toUpperCase() + ";");
			print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgTeamLogo " + logo_path + "TLogo" + CricketUtil.PNG_EXTENSION + ";");
			
			
			print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vRows " + "4" + ";");
			print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStatHead01 " + "PLAYER" + ";");
			print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStatHead02 " + "MATCHES" + ";");
			print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStatHead03 " + "RUNS" + ";");
			print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStatHead04 " + "S/R" + ";");
			
			for(int i = 0; i <= tournament.size() - 1 ; i++) {
				if(tournament.get(i).getRuns() > 0) {
					if(row_no < 10) {
						if(tournament.get(i).getRuns() > 0) {
							row_no = row_no + 1;
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vCoumms " + row_no + ";");
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStatValue0" + row_no + "A" + " " + tournament.get(i).getPlayer().getFull_name() + ";");
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStatValue0" + row_no + "B" + " " + tournament.get(i).getMatches() + ";");
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStatValue0" + row_no + "C" + " " + tournament.get(i).getRuns() + ";");
							if(tournament.get(i).getBallsFaced() >= 1) {
								DecimalFormat df = new DecimalFormat("0.0");

								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStatValue0" + row_no + "D" + " " + 
										df.format((100 * (double)tournament.get(i).getRuns()) / (double)tournament.get(i).getBallsFaced()) + ";");
							}
						}
					}else {
						break;
					}
				}
			}
			
			print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW ON;");
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In STOP;");
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out STOP;");
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 71.0;");
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out SHOW 0.0;");
			print_writer.println("LAYER1*EVEREST*GLOBAL SNAPSHOT_PATH C:/Temp/Preview.png;");
			print_writer.println("LAYER1*EVEREST*GLOBAL SNAPSHOT 1920 1080;");
			TimeUnit.SECONDS.sleep(1);
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out SHOW 0.0;");
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 0.0;");
			print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW OFF;");
			this.status = CricketUtil.SUCCESSFUL;
			break;
		}
	}
	public void populateMostWickets(PrintWriter print_writer,String viz_scene,List<Tournament> tournament_boc,MatchAllData match ,String session_selected_broadcaster) throws InterruptedException {
		switch (session_selected_broadcaster.toUpperCase()) {
		case "FRUIT":
			int row_no=0;
			
			Collections.sort(tournament_boc,new CricketFunctions.BowlerWicketsComparator());
			print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHeader " + "MOST WICKETS" + ";");
			
			print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSubHeader " + match.getSetup().getTournament().toUpperCase() + ";");
			print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgTeamLogo " + logo_path + "TLogo" + CricketUtil.PNG_EXTENSION + ";");
			
			
			print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vRows " + "4" + ";");
			print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStatHead01 " + "PLAYER" + ";");
			print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStatHead02 " + "MATCHES" + ";");
			print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStatHead03 " + "WICKETS" + ";");
			print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStatHead04 " + "ECON." + ";");
			
			for(int i = 0; i <= tournament_boc.size() - 1 ; i++) {
				//System.out.println("Name -" + tournament.get(i).getPlayer().getFull_name().toUpperCase() + " - Runs -" + tournament.get(i).getRuns());
				if(tournament_boc.get(i).getWickets() > 0) {
					if(row_no < 10) {
						if(tournament_boc.get(i).getWickets()>0) {
							row_no = row_no + 1;
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vCoumms " + row_no + ";");
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStatValue0" + row_no + "A" + " " + tournament_boc.get(i).getPlayer().getFull_name() + ";");
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStatValue0" + row_no + "B" + " " + tournament_boc.get(i).getMatches() + ";");
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStatValue0" + row_no + "C" + " " + tournament_boc.get(i).getWickets() + ";");
							if(tournament_boc.get(i).getBallsBowled() >= 1) {
								
								DecimalFormat df_b = new DecimalFormat("0.00");
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStatValue0" + row_no + "D" + " " + 
										df_b.format(((double)tournament_boc.get(i).getRunsConceded() / (double)tournament_boc.get(i).getBallsBowled())*6) + ";");
							}
						}
					}else {
						//print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStatValue0" + row_no + "D" + " " + "-" + ";");
						break;
					}
				}
				
			}
			
			print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW ON;");
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In STOP;");
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out STOP;");
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 71.0;");
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out SHOW 0.0;");
			print_writer.println("LAYER1*EVEREST*GLOBAL SNAPSHOT_PATH C:/Temp/Preview.png;");
			print_writer.println("LAYER1*EVEREST*GLOBAL SNAPSHOT 1920 1080;");
			TimeUnit.SECONDS.sleep(1);
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out SHOW 0.0;");
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 0.0;");
			print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW OFF;");
			this.status = CricketUtil.SUCCESSFUL;
			break;
		}
	}
	public void populateMostFours(PrintWriter print_writer,String viz_scene,List<Tournament> tournament_fours,MatchAllData match ,String session_selected_broadcaster) throws InterruptedException {
		switch (session_selected_broadcaster.toUpperCase()) {
		case "FRUIT":
			int row_no=0;
			
			//Collections.sort(tournament_fours);
			Collections.sort(tournament_fours,new CricketFunctions.BatsmanFoursComparator());
			print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSubHeader " + match.getSetup().getTournament().toUpperCase() + ";");
			print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgTeamLogo " + logo_path + "TLogo" + CricketUtil.PNG_EXTENSION + ";");
			
			print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHeader " + "MOST FOURS" + ";");
			
			print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vRows " + "3" + ";");
			print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStatHead01 " + "PLAYER" + ";");
			print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStatHead02 " + "MATCHES" + ";");
			print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStatHead03 " + "FOURS" + ";");
			//print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStatHead04 " + "ECON." + ";");
			
			for(int i = 0; i <= tournament_fours.size() - 1 ; i++) {
				//System.out.println("Name -" + tournament_fours.get(i).getPlayer().getFull_name().toUpperCase() + " - Fours -" + tournament_fours.get(i).getFours());
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vCoumms " + row_no + ";");
				if(tournament_fours.get(i).getFours() > 0) {
					if(row_no < 10) {
						if(tournament_fours.get(i).getFours()>0) {
							row_no = row_no + 1;
							
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStatValue0" + row_no + "A" + " " + tournament_fours.get(i).getPlayer().getFull_name() + ";");
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStatValue0" + row_no + "B" + " " + tournament_fours.get(i).getMatches() + ";");
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStatValue0" + row_no + "C" + " " + tournament_fours.get(i).getFours() + ";");
						}
					}	
				}
			}
			
			print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW ON;");
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In STOP;");
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out STOP;");
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 71.0;");
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out SHOW 0.0;");
			print_writer.println("LAYER1*EVEREST*GLOBAL SNAPSHOT_PATH C:/Temp/Preview.png;");
			print_writer.println("LAYER1*EVEREST*GLOBAL SNAPSHOT 1920 1080;");
			TimeUnit.SECONDS.sleep(1);
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out SHOW 0.0;");
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 0.0;");
			print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW OFF;");
			this.status = CricketUtil.SUCCESSFUL;
			break;
		}
	}
	public void populateMostSixes(PrintWriter print_writer,String viz_scene,List<Tournament> tournament_sixes,MatchAllData match ,String session_selected_broadcaster) throws InterruptedException {
		switch (session_selected_broadcaster.toUpperCase()) {
		case "FRUIT":
			int row_no=0;
			
			//Collections.sort(tournament_sixes);
			Collections.sort(tournament_sixes,new CricketFunctions.BatsmanSixesComparator());
			print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSubHeader " + match.getSetup().getTournament().toUpperCase() + ";");
			print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgTeamLogo " + logo_path + "TLogo" + CricketUtil.PNG_EXTENSION + ";");
			
			print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHeader " + "MOST SIXES" + ";");
			
			print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vRows " + "3" + ";");
			print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStatHead01 " + "PLAYER" + ";");
			print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStatHead02 " + "MATCHES" + ";");
			print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStatHead03 " + "SIXES" + ";");
			//print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStatHead04 " + "ECON." + ";");
			
			for(int i = 0; i <= tournament_sixes.size() - 1 ; i++) {
				//System.out.println("Name -" + tournament_sixes.get(i).getPlayer().getFull_name().toUpperCase() + " - Sixes -" + tournament_sixes.get(i).getSixes());
				if(tournament_sixes.get(i).getSixes() > 0) {
					if(row_no < 10) {
						if(tournament_sixes.get(i).getSixes()>0) {
							row_no = row_no + 1;
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vCoumms " + row_no + ";");
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStatValue0" + row_no + "A" + " " + tournament_sixes.get(i).getPlayer().getFull_name() + ";");
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStatValue0" + row_no + "B" + " " + tournament_sixes.get(i).getMatches() + ";");
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStatValue0" + row_no + "C" + " " + tournament_sixes.get(i).getSixes() + ";");
						}
					}
				}	
			}
			
			print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW ON;");
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In STOP;");
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out STOP;");
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 71.0;");
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out SHOW 0.0;");
			print_writer.println("LAYER1*EVEREST*GLOBAL SNAPSHOT_PATH C:/Temp/Preview.png;");
			print_writer.println("LAYER1*EVEREST*GLOBAL SNAPSHOT 1920 1080;");
			TimeUnit.SECONDS.sleep(1);
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out SHOW 0.0;");
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 0.0;");
			print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW OFF;");
			this.status = CricketUtil.SUCCESSFUL;
			break;
		}
	}
	public void populateHighestScore(PrintWriter print_writer,String viz_scene,List<Tournament> tournament_high_score,MatchAllData match ,String session_selected_broadcaster) throws InterruptedException {
		switch (session_selected_broadcaster.toUpperCase()) {
		case "FRUIT":
			int row_no = 0;
			List<BestStats> top_ten_beststats = new ArrayList<BestStats>();
			for(Tournament tourn : tournament_high_score) {
				for(BestStats bs : tourn.getBatsman_best_Stats()) {
					top_ten_beststats.add(bs);
				}
			}
			
			Collections.sort(top_ten_beststats, new CricketFunctions.PlayerBestStatsComparator());

			/*for(BestStats Top_ten_bs : top_ten_beststats) {
				System.out.println("Best Stats : " + Top_ten_bs);
			}*/
			print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBatSubHeader " + match.getSetup().getTournament().toUpperCase() + ";");
			print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgTeamLogo " + logo_path + "TLogo" + CricketUtil.PNG_EXTENSION + ";");
			print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgTLogo " + logo_path + "TLogo" + 
					CricketUtil.PNG_EXTENSION + ";");
			print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBatTeamName " + "HIGHEST INDIVIDUAL SCORE" + ";");
			
			print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeamFirstName " + match.getSetup().getMatchIdent().toUpperCase() + ";");
			print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeamLastName " + match.getSetup().getMatchIdent().toUpperCase() + ";");
			
			print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vRows " + "4" + ";");
			print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStatHead01 " + "PLAYER" + ";");
			print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStatHead02 " + "SCORE" + ";");
			print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStatHead03 " + "BALLS"+ ";");
			print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStatHead04 " + "OPPONENT TEAM" + ";");
			
			for(int i = 0; i <= top_ten_beststats.size() - 1 ; i++) {
				if(row_no < 10) {
					row_no = row_no + 1;

					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStatValue0" + row_no + "A" + " " + top_ten_beststats.get(i).getPlayer().getFull_name() + ";");
					if(top_ten_beststats.get(i).getBestEquation() % 2 == 0) {
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStatValue0" + row_no + "B" + " " + top_ten_beststats.get(i).getBestEquation() / 2 + ";");
					}else {
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStatValue0" + row_no + "B" + " " + 
								top_ten_beststats.get(i).getBestEquation() / 2  + "*" + ";");
					}
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStatValue0" + row_no + "C" + " " + top_ten_beststats.get(i).getBalls() + ";");
					
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStatValue0" + row_no + "D" + " " + 
							top_ten_beststats.get(i).getOpponentTeam().getTeamName4().toUpperCase() + ";");
					
				}	
			}
			
			print_writer.println("LAYER2*EVEREST*GLOBAL PREVIEW ON;");
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In STOP;");
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out STOP;");
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 196.0;");
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out SHOW 0.0;");
			print_writer.println("LAYER2*EVEREST*GLOBAL SNAPSHOT_PATH C:/Temp/Preview.png;");
			print_writer.println("LAYER2*EVEREST*GLOBAL SNAPSHOT 1920 1080;");
			TimeUnit.SECONDS.sleep(1);
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out SHOW 0.0;");
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 0.0;");
			print_writer.println("LAYER2*EVEREST*GLOBAL PREVIEW OFF;");
			this.status = CricketUtil.SUCCESSFUL;
			break;
		}
	}
	public void populateThisOver(PrintWriter print_writer,String viz_scene,MatchAllData match,String session_selected_broadcaster) throws InterruptedException 
	{
		switch (session_selected_broadcaster.toUpperCase()) {
			case "FRUIT":
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSubHeader " + "THIS OVER" + ";");
				for(Inning inn : match.getMatch().getInning()) {
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgTeamLogo " + logo_path + inn.getBowling_team().getTeamName4() + 
							CricketUtil.PNG_EXTENSION + ";");
					if (inn.getIsCurrentInning().toUpperCase().equalsIgnoreCase(CricketUtil.YES)) {
						for(BowlingCard boc : inn.getBowlingCard()) {
							if(boc.getStatus().toUpperCase().equalsIgnoreCase("CURRENTBOWLER") || boc.getStatus().toUpperCase().equalsIgnoreCase("LASTBOWLER")) {
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerName " + boc.getPlayer().getFull_name() + ";");
								//System.out.println("SIZE :" + CricketFunctions.getEventsText(CricketUtil.OVER,",", match.getEvents(),0).length());
								
								String arr[] = CricketFunctions.getEventsText(CricketUtil.OVER,0,",", match.getEventFile().getEvents(),0).split(",");
								for(int i=0;i < CricketFunctions.getEventsText(CricketUtil.OVER,0,",", match.getEventFile().getEvents(),0).split(",").length;i++) {
										print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$BigBandGrp$Data$group$"+ (i+2) +"*CONTAINER SET ACTIVE 1;");
										print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateValue"+ (i+1)+ " " + arr[i] + ";");
//										System.out.println("Over : " + (i+1) + " - " + arr[i]);
								}
							}
						}
					}
				}
				
				print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW ON;");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*CONTAINER SET ACTIVE 0;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In STOP;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out STOP;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 196.0;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out SHOW 0.0;");
				print_writer.println("LAYER1*EVEREST*GLOBAL SNAPSHOT_PATH C:/Temp/Preview.png;");
				print_writer.println("LAYER1*EVEREST*GLOBAL SNAPSHOT 1920 1080;");
				TimeUnit.SECONDS.sleep(1);
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out SHOW 0.0;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 0.0;");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*CONTAINER SET ACTIVE 1;");
				print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW OFF;");
				this.status = CricketUtil.SUCCESSFUL;
				break;
		}
	}
	public void populateWorm(PrintWriter print_writer,String viz_scene, int whichInning, MatchAllData match, String session_selected_broadcaster) throws InterruptedException
	{
		switch (session_selected_broadcaster.toUpperCase()) {
		case "FRUIT":
			if (match == null) {
				this.status = "ERROR: Match is null";
			} else if(whichInning == 0) {
				this.status = "ERROR: Inning is null";
			}else {
				
				int maxRuns = 0,runsIncr = 0,wicketcountHome=0,wicketcountAway=0;
				//long lngth = 0;
				
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHeader01 "
						+ "" + ";");
				
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHeader02 "
						+ match.getSetup().getMatchIdent().toUpperCase() + ";");
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSubHeader "
						+ match.getSetup().getTournament() + ";");
								
				for(Inning inn : match.getMatch().getInning()) {
					if (inn.getInningNumber() == whichInning) {
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgLogo " + logo_path + "TLogo" + 
								CricketUtil.PNG_EXTENSION + ";");
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgTLogo " + logo_path + "TLogo" + 
								CricketUtil.PNG_EXTENSION + ";");
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomeTeamName " + match.getMatch().getInning().get(0).getBatting_team().getTeamName2() + ";");
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomeTeamScore " + match.getMatch().getInning().get(0).getTotalRuns() + "-" + match.getMatch().getInning().get(0).getTotalWickets() + ";");
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomeTeamOvers " + CricketFunctions.OverBalls(match.getMatch().getInning().get(0).getTotalOvers(), match.getMatch().getInning().get(0).getTotalBalls()) + ";");
						
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tAwayTeamName " + match.getMatch().getInning().get(1).getBatting_team().getTeamName2() + ";");
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tAwayTeamScore " + match.getMatch().getInning().get(1).getTotalRuns() + "-" + match.getMatch().getInning().get(1).getTotalWickets() + ";");
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tAwayTeamOvers " + CricketFunctions.OverBalls(match.getMatch().getInning().get(1).getTotalOvers(), match.getMatch().getInning().get(1).getTotalBalls()) + ";");
					}
				}
				
				List<String> overByOverRuns = new ArrayList<String>();
				for(int inn_count = 1; inn_count <= whichInning; inn_count++)
				{
					overByOverRuns.clear();
					for(OverByOverData Over : CricketFunctions.getOverByOverData(match,inn_count ,"WORM" ,match.getEventFile().getEvents())) {
						overByOverRuns.add(String.valueOf(Over.getOverTotalRuns()));
					}
					
					//String cumm_runs = String.valueOf("0") + "_" + String.join("_", overByOverRuns); // Store Per Overs Runs
					String cumm_runs = String.join("_", overByOverRuns); // Store Per Overs Runs

					if(match.getMatch().getInning().get(0).getTotalRuns() > match.getMatch().getInning().get(1).getTotalRuns()) {
						maxRuns = match.getMatch().getInning().get(0).getTotalRuns();
						if(maxRuns % 4 == 0) {
							maxRuns = maxRuns + 1;
						}
					}
					else {
						maxRuns = match.getMatch().getInning().get(1).getTotalRuns();
						if(maxRuns % 4 == 0) {
							maxRuns = maxRuns + 1;
						}
					}
					
					
					while (maxRuns % 4 != 0) {     // 5 label in y-axis
						maxRuns = maxRuns + 1;    // keep incrementing till max runs is divisible by 5. maxRuns = 35
					}
					
					for(int i =0; i < 4;i++) {
						runsIncr = maxRuns / 4; // 35/5=7 -> Y axis will be plot like 6,12,18,24,30 & 36	
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET TxtScore" + (4 - i) + " " + runsIncr*(i+1) + ";");
					}
					
					if(inn_count == 1) {
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$All$WormGrp$Worms$Main$HomeGraph1$Linechart*GEOMETRY*LINE_CHART SET MAX_VALUE " + maxRuns + ";");

						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$All$WormGrp$Worms$Main$HomeGraph1$Linechart*GEOMETRY*LINE_CHART SET DATA " + cumm_runs + ";");
						
						for(int j=0; j<=CricketFunctions.getOverByOverData(match,inn_count,"WORM", match.getEventFile().getEvents()).size()-1;j++) {
							if(match.getMatch().getInning().get(inn_count-1).getTotalWickets() > 0) {
								if(CricketFunctions.getOverByOverData(match,inn_count,"WORM", match.getEventFile().getEvents()).get(j).getOverTotalWickets() > 0) {
									wicketcountHome = wicketcountHome + 1;
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$All$WormGrp$Worms$Main$HomeGraph1$Wickets*CONTAINER SET ACTIVE 1;");

									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$All$WormGrp$Worms$Main$HomeGraph1$Wickets$Group" + (wicketcountHome - 1) + "$Group" + (wicketcountHome - 1) + "_XPos*CONTAINER SET ACTIVE 1;");
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$All$WormGrp$Worms$Main$HomeGraph1$Wickets$Group" + (wicketcountHome - 1) + "$Group" + (wicketcountHome - 1) + "_XPos*FUNCTION*TAG_POSITION SET MAX_VALUE " + match.getSetup().getMaxOvers() + ";");

									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vHomeWickets " + (wicketcountHome-1) + ";");
									
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$All$WormGrp$Worms$Main$HomeGraph1$Wickets$Group" + (wicketcountHome - 1) + "*FUNCTION*TAG_POSITION SET MAX_VALUE " + maxRuns + ";");
									
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET yHomePos" + (wicketcountHome-1) + " " + 
											CricketFunctions.getOverByOverData(match, inn_count,"WORM", match.getEventFile().getEvents()).get(j).getOverTotalRuns() + ";");
									
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET xHomePos" + (wicketcountHome - 1) + " " + (j - 0.5) + ";");
									
									
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET selHomeGroup"+ (wicketcountHome - 1) + " " + (CricketFunctions.
											getOverByOverData(match,inn_count,"WORM", match.getEventFile().getEvents()).get(j).getOverTotalWickets()-1) + ";");
									
//									print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET selGroup"+ wicketcount + " " + CricketFunctions.
//											getOverByOverData(match,whichInning,"WORM", match.getEvents()).get(j).getOverTotalWickets() + ";");
//									System.out.println("Over : " + CricketFunctions.getOverByOverData(match, inn_count,"WORM", match.getEvents()).get(j).getOverTotalWickets()
//											+ " - Runs : " + CricketFunctions.getOverByOverData(match, inn_count,"WORM", match.getEvents()).get(j).getOverTotalRuns());
								}
							}else {
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$All$WormGrp$Worms$Main$HomeGraph1$Wickets*CONTAINER SET ACTIVE 0;");
							}
						}
					}
					if(inn_count == 2) {
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$All$WormGrp$Worms$Main$AwayGraph2$Linechart*GEOMETRY*LINE_CHART SET MAX_VALUE " + maxRuns + ";");
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$All$WormGrp$Worms$Main$AwayGraph2$Linechart*GEOMETRY*LINE_CHART SET DATA " + cumm_runs + ";");
						
						for(int j=0; j<=CricketFunctions.getOverByOverData(match,inn_count,"WORM", match.getEventFile().getEvents()).size()-1;j++) {
							if(match.getMatch().getInning().get(inn_count-1).getTotalWickets() > 0) {
								if(CricketFunctions.getOverByOverData(match,inn_count,"WORM", match.getEventFile().getEvents()).get(j).getOverTotalWickets() > 0) {
									wicketcountAway = wicketcountAway + 1;
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$All$WormGrp$Worms$Main$AwayGraph2$Wickets*CONTAINER SET ACTIVE 1;");

									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$All$WormGrp$Worms$Main$AwayGraph2$Wickets$Group" + (wicketcountAway - 1) + "$Group" + (wicketcountAway-1) + "_XPos*CONTAINER SET ACTIVE 1;");

									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$All$WormGrp$Worms$Main$AwayGraph2$Wickets$Group" + (wicketcountAway - 1) + "$Group" + (wicketcountAway-1) + "_XPos*FUNCTION*TAG_POSITION SET MAX_VALUE " + match.getSetup().getMaxOvers() + ";");

									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vAwayWickets " + (wicketcountAway-1) + ";");
									
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$All$WormGrp$Worms$Main$AwayGraph2$Wickets$Group" + (wicketcountAway - 1) + "*FUNCTION*TAG_POSITION SET MAX_VALUE " + maxRuns + ";");
									
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET yAwayPos" + (wicketcountAway -1) + " " + 
											CricketFunctions.getOverByOverData(match, inn_count,"WORM", match.getEventFile().getEvents()).get(j).getOverTotalRuns() + ";");
									
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET xAwayPos" + (wicketcountAway - 1) + " " + (j - 0.5) + ";");
									
									
									print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET selAwayGroup"+ (wicketcountAway - 1) + " " + (CricketFunctions.
											getOverByOverData(match,inn_count,"WORM", match.getEventFile().getEvents()).get(j).getOverTotalWickets()-1) + ";");
									
//									print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET selGroup"+ wicketcount + " " + CricketFunctions.
//											getOverByOverData(match,whichInning,"WORM", match.getEvents()).get(j).getOverTotalWickets() + ";");
//									System.out.println("Over : " + CricketFunctions.getOverByOverData(match, inn_count,"WORM", match.getEvents()).get(j).getOverTotalWickets()
//											+ " - Runs : " + CricketFunctions.getOverByOverData(match, inn_count,"WORM", match.getEvents()).get(j).getOverTotalRuns());
								}
							}else {
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$All$WormGrp$Worms$Main$AwayGraph2$Wickets*CONTAINER SET ACTIVE 0;");
							}
						}
					}
				}
//				for(int i=0; i<(CricketFunctions.getOverByOverData(match,whichInning,"WORM", match.getEvents()).size()-1);i++) {
//					overByOverRuns.add(String.valueOf(CricketFunctions.getOverByOverData(match,whichInning,"WORM", match.getEvents()).get(i).getOverTotalRuns()));
//					//System.out.println("Runs : " + i + " - " + String.valueOf(CricketFunctions.getOverByOverData(match,whichInning,"WORM", match.getEvents()).get(i).getOverTotalRuns()));
//				}
//				
//				String cumm_runs = String.valueOf("0") + "_" + String.join("_", overByOverRuns); // Store Per Overs Runs
//				//System.out.println("Runs : " + cumm_runs);
//				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main$All$WormGrp$Worms$Main$HomeGraph1$Linechart*GEOMETRY*LINE_CHART SET DATA " + cumm_runs + ";");
//
//				
//				if(match.getInning().get(0).getTotalRuns() > match.getInning().get(1).getTotalRuns()) {
//					maxRuns = match.getInning().get(0).getTotalRuns();
//				}
//				else {
//					maxRuns = match.getInning().get(1).getTotalRuns();
//				}
//				while (maxRuns % 4 != 0) {     // 5 label in y-axis
//					maxRuns = maxRuns + 1;    // keep incrementing till max runs is divisible by 5. maxRuns = 35
//				}
//				
//				for(int i =0; i < 4;i++) {
//					runsIncr = maxRuns / 4; // 35/5=7 -> Y axis will be plot like 6,12,18,24,30 & 36	
//					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET TxtScore" + (4 - i) + " " + runsIncr*(i+1) + ";");
//				}
				
//				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main$DataAll$BAtting$Worms$Graph$Linechart*GEOMETRY*LINE_CHART SET MAX_VALUE " + maxRuns + ";");
				
//				for(int j=0; j<=CricketFunctions.getOverByOverData(match,whichInning,"WORM", match.getEvents()).size()-1;j++) {
//					if(match.getInning().get(whichInning-1).getTotalWickets() > 0) {
//						if(CricketFunctions.getOverByOverData(match,whichInning,"WORM", match.getEvents()).get(j).getOverTotalWickets() > 0) {
//							wicketcount = wicketcount + 1;
//							
//							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vWickets " + wicketcount + ";");
//							
//							print_writer.println("LAYER1*EVEREST*TREEVIEW*Group" + wicketcount + "*FUNCTION*TAG_POSITION SET MAX_VALUE " + maxRuns + ";");
//							
//							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET yPos" + wicketcount + " " + 
//									CricketFunctions.getOverByOverData(match, whichInning,"WORM", match.getEvents()).get(j).getOverTotalRuns() + ";");
//							
//							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET xPos" + wicketcount + " " + j + ";");
//							
//							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET selGroup"+ wicketcount + " " + CricketFunctions.
//									getOverByOverData(match,whichInning,"WORM", match.getEvents()).get(j).getOverTotalWickets() + ";");
//							System.out.println("Over : " + CricketFunctions.getOverByOverData(match, whichInning,"WORM", match.getEvents()).get(j).getOverNumber()
//									+ " - Runs : " + CricketFunctions.getOverByOverData(match, whichInning,"WORM", match.getEvents()).get(j).getOverTotalRuns());
//						}
//					}
//				}	
			}
			print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW ON;");
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In STOP;");
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out STOP;");
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 140;");
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out SHOW 0.0;");
			print_writer.println("LAYER1*EVEREST*GLOBAL SNAPSHOT_PATH C:/Temp/Preview.png;");
			print_writer.println("LAYER1*EVEREST*GLOBAL SNAPSHOT 1920 1080;");
			TimeUnit.SECONDS.sleep(1);
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out SHOW 0.0;");
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 0.0;");
			print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW OFF;");
			
			this.status = CricketUtil.SUCCESSFUL;
			break;
		}
		
//		switch (session_selected_broadcaster.toUpperCase()) {
//		case "FRUIT":
//			if (match == null) {
//				this.status = "ERROR: Match is null";
//			} else if(whichInning == 0) {
//				this.status = "ERROR: Inning is null";
//			}else {
//				
//				int maxRuns = 0,runsIncr = 0,wicketcount=0;
//				//long lngth = 0;
//				
//				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeamFirstName " + match.getMatchIdent().toUpperCase() + ";");
//				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeamLastName " + match.getMatchIdent().toUpperCase() + ";");
//				
//				for(Inning inn : match.getInning()) {
//					if (inn.getInningNumber() == whichInning) {
//						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgTeamLogo " + logo_path + inn.getBatting_team().getTeamName4() + 
//								CricketUtil.PNG_EXTENSION + ";");
//						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeamName " + inn.getBatting_team().getTeamName1().toUpperCase() + ";");
//					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSubHeader " + "v " + inn.getBowling_team().getTeamName1().toUpperCase() + ";");
//					}
//				}
//				
//				List<String> overByOverRuns = new ArrayList<String>();
//				
//				for(int i=0; i<(CricketFunctions.getOverByOverData(match,whichInning,"WORM", match.getEvents()).size()-1);i++) {
//					overByOverRuns.add(String.valueOf(CricketFunctions.getOverByOverData(match,whichInning,"WORM", match.getEvents()).get(i).getOverTotalRuns()));
//					//System.out.println("Runs : " + i + " - " + String.valueOf(CricketFunctions.getOverByOverData(match,whichInning,"WORM", match.getEvents()).get(i).getOverTotalRuns()));
//				}
//				
//				String cumm_runs = String.valueOf("0") + "_" + String.join("_", overByOverRuns); // Store Per Overs Runs
//				//System.out.println("Runs : " + cumm_runs);
//				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$DataAll$BAtting$Worms$Graph$Linechart*GEOMETRY*LINE_CHART SET DATA " + cumm_runs + ";");
//
//				
//				if(match.getInning().get(0).getTotalRuns() > match.getInning().get(1).getTotalRuns()) {
//					maxRuns = match.getInning().get(0).getTotalRuns();
//				}
//				else {
//					maxRuns = match.getInning().get(1).getTotalRuns();
//				}
//				while (maxRuns % 4 != 0) {     // 5 label in y-axis
//					maxRuns = maxRuns + 1;    // keep incrementing till max runs is divisible by 5. maxRuns = 35
//				}
//				
//				for(int i =0; i < 4;i++) {
//					runsIncr = maxRuns / 4; // 35/5=7 -> Y axis will be plot like 6,12,18,24,30 & 36	
//					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET TxtScore" + (4 - i) + " " + runsIncr*(i+1) + ";");
//				}
//				
//				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main$DataAll$BAtting$Worms$Graph$Linechart*GEOMETRY*LINE_CHART SET MAX_VALUE " + maxRuns + ";");
//				
//				for(int j=0; j<=CricketFunctions.getOverByOverData(match,whichInning,"WORM", match.getEvents()).size()-1;j++) {
//					if(match.getInning().get(whichInning-1).getTotalWickets() > 0) {
//						if(CricketFunctions.getOverByOverData(match,whichInning,"WORM", match.getEvents()).get(j).getOverTotalWickets() > 0) {
//							wicketcount = wicketcount + 1;
//							
//							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vWickets " + wicketcount + ";");
//							
//							print_writer.println("LAYER2*EVEREST*TREEVIEW*Group" + wicketcount + "*FUNCTION*TAG_POSITION SET MAX_VALUE " + maxRuns + ";");
//							
//							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET yPos" + wicketcount + " " + 
//									CricketFunctions.getOverByOverData(match, whichInning,"WORM", match.getEvents()).get(j).getOverTotalRuns() + ";");
//							
//							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET xPos" + wicketcount + " " + j + ";");
//							
//							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET selGroup"+ wicketcount + " " + CricketFunctions.
//									getOverByOverData(match,whichInning,"WORM", match.getEvents()).get(j).getOverTotalWickets() + ";");
//							System.out.println("Over : " + CricketFunctions.getOverByOverData(match, whichInning,"WORM", match.getEvents()).get(j).getOverNumber()
//									+ " - Runs : " + CricketFunctions.getOverByOverData(match, whichInning,"WORM", match.getEvents()).get(j).getOverTotalRuns());
//						}
//					}
//				}	
//			}
//			print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW ON;");
//			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In STOP;");
//			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out STOP;");
//			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 197.0;");
//			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out SHOW 0.0;");
//			print_writer.println("LAYER1*EVEREST*GLOBAL SNAPSHOT_PATH C:/Temp/Preview.png;");
//			print_writer.println("LAYER1*EVEREST*GLOBAL SNAPSHOT 1920 1080;");
//			TimeUnit.SECONDS.sleep(1);
//			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out SHOW 0.0;");
//			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 0.0;");
//			print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW OFF;");
//			
//			this.status = CricketUtil.SUCCESSFUL;
//			break;
//		}
			
	}
	public void populateThisSeries(PrintWriter print_writer, String viz_scene,int Playerid,String TypeofProfile,List<Tournament> this_series,
			MatchAllData match, String broadcaster) throws InterruptedException {
		
		if (match == null) {
			this.status = "ERROR: Match is null";
		} else {
			
			double strike_rate = 0 , economy_rate=0;
			int k=0;
		
			List<BestStats> top_batsman_beststats = new ArrayList<BestStats>();
			List<BestStats> top_bowler_beststats = new ArrayList<BestStats>();
			for(Tournament tourn : this_series) {
				for(BestStats bs : tourn.getBatsman_best_Stats()) {
					top_batsman_beststats.add(bs);
				}
				for(BestStats bfig : tourn.getBowler_best_Stats()) {
					top_bowler_beststats.add(bfig);
				}
			}
			
			Collections.sort(top_batsman_beststats, new CricketFunctions.PlayerBestStatsComparator());
			Collections.sort(top_bowler_beststats, new CricketFunctions.PlayerBestStatsComparator());
			
			//print_writer.println("-1 RENDERER*TREE*$Main$All_Grp$PositionY$LT02$All$Out$PlayerNameGrp$TeamNameGrp$noname$FOW*GEOM*TEXT SET " + " " + "\0");
			
			print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSubHeader " + "THIS SERIES" + ";");
			print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgTLogo " + logo_path + "TLogo" + 
					CricketUtil.PNG_EXTENSION + ";");
			
			for(int i = 0; i <= this_series.size() - 1 ; i++) {
				if(this_series.get(i).getPlayerId() == Playerid) {
					
					if(this_series.get(i).getPlayer().getTeamId() == match.getSetup().getHomeTeamId()) {
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgTeamLogo " + logo_path + match.getSetup().getHomeTeam().getTeamName4().toUpperCase() + 
								CricketUtil.PNG_EXTENSION + ";");
					}else {
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgTeamLogo " + logo_path + match.getSetup().getAwayTeam().getTeamName4().toUpperCase() + 
								CricketUtil.PNG_EXTENSION + ";");
					}
					
					print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerName " + 
							this_series.get(i).getPlayer().getFull_name().toUpperCase() + ";");
					
					switch(TypeofProfile.toUpperCase()) {
					case CricketUtil.BATSMAN:
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateHead " + "MATCHES" + ";");
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateValue " + this_series.get(i).getMatches() + ";");
						
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateHead1 " + "RUNS" + ";");
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateValue1 " + this_series.get(i).getRuns() + ";");
						
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateHead2 " + "4s/6s" + ";");
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateValue2 " + this_series.get(i).getFours() 
								+"/" + this_series.get(i).getSixes() + ";");
						
						
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateHead3 " + "S/R" + ";");			
						if(this_series.get(i).getBallsFaced() == 0 || this_series.get(i).getRuns()== 0) {
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateValue3 " + "-" + ";");
						}else {
							strike_rate = this_series.get(i).getRuns() * 100;
							strike_rate = strike_rate/this_series.get(i).getBallsFaced();
							DecimalFormat df = new DecimalFormat("0.0");
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateValue3 " + df.format(strike_rate) + ";");
						}
						 
						for(int j=0;j<= top_batsman_beststats.size()-1;j++) {
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateHead4 " + "BEST" + ";");
							if(top_batsman_beststats.get(j).getPlayerId() == Playerid) {
								if(k == 0) {
									k += 1;
									if(top_batsman_beststats.get(j).getBestEquation() % 2 == 0) {
										print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateValue4 " + top_batsman_beststats.get(j).getBestEquation()/2 + ";");
									}else {
										print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateValue4 " + (top_batsman_beststats.get(j).getBestEquation()-1) / 2 + ";");
									}
									break;
								}
							}else {
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateValue4 " + "-" + ";");
							}
						}
						break;
					case CricketUtil.BOWLER:
						
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateHead " + "MATCHES" + ";");
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateValue " + this_series.get(i).getMatches() + ";");
						
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateHead1 " + "WICKETS" + ";");
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateValue1 " + this_series.get(i).getWickets() + ";");
						
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateHead2 " + "DOTS" + ";");
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateValue2 " + this_series.get(i).getDots() + ";");
						
						print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateHead3 " + "ECON." + ";");
						if(this_series.get(i).getBallsBowled() == 0 || this_series.get(i).getRunsConceded() == 0) {
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateValue3 " + "-" + ";");
						}else {
							economy_rate = (this_series.get(i).getRunsConceded()*1.00) /this_series.get(i).getBallsBowled();
							economy_rate = economy_rate * 6;
							DecimalFormat df = new DecimalFormat("0.00");
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateValue3 " + df.format(economy_rate) + ";");
						}
						
						for(int j=0;j<= top_bowler_beststats.size()-1;j++) {
							print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateHead4 " + "BEST" + ";");
							if(top_bowler_beststats.get(j).getPlayerId() == Playerid) {
								if(k == 0) {
									k += 1;
									if(top_bowler_beststats.get(j).getBestEquation() % 1000 > 0) {
										print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateValue4 " + 
												((top_bowler_beststats.get(j).getBestEquation() / 1000) +1) + "-" + (1000 - (top_bowler_beststats.get(j).getBestEquation() % 1000)) + ";");
									}
									else {
										print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateValue4 " + 
												(top_bowler_beststats.get(j).getBestEquation() / 1000) + "-" + Math.abs(top_bowler_beststats.get(j).getBestEquation()) + ";");
									}
									break;
								}
							}else if(top_bowler_beststats.get(j).getPlayerId() != Playerid) {
								print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStateValue4 " + "-" + ";");
							}
						}
						break;
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
			TimeUnit.SECONDS.sleep(1);
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*Out SHOW 0.0;");
			print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 0.0;");
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Infobar*CONTAINER SET ACTIVE 1;");
			print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW OFF;");
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

	public static Statistics updateTournamentDataWithStats(Statistics stat,List<MatchAllData> tournament_matches,MatchAllData currentMatch,String selectedMatchType) 
	{
		boolean player_found = false;
		for(MatchAllData match : tournament_matches) {
			if(!match.getMatch().getMatchFileName().equalsIgnoreCase(currentMatch.getMatch().getMatchFileName())) {
				if(stat.getStats_type().getStats_short_name().equalsIgnoreCase(match.getSetup().getMatchType()) || (match.getSetup().getMatchType().equalsIgnoreCase("ODI") && 
						selectedMatchType.equalsIgnoreCase("OD") && stat.getStats_type().getStats_short_name().equalsIgnoreCase(selectedMatchType))) {
					for(Inning inn : match.getMatch().getInning()) {
						for(BattingCard bc : inn.getBattingCard()) {
							if(bc.getPlayerId() == stat.getPlayer_id()) {
								player_found = true;
								if(bc.getBatsmanInningStarted() != null && bc.getBatsmanInningStarted().equalsIgnoreCase(CricketUtil.YES)) {
									stat.setInnings(stat.getInnings() + 1);
								}
								stat.setRuns(stat.getRuns() + bc.getRuns());
								stat.setFours(stat.getFours() + bc.getFours());
								stat.setSixes(stat.getSixes() + bc.getSixes());
								stat.setBalls_faced(stat.getBalls_faced() + bc.getBalls());
								
								if(bc.getRuns() < 50 && bc.getRuns() >= 30) {
									stat.setThirties(stat.getThirties() + 1);
								}else if(bc.getRuns() < 100 && bc.getRuns() >= 50) {
									stat.setFifties(stat.getFifties() + 1);
								}else if(bc.getRuns() >= 100){
									stat.setHundreds(stat.getHundreds() + 1);
								}
								
							}
						}
						if(inn.getBowlingCard() != null && inn.getBowlingCard().size()>0) {
							for(BowlingCard boc : inn.getBowlingCard()) {
								if(boc.getPlayerId() == stat.getPlayer_id()) {
									stat.setWickets(stat.getWickets() + boc.getWickets());
									stat.setRuns_conceded(stat.getRuns_conceded() + boc.getRuns());
									stat.setBalls_bowled(stat.getBalls_bowled() + (boc.getOvers()*6 + boc.getBalls()));
									stat.setDotbowled(stat.getDotbowled() + boc.getDots());
									if(boc.getWickets() < 5 && boc.getWickets() >= 3) {
										stat.setPlus_3(stat.getPlus_3() + 1);
									}	
									else if(boc.getWickets() >= 5){
										stat.setPlus_5(stat.getPlus_5() + 1);
									}
								}
							}							
						}
					}
					player_found = false;
					for(Player hs : match.getSetup().getHomeSquad()) {
						if(stat.getPlayer_id() == hs.getPlayerId()) {
							player_found = true;
						}
					}
					for(Player as : match.getSetup().getAwaySquad()) {
						if(stat.getPlayer_id() == as.getPlayerId()) {
							player_found = true;
						}
					}
					if(player_found == true){
						stat.setMatches(stat.getMatches() + 1);
					}
				}
			}
		}
		return stat;
	}
	public static Statistics updateStatisticsWithMatchData(Statistics stat, MatchAllData match,String selectedMatchType)
	{
		boolean player_found = false;
		if(stat.getStats_type().getStats_short_name().equalsIgnoreCase(match.getSetup().getMatchType()) || (match.getSetup().getMatchType().equalsIgnoreCase("ODI") && 
				selectedMatchType.equalsIgnoreCase("OD") && stat.getStats_type().getStats_short_name().equalsIgnoreCase(selectedMatchType))) {
			stat.setTournament_fours(stat.getTournament_fours() + match.getMatch().getInning().get(0).getTotalFours());
			stat.setTournament_fours(stat.getTournament_fours() + match.getMatch().getInning().get(1).getTotalFours());
			for(Inning inn : match.getMatch().getInning()) {
				for(BattingCard bc : inn.getBattingCard()) {
					if(bc.getPlayerId() == stat.getPlayer_id()) {
						player_found = true;
						if(bc.getBatsmanInningStarted() == null) {
						}
						else if(bc.getBatsmanInningStarted().equalsIgnoreCase(CricketUtil.YES)) {
							stat.setInnings(stat.getInnings() + 1);
						}
						
						stat.setRuns(stat.getRuns() + bc.getRuns());
						stat.setFours(stat.getFours() + bc.getFours());
						stat.setSixes(stat.getSixes() + bc.getSixes());
						stat.setBalls_faced(stat.getBalls_faced() + bc.getBalls());
				
						if(bc.getRuns() < 50 && bc.getRuns() >= 30) {
							stat.setThirties(stat.getThirties() + 1);
						}else if(bc.getRuns() < 100 && bc.getRuns() >= 50) {
							stat.setFifties(stat.getFifties() + 1);
						}else if(bc.getRuns() >= 100){
							stat.setHundreds(stat.getHundreds() + 1);
						}
					}
				}
				if(inn.getBowlingCard() != null && inn.getBowlingCard().size()>0) {
					for(BowlingCard boc : inn.getBowlingCard()) {
						if(boc.getPlayerId() == stat.getPlayer_id()) {
							player_found = true;
							stat.setWickets(stat.getWickets() + boc.getWickets());
							stat.setRuns_conceded(stat.getRuns_conceded() + boc.getRuns());
							stat.setBalls_bowled(stat.getBalls_bowled() + (boc.getOvers()*6 + boc.getBalls()));
							stat.setDotbowled(stat.getDotbowled() + boc.getDots());
							//System.out.println(boc.getWickets());
							if(boc.getWickets() >= 3 && boc.getWickets() < 5) {
								stat.setPlus_3(stat.getPlus_3() + 1);
							}else if(boc.getWickets() >= 5){
								stat.setPlus_5(stat.getPlus_5() + 1);
							}
						}
					}							
				}
			}
			player_found = false;
			for(Player hs : match.getSetup().getHomeSquad()) {
				if(stat.getPlayer_id() == hs.getPlayerId()) {
					player_found = true;
				}
			}
			for(Player as : match.getSetup().getAwaySquad()) {
				if(stat.getPlayer_id() == as.getPlayerId()) {
					player_found = true;
				}
			}
			if(player_found == true){
				stat.setMatches(stat.getMatches() + 1);
			}
		}
		return stat;
	}
	public static List<Tournament> extractTournamentStats(String typeOfExtraction, List<MatchAllData> tournament_matches,CricketService cricketService,
			MatchAllData currentMatch, List<Tournament> past_tournament_stats) 
	{		
		int playerId = -1;
		List<Tournament> tournament_stats = new ArrayList<Tournament>();
		boolean has_match_started = false;
		
		switch(typeOfExtraction) {
		case "COMBINED_PAST_CURRENT_MATCH_DATA":
			
			 return extractTournamentStats("CURRENT_MATCH_DATA", tournament_matches, cricketService, currentMatch, 
					extractTournamentStats("PAST_MATCHES_DATA", tournament_matches, cricketService, currentMatch, null));
			
		case "PAST_MATCHES_DATA":
			
			for(MatchAllData mtch : tournament_matches) {
				if(!mtch.getMatch().getMatchFileName().equalsIgnoreCase(currentMatch.getMatch().getMatchFileName())) {
					
					has_match_started = false;
					
					if(mtch.getSetup().getMatchType().equalsIgnoreCase("ODI") || mtch.getSetup().getMatchType().equalsIgnoreCase("OD")) {
						if(mtch.getMatch().getInning().get(0).getTotalRuns() > 0 || (6 * mtch.getMatch().getInning().get(0).getTotalOvers() 
							+ mtch.getMatch().getInning().get(0).getTotalBalls()) > 0) {
							has_match_started = true;
						}
						for(Inning inn : mtch.getMatch().getInning())
						{
							if(inn.getTotalRuns() > 0 || (6 * inn.getTotalOvers() + inn.getTotalBalls()) > 0) {
								has_match_started = true;
							}
							
							if(inn.getBattingCard() != null && inn.getBattingCard().size() > 0) {
								
								for(BattingCard bc : inn.getBattingCard())
								{
									playerId = -1;
									for(int i=0; i<=tournament_stats.size() - 1;i++)
									{
										if(bc.getPlayerId() == tournament_stats.get(i).getPlayerId()) {
											playerId = i;
											break;
										}
									}
									if(playerId >= 0) {
										
										tournament_stats.get(playerId).setRuns(tournament_stats.get(playerId).getRuns() + bc.getRuns()); // existing record
										tournament_stats.get(playerId).setBallsFaced(tournament_stats.get(playerId).getBallsFaced() + bc.getBalls());
										tournament_stats.get(playerId).setFours(tournament_stats.get(playerId).getFours() + bc.getFours());
										tournament_stats.get(playerId).setSixes(tournament_stats.get(playerId).getSixes() + bc.getSixes());
										
//										if(bc.getStatus().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
//											tournament_stats.get(playerId).getBatsman_best_Stats().add(new BestStats(
//													bc.getPlayerId(), (bc.getRuns() * 2) + 1, bc.getBalls(), inn.getBowling_team(), bc.getPlayer(),""));
//											
//										}else {
//											tournament_stats.get(playerId).getBatsman_best_Stats().add(new BestStats(
//													bc.getPlayerId(), bc.getRuns() * 2, bc.getBalls(), inn.getBowling_team(), bc.getPlayer(),""));
//										}
										
									}else {
										tournament_stats.add(new Tournament(bc.getPlayerId(), bc.getRuns(), bc.getFours(), bc.getSixes(), 0, 0, 0, bc.getBalls(), 
												0,bc.getStatus(),0,0,0,0, bc.getPlayer(), new ArrayList<BestStats>(), new ArrayList<BestStats>()));
										
//										if(bc.getStatus().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
//											tournament_stats.get(tournament_stats.size() - 1).getBatsman_best_Stats().add(
//													new BestStats(bc.getPlayerId(), (bc.getRuns() * 2) + 1, bc.getBalls(),
//													inn.getBowling_team(), bc.getPlayer(),""));
//											
//										}else {
//											tournament_stats.get(tournament_stats.size() - 1).getBatsman_best_Stats().add(
//													new BestStats(bc.getPlayerId(), (bc.getRuns() * 2), bc.getBalls(),
//													inn.getBowling_team(), bc.getPlayer(),""));
//										}

									}	
								}
							}
							
							if(inn.getBowlingCard() != null && inn.getBowlingCard().size() > 0) {
								
								for(BowlingCard boc : inn.getBowlingCard())
								{
									playerId = -1;
									for(int i=0; i<=tournament_stats.size() - 1;i++)
									{
										if(boc.getPlayerId() == tournament_stats.get(i).getPlayerId()) {
											playerId = i;
											break;
										}
									}
									
//									if(playerId >= 0) {
//										
//										tournament_stats.get(playerId).setWickets(tournament_stats.get(playerId).getWickets() + boc.getWickets());
//										tournament_stats.get(playerId).setRunsConceded(tournament_stats.get(playerId).getRunsConceded() + boc.getRuns()); // existing record
//										tournament_stats.get(playerId).setDots(tournament_stats.get(playerId).getDots() + boc.getDots());
//										tournament_stats.get(playerId).setBallsBowled(tournament_stats.get(playerId).getBallsBowled() + 
//												6 * boc.getOvers() + boc.getBalls());
//
//										tournament_stats.get(playerId).getBowler_best_Stats().add(new BestStats(
//												boc.getPlayerId(), (1000 * boc.getWickets()) - boc.getRuns(), 6 * boc.getOvers() + boc.getBalls(), 
//												inn.getBatting_team(), boc.getPlayer(),""));
//										
//									}else {
//										
//										tournament_stats.add(new Tournament(boc.getPlayerId(), 0, 0, 0, boc.getWickets(), boc.getRuns(), 6 * boc.getOvers() + boc.getBalls(), 0, 
//												boc.getDots(),null,0,0,0,0, boc.getPlayer(), new ArrayList<BestStats>(), new ArrayList<BestStats>()));
//										
//										tournament_stats.get(tournament_stats.size() - 1).getBowler_best_Stats().add(new BestStats(
//												boc.getPlayerId(), (1000 * boc.getWickets()) - boc.getRuns(), 6 * boc.getOvers() + boc.getBalls(), 
//												inn.getBatting_team(), boc.getPlayer(),""));
//																				
//									}
								}
							}
						}
						
						if(has_match_started == true) {
							for(Tournament trmnt : tournament_stats) {
								for(Player plyr : mtch.getSetup().getHomeSquad()) {
									if(plyr.getPlayerId() == trmnt.getPlayerId()) {
										trmnt.setMatches(trmnt.getMatches() + 1);
									}
								}
								for(Player plyr : mtch.getSetup().getAwaySquad()) {
									if(plyr.getPlayerId() == trmnt.getPlayerId()) {
										trmnt.setMatches(trmnt.getMatches() + 1);
									}
								}
							}
						}
					}
				}
			}
			
			return tournament_stats;
			
		case "CURRENT_MATCH_DATA":
			
			has_match_started = false;
			if(currentMatch.getSetup().getMatchType().equalsIgnoreCase("ODI") || currentMatch.getSetup().getMatchType().equalsIgnoreCase("OD")) {
				if(currentMatch.getMatch().getInning().get(0).getTotalRuns() > 0 || (6 * currentMatch.getMatch().getInning().get(0).getTotalOvers() 
						+ currentMatch.getMatch().getInning().get(0).getTotalBalls()) > 0) {
					has_match_started = true;
				}
				
				for(Inning inn : currentMatch.getMatch().getInning())
				{
					if(inn.getTotalRuns() > 0 || (6 * inn.getTotalOvers() + inn.getTotalBalls()) > 0) {
						has_match_started = true;
					}

					for(BattingCard bc : inn.getBattingCard())
					{
						playerId = -1;
						for(int i=0; i<=past_tournament_stats.size() - 1;i++)
						{
							if(bc.getPlayerId() == past_tournament_stats.get(i).getPlayerId()) {
								playerId = i;
								break;
							}
						}
						
						if(playerId >= 0) {
							past_tournament_stats.get(playerId).setRuns(past_tournament_stats.get(playerId).getRuns() + bc.getRuns()); // existing record
							past_tournament_stats.get(playerId).setBallsFaced(past_tournament_stats.get(playerId).getBallsFaced() + bc.getBalls());
							past_tournament_stats.get(playerId).setFours(past_tournament_stats.get(playerId).getFours() + bc.getFours());
							past_tournament_stats.get(playerId).setSixes(past_tournament_stats.get(playerId).getSixes() + bc.getSixes());
							
//							if(bc.getStatus().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
//								past_tournament_stats.get(playerId).getBatsman_best_Stats().add(new BestStats(bc.getPlayerId(), (bc.getRuns() * 2) + 1, 
//										bc.getBalls(), inn.getBowling_team(), bc.getPlayer(),""));
//								
//							}else {
//								past_tournament_stats.get(playerId).getBatsman_best_Stats().add(new BestStats(bc.getPlayerId(), (bc.getRuns() * 2), 
//										bc.getBalls(), inn.getBowling_team(), bc.getPlayer(),""));
//							}
							
						}else {
							past_tournament_stats.add(new Tournament(bc.getPlayerId(), bc.getRuns(), bc.getFours(), bc.getSixes(), 0, 0, 0, bc.getBalls(), 
									0,bc.getStatus(),0,0,0,0, bc.getPlayer(), new ArrayList<BestStats>(),new ArrayList<BestStats>()));
							
//							if(bc.getStatus().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
//								past_tournament_stats.get(past_tournament_stats.size()-1).getBatsman_best_Stats().add(new BestStats(bc.getPlayerId(), 
//										(bc.getRuns() * 2) + 1, bc.getBalls(), inn.getBowling_team(), bc.getPlayer(),""));
//								
//							}else {
//								past_tournament_stats.get(past_tournament_stats.size()-1).getBatsman_best_Stats().add(new BestStats(bc.getPlayerId(), 
//										(bc.getRuns() * 2), bc.getBalls(), inn.getBowling_team(), bc.getPlayer(),""));
//							}
						}	
					}

					if(inn.getBowlingCard() != null && inn.getBowlingCard().size() > 0 ) {
						for(BowlingCard boc : inn.getBowlingCard())
						{
							playerId = -1;
							for(int i=0; i<=past_tournament_stats.size() - 1;i++)
							{
								if(boc.getPlayerId() == past_tournament_stats.get(i).getPlayer().getPlayerId()) {
									playerId = i;
									break;
								}
							}
//							if(playerId >= 0) {
//								
//								past_tournament_stats.get(playerId).setRunsConceded(past_tournament_stats.get(playerId).getRunsConceded() + boc.getRuns()); // existing record
//								past_tournament_stats.get(playerId).setWickets(past_tournament_stats.get(playerId).getWickets() + boc.getWickets());
//								past_tournament_stats.get(playerId).setDots(past_tournament_stats.get(playerId).getDots() + boc.getDots());
//								past_tournament_stats.get(playerId).setBallsBowled(past_tournament_stats.get(playerId).getBallsBowled() + 
//										6 * boc.getOvers() + boc.getBalls());
//
//								past_tournament_stats.get(playerId).getBowler_best_Stats().add(new BestStats(
//										boc.getPlayerId(), (1000 * boc.getWickets()) - boc.getRuns(), 6 * boc.getOvers() + boc.getBalls(), 
//										inn.getBatting_team(), boc.getPlayer(),""));
//	
//							}else {
//								
//								past_tournament_stats.add(new Tournament(boc.getPlayerId(), 0, 0, 0, boc.getWickets(), boc.getRuns(), 6 * boc.getOvers() + boc.getBalls(), 0, 
//										boc.getDots(),null,0,0,0,0, boc.getPlayer(), new ArrayList<BestStats>(), new ArrayList<BestStats>()));
//								
//								past_tournament_stats.get(past_tournament_stats.size() - 1).getBowler_best_Stats().add(new BestStats(boc.getPlayerId(), 
//										(1000 * boc.getWickets()) - boc.getRuns(), 6 * boc.getOvers() + boc.getBalls(), 
//										inn.getBatting_team(), boc.getPlayer(),""));
//								
//							}
						}
					}
					
				}
				
				if(has_match_started == true) {
					for(Tournament trmnt : past_tournament_stats) {
						for(Player plyr : currentMatch.getSetup().getHomeSquad()) {
							if(trmnt.getPlayerId() == plyr.getPlayerId()) {
								trmnt.setMatches(trmnt.getMatches() + 1);
							}
						}
						for(Player plyr : currentMatch.getSetup().getAwaySquad()) {
							if(trmnt.getPlayerId() == plyr.getPlayerId()) {
								trmnt.setMatches(trmnt.getMatches() + 1);
							}
						}
					}
				}
				
			}
			
			return past_tournament_stats;
		}
		return null;
	}
}