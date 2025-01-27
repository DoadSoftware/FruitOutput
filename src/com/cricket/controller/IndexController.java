package com.cricket.controller;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.bind.JAXBContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cricket.broadcaster.DOAD_FRUIT;
import com.cricket.broadcaster.ISPL_FRUIT;
import com.cricket.broadcaster.LCT_FRUIT;
import com.cricket.containers.Scene;
import com.cricket.model.Configuration;
import com.cricket.model.Match;
import com.cricket.model.MatchAllData;
import com.cricket.model.Review;
import com.cricket.model.Speed;
import com.cricket.service.CricketService;
import com.cricket.util.CricketFunctions;
import com.cricket.util.CricketUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.json.JSONObject;
	
@Controller
public class IndexController 
{
	@Autowired
	CricketService cricketService;

	public static MatchAllData session_match;
	public static DOAD_FRUIT this_fruit;
	public static LCT_FRUIT this_fruit_lct;
	public static ISPL_FRUIT this_ispl_fruit;
	public static String expiry_date = "2025-12-31";
	public static String current_date;
	public static Speed lastSpeed = new Speed(" ",0);
	public static Review lastReview = new Review();
	public static long time_elapsed = 0;
	public static long last_match_time_stamp = 0;
	public static Configuration session_configuration = new Configuration();
	public static List<Scene> session_selected_scenes = new ArrayList<Scene>();

