package com.cricket.containers;

import java.util.List;

import com.cricket.model.BattingCard;
import com.cricket.model.BowlingCard;

public class Infobar {
	
	private boolean infobar_on_screen;
	private boolean powerplay_on_screen;
	private boolean bowler_last;
	private String directer_section;
	private String middle_section;
	private String full_section;
	private String last_full_section;
	private String bottom_right_top_section;
	private String bottom_right_bottom_section;
	private String bottom_right_section;
	private String top_section;
	private String last_middle_section;
	private String last_bottom_right_top_section;
	private String last_bottom_right_bottom_section;
	private String last_bottom_right_section;
	private String last_top_section;
	private String ident_section;
	private String last_ident_section;
	private String last_speed_value;
	private String last_x_ball_value;

	
	private String bottom_left_section;
	private String last_bottom_left_section;
	private String top_right_section;
	private String last_top_right_section;
	private String bottom_section;
	private String last_bottom_section;
	private int player_id;
	
	List<BattingCard> last_batsmen;
	
	BowlingCard last_bowler;
	
	public boolean isInfobar_on_screen() {
		return infobar_on_screen;
	}
	public void setInfobar_on_screen(boolean infobar_on_screen) {
		this.infobar_on_screen = infobar_on_screen;
	}
	public boolean isPowerplay_on_screen() {
		return powerplay_on_screen;
	}
	public void setPowerplay_on_screen(boolean powerplay_on_screen) {
		this.powerplay_on_screen = powerplay_on_screen;
	}
	public String getMiddle_section() {
		return middle_section;
	}
	public void setMiddle_section(String middle_section) {
		this.middle_section = middle_section;
	}
	public String getBottom_right_top_section() {
		return bottom_right_top_section;
	}
	public void setBottom_right_top_section(String bottom_right_top_section) {
		this.bottom_right_top_section = bottom_right_top_section;
	}
	public String getBottom_right_bottom_section() {
		return bottom_right_bottom_section;
	}
	public void setBottom_right_bottom_section(String bottom_right_bottom_section) {
		this.bottom_right_bottom_section = bottom_right_bottom_section;
	}
	public String getBottom_right_section() {
		return bottom_right_section;
	}
	public void setBottom_right_section(String bottom_right_section) {
		this.bottom_right_section = bottom_right_section;
	}
	public String getTop_section() {
		return top_section;
	}
	public void setTop_section(String top_section) {
		this.top_section = top_section;
	}
	public String getLast_middle_section() {
		return last_middle_section;
	}
	public void setLast_middle_section(String last_middle_section) {
		this.last_middle_section = last_middle_section;
	}
	public String getLast_bottom_right_top_section() {
		return last_bottom_right_top_section;
	}
	public void setLast_bottom_right_top_section(String last_bottom_right_top_section) {
		this.last_bottom_right_top_section = last_bottom_right_top_section;
	}
	public String getLast_bottom_right_bottom_section() {
		return last_bottom_right_bottom_section;
	}
	public void setLast_bottom_right_bottom_section(String last_bottom_right_bottom_section) {
		this.last_bottom_right_bottom_section = last_bottom_right_bottom_section;
	}
	public String getLast_bottom_right_section() {
		return last_bottom_right_section;
	}
	public void setLast_bottom_right_section(String last_bottom_right_section) {
		this.last_bottom_right_section = last_bottom_right_section;
	}
	public String getLast_top_section() {
		return last_top_section;
	}
	public void setLast_top_section(String last_top_section) {
		this.last_top_section = last_top_section;
	}
	public List<BattingCard> getLast_batsmen() {
		return last_batsmen;
	}
	public void setLast_batsmen(List<BattingCard> last_batsmen) {
		this.last_batsmen = last_batsmen;
	}
	public BowlingCard getLast_bowler() {
		return last_bowler;
	}
	public void setLast_bowler(BowlingCard last_bowler) {
		this.last_bowler = last_bowler;
	}
	public String getIdent_section() {
		return ident_section;
	}
	public void setIdent_section(String ident_section) {
		this.ident_section = ident_section;
	}
	public String getLast_ident_section() {
		return last_ident_section;
	}
	public void setLast_ident_section(String last_ident_section) {
		this.last_ident_section = last_ident_section;
	}
	public String getBottom_left_section() {
		return bottom_left_section;
	}
	public void setBottom_left_section(String bottom_left_section) {
		this.bottom_left_section = bottom_left_section;
	}
	public String getTop_right_section() {
		return top_right_section;
	}
	public void setTop_right_section(String top_right_section) {
		this.top_right_section = top_right_section;
	}
	public String getLast_bottom_left_section() {
		return last_bottom_left_section;
	}
	public void setLast_bottom_left_section(String last_bottom_left_section) {
		this.last_bottom_left_section = last_bottom_left_section;
	}
	public String getLast_top_right_section() {
		return last_top_right_section;
	}
	public void setLast_top_right_section(String last_top_right_section) {
		this.last_top_right_section = last_top_right_section;
	}
	public String getBottom_section() {
		return bottom_section;
	}
	public void setBottom_section(String bottom_section) {
		this.bottom_section = bottom_section;
	}
	public String getLast_bottom_section() {
		return last_bottom_section;
	}
	public void setLast_bottom_section(String last_bottom_section) {
		this.last_bottom_section = last_bottom_section;
	}
	public String getDirecter_section() {
		return directer_section;
	}
	public void setDirecter_section(String directer_section) {
		this.directer_section = directer_section;
	}
	public String getFull_section() {
		return full_section;
	}
	public void setFull_section(String full_section) {
		this.full_section = full_section;
	}
	public String getLast_full_section() {
		return last_full_section;
	}
	public void setLast_full_section(String last_full_section) {
		this.last_full_section = last_full_section;
	}
	public String getLast_speed_value() {
		return last_speed_value;
	}
	public void setLast_speed_value(String last_speed_value) {
		this.last_speed_value = last_speed_value;
	}
	public int getPlayer_id() {
		return player_id;
	}
	public void setPlayer_id(int player_id) {
		this.player_id = player_id;
	}
	public String getLast_x_ball_value() {
		return last_x_ball_value;
	}
	public void setLast_x_ball_value(String last_x_ball_value) {
		this.last_x_ball_value = last_x_ball_value;
	}
	public boolean isBowler_last() {
		return bowler_last;
	}
	public void setBowler_last(boolean bowler_last) {
		this.bowler_last = bowler_last;
	}
	
}
