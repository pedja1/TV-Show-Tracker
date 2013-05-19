package rs.pedjaapps.tvshowtracker;

public class AgendaSection implements Agenda{

	private String airs;
	
	public AgendaSection(String airs){
		this.airs = airs;
	}
	
	public String getAirs(){
		return airs;
	}
	
	@Override
	public boolean isSection() {
		// TODO Auto-generated method stub
		return true;
	}

}