	@RequestMapping(value = {"/","/initialise"}, method={RequestMethod.GET,RequestMethod.POST}) 
	public String initialisePage(ModelMap model) throws MalformedURLException, IOException
	{
		
		if(current_date == null || current_date.isEmpty()) {
			current_date = CricketFunctions.getOnlineCurrentDate();
		}

		model.addAttribute("match_files", new File(CricketUtil.CRICKET_SERVER_DIRECTORY + CricketUtil.MATCHES_DIRECTORY).listFiles(new FileFilter() {
			@Override
		    public boolean accept(File pathname) {
		        String name = pathname.getName().toLowerCase();
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
			@RequestParam(value = "configuration_file_name", required = false, defaultValue = "") String configuration_file_name,
			@RequestParam(value = "select_broadcaster", required = false, defaultValue = "") String select_broadcaster,
			@RequestParam(value = "speed_select", required = false, defaultValue = "") String speed_select,
			@RequestParam(value = "select_audio", required = false, defaultValue = "") String select_audio,
			@RequestParam(value = "select_cricket_matches", required = false, defaultValue = "") String selectedMatch,
			@RequestParam(value = "vizIPAddress", required = false, defaultValue = "") String vizIPAddress,
			@RequestParam(value = "vizPortNumber", required = false, defaultValue = "") int vizPortNumber) 
					throws Exception 
	{
		if(current_date == null || current_date.isEmpty()) {
			
			model.addAttribute("error_message","You must be connected to the internet online");
			return "error";
		
		} else if(new SimpleDateFormat("yyyy-MM-dd").parse(expiry_date).before(new SimpleDateFormat("yyyy-MM-dd").parse(current_date))) {
			
			model.addAttribute("error_message","This software has expired");
			return "error";
			
		}else {
		
			last_match_time_stamp = new File(CricketUtil.CRICKET_DIRECTORY + CricketUtil.MATCHES_DIRECTORY 
				+ selectedMatch).lastModified();
			
			session_configuration = new Configuration(selectedMatch, select_broadcaster, 
					speed_select,"","", select_audio,vizIPAddress, vizPortNumber,"");
			
			JAXBContext.newInstance(Configuration.class).createMarshaller().marshal(session_configuration, 
					new File(CricketUtil.CRICKET_DIRECTORY + CricketUtil.CONFIGURATIONS_DIRECTORY + configuration_file_name));
			

			session_match = new MatchAllData();
			session_match.setMatch(new ObjectMapper().readValue(new File(CricketUtil.CRICKET_DIRECTORY + 
				CricketUtil.MATCHES_DIRECTORY + selectedMatch), Match.class));
			
			session_match.getMatch().setMatchFileName(selectedMatch);
			session_match = CricketFunctions.populateMatchVariables(cricketService, CricketFunctions.readOrSaveMatchFile(CricketUtil.READ,
				CricketUtil.SETUP + "," + CricketUtil.MATCH + "," + CricketUtil.EVENT, session_match));
			session_match.getSetup().setMatchFileTimeStamp(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()));
			
			model.addAttribute("session_match", session_match);
			model.addAttribute("session_configuration", session_configuration);
			model.addAttribute("session_selected_broadcaster", select_broadcaster);
			model.addAttribute("session_selected_scenes",session_selected_scenes);
			switch (select_broadcaster) {
			
			case CricketUtil.DOAD_FRUIT:
				this_fruit = new DOAD_FRUIT();
				session_selected_scenes.add(0,new Scene(CricketUtil.Doad_Fruit_scene,"FRONT_LAYER")); // Front layer
				session_selected_scenes.get(0).scene_load(CricketFunctions
						.processPrintWriter(session_configuration).get(0), select_broadcaster);
				this_fruit.initialize_fruit(CricketFunctions.processPrintWriter(
						session_configuration).get(0), session_match,session_configuration);
				//CricketFunctions.getInteractive(session_match, "FULL_WRITE");	
				break;
			case CricketUtil.ISPL_FRUIT:
				this_ispl_fruit = new ISPL_FRUIT();
				session_selected_scenes.add(0,new Scene(CricketUtil.Doad_Fruit_scene,"FRONT_LAYER")); // Front layer
				session_selected_scenes.get(0).scene_load(CricketFunctions
						.processPrintWriter(session_configuration).get(0), select_broadcaster);
				this_ispl_fruit.initialize_fruit(CricketFunctions.processPrintWriter(
						session_configuration).get(0), session_match,session_configuration);
				
				this_ispl_fruit.ProcessGraphicOption("ANIMATE-IN-LOGO", session_match, cricketService, 
						CricketFunctions.processPrintWriter(session_configuration).get(0), session_selected_scenes,"",
						session_configuration);
				//CricketFunctions.getInteractive(session_match, "FULL_WRITE");	
				break;
			case "LCT_FRUIT":
				this_fruit_lct = new LCT_FRUIT();
				session_selected_scenes.add(0,new Scene("D:/DOAD_In_House_Everest/Everest_Cricket/EVEREST_FRUIT_LCT/Scenes/Fruit.sum","FRONT_LAYER")); // Front layer
				session_selected_scenes.get(0).scene_load(CricketFunctions
						.processPrintWriter(session_configuration).get(0), select_broadcaster);
				this_fruit_lct.initialize_fruit(CricketFunctions.processPrintWriter(
						session_configuration).get(0), session_match,session_configuration);
				this_fruit_lct.ProcessGraphicOption("ANIMATE-IN-LOGO", session_match, cricketService, 
						CricketFunctions.processPrintWriter(session_configuration).get(0), session_selected_scenes,"",
						session_configuration);
				break;
				
			}
			
			if(select_broadcaster.equalsIgnoreCase(CricketUtil.ISPL_FRUIT)) {
				if(new File("C:/Sports/Cricket/VE/VR-Speed.txt").exists()) {
					lastSpeed.setSpeedFileModifiedTime(new File("C:/Sports/Cricket/VE/VR-Speed.txt").lastModified());
				}	
			}else {
				if(new File(CricketUtil.CRICKET_DIRECTORY + CricketUtil.SPEED_DIRECTORY + CricketUtil.SPEED_TXT).exists()) {
					lastSpeed.setSpeedFileModifiedTime(new File(CricketUtil.CRICKET_DIRECTORY + CricketUtil.SPEED_DIRECTORY 
							+ CricketUtil.SPEED_TXT).lastModified());
				}
			}
			if(new File(CricketUtil.REVIEWS).exists()) {
				lastReview.setLastTimeStamp(new File(CricketUtil.REVIEWS).lastModified());
			}else {
				lastReview.setLastTimeStamp(0);
			}
			return "output";
		}
	}

	@RequestMapping(value = {"/processCricketProcedures"}, method={RequestMethod.GET,RequestMethod.POST})    
	public @ResponseBody String processCricketProcedures(
		@RequestParam(value = "whatToProcess", required = false, defaultValue = "") String whatToProcess,
		@RequestParam(value = "valueToProcess", required = false, defaultValue = "") String valueToProcess) 
					throws Exception 
	{
		Speed this_speed = new Speed();
		Review this_review= new Review();
		
		switch (whatToProcess.toUpperCase()) {
		case "GET-CONFIG-DATA":

			session_configuration = (Configuration)JAXBContext.newInstance(Configuration.class).createUnmarshaller().unmarshal(
				new File(CricketUtil.CRICKET_DIRECTORY + CricketUtil.CONFIGURATIONS_DIRECTORY + valueToProcess));
			
			return JSONObject.fromObject(session_configuration).toString();
						
		case "RE_READ_DATA":
			
			session_match = CricketFunctions.populateMatchVariables(cricketService, CricketFunctions.readOrSaveMatchFile(CricketUtil.READ,
				CricketUtil.SETUP + "," + CricketUtil.MATCH + "," + CricketUtil.EVENT, session_match));
			return JSONObject.fromObject(session_match).toString();
		
		case "READ-MATCH-AND-POPULATE":

			switch (session_configuration.getBroadcaster()) {
			case CricketUtil.DOAD_FRUIT: 
				
				if(session_match.getMatch() != null && last_match_time_stamp != new File(CricketUtil.CRICKET_DIRECTORY 
					+ CricketUtil.MATCHES_DIRECTORY + session_match.getMatch().getMatchFileName()).lastModified()) {
					
					session_match = CricketFunctions.populateMatchVariables(cricketService, CricketFunctions.readOrSaveMatchFile(CricketUtil.READ,
							CricketUtil.MATCH + "," + CricketUtil.EVENT, session_match));

					if(!session_configuration.getPrimaryIpAddress().isEmpty()) {
						this_fruit.updateFruit(session_selected_scenes.get(0), 
						session_match,CricketFunctions.processPrintWriter(session_configuration).get(0),session_configuration);
					}
					last_match_time_stamp = new File(CricketUtil.CRICKET_DIRECTORY + CricketUtil.MATCHES_DIRECTORY 
						+ session_match.getMatch().getMatchFileName()).lastModified();
				}

				if(!session_configuration.getPrimaryIpAddress().isEmpty()) {
					
					this_speed = CricketFunctions.getCurrentSpeed(CricketUtil.CRICKET_DIRECTORY 
							+ CricketUtil.SPEED_DIRECTORY + CricketUtil.SPEED_TXT, lastSpeed);
					if(this_speed != null) {
						this_fruit.populateSpeed(CricketFunctions.processPrintWriter(session_configuration).get(0),this_speed);
						lastSpeed = this_speed;
					}
					this_review=CricketFunctions.getCurrentReview(CricketUtil.REVIEWS, lastReview);
					if(this_review != null) {
						this_fruit.populateReview(CricketFunctions.processPrintWriter(session_configuration).get(0), session_match,lastReview);
						lastReview=this_review;
					}
				}
				break;
			
			case CricketUtil.ISPL_FRUIT: 
				
				if(session_match.getMatch() != null && last_match_time_stamp != new File(CricketUtil.CRICKET_DIRECTORY 
					+ CricketUtil.MATCHES_DIRECTORY + session_match.getMatch().getMatchFileName()).lastModified()) {
					
					session_match = CricketFunctions.populateMatchVariables(cricketService, CricketFunctions.readOrSaveMatchFile(CricketUtil.READ,
							CricketUtil.MATCH + "," + CricketUtil.EVENT, session_match));
					
					//CricketFunctions.getInteractive(session_match,"FULL_WRITE");

					if(!session_configuration.getPrimaryIpAddress().isEmpty()) {
						this_ispl_fruit.updateFruit(session_selected_scenes.get(0), 
						session_match,CricketFunctions.processPrintWriter(session_configuration).get(0),session_configuration);
					}
					last_match_time_stamp = new File(CricketUtil.CRICKET_DIRECTORY + CricketUtil.MATCHES_DIRECTORY 
						+ session_match.getMatch().getMatchFileName()).lastModified();
				}

				if(!session_configuration.getPrimaryIpAddress().isEmpty()) {
					
					this_speed = CricketFunctions.getCurrentSpeed("C:/Sports/Cricket/VE/VR-Speed.txt", lastSpeed);
					if(this_speed != null) {
						this_ispl_fruit.populateSpeed(CricketFunctions.processPrintWriter(session_configuration).get(0),this_speed);
						lastSpeed = this_speed;
					}
					this_review=CricketFunctions.getCurrentReview(CricketUtil.REVIEWS, lastReview);
					if(this_review != null) {
						this_ispl_fruit.populateReview(CricketFunctions.processPrintWriter(session_configuration).get(0), session_match,lastReview);
						lastReview=this_review;
					}
				}
				break;
			case "LCT_FRUIT":
				if(session_match.getMatch() != null && last_match_time_stamp != new File(CricketUtil.CRICKET_DIRECTORY 
						+ CricketUtil.MATCHES_DIRECTORY + session_match.getMatch().getMatchFileName()).lastModified()) {
						
						session_match = CricketFunctions.populateMatchVariables(cricketService, CricketFunctions.readOrSaveMatchFile(CricketUtil.READ,
								CricketUtil.MATCH + "," + CricketUtil.EVENT, session_match));

						if(!session_configuration.getPrimaryIpAddress().isEmpty()) {
							this_fruit_lct.updateFruit(session_selected_scenes.get(0), 
							session_match,CricketFunctions.processPrintWriter(session_configuration).get(0),session_configuration);
						}
						last_match_time_stamp = new File(CricketUtil.CRICKET_DIRECTORY + CricketUtil.MATCHES_DIRECTORY 
							+ session_match.getMatch().getMatchFileName()).lastModified();
					}

					if(!session_configuration.getPrimaryIpAddress().isEmpty()) {
						
						this_speed = CricketFunctions.getCurrentSpeed(CricketUtil.CRICKET_DIRECTORY 
								+ CricketUtil.SPEED_DIRECTORY + CricketUtil.SPEED_TXT, lastSpeed);
						if(this_speed != null) {
							this_fruit_lct.populateSpeed(CricketFunctions.processPrintWriter(session_configuration).get(0),this_speed);
							lastSpeed = this_speed;
						}
						this_review=CricketFunctions.getCurrentReview(CricketUtil.REVIEWS, lastReview);
						if(this_review != null) {
							this_fruit_lct.populateReview(CricketFunctions.processPrintWriter(session_configuration).get(0), session_match,lastReview);
							lastReview=this_review;
						}
					}
				break;
			
			}
			return JSONObject.fromObject(session_match).toString();

		default:
			
			switch (session_configuration.getBroadcaster()) {
			case "DOAD_FRUIT":
				this_fruit.ProcessGraphicOption(whatToProcess, session_match, cricketService, 
					CricketFunctions.processPrintWriter(session_configuration).get(0), session_selected_scenes, valueToProcess,session_configuration);
				break;
			case CricketUtil.ISPL_FRUIT:
				//CricketFunctions.getInteractive(session_match, "FULL_WRITE");	
				this_ispl_fruit.ProcessGraphicOption(whatToProcess, session_match, cricketService, 
						CricketFunctions.processPrintWriter(session_configuration).get(0), session_selected_scenes, valueToProcess,session_configuration);
				break;
			case "LCT_FRUIT":
				this_fruit_lct.ProcessGraphicOption(whatToProcess, session_match, cricketService, 
						CricketFunctions.processPrintWriter(session_configuration).get(0), session_selected_scenes, valueToProcess,session_configuration);
					break;
			
			}
			
			return JSONObject.fromObject(null).toString();
		}
	}
}