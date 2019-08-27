//Nagios Plugin to check an IBM AS/400
//
//Developed September 2004 
//Last Modified June 2013
//
//This class is used as a varible structure
//holding language conversions for parsing tokens 
//
//Definitions for the ITALIAN language
//provided by Morandotti
//

public class check_as400_lang{
	//These constants are referenced during parsing so
	//that the correct phrases are found.

	//This is found at the bottom when you type dspjob (name of a job 
	//that exists)
	public String SELECTION="Selezione";
	
	//This is found at the end of lists
	public String LIST_END="Fine";

	//Run dspmsg and it will display "No messages available" if there are no 
	//messages
	public String NO_MESSAGES_AVAILABLE="Nessun messaggio disponibile";
  public String NO_ENTRIES_EXIST="Nessun esistono voci nella versione corrente del registro";  //CPF2447 No entries exist in current version of log ????
	//The "password has expired"/"password expires" messages are the messages
	//you get when you login with an account which has an expired/will expire
	//password.
	public String PASSWORD_HAS_EXPIRED="Password scaduta";
	public String PASSWORD_EXPIRES="Scadenza della password";

	//The "Display Messages" is what you get after logging into an account
	//which displays any messages before continuing to the menu.
	public String DISPLAY_MESSAGES="Display Messages";
	
	//This is the status of job or sbs when you type dspjob or dspsbsd
	public String ACTIVE="ATTIVO";
	
	//This is the "DB Capability" dsplay when you type wrksyssts
	public String DB_CAPABILITY="capacit\u00E0 DB";
	
	//This is le display for the login screen
	public String LOGIN_SCREEN="Sistema . . . . .";

	//Run wrkoutq blah* and it will say "(No output queues)"
	public String NO_OUTPUT_QUEUES="Non ci sono code di emissione";

	//If you type dspsbsd blah it will say "...not found..."
	public String NOT_FOUND="Non trovato";
	//If you type dspjob QINTER, it should complain that there are duplicate 
	//jobs and print at the bottom of the window "duplicate jobs found" 
	public String DUPLICATE="Sono stati trovati dei lavori duplicati";

	//if you type dspjob blah, it will respond Job //blah not found
	//Only put the Job // part.
	public String JOB="lavoro //";

	//If try and execute a command that you are not allowed it will say
	//"library *LIBL not allowed"
	public String LIBRARY_NOT_ALLOWED="libreria *LIBL non trovato";

	//On a login with an expired password we look for "Exit sign-on" on the 
	//screen before we send the F3 to exit and disconnect. 
	public String EXIT_SIGNON="Exit sign-on request";
	
	//If you type WRKACTJOB it may respond "No active jobs to display"
	//when there is no job like searched for in the sytem
	public String NO_JOB_TO_DISPLAY="Nessun lavoro attivo da visualizzare";
	
	//Messages needing a reply OR Messages not needing a reply   Nachrichten, fuer die eine/keine Antwort erforderlich ist.
	public String MSG_NEED_REPLY="Messaggi che richiedono una risposta";
	public int MSG_NEED_REPLY_OFFSET=75;
	public String MSG_NOT_NEED_REPLY="Messaggi che non richiedono risposta";
	
	//WRKDSKSTS The "Request" message.
	public String REQUEST_WORD="Ric";
	public String DSK_STS_COMPRESSION="Compressione";
};