package com.cricket.controller;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.cricket.archive.Archive;
import com.cricket.broadcaster.FRUIT;
import com.cricket.containers.Infobar;
import com.cricket.containers.Scene;
import com.cricket.model.BattingCard;
import com.cricket.model.BowlingCard;
import com.cricket.model.Configuration;
import com.cricket.model.EventFile;
import com.cricket.model.ForeignLanguageData;
import com.cricket.model.Inning;
import com.cricket.model.Match;
import com.cricket.model.MatchAllData;
import com.cricket.model.MultiLanguageDatabase;
import com.cricket.model.Setup;
import com.cricket.model.Statistics;
import com.cricket.model.Tournament;
import com.cricket.service.CricketService;
import com.cricket.util.CricketFunctions;
import com.cricket.util.CricketUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
	

@Controller
@SessionAttributes(value = {"session_configuration","session_llc_big_screen","session_llc",
	"session_llc_ar","session_selected_broadcaster","session_selected_scenes"})
public class IndexController 
{
	@Autowired
	CricketService cricketService;
	public static MatchAllData session_match;
	public static MultiLanguageDatabase multiLanguage;
	public static Archive session_archive;
	
//	public static Doad this_doad;
	public static FRUIT this_fruit;
	//	public static DoadVizMulti this_multi;
	public static String expiry_date = "2023-11-30";
	public static String session_selected_second_broadcaster;
	public static String current_date;
	public boolean show_speed = true;
	public static long time_elapsed = 0;
	public static long last_setup_time_stamp = 0;
	public static long last_match_time_stamp = 0;
	public static long last_Speed_time_stamp = 0;
	
	List<ForeignLanguageData> foreignLanguage = new ArrayList<ForeignLanguageData>();
	List<MatchAllData> cricket_matches = new ArrayList<MatchAllData>();
	List<Tournament> past_tournament_stats = new ArrayList<Tournament>();
	List<Statistics> session_statistics = new ArrayList<Statistics>();
	//List<Scene> session_selected_scenes = new ArrayList<Scene>();
	
	@RequestMapping(value = {"/","/initialise"}, method={RequestMethod.GET,RequestMethod.POST}) 
	public String initialisePage(ModelMap model, 
		@ModelAttribute("session_selected_broadcaster") String session_selected_broadcaster, 
		@ModelAttribute("session_configuration") Configuration session_configuration, 
		@ModelAttribute("session_selected_scenes") List<Scene> session_selected_scenes) 
		throws JAXBException, IOException, ParseException, IllegalAccessException, InvocationTargetException, URISyntaxException 
	{
		
		if(current_date == null || current_date.isEmpty()) {
			current_date = CricketFunctions.getOnlineCurrentDate();
		}
		
		model.addAttribute("session_viz_scenes", new File(CricketUtil.CRICKET_SERVER_DIRECTORY + CricketUtil.SCENES_DIRECTORY).listFiles(new FileFilter() {
			@Override
		    public boolean accept(File pathname) {
		        String name = pathname.getName().toLowerCase();
		        return name.endsWith(".via") && pathname.isFile();
		    }
		}));

		model.addAttribute("match_files", new File(CricketUtil.CRICKET_SERVER_DIRECTORY + CricketUtil.MATCHES_DIRECTORY).listFiles(new FileFilter() {
			@Override
		    public boolean accept(File pathname) {
		        String name = pathname.getName().toLowerCase();
//		        System.out.println("Files name : " + name);
		        return name.endsWith(".json") && pathname.isFile();
		    }
		}));

		model.addAttribute("configuration_files", new File(CricketUtil.CRICKET_SERVER_DIRECTORY + CricketUtil.CONFIGURATIONS_DIRECTORY).listFiles(new FileFilter() {
			@Override
		    public boolean accept(File pathname) {
		        String name = pathname.getName().toLowerCase();
		        return name.endsWith(".xml") && pathname.isFile();
		    }
		}));
		
		File files[] = new File(CricketUtil.CRICKET_SERVER_DIRECTORY + CricketUtil.MATCHES_DIRECTORY).listFiles(new FileFilter() {
			@Override
		    public boolean accept(File pathname) {
		        String name = pathname.getName().toLowerCase();
//		        System.out.println("Files : " + name);
		        return name.endsWith(".json") && pathname.isFile();
		    }
		});

		if(cricket_matches == null || cricket_matches.size()<=0) {
//			session_match = new Match();
			cricket_matches = CricketFunctions.getTournamentMatches(files, cricketService);
		}
		if(session_statistics == null || session_statistics.size()<=0) {
			session_statistics = cricketService.getAllStats();
		}
		
		if(new File(CricketUtil.CRICKET_DIRECTORY + CricketUtil.CONFIGURATIONS_DIRECTORY + CricketUtil.OUTPUT_XML).exists()) {
			session_configuration = (Configuration)JAXBContext.newInstance(Configuration.class).createUnmarshaller().unmarshal(
					new File(CricketUtil.CRICKET_DIRECTORY + CricketUtil.CONFIGURATIONS_DIRECTORY + CricketUtil.OUTPUT_XML));
		} else {
			session_configuration = new Configuration();
			JAXBContext.newInstance(Configuration.class).createMarshaller().marshal(session_configuration, 
					new File(CricketUtil.CRICKET_DIRECTORY + CricketUtil.CONFIGURATIONS_DIRECTORY + CricketUtil.OUTPUT_XML));
		}
		
		model.addAttribute("session_configuration",session_configuration);
		model.addAttribute("session_selected_scenes",session_selected_scenes);
		
		return "initialise";
	}

