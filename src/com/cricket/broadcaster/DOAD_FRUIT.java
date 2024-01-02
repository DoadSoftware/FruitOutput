package com.cricket.broadcaster;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

public class DOAD_FRUIT extends Scene{

	public Infobar infobar = new Infobar();
	public String session_selected_broadcaster = "DOAD_FRUIT";
	public String which_graphics_onscreen = "";
	public int previous_bowler = 0;
	boolean player_found = false;
	List<String>str=new ArrayList<String>();
	
	public DOAD_FRUIT() {
		super();
	}
	
	public DOAD_FRUIT(String scene_path, String which_Layer) {
		super(scene_path, which_Layer);
	}

	public void updateFruit(Scene scene, MatchAllData match,PrintWriter print_writer) throws Exception
	{
		populateFruit(match, print_writer);

	}
	
	public Object ProcessGraphicOption(String whatToProcess, MatchAllData match, CricketService cricketService,
			PrintWriter print_writer, List<Scene> scenes, String valueToProcess) throws Exception{
			
			switch (whatToProcess.toUpperCase()) {
			case "POPULATE-FRUIT": 
				populateFruit(match, print_writer);
			case "ANIMATE-IN-FRUIT": case "ANIMATE-OUT": case "CLEAR-ALL": 
				switch (session_selected_broadcaster.toUpperCase()) {
				case "DOAD_FRUIT":
					switch (whatToProcess.toUpperCase()) {
					case "ANIMATE-IN-FRUIT":
						processAnimation(print_writer, "In", "START", session_selected_broadcaster,1);
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
							break;
						}
						break;
					}
				}
			}
			return null;
		}
	public void populateInfobar(PrintWriter print_writer,String viz_scene, MatchAllData match, String session_selected_broadcaster) throws Exception 
	{
		populateFruit(match, print_writer);
	}
	
	/*	index		data_int						data_str
	 * 	  0			last Bowler ID			        inning 1 dots
	 * 	  1			ball since last boundary		inning 1 comparision w.r.t. inning 2
	 * 	  2											PreOtherRunWicket
	 * 	  3											This over
	 * 	  4											last 30 balls
	 *	  5
	 *
	 */	  
	
	public void populateFruit( MatchAllData match,PrintWriter print_writer) throws Exception 
	{
		
		BattingCard this_bc;
		List<Integer> data_int = new ArrayList<Integer>();
		List<String> data_str = new ArrayList<String>();
		List<Boolean> data_bool = new ArrayList<Boolean>();

		List<String> arr1 =CricketFunctions.getFirstPowerPlayScores(match, Arrays.asList(1, 2), match.getEventFile().getEvents());
		List<String> arr2 =CricketFunctions.getSecPowerPlayScores(match, Arrays.asList(1, 2), match.getEventFile().getEvents());
		List<String> arr3 =CricketFunctions.getThirdPowerPlayScore(match, Arrays.asList(1, 2), match.getEventFile().getEvents());
						
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomeStatValue1 " + match.getMatch().getInning().get(0).getTotalFours() + ";");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomeStatValue2 " + match.getMatch().getInning().get(0).getTotalSixes() + ";");

