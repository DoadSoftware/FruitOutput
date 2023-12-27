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

import com.cricket.broadcaster.FRUIT;
import com.cricket.containers.Infobar;
import com.cricket.containers.Scene;
import com.cricket.model.BattingCard;
import com.cricket.model.BowlingCard;
import com.cricket.model.Configuration;
import com.cricket.model.EventFile;
import com.cricket.model.Inning;
import com.cricket.model.Match;
import com.cricket.model.MatchAllData;
import com.cricket.model.MultiLanguageDatabase;
import com.cricket.model.Setup;
import com.cricket.model.Speed;
import com.cricket.service.CricketService;
import com.cricket.util.CricketFunctions;
import com.cricket.util.CricketUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.sf.json.JSONObject;
	

@Controller
@SessionAttributes(value = {"session_configuration","session_selected_broadcaster","session_selected_scenes"})
public class IndexController 
{
	@Autowired
	CricketService cricketService;
	public static MatchAllData session_match;
	public static MultiLanguageDatabase multiLanguage;
	
	public static FRUIT this_fruit;
	public static String expiry_date = "2024-11-30";
	public static String current_date;
	public static Speed lastSpeed = new Speed();
	public static long time_elapsed = 0;
	public static long last_setup_time_stamp = 0;
	public static long last_match_time_stamp = 0;
	public static long last_Review_time_stamp = 0;

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
			@RequestParam(value = "speed_select", required = false, defaultValue = "") String speed_select,
			@RequestParam(value = "select_cricket_matches", required = false, defaultValue = "") String selectedMatch,
			@RequestParam(value = "vizIPAddress", required = false, defaultValue = "") String vizIPAddress,
			@RequestParam(value = "vizPortNumber", required = false, defaultValue = "") int vizPortNumber,
			@RequestParam(value = "vizSceneName", required = false, defaultValue = "") String vizScene,
			@RequestParam(value = "vizLanguage", required = false, defaultValue = "") String vizLanguage) 
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
			
			last_match_time_stamp = new File(CricketUtil.CRICKET_DIRECTORY + CricketUtil.MATCHES_DIRECTORY + selectedMatch).lastModified();
			last_setup_time_stamp = new File(CricketUtil.CRICKET_DIRECTORY + CricketUtil.SETUP_DIRECTORY + selectedMatch).lastModified();
			
			session_configuration = new Configuration(selectedMatch, session_selected_broadcaster, speed_select, vizIPAddress, vizPortNumber, vizLanguage);
			
			JAXBContext.newInstance(Configuration.class).createMarshaller().marshal(session_configuration, 
					new File(CricketUtil.CRICKET_DIRECTORY + CricketUtil.CONFIGURATIONS_DIRECTORY + configuration_file_name));
			
			
			switch (session_selected_broadcaster) {
			case "FRUIT":
				this_fruit = new FRUIT();
				this_fruit.infobar = new Infobar();
				session_selected_scenes.add(new Scene("D:/DOAD_In_House_Everest/Everest_Cricket/EVEREST_FRUIT/Scenes/Fruit.sum","FRONT_LAYER")); // Front layer
				session_selected_scenes.add(new Scene("","MIDDLE_LAYER"));
				
				session_selected_scenes.get(0).scene_load(CricketFunctions.processPrintWriter(session_configuration).get(0), session_selected_broadcaster);
				break;
			}
			
			session_match = new MatchAllData();
			if(new File(CricketUtil.CRICKET_DIRECTORY + CricketUtil.SETUP_DIRECTORY + selectedMatch).exists()) {
				session_match.setSetup(new ObjectMapper().readValue(new File(CricketUtil.CRICKET_DIRECTORY + CricketUtil.SETUP_DIRECTORY + 
						selectedMatch), Setup.class));
				session_match.setMatch(new ObjectMapper().readValue(new File(CricketUtil.CRICKET_DIRECTORY + CricketUtil.MATCHES_DIRECTORY + 
						selectedMatch), Match.class));
			}
			if(new File(CricketUtil.CRICKET_DIRECTORY + CricketUtil.EVENT_DIRECTORY + 	selectedMatch).exists()) {
				session_match.setEventFile(new ObjectMapper().readValue(new File(CricketUtil.CRICKET_DIRECTORY + CricketUtil.EVENT_DIRECTORY + 
						selectedMatch), EventFile.class));
			}
			session_match.getMatch().setMatchFileName(selectedMatch);
			session_match = CricketFunctions.populateMatchVariables(cricketService, CricketFunctions.readOrSaveMatchFile(CricketUtil.READ,CricketUtil.SETUP + "," + 
					CricketUtil.MATCH + "," + CricketUtil.EVENT, session_match));			
			session_match.getSetup().setMatchFileTimeStamp(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()));

			model.addAttribute("session_match", session_match);
			model.addAttribute("session_configuration", session_configuration);
			model.addAttribute("session_selected_broadcaster", session_selected_broadcaster);
			model.addAttribute("session_selected_scenes",session_selected_scenes);
			
			this_fruit.initialize_fruit(CricketFunctions.processPrintWriter(session_configuration).get(0), session_match,session_configuration);
			if(new File(CricketUtil.CRICKET_DIRECTORY + "Speed/SPEED.txt").exists()) {
				lastSpeed.setSpeedFileModifiedTime(new File(CricketUtil.CRICKET_DIRECTORY + "Speed/SPEED.txt").lastModified());
			}else {
				lastSpeed.setSpeedFileModifiedTime(0);
			}
			
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
					throws Exception 
	{
		switch (whatToProcess.toUpperCase()) {
		case "GET-CONFIG-DATA":

			session_configuration = (Configuration)JAXBContext.newInstance(Configuration.class).createUnmarshaller().unmarshal(
				new File(CricketUtil.CRICKET_DIRECTORY + CricketUtil.CONFIGURATIONS_DIRECTORY + valueToProcess));
			
			return JSONObject.fromObject(session_configuration).toString();
						
		case "RE_READ_DATA":case "UPDATE-MATCH-ON-OUTPUT-FORM":
			session_match = CricketFunctions.populateMatchVariables(cricketService, CricketFunctions.readOrSaveMatchFile(CricketUtil.READ,CricketUtil.SETUP + "," + 
					CricketUtil.MATCH + "," + CricketUtil.EVENT, session_match));
			return JSONObject.fromObject(session_match).toString();
		case "READ-MATCH-AND-POPULATE":
			switch (session_selected_broadcaster) {
			case "FRUIT":
				if(last_match_time_stamp != new File(CricketUtil.CRICKET_DIRECTORY + CricketUtil.MATCHES_DIRECTORY 
						+ session_match.getMatch().getMatchFileName()).lastModified()) {
					
//					System.out.println("NAME : " + session_match.getSetup().getHomeSquad().get(0).getPlayerId());
//					session_match = CricketFunctions.populateMatchVariables(cricketService, CricketFunctions.readOrSaveMatchFile(CricketUtil.READ,
//							CricketUtil.MATCH + "," + CricketUtil.EVENT, session_match));
					
					session_match = CricketFunctions.populateMatchVariables(cricketService, CricketFunctions.readOrSaveMatchFile(CricketUtil.READ,
							CricketUtil.SETUP + "," + CricketUtil.MATCH + "," + CricketUtil.EVENT, session_match));
					
					this_fruit.updateInfobar(session_selected_scenes.get(0), session_match,CricketFunctions.processPrintWriter(session_configuration).get(0));
					
					last_match_time_stamp = new File(CricketUtil.CRICKET_DIRECTORY + CricketUtil.MATCHES_DIRECTORY 
							+ session_match.getMatch().getMatchFileName()).lastModified();
				}
				
				if(new File(CricketUtil.CRICKET_DIRECTORY + "Speed/SPEED.txt").exists() 
						&& lastSpeed.getSpeedFileModifiedTime() != new File(CricketUtil.CRICKET_DIRECTORY + "Speed/SPEED.txt").lastModified()) {
						this_fruit.populateSpeed(CricketFunctions.processPrintWriter(session_configuration).get(0),lastSpeed);
						lastSpeed.setSpeedFileModifiedTime(new File(CricketUtil.CRICKET_DIRECTORY + "Speed/SPEED.txt").lastModified());
					}
				if(new File(CricketUtil.REVIEWS).exists() &&
						last_Review_time_stamp!=new File(CricketUtil.REVIEWS).lastModified()) {
						this_fruit.populateReview(CricketFunctions.processPrintWriter(session_configuration).get(0), session_match, last_Review_time_stamp);
						last_Review_time_stamp=new File(CricketUtil.REVIEWS).lastModified();
				}
				
				break;
			}

		default:
			switch (session_selected_broadcaster) {
			case "FRUIT":
				this_fruit.ProcessGraphicOption(whatToProcess, session_match, cricketService, 
						CricketFunctions.processPrintWriter(session_configuration).get(0), session_selected_scenes, valueToProcess);
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