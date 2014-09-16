<?php
# This PHP for pnp4nagios templates
#
# Plugin: check_as400  (DBFault)
# Copyright (c) 2006-2008 Shao Pin, Cheng (http://cjt74392.blogspot.tw/2011/11/nagios-checkas400-plugin.html)
# 
# $Id: check_as400_dbf.php 627 2013-04-23 11:14:06Z pitchfork $
#
#
$opt[1] = "--vertical-label Per-second -l0  --title \"$hostname / $servicedesc\" ";
#
#
#
$def[1] = "";
$def[1] .= "DEF:var1=$rrdfile:$DS[1]:AVERAGE " ;
$def[1] .= "DEF:var2=$rrdfile:$DS[2]:AVERAGE " ;
$def[1] .= "DEF:var3=$rrdfile:$DS[3]:AVERAGE " ;
$def[1] .= "DEF:var4=$rrdfile:$DS[4]:AVERAGE " ;
if ($WARN[1] != "") {
    $def[1] .= "HRULE:$WARN[1]#FFFF00 ";
}
if ($CRIT[1] != "") {
    $def[1] .= "HRULE:$CRIT[1]#FF0000 ";       
}
$def[1] .= "LINE:var1#FF6600:\"Pool 1 DB Fault \" " ;
$def[1] .= "GPRINT:var1:LAST:\"\t%6.1lf last\" " ;
$def[1] .= "GPRINT:var1:AVERAGE:\"%6.1lf avg\" " ;
$def[1] .= "GPRINT:var1:MAX:\"%6.1lf max\\n\" ";
$def[1] .= "LINE:var2#FFE500:\"Pool 1 Non-DB Fault \" " ;
$def[1] .= "GPRINT:var2:LAST:\"  %6.1lf last\" " ;
$def[1] .= "GPRINT:var2:AVERAGE:\"%6.1lf avg\" " ;
$def[1] .= "GPRINT:var2:MAX:\"%6.1lf max\\n\" " ;
$def[1] .= "LINE:var3#660099:\"Pool 2 DB Fault \" " ;
$def[1] .= "GPRINT:var3:LAST:\"\t%6.1lf last\" " ;
$def[1] .= "GPRINT:var3:AVERAGE:\"%6.1lf avg\" " ;
$def[1] .= "GPRINT:var3:MAX:\"%6.1lf max\\n\" " ;
$def[1] .= "LINE:var4#D580FE:\"Pool 2 Non-DB Fault \" " ;
$def[1] .= "GPRINT:var4:LAST:\"  %6.1lf last\" " ;
$def[1] .= "GPRINT:var4:AVERAGE:\"%6.1lf avg\" " ;
$def[1] .= "GPRINT:var4:MAX:\"%6.1lf max\\n\" " ;
?>