//		data_str = new ArrayList<String>();
//		for(int i = 0; i < 12; i++) {
//			data_str.add("");
//		}

		data_bool = new ArrayList<Boolean>();
		data_bool.add(false); // Balls since last boundary 
		int ballcount=0,run_c=0,wkt_c=0;
		data_int = new ArrayList<Integer>();
		data_int.add(0); // 0 -> Total runs inning1 w.r.t inn 2
		data_int.add(0); // 1 -> Total wickets inning1 w.r.t inn 2
		data_int.add(0); // 2 -> Last bowler ID
		data_int.add(0); // 3 -> Balls since last boundary
		data_int.add(0); // 4 -> 1st Innings dot balls
		data_int.add(0); // 5 -> 2nd Innings dot balls
		data_int.add(0);
		
		if (match.getEventFile().getEvents() != null && match.getEventFile().getEvents().size() > 0) {
			for (int i = match.getEventFile().getEvents().size() - 1; i >= 0; i--) {
				// Inning comparison
				if(match.getEventFile().getEvents().get(i).getEventInningNumber() == 1) {
					if (match.getEventFile().getEvents().get(i).getEventOverNo() == match.getMatch().getInning().get(1).getTotalOvers()
		                && match.getEventFile().getEvents().get(i).getEventBallNo() == match.getMatch().getInning().get(1).getTotalBalls()) {
						switch (match.getEventFile().getEvents().get(i).getEventType()) {
						case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE:  case CricketUtil.FIVE : 
						case CricketUtil.DOT: case CricketUtil.FOUR: case CricketUtil.SIX: 
							data_int.set(0, data_int.get(0) + match.getEventFile().getEvents().get(i).getEventRuns());
							break;
							case CricketUtil.WIDE: case CricketUtil.NO_BALL: case CricketUtil.BYE: case CricketUtil.LEG_BYE: case CricketUtil.PENALTY:
							data_int.set(0, data_int.get(0) + match.getEventFile().getEvents().get(i).getEventRuns()+
									 match.getEventFile().getEvents().get(i).getEventSubExtraRuns()
									 + match.getEventFile().getEvents().get(i).getEventExtraRuns());
							break;
						case CricketUtil.LOG_WICKET:
							data_int.set(0, data_int.get(0) + match.getEventFile().getEvents().get(i).getEventRuns());
							switch (match.getEventFile().getEvents().get(i).getEventHowOut()) {
							case CricketUtil.RETIRED_HURT: case CricketUtil.ABSENT_HURT: case CricketUtil.CONCUSSED:
								break;
							default:
								data_int.set(1, data_int.get(1) + 1);
							}
							break;
						case CricketUtil.LOG_ANY_BALL:
							data_int.set(0, data_int.get(0) + match.getEventFile().getEvents().get(i).getEventRuns());
					        if (match.getEventFile().getEvents().get(i).getEventExtra() != null 
					        	&& !match.getEventFile().getEvents().get(i).getEventExtra().isEmpty()) {
								data_int.set(0, data_int.get(0) + match.getEventFile().getEvents().get(i).getEventExtraRuns());
					        }
					        if (match.getEventFile().getEvents().get(i).getEventSubExtra() != null 
								&& !match.getEventFile().getEvents().get(i).getEventSubExtra().isEmpty()) {
								data_int.set(0, data_int.get(0) + match.getEventFile().getEvents().get(i).getEventSubExtraRuns());
					        }
					        if (match.getEventFile().getEvents().get(i).getEventHowOut() != null 
								&& !match.getEventFile().getEvents().get(i).getEventHowOut().isEmpty()) {
								data_int.set(1, data_int.get(1) + 1);
					        }
							break;
						}
					}
				}
				// Last bowler
				if(match.getEventFile().getEvents().get(i).getEventInningNumber() 
					== match.getMatch().getInning().stream().filter(inn -> inn.getIsCurrentInning()
					.equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null).getInningNumber()) {
					if (match.getEventFile().getEvents().get(i).getEventType().equalsIgnoreCase(CricketUtil.END_OVER)) {
						if(data_int.get(2) == 0) {
							data_int.set(2, -1 * match.getEventFile().getEvents().get(i).getEventBowlerNo());
						} else if(data_int.get(2) < 0) {
							data_int.set(2, match.getEventFile().getEvents().get(i).getEventBowlerNo());
						}
					}
				}
				// Balls since last boundary
				if (match.getEventFile().getEvents().get(i).getEventType().equalsIgnoreCase(CricketUtil.SIX) 
		    		|| match.getEventFile().getEvents().get(i).getEventType().equalsIgnoreCase(CricketUtil.FOUR)
		    		&& match.getEventFile().getEvents().get(i).getEventWasABoundary() != null
		    		&& match.getEventFile().getEvents().get(i).getEventWasABoundary().equalsIgnoreCase(CricketUtil.YES)) {
					data_bool.set(0, true);
				} else {
					if(data_bool.get(0) == false) {
						switch (match.getEventFile().getEvents().get(i).getEventType()) {
						case CricketUtil.ONE: case CricketUtil.TWO: case CricketUtil.THREE: case CricketUtil.DOT: case CricketUtil.FIVE: 
						case CricketUtil.BYE: case CricketUtil.LEG_BYE: case CricketUtil.PENALTY: case CricketUtil.LOG_WICKET:
							data_int.set(3, data_int.get(3) + 1);
							break;
						case CricketUtil.LOG_ANY_BALL:
							if (match.getEventFile().getEvents().get(i).getEventType().equalsIgnoreCase(CricketUtil.SIX) 
						    		|| match.getEventFile().getEvents().get(i).getEventType().equalsIgnoreCase(CricketUtil.FOUR)
						    		&& match.getEventFile().getEvents().get(i).getEventWasABoundary() != null
						    		&& match.getEventFile().getEvents().get(i).getEventWasABoundary().equalsIgnoreCase(CricketUtil.YES)) {
								data_bool.set(0, true);
							} else {
								data_int.set(3, data_int.get(3) + 1);
							}
							break;
						}
					}
				}
				// Both innings DOT ball count
				
				if(match.getEventFile().getEvents().get(i).getEventInningNumber() == 1) {
					switch (match.getEventFile().getEvents().get(i).getEventType()) {
					case CricketUtil.DOT: 
						data_int.set(4, data_int.get(4) + 1);
					break;	
					case CricketUtil.LOG_WICKET:
						 if (match.getEventFile().getEvents().get(i).getEventExtra() == null 
				        	&& match.getEventFile().getEvents().get(i).getEventSubExtra() == null 
				        	&& match.getEventFile().getEvents().get(i).getEventSubExtraRuns()== 0) {
							 data_int.set(4, data_int.get(4) + 1);
				        }
						break;
					}
				}else if(match.getEventFile().getEvents().get(i).getEventInningNumber() == 2) {
					switch (match.getEventFile().getEvents().get(i).getEventType()) {
					case CricketUtil.DOT: 
						data_int.set(5, data_int.get(5) + 1);
					break;	
					case CricketUtil.LOG_WICKET:
						 if (match.getEventFile().getEvents().get(i).getEventExtra() == null 
				        	&& match.getEventFile().getEvents().get(i).getEventSubExtra() == null 
							&& match.getEventFile().getEvents().get(i).getEventSubExtraRuns()== 0) {
							 data_int.set(5, data_int.get(5) + 1);
				        }
						break;
					}
				}
				// LAST 30 BALLS 
//				if (ballcount<=30) {
//					switch (match.getEventFile().getEvents().get(i).getEventType())
//					    {
//					    case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE:  case CricketUtil.FIVE : case CricketUtil.DOT:
//					    case CricketUtil.FOUR: case CricketUtil.SIX: case CricketUtil.WIDE: case CricketUtil.NO_BALL: case CricketUtil.BYE: 
//					    	case CricketUtil.LEG_BYE: case CricketUtil.PENALTY: case CricketUtil.LOG_ANY_BALL:
//					    		ballcount++;
//					    		run_c+=match.getEventFile().getEvents().get(i).getEventRuns()+match.getEventFile().getEvents().get(i).getEventExtraRuns()
//							    		  + match.getEventFile().getEvents().get(i).getEventSubExtraRuns();
//					     
//					      break;
//					    case CricketUtil.LOG_WICKET: 
//					      if(match.getEventFile().getEvents().get(i).getEventHowOut().equalsIgnoreCase(CricketUtil.RETIRED_HURT)) {
//					    	  break;
//					      }else {
//					    	  ballcount++;
//					    	  run_c+=match.getEventFile().getEvents().get(i).getEventRuns()+match.getEventFile().getEvents().get(i).getEventExtraRuns()
//						    		  + match.getEventFile().getEvents().get(i).getEventSubExtraRuns();
//					    	  wkt_c=+1;
//					      }
//					      break;
//					    }
//					if(ballcount==30) {
//						data_str.set(6, run_c+"-"+wkt_c);
//						}
//					  }
			}
		}
	
		for(Inning inn : match.getMatch().getInning()) {
			if (inn.getIsCurrentInning().toUpperCase().equalsIgnoreCase(CricketUtil.YES)) {
				
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomeTeamName " + 
					inn.getBatting_team().getTeamName2() + ";");
				
/********************************************************** EVENTS  *********************************************/
//				data_str = new ArrayList<String>();
//				for(int i = 0; i <= 12; i++) {
//					if(i==0||i==1) {
//						data_str.add("0");
//					}else {
//						data_str.add(""); 
//					}
//					
//				}
//				data_int = new ArrayList<Integer>();
//				for(int i = 0; i <= 2; i++) {
//					data_int.add(0); 
//				} 
//				Event_of_fruit(data_str, data_int,inn,match);
//				for(int i=0;i<data_str.size()-1;i++) {
//					System.out.println("index  "+i+"  "+data_str.get(i));
//					
//				}
				for(int i=0;i<data_int.size()-1;i++) {
					System.out.println("index  "+i+"  "+data_int.get(i));
					
				}
				
				
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
	
				}else if(inn.getInningNumber() == 2) {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tText " 
							+ CricketFunctions.generateMatchSummaryStatus(inn.getInningNumber(), match, CricketUtil.SHORT).toUpperCase() + ";");
						
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
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET AwayStatValue4 " +data_str.get(1) + ";");
						
					
				}

				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomeStatValue4 " + data_str.get(0) + ";");
				Integer lastFewOverData=data_int.get(1);
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSmallFreeText1 " + "BALL" + 
						CricketFunctions.Plural(lastFewOverData).toUpperCase() 
						+ " SINCE LAST BOUNDARY : " + 
						data_int.get(1) + ";");
