<?php

define ('DBUSER', 'root');
define ('DBPW', '1234');
define ('DBHOST', 'localhost');
define ('DBNAME', 'ParkingTest01');

$dbc = mysqli_connect(DBHOST, DBUSER, DBPW);
if(!$dbc){
die("Database connection failed: " . mysqli_error($dbc))
exit();
}

$dbs = mysqli_select_db($dbc, DBNAME);
if(!$dbs){
die("Database selection failed: " . mysqli_error($dbc))
exit();
}

$result = mysqli_query($dbc, "SHOW COLUMS FROM CUSTOMER");
$numberOfRows = mysqli_num_rows($result);
if($numberOfRows > 0){
$values = mysqli_query($dbc, "SELECT * FROM custimer WHERE recorder_id='Fred'");
while($rowr = mysqli_fetch_row($values)){
for($j=0; $j<$numberOfRows;$j++){
$cvs_output .= $rowr[$j].", ";
}
$csv_output .= "\n";
}
}
print $csv_output;
exit;
?>