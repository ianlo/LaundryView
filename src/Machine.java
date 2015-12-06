
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
	
	//Time remaining in minutes.
	private int timeRemaining;
	/**
	 * Creates a new laundry machine.
	 * @param type one of the type constants given by this class (i.e. WASHER, DRYER).
	 * @param status one of the status constants given by this class (i.e. AVAILABLE, RUNNING, etc.).
	 */
	public Machine(int type, int status){
		this.type = type;
		this.status = status;
	}
	
	//TODO add methods for changing the status and setting the time remaining on the machine.
	
}
