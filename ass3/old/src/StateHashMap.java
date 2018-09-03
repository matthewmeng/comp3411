import java.util.HashMap;
import java.util.Map;

public class StateHashMap {
	private Map<String,State> visitedMap = new HashMap<String,State>();
	
	public StateHashMap(){
		
	}
	
	public boolean add(State state) {
		if(!visitedMap.containsKey(state.getInfo())) {
			visitedMap.put(state.getInfo(),state);
			//System.out.println(visitedView.info);
			return true;
		}else {
			return false;
		}
	}
	
	public void delete(State state) {
		if(visitedMap.containsKey(state.getInfo())) {
			visitedMap.remove(state.getInfo());
		}
	}
	

}
