
public class Machine {
	//Machine states.
	public static final int AVAILABLE = 0;
	public static final int RUNNING = 1;
	public static final int ENDED = 2;
	public static final int OUTOFSERVICE = 3;
	
	//Dryer or Washer.
	public static final int DRYER = 0;
	public static final int WASHER = 1;
	
	//Local fields.
	private int type;
	private int status;
	private int number;
	
	//Time remaining in minutes.
	private int timeRemaining;
	/**
	 * Creates a new laundry machine.
	 * @param type one of the type constants given by this class (i.e. WASHER, DRYER).
	 * @param status one of the status constants given by this class (i.e. AVAILABLE, RUNNING, etc.).
	 * @param number the laundry machine number that is written on the machine.
	 */
	public Machine(int type,  int number){
		this.type = type;
		this.status = AVAILABLE;
		this.number = number;
	}
	
	public int getType() {
		return type;
	}
	
	public int getStatus() {
		return status;
	}
	
	public void setStatus(int status) {
		this.status = status;
		if (status != RUNNING) timeRemaining = 0;
	}
	
	public int getTimeRemaining() {
		return timeRemaining;
	}
	
	//When you set the time remaining, it automatically sets the machine to running.
	public void setTimeRemaining(int timeRemaining) {
		this.timeRemaining = timeRemaining;
		this.status = RUNNING;
	}
	
	public int getNumber() {
		return number;
	}
		
}
