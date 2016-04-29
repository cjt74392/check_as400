AS400 Nagios Plugin - Installation instructions
----------------------------------------------------

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

	(optional 1 - Support EN/FR/DE/IT)
	1.If your system is running on another language than english, follow the steps below
	# cd /usr/local/nagios/libexec/check_as400/
	2.Then copy the java class file of your language (EX:substitute french with your language)
	# cp check_as400_lang_french.java ../check_as400_lang.java
	3.Then compile it
	# javac check_as400_lang.java
	
	(optional 2 - Change Nagios user profile to EN)
	The NAGIOS user profile has INLPGM parameter set to a CLP (MAINGBCL)
	This is the MAINGBCL: (This CLP change the language in English)
	
	/*********************************************************************/ 
	/* SERVER PROFILE FOR NAGIOS !!!!!!!!!                                                               */
	/*********************************************************************/ 
	PGM                                                                                                                                   
	             CHGSYSLIBL LIB(QSYS2924)                                                                         
	             RCLRSC                                                                                                               
	ENDPGM                                                                                                                            

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

If you have suggestions or would like to submit bugs,  email me at cjt74392@ms10.hinet.net 
