package rs.pedjaapps.tvshowtracker.model;

public class AgendaSection implements Agenda{

	private String showName;
	
	public AgendaSection(String showName){
		this.showName = showName;
	}
	
	public String getShowName(){
		return showName;
	}
	
	@Override
	public boolean isSection() {
		// TODO Auto-generated method stub
		return true;
	}

}
