<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>

  <meta charset="utf-8" name="viewport" content="width=device-width, initial-scale=1">
  <title>Output Screen</title>
	
  <script type="text/javascript" src="<c:url value="/webjars/jquery/3.6.0/jquery.min.js"/>"></script>
  <script type="text/javascript" src="<c:url value="/webjars/bootstrap/5.1.3/js/bootstrap.min.js"/>"></script>
  <script type="text/javascript" src="<c:url value="/resources/javascript/index.js"/>"></script>
  
  <link rel="stylesheet" href="<c:url value="/webjars/bootstrap/5.1.3/css/bootstrap.min.css"/>"/>  
  <link href="<c:url value="/webjars/font-awesome/6.0.0/css/all.css"/>" rel="stylesheet">
  <script type="text/javascript">
	  
  
  $(document).on("keydown", function(e){
	  
	//if(session_selected_broadcaster != ' '){
		if(e.altKey && e.key === 's'){
		  e.preventDefault()
		  processUserSelectionData('LOGGER_FORM_KEYPRESS','SPEED');
	  }else if(e.altKey && e.key === 'r'){
		  e.preventDefault()
		  processUserSelectionData('LOGGER_FORM_KEYPRESS','RE_READ');
	  }
	  else if(e.ctrlKey && e.key === '`'){
		  e.preventDefault()
		  processUserSelectionData('LOGGER_FORM_KEYPRESS','PLAYER_OF_THE_TOURNAMENT');
	  }else if(e.ctrlKey && e.key === 'i'){
		  e.preventDefault()
		  processUserSelectionData('LOGGER_FORM_KEYPRESS','LOF_BATTING');
	  }else if(e.shiftKey && e.key === 'I'){
		  e.preventDefault()
		  processUserSelectionData('LOGGER_FORM_KEYPRESS','LOF_BOWLING');
	  }else if(e.shiftKey && e.key === 'F8'){
		  e.preventDefault()
		  processUserSelectionData('LOGGER_FORM_KEYPRESS','LT_PLAYING_XI');
	  }else if(e.altKey && e.key === 'p'){
		  e.preventDefault()
		  processUserSelectionData('LOGGER_FORM_KEYPRESS','PHASE');
	  }
	  else if(e.ctrlKey && e.key === 'F1'){
		  e.preventDefault()
		  processUserSelectionData('LOGGER_FORM_KEYPRESS','BAT_PERFORMER');
	  }else if(e.shiftKey && e.key === 'F1'){
		  e.preventDefault()
		  processUserSelectionData('LOGGER_FORM_KEYPRESS','MINI_BATTING_CARD');
	  }else if(e.altKey && e.key === 'F1'){
		  e.preventDefault()
		  processUserSelectionData('LOGGER_FORM_KEYPRESS','BAT_GRIFF');
	  }else if(e.ctrlKey && e.key === 'F2'){
		  e.preventDefault()
		  processUserSelectionData('LOGGER_FORM_KEYPRESS','BALL_PERFORMER');
	  }else if(e.shiftKey && e.key === 'F2'){
		  e.preventDefault()
		  processUserSelectionData('LOGGER_FORM_KEYPRESS','MINI_BOWLING_CARD');
	  }else if(e.altKey && e.key === 'F2'){
		  e.preventDefault()
		  processUserSelectionData('LOGGER_FORM_KEYPRESS','BALL_GRIFF');
	  }
	  else if(e.shiftKey && e.key === 'A'){
		  e.preventDefault()
		  processUserSelectionData('LOGGER_FORM_KEYPRESS','DLS_EQUATION');
	  }else if(e.ctrlKey && e.key === 'F6'){
		  e.preventDefault()
		  processUserSelectionData('LOGGER_FORM_KEYPRESS','HOW_OUT_QUICK');
	  }else if(e.shiftKey && e.key === 'F6'){
		  e.preventDefault()
		  processUserSelectionData('LOGGER_FORM_KEYPRESS','HOW_OUT_W_O_FIELDER');
	  }else if(e.altKey && e.key === 'k'){
		  e.preventDefault()
		  processUserSelectionData('LOGGER_FORM_KEYPRESS','LT_PARTNERSHIP');
	  }else if(e.ctrlKey && e.key === 'k'){
		  e.preventDefault()
		  processUserSelectionData('LOGGER_FORM_KEYPRESS','BUG_CURR_PARTNERSHIP');
	  }else if(e.shiftKey && e.key === 'F4'){
		  e.preventDefault()
		  processUserSelectionData('LOGGER_FORM_KEYPRESS','BUG_MULTI_PARTNERSHIP');
	  }else if(e.ctrlKey && e.key === 'F5'){
		  e.preventDefault()
		  processUserSelectionData('LOGGER_FORM_KEYPRESS','BATSMAN_STYLE');
	  }else if(e.shiftKey && e.key === 'F5'){
		  e.preventDefault()
		  processUserSelectionData('LOGGER_FORM_KEYPRESS','BATSMAN_ALL');
	  }else if(e.ctrlKey && e.key === 'F9'){
		  e.preventDefault()
		  processUserSelectionData('LOGGER_FORM_KEYPRESS','BOWLER_STYLE');
	  }else if(e.shiftKey && e.key === 'F9'){
		  e.preventDefault()
		  processUserSelectionData('LOGGER_FORM_KEYPRESS','BOWLER_ALL');
	  }else if(e.ctrlKey && e.key === 'd'){
		  e.preventDefault()
		  processUserSelectionData('LOGGER_FORM_KEYPRESS','FF_PP_BAT');
	  }else if(e.ctrlKey && e.key === 'e'){
		  e.preventDefault()
		  processUserSelectionData('LOGGER_FORM_KEYPRESS','FF_PP_BALL');
	  }else if(e.ctrlKey && e.key === 'F11'){
		  e.preventDefault()
		  processUserSelectionData('LOGGER_FORM_KEYPRESS','MATCH_SUMMARY');
	  }else if(e.shiftKey && e.key === 'F11'){
		  e.preventDefault()
		  processUserSelectionData('LOGGER_FORM_KEYPRESS','PREVIOUS_MATHC_SUMMARY');
	  }else if(e.ctrlKey && e.key === 'F10'){
		  e.preventDefault()
		  processUserSelectionData('LOGGER_FORM_KEYPRESS','MANHATTAN');
	  }else if(e.shiftKey && e.key === 'F10'){
		  e.preventDefault()
		  processUserSelectionData('LOGGER_FORM_KEYPRESS','WORMS');
	  }else if(e.altKey && e.key === 'F10'){
		  e.preventDefault()
		  processUserSelectionData('LOGGER_FORM_KEYPRESS','LT_MANHATTAN');
	  }else if(e.ctrlKey && e.key === 'F7'){
		  e.preventDefault()
		  processUserSelectionData('LOGGER_FORM_KEYPRESS','DOUBLE_TEAMS');
	  }else if(e.ctrlKey && e.key === 'F8'){
		  e.preventDefault()
		  processUserSelectionData('LOGGER_FORM_KEYPRESS','PLAYING_XI');
	  }else if(e.altKey && e.key === 'F8'){
		  e.preventDefault()
		  processUserSelectionData('LOGGER_FORM_KEYPRESS','PLAYING_XI_2_LLC');
	  }
	  else if(e.shiftKey && e.key === 'O'){
		  e.preventDefault()
		  processUserSelectionData('LOGGER_FORM_KEYPRESS','LT_POINTERS');
	  }else if(e.shiftKey && e.key === 'K'){
		  e.preventDefault()
		  processUserSelectionData('LOGGER_FORM_KEYPRESS','CURR_PARTNERSHIP');
	  }else if(e.shiftKey && e.key === 'T'){
		  e.preventDefault()
		  processUserSelectionData('LOGGER_FORM_KEYPRESS','TEAMS_LOGO');
	  }else if(e.altKey && e.key === 't'){
		  e.preventDefault()
		  processUserSelectionData('LOGGER_FORM_KEYPRESS','TEAMS_SQUAD');
	  }
	  else if(e.shiftKey && e.key === 'O'){
		  e.preventDefault()
		  processUserSelectionData('LOGGER_FORM_KEYPRESS','BUG_DISMISSAL');
	  }else if(e.shiftKey && e.key === 'F3'){
		  e.preventDefault()
		  processUserSelectionData('LOGGER_FORM_KEYPRESS','FALL_OF_WICKETS');
	  }else if(e.ctrlKey && e.key === 'F3'){
		  e.preventDefault()
		  processUserSelectionData('LOGGER_FORM_KEYPRESS','COMPARISON');
	  }else if(e.ctrlKey && e.key === 'l'){
		  e.preventDefault()
		  processUserSelectionData('LOGGER_FORM_KEYPRESS','BUG_POWERPLAY');
	  }else if(e.ctrlKey && e.key === 'a'){
		  e.preventDefault()
		  processUserSelectionData('LOGGER_FORM_KEYPRESS','PROJECTED_SCORE');
	  }else if(e.ctrlKey && e.key === 'p'){
		  e.preventDefault()
		  processUserSelectionData('LOGGER_FORM_KEYPRESS','LT_POINTS_TABLE');
	  }else if(e.ctrlKey && e.key === 'b'){
		  e.preventDefault()
		  processUserSelectionData('LOGGER_FORM_KEYPRESS','BATSMAN_IN_AT');
	  }
	  else if(e.altKey && e.key === 'F12'){
		  e.preventDefault()
		  processUserSelectionData('LOGGER_FORM_KEYPRESS','TEAM_ALL');
	  }else if(e.ctrlKey && e.key === 's'){
		  e.preventDefault()
		  processUserSelectionData('LOGGER_FORM_KEYPRESS','THIS_SERIES');
	  }else if(e.ctrlKey && e.key === 'm'){
		  e.preventDefault()
		  processUserSelectionData('LOGGER_FORM_KEYPRESS','FF_MATCH_PROMO');
	  }else if(e.shiftKey && e.key === 'N'){
		  e.preventDefault()
		  processUserSelectionData('LOGGER_FORM_KEYPRESS','LT_MATCH_PROMO');
	  }else if(e.shiftKey && e.key === 'M'){
		  e.preventDefault()
		  processUserSelectionData('LOGGER_FORM_KEYPRESS','DOUBLE_MATCH_ID');
	  }else if(e.ctrlKey && e.key === 'z'){
		  e.preventDefault()
		  processUserSelectionData('LOGGER_FORM_KEYPRESS','MOST_WICKETS');
	  }else if(e.ctrlKey && e.key === 'x'){
		  e.preventDefault()
		  processUserSelectionData('LOGGER_FORM_KEYPRESS','MOST_FOURS');
	  }else if(e.ctrlKey && e.key === 'c'){
		  e.preventDefault()
		  processUserSelectionData('LOGGER_FORM_KEYPRESS','MOST_SIXES');
	  }else if(e.shiftKey && e.key === 'X'){
		  e.preventDefault()
		  processUserSelectionData('LOGGER_FORM_KEYPRESS','FF_STATS');
	  }
	  else if(e.shiftKey && e.key === 'D'){
		  e.preventDefault()
		  processUserSelectionData('LOGGER_FORM_KEYPRESS','FF_TARGET');
	  }else if(e.ctrlKey && e.key === 'q'){
		  e.preventDefault()
		  processUserSelectionData('LOGGER_FORM_KEYPRESS','THIS_MATCH_FOURS');
	  }else if(e.shiftKey && e.key === 'Q'){
		  e.preventDefault()
		  processUserSelectionData('LOGGER_FORM_KEYPRESS','THIS_MATCH_SIXES');
	  }else if(e.shiftKey && e.key === 'P'){
		  e.preventDefault()
		  processUserSelectionData('LOGGER_FORM_KEYPRESS','SEASON_PROFILE');
	  }else if(e.shiftKey && e.key === 'B'){
		  e.preventDefault()
		  processUserSelectionData('LOGGER_FORM_KEYPRESS','BOWLER_SPEED');
	  }else if(e.altKey && e.key === 'b'){
		  e.preventDefault()
		  processUserSelectionData('LOGGER_FORM_KEYPRESS','LT_LINE_UP');
	  }
	  else if(e.ctrlKey && e.key === 'F12'){
		  e.preventDefault()
		  processUserSelectionData('LOGGER_FORM_KEYPRESS','IDENT');
	  }else if(e.altKey && e.key === '1'){
		  e.preventDefault()
		  processUserSelectionData('LOGGER_FORM_KEYPRESS','INFOBAR_TOP');
	  }else if(e.altKey && e.key === '2'){
		  e.preventDefault()
		  processUserSelectionData('LOGGER_FORM_KEYPRESS','INFOBAR_RIGHT');
	  }else if(e.altKey && e.key === '3'){
		  e.preventDefault()
		  processUserSelectionData('LOGGER_FORM_KEYPRESS','INFOBAR_BOTTOM_RIGHT');
	  }else if(e.altKey && e.key === '4'){
		  e.preventDefault()
		  processUserSelectionData('LOGGER_FORM_KEYPRESS','INFOBAR_DIRECTOR');
	  }else if(e.altKey && e.key === '5'){
		  e.preventDefault()
		  processUserSelectionData('LOGGER_FORM_KEYPRESS','INFOBAR_BOTTOM_LEFT');
	  }else if(e.altKey && e.key === '6'){
		  e.preventDefault()
		  processUserSelectionData('LOGGER_FORM_KEYPRESS','INFOBAR_BOTTOM');
	  }else if(e.altKey && e.key === '7'){
		  e.preventDefault()
		  processUserSelectionData('LOGGER_FORM_KEYPRESS','INFOBAR_MIDDLE');
	  }else if(e.altKey && e.key === '8'){
		  e.preventDefault()
		  processUserSelectionData('LOGGER_FORM_KEYPRESS','INFOBAR_SECTION5');
	  }else if(e.altKey && e.key === '9'){
		  e.preventDefault()
		  processUserSelectionData('LOGGER_FORM_KEYPRESS','ANIMATE_OUT_INFOBAR_TOP');
	  }else if(e.altKey && e.key === '0'){
		  e.preventDefault()
		  processUserSelectionData('LOGGER_FORM_KEYPRESS','ANIMATE_OUT_INFOBAR_RIGHT');
	  }else if(e.ctrlKey && e.key === '1'){
		  e.preventDefault()
		  processUserSelectionData('LOGGER_FORM_KEYPRESS','INFOBAR_POWERPLAY'); 
	  }else if(e.ctrlKey && e.key === '2'){
		  e.preventDefault()
		  processUserSelectionData('LOGGER_FORM_KEYPRESS','INFOBAR_SPONSOR');
	  }else if(e.ctrlKey && e.key === '3'){
		  e.preventDefault()
		  processUserSelectionData('LOGGER_FORM_KEYPRESS','INFOBAR_LEFT');
	  }else if(e.ctrlKey && e.key === '4'){
		  e.preventDefault()
		  processUserSelectionData('LOGGER_FORM_KEYPRESS','IDENT_DATA');
	  }else if(e.ctrlKey && e.key === 'g'){
		  e.preventDefault()
		  processUserSelectionData('LOGGER_FORM_KEYPRESS','FF_THIS_SERIES');
	  }else if(e.key === "F1" || e.key === "F2" || e.key === "F3" || e.key === "F4" || e.key === "F5" || e.key === "F6" ||  e.key === "F7" || e.key === "F8" ||
				e.key === "F9" || e.key === "F10" || e.key === "F11" || e.key === "F12" || e.key === "ArrowDown" || e.key === "ArrowUp" || e.key === " " ||
				e.key === "PageUp" || e.key === "PageDown" || e.key === "1") {
	      // Suppress default behaviour 
	      // e.g. F1 in Chrome on Windows usually opens Windows help
	      e.preventDefault()
	      processUserSelectionData('LOGGER_FORM_KEYPRESS',e.which);
	  }else{
		  processUserSelectionData('LOGGER_FORM_KEYPRESS',e.which);
	  }
  }); 
  
  setInterval(() => {
	  processCricketProcedures('READ-MATCH-AND-POPULATE');		
	}, 1000);
  
 	
  </script>
