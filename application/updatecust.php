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

$FirstName = mysqli_real_escape_string($dbc, $_GET['FirstName']);
$LastName = mysqli_real_escape_string($dbc, $_GET['LastName']);
$Email = mysqli_real_escape_string($dbc, $_GET['Email']);
$CarId = mysqli_real_escape_string($dbc, $_GET['CarId']);
$StudentId = mysqli_real_escape_string($dbc, $_GET['StudentId']);

$query = "UPDATE customer SET first_name='$FirstName', last_name='$LastName', 
email='$Email', car_id='$CarId' WHERE cust_id='$StudentId'";

$result = mysqli_query($dbc, $query) or trigger_error("Query MySQL Error: " .
mysqli_error($dbc));

mysqli_close($dbc);

?>