/****************************************** Projected And PhaseBy *************************************************************/
	
				if(inn.getInningNumber() == 1 && inn.getIsCurrentInning().equalsIgnoreCase("YES")) {
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
/****************************************************Current Two Batsmen*******************************/
				if(inn.getPartnerships() != null && inn.getPartnerships().size() > 0) {
					
					this_bc = inn.getBattingCard().stream().filter(batcard -> batcard.getPlayerId() 
							== inn.getPartnerships().get(inn.getPartnerships().size() - 1).getFirstBatterNo()).findAny().orElse(null);
					
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBatterName1 " + this_bc.getPlayer().getTicker_name() + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBatterRun1 " + this_bc.getRuns() + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBatterBall1 " + this_bc.getBalls() + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBoundaries1 " + this_bc.getFours() + "/" + 
						this_bc.getSixes() + ";");
					if(this_bc.getStatus().toUpperCase().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
						if(this_bc.getOnStrike().equalsIgnoreCase(CricketUtil.YES)) {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectStriker1" + " " + "1" + ";");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectStriker2" + " " + "0" + ";");
						
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
							this_bc.getSixes() + ";");
					
					if(this_bc1.getStatus().toUpperCase().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
						if(this_bc1.getOnStrike().equalsIgnoreCase(CricketUtil.YES)) {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectStriker1" + " " + "0" + ";");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectStriker2" + " " + "1" + ";");
							
						}	
					}
					if(this_bc.getStrikeRate() != null) {
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
									+ this_bc.getPlayer().getTicker_name() + "  " +this_bc.getHowOutText()+ "    ;");
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
				}
