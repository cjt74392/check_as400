# check_as400
Nagios plugin to monitor IBM System i (AS/400)

      AJ                = Number of active jobs in system.
      CJ <job> [-w -c]  = Check to see if job <job> is in the system.[Number of ACTIVE <job>]
      CJS <sbs> <job> [status <STATUS>] [noperm]
                        = Check to see if job is existing in Subsystem and has this status.
                          Job checking can be controlled by :
                          status <status>       = any other status goes to critical
                          noperm                = don't go to critical if job is not in the system
                          NOTE: if JobStatus is set, it has highest Priority
      JOBS              = Number of jobs in system.
      JOBQ <lib/jobq>   = Number of jobs in JOBQ.
      CPU               = CPU load.
      CPUC <cpuBase>    = CPU load, Consider Current processing capacity. (CPU used * VP nums / cpuBase).
                          NOTE: Specify <cpuBase>, EX: You want use 3 CPU only, but VP use more than 3.
      CPUT <job>        = Top CPU used job. The total processing unit time used by the job
                          Specify job name, ex: *ALL or QZ* or QZDASOINIT
      US                = Percent free storage
      ASP <aspNum>      = Check ASP <aspNum> used
      DISK              = Check DISK Status.
      DB                = DB utilization. (Not available after V6R1)
      DBFault           = Pool DB/Non-DB Fault
      LOGIN             = Check if login completes.
      MSG <user>        = Check for any unanswered messages on msg queue <user>
                          Any unanswered messages causes warning status.
      OUTQ <queue>      = Check outq files, writer and status. No writer, or
                          status of 'HLD' causes warning status. This default
                          behavior can be modified with the following options:
                             nw    = Don't go critical when no writer
                             ns    = Don't warn if status is 'HLD'
                             nf    = Ignore number of files in queue
                          NOTE: threshold values are used on number of files
      SBS <subsystem>   = Check if the subsystem <subsystem> is running.
                          NOTE: specify <subsystem> as library/subsystem
      PRB               = Check if the problem was identified.
      FDN               = Number of file members; specify library/filename
      ---------- VISION MIMIX ----------
      MIMIX <DG name>   = Check MIMIX Data Group Unprocessed Entry Count, Transfer definition, RJ link state.
      ---------- Rocket iCluster ----------
      ICNODE            = Check for any Inactive or Failed Node status.
      ICGROUP           = Check for any Inactive or Indoubt Group status.
      ICSWTCHRDY <grp>  = Check for multiple conditions for switch readiness.

========================================================================
Install Note 
========================================================================
* Modify your $NAGIOS_PATH/.as400 with the correct user and password.(User name <= 9 characters)
* Modify chech_as400 with the correct java path. 
* Set NAGIOS user profile Display sign-on information *NO 
- CHGUSRPRF USRPRF(NAGIOS) DSPSGNINF(*NO) 
Initial program to call  . . . .   *NONE
Initial menu . . . . . . . . . .   MAIN
* For languages other than English , change system library list to ENGLISH.
* Nagios user may need to use IBM default sign-on display file
   Or change nagiso user to other subsystem description to use system default display file(QSYS/QDSIGNON)
* Avoid the massive entries log
CHGJOBD JOBD(NAGIOSJOBD) LOG(4 00 *MSG)
*Use SSL connection
 Must add CE to JAVA, EX:
keytool -import -trustcacerts -keystore /usr/lib/jvm/java-1.7.0-openjdk-1.7.0.85.x86_64/jre/lib/security/cacerts -storepass changeit -noprompt -alias xxxx_ce -file /xx/xxx/XXXX.cer


Preinstall notes
-------------------

* Security Note: Realize that this plugin communicates to the AS400 via telnet, which is easy to sniff and capture user names and passwords.  Use a generic user with restrictive rights for the plugin. The user needs access to wrksyssts, wrkoutq, wrkactjob, dspjob, dspsbsd and dspmsg.

Quick And Easy
-----------------
1) For languages other than English you will need to recompile the plugin before continuing.  Refer to steps 1 and 2 of the Manual installation section below.
 * Type
   ./install

2) Modify your $NAGIOS_PATH/libexec/.as400 with the correct user and password
3) Add the contents of the checkcommands.example file into your $NAGIOS/etc/checkcommands.cfg
4) Add services to your nagios configuration.  There are examples in the services.example file.
5) Add dependencies to your nagios configuration.  There are examples in the dependency.example file.
6) DONE!

Manual Installation
-------------------
1) For languages other than English ,  change system library list to ENGLISH
     <1>.Login AS/400 Nagios check ID.
     <2>.CHGSYSLIBL LIB(QSYS2984) OPTION(*ADD)
     <3>Logout

    <Option> If you still want to use other languages, you will need to create and recompile  the language before continuing.
        <1>.rm check_as400_lang.java
        <2>.ln -s langs/check_as400_lang_(your language).java .
        <3>.javac check_as400_lang.java

2) Modify chech_as400 with the correct java path.

3) <Option>Compile the plugin:
         javac check_as400.java

4) Copy all .class files to nagios/libexec
         cp *.class /usr/local/nagios/libexec

5) EDIT and Copy the check_as400 script to nagios/libexec
         cp check_as400 /usr/local/nagios/libexec
         vi /usr/local/nagios/libexec/check_as400
   * Make sure you modify the JAVA_START path in the check_as400 script.

6) Create a file in /usr/local/nagios/libexec called .as400, with contents matching that of he file example.as400.  This file contains the user name and password used to login to the AS/400.

7) Change the user and group of these files to your nagios user:
        cd /usr/local/nagios/libexec
        chown nagios:nagios *.class check_as400 .as400
        chmod 700 .as400
   * Security Note! Set the permissions of the /usr/local/nagios/libexec/.as400 file.  This way only the nagios user can read the contents of this file.

8) Add Nagios configuration
   * Add the contents of checkcommands.example to your Nagios checkcommands.cfg
   * Add services to your nagios configuration.  There are examples in the services.example file.
   * Add dependencies to your nagios configuration.  There are examples in the dependency.example file.

9) Set AS/400 NAGIOS user profile Display sign-on information *NO
CHGUSRPRF USRPRF(NAGIOS) DSPSGNINF(*NO)

Enjoy!
