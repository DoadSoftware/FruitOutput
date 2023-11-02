package com.cricket.containers;

import java.util.List;

public class Bug {
	
	private String options_to_process;
	
	private String stat_option;
	private int inning_number;
	private int player_id;
	
	private List<String> stats_text;
	private String header_text;
	private String subheader_text;
	
	public Bug() {
		super();
	}
	public String getOptions_to_process() {
		return options_to_process;
	}
	public void setOptions_to_process(String options_to_process) {
		this.options_to_process = options_to_process;
	}
	public String getStat_option() {
		return stat_option;
	}
	public void setStat_option(String stat_option) {
		this.stat_option = stat_option;
	}
	public int getInning_number() {
		return inning_number;
	}
	public void setInning_number(int inning_number) {
		this.inning_number = inning_number;
	}
	public int getPlayer_id() {
		return player_id;
	}
	public void setPlayer_id(int player_id) {
		this.player_id = player_id;
	}
	public String getHeader_text() {
		return header_text;
	}
	public void setHeader_text(String header_text) {
		this.header_text = header_text;
	}
	public String getSubheader_text() {
		return subheader_text;
	}
	public void setSubheader_text(String subheader_text) {
		this.subheader_text = subheader_text;
	}
	public List<String> getStats_text() {
		return stats_text;
	}
	public void setStats_text(List<String> stats_text) {
		this.stats_text = stats_text;
	}
}
