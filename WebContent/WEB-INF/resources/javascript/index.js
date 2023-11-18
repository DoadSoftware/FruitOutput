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
		document.getElementById('which_scene').value = dataToProcess.primaryScene;
		document.getElementById('vizIPAddress').value = dataToProcess.primaryIpAddress;
		document.getElementById('vizPortNumber').value = dataToProcess.primaryPortNumber;
		document.getElementById('vizSecondaryIPAddress').value = dataToProcess.secondaryIpAddress;
		document.getElementById('vizSecondaryPortNumber').value = dataToProcess.secondaryPortNumber;
		document.getElementById('vizTertiaryIPAddress').value = dataToProcess.tertiaryIpAddress;
		document.getElementById('vizTertiaryPortNumber').value = dataToProcess.tertiaryPortNumber;
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
								}
							}
						else if(bc.playerId == par.secondBatterNo) {
							if(bc.onStrike == 'NO'){
								document.getElementById('inning1_battingcard2_lbl').innerHTML = bc.player.surname + ' ' + bc.runs + '(' + bc.balls + ')';
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
		/*case 18:
			processCricketProcedures('POPULATE-L3-BUGTARGET');
			break;*/
		case 'RE_READ':
			processCricketProcedures('RE_READ_DATA');
			break;
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
			
		case 'ANIMATE_OUT_INFOBAR_TOP':
			switch ($('#selected_broadcaster').val().toUpperCase()){
				case 'APL': case 'PUNJAB_T20':
					processCricketProcedures('ANIMATE-OUT-SECTION2');
					break;
			}
			break;	
		// case Alt+Minus keys this will animate out infobar
		// case CtrL+1..to...Ctrl+0 keys this will do all change ons for infobar
		// case F1 to f12, Ctrl+F1 to Ctrl+F12, Shift+F1 to Shift+12, Crtrl+Shift+F1 to Ctrl+Shift+F12 // Tese buttons will have all the graphics excluding infobar
		}
		break;
	}
}
function processUserSelection(whichInput)
{	
	switch ($(whichInput).attr('name')) {
	
	case 'select_configuration_file':
	
		processCricketProcedures('GET-CONFIG-DATA');
		break;
	
	
	case 'animate_in_speed':
		var speed_value = document.getElementById("speedtext").value;
		if(speed_value >= 45 && speed_value <= 160){
			processCricketProcedures('ANIMATE_IN_SPEED_SECOND_BROADCASTER');
			//alert(speed_value);
		}
		break;	
	
	case 'animateout_graphic_btn':
		if(confirm('It will Also Delete Your Preview from Directory...\r\n \r\nAre You Sure To Animate Out? ') == true){
			processCricketProcedures('ANIMATE-OUT');	
		}
		break;
	case 'animate_out_graphic_btn':
		processCricketProcedures('ANIMATE-OUT-SECTION2');	
		break;
	case 'clearall_graphic_btn':
		processCricketProcedures('CLEAR-ALL');
		break;
		
	// Graphic Button
	///-----------------------------------------------------------------------------------///	
	case 'bug_graphic_btn': case 'howout_graphic_btn': case 'howout_both_graphic_btn': case 'bat_performer_graphic_btn': case 'ball_performer_graphic_btn':
		
		$("#captions_div").hide();
		$("#cancel_match_setup_btn").hide();
		$("#expiry_message").hide();
		
		switch ($(whichInput).attr('name')) {
		case 'bug_multi_partnership_graphic_btn':
			processCricketProcedures('BUG_MULTI_GRAPHICS-OPTIONS');
			break;
		case 'bug_graphic_btn': // DJ -> all these buttong will NOT go to 'processCricketProcedures' to do AJAX calling. But will instead use session_match
			addItemsToList('BUG-OPTIONS',null);
			addItemsToList('POPULATE-BATSMAN',null);			
			break;
		}
		break;
	case 'scorecard_graphic_btn': case 'bowlingcard_graphic_btn': case 'matchsummary_graphic_btn': case 'populate_match_summary__btn':  case 'populate_bug_btn': 
	processWaitingButtonSpinner('START_WAIT_TIMER');
		switch ($(whichInput).attr('name')) {
		case 'scorecard_graphic_btn':
			processCricketProcedures('POPULATE-FF-SCORECARD');
			break;
		case 'bowlingcard_graphic_btn':
			processCricketProcedures('POPULATE-FF-BOWLINGCARD');
			break;
		case 'populate_partnership_btn':
			processCricketProcedures('POPULATE-FF-PARTNERSHIP');
			break;
		}
		break;
/*	case 'cancel_match_setup_btn':
		document.output_form.method = 'post';
		document.output_form.action = 'initialise';
	   	document.output_form.submit();
		break;*/
	case 'cancel_graphics_btn':
		$('#select_graphic_options_div').empty();
		document.getElementById('select_graphic_options_div').style.display = 'none';
		$('#lastxball_div').empty();
		document.getElementById('lastxball_div').style.display = 'none';
		$("#captions_div").show();
		$("#cancel_match_setup_btn").show();
		break;
	case 'select_broadcaster':
		switch ($('#select_broadcaster :selected').val().toUpperCase()) {
		case 'THAILAND': case 'ACC_NEPAL': case 'RSWS':
			$('#vizPortNumber').attr('value','1980');
			$('label[for=vizScene], input#vizScene').hide();
			$('label[for=which_scene], select#which_scene').show();
			$('label[for=which_layer], select#which_layer').show();
			switch ($('#which_scene :selected').val().toUpperCase()) {
			case 'INFOBAR':
				document.getElementById('selected_which_scene').value = 'D:/DOAD_In_House_Everest/Everest_Cricket/EVEREST_APL2022/Scenes/ScoreBug_Test.sum';
				break;
			}
			switch ($('#which_layer :selected').val().toUpperCase()) {
			case 'FRONT':
				document.getElementById('selected_which_layer').value = 'FRONT_LAYER';
				break;
			}			
			break;
		}
		break;
	case 'load_scene_btn':
		/*if(checkEmpty($('#vizIPAddress'),'IP Address Blank') == false
			|| checkEmpty($('#vizPortNumber'),'Port Number Blank') == false) {
			return false;
		}*/
		//document.initialise_form.method = 'post';
		//document.initialise_form.action = 'initialise';
	   	//document.initialise_form.submit();
      	document.initialise_form.submit();
		break;
	case 'selectInni':
		switch ($(whichInput).attr('name')) {
		case 'selectInni':
			addItemsToList('POPULATE-PLAYERSBAT',session_match);
			break;
		}
		break;
	case 'selectInnin':
		switch ($(whichInput).attr('name')) {
		case 'selectInnin':
			addItemsToList('POPULATE-PLAYERSBALL',session_match);
			break;
		}
		break;
	case 'selectPartInning':
		switch ($(whichInput).attr('name')) {
		case 'selectPartInning':
			addItemsToList('POPULATE-PARTNER',session_match);
			break;
		}
		break;		
	case 'selectInning': case 'selectStatsType': case 'selectStatType': case 'selectInn':
		switch ($(whichInput).attr('name')) {
		case 'selectInning': case 'selectStatsType':
			addItemsToList('POPULATE-PLAYERS',session_match);
			break;
		case 'selectInn': case 'selectStatType':
			addItemsToList('POPULATE-HOWOUT-PLAYERS',session_match);
			break;
		}
		break;
	case 'selectTeam': case 'selectCaptianWicketKeeper':
		addItemsToList('POPULATE-PLAYER',session_match);
		break;
	case 'selectTeams':
		addItemsToList('POPULATE-PROFILE',session_match);
		break;
	case 'selectl3Teams':
		switch ($(whichInput).attr('name')) {
		case 'selectl3Teams':
			addItemsToList('POPULATE-L3PROFILEBAT',session_match);
			addItemsToList('POPULATE-L3PROFILE',session_match);
			addItemsToList('POPULATE-THISSERIES-STATS',session_match);
			break;
		}
		break;
		
	case 'selectType':
		switch ($(whichInput).attr('name')) {
		case 'selectType':
			if($('#selectType option:selected').val() == 'RUNS'){
				processCricketProcedures('MOST1_GRAPHICS-OPTIONS');
			}else if($('#selectType option:selected').val() == 'WICKETS'){
				processCricketProcedures('MOST1_WICKETS_GRAPHICS-OPTIONS');
			}
			//processCricketProcedures('MOST1_GRAPHICS-OPTIONS');
			break;
		}
		break;
		
	case 'selectmoststatsType':
		switch ($(whichInput).attr('name')) {
		case 'selectmoststatsType':
			if($('#selectType option:selected').val() == 'RUNS'){
				processCricketProcedures('MOST1_GRAPHICS-OPTIONS');
			}else if($('#selectType option:selected').val() == 'WICKETS'){
				processCricketProcedures('MOST1_WICKETS_GRAPHICS-OPTIONS');
			}
			break;
		}
		break;
		
	case 'selectleaderboardType':
		switch ($(whichInput).attr('name')) {
		case 'selectleaderboardType':
		
			if ($('#selectleaderboardType option:selected').val() == 'most_runs'){
				processCricketProcedures('LEADERBOARD_GRAPHICS-OPTIONS');
			} else if($('#selectleaderboardType option:selected').val() == 'most_wickets'){
				processCricketProcedures('WICKETS_GRAPHICS-OPTIONS');
			}else if($('#selectleaderboardType option:selected').val() == 'most_fours'){
				processCricketProcedures('FOURS_GRAPHICS-OPTIONS');
			}else if($('#selectleaderboardType option:selected').val() == 'most_sixes'){
				processCricketProcedures('SIXES_GRAPHICS-OPTIONS');
			}
			break;
		}
		break;
	
	case 'selectedInning':
		switch ($(whichInput).attr('name')) {
		case 'selectedInning':
			addItemsToList('POPULATE-PLAYERS_DATA',session_match);
			break;
		}
		break;
	case 'selectedInn':
		switch ($(whichInput).attr('name')) {
		case 'selectedInn':
			addItemsToList('POPULATE-BOWLER_DATA',session_match);
			break;
		}
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
	case 'POPULATE-FF-SCORECARD': 
		switch ($('#selected_broadcaster').val().toUpperCase()) {
		case 'ACC_NEPAL':
			valueToProcess = 'D:/DOAD_In_House_Everest/Everest_Cricket/Everest_Cric2022/Scenes/Bat_Ball_Summ_PTable.sum' + ',' 
								+ document.getElementById('which_keypress').value ;
			break;
			
		}
		break;
	case 'POPULATE-FF-BOWLINGCARD': 
		switch ($('#selected_broadcaster').val().toUpperCase()) {
		case 'ACC_NEPAL':
			valueToProcess = 'D:/DOAD_In_House_Everest/Everest_Cricket/Everest_Cric2022/Scenes/Bat_Ball_Summ_PTable.sum' + ',' 
								+ document.getElementById('which_keypress').value ;
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
			case 'SHOW_SPEED':
				if(data == true){
					document.getElementById('speed_lbl').innerHTML = 'Show Speed: ' + 'YES';
				}else if(data == false){
					document.getElementById('speed_lbl').innerHTML = 'Show Speed: ' + 'NO';
				}
				break;
			case 'NAMESUPER_GRAPHICS-OPTIONS':
				addItemsToList('NAMESUPER-OPTIONS',data);
				break;
			case 'BUG_DB2_GRAPHICS-OPTIONS':
				addItemsToList('POPULATE-BUG-SCENE',data);
				session_match = data;
				break;
			
			
			case 'POPULATE-FF-MATCHID': case 'POPULATE-LT-MATCHID': case 'POPULATE-FF-LANDMARK': case 'POPULATE-LT-EQUATION': case 'POPULATE-FF-POSITION_LANDMARK':
			//if (data.status.toUpperCase() == 'SUCCESSFUL') {
					if(confirm('Animate In?') == true){
						$('#select_graphic_options_div').empty();
						document.getElementById('select_graphic_options_div').style.display = 'none';
						$("#captions_div").show();
			        	switch(whatToProcess) {
						case 'POPULATE-THISOVER':
							processCricketProcedures('ANIMATE-IN-THISOVER');
							break;
						case 'POPULATE-FF-MATCHID':
							processCricketProcedures('ANIMATE-IN-MATCHID');				
							break;
						case 'POPULATE-LT-MATCHID':
							processCricketProcedures('ANIMATE-IN-L3MATCHID');
							break;
						}
					}
				//} else {
					//alert(data.status);
				//}
				break;
			case 'POPULATE-FRUIT':
			//if (data.status.toUpperCase() == 'SUCCESSFUL') {
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
				//} else {
					//alert(data.status);
				//}
				break;
        	}
			processWaitingButtonSpinner('END_WAIT_TIMER');
	    },    
	    error : function(e) {    
	  	 	console.log('Error occured in ' + whatToProcess + ' with error description = ' + e);     
	    }    
	});
}
function addItemsToList(whatToProcess, dataToProcess)
{
	var select,option,header_text,div,table,tbody,row,max_cols;
	var cellCount = 0;

	switch (whatToProcess) {
	case 'POPULATE-BOWLERSTYLE':
		$('#selectbowler').empty();
		
		session_match.match.inning.forEach(function(inn,index,arr){
			if(inn.inningNumber == $('#selectBowlerInning option:selected').val()){
				if(inn.bowlingTeamId == session_match.setup.homeTeamId){
					session_match.setup.homeSquad.forEach(function(hs,index,arr){
						$('#selectbowler').append(
							$(document.createElement('option')).prop({
							value: hs.playerId,
							text: hs.full_name
						}))
					});
					session_match.setup.homeOtherSquad.forEach(function(hos,index,arr){
						$('#selectbowler').append(
							$(document.createElement('option')).prop({
							value: hos.playerId,
							text: hos.full_name + ' (OTHER)'
						}))
					});
				}else{
					session_match.setup.awaySquad.forEach(function(as,index,arr){
						$('#selectbowler').append(
							$(document.createElement('option')).prop({
							value: as.playerId,
							text: as.full_name
						}))
					});
					session_match.setup.awayOtherSquad.forEach(function(aos,index,arr){
						$('#selectbowler').append(
							$(document.createElement('option')).prop({
							value: aos.playerId,
							text: aos.full_name + ' (OTHER)'
						}))
					});
				}
			}
		});
		break;
		
	case 'POPULATE-PLAYERS' :
	
		$('#selectPlayers').empty();

		session_match.match.inning.forEach(function(inn,index,arr){
			if(inn.inningNumber == $('#selectInning option:selected').val()){
				if($('#selectStatsType option:selected').val() == 'Batsman'){
					inn.battingCard.forEach(function(bc,bc_index,bc_arr){
			            $('#selectPlayers').append(
							$(document.createElement('option')).prop({
			                value: bc.playerId,
			                text: bc.player.full_name + " - " + bc.status
			            }))					
					});
				} else{
					inn.bowlingCard.forEach(function(boc,boc_index,boc_arr){
			            $('#selectPlayers').append(
							$(document.createElement('option')).prop({
			                value: boc.playerId,
			                text: boc.player.full_name
			            }))
			            						
					});
				}	
			}
		});
		break;
	case 'MATCH_SUMMARY-OPTIONS':
		switch ($('#selected_broadcaster').val().toUpperCase()) {
		
		case 'FAIR_BREAK': case 'RPL':

			$('#select_graphic_options_div').empty();
	
			header_text = document.createElement('h6');
			header_text.innerHTML = 'Select Graphic Options';
			document.getElementById('select_graphic_options_div').appendChild(header_text);
			
			table = document.createElement('table');
			table.setAttribute('class', 'table table-bordered');
					
			tbody = document.createElement('tbody');
	
			table.appendChild(tbody);
			document.getElementById('select_graphic_options_div').appendChild(table);
			
			row = tbody.insertRow(tbody.rows.length);
			
			select = document.createElement('select');
			select.id = 'selectType';
			select.name = select.id;
			
			option = document.createElement('option');
			option.value = 'with_photo';
			option.text = 'With Photo';
			select.appendChild(option);
			
			option = document.createElement('option');
			option.value = 'without_photo';
			option.text = 'Without Photo';
			select.appendChild(option);
			
			row.insertCell(cellCount).appendChild(select);
			cellCount = cellCount + 1;
			
			option = document.createElement('input');
	    	option.type = 'button';
			option.name = 'populate_match_summary__btn';
		    option.value = 'Populate';
			
		    option.id = option.name;
		    option.setAttribute('onclick',"processUserSelection(this)");
		    
		    div = document.createElement('div');
		    div.append(option);
	
			option = document.createElement('input');
			option.type = 'button';
			option.name = 'cancel_graphics_btn';
			option.id = option.name;
			option.value = 'Cancel';
			option.setAttribute('onclick','processUserSelection(this)');
	
		    div.append(option);
		    
		    row.insertCell(cellCount).appendChild(div);
		    cellCount = cellCount + 1;
		    
			document.getElementById('select_graphic_options_div').style.display = '';
			break;
		}
		break;
	case 'BOWLERSPEED-OPTIONS':
	
		switch ($('#selected_broadcaster').val().toUpperCase()) {
		
		case 'DOAD_LLC':

			$('#select_graphic_options_div').empty();
	
			header_text = document.createElement('h6');
			header_text.innerHTML = 'Select Graphic Options';
			document.getElementById('select_graphic_options_div').appendChild(header_text);
			
			table = document.createElement('table');
			table.setAttribute('class', 'table table-bordered');
					
			tbody = document.createElement('tbody');
	
			table.appendChild(tbody);
			document.getElementById('select_graphic_options_div').appendChild(table);
			
			row = tbody.insertRow(tbody.rows.length);
			
			select = document.createElement('select');
			select.id = 'selectInnin';
			select.name = select.id;
			
			if(document.getElementById('selected_match_max_overs').value > 0) {
				max_cols = 2;
			} else {
				max_cols = 4;
			}
			for(var i=1; i<=max_cols; i++) {
				option = document.createElement('option');
				option.value = i;
			    option.text = 'Inning ' + i;
			    select.appendChild(option);
			    
			    if(session_match.match.inning[1].isCurrentInning.toUpperCase().includes('YES')){
					option.selected = true;	
				}
			}
			row.insertCell(cellCount).appendChild(select);
			cellCount = cellCount + 1;
			
			switch(whatToProcess){
			case 'BOWLERSPEED-OPTIONS':
			switch ($('#selected_broadcaster').val().toUpperCase()) {
		
			case 'DOAD_LLC':
				select.setAttribute('onchange',"processUserSelection(this)");

				select = document.createElement('select');
				select.id = 'selectPlayers';
				select.name = select.id;
				
				row.insertCell(cellCount).appendChild(select);
				cellCount = cellCount + 1;
				
				break;
			}
			
			break;
			
		}
		
		option = document.createElement('input');
	    option.type = 'button';
		    
			switch (whatToProcess) {
			case 'BOWLERSPEED-OPTIONS':
				option.name = 'populate_bowlerspeed_btn';
			    option.value = 'Populate BowlerSpeed';
				break
			}
		    option.id = option.name;
		    option.setAttribute('onclick',"processUserSelection(this)");
		    
		    div = document.createElement('div');
		    div.append(option);

			option = document.createElement('input');
			option.type = 'button';
			option.name = 'cancel_graphics_btn';
			option.id = option.name;
			option.value = 'Cancel';
			option.setAttribute('onclick','processUserSelection(this)');
	
		    div.append(option);
		    
		    row.insertCell(cellCount).appendChild(div);
		    cellCount = cellCount + 1;
		    
			document.getElementById('select_graphic_options_div').style.display = '';

			break;
		}
		break;
	case'NAMESUPER-OPTIONS': case 'NAMESUPER_PLAYER-OPTIONS':  case'PLAYERPROFILE-OPTIONS': case'L3PLAYERPROFILE-OPTIONS': case 'BUG_DB-OPTIONS':
	switch ($('#selected_broadcaster').val().toUpperCase()) {
		case 'BUKHATIR': case 'GPCL': case 'ACC': case 'NEPAL_T20': case 'ASSAM': case 'EVEREST_NEPAL_T20': case 'THAILAND': case 'DOAD_LLC': 
		case 'ICPL': case 'LCT': case 'FAIR_BREAK': case 'ACC_NEPAL': case 'APL': case 'PUNJAB_T20': case 'EVEREST_PUNJAB_T20': case 'MAHARAJA_T20': case 'EVEREST_APL_T20':
		case 'RPL': case 'RSWS':
			$('#select_graphic_options_div').empty();
	
			header_text = document.createElement('h6');
			header_text.innerHTML = 'Select Graphic Options';
			document.getElementById('select_graphic_options_div').appendChild(header_text);
			
			table = document.createElement('table');
			table.setAttribute('class', 'table table-bordered');
					
			tbody = document.createElement('tbody');
	
			table.appendChild(tbody);
			document.getElementById('select_graphic_options_div').appendChild(table);
			
			row = tbody.insertRow(tbody.rows.length);
			
			switch(whatToProcess){
			case'NAMESUPER-OPTIONS':
					switch ($('#selected_broadcaster').val().toUpperCase()) {
					case 'BUKHATIR': case 'GPCL': case 'ACC': case 'NEPAL_T20': case 'ASSAM': case 'EVEREST_NEPAL_T20': case 'THAILAND': case 'DOAD_LLC': 
					case 'ICPL': case 'LCT': case 'FAIR_BREAK': case 'ACC_NEPAL': case 'APL': case 'PUNJAB_T20': case 'EVEREST_PUNJAB_T20': case 'MAHARAJA_T20': 
					case 'EVEREST_APL_T20': case 'RPL': case 'RSWS':
						select = document.createElement('select');
						select.style = 'width:130px';
						select.id = 'selectNameSuper';
						select.name = select.id;
						
						dataToProcess.forEach(function(ns,index,arr1){
							option = document.createElement('option');
							option.value = ns.namesuperId;
							option.text = ns.prompt ;
							select.appendChild(option);
						});
						
						row.insertCell(cellCount).appendChild(select);
						cellCount = cellCount + 1;
						
						break;
					}
					break;
			case 'NAMESUPER_PLAYER-OPTIONS':
				switch ($('#selected_broadcaster').val().toUpperCase()) {
					case 'BUKHATIR': case 'GPCL': case 'ACC': case 'NEPAL_T20': case 'ASSAM': case 'EVEREST_NEPAL_T20': case 'THAILAND': case 'ICPL': 
					case 'LCT': case 'FAIR_BREAK': case 'ACC_NEPAL': case 'APL': case 'PUNJAB_T20': case 'EVEREST_PUNJAB_T20': case 'MAHARAJA_T20':
					case 'EVEREST_APL_T20': case 'RPL': case 'RSWS': 
						select = document.createElement('select');
						select.id = 'selectTeam';
						select.name = select.id;
						
						option = document.createElement('option');
						option.value = session_match.setup.homeTeamId;
						option.text = session_match.setup.homeTeam.teamName1;
						select.appendChild(option);
						
						option = document.createElement('option');
						option.value = session_match.setup.awayTeamId;
						option.text = session_match.setup.awayTeam.teamName1;
						select.appendChild(option);
					
						select.setAttribute('onchange',"processUserSelection(this)");
						row.insertCell(cellCount).appendChild(select);
						cellCount = cellCount + 1;
		
						select = document.createElement('select');
						select.style = 'width:100px';
						select.id = 'selectCaptainWicketKeeper';
						select.name = select.id;
						
						option = document.createElement('option');
						option.value = 'Player';
						option.text = 'Player';
						select.appendChild(option);
						
						option = document.createElement('option');
						option.value = 'Captain';
						option.text = 'Captain';
						select.appendChild(option);
						
						option = document.createElement('option');
						option.value = 'Captain-WicketKeeper';
						option.text = 'Captain-WicketKeeper';
						select.appendChild(option);
		
						option = document.createElement('option');
						option.value = 'Player Of The Match';
						option.text = 'Player Of The Match';
						select.appendChild(option);
						
						option = document.createElement('option');
						option.value = 'Wicket_Keeper';
						option.text = 'WicketKeeper';
						select.appendChild(option);
						
						option = document.createElement('option');
						option.value = 'Player Of The Tournament';
						option.text = 'Player Of The Tournament';
						select.appendChild(option);
						
						option = document.createElement('option');
						option.value = 'Player Of The Series';
						option.text = 'Player Of The Series';
						select.appendChild(option);
						
						select.setAttribute('onchange',"processUserSelection(this)");
						row.insertCell(cellCount).appendChild(select);
						cellCount = cellCount + 1;
						
						select = document.createElement('select');
						select.style = 'width:100px';
						select.id = 'selectPlayer';
						select.name = select.id;
						
						row.insertCell(cellCount).appendChild(select);
						cellCount = cellCount + 1;
						
						break;	
				}
				break;
			}
			
			option = document.createElement('input');
		    option.type = 'button';
			switch (whatToProcess) {
			case'NAMESUPER-OPTIONS':
			    option.name = 'populate_namesuper_btn';
			    option.value = 'Populate Namesuper';
				break;
			case 'NAMESUPER_PLAYER-OPTIONS':	
				option.name = 'populate_namesuper_player_btn';
			    option.value = 'Populate Namesuper-Player';
				break;
			}
		    option.id = option.name;
		    option.setAttribute('onclick',"processUserSelection(this)");
		    
		    div = document.createElement('div');
		    div.append(option);

			option = document.createElement('input');
			option.type = 'button';
			option.name = 'cancel_graphics_btn';
			option.id = option.name;
			option.value = 'Cancel';
			option.setAttribute('onclick','processUserSelection(this)');
	
		    div.append(option);
		    
		    row.insertCell(cellCount).appendChild(div);
		    cellCount = cellCount + 1;
		    
			document.getElementById('select_graphic_options_div').style.display = '';

			break;
		}
		break;
	}
	
	
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