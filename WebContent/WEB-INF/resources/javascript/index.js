var session_match,stats;
function processWaitingButtonSpinner(whatToProcess) 
{
	switch (whatToProcess) {
	case 'START_WAIT_TIMER': 
		$('.spinner-border').show();
		$(':button').prop('disabled', true);
		break;
	case 'END_WAIT_TIMER': 
		$('.spinner-border').hide();
		$(':button').prop('disabled', false);
		break;
	}
}
function initialiseForm(whatToProcess,dataToProcess)
{
	switch (whatToProcess) {
	case 'UPDATE-CONFIG':
		document.getElementById('configuration_file_name').value = $('#select_configuration_file option:selected').val();
		document.getElementById('select_cricket_matches').value = dataToProcess.filename;
		document.getElementById('select_broadcaster').value = dataToProcess.broadcaster;
		document.getElementById('speed_select').value = dataToProcess.speedUnit;
		document.getElementById('vizIPAddress').value = dataToProcess.primaryIpAddress;
		document.getElementById('vizPortNumber').value = dataToProcess.primaryPortNumber;
		break;
	case 'initialise':
		processUserSelection($('#select_broadcaster'));
		break;
	case 'UPDATE-MATCH-ON-OUTPUT-FORM':
	
		dataToProcess.match.inning.forEach(function(inn,index,arr){
			
			if(inn.isCurrentInning == 'YES'){
			inn.battingCard.forEach(function(bc,index,arr){
					if(inn.partnerships != null && inn.partnerships.length > 0) {	
						inn.partnerships.forEach(function(par,index,arr){
							if(bc.playerId == par.firstBatterNo) {
								
								if(bc.onStrike == 'YES'){
									document.getElementById('inning1_battingcard1_lbl').innerHTML = bc.player.surname + '*' + ' ' + bc.runs + '(' + bc.balls + ')' ;
								}else{
								document.getElementById('inning1_battingcard1_lbl').innerHTML = bc.player.surname +  ' ' + bc.runs + '(' + bc.balls + ')' ;
	
								}
							}
						else if(bc.playerId == par.secondBatterNo) {
							if(bc.onStrike == 'NO'){
								document.getElementById('inning1_battingcard2_lbl').innerHTML = bc.player.surname + ' ' + bc.runs + '(' + bc.balls + ')';
							}else{
							document.getElementById('inning1_battingcard2_lbl').innerHTML = bc.player.surname + '*' + ' ' + bc.runs + '(' + bc.balls + ')';
	
							}
						}
							});
						
					}
					document.getElementById('inning1_totalruns_lbl').innerHTML = inn.batting_team.teamName4 + '-' + parseInt(inn.totalRuns) + '-' 
										+ parseInt(inn.totalWickets) + '('+ parseInt(inn.totalOvers) + '.' + parseInt(inn.totalBalls) + ')' ;
				
			});
			}
			if(inn.isCurrentInning == 'YES'){
			inn.bowlingCard.forEach(function(boc,index,arr){
						if(boc.status == 'CURRENTBOWLER'){
							document.getElementById('inning1_bowlingcard_lbl').innerHTML = boc.player.surname + ' ' + boc.wickets 
										+ '-' + boc.runs + '(' + boc.overs + '.' + boc.balls + ')';
						}else if(boc.status == 'LASTBOWLER'){
							document.getElementById('inning1_bowlingcard_lbl').innerHTML = boc.player.surname + ' ' + boc.wickets 
										+ '-' + boc.runs + '(' + boc.overs + '.' + boc.balls + ')';
						}
					});	
					}
		});
		break;
	}
}
function processUserSelectionData(whatToProcess,dataToProcess){
	switch (whatToProcess) {
	case 'LOGGER_FORM_KEYPRESS':
		
		switch (dataToProcess) {
		case 'FRUIT':
			processCricketProcedures('POPULATE-FRUIT');
			break;
		case 32:
			processCricketProcedures('CLEAR-ALL');
			break;
		
		case 189:
			if(confirm('It will Also Delete Your Preview from Directory...\r\n \r\nAre You Sure To Animate Out? ') == true){
				processCricketProcedures('ANIMATE-OUT');
			}
			break;
		case 49: case 50: case 51: case 52: // Key 1 to 4
			if(session_match.setup.maxOvers > 0){
				switch (dataToProcess) {
				case 51: case 52: // Key 1 to 4
					alert("3rd and 4th inning NOT available in a limited over match");
					return false;
				}				
			}
			document.getElementById('which_keypress').value = parseInt(dataToProcess) - 48; // DJ
			document.getElementById('selected_inning').innerHTML = 'Selected Inning: ' + (parseInt(dataToProcess) - 48);
			break;
		}
	}
}
function processUserSelection(whichInput)
{	
	switch ($(whichInput).attr('name')) {
	
	case 'select_configuration_file':
	
		processCricketProcedures('GET-CONFIG-DATA');
		break;
	
	case 'animateout_graphic_btn':
		if(confirm('It will Also Delete Your Preview from Directory...\r\n \r\nAre You Sure To Animate Out? ') == true){
			processCricketProcedures('ANIMATE-OUT');	
		}
		break;
	case 'clearall_graphic_btn':
		processCricketProcedures('CLEAR-ALL');
		break;
	case 'cancel_graphics_btn':
		$('#select_graphic_options_div').empty();
		document.getElementById('select_graphic_options_div').style.display = 'none';
		$("#captions_div").show();
		$("#cancel_match_setup_btn").show();
		break;
	case 'load_scene_btn':
      	document.initialise_form.submit();
		break;
	}
}
function processCricketProcedures(whatToProcess)
{
	var valueToProcess;
	
	switch(whatToProcess) {
	case 'GET-CONFIG-DATA':
		valueToProcess = $('#select_configuration_file option:selected').val();
		break;
		
	case 'READ-MATCH-AND-POPULATE':
		valueToProcess = $('#matchFileTimeStamp').val();
		break;
	case 'POPULATE-FRUIT':
		switch ($('#selected_broadcaster').val().toUpperCase()) {
		case 'FRUIT':
			valueToProcess = 'D:/DOAD_In_House_Everest/Everest_Cricket/EVEREST_FRUIT/Scenes/Fruit.sum';
			break;
			
		}
		break;
	
	}

	$.ajax({    
        type : 'Get',     
        url : 'processCricketProcedures.html',     
        data : 'whatToProcess=' + whatToProcess + '&valueToProcess=' + valueToProcess, 
        dataType : 'json',
        success : function(data) {
        	switch(whatToProcess) {
			case 'GET-CONFIG-DATA':
				initialiseForm('UPDATE-CONFIG',data);
				break;
			case 'READ-MATCH-AND-POPULATE': case 'RE_READ_DATA':
				if(data){
					session_match = data;
					initialiseForm('UPDATE-MATCH-ON-OUTPUT-FORM',data);
				}
				break;
			case 'POPULATE-FRUIT':
					if(confirm('Animate In?') == true){
							$('#select_graphic_options_div').empty();
							document.getElementById('select_graphic_options_div').style.display = 'none';
							$("#captions_div").show();
							
				        	switch(whatToProcess) {
							case 'POPULATE-FRUIT':
								processCricketProcedures('ANIMATE-IN-FRUIT');
								break;
							}
						}
				break;
        	}
			processWaitingButtonSpinner('END_WAIT_TIMER');
	    },    
	    error : function(e) {    
	  	 	console.log('Error occured in ' + whatToProcess + ' with error description = ' + e);     
	    }    
	});
}

function checkEmpty(inputBox,textToShow) {

	var name = $(inputBox).attr('id');
	
	document.getElementById(name + '-validation').innerHTML = '';
	document.getElementById(name + '-validation').style.display = 'none';
	$(inputBox).css('border','');
	if(document.getElementById(name).value.trim() == '') {
		$(inputBox).css('border','#E11E26 2px solid');
		document.getElementById(name + '-validation').innerHTML = textToShow + ' required';
		document.getElementById(name + '-validation').style.display = '';
		document.getElementById(name).focus({preventScroll:false});
		return false;
	}
	return true;	
}