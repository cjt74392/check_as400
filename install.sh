#!/bin/bash
#NAGIOS_PATH=/usr/local/nagios
#NAGIOS_USER=nagios
#NAGIOS_GROUP=nagios

echo "Nagios Check_AS400 Plugin Installation Script"
echo

#DON'T MODIFY PAST THIS POINT
#-------------------------------------
#
#Lets define some functions we will need.
#
READ_NAGIOS_PATH(){
  echo -n "Please type the full path to nagios directory (ex. /usr/local/nagios): "
  read NAGIOS_PATH
  if [ ! -d $NAGIOS_PATH ]; 
  then 
    echo "That path does not seem to exist!"
    READ_NAGIOS_PATH	 
  fi
}

READ_JAVA_PATH(){
  echo -n "Please type the full path to your java executable (ex. /usr/bin/java): "
  read JAVA_PATH
  if [ ! -x $JAVA_PATH ];
  then
    echo "That does not seem to exist or be executable!"
    READ_JAVA_PATH
  fi
}

DETECT_NAGIOS_OWNER_GROUP(){
  echo
  if [ -e $NAGIOS_PATH/etc/nagios.cfg ];
  then
    NAGIOS_USER=`cat $NAGIOS_PATH/etc/nagios.cfg |grep -e nagios_user | cut -d= -f2`
    NAGIOS_GROUP=`cat $NAGIOS_PATH/etc/nagios.cfg |grep -e nagios_group | cut -d= -f2`
    if [ $NAGIOS_USER ]; 
    then
      if [ $NAGIOS_GROUP ]; 
      then
        echo "Detected nagios user as '$NAGIOS_USER' and the group as '$NAGIOS_GROUP'..."
      else
	echo
        echo "ERROR: Unable to detect your nagios user and group. "
        echo "Is your $NAGIOS_PATH/etc/nagios.cfg properly setup?"
        exit 1
      fi 
    else
      echo
      echo "ERROR: Unable to detect your nagios user and group. "
      echo "Is your $NAGIOS_PATH/etc/nagios.cfg properly setup?"
      exit 1
    fi
  else
    echo
    echo "ERROR: Your $NAGIOS_PATH/etc/nagios.cfg file does not seem to exist!"
    exit 1
  fi
}

READ_NAGIOS_PATH
READ_JAVA_PATH
DETECT_NAGIOS_OWNER_GROUP

##!/bin/sh
#NAGIOS_PATH=`dirname $0`
#USER=`cat $NAGIOS_PATH/.as400 |grep -e USER | cut -d = -f 2`
#PASS=`cat $NAGIOS_PATH/.as400 |grep -e PASS | cut -d = -f 2`
#JAVA_START="/usr/lib/java/bin/java -cp $NAGIOS_PATH"

#$JAVA_START check_as400 -u $USER -p $PASS $*

echo "Generating check_as400 script based on your paths..."
echo "USER=\`cat $NAGIOS_PATH/libexec/.as400 |grep -e USER | cut -d = -f 2\`" >check_as400
echo "PASS=\`cat $NAGIOS_PATH/libexec/.as400 |grep -e PASS | cut -d = -f 2\`" >>check_as400
echo "$JAVA_PATH -cp $NAGIOS_PATH/libexec check_as400 -u \$USER -p \$PASS \$*" >>check_as400
chmod 744 check_as400

echo "Installing java classes..."
cp *.class $NAGIOS_PATH/libexec
echo "Installing check script..."
cp check_as400 $NAGIOS_PATH/libexec
if [ ! -e $NAGIOS_PATH/libexec/.as400 ] ;
then
  echo "Installing .as400 security file..."
  cp example/example.as400 $NAGIOS_PATH/libexec/.as400
  chmod 700 $NAGIOS_PATH/libexec/.as400
fi

echo "Setting permissions..."
cd $NAGIOS_PATH/libexec
chown $NAGIOS_USER:$NAGIOS_GROUP check_as400.class check_as400_cmd_vars.class check_as400_lang.class check_as400 .as400
RESULT=$?
if [ $RESULT -eq 1 ];
then
  echo 
  echo "ERROR: Unable to set permissions on the files in $NAGIOS_PATH/libexec!"
  echo "Check to make sure they have proper owner and group permissions "
  echo "before using the plugin!"
fi

echo
echo "Install Complete!"
echo
echo " !!!!! Be sure and modify your $NAGIOS_PATH/libexec/.as400 "
echo " !!!!! with the correct user and password.        "
echo
echo "Also add the contents of the checkcommands.example file"
echo "into your $NAGIOS/etc/checkcommands.cfg"
echo
 
