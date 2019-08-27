/*----------------------------------
Nagios Plugin to check an IBM System i (AS/400)

Developed June 2003

After Ver 0.18,  Modified by Shao-Pin, Cheng since 2010/06/31
------------------------------------

CHANGE LOG:

0.18.01
*Fix check Expired password
*Added check DB utilization

0.18.02
*Added check QCMN JOB Transaction timeout. (Only check for CUB customize CLP result ) 
*Added debug check logout "Job ending immediately."
*Added check DISK Status.

0.18.03
*Modified the DSPMSG, see the last message and the number of message needing a reply.

0.18.04
*Added check single ASP used. 
*Added check CPUC, when use  Dynamic hardware  resource, CPU load may need consider Current processing capacity .

0.18.05.03
* Fix some German language definitions, thank Stefan Hlustik

0.18.06
* Fix ASP reach threshold cause monitor data error.
* Added check MIMIX Unprocessed Entry Count.  (Note: You should add MIMIX lib  to LIBL.)

0.18.07
* Fix V6R1 JOB DB US DBFault check

1.0.0
* Official version 1.0.0 release.
* Fix DSPMSG get null value and correct the message count.

1.0.1
* Added CPU CPUC JOBS ASP MIMIX performance data warning and critical value.

1.1.1
* Added check number of jobs in JOBQ.

1.2.0
* Added check Job Status.
* MIMIX Unprocessed Entry Count add check transfer definition and RJ link state.

1.2.4
* Added French language definitions.
* Fixed check CJ command && parse error.
* Fixed check OUTQ error with V6R1/V7R1. And can specify the library now.

1.2.5                                     
* Added check for iCluster Node Status
* Added check for iCluster Group Status
* Added check for iCluster Switch Readines  (Thanks, Mark Watts)

1.2.6                                     
* Added check for top CPU used job.

1.2.7.2                                     
* Added check for work with problems.
* Fixed French language check DISK halt. 1.2.7.1
* Fixed iCluster parse String lost define

1.2.8
* Added check for Number of file members.

1.2.9
* Fixed check MSG for LANG FR/GE string index out of range error.

1.3.1
* Modified check CJ for duplicate jobs, can specify job number for CRITICAL and WARNING value.

1.3.2
* Modified Rocket iCluster checks to recognize Nagios user Not Authorized to iCluster

1.4.2
* Added SSL connection option.
* Fixed user name display in Login ERROR message. 
* Fixed Log in failure on 10 character username.

1.4.3
* Added ITALIAN language. (Thanks to Riccardo Morandotti)
* Fixed CJS status

1.4.4
* Fixed CPUT problem on OS V7R2.
* Added check job temporary storage used. (Thanks, BIANCHI Xavier)

1.4.5
* Fixed ASP problem on OS V7R2 (If your OS is V6, you need to change source code. find string "parseWrkAspBrm". *V5R3 +128)

1.4.6
* Fixed ASP CPU JOB problem on OS V7R2
* TLS 1.2 support (If your java version lower then 1.8, you need uncomment sslSocket.setEnabledProtocols(new String[] {"TLSv1.2"}); )10/30/18

1.4.7
* Added CUB check NTDCRAMT (Only for CUB)

1.5.1
* Fixed WRKSYSSTS check problem on V7R3

1.5.2
* Added check for specific messages on log (CKMSG)(2019/08/21 Thanks, j. howell)
* Added check for temp and perm address use (CKADDR) (2019/08/21Thanks, j. howell)
* Added some debug output
--------------------------------------------------------------
Last Modified  2019/06/11 by Shao-Pin, Cheng  , Taipei, Taiwan
Mail & PayPal donate: cjt74392@ms10.hinet.net
--------------------------------------------------------------*/