</head>
<body>
<form:form name="output_form" autocomplete="off" action="POST">
<div class="content py-5" style="background-color: #EAE8FF; color: #2E008B">
  <div class="container">
	<div class="row">
	 <div class="col-md-8 offset-md-2">
       <span class="anchor"></span>
         <div class="card card-outline-secondary">
           <div class="card-header">
             <h3 class="mb-0">Output</h3>
            <!--   <h3 class="mb-0">${licence_expiry_message}</h3>  -->
           </div>
          <div class="card-body">
          
          	<!--  <div class="form-group row row-bottom-margin ml-2" style="margin-bottom:5px;">
			    <button style="background-color:#2E008B;color:#FEFEFE;" class="btn btn-sm" type="button"
			  		name="cancel_match_setup_btn" id="cancel_match_setup_btn" onclick="processUserSelection(this)">
		  		<i class="fas fa-window-close"></i> Back</button>
	         </div>
	         <div class="form-group row row-bottom-margin ml-2" style="margin-bottom:5px;">
	         	<h3 class="mb-0" id="expiry_message">${licence_expiry_message}</h3>
	         </div> -->
	         
	         
			  <div id="select_graphic_options_div" style="display:none;">
			  </div>
			  <div id="lastxball_div" style="display:none;">
			  </div>
			  <div id="captions_div" class="form-group row row-bottom-margin ml-2" style="margin-bottom:5px;">
			  	<!--  <label class="col-sm-4 col-form-label text-left">${licence_expiry_message} </label> -->
			    <label class="col-sm-4 col-form-label text-left">Match: ${session_match.match.matchFileName} </label>
			   
			    <label class="col-sm-4 col-form-label text-left">Broadcaster: ${session_selected_broadcaster.replace("_"," ")} </label>
			    <label class="col-sm-4 col-form-label text-left">2nd Broadcaster: ${session_selected_second_broadcaster.replace("_"," ")} </label>
			    <label id="selected_inning" class="col-sm-4 col-form-label text-left">Current Inning: ${current_inning} </label>
			    <label id="inning1_totalruns_lbl" class="col-sm-4 col-form-label text-left">${curr_team_total}</label>
			    <label id="inning1_battingcard1_lbl" class="col-sm-4 col-form-label text-left">${curr_player}</label>
			    <label id="inning1_battingcard2_lbl" class="col-sm-4 col-form-label text-left">${curr_player2}</label>
			    <label id="inning1_bowlingcard_lbl" class="col-sm-4 col-form-label text-left">${curr_bowler}</label>
			    
			    <c:if test="${(session_selected_broadcaster == 'DOAD_LLC')}">
			    <label id="speed_lbl" class="col-sm-4 col-form-label text-left">Show Speed: YES</label>
			    </c:if>
  				
  				
  				<div class="left">
  				 <c:if test="${(session_selected_second_broadcaster == 'DOAD_LLC')}">
  					<label>SPEED </label>
  					<input type = "text" name = "speedtext" id="speedtext"/>
  					<button style="background-color:#ffeb2b;color:#000000;" class="btn btn-sm" type="button"
			  			name="animate_in_speed" id="animate_in_speed" onclick="processUserSelection(this)"> Animate-In Speed </button>
  				 </c:if>
  				</div>
  				
  				<div class="left">
  				
  				<c:if test="${(session_selected_broadcaster != ' ')}">
  					<button style="background-color:#f44336;color:#FEFEFE;" class="btn btn-sm" type="button"
			  		name="animateout_graphic_btn" id="animateout_graphic_btn" onclick="processUserSelection(this)"> AnimateOut (-) </button>
  				</c:if>
  				
			  	<c:if test="${(session_selected_broadcaster != ' ')}">
  					<button style="background-color:#f44336;color:#FEFEFE;" class="btn btn-sm" type="button"
			  			name="clearall_graphic_btn" id="clearall_graphic_btn" onclick="processUserSelection(this)"> Clear All (SpaceBar) </button>
  				</c:if>


			  	
			  	<c:if test="${(session_selected_broadcaster == 'FRUIT')}">
			  	<button style="background-color:#ffd700;color:#000000;" class="btn btn-sm" type="button"
			  		name="infobar_graphic_btn" id="infobar_graphic_btn" onclick="processUserSelection(this)"> Infobar (F12)</button>
				<button style="background-color:#ffd700;color:#000000;" class="btn btn-sm" type="button"
			  		name="infobar_bottom-left_graphic_btn" id="infobar_bottom-left_graphic_btn" onclick="processUserSelection(this)"> Infobar Bottom-Left (Alt+5)</button>
			  	<button style="background-color:#ffd700;color:#000000;" class="btn btn-sm" type="button"
			  		name="infobar_bottom_graphic_btn" id="infobar_bottom_graphic_btn" onclick="processUserSelection(this)"> Infobar Bottom (Alt+6)</button>
			  	<button style="background-color:#ffd700;color:#000000;" class="btn btn-sm" type="button"
			  		name="infobar_bottom-right_graphic_btn" id="infobar_bottom-right_graphic_btn" onclick="processUserSelection(this)"> Infobar Bottom-Right (Alt+3)</button>
			  	<button style="background-color:#ffd700;color:#000000;" class="btn btn-sm" type="button"
			  		name="director_graphic_btn" id="director_graphic_btn" onclick="processUserSelection(this)"> Infobar Director (Alt+4)</button>
			  	
			  	<button style="background-color:#2E008B;color:#FEFEFE;" class="btn btn-sm" type="button"
			  		name="dls_graphic_btn" id="dls_graphic_btn" onclick="processUserSelection(this)"> DLS (A)</button>
			  	<button style="background-color:#2E008B;color:#FEFEFE;" class="btn btn-sm" type="button"
			  		name="dls_equation_graphic_btn" id="dls_equation_graphic_btn" onclick="processUserSelection(this)"> DLS EQUATION (Shift+A)</button>
			  		
			  	<button style="background-color:#2E008B;color:#FEFEFE;" class="btn btn-sm" type="button"
			  		name="four_graphic_btn" id="four_graphic_btn" onclick="processUserSelection(this)"> This Match Four (Ctrl+Q)</button> 
			  	<button style="background-color:#2E008B;color:#FEFEFE;" class="btn btn-sm" type="button"
			  		name="six_graphic_btn" id="six_graphic_btn" onclick="processUserSelection(this)"> This Match Six (Shift+Q)</button> 
			  	<button style="background-color:#2E008B;color:#FEFEFE;" class="btn btn-sm" type="button"
			  		name="drone_graphic_btn" id="drone_graphic_btn" onclick="processUserSelection(this)"> Drone Bug (`)</button> 
			  		
			  	<button style="background-color:#2E008B;color:#FEFEFE;" class="btn btn-sm" type="button"
			  		name="bug_graphic_btn" id="bug_graphic_btn" onclick="processUserSelection(this)"> Bug BatsmanScore (F)</button> 
		 	    <button style="background-color:#2E008B;color:#FEFEFE;" class="btn btn-sm" type="button"
			  		name="bug_dismissal_graphic_btn" id="bug_dismissal_graphic_btn" onclick="processUserSelection(this)"> Bug-Dismissal (Shift+O)</button> 
			  	<button style="background-color:#2E008B;color:#FEFEFE;" class="btn btn-sm" type="button"
			  		name="bugbowler_graphic_btn" id="bugbowler_graphic_btn" onclick="processUserSelection(this)"> Bug BowlerFigure (G)</button>
			  	<button style="background-color:#2E008B;color:#FEFEFE;" class="btn btn-sm" type="button"
			  		name="bug_db_graphic_btn" id="bug_db_graphic_btn" onclick="processUserSelection(this)"> Bug DataBase (K)</button>
			  	<button style="background-color:#2E008B;color:#FEFEFE;" class="btn btn-sm" type="button"
			  		name="bug_powerplay_graphic_btn" id="bug_powerplay_graphic_btn" onclick="processUserSelection(this)"> Bug Powerplay (Ctrl+L)</button>
			  	<button style="background-color:#2E008B;color:#FEFEFE;" class="btn btn-sm" type="button"
			  		name="bug_toss_graphic_btn" id="bug_toss_graphic_btn" onclick="processUserSelection(this)"> Bug-Toss (T)</button>
			  	<button style="background-color:#2E008B;color:#FEFEFE;" class="btn btn-sm" type="button"
			  		name="bug_partnership_graphic_btn" id="bug_partnership_graphic_btn" onclick="processUserSelection(this)"> Bug-Partnership (Ctrl+K)</button>
			  	<button style="background-color:#2E008B;color:#FEFEFE;" class="btn btn-sm" type="button"
			  		name="bug_highlight_graphic_btn" id="bug_highlight_graphic_btn" onclick="processUserSelection(this)"> Bug Highlight (H)</button>
			  			
			  	<button style="background-color:#6a33f7;color:#FEFEFE;;" class="btn btn-sm" type="button"
			  		name="scorecard_graphic_btn" id="scorecard_graphic_btn" onclick="processUserSelection(this)"> ScoreCard (F1)</button>
			  	<button style="background-color:#6a33f7;color:#FEFEFE;" class="btn btn-sm" type="button"
			  		name="bowlingcard_graphic_btn" id="bowlingcard_graphic_btn" onclick="processUserSelection(this)"> BowlingCard (F2)</button>
			  	<button style="background-color:#6a33f7;color:#FEFEFE;" class="btn btn-sm" type="button"
			  		name="matchsummary_graphic_btn" id="matchsummary_graphic_btn" onclick="processUserSelection(this)"> Match Summary (Ctrl+F11)</button>
			  	<button style="background-color:#2E008B;color:#FEFEFE;" class="btn btn-sm" type="button"
			  		name="pointstable_graphic_btn" id="pointstable_graphic_btn" onclick="processUserSelection(this)"> Points Table (P)</button>
			  	<button style="background-color:#2E008B;color:#FEFEFE;" class="btn btn-sm" type="button"
			  		name="previous_summary_graphic_btn" id="previous_summary_graphic_btn" onclick="processUserSelection(this)"> Previous Match Summary (Shift+F11)</button>
			  		
			  	<button style="background-color:#2E008B;color:#FEFEFE;" class="btn btn-sm" type="button"
			  		name="ff_target_graphic_btn" id="ff_target_graphic_btn" onclick="processUserSelection(this)"> FF Target (Shift+D)</button>
			  	<button style="background-color:#2E008B;color:#FEFEFE;" class="btn btn-sm" type="button"
				  		name="leaderboard_graphic_btn" id="leaderboard_graphic_btn" onclick="processUserSelection(this)"> LeaderBoard (X)</button>	
			  		
			  	<button style="background-color:#ffe2db;color:#000000;" class="btn btn-sm" type="button"
			  		name="matchid_graphic_btn" id="matchid_graphic_btn" onclick="processUserSelection(this)"> MatchID (M)</button>
			  	<button style="background-color:#ffe2db;color:#000000;" class="btn btn-sm" type="button"
		  			name="match_promo_graphic_btn" id="match_promo_graphic_btn" onclick="processUserSelection(this)"> FF Match Promo (Ctrl+M)</button>
			  	<button style="background-color:#e0ffff;color:#000000;" class="btn btn-sm" type="button"
			  		name="ltpartnership_graphic_btn" id="ltpartnership_graphic_btn" onclick="processUserSelection(this)"> Current Partnership (Shift+K)</button>
			  	<button style="background-color:#6a33f7;color:#FEFEFE;" class="btn btn-sm" type="button"
			  		name="ffpartnership_graphic_btn" id="ffpartnership_graphic_btn" onclick="processUserSelection(this)"> FFPartnership (F4)</button>
			  	<button style="background-color:#bcb88a;color:#000000;" class="btn btn-sm" type="button"
			  		name="doubleteams_graphic_btn" id="doubleteams_graphic_btn" onclick="processUserSelection(this)"> Double Teams (Ctrl+F7)</button>
			  	<button style="background-color:#2E008B;color:#FEFEFE;" class="btn btn-sm" type="button"
			  		name="playingxi_graphic_btn" id="playingxi_graphic_btn" onclick="processUserSelection(this)"> PlayingXI (Ctrl+F8)</button>
			  		
			  	<button style="background-color:#ffb6c1;color:#000000;" class="btn btn-sm" type="button"
			  		name="playerprofile_graphic_btn" id="playerprofile_graphic_btn" onclick="processUserSelection(this)">PlayerProfile Bat (Ctrl+D)</button>
			  	<button style="background-color:#ffb6c1;color:#000000;" class="btn btn-sm" type="button"
			  		name="playerprofileball_graphic_btn" id="playerprofileball_graphic_btn" onclick="processUserSelection(this)">PlayerProfile Ball (Ctrl+E)</button>
			  		
			  	<button style="background-color:#ffe2db;color:#000000;" class="btn btn-sm" type="button"
			  		name="l3matchid_graphic_btn" id="l3matchid_graphic_btn" onclick="processUserSelection(this)"> L3MatchID (N)</button>
			  		
			  	<button style="background-color:#ffd700;color:#000000;" class="btn btn-sm" type="button"
			  		name="namesuper_graphic_btn" id="namesuper_graphic_btn" onclick="processUserSelection(this)">NameSuper-DB (F10)</button>
			  	<button style="background-color:#ffd700;color:#000000;" class="btn btn-sm" type="button"
			  		name="namesuper_player_graphic_btn" id="namesuper_player_graphic_btn" onclick="processUserSelection(this)">NameSuper-Player (F8)</button>
			  	
			  	<button style="background-color:#ff6347;color:#000000;" class="btn btn-sm" type="button"
			  		name="batsmanstyle_graphic_btn" id="batsmanstyle_graphic_btn" onclick="processUserSelection(this)">Batsman Style (Ctrl+F5)</button>
			    <button style="background-color:#ff6347;color:#000000;" class="btn btn-sm" type="button"
			  		name="bowlerstyle_graphic_btn" id="bowlerstyle_graphic_btn" onclick="processUserSelection(this)">Bowler Style (Ctrl+F9)</button>
			  			
			  	<button style="background-color:#ff6347;color:#000000;" class="btn btn-sm" type="button"
			  		name="batsmanstats_graphic_btn" id="batsmanstats_graphic_btn" onclick="processUserSelection(this)"> BatThisMatch (F5)</button>
			  	<button style="background-color:#ff6347;color:#000000;" class="btn btn-sm" type="button"
			  		name="bowlerstats_graphic_btn" id="bowlerstats_graphic_btn" onclick="processUserSelection(this)"> BowlerThisMatch (F9)</button>
			  		
			  	<button style="background-color:#f44336;color:#FEFEFE;" class="btn btn-sm" type="button"
			  		name="howout_graphic_btn" id="howout_graphic_btn" onclick="processUserSelection(this)">How Out (F6)</button>
			  	<button style="background-color:#f44336;color:#FEFEFE;" class="btn btn-sm" type="button"
			  		name="quick_howout_graphic_btn" id="quick_howout_graphic_btn" onclick="processUserSelection(this)">How Out Quick (Ctrl+F6)</button>
			  	<button style="background-color:#f44336;color:#FEFEFE;" class="btn btn-sm" type="button"
			  		name="howoutwithoutfielder_graphic_btn" id="howoutwithoutfielder_graphic_btn" onclick="processUserSelection(this)">How Out W/O Fielder (Shift+F6)</button>
			  		
			  	<button style="background-color:#32cd32;color:#000000;" class="btn btn-sm" type="button"
			  		name="split_graphic_btn" id="split_graphic_btn" onclick="processUserSelection(this)"> 30-50-Split (S)</button>
			  	<button style="background-color:#32cd32;color:#000000;" class="btn btn-sm" type="button"
			  		name="projected_score_graphic_btn" id="projected_score_graphic_btn" onclick="processUserSelection(this)"> Projected Score (Ctrl+A)</button>
			  	<button style="background-color:#32cd32;color:#000000;" class="btn btn-sm" type="button"
			  		name="fallofwicket_graphic_btn" id="fallofwicket_graphic_btn" onclick="processUserSelection(this)"> FallOfWicket (Shift+F3)</button>
			  	<button style="background-color:#2E008B;color:#FEFEFE;" class="btn btn-sm" type="button"
			  		name="comparision_graphic_btn" id="comparision_graphic_btn" onclick="processUserSelection(this)"> Comparision (Ctrl+F3)</button>
			  	
			  	<button style="background-color:#32cd32;color:#000000;" class="btn btn-sm" type="button"
			  		name="teamsummary_graphic_btn" id="teamsummary_graphic_btn" onclick="processUserSelection(this)"> Team-0s,1s,2s (Alt+F12)</button>
			  	<button style="background-color:#bbb477;color:#000000;" class="btn btn-sm" type="button"
			  		name="playersummary_graphic_btn" id="playersummary_graphic_btn" onclick="processUserSelection(this)">Batsman-0s,1s,2s (Shift+F5)</button>
			  	<button style="background-color:#bbb477;color:#000000;" class="btn btn-sm" type="button"
			  		name="bowlersummary_graphic_btn" id="bowlersummary_graphic_btn" onclick="processUserSelection(this)">Bowler-0s,1s,2s (Shift+F9)</button>
			  	
			  	<button style="background-color:#ffb6c1;color:#000000;" class="btn btn-sm" type="button"
			  		name="l3playerprofileball_graphic_btn" id="l3playerprofileball_graphic_btn" onclick="processUserSelection(this)">L3PlayerProfile Ball (F11)</button>
			  	<button style="background-color:#ffb6c1;color:#000000;" class="btn btn-sm" type="button"
			  		name="l3playerprofileBat_graphic_btn" id="l3playerprofileBat_graphic_btn" onclick="processUserSelection(this)">L3PlayerProfile Bat (F7)</button>
			  		
			  	<button style="background-color:#2f4f4f;color:#FEFEFE;" class="btn btn-sm" type="button"
			  		name="target_graphic_btn" id="target_graphic_btn" onclick="processUserSelection(this)"> Target (D)</button>
			  	<button style="background-color:#2f4f4f;color:#FEFEFE;" class="btn btn-sm" type="button"
			  		name="equation_graphic_btn" id="equation_graphic_btn" onclick="processUserSelection(this)"> Equation (E)</button>
			  		
			  	<button style="background-color:#2E008B;color:#FEFEFE;" class="btn btn-sm" type="button"
			  		name="generic_lt_graphic_btn" id="generic_lt_graphic_btn" onclick="processUserSelection(this)"> Generic_LT (Q)</button>
			  	
			  	<button style="background-color:#2E008B;color:#FEFEFE;" class="btn btn-sm" type="button"
			  		name="this_series_stats_graphic_btn" id="this_series_stats_graphic_btn" onclick="processUserSelection(this)"> This Series Stats (Ctrl+S)</button>
			  		
			  	<button style="background-color:#2E008B;color:#FEFEFE;" class="btn btn-sm" type="button"
			  		name="most_runs_graphic_btn" id="most_runs_graphic_btn" onclick="processUserSelection(this)"> Most Runs (Z)</button>
			  	 <button style="background-color:#2E008B;color:#FEFEFE;" class="btn btn-sm" type="button"
			  		name="most_wickets_graphic_btn" id="most_wickets_graphic_btn" onclick="processUserSelection(this)"> Top Wickets (Ctrl+Z)</button>
			  	<button style="background-color:#2E008B;color:#FEFEFE;" class="btn btn-sm" type="button"
			  		name="most_fours_graphic_btn" id="most_fours_graphic_btn" onclick="processUserSelection(this)"> Top Fours (Ctrl+X)</button>
			  	<button style="background-color:#2E008B;color:#FEFEFE;" class="btn btn-sm" type="button"
			  		name="most_sixes_graphic_btn" id="most_sixes_graphic_btn" onclick="processUserSelection(this)"> Top Sixes (Ctrl+C)</button>
			  	<button style="background-color:#40e0d0;color:#000000;" class="btn btn-sm" type="button"
			  		name="manhattan_graphic_btn" id="manhattan_graphic_btn" onclick="processUserSelection(this)">Manhattan (Ctrl+F10)</button>	
			  	<!--  <button style="background-color:#2E008B;color:#FEFEFE;" class="btn btn-sm" type="button"
			  		name="highest_runs_graphic_btn" id="highest_runs_graphic_btn" onclick="processUserSelection(this)"> Highest Individual Score </button> -->
			  	</c:if>			  	
  				</div>
			  </div>
	       </div>
	    </div>
       </div>
    </div>
  </div>
</div>
<input type="hidden" id="which_keypress" name="which_keypress" value="${session_match.setup.which_key_press}"/>
<input type="hidden" name="selected_broadcaster" id="selected_broadcaster" value="${session_selected_broadcaster}"/>
<input type="hidden" name="selected_which_layer" id="selected_which_layer" value="${selected_layer}"/>
<input type="hidden" name="selected_which_scene" id="selected_which_scene" value="${selected_scene}"/>
<input type="hidden" name="selected_match_max_overs" id="selected_match_max_overs" value="${session_match.setup.maxOvers}"/>
<input type="hidden" id="matchFileTimeStamp" name="matchFileTimeStamp" value="${session_match.setup.matchFileTimeStamp}"></input>
</form:form>
</body>
</html>