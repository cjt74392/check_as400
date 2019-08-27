//Nagios Plugin to check an IBM AS/400
//
//Developed September 2004 
//Last Modified Jul 4 2013
//
//This class is used as a varible structure
//holding language conversions for parsing tokens 
//
//Definitions for the GERMAN language
//Thanks to Boer, Andr, Stefan Hlustik

public class check_as400_lang{
	//These constants are referenced during parsing so
	//that the correct phrases are found.

	//This is found at the bottom when you type dspjob (name of a job 
	//that exists)
	public String SELECTION="Auswahl";

		//This is the status of job or sbs when you type dspjob or dspsbsd
	public String ACTIVE="ACTIVA";
	
		//This is the "DB Capability" dsplay when you type wrksyssts
	public String DB_CAPABILITY="higkeit";
	
		//This is le display for the login screen
	public String LOGIN_SCREEN="System  . . . . .";

	//Run dspmsg and it will display "No messages available" if there are no 
	//messages
	public String NO_MESSAGES_AVAILABLE="Keine Nachrichten verf";
  public String NO_ENTRIES_EXIST="Keine Eintrage vorhanden sind in der aktuellen Version von log"; //CPF2447 No entries exist in current version of log
	//The "password has expired"/"password expires" messages are the messages
	//you get when you login with an account which has an expired/will expire
	//password.
	public String PASSWORD_HAS_EXPIRED="um die Anmeldeanfrage";
	public String PASSWORD_EXPIRES="Vorheriges Anmelden";

	//The "Display Messages" is what you get after logging into an account
	//which displays any messages before continuing to the menu.
	public String DISPLAY_MESSAGES="Display Messages";

	//Run wrkoutq blah* and it will say "(No output queues)"
	public String NO_OUTPUT_QUEUES="Keine Ausgabewarteschlangen";

	//If you type dspsbsd blah it will say "...not found..."
	public String NOT_FOUND="nicht gefunden";

	//If you type dspjob QINTER, it should complain that there are duplicate 
	//jobs and print at the bottom of the window "duplicate jobs found" 
	public String DUPLICATE="Mehrfach vorhandene";

	//if you type dspjob blah, it will respond Job //blah not found
	//Only put the Job // part.
	public String JOB="Job //";

	//If try and execute a command that you are not allowed it will say
	//"library *LIBL not allowed"
	public String LIBRARY_NOT_ALLOWED="Bibliothek *LIBL nicht gefunden";

	//On a login with an expired password we look for "Exit sign-on" on the 
	//screen before we send the F3 to exit and disconnect. 
	public String EXIT_SIGNON="Verlassen";
	
	//If you type WRKACTJOB it may respond "No active jobs to display"
	//when there is no job like searched for in the sytem
	public String NO_JOB_TO_DISPLAY="Keine aktiven Jobs anzuzeigen";

  //Messages needing a reply OR Messages not needing a reply   Nachrichten, fur die enie/kenie Antwort erforderlich ist.
	public String MSG_NEED_REPLY="die eine Antwort erf";
	public String MSG_NOT_NEED_REPLY="die keine Antwort erfor";
	
	//WRKDSKSTS The "Request/Compression/Bottom" message.
	public String REQUEST_WORD="Anford";
	public String DSK_STS_COMPRESSION="Verdichtung";
	public String LIST_END="Ende";
};