import java.io.*;
import java.net.*;
import java.text.*;
import java.util.Date;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class check_as400{
	final static String VERSION="1.5.2";

	public static void printUsage(){
		System.out.println("Usage: check_as400 -H host -u user -p pass [-v var] [-w warn] [-c critical]\n");
		System.out.println("    (-h) for detailed help");
		System.out.println("    (-V) for version information\n");
	}
	
	public static void printUsageDetail(){
		System.out.println("Check_as400 Version: "+VERSION);
		System.out.println("Usage: check_as400 -H host -u user -p pass [-v var] [-w warn] [-c critical]\n");
		System.out.println("Options:\n");
		System.out.println("-H HOST\n   Name of the host to check");
		System.out.println("-u USERNAME\n   Username to login to host");
		System.out.println("-p PASSWORD\n   Password to login to host");
		System.out.println("-v STRING\n   Variable to check.  Valid variables include:");
		System.out.println("      AJ                = Number of active jobs in system.");
		System.out.println("      CJ <job> [-w -c]  = Check to see if job <job> is in the system.[Number of ACTIVE <job>]");
		System.out.println("      CJS <sbs> <job> [status <STATUS>] [noperm]");
		System.out.println("                        = Check to see if job is existing in Subsystem and has this status.");
		System.out.println("                          Job checking can be controlled by :");
		System.out.println("                          status <status>	= any other status goes to critical");
		System.out.println("                          noperm 		= don't go to critical if job is not in the system");
	  //System.out.println("                          onlyone		= go to critical if job is shown twice otherwise ignore it");
		System.out.println("                          NOTE: if JobStatus is set, it has highest Priority");
		System.out.println("      JOBS              = Number of jobs in system.");
		System.out.println("      CJM <job>         = Check the temporary storage used of job.");
		System.out.println("      JOBQ <lib/jobq>   = Number of jobs in JOBQ.");
		System.out.println("      CPU               = CPU load.");
		System.out.println("      CPUC <cpuBase>    = CPU load, Consider Current processing capacity. (CPU used * VP nums / cpuBase).");
		System.out.println("                          NOTE: Specify <cpuBase>, EX: You want use 3 CPU only, but VP use more than 3.");
		System.out.println("      CPUT <job>        = Top CPU used job. The total processing unit time used by the job");
		System.out.println("                          Specify job name, ex: *ALL or QZ* or QZDASOINIT");	
		System.out.println("      US                = Percent free storage");
		System.out.println("      ASP <aspNum>      = Check ASP <aspNum> used");
		System.out.println("      DISK              = Check DISK Status.");
		System.out.println("      DB                = DB utilization. (Not available after V6R1)");
		System.out.println("      DBFault           = Pool DB/Non-DB Fault");
		System.out.println("      LOGIN             = Check if login completes.");
		System.out.println("      MSG <user>        = Check for any unanswered messages on msg queue <user>");
		System.out.println("                          Any unanswered messages causes warning status.");
		System.out.println("      CKMSG <starttime> <MSGID> [<text string>]");
		System.out.println("                        = Look for existense of MSGID in the messages since <starttime>");
		System.out.println("                          starttime can be either hh:mm:ss or epoch time.");
		System.out.println("                          MSGID is the message identifier.");
		System.out.println("                          Optionally a text string associated with the MSGID can be specified.");
		System.out.println("                          Returns CRITICAL if message found.");
		System.out.println("      CKADDR [TEMP | PERM]");
    System.out.println("                        = Checks temporary or permanent address use.");
		System.out.println("      OUTQ <queue>      = Check outq files, writer and status. No writer, or");
		System.out.println("                          status of 'HLD' causes warning status. This default");
		System.out.println("                          behavior can be modified with the following options:");
		System.out.println("                             nw    = Don't go critical when no writer");
		System.out.println("                             ns    = Don't warn if status is 'HLD'");
		System.out.println("                             nf    = Ignore number of files in queue");
		System.out.println("                          NOTE: threshold values are used on number of files");
		System.out.println("      SBS <subsystem>   = Check if the subsystem <subsystem> is running.");
		System.out.println("                          NOTE: specify <subsystem> as library/subsystem");
		System.out.println("      PRB               = Check if the problem was identified.(Need *WRKPRB ANZPRB/CHGPRB/DLTPRB/SNDSRVRQS auth)");
		System.out.println("      FDN               = Number of file members; specify library/filename ");
		System.out.println("      ---------- VISION MIMIX ----------");
		System.out.println("      MIMIX <DG name>   = Check MIMIX Data Group Unprocessed Entry Count, Transfer definition, RJ link state.");
		System.out.println("      ---------- Rocket iCluster ----------");
		System.out.println("      ICNODE            = Check for any Inactive or Failed Node status.");
    System.out.println("      ICGROUP           = Check for any Inactive or Indoubt Group status.");
    System.out.println("      ICSWTCHRDY <grp>  = Check for multiple conditions for switch readiness.");
    //System.out.println("      ---------- CUB check CLP ----------");
  	//System.out.println("      CMD              = Check Disk I/O, Transaction count....(Only for CUB)");
        System.out.println("                                                                                   ");
		System.out.println("-h\n   Print this help screen");
		System.out.println("-V\n   Print version information");
		System.out.println("-SSL\n   Use SSL connection\n       NOTE: Need to be one of the first arguments to work");
		System.out.println("                          This option must import CE to Java first, EX:");
		System.out.println("                          keytool -import -trustcacerts -keystore /.../security/cacerts -storepass changeit -noprompt -alias xxx -file XXX.cer");
		System.out.println("-d\n   Be verbose (debug)\n       NOTE: Needs to be one of the first arguments to work");
		System.out.println("-D\n   Be verbose and dump screen outputs (debug)");
		System.out.println("       NOTES: Needs to be one of the first arguments to work");
		System.out.println("             When things are not working, use this flag, redirect the output to a file and send it to me!");
		System.out.println("\nNotes:\n -CPU, DB and US threshold's are decimal, JOBS, JOBQ and OUTQ ... are integers.\n");
	}

	public static void parseCmdLineArgs(String [] args){
		int i=0;
		int READY_FLAG=127,CRIT_FLAG=64,WARN_FLAG=32,HOST_FLAG=16,USER_FLAG=8,PASS_FLAG=4,CMD_FLAG=2,ARG_FLAG=1;
		int flag=0;

		try{
			while(flag!=READY_FLAG){
				if(args[i].equals("-H")){
					ARGS.hostName=args[++i];
					flag=flag | HOST_FLAG;
				}
				else if(args[i].equals("-u")){
					ARGS.userName=args[++i];
					flag=flag | USER_FLAG;
				}
				else if(args[i].equals("-p")){
					ARGS.passWord=args[++i];
					flag=flag | PASS_FLAG;
				}
				else if(args[i].equals("-d")){
					ARGS.DEBUG=true;
				}
				else if(args[i].equals("-D")){
					ARGS.DEBUG=ARGS.DEBUG_PLUS=true;
				}
				else if(args[i].equals("-SSL")){
					ARGS.SSL=true;
				}
				else if(args[i].equals("-w")){
					ARGS.tHoldWarn=(new Double(args[++i])).doubleValue();
					flag=flag | WARN_FLAG;
				}
				else if(args[i].equals("-c")){
					ARGS.tHoldCritical=(new Double(args[++i])).doubleValue();
					flag=flag | CRIT_FLAG;
				}
				else if(args[i].equals("-h")){
					printUsageDetail();
					System.exit(0);
				}
				else if(args[i].equals("-V")){
					System.out.println("");
					System.out.println("Check_as400 Version: "+VERSION);
					System.out.println("---------------------------");		
					System.out.println("Check_as400 is Nagios Plugin to check IBM System i. \n");
					System.out.println("Feedback and help");		
					System.out.println("-----------------");					
					System.out.println("Shao-Pin Cheng, Taipei, Taiwan");
					System.out.println("Mail & PayPal donate: cjt74392@ms10.hinet.net");
					System.out.println("");
					System.exit(0);
				}
				else if(args[i].equals("-v")){
					if(args[++i].equals("CPU")){
						ARGS.command=WRKSYSSTS;
						ARGS.checkVariable=CPU;
						flag=flag | CMD_FLAG | ARG_FLAG;
					}
					else if(args[i].equals("CPUC")){
						ARGS.command=WRKSYSACT;
						ARGS.checkVariable=CPUC;
						ARGS.cpuNum=args[++i];
						flag=flag | CMD_FLAG | ARG_FLAG;
					}
					else if(args[i].equals("CPUT")){
						ARGS.command=TOPCPUJOB;
						ARGS.checkVariable=CPUT;
						++i;
						if (args[i].equals("-w")||args[i].equals("-c")){
							ARGS.job="*ALL";
							i--;
						}
						else {
						ARGS.job=args[i];
					  }
						flag=flag | CMD_FLAG | ARG_FLAG;
					}
					else if(args[i].equals("DB")){
						ARGS.command=WRKSYSSTS;
						ARGS.checkVariable=DB;
						flag=flag | CMD_FLAG | ARG_FLAG;
					}
			    		else if(args[i].equals("FDN")){
       						ARGS.command=DSPFD;
       						ARGS.checkVariable=FDN;
       						ARGS.fdFile=args[++i];
       						flag=flag | CMD_FLAG | ARG_FLAG;
     					}
					else if(args[i].equals("US")){
						ARGS.command=WRKSYSSTS;
						ARGS.checkVariable=US;
						flag=flag | CMD_FLAG | ARG_FLAG;
					}
					else if(args[i].equals("DBFault")){
						ARGS.command=WRKSYSSTS;
						ARGS.checkVariable=DBFault;
						flag=flag | CMD_FLAG | ARG_FLAG;
					}
					else if(args[i].equals("CMD")){
						ARGS.command=CMDCLP;
						ARGS.checkVariable=CMD;
						ARGS.cmdCL=args[++i];
						++i;
						while(true){
							if(args[i].equals("XXPARM")){
								ARGS.cmd_parm=args[i];
								ARGS.outQFlags=ARGS.outQFlags | CMDPARM_FLAG;
								i++;
							}
							else{
								i--;
								break;
							}
						}
						flag=flag | CMD_FLAG | ARG_FLAG;
					}					
					else if(args[i].equals("DTAQD")){
						ARGS.command=DSPDTAQD;
						ARGS.checkVariable=DTAQD;
						ARGS.dtaqdName=args[++i];
						flag=flag | CMD_FLAG | ARG_FLAG;
					}
					else if(args[i].equals("DISK")){
						ARGS.command=WRKDSKSTS;
						ARGS.checkVariable=DISK;
						flag=flag | CMD_FLAG | ARG_FLAG | WARN_FLAG | CRIT_FLAG;
					}
					else if(args[i].equals("ASP")){
						ARGS.command=WRKASPBRM;
						ARGS.checkVariable=ASP;
						ARGS.aspNums=args[++i];
						flag=flag | CMD_FLAG | ARG_FLAG;
					}
					else if(args[i].equals("PRB")){
						ARGS.command=WRKPRB;
						ARGS.checkVariable=PRB;
						flag=flag | CMD_FLAG | ARG_FLAG | WARN_FLAG | CRIT_FLAG;
					}
					else if(args[i].equals("MIMIX")){
						ARGS.command=DSPDGSTS;
						ARGS.checkVariable=MIMIX;
						ARGS.dgDef=args[++i];
						flag=flag | CMD_FLAG | ARG_FLAG;
					}
					else if(args[i].equals("AJ")){
						ARGS.command=WRKACTJOB;
						ARGS.checkVariable=AJOBS;
						flag=flag | CMD_FLAG | ARG_FLAG;
					}					
					else if(args[i].equals("JOBS")){
						ARGS.command=WRKSYSSTS;
						ARGS.checkVariable=JOBS;
						flag=flag | CMD_FLAG | ARG_FLAG;
					}
					else if(args[i].equals("JOBQ")){
						ARGS.command=WRKJOBQ;
						ARGS.checkVariable=JOBQ;
						ARGS.jobQ=args[++i];
						flag=flag | CMD_FLAG | ARG_FLAG;
					}
					else if(args[i].equals("SBS")){
						ARGS.command=DSPSBSD;
						ARGS.checkVariable=SBS;
						ARGS.subSystem=args[++i];
						flag=flag | CMD_FLAG | ARG_FLAG | WARN_FLAG | CRIT_FLAG;
					}
					else if(args[i].equals("CJ")){
						ARGS.command=DSPJOB;
						ARGS.checkVariable=DJOB;
						ARGS.job=args[++i];
						flag=flag | CMD_FLAG | ARG_FLAG;
						++i;
							while(true){
								//In this case all further addition parameters are optional
								if (i==args.length){
									flag=flag | WARN_FLAG | CRIT_FLAG; // Parameter completely set
									break;
								}
								else{
									i--;
									break;
								}
							}
					}
          else if(args[i].equals("CJM")){
						ARGS.command=DSPJOBM;
						ARGS.checkVariable=DJOBM;
						ARGS.job=args[++i];
						flag=flag | CMD_FLAG | ARG_FLAG;
					}
					else if(args[i].equals("LOGIN")){
						ARGS.command=CMDLOGIN;
						flag=flag | CMD_FLAG | ARG_FLAG | WARN_FLAG | CRIT_FLAG;
					}
					else if(args[i].equals("MSG")){
						ARGS.command=DSPMSG;
						ARGS.checkVariable=MSG;
						ARGS.msgUser=args[++i];
						flag=flag | CMD_FLAG | ARG_FLAG | WARN_FLAG | CRIT_FLAG;
					}
					else if(args[i].equals("ICNODE")){
						ARGS.command=DMWRKNODE;
						ARGS.checkVariable=ICNODE;
						flag=flag | CMD_FLAG | ARG_FLAG | WARN_FLAG | CRIT_FLAG;
					}
					else if(args[i].equals("ICGROUP")){
						ARGS.command=DMWRKGRP;
						ARGS.checkVariable=ICGROUP;
						flag=flag | CMD_FLAG | ARG_FLAG | WARN_FLAG | CRIT_FLAG;
					}                    
					else if(args[i].equals("ICSWTCHRDY")){
						ARGS.command=DMSWTCHRDY;
						ARGS.checkVariable=ICSWRDY;
						ARGS.icGroup=args[++i];
						flag=flag | CMD_FLAG | ARG_FLAG | WARN_FLAG | CRIT_FLAG;
					}
					else if(args[i].equals("OUTQ")){
						ARGS.command=WRKOUTQ;
						ARGS.checkVariable=OUTQ;
						ARGS.outQ=args[++i];
						/*nw    = Don't warn when no writer
						ns    = Don't warn if status is 'HLD'
						nf    = Ignore number of files in queue*/
						++i;
						while(true){
							if(args[i].equals("nw")){
								ARGS.outQFlags=ARGS.outQFlags | OUTQ_NW;
								i++;
							}
							else if(args[i].equals("ns")){
								ARGS.outQFlags=ARGS.outQFlags | OUTQ_NS;
								i++;
							}
							else if(args[i].equals("nf")){
								ARGS.outQFlags=ARGS.outQFlags | OUTQ_NF;
								i++;
								flag=flag | WARN_FLAG | CRIT_FLAG;
							}
							else{
								i--;
								break;
							}
							
						}
						flag=flag | CMD_FLAG | ARG_FLAG;
					}
					else if(args[i].equals("CJS")){
							ARGS.command=CHKJOBSTS;
							ARGS.checkVariable=JOBSTS;
							ARGS.subSystem=args[++i];
							ARGS.job=args[++i];
												
							++i;
							while(true){
								//In this case all further addition parameters are optional
								if (i==args.length){
									flag=flag | WARN_FLAG | CRIT_FLAG; // Parameter completely set
									break;
								}
								else if(args[i].equals("status")){
									ARGS.chk_status=args[++i];
									ARGS.JobFlags=ARGS.JobFlags | JOBSTS_STATUS;
									i++;
								}
								else if(args[i].equals("noperm")){
									ARGS.JobFlags=ARGS.JobFlags | JOBSTS_NOPERM;
									i++;
								}
								else if(args[i].equals("onlyone")){
									ARGS.JobFlags=ARGS.JobFlags | JOBSTS_ONLYONE;
									i++;
								}
								else{
									i--;
									break;
								}
							}
							flag=flag | CMD_FLAG | ARG_FLAG;
					}
          /* added jwh - find specific msgid in msgqueue */
					else if(args[i].equals("CKMSG")){
			      ARGS.command=DSPLOG;
						ARGS.checkVariable=CKMSG;
						ARGS.startTime=parseCKMSGtime(args[++i]);
						ARGS.msgID=args[++i];
						flag=flag | CMD_FLAG | ARG_FLAG | WARN_FLAG | CRIT_FLAG;
						/* optionally can provide search text */
						ARGS.srchText="";
            int extra=args.length-1;
            while (i<extra) {
					        	ARGS.srchText = ARGS.srchText+args[++i]+" ";
						}
					}
          /* added jwh - check temp or perm address use */
          else if(args[i].equals("CKADDR")){
            ARGS.command=WRKSYSSTS;
            ARGS.checkVariable=CKADDR;
            ARGS.addrtype=args[++i];
            flag=flag | CMD_FLAG | ARG_FLAG;
          }
				}
				else{
					System.out.println("Unknown option ["+args[i]+"]");
					System.exit(WARN);
				}
				i++;
			}
			
			if(ARGS.checkVariable==US || ARGS.checkVariable==DJOB){
				if(ARGS.tHoldWarn<ARGS.tHoldCritical){
					System.out.println("Warning threshold should be greater than the Critical threshold.");
					System.exit(WARN);
				}
			}
			else if(ARGS.tHoldWarn>ARGS.tHoldCritical){
				System.out.println("Warning threshold should be less than the Critical threshold.");
				System.exit(WARN);
			}
		}
		catch(Exception e){
			printUsage();
	 /* e.printStackTrace();
      System.out.println(e.toString()); */
			System.exit(WARN);
		}
	}

	public static void main(String [] args){
		ARGS=new check_as400_cmd_vars();
		LANG=new check_as400_lang();
		
		parseCmdLineArgs(args);
		
		//establish connection to server
		if(ARGS.DEBUG) System.out.print("Establishing connection to server...");
		if(open()){		
			if(ARGS.DEBUG) System.out.println("done.\nLogging in...");
			//login
			if(login()){
				if(ARGS.DEBUG) System.out.println("Login completed.\nSending command ("+ARGS.command+")...");
				//send command
				String result=runCommand();
				if(result!=null){
					if(ARGS.DEBUG) System.out.println("Command sent.\nParsing results...");
					//parse and disconnect from server
					logout(parse(result));
					if(ARGS.DEBUG) System.out.println("Finished.");
				}
				else{
					System.out.println("CRITICAL - Unexpected output on command");
					logout(CRITICAL);
				}
			}
			else{
				System.out.println("CRITICAL - Unexpected output on login");
				logout(CRITICAL);
			}
		}
		else
			System.exit(CRITICAL);
		
	}
	
	public static String runCommand(){
		
		/*send proper command*/
		switch(ARGS.command){
			case WRKSYSSTS:
				send("wrksyssts astlvl(*intermed)\r");
				/*Wait and recieve output screen*/
				return waitReceive("===>",NONE);
			case WRKOUTQ:
				send("wrkoutq "+ARGS.outQ.toLowerCase()+"*\r");
				/*Wait and recieve output screen*/
				return waitReceive("===>",GETOUTQ);
			case DSPMSG:
			  send("CHGVTMAP DOWN(*CTLD *CTLF *NXTSCR *ESCZ)\r");
				waitReceive("F3=",NONE);
				//send("dspmsg msgq("+ARGS.msgUser+") msgtype(*INQ) astlvl(*intermed)\r");
				send("dspmsg "+ARGS.msgUser+" astlvl(*basic)\r");
				/*Wait and recieve output screen*/
				return waitReceive("F3=",NONE);     
			case DSPLOG:  /* added jwh - look for specific msgid in msgqueue */
				send("CHGVTMAP DOWN(*CTLD *CTLF *NXTSCR *ESCZ)\r");
				waitReceive("F3=",NONE);
				send("dsplog period(('"+ARGS.startTime+"')) msgid("+ARGS.msgID+")\r");
				return waitReceive("F3=",NONE);
			case DSPSBSD:
				send("dspsbsd sbsd("+ARGS.subSystem+")\r");
				/*Wait and recieve output screen*/
				return waitReceive("===>",GETSBSD);
			case DSPJOB:
				send("CHGVTMAP DOWN(*CTLD *CTLF *NXTSCR *ESCZ)\r");
				waitReceive("F3=",NONE);
				send("dspjob "+ARGS.job+"\r");
				/*Wait and recieve output screen*/
				waitReceive(LANG.SELECTION,GETJOB);
				send("1\r");
				return waitReceive("F12=",NONE);
			case DSPJOBM:
        send("dspjob "+ARGS.job+" \r");
        /*Wait and recieve output screen*/
        waitReceive(LANG.SELECTION,GETJOBM);
        send("3\r");
        return waitReceive("F12=",NONE);
			case WRKJOBQ:
				send("wrkjobq "+ARGS.jobQ+"* \r");
				/*Wait and recieve output screen*/
				return waitReceive("===>",NONE);
			case WRKACTJOB:
				send("wrkactjob\r");
				/*Wait and recieve output screen*/
				return waitReceive("===>",NONE);
			case TOPCPUJOB:
				send("WRKACTJOB SEQ(*CPU) JOB("+ARGS.job+")\r");
				/*Wait and recieve output screen*/
				waitReceive("===>",NONE);
				//After V7R2 not necessary. send((char)27+"-");       
				//After V7R2 not necessary. waitReceive("===>",NONE);
	 			send((char)27+"0");
				return  waitReceive("AuxIO",NONE);
			case DSPFD:
	    	send("DSPFD FILE("+ARGS.fdFile+") TYPE(*ATR)\r");
	      /*Wait and receive output screen*/
	    	waitReceive("F3=",GETFD);
	    	send("+24\r");
	    	return waitReceive("F3=",NONE);
			case CHKJOBSTS:
				send("wrkactjob sbs("+ARGS.subSystem+") job("+ARGS.job+")\r");
				/*Wait and recieve output screen*/
				return waitReceive("===>",NONE);
			case CMDCLP:
				//send("CALL SJLLIB/DISKBUSY\r");
				if (ARGS.cmd_parm!=null){
				send("CALL "+ARGS.cmdCL+" "+ARGS.cmd_parm+"\r");
				/*Wait and recieve output screen*/
				return waitReceive("F20=",NONE);
			  }
			  else {
			  send("CALL "+ARGS.cmdCL+" \r");
				/*Wait and recieve output screen*/
				return waitReceive("F20=",NONE);
			  }
			case DSPDTAQD:
				send("DSPDTAQD "+ARGS.dtaqdName+" \r");
				/*Wait and recieve output screen*/
				return  waitReceive("F3=",NONE);
			case WRKDSKSTS:
				send("CHGVTMAP DOWN(*CTLD *CTLF *NXTSCR *ESCZ)\r");
				waitReceive("F3=",NONE);
				send("WRKDSKSTS\r");
				/*Wait and recieve output screen*/
				waitReceive(LANG.REQUEST_WORD,NONE);
	 			send((char)27+"-");
				return  waitReceive(LANG.DSK_STS_COMPRESSION,NONE);
			case WRKASPBRM:
				send("CHGVTMAP DOWN(*CTLD *CTLF *NXTSCR *ESCZ)\r");
				waitReceive("F3=",NONE);
				send("WRKASPBRM ASP("+ARGS.aspNums+")\r");
				/*Wait and recieve output screen*/
				waitReceive("F3=",NONE);
	 			send((char)27+"-");
				return  waitReceive("Threshold",NONE);
			case WRKSYSACT:
				send("WRKSYSACT\r");
				/*Wait and recieve output screen*/
				return waitReceive("processing",NONE);
			case WRKPRB:
				send("WRKPRB\r");
				/*Wait and recieve output screen*/
				return waitReceive("F3=",NONE);
			case DSPDGSTS:
				send("DSPDGSTS DGDFN("+ARGS.dgDef+") VIEW(*MERGED)\r");
				/*Wait and recieve output screen*/
				return waitReceive("Restart statistics",NONE);
      case DMWRKNODE:
          send("CHGCURLIB CURLIB(ICLUSTER)\r");
          waitReceive("changed to ICLUSTER",NONE);
          send("CALL QCMD \r");
          waitReceive("F3=",NONE);
          send((char)27+"0");
          waitReceive("F3=",NONE);
          send("CHGVTMAP DOWN(*CTLD *CTLF *NXTSCR *ESCZ)\r");
          waitReceive("F3=",NONE);
          send("ICLUSTER/DMWRKNODE \r");
          /*Wait and recieve output screen*/
          return waitReceive("===>",NONE);
      case DMWRKGRP:
          send("CHGCURLIB CURLIB(ICLUSTER)\r");
          waitReceive("changed to ICLUSTER",NONE);
          send("CALL QCMD \r");
          waitReceive("F3=",NONE);
          send((char)27+"0");
          waitReceive("F3=",NONE);
          send("CHGVTMAP DOWN(*CTLD *CTLF *NXTSCR *ESCZ)\r");
          waitReceive("F3=",NONE);
          send("ICLUSTER/DMWRKGRP \r");
          /*Wait and recieve output screen*/
          return waitReceive("===>",NONE);
      case DMSWTCHRDY:
          send("CHGCURLIB CURLIB(ICLUSTER)\r");
          waitReceive("changed to ICLUSTER",NONE);
          send("CALL QCMD \r");
          waitReceive("F3=",NONE);
          send((char)27+"0");
          waitReceive("F3=",NONE);
          send("CHGVTMAP DOWN(*CTLD *CTLF *NXTSCR *ESCZ)\r");
          waitReceive("F3=",NONE);
          send("ICLUSTER/DMSWTCHRDY ICGROUP("+ARGS.icGroup+") \r");
          /*Wait and recieve output screen*/
          return waitReceive("===>",NONE);
			case CMDLOGIN:
				System.out.println("OK - Login completed successfully");
				logout(OK);							
			default:
				return null;
		}

	}	
	
	public static int parse(String buffer){
		switch(ARGS.command){
			case WRKSYSSTS:
				return parseWrkSysSts(buffer);
			case WRKOUTQ:
				return parseWrkOutQ(buffer);
			case DSPMSG:
				return parseDspMsg(buffer);
			case DSPSBSD:
				return parseDspSbsD(buffer);
			case DSPJOB:
				return parseDspJob(buffer);
			case DSPJOBM:
				return parseDspJobM(buffer);
			case WRKJOBQ:
				return parseWrkJobq(buffer);
			case WRKACTJOB:
				return parseWrkActJob(buffer);
			case TOPCPUJOB:
				return parseWrkActJobTop(buffer);
			case CMDCLP:
				return parseCmdClp(buffer);
			case DSPDTAQD:
				return parseDspDtaqd(buffer);
			case WRKDSKSTS:
				return parseWrkDskSts(buffer);
			case WRKASPBRM:
				return parseWrkAspBrm(buffer);
			case WRKSYSACT:
				return parseWrkSysAct(buffer);
			case DSPDGSTS:
				return parseDspDgSts(buffer);
			case CHKJOBSTS:
				return parseChkJobSts(buffer);
			case WRKPRB:
				return parseWrkPrb(buffer);
			case DSPFD:
				return parseDspFd(buffer);
			case DMSWTCHRDY:
				return parseICSwRdySts(buffer);
			case DMWRKGRP:
				return parseICGrpSts(buffer);
			case DMWRKNODE:
				return parseICNodeSts(buffer);
			case DSPLOG:
				return parseLogSearch(buffer);				
		}
		return UNKNOWN;
	}	
	
	public static int getStatus(double val){
		int returnStatus=UNKNOWN;
	
		if(ARGS.checkVariable==CPU || ARGS.checkVariable==DB || ARGS.checkVariable==JOBS || ARGS.checkVariable==AJOBS || ARGS.checkVariable==OUTQ || ARGS.checkVariable==DBFault || ARGS.checkVariable==CMD || ARGS.checkVariable==ASP || ARGS.checkVariable==CPUC || ARGS.checkVariable==CPUT || ARGS.checkVariable==MIMIX || ARGS.checkVariable==JOBQ || ARGS.checkVariable==FDN || ARGS.checkVariable==DJOBM || ARGS.checkVariable==DTAQD || ARGS.checkVariable==CKADDR){
			if(val<ARGS.tHoldWarn){
				System.out.print("OK - ");
				returnStatus=OK;
			}
			else if(val>=ARGS.tHoldWarn && val<ARGS.tHoldCritical){
				System.out.print("WARNING - ");
				returnStatus=WARN;
			}
			else{
				System.out.print("CRITICAL - ");
				returnStatus=CRITICAL;
			}
		}
		else if(ARGS.checkVariable==US || ARGS.checkVariable==DJOB){
			if(val>ARGS.tHoldWarn){
				System.out.print("OK - ");
				returnStatus=OK;
			}
			else if(val<=ARGS.tHoldWarn && val>ARGS.tHoldCritical){
				System.out.print("WARNING - ");
				returnStatus=WARN;
			}
			else{
				System.out.print("CRITICAL - ");
				returnStatus=CRITICAL;
			}
		}
		else
			System.out.print("UNKNOWN - ");
			
		return returnStatus;
		
	}
		
	public static int parseDspMsg(String paramString) {
         if(paramString.indexOf(LANG.NO_MESSAGES_AVAILABLE)!=-1){
         		if(paramString.indexOf(LANG.NO_MESSAGES_AVAILABLE)<paramString.indexOf(LANG.MSG_NOT_NEED_REPLY)){
                 System.out.println("OK - No messages");
                 return 0;
            }
         }
         //Only display last MSG.
         int i = paramString.indexOf(LANG.MSG_NEED_REPLY, 0);
         int j = paramString.indexOf(" (", 0);
         i += 75;
         // case message line begins with (C G D F)
				 if (j<i){
    		     i = paramString.indexOf(") ", i);
    				 i += 2;
    				 j = paramString.indexOf(".", i); 
				 }

         //j -= 1;
         String str1 = paramString.substring(i, j);
         //Count MSG num 
         int p=0;
         boolean botflag=true;
         int count = 0;
         while(botflag){
           int k = paramString.indexOf("F1=", 0);
           k -= 1;    
           if(paramString.indexOf(LANG.MSG_NOT_NEED_REPLY)!=-1){
           	 botflag=false;
           	 k = paramString.indexOf(LANG.MSG_NOT_NEED_REPLY, 0);
             k -= 27;
           }
           String str3 = paramString.substring(i, k);
	         String[] str4=new String[str3.length()];
           for(int l = 0; l < str4.length;l++) {
              str4[l]=str3.substring(l,l+1);
              if(str4[l].equals("(")) {
                       count++;
              }
           }
		   		 send((char)27+"z");
		  		 paramString=waitReceive("F3=",NONE);
		  		 p++;
         }	
     
        //MSG detial
         try {
                 String str2 = new String(str1.getBytes("ISO-8859-15"), "UTF-8");
                 System.out.println(str2+" ( "+count+" MSG need reply) | msgnum="+count+";;;0; "); 
         }
         catch (UnsupportedEncodingException localUnsupportedEncodingException) {
                 System.err.println(localUnsupportedEncodingException);
         }
         return 1;
	}		

	public static int parseWrkOutQ(String buffer){
		int returnStatus=UNKNOWN;
		int start=buffer.indexOf(ARGS.outQ.toUpperCase());
		
		if (ARGS.outQ.toUpperCase().indexOf("/") != -1){
		  String[] array = ARGS.outQ.toUpperCase().split("/");
		  start=buffer.indexOf(array[1]);
		}
		
		int files=(new Integer((buffer.substring(start+26,start+38)).trim())).intValue();
		String writer=(buffer.substring(start+42,start+53)).trim(); //V6 head 44 ok
		String status=(buffer.substring(start+60,start+63)).trim(); //V6 end  63 ok 
		// nw    = Don't warn when no writer       = 1
		// ns    = Don't warn if status is 'HLD'   = 2 
		// nf    = Ignore number of files in queue = 4 
		if(writer.equals("[8;64H") || writer.equals("[8;62H")){		
			if((ARGS.outQFlags & OUTQ_NW)!=OUTQ_NW){
				System.out.print("CRITICAL - NO WRITER - ");
				returnStatus=CRITICAL;
			}
			writer="N/A";
			status=(buffer.substring(start+54,start+59)).trim();
		}

		if(returnStatus==UNKNOWN && !(status.equals("RLS"))){
			if((ARGS.outQFlags & OUTQ_NS)!=OUTQ_NS){
				System.out.print("WARNING - QUEUE NOT RELEASED - ");
				returnStatus=WARN;
			}
		}

		if(returnStatus==UNKNOWN && (ARGS.outQFlags & OUTQ_NF)!=OUTQ_NF)
			returnStatus=getStatus(files);
			
		if(returnStatus==UNKNOWN){
			System.out.print("OK - ");
			returnStatus=OK;
		}

		System.out.println("writer("+writer+") status("+status+") files("+files+")");
		
		return returnStatus;
	}

	public static int parseDspSbsD(String buffer){
		int start=0;
		int returnStatus=UNKNOWN;

		start=findToken(buffer,":",4)+1;
		String status=(buffer.substring(start,start+9)).trim();
		if(status.equals(LANG.ACTIVE)){
			System.out.print("OK - ");
			returnStatus=OK;
		}
		else{
			System.out.print("CRITICAL - ");
			returnStatus=CRITICAL;
		}

		System.out.println("subsystem("+ARGS.subSystem+") status("+status+")");

		return returnStatus;
	}
	
	public static int parseDspJob(String buffer){
		int start=0;
		int returnStatus=UNKNOWN;

		start=findToken(buffer,":",5)+1;
		String status=(buffer.substring(start,start+10)).trim();
		if(status.equals(LANG.ACTIVE)){
			System.out.print("OK - ");
			returnStatus=OK;
		}
		else{
			System.out.print("CRITICAL - ");
			returnStatus=CRITICAL;
		}

		System.out.println("Job("+ARGS.job+") status("+status+")");

		return returnStatus;
	}

  public static int parseDspJobM(String buffer){
    int start=0;
    int returnStatus=UNKNOWN; 
       
    start=findToken(buffer,":",12)+1;
    String memoryS=buffer.substring(start,start+12).trim();
    String memoryS1=memoryS.replaceAll("\\D(.*)","").trim();
    //System.out.println(memoryS1);
    int memory=Integer.parseInt(memoryS1);

    returnStatus=getStatus(memory);
    
    System.out.println("Temporary storage used by job "+ARGS.job+" : "+memory+" | swap="+memory+";"+ARGS.tHoldWarn+";"+ARGS.tHoldCritical+";0; ");
       
    return returnStatus;       
  }
	
	public static int parseDspFd(String buffer){
    int start=0;
    int returnStatus=UNKNOWN;
   
   	if(ARGS.checkVariable==FDN){
    	 start=findToken(buffer,":",2)+1;
     	double fd=(new Double(checkDouble((buffer.substring(start,start+17)).trim()))).doubleValue();
     
     	returnStatus=getStatus(fd);
     
     	System.out.println(ARGS.fdFile+" Member count "+fd+"| jobs="+fd+";"+ARGS.tHoldWarn+";"+ARGS.tHoldCritical+";0; ");
   	}
   	return returnStatus;
  }
 
	public static int parseWrkActJob(String buffer){
		int start=0;
		int returnStatus=UNKNOWN;		

		start=findToken(buffer,":",7)+1;
		if(buffer.indexOf("UTC+")!=-1){
			start=findToken(buffer,":",8)+1;  //V7R3 or UTC
		} 
		int jobs=(new Integer((buffer.substring(start,start+8)).trim())).intValue();

		returnStatus=getStatus(jobs);

		System.out.println(jobs+" active jobs in system | jobs="+jobs+";"+ARGS.tHoldWarn+";"+ARGS.tHoldCritical+";0; ");

		return returnStatus;
	}	

	public static int parseWrkActJobTop(String buffer){
		int start=0;
		int returnStatus=UNKNOWN;
		int start0=0;
		int start1=0;		
		//JOB Name
		start0=findToken(buffer,"CPU %",2)+40;
		String topjobname=(new String((buffer.substring(start0,start0+11)).trim()));
		//CPU
		start=findToken(buffer,"CPU %",2)+69;
		double topcpujob=(new Double((buffer.substring(start,start+12)).trim())).doubleValue();
		//AuxIO
		start1=findToken(buffer,"CPU %",2)+93;
		int topcpuio=(new Integer((buffer.substring(start1,start1+6)).trim())).intValue();
		
		returnStatus=getStatus(topcpujob);

		System.out.println("JOB:"+topjobname+", Total CPU Time:"+topcpujob+", AuxIO:"+topcpuio+" | cput="+topcpujob+";"+ARGS.tHoldWarn+";"+ARGS.tHoldCritical+";0; ");

		return returnStatus;
	}	

	public static int parseWrkJobq(String buffer){
		int start=0;
		int returnStatus=UNKNOWN;		
    
    if(buffer.indexOf("RLS")!=-1){
			start=findToken(buffer,"RLS",1)-30;
		}
		else if (buffer.indexOf("HLD")!=-1){
			start=findToken(buffer,"HLD",1)-30;
		}
		else{
			System.out.println(ARGS.jobQ+" job Queues *NOT FOUND* or Damaged.");
			return CRITICAL;
		}
    
		int jobQs=(new Integer((buffer.substring(start,start+12)).trim())).intValue();

		returnStatus=getStatus(jobQs);

		System.out.println(jobQs+" jobs in "+ARGS.jobQ+" job queue. | jobqs="+jobQs+";"+ARGS.tHoldWarn+";"+ARGS.tHoldCritical+";0; ");

		return returnStatus;
	}
	
	public static int parseChkJobSts(String buffer){
		int start=0;
		int returnStatus=UNKNOWN;
		
		// Priority :
		// 1st : Jobstatus
		// 2nd : Existence
		
		// Check if job is in the system
		if(buffer.indexOf(LANG.NO_JOB_TO_DISPLAY)!=-1){
			if((ARGS.JobFlags&JOBSTS_NOPERM)!=JOBSTS_NOPERM)
			{
				System.out.println("CRITICAL - No Job "+ARGS.job+" in Subsystem "+ ARGS.subSystem);
				logout(CRITICAL);
			}
			else 
			{
				System.out.println("INFORMATION - No Job "+ARGS.job+" in Subsystem "+ ARGS.subSystem);	
				logout(OK);
			}
		}
		
		start=findToken(buffer,ARGS.job,2);
		String status=(buffer.substring(start+53,start+59)).trim();
		if(status.equals(ARGS.chk_status)){
			System.out.print("OK - ");
			returnStatus=OK;
			}
		else{
			if((ARGS.JobFlags & JOBSTS_STATUS) == JOBSTS_STATUS)
			{
			 System.out.print("CRITICAL - ");
    		 returnStatus=CRITICAL;
			}
			else returnStatus=OK;
    	}
		
		if (ARGS.DEBUG){
			System.out.print("Start : "); System.out.println(start);
		}
		System.out.println("job("+ARGS.subSystem+"/"+ARGS.job+") status("+status+")");

		return returnStatus;
	}	
	
/* parse output from dsplog - jwh */
	public static int parseLogSearch(String buffer){
    int returnStatus=UNKNOWN;
		/* ok return is "(No messages available)" or "No entries exist" */
		/* critical return is anything else */
		if (buffer.indexOf(LANG.NO_MESSAGES_AVAILABLE) != -1 || buffer.indexOf(LANG.NO_ENTRIES_EXIST) != -1) {
			System.out.println("OK - Message "+ARGS.msgID+"not present");
			returnStatus = OK;
		} 
		else {
			if (ARGS.srchText != "") {
				if (buffer.indexOf(ARGS.srchText) == -1) {
				  System.out.println("OK - Message "+ARGS.msgID+" "+ARGS.srchText+"not present");
				  returnStatus = OK;
				}
			  else {
				  		returnStatus = CRITICAL;
			        System.out.println("CRITICAL - Message "+ARGS.msgID+" "+ARGS.srchText+"is present");
			  }
		  }
		}
		return returnStatus;
	}

/* parse time passed in for CKMSG - convert from epoch time to string if necessary - jwh */
	public static String parseCKMSGtime(String timevalue) {
		String parsedTime;
		long epochtime;
		SimpleDateFormat newDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
		if (timevalue.indexOf(":") != -1) {
			return timevalue;
		} else {
			epochtime = Long.parseLong(timevalue);
			Date date = new Date(epochtime*1000L);
			parsedTime = newDate.format(date);
			parsedTime = parsedTime.substring(12,19);
			return parsedTime;
		}
	}


	public static int findToken(String buffer, String token, int instance){
		int index=0,start=-1,newStart=0;
		while(index<instance){
			start=buffer.indexOf(token,start+1);
			if(start!=-1)
				index++;
			else{
				System.out.println("Parsing ERROR!");
				logout(CRITICAL);
			}
		}
		return start;
	}

	public static String checkDouble(String buffer){
		return buffer.replace(',','.');
	}
	
	public static int parseWrkSysSts(String buffer){
		int start=0;
		int returnStatus=UNKNOWN;
		NumberFormat nf=NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);

		if(ARGS.checkVariable==CPU){
			start=findToken(buffer,":",3)+1;
			if(buffer.indexOf("UTC+")!=-1){
				start=findToken(buffer,":",4)+1;  //V7R3 or UTC
			} 
			double cpu=(new Double(checkDouble((buffer.substring(start,start+11)).trim()))).doubleValue();

			returnStatus=getStatus(cpu);

			System.out.println("CPU Load ("+nf.format(cpu)+"%) | CPU="+nf.format(cpu)+";"+ARGS.tHoldWarn+";"+ARGS.tHoldCritical+";0; ");
		}
		else if(ARGS.checkVariable==DB){
			if(buffer.indexOf(LANG.DB_CAPABILITY)!=-1){
			start=findToken(buffer,":",5)+1;
			double db=(new Double(checkDouble((buffer.substring(start,start+11)).trim()))).doubleValue();

			returnStatus=getStatus(db);

			System.out.println("DB Load ("+nf.format(db)+"%) | DBload="+nf.format(db)+";"+ARGS.tHoldWarn+";"+ARGS.tHoldCritical+";0; ");
			}
                        else{
                        returnStatus=WARN;
                        System.out.println("DB Load NOT available after OS V6R1");
                        }

		}
		else if(ARGS.checkVariable==US){
			double percentFree,total,percentUsed,USwar,UScri;
			//If reach QSTGLOWLMT
      int stgcon=0;
      int aspcol=11;
      if(buffer.indexOf("Critical storage condition exists")!=-1){
      		stgcon=15;
      		aspcol=8;
      }

			start=findToken(buffer,":",10)+1+stgcon;
			if(buffer.indexOf("UTC+")!=-1){
				start=findToken(buffer,":",11)+1+stgcon;  //V7R3 or UTC
			} 
			percentUsed=(new Double(checkDouble(buffer.substring(start,start+aspcol)))).doubleValue();
			percentFree=100.0-(new Double(checkDouble(buffer.substring(start,start+aspcol)))).doubleValue();
			USwar=100-ARGS.tHoldWarn;
			UScri=100-ARGS.tHoldCritical;
			if(buffer.indexOf(LANG.DB_CAPABILITY)!=-1){
			start=findToken(buffer,":",6)+1;
			}
			else if(buffer.indexOf("UTC+")!=-1){
				start=findToken(buffer,":",9)+1; //V7R3 or UTC time
			}
      else{
        start=findToken(buffer,":",8)+1;
      }
			String tot=((buffer.substring(start,start+11))).trim();
			total=(new Double(checkDouble(tot.substring(0,tot.length()-1)))).doubleValue();

			returnStatus=getStatus(percentFree);

			System.out.println(nf.format(total*(percentFree/100))+" "+tot.substring(tot.length()-1)+" ("+nf.format(percentFree)+"%) free of "+((buffer.substring(start,start+11))).trim()+" | ASP="+nf.format(percentUsed)+"%;"+USwar+";"+UScri+";0; ");
		}
		else if(ARGS.checkVariable==JOBS){
			if(buffer.indexOf(LANG.DB_CAPABILITY)!=-1){
				start=findToken(buffer,":",11)+1;
			}
			else if(buffer.indexOf("UTC+")!=-1){
				start=findToken(buffer,":",10)+1; //V7R3 or UTC time
			}
			else{
				start=findToken(buffer,":",9)+1;
			}
			int jobs=(new Integer((buffer.substring(start,start+11)).trim())).intValue();

			returnStatus=getStatus(jobs);
			
			System.out.println(jobs+" jobs in system | jobs="+jobs+";"+ARGS.tHoldWarn+";"+ARGS.tHoldCritical+";0; ");
		}
    else if(ARGS.checkVariable==CKADDR) {
      if(ARGS.addrtype.equals("PERM")){
        start=findToken(buffer,":",11)+1;
      }
      else if(ARGS.addrtype.equals("TEMP")){
        start=findToken(buffer,":",13)+1;
      }
      else{
        if(ARGS.DEBUG) {
          System.out.println("Unknown address type provided, need PERM or TEMP");
        }
        returnStatus=UNKNOWN;
      }
      double addrs=(new Double(checkDouble((buffer.substring(start,start+11)).trim()))).doubleValue();
      returnStatus=getStatus(addrs);
      System.out.println(ARGS.addrtype+" addrs "+addrs+" % used | addrs="+addrs+"%;"+ARGS.tHoldWarn+";"+ARGS.tHoldCritical+";0; ");
    }
		
		else if(ARGS.checkVariable==DBFault){
			if(buffer.indexOf("UTC+")!=-1){
			  start=findToken(buffer,"+",6)+4; //V7R3 or UTC time
			}
			else{
			  start=findToken(buffer,"+",5)+2;
		  }
			String sDB1F=(new String((buffer.substring(start,start+6)))).trim();
			double DB1F=(new Double(checkDouble(sDB1F.substring(0,sDB1F.length())))).doubleValue();
			start=findToken(buffer,"+",5)+9;
			String sNonDB1F=(new String((buffer.substring(start,start+6)))).trim();
			double NonDB1F=(new Double(checkDouble(sNonDB1F.substring(0,sNonDB1F.length())))).doubleValue();

			//OS check
			if(buffer.indexOf(LANG.DB_CAPABILITY)!=-1){
			  start=1207;
			  String sDB2F=(new String((buffer.substring(start,start+6)))).trim();
			  double DB2F=(new Double(checkDouble(sDB2F.substring(0,sDB2F.length())))).doubleValue();
			  start=1221;
			  String sNonDB2F=(new String((buffer.substring(start,start+6)))).trim();
			  double NonDB2F=(new Double(checkDouble(sNonDB2F.substring(0,sNonDB2F.length())))).doubleValue();
			  double totDB1F=DB1F+NonDB1F;
			  returnStatus=getStatus(totDB1F);
			  System.out.println("POOL 1: "+DB1F+" / "+NonDB1F+", POOL 2: "+DB2F+" / "+NonDB2F+" (DB / Non-DB Fault) | Pool1dbf="+DB1F+";;;0; Pool1ndbf="+NonDB1F+";;;0; Pool2dbf="+DB2F+";;;0; Pool2ndbf="+NonDB2F+";;;0; ");
			}
			else{
				//DB2F
			  if(buffer.indexOf("UTC+")!=-1){
			    start=1171+36; //V7R3 or UTC time  OK
			  }
		  	else{
			    start=1171;
		    }				
			  String sDB2F=(new String((buffer.substring(start,start+8)))).trim();  //V7R2 +6
        double DB2F=(new Double(checkDouble(sDB2F.substring(0,sDB2F.length())))).doubleValue();
        //NonDB2F
			  if(buffer.indexOf("UTC+")!=-1){
			    start=1185+39; //V7R3 or UTC time
			  }
		  	else{
			    start=1185;
		    }
			  String sNonDB2F=(new String((buffer.substring(start,start+8)))).trim(); //V7R2 +6
        double NonDB2F=(new Double(checkDouble(sNonDB2F.substring(0,sNonDB2F.length())))).doubleValue();
        double totDB1F=DB1F+NonDB1F;
			  returnStatus=getStatus(totDB1F);
			  System.out.println("POOL 1: "+DB1F+" / "+NonDB1F+", POOL 2: "+DB2F+" / "+NonDB2F+" (DB / Non-DB Fault) | Pool1dbf="+DB1F+";;;0; Pool1ndbf="+NonDB1F+";;;0; Pool2dbf="+DB2F+";;;0; Pool2ndbf="+NonDB2F+";;;0; ");
			}
		}
		
		return returnStatus;
	}
	
	public static int parseCmdClp(String buffer){
		int start=0;
		int returnStatus=UNKNOWN;
		NumberFormat nf=NumberFormat.getInstance();
		nf.setMaximumFractionDigits(0);

		String CMD=ARGS.cmdCL;
		
		if (ARGS.cmdCL.toUpperCase().indexOf("/") != -1){
		  String[] array = ARGS.cmdCL.toUpperCase().split("/");
		  CMD=array[1];
		}
		
		if(CMD.equals("DISKBUSY")){
			start=findToken(buffer,"000001",1)+7;
			double num1=(new Double(checkDouble((buffer.substring(start,start+9)).trim()))).doubleValue();
			returnStatus=getStatus(num1);
			System.out.println("Disk Busy avg:("+nf.format(num1)+"%). | cnt="+nf.format(num1)+";"+ARGS.tHoldWarn+";"+ARGS.tHoldCritical+";0; ");
		}
		else if(CMD.equals("TRSCOUNT") || CMD.equals("TRSCOUNT1")){
			start=findToken(buffer,"000001",1)+7;
			Double num1=(new Double(checkDouble((buffer.substring(start,start+16)).trim()))).doubleValue();
			Double Teller1=(new Double(checkDouble((buffer.substring(start+19,start+32)).trim()))).doubleValue();
			Double MyBank1=(new Double(checkDouble((buffer.substring(start+35,start+48)).trim()))).doubleValue();
			try{
			DecimalFormat f=new java.text.DecimalFormat("");
			Number num = f.parse(buffer.substring(start,start+16).trim());
			Number Teller = f.parse(buffer.substring(start+19,start+32).trim());
			Number MyBank = f.parse(buffer.substring(start+35,start+48).trim());
			num1 = (Double)num.doubleValue();
			Teller1 = (Double)Teller.doubleValue();
			MyBank1 = (Double)MyBank.doubleValue();
      }catch(Exception e)
      {
      	System.out.println("Unable to parse date strings:" +num1);
      }
		  returnStatus=getStatus(num1);
			//System.out.println("IFX transactions: "+nf.format(num1)+", Teller: "+nf.format(Teller1)+", MyBank: "+nf.format(MyBank1)+". | cnt="+nf.format(num1)+";"+ARGS.tHoldWarn+";"+ARGS.tHoldCritical+";0; teller="+nf.format(Teller1)+";;;0; mybank="+nf.format(MyBank1)+";;;0; ");
			System.out.println("IFX transactions: "+nf.format(num1)+", Teller: "+nf.format(Teller1)+", MyBank: "+nf.format(MyBank1)+". | cnt="+num1+";"+ARGS.tHoldWarn+";"+ARGS.tHoldCritical+";0; teller="+Teller1+";;;0; mybank="+MyBank1+";;;0; ");
		}
		else if(CMD.equals("CSLOG1CLP")){
			start=findToken(buffer,"...",1)+8;
			double NTDCRAMTc=(new Double(checkDouble((buffer.substring(start,start+5)).trim()))).doubleValue();
			start=findToken(buffer,"TIMES...",1)+13;
			double NTDCRAMTt=(new Double(checkDouble((buffer.substring(start,start+6)).trim()))).doubleValue();
			returnStatus=getStatus(NTDCRAMTt);
			System.out.println("IFX NTDCRAMT Total counts:"+nf.format(NTDCRAMTc)+", Avg RepsTime:"+nf.format(NTDCRAMTt)+"ms. | cnt="+NTDCRAMTc+";;;0; Reps="+NTDCRAMTt+"ms;"+ARGS.tHoldWarn+";"+ARGS.tHoldCritical+";0; ");
		}
		else{
	    start=findToken(buffer,"000001",1)+7;
			Double num1=(new Double(checkDouble((buffer.substring(start,start+9)).trim()))).doubleValue();
			returnStatus=getStatus(num1);
			System.out.println(CMD+": ("+nf.format(num1)+"). | cnt="+nf.format(num1)+";"+ARGS.tHoldWarn+";"+ARGS.tHoldCritical+";0; ");
	  }
		return returnStatus;
	}

	public static int parseDspDtaqd(String buffer){
		int start=0;
		int returnStatus=UNKNOWN;		
		start=findToken(buffer,":",5)+1;
		int dtaqNbr=(new Integer((buffer.substring(start,start+6)).trim())).intValue();
		returnStatus=getStatus(dtaqNbr);
		System.out.println(ARGS.dtaqdName+" Data Queue Number of Entries: "+dtaqNbr+" . | num="+dtaqNbr+";"+ARGS.tHoldWarn+";"+ARGS.tHoldCritical+";0; ");
		return returnStatus;
	}

	public static int parseWrkDskSts(String buffer){
		int returnStatus=UNKNOWN;
		String failcnt="No",busycnt="No",degcnt="No",hdwcnt="No",pwlose="No";
		int countString=findStr(buffer,LANG.ACTIVE);
    boolean botflag=true;
          
		if(ARGS.checkVariable==DISK){
		  while(botflag){
		   if(buffer.indexOf("FAILED")!=-1){
				 failcnt="Yes";
		   }
		   if(buffer.indexOf("BUSY")!=-1){
				 busycnt="Yes";
		   }
		   if(buffer.matches(".*DEGRADED.*")){
				 degcnt="Yes";
		   }
		   if(buffer.matches(".*HDW FAIL.*")){
				 hdwcnt="Yes";
		   }
		   if(buffer.indexOf("PWR LOSS")!=-1){
				 pwlose="Yes";
		   }
		   if(buffer.indexOf(LANG.LIST_END)!=-1){
				 botflag=false;
		   }
		   else{
		   		 send((char)27+"z");
		  		 buffer=waitReceive("F3=",NONE);
		  		 countString=findStr(buffer,LANG.ACTIVE)+countString;
		   }
		  }
		  
		   if(failcnt=="Yes" || busycnt=="Yes" || degcnt=="Yes" || hdwcnt=="Yes" || pwlose=="Yes"){
			  	 System.out.print("CRITICAL - ");
				 returnStatus=CRITICAL;
		   }
		   else{
		  	     System.out.print("OK - ");
				 returnStatus=OK;
		   }
			System.out.println("ACTIVE:"+countString+", FAILED:"+failcnt+", BUSY:"+busycnt+", DEGRADED:"+degcnt+", HDW FAIL:"+hdwcnt+", PWR-LOSS:"+pwlose);
		}
		
		return returnStatus;
	}
	
	public static int parseWrkAspBrm(String buffer){
		int start=0;
		int returnStatus=UNKNOWN;		
		//start=findToken(buffer,"Used",1)+201;  //V5R3 - V6R1
		start=findToken(buffer,"Ovrflw",1)+130;  //V7R2
		double useds=(new Double(checkDouble((buffer.substring(start,start+5)).trim()))).doubleValue();
		returnStatus=getStatus(useds);
		 if(returnStatus==OK){
			System.out.println(useds+"% used in ASP "+ARGS.aspNums+" | asp="+useds+"%;"+ARGS.tHoldWarn+";"+ARGS.tHoldCritical+";0; ");
	     }
	     else {
	     	System.out.println(useds+"% used in ASP "+ARGS.aspNums+"! | asp="+useds+"%;"+ARGS.tHoldWarn+";"+ARGS.tHoldCritical+";0; ");
	     }
		return returnStatus;
	}	

	public static int parseWrkPrb(String buffer){
		int count=0;
		int count1=0;
		String buffer1 = buffer;
		int returnStatus=UNKNOWN;
		while(buffer1.indexOf("PREPARED")!=-1){
			 buffer1 = buffer1.substring(buffer.indexOf("PREPARED") + 8); 
       count1++;
		}
		while(buffer.indexOf("OPENED")!=-1){
			 buffer = buffer.substring(buffer.indexOf("OPENED") + 6); 
       count++; 
       returnStatus=CRITICAL;
		}
		if (count==0){returnStatus=OK;}
		System.out.println("There are "+count+" OPENED / "+count1+" PREPARED status problems. | prb="+count+";1;1;0; ");
		return returnStatus;
	}	

	public static int parseWrkSysAct(String buffer){
		int start=0;
		int returnStatus=UNKNOWN;		
		start=findToken(buffer,":",11)+1;
		double cpunums=(new Double(checkDouble((buffer.substring(start,start+9)).trim()))).doubleValue();

		send((char)27+"3");
		waitReceive("F3=",NONE);
		send("WRKSYSSTS\r");
		buffer=waitReceive("F3=",NONE);
		if(buffer.indexOf("UTC+")!=-1){
			start=findToken(buffer,":",4)+1; //V7R3 or UTC time
		}
		else{
			start=findToken(buffer,":",3)+1;
		}
		double cpus=(new Double(checkDouble((buffer.substring(start,start+11)).trim()))).doubleValue();
		returnStatus=getStatus(cpus);
		
		double capc = Double.parseDouble(ARGS.cpuNum);
		NumberFormat   nbf=NumberFormat.getInstance();
         nbf.setMinimumFractionDigits(2);
         String   truecpu   =   nbf.format(cpus*cpunums/capc);

		System.out.println("CPU Load("+cpus+"%),Capacity("+cpunums+"), True CPU Load("+truecpu+"%) | CPU="+truecpu+"%;;;0; CPUO="+cpus+"%;0;0;0; CPUS="+cpunums+"unit;"+ARGS.tHoldWarn+";"+ARGS.tHoldCritical+";0; ");
		return returnStatus;
	}
	
	public static int parseDspDgSts(String buffer){
	int start=0;
	int returnStatus=UNKNOWN;		
	start=findToken(buffer,"DB  Apply-",1)+40;
	String unprocessS = (new String((buffer.substring(start,start+12)))).trim();
	if (unprocessS.equals("")){
		unprocessS = "0";
	}
	else{
	unprocessS = unprocessS.replaceAll(",", "");
    }
	int unprocess = Integer.parseInt(unprocessS);
	
	//Find Object in error
	//int start1=0;
	//start1=findToken(buffer,":",8)+1;
	//int objerr = (new Integer((buffer.substring(start1,start1+7)).trim())).intValue();
	
	returnStatus=getStatus(unprocess);
	//if (objerr > 0){
	//	returnStatus=CRITICAL;
	//}
	//System.out.println(ARGS.dgDef+" Unprocessed Entry Count :"+unprocess+", Object error:"+objerr+" | unp="+unprocess+";1000000;2000000;0; objerr="+objerr+";1;1;0; ");
  
  String TRD="PRIMARY-A";
  if(buffer.indexOf("PRIMARY-I")!=-1){
			 System.out.print("But Transfer definition not active. ");
			 returnStatus=CRITICAL;
	}
  if(buffer.indexOf("SYNC")==-1){
  		 System.out.print("But Remote journal link inactive. ");
			 returnStatus=CRITICAL;
	}
	start=findToken(buffer,":",10)+1;
	String SyncState = (new String((buffer.substring(start,start+12)))).trim();
	
	System.out.println(ARGS.dgDef+" Unprocessed Entry Count:"+unprocess+", TRD:"+TRD+", RJL State:"+SyncState+" | unp="+unprocess+";"+ARGS.tHoldWarn+";"+ARGS.tHoldCritical+";0; ");
	return returnStatus;
  }

  public static int parseICNodeSts(String buffer){
      int returnStatus=UNKNOWN;
      String noauthrzd="No",failcnt="No",inactcnt="No",unkncnt="No";
      int i=0;    
      boolean botflag=true;
        
      if(ARGS.checkVariable==ICNODE){
         while(botflag){
        	 if(buffer.indexOf("not authorized")!=-1){
                 noauthrzd="Yes";
            }
        	if(buffer.indexOf("FAILED")!=-1){
               failcnt="Yes";
            }
            if(buffer.indexOf("INACTIVE")!=-1){
               inactcnt="Yes";
            }
            if(buffer.indexOf("UNKNOWN")!=-1){
               unkncnt="Yes";
            }
            if(buffer.indexOf(LANG.LIST_END)!=-1){
               botflag=false;
            }
            else{
               send((char)27+"z");
               buffer=waitReceive("F3=",NONE);
               i++;
            }
         }
         if(noauthrzd=="Yes" || failcnt=="Yes" || inactcnt=="Yes" || unkncnt == "Yes"){
               System.out.print("CRITICAL - ");
               returnStatus=CRITICAL;
         }
         else if(i>20){
               System.out.print("UNKNOWN - More then 20 page. Stop check.  ");
               returnStatus=UNKNOWN;
         }
         else{
               System.out.print("OK - ");
               returnStatus=OK;
         }
         if(noauthrzd=="Yes"){
        	 System.out.println("User not authorized to ICLUSTER"); 
         }
         else{
         System.out.println("FAILED:"+failcnt+", INACTIVE:"+inactcnt+", UNKNOWN:"+unkncnt);
         }
      }
      
      return returnStatus;
  }

  public static int parseICGrpSts(String buffer){
      int returnStatus=UNKNOWN;
      String noauthrzd="No",indbtcnt="No",inactcnt="No",unkncnt="No",nonecnt="No",errcnt="No",swocnt="No",befxcnt="No",stscnt="No",strjcnt="No",trgcnt="No",chkidcnt="No",constrcnt="No",chgrlcnt="No",mrkpcnt="No",aftxcnt="No",rsscnt="No",endapycnt="No";
      int i=0;    
      boolean botflag=true;
      if(ARGS.checkVariable==ICGROUP){
         while(botflag){
            if(buffer.indexOf("authorized")!=-1){
                 noauthrzd="Yes";
            }
            if(buffer.indexOf("*INDOUBT")!=-1){
               indbtcnt="Yes";
            }
            if(buffer.indexOf("*INACTIVE")!=-1){
               inactcnt="Yes";
            }
            if(buffer.indexOf("*UNKNOWN")!=-1){
               unkncnt="Yes";
            }
            if(buffer.indexOf("*NONE")!=-1){
               nonecnt="Yes";
            }
            if(buffer.indexOf("IN_ERROR")!=-1){
               errcnt="Yes";
            }              
            if(buffer.indexOf(LANG.LIST_END)!=-1){
               botflag=false;
            }
            else{
               send((char)27+"z");
               buffer=waitReceive("F3=",NONE);
               i++;
            }
         }
         if(inactcnt=="Yes"){
               System.out.print("WARNING - ");
               returnStatus=WARN;
         }
         else if(noauthrzd=="Yes" || indbtcnt=="Yes" || unkncnt == "Yes" || nonecnt == "Yes" || errcnt == "Yes"){
               System.out.print("CRITICAL - ");
               returnStatus=CRITICAL;
         }
         else if(i>20){
               System.out.print("UNKNOWN - More then 20 page. Stop check.  ");
               returnStatus=UNKNOWN;
         }
         else{
               System.out.print("OK - ");
               returnStatus=OK;
         }
         if(noauthrzd=="Yes"){
        	 System.out.println("User not authorized to ICLUSTER"); 
         }
         else{
         System.out.println("INDOUBT:"+indbtcnt+", INACTIVE:"+inactcnt+", UNKNOWN:"+unkncnt+", NONE:"+nonecnt+", IN_ERROR:"+errcnt);
         }
       }
       return returnStatus;
  }    
  
  
  public static int parseICSwRdySts(String buffer){
      int returnStatus=UNKNOWN;
      String noauthrzd="No",ntvldcnt="No",suscnt="No",ooscnt="No",latcnt="No",cmtcnt="No";
      int i=0;    
      if(ARGS.checkVariable==ICSWRDY){
    	  if(buffer.indexOf("not authorized")!=-1){
              noauthrzd="Yes";
              System.out.println("CRITICAL - User not authorized to ICLUSTER");
              returnStatus=CRITICAL;
          }
    	  if(buffer.indexOf("code 1")!=-1){
               ntvldcnt="Yes";
               System.out.println("CRITICAL - Group not valid for a roleswap.");
               returnStatus=CRITICAL;
            }
            else if(buffer.indexOf("code 2")!=-1){
               suscnt="Yes";
               System.out.println("WARNING - Group has suspended objects");
               returnStatus=WARN;
            }
            else if(buffer.indexOf("code 3")!=-1){
               ooscnt="Yes";
               System.out.println("WARNING - Group has Out Of Sync objects");
               returnStatus=WARN;
            }
            else if(buffer.indexOf("code 4")!=-1){
               latcnt="Yes";
               System.out.println("WARNING - Group latency is exceeding user defined thresholds");
               returnStatus=WARN;
            }
            else if(buffer.indexOf("code 5")!=-1){
               cmtcnt="Yes";
               System.out.println("WARNING - Group has open commitment control cycles present");
               returnStatus=WARN;
            }
            else{
               System.out.println("OK - Group is switch-ready based on user parameters");
               returnStatus=OK;
            }  
         }
        
      return returnStatus;
  }		
	
	public static boolean login(){

		//if(ARGS.DEBUG) System.out.println("  sending hello...");
		/*send hello so the server will send login screen*/
		//send((char)27+"");

		if(ARGS.DEBUG) System.out.println("  waiting for screen...");
		/*Wait for the login screen*/
		if(waitReceive("IBM CORP",NONE)!=null){
			if(ARGS.DEBUG) System.out.println("  sending login information for "+ARGS.userName+"...");
			int unameLength;
			unameLength=ARGS.userName.length();
			/*send login user/pass*/
			send(ARGS.userName);
			if (unameLength != 10) send("\t");
			send(ARGS.passWord+"\r");
			
			if(ARGS.DEBUG) System.out.println("  waiting for login to process...");
			/*Wait and recieve command screen*/
			if(waitReceive("===>",LOGIN)!=null)
				return true;
		}
		
		return false;
	}	
	
	//Recieves all info in stream until it see's the string 'str'.
	public static String waitReceive(String str,int procedure){
		String buffer=new String();
		boolean flag=true;

		if(ARGS.DEBUG) System.out.println("    waiting for token "+str+"...");

		buffer="";
		try{
			while(flag){
				int ch;
				while((ch=ioReader.read())!=-1){
					buffer=buffer+(char)ch;

					if(ioReader.ready()==false)
						break;
				}
				if(ARGS.DEBUG_PLUS) System.out.println("\n**BUFFER IS:**\n"+buffer+"\n**END OF BUFFER**\n");
				if(procedure==LOGIN){
					if(buffer.indexOf("CPF1107")!=-1){
						System.out.println("CRITICAL - Login ERROR, Invalid password");
						close();
						System.exit(CRITICAL);
					}
					else if(buffer.indexOf("CPF1120")!=-1){
						System.out.println("CRITICAL - Login ERROR, Invalid username");
						close();
						System.exit(CRITICAL);
					}
					else if(buffer.indexOf("/"+ARGS.userName.toUpperCase()+"/")!=-1){
						if(ARGS.DEBUG) System.out.println("      responding to allocated to another job message...");
						send("\r");
						buffer="";
					}
					else if(buffer.indexOf(LANG.PASSWORD_HAS_EXPIRED)!=-1){
					//else if(buffer.indexOf("Password has expired")!=-1){
						send((char)27+"3");
						//waitReceive(LANG.EXIT_SIGNON,NONE);
						waitReceive("Exit sign-on request",NONE);
						send("Y\r");
						System.out.println("WARNING - Expired password, Please change it.");
						close();
						System.exit(WARN);
					}
					else if(buffer.indexOf("LANG.PASSWORD_EXPIRES")!=-1){
					//else if(buffer.indexOf("Days until password expires")!=-1){
						send((char)27+"3");
						//waitReceive(LANG.EXIT_SIGNON,NONE);
						waitReceive("Exit sign-on request",NONE);
						send("Y\r");
						System.out.println("WARNING - Expired password, Please change it.");
						close();
						System.exit(WARN);
					}
					else if(buffer.indexOf("CPF1394")!=-1){
            System.out.println("CRITICAL - Login ERROR, User profile "+ARGS.userName+" cannot sign on.");
            close();
            System.exit(CRITICAL);
          }
					else if(buffer.indexOf(LANG.PASSWORD_EXPIRES)!=-1){
						if(ARGS.DEBUG) System.out.println("      responding to password expires message...");
						send("\r");
						buffer="";
					}
					else if(buffer.indexOf(LANG.DISPLAY_MESSAGES)!=-1){
						if(ARGS.DEBUG) System.out.println("      continuing through message display...");
						send((char)27+"3");
						buffer="";
					}
				}
				else if(procedure==GETOUTQ){
					if(buffer.indexOf(LANG.NO_OUTPUT_QUEUES)!=-1){
						System.out.println("CRITICAL - outq "+ARGS.outQ+" does NOT exist");
						logout(CRITICAL);
					}
				}
        else if(procedure==GETSBSD){
          if(buffer.indexOf(LANG.NOT_FOUND)!=-1){
            System.out.println("CRITICAL - subsystem("+ARGS.subSystem+") NOT IN SYSTEM");
            logout(CRITICAL);
          }
        }
        else if(procedure==GETFD){
          if(buffer.indexOf(LANG.NOT_FOUND)!=-1){
            System.out.println("CRITICAL - FD object ("+ARGS.fdFile+") NOT IN SYSTEM");
            logout(CRITICAL);
          }
        }
        else if(procedure==GETJOB){
          if(buffer.indexOf(LANG.DUPLICATE)!=-1){
            int countString=findStr(buffer,LANG.ACTIVE);					
            boolean botflag=true;
            while(botflag){     
            	if(buffer.indexOf(LANG.LIST_END)!=-1){
               	botflag=false;
            	}
            	else{
               	send((char)27+"z");
               	buffer=waitReceive("F3=",NONE);
               	countString=findStr(buffer,LANG.ACTIVE)+countString;
            	}
            }				
            send((char)27+"3");
            waitReceive("===>",NONE);
            int returnStatus=getStatus(countString);
            System.out.println("Active jobs("+ARGS.job+" cnt >= "+countString+")");
            logout(returnStatus);
          }
          if(buffer.indexOf(LANG.JOB)!=-1){
						System.out.println("CRITICAL - job("+ARGS.job+") NOT IN SYSTEM");
						logout(CRITICAL);
          }					
        }
        else if(procedure==GETJOBM){
          if(buffer.indexOf(LANG.DUPLICATE)!=-1){
            System.out.println("WARNING - duplicate jobs("+ARGS.job+")");
            send((char)27+"3");
            waitReceive("===>",NONE);
            logout(CRITICAL);
          }
          if(buffer.indexOf(LANG.JOB)!=-1){
            System.out.println("CRITICAL - job("+ARGS.job+") NOT IN SYSTEM");
            logout(CRITICAL);
          }
        }
        else if(procedure==CKMSG) {
			      if(buffer.indexOf(ARGS.msgID) != -1) {
				    logout(CRITICAL);
			      } else {
               logout(OK);
            }
		    }              
				//check for command not allowed errors
				if(procedure!=LOGIN){
					if(buffer.indexOf(LANG.LIBRARY_NOT_ALLOWED)!=-1){
						send((char)27+"3");
						System.out.println("CRITICAL - Command NOT allowed");
						logout(CRITICAL);
					}
				}
				if(buffer.indexOf(str)!=-1)
					flag=false;
				
			}
		}
		catch(Exception e) {	
			System.out.println("CRITICAL: Network error:"+e);
			return null;
		}

		if(ARGS.DEBUG) System.out.println("    token received.");

		return buffer;	
	}
	
	public static void logout(int exitStatus){
		//send F3
		if(ARGS.DEBUG) System.out.println("Logging out...\n  sending F3...");
		send((char)27+"3");
		waitReceive("===>",NONE);

		if(ARGS.DEBUG) System.out.println("  requesting signoff...");
		//send logout
		send("signoff *nolist\r");
		//waitReceive(";53H",NONE);
		waitReceive(LANG.LOGIN_SCREEN,NONE);
		
		if(ARGS.DEBUG) System.out.println("Job ending immediately");
		send("\r");
		waitReceive(LANG.LOGIN_SCREEN,NONE);
		
		if(ARGS.DEBUG) System.out.println("  terminating connection...");

		close();
		if(ARGS.DEBUG) System.out.println("Logged out.");
		System.exit(exitStatus);
	}	

	//open connection to server
	public static boolean open() {
		try {
			if(ARGS.SSL){
				SSLSocket sslSocket = (SSLSocket) SSLSocketFactory.getDefault()
				.createSocket(ARGS.hostName,992);
				//sslSocket.setEnabledProtocols(new String[] {"TLSv1.2"});
				ioWriter=new PrintWriter(sslSocket.getOutputStream(),true);
			  ioReader=new BufferedReader(new InputStreamReader(sslSocket.getInputStream()));
		  }
		  else{
		  	ioSocket=new Socket(ARGS.hostName,23);
		  	ioWriter=new PrintWriter(ioSocket.getOutputStream(),true);
			  ioReader=new BufferedReader(new InputStreamReader(ioSocket.getInputStream()));
		  }

			send("\n\r");

			return true;
		}
		catch(Exception e) {
			System.out.println("CRITICAL: Network error:"+e);
			return false;
		}
	}

	//close connection to server
	public static boolean close() {
		try {
			if(ARGS.SSL){
				sslSocket.close();
		  }
		  else{
		  ioSocket.close();
		  }
			return true;
		}
		catch (  NullPointerException expected){
			return true;
		}
		catch (IOException e) {
			System.out.println("CRITICAL: Network error:"+e);
			return false;
		}
	}

	//read line from stream
	public static String read(){
		String str=new String();
		try{
			str=ioReader.readLine();
		}
		catch(Exception e) {
			System.out.println("CRITICAL: Network error: "+e);
			System.exit(CRITICAL);
		}
		return str;
	}
	
	//write str to stream
	public static void send(String str){
		try{
			ioWriter.print(str);
			ioWriter.flush();
		}
		catch(Exception e) {
			System.out.println("CRITICAL: Network error: "+e);
			System.exit(CRITICAL);
		}	
	}	

	public static int findStr(String srcText, String keyword) {
		int countString = 0;
		Pattern p = Pattern.compile(keyword);
		Matcher m = p.matcher(srcText);
		while (m.find()) {
			countString++;
		}
		return countString;
	}
	
	private static SSLSocket sslSocket;
	private static Socket ioSocket;
	private static PrintWriter ioWriter;
	private static BufferedReader ioReader;
	private static check_as400_cmd_vars ARGS;
	private static check_as400_lang LANG;
	
	//These constants are for the Commands
	final static int WRKSYSSTS=0,WRKOUTQ=1,DSPMSG=2,DSPSBSD=3,DSPJOB=4,WRKACTJOB=5,CMDLOGIN=6,CMDCLP=7,WRKDSKSTS=8,WRKASPBRM=9,WRKSYSACT=10,DSPDGSTS=11,WRKJOBQ=12,CHKJOBSTS=13,DMWRKNODE=14,DMWRKGRP=15,DMSWTCHRDY=16,TOPCPUJOB=17,WRKPRB=18,DSPFD=19,DSPJOBM=20,DSPDTAQD=21,DSPLOG=22;
	//These constants are for the variables
	final static int CPU=0,DB=1,US=2,JOBS=3,MSG=4,OUTQ=5,SBS=6,DJOB=7,AJOBS=8,DBFault=9,CMD=10,DISK=11,ASP=12,CPUC=13,MIMIX=14,JOBQ=15,JOBSTS=16,ICNODE=17,ICGROUP=18,ICSWRDY=19,CPUT=20,PRB=21,FDN=22,DJOBM=23,DTAQD=24,CKMSG=25,CKADDR=26;
	//These constants are for the wait recieve, controlling
	//any other logic that it should turn on. For example checking
	//for invalid login.
	final static int NONE=0,LOGIN=1,GETOUTQ=2,GETJOB=3,GETSBSD=4,GETFD=5,GETJOBM=6;
	//Theses constants are the exit status for each error type
	final static int OK=0,WARN=1,CRITICAL=2,UNKNOWN=3;
	//These constants are used as flags on OUTQ
	final static int OUTQ_NW=1,OUTQ_NS=2,OUTQ_NF=4;
	//These constants are used as flags on JOBSTS and CMD
	final static int JOBSTS_NOPERM=1,JOBSTS_ONLYONE=2,JOBSTS_STATUS=4;
	//These constants are used as flags on CMD
	final static int CMDPARM_FLAG=1;
};
