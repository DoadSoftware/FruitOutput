<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>

  <meta charset="utf-8" name="viewport" content="width=device-width, initial-scale=1">
  <title>Initialise Screen</title>

  <script type="text/javascript" src="<c:url value="/webjars/jquery/3.6.0/jquery.min.js"/>"></script>
  <script type="text/javascript" src="<c:url value="/webjars/bootstrap/5.1.3/js/bootstrap.min.js"/>"></script>
  <script type="text/javascript" src="<c:url value="/resources/javascript/index.js"/>"></script>
  
  <link rel="stylesheet" href="<c:url value="/webjars/bootstrap/5.1.3/css/bootstrap.min.css"/>"/>  
  <link href="<c:url value="/webjars/font-awesome/6.0.0/css/all.css"/>" rel="stylesheet">
		
</head>
<body onload="initialiseForm('initialise',null)">
<form:form name="initialise_form" autocomplete="off" action="output" method="POST">
<div class="content py-5" style="background-color: #EAE8FF; color: #2E008B">
  <div class="container">
	<div class="row">
	 <div class="col-md-8 offset-md-2">
       <span class="anchor"></span>
         <div class="card card-outline-secondary">
           <div class="card-header">
             <h3 class="mb-0">Initialise</h3>
           </div>
          <div class="card-body">
            <div id="initialise_div" style="display:none;">
			  </div>
			  <div class="form-group row row-bottom-margin ml-2" style="margin-bottom:5px;">
			    <label for="select_configuration_file" class="col-sm-4 col-form-label text-left">Select Configuration </label>
			    <div class="col-sm-6 col-md-6">
			      <select id="select_configuration_file" name="select_configuration_file" 
			      		class="brower-default custom-select custom-select-sm" onchange="processUserSelection(this)">
			          	<option value=""></option>
						<c:forEach items = "${configuration_files}" var = "config">
				          	<option value="${config.name}">${config.name}</option>
						</c:forEach>
			      </select>
			    </div>
			  </div>
			  <div class="form-group row row-bottom-margin ml-2" style="margin-bottom:5px;">
			    <label for="configuration_file_name" class="col-sm-4 col-form-label text-left">Configuration File Name </label>
			    <div class="col-sm-6 col-md-6">
		             <input type="text" id="configuration_file_name" name="configuration_file_name"
		             	class="form-control form-control-sm floatlabel"></input>
			    </div>
			  </div>
			  <div class="form-group row row-bottom-margin ml-2" style="margin-bottom:5px;">
			    <label for="select_cricket_matches" class="col-sm-4 col-form-label text-left">Select Cricket Match </label>
			    <div class="col-sm-6 col-md-6">
			      <select id="select_cricket_matches" name="select_cricket_matches" 
			      		class="brower-default custom-select custom-select-sm">
						<c:forEach items = "${match_files}" var = "match">
				          	<option value="${match.name}">${match.name}</option>
						</c:forEach>
			      </select>
			    </div>
			  </div>
			  <div class="form-group row row-bottom-margin ml-2" style="margin-bottom:5px;">
			    <label for="select_broadcaster" class="col-sm-4 col-form-label text-left">Select Broadcaster </label>
			    <div class="col-sm-6 col-md-6">
			      <select id="select_broadcaster" name="select_broadcaster" class="browser-default custom-select custom-select-sm"
			      		onchange="processUserSelection(this)">
			      		<option value="DOAD_FRUIT">DOAD_FRUIT NEW</option>
			      		<option value="ISPL_FRUIT">DOAD_FRUIT<!-- ISPL FRUIT --></option>
			      		<option value="LCT_FRUIT">LCT FRUIT</option> 
			      		
			      </select>
			    </div>
			  </div>
			  <div class="form-group row row-bottom-margin ml-2" style="margin-bottom:5px;">
				    <label for="showSpeed" class="col-sm-4 col-form-label text-left"> Select Speed </label>
				    <div class="col-sm-6 col-md-6">
				        <select id="showSpeed" name="showSpeed" class="browser-default custom-select custom-select-sm"
				                onchange="processUserSelection(this)">
				            <option value="WITH" ${session_configuration.showSpeed == 'WITH' ? 'selected' : ''}>WITH</option>
				            <option value="WITHOUT" ${session_configuration.showSpeed == 'WITHOUT' ? 'selected' : ''}>WITHOUT</option>
				        </select>
				    </div>
				</div>
			   <div class="form-group row row-bottom-margin ml-2" style="margin-bottom:5px;">
			    <label for="speed_select" class="col-sm-4 col-form-label text-left"> Select Speed Type</label>
			    <div class="col-sm-6 col-md-6">
			      <select id="speed_select" name="speed_select" value="${session_configuration.speedUnit}" class="browser-default custom-select custom-select-sm"
			      		onchange="processUserSelection(this)">
			      		<option value="KP/H">KP/H</option>
			      		<option value="MP/H">MP/H</option>
			      </select>
			    </div>
			  </div>
			  <div class="form-group row row-bottom-margin ml-2" style="margin-bottom:5px;">
			    <label for="showReview" class="col-sm-4 col-form-label text-left"> Select Review </label>
			    <div class="col-sm-6 col-md-6">
			        <select id="showReview" name="showReview" class="browser-default custom-select custom-select-sm"
			                onchange="processUserSelection(this)">
			            <option value="WITH" ${session_configuration.showReview == 'WITH' ? 'selected' : ''}>WITH</option>
			            <option value="WITHOUT" ${session_configuration.showReview == 'WITHOUT' ? 'selected' : ''}>WITHOUT</option>
			        </select>
			    </div>
				</div>
			 <div class="form-group row row-bottom-margin ml-2" style="margin-bottom:5px;">
			    <label for="showSubs" class="col-sm-4 col-form-label text-left"> Select Subs </label>
			    <div class="col-sm-6 col-md-6">
			        <select id="showSubs" name="showSubs" class="browser-default custom-select custom-select-sm"
			                onchange="processUserSelection(this)">
			            <option value="WITH" ${session_configuration.showSubs == 'WITH' ? 'selected' : ''}>WITH</option>
			            <option value="WITHOUT" ${session_configuration.showSubs == 'WITHOUT' ? 'selected' : ''}>WITHOUT</option>
			        </select>
			    </div>
				</div>
			  <div class="form-group row row-bottom-margin ml-2" style="margin-bottom:5px;">
			    <label for="speed_select" class="col-sm-4 col-form-label text-left"> Audio </label>
			    <div class="col-sm-6 col-md-6">
			      <select id="select_audio" name="select_audio" value="${session_configuration.audio}" class="browser-default custom-select custom-select-sm"
			      		onchange="processUserSelection(this)">
			      		<option value=""></option>
			      		<option value="LastBallAudio">Last Ball Audio</option>
			      		<option value="LastBallEndOverAudio">Without Audio</option>
			      </select>
			    </div>
			  </div>
			  <div class="form-group row row-bottom-margin ml-2" style="margin-bottom:5px;">
			    <label for="select_Client" class="col-sm-4 col-form-label text-left">Select Client </label>
			    <div class="col-sm-6 col-md-6">
			      <select id="select_Client" name="select_Client" value="${session_configuration.select_Client}"class="browser-default custom-select custom-select-sm"
			      		onchange="processUserSelection(this)">
			      		<option value="0"></option>
			      		<option value="1">BCCI</option>
			      		<option value="2">Absolute Broadcast</option>
			      		<option value="3">ICC</option> 
			      		<option value="4">RISE</option>
			      		<option value="5">WILDTRACK</option> 
			      </select>
			    </div>
			  </div>
			<div class="row">
			<table class="table table-bordered table-responsive">
			  <tbody>
			    <tr>
			      <td>
				    <label for="vizIPAddress" class="col-sm-4 col-form-label text-left">Viz/Everest IP</label>				    
		             <input type="text" id="vizIPAddress" name="vizIPAddress" value="${session_configuration.primaryIpAddress}"
		             	class="form-control form-control-sm floatlabel"></input>
			      </td>
			      <td>
				    <label for="vizPortNumber" class="col-sm-4 col-form-label text-left">Viz/Everest Port</label>				    
		             <input type="text" id="vizPortNumber" name="vizPortNumber" value="${session_configuration.primaryPortNumber}"
		             	class="form-control form-control-sm floatlabel"></input>
			      </td>
			    </tr>
			  </tbody>
		    </table>
		    </div>		  
		    <button style="background-color:#2E008B;color:#FEFEFE;" class="btn btn-sm" type="button"
		  		name="load_scene_btn" id="load_scene_btn" onclick="processUserSelection(this)"> Load Scene</button>
	       </div>
	    </div>
       </div>
    </div>
  </div>
</div>
</form:form>
</body>
</html>