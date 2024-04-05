<?php
include_once("connect.php");

$id = $_GET['id']; 

$sqlDeleteMessage = "DELETE FROM tblmessage WHERE messageID=$id";
mysqli_query($connection, $sqlDeleteMessage);

$_SESSION['userid'] = $_GET['uid'];
$_SESSION['username'] = $_GET['uname'];
header('Location: logged_in.php');
// ?>