	@RequestMapping(value = {"/output"}, method={RequestMethod.GET,RequestMethod.POST}) 
	public String outputPage(ModelMap model,
			@ModelAttribute("session_configuration") Configuration session_configuration,
			@ModelAttribute("session_selected_scenes") List<Scene> session_selected_scenes,
			@ModelAttribute("session_selected_broadcaster") String session_selected_broadcaster, 
			@RequestParam(value = "configuration_file_name", required = false, defaultValue = "") String configuration_file_name,
			@RequestParam(value = "select_broadcaster", required = false, defaultValue = "") String select_broadcaster,
			@RequestParam(value = "select_second_broadcaster", required = false, defaultValue = "") String select_second_broadcaster,
			@RequestParam(value = "which_layer", required = false, defaultValue = "") String which_layer,
			@RequestParam(value = "which_scene", required = false, defaultValue = "") String which_scene,
			@RequestParam(value = "select_cricket_matches", required = false, defaultValue = "") String selectedMatch,
			@RequestParam(value = "vizIPAddress", required = false, defaultValue = "") String vizIPAddress,
			@RequestParam(value = "vizPortNumber", required = false, defaultValue = "") int vizPortNumber,
			@RequestParam(value = "vizSceneName", required = false, defaultValue = "") String vizScene,
			@RequestParam(value = "vizLanguage", required = false, defaultValue = "") String vizLanguage,
			@RequestParam(value = "vizSecondaryIPAddress", required = false, defaultValue = "") String vizSecondaryIPAddress,
			@RequestParam(value = "vizSecondaryPortNumber", required = false, defaultValue = "") int vizSecondaryPortNumber,
			@RequestParam(value = "vizSecondaryScene", required = false, defaultValue = "") String vizSecondaryScene,
			@RequestParam(value = "vizSecondaryLanguage", required = false, defaultValue = "") String vizSecondaryLanguage,
			@RequestParam(value = "vizTertiaryIPAddress", required = false, defaultValue = "") String vizTertiaryIPAddress,
			@RequestParam(value = "vizTertiaryPortNumber", required = false, defaultValue = "") int vizTertiaryPortNumber,
			@RequestParam(value = "vizTertiaryScene", required = false, defaultValue = "") String vizTertiaryScene,
			@RequestParam(value = "vizTertiaryLanguage", required = false, defaultValue = "") String vizTertiaryLanguage) 
					throws UnknownHostException, IllegalAccessException, InvocationTargetException, ParseException, 
					IOException, InterruptedException, JAXBException, URISyntaxException
	{
		if(current_date == null || current_date.isEmpty()) {
			
			model.addAttribute("error_message","You must be connected to the internet online");
			return "error";
		
		} else if(new SimpleDateFormat("yyyy-MM-dd").parse(expiry_date).before(new SimpleDateFormat("yyyy-MM-dd").parse(current_date))) {
			
			model.addAttribute("error_message","This software has expired");
			return "error";
			
		}else {
		
			session_selected_broadcaster = select_broadcaster;
			session_selected_second_broadcaster = select_second_broadcaster;
			
			last_match_time_stamp = new File(CricketUtil.CRICKET_DIRECTORY + CricketUtil.MATCHES_DIRECTORY + selectedMatch).lastModified();
			last_setup_time_stamp = new File(CricketUtil.CRICKET_DIRECTORY + CricketUtil.SETUP_DIRECTORY + selectedMatch).lastModified();
			last_Speed_time_stamp = new File(CricketUtil.CRICKET_DIRECTORY + "Speed/SPEED.txt").lastModified();
			
			session_configuration = new Configuration(selectedMatch, select_broadcaster, vizIPAddress, vizPortNumber,
					vizScene, vizLanguage, vizSecondaryIPAddress, vizSecondaryPortNumber, vizSecondaryScene, vizSecondaryLanguage, 
					vizTertiaryIPAddress, vizTertiaryPortNumber, vizTertiaryScene, vizTertiaryLanguage); 
			
			switch (session_selected_broadcaster) {
			case "FRUIT":
				session_selected_scenes.add(new Scene("D:/DOAD_In_House_Everest/Everest_Cricket/Everest_Cric2022/Scenes/Scorebug_Test.sum"
						,which_layer)); // Front layer
				session_selected_scenes.add(new Scene("","MIDDLE_LAYER"));
				break;
			}
			
			if(!vizIPAddress.trim().isEmpty()) {
				
				switch (session_selected_broadcaster) {
				case "FRUIT":
					this_fruit = new FRUIT();
					this_fruit.infobar = new Infobar();
					session_selected_scenes.get(0).scene_load(CricketFunctions.processPrintWriter(session_configuration).get(0), session_selected_broadcaster);
					break;
				}
			}

			model.addAttribute("manual_files", new File(CricketUtil.CRICKET_SERVER_DIRECTORY + "Manual/Data/").listFiles(new FileFilter() {
				@Override
			    public boolean accept(File pathname) {
			        String name = pathname.getName().toLowerCase();
			        return name.endsWith(".json") && pathname.isFile();
			    }
			}));
			
			JAXBContext.newInstance(Configuration.class).createMarshaller().marshal(session_configuration, 
					new File(CricketUtil.CRICKET_DIRECTORY + CricketUtil.CONFIGURATIONS_DIRECTORY + configuration_file_name));
			
			session_match = new MatchAllData();
			if(new File(CricketUtil.CRICKET_DIRECTORY + CricketUtil.SETUP_DIRECTORY + 
					selectedMatch).exists()) {
				session_match.setSetup(new ObjectMapper().readValue(new File(CricketUtil.CRICKET_DIRECTORY + CricketUtil.SETUP_DIRECTORY + 
						selectedMatch), Setup.class));
				session_match.setMatch(new ObjectMapper().readValue(new File(CricketUtil.CRICKET_DIRECTORY + CricketUtil.MATCHES_DIRECTORY + 
						selectedMatch), Match.class));
			}
			if(new File(CricketUtil.CRICKET_DIRECTORY + CricketUtil.EVENT_DIRECTORY + 
					selectedMatch).exists()) {
				session_match.setEventFile(new ObjectMapper().readValue(new File(CricketUtil.CRICKET_DIRECTORY + CricketUtil.EVENT_DIRECTORY + 
						selectedMatch), EventFile.class));
			}
			session_match.getMatch().setMatchFileName(selectedMatch);
			session_match = CricketFunctions.populateMatchVariables(cricketService, CricketFunctions.readOrSaveMatchFile(CricketUtil.READ,CricketUtil.SETUP + "," + 
					CricketUtil.MATCH + "," + CricketUtil.EVENT, session_match));			
			session_match.getSetup().setMatchFileTimeStamp(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()));
			
			past_tournament_stats = CricketFunctions.extractTournamentStats("PAST_MATCHES_DATA",false, cricket_matches, cricketService, session_match, null);
			
			for(Inning inn : session_match.getMatch().getInning()) {
				if(inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)) {
					model.addAttribute("current_inning", inn.getInningNumber());
					model.addAttribute("curr_team_total", inn.getBatting_team().getTeamName3() + "-" + inn.getTotalRuns() + "-" + inn.getTotalWickets() 
						+ " (" + CricketFunctions.OverBalls(inn.getTotalOvers(), inn.getTotalBalls()) + ")");
					for(BattingCard bc : inn.getBattingCard()) {
						if(bc.getOnStrike() != null) {
							if(bc.getOnStrike().equalsIgnoreCase(CricketUtil.YES)){
								model.addAttribute("curr_player", bc.getPlayer().getTicker_name().toUpperCase() + "* " + bc.getRuns() + "(" + bc.getBalls() + ")" );
							}
							if(bc.getOnStrike().equalsIgnoreCase(CricketUtil.NO)) {
								model.addAttribute("curr_player2", bc.getPlayer().getTicker_name().toUpperCase() + " " + bc.getRuns() + "(" + bc.getBalls() + ")" );
							}
						}
					}
					if(inn.getBowlingCard() != null) {
						for(BowlingCard boc : inn.getBowlingCard()) {
							if(boc.getStatus().equalsIgnoreCase(CricketUtil.CURRENT + CricketUtil.BOWLER)) {
								model.addAttribute("curr_bowler", boc.getPlayer().getTicker_name().toUpperCase() + ": " + boc.getWickets() + "-" + boc.getRuns() 
								+ "(" + CricketFunctions.OverBalls(boc.getOvers(), boc.getBalls()) + ")");
							}else if(boc.getStatus().equalsIgnoreCase(CricketUtil.LAST + CricketUtil.BOWLER)) {
								model.addAttribute("curr_bowler", boc.getPlayer().getTicker_name().toUpperCase() + ": " + boc.getWickets() + "-" + boc.getRuns() 
								+ "(" + CricketFunctions.OverBalls(boc.getOvers(), boc.getBalls()) + ")");
							}
						} 
					}
				}
			}
			model.addAttribute("session_match", session_match);
			model.addAttribute("session_configuration", session_configuration);
			model.addAttribute("session_selected_broadcaster", session_selected_broadcaster);
			model.addAttribute("session_selected_second_broadcaster", session_selected_second_broadcaster);
			model.addAttribute("session_selected_scenes",session_selected_scenes);
			
