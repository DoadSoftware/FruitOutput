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
	  
	  if(e.altKey && e.key === 'f'){
		  e.preventDefault();
		  processUserSelectionData('LOGGER_FORM_KEYPRESS','FRUIT');
	  }else if(e.altKey && e.key == 'r'){
		  e.preventDefault();
		  processUserSelectionData('LOGGER_FORM_KEYPRESS','RE_READ_DATA');
	  }else if(e.altKey && e.key === 't'){
		  e.preventDefault();
		  processUserSelectionData('LOGGER_FORM_KEYPRESS','TEAM');
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
			  	<label class="col-sm-4 col-form-label text-left">Fruit: Alt + f </label>
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