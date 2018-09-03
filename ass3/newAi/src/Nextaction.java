
public class Nextaction {
	private int choice;
	public Nextaction(int choice) {
		this.choice = choice;
	}
	public char getAction() {
		char action = 0;
		switch(choice) {
		case 1:
			action = 'f';
			break;
		case 2:
		 	action = 'l';
			break;
		case 3:
		 	action = 'r';

			break;
		case 4:
			action = 'l';
			break;
		}

		return action;
	}
	
}
