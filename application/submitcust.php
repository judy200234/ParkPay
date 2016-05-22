<?php

define ('DBUSER', 'root');
define ('DBPW', '1234');
define ('DBHOST', 'localhost');
define ('DBNAME', 'ParkingTest01');

$dbc = mysqli_connect(DBHOST, DBUSER, DBPW) or die("Database connection failed: " . mysqli_error($dbc));

$dbs = mysqli_select_db($dbc, DBNAME)or die("Database selection failed: " . mysqli_error($dbc));

$StudentId = mysqli_real_escape_string($dbc, $_GET['StudentId']);
$FirstName = mysqli_real_escape_string($dbc, $_GET['FirstName']);
$LastName = mysqli_real_escape_string($dbc, $_GET['LastName']);
$Email = mysqli_real_escape_string($dbc, $_GET['Email']);
$CarId = mysqli_real_escape_string($dbc, $_GET['CarId']);

$query = "INSERT INTO app (`cust_id`, `first_name`, `last_name`, `email`, `car_id`) 
          VALUES ('$FirstName', '$LastName', '$Email', '$CarId', '$StudentId')";

$result = mysqli_query($dbc, $query) or trigger_error("Query MySQL Error: " . mysqli_error($dbc));

mysqli_close($dbc);

?>