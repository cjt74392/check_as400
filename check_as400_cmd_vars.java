//Nagios Plugin to check an IBM AS/400
//
//Developed June 2003
//Last Modified May 13 2013
//
//This class is used as a varible structure
//holding parsed command line arguments
//

public class check_as400_cmd_vars{
        //These hold the basic information we need to logon
        public String hostName,userName,passWord;

        //These store specific information on a requested operation
        public String outQ,msgUser,subSystem,job,aspNums,cpuNum,dgDef,jobQ,status,chk_status,icNode,icGroup,cmdCL,fdFile,cmd_parm,dtaqdName,startTime,msgID,srchText,addrtype;

        //These keep track of what we are suppose to be checking!
        public int checkVariable,command,outQFlags=0,JobFlags;

        //these two hold the warn and critical values
        public double tHoldWarn,tHoldCritical;

        //And of course everybody needs debug variables for when
        //things get ugly!
        public boolean DEBUG=false,DEBUG_PLUS=false,SSL=false;
};