			return "output";
		}
	}

	@RequestMapping(value = {"/processCricketProcedures"}, method={RequestMethod.GET,RequestMethod.POST})    
	public @ResponseBody String processCricketProcedures(
		@ModelAttribute("session_configuration") Configuration session_configuration,
		@ModelAttribute("session_selected_broadcaster") String session_selected_broadcaster, 
		@ModelAttribute("session_selected_scenes") List<Scene> session_selected_scenes,
		@RequestParam(value = "whatToProcess", required = false, defaultValue = "") String whatToProcess,
		@RequestParam(value = "valueToProcess", required = false, defaultValue = "") String valueToProcess) 
					throws IOException, IllegalAccessException, InvocationTargetException, JAXBException, InterruptedException, CloneNotSupportedException, 
					ParseException, URISyntaxException 
	{
		switch (whatToProcess.toUpperCase()) {
		case "GET-CONFIG-DATA":

			session_configuration = (Configuration)JAXBContext.newInstance(Configuration.class).createUnmarshaller().unmarshal(
				new File(CricketUtil.CRICKET_DIRECTORY + CricketUtil.CONFIGURATIONS_DIRECTORY + valueToProcess));
			
			return JSONObject.fromObject(session_configuration).toString();
			
		case "ANIMATE_IN_SPEED_SECOND_BROADCASTER":
			switch (session_selected_second_broadcaster) {
			case "DOAD_LLC":
//				return (String) session_llc_big_screen.ProcessGraphicOption(whatToProcess, session_match, cricketService, cricket_matches, 
//						CricketFunctions.processPrintWriter(session_configuration).get(1), session_selected_scenes, valueToProcess, session_statistics);
			}
			
		case "RE_READ_DATA":
			
			File files[] = new File(CricketUtil.CRICKET_SERVER_DIRECTORY + CricketUtil.MATCHES_DIRECTORY).listFiles(new FileFilter() {
				@Override
			    public boolean accept(File pathname) {
			        String name = pathname.getName().toLowerCase();
			        return name.endsWith(".json") && pathname.isFile();
			    }
			});
			
			cricket_matches = CricketFunctions.getTournamentMatches(files, cricketService);
			session_statistics = cricketService.getAllStats();
			session_match = CricketFunctions.populateMatchVariables(cricketService, CricketFunctions.readOrSaveMatchFile(CricketUtil.READ,
					CricketUtil.MATCH, session_match));
			
			return JSONObject.fromObject(session_match).toString();
			
		case "SHOW_SPEED":
			
			if(show_speed == true) {
				show_speed = false;
			}else {
				show_speed = true;
			}
			
			return String.valueOf(show_speed);
			
		case "NAMESUPER_GRAPHICS-OPTIONS": case "L3_MATCH-PROMO_GRAPHICS-OPTIONS": case "BUG_DB_GRAPHICS-OPTIONS": case "MOST_GRAPHICS-OPTIONS": 
		case "MOST1_GRAPHICS-OPTIONS": case "MOST1_WICKETS_GRAPHICS-OPTIONS": case "MOST_LEADERBOARD_GRAPHICS-OPTIONS":
		case "LEADERBOARD_GRAPHICS-OPTIONS": case "WICKETS_GRAPHICS-OPTIONS": case "FOURS_GRAPHICS-OPTIONS": case "SIXES_GRAPHICS-OPTIONS":
		case "BUG_DB2_GRAPHICS-OPTIONS": case "POPULATE-LASTX": case "HOWOUT_BOTH_GRAPHICS-OPTIONS": case "BATSMANSTATS_BOTH_GRAPHICS-OPTIONS":
		case "THIS_SESSION_GRAPHICS-OPTIONS": case "LT_POINTERS_GRAPHICS-OPTIONS": case "FF_POINTERS_GRAPHICS-OPTIONS": case "POINTER_GRAPHICS-OPTIONS":
		case "MATCH_GRAPHICS-OPTIONS":
			
			switch (session_selected_broadcaster) {
			case "FRUIT":
				return (String) this_fruit.ProcessGraphicOption(whatToProcess, session_match, cricketService, cricket_matches, 
						CricketFunctions.processPrintWriter(session_configuration).get(0), session_selected_scenes, valueToProcess, session_statistics);
			}
			
		case "PROMPT_GRAPHICS-OPTIONS": case "TEAM_FIXTURES_GRAPHICS-OPTIONS": case "TEAM_SQUAD_GRAPHICS-OPTIONS":
			switch (session_selected_broadcaster) {
			case "FRUIT":
				return (String) this_fruit.ProcessGraphicOption(whatToProcess, session_match, cricketService, cricket_matches, 
						CricketFunctions.processPrintWriter(session_configuration).get(0), session_selected_scenes, valueToProcess, session_statistics);
			}
		case "MATCH-PROMO_GRAPHICS-OPTIONS": case "PREVIOUS_SUMMARY_GRAPHICS-OPTIONS": case "LT-TIEID-DOUBLE_GRAPHICS-OPTIONS": 
		case "LTMATCH-PROMO_GRAPHICS-OPTIONS": case "PLAYOFF_GRAPHICS-OPTIONS":
			return JSONArray.fromObject(CricketFunctions.processAllFixtures(cricketService)).toString();
		case "READ-MATCH-AND-POPULATE":

			if(last_match_time_stamp != new File(CricketUtil.CRICKET_DIRECTORY + CricketUtil.MATCHES_DIRECTORY 
				+ session_match.getMatch().getMatchFileName()).lastModified()
				|| last_Speed_time_stamp != new File(CricketUtil.CRICKET_DIRECTORY + "Speed/SPEED.txt").lastModified()) {
				
				session_match = CricketFunctions.populateMatchVariables(cricketService, CricketFunctions.readOrSaveMatchFile(CricketUtil.READ,
						CricketUtil.SETUP + "," + CricketUtil.MATCH + "," + CricketUtil.EVENT, session_match));
			}

			switch (session_selected_broadcaster) {
			case "FRUIT":
				if(last_match_time_stamp != new File(CricketUtil.CRICKET_DIRECTORY + CricketUtil.MATCHES_DIRECTORY 
						+ session_match.getMatch().getMatchFileName()).lastModified()) {
					session_match = CricketFunctions.populateMatchVariables(cricketService, CricketFunctions.readOrSaveMatchFile(CricketUtil.READ,
							CricketUtil.SETUP + "," + CricketUtil.MATCH + "," + CricketUtil.EVENT, session_match));
					this_fruit.updateInfobar(session_selected_scenes.get(0), session_match,show_speed, CricketFunctions.processPrintWriter(session_configuration).get(0));
					last_match_time_stamp = new File(CricketUtil.CRICKET_DIRECTORY + CricketUtil.MATCHES_DIRECTORY 
							+ session_match.getMatch().getMatchFileName()).lastModified();
				}
				if(last_Speed_time_stamp != new File(CricketUtil.CRICKET_DIRECTORY + "Speed/SPEED.txt").lastModified()) {
					session_match = CricketFunctions.populateMatchVariables(cricketService, CricketFunctions.readOrSaveMatchFile(CricketUtil.READ,
						CricketUtil.SETUP + "," + CricketUtil.MATCH + "," + CricketUtil.EVENT, session_match));
					this_fruit.updateSpeed(session_selected_scenes.get(0), session_match,show_speed, 
							CricketFunctions.processPrintWriter(session_configuration).get(0));
					last_Speed_time_stamp = new File(CricketUtil.CRICKET_DIRECTORY + "Speed/SPEED.txt").lastModified();
				}
				
				break;
			}

		default:
			switch (session_selected_broadcaster) {
			case "FRUIT":
				this_fruit.ProcessGraphicOption(whatToProcess, session_match, cricketService, cricket_matches, 
						CricketFunctions.processPrintWriter(session_configuration).get(0), session_selected_scenes, valueToProcess, session_statistics);
				break;
			}
			return JSONObject.fromObject(session_match).toString();
		}
	}
	@ModelAttribute("session_configuration")
	public Configuration session_configuration(){
		return new Configuration();
	} 
	@ModelAttribute("session_selected_scenes")
	public List<Scene> session_selected_scenes(){
		return new ArrayList<Scene>();
	}
	@ModelAttribute("session_selected_broadcaster")
	public String session_selected_broadcaster(){
		return new String();
	}
}