/****************************************Current and last bowlers*******************************************************************/
				if(inn.getBowlingCard() != null) {
					for(BowlingCard boc : inn.getBowlingCard()) {
						if(boc.getStatus().toUpperCase().equalsIgnoreCase("CURRENTBOWLER")) {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectBowler2" + " " + "0" + ";");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectBowler1" + " " + "1" + ";");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBowlerName1 " + boc.getPlayer().getTicker_name() + ";");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFigure1 " + boc.getWickets() + "-" + boc.getRuns() + ";");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tOvers1 " + CricketFunctions.OverBalls(boc.getOvers(), boc.getBalls()) + ";");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tDots1 " + boc.getDots() + ";");
							
							if(boc.getEconomyRate() == null) {
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tEconomy1 " + "-" + ";");
							}else {
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tEconomy1 " + boc.getEconomyRate() + ";");
							}
						}
						else if(boc.getStatus().toUpperCase().equalsIgnoreCase("LASTBOWLER")) {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectBowler1" + " " + "0" + ";");
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
							
						}
						
							if(CricketFunctions.previousBowler(match, match.getEventFile().getEvents()) != "") {
								previous_bowler = Integer.valueOf(CricketFunctions.previousBowler(match, match.getEventFile().getEvents()).split(",")[5]);
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
			if(inn.getBowlingCard()!=null) {
				for(BowlingCard boc : inn.getBowlingCard()) {
					
					if(boc.getStatus().toUpperCase().equalsIgnoreCase("CURRENTBOWLER")) {
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectBowler2" + " " + "0" + ";");
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectBowler1" + " " + "1" + ";");
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBowlerName1 " + boc.getPlayer().getTicker_name() + ";");
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFigure1 " + boc.getWickets() + "-" + boc.getRuns() + ";");
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tOvers1 " + CricketFunctions.OverBalls(boc.getOvers(), boc.getBalls()) + ";");
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tDots1 " + boc.getDots() + ";");
						
						if(boc.getEconomyRate() == null) {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tEconomy1 " + "-" + ";");
						}else {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tEconomy1 " + boc.getEconomyRate() + ";");
						}
					}
					else if(boc.getStatus().toUpperCase().equalsIgnoreCase("LASTBOWLER")) {
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectBowler1" + " " + "0" + ";");
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
						
					}
					if(data_int.get(0)!= 0) {
						if(boc.getPlayerId()==data_int.get(0)) {
							previous_bowler = Integer.valueOf(CricketFunctions.previousBowler(match, match.getEventFile().getEvents()).split(",")[5]);
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
					}			
	/******************************************  THIS OVER  *****************************************************/

					if(boc.getStatus().toUpperCase().equalsIgnoreCase("CURRENTBOWLER") || boc.getStatus().toUpperCase().equalsIgnoreCase("LASTBOWLER")) {
						String str=replaceTermsInString(data_str.get(4));
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tThisOverData " + reverseStringWithPreservation(str) + ";");
					
						if (((inn.getTotalOvers() * 6) + inn.getTotalBalls()) >= 30)
						{	String[] last_30=data_str.get(5).split("-");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSmallFreeText2 " + "LAST 30 BALLS : " + 
									last_30[0] + " RUN"+CricketFunctions.Plural(Integer.valueOf(last_30[0])).toUpperCase()+" , " +last_30[1] +" WICKET"+CricketFunctions.Plural(Integer.valueOf(last_30[1])).toUpperCase()+ ";");
						}
						else  
						{
							if(inn.getTotalOvers() > 0 || inn.getTotalBalls() > 0) {
								/********************************PreOtherRunWicket*/
								String Run_wicket[]=data_str.get(3).split("-");//done
								System.out.println(Run_wicket[1]+"-"+Run_wicket[0]);
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
					System.out.println(Integer.valueOf(CricketFunctions.PreOtherRunWicket(data_int.get(0),inn.getInningNumber(),",",match,match.getEventFile().getEvents()).split(",")[1])+" "+					Integer.valueOf(CricketFunctions.PreOtherRunWicket(previous_bowler,inn.getInningNumber(),",",match,match.getEventFile().getEvents()).split(",")[0]));

//					Integer.valueOf(CricketFunctions.PreOtherRunWicket(previous_bowler,inn.getInningNumber(),",",match,match.getEventFile().getEvents()).split(",")[1]);
//					Integer.valueOf(CricketFunctions.PreOtherRunWicket(previous_bowler,inn.getInningNumber(),",",match,match.getEventFile().getEvents()).split(",")[0]);
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
		case "DOAD_FRUIT":
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

	private void Event_of_fruit(List<String> data_str, List<Integer> data_int, Inning inn,MatchAllData match) {
		
		boolean last_boundary=false, bowler=false,last_crnt_ovr = true,last_ovr=true;
		
		int total_runs = 0,total_wickets=0;
		int wicket_count = 0;int run_count=0,ballcount=0,run_c=0,wkt_c=0;
		int FirstPPtotal_run[] = {0, 0}, FirstPPtotal_wickets[] = {0, 0},SecondPPtotal_run[] = {0, 0}, SecondPPtotal_wickets [] = {0, 0},ThirdPPtotal_run [] = {0, 0}, ThirdPPtotal_wickets[] = {0, 0},ball_count = 0;
        int SecondPPStartOver=0,SecondPPEndOver=0,ThirdPPStartOver=0,ThirdPPEndOver=0;
        List<Integer>inning_numbers=Arrays.asList(1, 2);
		BowlingCard this_bowC; 
		data_int.set(1, data_int.get(1) + 0);
		
		if (match.getEventFile().getEvents() != null && match.getEventFile().getEvents().size() > 0) {
			
		    for (int i = match.getEventFile().getEvents().size() - 1; i >= 0; i--) {
		    
/*********************************************** INNING COMPARISON **********************************************************************/
		    	
		    		if(match.getEventFile().getEvents().get(i).getEventInningNumber() == 1) {
						switch (match.getEventFile().getEvents().get(i).getEventType()) {
						case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE:  case CricketUtil.FIVE : case CricketUtil.DOT:
				        case CricketUtil.FOUR: case CricketUtil.SIX: case CricketUtil.WIDE: case CricketUtil.NO_BALL: case CricketUtil.BYE: case CricketUtil.LEG_BYE: case CricketUtil.PENALTY:
				        	total_runs += match.getEventFile().getEvents().get(i).getEventRuns();
				        	break;
				        	
				        case CricketUtil.LOG_WICKET:
				        	if(match.getEventFile().getEvents().get(i).getEventRuns() > 0) {
				        		total_runs += match.getEventFile().getEvents().get(i).getEventRuns();
				        	}
				        	if(match.getEventFile().getEvents().get(i).getEventHowOut().equalsIgnoreCase(CricketUtil.RETIRED_HURT)) {
				        		total_wickets += 0;
				        	}else {
				        		total_wickets += 1;
				        	}
				        	break;
				        
				        case CricketUtil.LOG_ANY_BALL:
				        	total_runs += match.getEventFile().getEvents().get(i).getEventRuns();
					          if (match.getEventFile().getEvents().get(i).getEventExtra() != null && !match.getEventFile().getEvents().get(i).getEventExtra().isEmpty()) {
					        	  total_runs += match.getEventFile().getEvents().get(i).getEventExtraRuns();
					          }
					          if (match.getEventFile().getEvents().get(i).getEventSubExtra() != null && !match.getEventFile().getEvents().get(i).getEventSubExtra().isEmpty()) {
					        	  total_runs += match.getEventFile().getEvents().get(i).getEventSubExtraRuns();
					          }
					          if (match.getEventFile().getEvents().get(i).getEventHowOut() != null && !match.getEventFile().getEvents().get(i).getEventHowOut().isEmpty()) {
					        	  total_wickets += 1;
					          }
					          break;
						}
						if (match.getEventFile().getEvents().get(i).getEventOverNo() == match.getMatch().getInning().get(1).getTotalOvers()
				                    && match.getEventFile().getEvents().get(i).getEventBallNo() == match.getMatch().getInning().get(1).getTotalBalls()) {
							    data_str.set(2, total_runs + "-" + total_wickets);
						}

					}

/****************** Last bowler Id *************************************************************************/

		    if(!bowler) {
				if (match.getEventFile().getEvents().get(i).getEventType().equalsIgnoreCase(CricketUtil.END_OVER)&&
						match.getEventFile().getEvents().get(match.getEventFile().getEvents().size()-1).getEventType().equalsIgnoreCase(CricketUtil.END_OVER)	) {
						int endIndex = i - 1;
		                while (endIndex >= 0 && !match.getEventFile().getEvents().get(endIndex).getEventType().equalsIgnoreCase(CricketUtil.END_OVER)) {
		                    endIndex--;
		                }
		                if (endIndex >= 0) {
		                	bowler = true;
		                    data_int.set(0, match.getEventFile().getEvents().get(endIndex).getEventBowlerNo());
		                }
						
					}else  {
						int endIndex = 0;
		                while (endIndex < match.getEventFile().getEvents().size() 
		                	&& !match.getEventFile().getEvents().get(endIndex).getEventType().equalsIgnoreCase(CricketUtil.END_OVER)) {
		                    endIndex++;
		                }
		                if (endIndex < match.getEventFile().getEvents().size()) {
		                	bowler = true;
		                	 data_int.set(0, match.getEventFile().getEvents().get(endIndex).getEventBowlerNo());
		                }										
					}
			}
					
		
					
		/***********************************************Balls since last boundary********************************************/
					
				if (match.getEventFile().getEvents().get(i).getEventType().equalsIgnoreCase(CricketUtil.SIX) 
	    			  || match.getEventFile().getEvents().get(i).getEventType().equalsIgnoreCase(CricketUtil.FOUR)) {
					data_int.set(1, data_int.get(1) + 0);
					last_boundary=true;
	  	        }if(!last_boundary) {
	  	        	switch (match.getEventFile().getEvents().get(i).getEventType()) {
		  	        case CricketUtil.ONE: case CricketUtil.TWO: case CricketUtil.THREE: case CricketUtil.DOT: case CricketUtil.FIVE: case CricketUtil.BYE: 
		  	        case CricketUtil.LEG_BYE: case CricketUtil.PENALTY: case CricketUtil.LOG_WICKET:
		  	        	data_int.set(1, data_int.get(1) + 1);
		  	          break;
		  	        case CricketUtil.LOG_ANY_BALL: 
		  	          if (((match.getEventFile().getEvents().get(i).getEventRuns() == Integer.valueOf(CricketUtil.FOUR)) || (match.getEventFile().getEvents().get(i).getEventRuns() == Integer.valueOf(CricketUtil.SIX))) 
		  	        		  && (match.getEventFile().getEvents().get(i).getEventWasABoundary() != null) &&  (match.getEventFile().getEvents().get(i).getEventWasABoundary().equalsIgnoreCase(CricketUtil.YES))) {
		  	        	data_int.set(1, data_int.get(1) + 0);
		  	          }else {
		  	        	data_int.set(1, data_int.get(1) + 1);
		  	          }
		  	          break;
		  	        }
	  	        }
					
		 /******************************************  event  Comparison *********************************************************/
				if(inn.getInningNumber()==1) {
					switch (match.getEventFile().getEvents().get(i).getEventType()) {
			        case CricketUtil.DOT: case CricketUtil.LOG_WICKET:
						data_str.set(0, String.valueOf(Integer.valueOf(data_str.get(0)) + 1));
			        	break;
			        case CricketUtil.LOG_ANY_BALL:
			        	if(match.getEventFile().getEvents().get(i).getEventHowOut().equalsIgnoreCase(CricketUtil.RUN_OUT)||
			        			match.getEventFile().getEvents().get(i).getEventHowOut().equalsIgnoreCase(CricketUtil.STUMPED)) {
			        		data_str.set(0, String.valueOf(Integer.valueOf(data_str.get(0)) + 1));
			        	}
			        	break;
				}
				}else if(inn.getInningNumber()==2) {
					switch (match.getEventFile().getEvents().get(i).getEventType()) {
			        case CricketUtil.DOT: case CricketUtil.LOG_WICKET:
						data_str.set(1, String.valueOf(Integer.valueOf(data_str.get(0)) + 1));
			        	break;
			        case CricketUtil.LOG_ANY_BALL:
			        	if(match.getEventFile().getEvents().get(i).getEventHowOut().equalsIgnoreCase(CricketUtil.RUN_OUT)||
			        			match.getEventFile().getEvents().get(i).getEventHowOut().equalsIgnoreCase(CricketUtil.STUMPED)) {
			        		data_str.set(1, String.valueOf(Integer.valueOf(data_str.get(0)) + 1));
			        	}
			        	break;
					}
				}
					
		/************************************** PreOtherRunWicket ***********************************************************/
				if(data_int.get(0) == match.getEventFile().getEvents().get(i).getEventBowlerNo()) {
					if(inn.getTotalOvers() == match.getEventFile().getEvents().get(i).getEventOverNo() && match.getEventFile().getEvents().get(i).getEventBallNo() == 0) {
						switch(match.getEventFile().getEvents().get(i).getEventType()) {
						case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE:  case CricketUtil.FIVE : case CricketUtil.DOT:
				        case CricketUtil.FOUR: case CricketUtil.SIX: 
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
					          }
					          break;
				        case CricketUtil.WICKET:
				        	wicket_count += 1;
				        	break;
						}
					}
					if((inn.getTotalOvers() - 1) == match.getEventFile().getEvents().get(i).getEventOverNo()) {
						switch(match.getEventFile().getEvents().get(i).getEventType()) {
						case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE:  case CricketUtil.FIVE : case CricketUtil.DOT:
				        case CricketUtil.FOUR: case CricketUtil.SIX: 
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
					          }
					          break;
				        case CricketUtil.LOG_WICKET:
				        	wicket_count += 1;
				        	break;      
						}
					}
					data_str.set(3, run_count+"-"+wicket_count);
				}
		/*********************************************************** getEventsText ***********************************************************************/							

			if (inn.getBowlingCard() != null) {
				
				this_bowC = inn.getBowlingCard().stream().filter(bowlingCard ->bowlingCard.getStatus().equalsIgnoreCase(CricketUtil.CURRENT + CricketUtil.BOWLER) ||
			                    bowlingCard.getStatus().equalsIgnoreCase(CricketUtil.LAST + CricketUtil.BOWLER)).findAny().orElse(null);
				if(last_crnt_ovr){
					if((match.getEventFile().getEvents().get(i).getEventBowlerNo()!=this_bowC.getPlayerId()||
							match.getEventFile().getEvents().get(i).getEventType().equalsIgnoreCase(CricketUtil.CHANGE_BOWLER))
							&& match.getEventFile().getEvents().get(i).getEventBallNo() <= 0 && !match.getEventFile().getEvents().get(i).getEventType().equalsIgnoreCase(CricketUtil.LOG_ANY_BALL)) {
						last_crnt_ovr = false;
					}else {
						 switch (match.getEventFile().getEvents().get(i).getEventType())
						    {
						    case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE:  case CricketUtil.FIVE : case CricketUtil.DOT:
						    case CricketUtil.FOUR: case CricketUtil.SIX: 
						      data_str.set(4, data_str.get(4)+String.valueOf(match.getEventFile().getEvents().get(i).getEventRuns()+" | "));
						      break;
						    case CricketUtil.WIDE: case CricketUtil.NO_BALL: case CricketUtil.BYE: 
						    	case CricketUtil.LEG_BYE: case CricketUtil.PENALTY:
						    		 data_str.set(4, data_str.get(4)+String.valueOf(match.getEventFile().getEvents().get(i).getEventRuns()+
								    		  + match.getEventFile().getEvents().get(i).getEventSubExtraRuns())+"+" + match.getEventFile().getEvents().get(i).getEventType()+" | ");
						    		 break;
						    case CricketUtil.LOG_ANY_BALL:
						      data_str.set(4, data_str.get(4)+String.valueOf(match.getEventFile().getEvents().get(i).getEventRuns()
						    		  + match.getEventFile().getEvents().get(i).getEventSubExtraRuns())+"+" + match.getEventFile().getEvents().get(i).getEventExtra()+" | ");
						      	break;
						    case CricketUtil.LOG_WICKET: 
					    		 if (match.getEventFile().getEvents().get(i).getEventRuns() > 0) {
							    	  data_str.set(4, data_str.get(4)+String.valueOf(match.getEventFile().getEvents().get(i).getEventRuns()+match.getEventFile().getEvents().get(i).getEventExtraRuns()
								    		  + match.getEventFile().getEvents().get(i).getEventSubExtraRuns()) + match.getEventFile().getEvents().get(i).getEventType()+" | ");
							      } else {
							    	  data_str.set(4, data_str.get(4)+ match.getEventFile().getEvents().get(i).getEventType()+" | ");
							      }
					    		 break;
						    }
					}
				}				    
			}
/*********************************************LAST 30 BALLS ***********************************************/

			if (ballcount<=30) {
				switch (match.getEventFile().getEvents().get(i).getEventType())
				    {
				    case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE:  case CricketUtil.FIVE : case CricketUtil.DOT:
				    case CricketUtil.FOUR: case CricketUtil.SIX: case CricketUtil.WIDE: case CricketUtil.NO_BALL: case CricketUtil.BYE: 
				    	case CricketUtil.LEG_BYE: case CricketUtil.PENALTY: case CricketUtil.LOG_ANY_BALL:
				    		ballcount++;
				    		run_c+=match.getEventFile().getEvents().get(i).getEventRuns()+match.getEventFile().getEvents().get(i).getEventExtraRuns()
						    		  + match.getEventFile().getEvents().get(i).getEventSubExtraRuns();
				     
				      break;
				    case CricketUtil.LOG_WICKET: 
				      if(match.getEventFile().getEvents().get(i).getEventHowOut().equalsIgnoreCase(CricketUtil.RETIRED_HURT)) {
				    	  break;
				      }else {
				    	  ballcount++;
				    	  run_c+=match.getEventFile().getEvents().get(i).getEventRuns()+match.getEventFile().getEvents().get(i).getEventExtraRuns()
					    		  + match.getEventFile().getEvents().get(i).getEventSubExtraRuns();
				    	  wkt_c=+1;
				      }
				      break;
				    }
				if(ballcount==30) {
					data_str.set(5, run_c+"-"+wkt_c);
					}
				  }
/*************************************************** powerplays*****************************************************/			
			 
			for (Integer inn_num : inning_numbers) {
				 if(match.getEventFile().getEvents().get(i).getEventInningNumber() == inn_num)
                 {
             		switch(match.getEventFile().getEvents().get(i).getEventType()) {
 					case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE:  case CricketUtil.FIVE : case CricketUtil.DOT:
 					case CricketUtil.FOUR: case CricketUtil.SIX: case CricketUtil.LOG_WICKET: //case CricketUtil.LOG_ANY_BALL:
 					case CricketUtil.BYE: case CricketUtil.LEG_BYE:
 						ball_count = ball_count + 1;
 						break;
 					}
             		
//             		if(ball_count == 37) {
//     					if(match.getEventFile().getEvents().get(i+1).getEventType().equalsIgnoreCase(CricketUtil.END_OVER)) {
//     						ball_count = 36;
//     					}
//     				}
             		
             		// ---------------------------------------------------FirstPP----------------
             		
             		if(ball_count >= ((match.getMatch().getInning().get(inn_num-1).getFirstPowerplayStartOver() - 1) * Integer.valueOf(match.getSetup().getBallsPerOver())) && 
             				ball_count < (match.getMatch().getInning().get(inn_num-1).getFirstPowerplayEndOver() * Integer.valueOf(match.getSetup().getBallsPerOver()))) {
             			switch (match.getEventFile().getEvents().get(i).getEventType())
                         {
                         	case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE:  case CricketUtil.FIVE : case CricketUtil.DOT:
                         	case CricketUtil.FOUR: case CricketUtil.SIX:
                         		FirstPPtotal_run[inn_num-1]+= match.getEventFile().getEvents().get(i).getEventRuns();
                                 break;

                         	case CricketUtil.WIDE: case CricketUtil.NO_BALL: case CricketUtil.BYE: case CricketUtil.LEG_BYE: case CricketUtil.PENALTY:
                         		FirstPPtotal_run[inn_num-1]+= match.getEventFile().getEvents().get(i).getEventRuns();
                                 break;

                         	case CricketUtil.LOG_WICKET:
                                 if (match.getEventFile().getEvents().get(i).getEventRuns() > 0)
                                 {
                                 	FirstPPtotal_run[inn_num-1]+= match.getEventFile().getEvents().get(i).getEventRuns();
                                 }
                                 FirstPPtotal_run[inn_num-1]+= 1;
                                 break;

                         	case CricketUtil.LOG_ANY_BALL:
                         		FirstPPtotal_run[inn_num-1]+= match.getEventFile().getEvents().get(i).getEventRuns();
                                 if (match.getEventFile().getEvents().get(i).getEventExtra() != null)
                                 {
                                 	FirstPPtotal_run[inn_num-1]+= match.getEventFile().getEvents().get(i).getEventExtraRuns();
                                 }
                                 if (match.getEventFile().getEvents().get(i).getEventSubExtra() != null)
                                 {
                                 	FirstPPtotal_run[inn_num-1]+= match.getEventFile().getEvents().get(i).getEventSubExtraRuns();
                                 }
                                 if (match.getEventFile().getEvents().get(i).getEventHowOut() != null && !match.getEventFile().getEvents().get(i).getEventHowOut().isEmpty())
                                 {
                                 	FirstPPtotal_wickets[inn_num-1] += 1;
                                 }
                                 break;
                         }
             		} else if(ball_count >= ((match.getMatch().getInning().get(inn_num-1).getFirstPowerplayStartOver() - 1) * Integer.valueOf(match.getSetup().getBallsPerOver())) && 
             				ball_count == (match.getMatch().getInning().get(inn_num-1).getFirstPowerplayEndOver() * Integer.valueOf(match.getSetup().getBallsPerOver()))) {
             			if(!match.getEventFile().getEvents().get(i-2).getEventType().equalsIgnoreCase(CricketUtil.END_OVER) || !match.getEventFile().getEvents().get(i-2).getEventType().equalsIgnoreCase(CricketUtil.CHANGE_BOWLER)) {
             				switch (match.getEventFile().getEvents().get(i).getEventType())
                             {
                             	case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE:  case CricketUtil.FIVE : case CricketUtil.DOT:
                             	case CricketUtil.FOUR: case CricketUtil.SIX:
                             		FirstPPtotal_run[inn_num-1]+= match.getEventFile().getEvents().get(i).getEventRuns();
                                     break;

                             	case CricketUtil.WIDE: case CricketUtil.NO_BALL: case CricketUtil.BYE: case CricketUtil.LEG_BYE: case CricketUtil.PENALTY:
                             		FirstPPtotal_run[inn_num-1]+= match.getEventFile().getEvents().get(i).getEventRuns();
                                     break;

                             	case CricketUtil.LOG_WICKET:
                                     if (match.getEventFile().getEvents().get(i).getEventRuns() > 0)
                                     {
                                     	FirstPPtotal_run[inn_num-1]+= match.getEventFile().getEvents().get(i).getEventRuns();
                                     }
                                     FirstPPtotal_wickets[inn_num-1] += 1;
                                     break;

                             	case CricketUtil.LOG_ANY_BALL:
                             		FirstPPtotal_run[inn_num-1]+= match.getEventFile().getEvents().get(i).getEventRuns();
                                     if (match.getEventFile().getEvents().get(i).getEventExtra() != null)
                                     {
                                     	FirstPPtotal_run[inn_num-1]+= match.getEventFile().getEvents().get(i).getEventExtraRuns();
                                     }
                                     if (match.getEventFile().getEvents().get(i).getEventSubExtra() != null)
                                     {
                                     	FirstPPtotal_run[inn_num-1]+= match.getEventFile().getEvents().get(i).getEventSubExtraRuns();
                                     }
                                     if (match.getEventFile().getEvents().get(i).getEventHowOut() != null && !match.getEventFile().getEvents().get(i).getEventHowOut().isEmpty())
                                     {
                                     	FirstPPtotal_wickets[inn_num-1] += 1;
                                     }
                                     break;
                             }
             			}
             		}
             		
             		//----------------------------------------------------------------SecondPP-----------------------------------------------
             		
             		if(ball_count > ((SecondPPStartOver - 1) * Integer.valueOf(match.getSetup().getBallsPerOver())) && ball_count < (SecondPPEndOver * Integer.valueOf(match.getSetup().getBallsPerOver()))) {
         				if(ball_count == 37) {
         					if(match.getEventFile().getEvents().get(i-1).getEventType().equalsIgnoreCase(CricketUtil.WIDE) || match.getEventFile().getEvents().get(i-1).getEventType().equalsIgnoreCase(CricketUtil.NO_BALL)) {
         						SecondPPtotal_run [inn_num-1]+= match.getEventFile().getEvents().get(i-1).getEventRuns();
         					}
         				}
         				
         				//System.out.println("inning = " + match.getEventFile().getEvents().get(i).getEventInningNumber() + "   event = " + match.getEventFile().getEvents().get(i).getEventType());
             			switch (match.getEventFile().getEvents().get(i).getEventType())
                         {
                         case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE:  case CricketUtil.FIVE : case CricketUtil.DOT:
     					case CricketUtil.FOUR: case CricketUtil.SIX:
     						SecondPPtotal_run [inn_num-1]+= match.getEventFile().getEvents().get(i).getEventRuns();
                                 break;

                             case CricketUtil.WIDE:
                             	if(ball_count == 90 && match.getEventFile().getEvents().get(i-1).getEventType().equalsIgnoreCase(CricketUtil.CHANGE_BOWLER)) {
                                     break;
                             	}else {
                             		SecondPPtotal_run [inn_num-1]+= match.getEventFile().getEvents().get(i).getEventRuns();
                                     break;
                             	}
                             case CricketUtil.NO_BALL: case CricketUtil.BYE: case CricketUtil.LEG_BYE: case CricketUtil.PENALTY:
                             	SecondPPtotal_run [inn_num-1]+= match.getEventFile().getEvents().get(i).getEventRuns();
                                 break;
                             case CricketUtil.LOG_WICKET:
                                 if (match.getEventFile().getEvents().get(i).getEventRuns() > 0)
                                 {
                                 	SecondPPtotal_run [inn_num-1]+= match.getEventFile().getEvents().get(i).getEventRuns();
                                 }
                                 SecondPPtotal_wickets [inn_num-1] += 1;
                                 break;

                             case CricketUtil.LOG_ANY_BALL:
                             	SecondPPtotal_run [inn_num-1]+= match.getEventFile().getEvents().get(i).getEventRuns();
                                 if (match.getEventFile().getEvents().get(i).getEventExtra() != null)
                                 {
                                 	SecondPPtotal_run [inn_num-1]+= match.getEventFile().getEvents().get(i).getEventExtraRuns();
                                 }
                                 if (match.getEventFile().getEvents().get(i).getEventSubExtra() != null)
                                 {
                                 	SecondPPtotal_run [inn_num-1]+= match.getEventFile().getEvents().get(i).getEventSubExtraRuns();
                                 }
                                 if (match.getEventFile().getEvents().get(i).getEventHowOut() != null && !match.getEventFile().getEvents().get(i).getEventHowOut().isEmpty())
                                 {
                                 	SecondPPtotal_wickets [inn_num-1] += 1;
                                 }
                                 break;
                         }
             		} else if(ball_count > ((SecondPPStartOver - 1) * Integer.valueOf(match.getSetup().getBallsPerOver())) && ball_count == (SecondPPEndOver * Integer.valueOf(match.getSetup().getBallsPerOver()))) {
             			if(!match.getEventFile().getEvents().get(i-2).getEventType().equalsIgnoreCase(CricketUtil.END_OVER) || !match.getEventFile().getEvents().get(i-2).getEventType().equalsIgnoreCase(CricketUtil.CHANGE_BOWLER)) {
             				switch (match.getEventFile().getEvents().get(i).getEventType())
                             {
                             	case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE:  case CricketUtil.FIVE : case CricketUtil.DOT:
                             	case CricketUtil.FOUR: case CricketUtil.SIX:
                             		SecondPPtotal_run [inn_num-1]+= match.getEventFile().getEvents().get(i).getEventRuns();
                                     break;

         						case CricketUtil.WIDE: 
         							if(ball_count == 90 && match.getEventFile().getEvents().get(i-1).getEventType().equalsIgnoreCase(CricketUtil.CHANGE_BOWLER)) {
                                         break;
                                 	}else {
                                 		SecondPPtotal_run [inn_num-1]+= match.getEventFile().getEvents().get(i).getEventRuns();
                                         break;
                                 	}
         						case CricketUtil.NO_BALL: case CricketUtil.BYE: case CricketUtil.LEG_BYE: case CricketUtil.PENALTY:
         							SecondPPtotal_run [inn_num-1]+= match.getEventFile().getEvents().get(i).getEventRuns();
                                     break;
         						case CricketUtil.LOG_WICKET:
                                     if (match.getEventFile().getEvents().get(i).getEventRuns() > 0)
                                     {
                                     	SecondPPtotal_run [inn_num-1]+= match.getEventFile().getEvents().get(i).getEventRuns();
                                     }
                                     SecondPPtotal_wickets [inn_num-1] += 1;
                                     break;

         						case CricketUtil.LOG_ANY_BALL:
         							SecondPPtotal_run [inn_num-1]+= match.getEventFile().getEvents().get(i).getEventRuns();
                                     if (match.getEventFile().getEvents().get(i).getEventExtra() != null)
                                     {
                                     	SecondPPtotal_run [inn_num-1]+= match.getEventFile().getEvents().get(i).getEventExtraRuns();
                                     }
                                     if (match.getEventFile().getEvents().get(i).getEventSubExtra() != null)
                                     {
                                     	SecondPPtotal_run [inn_num-1]+= match.getEventFile().getEvents().get(i).getEventSubExtraRuns();
                                     }
                                     if (match.getEventFile().getEvents().get(i).getEventHowOut() != null && !match.getEventFile().getEvents().get(i).getEventHowOut().isEmpty())
                                     {
                                     	SecondPPtotal_run [inn_num-1]+= 1;
                                     }
                                     break;
                             }
             			}
             		}
             		
             		
             		//--------------------------------thirdpp-----------------------------------------------------------
             		
             		
             		if(ball_count > ((ThirdPPStartOver - 1) * Integer.valueOf(match.getSetup().getBallsPerOver())) && ball_count <= (ThirdPPEndOver * Integer.valueOf(match.getSetup().getBallsPerOver()))) {
             			if(ball_count == 91 || ball_count == 241) {
         					if(match.getEventFile().getEvents().get(i-1).getEventType().equalsIgnoreCase(CricketUtil.WIDE) || match.getEventFile().getEvents().get(i-1).getEventType().equalsIgnoreCase(CricketUtil.NO_BALL)) {
         						ThirdPPtotal_run [inn_num-1] += match.getEventFile().getEvents().get(i-1).getEventRuns();
         					}
         				}
             			//System.out.println("inning = " + match.getEventFile().getEvents().get(i).getEventInningNumber() + "   event = " + match.getEventFile().getEvents().get(i).getEventType());
             			switch (match.getEventFile().getEvents().get(i).getEventType())
                         {
                         	case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE:  case CricketUtil.FIVE : case CricketUtil.DOT:
                         	case CricketUtil.FOUR: case CricketUtil.SIX:
                         		ThirdPPtotal_run [inn_num-1] += match.getEventFile().getEvents().get(i).getEventRuns();
                                 break;

                             case CricketUtil.WIDE: case CricketUtil.NO_BALL: case CricketUtil.BYE: case CricketUtil.LEG_BYE: case CricketUtil.PENALTY:
                             	ThirdPPtotal_run [inn_num-1] += match.getEventFile().getEvents().get(i).getEventRuns();
                                 break;

                             case CricketUtil.LOG_WICKET:
                                 if (match.getEventFile().getEvents().get(i).getEventRuns() > 0)
                                 {
                                 	ThirdPPtotal_run [inn_num-1] += match.getEventFile().getEvents().get(i).getEventRuns();
                                 }
                                 ThirdPPtotal_wickets[inn_num-1] += 1;
                                 break;

                             case CricketUtil.LOG_ANY_BALL:
                             	ThirdPPtotal_run [inn_num-1] += match.getEventFile().getEvents().get(i).getEventRuns();
                                 if (match.getEventFile().getEvents().get(i).getEventExtra() != null)
                                 {
                                 	ThirdPPtotal_run [inn_num-1] += match.getEventFile().getEvents().get(i).getEventExtraRuns();
                                 }
                                 if (match.getEventFile().getEvents().get(i).getEventSubExtra() != null)
                                 {
                                 	ThirdPPtotal_run [inn_num-1] += match.getEventFile().getEvents().get(i).getEventSubExtraRuns();
                                 }
                                 if (match.getEventFile().getEvents().get(i).getEventHowOut() != null && !match.getEventFile().getEvents().get(i).getEventHowOut().isEmpty())
                                 {
                                 	ThirdPPtotal_wickets[inn_num-1] += 1;
                                 }
                                 break;
                         }
             		}
                 }
			 }data_str.set(6, FirstPPtotal_run  [0]+"-"+FirstPPtotal_wickets[0]);
			 data_str.set(7,  FirstPPtotal_run  [1]+"-"+FirstPPtotal_wickets[1]);
			 data_str.set(8,  SecondPPtotal_run [0]+"-"+SecondPPtotal_wickets[0]);
			 data_str.set(9,  SecondPPtotal_run [1]+"-"+SecondPPtotal_wickets[1]);
			 data_str.set(10, ThirdPPtotal_run  [0]+"-"+ThirdPPtotal_wickets[0]);
			 data_str.set(11, ThirdPPtotal_run  [1]+"-"+ThirdPPtotal_wickets[1]);		
		  }
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
	
}